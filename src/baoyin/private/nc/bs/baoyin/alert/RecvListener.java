/*
 * �������� 2005-12-14
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.bs.baoyin.alert;

import java.math.BigInteger;

import com.wondertek.esmp.esms.empp.EMPPAnswer;
import com.wondertek.esmp.esms.empp.EMPPChangePassResp;
import com.wondertek.esmp.esms.empp.EMPPDeliver;
import com.wondertek.esmp.esms.empp.EMPPDeliverReport;
import com.wondertek.esmp.esms.empp.EMPPObject;
import com.wondertek.esmp.esms.empp.EMPPRecvListener;
import com.wondertek.esmp.esms.empp.EMPPReqNoticeResp;
import com.wondertek.esmp.esms.empp.EMPPSubmitSM;
import com.wondertek.esmp.esms.empp.EMPPSubmitSMResp;
import com.wondertek.esmp.esms.empp.EMPPSyncAddrBookResp;
import com.wondertek.esmp.esms.empp.EMPPTerminate;
import com.wondertek.esmp.esms.empp.EMPPUnAuthorization;
import com.wondertek.esmp.esms.empp.EmppApi;

/**
 * @author chensheng
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class RecvListener implements EMPPRecvListener {

	private static final long RECONNECT_TIME = 10 * 1000;
    
    private EmppApi emppApi = null;
    
    private int closedCount = 0;
    
    protected RecvListener(){
        
    }
    
    public RecvListener(EmppApi emppApi){
        this.emppApi = emppApi;
    }
  
 	 // ������յ�����Ϣ
    public void onMessage(EMPPObject message) {
        if(message instanceof EMPPUnAuthorization){
            EMPPUnAuthorization unAuth=(EMPPUnAuthorization)message;
            System.out.println("�ͻ�����Ȩִ�д˲��� commandId="+unAuth.getUnAuthCommandId());
            return;
         }
        if(message instanceof EMPPSubmitSMResp){
        	EMPPSubmitSMResp resp=(EMPPSubmitSMResp)message;
        	System.out.println("�յ�sumbitResp:");
        	byte[] msgId=fiterBinaryZero(resp.getMsgId());
        	
			System.out.println("msgId="+new BigInteger(msgId));
        	System.out.println("result="+resp.getResult());
        	return;
        }
  		if(message instanceof EMPPDeliver){
  			EMPPDeliver deliver = (EMPPDeliver)message;
  			if(deliver.getRegister()==EMPPSubmitSM.EMPP_STATUSREPORT_TRUE){
  				//�յ�״̬����
  				EMPPDeliverReport report=deliver.getDeliverReport();
  				System.out.println("�յ�״̬����:");
  				byte[] msgId=fiterBinaryZero(report.getMsgId());
  			    
  				System.out.println("msgId="+new BigInteger(msgId));
  			    System.out.println("status="+report.getStat());
  			    
  			}else{
  				//�յ��ֻ��ظ�
	  			System.out.println("�յ�"+deliver.getSrcTermId()+"���͵Ķ���");
	  			System.out.println("��������Ϊ��"+deliver.getMsgContent());
  			}
  		    return;
  		}
  		 if(message instanceof EMPPSyncAddrBookResp){
  		      EMPPSyncAddrBookResp resp=(EMPPSyncAddrBookResp)message;
  		      if(resp.getResult()!=EMPPSyncAddrBookResp.RESULT_OK)
  		          System.out.println("ͬ��ͨѶ¼ʧ��");
  		      else{
  		          System.out.println("�յ����������͵�ͨѶ¼��Ϣ");
  		          System.out.println("ͨѶ¼����Ϊ��"+resp.getAddrBookType());
  		          System.out.println(resp.getAddrBook());
  		      }
          }
  		 if(message instanceof EMPPChangePassResp){
                EMPPChangePassResp resp=(EMPPChangePassResp)message;
                if(resp.getResult()==EMPPChangePassResp.RESULT_VALIDATE_ERROR)
                    System.out.println("�������룺��֤ʧ��");
                if(resp.getResult()==EMPPChangePassResp.RESULT_OK)
                {
                    System.out.println("��������ɹ�,������Ϊ��"+resp.getPassword());
                    emppApi.setPassword(resp.getPassword());
                }
                return;
                
            } 
  		 if(message instanceof EMPPReqNoticeResp){
                EMPPReqNoticeResp response=(EMPPReqNoticeResp)message;
                if(response.getResult()!=EMPPReqNoticeResp.RESULT_OK)
                   System.out.println("��ѯ��Ӫ�̷�����Ϣʧ��");
                else{
                   System.out.println("�յ���Ӫ�̷�������Ϣ");
                   System.out.println(response.getNotice());
                }
                return;
           }
  		if(message instanceof EMPPAnswer){
  		    System.out.println("�յ���ҵ���ʽ��");
             EMPPAnswer  answer=(EMPPAnswer)message;
             System.out.println(answer.getAnswer());
             
         }
  		    System.out.println(message);
  	    
       }
 	 //�������Ӷϵ��¼�
     public void OnClosed(Object object) {
        // �������Ǳ������������ϵ�������Ҫ����
        if(object instanceof EMPPTerminate){
            System.out.println("�յ����������͵�Terminate��Ϣ��������ֹ");
            return;
        }
        //����ע��Ҫ��emppApi��Ϊ�������빹�캯��
        RecvListener listener = new RecvListener(emppApi)
		;
        System.out.println("���Ӷϵ�������"+(++closedCount));
        for(int i = 1;!emppApi.isConnected();i++){
            try {
                System.out.println("��������:"+i);
                Thread.sleep(RECONNECT_TIME);
                emppApi.reConnect(listener);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("�����ɹ�");
    }
 
 	//��������¼�
    public void OnError(Exception e) {
        e.printStackTrace();
    }
    
    private static byte[] fiterBinaryZero(byte[] bytes) {
        byte[] returnBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            returnBytes[i] = bytes[i];
        }
        return returnBytes;
    }
}
