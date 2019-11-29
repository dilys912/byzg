package nc.bs.pub.pf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.framework.cluster.itf.BytesClusterMessage;
import nc.bs.framework.cluster.itf.ClusterMessageHeader;
import nc.bs.framework.cluster.itf.ClusterSender;
import nc.bs.framework.cluster.itf.ClusterServiceException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.pf.change.VOConversion;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.psndoc.PsnbasdocVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pfflow.PfDataTypes;
import nc.vo.pub.pfflow.PfOperatorTypes;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.wfengine.core.util.DateUtilities;

public class PfUtilTools extends PfUtilBaseTools
{
  public static Object instantizeObject(String pkBilltype, String checkClsName)
    throws BusinessException
  {
    IPFMetaModel pfmeta = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());

    String module = pfmeta.queryModuleOfBilltype(pkBilltype);
    return NewObjectService.newInstance(module, checkClsName);
  }

  public static String[] fetchPhonesByUserId(String[] userIds)
    throws BusinessException
  {
    if ((userIds == null) || (userIds.length == 0))
      return null;
    ArrayList alPhones = new ArrayList();
    try
    {
      IUserManageQuery umq = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());

      for (int i = 0; i < userIds.length; i++) {
        PsnbasdocVO psnVO = umq.getPsnbasdocByUserid(userIds[i]);
        if (psnVO != null) {
          String phone = psnVO.getMobile();
          if ((phone != null) && (phone.length() > 0))
            alPhones.add(phone);
        }
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return (String[])(String[])alPhones.toArray(new String[0]);
  }

  public static Object getBizRuleImpl(String billType)
  {
    Object obj = null;
    try {
      BilltypeVO billVo = PfDataCache.getBillTypeInfo(billType);
      String checkClsName = billVo.getCheckclassname();
      if ((checkClsName != null) && (checkClsName.trim().length() != 0)) {
        obj = instantizeObject(billVo.getPrimaryKey(), checkClsName.trim());
        Logger.debug(">>获得单据类型=" + billType + "的业务规则实现=" + obj);
      }
    }
    catch (Exception ex) {
      Logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

  public static Object getParticipantFilterImpl(String billType, int iClasstype)
  {
    Object obj = null;
    try {
      Billtype2VO bt2VO = null;
      ArrayList alBilltype2VO = PfDataCache.getBillType2Info(billType, iClasstype);
      Iterator iter;
      if (alBilltype2VO.size() == 1)
        bt2VO = (Billtype2VO)alBilltype2VO.get(0);
      else {
        for (iter = alBilltype2VO.iterator(); iter.hasNext(); ) {
          Billtype2VO tempVO = (Billtype2VO)iter.next();
          if (tempVO.getPk_billtype().equals(billType)) {
            bt2VO = tempVO;
          }
        }
      }
      if (bt2VO != null) {
        String checkClsName = bt2VO.getClassname();
        if ((checkClsName != null) && (checkClsName.trim().length() != 0)) {
          if (bt2VO.getPk_billtype().equals("XX"))
            obj = NewObjectService.newInstance("uap", checkClsName);
          else
            obj = instantizeObject(bt2VO.getPk_billtype(), checkClsName.trim());
        }
        Logger.debug(">>获得单据类型=" + billType + "的业务规则类=" + checkClsName + ";实例=" + obj);
      }
      else {
        obj = NewObjectService.newInstance("uap", "nc.impl.uap.pf.ParticipantFilteredBySuperiorImpl");
      }
    }
    catch (Exception ex)
    {
      Logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

  private static Object findChangeScriptClass(String sourceBillType, String destBillType)
    throws Exception
  {
    Logger.debug("查询交换类开始......");

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;

    String fullyQualifiedClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;

    BilltypeVO srcBilltypeVO = null;
    BilltypeVO destBilltypeVO = null;
    String srcParentBilltype = null;
    String destParentBilltype = null;

    Object changeImpl = null;
    try {
      changeImpl = newVoMappingObject(sourceBillType, destBillType, fullyQualifiedClassName);
    }
    catch (Exception e1) {
      destBilltypeVO = PfDataCache.getBillTypeInfo(destBillType);
      if (destBilltypeVO.getBillstyle() != null) {
        destParentBilltype = PfDataCache.getBillTypeByStyle(destBilltypeVO.getBillstyle().toString());

        strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destParentBilltype;
        fullyQualifiedClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;
        try {
          changeImpl = newVoMappingObject(sourceBillType, destParentBilltype, fullyQualifiedClassName);
        }
        catch (Exception e2)
        {
          srcBilltypeVO = PfDataCache.getBillTypeInfo(sourceBillType);
          if (srcBilltypeVO.getBillstyle() != null) {
            srcParentBilltype = PfDataCache.getBillTypeByStyle(srcBilltypeVO.getBillstyle().toString());

            strClassNameNoPackage = "CHG" + srcParentBilltype + "TO" + destBillType;
            fullyQualifiedClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;
            try {
              changeImpl = newVoMappingObject(srcParentBilltype, destBillType, fullyQualifiedClassName);
            }
            catch (Exception e3) {
              strClassNameNoPackage = "CHG" + srcParentBilltype + "TO" + destParentBilltype;
              fullyQualifiedClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;
              changeImpl = newVoMappingObject(srcParentBilltype, destParentBilltype, fullyQualifiedClassName);
            }
          }
          else {
            throw new PFBusinessException("源单据无单据大类，无法获取其父单据类型");
          }
        }
      } else {
        Logger.debug("目的单据无单据大类，尝试查找源单据的父单据类型");

        srcBilltypeVO = PfDataCache.getBillTypeInfo(sourceBillType);
        if (srcBilltypeVO.getBillstyle() != null) {
          srcParentBilltype = PfDataCache.getBillTypeByStyle(srcBilltypeVO.getBillstyle().toString());

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

  public static Class getClassNameClass(String className, int intLen)
    throws BusinessException
  {
    AggregatedValueObject[] retVos = pfInitVos(className, intLen);
    return retVos.getClass();
  }

  public static Class getClassByName(String className)
    throws BusinessException
  {
    try
    {
      return Class.forName(className); } catch (ClassNotFoundException e) {
    }
      catch (Exception e){
    throw new PFBusinessException(e.getMessage(), e);
      }
	return null;
  }

  public static String getCurrentDatabase()
  {
    String userDataSource = InvocationInfoProxy.getInstance().getUserDataSource();
    if (userDataSource == null)
      userDataSource = "design";
    return userDataSource;
  }

  private static String getExpressValue(String macro, String type, Object leftValue, Object rightValue)
  {
    if (macro.equalsIgnoreCase("L"))
      return leftValue.toString();
    if (macro.equalsIgnoreCase("R"))
      return rightValue.toString();
    if (macro.equalsIgnoreCase("L-R")) {
      if (type.equalsIgnoreCase("INTEGER")) {
        return new Integer(((Integer)leftValue).intValue() - ((Integer)rightValue).intValue()).toString();
      }
      if (type.equalsIgnoreCase("Double")) {
        double value = ((UFDouble)leftValue).doubleValue() - Double.parseDouble((String)rightValue);

        return String.valueOf(value);
      }
    } else if (macro.equalsIgnoreCase("R-L")) {
      if (type.equalsIgnoreCase("INTEGER")) {
        return new Integer(((Integer)rightValue).intValue() - ((Integer)leftValue).intValue()).toString();
      }
      if (type.equalsIgnoreCase("Double")) {
        double value = ((UFDouble)rightValue).doubleValue() - Double.parseDouble((String)leftValue);

        return String.valueOf(value);
      }
    } else if ((macro.equalsIgnoreCase("L+R")) || (macro.equalsIgnoreCase("R+L"))) {
      if (type.equalsIgnoreCase("INTEGER")) {
        return new Integer(((Integer)leftValue).intValue() + ((Integer)rightValue).intValue()).toString();
      }
      if (type.equalsIgnoreCase("Double")) {
        double value = ((UFDouble)leftValue).doubleValue() + Double.parseDouble((String)rightValue);

        return String.valueOf(value);
      }
    }
    return macro;
  }

  public static boolean isTrueOrNot(Object InObject, String objType, String opType, String value)
    throws BusinessException
  {
    Logger.debug("****比较类型:" + objType + "比较类型运算符:" + opType + "****");

    if (PfDataTypes.UFBOOLEAN.toString().equals(objType))
    {
      UFBoolean typeBoolean = (UFBoolean)InObject;

      if ((PfOperatorTypes.EQ.toString().equals(opType)) || ("=".equals(opType)))
      {
        return typeBoolean.equals(UFBoolean.valueOf(value));
      }

      return typeBoolean.booleanValue();
    }if (PfDataTypes.STRING.toString().equals(objType))
    {
      if (InObject == null) return false;
      String typeString = (String)InObject;

      if ((PfOperatorTypes.EQ.toString().equals(opType)) || ("=".equals(opType)))
      {
        return typeString.equals(value);
      }

      if (PfOperatorTypes.LIKE.toString().equals(opType))
      {
        return typeString.indexOf(value) > -1;
      }

    }
    else if (PfDataTypes.INTEGER.toString().equals(objType))
    {
      if (InObject == null) {
        InObject = new Integer(0);
      }
      if (value == null) {
        value = "0";
      }
      Integer typeInteger = (Integer)InObject;

      if ((PfOperatorTypes.EQ.toString().equals(opType)) || ("=".equals(opType)))
      {
        return typeInteger.intValue() == Integer.parseInt(value);
      }

      if (PfOperatorTypes.GE.toString().equals(opType))
      {
        return typeInteger.intValue() >= Integer.parseInt(value);
      }

      if (PfOperatorTypes.LE.toString().equals(opType))
      {
        return typeInteger.intValue() <= Integer.parseInt(value);
      }

      if (PfOperatorTypes.NE.toString().equals(opType))
      {
        return typeInteger.intValue() != Integer.parseInt(value);
      }

      if (PfOperatorTypes.LT.toString().equals(opType))
      {
        return typeInteger.intValue() < Integer.parseInt(value);
      }

      if (PfOperatorTypes.GT.toString().equals(opType))
      {
        return typeInteger.intValue() > Integer.parseInt(value);
      }

    }
    else if (PfDataTypes.UFDOUBLE.toString().equals(objType))
    {
      if (InObject == null) {
        InObject = new UFDouble(0);
      }
      if (value == null) {
        value = "0";
      }
      UFDouble typeDouble = (UFDouble)InObject;

      if ((PfOperatorTypes.EQ.toString().equals(opType)) || ("=".equals(opType)))
      {
        return typeDouble.doubleValue() == Double.parseDouble(value);
      }

      if (PfOperatorTypes.GE.toString().equals(opType))
      {
        return typeDouble.doubleValue() >= Double.parseDouble(value);
      }

      if (PfOperatorTypes.LE.toString().equals(opType))
      {
        return typeDouble.doubleValue() <= Double.parseDouble(value);
      }

      if (PfOperatorTypes.NE.toString().equals(opType))
      {
        return typeDouble.doubleValue() != Double.parseDouble(value);
      }

      if (PfOperatorTypes.LT.toString().equals(opType))
      {
        return typeDouble.doubleValue() < Double.parseDouble(value);
      }

      if (PfOperatorTypes.GT.toString().equals(opType))
      {
        return typeDouble.doubleValue() > Double.parseDouble(value);
      }

    }

    return false;
  }

  public static Object parseFunction(String functionNote, String className, String method, String parameter, PfParameterVO paraVo, Hashtable methodReturnHas)
    throws BusinessException
  {
    Object checkObject = null;

    Logger.debug("parseFunction解析函数类名:" + className + "方法名:" + method + "参数:" + parameter + "开始");
    if (className == null) {
      String errString = "parseFunction无类名，无法运行函数";
      throw new PFBusinessException(errString);
    }if (method == null) {
      String errString = "parseFunction无方法名，无法运行函数";
      throw new PFBusinessException(errString);
    }

    checkObject = runClass(className, method, parameter, paraVo, null, methodReturnHas);
    if (functionNote != null)
      Logger.debug("函数#" + functionNote + "#运行返回值为：" + String.valueOf(checkObject));
    else {
      Logger.debug("函数运行返回值为：" + String.valueOf(checkObject));
    }
    Logger.debug("parseFunction解析函数类名:" + className + "方法名:" + method + "参数:" + parameter + "****结束");

    return checkObject;
  }

  private static void parseParmeter(String dealStr, Object[] paraObjects, Class[] paraClasses, int arrIndex, PfParameterVO paraVo, Hashtable methodReturnHas)
    throws BusinessException
  {
    String fieldName = null;
    String datatype = null;
    int retIndex = dealStr.indexOf(":");
    if (retIndex < 0) {
      throw new PFBusinessException("注册参数不正确");
    }
    try
    {
      fieldName = dealStr.substring(0, retIndex);
      if (fieldName.substring(0, 3).equals("COM"))
      {
        paraObjects[arrIndex] = methodReturnHas.get(fieldName.substring(3));
      } else if (fieldName.startsWith("OBJ"))
      {
        if (fieldName.endsWith("ARY"))
          paraObjects[arrIndex] = paraVo.m_userObjs;
        else {
          paraObjects[arrIndex] = paraVo.m_userObj;
        }
      }
      else {
        paraObjects[arrIndex] = paraVo.m_standHeadVo.getAttributeValue(fieldName);
      }
      datatype = dealStr.substring(retIndex + 1);
      paraClasses[arrIndex] = parseTypeClass(datatype);
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFBusinessException("未找到注册的类文件", ex);
    }
  }

  public static boolean parseSyntax(String className, String method, String parameter, String funNote, String returnType, String ysf, String value, String className2, String method2, String parameter2, String funNote2, PfParameterVO paraVo, Hashtable methodReturnHas)
    throws BusinessException
  {
    return parseSyntax(className, method, parameter, funNote, returnType, ysf, value, className2, method2, parameter2, funNote2, paraVo, methodReturnHas, null, null);
  }

  public static boolean parseSyntax(String className, String method, String parameter, String funNote, String returnType, String ysf, String value, String className2, String method2, String parameter2, String funNote2, PfParameterVO paraVo, Hashtable methodReturnHas, StringBuffer errorMessageBuffer)
    throws BusinessException
  {
    return parseSyntax(className, method, parameter, funNote, returnType, ysf, value, className2, method2, parameter2, funNote2, paraVo, methodReturnHas, errorMessageBuffer, null);
  }

  public static boolean parseSyntax(String className, String method, String parameter, String funNote, String returnType, String ysf, String value, String className2, String method2, String parameter2, String funNote2, PfParameterVO paraVo, Hashtable methodReturnHas, StringBuffer errorMessageBuffer, StringBuffer originalHintBuffer)
    throws BusinessException
  {
    String strTmp = "";
    Object leftObject = null; Object rightObject = null;

    strTmp = "parseSyntax解析语法开始";
    Logger.debug(strTmp);
    if (className == null)
    {
      Logger.debug("parseSyntax解析语法结束");
      return false;
    }

    boolean retBool = true;

    leftObject = parseFunction(funNote, className, method, parameter, paraVo, methodReturnHas);
    if (className2 == null)
    {
      Logger.debug("用户输入的右值为：" + value);
    }
    else {
      rightObject = parseFunction(funNote2, className2, method2, parameter2, paraVo, methodReturnHas);

      value = String.valueOf(rightObject);
      Logger.debug("右函数的运行值为:" + value);
    }

    retBool = isTrueOrNot(leftObject, returnType.toUpperCase(), ysf, value);
    if (retBool) {
      Logger.debug("函数运行结果判断成功!");
    } else {
      Logger.debug("函数运行结果判断不成功!");
      if (errorMessageBuffer != null) {
        errorMessageBuffer.append(funNote);
        errorMessageBuffer.append("\n");
        errorMessageBuffer.append(leftObject);
        errorMessageBuffer.append("\n");
        errorMessageBuffer.append(ysf);
        errorMessageBuffer.append("\n");
        errorMessageBuffer.append(funNote2);
        errorMessageBuffer.append("\n");
        errorMessageBuffer.append(rightObject);
      }

      String MACRO_TAG = "%%";
      String originalHint = "";
      if (originalHintBuffer != null) {
        originalHint = originalHintBuffer.toString();
        String hintAfterParse = translateMacro(originalHint, MACRO_TAG, returnType, leftObject, value);

        originalHintBuffer.append(hintAfterParse);
      }
    }

    strTmp = "parseSyntax解析语法结束";
    Logger.debug(strTmp);
    return retBool;
  }

  private static AggregatedValueObject pfInitVoClass(String billType, int ChildLen)
    throws BusinessException
  {
    VotableVO votable = null;
    votable = PfDataCache.getBillTypeToVO(billType, true);
    if (votable == null) {
      throw new PFBusinessException("单据类型" + billType + "未在pub_votable中注册主表VO信息");
    }
    String billVOClzName = votable.getBillvo();
    Logger.debug("**************billVO:" + billVOClzName);
    String headVoClassName = votable.getHeaditemvo();
    Logger.debug("**************billHeadVO:" + billVOClzName);

    votable = PfDataCache.getBillTypeToVO(billType, false);
    String itemVoClassName = null;
    if (votable == null)
      Logger.warn("单据类型" + billType + "未在pub_votable中注册子表VO信息");
    else {
      itemVoClassName = votable.getHeaditemvo();
    }
    Logger.debug("**************billItemVO:" + billVOClzName);
    Logger.debug("**************ChildLen:" + ChildLen);

    AggregatedValueObject retObj = pfInitVo(billVOClzName, headVoClassName, itemVoClassName, ChildLen);

    return retObj;
  }

  public static AggregatedValueObject[] pfInitVosClass(String strBillVoClassName)
    throws BusinessException
  {
    AggregatedValueObject[] retVos = null;

    retVos = pfInitVos(strBillVoClassName, 1);
    return retVos;
  }

  private static AggregatedValueObject[] pfInitVosClass(String billType, AggregatedValueObject[] sourceBillVOs)
    throws BusinessException
  {
    VotableVO votable = PfDataCache.getBillTypeToVO(billType, true);
    if (votable == null) {
      throw new PFBusinessException("单据类型" + billType + "未在pub_votable中注册主表VO信息");
    }
    String billvoClzName = votable.getBillvo();
    Logger.debug("************pfInitVosClass::billVO:" + billvoClzName);
    AggregatedValueObject[] retVos = null;

    retVos = pfInitVos(billvoClzName, sourceBillVOs.length);

    for (int i = 0; i < sourceBillVOs.length; i++) {
      int childLength = sourceBillVOs[i].getChildrenVO() == null ? 0 : sourceBillVOs[i].getChildrenVO().length;

      Logger.debug("************pfInitVosClass::childLen:" + childLength);
      retVos[i] = pfInitVoClass(billType, childLength);
    }
    return retVos;
  }

  public static AggregatedValueObject runChangeData(String sourceBillType, String destBillType, AggregatedValueObject sourceBillVO)
    throws BusinessException
  {
    return runChangeData(sourceBillType, destBillType, sourceBillVO, null);
  }

  public static AggregatedValueObject runChangeData(String sourceBillType, String destBillType, AggregatedValueObject sourceBillVO, PfParameterVO paraVo)
    throws BusinessException
  {
    Logger.debug(">>开始单据VO交换=" + sourceBillType + "到" + destBillType);

    AggregatedValueObject destBillVO = null;

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;
    String bsChgClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;
    Object changeImpl = null;
    try {
      changeImpl = findChangeScriptClass(sourceBillType, destBillType);
    } catch (Exception e1) {
      Logger.warn("错误：实例化交换类失败=" + bsChgClassName, e1);
    }
    if (changeImpl == null) {
      Logger.warn("无交换类............");
      Logger.warn("单据VO交换完成........");
      return null;
    }
    try
    {
      if ((changeImpl instanceof VOConversion)) {
        VOConversion tmpVar = (VOConversion)changeImpl;
        tmpVar.setSourceBilltype(sourceBillType);
        tmpVar.setDestBilltype(destBillType);
        if (paraVo != null) {
          initConversionEnv(paraVo, tmpVar);
        }
      }

      adjustBeforeVoMapping(destBillType, new AggregatedValueObject[] { sourceBillVO });

      int intChildLen = 0;
      if (sourceBillVO.getChildrenVO() != null) {
        intChildLen = sourceBillVO.getChildrenVO().length;
      }
      AggregatedValueObject destVo = pfInitVoClass(destBillType, intChildLen);

      destBillVO = ((IchangeVO)changeImpl).retChangeBusiVO(sourceBillVO, destVo);

      adjustAfterVoMapping(sourceBillType, destBillType, new AggregatedValueObject[] { sourceBillVO }, new AggregatedValueObject[] { destBillVO });
    }
    catch (Exception e)
    {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    }

    Logger.debug(">>结束单据VO交换=" + sourceBillType + "到" + destBillType);
    return destBillVO;
  }

  private static void initConversionEnv(PfParameterVO paraVo, VOConversion tmpVar)
  {
    tmpVar.setSysDate(paraVo.m_currentDate.substring(0, 10));

    tmpVar.setSysOperator(paraVo.m_operator);

    String accountYear = "";
    try {
      AccountCalendar calendar = AccountCalendar.getInstance();
      calendar.setDate(DateUtilities.getInstance().parse(paraVo.m_currentDate));
      accountYear = calendar.getYearVO().getPeriodyear();
    } catch (InvalidAccperiodExcetion e) {
      Logger.warn(e.getMessage(), e);
    } catch (ParseException e) {
      Logger.warn(e.getMessage(), e);
    }
    tmpVar.setSysAccountYear(accountYear);

    tmpVar.setSysCorp(InvocationInfoProxy.getInstance().getCorpCode());

    tmpVar.setSysTime(new UFDateTime(new Date()).toString());
  }

  public static AggregatedValueObject[] runChangeDataAry(String sourceBillType, String destBillType, AggregatedValueObject[] sourceBillVOs)
    throws BusinessException
  {
    return runChangeDataAry(sourceBillType, destBillType, sourceBillVOs, null);
  }

  public static AggregatedValueObject[] runChangeDataAry(String sourceBillType, String destBillType, AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo)
    throws BusinessException
  {
    Logger.debug(">>开始单据VO批量交换=" + sourceBillType + "到" + destBillType);

    AggregatedValueObject[] destBillVOs = null;

    String strClassNameNoPackage = "CHG" + sourceBillType + "TO" + destBillType;
    String bsChgClassName = "nc.bs.pf.changedir." + strClassNameNoPackage;
    Object changeImpl = null;
    try {
      changeImpl = findChangeScriptClass(sourceBillType, destBillType);
    } catch (Exception e1) {
      Logger.warn("错误：实例化交换类失败=" + bsChgClassName, e1);
    }
    if (changeImpl == null) {
      Logger.warn("无交换类............");
      Logger.warn("业务交换执行过程完成........");
      return null;
    }
    try
    {
      if ((changeImpl instanceof VOConversion)) {
        VOConversion tmpVar = (VOConversion)changeImpl;
        tmpVar.setSourceBilltype(sourceBillType);
        tmpVar.setDestBilltype(destBillType);
        if (paraVo != null) {
          initConversionEnv(paraVo, tmpVar);
        }
      }

      adjustBeforeVoMapping(destBillType, sourceBillVOs);

      AggregatedValueObject[] destVo = pfInitVosClass(destBillType, sourceBillVOs);

      destBillVOs = ((IchangeVO)changeImpl).retChangeBusiVOs(sourceBillVOs, destVo);

      adjustAfterVoMapping(sourceBillType, destBillType, sourceBillVOs, destBillVOs);
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFBusinessException(ex.getMessage(), ex);
    }
    Logger.debug(">>结束单据VO批量交换=" + sourceBillType + "到" + destBillType);
    return destBillVOs;
  }

  public static Object newVoMappingObject(String sourceBillType, String destBillType, String fullyQualifiedClassName)
    throws BusinessException
  {
    Object changeObj = null;
    IPFMetaModel pfmeta = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());

    String moduleOfDest = pfmeta.queryModuleOfBilltype(destBillType);

    String moduleOfSrc = pfmeta.queryModuleOfBilltype(sourceBillType);

    if ((moduleOfDest == null) || (moduleOfDest.trim().length() == 0)) {
      if ((moduleOfSrc == null) || (moduleOfSrc.trim().length() == 0)) {
        throw new PFBusinessException("错误：源或目的单据类型都没有注册模块名");
      }
      changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
    }
    else {
      try {
        changeObj = NewObjectService.newInstance(moduleOfDest, fullyQualifiedClassName);
      }
      catch (Exception e) {
        if ((moduleOfSrc == null) || (moduleOfSrc.trim().length() == 0)) {
          throw new PFBusinessException("错误：VO交换类不在目的单据类型所属模块中");
        }
        changeObj = NewObjectService.newInstance(moduleOfSrc, fullyQualifiedClassName);
      }
    }
    return changeObj;
  }

  public static Object runClass(String className, String method, String parameter, PfParameterVO paraVo, Hashtable keyHas, Hashtable methodReturnHas)
    throws BusinessException
  {
    Logger.debug("**********执行类PfUtilTools.runClass()开始************");

    Logger.debug("执行类:" + className + ";方法:" + method + ";参数:" + parameter);

    StringTokenizer tmpStrToken = new StringTokenizer(parameter, ",");
    String[] paraStrs = new String[tmpStrToken.countTokens()];
    int index = 0;
    while (tmpStrToken.hasMoreTokens()) {
      paraStrs[(index++)] = tmpStrToken.nextElement().toString();
    }
    Object[] paraObjects = new Object[paraStrs.length];
    Class[] paraClasses = new Class[paraStrs.length];

    for (int i = 0; i < paraStrs.length; i++)
    {
      boolean isVo = paraStrs[i].indexOf(".") > 0;
      int colonPos = paraStrs[i].indexOf(":");

      if (paraStrs[i].startsWith("&"))
      {
        String paramKey = paraStrs[i].substring(1, colonPos);
        String strDataType = paraStrs[i].substring(colonPos + 1);
        Object valueObj = keyHas == null ? null : keyHas.get(paramKey);

        if (isVo)
        {
          if (strDataType.endsWith("[]")) {
            strDataType = strDataType.substring(0, strDataType.length() - 2);
            int intAryLen = 0;
            if ((valueObj instanceof AggregatedValueObject[]))
              intAryLen = ((AggregatedValueObject[])(AggregatedValueObject[])valueObj).length;
            else if ((valueObj instanceof ValueObject[])) {
              intAryLen = ((ValueObject[])(ValueObject[])valueObj).length;
            }
            paraClasses[i] = getClassNameClass(strDataType, intAryLen);
          } else {
            paraClasses[i] = getClassByName(strDataType);
          }
        }
        else paraClasses[i] = parseTypeClass(strDataType);

        paraObjects[i] = valueObj;
      }
      else if (isVo)
      {
        if (colonPos < 0) {
          throw new PFBusinessException("参数注册处VO必须重新注册");
        }
        String tmpbillType = paraStrs[i].substring(colonPos + 1);
        String voClassName = paraStrs[i].substring(0, colonPos);

        if (tmpbillType.equals("00"))
          throw new PFBusinessException("参数注册无标准VO");
        if (tmpbillType.equals("01"))
        {
          if (voClassName.endsWith("[]")) {
            paraObjects[i] = paraVo.m_preValueVos;
            paraClasses[i] = nc.vo.pub.AggregatedValueObject.class;
          } else {
            paraObjects[i] = paraVo.m_preValueVo;
            paraClasses[i] = AggregatedValueObject.class;
          }
        }
        else
        {
          Integer inBillType = PfDataCache.getBillTypeInfo(paraVo.m_billType).getBillstyle();

          Integer currBillType = PfDataCache.getBillTypeInfo(tmpbillType).getBillstyle();
          if ((tmpbillType.equals(paraVo.m_billType)) || ((inBillType != null) && (currBillType != null) && (inBillType.equals(currBillType))))
          {
            if (voClassName.endsWith("[]")) {
              paraObjects[i] = paraVo.m_preValueVos;
              paraClasses[i] = getClassNameClass(voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
            }
            else {
              paraObjects[i] = paraVo.m_preValueVo;
              paraClasses[i] = getClassByName(voClassName);
            }

          }
          else if (voClassName.endsWith("[]")) {
            paraObjects[i] = runChangeDataAry(paraVo.m_billType, tmpbillType, paraVo.m_preValueVos, paraVo);

            paraClasses[i] = getClassNameClass(voClassName.substring(0, voClassName.length() - 2), paraVo.m_preValueVos.length);
          }
          else {
            paraObjects[i] = runChangeData(paraVo.m_billType, tmpbillType, paraVo.m_preValueVo, paraVo);

            paraClasses[i] = getClassByName(voClassName);
          }
        }
      }
      else
      {
        parseParmeter(paraStrs[i], paraObjects, paraClasses, i, paraVo, methodReturnHas);
      }

    }

    Object tmpObj = null;
    try {
      IPFMetaModel pfMeta = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());

      String module = pfMeta.queryModuleOfBilltype(paraVo.m_billType);
      tmpObj = NewObjectService.newInstance(module, className);
    } catch (Exception e) {
      Logger.warn("在模块中找不到类，则假设为PUBLIC类：" + className, e);
      try
      {
        Class cls = Class.forName(className);
        tmpObj = cls.newInstance();
      } catch (ClassNotFoundException ex) {
        Logger.error(ex.getMessage(), ex);
      } catch (InstantiationException ex) {
        Logger.error(ex.getMessage(), ex);
      } catch (IllegalAccessException ex) {
        Logger.error(ex.getMessage(), ex);
      }
    }

    if (tmpObj == null) {
      throw new PFBusinessException("无法实例化类：" + className);
    }

    Object retObj = null;
    try {
      Class c = tmpObj.getClass();

      Method cm = c.getMethod(method, paraClasses);

      synchronized (Thread.currentThread()) {
        if (cm.getReturnType().toString().equals("void"))
          cm.invoke(tmpObj, paraObjects);
        else
          retObj = cm.invoke(tmpObj, paraObjects);
      }
    }
    catch (SecurityException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    }
    catch (InvocationTargetException e) {
      Throwable expt = e.getTargetException();
      Logger.error(e.getMessage(), e);
      if ((expt instanceof BusinessException))
      {
        throw ((BusinessException)expt);
      }if (((expt instanceof RemoteException)) && ((expt.getCause() instanceof BusinessException))) {
        throw ((BusinessException)expt.getCause());
      }
      throw new PFBusinessException(expt.getMessage(), expt);
    }

    Logger.debug("************执行类PfUtilTools.runClass()结束************");
    return retObj;
  }

  protected static String translateMacro(String content, String macro_tag, String type, Object leftValue, Object rightValue)
  {
    int pos_b = 0;
    int pos_e = 0;
    int offset = 0;
    boolean bFound = false;
    boolean isMatch = false;
    StringBuffer buffer = new StringBuffer();
    do
    {
      pos_e = content.indexOf(macro_tag, pos_b);
      bFound = pos_e != -1;
      offset = bFound ? macro_tag.length() : 0;
      if (bFound)
      {
        if (isMatch)
        {
          String macro = content.substring(pos_b, pos_e);

          buffer.append(getExpressValue(macro, type, leftValue, rightValue));
        }
        else {
          buffer.append(content.substring(pos_b, pos_e));
        }
        isMatch = !isMatch;
      } else {
        buffer.append(content.substring(pos_b));
      }
      pos_b = pos_e + offset;
    }while (pos_b != -1);

    return buffer.toString();
  }

  public static boolean isContinue(String pkCorp, String currUserPK, int configflag, String configedOperator)
    throws BusinessException
  {
    switch (configflag)
    {
    case 2:
      return configedOperator.equals(currUserPK);
    case 3:
      IUserManageQuery roleBS = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());

      RoleVO[] roles = roleBS.getUserRole(currUserPK, pkCorp);

      if ((roles == null) || (roles.length == 0)) {
        return false;
      }
      for (int i = 0; i < roles.length; i++) {
        if (configedOperator.equals(roles[i].getPrimaryKey()))
          return true;
      }
      return false;
    }

    return true;
  }

  public static void clusterFile(String relativePath, String fileName, String module)
  {
    Logger.debug(">>流程平台集群消息发送开始");

    String strFile = RuntimeEnv.getInstance().getNCHome() + relativePath + fileName;
    File file = new File(strFile);
    if (!file.exists()) {
      Logger.warn(">>文件=" + strFile + "并不存在，无法集群化文件。");
      return;
    }

    BufferedInputStream bis = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(file));
      byte[] bytes = new byte[bis.available()];
      bis.read(bytes);
      BytesClusterMessage message = new BytesClusterMessage();

      ClusterMessageHeader header = new ClusterMessageHeader("fileType", "PF");
      message.addHeader(header);

      ClusterMessageHeader header2 = new ClusterMessageHeader("fileName", fileName);
      message.addHeader(header2);

      ClusterMessageHeader header3 = new ClusterMessageHeader("ts", String.valueOf(file.lastModified()));

      message.addHeader(header3);

      ClusterMessageHeader header4 = new ClusterMessageHeader("relativePath", relativePath);
      message.addHeader(header4);

      if (module != null)
      {
        ClusterMessageHeader header5 = new ClusterMessageHeader("module", module);
        message.addHeader(header5);
      }

      message.setBytes(bytes);
      ClusterSender sender = (ClusterSender)NCLocator.getInstance().lookup(ClusterSender.class.getName());

      sender.sendMessage(message);

      Logger.debug(">>流程平台集群消息发送结束=" + strFile);

      if (null != bis) {
        try {
          bis.close();
        } catch (IOException e2) {
          Logger.error(e2.getMessage(), e2);
        }
        bis = null;
      }
    }
    catch (FileNotFoundException e2)
    {
      Logger.error(e2.getMessage(), e2);

      if (null != bis) {
        try {
          bis.close();
        } catch (IOException e3) {
          Logger.error(e3.getMessage(), e3);
        }
        bis = null;
      }
    }
    catch (IOException e2)
    {
      Logger.error(e2.getMessage(), e2);

      if (null != bis) {
        try {
          bis.close();
        } catch (IOException e3) {
          Logger.error(e3.getMessage(), e3);
        }
        bis = null;
      }
    }
    catch (ClusterServiceException e2)
    {
      Logger.error(e2.getMessage(), e2);

      if (null != bis) {
        try {
          bis.close();
        } catch (IOException e3) {
          Logger.error(e3.getMessage(), e3);
        }
        bis = null;
      }
    }
    finally
    {
      if (null != bis) {
        try {
          bis.close();
        } catch (IOException e2) {
          Logger.error(e2.getMessage(), e2);
        }
        bis = null;
      }
    }
  }
}