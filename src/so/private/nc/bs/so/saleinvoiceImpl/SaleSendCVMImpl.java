
package nc.bs.so.saleinvoiceImpl;

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
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleSendCvmVO;

import org.apache.commons.lang.StringUtils;

/**
 * 发出商品(外部接口)
 * 2017年11月22日 
 * @author PengJia
 *
 */
@SuppressWarnings("all")
public class SaleSendCVMImpl implements IHttpServletAdaptor{
	
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
		result = this.sendCVM(jsonStr);
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		in.close();
	}
		public String sendCVM(String jsonStr){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		JSONArray jsonArrList = new JSONArray();
		Map mls = new HashMap();
		try {
			jsonArrList = new JSONArray().fromObject(jsonStr);
		} catch (Exception e1) {
			e1.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+ e1.getMessage() + "'\"}]";
		}
		JSONObject jsonObject = null;
		List<Map> listMap = new ArrayList<Map>();//add by zwx 2019-3-20 增加传入公司参数
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
						if(key.equals("bodylist")){
							JSONArray jsonArray = jsonObject.getJSONArray(key);
							for (int i = 0; i < jsonArray.size(); i++) {
								String key1 = null;
								String value1 = null;
								JSONObject jsonOb = jsonArray.getJSONObject(i);
								Map map1 = new HashMap();
								Iterator iter = jsonOb.keys();
								while (iter.hasNext()) {
									key1 = (String) iter.next();
									value1 = jsonOb.getString(key1);
									map1.put(key1, value1);
								}
								listMap.add(map1);
							}
						}else{
							mls.put(key, value);
						}
						}
				} catch (Exception e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+ e.getMessage() + "'\"}]";
				}
			}
		}
		String username = (String) mls.get("username");
		String password = (String) mls.get("password");
		if (null==username || !username.equals("baosteel")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		if (null==password ||  !password.equals("123456")) {
			return "[{\"status\":\"error\",\"message\":\"标识错误或者标识为空\"}]";
		}
		
		//add by zwx 2019-3-20 增加传入公司参数 必输项
		ArrayList corplist = new ArrayList();
		for(int i = 0;i<listMap.size();i++){
			String code = (String) listMap.get(i).get("unitcode")==""?"":(String) listMap.get(i).get("unitcode");
			if(!corplist.contains(code)){
				corplist.add(code);
			}
		}
		if(corplist.size()==0){
			return "[{\"status\":\"error\",\"message\":\"公司参数必传\"}]";
		}
		String corpstr = StringUtils.join(corplist.toArray(), "','");
		//end by zwx
		
		JSONArray jsonArrayList = new JSONArray();		
	        HashMap<String, Object> ObMap = new  HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			sb.append("select * from  so_salecvm where nvl(dr,0)=0")
			.append(" and unitcode in ('"+corpstr+"') ");
			List ListData = new ArrayList();
			try {
				 ListData = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
			} catch (Exception e) {
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"查询数据异常'"+e.getMessage()+"'\"}]";
			}
			if(ListData.size()>0){
				HashMap map=null;
				for(int i =0;i<ListData.size();i++){
				map = (HashMap) ListData.get(i);
				JSONObject json = null;
				json = new JSONObject();
				json.put("custcode", map.get("custcode")==null?"":map.get("custcode").toString());// 客商id
				json.put("unitcode", map.get("unitcode")==null?"":map.get("unitcode").toString());// 公司代码
				json.put("chFl", map.get("chfl")==null?"":map.get("chfl").toString());// 存货分类
				json.put("vdiliveraddress", map.get("vdiliveraddress")==null?"":map.get("vdiliveraddress").toString());//地址
				json.put("jsDate", map.get("jsdate")==null?"":map.get("jsdate").toString());// 结算日期
				json.put("bykp", map.get("bykp")==null?"":map.get("bykp").toString());//本月开票
				json.put("bywkp", map.get("bywkp")==null?"":map.get("bywkp").toString());//本月未开票
				json.put("lswdz", map.get("lswdz")==null?"":map.get("lswdz").toString());//历史未开票
				json.put("sywdz", map.get("sywdz")==null?"":map.get("sywdz").toString());//上月未开票					
				jsonArrayList.add(json);
				}
			}else{
				return "[{\"status\":\"error\",\"message\":\"CVM查询条件未查到对应的数据!\"}]";
			}
		return "[{\"status\":\"success\",\"message\":\"成功\",\"bodylist\":" + jsonArrayList.toString() + "}]";
	}
	/**
	 * 查询客商收货地址档案主键
	 * 2018年3月26日14:22:24
	 * @param AddressName 地址名称
	 * @return
	 * @throws DAOException
	 */
	private static String QueryAddress(String AddressName) throws DAOException{
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		sb.append(" select addr.pk_address ") 
		.append("   from bd_address addr ") 
		.append("  where addr.addrname = '"+AddressName+"' ") 
		.append("    and nvl(addr.dr,0)=0 and pk_corp ='0001'");
		List list = null;
		Map map = new HashMap();
		String pk_addr = null;
			list = (List) dao.executeQuery(sb.toString(), new MapListProcessor());
			if(list.size()>0){
				for(int i = 0;i<list.size();i++ ){
					map = (Map) list.get(0);
					pk_addr = (String) map.get("pk_address");
				}
			}else{
				return null;
			}
		return pk_addr;
	}
}
