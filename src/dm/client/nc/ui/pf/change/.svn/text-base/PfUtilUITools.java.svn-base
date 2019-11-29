package nc.ui.pf.change;

import java.util.Date;
import nc.bs.logging.Logger;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.IUINodecodeSearcher;
import nc.vo.bd.CorpVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.PFBusinessException;

public class PfUtilUITools extends PfUtilBaseTools
{
  private static AggregatedValueObject pfInitVoClass(String billType, int iChildLength)
    throws BusinessException
  {
    String[] billInfo = PfUIDataCache.getStrBillVo(billType);
    String strBillVoClassName = billInfo[0];
    String strHeadVoClassName = billInfo[1];
    String strItemVoClassName = billInfo[2];

    AggregatedValueObject retObj = pfInitVo(strBillVoClassName, strHeadVoClassName, strItemVoClassName, iChildLength);

    return retObj;
  }

  private static AggregatedValueObject[] pfInitVosClass(String billType, AggregatedValueObject[] sourceBillVOs)
    throws BusinessException
  {
    String[] billInfo = PfUIDataCache.getStrBillVo(billType);
    String strBillVoClassName = billInfo[0];

    AggregatedValueObject[] retVos = null;

    retVos = pfInitVos(strBillVoClassName, sourceBillVOs.length);

    for (int i = 0; i < sourceBillVOs.length; i++) {
      retVos[i] = pfInitVoClass(billType, sourceBillVOs[i].getChildrenVO().length);
    }
    return retVos;
  }

  public static AggregatedValueObject runChangeData(String sourceBillType, String destBillType, AggregatedValueObject sourceBillVO)
    throws BusinessException
  {
    if (sourceBillVO == null) {
      return null;
    }
    Logger.debug(">>开始单据VO交换=" + sourceBillType + "到" + destBillType);

    AggregatedValueObject destBillVO = null;

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;

    String uiChgClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;
    Object changeImpl = null;
    try
    {
      changeImpl = findUIChangeScriptClass(sourceBillType, destBillType);
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFBusinessException("前台VO交换失败，找不到交换类=" + uiChgClassName);
    }

    if ((changeImpl instanceof VOConversionUI))
    {
      VOConversionUI tmpVar = (VOConversionUI)changeImpl;

      initConversionEnv(tmpVar);
    }

    adjustBeforeVoMapping(destBillType, new AggregatedValueObject[] { sourceBillVO });

    AggregatedValueObject destVo = pfInitVoClass(destBillType, sourceBillVO.getChildrenVO().length);

    destBillVO = ((IchangeVO)changeImpl).retChangeBusiVO(sourceBillVO, destVo);

    adjustAfterVoMapping(sourceBillType, destBillType, new AggregatedValueObject[] { sourceBillVO }, new AggregatedValueObject[] { destBillVO });

    Logger.debug(">>结束单据VO交换=" + sourceBillType + "到" + destBillType);

    return destBillVO;
  }

  private static Object findUIChangeScriptClass(String sourceBillType, String destBillType)
    throws Exception
  {
    Logger.debug("查询交换类开始......");

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;

    String fullyQualifiedClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;

    BilltypeVO srcBilltypeVO = null;
    BilltypeVO destBilltypeVO = null;
    String srcParentBilltype = null;
    String destParentBilltype = null;

    Object changeImpl = null;
    try {
      Logger.debug("1.尝试实例化类=" + fullyQualifiedClassName);
      Class c = Class.forName(fullyQualifiedClassName);
      changeImpl = c.newInstance();
    } catch (Exception e1) {
      Logger.debug("尝试查找目的单据的父单据类型");

      destBilltypeVO = PfUIDataCache.getBillType(destBillType);
      if (destBilltypeVO.getBillstyle() != null) {
        Logger.debug("找到目的单据的父单据类型");
        destParentBilltype = PfUIDataCache.getBillTypeByStyle(destBilltypeVO.getBillstyle().toString());

        strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destParentBilltype;
        fullyQualifiedClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;
        try {
          Logger.debug("2.尝试实例化类=" + fullyQualifiedClassName);
          Class c = Class.forName(fullyQualifiedClassName);
          changeImpl = c.newInstance();
        } catch (Exception e2) {
          Logger.debug("尝试查找源单据的父单据类型");

          srcBilltypeVO = PfUIDataCache.getBillType(sourceBillType);
          if (srcBilltypeVO.getBillstyle() != null) {
            Logger.debug("找到源单据的父单据类型");
            srcParentBilltype = PfUIDataCache.getBillTypeByStyle(srcBilltypeVO.getBillstyle().toString());

            strClassNameNoPackage = "CHG" + srcParentBilltype + "TO" + destBillType;
            fullyQualifiedClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;
            try {
              Logger.debug("3.尝试实例化类=" + fullyQualifiedClassName);
              Class c = Class.forName(fullyQualifiedClassName);
              changeImpl = c.newInstance();
            } catch (Exception e3) {
              strClassNameNoPackage = "CHG" + srcParentBilltype + "TO" + destParentBilltype;
              fullyQualifiedClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;
              Logger.debug("4.尝试实例化类=" + fullyQualifiedClassName);
              Class c = Class.forName(fullyQualifiedClassName);
              changeImpl = c.newInstance();
            }
          } else {
            throw new PFBusinessException("源单据无单据大类，无法获取其父单据类型");
          }
        }
      } else {
        Logger.debug("目的单据无单据大类，尝试查找源单据的父单据类型");

        srcBilltypeVO = PfUIDataCache.getBillType(sourceBillType);
        if (srcBilltypeVO.getBillstyle() != null) {
          srcParentBilltype = PfUIDataCache.getBillTypeByStyle(srcBilltypeVO.getBillstyle().toString());

          strClassNameNoPackage = "CHG" + srcParentBilltype + "TO" + destBillType;
          fullyQualifiedClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;

          Logger.debug("5.尝试实例化类=" + fullyQualifiedClassName);
          Class c = Class.forName(fullyQualifiedClassName);
          changeImpl = c.newInstance();
        } else {
          throw new PFBusinessException("错误：源单据、目的单据都无单据大类");
        }
      }
    }
    Logger.debug("查询交换类结束......");
    return changeImpl;
  }

  public static AggregatedValueObject[] runChangeDataAry(String sourceBillType, String destBillType, AggregatedValueObject[] sourceBillVOs)
    throws BusinessException
  {
    if ((sourceBillVOs == null) || (sourceBillVOs.length == 0)) {
      return null;
    }
    Logger.debug(">>开始单据VO批量交换=" + sourceBillType + "到" + destBillType);

    AggregatedValueObject[] destBillVOs = null;

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;

    String uiChgClassName = "nc.ui.pf.changedir." + strClassNameNoPackage;
    Object uiChgObj = null;
    try
    {
      uiChgObj = findUIChangeScriptClass(sourceBillType, destBillType);
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFBusinessException("前台VO交换失败，找不到交换类=" + uiChgClassName);
    }

    if ((uiChgObj instanceof VOConversionUI)) {
      VOConversionUI tmpVar = (VOConversionUI)uiChgObj;
      initConversionEnv(tmpVar);
    }

    adjustBeforeVoMapping(destBillType, sourceBillVOs);

    AggregatedValueObject[] destVo = pfInitVosClass(destBillType, sourceBillVOs);

    destBillVOs = ((IchangeVO)uiChgObj).retChangeBusiVOs(sourceBillVOs, destVo);

    adjustAfterVoMapping(sourceBillType, destBillType, sourceBillVOs, destBillVOs);

    Logger.debug(">>结束单据VO批量交换=" + sourceBillType + "到" + destBillType);
    return destBillVOs;
  }

  private static void initConversionEnv(VOConversionUI tmpVar)
  {
    tmpVar.setSysDate(ClientEnvironment.getInstance().getDate().toString());

    tmpVar.setSysOperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());

    tmpVar.setSysAccountYear(ClientEnvironment.getInstance().getAccountYear());

    tmpVar.setSysCorp(ClientEnvironment.getInstance().getCorporation().getPk_corp());

    tmpVar.setSysTime(new UFDateTime(new Date()).toString());
  }

  public static String findCustomNodeOfBilltype(BilltypeVO btVO, ILinkQueryData lqd)
  {
    Logger.debug("::查找自定义节点 findCustomNodeOfBilltype=" + btVO.getPrimaryKey());

    String strClassName = btVO.getDef3();
    Logger.debug("::bd_billtype.def3=" + strClassName);
    if (StringUtil.isEmptyWithTrim(strClassName)) {
      return null;
    }
    Object obj = null;
    try {
      Class c = Class.forName(strClassName);
      obj = c.newInstance();
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    if ((obj instanceof IUINodecodeSearcher)) {
      Logger.debug("::bd_billtype.def3实现了接口IUINodecodeSearcher");
      return ((IUINodecodeSearcher)obj).findNodecode(lqd);
    }
    return null;
  }

  public static String findNodecodeOfBilltype(String pkBilltype, ILinkQueryData lqd)
  {
    BilltypeVO btVO = PfUIDataCache.getBillType(pkBilltype);
    if (btVO == null) {
      return null;
    }

    String strClassName = btVO.getDef3();
    if (strClassName == null) {
      return btVO.getNodecode();
    }

    Object obj = null;
    try {
      Class c = Class.forName(strClassName);
      obj = c.newInstance();
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    if ((obj instanceof IUINodecodeSearcher)) {
      return ((IUINodecodeSearcher)obj).findNodecode(lqd);
    }
    return btVO.getNodecode();
  }
}