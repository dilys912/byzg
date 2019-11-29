package nc.impl.packing;

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
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

/**
 * 功能：根据条件返回cvm包装物接口数据 包装物接口实现类
 * 
 * @author pengjiajia 2018年4月24日16:16:16
 */
@SuppressWarnings("all")
public class PackingImpl implements IHttpServletAdaptor {

	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		StringBuffer sb = null;
		String s = "";
		InputStream in = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in,
				"utf-8"));
		sb = new StringBuffer();
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		String json = checkPacking(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}

	public String checkPacking(String strJson) {
		// 解析json
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject().fromObject(strJson);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误\"}";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.getString(key);
			mls.put(key, value);
		}
		// 唯一标识
		String username = (String) mls.get("username");
		String password = (String) mls.get("password");
		if (!"baosteel".equals(username) && !"123456".equals(password)) {
			return "{\"status\":\"error\",\"message\":\"账号错误或者账号为空\"}";
		}
		// 开始日期
		String startdate = (String) mls.get("startdate");
		// 结束日期
		String enddate = (String) mls.get("enddate");
		// 公司主键
		String unitcode = (String) mls.get("corp");
		String cbiztype = null;
		// 销售订单业务类型(成品销售)
		if (unitcode.equals("10301")) {
			//上海
			cbiztype = "1016A21000000000WOPI";
		} else if (unitcode.equals("10307")) {
			//武汉
			cbiztype = "1071A21000000000OTC4";
		} else if (unitcode.equals("10314")){
			//哈尔滨
			cbiztype = "1103A2100000000016TZ";
		} else if (unitcode.equals("10313")){
			//河南
			cbiztype = "1097A2100000000018J9";
		} else if (unitcode.equals("10302")){
			//河北
			cbiztype = "1017A210000000012Z99";
		} else if(unitcode.equals("10303")){
			//成都
			cbiztype = "1018A21000000001HZS9";
		} else if(unitcode.equals("10304")){
			//佛山
			cbiztype = "1019A210000000013ZXI";
		}else if(unitcode.equals("10318")){
			//宝翼2
			cbiztype ="110711100000000000SI";
		}


		// 包装物业务类型主键
		String BZcbiztype = null;
		if (unitcode.equals("10301")) {
			//上海
			BZcbiztype = "1016A21000000000WOPC";
		} else if (unitcode.equals("10307")) {
			//武汉
			BZcbiztype = "1071A21000000000OTBZ";
		} else if (unitcode.equals("10314")){
			//哈尔滨
			BZcbiztype = "1103A2100000000016TR";
		} else if(unitcode.equals("10313")){
			//河南
			BZcbiztype = "1097A2100000000018HM";
		} else if(unitcode.equals("10302")){
			//河北
			BZcbiztype = "1017A210000000012Z94";
		} else if(unitcode.equals("10303")){
			//成都
			BZcbiztype = "1018A21000000001HW9T";
		}else if(unitcode.equals("10304")){
			//佛山
			BZcbiztype = "1019A210000000013ZXA";
		}else if(unitcode.equals("10318")){
			//宝翼2
			BZcbiztype = "110711100000000000SC";
		}
		/**
		 * 111000012    上海宝翼制罐有限公司
		 * 111000002	成都宝钢制罐有限公司
		 * 111000003	佛山宝钢制罐有限公司
		 * 111000004	河北宝钢制罐北方有限公司
		 * 111000100	武汉宝钢包装有限公司沌口制罐分公司
		 * 211200127	河南宝钢制罐有限公司
		 * 111000156	哈尔滨宝钢制罐有限公司
		 * 210211839    上海宝翼制罐有限公司2
		 */
		//客商编码
		StringBuffer custcode = new StringBuffer();
		if (unitcode.equals("10301")) {
			//上海
			custcode.append("('111000100','111000156','211200127','111000004','111000002','111000003','210211839')");
		} else if (unitcode.equals("10307")) {
			//武汉
			custcode.append("('111000012','111000156','211200127','111000004','111000002','111000003','210211839')");
		} else if (unitcode.equals("10314")){
			//哈尔滨
			custcode.append("('111000012','111000100','211200127','111000004','111000002','111000003','210211839')");
		} else if(unitcode.equals("10313")){
			//河南
			custcode.append("('111000012','111000100','111000156','111000004','111000002','111000003','210211839')");
		}else if(unitcode.equals("10302")){
			//河北
			custcode.append("('111000012','111000100','111000156','211200127','111000002','111000003','210211839')");
		}else if(unitcode.equals("10303")){
			//成都
			custcode.append("('111000012','111000100','111000156','211200127','111000004','111000003','210211839')");
		}else if(unitcode.equals("10304")){
			//佛山
			custcode.append("('111000012','111000100','111000156','211200127','111000004','111000002','210211839')");
		}else if(unitcode.equals("10318")){
			//日新
			custcode.append("('111000012','111000100','111000156','211200127','111000004','111000002','111000003')");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.unitcode, ")
		.append("        a.custcode, ")
		.append("        a.pk_address, ")
		.append("        a.dbate, ")
		.append("        sum(a.bankwoodtuopan) as bankwoodtuopan, ")
		.append("        sum(a.bankwooddinggai) as bankwooddinggai, ")
		.append("        sum(a.bankwooddianguanzi) as bankwooddianguanzi, ")
		.append("        sum(a.bankplastictuopan) as bankplastictuopan, ")
		.append("        sum(a.bankplasticdinggai) as bankplasticdinggai, ")
		.append("        sum(a.bankplasticdianguanzi) as bankplasticdianguanzi, ")
		.append("        sum(a.tuopan) as tuopan, ")
		.append("        sum(a.wooddinggai) as wooddinggai, ")
		.append("        sum(a.plasticdinggai) as plasticdinggai, ")
		.append("        sum(a.woodtuopan) as woodtuopan, ")
		.append("        sum(a.plastictuopan) as plastictuopan, ")
		.append("        sum(a.wooddianguanzi) as wooddianguanzi, ")
		.append("        sum(a.plasticdianguanzi) as plasticdianguanzi ")
		.append("   from ((select a.unitcode as unitcode, ")
		.append("                 a.custcode as custcode, ")
		.append("                 a.pk_address as pk_address, ")
		.append("                 a.dbilldate as dbate, ")
		.append("                 sum(0) bankwoodtuopan, ")
		.append("                 sum(0) bankwooddinggai, ")
		.append("                 sum(0) bankwooddianguanzi, ")
		.append("                 sum(0) bankplastictuopan, ")
		.append("                 sum(0) bankplasticdinggai, ")
		.append("                 sum(0) bankplasticdianguanzi, ")
		.append("                 sum(a.tuopan) tuopan, ")
		.append("                 sum(a.wooddinggai) wooddinggai, ")
		.append("                 sum(a.plasticdinggai) plasticdinggai, ")
		.append("                 sum(a.woodtuopan) woodtuopan, ")
		.append("                 sum(a.plastictuopan) plastictuopan, ")
		.append("                 sum(a.wooddianguanzi) wooddianguanzi, ")
		.append("                 sum(a.plasticdianguanzi) plasticdianguanzi ")
		.append("            from ((select corp.unitcode, ")
		.append("                          cub.custcode, ")
		.append("                          address.pk_address, ")
		.append("                          substr(h.dbilldate, 0, 7) dbilldate, ")
		.append("                          sum(0) bankwoodtuopan, ")
		.append("                          sum(0) bankwooddinggai, ")
		.append("                          sum(0) bankwooddianguanzi, ")
		.append("                          sum(0) bankplastictuopan, ")
		.append("                          sum(0) bankplasticdinggai, ")
		.append("                          sum(0) bankplasticdianguanzi, ")
		.append("                          sum(0) tuopan, ")
		.append("                          sum(decode(mm_glzb_b.dinggai, '木顶盖', 1, 0)) wooddinggai, ")
		.append("                          sum(decode(mm_glzb_b.dinggai, '塑料顶盖', 1, 0)) plasticdinggai, ")
		.append("                          sum(decode(mm_glzb_b.tuopan, '木托盘', 1, 0)) woodtuopan, ")
		.append("                          sum(decode(mm_glzb_b.tuopan, '塑料托盘', 1, 0)) plastictuopan, ")
		.append("                          sum(decode(mm_glzb_b.dianguanzhi, '普通垫罐纸', ceil(noutnum / 389 + 1), 0)) wooddianguanzi, ")
		.append("                          sum(decode(mm_glzb_b.dianguanzhi, '塑料垫罐纸', ceil(noutnum / 389 + 1), 0)) plasticdianguanzi ")
		.append("                     from ic_general_h h ")
		.append("                     left join ic_general_b b ")
		.append("                       on h.cgeneralhid = b.cgeneralhid ")
		.append("                     left join bd_address address ")
		.append("                       on h.vdiliveraddress = address.addrname ")
		.append("                     left join bd_invbasdoc inv ")
		.append("                       on b.cinvbasid = inv.pk_invbasdoc ")
		.append("                     left join bd_cumandoc cum ")
		.append("                       on h.ccustomerid = cum.pk_cumandoc ")
		.append("                     left join bd_cubasdoc cub ")
		.append("                       on cub.pk_cubasdoc = cum.pk_cubasdoc ")
		.append("                     left join bd_invcl invcl ")
		.append("                       on invcl.pk_invcl = inv.pk_invcl ")
		.append("                     left join bd_rdcl rd ")
		.append("                       on h.cdispatcherid = rd.pk_rdcl ")
		.append("                     left join mm_glzb_b mm_glzb_b ")
		.append("                       on b.vfree1 = mm_glzb_b.dh ")
		.append("                     left join bd_corp corp ")
		.append("                       on corp.pk_corp = h.pk_corp ")
		.append("                    where nvl(h.dr, 0) = 0 ")
		.append("                      and nvl(b.dr, 0) = 0 ")
		.append("                      and h.fbillflag ='3' ")
		.append("                      and h.dbilldate >= '"+startdate+"' ")
		.append("                      and h.dbilldate <= '"+enddate+"' ")
		.append("                      and corp.unitcode = '"+unitcode+"' ")
		.append("                      and rd.rdcode = '0601' ")
		.append("                      and h.cbiztype = '"+cbiztype+"' ")
		.append("                      and substr(invcl.invclasscode, 0, 2) = '23' ")
		.append("                      and cub.custcode not in "+custcode+"")
		.append("                    group by corp.unitcode, ")
		.append("                             cub.custcode, ")
		.append("                             address.pk_address, ")
		.append("                             substr(h.dbilldate, 0, 7)) union ")
		.append("                  (select corp.unitcode, ")
		.append("                          cub.custcode, ")
		.append("                          address.pk_address, ")
		.append("                          substr(h.dbilldate, 0, 7) as bdate, ")
		.append("                          sum(0) bankwoodtuopan, ")
		.append("                          sum(0) bankwooddinggai, ")
		.append("                          sum(0) bankwooddianguanzi, ")
		.append("                          sum(0) bankplastictuopan, ")
		.append("                          sum(0) bankplasticdinggai, ")
		.append("                          sum(0) bankplasticdianguanzi, ")
		.append("                          count(b.vfree1) tuopan, ")
		.append("                          sum(0) wooddinggai, ")
		.append("                          sum(0) plasticdinggai, ")
		.append("                          sum(0) woodtuopan, ")
		.append("                          sum(0) plastictuopan, ")
		.append("                          sum(0) wooddianguanzi, ")
		.append("                          sum(0) plasticdianguanzi ")
		.append("                     from ic_general_h h ")
		.append("                     left join ic_general_b b ")
		.append("                       on h.cgeneralhid = b.cgeneralhid ")
		.append("                     left join bd_address address ")
		.append("                       on h.vdiliveraddress = address.addrname ")
		.append("                     left join bd_invbasdoc inv ")
		.append("                       on b.cinvbasid = inv.pk_invbasdoc ")
		.append("                     left join bd_cumandoc cum ")
		.append("                       on h.ccustomerid = cum.pk_cumandoc ")
		.append("                     left join bd_cubasdoc cub ")
		.append("                       on cub.pk_cubasdoc = cum.pk_cubasdoc ")
		.append("                     left join bd_invcl invcl ")
		.append("                       on invcl.pk_invcl = inv.pk_invcl ")
		.append("                     left join bd_rdcl rd ")
		.append("                       on h.cdispatcherid = rd.pk_rdcl ")
		.append("                     left join bd_corp corp ")
		.append("                       on corp.pk_corp = h.pk_corp ")
		.append("                     left join mm_glzb_b mm_glzb_b ")
		.append("                       on inv.invcode = mm_glzb_b.lh ")
		.append("                    where nvl(h.dr, 0) = 0 ")
		.append("                      and nvl(b.dr, 0) = 0 ")
		.append("                      and h.fbillflag ='3' ")
		.append("                      and h.dbilldate >= '"+startdate+"' ")
		.append("                      and h.dbilldate <= '"+enddate+"' ")
		.append("                      and corp.unitcode = '"+unitcode+"' ")
		.append("                      and (substr(invcl.invclasscode, 0, 2) = '21' ")
		.append("                      or substr(invcl.invclasscode, 0, 2) = '22') ")
		.append("                      and rd.rdcode = '0601' ")
		.append("                      and h.cbiztype = '"+cbiztype+"' ")
		.append("                    group by corp.unitcode, ")
		.append("                             cub.custcode, ")
		.append("                             address.pk_address, ")
		.append("                             substr(h.dbilldate, 0, 7))) a ")
		.append("           group by a.unitcode, a.custcode, a.pk_address, a.dbilldate) union all ")
		.append("         (select corp.unitcode as unitcode, ")
		.append("                 cub.custcode as custcode, ")
		.append("                 address.pk_address as pk_address, ")
		.append("                 substr(h.dreceivedate, 0, 7) as bdate, ")
		.append("                 sum(decode(invcl.invclasscode, '25020101', b.narrvnum, 0)) bankwoodtuopan, ")
		.append("                 sum(decode(invcl.invclasscode, '25020102', b.narrvnum, 0)) bankwooddinggai, ")
		.append("                 sum(decode(invcl.invclasscode, '25020103', nvl(b.narrvnum, 0) * nvl(invcl.averagecost, 0), 0)) bankwooddianguanzi, ")
		.append("                 sum(decode(invcl.invclasscode, '25020115', b.narrvnum, 0)) bankplastictuopan, ")
		.append("                 sum(decode(invcl.invclasscode, '25020116', b.narrvnum, 0)) bankplasticdinggai, ")
		.append("                 sum(decode(invcl.invclasscode, '25020117', b.narrvnum, 0)) bankplasticdianguanzi, ")
		.append("                 sum(0) tuopan, ")
		.append("                 sum(0) wooddinggai, ")
		.append("                 sum(0) plasticdinggai, ")
		.append("                 sum(0) woodtuopan, ")
		.append("                 sum(0) plastictuopan, ")
		.append("                 sum(0) wooddianguanzi, ")
		.append("                 sum(0) plasticdianguanzi ")
		.append("            from po_arriveorder h ")
		.append("            left join po_arriveorder_b b ")
		.append("              on h.carriveorderid = b.carriveorderid ")
		.append("            left join po_order_b pob ")
		.append("              on pob.corder_bid = b.corder_bid ")
		.append("            left join po_order poh ")
		.append("              on poh.corderid = pob.corderid ")
		.append("            left join bd_address address ")
		.append("              on poh.cdeliveraddress = address.addrname ")
		.append("            left join bd_invbasdoc inv ")
		.append("              on b.cbaseid = inv.pk_invbasdoc ")
		.append("            left join bd_cubasdoc cub ")
		.append("              on cub.pk_cubasdoc = h.cvendorbaseid ")
		.append("            left join bd_invcl invcl ")
		.append("              on invcl.pk_invcl = inv.pk_invcl ")
		.append("            left join bd_corp corp ")
		.append("              on corp.pk_corp = h.pk_corp ")
		.append("           where nvl(h.dr, 0) = 0 ")
		.append("             and nvl(b.dr, 0) = 0 ")
		.append("             and h.ibillstatus = '3' ")
		.append("             and corp.unitcode = '"+unitcode+"' ")
		.append("             and h.cbiztype = '"+BZcbiztype+"' ")
		.append("             and h.dreceivedate >= '"+startdate+"' ")
		.append("             and h.dreceivedate <= '"+enddate+"' ")
		.append("           group by cub.custcode, ")
		.append("                    substr(h.dreceivedate, 0, 7), ")
		.append("                    address.pk_address, ")
		.append("                    corp.unitcode)) a ")
		.append("  group by a.unitcode, a.custcode, a.pk_address, a.dbate ");
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup( IUAPQueryBS.class.getName());
		List SendList = new ArrayList();
		try {
			SendList = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"数据库查询错误\"" + e.getMessage() + "}";
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < SendList.size(); i++) {
			Map map = (Map) SendList.get(i);
			// 公司
			String BZunitcode = map.get("unitcode") == null ? "" : map.get( "unitcode").toString();
			// 客商编码
			String BZcustcode = map.get("custcode") == null ? "" : map.get( "custcode").toString();
			// 客商地址主键
			String BZpk_addr = map.get("pk_address") == null ? "" : map .get("pk_address").toString();
			// 日期
			String BZdbilldate = map.get("dbate") == null ? "" : map.get( "dbate").toString();
			// 托盘
			String BZtuopan = map.get("tuopan") == null ? "0" : map .get("tuopan").toString();
			// 木顶盖
			String BZwooddinggai = map.get("wooddinggai") == null ? "0" : map .get("wooddinggai").toString();
			// 塑料顶盖
			String BZplasticdinggai = map.get("plasticdinggai") == null ? "0" : map.get("plasticdinggai").toString();
			// 木托盘
			String BZwoodtuopan = map.get("woodtuopan") == null ? "0" : map.get("woodtuopan").toString();
			// 塑料托盘
			String BZplastictuopan = map.get("plastictuopan") == null ? "0" : map.get("plastictuopan").toString();
			// 垫罐纸
			String BZwooddianguanzi = map.get("wooddianguanzi") == null ? "0" : map.get("wooddianguanzi").toString();
			// 塑料垫罐纸
			String BZplasticdianguanzi = map.get("plasticdianguanzi") == null ? "0" : map.get("plasticdianguanzi").toString();
			// 木托盘
			String bankwoodtuopan = map.get("bankwoodtuopan") == null ? "0" : map.get("bankwoodtuopan").toString();
			// 木顶盖
			String bankwooddinggai = map.get("bankwooddinggai") == null ? "0" : map.get("bankwooddinggai").toString();
			// 垫罐纸
			String bankwooddianguanzi= map.get("bankwooddianguanzi") == null ? "0" : map.get("bankwooddianguanzi").toString();
			// 塑料底盘
			String bankplastictuopan= map.get("bankplastictuopan") == null ? "0" : map.get("bankplastictuopan").toString();
			// 塑料顶盖
			String bankplasticdinggai= map.get("bankplasticdinggai") == null ? "0" : map.get("bankplasticdinggai").toString();
			// 塑料垫罐纸
			String bankplasticdianguanzi= map.get("bankplasticdianguanzi") == null ? "0" : map.get("bankplasticdianguanzi").toString();
			jsonObj.put("corp", BZunitcode);// 公司
			jsonObj.put("custcode", BZcustcode);// 客商编码
			jsonObj.put("address", BZpk_addr);// 地址主键
			jsonObj.put("date", BZdbilldate);// 日期
			jsonObj.put("gaizi", BZtuopan);// 托盘
			jsonObj.put("wooddianguanzi", BZwooddianguanzi);// 垫罐纸
			jsonObj.put("woodtuopan", BZwoodtuopan);// 木托盘
			jsonObj.put("wooddinggai", BZwooddinggai);// 木顶盖
			jsonObj.put("plasticdianguanzi", BZplasticdianguanzi);// 塑料垫罐纸
			jsonObj.put("plasticdinggai", BZplasticdinggai);// 塑料顶盖
			jsonObj.put("plastictuopan", BZplastictuopan);// 塑料托盘
			jsonObj.put("bankwoodtuopan", bankwoodtuopan); // 木托盘
			jsonObj.put("bankwooddinggai", bankwooddinggai); // 木顶盖
			jsonObj.put("bankwooddianguanzi", bankwooddianguanzi); // 垫罐纸
			jsonObj.put("bankplastictuopan", bankplastictuopan);// 塑料底盘
			jsonObj.put("bankplasticdinggai", bankplasticdinggai);// 塑料顶盖
			jsonObj.put("bankplasticdianguanzi", bankplasticdianguanzi);// 塑料垫罐纸
			jsonObj.put("bankgaizi", bankplasticdianguanzi);//盖子
			jsonArray.add(jsonObj);
		}
		return "{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + jsonArray.toString() + "}";
	}
}
