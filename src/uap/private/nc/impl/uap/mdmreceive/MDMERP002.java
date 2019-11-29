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
 *  MDM�ӿ�Ա����Ϣ�������������
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
		JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
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
			
			PsnbasdocVO person = JsonToBVO(psn, obj);//ת������
			JSONObject saveConfig = saveConfig(person);
			if (saveConfig.getBoolean("s")) {
				msg.put("SYNSTATUS", 0);// 0���ɹ���
				msg.put("SYNRESULT", saveConfig.getString("m"));
			} else {
				msg.put("SYNSTATUS", 1);// 1��ʧ�ܣ�
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}
			resarr.add(msg);
		}
		return returnMsg(true,"ִ�����",puuid,resarr.toString());
	}
	/**
	 * ת������
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
	 * ��ѯ���ӱ�����  �ж������Ƿ����  �����ھ��޸�  �����������
	 * @param hvo
	 * @return 
	 */
	public JSONObject saveConfig(PsnbasdocVO vo){
		if(vo==null){
			return exeReMsg(false,"�洢������Ϣ��δ��ȡ������");
		}
		String vdef3 = vo.getVdef3();
		String query = "select * from bd_psnbasdoc where vdef3 = '"+vdef3+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<PsnbasdocVO> lists = (List) dao .executeQuery(query.toString(),new BeanListProcessor(PsnbasdocVO.class));
			if(lists.size()==0){
				dao.insertVO(vo);
				return exeReMsg(true,"������Ϣ��ɣ�");
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
	 * �ж��ַ�String
	 * */
	private String strFieldHas(JSONObject j, String f) {
		String val = null;
		if (j.has(f)) {
			val = j.getString(f);
		}
		return val;
	}

}
