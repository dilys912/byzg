package nc.impl.uap.mdmreceive;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.psndoc.PsnbasdocVO;
import nc.vo.pub.BusinessException;

/**
 *  MDM接口员工信息档案接收入口类
 *  wy
 *  2019/9/17
 *  PsnbasdocVO
 * @author Administrator
 *
 */
public class MDMERP002 implements IxbusReceive {

	public JSONObject exeScript(JSONObject jsonarray) {
		String cmsg = checkJson(jsonarray);
		if(cmsg!=null){
			return returnMsg(false,cmsg, null, null);
		}
		JSONArray resarr = new JSONArray();//存储回执信息
		JSONArray jsonarrays = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			PsnbasdocVO psn = new PsnbasdocVO();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			
			PsnbasdocVO person = JsonToBVO(psn, obj);//转换数据
			JSONObject saveConfig = saveConfig(person);
			if (saveConfig.getBoolean("s")) {
				msg.put("SYNSTATUS", 0);// 0（成功）
				msg.put("SYNRESULT", saveConfig.getString("m"));
			} else {
				msg.put("SYNSTATUS", 1);// 1（失败）
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}
			resarr.add(msg);
		}
		return returnMsg(true,"执行完成",puuid,resarr.toString());
	}
	/**
	 * 转换数据
	 * @param vo
	 * @param obj
	 * @return
	 */
	private PsnbasdocVO JsonToBVO(PsnbasdocVO vo, JSONObject obj) {
		vo.setVdef3(strFieldHas(obj,"CODE"));
		vo.setPsnname(strFieldHas(obj,"DESC1"));
		vo.setUsedname(strFieldHas(obj,"DESC4"));
		vo.setSex(strFieldHas(obj, "DESC5"));
		vo.setId(strFieldHas(obj,"DESC6"));
		vo.setOfficephone(strFieldHas(obj, "DESC12"));
		vo.setEmail(strFieldHas(obj,"DESC13"));
		vo.setMobile(strFieldHas(obj,"DESC19"));
		//vo.setPk_psnbasdoc("123456678");
		vo.setPk_corp("null");
		return vo;
	}


	/**
	 * 查询主子表连查  判断主键是否存在  若存在就修改  不存在则添加
	 * @param hvo
	 * @return 
	 */
	public JSONObject saveConfig(PsnbasdocVO vo){
		if(vo==null){
			return exeReMsg(false,"存储配置信息，未获取到数据");
		}
		String vdef3 = vo.getVdef3();
		String query = "select * from bd_psnbasdoc where vdef3 = '"+vdef3+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<PsnbasdocVO> lists = (List) dao .executeQuery(query.toString(),new BeanListProcessor(PsnbasdocVO.class));
			if(lists.size()==0){
				dao.insertVO(vo);
				return exeReMsg(true,"新增信息完成；");
			}else {
				for (int i = 0; i < lists.size(); i++) {
					PsnbasdocVO psnbasdocVO = lists.get(i);
					psnbasdocVO.setVdef3(vo.getVdef3());
					psnbasdocVO.setPsnname(vo.getPsnname());
					psnbasdocVO.setUsedname(vo.getUsedname());
					psnbasdocVO.setSex(vo.getSex());
					psnbasdocVO.setId(vo.getId());
					vo.setOfficephone(vo.getOfficephone());
					psnbasdocVO.setEmail(vo.getEmail());
					psnbasdocVO.setMobile(vo.getMobile());
					dao.updateVO(psnbasdocVO);
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

}
