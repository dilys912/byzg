package nc.impl.ic.getKlrep;

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
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

public class GetKlImpl implements IHttpServletAdaptor {
	
	IUAPQueryBS uap =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
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
        String result = getKl(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	
	public String getKl(String json) {
		// TODO Auto-generated method stub
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);
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
		for(int j = 0;j<lsmap.size();j++){
			Map mls = lsmap.get(j);
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
			sb.append(" select invcode,invname,price,def1,def2,substr(ts,0,10) as ts, ")
			.append(" sum(kl0) as kl0,sum(kl1) as kl1,sum(kl2) as kl2,sum(kl3) as kl3,sum(kl4) as kl4,sum(kl5) as kl5,  ")
			//.append(" sum(kl6) as kl6,sum(kl7) as kl7,sum(kl8) as kl8,sum(kl9) as kl9,sum(kl10) as kl10,  ")
			.append(" sum(je0) as je0,sum(je1) as je1,sum(je2) as je2,sum(je3) as je3,sum(je4) as je4,sum(je5) as je5  ")
			//.append(" sum(je6) as je6,sum(je7) as je7,sum(je8) as je8,sum(je9) as je9,sum(je10) as je10  ")
			.append(" from ic_teamkl where pk_corp='"+pk_corp+"'  and nvl(dr,0)=0  ");
//			String invclasscode = mls.get("invclasscode")==null?"":mls.get("invclasscode").toString();
//			if(StringUtils.isNotBlank(invclasscode)){
//				int length = invclasscode.length();
//				sb.append(" and substr(invcode,0,'"+length+"')='"+invclasscode+"'");
//			}
//			String custcode = mls.get("custcode")==null?"":mls.get("custcode").toString();
//			if(StringUtils.isNotBlank(custcode)){
//				sb.append(" and def1='"+custcode+"'");
//			}
			sb.append(" group by invcode,invname,price,def1,def2,ts  ");
			List list = null;
			try {
				list = (List) uap.executeQuery(sb.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}]";
			}
			StringBuffer all = new StringBuffer();
			all.append("[");
			StringBuffer result = new StringBuffer();
			result.append("[");
			JSONObject jsonh = new JSONObject();
			jsonh.put("status", "success");
			jsonh.put("message", "获取成功");
			if(list != null && list.size()>0){
				for(int i = 0 ;i<list.size();i++){
					JSONObject jsonb = new JSONObject();
					Map map = (Map) list.get(i);
					jsonb.put("corp", applyCorp);
					jsonb.put("invname", map.get("invname")==null?"":map.get("invname").toString());
					String invcode = map.get("invcode")==null?"":map.get("invcode").toString();
					jsonb.put("invcode", invcode);
					String invclasscode1 = "";
					try {
						invclasscode1 = getInvclasscode(invcode);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();//发生异常不影响后续查询
					}
					String ts = map.get("ts")==null?"":map.get("ts").toString();
					UFDouble countXcl = new UFDouble(0);//获取该物料的总库存
					String vfree1 = map.get("kl0")==null?"":map.get("kl0").toString();
					String vfree2 = map.get("kl1")==null?"":map.get("kl1").toString();
					String vfree3 = map.get("kl2")==null?"":map.get("kl2").toString();
					String vfree4 = map.get("kl3")==null?"":map.get("kl3").toString();
					String vfree5 = map.get("kl4")==null?"":map.get("kl4").toString();
					String vfree6 = map.get("kl5")==null?"":map.get("kl5").toString();
					countXcl=countXcl.add(new UFDouble(vfree1)).add(new UFDouble(vfree2)).add(new UFDouble(vfree3)).add(new UFDouble(vfree4)).add(new UFDouble(vfree5)).add(new UFDouble(vfree6));
					jsonb.put("date", ts);//结算日期
					jsonb.put("depositing", String.valueOf(countXcl));
					jsonb.put("invclasscode", invclasscode1);
					jsonb.put("custcode", map.get("def1")==null?"":map.get("def1").toString());
					jsonb.put("custname", map.get("def2")==null?"":map.get("def2").toString());
					jsonb.put("avgprice", map.get("price")==null?"":map.get("price").toString());
					jsonb.put("vfree1", vfree1);
					jsonb.put("vfree2", vfree2);
					jsonb.put("vfree3", vfree3);
					jsonb.put("vfree4", vfree4);
					jsonb.put("vfree5", vfree5);
					jsonb.put("vfree6", vfree6);
//					jsonb.put("kl6", map.get("kl6")==null?"":map.get("kl6").toString());
//					jsonb.put("kl7", map.get("kl7")==null?"":map.get("kl7").toString());
//					jsonb.put("kl8", map.get("kl8")==null?"":map.get("kl8").toString());
//					jsonb.put("kl9", map.get("kl9")==null?"":map.get("kl9").toString());
//					jsonb.put("kl10", map.get("kl10")==null?"":map.get("kl10").toString());
					jsonb.put("je0", map.get("je0")==null?"":map.get("je0").toString());
					jsonb.put("je1", map.get("je1")==null?"":map.get("je1").toString());
					jsonb.put("je2", map.get("je2")==null?"":map.get("je2").toString());
					jsonb.put("je3", map.get("je3")==null?"":map.get("je3").toString());
					jsonb.put("je4", map.get("je4")==null?"":map.get("je4").toString());
					jsonb.put("je5", map.get("je5")==null?"":map.get("je5").toString());
//					jsonb.put("je6", map.get("je6")==null?"":map.get("je6").toString());
//					jsonb.put("je7", map.get("je7")==null?"":map.get("je7").toString());
//					jsonb.put("je8", map.get("je8")==null?"":map.get("je8").toString());
//					jsonb.put("je9", map.get("je9")==null?"":map.get("je9").toString());
//					jsonb.put("je10", map.get("je10")==null?"":map.get("je10").toString());
					if(i == list.size()-1){
						result.append(jsonb+"\n");
						result.append("]");
					}else{
						result.append(jsonb+","+"\n");
					}
				}
				jsonh.put("bodylist", result.toString());
			}else{
				jsonh.put("bodylist", "[]");
			}
			all.append(jsonh.toString());
			all.append("]");
			return all.toString();
		}
		return null;
	}
	
    public String getInvclasscode(String invcode) throws BusinessException{
    	StringBuffer sb = new StringBuffer();
    	sb.append(" select a.invclasscode from bd_invcl a ") 
    	.append(" left join bd_invbasdoc b on a.pk_invcl = b.pk_invcl ") 
    	.append(" where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
    	.append(" and b.invcode='"+invcode+"' ");
    	String invclasscode = "";
        invclasscode = (String) uap.executeQuery(sb.toString(), new ColumnProcessor());
        return invclasscode;
    }
}
