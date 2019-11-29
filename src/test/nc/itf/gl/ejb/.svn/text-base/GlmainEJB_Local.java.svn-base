package nc.itf.gl.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.gl.accbookprint.PrintInfoAllVO;
import nc.vo.gl.accbookprint.PrintdealclassVO;
import nc.vo.gl.accbookprint.PrintitemVO;
import nc.vo.gl.accbookprint.QueryconditionVO;
import nc.vo.gl.balancebooks.BalanceResultVO;
import nc.vo.gl.build.InitbuildVO;
import nc.vo.gl.commonvoucher.CommonvoucherVO;
import nc.vo.gl.detailbooks.DetailResultVO;
import nc.vo.gl.glreport.VerifyResultVO;
import nc.vo.gl.glreport.VeryfyQeuryVO;
import nc.vo.gl.map.DocmaptempletVO;
import nc.vo.gl.multibooks.MultiColFormatVO;
import nc.vo.gl.multibooks.MultiFormatVO;
import nc.vo.gl.primevoucher.PrimebillVO;
import nc.vo.gl.pubvoucher.DetailVO;
import nc.vo.gl.pubvoucher.GLParameterVO;
import nc.vo.gl.pubvoucher.OperationResultVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.gl.querymodel.CacheRotateCrossVO;
import nc.vo.gl.querymodel.CondtionVO;
import nc.vo.gl.querymodel.ExtendreportBVO;
import nc.vo.gl.querymodel.ExtendreportPropVO;
import nc.vo.gl.querymodel.ExtendreportRuleVO;
import nc.vo.gl.querymodel.ReportConfigTableVO;
import nc.vo.gl.querymodel.ReportConfigVO;
import nc.vo.gl.querymodel.ReportInfoVO;
import nc.vo.gl.uicfg.UIConfigVO;
import nc.vo.gl.voucher.VoucherCheckConfigVO;
import nc.vo.gl.voucher.VoucherDataPreLoadVO;
import nc.vo.gl.voucher.VouchermodeVO;
import nc.vo.gl.voucheradjust.VoucherAdjustVO;
import nc.vo.gl.voucherquery.VoucherQueryConditionVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.balance.GlAssVO;
import nc.vo.glcom.balance.GlBalanceVO;
import nc.vo.glcom.balance.GlDetailVO;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.glcom.balance.SubjAssembleQryVO;
import nc.vo.glcom.balance.SubjAssembleVO;
import nc.vo.glcom.exception.GLBusinessException;
import nc.vo.glcom.glperiod.GlPeriodVO;
import nc.vo.glcom.query.SmallVoucherVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class GlmainEJB_Local extends BeanBase
  implements GlmainEJBEjbObject
{
  private GlmainEJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (GlmainEJBEjbBean)getEjb();
  }

  public void insert(ReportInfoVO arg0) throws BusinessException {
    beforeCallMethod(200);
    Exception er = null;
    try {
      _getBeanObject().insert(arg0);
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

  public ExtendreportRuleVO[] getLevels(ExtendreportRuleVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(201);
    Exception er = null;
    ExtendreportRuleVO[] o = null;
    try {
      o = (ExtendreportRuleVO[])_getBeanObject().getLevels(arg0, arg1);
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
  public String[] insertArray(ReportConfigVO[] arg0) throws BusinessException {
    beforeCallMethod(202);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray(arg0);
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
  public nc.vo.gl.querymodel.ExtendreportItemVO[] queryAll() throws BusinessException {
    beforeCallMethod(203);
    Exception er = null;
    nc.vo.gl.querymodel.ExtendreportItemVO[] o = null;
    try {
      o = (nc.vo.gl.querymodel.ExtendreportItemVO[])_getBeanObject().queryAll();
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
  public String[] getAccsubjCode(ExtendreportRuleVO[] arg0) throws BusinessException {
    beforeCallMethod(204);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAccsubjCode(arg0);
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
    return o;
  }
  public ExtendreportRuleVO[] getAccsubjRuleVO(ExtendreportRuleVO[] arg0) throws BusinessException {
    beforeCallMethod(205);
    Exception er = null;
    ExtendreportRuleVO[] o = null;
    try {
      o = (ExtendreportRuleVO[])_getBeanObject().getAccsubjRuleVO(arg0);
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
  public ExtendreportBVO[] getAccsubjVO(String arg0) throws BusinessException {
    beforeCallMethod(206);
    Exception er = null;
    ExtendreportBVO[] o = null;
    try {
      o = (ExtendreportBVO[])_getBeanObject().getAccsubjVO(arg0);
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
  public String getDataObjectOid() throws BusinessException {
    beforeCallMethod(207);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getDataObjectOid();
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
  public ExtendreportBVO[] getExtendreportBVO(String arg0) throws BusinessException {
    beforeCallMethod(208);
    Exception er = null;
    ExtendreportBVO[] o = null;
    try {
      o = (ExtendreportBVO[])_getBeanObject().getExtendreportBVO(arg0);
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
  public nc.vo.gl.querymodel.ExtendreportVO getExtendReportVO(CondtionVO arg0) throws BusinessException {
    beforeCallMethod(209);
    Exception er = null;
    nc.vo.gl.querymodel.ExtendreportVO o = null;
    try {
      o = _getBeanObject().getExtendReportVO(arg0);
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
  public nc.vo.gl.querymodel.ExtendreportItemVO[] getItemPropConTableObject(ExtendreportBVO[] arg0, ExtendreportRuleVO[] arg1) throws BusinessException {
    beforeCallMethod(210);
    Exception er = null;
    nc.vo.gl.querymodel.ExtendreportItemVO[] o = null;
    try {
      o = (nc.vo.gl.querymodel.ExtendreportItemVO[])_getBeanObject().getItemPropConTableObject(arg0, arg1);
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
  public ExtendreportRuleVO[] getAccsubjLevels(ExtendreportRuleVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(211);
    Exception er = null;
    ExtendreportRuleVO[] o = null;
    try {
      o = (ExtendreportRuleVO[])_getBeanObject().getAccsubjLevels(arg0, arg1);
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
  public ExtendreportPropVO[] queryProp(String arg0) throws BusinessException {
    beforeCallMethod(212);
    Exception er = null;
    ExtendreportPropVO[] o = null;
    try {
      o = (ExtendreportPropVO[])_getBeanObject().queryProp(arg0);
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
    return o;
  }
  public ExtendreportPropVO[] queryPropss(String arg0) throws BusinessException {
    beforeCallMethod(213);
    Exception er = null;
    ExtendreportPropVO[] o = null;
    try {
      o = (ExtendreportPropVO[])_getBeanObject().queryPropss(arg0);
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
  public ExtendreportRuleVO[] queryRules(String arg0) throws BusinessException {
    beforeCallMethod(214);
    Exception er = null;
    ExtendreportRuleVO[] o = null;
    try {
      o = (ExtendreportRuleVO[])_getBeanObject().queryRules(arg0);
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
  public CacheRotateCrossVO queryTableData(String arg0) throws BusinessException {
    beforeCallMethod(215);
    Exception er = null;
    CacheRotateCrossVO o = null;
    try {
      o = _getBeanObject().queryTableData(arg0);
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
  public ReportConfigTableVO[] queryTableData() throws BusinessException {
    beforeCallMethod(216);
    Exception er = null;
    ReportConfigTableVO[] o = null;
    try {
      o = (ReportConfigTableVO[])_getBeanObject().queryTableData();
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
  public Vector getMemoryResultSet(CondtionVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(217);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getMemoryResultSet(arg0, arg1, arg2);
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
  public HashMap getSubjCodeToName(String arg0) throws BusinessException {
    beforeCallMethod(218);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getSubjCodeToName(arg0);
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
    return o;
  }
  public Vector GetResultSet(CondtionVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(219);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().GetResultSet(arg0, arg1, arg2);
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
  public ExtendreportRuleVO[] getSubjLevels(ExtendreportRuleVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(220);
    Exception er = null;
    ExtendreportRuleVO[] o = null;
    try {
      o = (ExtendreportRuleVO[])_getBeanObject().getSubjLevels(arg0, arg1);
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
  public BalanceResultVO getBalance(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(221);
    Exception er = null;
    BalanceResultVO o = null;
    try {
      o = _getBeanObject().getBalance(arg0);
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
  public GlPeriodVO getGlPeriod(UFDate arg0) throws BusinessException {
    beforeCallMethod(222);
    Exception er = null;
    GlPeriodVO o = null;
    try {
      o = _getBeanObject().getGlPeriod(arg0);
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
  public GlBalanceVO[] getOccur(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(223);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getOccur(arg0);
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
    return o;
  }
  public GlBalanceVO[] getEndBalance(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(224);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getEndBalance(arg0);
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
  public GlBalanceVO[] getBeginBalance(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(225);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getBeginBalance(arg0);
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
  public GlDetailVO[] getDetailBook(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(226);
    Exception er = null;
    GlDetailVO[] o = null;
    try {
      o = (GlDetailVO[])_getBeanObject().getDetailBook(arg0);
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
  public GlBalanceVO[] getAccountDetail(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(227);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getAccountDetail(arg0);
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
  public SubjAssembleVO[] getAssembleFromVoucher(SubjAssembleQryVO arg0) throws BusinessException {
    beforeCallMethod(228);
    Exception er = null;
    SubjAssembleVO[] o = null;
    try {
      o = (SubjAssembleVO[])_getBeanObject().getAssembleFromVoucher(arg0);
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
    return o;
  }
  public AccsubjVO[] getCarryOverSubjs(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(229);
    Exception er = null;
    AccsubjVO[] o = null;
    try {
      o = (AccsubjVO[])_getBeanObject().getCarryOverSubjs(arg0, arg1, arg2);
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
  public BalanceResultVO getDayBalance(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(230);
    Exception er = null;
    BalanceResultVO o = null;
    try {
      o = _getBeanObject().getDayBalance(arg0);
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
  public DetailResultVO getDetailBooksData(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(231);
    Exception er = null;
    DetailResultVO o = null;
    try {
      o = _getBeanObject().getDetailBooksData(arg0);
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
  public GlBalanceVO[] getOccurByRtVoucherPk(String[] arg0, String[] arg1, String arg2, int[] arg3) throws BusinessException {
    beforeCallMethod(232);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getOccurByRtVoucherPk(arg0, arg1, arg2, arg3);
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
  public GlBalanceVO[] getOccurByVoucherPk(String[] arg0, String[] arg1, String arg2, int[] arg3) throws BusinessException {
    beforeCallMethod(233);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getOccurByVoucherPk(arg0, arg1, arg2, arg3);
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
  public String[][] getOccurSubjs(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(234);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getOccurSubjs(arg0);
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
  public GlBalanceVO[] getSumBigAmount(GlQueryVO arg0) throws BusinessException {
    beforeCallMethod(235);
    Exception er = null;
    GlBalanceVO[] o = null;
    try {
      o = (GlBalanceVO[])_getBeanObject().getSumBigAmount(arg0);
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
  public String[] isBalanceDetailEqual(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(236);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().isBalanceDetailEqual(arg0, arg1, arg2);
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
  public Boolean recalculateBalance(String arg0) throws BusinessException {
    beforeCallMethod(237);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().recalculateBalance(arg0);
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
  public String[] getVoucherScope(SubjAssembleQryVO arg0) throws BusinessException {
    beforeCallMethod(238);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getVoucherScope(arg0);
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
  public void delete(CommonvoucherVO arg0) throws BusinessException {
    beforeCallMethod(239);
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
  }

  public String insert(CommonvoucherVO arg0) throws BusinessException {
    beforeCallMethod(240);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
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
  public CommonvoucherVO[] query(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(241);
    Exception er = null;
    CommonvoucherVO[] o = null;
    try {
      o = (CommonvoucherVO[])_getBeanObject().query(arg0, arg1);
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
  public void update(CommonvoucherVO arg0) throws BusinessException {
    beforeCallMethod(242);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
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
  }

  public CommonvoucherVO[] queryByVO(CommonvoucherVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(243);
    Exception er = null;
    CommonvoucherVO[] o = null;
    try {
      o = (CommonvoucherVO[])_getBeanObject().queryByVO(arg0, arg1);
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
  public String[] insertArray(CommonvoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(244);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray(arg0);
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
    return o;
  }
  public CommonvoucherVO[] queryAllCommonvoucherVOs(String arg0) throws BusinessException {
    beforeCallMethod(245);
    Exception er = null;
    CommonvoucherVO[] o = null;
    try {
      o = (CommonvoucherVO[])_getBeanObject().queryAllCommonvoucherVOs(arg0);
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
  public CommonvoucherVO findByPKCommonvoucherVO(String arg0) throws BusinessException {
    beforeCallMethod(246);
    Exception er = null;
    CommonvoucherVO o = null;
    try {
      o = _getBeanObject().findByPKCommonvoucherVO(arg0);
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
  public void deleteByPK(CommonvoucherVO arg0) throws BusinessException {
    beforeCallMethod(247);
    Exception er = null;
    try {
      _getBeanObject().deleteByPK(arg0);
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
  }

  public void distribute(CommonvoucherVO arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(248);
    Exception er = null;
    try {
      _getBeanObject().distribute(arg0, arg1);
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

  public CommonvoucherVO saveCommonvoucher(CommonvoucherVO arg0) throws BusinessException {
    beforeCallMethod(249);
    Exception er = null;
    CommonvoucherVO o = null;
    try {
      o = _getBeanObject().saveCommonvoucher(arg0);
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
  public void delete(DetailVO arg0) throws BusinessException {
    beforeCallMethod(250);
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

  public String insert(DetailVO arg0) throws BusinessException {
    beforeCallMethod(251);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
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
  public void update(DetailVO arg0) throws BusinessException {
    beforeCallMethod(252);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
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

  public String[] insertArray(DetailVO[] arg0) throws BusinessException {
    beforeCallMethod(253);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray(arg0);
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
  public DetailVO findByPKDetailVO(String arg0) throws BusinessException {
    beforeCallMethod(254);
    Exception er = null;
    DetailVO o = null;
    try {
      o = _getBeanObject().findByPKDetailVO(arg0);
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
  public void delete(nc.vo.gl.extendreport.ExtendreportVO arg0) throws BusinessException {
    beforeCallMethod(255);
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

  public void insert(nc.vo.gl.extendreport.ExtendreportVO arg0) throws BusinessException {
    beforeCallMethod(256);
    Exception er = null;
    try {
      _getBeanObject().insert(arg0);
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

  public void update(nc.vo.gl.extendreport.ExtendreportVO arg0) throws BusinessException {
    beforeCallMethod(257);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
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
  }

  public nc.vo.gl.extendreport.ExtendreportVO[] queryAll(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(258);
    Exception er = null;
    nc.vo.gl.extendreport.ExtendreportVO[] o = null;
    try {
      o = (nc.vo.gl.extendreport.ExtendreportVO[])_getBeanObject().queryAll(arg0, arg1);
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
  public Boolean isCodeNameRepeat(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(259);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().isCodeNameRepeat(arg0, arg1, arg2, arg3);
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
  public nc.vo.gl.extendreport.ExtendreportItemVO[] queryAllItem(String arg0) throws BusinessException {
    beforeCallMethod(260);
    Exception er = null;
    nc.vo.gl.extendreport.ExtendreportItemVO[] o = null;
    try {
      o = (nc.vo.gl.extendreport.ExtendreportItemVO[])_getBeanObject().queryAllItem(arg0);
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
  public nc.vo.sm.UserVO[] queryUserByFuncCode(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(261);
    Exception er = null;
    nc.vo.sm.UserVO[] o = null;
    try {
      o = (nc.vo.sm.UserVO[])_getBeanObject().queryUserByFuncCode(arg0, arg1);
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
  public void sysBdInfo() throws BusinessException {
    beforeCallMethod(262);
    Exception er = null;
    try {
      _getBeanObject().sysBdInfo();
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

  public String getAssID(AssVO[] arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(263);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getAssID(arg0, arg1);
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
  public String[] getAssIDs(AssVO[] arg0) throws BusinessException {
    beforeCallMethod(264);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAssIDs(arg0);
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
  public GlAssVO[] queryAllByIDs(String[] arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(265);
    Exception er = null;
    GlAssVO[] o = null;
    try {
      o = (GlAssVO[])_getBeanObject().queryAllByIDs(arg0, arg1);
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
  public AssVO[] queryAssvosByid(String arg0) throws BusinessException {
    beforeCallMethod(266);
    Exception er = null;
    AssVO[] o = null;
    try {
      o = (AssVO[])_getBeanObject().queryAssvosByid(arg0);
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
  public void beforeOperate(String arg0, int arg1, String arg2, String arg3, Object arg4) throws BusinessException {
    beforeCallMethod(267);
    Exception er = null;
    try {
      _getBeanObject().beforeOperate(arg0, arg1, arg2, arg3, arg4);
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

  public void afterOperate(String arg0, int arg1, String arg2, String arg3, Object arg4) throws BusinessException {
    beforeCallMethod(268);
    Exception er = null;
    try {
      _getBeanObject().afterOperate(arg0, arg1, arg2, arg3, arg4);
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
  }

  public void deleteUnusedAssid() throws BusinessException {
    beforeCallMethod(269);
    Exception er = null;
    try {
      _getBeanObject().deleteUnusedAssid();
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
  }

  public String[] getAllsubPkByValuePK(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(270);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAllsubPkByValuePK(arg0, arg1, arg2);
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
  public String[] getCodeNameByValuePk(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(271);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getCodeNameByValuePk(arg0, arg1);
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
  public String[] getAssSigleIDs(ArrayList arg0) throws BusinessException {
    beforeCallMethod(272);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAssSigleIDs(arg0);
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
  public String getValuePkByValueCode(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(273);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getValuePkByValueCode(arg0, arg1, arg2);
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
  public void getValuePkByValueCode(BdinfoVO arg0, String arg1, ArrayList arg2, Hashtable arg3) throws BusinessException {
    beforeCallMethod(274);
    Exception er = null;
    try {
      _getBeanObject().getValuePkByValueCode(arg0, arg1, arg2, arg3);
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
  }

  public String getCorpSpeciallizedID(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(275);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getCorpSpeciallizedID(arg0, arg1);
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
  public String getValuesFromStrArray(String[] arg0) throws BusinessException {
    beforeCallMethod(276);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getValuesFromStrArray(arg0);
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
  public ArrayList getGroupAssIDs(AssVO[] arg0) throws BusinessException {
    beforeCallMethod(277);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getGroupAssIDs(arg0);
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
  public String getPkBdinfoByCode(String arg0) throws BusinessException {
    beforeCallMethod(278);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getPkBdinfoByCode(arg0);
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
  public String getPkBdinfoByName(String arg0) throws BusinessException {
    beforeCallMethod(279);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getPkBdinfoByName(arg0);
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
  public String getPowerIdTable(AssVO[] arg0, String arg1, String arg2, Hashtable arg3) throws BusinessException {
    beforeCallMethod(280);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getPowerIdTable(arg0, arg1, arg2, arg3);
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
  public ArrayList getTransferAssID(ArrayList arg0) throws BusinessException {
    beforeCallMethod(281);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getTransferAssID(arg0);
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
  public String getValuePkByValueName(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(282);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getValuePkByValueName(arg0, arg1, arg2);
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
  public String[][] getValuePksByValueCodes(BdinfoVO arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(283);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getValuePksByValueCodes(arg0, arg1, arg2);
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
  public void refreshCodeName(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(284);
    Exception er = null;
    try {
      _getBeanObject().refreshCodeName(arg0, arg1);
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
  }

  public UFDouble getVerifyed(VeryfyQeuryVO arg0) throws GLBusinessException {
    beforeCallMethod(285);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getVerifyed(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getShouldVerifyed(VeryfyQeuryVO arg0) throws GLBusinessException {
    beforeCallMethod(286);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getShouldVerifyed(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] getAllVoucherPKs(GlQueryVO arg0) throws GLBusinessException {
    beforeCallMethod(287);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAllVoucherPKs(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getNotVerifyedBalance(VeryfyQeuryVO arg0) throws GLBusinessException {
    beforeCallMethod(288);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getNotVerifyedBalance(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getOppositAccsubj(GlQueryVO arg0) throws GLBusinessException {
    beforeCallMethod(289);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getOppositAccsubj(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VerifyResultVO[] getShouldVerifyedBalance(VeryfyQeuryVO arg0) throws GLBusinessException {
    beforeCallMethod(290);
    Exception er = null;
    VerifyResultVO[] o = null;
    try {
      o = (VerifyResultVO[])_getBeanObject().getShouldVerifyedBalance(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public VerifyResultVO[] getVerifyedBalance(VeryfyQeuryVO arg0) throws GLBusinessException {
    beforeCallMethod(291);
    Exception er = null;
    VerifyResultVO[] o = null;
    try {
      o = (VerifyResultVO[])_getBeanObject().getVerifyedBalance(arg0);
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
      if ((er instanceof GLBusinessException)) {
        throw ((GLBusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String add(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(292);
    Exception er = null;
    String o = null;
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
  public void update(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(293);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
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
  }

  public VoucherVO[] queryByCondition(VoucherQueryConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(294);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryByCondition(arg0);
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
  public String addEffect(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(295);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().addEffect(arg0);
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
  public String[] addBatche(VoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(296);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().addBatche(arg0);
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
  public void deleteByVoucher(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(297);
    Exception er = null;
    try {
      _getBeanObject().deleteByVoucher(arg0);
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

  public VoucherVO[] queryByIDs(String[] arg0) throws BusinessException {
    beforeCallMethod(298);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryByIDs(arg0);
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
  public UFDate getStartDate(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(299);
    Exception er = null;
    UFDate o = null;
    try {
      o = _getBeanObject().getStartDate(arg0, arg1);
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
  public String[] getStartPeriod(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(300);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getStartPeriod(arg0, arg1);
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
  public Integer getQuantityDigit(String arg0) throws BusinessException {
    beforeCallMethod(301);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getQuantityDigit(arg0);
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
  public UFBoolean isDep(String arg0) throws BusinessException {
    beforeCallMethod(302);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDep(arg0);
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
  public Integer getStartVoucherNO(String arg0) throws BusinessException {
    beforeCallMethod(303);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getStartVoucherNO(arg0);
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
  public UFBoolean isEditOwnVoucher(String arg0) throws BusinessException {
    beforeCallMethod(304);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isEditOwnVoucher(arg0);
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
  public UFBoolean isEditVoucherNO(String arg0) throws BusinessException {
    beforeCallMethod(305);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isEditVoucherNO(arg0);
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
  public UFBoolean isVoucherNOAutoFill(String arg0) throws BusinessException {
    beforeCallMethod(306);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isVoucherNOAutoFill(arg0);
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
  public UFBoolean isVoucherTimeOrdered(String arg0) throws BusinessException {
    beforeCallMethod(307);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isVoucherTimeOrdered(arg0);
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
  public UFBoolean isThisMonCanTally(String arg0) throws BusinessException {
    beforeCallMethod(308);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isThisMonCanTally(arg0);
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
  public String[] getSettlePeriod(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(309);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getSettlePeriod(arg0, arg1);
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
  public Integer getOppSubjStyle(String arg0) throws BusinessException {
    beforeCallMethod(310);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getOppSubjStyle(arg0);
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
  public Integer getAccBookSubjDispNameStyle(String arg0) throws BusinessException {
    beforeCallMethod(311);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getAccBookSubjDispNameStyle(arg0);
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
  public Integer getAmountAllowZero(String arg0) throws BusinessException {
    beforeCallMethod(312);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getAmountAllowZero(arg0);
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
  public Integer getAmountMustBalance(String arg0) throws BusinessException {
    beforeCallMethod(313);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getAmountMustBalance(arg0);
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
  public Integer getAssDispNameStyle(String arg0) throws BusinessException {
    beforeCallMethod(314);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getAssDispNameStyle(arg0);
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
  public int[] getAssetTypeScheme(String arg0) throws BusinessException {
    beforeCallMethod(315);
    Exception er = null;
    int[] o = null;
    try {
      o = (int[])_getBeanObject().getAssetTypeScheme(arg0);
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
  public int[] getCunhuoTypeCode(String arg0) throws BusinessException {
    beforeCallMethod(316);
    Exception er = null;
    int[] o = null;
    try {
      o = (int[])_getBeanObject().getCunhuoTypeCode(arg0);
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
  public Integer getBalanceControlStyle(String arg0) throws BusinessException {
    beforeCallMethod(317);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getBalanceControlStyle(arg0);
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
  public Integer getBillFormat(String arg0) throws BusinessException {
    beforeCallMethod(318);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getBillFormat(arg0);
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
  public Integer getBillTitleStyle(String arg0) throws BusinessException {
    beforeCallMethod(319);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getBillTitleStyle(arg0);
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
  public Integer getCurrStyle(String arg0) throws BusinessException {
    beforeCallMethod(320);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getCurrStyle(arg0);
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
  public String getDefaultDepMethod(String arg0) throws BusinessException {
    beforeCallMethod(321);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getDefaultDepMethod(arg0);
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
  public Integer getDistributePeriod(String arg0) throws BusinessException {
    beforeCallMethod(322);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getDistributePeriod(arg0);
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
  public Integer getFracMustBalance(String arg0) throws BusinessException {
    beforeCallMethod(323);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getFracMustBalance(arg0);
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
  public GLParameterVO getGlparameter(String arg0) throws BusinessException {
    beforeCallMethod(324);
    Exception er = null;
    GLParameterVO o = null;
    try {
      o = _getBeanObject().getGlparameter(arg0);
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
  public Integer getPriceDigit(String arg0) throws BusinessException {
    beforeCallMethod(325);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getPriceDigit(arg0);
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
  public Integer getRunTimeReconcile(String arg0) throws BusinessException {
    beforeCallMethod(326);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getRunTimeReconcile(arg0);
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
  public UFBoolean isRequireCasherSign(String arg0) throws BusinessException {
    beforeCallMethod(327);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isRequireCasherSign(arg0);
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
  public UFBoolean isTallyAfterChecked(String arg0) throws BusinessException {
    beforeCallMethod(328);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isTallyAfterChecked(arg0);
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
  public UFBoolean isTransferVoucherEditable(String arg0) throws BusinessException {
    beforeCallMethod(329);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isTransferVoucherEditable(arg0);
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
  public UFBoolean isUnTallyable(String arg0) throws BusinessException {
    beforeCallMethod(330);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUnTallyable(arg0);
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
  public Integer getOccurControlStyle(String arg0) throws BusinessException {
    beforeCallMethod(331);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getOccurControlStyle(arg0);
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
  public UFBoolean isAssAllowedNull(String arg0) throws BusinessException {
    beforeCallMethod(332);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isAssAllowedNull(arg0);
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
  public UFBoolean isInstantPrint(String arg0) throws BusinessException {
    beforeCallMethod(333);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isInstantPrint(arg0);
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
  public Integer getSignDateMethod(String arg0) throws BusinessException {
    beforeCallMethod(334);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getSignDateMethod(arg0);
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
  public UFBoolean isCheckBreakNoPrint(String arg0) throws BusinessException {
    beforeCallMethod(335);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCheckBreakNoPrint(arg0);
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
  public Integer getVoucherFlowCTL(String arg0) throws BusinessException {
    beforeCallMethod(336);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getVoucherFlowCTL(arg0);
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
  public Integer getPrintStyle(String arg0) throws BusinessException {
    beforeCallMethod(337);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getPrintStyle(arg0);
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
  public Integer getQuantityAllowZero(String arg0) throws BusinessException {
    beforeCallMethod(338);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getQuantityAllowZero(arg0);
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
  public Integer getRecVoucherPreDate(String arg0) throws BusinessException {
    beforeCallMethod(339);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getRecVoucherPreDate(arg0);
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
  public Integer getSubjDispNameStyle(String arg0) throws BusinessException {
    beforeCallMethod(340);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getSubjDispNameStyle(arg0);
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
  public Integer getTimeLimit(String arg0) throws BusinessException {
    beforeCallMethod(341);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getTimeLimit(arg0);
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
  public Integer getTransferCEStyle(String arg0) throws BusinessException {
    beforeCallMethod(342);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getTransferCEStyle(arg0);
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
  public Integer getWriteOff(String arg0) throws BusinessException {
    beforeCallMethod(343);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getWriteOff(arg0);
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
  public UFBoolean isAddStyleUniform() throws BusinessException {
    beforeCallMethod(344);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isAddStyleUniform();
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
  public UFBoolean isAssetTypeUniform() throws BusinessException {
    beforeCallMethod(345);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isAssetTypeUniform();
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
  public UFBoolean isBillSubjInSameOrient(String arg0) throws BusinessException {
    beforeCallMethod(346);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isBillSubjInSameOrient(arg0);
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
  public UFBoolean isCheckCFSaveVoucher(String arg0) throws BusinessException {
    beforeCallMethod(347);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCheckCFSaveVoucher(arg0);
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
  public UFBoolean isCheckForeign(String arg0) throws BusinessException {
    beforeCallMethod(348);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCheckForeign(arg0);
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
  public String isCheckOtherModule(String arg0) throws BusinessException {
    beforeCallMethod(349);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().isCheckOtherModule(arg0);
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
  public UFBoolean isCheckProfitAndLos(String arg0) throws BusinessException {
    beforeCallMethod(350);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCheckProfitAndLos(arg0);
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
  public UFBoolean isCheckUncontinuedNo(String arg0) throws BusinessException {
    beforeCallMethod(351);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCheckUncontinuedNo(arg0);
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
    return o;
  }
  public UFBoolean isCurrRateUniform() throws BusinessException {
    beforeCallMethod(352);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isCurrRateUniform();
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
  public UFBoolean isDelInputVoucherOut(String arg0) throws BusinessException {
    beforeCallMethod(353);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDelInputVoucherOut(arg0);
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
  public UFBoolean isDepAllAtLast(String arg0) throws BusinessException {
    beforeCallMethod(354);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDepAllAtLast(arg0);
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
  public UFBoolean isDepMethodUniform() throws BusinessException {
    beforeCallMethod(355);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDepMethodUniform();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(355, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isDevEffectDep(String arg0) throws BusinessException {
    beforeCallMethod(356);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDevEffectDep(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(356, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isAccCtrlBalanceInDetail(String arg0) throws BusinessException {
    beforeCallMethod(357);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isAccCtrlBalanceInDetail(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(357, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isEnableSetUnEven(String arg0) throws BusinessException {
    beforeCallMethod(358);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isEnableSetUnEven(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(358, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isFreevalueDefault(String arg0) throws BusinessException {
    beforeCallMethod(359);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isFreevalueDefault(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(359, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isInputVoucherNeedCheck(String arg0) throws BusinessException {
    beforeCallMethod(360);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isInputVoucherNeedCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(360, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isLoadByDepart(String arg0) throws BusinessException {
    beforeCallMethod(361);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isLoadByDepart(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(361, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isDisplayNullFree(String arg0) throws BusinessException {
    beforeCallMethod(362);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isDisplayNullFree(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(362, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isModDistribute(String arg0) throws BusinessException {
    beforeCallMethod(363);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isModDistribute(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(363, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isReceivedVoucherNeedConfirm(String arg0) throws BusinessException {
    beforeCallMethod(364);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isReceivedVoucherNeedConfirm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(364, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isSettleEnableRT(String arg0) throws BusinessException {
    beforeCallMethod(365);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isSettleEnableRT(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(365, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isSubjUniform() throws BusinessException {
    beforeCallMethod(366);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isSubjUniform();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(366, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isUnSettlable(String arg0) throws BusinessException {
    beforeCallMethod(367);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUnSettlable(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(367, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isUseCaseUniform() throws BusinessException {
    beforeCallMethod(368);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUseCaseUniform();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(368, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isInstantProductVoucher(String arg0) throws BusinessException {
    beforeCallMethod(369);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isInstantProductVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(369, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isVoucherTypeUniform() throws BusinessException {
    beforeCallMethod(370);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isVoucherTypeUniform();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(370, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public Boolean isLocalFracExt(String arg0) throws BusinessException {
    beforeCallMethod(371);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().isLocalFracExt(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(371, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isVoucherSaveRTVerify(String arg0) throws BusinessException {
    beforeCallMethod(372);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isVoucherSaveRTVerify(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(372, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isInputAttachment(String arg0) throws BusinessException {
    beforeCallMethod(373);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isInputAttachment(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(373, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isSettlableWithContrasted(String arg0) throws BusinessException {
    beforeCallMethod(374);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isSettlableWithContrasted(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(374, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFDate getEndDate(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(375);
    Exception er = null;
    UFDate o = null;
    try {
      o = _getBeanObject().getEndDate(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(375, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public GlPeriodVO getPeriod(String arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(376);
    Exception er = null;
    GlPeriodVO o = null;
    try {
      o = _getBeanObject().getPeriod(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(376, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFDate getStartDate(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(377);
    Exception er = null;
    UFDate o = null;
    try {
      o = _getBeanObject().getStartDate(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(377, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFDate getLastValuableDate(String arg0) throws BusinessException {
    beforeCallMethod(378);
    Exception er = null;
    UFDate o = null;
    try {
      o = _getBeanObject().getLastValuableDate(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(378, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String save(String arg0, String arg1, String arg2, String arg3, String[] arg4, String arg5, String arg6, VoucherVO[] arg7, String arg8) throws BusinessException {
    beforeCallMethod(379);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().save(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(379, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String insert(InitbuildVO arg0) throws BusinessException {
    beforeCallMethod(380);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(380, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void cancelBuild(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(381);
    Exception er = null;
    try {
      _getBeanObject().cancelBuild(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(381, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public int isBuiltByGlOrgBook(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(382);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().isBuiltByGlOrgBook(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(382, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public int isBuilt(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(383);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().isBuilt(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(383, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String adjustInitAfterBuild(String arg0, String arg1, String arg2, String arg3, String[] arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(384);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().adjustInitAfterBuild(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(384, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String deleteAndReProduceInit(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(385);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().deleteAndReProduceInit(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(385, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void logicDeleteOnKeyEvent(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(386);
    Exception er = null;
    try {
      _getBeanObject().logicDeleteOnKeyEvent(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(386, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void reBuildInitDetail(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(387);
    Exception er = null;
    try {
      _getBeanObject().reBuildInitDetail(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(387, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String reCalculateInit(String arg0, String arg1, String arg2, String arg3, String[] arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(388);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().reCalculateInit(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(388, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String saveImportData(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(389);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().saveImportData(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(389, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public DocmaptempletVO[] queryByDocMapTempletVO(DocmaptempletVO arg0) throws BusinessException {
    beforeCallMethod(390);
    Exception er = null;
    DocmaptempletVO[] o = null;
    try {
      o = (DocmaptempletVO[])_getBeanObject().queryByDocMapTempletVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(390, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public DocmaptempletVO saveDocMapTemplet(DocmaptempletVO arg0) throws BusinessException {
    beforeCallMethod(391);
    Exception er = null;
    DocmaptempletVO o = null;
    try {
      o = _getBeanObject().saveDocMapTemplet(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(391, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public DocmaptempletVO queryByDocMapTempletPK(String arg0) throws BusinessException {
    beforeCallMethod(392);
    Exception er = null;
    DocmaptempletVO o = null;
    try {
      o = _getBeanObject().queryByDocMapTempletPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(392, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void deleteByDocMapTempletPK(String arg0) throws BusinessException {
    beforeCallMethod(393);
    Exception er = null;
    try {
      _getBeanObject().deleteByDocMapTempletPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(393, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Boolean hasUsed(String arg0) throws BusinessException {
    beforeCallMethod(394);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().hasUsed(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(394, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String[][] copy(String[] arg0, MultiFormatVO[] arg1) throws BusinessException {
    beforeCallMethod(395);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().copy(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(395, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String insert(MultiFormatVO arg0) throws BusinessException {
    beforeCallMethod(396);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(396, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String update(MultiFormatVO arg0) throws BusinessException {
    beforeCallMethod(397);
    Exception er = null;
    String o = null;
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
      afterCallMethod(397, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public Integer deletebykey(String arg0) throws BusinessException {
    beforeCallMethod(398);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().deletebykey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(398, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public MultiFormatVO queryByKey(String arg0) throws BusinessException {
    beforeCallMethod(399);
    Exception er = null;
    MultiFormatVO o = null;
    try {
      o = _getBeanObject().queryByKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(399, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public MultiFormatVO[] searchByGlorgbook(String arg0) throws BusinessException {
    beforeCallMethod(400);
    Exception er = null;
    MultiFormatVO[] o = null;
    try {
      o = (MultiFormatVO[])_getBeanObject().searchByGlorgbook(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(400, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public MultiColFormatVO[] getColFormatsByKeys(String[] arg0) throws BusinessException {
    beforeCallMethod(401);
    Exception er = null;
    MultiColFormatVO[] o = null;
    try {
      o = (MultiColFormatVO[])_getBeanObject().getColFormatsByKeys(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(401, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void removeColFormatbykey(String arg0) throws BusinessException {
    beforeCallMethod(402);
    Exception er = null;
    try {
      _getBeanObject().removeColFormatbykey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(402, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public MultiColFormatVO[] searchColFormatsByGlorgbook(String arg0) throws BusinessException {
    beforeCallMethod(403);
    Exception er = null;
    MultiColFormatVO[] o = null;
    try {
      o = (MultiColFormatVO[])_getBeanObject().searchColFormatsByGlorgbook(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(403, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String[] updateFormat(MultiColFormatVO[] arg0) throws BusinessException {
    beforeCallMethod(404);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().updateFormat(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(404, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void delete(PrimebillVO arg0) throws BusinessException {
    beforeCallMethod(405);
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
      afterCallMethod(405, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String insert(PrimebillVO arg0) throws BusinessException {
    beforeCallMethod(406);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(406, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void update(PrimebillVO arg0) throws BusinessException {
    beforeCallMethod(407);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(407, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void deleteByParent(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(408);
    Exception er = null;
    try {
      _getBeanObject().deleteByParent(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(408, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PrimebillVO findByPKPrimebillVO(String arg0) throws BusinessException {
    beforeCallMethod(409);
    Exception er = null;
    PrimebillVO o = null;
    try {
      o = _getBeanObject().findByPKPrimebillVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(409, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public PrimebillVO getVoByparent(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(410);
    Exception er = null;
    PrimebillVO o = null;
    try {
      o = _getBeanObject().getVoByparent(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(410, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public PrintInfoAllVO getPrintInfo(QueryconditionVO arg0) throws BusinessException {
    beforeCallMethod(411);
    Exception er = null;
    PrintInfoAllVO o = null;
    try {
      o = _getBeanObject().getPrintInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(411, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void savePrintInfo(QueryconditionVO arg0, PrintInfoAllVO arg1) throws BusinessException {
    beforeCallMethod(412);
    Exception er = null;
    try {
      _getBeanObject().savePrintInfo(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(412, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String savePrintItem(PrintitemVO arg0) throws BusinessException {
    beforeCallMethod(413);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().savePrintItem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(413, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void deletePrintItem(PrintitemVO arg0) throws BusinessException {
    beforeCallMethod(414);
    Exception er = null;
    try {
      _getBeanObject().deletePrintItem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(414, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PrintitemVO[] getPrintItem(PrintitemVO arg0) throws BusinessException {
    beforeCallMethod(415);
    Exception er = null;
    PrintitemVO[] o = null;
    try {
      o = (PrintitemVO[])_getBeanObject().getPrintItem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(415, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public PrintdealclassVO[] getPrintDealClass() throws BusinessException {
    beforeCallMethod(416);
    Exception er = null;
    PrintdealclassVO[] o = null;
    try {
      o = (PrintdealclassVO[])_getBeanObject().getPrintDealClass();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(416, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public nc.vo.gl.pubvoucher.UserVO[] queryAllUser() throws BusinessException {
    beforeCallMethod(417);
    Exception er = null;
    nc.vo.gl.pubvoucher.UserVO[] o = null;
    try {
      o = (nc.vo.gl.pubvoucher.UserVO[])_getBeanObject().queryAllUser();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(417, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] save(VoucherVO arg0, VoucherCheckConfigVO arg1, Object arg2) throws BusinessException {
    beforeCallMethod(418);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().save(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(418, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] save(VoucherVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(419);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().save(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(419, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] delete(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(420);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().delete(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(420, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO[] queryByConditionVO(VoucherQueryConditionVO[] arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(421);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryByConditionVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(421, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO[] queryByVO(VoucherVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(422);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(422, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO findByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(423);
    Exception er = null;
    VoucherVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(423, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO[] queryAll(String arg0) throws BusinessException {
    beforeCallMethod(424);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryAll(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(424, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO[] queryByPks(String[] arg0) throws BusinessException {
    beforeCallMethod(425);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().queryByPks(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(425, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public Integer getCorrectVoucherNo(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(426);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getCorrectVoucherNo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(426, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public Boolean isExistRegulationVoucher(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(427);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().isExistRegulationVoucher(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(427, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO queryByPk(String arg0) throws BusinessException {
    beforeCallMethod(428);
    Exception er = null;
    VoucherVO o = null;
    try {
      o = _getBeanObject().queryByPk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(428, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] abandonByPks(String[] arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(429);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().abandonByPks(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(429, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherVO[] catVouchers(VoucherVO[] arg0) throws BusinessException {
    beforeCallMethod(430);
    Exception er = null;
    VoucherVO[] o = null;
    try {
      o = (VoucherVO[])_getBeanObject().catVouchers(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(430, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void copyVoucher(VoucherVO arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(431);
    Exception er = null;
    try {
      _getBeanObject().copyVoucher(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(431, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void copyVoucherUnChecked(VoucherVO arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(432);
    Exception er = null;
    try {
      _getBeanObject().copyVoucherUnChecked(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(432, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public OperationResultVO deleteByPk(String arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(433);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().deleteByPk(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(433, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO deleteByPk(String arg0) throws BusinessException {
    beforeCallMethod(434);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().deleteByPk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(434, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO deleteByPks(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(435);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().deleteByPks(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(435, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] saveError(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(436);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().saveError(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(436, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void adjustAss() throws BusinessException {
    beforeCallMethod(437);
    Exception er = null;
    try {
      _getBeanObject().adjustAss();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(437, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustAss(String arg0) throws BusinessException {
    beforeCallMethod(438);
    Exception er = null;
    try {
      _getBeanObject().adjustAss(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(438, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustAccsubjAss(String arg0) throws BusinessException {
    beforeCallMethod(439);
    Exception er = null;
    try {
      _getBeanObject().adjustAccsubjAss(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(439, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustAssByMaxAss(String arg0) throws BusinessException {
    beforeCallMethod(440);
    Exception er = null;
    try {
      _getBeanObject().adjustAssByMaxAss(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(440, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustAssByMaxAss() throws BusinessException {
    beforeCallMethod(441);
    Exception er = null;
    try {
      _getBeanObject().adjustAssByMaxAss();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(441, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustCashbankSubjVoucher(String arg0) throws BusinessException {
    beforeCallMethod(442);
    Exception er = null;
    try {
      _getBeanObject().adjustCashbankSubjVoucher(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(442, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void adjustOppsubj(String[] arg0) throws BusinessException {
    beforeCallMethod(443);
    Exception er = null;
    try {
      _getBeanObject().adjustOppsubj(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(443, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public VoucherAdjustVO[] adjustVoucherNo(String[] arg0, String[] arg1, String[] arg2, String[] arg3, Integer arg4) throws BusinessException {
    beforeCallMethod(444);
    Exception er = null;
    VoucherAdjustVO[] o = null;
    try {
      o = (VoucherAdjustVO[])_getBeanObject().adjustVoucherNo(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(444, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherAdjustVO[] adjustVoucherNoLimited(String[] arg0, String[] arg1, String[] arg2, String[] arg3, Integer arg4, Integer arg5, Integer arg6) throws BusinessException {
    beforeCallMethod(445);
    Exception er = null;
    VoucherAdjustVO[] o = null;
    try {
      o = (VoucherAdjustVO[])_getBeanObject().adjustVoucherNoLimited(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(445, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] checkVoucherByPks(String[] arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(446);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().checkVoucherByPks(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(446, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] checkVoucherByPk(String arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(447);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().checkVoucherByPk(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(447, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] checkTimeOrdered(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(448);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().checkTimeOrdered(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(448, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UIConfigVO load(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(449);
    Exception er = null;
    UIConfigVO o = null;
    try {
      o = _getBeanObject().load(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(449, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void save(UIConfigVO arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(450);
    Exception er = null;
    try {
      _getBeanObject().save(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(450, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public OperationResultVO[] abandonVoucherByPk(String arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(451);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().abandonVoucherByPk(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(451, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] abandonVoucherByPks(String[] arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(452);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().abandonVoucherByPks(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(452, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void outputVoucherByPKs(String[] arg0) throws BusinessException {
    beforeCallMethod(453);
    Exception er = null;
    try {
      _getBeanObject().outputVoucherByPKs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(453, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public VoucherDataPreLoadVO addCardLoad(VoucherDataPreLoadVO arg0) throws BusinessException {
    beforeCallMethod(454);
    Exception er = null;
    VoucherDataPreLoadVO o = null;
    try {
      o = _getBeanObject().addCardLoad(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(454, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VoucherDataPreLoadVO queryVoucherLoad(VoucherDataPreLoadVO arg0) throws BusinessException {
    beforeCallMethod(455);
    Exception er = null;
    VoucherDataPreLoadVO o = null;
    try {
      o = _getBeanObject().queryVoucherLoad(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(455, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isExistBreakNo(VoucherVO arg0) throws BusinessException {
    beforeCallMethod(456);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isExistBreakNo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(456, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isExistBreakNo(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(457);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isExistBreakNo(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(457, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public UFBoolean isThereTalliedVoucher(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(458);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isThereTalliedVoucher(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(458, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] queryBreakNo(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(459);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().queryBreakNo(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(459, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] queryBreakNoAddVt(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(460);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().queryBreakNoAddVt(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(460, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public SmallVoucherVO[] queryForSettle(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(461);
    Exception er = null;
    SmallVoucherVO[] o = null;
    try {
      o = (SmallVoucherVO[])_getBeanObject().queryForSettle(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(461, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] signVoucherByPk(String arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(462);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().signVoucherByPk(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(462, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] signVoucherByPks(String[] arg0, String arg1, Boolean arg2) throws BusinessException {
    beforeCallMethod(463);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().signVoucherByPks(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(463, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] signVoucherByPks(String[] arg0, String arg1, UFDate arg2, Boolean arg3) throws BusinessException {
    beforeCallMethod(464);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().signVoucherByPks(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(464, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO tallyinitVoucherByPks(String[] arg0, String arg1, UFDate arg2, Boolean arg3) throws BusinessException {
    beforeCallMethod(465);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().tallyinitVoucherByPks(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(465, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO tallyVoucherByPk(String arg0, String arg1, UFDate arg2, Boolean arg3) throws BusinessException {
    beforeCallMethod(466);
    Exception er = null;
    OperationResultVO o = null;
    try {
      o = _getBeanObject().tallyVoucherByPk(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(466, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public OperationResultVO[] tallyVoucherByPks(String[] arg0, String arg1, UFDate arg2, Boolean arg3) throws BusinessException {
    beforeCallMethod(467);
    Exception er = null;
    OperationResultVO[] o = null;
    try {
      o = (OperationResultVO[])_getBeanObject().tallyVoucherByPks(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(467, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void delete(VouchermodeVO arg0) throws BusinessException {
    beforeCallMethod(468);
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
      afterCallMethod(468, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String insert(VouchermodeVO arg0) throws BusinessException {
    beforeCallMethod(469);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(469, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public void update(VouchermodeVO arg0) throws BusinessException {
    beforeCallMethod(470);
    Exception er = null;
    try {
      _getBeanObject().update(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(470, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public VouchermodeVO[] queryByVO(VouchermodeVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(471);
    Exception er = null;
    VouchermodeVO[] o = null;
    try {
      o = (VouchermodeVO[])_getBeanObject().queryByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(471, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public String[] insertArray(VouchermodeVO[] arg0) throws BusinessException {
    beforeCallMethod(472);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(472, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VouchermodeVO[] queryAllVouchermodeVOs(String arg0) throws BusinessException {
    beforeCallMethod(473);
    Exception er = null;
    VouchermodeVO[] o = null;
    try {
      o = (VouchermodeVO[])_getBeanObject().queryAllVouchermodeVOs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(473, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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
  public VouchermodeVO findByPKVouchermodeVO(String arg0) throws BusinessException {
    beforeCallMethod(474);
    Exception er = null;
    VouchermodeVO o = null;
    try {
      o = _getBeanObject().findByPKVouchermodeVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(474, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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