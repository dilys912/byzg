package nc.itf.pub.rino;

import java.util.List;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @author zip
 * @date 2012-3-8 上午6:26:55
 * @type nc.pub.zip.itf.IPubDMO
 */
@SuppressWarnings("rawtypes")
public interface IPubDMO {

	public abstract Object getObject(String sql) throws BusinessException;

	public abstract SuperVO getBean(String sql, Class clazz) throws BusinessException;

	public abstract List getBeanList(String sql, Class clazz) throws BusinessException;

	public abstract List getMapList(String sql) throws BusinessException;

	public abstract Object[] getArray(String sql) throws BusinessException;

	public abstract AggregatedValueObject getBillVO(String[] billVONames, String parentPK) throws BusinessException;

	public abstract AggregatedValueObject[] getBillVOs(String[] billVONames, String where) throws BusinessException;

	public abstract String insert(SuperVO vo) throws BusinessException;

	public abstract String[] insert(List voList) throws BusinessException;

	public abstract String[] insert(SuperVO[] vos) throws BusinessException;

	public abstract AggregatedValueObject insert(AggregatedValueObject billVO) throws BusinessException;

	public abstract AggregatedValueObject[] insert(AggregatedValueObject[] billVOs) throws BusinessException;

	public abstract void delete(Class clazz, String pk) throws BusinessException;

	public abstract void delete(SuperVO vo) throws BusinessException;

	public abstract void delete(SuperVO[] vos) throws BusinessException;

	public abstract void delete(List voList) throws BusinessException;

	public abstract AggregatedValueObject delete(AggregatedValueObject billVO) throws BusinessException;

	public abstract AggregatedValueObject[] delete(AggregatedValueObject[] billVOs) throws BusinessException;

	public abstract int update(SuperVO vo) throws BusinessException;

	public abstract void update(SuperVO vo, String[] fields) throws BusinessException;

	public abstract int update(SuperVO[] vos) throws BusinessException;

	public abstract int update(SuperVO[] vos, String[] fields) throws BusinessException;

	public abstract void update(List voList) throws BusinessException;

	public abstract int executeUpdate(String sql) throws BusinessException;

	public abstract Object invokeExecutor(String executor, Object param) throws BusinessException;

	/**
	 * 系统动作接口：支持多个聚合VO执行
	 * shikun 2014-09-28  
	 * */
	public abstract void N_XX_Action(String actionname, String billtype,String date, List<AggregatedValueObject> listaggvo) throws BusinessException;
}
