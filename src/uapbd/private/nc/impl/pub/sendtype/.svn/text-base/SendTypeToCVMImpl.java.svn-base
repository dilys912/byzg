package nc.impl.pub.sendtype;

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
/*
 * 发运方式
 */
public class SendTypeToCVMImpl implements IHttpServletAdaptor {
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
			List list = new ArrayList();
			StringBuffer sb = new StringBuffer();
			sb.append(" select a.sendcode,a.sendname,nvl(b.unitcode,'0001') unitcode from bd_sendtype a  ") 
			.append(" left join bd_corp b on a.pk_corp = b.pk_corp ") 
			.append(" where nvl(a.dr,0)=0 and a.pk_corp='0001' ") 
			.append(" order by b.unitcode "); 
			try {
				list= (List) dao.executeQuery(sb.toString(), new MapListProcessor());
				if (list.size()>0) {
					for(int j=0;j<list.size();j++){
						Map map = (Map) list.get(j);
						String sendcode = map.get("sendcode")==null?"":map.get("sendcode").toString().trim();//发运编码
						String sendname = map.get("sendname")==null?"":map.get("sendname").toString().trim();//发运名称
						String unitcode = map.get("unitcode")==null?"":map.get("unitcode").toString().trim();//公司编码
						JSONObject jb = new JSONObject();
						jb.put("sendcode", sendcode);
						jb.put("sendname", sendname);
						jb.put("unitcode", unitcode);
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
