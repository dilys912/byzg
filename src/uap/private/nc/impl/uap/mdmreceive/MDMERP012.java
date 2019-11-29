package nc.impl.uap.mdmreceive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.accperiod.IPrivateAccperiodAccessor;
import nc.itf.uap.bd.corp.ICorp;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.b21.AdjustrateVO;
import nc.vo.bd.b21.CurrinfoExAggVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.bd.b21.CurrrateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 接收MDM汇率代码接口
 * wy
 * 2019/10/09
 * @author Administrator
 *
 */
public class MDMERP012 implements IxbusReceive{
 	public JSONObject exeScript(JSONObject json) {
		String checkJson = checkJson(json);
		if(checkJson!=null){
			return returnMsg(false,checkJson, null, null);
		}
		JSONArray resarr = new JSONArray();//存储回执信息
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			//聚合表   CurrinfoExAggVO
			CurrinfoExAggVO aggVO = new CurrinfoExAggVO();
			String tableCodes[] = aggVO.getTableCodes();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			/*//主表CurrinfoVO
			CurrinfoVO infoVO = new CurrinfoVO();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			infoVO = jsonToInfoVO(infoVO, obj);
			aggVO.setParentVO(infoVO);
			
			AdjustrateVO[] ratearr = new AdjustrateVO[1];//期间汇率
			AdjustrateVO ratevo = new AdjustrateVO();
			ratevo = jsonToQjRateVO(ratevo, obj);	
			
			//子表2  CurrrateVO  
			CurrrateVO[] rateVO = new CurrrateVO[jsonarrays.size()];
			for (int j = 0; j < rateVO.length; j++) {
				CurrrateVO rVO = new CurrrateVO();
				rVO = jsonTorateVO(rVO, obj);	
				rateVO[j] = rVO;
			}
			aggVO.setChildrenVO(rateVO);
			aggVO.setTableVO(tableCodes[1], rateVO);*/
//			JSONObject saveConfig = saveConfig(aggVO,tableCodes);
			JSONObject saveConfig = jsonToQjRateVO(obj);
			if(saveConfig.getBoolean("s")){
				msg.put("SYNSTATUS", 0);// 0（成功）
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}else {
				msg.put("SYNSTATUS", 1);// 1（失败）
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}	
			resarr.add(msg);
		}
		return returnMsg(true,"执行完成",puuid,resarr.toString());
	}
 	
 	/**
 	 * 判断CODE是存在  存在则修改  不存在则添加
 	 * @param aggVO
 	 * @param tableCodes
 	 * @return
 	 *//*
	@SuppressWarnings("unchecked")
	public JSONObject saveConfig(CurrinfoExAggVO aggVO,String tableCodes[]){
		if(aggVO == null){
			return exeReMsg(false,"存储配置信息，未获取到数据");
		}
		CurrinfoVO hvo = (CurrinfoVO)aggVO.getParentVO();
		CurrrateVO[] bvo = (CurrrateVO[]) aggVO.getTableVO(tableCodes[1]);
		
		String judgeCurr = judgeCurrType(hvo.getPk_currtype());
		if(judgeCurr == null){
			return exeReMsg(false,"找不到该源币种；");
		}
		hvo.setPk_currtype(judgeCurr);
		String judgeOppCurrType = judgeOppCurrType(hvo.getOppcurrtype());
		if(judgeOppCurrType == null){
			return exeReMsg(false,"找不到该目标币种；");
		}
		hvo.setOppcurrtype(judgeOppCurrType);
		
		String code = bvo[0].getCode();
		String sql = "select * from bd_currrate where code = '"+code+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CurrrateVO> list = (List) dao.executeQuery(sql.toString(),new BeanListProcessor(CurrrateVO.class));
			if(list.size()==0){
				dao.insertVO(hvo);
				bvo[0].setPk_currinfo(hvo.getPk_currinfo());
				dao.insertVOArrayWithPK(bvo);
				return exeReMsg(true,"新增信息完成；");
			}else {
				CurrrateVO cVO = null;
				for (int i = 0; i < list.size(); i++) {
					cVO = list.get(i);
					cVO.setCode(bvo[0].getCode());
					cVO.setRate(bvo[0].getRate());
					cVO.setRatedate(bvo[0].getRatedate());
					cVO.setRatetype(bvo[0].getRatetype());
					cVO.setOpprate(bvo[0].getOpprate());
					dao.updateVO(cVO);
				}
				String pk_currinfo = cVO.getPk_currinfo();				
				//根据bd_currrate 中的 pk_currinfo 来修改 bd_currinfo 中 主键为 pk_currinfo 的数据
				UpdateDataVO(pk_currinfo,hvo);
				return exeReMsg(true,"更新信息完成；");
			}
		} catch (BusinessException e) {
			return exeReMsg(false,"存储配置信息异常："+e.getMessage());
		}
	}
	
	*//**
	 * 主表数据
	 * @param infoVO
	 * @param obj
	 * @return
	 *//*
	public CurrinfoVO jsonToInfoVO(CurrinfoVO infoVO, JSONObject obj){
		infoVO.setPk_currtype(strFieldHas(obj, "DESC1"));//源币种
		infoVO.setOppcurrtype(strFieldHas(obj,"DESC2"));//目的币种
		infoVO.setDr(intFieldHas(obj,"DESC7"));//
		return infoVO;
	}*/
	
	/**
	 * 子表数据  CurrrateVO
	 * @param rateVO
	 * @param obj
	 * @return
	 *//*
	String strFieldHas = null;
	public CurrrateVO jsonTorateVO(CurrrateVO rateVO, JSONObject obj){
		rateVO.setCode(strFieldHas(obj, "CODE"));//编码
		strFieldHas = strFieldHas(obj, "DESC3");
		String a = strFieldHas.substring(0,4);	
		String g = strFieldHas.substring(4,6);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nyr = sdf.format(date).substring(8, 10);	
		String d = a+"-"+g+"-"+nyr;
		rateVO.setRatedate(new UFDate(d));//汇率月份  DESC3
		rateVO.setRatetype(strFieldHas(obj, "DESC4"));//汇率类型
		rateVO.setRate(new UFDouble(strFieldHas(obj, "DESC5")));//汇率就是记账汇率    
		rateVO.setOpprate(new UFDouble(strFieldHas(obj, "DESC6")));//反向汇率
		return rateVO;
	}*/
	
	/**
	 * 子表数据  CurrrateVO
	 * @param rateVO
	 * @param obj
	 * @return
	 */
	public JSONObject jsonToQjRateVO(JSONObject obj){
		//主表
		CurrinfoVO infoVO = new CurrinfoVO();
		if(strFieldHas(obj, "DESC1")==null||strFieldHas(obj, "DESC1").toString().equals("")){
			return exeReMsg(false,"未获取到DESC1对应值!");
		}
		String pkybz="";
		try {
			pkybz = judgeCurrType(strFieldHas(obj, "DESC1"));
		} catch (DAOException e) {
			return exeReMsg(false,"查询DESC1币种异常"+e.getMessage());
		}
		if(pkybz.equals("")){
			return exeReMsg(false,"未查询到DESC1币种"+strFieldHas(obj, "DESC1")+"，请先同步币种");
		}
		infoVO.setPk_currtype(pkybz);//源币种
		
		if(strFieldHas(obj, "DESC2")==null||strFieldHas(obj, "DESC2").toString().equals("")){
			return exeReMsg(false,"未获取到DESC2对应值!");
		}
		String mdbz="";
		try {
			mdbz = judgeCurrType(strFieldHas(obj, "DESC2"));
		} catch (DAOException e) {
			return exeReMsg(false,"查询DESC2币种异常"+e.getMessage());
		}
		if(mdbz.equals("")){
			return exeReMsg(false,"未查询到DESC2币种"+strFieldHas(obj, "DESC2")+"，请先同步币种");
		}
		infoVO.setOppcurrtype(mdbz);//目的币种
//		infoVO.setDr(intFieldHas(obj,"DESC7"));//
		
		//月汇率子表
		AdjustrateVO rateVO = new AdjustrateVO();
		rateVO.setPk_accperiodscheme("0001AA00000000000001");//会计期间方案主键
		if(strFieldHas(obj, "DESC5")==null||strFieldHas(obj, "DESC5").toString().equals("")){
			return exeReMsg(false,"未获取到DESC5对应值!");
		}
		if(strFieldHas(obj, "DESC3")==null||strFieldHas(obj, "DESC3").toString().equals("")){
			return exeReMsg(false,"未获取到DESC3对应值!");
		}
		if(strFieldHas(obj, "DESC3").toString().length()<6){
			return exeReMsg(false,"DESC3汇率月份长度不能小于6位！");
		}
		rateVO.setAdjustrate(new UFDouble(strFieldHas(obj, "DESC5")));//汇率
		rateVO.setPk_corp("0001");//暂时默认集团统一管理
		String dateStr = strFieldHas(obj, "DESC3");
		String yearStr = dateStr.substring(0,4);//截取年
		String monthStr = dateStr.substring(4,6);//截取
		String pkacc="";
		try {
			pkacc = getPeriodPk(yearStr);
		} catch (DAOException e2) {
			return exeReMsg(false,"查询当前年份对应的会计期间异常"+e2.getMessage());
		}
		if(pkacc.equals("")){
			return exeReMsg(false,"未查询到当前年份对应的会计期间！需要ERP维护!");
		}
		String pkmonth="";
		try {
			pkmonth = getPeriodMonthPk(monthStr, pkacc);
		} catch (DAOException e1) {
			return exeReMsg(false,"查询当前月份对应的会计月份异常"+e1.getMessage());
		}
		if(pkmonth.equals("")){
			return exeReMsg(false,"未查询到当前月份对应的会计月份！需要ERP维护!");
		}
		rateVO.setRatemonth(monthStr);
		rateVO.setPk_accperiod(pkacc);
		rateVO.setPk_accperiodmonth(pkmonth);
		String pkcurrinfo="";
		try {
			pkcurrinfo = getPkCurr(pkybz,mdbz);
		} catch (DAOException e) {
			return exeReMsg(false,"查询源币种与目标币种对应的汇率信息异常！"+e.getMessage());
		}
		if(pkcurrinfo.length()>0){
			infoVO.setPk_currinfo(pkcurrinfo);
			rateVO.setPk_currinfo(pkcurrinfo);	
			rateVO.setDr(intFieldHas(obj,"DESC7"));//
			String pkrate;
			try {
				pkrate = isExistRate(rateVO);
			} catch (DAOException e) {
				return exeReMsg(false,"查询期间汇率异常！"+e.getMessage());
			}
			BaseDAO dao = new BaseDAO();
			if(pkrate.length()==0){
				try {
					dao.insertVO(rateVO);
				} catch (DAOException e) {
					return exeReMsg(false,"插入汇率异常！"+e.getMessage());
				}
			}else{
				rateVO.setPrimaryKey(pkrate);
				try {
					dao.updateVO(rateVO);
				} catch (DAOException e) {
					return exeReMsg(false,"更新汇率异常！"+e.getMessage());
				}
			}
		}else{
			return exeReMsg(false,"请先在ERP汇率维护源币种与目标币种对应的关系！");//只负责更新汇率
		}
		return exeReMsg(true,"更新汇率成功！");		
		
	}

	/**
	 * 校验json 节点
	 * */
	private String checkJson(JSONObject json) {
		// check
		if (!json.has("ESB")) {
			return "JSON解析：未获取到ESB节点;";
		}
		if (!json.getJSONObject("ESB").has("DATA")) {
			return "JSON解析：未获取到DATA节点;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").has("DATAINFOS")) {
			return "JSON解析：未获取到DATAINFOS节点;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").has("DATAINFO")) {
			return "JSON解析：未获取到DATAINFO数据;";
		}
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			if (!obj.has("CODE")) {
				return "JSON解析第"+(i+1)+"条：未获取到CODE数据;";
			}
			if (!obj.has("UUID")) {
				return "JSON解析第"+(i+1)+"条：未获取到UUID数据;";
			}
		}
		return null;
	}
	
	/**
	 * 返回信息
	 * @param s
	 * @param m
	 * @return
	 */
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
	 * 修改 bd_currinfo 信息
	 * @param pk_currinfo
	 * @param cvo
	 * @return
	 */
	public JSONObject UpdateDataVO(String pk_currinfo,CurrinfoVO cvo){
		if(pk_currinfo == null || cvo == null){
			return exeReMsg(false,"存储配置信息，未获取到数据；");
		}
		String sql = "select * from bd_currinfo where pk_currinfo = '"+pk_currinfo+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CurrinfoVO> list = (List) dao.executeQuery(sql.toString(),new BeanListProcessor(CurrinfoVO.class));
			if(list == null){
				return exeReMsg(false,"存储配置信息，未获取到pk_currinfo数据；");
			}else {
				for (int i = 0; i < list.size(); i++) {
					CurrinfoVO hvo = list.get(i);
					hvo.setPk_currinfo(pk_currinfo);
					hvo.setOppcurrtype(cvo.getOppcurrtype());
					hvo.setPk_currtype(cvo.getPk_currtype());
					hvo.setDr(cvo.getDr());
					dao.updateVO(hvo);
				}
			} 			
		} catch (DAOException e) {
			return exeReMsg(false,"存储配置信息异常："+e.getMessage());
		}
		return null;
	}
	


	/**
	 * 判断源币种
	 * @param ybz
	 * @return
	 * @throws DAOException 
	 */
	public String judgeCurrType(String ybz) throws DAOException{
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_currtype from bd_currtype where currtypecode = '"+ybz+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkcurtype = obj==null?"":obj.toString();
		
		return pkcurtype;		
	}
	
	/**
	 * 判断目标币种
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String judgeOppCurrType(String bbbz) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_currtype from bd_currtype where currtypecode = '"+bbbz+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkcurtype = obj==null?"":obj.toString();
		
		return pkcurtype;		
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
	 * 获取会计期间主键
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
	 * 获取源币种、目标币种对应主键
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPkCurr(String ysbz,String mbbz) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append("  select pk_currinfo ") 
		.append("           from bd_currinfo ") 
		.append("          where oppcurrtype = '"+mbbz+"' ") 
		.append("            and pk_currtype = '"+ysbz+"' ") 
		.append("            and pk_corp = '0001' ") 
		.append("            and nvl(dr, 0) = 0 ") ;

		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkCurr = obj==null?"":obj.toString();
		
		return pkCurr;		
	}
	
	
	/**
	 * 判断调整汇率是否存在
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String isExistRate(AdjustrateVO vo) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_adjustrate from bd_adjustrate ") 
		.append(" where pk_accperiod = '"+vo.getPk_accperiod()+"' ") 
		.append(" and pk_accperiodmonth = '"+vo.getPk_accperiodmonth()+"' ") 
		.append(" and pk_corp = '0001' ") 
		.append(" and ratemonth = '"+vo.getRatemonth()+"' ") ;


		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkrate = obj==null?"":obj.toString();
		
		return pkrate;		
	}
	
	
	
}
