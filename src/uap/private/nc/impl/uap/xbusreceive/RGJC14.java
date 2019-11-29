package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.UpToDate;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
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
 * 收款信息接收应答
 *
 */
public class RGJC14  implements IxbusReceive {
	
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
			djzbHeader.setPzglh(0);
			djzbHeader.setDjdl("sk");
			String subcode = jsons.getString("SUB_RECEIPT_CODE");//子收条号，用于红冲关联
			String bzNo = jsons.getString("RECEIPT_CODE");//收款收条号
			String daterq = jsons.getString("VOUCHER_DATE").toString();
			String corpCode =  jsons.getString("COMPANY_CODE")==null?"":jsons.getString("COMPANY_CODE");//账套
			if(corpCode.equals("")){
				message.append("未获取到账套编码;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未获取到账套编码", null,null);
			}
			String corpid = null;
			try {
				corpid = getCorp(corpCode);
			} catch (DAOException e1) {
				message.append("ERP查询账套"+corpCode+"对应的公司异常："+e1.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP查询账套对应的公司异常："+e1.getMessage(), null,null);
			}
			if(corpid.equals("")){
				message.append("ERP未查询到当前账套编码："+corpCode+"对应的公司;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP未查询到当前账套编码："+corpCode+"对应的公司", null,null);
			}
			if(daterq == null || daterq.length()==0){
				daterq=dqsj.substring(0, 10);
			}else{
				daterq = daterq.substring(0, 4)+"-"+daterq.substring(4, 6)+"-"+daterq.substring(6, 8);
			}
			UFDate skrq = new UFDate(daterq);//付款凭证日期
			String yearStr = daterq.substring(0, 4);
			String monthStr = daterq.substring(5, 7);
			String kjqj = null;
			
			try {
				kjqj = getPeriodPk(yearStr);
				
			} catch (DAOException e2) {
				message.append("查询ERP配置会计期间："+kjqj+"异常"+","+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
			
			/*HashMap map;
			try {
				map = getCustInfo(bzNo);
			} catch (DAOException e) {
				return returnMsg(false, "查询收款单对应的客商及银行账户信息异常", null,null);
			}
			if(map==null){
				return returnMsg(false, "未查询到收款单对应的客商及银行账户信息", null,null);
			}
			String corp = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
			String ks = map.get("ks")==null?"":map.get("ks").toString();
			String account = map.get("account")==null?"":map.get("account").toString();
			*/
//			djzbHeader.setDjbh(bzNo);//单据号 
			
			djzbHeader.setScomment(subcode);
			djzbHeader.setDjkjnd(String.valueOf(skrq.getYear()));
			djzbHeader.setDjkjqj(String.valueOf(skrq.getMonth()));
			djzbHeader.setDjlxbm("D2");
			djzbHeader.setQcbz(new UFBoolean(false));
			
			String ywbm;
			try {
				ywbm = getYwbm(corpid);
			} catch (DAOException e) {
				message.append("查询收款单单据类型异常："+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "查询收款单单据类型异常："+e.getMessage(), null,null);
			}
			if(ywbm.equals("")){
				message.append("ERP未查询到当前账套："+corpCode+"对应的收款单业务主键;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP未查询到当前账套："+corpCode+"对应的收款单业务主键", null,null);
			}
			djzbHeader.setYwbm(ywbm);//单据类型 //需要根据公司来查询得到对应的公司的收款单主键
			djzbHeader.setDjrq(skrq);//单据日期
			djzbHeader.setDwbm(corpid);//公司
			
//			String voucher_no = jsons.getString("VOUCHER_NO");//凭证号
//			djzbHeader.setVouchertypeno(voucher_no);
			djzbHeader.setEffectdate(skrq);//起效日期
			String kscode = jsons.getString("SETTLE_USER_CODE")==null?"":jsons.get("SETTLE_USER_CODE").toString();//结算用户编码
			if(kscode.equals("")){
				message.append("未获结算用户编码编码;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未获结算用户编码编码", null,null);
			}
			String ksid = null;
			try {
				ksid = getKsid(kscode,corpid);//根据MDM客商编码获取
			} catch (DAOException e) {
				message.append("ERP查询客商主键异常："+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "ERP查询客商主键异常："+e.getMessage(), null,null);
			}	
			if(ksid.equals("")){
				message.append("ERP未查询到当前客商编码："+kscode+"对应的主键，请分配到当前公司"+corpid+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP未查询到当前客商编码："+kscode+"对应的主键，请分配到当前公司"+corpid+"", null,null);
			}
			djzbHeader.setKsbm_cl(ksid);//供应商  //根据报支清单号获得应付单中的客商
		
			String bankcode = jsons.getString("BANK_CODE")==null?"":jsons.get("BANK_CODE").toString();//银行账户编码
			if(bankcode.equals("")){
				message.append("未获取到银行账户编码;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未获取到银行账户编码", null,null);
			}
			String accid = null;
			try {
				accid = getAccountid(bankcode,corpid);
			} catch (DAOException e1) {
				message.append("ERP查询银行账户主键异常："+e1.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP查询银行账户主键异常："+e1.getMessage(), null,null);
			}
			if(accid.equals("")){
				message.append("ERP未查询到当前银行账户编码："+bankcode+"对应的主键;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP未查询到当前银行账户编码："+bankcode+"对应的主键", null,null);
			}
//			String bankname = jsons.getString("BANK_NAME")==null?"":jsons.get("BANK_NAME").toString();//银行名称
			djzbHeader.setAccountid(accid);//银行账户 根据
			
			djzbHeader.setYbje(new UFDouble(jsons.getString("PAPER_AMT")));//原币金额	
			String bz = jsons.getString("CURRENCY_CODE");
			HashMap bzMap;
			try {
				bzMap = getBzInfo(bz, kjqj,kjyf);
			} catch (DAOException e) {
				message.append("查询币种"+bz+"对应人民币汇率信息异常;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "查询币种"+bz+"对应人民币汇率信息异常", null,null);
			}
			if(bzMap==null){
				message.append("未查询到"+yearStr+monthStr+"期间币种"+bz+"对应人民币汇率信息;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "未查询到"+yearStr+monthStr+"期间币种"+bz+"对应人民币汇率信息", null,null);
			}
			String bzpk = bzMap.get("bz")==null?"":bzMap.get("bz").toString();
			String rate = bzMap.get("rate")==null?"":bzMap.get("rate").toString();
			djzbHeader.setBbhl(new UFDouble(rate));//本币汇率 // 需要根据币种查询当前月份汇率
			
			djzbHeader.setBzbm(bzpk);//币种 //需要mdm分发的转换
			//表体的VO插入
			DJZBItemVO djzbItemVO = new DJZBItemVO();
			djzbItemVO.setDwbm(corpid);//公司
			djzbItemVO.setFx(-1);//(单据辅表)方向
			djzbItemVO.setWldx(new Integer(0));//往来对象 0=客户
			djzbItemVO.setBbhl(new UFDouble(rate));//本币汇率 // 需要根据币种查询当前月份汇率
			djzbItemVO.setKsbm_cl(ksid);
			djzbItemVO.setAccountid(accid);
			djzbItemVO.setBzbm(bzpk);//币种 //需要mdm分发的转换
			if(bzpk.equals("00010000000000000001")){//人民币
				djzbHeader.setBbje(new UFDouble(jsons.getString("PAPER_AMT")));//本币金额//当前我国币种金额
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAPER_AMT")));//原币余额
				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//本币余额
				
				djzbItemVO.setDfbbje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方本币金额
				djzbItemVO.setDfybje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方原币金额 
				djzbItemVO.setDfybwsje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方原币无税金额
				djzbItemVO.setDfbbwsje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方本币无税金额
				
				
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//本币余额 需要根据账户余额取数
			}else{//非人民币，本币金额需要转换
				djzbHeader.setBbje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//本币金额//当前我国币种金额
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAPER_AMT")));//原币余额
				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//本币余额
				
				djzbItemVO.setDfbbje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//贷方本币金额
				djzbItemVO.setDfybje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方原币金额
				djzbItemVO.setDfybwsje(new UFDouble(jsons.getString("PAPER_AMT")));//贷方原币无税金额
				djzbItemVO.setDfbbwsje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//贷方本币无税金额
				
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//本币余额 
			}

			
			djzbItemVO.setQxrq(skrq);//起效日期
			djzbItemVO.setBilldate(skrq);
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
				message.append("保存收款单异常;");
				msg.put("SYNSTATUS", 1);// 0（成功）或者1（失败）
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
		sfkVO.setDr("0");
		sfkVO.setTs(daterq);
		sfkVO.setPk_corp(corpid);
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
		.append("    and nvl(dr, 0) = 0 ") ;
		BaseDAO dao = new BaseDAO();
		HashMap map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
		return map;
	}
	
	
	/**
	 * 获取当前公司收款单单据类型
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
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
	 * 根据账套编码获得对应公司主键
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	public String getCorp(String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_corp from bd_corp ") 
		.append(" where def8 = '"+corp+"' ") 
		.append(" and nvl(dr,0) = 0 ") ;
		BaseDAO dao = new BaseDAO();	
		Object corpid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String corpStr = corpid==null?"":corpid.toString();
		return corpStr;

	}
	
	/**
	 * 根据集团客商编码、公司获得ERP客商id
	 * @param kscode
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	public String getKsid(String kscode,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct man.pk_cumandoc as cmanid ") 
		.append("   from bd_cubasdoc  doc ") 
		.append("  left join  bd_cumandoc man  on  doc.pk_cubasdoc=man.pk_cubasdoc ") 
		.append("  where man.PK_CORP = '"+corp+"' ") 
		.append("    and doc.custcode = '"+kscode+"' ") 
		.append("    and nvl(man.dr, 0) = 0 ") 
		.append(" and (man.custflag = '1' OR man.custflag = '3') ") 
		.append("    and nvl(doc.dr, 0) = 0 ") ;
		BaseDAO dao = new BaseDAO();	
		Object ksid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String ksidStr = ksid==null?"":ksid.toString();
		return ksidStr;

	}
	
	/**
	 * 根据MDM银行账号编码获得
	 * @param accode
	 * @return
	 * @throws DAOException
	 */
	public String getAccountid(String accode,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_accid from bd_accid where def11 = '"+accode+"' and nvl(dr,0) = 0 and pk_corp = '"+corp+"'") ;
		BaseDAO dao = new BaseDAO();	
		Object accid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String accidStr = accid==null?"":accid.toString();
		return accidStr;

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

	
	
}
