
package nc.impl.so.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.ct.pub.ManageHeaderVO;
import nc.vo.ct.pub.ManageItemVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.uap.pf.PFBusinessException;

import org.apache.commons.lang.StringUtils;
@SuppressWarnings("all")
public class SoOrderServiceImpl implements IHttpServletAdaptor {

	String pk_cur = "00010000000000000001";//币种
    IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String result = sendSoBill(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	public String sendSoBill(String json){		
		// 设置数据源
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);//设置数据源
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = nc.net.sf.json.JSONArray.fromObject(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式:"+e.getMessage()+"\"}]";
		}
		Map mls = new HashMap();
		List<Map> lsmap = new ArrayList<Map>();//表体数据
		if (jsonarray != null && jsonarray.size() > 0) {
			for (int i = 0; i < jsonarray.size(); i++) {
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while (it.hasNext()) {
					key = (String) it.next();
					value = jsonobject.getString(key);
					if (key.equals("bodylist")) {
						nc.net.sf.json.JSONArray array = jsonobject.getJSONArray(key);
						for (int j = 0; j < array.size(); j++) {
							String key1 = null;
							String value1 = null;
							nc.net.sf.json.JSONObject jb = array.getJSONObject(j);
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
			}
		}
		System.out.println("json解析完成----------");

		String ksid = "";// 名称
		String lb = "";// 编码
		String coperatorid = "";// 编码
		String user = "";//接口连接用户
	    String password = "";//接口连接密码
		
	    String username = mls.get("username")==null?"":mls.get("username").toString();
		if(StringUtils.isNotBlank(username)){
			if(!username.equals("baosteel")){
				return "[{\"status\":\"error\",\"message\":\"用户名错误\"}]";
			}
		}else{
			return "[{\"status\":\"error\",\"message\":\"用户名为空\"}]";
		}
		
		String pwd = mls.get("password")==null?"":mls.get("password").toString();
		if(StringUtils.isNotBlank(pwd)){
			if(!pwd.equals("123456")){
				return "[{\"status\":\"error\",\"message\":\"密码错误\"}]";
			}
		}else{
			return "[{\"status\":\"error\",\"message\":\"密码为空\"}]";
		}
		String corp = mls.get("corp")==null?"":mls.get("corp").toString();//公司
		if(StringUtils.isBlank(corp)){
			return "[{\"status\":\"error\",\"message\":\"公司为空\"}]"; 
		}
		String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+corp+"' and nvl(a.dr,0)=0";
		List corpList = null;
		try {
			corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
			if(corpList == null || corpList.size() == 0){
				return "[{\"status\":\"error\",\"message\":\"公司在NC系统不存在，请检查\"}]"; 
			}
		} catch (BusinessException e4) {
			e4.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\"}]";
		}
		
		
		Map mapcorp = (Map) corpList.get(0);
		String pk_corp = mapcorp.get("pk_corp").toString();
		//销售组织
		String xszz =null;
		if(pk_corp.equals("1016")){
			 xszz = "0001A21000000004EF2F";// 销售组织(上海)
		}else if(pk_corp.equals("1071")){
			 xszz = "0001A21000000006RP9L";// 销售组织(武汉)
		}else if(pk_corp.equals("1103")){
			 xszz=  "0001A2100000000BX7UF";// 销售组织(哈尔滨)
		}else if(pk_corp.equals("1097")){
			xszz = "0001A21000000006NQZQ";//  销售组织(河南)
		}else if(pk_corp.equals("1017")){
			xszz = "0001A21000000006X2F3";//  销售组织(河北)
		}else if(pk_corp.equals("1018")){
			xszz = "0001A21000000007HD39";//  销售组织(成都)
		}else if(pk_corp.equals("1019")){
			xszz = "0001A2100000000B3L0R";//  销售组织(佛山)
		}else if(pk_corp.equals("1107")){
			xszz = "000111100000000LXVVU";//  销售组织(宝翼2)
		}
		// ksid客户（客商）
		Object ksidobj = mls.get("ksid");
		if (ksidobj == null) {
			return "[{\"status\":\"error\",\"message\":\"客户（客商）(ksid)不可为空\"}]";
		} else {
			ksid = ksidobj.toString().trim();
		}


		String usercode = mls.get("usercode")==null?"":mls.get("usercode").toString();//制单人编码
		if(StringUtils.isBlank(usercode)){
			return "[{\"status\":\"error\",\"message\":\"制单人编码为空\"}]";
		}
		String operatid = "";
		try {
			operatid = getUserId(usercode);
		} catch (BusinessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e2.getMessage()+"\"}]";
		}
		if(StringUtils.isBlank(operatid)){
			return "[{\"status\":\"error\",\"message\":\"制单人编码在NC系统不存在，请检查\"}]"; 
		}
		//备注
		 String memo = mls.get("memo")==null?"":mls.get("memo").toString();
		 String zpflag = mls.get("zpflag")==null?"0":mls.get("zpflag").toString();
			
		//校验表体
		for (int i = 0; i < lsmap.size(); i++) {
			Map imp = lsmap.get(i);

			// 存货编码
			Object cinventoryidobj = imp.get("cinventoryid") == null?"":imp.get("cinventoryid").toString();
			if (StringUtils.isBlank(cinventoryidobj.toString())) {
				return "[{\"status\":\"error\",\"message\":\"存货编码(cinventoryid)不可为空\"}]";
			}
			// 出库数量
			Object ckslobj = imp.get("cksl") == null?"":imp.get("cksl").toString();
			if (StringUtils.isBlank(ckslobj.toString())) {
				return "[{\"status\":\"error\",\"message\":\"出库数量(cksl)不可为空\"}]";
			}
			// 发货仓库
			String def13 = imp.get("tsms")==null?"":imp.get("tsms").toString();//途损模式
			String tsbl = imp.get("tsbl")==null?"":imp.get("tsbl").toString();//途损比例
			UFDouble dpzk = new UFDouble(0);//单品折扣
			if(StringUtils.isNotBlank(def13) && StringUtils.isNotBlank(tsbl)){
				if(new UFDouble(tsbl).compareTo(new UFDouble(100))>0){
					return "[{\"status\":\"error\",\"message\":\"途损比例不能大于100\"}]";
				}
				dpzk = new UFDouble(100).sub(new UFDouble(tsbl));//单品折扣
			}
			String sl = imp.get("sl")==null?"":imp.get("sl").toString();
			if(StringUtils.isBlank(sl)){
				return "[{\"status\":\"error\",\"message\":\"税率为空\"}]";
			}
			String price = imp.get("price")==null?"":imp.get("price").toString();
			if(StringUtils.isBlank(price)){
				return "[{\"status\":\"error\",\"message\":\"单价为空\"}]";
			}
			String dhdate = imp.get("dhdate")==null?"":imp.get("dhdate").toString();//到货日期
			if(StringUtils.isBlank(dhdate)){
				return "[{\"status\":\"error\",\"message\":\"到货日期为空\"}]";
			}
			if(new UFDate(dhdate).before(new UFDate(new Date()))){
				return "[{\"status\":\"error\",\"message\":\"到货日期不能小于当前日期\"}]";
			}
		}
		Map headmap = lsmap.get(0);
		String hddhd = headmap.get("dhdd")==null?"":headmap.get("dhdd").toString();
		//客商名称
		HashMap<String, String> cubs  = null;
		try {
			cubs = QueryCubasdoc(ksid,pk_corp);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"客商信息获取异常-"+e1.toString()+"\"}]";
		}
		if(cubs.size()<=0){
			return "[{\"status\":\"error\",\"message\":\"客商编码"+ksid+"在NC系统中未找到或未分配给该公司，请去集团进行分配\"}]";
		}
		//操作人、制单人编码
		String coperators  = "";
		try {
			coperators = QueryUserID(usercode);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			return "[{\"status\":\"error\",\"message\":\"制单人信息获取异常-"+e1.toString()+"\"}]";
		}
		if(StringUtils.isBlank(coperators)){
			return "[{\"status\":\"error\",\"message\":\"制单人编码在NC系统中未找到对应的人员\"}]";
		}
		//销售组织
		String pk_calbody = "";
		try {
			pk_calbody = getCalBodyId(pk_corp);
		} catch (BusinessException e2) {
			// TODO Auto-generated catch block
			return "[{\"status\":\"error\",\"message\":\"数据库操作异常\"}]";
		}
		//部门主键
		String deptid = "";
		try {
			deptid = getDeptId(pk_corp);
		} catch (BusinessException e2) {
			// TODO Auto-generated catch block
			return "[{\"status\":\"error\",\"message\":\"数据库操作异常\"}]";
		}
		String zdsl = mls.get("ninspacenum")==null?"":mls.get("ninspacenum").toString();;//整垛数量
		if(StringUtils.isBlank(zdsl)){
			return "[{\"status\":\"error\",\"message\":\"整垛数量为空\"}]";
		}
		String def8 = mls.get("jhfs")==null?"":mls.get("jhfs").toString();//交货方式
		if(StringUtils.isBlank(def8)){
			return "[{\"status\":\"error\",\"message\":\"交货方式为空\"}]";
		}
		String sendcode = mls.get("sendcode")==null?"":mls.get("sendcode").toString();//发运编码
		String pk_sendtype = "";//发运方式主键
		if(def8.equals("送货")){
			if(StringUtils.isBlank(sendcode)){
				return "[{\"status\":\"error\",\"message\":\"发运方式编码为空\"}]";
			}
			try {
				pk_sendtype = getSendId(sendcode, pk_corp);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e2.getMessage()+"\"}]";
			}
			if(StringUtils.isBlank(pk_sendtype)){
				return "[{\"status\":\"error\",\"message\":\"发运方式在NC系统不存在，请检查\"}]";
			}
		}
		String custbillcode = mls.get("khddh")==null?"":mls.get("khddh").toString();//客户订单号
        SaleOrderVO vo = new SaleOrderVO();
        SaleorderHVO hvo = new SaleorderHVO();
        if(StringUtils.isNotBlank(custbillcode)){
        	hvo.setVreceiptcode(custbillcode);
        }
        //客商
        hvo.setVdef8(def8);//交货方式
        hvo.setCcustomerid(cubs.get("pk_cumandoc"));//客户管理id
        if(def8.equals("送货")){
        	hvo.setCtransmodeid(pk_sendtype);//发运方式
        }else{
        	hvo.setBdeliver(new UFBoolean(false));//是否发运
        }
        hvo.setCsalecorpid(xszz);//销售组织
        hvo.setCcalbodyid(pk_calbody);//库存组织
        
        hvo.setCdeptid(deptid);//部门
        
        hvo.setCbiztype(getcbiztype(pk_corp));//业务流程
        hvo.setCcurrencytypeid(pk_cur);//币种
        hvo.setCoperatorid(coperators);//制单人
        
        hvo.setCreceiptcorpid(cubs.get("pk_cumandoc"));//收货单位=客户
        hvo.setCreceiptcustomerid(cubs.get("pk_cumandoc"));//开票单位=客户
        if(StringUtils.isBlank(hddhd)){
        	hvo.setVreceiveaddress(cubs.get("addrname")==null?"":cubs.get("addrname").toString());//收货地址
        }else{
        	String addressname = "";
        	try {
				addressname = getAddrName(hddhd, pk_corp);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\"}]";
			}
			if(StringUtils.isBlank(addressname)){
				return "[{\"status\":\"error\",\"message\":\"到货地点在NC系统中未找，请检查\"}]";
			}
			hvo.setVreceiveaddress(addressname);
        }
        //收付款协议
        if(pk_corp.equals("1016")){
        	hvo.setCtermprotocolid("1016A210000000033ST5");//收款协议(上海)
        }else if(pk_corp.equals("1071")){
        	hvo.setCtermprotocolid("1071A21000000000YA9O");//收款协议(武汉)
        }else if(pk_corp.equals("1103")){
        	hvo.setCtermprotocolid("1103A2100000000042W7");//收款协议(哈尔滨)
        }else if(pk_corp.equals("1097")){
        	hvo.setCtermprotocolid("1097A21000000005TXFB");//收款协议(河南)
        }else if(pk_corp.equals("1017")){
        	hvo.setCtermprotocolid("1017A210000000015QS9");//收款协议(河北)
        }else if(pk_corp.equals("1018")){
        	hvo.setCtermprotocolid("1018A210000000054YTG");//收款协议(成都)
        }else if(pk_corp.equals("1019")){
        	hvo.setCtermprotocolid("1019A210000000017WWZ");//收款协议(佛山)
        }else if(pk_corp.equals("1107")){
        	hvo.setCtermprotocolid("11071110000000000PYB");//收款协议(宝翼2)
        }
        hvo.setVdef11(zdsl);//整垛数量    
        hvo.setStatus(2);
        hvo.setFstatus(1);
        hvo.setDbilldate(new UFDate(new Date()));
        hvo.setPk_corp(pk_corp);
        hvo.setCreceipttype("30");//单据类型
        hvo.setDmakedate(new UFDate(new Date()));
        hvo.setNdiscountrate(new UFDouble(100.0));//整单折扣
        hvo.setVnote(memo);
        
        SaleorderBVO[] bvos = new SaleorderBVO[lsmap.size()]; 
        for (int i = 0; i < lsmap.size(); i++) {
        	Map mis = lsmap.get(i);
        	String cinventoryid = mis.get("cinventoryid").toString().trim();//存货编码
			String cksl_s = mis.get("cksl").toString().trim();
			UFDouble cksl = new UFDouble(cksl_s);
			//仓库
			String cwarehouseids = "";
			String fhck=mis.get("cbodywarehouseid").toString().trim();
			try {
				if(StringUtils.isBlank(fhck)){
					fhck = "B01";//如果仓库为空，则默认本仓库
				}
				cwarehouseids = QueryStordoc(fhck,pk_corp);
			} catch (BusinessException e1) {
				//e1.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"仓库获取异常-"+e1.toString()+"\"}]";
			}
			if(StringUtils.isBlank(cwarehouseids)){
				return "[{\"status\":\"error\",\"message\":\"仓库在NC系统中未找到对应的仓库\"}]";
			}
		   
        	//存货
			HashMap<String, String> invm  = null;
			try {
				invm = QueryInv(cinventoryid,pk_corp);
			} catch (BusinessException e1) {
				e1.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"存货信息获取异常-"+e1.toString()+"\"}]";
			}
			if(invm.size()<=0){
				return "[{\"status\":\"error\",\"message\":\"存货编码"+cinventoryid+"在NC系统中未找到或未分配给该公司，请去集团分配\"}]";
			}
			
			String def13 = mis.get("tsms")==null?"":mis.get("tsms").toString();//途损模式
			String tsbl = mis.get("tsbl")==null?"":mis.get("tsbl").toString();//途损比例
			UFDouble dpzk = new UFDouble(0);//单品折扣
			if(StringUtils.isNotBlank(def13) && StringUtils.isNotBlank(tsbl)){
				if(new UFDouble(tsbl).compareTo(new UFDouble(100))>0){
					return "[{\"status\":\"error\",\"message\":\"途损比例不能大于100\"}]";
				}
				dpzk = new UFDouble(100).sub(new UFDouble(tsbl));//单品折扣
			}
			
			String dhdate = mis.get("dhdate")==null?"":mis.get("dhdate").toString();//到货日期
			String sl = mis.get("sl")==null?"":mis.get("sl").toString();//税率
			String price = mis.get("price")==null?"":mis.get("price").toString();//单价
			String rowmemo = mis.get("rowmemo")==null?"":mis.get("rowmemo").toString();//备注
			String fhdd = mis.get("fhdd")==null?"":mis.get("fhdd").toString();//发货地点
			String dhdd = mis.get("dhdd")==null?"":mis.get("dhdd").toString();//到货地点
			String pk_areacl1 = "";
			String pk_areacl = "";
			String addrname = "";
			if(def8.equals("送货")){
				if(StringUtils.isBlank(dhdd)){
					return "[{\"status\":\"error\",\"message\":\"发运方式为送货时到货地点不能为空\"}]";
				}
				if(StringUtils.isBlank(fhdd)){
					return "[{\"status\":\"error\",\"message\":\"发运方式为送货时发货地点不能为空\"}]";
				}
				try {
					pk_areacl1 = getAreaclID(fhdd, pk_corp);
				} catch (BusinessException e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
				}
				if(StringUtils.isBlank(pk_areacl1)){
					return "[{\"status\":\"error\",\"message\":\"发货地点在NC系统中未找，请检查\"}]";
				}
				try {
					pk_areacl = getAreaclID(dhdd, pk_corp);
				} catch (BusinessException e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
				}
				if(StringUtils.isBlank(pk_areacl)){
					return "[{\"status\":\"error\",\"message\":\"到货地点在NC系统中未找，请检查\"}]";
				}
				try {
					addrname = getAddrName(dhdd, pk_corp);
				} catch (BusinessException e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
				}
			}else{
				if(StringUtils.isNotBlank(dhdd)){
					try {
						pk_areacl = getAreaclID(dhdd, pk_corp);
					} catch (BusinessException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
					}
					if(StringUtils.isBlank(pk_areacl)){
						return "[{\"status\":\"error\",\"message\":\"到货地点在NC系统中未找，请检查\"}]";
					}
					try {
						addrname = getAddrName(dhdd, pk_corp);
					} catch (BusinessException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
					}
				}
				if(StringUtils.isNotBlank(fhdd)){
					try {
						pk_areacl1 = getAreaclID(fhdd, pk_corp);
					} catch (BusinessException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"数据库操作异常,请稍后重试\"}]";
					}
					if(StringUtils.isBlank(pk_areacl1)){
						return "[{\"status\":\"error\",\"message\":\"发货地点在NC系统中未找，请检查\"}]";
					}
				}
			}
			SaleorderBVO bvo = new SaleorderBVO();
			
			//edit by src 2016-8-19 
			bvo.setPkcorp(pk_corp);
			bvo.setFrowstatus(1);
			//end by src
			bvo.setStatus(2);
			bvo.setCrowno(i+1+"0");//行号
			bvo.setCinventoryid(invm.get("pk_invmandoc"));
			bvo.setCinvbasdocid(invm.get("pk_invbasdoc"));
			bvo.setTsbl(new UFDouble(tsbl));//途损比例
			bvo.setNitemdiscountrate(dpzk);//单品折扣
			bvo.setVdef13(def13);//途损模式
			if(zpflag.equals("0")){
				bvo.setBlargessflag(new UFBoolean(false));
			}else if(zpflag.equals("1")){
				bvo.setBlargessflag(new UFBoolean(true));
			}else{
				bvo.setBlargessflag(new UFBoolean(false));
			}
			bvo.setFrownote(rowmemo);//备注
			bvo.setCbodywarehouseid(cwarehouseids);//发货仓库
			bvo.setFinished(new UFBoolean(false));
			bvo.setBifreceiptfinish(new UFBoolean(false));
			bvo.setBifinventoryfinish(new UFBoolean(false));
			bvo.setBifinvoicefinish(new UFBoolean(false));
			//add by src 2016-8-30 添加主计量单位，批次状态
			bvo.setCunitid(invm.get("cunitid"));
			bvo.setFbatchstatus(0); 
			UFDouble tax =  new UFDouble(sl.toString());//税率
			bvo.setNnumber(cksl);//数量
			bvo.setNquoteunitnum(cksl);//报价单位数量
			bvo.setDconsigndate(new UFDate(new Date()));//计划发货时间
			bvo.setDdeliverdate(new UFDate(dhdate));//要求收货日期
			bvo.setCcurrencytypeid(pk_cur);
			bvo.setCconsigncorpid(pk_corp);//发货公司
			
			//ntaxrate
			UFDouble nprice = new UFDouble(price);
			bvo.setNtaxrate(tax);//税率
			bvo.setNoriginalcurtaxprice(nprice);//含税单价
			UFDouble noriginalcurdiscountmny  =  new UFDouble(cksl.multiply(nprice).multiply(new UFDouble(tsbl).div(100)));
			bvo.setNoriginalcurdiscountmny(noriginalcurdiscountmny);
			UFDouble jshj = cksl.multiply(nprice).sub(noriginalcurdiscountmny);
			bvo.setNoriginalcursummny(jshj);//价税合计
			bvo.setNsummny(jshj);//本币价税合计
			bvo.setNoriginalcurtaxnetprice(nprice);//含税净价
			//不含税价=含税价/(1+17%)
			UFDouble shui = tax.div(100);
			UFDouble sh1 = shui.add(1);
			
			UFDouble noriginalcurprice = new UFDouble(nprice.div(sh1).toDouble(),2);//无税单价
			
			bvo.setNoriginalcurprice(noriginalcurprice);
			bvo.setNoriginalcurnetprice(noriginalcurprice);//无税净价
			UFDouble wsje  = new UFDouble(noriginalcurprice.multiply(cksl).sub(noriginalcurdiscountmny).toDouble(),2);
			bvo.setNoriginalcurmny(wsje);//无税金额
			bvo.setNmny(wsje);//本币无税金额
			bvo.setNdiscountrate(new UFDouble(100.0));//整单折扣
			
			bvo.setNexchangeotobrate(new UFDouble(1));//折本汇率
			bvo.setCadvisecalbodyid(pk_calbody);//建议发货库存组织
			
			UFDouble se = jshj.sub(wsje);
			bvo.setNtaxmny(se);//本币税额
			bvo.setNoriginalcurtaxmny(se);//原币税额
			bvo.setNorgqtnetprc(noriginalcurprice);//报价单位无税净价
			bvo.setNorgqttaxnetprc(nprice);//报价单位含税净价
			bvo.setCcurrencytypeid(pk_cur);//币种
			//add by src 2016-7-5
		    bvo.setCreceiptcorpid(cubs.get("pk_cumandoc"));//收货单位=客户)
		    //bvo.setVreceiveaddress(cubs.get("addrname")==null?"":cubs.get("addrname").toString());//收货地址（客商地址）
		    bvo.setVreceiveaddress(addrname);//收货地点名称
	    	bvo.setCrecaddrnode(dhdd);//收货地点（地点档案）
	    	bvo.setCreceiptareaid(pk_areacl);//到货地区
		    bvo.setBoosflag(new UFBoolean(false));
		    //折扣金额结算 = 数量*无税单价*(1-(单品折扣/100)) 彭佳佳 2018年11月29日15:23:51
		   
		    bvo = fillBodyInfo(bvo);
        	bvos[i]=bvo;  
		}
        vo.setParentVO(hvo);
        vo.setChildrenVO(bvos);
        vo.setIAction(0);
        vo.setBillTypeCode("30");
        vo.setIsDel7D(false);
        vo.setStatus(2);
        vo.setFirstTime(true);
        vo.setCheckCredit(true);
        vo.setCheckPeriod(true);
        vo.setBCheckATP(true);
        ArrayList<HashMap<String,String>> okbillls = new ArrayList<HashMap<String,String>>(); 
        ArrayList arrReturnFromBs = null;
		try {
			InvocationInfoProxy.getInstance().setUserCode(coperators);
			InvocationInfoProxy.getInstance().setCorpCode(pk_corp);
			arrReturnFromBs = (ArrayList) new PfUtilBO().processAction("PreKeep", "30", new UFDate(new Date()).toString(), null, vo, null);
		} catch (Exception e) {
			//e.printStackTrace();
			//判断单据号是否重复
			if(e.getMessage().contains("java.sql.SQLException")){
				return "[{\"status\":\"error\",\"message\":\"您提供的单据编码不唯一，系统无法保存;\"}]";
			}
			return "[{\"status\":\"error\",\"message\":\"单据保存失败-"+e.getMessage()+"\"}]";
		}
		// 得到主键
        if (arrReturnFromBs == null || arrReturnFromBs.size() == 0) {
          return "[{\"status\":\"error\",\"message\":\"单据保存失败\"}]";
        }else{
        	HashMap<String,String> bill_1 = new HashMap<String, String>();
        	bill_1.put("billname", "销售订单");
        	bill_1.put("vbillcode", arrReturnFromBs.get(1).toString());
        	bill_1.put("hpk", arrReturnFromBs.get(0).toString());
        	okbillls.add(bill_1);
        	SaleOrderVO aggvo = new SaleOrderVO();
			try {
				aggvo = getAggVO(arrReturnFromBs.get(1).toString(), pk_corp);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e1.getMessage()+"\"}]";
			}
			if(aggvo == null){
				return "[{\"status\":\"error\",\"message\":\"销售订单"+arrReturnFromBs.get(1).toString()+"在NC系统不存在或已被删除\"}]";
			}
        	ArrayList approveList = null;
        	try {
    			InvocationInfoProxy.getInstance().setUserCode(coperators);
    			InvocationInfoProxy.getInstance().setCorpCode(pk_corp);
    			approveList = (ArrayList) new PfUtilBO().processAction("APPROVE", "30", new UFDate(new Date()).toString(), null, aggvo, null);
    			//释放订单审核保护锁  edit 2018年12月12日09:42:44
    			PKLock lock = PKLock.getInstance();
    			lock.releaseDynamicLocks();
        	} catch (Exception e) {
    			e.printStackTrace();
    			return "[{\"status\":\"error\",\"message\":\"单据审核失败-"+e.getMessage()+"\"}]";
    		}
	        return "[{\"status\":\"success\",\"billcode\":\""+arrReturnFromBs.get(1).toString()+"\",\"message\":\"单据保存成功\"}]";
        }
	}
	
	public SaleOrderVO getAggVO (String billcode,String corp) throws BusinessException{
		SaleOrderVO aggvo = new SaleOrderVO();
		StringBuffer sbh = new StringBuffer();
		StringBuffer sbb = new StringBuffer();
		sbh.append("select * from so_sale a where a.vreceiptcode='"+billcode+"' and nvl(a.dr,0)=0 and a.pk_corp='"+corp+"'");
		List<SaleorderHVO> listhvo = null;
		List<SaleorderBVO> listbvo = null;
		listhvo = (List<SaleorderHVO>) uap.executeQuery(sbh.toString(), new BeanListProcessor(SaleorderHVO.class));
		if(listhvo != null&&listhvo.size()>0){
			String csaleid = listhvo.get(0).getCsaleid();
			sbb.append("select * from so_saleorder_b b where b.csaleid ='"+csaleid+"' and nvl(b.dr,0)=0");
			listbvo = (List<SaleorderBVO>) uap.executeQuery(sbb.toString(), new BeanListProcessor(SaleorderBVO.class));
			if(listbvo != null&&listbvo.size()>0){
				SaleorderHVO hvo = listhvo.get(0);
				SaleorderBVO [] bvo = new SaleorderBVO[listbvo.size()];
				bvo=listbvo.toArray(new SaleorderBVO[listbvo.size()]);
				aggvo.setParentVO(hvo);
				aggvo.setChildrenVO(bvo);
				return aggvo;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	private String QueryUserID(String psnname) throws BusinessException {
		
		StringBuffer psql = new StringBuffer();
		psql.append(" select cuserid ") 
		.append("   from sm_user ") 
		.append("  where nvl(dr, 0) = 0 ") 
		.append("    and user_code = '"+psnname+"' ");
		List lsi = (List) uap.executeQuery(psql.toString(), new MapListProcessor());
		String cuserid ="";
		if(lsi.size()>0){
			Map mid  = (Map) lsi.get(0);
			cuserid = mid.get("cuserid").toString();
		}
		return cuserid;
	}
	
	//查询存货信息
	private HashMap QueryInv(String invcode,String pkcorp) throws BusinessException {
		
		HashMap<String, String> invs=new HashMap<String, String>();
		
		StringBuffer isql = new StringBuffer();
		isql.append(" select inv.invname, inv.invspec,inv.assistunit, man.pk_invbasdoc, man.pk_invmandoc,inv.pk_measdoc as cunitid,  ") 
		.append(" 		 	con.pk_measdoc,con.mainmeasrate,man.refsaleprice,tax.taxratio,inv.def1,man.prodarea  ") //edit by src   2016-8-8添加注册证和产地
		.append(" 		 	from bd_invbasdoc inv       ") 
		.append(" 		 	left join bd_invmandoc man         ") 
		.append(" 		 	on inv.pk_invbasdoc = man.pk_invbasdoc      ") 
		.append(" 		 	left join bd_convert con   ") 
		.append(" 		 	on inv.pk_invbasdoc = con.pk_invbasdoc  ") 
		.append(" 		 	left join bd_taxitems tax ") 
		.append(" 		 	on inv.pk_taxitems = tax.pk_taxitems ") 
		.append(" 		 	where nvl(inv.dr, 0) = 0        ") 
		.append(" 		 	and nvl(man.dr,0) = 0   ") 
		.append(" 		 	and nvl(con.dr,0) = 0   ") 
		.append(" 	        and man.pk_corp = '"+pkcorp+"'      ") 
		.append(" 	        and invcode = '"+invcode+"'  ");
		
		List lsi = (List) uap.executeQuery(isql.toString(), new MapListProcessor());
		
		if(lsi.size()>0){
			invs  =  (HashMap<String, String>) lsi.get(0);
		}
		return invs;
		
	}
	
	 
	//查询仓库
	private String QueryStordoc(String ckbm,String pkcorp) throws BusinessException {
		StringBuffer ssql = new StringBuffer();
		ssql.append(" select pk_stordoc,storname,pk_calbody  ") 
		.append(" 	from bd_stordoc  ") 
		.append(" 	where nvl(dr,0)=0  ") 
		.append(" 	and pk_corp = '"+pkcorp+"'  ") 
		.append(" 	and isatpaffected = 'Y' ") 
		.append(" 	and storcode = '"+ckbm+"' ") ;		
		List lsi = (List) uap.executeQuery(ssql.toString(), new MapListProcessor());
		String pk_stordoc = "";
		if(lsi.size()>0){
			Map lmap  = (Map) lsi.get(0);
			pk_stordoc = lmap.get("pk_stordoc").toString();
		}
		return pk_stordoc;
	}
	
	//查询客商
	private HashMap<String, String> QueryCubasdoc(String custcode,String pk_corp) throws BusinessException {
		HashMap<String, String> cubs = new HashMap<String, String>();
		StringBuffer ksql = new StringBuffer();
		ksql.append(" select b.pk_cumandoc,a.custname,a.pk_cubasdoc,c.addrname from  bd_cubasdoc a ") 
		.append("  left join bd_cumandoc b on a.pk_cubasdoc = b.pk_cubasdoc ")
		.append("  left join bd_custaddr c on a.pk_cubasdoc = c.pk_cubasdoc")
		.append("  where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
		.append("  and (b.custflag='0' or b.custflag='1' or b.custflag='2') ") 
		.append("  and (a.pk_corp='0001' and b.pk_corp='"+pk_corp+"') ") 
		.append("  and a.custcode='"+custcode+"' ");
		List lsi = (List) uap.executeQuery(ksql.toString(),new MapListProcessor());
		if (lsi.size() > 0) {
			cubs = (HashMap<String, String>) lsi.get(0);
		}
		return cubs;
	}


	//查询类别
	private HashMap<String,String> QueryDefdoc(String lb,String pk_corp) throws BusinessException {
		 
		StringBuffer ksql = new StringBuffer();
		ksql.append(" select doccode, docname, pk_defdoc, pk_defdoc1 ") 
		.append("   from bd_defdoc ") 
		.append("  where (pk_defdoclist = '0001211000000000GZQF' and ") 
		.append("        (pk_corp = '0001' or pk_corp = '"+pk_corp+"')) ") 
		.append("    and (sealflag is null or sealflag <> 'Y') ") 
		.append(" and doccode = '"+lb+"' ")  
		.append("  order by doccode ") ;

		List lsi = (List) uap.executeQuery(ksql.toString(),new MapListProcessor());
		
		HashMap<String, String> lbs = new HashMap<String,String>();
		if (lsi.size() > 0) {
			lbs  = (HashMap<String, String>) lsi.get(0);
		}
		return lbs;
	}
	
	/**
	 * 根据存货id+批次获得生产日期
	 * @param invcode
	 * @return
	 * @throws DAOException
	 */
	public HashMap getInvDate(String pk_invbasdoc,String pk_corp) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select bat.pk_invbasdoc, ") 
		.append("        bat.dproducedate, ") 
		.append("        bat.dvalidate, ") 
		.append(" 	   bat.vbatchcode ") 
		.append("   from scm_batchcode bat ") 
		.append("   left join bd_invbasdoc bas ") 
		.append("   on bas.pk_invbasdoc = bat.pk_invbasdoc ") 
		.append("  where 1 = 1  ") 
		.append("    and bas.pk_invbasdoc in ('"+pk_invbasdoc+"') ") 
		.append("    and bas.pk_corp = '"+pk_corp+"' ") 
		.append("    and nvl(bat.dr,0) = 0 ") 
		.append("    and nvl(bas.dr,0) = 0 ") ;
		
		List lsi = (List) uap.executeQuery(sql.toString(),new MapListProcessor());
		HashMap invmap = new HashMap();
		if (lsi.size() > 0) {
			for(int i = 0;i<lsi.size();i++){
				Map addrMap = (Map) lsi.get(i);  
				String pkinv = addrMap.get("pk_invbasdoc").toString();
				String vbat = addrMap.get("vbatchcode").toString();
				String dproducedate = addrMap.get("dproducedate").toString();
				invmap.put(pkinv+vbat, dproducedate);
			}
		}
		return invmap;
	}
	
	
	/**
	 * 查询单价：根据价格维护表取得
	 * @throws DAOException 
	 */
	public HashMap getInvPrice(String ksid,String invbasid,String pk_corp) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT nprice0,cinvbasdocid FROM prm_tariffcurlist  ") 
		.append(" where cinvbasdocid in ('"+invbasid+"')  ") 
		.append(" and ccurrencyid = '00010000000000000001'    ") 
		.append(" and pk_corp = '"+pk_corp+"' ") 
		.append(" and ( ccubasdocid in ('"+ksid+"') or ccubasdocid = '#123456789*123456789' )   ") 
		.append(" and ( csaleorganid = '0001211000000000FZII' or csaleorganid = '#123456789*123456789' )   ") 
		.append(" and nvl(dr,0) = 0 ") 
		.append(" order by caskunicode desc , ts desc ") ;

		List lsi = (List) uap.executeQuery(sql.toString(), new MapListProcessor());
		UFDouble price = new UFDouble(0.0);
		HashMap pricemap = new HashMap();
		if (lsi.size() > 0) {
			for(int i = 0;i<lsi.size();i++){
				Map addrMap = (Map) lsi.get(i);  
				UFDouble nprice = addrMap.get("nprice0")==null?new UFDouble(0.1):new UFDouble(addrMap.get("nprice0").toString());
				String cinvbasdocid = addrMap.get("cinvbasdocid").toString(); 
				pricemap.put(cinvbasdocid, nprice);
			}
		} 
		return pricemap;
	}
	
	//add by src 2016-8-10 查询公司档案
	private HashMap<String,String> QueryCorpdoc(String corp) throws BusinessException {
		 
		StringBuffer ksql = new StringBuffer();
		ksql.append(" select pk_corp ") 
		.append("   from bd_corp ") 
		.append("  where  pk_corp = '"+corp+"' ")
		.append(" and nvl(dr,0) = 0 "); 
		List lsi = (List) uap.executeQuery(ksql.toString(),new MapListProcessor());
		
		HashMap<String, String> lbs = new HashMap<String,String>();
		if (lsi.size() > 0) {
			lbs  = (HashMap<String, String>) lsi.get(0);
		}
		return lbs;
	}
	//end by src 
	
	//查询整剁数量 by src 2017年11月13日13:44:57
	private String queryZDNum(String pk_cubasdoc) throws BusinessException{
		
		String sql = "SELECT bd_cubasdoc.def5  FROM bd_cubasdoc WHERE bd_cubasdoc.pk_cubasdoc = '"+pk_cubasdoc+"'";
		List lsi = (List) uap.executeQuery(sql, new MapListProcessor());
		String vdef5 = "";
		if(lsi.size()>0){
			Map map = (Map) lsi.get(0);
			vdef5 = map.get("def5")==null?"":map.get("def5").toString();
		}
		return vdef5;
	}
	
	/**
   	 * 根据公司主键获取库存组织主键
   	 * by src 2017-12-12 11:45:11
	 * @throws BusinessException 
   	 */
   	public String getCalBodyId(String corp) throws BusinessException{
   		String pk_calbody = "";
   		String sql = "select pk_calbody from bd_calbody where pk_corp = '"+corp+"' and nvl(dr,0)=0 ";
		pk_calbody = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_calbody;
   	} 
	
	//add by src 2016-9-4 补全表体信息
	private SaleorderBVO fillBodyInfo(SaleorderBVO bvo){ 
        UFDouble nprice = bvo.getNoriginalcurtaxprice()==null?new UFDouble(0.0):bvo.getNoriginalcurtaxprice();//0.1
        UFDouble num = bvo.getNnumber()==null?new UFDouble(0.0):bvo.getNnumber();
        UFDouble tsbl = bvo.getTsbl()==null?new UFDouble(0.0):bvo.getTsbl();
        String cunitid =  bvo.getCunitid()==null?"":bvo.getCunitid();
        bvo.setCquoteunitid(cunitid); 
        bvo.setTconsigntime("00:00:00");
        bvo.setTdelivertime("00:00:00");
        bvo.setNdiscountmny(new UFDouble(0.0));
        bvo.setNdiscountrate(new UFDouble(100.0)); 
       // bvo.setNitemdiscountrate(new UFDouble(100.0)); 
        //折扣金额结算 = 数量*无税单价*(1-(单品折扣/100)) 彭佳佳 2018年11月29日15:23:51
        UFDouble noriginalcurdiscountmny  =  new UFDouble(num.multiply(nprice).multiply(new UFDouble(tsbl).div(100)));
	    bvo.setNoriginalcurdiscountmny(noriginalcurdiscountmny);
        UFDouble jshj = num.multiply(nprice).sub(noriginalcurdiscountmny);
        
        bvo.setNoriginalcursummny(jshj);//价税合计
        bvo.setNsummny(jshj);//本币价税合计 

        UFDouble pricenew = nprice.div(bvo.getNtaxrate().div(100).add(1));//0.085
        UFDouble sumnum = pricenew.multiply(num).sub(noriginalcurdiscountmny);//0.085*num
        BigDecimal valuedouble = new BigDecimal(sumnum.doubleValue()).setScale(2, RoundingMode.UP);
        UFDouble mny = new UFDouble(valuedouble.doubleValue());
//        UFDouble senew = jshj.multiply(bvo.getNtaxrate()).div(100).div(bvo.getNtaxrate().div(100).add(1));//edit by src 2016-8-30 noriginalcurtaxmny->noriginalcursummny*ntaxrate/100.0/(1.0+ntaxrate/100.0) 
        UFDouble senew = jshj.sub(mny);
        
        
        bvo.setNtaxmny(senew);//本币税额 0.4
        bvo.setNoriginalcurtaxmny(senew);//原币税额
        bvo.setNnetprice(pricenew); 
        bvo.setNorgqtnetprc(pricenew);  
        bvo.setNorgqtprc(pricenew);   
        bvo.setNorgqttaxprc(pricenew);
        bvo.setNorgqtprc(pricenew); 
        bvo.setNorgqttaxnetprc(nprice);
        bvo.setNorgqttaxprc(nprice); 
        //bvo.setNoriginalcurdiscountmny(new UFDouble(0.0));
        
        bvo.setNmny(mny);//本币无税金额
        bvo.setNoriginalcurmny(mny);//无税金额
        bvo.setNoriginalcurnetprice(pricenew); 
        bvo.setNoriginalcurprice(pricenew);   
        bvo.setNoriginalcursummny(num.multiply(nprice).sub(noriginalcurdiscountmny));//0.1*num 价税合计
        bvo.setNoriginalcurtaxmny(senew);//原币税额
        bvo.setNoriginalcurtaxnetprice(nprice);
        bvo.setNoriginalcurtaxprice(nprice);
        bvo.setNprice(pricenew);
        bvo.setNqtnetprc(pricenew);
        bvo.setNqtprc(pricenew);
        bvo.setNqttaxnetprc(nprice);
        bvo.setNqttaxprc(nprice);
        bvo.setNquoteunitnum(num);
        bvo.setNreturntaxrate(new UFDouble(0.0)); 
        bvo.setNsummny(num.multiply(nprice));//价税合计
        bvo.setNoriginalcurtaxnetprice(nprice);
        bvo.setNoriginalcurtaxprice(nprice);
        bvo.setNprice(pricenew);
        bvo.setNqtnetprc(pricenew);
        bvo.setNqtprc(pricenew);
        bvo.setNqttaxnetprc(nprice);
        bvo.setNqttaxprc(nprice);
        bvo.setNquoteunitnum(num);
        //bvo.setNquoteunitrate(new UFDouble(1.0));
        bvo.setNreturntaxrate(new UFDouble(0.0)); 
        bvo.setNsummny(jshj);
        bvo.setNtaxmny(senew);//本币税额
        bvo.setNtaxnetprice(nprice);
        bvo.setNtaxprice(nprice); 
        String flag = (bvo.getCpricecalproc()==null?"":bvo.getCpricecalproc()) ;
        if(flag.length()>0){
        	bvo.setNqtorgtaxprc(nprice);  
        }
        bvo.setExets(new UFDateTime(new Date()));
	
       /* 
        UFDouble jshj = num.multiply(nprice);
        bvo.setNoriginalcursummny(jshj);//价税合计
        bvo.setNsummny(jshj);//本币价税合计 
        
        bvo.setNmny(bvo.getNoriginalcurnetprice()); 
        bvo.setNoriginalcurmny(bvo.getNoriginalcurnetprice());
        
        
        bvo.setNorgqtnetprc(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNoriginalcurnetprice(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNoriginalcurprice(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNprice(jshj.sub(bvo.getNoriginalcurtaxmny()));
        
        bvo.setNorgqtprc(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNoriginalcurmny(jshj.sub(bvo.getNoriginalcurtaxmny()));//无税金额noriginalcurmny->noriginalcursummny-noriginalcurtaxmny 
        bvo.setNmny(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNqtnetprc(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNqtprc(jshj.sub(bvo.getNoriginalcurtaxmny()));
        bvo.setNreturntaxrate(new UFDouble(0.0));*/
        
        /*bvo.setNoriginalcurdiscountmny(num.multiply(bvo.getNoriginalcurtaxprice()).sub(bvo.getNoriginalcursummny()));//原币折扣额
        bvo.setNtaxnetprice(bvo.getNoriginalcurtaxnetprice());
        bvo.setNtaxprice(bvo.getNoriginalcurtaxprice());
        bvo.setNprice(bvo.getNoriginalcurprice());
        bvo.setNmny(bvo.getNoriginalcurnetprice()); */
		return bvo;
	}
	/**
	 * 组装销售合同聚合VO
	 * by src 2017年12月13日14:21:56
	 * @throws BusinessException 
	 */
	public ManageVO getManageVO(String billcode,String corp ) throws BusinessException{
		ManageVO aggvo = new ManageVO();
		StringBuffer sbh = new StringBuffer();
		StringBuffer sbb = new StringBuffer();
		sbh.append("select * from ct_manage a where a.ct_code='"+billcode+"' and nvl(a.dr,0)=0 and a.pk_corp='"+corp+"'");
		List<ManageHeaderVO> listhvo = null;
		List<ManageItemVO> listbvo = null;
		listhvo = (List<ManageHeaderVO>) uap.executeQuery(sbh.toString(), new BeanListProcessor(ManageHeaderVO.class));
		ManageHeaderVO hvo = listhvo.get(0);
		String hid = hvo.getPk_ct_manage();
		sbb.append("select * from ct_manage_b b where b.pk_ct_manage ='"+hid+"' and nvl(b.dr,0)=0");
		listbvo = (List<ManageItemVO>) uap.executeQuery(sbb.toString(), new BeanListProcessor(ManageItemVO.class));
		ManageItemVO [] bvo = new ManageItemVO[listbvo.size()];
		bvo=listbvo.toArray(new ManageItemVO[listbvo.size()]);
		aggvo.setParentVO(hvo);
		aggvo.setChildrenVO(bvo);
		return aggvo;
	}
	
	/**
   	 *根据制单人编码获取制单人主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getUserId(String usercode) throws BusinessException{
   		String operateid = "";
   		String sql = "select cuserid from sm_user where user_code = '"+usercode+"' and nvl(dr,0)=0";
		operateid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return operateid;
   	}
   	/**
   	 *根据合同编码获取合同主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getCtTypeId(String cttypecode,String pk_corp) throws BusinessException{
   		String pk_ct_type = "";
   		String sql = "select pk_ct_type  from ct_type a where a. typecode = '"+cttypecode+"' and nvl(a.dr,0)=0 and pk_corp ='"+pk_corp+"'";
   		pk_ct_type = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_ct_type;
   	}
   	/**
   	 *根据收付款协议编码获取收付款协议主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getPayTermId(String paytermcode,String pk_corp) throws BusinessException{
   		String pk_payterm = "";
   		String sql = "select a.pk_payterm from bd_payterm a where a.termid = '"+paytermcode+"' and a.pk_corp = '"+pk_corp+"' and nvl(a.dr,0)=0";
   		pk_payterm = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_payterm;
   	}
   	/**
   	 *根据部门名称获取部门主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getDeptId(String pk_corp) throws BusinessException{
   		String pk_deptdoc = "";
   		String sql = "select a.pk_deptdoc from bd_deptdoc a where  a.deptname like'销售部%' and a.pk_corp = '"+pk_corp+"' and nvl(a.dr,0)=0";
   		pk_deptdoc = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_deptdoc;
   	}
   	/**
   	 *根据发运编码获取发运主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getSendId(String sendcode,String pk_corp) throws BusinessException{
   		String pk_sendtype = "";
   		String sql = "select a.pk_sendtype from bd_sendtype a where  a.sendcode ='"+sendcode+"' and a.pk_corp = '0001' and nvl(a.dr,0)=0";
   		pk_sendtype = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_sendtype;
   	}
   	/**
   	 * 根据地点档案主键获取地区主键
   	 * @param vos
   	 * @param sourceBillType
   	 * @param destBillType
   	 * @return
   	 * @throws BusinessException 
   	 * @throws BusinessException
   	 */
   	public String getAreaclID(String pk_address,String pk_corp) throws BusinessException{
   		String pk_areacl = "";
   		String sql = "select b.pk_areacl from bd_address b where b.pk_address='"+pk_address+"' and b.pk_corp='0001' and nvl(b.dr,0)=0";
   		pk_areacl = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_areacl;
   	}
   	/**
   	 * 根据地点档案主键获取地址名称
   	 * @param vos
   	 * @param sourceBillType
   	 * @param destBillType
   	 * @return
   	 * @throws BusinessException 
   	 * @throws BusinessException
   	 */
   	public String getAddrName(String pk_address,String pk_corp) throws BusinessException{
   		String addrname = "";
   		String sql = "select b.addrname from bd_address b where b.pk_address='"+pk_address+"' and b.pk_corp='0001' and nvl(b.dr,0)=0";
   		addrname = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return addrname;
   	}
   	/**
   	 * 查询业务流程主键
   	 * @param pk_corp
   	 * @return
   	 */
   	public String getcbiztype(String pk_corp){
   		String cbiztype ="";
   		String sql ="select pk_busitype from bd_busitype where busicode ='S001' and pk_corp ='"+pk_corp+"' and nvl(dr,0)=0";
   		try {
			cbiztype = (String) uap.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
   		return cbiztype;
   	}
    public DMDataVO[] convertSourceVO(AggregatedValueObject[] vos, String sourceBillType, String destBillType)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return null;
    }
    ArrayList v = new ArrayList();

    DMVO[] dvo = (DMVO[])null;
    try {
      dvo = (DMVO[])PfUtilTools.runChangeDataAry(sourceBillType, destBillType, vos);
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }

    for (int i = 0; i < dvo.length; i++) {
      for (int j = 0; j < dvo[i].getChildrenVO().length; j++) {
        v.add(dvo[i].getChildrenVO()[j]);
      }
    }
    DMDataVO[] dmdvos = new DMDataVO[v.size()];
    dmdvos = (DMDataVO[])v.toArray(dmdvos);
    v = null;

//    if (sourceBillType.equals("21")) {
//      return pushsavePrepareDailyPlanFromPO(dmdvos);
//    }
    for (int i = 0; i < dmdvos.length; i++) {
      if (!dmdvos[i].getAttributeValue("vbilltype").equals("30"))
        continue;
      if ((dmdvos[i].getAttributeValue("pkdeststoreorg") == null) || (dmdvos[i].getAttributeValue("pkdeststoreorg").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstoreorg", dmdvos[i].getAttributeValue("pkdeststoreorg"));
      dmdvos[i].setAttributeValue("pkdeststoreorg", null);

      if ((dmdvos[i].getAttributeValue("pkdeststore") != null) && (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() > 0))
      {
        dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
        dmdvos[i].removeAttributeName("pkdeststore");
      }

      if ((dmdvos[i].getAttributeValue("pkdeststore") == null) || (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
      dmdvos[i].removeAttributeName("pkdeststore");
    }

    SuperVOUtil.execFormulaWithVOs(dmdvos, new String[] { "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)", "pksendaddress->iif(getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore) = \"\",getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore))" }, null);

    return dmdvos;
  }
    
    /**
     * 获得回写销售订单的数据。 功能： 参数： 返回： 例外： 日期：(2002-8-6 13:12:37) 修改日期，修改人，修改原因，注释标志：
     * 
     * @param vos
     *          nc.vo.dm.pub.DMDataVO[]
     */
    public DMDataVO[] getWriteOrderData(DMDataVO[] vos) {
      if (vos == null || vos.length == 0)
        return null;
      DMDataVO[] ordervos = new DMDataVO[vos.length];

      String pkdelivdaypl;
      UFDouble dnum;

      for (int i = 0; i < vos.length; i++) {

        ordervos[i] = new DMDataVO();
        ordervos[i].setStatus(vos[i].getStatus());
        pkdelivdaypl = (String) vos[i].getAttributeValue("pk_delivdaypl");

        Object o = vos[i].getAttributeValue("dnum");
        dnum = (o == null ? new UFDouble(0) : new UFDouble(o.toString()));

        ordervos[i].setAttributeValue("pkbillh", vos[i]
            .getAttributeValue("pkbillh"));
        ordervos[i].setAttributeValue("pkbillb", vos[i]
            .getAttributeValue("pkbillb"));
        ordervos[i].setAttributeValue("vbilltype", vos[i]
            .getAttributeValue("vbilltype"));
        ordervos[i].setAttributeValue("pksalecorp", vos[i]
            .getAttributeValue("pksalecorp"));

        ordervos[i].setAttributeValue("orderstatus", vos[i]
            .getAttributeValue("delautocanceloder"));

        if (ordervos[i].getStatus() == VOStatus.DELETED)
          ordervos[i].setAttributeValue("ndelivernum", dnum.multiply(-1));

        else if (ordervos[i].getStatus() == VOStatus.NEW)
          ordervos[i].setAttributeValue("ndelivernum", dnum);
      }

      return ordervos;
    }
    
}