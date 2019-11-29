package nc.ui.bd.b14;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.inv.IInvcl;
import nc.itf.uap.bd.inv.IInvclQry;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.b14.InvclVO;
import nc.vo.pub.BusinessException;
import nc.vo.rino.pda.BasicdocVO;

/**
 * 存货分类基本档案扩展
 * @author yhj 2014-02-20
 *
 */
public class InvclBO_ClientExt extends nc.ui.bd.b14.InvclBO_Client{

	
	static IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
			.lookup(IUAPQueryBS.class.getName());
	static IUifService iserviceDao = (IUifService) NCLocator.getInstance()
			.lookup(IUifService.class.getName());

    public InvclBO_ClientExt()
    {
    }

    public static InvclVO findByPrimaryKey(String key)
        throws BusinessException
    {
        return getIquery().findByInvClVOByPk(key);
    }

    public static InvclVO[] queryAll(String unitCode)
        throws BusinessException
    {
        return getIquery().queryAllInvClVO(unitCode);
    }

    public static boolean delete(InvclVO vo, String parentID)
        throws BusinessException
    {
    	boolean bos = getIupdate().delete(vo, parentID);
    	/**
		 * add by yhj 2014-02-20
		 */
		if (vo != null) {
			String sql = "select * from pda_basicdoc where bdid='"+ vo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
			BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql,new BeanProcessor(BasicdocVO.class));
			if (checkVO != null) {
				iserviceDao.deleteByWhereClause(BasicdocVO.class,"bdid='" + vo.getPrimaryKey() + "' and sysflag='N'");
				checkVO.setProctype("delete");
				iserviceDao.update(checkVO);
			}
		}
        return bos; 
    }

    public static String insert(InvclVO invcl, String parentOID)
        throws BusinessException
    {
    	String ret = getIupdate().insert(invcl, parentOID);
    	/**
		 * add by yhj 2014-02-22 新增保存后操作
		 */
		if (invcl.getMemo() != null && invcl.getMemo().startsWith("PDA")) {
			BasicdocVO vo = new BasicdocVO();
			vo.setBdname(invcl.getInvclassname());
			vo.setPk_corp(invcl.getPk_corp());
			vo.setBdid(ret);
			vo.setBdtype("CHFL");
			vo.setProctype("add");
			vo.setSysflag("Y");
			iserviceDao.insert(vo);
		}
        return ret;
    }

    public static void update(InvclVO invcl)
        throws BusinessException
    {
        getIupdate().update(invcl);
        /**
		 * add by yhj 2014-02-20 修改保存后操作
		 */
		String sql = "select * from pda_basicdoc where bdid='"+ invcl.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
		BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql,new BeanProcessor(BasicdocVO.class));
		boolean pdaFlag = invcl.getMemo() != null && invcl.getMemo().startsWith("PDA");
		if (checkVO == null && pdaFlag) {
			BasicdocVO vo = new BasicdocVO();
			vo.setBdname(invcl.getInvclassname());
			vo.setBdid(invcl.getPrimaryKey());
			vo.setBdtype("CHFL");
			vo.setPk_corp(invcl.getPk_corp());//确认一下有没有公司
			vo.setProctype("add");
			vo.setSysflag("Y");
			iserviceDao.insert(vo);
		} else if (checkVO != null && !checkVO.getBdname().equals(invcl.getInvclassname()) && pdaFlag) {
			iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='"+ invcl.getPrimaryKey() + "' and sysflag='N'");
			checkVO.setBdname(invcl.getInvclassname());
			iserviceDao.update(checkVO);
		} else if (checkVO != null && !pdaFlag) {
			iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='"+ invcl.getPrimaryKey() + "' and sysflag='N'");
			checkVO.setProctype("delete");
			iserviceDao.update(checkVO);
		}
    }

    private static IInvcl getIupdate()
    {
        if(iupdate == null)
            iupdate = (IInvcl)NCLocator.getInstance().lookup(nc.itf.uap.bd.inv.IInvcl.class.getName());
        return iupdate;
    }

    private static IInvclQry getIquery()
    {
        if(iquery == null)
            iquery = (IInvclQry)NCLocator.getInstance().lookup(nc.itf.uap.bd.inv.IInvclQry.class.getName());
        return iquery;
    }

    private static IInvclQry iquery;
    private static IInvcl iupdate;

}
