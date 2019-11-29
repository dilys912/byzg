package nc.bs.ic.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.runtime.directive.Foreach;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.common.ListProcessor;
import nc.vo.cvm.log.LogVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * 现存量查询接口
 * 入参：String{ca:验证码,corp:公司编号}
 * 返回：json{invcode:存货编码}
 * @author Longkaisheng
 * 2017-11-13
 * 
 * */
public class QueryNoStockImpl implements IHttpServletAdaptor{
	IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	/*String corpLog = "";*///add by zwx 2019-4-2 用于日志记录
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		StringBuffer sb = null;
		String s ="";
			InputStream in = request.getInputStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
	        sb = new StringBuffer();  
	        while ((s = br.readLine()) != null) {  
	            sb.append(s);  
	        }
		String json = "";
		try {
			json = queryNoStock(sb.toString());
			//edit by zwx 2019-4-2 接口日志增加
			/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String startTime=df.format(new Date());//开始调用时间
			json = queryNoStock(sb.toString());
			String endTime=df.format(new Date());//结束时间
			recordLogs(sb.toString(),startTime,endTime);*/
			//end by zwx
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json = "{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}";
		}
		response.getOutputStream().write(json.getBytes("UTF-8"));
	}
	
	public String queryNoStock(String q) throws BusinessException {
		JSONObject json = null;
		try {
			json = new JSONObject().fromObject(q);//解析json
		} catch (Exception e1) {
			e1.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误"+e1.getMessage()+"\"}";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();
		Iterator iterator = json.keys();//获取json所有的key值
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
		
		String username = mls.get("username").toString();//获取用户名
		String password = mls.get("password").toString();//获取密码
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
			//corpLog = pk_corp;//add by zwx 2019-4-2 日志存放公司
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}";
		}
		if(StringUtils.isBlank(pk_corp)){
			return "{\"status\":\"error\",\"message\":\"该公司编码:"+corp+"在NC未找到\"}";
		}
		List conwkc = new ArrayList();
		for(int i =0 ;i<lsmap.size();i++){
			StringBuffer invclasscodestr = new StringBuffer();//获取存货分类查询条件
			Map invclasscodemap =lsmap.get(i);
			Map custcodemap = lsmap.get(i);
			String invclasscode1 = invclasscodemap.get("invclasscode")==null?"":invclasscodemap.get("invclasscode").toString();
			String custcode1 = custcodemap.get("custcode")==null?"":custcodemap.get("custcode").toString();
			if(StringUtils.isNotBlank(invclasscode1)){
				if(invclasscode1.startsWith("23")){
					String classcode = invclasscode1+"%";
					invclasscodestr.append("    and  invcl.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						invclasscodestr.append("     and cust.custcode ='"+custcode1+"'    ");
					}else{
						return "{\"status\":\"error\",\"message\":\"查询23无库存及新建物料需要传入客商编码作为查询条件\"}";
					}
				}else{
					String classcode = invclasscode1+"%";
					invclasscodestr.append("     and  invcl.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						return "{\"status\":\"error\",\"message\":\"查询21,22无库存及新建物料不需要传入客商编码作为查询条件\"}";
					}
				}
				conwkc.add(invclasscodestr);
			}else{
			   return "{\"status\":\"error\",\"message\":\"查询无库存及新建物料存货分类编码必须作为查询条件\"}";
			}
		}
		
		/*List connew = new ArrayList();
		for(int i =0 ;i<lsmap.size();i++){
			StringBuffer invclasscodestr = new StringBuffer();//获取存货分类查询条件
			Map invclasscodemap =lsmap.get(i);
			Map custcodemap = lsmap.get(i);
			String invclasscode1 = invclasscodemap.get("invclasscode")==null?"":invclasscodemap.get("invclasscode").toString();
			String custcode1 = custcodemap.get("custcode")==null?"":custcodemap.get("custcode").toString();
			if(StringUtils.isNotBlank(invclasscode1)){
				if(invclasscode1.startsWith("23")){
					String classcode = invclasscode1+"%";
					invclasscodestr.append("    and  a.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						invclasscodestr.append("     and a.custcode ='"+custcode1+"'    ");
					}else{
						return "{\"status\":\"error\",\"message\":\"查询23新建的物料需要传入客商编码作为查询条件\"}";
					}
					connew.add(invclasscodestr);
				}
			}else{
			   return "{\"status\":\"error\",\"message\":\"查询新建的物料存货分类编码必须作为查询条件\"}";
			}
		
		}*/
//		StringBuffer custcodestr = new StringBuffer();//获取客商查询条件
//		for(int i =0 ;i<lsmap.size();i++){
//			Map custcodemap =lsmap.get(i);
//			String custcode1 = custcodemap.get("custcode")==null?"":custcodemap.get("custcode").toString();
//			if(StringUtils.isNotBlank(custcode1)){
//					custcodestr.append("  a.custcode ='"+custcode1+"' or" );
//			}
//		}
//		StringBuffer invcodestr = new StringBuffer();//获取存货编码查询条件
//		for(int i =0 ;i<lsmap.size();i++){
//			Map invcodemap =lsmap.get(i);
//			String invcode1 = invcodemap.get("invcode")==null?"":invcodemap.get("invcode").toString();
//			if(StringUtils.isNotBlank(invcode1)){
//					invcodestr.append("  a.invcode ='"+invcode1+"'  or" );
//			}
//		}
//		String invclasscodestrstring = "";
//		String custcodestrstring = "";
//		String invcodestrstring = "";
//		//循环遍历条件去掉最后一个or
//		if(invclasscodestr.length()>0){
//			invclasscodestrstring = invclasscodestr.substring(0, invclasscodestr.length()-2);
//		}
//		if(custcodestr.length()>0){
//			custcodestrstring = custcodestr.substring(0, custcodestr.length()-2);
//		}
//		if(invcodestr.length()>0){
//			invcodestrstring = invcodestr.substring(0, invcodestr.length()-2);
//		}
		List result1 = new ArrayList();
		for(int i =0;i<conwkc.size();i++){
			StringBuffer constr = (StringBuffer) conwkc.get(i);
			StringBuffer sb = new StringBuffer();
			//edit by mcw
			sb.append(" select distinct man.pk_corp, ")
			.append("                 inv.pk_invbasdoc, ")
			.append("                 inv.invcode, ")
			.append("                 inv.invname, ")
			.append("                 invcl.invclasscode, ")
			.append("                 cust.custcode, ")
			.append("                 man.sealflag, ")
			.append("                 onh.num ")
			.append("   from bd_invbasdoc inv ")
			.append("   left join bd_invmandoc man ")
			.append("     on man.pk_invbasdoc = inv.pk_invbasdoc ")
			.append("   left join bd_invcl invcl ")
			.append("     on inv.pk_invcl = invcl.pk_invcl ")
			.append("   left join bd_cubasdoc cust ")
			.append("     on man.def17 = cust.pk_cubasdoc ")
			.append("   left join (select kp.pk_corp, ")
			.append("                     kp.ccalbodyid, ")
			.append("                     kp.cinventoryid, ")
			.append("                     invcl.invclasscode, ")
			.append("                     inv.invcode, ")
			.append("                     inv.invname, ")
			.append("                     cust.custcode, ")
			.append("                     man.sealflag, ")
			.append("                     SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num, ")
			.append("                     SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum ")
			.append("                from v_ic_onhandnum6 kp ")
			.append("                left join bd_invmandoc man ")
			.append("                  on kp.cinventoryid = man.pk_invmandoc ")
			.append("                left join bd_invbasdoc inv ")
			.append("                  on man.pk_invbasdoc = inv.pk_invbasdoc ")
			.append("                left join bd_invcl invcl ")
			.append("                  on inv.pk_invcl = invcl.pk_invcl ")
			.append("                left join bd_cubasdoc cust ")
			.append("                  on man.def17 = cust.pk_cubasdoc ")
			.append("                               group by kp.pk_corp, ")
			.append("                        kp.ccalbodyid, ")
			.append("                        inv.invcode, ")
			.append("                        inv.invname, ")
			.append("                        cust.custcode, ")
			.append("                        man.sealflag, ")
			.append("                        invcl.invclasscode, ")
			.append("                        kp.cinventoryid) onh ")
			.append("     on man.pk_invmandoc = onh.cinventoryid ")
			.append("  where nvl(man.sealflag, 'N') = 'N' ")
			.append("    and nvl(onh.num, 0) = 0 ")
			.append("    and man.pk_corp ='"+pk_corp+"'   ");
			//end  by mcw
			/*sb.append("  select distinct a.pk_corp,   ") 
			.append("             a.ccalbodyid,  ") 
			.append("             a.invclasscode,  ") 
			.append("             a.invcode, ") 
			.append("             a.invname,  ") 
			.append("             a.custcode,")
			.append("             a.sealflag,              ") 
			.append("             a.num,   ") 
			.append("             b.freezenum,   ") 
			.append("             a.ngrossnum,   ") 
			.append("             b.ngrossnum nfreezegrossnum   ") 
			.append("        from (select kp.pk_corp,   ") 
			.append("                     kp.ccalbodyid,   ") 
			//.append("                     kp.cwarehouseid,   ") 
			.append("                     kp.cinventoryid, ") 
			.append("                     invcl.invclasscode, ") 
			.append("                     inv.invcode, ") 
			.append("                     inv.invname,  ") 
			.append("                     cust.custcode,")
			.append("                     man.sealflag,                     ") 
			.append("                     SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num,   ") 
			.append("                     SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum   ") 
			.append("                from v_ic_onhandnum6 kp ") 
			.append("                left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc  ") 
			.append("                left join bd_invbasdoc inv on man.pk_invbasdoc = inv.pk_invbasdoc  ") 
			.append("                left join bd_invcl invcl  on inv.pk_invcl = invcl.pk_invcl ") 
			.append("                left join bd_cubasdoc cust on man.def17 = cust.pk_cubasdoc              ") 
			.append("                 where  ((0 = 0) and (0 = 0) and   ") 
			.append("                     (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and   ") 
			.append("                     (kp.pk_corp = '"+pk_corp+"'))   ") 
			.append("               group by kp.pk_corp,   ") 
			.append("                        kp.ccalbodyid,   ") 
			//.append("                        kp.cwarehouseid, ") 
			.append("                        inv.invcode, ") 
			.append("                        inv.invname, ") 
			.append("                        cust.custcode,")
			.append("                        man.sealflag, ") 
			.append("                        invcl.invclasscode,    ") 
			.append("                        kp.cinventoryid) a,   ") 
			.append("             (select kp.pk_corp,   ") 
			.append("                     kp.ccalbodyid,   ") 
			//.append("                     kp.cwarehouseid,   ") 
			.append("                     kp.cinventoryid, ") 
			.append("                     invcl.invclasscode, ") 
			.append("                     inv.invcode, ") 
			.append("                     inv.invname, ") 
			.append("                     cust.custcode,")
			.append("                     man.sealflag,                      ") 
			.append("                     sum(nvl(nfreezenum, 0)) freezenum,   ") 
			.append("                     sum(nvl(ngrossnum, 0)) ngrossnum   ") 
			.append("                from ic_freeze kp ") 
			.append("                left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc  ") 
			.append("                left join bd_invbasdoc inv on man.pk_invbasdoc = inv.pk_invbasdoc  ") 
			.append("                left join bd_invcl invcl  on inv.pk_invcl = invcl.pk_invcl ") 
			.append("                left join bd_cubasdoc cust on man.def17 = cust.pk_cubasdoc ") 
			.append("                 where  (cthawpersonid is null and (0 = 0) and   ") 
			.append("                     (0 = 0) and (0 = 0) and   ") 
			.append("                     (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and   ") 
			.append("                     (kp.pk_corp = '"+pk_corp+"'))   ") 
			.append("               group by kp.pk_corp,   ") 
			.append("                        kp.ccalbodyid,   ") 
			//.append("                        kp.cwarehouseid, ") 
			.append("                        invcl.invclasscode, ") 
			.append("                        inv.invcode, ") 
			.append("                        inv.invname,   ") 
			.append("                        cust.custcode,")
			.append("                        man.sealflag,  ") 
			.append("                        kp.cinventoryid) b   ") 
			.append("       where a.pk_corp = b.pk_corp(+)   ") 
			.append("         and a.ccalbodyid = b.ccalbodyid(+)   ") 
			//.append("         and a.cwarehouseid = b.cwarehouseid(+)   ") 
			.append("         and a.cinventoryid = b.cinventoryid(+)           ") 
			.append("         and (a.num = 0)  ") 
			.append("         and nvl(a.sealflag,'N')='N' ") ;*/
			
			if(constr.length()>0){
				sb.append(""+constr+"");
			}
			List res = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			if(res.size()>0){
				result1.add(res);
				System.out.println("result1的长度为："+res.size());
			}
			
		}
		
		/*StringBuffer sb1 = new StringBuffer();//21，22新建的物料全部返回
		sb1.append(" select a.invcode, a.invname, a.invclasscode ") 
		.append("   from (select invbasdoc.invcode, ") 
		.append("                invbasdoc.invname, ") 
		.append("                bdinvcl.invclasscode, ") 
		.append("                cust.custcode,")
		.append("                invmandoc.pk_corp ") 
		.append("           from bd_invbasdoc invbasdoc ") 
		.append("           left join bd_invcl bdinvcl ") 
		.append("             on invbasdoc.pk_invcl = bdinvcl.pk_invcl ") 
		.append("           left join bd_invmandoc invmandoc ") 
		.append("             on invbasdoc.pk_invbasdoc = invmandoc.pk_invbasdoc ") 
		.append("           left join bd_cubasdoc cust ") 
		.append("             on invmandoc.def17 = cust.pk_cubasdoc ") 
		.append("          where invbasdoc.invcode not in ") 
		.append("                ((select distinct inv.invcode ") 
		.append("                   from ic_onhandnum a ") 
		.append("                   left join bd_invbasdoc inv ") 
		.append("                     on a.cinvbasid = inv.pk_invbasdoc ") 
		.append("                   left join bd_invmandoc man ") 
		.append("                     on inv.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("                  where nvl(a.dr, 0) = 0 ") 
		.append("                    and nvl(inv.dr, 0) = 0 ") 
		.append("                    and nvl(man.dr, 0) = 0 ") 
		.append("                    and (substr(inv.invcode, 0, 2) = '21' or ") 
		.append("                         substr(inv.invcode, 0, 2) = '22') ") 
		.append("                    and a.pk_corp = '"+pk_corp+"' ") 
		.append("                    and man.pk_corp = '"+pk_corp+"' ") 
		.append("                    and nvl(man.sealflag, 'N') = 'N')) ") 
		.append("            and nvl(invbasdoc.dr, 0) = 0 ") 
		.append("            and nvl(bdinvcl.dr, 0) = 0 ") 
		.append("            and nvl(invmandoc.dr, 0) = 0 ") 
		.append("            and nvl(invmandoc.sealflag, 'N') = 'N' ") 
		.append("            and invmandoc.pk_corp = '"+pk_corp+"' ") 
		.append("            and (substr(invbasdoc.invcode, 0, 2) = '21' or ") 
		.append("                substr(invbasdoc.invcode, 0, 2) = '22')) a where a.pk_corp='"+pk_corp+"'");
		List result2 = new ArrayList();*/
		/*for(int i =0; i<connew.size();i++){
			StringBuffer constr = (StringBuffer)connew.get(i);
			StringBuffer sb2 = new StringBuffer();//23新建的物料需要根据查询条件来查询
			sb2.append(" select a.invcode, a.invname, a.invclasscode ") 
			.append("   from (select invbasdoc.invcode, ") 
			.append("                invbasdoc.invname, ") 
			.append("                bdinvcl.invclasscode, ") 
			.append("                cust.custcode,")
			.append("                invmandoc.pk_corp ") 
			.append("           from bd_invbasdoc invbasdoc ") 
			.append("           left join bd_invcl bdinvcl ") 
			.append("             on invbasdoc.pk_invcl = bdinvcl.pk_invcl ") 
			.append("           left join bd_invmandoc invmandoc ") 
			.append("             on invbasdoc.pk_invbasdoc = invmandoc.pk_invbasdoc ") 
			.append("           left join bd_cubasdoc cust ") 
			.append("             on invmandoc.def17 = cust.pk_cubasdoc ") 
			.append("          where invbasdoc.invcode not in ") 
			.append("                ((select distinct inv.invcode ") 
			.append("                   from ic_onhandnum a ") 
			.append("                   left join bd_invbasdoc inv ") 
			.append("                     on a.cinvbasid = inv.pk_invbasdoc ") 
			.append("                   left join bd_invmandoc man ") 
			.append("                     on inv.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("                  where nvl(a.dr, 0) = 0 ") 
			.append("                    and nvl(inv.dr, 0) = 0 ") 
			.append("                    and nvl(man.dr, 0) = 0 ") 
			.append("                    and (substr(inv.invcode, 0, 2) = '23') ") 
			.append("                    and a.pk_corp = '"+pk_corp+"' ") 
			.append("                    and man.pk_corp = '"+pk_corp+"' ") 
			.append("                    and nvl(man.sealflag, 'N') = 'N')) ") 
			.append("            and nvl(invbasdoc.dr, 0) = 0 ") 
			.append("            and nvl(bdinvcl.dr, 0) = 0 ") 
			.append("            and nvl(invmandoc.dr, 0) = 0 ") 
			.append("            and nvl(invmandoc.sealflag, 'N') = 'N' ") 
			.append("            and invmandoc.pk_corp = '"+pk_corp+"' ") 
			.append("            and (substr(invbasdoc.invcode, 0, 2) = '23')) a where a.pk_corp='"+pk_corp+"'"); 
			
			if(constr.length()>0){
				sb2.append(""+constr+"");
			}
			List res = (List) bs.executeQuery(sb2.toString(), new MapListProcessor());
			if(res.size()>0){
				result2.add(res);
			}
		}
		
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		
		List newInfo = null;//21,22新建物料未入库过
		try {
			newInfo = (List) bs.executeQuery(sb1.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"数据库操作异常"+e.getMessage()+"\"}";
		}*/
		
		JSONObject jsonH = new JSONObject();
		jsonH.put("status", "success");
		jsonH.put("message", "获取成功");
		StringBuffer str = new StringBuffer();
		str.append("[");
		if((result1.size() ==0)/*&&(result2.size() ==0)&&(newInfo == null || newInfo.size()==0)*/){
			return "{\"status\":\"error\",\"message\":\"未查询到无库存料号跟新建的物料，请检查查询条件\"}";
		}else{
			if(result1 != null && result1.size()>0){
				for(int i =0 ; i<result1.size() ;i++){
					List l = (List) result1.get(i);
					for(int j = 0 ; j<l.size() ;j++){
						Map wkcmap = (Map) l.get(j);
						JSONObject jsoninfo = new JSONObject();
						String invcode = wkcmap.get("invcode")==null?"":wkcmap.get("invcode").toString();
						String invname = wkcmap.get("invname")==null?"":wkcmap.get("invname").toString();
						String invclasscode = wkcmap.get("invclasscode")==null?"":wkcmap.get("invclasscode").toString();
						jsoninfo.put("invcode", invcode);
						jsoninfo.put("invname", invname);
						jsoninfo.put("invclasscode", invclasscode);
						str.append(jsoninfo+",");
					}
				}
			}
	/*		if(result2 != null && result2.size()>0){
				for(int i =0 ; i<result2.size() ;i++){
					List l = (List) result2.get(i);
					for(int j = 0 ; j<l.size() ;j++){
						Map newmap = (Map) l.get(j);
						JSONObject jsoninfo = new JSONObject();
						String invcode = newmap.get("invcode")==null?"":newmap.get("invcode").toString();
						String invname = newmap.get("invname")==null?"":newmap.get("invname").toString();
						String invclasscode = newmap.get("invclasscode")==null?"":newmap.get("invclasscode").toString();
						jsoninfo.put("invcode", invcode);
						jsoninfo.put("invname", invname);
						jsoninfo.put("invclasscode", invclasscode);
						str.append(jsoninfo+",");
					}
				}
			}
			if(newInfo != null && newInfo.size()>0){
				for(int i =0 ; i<newInfo.size();i++){
					//edit by mcw
					Map newInfoMap = (Map) newInfo.get(i);
					JSONObject jsoninfo = new JSONObject();
					String invcode = newInfoMap.get("invcode")==null?"":newInfoMap.get("invcode").toString();
					String invname = newInfoMap.get("invname")==null?"":newInfoMap.get("invname").toString();
					String invclasscode = newInfoMap.get("invclasscode")==null?"":newInfoMap.get("invclasscode").toString();
					jsoninfo.put("invcode", invcode);
					jsoninfo.put("invname", invname);
					jsoninfo.put("invclasscode", invclasscode);
					str.append(jsoninfo+",");
				}
			}*/
		}
		str = str.deleteCharAt(str.length()-1);
		str.append("]");
		jsonH.put("bodylist", str.toString());
	   /* Map map1=JSONObject.fromObject(jsonH);//jsonObject转map
	   JSONArray ja = (JSONArray) map1.get("bodylist");//得到bodylist对应的值
	   List<Map<String,Object>> mapListJson = (List)ja;*///jsonaray转list
		return jsonH.toString();
		} 
	public Map getCustInfo(String pk_corp,String invcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
//		sql.append("    select a.custcode,a.custname from bd_cubasdoc a left join bd_cumandoc b on a.pk_cubasdoc = b.pk_cubasdoc    ")
//		.append("   where  (nvl(a.dr,0)=0 and nvl(b.dr,0)=0)    ")
//		.append("   and b.pk_corp='"+pk_corp+"' and b.pk_cumandoc='"+pk_cumandoc+"'     ");
		sql.append(" select c.custcode,c.custname,c.pk_cubasdoc from bd_invbasdoc a ") 
		.append(" left join bd_invmandoc b ") 
		.append(" on a.pk_invbasdoc = b.pk_invbasdoc ") 
		.append(" left join bd_cubasdoc c ") 
		.append(" on b.def17 = c.pk_cubasdoc ") 
		.append(" where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0) ") 
		.append(" and b.pk_corp='"+pk_corp+"' and a.invcode='"+invcode+"' "); 
		List list = (List) bs.executeQuery(sql.toString(), new MapListProcessor());
		if(list.size()>0){
			Map map = (Map) list.get(0);
			return map;
		}
		return null;
	}
	
	private String getcorp(String unitcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 根据公司编码查询公司代码
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_corp from bd_corp where unitcode ='"+unitcode+"' and nvl(dr,0)=0");
		String pk_corp = (String) bs.executeQuery(sql.toString(), new ColumnProcessor());
		return pk_corp;
	}
	
	
	//add by zwx 2019-4-2 记录日志
	/**
	 * 记录日志
	 * @param vo
	 */
	/*public void recordLogs(String json,String startTime,String endTime){
			LogVO logVO = new LogVO();
			logVO.setPk_corp(corpLog);//单位编码
			logVO.setDr(0);
			logVO.setTs(new UFDateTime());
			logVO.setDjlx("获取无库存物料");//放单据类型
			logVO.setLContext(json.substring(0,199));
			logVO.setStart_time(new UFDateTime(startTime));
			logVO.setEnd_time(new UFDateTime(endTime));
			
			IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			//将LogVO插入到数据库
  			try {
  				ivo.insertVO(logVO);
			} catch (BusinessException e) {
				e.printStackTrace();
			}  
	}*/

	//end by zwx
}
