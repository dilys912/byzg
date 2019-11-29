/**
 * 
 */
package nc.impl.bgjs.pub;

import java.util.ArrayList;
import java.util.HashMap;

import nc.itf.bgjs.pub.IBGJSITF;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @author zhwj
 *
 */
public class BGJSImpl implements IBGJSITF{

	/**
	 * 
	 */
	public BGJSImpl() {
		// TODO Auto-generated constructor stub
	}

	public Object getObjValue(Object obj) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}


	// 保存VO数组带PK
	public void insertVOsWithPK(Object[] objs) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.insertVOsWithPK(objs);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 保存VO数组不带PK
	public void insertVOsArr(Object[] objs) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.insertVOsArr(objs);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 删除VO数组
	public void deleteVOsArr(Object[] objs) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.deleteVOsArr(objs);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 删除VO
	public void deleteVO(Object obj) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.deleteVO(obj);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 更新VO数组
	public void updateVOsArr(Object[] objs) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.updateVOsArr(objs);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 更新VO
	public void updateVO(Object obj) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.updateVO(obj);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 保存VO返回PK
	public String insertVObackPK(Object obj) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 return dao.insertVObackPK(obj);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	// 执行update语句
	public void updateBYsql(String sql) throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 dao.updateBYsql(sql);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public ArrayList getVOsfromSql(String sql, String voname)
			throws BusinessException {
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 return dao.getVOsfromSql(sql,voname);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public ArrayList getJieAnData(HashMap hminfo) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 return dao.getJieAnData(hminfo);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public ArrayList doJieAn(ArrayList list) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 return dao.doJieAn(list);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public ArrayList doUNJieAn(ArrayList list) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			nc.bs.bgjs.jiean.ServerDAO dao = new nc.bs.bgjs.jiean.ServerDAO();
			 return dao.doUNJieAn(list);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	
}
