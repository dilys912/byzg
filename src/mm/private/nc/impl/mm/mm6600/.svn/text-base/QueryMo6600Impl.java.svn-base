package nc.impl.mm.mm6600;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

public class QueryMo6600Impl implements IHttpServletAdaptor {
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String result = queryMo(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	public String queryMo(String json) {
		// TODO Auto-generated method stub
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);//设置数据源
		StringBuffer result = new StringBuffer();
		result.append("[");
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = new nc.net.sf.json.JSONArray().fromObject(json);
		} catch (Exception e) {
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式:"+e.getMessage()+"\"}]";
		}
		List<Map> lsmap = new ArrayList<Map>();
		if(jsonarray != null&&jsonarray.size()>0){
			for(int i =0;i<jsonarray.size();i++){
				Map map = new HashMap();
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while(it.hasNext()){
					key = (String) it.next();
					value = jsonobject.getString(key);
					map.put(key, value);
				}
				lsmap.add(map);
			}
		}
		
		for(int i = 0;i<lsmap.size();i++){
			Map mls = lsmap.get(i);
			String username=mls.get("username")==null?"":mls.get("username").toString();//用户名
			if(StringUtils.isNotBlank(username)){
				if(!username.equals("baosteel")){
					return "[{\"status\":\"error\",\"message\":\"用户名错误\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"用户名为空\"}]"; 
			}
			String pwd=mls.get("password")==null?"":mls.get("password").toString();//密码
			if(StringUtils.isNotBlank(pwd)){
				if(!pwd.equals("123456")){
					return "[{\"status\":\"error\",\"message\":\"密码错误\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"密码为空\"}]"; 
			}
			String billcode = mls.get("billcode")==null?"":mls.get("billcode").toString();//生产订单号
			if(StringUtils.isBlank(billcode)){
				return "[{\"status\":\"error\",\"message\":\"生产订单号为空\"}]";
			}
			String invcode = mls.get("invcode")==null?"":mls.get("invcode").toString();//料号
			String applyCorp=mls.get("corp")==null?"":mls.get("corp").toString();//申请公司
			if(StringUtils.isBlank(applyCorp)){
				return "[{\"status\":\"error\",\"message\":\"公司为空\"}]"; 
			}
			String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
			List corpList = null;
			try {
				corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
				if(corpList == null || corpList.size() == 0){
					return "[{\"status\":\"error\",\"message\":\"公司在NC系统不存在，请检查\"}]"; 
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\"}]";
			}
			Map map1 = (Map) corpList.get(0);
			String pk_corp = map1.get("pk_corp").toString();
			StringBuffer sb = new StringBuffer();
			sb.append(" select a.jhwgsl,a.sjwgsl,a.zt,a.scddh,a.pk_corp from mm_mo a  where a.scddh ='"+billcode+"'  and a.pk_corp = '"+pk_corp+"' and nvl(a.dr, 0) = 0 "); 
	        List list = null;
	        nc.net.sf.json.JSONObject jsonH = new nc.net.sf.json.JSONObject();
	        StringBuffer jsonStr = new StringBuffer();
	        try {
				list = (List) uap.executeQuery(sb.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\"}]";
			}
			if(list  != null && list.size()>0){
				Map map = (Map) list.get(0);
				String scddh = map.get("scddh") == null?"":map.get("scddh").toString();//生产订单号
				String zt = map.get("zt") == null?"":map.get("zt").toString();//状态
				String ztname = "";
				if(zt.equals("A")){
					ztname = "计划";
				}else if(zt.equals("B")){
					ztname = "投放";
				}else if(zt.equals("C")){
					ztname = "结束";
				}else{
					ztname = "结束";
				}
				String custcode = map.get("custcode") == null?"":map.get("custcode").toString();//客商编码
				String ddwwgsl = map.get("sjwgsl") == null?"":map.get("sjwgsl").toString();//订单未完工数量
				String ddsl = map.get("jhwgsl") == null?"":map.get("jhwgsl").toString();//订单数量
				String ddwgsl = map.get("ddwgsl") == null?"":map.get("ddwgsl").toString();//订单完工数量
				String lh = map.get("lh") == null?"":map.get("lh").toString();//料号 
				jsonH.put("status", "success");
				jsonH.put("message", "获取成功");
				jsonH.put("scddh", scddh);
				jsonH.put("zt", ztname);
				jsonH.put("custcode", custcode);
				jsonH.put("ddwwgsl", ddwwgsl);
				jsonH.put("ddsl", ddsl);
				jsonH.put("ddwgsl", "0");
				jsonH.put("lh", lh);
				jsonH.put("glsl", "0");
			}else{
				jsonH.put("status", "success");
				jsonH.put("message", "获取成功");
				jsonH.put("scddh", billcode);
				jsonH.put("zt", "");
				jsonH.put("custcode", "");
				jsonH.put("ddwwgsl", "0");
				jsonH.put("ddsl", "0");
				jsonH.put("ddwgsl", "0");
				jsonH.put("lh", invcode);
				jsonH.put("glsl", "0");
			}
			//1确认CVM需要的字段
			//2组装JSON报文返回
			//3异常信息处理
			if(i == lsmap.size()-1){
				result.append(jsonH+"\n");
			}else{
				result.append(jsonH+","+"\n");
			}
		}
		result.append("]");
		return result.toString();
	}
}
