package nc.impl.uap.bd.corp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.pub.CheckSealCanChg;
import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pflock.IPfBusinessLock;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.trade.lock.BDConsistenceCheck;
import nc.bs.trade.lock.BDLockData;
import nc.bs.uap.bd.BDException;
import nc.bs.uap.bd.BDRuntimeException;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.corp.ICorp;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.itf.uap.bd.corp.ISettleCenterQueryForCorp;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.uap.bd.innercode.IInnerCodeService;
import nc.itf.uap.license.ILicenseService;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.itf.uap.sfapp.IAccountService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.ConditionVO;
import nc.vo.bd.CorpVO;
import nc.vo.bd.CorphistoryVO;
import nc.vo.bd.InnerCorpCode;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.util.tree.IOPCreator;
import nc.vo.util.tree.MethodGroup;
import nc.vo.util.tree.TreeDetail;
import nc.vo.util.tree.TreeOperationException;
import nc.vo.util.tree.TreeUtil;
import nc.vo.util.tree.XTreeModel;
import nc.vo.util.tree.XTreeNode;

public class CorpImpl
  implements ICorpQry, ICorp
{
  private static final String CORPKIND_BALANCE = "0001KHH00000000A0002";
  private BaseDAO m_baseDAO;

  private void addInnerCode(CorpVO vo)
    throws BusinessException
  {
    try
    {
      InnerCorpCode innercode = new InnerCorpCode();
      IInnerCodeService iInnerCodeService = (IInnerCodeService)NCLocator.getInstance().lookup(IInnerCodeService.class.getName());

      String[] innercodes = null;

      innercodes = iInnerCodeService.getCodes(innercode, vo.getFathercorp(), "bd_corp", 1);

      vo.setInnercode(innercodes[0]);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
  }

  public void cancelCorp(CorpVO corp)
    throws BusinessException
  {
    IDeptdocQry deptQuery = null;
    deptQuery = (IDeptdocQry)NCLocator.getInstance().lookup(IDeptdocQry.class.getName());

    DeptdocVO filter = new DeptdocVO();
    filter.setPk_corp(corp.getPk_corp());
    filter.setCanceled(new UFBoolean(false));
    DeptdocVO[] voArray = deptQuery.queryByVO(filter, new Boolean(true));

    if ((voArray != null) || (voArray.length != 0)) {
      throw new BusinessException(MultiLangTrans.getTransStr("MO2", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UPP100406-000001") }));
    }

    update(corp);
  }

  public boolean checkIsOnly(CorpVO condCorpVO, boolean isInsert)
    throws BusinessException
  {
    String strConditionNames = "";
    String strAndOr = " or  ";

    if (condCorpVO.getUnitcode() != null) {
      strConditionNames = strConditionNames + strAndOr + "unitcode='" + condCorpVO.getUnitcode() + "' ";
    }

    if (condCorpVO.getUnitname() != null) {
      strConditionNames = strConditionNames + strAndOr + "unitname='" + condCorpVO.getUnitname() + "' ";
    }

    if (strConditionNames.trim().length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);

      if (!isInsert)
        strConditionNames = "(" + strConditionNames + ")" + " and pk_corp!='" + condCorpVO.getPrimaryKey() + "'";
    }
    else {
      strConditionNames = "pk_corp!='" + condCorpVO.getPrimaryKey() + "'";
    }

    IUAPQueryBS iquery = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

    Collection col = iquery.retrieveByClause(CorpVO.class, strConditionNames);

    return (col == null) || (col.size() == 0);
  }

  private void checkBeforeDelete(CorpVO vo)
    throws BusinessException
  {
    CorpVO[] vos = getChildCorp(vo.getPk_corp());
    if ((vos != null) && (vos.length > 0)) {
      throw new BDException(MultiLangTrans.getTransStr("MO2", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC001-0000039") }));
    }

    if (isHasAccount(vo.getPk_corp())) {
      throw new BDException(NCLangResOnserver.getInstance().getStrByID("100406", "UPP100406-000041") + "," + MultiLangTrans.getTransStr("MP1", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC001-0000039") }));
    }

    checkSettelCenterAndUnitIssues(vo);
  }

  private void checkSettelCenterAndUnitIssues(CorpVO vo) throws BusinessException
  {
    ISettleCenterQueryForCorp iISettleCenterQueryForCorp = null;
    try {
      iISettleCenterQueryForCorp = (ISettleCenterQueryForCorp)NCLocator.getInstance().lookup(ISettleCenterQueryForCorp.class.getName());
    }
    catch (ComponentException e)
    {
      Logger.error(e.getMessage(), e);
      Logger.error("警告:未安装UAPBD模块,找不到ISettleCenterQueryForCorp");
      return;
    }
    boolean corpIsSetterCenter = iISettleCenterQueryForCorp.corpIsSettlerCenter(vo.getPk_corp());
    if (corpIsSetterCenter)
    {
      throw new BDException(MultiLangTrans.getTransStr("MC8", new String[] { NCLangResOnserver.getInstance().getStrByID("common", "UC000-0000404"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003234") }) + "," + MultiLangTrans.getTransStr("MP1", new String[] { NCLangResOnserver.getInstance().getStrByID("common", "UC001-0000039") }));
    }

    boolean corpIsSetterUnit = iISettleCenterQueryForCorp.corpIsSettlerUnit(vo.getPk_corp());
    if (corpIsSetterUnit)
    {
      throw new BDException(MultiLangTrans.getTransStr("MC8", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000404"), NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0003242") }) + "," + MultiLangTrans.getTransStr("MP1", new String[] { NCLangResOnserver.getInstance().getStrByID("common", "UC001-0000039") }));
    }
  }

  public int delete(CorpVO vo)
    throws BusinessException
  {
    boolean isLocked = false;
    try
    {
      isLocked = PKLock.getInstance().acquireLock(vo.getPrimaryKey(), getUserID(), null);

      if (!isLocked) {
        throw new BDException(BDMsg.MSG_LOCKED());
      }

      checkBeforeDelete(vo);

      getBaseDAO().deleteVO(vo);
      new BDDelLog().delPKs("bd_corp", new String[] { vo.getPrimaryKey() });
    }
    finally
    {
      if (isLocked) {
        PKLock.getInstance().releaseLock(vo.getPrimaryKey(), getUserID(), null);
      }
    }
    return 1;
  }

  public CorpVO[] findByPrimaryKey(String[] keys)
    throws BusinessException
  {
    CorpVO[] corps = null;
    CorpVO[] tempvos = null;
    Hashtable h = new Hashtable();
    String strWhere = "";

    if ((keys == null) || (keys.length == 0))
      return null;
    for (int i = 0; i < keys.length; i++) {
      if (i != 0)
        strWhere = strWhere + ",";
      strWhere = strWhere + "'" + keys[i] + "'";
    }
    if (strWhere.trim().length() > 0)
      strWhere = "(" + strWhere + ")";
    else {
      return null;
    }

    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, strWhere);
    if ((c != null) && (c.size() > 0)) {
      tempvos = new CorpVO[c.size()];
      c.toArray(tempvos);
    }

    for (int i = 0; i < (tempvos == null ? 0 : tempvos.length); i++) {
      h.put(tempvos[i].getPrimaryKey(), tempvos[i]);
    }
    for (int i = 0; i < keys.length; i++) {
      corps[i] = ((CorpVO)h.get(keys[i]));
    }

    corps = getTopSettleCenter(corps);

    return corps;
  }

  public CorpVO findCorpVOByPK(String key)
    throws BusinessException
  {
    if ("0001".equals(key)) {
      CorpVO groupCorp = new CorpVO();
      groupCorp.setPk_corp("0001");
      return groupCorp;
    }

    CorpVO corp = (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, key);

    getTopSettleCenter(corp);

    return corp;
  }

  public CorpVO findCorpVOByPK(String key, String dbName)
    throws BusinessException
  {
    CorpVO corp = (CorpVO)new BaseDAO(dbName).retrieveByPK(CorpVO.class, key);

    return corp;
  }

  public CorpVO[] findCorpVOByPK(String[] keys, String t, String t1)
    throws BusinessException
  {
    return findByPrimaryKey(keys);
  }

  public int getAccountedCorpNumber()
    throws BusinessException
  {
    IAccountService iIAccountService = null;

    iIAccountService = (IAccountService)NCLocator.getInstance().lookup(IAccountService.class.getName());
    ConfigParameter cp = iIAccountService.getConfigParameter();

    Account[] accounts = cp.getAryAccounts();
    if (accounts == null) {
      return 0;
    }

    HashSet hs = new HashSet();
    for (int i = 0; i < accounts.length; i++) {
      String dsName = accounts[i].getDataSourceName();
      if ((dsName == null) || (dsName.trim().length() == 0))
        continue;
      Collection c = getBaseDAO().retrieveByClause(CorpVO.class, "1=1", "innercode");

      if ((c == null) || (c.size() == 0))
        continue;
      CorpVO[] corps = (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);

      if (corps != null) {
        for (int j = 0; j < corps.length; j++) {
          boolean bHashAccounted = corps[j].getIshasaccount() == null ? false : corps[j].getIshasaccount().booleanValue();

          if (bHashAccounted)
            hs.add(corps[j].getUnitcode());
        }
      }
    }
    return hs.size();
  }

  public String[] getAccountedCorpPKs()
    throws BusinessException
  {
    String where = "ishasaccount='Y' ";
    String order = "pk_corp ";
    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, where, order, new String[] { "pk_corp" });

    String[] keys = null;
    if ((c != null) && (c.size() > 0)) {
      List list = VOUtil.extractFieldValues((CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]), "pk_corp", null);

      if ((list != null) && (list.size() > 0)) {
        keys = new String[list.size()];
        list.toArray(keys);
      }
    }
    return keys;
  }

  public CorpVO[] getAllChildCorp(String pkcorp)
    throws BusinessException
  {
    return getAllChildCorp(pkcorp, new UFBoolean(false));
  }

  public CorpVO[] getAllChildCorp(String pkcorp, UFBoolean isAccounted)
    throws BusinessException
  {
    CorpVO[] children = null;
    if ("0001".equals(pkcorp)) {
      CorpVO[] vos = null;
      if ((isAccounted != null) && (isAccounted.booleanValue()))
        vos = queryCorpVOByWhereSQL("ishasaccount = 'Y'");
      else {
        vos = queryAllCorpVO(null);
      }
      if (vos != null) {
        VOUtil.ascSort(vos, new String[] { "innercode" });
      }
      return vos;
    }

    IInnerCodeService codeManager = (IInnerCodeService)NCLocator.getInstance().lookup(IInnerCodeService.class.getName());

    String where = " 1=1 ";
    if ((isAccounted != null) && (isAccounted.booleanValue())) {
      where = where + " and ishasaccount = 'Y' ";
    }
    children = (CorpVO[])(CorpVO[])codeManager.findChildByFatherID(new InnerCorpCode(), pkcorp, where);

    return children;
  }

  public int getAvailableLicenses()
    throws BusinessException
  {
    int accountedCorps = getAccountedCorpNumber();
    Logger.info("已经建立帐套公司：：" + accountedCorps);
    ILicenseService iILicenseService = (ILicenseService)NCLocator.getInstance().lookup(ILicenseService.class.getName());

    int licenses = iILicenseService.getProductLicense("corp");
    Logger.info("授权建立帐套公司：：" + licenses);
    return licenses - accountedCorps;
  }

  public CorpVO[] getChildCorp(String pkcorp)
    throws BusinessException
  {
    CorpVO[] corps = null;
    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, "fathercorp = '" + pkcorp + "'", "pk_corp");

    if ((c != null) && (c.size() > 0)) {
      corps = new CorpVO[c.size()];
      c.toArray(corps);
    }

    corps = getTopSettleCenter(corps);

    return corps;
  }

  public String[] getCorpNames(String[] pkcorp)
    throws BusinessException
  {
    int num = pkcorp == null ? 0 : pkcorp.length;
    if (num == 0) {
      return null;
    }
    String[] names = new String[num];
    CorpVO vo = null;

    for (int i = 0; i < num; i++) {
      if ((pkcorp[i] == null) || (pkcorp[i].trim().length() == 0)) {
        names[i] = "";
      }
      else if (pkcorp[i].equals("0001")) {
        names[i] = NCLangResOnserver.getInstance().getStrByID("100406", "UPP100406-000000");
      }
      else {
        try
        {
          vo = (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, pkcorp[i], new String[] { "unitname" });

          names[i] = (vo == null ? null : vo.getUnitname());
        } catch (Exception e) {
          throw new BDException("findByPrimaryKey(String key) Exception: " + e.getMessage());
        }

      }

    }

    return names;
  }

  public CorpVO[] getFABCorps(String pkCorp)
    throws BusinessException
  {
    CorpVO[] corps = null;
    CorpVO vo = null;
    String pkFatherCorp = null;
    ArrayList al = new ArrayList();

    Object o = getBaseDAO().retrieveByPK(CorpVO.class, pkCorp, new String[] { "fathercorp" });

    pkFatherCorp = o == null ? null : ((CorpVO)o).getFathercorp();

    if (pkFatherCorp != null) {
      al.addAll(getBaseDAO().retrieveByClause(CorpVO.class, "fathercorp = '" + pkFatherCorp + "'", new String[] { "pk_corp", "unitcode", "unitname", "fathercorp" }));
    }
    else
    {
      al.addAll(getBaseDAO().retrieveByClause(CorpVO.class, "fathercorp is null ", new String[] { "pk_corp", "unitcode", "unitname", "fathercorp" }));
    }

    while ((pkFatherCorp != null) && (pkFatherCorp.length() > 0)) {
      vo = (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, pkFatherCorp, new String[] { "pk_corp", "unitcode", "unitname", "fathercorp" });

      al.add(vo);
      pkFatherCorp = vo.getFathercorp();
    }

    if (al.size() > 0) {
      corps = new CorpVO[al.size()];
      al.toArray(corps);
    }

    return corps;
  }

  public String getNextPkCorp()
    throws BusinessException
  {
    String sql = "select max(pk_corp) from bd_corp ";
    String maxPk = null;

    IUAPQueryBS iquery = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

    ColumnProcessor p = new ColumnProcessor();
    Object o = iquery.executeQuery(sql, p);
    if (o != null)
      maxPk = o.toString();
    if (maxPk == null)
      maxPk = "1000";
    String newPk = String.valueOf(Integer.parseInt(maxPk) + 1);
    while (newPk.length() < 4) {
      newPk = "0" + newPk;
    }
    return newPk;
  }

  public String getNextPkCorp(String dataSourceName)
    throws BusinessException
  {
    return getNextPkCorp();
  }

  public CorpVO[] getSelfAndAllChildCorp(String pkcorp)
    throws BusinessException
  {
    return getSelfAndAllChildCorp(pkcorp, new UFBoolean(false));
  }

  public CorpVO[] getSelfAndAllChildCorp(String pkcorp, UFBoolean isAccounted)
    throws BusinessException
  {
    ArrayList al = new ArrayList();
    CorpVO[] vos = null;

    CorpVO vo = findCorpVOByPK(pkcorp);
    if (vo != null) {
      al.add(vo);
    }
    CorpVO[] cvos = getAllChildCorp(pkcorp, isAccounted);
    if ((cvos != null) && (cvos.length > 0)) {
      for (int i = 0; i < cvos.length; i++) {
        al.add(cvos[i]);
      }
    }
    if (al.size() > 0) {
      vos = new CorpVO[al.size()];
      al.toArray(vos);
    }
    return vos;
  }

  private BaseDAO getBaseDAO() throws BusinessException {
    if (this.m_baseDAO == null) {
      this.m_baseDAO = new BaseDAO();
    }
    return this.m_baseDAO;
  }

  private void getTopSettleCenter(CorpVO vo)
    throws BusinessException
  {
    String pk_settlecenter = null;

    if (vo == null)
      return;
    if ((vo.getpkCorpkind() == null) || (vo.getpkCorpkind().length() == 0))
      return;
    if (!vo.getpkCorpkind().equals("0001KHH00000000A0002")) {
      return;
    }
    CorpVO[] vos = queryByWhereSQL1("pk_corpkind = '0001KHH00000000A0002'");

    Hashtable ht = new Hashtable();
    if ((vos == null) || (vos.length == 0)) {
      vo.setPkTopSettleCenter(vo.getPk_corp());
      return;
    }
    for (int i = 0; i < vos.length; i++)
      ht.put(vos[i].getPk_corp(), vos[i]);
    pk_settlecenter = vo.getPk_corp();
    String fkey = vo.getFathercorp();
    while ((fkey != null) && (fkey.length() > 0)) {
      pk_settlecenter = ht.get(fkey) == null ? pk_settlecenter : fkey;
      fkey = ht.get(fkey) == null ? null : ((CorpVO)ht.get(fkey)).getFathercorp();
    }

    vo.setPkTopSettleCenter(pk_settlecenter);
  }

  private CorpVO[] getTopSettleCenter(CorpVO[] corps)
    throws BusinessException
  {
    if ((corps == null) || (corps.length == 0)) {
      return corps;
    }

    CorpVO[] vos = queryByWhereSQL1("pk_corpkind = '0001KHH00000000A0002'");

    if ((vos == null) || (vos.length == 0)) {
      return corps;
    }
    Hashtable ht = new Hashtable();
    for (int i = 0; i < vos.length; i++) {
      ht.put(vos[i].getPk_corp(), vos[i]);
    }
    String pk_settlecenter = null;
    String fkey = null;
    for (int i = 0; i < corps.length; i++)
    {
      if ((corps[i].getpkCorpkind() == null) || (corps[i].getpkCorpkind().length() == 0)) {
        continue;
      }
      if (!corps[i].getpkCorpkind().equals("0001KHH00000000A0002")) {
        continue;
      }
      pk_settlecenter = corps[i].getPk_corp();
      fkey = corps[i].getFathercorp();
      while ((fkey != null) && (fkey.length() > 0)) {
        pk_settlecenter = ht.get(fkey) == null ? pk_settlecenter : fkey;
        fkey = ht.get(fkey) == null ? null : ((CorpVO)ht.get(fkey)).getFathercorp();
      }

      corps[i].setPkTopSettleCenter(pk_settlecenter);
    }

    return corps;
  }

  public String insert(CorpVO corp)
    throws BusinessException
  {
    new CheckSealCanChg(corp, "pk_corp", "fathercorp", "isseal").checkCanChgSeal();
    addInnerCode(corp);
    corp.setPrimaryKey(getNextPkCorp());
    if (!checkIsOnly(corp, true)) {
      throw new BDException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000417") + "," + NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000414") }));
    }

    String key = getBaseDAO().insertVOWithPK(corp);
    CacheProxy.fireDataInserted("bd_corp", key);
    return key;
  }

  private String getUserID() throws BusinessException
  {
    return InvocationInfoProxy.getInstance().getUserCode();
  }

  private void checkFatherExist(String fatherPk)
    throws BusinessException
  {
    if (fatherPk != null) {
      CorpVO father = findCorpVOByPK(fatherPk);
      if (father == null)
        throw new BDException(MultiLangTrans.getTransStr("MC7", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UPT100406-000004"), null }));
    }
  }

  public String insertCorp(CorpVO corp, String[] accountMsgInfo)
    throws BusinessException
  {
    boolean isCodeLocked = false;
    boolean isNameLocked = false;
    try
    {
      isCodeLocked = PKLock.getInstance().acquireLock(corp.getUnitcode() + "unitcode", getUserID(), null);

      isNameLocked = PKLock.getInstance().acquireLock(corp.getUnitname() + "unitname", getUserID(), null);

      boolean isOnly = checkIsOnly(corp, true);
      if ((!isCodeLocked) || (!isNameLocked) || (!isOnly)) {
        throw new BDException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000417") + "," + NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000414") }));
      }

      checkFatherExist(corp.getFathercorp());

      String corpid = insert(corp);
      CacheProxy.fireDataInserted("bd_corp", corpid);

      String str1 = corp.getPrimaryKey();
      return str1;
    }
    finally
    {
      if (isCodeLocked) {
        PKLock.getInstance().releaseLock(corp.getUnitcode() + "unitcode", getUserID(), null);
      }
      if (isNameLocked)
        PKLock.getInstance().releaseLock(corp.getUnitname() + "unitname", getUserID(), null);
    }
  }

  public String insertDefaultCorp(String dsName, CorpVO corp, String[] accountMsgInfo)
    throws BusinessException
  {
    throw new RuntimeException("should not run into this");
  }

  public String insertHistory(CorpVO corp)
    throws BusinessException
  {
    String rs = null;

    UFDate currtime = new UFDate(System.currentTimeMillis());

    CorphistoryVO oldvo = null;
    String strWhere = "pk_corp = '" + corp.getPrimaryKey() + "'";
    String orderby = "begindate desc,ts desc";

    Collection c = getBaseDAO().retrieveByClause(CorphistoryVO.class, strWhere, orderby);

    CorphistoryVO[] oldvos = (c == null) || (c.size() == 0) ? null : (CorphistoryVO[])(CorphistoryVO[])c.toArray(new CorphistoryVO[c.size()]);

    if ((oldvos != null) && (oldvos.length > 0)) {
      oldvo = oldvos[0];
      oldvo.setEnddate(currtime);
      getBaseDAO().updateVO(oldvo);
    }

    CorphistoryVO history = new CorphistoryVO(corp);
    history.setBegindate(currtime);
    rs = getBaseDAO().insertVO(history);

    return rs;
  }

  public boolean isHasAccount(String pkcorp)
    throws BusinessException
  {
    CorpVO corp = (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, pkcorp, new String[] { "ishasaccount" });

    UFBoolean isHasAcc = corp == null ? null : corp.getIshasaccount();
    if (isHasAcc != null) {
      return isHasAcc.booleanValue();
    }

    return false;
  }

  public CorpVO[] multiCorpSelect(ConditionVO condition)
    throws BusinessException
  {
    int bl = condition.getAttributeValue("BeginLevel") == null ? -1 : ((Integer)condition.getAttributeValue("BeginLevel")).intValue();

    int el = condition.getAttributeValue("EndLevel") == null ? -1 : ((Integer)condition.getAttributeValue("EndLevel")).intValue();

    String where = condition.getAttributeValue("where") == null ? null : (String)condition.getAttributeValue("where");

    String user = condition.getAttributeValue("useid") == null ? null : condition.getAttributeValue("useid").toString();

    String node = condition.getAttributeValue("node") == null ? null : condition.getAttributeValue("node").toString();

    CorpVO[] vos = null;

    TreeDetail detail = null;

    detail = new TreeDetail();

    detail.setPolicy(1);

    MethodGroup mg = new MethodGroup();
    try
    {
      mg.setKeyField(CorpVO.class.getMethod("getPk_corp", null));
      mg.setAssKeyField(CorpVO.class.getMethod("getFathercorp", null));

      mg.setNameField(CorpVO.class.getMethod("getUnitname", null));
      mg.setSortCodeFiled(CorpVO.class.getMethod("getUnitcode", null));
    }
    catch (SecurityException e)
    {
      Logger.error(e.getMessage(), e);
      throw new BDRuntimeException("Fatal error!");
    } catch (NoSuchMethodException e) {
      Logger.error(e.getMessage(), e);
      throw new BDRuntimeException("Fatal error!");
    }

    mg.setHowDisplay(new boolean[] { false, true, true });
    mg.setAimClass(CorpVO.class);

    detail.setMg(new MethodGroup[] { mg });

    XTreeModel model = null;
    try {
      model = IOPCreator.createTreeModel(detail);
    } catch (TreeOperationException e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }

    CorpVO[] total = queryAllInfo(null);

    if ((total == null) || (total.length == 0))
      return null;
    try
    {
      model.createTree(total);
    } catch (TreeOperationException e) {
      Logger.error(e.getMessage(), e);
      throw new BDException(e.getMessage());
    }
    int level = TreeUtil.getSubTreeLevel((XTreeNode)model.getRoot());
    bl = bl == -1 ? 0 : bl;
    el = el > level ? level : el == -1 ? level : el;

    Object[] tadata = TreeUtil.getSubTreeValue((XTreeNode)model.getRoot(), bl, el);

    if (tadata != null) {
      total = new CorpVO[tadata.length];
      System.arraycopy(tadata, 0, total, 0, tadata.length);
    } else {
      total = null;
      return null;
    }

    if (total.length == 0) {
      return null;
    }
    Hashtable hash = new Hashtable();
    Hashtable temp = new Hashtable();
    int size = total.length;
    for (int i = 0; i < size; i++) {
      hash.put(total[i].getPk_corp(), total[i]);
    }

    IFuncPower iIFuncPower = (IFuncPower)NCLocator.getInstance().lookup(IFuncPower.class.getName());

    String[] pk_corps = iIFuncPower.queryCorpByUserAndFunc(user, node);

    if ((pk_corps == null) || (pk_corps.length == 0)) {
      hash.clear();
      return null;
    }

    int psize = pk_corps.length;
    for (int i = 0; i < psize; i++) {
      if (hash.containsKey(pk_corps[i])) {
        temp.put(pk_corps[i], hash.get(pk_corps[i]));
      }
    }
    hash.clear();

    CorpVO[] cvos = queryInfoByWhereSQL(where);
    int csize = cvos == null ? 0 : cvos.length;
    if (csize == 0) {
      hash.clear();
      temp.clear();
      return null;
    }
    Vector v = new Vector();
    for (int i = 0; i < csize; i++) {
      if (temp.containsKey(cvos[i].getPk_corp())) {
        v.addElement(temp.get(cvos[i].getPk_corp()));
      }
    }
    temp.clear();
    if (v.size() != 0) {
      vos = new CorpVO[v.size()];
      v.copyInto(vos);
    }

    return vos;
  }

  public String[] queryAllCenterPKs()
    throws BusinessException
  {
    String[] corpPKs = null;

    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, "pk_corpkind = '0001KHH00000000A0002'", new String[] { "pk_corpkind" });

    CorpVO[] corps = (c == null) || (c.size() == 0) ? null : (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);

    if ((corps != null) && (corps.length > 0)) {
      corpPKs = new String[corps.length];
      for (int i = 0; i < corps.length; i++) {
        corpPKs[i] = ((corps[i] == null) || (corps[i].getPrimaryKey() == null) ? null : corps[i].getPrimaryKey());
      }

    }

    return corpPKs;
  }

  public CorpVO[] queryAllCorpVO(String unitCode)
    throws BusinessException
  {
    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, "1=1", "innercode");

    CorpVO[] corps = (c == null) || (c.size() == 0) ? null : (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);

    corps = getTopSettleCenter(corps);
    return corps;
  }

  public CorpVO[] queryAllInfo(String unitCode)
    throws BusinessException
  {
    return queryAllCorpVO(unitCode);
  }

  private CorpVO[] queryByWhereSQL1(String whereSQL)
    throws BusinessException
  {
    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, whereSQL);
    return (c == null) || (c.size() == 0) ? null : (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);
  }

  public CorpVO[] queryCorpVOByVO(CorpVO condCorpVO, Boolean isAnd)
    throws BusinessException
  {
    Collection c = getBaseDAO().retrieve(condCorpVO, isAnd == null ? true : isAnd.booleanValue());

    CorpVO[] corps = (c == null) || (c.size() == 0) ? null : (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);

    corps = getTopSettleCenter(corps);

    return corps;
  }

  public CorpVO[] queryCorpVOByWhereSQL(String whereSQL)
    throws BusinessException
  {
    Collection c = getBaseDAO().retrieveByClause(CorpVO.class, whereSQL);
    CorpVO[] corps = (c == null) || (c.size() == 0) ? null : (CorpVO[])(CorpVO[])c.toArray(new CorpVO[c.size()]);

    corps = getTopSettleCenter(corps);

    return corps;
  }

  public CorpVO[] queryInfoByWhereSQL(String whereSQL)
    throws BusinessException
  {
    IUAPQueryBS iquery = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

    Collection col = iquery.retrieveByClause(CorpVO.class, whereSQL, new String[] { "pk_corp", "unitcode", "unitname", "fathercorp" });

    if ((col == null) || (col.size() == 0)) {
      return null;
    }
    CorpVO[] vos = (CorpVO[])(CorpVO[])col.toArray(new CorpVO[0]);
    VOUtil.ascSort(vos, new String[] { "pk_corp" });
    return vos;
  }

  private boolean isSeal(CorpVO vo, CorpVO oldVO)
  {
    boolean isSeal = false;
    if (vo.getIsseal().booleanValue()) {
      if (oldVO.getIsseal().booleanValue())
        vo.setSealeddate(oldVO.getSealeddate());
      else {
        isSeal = true;
      }
    }
    return isSeal;
  }

  private void beforeSeal(boolean isSeal, CorpVO corp, BDOperateServ bdOS) throws BusinessException
  {
    if (isSeal) {
      corp.setSealeddate(new UFDate(System.currentTimeMillis()));
      bdOS.beforeOperate("100406", 1, corp.getPrimaryKey(), null, null);
    }
    else
    {
      corp.setSealeddate(null);
    }
  }

  private void afterSeal(boolean isSeal, CorpVO corp, BDOperateServ bdOS) throws BusinessException
  {
    if (isSeal)
      bdOS.afterOperate("100406", 1, corp.getPrimaryKey(), null, null);
  }

  public String update(CorpVO corp)
    throws BusinessException
  {
    new CheckSealCanChg(corp, "pk_corp", "fathercorp", "isseal").checkCanChgSeal();
    IPfBusinessLock bdLock = new PfBusinessLock();
    try
    {
      HYBillVO billVo = new HYBillVO();
      billVo.setParentVO(corp);
      if (corp.getTs() != null) {
        bdLock.lock(new BDLockData(billVo), new BDConsistenceCheck(billVo));
      }

      if (!checkIsOnly(corp, false)) {
        throw new BDException(MultiLangTrans.getTransStr("MC1", new String[] { NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000417") + "," + NCLangResOnserver.getInstance().getStrByID("100406", "UC000-0000414") }));
      }

      CorpVO oldVO = findCorpVOByPK(corp.getPrimaryKey());
      BDOperateServ bdOS = new BDOperateServ();
      boolean isSeal = isSeal(corp, oldVO);
      beforeSeal(isSeal, corp, bdOS);

      updateInnerCode(corp);
      getBaseDAO().updateVO(corp);
      CacheProxy.fireDataUpdated("bd_corp", corp.getPrimaryKey());

      afterSeal(isSeal, corp, bdOS);

      if (corp.isBackup())
        insertHistory(corp);
      String str = "ok";
      return str;
    }
    finally
    {
      bdLock.unLock();
    }
  }

  public void updateAccountFlag(String pkCorp, boolean bHasAccount)
    throws BusinessException
  {
    CorpVO corp = new CorpVO();
    corp.setPk_corp(pkCorp);
    corp.setIshasaccount(new UFBoolean(bHasAccount));

    getBaseDAO().updateVO(corp, new String[] { "ishasaccount" });
  }

  private void updateInnerCode(CorpVO vo)
    throws BusinessException
  {
    InnerCorpCode innercode = new InnerCorpCode();
    IInnerCodeService iInnerCodeService = (IInnerCodeService)NCLocator.getInstance().lookup(IInnerCodeService.class.getName());

    String[] innercodes = null;

    CorpVO oldvo = (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, vo.getPrimaryKey());

    String oldFather = (oldvo.getFathercorp() == null) || (oldvo.getFathercorp().trim().length() == 0) ? null : oldvo.getFathercorp().trim();

    String newFather = (vo.getFathercorp() == null) || (vo.getFathercorp().trim().length() == 0) ? null : vo.getFathercorp().trim();

    vo.setInnercode(oldvo.getInnercode());
    vo.setMaxinnercode(oldvo.getMaxinnercode());

    if (((oldFather != null) && (oldFather.equalsIgnoreCase(newFather))) || ((oldFather == null) && (newFather == null)))
    {
      return;
    }
    String oldInnercode = oldvo.getInnercode();
    innercodes = iInnerCodeService.getCodes(innercode, vo.getFathercorp(), "bd_corp", 1);

    vo.setInnercode(innercodes[0]);
    if ((oldInnercode != null) && (!oldInnercode.equalsIgnoreCase(innercodes[0])))
    {
      iInnerCodeService.updateAllChildrenCodes(innercode, oldInnercode, innercodes[0], "bd_corp");
    }
  }

  public CorpVO insertCorpReturnSelf(CorpVO corp, String[] accountMsgInfo) throws BusinessException
  {
    String pk = insert(corp);

    return (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, pk);
  }

  public CorpVO updateReturnSelf(CorpVO corp) throws BusinessException {
    update(corp);
    return (CorpVO)getBaseDAO().retrieveByPK(CorpVO.class, corp.getPrimaryKey());
  }
}