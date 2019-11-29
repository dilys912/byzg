package nc.bs.ar.receivableImpl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.ar.receivableItf.IreceivableService;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 生成应收单订单
 * @author PengJia
 * 2017年11月8日10:59:05
 * 
 */
@SuppressWarnings("all")
public class ReceivableImpl implements IreceivableService{
	public String Receivable(String json) {
		JSONArray jsonArrayList = new JSONArray();
		Map map = new HashMap();
		List<Map> listMap = new ArrayList<Map>();
		try {
			jsonArrayList = new  JSONArray(json);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JSONObject jsonObject = null;
		if(jsonArrayList.length()>0){
			for(int j =0;j<jsonArrayList.length();j++){
				try {
					jsonObject = jsonArrayList.getJSONObject(j);
					//解析json数据
					Iterator iterator  = jsonObject.keys();
					String key = null;
					String value =null;
					while(iterator.hasNext()){
						key = (String) iterator.next();
						value = jsonObject.getString(key);
						if(key.equals("bodylist")){
							JSONArray jsonArray = jsonObject.getJSONArray(key);
							for(int i = 0;i<jsonArray.length();i++){
								String key1 = null;
								String value1 =null;
								JSONObject jsonOb =  jsonArray.getJSONObject(i);
								Map map1 =new HashMap();
								Iterator iter = jsonOb.keys();
								while(iter.hasNext()){
									key1 = (String)iter.next();
									value1 = jsonOb.getString(key1);
									map1.put(key1, value1);
								}
								listMap.add(map1);
							}
						}else{
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式'"+e.getMessage()+"'\"}";
				}
				
			}
		}
		String username = "";//接口连接用户
		String password = "";//接口连接密码
		//接口连接用户名
		username = (String) map.get("username");
		password = (String)map.get("password");
		if(null==username){
			return "{\"status\":\"error\",\"message\":\"连接用户(username)不可为空\",\"bodylist\":[]}";
		}else if(username.equals("shzl")){
			username = username.toString().trim();
		}else{
			return "{\"status\":\"error\",\"message\":\"连接用户(username)错误\",\"bodylist\":[]}";
		}
		
		if(null==password){
			return "{\"status\":\"error\",\"message\":\"连接密码(password)不可为空\",\"bodylist\":[]}";
		}else if(password.equals("123456")){
			password = password.toString().trim();
		}else{
			return "{\"status\":\"error\",\"message\":\"连接密码(password)错误\",\"bodylist\":[]}";
		}
		
		
		//公司编码
		String company = (String) map.get("pk_corp");
		Map<String, String> comMap = null;
		if(company == null){
			return "{\"status\":\"error\",\"message\":\"公司编码(company)不可为空\",\"bodylist\":[]}";
		}else{
			comMap = this.QueryCorpdoc(company);
		}
		
		//单据日期 (当前时间)
		Date ufdate =new Date();
		ufdate = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		String time = sdf.format(ufdate);
		String yy = time.substring(0, 4);//年份
		String mm = time.substring(5, 7);//月份
		
		//起效日期 (当前时间)
		UFDate Startdate =new UFDate();
		Startdate = new UFDate(new Date()); 
		
//		//部门编码
//		String deptcode = (String) map.get("deptcode");
//		String dept = null;
//		Map<String, String> deptMap = null;
//		if(deptcode == null){
//			return "{\"status\":\"error\",\"message\":\"部门编码(deptcode)不可为空\",\"bodylist\":[]}";
//		}else{
//			deptMap =this.QueryDeptdoc(deptcode, comMap.get("pk_corp"));
//		}
		
		//客户编码
		String custcode = (String) map.get("custcode");
		Map<String, String> custMap =null;
		if(custcode ==null){
			return "{\"status\":\"error\",\"message\":\"客户编码(custcode)不可为空\",\"bodylist\":[]}";
		}else{
			custMap = this.QueryCustDoc(custcode, company);
		}
		//往来对象
//		String contactsObj = (String) map.get("contactsobj");
//		HashMap<String, String> conObj = null;
//		if(contactsObj ==null){
//			return "{\"status\":\"error\",\"message\":\"往来对象(contactsobj)不可为空\",\"bodylist\":[]}";
//		}else{
//			conObj = this.QueryObj(contactsObj, comMap.get("pk_corp"));
//		}
		
	
		//纸质发票号
		String invoicecode = (String)map.get("invoicecode")==null?"":(String)map.get("invoicecode");
			
		//本币汇率
//		Object rate =map.get("rate");
//		String ra = null;
//		Map<String, String> rateMap =null;
//		if(null ==rate){
//			return "{\"status\":\"error\",\"message\":\"本币汇率(rate)不可为空\",\"bodylist\":[]}";
//		}else{
//			ra = rate.toString().trim();
//		}
		
//		//生效标志
//		Object sxbzmc = map.get("sxbzmc");
//		String sxbz = null;
//		Map<String, String> sxbzMap =null;
//		if(null == sxbzmc){
//			return "{\"status\":\"error\",\"message\":\"生效标志(sxbzmc)不可为空\",\"bodylist\":[]}";
//		}else{
//			sxbz = sxbzmc.toString().trim();
//		}
		//录入人员信息
//		String psncode = (String)map.get("psncode");
//		Map<String,String> psnMap = null;
//		if(null==psncode){
//			return "{\"status\":\"error\",\"message\":\"人员信息(psncode)不可为空\",\"bodylist\":[]}";
//		}else{
//			psnMap = this.QueryPsndoc(psncode, comMap.get("pk_corp"));
//		}
		
		//应收单聚合VO
		DJZBVO djzbVo = new DJZBVO();
		DJZBHeaderVO hvo = new DJZBHeaderVO();
//		hvo.setBbje(new UFDouble());//本币金额
		hvo.setKsbm_cl(custMap.get("pk_cubasdoc"));//客户
		hvo.setDeptid("1076A210000000002K6A");//部门
		hvo.setDjdl("ys");//单据大类
		hvo.setDjkjnd(yy);//单据会计年度
		hvo.setDjkjqj(mm);//单据会计期间
		hvo.setDjlxbm("D0");//单据类型编码
		hvo.setDjrq(Startdate);//单据日期
		hvo.setDjzt(1);//单据状态
		hvo.setDwbm(company);//单位编码(公司)
		hvo.setEffectdate(Startdate);//起效日期
		hvo.setLrr("");//录入人   
		hvo.setLybz(0);//来源标志
		hvo.setPrepay(new UFBoolean("N"));//预收款标志
		hvo.setPzglh(0);//系统标志
		hvo.setQcbz(new UFBoolean("N"));//期初标志
		hvo.setShkjnd(yy);//审计会计年度    年份
		hvo.setShkjqj(mm);//审计 会计期间  月份
		hvo.setSpzt("");//审批状态
		hvo.setSxbz(10);//生效标志
		hvo.setSxkjnd(yy);//生效年度   年份
		hvo.setSxkjqj(mm);//生效期间   月份
		hvo.setSxr("");//生效人
		hvo.setSxrq(Startdate);//生效日期  (单据日期)
		hvo.setYbje(new UFDouble());//原币金额
		hvo.setZgyf(0);//暂估应收标志
//		hvo.setWldx(Integer.valueOf(conObj.get("custflag"))); //往来对象
		hvo.setZyx17(invoicecode);//纸质发票号
		hvo.setZzzt(0);//支付状态
		hvo.setXslxbm("1016A21000000000WOPI");//业务类型
		hvo.setYwbm("0001A21000000000E7SB");//单据类型
	
		DJZBItemVO[] djzbBvo = new DJZBItemVO[listMap.size()];
		for (int i = 0; i < listMap.size(); i++) {
			Map bodyMap =  listMap.get(i);
			//客户编码,客户名称
			String custno =  (String) bodyMap.get("custno");
			String custno1 = null;
			Map<String,String> custnoMap=null;
			if(null == custno){
				return "{\"status\":\"error\",\"message\":\"客户信息(custno)不可为空\",\"bodylist\":[]}";
			}else{
				custnoMap = this.QueryCustDoc(custno, comMap.get("pk_corp"));
			}
			
			//部门
			Object deptcode1 = bodyMap.get("deptcode");
			String dept1 = null;
			Map<String,String> dept1Map = null;
			if(null == deptcode1 ){
				return "{\"status\":\"error\",\"message\":\"部门信息(deptcode1)不可为空\",\"bodylist\":[]}";
			}else{
				dept1 = deptcode1.toString().trim();
			}
			
			//收付款协议
//			Object sfkxymc = bodyMap.get("sfkxymc");
//			String sfkxy = null;
//			if(sfkxymc!=null){
//				sfkxy = sfkxymc.toString().trim();
//			}
			
//			//币种
//			Object bzmc = bodyMap.get("currency");
//			String bz = null;
//			if(null == bzmc){
//				return "{\"status\":\"error\",\"message\":\"币种信息(bzmc)不可为空\",\"bodylist\":[]}";
//			}else{
//				bz = bzmc.toString().trim();
//			}
//			
//			//本币汇率
//			Object bbhlmc = bodyMap.get("rate");
//			String bbhl = null;
//			if(null != bbhlmc){
//				bbhl = bbhlmc.toString().trim();
//			}
//			//发票号 
//			Object fpnomc = bodyMap.get("fpnomc");
//			String fpno = null;
//			if(fpnomc ==null){
//				return "{\"status\":\"error\",\"message\":\"发票号信息(fpnomc)不可为空\",\"bodylist\":[]}";
//			}else{
//				fpno = fpnomc.toString().trim();
//			}
			
			//含税单价 
			Object hsdjmc = bodyMap.get("hsdjmc");
			//借方数量
			Object jfnumbermc = bodyMap.get("jfnumbermc");
			//税率
			Object slmc = bodyMap.get("slmc");
			//实际税率
			UFDouble ntaxrate1=new UFDouble(1).add(new UFDouble(slmc.toString()).div(new UFDouble(100)));//实际税率是1.17  新增的
			//无税单价
			UFDouble Bjdwwsdj = new UFDouble(hsdjmc.toString()).div(ntaxrate1);
			//含税金额 
			UFDouble hsje = new UFDouble(jfnumbermc.toString()).multiply(new UFDouble(hsdjmc.toString()));
			//数据格式化
			DecimalFormat df = new DecimalFormat("######0.00");
			Object aa = df.format(hsje);
			//无税金额
			UFDouble wsje = new UFDouble(aa.toString()).div(ntaxrate1);
			//税金
			UFDouble sj = hsje.sub(wsje);
			
//			//订单号
//			Object ddnomc = bodyMap.get("ddnomc");
//			String ddno = null;
//			if(ddnomc!=null){
//				ddno = ddnomc.toString().trim();
//			}
			//存货编码
			String chbmcode = (String) bodyMap.get("chbmcode");
			HashMap<String,String> chbmMap = null;
			if(null == chbmcode){
				return "{\"status\":\"error\",\"message\":\"存货编码信息(chbmcode)不可为空\",\"bodylist\":[]}";
			}else{
				chbmMap = this.QueryInv(chbmcode, comMap.get("pk_corp"));
			}
			//应收单据子表VO
			DJZBItemVO bvo = new DJZBItemVO();
//			bvo.setBbhl(new UFDouble(bbhl));//本币汇率
			bvo.setBbye(hsje);//本币余额
			bvo.setBilldate(Startdate);//单据日期
			bvo.setBjdwhsdj(new UFDouble(hsdjmc.toString()));//报价单位含税单价
			bvo.setBjdwsl(new UFDouble(jfnumbermc.toString()));//报价单位数量
			bvo.setBjdwwsdj(Bjdwwsdj);//报价单位无税单价
			bvo.setBjjldw("");//报价单位计量单位
			bvo.setBzbm("00010000000000000001");//币种编码
			bvo.setChbm_cl(chbmMap.get("pk_invmandoc"));//存货管理档案主键
			bvo.setCinventoryid(chbmMap.get("pk_invbasdoc"));//存货基本档案主键
			bvo.setPk_invcl(chbmMap.get("Pk_invcl"));//存货分类主键
			bvo.setDeptid("1076A210000000002K6A");//部门主键
			bvo.setDj(Bjdwwsdj);//单价
			bvo.setDwbm(comMap.get("pk_corp"));//公司PK
			bvo.setFlbh(1);//单据分录编号
			bvo.setFx(1);//方向
			bvo.setHbbm("");//伙伴编码
			bvo.setHsdj(new UFDouble(hsdjmc.toString()));//含税单价
			bvo.setIsSFKXYChanged(new UFBoolean("N"));//收付款协议是否发生变化
			bvo.setIsverifyfinished(new UFBoolean("N"));//是否核销完成
			bvo.setJfbbje(hsje);//借方本币金额
			bvo.setJfbbsj(sj);//借方本币税金
			bvo.setJfshl(new UFDouble(jfnumbermc.toString())); //借方数量
			bvo.setJfybje(hsje);//借方原币金额
			bvo.setJfybsj(sj);//借方原币税金
			bvo.setJfybwsje(wsje);//借方原币无税金额
			bvo.setKsbm_cl(custnoMap.get("pk_cumandoc"));//客商管理档案主键
			bvo.setKslb(1);//扣税类别
			bvo.setQxrq(Startdate);//起效日期
			bvo.setSfbz("3");//收付标志
			bvo.setShlye(new UFDouble(jfnumbermc.toString()));//数量余额
			bvo.setSl(new UFDouble(slmc.toString()));//税率
			bvo.setYbye(new UFDouble(hsje.toString()));//原币金额
			bvo.setYwybm("");//业务员PK
			bvo.setBzmc("00010000000000000001");//币种
			djzbBvo[i]=bvo;
		}
		   djzbVo.setParentVO(hvo);
		   djzbVo.setChildrenVO(djzbBvo);
		
		   ArrayList<HashMap<String,String>> okbillls = new ArrayList<HashMap<String,String>>(); 
	        
	        ArrayList arrReturnFromBs = null;
			try {
				InvocationInfoProxy.getInstance().setUserCode("0001AA1000000009DL6H");
				InvocationInfoProxy.getInstance().setCorpCode("1016");
				arrReturnFromBs = (ArrayList) new PfUtilBO().processAction("SAVE", "D0", Startdate.toString(), null, djzbVo, null);
				
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"单据保存失败-"+e.toString()+"\",\"bodylist\":[]}";
			}
			// 得到主键
	        if (arrReturnFromBs == null || arrReturnFromBs.size() == 0) {
	            return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"单据保存失败\",\"bodylist\":[]}";
	        }else{
	        	//记录执行单据号及HPK
	        	HashMap<String,String> bill_1 = new HashMap<String, String>();
	        	bill_1.put("billname", "应收单");
	        	bill_1.put("vbillcode", arrReturnFromBs.get(1).toString());
	        	bill_1.put("hpk", arrReturnFromBs.get(0).toString());
	        	okbillls.add(bill_1);
	        	return "{\"status\":\"error\",\"orderno\":\"\",\"message\":\"Success单据保存成功\",\"bodylist\":[]}";
        }
	}
	
	IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	/**
	 * 获取客商信息
	 * 2017年11月15日
	 * @param custCode
	 * @return
	 */
	private HashMap<String,String> QueryCustDoc(String CustCode,String pk_corp){
		HashMap<String, String> custInfo = new HashMap<String, String>();
		StringBuffer custSql = new StringBuffer();
		custSql.append(" SELECT bd_cubasdoc.custcode, ") 
		.append("        bd_cubasdoc.custname,")  
		.append("        bd_cubasdoc.pk_cubasdoc,  ")  
		.append("        bd_cumandoc.custflag, ")  
		.append("        bd_cubasdoc.dr,")  
		.append("        bd_cubasdoc.ts "+"\n") 
		.append("   FROM bd_cubasdoc "+"\n")
		.append("   left join bd_cumandoc "+"\n")
		.append("   on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc "+"\n")
		.append("   WHERE 1 = 1 "+"\n")
		.append("    and (bd_cumandoc.pk_corp = '"+pk_corp+"')"+"\n") 
		.append("    and bd_cubasdoc.custcode ='"+CustCode+"'")
		.append("	 and nvl(bd_cubasdoc.dr ,0)=0");
		try {
			List lsi = (List) bs.executeQuery(custSql.toString(),new MapListProcessor());
			if (lsi.size() > 0) {
				custInfo = (HashMap<String, String>) lsi.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custInfo;
	}
	
	/**
	 * 查询公司档案信息
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	private HashMap<String,String> QueryCorpdoc(String corp) {
		StringBuffer corpSql = new StringBuffer();
		corpSql.append(" select pk_corp ") 
		.append("   from bd_corp ") 
		.append("  where  pk_corp = '"+corp+"' ")
		.append(" and nvl(dr,0) = 0 "); 
		HashMap<String, String> lbs = new HashMap<String, String>();
		try {
			List lsi = (List) bs.executeQuery(corpSql.toString(),new MapListProcessor());
			if (lsi.size() > 0) {
				lbs = (HashMap<String, String>) lsi.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lbs;
	}
	/**
	 * 往来对象
	 * @param code
	 * @param corp
	 * @return
	 */
	private HashMap<String,String> QueryObj(String code,String corp){
		StringBuffer sb = new StringBuffer();
		sb.append(" select  a.custflag ") 
		.append("   from bd_cumandoc a  ") 
		.append("  inner join bd_cubasdoc b  ") 
		.append("     on a.pk_cubasdoc = b.pk_cubasdoc where b.custcode ='"+code+"' and a.pk_corp ='"+corp+"' ");
		HashMap<String, String> lbs = new HashMap<String, String>();
		try {
			List lsi = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
			if(lsi.size()>0){
				lbs = (HashMap<String, String>) lsi.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lbs;	
	}
	/**
	 * 查询存货信息  
	 * 2017年11月15日
	 * @param InvCOde
	 * @param corp
	 * @return
	 */
	private HashMap<String,String> QueryInv(String InvCOde,String corp){
		StringBuffer sb = new StringBuffer();
		sb.append(" select invbas.pk_invbasdoc, invman.pk_invmandoc,invcl.pk_invcl ") 
		.append("   from bd_invbasdoc invbas ") 
		.append("   join bd_invmandoc invman ") 
		.append("     on invbas.pk_invbasdoc = invman.pk_invbasdoc ") 
		.append("     left join bd_invcl invcl ") 
		.append("     on invbas.pk_invcl = invcl.pk_invcl   ") 
		.append("     where invbas.invcode ='"+InvCOde+"' and invman.pk_corp ='"+corp+"'");
		HashMap<String, String> invMap = new HashMap<String, String>();
		try {
			List invList = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
			if(invList.size()>0){
				invMap = (HashMap<String, String>) invList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return invMap;
	}
	/**
	 * 查询部门信息
	 * 2017年11月15日
	 * @param deptcode
	 * @param pk_corp
	 * @return
	 */
	private HashMap<String,String> QueryDeptdoc(String deptcode,String pk_corp){
		StringBuffer sb = new StringBuffer();
		sb.append(" select dept.pk_deptdoc from bd_deptdoc  dept  ") 
		  .append(" where nvl(dept.dr,0)=0 and dept.deptcode ='"+deptcode+"'  and dept.pk_corp ='"+pk_corp+"'");
		HashMap<String,String> deptMap = new HashMap<String, String>();
		try {
			List deptList = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
			if(deptList.size()>0){
				deptMap = (HashMap<String, String>) deptList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptMap;
	}
	
	/**
	 * 查询人员信息
	 * @param psncode
	 * @param corp
	 * @return
	 */
	private HashMap<String,String> QueryPsndoc(String psncode ,String corp){
		StringBuffer psnBuffer = new StringBuffer();
		psnBuffer.append("   select psn.pk_psndoc from bd_psndoc psn ")
				 .append("   where psn.psncode ='"+psncode+"' ")
				 .append("   and psn.pk_corp ='"+corp+"' and nvl(psn.dr,0)=0");
		HashMap<String,String> psnMap = new  HashMap<String, String>();
		try {
			List psnList = (List) bs.executeQuery(psnBuffer.toString(),new MapListProcessor());
			if(psnList.size()>0){
				psnMap = (HashMap<String, String>) psnList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return psnMap;
	}
}
