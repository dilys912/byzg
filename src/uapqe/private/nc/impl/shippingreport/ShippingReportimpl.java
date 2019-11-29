package nc.impl.shippingreport;

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
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;
/**
 * ���ܣ�������������cvm��Ʒ������ϸ��������
 * ��Ʒ������ϸ��ӿ�ʵ����
 * @author pengjia
 * 2018��4��24��16:16:44
 */
public class ShippingReportimpl implements IHttpServletAdaptor{

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
		String json = checkShippingReport(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	@SuppressWarnings("unchecked")
	public String checkShippingReport(String strJson)  {
		// ����json
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject().fromObject(strJson);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json��������\"}";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {	
			key = (String) iterator.next();
			value = jsonObject.getString(key);
			if (key.equals("bodylist")) {
				JSONArray array = jsonObject.getJSONArray(key);
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
		//Ψһ��ʶ
		String username = (String)mls.get("username");
		if(!"baosteel".equals(username)){
			return "{\"status\":\"error\",\"message\":\"�˺Ŵ�������˺�Ϊ��\"}";
		}
		String password = (String) mls.get("password");
		if(!"123456".equals(password)){
			return "{\"status\":\"error\",\"message\":\"��������������Ϊ��\"}";
		}
		//��ʼ����
		String startdate = (String) mls.get("startdate");
		if(StringUtils.isBlank(startdate)){
			return "{\"status\":\"error\",\"message\":\"��ʼ���ڲ���Ϊ��\"}";
		}
		//��������
		String enddate = (String) mls.get("enddate");
		if(StringUtils.isBlank(enddate)){
			return "{\"status\":\"error\",\"message\":\"�������ڲ���Ϊ��\"}";
		}
		if(new UFDate(enddate).before(new UFDate(startdate))){
			return "{\"status\":\"error\",\"message\":\"�������ڲ���С�ڿ�ʼ����\"}";
		}
		String corp = (String) mls.get("corp");
		if(StringUtils.isBlank(corp)){
			return "{\"status\":\"error\",\"message\":\"��˾���벻��Ϊ��\"}";
		}
		
		String cbiztype =null;
		//����ҵ������(��Ʒ���� table:bd_busitype)
		//�Ϻ�����
		if(corp.equals("10301")){
			cbiztype ="1016A21000000000WOPI";
		//�人
		}else if(corp.equals("10307")){
			cbiztype ="1071A21000000000OTC4";
		//������
		}else if (corp.equals("10314")){
			cbiztype ="1103A2100000000016TZ";
		//����
		}else if(corp.equals("10313")){
			cbiztype ="1097A2100000000018J9";
		//�ӱ�	
		}else if(corp.equals("10302")){
			cbiztype ="1017A210000000012Z99";
		//�ɶ�
		}else if(corp.equals("10303")){
			cbiztype ="1018A21000000001HZS9";
		//��ɽ
		}else if(corp.equals("10304")){
			cbiztype ="1019A210000000013ZXI";
		}else if(corp.equals("10318")){
			//����2
			cbiztype ="110711100000000000SI";
		}
		
		
		
		StringBuffer strinvclasscode = new StringBuffer();
		strinvclasscode.append("(");
		StringBuffer strcustcode = new StringBuffer();
		strcustcode.append("(");
		String invclasscode="";
		String custcode="";
		String invcode="";
		for (int i = 0; i < lsmap.size(); i++) {
			Map map = lsmap.get(i);
			invclasscode = map.get("invclasscode")==null?"":map.get("invclasscode").toString();
			custcode = map.get("custcode")==null?"":map.get("custcode").toString();
			invcode =  map.get("invcode")==null?"":map.get("invcode").toString();
		}
		//��˾
		//����
		//������ࡪ������>����
		//������롪������>�޸�
		StringBuffer sql = new StringBuffer();
		//��ѯ���
		sql.append(" select sum(a.noutnum) noutnum, ") 
		.append("        a.gysbm custcode, ") 
		.append("        a.dbilldate, ") 
		.append("        a.vbillcode, ") 
		.append("        count(a.duo) duonum, ") 
		.append("        a.invclasscode, ") 
		.append("        a.vdiliveraddress, ")
		.append("        a.invcode")
		.append("   from (select s.gysbm, ") 
		.append("                s.dbilldate, ") 
		.append("                s.vbillcode, ") 
		.append("                s.noutnum, ") 
		.append("                s.duo, ") 
		.append("                s.invclasscode, ") 
		.append("                s.vdiliveraddress, ")
		.append("                s.invcode")
		.append("           from (select tm1.dbilldate, ") 
		.append("                        tm1.vbillcode, ") 
		.append("                        tm1.vuserdef12, ") 
		.append("                        tm1.vuserdef7, ") 
		.append("                        tm1.vdiliveraddress, ") 
		.append("                        tb4.custcode gysbm, ") 
		.append("                        invcl.invclasscode, ") 
		.append("                        tb4.custname gys, ") 
		.append("                        tb3.custcode cysbm, ") 
		.append("                        (CASE nvl(tb3.custname, '1') ") 
		.append("                          when '1' then ") 
		.append("                           tm1.vuserdef13 ") 
		.append("                          ELSE ") 
		.append("                           tb3.custname ") 
		.append("                        END) cys, ") 
		.append("                        tb1.storname ck, ") 
		.append("                        tbs.storname drck, ") 
		.append("                        td1.noutnum, ") 
		.append("                        count(td1.crowno) duo, ") 
		.append("                        tb6.def4, ") 
		.append("                        (Case when regexp_like(bus.busicode, 'S00[1,3]') then ") 
		.append("                           '�Բ�' ") 
		.append("                          else ") 
		.append("                           '����' ") 
		.append("                        END) busicode, ") 
		.append("                        td1.vfree1, ")
		.append("                        tb5.invcode")
		.append("                   from (select * ") 
		.append("                           from ic_general_b ") 
		.append("                          where cvendorid = '' ") 
		.append("                             or '' is null ") 
		.append("                            and vbatchcode = '' ") 
		.append("                             or '' is null) td1 ") 
		.append("                   left join (select * ") 
		.append("                               from ic_general_h h ") 
		.append("                              where h.cbiztype IN ") 
		.append("                                    (SELECT pk_busitype ") 
		.append("                                       FROM bd_busitype ") 
		.append("                                      where regexp_like(busicode, ") 
		.append("                                                        Case nvl('', '1') when '�Բ�' then ") 
		.append("                                                           'S00[1,3]' ") 
		.append("                                                          when '1' then ") 
		.append("                                                           '^[A-Za-z0-9]+' ") 
		.append("                                                        END))) tm1 ") 
		.append("                     on td1.cgeneralhid = tm1.cgeneralhid ") 
		.append("                   left join bd_busitype bus ") 
		.append("                     on bus.pk_busitype = tm1.cbiztype ") 
		.append("                   left join bd_stordoc tb1 ") 
		.append("                     on tb1.pk_stordoc = tm1.cwarehouseid ") 
		.append("                   left join bd_stordoc tbs ") 
		.append("                     on tbs.pk_stordoc = tm1.cotherwhid ") 
		.append("                   left join dm_trancust tb2 ") 
		.append("                     on tb2.pk_trancust = tm1.cwastewarehouseid ") 
		.append("                   left join bd_cubasdoc tb3 ") 
		.append("                     on tb3.pk_cubasdoc = tb2.pkcusmandoc ") 
		.append("                   left join bd_cumandoc man ") 
		.append("                     on tm1.ccustomerid = man.pk_cumandoc ") 
		.append("                   left join bd_cubasdoc tb4 ") 
		.append("                     on tb4.pk_cubasdoc = man.pk_cubasdoc ") 
		.append("                   left join bd_invbasdoc tb5 ") 
		.append("                     on tb5.pk_invbasdoc = td1.cinvbasid ") 
		.append("                   left join bd_invmandoc tb6 ") 
		.append("                     on tb6.pk_invmandoc = td1.cinventoryid ") 
		.append("                   left join bd_invcl invcl ") 
		.append("                     on tb5.pk_invcl = invcl.pk_invcl ") 
		.append(" 					left join bd_corp corp ")
        .append("					  on corp.pk_corp = td1.pk_corp")
		.append("                  where nvl(td1.dr, 0) = 0 ") 
		.append("                    and tm1.cbilltypecode = '4C' ") 
		.append("                    and corp.unitcode = '"+corp+"' "); 
		if(StringUtils.isNotBlank(invcode)){
			sql.append("            and (tb5.invcode = '"+invcode+"' ) ") ;
		};
		if(StringUtils.isNotBlank(custcode)){
			sql.append("            and (tb4.custcode = '"+custcode+"' ) ") ;
		};
		if(StringUtils.isNotBlank(invclasscode)){
			sql.append("            and (invcl.invclasscode like "+invclasscode+"||'%') ");
		};
		sql.append("                 and tm1.dbilldate >= '"+startdate+"' ") 
		.append("                    and tm1.dbilldate <= '"+enddate+"' ") 
		.append("                    and nvl(td1.noutnum, 0) <> 0 ") 
		.append("                   and  tm1.cbiztype  ='"+cbiztype+"' ") 
		.append("                  group by tm1.dbilldate, ") 
		.append("                           tm1.vbillcode, ") 
		.append("                           tm1.vuserdef12, ") 
		.append("                           tm1.vuserdef7, ") 
		.append("                           tm1.vdiliveraddress, ") 
		.append("                           tb4.custcode, ") 
		.append("                           tb4.custname, ") 
		.append("                           tb3.custname, ") 
		.append("                           tb3.custcode, ") 
		.append("                           td1.noutnum, ") 
		.append("                           tb1.storname, ") 
		.append("                           invcl.invclasscode, ") 
		.append("                           tbs.storname, ") 
		.append("                           tb6.def4, ") 
		.append("                           tm1.vuserdef13, ") 
		.append("                           bus.busicode, ") 
		.append("                           td1.vfree1, ") 
		.append("                           tb5.invcode) s")
		.append("          where (s.ck = '' or '' is null) ") 
		.append("            and (s.gys = '' or '' is null) ") 
		.append("            and (s.vbillcode = '' or '' is null) ") 
		.append("            and (s.vfree1 = '' or '' is null)) a ") 
		.append("  group by a.gysbm, ") 
		.append("           a.dbilldate, ") 
		.append("           a.vbillcode, ") 
		.append("           a.invclasscode, ") 
		.append("           a.vdiliveraddress, ")
		.append("           a.invcode"); 

		IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List list = null;
		try {
			list = (List<Object>) uap.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"���ݿ��ѯ����\""+e.getMessage()+"}";
		}
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String duonum = map.get("duonum") == null?"":map.get("duonum").toString();//����
			String invclasscode1 = map.get("invclasscode") == null?"":map.get("invclasscode").toString();//����������
			String gysbm = map.get("custcode") == null?"":map.get("custcode").toString();//���̱���
			String dbilldate = map.get("dbilldate") == null?"":map.get("dbilldate").toString();//����ʱ��
			String vbillcode = map.get("vbillcode") == null?"":map.get("vbillcode").toString();//���۵���
			String noutnum = map.get("noutnum") == null?"":map.get("noutnum").toString();//��������
			String addr = map.get("vdiliveraddress") == null?"":map.get("vdiliveraddress").toString();//�ջ���ַ
			String invcode1 = map.get("invcode")==null?"":map.get("invcode").toString();//�������
			JSONObject rjson = new JSONObject();
			rjson.put("corp", corp);
			rjson.put("duonum", duonum);
			rjson.put("invclasscode", invclasscode1);
			rjson.put("invcode", invcode1);
			rjson.put("custcode", gysbm);
			rjson.put("dbilldate", dbilldate);
			rjson.put("vbillcode", vbillcode);
			rjson.put("noutnum", noutnum);
			String pk_address = getaddr(addr)==null?"":getaddr(addr);
			rjson.put("pk_custaddr", pk_address);
			jsonArray.add(rjson);
		}
		return "{\"status\":\"success\",\"message\":\"�ɹ�\",\"bodylist\":"+jsonArray.toString()+"}";
	}
	
	private String getaddr(String addr){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//��ַ��Ϣ
		StringBuffer sql = new StringBuffer();
		sql.append(" select addr.pk_address ") 
		.append("   from bd_address addr ") 
		.append("  where addr.addrname = '"+addr+"' ") 
		.append("    and nvl(addr.dr,0)=0 and pk_corp ='0001'");
		List list = null;
		try {
			list = (List) bs.executeQuery(sql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"���ݿ��ѯ����\""+e.getMessage()+"}";
		}
		String pk_address = null;
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 pk_address=  map.get("pk_address") == null?"":map.get("pk_address").toString();
			}
		}else{
			return null;
		}
		return pk_address;
	}
}
