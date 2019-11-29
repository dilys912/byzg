package nc.itf.scm.so;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.pub.AvgSaleQueryVO;
import nc.vo.so.pub.ProdNotInstallException;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so001.ComActionDescVO;
import nc.vo.so.so001.SODataSet;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so008.OosinfoItemVO;
import nc.vo.so.so008.OosinfoVO;
import nc.vo.so.so009.ReceiveVO;
import nc.vo.so.so012.SaleBalVO;
import nc.vo.so.so012.SalebalanceVO;
import nc.vo.so.so012.SquareTotalVO;
import nc.vo.so.so012.SquareVO;
import nc.vo.so.so013.FeeVO;
import nc.vo.so.so015.ARSubHVO;
import nc.vo.so.so015.ARSubVO;
import nc.vo.so.so016.OrdBalVO;
import nc.vo.so.so016.OrdBalanceVO;
import nc.vo.so.so017.ChannelGroupHVO;
import nc.vo.so.so017.ChannelGroupVO;
import nc.vo.so.so022.SaleGrossprofitVO;
import nc.vo.so.so023.ParamVO;
import nc.vo.so.so024.TurnOverBVO;
import nc.vo.so.so033.ATPVO;
import nc.vo.so.so033.SaleExecRptVO;
import nc.vo.so.so033.SaleReportVO;
import nc.vo.so.so038.RestrictcenterVO;
import nc.vo.so.so066.SaleOrderDetailVO;
import nc.vo.so.so066.SaleQueryVO;
import nc.vo.so.so090.CoopwithVO;
import nc.vo.so.so102.InvcalbodyVO;
import nc.vo.so.so103.BuylargessBVO;
import nc.vo.so.so103.BuylargessHVO;
import nc.vo.so.so103.BuylargessVO;
import nc.vo.so.so105.SalespromotionVO;
import nc.vo.sp.sp015.PreorderVO;

public class SCM_SO_SOOrderEJB_Local extends BeanBase
  implements SCM_SO_SOOrderEJBEjbObject
{
  private SCM_SO_SOOrderEJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (SCM_SO_SOOrderEJBEjbBean)getEjb();
  }

  public UFDouble getATPNum(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(200);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getATPNum(arg0, arg1, arg2, arg3);
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
    return o;
  }
  public UFDouble getOnHandNum(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) throws BusinessException {
    beforeCallMethod(201);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getOnHandNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
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
  public ATPVO[] getATPVO(AggregatedValueObject arg0) throws BusinessException {
    beforeCallMethod(202);
    Exception er = null;
    ATPVO[] o = null;
    try {
      o = (ATPVO[])_getBeanObject().getATPVO(arg0);
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
  public UFDouble getSuperOnHandNum(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(203);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getSuperOnHandNum(arg0, arg1, arg2, arg3);
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
  public void delete(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(204);
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

  public ArrayList insert(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(205);
    Exception er = null;
    ArrayList o = null;
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
  public String update(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(206);
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
  public void audit(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(207);
    Exception er = null;
    try {
      _getBeanObject().audit(arg0);
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
  }

  public void closeARSub(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(208);
    Exception er = null;
    try {
      _getBeanObject().closeARSub(arg0);
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
  }

  public void openARSub(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(209);
    Exception er = null;
    try {
      _getBeanObject().openARSub(arg0);
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
  }

  public String subAcct(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(210);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().subAcct(arg0);
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
  public String unSubAcct(ARSubVO arg0) throws BusinessException {
    beforeCallMethod(211);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().unSubAcct(arg0);
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
  public ArrayList insertForJbFl(ARSubVO[] arg0) throws BusinessException {
    beforeCallMethod(212);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insertForJbFl(arg0);
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
  public String[] EndAndNewArsub(ARSubHVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(213);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().EndAndNewArsub(arg0, arg1);
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
  public OrdBalVO updateSoBalance(OrdBalVO arg0) throws BusinessException {
    beforeCallMethod(214);
    Exception er = null;
    OrdBalVO o = null;
    try {
      o = _getBeanObject().updateSoBalance(arg0);
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
  public OrdBalanceVO updateSoBalance(OrdBalanceVO arg0, OrdBalanceVO arg1) throws BusinessException {
    beforeCallMethod(215);
    Exception er = null;
    OrdBalanceVO o = null;
    try {
      o = _getBeanObject().updateSoBalance(arg0, arg1);
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
  public void delete(BomorderVO arg0) throws BusinessException {
    beforeCallMethod(216);
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

  public String insert(BomorderVO arg0) throws BusinessException {
    beforeCallMethod(217);
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
  public void update(BomorderVO arg0) throws BusinessException {
    beforeCallMethod(218);
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

  public void updateItems(BomorderItemVO[] arg0) throws BusinessException {
    beforeCallMethod(219);
    Exception er = null;
    try {
      _getBeanObject().updateItems(arg0);
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

  public void insertItems(BomorderItemVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(220);
    Exception er = null;
    try {
      _getBeanObject().insertItems(arg0, arg1, arg2);
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
  }

  public BuylargessVO[] query(String arg0) throws Exception {
    beforeCallMethod(221);
    Exception er = null;
    BuylargessVO[] o = null;
    try {
      o = (BuylargessVO[])_getBeanObject().query(arg0);
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
  public BuylargessBVO[] findItemsForHeader(String arg0) throws BusinessException {
    beforeCallMethod(222);
    Exception er = null;
    BuylargessBVO[] o = null;
    try {
      o = (BuylargessBVO[])_getBeanObject().findItemsForHeader(arg0);
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
  public BuylargessHVO[] queryAllHeadData(String arg0) throws BusinessException {
    beforeCallMethod(223);
    Exception er = null;
    BuylargessHVO[] o = null;
    try {
      o = (BuylargessHVO[])_getBeanObject().queryAllHeadData(arg0);
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
  public ArrayList getLargessAndBindingsByInvs(String[] arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(224);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getLargessAndBindingsByInvs(arg0, arg1);
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
  public ChannelGroupVO insert(ChannelGroupVO arg0) throws BusinessException {
    beforeCallMethod(225);
    Exception er = null;
    ChannelGroupVO o = null;
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
  public ChannelGroupVO update(ChannelGroupVO arg0, ClientLink arg1) throws BusinessException {
    beforeCallMethod(226);
    Exception er = null;
    ChannelGroupVO o = null;
    try {
      o = _getBeanObject().update(arg0, arg1);
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
  public void audit(ChannelGroupHVO arg0) throws BusinessException {
    beforeCallMethod(227);
    Exception er = null;
    try {
      _getBeanObject().audit(arg0);
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

  public void auditCheckCG(AggregatedValueObject arg0) throws BusinessException {
    beforeCallMethod(228);
    Exception er = null;
    try {
      _getBeanObject().auditCheckCG(arg0);
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

  public void cancelAudit(String arg0) throws BusinessException {
    beforeCallMethod(229);
    Exception er = null;
    try {
      _getBeanObject().cancelAudit(arg0);
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

  public void ChannelGroup_delete(String arg0) throws BusinessException {
    beforeCallMethod(230);
    Exception er = null;
    try {
      _getBeanObject().ChannelGroup_delete(arg0);
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
  }

  public void saveCheckCG(AggregatedValueObject arg0) throws BusinessException {
    beforeCallMethod(231);
    Exception er = null;
    try {
      _getBeanObject().saveCheckCG(arg0);
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
  }

  public void delete(CoopwithVO arg0) throws BusinessException {
    beforeCallMethod(232);
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
  }

  public String insert(CoopwithVO arg0) throws BusinessException {
    beforeCallMethod(233);
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
  public void update(CoopwithVO arg0) throws BusinessException {
    beforeCallMethod(234);
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
  }

  public void deleteByPK(String arg0) throws BusinessException {
    beforeCallMethod(235);
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

  public void delete(FeeVO arg0) throws BusinessException {
    beforeCallMethod(236);
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

  public String insert(FeeVO arg0) throws BusinessException {
    beforeCallMethod(237);
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
  public UFDateTime update(FeeVO arg0) throws BusinessException {
    beforeCallMethod(238);
    Exception er = null;
    UFDateTime o = null;
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
  public void auditFee(FeeVO arg0) throws BusinessException {
    beforeCallMethod(239);
    Exception er = null;
    try {
      _getBeanObject().auditFee(arg0);
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

  public void blankOut(FeeVO arg0) throws BusinessException {
    beforeCallMethod(240);
    Exception er = null;
    try {
      _getBeanObject().blankOut(arg0);
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

  public void unauditFee(FeeVO arg0) throws BusinessException {
    beforeCallMethod(241);
    Exception er = null;
    try {
      _getBeanObject().unauditFee(arg0);
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

  public InvcalbodyVO edit(InvcalbodyVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(242);
    Exception er = null;
    InvcalbodyVO o = null;
    try {
      o = _getBeanObject().edit(arg0, arg1);
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
  public void delete(OosinfoVO arg0) throws BusinessException {
    beforeCallMethod(243);
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

  public String insert(OosinfoVO arg0) throws BusinessException {
    beforeCallMethod(244);
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
  public void update(OosinfoVO arg0) throws BusinessException {
    beforeCallMethod(245);
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
  }

  public void updateOosinItems(OosinfoItemVO[] arg0) throws BusinessException {
    beforeCallMethod(246);
    Exception er = null;
    try {
      _getBeanObject().updateOosinItems(arg0);
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

  public TurnOverBVO[] OrderMoney_setNum(TurnOverBVO[] arg0, UFDouble arg1, UFDouble arg2, UFDouble arg3, UFDate arg4, UFDate arg5, String arg6, String arg7) throws BusinessException {
    beforeCallMethod(247);
    Exception er = null;
    TurnOverBVO[] o = null;
    try {
      o = (TurnOverBVO[])_getBeanObject().OrderMoney_setNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  public void updateData(String arg0, String arg1) throws RemoteException {
    beforeCallMethod(248);
    Exception er = null;
    try {
      _getBeanObject().updateData(arg0, arg1);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public void delete(PreorderVO arg0) throws BusinessException {
    beforeCallMethod(249);
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

  public ArrayList insert(PreorderVO arg0) throws BusinessException {
    beforeCallMethod(250);
    Exception er = null;
    ArrayList o = null;
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
  public Object update(PreorderVO arg0) throws BusinessException {
    beforeCallMethod(251);
    Exception er = null;
    Object o = null;
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
  public void auditone(PreorderVO arg0) throws BusinessException {
    beforeCallMethod(252);
    Exception er = null;
    try {
      _getBeanObject().auditone(arg0);
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

  public void blankOutOne(String arg0) throws BusinessException {
    beforeCallMethod(253);
    Exception er = null;
    try {
      _getBeanObject().blankOutOne(arg0);
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

  public HashMap revisePreOrder(PreorderVO arg0) throws BusinessException {
    beforeCallMethod(254);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().revisePreOrder(arg0);
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
  public void unAuditone(String arg0) throws BusinessException {
    beforeCallMethod(255);
    Exception er = null;
    try {
      _getBeanObject().unAuditone(arg0);
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

  public Object[] batchPreOrderToOrder(SaleOrderVO[] arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(256);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().batchPreOrderToOrder(arg0, arg1);
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
  public TurnOverBVO[] ProfitABC_setNum(TurnOverBVO[] arg0, UFDouble arg1, UFDouble arg2, UFDouble arg3, UFDate arg4, UFDate arg5, String arg6, String arg7) throws BusinessException {
    beforeCallMethod(257);
    Exception er = null;
    TurnOverBVO[] o = null;
    try {
      o = (TurnOverBVO[])_getBeanObject().ProfitABC_setNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  public Object processAction_RequiresNew(String arg0, String arg1, String arg2, PfUtilWorkFlowVO arg3, AggregatedValueObject arg4, Object arg5) throws BusinessException {
    beforeCallMethod(258);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().processAction_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5);
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
  public Object processActionExtended_RequiresNew(String arg0, String arg1, String arg2, PfUtilWorkFlowVO arg3, AggregatedValueObject arg4, Object arg5, HashMap arg6) throws BusinessException {
    beforeCallMethod(259);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().processActionExtended_RequiresNew(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  public Object RunNewEjbFun_RequiresNew(ArrayList arg0, String arg1) throws BusinessException {
    beforeCallMethod(260);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().RunNewEjbFun_RequiresNew(arg0, arg1);
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
  public ReceiveVO[] updateCustAR(ReceiveVO[] arg0) throws BusinessException {
    beforeCallMethod(261);
    Exception er = null;
    ReceiveVO[] o = null;
    try {
      o = (ReceiveVO[])_getBeanObject().updateCustAR(arg0);
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
  public String insert(RestrictcenterVO arg0) throws BusinessException {
    beforeCallMethod(262);
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
  public void update(RestrictcenterVO arg0) throws BusinessException {
    beforeCallMethod(263);
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

  public void deleteAll(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(264);
    Exception er = null;
    try {
      _getBeanObject().deleteAll(arg0, arg1);
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

  public String[] insertArray(RestrictcenterVO[] arg0) throws BusinessException {
    beforeCallMethod(265);
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
  public RestrictcenterVO[] updateAll(RestrictcenterVO[] arg0) throws BusinessException {
    beforeCallMethod(266);
    Exception er = null;
    RestrictcenterVO[] o = null;
    try {
      o = (RestrictcenterVO[])_getBeanObject().updateAll(arg0);
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
  public Object processAction(ComActionDescVO arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(267);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().processAction(arg0, arg1);
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
  public Object processQuery(ComActionDescVO arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(268);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().processQuery(arg0, arg1);
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
  public CircularlyAccessibleValueObject[] processQuery1(ComActionDescVO arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(269);
    Exception er = null;
    CircularlyAccessibleValueObject[] o = null;
    try {
      o = (CircularlyAccessibleValueObject[])_getBeanObject().processQuery1(arg0, arg1);
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
  public AggregatedValueObject[] processQuery2(ComActionDescVO arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(270);
    Exception er = null;
    AggregatedValueObject[] o = null;
    try {
      o = (AggregatedValueObject[])_getBeanObject().processQuery2(arg0, arg1);
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
  public Object getComInstance(String arg0, String arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(271);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().getComInstance(arg0, arg1, arg2);
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
  public Object getComInstance(String arg0, String arg1) throws ProdNotInstallException {
    beforeCallMethod(272);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().getComInstance(arg0, arg1);
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
      if ((er instanceof ProdNotInstallException)) {
        throw ((ProdNotInstallException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Object getComInstance(String arg0, Class[] arg1, Object[] arg2, String arg3, boolean arg4) throws BusinessException {
    beforeCallMethod(273);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().getComInstance(arg0, arg1, arg2, arg3, arg4);
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
  public Class getComClass(String arg0, String arg1) throws ProdNotInstallException {
    beforeCallMethod(274);
    Exception er = null;
    Class o = null;
    try {
      o = _getBeanObject().getComClass(arg0, arg1);
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
      if ((er instanceof ProdNotInstallException)) {
        throw ((ProdNotInstallException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Class getComClass(String arg0, String arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(275);
    Exception er = null;
    Class o = null;
    try {
      o = _getBeanObject().getComClass(arg0, arg1, arg2);
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
  public Object runComMethod(String arg0, Object arg1, String arg2, Class[] arg3, Object[] arg4) throws ProdNotInstallException, Exception {
    beforeCallMethod(276);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().runComMethod(arg0, arg1, arg2, arg3, arg4);
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
      if ((er instanceof ProdNotInstallException)) {
        throw ((ProdNotInstallException)er);
      }
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
  public Object runComMethod(String arg0, Object arg1, String arg2, Class[] arg3, Object[] arg4, boolean arg5) throws Exception {
    beforeCallMethod(277);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().runComMethod(arg0, arg1, arg2, arg3, arg4, arg5);
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
  public Object runComMethod(String arg0, String arg1, String arg2, Class[] arg3, Object[] arg4, boolean arg5) throws Exception {
    beforeCallMethod(278);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().runComMethod(arg0, arg1, arg2, arg3, arg4, arg5);
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
  public Object runComMethod(String arg0, String arg1, String arg2, Class[] arg3, Object[] arg4) throws ProdNotInstallException, Exception {
    beforeCallMethod(279);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().runComMethod(arg0, arg1, arg2, arg3, arg4);
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
      if ((er instanceof ProdNotInstallException)) {
        throw ((ProdNotInstallException)er);
      }
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
  public SODataSet fillSODataSet(SODataSet arg0) throws BusinessException {
    beforeCallMethod(280);
    Exception er = null;
    SODataSet o = null;
    try {
      o = _getBeanObject().fillSODataSet(arg0);
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
  public SODataSet fillSODataSetBySql(String arg0) throws BusinessException {
    beforeCallMethod(281);
    Exception er = null;
    SODataSet o = null;
    try {
      o = _getBeanObject().fillSODataSetBySql(arg0);
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
  public Object[][] getObjectArray(String arg0, String[] arg1, String arg2, String[] arg3) throws BusinessException {
    beforeCallMethod(282);
    Exception er = null;
    Object[][] o = (Object[][])null;
    try {
      o = (Object[][])_getBeanObject().getObjectArray(arg0, arg1, arg2, arg3);
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
  public Object[] processActionExt(ComActionDescVO[] arg0, ArrayList arg1, ArrayList arg2) throws BusinessException {
    beforeCallMethod(283);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().processActionExt(arg0, arg1, arg2);
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
  public Object[] processActionExtX(ComActionDescVO[] arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(284);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().processActionExtX(arg0, arg1);
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
  public Object[] processQueryExt(ComActionDescVO[] arg0, ArrayList arg1, ArrayList arg2) throws BusinessException {
    beforeCallMethod(285);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().processQueryExt(arg0, arg1, arg2);
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
  public Object[] processQueryExtX(ComActionDescVO[] arg0, ArrayList arg1) throws BusinessException, BusinessException {
    beforeCallMethod(286);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().processQueryExtX(arg0, arg1);
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
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isModelStated(String arg0) throws BusinessException {
    beforeCallMethod(287);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isModelStated(arg0);
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
  public HashMap getAnyValueSORow(String arg0, String[] arg1, String arg2, String[] arg3, String arg4) throws BusinessException {
    beforeCallMethod(288);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getAnyValueSORow(arg0, arg1, arg2, arg3, arg4);
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
  public SORowData[] getSORows(String arg0) throws BusinessException {
    beforeCallMethod(289);
    Exception er = null;
    SORowData[] o = null;
    try {
      o = (SORowData[])_getBeanObject().getSORows(arg0);
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
  public void updateData(String arg0) throws BusinessException {
    beforeCallMethod(290);
    Exception er = null;
    try {
      _getBeanObject().updateData(arg0);
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
  }

  public SaleExecRptVO[] queryInvoiceExec(String arg0) throws BusinessException {
    beforeCallMethod(291);
    Exception er = null;
    SaleExecRptVO[] o = null;
    try {
      o = (SaleExecRptVO[])_getBeanObject().queryInvoiceExec(arg0);
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
  public SaleExecRptVO[] queryOrderExec(String arg0) throws BusinessException {
    beforeCallMethod(292);
    Exception er = null;
    SaleExecRptVO[] o = null;
    try {
      o = (SaleExecRptVO[])_getBeanObject().queryOrderExec(arg0);
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
  public SaleExecRptVO[] queryReceiptExec(String arg0) throws BusinessException {
    beforeCallMethod(293);
    Exception er = null;
    SaleExecRptVO[] o = null;
    try {
      o = (SaleExecRptVO[])_getBeanObject().queryReceiptExec(arg0);
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
  public SaleGrossprofitVO[] queryByCondition(ConditionVO[] arg0, ParamVO arg1) throws BusinessException {
    beforeCallMethod(294);
    Exception er = null;
    SaleGrossprofitVO[] o = null;
    try {
      o = (SaleGrossprofitVO[])_getBeanObject().queryByCondition(arg0, arg1);
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
  public String getUnitByInvPK(String arg0) throws BusinessException {
    beforeCallMethod(295);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getUnitByInvPK(arg0);
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
  public TurnOverBVO[] SaleMoneyABC_setNum(TurnOverBVO[] arg0, UFDouble arg1, UFDouble arg2, UFDouble arg3, UFDate arg4, UFDate arg5, String arg6, String arg7) throws BusinessException {
    beforeCallMethod(296);
    Exception er = null;
    TurnOverBVO[] o = null;
    try {
      o = (TurnOverBVO[])_getBeanObject().SaleMoneyABC_setNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  public ArrayList insert(SaleOrderVO arg0) throws BusinessException {
    beforeCallMethod(297);
    Exception er = null;
    ArrayList o = null;
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
  public ArrayList update(SaleOrderVO arg0) throws BusinessException {
    beforeCallMethod(298);
    Exception er = null;
    ArrayList o = null;
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
  public void returnCode(SaleOrderVO arg0) throws BusinessException {
    beforeCallMethod(299);
    Exception er = null;
    try {
      _getBeanObject().returnCode(arg0);
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
  }

  public void orderDelete(String arg0) throws BusinessException {
    beforeCallMethod(300);
    Exception er = null;
    try {
      _getBeanObject().orderDelete(arg0);
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
  }

  public void saveSaleBillForImport(SaleOrderVO arg0) throws BusinessException {
    beforeCallMethod(301);
    Exception er = null;
    try {
      _getBeanObject().saveSaleBillForImport(arg0);
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
  }

  public void orderUpdateLock(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(302);
    Exception er = null;
    try {
      _getBeanObject().orderUpdateLock(arg0, arg1);
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
  }

  public void updateOrderEnd(SaleorderBVO[] arg0) throws BusinessException {
    beforeCallMethod(303);
    Exception er = null;
    try {
      _getBeanObject().updateOrderEnd(arg0);
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

  public void updateRetinvFlag(String arg0) throws BusinessException {
    beforeCallMethod(304);
    Exception er = null;
    try {
      _getBeanObject().updateRetinvFlag(arg0);
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

  public SaleQueryVO[] queryByCondition(String arg0, String arg1, Hashtable arg2) throws BusinessException {
    beforeCallMethod(305);
    Exception er = null;
    SaleQueryVO[] o = null;
    try {
      o = (SaleQueryVO[])_getBeanObject().queryByCondition(arg0, arg1, arg2);
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
  public SaleOrderDetailVO[] queryByOrderCondition(String arg0, Hashtable arg1) throws BusinessException {
    beforeCallMethod(306);
    Exception er = null;
    SaleOrderDetailVO[] o = null;
    try {
      o = (SaleOrderDetailVO[])_getBeanObject().queryByOrderCondition(arg0, arg1);
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
  public SaleReportVO[] queryByCondition(String arg0, boolean arg1, boolean arg2, boolean arg3, int arg4) throws BusinessException {
    beforeCallMethod(307);
    Exception er = null;
    SaleReportVO[] o = null;
    try {
      o = (SaleReportVO[])_getBeanObject().queryByCondition(arg0, arg1, arg2, arg3, arg4);
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
  public SaleReportVO[] queryByCondition(String arg0) throws BusinessException {
    beforeCallMethod(308);
    Exception er = null;
    SaleReportVO[] o = null;
    try {
      o = (SaleReportVO[])_getBeanObject().queryByCondition(arg0);
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
  public SaleReportVO[] queryByConditionForSaleExec(String arg0) throws BusinessException {
    beforeCallMethod(309);
    Exception er = null;
    SaleReportVO[] o = null;
    try {
      o = (SaleReportVO[])_getBeanObject().queryByConditionForSaleExec(arg0);
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
  public String insert(SalebalanceVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(310);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert(arg0, arg1);
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
  public void update(SalebalanceVO arg0) throws BusinessException {
    beforeCallMethod(311);
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

  public String[] insertArray(SalebalanceVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(312);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray(arg0, arg1);
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
  public void Salebalance_delete(String arg0) throws BusinessException {
    beforeCallMethod(313);
    Exception er = null;
    try {
      _getBeanObject().Salebalance_delete(arg0);
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
  }

  public String[] execBalance(SaleBalVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(314);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().execBalance(arg0, arg1);
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
  public ArrayList insert(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(315);
    Exception er = null;
    ArrayList o = null;
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
  public void close(String arg0) throws BusinessException {
    beforeCallMethod(316);
    Exception er = null;
    try {
      _getBeanObject().close(arg0);
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

  public ArrayList update(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(317);
    Exception er = null;
    ArrayList o = null;
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
  public void freeze(String arg0) throws BusinessException {
    beforeCallMethod(318);
    Exception er = null;
    try {
      _getBeanObject().freeze(arg0);
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

  public void approve(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(319);
    Exception er = null;
    try {
      _getBeanObject().approve(arg0);
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

  public void approve0(String arg0) throws BusinessException {
    beforeCallMethod(320);
    Exception er = null;
    try {
      _getBeanObject().approve0(arg0);
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

  public void checkArsubValidity(Hashtable arg0, boolean arg1) throws BusinessException {
    beforeCallMethod(321);
    Exception er = null;
    try {
      _getBeanObject().checkArsubValidity(arg0, arg1);
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

  public void invoiceDelete(String arg0) throws BusinessException {
    beforeCallMethod(322);
    Exception er = null;
    try {
      _getBeanObject().invoiceDelete(arg0);
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
  }

  public Hashtable fillDataWithARSubAcct(Hashtable arg0) throws BusinessException {
    beforeCallMethod(323);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().fillDataWithARSubAcct(arg0);
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
  public void giveup(String arg0) throws BusinessException {
    beforeCallMethod(324);
    Exception er = null;
    try {
      _getBeanObject().giveup(arg0);
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

  public ArrayList insertInit(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(325);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insertInit(arg0);
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
  public void saveSaleInvoiceForImport(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(326);
    Exception er = null;
    try {
      _getBeanObject().saveSaleInvoiceForImport(arg0);
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

  public ArrayList updateInit(SaleinvoiceVO arg0) throws BusinessException {
    beforeCallMethod(327);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().updateInit(arg0);
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
  public void invoiceUpdateLock(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(328);
    Exception er = null;
    try {
      _getBeanObject().invoiceUpdateLock(arg0, arg1);
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

  public void writeToARSub(SaleinvoiceVO arg0, Hashtable arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(329);
    Exception er = null;
    try {
      _getBeanObject().writeToARSub(arg0, arg1, arg2);
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
  }

  public SaleinvoiceVO autoUniteInvoiceToUI(SaleinvoiceVO arg0, ClientLink arg1) throws BusinessException {
    beforeCallMethod(330);
    Exception er = null;
    SaleinvoiceVO o = null;
    try {
      o = _getBeanObject().autoUniteInvoiceToUI(arg0, arg1);
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
  public int getStatus(String arg0) throws BusinessException {
    beforeCallMethod(331);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().getStatus(arg0);
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
  public SaleinvoiceBVO[] queryBodyAllData(String arg0) throws BusinessException {
    beforeCallMethod(332);
    Exception er = null;
    SaleinvoiceBVO[] o = null;
    try {
      o = (SaleinvoiceBVO[])_getBeanObject().queryBodyAllData(arg0);
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
  public SaleVO[] queryHeadAllData(String arg0) throws BusinessException {
    beforeCallMethod(333);
    Exception er = null;
    SaleVO[] o = null;
    try {
      o = (SaleVO[])_getBeanObject().queryHeadAllData(arg0);
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
  public String[][] getLang(String[][] arg0, String arg1, int arg2, int arg3) {
    beforeCallMethod(334);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getLang(arg0, arg1, arg2, arg3);
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
    if (null != er)
    {
      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SaleVO[] queryAllData() throws BusinessException {
    beforeCallMethod(335);
    Exception er = null;
    SaleVO[] o = null;
    try {
      o = (SaleVO[])_getBeanObject().queryAllData();
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
  public SaleinvoiceVO queryData(String arg0) throws BusinessException {
    beforeCallMethod(336);
    Exception er = null;
    SaleinvoiceVO o = null;
    try {
      o = _getBeanObject().queryData(arg0);
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
  public Hashtable getOutNumber(Hashtable arg0, String arg1) throws BusinessException {
    beforeCallMethod(337);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().getOutNumber(arg0, arg1);
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
  public double getOutNumber(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(338);
    Exception er = null;
    double o = 0.0D;
    try {
      o = _getBeanObject().getOutNumber(arg0, arg1, arg2);
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
  public String getVerifyrule(String arg0) throws BusinessException {
    beforeCallMethod(339);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getVerifyrule(arg0);
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
  public SaleorderHVO[] queryOrder(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(340);
    Exception er = null;
    SaleorderHVO[] o = null;
    try {
      o = (SaleorderHVO[])_getBeanObject().queryOrder(arg0, arg1, arg2);
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
  public AggregatedValueObject querySourceBillVOForLinkAdd(String arg0, String arg1, String arg2, Object arg3) throws BusinessException {
    beforeCallMethod(341);
    Exception er = null;
    AggregatedValueObject o = null;
    try {
      o = _getBeanObject().querySourceBillVOForLinkAdd(arg0, arg1, arg2, arg3);
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
  public SaleVO[] queryAllHeadData(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(342);
    Exception er = null;
    SaleVO[] o = null;
    try {
      o = (SaleVO[])_getBeanObject().queryAllHeadData(arg0, arg1, arg2);
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
  public AvgSaleQueryVO getAvgSaleData(AvgSaleQueryVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(343);
    Exception er = null;
    AvgSaleQueryVO o = null;
    try {
      o = _getBeanObject().getAvgSaleData(arg0, arg1);
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
  public String[][] getCustomerInfo(String arg0) throws BusinessException {
    beforeCallMethod(344);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getCustomerInfo(arg0);
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
  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(345);
    Exception er = null;
    SaleinvoiceVO o = null;
    try {
      o = _getBeanObject().getDataFrom4CTo32(arg0, arg1);
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
  public String[][] getInventoryInfo(String arg0) throws BusinessException {
    beforeCallMethod(346);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getInventoryInfo(arg0);
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
  public String getInvoiceCode(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(347);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getInvoiceCode(arg0, arg1);
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
  public double getInvoiceNumber(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(348);
    Exception er = null;
    double o = 0.0D;
    try {
      o = _getBeanObject().getInvoiceNumber(arg0, arg1);
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
  public String[][] getInvoiceType() throws BusinessException {
    beforeCallMethod(349);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getInvoiceType();
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
  public String[] getOrderNumber(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(350);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getOrderNumber(arg0, arg1);
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
  public String[][] getWarehouseInfo(String arg0) throws BusinessException {
    beforeCallMethod(351);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().getWarehouseInfo(arg0);
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
  public boolean isCodeExist(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(352);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isCodeExist(arg0, arg1);
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
  public Hashtable queryBillCodeBySource(int arg0, String arg1) throws BusinessException {
    beforeCallMethod(353);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().queryBillCodeBySource(arg0, arg1);
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
  public SaleinvoiceBVO[] queryBodyData(String arg0) throws BusinessException {
    beforeCallMethod(354);
    Exception er = null;
    SaleinvoiceBVO[] o = null;
    try {
      o = (SaleinvoiceBVO[])_getBeanObject().queryBodyData(arg0);
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
  public SaleinvoiceVO[] queryDataBills(String[] arg0) throws BusinessException {
    beforeCallMethod(355);
    Exception er = null;
    SaleinvoiceVO[] o = null;
    try {
      o = (SaleinvoiceVO[])_getBeanObject().queryDataBills(arg0);
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
  public SaleVO queryHeadData(String arg0) throws BusinessException {
    beforeCallMethod(356);
    Exception er = null;
    SaleVO o = null;
    try {
      o = _getBeanObject().queryHeadData(arg0);
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
  public SaleinvoiceBVO[] queryInitBodyData(String arg0) throws BusinessException {
    beforeCallMethod(357);
    Exception er = null;
    SaleinvoiceBVO[] o = null;
    try {
      o = (SaleinvoiceBVO[])_getBeanObject().queryInitBodyData(arg0);
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
  public SaleinvoiceVO queryInitData(String arg0) throws BusinessException {
    beforeCallMethod(358);
    Exception er = null;
    SaleinvoiceVO o = null;
    try {
      o = _getBeanObject().queryInitData(arg0);
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
  public SaleorderHVO[] queryInitOrderData(String arg0) throws BusinessException {
    beforeCallMethod(359);
    Exception er = null;
    SaleorderHVO[] o = null;
    try {
      o = (SaleorderHVO[])_getBeanObject().queryInitOrderData(arg0);
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
  public SaleorderBVO[] queryOrderBodyList(String arg0) throws BusinessException {
    beforeCallMethod(360);
    Exception er = null;
    SaleorderBVO[] o = null;
    try {
      o = (SaleorderBVO[])_getBeanObject().queryOrderBodyList(arg0);
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
  public SaleorderHVO[] queryOrderData(String arg0) throws BusinessException {
    beforeCallMethod(361);
    Exception er = null;
    SaleorderHVO[] o = null;
    try {
      o = (SaleorderHVO[])_getBeanObject().queryOrderData(arg0);
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
  public SaleorderHVO[] queryOrderList(String arg0) throws BusinessException {
    beforeCallMethod(362);
    Exception er = null;
    SaleorderHVO[] o = null;
    try {
      o = (SaleorderHVO[])_getBeanObject().queryOrderList(arg0);
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
  public SaleinvoiceVO[] queryOrderStore(SaleinvoiceVO[] arg0) throws BusinessException {
    beforeCallMethod(363);
    Exception er = null;
    SaleinvoiceVO[] o = null;
    try {
      o = (SaleinvoiceVO[])_getBeanObject().queryOrderStore(arg0);
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
  public Hashtable queryStrikeData(String arg0) throws BusinessException {
    beforeCallMethod(364);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().queryStrikeData(arg0);
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
  public AggregatedValueObject[] queryDataByWhere(String arg0) throws BusinessException {
    beforeCallMethod(365);
    Exception er = null;
    AggregatedValueObject[] o = null;
    try {
      o = (AggregatedValueObject[])_getBeanObject().queryDataByWhere(arg0);
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
  public void send(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(366);
    Exception er = null;
    try {
      _getBeanObject().send(arg0, arg1, arg2, arg3, arg4);
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
  }

  public void delete(SquareVO arg0) throws BusinessException {
    beforeCallMethod(367);
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
  }

  public String insert(SquareVO arg0) throws BusinessException {
    beforeCallMethod(368);
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
  public void update(SquareVO arg0) throws BusinessException {
    beforeCallMethod(369);
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
  }

  public Object[] batchSquare(SquareVO[] arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(370);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().batchSquare(arg0, arg1);
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
  public Object[] batchUnSquare(SquareVO[] arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(371);
    Exception er = null;
    Object[] o = null;
    try {
      o = (Object[])_getBeanObject().batchUnSquare(arg0, arg1);
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
  public Object exeSquareBalance(SquareVO arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(372);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().exeSquareBalance(arg0, arg1);
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
  public Object exeUnSquareBalance(SquareVO arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(373);
    Exception er = null;
    Object o = null;
    try {
      o = _getBeanObject().exeUnSquareBalance(arg0, arg1);
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
  public boolean setAbnormal(SquareVO arg0) throws BusinessException {
    beforeCallMethod(374);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().setAbnormal(arg0);
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
  public void squareByInvoice(SquareVO arg0) throws BusinessException {
    beforeCallMethod(375);
    Exception er = null;
    try {
      _getBeanObject().squareByInvoice(arg0);
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
  }

  public void squareByOut(SquareVO arg0) throws BusinessException {
    beforeCallMethod(376);
    Exception er = null;
    try {
      _getBeanObject().squareByOut(arg0);
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
  }

  public void Ic4453toSquare(WastageBillVO[] arg0) throws BusinessException {
    beforeCallMethod(377);
    Exception er = null;
    try {
      _getBeanObject().Ic4453toSquare(arg0);
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
  }

  public void Ic4453toUnSquare(WastageBillVO[] arg0) throws BusinessException {
    beforeCallMethod(378);
    Exception er = null;
    try {
      _getBeanObject().Ic4453toUnSquare(arg0);
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
  }

  public SquareVO[] changeTotalToSquareVO(SquareTotalVO[] arg0) throws BusinessException {
    beforeCallMethod(379);
    Exception er = null;
    SquareVO[] o = null;
    try {
      o = (SquareVO[])_getBeanObject().changeTotalToSquareVO(arg0);
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
  public TurnOverBVO[] TurnOver_setNum(TurnOverBVO[] arg0, UFDouble arg1, UFDouble arg2, UFDouble arg3, UFDate arg4, UFDate arg5, String arg6, String arg7) throws BusinessException, Exception {
    beforeCallMethod(380);
    Exception er = null;
    TurnOverBVO[] o = null;
    try {
      o = (TurnOverBVO[])_getBeanObject().TurnOver_setNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
  public TurnOverBVO[] TypeRateABC_setNum(TurnOverBVO[] arg0, UFDouble arg1, UFDouble arg2, UFDouble arg3, UFDate arg4, UFDate arg5, String arg6, String arg7) throws BusinessException {
    beforeCallMethod(381);
    Exception er = null;
    TurnOverBVO[] o = null;
    try {
      o = (TurnOverBVO[])_getBeanObject().TypeRateABC_setNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
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
    return o;
  }
  public void delete(SalespromotionVO arg0) throws RemoteException {
    beforeCallMethod(382);
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
      afterCallMethod(382, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public SalespromotionVO insert(SalespromotionVO arg0) throws RemoteException {
    beforeCallMethod(383);
    Exception er = null;
    SalespromotionVO o = null;
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
      afterCallMethod(383, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SalespromotionVO update(SalespromotionVO arg0) throws RemoteException {
    beforeCallMethod(384);
    Exception er = null;
    SalespromotionVO o = null;
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
      afterCallMethod(384, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SalespromotionVO audit(SalespromotionVO arg0) throws RemoteException {
    beforeCallMethod(385);
    Exception er = null;
    SalespromotionVO o = null;
    try {
      o = _getBeanObject().audit(arg0);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SalespromotionVO unAudit(SalespromotionVO arg0) throws RemoteException, BusinessException {
    beforeCallMethod(386);
    Exception er = null;
    SalespromotionVO o = null;
    try {
      o = _getBeanObject().unAudit(arg0);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void unite(String arg0, String arg1, String arg2, String arg3, UFDouble arg4, String arg5, ClientLink arg6) throws RemoteException {
    beforeCallMethod(387);
    Exception er = null;
    try {
      _getBeanObject().unite(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
  }

  public SalespromotionVO blankout(SalespromotionVO arg0) throws RemoteException {
    beforeCallMethod(388);
    Exception er = null;
    SalespromotionVO o = null;
    try {
      o = _getBeanObject().blankout(arg0);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getInvRefPrice(String arg0) throws RemoteException {
    beforeCallMethod(389);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getInvRefPrice(arg0);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList reduceRMCall(ArrayList arg0) throws RemoteException, BusinessException {
    beforeCallMethod(390);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().reduceRMCall(arg0);
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
      if ((er instanceof RemoteException)) {
        throw ((RemoteException)er);
      }
      if ((er instanceof BusinessException)) {
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