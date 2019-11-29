package nc.bs.ia.boundary.fip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dap.rtvouch.RtVouchException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.dap.pub.IDapSendMessage;
import nc.itf.dmp.pub.IDmpSendMessage;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.vouchtemp.RetVoucherVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ExAggregatedVO;
import nc.vo.pub.lang.UFDouble;

public class FIPVoucher
{
  public void createRealtimeVoucher(IaInoutledgerVO[] ledgerVOs, boolean dapStarted, boolean dmpStarted)
  {
    initExtraInfo(ledgerVOs);
    int length = ledgerVOs.length;
    String pk_corp = ledgerVOs[0].getPk_corp();
    String basCurID = getBasicCurID(pk_corp);
    DapMsgVO[] messages = new DapMsgVO[length];
    for (int i = 0; i < length; i++) {
      DapMsgVO message = createAddMessage(ledgerVOs[i], basCurID);
      messages[i] = message;
    }
    if (dapStarted) {
      RetVoucherVO[] vos = addToDAP(messages, ledgerVOs);
      if (vos.length > 0) {
        saveVoucherIDToDetailAccount(vos, ledgerVOs);
      }
    }
    if (dmpStarted)
      addToDMP(messages, ledgerVOs);
  }

  private DapMsgVO createAddMessage(IaInoutledgerVO ledgerVO, String basCurID)
  {
    DapMsgVO message = new DapMsgVO();

    message.setCorp(ledgerVO.getPk_corp());

    message.setSys("IA");

    message.setProc(ledgerVO.getCbilltypecode());

    message.setBusiName(ledgerVO.getVdispatchname());

    if (ledgerVO.getCsumrtvouchid() != null) {
      message.setProcMsg(ledgerVO.getCsumrtvouchid());
    }
    else
    {
      message.setProcMsg(ledgerVO.getCbill_bid());
    }
    if (message.getProcMsg() == null) {
      String tip = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000013");

      ExceptionUtils.wrappBusinessException(tip);
    }

    message.setBillCode(ledgerVO.getVbillcode());

    message.setBusiDate(ledgerVO.getDbilldate());

    message.setBusiType(ledgerVO.getCdispatchid());

    if (ledgerVO.getCauditorid() != null) {
      message.setChecker(ledgerVO.getCauditorid());
    }
    else
    {
      message.setChecker(ledgerVO.getCoperatorid());
    }

    message.setComment(ledgerVO.getVnote());

    if (ledgerVO.getCauditorid() != null) {
      message.setOperator(ledgerVO.getCauditorid());
    }
    else
    {
      message.setOperator(ledgerVO.getCoperatorid());
    }

    message.setCurrency(basCurID);
    UFDouble money = ledgerVO.getNmoney();
    money = money != null ? money : ledgerVO.getNplanedmny();

    message.setMoney(money);

    message.setMsgType(0);

    message.setRequestNewTranscation(false);

    return message;
  }

  private RetVoucherVO[] addToDAP(DapMsgVO[] messages, IaInoutledgerVO[] ledgerVOs)
  {
    IDapSendMessage bo = (IDapSendMessage)NCLocator.getInstance().lookup(IDapSendMessage.class.getName());

    int length = ledgerVOs.length;
    List list = new ArrayList();
    for (int i = 0; i < length; i++) {
      BillVO bill = ledgerVOs[i].changeViewToBill();
      RetVoucherVO vo = null;
      try {
        vo = bo.sendMessage(messages[i], bill);
      }
      catch (BusinessException ex) {
        ex.printStackTrace();
        Throwable cause = ExceptionUtils.unmarsh(ex);
        if ((cause instanceof RtVouchException)) {
          String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000014");

          message = message + cause.getMessage();
          message = message + "\r\n";
          message = message + "单据号：" + messages[i].getBillCode();
          message = message + "\r\n";
          message = message + "单据类型：" + messages[i].getProc();
          message = message + "\r\n";
          message = message + "业务类型名称：" + messages[i].getBusiName();
          message = message + "\r\n";
          message = message + "存货编码：" + ledgerVOs[i].getCinventorycode();

          ExceptionUtils.wrappBusinessException(message, cause);
        }
        else {
          ExceptionUtils.wrappException(ex);
        }
      }
      if (vo != null) {
        list.add(vo);
      }
    }
    int size = list.size();
    RetVoucherVO[] vos = new RetVoucherVO[size];
    vos = (RetVoucherVO[])(RetVoucherVO[])list.toArray(vos);
    return vos;
  }

  private void addToDMP(DapMsgVO[] messages, IaInoutledgerVO[] ledgerVOs)
  {
    IDmpSendMessage bo = (IDmpSendMessage)NCLocator.getInstance().lookup(IDmpSendMessage.class.getName());

    int length = ledgerVOs.length;
    for (int i = 0; i < length; i++) {
      String departID = ledgerVOs[i].getCdeptid();
      String projectID = ledgerVOs[i].getCprojectid();
      if (departID == null) {
        continue;
      }
      if (projectID == null) {
        continue;
      }
      BillVO bill = ledgerVOs[i].changeViewToBill();
      try
      {
        bo.sendMessage(messages[i], new ExAggregatedVO(bill));
      }
      catch (BusinessException ex) {
        ex.printStackTrace();
        String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000015");

        message = message + "\r\n";
        message = message + "单据号：" + messages[i].getBillCode();
        message = message + "\r\n";
        message = message + "单据类型：" + messages[i].getProc();
        message = message + "\r\n";
        message = message + "业务类型名称：" + messages[i].getBusiName();
        message = message + "\r\n";
        message = message + "存货编码：" + ledgerVOs[i].getCinventorycode();

        Throwable cause = ExceptionUtils.unmarsh(ex);
        message = message + cause.getMessage();
        ExceptionUtils.wrappBusinessException(message, cause);
      }
    }
  }

  private void saveVoucherIDToDetailAccount(RetVoucherVO[] vos, IaInoutledgerVO[] ledgerVOs)
  {
    int length = vos.length;
    String[][] data = new String[length][3];
    for (int i = 0; i < length; i++) {
      String voucherID = vos[i].m_voucherPk;
      String csumrtvouchid = ledgerVOs[i].getCsumrtvouchid();
      String cbill_bid = ledgerVOs[i].getCbill_bid();
      String id = csumrtvouchid != null ? csumrtvouchid : cbill_bid;
      data[0][0] = id;
      data[0][1] = voucherID;
    }
    TempTableDefine define = new TempTableDefine();
    String temptable = define.getUpdateUniteVoucher(data);

    SqlBuilder sql = new SqlBuilder();
    sql.append(" update ia_bill_b set cvoucherid=b.csumrtvouchid from ");
    sql.append(temptable);
    sql.append(" b where ");
    sql.startParentheses();
    sql.append(" ia_bill_b.cbill_bid = b.cbill_bid");
    sql.endParentheses();
    sql.append(" or ");
    sql.startParentheses();
    sql.append(" ia_bill_b.csumrtvouchid = b.cbill_bid  ");
    sql.endParentheses();
  }

  public void deleteRealtimeVoucher(IaInoutledgerVO[] ledgerVOs, boolean dapStarted, boolean dmpStarted)
  {
    int length = ledgerVOs.length;
    DapMsgVO[] messages = new DapMsgVO[length];
    for (int i = 0; i < length; i++) {
      DapMsgVO message = createDeleteMessage(ledgerVOs[i]);
      messages[i] = message;
    }
    if (dapStarted) {
      removeFromDAP(messages);
    }
    if (dmpStarted)
      removeFromDMP(messages);
  }

  private DapMsgVO createDeleteMessage(IaInoutledgerVO ledgerVO)
  {
    DapMsgVO message = new DapMsgVO();

    message.setCorp(ledgerVO.getPk_corp());

    message.setSys("IA");

    message.setProc(ledgerVO.getCbilltypecode());

    if (ledgerVO.getCsumrtvouchid() != null) {
      message.setProcMsg(ledgerVO.getCsumrtvouchid());
    }
    else
    {
      message.setProcMsg(ledgerVO.getCbill_bid());
    }
    if (message.getProcMsg() == null) {
      String tip = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000013");

      ExceptionUtils.wrappBusinessException(tip);
    }

    message.setBillCode(ledgerVO.getVbillcode());

    message.setMsgType(1);

    message.setRequestNewTranscation(false);

    return message;
  }

  private void removeFromDAP(DapMsgVO[] messages)
  {
    IDapSendMessage bo = (IDapSendMessage)NCLocator.getInstance().lookup(IDapSendMessage.class.getName());

    int length = messages.length;
    for (int i = 0; i < length; i++)
      try {
        bo.sendMessage(messages[i], null);
      }
      catch (BusinessException ex) {
        ex.printStackTrace();
        String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000014");

        message = message + ex.getMessage();
        ExceptionUtils.wrappBusinessException(message);
      }
  }

  private void removeFromDMP(DapMsgVO[] messages)
  {
    IDmpSendMessage bo = (IDmpSendMessage)NCLocator.getInstance().lookup(IDmpSendMessage.class.getName());

    int length = messages.length;
    for (int i = 0; i < length; i++)
      try {
        bo.sendMessage(messages[i], null);
      }
      catch (BusinessException ex) {
        ex.printStackTrace();
        String message = NCLangResOnserver.getInstance().getStrByID("2014", "UPP2014-000015");

        message = message + ex.getMessage();
        ExceptionUtils.wrappBusinessException(message);
      }
  }

  private void initExtraInfo(IaInoutledgerVO[] ledgerVOs)
  {
    Map dispatchNameIndex = new HashMap();

    Map customBaseIDIndex = new HashMap();

    Map departmentNameIndex = new HashMap();

    Map departmentAttributeIndex = new HashMap();

    Map invclassIndex = new HashMap();

    Map invcodeIndex = new HashMap();

    Map invnameIndex = new HashMap();

    Map invtypeIndex = new HashMap();

    Map jobcodeIndex = new HashMap();

    Map jobnameIndex = new HashMap();

    int length = ledgerVOs.length;
    for (int i = 0; i < length; i++) {
      initDispatchName(ledgerVOs[i], dispatchNameIndex);
      initCustomBaseID(ledgerVOs[i], customBaseIDIndex);
      initDepartment(ledgerVOs[i], departmentNameIndex, departmentAttributeIndex);

      initInv(ledgerVOs[i], invclassIndex, invcodeIndex, invnameIndex, invtypeIndex);

      initJob(ledgerVOs[i], jobcodeIndex, jobnameIndex);
    }
  }

  private void initDispatchName(IaInoutledgerVO ledgerVO, Map index)
  {
    String cdispatchid = ledgerVO.getCdispatchid();
    if (cdispatchid == null) {
      return;
    }
    if (ledgerVO.getVdispatchname() != null) {
      return;
    }
    String rdname = null;

    if (index.containsKey(cdispatchid)) {
      rdname = (String)index.get(cdispatchid);
      ledgerVO.setVdispatchname(rdname);
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" select rdname from bd_rdcl where ");
    sql.append(" pk_rdcl", cdispatchid);
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      rdname = rowset.getString(0);
    }
    ledgerVO.setVdispatchname(rdname);
    index.put(cdispatchid, rdname);
  }

  private void initCustomBaseID(IaInoutledgerVO ledgerVO, Map index)
  {
    String customvendorid = ledgerVO.getCcustomvendorid();
    if (customvendorid == null) {
      return;
    }
    if (ledgerVO.getCcustombasid() != null) {
      return;
    }
    String custombasid = null;

    if (index.containsKey(customvendorid)) {
      custombasid = (String)index.get(customvendorid);
      ledgerVO.setCcustombasid(custombasid);
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" select pk_cubasdoc from bd_cumandoc where ");
    sql.append(" pk_cumandoc", customvendorid);
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      custombasid = rowset.getString(0);
    }
    ledgerVO.setCcustombasid(custombasid);
    index.put(customvendorid, custombasid);
  }

  private void initDepartment(IaInoutledgerVO ledgerVO, Map departmentCodeIndex, Map departmentAttributeIndex)
  {
    String departID = ledgerVO.getCdeptid();
    if (departID == null) {
      return;
    }
    if ((ledgerVO.getCdeptcode() != null) && (ledgerVO.getIdeptattr() != null))
    {
      return;
    }
    String departcode = null;
    Integer departattribute = null;

    if (departmentCodeIndex.containsKey(departID)) {
      departcode = (String)departmentCodeIndex.get(departID);
      departattribute = (Integer)departmentAttributeIndex.get(departID);

      ledgerVO.setCdeptcode(departcode);
      ledgerVO.setIdeptattr(departattribute);
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" select deptcode,deptattr from bd_deptdoc where ");
    sql.append(" pk_deptdoc", departID);
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      departcode = rowset.getString(0);
      departattribute = rowset.getInteger(1);
    }
    ledgerVO.setCdeptcode(departcode);
    ledgerVO.setIdeptattr(departattribute);

    departmentCodeIndex.put(departID, departcode);
    departmentAttributeIndex.put(departID, departattribute);
  }

  private void initInv(IaInoutledgerVO ledgerVO, Map invclassIndex, Map invcodeIndex, Map invnameIndex, Map invtypeIndex)
  {
    String cinventoryid = ledgerVO.getCinventoryid();
    if (cinventoryid == null) {
      return;
    }
    if ((ledgerVO.getCinvclid() != null) && (ledgerVO.getCinventorycode() != null) && (ledgerVO.getCinventoryname() != null) && (ledgerVO.getCinventorytype() != null))
    {
      return;
    }
    String invclass = null;
    String invcode = null;
    String invname = null;
    String invtype = null;

    if (invclassIndex.containsKey(cinventoryid)) {
      invclass = (String)invclassIndex.get(cinventoryid);
      invcode = (String)invcodeIndex.get(cinventoryid);
      invname = (String)invnameIndex.get(cinventoryid);
      invtype = (String)invtypeIndex.get(cinventoryid);

      ledgerVO.setCinvclid(invclass);
      ledgerVO.setCinventorycode(invcode);
      ledgerVO.setCinventoryname(invname);
      ledgerVO.setCinventorytype(invtype);
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" select a.pk_invcl,a.invcode,a.invname,a.invtype from ");
    sql.append(" bd_invbasdoc a,bd_invmandoc b where ");
    sql.append(" a.pk_invbasdoc = b.pk_invbasdoc ");
    sql.append(" and b.pk_invmandoc", cinventoryid);
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      invclass = rowset.getString(0);
      invcode = rowset.getString(1);
      invname = rowset.getString(2);
      invtype = rowset.getString(3);
    }
    ledgerVO.setCinvclid(invclass);
    ledgerVO.setCinventorycode(invcode);
    ledgerVO.setCinventoryname(invname);
    ledgerVO.setCinventorytype(invtype);

    invclassIndex.put(cinventoryid, invclass);
    invcodeIndex.put(cinventoryid, invcode);
    invnameIndex.put(cinventoryid, invname);
    invtypeIndex.put(cinventoryid, invtype);
  }

  private void initJob(IaInoutledgerVO ledgerVO, Map jobcodeIndex, Map jobnameIndex)
  {
    String projectID = ledgerVO.getCprojectid();
    if (projectID == null) {
      return;
    }
    if ((ledgerVO.getCprojectcode() != null) && (ledgerVO.getCprojectname() != null))
    {
      return;
    }
    String jobcode = null;
    String jobname = null;

    if (jobcodeIndex.containsKey(projectID)) {
      jobcode = (String)jobcodeIndex.get(projectID);
      jobname = (String)jobnameIndex.get(projectID);

      ledgerVO.setCprojectcode(jobcode);
      ledgerVO.setCprojectname(jobname);
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" select a.jobcode,a.jobname from bd_jobbasfil a, ");
    sql.append(" bd_jobmngfil b where a.pk_jobbasfil = b.pk_jobbasfil ");
    sql.append(" and b.pk_jobmngfil", projectID);
    IRowSet rowset = DataAccessUtils.query(sql.toString());
    if (rowset.next()) {
      jobcode = rowset.getString(0);
      jobname = rowset.getString(1);
    }
    ledgerVO.setCprojectcode(jobcode);
    ledgerVO.setCprojectname(jobname);

    jobcodeIndex.put(projectID, jobcode);
    jobnameIndex.put(projectID, jobname);
  }

  private String getBasicCurID(String pk_corp)
  {
    ISysInitQry bo = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());

    String basCurID = null;
    try {
      basCurID = bo.getPkValue(pk_corp, "BD301");
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }

    return basCurID;
  }
}