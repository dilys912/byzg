package nc.itf.uap.itfcheck;

import nc.net.sf.json.JSONObject;

public interface IxbusReceive {

	/**
	 * ��ִ�ӿڵ��÷���
	 * 
	 * @return JSONObject ��ִʾ��
	 * JSONObject j = new JSONObject();
	 * j.put("s","success/error");
	 * j.put("m","�ɹ���Ϣ/ʧ����Ϣ");
	 * j.put("c","���ص�����");
	 * */
	public JSONObject exeScript(JSONObject json);
}
