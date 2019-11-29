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
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;

/**
 * MDM�ӿڵ������������
 * @author wang
 * */
public class JWJCIF implements IxbusReceive{

	public JSONObject exeScript(JSONObject jsonarray) {

		// check json
		String cmsg = checkJson(jsonarray);
		if(cmsg!=null){
			return returnMsg(false,cmsg, null, null);
		}
		JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
		JSONArray jsonarrays = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = jsonarray.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			XmlAggEntity xmlAggEntity = new XmlAggEntity();
			XmlHEntity xmlHEntity = new XmlHEntity();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			xmlHEntity = JsonToHVO(xmlHEntity,obj);
			xmlAggEntity.setParentVO(xmlHEntity);
			
			JSONArray jrrys = obj.getJSONObject("MULTICODE").getJSONArray("VALUELIST");
			XmlBEntity[] xmlBEntitys = new XmlBEntity[jrrys.size()];
			for (int j = 0; j < jrrys.size(); j++) {
				XmlBEntity xmlBEntity = new XmlBEntity();
				JSONObject objs = jrrys.getJSONObject(j);
				xmlBEntity = JsonToBVO(xmlBEntity, objs);
				xmlBEntity.setPk_config_h(code);
				xmlBEntitys[j] = xmlBEntity;
			}
			xmlAggEntity.setChildrenVO(xmlBEntitys);

			JSONObject dj = saveConfig(xmlAggEntity);
			if (dj.getBoolean("s")) {
				JSONObject xj = createXml(xmlAggEntity, xmlHEntity.getItfcode());
				if (xj.getBoolean("s")) {
					msg.put("SYNSTATUS", 0);// 0���ɹ�������1��ʧ�ܣ�
					msg.put("SYNRESULT", dj.getString("m") + xj.getString("m"));
				} else {
					msg.put("SYNSTATUS", 1);// 0���ɹ�������1��ʧ�ܣ�
					msg.put("SYNRESULT", xj.getString("m"));
				}
			} else {
				msg.put("SYNSTATUS", 1);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
		}
		return returnMsg(true, "ִ�����", puuid,resarr.toString());
	}
	
	
	/**
	 * ����xml
	 * */
	public JSONObject createXml(XmlAggEntity aggvo, String xmlname) {
		if (aggvo == null) {
			return exeReMsg(false,"����xmlδ��ȡ������");
		}
		XmlHEntity hvo = (XmlHEntity) aggvo.getParentVO();
		XmlBEntity[] bvos = (XmlBEntity[]) aggvo.getChildrenVO();
		try {
			// 1������document����
			Document document = DocumentHelper.createDocument();
			// 2���������ڵ�root
			Element root = document.addElement("app");
			// 4�������ӽڵ㼰�ӽڵ����� ���ͷxml����
			ArrayList<String> eles_H = new ArrayList<String>();
			String[] hs = hvo.getAttributeNames();
			for (int i = 0; i < hs.length; i++) {
				eles_H.add(hs[i]);
			}
			eles_H.add("bodylist");

			String[] eles_B = bvos[0].getAttributeNames();
			for (int i = 0; i < eles_H.size(); i++) {
				Element element = root.addElement(eles_H.get(i));
				if ("bodylist".equals(element.getName())) {
					for (int k = 0; k < bvos.length; k++) {
						XmlBEntity bvo = bvos[k];
						Element row = element.addElement("row");
						for (int j = 0; j < eles_B.length; j++) {
							Element element2 = row.addElement(eles_B[j]);
							Object ovb = bvo.getAttributeValue(eles_B[j]);
							if (ovb != null) {
								element2.setText(ovb.toString());
							} else {
								element2.setText("");
							}
						}
					}
				} else {
					Object ov = hvo.getAttributeValue(eles_H.get(i));
					if (ov != null) {
						element.setText(ov.toString());
					} else {
						element.setText("");
					}
				}
			}
			// 5����������xml�ĸ�ʽ
			OutputFormat format = OutputFormat.createPrettyPrint();
			// ���ñ����ʽ
			format.setEncoding("UTF-8");
			// 6������xml�ļ�
			String filename = getNChomeXmlPath()+xmlname+".xml";
			File xmlfile = new File(filename);
			if (xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			XMLWriter writer = new XMLWriter(new FileOutputStream(xmlfile),format);
			// �����Ƿ�ת�壬Ĭ��ʹ��ת���ַ�
			writer.setEscapeText(false);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			//e.printStackTrace();
			return exeReMsg(false,"����xml�쳣��"+e.getMessage());
		}
		return exeReMsg(true,"����xml��ɣ�");
	}


	
	/**
	 * ��ѯ���ӱ�����  �ж������Ƿ����  �����ھ��޸�  �����������
	 * @param hvo
	 * @return 
	 */
	public JSONObject saveConfig(XmlAggEntity aggvo){
		if(aggvo==null){
			return exeReMsg(false,"�洢������Ϣ��δ��ȡ������");
		}
		XmlHEntity hvo = (XmlHEntity) aggvo.getParentVO();
		XmlBEntity[] bvos = (XmlBEntity[]) aggvo.getChildrenVO();
		String pk_itfconfig_h = hvo.getPk_config_h();
		String query = "select pk_config_h from itf_config_h where pk_config_h = '"+pk_itfconfig_h+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List lists = (List) dao .executeQuery(query.toString(),new ArrayListProcessor());
			if(lists.size()==0){
				dao.insertVOWithPK(hvo);
				dao.insertVOArrayWithPK(bvos);
				return exeReMsg(true,"������Ϣ��ɣ�");
			}else {
				dao.deleteVO(hvo);
				dao.deleteVOArray(bvos);
				dao.insertVOWithPK(hvo);
				dao.insertVOArrayWithPK(bvos);
				return exeReMsg(true,"������Ϣ��ɣ�");
			}
		} catch (BusinessException e) {
			//e.printStackTrace();
			return exeReMsg(false,"�洢������Ϣ�쳣��"+e.getMessage());
		}
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
			if (!obj.has("MULTICODE")) {
				return "JSON������"+(i+1)+"����δ��ȡ��MULTICODE����;";
			}
			if (!obj.getJSONObject("MULTICODE").has("VALUELIST")) {
				return "JSON������"+(i+1)+"����δ��ȡ��VALUELIST����;";
			}
			JSONArray jrrys = obj.getJSONObject("MULTICODE").getJSONArray("VALUELIST");
			for (int j = 0; j < jrrys.size(); j++) {
				JSONObject objs = jrrys.getJSONObject(j);
				if (!(objs.has("LISTCODE"))) {
					return "JSON������"+(i+1)+"��"+(j+1)+"�У�δ��ȡ��LISTCODE����;";
				}
			}
		}
		return null;
	}

	/**
	 * ת����ͷ����
	 * 
	 * @param oj
	 * @param hvo
	 * @return
	 * */
	private XmlHEntity JsonToHVO(XmlHEntity hvo, JSONObject obj) {

		hvo.setPk_config_h(strFieldHas(obj, "CODE"));
		hvo.setItfcode(strFieldHas(obj, "DESC1"));
		hvo.setItfname(strFieldHas(obj, "DESC2"));
		hvo.setItftype(strFieldHas(obj, "DESC3"));
		hvo.setItfaddress(strFieldHas(obj, "DESC4"));
		hvo.setSendsys(strFieldHas(obj, "DESC5"));
		hvo.setReceivesys(strFieldHas(obj, "DESC6"));
		hvo.setReceivedatabase(strFieldHas(obj, "DESC7"));
		hvo.setDatatype(strFieldHas(obj, "DESC8"));
		hvo.setVnote(strFieldHas(obj, "DESC9"));
		hvo.setMsgtoken(strFieldHas(obj, "DESC10"));
		hvo.setPassword(strFieldHas(obj, "DESC11"));
		hvo.setServiceid(strFieldHas(obj, "DESC12"));
		hvo.setSourceappcode(strFieldHas(obj, "DESC13"));
		hvo.setTempfield(strFieldHas(obj, "DESC14"));
		hvo.setVersion(strFieldHas(obj, "DESC15"));
		hvo.setPk_corp(strFieldHas(obj, "DESC16"));
		hvo.setDr(intFieldHas(obj, "DESC17"));

		return hvo;
	}

	/**
	 * ת����������
	 * 
	 * @param obj
	 * @param bvo
	 * @return
	 * */
	private XmlBEntity JsonToBVO(XmlBEntity bvo, JSONObject obj) {

		bvo.setPk_config_b(strFieldHas(obj,"LISTCODE"));
		bvo.setOrdernum(intFieldHas(obj, "DESC1"));
		bvo.setHeadorbady(strFieldHas(obj, "DESC2"));
		bvo.setFieldcode(strFieldHas(obj, "DESC3"));
		bvo.setFieldtype(strFieldHas(obj, "DESC4"));
		bvo.setFieldlength(intFieldHas(obj, "DESC5"));
		bvo.setFieldprecise(intFieldHas(obj, "DESC6"));
		bvo.setFieldexplain(strFieldHas(obj, "DESC7"));
		bvo.setSendsysfield(strFieldHas(obj, "DESC8"));
		bvo.setReceivesysfield(strFieldHas(obj, "DESC9"));
		bvo.setCheckcode(strFieldHas(obj, "DESC10"));
		bvo.setIsMustEnter(strFieldHas(obj, "DESC11"));
		bvo.setIsNull(strFieldHas(obj, "DESC12"));
		bvo.setPk_corp(strFieldHas(obj, "DESC13"));
		bvo.setDr(intFieldHas(obj, "DESC14"));

		return bvo;
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

	/**
	 * �ж��ַ�Integer
	 * */
	private Integer intFieldHas(JSONObject j, String f) {
		Integer val = null;
		if (j.has(f)) {
			val = Integer.valueOf(j.getString(f));
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
	 * ��ȡNChome��Ŀ¼·��
	 * */
	private String getNChomeXmlPath() {
		String homepath = System.getProperty("nc.server.location",System.getProperty("user.dir"));
		return homepath + "/resources/itfconfig/";
	}

}
