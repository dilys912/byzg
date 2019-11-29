package nc.impl.uap.bd.cust;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.pub.BDOperateContextObject;
import nc.bs.bd.pub.CAVOBDBaseDocAdapter;
import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mw.sqltrans.TempTable;
import nc.bs.uap.bd.BDException;
import nc.itf.uap.bd.IuniqueCheckCondition;
import nc.itf.uap.bd.accbank.IAccbankPrivate;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.itf.uap.rbac.IPowerManageQuery;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.mapping.ext.JoinQueryMappingMetaWrapper;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b08.AreaClassMapping;
import nc.vo.bd.b08.CbdocVO;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAddrMapping;
import nc.vo.bd.b08.CustAddrVO;
import nc.vo.bd.b08.CustAreaVO;
import nc.vo.bd.b08.CustBankMapping;
import nc.vo.bd.b08.CustBankVO;
import nc.vo.bd.b08.CustBasMapping;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.bd.b08.CustDivideListVO;
import nc.vo.bd.b08.CustManMapping;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.bd.b09.CustManVO;
import nc.vo.bd.b23.AccbankVO;
import nc.vo.ml.Language;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.general.GeneralExVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.IHashKey;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.uap.rbac.power.UserPowerQueryVO;

public class CustBasDocDAO
{
  private IuniqueCheckCondition checkcond;

  public void deleteAddrByCustBasPk(String headerKey)
    throws BusinessException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delByWhereClause("bd_custaddr", "pk_custaddr", "pk_cubasdoc = '" + headerKey + "'");

    new BaseDAO().deleteByClause(new CustAddrMapping(), "pk_cubasdoc = '" + headerKey + "' ");
  }

  private void deleteCustBank(PersistenceManager manager, CustBankVO vo)
    throws DbException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delPKs("bd_custbank", new String[] { vo.getPrimaryKey() });
    manager.deleteByPK(new CustBankMapping(), vo.getPrimaryKey());
  }

  public void deleteBankByBasPk(String strPK)
    throws BusinessException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delByWhereClause("bd_custbank", "pk_custbank", "pk_cubasdoc = '" + strPK + "'");

    new BaseDAO().deleteByClause(new CustBankMapping(), "pk_cubasdoc = '" + strPK + "' ");
  }

  public boolean deleteByPK(String custBasPk, String pk_corp)
    throws BusinessException
  {
    deleteAddrByCustBasPk(custBasPk);
    deleteBankByBasPk(custBasPk);
    deleteHeaderByPK(custBasPk, pk_corp);
    return true;
  }

  public void deleteHeaderByPK(String strPK, String pk_corp)
    throws BusinessException
  {
    IReferenceCheck ref = (IReferenceCheck)NCLocator.getInstance().lookup(IReferenceCheck.class.getName());

    if (ref.isReferenced("bd_cubasdoc", strPK)) {
      throw new BDException(BDMsg.MSG_REF_NOT_DELETE());
    }

    BDOperateServ bdOpServ = new BDOperateServ("10080804", 6, 0);

    bdOpServ.beforeOperate("10080804", 6, strPK, pk_corp, null);

    Collection c = new BaseDAO().retrieveByClause(CustBasVO.class, new CustBasMapping(), "pk_cubasdoc1 = '" + strPK + "' ", new String[] { "pk_cubasdoc" });

    if ((c != null) && (c.size() > 0)) {
      throw new BDException(BDMsg.MSG_DATA_DELETE_FAIL() + " " + NCLangResOnserver.getInstance().getStrByID("10080804", "UPP10080804-000010"));
    }

    deleteCustBaseDirect(strPK);
    bdOpServ.afterOperate("10080804", 6, strPK, pk_corp, null);
  }

  protected void deleteCustBaseDirect(String strPK)
    throws DAOException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delPKs("bd_cubasdoc", new String[] { strPK });
    new BaseDAO().deleteByClause(new CustBasMapping(), "pk_cubasdoc = '" + strPK + "'");
  }

  public void deleteAddressByVo(PersistenceManager manager, CustAddrVO vo)
    throws DbException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delPKs("bd_custaddr", new String[] { vo.getPrimaryKey() });
    manager.deleteObject(vo, new CustAddrMapping());
  }

  public CustBasVO[] findAssignedDocForCorp(String pk_corp)
    throws BusinessException
  {
    CustBasVO[] vos = null;
    String where = "pk_corp = '0001' and pk_cubasdoc in (select distinct pk_cubasdoc from bd_cumandoc where pk_corp = '" + pk_corp + "') ";

    Collection c = new BaseDAO().retrieveByClause(CustBasVO.class, new CustBasMapping(), where, new String[] { "custname", "pk_cubasdoc", "pk_areacl", "custcode" });

    if ((c != null) && (c.size() > 0)) {
      vos = new CustBasVO[c.size()];
      c.toArray(vos);
    }
    return vos;
  }

  protected CbdocVO[] findBasDocDetailInArea(String pk_areacl, String pk_corp)
    throws SQLException
  {
    CustManVO[] custManVO = findDocDetailInarea(pk_areacl, pk_corp, null);
    if ((custManVO != null) && (custManVO.length > 0)) {
      List list = new ArrayList();
      for (int i = 0; i < custManVO.length; ++i) {
        CbdocVO vo = new CbdocVO();
        vo.setAddrs(custManVO[i].getAddrs());
        vo.setBanks(custManVO[i].getBanks());
        vo.setParentVO(custManVO[i].getCustBasVO());
        list.add(vo);
      }
      return ((CbdocVO[])(CbdocVO[])list.toArray(new CbdocVO[0]));
    }
    return null;
  }

  protected CustManVO[] findDocDetailInarea(String pk_areal, String pk_corp, String pk_settleunit)
    throws SQLException
  {
    CustManVO[] custManVO = null;
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();

      String tempTableName = createTempTable(manager);

      int count = requiredCustPkToTemp(pk_areal, pk_corp, pk_settleunit, manager, tempTableName);

      if (count == 0) {
        Object localObject1 = null;
        return (CustManVO[]) localObject1;
      }
      HashMap adapter = new HashMap();

      queryCustBasDocData(manager, tempTableName, adapter);

      queryCustManDocData(manager, pk_corp, tempTableName, adapter);

      queryCustAddress(manager, tempTableName, adapter);

      queryCustBank(pk_corp, manager, tempTableName, adapter);

      custManVO = (CustManVO[])(CustManVO[])adapter.values().toArray(new CustManVO[0]);
    }
    catch (DbException e)
    {
    }
    finally {
      if (manager != null)
        manager.release();
    }
    return custManVO;
  }

  private String createTempTable(PersistenceManager manager)
    throws SQLException
  {
    TempTable tmptab = new TempTable();
    String vtn = tmptab.createTempTable(manager.getJdbcSession().getConnection(), "cust", "custid char(20)", "custid");

    return vtn;
  }

  private int requiredCustPkToTemp(String pk_areal, String pk_corp, String pk_settleunit, PersistenceManager manager, String tempTable)
    throws DbException
  {
    StringBuffer insert = new StringBuffer();
    insert.append("insert into " + tempTable + " select pk_cubasdoc from bd_cubasdoc where pk_areacl = '" + pk_areal + "' ");

    if (("0001".equals(pk_corp)) || (pk_corp == null)) {
      insert.append(" and pk_corp = '0001' ");
    } else {
      insert.append(" and pk_cubasdoc in (select distinct pk_cubasdoc from bd_cumandoc where pk_corp = '" + pk_corp + "'");

      if ((pk_settleunit != null) && (pk_settleunit.length() > 0))
        insert.append(" and pk_settleunit = '" + pk_settleunit + "'");
      insert.append(")");
    }
    return manager.getJdbcSession().executeUpdate(insert.toString());
  }

  private void queryCustBasDocData(PersistenceManager manager, String tempTableName, HashMap adapter)
    throws DbException
  {
    String from = " bd_cubasdoc a, " + tempTableName + " b ";
    String where = "a.pk_cubasdoc = b.custid";
    JoinQueryMappingMetaWrapper metaWrapper = new JoinQueryMappingMetaWrapper(new CustBasMapping(), from, "a");

    Collection collection = manager.retrieveByClause(CustBasVO.class, metaWrapper, where);
    Iterator iter;
    if (collection != null)
      for (iter = collection.iterator(); iter.hasNext(); ) {
        CustBasVO custBasVO = (CustBasVO)iter.next();
        CustManVO cumanvo = new CustManVO();
        cumanvo.setParentVO(custBasVO);
        adapter.put(custBasVO.getPrimaryKey(), cumanvo);
      }
  }

  private void queryCustManDocData(PersistenceManager manager, String pk_corp, String tempTableName, HashMap adapter)
    throws DbException
  {
    if ((pk_corp == null) || ("0001".equals(pk_corp)))
      return;
    String from = " bd_cumandoc a, " + tempTableName + " b ";
    String where = " a.pk_cubasdoc = b.custid and a.pk_corp = '" + pk_corp + "' order by a.pk_cubasdoc";

    JoinQueryMappingMetaWrapper metaWrapper = new JoinQueryMappingMetaWrapper(new CustManMapping(), from, "a");

    Collection collection = manager.retrieveByClause(CumandocVO.class, metaWrapper, where, metaWrapper.getColumns());

    CumandocVO[] manVOs = (CumandocVO[])(CumandocVO[])collection.toArray(new CumandocVO[0]);

    if (manVOs != null) {
      CumandocVO[] mandoc = new CumandocVO[2];
      int i = 0; for (int mod = 1; i < manVOs.length; ++mod) {
        if (mod % 2 == 0) {
          mandoc[1] = manVOs[i];

          ((CustManVO)adapter.get(manVOs[i].getPk_cubasdoc())).setChildrenVO(mandoc);

          mandoc = new CumandocVO[2];
        } else {
          mandoc[0] = manVOs[i];
        }
        ++i;
      }
    }
  }

  private void queryCustAddress(PersistenceManager manager, String tempTableName, HashMap adapter)
    throws DbException
  {
    String from = " bd_custaddr a, " + tempTableName + " b ";
    String where = " a.pk_cubasdoc = b.custid  order by a.pk_cubasdoc";
    JoinQueryMappingMetaWrapper metaWrapper = new JoinQueryMappingMetaWrapper(new CustAddrMapping(), from, "a");

    Collection collection = manager.retrieveByClause(CustAddrVO.class, metaWrapper, where, metaWrapper.getColumns());

    ArrayList list = new ArrayList();
    String key = "pk_cubasdoc";
    for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
      CustAddrVO addr = (CustAddrVO)iter.next();
      String pk_cubasdoc = addr.getPk_cubasdoc();
      if (!(key.equals(pk_cubasdoc))) {
        if (list.size() > 0) {
          CustAddrVO[] vos = (CustAddrVO[])(CustAddrVO[])list.toArray(new CustAddrVO[0]);

          CustManVO tvo = (CustManVO)adapter.get(key);
          if (tvo != null)
            tvo.setAddrs(vos);
        }
        key = pk_cubasdoc;
        list.clear();
      }
      list.add(addr);
    }

    CustAddrVO[] addr = (CustAddrVO[])(CustAddrVO[])list.toArray(new CustAddrVO[0]);
    CustManVO lvo = (CustManVO)adapter.get(key);
    if (lvo != null)
      lvo.setAddrs(addr);
  }

  private void queryCustBank(String pk_corp, PersistenceManager manager, String tempTableName, HashMap adapter)
    throws DbException
  {
    String from = " bd_custbank a, " + tempTableName + " b ";
    String where = " a.pk_cubasdoc = b.custid and (pk_corp='0001' or pk_corp = '" + pk_corp + "')" + " order by a.pk_cubasdoc";

    JoinQueryMappingMetaWrapper metaWrapper = new JoinQueryMappingMetaWrapper(new CustBankMapping(), from, "a");

    Collection collection = manager.retrieveByClause(CustBankVO.class, metaWrapper, where, metaWrapper.getColumns());

    String key = "bank";
    ArrayList list = new ArrayList();
    for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
      CustBankVO bank = (CustBankVO)iter.next();
      String pk_cubasdoc = bank.getPk_cubasdoc();
      if (!(key.equals(pk_cubasdoc))) {
        if (list.size() > 0)
        {
          CustBankVO[] vos = (CustBankVO[])(CustBankVO[])list.toArray(new CustBankVO[0]);

          CustManVO tvo = (CustManVO)adapter.get(key);
          tvo.setBanks(vos);
        }
        key = pk_cubasdoc;
        list.clear();
      }
      list.add(bank);
    }

    CustBankVO[] vos = (CustBankVO[])(CustBankVO[])list.toArray(new CustBankVO[0]);
    CustManVO tvo = (CustManVO)adapter.get(key);
    if (tvo != null)
      tvo.setBanks(vos);
  }

  public CustBasVO[] queryCustByAreaPKAndCorp(String[] area_pk, String pk_corp)
    throws BusinessException
  {
    if ((area_pk == null) || (area_pk.length == 0))
      return null;
    CustBasVO[] vos = null;
    StringBuffer in = new StringBuffer();
    in.append(" pk_areacl in(");
    for (int i = 0; i < area_pk.length; ++i) {
      if (i == area_pk.length - 1)
        in.append("'" + area_pk[i] + "' ");
      else {
        in.append("'" + area_pk[i] + "', ");
      }
    }
    in.append(")");
    vos = queryCustBasByCondiCorp(in.toString(), pk_corp);
    return vos;
  }

  public CustAreaVO[] getCustArea(String pk_corp, String userId)
    throws BusinessException
  {
    CustAreaVO[] vos = null;
    pk_corp = (pk_corp == null) ? "0001" : pk_corp;
    String where = "pk_corp = '0001' ";
    if (!("0001".equals(pk_corp))) {
      where = where + "or pk_corp = '" + pk_corp.trim() + "' ";
    }
    Collection c = new BaseDAO().retrieveByClause(CustAreaVO.class, new AreaClassMapping(), where);

    if ((c != null) && (c.size() > 0))
    {
      List list = filterByPowerAreaCl((CustAreaVO[])(CustAreaVO[])c.toArray(new CustAreaVO[0]), pk_corp, userId);
      if (list.size() > 0) {
        vos = (CustAreaVO[])(CustAreaVO[])list.toArray(new CustAreaVO[0]);
        VOUtil.ascSort(vos, new String[] { "pk_fatherarea", "areaclcode" });
      }
    }
    return vos;
  }

  protected List filterByPowerAreaCl(CircularlyAccessibleValueObject[] cVo, String pk_corp, String userId)
    throws BusinessException
  {
    List powerVoList = new ArrayList();
    if ((cVo == null) || (cVo.length == 0))
      return powerVoList;
    if (userId != null) {
      PowerResultVO powerVo = queryPowerAreaClass(pk_corp, userId);
      if (powerVo.isPowerControl()) {
        String[] pks = powerVo.getPowerId();
        if ((pks != null) && (pks.length > 0)) {
          List powerList = Arrays.asList(pks);
          for (int i = 0; i < cVo.length; ++i) {
            if (powerList.contains(cVo[i].getAttributeValue("pk_areacl"))) {
              powerVoList.add(cVo[i]);
              if (cVo[i] instanceof CustAreaVO) {
                CustAreaVO areavo = (CustAreaVO)cVo[i];
                if ((areavo.getPk_fatherarea() != null) && (!(powerList.contains(areavo.getPk_fatherarea()))))
                  areavo.setPk_fatherarea(null);
              }
            }
          }
          return powerVoList;
        }

        return powerVoList;
      }
    }

    return Arrays.asList(cVo);
  }

  protected PowerResultVO queryPowerAreaClass(String pk_corp, String userId)
    throws BusinessException
  {
    IPowerManageQuery impl = (IPowerManageQuery)NCLocator.getInstance().lookup(IPowerManageQuery.class.getName());
    UserPowerQueryVO condvo = new UserPowerQueryVO();
    condvo.setResouceId(24);
    condvo.setCorpPK(pk_corp);
    condvo.setOrgPK(pk_corp);
    condvo.setUserPK(userId);
    PowerResultVO resultvo = impl.getUserPower(condvo);
    return resultvo;
  }

  public CustBankVO[] getCustBankByBasPk(PersistenceManager manager, String custBas_pk, String pk_corp)
    throws DbException
  {
    String where = "pk_cubasdoc = '" + custBas_pk + "' and (pk_corp = '0001'";

    if ((pk_corp == null) || ("0001".equals(pk_corp)))
      where = where + ")";
    else
      where = where + " or pk_corp = '" + pk_corp + "')";
    Collection col = manager.retrieveByClause(CustBankVO.class, new CustBankMapping(), where);

    return ((CustBankVO[])(CustBankVO[])col.toArray(new CustBankVO[col.size()]));
  }

  public CustAddrVO[] getCustAddressByBasPk(PersistenceManager manager, String bas_pk)
    throws DbException
  {
    String where = "pk_cubasdoc = '" + bas_pk + "'";
    Collection col = manager.retrieveByClause(CustAddrVO.class, new CustAddrMapping(), where);

    return ((CustAddrVO[])(CustAddrVO[])col.toArray(new CustAddrVO[col.size()]));
  }

  public CubasdocVO queryCustByBasPkAndCorp(String custBasPk, String pk_corp)
    throws BusinessException
  {
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();
      CubasdocVO retVO = new CubasdocVO();
      retVO.setParentVO(getCustBaseHeadByPk(custBasPk));
      retVO.setChildrenVO(getCustAddressByBasPk(manager, custBasPk));
      retVO.setCustBankVOs(getCustBankByBasPk(manager, custBasPk, pk_corp));

      CubasdocVO localCubasdocVO1 = retVO;

      return localCubasdocVO1;
    }
    catch (DbException e)
    {
    }
    finally
    {
      if (manager != null)
        manager.release();
    }
	return null;
  }

  public CustDivideListVO[] getCustDivideList(String[] areaCl_pk, String pk_corp)
    throws BusinessException
  {
    CustBasVO[] basVos = queryCustByAreaPKAndCorp(areaCl_pk, pk_corp);
    if ((basVos != null) && (basVos.length > 0)) {
      ArrayList al = new ArrayList();
      int i = 0; for (int count = basVos.length; i < count; ++i) {
        CustDivideListVO lvo = new CustDivideListVO();
        lvo.setPk_corp(basVos[i].getPk_corp());
        lvo.setCustcode(basVos[i].getCustcode());
        lvo.setCustname(basVos[i].getCustname());
        lvo.setPk_cubasdoc(basVos[i].getPk_cubasdoc());
        al.add(lvo);
      }
      return ((CustDivideListVO[])(CustDivideListVO[])al.toArray(new CustDivideListVO[0]));
    }
    return null;
  }

  public CustBasVO getCustBaseHeadByPk(String custBas_pk)
    throws BusinessException
  {
    return ((CustBasVO)new BaseDAO().retrieveByPK(CustBasVO.class, new CustBasMapping(), custBas_pk));
  }

  public CustDivideListVO[] getCustRightList(String strCorpID, String strGroupID)
    throws BusinessException
  {
    throw new BusinessException("The method is useless.");
  }

  public String insert(CubasdocVO vo, boolean force, String pk_settleunit)
    throws BusinessException
  {
    String key = null;
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();

      checkUserDefinedUniqueRules(vo, manager);

      checkCustCodeNameUnique((CustBasVO)vo.getCustBasVO(), pk_settleunit, force);

      CustBasVO headerVO = (CustBasVO)vo.getParentVO();

      checkSamePk_Corp1(headerVO, manager);
      key = insertCustBaseHeader(headerVO, manager);

      insertCustAddressByBasVo(manager, vo, key);

      insertCustBankByBasVo(manager, vo, pk_settleunit, key, headerVO);
    }
    catch (DbException e) {
    }
    finally {
      if (manager != null)
        manager.release();
    }
    return key;
  }

  private void insertCustBankByBasVo(PersistenceManager manager, CubasdocVO vo, String pk_settleunit, String key, CustBasVO headerVO)
    throws BusinessException, DbException
  {
    CustBankVO[] banks = (CustBankVO[])(CustBankVO[])vo.getCustBankVOs();
    insertNewCustBank(banks, headerVO.getPk_corp(), pk_settleunit);
    for (int i = 0; i < banks.length; ++i) {
      banks[i].setPk_cubasdoc(key);
    }
    insertBank(manager, banks);
  }

  private void insertCustAddressByBasVo(PersistenceManager manager, CubasdocVO vo, String key) throws DbException
  {
    CustAddrVO[] items = (CustAddrVO[])(CustAddrVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; ++i) {
      items[i].setPk_cubasdoc(key);
    }
    insertCustAddress(manager, items);
  }

  private void checkUserDefinedUniqueRules(CubasdocVO vo, PersistenceManager manager)
    throws BusinessException, DbException
  {
    ArrayList list = getFieldCodesAndFieldNames();
    if ((list == null) || (list.size() == 0))
      return;
    ArrayList code = (ArrayList)list.get(0);
    ArrayList name = (ArrayList)list.get(1);
    if ((code == null) || (code.size() == 0))
      return;
    String sql = "select b.unitname from bd_cubasdoc a left outer join bd_corp b ON a.pk_corp=b.pk_corp where ";

    String where = "";
    int i = 0; for (int count = code.size(); i < count; ++i) {
      Object value = null;
      if (((value = vo.getParentVO().getAttributeValue((String)code.get(i))) != null) && (((!(value instanceof String)) || (((String)value).length() > 0))))
      {
        if ((value instanceof String) || (value instanceof UFBoolean)) {
          where = where + " and a." + code.get(i) + " = '" + value.toString() + "' ";
        }
        else
          where = where + " and a." + code.get(i) + " = " + value.toString();
      }
      else where = where + " and a." + code.get(i) + " is null ";
    }

    String primaryKey = vo.getParentVO().getPrimaryKey();
    if ((primaryKey != null) && (primaryKey.length() > 0)) {
      where = where + " and pk_cubasdoc != '" + primaryKey + "' ";
    }
    JdbcSession session = manager.getJdbcSession();
    List result = (List)session.executeQuery(sql + where.substring(4), new ColumnListProcessor(1));

    if ((result != null) && (result.size() > 0)) {
      String unitName = (String)result.get(0);
      if (unitName == null) {
        unitName = NCLangResOnserver.getInstance().getStrByID("common", "UC001-0000072");
      }
      String msnErr = NCLangResOnserver.getInstance().getStrByID("10080806", "UPP10080806-000098", null, new String[] { unitName });

      int a = 0; for (int count = name.size(); a < count; a++) {
        msnErr = msnErr + "[" + NCLangResOnserver.getInstance().getStrByID("10080804", (String)name.get(a)) + "],";
      }

      throw new BusinessException(msnErr.substring(0, msnErr.length() - 1));
    }
  }

  private void checkCustCodeNameUnique(CustBasVO vo, String pk_settleunit, boolean force)
    throws BusinessException
  {
    int code = checkCodeAndNameUnique(vo, null, pk_settleunit);
    if (code == 2) {
      throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { "[" + NCLangResOnserver.getInstance().getStrByID("10080804", "UC000-0001587") + "]" }));
    }

    if ((force) || 
      (code != 4)) return;
    BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("10080804", "UPP10080804-000033"));

    e.setHint("1");
    throw e;
  }

  protected int checkCodeAndNameUnique(CustBasVO basDocVO, String pk_corp, String pk_settleUnit)
    throws DAOException
  {
    String checkSql = basDocVO.getCheckNameSql(pk_corp, pk_settleUnit);
    BaseDAO dao = new BaseDAO();
    List list = (List)dao.executeQuery(checkSql, new ArrayListProcessor());
    if ((list == null) || (list.size() == 0))
      return 1;
    String custCode = basDocVO.getCustcode();
    String custName = basDocVO.getCustname();
    boolean isNameUnique = false;
    int i = 0; for (int count = list.size(); i < count; ++i) {
      String code = (String)((Object[])(Object[])list.get(i))[1];
      String name = (String)((Object[])(Object[])list.get(i))[2];
      if (custCode.equalsIgnoreCase(code))
        return 2;
      if (custName.equals(name))
        isNameUnique = true;
    }
    if (isNameUnique)
      return 4;
    return 1;
  }

  protected void checkSamePk_Corp1(CustBasVO basVO, PersistenceManager manager)
    throws DbException, BusinessException
  {
    if (basVO.getCustprop().intValue() <= 0)
      return;
    String pk_corp = (basVO.getPk_corp() == null) ? "0001" : basVO.getPk_corp();
    String pk_corp1 = basVO.getPk_corp1();
    String pk_cubasdoc = basVO.getPk_cubasdoc();

    if ((pk_corp1 == null) || (pk_corp1.length() == 0) || (pk_corp == null)) {
      return;
    }
    String sql = "select count(*) from bd_cubasdoc where custprop >0 and pk_corp1 = '" + pk_corp1.trim() + "'";

    if (pk_corp.equals("0001"))
      sql = sql + " and pk_corp = '0001'";
    else {
      sql = sql + " and pk_cubasdoc in (select distinct pk_cubasdoc from bd_cumandoc where pk_corp = '" + basVO.getPk_corp() + "')";
    }

    if ((pk_cubasdoc != null) && (pk_cubasdoc.length() > 0)) {
      sql = sql + " and pk_cubasdoc <>'" + pk_cubasdoc + "'";
    }

    JdbcSession session = manager.getJdbcSession();
    Object obj = session.executeQuery(sql, new ColumnProcessor(1));
    String count = obj.toString();
    if (Integer.parseInt(count) > 0)
      throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { "[" + NCLangResOnserver.getInstance().getStrByID("10080804", "UPT10080804-000068") + "]" }));
  }

  public String insertCustBaseHeader(CustBasVO cubasdocHeader, PersistenceManager manager)
    throws DbException
  {
    String key = manager.insertObject(cubasdocHeader, new CustBasMapping());
    CacheProxy.fireDataInserted("bd_cubasdoc", key);
    return key;
  }

  private ArrayList getFieldCodesAndFieldNames()
  {
    if (this.checkcond == null) {
      this.checkcond = ((IuniqueCheckCondition)NCLocator.getInstance().lookup(IuniqueCheckCondition.class.getName()));
    }

    return this.checkcond.getFieldCodesAndFieldNames("custdoc");
  }

  private void checkCustBankUnique(PersistenceManager manager, CustBankVO[] custbank, boolean isAdd)
    throws BusinessException, DbException
  {
    String where = "1=1 ";
    if ((custbank == null) || (custbank.length == 0))
      return;
    where = where + " and account in (' '";
    for (int i = 0; i < custbank.length; ++i) {
      if ((custbank[i].getAccount() == null) || (custbank[i].getAccount().trim().length() <= 0))
        continue;
      where = where + ",'" + custbank[i].getAccount().trim() + "' ";
    }

    where = where + ") ";
    if ((custbank[0].getPk_cubasdoc() != null) && (custbank[0].getPk_cubasdoc().trim().length() > 0))
    {
      where = where + "and pk_cubasdoc = '" + custbank[0].getPk_cubasdoc() + "' ";
    }

    if (!(isAdd)) {
      where = where + "and pk_custbank <> '" + custbank[0].getPk_custbank() + "' ";
    }

    Collection c = manager.retrieveByClause(CustBankVO.class, new CustBankMapping(), where);

    if ((c != null) && (c.size() > 0)) {
      String reduplicated = "";
      for (Iterator iter = c.iterator(); iter.hasNext(); ) {
        CustBankVO bankvo = (CustBankVO)iter.next();
        reduplicated = reduplicated + "," + bankvo.getAccount();
      }
      throw new BDException(NCLangResOnserver.getInstance().getString("10080804", null, "UPP10080804-000062", null, new String[] { reduplicated.substring(1) }));
    }
  }

  public String[] insertBank(PersistenceManager manager, CustBankVO[] custbank)
    throws DbException, BusinessException
  {
    checkCustBankUnique(manager, custbank, true);
    String[] bankPks = manager.insertObject(custbank, new CustBankMapping());

    CacheProxy.fireDataInserted("bd_custbank", null);
    return bankPks;
  }

  private String[] insertCustAddress(PersistenceManager manager, CustAddrVO[] custAddr)
    throws DbException
  {
    String[] key = manager.insertObject(custAddr, new CustAddrMapping());
    CacheProxy.fireDataInserted("bd_custaddr", null);
    return key;
  }

  protected CbdocVO[] queryCustByCondiCorp(String sCon, String pk_corp, String userid)
    throws BusinessException
  {
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();

      List list = filterByPowerAreaCl(queryCustBasByCondiCorp(sCon, pk_corp), pk_corp, userid);
      if (list.size() <= 0) {
        Object localObject1 = null;
        return (CbdocVO[]) localObject1;
      }
      Vector v = new Vector();
      for (int i = 0; i < list.size(); ++i) {
    	CbdocVO vo = new CbdocVO();
        vo.setParentVO((CustBasVO)list.get(i));
        String primaryKey = ((CustBasVO)list.get(i)).getPrimaryKey();
        CustAddrVO[] addrs = getCustAddressByBasPk(manager, primaryKey);
        CustBankVO[] banks = getCustBankByBasPk(manager, primaryKey, pk_corp);
        vo.setAddrs(addrs);
        vo.setBanks(banks);
        v.addElement(vo);
      }
      CbdocVO[] Tvos = new CbdocVO[v.size()];
      v.copyInto(Tvos);
      CbdocVO[] vo = Tvos;

      return vo;
    }
    catch (DbException e)
    {
    }
    finally
    {
      if (manager != null)
        manager.release();
    }
	return null;
  }

  public CustBasVO[] queryCustBasByCondiCorp(String strCon, String pk_corp)
    throws BusinessException
  {
    CustBasVO[] custVOs = null;
    String where = "1=1 ";
    if ((pk_corp != null) && (pk_corp.trim().length() > 0)) {
      where = where + "and pk_corp = '" + pk_corp.trim() + "' ";
    }
    if ((strCon != null) && (strCon.trim().length() > 0)) {
      where = where + "and (" + strCon.trim() + ") ";
    }
    Collection c = new BaseDAO().retrieveByClause(CustBasVO.class, new CustBasMapping(), where);

    if ((c != null) && (c.size() > 0)) {
      custVOs = (CustBasVO[])(CustBasVO[])c.toArray(new CustBasVO[0]);
    }

    return custVOs;
  }

  private String[] setCumanDivideCancel(PersistenceManager manager, CustDivideListVO[] aryDel)
    throws BusinessException, DbException
  {
    if ((aryDel == null) || (aryDel.length == 0))
      return null;
    StringBuffer timeRecode = new StringBuffer();
    long currentTimeMillis = System.currentTimeMillis();
    timeRecode.append("\nBegin cancel asssigning customer.Time:" + new UFDateTime(currentTimeMillis).toString());

    ArrayList hintList = new ArrayList();

    HashMap hm = getCorpNames(aryDel);

    long newTime = System.currentTimeMillis();
    timeRecode.append("\nGet corp names cost time:" + (newTime - currentTimeMillis));
    currentTimeMillis = newTime;

    for (int i = 0; i < aryDel.length; ++i) {
      if (aryDel[i] == null)
        continue;
      String pk_corp = aryDel[i].getPk_corp();
      String corpName = (hm.get(pk_corp) == null) ? pk_corp : hm.get(pk_corp).toString();

      timeRecode.append("\n---------cancel cust '" + aryDel[i].getCustname() + "' assigned to corp '" + corpName + "'----");

      boolean referFlag = referCheckForCancelAssign(manager, aryDel[i], hintList, corpName);

      newTime = System.currentTimeMillis();
      timeRecode.append("\nChecking reference relation cost time:" + (newTime - currentTimeMillis));
      currentTimeMillis = newTime;
      if (!(referFlag)) {
        deletAssignedData(manager, aryDel[i], hintList, corpName, timeRecode);
      }
    }
    hintList.add(timeRecode.toString());
    Logger.error("Forcustcancelassigndebug:" + timeRecode.toString());
    return ((String[])(String[])hintList.toArray(new String[0]));
  }

  private void deletAssignedData(PersistenceManager manager, CustDivideListVO aryDel, ArrayList hintList, String corpName, StringBuffer timeRecode)
    throws BusinessException, DbException
  {
    long time = System.currentTimeMillis();
    String pk_cubasdoc = aryDel.getPk_cubasdoc();
    String pk_corp = aryDel.getPk_corp();
    BDOperateServ bdOpServ = new BDOperateServ("10080804", 15, 0);

    bdOpServ.beforeOperate("10080804", 15, pk_cubasdoc, pk_corp, null);

    timeRecode.append("\nExecuting beforeOperate method of registed cancelassign plugins costs time:" + (System.currentTimeMillis() - time));
    time = System.currentTimeMillis();

    CustManDocDAO dao = new CustManDocDAO();
    dao.deleteCustManByPk(pk_cubasdoc, pk_corp, manager);

    deleteCustBankByBasPkAndCorp(manager, pk_cubasdoc, pk_corp);
    timeRecode.append("\nDeleting cust mandoc and corpartion added bank costs time:" + (System.currentTimeMillis() - time));
    time = System.currentTimeMillis();
    bdOpServ.afterOperate("10080804", 15, pk_cubasdoc, pk_corp, null);

    timeRecode.append("\nExecuting afterOperate method of registed cancelassign plugins costs time:" + (System.currentTimeMillis() - time));

    hintList.add(BDMsg.MSG_DATA_ASSIGN_CANCEL_SUCCESS() + NCLangResOnserver.getInstance().getStrByID("10080804", "UC000-0001574") + ":" + aryDel.getCustname() + "," + NCLangResOnserver.getInstance().getStrByID("commonres", "UC000-0000404") + ":" + corpName + "\n");
  }

  private boolean referCheckForCancelAssign(PersistenceManager manager, CustDivideListVO aryDel, ArrayList hintList, String corpName)
    throws DbException, BusinessException
  {
    String pk_cubasdoc = aryDel.getPk_cubasdoc();
    String pk_corp = aryDel.getPk_corp();
    String sqlMan = "select pk_cumandoc from bd_cumandoc where pk_corp=? and pk_cubasdoc= ?";
    SQLParameter para = new SQLParameter();
    para.addParam(pk_corp);
    para.addParam(pk_cubasdoc);
    ArrayList list = (ArrayList)manager.getJdbcSession().executeQuery(sqlMan, para, new ColumnListProcessor());

    IReferenceCheck iIReferenceCheck = (IReferenceCheck)NCLocator.getInstance().lookup(IReferenceCheck.class.getName());

    boolean referFlag = iIReferenceCheck.isReferenced("bd_cumandoc", list);
    if (!(referFlag))
    {
      ArrayList listBankPk = getBankPksByCustCorp(manager, pk_cubasdoc, pk_corp);

      referFlag = iIReferenceCheck.isReferenced("bd_custbank", listBankPk);
    }

    if (referFlag) {
      hintList.add(BDMsg.MSG_DATA_ASSIGN_CANCEL_FAIL() + ":" + NCLangResOnserver.getInstance().getStrByID("10080804", "UPP10080804-000012") + ", " + NCLangResOnserver.getInstance().getStrByID("10080804", "UC000-0001578") + ":" + aryDel.getCustname() + "," + NCLangResOnserver.getInstance().getStrByID("commonres", "UC000-0000404") + ":" + corpName + "\n");
    }

    return referFlag;
  }

  private void deleteCustBankByBasPkAndCorp(PersistenceManager manager, String pk_cubasdoc, String pk_corp) throws DbException
  {
    BDDelLog dellog = new BDDelLog();
    dellog.delByWhereClause("bd_custbank", "pk_custbank", " pk_corp = '" + pk_corp + "' and pk_cubasdoc = '" + pk_cubasdoc + "'");

    String deleteBank = "delete from bd_custbank where pk_corp = '" + pk_corp + "' and pk_cubasdoc = '" + pk_cubasdoc + "'";

    manager.getJdbcSession().executeUpdate(deleteBank);
  }

  private ArrayList getBankPksByCustCorp(PersistenceManager manager, String pk_cubasdoc, String pk_corp)
    throws DbException
  {
    String selectBank = "select pk_custbank from bd_custbank where pk_corp = ? and pk_cubasdoc = ?";
    SQLParameter para = new SQLParameter();
    para.addParam(pk_corp);
    para.addParam(pk_cubasdoc);
    return ((ArrayList)manager.getJdbcSession().executeQuery(selectBank, para, new ColumnListProcessor()));
  }

  private HashMap getCorpNames(CustDivideListVO[] aryDel)
    throws BusinessException
  {
    HashMap ht = new HashMap();
    if (aryDel != null) {
      ArrayList list = new ArrayList();
      for (int i = 0; i < aryDel.length; ++i) {
        if ((aryDel[i].getPk_corp() == null) || (aryDel[i].getPk_corp().length() <= 0))
          continue;
        list.add(aryDel[i].getPk_corp());
      }
      if (list.size() > 0) {
        String[] corpNames = getCorpNames(list);

        for (int i = 0; i < corpNames.length; ++i) {
          ht.put(list.get(i), corpNames[i]);
        }
      }
    }
    return ht;
  }

  public void update(CubasdocVO vo, boolean force, String pk_settleunit)
    throws BusinessException
  {
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();

      defaultBankUniqueCheck(vo, manager);

      checkUserDefinedUniqueRules(vo, manager);

      checkCustCodeNameUnique((CustBasVO)vo.getCustBasVO(), pk_settleunit, force);

      CustBasVO custBasVo = (CustBasVO)vo.getParentVO();

      checkSamePk_Corp1((CustBasVO)vo.getParentVO(), manager);
      IsLoopPkCubasdoc1(custBasVo.getPk_cubasdoc1(), custBasVo.getPk_cubasdoc());

      String strHeadPK = custBasVo.getPrimaryKey();

      updateCustAddress(manager, vo);
      processCustBank(manager, vo, pk_settleunit);
      processCustHead(manager, custBasVo);
      updateCumanPricgroup(strHeadPK, manager);
    }
    catch (DbException e)
    {
    }
    finally {
      if (manager != null)
        manager.release();
    }
  }

  private void processCustHead(PersistenceManager manager, CustBasVO custBasVo)
    throws BusinessException
  {
    CustBasVO oldbasVO = getCustBaseHeadByPk(custBasVo.getPrimaryKey());

    UFDateTime dtime = custBasVo.getTs();
    UFDateTime olddTime = oldbasVO.getTs();
    if ((dtime != null) && (olddTime != null) && 
      (!(dtime.equals(olddTime)))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000604"));
    }

    BDOperateContextObject bd_datas = new BDOperateContextObject(custBasVo, oldbasVO, new CAVOBDBaseDocAdapter(custBasVo, "pk_cubasdoc", "custcode", "custname"));

    BDOperateServ operServ = new BDOperateServ();
    operServ.beforeOperate("10080804", 3, custBasVo.getPrimaryKey(), null, bd_datas);

    updateCustBaseHead(custBasVo);
    operServ.afterOperate("10080804", 3, custBasVo.getPrimaryKey(), null, bd_datas);
  }

  private void processCustBank(PersistenceManager manager, CubasdocVO vo, String pk_settleunit)
    throws BusinessException, DbException
  {
    CustBankVO[] banks = (CustBankVO[])(CustBankVO[])vo.getCustBankVOs();
    CustBasVO custBasVO = (CustBasVO)vo.getParentVO();
    insertNewCustBank(banks, custBasVO.getPk_corp(), pk_settleunit);
    List insert = new ArrayList();
    for (int i = 0; i < ((banks == null) ? 0 : banks.length); ++i) {
      switch (banks[i].getStatus())
      {
      case 2:
        banks[i].setPk_cubasdoc(custBasVO.getPrimaryKey());
        insert.add(banks[i]);
        break;
      case 1:
        updateBank(manager, banks[i]);
        break;
      case 3:
        deleteCustBank(manager, banks[i]);
      }
    }
    if (insert.size() > 0)
      insertBank(manager, (CustBankVO[])(CustBankVO[])insert.toArray(new CustBankVO[0]));
  }

  private void updateCustAddress(PersistenceManager manager, CubasdocVO vo)
    throws DbException
  {
    String strHeadPK = ((CustBasVO)vo.getParentVO()).getPrimaryKey();
    CustAddrVO[] items = (CustAddrVO[])(CustAddrVO[])vo.getChildrenVO();
    List list = new ArrayList();
    for (int i = 0; i < ((items == null) ? 0 : items.length); ++i) {
      switch (items[i].getStatus())
      {
      case 2:
        items[i].setPk_cubasdoc(strHeadPK);
        list.add(items[i]);
        break;
      case 1:
        updateCustAddress(manager, items[i]);
        break;
      case 3:
        deleteAddressByVo(manager, items[i]);
      }
    }
    if (list.size() > 0)
      insertCustAddress(manager, (CustAddrVO[])(CustAddrVO[])list.toArray(new CustAddrVO[0]));
  }

  private void defaultBankUniqueCheck(CubasdocVO vo, PersistenceManager manager)
    throws BusinessException, DbException
  {
    CustBankVO[] banks = (CustBankVO[])(CustBankVO[])vo.getCustBankVOs();

    ArrayList list = new ArrayList();

    boolean isNeedCheck = false;

    String pk_corp = null;

    int count = 0;

    for (int i = 0; i < ((banks == null) ? 0 : banks.length); ++i)
    {
      if ((((banks[i].getStatus() == 1) || (banks[i].getStatus() == 3))) && 
        (banks[i].getPrimaryKey() != null)) {
        list.add(banks[i].getPrimaryKey().trim());
      }

      if (((banks[i].getStatus() != 1) && (banks[i].getStatus() != 2)) || 
        (banks[i].getDefflag() == null) || (!(banks[i].getDefflag().booleanValue())))
        continue;
      isNeedCheck = true;
      pk_corp = banks[i].getPk_corp();
      ++count;
    }

    if (count > 1) {
      String lang = NCLangResOnserver.getInstance().getCurrLanguage().getCode();

      throw new BusinessException(NCLangResOnserver.getInstance().getString(lang, "10080806", null, "UPP10080806-000088", null, new String[] { "" + count }));
    }

    CustBasVO basevo = (CustBasVO)vo.getParentVO();

    String base_pk = basevo.getPrimaryKey();

    String base_corp = basevo.getPk_corp();

    if (isNeedCheck) {
      String exceptBankPk = "";
      if (list.size() > 0) {
        String[] corps = (String[])(String[])list.toArray(new String[0]);
        for (int i = 0; i < corps.length; ++i) {
          exceptBankPk = exceptBankPk + ",'" + corps[i] + "'";
        }
        exceptBankPk = exceptBankPk.substring(1);
      }
      JdbcSession session = manager.getJdbcSession();
      String check;
      if ((("0001".equals(base_corp)) && ("0001".equals(pk_corp))) || (!("0001".equals(base_corp))))
      {
        check = " select distinct pk_corp from bd_custbank where defflag = 'Y' and pk_cubasdoc = '" + base_pk + "' ";

        if ((exceptBankPk != null) && (exceptBankPk.length() > 0))
          check = check + " and pk_custbank not in (" + exceptBankPk + ")";
        List corpList = (List)session.executeQuery(check, new ColumnListProcessor(1));

        if (corpList.size() > 0) {
          if ("0001".equals(base_corp)) {
            String[] corpNames = getCorpNames(corpList);
            String err = NCLangResOnserver.getInstance().getStrByID("10080806", "UPP10080806-000085") + "\n";

            for (int i = 0; i < corpNames.length; ++i) {
              if (i != 0)
                err = err + ",";
              err = err + corpNames[i];
            }
            err = err + ".";
            throw new BusinessException(err);
          }
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10080806", "UPP10080806-000086"));
        }

      }
      else
      {
        check = " select count(*) from bd_custbank where defflag = 'Y' and (pk_corp = '0001' or pk_corp = '" + pk_corp + "') and pk_cubasdoc = '" + base_pk + "'";

        if ((exceptBankPk != null) && (exceptBankPk.length() > 0))
          check = check + " and pk_custbank not in (" + exceptBankPk + ")";
        Object obj = session.executeQuery(check, new ColumnProcessor(1));

        if ((obj != null) && (Integer.parseInt(obj.toString()) > 0))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10080806", "UPP10080806-000087"));
      }
    }
  }

  private String[] getCorpNames(List corpList)
    throws BusinessException
  {
    String[] corpNames = ((ICorpQry)NCLocator.getInstance().lookup(ICorpQry.class.getName())).getCorpNames((String[])(String[])corpList.toArray(new String[0]));

    return corpNames;
  }

  private void updateBank(PersistenceManager manager, CustBankVO custbank)
    throws DbException, BusinessException
  {
    checkCustBankUnique(manager, new CustBankVO[] { custbank }, false);
    manager.updateObject(custbank, new CustBankMapping());
    CacheProxy.fireDataUpdated("bd_custbank", custbank.getPrimaryKey());
  }

  public void updateForEx(CubasdocVO vo)
    throws BusinessException
  {
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();
      String sCustCode = ((CustBasVO)vo.getParentVO()).getCustcode();
      if ((sCustCode != null) && (sCustCode.length() != 0)) {
        checkCodeUniqueForEx(vo);

        updateCustAddress(manager, vo);
        processCustBank(manager, vo, null);
        processCustHead(manager, (CustBasVO)vo.getParentVO());
      }
    }
    catch (DbException e) {
    }
    finally {
      if (manager != null)
        manager.release();
    }
  }

  private void checkCodeUniqueForEx(CubasdocVO vo)
    throws DAOException, BusinessException
  {
    String sqlCheckName = ((CustBasVO)vo.getParentVO()).getCheckNameSqlForExChange(null, null);

    Boolean Bln_foundSameCode = (Boolean)new BaseDAO().executeQuery(sqlCheckName, new BooleanResultSetProcessor());

    if (Bln_foundSameCode.booleanValue())
      throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { "[" + NCLangResOnserver.getInstance().getStrByID("10080804", "UC000-0001587") + "]" }));
  }

  private void updateCustBaseHead(CustBasVO cubasdocHeader)
    throws BusinessException
  {
    int  i = new BaseDAO().updateObject(cubasdocHeader, new CustBasMapping());
    CacheProxy.fireDataUpdated("bd_cubasdoc", cubasdocHeader.getPrimaryKey());
  }

  private void updateCustAddress(PersistenceManager manager, CustAddrVO cubasdocItem)
    throws DbException
  {
    manager.updateObject(cubasdocItem, new CustAddrMapping());
    CacheProxy.fireDataUpdated("bd_custaddr", cubasdocItem.getPrimaryKey());
  }

  public void updateCumanPricgroup(String strPk_cubasdoc, PersistenceManager manager)
    throws DbException
  {
    String sql = "select pk_pricegroup from bd_cubasdoc where pk_cubasdoc='" + strPk_cubasdoc + "' ";

    JdbcSession session = manager.getJdbcSession();
    Object pk_pricegroup = session.executeQuery(sql, new ColumnProcessor(1));

    if (pk_pricegroup != null) {
      sql = "update bd_cumandoc set pk_pricegroupcorp='" + pk_pricegroup.toString() + "' where pk_cubasdoc='" + strPk_cubasdoc + "' and bd_cumandoc.pk_pricegroupcorp is null ";

      session.executeUpdate(sql);
    }
  }

  public String[] setCumanDoc(CustDivideListVO[] aryCustData, CustDivideListVO[] aryDel, GeneralExVO defaultValueVO)
    throws BusinessException, SQLException
  {
    String[] aryReturn = null;
    PersistenceManager manager = null;
    try {
      manager = PersistenceManager.getInstance();
      if (aryCustData != null)
      {
        String vtn = createTempTableForAssign(manager);

        putAssignCustToTempTable(manager, aryCustData, vtn);

        filterAssignedCust(manager, vtn);

        uniqueCorpCheckOfInnerCust(manager, vtn);

        CustDivideListVO[] toAssign = getNeedAssignCust(manager, vtn, aryCustData);

        if ((toAssign != null) && (toAssign.length > 0)) {
          assignCustToCorp(manager, defaultValueVO, toAssign);
        }
      }
      aryReturn = setCumanDivideCancel(manager, aryDel);
    }
    catch (DbException e) {
    }
    finally {
      if (manager != null)
        manager.release();
    }
    return aryReturn;
  }

  private String createTempTableForAssign(PersistenceManager manager)
    throws SQLException
  {
    TempTable tmptab = new TempTable();
    String vtn = tmptab.createTempTable(manager.getJdbcSession().getConnection(), "custbasSet", "basid char(20),corp char(4), pk_corp1 char(4), ts char(19)", "pk_corp1");

    return vtn;
  }

  private void assignCustToCorp(PersistenceManager manager, GeneralExVO defaultValueVO, CustDivideListVO[] toAssign)
    throws DbException
  {
    Hashtable ht_custFlag1 = new Hashtable();
    Hashtable ht_custFlag2 = new Hashtable();
    ht_custFlag1.put("0", "0");
    ht_custFlag2.put("0", "4");
    ht_custFlag1.put("1", " ");
    ht_custFlag2.put(" ", "1");
    ht_custFlag1.put("2", "2");
    ht_custFlag2.put("2", "3");

    CumandocVO sourceVO1 = new CumandocVO(defaultValueVO, true);
    if (sourceVO1.getFrozenflag() == null)
      sourceVO1.setFrozenflag(new UFBoolean(false));
    sourceVO1.setPrimaryKey(null);

    CumandocVO sourceVO2 = new CumandocVO(defaultValueVO, false);
    if (sourceVO2.getFrozenflag() == null)
      sourceVO2.setFrozenflag(new UFBoolean(false));
    sourceVO2.setPrimaryKey(null);

    String oldFlag = sourceVO1.getCustflag();
    if ((oldFlag == null) || ((!(oldFlag.equals("0"))) && (!(oldFlag.equals("1"))) && (!(oldFlag.equals("2")))))
    {
      oldFlag = "0";
    }
    sourceVO1.setCustflag(ht_custFlag1.get(oldFlag).toString());
    sourceVO2.setCustflag(ht_custFlag2.get(sourceVO1.getCustflag()).toString());

    int assignCount = toAssign.length;
    CumandocVO[] cumanVOArray = new CumandocVO[assignCount * 2];
    for (int i = 0; i < assignCount; ++i)
    {
      CumandocVO cumanVO = (CumandocVO)sourceVO1.clone();
      cumanVO.setPk_corp(toAssign[i].getPk_corp());
      cumanVO.setPk_cubasdoc(toAssign[i].getPk_cubasdoc());
      cumanVO.setPk_pricegroupcorp(toAssign[i].getPk_pricegroup());
      cumanVO.setCmnecode(toAssign[i].getMnecode());
      cumanVOArray[(i * 2)] = cumanVO;

      CumandocVO cumanVO2 = (CumandocVO)sourceVO2.clone();
      cumanVO2.setPk_corp(toAssign[i].getPk_corp());
      cumanVO2.setPk_cubasdoc(toAssign[i].getPk_cubasdoc());
      cumanVO2.setPk_pricegroupcorp(toAssign[i].getPk_pricegroup());
      cumanVO2.setCmnecode(toAssign[i].getMnecode());
      cumanVOArray[(i * 2 + 1)] = cumanVO2;
    }
    manager.insertObject(cumanVOArray, new CustManMapping());
  }

  private CustDivideListVO[] getNeedAssignCust(PersistenceManager manager, String vtn, CustDivideListVO[] aryCustData)
    throws DbException
  {
    String select = "select basid,corp from " + vtn;
    List list = (List)manager.getJdbcSession().executeQuery(select, new ArrayListProcessor());

    HashMap map = Hashlize.hashlizeObjects(aryCustData, new IHashKey() {
      public String getKey(Object o) {
        CustDivideListVO vo = (CustDivideListVO)o;
        return vo.getPk_cubasdoc() + vo.getPk_corp();
      }
    });
    if (list.size() > 0) {
      List voList = new ArrayList();
      for (Iterator iter = list.iterator(); iter.hasNext(); ) {
        Object[] obj = (Object[])(Object[])iter.next();
        String key = ((String)obj[0]) + ((String)obj[1]);
        if (map.containsKey(key)) {
          voList.add(((List)map.get(key)).get(0));
        }
      }
      return ((CustDivideListVO[])(CustDivideListVO[])voList.toArray(new CustDivideListVO[0]));
    }
    return null;
  }

  private void filterAssignedCust(PersistenceManager manager, String vtn)
    throws DbException
  {
    String filter = "delete from " + vtn + " where exists (select 1 from bd_cumandoc where " + vtn + ".basid = bd_cumandoc.pk_cubasdoc and " + vtn + ".corp = bd_cumandoc.pk_corp)";

    manager.getJdbcSession().executeUpdate(filter);
  }

  private void putAssignCustToTempTable(PersistenceManager manager, CustDivideListVO[] aryCustData, String vtn) throws DbException
  {
    String insert = "insert into " + vtn + " (basid,corp) values(?,?) ";
    for (int i = 0; i < aryCustData.length; ++i) {
      SQLParameter para = new SQLParameter();
      para.addParam(aryCustData[i].getPk_cubasdoc());
      para.addParam(aryCustData[i].getPk_corp());
      manager.getJdbcSession().executeUpdate(insert, para);
    }
  }

  private void uniqueCorpCheckOfInnerCust(PersistenceManager manager, String vtn)
    throws BusinessException, DbException
  {
    StringBuffer update = new StringBuffer();
    update.append("update ").append(vtn).append(" set pk_corp1=bd_cubasdoc.pk_corp1 from ").append(vtn).append(", bd_cubasdoc where ").append(vtn).append(".basid=bd_cubasdoc.pk_cubasdoc");

    manager.getJdbcSession().executeUpdate(update.toString());

    StringBuffer select = new StringBuffer();
    select.append("select custcode from bd_cubasdoc bas inner join ").append(vtn).append(" cust ").append(" on bas.pk_corp1 = cust.pk_corp1 ").append("where bas.pk_corp in (select distinct corp from ").append(vtn).append(") and bas.pk_corp1 is not null");

    List list = (List)manager.getJdbcSession().executeQuery(select.toString(), new ColumnListProcessor());

    if (list.size() > 0) {
      StringBuffer hint = new StringBuffer();
      for (Iterator iter = list.iterator(); iter.hasNext(); ) {
        String custCode = (String)iter.next();
        hint.append(",").append(custCode);
      }
      throw new BusinessException(BDMsg.MSG_DATA_ASSIGN_FAIL() + NCLangResOnserver.getInstance().getStrByID("10080804", "UPP10080804-000014") + ", " + NCLangResOnserver.getInstance().getStrByID("10080804", "UC000-0001587") + ":" + hint.substring(1));
    }
  }

  private void IsLoopPkCubasdoc1(String pk_father, String pk_cubasdoc)
    throws BusinessException
  {
    if ((pk_father == null) || (pk_cubasdoc == null))
      return;
    boolean isLoop = false;
    int count = 0;
    Set set = new HashSet();
    set.add(pk_cubasdoc);
    set.add(pk_father);

    String strFather = pk_father;
    while ((!(isLoop)) && (count < 20)) {
      String[] pks = getFatherCubasdoc(strFather, true);
      if (pks == null) break; if (pks[0] == null)
        break;
      String father = pks[0];
      if (set.contains(father)) {
        isLoop = true;
        break;
      }
      set.add(father);
      strFather = father;

      ++count;
    }
    if (isLoop)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10080804", "UPP10080804-000013"));
  }

  String[] getFatherCubasdoc(String pk_cubasdoc, boolean isFindFather)
    throws DAOException
  {
    String sql = null;
    if (isFindFather) {
      sql = "select pk_cubasdoc1 from bd_cubasdoc where pk_cubasdoc = '" + pk_cubasdoc + "'";
    }
    else {
      sql = "select pk_cubasdoc from bd_cubasdoc where pk_cubasdoc1 = '" + pk_cubasdoc + "'";
    }
    List list = (List)new BaseDAO().executeQuery(sql, new ColumnListProcessor());

    return ((String[])(String[])list.toArray(new String[0]));
  }

  private void insertNewCustBank(CustBankVO[] banks, String pk_corp, String pk_settleunit)
    throws BusinessException
  {
    if ((banks == null) || (banks.length == 0))
      return;
    int[] index = new int[banks.length];
    List bankList = new ArrayList();
    int count = 0;
    for (int i = 0; i < banks.length; ++i) {
      CustBankVO bank = banks[i];
      if ((banks[i].getStatus() == 3) || (banks[i].getPk_accbank() != null))
        continue;
      AccbankVO tempVO = new AccbankVO();
      if (bank.getPk_corp() != null)
        tempVO.setPk_corp(bank.getPk_corp());
      else {
        tempVO.setPk_corp(pk_corp);
      }
      if (bank.getPk_corp() != null)
        tempVO.setCurrentcorp(bank.getPk_corp());
      else {
        tempVO.setCurrentcorp(pk_corp);
      }
      tempVO.setPk_currtype(bank.getPk_currtype());

      tempVO.setBankacc(bank.getAccount());
      tempVO.setBankname(bank.getAccname());
      tempVO.setAddress(bank.getAccaddr());
      tempVO.setMemo(bank.getMemo());

      tempVO.setBankowner("¹¤ÐÐ");

      tempVO.setGenebranprop(new Integer(3));

      tempVO.setNetbankflag(new Integer(2));
      tempVO.setNetqueryflag(new Integer(0));

      tempVO.setBanktype(new Integer(0));

      tempVO.setCtlprop(new Integer(0));

      tempVO.setArapprop(new Integer(2));

      tempVO.setPk_createunit(pk_settleunit);
      tempVO.setSealflag(new UFBoolean(false));
      tempVO.setGroupaccount(Integer.valueOf(0));
      tempVO.setSignflag(UFBoolean.FALSE);
      bankList.add(tempVO);
      index[(count++)] = i;
    }

    if (bankList.size() > 0) {
      IAccbankPrivate bank = (IAccbankPrivate)NCLocator.getInstance().lookup(IAccbankPrivate.class.getName());

      String[] keys = bank.insertAccbankVOs((AccbankVO[])(AccbankVO[])bankList.toArray(new AccbankVO[0]));

      for (int i = 0; i < keys.length; ++i)
        banks[index[i]].setPk_accbank(keys[i]);
    }
  }

  class BooleanResultSetProcessor
    implements ResultSetProcessor
  {
    private static final long serialVersionUID = -8747161868534149216L;

    public Object handleResultSet(ResultSet rs)
      throws SQLException
    {
      if (rs.next()) {
        return new Boolean(true);
      }
      return new Boolean(false);
    }
  }
}