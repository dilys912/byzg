package nc.impl.uap.bd.cust;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDException;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.bd.cust.ICuBasDocQry;
import nc.itf.uap.bd.cust.ICuBasDocTmp;
import nc.itf.uap.bd.cust.ICuBasDocTmpQry;
import nc.itf.uap.bd.cust.ICuBasForPFXX;
import nc.itf.uap.bd.cust.ICustBasDocOperate;
import nc.itf.uap.bd.cust.ICustBasDocQuery;
import nc.ui.bd.b08.AssociateFileUtil;
import nc.vo.bd.BDMsg;
import nc.vo.bd.b08.CbdocVO;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAreaVO;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.bd.b08.CustDivideListVO;
import nc.vo.bd.b08.CustcombineVO;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.general.GeneralExVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.IHashKey;
import nc.vo.trade.voutils.VOUtil;

public class CubasdocImpl
  implements ICuBasDocQry, ICuBasForPFXX, ICuBasDocTmp, ICuBasDocTmpQry, ICustBasDocQuery, ICustBasDocOperate
{
  private CustBasDocDAO m_custBasDao = null;

  public CubasdocVO findCubasdocVOByPK(String key)
    throws BusinessException
  {
    return getCustData(key, "0001");
  }

  public String insert(CubasdocVO cubasdoc)
    throws BusinessException
  {
    CustBasDocDAO dmo = getCustBasDao();
    String key = dmo.insert(cubasdoc, false, null);
    CacheProxy.fireDataInserted("bd_cubasdoc", null);
    CacheProxy.fireDataInserted("bd_custaddr", null);
    CacheProxy.fireDataInserted("bd_custbank", null);
    return key;
  }

  private CustBasDocDAO getCustBasDao()
  {
    if (this.m_custBasDao == null)
      this.m_custBasDao = new CustBasDocDAO();
    return this.m_custBasDao;
  }

  public void update(CubasdocVO cubasdoc)
    throws BusinessException
  {
    boolean islock = PKLock.getInstance().acquireBatchLock(cubasdoc.getLockpks(), cubasdoc.getUserid(), null);

    if (islock) {
      try {
        getCustBasDao().update(cubasdoc, false, null);
      } finally {
        PKLock.getInstance().releaseBatchLock(cubasdoc.getLockpks(), cubasdoc.getUserid(), null);
      }

    }
    else
    {
      throw new BusinessException(BDMsg.MSG_LOCKED());
    }
    CacheProxy.fireDataUpdated("bd_cubasdoc", null);
    CacheProxy.fireDataUpdated("bd_custaddr", null);
    CacheProxy.fireDataUpdated("bd_custbank", null);
  }

  public void combin(String src_pk, String des_pk, CustcombineVO combineVO)
    throws BusinessException
  {
    CustBasVO srcBasVo = getCustBasDao().getCustBaseHeadByPk(src_pk);
    CustBasVO desBasVo = getCustBasDao().getCustBaseHeadByPk(des_pk);
    String[] corps = multiCombin(src_pk, des_pk);

    if ("0001".equals(combineVO.getPk_corp())) {
      AssociateFileUtil.getCustAccociateFileUtil().transferAssociateFiles(srcBasVo, desBasVo, "0001");
    }

    int i = 0; for (int count = (corps == null) ? 0 : corps.length; i < count; ++i) {
      AssociateFileUtil.getCustAccociateFileUtil().transferAssociateFiles(srcBasVo, desBasVo, corps[i]);
    }
    combineVO.setOperatedatetime(new UFDateTime(System.currentTimeMillis()).toString());

    new BaseDAO().insertVO(combineVO);
  }

  public boolean deleteCubasDocByPK(String strPK, String strUserID)
    throws BusinessException
  {
    boolean islock = false;
    islock = PKLock.getInstance().acquireLock(strPK, strUserID, null);

    if (islock) {
      try {
        CustBasDocDAO dao = getCustBasDao();
        CustBasVO vo = dao.getCustBaseHeadByPk(strPK);
        boolean flag = dao.deleteByPK(strPK, "0001");
        AssociateFileUtil.getCustAccociateFileUtil().deleteAssociateFile(vo, "0001");
        boolean bool1 = flag;

        return bool1;
      }
      finally
      {
        PKLock.getInstance().releaseLock(strPK, strUserID, null);
      }

    }

    throw new BusinessException(BDMsg.MSG_LOCKED());
  }

  public CustBasVO[] getAssignedDocForCorp(String strPkCorp)
    throws BusinessException
  {
    CustBasVO[] custArea = null;
    CustBasDocDAO dmo = getCustBasDao();
    custArea = dmo.findAssignedDocForCorp(strPkCorp);
    return custArea;
  }

  public CustAreaVO[] getCustArea(String strPkCorp, String userId)
    throws BusinessException
  {
    return getCustBasDao().getCustArea(strPkCorp, userId);
  }

  public CubasdocVO getCustData(String strCustID, String strPkCorp)
    throws BusinessException
  {
    CustBasDocDAO dmo = getCustBasDao();
    CubasdocVO custData = dmo.queryCustByBasPkAndCorp(strCustID, strPkCorp);

    return custData;
  }

  public CbdocVO[] getCustDetailInArea(String pk_areacl, String strPkCorp)
    throws BusinessException
  {
    CbdocVO[] vos = null;
    try {
      CustBasDocDAO dmo = new CustBasDocDAO();
      vos = dmo.findBasDocDetailInArea(pk_areacl, strPkCorp);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return vos;
  }

  public CustDivideListVO[] getCustDivideList(String[] sArea, String strPkCorp)
    throws BusinessException
  {
    return getCustBasDao().getCustDivideList(sArea, strPkCorp);
  }

  public CustBasVO[] getCustInArea(String[] pk_areacl, String strPkCorp)
    throws BusinessException
  {
    CustBasVO[] custArea = null;
    try {
      CustBasDocDAO dao = new CustBasDocDAO();
      custArea = dao.queryCustByAreaPKAndCorp(pk_areacl, strPkCorp);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return custArea;
  }

  public CustDivideListVO[] getCustRightList(String strPkCorp, String strPkGroup)
    throws BusinessException
  {
    return getCustBasDao().getCustRightList(strPkCorp, strPkGroup);
  }

  public String insertForce(CubasdocVO cubasdoc)
    throws BusinessException
  {
    try
    {
      CustBasDocDAO dmo = new CustBasDocDAO();
      String key = dmo.insert(cubasdoc, true, null);

      CacheProxy.fireDataInserted("bd_cubasdoc", null);
      CacheProxy.fireDataInserted("bd_custaddr", null);
      CacheProxy.fireDataInserted("bd_custbank", null);

      return key;
    } catch (BusinessException be) {
      throw new BDException(be.getMessage());
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
  }

  private String[] multiCombin(String src_pk, String des_pk)
    throws BusinessException
  {
    BaseDAO baseDAO = new BaseDAO();

    Collection c = baseDAO.retrieveByClause(CumandocVO.class, "pk_cubasdoc = '" + src_pk + "' or pk_cubasdoc = '" + des_pk + "'");

    List list = VOUtil.extractFieldValues((CumandocVO[])(CumandocVO[])c.toArray(new CumandocVO[c.size()]), "pk_corp", null);

    Set set = new HashSet(list);
    String[] corps = (String[])(String[])set.toArray(new String[0]);
    CumandocImpl cumanImp = new CumandocImpl();

    for (int i = 0; i < corps.length; ++i) {
      try {
        cumanImp.combin(src_pk, des_pk, corps[i]);
      } catch (BusinessException e) {
        Logger.error(e.getMessage(), e);
        throw new BDException(e.getMessage());
      }
    }

    getCustBasDao().deleteBankByBasPk(src_pk.trim());

    getCustBasDao().deleteAddrByCustBasPk(src_pk.trim());
    getCustBasDao().deleteCustBaseDirect(src_pk.trim());
    return corps;
  }

  public CbdocVO[] queryCust(String strCon, String strPkCorp, String userId)
    throws BusinessException
  {
    return getCustBasDao().queryCustByCondiCorp(strCon, strPkCorp, userId);
  }

  public CustBasVO[] queryCustBasList(String strCon, String strPkCorp)
    throws BusinessException
  {
    return getCustBasDao().queryCustBasByCondiCorp(strCon, strPkCorp);
  }

  public String[] setCumanDoc(CustDivideListVO[] aryCuman, CustDivideListVO[] aryDel, String strPkCorpaa, GeneralExVO defaultValueVO)
    throws BusinessException
  {
    try
    {
      CustBasDocDAO dmo = new CustBasDocDAO();
      String[] hint = dmo.setCumanDoc(aryCuman, aryDel, defaultValueVO);
      deleteAssociateFileForCancel(aryDel);
      CacheProxy.fireDataInserted("bd_cumandoc", null);
      return hint;
    } catch (BusinessException e) {
      throw e;
    } catch (SQLException sql) {
      Logger.error(sql);
      throw new RuntimeException(sql.getMessage());
    }
  }

  private void deleteAssociateFileForCancel(CustDivideListVO[] aryDel)
    throws BusinessException
  {
    HashMap map;
    Iterator iter;
    if ((aryDel != null) && (aryDel.length > 0)) {
      map = Hashlize.hashlizeVOs(aryDel, new IHashKey() {
        public String getKey(Object o) {
          return ((CustDivideListVO)o).getPk_cubasdoc();
        }
      });
      for (iter = map.keySet().iterator(); iter.hasNext(); ) {
        String key = (String)iter.next();
        ArrayList list = (ArrayList)map.get(key);
        CustBasVO vo = getCustBasDao().getCustBaseHeadByPk(key);
        int i = 0; for (int count = list.size(); i < count; ++i)
          AssociateFileUtil.getCustAccociateFileUtil().deleteAssociateFile(vo, ((CustDivideListVO)list.get(i)).getPk_corp());
      }
    }
  }

  public void updateForce(CubasdocVO cubasdoc)
    throws BusinessException
  {
    boolean islock = false;
    islock = PKLock.getInstance().acquireBatchLock(cubasdoc.getLockpks(), cubasdoc.getUserid(), "");
    try
    {
      if (islock) {
        CustBasDocDAO dmo = new CustBasDocDAO();
        dmo.update(cubasdoc, true, null);
      }
      else
      {
        throw new BusinessException(BDMsg.MSG_LOCKED());
      }
    }
    catch (BusinessException be)
    {
    }
    catch (Exception e) {
      throw new BDException(e.getMessage());
    } finally {
      PKLock.getInstance().releaseBatchLock(cubasdoc.getLockpks(), cubasdoc.getUserid(), "");
    }

    CacheProxy.fireDataUpdated("bd_cubasdoc", null);
    CacheProxy.fireDataUpdated("bd_custaddr", null);
    CacheProxy.fireDataUpdated("bd_custbank", null);
  }

  public void updateForEx(CubasdocVO cubasdoc)
    throws BusinessException
  {
  }

  public CustcombineVO[] getCustCombine(String whereSql)
    throws BusinessException
  {
    BaseDAO dao = new BaseDAO();
    Collection colle = dao.retrieveByClause(CustcombineVO.class, whereSql + " order by operatedatetime desc ");
    return ((CustcombineVO[])(CustcombineVO[])colle.toArray(new CustcombineVO[0]));
  }

  public String[] setCumanDocForEx_change(CustDivideListVO[] aryCustData, String strPKCorp, String strPk_cumandoc, int custflag)
    throws BusinessException
  {
    return null;
  }

  public String insertForEx_change(CubasdocVO vo) throws BusinessException {
    return null; }

  public CubasdocVO queryCustBasDocVOByPK(String key) throws BusinessException {
    return findCubasdocVOByPK(key);
  }

  public CbdocVO[] queryBasCustByCondi(String strCon, String pk_corp, String userId) throws BusinessException {
    return queryCust(strCon, pk_corp, userId);
  }

  public String insertCust(CubasdocVO cuBasDocVo, boolean isAllowSameNames) throws BusinessException {
    if (!(isAllowSameNames)) {
      return insert(cuBasDocVo);
    }
    return insertForce(cuBasDocVo);
  }

  public void updateCust(CubasdocVO cuBasDocVo, boolean isAllowSameNames) throws BusinessException {
    if (!(isAllowSameNames))
      update(cuBasDocVo);
    else
      updateForce(cuBasDocVo);
  }

  public boolean deleteCustBasDocByPK(String strPK) throws BusinessException {
    return deleteCubasDocByPK(strPK, null);
  }

  public CbdocVO[] queryCustInArea(String pk_areacl, String strPkCorp) throws BusinessException {
    return getCustDetailInArea(pk_areacl, strPkCorp);
  }
}