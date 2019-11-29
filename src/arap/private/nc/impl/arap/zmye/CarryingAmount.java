package nc.impl.arap.zmye;

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
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
/**
 * 客户账面余额反馈
 * 2018年5月10日15:17:27
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class CarryingAmount implements IHttpServletAdaptor{
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = null;
		InputStream inputStream = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		String jsonStr = buffer.toString();
		result = this.carryAmount(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
}
	public String carryAmount(String json){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
		try {
			jsonArrList = new JSONArray().fromObject(json);
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
		String username = (String) mls.get("username");
		String password = (String) mls.get("password");
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		StringBuffer sb = new StringBuffer();
		String CVMcustcode = mls.get("custcode").toString();
		String CVMunitcode = mls.get("unitcode").toString();
		sb.append(" select sum(nvl(a.ysje, 0) - nvl(a.skje, 0)) ye, a.custcode,a.unitcode ") 
		.append("   from (select decode(zb.djdl, 'ys', sum(fb.Jfbbje), '0') ysje, ") 
		.append("                decode(zb.djdl, 'sk', sum(fb.ybye), '0') skje, ") 
		.append("                bas.custcode, ")
		.append("                corp.unitcode")
		.append("           from arap_djzb zb ") 
		.append("           left join arap_djfb fb ") 
		.append("             on zb.VOUCHID = fb.VOUCHID ") 
		.append("           left join bd_cumandoc man ") 
		.append("             on man.pk_cumandoc = fb.ksbm_cl ") 
		.append("           left join bd_cubasdoc bas ") 
		.append("             on bas.pk_cubasdoc = man.pk_cubasdoc ") 
		.append("           left join bd_corp corp  ") 
		.append("             on corp.pk_corp = zb.dwbm ") 
		.append("          where bas.custcode = '"+CVMcustcode+"' ") 
		.append("            and zb.djzt = '2' ") 
		.append("            and corp.unitcode='"+CVMunitcode+"' ") 
		.append("            and nvl(zb.dr,0)=0 ") 
		.append("            and nvl(fb.dr,0)=0        ") 
		.append("          group by bas.custcode, zb.djdl,corp.unitcode) a ") 
		.append("  group by a.custcode,a.unitcode ");
		List list = new ArrayList();
		Map map = new HashMap();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		 try {
			 list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"查询数据异常'"+ e.getMessage() + "'\"}]";
		}
		if(list.size()>0){
			for(int i = 0 ;i<list.size();i++){
			  map = (Map) list.get(i);
			  String balance = map.get("ye")==null?"0":map.get("ye").toString();
			  String custcode = map.get("custcode").toString();
			  String unitcode = map.get("unitcode").toString();
			  jsonObj.put("username", "baosteel");
			  jsonObj.put("password", "123456");
			  jsonObj.put("balance", balance);
			  jsonObj.put("custcode", custcode);
			  jsonObj.put("unitcode", unitcode);
			}
			jsonArray.add(jsonObj);
		}
		return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + jsonArray.toString() + "}]";
	}
}
