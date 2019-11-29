package nc.bs.pub.pf.pfframe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.compiler.IWorkFlowRet;
import nc.bs.pub.compiler.IWorkflowBatch;
import nc.bs.pub.pf.IPfPersonFilter2;
import nc.bs.pub.pf.PfUtilDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.pub.pf.bservice.IPFActionConstrict;
import nc.bs.pub.pf.exception.WorkFlowException;
import nc.bs.pub.wfengine.impl.ApproveFlowManager;
import nc.impl.uap.pf.PFConfigImpl;
import nc.impl.uap.pf.PFMessageImpl;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.vo.pf.change.IChangeVOCheck;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.MessageinfoVO;
import nc.vo.pub.pf.IPFSourceBillFinder;
import nc.vo.pub.pf.IPfBackCheck2;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.SourceBillInfo;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.pfflow04.BackmsgVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.rbac.RoleVO;

public class PFBusiAction
  implements IPFBusiAction
{
  private IWorkflowMachine bpWorkFlow = null;

  private boolean startApproveFlowAfterAction(String billtype, AggregatedValueObject billVO, Object userObj, Object retObj, HashMap eParam, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn)
    throws BusinessException
  {
    Logger.debug("************尝试 启动审批流*************");
    boolean issend = true;
    boolean bStarted = false;
    if ((eParam != null) && ("nosendmessage".equals(eParam.get("nosendmessage")))) {
      issend = false;
    }
    String startupTrace = "************审批流不可启动*************";
    if (issend) {
      PfParameterVO paraVo = (PfParameterVO)hashBilltypeToParavo.get(billtype);
      fetchBillId(paraVo, billVO, retObj);

      if ((paraVo.m_billId != null) && (paraVo.m_billNo != null))
      {
        bStarted = sendWorkFlowOnSaveEx(billtype, billVO, userObj, eParam, hashBilltypeToParavo, hashMethodReturn);

        if (bStarted)
          startupTrace = "************审批流成功启动*************";
      }
      else
      {
        startupTrace = "************单据id或单据号为空,不能启动审批流*************";
      }
    }
    Logger.warn(startupTrace);
    return bStarted;
  }

  private void fetchBillId(PfParameterVO paravo, AggregatedValueObject billVO, Object retObj)
  {
    paravo.m_billId = getBillID(retObj);

    PublicHeadVO standHeadVo = new PublicHeadVO();
    if (billVO != null)
      getHeadInfo(standHeadVo, billVO, paravo.m_billType);
    paravo.m_billNo = standHeadVo.billNo;
    if (paravo.m_billId == null)
    {
      paravo.m_billId = standHeadVo.pkBillId;
    }
  }

  private void actionBeforeWorkFlow(PfParameterVO paraVo)
    throws PFBusinessException
  {
    String actionName = paraVo.m_actionName.toUpperCase();
    try {
      if (actionName.endsWith("UNAPPROVE"))
      {
        if (!(paraVo.m_preValueVo instanceof IPfBackCheck2))
        {
          ApproveFlowManager.hmParaVOs.put(paraVo.m_billId, paraVo);
          getBpWorkFlow().cancelCheckFlow(paraVo.m_billType, paraVo.m_billId, paraVo.m_operator);
        }
      } else if ((actionName.endsWith("DELETE")) || (actionName.endsWith("DISCARD")) || (actionName.endsWith("BLANKOUT")))
      {
        if ((paraVo.m_preValueVo instanceof IPfBackCheck2))
        {
          getBpWorkFlow().deleteCheckFlow(paraVo.m_billType, paraVo.m_billId, paraVo.m_operator, true);
        }
        else
          getBpWorkFlow().deleteCheckFlow(paraVo.m_billType, paraVo.m_billId, paraVo.m_operator, false);
      }
    }
    catch (BusinessException e)
    {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    }
    finally {
      ApproveFlowManager.hmParaVOs.remove(paraVo.m_billId);
    }
  }

  private boolean getLastStep(String pkBilltype, String actionName)
    throws PFBusinessException
  {
    boolean retflag = true;
    Logger.debug("****判断动作" + actionName + "是否结束动作getLastStep开始****");
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      retflag = dmo.queryLastStep(pkBilltype, actionName);
      Logger.debug("==" + (retflag ? "是" : "不是") + "结束动作==");
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    }
    Logger.debug("****判断动作" + actionName + "是否结束动作getLastStep结束****");
    return retflag;
  }

  private boolean sendWorkFlowOnSaveEx(String billtype, AggregatedValueObject vo, Object userObj, HashMap hmPfExParams, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn)
    throws BusinessException
  {
    PfParameterVO paraVo = (PfParameterVO)hashBilltypeToParavo.get(billtype);
    try {
      return getBpWorkFlow().sendWorkFlowOnSave(paraVo, hashMethodReturn, hmPfExParams);
    } catch (WorkFlowException e) {
      switch (e.getState()) {
      case -3:
        Logger.debug(e.getMessage());
        throw new PFBusinessException(e.getMessage(), e);
      case -4:
        return false;
      case 1:
        paraVo.m_autoApproveAfterCommit = true;

        actionOnStep("APPROVE", paraVo);

        actiondrive(vo, userObj, hashBilltypeToParavo, hashMethodReturn, paraVo);
        return true;
      }

      throw new PFBusinessException("非法的WorkFlowException状态标志:" + e.getState());
    } catch (BusinessException ex) {
    
    throw new PFBusinessException(ex.getMessage(), ex);
    }
  }

  private void getHeadInfo(PublicHeadVO headvo, AggregatedValueObject vo, String billtype)
  {
    headvo.billType = billtype;
    VotableVO attrvo = PfDataCache.getBillTypeToVO(billtype, true);
    if (attrvo == null) {
      Logger.error(">>>WARNING::未在VO对照表中进行注册VO");
      return;
    }if (vo.getParentVO() == null) {
      Logger.error(">>>WARNING::传入的单据聚合VO无主表");
      return;
    }

    if (attrvo.getApproveid() != null) {
      headvo.approveId = ((String)vo.getParentVO().getAttributeValue(attrvo.getApproveid()));
    }
    if (attrvo.getBillno() != null) {
      headvo.billNo = ((String)vo.getParentVO().getAttributeValue(attrvo.getBillno()));
    }
    if (attrvo.getBusitype() != null) {
      headvo.businessType = ((String)vo.getParentVO().getAttributeValue(attrvo.getBusitype()));
    }
    headvo.corpId = ((String)vo.getParentVO().getAttributeValue("pk_corp"));
    if (headvo.corpId == null) {
      headvo.corpId = ((String)vo.getParentVO().getAttributeValue("dwbm"));
    }
    if (attrvo.getOperator() != null) {
      headvo.operatorId = ((String)vo.getParentVO().getAttributeValue(attrvo.getOperator()));
    }
    if (attrvo.getBillid() != null)
      headvo.pkBillId = ((String)vo.getParentVO().getAttributeValue(attrvo.getBillid()));
  }

  private void getVariableValue(String billType, String actionName, String currentDate, AggregatedValueObject billvo, AggregatedValueObject[] billvos, Object userObj, Object[] userObjs, PfUtilWorkFlowVO workFlow, Hashtable hashBilltypeToParavo)
  {
    Logger.debug(">>>PFBusiAction.getVariableValue(" + actionName + "," + billType + ") START<<<");

    PfParameterVO paraVo = new PfParameterVO();
    paraVo.m_billType = billType;

    PublicHeadVO standHeadVo = new PublicHeadVO();
    if (billvo != null)
      getHeadInfo(standHeadVo, billvo, billType);
    else if ((billvos != null) && (billvos.length > 0)) {
      getHeadInfo(standHeadVo, billvos[0], billType);
    }

    if ((standHeadVo.businessType == null) || (standHeadVo.businessType.trim().length() == 0))
      paraVo.m_businessType = "KHHH0000000000000001";
    else {
      paraVo.m_businessType = standHeadVo.businessType;
    }

    paraVo.m_billNo = standHeadVo.billNo;
    paraVo.m_billId = standHeadVo.pkBillId;
    paraVo.m_coId = standHeadVo.corpId;
    paraVo.m_currentDate = currentDate;

    paraVo.m_makeBillOperator = standHeadVo.operatorId;
    paraVo.m_preValueVo = billvo;
    paraVo.m_preValueVos = billvos;
    paraVo.m_standHeadVo = standHeadVo;
    paraVo.m_userObj = userObj;
    paraVo.m_userObjs = userObjs;
    paraVo.m_workFlow = workFlow;

    if (actionName.length() > 20) {
      paraVo.m_actionName = actionName.substring(0, actionName.length() - 20);
      paraVo.m_operator = actionName.substring(actionName.length() - 20);
    } else {
      paraVo.m_actionName = actionName;
      paraVo.m_operator = standHeadVo.approveId;
    }

    if ((paraVo.m_operator == null) || (paraVo.m_operator.trim().length() == 0)) {
      paraVo.m_operator = paraVo.m_makeBillOperator;
    }

    Logger.debug(">>>billType=" + paraVo.m_billType + " busiType=" + paraVo.m_businessType);
    Logger.debug(">>>billMaker=" + paraVo.m_makeBillOperator + " operator=" + paraVo.m_operator);
    Logger.debug(">>>corpPK=" + paraVo.m_coId + " billId=" + paraVo.m_billId);
    Logger.debug(">>>actionName=" + paraVo.m_actionName + " billNo=" + paraVo.m_billNo);

    hashBilltypeToParavo.put(billType, paraVo);

    Logger.debug(">>>PFBusiAction.getVariableValue(" + actionName + "," + billType + ") END<<<");
  }

  private String getBillID(Object tmpObj)
  {
    Logger.debug("****从动作脚本执行后的返回值中获得单据Id开始****");
    String billId = null;
    if (tmpObj == null) return billId;

    if ((tmpObj instanceof ArrayList)) {
      ArrayList alRetObj = (ArrayList)tmpObj;
      Object[] retObjAry = alRetObj.toArray();
      if ((retObjAry.length > 1) && ((retObjAry[1] instanceof ArrayList))) {
        billId = ((ArrayList)retObjAry[1]).toArray()[0].toString();
        Logger.debug("获得单据Id:Array的第一维的数组的第0维为单据ID");
      }
      else if ((retObjAry[0] != null) && (!retObjAry[0].equals(""))) {
        billId = retObjAry[0].toString();
        Logger.debug("获得单据Id:Array的第0维为单据ID");
      }
    }
    else if ((tmpObj instanceof String)) {
      billId = (String)tmpObj;
      Logger.debug("直接从对象String取的ID");
    }
    Logger.debug("****从动作脚本执行后的返回值中获得单据Id=" + billId + "结束****");
    return billId;
  }

  private Object actionOnStep(String actionName, PfParameterVO paraVo)
    throws BusinessException
  {
    Logger.debug(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") START<<<");

    Object actionReturnObj = null;

    if ((paraVo.m_billId == null) && (paraVo.m_preValueVo != null)) {
      try {
        paraVo.m_billId = paraVo.m_preValueVo.getParentVO().getPrimaryKey();
      } catch (BusinessException e) {
        Logger.error(e.getMessage(), e);
        throw new PFBusinessException(e.getMessage(), e);
      }
      Logger.debug("*********单据驱动保存、审核(获得驱动时单据主键)*****");
    }
    actionReturnObj = new PFRunClass().runComBusi(paraVo, UFBoolean.FALSE, actionName);

    Logger.info(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") END<<<");

    return actionReturnObj;
  }

  private boolean actiondrive(AggregatedValueObject srcBillVO, Object userObj, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn, PfParameterVO paraVo)
    throws BusinessException
  {
    Logger.debug("*********执行动作驱动actiondrive开始********");

    PfUtilActionVO[] drivedActions = getActionDriveVOs(paraVo);
    String srcBilltype = paraVo.m_billType;

    if ((drivedActions == null) || (drivedActions.length == 0)) {
      return false;
    }

    Hashtable execDriveActionsHas = new Hashtable();

    LinkedHashSet billTypesStartedFlow = new LinkedHashSet();

    AggregatedValueObject destVo = null;

    String srcOperator = paraVo.m_operator;

    for (int i = 0; i < drivedActions.length; i++)
    {
      String destBillType = drivedActions[i].getBillType();

      String beDrivedActionName = drivedActions[i].getActionName();

      String currentExecDrive = destBillType + ":" + beDrivedActionName;
      Logger.debug("执行驱动单据动作:" + currentExecDrive + "开始");
      if (execDriveActionsHas.containsKey(currentExecDrive)) {
        Logger.debug("单据动作:" + currentExecDrive + "已执行,继续循环操作.");
      }
      else {
        execDriveActionsHas.put(currentExecDrive, currentExecDrive);

        Object tmpObj = null;
        if (!billTypesStartedFlow.contains(destBillType))
        {
          if (hashBilltypeToParavo.containsKey(destBillType)) {
            Logger.debug("执行单据[" + currentExecDrive + "]驱动");
          } else {
            Logger.debug("不存在被驱动单据VO,则进行以源单据为准的数据转换");

            Object checkClzInstance = PfUtilTools.getBizRuleImpl(srcBilltype);
            if ((checkClzInstance instanceof IChangeVOCheck)) {
              boolean bValid = ((IChangeVOCheck)checkClzInstance).checkValidOrNeed(srcBillVO, destBillType, beDrivedActionName);

              if (!bValid) {
                Logger.debug("源单据VO不允许数据转换，则继续下个驱动");
                continue;
              }

            }

            destVo = PfUtilTools.runChangeData(drivedActions[i].getDriveBillType(), destBillType, srcBillVO, paraVo);

            Logger.debug("获得单据:" + destBillType + "的数据交换VO完成");

            if (destVo == null)
            {
              Logger.warn(">交换到的单据VO为空，则继续下个驱动");
              continue;
            }

            AggregatedValueObject[] driveVos = PfUtilTools.pfInitVosClass(destVo.getClass().getName());

            driveVos[0] = destVo;

            Object[] driveObjs = null;
            if (userObj != null) {
              driveObjs = (Object[])(Object[])Array.newInstance(userObj.getClass(), 1);

              driveObjs[0] = userObj;
            }
            Logger.debug("进行单据:" + destBillType + "的数据数组VO[0]完成");

            getVariableValue(destBillType, drivedActions[i].getActionName(), paraVo.m_currentDate, destVo, driveVos, userObj, driveObjs, null, hashBilltypeToParavo);

            PfParameterVO tempPara = (PfParameterVO)hashBilltypeToParavo.get(destBillType);
            tempPara.m_splitValueVos = driveVos;

            if (tempPara.m_operator == null) {
              tempPara.m_operator = srcOperator;
            }
          }

          tmpObj = actionOnStep(beDrivedActionName, (PfParameterVO)hashBilltypeToParavo.get(destBillType));

          boolean bFlowStarted = false;
          if ((beDrivedActionName.toUpperCase().endsWith("SAVE")) || (beDrivedActionName.toUpperCase().endsWith("EDIT")))
          {
            bFlowStarted = startApproveFlowAfterAction(destBillType, destVo, userObj, tmpObj, null, hashBilltypeToParavo, hashMethodReturn);

            if (bFlowStarted) {
              billTypesStartedFlow.add(destBillType);
            }
          }

          if ((!bFlowStarted) && (!srcBilltype.equals(destBillType))) {
            insertPushWorkitems((PfParameterVO)hashBilltypeToParavo.get(destBillType), srcBilltype, destBillType, tmpObj);
          }
        }
        Logger.debug("***执行驱动单据动作:" + currentExecDrive + "结束***");
      }
    }
    Logger.debug("*********执行动作驱动actiondrive结束********");
    return true;
  }

  private void insertPushWorkitems(PfParameterVO paravo, String srcBillType, String destBillType, Object retObj)
  {
    Logger.debug(">>发送推式消息=" + destBillType + "开始");
    String pkcorp = paravo.m_coId;
    String pk_busitype = paravo.m_businessType;
    String senderman = paravo.m_operator;

    BillbusinessVO condVO = new BillbusinessVO();
    condVO.setPk_corp(pkcorp);
    condVO.setPk_businesstype(pk_busitype);
    condVO.setPk_billtype(srcBillType);

    BaseDAO dao = new BaseDAO();
    try {
      Collection co = dao.retrieve(condVO, true);
      if (co.size() > 0) {
        BillbusinessVO vo = (BillbusinessVO)co.iterator().next();
        UFBoolean isMsg = vo.getForwardmsgflag();
        if ((isMsg == null) || (!isMsg.booleanValue())) {
          Logger.debug(">>源单据" + srcBillType + "不可发送下游消息，返回");
          return;
        }
      }
    } catch (DAOException ex) {
      Logger.error(ex.getMessage(), ex);
      return;
    }

    if ((paravo.m_splitValueVos == null) || (paravo.m_splitValueVos.length == 0)) {
      return;
    }
    Logger.debug(">>推式消息，分单数=" + paravo.m_splitValueVos.length);

    Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
    IPfPersonFilter2 filter = null;
    if ((checkClzInstance instanceof IPfPersonFilter2)) {
      filter = (IPfPersonFilter2)checkClzInstance;
    }
    for (int k = 0; k < paravo.m_splitValueVos.length; k++)
    {
      AggregatedValueObject billvo = paravo.m_splitValueVos[k];

      fetchBillId(paravo, billvo, retObj);
      try
      {
        PFConfigImpl pfcfg = new PFConfigImpl();
        RoleVO[] roles = pfcfg.queryRolesHasBillbusi(pkcorp, destBillType, pk_busitype, true);
        if ((roles == null) || (roles.length == 0)) {
          Logger.debug(">>单据无参与角色，返回");
          return;
        }

        HashSet hsUserPKs = null;

        if (filter == null)
        {
          hsUserPKs = new HashSet();
          IRoleManageQuery rmq = (IRoleManageQuery)NCLocator.getInstance().lookup(IRoleManageQuery.class.getName());

          for (int i = 0; i < (roles == null ? 0 : roles.length); i++) {
            UserVO[] users = rmq.getUsers(roles[i].getPk_role(), roles[i].getPk_corp());
            for (int j = 0; j < (users == null ? 0 : users.length); j++)
              hsUserPKs.add(users[j].getPrimaryKey());
          }
        }
        else
        {
          hsUserPKs = filter.filterUsers(srcBillType, destBillType, billvo, roles);
        }

        ArrayList alItems = new ArrayList();
        for (Iterator i = hsUserPKs.iterator(); i.hasNext();) {
        	String userId = (String)i.next();
          MessageinfoVO wi = new MessageinfoVO();
          wi.setPk_billtype(destBillType);
          wi.setPk_srcbilltype(srcBillType);
          wi.setBillid(paravo.m_billId);
          wi.setBillno(paravo.m_billNo);
          wi.setCheckman(userId);

          wi.setTitle(Pfi18nTools.i18nBilltypeName(srcBillType, null) + "推式产生新单据：" + Pfi18nTools.i18nBilltypeName(destBillType, null) + paravo.m_billNo + "，请处理");

          wi.setPk_busitype(pk_busitype);
          wi.setPk_corp(pkcorp);
          wi.setSenderman(senderman);
          alItems.add(wi);
        }

        new PFMessageImpl().insertPushOrPullMsgs((MessageinfoVO[])(MessageinfoVO[])alItems.toArray(new MessageinfoVO[alItems.size()]), 3);
      }
      catch (Exception e)
      {
        Logger.error(e.getMessage(), e);
      }
    }
    Logger.debug(">>发送推式消息=" + destBillType + "结束");
  }

  private PfUtilActionVO[] getActionDriveVOs(PfParameterVO paraVo)
    throws PFBusinessException
  {
    PfUtilActionVO[] driveActions = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      driveActions = dmo.queryDriveAction(paraVo.m_billType, paraVo.m_businessType, paraVo.m_coId, paraVo.m_actionName, paraVo.m_operator);
    }
    catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage(), e);
    }
    return driveActions;
  }

  public Object processAction(String actionName, String billType, String currentDate, PfUtilWorkFlowVO workFlow, AggregatedValueObject billvo, Object userObj, HashMap eParam)
    throws BusinessException
  {
    Logger.init("workflow");
    Logger.debug(">>>PFBusiAction.processAction(" + actionName + "," + billType + ") START<<<");

    Object retObj = null;

    AggregatedValueObject[] inVos = null;
    if (billvo != null) {
      inVos = PfUtilTools.pfInitVosClass(billvo.getClass().getName());
      inVos[0] = billvo;
    }

    Hashtable hashBilltypeToParavo = new Hashtable();
    Hashtable hashMethodReturn = new Hashtable();

    getVariableValue(billType, actionName, currentDate, billvo, inVos, userObj, null, workFlow, hashBilltypeToParavo);

    PfParameterVO paraVoOfBilltype = (PfParameterVO)hashBilltypeToParavo.get(billType);
    actionBeforeWorkFlow(paraVoOfBilltype);

    new PFActionConstrict().actionConstrict(paraVoOfBilltype, hashMethodReturn);

    retObj = actionOnStep(paraVoOfBilltype.m_actionName, paraVoOfBilltype);

    if ((retObj instanceof IWorkFlowRet))
    {
      return ((IWorkFlowRet)retObj).m_inVo;
    }

    try
    {
      Object[] tmpObj = (Object[])(Object[])retObj;
      Hashtable hasNoProc = null;

      if ((tmpObj != null) && (tmpObj.length > 0) && ((tmpObj[0] instanceof IWorkflowBatch))) {
        IWorkflowBatch wfBatch = (IWorkflowBatch)tmpObj[0];
        hasNoProc = wfBatch.getNoPassAndGoing();
        Object[] userObjs = (Object[])(Object[])wfBatch.getUserObj();
        retObj = userObjs[0];
      }
      if ((hasNoProc != null) && (hasNoProc.containsKey("0"))) {
        return null;
      }
    }
    catch (Exception e)
    {
    }
    backMsg(paraVoOfBilltype);

    if (getLastStep(billType, paraVoOfBilltype.m_actionName)) {
      actiondrive(billvo, userObj, hashBilltypeToParavo, hashMethodReturn, paraVoOfBilltype);
    }

    if ((paraVoOfBilltype.m_actionName.toUpperCase().endsWith("SAVE")) || (paraVoOfBilltype.m_actionName.toUpperCase().endsWith("EDIT")))
    {
      startApproveFlowAfterAction(paraVoOfBilltype.m_billType, billvo, userObj, retObj, eParam, hashBilltypeToParavo, hashMethodReturn);
    }

    Logger.debug(">>>PFBusiAction.processAction(" + actionName + "," + billType + ") END<<<");
    return retObj;
  }

  private void backMsg(PfParameterVO paravo)
  {
    Logger.debug(">>上游消息处理=" + paravo.m_billType + "开始");

    BaseDAO dao = new BaseDAO();
    try {
      BillbusinessVO condVO = new BillbusinessVO();
      condVO.setPk_corp(paravo.m_coId);
      condVO.setPk_businesstype(paravo.m_businessType);
      condVO.setPk_billtype(paravo.m_billType);
      Collection co = dao.retrieve(condVO, true);
      if (co.size() > 0) {
        BillbusinessVO vo = (BillbusinessVO)co.iterator().next();
        UFBoolean isMsg = vo.getBackmsgflag();
        if ((isMsg == null) || (!isMsg.booleanValue())) {
          Logger.debug(">>单据" + paravo.m_billType + "不可发送上游消息，返回");
          return;
        }
      }
    } catch (DAOException ex) {
      Logger.error(ex.getMessage(), ex);
      return;
    }

    try
    {
      BackmsgVO condVO = new BackmsgVO();

      condVO.setPk_busitype(paravo.m_businessType);
      condVO.setPk_srcbilltype(paravo.m_billType);
      condVO.setActiontype(paravo.m_actionName);
      condVO.setIsapprover(null);
      condVO.setIsbillmaker(null);
      Collection coBackmsg = dao.retrieve(condVO, true);
      if (coBackmsg.size() == 0) {
        Logger.debug(">>单据" + paravo.m_billType + "没有进行上游消息配置，返回");
        return;
      }

      BilltypeVO billVo = PfDataCache.getBillTypeInfo(paravo.m_billType);
      if ((billVo.getCheckclassname() != null) && (billVo.getCheckclassname().trim().length() != 0)) {
        Object obj = PfUtilTools.instantizeObject(billVo.getPrimaryKey(), billVo.getCheckclassname().trim());
        IPFSourceBillFinder srcFinder;
        Iterator iter;
        if ((obj instanceof IPFSourceBillFinder)) {
          srcFinder = (IPFSourceBillFinder)obj;
          for (iter = coBackmsg.iterator(); iter.hasNext(); )
          {
            executeBackmsgs(srcFinder, (BackmsgVO)iter.next(), paravo);
          }
        } else {
          Logger.debug(">>单据" + paravo.m_billType + "的审批流检查类没有实现接口IPFSourceBillFinder，返回");
          return;
        }
      } else {
        Logger.debug(">>单据" + paravo.m_billType + "没有注册审批流检查类，返回");
        return;
      }
    } catch (Exception ex) {
      Logger.error(ex.getMessage(), ex);
    }
  }

  private void executeBackmsgs(IPFSourceBillFinder srcFinder, BackmsgVO backmsgVO, PfParameterVO paravo)
    throws BusinessException
  {
    Logger.debug(">>给上游单据" + backmsgVO.getPk_destbilltype() + "发送上游消息 开始");
    SourceBillInfo[] infos = srcFinder.findSourceBill(backmsgVO.getPk_destbilltype(), paravo.m_preValueVo);

    HashSet hsBillmakers = new HashSet();
    HashSet hsApprovers = new HashSet();
    for (int i = 0; i < (infos == null ? 0 : infos.length); i++) {
      hsBillmakers.add(infos[i].getBillmaker());
      hsApprovers.add(infos[i].getApprover());
    }

    String msgContent = "下游单据：" + Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + " " + paravo.m_billNo + "进行了单据动作：" + Pfi18nTools.i18nActionName(paravo.m_billType, paravo.m_actionName, null) + "(" + paravo.m_actionName + ")" + "的处理，请查看";

    ArrayList alItems = new ArrayList();
    if ((backmsgVO.getIsbillmaker().booleanValue()) && (backmsgVO.getIsapprover().booleanValue())) {
      hsBillmakers.addAll(hsApprovers);
      constructBackmsgs(paravo, hsBillmakers, msgContent, alItems);
    } else if (backmsgVO.getIsbillmaker().booleanValue()) {
      constructBackmsgs(paravo, hsBillmakers, msgContent, alItems);
    } else if (backmsgVO.getIsapprover().booleanValue()) {
      constructBackmsgs(paravo, hsApprovers, msgContent, alItems);
    }

    new PFMessageImpl().insertPushOrPullMsgs((MessageinfoVO[])(MessageinfoVO[])alItems.toArray(new MessageinfoVO[alItems.size()]), 5);

    Logger.debug(">>给上游单据" + backmsgVO.getPk_destbilltype() + "发送上游消息 结束");
  }

  private void constructBackmsgs(PfParameterVO paravo, HashSet<String> hsReceivers, String msgContent, ArrayList<MessageinfoVO> alItems)
  {
    for (String receiver : hsReceivers) {
      MessageinfoVO wi = new MessageinfoVO();
      wi.setPk_billtype(paravo.m_billType);

      wi.setBillid(paravo.m_billId);
      wi.setBillno(paravo.m_billNo);
      wi.setCheckman(receiver);
      wi.setTitle(msgContent);
      wi.setPk_busitype(paravo.m_businessType);
      wi.setPk_corp(paravo.m_coId);
      wi.setSenderman(paravo.m_operator);
      alItems.add(wi);
    }
  }

  public Object[] processBatch(String actionName, String billType, String currentDate, AggregatedValueObject[] billvos, Object[] userObjAry, PfUtilWorkFlowVO workFlow)
    throws BusinessException
  {
    Logger.init("workflow");
    Logger.debug(">>>PFBusiAction.processBatch(" + actionName + "," + billType + ") START<<<");

    Object[] retObjs = null;

    if (billvos == null) {
      return retObjs;
    }

    IPFActionConstrict aConstrict = new PFActionConstrict();

    Hashtable hashBilltypeToParavo = new Hashtable();
    Hashtable hashMethodReturn = new Hashtable();
    for (int i = 0; i < billvos.length; i++)
    {
      if ((userObjAry != null) && (userObjAry.length >= 1))
      {
        getVariableValue(billType, actionName, currentDate, billvos[i], billvos, userObjAry[i], userObjAry, workFlow, hashBilltypeToParavo);
      }
      else
      {
        getVariableValue(billType, actionName, currentDate, billvos[i], billvos, null, null, workFlow, hashBilltypeToParavo);
      }

      PfParameterVO paraOfThisBill = (PfParameterVO)hashBilltypeToParavo.get(billType);

      actionBeforeWorkFlow(paraOfThisBill);

      aConstrict.actionConstrict(paraOfThisBill, hashMethodReturn);
    }
    PfParameterVO paravoOfLastSrcBill = (PfParameterVO)hashBilltypeToParavo.get(billType);
    paravoOfLastSrcBill.m_preValueVo = null;

    retObjs = (Object[])(Object[])actionOnStep(paravoOfLastSrcBill.m_actionName, paravoOfLastSrcBill);

    Hashtable hasNoProc = null;

    if ((retObjs != null) && (retObjs.length > 0) && ((retObjs[0] instanceof IWorkflowBatch))) {
      IWorkflowBatch wfBatch = (IWorkflowBatch)retObjs[0];
      hasNoProc = wfBatch.getNoPassAndGoing();
      retObjs = (Object[])(Object[])wfBatch.getUserObj();
    }

    if (hasNoProc == null) {
      hasNoProc = new Hashtable();
    }

    AggregatedValueObject[] beforeVos = paravoOfLastSrcBill.m_preValueVos;
    for (int i = 0; (beforeVos != null) && (i < beforeVos.length); i++)
    {
      if (beforeVos[i] == null)
      {
        continue;
      }

      if (hasNoProc.containsKey(String.valueOf(i)))
      {
        continue;
      }

      Object tmpActionObj = null;
      if ((userObjAry != null) && (userObjAry.length != 0)) {
        tmpActionObj = userObjAry[i];
      }

      if ((userObjAry != null) && (userObjAry.length >= 1))
      {
        getVariableValue(billType, actionName, currentDate, beforeVos[i], beforeVos, tmpActionObj, userObjAry, workFlow, hashBilltypeToParavo);
      }
      else
      {
        getVariableValue(billType, actionName, currentDate, beforeVos[i], beforeVos, null, null, workFlow, hashBilltypeToParavo);
      }

      backMsg((PfParameterVO)hashBilltypeToParavo.get(billType));

      String strActionNameOfPara = paravoOfLastSrcBill.m_actionName;
      if (getLastStep(billType, strActionNameOfPara))
      {
        Object o = hashBilltypeToParavo.get(billType);
        hashBilltypeToParavo = new Hashtable();
        hashBilltypeToParavo.put(billType, o);
        actiondrive(beforeVos[i], tmpActionObj, hashBilltypeToParavo, hashMethodReturn, paravoOfLastSrcBill);
      }

      if ((!strActionNameOfPara.toUpperCase().endsWith("SAVE")) && (!strActionNameOfPara.toUpperCase().endsWith("EDIT"))) {
        continue;
      }
      startApproveFlowAfterAction(paravoOfLastSrcBill.m_billType, beforeVos[i], tmpActionObj, retObjs == null ? null : retObjs[i], null, hashBilltypeToParavo, hashMethodReturn);
    }

    Logger.debug(">>>PFBusiAction.processBatch(" + actionName + "," + billType + ") END<<<");
    return retObjs;
  }

  public IWorkflowMachine getBpWorkFlow()
  {
    return this.bpWorkFlow;
  }

  public void setBpWorkFlow(IWorkflowMachine bpWorkFlow) {
    this.bpWorkFlow = bpWorkFlow;
  }
}