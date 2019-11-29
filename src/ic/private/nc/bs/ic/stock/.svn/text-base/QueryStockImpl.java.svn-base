package nc.bs.ic.stock;

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

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;

/**
 * 现存量查询实现接口
 * @author Longkaisheng
 * 2017-11-03
 * 
 * */
public class QueryStockImpl implements IHttpServletAdaptor{
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		StringBuffer sb = null;
		String s ="";
		request.setCharacterEncoding("UTF-8");
			InputStream in = request.getInputStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));  
	        sb = new StringBuffer();  
	        while ((s = br.readLine()) != null) {  
	            sb.append(s);  
	        }
	        String json = "";
			try {
				json = queryStock(sb.toString());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				json = "{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}";
			}
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	// add by lks 2017-11-03 查询现存量
	public String queryStock(String query) throws BusinessException {
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONObject json = null;
		try {
			json = new JSONObject().fromObject(query);//解析json
		} catch (Exception e1) {
			e1.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误"+e1.getMessage()+"\"}";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();
		Iterator iterator = json.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = json.getString(key);
			if (key.equals("bodylist")) {
				JSONArray array = json.getJSONArray(key);
				for (int i = 0; i < array.size(); i++) {
					String key1 = null;
					String value1 = null;
					JSONObject jb = array.getJSONObject(i);
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
		String username = mls.get("username").toString();//获取验证码
		String password = mls.get("password").toString();
		String corp= mls.get("corp").toString();//接收公司编号
		if (StringUtils.isBlank(username)) {
			return "{\"status\":\"error\",\"message\":\"用户名为空\"}";
		}
		if (StringUtils.isBlank(password)) {
			return "{\"status\":\"error\",\"message\":\"密码为空\"}";
		}
		if (!username.equals("baosteel")) {
			return "{\"status\":\"error\",\"message\":\"用户名错误\"}";
		}
		if (!password.equals("123456")) {
			return "{\"status\":\"error\",\"message\":\"密码错误\"}";
		}
		if (StringUtils.isBlank(corp)) {
			return "{\"status\":\"error\",\"message\":\"公司编码为空\"}";
		}
		String pk_corp = "";
		try {
			pk_corp = getcorp(corp);//查询公司主键
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}";
		}
		if(StringUtils.isBlank(pk_corp)){
			return "{\"status\":\"error\",\"message\":\"该公司编码:"+corp+"在NC未找到\"}";
		}
		List conykc = new ArrayList();
		for(int i =0 ;i<lsmap.size();i++){
			StringBuffer invclasscodestr = new StringBuffer();//获取存货分类查询条件
			Map invclasscodemap =lsmap.get(i);
			Map custcodemap = lsmap.get(i);
			Map invcodemap = lsmap.get(i);
			String invclasscode1 = invclasscodemap.get("invclasscode")==null?"":invclasscodemap.get("invclasscode").toString();
			String custcode1 = custcodemap.get("custcode")==null?"":custcodemap.get("custcode").toString();
			String invcode1 = invcodemap.get("invcode")==null?"":invcodemap.get("invcode").toString();
			if(StringUtils.isNotBlank(invclasscode1)){
				if(invclasscode1.startsWith("23")){
					String classcode = invclasscode1+"%";
					invclasscodestr.append("    and  a.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						invclasscodestr.append("     and a.custcode ='"+custcode1+"'    ");
						if(StringUtils.isNotBlank(invcode1)){
							invclasscodestr.append("     and a.invcode ='"+invcode1+"'    ");
						}
					}else{
						return "{\"status\":\"error\",\"message\":\"查询23有库存的物料需要传入客商编码作为查询条件\"}";
					}
				}else{
					String classcode = invclasscode1+"%";
					invclasscodestr.append("     and  a.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						return "{\"status\":\"error\",\"message\":\"查询21,22有库存的物料不需要传入客商编码作为查询条件\"}";
					}
					if(StringUtils.isNotBlank(invcode1)){
						invclasscodestr.append("     and a.invcode ='"+invcode1+"'    ");
					}
				}
				conykc.add(invclasscodestr);
			}else{
			   return "{\"status\":\"error\",\"message\":\"查询有库存物料存货分类编码必须作为查询条件\"}";
			}
		}
		List result1 = new ArrayList();
		for(int i = 0 ;i<conykc.size() ;i++){
			StringBuffer constr = (StringBuffer) conykc.get(i);
			StringBuffer sb = new StringBuffer();
		    sb.append(" select distinct a.pk_corp,     ") 
		    .append("                    a.ccalbodyid,  ") 
		    .append("                    a.storcode , ") 
		    .append("                    a.storname, ") 
		    .append("                    a.invclasscode,    ") 
		    .append("                    a.invcode,   ") 
		    .append("                    a.invname,    ") 
		    .append("                    a.custcode, ") 
		    .append("                    a.sealflag,   ") 
		    .append("                    a.num,                  ") 
		    .append("                    b.freezenum,     ") 
		    .append("                    a.ngrossnum,     ") 
		    .append("                    b.ngrossnum nfreezegrossnum     ") 
		    .append("               from (select kp.pk_corp,  ") 
		    .append("                            kp.ccalbodyid,     ") 
		    .append("                            kp.cwarehouseid,   ") 
		    .append("                            stor.storcode, ") 
		    .append("                            stor.storname,   ") 
		    .append("                            kp.cinventoryid,   ") 
		    .append("                            invcl.invclasscode,   ") 
		    .append("                            inv.invcode,   ") 
		    .append("                            inv.invname,    ") 
		    .append("                            cust.custcode, ") 
		    .append("                            man.sealflag,                       ") 
		    .append("                            SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num,                                ") 
		    .append("                            SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum     ") 
		    .append("                       from v_ic_onhandnum6 kp   ") 
		    .append("                       left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc    ") 
		    .append("                       left join bd_invbasdoc inv on man.pk_invbasdoc = inv.pk_invbasdoc    ") 
		    .append("                       left join bd_invcl invcl  on inv.pk_invcl = invcl.pk_invcl   ") 
		    .append("                       left join bd_cubasdoc cust on man.def17 = cust.pk_cubasdoc ") 
		    .append("                       left join bd_stordoc stor on kp.cwarehouseid = stor.pk_stordoc                ") 
		    .append("                        where  ((0 = 0) and (0 = 0) and     ") 
		    .append("                            (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and     ") 
		    .append("                            (kp.pk_corp = '"+pk_corp+"'))     ") 
		    .append("                      group by kp.pk_corp,    ") 
		    .append("                               kp.ccalbodyid,     ") 
		    .append("                               kp.cwarehouseid, ") 
		    .append("                               stor.storcode, ") 
		    .append("                               stor.storname,   ") 
		    .append("                               inv.invcode,   ") 
		    .append("                               inv.invname,   ") 
		    .append("                               cust.custcode, ") 
		    .append("                               man.sealflag,   ") 
		    .append("                               invcl.invclasscode,      ") 
		    .append("                               kp.cinventoryid) a,     ") 
		    .append("                    (select kp.pk_corp,    ") 
		    .append("                            kp.ccalbodyid,     ") 
		    .append("                            kp.cwarehouseid,  ") 
		    .append("                            stor.storcode, ") 
		    .append("                            stor.storname,    ") 
		    .append("                            kp.cinventoryid,   ") 
		    .append("                            invcl.invclasscode,   ") 
		    .append("                            inv.invcode,   ") 
		    .append("                            inv.invname,   ") 
		    .append("                            cust.custcode, ") 
		    .append("                            man.sealflag,                        ") 
		    .append("                            sum(nvl(nfreezenum, 0)) freezenum,     ") 
		    .append("                            sum(nvl(ngrossnum, 0)) ngrossnum     ") 
		    .append("                       from ic_freeze kp   ") 
		    .append("                       left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc    ") 
		    .append("                       left join bd_invbasdoc inv on man.pk_invbasdoc = inv.pk_invbasdoc    ") 
		    .append("                       left join bd_invcl invcl  on inv.pk_invcl = invcl.pk_invcl   ") 
		    .append("                       left join bd_cubasdoc cust on man.def17 = cust.pk_cubasdoc   ") 
		    .append("                        left join bd_stordoc stor on kp.cwarehouseid = stor.pk_stordoc  ") 
		    .append("                        where  (cthawpersonid is null and (0 = 0) and     ") 
		    .append("                            (0 = 0) and (0 = 0) and     ") 
		    .append("                            (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and     ") 
		    .append("                            (kp.pk_corp = '"+pk_corp+"'))     ") 
		    .append("                      group by kp.pk_corp,     ") 
		    .append("                               kp.ccalbodyid,     ") 
		    .append("                               kp.cwarehouseid,  ") 
		    .append("                               stor.storcode, ") 
		    .append("                               stor.storname,  ") 
		    .append("                               invcl.invclasscode,   ") 
		    .append("                               inv.invcode,   ") 
		    .append("                               inv.invname,     ") 
		    .append("                               cust.custcode, ") 
		    .append("                               man.sealflag,    ") 
		    .append("                               kp.cinventoryid) b     ") 
		    .append("              where a.pk_corp = b.pk_corp(+)     ") 
		    .append("                and a.ccalbodyid = b.ccalbodyid(+)       ") 
		    .append("                and a.cwarehouseid = b.cwarehouseid(+)     ") 
		    .append("                and a.cinventoryid = b.cinventoryid(+)             ") 
		    .append("                and (a.num <> 0)    ") 
		    .append("                and nvl(a.sealflag,'N')='N'  "); 
		    if(constr.length()>0){
				sb.append(""+constr+"");
			}
			List res = (List) uap.executeQuery(sb.toString(), new MapListProcessor());
			if(res.size()>0){
				result1.add(res);
			}
		}
		HashMap invmap = new HashMap();
		JSONObject jsonH = new JSONObject();
		jsonH.put("status", "success");
		jsonH.put("message", "获取成功");
		StringBuffer str = new StringBuffer();
		str.append("[");
		if(result1.size()>0){
			for(int i =0 ; i<result1.size() ;i++){
				List l = (List) result1.get(i);
				for(int j = 0 ; j<l.size() ;j++){
					Map wkcmap = (Map) l.get(j);
					JSONObject jsoninfo = new JSONObject();
					String invcode = wkcmap.get("invcode")==null?"":wkcmap.get("invcode").toString();//物料编码
					String invname = wkcmap.get("invname")==null?"":wkcmap.get("invname").toString();//物料名称
					String invclasscode = wkcmap.get("invclasscode")==null?"":wkcmap.get("invclasscode").toString();//存货分类编码
					String storcode = wkcmap.get("storcode")==null?"":wkcmap.get("storcode").toString();//仓库编码
					String storname = wkcmap.get("storname")==null?"":wkcmap.get("storname").toString();//仓库名称
					String num = wkcmap.get("num")==null?"":wkcmap.get("num").toString();//库存
					String freezenum = wkcmap.get("freezenum")==null?"0":wkcmap.get("freezenum").toString();//隔离数量
					jsoninfo.put("invcode", invcode);
					jsoninfo.put("invname", invname);
					jsoninfo.put("invclasscode", invclasscode);
					jsoninfo.put("storcode", storcode);
					jsoninfo.put("storname", storname);
					jsoninfo.put("depositing", num);//可用量
					jsoninfo.put("freezenum", freezenum);
					str.append(jsoninfo+",");
				}
			}
		}else{
			return "{\"status\":\"error\",\"message\":\"未查询到库存信息,请检查查询条件\"}";
		}
		str = str.deleteCharAt(str.length()-1);
		str.append("]");
		jsonH.put("bodylist", str.toString());
		return jsonH.toString();
		}
	private String getcorp(String unitcode) throws BusinessException{
		// 根据公司编码查询公司代码
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_corp from bd_corp where unitcode ='"+unitcode+"' and nvl(dr,0)=0");
		String pk_corp = (String) uap.executeQuery(sql.toString(), new ColumnProcessor());
		return pk_corp;
	}
}
