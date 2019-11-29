package nc.bs.dm.freight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

/**
 * 运费查询接口类
 * 入参：json:{cs:验证码}
 * 返回：json:{invcode:物料代码,invname:物料名称,depositing:可用数量,freezenum:隔离数量,storname:仓库名称,invtype:每垛数量}
 * @author Longkaisheng
 * 2017-11-03
 * 
 * */
@SuppressWarnings("all")
public class QueryFreightImpl implements IHttpServletAdaptor{
	
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		StringBuffer sb = null;
		String s ="";
		InputStream in = request.getInputStream();  
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));  
        sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }
		String json = this.queryFreight(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	//运费查询接口实现
	public String queryFreight(String gjson) {
		JSONObject json = null;
		try {
			json = new JSONObject().fromObject(gjson);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误\"}";
		}
		String username = json.getString("username");
		String password = json.getString("password");
		if ("baosteel".equals(username) && "123456".equals(password)) {
			String date = json.getString("date");
			String source = InvocationInfoProxy.getInstance().getUserDataSource();
			InvocationInfoProxy.getInstance().setUserDataSource(source);
			IUAPQueryBS dao = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = new ArrayList();
			StringBuffer sql=new StringBuffer();
			JSONArray jsonArr = new JSONArray();
			sql.append(" select distinct bd_cubasdoc.custcode, ") 
			.append("                 bd_cubasdoc.custname, ") 
			.append("                 bd_cubasdoc.pk_corp, ") 
			.append("                 dm_baseprice.dbaseprice, ") 
			.append("                 dm_baseprice.taxprice, ") 
			.append("                 dm_baseprice.rate, ") 
			.append("                 dm_baseprice.ipricetype, ") 
			.append("                 dm_baseprice.effectdate, ") 
			.append("                 dm_baseprice.expirationdate, ") 
			.append("                 bd_sendtype.sendcode, ") 
			.append("                 bd_sendtype.sendname, ") 
			.append("                 c.addrname   fromaddressname , ") 
			.append("                 d.addrname  toaddressname , ") 
			.append("                 dm_baseprice.pkfromaddress, ") 
			.append("                 dm_baseprice.pktoaddress,")
			.append("                 dm_baseprice.pk_basicprice")
			.append("   FROM dm_baseprice ") 
			.append("      left join  dm_trancust on dm_baseprice.pk_transcust = dm_trancust.pk_trancust ") 
			.append("        left join bd_cubasdoc on dm_trancust.pkcusmandoc = bd_cubasdoc.pk_cubasdoc ") 
			.append("        left join dm_vehicletype on dm_baseprice.pk_vehicletype = dm_vehicletype.pk_vehicletype ") 
			.append("       left join  bd_custaddr on bd_cubasdoc.pk_cubasdoc = bd_custaddr.pk_cubasdoc ") 
			.append("       left join  dm_transcontainer on dm_baseprice.pk_transcontainer = dm_transcontainer.pk_transcontainer ") 
			.append("       left join  bd_sendtype on dm_baseprice.pk_sendtype = bd_sendtype.pk_sendtype ") 
			.append("        left join bd_invcl on dm_baseprice.pk_invclass = bd_invcl.pk_invcl ") 
			.append("       left join  bd_invbasdoc on dm_baseprice.pk_inventory = bd_invbasdoc.pk_invbasdoc ") 
			.append("        left join dm_packsort on dm_baseprice.pkpacksort = dm_packsort.pk_packsort ") 
			.append("       left join  bd_areacl  a   on dm_baseprice.pkfromarea = a.pk_areacl ") 
			.append("       left join  bd_areacl  b   on dm_baseprice.pktoarea = b.pk_areacl ") 
			.append("        left join bd_address c   on dm_baseprice.pkfromaddress = c.pk_address ") 
			.append("        left join bd_address d   on dm_baseprice.pktoaddress = d.pk_address ") 
			.append("        where to_char(to_date(dm_baseprice.ts,'yyyy-mm-dd  hh24:mi:ss'),'yyyy-mm-dd') >='"+date+"' ")
			.append("        and nvl(dm_baseprice.dr,0)=0 "); 
			try {
				list=(List) dao.executeQuery(sql.toString(), new MapListProcessor());
				for (int i = 0; i < list.size(); i++) {
					Map map=(Map) list.get(i);
					String custcode = map.get("custcode") == null?"":map.get("custcode").toString();//承运商编号
					String custname = map.get("custname") == null?"":map.get("custname").toString();//承运商名称
					String dbaseprice = map.get("dbaseprice") == null?"":map.get("dbaseprice").toString();//无税单价
					String taxprice = map.get("taxprice") == null?"":map.get("taxprice").toString();//含税单价
					String rate = map.get("rate") == null?"":map.get("rate").toString();//税率
					String ipricetype = map.get("ipricetype") == null?"":map.get("ipricetype").toString();//计价依据
					String effectdate = map.get("effectdate") == null?"":map.get("effectdate").toString();//开始日期
					String expirationdate = map.get("expirationdate") == null?"":map.get("expirationdate").toString();//结束日期
					String sendcode = map.get("sendcode") == null?"":map.get("sendcode").toString();//发货方式编码
					String sendname = map.get("sendname") == null?"":map.get("sendname").toString();//发货方式名称
					String fromaddressname = map.get("fromaddressname") == null?"":map.get("fromaddressname").toString();//发货地址
					String toaddressname = map.get("toaddressname") == null?"":map.get("toaddressname").toString();//收货地址
					String fromaddressId = map.get("pkfromaddress") == null?"":map.get("pkfromaddress").toString();
					String toaddressId = map.get("pktoaddress") == null?"":map.get("pktoaddress").toString(); 
					String pk_basicprice = map.get("pk_basicprice").toString();//运费价格表主键
					if("0".equals(ipricetype)){
						ipricetype="重量";
					}else if("1".equals(ipricetype)){
						ipricetype="件数";
					}else if("2".equals(ipricetype)){
						ipricetype="整车";
					}else if("3".equals(ipricetype)){
						ipricetype="数量";
					}else if("4".equals(ipricetype)){
						ipricetype="体积";
					}
					JSONObject rjson = new JSONObject();
					rjson.put("custcode", custcode);
					rjson.put("custname", custname);
					rjson.put("dbaseprice", dbaseprice);
					rjson.put("taxprice", taxprice);
					rjson.put("rate", rate);
					rjson.put("ipricetype", ipricetype);
					rjson.put("effectdate", StringUtils.deleteWhitespace(effectdate));
					rjson.put("expirationdate", StringUtils.deleteWhitespace(expirationdate));
					rjson.put("sendcode", sendcode);
					rjson.put("sendname", sendname);
					rjson.put("fromaddressname", fromaddressname);
					rjson.put("toaddressname", toaddressname);
					rjson.put("fromaddressid", fromaddressId);
					rjson.put("toaddressid", toaddressId);
					rjson.put("pk_basicprice", pk_basicprice);
					jsonArr.add(rjson);
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库访问错误\"}";
			}
			return "{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":"+jsonArr.toString()+"}";
		}else{
			return "{\"status\":\"error\",\"message\":\"用户名或密码错误\"}";
		}
	}
}