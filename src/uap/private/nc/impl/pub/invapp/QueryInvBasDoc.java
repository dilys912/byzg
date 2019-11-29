package nc.impl.pub.invapp;

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
 * NC老物料传输CVM
 * 2018年12月21日
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class QueryInvBasDoc implements IHttpServletAdaptor{

	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String result = null;
		InputStream inputStream = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		String jsonStr = buffer.toString();
		result = this.QueryInv(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
		
	}
	public String QueryInv(String jsonStr){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
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
		String invcode = mls.get("invcode").toString();
		String corp = mls.get("corp").toString();
		StringBuffer sb = new StringBuffer();
		sb.append(" select b.invcode    as wlbm, ") 
		.append("        b.invname      as wlmc, ") 
		.append("        a.invclasscode as wlflbm, ") 
		.append("        b.invspec      as gg, ") 
		.append("        b.invtype      as xh, ") 
		.append("        b.invpinpai    as pp, ") 
		.append("        m.docname      as cpdlmc, ") 
		.append("        m.doccode      as cpdlbm, ") 
		.append("        l.docname      as cpxtmc, ") 
		.append("        l.doccode      as cpxtbm, ") 
		.append("        f.docname      as cpxlmc, ") 
		.append("        f.doccode      as cpxlbm, ") 
		.append("        i.docname      as cpgymc, ") 
		.append("        i.doccode      as cpgybm, ") 
		.append("        j.docname      as gwzzmc, ") 
		.append("        j.doccode      as gwzzbm, ") 
		.append("        k.docname      as czmc, ") 
		.append("        k.doccode      as czbm, ") 
		.append("        g.custname     as custname, ") 
		.append("        g.custcode     as custcode, ") 
		.append("        h.unitname     as unitcode, ") 
		.append("        h.unitcode     as pk_corp ") 
		.append("   from bd_invcl a ") 
		.append("   left join bd_invbasdoc b ") 
		.append("     on a.pk_invcl = b.pk_invcl ") 
		.append("   left join bd_invmandoc c ") 
		.append("     on b.pk_invbasdoc = c.pk_invbasdoc ") 
		.append("   left join bd_defdoc m ") 
		.append("     on b.def1 = m.pk_defdoc ") 
		.append("   left join bd_defdoc l ") 
		.append("     on b.def13 = l.pk_defdoc ") 
		.append("   left join bd_defdoc f ") 
		.append("     on b.def14 = f.pk_defdoc ") 
		.append("   left join bd_defdoc i ") 
		.append("     on b.def15 = i.pk_defdoc ") 
		.append("   left join bd_defdoc j ") 
		.append("     on b.def16 = j.pk_defdoc ") 
		.append("   left join bd_defdoc k ") 
		.append("     on b.def18 = k.pk_defdoc ") 
		.append("   left join bd_cubasdoc g ") 
		.append("     on c.def17 = g.pk_cubasdoc ") 
		.append("   left join bd_corp h ") 
		.append("     on c.pk_corp = h.pk_corp ") 
		.append("  where nvl(a.dr, 0) = 0 ") 
		.append("    and nvl(b.dr, 0) = 0 ") 
		.append("    and nvl(c.dr, 0) = 0 ") 
		.append("    and (a.invclasscode like '21%' or a.invclasscode like '22%' or ") 
		.append("         a.invclasscode like '23%') ") 
		.append("    and c.pk_corp = '"+corp+"' ") 
		.append("    and nvl(c.sealflag, 'N') = 'N' ") 
		.append("    and b.invcode ='"+invcode+"' ");
		
		List list = new ArrayList();
		try {
			list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		JSONArray jsonarr = new JSONArray();
		JSONObject jsonobj = new JSONObject();
		for(int i =0;i<list.size();i++){
			Map map = new HashMap();
			map = (Map) list.get(i);
			String  wlbm = map.get("wlbm").toString();
			String  wlmc = map.get("wlmc").toString();
			String  wlflbm = map.get("wlflbm").toString();
			String  gg = map.get("gg").toString();
			String  xh = map.get("xh").toString();
			String  pp = map.get("pp").toString();
			String  cpdlmc = map.get("cpdlmc").toString();
			String  cpdlbm = map.get("cpdlbm").toString();
			String  cpxtmc = map.get("cpxtmc").toString();
			String  cpxtbm = map.get("cpxtbm").toString();
			String  cpxlmc = map.get("cpxlmc").toString();
			String  cpxlbm = map.get("cpxlbm").toString();
			String  cpgymc = map.get("cpgymc").toString();
			String  cpgybm = map.get("cpgybm").toString();
			String  gwzzmc = map.get("gwzzmc").toString();
			String  gwzzbm = map.get("gwzzbm").toString();
			String  czmc = map.get("czmc").toString();
			String  czbm = map.get("czbm").toString();
			String  custname = map.get("custname").toString();
			String  custcode = map.get("custcode").toString();
			String  unitcode = map.get("unitcode").toString();
			String  pk_corp = map.get("pk_corp").toString();
		}
		return jsonStr;
		
	}

}
