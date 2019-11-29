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
 * ����MDM���״���ӿ�
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
		JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
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
				msg.put("SYNSTATUS", 0);// 0���ɹ���
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}else {
				msg.put("SYNSTATUS", 1);// 1��ʧ�ܣ�
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}	
			resarr.add(msg);
		}
		return returnMsg(true,"ִ�����",puuid,resarr.toString());
	}
	
	/**
	 * �ж�CODE  ���޸�  ������
	 * @param corpVO
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public JSONObject saveConfig(CorpVO vo){
		if(vo==null){
			return exeReMsg(false,"�洢������Ϣ��δ��ȡ������");
		}
		String def8 = vo.getDef8();
		String sql = "select * from bd_corp where DEF8 = '"+def8+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CorpVO> lists = (List) dao .executeQuery(sql.toString(),new BeanListProcessor(CorpVO.class));
			if(lists.size()==0){	        
	            ICorp impl = (ICorp)NCLocator.getInstance().lookup(nc.itf.uap.bd.corp.ICorp.class.getName());
	            vo = impl.insertCorpReturnSelf((CorpVO)vo, null);
				return exeReMsg(true,"������Ϣ��ɣ�");
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
				return exeReMsg(true,"������Ϣ��ɣ�");
			}
		} catch (BusinessException e) {
			//e.printStackTrace();
			return exeReMsg(false,"�洢������Ϣ�쳣��"+e.getMessage());
		}
	}

	
	/**
	 * ����״̬
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
	 * ����ת��
	 * @param corpVO
	 * @param obj
	 * @return
	 */
	public CorpVO changData(CorpVO corpVO,JSONObject obj){
		corpVO.setDef8(strF(obj,"CODE"));//���״���
		corpVO.setUnitname(strF(obj,"DESC1"));//��������
		corpVO.setUnitshortname(strF(obj,"DESC2"));//���׼��
		corpVO.setUnitcode(strF(obj,"DESC3"));//��֯������
		return corpVO;
	}
	
	/**
	 * �ж��ַ�String 
	 * */
	private String strF(JSONObject j, String f) {
		String val = null;
		if (j.has(f)) {
			val = j.getString(f);
		}
		return val;
	}
	
	
	/**
	 * ��װ��ִ
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
	 * У��json �ڵ�
	 * */
	private String checkJson(JSONObject json) {
		// check
		if (!json.has("ESB")) {
			return "JSON������δ��ȡ��ESB�ڵ�;";
		}
		if (!json.getJSONObject("ESB").has("DATA")) {
			return "JSON������δ��ȡ��DATA�ڵ�;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").has("DATAINFOS")) {
			return "JSON������δ��ȡ��DATAINFOS�ڵ�;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").has("DATAINFO")) {
			return "JSON������δ��ȡ��DATAINFO����;";
		}
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			if (!obj.has("CODE")) {
				return "JSON������"+(i+1)+"����δ��ȡ��CODE����;";
			}
			if (!obj.has("UUID")) {
				return "JSON������"+(i+1)+"����δ��ȡ��UUID����;";
			}
		}
		return null;
	}
}
