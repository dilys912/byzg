package nc.impl.ic.bgwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.execute.Executor;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.impl.ic.getKlrep.GetKlRepImpl;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

/**
 * �人����Ԥ������
 * @author pengjia
 *
 */
@SuppressWarnings("all")
public class WhBackTask implements IBusinessPlugin {

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

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String implementReturnMessage(Key[] arg0,final String pk_corp, UFDate arg2)
			throws BusinessException {
		String sql = "delete from ic_teamkl where pk_corp ='"+pk_corp+"'";
		new BaseDAO().executeUpdate(sql);
		final GetKlRepImpl kl = new GetKlRepImpl();
		// ��������
		Runnable thread = new Runnable() {
			public void run() {
				kl.getKlRep(pk_corp);
			}
		}; 
		new Executor(thread).start();
		return null;
	}

	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2,
			UFDate arg3) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
