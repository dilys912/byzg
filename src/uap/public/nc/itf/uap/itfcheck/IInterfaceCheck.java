package nc.itf.uap.itfcheck;


import nc.net.sf.json.JSONObject;
import nc.vo.uap.itfcheck.XbusRestRequestEntity;



/**
 * NC��XBUS�ӿ�����У��ӿ�
 * 
 * */
public interface IInterfaceCheck {
	
	/**
	 * ��װУ�鷢�ͽӿ�����
	 * @param billcode Դ����
	 * @param JSONObject �еı�����bodylist�ֶ�
	 * @param Itfcode �ӿ������ĵ�����
	 * */
	public JSONObject assembleItfData(JSONObject json,String hcode,String bcode);
	
	/**
	 * ���У��ӿ�����
	 * @param xbusstr xbus��ִ�ĵ���
	 * @param Itfcode �ӿ������ĵ�����
	 * */
	public JSONObject disassembleItfData(String xbusstr,String itfcode);
	
	/**
	 * ��������
	 * @param type== JSON/XBUS
	 * */
	//edit by zwx 2019-11-17 ���ӹ�˾��ERP���ݺš���Ƶ��ݺŲ����¼��
//	public JSONObject sendRequest(JSONObject rjb,String type,String erpBillCode,String bcBillCode,String corp);
	public JSONObject sendRequest(JSONObject rjb,String type);
	
}
