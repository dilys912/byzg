package nc.impl.pub.cust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.NCLocator;
import nc.itf.bh.INcBhItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.cust.ICuBasDocTmp;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAddrVO;
import nc.vo.bd.b08.CustBankVO;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.cu.SteelCubasdocVO;
import nc.vo.cu.SteelCustAddrVO;
import nc.vo.cu.SteelCustBankVO;
import nc.vo.cu.SteelCustMultiBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;
/**
 * 客商申请实现
 * 2018年3月13日15:06:38
 * @author lks
 *
 */
public class CustbasdocImpl implements IHttpServletAdaptor{

	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = null;
		String s = "";
		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
		sb = new StringBuffer();
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		String json = sendCustdoc(sb.toString());
		response.getOutputStream().write(json.getBytes("UTF-8"));
		br.close();
	}
	
	public String sendCustdoc(String jsonstr) { 
		// 解析json
		JSONObject json = null;
		String jg = null;
		try {
			json = new JSONObject().fromObject(jsonstr);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"json解析错误\"}";
		}
		JSONArray addrjson=json.getJSONArray("addrlist");
		List<Map> addrlist =new ArrayList<Map>();
		if(json!=null&&json.size()>0){
			for (int i = 0; i < addrjson.size(); i++) {
				JSONObject gjson = addrjson.getJSONObject(i);
				Iterator it = gjson.keys();
				String key =null;
				String value = null;
				Map map = new HashMap();
				while(it.hasNext()){
					key = (String) it.next();
					value = gjson.getString(key);
					map.put(key, value);
			}
				addrlist.add(map);
		}
		}
		JSONArray bankjson=json.getJSONArray("banklist");
		List<Map> banklist =new ArrayList<Map>();
		if(json!=null&&json.size()>0){
			for (int i = 0; i < bankjson.size(); i++) {
				JSONObject gjson = bankjson.getJSONObject(i);
				Iterator it = gjson.keys();
				String key =null;
				String value = null;
				Map map = new HashMap();
				while(it.hasNext()){
					key = (String) it.next();
					value = gjson.getString(key);
					map.put(key, value);
			}
				banklist.add(map);
		}
		}
		//唯一标识
		String username = json.getString("username");
		String password = json.getString("password");
		if(!"baosteel".equals(username)&&!"123456".equals(password)){
			return "{\"status\":\"error\",\"message\":\"用户名或密码错误\"}";
		}
		// 申请公司
		String pk_corp = json.getString("corp");
		if (StringUtils.isBlank(pk_corp)) {
			return "{\"status\":\"error\",\"message\":\"申请公司不可为空\"}";
		}else{
			try {
				pk_corp=Querycorp(json.getString("corp"));
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"申请公司查询异常\"}";
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"申请公司查询异常\"}";
			}
		}
		// 客商名称
		String custname = StringUtils.deleteWhitespace(json.getString("custname"));
		if (StringUtils.isBlank(custname)) {
			return "{\"status\":\"error\",\"message\":\"客商名称不可为空\"}";
		}
		// 客商简称
		String custshortname = json.getString("custshortname");
		if (StringUtils.isBlank(custshortname)) {
			return "{\"status\":\"error\",\"message\":\"客商简称不可为空\"}";
		} 
		// 客商类型
		Integer custprop = json.getInt("custprop");
		if (custprop==null) {
			return "{\"status\":\"error\",\"message\":\"客商类型不可为空\"}";
		}
		// 客商属性
		Integer cvflag = json.getInt("cvflag");
		if(cvflag==null){
			return "{\"status\":\"error\",\"message\":\"客商属性不可为空\"}";
		}
		// 纳税人登记号
		String taxpayerid = json.getString("taxpayerid");
		if (StringUtils.isBlank(taxpayerid)) {
			return "{\"status\":\"error\",\"message\":\"纳税人登记号不可为空\"}";
		} 
		//所属地区
		String pk_areacl = json.getString("pk_areacl");
		if(StringUtils.isBlank(pk_areacl)){
			return "{\"status\":\"error\",\"message\":\"所属地区不可为空\"}";
		}
		String pk_areacl2 =null;
		try {
			pk_areacl2 =getaddr(pk_areacl);
		} catch (BusinessException e3) {
			e3.printStackTrace();
			return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e3.getMessage()+"\"}";
		}
		if(StringUtils.isBlank(pk_areacl2)){
			return "{\"status\":\"error\",\"message\":\"所属地区错误\"}";
		}
		String address = json.getString("address");
		if(StringUtils.isBlank(address)){
			return "{\"status\":\"error\",\"message\":\"通信地区不可为空\"}";
		}
		String phone = json.getString("phone");
		if(StringUtils.isBlank(phone)){
			return "{\"status\":\"error\",\"message\":\"联系电话不可为空\"}";
		}
		//所属公司
		// 客商编码
		String custcode = json.getString("custcode");
		// 查询是否已有此客商来确定在增删改操作
		// 新增
		//add
		if (StringUtils.isBlank(custcode)) {
			String custcode1 =null;
			try {
				 custcode1 = getcustcode(custname);
			} catch (BusinessException e3) {
				e3.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库操作错误"+e3.getMessage()+"}";
			}
			if(StringUtils.isNotBlank(custcode1)){
				return "{\"status\":\"error\",\"message\":\"客商名重复\",\"bodylist\":[{\"custname\":\""+custname+"\"}]}";
			}
			// 聚合VO
			SteelCustMultiBillVO vo = new SteelCustMultiBillVO();
			// 客商申请表VO
			SteelCubasdocVO hvo = new SteelCubasdocVO();
			hvo.setPk_corp(pk_corp);// 申请公司
			hvo.setCustname(custname);// 客商名称
			hvo.setCustshortname(custshortname);// 客商简称
			hvo.setCustprop(custprop);// 客商类型
			hvo.setPk_areacl(pk_areacl2);//所属地区
			hvo.setTaxpayerid(taxpayerid);// 纳税人登记号
			hvo.setCvflag(cvflag);// 客商属性
			hvo.setConaddr(address);//通信地址
			hvo.setPhone1(phone);//联系电话
			hvo.setAuthflag(1);// 审批状态
			hvo.setFreecustflag(new UFBoolean("N"));
			hvo.setIsconnflag(new UFBoolean("N"));
			hvo.setDrpnodeflag(new UFBoolean("N"));
			// 客商申请表体VO
			SteelCustAddrVO[] bvos = new SteelCustAddrVO[addrlist.size()];
			for (int i = 0; i < addrlist.size(); i++) {
				Map addrmap = addrlist.get(i);
					// 发货地址名称
					String addrname = (String) addrmap.get("addrname");
					//联系人
					String lxrname = (String) addrmap.get("lxrname");
					//联系人电话
					String lxrphone = (String) addrmap.get("lxrphone");
					String pk_address = (String) addrmap.get("pk_address");
					String pk_areacl1 = null;
					try {
						pk_areacl1 = getaddr((String) addrmap.get("pk_areacl"));
					} catch (BusinessException e) {
						e.printStackTrace();
						return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
					}
					if(StringUtils.isBlank(pk_areacl1)){
						return "{\"status\":\"error\",\"message\":\"所属地区错误\"}";
					}
					SteelCustAddrVO bvo = new SteelCustAddrVO();
					bvo.setAddrname(addrname);// 发货地址名称
					bvo.setLinkman(lxrname);
					bvo.setPk_address(pk_address);
					bvo.setPhone(lxrphone);
					bvo.setPk_areacl(pk_areacl1);
					bvo.setDefaddrflag(new UFBoolean("N"));
					bvos[i] = bvo;
			}
			SteelCustBankVO [] banks = new SteelCustBankVO[banklist.size()];
			for (int i = 0; i < banklist.size(); i++) {
				Map bankmap =banklist.get(i);
				String bankname = (String) bankmap.get("bankname");//银行名称
				String bankcode = (String) bankmap.get("bankcode");//银行账号
				String bankaddr = (String) bankmap.get("bankaddr");
				String pk_currtype = null;//币种
				try {
					pk_currtype = getcurrtype((String) bankmap.get("pk_currtype"));
				} catch (BusinessException e) {
					e.printStackTrace();
					return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
				}
				if(StringUtils.isBlank(pk_currtype)){
					return "{\"status\":\"error\",\"message\":\"币种参数错误\"}";
				}
				String bankowner = (String) bankmap.get("bankowner");//银行类型
				SteelCustBankVO bvo = new SteelCustBankVO();
				bvo.setAccount(bankcode);
				bvo.setAccname(bankname);
				bvo.setAccaddr(bankaddr);//地址
				bvo.setPk_currtype(pk_currtype);
				bvo.setBankowner(bankowner);
				bvo.setPk_corp(pk_corp);
				bvo.setDr(0);
				bvo.setDefflag(new UFBoolean("N"));
				banks[i]=bvo;
			}
			vo.setChildrenVO(banks);
			vo.setChildrenVO(bvos);
			vo.setParentVO(hvo);
			vo.setTableVO("BANK",banks);
			vo.setTableVO("ADDR",bvos);
			// 保存前对税号做唯一性验证
			// 税号
			String sh = null;
			try {
				sh = Querysh(taxpayerid);
			} catch (BusinessException e1) {
				e1.printStackTrace();
				return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"纳税人登记号查询出现异常" + e1.toString()+"}";
			}
			if(StringUtils.isNotBlank(sh)){
				return "{\"status\":\"error\",\"message\":\"纳税人登记号重复\"}";
			}
			// 调用客商申请的保存方法
			IUifService iu = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
			SteelCustMultiBillVO  a =null;
			try{
				a =(SteelCustMultiBillVO) iu.saveBD(vo, "nc.ui.cu.ClientUICheckRuleGetter");
			} catch (UifException e2) {
				e2.printStackTrace();
				return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"单据保存失败" + e2.toString()+"}";
			}
			// 客商申请审核
			INcBhItf INcBhItf = (INcBhItf) NCLocator.getInstance().lookup(INcBhItf.class.getName());
			try {
				INcBhItf.insertCust(a);
			} catch (RemoteException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"单据审核失败" + e.getMessage()+"}";
			}
			//查询客商编码
			String custcodes = null;
			try {
				custcodes = Querycode(custname);
			} catch (BusinessException e1) {
				e1.printStackTrace();
				return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"客商编码查询失败\"}";
			}
			JSONArray straddrid = new JSONArray();
			JSONArray strabankid = new JSONArray();
			try {
				jg =getcustcode(custname);
				List addrlists = getcustaddr(jg);
				List banklists = getcustbank(jg);
				if(addrlists!=null){
					for (int i = 0; i < addrlists.size(); i++) {
						JSONObject jsonaddr = new JSONObject();
						Map map=(Map) addrlists.get(i);
						String addrid= (String) map.get("pk_custaddr");
						String addrname = (String) map.get("addrname");
						jsonaddr.put("addrid", addrid);
						jsonaddr.put("addrname", addrname);
						straddrid.add(jsonaddr);
					}
				}
				if(banklists!=null){
					for (int i = 0; i < banklists.size(); i++) {
						JSONObject jsonbank = new JSONObject();
						Map map=(Map) banklists.get(i);
						String bankid= (String) map.get("pk_custbank");
						String bankcode = (String) map.get("account");
						jsonbank.put("bankid", bankid);
						jsonbank.put("bankcode", bankcode);
						strabankid.add(jsonbank);
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
			}
			return "{\"status\":\"success\",\"message\":\"客商申请成功\",\"custcode\":\""+jg+"\",\"addrlist\":"+straddrid+",\"banklist\":"+strabankid+"}";
		}else{
		// 修改
			//根据客商编码获取公司和客商pk
			String custid;
			try {
				custid = getPkcust(custcode);
			} catch (BusinessException e1) {
				e1.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库运行错误" + e1.getMessage()+"}";
			}
			// 修改 客商申请单审核后无法修改 且没有取消审核按钮 因此直接修改客商基本档案
			// 所以需要将之前申请单的聚合vo改为客商基本档案的聚合vo
			if(StringUtils.isBlank(custid)){
				return "{\"status\":\"error\",\"message\":\"客商不存在\"}";
			}
			CubasdocVO cubasdocvo = new CubasdocVO();
			cubasdocvo.getParentVO();
			// 表头vo
			CustBasVO chvo = new CustBasVO();
			chvo.setCustcode(custcode);// 客商编码
			chvo.setPk_corp("0001");// 申请公司
			chvo.setCustname(custname);// 客商名称
			chvo.setCustshortname(custshortname);// 客商简称
			chvo.setCustprop(custprop);// 客商类型
			chvo.setPk_areacl(pk_areacl2);//所属地区
			chvo.setTaxpayerid(taxpayerid);// 纳税人登记号
			chvo.setFreecustflag(new UFBoolean("N"));
			chvo.setIsconnflag(new UFBoolean("N"));
			chvo.setDrpnodeflag(new UFBoolean("N"));
			chvo.setPk_cubasdoc(custid);
			chvo.setPhone1(phone);
			chvo.setConaddr(address);
			chvo.setStatus(1);
			// 表体vo
			CustAddrVO [] bvos = new CustAddrVO[addrlist.size()];
			for (int i = 0; i < addrlist.size(); i++) {
				Map addrmap = addrlist.get(i);
					// 发货地址名称
					String addrname = (String) addrmap.get("addrname");
					//联系人
					String lxrname = (String) addrmap.get("lxrname");
					//联系人电话
					String lxrphone = (String) addrmap.get("lxrphone");
					String pk_address = (String) addrmap.get("pk_address");
					String pk_custaddr = (String) addrmap.get("pk_custaddr");
					if(StringUtils.isBlank(pk_custaddr)){
						return "{\"status\":\"error\",\"message\":\"地址主键不能为空\"}";
					}
					String pk_areacl1 = null;
					try {
						pk_areacl1 = getaddr((String) addrmap.get("pk_areacl"));
					} catch (BusinessException e) {
						e.printStackTrace();
						return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
					}
					if(StringUtils.isBlank(pk_areacl1)){
						return "{\"status\":\"error\",\"message\":\"所属地区错误\"}";
					}
					CustAddrVO bvo = new CustAddrVO();
					bvo.setAddrname(addrname);// 发货地址名称
					bvo.setLinkman(lxrname);
					bvo.setPk_address(pk_address);
					bvo.setPhone(lxrphone);
					bvo.setPk_areacl(pk_areacl1);
					bvo.setPk_custaddr(pk_custaddr);
					bvo.setStatus(1);
					bvo.setPk_cubasdoc(custid);
					bvo.setDefaddrflag(new UFBoolean("N"));
					bvos[i] = bvo;
			}
			CustBankVO [] banks = new CustBankVO[banklist.size()];
			for (int i = 0; i < banklist.size(); i++) {
				Map bankmap =banklist.get(i);
				String bankname = (String) bankmap.get("bankname");//银行名称
				String bankcode = (String) bankmap.get("bankcode");//银行账号
				String bankaddr = (String) bankmap.get("bankaddr");
				String pk_custbank = (String) bankmap.get("pk_custbank");
				String pk_currtype = null;//币种
				try {
					pk_currtype = getcurrtype((String) bankmap.get("pk_currtype"));
				} catch (BusinessException e) {
					e.printStackTrace();
					return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
				}
				if(StringUtils.isBlank(pk_currtype)){
					return "{\"status\":\"error\",\"message\":\"币种参数错误\"}";
				}
				String bankowner = (String) bankmap.get("bankowner");//银行类型
				CustBankVO bvo = new CustBankVO();
				//如果客商银行主键不为空 则不需要放银行编码  Edit 2018年10月15日13:38:44
				if(StringUtils.isBlank(pk_custbank)){
					//新增
					bvo.setAccount(bankcode);
					bvo.setStatus(2);
				}else{
					//修改
					bvo.setStatus(0);
					bvo.setPk_custbank(pk_custbank);
				}
				bvo.setAccname(bankname);
				bvo.setAccaddr(bankaddr);//地址
				bvo.setPk_currtype(pk_currtype);
				bvo.setPk_accbank(queryaccbank(bankcode));
				bvo.setPk_corp(pk_corp);
				bvo.setPk_cubasdoc(custid);
				bvo.setDefflag(new UFBoolean("N"));
				banks[i]=bvo;
			}
			String sss [] = new String [1];
			sss [0] = chvo.getPk_cubasdoc();
			cubasdocvo.setLockpks(sss);
			cubasdocvo.setUserid("0001A21000000006SQT3");
			cubasdocvo.setParentVO(chvo);
			cubasdocvo.setCustAddrVOs(bvos);
			cubasdocvo.setCustBankVOs(banks);
		
			// 客商申请表修改
			ICuBasDocTmp ICuBasDocTmp = (ICuBasDocTmp) NCLocator.getInstance().lookup(ICuBasDocTmp.class.getName());
			try {
				ICuBasDocTmp.update(cubasdocvo);
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"客商编码回写失败'"+ e.getMessage()+"\"}";
			}
			JSONArray straddrid = new JSONArray();
			JSONArray strabankid = new JSONArray();
			try {
				List addrlists = getcustaddr(custcode);
				List banklists = getcustbank(custcode);
				if(addrlists!=null){
					for (int i = 0; i < addrlists.size(); i++) {
						JSONObject jsonaddr = new JSONObject();
						Map map=(Map) addrlists.get(i);
						String addrid= (String) map.get("pk_custaddr");
						String addrname = (String) map.get("addrname");
						jsonaddr.put("addrid", addrid);
						jsonaddr.put("addrname", addrname);
						straddrid.add(jsonaddr);
					}
				}
				if(banklists!=null){
					for (int i = 0; i < banklists.size(); i++) {
						JSONObject jsonbank = new JSONObject();
						Map map=(Map) banklists.get(i);
						String bankid= (String) map.get("pk_custbank");
						String bankcode = (String) map.get("account");
						jsonbank.put("bankid", bankid);
						jsonbank.put("bankcode", bankcode);
						strabankid.add(jsonbank);
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"message\":\"数据库访问错误"+e.getMessage()+"\"}";
			}
			return "{\"status\":\"success\",\"message\":\"客商修改成功\",\"custcode\":\""+custcode+"\",\"addrlist\":"+straddrid+",\"banklist\":"+strabankid+"}";
		}
	}
	//根据客商编码获取公司和客商pk
	private String getPkcust(String custcode) throws BusinessException {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer ssql = new StringBuffer();
		ssql.append("select cub.pk_cubasdoc from bd_cubasdoc cub where cub.custcode ='"+custcode+"' and nvl(dr,0)=0");
		List list = (List) bs.executeQuery(ssql.toString(),
				new MapListProcessor());
		String custid = null;
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 custid= (String) map.get("pk_cubasdoc");
			}
			return custid;
		}
		return null;
	}
	/**
	 * 查询开户银行主键
	 * @param acccode 银行账号
	 * @return
	 */
	private String queryaccbank(String acccode){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql =" select pk_accbank from bd_accbank where bankacc ='"+acccode+"' and nvl(dr,0)=0;";
		String pk_accbank = "";
		try {
			List list = (List) bs.executeQuery(sql, new MapListProcessor());
			if(list.size()>0){
				Map lmap = (Map) list.get(0);
				pk_accbank = lmap.get("pk_accbank").toString();
				return pk_accbank;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private String Querysh(String taxpayerid) throws BusinessException {
		// 对纳税人登记号进行唯一性验证
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		StringBuffer ssql = new StringBuffer();
		ssql.append("select taxpayerid,dr from steel_cubasdoc where taxpayerid ='"
				+ taxpayerid + "'and dr=0");
		List lsi = (List) bs.executeQuery(ssql.toString(),
				new MapListProcessor());
		String sh = "";
		if (lsi.size() > 0) {
			Map lmap = (Map) lsi.get(0);
			sh = lmap.get("taxpayerid").toString();
		}
		return sh;
	}


	private String Querycode(String custname) throws BusinessException {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询客商档案的编码
		StringBuffer ssql = new StringBuffer();
		ssql
				.append("select custname,custcode,dr from bd_cubasdoc where  custname ='"
						+ custname + "'and dr=0");
		List lsi = (List) bs.executeQuery(ssql.toString(),
				new MapListProcessor());
		String custcodes = "";
		if (lsi.size() > 0) {
			Map lmap = (Map) lsi.get(0);
			custcodes = lmap.get("custcode").toString();
		}
		return custcodes;
	}
	
	private String Querycorp(String unitcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询公司代码
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_corp from bd_corp where unitcode ='"+unitcode+"' and dr=0");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		String corp = null;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 corp= (String) map.get("pk_corp");
			}
			return corp;
		}
		return null;
	}
	
	private String getaddr(String areaclcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询地址档案编码
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
	
	private String getcurrtype(String currtypecode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//查询币种代码
		StringBuffer sql = new StringBuffer();
		sql.append("select b.pk_currtype from bd_currtype b where b.currtypecode ='"+currtypecode+"' ");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		String pk_currtype = null;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 pk_currtype= (String) map.get("pk_currtype");
			}
			return pk_currtype;
		}
		return null;
	}
	private String getcustcode(String custcode) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select cub.custcode from bd_cubasdoc cub where cub.custname ='"+custcode+"' and nvl(dr,0)=0");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		String custcodes = null;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				 Map map=(Map) list.get(i);
				 custcodes= (String) map.get("custcode");
			}
			return custcodes;
		}
		return null;
	}
	
	private List getcustaddr(String custaddr) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select addr.pk_custaddr,addr.addrname from bd_custaddr addr left join bd_cubasdoc cub on  addr.pk_cubasdoc = cub.pk_cubasdoc where cub.custcode = '"+custaddr+"' and nvl(addr.dr,0) = 0");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		return list;
	}
	
	private List getcustbank(String custbank) throws BusinessException{
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select bank.pk_custbank,bank.account from bd_custbank bank left join bd_cubasdoc cub on bank.pk_cubasdoc = cub.pk_cubasdoc where cub.custcode ='"+custbank+"' and nvl(bank.dr,0) = 0");
		List list = (List) bs.executeQuery(sql.toString(),
				new MapListProcessor());
		return list;
	}
}
