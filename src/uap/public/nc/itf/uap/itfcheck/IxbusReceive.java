package nc.itf.uap.itfcheck;

import nc.net.sf.json.JSONObject;

public interface IxbusReceive {

	/**
	 * 回执接口调用方法
	 * 
	 * @return JSONObject 回执示例
	 * JSONObject j = new JSONObject();
	 * j.put("s","success/error");
	 * j.put("m","成功消息/失败消息");
	 * j.put("c","返回的数据");
	 * */
	public JSONObject exeScript(JSONObject json);
}
