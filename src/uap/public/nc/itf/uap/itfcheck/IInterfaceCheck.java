package nc.itf.uap.itfcheck;


import nc.net.sf.json.JSONObject;
import nc.vo.uap.itfcheck.XbusRestRequestEntity;



/**
 * NC与XBUS接口数据校验接口
 * 
 * */
public interface IInterfaceCheck {
	
	/**
	 * 组装校验发送接口数据
	 * @param billcode 源单号
	 * @param JSONObject 中的表体用bodylist字段
	 * @param Itfcode 接口配置文档名称
	 * */
	public JSONObject assembleItfData(JSONObject json,String hcode,String bcode);
	
	/**
	 * 拆解校验接口数据
	 * @param xbusstr xbus回执的电文
	 * @param Itfcode 接口配置文档名称
	 * */
	public JSONObject disassembleItfData(String xbusstr,String itfcode);
	
	/**
	 * 发送请求
	 * @param type== JSON/XBUS
	 * */
	//edit by zwx 2019-11-17 增加公司、ERP单据号、标财单据号插入记录表
//	public JSONObject sendRequest(JSONObject rjb,String type,String erpBillCode,String bcBillCode,String corp);
	public JSONObject sendRequest(JSONObject rjb,String type);
	
}
