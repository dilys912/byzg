package nc.ui.pub.pf;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFTemplate;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.uap.pf.TemplateParaVO;

public class PfUtilBO_Client
{
  public static String[] getBusitypeByCorpAndStyle(String arg0, String arg1)
    throws Exception
  {
    IPFConfig bsPfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    String[] o = null;
    o = bsPfConfig.getBusitypeByCorpAndStyle(arg0, arg1);
    return o;
  }

  public static String getTemplateId(int templateStyle, String pkCorp, String funNode, String operator, String busiType, String nodeKey)
    throws Exception
  {
    IPFTemplate bo = getBSTemplate();
    TemplateParaVO tempVo = getTemplatePara(templateStyle, pkCorp, funNode, operator, busiType, nodeKey);
    return bo.getTemplateId(tempVo);
  }

  private static TemplateParaVO getTemplatePara(int templateStyle, String pkCorp, String funNode, String operator, String busiType, String nodeKey)
  {
    TemplateParaVO tempVo = new TemplateParaVO();
    tempVo.setBusiType(busiType);
    tempVo.setFunNode(funNode);
    tempVo.setPk_Corp(pkCorp);
    tempVo.setPk_orgUnit(pkCorp);
    tempVo.setNodeKey(nodeKey);
    tempVo.setOperator(operator);
    tempVo.setTemplateType(templateStyle);
    return tempVo;
  }

  private static IPFTemplate getBSTemplate()
    throws ComponentException
  {
    IPFTemplate bsTemplate = (IPFTemplate)NCLocator.getInstance().lookup(IPFTemplate.class.getName());
    return bsTemplate;
  }

  public static String[] getTemplateIdAry(int templateStyle, String pkCorp, String funNode, String operator, String busiType, String nodeKey) throws Exception
  {
    IPFTemplate bo = getBSTemplate();
    TemplateParaVO tempVo = getTemplatePara(templateStyle, pkCorp, funNode, operator, busiType, nodeKey);
    String[] o = null;
    o = bo.getSingleTempIdAry(tempVo);
    return o;
  }

  public static String[] getTemplateIdAry2(int templateStyle, String pkCorp, String funNode, String operator, String busiType, String nodeKey)
    throws Exception
  {
    IPFTemplate bo = getBSTemplate();
    TemplateParaVO tempVo = getTemplatePara(templateStyle, pkCorp, funNode, operator, busiType, nodeKey);
    String[] o = null;
    o = bo.getComplexTempIdAry(tempVo);
    return o;
  }

  public static boolean isExistWorkFlow(String arg0, String arg1, String arg2, String arg3)
    throws Exception
  {
    IPFWorkflowQry bsWorkflow = (IPFWorkflowQry)NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName());
    return bsWorkflow.isExistWorkFlow(arg0, arg1, arg2, arg3);
  }

  public static AggregatedValueObject queryBillDataVO(String arg0, String arg1) throws Exception
  {
    IPFConfig pfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    AggregatedValueObject o = pfConfig.queryBillDataVO(arg0, arg1);
    return o;
  }

  public static CircularlyAccessibleValueObject[] queryBodyAllData(String arg0, String arg1, String arg2) throws Exception
  {
    IPFConfig pfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    CircularlyAccessibleValueObject[] o = null;
    o = pfConfig.queryBodyAllData(arg0, arg1, arg2);
    return o;
  }

  public static CircularlyAccessibleValueObject[] queryHeadAllData(String arg0, String arg1, String arg2)
    throws Exception
  {
    IPFConfig pfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    CircularlyAccessibleValueObject[] o = null;
    o = pfConfig.queryHeadAllData(arg0, arg1, arg2);
    return o;
  }

  public static int queryWorkFlowStatus(String arg0, String arg1, String arg2)
    throws Exception
  {
    IPFWorkflowQry bsWorkflow = (IPFWorkflowQry)NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName());
    int o = 0;
    o = bsWorkflow.queryWorkflowStatus(arg0, arg1, arg2);
    return o;
  }
}