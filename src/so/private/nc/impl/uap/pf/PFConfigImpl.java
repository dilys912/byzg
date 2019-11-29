package nc.impl.uap.pf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.mobile.WirelessManager;
import nc.bs.pub.msg.PFMessageDMO;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.pub.pf.PfUtilDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.rbac.IPowerManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.FieldDefList;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.SysMessageParam;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.uap.rbac.power.PowerQueryByResVO;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.uap.rbac.power.UserPowerQueryVO;

public class PFConfigImpl
  implements IPFConfig
{
  private boolean isContinue(String corpPK, String operator, int configflag, String dataOperator)
    throws Exception
  {
    boolean retFlag = false;

    if ((configflag == 2) && 
      (!dataOperator.equals(operator))) {
      retFlag = true;
    }

    if (configflag == 3)
    {
      IUserManageQuery umq = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());

      RoleVO[] roles = umq.getUserRole(operator, corpPK);

      boolean isExistGroup = false;
      Logger.debug("当前组ID" + dataOperator);
      for (int i = 0; i < (roles == null ? 0 : roles.length); i++) {
        Logger.debug("用户所在组ID" + roles[i].getPrimaryKey());
        if (dataOperator.equals(roles[i].getPrimaryKey())) {
          isExistGroup = true;
          break;
        }
      }
      if (!isExistGroup)
        retFlag = true;
    }
    return retFlag;
  }

  public String[] getBusitypeByCorpAndStyle(String pk_corp, String style)
    throws BusinessException
  {
    Collection co = null;
    try {
      BusitypeVO condvo = new BusitypeVO();
      condvo.setPk_corp(pk_corp);
      condvo.setVerifyrule(style);
      BaseDAO dao = new BaseDAO();
      co = dao.retrieve(condvo, true, new String[] { condvo.getPKFieldName() });
    } catch (DAOException ex) {
      Logger.error(ex.getMessage(), ex);
      throw new PFBusinessException(ex.getMessage());
    }
    if ((co == null) || (co.size() == 0))
      return null;
    ArrayList alRet = new ArrayList();
    for (Iterator iter = co.iterator(); iter.hasNext(); ) {
      BusitypeVO busitype = (BusitypeVO)iter.next();
      alRet.add(busitype.getPrimaryKey());
    }
    return (String[])(String[])alRet.toArray(new String[alRet.size()]);
  }

  public BusitypeVO[] querybillBusinessType(String corpId, String billType)
    throws BusinessException
  {
    BusitypeVO[] busiVos = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      busiVos = dmo.queryBillBusinessType(corpId, billType);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return busiVos;
  }

  public BillbusinessVO[] querybillSource(String corpId, String billType, String businessType)
    throws BusinessException
  {
    BillbusinessVO[] billReferVo = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      billReferVo = dmo.queryBillSource(corpId, billType, businessType);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return billReferVo;
  }

  public PfUtilBillActionVO[] querybillStateActionStyle(String corpId, String billType, String billState, String businessType, String actionStyle, String operator)
    throws BusinessException
  {
    PfUtilBillActionVO[] billActionVos = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      billActionVos = dmo.queryBillStateActionStyle(corpId, billType, billState, businessType, actionStyle);

      Vector v = new Vector();
      if ((billActionVos != null) && (billActionVos.length > 0)) {
        for (int i = 0; i < billActionVos.length; i++)
        {
          boolean isSkip = isContinue(corpId, operator, billActionVos[i].getConfigFlag(), billActionVos[i].getRelaPk());

          if (!isSkip) {
            v.addElement(billActionVos[i]);
          }
        }
      }
      if (v.size() > 0) {
        billActionVos = new PfUtilBillActionVO[v.size()];
        v.copyInto(billActionVos);
      } else {
        billActionVos = null;
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return billActionVos;
  }

  public PfUtilBillActionVO[] querybillStateActionStyleNoBusi(String billType, String billState, String actionStyle)
    throws BusinessException
  {
    PfUtilBillActionVO[] billActionVo = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      billActionVo = dmo.queryBillStateActionStyleNoBusi(billType, billState, actionStyle);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return billActionVo;
  }

  public CircularlyAccessibleValueObject[] queryHeadAllData(String billType, String businessType, String whereString)
    throws BusinessException
  {
    Logger.info("*****查询单据的表头数据开始*****");

    StringBuffer retWhere = new StringBuffer();
    BilltypeVO billTypeVO = PfDataCache.getBillTypeInfo(billType);

    boolean isExistDbWhere = true;
    if ((billTypeVO.getWherestring() != null) && (!billTypeVO.getWherestring().trim().equals(""))) {
      retWhere.append(" ").append(billTypeVO.getWherestring());
    } else {
      isExistDbWhere = false;
      retWhere.append(" ");
    }

    if ((whereString != null) && (!whereString.trim().equals(""))) {
      if (isExistDbWhere)
        retWhere.append(" and ").append(whereString);
      else {
        retWhere.append(whereString);
      }
    }

    VotableVO votable = PfDataCache.getBillTypeToVO(billType, true);
    String strBusiType = votable.getBusitype();
    if (retWhere.length() > 10) {
      if ((businessType != null) && (!businessType.trim().equals("")))
        retWhere.append(" and ").append(strBusiType).append("='").append(businessType).append("'");
    }
    else
      retWhere.delete(0, retWhere.length());
    String referClsName = billTypeVO.getReferclassname();
    if ((referClsName == null) || (referClsName.equals(""))) {
      Logger.error("未注册实现查询数据的Dmo端类文件");
      throw new PFBusinessException("未注册实现查询数据的Dmo端类文件");
    }
    Logger.info("查询主表的条件语句：" + retWhere);
    IQueryData tmpObj = (IQueryData)PfUtilTools.instantizeObject(billType, referClsName.trim());
    CircularlyAccessibleValueObject[] retVos = tmpObj.queryAllHeadData(retWhere.toString());
    Logger.info("*****查询单据的表头数据结束*****");
    return retVos;
  }

  public CircularlyAccessibleValueObject[] queryBodyAllData(String billType, String parentPK, String bodyCondition)
    throws BusinessException
  {
    CircularlyAccessibleValueObject[] retVos = null;
    Logger.info("*****查询单据的表体数据开始*****");

    BilltypeVO billReferVo = PfDataCache.getBillTypeInfo(billType);
    if ((billReferVo.getReferclassname() == null) || (billReferVo.getReferclassname().equals(""))) {
      Logger.error("未注册实现查询数据的Dmo端类文件");
      throw new PFBusinessException("未注册实现查询数据的Dmo端类文件");
    }

    Object queryObj = PfUtilTools.instantizeObject(billType, billReferVo.getReferclassname().trim());

    if ((bodyCondition == null) || (bodyCondition.trim().equals(""))) {
      Logger.info("执行实现接口IQueryData的数据");
      IQueryData tmpObj = (IQueryData)queryObj;
      retVos = tmpObj.queryAllBodyData(parentPK);
    } else {
      Logger.info("执行实现接口IQueryData2的数据");
      Logger.info("执行实现接口IQueryData2的数据,子表查询条件为:" + bodyCondition);
      IQueryData2 tmpObj = (IQueryData2)queryObj;
      retVos = tmpObj.queryAllBodyData(parentPK, bodyCondition);
    }
    Logger.info("*****查询单据的表体数据结束*****");
    return retVos;
  }

  public AggregatedValueObject queryBillDataVO(String billType, String billId)
    throws BusinessException
  {
    BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo(billType);

    VotableVO votable = PfDataCache.getBillTypeToVO(billType, true);

    String pkField = votable.getPkfield();

    String billVoClassName = votable.getBillvo();

    String tablename = votable.getVotable();
    try
    {
      AggregatedValueObject billVO = (AggregatedValueObject)(AggregatedValueObject)Class.forName(billVoClassName).newInstance();

      String referClsName = billtypeVO.getReferclassname();
      if ((referClsName == null) || (referClsName.length() == 0)) {
        throw new PFBusinessException("单据类型尚未注册查询单据数据的类");
      }

      Logger.debug(">>通过业务单据查询类=" + referClsName + ",查询单据聚合VO");
      IQueryData queryDataObj = (IQueryData)PfUtilTools.instantizeObject(billType, referClsName.trim());

      String m_wherestr = tablename + "." + pkField + "='" + billId + "'";
      CircularlyAccessibleValueObject[] headVOs = queryDataObj.queryAllHeadData(m_wherestr);
      CircularlyAccessibleValueObject[] bodyVOs = queryDataObj.queryAllBodyData(billId);
      if ((headVOs == null) || (headVOs.length == 0)) {
        throw new PFBusinessException("获取单据主表VO失败");
      }
      billVO.setParentVO(headVOs[0]);
      billVO.setChildrenVO(bodyVOs);
      return billVO;
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    throw new PFBusinessException(e.getMessage());
    }
  }

  public PfUtilBillActionVO[] querybillActionStyle(String billType, String actionStyle)
    throws BusinessException
  {
    PfUtilBillActionVO[] billActionVos = null;
    try {
      PfUtilDMO dmo = new PfUtilDMO();
      billActionVos = dmo.queryBillActionStyle(billType, actionStyle);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
    return billActionVos;
  }

  public SysMessageParam getSysMsgParam()
    throws BusinessException
  {
    return WirelessManager.fetchSysMsgParam();
  }

  public void saveSysMsgParam(SysMessageParam smp)
    throws BusinessException
  {
    try
    {
      WirelessManager.saveSysMsgParam(smp);
    } catch (IOException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
  }

  public PowerResultVO queryPowerBusiness(String userID, String corpPK, String billtype)
    throws BusinessException
  {
    PowerResultVO voRet = new PowerResultVO();

    PowerResultVO voPower = getPowerBillBusiness(userID, corpPK);
    voRet.setPowerControl(voPower.isPowerControl());
    if (voPower.isPowerControl())
    {
      String[] sBusinessCtrl = getBusinessOfBilltype(voPower.getPowerId(), billtype);

      String[] sBusinessNoCtrl = null;
      try {
        sBusinessNoCtrl = new PfUtilDMO().queryBusitypeOfNoControl(corpPK, billtype);
      } catch (DbException e) {
        Logger.error(e.getMessage(), e);
        throw new PFBusinessException(e.getMessage());
      }

      if ((sBusinessCtrl == null) || (sBusinessCtrl.length == 0)) {
        voRet.setPowerId(sBusinessNoCtrl);
      } else if ((sBusinessNoCtrl == null) || (sBusinessNoCtrl.length == 0)) {
        voRet.setPowerId(sBusinessCtrl);
      } else {
        String[] sBusinessAll = new String[sBusinessCtrl.length + sBusinessNoCtrl.length];
        int i = 0;
        for (; i < sBusinessCtrl.length; i++) {
          sBusinessAll[i] = sBusinessCtrl[i];
        }
        for (int j = 0; j < sBusinessNoCtrl.length; j++) {
          sBusinessAll[(i++)] = sBusinessNoCtrl[j];
        }
        voRet.setPowerId(sBusinessAll);
      }
    }
    return voRet;
  }

  private PowerResultVO getPowerBillBusiness(String userID, String corpPK)
    throws BusinessException
  {
    UserPowerQueryVO voQuery = new UserPowerQueryVO();
    voQuery.setResouceId(10);
    voQuery.setUserPK(userID);
    voQuery.setOrgTypeCode(1);
    voQuery.setOrgPK(corpPK);
    voQuery.setCorpPK(corpPK);
    IPowerManageQuery iPower = (IPowerManageQuery)NCLocator.getInstance().lookup(IPowerManageQuery.class.getName());

    PowerResultVO voPower = iPower.getUserPower(voQuery);
    return voPower;
  }

  private String[] getBusinessOfBilltype(String[] busiPks, String billtype)
    throws BusinessException
  {
    if ((busiPks == null) || (busiPks.length == 0))
      return null;
    String sWhere = "pk_billType='" + billtype + "' and (pk_billbusiness='" + busiPks[0] + "'";
    for (int i = 1; i < busiPks.length; i++) {
      sWhere = sWhere + " or pk_billbusiness='" + busiPks[i] + "'";
    }
    sWhere = sWhere + ")";
    Collection collection = new BaseDAO().retrieveByClause(BillbusinessVO.class, sWhere, new String[] { "pk_businesstype" });

    if ((collection == null) || (collection.size() == 0))
      return null;
    String[] sRet = new String[collection.size()];
    BillbusinessVO voTmp = null;
    int i = 0;
    for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
      voTmp = (BillbusinessVO)iter.next();
      sRet[i] = voTmp.getPk_businesstype();
      i++;
    }
    return sRet;
  }

  public void generateBillItemByDDC(String mainTable, String subTable, String pk_billtype)
    throws BusinessException
  {
    if ((mainTable != null) && (subTable != null)) {
      Datadict datadict = Datadict.getDefaultInstance();

      TableDef m_tableDef = datadict.findTableDef(mainTable);
      TableDef s_tableDef = datadict.findTableDef(subTable);
      if (m_tableDef == null)
        throw new PFBusinessException("数据字典中没有找到这个表" + mainTable);
      if (s_tableDef == null) {
        throw new PFBusinessException("数据字典中没有找到这个表" + s_tableDef);
      }
      FieldDefList m_fieldDefList = m_tableDef.getFieldDefs();
      FieldDefList s_tableDefList = s_tableDef.getFieldDefs();
      int mCount = m_fieldDefList.getCount();
      DefitemVO[] defItemVOs = new DefitemVO[mCount + s_tableDefList.getCount()];

      for (int i = 0; i < mCount; i++)
      {
        FieldDef fieldDef = m_fieldDefList.getFieldDef(i);
        defItemVOs[i] = new DefitemVO();
        defItemVOs[i].setHeadflag(UFBoolean.TRUE);
        setDefItemVOByFieldDef(defItemVOs[i], fieldDef, pk_billtype);
      }

      for (int j = 0; j < s_tableDefList.getCount(); j++) {
        defItemVOs[(mCount + j)] = new DefitemVO();
        defItemVOs[(mCount + j)].setHeadflag(UFBoolean.FALSE);
        setDefItemVOByFieldDef(defItemVOs[(mCount + j)], s_tableDefList.getFieldDef(j), pk_billtype);
      }

      if (defItemVOs.length > 0) {
        PfUtilDMO pfdmo = new PfUtilDMO();
        pfdmo.insertBillitems(defItemVOs);
      }
    }
  }

  private void setDefItemVOByFieldDef(DefitemVO defitemVO, FieldDef fieldDef, String pk_billtype)
  {
    if ((defitemVO != null) && (fieldDef != null)) {
      defitemVO.setPk_billtype(pk_billtype);
      defitemVO.setAttrname(fieldDef.getID());
      defitemVO.setItemname("");
      defitemVO.setResourceid(fieldDef.getDisplayName());
      int itemtype = transDataType(fieldDef.getDataType());
      defitemVO.setItemtype(new Integer(itemtype));
    }
  }

  private int transDataType(int dataType)
  {
    int itemtype = 0;
    switch (dataType) {
    case 2:
    case 3:
    case 6:
    case 7:
    case 8:
      itemtype = 1;
      break;
    case 16:
      itemtype = 5;
      break;
    case 91:
      itemtype = 2;
      break;
    case 92:
    case 93:
      itemtype = 4;
      break;
    case -5:
    case 4:
    case 5:
      itemtype = 3;
      break;
    default:
      itemtype = 0;
    }

    return itemtype;
  }

  public BillbusinessVO[] queryBillDest(String billType, String busiType)
    throws BusinessException
  {
    try
    {
      return new PfUtilDMO().queryBillDest(billType, busiType);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
    throw new PFBusinessException(e.getMessage());
    }
  }

  public RoleVO[] queryRolesHasBillbusi(String pkCorp, String billType, String busiType, boolean bQueryAllCorp)
    throws BusinessException
  {
    BillbusinessVO condVO = new BillbusinessVO();
    condVO.setPk_billtype(billType);
    condVO.setPk_businesstype(busiType);
    BaseDAO dao = new BaseDAO();
    Collection co = dao.retrieve(condVO, true, new String[] { "pk_billbusiness" });
    BillbusinessVO billbusiVO = null;
    if (co.size() > 0) {
      Iterator iter = co.iterator();
      billbusiVO = (BillbusinessVO)iter.next();
    } else {
      return null;
    }
    PowerQueryByResVO voQuery = new PowerQueryByResVO();
    voQuery.setResouceId(10);
    voQuery.setResourceDataId(billbusiVO.getPrimaryKey());
    voQuery.setOrgTypeCode(1);
    voQuery.setOrgPK(pkCorp);
    voQuery.setCorpPK(pkCorp);
    voQuery.setQueryAllCorp(bQueryAllCorp);
    voQuery.setQueryAllocRole(false);
    IPowerManageQuery query = (IPowerManageQuery)NCLocator.getInstance().lookup(IPowerManageQuery.class.getName());

    return query.queryPowerRole(voQuery);
  }

  public void completeWorkitem(String billId, String[] srcBillIds, String checkman, String billtype, String srcBilltype)
    throws BusinessException
  {
    HashSet hs = new HashSet();
    if ((billId != null) && (billId.length() != 0))
      hs.add(billId);
    for (int i = 0; i < (srcBillIds == null ? 0 : srcBillIds.length); i++) {
      hs.add(srcBillIds[i]);
    }
    if (hs.size() == 0)
      return;
    try {
      new PFMessageDMO().completeWorkitem(hs, checkman, billtype, srcBilltype);
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new PFBusinessException(e.getMessage());
    }
  }
}