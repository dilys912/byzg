package nc.ui.pf.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import nc.bs.dbcache.intf.ICacheVersionBS;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.currtype.ICurrtype;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFVoMapping;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.ui.cache.UiCacheManager;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.billtobill.BilltobillreferBO_Client;
import nc.ui.pub.billtype.BilltypeBO_Client;
import nc.ui.pub.msg.MessagePanelOptions;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.cache.CacheKey;
import nc.vo.cache.ICacheManager;
import nc.vo.cache.exception.CacheException;
import nc.vo.cache.ext.CacheToMapAdapter;
import nc.vo.cache.ext.ElementVersionSensitiveMap;
import nc.vo.cache.ext.ICacheVersionMonitor;
import nc.vo.cache.ext.ObjectCacheVersionMonitor;
import nc.vo.cache.ext.VersionMonitorFactory;
import nc.vo.cache.policy.CachePolicyFactory;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pf.pub.BasedocVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtobill.BilltobillreferVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

public class PfUIDataCache
{
  private static final String CACHE_REGION = "platform";
  public static final String BUSIBYCORPANDBILL = "BUSIBYCORPANDBILL";
  private static final String SOURCEBYCORPANDBILLANDBUSI = "SOURCEBYCORPANDBILLANDBUSI";
  private static final String BUTTONBYBILLANDGRP = "BUTTONBYBILLANDGRP";
  public static final String KHHINFOHAS = "KHHINFOHAS";
  public static final String KHHUIBILLVO = "KHHUIBILLVO";
  private static final String KHHBILLPKHAS = "KHHBILLPKHAS";
  public static final String KHHBILLTYPEHAS = "KHHBILLTYPEHAS";
  private static final String KHHBDINFO = "KHHBDINFO";
  private static final String KHHCURRENCY = "KHHCURRENCY";
  private static final String MSGPANEL_OPTIONS = "MSGPANELOPTIONS";
  private static Hashtable dsName_instance_map = new Hashtable();

  private static String STR_BILLSTYLETOTYPE = "BILLSTYLETOTYPE";

  public static String STR_BILLTYPE2INFO = "BILLTYPE2INFO";

  private static String getCurrentDs()
  {
    if (RuntimeEnv.getInstance().isRunningInServer())
    {
      String dsName = InvocationInfoProxy.getInstance().getUserDataSource();
      return dsName;
    }

    Account account = ClientEnvironment.getInstance().getConfigAccount();
    String dsName = account.getDataSourceName();
    return dsName;
  }

  private static ElementVersionSensitiveMap getVersionSensitiveCache()
  {
    String currDS = getCurrentDs();
    if (dsName_instance_map.get(currDS) == null) {
      CacheToMapAdapter adapter = CacheToMapAdapter.getInstance(currDS + "platform", CachePolicyFactory.createLRUCachePolicy());

      ElementVersionSensitiveMap versionMap = new ElementVersionSensitiveMap(adapter, new PfVersionMonitorFacotry());

      dsName_instance_map.put(currDS, versionMap);
    }
    return (ElementVersionSensitiveMap)dsName_instance_map.get(currDS);
  }

  private static ICacheManager getFileCacheManager()
    throws CacheException
  {
    ICacheManager cache = UiCacheManager.getInstance();
    cache.initCacheRegion(getCacheRegion(), CachePolicyFactory.createDynamicFileCachePolicy());
    return cache;
  }

  private static String getCacheRegion()
  {
    return "platform";
  }

  public static MessagePanelOptions getMsgPanelOptions()
  {
    MessagePanelOptions mpo = null;
    CacheKey cacheKey = null;
    try {
      cacheKey = CacheKey.createKey(ClientEnvironment.getInstance().getUser().getPrimaryKey() + "MSGPANELOPTIONS");

      mpo = (MessagePanelOptions)getFileCacheManager().getCacheObject(cacheKey, getCacheRegion());
      if (mpo == null) {
        mpo = new MessagePanelOptions();
        getFileCacheManager().putCacheObject(cacheKey, mpo, getCacheRegion());
      }
    } catch (Throwable e) {
      e.printStackTrace();
      if ((cacheKey != null) && (mpo == null))
      {
        mpo = new MessagePanelOptions();
        try {
          getFileCacheManager().putCacheObject(cacheKey, mpo, getCacheRegion());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        mpo = new MessagePanelOptions();
      }
    }
    return mpo;
  }

  public static void putMsgPanelOptions(MessagePanelOptions mpo)
  {
    CacheKey cacheKey = CacheKey.createKey(ClientEnvironment.getInstance().getUser().getPrimaryKey() + "MSGPANELOPTIONS");
    try
    {
      getFileCacheManager().putCacheObject(cacheKey, mpo, getCacheRegion());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String[] getStrBillVo(String pkBillType)
  {
    HashMap hashCacheObj = null;
    String hashKey = pkBillType;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHUIBILLVO");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setVoTables(hashCacheObj, pkBillType);
        getVersionSensitiveCache().put("KHHUIBILLVO", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setVoTables(hashCacheObj, pkBillType);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return (String[])(String[])hashCacheObj.get(pkBillType);
  }

  private static void setVoTables(HashMap hashCacheObj, String pkBillType)
  {
    try
    {
      IPFVoMapping bsVotable = (IPFVoMapping)NCLocator.getInstance().lookup(IPFVoMapping.class.getName());

      String[] retAry = bsVotable.querybillVoInfo(pkBillType);
      hashCacheObj.put(pkBillType, retAry);
    } catch (BusinessException e) {
      e.printStackTrace();
    }
  }

  public static BasedocVO getBdinfo(String strPK)
  {
    HashMap hashCacheObj = null;
    String hashKey = strPK;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHBDINFO");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setBdInfo(hashCacheObj);
        getVersionSensitiveCache().put("KHHBDINFO", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setBdInfo(hashCacheObj);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (BasedocVO)hashCacheObj.get(hashKey);
  }

  public static BilltobillreferVO getBillInfo(String currentBillType, String billType)
  {
    HashMap hashCacheObj = null;
    String hashKey = currentBillType + billType;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHINFOHAS");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        getBillReferInfos(hashCacheObj);
        getVersionSensitiveCache().put("KHHINFOHAS", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        getBillReferInfos(hashCacheObj);
      }

      return (BilltobillreferVO)hashCacheObj.get(hashKey);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String getBillPK(String billType)
  {
    HashMap hashCacheObj = null;
    String hashKey = billType;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHBILLPKHAS");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setBillPK(hashCacheObj);
        getVersionSensitiveCache().put("KHHBILLPKHAS", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setBillPK(hashCacheObj);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (String)hashCacheObj.get(hashKey);
  }

  public static String getBillTypeByStyle(String billStyle)
  {
    HashMap hashCacheObj = null;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get(STR_BILLSTYLETOTYPE);
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setBilltypeOfStyle(hashCacheObj);

        getVersionSensitiveCache().put(STR_BILLSTYLETOTYPE, hashCacheObj);
      } else if (!hashCacheObj.containsKey(billStyle)) {
        setBilltypeOfStyle(hashCacheObj);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (String)hashCacheObj.get(billStyle);
  }

  private static void setBilltypeOfStyle(HashMap hashCacheObj)
  {
    BilltypeVO[] tmpVos = null;
    try {
      tmpVos = BilltypeBO_Client.queryAllBillType();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (int i = 0; i < (tmpVos == null ? 0 : tmpVos.length); i++)
    {
      BilltypeVO billtype = tmpVos[i];
      if ((billtype.getIsroot() == null) || (!billtype.getIsroot().booleanValue()) || (billtype.getBillstyle() == null))
        continue;
      hashCacheObj.put(billtype.getBillstyle().toString(), billtype.getPrimaryKey());
    }
  }

  public static ArrayList getBillType2Info(String billType, int classtype)
  {
    HashMap hashCacheObj = null;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get(STR_BILLTYPE2INFO);

      if (hashCacheObj == null) {
        hashCacheObj = queryAllBilltype2();

        getVersionSensitiveCache().put(STR_BILLTYPE2INFO, hashCacheObj);
      } else if ((!hashCacheObj.containsKey(Integer.valueOf(classtype))) || (!hasCachedBilltype2(hashCacheObj, billType)))
      {
        HashMap hmBilltype2VOs = queryAllBilltype2();
        hashCacheObj.putAll(hmBilltype2VOs);
      }
    } catch (Exception ex) {
      Logger.warn(ex.getMessage(), ex);
    }

    return findBilltype2ByBilltypeAndClasstype(billType, classtype, hashCacheObj);
  }

  private static HashMap queryAllBilltype2() throws BusinessException {
    IUAPQueryBS uapQry = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    String strSQL = "select pk_billtype,classname,classtype from bd_billtype2";
    ArrayList alRet = (ArrayList)uapQry.executeQuery(strSQL, new BaseProcessor() {
      public Object processResultSet(ResultSet rs) throws SQLException {
        ArrayList al = new ArrayList();
        while (rs.next()) {
          Billtype2VO vo = new Billtype2VO();

          vo.state = Integer.valueOf(2);
          String pk_billtype = rs.getString(1);
          vo.setPk_billtype(pk_billtype == null ? "" : pk_billtype.trim());

          String classname = rs.getString(2);
          vo.setClassname(classname == null ? "" : classname.trim());
          int iClsType = rs.getInt(3);
          vo.setClasstype(new Integer(iClsType));

          al.add(vo);
        }
        return al;
      }
    });
    ArrayList alDynamic = new ArrayList();
    ArrayList alSuperior = new ArrayList();
    ArrayList alReceiver = new ArrayList();
    for (Iterator iter = alRet.iterator(); iter.hasNext(); ) {
      Billtype2VO bt2VO = (Billtype2VO)iter.next();
      switch (bt2VO.getClasstype().intValue()) {
      case 1:
        alDynamic.add(bt2VO);
        break;
      case 2:
        alSuperior.add(bt2VO);
        break;
      case 3:
        alReceiver.add(bt2VO);
      }

    }

    HashMap hm = new HashMap();
    hm.put(Integer.valueOf(1), alDynamic);
    hm.put(Integer.valueOf(2), alSuperior);
    hm.put(Integer.valueOf(3), alReceiver);
    return hm;
  }

  private static ArrayList findBilltype2ByBilltypeAndClasstype(String billType, int classtype, HashMap hashCacheObj)
  {
    ArrayList alRet = new ArrayList();
    ArrayList alBilltype2VO = (ArrayList)hashCacheObj.get(Integer.valueOf(classtype));
    for (Iterator iter = alBilltype2VO.iterator(); iter.hasNext(); ) {
      Billtype2VO bt2VO = (Billtype2VO)iter.next();
      if (classtype == bt2VO.getClasstype().intValue())
      {
        if ((billType.equals(bt2VO.getPk_billtype())) || (bt2VO.getPk_billtype().equals("XX")))
        {
          alRet.add(bt2VO);
        }
      }
    }
    return alRet;
  }

  private static boolean hasCachedBilltype2(HashMap hashCacheObj, String billType)
  {
    for (Iterator iter = hashCacheObj.values().iterator(); iter.hasNext(); ) {
      ArrayList alBilltype2VO = (ArrayList)iter.next();
      for (Iterator  iterator= alBilltype2VO.iterator(); iterator.hasNext(); ) {
        Billtype2VO bt2VO = (Billtype2VO)iterator.next();
        if (billType.equals(bt2VO.getPk_billtype()))
          return true;
      }
    }
    Iterator iterator;
    return false;
  }

  public static BilltypeVO getBillType(String billType)
  {
    HashMap hashCacheObj = null;
    String hashKey = billType;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHBILLTYPEHAS");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setBillType(hashCacheObj);
        getVersionSensitiveCache().put("KHHBILLTYPEHAS", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setBillType(hashCacheObj);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (BilltypeVO)hashCacheObj.get(hashKey);
  }

  public static BusitypeVO[] getBusiByCorpAndBill(String pkCorp, String billType)
  {
    HashMap hashCacheObj = null;
    String hashKey = pkCorp + billType;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("BUSIBYCORPANDBILL");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setBusiByCorpAndBill(hashCacheObj, pkCorp, billType);
        getVersionSensitiveCache().put("BUSIBYCORPANDBILL", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setBusiByCorpAndBill(hashCacheObj, pkCorp, billType);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (BusitypeVO[])(BusitypeVO[])hashCacheObj.get(hashKey);
  }

  public static PfUtilBillActionVO[] getButtonByBillAndGrp(String billType, String actionStyle)
  {
    String hashKey = billType + actionStyle;
    HashMap hashCacheObj = null;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("BUTTONBYBILLANDGRP");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setButtonByBillAndGrp(hashCacheObj, billType, actionStyle);
        getVersionSensitiveCache().put("BUTTONBYBILLANDGRP", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setButtonByBillAndGrp(hashCacheObj, billType, actionStyle);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (PfUtilBillActionVO[])(PfUtilBillActionVO[])hashCacheObj.get(hashKey);
  }

  public static CurrtypeVO getCurrType(String strPk)
  {
    HashMap hashCacheObj = null;
    String hashKey = strPk;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("KHHCURRENCY");
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setCurrTypeInfo(hashCacheObj);
        getVersionSensitiveCache().put("KHHCURRENCY", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setCurrTypeInfo(hashCacheObj);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (CurrtypeVO)hashCacheObj.get(hashKey);
  }

  public static BillbusinessVO[] getSourceByCorpAndBillAndBusi(String pkCorp, String billType, String businessType)
  {
    String hashKey = null;
    HashMap hashCacheObj = null;
    try {
      hashCacheObj = (HashMap)getVersionSensitiveCache().get("SOURCEBYCORPANDBILLANDBUSI");

      if (businessType == null)
        hashKey = pkCorp + billType;
      else {
        hashKey = pkCorp + billType + businessType;
      }
      if (hashCacheObj == null) {
        hashCacheObj = new HashMap();
        setSourceByCorpAndBillAndBusi(hashCacheObj, pkCorp, billType, businessType);
        getVersionSensitiveCache().put("SOURCEBYCORPANDBILLANDBUSI", hashCacheObj);
      } else if (!hashCacheObj.containsKey(hashKey)) {
        setSourceByCorpAndBillAndBusi(hashCacheObj, pkCorp, billType, businessType);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return (BillbusinessVO[])(BillbusinessVO[])hashCacheObj.get(hashKey);
  }

  private static void setBdInfo(HashMap tmpHas)
  {
    try
    {
      BasedocVO[] tmpVos = PfUtilBaseTools.getAllBasedocVO();
      for (int i = 0; i < (tmpVos == null ? 0 : tmpVos.length); i++)
        tmpHas.put(tmpVos[i].getDocPK(), tmpVos[i]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void getBillReferInfos(HashMap tmpHas)
  {
    try
    {
      BilltobillreferVO[] tmpVos = BilltobillreferBO_Client.queryAll();
      for (int i = 0; i < (tmpVos == null ? 0 : tmpVos.length); i++) {
        String strkey = tmpVos[i].getBilltype().trim() + tmpVos[i].getSourcebilltype().trim();
        tmpHas.put(strkey, tmpVos[i]);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void setBillPK(HashMap tmpHas)
  {
    try
    {
      VotableVO condVO = new VotableVO();
      condVO.setHeadbodyflag(UFBoolean.TRUE);
      IUAPQueryBS uapQryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

      Collection co = uapQryBS.retrieve(condVO, true, new String[] { "pk_billtype", "pkfield" });

      for (Iterator iter = co.iterator(); iter.hasNext(); ) {
        VotableVO vot = (VotableVO)iter.next();
        tmpHas.put(vot.getPk_billtype(), vot.getPkfield());
      }
    }
    catch (Exception ex)
    {
      Iterator iter;
      ex.printStackTrace();
    }
  }

  private static void setBillType(HashMap tmpHas)
  {
    try
    {
      BilltypeVO[] tmpVos = BilltypeBO_Client.queryAllBillType();
      if (tmpVos != null)
        for (int i = 0; i < tmpVos.length; i++)
          if (tmpVos[i].getNodecode() != null)
            tmpHas.put(tmpVos[i].getPrimaryKey().trim(), tmpVos[i]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void setBusiByCorpAndBill(HashMap tmpHas, String pkCorp, String billType)
  {
    try
    {
      String key = pkCorp + billType;
      IPFConfig pfConfig = getPFConfig();
      BusitypeVO[] billReferVos = pfConfig.querybillBusinessType(pkCorp, billType);
      if (billReferVos != null)
        tmpHas.put(key, billReferVos);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static IPFConfig getPFConfig()
    throws ComponentException
  {
    IPFConfig pfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    return pfConfig;
  }

  private static void setButtonByBillAndGrp(HashMap tmpHas, String billType, String actionStyle)
  {
    PfUtilBillActionVO[] billActionVos = null;
    try
    {
      String key = billType + actionStyle;
      billActionVos = getPFConfig().querybillActionStyle(billType, actionStyle);
      if (billActionVos != null)
        tmpHas.put(key, billActionVos);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void setCurrTypeInfo(HashMap tmpHas)
  {
    try
    {
      ICurrtype currtype = (ICurrtype)NCLocator.getInstance().lookup(ICurrtype.class.getName());
      CurrtypeVO[] tmpVos = currtype.queryAllCurrtypeVO(null);
      if (tmpVos != null)
        for (int i = 0; i < tmpVos.length; i++)
          tmpHas.put(tmpVos[i].getPk_currtype(), tmpVos[i]);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private static void setSourceByCorpAndBillAndBusi(HashMap hashCacheObj, String pkCorp, String billType, String businessType)
  {
    BillbusinessVO[] billReferVo = null;
    try
    {
      String key = null;
      if (businessType == null)
        key = pkCorp + billType;
      else {
        key = pkCorp + billType + businessType;
      }
      billReferVo = getPFConfig().querybillSource(pkCorp, billType, businessType);
      hashCacheObj.put(key, billReferVo);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static ICacheVersionBS getCacheVersionBS() {
    ICacheVersionBS CacheService = (ICacheVersionBS)NCLocator.getInstance().lookup(ICacheVersionBS.class.getName());

    return CacheService;
  }

  static class PfVersionMonitorFacotry
    implements VersionMonitorFactory
  {
    public ICacheVersionMonitor createVersionMonitor(Object arg0)
    {
      return new ObjectCacheVersionMonitor((String)arg0, 600000L);
    }
  }
}