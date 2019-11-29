package nc.impl.fy;

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
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

/**
 * ʣ��δ������Ʒ
 * @author PengJia
 *2018��3��16��16:45:28
 */
public class DespatchImpl implements IHttpServletAdaptor {
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = null;
		InputStream inputStream = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		String jsonStr = buffer.toString();
		result = this.despatch(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
	}
	public String despatch(String jsonstr) {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
		try {
			jsonArrList = new JSONArray().fromObject(jsonstr);
		} catch (Exception e1) {
			e1.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"json��������-����json��ʽ'"+ e1.getMessage() + "'\"}]";
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
					return "[{\"status\":\"error\",\"message\":\"json��������-����json��ʽ'"+ e.getMessage() + "'\"}]";
				}
			}
		}
		
		String username = (String) mls.get("username");
		String password = (String) mls.get("password");
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"��ʶ������߱�ʶΪ��\"}]";
		}
		
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"��ʶ������߱�ʶΪ��\"}]";
		}
		//���̴���
		String custcode = (String) mls.get("custcode");
		//�������
		String invcode = (String) mls.get("invcode");
		//��˾����
		String corp = (String) mls.get("corp");
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select sum(dm.dnum) invsum, "); 
		sb.append("          sum(dm.dsendnum) invysum, "); 
		sb.append("          bas.custcode, "); 
		sb.append("          invbas.invcode ");
		sb.append("   from dm_delivdaypl dm ") ;
		sb.append("   left join so_sale so ") ;
		sb.append("     on dm.PKBILLH = so.CSALEID "); 
		sb.append("   left join bd_cumandoc man "); 
		sb.append("     on man.pk_cumandoc = dm.creceiptcorpid "); 
		sb.append("   left join bd_cubasdoc bas ") ;
		sb.append("     on bas.pk_cubasdoc = man.pk_cubasdoc ") ;
		sb.append("   left join bd_invmandoc invman ") ;
		sb.append("     on invman.pk_invmandoc = dm.pkinv ") ;
		sb.append("   left join bd_invbasdoc invbas ") ;
		sb.append("     on invman.pk_invbasdoc = invbas.pk_invbasdoc ") ;
		sb.append("   left join bd_corp corp");
		sb.append("      on corp.pk_corp = so.pk_corp");
		sb.append("  where bas.custcode ='"+custcode+"' ") ;
		sb.append("    and invbas.invcode ='"+invcode+"' ");
		if(StringUtils.isNotBlank(corp)){
		sb.append("   and corp.unitcode = '"+corp+"'");
		}
		sb.append("	 and so.fstatus = '2'");
		sb.append("    and nvl(dm.dr,0) = 0");
		sb.append("    and nvl(so.dr,0) = 0");
		sb.append(" group by bas.custcode,");
		sb.append("          invbas.invcode");
		List sumList = new ArrayList();
		try {
			sumList= (List) bs.executeQuery(sb.toString(), new MapListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"���ݿ��ѯ�쳣'"+ e.getMessage() + "'\"}]";
		}
		JSONArray jsonArray = new JSONArray();
		for(int i =0;i<sumList.size();i++){
			JSONObject json = new JSONObject();
			HashMap sumMap = new HashMap();
			sumMap = (HashMap) sumList.get(i);
			//���̱���
			String kscode = (String) sumMap.get("custcode");
			//�������
			String chcode = (String) sumMap.get("invcode");
			//����������
			Object invsum = sumMap.get("invsum")==null?"0":sumMap.get("invsum");
			//�ѷ�������
			Object invysum = sumMap.get("invysum")==null?"0":sumMap.get("invysum");
			//ʣ��δ��������                                                                                                                                                                                                                                                                                                                                                 
			UFDouble invnsum = new UFDouble(invsum.toString()).sub(new UFDouble(invysum.toString()));
			//����3%��������
			UFDouble invlv = new UFDouble(invsum.toString()).multiply(new UFDouble(0.03));
			//����δ���������Ƿ����������3%
			int ff = (int) invlv.sub(invnsum).doubleValue();
			//Yes
			if(ff>=0){
				json.put("custcode", kscode);
				json.put("invcode", chcode);
				json.put("invnsum", "0");
				jsonArray.add(json);
				return "[{\"status\":\"success\",\"message\":\"�ɹ�\",\"bodylist\":" + jsonArray.toString() + "}]";
		    //No
			}else{
				json.put("custcode", kscode);
				json.put("invcode", chcode);
				json.put("invnsum", invnsum);
				jsonArray.add(json);
			}
		}
		if(sumList.size()>0){
			return "[{\"status\":\"success\",\"message\":\"�ɹ�\",\"bodylist\":" + jsonArray.toString() + "}]";
		}else{
			JSONObject json1 = new JSONObject();
			json1.put("invnsum", "0");
			jsonArray.add(json1);
			return "[{\"status\":\"success\",\"message\":\"�ɹ�\",\"bodylist\":" + jsonArray.toString() + "}]";
		}
	}
}
