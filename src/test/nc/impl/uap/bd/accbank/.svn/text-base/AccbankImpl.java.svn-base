package nc.impl.uap.bd.accbank;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.pub.DefaultIsNeedChkUpdateRef;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.bduifactory.busicom.BDDocSyncInform;
import nc.bs.pub.pflock.IConsistenceCheck;
import nc.bs.pub.pflock.ILockData;
import nc.bs.pub.pflock.IPfBusinessLock;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.trade.lock.BDConsistenceCheck;
import nc.bs.trade.lock.BDLockData;
import nc.bs.uap.bd.BDException;
import nc.impl.uap.bd.account.AccountDocImpl;
import nc.impl.uap.bd.account.SettleAccountImpl;
import nc.itf.uap.bd.ISettleCenter;
import nc.itf.uap.bd.accbank.IAccbank;
import nc.itf.uap.bd.accbank.IAccbankPrivate;
import nc.itf.uap.bd.account.IAccountDoc;
import nc.itf.uap.bd.account.ISettleAccount;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.BDRes;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b120.AccidVO;
import nc.vo.bd.b23.AccbankFieldsMapping;
import nc.vo.bd.b23.AccbankFtsVO;
import nc.vo.bd.b23.AccbankHeaderVO;
import nc.vo.bd.b23.AccbankVO;
import nc.vo.logging.Debug;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;

public class AccbankImpl
  implements IAccbank, IAccbankPrivate
{
  BaseDAO m_baseDAO = null;
  ISettleCenter m_iSettleCenter = null;

  private static final HashMap field_column_map = AccbankFieldsMapping.getAllFieldsColumnMap();
  private static final HashMap field_type_map = AccbankFieldsMapping.getAllFieldsTypeMap();

  private ISettleCenter getISettleCenter()
    throws BusinessException
  {
    if (this.m_iSettleCenter == null) {
      try {
        this.m_iSettleCenter = ((ISettleCenter)NCLocator.getInstance().lookup(ISettleCenter.class.getName()));
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
        throw new BDException(e.getMessage());
      }
    }
    return this.m_iSettleCenter;
  }

  private boolean isSettleCenter(AccbankVO vo) throws BusinessException {
    String pk_corp = null;
    if ((vo.getCurrentcorp() != null) && (vo.getCurrentcorp().trim().length() > 0))
      pk_corp = vo.getCurrentcorp();
    else {
      pk_corp = vo.getPk_corp();
    }
    return getISettleCenter().isSettleCenter(pk_corp);
  }

  private String getFilterFromVO(SuperVO vo, String fieldName, int fieldType) {
    if ((vo == null) || (fieldName == null)) return null;
    String where = "";

    Object o = vo.getAttributeValue(fieldName);
    if (o == null) return null;

    where = where + ((String)field_column_map.get(fieldName));
    where = where + " = ";
    switch (fieldType) {
    case 0:
    case 4:
    case 7:
    case 11:
      where = where + o.toString();
      break;
    case 1:
    case 2:
    case 3:
    case 5:
    case 6:
    case 9:
      where = where + "'" + o.toString() + "'";
    case 8:
    case 10:
    }
    return where;
  }

  private static Object getVoValueFromRS(int uftype, ResultSet rs, int index) throws SQLException {
    Object value = null;
    switch (uftype)
    {
    case 0:
      int iv = rs.getInt(index);
      if (rs.wasNull()) break;
      value = new Integer(iv); break;
    case 7:
      long lv = rs.getLong(index);
      if (rs.wasNull()) break;
      value = new Long(lv); break;
    case 1:
      String s = rs.getString(index);
      if (s == null) break;
      value = s.trim(); break;
    case 2:
      String d = rs.getString(index);
      if ((d == null) || ((d = d.trim()).length() <= 0)) break;
      value = new UFDate(d); break;
    case 3:
      String dt = rs.getString(index);
      if ((dt == null) || ((dt = dt.trim()).length() <= 0)) break;
      value = new UFDateTime(dt); break;
    case 6:
      String t = rs.getString(index);
      if ((t == null) || ((t = t.trim()).length() <= 0)) break;
      value = new UFTime(t); break;
    case 4:
      value = DataManageObject.getUFDouble(rs, index, 8);
      break;
    case 11:
      BigDecimal bd = rs.getBigDecimal(index);
      if (bd == null) break;
      value = new UFDouble(bd); break;
    case 5:
      String b = rs.getString(index);
      if ((b == null) || ((b = b.trim()).length() <= 0)) break;
      value = new UFBoolean(b); break;
    case 9:
      value = rs.getTimestamp(index);
      break;
    case 8:
      byte bv = rs.getByte(index);
      if (rs.wasNull()) break;
      value = new Byte(bv); break;
    case 10:
      value = rs.getBytes(index);
      break;
    default:
      throw new SQLException("Not supported type -- " + uftype + "!");
    }
    label436: return value;
  }

  public void deleteAccbankVOsByPks(String[] accbankPks)
    throws BusinessException
  {
//    if ((accbankPks == null) || (accbankPks.length == 0)) return;
//
//    IPfBusinessLock bdLock = new PfBusinessLock();
//    try {
//      bdLock.lock(new ILockData(accbankPks) {
//        public String[] getLockPks() throws BusinessException {
//          return this.val$accbankPks;
//        }
//      }
//      , new IConsistenceCheck()
//      {
//        public void checkConsistence()
//          throws BusinessException
//        {
//        }
//
//      });
//      IReferenceCheck refcheck = (IReferenceCheck)NCLocator.getInstance().lookup(IReferenceCheck.class.getName());
//      for (int i = 0; i < accbankPks.length; ++i) {
//        String accbankPk = accbankPks[i];
//        if (refcheck.isReferenced("bd_accbank", accbankPk)) {
//          throw new BDException(BDRes.getS("{0}已经被{1}引用", "MC8", new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("10081602", "UPP10081602-000000"), null }) + "," + BDRes.getS("不能{0}", "MP1", new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000039") }));
//        }
//
//      }
//
//      BaseDAO baseDAO = getBaseDAO();
//
//      baseDAO.deleteByPKs(AccbankVO.class, accbankPks);
//
//      BDDelLog delLog = new BDDelLog();
//      delLog.delPKs("bd_accbank", accbankPks);
//
//      InSqlBatchCaller caller = new InSqlBatchCaller(accbankPks);
//      caller.execute(new IInSqlBatchCallBack(delLog) {
//        public Object doWithInSql(String inSql) throws BusinessException, SQLException {
//          try {
//            this.val$delLog.delByWhereClause("bd_accbank_fts", "pk_accbank_fts", "pk_accbank in " + inSql);
//          } catch (Exception e) {
//            Logger.error(e.getMessage(), e);
//            throw new BDException(e.getMessage());
//          }
//          return null;
//        }
//      });
//      caller.execute(new IInSqlBatchCallBack(baseDAO) {
//        public Object doWithInSql(String inSql) throws BusinessException, SQLException {
//          try {
//            this.val$baseDAO.deleteByClause(AccbankFtsVO.class, "pk_accbank in " + inSql);
//          } catch (Exception e) {
//            Logger.error(e.getMessage(), e);
//            throw new BDException(e.getMessage());
//          }
//          return null;
//        }
//
//      });
//    }
//    catch (BusinessException e)
//    {
//    }
//    catch (Exception e)
//    {
//    }
//    finally
//    {
//      if (bdLock != null)
//        bdLock.unLock();
//    }
  }

  public void deleteAccbankVO(AccbankVO vo)
    throws BusinessException
  {
    IPfBusinessLock bdLock = new PfBusinessLock();
    try {
      HYBillVO billVo = new HYBillVO();
      billVo.setParentVO(vo);
      bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));

      IReferenceCheck refcheck = (IReferenceCheck)NCLocator.getInstance().lookup(IReferenceCheck.class.getName());
      if (refcheck.isReferenced(vo.getTableName(), vo.getPrimaryKey())) {
        throw new BusinessException(BDMsg.MSG_REF_NOT_DELETE());
      }

      deleteDB(vo);

      new BDDelLog().delPKs(vo.getTableName(), new String[] { vo.getPrimaryKey() });
    }
    catch (BusinessException e)
    {
    }
    catch (Exception e)
    {
    }
    finally
    {
      if (bdLock != null)
        bdLock.unLock();
    }
  }

  private void deleteDB(AccbankVO vo)
    throws BusinessException
  {
    try
    {
      AccbankHeaderVO header = vo.getHeaderVO();
      AccbankFtsVO fts = vo.getFtsVO();
      getBaseDAO().deleteVO(header);
      deleteFtsVO(fts);
      new BDDelLog().delPKs(header.getTableName(), new String[] { header.getPrimaryKey() });
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
  }

  private void deleteFtsVO(AccbankFtsVO fts) throws BusinessException
  {
    try {
      if ((fts.getPrimaryKey() != null) && (fts.getPrimaryKey().trim().length() > 0))
      {
        getBaseDAO().deleteVO(fts);
        new BDDelLog().delPKs(fts.getTableName(), new String[] { fts.getPrimaryKey() });
      }
      else {
        String where = "pk_accbank = '" + fts.getPk_accbank() + "' ";
        new BDDelLog().delByWhereClause(fts.getTableName(), "pk_accbank_fts", where);
        getBaseDAO().deleteByClause(AccbankFtsVO.class, where);
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
  }

  public AccbankVO queryAccbankVOByPk(String key) throws BusinessException {
    AccbankVO vo = null;
    try
    {
      AccbankHeaderVO header = (AccbankHeaderVO)getBaseDAO().retrieveByPK(AccbankHeaderVO.class, key);

      AccbankFtsVO fts = null;
      if ((header != null) && (header.getPk_corp() != null))
      {
        String where = "pk_accbank = '" + header.getPk_accbank() + "' ";
        where = where + "and pk_corp = '" + header.getPk_corp().trim() + "' ";
        Collection c = getBaseDAO().retrieveByClause(AccbankFtsVO.class, where);
        SuperVO[] tempvos = ((c == null) || (c.size() == 0)) ? null : (AccbankFtsVO[])(AccbankFtsVO[])c.toArray(new AccbankFtsVO[c.size()]);

        fts = (tempvos == null) ? null : (AccbankFtsVO)tempvos[0];
        vo = new AccbankVO(header, fts);
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return vo;
  }

  public AccbankVO queryAccbankVOByPkAndCorp(String key, String pk_corp)
    throws BusinessException
  {
    AccbankVO vo = null;
    try
    {
      AccbankHeaderVO header = (AccbankHeaderVO)getBaseDAO().retrieveByPK(AccbankHeaderVO.class, key);
      AccbankFtsVO fts = null;

      String where = "pk_accbank = '" + header.getPk_accbank() + "' ";
      where = where + "and pk_corp = '" + pk_corp + "' ";
      Collection c = getBaseDAO().retrieveByClause(AccbankFtsVO.class, where);
      SuperVO[] tempvos = ((c == null) || (c.size() == 0)) ? null : (SuperVO[])(SuperVO[])c.toArray(new SuperVO[c.size()]);

      fts = (tempvos == null) ? null : (AccbankFtsVO)tempvos[0];

      vo = new AccbankVO(header, fts);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return vo;
  }

  private BaseDAO getBaseDAO() {
    if (this.m_baseDAO == null) {
      this.m_baseDAO = new BaseDAO();
    }
    return this.m_baseDAO;
  }

  private void handleException(Exception e) throws BusinessException {
    String msg = e.getMessage();
    if ((msg == null) || (msg.trim().length() == 0)) msg = e.getClass().getName();
    Logger.error(e.getMessage(), e);
    throw new BDException(e.getMessage());
  }

  private void checkUnitAccordance(AccbankVO vo)
    throws Exception
  {
    String pk_accid = vo.getPk_accid();
    if (pk_accid == null) return;

    ISettleAccount settleAccount = new SettleAccountImpl();
    AccidVO accid = settleAccount.querySettleAccountByPk(pk_accid);

    String pk_settleunit = (vo.getPk_settleunit() == null) ? "" : vo.getPk_settleunit();
    String pk_settlecent = (vo.getPk_settlecent() == null) ? "" : vo.getPk_settlecent();
    String accidSettleunit = (accid == null) ? "" : accid.getPk_settleunit();
    String accidSettlecent = (accid == null) ? "" : accid.getPk_settlecent();
    if ((!(pk_settleunit.equalsIgnoreCase(accidSettleunit))) && (!(pk_settlecent.equalsIgnoreCase(accidSettlecent))))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000004"));
  }

  private void checkCorrespondAccidNormal(AccbankVO vo, boolean isAdd)
    throws Exception
  {
    if ((isAdd) || (vo.getPk_accid() == null) || (vo.getPk_accid().length() == 0))
      return;
    String where = " accflag <> 3 and  pk_accbank = '" + vo.getPrimaryKey() + "'";
    Collection c = getBaseDAO().retrieveByClause(AccidVO.class, where);
    SuperVO[] accidvo = ((c == null) || (c.size() == 0)) ? null : (SuperVO[])(SuperVO[])c.toArray(new SuperVO[c.size()]);

    if ((accidvo != null) && (accidvo.length > 0)) {
      StringBuffer buff = new StringBuffer();
      for (int i = 0; i < accidvo.length; ++i) {
        AccidVO accid = (AccidVO)accidvo[i];
        buff.append("\n" + accid.getAccidcode() + "-" + accid.getAccidname() + "  ");
      }
      String error = NCLangRes4VoTransl.getNCLangRes().getStrByID("10081602", "UPP10081602-000064") + buff.toString();

      throw new BusinessException(error);
    }
  }

  private void checkPk_currtype(AccbankVO vo)
    throws BusinessException
  {
    String pk_currtype = vo.getPk_currtype();
    String pk_accid = vo.getPk_accid();
    if ((pk_accid == null) || (pk_accid.trim().length() <= 0)) return;
    AccidVO accid;
    try {
      accid = (AccidVO)getBaseDAO().retrieveByPK(AccidVO.class, pk_accid);
    } catch (Exception e) {
      Debug.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    if ((accid != null) && (!(pk_currtype.equals(accid.getPk_currtype()))))
      throw new BDException(NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000072"));
  }

  public AccbankVO insertAccbankVO(AccbankVO vo)
    throws BusinessException
  {
    AccbankVO ruturnVO = null;
    IPfBusinessLock bdLock = new PfBusinessLock();
    if ((vo.getCurrentcorp() == null) || (vo.getCurrentcorp().trim().length() == 0)) {
      throw new BDException("current corp is null");
    }
    try
    {
      HYBillVO billVo = new HYBillVO();
      billVo.setParentVO(vo);
      bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));

      if (isRepeat(vo, true)) {
        throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000000") }));
      }

      if (isSettleCenter(vo))
      {
        checkPk_currtype(vo);

        checkUnitAccordance(vo);

        checkTopAccount(vo, true);

        checkRemote(vo);
      }

      ruturnVO = insertDB(vo);
    }
    catch (BusinessException e)
    {
    }
    catch (Exception e)
    {
      throw new BDException(e.getMessage());
    }
    finally {
      bdLock.unLock();
    }
    return ruturnVO;
  }

  private AccbankVO insertDB(AccbankVO vo) throws Exception {
    String key = null;
    AccbankHeaderVO header = vo.getHeaderVO();
    AccbankFtsVO fts = vo.getFtsVO();

    header.setDr(new Integer(0));
    key = getBaseDAO().insertVO(header);
    header.setPrimaryKey(key);
    CacheProxy.fireDataInserted(header.getTableName(), key);

    if (fts != null) {
      fts.setPk_accbank(key);
      fts.setDr(new Integer(0));
      String ftsKey = getBaseDAO().insertVO(fts);
      fts.setPrimaryKey(ftsKey);
      CacheProxy.fireDataInserted(fts.getTableName(), ftsKey);
    }
    return new AccbankVO(header, fts);
  }

  public String[] insertAccbankVOs(AccbankVO[] accbankVOs)
    throws BusinessException
  {
    String[] keys = null;
    try {
      if ((accbankVOs != null) && (accbankVOs.length > 0)) {
        for (int i = 0; i < accbankVOs.length; ++i) {
          AccbankVO accbank = accbankVOs[i];
//          if (isRepeat(accbank, true)) {
//            throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000000") }));
//          }

          if (!(isSettleCenter(accbank)))
            continue;
          checkPk_currtype(accbank);

          checkUnitAccordance(accbank);

          checkTopAccount(accbank, true);

          checkRemote(accbank);
        }

        keys = insertVOsDB(accbankVOs);
      }
    } catch (Exception e) {
      handleException(e);
    }
    return keys;
  }

  private String[] insertVOsDB(AccbankVO[] vos) throws BusinessException {
    if ((vos == null) || (vos.length == 0)) return null;
    AccbankHeaderVO[] headers = new AccbankHeaderVO[vos.length];
    AccbankFtsVO[] ftss = new AccbankFtsVO[vos.length];

    for (int i = 0; i < vos.length; ++i) {
      AccbankVO accbank = vos[i];
      headers[i] = accbank.getHeaderVO();
      ftss[i] = accbank.getFtsVO();
      headers[i].setDr(new Integer(0));
    }

    String[] keys = null;
    try
    {
      keys = getBaseDAO().insertVOArray(headers);

      ArrayList validFtsVOs = new ArrayList();
      for (int i = 0; i < keys.length; ++i) {
        String key = keys[i];
        if (ftss[i] != null) {
          ftss[i].setPk_accbank(key);
          ftss[i].setDr(new Integer(0));
          validFtsVOs.add(ftss[i]);
        }

        CacheProxy.fireDataInserted("bd_accbank", key);
      }

      if ((validFtsVOs != null) && (validFtsVOs.size() > 0)) {
        AccbankFtsVO[] ftsvos = (AccbankFtsVO[])(AccbankFtsVO[])validFtsVOs.toArray(new AccbankFtsVO[validFtsVOs.size()]);

        String[] ftsKeys = getBaseDAO().insertVOArray(ftsvos);

        for (int i = 0; i < ftsvos.length; ++i)
          CacheProxy.fireDataInserted("bd_accbank", ftsKeys[i]);
      }
    }
    catch (Exception e)
    {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return keys;
  }

  private boolean isRepeat(AccbankVO vo, boolean isAdd) throws BusinessException {
    if ((vo.getBankacc() == null) || (vo.getBankacc().trim().length() == 0)) {
      throw new BusinessException(MultiLangTrans.getTransStr("MC3", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UC000-0004118") }));
    }
    vo.setBankacc(vo.getBankacc().trim());

    String pk_corp = vo.getPk_corp();
    String pk_createunit = vo.getPk_createunit();

    String where = "bd_accbank.bankacc = '" + vo.getBankacc() + "' ";

    if (!("0001".equals(pk_corp)))
    {
      where = where + "and (bd_accbank.pk_corp = '" + pk_corp + "' or bd_accbank.pk_corp = '0001') ";
    }

    if ((pk_createunit != null) && (pk_createunit.trim().length() > 0))
      where = where + "and bd_accbank_fts.pk_createunit = '" + pk_createunit + "' ";
    else {
      where = where + "and (bd_accbank_fts.pk_createunit is null or bd_accbank_fts.pk_createunit='')";
    }

    if (!(isAdd)) {
      where = where + "and bd_accbank.pk_accbank <> '" + vo.getPrimaryKey() + "' ";
    }

    String sql = "select count(bd_accbank.bankacc) from bd_accbank, bd_accbank_fts where " + where;
    Object o = getBaseDAO().executeQuery(sql, new ColumnProcessor());
    return (o != null);
  }

  public AccbankVO[] queryAccbankVOsByCondition(String pk_corp, String condition)
    throws BusinessException
  {
	return null;
//    AccbankVO[] vos = null;
//
//    String[] fields = (String[])(String[])field_column_map.keySet().toArray(new String[field_column_map.size()]);
//    String select = "select ";
//    for (int i = 0; i < fields.length; ++i) {
//      String column = (String)field_column_map.get(fields[i]);
//      select = select + column + ", ";
//    }
//    select = select.substring(0, select.length() - 2) + " ";
//
//    String from = "from bd_accbank left outer join bd_accbank_fts on (bd_accbank.pk_accbank = bd_accbank_fts.pk_accbank ";
//    if ((pk_corp != null) && (pk_corp.trim().length() > 0)) {
//      from = from + "and bd_accbank_fts.pk_corp = '" + pk_corp + "' ";
//    }
//    from = from + ") ";
//
//    String where = "where 1=1 ";
//    if ((pk_corp != null) && (pk_corp.trim().length() > 0)) {
//      where = where + "and (bd_accbank.pk_corp = '" + pk_corp + "' or bd_accbank.pk_corp = '0001') ";
//    }
//    if ((condition != null) && (condition.trim().length() > 0)) {
//      where = where + "and (" + condition + ") ";
//    }
//    String sql = select + from + where;
//
//    JdbcSession session = null;
//    try {
//      session = new JdbcSession();
//      ArrayList accbankList = (ArrayList)session.executeQuery(sql, new ResultSetProcessor(fields)
//      {
//        private static final long serialVersionUID = -139086209790287819L;
//
//        public Object handleResultSet(ResultSet rs) throws SQLException {
//          ArrayList al = new ArrayList();
//
//          while (rs.next()) {
//            AccbankVO vo = new AccbankVO();
//            for (int i = 0; i < this.val$fields.length; ++i) {
//              Object value = AccbankImpl.access$100(((Integer)AccbankImpl.field_type_map.get(this.val$fields[i])).intValue(), rs, i + 1);
//
//              vo.setAttributeValue(this.val$fields[i], value);
//            }
//
//            al.add(vo);
//          }
//          return al;
//        }
//      });
//      if (accbankList.size() > 0) {
//        vos = new AccbankVO[accbankList.size()];
//        accbankList.toArray(vos);
//      }
//    }
//    catch (Exception e)
//    {
//    }
//    finally {
//      if (session != null) {
//        session.closeAll();
//      }
//    }
//    return vos;
  }

  public AccbankVO[] queryAccbankVOsByVO(AccbankVO conditionVO, Boolean isAnd)
    throws BusinessException
  {
    String where = "1=1 ";
    String andOr = ((isAnd == null) || (isAnd.booleanValue())) ? " and " : " or ";
    String[] fields = (String[])(String[])field_column_map.keySet().toArray(new String[field_column_map.size()]);
    for (int i = 0; i < fields.length; ++i) {
      String field = fields[i];
      String fieldWhere = getFilterFromVO(conditionVO, field, ((Integer)field_type_map.get(field)).intValue());
      if (fieldWhere != null) {
        where = where + andOr + fieldWhere;
      }
    }
    return queryAccbankVOsByCondition(null, where);
  }

  private void checkTopAccount(AccbankVO vo, boolean isAdd)
    throws Exception
  {
    Integer IntGene = vo.getGenebranprop();
    int genebranprop = (IntGene == null) ? 2 : IntGene.intValue();
    if (genebranprop != 0) return;

    String strWhere = "bd_accbank.bankowner = '" + vo.getBankowner() + "' ";
    strWhere = strWhere + "and bd_accbank.pk_currtype = '" + vo.getPk_currtype() + "' ";
    strWhere = strWhere + "and bd_accbank.pk_corp = '" + vo.getCurrentcorp() + "' ";
    strWhere = strWhere + "and bd_accbank_fts.genebranprop = 0 ";
    if (!(isAdd)) {
      strWhere = strWhere + "and bd_accbank.pk_accbank <> '" + vo.getPrimaryKey() + "' ";
    }
    SuperVO[] vos = queryAccbankVOsByCondition(vo.getCurrentcorp(), strWhere);
    if ((vos != null) && (vos.length > 0))
      throw new BusinessException(MultiLangTrans.getTransStr("MC2", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000001"), "1" }));
  }

  private void checkRemote(AccbankVO vo)
    throws Exception
  {
    Integer IntGene = vo.getGenebranprop();
    int genebranprop = (IntGene == null) ? 2 : IntGene.intValue();
    String pk_createunit = vo.getPk_createunit();
    if ((pk_createunit == null) || (pk_createunit.trim().length() <= 0) || (genebranprop != 0))
      return;
    throw new Exception(MultiLangTrans.getTransStr("MP1", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000058") }));
  }

  public AccbankVO updateAccbankVO(AccbankVO vo)
    throws BusinessException
  {
    AccbankVO returnVO = null;
    IPfBusinessLock bdLock = new PfBusinessLock();
    if ((vo.getCurrentcorp() == null) || (vo.getCurrentcorp().trim().length() == 0))
      throw new BDException("current corp is null");
    try
    {
      HYBillVO billVo = new HYBillVO();
      billVo.setParentVO(vo);
      bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));

      if (isRepeat(vo, false)) {
        throw new BusinessException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000000") }));
      }

      checkAccidCurrtype(vo);

      checkNetBankFlag(vo);

      if (isSettleCenter(vo))
      {
        checkPk_currtype(vo);

        checkUnitAccordance(vo);

        checkTopAccount(vo, false);

        checkRemote(vo);

        checkCorrespondAccidNormal(vo, false);
      }

      BDDocSyncInform bdDocSync = new BDDocSyncInform("10081602", "pk_accbank", "bankname", "bankacc");
      bdDocSync.bdDocSyncUpdateBefore(null, vo);

      returnVO = updateBD(vo);

      bdDocSync.bdDocSyncUpdateAfter(null, returnVO);
    }
    catch (BusinessException e)
    {
    }
    catch (Exception e)
    {
      throw new BDException(e.getMessage());
    }
    finally {
      bdLock.unLock();
    }
    return returnVO;
  }

  private void checkAccidCurrtype(AccbankVO vo)
    throws BusinessException
  {
    IAccountDoc accountdoc = new AccountDocImpl();
    AccidVO[] accids = accountdoc.queryAccountDocsByCondition("pk_accbank = '" + vo.getPrimaryKey() + "' ");
    if ((accids != null) && (accids.length > 0)) {
      AccidVO accid = accids[0];
      if (!(vo.getPk_currtype().equals(accid.getPk_currtype())))
        throw new BDException(NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000073"));
    }
  }

  private boolean isExistFtsVO(AccbankFtsVO fts)
    throws BusinessException
  {
    if (fts == null) return false;
    String pk_accbank = fts.getPk_accbank();
    String pk_corp = fts.getPk_corp();
    String pk_accbank_fts = fts.getPk_accbank_fts();

    if ((pk_accbank == null) || (pk_corp == null)) {
      throw new IllegalArgumentException("pk_accbank is null or pk_corp is null");
    }
    String where = "pk_accbank = '" + pk_accbank + "' ";
    where = where + "and pk_corp = '" + pk_corp + "' ";
    if ((pk_accbank_fts != null) && (pk_accbank_fts.trim().length() > 0)) {
      where = where + "and pk_accbank_fts <> '" + pk_accbank_fts + "' ";
    }
    Collection c = null;
    try {
      c = getBaseDAO().retrieveByClause(AccbankFtsVO.class, where, new String[] { "pk_accbank" });
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return ((c != null) && (c.size() > 0));
  }

  private AccbankFtsVO updateFtsVO(AccbankFtsVO fts)
    throws BusinessException
  {
    if (isExistFtsVO(fts)) {
      deleteFtsVO(fts);
    }
    try
    {
      if ((fts.getPrimaryKey() == null) || (fts.getPrimaryKey().trim().length() == 0)) {
        fts.setDr(new Integer(0));
        String key = getBaseDAO().insertVO(fts);
        fts.setPrimaryKey(key);
        CacheProxy.fireDataInserted(fts.getTableName(), key);
      }
      else {
        updateVOAndTs(fts);
        CacheProxy.fireDataUpdated(fts.getTableName(), fts.getPrimaryKey());
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return fts;
  }

  private AccbankVO updateBD(AccbankVO vo)
    throws Exception
  {
    AccbankHeaderVO header = vo.getHeaderVO();
    AccbankFtsVO fts = vo.getFtsVO();

    updateVOAndTs(header);
    CacheProxy.fireDataUpdated(header.getTableName(), header.getPrimaryKey());

    fts = updateFtsVO(fts);
    return new AccbankVO(header, fts);
  }

  private void updateVOAndTs(SuperVO vo) throws Exception {
    vo.setAttributeValue("dr", Integer.valueOf(0));
    getBaseDAO().updateVO(vo);
    SuperVO newVO = (SuperVO)getBaseDAO().retrieveByPK(vo.getClass(), vo.getPrimaryKey(), new String[] { "ts" });
    vo.setAttributeValue("dr", new Integer(0));
    vo.setAttributeValue("ts", (UFDateTime)newVO.getAttributeValue("ts"));
  }

  public AccbankVO[] queryAccbankVOsByPks(String[] accbankPks)
    throws BusinessException
  {
    if ((accbankPks == null) || (accbankPks.length == 0)) return null;
    AccbankVO[] accbanks = null;

    InSqlBatchCaller caller = new InSqlBatchCaller(accbankPks);
    try {
      accbanks = (AccbankVO[])(AccbankVO[])caller.execute(new IInSqlBatchCallBack() {
        ArrayList al = new ArrayList();

        public Object doWithInSql(String inSql) throws BusinessException, SQLException { String where = "pk_accbank in ";
          try
          {
            Collection c = AccbankImpl.this.getBaseDAO().retrieveByClause(AccbankVO.class, where + inSql);
            SuperVO[] tempVOs = ((c == null) || (c.size() == 0)) ? null : (SuperVO[])(SuperVO[])c.toArray(new SuperVO[c.size()]);

            if ((tempVOs != null) && (tempVOs.length > 0))
              this.al.addAll(Arrays.asList(tempVOs));
          }
          catch (Exception e) {
            Logger.error(e.getMessage(), e);
            throw new BDException(e.getMessage());
          }
          if (this.al.size() > 0) {
            return ((AccbankVO[])(AccbankVO[])this.al.toArray(new AccbankVO[this.al.size()]));
          }

          return null;
        }
      });
    }
    catch (BusinessException e)
    {
      throw e;
    } catch (SQLException e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    return accbanks;
  }

  private void checkNetBankFlag(AccbankVO vo) throws BusinessException
  {
    DefaultIsNeedChkUpdateRef updateChecker = new DefaultIsNeedChkUpdateRef(new String[] { "netbankflag" });
    if (updateChecker.isNeedCheckUpdateReference(vo, (AccbankVO)getBaseDAO().retrieveByPK(AccbankVO.class, vo.getPrimaryKey()))) {
      Collection col = getBaseDAO().retrieveByClause(AccidVO.class, "pk_accbank='" + vo.getPrimaryKey() + "'");
      if (col.size() > 0)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081602", "UPP10081602-000076"));
    }
  }
}