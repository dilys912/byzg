package nc.ui.bd.b27;

/**
 * 货位基本档案扩展
 * @author yhj 2014-02-20
 *
 */
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.warehouse.ICargdoc;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.b27.CargdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.rino.pda.BasicdocVO;

public class CargdocBO_ClientExt extends nc.ui.bd.b27.CargdocBO_Client {

	static IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	static IUifService iserviceDao = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());

	private static ICargdoc iCargdoc = null;

	private static ICargdoc getICargdoc() throws BusinessException {
		if (iCargdoc == null) iCargdoc = (ICargdoc) NCLocator.getInstance().lookup(nc.itf.uap.bd.warehouse.ICargdoc.class.getName());
		return iCargdoc;
	}

	public static String insert(CargdocVO cvo, String parentOID) throws BusinessException {
		String str = getICargdoc().insertCargdocVO(cvo);
		/**
		 * add by yhj 2014-02-20 新增保存后操作
		 */
		if (cvo.getMemo() != null && cvo.getMemo().startsWith("PDA")) {
			String pk_stordoc = cvo.getPk_stordoc();
			String pk_corp = null;
			if (pk_stordoc != null) {
				String sql = "select pk_corp from bd_stordoc where nvl(dr,0) = 0 and pk_stordoc='" + pk_stordoc + "'";
				pk_corp = (String) iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
			}
			BasicdocVO vo = new BasicdocVO();
			vo.setBdname(cvo.getCsname());
			vo.setBdid(str);
			vo.setBdtype("HW");
			vo.setPk_corp(pk_corp);//确认一下是否有公司主键
			vo.setProctype("add");
			vo.setSysflag("Y");
			String storname = (String) iUAPQueryBS.executeQuery("select storname from bd_stordoc where pk_stordoc='" + cvo.getPk_stordoc() + "'", new ColumnProcessor());
			vo.setDef2(storname);
			iserviceDao.insert(vo);
		}
		return str;
	}

	public static void update(CargdocVO cargdoc) throws BusinessException {
		getICargdoc().updateCargdocVO(cargdoc);
		/**
		 * add by yhj 2014-02-20 修改保存后操作
		 */
		String sql = "select * from pda_basicdoc where bdid='" + cargdoc.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
		BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
		boolean pdaFlag = cargdoc.getMemo() != null && cargdoc.getMemo().startsWith("PDA");
		if (checkVO == null && pdaFlag) {
			BasicdocVO vo = new BasicdocVO();
			vo.setBdname(cargdoc.getCsname());
			vo.setBdid(cargdoc.getPrimaryKey());
			vo.setBdtype("HW");
			String pk_stordoc = cargdoc.getPk_stordoc();
			String pk_corp = null;
			if (pk_stordoc != null) {
				String pk_corp_SQL = "select pk_corp from bd_stordoc where nvl(dr,0) = 0 and pk_stordoc='" + pk_stordoc + "'";
				pk_corp = (String) iUAPQueryBS.executeQuery(pk_corp_SQL, new ColumnProcessor());
			}
			vo.setPk_corp(pk_corp);
			vo.setProctype("add");
			vo.setSysflag("Y");
			String storname = (String) iUAPQueryBS.executeQuery("select storname from bd_stordoc where pk_stordoc='" + cargdoc.getPk_stordoc() + "'", new ColumnProcessor());
			vo.setDef2(storname);
			iserviceDao.insert(vo);
		} else if (checkVO != null && !checkVO.getBdname().equals(cargdoc.getCsname()) && pdaFlag) {
			iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + cargdoc.getPrimaryKey() + "' and sysflag='N'");
			checkVO.setBdname(cargdoc.getCsname());
			checkVO.setProctype("update");
			iserviceDao.update(checkVO);
		} else if (checkVO != null && !pdaFlag) {
			iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + cargdoc.getPrimaryKey() + "' and sysflag='N'");
			checkVO.setProctype("delete");
			iserviceDao.update(checkVO);
		}
	}

	public static boolean delete(CargdocVO vo, String parentID) throws BusinessException {
		boolean bo = getICargdoc().deleteCargdocVO(vo);
		/**
		 * add by yhj 2014-02-20
		 */
		if (vo != null) {
			String sql = "select * from pda_basicdoc where bdid='" + vo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
			BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
			if (checkVO != null) {
				iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + vo.getPrimaryKey() + "' and sysflag='N'");
				checkVO.setProctype("delete");
				iserviceDao.update(checkVO);
			}
		}

		return bo;
	}

}
