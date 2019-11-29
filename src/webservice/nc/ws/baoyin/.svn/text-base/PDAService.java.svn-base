package nc.ws.baoyin;

import nc.bs.framework.common.NCLocator;
import nc.itf.ws.baoyin.IPDAServiceExecutor;

/**
 * @author Administrator
 * @date Jan 24, 2014 4:56:29 PM
 * @type nc.ws.baoyin.PDAServicea
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
public class PDAService {

	public String execute(String param) throws Exception {
		IPDAServiceExecutor executor = (IPDAServiceExecutor) NCLocator.getInstance().lookup(IPDAServiceExecutor.class.getName());
		return (String)executor.execute(param);
	}

}
