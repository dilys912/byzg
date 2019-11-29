package nc.impl.pub.invcl;

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
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

public class SendInvclToCVMImpl implements IHttpServletAdaptor {
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
        
        String result = sendInvclToCVM(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }

	public String sendInvclToCVM(String json) {
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
			nc.net.sf.json.JSONObject jbH = new nc.net.sf.json.JSONObject();
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
				List list = new ArrayList();
				StringBuffer sb = new StringBuffer();
				sb.append("select w.pk_invcl,w.invclasscode,substr(w.invclasscode,0,length(w.invclasscode)-2) fathercode,w.invclassname,w.invclasslev,w.endflag,w.dr,w.ts from bd_invcl w "+"\n")
				.append(" where nvl(w.dr,0)=0 and (invclasscode like '21%' or invclasscode like '22%' or invclasscode like '23%') order by invclasscode "+"\n");
				//.append(" and to_char(to_date(a.ts, 'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') order by invclasscode");//查询存货分类信息
				try {
					list= (List) dao.executeQuery(sb.toString(), new MapListProcessor());
					if (list.size()>0) {
						for(int j=0;j<list.size();j++){
							Map map = (Map) list.get(j);
							String pk_invcl = map.get("pk_invcl")==null?"":map.get("pk_invcl").toString().trim();//主键
							String invclasscode = map.get("invclasscode")==null?"":map.get("invclasscode").toString().trim();//存货分类编码
							String fathercode=map.get("fathercode")==null?"01":map.get("fathercode").toString().trim();//父级编码
							String invclassname = map.get("invclassname")==null?"":map.get("invclassname").toString().trim();//存货分类名称
							String invclasslev = map.get("invclasslev")==null?"":map.get("invclasslev").toString().trim();//编码级次
							String endflag = map.get("endflag")==null?"":map.get("endflag").toString().trim();//末级标志
							//JSONObject jb = new JSONObject();
							nc.net.sf.json.JSONObject jb = new nc.net.sf.json.JSONObject();
							jb.put("pk_invcl", pk_invcl);
							jb.put("invclasscode", invclasscode);
							jb.put("fathercode", fathercode);
							jb.put("invclassname", invclassname);
							jb.put("invclasslev", invclasslev);
							jb.put("endflag", endflag);
							if(j == list.size()-1){
								jsonStr.append(jb+"\n");
							}else{
								jsonStr.append(jb+","+"\n");
							}
						}
						jsonStr.append("]");
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
