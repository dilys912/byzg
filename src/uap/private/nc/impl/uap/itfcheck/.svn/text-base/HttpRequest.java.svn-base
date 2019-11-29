package nc.impl.uap.itfcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import nc.net.sf.json.JSONObject;
import nc.vo.uap.itfcheck.XbusRestRequestEntity;

public class HttpRequest {
	
    /**
     * 向 URL 发送POST方法的请求
     * 
     * @param XbusRestRequestEntity 参数
     *            
     *            type = JSON/XBUS
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public JSONObject sendXbus(XbusRestRequestEntity xre,String type) {
    	
    	String url = xre.getUrl();
    	String sourceAppCode = xre.getSourceAppCode();
    	String serviceId = xre.getServiceId();
    	String msgSendTime = xre.getMsgSendTime();
    	String password = xre.getPassword();
    	String msgToken = xre.getMsgToken();
    	String tempField = xre.getTempField();
    	String version = xre.getVersion();
    	String msgBody = xre.getMsgBody();
    	
    	boolean isjson = false;
    	if("JSON".equals(type)){
    		isjson = true;
    	}
    	if(!isEmpty(url)){
    		return returnMsg(false, "接口地址【url】不可为空", null);
    	}
    	if(!isEmpty(msgBody)){
    		return returnMsg(false, "消息内容【msgBody】不可为空", null);
    	}
    	if(!isjson){
    		if(!isEmpty(sourceAppCode)){
    			return returnMsg(false, "源系统代码【sourceAppCode】不可为空", null);
    		}
    		if(!isEmpty(serviceId)){
    			return returnMsg(false, "服务代号【serviceId】不可为空", null);
    		}
    		if(!isEmpty(msgSendTime)){
    			return returnMsg(false, "发送时间【msgSendTime】不可为空,格式为YYYYMMDDHHMMSSsss（精确到毫秒，共17位）", null);
    		}    		
    	}
    	
    	String param = "";
    	if(isjson){
    		param=msgBody;
    	}else{
    		/*param="msgBody="+msgBody;*/
    		param=msgBody;
    		
    	}
    	
//        PrintWriter out = null; 
    	//edit by zwx 2019-10-10 乱码问题更改
        OutputStreamWriter out = null;
        
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
//            conn.setRequestProperty("content-type", "text/plain");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//            conn.setRequestProperty("user-agent","Java/1.6.0_19");
            conn.setRequestProperty("accept", " text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("content-length","6");
            if(!isjson){
            	conn.setRequestProperty("sourceAppCode", sourceAppCode);
            	conn.setRequestProperty("password", password);
            	conn.setRequestProperty("serviceId", serviceId);
            	conn.setRequestProperty("msgToken", msgToken);
            	conn.setRequestProperty("msgSendTime", msgSendTime);
            	conn.setRequestProperty("tempField", tempField);
            	conn.setRequestProperty("version", version);            	
            }
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            //设置超时，防止程序假死
            conn.setConnectTimeout(3000);//单位毫秒
            
            /*
             // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            */
            
            //edit by zwx 2019-10-10 乱码问题更改
            out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out.write(param);
            
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if(result.indexOf("success")==-1){
            	return returnMsg(false, "发送 POST请求失败", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnMsg(false, "发送 POST请求出现异常!", e.getMessage());
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return returnMsg(true, "数据发送成功", result);
    }
    
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 组装接口调用回执
     * 
     * */
    private JSONObject returnMsg(boolean b,String m,String c) {
    	
    	JSONObject j = new JSONObject();
    	if(b){
    		j.put("states", "success");
    		j.put("message", m);
    		j.put("data", c);
    	}else{
    		j.put("states", "error");
    		j.put("message", m);
    		j.put("data", null);
    	}
    	
		return j;
	}
    
    /**
     * 非空非null判断
     * @return 
     * */
    private boolean isEmpty(String s) {
		boolean b = false;
		if(s != null && s != ""){
			b = true;
		}
		return b;
	}
}