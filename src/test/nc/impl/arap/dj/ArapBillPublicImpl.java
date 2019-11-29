package nc.impl.arap.dj;

import java.util.ArrayList;
import java.util.List;
import nc.bs.arap.change.PubchangeBO;
import nc.bs.arap.outer.ArapCheckInterfaceBO;
import nc.bs.dao.DAOException;
import nc.bs.ep.dj.ARAPDjAlienBill;
import nc.bs.ep.dj.ARAP_FTS_PaymentImpl;
import nc.bs.ep.dj.ApplayBillBO;
import nc.bs.ep.dj.ApplayBillDMO;
import nc.bs.ep.dj.DJZBDAO;
import nc.bs.logging.Log;
import nc.itf.arap.pub.IArapBillPublic;
import nc.jdbc.framework.exception.DbException;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.verifynew.BusinessShowException;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.IFTSReceiverVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.sourcebill.LightBillVO;

public class ArapBillPublicImpl
  implements IArapBillPublic
{
  nc.bs.ep.dj.DJZBBO djzbbo = new nc.bs.ep.dj.DJZBBO();
  ApplayBillBO applyBo = new ApplayBillBO();
  ARAPDjAlienBill alienBill = new ARAPDjAlienBill();
  ARAP_FTS_PaymentImpl ftsPay = new ARAP_FTS_PaymentImpl();
  PubchangeBO changeBo = new PubchangeBO();

  public ResMessage accountArapBillContr(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.accountContr(djzbvo);
  }

  public ResMessage auditArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.auditABill(djzbvo);
  }

  public ResMessage auditOneArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.alienBill.auditOneBill(djzbvo);
  }

  public void backGoing(AggregatedValueObject vo, String approveId, String approveDate, String backNote)
    throws BusinessException
  {
    try
    {
      new ApplayBillDMO().backGoing(vo, approveId, approveDate, backNote);
    } catch (Exception e) {
      throw new BusinessShowException("ArapBillPublicImpl::backGoing(dJZB) Exception!", new Exception(e));
    }
  }

  public void backNoState(AggregatedValueObject vo, String approveId, String approveDate, String backNote)
    throws BusinessException
  {
    try
    {
      new ApplayBillDMO().backNoState(vo, approveId, approveDate, backNote);
    } catch (Exception e) {
      throw new BusinessShowException("ArapBillPublicImpl::backNoState(dJZB) Exception!", new Exception(e));
    }
  }

  public void changeARAPBillStatus(String strBillPK)
    throws BusinessException
  {
    this.ftsPay.changeARAPBillStatus(strBillPK);
  }

  public void changeARAPBillStatusUnVerify(String strBillPK, UFDate rq, String strOperatorPK)
    throws BusinessException
  {
    this.ftsPay.changeARAPBillStatusUnVerify(strBillPK, rq, strOperatorPK);
  }

  public void changeARAPBillStatusVerify(String strBillPK, UFDate rq, String strOperatorPK, Boolean bSucceed)
    throws BusinessException
  {
    this.ftsPay.changeARAPBillStatusVerify(strBillPK, rq, strOperatorPK, bSucceed);
  }

  public boolean checkGoing(AggregatedValueObject vo, String ApproveId, String ApproveDate, String checkNote)
    throws BusinessException
  {
    try
    {
      return new ApplayBillDMO().checkGoing(vo, ApproveId, ApproveDate, checkNote); } catch (Exception e) {
    	  throw new BusinessShowException("ArapBillPublicImpl::checkGoing(dJZB) Exception!", new Exception(e));
    }
    
  }

  public boolean checkNoPass(AggregatedValueObject vo, String ApproveId, String ApproveDate, String checkNote)
    throws BusinessException
  {
    try
    {
      return new ApplayBillDMO().checkNoPass(vo, ApproveId, ApproveDate, checkNote); } catch (Exception e) {
    	  throw new BusinessShowException("ArapBillPublicImpl::checkNoPass(dJZB) Exception!", new Exception(e));
    }
    
  }

  public boolean checkPass(AggregatedValueObject vo, String ApproveId, String ApproveDate, String checkNote)
    throws BusinessException
  {
    try
    {
      return new ApplayBillDMO().checkPass(vo, ApproveId, ApproveDate, checkNote); } catch (Exception e) {
    	  throw new BusinessShowException("ArapBillPublicImpl::checkPass(dJZB) Exception!", new Exception(e));
    }
   
  }

  public DJZBVO deleteArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.djzbbo.deleteDj(djzbvo);
  }

  public void deleteOutArapBillByPk(String djpk)
    throws BusinessException
  {
    this.djzbbo.deleteOutBill(djpk);
  }

  public void deleteOutBillandfb(String djpk, String fboid)
    throws BusinessException
  {
    this.djzbbo.deleteOutBillandfb(djpk, fboid);
  }

  public void deleteOutBillbyWhere(String whereString)
    throws BusinessException
  {
    this.djzbbo.deleteOutBillbyWhere(whereString);
  }

  public DJZBVO editArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.djzbbo.editDj(djzbvo);
  }

  public DJZBVO findArapBillByPK(String djpk)
    throws BusinessException
  {
    return this.djzbbo.findByPrimaryKey(djpk);
  }

  public DJZBVO[] findArapBillByPKs(String[] djpks)
    throws BusinessException
  {
    return this.djzbbo.findByPrimaryKeys(djpks);
  }

  public DJZBVO findArapBillByPKSS(String djpk)
    throws BusinessException
  {
    return this.djzbbo.findByPrimaryKey_SS(djpk);
  }

  public DJZBVO findArapBillByPKTB(String djpk)
    throws BusinessException
  {
    return new nc.bs.ep.dj_tb.DJZBBO().findByPrimaryKey(djpk);
  }

  public UFDate getSysNoSettleFirstDate(String pk_corp, String syscode)
    throws BusinessException
  {
    return this.changeBo.getSysNoSettleFirstDate(pk_corp, syscode);
  }

  public ResMessage isArapBillDistributes(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.isDistributes(djzbvo);
  }

  public void returnArapBillCode(DJZBVO djzbvo)
    throws BusinessException
  {
    this.djzbbo.returnBillCode(djzbvo);
  }

  public DJZBVO saveArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return (DJZBVO)this.djzbbo.save(djzbvo).get(1);
  }

  public void sendArapBillDelMsg(DJZBVO djzbvo)
    throws BusinessException
  {
    this.applyBo.sendMessage_del(djzbvo, UFBoolean.TRUE);
  }

  public void sendArapBillMsg(DJZBVO djzbvo)
    throws BusinessException
  {
    this.applyBo.sendMessage(djzbvo);
  }

  public ResMessage unAuditArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.unAuditABill(djzbvo);
  }

  public ResMessage unAuditOneArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.alienBill.unAuditOneBill(djzbvo);
  }

  public ResMessage unYhqrArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.unYhqr(djzbvo);
  }

  public ResMessage yhqrArapBill(DJZBVO djzbvo)
    throws BusinessException
  {
    return this.applyBo.yhqr(djzbvo);
  }

  public void deleteOutArapBillByPks(String[] ids)
    throws BusinessException
  {
    this.djzbbo.deleteOutBill(ids, null);
  }

  public DJZBHeaderVO[] queryHead(String key)
    throws BusinessException
  {
    return this.djzbbo.queryHead(key);
  }

  public List saveArapBillForSettle(DJZBVO[] djzbvos)
    throws BusinessException
  {
    return this.djzbbo.saveAPBillForSettle(djzbvos);
  }

  public DJZBVO[] findItemByHead(DJZBHeaderVO[] heads)
    throws BusinessException
  {
    return this.djzbbo.findItem_ByHead(heads);
  }

  public void updateArapBillByFTSReceiver(IFTSReceiverVO vo)
    throws BusinessException
  {
    this.djzbbo.updateArapBillByFTSReceiver(vo);
  }

  public boolean canChangeFTSReceiver(String billpk)
    throws BusinessException
  {
    return canChangeFTSReceiver(billpk);
  }

  public boolean isSysInuse(String pk_corp, String system, UFDate billDate)
    throws BusinessException
  {
    ArapCheckInterfaceBO bo = new ArapCheckInterfaceBO();
    return bo.isSysInuse(pk_corp, system, billDate);
  }

  public DJZBVO[] getDjvobySourcePk(String sOuterPk)
    throws Exception
  {
    ARAPDjAlienBill bo = new ARAPDjAlienBill();
    return bo.getDjvobySourcePk(sOuterPk);
  }

  public DJZBVO[] getDjvobySourceHh(String sOuterPk) throws Exception {
    ARAPDjAlienBill bo = new ARAPDjAlienBill();
    return bo.getDjvobySourceHh(sOuterPk);
  }

  public void setOtherSysFlag(String fb_oid, String othersysflag, UFBoolean pausetransact) throws BusinessException {
    try {
      new DJZBDAO().setOtherSysFlag(fb_oid, othersysflag, pausetransact);
    } catch (DAOException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    } catch (DbException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
  }

  public LightBillVO[] getForwardBills(String curBillType, String curBillID, String forwardBillType) {
    return new nc.bs.ep.dj.DJZBBO().getForwardBills(curBillType, curBillID, forwardBillType);
  }

  public DJZBVO updateArapBill(DJZBVO djzbvo) throws BusinessException {
    return this.djzbbo.updateDj(djzbvo);
  }

  public String[] getDjpksbySourcePk(String sOuterPk) throws BusinessException {
    ARAPDjAlienBill bo = new ARAPDjAlienBill();
    return bo.getDjpksbySourcePk(sOuterPk);
  }
}