package nc.impl.beginning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
 * 成品期初数
 * @author mcw
 *
 */
public class StartingValueImpl implements IHttpServletAdaptor {

	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//ServletInputStream inputStream = request.getInputStream();
		InputStream is = request.getInputStream();//服务端读取客户端请求过来的数据，返回字节输入流
		BufferedReader br = new  BufferedReader(new InputStreamReader(is,"UTF-8"));//使用utf-8字符集来读取字节并解码成字符
		StringBuffer  sb=new StringBuffer();
		String str = null;
	    while((str = br.readLine()) != null){
	      sb.append(str);
	    }
	    String json=queryBeginningData(sb.toString());
	    
	}

	private String queryBeginningData(String query) {
		JSONObject json = null;
		JSONArray  injsonarr  = new JSONArray();
		try {
			json = new JSONObject().fromObject(query);//解析json
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误\"}";
		}
		
		Map mls=new HashMap();
		Iterator keys = json.keys();
		String key="";
		String value="";
	     while (keys.hasNext()) {
			key = (String) keys.next();
			value=json.getString(key);
			mls.put(key, value);
		}
	
	    //唯一标识
		String username = mls.get("username").toString();
		String password = mls.get("password").toString();
		if (null == username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		if (null == password || !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
	     String gcorp = mls.get("unitcode").toString();
	     if (null == gcorp || !gcorp.equals("10301")|| !gcorp.equals("10302")|| !gcorp.equals("10303")|| !gcorp.equals("10301")
	    		 || !gcorp.equals("10304")|| !gcorp.equals("10307")|| !gcorp.equals("10313")|| !gcorp.equals("10314")|| !gcorp.equals("10318")) {
				return "[{\"status\":\"error\",\"message\":\"公司编码不可为空或者公司编码错误，请检查公司编码\"}]";
			}
		 String pk_corp = getcorp(gcorp);//根据传入的公司编码获取公司主键
	     
		 String startdate = (String) mls.get("bdate");
		 IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 StringBuffer sb = new StringBuffer();
		 sb.append("  select cub.custcode, ")
		 .append("   invb.invcode, ")
		 .append("   sum(nvl(xcl.qcsl, 0)) qcnum ")
		 .append("   from (select cinventoryid, ")
		 .append("   sum(nvl(nonhandnum, 0)) qcsl ")
		 .append("    from ((select     hand.cinventoryid, ")
		 .append("    sum(nvl(nonhandnum, 0)) nonhandnum ")
		 .append("    from ic_month_hand hand, ")
		 .append("  bd_invmandoc  man, ")
		 .append("   bd_invbasdoc  invb ")
		 .append(" where hand.cinventoryid = man.pk_invmandoc ")
		 .append("  and man.pk_invbasdoc = invb.pk_invbasdoc ")
		 .append(" and substr(invb.invcode, 0, 2) in ('21', '22', '23') ")
		 .append(" and (hand.pk_corp = trim(leading ' ' from '1107') or ")
		 .append(" trim(leading ' ' from '"+pk_corp+"') is null) ")
		 .append("  and hand.dyearmonth = '2012-12' ")
		 .append("  group by hand.cinventoryid ")
		 .append(" ) union all ")
		 .append(" (select  ")
		 .append(" rec.cinventoryid, ")
		 .append("  sum(nvl(ninnum, 0) - nvl(noutnum, 0)) nonhandnum ")
		 .append("   from ic_month_record rec, ")
		 .append("  bd_invmandoc    man, ")
		 .append(" bd_invbasdoc    invb ")
		 .append(" where rec.cinventoryid = man.pk_invmandoc ")
		 .append("  and man.pk_invbasdoc = invb.pk_invbasdoc ")
		 .append(" and substr(invb.invcode, 0, 2) in ('21', '22', '23') ")
		 .append(" and (rec.pk_corp = trim(leading ' ' from '"+pk_corp+"') or ")
		 .append("   trim(leading ' ' from '"+pk_corp+"') is null) ")
		 .append("and rec.dyearmonth >= '2013-01' ")
		 .append("  and ((rec.dyearmonth <= '2015-05') or ")
		 .append(" trim(leading ' ' from '"+startdate+"') is null) ")
		 .append("group by rec.cinventoryid ")
		 .append(") union  all ")
		 .append(" (select  ")
		 .append(" b.cinventoryid, ")
		 .append(" sum(nvl(b.ninnum, 0) - nvl(b.noutnum, 0)) nonhandnum ")
		 .append("from ic_general_h h ")
		 .append("inner join ic_general_b b ")
		 .append(" on h.cgeneralhid = b.cgeneralhid ")
		 .append(" left join ic_general_bb1 bb1 ")
		 .append(" on bb1.cgeneralbid = b.cgeneralbid ")
		 .append(" and nvl(bb1.dr, 0) = 0 ")
		 .append(" inner join bd_invmandoc invm ")
		 .append("  on invm.pk_invmandoc = b.cinventoryid ")
		 .append("  inner join bd_invbasdoc invb ")
		 .append(" on invb.pk_invbasdoc = invm.pk_invbasdoc ")
		 .append(" where nvl(h.dr, 0) = 0 ")
		 .append("  and nvl(b.dr, 0) = 0 ")
		 .append(" and h.cbilltypecode not in ")
		 .append(" ('40', '4Q', '4K', '4N') ")
		 .append(" and (h.pk_corp = trim(leading ' ' from '"+pk_corp+"') or ")
		 .append("  trim(leading ' ' from '"+pk_corp+"') is null) ")
		 .append("  and substr(invb.invcode, 0, 2) in ('21', '22', '23') ")
		 .append("  and (dbizdate > '2015-05-31') ")
		 .append("  and (dbizdate < trim(leading ' ' from '"+startdate+"') or ")
		 .append(" trim(leading ' ' from '"+startdate+"') is null) ")
		 .append(" group by  b.cinventoryid ")
		 .append(" ) ) ")
		 .append("  group by cinventoryid) xcl ")
		 .append("     inner join bd_invmandoc invm ")
		 .append("     on invm.pk_invmandoc = xcl.cinventoryid ")
		 .append("  inner join bd_invbasdoc invb ")
		 .append("     on invb.pk_invbasdoc = invm.pk_invbasdoc ")
		 .append("   left join bd_cubasdoc cub ")
		 .append("     on invm.def17 = cub.pk_cubasdoc ")
		 .append("   left join bd_invcl invcl ")
		 .append("     on invb.pk_invcl = invcl.pk_invcl ")
		 .append("  where invm.sealflag = 'N' ")
		 .append("  group by cub.custcode, ")
		 .append(" invb.invcode ");
		
		 List inlist = null;
			try {
				inlist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库异常\""+e.getMessage()+"}";
			}
			for (int i = 0; i < inlist.size(); i++) {
				Map inMap = new HashMap();
				inMap = (Map) inlist.get(i);
				// 客商编码
				String custcode = inMap.get("custcode") == null ? "" : inMap.get(
						"custcode").toString();
				// 物料编码
				String invcode = inMap.get("invcode") == null ? "" : inMap.get(
						"invcode").toString();
				// 期初库存数量
				String qcnumber = inMap.get("qcnum") == null ? "0" : inMap
						.get("qcnum").toString();
				json.put("custcode", custcode);
				json.put("invcode", invcode);
				json.put("qcnumber", qcnumber);
				injsonarr.add(json);
		}
	     		 return "{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":"+injsonarr.toString()+"}";
	}

	
	/**
	 * 查询公司代码
	 * @param unitcode
	 * @return
	 */
	private String getcorp(String unitcode) {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_corp from bd_corp where unitcode ='"+unitcode+"' and dr=0");
		List list = null;
		try {
			list = (List) bs.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		String corp = null;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 corp= map.get("pk_corp")==null?"":map.get("pk_corp").toString();
			}
			return corp;
		}
		return null;
	}

}
