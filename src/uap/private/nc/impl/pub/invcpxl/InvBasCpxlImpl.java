package nc.impl.pub.invcpxl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

public class InvBasCpxlImpl implements IHttpServletAdaptor {
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
        String result = sendTypeToCVM(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	public String sendTypeToCVM(String json) {
		//获取NC这边存货分类信息
		String source=InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);//设置数据源
		IUAPQueryBS dao = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//BaseDAO dao = new BaseDAO();
		//dao.setMaxRows(-1);//查询所有数据，查询数量不收限制
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
		StringBuffer jsonStr = new StringBuffer();//拼装json
		jsonStr.append("[");
		StringBuffer allJson = new StringBuffer();
		JSONObject jbH = new JSONObject();
		jbH.put("status", "success");
		jbH.put("message", "获取成功");
		for(int i = 0 ; i<lsmap.size() ; i++){
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
			String code = mls.get("code")==null?"":mls.get("code").toString();//编码
			if(StringUtils.isBlank(code)){
				return "[{\"status\":\"error\",\"message\":\"编码为空\"}]"; 
			}
			List list = new ArrayList();
			StringBuffer sb = new StringBuffer();
			sb.append(" select distinct doccode, docname") 
			.append("   from bd_defdoc ") 
			.append("   left outer join bd_defdef ") 
			.append("     on bd_defdoc.pk_defdoclist = bd_defdef.pk_defdoclist ") 
			.append("  where 11 = 11 ") 
			.append("    and bd_defdoc.pk_defdoclist = ") 
			.append("        (select pk_defdoclist from bd_defdoclist where doclistcode = '"+code+"') ") 
			.append("    and (sealflag is null or sealflag <> 'Y') ") 
			.append("  order by doccode "); 
			try {
				list= (List) dao.executeQuery(sb.toString(), new MapListProcessor());
				if (list.size()>0) {
					for(int j=0;j<list.size();j++){
						Map map = (Map) list.get(j);
						String cpxlcode = map.get("doccode")==null?"":map.get("doccode").toString().trim();//发运编码
						String cpxlname = map.get("docname")==null?"":map.get("docname").toString().trim();//发运名称
						JSONObject jb = new JSONObject();
						jb.put("code", cpxlcode);
						jb.put("name", cpxlname);
						if(j == list.size()-1){
							jsonStr.append(jb+"\n");
						}else{
							jsonStr.append(jb+","+"\n");
						}
					}
					jsonStr.append("]");
				}else{
					return "[{\"status\":\"error\",\"message\":\"该编码"+code+"未能在NC查询到数据，请检查\"}]";
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库查询出错:+"+e.getMessage()+"\"}]";
			}
		}
		jbH.put("bodylist",jsonStr.toString());
		allJson.append(jbH);
		return allJson.toString();
	}
}
