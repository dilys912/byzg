package nc.bs.so.saleinvoiceImpl;

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
import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

import com.ibm.db2.jcc.b.cb;

/**
 * 2018年1月12日10:57:42
 * 接收cvm条件,返回审核销售发票数据(外部接口)
 * @author PengJia
 *
 */
@SuppressWarnings("all")
public class SaleToSendCVMImpl implements IHttpServletAdaptor {

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
		result = this.saleToSendCVM(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
	}
	public String saleToSendCVM(String jsonstr) {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
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
							for (int i = 0; i < jsonArray.size(); i++) {
								String key1 = null;
								String value1 = null;
								JSONObject jsonOb = jsonArray.getJSONObject(i);
								Map map1 = new HashMap();
								Iterator iter = jsonOb.keys();
								while (iter.hasNext()) {
									key1 = (String) iter.next();
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
		JSONArray jsonArray = new JSONArray();
		JSONArray listArr = new JSONArray();
		StringBuffer custcode = new StringBuffer();
		
		custcode.append("(");
		for(int j = 0; listMap.size() > j; j++){
			String code = (String) listMap.get(j).get("kscode")==""?"":(String) listMap.get(j).get("kscode");
			if(j==listMap.size()-1){
				if(StringUtils.isNotBlank(code)){
					custcode.append("'");
					custcode.append(listMap.get(j).get("kscode")==null?"":listMap.get(j).get("kscode"));
					custcode.append("')");
				}else{
					custcode.deleteCharAt(custcode.length()-1).append(")");
				}
			}else{
				if(StringUtils.isNotBlank(code)){
					custcode.append("'");
					custcode.append(listMap.get(j).get("kscode")==null?"":listMap.get(j).get("kscode"));
					custcode.append("',");
				}
			}
		}
		StringBuffer corpcode = new StringBuffer();
		corpcode.append("(");
		String code=null;
		for(int k = 0; listMap.size() > k; k++){
			 code = (String) listMap.get(k).get("corp")==null?"":(String) listMap.get(k).get("corp");
			if(StringUtils.isNotBlank(code)){
				if(k==listMap.size()-1){
					if(StringUtils.isNotBlank(code)){
						corpcode.append("'");
						corpcode.append(listMap.get(k).get("corp")==null?"":listMap.get(k).get("corp"));
						corpcode.append("')");
					}
				}else{
					if(StringUtils.isNotBlank(code)){
						corpcode.append("'");
						corpcode.append(listMap.get(k).get("corp")==null?"":listMap.get(k).get("corp"));
						corpcode.append("',");
					}
				}
			}else if(StringUtils.isBlank(code)){
				return "[{\"status\":\"error\",\"message\":\"公司不能为空\"}]";
			}
		}
		String cbiztype =null;
		if(code.equals("10301")){
			cbiztype ="1016A21000000000WOPI"; //成品销售业务流程(上海)
		}else if(code.equals("10307")){
			cbiztype ="1071A21000000000OTC4"; //成品销售业务流程(武汉)
		}else if (code.equals("10314")){
			cbiztype ="1103A2100000000016TZ"; //成品销售业务流程(哈尔滨)
		}else if(code.equals("10313")){
			cbiztype ="1097A2100000000018J9"; //成品销售业务流程(河南)
		}else if(code.equals("10302")){
			cbiztype ="1017A210000000012Z99"; //成品销售业务流程(河北)
		}else if(code.equals("10303")){
			cbiztype ="1018A21000000001HZS9"; //成品销售业务流程(成都)
		}else if(code.equals("10304")){
			cbiztype ="1019A210000000013ZXI"; //成品销售业务流程(佛山)
		}else if(code.equals("10318")){
			cbiztype ="110711100000000000SI"; //成品销售业务流程(宝翼2)
		}
		String sql = "select pk_corp from bd_corp where unitcode ='"+code+"'";
		Object pk_corp =null;
		try {
			pk_corp =  new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e1) {
			e1.printStackTrace();
		}
		String username = (String) mls.get("username");
		String password = (String) mls.get("password");
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		List saleList = new ArrayList();
		    String bdate = (String) mls.get("bdate");
		    String edate = (String) mls.get("edate");
			for (int i = 0; listMap.size() > i; i++) {
				HashMap map1 = new HashMap();
				map1 = (HashMap) listMap.get(i);
				BaseDAO dao = new BaseDAO();
				JSONObject jsonOb = new JSONObject();
				StringBuffer sb = new StringBuffer();
				sb.append("  select           corp.unitcode corp,   ") 
				.append("                     cubas.custcode kscode,   ") 
				.append("                     addr.pk_address vdiliveraddress,   ") 
				.append("                     invcl.invclasscode chfl,   ") 
				.append("                     sale.dbilldate fhrq,   ") 
				.append("                     sale.vreceiptcode vbillcode,   ") 
				.append("                     sum(saleb.nnumber) fhqty   ") 
				.append("                from so_saleinvoice sale   ") 
				.append("                left join so_saleinvoice_b saleb   ") 
				.append("                  on sale.csaleid = saleb.csaleid ") 
				.append("                left join ic_general_b b ") 
				.append("                  on b.cgeneralbid = saleb.cupsourcebillbodyid ") 
				.append("                left join ic_general_h h ") 
				.append("                  on b.cgeneralhid = h.cgeneralhid   ") 
				.append("                left join bd_cumandoc cuman   ") 
				.append("                  on sale.creceiptcorpid = cuman.pk_cumandoc   ") 
				.append("                left join bd_cubasdoc cubas   ") 
				.append("                  on cuman.pk_cubasdoc = cubas.pk_cubasdoc   ") 
				.append("                left join bd_address addr   ") 
				.append("                  on addr.addrname = h.vdiliveraddress and addr.pk_corp ='0001'  ") 
				.append("                left join bd_salestru sales   ") 
				.append("                  on sales.csalestruid = sale.csalecorpid   ") 
				.append("                left join bd_invbasdoc invbas   ") 
				.append("                  on saleb.cinvbasdocid = invbas.pk_invbasdoc   ") 
				.append("                left join bd_invcl invcl   ") 
				.append("                  on invbas.pk_invcl = invcl.pk_invcl   ") 
				.append("                left join bd_corp corp   ") 
				.append("                  on sale.pk_corp = corp.pk_corp  "); 
				if(custcode.length()>1){
						sb.append("        where cubas.custcode in "+custcode+"  and ") ;
					}else{
						sb.append("where ");
					}
			  sb.append("   sale.dbilldate >= ") 
				.append("                     to_char(to_date('"+bdate+"', 'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd')   ") 
				.append("                 and sale.dbilldate <=   ") 
				.append("                     to_char(to_date('"+edate+"', 'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd')   ") 
				.append("                 and corp.unitcode in "+corpcode+"") 
				.append("                 and sale.fstatus = '2'   ") 
				.append("                 and nvl(sale.dr, 0) = 0   ") 
				.append("                 and nvl(saleb.dr, 0) = 0   ") 
				.append("                 and sale.cbiztype = '"+cbiztype+"' ")
				.append("               group by corp.unitcode,   ") 
				.append("                        cubas.custcode,   ") 
				.append("                        addr.pk_address,   ") 
				.append("                        invcl.invclasscode,   ") 
				.append("                        sale.dbilldate,   ") 
				.append("                        sale.vreceiptcode "); 
			      /**
			       * 修改获取收发货地址信息
			       */
//			      sb.append(" select corp.unitcode corp, ") 
//			      .append("        cubas.custcode kscode, ") 
//			      .append("        addr.pk_address vdiliveraddress, ") 
//			      .append("        invcl.invclasscode chfl, ") 
//			      .append("        sale.dbilldate fhrq, ") 
//			      .append("        sale.vreceiptcode vbillcode, ") 
//			      .append("        sum(saleb.nnumber) fhqty ") 
//			      .append("   from so_saleinvoice sale ") 
//			      .append("   left join so_saleinvoice_b saleb ") 
//			      .append("     on sale.csaleid = saleb.csaleid ") 
//			      .append("   left join bd_cumandoc cuman ") 
//			      .append("     on sale.creceiptcorpid = cuman.pk_cumandoc ") 
//			      .append("   left join bd_cubasdoc cubas ") 
//			      .append("     on cuman.pk_cubasdoc = cubas.pk_cubasdoc ") 
//			      .append("   left join bd_address addr ") 
//			      .append("     on addr.addrname = sale.vreceiveaddress ") 
//			      .append("   left join bd_salestru sales ") 
//			      .append("     on sales.csalestruid = sale.csalecorpid ") 
//			      .append("   left join bd_invbasdoc invbas ") 
//			      .append("     on saleb.cinvbasdocid = invbas.pk_invbasdoc ") 
//			      .append("   left join bd_invcl invcl ") 
//			      .append("     on invbas.pk_invcl = invcl.pk_invcl ") 
//			      .append("   left join bd_corp corp ") 
//			      .append("     on sale.pk_corp = corp.pk_corp ");
//			      if(custcode.length()>1){
//						sb.append("        where cubas.custcode in "+custcode+"  and ") ;
//					}else{
//						sb.append("where ");
//					}
//			      sb.append("   sale.dbilldate >= ") 
//			      .append("        to_char(to_date('"+bdate+"', 'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd') ") 
//			      .append("    and sale.dbilldate <= ") 
//			      .append("        to_char(to_date('"+edate+"', 'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd') ") 
//			      .append("    and corp.unitcode in "+corpcode+"") 
//			      .append("    and sale.fstatus = '2' ") 
//			      .append("    and nvl(sale.dr, 0) = 0 ") 
//			      .append("    and nvl(saleb.dr, 0) = 0 ") 
//			      .append("    and sale.cbiztype != '1016A210000000098ZO3'") 
//			      .append("  group by corp.unitcode, ") 
//			      .append("           cubas.custcode, ") 
//			      .append("           addr.pk_address, ") 
//			      .append("           invcl.invclasscode, ") 
//			      .append("           sale.dbilldate, ") 
//			      .append("           sale.vreceiptcode");
				try {
					saleList = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
				} catch (BusinessException e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"数据库查询异常'"+ e.getMessage() + "'\"}]";
				}
					for (int j = 0; j < saleList.size(); j++) {
						JSONObject json = null;
						HashMap map = (HashMap) saleList.get(j);
						String vbillcode = (String) map.get("vbillcode");
						for (int y = 0; y < jsonArray.size(); y++) {
							jsonOb = (JSONObject) jsonArray.get(y);
							if (vbillcode.equals(jsonOb.get("vbillcode"))) {
								json = jsonOb;
								break;
							}
						}
						boolean flag = false;
						if (json == null) {
							flag = true;
							json = new JSONObject();
							json.put("corp", map.get("corp"));
							json.put("kscode", map.get("kscode"));
							json.put("vdiliveraddress", map.get("vdiliveraddress")==null?"":map.get("vdiliveraddress").toString());
							json.put("fhrq", map.get("fhrq")); 
							json.put("vbillcode", map.get("vbillcode"));
						}
						JSONObject jsonBody = null;
						JSONArray ja1 = null;
						Object object = json.get("bodylist");
						if (object == null) {
							ja1 = new JSONArray();
						} else {
							ja1 = (JSONArray) object;
						}
						jsonBody = new JSONObject();
						jsonBody.put("chfl", map.get("chfl"));
						jsonBody.put("fhqty", map.get("fhqty"));
						ja1.add(jsonBody);
						json.put("bodylist", ja1);
						if (flag) {
							jsonArray.add(json);
						}
					}
		       }
		if (saleList.size() > 0) {
			return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + jsonArray.toString() + "}]";
		} else {
			return "[{\"status\":\"error\",\"message\":\"暂无可显示的数据\"}]";
		}
	}
}


