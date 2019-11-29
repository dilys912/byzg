package nc.bs.so.bgTask;

import org.apache.commons.logging.impl.Log4jFactory;
import org.apache.log4j.Logger;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.execute.Executor;
import nc.bs.logging.Log;
import nc.bs.logging.impl.log4j.Log4jLogger;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.so.saleinvoiceImpl.QuerySaleCVMImpl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
/**
 * 日新发出商品预警任务
 * @author mcw
 *
 */
public class RxsaleTask implements IBusinessPlugin {

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
		final String corp = "10318";//edit by mcw
		String sql = "delete from so_salecvm where unitcode ='"+corp+"'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
		final QuerySaleCVMImpl Querysale  = new QuerySaleCVMImpl();
		Runnable run =new Runnable(){//匿名内部类
			public void run() {
				try {
					Querysale.sendCVM(corp);
//					Thread.currentThread()返回的是当前运行的线程
					//判断线程是否停止
					if (Thread.currentThread().isInterrupted()){
						nc.bs.logging.Logger.debug("定时任务的状态已经是停止状态了，我要退出了"+Thread.currentThread().getName());
		            }
					//判断当前线程是否处于活动状态,“活动状态”是指线程处于运行或者准备开始运行的状态
					if (Thread.currentThread().isAlive()){
						nc.bs.logging.Logger.debug("定时任务中当前线程处于活动状态"+Thread.currentThread().getName());
		            }
					nc.bs.logging.Logger.debug("测试定时4");
				} catch (DAOException e) {
					nc.bs.logging.Logger.debug("测试定时3");
					e.printStackTrace();
				}
			}
		};
		nc.bs.logging.Logger.debug("测试定时1");
		/*nc.bs.logging.Logger.error("测试11");
	    nc.bs.logging.Logger.info("测试111");*/
		new  Executor(run).start(); 
		nc.bs.logging.Logger.debug("测试定时2");
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
