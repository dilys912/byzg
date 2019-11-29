package nc.impl.scm.so.so003;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.scm.so.so003.ISendMsg;
import nc.itf.uap.pf.IPFMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.msg.CommonMessageVO;
import nc.vo.pub.msg.UserNameObject;
import nc.vo.scm.pub.SCMEnv;

public class SendMsgImpl
  implements ISendMsg
{
  public void send(String cbillid, String ccustomerid, String cinventoryid, String csenderid, String creceiverid)
    throws BusinessException
  {
    try
    {
      SendMsgDMO dmo = new SendMsgDMO();
      IPFMessage dmoMsg = (IPFMessage)NCLocator.getInstance().lookup(IPFMessage.class.getName());
      String billcode = dmo.getSOBillCode(cbillid);
      String customer = dmo.getCustName(ccustomerid);
      String[] invcn = dmo.getInvCodeName(cinventoryid);
      String[] sender = dmo.getOperator(csenderid);
      String[] receiver = dmo.getOperator(creceiverid);
      UserNameObject uno = new UserNameObject(receiver[1]);
      uno.setUserPK(creceiverid);
      uno.setUserCode(receiver[0]);
      String msg = NCLangResOnserver.getInstance().getStrByID("so003", "UPPso003-000001", null, new String[] { billcode, customer, invcn[0], invcn[1] });

      CommonMessageVO voMsg = new CommonMessageVO();
      voMsg.setMessageContent(msg);
      voMsg.setSender(csenderid);
      voMsg.setReceiver(new UserNameObject[] { uno });
      voMsg.setTitle(NCLangResOnserver.getInstance().getStrByID("so003", "UPPso003-000005"));
      voMsg.setSendDataTime(new UFDateTime(System.currentTimeMillis()));
      dmoMsg.insertCommonMsg(voMsg);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  private void reportException(Exception e) {
    SCMEnv.out(e.getMessage());
  }
}