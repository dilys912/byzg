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
 * �ִ�����ѯʵ�ֽӿ�
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
				json = "{\"status\":\"error\",\"message\":\"���ݿ�����쳣"+e.getMessage()+"\"}";
			}
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	// add by lks 2017-11-03 ��ѯ�ִ���
	public String queryStock(String query) throws BusinessException {
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//��ȡ����Դ
		InvocationInfoProxy.getInstance().setUserDataSource(source);
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONObject json = null;
		try {
			json = new JSONObject().fromObject(query);//����json
		} catch (Exception e1) {
			e1.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json��������"+e1.getMessage()+"\"}";
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
		String username = mls.get("username").toString();//��ȡ��֤��
		String password = mls.get("password").toString();
		String corp= mls.get("corp").toString();//���չ�˾���
		if (StringUtils.isBlank(username)) {
			return "{\"status\":\"error\",\"message\":\"�û���Ϊ��\"}";
		}
		if (StringUtils.isBlank(password)) {
			return "{\"status\":\"error\",\"message\":\"����Ϊ��\"}";
		}
		if (!username.equals("baosteel")) {
			return "{\"status\":\"error\",\"message\":\"�û�������\"}";
		}
		if (!password.equals("123456")) {
			return "{\"status\":\"error\",\"message\":\"�������\"}";
		}
		if (StringUtils.isBlank(corp)) {
			return "{\"status\":\"error\",\"message\":\"��˾����Ϊ��\"}";
		}
		String pk_corp = "";
		try {
			pk_corp = getcorp(corp);//��ѯ��˾����
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"���ݿ�����쳣"+e.getMessage()+"\"}";
		}
		if(StringUtils.isBlank(pk_corp)){
			return "{\"status\":\"error\",\"message\":\"�ù�˾����:"+corp+"��NCδ�ҵ�\"}";
		}
		List conykc = new ArrayList();
		for(int i =0 ;i<lsmap.size();i++){
			StringBuffer invclasscodestr = new StringBuffer();//��ȡ��������ѯ����
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
						return "{\"status\":\"error\",\"message\":\"��ѯ23�п���������Ҫ������̱�����Ϊ��ѯ����\"}";
					}
				}else{
					String classcode = invclasscode1+"%";
					invclasscodestr.append("     and  a.invclasscode like'"+classcode+"'    " );
					if(StringUtils.isNotBlank(custcode1)){
						return "{\"status\":\"error\",\"message\":\"��ѯ21,22�п������ϲ���Ҫ������̱�����Ϊ��ѯ����\"}";
					}
					if(StringUtils.isNotBlank(invcode1)){
						invclasscodestr.append("     and a.invcode ='"+invcode1+"'    ");
					}
				}
				conykc.add(invclasscodestr);
			}else{
			   return "{\"status\":\"error\",\"message\":\"��ѯ�п�����ϴ��������������Ϊ��ѯ����\"}";
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
		jsonH.put("message", "��ȡ�ɹ�");
		StringBuffer str = new StringBuffer();
		str.append("[");
		if(result1.size()>0){
			for(int i =0 ; i<result1.size() ;i++){
				List l = (List) result1.get(i);
				for(int j = 0 ; j<l.size() ;j++){
					Map wkcmap = (Map) l.get(j);
					JSONObject jsoninfo = new JSONObject();
					String invcode = wkcmap.get("invcode")==null?"":wkcmap.get("invcode").toString();//���ϱ���
					String invname = wkcmap.get("invname")==null?"":wkcmap.get("invname").toString();//��������
					String invclasscode = wkcmap.get("invclasscode")==null?"":wkcmap.get("invclasscode").toString();//����������
					String storcode = wkcmap.get("storcode")==null?"":wkcmap.get("storcode").toString();//�ֿ����
					String storname = wkcmap.get("storname")==null?"":wkcmap.get("storname").toString();//�ֿ�����
					String num = wkcmap.get("num")==null?"":wkcmap.get("num").toString();//���
					String freezenum = wkcmap.get("freezenum")==null?"0":wkcmap.get("freezenum").toString();//��������
					jsoninfo.put("invcode", invcode);
					jsoninfo.put("invname", invname);
					jsoninfo.put("invclasscode", invclasscode);
					jsoninfo.put("storcode", storcode);
					jsoninfo.put("storname", storname);
					jsoninfo.put("depositing", num);//������
					jsoninfo.put("freezenum", freezenum);
					str.append(jsoninfo+",");
				}
			}
		}else{
			return "{\"status\":\"error\",\"message\":\"δ��ѯ�������Ϣ,�����ѯ����\"}";
		}
		str = str.deleteCharAt(str.length()-1);
		str.append("]");
		jsonH.put("bodylist", str.toString());
		return jsonH.toString();
		}
	private String getcorp(String unitcode) throws BusinessException{
		// ���ݹ�˾�����ѯ��˾����
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_corp from bd_corp where unitcode ='"+unitcode+"' and nvl(dr,0)=0");
		String pk_corp = (String) uap.executeQuery(sql.toString(), new ColumnProcessor());
		return pk_corp;
	}
}
