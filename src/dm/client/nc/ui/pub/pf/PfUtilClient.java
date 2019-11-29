package nc.ui.pub.pf;

import java.awt.Container;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComponentUtil;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.logging.Debug;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtobill.BilltobillreferVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IGetBusiDataForFlow;
import nc.vo.pub.pf.IPfRetException;
import nc.vo.pub.pf.IProcActionRetObject;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.TaskInfo;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.PFRuntimeException;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.wfengine.pub.WFTask;

public class PfUtilClient
{
  private static boolean m_checkFlag = true;

  private static String m_currentBillType = null;

  private static int m_iCheckResult = 0;

  private static boolean m_isOk = false;

  private static boolean m_isRetChangeVo = false;

  private static boolean m_isSuccess = true;

  private static String m_sourceBillType = null;

  private static AggregatedValueObject m_tmpRetVo = null;

  private static AggregatedValueObject[] m_tmpRetVos = null;

  public static boolean makeFlag = false;

  private static PfUtilWorkFlowVO checkOnSave(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject billVo, Stack dlgResult)
    throws Exception
  {
    IWorkflowMachine bsWorkflow = (IWorkflowMachine)NCLocator.getInstance().lookup(IWorkflowMachine.class.getName());

    PfUtilWorkFlowVO wfVo = bsWorkflow.checkWorkFlow(actionName, billType, currentDate, billVo);

    if (wfVo != null)
    {
      Vector assignInfos = wfVo.getTaskInfo().getAssignableInfos();
      if ((assignInfos != null) && (assignInfos.size() > 0))
      {
        DispatchDialog dd = new DispatchDialog(parent, assignInfos);
        int iClose = dd.showModal();
        dlgResult.push(new Integer(iClose));
      }

      if ((billVo instanceof IGetBusiDataForFlow))
        fetchMoneyInfo(billVo, wfVo);
      else {
        zeroMoneyInfo(wfVo);
      }
    }
    return wfVo;
  }

  private static PfUtilWorkFlowVO checkWorkFlow(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject billVo)
    throws Exception
  {
    PfUtilWorkFlowVO wfVo = null;
    WorkFlowCheckDlg clientWorkFlow = null;
    try {
      IWorkflowMachine bsWorkflow = (IWorkflowMachine)NCLocator.getInstance().lookup(IWorkflowMachine.class.getName());

      wfVo = bsWorkflow.checkWorkFlow(actionName, billType, currentDate, billVo);
      if (wfVo == null) {
        m_checkFlag = true;
        PfUtilWorkFlowVO localPfUtilWorkFlowVO1 = wfVo;
        return localPfUtilWorkFlowVO1;
      }
      if ((billVo instanceof IGetBusiDataForFlow)) {
        fetchMoneyInfo(billVo, wfVo);
        clientWorkFlow = new WorkFlowCheckDlg(parent, wfVo, currentDate, true);
      } else {
        zeroMoneyInfo(wfVo);
        clientWorkFlow = new WorkFlowCheckDlg(parent, wfVo, currentDate, false);
      }
      clientWorkFlow.showModal();

      if (clientWorkFlow.getResult() == 1)
      {
        m_checkFlag = true;
        wfVo = clientWorkFlow.getWorkFlow();
      } else {
        m_checkFlag = false;
        wfVo = null;
      }
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      throw ex;
    } finally {
      if (clientWorkFlow != null) {
        UIComponentUtil.removeAllComponentRefrence(clientWorkFlow);
      }
    }
    return wfVo;
  }

  private static void zeroMoneyInfo(PfUtilWorkFlowVO wfVo)
  {
    UFDouble zeroMny = new UFDouble(0);
    wfVo.setMoney(zeroMny);
    wfVo.setLocalMoney(zeroMny);
    wfVo.setAssMoney(zeroMny);
    wfVo.setCurrency(null);

    wfVo.setPreMoney(zeroMny);
    wfVo.setPreLocalMoney(zeroMny);
    wfVo.setPreAssMoney(zeroMny);
  }

  private static void fetchMoneyInfo(AggregatedValueObject billVo, PfUtilWorkFlowVO wfVo)
  {
    UFDouble mny = ((IGetBusiDataForFlow)billVo).getPfMoney();
    UFDouble localMny = ((IGetBusiDataForFlow)billVo).getPfLocalMoney();
    UFDouble assMny = ((IGetBusiDataForFlow)billVo).getPfAssMoney();
    wfVo.setMoney(mny);
    wfVo.setLocalMoney(localMny);
    wfVo.setAssMoney(assMny);
    wfVo.setCurrency(((IGetBusiDataForFlow)billVo).getPfCurrency());

    wfVo.setPreMoney(mny);
    wfVo.setPreLocalMoney(localMny);
    wfVo.setPreAssMoney(assMny);
  }

  public static void childButtonClicked(ButtonObject bo, String pkCorp, String FunNode, String pkOperator, String currentBillType, Container parent)
  {
    childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent, null);
  }

  public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode, String pkOperator, String currentBillType, Container parent, Object userObj)
  {
    childButtonClicked(btnObj, pkCorp, funNode, pkOperator, currentBillType, parent, userObj, null);
  }

  public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode, String pkOperator, String currentBillType, Container parent, Object userObj, String sourceBillId)
  {
    String tmpString = btnObj.getTag();
    int findIndex = tmpString.indexOf(":");

    String billType = tmpString.substring(0, findIndex);

    String businessType = tmpString.substring(findIndex + 1);
    if (businessType.trim().length() == 0) {
      businessType = null;
    }
    makeFlag = false;
    if (billType.toUpperCase().equals("MAKEFLAG")) {
      Debug.debug("******自制单据******");
      makeFlag = true;
      return;
    }
    Debug.debug("******参照来源单据******");
    try
    {
      BilltobillreferVO billtobillVO = PfUIDataCache.getBillInfo(currentBillType, billType);

      funNode = PfUIDataCache.getBillType(billType).getNodecode();
      if ((funNode == null) || (funNode.equals("")))
      {
        throw new PFBusinessException("请注册单据的功能节点号");
      }

      String strQueryTemplateId = null;
      String referClassName = null;
      String srcNodekey = null;
      if (billtobillVO != null) {
        strQueryTemplateId = billtobillVO.getQuerytemplateid();
        strQueryTemplateId = strQueryTemplateId == null ? null : strQueryTemplateId.trim();
        referClassName = billtobillVO.getReferclassname();
        srcNodekey = billtobillVO.getChangeclassname();

        if ((srcNodekey != null) && (srcNodekey.startsWith("<")) && (srcNodekey.endsWith(">")))
          srcNodekey = srcNodekey.substring(1, srcNodekey.length() - 1);
        else {
          srcNodekey = null;
        }
      }

      QueryConditionClient condition = null;
      boolean isQueryRelationCorp = true;
      if ((strQueryTemplateId == null) || (strQueryTemplateId.equals(""))) {
        Debug.debug("调用通用的查询Id");

        strQueryTemplateId = PfUtilBO_Client.getTemplateId(1, pkCorp, funNode, pkOperator, businessType, srcNodekey);

        isQueryRelationCorp = false;
        condition = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator, funNode);
      }
      else if (strQueryTemplateId.startsWith("<")) {
        Debug.debug("产品组自定义特色查询");
        Debug.debug("该代码必须实现接口,同时以容器为构造函数");
        strQueryTemplateId = strQueryTemplateId.substring(1, strQueryTemplateId.length() - 1);
        condition = loadUserQuery(parent, strQueryTemplateId, pkCorp, pkOperator, funNode, businessType, currentBillType, billType, srcNodekey, userObj);
      }
      else {
        Debug.debug("支持多单位帐查询的产品组自定义查询");
        isQueryRelationCorp = true;
        condition = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator, funNode);
      }

      if (sourceBillId == null) {
        condition.showModal();

        if (condition.isCloseOK())
        {
          refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, billType, businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp, sourceBillId, condition);
        }
        else
        {
          m_isOk = false;
          return;
        }
      } else {
        refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, billType, businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp, sourceBillId, condition);

        return;
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  private static void refBillSource(String pkCorp, String funNode, String pkOperator, String currentBillType, Container parent, Object userObj, String billType, String businessType, String strQueryTemplateId, String referClassName, String srcNodekey, boolean isQueryRelationCorp, String sourceBillId, QueryConditionClient condition)
    throws Exception
  {
    String pkField = null;
    pkField = PfUIDataCache.getBillPK(billType);
    if ((pkField == null) || (pkField.trim().length() == 0)) throw new Exception("请注册单据的关键字");
    String whereString = null;
    if (sourceBillId == null)
      whereString = condition.getWhereSQL();
    else {
      whereString = pkField + "='" + sourceBillId + "'";
    }
    if (isQueryRelationCorp)
      whereString = whereString + "1";
    else {
      whereString = whereString + "0";
    }

    AbstractReferQueryUI billReferUI = null;
    if ((referClassName != null) && (!referClassName.equals(""))) {
      Debug.debug("产品组自定义的参照界面");
      billReferUI = loadReferUI(referClassName, pkField, pkCorp, pkOperator, funNode, whereString, billType, businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
    }
    else
    {
      Debug.debug("通用的查询界面");
      billReferUI = new BillSourceDLG(pkField, pkCorp, pkOperator, funNode, whereString, billType, businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
    }

    if ((billReferUI instanceof BillSourceDLG))
    {
      m_isRetChangeVo = false;
    }
    else {
      m_isRetChangeVo = true;
    }

    billReferUI.setQueyDlg(condition);

    billReferUI.addBillUI();

    billReferUI.loadHeadData();

    billReferUI.showModal();
    if (billReferUI.getResult() == 1) {
      m_sourceBillType = billType;
      m_currentBillType = currentBillType;
      m_tmpRetVo = billReferUI.getRetVo();
      m_tmpRetVos = billReferUI.getRetVos();
      m_isOk = true;
    } else {
      m_isOk = false;
    }
  }

  public static int getCurrentCheckResult()
  {
    return m_iCheckResult;
  }

  public static AggregatedValueObject getRetOldVo()
  {
    return m_tmpRetVo;
  }

  public static AggregatedValueObject[] getRetOldVos()
  {
    return m_tmpRetVos;
  }

  public static AggregatedValueObject getRetVo()
  {
    try
    {
      if (!m_isRetChangeVo)
      {
        m_tmpRetVo = PfChangeBO_Client.pfChangeBillToBill(m_tmpRetVo, m_sourceBillType, m_currentBillType);
      }
    }
    catch (BusinessException ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFRuntimeException("VO交换错误：" + ex.getMessage(), ex);
    }
    return m_tmpRetVo;
  }

  public static AggregatedValueObject[] getRetVos()
  {
    try
    {
      if (!m_isRetChangeVo)
      {
        m_tmpRetVos = PfChangeBO_Client.pfChangeBillToBillArray(m_tmpRetVos, m_sourceBillType, m_currentBillType);
      }
    }
    catch (BusinessException ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFRuntimeException("VO交换错误：" + ex.getMessage(), ex);
    }
    return m_tmpRetVos;
  }

  private static boolean hintBeforeAction(Container parent, String actionName, String billType)
  {
    String hintString = null;
    String actionType = null;
    if (actionName.length() > 20)
      actionType = actionName.substring(0, actionName.length() - 20);
    String hintRes = "Dshowhint" + billType + actionType;
    hintString = NCLangRes.getInstance().getStrByID("pub_billaction", hintRes);

    hintString = hintString == null ? "" : hintString.trim();
    if ((!hintString.equals("")) && (!hintString.equals(hintRes)))
    {
      if (MessageDialog.showYesNoDlg(parent, null, hintString) != 4) {
        m_isSuccess = false;
        return false;
      }
    }
    return true;
  }

  public static boolean isCanceled()
  {
    return !m_checkFlag;
  }

  public static boolean isCloseOK()
  {
    return m_isOk;
  }

  public static boolean isSuccess()
  {
    return m_isSuccess;
  }

  private static void loadDLG(Container parent, PfUtilActionVO actionVo, Object userObj)
  {
    try
    {
      Class c = Class.forName(actionVo.getClassName());
      Class[] ArgsClass = { Container.class };
      Object[] Arguments = { parent };
      Constructor ArgsConstructor = c.getConstructor(ArgsClass);
      Object retObj = ArgsConstructor.newInstance(Arguments);
      if ((retObj instanceof IinitData))
        ((IinitData)retObj).initData(userObj.toString());
      else {
        ((IinitData2)retObj).initData(userObj);
      }
      ((UIDialog)retObj).showModal();
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  public static AbstractReferQueryUI loadReferUI(String referClassName, String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, String nodeKey, Object userObj, Container parent)
  {
    Class c = null;
    try
    {
      c = Class.forName(referClassName);

      Class[] Args220Class = { String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Object.class, Container.class };

      Object[] Arguments = { pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, nodeKey, userObj, parent };

      Constructor ArgsConstructor = c.getConstructor(Args220Class);
      return (AbstractReferQueryUI)ArgsConstructor.newInstance(Arguments);
    }
    catch (NoSuchMethodException ex) {
      try {
        Class[] ArgsClass = { String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Container.class };

        Object[] Arguments = { pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent };

        Constructor ArgsConstructor = c.getConstructor(ArgsClass);
        return (AbstractReferQueryUI)ArgsConstructor.newInstance(Arguments);
      } catch (NoSuchMethodException e) {
        MessageDialog.showErrorDlg(parent, "产品组自定义参照", "产品组自定义参照没有平台要求的构造方法,请继承AbstractReferQueryUI");
      }
      catch (Exception e) {
        Logger.error(e.getMessage(), e);
        MessageDialog.showErrorDlg(parent, "产品组自定义参照", "产品组自定义参照错误:" + ex.getMessage());
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
      MessageDialog.showErrorDlg(parent, "产品组自定义参照", "产品组220以后自定义参照错误:" + ex.getMessage());
    }
    return null;
  }

  private static QueryConditionClient loadUserQuery(Container parent, String className, String pkCorp, String pkOperator, String FunNode, String businessType, String currBillType, String sourceBillType, String nodeKey, Object userObj)
  {
    try
    {
      Class c = Class.forName(className);
      Class[] ArgsClass = { Container.class };
      Object[] Arguments = { parent };
      Constructor ArgsConstructor = c.getConstructor(ArgsClass);
      Object retObj = ArgsConstructor.newInstance(Arguments);
      if ((retObj instanceof IinitQueryData))
      {
        ((IinitQueryData)retObj).initData(pkCorp, pkOperator, FunNode, businessType, currBillType, sourceBillType, userObj);
      }
      else
      {
        ((IinitQueryData2)retObj).initData(pkCorp, pkOperator, FunNode, businessType, currBillType, sourceBillType, nodeKey, userObj);
      }

      return (QueryConditionClient)retObj;
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
    return null;
  }

  public static Object processAction(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject vo, Object userObj)
    throws Exception
  {
    Object retObj = processAction(parent, actionName, billType, currentDate, vo, userObj, null, null);

    return retObj;
  }

  public static Object processAction(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass, AggregatedValueObject checkVo)
    throws Exception
  {
    Object retObj = null;

    if (checkVo == null) {
      checkVo = billvo;
    }

    try
    {
      boolean isContinue = beforeProcessAction(parent, actionName, billType, checkVo, userObj, strBeforeUIClass);

      if (!isContinue) {
        return null;
      }
      HashMap hmPfExParams = new HashMap();
      PfUtilWorkFlowVO workflowVo = null;

      if ((actionName.toUpperCase().endsWith("SAVE")) || (actionName.toUpperCase().endsWith("EDIT")))
      {
        Stack dlgResult = new Stack();
        workflowVo = checkOnSave(parent, "SAVE", billType, currentDate, billvo, dlgResult);

        if (dlgResult.size() > 0) {
          Integer iClose = (Integer)dlgResult.pop();
          if (iClose.intValue() == 2) {
            m_isSuccess = false;
            return null;
          }
        }
      }

      IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());

      retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workflowVo, billvo, userObj, hmPfExParams);

      afterProcessAction(parent, retObj);
      m_isSuccess = true;
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj, retObj, ex);
    }

    retObjRun(parent, userObj, retObj);

    return retObj;
  }

  private static Object actionExceptionProcess(Container parent, PfUtilWorkFlowVO workFlow, String actionName, String billType, String currentDate, Object userObj, Object retObj, Exception ex)
    throws Exception
  {
    if ((ex instanceof IPfRetException)) {
      IPfRetException retEx = (IPfRetException)ex;

      if (retEx.getBusiStyle() == 1)
      {
        try {
          retObj = runAfterActionException(parent, workFlow, actionName, billType, currentDate, retEx, userObj);

          m_isSuccess = true;
        } catch (Exception e) {
          m_isSuccess = false;
          throw e;
        }
      } else {
        m_isSuccess = false;
        throw ex;
      }
    } else {
      m_isSuccess = false;
      throw ex;
    }
    return retObj;
  }

  private static void afterProcessAction(Container parent, Object retObj)
    throws Exception
  {
    if ((retObj instanceof IProcActionRetObject)) {
      IProcActionRetObject tmpRetObj = (IProcActionRetObject)retObj;
      String afterClassName = tmpRetObj.getClassStr();
      Object retValObj = tmpRetObj.getObj();
      Object retRunObj = runClass(afterClassName);

      ((IUIAfterProcAction)retRunObj).runClass(parent, retValObj);
    }
  }

  public static Object processAction(String actionName, String billType, String currentDate, AggregatedValueObject vo)
    throws Exception
  {
    Object retObj = processAction(null, actionName, billType, currentDate, vo, null);
    return retObj;
  }

  public static Object processAction(String actionName, String billType, String currentDate, AggregatedValueObject vo, Object userObj)
    throws Exception
  {
    Object retObj = processAction(null, actionName, billType, currentDate, vo, userObj);
    return retObj;
  }

  public static Object processActionFlow(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject vo, Object userObj)
    throws Exception
  {
    Object retObj = processActionFlow(parent, actionName, billType, currentDate, vo, userObj, null);

    return retObj;
  }

  public static Object processActionFlow(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject billVo, Object userObj, String strBeforeUIClass)
    throws Exception
  {
    Object retObj = null;

    PfUtilWorkFlowVO workFlow = null;
    try
    {
      boolean isContinue = beforeProcessAction(parent, actionName, billType, billVo, userObj, strBeforeUIClass);

      if (!isContinue) {
        return null;
      }
      workFlow = new PfUtilWorkFlowVO();
      if ((actionName.length() >= 7) && (actionName.toUpperCase().substring(0, 7).equals("APPROVE")))
      {
        workFlow = checkWorkFlow(parent, actionName, billType, currentDate, billVo);

        if ((workFlow == null) && (!m_checkFlag)) {
          m_isSuccess = false;
          return null;
        }
      }
      else {
        workFlow = null;
      }

      IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());

      retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workFlow, billVo, userObj, null);

      afterProcessAction(parent, retObj);

      m_isSuccess = true;
      if (workFlow != null)
      {
        boolean isCheckPass = workFlow.getIsCheckPass();
        if (isCheckPass)
        {
          WFTask currTask = workFlow.getTaskInfo().getTask();
          if (currTask.getTaskType() == 4) {
            if (currTask.isBackToFirstActivity())
              m_iCheckResult = 3;
            else
              m_iCheckResult = 2;
          }
          else m_iCheckResult = 0; 
        }
        else {
          m_iCheckResult = 1;
        }
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = actionExceptionProcess(parent, workFlow, actionName, billType, currentDate, userObj, retObj, ex);
    }

    retObjRun(parent, userObj, retObj);

    return retObj;
  }

  private static boolean beforeProcessAction(Container parent, String actionName, String billType, AggregatedValueObject billVo, Object userObj, String strBeforeUIClass)
    throws Exception
  {
    boolean isContinue = hintBeforeAction(parent, actionName, billType);
    if (!isContinue) {
      return false;
    }

    BilltypeVO billtypeVO = PfUIDataCache.getBillType(billType);
    if (billtypeVO.getDef3() == null) {
      String strClassName = UIBeforeRunClass.getUIClass(billType);
      if (strClassName != null) {
        Class c = Class.forName(strClassName);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction)) {
          ((IUIBeforeProcAction)o).runClass(parent, billType, actionName, billVo, userObj);
        }
      }
      if (strBeforeUIClass != null) {
        Class c = Class.forName(strBeforeUIClass);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction))
          ((IUIBeforeProcAction)o).runClass(parent, billType, actionName, billVo, userObj);
      }
    }
    else {
      String strClassName = billtypeVO.getDef3();
      if (strClassName != null) {
        Class c = Class.forName(strClassName);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction)) {
          ((IUIBeforeProcAction)o).runClass(parent, billType, actionName, billVo, userObj);
        }
      }
    }
    return true;
  }

  private static Object processActionFlowInner(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject vo, Object userObj, PfUtilWorkFlowVO workFlow)
    throws Exception
  {
    Object retObj = null;
    try
    {
      IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());

      retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workFlow, vo, userObj, null);

      afterProcessAction(parent, retObj);
      m_isSuccess = true;
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj, retObj, ex);
    }

    retObjRun(parent, userObj, retObj);

    return retObj;
  }

  private static void retObjRun(Container parent, Object userObj, Object retObj)
  {
    if ((retObj instanceof PfUtilActionVO)) {
      PfUtilActionVO retVo = null;
      if (userObj == null) {
        System.out.println("用户对象输入为NULL");
      }
      retVo = (PfUtilActionVO)retObj;
      if (retVo.getIsDLG()) {
        System.out.println("执行DLG");
        loadDLG(parent, retVo, retVo.getUIObj());
      } else {
        System.out.println("执行PANEL");
        BillQueryDLG bQDlg = new BillQueryDLG(parent, retVo, retVo.getUIObj());
        bQDlg.showModal();
      }
    }
  }

  public static Object processActionNoSendMessage(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass, AggregatedValueObject checkVo)
    throws Exception
  {
    Object retObj = null;
    if (checkVo == null) {
      checkVo = billvo;
    }
    try
    {
      boolean isContinue = beforeProcessAction(parent, actionName, billType, checkVo, userObj, strBeforeUIClass);

      if (!isContinue) {
        return null;
      }
      HashMap hmPfExParams = new HashMap();
      hmPfExParams.put("nosendmessage", "nosendmessage");

      IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());

      retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, null, billvo, userObj, hmPfExParams);

      afterProcessAction(parent, retObj);
      m_isSuccess = true;
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj, retObj, ex);
    }

    retObjRun(parent, userObj, retObj);

    return retObj;
  }

  public static Object[] processBatch(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry)
    throws Exception
  {
    Object retObj = processBatch(parent, actionName, billType, currentDate, voAry, userObjAry, null);

    return (Object[])(Object[])retObj;
  }

  public static Object[] processBatch(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass)
    throws Exception
  {
    Object retObj = null;
    try
    {
      boolean isContinue = beforeProcessBatchAction(parent, actionName, billType, voAry, userObjAry, strBeforeUIClass);

      if (!isContinue) {
        return null;
      }

      PfUtilWorkFlowVO workflowVo = null;
      if (voAry.length == 1)
      {
        if ((actionName.toUpperCase().endsWith("SAVE")) || (actionName.toUpperCase().endsWith("EDIT")))
        {
          Stack dlgResult = new Stack();
          workflowVo = checkOnSave(parent, "SAVE", billType, currentDate, voAry[0], dlgResult);

          if (dlgResult.size() > 0) {
            Integer iClose = (Integer)dlgResult.pop();
            if (iClose.intValue() == 2) {
              m_isSuccess = false;
              return null;
            }
          }
        }

      }

      IPFBusiAction bsBusiAction = (IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class.getName());

      retObj = bsBusiAction.processBatch(actionName, billType, currentDate, voAry, userObjAry, workflowVo);

      m_isSuccess = true;

      afterProcessAction(parent, retObj);
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = batchActionExceptionProcess(parent, null, actionName, billType, currentDate, userObjAry, retObj, ex);
    }

    return (Object[])(Object[])retObj;
  }

  private static boolean beforeProcessBatchAction(Container parent, String actionName, String billType, AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass)
    throws Exception
  {
    boolean isContinue = hintBeforeAction(parent, actionName, billType);

    BilltypeVO billVo = PfUIDataCache.getBillType(billType);
    if (billVo.getDef3() == null) {
      String strClassName = UIBeforeRunClass.getUIClass(billType);
      if (strClassName != null) {
        Class c = Class.forName(strClassName);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction)) {
          ((IUIBeforeProcAction)o).runBatchClass(parent, billType, actionName, voAry, userObjAry);
        }
      }
      if (strBeforeUIClass != null) {
        Class c = Class.forName(strBeforeUIClass);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction))
          ((IUIBeforeProcAction)o).runBatchClass(parent, billType, actionName, voAry, userObjAry);
      }
    }
    else {
      String strClassName = billVo.getDef3();
      if (strClassName != null) {
        Class c = Class.forName(strClassName);
        Object o = c.newInstance();
        if ((o instanceof IUIBeforeProcAction)) {
          ((IUIBeforeProcAction)o).runBatchClass(parent, billType, actionName, voAry, userObjAry);
        }
      }
    }
    return isContinue;
  }

  private static Object batchActionExceptionProcess(Container parent, PfUtilWorkFlowVO workflowVo, String actionName, String billType, String currentDate, Object[] userObjAry, Object retObj, Exception ex)
    throws Exception
  {
    if ((ex instanceof IPfRetException)) {
      IPfRetException retEx = (IPfRetException)ex;

      if (retEx.getBusiStyle() == 1)
      {
        try {
          retObj = runAfterBatchException(parent, workflowVo, actionName, billType, currentDate, retEx, userObjAry);

          m_isSuccess = true;
        } catch (Exception e) {
          m_isSuccess = false;
          throw e;
        }
      } else {
        m_isSuccess = false;
        throw ex;
      }
    } else {
      m_isSuccess = false;
      throw ex;
    }
    return retObj;
  }

  public static Object[] processBatch(String actionName, String billType, String currentDate, AggregatedValueObject[] voAry)
    throws Exception
  {
    Object[] retObj = processBatch(null, actionName, billType, currentDate, voAry, null);
    return retObj;
  }

  public static Object[] processBatch(String actionName, String billType, String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry)
    throws Exception
  {
    Object[] retObj = processBatch(null, actionName, billType, currentDate, voAry, userObjAry);
    return retObj;
  }

  public static Object[] processBatchFlow(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry)
    throws Exception
  {
    Object retObj = null;
    PfUtilWorkFlowVO workFlow = null;
    try
    {
      boolean isContinue = beforeProcessBatchAction(parent, actionName, billType, voAry, userObjAry, null);

      if (!isContinue) {
        return null;
      }

      workFlow = new PfUtilWorkFlowVO();
      if ((actionName.length() >= 7) && (actionName.toUpperCase().substring(0, 7).equals("APPROVE")))
      {
        workFlow = checkWorkFlow(parent, actionName, billType, currentDate, voAry[0]);

        if ((workFlow == null) && (!m_checkFlag)) {
          m_isSuccess = false;
          return null;
        }
      }
      else {
        workFlow = null;
      }

      IPFBusiAction bsBusiAction = (IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class.getName());

      retObj = bsBusiAction.processBatch(actionName, billType, currentDate, voAry, userObjAry, workFlow);

      m_isSuccess = true;

      afterProcessAction(parent, retObj);
    }
    catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = batchActionExceptionProcess(parent, workFlow, actionName, billType, currentDate, userObjAry, retObj, ex);
    }

    return (Object[])(Object[])retObj;
  }

  private static Object[] processBatchFlowInner(Container parent, String actionName, String billType, String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry, PfUtilWorkFlowVO workFlow)
    throws Exception
  {
    Object retObj = null;
    try
    {
      IPFBusiAction bsBusiAction = (IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class.getName());

      retObj = bsBusiAction.processBatch(actionName, billType, currentDate, voAry, userObjAry, workFlow);

      m_isSuccess = true;

      afterProcessAction(parent, retObj);
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);

      retObj = batchActionExceptionProcess(parent, workFlow, actionName, billType, currentDate, userObjAry, retObj, ex);
    }

    return (Object[])(Object[])retObj;
  }

  public static int queryWorkFlowStatus(String busiType, String billType, String billId)
  {
    try
    {
      return PfUtilBO_Client.queryWorkFlowStatus(busiType, billType, billId);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }return -1;
  }

  public static void retAddBtn(ButtonObject boAdd, String corpId, String billType, ButtonObject boBusiness)
  {
    try
    {
      String businessType = null;
      if (boBusiness != null) {
        businessType = boBusiness.getTag();
      }

      BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(corpId, billType, businessType);

      boAdd.removeAllChildren();
      if (billBusiVOs == null) {
        return;
      }

      ButtonObject btnAddChild = null;
      for (int i = 0; i < billBusiVOs.length; i++) {
        String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(), billBusiVOs[i].getBilltypename());

        btnAddChild = new ButtonObject(showName);
        btnAddChild.setPowerContrl(false);

        btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":" + billBusiVOs[i].getPk_businesstype().trim());

        boAdd.addChildButton(btnAddChild);
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  public static ButtonObject getRefAddBtn(ButtonObject boAdd, String billType)
    throws BusinessException
  {
    ButtonObject[] btnAddChild = boAdd.getChildButtonGroup();
    for (int i = 0; i < btnAddChild.length; i++) {
      ButtonObject bo = btnAddChild[i];
      if (bo.getTag().startsWith(billType))
        return bo;
    }
    throw new BusinessException("Not found billtype:" + billType + "'s Add Btn");
  }

  public static void retBusinessBtn(ButtonObject inoutBoBusiness, String corpId, String billType)
  {
    try
    {
      BusitypeVO[] busiTypeVos = getBusiByCorpAndBill(corpId, billType);
      inoutBoBusiness.removeAllChildren();
      if ((busiTypeVos == null) || (busiTypeVos.length == 0)) {
        return;
      }
      ButtonObject btnBusitype = null;
      String strOldCorp = busiTypeVos[0].getPk_corp();

      String strCurrCorp = null;
      for (int i = 0; i < busiTypeVos.length; i++) {
        strCurrCorp = busiTypeVos[i].getPk_corp();
        if (!strCurrCorp.equals(strOldCorp)) {
          btnBusitype = new ButtonObject("");
          btnBusitype.setSeperator(true);
          inoutBoBusiness.addChildButton(btnBusitype);
        }

        String showName = null;

        if ((showName == null) || (showName.trim().length() == 0))
          showName = busiTypeVos[i].getBusiname();
        btnBusitype = new ButtonObject(showName);

        btnBusitype.setPowerContrl(false);

        btnBusitype.setTag(busiTypeVos[i].getPrimaryKey());
        btnBusitype.setData(busiTypeVos[i]);
        inoutBoBusiness.addChildButton(btnBusitype);

        strOldCorp = strCurrCorp;
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  private static BusitypeVO[] getBusiByCorpAndBill(String corpId, String billType)
    throws BusinessException
  {
    BusitypeVO[] busitypeAll = PfUIDataCache.getBusiByCorpAndBill(corpId, billType);
    if ((busitypeAll == null) || (busitypeAll.length == 0))
      return null;
    String sUserid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
    IPFConfig ipf = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    PowerResultVO voPower = ipf.queryPowerBusiness(sUserid, corpId, billType);
    if (!voPower.isPowerControl())
      return busitypeAll;
    String[] sBusinessOfPower = voPower.getPowerId();
    if ((sBusinessOfPower == null) || (sBusinessOfPower.length == 0))
      return null;
    HashSet setBusitypeOfPower = new HashSet();
    for (int i = 0; i < sBusinessOfPower.length; i++) {
      setBusitypeOfPower.add(sBusinessOfPower[i]);
    }
    ArrayList listBusitype = new ArrayList();
    for (int i = 0; i < busitypeAll.length; i++) {
      if (setBusitypeOfPower.contains(busitypeAll[i].getPrimaryKey()))
        listBusitype.add(busitypeAll[i]);
    }
    return (BusitypeVO[])(BusitypeVO[])listBusitype.toArray(new BusitypeVO[0]);
  }

  public static void retElseBtn(ButtonObject boElse, String billType, String actionStyle)
  {
    try
    {
      PfUtilBillActionVO[] billActionVos = (PfUtilBillActionVO[])PfUIDataCache.getButtonByBillAndGrp(billType, actionStyle);

      boElse.removeAllChildren();

      ButtonObject btnChild = null;
      for (int i = 0; i < (billActionVos == null ? 0 : billActionVos.length); i++)
      {
        String showName = Pfi18nTools.i18nActionName(billActionVos[i].getPkBillType(), billActionVos[i].getActionName(), billActionVos[i].getActionNote());

        btnChild = new ButtonObject(showName, showName, billActionVos[i].getActionName());
        btnChild.setPowerContrl(false);
        btnChild.setTag(billActionVos[i].getActionName().trim());
        boElse.addChildButton(btnChild);
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  private static Object runAfterActionException(Container parent, PfUtilWorkFlowVO workFlow, String actionName, String billType, String currentDate, IPfRetException retEx, Object userObj)
    throws Exception
  {
    Object retObj = null;
    try
    {
      String procClassName = retEx.getProcClass();

      Object retValObj = retEx.getObject();

      Object retRunObj = runClass(procClassName);
      if ((retRunObj instanceof IRunClassAfterException)) {
        IRunClassAfterException runAfter = (IRunClassAfterException)retRunObj;
        runAfter.runClass(parent, retValObj);

        Object retProcObj = runAfter.getobj();

        retObj = processActionFlowInner(parent, actionName, billType, currentDate, (AggregatedValueObject)retProcObj, userObj, workFlow);
      }
    }
    catch (Exception ex) {
      throw ex;
    }
    return retObj;
  }

  private static Object runAfterBatchException(Container parent, PfUtilWorkFlowVO workFlow, String actionName, String billType, String currentDate, IPfRetException retEx, Object[] userObj)
    throws Exception
  {
    Object retObj = null;
    try
    {
      String procClassName = retEx.getProcClass();

      Object retValObj = retEx.getObject();

      Object retRunObj = runClass(procClassName);
      if ((retRunObj instanceof IRunClassAfterException)) {
        IRunClassAfterException runAfter = (IRunClassAfterException)retRunObj;
        runAfter.runClass(parent, retValObj);

        Object retProcObj = runAfter.getobj();

        retObj = processBatchFlowInner(parent, actionName, billType, currentDate, (AggregatedValueObject[])(AggregatedValueObject[])retProcObj, userObj, workFlow);
      }
    }
    catch (Exception ex) {
      throw ex;
    }
    return retObj;
  }

  private static Object runClass(String className)
    throws Exception
  {
    Object tmpObj = null;
    String errString = null;
    try
    {
      Class c = Class.forName(className);

      tmpObj = c.newInstance();
    } catch (ClassNotFoundException ex) {
      errString = "未找到在数据库注册表中所注册的类文件：" + className;
      throw new Exception(errString);
    } catch (InstantiationException ex) {
      errString = "不能实例化在数据库注册表中所注册的类文件：" + className;
      throw new Exception(errString);
    } catch (IllegalAccessException ex) {
      errString = "非法获取错误";
      throw new Exception(errString);
    } catch (IllegalArgumentException ex) {
      errString = "在同一类中不能存在方法名相同且参数个数一致的方法.";
      throw new Exception(errString);
    } catch (Exception ex) {
      errString = "其它错误!!!!!!!!!!!!!";
      throw new Exception(errString);
    }
    return tmpObj;
  }

  private static QueryConditionClient setConditionClient(String templateId, Container parent, boolean isRelationCorp, String pkOperator, String funNode)
  {
    QueryConditionClient querycond = new QueryConditionClient(parent);

    if (isRelationCorp) {
      querycond.setPlatForm(isRelationCorp);
      UIRefPane corpPane = new UIRefPane();
      corpPane.setRefNodeName("公司目录");
      String whereSQL = constructWherePart(pkOperator, funNode);
      corpPane.setWhereString(whereSQL);
      querycond.setValueRef("pk_corp", corpPane);
    } else {
      querycond.hideNormal();
    }
    querycond.setTempletID(templateId);
    return querycond;
  }

  private static String constructWherePart(String pkOperator, String funNode)
  {
    String baseSql = " where (isseal is null or isseal<>'Y') and ishasaccount='Y' ";

    IFuncPower ifp = (IFuncPower)NCLocator.getInstance().lookup(IFuncPower.class.getName());
    String[] strPkCorps = null;
    try {
      strPkCorps = ifp.queryCorpByUserAndFunc(pkOperator, funNode);
    } catch (BusinessException e) {
      Logger.error(e.getMessage(), e);
    }
    if ((strPkCorps == null) || (strPkCorps.length == 0))
      return baseSql;
    String whereSQL = baseSql + " and pk_corp in (";
    for (int i = 0; i < strPkCorps.length; i++) {
      whereSQL = whereSQL + "'" + strPkCorps[i] + "',";
    }
    whereSQL = whereSQL.substring(0, whereSQL.length() - 1) + ")";

    return whereSQL;
  }
}