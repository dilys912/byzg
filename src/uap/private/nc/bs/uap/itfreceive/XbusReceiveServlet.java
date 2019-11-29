package nc.bs.uap.itfreceive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.itfcheck.RECEIVEMESSAGEVO;
import nc.vo.uap.itfcheck.SENDMESSAGEVO;


/**
 * Xbus�ⲿ�ӿ�
 * @parm ���ʵ�ַ��http://10.70.76.11:80/service/XbusReceiveServlet�����ýӿ����������Ի�����
 * 
 * */
public class XbusReceiveServlet implements IHttpServletAdaptor {

	private List<String> sourceApps = Arrays.asList("BC", "RG");//�Խӵ�ϵͳ
	
	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String hahastr = request.getParameter("haha");
		//����Ͷ��Ϣ��key���ֵ
		String sourceAppCode = request.getHeader("sourceAppCode");
		String password = request.getHeader("password");
		String serviceId = request.getHeader("serviceId");
		String msgToken = request.getHeader("msgToken");
		String msgSendTime = request.getHeader("msgSendTime");
		//У��
		/*if(isEmpty(sourceAppCode)){
			String ms = "The value of [sourceAppCode] was not obtained!";
			response.getOutputStream().write(ms.getBytes("UTF-8"));
			return;
		}else{
			if(!sourceApps.contains(sourceAppCode)){
				String ms = "Illegal sources!";
				response.getOutputStream().write(ms.getBytes("UTF-8"));
				return;
			}
		}*/
//		if(isEmpty(password)){
//			String ms = "The value of [password] was not obtained!";
//			response.getOutputStream().write(ms.getBytes("UTF-8"));
//			return;
//		}
//		else{
//			if(!pass.equals(password)){
//				String ms = "Password error!";
//				response.getOutputStream().write(ms.getBytes("UTF-8"));
//				return;
//			}
//		}
		if(isEmpty(serviceId)){
			String ms = "The value of [serviceId] was not obtained!";
			response.getOutputStream().write(ms.getBytes("UTF-8"));
			return;
		}
		//
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {
            sb.append(s);  
        }
        //��ȡmsgBody��ֵ
        String val = sb.toString();
        if(isEmpty(val)){
        	String ms = "The value of [msgBody] was not obtained!";
        	response.getOutputStream().write(ms.getBytes("UTF-8"));
        	return;
        }
        //����XBUS����
        IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName());
		JSONObject jsons = ifc.disassembleItfData(val,serviceId);
		
        JSONObject mess = distributeRunClass(serviceId, jsons);//ִ�ж�Ӧ����
        String state = (String) mess.get("state");
        String m = mess.get("message")==null?"":mess.get("message").toString();
        String c = mess.get("content")==null?"":mess.get("content").toString();
        RECEIVEMESSAGEVO receivemessageVO = null;
        if("success".equals(state)){
        	receivemessageVO = recordInsertVO(true,serviceId,sb.toString(),m+c);        	
        }else{
        	receivemessageVO = recordInsertVO(false,serviceId,sb.toString(),m+c);
        }
        if(receivemessageVO!=null){
        	//�����ݿ��¼
        	BaseDAO base = new BaseDAO();
        	try {
        		base.insertVO(receivemessageVO);
        	} catch (DAOException e) {
        		e.printStackTrace();
        	}
        }
        String result = "Message is received successfully";
        br.close();
        response.setHeader("returnCode", "0");
        response.getOutputStream().write(result.getBytes("UTF-8"));
	}

	/**
	 * �ַ�ִ�ж�Ӧ�����ű���
	 * @return 
	 * */
	private JSONObject distributeRunClass(String serviceId,JSONObject reqjson) {
		
		try {
			Class c = Class.forName("nc.impl.uap.xbusreceive."+serviceId);
			Object test = c.newInstance();
			Method method = c.getMethod("exeScript", JSONObject.class);
			Object o = method.invoke(test, reqjson);
			if(o!=null){
				String ms = o.toString();
				return returnMsg(true, ms, null);
			}else{
				return returnMsg(false, "����ִ�з���ֵΪ��,����", null);
			}
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			return returnMsg(false, "δ��ȡ����Ӧ������", e.getMessage());
		} catch (InstantiationException e) {
			//e.printStackTrace();
			return returnMsg(false, "��Ӧ������ִ���쳣", e.getMessage());
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			return returnMsg(false, "�Ƿ�����,�����Ӧִ����", e.getMessage());
		} catch (SecurityException e) {
			//e.printStackTrace();
			return returnMsg(false, "��ȫ�쳣", e.getMessage());
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			return returnMsg(false, "δ��ȡ����Ӧ�෽��", e.getMessage());
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			return returnMsg(false, "����������һ�����Ϸ�����ȷ�Ĳ���", e.getMessage());
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			return returnMsg(false, "���õķ������ڲ��׳����쳣", e.getMessage());
		}
	}
	
	
	/**
	 * @param billcode ���ݺ�
	 * @param val ��Ϣ��
	 * @param b true 0==�ɹ�  false  -1==ʧ��
	 * @param mess ִ����Ϣ
	 * */
	private RECEIVEMESSAGEVO recordInsertVO(boolean b,String billcode, String val,String mess) {
		long lt = new Date().getTime();
		RECEIVEMESSAGEVO svo = new RECEIVEMESSAGEVO();
		svo.setMessage_body(val);
		svo.setMessage_code(lt+"");
		if(b){
			svo.setMessage_state(new Integer(0));			
		}else{
			svo.setMessage_state(new Integer(-1));			
		}
		svo.setMessage_group_name(billcode);
		svo.setMessage_operate_version(new UFDouble(0.00));
		svo.setMessage_operate_info(mess);
		svo.setPk_corp("0001");
		svo.setDr(new Integer(0));
		return svo;
	}
	
	
    /**
     * �ǿշ�null�ж�
     * @return �ǿ�==true / ��==false
     * */
    private boolean isEmpty(String s) {
		boolean b = true;
		if(s != null && s != ""){
			b = false;
		}
		return b;
	}
    
	/**
	 * ��װ��ִ
	 * */
	private JSONObject returnMsg(boolean b,String m,String c) {
		JSONObject json = new JSONObject();
		if(b){
			json.put("state", "success");
			json.put("message", m);
			json.put("content", c);
		}else{
			json.put("state", "error");
			json.put("message", m);
			json.put("content", c);
		}
		return json;
	}
}
