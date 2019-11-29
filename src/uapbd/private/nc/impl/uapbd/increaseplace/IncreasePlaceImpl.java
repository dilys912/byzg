package nc.impl.uapbd.increaseplace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.uap.sfapp.util.SFAppServiceUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.b202.AddressVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.scm.pub.SCMEnv;

import org.apache.commons.lang.StringUtils;

/**
 * 新增地点档案
 * 2018年4月24日16:17:27
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class IncreasePlaceImpl implements IHttpServletAdaptor {
	
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = null;
		String s ="";
		InputStream in = request.getInputStream();  
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));  
	    sb = new StringBuffer();  
	    while ((s = br.readLine()) != null) {  
	        sb.append(s);  
	    }
		String json = this.saveIincreasePlace(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	public String saveIincreasePlace(String str) {
		JSONObject json =null;
		try {
			json = new JSONObject().fromObject(str);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误\"}";
		}
		AddressVO addrvo = new AddressVO();
		String username = json.getString("username");
		String password = json.getString("password");
		Object code =null;
		if(!username.equals("baosteel") || !password.equals("123456".toString())){
			return "{\"status\":\"error\",\"massage\":\"用户名错误或密码错误\"}";
		}
			String addrname =  StringUtils.trim(json.getString("addrname"));//地点全称
			String addrshortname = json.getString("addrshortname");//地点简称
			String county = json.getString("county");//所属县市
			String province = json.getString("province");//所属省份
			String postalcode ="000000";  //邮政编码
			String pk_areacl = json.getString("pk_areacl"); //地点分类编码
			String pk_corp = json.getString("pk_corp");//公司主键
			//nc地址主键
			String pk_address = json.getString("ncdzpk")==null?"":json.getString("ncdzpk").toString();
			BillcodeGenerater bc= new BillcodeGenerater();
			String billcode="";//获取单据号
			try {
				billcode = SFAppServiceUtil.getBillcodeRuleService().getBillCode_RequiresNew("psndoc", pk_corp,null, null);
			} catch (ValidationException e2) {
				e2.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"获取单据号异常:"+e2.getMessage()+"\"}]";
			} catch (BusinessException e2) {
				e2.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"获取单据号异常:"+e2.getMessage()+"\"}]";
			}
			String count = billcode.substring(billcode.length()- 4,billcode.length());
			String addrcode  =pk_areacl+count;//地点编码
			if(StringUtils.isBlank(addrname)){
				return "{\"status\":\"error\",\"massage\":\"地点全称不能为空\"}";
			}
			if(StringUtils.isBlank(addrshortname)){
				return "{\"status\":\"error\",\"massage\":\"地点简称不能为空\"}";
			}
			if(StringUtils.isBlank(county)){
				return "{\"status\":\"error\",\"massage\":\"所属县市不能为空\"}";
			}
			if(StringUtils.isBlank(province)){
				return "{\"status\":\"error\",\"massage\":\"所属省份不能为空\"}";
			}
			if(StringUtils.isBlank(pk_areacl)){
				return "{\"status\":\"error\",\"massage\":\"地点分类不能为空\"}";
			}
			String areacl =null;
			try {
				areacl=getaddr(pk_areacl);
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"massage\":\"数据库访问错误"+e.getMessage()+"\"}";
			}
			//新增
			if(StringUtils.isBlank(pk_address)){
				addrvo.setAddrcode(addrcode);
				addrvo.setAddrname(addrname);
				addrvo.setAddrshortname(addrshortname);
				addrvo.setCounty(county);
				addrvo.setProvince(province);
				addrvo.setPk_corp("0001");
				addrvo.setPk_areacl(areacl);
				addrvo.setPostalcode(postalcode);
				addrvo.setDr(0);
			//修改
			}else{
				String sql="select addrcode from bd_address where pk_address = '"+pk_address+"' and nvl(dr,0)=0 and pk_corp='0001'";
				
				try {
					code= new BaseDAO().executeQuery(sql, new ColumnProcessor());
				} catch (DAOException e) {
					e.printStackTrace();
					return "{\"status\":\"error\",\"massage\":\"数据库访问错误"+e.getMessage()+"\"}";
				}
				if(code!=null){
					addrvo.setPrimaryKey(pk_address);
					addrvo.setAddrcode(code.toString());
					addrvo.setAddrname(addrname);
					addrvo.setAddrshortname(addrshortname);
					addrvo.setCounty(county);
					addrvo.setProvince(province);
					addrvo.setPk_corp("0001");
					addrvo.setPk_areacl(areacl);
					addrvo.setPostalcode(postalcode);
					addrvo.setDr(0);
				}else{
					return "{\"status\":\"error\",\"massage\":\"未能查询到ERP地点档案信息\"}";
				}
				
				
			}
				String s = null;
				try {
					if(StringUtils.isBlank(pk_address)){
						s = new BaseDAO().insertVO(addrvo);
						String sql = "select addrcode from bd_address where pk_corp ='0001' and pk_address ='"+s+"'";
						Object dzcode = new BaseDAO().executeQuery(sql, new ColumnProcessor());
						return "{\"status\":\"success\",\"massage\":\"新增成功\",\"bodylist\":[{\"dzcode\":\""+dzcode+"\",\"pk_address\":\""+s+"\"}]}";
					}else{
						new BaseDAO().updateVO(addrvo);
					}
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return "{\"status\":\"success\",\"massage\":\"修改成功\",\"bodylist\":[{\"dzcode\":\""+code.toString()+"\",\"pk_address\":\""+pk_address+"\"}]}";		
	}

	private String getaddr(String areaclcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		// 查询客商档案的编码
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_areacl from bd_areacl where areaclcode ='"+areaclcode+"' and nvl(dr,0)=0");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		String areacl = null;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 areacl= (String) map.get("pk_areacl");
			}
			return areacl;
		}
		return null;
	}
}