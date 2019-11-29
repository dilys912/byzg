package nc.impl.uap.mdmreceive;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import nc.bs.dao.BaseDAO;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.b02.AccsubjParentVO;
import nc.vo.bd.b120.AccidVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;

/**
 * MDM-->ERP    银行账户
 * @author gt
 * */
public class MDMERP015 implements IxbusReceive{

	public JSONObject exeScript(JSONObject jsonarray) {

		// check json
		String cmsg = checkJson(jsonarray);
		if(cmsg!=null){
			return returnMsg(false,cmsg, null, null);
		}
		JSONArray resarr = new JSONArray();//存储回执信息
		JSONArray jsonarrays = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			AccidVO accHVO= new AccidVO();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			accHVO = JsonToHVO(accHVO,obj);

			JSONObject dj = saveConfig(accHVO);
				if (dj.getBoolean("s")) {
					msg.put("SYNSTATUS", 0);// 0（成功）或者1（失败）
					msg.put("SYNRESULT", dj.getString("m"));
				} else {
					msg.put("SYNSTATUS", 1);// 0（成功）或者1（失败）
					msg.put("SYNRESULT", dj.getString("m"));
				}
			
			resarr.add(msg);
		}
		return returnMsg(true, "执行完成", puuid,resarr.toString());
	}
	

	
	/**
	 * 查询主子表连查  判断主键是否存在  若存在就修改  不存在则添加
	 * @param hvo
	 * @return 
	 */
	public JSONObject saveConfig(AccidVO hvo){
		if(hvo==null){
			return exeReMsg(false,"存储配置信息，未获取到数据");
		}
        
		String pk_acc = hvo.getAccidcode();
		String query = "select * from bd_accid where accidcode = '"+pk_acc+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<AccidVO> lists = (List<AccidVO>) dao.executeQuery(query.toString(),new BeanListProcessor(AccidVO.class));
			if(lists.size()==0){
				dao.insertVOWithPK(hvo);
				return exeReMsg(true,"新增信息完成；");
			}else {
			    if(lists.size()>1){
				      return exeReMsg(false,"存在多条数据无法更新");
				   }else{
					   AccidVO sVO= lists.get(0);
					   sVO.setAccidcode(hvo.getAccidcode());      
					   sVO.setAccidname(hvo.getAccidname());      
					   sVO.setPk_corp(hvo.getPk_corp());      
					   sVO.setPk_accbank(hvo.getPk_accbank());
					   sVO.setFrozenflag(hvo.getFrozenflag());
					   
					   
					   dao.updateVO(sVO);
					   return exeReMsg(true,"更新信息完成；");
					   
				   }
					
				}
		} catch (BusinessException e) {
			//e.printStackTrace();
			return exeReMsg(false,"存储配置信息异常："+e.getMessage());
		}
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
	 * 转换表头数据
	 * 
	 * @param oj
	 * @param hvo
	 * @return
	 * */
	private AccidVO JsonToHVO(AccidVO hvo, JSONObject obj) {

		hvo.setAccidcode(strFieldHas(obj, "CODE"));     //账户编码
		hvo.setAccidname(strFieldHas(obj, "DESC1"));    //账户名称
		hvo.setPk_corp(strFieldHas(obj, "DESC5"));      //公司
		hvo.setPk_accbank(strFieldHas(obj, "DESC6"));   //账号
		hvo.setFrozenflag(strFieldHas(obj, "DESC7"));   //账户状态
		hvo.setAcccl(0);                                //分类
		hvo.setFaccount(new UFBoolean(false));          
		hvo.setIsaccounting(new UFBoolean(false));      //是否最低限额控制

		
		hvo.setAccflag(0);                              //账户标志
		return hvo;
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
	 * 获取NChome根目录路径
	 * */
	private String getNChomeXmlPath() {
		String homepath = System.getProperty("nc.server.location",System.getProperty("user.dir"));
		return homepath + "/resources/itfconfig/";
	}

}
