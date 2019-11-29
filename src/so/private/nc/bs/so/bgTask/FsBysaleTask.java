package nc.bs.so.bgTask;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.execute.Executor;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.so.saleinvoiceImpl.QuerySaleCVMImpl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

public class FsBysaleTask implements IBusinessPlugin {

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

	public String implementReturnMessage(Key[] arg0, final String unitcode, UFDate arg2)
			throws BusinessException {
		final String corp = "10304";//edit by mcw
		String sql = "delete from so_salecvm where unitcode ='"+corp+"'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
		final QuerySaleCVMImpl Querysale  = new QuerySaleCVMImpl();
		Runnable run =new Runnable(){
			public void run() {
				try {
					Querysale.sendCVM(corp);
				} catch (DAOException e) {
					e.printStackTrace();
				}
			}
		};
		new  Executor(run).start(); 
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
