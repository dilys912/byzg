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

public class NewQueryMo6600Impl implements IHttpServletAdaptor {
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
        String result = newQueryMo(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	public String newQueryMo(String json) {
		// TODO Auto-generated method stub
		// 设置数据源
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);//设置数据源
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = nc.net.sf.json.JSONArray.fromObject(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式:"+e.getMessage()+"\"}]";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();//表体数据
		if (jsonarray != null && jsonarray.size() > 0) {
			for (int i = 0; i < jsonarray.size(); i++) {
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while (it.hasNext()) {
					key = (String) it.next();
					value = jsonobject.getString(key);
					if (key.equals("bodylist")) {
						nc.net.sf.json.JSONArray array = jsonobject.getJSONArray(key);
						for (int j = 0; j < array.size(); j++) {
							String key1 = null;
							String value1 = null;
							nc.net.sf.json.JSONObject jb = array.getJSONObject(j);
							Map mitems = new HashMap();
							Iterator iterator1 = jb.keys();
							while (iterator1.hasNext()) {
								key1 = (String) iterator1.next();
								value1 = jb.getString(key1);
								mitems.put(key1, value1);
							}
							lsmap.add(mitems);
						}
					} else {
						mls.put(key, value);
						
					}
				}
			}
		}
		System.out.println("json解析完成----------");
		
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
		StringBuffer custcodestr = new StringBuffer();
		StringBuffer billcodestr = new StringBuffer();
		if(lsmap.size()>0){
			for(int i = 0; i<lsmap.size() ;i++){
				Map map = lsmap.get(i);
				String custcode = map.get("custcode")==null?"":map.get("custcode").toString();//客商编码
				if(StringUtils.isNotBlank(custcode)){
					if(i == lsmap.size()-1){
						custcodestr.append(" c.custcode='"+custcode+"'");
					}else{
						custcodestr.append(" c.custcode='"+custcode+"'"+"or");
					}
				}
			}
			for(int i = 0; i<lsmap.size() ;i++){
				Map map = lsmap.get(i);
				String billcode = map.get("billcode")==null?"":map.get("billcode").toString();//生产订单号
				if(StringUtils.isNotBlank(billcode)){
					if(i == lsmap.size()-1){
						billcodestr.append(" a.scddh='"+billcode+"'");
					}else{
						billcodestr.append(" a.scddh='"+billcode+"'"+"or");
					}
				}
			}
		}
		if(custcodestr.toString().endsWith("or")){
			custcodestr =new StringBuffer(custcodestr.toString().substring(0, custcodestr.toString().length()-2));
		}
		if(billcodestr.toString().endsWith("or")){
			billcodestr =new StringBuffer(billcodestr.toString().substring(0, billcodestr.toString().length()-2));
		}
		StringBuffer sb = new StringBuffer();//查询SQL；
		sb.append(" select a.scddh,a.zt,c.custcode,d.invcode,a.jhwgsl,a.sjwgsl from mm_mo a  ") 
		.append(" left join bd_cumandoc b on a.ksid = b.pk_cumandoc ") 
		.append(" left join bd_cubasdoc c on b.pk_cubasdoc = c.pk_cubasdoc ") 
		.append(" left join bd_invbasdoc d on a.wlbmid=d.pk_invbasdoc ")
		.append(" where a.pk_corp='"+pk_corp+"' and nvl(a.dr,0)=0 and a.zt='B' "); 
		if(custcodestr.length()>0){
			sb.append(" and ("+custcodestr+"  )");
		}
		if(billcodestr.length()>0){
			sb.append(" and ("+billcodestr+"  )");
		}
        List list = new ArrayList();
        StringBuffer allinfo = new StringBuffer();
        StringBuffer result = new StringBuffer();
		allinfo.append("[");
		result.append("[");
        nc.net.sf.json.JSONObject jsonH = new nc.net.sf.json.JSONObject();
        jsonH.put("status", "success");
		jsonH.put("message", "获取成功");
        StringBuffer jsonStr = new StringBuffer();
        try {
			list = (List) uap.executeQuery(sb.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\"}]";
		}
		if(list  != null && list.size()>0){
			for(int i = 0 ;i<list.size() ;i++){
				nc.net.sf.json.JSONObject jsonB = new nc.net.sf.json.JSONObject();
				Map map = (Map) list.get(i);
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
				String ddsl = map.get("jhwgsl") == null?"":map.get("jhwgsl").toString();//订单数量
				String ddwgsl = map.get("sjwgsl") == null?"":map.get("sjwgsl").toString();//订单完工数量
				String ddwwgsl = String.valueOf(new UFDouble(ddsl).sub(new UFDouble(ddwgsl)));//订单未完工数量
				String lh = map.get("invcode") == null?"":map.get("invcode").toString();//料号 
				//UFDouble glsl = new UFDouble(ddsl).sub(new UFDouble(ddwwgsl)).sub(new UFDouble(ddwgsl));//隔离数量
				//DecimalFormat decimalFormat = new DecimalFormat("###################.###########"); 
				jsonB.put("scddh", scddh);
				jsonB.put("zt", ztname);
				jsonB.put("custcode", custcode);
				jsonB.put("ddwwgsl", ddwwgsl);
				jsonB.put("ddsl", ddsl);
				jsonB.put("ddwgsl", ddwgsl);
				jsonB.put("lh", lh);
				jsonB.put("glsl", "0");
				result.append(jsonB+",");
			}
			result = result.deleteCharAt(result.length()-1);
			result.append("]");
			jsonH.put("bodylist", result.toString());
			allinfo.append(jsonH);
			allinfo.append("]");
		}else{
			jsonH.put("bodylist", "[]");
			allinfo.append(jsonH);
			allinfo.append("]");
		}
		//1确认CVM需要的字段
		//2组装JSON报文返回
		//3异常信息处理
	return allinfo.toString();
}

}
