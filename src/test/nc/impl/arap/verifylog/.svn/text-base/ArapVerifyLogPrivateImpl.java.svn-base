package nc.impl.arap.verifylog;

import java.util.ArrayList;
import java.util.Vector;
import nc.itf.arap.prv.IArapVerifyLogPrivate;
import nc.vo.arap.transaction.RemoteTransferVO;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.arap.verify.VITFilterCondVO;
import nc.vo.arap.verifynew.DisplayVO;
import nc.vo.arap.verifynew.MethodVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.verifynew.pub.ConditionVO;
import nc.vo.verifynew.pub.DefaultVerifyRuleVO;
import nc.vo.verifynew.pub.VerifyLogVO;
import nc.vo.verifynew.pub.VerifyVO;

public class ArapVerifyLogPrivateImpl
  implements IArapVerifyLogPrivate
{
  public RemoteTransferVO getVerifyDetailRecBySum(DJCLBVO vo)
    throws BusinessException
  {
    return new nc.bs.arap.verify.VerifyBO().getVerifyDetailRecBySum(vo);
  }

  public RemoteTransferVO onVITSelectData(VITFilterCondVO vo)
    throws BusinessException
  {
    return new nc.bs.arap.verify.VerifyBO().onVITSelectData(vo);
  }

  public VerifyVO[] onFilterVerifyVO(ConditionVO condition)
    throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().onFilterData(condition);
  }

  public Object[] onFilterDj(ConditionVO condition)
    throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().onFilterDj(condition);
  }

  public Object[] onFilterVerifyLog(ConditionVO conditoin, boolean isSum)
    throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().onFilterLog(conditoin, isSum);
  }

  public String[] unSaveVerifyLog(String[] hepch)
    throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().unSave(hepch);
  }

  public DJZBVO saveIntimeVerify(DJZBVO bcdj, Vector djpk, Vector jsje, String pk_corp, UFDate jsrq, DJZBVO selectedVO, DefaultVerifyRuleVO[] rules, UFDouble je, int pzglh) throws BusinessException {
    return new nc.bs.arap.verifynew.VerifyBO().saveIntimeVerify(bcdj, djpk, jsje, pk_corp, jsrq, selectedVO, rules, je, pzglh);
  }

  public String save(VerifyLogVO[] vos, ArrayList jsvo)
    throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().save(vos);
  }

  public String[] onCancelVerify_RequiresNew(Vector dJCLv)
    throws BusinessException
  {
    return new nc.bs.arap.verify.VerifyBO().onCancelVerify(dJCLv);
  }

  public String[] getBillsByPJ(String pk_pj) throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().getBillsByPJ(pk_pj);
  }

  public MethodVO[] getMethods(int lc) throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().getMethods(lc);
  }

  public MethodVO[] getAllMethods() throws BusinessException
  {
    return new nc.bs.arap.verifynew.VerifyBO().getAllMethods();
  }

  public DisplayVO[] autoVerify(ConditionVO condition, DefaultVerifyRuleVO[] rules) throws BusinessException {
    return new nc.bs.arap.verifynew.VerifyBO().autoVerify(condition, rules);
  }

  public VerifyLogVO[] autoVerifySim(ConditionVO condition, DefaultVerifyRuleVO[] rules) throws BusinessException {
    return new nc.bs.arap.verifynew.VerifyBO().autoVerifySim(condition, rules);
  }
}