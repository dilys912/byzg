package nc.impl.pub.rino;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @author zip
 * @date 2012-3-8 上午6:30:33
 * @type nc.pub.zip.impl.PubDMOImpl
 */
@SuppressWarnings("rawtypes")
public class PubDMOImpl implements IPubDMO {

	public Object getObject(String sql) throws BusinessException {
		try {
			return DAOTools.getObject(sql);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public SuperVO getBean(String sql, Class clazz) throws BusinessException {
		try {
			return DAOTools.getBean(sql, clazz);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public List getBeanList(String sql, Class clazz) throws BusinessException {
		try {
			return DAOTools.getBeanList(sql, clazz);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public List getMapList(String sql) throws BusinessException {
		try {
			return DAOTools.getMapList(sql);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public Object[] getArray(String sql) throws BusinessException {
		try {
			return DAOTools.getArray(sql);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject getBillVO(String[] billVONames, String parentPK) throws BusinessException {
		try {
			return DAOTools.getBillVO(billVONames, parentPK);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject[] getBillVOs(String[] billVONames, String where) throws BusinessException {
		try {
			return DAOTools.getBillVOs(billVONames, where);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public String insert(SuperVO vo) throws BusinessException {
		try {
			return DAOTools.insert(vo);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public String[] insert(List voList) throws BusinessException {
		try {
			return DAOTools.insert(voList);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public String[] insert(SuperVO[] vos) throws BusinessException {
		try {
			return DAOTools.insert(vos);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject insert(AggregatedValueObject billVO) throws BusinessException {
		try {
			return DAOTools.insert(billVO);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject[] insert(AggregatedValueObject[] billVOs) throws BusinessException {
		try {
			return DAOTools.insert(billVOs);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void delete(Class clazz, String pk) throws BusinessException {
		try {
			DAOTools.delete(clazz, pk);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void delete(SuperVO vo) throws BusinessException {
		try {
			DAOTools.delete(vo);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void delete(SuperVO[] vos) throws BusinessException {
		try {
			DAOTools.delete(vos);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void delete(List voList) throws BusinessException {
		try {
			DAOTools.delete(voList);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject delete(AggregatedValueObject billVO) throws BusinessException {
		try {
			return DAOTools.delete(billVO);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public AggregatedValueObject[] delete(AggregatedValueObject[] billVOs) throws BusinessException {
		try {
			return DAOTools.delete(billVOs);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public int update(SuperVO vo) throws BusinessException {
		try {
			return DAOTools.update(vo);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void update(SuperVO vo, String[] fields) throws BusinessException {
		try {
			DAOTools.update(vo, fields);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public int update(SuperVO[] vos) throws BusinessException {
		try {
			return DAOTools.update(vos);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public int update(SuperVO[] vos, String[] fields) throws BusinessException {
		try {
			return DAOTools.update(vos, fields);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public void update(List voList) throws BusinessException {
		try {
			DAOTools.update(voList);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public int executeUpdate(String sql) throws BusinessException {
		try {
			return DAOTools.executeUpdate(sql);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}

	public Object invokeExecutor(String executor, Object param) throws BusinessException {
		try {
			Object executorObj = Class.forName(executor).newInstance();
			Method method = executorObj.getClass().getMethod("execute", Object.class);
			return method.invoke(executorObj, param);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
	}
	/**
	 * 系统动作接口：支持多个聚合VO执行
	 * shikun 2014-09-28  
	 * */
	public void N_XX_Action(String actionName, String billType, String currentDate, List<AggregatedValueObject> listaggvo) throws BusinessException {
		if (listaggvo!=null&&listaggvo.size()>0) {
		    IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
            HashMap hmPfExParams = new HashMap();
			for (int i = 0; i < listaggvo.size(); i++) {
				AggregatedValueObject voi = listaggvo.get(i);
			    iIplatFormEntry.processAction(actionName, billType, currentDate, null, voi, null, hmPfExParams);
			}
		}
	}

}
