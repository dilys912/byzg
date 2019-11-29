package nc.impl.productmove;

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

import org.apache.commons.lang.StringUtils;
/**
 * ��Ʒ�춯��̬���
 * 2018��4��19��13:11:48
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class ProductMoveImpl implements IHttpServletAdaptor{

	public void doAction(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		StringBuffer sb = null;
		String s ="";
		InputStream in = request.getInputStream();  
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));  
        sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }
		String json = queryProductMove(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	public String queryProductMove(String query) {
		JSONObject json = null;
		try {
			json = new JSONObject().fromObject(query);//����json
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json��������\"}";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();
		Iterator iterator = json.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = json.getString(key);
				mls.put(key, value);
		}
		//��˾��� 
		String gcorp = mls.get("unitcode").toString();
		String pk_corp = getcorp(gcorp);
		String username = mls.get("username").toString();
		String password = mls.get("password").toString();
		JSONArray  injsonarr  = new JSONArray();
		JSONObject injson  = new JSONObject();
		//�жϵ��ýӿڵĹ�˾
		if ("baosteel".equals(username) && "123456".equals(password)) {
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			StringBuffer sb = new StringBuffer();
			if(gcorp.equals("10301")){
			  sb.append(" select a.custcode custcode,")
			    .append("b.invcode invcode,StockNumber ")
				.append("    from (select custcode, a, sum(z) z, sum(d) d, sum(f) f ") 
				.append("           from ( select a.a,a.custcode, ") 
				.append("                       sum(a.z) z, ") 
				.append("                        nvl(sum(�ϺŶ�����), 0) d, ") 
				.append("                 sum(z - nvl(�ϺŶ�����, 0)) f ") 
				.append("            from (select case substr(f.invclasscode, 1, 2) ") 
				.append("                when '23' then   ") 
				.append("                     '����'") 
				.append("                         else") 
				.append("                            '�޸�'") 
				.append("                               end a,a.cinvbasid,     ") 
				.append("                                nvl(c.custcode, 0) custcode, ") 
				.append("                                sum(nvl(a.ninspacenum, 0)) z ") 
				.append("             from V_IC_ONHANDNUM6 a") 
				.append("          left outer join bd_invmandoc b on a.cinventoryid =      b.pk_invmandoc") 
				.append("     left outer join bd_invbasdoc d on d.pk_invbasdoc =b.pk_invbasdoc") 
				.append("    left outer join bd_invcl f on d.pk_invcl =  f.pk_invcl") 
				.append("       left outer join bd_cubasdoc c on c.pk_cubasdoc =   b.def17") 
				.append("        left outer join bd_stordoc e on e.pk_stordoc = a.cwarehouseid") 
				.append("       where a.pk_corp = '"+pk_corp+"'") 
				.append("          and substr(f.invclasscode, 1, 2) in") 
				.append("            ('21', '22', '23')") 
				.append("             and a.ninspacenum <> 0") 
				.append("  and ( c.custcode = '' or  '' is null)and (f.invclasscode like ''||'%' or '' is null)                          ") 
				.append("    group by substr(f.invclasscode, 1, 2),   c.custcode,a.cinvbasid") 
				.append("    order by c.custcode,substr(f.invclasscode, 1, 2)) a                        ") 
				.append("    left outer join (select cinvbasid,sum(nvl(g.nfreezenum,0) -nvl(g.ndefrznum,0)) �ϺŶ����� ") 
				.append("       from ic_freeze g") 
				.append("      where dthawdate >= '2013-01-01'") 
				.append("  group by cinvbasid) g on g.cinvbasid = a.cinvbasid ") 
				.append("     group by a.a,  a.custcode") 
				.append(" ) a") 
				.append("    group by custcode, a) a,")
				.append("  (select a.a,a.invcode,a.invname,a.custcode,")
				.append("     sum(a.StockNumber) StockNumber,")
				.append("       nvl(sum(�ϺŶ�����), 0) �ϺŶ�����,")
				.append("       sum(StockNumber - nvl(�ϺŶ�����, 0)) �Ϻſ��ÿ�� ")
				.append("   from (select case substr(f.invclasscode, 1, 2)")
				.append("      when '23' then")
				.append("           '����'")
				.append("            else")
				.append("             '�޸�'")
				.append("  end a,")
				.append("    d.invcode,")
				.append("    d.invname,")
				.append("       a.cinvbasid,")
				.append("      nvl(c.custcode, 0) custcode,")
				.append("     sum(nvl(a.ninspacenum, 0)) StockNumber")
				.append("  from V_IC_ONHANDNUM6 a")
				.append("   left outer join bd_invmandoc b on a.cinventoryid =b.pk_invmandoc")
				.append(" left outer join bd_invbasdoc d on d.pk_invbasdoc =b.pk_invbasdoc")
				.append("  left outer join bd_invcl f on d.pk_invcl = f.pk_invcl")
				.append("  left outer join bd_cubasdoc c on c.pk_cubasdoc = b.def17")
				.append("  left outer join bd_stordoc e on e.pk_stordoc =a.cwarehouseid")
				.append("   where a.pk_corp = '"+pk_corp+"'")
				.append("    and substr(f.invclasscode, 1, 2) in ('21', '22', '23')")
				.append("    and a.ninspacenum <> 0 ")
				.append("     and ( c.custcode = '' or  '' is null)and (f.invclasscode like ''||'%' or '' is null)                                    ")
				.append("     group by substr(f.invclasscode, 1, 2),")
				.append("     c.custcode, d.invcode,d.invname,a.cinvbasid")
				.append(" order by c.custcode, substr(f.invclasscode, 1, 2)) a")
				.append("        left outer join (select cinvbasid,")
				.append("       sum(nvl(g.nfreezenum, 0) -")
				.append("          nvl(g.ndefrznum, 0)) �ϺŶ����� ")
				.append("        from ic_freeze g")
				.append("             where dthawdate >= '2013-01-01'")
				.append("   group by cinvbasid) g on g.cinvbasid =a.cinvbasid")
				.append("      group by a.a,a.invcode, a.invname, a.custcode")
				.append("          ) b ")
				.append("           where a.a = b.a")
				.append("            and a.custcode = b.custcode")
				.append("          and �Ϻſ��ÿ��<>0")
				.append("             order by a.custcode, a.a");
				 
			}else{
				sb.append(" select a.custcode custcode, b.invcode invcode, StockNumber ") 
				.append("   from (select custcode, a, sum(z) z, sum(d) d, sum(f) f ") 
				.append("           from (select a.a, ") 
				.append("                        a.custcode, ") 
				.append("                        sum(a.z) z, ") 
				.append("                        nvl(sum(liaodj), 0) d, ") 
				.append("                        sum(z - nvl(liaodj, 0)) f ") 
				.append("                   from (select case substr(f.invclasscode, 1, 2) ") 
				.append("                                  when '23' then ") 
				.append("                                   '����' ") 
				.append("                                  else ") 
				.append("                                   '�޸�' ") 
				.append("                                end a, ") 
				.append("                                a.cinvbasid, ") 
				.append("                                nvl(c.custcode, 0) custcode, ") 
				.append("                                sum(nvl(a.ninspacenum, 0)) z ") 
				.append("                           from V_IC_ONHANDNUM6 a ") 
				.append("                           left outer join bd_invmandoc b ") 
				.append("                             on a.cinventoryid = b.pk_invmandoc ") 
				.append("                           left outer join bd_invbasdoc d ") 
				.append("                             on d.pk_invbasdoc = b.pk_invbasdoc ") 
				.append("                           left outer join bd_invcl f ") 
				.append("                             on d.pk_invcl = f.pk_invcl ") 
				.append("                           left outer join bd_cubasdoc c ") 
				.append("                             on c.pk_cubasdoc = b.def17 ") 
				.append("                           left outer join bd_stordoc e ") 
				.append("                             on e.pk_stordoc = a.cwarehouseid ") 
				.append("                          where a.pk_corp = '"+pk_corp+"' ") 
				.append("                            and substr(f.invclasscode, 1, 2) in ") 
				.append("                                ('21', '22', '23') ") 
				.append("                            and a.ninspacenum <> 0 ") 
				.append("                          group by substr(f.invclasscode, 1, 2), ") 
				.append("                                   c.custcode, ") 
				.append("                                   a.cinvbasid ") 
				.append("                          order by c.custcode, substr(f.invclasscode, 1, 2)) a ") 
				.append("                   left outer join (select cinvbasid, ") 
				.append("                                          sum(nvl(g.nfreezenum, 0) - ") 
				.append("                                              nvl(g.ndefrznum, 0)) liaodj ") 
				.append("                                     from ic_freeze g ") 
				.append("                                    where dthawdate >= '2013-01-01' ") 
				.append("                                    group by cinvbasid) g ") 
				.append("                     on g.cinvbasid = a.cinvbasid ") 
				.append("                  group by a.a, a.custcode) a ") 
				.append("          group by custcode, a) a, ") 
				.append("        (select a.a, ") 
				.append("                a.invcode, ") 
				.append("                a.invname, ") 
				.append("                a.custcode, ") 
				.append("                sum(liaozkc - nvl(liaodj, 0)) StockNumber ") 
				.append("           from (select case substr(f.invclasscode, 1, 2) ") 
				.append("                          when '23' then ") 
				.append("                           '����' ") 
				.append("                          else ") 
				.append("                           '�޸�' ") 
				.append("                        end a, ") 
				.append("                        d.invcode, ") 
				.append("                        d.invname, ") 
				.append("                        a.cinvbasid, ") 
				.append("                        nvl(c.custcode, 0) custcode, ") 
				.append("                        sum(nvl(a.ninspacenum, 0)) liaozkc ") 
				.append("                   from V_IC_ONHANDNUM6 a ") 
				.append("                   left outer join bd_invmandoc b ") 
				.append("                     on a.cinventoryid = b.pk_invmandoc ") 
				.append("                   left outer join bd_invbasdoc d ") 
				.append("                     on d.pk_invbasdoc = b.pk_invbasdoc ") 
				.append("                   left outer join bd_invcl f ") 
				.append("                     on d.pk_invcl = f.pk_invcl ") 
				.append("                   left outer join bd_cubasdoc c ") 
				.append("                     on c.pk_cubasdoc = b.def17 ") 
				.append("                   left outer join bd_stordoc e ") 
				.append("                     on e.pk_stordoc = a.cwarehouseid ") 
				.append("                  where a.pk_corp = '"+pk_corp+"' ") 
				.append("                    and substr(f.invclasscode, 1, 2) in ('21', '22', '23') ") 
				.append("                    and a.ninspacenum <> 0 ") 
				.append("                  group by substr(f.invclasscode, 1, 2), ") 
				.append("                           c.custcode, ") 
				.append("                           d.invcode, ") 
				.append("                           d.invname, ") 
				.append("                           a.cinvbasid ") 
				.append("                  order by c.custcode, substr(f.invclasscode, 1, 2)) a ") 
				.append("           left outer join (select cinvbasid, ") 
				.append("                                  sum(nvl(g.nfreezenum, 0) - ") 
				.append("                                      nvl(g.ndefrznum, 0)) liaodj ") 
				.append("                             from ic_freeze g ") 
				.append("                            where dthawdate >= '2013-01-01' ") 
				.append("                            group by cinvbasid) g ") 
				.append("             on g.cinvbasid = a.cinvbasid ") 
				.append("          group by a.a, a.invcode, a.invname, a.custcode) b ") 
				.append("  where a.a = b.a ") 
				.append("    and a.custcode = b.custcode ") 
				.append("    and StockNumber <> 0 ") 
				.append("  order by a.custcode, a.a ");
			}
			
			List inlist = null;
			try {
				inlist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"���ݿ��쳣\""+e.getMessage()+"}";
			}
			
			
			for(int i =0;i<inlist.size();i++){
				Map inMap = new HashMap();
				inMap = (Map) inlist.get(i);
				//���̱���
				String custcode = inMap.get("custcode")==null?"":inMap.get("custcode").toString();
				//���ϱ���
				String invcode = inMap.get("invcode")==null?"":inMap.get("invcode").toString();
				//��ĩ�������
				String stocknumber = inMap.get("stocknumber")==null?"0":inMap.get("stocknumber").toString();
//				injson.put("username", "baosteel");
//				injson.put("password", "123456");
				injson.put("unitcode", gcorp);
				injson.put("custcode", custcode);
				injson.put("invcode", invcode);
				injson.put("invclasscode", getinvcl(invcode)==null?"":getinvcl(invcode));
				injson.put("stocknumber", stocknumber);
				injsonarr.add(injson);
			}
		}else{
			return "{\"status\":\"error\",\"message\":\"�û���,������֤ʧ��!\"}";
		}
		return "{\"status\":\"success\",\"message\":\"�ɹ�\",\"bodylist\":"+injsonarr.toString()+"}";
		} 
	/**
	 * ��ѯ��˾����
	 * @param unitcode
	 * @return
	 */
	private String getcorp(String unitcode){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		// ��ѯ���̵����ı���
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
	/**
	 * ��ѯ�������
	 * @param invcode
	 * @return
	 */
	private String getinvcl(String invcode){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		// ��ѯ���̵����ı���
		StringBuffer sql = new StringBuffer();
		sql.append(" select invcl.invclasscode ") 
		.append("   from bd_invbasdoc invbas ") 
		.append("   left join bd_invcl invcl ") 
		.append("     on invbas.pk_invcl = invcl.pk_invcl ") 
		.append("  where invbas.invcode = '"+invcode+"' ") 
		.append("        and nvl(invbas.dr,0)=0 ");
		List list = null;
		String invclasscode = null;
		try {
			list = (List) bs.executeQuery(sql.toString(), new MapListProcessor());
			if(list.size()>0){
				for(int i =0;i<list.size();i++){
					Map map=(Map) list.get(i);
					invclasscode = map.get("invclasscode")==null?"":map.get("invclasscode").toString();
				}
			}else{
				return null;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return invclasscode;
		
	}
}
