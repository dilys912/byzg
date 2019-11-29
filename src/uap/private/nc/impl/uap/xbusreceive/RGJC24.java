package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.UpToDate;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;
import nc.vo.uap.xbusreceive.SFKMessageVO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.pub.pf.PfUtilBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * 付款信息接收应答
 *
 */
public class RGJC24  implements IxbusReceive {
	
	public JSONObject exeScript(JSONObject jsonarray) {
			System.out.println("json格式："+jsonarray);
			JSONObject jsons = jsonarray.getJSONObject("content");		
			JSONArray resarr = new JSONArray();//存储回执信息
			JSONObject msg = new JSONObject();
			DJZBVO djzbvo = new DJZBVO();
			SFKMessageVO sfkVO=new SFKMessageVO();
			StringBuffer message=new StringBuffer();//存储校验信息
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			String dqsj=new UFDateTime(System.currentTimeMillis()).toString();//系统时间
			//表头的VO插入
			DJZBHeaderVO djzbHeader = new DJZBHeaderVO();
			djzbHeader.setDr(0);
			djzbHeader.setPzglh(1);
			djzbHeader.setDjdl("fk");
			String bzNo = jsons.getString("BILL_NO");
			String daterq = jsons.getString("VOUCHER_DATE").toString();
			HashMap map;
			try {
				map = getCustInfo(bzNo);
			} catch (DAOException e) {
				message.append("查询付款单对应的客商及银行账户信息异常;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				//sfkVO.setPk_corp("");
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "查询付款单对应的客商及银行账户信息异常", null,null);
			}
			if(map==null){
				
				message.append("未查询到付款单对应的客商及银行账户信息;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				//sfkVO.setPk_corp("");
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未查询到付款单对应的客商及银行账户信息", null,null);
			}
			String corp = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
			if(daterq == null || daterq.length()==0){
				daterq=dqsj.substring(0, 10);
			}else{
				daterq = daterq.substring(0, 4)+"-"+daterq.substring(4, 6)+"-"+daterq.substring(6, 8);
			}
			
			UFDate fkrq = new UFDate(daterq);//付款凭证日期
			String yearStr = daterq.substring(0, 4);
			String monthStr = daterq.substring(5, 7);
			String kjqj = null;
			
			try {
				kjqj = getPeriodPk(yearStr);
			} catch (DAOException e2) {
				message.append("查询ERP配置会计期间："+kjqj+"异常"+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "查询ERP配置会计期间："+kjqj+"异常"+e2.getMessage(), null,null);
			}
			if(kjqj.equals("")){
				message.append("需要ERP配置会计期间："+kjqj+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "需要ERP配置会计期间："+kjqj, null,null);
			}
			String kjyf = null;
			try {
				kjyf = getPeriodMonthPk(monthStr,kjqj);
			} catch (DAOException e2) {
				message.append("查询ERP配置会计月份："+yearStr+kjyf+"异常"+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "查询ERP配置会计月份："+yearStr+kjyf+"异常"+e2.getMessage(), null,null);
			}
			if(kjyf.equals("")){
				message.append("需要ERP配置会计月份："+yearStr+kjyf+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "需要ERP配置会计月份："+yearStr+kjyf, null,null);
			}
			
			
			String ks = map.get("ks")==null?"":map.get("ks").toString();
			String account = map.get("account")==null?"":map.get("account").toString();
			
			djzbHeader.setDjbh(bzNo);//单据号 报支清单号
			djzbHeader.setDjkjnd(String.valueOf(fkrq.getYear()));
			djzbHeader.setDjkjqj(String.valueOf(fkrq.getMonth()));
			djzbHeader.setDjlxbm("D3");
			djzbHeader.setQcbz(new UFBoolean(false));
			String ywbm;
			try {
				ywbm = getYwbm(corp);
			} catch (DAOException e) {
				message.append("查询付款单类型异常："+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "查询付款单类型异常："+e.getMessage(), null,null);
			}
			if(ywbm.equals("")){
				message.append("ERP未查询到公司主键："+corp+"对应的收款单业务主键;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP未查询到公司主键："+corp+"对应的收款单业务主键", null,null);
			}
			djzbHeader.setYwbm(ywbm);//单据类型 //需要根据公司来查询得到对应的公司的付款单主键
			djzbHeader.setDjrq(fkrq);//单据日期
			djzbHeader.setDwbm(corp);//公司 //如何区分传送哪个公司？需要查询mdm分发后对应的公司
			
			djzbHeader.setEffectdate(fkrq);//起效日期
			djzbHeader.setKsbm_cl(ks);//供应商 
			String bz = jsons.getString("CURRENCY_CODE");
			HashMap bzMap;
			try {
				bzMap = getBzInfo(bz, kjqj,kjyf);
			} catch (DAOException e) {
				message.append("查询币种/汇率信息异常;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "查询币种/汇率信息异常", null,null);
			}
			if(bzMap==null){
				message.append("未查询到当前币种/汇率信息;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未查询到当前币种/汇率信息", null,null);
			}
			String bzpk = bzMap.get("bz").toString();
			String rate = bzMap.get("rate").toString();
			djzbHeader.setBbhl(new UFDouble(rate));//本币汇率 // 需要根据币种查询当前月份汇率
			
			djzbHeader.setBzbm(bzpk);//币种 //需要mdm分发的转换
//			djzbHeader.setWldx(Integer.valueOf(jsons.getString("DESC6")));//往来对象
			//表体的VO插入
			DJZBItemVO djzbItemVO = new DJZBItemVO();
			djzbItemVO.setDwbm(corp);//公司
			djzbItemVO.setFx(1);//方向
			djzbItemVO.setWldx(new Integer(1));//往来对象 1=供应商
			djzbItemVO.setBbhl(new UFDouble(rate));//本币汇率 // 需要根据币种查询当前月份汇率
			djzbItemVO.setBzbm(bzpk);//币种 //需要mdm分发的转换
			if(bzpk.equals("00010000000000000001")){//人民币
				djzbHeader.setBbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//本币金额//当前我国币种金额
				djzbHeader.setYbje(new UFDouble(jsons.getString("PAY_AMOUNT")));//原币金额
				
				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//本币余额
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//原币余额
				djzbItemVO.setJfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//借方本币金额
				djzbItemVO.setJfybje(new UFDouble(jsons.getString("PAY_AMOUNT")));//借方原币金额
				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//借方原币无税金额
				djzbItemVO.setWbfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//借方本币无税金额
//				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//借方原币无税金额
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//本币余额 需要根据账户余额取数
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//本币金额
			}else{
				djzbHeader.setBbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//本币金额//当前我国币种金额
				djzbHeader.setYbje(new UFDouble(jsons.getString("PAY_AMOUNT")));//原币金额
				
				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//本币余额
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//原币余额
				djzbItemVO.setJfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//借方本币金额
				djzbItemVO.setJfybje(new UFDouble(jsons.getString("PAY_AMOUNT")));//借方原币金额
				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//借方原币无税金额
				djzbItemVO.setWbfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//借方本币无税金额
			}
			
			djzbItemVO.setQxrq(fkrq);//起效日期
			djzbItemVO.setKsbm_cl(ks);//供应商 
//			djzbItemVO.setKslb(Integer.valueOf(jsons.getString("DESC17")));//扣税类别
			djzbItemVO.setBilldate(fkrq);
			
//			try {
//				String accid = getCustBankId(ks,corp);
//				djzbItemVO.setAccountid(accid);
//			} catch (DAOException e) {
//				return returnMsg(false, "查询银行账户信息异常"+e.getMessage(), null,null);
//			}
			DJZBItemVO items[] = new DJZBItemVO[1];
			items[0] =djzbItemVO;
			djzbvo.setParentVO(djzbHeader);
			djzbvo.setChildrenVO(items);
			List sj = saveConfig(djzbvo,message);
			JSONObject dj=(JSONObject) sj.get(1);
			System.out.println("保存结果"+dj);
			if (dj.getBoolean("s")) {
				message.append("新增信息完成;");
				msg.put("SYNSTATUS", 0);// 0（成功）或者1（失败）
				msg.put("SYNRESULT", dj.getString("m") );
				
			} else {
				msg.put("SYNSTATUS", 1);// 0（成功）或者1（失败）
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
			sfkVO.setDr("0");
			sfkVO.setTs(daterq);
			sfkVO.setPk_corp(corp);
			sfkVO.setBcBillno(bzNo);
			sfkVO.setErpBillno(sj.get(2).toString());
			sfkVO.setMessage(sj.get(0).toString());
			try {
				iVOPersistence.insertVO(sfkVO);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return returnMsg(true, "执行完成", null,resarr.toString());
	}
	/**
	 * 组装回执
	 * */
	private JSONObject returnMsg(boolean b, String m, String puuid, String d) {
		JSONObject json = new JSONObject();
		if (b) {
			json.put("s", "success");
		} else {
			json.put("s", "error");
		}
		json.put("m", m);
		json.put("puuid", puuid);
		json.put("d", d);
		return json;
	}

	
	/**
	 * 查询主子表连查  判断主键是否存在  若存在就修改  不存在则添加
	 * @param hvo
	 * @return 
	 */
	public List saveConfig(DJZBVO aggvo,StringBuffer message){
		List list=new ArrayList();
		JSONObject msg=new JSONObject();
		String djbhERP="";
		String erpDjh="";
		if(aggvo==null){
			message.append("存储配置信息，未获取到数据;");
			msg=exeReMsg(false,"存储配置信息，未获取到数据");
			djbhERP="";
			list.add(message);
			list.add(msg);
			list.add(djbhERP);
			return list;
		}
		DJZBHeaderVO hvo = (DJZBHeaderVO) aggvo.getParentVO();
		DJZBItemVO[] bvos = (DJZBItemVO[]) aggvo.getChildrenVO();
		
		try {
			//只需要新增即可
			Object djh=new PfUtilBO().processAction("SAVE", "D3", new UFDate(new Date()).toString(), null, aggvo, null);
			if(djh != null){
				//[0001AA100000000N4THD, nc.vo.ep.dj.DJZBVO@cdf81c]
			     djbhERP=djh.toString().replace("[", "");
			     int num=djbhERP.indexOf(",");
			     djbhERP=djbhERP.substring(0, num);
			     erpDjh=queryDjh(djbhERP);
			}else {
				erpDjh="";
			}
		} catch (Exception e) {
			message.append("保存收款单异常:"+e.getMessage()+";");
			msg= exeReMsg(false,"保存收款单异常:"+e.getMessage());
			list.add(message);
			list.add(msg);
			list.add(djbhERP);
			return list;
		}
		
		msg=exeReMsg(true,"新增信息完成；");
		list.add(message);
		list.add(msg);
		list.add(erpDjh);
		return list;
			
	}
	
	private  String  queryDjh(String djbhERP){
		  String billno="";
		  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  String check=" select djbh from arap_djzb where vouchid='"+djbhERP+"' and nvl(dr,0)= 0";
	 	  try {
		      billno = (String) receiving.executeQuery(check, new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billno;  
	  }
	private JSONObject exeReMsg(boolean s, String m) {
		JSONObject j = new JSONObject();
		if (s) {
			j.put("s", true);
		} else {
			j.put("s", false);
		}
		j.put("m", m);
		return j;
	}
	
	/**
	 * 判断字符String
	 * */
	private String strFieldHas(JSONObject j, String f) {
		String val = null;
		if (j.has(f)) {
			val = j.getString(f);
		}
		return val;
	}

	/**
	 * 判断字符Integer
	 * */
	private Integer intFieldHas(JSONObject j, String f) {
		Integer val = null;
		if (j.has(f)) {
			val = Integer.valueOf(j.getString(f));
		}
		return val;
	}
	
	/**
	 * 判断字符UFdouble
	 * */

	private UFDouble UfdFieldHas(JSONObject j, String f) {
		UFDouble ufd = new UFDouble();
		if (j.has(f)) {
			ufd = new UFDouble(j.getString(f));
		}
		return ufd;
	}
	
	
	/**
	 * 通过应付单存储的报支清单中间表获取对应的客商、银行账户信息、公司
	 * @param bzNo
	 * @return
	 * @throws DAOException
	 */
	private HashMap getCustInfo(String bzNo) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_corp, ksbm_cl as ks, gys_account as account ") 
		.append("   from itf_arap_bz bz ") 
		.append("  where billno_bz = '"+bzNo+"' ") 
		.append("    and nvl(dr, 0) = 0 order by ts desc ") ;
		BaseDAO dao = new BaseDAO();
		HashMap map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
		return map;
	}
	
	
	/**
	 * 获取当前公司付款单单据类型
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	private String getYwbm(String corp) throws DAOException{
		BaseDAO dao = new BaseDAO();	
		StringBuffer sql = new StringBuffer();
		sql.append(" select djlxoid  from arap_djlx ") 
		.append(" where djlxjc = '付款单' ") 
		.append(" and dwbm = '"+corp+"' ") 
		.append(" and nvl(dr,0) = 0 ");
		Object djlx = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String djlxStr = djlx==null?"":djlx.toString();
		return djlxStr;
	}
	
	/**
	 * 获取币种主键、外币汇率
	 * @param bz
	 * @return
	 * @throws DAOException 
	 *//*
	private HashMap getBzInfo(String bz,UFDate date,String corp) throws DAOException{
		HashMap map = new HashMap();
		
		if(bz.equals("CNY")){//人民币默认为汇率1
			map.put("bz", "00010000000000000001");
			map.put("rate", "1.00000");
		}else{
			String datestr = date.getYear()+"-"+date.getMonth();
			StringBuffer sql = new StringBuffer();
			sql.append(" select cur.rate, cur.pk_currinfo ") 
			.append("   from bd_currinfo info, bd_currrate cur ") 
			.append("  where info.pk_currinfo = cur.pk_currinfo ") 
			.append("    and info.pk_currtype = '00010000000000000001' ") 
			.append("    and info.oppcurrtype = ") 
			.append("        (select pk_currtype from bd_currtype where currtypecode = '"+bz+"') ") 
			.append("    and cur.ratedate like '"+datestr+"%' ") 
			.append("    and nvl(info.dr, 0) = 0 ") 
			.append("    and nvl(cur.dr, 0) = 0 ") 
			.append("    and info.pk_corp = '"+corp+"' ") ;

			BaseDAO dao = new BaseDAO();
			map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
			if(map!=null){	
				if(map.get("bz")==null){
					return null;
				}else{
					String bzpk = map.get("bz").toString();
					if(bzpk.equals("00010000000000000001")){//人民币汇率为1
						map.put("rate", "1.00000");
					}else{
						return map;
					}
				}
			}
		}
		
		return map;
	}*/
	
	/**
	 * 根据账户查询id
	 * @param account
	 * @return
	 * @throws DAOException 
	 */
	public String getAccid(String account,String pk_corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_accid ") 
		.append("   from bd_accid ") 
		.append("  where accidcode = '"+account+"' ") 
		.append("    and pk_corp = '"+pk_corp+"' ") ;
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(sql.toString(),new ColumnProcessor());
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}

	}
	
	/**
	 * 获取币种主键、外币汇率
	 * @param bz
	 * @return
	 * @throws DAOException 
	 */
	private HashMap getBzInfo(String bz,String kjqj,String kjyf) throws DAOException{
		HashMap map = new HashMap();
		
		if(bz.equals("CNY")){//人民币默认为汇率1
			map.put("bz", "00010000000000000001");
			map.put("rate", "1.00000");
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append(" select adj.adjustrate as rate,info.oppcurrtype as bz") 
			.append("   from bd_currinfo info, bd_adjustrate adj ") 
			.append("  where info.pk_currinfo = adj.pk_currinfo ") 
			.append("    and info.pk_currtype = '00010000000000000001' ") 
			.append("    and info.oppcurrtype = ") 
			.append("        (select pk_currtype from bd_currtype where currtypecode = '"+bz+"') ") 
			.append("    and adj.pk_accperiod = '"+kjqj+"' ") 
			.append("    and adj.pk_accperiodmonth = '"+kjyf+"' ") 
			.append("    and nvl(info.dr, 0) = 0 ") 
			.append("    and nvl(adj.dr, 0) = 0 ") ;

			BaseDAO dao = new BaseDAO();
			map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
			if(map!=null){	
				if(map.get("bz")==null){
					return null;
				}else{
					String bzpk = map.get("bz").toString();
					if(bzpk.equals("00010000000000000001")){//人民币汇率为1
						map.put("rate", "1.00000");
					}else{
						return map;
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * 获取会计期间主键
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPeriodPk(String year) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_accperiod from bd_accperiod where periodyear = '"+year+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkacc = obj==null?"":obj.toString();
		
		return pkacc;		
	}
	
	/**
	 * 获取会计月份主键
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPeriodMonthPk(String month,String pkacc) throws DAOException{
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_accperiodmonth from bd_accperiodmonth where month = '"+month+"' and pk_accperiod = '"+pkacc+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkmonth = obj==null?"":obj.toString();
		
		return pkmonth;		
	}
	
	
	/**
	 * 根据账户查询id
	 * @param account
	 * @return
	 * @throws DAOException 
	 */
	public String getCustBankId(String ksid,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append("  ") 
		.append(" select cub.account ") 
		.append("   from bd_cumandoc cum ") 
		.append("  inner join bd_cubasdoc bas on bas.pk_cubasdoc = cum.pk_cubasdoc ") 
		.append("  inner join bd_custbank cub on bas.pk_cubasdoc = cub.pk_cubasdoc ") 
		.append("  where cum.pk_cumandoc = '"+ksid+"' ") 
		.append("    and cum.pk_corp = '"+corp+"' ") 
		.append("    and nvl(cum.dr, 0) = 0 ") 
		.append("    and nvl(bas.dr, 0) = 0 ") 
		.append("    and nvl(cub.dr, 0) = 0 ") 
		.append("    and (cum.custflag = '1' OR cum.custflag = '3') ") 
		.append("    and (cum.sealflag is null AND (frozenflag = 'N' or frozenflag is null)) ");

		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(sql.toString(),new ColumnProcessor());
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}

	}
}
