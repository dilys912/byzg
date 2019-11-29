package nc.bs.ic.alert;

import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

public class WarningAlert implements IBusinessPlugin {

	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] akey, String s,
			UFDate ufdate) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String implementReturnMessage(Key[] akey, String s, UFDate ufdate)
			throws BusinessException {
		return "Ô¤¾¯Ö´ÐÐÖÐ";
	}

	public Object implementReturnObject(Key[] akey, String s, UFDate ufdate)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] akey, String s, String s1,
			UFDate ufdate) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
