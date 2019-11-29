package nc.impl.pub.rino;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 数据库操作工具类
 * @author zip
 * @date 2012-1-7 下午9:59:18
 * @type nc.pub.zip.bs.DAOTools
 */
@SuppressWarnings("rawtypes")
public final class DAOTools {
	private final static BaseDAO dao = new BaseDAO();
	private final static IUifService billDAO = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());

	public static final Object getObject(String sql) throws Exception {
		return dao.executeQuery(sql, new ColumnProcessor());
	}

	public static final SuperVO getBean(String sql, Class clazz) throws Exception {
		return (SuperVO) dao.executeQuery(sql, new BeanProcessor(clazz));
	}

	public static final List getBeanList(String sql, Class clazz) throws Exception {
		return (List) dao.executeQuery(sql, new BeanListProcessor(clazz));
	}

	public static final List getMapList(String sql) throws Exception {
		return (List) dao.executeQuery(sql, new MapListProcessor());
	}

	public static final Object[] getArray(String sql) throws Exception {
		ArrayList rst = (ArrayList) dao.executeQuery(sql, new ArrayListProcessor());
		if (rst == null || rst.size() <= 0) return null;
		Object[] data = new Object[rst.size()];
		for (int i = 0; i < rst.size(); i++) {
			data[i] = ((Object[]) rst.get(i))[0];
		}
		return data;
	}

	public static final AggregatedValueObject getBillVO(String[] billVONames, String parentPK) throws Exception {
		return billDAO.queryBillVOByPrimaryKey(billVONames, parentPK);
	}

	public static final AggregatedValueObject[] getBillVOs(String[] billVONames, String where) throws Exception {
		return billDAO.queryBillVOByCondition(billVONames, where);
	}

	public static final String insert(SuperVO vo) throws Exception {
		return dao.insertVO(vo);
	}

	public static final String[] insert(List voList) throws Exception {
		return dao.insertVOList(voList);
	}

	public static final String[] insert(SuperVO[] vos) throws Exception {
		return dao.insertVOArray(vos);
	}

	public static final AggregatedValueObject insert(AggregatedValueObject billVO) throws Exception {
		return billDAO.saveBill(billVO);
	}

	public static final AggregatedValueObject[] insert(AggregatedValueObject[] billVOs) throws Exception {
		return billDAO.saveBDs(billVOs, null);
	}

	public static final void delete(Class clazz, String pk) throws Exception {
		dao.deleteByPK(clazz, pk);
	}

	public static final void delete(SuperVO vo) throws Exception {
		dao.deleteVO(vo);
	}

	public static final void delete(SuperVO[] vos) throws Exception {
		dao.deleteVOArray(vos);
	}

	public static final void delete(List voList) throws Exception {
		dao.deleteVOList(voList);
	}

	public static final AggregatedValueObject delete(AggregatedValueObject billVO) throws Exception {
		return billDAO.deleteBill(billVO);
	}

	public static final AggregatedValueObject[] delete(AggregatedValueObject[] billVOs) throws Exception {
		AggregatedValueObject[] results = new AggregatedValueObject[billVOs.length];
		for (int i = 0; i < billVOs.length; i++) {
			results[i] = delete(billVOs[i]);
		}
		return results;
	}

	public static final int update(SuperVO vo) throws Exception {
		dao.updateVO(vo);
		return 1;
	}

	public static final void update(SuperVO vo, String[] fields) throws Exception {
		dao.updateVO(vo, fields);
	}

	public static final int update(SuperVO[] vos) throws Exception {
		dao.updateVOArray(vos);
		return vos.length;
	}

	public static final int update(SuperVO[] vos, String[] fields) throws Exception {
		dao.updateVOArray(vos, fields);
		return vos.length;
	}

	public static final void update(List voList) throws Exception {
		dao.updateVOList(voList);
	}

	public static final int executeUpdate(String sql) throws Exception {
		return dao.executeUpdate(sql);
	}

	public static final BaseDAO getDAO() {
		return dao;
	}

	public static final IUifService getBillDAO() {
		return billDAO;
	}
}
