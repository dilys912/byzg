package nc.itf.arap.prv;

import java.util.ArrayList;
import java.util.Vector;
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

public abstract interface IArapVerifyLogPrivate
{
  public abstract VerifyVO[] onFilterVerifyVO(ConditionVO paramConditionVO)
    throws BusinessException;

  public abstract Object[] onFilterVerifyLog(ConditionVO paramConditionVO, boolean paramBoolean)
    throws BusinessException;

  public abstract String[] unSaveVerifyLog(String[] paramArrayOfString)
    throws BusinessException;

  public abstract DJZBVO saveIntimeVerify(DJZBVO paramDJZBVO1, Vector paramVector1, Vector paramVector2, String paramString, UFDate paramUFDate, DJZBVO paramDJZBVO2, DefaultVerifyRuleVO[] paramArrayOfDefaultVerifyRuleVO, UFDouble paramUFDouble, int paramInt)
    throws BusinessException;

  public abstract String save(VerifyLogVO[] paramArrayOfVerifyLogVO, ArrayList paramArrayList)
    throws BusinessException;

  public abstract RemoteTransferVO getVerifyDetailRecBySum(DJCLBVO paramDJCLBVO)
    throws BusinessException;

  public abstract RemoteTransferVO onVITSelectData(VITFilterCondVO paramVITFilterCondVO)
    throws BusinessException;

  public abstract String[] onCancelVerify_RequiresNew(Vector paramVector)
    throws BusinessException;

  public abstract String[] getBillsByPJ(String paramString)
    throws BusinessException;

  public abstract MethodVO[] getMethods(int paramInt)
    throws BusinessException;

  public abstract MethodVO[] getAllMethods()
    throws BusinessException;

  public abstract DisplayVO[] autoVerify(ConditionVO paramConditionVO, DefaultVerifyRuleVO[] paramArrayOfDefaultVerifyRuleVO)
    throws BusinessException;

  public abstract VerifyLogVO[] autoVerifySim(ConditionVO paramConditionVO, DefaultVerifyRuleVO[] paramArrayOfDefaultVerifyRuleVO)
    throws BusinessException;
}