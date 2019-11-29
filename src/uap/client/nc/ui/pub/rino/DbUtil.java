package nc.ui.pub.rino;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;

public class DbUtil {
	public static IPubDMO getDMO() {
		return (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
	}
}
