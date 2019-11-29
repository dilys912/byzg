package nc.impl.production;

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

import nc.bs.dao.BaseDAO;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
/**
 * 成品生产下线数据
 * 2018年6月1日10:27:39
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class ProductionOut implements IHttpServletAdaptor{

	public void doAction(HttpServletRequest request, HttpServletResponse respone) throws ServletException, IOException {
		InputStream inputStream = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
		StringBuffer sb = new StringBuffer();
		String line ="";
		while((line = in.readLine())!=null){
			sb.append(line);
		}
		String result = this.ProductionSendCVM(sb.toString());
		respone.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
		
	}
	public String ProductionSendCVM (String jsonStr){
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
		try {
			jsonArrList = new JSONArray().fromObject(jsonStr);
		} catch (Exception e1) {
			e1.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+ e1.getMessage() + "'\"}]";
		}
		JSONObject jsonObject = null;
		if (jsonArrList.size() > 0) {
			for (int j = 0; j < jsonArrList.size(); j++) {
				try {
					jsonObject = jsonArrList.getJSONObject(j);
					Iterator iterator = jsonObject.keys();
					String key = null;
					String value = null;
					while (iterator.hasNext()) {
						key = (String) iterator.next();
						value = jsonObject.getString(key);
							mls.put(key, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+ e.getMessage() + "'\"}]";
				}

			}
		}
		//用户名
		String username = mls.get("username")==null?"":mls.get("username").toString();
		//密码
		String password = mls.get("password")==null?"":mls.get("password").toString();
		
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		//公司
		String unitcode = mls.get("unitcode")==null?"":mls.get("unitcode").toString();
		
		//开始时间
		String sdate = mls.get("startdate")==null?"":mls.get("startdate").toString();
		String startdate = sdate+' '+"08:30:00";
		
		//结束时间
		String edate = mls.get("enddate")==null?"":mls.get("enddate").toString();
		String enddate = edate+' '+"08:30:00";
	
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		JSONObject Strjson = new JSONObject();
		JSONArray  strjsonArr  = new JSONArray();
		StringBuffer sb = new StringBuffer();
		sb.append(" select invcode as invcode, invclasscode as invclasscode,xxrq as xxrq,sum(innum) innum ") 
		.append("   from v_view_ProductionOut ") 
		.append("  where ssdate >= to_date('"+startdate+"', 'yyyy-mm-dd hh24:mi:ss') ") 
		.append("    and ssdate < to_date('"+enddate+"', 'yyyy-mm-dd hh24:mi:ss') ") 
		.append("    and unitcode = '"+unitcode+"' ") 
		.append("  group by invcode, invclasscode,xxrq ") ;
		List list = new ArrayList();
		Map map = new HashMap();
		try {
			list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"查询数据异常'"+e.getMessage()+"'\"}]";
		}
		if(list.size()>0){
			for(int  a = 0;a<list.size();a++){
				map = (Map) list.get(a);
				//存货编码
				String invcode = map.get("invcode")==null?"":map.get("invcode").toString();
				//存货分类
				String invclasscode = map.get("invclasscode")==null?"": map.get("invclasscode").toString();
				//客户编码
				String custcode = QueryCustcode(invcode,unitcode)==null?"":QueryCustcode(invcode,unitcode).toString();
				//成品下线数量
				String innum = map.get("innum")==null?"0":map.get("innum").toString();
				//下线日期
				String xxrq = map.get("xxrq")==null?"":map.get("xxrq").toString();
				Strjson.put("invcode", invcode);
				Strjson.put("invclasscode", invclasscode);
				Strjson.put("unitcode", unitcode);
				Strjson.put("custcode", custcode);
				Strjson.put("innum", innum);
				Strjson.put("xxrq", xxrq);
				strjsonArr.add(Strjson);
			}
		}else{
			return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":"+strjsonArr.toString()+"}]";
		}
		return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + strjsonArr.toString() + "}]";
	}
	/**
	 * 依据存货编码查询默认客户代码
	 * @param invcode
	 * @param corp
	 * @return
	 */
	private  String QueryCustcode(String invcode,String corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String custcode = null;
		StringBuffer sb = new StringBuffer();
		sb.append("  select invman.def17,cubas.custcode,cubas.custname,corp.unitcode  ") 
		.append("        from bd_invbasdoc invbas   ") 
		.append("        left join bd_invmandoc invman   ") 
		.append("          on invbas.pk_invbasdoc = invman.pk_invbasdoc   ") 
		.append("        left join bd_cubasdoc cubas   ") 
		.append("          on cubas.pk_cubasdoc = invman.def17  ") 
		.append("        left join bd_corp corp  ") 
		.append("          on corp.pk_corp =  invman.pk_corp ") 
		.append("       where invbas.invcode = '"+invcode+"'") 
		.append("         and corp.unitcode ='"+corp+"' ") 
		.append("         and nvl(invbas.dr,0)=0   ") 
		.append("         and invman.sealflag ='N'") ;
		try {
			List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			Map map = new HashMap();
			
			if(list.size()>0){
				for(int i =0;i<list.size();i++){
					map = (Map) list.get(i);
					custcode = (String) map.get("custcode");
				}
			}else{
				return null;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return custcode;
	}
	
}
