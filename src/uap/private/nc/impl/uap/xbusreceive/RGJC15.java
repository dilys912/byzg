package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


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
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.pub.pf.PfUtilBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.xbusreceive.SFKMessageVO;
/**
 * 收款红冲信息应答
 *
 */
public class RGJC15  implements IxbusReceive {
	
	public JSONObject exeScript(JSONObject jsonarray) {
			System.out.println("json格式："+jsonarray);
			JSONObject jsons = jsonarray.getJSONObject("content");
			JSONArray resarr = new JSONArray();//存储回执信息
			JSONObject msg = new JSONObject();
			JSONObject dj = new JSONObject();
			SFKMessageVO sfkVO=new SFKMessageVO();
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			String itfcorp ="0001";
			String itfbccode = "";
			String itfmsg = "";
			String zzlx = jsons.getString("PROC_TYPE").toString();//R-红冲；Z-转账；B-退款；
			if(zzlx.equals("R")||zzlx.equals("B")||zzlx.equals("Z")){//红冲根据蓝字生成的、退款是直接做的一张退款单
				DJZBVO djzbvo = new DJZBVO();
				String subcode = jsons.getString("SUB_RECEIPT_CODE");//子收条号
				itfbccode = subcode;
				UFDouble dealMoney = jsons.get("PROC_AMT")==null?new UFDouble(0.0):new UFDouble(jsons.get("PROC_AMT").toString());//收款收条金额
				//表头的VO插入
				DJZBHeaderVO djzbHeader = null;
				try {
					djzbHeader = getHeaderVO(subcode);
				} catch (DAOException e1) {
					itfmsg = "ERP查询到标财[子收条号]："+subcode+"对应的收款单异常："+e1.getMessage();
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				if(djzbHeader==null){
					itfmsg = "ERP未查询到标财[子收条号]："+subcode+"对应的收款单！";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				itfcorp = djzbHeader.getDwbm();
				
				DJZBItemVO items[] = new DJZBItemVO[1];
				DJZBItemVO bodyvo = null;
				try {
					bodyvo = getBodyVO(djzbHeader.getVouchid());
				} catch (DAOException e) {
					itfmsg = "ERP未查询到标财[子收条号]："+subcode+"对应的收款单表体异常："+e.getMessage();
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				if(bodyvo==null){
					itfmsg ="ERP未查询到标财[子收条号]："+subcode+"对应的收款单表体！";
					return returnMsg(false,itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				
				String bzpk = bodyvo.getBzbm()==null?"":bodyvo.getBzbm();//币种 
				if(bzpk.equals("")){
					itfmsg = "未获取到原单据："+djzbHeader.getDjbh()+"币种信息！";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				UFDouble rate = bodyvo.getBbhl()==null?new UFDouble(0.0):bodyvo.getBbhl();//汇率
				if(rate.doubleValue()==0){
					itfmsg = "ERP原收条单据："+djzbHeader.getDjbh()+"对应汇率信息为0/空，请确认！";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				bodyvo.setVouchid("");
				bodyvo.setFb_oid("");
				
				if(bzpk.equals("00010000000000000001")){//人民币
					djzbHeader.setBbje(dealMoney.multiply(-1));//本币金额//当前我国币种金额
					bodyvo.setYbye(dealMoney.multiply(-1));//原币余额
					bodyvo.setBbye(dealMoney.multiply(-1));//本币余额
					
					bodyvo.setDfbbje(dealMoney.multiply(-1));//贷方本币金额
					bodyvo.setDfybje(dealMoney.multiply(-1));//贷方原币金额 
					bodyvo.setDfybwsje(dealMoney.multiply(-1));//贷方原币无税金额
					bodyvo.setDfbbwsje(dealMoney.multiply(-1));//贷方本币无税金额
				}else{
					djzbHeader.setBbje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//本币金额//当前我国币种金额
					bodyvo.setYbye(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//原币余额
					bodyvo.setBbye(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//本币余额
					
					bodyvo.setDfbbje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//贷方本币金额
					bodyvo.setDfybje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//贷方原币金额 
					bodyvo.setDfybwsje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//贷方原币无税金额
					bodyvo.setDfbbwsje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//贷方本币无税金额
				}
				
				djzbHeader.setScomment(subcode);
				items[0] = bodyvo;
				djzbHeader.setVouchid("");
				djzbHeader.setDjbh("");
				djzbvo.setParentVO(djzbHeader);
				djzbvo.setChildrenVO(items);
				List sj = saveConfig(djzbvo);
				dj=(JSONObject) sj.get(1);
				sfkVO.setPk_corp(djzbHeader.getDwbm());
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(dj.toString());
				try {
					iVOPersistence.insertVO(sfkVO);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				itfmsg = "ERP收款信息红冲支持类型：R-红冲；B-退款；Z-转账！";
				return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
			}
			
			System.out.println("保存结果"+dj);
			if (dj.getBoolean("s")) {
				msg.put("SYNSTATUS", 0);// 0（成功）或者1（失败）
				msg.put("SYNRESULT", dj.getString("m") );
				
			} else {
				msg.put("SYNSTATUS", 1);// 0（成功）或者1（失败）
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
		return returnMsg(true, "执行完成", null,resarr.toString(),itfcorp,itfbccode,itfmsg);
	}
	/**
	 * 组装回执
	 * */
	private JSONObject returnMsg(boolean b, String m, String puuid, String d,String corp,String bccode,String msg) {
		JSONObject json = new JSONObject();
		if (b) {
			json.put("s", "success");
		} else {
			json.put("s", "error");
		}
		json.put("m", m);
		json.put("puuid", puuid);
		json.put("d", d);
		
		if(!m.equals("执行完成")){
			SFKMessageVO sfkVO=new SFKMessageVO();
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			
			sfkVO.setPk_corp(corp);
			sfkVO.setBcBillno(bccode);
			sfkVO.setErpBillno("");
			sfkVO.setMessage(msg);
			try {
				iVOPersistence.insertVO(sfkVO);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return json;
	}

	
	/**
	 * 查询主子表连查  判断主键是否存在  若存在就修改  不存在则添加
	 * @param hvo
	 * @return 
	 */
	public List saveConfig(DJZBVO aggvo){
		List list=new ArrayList();
		JSONObject msg=new JSONObject();
		String djbhERP="";
		String erpDjh="";
		StringBuffer message= new StringBuffer();
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
			Object djh=new PfUtilBO().processAction("SAVE", "D2", new UFDate(new Date()).toString(), null, aggvo, null);
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
		//message.append("新增信息完成；");
		msg=exeReMsg(true,"新增信息完成；");
		list.add(message);
		list.add(msg);
		list.add(erpDjh);
		return list;
			
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
	 * 通过应付单存储的报支清单中间表获取对应的客商、银行账户信息、公司
	 * @param bzNo
	 * @return
	 * @throws DAOException
	 *//*
	private HashMap getCustInfo(String bzNo) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_corp, ksbm_cl as ks, gys_account as account ") 
		.append("   from itf_arap_bz bz ") 
		.append("  where billno_bz = '"+bzNo+"' ") 
		.append("    and nvl(dr, 0) = 0 ") ;
		BaseDAO dao = new BaseDAO();
		HashMap map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
		return map;
	}
	
	
	*//**
	 * 获取当前公司收款单单据类型
	 * @param corp
	 * @return
	 * @throws DAOException
	 *//*
	private String getYwbm(String corp) throws DAOException{
		BaseDAO dao = new BaseDAO();	
		StringBuffer sql = new StringBuffer();
		sql.append(" select djlxoid  from arap_djlx ") 
		.append(" where djlxjc = '收款单' ") 
		.append(" and dwbm = '"+corp+"' ") 
		.append(" and nvl(dr,0) = 0 ");
		Object djlx = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String djlxStr = djlx==null?"":djlx.toString();
		return djlxStr;
	}
	
	*//**
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
	}
	
	*/
	
	/**
	 * 获得表头信息，转换红字
	 * @param pzh
	 * @return
	 * @throws DAOException
	 */
	public DJZBHeaderVO getHeaderVO(String subcode) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from arap_djzb where scomment = '"+subcode+"' and nvl(dr,0) = 0 and bbje>0 order by ts asc ") ;
		BaseDAO dao = new BaseDAO();
		DJZBHeaderVO vo = (DJZBHeaderVO) dao.executeQuery(sql.toString(), new BeanProcessor(DJZBHeaderVO.class));
		if(vo!=null){
			vo.setBbje(vo.getBbje().multiply(-1));//本币金额//当前我国币种金额
			vo.setYbje(vo.getYbje().multiply(-1));//原币金额
			return vo;
		}else {
			return null;
		}
		

	}
	
	/**
	 * 获得表体信息，转换红字
	 * @param pzh
	 * @return
	 * @throws DAOException
	 */
	public DJZBItemVO getBodyVO(String vouchid) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from arap_djfb where vouchid = '"+vouchid+"' and nvl(dr,0) =0 order by ts asc ") ;
		BaseDAO dao = new BaseDAO();
		DJZBItemVO vo = (DJZBItemVO) dao.executeQuery(sql.toString(), new BeanProcessor(DJZBItemVO.class));
		/*if(vo!=null){
//			vo.setDfbbje(vo.getDfbbje().multiply(-1));//贷方本币金额
//			vo.setBbye(vo.getBbye().multiply(-1));//本币余额//标财付钱不需要处理余额
//			vo.setDfybje(vo.getDfybje().multiply(-1));//贷方原币金额
			return vo;
		}else {
			return null;
		}*/
		return vo;
	}
	
	/**
	 * 查询单据号
	 * @param djbhERP
	 * @return
	 */
	 private  String  queryDjh(String djbhERP){
		  String billno="";
		  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  String check=" select djbh from arap_djzb where vouchid='"+djbhERP+"' and nvl(dr,0)= 0";
	 	  try {
		      billno =  receiving.executeQuery(check, new ColumnProcessor())==null?"":(String)receiving.executeQuery(check, new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billno;  
	  }
}
