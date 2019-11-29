package nc.impl.qereport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import nc.vo.pub.BusinessException;
/**
 * 成品出入库动态数据
 * 2018年9月11日
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class QEReportBaseImpl implements IHttpServletAdaptor{

	public void doAction(HttpServletRequest request, HttpServletResponse respone)
			throws ServletException, IOException {
					String result = null;
		InputStream inputstream = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line =null;
		while((line = br.readLine())!=null){
			buffer.append(line);
		}
		result = QEReportBase(buffer.toString());
		respone.getOutputStream().write(result.toString().getBytes("UTF-8"));
		br.close();
	}

	private String QEReportBase(String jsonStr){
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
		IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//公司编码
		String unitcode = mls.get("unitcode")==null?"":mls.get("unitcode").toString();
		String pk_corp = this.getpk_corp(unitcode);
		//业务类型
		String cdispatcherid = null;
		if(pk_corp.equals("1016")){
			cdispatcherid ="1016A21000000000WO08";
		}else if(pk_corp.equals("1071")){
			cdispatcherid ="1071A21000000000UWYS";
		}else if(pk_corp.equals("1103")){
			cdispatcherid ="1103A2100000000016SC";
		}else if(pk_corp.equals("1097")){
			cdispatcherid ="1097A2100000000018OW";
		}else if(pk_corp.equals("1017")){
			cdispatcherid ="1017A210000000014P1W";
		}else if(pk_corp.equals("1018")){
			cdispatcherid ="1018A210000000051S9R";
		}else if(pk_corp.equals("1019")){
			cdispatcherid ="1019A210000000013ZQ7";
		}else if(pk_corp.equals("1107")){
			cdispatcherid ="110711100000000003GD";
		}
		//开始时间
		String sdate = mls.get("startdate")==null?"":mls.get("startdate").toString();
		//结束时间
		String edate = mls.get("enddate")==null?"":mls.get("enddate").toString();
		//edit by mcw
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		 
			Date bdate;
			try {
				bdate = sdf.parse(edate);//要实现日期+1 需要String转成Date类型
				Calendar c = Calendar.getInstance();  
	            c.setTime(bdate);  
	            c.add(Calendar.DAY_OF_MONTH, 1); //利用Calendar 实现 Date日期+1天
	            bdate = c.getTime();
	            System.out.println("Date结束日期+1 " +sdf.format(bdate));//打印Date日期,显示成功+1天
	            
	            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	            String e1Date = sdf1.format(bdate);
	            System.out.println("Date类型转String类型  "+e1Date);//将日期转成String类型 方便进入数据库比较
	          //end by mcw
	    		StringBuffer buffer = new StringBuffer();
	    		//Edit by mcw
	    		buffer.append(" select sum(nvl(a.ninnum, 0)) ninnum, ")
	    		.append("        sum(nvl(a.noutnum, 0)) noutnum, ")
	    		.append("        a.custcode, ")
	    		.append("        a.invcode, ")
	    		.append("        a.dbizdate ")
	    		.append("   from ((select sum(nvl(s.ninnum, 0)) ninnum, ")
	    		.append("        sum(0) noutnum, ")
	    		.append("         s.dbizdate, ")
	    		.append("        s.invcode, ")
	    		.append("        s.custcode ")
	    		.append("   from ((select  ")
	    		.append("                sum(nvl(a.ninspacenum, 0)) as ninnum, ")
	    		.append("                sum(0) noutnum, ")
	    		.append("                b.dbizdate as dbizdate, ")
	    		.append("                d.invcode, ")
	    		.append("                 cubas.custcode       ")
	    		.append("           from ic_general_bb1 a ")
	    		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ")
	    		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ")
	    		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ")
	    		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ")
	    		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ")
	    		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ")
	    		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ")
	    		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ")
	    		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ")
	    		.append("          left  join bd_invmandoc invm on invm.pk_invmandoc = b.cinventoryid ")
	    		.append("           left join bd_cumandoc cum on cum.pk_cumandoc = c.ccustomerid    ")
	    		.append("           left join bd_cubasdoc cubas on cubas.pk_cubasdoc = invm.def17         ")
	    		.append("          where (a.pk_corp = '"+pk_corp+"' or '"+pk_corp+"' is null) ")
	    		.append("            and a.dr = '0' ")
	    		.append("            and (i.xxrq || i.xxsj >= '"+sdate+"' || '00:00:00' or '"+sdate+"' is null) ")
	    		.append("            and (i.xxrq || i.xxsj <= '"+e1Date+"' || '00:00:00' or  ")
	    		.append(" '"+e1Date+"' is null) ")
	    		.append("             group by d.invcode, cubas.custcode,b.dbizdate ")
	    		.append("          ) ")
	    		.append(" union all ")
	    		.append("  (select sum(nvl(icb.ninnum, 0)) as ninnum, ")
	    		.append("                sum(0) noutnum, ")
	    		.append("                 icb.dbizdate as dbizdate, ")
	    		.append("                bas.invcode, ")
	    		.append("              cubas.custcode ")
	    		.append("             from mm_glcl_b glclb ")
	    		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ")
	    		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ")
	    		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ")
	    		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ")
	    		.append("           left join ic_general_b icb on glclb.pk_glcl_b =icb.vsourcebillcode ")
	    		.append("            left join ic_general_h h on h.cgeneralhid=icb.cgeneralhid ")
	    		.append("           left join bd_cumandoc cum on cum.pk_cumandoc = h.ccustomerid    ")
	    		.append("           left join bd_cubasdoc cubas on cubas.pk_cubasdoc = man.def17  ")
	    		.append("          where (glcl.pk_corp = '"+pk_corp+"' or '"+pk_corp+"' is null) ")
	    		.append("            and nvl(icb.dr, 0) = 0 ")
	    		.append("            and lydjlx = 'isolation' ")
	    		.append("            and (glcl.clrq || glcl.xxsj >='"+sdate+"' || '00:00:00' or ")
	    		.append("                '"+sdate+"' is null) ")
	    		.append("            and (glcl.clrq || glcl.xxsj <= '"+e1Date+"' || '00:00:00' or ")
	    		.append("               '"+e1Date+"' is null) ")
	    		.append("            and h.cbilltypecode='46' ")
	    		.append("          group by bas.invcode,  cubas.custcode,icb.dbizdate)) s ")
	    		.append("          group by s.invcode,s.custcode,s.dbizdate ")
	    		.append("                    ) union all ")
	    	/*	buffer.append(" select sum(nvl(a.ninnum,0)) ninnum, ") 
	    			.append("          sum(nvl(a.noutnum,0)) noutnum, ") 
	    			.append("          a.custcode, ") 
	    			.append("          a.invcode,  ") 
	    			.append("          a.dbizdate ") 
	    			.append("   from ((select sum(nvl(b.ninnum, 0)) ninnum, ") 
	    			.append("                 sum(0) noutnum, ") 
	    			.append("                 b.dbizdate, ") 
	    			.append("                 invb.invcode, ") 
	    			.append("                 cubas.custcode ") 
	    			.append("            from ic_general_h h ") 
	    			.append("           inner join ic_general_b b ") 
	    			.append("              on h.cgeneralhid = b.cgeneralhid ") 
	    			.append("           inner join bd_invmandoc invm ") 
	    			.append("              on invm.pk_invmandoc = b.cinventoryid ") 
	    			.append("           inner join bd_invbasdoc invb ") 
	    			.append("              on invb.pk_invbasdoc = invm.pk_invbasdoc ") 
	    			.append("            left join bd_cumandoc cum ") 
	    			.append("              on cum.pk_cumandoc = h.ccustomerid       ") 
	    			.append("            left join bd_cubasdoc cubas ") 
	    			.append("              on cubas.pk_cubasdoc = invm.def17 ") 
	    			.append("           where nvl(h.dr, 0) = 0 ") 
	    			.append("             and nvl(b.dr, 0) = 0 ")
	    			.append("             and h.fbillflag ='4' ")
	    			.append("             and h.cdispatcherid ='1085A210000000001P2B'") 
	    			.append("             and cbilltypecode = '46' ") 
	    			.append("             and substr(invb.invcode, 0, 2) = '23' ") 
	    			.append("             and b.dbizdate >= '"+sdate+"' ") 
	    			.append("             and b.dbizdate <= '"+edate+"' ") 
	    			.append("             and h.pk_corp = '"+pk_corp+"' ") 
	    			.append("           group by invb.invcode, ") 
	    			.append("                    b.dbizdate,             ") 
	    			.append("                    h.cbiztype, ") 
	    			.append("                    cubas.custcode, ") 
	    			.append("                    h.cbilltypecode) union all ") */
	    			.append("         (select sum(0) ninnum, ") 
	    			.append("                 sum(nvl(b.noutnum, 0)) noutnum, ") 
	    			.append("                 b.dbizdate, ") 
	    			.append("                 invb.invcode, ") 
	    			.append("                 cub.custcode ") 
	    			.append("            from ic_general_h h ") 
	    			.append("           inner join ic_general_b b ") 
	    			.append("              on h.cgeneralhid = b.cgeneralhid ") 
	    			.append("           inner join bd_invmandoc invm ") 
	    			.append("              on invm.pk_invmandoc = b.cinventoryid ") 
	    			.append("           inner join bd_invbasdoc invb ") 
	    			.append("              on invb.pk_invbasdoc = invm.pk_invbasdoc ") 
	    			.append("            left join bd_cumandoc cum ") 
	    			.append("              on cum.pk_cumandoc = h.ccustomerid ") 
	    			.append("            left join bd_cubasdoc cub ") 
	    			.append("              on cub.pk_cubasdoc = cum.pk_cubasdoc ") 
	    			.append("           where nvl(h.dr, 0) = 0 ") 
	    			.append("             and nvl(b.dr, 0) = 0 ") 
	    			.append("             and h.fbillflag ='3' ")
	    			.append("             and h.cdispatcherid  = '"+cdispatcherid+"' ") 
	    			.append("             and cbilltypecode = '4C' ") 
	    			.append("             and b.dbizdate >= '"+sdate+"' ") 
	    			.append("             and b.dbizdate <= '"+edate+"' ") 
	    			//.append("             and substr(invb.invcode, 0, 2) = '23' ") 
	    			.append("             and h.pk_corp = '"+pk_corp+"' ") 
	    			.append("           group by invb.invcode, ") 
	    			.append("                    b.dbizdate, ") 
	    			.append("                    cub.custcode, ") 
	    			.append("                    h.cbiztype )) a ") 
	    			.append("  group by a.custcode, a.invcode,  a.dbizdate ");
	    		JSONObject Strjson = new JSONObject();
	    		JSONArray  strjsonArr  = new JSONArray();
	    		List list = (List) uap.executeQuery(buffer.toString(), new MapListProcessor());
				Map map = new HashMap();
				for(int  a = 0;a<list.size();a++){
					map = (Map) list.get(a);
					String custcode = map.get("custcode")==null?"":map.get("custcode").toString();
					String invcode  = map.get("invcode")==null?"":map.get("invcode").toString();
					String bqinnum = map.get("ninnum")==null?"0":map.get("ninnum").toString();
					String bqoutnum = map.get("noutnum")==null?"0":map.get("noutnum").toString();
					String dbizdate = map.get("dbizdate").toString();
					
					Strjson.put("custcode", custcode);//客商
					Strjson.put("invcode", invcode);//物料编号
					Strjson.put("bqinnum", bqinnum);//生产入库数量
					Strjson.put("bqoutnum", bqoutnum);//成品出库数量
					Strjson.put("unitcode", unitcode);//公司
					Strjson.put("dbizdate", dbizdate);//日期
					strjsonArr.add(Strjson);
				}
				return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + strjsonArr.toString() + "}]";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"查询数据异常'"+e.getMessage()+"'\"}]";
			} 
	}
	
	/**
	 * @param unitcode
	 * @return
	 */
	private String getpk_corp(String unitcode){
		String sql = "select pk_corp from bd_corp where unitcode ='"+unitcode+"' and nvl(dr,0)=0";
		String pk_corp = null;
		try {
			 pk_corp = (String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return pk_corp;
	}
	/**
	 * 弃用
	 * 依据存货编码查询默认客户代码
	 * @param invcode
	 * @param corp
	 * @return
	 */
	private  String QueryCustcode(String invcode,String corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sb = new StringBuffer();
		sb.append("  select cubas.custcode  ") 
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
			String custcode = null;
			try {
				custcode = (String) bs.executeQuery(sb.toString(), new ColumnProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		return custcode;
	}
}
