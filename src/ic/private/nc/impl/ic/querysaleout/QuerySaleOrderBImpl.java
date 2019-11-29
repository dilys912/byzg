package nc.impl.ic.querysaleout;

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
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;
public class QuerySaleOrderBImpl implements IHttpServletAdaptor {
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String result = querySaleOrderB(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }

	public String querySaleOrderB(String json) {
		// TODO Auto-generated method stub
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//��ȡ����Դ
		InvocationInfoProxy.getInstance().setUserDataSource(source);//��������Դ
		//JSONObject jsonstr = null;
		StringBuffer res = new StringBuffer();//�������json
		res.append("[");
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = new nc.net.sf.json.JSONArray().fromObject(json);
		} catch (Exception e) {
			return "[{\"status\":\"error\",\"message\":\"json��������-����json��ʽ:"+e.getMessage()+"\"}]";
		}
		List<Map> lsmap = new ArrayList<Map>();
		if(jsonarray != null&&jsonarray.size()>0){
			for(int i =0;i<jsonarray.size();i++){
				Map map = new HashMap();
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while(it.hasNext()){
					key = (String) it.next();
					value = jsonobject.getString(key);
					map.put(key, value);
				}
				lsmap.add(map);
			}
		}
		for (int i = 0;i<lsmap.size();i++){
			StringBuffer result = new StringBuffer();//����ӱ�
			result.append("[");
			Map mls = lsmap.get(i);
			String username=mls.get("username")==null?"":mls.get("username").toString();//�û���
			if(StringUtils.isNotBlank(username)){
				if(!username.equals("baosteel")){
					return "[{\"status\":\"error\",\"message\":\"�û�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"�û���Ϊ��\"}]"; 
			}
			String pwd=mls.get("password")==null?"":mls.get("password").toString();//����
			if(StringUtils.isNotBlank(pwd)){
				if(!pwd.equals("123456")){
					return "[{\"status\":\"error\",\"message\":\"�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"����Ϊ��\"}]"; 
			}
			String billcode = mls.get("billcode")==null?"":mls.get("billcode").toString();//����������
			if(StringUtils.isBlank(billcode)){
				return "[{\"status\":\"error\",\"message\":\"��������Ϊ��\"}]";
			}
			String invbascode = mls.get("invcode")==null?"":mls.get("invcode").toString();//�Ϻ�
			String applyCorp=mls.get("corp")==null?"":mls.get("corp").toString();//���빫˾
			if(StringUtils.isBlank(applyCorp)){
				return "[{\"status\":\"error\",\"message\":\"��˾Ϊ��\"}]"; 
			}
			String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
			List corpList = null;
			try {
				corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
				if(corpList == null || corpList.size() == 0){
					return "[{\"status\":\"error\",\"message\":\"��˾��NCϵͳ�����ڣ�����\"}]"; 
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]";
			}
			Map map2 = (Map) corpList.get(0);
			String pk_corp = map2.get("pk_corp").toString();
			StringBuffer tj = new StringBuffer();//��ѯ����
			if(StringUtils.isNotBlank(invbascode)){
				tj.append(" b.vfirstbillcode = '"+billcode+"'"+"\n")
				  .append(" and e.invcode = '"+invbascode+"'"+"\n")
				  .append(" and a.pk_corp = '"+pk_corp+"'"+"\n");
			}else{
				tj.append(" b.vfirstbillcode = '"+billcode+"'"+"\n")
				  .append(" and a.pk_corp = '"+pk_corp+"'"+"\n");
			}
			StringBuffer sb = new StringBuffer();//��ѯSQL ȡ���̬�����۳��ⵥ
			sb.append(" select a.pk_corp,d.custcode,g.fstatus, ") 
			.append("        e.invcode,f.invclasscode,sum(b.noutnum) as countnum,   ") 
			.append("        sum(b.nshouldoutnum) as nshouldoutnum")
			.append("        from ic_general_h a    ") 
			.append("        left join ic_general_b b on a.cgeneralhid  = b.cgeneralhid   ") 
			.append("        left join so_sale g on b.cfirstbillhid = g.csaleid    ")
			.append("        left join bd_cumandoc c on a.ccustomerid = c.pk_cumandoc   ") 
			.append("        left join bd_cubasdoc d on c.pk_cubasdoc = d.pk_cubasdoc   ") 
			.append("        left join bd_invbasdoc e on b.cinvbasid = e.pk_invbasdoc   ") 
			.append("        left join bd_invcl f on e.pk_invcl = f.pk_invcl   ") 
			.append("        where "+tj+" ") 
			.append("        and a.fbillflag=3 ") 
			.append("        and (nvl(a.dr,0)=0 and nvl(b.dr,0)=0)   ") 
			.append("        group by ") 
			.append("        a.pk_corp,d.custcode,  ") 
			.append("        e.invcode, ") 
			.append("        f.invclasscode, ") 
			.append("        g.fstatus");

			List ls = null;
			nc.net.sf.json.JSONObject jsonH = new nc.net.sf.json.JSONObject();//��װ��ͷ����
			try {
				ls= (List) uap.executeQuery(sb.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ��ѯ����:+"+e.getMessage()+"\"}]";
			}
			if(ls.size()>0){
				Map map = (Map) ls.get(0);
				String pkcorp = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
				String custcode = map.get("custcode")==null?"":map.get("custcode").toString();
				String zt = map.get("fstatus")==null?"":map.get("fstatus").toString();
				String ztname = "";
				if(zt.equals("1")){
					ztname = "����";
				}else if(zt.equals("2")){
					ztname = "���";
				}else if(zt.equals("3")){
					ztname = "����";
				}else if(zt.equals("5")){
					ztname = "����";
				}else if(zt.equals("6")){
					ztname = "����";
				}else{
					ztname = "δ֪";
				}
				for(int j = 0;j<ls.size();j++){
					nc.net.sf.json.JSONObject jsonB = new nc.net.sf.json.JSONObject();//��װ��������
					Map map1 = (Map) ls.get(j);
					String invcode = map1.get("invcode")==null?"":map1.get("invcode").toString();
					String invclasscode = map1.get("invclasscode")==null?"":map1.get("invclasscode").toString();
					String noutnum = map1.get("countnum")==null?"":map1.get("countnum").toString();
					String nshouldoutnum = map1.get("nshouldoutnum")==null?"":map1.get("nshouldoutnum").toString();
					UFDouble nnotoutnum = new UFDouble(nshouldoutnum).sub(new UFDouble(noutnum));//
					jsonB.put("invcode", invcode);
					jsonB.put("invclasscode", invclasscode);
					jsonB.put("noutnum", noutnum);
					jsonB.put("nshouldoutnum", nshouldoutnum);
					jsonB.put("nnotoutnum", nnotoutnum);
					if(j == ls.size()-1){
						result.append(jsonB+"\n");
					}else{
						result.append(jsonB+","+"\n");
					}
				}
				result.append("]");
				jsonH.put("status", "success");
				jsonH.put("billcode", billcode);
				jsonH.put("ztname", ztname);
				jsonH.put("pk_corp", pkcorp);
				jsonH.put("custcode", custcode);
				jsonH.put("bodylist", result.toString());
			}else{
				nc.net.sf.json.JSONObject jsonB = new nc.net.sf.json.JSONObject();
				jsonB.put("invcode", invbascode);
				jsonB.put("invclasscode", "");
				jsonB.put("noutnum", "0");
				jsonB.put("nshouldoutnum", "0");
				jsonB.put("nnotoutnum", "0");
				result.append(jsonB+"\n");
				result.append("]");
				jsonH.put("status", "success");
				jsonH.put("billcode", billcode);
				jsonH.put("ztname", "");
				jsonH.put("pk_corp", pk_corp);
				jsonH.put("custcode", "");
				jsonH.put("bodylist", result.toString());
			}
			if(i == lsmap.size()-1){
				res.append(jsonH+"\n");
			}else{
				res.append(jsonH+","+"\n");
			}
		}
		res.append("]");
		return res.toString();
	}

}
