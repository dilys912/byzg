package nc.impl.uap.mdmreceive;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.corp.ICorp;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.CorpVO;
import nc.vo.pub.BusinessException;

/**
 * 接收MDM账套代码接口
 * wy
 * 2019/9/23
 * @author Administrator
 *
 */
public class MDMERP011 implements IxbusReceive{

	public JSONObject exeScript(JSONObject json) {
		String cmsg = checkJson(json);
		if(cmsg!=null){
			return returnMsg(false,cmsg, null, null);
		}
		JSONArray resarr = new JSONArray();//存储回执信息
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			CorpVO cvo = new CorpVO();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			
			cvo = changData(cvo, obj);
			JSONObject saveConfig = saveConfig(cvo);
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
	 * 判断CODE  有修改  无增加
	 * @param corpVO
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public JSONObject saveConfig(CorpVO vo){
		if(vo==null){
			return exeReMsg(false,"存储配置信息，未获取到数据");
		}
		String def8 = vo.getDef8();
		String sql = "select * from bd_corp where DEF8 = '"+def8+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CorpVO> lists = (List) dao .executeQuery(sql.toString(),new BeanListProcessor(CorpVO.class));
			if(lists.size()==0){	        
	            ICorp impl = (ICorp)NCLocator.getInstance().lookup(nc.itf.uap.bd.corp.ICorp.class.getName());
	            vo = impl.insertCorpReturnSelf((CorpVO)vo, null);
				return exeReMsg(true,"新增信息完成；");
			}else {
				for (int i = 0; i < lists.size(); i++) {
					CorpVO corpVO = lists.get(i);
					corpVO.setUnitname(vo.getUnitname());
					corpVO.setUnitshortname(vo.getUnitshortname());
					corpVO.setUnitcode(vo.getUnitcode());
					corpVO.setTs(null);
					ICorp impl = (ICorp)NCLocator.getInstance().lookup(nc.itf.uap.bd.corp.ICorp.class.getName());
					impl.update(corpVO);
				}
				return exeReMsg(true,"更新信息完成；");
			}
		} catch (BusinessException e) {
			//e.printStackTrace();
			return exeReMsg(false,"存储配置信息异常："+e.getMessage());
		}
	}

	
	/**
	 * 返回状态
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
	 * 数据转换
	 * @param corpVO
	 * @param obj
	 * @return
	 */
	public CorpVO changData(CorpVO corpVO,JSONObject obj){
		corpVO.setDef8(strF(obj,"CODE"));//账套代码
		corpVO.setUnitname(strF(obj,"DESC1"));//账套名称
		corpVO.setUnitshortname(strF(obj,"DESC2"));//账套简称
		corpVO.setUnitcode(strF(obj,"DESC3"));//组织机构码
		return corpVO;
	}
	
	/**
	 * 判断字符String 
	 * */
	private String strF(JSONObject j, String f) {
		String val = null;
		if (j.has(f)) {
			val = j.getString(f);
		}
		return val;
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
}
