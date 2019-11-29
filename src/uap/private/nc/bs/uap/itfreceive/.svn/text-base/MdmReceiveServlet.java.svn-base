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
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.itfcheck.RECEIVEMESSAGEVO;


/**
 * MDM外部接口
 * @parm 访问地址：http://10.70.76.11:80/service/MdmReceiveServlet【配置接口名】【测试环境】
 * 
 * */
public class MdmReceiveServlet implements IHttpServletAdaptor {

	
	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//根据投信息的key获得值
		String serviceId = request.getHeader("serviceId");
		//校验
		if(isEmpty(serviceId)){
			String ms = toResponse(false, "Header中未获取到来源系统号 [serviceId]值，请检查;", null, null);
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
        br.close();
        //获取Body的值
        JSONObject jsons  = null;
		try {
			jsons = JSONObject.fromObject(sb.toString());
		} catch (Exception e) {
			//e.getStackTrace();
			String ms = toResponse(false, "JSON数据解析异常，请检查JSON格式："+e.getMessage(), null, null);
			response.getOutputStream().write(ms.getBytes("UTF-8"));
			return;
		}
        
        JSONObject mess = distributeRunClass(serviceId, jsons);//执行对应方法
        String state = (String) mess.get("s");
        String m = mess.get("m")==null?null:mess.get("m").toString();
        String puuid = mess.get("puuid")==null?null:mess.get("puuid").toString();
        String d = mess.get("d")==null?null:mess.get("d").toString();
        RECEIVEMESSAGEVO receivemessageVO = null;
        if("success".equals(state)){
        	receivemessageVO = recordInsertVO(true,serviceId,sb.toString(),m);        	
        }else{
        	receivemessageVO = recordInsertVO(false,serviceId,sb.toString(),m);
        }
        if(receivemessageVO!=null){
        	//存数据库记录
        	BaseDAO base = new BaseDAO();
        	try {
        		base.insertVO(receivemessageVO);
        	} catch (DAOException e) {
        		e.printStackTrace();
        	}
        }
        JSONArray rj = null;
        if(!isEmpty(d)){
        	rj = JSONArray.fromObject(d);
        }
        String result = "Message is received successfully";
        if("success".equals(state)){
        	result = toResponse(true, m, puuid, rj);
        }else{
        	result = toResponse(false, m, puuid, rj);
        }
        response.getOutputStream().write(result.getBytes("UTF-8"));
	}

	/**
	 * 分发执行对应动作脚本类
	 * @return 
	 * */
	private JSONObject distributeRunClass(String serviceId,JSONObject reqjson) {
		
		try {
			Class c = Class.forName("nc.impl.uap.mdmreceive."+serviceId);
			Object exe = c.newInstance();
			Method method = c.getMethod("exeScript", JSONObject.class);
			Object o = method.invoke(exe, reqjson);
			if(o!=null){
				JSONObject ms = JSONObject.fromObject(o);
				return ms;
			}else{
				return returnMsg(false, "方法执行返回值为空,请检查", null);
			}
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			return returnMsg(false, "未获取到对应处理方法类："+e.getMessage(),null);
		} catch (InstantiationException e) {
			//e.printStackTrace();
			return returnMsg(false, "对应方法类执行异常："+e.getMessage(),null);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			return returnMsg(false, "非法参数,请检查对应执行类："+e.getMessage(),null);
		} catch (SecurityException e) {
			//e.printStackTrace();
			return returnMsg(false, "安全异常："+e.getMessage(),null);
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			return returnMsg(false, "未获取到对应类方法："+e.getMessage(),null);
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			return returnMsg(false, "方法传递了一个不合法或不正确的参数："+e.getMessage(),null);
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			return returnMsg(false, "调用的方法的内部抛出了异常："+e.getMessage(),null);
		}
	}
	
	
	/**
	 * @param billcode 单据号
	 * @param val 消息体
	 * @param b true 0==成功  false  -1==失败
	 * @param mess 执行消息
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
     * 非空非null判断
     * @return 非空==true / 空==false
     * */
    private boolean isEmpty(String s) {
		boolean b = true;
		if(s != null && s != ""){
			b = false;
		}
		return b;
	}
    
	/**
	 * 组装回执
	 * */
	private JSONObject returnMsg(boolean b,String m,String d) {
		JSONObject json = new JSONObject();
		if(b){
			json.put("s", "success");
		}else{
			json.put("s", "error");
		}
		json.put("m", m);
		json.put("d", d);
		return json;
	}
	
	/**
	 * 封装回执消息
	 * @param state==S/E
	 * @param results ==消息
	 * @param arrdf ==执行情况
	 * 
	 * "DATAINFO": [
					{
						"CODE": "主编码",
						"UUID": "数据唯一标识",
						"SYNSTATUS": "0（成功）或者1（失败）",
						"SYNRESULT": "成功或者失败原因"
					}
	 * */
	private String toResponse(boolean state,String results,String puuid,JSONArray arrdf) {
		JSONObject esb = new JSONObject();
		JSONObject result = new JSONObject();
		if(state){
			result.put("RESULT", "S");			
		}else{
			result.put("RESULT", "E");	
		}
		JSONObject datainfos = new JSONObject();
		JSONObject datainfo = new JSONObject();
		datainfo.put("PUUID", puuid);
		datainfo.put("DATAINFO", arrdf);
		
		datainfos.put("DATAINFOS", datainfo);
		result.put("DATA", datainfos);
		result.put("DESC", results);
		esb.put("ESB", result);
		return esb.toString();
	}
}
