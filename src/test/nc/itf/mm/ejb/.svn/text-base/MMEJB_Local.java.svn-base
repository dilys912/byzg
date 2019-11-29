package nc.itf.mm.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.exception.FrameworkEJBException;
import nc.bs.logging.Logger;
import nc.bs.mw.naming.BeanBase;
import nc.vo.bd.b422.BclbItemVO;
import nc.vo.bd.b423.ScVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.dm.dm1010.XtbomVO;
import nc.vo.dm.dm1020.XtfpVO;
import nc.vo.dm.dm1030.JhbomItemVO;
import nc.vo.dm.dm1030.JhbomVO;
import nc.vo.dm.dm1040.InvmandocVO;
import nc.vo.dm.dm1040.PzbomVO;
import nc.vo.dm.dm1040.PzbomZpVO;
import nc.vo.dm.dm3010.XsycClearConditionVO;
import nc.vo.dm.dm3010.XsycLoadConditionVO;
import nc.vo.dm.dm3010.XsycQueryConditionVO;
import nc.vo.dm.dm3020.LoadReportVO;
import nc.vo.dm.dm3020.UpdateDataVO;
import nc.vo.dm.dm4060.DeptTotalVO;
import nc.vo.dm.dm4060.WkTotalVO;
import nc.vo.ds.ds1010.GzzxjxVO;
import nc.vo.fs.fs1010.AbomHeaderViewVO;
import nc.vo.fs.fs1010.AbomItemViewVO;
import nc.vo.fs.fs1010.AbomVO;
import nc.vo.fs.fs1010.AbomZpVO;
import nc.vo.fs.fs1020.AbomVOComposite;
import nc.vo.fs.fs1020.OrderBaseInfoVO;
import nc.vo.fs.fs1020.OrderState;
import nc.vo.me.me1020.JldHeaderVO;
import nc.vo.me.me1020.JldVO;
import nc.vo.me.me1030.HhzxHeaderVO;
import nc.vo.me.me1030.HhzxItemVO;
import nc.vo.me.me1030.HhzxVO;
import nc.vo.me.me1040.JlbsjVO;
import nc.vo.me.me1050.JldsjHeaderVO;
import nc.vo.me.me1050.JldsjItemVO;
import nc.vo.me.me1050.JldsjVO;
import nc.vo.me.me1060.CountParaVO;
import nc.vo.me.me1070.JldsjBVO;
import nc.vo.me.me2030.JlcsjVO;
import nc.vo.me.me2040.JlctcHeaderVO;
import nc.vo.me.me2040.JlctcItemVO;
import nc.vo.me.me2040.JlctcVO;
import nc.vo.me.me3010.GzzxztVO;
import nc.vo.me.me3020.DdxmdyVO;
import nc.vo.me.me3020.FsbgxmVO;
import nc.vo.me.me3030.YieldInfoVO;
import nc.vo.me.me3040.DdzxVO;
import nc.vo.me.me3040.FsbgVO;
import nc.vo.me.me3040.MassVO;
import nc.vo.me.me3070.XfyaVO;
import nc.vo.me.me3071.JkzxVO;
import nc.vo.mm.pub.FreeItemVO;
import nc.vo.mm.pub.KzcsVO;
import nc.vo.mm.pub.MmReeditLogCircularlyAcccessibleVO;
import nc.vo.mm.pub.pub1020.BomHeaderVO;
import nc.vo.mm.pub.pub1020.BomVO;
import nc.vo.mm.pub.pub1020.DisItemVO;
import nc.vo.mm.pub.pub1020.LwVO;
import nc.vo.mm.pub.pub1020.MMInputVO;
import nc.vo.mm.pub.pub1020.RtItemVO;
import nc.vo.mm.pub.pub1020.SourceVO;
import nc.vo.mm.pub.pub1020.WkVO;
import nc.vo.mm.pub.pub1025.HbclItemVO;
import nc.vo.mm.pub.pub1025.HbclVO;
import nc.vo.mm.pub.pub1025.XqfjVO;
import nc.vo.mm.pub.pub1025.XqlyVO;
import nc.vo.mm.pub.pub1025.XsddVO;
import nc.vo.mm.pub.pub1025.XsycVO;
import nc.vo.mm.pub.pub1025.YhjhVO;
import nc.vo.mm.pub.pub1030.CetzHeaderVO;
import nc.vo.mm.pub.pub1030.CetzItemVO;
import nc.vo.mm.pub.pub1030.CetzVO;
import nc.vo.mm.pub.pub1030.CostRtVO;
import nc.vo.mm.pub.pub1030.GxbcVO;
import nc.vo.mm.pub.pub1030.GxjhVO;
import nc.vo.mm.pub.pub1030.HandandtakeUnitVO;
import nc.vo.mm.pub.pub1030.JslxszVO;
import nc.vo.mm.pub.pub1030.LbVO;
import nc.vo.mm.pub.pub1030.LbzdVO;
import nc.vo.mm.pub.pub1030.MOSourceVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoItemVO;
import nc.vo.mm.pub.pub1030.PgaHeaderVO;
import nc.vo.mm.pub.pub1030.PgaItemVO;
import nc.vo.mm.pub.pub1030.PgaVO;
import nc.vo.mm.pub.pub1030.PgdItemVO;
import nc.vo.mm.pub.pub1030.PgdVO;
import nc.vo.mm.pub.pub1030.PickmHeaderVO;
import nc.vo.mm.pub.pub1030.PickmItemVO;
import nc.vo.mm.pub.pub1030.SflbdzVO;
import nc.vo.mm.pub.pub1030.TsxszVO;
import nc.vo.mm.pub.pub1030.ZylVO;
import nc.vo.mm.pub.pub1040.JxdHeaderVO;
import nc.vo.mm.pub.pub1040.JxdItemVO;
import nc.vo.mm.pub.pub1040.JxdVO;
import nc.vo.mm.pub.pub1040.PlacedocHeaderVO;
import nc.vo.mm.pub.pub1040.PlacedocItemVO;
import nc.vo.mm.pub.pub1040.PlacedocVO;
import nc.vo.mm.pub.pub1040.ProductfactoryVO;
import nc.vo.mm.pub.pub1040.SupplyRatioVO;
import nc.vo.mo.mo1020.FrockInvVO;
import nc.vo.mo.mo1020.ProductVO;
import nc.vo.mo.mo1020.WantInvVO;
import nc.vo.mo.mo1022.MORecursiveVO;
import nc.vo.mo.mo1030.QualityReportVO;
import nc.vo.mo.mo1030.WrVO;
import nc.vo.mo.mo1032.GzglHeaderVO;
import nc.vo.mo.mo1032.GzglItemVO;
import nc.vo.mo.mo1034.ZlgzHeadVO;
import nc.vo.mo.mo1034.ZlgzItemVO;
import nc.vo.mo.mo1034.ZlgzVO;
import nc.vo.mo.mo1036.WgmxVO;
import nc.vo.mo.mo1050.MoStatVO;
import nc.vo.mo.mo1050.StatConditionVO;
import nc.vo.mo.mo1090.QryScddVO;
import nc.vo.mo.mo1091.ZzcxVO;
import nc.vo.mo.mo2010.InvreplVO;
import nc.vo.mo.mo2030.PickmBVO;
import nc.vo.mo.mo2035.PickmUnitVO;
import nc.vo.mp.mp2015.RlistVO;
import nc.vo.mp.mp2015.WlPropVO;
import nc.vo.mp.mp2040.NljhVO;
import nc.vo.mp.mp3010.ReleaseVO;
import nc.vo.mp.mp4010.AnalysisConditionVO;
import nc.vo.mp.mp4010.AnalysisResultVO;
import nc.vo.mp.mp4020.MpsBillVO;
import nc.vo.mp.mp4040.GyxqVO;
import nc.vo.mr.mr1210.CgzxClearConditionVO;
import nc.vo.mr.mr1210.CgzxInitVO;
import nc.vo.mr.mr1210.CgzxLoadConditionVO;
import nc.vo.mr.mr1210.CgzxQueryConditionVO;
import nc.vo.mr.mr1210.CgzxVO;
import nc.vo.mr.mr2027.MaterialVO;
import nc.vo.mr.mr2027.WkScheduleInputVO;
import nc.vo.mr.mr2027.WkScheduleResultVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.qc.pub.QcresultVO;
import nc.vo.sf.sf1020.DynamicKhwlszVO;
import nc.vo.sf.sf1020.KhwlszVO;
import nc.vo.sf.sf2010.GxjhInitVO;
import nc.vo.sf.sf2010.PgdCreateConditionVO;
import nc.vo.sf.sf2010.WorkoutConditionVO;
import nc.vo.sf.sf2020.BatchUpdateCtrlVO;
import nc.vo.sf.sf2020.UnitToSingleVO;
import nc.vo.sf.sf2030.CdDataVO;
import nc.vo.sf.sf2035.TotalQueryVO;
import nc.vo.sf.sf2040.ProdNumForSFHeaderVO;
import nc.vo.sf.sf2040.ProdNumForSFVO;
import nc.vo.sf.sf2050.ZzVO;
import nc.vo.sf.sf2060.GxzxqkVO;
import nc.vo.sf.sf2070.TaskShowVO;
import nc.vo.sf.sf2071.GxlckHeaderVO;
import nc.vo.sf.sf2071.GxlckItemVO;
import nc.vo.sf.sf2072.GxImplementInfoVO;
import nc.vo.sm.UserVO;

public class MMEJB_Local extends BeanBase
  implements MMEJBEjbObject
{
  private MMEJBEjbBean _getBeanObject()
    throws RemoteException
  {
    return (MMEJBEjbBean)getEjb();
  }

  public void deleteAll(AbomHeaderViewVO arg0) throws BusinessException {
    beforeCallMethod(200);
    Exception er = null;
    try {
      _getBeanObject().deleteAll(arg0);
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

  public AbomItemViewVO[] findAbomItemsForHead(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(201);
    Exception er = null;
    AbomItemViewVO[] o = null;
    try {
      o = (AbomItemViewVO[])_getBeanObject().findAbomItemsForHead(arg0, arg1);
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
  public AbomVO findByAbombid(String arg0) throws BusinessException {
    beforeCallMethod(202);
    Exception er = null;
    AbomVO o = null;
    try {
      o = _getBeanObject().findByAbombid(arg0);
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
  public AbomVO findByAbomid(String arg0) throws BusinessException {
    beforeCallMethod(203);
    Exception er = null;
    AbomVO o = null;
    try {
      o = _getBeanObject().findByAbomid(arg0);
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
  public AbomVO findFromRoot(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(204);
    Exception er = null;
    AbomVO o = null;
    try {
      o = _getBeanObject().findFromRoot(arg0, arg1, arg2, arg3);
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
  public AbomZpVO[] findAbomZpByAbombid(String arg0) throws BusinessException {
    beforeCallMethod(205);
    Exception er = null;
    AbomZpVO[] o = null;
    try {
      o = (AbomZpVO[])_getBeanObject().findAbomZpByAbombid(arg0);
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
  public void updateAbomHeaderVO(AbomHeaderViewVO arg0) throws BusinessException {
    beforeCallMethod(206);
    Exception er = null;
    try {
      _getBeanObject().updateAbomHeaderVO(arg0);
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
  }

  public String cancelAbom(String arg0, XsddVO[] arg1) throws BusinessException {
    beforeCallMethod(207);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().cancelAbom(arg0, arg1);
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
  public boolean isDefaultJldw(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(208);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isDefaultJldw(arg0, arg1);
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
  public void delete_Abom(AbomVO arg0) throws BusinessException {
    beforeCallMethod(209);
    Exception er = null;
    try {
      _getBeanObject().delete_Abom(arg0);
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

  public String[] insert_Abom(AbomVO arg0) throws BusinessException {
    beforeCallMethod(210);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insert_Abom(arg0);
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
  public String[] update_Abom(AbomVO arg0) throws BusinessException {
    beforeCallMethod(211);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().update_Abom(arg0);
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
  public AnalysisResultVO[] buildAnalysisResults(AnalysisConditionVO arg0) throws BusinessException {
    beforeCallMethod(212);
    Exception er = null;
    AnalysisResultVO[] o = null;
    try {
      o = (AnalysisResultVO[])_getBeanObject().buildAnalysisResults(arg0);
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
  public ArrayList addCetzBill(CetzHeaderVO arg0) throws BusinessException {
    beforeCallMethod(213);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().addCetzBill(arg0);
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
  public CetzVO[] auditCetzBill(CetzHeaderVO[] arg0, String arg1, String arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(214);
    Exception er = null;
    CetzVO[] o = null;
    try {
      o = (CetzVO[])_getBeanObject().auditCetzBill(arg0, arg1, arg2, arg3);
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
  public CetzVO[] createCetzDetail(CetzHeaderVO[] arg0, ConditionVO[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(215);
    Exception er = null;
    CetzVO[] o = null;
    try {
      o = (CetzVO[])_getBeanObject().createCetzDetail(arg0, arg1, arg2);
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
  public void deleteCetzBill(CetzHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(216);
    Exception er = null;
    try {
      _getBeanObject().deleteCetzBill(arg0, arg1);
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

  public CetzVO[] deleteCetzDetailForHeader(CetzHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(217);
    Exception er = null;
    CetzVO[] o = null;
    try {
      o = (CetzVO[])_getBeanObject().deleteCetzDetailForHeader(arg0, arg1);
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
  public CetzVO[] execteCetz(CetzHeaderVO[] arg0, String arg1, String arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(218);
    Exception er = null;
    CetzVO[] o = null;
    try {
      o = (CetzVO[])_getBeanObject().execteCetz(arg0, arg1, arg2, arg3);
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
  public CetzHeaderVO[] findCetzBillByCond(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(219);
    Exception er = null;
    CetzHeaderVO[] o = null;
    try {
      o = (CetzHeaderVO[])_getBeanObject().findCetzBillByCond(arg0);
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
  public CetzItemVO[] findCetzDetailByHeadkey(String arg0) throws BusinessException {
    beforeCallMethod(220);
    Exception er = null;
    CetzItemVO[] o = null;
    try {
      o = (CetzItemVO[])_getBeanObject().findCetzDetailByHeadkey(arg0);
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
  public CetzVO[] unAuditCetzBill(CetzHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(221);
    Exception er = null;
    CetzVO[] o = null;
    try {
      o = (CetzVO[])_getBeanObject().unAuditCetzBill(arg0, arg1);
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
  public void updateCetzBill(CetzHeaderVO arg0) throws BusinessException {
    beforeCallMethod(222);
    Exception er = null;
    try {
      _getBeanObject().updateCetzBill(arg0);
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

  public ArrayList update_Cetz(CetzVO arg0) throws BusinessException {
    beforeCallMethod(223);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Cetz(arg0);
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
  public void clear(CgzxClearConditionVO arg0) throws BusinessException {
    beforeCallMethod(224);
    Exception er = null;
    try {
      _getBeanObject().clear(arg0);
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
  }

  public Integer load(CgzxLoadConditionVO arg0) throws BusinessException {
    beforeCallMethod(225);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().load(arg0);
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
  public String[] save(CgzxVO[] arg0) throws BusinessException {
    beforeCallMethod(226);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().save(arg0);
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
  public CgzxVO[] queryByConditionVO(CgzxQueryConditionVO arg0) throws BusinessException {
    beforeCallMethod(227);
    Exception er = null;
    CgzxVO[] o = null;
    try {
      o = (CgzxVO[])_getBeanObject().queryByConditionVO(arg0);
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
  public CgzxVO findByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(228);
    Exception er = null;
    CgzxVO o = null;
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
  public String[] insertArray(CgzxVO[] arg0) throws BusinessException {
    beforeCallMethod(229);
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
  public CgzxInitVO getInitData_Cgzx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(230);
    Exception er = null;
    CgzxInitVO o = null;
    try {
      o = _getBeanObject().getInitData_Cgzx(arg0, arg1);
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
  public CgzxVO[] queryAll_Cgzx(String arg0) throws BusinessException {
    beforeCallMethod(231);
    Exception er = null;
    CgzxVO[] o = null;
    try {
      o = (CgzxVO[])_getBeanObject().queryAll_Cgzx(arg0);
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
  public void delete_Cgzx(CgzxVO arg0) throws BusinessException {
    beforeCallMethod(232);
    Exception er = null;
    try {
      _getBeanObject().delete_Cgzx(arg0);
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

  public String insert_Cgzx(CgzxVO arg0) throws BusinessException {
    beforeCallMethod(233);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Cgzx(arg0);
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
  public void update_Cgzx(CgzxVO arg0) throws BusinessException {
    beforeCallMethod(234);
    Exception er = null;
    try {
      _getBeanObject().update_Cgzx(arg0);
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

  public String[] doSave(DdzxVO arg0, CircularlyAccessibleValueObject[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(235);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().doSave(arg0, arg1, arg2);
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
  public FsbgVO[] makeBlankFsbgVOs(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(236);
    Exception er = null;
    FsbgVO[] o = null;
    try {
      o = (FsbgVO[])_getBeanObject().makeBlankFsbgVOs(arg0, arg1, arg2, arg3);
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
  public DdzxVO[] queryDdzxsByWhere(String arg0) throws BusinessException {
    beforeCallMethod(237);
    Exception er = null;
    DdzxVO[] o = null;
    try {
      o = (DdzxVO[])_getBeanObject().queryDdzxsByWhere(arg0);
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
  public String insertDdzx(DdzxVO arg0) throws BusinessException {
    beforeCallMethod(238);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertDdzx(arg0);
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
  public void updateDdzx(DdzxVO arg0) throws BusinessException {
    beforeCallMethod(239);
    Exception er = null;
    try {
      _getBeanObject().updateDdzx(arg0);
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

  public PgaHeaderVO findPgaHeader(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(240);
    Exception er = null;
    PgaHeaderVO o = null;
    try {
      o = _getBeanObject().findPgaHeader(arg0, arg1, arg2);
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
  public MassVO getMassVOByPK(String arg0) throws BusinessException {
    beforeCallMethod(241);
    Exception er = null;
    MassVO o = null;
    try {
      o = _getBeanObject().getMassVOByPK(arg0);
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
  public String getBillTempletID(String arg0) throws BusinessException {
    beforeCallMethod(242);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getBillTempletID(arg0);
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
  public ArrayList getExceptionBasic(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(243);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getExceptionBasic(arg0, arg1, arg2, arg3);
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
  public FsbgxmVO[] findFsbgxmVOs(String arg0) throws BusinessException {
    beforeCallMethod(244);
    Exception er = null;
    FsbgxmVO[] o = null;
    try {
      o = (FsbgxmVO[])_getBeanObject().findFsbgxmVOs(arg0);
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
  public BclbItemVO findBclbItemVO(String arg0) throws BusinessException {
    beforeCallMethod(245);
    Exception er = null;
    BclbItemVO o = null;
    try {
      o = _getBeanObject().findBclbItemVO(arg0);
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
  public UFBoolean validateAuthority(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(246);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().validateAuthority(arg0, arg1, arg2);
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
  public void delete_Dbyz(DdzxVO arg0) throws BusinessException {
    beforeCallMethod(247);
    Exception er = null;
    try {
      _getBeanObject().delete_Dbyz(arg0);
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

  public DdxmdyVO findByWkClassid(String arg0) throws BusinessException {
    beforeCallMethod(248);
    Exception er = null;
    DdxmdyVO o = null;
    try {
      o = _getBeanObject().findByWkClassid(arg0);
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
  public void delete_Ddxmdy(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(249);
    Exception er = null;
    try {
      _getBeanObject().delete_Ddxmdy(arg0, arg1);
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

  public ArrayList insert_Ddxmdy(DdxmdyVO arg0) throws BusinessException {
    beforeCallMethod(250);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Ddxmdy(arg0);
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
  public ArrayList update_Ddxmdy(DdxmdyVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(251);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Ddxmdy(arg0, arg1);
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
  public UFDouble[] getExisting(String arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(252);
    Exception er = null;
    UFDouble[] o = null;
    try {
      o = (UFDouble[])_getBeanObject().getExisting(arg0, arg1, arg2);
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
  public JxdHeaderVO[] findByQueryCondition(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(253);
    Exception er = null;
    JxdHeaderVO[] o = null;
    try {
      o = (JxdHeaderVO[])_getBeanObject().findByQueryCondition(arg0);
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
  public JxdItemVO[] findItemsByFjbw(String arg0) throws BusinessException {
    beforeCallMethod(254);
    Exception er = null;
    JxdItemVO[] o = null;
    try {
      o = (JxdItemVO[])_getBeanObject().findItemsByFjbw(arg0);
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
  public String auditFjbw(String arg0, Integer arg1, String arg2, String arg3, UFBoolean arg4, UFDate arg5) throws BusinessException {
    beforeCallMethod(255);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().auditFjbw(arg0, arg1, arg2, arg3, arg4, arg5);
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
    return o;
  }
  public UFBoolean isExistFjbwForJxd(String arg0) throws BusinessException {
    beforeCallMethod(256);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isExistFjbwForJxd(arg0);
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
  public String[] makeMoAndPraybill(JxdItemVO[] arg0, JxdHeaderVO arg1, UserVO arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(257);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().makeMoAndPraybill(arg0, arg1, arg2, arg3);
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
  public String[] unMakeMoAndPraybill(JxdItemVO[] arg0, JxdHeaderVO arg1, UserVO arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(258);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().unMakeMoAndPraybill(arg0, arg1, arg2, arg3);
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
  public void delete_Fjbw(JxdHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(259);
    Exception er = null;
    try {
      _getBeanObject().delete_Fjbw(arg0, arg1);
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

  public String[] insert_Fjbw(JxdVO arg0) throws BusinessException {
    beforeCallMethod(260);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insert_Fjbw(arg0);
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
  public String update_Fjbw(JxdVO arg0) throws BusinessException {
    beforeCallMethod(261);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().update_Fjbw(arg0);
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
  public GxImplementInfoVO[] queryGxImplementInfo(String arg0) throws BusinessException {
    beforeCallMethod(262);
    Exception er = null;
    GxImplementInfoVO[] o = null;
    try {
      o = (GxImplementInfoVO[])_getBeanObject().queryGxImplementInfo(arg0);
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
  public String start(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(263);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().start(arg0);
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
  public ArrayList update(UnitToSingleVO arg0, GxbcVO[] arg1) throws BusinessException {
    beforeCallMethod(264);
    Exception er = null;
    ArrayList o = null;
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
  public String finish(UnitToSingleVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(265);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().finish(arg0, arg1, arg2);
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
  public PgdVO dealSerialAndWhole(PgdVO arg0) throws BusinessException {
    beforeCallMethod(266);
    Exception er = null;
    PgdVO o = null;
    try {
      o = _getBeanObject().dealSerialAndWhole(arg0);
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
  public ArrayList findBodyByPgd(String arg0, String arg1, String arg2, String arg3, UFDate arg4, String arg5, UFBoolean arg6) throws BusinessException {
    beforeCallMethod(267);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findBodyByPgd(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  public Vector findWtXh(String arg0, String arg1, String arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(268);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().findWtXh(arg0, arg1, arg2, arg3);
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
  public ArrayList finishJjd(UnitToSingleVO arg0, HandandtakeUnitVO[] arg1) throws BusinessException {
    beforeCallMethod(269);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().finishJjd(arg0, arg1);
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
  public ArrayList getBaseSz(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(270);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getBaseSz(arg0, arg1);
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
  public ArrayList getJjd(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(271);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getJjd(arg0, arg1, arg2, arg3, arg4);
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
  public HandandtakeUnitVO[] getJqgxJjd(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(272);
    Exception er = null;
    HandandtakeUnitVO[] o = null;
    try {
      o = (HandandtakeUnitVO[])_getBeanObject().getJqgxJjd(arg0, arg1, arg2, arg3, arg4);
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
  public ProdNumForSFHeaderVO[] getWgSlForCm(String arg0, String arg1, UFDate arg2, UFDate arg3, String[] arg4) throws BusinessException {
    beforeCallMethod(273);
    Exception er = null;
    ProdNumForSFHeaderVO[] o = null;
    try {
      o = (ProdNumForSFHeaderVO[])_getBeanObject().getWgSlForCm(arg0, arg1, arg2, arg3, arg4);
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
  public ArrayList getZylForCm(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(274);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getZylForCm(arg0, arg1, arg2, arg3);
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
  public ProdNumForSFVO[] getZzSlForCm(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(275);
    Exception er = null;
    ProdNumForSFVO[] o = null;
    try {
      o = (ProdNumForSFVO[])_getBeanObject().getZzSlForCm(arg0, arg1, arg2, arg3);
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
  public Boolean isAllPgdFinished(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(276);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().isAllPgdFinished(arg0, arg1);
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
  public String unStart(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(277);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().unStart(arg0);
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
  public ArrayList updateGxWgbg(UnitToSingleVO arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(278);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().updateGxWgbg(arg0, arg1);
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
  public void pgdJs(HandandtakeUnitVO[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(279);
    Exception er = null;
    try {
      _getBeanObject().pgdJs(arg0, arg1, arg2, arg3, arg4);
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
  }

  public void pgdUnJs(HandandtakeUnitVO[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(280);
    Exception er = null;
    try {
      _getBeanObject().pgdUnJs(arg0, arg1, arg2, arg3, arg4);
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
  }

  public ArrayList pgdYj(HandandtakeUnitVO arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(281);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().pgdYj(arg0, arg1, arg2, arg3, arg4);
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
  public void pgdUnYj(HandandtakeUnitVO arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(282);
    Exception er = null;
    try {
      _getBeanObject().pgdUnYj(arg0, arg1, arg2, arg3, arg4);
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
  }

  public String unfinish(UnitToSingleVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(283);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().unfinish(arg0, arg1);
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
  public UFBoolean checkScddZt(String arg0) throws BusinessException {
    beforeCallMethod(284);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().checkScddZt(arg0);
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
  public void saveHjy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(285);
    Exception er = null;
    try {
      _getBeanObject().saveHjy(arg0, arg1, arg2);
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

  public void saveJyy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(286);
    Exception er = null;
    try {
      _getBeanObject().saveJyy(arg0, arg1, arg2);
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
  }

  public void saveSjxx(String arg0, UFDateTime arg1, String arg2, UFDateTime arg3, String arg4, UFDateTime arg5, String arg6) throws BusinessException {
    beforeCallMethod(287);
    Exception er = null;
    try {
      _getBeanObject().saveSjxx(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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

  public void savePjzyy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(288);
    Exception er = null;
    try {
      _getBeanObject().savePjzyy(arg0, arg1, arg2);
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
  }

  public void savePjhjy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(289);
    Exception er = null;
    try {
      _getBeanObject().savePjhjy(arg0, arg1, arg2);
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
  }

  public void savePjjyy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(290);
    Exception er = null;
    try {
      _getBeanObject().savePjjyy(arg0, arg1, arg2);
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

  public void savePjxx(String arg0, UFDateTime arg1, String arg2, UFDateTime arg3, String arg4, UFDateTime arg5, String arg6) throws BusinessException {
    beforeCallMethod(291);
    Exception er = null;
    try {
      _getBeanObject().savePjxx(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  }

  public void saveZyy(String arg0, UFDateTime arg1, String arg2) throws BusinessException {
    beforeCallMethod(292);
    Exception er = null;
    try {
      _getBeanObject().saveZyy(arg0, arg1, arg2);
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
  }

  public GxlckHeaderVO[] queryGxlckHeaders(String arg0) throws BusinessException {
    beforeCallMethod(293);
    Exception er = null;
    GxlckHeaderVO[] o = null;
    try {
      o = (GxlckHeaderVO[])_getBeanObject().queryGxlckHeaders(arg0);
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
  public GxlckItemVO[] queryGxlckItems(GxlckHeaderVO arg0) throws BusinessException {
    beforeCallMethod(294);
    Exception er = null;
    GxlckItemVO[] o = null;
    try {
      o = (GxlckItemVO[])_getBeanObject().queryGxlckItems(arg0);
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
  public GxzxqkVO[] queryGxzxqk(String arg0) throws BusinessException {
    beforeCallMethod(295);
    Exception er = null;
    GxzxqkVO[] o = null;
    try {
      o = (GxzxqkVO[])_getBeanObject().queryGxzxqk(arg0);
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
  public GyxqVO[] statGyxq(String arg0, String arg1, String arg2, nc.vo.mp.mp4020.InvInfoVO arg3, boolean arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(296);
    Exception er = null;
    GyxqVO[] o = null;
    try {
      o = (GyxqVO[])_getBeanObject().statGyxq(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  public GzzxjxVO[] queryAll_Gzzxjx(String arg0) throws BusinessException {
    beforeCallMethod(297);
    Exception er = null;
    GzzxjxVO[] o = null;
    try {
      o = (GzzxjxVO[])_getBeanObject().queryAll_Gzzxjx(arg0);
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
  public String[] insertGzzxjxArray(GzzxjxVO[] arg0) throws BusinessException {
    beforeCallMethod(298);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertGzzxjxArray(arg0);
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
  public GzzxjxVO findGzzxjxByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(299);
    Exception er = null;
    GzzxjxVO o = null;
    try {
      o = _getBeanObject().findGzzxjxByPrimaryKey(arg0);
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
  public GzzxjxVO[] queryByWhere_Gzzxjx(String arg0) throws BusinessException {
    beforeCallMethod(300);
    Exception er = null;
    GzzxjxVO[] o = null;
    try {
      o = (GzzxjxVO[])_getBeanObject().queryByWhere_Gzzxjx(arg0);
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
  public int findByBs(String arg0) throws BusinessException {
    beforeCallMethod(301);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().findByBs(arg0);
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
  public boolean lock_Gzzxjx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(302);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lock_Gzzxjx(arg0, arg1);
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
  public boolean delete_Gzzxjx(GzzxjxVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(303);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Gzzxjx(arg0, arg1);
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
  public String insert_Gzzxjx(GzzxjxVO arg0) throws BusinessException {
    beforeCallMethod(304);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Gzzxjx(arg0);
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
  public void update_Gzzxjx(GzzxjxVO arg0) throws BusinessException {
    beforeCallMethod(305);
    Exception er = null;
    try {
      _getBeanObject().update_Gzzxjx(arg0);
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

  public void free_Gzzxjx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(306);
    Exception er = null;
    try {
      _getBeanObject().free_Gzzxjx(arg0, arg1);
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
  }

  public GzzxztVO[] queryByWhereCondition_Gzzxzt(ConditionVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(307);
    Exception er = null;
    GzzxztVO[] o = null;
    try {
      o = (GzzxztVO[])_getBeanObject().queryByWhereCondition_Gzzxzt(arg0, arg1);
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
  public String[] save_Gzzxzt(GzzxztVO[] arg0) throws BusinessException {
    beforeCallMethod(308);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().save_Gzzxzt(arg0);
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
  public boolean delete_Gzzxzt(GzzxztVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(309);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Gzzxzt(arg0, arg1);
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
  public String insert_Gzzxzt(GzzxztVO arg0) throws BusinessException {
    beforeCallMethod(310);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Gzzxzt(arg0);
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
  public void update_Gzzxzt(GzzxztVO arg0) throws BusinessException {
    beforeCallMethod(311);
    Exception er = null;
    try {
      _getBeanObject().update_Gzzxzt(arg0);
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

  public void accept(HandandtakeUnitVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(312);
    Exception er = null;
    try {
      _getBeanObject().accept(arg0, arg1);
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

  public void updateArray_Handandtake(HandandtakeUnitVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(313);
    Exception er = null;
    try {
      _getBeanObject().updateArray_Handandtake(arg0, arg1);
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

  public void deletArray(HandandtakeUnitVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(314);
    Exception er = null;
    try {
      _getBeanObject().deletArray(arg0, arg1);
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

  public HandandtakeUnitVO[] fingJjdBySql(String arg0) throws BusinessException {
    beforeCallMethod(315);
    Exception er = null;
    HandandtakeUnitVO[] o = null;
    try {
      o = (HandandtakeUnitVO[])_getBeanObject().fingJjdBySql(arg0);
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
  public void modifyJJdZt(HandandtakeUnitVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(316);
    Exception er = null;
    try {
      _getBeanObject().modifyJJdZt(arg0, arg1, arg2);
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

  public void unAccept(HandandtakeUnitVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(317);
    Exception er = null;
    try {
      _getBeanObject().unAccept(arg0, arg1);
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
  }

  public ArrayList insert_Handandtake(HandandtakeUnitVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(318);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Handandtake(arg0, arg1);
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
  public void update_Handandtake(HandandtakeUnitVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(319);
    Exception er = null;
    try {
      _getBeanObject().update_Handandtake(arg0, arg1);
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

  public int deleteRU(HbclVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(320);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().deleteRU(arg0, arg1);
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
  public int deleteRUb(HbclItemVO[] arg0) throws BusinessException {
    beforeCallMethod(321);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().deleteRUb(arg0);
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
  public DdVO[] getCbbItems(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(322);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getCbbItems(arg0, arg1);
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
  public String insert_Hbcl(HbclVO arg0) throws BusinessException {
    beforeCallMethod(323);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Hbcl(arg0);
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
  public HbclVO[] query_Hbcl(String arg0) throws BusinessException {
    beforeCallMethod(324);
    Exception er = null;
    HbclVO[] o = null;
    try {
      o = (HbclVO[])_getBeanObject().query_Hbcl(arg0);
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
  public HbclVO[] query_Hbcl(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(325);
    Exception er = null;
    HbclVO[] o = null;
    try {
      o = (HbclVO[])_getBeanObject().query_Hbcl(arg0, arg1);
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
  public void update_Hbcl(HbclVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(326);
    Exception er = null;
    try {
      _getBeanObject().update_Hbcl(arg0, arg1);
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

  public HhzxHeaderVO[] queryHhzxHeaders(HhzxHeaderVO arg0) throws BusinessException {
    beforeCallMethod(327);
    Exception er = null;
    HhzxHeaderVO[] o = null;
    try {
      o = (HhzxHeaderVO[])_getBeanObject().queryHhzxHeaders(arg0);
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
  public HhzxItemVO[] queryHhzxItems(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(328);
    Exception er = null;
    HhzxItemVO[] o = null;
    try {
      o = (HhzxItemVO[])_getBeanObject().queryHhzxItems(arg0, arg1, arg2);
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
  public void delete_Hhzx(HhzxVO arg0) throws BusinessException {
    beforeCallMethod(329);
    Exception er = null;
    try {
      _getBeanObject().delete_Hhzx(arg0);
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

  public String insert_Hhzx(HhzxVO arg0) throws BusinessException {
    beforeCallMethod(330);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Hhzx(arg0);
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
  public void update_Hhzx(HhzxVO arg0) throws BusinessException {
    beforeCallMethod(331);
    Exception er = null;
    try {
      _getBeanObject().update_Hhzx(arg0);
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
  }

  public SysInitVO[] getJhbomInitData(String arg0) throws BusinessException {
    beforeCallMethod(332);
    Exception er = null;
    SysInitVO[] o = null;
    try {
      o = (SysInitVO[])_getBeanObject().getJhbomInitData(arg0);
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
  public JhbomItemVO modify_Jhbom(String arg0, String arg1, JhbomVO arg2) throws BusinessException {
    beforeCallMethod(333);
    Exception er = null;
    JhbomItemVO o = null;
    try {
      o = _getBeanObject().modify_Jhbom(arg0, arg1, arg2);
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
  public Vector circleMonitor(String arg0) throws BusinessException {
    beforeCallMethod(334);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().circleMonitor(arg0);
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
  public JhbomVO findByWl(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(335);
    Exception er = null;
    JhbomVO o = null;
    try {
      o = _getBeanObject().findByWl(arg0, arg1);
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
  public Integer delete_Jhbom(String arg0, String arg1, JhbomVO arg2) throws BusinessException {
    beforeCallMethod(336);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().delete_Jhbom(arg0, arg1, arg2);
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
  public JhbomVO insert_Jhbom(String arg0, JhbomVO arg1) throws BusinessException {
    beforeCallMethod(337);
    Exception er = null;
    JhbomVO o = null;
    try {
      o = _getBeanObject().insert_Jhbom(arg0, arg1);
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
  public JkzxVO[] queryAll_Jkzx(String arg0) throws BusinessException {
    beforeCallMethod(338);
    Exception er = null;
    JkzxVO[] o = null;
    try {
      o = (JkzxVO[])_getBeanObject().queryAll_Jkzx(arg0);
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
  public JkzxVO[] queryJkzxByVO(JkzxVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(339);
    Exception er = null;
    JkzxVO[] o = null;
    try {
      o = (JkzxVO[])_getBeanObject().queryJkzxByVO(arg0, arg1);
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
  public JkzxVO[] queryByWhereCondition(ConditionVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(340);
    Exception er = null;
    JkzxVO[] o = null;
    try {
      o = (JkzxVO[])_getBeanObject().queryByWhereCondition(arg0, arg1, arg2);
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
  public String[] save_Jkzx(JkzxVO[] arg0) throws BusinessException {
    beforeCallMethod(341);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().save_Jkzx(arg0);
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
  public void delete_Jkzx(JkzxVO arg0) throws BusinessException {
    beforeCallMethod(342);
    Exception er = null;
    try {
      _getBeanObject().delete_Jkzx(arg0);
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

  public boolean delete_Jkzx(JkzxVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(343);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Jkzx(arg0, arg1);
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
  public String insert_Jkzx(JkzxVO arg0) throws BusinessException {
    beforeCallMethod(344);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Jkzx(arg0);
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
  public void update_Jkzx(JkzxVO arg0) throws BusinessException {
    beforeCallMethod(345);
    Exception er = null;
    try {
      _getBeanObject().update_Jkzx(arg0);
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

  public JlbsjVO[] queryAll_Jlbsj(String arg0) throws BusinessException {
    beforeCallMethod(346);
    Exception er = null;
    JlbsjVO[] o = null;
    try {
      o = (JlbsjVO[])_getBeanObject().queryAll_Jlbsj(arg0);
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
  public JlbsjVO[] queryJlbsjByVO(JlbsjVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(347);
    Exception er = null;
    JlbsjVO[] o = null;
    try {
      o = (JlbsjVO[])_getBeanObject().queryJlbsjByVO(arg0, arg1);
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
  public String[] insertJlbsjArray(JlbsjVO[] arg0) throws BusinessException {
    beforeCallMethod(348);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertJlbsjArray(arg0);
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
  public JlbsjVO findByPrimaryKey_Jlbsj(String arg0) throws BusinessException {
    beforeCallMethod(349);
    Exception er = null;
    JlbsjVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Jlbsj(arg0);
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
  public JlbsjVO[] qryZzcbJlbsjVOByZh_Jlbsj(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(350);
    Exception er = null;
    JlbsjVO[] o = null;
    try {
      o = (JlbsjVO[])_getBeanObject().qryZzcbJlbsjVOByZh_Jlbsj(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  public void aduitJlbsjVOs(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(351);
    Exception er = null;
    try {
      _getBeanObject().aduitJlbsjVOs(arg0, arg1, arg2);
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

  public JlbsjVO getJlbsjSccbz(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(352);
    Exception er = null;
    JlbsjVO o = null;
    try {
      o = _getBeanObject().getJlbsjSccbz(arg0, arg1, arg2, arg3, arg4);
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
  public KzcsVO getKzcsVO(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(353);
    Exception er = null;
    KzcsVO o = null;
    try {
      o = _getBeanObject().getKzcsVO(arg0, arg1, arg2);
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
  public JlbsjVO[] qryJlbsjByCondition(ConditionVO[] arg0, String arg1, int arg2) throws BusinessException {
    beforeCallMethod(354);
    Exception er = null;
    JlbsjVO[] o = null;
    try {
      o = (JlbsjVO[])_getBeanObject().qryJlbsjByCondition(arg0, arg1, arg2);
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
  public String[] saveJlbsjVOs(JlbsjVO[] arg0) throws BusinessException {
    beforeCallMethod(355);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveJlbsjVOs(arg0);
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
  public void unAduitJlbsjVOs(String[] arg0) throws BusinessException {
    beforeCallMethod(356);
    Exception er = null;
    try {
      _getBeanObject().unAduitJlbsjVOs(arg0);
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
  }

  public void delete_Jlbsj(JlbsjVO arg0) throws BusinessException {
    beforeCallMethod(357);
    Exception er = null;
    try {
      _getBeanObject().delete_Jlbsj(arg0);
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
  }

  public String insert_Jlbsj(JlbsjVO arg0) throws BusinessException {
    beforeCallMethod(358);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Jlbsj(arg0);
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
  public void update_Jlbsj(JlbsjVO arg0) throws BusinessException {
    beforeCallMethod(359);
    Exception er = null;
    try {
      _getBeanObject().update_Jlbsj(arg0);
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
  }

  public JlcsjVO[] queryAll_Jlcsj(String arg0) throws BusinessException {
    beforeCallMethod(360);
    Exception er = null;
    JlcsjVO[] o = null;
    try {
      o = (JlcsjVO[])_getBeanObject().queryAll_Jlcsj(arg0);
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
  public JlcsjVO[] queryJlcsjByVO(JlcsjVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(361);
    Exception er = null;
    JlcsjVO[] o = null;
    try {
      o = (JlcsjVO[])_getBeanObject().queryJlcsjByVO(arg0, arg1);
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
  public String[] insertArray_Jlcsj(JlcsjVO[] arg0) throws BusinessException {
    beforeCallMethod(362);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray_Jlcsj(arg0);
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
  public JlcsjVO findByPrimaryKey_Jlcsj(String arg0) throws BusinessException {
    beforeCallMethod(363);
    Exception er = null;
    JlcsjVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Jlcsj(arg0);
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
  public void aduitJlcsjVOs(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(364);
    Exception er = null;
    try {
      _getBeanObject().aduitJlcsjVOs(arg0, arg1, arg2);
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
  }

  public JlcsjVO[] qryJlcsjByCondition(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(365);
    Exception er = null;
    JlcsjVO[] o = null;
    try {
      o = (JlcsjVO[])_getBeanObject().qryJlcsjByCondition(arg0);
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
  public JlcsjVO[] qryZzcbJlbsjVOByZh(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(366);
    Exception er = null;
    JlcsjVO[] o = null;
    try {
      o = (JlcsjVO[])_getBeanObject().qryZzcbJlbsjVOByZh(arg0, arg1, arg2, arg3);
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
  public String[] saveJlcsjVOs(JlcsjVO[] arg0) throws BusinessException {
    beforeCallMethod(367);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveJlcsjVOs(arg0);
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
  public void unAduitJlcsjVOs(String[] arg0) throws BusinessException {
    beforeCallMethod(368);
    Exception er = null;
    try {
      _getBeanObject().unAduitJlcsjVOs(arg0);
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
  }

  public void delete_Jlcsj(JlcsjVO arg0) throws BusinessException {
    beforeCallMethod(369);
    Exception er = null;
    try {
      _getBeanObject().delete_Jlcsj(arg0);
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

  public String insert_Jlcsj(JlcsjVO arg0) throws BusinessException {
    beforeCallMethod(370);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Jlcsj(arg0);
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
  public void update_Jlcsj(JlcsjVO arg0) throws BusinessException {
    beforeCallMethod(371);
    Exception er = null;
    try {
      _getBeanObject().update_Jlcsj(arg0);
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
  }

  public int adjustOrders(JlctcVO arg0) throws BusinessException {
    beforeCallMethod(372);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().adjustOrders(arg0);
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
  public void updateJlctcZt(JlctcHeaderVO arg0) throws BusinessException {
    beforeCallMethod(373);
    Exception er = null;
    try {
      _getBeanObject().updateJlctcZt(arg0);
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
  }

  public JlctcItemVO[] calculateBalance(JlctcHeaderVO arg0) throws BusinessException {
    beforeCallMethod(374);
    Exception er = null;
    JlctcItemVO[] o = null;
    try {
      o = (JlctcItemVO[])_getBeanObject().calculateBalance(arg0);
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
  public JlctcItemVO[] gatherOrders(JlctcHeaderVO arg0) throws BusinessException {
    beforeCallMethod(375);
    Exception er = null;
    JlctcItemVO[] o = null;
    try {
      o = (JlctcItemVO[])_getBeanObject().gatherOrders(arg0);
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
  public JlctcHeaderVO[] queryJlctcHeaders(JlctcHeaderVO arg0) throws BusinessException {
    beforeCallMethod(376);
    Exception er = null;
    JlctcHeaderVO[] o = null;
    try {
      o = (JlctcHeaderVO[])_getBeanObject().queryJlctcHeaders(arg0);
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
  public JlctcItemVO[] queryJlctcItems(String arg0) throws BusinessException {
    beforeCallMethod(377);
    Exception er = null;
    JlctcItemVO[] o = null;
    try {
      o = (JlctcItemVO[])_getBeanObject().queryJlctcItems(arg0);
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
  public JlctcHeaderVO queryLastJlctcHeader(JlctcHeaderVO arg0) throws BusinessException {
    beforeCallMethod(378);
    Exception er = null;
    JlctcHeaderVO o = null;
    try {
      o = _getBeanObject().queryLastJlctcHeader(arg0);
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
  public void delete_Jlctc(JlctcVO arg0) throws BusinessException {
    beforeCallMethod(379);
    Exception er = null;
    try {
      _getBeanObject().delete_Jlctc(arg0);
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
  }

  public String insert_Jlctc(JlctcVO arg0) throws BusinessException {
    beforeCallMethod(380);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Jlctc(arg0);
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
  public void update_Jlctc(JlctcVO arg0) throws BusinessException {
    beforeCallMethod(381);
    Exception er = null;
    try {
      _getBeanObject().update_Jlctc(arg0);
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

  public JldVO findByPrimaryKey_Jld(String arg0) throws BusinessException {
    beforeCallMethod(382);
    Exception er = null;
    JldVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Jld(arg0);
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
  public String insertJld(JldHeaderVO arg0) throws BusinessException {
    beforeCallMethod(383);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertJld(arg0);
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
  public JldHeaderVO[] queryJldsByJzwl(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(384);
    Exception er = null;
    JldHeaderVO[] o = null;
    try {
      o = (JldHeaderVO[])_getBeanObject().queryJldsByJzwl(arg0, arg1);
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
  public void updateJld(JldHeaderVO arg0) throws BusinessException {
    beforeCallMethod(385);
    Exception er = null;
    try {
      _getBeanObject().updateJld(arg0);
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
  }

  public String[] updateJldHb(JldVO arg0) throws BusinessException {
    beforeCallMethod(386);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().updateJldHb(arg0);
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
    return o;
  }
  public void delete_Jld(JldVO arg0) throws BusinessException {
    beforeCallMethod(387);
    Exception er = null;
    try {
      _getBeanObject().delete_Jld(arg0);
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

  public boolean deleteByPks(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(388);
    Exception er = null;
    boolean o = false;
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
  public JldsjVO findByPrimaryKey_Jldsj(String arg0) throws BusinessException {
    beforeCallMethod(389);
    Exception er = null;
    JldsjVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Jldsj(arg0);
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
  public void aduitJldsjVOs(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(390);
    Exception er = null;
    try {
      _getBeanObject().aduitJldsjVOs(arg0, arg1, arg2);
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
  }

  public void lockRows(String[] arg0) throws BusinessException {
    beforeCallMethod(391);
    Exception er = null;
    try {
      _getBeanObject().lockRows(arg0);
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
  }

  public JldsjItemVO[] qryJldsjByLrgjd(String arg0, String arg1, String[] arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(392);
    Exception er = null;
    JldsjItemVO[] o = null;
    try {
      o = (JldsjItemVO[])_getBeanObject().qryJldsjByLrgjd(arg0, arg1, arg2, arg3, arg4);
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
  public JldsjItemVO[] qryJldsjItemsByHeader(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(393);
    Exception er = null;
    JldsjItemVO[] o = null;
    try {
      o = (JldsjItemVO[])_getBeanObject().qryJldsjItemsByHeader(arg0, arg1);
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
    return o;
  }
  public JldsjItemVO[] qryJldsjItemsByPks(String[] arg0) throws BusinessException {
    beforeCallMethod(394);
    Exception er = null;
    JldsjItemVO[] o = null;
    try {
      o = (JldsjItemVO[])_getBeanObject().qryJldsjItemsByPks(arg0);
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
  public JldsjHeaderVO[] qryJldsjVOsByCondition(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(395);
    Exception er = null;
    JldsjHeaderVO[] o = null;
    try {
      o = (JldsjHeaderVO[])_getBeanObject().qryJldsjVOsByCondition(arg0);
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
  public JldsjHeaderVO[] qryJldsjVOsByMerage(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws BusinessException {
    beforeCallMethod(396);
    Exception er = null;
    JldsjHeaderVO[] o = null;
    try {
      o = (JldsjHeaderVO[])_getBeanObject().qryJldsjVOsByMerage(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
  public void unAduitJldsjVOs(String[] arg0) throws BusinessException {
    beforeCallMethod(397);
    Exception er = null;
    try {
      _getBeanObject().unAduitJldsjVOs(arg0);
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
  }

  public void unLockRows(String[] arg0) throws BusinessException {
    beforeCallMethod(398);
    Exception er = null;
    try {
      _getBeanObject().unLockRows(arg0);
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
  }

  public void updateSfzcbByJld(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(399);
    Exception er = null;
    try {
      _getBeanObject().updateSfzcbByJld(arg0, arg1);
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
  }

  public void updateSfzcb(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(400);
    Exception er = null;
    try {
      _getBeanObject().updateSfzcb(arg0, arg1);
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
  }

  public void delete_Jldsj(JldsjVO arg0) throws BusinessException {
    beforeCallMethod(401);
    Exception er = null;
    try {
      _getBeanObject().delete_Jldsj(arg0);
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
  }

  public boolean delete_Jldsj(JldsjVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(402);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Jldsj(arg0, arg1);
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
    return o;
  }
  public JldsjVO insert_Jldsj(JldsjVO arg0) throws BusinessException {
    beforeCallMethod(403);
    Exception er = null;
    JldsjVO o = null;
    try {
      o = _getBeanObject().insert_Jldsj(arg0);
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
  public JldsjVO update_Jldsj(JldsjVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(404);
    Exception er = null;
    JldsjVO o = null;
    try {
      o = _getBeanObject().update_Jldsj(arg0, arg1);
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
  public String queryName(String arg0) throws BusinessException {
    beforeCallMethod(405);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().queryName(arg0);
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
    return o;
  }
  public JldsjBVO[] queryAll_JldsjB(String arg0) throws BusinessException {
    beforeCallMethod(406);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().queryAll_JldsjB(arg0);
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
  public JldsjBVO[] queryJld(String arg0) throws BusinessException {
    beforeCallMethod(407);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().queryJld(arg0);
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
    return o;
  }
  public JldsjBVO[] queryJldsj(String arg0) throws BusinessException {
    beforeCallMethod(408);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().queryJldsj(arg0);
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
    return o;
  }
  public JldsjBVO[] queryJldsj_b(String arg0) throws BusinessException {
    beforeCallMethod(409);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().queryJldsj_b(arg0);
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
  public JldsjBVO[] queryJsbs(String arg0) throws BusinessException {
    beforeCallMethod(410);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().queryJsbs(arg0);
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
  public JldsjBVO[] querypd_dd(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(411);
    Exception er = null;
    JldsjBVO[] o = null;
    try {
      o = (JldsjBVO[])_getBeanObject().querypd_dd(arg0, arg1);
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
  public void countMixCenter(String arg0, String arg1, String arg2, UFDate arg3, UFDate arg4) throws BusinessException {
    beforeCallMethod(412);
    Exception er = null;
    try {
      _getBeanObject().countMixCenter(arg0, arg1, arg2, arg3, arg4);
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

  public void countJldwlsj(CountParaVO arg0) throws BusinessException {
    beforeCallMethod(413);
    Exception er = null;
    try {
      _getBeanObject().countJldwlsj(arg0);
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
  }

  public UFBoolean isUsed(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(414);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUsed(arg0, arg1, arg2);
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
    return o;
  }
  public JslxszVO[] queryByVO(JslxszVO arg0) throws BusinessException {
    beforeCallMethod(415);
    Exception er = null;
    JslxszVO[] o = null;
    try {
      o = (JslxszVO[])_getBeanObject().queryByVO(arg0);
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
  public JslxszVO[] queryAll(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(416);
    Exception er = null;
    JslxszVO[] o = null;
    try {
      o = (JslxszVO[])_getBeanObject().queryAll(arg0, arg1);
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
  public String[] saveArray(JslxszVO[] arg0) throws BusinessException {
    beforeCallMethod(417);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveArray(arg0);
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
  public String[] insertJxdArray(JxdHeaderVO[] arg0) throws BusinessException {
    beforeCallMethod(418);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertJxdArray(arg0);
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
  public JxdHeaderVO findJxdByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(419);
    Exception er = null;
    JxdHeaderVO o = null;
    try {
      o = _getBeanObject().findJxdByPrimaryKey(arg0);
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
  public JxdHeaderVO[] findJxdByQueryCondition(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(420);
    Exception er = null;
    JxdHeaderVO[] o = null;
    try {
      o = (JxdHeaderVO[])_getBeanObject().findJxdByQueryCondition(arg0);
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
  public void cancelGenScdd(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(421);
    Exception er = null;
    try {
      _getBeanObject().cancelGenScdd(arg0, arg1);
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
  }

  public JxdItemVO[] findJxmlByJxd(String arg0) throws BusinessException {
    beforeCallMethod(422);
    Exception er = null;
    JxdItemVO[] o = null;
    try {
      o = (JxdItemVO[])_getBeanObject().findJxmlByJxd(arg0);
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
  public void changeJxdZt(Integer arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(423);
    Exception er = null;
    try {
      _getBeanObject().changeJxdZt(arg0, arg1, arg2);
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
  }

  public UFBoolean checkJxdDdsczt(String arg0) throws BusinessException {
    beforeCallMethod(424);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().checkJxdDdsczt(arg0);
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
  public void finishJxd(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(425);
    Exception er = null;
    try {
      _getBeanObject().finishJxd(arg0, arg1);
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
  }

  public void genScdd(JxdVO arg0, UserVO arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(426);
    Exception er = null;
    try {
      _getBeanObject().genScdd(arg0, arg1, arg2);
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
  }

  public void jxdJh(String[] arg0, UFDate[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(427);
    Exception er = null;
    try {
      _getBeanObject().jxdJh(arg0, arg1, arg2);
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
  }

  public void delete_Jxd(JxdHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(428);
    Exception er = null;
    try {
      _getBeanObject().delete_Jxd(arg0, arg1);
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
  }

  public String insert_Jxd(JxdHeaderVO arg0) throws BusinessException {
    beforeCallMethod(429);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Jxd(arg0);
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
  public void update_Jxd(JxdHeaderVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(430);
    Exception er = null;
    try {
      _getBeanObject().update_Jxd(arg0, arg1);
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
  }

  public ArrayList getJxdTotalResult(ConditionVO[] arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(431);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getJxdTotalResult(arg0, arg1);
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
    return o;
  }
  public void modify(DynamicKhwlszVO[] arg0) throws BusinessException {
    beforeCallMethod(432);
    Exception er = null;
    try {
      _getBeanObject().modify(arg0);
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

  public KhwlszVO[] queryAll_Khwlsz(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(433);
    Exception er = null;
    KhwlszVO[] o = null;
    try {
      o = (KhwlszVO[])_getBeanObject().queryAll_Khwlsz(arg0, arg1, arg2);
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
  public KhwlszVO[] queryAll_Khwlsz(String arg0) throws BusinessException {
    beforeCallMethod(434);
    Exception er = null;
    KhwlszVO[] o = null;
    try {
      o = (KhwlszVO[])_getBeanObject().queryAll_Khwlsz(arg0);
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
  public DynamicKhwlszVO[] queryKhwlszByVO(KhwlszVO arg0, Boolean arg1, DynamicKhwlszVO arg2) throws BusinessException {
    beforeCallMethod(435);
    Exception er = null;
    DynamicKhwlszVO[] o = null;
    try {
      o = (DynamicKhwlszVO[])_getBeanObject().queryKhwlszByVO(arg0, arg1, arg2);
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
  public DynamicKhwlszVO getPrototype(String arg0) throws BusinessException {
    beforeCallMethod(436);
    Exception er = null;
    DynamicKhwlszVO o = null;
    try {
      o = _getBeanObject().getPrototype(arg0);
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
  public Vector getDisplayInfo(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(437);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getDisplayInfo(arg0, arg1);
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
    return o;
  }
  public Vector getKzcs(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(438);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getKzcs(arg0, arg1);
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
    return o;
  }
  public void deletePoArray(String[] arg0) throws BusinessException {
    beforeCallMethod(439);
    Exception er = null;
    try {
      _getBeanObject().deletePoArray(arg0);
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

  public ArrayList getKcInfo(String arg0, String arg1, String arg2, String arg3, int arg4, FreeItemVO arg5) throws BusinessException {
    beforeCallMethod(440);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getKcInfo(arg0, arg1, arg2, arg3, arg4, arg5);
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
    return o;
  }
  public Vector getQueryData(ConditionVO[] arg0, boolean arg1) throws BusinessException {
    beforeCallMethod(441);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getQueryData(arg0, arg1);
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
    return o;
  }
  public String[] insertPoArray(nc.vo.mm.pub.pub1025.PoVO[] arg0) throws BusinessException {
    beforeCallMethod(442);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertPoArray(arg0);
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
    return o;
  }
  public LbVO[] queryAll_Lb(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(443);
    Exception er = null;
    LbVO[] o = null;
    try {
      o = (LbVO[])_getBeanObject().queryAll_Lb(arg0, arg1, arg2, arg3, arg4);
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
    return o;
  }
  public String[] insertArray_Lb(LbVO[] arg0) throws BusinessException {
    beforeCallMethod(444);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertArray_Lb(arg0);
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
  public String buildLBByCondition(String arg0, LbzdVO arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(445);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().buildLBByCondition(arg0, arg1, arg2, arg3);
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
  public LbzdVO findLbzdByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(446);
    Exception er = null;
    LbzdVO o = null;
    try {
      o = _getBeanObject().findLbzdByPrimaryKey(arg0);
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
  public String findLbzdPKByGzzxid(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(447);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().findLbzdPKByGzzxid(arg0, arg1, arg2);
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
  public Integer getLbzqByGzzxid(String arg0) throws BusinessException {
    beforeCallMethod(448);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().getLbzqByGzzxid(arg0);
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
  public boolean isExistResSet(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(449);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isExistResSet(arg0, arg1, arg2);
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
  public ArrayList queryLB(String arg0, String arg1, String arg2, UFDate arg3, UFDate arg4) throws BusinessException {
    beforeCallMethod(450);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().queryLB(arg0, arg1, arg2, arg3, arg4);
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
    return o;
  }
  public void updateLB(LbVO[] arg0) throws BusinessException {
    beforeCallMethod(451);
    Exception er = null;
    try {
      _getBeanObject().updateLB(arg0);
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
  }

  public String insert_Lb(LbVO arg0) throws BusinessException {
    beforeCallMethod(452);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Lb(arg0);
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
  public LbzdVO findByPrimaryKey_Lbzd(String arg0) throws BusinessException {
    beforeCallMethod(453);
    Exception er = null;
    LbzdVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Lbzd(arg0);
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
    return o;
  }
  public String findPKByGzzxid(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(454);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().findPKByGzzxid(arg0, arg1, arg2);
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
  public String[] insert_Lbzd(LbzdVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(455);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insert_Lbzd(arg0, arg1);
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
  public String[] update_Lbzd(LbzdVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(456);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().update_Lbzd(arg0, arg1);
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
  public void cancelRun(String arg0) throws BusinessException {
    beforeCallMethod(457);
    Exception er = null;
    try {
      _getBeanObject().cancelRun(arg0);
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
  }

  public Integer releaseMDOToken(String arg0) throws BusinessException {
    beforeCallMethod(458);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().releaseMDOToken(arg0);
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
  public nc.vo.dm.dm4010.InvInfoVO[] getKxsWlInfo(String arg0) throws BusinessException {
    beforeCallMethod(459);
    Exception er = null;
    nc.vo.dm.dm4010.InvInfoVO[] o = null;
    try {
      o = (nc.vo.dm.dm4010.InvInfoVO[])_getBeanObject().getKxsWlInfo(arg0);
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
  public LwVO[] getLw(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(460);
    Exception er = null;
    LwVO[] o = null;
    try {
      o = (LwVO[])_getBeanObject().getLw(arg0, arg1);
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
  public String performMDO(MMInputVO arg0) throws BusinessException {
    beforeCallMethod(461);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().performMDO(arg0);
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
  public Integer setMDOToken(String arg0) throws BusinessException {
    beforeCallMethod(462);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().setMDOToken(arg0);
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
  public MOSourceVO[] getSources(String arg0) throws BusinessException {
    beforeCallMethod(463);
    Exception er = null;
    MOSourceVO[] o = null;
    try {
      o = (MOSourceVO[])_getBeanObject().getSources(arg0);
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
  public String[] unfinish_MO(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(464);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().unfinish_MO(arg0);
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
  public void freeArray_MO(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(465);
    Exception er = null;
    try {
      _getBeanObject().freeArray_MO(arg0, arg1);
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
  }

  public boolean lockArray_MO(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(466);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lockArray_MO(arg0, arg1);
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
  public ProductVO getMoProduct(String arg0, UFDouble arg1, FreeItemVO arg2) throws BusinessException {
    beforeCallMethod(467);
    Exception er = null;
    ProductVO o = null;
    try {
      o = _getBeanObject().getMoProduct(arg0, arg1, arg2);
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
  public void createMOByMOArray(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(468);
    Exception er = null;
    try {
      _getBeanObject().createMOByMOArray(arg0);
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

  public ArrayList createMOByPOArray(nc.vo.mm.pub.pub1030.MoVO arg0, ArrayList arg1, UFBoolean arg2) throws BusinessException {
    beforeCallMethod(469);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().createMOByPOArray(arg0, arg1, arg2);
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
  public String[] getAvaVersion(String arg0, String arg1, String arg2, String arg3, UFDouble arg4, FreeItemVO arg5) throws BusinessException {
    beforeCallMethod(470);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getAvaVersion(arg0, arg1, arg2, arg3, arg4, arg5);
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
    return o;
  }
  public MoHeaderVO[] queryMoByWhere(String arg0) throws BusinessException {
    beforeCallMethod(471);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().queryMoByWhere(arg0);
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
  public DdVO[] getDds_MO(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(472);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getDds_MO(arg0, arg1);
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
  public void checkTimeStamp_Mo(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(473);
    Exception er = null;
    try {
      _getBeanObject().checkTimeStamp_Mo(arg0);
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
  }

  public ArrayList getFlushMOForMO(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(474);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getFlushMOForMO(arg0);
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
  public MoItemVO[] getMoItems(String arg0) throws BusinessException {
    beforeCallMethod(475);
    Exception er = null;
    MoItemVO[] o = null;
    try {
      o = (MoItemVO[])_getBeanObject().getMoItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(475, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] getPickmStat(String[] arg0) throws BusinessException {
    beforeCallMethod(476);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getPickmStat(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(476, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.MoVO[] moput(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(477);
    Exception er = null;
    nc.vo.mm.pub.pub1030.MoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.MoVO[])_getBeanObject().moput(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(477, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] mounput(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(478);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().mounput(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(478, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] over(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(479);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().over(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(479, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] unover(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(480);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().unover(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(480, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void writeDczt(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(481);
    Exception er = null;
    try {
      _getBeanObject().writeDczt(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(481, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.bd.b431.InvbasdocVO[] getInvBasDocVO(String[] arg0) throws BusinessException {
    beforeCallMethod(482);
    Exception er = null;
    nc.vo.bd.b431.InvbasdocVO[] o = null;
    try {
      o = (nc.vo.bd.b431.InvbasdocVO[])_getBeanObject().getInvBasDocVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(482, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getManIDFromProID(String arg0) throws BusinessException {
    beforeCallMethod(483);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getManIDFromProID(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(483, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updatePrintedFlag(String[] arg0) throws BusinessException {
    beforeCallMethod(484);
    Exception er = null;
    try {
      _getBeanObject().updatePrintedFlag(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(484, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void backwrite(nc.vo.mm.pub.pub1030.MoVO[] arg0, UFDouble arg1, String arg2, UFDateTime arg3) throws BusinessException {
    beforeCallMethod(485);
    Exception er = null;
    try {
      _getBeanObject().backwrite(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(485, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mm.pub.pub1030.PickmVO findClosablePickm(String arg0) throws BusinessException {
    beforeCallMethod(486);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().findClosablePickm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(486, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void revisemo(String arg0, String arg1, nc.vo.mm.pub.pub1030.MoVO arg2) throws BusinessException {
    beforeCallMethod(487);
    Exception er = null;
    try {
      _getBeanObject().revisemo(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(487, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public KhwlszVO[] getKhwls(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(488);
    Exception er = null;
    KhwlszVO[] o = null;
    try {
      o = (KhwlszVO[])_getBeanObject().getKhwls(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(488, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PraybillVO motopro(nc.vo.mm.pub.pub1030.MoVO arg0, String arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(489);
    Exception er = null;
    PraybillVO o = null;
    try {
      o = _getBeanObject().motopro(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(489, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] saveMO(nc.vo.mm.pub.pub1030.MoVO[] arg0, nc.vo.mm.pub.pub1030.PickmVO[] arg1) throws BusinessException {
    beforeCallMethod(490);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveMO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(490, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] batchRevise(nc.vo.mm.pub.pub1030.MoVO[] arg0, Integer[] arg1) throws BusinessException {
    beforeCallMethod(491);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().batchRevise(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(491, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void finishMOByPgd(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(492);
    Exception er = null;
    try {
      _getBeanObject().finishMOByPgd(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(492, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void deleteBySource(String[] arg0, String[] arg1, Integer arg2) throws BusinessException {
    beforeCallMethod(493);
    Exception er = null;
    try {
      _getBeanObject().deleteBySource(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(493, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String saveMOWithMorePickms(nc.vo.mm.pub.pub1030.MoVO arg0, nc.vo.mm.pub.pub1030.PickmVO[] arg1) throws BusinessException {
    beforeCallMethod(494);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().saveMOWithMorePickms(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(494, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getMainWCenterID(String arg0) throws BusinessException {
    beforeCallMethod(495);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getMainWCenterID(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(495, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getRtVerByMainWCenter(nc.vo.mm.pub.pub1030.MoVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(496);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getRtVerByMainWCenter(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(496, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_MO(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(497);
    Exception er = null;
    try {
      _getBeanObject().delete_MO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(497, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList update_MO(nc.vo.mm.pub.pub1030.MoVO arg0) throws BusinessException {
    beforeCallMethod(498);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_MO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(498, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] finishMO(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(499);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().finishMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(499, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MORecursiveVO findRecursivelyByParent(String arg0) throws BusinessException {
    beforeCallMethod(500);
    Exception er = null;
    MORecursiveVO o = null;
    try {
      o = _getBeanObject().findRecursivelyByParent(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(500, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] getInitData_MPS() throws BusinessException {
    beforeCallMethod(501);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getInitData_MPS();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(501, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getMPSCancel(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(502);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getMPSCancel(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(502, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public LwVO[] getMPSlw(String arg0, String arg1, DdVO[] arg2, String arg3) throws BusinessException {
    beforeCallMethod(503);
    Exception er = null;
    LwVO[] o = null;
    try {
      o = (LwVO[])_getBeanObject().getMPSlw(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(503, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] getInitData_MRP() throws BusinessException {
    beforeCallMethod(504);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getInitData_MRP();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(504, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getCancel(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(505);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getCancel(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(505, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public LwVO[] getlw(String arg0, String arg1, DdVO[] arg2, String arg3) throws BusinessException {
    beforeCallMethod(506);
    Exception er = null;
    LwVO[] o = null;
    try {
      o = (LwVO[])_getBeanObject().getlw(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(506, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public LwVO[] getlw_MRP(String arg0, String arg1, DdVO[] arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(507);
    Exception er = null;
    LwVO[] o = null;
    try {
      o = (LwVO[])_getBeanObject().getlw_MRP(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(507, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SourceVO[] getMoForCostCM(String arg0, String arg1, UFDate arg2, UFDate arg3, String arg4) throws BusinessException {
    beforeCallMethod(508);
    Exception er = null;
    SourceVO[] o = null;
    try {
      o = (SourceVO[])_getBeanObject().getMoForCostCM(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(508, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public CostRtVO[] getPgdGxToCostCM(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(509);
    Exception er = null;
    CostRtVO[] o = null;
    try {
      o = (CostRtVO[])_getBeanObject().getPgdGxToCostCM(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(509, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] getZzslToCostCM(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(510);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().getZzslToCostCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(510, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO getMOByMOIDCM(String arg0) throws BusinessException {
    beforeCallMethod(511);
    Exception er = null;
    MoHeaderVO o = null;
    try {
      o = _getBeanObject().getMOByMOIDCM(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(511, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO getMOInfoCM(String arg0) throws BusinessException {
    beforeCallMethod(512);
    Exception er = null;
    MoHeaderVO o = null;
    try {
      o = _getBeanObject().getMOInfoCM(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(512, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.me.me1050.CostStaticVO[] getStatisticgxBcpCM(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(513);
    Exception er = null;
    nc.vo.me.me1050.CostStaticVO[] o = null;
    try {
      o = (nc.vo.me.me1050.CostStaticVO[])_getBeanObject().getStatisticgxBcpCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(513, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.me.me1050.CostStaticVO[] getStatisticgxWgfzfwCM(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(514);
    Exception er = null;
    nc.vo.me.me1050.CostStaticVO[] o = null;
    try {
      o = (nc.vo.me.me1050.CostStaticVO[])_getBeanObject().getStatisticgxWgfzfwCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(514, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] queryStatisticBfslCM(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(515);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().queryStatisticBfslCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(515, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] getWgslToCostCM(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(516);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().getWgslToCostCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(516, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] queryStatisticWgslCM(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(517);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().queryStatisticWgslCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(517, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] queryStatisticZcslCM(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(518);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().queryStatisticZcslCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(518, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.CostStaticVO[] queryStatisticZylCM(String arg0, String arg1, UFDate arg2, UFDate arg3) throws BusinessException {
    beforeCallMethod(519);
    Exception er = null;
    nc.vo.mm.pub.pub1030.CostStaticVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.CostStaticVO[])_getBeanObject().queryStatisticZylCM(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(519, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void afterCoCM(String arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(520);
    Exception er = null;
    try {
      _getBeanObject().afterCoCM(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(520, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void afterCo2CM(String arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(521);
    Exception er = null;
    try {
      _getBeanObject().afterCo2CM(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(521, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void rewriteIncomNumIC(String arg0, String arg1, UFDouble arg2, UFDouble arg3) throws BusinessException {
    beforeCallMethod(522);
    Exception er = null;
    try {
      _getBeanObject().rewriteIncomNumIC(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(522, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void setLockedFlagIC(ArrayList arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(523);
    Exception er = null;
    try {
      _getBeanObject().setLockedFlagIC(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(523, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public MoHeaderVO[] getMOInfoForECN(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(524);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().getMOInfoForECN(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(524, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmHeaderVO[] findPickmHeaderByCond(String arg0) throws BusinessException {
    beforeCallMethod(525);
    Exception er = null;
    PickmHeaderVO[] o = null;
    try {
      o = (PickmHeaderVO[])_getBeanObject().findPickmHeaderByCond(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(525, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void onRearOrderDelete(String[] arg0) throws BusinessException {
    beforeCallMethod(526);
    Exception er = null;
    try {
      _getBeanObject().onRearOrderDelete(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(526, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mo.mo1030.WrHeaderVO[] queryWrHeaderByWhereQC(String arg0) throws BusinessException {
    beforeCallMethod(527);
    Exception er = null;
    nc.vo.mo.mo1030.WrHeaderVO[] o = null;
    try {
      o = (nc.vo.mo.mo1030.WrHeaderVO[])_getBeanObject().queryWrHeaderByWhereQC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(527, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1030.WrItemVO findWrItemByPKQC(String arg0) throws BusinessException {
    beforeCallMethod(528);
    Exception er = null;
    nc.vo.mo.mo1030.WrItemVO o = null;
    try {
      o = _getBeanObject().findWrItemByPKQC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(528, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void writeJyztQC(String arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(529);
    Exception er = null;
    try {
      _getBeanObject().writeJyztQC(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(529, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void cancelCheckQC(String[] arg0) throws BusinessException {
    beforeCallMethod(530);
    Exception er = null;
    try {
      _getBeanObject().cancelCheckQC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(530, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void rewriteQualityCheckQC(QcresultVO[] arg0) throws BusinessException {
    beforeCallMethod(531);
    Exception er = null;
    try {
      _getBeanObject().rewriteQualityCheckQC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(531, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public UFBoolean[] isUnAuditQC(String[] arg0) throws BusinessException {
    beforeCallMethod(532);
    Exception er = null;
    UFBoolean[] o = null;
    try {
      o = (UFBoolean[])_getBeanObject().isUnAuditQC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(532, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer savePickmSC(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(533);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().savePickmSC(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(533, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList createMOSO(nc.vo.mm.pub.pub1030.MoVO arg0, ArrayList arg1, UFBoolean arg2) throws BusinessException {
    beforeCallMethod(534);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().createMOSO(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(534, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void createMOBatchSO(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(535);
    Exception er = null;
    try {
      _getBeanObject().createMOBatchSO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(535, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public GxjhInitVO getMoGxjhInitData(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(536);
    Exception er = null;
    GxjhInitVO o = null;
    try {
      o = _getBeanObject().getMoGxjhInitData(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(536, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void createPgd(PgdCreateConditionVO arg0, UFBoolean arg1, UFBoolean arg2) throws BusinessException {
    beforeCallMethod(537);
    Exception er = null;
    try {
      _getBeanObject().createPgd(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(537, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public MoHeaderVO[] findGxjhByConditionVO(String arg0, String arg1, ConditionVO[] arg2) throws BusinessException {
    beforeCallMethod(538);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().findGxjhByConditionVO(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(538, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GxjhVO[] findGxjhItemsForHead(String arg0) throws BusinessException {
    beforeCallMethod(539);
    Exception er = null;
    GxjhVO[] o = null;
    try {
      o = (GxjhVO[])_getBeanObject().findGxjhItemsForHead(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(539, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO findClInfo(MoHeaderVO arg0) throws BusinessException {
    beforeCallMethod(540);
    Exception er = null;
    MoHeaderVO o = null;
    try {
      o = _getBeanObject().findClInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(540, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GxjhVO[][] workoutGxjh(WorkoutConditionVO arg0, PgdCreateConditionVO arg1) throws BusinessException {
    beforeCallMethod(541);
    Exception er = null;
    GxjhVO[][] o = (GxjhVO[][])null;
    try {
      o = (GxjhVO[][])_getBeanObject().workoutGxjh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(541, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getPgdMaxZtByGxh(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(542);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getPgdMaxZtByGxh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(542, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] update_MoGxjh(nc.vo.sf.sf2010.MoVO arg0) throws BusinessException {
    beforeCallMethod(543);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().update_MoGxjh(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(543, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1050.InitVO getInitData_MoStat() throws BusinessException {
    beforeCallMethod(544);
    Exception er = null;
    nc.vo.mo.mo1050.InitVO o = null;
    try {
      o = _getBeanObject().getInitData_MoStat();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(544, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoStatVO[] queryMoStatByConditionVO(StatConditionVO arg0, int arg1) throws BusinessException {
    beforeCallMethod(545);
    Exception er = null;
    MoStatVO[] o = null;
    try {
      o = (MoStatVO[])_getBeanObject().queryMoStatByConditionVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(545, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4020.InvInfoVO getProduceInfo(nc.vo.mp.mp4030.PoVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(546);
    Exception er = null;
    nc.vo.mp.mp4020.InvInfoVO o = null;
    try {
      o = _getBeanObject().getProduceInfo(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(546, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MpsBillVO[] query_MpsBill(String arg0, String arg1, String arg2, nc.vo.mp.mp4020.InvInfoVO arg3, String arg4, String arg5, boolean arg6) throws BusinessException {
    beforeCallMethod(547);
    Exception er = null;
    MpsBillVO[] o = null;
    try {
      o = (MpsBillVO[])_getBeanObject().query_MpsBill(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(547, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer setToken(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(548);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().setToken(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(548, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean isUseRoute(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(549);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUseRoute(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(549, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getSjdw(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(550);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getSjdw(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(550, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer releaseToken(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(551);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().releaseToken(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(551, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String addYhtz(nc.vo.mp.mp2040.YhtzVO arg0) throws BusinessException {
    beforeCallMethod(552);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().addYhtz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(552, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] addYhtzs(nc.vo.mp.mp2040.YhtzVO[] arg0) throws BusinessException {
    beforeCallMethod(553);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().addYhtzs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(553, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delYhtz(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(554);
    Exception er = null;
    try {
      _getBeanObject().delYhtz(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(554, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mp.mp2040.YhtzVO[] findYhtz(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(555);
    Exception er = null;
    nc.vo.mp.mp2040.YhtzVO[] o = null;
    try {
      o = (nc.vo.mp.mp2040.YhtzVO[])_getBeanObject().findYhtz(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(555, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] getLyddhs(String[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(556);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getLyddhs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(556, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp2040.PoVO[] getlyvos(String[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(557);
    Exception er = null;
    nc.vo.mp.mp2040.PoVO[] o = null;
    try {
      o = (nc.vo.mp.mp2040.PoVO[])_getBeanObject().getlyvos(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(557, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public NljhVO[] getNljhsByBoth(String arg0, String arg1, String arg2, Integer arg3, UFDouble arg4, UFBoolean arg5) throws BusinessException {
    beforeCallMethod(558);
    Exception er = null;
    NljhVO[] o = null;
    try {
      o = (NljhVO[])_getBeanObject().getNljhsByBoth(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(558, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public NljhVO[] getNljhsByGzzx(String arg0, String arg1, String arg2, String arg3, Integer arg4) throws BusinessException {
    beforeCallMethod(559);
    Exception er = null;
    NljhVO[] o = null;
    try {
      o = (NljhVO[])_getBeanObject().getNljhsByGzzx(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(559, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public NljhVO[] getNljhsBySjzq(String arg0, String arg1, String arg2, UFDate arg3, Integer arg4, UFBoolean arg5) throws BusinessException {
    beforeCallMethod(560);
    Exception er = null;
    NljhVO[] o = null;
    try {
      o = (NljhVO[])_getBeanObject().getNljhsBySjzq(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(560, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void reCreateNljh(String arg0, String arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(561);
    Exception er = null;
    try {
      _getBeanObject().reCreateNljh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(561, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void updateYhtz(nc.vo.mp.mp2040.YhtzVO arg0) throws BusinessException {
    beforeCallMethod(562);
    Exception er = null;
    try {
      _getBeanObject().updateYhtz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(562, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void updateYhtzForAllUser(nc.vo.mp.mp2040.YhtzVO arg0, boolean arg1, boolean arg2) throws BusinessException {
    beforeCallMethod(563);
    Exception er = null;
    try {
      _getBeanObject().updateYhtzForAllUser(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(563, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void updateYhtzs(nc.vo.mp.mp2040.YhtzVO[] arg0) throws BusinessException {
    beforeCallMethod(564);
    Exception er = null;
    try {
      _getBeanObject().updateYhtzs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(564, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PgaHeaderVO[] queryPgaByWhere(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(565);
    Exception er = null;
    PgaHeaderVO[] o = null;
    try {
      o = (PgaHeaderVO[])_getBeanObject().queryPgaByWhere(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(565, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PgaItemVO[] getPgaItems(String arg0) throws BusinessException {
    beforeCallMethod(566);
    Exception er = null;
    PgaItemVO[] o = null;
    try {
      o = (PgaItemVO[])_getBeanObject().getPgaItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(566, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public int checkBzbm(String arg0) throws BusinessException {
    beforeCallMethod(567);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().checkBzbm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(567, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PgaItemVO[] getItemsByWk(String arg0) throws BusinessException {
    beforeCallMethod(568);
    Exception er = null;
    PgaItemVO[] o = null;
    try {
      o = (PgaItemVO[])_getBeanObject().getItemsByWk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(568, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PgaVO[] loadPga(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(569);
    Exception er = null;
    PgaVO[] o = null;
    try {
      o = (PgaVO[])_getBeanObject().loadPga(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(569, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void removePsn(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(570);
    Exception er = null;
    try {
      _getBeanObject().removePsn(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(570, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String[] checkRyId(PgaItemVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(571);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().checkRyId(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(571, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean deleteHeaders(PgaHeaderVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(572);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().deleteHeaders(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(572, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insert_Pga(PgaVO arg0) throws BusinessException {
    beforeCallMethod(573);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insert_Pga(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(573, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Pga(PgaVO arg0) throws BusinessException {
    beforeCallMethod(574);
    Exception er = null;
    try {
      _getBeanObject().update_Pga(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(574, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void delete(UnitToSingleVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(575);
    Exception er = null;
    try {
      _getBeanObject().delete(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(575, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList insert(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(576);
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
      afterCallMethod(576, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.sf.sf2030.QueryDataVO query(String arg0, String arg1, ConditionVO[] arg2, UFBoolean arg3, String arg4, Integer[] arg5) throws BusinessException {
    beforeCallMethod(577);
    Exception er = null;
    nc.vo.sf.sf2030.QueryDataVO o = null;
    try {
      o = _getBeanObject().query(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(577, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateArray(UnitToSingleVO[] arg0) throws BusinessException {
    beforeCallMethod(578);
    Exception er = null;
    try {
      _getBeanObject().updateArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(578, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList getGxh(String arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(579);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getGxh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(579, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getBillCode(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(580);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getBillCode(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(580, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteArray(UnitToSingleVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(581);
    Exception er = null;
    try {
      _getBeanObject().deleteArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(581, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.sf.sf2030.InitVO getInitData_Pgd(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(582);
    Exception er = null;
    nc.vo.sf.sf2030.InitVO o = null;
    try {
      o = _getBeanObject().getInitData_Pgd(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(582, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void returnBillCodeOnDelete(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(583);
    Exception er = null;
    try {
      _getBeanObject().returnBillCodeOnDelete(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(583, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void generateWwdByUnit(UnitToSingleVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(584);
    Exception er = null;
    try {
      _getBeanObject().generateWwdByUnit(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(584, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void generateWwd(PgdVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(585);
    Exception er = null;
    try {
      _getBeanObject().generateWwd(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(585, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public UFDouble[] Cd(UnitToSingleVO arg0, CdDataVO[] arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(586);
    Exception er = null;
    UFDouble[] o = null;
    try {
      o = (UFDouble[])_getBeanObject().Cd(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(586, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PgdVO[] convertUnitVoTopgdvos(UnitToSingleVO[] arg0) throws BusinessException {
    beforeCallMethod(587);
    Exception er = null;
    PgdVO[] o = null;
    try {
      o = (PgdVO[])_getBeanObject().convertUnitVoTopgdvos(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(587, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList convertVoToAggVO(Object arg0) throws BusinessException {
    beforeCallMethod(588);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().convertVoToAggVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(588, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RtItemVO getGxInfo(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(589);
    Exception er = null;
    RtItemVO o = null;
    try {
      o = _getBeanObject().getGxInfo(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(589, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PgdItemVO[] getPgdItemAndTsx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(590);
    Exception er = null;
    PgdItemVO[] o = null;
    try {
      o = (PgdItemVO[])_getBeanObject().getPgdItemAndTsx(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(590, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UnitToSingleVO[] getPgdVOs(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(591);
    Exception er = null;
    UnitToSingleVO[] o = null;
    try {
      o = (UnitToSingleVO[])_getBeanObject().getPgdVOs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(591, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UnitToSingleVO[] getPgdVOsByWk(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(592);
    Exception er = null;
    UnitToSingleVO[] o = null;
    try {
      o = (UnitToSingleVO[])_getBeanObject().getPgdVOsByWk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(592, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public KhwlszVO[] getTsxszByKsAndWl(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(593);
    Exception er = null;
    KhwlszVO[] o = null;
    try {
      o = (KhwlszVO[])_getBeanObject().getTsxszByKsAndWl(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(593, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public TsxszVO[] getTsxVOsByCorp(String arg0) throws BusinessException {
    beforeCallMethod(594);
    Exception er = null;
    TsxszVO[] o = null;
    try {
      o = (TsxszVO[])_getBeanObject().getTsxVOsByCorp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(594, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getValideGylx(String arg0, String arg1, String arg2, String arg3, UFDouble arg4) throws BusinessException {
    beforeCallMethod(595);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getValideGylx(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(595, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isExistPgdh(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(596);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isExistPgdh(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(596, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList queryDictory(String[] arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(597);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().queryDictory(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(597, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GxbcVO[] adjustGxbc(String arg0, String arg1, UnitToSingleVO arg2, GxbcVO[] arg3, UFDate[] arg4, Integer arg5) throws BusinessException {
    beforeCallMethod(598);
    Exception er = null;
    GxbcVO[] o = null;
    try {
      o = (GxbcVO[])_getBeanObject().adjustGxbc(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(598, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Hashtable bcfj(Hashtable arg0) throws BusinessException {
    beforeCallMethod(599);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().bcfj(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(599, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList dividePgdByWk(PgdVO[] arg0) throws BusinessException {
    beforeCallMethod(600);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().dividePgdByWk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(600, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GxbcVO[] getGxbcByPgd(String arg0) throws BusinessException {
    beforeCallMethod(601);
    Exception er = null;
    GxbcVO[] o = null;
    try {
      o = (GxbcVO[])_getBeanObject().getGxbcByPgd(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(601, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList getPgdKzcs(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(602);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getPgdKzcs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(602, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean pgdCancelXd(String[] arg0) throws BusinessException {
    beforeCallMethod(603);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().pgdCancelXd(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(603, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void pgdXd(String[] arg0, String arg1, UFDateTime arg2, UFBoolean arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(604);
    Exception er = null;
    try {
      _getBeanObject().pgdXd(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(604, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void pgdZyWw(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(605);
    Exception er = null;
    try {
      _getBeanObject().pgdZyWw(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(605, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void pgdDc(UnitToSingleVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(606);
    Exception er = null;
    try {
      _getBeanObject().pgdDc(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(606, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void pgdUndc(UnitToSingleVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(607);
    Exception er = null;
    try {
      _getBeanObject().pgdUndc(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(607, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.sf.sf2030.QueryDataVO queryToToTal(String arg0, String arg1, String arg2, String arg3, String arg4, UFDate[] arg5) throws BusinessException {
    beforeCallMethod(608);
    Exception er = null;
    nc.vo.sf.sf2030.QueryDataVO o = null;
    try {
      o = _getBeanObject().queryToToTal(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(608, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void returnPgd(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(609);
    Exception er = null;
    try {
      _getBeanObject().returnPgd(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(609, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void xgyxj(PgdVO[] arg0) throws BusinessException {
    beforeCallMethod(610);
    Exception er = null;
    try {
      _getBeanObject().xgyxj(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(610, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void batchUpdate(BatchUpdateCtrlVO arg0) throws BusinessException {
    beforeCallMethod(611);
    Exception er = null;
    try {
      _getBeanObject().batchUpdate(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(611, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList update_Pgd(UnitToSingleVO arg0) throws BusinessException {
    beforeCallMethod(612);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Pgd(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(612, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void Close(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(613);
    Exception er = null;
    try {
      _getBeanObject().Close(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(613, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PickmItemVO[] findItemsForHeader(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(614);
    Exception er = null;
    PickmItemVO[] o = null;
    try {
      o = (PickmItemVO[])_getBeanObject().findItemsForHeader(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(614, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void returnBillCode(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(615);
    Exception er = null;
    try {
      _getBeanObject().returnBillCode(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(615, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void returnBillCodeOnDelete_Pickm_mo2010(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(616);
    Exception er = null;
    try {
      _getBeanObject().returnBillCodeOnDelete_Pickm_mo2010(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(616, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String getBillCode_Pickm_mo2010(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(617);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getBillCode_Pickm_mo2010(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(617, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList queryDictory_Pickm_mo2010(String[] arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(618);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().queryDictory_Pickm_mo2010(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(618, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmItemVO[] findItemsForHeader_Pickm_mo2010(String arg0) throws BusinessException {
    beforeCallMethod(619);
    Exception er = null;
    PickmItemVO[] o = null;
    try {
      o = (PickmItemVO[])_getBeanObject().findItemsForHeader_Pickm_mo2010(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(619, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void splitPickmItemForGzzx(String arg0, String arg1, String arg2, String[] arg3, UFDouble arg4, UFDouble[] arg5, String arg6, String arg7) throws BusinessException {
    beforeCallMethod(620);
    Exception er = null;
    try {
      _getBeanObject().splitPickmItemForGzzx(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(620, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mm.pub.pub1030.PickmVO deliverMaterial(nc.vo.mm.pub.pub1030.PickmVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(621);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().deliverMaterial(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(621, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryPublic_Pickm_mo2010(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(622);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryPublic_Pickm_mo2010(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(622, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.PickmVO AcceptFromSuborder(PickmHeaderVO arg0, PickmItemVO arg1, HandandtakeUnitVO[] arg2) throws BusinessException {
    beforeCallMethod(623);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().AcceptFromSuborder(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(623, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.PickmVO findByPrimary(String arg0) throws BusinessException {
    beforeCallMethod(624);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().findByPrimary(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(624, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getWlmanid(String arg0) throws BusinessException {
    beforeCallMethod(625);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getWlmanid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(625, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String CancelCheck(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(626);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().CancelCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(626, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void CancelClose(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(627);
    Exception er = null;
    try {
      _getBeanObject().CancelClose(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(627, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void Check(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(628);
    Exception er = null;
    try {
      _getBeanObject().Check(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(628, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList batchCheck(nc.vo.mm.pub.pub1030.PickmVO[] arg0) throws BusinessException {
    beforeCallMethod(629);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().batchCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(629, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList collectDeliverMaterial(nc.vo.mm.pub.pub1030.PickmVO[] arg0, Object arg1) throws BusinessException {
    beforeCallMethod(630);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().collectDeliverMaterial(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(630, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.PickmVO createPickmVO(PickmHeaderVO arg0) throws BusinessException {
    beforeCallMethod(631);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().createPickmVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(631, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deletePickm(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(632);
    Exception er = null;
    try {
      _getBeanObject().deletePickm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(632, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public InvreplVO[] findByBtdwl(String arg0, String arg1, String arg2, String arg3, UFDate arg4, String arg5, FreeItemVO arg6, FreeItemVO arg7) throws BusinessException {
    beforeCallMethod(633);
    Exception er = null;
    InvreplVO[] o = null;
    try {
      o = (InvreplVO[])_getBeanObject().findByBtdwl(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(633, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector findPickmByGxh(String[] arg0, String[] arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(634);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().findPickmByGxh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(634, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] findClByItemPrimary(String arg0) throws BusinessException {
    beforeCallMethod(635);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().findClByItemPrimary(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(635, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmHeaderVO[] findHeaderByVO(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(636);
    Exception er = null;
    PickmHeaderVO[] o = null;
    try {
      o = (PickmHeaderVO[])_getBeanObject().findHeaderByVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(636, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void workingProcDM(nc.vo.mm.pub.pub1030.PickmVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(637);
    Exception er = null;
    try {
      _getBeanObject().workingProcDM(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(637, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList getATPNum(String arg0, String arg1, String[] arg2, String[] arg3, String[] arg4, String[] arg5, FreeItemVO[] arg6, String[] arg7) throws BusinessException {
    beforeCallMethod(638);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getATPNum(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(638, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Boolean getDefaultSfct(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(639);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().getDefaultSfct(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(639, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getReplaceInfo(String[] arg0) throws BusinessException {
    beforeCallMethod(640);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getReplaceInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(640, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getWHManager(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(641);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getWHManager(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(641, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Boolean isWholeMgt(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(642);
    Exception er = null;
    Boolean o = null;
    try {
      o = _getBeanObject().isWholeMgt(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(642, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void ModifyLydj(nc.vo.mm.pub.pub1030.PickmVO arg0, UFBoolean arg1) throws BusinessException {
    beforeCallMethod(643);
    Exception er = null;
    try {
      _getBeanObject().ModifyLydj(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(643, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Integer savePickm(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(644);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().savePickm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(644, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void setLockedFlag(ArrayList arg0, ArrayList arg1) throws BusinessException {
    beforeCallMethod(645);
    Exception er = null;
    try {
      _getBeanObject().setLockedFlag(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(645, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList batchCancelCheck(nc.vo.mm.pub.pub1030.PickmVO[] arg0) throws BusinessException {
    beforeCallMethod(646);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().batchCancelCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(646, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList batchCancelClose(nc.vo.mm.pub.pub1030.PickmVO[] arg0) throws BusinessException {
    beforeCallMethod(647);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().batchCancelClose(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(647, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList batchClose(nc.vo.mm.pub.pub1030.PickmVO[] arg0) throws BusinessException {
    beforeCallMethod(648);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().batchClose(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(648, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void batchSavePickm(nc.vo.mm.pub.pub1030.PickmVO[] arg0) throws BusinessException {
    beforeCallMethod(649);
    Exception er = null;
    try {
      _getBeanObject().batchSavePickm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(649, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public InvreplVO[] findTdlInfo(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(650);
    Exception er = null;
    InvreplVO[] o = null;
    try {
      o = (InvreplVO[])_getBeanObject().findTdlInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(650, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.PickmVO unAcceptFromSuborder(PickmHeaderVO arg0, PickmItemVO arg1, HandandtakeUnitVO[] arg2) throws BusinessException {
    beforeCallMethod(651);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().unAcceptFromSuborder(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(651, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Pickm_mo2010(String arg0, Integer arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(652);
    Exception er = null;
    try {
      _getBeanObject().delete_Pickm_mo2010(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(652, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList insert_Pickm_mo2010(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(653);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Pickm_mo2010(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(653, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList update_Pickm_mo2010(nc.vo.mm.pub.pub1030.PickmVO arg0) throws BusinessException {
    beforeCallMethod(654);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Pickm_mo2010(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(654, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void freeArray_PickmB(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(655);
    Exception er = null;
    try {
      _getBeanObject().freeArray_PickmB(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(655, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public boolean lockArray_PickmB(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(656);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lockArray_PickmB(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(656, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void backFlush(String[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(657);
    Exception er = null;
    try {
      _getBeanObject().backFlush(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(657, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void unBackFlush(String[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(658);
    Exception er = null;
    try {
      _getBeanObject().unBackFlush(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(658, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public MoHeaderVO[] queryByCondVO(ConditionVO[] arg0, ConditionVO[] arg1) throws BusinessException {
    beforeCallMethod(659);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().queryByCondVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(659, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmUnitVO[] queryBySql(ConditionVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(660);
    Exception er = null;
    PickmUnitVO[] o = null;
    try {
      o = (PickmUnitVO[])_getBeanObject().queryBySql(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(660, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList getInitData_PickmB_mo2035(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(661);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getInitData_PickmB_mo2035(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(661, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateArrays(HandandtakeUnitVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(662);
    Exception er = null;
    try {
      _getBeanObject().updateArrays(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(662, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public HandandtakeUnitVO[] querybyPick(String[] arg0) throws BusinessException {
    beforeCallMethod(663);
    Exception er = null;
    HandandtakeUnitVO[] o = null;
    try {
      o = (HandandtakeUnitVO[])_getBeanObject().querybyPick(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(663, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO[] getScddh(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(664);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().getScddh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(664, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void Dl(PickmBVO[] arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(665);
    Exception er = null;
    try {
      _getBeanObject().Dl(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(665, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PickmBVO[] queryByClbmid(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8) throws BusinessException {
    beforeCallMethod(666);
    Exception er = null;
    PickmBVO[] o = null;
    try {
      o = (PickmBVO[])_getBeanObject().queryByClbmid(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(666, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmBVO[] queryByScddh(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(667);
    Exception er = null;
    PickmBVO[] o = null;
    try {
      o = (PickmBVO[])_getBeanObject().queryByScddh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(667, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryPublic(String arg0) throws BusinessException {
    beforeCallMethod(668);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryPublic(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(668, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmBVO[] querySourceByScddh(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(669);
    Exception er = null;
    PickmBVO[] o = null;
    try {
      o = (PickmBVO[])_getBeanObject().querySourceByScddh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(669, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryPublic_Pickm(String[] arg0) throws BusinessException {
    beforeCallMethod(670);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryPublic_Pickm(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(670, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PickmItemVO[] collectQuery(ConditionVO[] arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(671);
    Exception er = null;
    PickmItemVO[] o = null;
    try {
      o = (PickmItemVO[])_getBeanObject().collectQuery(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(671, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo2020.PickmVO[] reverseQuery(ConditionVO[] arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(672);
    Exception er = null;
    nc.vo.mo.mo2020.PickmVO[] o = null;
    try {
      o = (nc.vo.mo.mo2020.PickmVO[])_getBeanObject().reverseQuery(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(672, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PlacedocItemVO[] findItemsForHeader_Placedoc(String arg0) throws BusinessException {
    beforeCallMethod(673);
    Exception er = null;
    PlacedocItemVO[] o = null;
    try {
      o = (PlacedocItemVO[])_getBeanObject().findItemsForHeader_Placedoc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(673, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PlacedocVO findPlacedocByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(674);
    Exception er = null;
    PlacedocVO o = null;
    try {
      o = _getBeanObject().findPlacedocByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(674, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PlacedocHeaderVO[] findHeaders(String arg0) throws BusinessException {
    beforeCallMethod(675);
    Exception er = null;
    PlacedocHeaderVO[] o = null;
    try {
      o = (PlacedocHeaderVO[])_getBeanObject().findHeaders(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(675, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Placedoc(PlacedocVO arg0) throws BusinessException {
    beforeCallMethod(676);
    Exception er = null;
    try {
      _getBeanObject().delete_Placedoc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(676, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String insert_Placedoc(PlacedocVO arg0) throws BusinessException {
    beforeCallMethod(677);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Placedoc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(677, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Placedoc(PlacedocVO arg0) throws BusinessException {
    beforeCallMethod(678);
    Exception er = null;
    try {
      _getBeanObject().update_Placedoc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(678, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.bd.b431.ProduceVO getProduct(String arg0) throws BusinessException {
    beforeCallMethod(679);
    Exception er = null;
    nc.vo.bd.b431.ProduceVO o = null;
    try {
      o = _getBeanObject().getProduct(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(679, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO findByPK(String arg0) throws BusinessException {
    beforeCallMethod(680);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO o = null;
    try {
      o = _getBeanObject().findByPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(680, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertPoArray(nc.vo.mm.pub.pub1025.PoVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(681);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertPoArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(681, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getPoBillCode(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(682);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getPoBillCode(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(682, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void checkPOstart(String arg0) throws BusinessException {
    beforeCallMethod(683);
    Exception er = null;
    try {
      _getBeanObject().checkPOstart(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(683, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void freeArray(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(684);
    Exception er = null;
    try {
      _getBeanObject().freeArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(684, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public boolean lockArray(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(685);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lockArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(685, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String deleteByXsdd(String arg0, String[] arg1, String[] arg2, String[] arg3, String arg4) throws BusinessException {
    beforeCallMethod(686);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().deleteByXsdd(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(686, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO[] findByJhddh(String arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(687);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.PoVO[])_getBeanObject().findByJhddh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(687, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] findDictForPo() throws BusinessException {
    beforeCallMethod(688);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().findDictForPo();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(688, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Hashtable findExcepts(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(689);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().findExcepts(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(689, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getCurrentPs(int arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(690);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getCurrentPs(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(690, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertJhddArray(nc.vo.mm.pub.pub1025.PoVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(691);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertJhddArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(691, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void insertOrsArray(ArrayList arg0, PraybillVO arg1) throws BusinessException {
    beforeCallMethod(692);
    Exception er = null;
    try {
      _getBeanObject().insertOrsArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(692, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void onPoCancel(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(693);
    Exception er = null;
    try {
      _getBeanObject().onPoCancel(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(693, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Vector onPoCancleRelease(nc.vo.mm.pub.pub1025.PoVO[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(694);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().onPoCancleRelease(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(694, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void onPoConfirm(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(695);
    Exception er = null;
    try {
      _getBeanObject().onPoConfirm(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(695, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void onPoRelease(nc.vo.mm.pub.pub1025.PoVO[] arg0, boolean arg1, UserVO arg2, String arg3) throws BusinessException {
    beforeCallMethod(696);
    Exception er = null;
    try {
      _getBeanObject().onPoRelease(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(696, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public PraybillVO potopro(ArrayList arg0, UFDate arg1, String arg2) throws BusinessException {
    beforeCallMethod(697);
    Exception er = null;
    PraybillVO o = null;
    try {
      o = _getBeanObject().potopro(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(697, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList sortPO(ReleaseVO arg0) throws BusinessException {
    beforeCallMethod(698);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().sortPO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(698, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean combineOrder(Vector arg0, String arg1) throws BusinessException {
    beforeCallMethod(699);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().combineOrder(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(699, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getRpcJhddh(nc.vo.mm.pub.pub1025.PoVO[] arg0) throws BusinessException {
    beforeCallMethod(700);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getRpcJhddh(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(700, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void onPoCancelOver(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(701);
    Exception er = null;
    try {
      _getBeanObject().onPoCancelOver(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(701, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void onPoOver(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(702);
    Exception er = null;
    try {
      _getBeanObject().onPoOver(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(702, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String[] insertPOs(nc.vo.mm.pub.pub1025.PoVO[] arg0) throws BusinessException {
    beforeCallMethod(703);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertPOs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(703, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Po(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(704);
    Exception er = null;
    try {
      _getBeanObject().delete_Po(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(704, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String insert_Po(nc.vo.mm.pub.pub1025.PoVO arg0) throws BusinessException {
    beforeCallMethod(705);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Po(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(705, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO[] query_Po(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(706);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.PoVO[])_getBeanObject().query_Po(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(706, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Po(nc.vo.mm.pub.pub1025.PoVO arg0) throws BusinessException {
    beforeCallMethod(707);
    Exception er = null;
    try {
      _getBeanObject().update_Po(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(707, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void freeArray_PoMoCreate(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(708);
    Exception er = null;
    try {
      _getBeanObject().freeArray_PoMoCreate(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(708, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public boolean lockArray_PoMoCreate(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(709);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lockArray_PoMoCreate(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(709, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BomHeaderVO[] getTpmrBOM(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(710);
    Exception er = null;
    BomHeaderVO[] o = null;
    try {
      o = (BomHeaderVO[])_getBeanObject().getTpmrBOM(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(710, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.PoVO[] getPoMpBackQueryJhdd(ConditionVO[] arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(711);
    Exception er = null;
    nc.vo.mp.mp4030.PoVO[] o = null;
    try {
      o = (nc.vo.mp.mp4030.PoVO[])_getBeanObject().getPoMpBackQueryJhdd(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(711, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.PoVO getDjcx(nc.vo.mp.mp4030.PoVO arg0) throws BusinessException {
    beforeCallMethod(712);
    Exception er = null;
    nc.vo.mp.mp4030.PoVO o = null;
    try {
      o = _getBeanObject().getDjcx(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(712, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.InitVO getInitDate() throws BusinessException {
    beforeCallMethod(713);
    Exception er = null;
    nc.vo.mp.mp4030.InitVO o = null;
    try {
      o = _getBeanObject().getInitDate();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(713, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mr.mr3020.InitVO getInitVO() throws BusinessException {
    beforeCallMethod(714);
    Exception er = null;
    nc.vo.mr.mr3020.InitVO o = null;
    try {
      o = _getBeanObject().getInitVO();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(714, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO[] queryJhdd(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(715);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.PoVO[])_getBeanObject().queryJhdd(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(715, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.PoVO getParent(String arg0, nc.vo.mp.mp4030.PoVO arg1, int arg2, String arg3) throws BusinessException {
    beforeCallMethod(716);
    Exception er = null;
    nc.vo.mp.mp4030.PoVO o = null;
    try {
      o = _getBeanObject().getParent(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(716, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.InitVO getPoXqBackQueryInitData() throws BusinessException {
    beforeCallMethod(717);
    Exception er = null;
    nc.vo.mp.mp4030.InitVO o = null;
    try {
      o = _getBeanObject().getPoXqBackQueryInitData();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(717, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mp.mp4030.PoVO[] getJhdd(ConditionVO[] arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(718);
    Exception er = null;
    nc.vo.mp.mp4030.PoVO[] o = null;
    try {
      o = (nc.vo.mp.mp4030.PoVO[])_getBeanObject().getJhdd(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(718, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ProductfactoryVO[] queryProductfactoryByWhereCondition(ConditionVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(719);
    Exception er = null;
    ProductfactoryVO[] o = null;
    try {
      o = (ProductfactoryVO[])_getBeanObject().queryProductfactoryByWhereCondition(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(719, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean delete_Productfactory(ProductfactoryVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(720);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Productfactory(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(720, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String insert_Productfactory(ProductfactoryVO arg0) throws BusinessException {
    beforeCallMethod(721);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Productfactory(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(721, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Productfactory(ProductfactoryVO arg0) throws BusinessException {
    beforeCallMethod(722);
    Exception er = null;
    try {
      _getBeanObject().update_Productfactory(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(722, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String[] copyPzbom(PzbomVO arg0) throws BusinessException {
    beforeCallMethod(723);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().copyPzbom(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(723, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deletePzbom(PzbomVO arg0) throws BusinessException {
    beforeCallMethod(724);
    Exception er = null;
    try {
      _getBeanObject().deletePzbom(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(724, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String[] insertPzbom(PzbomVO arg0) throws BusinessException {
    beforeCallMethod(725);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertPzbom(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(725, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public InvmandocVO getInvmandocByPK(String arg0) throws BusinessException {
    beforeCallMethod(726);
    Exception er = null;
    InvmandocVO o = null;
    try {
      o = _getBeanObject().getInvmandocByPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(726, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PzbomVO getPzbomByWlglid(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(727);
    Exception er = null;
    PzbomVO o = null;
    try {
      o = _getBeanObject().getPzbomByWlglid(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(727, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public PzbomZpVO[] getPzbomZpByBombPK(String arg0) throws BusinessException {
    beforeCallMethod(728);
    Exception er = null;
    PzbomZpVO[] o = null;
    try {
      o = (PzbomZpVO[])_getBeanObject().getPzbomZpByBombPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(728, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updatePzbom(PzbomVO arg0) throws BusinessException {
    beforeCallMethod(729);
    Exception er = null;
    try {
      _getBeanObject().updatePzbom(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(729, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Vector checkPzbomCircle(String arg0) throws BusinessException {
    beforeCallMethod(730);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().checkPzbomCircle(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(730, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] batchDeal(String[] arg0, String[] arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(731);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().batchDeal(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(731, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deal(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(732);
    Exception er = null;
    try {
      _getBeanObject().deal(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(732, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public QryScddVO[] queryScddInfoVO(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(733);
    Exception er = null;
    QryScddVO[] o = null;
    try {
      o = (QryScddVO[])_getBeanObject().queryScddInfoVO(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(733, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MmReeditLogCircularlyAcccessibleVO[] queryMOReeditLog(String arg0) throws BusinessException {
    beforeCallMethod(734);
    Exception er = null;
    MmReeditLogCircularlyAcccessibleVO[] o = null;
    try {
      o = (MmReeditLogCircularlyAcccessibleVO[])_getBeanObject().queryMOReeditLog(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(734, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MmReeditLogCircularlyAcccessibleVO[] queryBljhReeditLog(String arg0) throws BusinessException {
    beforeCallMethod(735);
    Exception er = null;
    MmReeditLogCircularlyAcccessibleVO[] o = null;
    try {
      o = (MmReeditLogCircularlyAcccessibleVO[])_getBeanObject().queryBljhReeditLog(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(735, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MmReeditLogCircularlyAcccessibleVO[] queryBljhBody(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(736);
    Exception er = null;
    MmReeditLogCircularlyAcccessibleVO[] o = null;
    try {
      o = (MmReeditLogCircularlyAcccessibleVO[])_getBeanObject().queryBljhBody(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(736, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MmReeditLogCircularlyAcccessibleVO[] queryWgbgReeditLog(String arg0) throws BusinessException {
    beforeCallMethod(737);
    Exception er = null;
    MmReeditLogCircularlyAcccessibleVO[] o = null;
    try {
      o = (MmReeditLogCircularlyAcccessibleVO[])_getBeanObject().queryWgbgReeditLog(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(737, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MmReeditLogCircularlyAcccessibleVO[] queryWgbgBody(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(738);
    Exception er = null;
    MmReeditLogCircularlyAcccessibleVO[] o = null;
    try {
      o = (MmReeditLogCircularlyAcccessibleVO[])_getBeanObject().queryWgbgBody(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(738, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer setToken_Rlist(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(739);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().setToken_Rlist(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(739, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer releaseToken_Rlist(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(740);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().releaseToken_Rlist(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(740, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean isUseRoute_Rlist(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(741);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isUseRoute_Rlist(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(741, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RlistVO[] queryZyqdByVO(RlistVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(742);
    Exception er = null;
    RlistVO[] o = null;
    try {
      o = (RlistVO[])_getBeanObject().queryZyqdByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(742, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RlistVO[] queryMaterialByVO(RlistVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(743);
    Exception er = null;
    RlistVO[] o = null;
    try {
      o = (RlistVO[])_getBeanObject().queryMaterialByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(743, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RlistVO[] queryWorkCenterByVO(RlistVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(744);
    Exception er = null;
    RlistVO[] o = null;
    try {
      o = (RlistVO[])_getBeanObject().queryWorkCenterByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(744, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RlistVO[] queryMaterialBodyByVO(RlistVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(745);
    Exception er = null;
    RlistVO[] o = null;
    try {
      o = (RlistVO[])_getBeanObject().queryMaterialBodyByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(745, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public RlistVO[] queryWorkCenterBodyByVO(RlistVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(746);
    Exception er = null;
    RlistVO[] o = null;
    try {
      o = (RlistVO[])_getBeanObject().queryWorkCenterBodyByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(746, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void createRlist(String arg0, String arg1, UFDate arg2, String[] arg3, WlPropVO[] arg4) throws BusinessException {
    beforeCallMethod(747);
    Exception er = null;
    try {
      _getBeanObject().createRlist(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(747, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public WlPropVO[] queryMpsMaterial(String arg0, String arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(748);
    Exception er = null;
    WlPropVO[] o = null;
    try {
      o = (WlPropVO[])_getBeanObject().queryMpsMaterial(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(748, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList queryByCondition_RyzTotal(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(749);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().queryByCondition_RyzTotal(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(749, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findDbjsByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(750);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findDbjsByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(750, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findDbyzByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(751);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findDbyzByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(751, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findFsbgByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(752);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findFsbgByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(752, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findWgqkByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(753);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findWgqkByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(753, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findXhqkByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(754);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findXhqkByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(754, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList findYcyzByRqWkclassid(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(755);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().findYcyzByRqWkclassid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(755, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO[] createMo(nc.vo.mm.pub.pub1025.PoVO arg0, MoHeaderVO[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(756);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().createMo(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(756, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GzzxjxVO[] queryGzxxjxByWkPK(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(757);
    Exception er = null;
    GzzxjxVO[] o = null;
    try {
      o = (GzzxjxVO[])_getBeanObject().queryGzxxjxByWkPK(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(757, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO[] queryMoByPoPK(String arg0) throws BusinessException {
    beforeCallMethod(758);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().queryMoByPoPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(758, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO[] queryMoByWkPK(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(759);
    Exception er = null;
    MoHeaderVO[] o = null;
    try {
      o = (MoHeaderVO[])_getBeanObject().queryMoByWkPK(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(759, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Hashtable queryPga(String arg0, UFDate arg1) throws BusinessException {
    beforeCallMethod(760);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().queryPga(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(760, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO[] queryPoByCon(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(761);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.PoVO[])_getBeanObject().queryPoByCon(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(761, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.PoVO queryPoByPoPK(String arg0) throws BusinessException {
    beforeCallMethod(762);
    Exception er = null;
    nc.vo.mm.pub.pub1025.PoVO o = null;
    try {
      o = _getBeanObject().queryPoByPoPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(762, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DisItemVO queryProduceByPK(String arg0) throws BusinessException {
    beforeCallMethod(763);
    Exception er = null;
    DisItemVO o = null;
    try {
      o = _getBeanObject().queryProduceByPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(763, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ScVO[] queryScByWkPKDate(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(764);
    Exception er = null;
    ScVO[] o = null;
    try {
      o = (ScVO[])_getBeanObject().queryScByWkPKDate(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(764, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] queryVersionByProducePK(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(765);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().queryVersionByProducePK(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(765, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WkVO queryWkByPK(String arg0) throws BusinessException {
    beforeCallMethod(766);
    Exception er = null;
    WkVO o = null;
    try {
      o = _getBeanObject().queryWkByPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(766, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WkVO[] queryWkByProducePK(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(767);
    Exception er = null;
    WkVO[] o = null;
    try {
      o = (WkVO[])_getBeanObject().queryWkByProducePK(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(767, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SflbdzVO[] findAll(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(768);
    Exception er = null;
    SflbdzVO[] o = null;
    try {
      o = (SflbdzVO[])_getBeanObject().findAll(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(768, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] findSflbid1(String arg0, String arg1, Integer[] arg2, String[] arg3, Integer[] arg4) throws BusinessException {
    beforeCallMethod(769);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().findSflbid1(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(769, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] findSflbid2(String arg0, String arg1, Integer[] arg2, String[] arg3) throws BusinessException {
    beforeCallMethod(770);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().findSflbid2(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(770, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Sflbdz(SflbdzVO[] arg0) throws BusinessException {
    beforeCallMethod(771);
    Exception er = null;
    try {
      _getBeanObject().update_Sflbdz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(771, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public WantInvVO[] getInsteadVOs(WantInvVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(772);
    Exception er = null;
    WantInvVO[] o = null;
    try {
      o = (WantInvVO[])_getBeanObject().getInsteadVOs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(772, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public FrockInvVO[] getFrockInvVOs(WantInvVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(773);
    Exception er = null;
    FrockInvVO[] o = null;
    try {
      o = (FrockInvVO[])_getBeanObject().getFrockInvVOs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(773, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WantInvVO[][] simput(nc.vo.mm.pub.pub1030.MoVO[] arg0) throws BusinessException {
    beforeCallMethod(774);
    Exception er = null;
    WantInvVO[][] o = (WantInvVO[][])null;
    try {
      o = (WantInvVO[][])_getBeanObject().simput(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(774, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String mpsBalance(MMInputVO arg0) throws BusinessException {
    beforeCallMethod(775);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().mpsBalance(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(775, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer releaseMpsToken(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(776);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().releaseMpsToken(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(776, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer setMpsToken(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(777);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().setMpsToken(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(777, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SupplyRatioVO[] getDetails(ConditionVO[] arg0, int arg1, String arg2) throws BusinessException {
    beforeCallMethod(778);
    Exception er = null;
    SupplyRatioVO[] o = null;
    try {
      o = (SupplyRatioVO[])_getBeanObject().getDetails(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(778, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public SupplyRatioVO[] queryByConditionVO_SupplyRatio(ConditionVO[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(779);
    Exception er = null;
    SupplyRatioVO[] o = null;
    try {
      o = (SupplyRatioVO[])_getBeanObject().queryByConditionVO_SupplyRatio(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(779, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public TaskShowVO[] queryTaskShowInfo(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(780);
    Exception er = null;
    TaskShowVO[] o = null;
    try {
      o = (TaskShowVO[])_getBeanObject().queryTaskShowInfo(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(780, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public JslxszVO[] getAllExpands(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(781);
    Exception er = null;
    JslxszVO[] o = null;
    try {
      o = (JslxszVO[])_getBeanObject().getAllExpands(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(781, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillTempletBodyVO[] getTsxItems(String arg0) throws BusinessException {
    beforeCallMethod(782);
    Exception er = null;
    BillTempletBodyVO[] o = null;
    try {
      o = (BillTempletBodyVO[])_getBeanObject().getTsxItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(782, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public TotalQueryVO getTotal(String arg0, String arg1, UFDate[] arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(783);
    Exception er = null;
    TotalQueryVO o = null;
    try {
      o = _getBeanObject().getTotal(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(783, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.sf.sf2030.InitVO getInitData(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(784);
    Exception er = null;
    nc.vo.sf.sf2030.InitVO o = null;
    try {
      o = _getBeanObject().getInitData(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(784, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public TsxszVO[] queryAll_Tsxsz(String arg0) throws BusinessException {
    beforeCallMethod(785);
    Exception er = null;
    TsxszVO[] o = null;
    try {
      o = (TsxszVO[])_getBeanObject().queryAll_Tsxsz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(785, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] saveArray_Tsxsz(TsxszVO[] arg0) throws BusinessException {
    beforeCallMethod(786);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().saveArray_Tsxsz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(786, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean isTsxUsed(String arg0) throws BusinessException {
    beforeCallMethod(787);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().isTsxUsed(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(787, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble[] getReportNum(String arg0) throws BusinessException {
    beforeCallMethod(788);
    Exception er = null;
    UFDouble[] o = null;
    try {
      o = (UFDouble[])_getBeanObject().getReportNum(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(788, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WgmxVO[] queryByWherePart(String arg0) throws BusinessException {
    beforeCallMethod(789);
    Exception er = null;
    WgmxVO[] o = null;
    try {
      o = (WgmxVO[])_getBeanObject().queryByWherePart(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(789, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public HashMap queryWkItems(MaterialVO[] arg0) throws BusinessException {
    beforeCallMethod(790);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().queryWkItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(790, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WkScheduleResultVO runWkSchedule(WkScheduleInputVO arg0) throws BusinessException {
    beforeCallMethod(791);
    Exception er = null;
    WkScheduleResultVO o = null;
    try {
      o = _getBeanObject().runWkSchedule(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(791, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getProduceInfo_Wlgz(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(792);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getProduceInfo_Wlgz(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(792, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GzglItemVO[] findWlgzs(String arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(793);
    Exception er = null;
    GzglItemVO[] o = null;
    try {
      o = (GzglItemVO[])_getBeanObject().findWlgzs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(793, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public GzglHeaderVO[] findWrItems(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(794);
    Exception er = null;
    GzglHeaderVO[] o = null;
    try {
      o = (GzglHeaderVO[])_getBeanObject().findWrItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(794, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getSerialAndWhole(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(795);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getSerialAndWhole(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(795, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList update_Wlgz(GzglHeaderVO arg0, GzglItemVO[] arg1) throws BusinessException {
    beforeCallMethod(796);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Wlgz(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(796, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean lock(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(797);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lock(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(797, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1030.WrItemVO[] getItems(String arg0) throws BusinessException {
    beforeCallMethod(798);
    Exception er = null;
    nc.vo.mo.mo1030.WrItemVO[] o = null;
    try {
      o = (nc.vo.mo.mo1030.WrItemVO[])_getBeanObject().getItems(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(798, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void free(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(799);
    Exception er = null;
    try {
      _getBeanObject().free(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(799, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mo.mo1030.WrHeaderVO[] queryByWhere(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(800);
    Exception er = null;
    nc.vo.mo.mo1030.WrHeaderVO[] o = null;
    try {
      o = (nc.vo.mo.mo1030.WrHeaderVO[])_getBeanObject().queryByWhere(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(800, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public HandandtakeUnitVO[] getJjd_Wr(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(801);
    Exception er = null;
    HandandtakeUnitVO[] o = null;
    try {
      o = (HandandtakeUnitVO[])_getBeanObject().getJjd_Wr(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(801, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void returnWrBillCodeOnDelete(String arg0, String arg1, String arg2, String arg3, String arg4) throws BusinessException {
    beforeCallMethod(802);
    Exception er = null;
    try {
      _getBeanObject().returnWrBillCodeOnDelete(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(802, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String getWrBillCode(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(803);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getWrBillCode(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(803, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public KzcsVO[] getKzcs_Wr(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(804);
    Exception er = null;
    KzcsVO[] o = null;
    try {
      o = (KzcsVO[])_getBeanObject().getKzcs_Wr(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(804, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void unBackFlush_Wr(WrVO arg0) throws BusinessException {
    beforeCallMethod(805);
    Exception er = null;
    try {
      _getBeanObject().unBackFlush_Wr(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(805, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void makeWrAndDeposit(CetzVO arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(806);
    Exception er = null;
    try {
      _getBeanObject().makeWrAndDeposit(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(806, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void getZylsFromWrVOs(nc.vo.mo.mo1070.WrItemVO[] arg0) throws BusinessException {
    beforeCallMethod(807);
    Exception er = null;
    try {
      _getBeanObject().getZylsFromWrVOs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(807, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public QualityReportVO findQReports(nc.vo.mo.mo1030.WrItemVO arg0) throws BusinessException {
    beforeCallMethod(808);
    Exception er = null;
    QualityReportVO o = null;
    try {
      o = _getBeanObject().findQReports(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(808, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer checkTimeStamp(WrVO arg0) throws BusinessException {
    beforeCallMethod(809);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().checkTimeStamp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(809, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer checkCurStamp(WrVO arg0) throws BusinessException {
    beforeCallMethod(810);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().checkCurStamp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(810, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean checkQCstart(String arg0) throws BusinessException {
    beforeCallMethod(811);
    Exception er = null;
    UFBoolean o = null;
    try {
      o = _getBeanObject().checkQCstart(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(811, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteByPgd(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(812);
    Exception er = null;
    try {
      _getBeanObject().deleteByPgd(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(812, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void reportFinishedCount(WrVO arg0) throws BusinessException {
    beforeCallMethod(813);
    Exception er = null;
    try {
      _getBeanObject().reportFinishedCount(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(813, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void freeMOArray(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(814);
    Exception er = null;
    try {
      _getBeanObject().freeMOArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(814, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void freeWrItemArray(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(815);
    Exception er = null;
    try {
      _getBeanObject().freeWrItemArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(815, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mo.mo1030.WrItemVO[] getAllItemsByMO(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(816);
    Exception er = null;
    nc.vo.mo.mo1030.WrItemVO[] o = null;
    try {
      o = (nc.vo.mo.mo1030.WrItemVO[])_getBeanObject().getAllItemsByMO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(816, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] getDdsJybz() throws BusinessException {
    beforeCallMethod(817);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getDdsJybz();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(817, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] getDdsLylx() throws BusinessException {
    beforeCallMethod(818);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getDdsLylx();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(818, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList getFlushMO(WrVO arg0) throws BusinessException {
    beforeCallMethod(819);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().getFlushMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(819, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void getFlushMOAndDc(WrVO arg0) throws BusinessException {
    beforeCallMethod(820);
    Exception er = null;
    try {
      _getBeanObject().getFlushMOAndDc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(820, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ZylVO[] queryZylsForItem(String arg0) throws BusinessException {
    beforeCallMethod(821);
    Exception er = null;
    ZylVO[] o = null;
    try {
      o = (ZylVO[])_getBeanObject().queryZylsForItem(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(821, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean[] getManageFlag(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(822);
    Exception er = null;
    UFBoolean[] o = null;
    try {
      o = (UFBoolean[])_getBeanObject().getManageFlag(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(822, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isFlushMO(String arg0) throws BusinessException {
    beforeCallMethod(823);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isFlushMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(823, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public MoHeaderVO findMOHeaderByMOid(String arg0) throws BusinessException {
    beforeCallMethod(824);
    Exception er = null;
    MoHeaderVO o = null;
    try {
      o = _getBeanObject().findMOHeaderByMOid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(824, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean lockMOArray(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(825);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().lockMOArray(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(825, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void qualifiedFinish(WrVO arg0) throws BusinessException {
    beforeCallMethod(826);
    Exception er = null;
    try {
      _getBeanObject().qualifiedFinish(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(826, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public BomVO queryByproduct(String arg0) throws BusinessException {
    beforeCallMethod(827);
    Exception er = null;
    BomVO o = null;
    try {
      o = _getBeanObject().queryByproduct(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(827, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean queryMoDczt(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(828);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().queryMoDczt(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(828, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String updateWrFxsl(String arg0, UFDouble arg1, UFDouble arg2) throws BusinessException {
    beforeCallMethod(829);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().updateWrFxsl(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(829, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void writeBfsl(String arg0, UFDouble arg1, UFDouble arg2) throws BusinessException {
    beforeCallMethod(830);
    Exception er = null;
    try {
      _getBeanObject().writeBfsl(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(830, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String checkExcessCtrl(nc.vo.mo.mo1030.WrItemVO[] arg0, UFDouble arg1) throws BusinessException {
    beforeCallMethod(831);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().checkExcessCtrl(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(831, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZylVO[] queryDefaultZylsForItem(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(832);
    Exception er = null;
    ZylVO[] o = null;
    try {
      o = (ZylVO[])_getBeanObject().queryDefaultZylsForItem(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(832, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void writeYyjsl(String arg0, UFDouble[] arg1) throws BusinessException {
    beforeCallMethod(833);
    Exception er = null;
    try {
      _getBeanObject().writeYyjsl(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(833, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList wrYj(HandandtakeUnitVO arg0, String arg1, String arg2, UFDouble arg3) throws BusinessException {
    beforeCallMethod(834);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().wrYj(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(834, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getUnQualifiednum(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(835);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getUnQualifiednum(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(835, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isBfMadeMO(String arg0) throws BusinessException {
    beforeCallMethod(836);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isBfMadeMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(836, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean[] ischkFree2(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(837);
    Exception er = null;
    UFBoolean[] o = null;
    try {
      o = (UFBoolean[])_getBeanObject().ischkFree2(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(837, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1030.ProduceVO getProduceVO(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(838);
    Exception er = null;
    nc.vo.mo.mo1030.ProduceVO o = null;
    try {
      o = _getBeanObject().getProduceVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(838, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isFxMadeMO(String arg0) throws BusinessException {
    beforeCallMethod(839);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isFxMadeMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(839, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFBoolean[] isStockDirect(String arg0, String[] arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(840);
    Exception er = null;
    UFBoolean[] o = null;
    try {
      o = (UFBoolean[])_getBeanObject().isStockDirect(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(840, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[][] queryMoztAndDcFlag(String[] arg0) throws BusinessException {
    beforeCallMethod(841);
    Exception er = null;
    String[][] o = (String[][])null;
    try {
      o = (String[][])_getBeanObject().queryMoztAndDcFlag(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(841, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WrVO queryWrVOByPk(String arg0) throws BusinessException {
    beforeCallMethod(842);
    Exception er = null;
    WrVO o = null;
    try {
      o = _getBeanObject().queryWrVOByPk(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(842, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public YieldInfoVO[] getScddidFilterByScbmPgd(YieldInfoVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(843);
    Exception er = null;
    YieldInfoVO[] o = null;
    try {
      o = (YieldInfoVO[])_getBeanObject().getScddidFilterByScbmPgd(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(843, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void divideAndDeposit(WrVO[] arg0) throws BusinessException {
    beforeCallMethod(844);
    Exception er = null;
    try {
      _getBeanObject().divideAndDeposit(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(844, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public HashMap getShaoJieWlInfo(String arg0, String arg1, String[] arg2) throws BusinessException {
    beforeCallMethod(845);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getShaoJieWlInfo(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(845, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getSumICOut(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(846);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getSumICOut(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(846, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public HashMap getSumSlByProduces(String arg0) throws BusinessException {
    beforeCallMethod(847);
    Exception er = null;
    HashMap o = null;
    try {
      o = _getBeanObject().getSumSlByProduces(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(847, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] getDdccxxZdys(String arg0) throws BusinessException {
    beforeCallMethod(848);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getDdccxxZdys(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(848, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void checkQcElementcheckflag(String arg0) throws BusinessException {
    beforeCallMethod(849);
    Exception er = null;
    try {
      _getBeanObject().checkQcElementcheckflag(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(849, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String isExistSubWr(String arg0) throws BusinessException {
    beforeCallMethod(850);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().isExistSubWr(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(850, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void divideAndReject(WrVO[] arg0) throws BusinessException {
    beforeCallMethod(851);
    Exception er = null;
    try {
      _getBeanObject().divideAndReject(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(851, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void delete_Wr(WrVO arg0) throws BusinessException {
    beforeCallMethod(852);
    Exception er = null;
    try {
      _getBeanObject().delete_Wr(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(852, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList insert_Wr(WrVO arg0) throws BusinessException {
    beforeCallMethod(853);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Wr(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(853, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList update_Wr(WrVO arg0) throws BusinessException {
    beforeCallMethod(854);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Wr(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(854, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1035.WrItemVO[] queryByCondition(ConditionVO[] arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(855);
    Exception er = null;
    nc.vo.mo.mo1035.WrItemVO[] o = null;
    try {
      o = (nc.vo.mo.mo1035.WrItemVO[])_getBeanObject().queryByCondition(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(855, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] getDds() throws BusinessException {
    beforeCallMethod(856);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getDds();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(856, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZylVO[] queryZylsForWrStat(ConditionVO[] arg0, Integer arg1, String arg2) throws BusinessException {
    beforeCallMethod(857);
    Exception er = null;
    ZylVO[] o = null;
    try {
      o = (ZylVO[])_getBeanObject().queryZylsForWrStat(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(857, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public BillTempletVO getBillTempletVO() throws BusinessException {
    beforeCallMethod(858);
    Exception er = null;
    BillTempletVO o = null;
    try {
      o = _getBeanObject().getBillTempletVO();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(858, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1070.YhtzVO[] getYhtz(String arg0) throws BusinessException {
    beforeCallMethod(859);
    Exception er = null;
    nc.vo.mo.mo1070.YhtzVO[] o = null;
    try {
      o = (nc.vo.mo.mo1070.YhtzVO[])_getBeanObject().getYhtz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(859, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertYhtz(nc.vo.mo.mo1070.YhtzVO[] arg0) throws BusinessException {
    beforeCallMethod(860);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertYhtz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(860, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateYxgs(nc.vo.mo.mo1070.WrItemVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(861);
    Exception er = null;
    try {
      _getBeanObject().updateYxgs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(861, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public void updateZyls(ZylVO[] arg0) throws BusinessException {
    beforeCallMethod(862);
    Exception er = null;
    try {
      _getBeanObject().updateZyls(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(862, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mm.pub.pub1030.WrHeaderVO[] getWrHeadByConVO(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(863);
    Exception er = null;
    nc.vo.mm.pub.pub1030.WrHeaderVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.WrHeaderVO[])_getBeanObject().getWrHeadByConVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(863, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mo.mo1070.WrItemVO[] getWrItemByHeaderPK(String arg0) throws BusinessException {
    beforeCallMethod(864);
    Exception er = null;
    nc.vo.mo.mo1070.WrItemVO[] o = null;
    try {
      o = (nc.vo.mo.mo1070.WrItemVO[])_getBeanObject().getWrItemByHeaderPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(864, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XfyaVO[] queryXfyaByWhereCondition(ConditionVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(865);
    Exception er = null;
    XfyaVO[] o = null;
    try {
      o = (XfyaVO[])_getBeanObject().queryXfyaByWhereCondition(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(865, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XfyaVO[] queryByWhereCond(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(866);
    Exception er = null;
    XfyaVO[] o = null;
    try {
      o = (XfyaVO[])_getBeanObject().queryByWhereCond(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(866, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XfyaVO[] queryXfyaByWhereCond(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(867);
    Exception er = null;
    XfyaVO[] o = null;
    try {
      o = (XfyaVO[])_getBeanObject().queryXfyaByWhereCond(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(867, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean delete_Xfya(XfyaVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(868);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Xfya(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(868, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String insert_Xfya(XfyaVO arg0) throws BusinessException {
    beforeCallMethod(869);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Xfya(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(869, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Xfya(XfyaVO arg0) throws BusinessException {
    beforeCallMethod(870);
    Exception er = null;
    try {
      _getBeanObject().update_Xfya(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(870, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public XqfjVO findXqfjByPrimaryKey(String arg0) throws BusinessException {
    beforeCallMethod(871);
    Exception er = null;
    XqfjVO o = null;
    try {
      o = _getBeanObject().findXqfjByPrimaryKey(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(871, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4010.InvInfoVO[] getExistValidDataWlglids(nc.vo.dm.dm4020.InputVO arg0) throws BusinessException {
    beforeCallMethod(872);
    Exception er = null;
    nc.vo.dm.dm4010.InvInfoVO[] o = null;
    try {
      o = (nc.vo.dm.dm4010.InvInfoVO[])_getBeanObject().getExistValidDataWlglids(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(872, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4010.InvInfoVO[] getWlInfo(String arg0) throws BusinessException {
    beforeCallMethod(873);
    Exception er = null;
    nc.vo.dm.dm4010.InvInfoVO[] o = null;
    try {
      o = (nc.vo.dm.dm4010.InvInfoVO[])_getBeanObject().getWlInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(873, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getXqfjForBrowse(nc.vo.dm.dm4020.InputVO arg0) throws BusinessException {
    beforeCallMethod(874);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getXqfjForBrowse(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(874, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XqfjVO[] getXqfjForDetail(nc.vo.dm.dm4020.InputVO arg0) throws BusinessException {
    beforeCallMethod(875);
    Exception er = null;
    XqfjVO[] o = null;
    try {
      o = (XqfjVO[])_getBeanObject().getXqfjForDetail(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(875, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getXqfpBillCode(String arg0, String arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(876);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getXqfpBillCode(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(876, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.XqfpVO[] queryXqfpByWhereCondition(ConditionVO[] arg0, ConditionVO[] arg1, String arg2, String arg3) throws BusinessException {
    beforeCallMethod(877);
    Exception er = null;
    nc.vo.mm.pub.pub1025.XqfpVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.XqfpVO[])_getBeanObject().queryXqfpByWhereCondition(arg0, arg1, arg2, arg3);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(877, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteXqfp(nc.vo.mm.pub.pub1025.XqfpVO arg0) throws BusinessException {
    beforeCallMethod(878);
    Exception er = null;
    try {
      _getBeanObject().deleteXqfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(878, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.mm.pub.pub1025.XqfpVO getMDSByPK(String arg0) throws BusinessException {
    beforeCallMethod(879);
    Exception er = null;
    nc.vo.mm.pub.pub1025.XqfpVO o = null;
    try {
      o = _getBeanObject().getMDSByPK(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(879, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String insertXqfp(nc.vo.mm.pub.pub1025.XqfpVO arg0) throws BusinessException {
    beforeCallMethod(880);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insertXqfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(880, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertXqfpArray(nc.vo.mm.pub.pub1025.XqfpVO[] arg0) throws BusinessException {
    beforeCallMethod(881);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertXqfpArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(881, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean isBillCodeExist(String arg0) throws BusinessException {
    beforeCallMethod(882);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().isBillCodeExist(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(882, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.XqfpVO[] queryByDateAndMaterial(nc.vo.mm.pub.pub1025.XqfpVO arg0, UFDate arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(883);
    Exception er = null;
    nc.vo.mm.pub.pub1025.XqfpVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.XqfpVO[])_getBeanObject().queryByDateAndMaterial(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(883, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void updateXqfp(nc.vo.mm.pub.pub1025.XqfpVO arg0) throws BusinessException {
    beforeCallMethod(884);
    Exception er = null;
    try {
      _getBeanObject().updateXqfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(884, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public DdVO[] getXtdxlx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(885);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().getXtdxlx(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(885, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4030.XqfpVO[] queryXqfpQueryByVO(nc.vo.dm.dm4030.XqfpVO arg0, Boolean arg1) throws BusinessException {
    beforeCallMethod(886);
    Exception er = null;
    nc.vo.dm.dm4030.XqfpVO[] o = null;
    try {
      o = (nc.vo.dm.dm4030.XqfpVO[])_getBeanObject().queryXqfpQueryByVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(886, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] queryLylx(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(887);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().queryLylx(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(887, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4030.XqfpVO[] queryVoByCondition(ConditionVO[] arg0, int arg1) throws BusinessException {
    beforeCallMethod(888);
    Exception er = null;
    nc.vo.dm.dm4030.XqfpVO[] o = null;
    try {
      o = (nc.vo.dm.dm4030.XqfpVO[])_getBeanObject().queryVoByCondition(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(888, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4010.InvInfoVO[] getWlInfo_XqfpXtQuery(String arg0) throws BusinessException {
    beforeCallMethod(889);
    Exception er = null;
    nc.vo.dm.dm4010.InvInfoVO[] o = null;
    try {
      o = (nc.vo.dm.dm4010.InvInfoVO[])_getBeanObject().getWlInfo_XqfpXtQuery(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(889, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector getXqfpForBrowse(nc.vo.dm.dm3040.InputVO arg0) throws BusinessException {
    beforeCallMethod(890);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().getXqfpForBrowse(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(890, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.XqfpVO[] getXqfpForDetail(nc.vo.dm.dm3040.InputVO arg0) throws BusinessException {
    beforeCallMethod(891);
    Exception er = null;
    nc.vo.mm.pub.pub1025.XqfpVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.XqfpVO[])_getBeanObject().getXqfpForDetail(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(891, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.dm.dm4010.InvInfoVO[] queryExistValidDataWl(nc.vo.dm.dm3040.InputVO arg0) throws BusinessException {
    beforeCallMethod(892);
    Exception er = null;
    nc.vo.dm.dm4010.InvInfoVO[] o = null;
    try {
      o = (nc.vo.dm.dm4010.InvInfoVO[])_getBeanObject().queryExistValidDataWl(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(892, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String getJhyid(String arg0) throws BusinessException {
    beforeCallMethod(893);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getJhyid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(893, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DdVO[] queryPublic_Xqly(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(894);
    Exception er = null;
    DdVO[] o = null;
    try {
      o = (DdVO[])_getBeanObject().queryPublic_Xqly(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(894, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XqlyVO[] findByVO(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(895);
    Exception er = null;
    XqlyVO[] o = null;
    try {
      o = (XqlyVO[])_getBeanObject().findByVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(895, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean delete_Xqly(XqlyVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(896);
    Exception er = null;
    boolean o = false;
    try {
      o = _getBeanObject().delete_Xqly(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(896, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String insert_Xqly(XqlyVO arg0) throws BusinessException {
    beforeCallMethod(897);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Xqly(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(897, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Xqly(XqlyVO arg0) throws BusinessException {
    beforeCallMethod(898);
    Exception er = null;
    try {
      _getBeanObject().update_Xqly(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(898, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public nc.vo.dm.dm3020.InitVO getXsddInitData(String arg0) throws BusinessException {
    beforeCallMethod(899);
    Exception er = null;
    nc.vo.dm.dm3020.InitVO o = null;
    try {
      o = _getBeanObject().getXsddInitData(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(899, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insertXsddArray(XsddVO[] arg0) throws BusinessException {
    beforeCallMethod(900);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insertXsddArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(900, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XsddVO[] queryExistValidDataWl_Xsdd(String arg0, UFDate arg1, UFDate arg2, Integer arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(901);
    Exception er = null;
    XsddVO[] o = null;
    try {
      o = (XsddVO[])_getBeanObject().queryExistValidDataWl_Xsdd(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(901, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XsddVO[] queryXsddByWl(String arg0, String arg1, UFDate arg2, Integer arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(902);
    Exception er = null;
    XsddVO[] o = null;
    try {
      o = (XsddVO[])_getBeanObject().queryXsddByWl(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(902, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String cancelJrgc(String arg0, String arg1, XsddVO[] arg2) throws BusinessException {
    beforeCallMethod(903);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().cancelJrgc(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(903, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public LoadReportVO loadXsdd(String arg0, String arg1, String arg2, String arg3, UFDate arg4, Integer arg5) throws BusinessException {
    beforeCallMethod(904);
    Exception er = null;
    LoadReportVO o = null;
    try {
      o = _getBeanObject().loadXsdd(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(904, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public LwVO[] xsddJrgc(String arg0, String arg1, UFDate arg2, UFDate arg3, XsddVO[] arg4) throws BusinessException {
    beforeCallMethod(905);
    Exception er = null;
    LwVO[] o = null;
    try {
      o = (LwVO[])_getBeanObject().xsddJrgc(arg0, arg1, arg2, arg3, arg4);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(905, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void clear_Xsdd(String arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(906);
    Exception er = null;
    try {
      _getBeanObject().clear_Xsdd(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(906, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public UpdateDataVO update_Xsdd(XsddVO[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(907);
    Exception er = null;
    UpdateDataVO o = null;
    try {
      o = _getBeanObject().update_Xsdd(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(907, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XsycVO[] queryAll_Xsyc(String arg0) throws BusinessException {
    beforeCallMethod(908);
    Exception er = null;
    XsycVO[] o = null;
    try {
      o = (XsycVO[])_getBeanObject().queryAll_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(908, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XsycVO findByPrimaryKey_Xsyc(String arg0) throws BusinessException {
    beforeCallMethod(909);
    Exception er = null;
    XsycVO o = null;
    try {
      o = _getBeanObject().findByPrimaryKey_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(909, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XsycVO[] queryXsycByConditionVO(XsycQueryConditionVO arg0) throws BusinessException {
    beforeCallMethod(910);
    Exception er = null;
    XsycVO[] o = null;
    try {
      o = (XsycVO[])_getBeanObject().queryXsycByConditionVO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(910, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void clear_Xsyc(XsycClearConditionVO arg0) throws BusinessException {
    beforeCallMethod(911);
    Exception er = null;
    try {
      _getBeanObject().clear_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(911, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Vector load_Xsyc(XsycLoadConditionVO arg0) throws BusinessException {
    beforeCallMethod(912);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().load_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(912, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] save_Xsyc(XsycVO[] arg0) throws BusinessException {
    beforeCallMethod(913);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().save_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(913, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Xsyc(XsycVO arg0) throws BusinessException {
    beforeCallMethod(914);
    Exception er = null;
    try {
      _getBeanObject().delete_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(914, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String insert_Xsyc(XsycVO arg0) throws BusinessException {
    beforeCallMethod(915);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().insert_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(915, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void update_Xsyc(XsycVO arg0) throws BusinessException {
    beforeCallMethod(916);
    Exception er = null;
    try {
      _getBeanObject().update_Xsyc(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(916, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public Hashtable circleCheck(String arg0) throws BusinessException {
    beforeCallMethod(917);
    Exception er = null;
    Hashtable o = null;
    try {
      o = _getBeanObject().circleCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(917, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XtbomVO findByWlID(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(918);
    Exception er = null;
    XtbomVO o = null;
    try {
      o = _getBeanObject().findByWlID(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(918, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Xtbom(XtbomVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(919);
    Exception er = null;
    try {
      _getBeanObject().delete_Xtbom(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(919, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList insert_Xtbom(XtbomVO arg0) throws BusinessException {
    beforeCallMethod(920);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Xtbom(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(920, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList update_Xtbom(XtbomVO arg0, String arg1) throws BusinessException {
    beforeCallMethod(921);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Xtbom(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(921, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Vector queryPublic_Xtfp() throws BusinessException {
    beforeCallMethod(922);
    Exception er = null;
    Vector o = null;
    try {
      o = _getBeanObject().queryPublic_Xtfp();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(922, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public XtfpVO[] findByWl_Xtfp(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(923);
    Exception er = null;
    XtfpVO[] o = null;
    try {
      o = (XtfpVO[])_getBeanObject().findByWl_Xtfp(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(923, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.InvbasdocVO queryInvbasdocByWlglid(String arg0) throws BusinessException {
    beforeCallMethod(924);
    Exception er = null;
    nc.vo.mm.pub.pub1025.InvbasdocVO o = null;
    try {
      o = _getBeanObject().queryInvbasdocByWlglid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(924, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1025.InvbasdocVO[] querySetupedMaterial(String arg0) throws BusinessException {
    beforeCallMethod(925);
    Exception er = null;
    nc.vo.mm.pub.pub1025.InvbasdocVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1025.InvbasdocVO[])_getBeanObject().querySetupedMaterial(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(925, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void delete_Xtfp(XtfpVO[] arg0) throws BusinessException {
    beforeCallMethod(926);
    Exception er = null;
    try {
      _getBeanObject().delete_Xtfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(926, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public ArrayList insert_Xtfp(XtfpVO[] arg0) throws BusinessException {
    beforeCallMethod(927);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().insert_Xtfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(927, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ArrayList update_Xtfp(XtfpVO[] arg0) throws BusinessException {
    beforeCallMethod(928);
    Exception er = null;
    ArrayList o = null;
    try {
      o = _getBeanObject().update_Xtfp(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(928, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public YhjhVO[] queryExistValidDataWl_Yhjh(String arg0, UFDate arg1, UFDate arg2) throws BusinessException {
    beforeCallMethod(929);
    Exception er = null;
    YhjhVO[] o = null;
    try {
      o = (YhjhVO[])_getBeanObject().queryExistValidDataWl_Yhjh(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(929, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public int loadYhjh(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(930);
    Exception er = null;
    int o = 0;
    try {
      o = _getBeanObject().loadYhjh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(930, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public YhjhVO[] queryByWl(String arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(931);
    Exception er = null;
    YhjhVO[] o = null;
    try {
      o = (YhjhVO[])_getBeanObject().queryByWl(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(931, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void clear_Yhjh(String arg0, String[] arg1) throws BusinessException {
    beforeCallMethod(932);
    Exception er = null;
    try {
      _getBeanObject().clear_Yhjh(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(932, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public YieldInfoVO[] queryYieldInfoByWhere(String arg0) throws BusinessException {
    beforeCallMethod(933);
    Exception er = null;
    YieldInfoVO[] o = null;
    try {
      o = (YieldInfoVO[])_getBeanObject().queryYieldInfoByWhere(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(933, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void deleteByMOArray(String[] arg0) throws BusinessException {
    beforeCallMethod(934);
    Exception er = null;
    try {
      _getBeanObject().deleteByMOArray(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(934, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String getYieldInfoBillTempletID(String arg0) throws BusinessException {
    beforeCallMethod(935);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().getYieldInfoBillTempletID(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(935, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public boolean[] getIscheck(String[] arg0, String arg1, String arg2) throws BusinessException {
    beforeCallMethod(936);
    Exception er = null;
    boolean[] o = null;
    try {
      o = (boolean[])_getBeanObject().getIscheck(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(936, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] getCcxhs(String arg0, int arg1) throws BusinessException {
    beforeCallMethod(937);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().getCcxhs(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(937, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public YieldInfoVO getMoRelatedInfo(String arg0) throws BusinessException {
    beforeCallMethod(938);
    Exception er = null;
    YieldInfoVO o = null;
    try {
      o = _getBeanObject().getMoRelatedInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(938, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WrVO[] buildElementCheckVO(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(939);
    Exception er = null;
    WrVO[] o = null;
    try {
      o = (WrVO[])_getBeanObject().buildElementCheckVO(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(939, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String isAlreadyCompCheck(String arg0) throws BusinessException {
    beforeCallMethod(940);
    Exception er = null;
    String o = null;
    try {
      o = _getBeanObject().isAlreadyCompCheck(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(940, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public void applyCheck(UFDateTime arg0, YieldInfoVO[] arg1, String arg2, String arg3, String arg4, String arg5) throws BusinessException {
    beforeCallMethod(941);
    Exception er = null;
    try {
      _getBeanObject().applyCheck(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(941, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
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

  public String[] save_YieldInfo(YieldInfoVO[] arg0) throws BusinessException {
    beforeCallMethod(942);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().save_YieldInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(942, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public DeptTotalVO[] queryDeptTotalVOS() throws BusinessException {
    beforeCallMethod(943);
    Exception er = null;
    DeptTotalVO[] o = null;
    try {
      o = (DeptTotalVO[])_getBeanObject().queryDeptTotalVOS();
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(943, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public WkTotalVO[] queryWkTotalVOS(String arg0) throws BusinessException {
    beforeCallMethod(944);
    Exception er = null;
    WkTotalVO[] o = null;
    try {
      o = (WkTotalVO[])_getBeanObject().queryWkTotalVOS(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(944, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] modify_Zlgz(ZlgzVO arg0, String[] arg1, String arg2) throws BusinessException {
    beforeCallMethod(945);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().modify_Zlgz(arg0, arg1, arg2);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(945, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public Integer delZlgz(String[] arg0, String arg1) throws BusinessException {
    beforeCallMethod(946);
    Exception er = null;
    Integer o = null;
    try {
      o = _getBeanObject().delZlgz(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(946, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZlgzItemVO[] findBodyForHead(nc.vo.mo.mo1034.QueryDataVO arg0) throws BusinessException {
    beforeCallMethod(947);
    Exception er = null;
    ZlgzItemVO[] o = null;
    try {
      o = (ZlgzItemVO[])_getBeanObject().findBodyForHead(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(947, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZlgzItemVO[] findByLyid(String arg0, Integer arg1) throws BusinessException {
    beforeCallMethod(948);
    Exception er = null;
    ZlgzItemVO[] o = null;
    try {
      o = (ZlgzItemVO[])_getBeanObject().findByLyid(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(948, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZlgzHeadVO[] findZlgzHead(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(949);
    Exception er = null;
    ZlgzHeadVO[] o = null;
    try {
      o = (ZlgzHeadVO[])_getBeanObject().findZlgzHead(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(949, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public String[] insert_Zlgz(ZlgzVO arg0) throws BusinessException {
    beforeCallMethod(950);
    Exception er = null;
    String[] o = null;
    try {
      o = (String[])_getBeanObject().insert_Zlgz(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(950, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public AbomVOComposite buildOrder(AbomVOComposite arg0, String arg1) throws BusinessException {
    beforeCallMethod(951);
    Exception er = null;
    AbomVOComposite o = null;
    try {
      o = _getBeanObject().buildOrder(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(951, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.PickmVO buildPickmForMO(nc.vo.mm.pub.pub1030.MoVO arg0) throws BusinessException {
    beforeCallMethod(952);
    Exception er = null;
    nc.vo.mm.pub.pub1030.PickmVO o = null;
    try {
      o = _getBeanObject().buildPickmForMO(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(952, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public OrderBaseInfoVO queryVirtualInfo(OrderBaseInfoVO arg0) throws BusinessException {
    beforeCallMethod(953);
    Exception er = null;
    OrderBaseInfoVO o = null;
    try {
      o = _getBeanObject().queryVirtualInfo(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(953, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public OrderState checkOrderState(OrderBaseInfoVO arg0) throws BusinessException {
    beforeCallMethod(954);
    Exception er = null;
    OrderState o = null;
    try {
      o = _getBeanObject().checkOrderState(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(954, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public AbomVOComposite queryBuiltOrder(OrderBaseInfoVO arg0) throws BusinessException {
    beforeCallMethod(955);
    Exception er = null;
    AbomVOComposite o = null;
    try {
      o = _getBeanObject().queryBuiltOrder(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(955, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.MoVO queryMo(String arg0, String arg1) throws BusinessException {
    beforeCallMethod(956);
    Exception er = null;
    nc.vo.mm.pub.pub1030.MoVO o = null;
    try {
      o = _getBeanObject().queryMo(arg0, arg1);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(956, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public nc.vo.mm.pub.pub1030.MoVO[] queryMos(OrderBaseInfoVO arg0) throws BusinessException {
    beforeCallMethod(957);
    Exception er = null;
    nc.vo.mm.pub.pub1030.MoVO[] o = null;
    try {
      o = (nc.vo.mm.pub.pub1030.MoVO[])_getBeanObject().queryMos(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(957, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public AbomVOComposite queryRawOrder(OrderBaseInfoVO arg0) throws BusinessException {
    beforeCallMethod(958);
    Exception er = null;
    AbomVOComposite o = null;
    try {
      o = _getBeanObject().queryRawOrder(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(958, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public OrderBaseInfoVO[] querySaleOrders(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(959);
    Exception er = null;
    OrderBaseInfoVO[] o = null;
    try {
      o = (OrderBaseInfoVO[])_getBeanObject().querySaleOrders(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(959, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZzVO[] getZzVOs(ConditionVO[] arg0) throws BusinessException {
    beforeCallMethod(960);
    Exception er = null;
    ZzVO[] o = null;
    try {
      o = (ZzVO[])_getBeanObject().getZzVOs(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(960, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public UFDouble getTrlByMOid(String arg0) throws BusinessException {
    beforeCallMethod(961);
    Exception er = null;
    UFDouble o = null;
    try {
      o = _getBeanObject().getTrlByMOid(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(961, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
        throw ((BusinessException)er);
      }

      if ((er instanceof RuntimeException)) {
        throw ((RuntimeException)er);
      }
      throw new FrameworkEJBException(er.getMessage(), er);
    }
    return o;
  }
  public ZzcxVO[] queryProduceRate(String arg0) throws BusinessException {
    beforeCallMethod(962);
    Exception er = null;
    ZzcxVO[] o = null;
    try {
      o = (ZzcxVO[])_getBeanObject().queryProduceRate(arg0);
    }
    catch (Exception e) {
      er = e;
    } catch (Throwable thr) {
      Logger.error("HGY:Unexpected error, tx will rb", thr);
      er = new FrameworkEJBException("Fatal unknown error", thr);
    }
    try {
      afterCallMethod(962, er);
    }
    catch (RemoteException remoteException) {
      Logger.error("HGY: Unexpected error when call afterCallMethod", remoteException);
    } finally {
    }
    if (null != er) {
      if ((er instanceof BusinessException)) {
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