package nc.bs.so.saleinvoiceImpl;

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

import nc.bs.dao.BaseDAO;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;
/**
 * 对账周期(外部接口)
 * 2018-1-30 14:45:49
 * @author PengJia
 *
 */
@SuppressWarnings("all")
public class CheckCycleToCVMImpl implements IHttpServletAdaptor {
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
		result = this.checkcycle(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
	}
	public String checkcycle(String jsonstr) {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
		List checkList = new ArrayList();
		try {
			jsonArrList = new JSONArray().fromObject(jsonstr);
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
						if(key.equals("bodylist")){
							JSONArray jsonArray = jsonObject.getJSONArray(key);
							for(int i = 0;i<jsonArray.size();i++){
								String key1 = null;
								String value1 =null;
								JSONObject jsonOb =  jsonArray.getJSONObject(i);
								Map map1 =new HashMap();
								Iterator iter = jsonOb.keys();
								while(iter.hasNext()){
									key1 = (String)iter.next();
									value1 = jsonOb.getString(key1);
									map1.put(key1, value1);
								}
									listMap.add(map1);
							}
						}else{
							mls.put(key, value);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+ e.getMessage() + "'\"}]";
				}

			}
		}
		
		StringBuffer custcode = new StringBuffer();
		custcode.append("(");
		for(int j = 0; listMap.size() > j; j++){
			String code = (String) listMap.get(j).get("custcode")==null?"":(String) listMap.get(j).get("custcode");
			if(j==listMap.size()-1){
				if(StringUtils.isNotBlank(code)){
					custcode.append("'");
					custcode.append(listMap.get(j).get("custcode")==null?"":listMap.get(j).get("custcode"));
					custcode.append("')");
				}else{
					custcode.deleteCharAt(custcode.length()-1).append(")");
				}
			}else{
				if(StringUtils.isNotBlank(code)){
					custcode.append("'");
					custcode.append(listMap.get(j).get("custcode")==null?"":listMap.get(j).get("custcode"));
					custcode.append("',");
				}
			}
		}
		StringBuffer corpcode = new StringBuffer();
		
		corpcode.append("(");
		for(int k = 0; listMap.size() > k; k++){
			String cc = (String) listMap.get(k).get("corp")==null?"":(String) listMap.get(k).get("corp");
			if(StringUtils.isNotBlank(cc)){
				String code = (String) listMap.get(k).get("corp");
				if(k==listMap.size()-1){
					if(StringUtils.isNotBlank(code)){
						corpcode.append("'");
						corpcode.append(listMap.get(k).get("corp")==null?"":listMap.get(k).get("corp"));
						corpcode.append("')");
					}else{
						corpcode.deleteCharAt(corpcode.length()-1).append(")");
					}
				}else{
					if(StringUtils.isNotBlank(code)){
						corpcode.append("'");
						corpcode.append(listMap.get(k).get("corp")==null?"":listMap.get(k).get("corp"));
						corpcode.append("',");
					}
				}
			}else if(StringUtils.isBlank(cc)){
				return "[{\"status\":\"error\",\"message\":\"公司不能为空\"}]";
			}
			
		}
		String username = (String) mls.get("username")==null?"":(String) mls.get("username");
		String password = (String) mls.get("password")==null?"":(String) mls.get("password");
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"接口连接标识错误或者标识为空\"}]";
		}
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"接口连接标识错误或者标识为空\"}]";
		}
		JSONArray jsonArray = new JSONArray();
		String date = (String) mls.get("jsdate");
		for(int i = 0;i<listMap.size();i++){
			HashMap map1 = new HashMap();
			map1 = (HashMap) listMap.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append(" select sum(a) dzqty, sum(b)sumdz, sum(b) / sum(a)avgdz,custcode,pk_custaddr,unitcode"); 
			sb.append("   from (select sum(saleb.nnumber) a, "); 
			sb.append("                ceil((To_date(sale.dbilldate, 'yyyy-mm-dd hh24-mi-ss') - "); 
			sb.append("                     To_date(ich.dbilldate, 'yyyy-mm-dd hh24-mi-ss'))), "); 
			sb.append("                sum(saleb.nnumber) * "); 
			sb.append("                ceil((To_date(sale.dbilldate, 'yyyy-mm-dd hh24-mi-ss') - "); 
			sb.append("                     To_date(ich.dbilldate, 'yyyy-mm-dd hh24-mi-ss'))) b, "); 
			sb.append("                cubas.custcode,addr.pk_custaddr,corp.unitcode  "); 
			sb.append("           from so_saleinvoice sale "); 
			sb.append("           left join so_saleinvoice_b saleb "); 
			sb.append("             on sale.csaleid = saleb.csaleid "); 
			sb.append("           join ic_general_b icb "); 
			sb.append("             on icb.cgeneralbid = saleb.cupsourcebillbodyid "); 
			sb.append("           left join ic_general_h ich "); 
			sb.append("             on ich.cgeneralhid = icb.cgeneralhid "); 
			sb.append("           left join bd_cumandoc cuman "); 
			sb.append("             on sale.creceiptcorpid = cuman.pk_cumandoc "); 
			sb.append("           left join bd_cubasdoc cubas "); 
			sb.append("             on cuman.pk_cubasdoc = cubas.pk_cubasdoc "); 
			sb.append("           left join bd_corp corp "); 
			sb.append("             on sale.pk_corp = corp.pk_corp ");
			sb.append("     	  left join bd_custaddr addr");
            sb.append("	            on addr.pk_cubasdoc = cubas.pk_cubasdoc");              
			if(custcode.length()>1){
				sb.append("          where cubas.custcode in "+custcode+" and"); 
			}else{
				sb.append("          where "); 
			}
			sb.append("             corp.unitcode in "+corpcode+" "); 
			sb.append("            and sale.dbilldate = '"+date+"' ");
			sb.append("            and sale.fstatus = '2'");
			sb.append("            and ich.cbiztype <>'1016A210000000098ZO3'");
			sb.append("          group by sale.dbilldate, "); 
			sb.append("                   ich.dbilldate, ");
			sb.append("                   saleb.nnumber, "); 
			sb.append("                   cubas.custcode,addr.pk_custaddr,corp.unitcode) "); 
			sb.append("  		 group by custcode,pk_custaddr,unitcode ") ;
			try {	
				checkList = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库查询异常'"+ e.getMessage() + "'\"}]";
			}
			for(int j = 0; j<checkList.size();j++){
				DecimalFormat df = new DecimalFormat("######0.00");
				JSONObject jsonOb = new JSONObject();
				HashMap checkMap = new HashMap();
				checkMap = (HashMap) checkList.get(j);
				String custno = (String) checkMap.get("custcode");
				String avgnum = df.format(new UFDouble(checkMap.get("avgdz").toString()));
				String suma = df.format(new UFDouble(checkMap.get("dzqty").toString()));
				String sumb = df.format(new UFDouble(checkMap.get("sumdz").toString()));
				String addr = (String) checkMap.get("pk_custaddr")==null?"":(String) checkMap.get("pk_custaddr");
				String corp = (String) checkMap.get("unitcode");
				jsonOb.put("custno", custno);
				jsonOb.put("avgdz", avgnum);
				jsonOb.put("dzqty", suma);
				jsonOb.put("sumdz", sumb);
				jsonOb.put("custaddre", addr);
				jsonOb.put("corp", corp);
				jsonArray.add(jsonOb);
			}
		}
		if(checkList.size()>0){
			return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + jsonArray.toString() + "}]";
		}else{
			return "[{\"status\":\"error\",\"message\":\"暂无可显示的数据\"}]";
		}
	}

}