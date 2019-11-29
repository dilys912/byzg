package nc.impl.dm.dm102;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import nc.bs.bd.CorpBO;
import nc.bs.bd.datapower.DataPowerServiceBO;
import nc.bs.dm.dm102.DeliverydailyplanDMO;
import nc.bs.dm.dm104.DelivbillHDMO;
import nc.bs.dm.pub.DmDMO;
import nc.bs.dm.pub.RewriteDMO;
import nc.bs.dm.pub.fromic.DmFromIcDMO;
import nc.bs.dm.pub.frompu.DmFromPUDMO;
import nc.bs.dm.pub.fromso.DmFromSoDMO;
import nc.bs.dm.pub.fromto.DmFromTODMO;
import nc.bs.dm.pub.toic.DMATP;
import nc.bs.dm.pub.toic.DmToIcDMO;
import nc.bs.dm.pub.toso.DmToSoDMO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.billcodemanage.BillcodeRuleBO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.sm.user.UserBO;
import nc.impl.dm.pub.DmImpl;
import nc.itf.dm.dm102.IDeliverydailyplan;
import nc.itf.dm.dm102.IDeliverydailyplanQuery;
import nc.itf.pu.inter.IPuToDm_Order;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.bd.CorpVO;
import nc.vo.dm.dm102.DMDelivdayplVOTool;
import nc.vo.dm.dm102.OutbillHHeaderVO;
import nc.vo.dm.dm102.OutbillHItemVO;
import nc.vo.dm.dm102.OutbillHVO;
import nc.vo.dm.dm102.OutbillVO;
import nc.vo.dm.dm102.ValueRangeHashtableDeliverydailyplan;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DailyPlanStatus;
import nc.vo.dm.pub.ExceptionUtils;
import nc.vo.dm.pub.ValueRange;
import nc.vo.dm.pub.ValueRangeHashtable;
import nc.vo.dm.pub.tools.StringTools;
import nc.vo.dm.pub.tools.UsefulTools;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.datatype.DataTypeTool;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.TimeAttributeName;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.scm.recordtime.RecordType;
import nc.vo.scm.rewrite.ParaDayplToPoRewriteVO;
import nc.vo.scm.rewrite.ParaDelivToPoRewriteVO;
import nc.vo.scm.rewrite.ParaRewriteVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.to.pub.BillVO;

public class DeliverydailyplanImpl extends DmImpl
  implements IDeliverydailyplan, IDeliverydailyplanQuery
{
  private void rewriteDailyPlanNum2Order(DMDataVO[] dailyPlanVOs)
    throws Exception
  {
    if (dailyPlanVOs[0].getAttributeValue("vbilltype").equals("30"))
    {
      DmToSoDMO dmo2 = new DmToSoDMO();
      dmo2.setDeliverNumToSO(dailyPlanVOs);
    }
    else if (dailyPlanVOs[0].getAttributeValue("vbilltype").equals("21")) {
      ParaDayplToPoRewriteVO rewriteVO = DMDelivdayplVOTool.getDayplWritePOData(dailyPlanVOs);
      getPUInter().rewriteDayplNum(rewriteVO);
    }
    else {
      ParaRewriteVO[] prvo = getParaRewriteVO(dailyPlanVOs, "dnum");
      if ((prvo != null) && (prvo.length > 0))
      {
        DmFromTODMO boNBDB = new DmFromTODMO();
        for (int i = 0; i < prvo.length; i++)
          boNBDB.backDMToOrder(prvo[i], new Integer(2));
      }
    }
  }

  private void modifyATP(DMDataVO[] dailyPlanVOs)
    throws Exception
  {
    DMVO dvo = new DMVO();
    dvo.setChildrenVO(dailyPlanVOs);

    DMATP dmatp = new DMATP();
    dmatp.modifyATP(dvo);
  }

  public DMDataVO[] auditDeliverydailyplan(DMDataVO[] vos)
    throws BusinessException
  {
    String[] sAllKeys = (String[])null;
    try
    {
      sAllKeys = checkLockPKs(vos);

      saveCheck(vos);

      auditCheckStorePower(vos);

      for (int i = 0; i < vos.length; i++)
      {
        signButtonClickTime(RecordType.AUDITSEND, vos[i], false);
      }

      DMDataVO[] arrayOfDMDataVO = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);
      return arrayOfDMDataVO;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
  }

  private void auditCheckStorePower(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      ArrayList alCheckVos = new ArrayList();
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getAttributeValue("vbilltype").toString().equals("21")) || (vos[i].getAttributeValue("pksendstore") == null) || (vos[i].getAttributeValue("pksendstore").toString().trim().length() == 0) || (vos[i].getAttributeValue("pksalecorp") == null) || (vos[i].getAttributeValue("pksalecorp").toString().trim().length() == 0))
        {
          continue;
        }
        alCheckVos.add(vos[i]);
      }

      DMDataVO[] checkvos = (DMDataVO[])alCheckVos.toArray(new DMDataVO[0]);
      if (checkvos.length == 0) {
        return;
      }

      Hashtable htCorp = new Hashtable();
      for (int i = 0; i < checkvos.length; i++) {
        htCorp.put(checkvos[i].getAttributeValue("pksalecorp").toString(), "");
      }
      String[] sCorps = UsefulTools.hashtableKeysToStrings(htCorp);

      DataPowerServiceBO bo = new DataPowerServiceBO();
      Hashtable htAllPowerStore = new Hashtable();
      for (int i = 0; i < sCorps.length; i++) {
        if (bo.isUsedDataPower("bd_stordoc", "仓库档案", sCorps[i])) {
          htCorp.put(checkvos[i].getAttributeValue("pksalecorp").toString(), new UFBoolean(true));
          String[] sPowerStores = bo.hasPower("bd_stordoc", "仓库档案", checkvos[i].getAttributeValue("userid").toString(), sCorps[i]);

          if ((sPowerStores != null) && (sPowerStores.length != 0)) {
            for (int j = 0; j < sPowerStores.length; j++)
              htAllPowerStore.put(sPowerStores[j], "haspower");
          }
        }
        else
        {
          htCorp.put(checkvos[i].getAttributeValue("pksalecorp").toString(), new UFBoolean(false));
        }
      }
      Hashtable htNoPowerStoreName = new Hashtable();
      for (int i = 0; i < checkvos.length; i++) {
        if ((!((UFBoolean)htCorp.get(checkvos[i].getAttributeValue("pksalecorp").toString())).booleanValue()) || (htAllPowerStore.get(checkvos[i].getAttributeValue("pksendstore").toString()) != null))
          continue;
        htNoPowerStoreName.put(checkvos[i].getAttributeValue("vsendstorename").toString(), "");
      }

      if (htNoPowerStoreName.size() > 0) {
        String NoPowerStoreName = StringTools.getStrIDsFromHt(htNoPowerStoreName);
        StringBuffer sbErroMessage = new StringBuffer("操作员对以下仓库无权限:");
        sbErroMessage.append(NoPowerStoreName);
        sbErroMessage.append(",\n无法审批!");
        throw new BusinessException(sbErroMessage.toString());
      }
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  protected String[] checkLockPKs(DMDataVO[] vos)
    throws BusinessException
  {
    DMVO vo = new DMVO();
    vo.setChildrenVO(vos);

    String sHeadPkName = null;
    String sBodyPkName = "pk_delivdaypl";
    String sHeadPkNameInBody = null;
    String[] sHeadLockFieldsNames = (String[])null;
    String[] sBodyLockFieldsNames = { "pk_delivdaypl", "pkbillh", "pkbillb" };
    String[] sAllKeys = (String[])null;
    try {
      DmDMO dmdmo = new DmDMO();
      sAllKeys = dmdmo.getLockPKsArray(vo, sHeadPkName, sBodyPkName, sHeadPkNameInBody, sHeadLockFieldsNames, sBodyLockFieldsNames);

      lockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }
    return sAllKeys;
  }

  private String[] checkLockPKsForDelInterface(DMDataVO[] vos)
    throws BusinessException, BusinessException, NamingException
  {
    DMVO vo = new DMVO();
    vo.setChildrenVO(vos);

    String sHeadPkName = null;
    String sBodyPkName = "pk_delivdaypl";
    String sHeadPkNameInBody = null;
    String[] sHeadLockFieldsNames = (String[])null;
    String[] sBodyLockFieldsNames = { "pk_delivdaypl" };
    String[] sAllKeys = (String[])null;
    try {
      DmDMO dmdmo = new DmDMO();
      sAllKeys = dmdmo.getLockPKsArray(vo, sHeadPkName, sBodyPkName, sHeadPkNameInBody, sHeadLockFieldsNames, sBodyLockFieldsNames);

      lockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }
    return sAllKeys;
  }

  private void checkTotalRowCount(int iAllRowsCount)
    throws BusinessException
  {
    int All_Rows_Count = 3000;

    if (iAllRowsCount > All_Rows_Count)
      throw new BusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000105", null, new String[] {  })));
  }

  protected void checkTs(DMDataVO[] vos)
    throws BusinessException, BusinessException, NamingException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    String[] sTs = new String[vos.length];
    String[] sPK = new String[vos.length];
    for (int i = 0; i < vos.length; i++) {
      if (vos[i].getStatus() != 2) {
        sPK[i] = ((String)vos[i].getAttributeValue("pk_delivdaypl"));
        sTs[i] = ((String)vos[i].getAttributeValue("ts"));
      }
      else {
        return;
      }
    }

    Hashtable ht = null;
    try {
      DmDMO dmdmo = new DmDMO();
      ht = dmdmo.checkRowExitOrChanged(sPK, sTs, "dm_delivdaypl", "pk_delivdaypl", "ts");
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }
    int ooo = 0;
    StringBuffer sb = new StringBuffer();
    String sCode = new String();
    for (int i = 0; (i < sPK.length) && 
      (sPK[i] != null); )
    {
      Integer iCheck = new Integer(ht.get(sPK[i]).toString());
      if ((iCheck.intValue() == 2) || (iCheck.intValue() == 0)) {
        sCode = (String)vos[i].getAttributeValue("vdelivdayplcode");
        if (ooo != 0) {
          sb.append(NCLangResOnserver.getInstance().getStrByID("40140404", "UPPSCMCommon-000000") + sCode);
        }
        else
        {
          sb.append(sCode);
        }
        ooo++;
      }
      i++;
    }

    if (sb.toString().trim().length() > 0)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000106", null, new String[] { sb.toString() }));
  }

  public DMDataVO[] endDeliverydailyplan(DMDataVO[] vos)
    throws BusinessException
  {
    return end(vos, false);
  }

  /** @deprecated */
  private OutbillHHeaderVO[] filterDlvitemsForOut(OutbillHHeaderVO[] items) throws BusinessException
  {
    try
    {
      OutbillHHeaderVO[] filterItems = (OutbillHHeaderVO[])null;
      ArrayList filterList = new ArrayList();

      String[] itemPks = new String[items.length];
      for (int i = 0; i < items.length; i++) {
        itemPks[i] = ((String)items[i].getAttributeValue("pk_delivdaypl"));
      }
      DmFromIcDMO icDmo = new DmFromIcDMO();
      DMDataVO[] itemOutnums = icDmo.queryDeliv2OutSumShouldOutNum(itemPks);

      Hashtable itemHash = new Hashtable();
      for (int i = 0; i < itemOutnums.length; i++) {
        itemHash.put(itemOutnums[i].getAttributeValue("pk_delivbill_b"), itemOutnums[i].getAttributeValue("sumyf"));
      }
      for (int i = 0; i < items.length; i++) {
        UFDouble ufdSumShould = null;
        if (itemHash.get(itemPks[i]) != null) {
          ufdSumShould = new UFDouble(itemHash.get(itemPks[i]).toString());
        }
        else
          ufdSumShould = new UFDouble(0);
        UFDouble ufdOutnum = (UFDouble)items[i].getAttributeValue("doutnum");
        if (ufdOutnum == null)
          ufdOutnum = new UFDouble(0);
        if ((ufdSumShould != null) && (items[i].getAttributeValue("dnum") != null)) {
          UFDouble ufdInvnum = ((UFDouble)items[i].getAttributeValue("dnum")).sub(ufdSumShould);
          if (ufdInvnum.sub(ufdOutnum).doubleValue() > 0.0D) {
            items[i].setAttributeValue("dinvnum", ufdInvnum);
            filterList.add(items[i]);
          }

        }

      }

      if (filterList.size() == 0) {
        throw new Exception(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000086"));
      }

      filterItems = new OutbillHHeaderVO[filterList.size()];
      filterItems = (OutbillHHeaderVO[])filterList.toArray(new OutbillHHeaderVO[0]);
      return filterItems;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  /** @deprecated */
  private OutbillHHeaderVO[] filterDlvitemsForOutNoOutNum(OutbillHHeaderVO[] items)
    throws BusinessException
  {
    try
    {
      OutbillHHeaderVO[] filterItems = (OutbillHHeaderVO[])null;
      ArrayList filterList = new ArrayList();

      String[] itemPks = new String[items.length];
      for (int i = 0; i < items.length; i++) {
        itemPks[i] = ((String)items[i].getAttributeValue("pk_delivdaypl"));
      }
      DmFromIcDMO icDmo = new DmFromIcDMO();
      DMDataVO[] itemOutnums = icDmo.queryDeliv2OutSumShouldOutNumNoOutNum(itemPks);

      Hashtable itemHash = new Hashtable();
      for (int i = 0; i < itemOutnums.length; i++) {
        itemHash.put(itemOutnums[i].getAttributeValue("pk_delivbill_b"), itemOutnums[i].getAttributeValue("sumyf"));
      }
      for (int i = 0; i < items.length; i++) {
        UFDouble ufdSumShould = null;
        if (itemHash.get(itemPks[i]) != null) {
          ufdSumShould = new UFDouble(itemHash.get(itemPks[i]).toString());
        }
        else {
          ufdSumShould = new UFDouble(0);
        }

        if ((ufdSumShould != null) && (items[i].getAttributeValue("dnum") != null)) {
          UFDouble ufdInvnum = ((UFDouble)items[i].getAttributeValue("dnum")).sub(ufdSumShould);
          if (ufdInvnum.doubleValue() > 0.0D) {
            items[i].setAttributeValue("dinvnum", ufdInvnum);
            items[i].setAttributeValue("doutnum", null);
            filterList.add(items[i]);
          }

        }

      }

      if (filterList.size() == 0) {
        throw new Exception(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000086"));
      }

      filterItems = new OutbillHHeaderVO[filterList.size()];
      filterItems = (OutbillHHeaderVO[])filterList.toArray(new OutbillHHeaderVO[0]);
      return filterItems;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private BillCodeObjValueVO getBillCodeObjVO(CircularlyAccessibleValueObject vo)
  {
    BillCodeObjValueVO bcovo = new BillCodeObjValueVO();
    String[] names = vo.getAttributeNames();
    String[] values = new String[names.length];
    for (int j = 0; j < names.length; j++) {
      values[j] = (vo.getAttributeValue(names[j]) == null ? "" : vo.getAttributeValue(names[j]).toString());

      if (names[j].equals("pkcorp"))
      {
        names[j] = "公司";
      }
      else if (names[j].equals("pkinv")) {
        names[j] = "存货";
      }
      else if (names[j].equals("pksalecorp")) {
        names[j] = "公司";
      }
    }
    bcovo.setAttributeValue(names, values);
    return bcovo;
  }

  public String deliverydailyplan_getDataPowerSubSql(String sTableName, String sTableShowName, String sFieldName, String[] saCorp, String sUserId)
    throws BusinessException
  {
    String sNullSubSql = " 1=2 ";
    if (saCorp.length == 0) {
      return sNullSubSql;
    }

    CorpVO[] voaPower = (CorpVO[])null;
    try
    {
      voaPower = new UserBO().queryAllCorpsByUserPK(sUserId);
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }

    String[] saFinalCorp = (String[])null;
    if (voaPower != null) {
      HashMap hmPowerCorp = new HashMap();
      int iLen = voaPower.length;
      for (int i = 0; i < iLen; i++) {
        hmPowerCorp.put(voaPower[i].getPk_corp(), null);
      }

      ArrayList listFinalCorp = new ArrayList();
      int iParaLen = saCorp.length;
      for (int i = 0; i < iParaLen; i++) {
        if (hmPowerCorp.containsKey(saCorp[i])) {
          listFinalCorp.add(saCorp[i]);
        }
      }
      if (listFinalCorp.size() > 0) {
        saFinalCorp = (String[])listFinalCorp.toArray(new String[0]);
      }
    }

    if ((saFinalCorp == null) && (sFieldName != null)) {
      return sNullSubSql;
    }

    if ((saFinalCorp == null) && (sFieldName == null)) {
      return null;
    }

    String sReturn = null;
    String sDataPower = null;
    try {
      sDataPower = new DataPowerServiceBO().getSubSqlForMutilCorp(sTableName, sTableShowName, sUserId, saFinalCorp);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(e);
    }
    if ((sDataPower != null) && (sDataPower.trim().length() != 0)) {
      if (sFieldName != null) {
        sReturn = sFieldName + " IN " + sDataPower;
      }
      else
      {
        sReturn = sDataPower;
      }
    }
    return sReturn;
  }

  /** @deprecated */
  private double getDelivScale(DMDataVO[] vos) throws BusinessException
  {
    double scale = 100.0D;
    try
    {
      boolean isExcess = false;
      if (vos.length > 0) {
        SysInitDMO sysinitdmo = new SysInitDMO();
        UFBoolean ufbIsExcess = sysinitdmo.getParaBoolean(vos[0].getAttributeValue("pkparamcorp").toString(), "DM008");
        if (ufbIsExcess != null) {
          isExcess = ufbIsExcess.booleanValue();
        }
      }
      if ((isExcess) && 
        (vos.length > 0)) {
        SysInitDMO sysinitdmo = new SysInitDMO();
        UFDouble ufdExcess = sysinitdmo.getParaDbl(vos[0].getAttributeValue("pkparamcorp").toString(), "DM009");
        if (ufdExcess != null) {
          scale = ufdExcess.doubleValue() + 100.0D;
        }
      }

    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    return scale;
  }

  public OutbillHVO[] getOnhandnum(OutbillHHeaderVO[] items, String userid, UFBoolean ufbIsConsiderOfOutNum)
    throws BusinessException
  {
    OutbillHVO[] outbills = (OutbillHVO[])null;

    String pkStoreorg = null; String pkcorp = null; String pkinv = null;
    try
    {
      saveCheck(items);

      if (ufbIsConsiderOfOutNum == null)
        ufbIsConsiderOfOutNum = new UFBoolean(false);
      if (ufbIsConsiderOfOutNum.booleanValue())
      {
        items = filterDlvitemsForOut(items);
      }
      else
      {
        items = filterDlvitemsForOutNoOutNum(items);
      }

      ArrayList stores = null;
      ArrayList oneStoreorg = null;
      ArrayList pkStores = null;

      DeliverydailyplanDMO dmo = new DeliverydailyplanDMO();
      stores = dmo.findStoresByCalbody(items, userid);

      Hashtable htInvStore = new Hashtable();
      ArrayList alInvs = new ArrayList();
      Hashtable htInvIDs = new Hashtable();
      for (int w = 0; w < items.length; w++)
      {
        if (!htInvIDs.containsKey(items[w].getAttributeValue("pkinv"))) {
          alInvs.add(items[w].getAttributeValue("pkinv"));
          htInvIDs.put(items[w].getAttributeValue("pkinv"), "");
        }
      }

      htInvStore = queryInvProduceStoreByInvs(alInvs);

      outbills = new OutbillHVO[items.length];
      for (int i = 0; i < items.length; i++) {
        outbills[i] = new OutbillHVO();
        if (items[i] != null) {
          pkStoreorg = (String)items[i].getAttributeValue("pksendstoreorg");
          pkcorp = (String)items[i].getAttributeValue("pksalecorp");
          pkinv = (String)items[i].getAttributeValue("pkinv");
          if (pkStoreorg != null)
          {
            for (int j = 0; j < stores.size(); j++) {
              oneStoreorg = (ArrayList)stores.get(j);
              if ((oneStoreorg == null) || (oneStoreorg.get(0) == null) || 
                (!pkStoreorg.equals(oneStoreorg.get(0).toString().trim())))
                continue;
              pkStores = (ArrayList)oneStoreorg.get(1);
              break;
            }

          }

          DmFromIcDMO invOnhandDmo = new DmFromIcDMO();
          if (pkStores != null)
          {
            OutbillHItemVO[] outbillitems = new OutbillHItemVO[pkStores.size()];
            for (int j = 0; j < pkStores.size(); j++) {
              if (pkStores.get(j) != null) {
                outbillitems[j] = new OutbillHItemVO();
                outbillitems[j].setPk_delivdaypl((String)items[i].getAttributeValue("pk_delivdaypl"));
                outbillitems[j].setPkstroe(((String[])pkStores.get(j))[0]);
                outbillitems[j].setVstorename(((String[])pkStores.get(j))[1]);
                UFDouble ufdonhandnum = invOnhandDmo.getOnHandNum(pkcorp, pkStoreorg, ((String[])pkStores.get(j))[0], pkinv, (String)items[i].getAttributeValue("vfree1"), (String)items[i].getAttributeValue("vfree2"), (String)items[i].getAttributeValue("vfree3"), (String)items[i].getAttributeValue("vfree4"), (String)items[i].getAttributeValue("vfree5"), (String)items[i].getAttributeValue("vfree6"), (String)items[i].getAttributeValue("vfree7"), (String)items[i].getAttributeValue("vfree8"), (String)items[i].getAttributeValue("vfree9"), (String)items[i].getAttributeValue("vfree10"), (String)items[i].getAttributeValue("vbatchcode"), (String)items[i].getAttributeValue("pkassistmeasure"));

                outbillitems[j].setDonhandnum(ufdonhandnum);
              }
            }
            outbillitems = resortStore(items[i], outbillitems, htInvStore);

            outbills[i].setChildrenVO(outbillitems);
          }
          else
          {
            outbills[i].setChildrenVO(null);
          }

        }

        outbills[i].setParentVO(items[i]);
      }
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }

    return outbills;
  }

  public UFDate getOtherDateByAheadPeriod(String pksendtype, String pksendstoreorg, String pkarrivearea, UFDate senddate, UFDate arrivedate)
    throws BusinessException
  {
    try
    {
      DmDMO dmo = new DmDMO();
      UFDate retdate = dmo.getOtherDateByAheadPeriod(pksendtype, pksendstoreorg, pkarrivearea, senddate, arrivedate);
      return retdate;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  protected ParaRewriteVO[] getParaRewriteVO(DMDataVO[] vos, String dNumKey)
  {
    Vector v = new Vector();
    for (int i = 0; i < vos.length; i++) {
      if (vos[i].getAttributeValue("vbilltype").toString().trim().equals("30"))
        continue;
      if (vos[i].getAttributeValue("vbilltype").toString().trim().equals("4U"))
        continue;
      if (vos[i].getAttributeValue("vbilltype").toString().trim().equals("21")) {
        continue;
      }
      ParaRewriteVO prvo = new ParaRewriteVO();
      String[] cbodyid = { (String)vos[i].getAttributeValue("pkbillb") };
      String[] cheadid = { (String)vos[i].getAttributeValue("pkbillh") };

      UFDouble[] dOldNum = { new UFDouble(0) };

      UFDouble[] dNum = new UFDouble[1];
      if ((vos[i].getAttributeValue(dNumKey) != null) && (vos[i].getAttributeValue(dNumKey).toString().trim().length() > 0)) {
        dNum[0] = new UFDouble(vos[i].getAttributeValue(dNumKey).toString());
      }
      else {
        dNum[0] = new UFDouble(0);
      }
      prvo.setPk_corp(vos[i].getAttributeValue("pksalecorp").toString());
      prvo.setCBodyIdArray(cbodyid);
      prvo.setCHeadIdArray(cheadid);
      prvo.setDOldNumArray(dOldNum);
      prvo.setDNumArray(dNum);

      v.add(prvo);
    }

    if (v.size() == 0) {
      return null;
    }
    return (ParaRewriteVO[])v.toArray(new ParaRewriteVO[v.size()]);
  }

  public DMDataVO[] getSourceSpareNumAndStatus(DMDataVO[] vos)
    throws BusinessException
  {
    String[] sBids = new String[vos.length];
    ArrayList alSOBids = new ArrayList();
    ArrayList also = new ArrayList();

    for (int i = 0; i < vos.length; i++) {
      if (vos[i].getAttributeValue("vbilltype").equals("30")) {
        alSOBids.add((String)vos[i].getAttributeValue("pkbillb"));
        also.add(vos[i]);
      }
    }
    DMDataVO[] sovos = (DMDataVO[])also.toArray(new DMDataVO[0]);

    String[] sSOBids = (String[])alSOBids.toArray(new String[0]);

    DMDataVO[] retvos = vos;
    try
    {
      if ((sovos != null) && (sovos.length > 0)) {
        DmFromSoDMO soDmo = new DmFromSoDMO();

        UFDouble[] soSpares = soDmo.getSoSpareNum(sovos);
        UFBoolean[] soClosed = soDmo.getIsBillsClosed(sSOBids);

        for (int i = 0; i < sovos.length; i++) {
          sovos[i].setAttributeValue("ndelivernum", soSpares[i]);
          sovos[i].setAttributeValue("orderstatus", soClosed[i]);
        }

      }

    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }

    return retvos;
  }

  public UFBoolean[] getSupplyFlag(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      DmFromSoDMO dmo = new DmFromSoDMO();
      UFBoolean[] bFlags = dmo.getSupplyFlag(vos);
      return bFlags;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private void insertTmpRecords(String strTmpTabName, String sSourceRecordSQL)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into ");
    sql.append(strTmpTabName);
    sql.append(" (");
    sql.append("pkstoreorg");
    sql.append(") ");

    sql.append(sSourceRecordSQL);

    SmartDMO smartDMO = new SmartDMO();
    smartDMO.executeUpdate(sql.toString(), new ArrayList(), new ArrayList());
  }

  protected boolean isModuleStarted(String sCorpID, String sModuleCode)
    throws BusinessException
  {
    boolean isUsed = false;
    try {
      RewriteDMO dmo = new RewriteDMO();
      isUsed = dmo.isModuleStarted(sCorpID, sModuleCode);
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    return isUsed;
  }

  private DMDataVO[] pushsavePrepareDailyPlanFromPO(DMDataVO[] vos)
    throws BusinessException
  {
    for (int i = 0; i < vos.length; i++)
    {
      Object obj = vos[i].getAttributeValue("pkorderplanid");
      if ((obj == null) || (obj.toString().length() <= 0)) {
        vos[i].setAttributeValue("pkdeststoreorg", vos[i].getAttributeValue("pkdeststoreorg2"));
      }

      vos[i].setAttributeValue("pkcorp", vos[i].getAttributeValue("pkarrivecorp"));
    }

    String[][] sItemKey = { { "creceiptcorpid", "csendcorpid" }, { "pkdeststore", "pksendstore" }, { "pkarrivecorp", "pksalecorp" }, { "pkarriveaddress", "pksendaddress" }, { "pkarrivearea", "pksendarea" }, { "vdestaddress", "vsendaddr" }, { "pkdeststoreorg", "pksendstoreorg" } };

    for (int i = 0; i < vos.length; i++)
    {
      vos[i].setAttributeValue("plandate", new UFDate(new Date()));
      vos[i].setAttributeValue("ordsndate", new UFDate(new Date()));

      if (vos[i].getAttributeValue("borderreturn").equals("Y"))
      {
        Object obj = vos[i].getAttributeValue("dnum");
        if ((obj != null) && (obj.toString().trim().length() > 0)) {
          vos[i].setAttributeValue("dnum", new UFDouble(obj.toString()).abs());

          if ((vos[i].getAttributeValue("dweight") != null) && (vos[i].getAttributeValue("dweight").toString().trim().length() != 0)) {
            vos[i].setAttributeValue("dweight", new UFDouble(vos[i].getAttributeValue("dweight").toString()).abs());
          }
          if ((vos[i].getAttributeValue("dvolumn") != null) && (vos[i].getAttributeValue("dvolumn").toString().trim().length() != 0)) {
            vos[i].setAttributeValue("dvolumn", new UFDouble(vos[i].getAttributeValue("dvolumn").toString()).abs());
          }
        }
        obj = vos[i].getAttributeValue("dassistnum");
        if ((obj != null) && (obj.toString().trim().length() > 0)) {
          vos[i].setAttributeValue("dassistnum", new UFDouble(obj.toString()).abs());
        }

        for (int j = 0; j < sItemKey.length; j++) {
          obj = vos[i].getAttributeValue(sItemKey[j][0]);
          vos[i].setAttributeValue(sItemKey[j][0], vos[i].getAttributeValue(sItemKey[j][1]));
          vos[i].setAttributeValue(sItemKey[j][1], obj);
        }

      }

      Object obj = vos[i].getAttributeValue("dnum");
      UFDouble ufdNum = new UFDouble(obj == null ? "0" : obj.toString());
      if (ufdNum.doubleValue() == 0.0D) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000087"));
      }

      obj = vos[i].getAttributeValue("dmoney");
      UFDouble ufdMoney = new UFDouble(obj == null ? "0" : obj.toString());
      vos[i].setAttributeValue("dunitprice", ufdMoney.div(ufdNum));
    }

    return vos;
  }

  private DMDataVO[] pushsavePrepareDailyPlan(DMDataVO[] vos)
    throws BusinessException
  {
    String vbilltype = (String)vos[0].getAttributeValue("vbilltype");

    if (vbilltype.equals("30")) {
      for (int i = 0; i < vos.length; i++)
      {
        if ((vos[i].getAttributeValue("pkdeststoreorg") == null) || (vos[i].getAttributeValue("pkdeststoreorg").toString().trim().length() <= 0))
          continue;
        vos[i].setAttributeValue("pksendstoreorg", vos[i].getAttributeValue("pkdeststoreorg"));
        vos[i].removeAttributeName("pkdeststoreorg");

        if ((vos[i].getAttributeValue("pkdeststore") == null) || (vos[i].getAttributeValue("pkdeststore").toString().trim().length() <= 0))
          continue;
        vos[i].setAttributeValue("pksendstore", vos[i].getAttributeValue("pkdeststore"));
        vos[i].removeAttributeName("pkdeststore");
      }

    }

    if (!vbilltype.equals("21")) {
      SuperVOUtil.execFormulaWithVOs(vos, new String[] { "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)", "pksendaddress->getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore)", "pksendaddress->iif(pksendaddress==null,getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),pksendaddress)", "vsendaddr->getColValue(bd_stordoc,storaddr,pk_stordoc,pksendstore)", "vsendaddr->iif(vsendaddr==null,getColValue(bd_calbody,area,pk_calbody,pksendstoreorg),vsendaddr)" }, null);
    }
    else
    {
      pushsavePrepareDailyPlanFromPO(vos);
    }

    for (int i = 0; i < vos.length; i++)
    {
      Object onum = vos[i].getAttributeValue("dnum");
      UFDouble dnum = onum == null ? new UFDouble(0) : new UFDouble(onum.toString());
      vos[i].setAttributeValue("ndelivernum", dnum);
      vos[i].setAttributeValue("nfeedbacknum", dnum);
      vos[i].setAttributeValue("orderstatus", new UFBoolean(true));
      if (!vbilltype.equals("21"))
        vos[i].setAttributeValue("pkcorp", vos[i].getAttributeValue("pksalecorp"));
      vos[i].setStatus(2);
      vos[i].setAttributeValue("snddate", vos[i].getAttributeValue("ordsndate"));
      vos[i].setAttributeValue("iplanstatus", new Integer(DailyPlanStatus.Free));
      vos[i].setAttributeValue("active", "pushsave");
    }
    return vos;
  }

  public DMDataVO[] pushSave(DMVO vo)
    throws BusinessException
  {
    Hashtable htCorpToCodes = new Hashtable();
    try {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(vo);
      long s = System.currentTimeMillis();
      DMDataVO[] vosOrig = (DMDataVO[])vo.getChildrenVO();
      if ((vosOrig == null) || (vosOrig.length == 0)) {
        return null;
      }
      showMethodTime("pushsavestart", s);
      String vbilltype = (String)vosOrig[0].getAttributeValue("vbilltype");

      DMDataVO[] vos = pushSaveDataChoose(vosOrig);
      showMethodTime("待保存数据筛选", s);
      if ((vos == null) || (vos.length == 0)) {
        return null;
      }

      pushsavePrepareDailyPlan(vos);
      showMethodTime("保存前准备数据", s);

      boolean bQryAllDeliorg = false;
      if (vos.length >= 30) {
        bQryAllDeliorg = true;
      }
      HashMap hmDelivorg = null;
      if (bQryAllDeliorg) {
    	if ("5I".equals(vbilltype)) {
    		hmDelivorg = queryAllDelivorgbyvbilltype();
		}else{
            hmDelivorg = queryAllDelivorg();
		}
      }
      showMethodTime("查询所有发运组织的属性", s);

      Hashtable hmRowToCorp = new Hashtable();
      Hashtable hmCorpToCount = new Hashtable();
      for (int i = 0; i < vos.length; i++)
      {
        String sCorpID4BillCode = null;
        int ibizattribute = 1;
        String spkstoreorg;
        if ((vbilltype.equals("21")) && (!vos[i].getAttributeValue("borderreturn").toString().equals("Y")))
        {
          sCorpID4BillCode = (String)vos[i].getAttributeValue("pkarrivecorp");
          ibizattribute = 2;
          spkstoreorg = (String)vos[i].getAttributeValue("pkdeststoreorg");
        }
        else {
          sCorpID4BillCode = (String)vos[i].getAttributeValue("pksalecorp");
          spkstoreorg = (String)vos[i].getAttributeValue("pksendstoreorg");
        }
        hmRowToCorp.put(i, sCorpID4BillCode);
        Object oCorpcount = hmCorpToCount.get(sCorpID4BillCode) == null ? "0" : hmCorpToCount.get(sCorpID4BillCode);
        int iCorpcount = Integer.parseInt(oCorpcount.toString());
        hmCorpToCount.put(sCorpID4BillCode, iCorpcount + 1);

        String vBillcode = (String)vos[i].getAttributeValue("vdelivdayplcode");
        if ((vBillcode == null) || (vBillcode.length() == 0)) {
          vBillcode = null;
        }

        if ((spkstoreorg == null) || (spkstoreorg.trim().length() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000088"));
        }
        String pk_corp = vos[i].getPk_corp();
        String sDefaultDelivorg = null;
        if (getParentCorpCode(pk_corp).equals("10395"))
        {
          sDefaultDelivorg = String.valueOf(vos[i].getAttributeValue("pkdelivorg"));
        }

        if ((sDefaultDelivorg == null) || (sDefaultDelivorg.equals("")) || (sDefaultDelivorg.equalsIgnoreCase("null")))
        {
          if (bQryAllDeliorg) {
            sDefaultDelivorg = queryDefaultDelivorgFromAll(sCorpID4BillCode, spkstoreorg, ibizattribute, hmDelivorg);
          }
          else {
        	if ("5I".equals(vbilltype)) {
                sDefaultDelivorg = queryDefaultDelivorgby5I(sCorpID4BillCode, spkstoreorg, ibizattribute);
			}else{
                sDefaultDelivorg = queryDefaultDelivorg(sCorpID4BillCode, spkstoreorg, ibizattribute);
			}
          }
          if ((sDefaultDelivorg == null) && (!vbilltype.equals("21")) && (!vbilltype.equals("30"))) {
            spkstoreorg = (String)vos[i].getAttributeValue("pkdeststoreorg");
            if ((spkstoreorg == null) || (spkstoreorg.trim().length() <= 0)) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000088"));
            }

            ibizattribute = 2;
            if (bQryAllDeliorg) {
              sDefaultDelivorg = queryDefaultDelivorgFromAll(sCorpID4BillCode, spkstoreorg, ibizattribute, hmDelivorg);
            }
            else {
            	if ("5I".equals(vbilltype)) {
                    sDefaultDelivorg = queryDefaultDelivorgby5I(sCorpID4BillCode, spkstoreorg, ibizattribute);
				}else{
		              sDefaultDelivorg = queryDefaultDelivorg(sCorpID4BillCode, spkstoreorg, ibizattribute);
				}
            }
          }
        }

        if ((sDefaultDelivorg == null) || (sDefaultDelivorg.trim().length() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000089"));
        }
        vos[i].setAttributeValue("pkdelivorg", sDefaultDelivorg);
      }
      showMethodTime("寻找发运组织", s);

      ArrayList alcorpids = UsefulTools.hashtableKeysToArrayList(hmCorpToCount);
      String[] scorpids = (String[])alcorpids.toArray(new String[0]);
      for (int i = 0; i < scorpids.length; i++) {
        BillCodeObjValueVO codevo = new BillCodeObjValueVO();
        codevo.setAttributeValue("公司", scorpids[i]);
        BillcodeGenerater ruleBO = new BillcodeGenerater();
        String[] vBillcodes = (String[])null;
        Object oCorpcount = hmCorpToCount.get(scorpids[i]) == null ? "0" : hmCorpToCount.get(scorpids[i]);
        int iCorpcount = Integer.parseInt(oCorpcount.toString());
        vBillcodes = ruleBO.getBatchBillCodes(DMBillTypeConst.m_delivDelivDayPl, scorpids[i], null, codevo, iCorpcount);
        htCorpToCodes.put(scorpids[i], vBillcodes);
        int iCodeCount = 0;
        for (int j = 0; j < vos.length; j++) {
          if (hmRowToCorp.get(j).equals(scorpids[i])) {
            vos[j].setAttributeValue("vdelivdayplcode", vBillcodes[iCodeCount]);
          }
          iCodeCount++;
        }
      }
      showMethodTime("批申请单据号", s);

      if (!vbilltype.equals("21")) {
        modifyATP(vos);
      }
      showMethodTime("修改可用量", s);

      DMDataVO[] retVOs = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);
      showMethodTime("日计划保存", s);

      rewriteDailyPlanNum2Order(vos);
      showMethodTime("回写订单的日计划数量", s);

      showMethodTime("pushsaveend", s);

      DMATP dmatp = new DMATP();
      DMVO dvo = new DMVO();
      dvo.setChildrenVO(vos);
      dmatp.checkAtpInstantly(dvo, null);
      return retVOs;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private DMDataVO[] pushSaveDataChoose(DMDataVO[] vosOrig)
  {
    String vbilltype = (String)vosOrig[0].getAttributeValue("vbilltype");

    SuperVOUtil.execFormulaWithVOs(vosOrig, new String[] { "pkinvbas->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,pkinv)", "unitweight->getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,pkinvbas)", "unitvolume->getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,pkinvbas)", "dweight->toNumber(unitweight)*toNumber(dnum)", "dvolumn->toNumber(unitvolume)*toNumber(dnum)", "laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,pkinvbas)", "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,pkinvbas)" }, null);

    ArrayList alChosenVOs = new ArrayList();
    for (int i = 0; i < vosOrig.length; i++)
    {
      Object oissendarranged = vosOrig[i].getAttributeValue("bdeliver");
      if (vbilltype.equals("30")) {
        if ((oissendarranged == null) || (!oissendarranged.toString().equals("Y")))
          return null;
      }
      else if ((!vbilltype.equals("21")) && (
          (oissendarranged == null) || (!oissendarranged.toString().equals("Y"))))
        {
          continue;
        }
      Object obj = vosOrig[i].getAttributeValue("bdericttrans");
      if ((obj != null) && (obj.toString().equals("Y"))) {
        continue;
      }
      Object olaborflag = vosOrig[i].getAttributeValue("laborflag");
      Object odiscountflag = vosOrig[i].getAttributeValue("discountflag");
      UFBoolean laborflag = olaborflag == null ? null : new UFBoolean(olaborflag.toString());
      UFBoolean discountflag = odiscountflag == null ? null : new UFBoolean(odiscountflag.toString());
      if (((laborflag != null) && (laborflag.booleanValue())) || ((discountflag != null) && (discountflag.booleanValue()))) {
        continue;
      }
      Object oosflag = vosOrig[i].getAttributeValue("boosflag");
      Object supplyflag = vosOrig[i].getAttributeValue("bsupplyflag");
      UFBoolean boosflag = oosflag == null ? new UFBoolean(false) : new UFBoolean(oosflag.toString());
      UFBoolean bsupplyflag = supplyflag == null ? new UFBoolean(false) : new UFBoolean(supplyflag.toString());
      SCMEnv.info("boosflag = " + boosflag + " : bsupplyflag = " + bsupplyflag);
      if ((boosflag.booleanValue()) || (bsupplyflag.booleanValue())) {
        continue;
      }
      alChosenVOs.add(vosOrig[i]);
    }
    DMDataVO[] vos = (DMDataVO[])alChosenVOs.toArray(new DMDataVO[0]);
    return vos;
  }

  private DMDataVO[] pushSavebak(DMVO vo)
    throws BusinessException
  {
    Vector v = new Vector();
    DMDataVO[] vos = (DMDataVO[])vo.getChildrenVO();
    if ((vos == null) || (vos.length == 0)) {
      return null;
    }

    if (vos[0].getAttributeValue("vbilltype").equals("21"))
      pushsavePrepareDailyPlanFromPO(vos);
    try
    {
      DeliverydailyplanDMO planDMO = new DeliverydailyplanDMO();
      for (int i = 0; i < vos.length; i++) {
        DMDataVO[] infovo = planDMO.getInfoForPushSave(vos[i]);

        Object oissendarranged = vos[i].getAttributeValue("bdeliver");

        String vbilltype = (String)vos[i].getAttributeValue("vbilltype");
        if (vbilltype.equals("30")) {
          if ((oissendarranged == null) || (!oissendarranged.toString().equals("Y")))
            return null;
        }
        else if ((!vbilltype.equals("21")) && (
            (oissendarranged == null) || (!oissendarranged.toString().equals("Y"))))
          {
            continue;
          }
        Object obj = vos[i].getAttributeValue("bdericttrans");
        if ((obj != null) && (obj.toString().equals("Y")))
        {
          continue;
        }
        Object olaborflag = infovo[0].getAttributeValue("laborflag");
        Object odiscountflag = infovo[0].getAttributeValue("discountflag");
        UFBoolean laborflag = olaborflag == null ? null : new UFBoolean(olaborflag.toString());
        UFBoolean discountflag = odiscountflag == null ? null : new UFBoolean(odiscountflag.toString());
        if (((laborflag != null) && (laborflag.booleanValue())) || ((discountflag != null) && (discountflag.booleanValue())))
        {
          continue;
        }
        Object oosflag = vos[i].getAttributeValue("boosflag");
        Object supplyflag = vos[i].getAttributeValue("bsupplyflag");
        UFBoolean boosflag = oosflag == null ? new UFBoolean(false) : new UFBoolean(oosflag.toString());
        UFBoolean bsupplyflag = supplyflag == null ? new UFBoolean(false) : new UFBoolean(supplyflag.toString());
        SCMEnv.info("boosflag = " + boosflag + " : bsupplyflag = " + bsupplyflag);
        if ((boosflag.booleanValue()) || (bsupplyflag.booleanValue()))
        {
          continue;
        }

        BillCodeObjValueVO codevo = new BillCodeObjValueVO();
        String sCorpID4BillCode = null;
        int ibizattribute = 1;
        String spkstoreorg;
        if (vbilltype.equals("21"))
        {
          if (vos[i].getAttributeValue("borderreturn").toString().equals("Y")) {
            sCorpID4BillCode = (String)vos[i].getAttributeValue("pksalecorp");
            spkstoreorg = (String)vos[i].getAttributeValue("pksendstoreorg");
          }
          else {
            sCorpID4BillCode = (String)vos[i].getAttributeValue("pkarrivecorp");
            ibizattribute = 2;
            spkstoreorg = (String)vos[i].getAttributeValue("pkdeststoreorg");
          }
        }
        else {
          sCorpID4BillCode = (String)vos[i].getAttributeValue("pksalecorp");
          spkstoreorg = (String)vos[i].getAttributeValue("pksendstoreorg");
        }
        codevo.setAttributeValue("公司", sCorpID4BillCode);

        String vBillcode = (String)vos[i].getAttributeValue("vdelivdayplcode");
        if ((vBillcode == null) || (vBillcode.length() == 0)) {
          vBillcode = null;
        }
        BillcodeGenerater ruleBO = new BillcodeGenerater();
        vBillcode = ruleBO.getBillCode(DMBillTypeConst.m_delivDelivDayPl, sCorpID4BillCode, vBillcode, codevo);

        Object onum = vos[i].getAttributeValue("dnum");
        UFDouble dnum = onum == null ? new UFDouble(0) : new UFDouble(onum.toString());
        vos[i].setAttributeValue("ndelivernum", dnum);
        vos[i].setAttributeValue("nfeedbacknum", dnum);

        Object ounitweight = infovo[0].getAttributeValue("unitweight");
        UFDouble dunitweight = ounitweight == null ? new UFDouble(0) : new UFDouble(ounitweight.toString());
        Object ounitvolume = infovo[0].getAttributeValue("unitvolume");
        UFDouble dunitvolume = ounitvolume == null ? new UFDouble(0) : new UFDouble(ounitvolume.toString());
        vos[i].setAttributeValue("dweight", dnum.multiply(dunitweight));
        vos[i].setAttributeValue("dvolumn", dnum.multiply(dunitvolume));
        vos[i].setAttributeValue("vdelivdayplcode", vBillcode);
        vos[i].setAttributeValue("orderstatus", new UFBoolean(true));
        vos[i].setAttributeValue("snddate", vos[i].getAttributeValue("ordsndate"));
        vos[i].setAttributeValue("iplanstatus", new Integer(DailyPlanStatus.Free));

        if (!vbilltype.equals("21")) {
          vos[i].setAttributeValue("pkcorp", vos[i].getAttributeValue("pksalecorp"));
        }
        vos[i].setStatus(2);

        if (vos[i].getAttributeValue("vbilltype").equals("30"))
        {
          if ((vos[i].getAttributeValue("pkdeststoreorg") != null) && (vos[i].getAttributeValue("pkdeststoreorg").toString().trim().length() > 0))
          {
            vos[i].setAttributeValue("pksendstoreorg", vos[i].getAttributeValue("pkdeststoreorg"));
            vos[i].removeAttributeName("pkdeststoreorg");

            if ((vos[i].getAttributeValue("pkdeststore") != null) && (vos[i].getAttributeValue("pkdeststore").toString().trim().length() > 0))
            {
              vos[i].setAttributeValue("pksendstore", vos[i].getAttributeValue("pkdeststore"));
              vos[i].removeAttributeName("pkdeststore");
            }
          }
        }

        if (!vos[0].getAttributeValue("vbilltype").equals("21")) {
          SuperVOUtil.execFormulaWithVOs(new CircularlyAccessibleValueObject[] { vos[i] }, new String[] { "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)", "pksendaddress->getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore)", "pksendaddress->iif(pksendaddress==null,getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),pksendaddress)", "vsendaddr->getColValue(bd_stordoc,storaddr,pk_stordoc,pksendstore)", "vsendaddr->iif(vsendaddr==null,getColValue(bd_calbody,area,pk_calbody,pksendstoreorg),vsendaddr)" }, null);
        }

        if ((spkstoreorg == null) || (spkstoreorg.trim().length() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000088"));
        }

        String sDefaultDelivorg = queryDefaultDelivorg(sCorpID4BillCode, spkstoreorg, ibizattribute);

        if ((sDefaultDelivorg == null) && (!vbilltype.equals("21")) && (!vbilltype.equals("30"))) {
          spkstoreorg = (String)vos[i].getAttributeValue("pkdeststoreorg");

          if ((spkstoreorg == null) || (spkstoreorg.trim().length() <= 0)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000088"));
          }

          ibizattribute = 2;
          sDefaultDelivorg = queryDefaultDelivorg(sCorpID4BillCode, spkstoreorg, ibizattribute);
        }

        if ((sDefaultDelivorg == null) || (sDefaultDelivorg.trim().length() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000089"));
        }
        vos[i].setAttributeValue("pkdelivorg", sDefaultDelivorg);

        v.add(vos[i]);
      }

      if (v.size() == 0) {
        System.out.print("Out due to v.size()==0");
        return null;
      }

      DMDataVO[] dailyPlanVOs = new DMDataVO[v.size()];
      v.copyInto(dailyPlanVOs);

      if (!dailyPlanVOs[0].getAttributeValue("vbilltype").equals("21")) {
        modifyATP(dailyPlanVOs);
      }

      DMDataVO[] retVOs = save(dailyPlanVOs, new ValueRangeHashtableDeliverydailyplan(), true);

      rewriteDailyPlanNum2Order(dailyPlanVOs);

      DMATP dmatp = new DMATP();
      DMVO dvo = new DMVO();
      dvo.setChildrenVO(dailyPlanVOs);
      dmatp.checkAtpInstantly(dvo, null);

      return retVOs;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public DMDataVO[] queryDeliverydailyplan(DMDataVO voCondNormal, ConditionVO[] voaCondDefined)
    throws BusinessException
  {
    try
    {
      ArrayList listDefinedVO = new ArrayList();
      StringBuffer sbWhereClause = new StringBuffer();

      String sbilltype = null;
      ConditionVO bbillrowenddeliv = null;
      ConditionVO bbillrowclose = null;
      ConditionVO bbillrowendout = null;

      if (voaCondDefined != null) {
        sbWhereClause.append("(");

        for (int i = 0; i < voaCondDefined.length; i++) {
          if (voaCondDefined[i].getFieldCode().equals("bd_delivorg.pk_delivorg")) {
            listDefinedVO.add(voaCondDefined[i]);
          }
          else if (voaCondDefined[i].getFieldCode().equals("vinvcl")) {
            sbWhereClause.append("  dm_delivdaypl.pkinv IN (SELECT pk_invmandoc FROM bd_invmandoc LEFT OUTER JOIN bd_invbasdoc ON  ").append("bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc LEFT OUTER JOIN bd_invcl ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl ").append("WHERE bd_invcl.invclasscode LIKE '").append(voaCondDefined[i].getValue()).append("%') ").append("AND ");
          }
          else if (voaCondDefined[i].getFieldCode().equals("bhasgendelivbill")) {
            if (voaCondDefined[i].getValue().equals("YES")) {
              sbWhereClause.append("  ( exists (SELECT 1 FROM dm_delivbill_b WHERE dm_delivbill_b.dr = 0 and dm_delivbill_b.pkdayplan=dm_delivdaypl.pk_delivdaypl)) ").append(" AND ");
            }
            else if (voaCondDefined[i].getValue().equals("NOT")) {
              sbWhereClause.append("  ( not exists (SELECT 1 FROM dm_delivbill_b WHERE dm_delivbill_b.dr = 0 and dm_delivbill_b.pkdayplan=dm_delivdaypl.pk_delivdaypl)) ").append("AND ");
            }

          }
          else if (voaCondDefined[i].getFieldCode().equals("bhasgenoutbill")) {
            if (voaCondDefined[i].getValue().equals("YES")) {
              sbWhereClause.append("  ( exists (SELECT 1 FROM ic_general_b ic_b WHERE ic_b.dr = 0 and ic_b.csourcebillbid=dm_delivdaypl.pk_delivdaypl and ic_b.csourcetype='7D')) ").append(" AND dm_delivdaypl.vbilltype<>'21' and ");
            }
            else if (voaCondDefined[i].getValue().equals("NOT")) {
              sbWhereClause.append("  (  not exists (SELECT 1 FROM ic_general_b ic_b WHERE ic_b.dr = 0 and ic_b.csourcebillbid=dm_delivdaypl.pk_delivdaypl and ic_b.csourcetype='7D')) ").append(" AND dm_delivdaypl.vbilltype<>'21' and  ");
            }

          }
          else if (voaCondDefined[i].getFieldCode().equals("boutend")) {
            if (voaCondDefined[i].getValue().equals("YES")) {
              sbWhereClause.append("  (dm_delivdaypl.dnum <= dm_delivdaypl.doutnum AND dm_delivdaypl.iplanstatus <> 2 )").append(" AND dm_delivdaypl.vbilltype<>'21' and  ");
            }
            else if (voaCondDefined[i].getValue().equals("NOT")) {
              sbWhereClause.append("  (dm_delivdaypl.dnum > isnull(dm_delivdaypl.doutnum, 0) AND dm_delivdaypl.iplanstatus <> 2 )").append(" AND dm_delivdaypl.vbilltype<>'21' and  ");
            }

          }
          else if (voaCondDefined[i].getFieldCode().startsWith("dm_delivdaypl.pkinv")) {
            voaCondDefined[i].setFieldCode("dm_delivdaypl.pkinv");
            listDefinedVO.add(voaCondDefined[i]);
          }
          else if (voaCondDefined[i].getFieldCode().startsWith("dm_delivdaypl.pkcust")) {
            voaCondDefined[i].setFieldCode("dm_delivdaypl.pkcust");
            listDefinedVO.add(voaCondDefined[i]);
          }
          else if (voaCondDefined[i].getFieldCode().equals("dm_delivdaypl.vbilltype")) {
            sbilltype = voaCondDefined[i].getValue();
            if (!sbilltype.equals("")) {
              listDefinedVO.add(voaCondDefined[i]);
            }
            else {
              sbilltype = null;
            }
          }
          else if (voaCondDefined[i].getFieldCode().equals("bbillrowclose")) {
            if (!voaCondDefined[i].getValue().equals("")) {
              bbillrowclose = voaCondDefined[i];
            }
          }
          else if (voaCondDefined[i].getFieldCode().equals("bbillrowenddeliv")) {
            if (!voaCondDefined[i].getValue().equals("")) {
              bbillrowenddeliv = voaCondDefined[i];
            }
          }
          else if (voaCondDefined[i].getFieldCode().equals("bbillrowendout")) {
            if (!voaCondDefined[i].getValue().equals("")) {
              bbillrowendout = voaCondDefined[i];
            }
          }
          else if ((voaCondDefined[i].getFieldCode().equals("dm_delivdaypl.borderreturn")) && (!voaCondDefined[i].getValue().equals("")))
          {
            voaCondDefined[i].setValue(" '" + (voaCondDefined[i].getValue().equals("YES") ? "Y" : "N") + "' and dm_delivdaypl.vbilltype='21' ) ");

            voaCondDefined[i].setFieldCode("(dm_delivdaypl.borderreturn");
            voaCondDefined[i].setDataType(1);
            listDefinedVO.add(voaCondDefined[i]);
          }
          else {
            listDefinedVO.add(voaCondDefined[i]);
          }

        }

        ConditionVO[] voaFiltedDefined = (ConditionVO[])listDefinedVO.toArray(new ConditionVO[listDefinedVO.size()]);

        String sTmp = voaFiltedDefined[0].getWhereSQL(voaFiltedDefined);

        if (DataTypeTool.getString_Trim0LenAsNull(sTmp) != null) {
          sbWhereClause.append(sTmp);
        }
        else {
          sbWhereClause.append(" 1=1 ");
        }

        sbWhereClause.append(") ");
      }
      else {
        sbWhereClause.append(" 1=1 ");
      }

      ArrayList listDeputCorpPK = (ArrayList)voCondNormal.getAttributeValue("DeputCorpPKs");
      if ((listDeputCorpPK != null) && 
        (listDeputCorpPK.size() != 0))
      {
        ClientLink linkInfoClient = (ClientLink)voCondNormal.getAttributeValue("ClientLink");
        String sDataPower = deliverydailyplan_getDataPowerSubSql("bd_calbody", "库存组织", "(case when dm_delivdaypl.vbilltype='21' and dm_delivdaypl.borderreturn='N' then dm_delivdaypl.pkdeststoreorg else  dm_delivdaypl.pksendstoreorg end) ", (String[])listDeputCorpPK.toArray(new String[listDeputCorpPK.size()]), linkInfoClient.getUser());

        if (sDataPower != null) {
          sbWhereClause.append(" AND " + sDataPower);
        }

      }

      Integer iPlanstatus = (Integer)voCondNormal.getAttributeValue("iplanstatus");
      if (iPlanstatus != null) {
        sbWhereClause.append(" AND dm_delivdaypl.iplanstatus=" + iPlanstatus);
      }

      if (voCondNormal.getAttributeValue("pk_delivdaypls") != null) {
        sbWhereClause.append(" AND ").append(new DMDataVO().getStrPKs("pk_delivdaypl", (String[])voCondNormal.getAttributeValue("pk_delivdaypls")));
      }

      DMVO dmvo = new DMVO();
      if ((bbillrowenddeliv == null) && (bbillrowclose == null) && (bbillrowendout == null)) {
        DMDataVO[] dailyPlans = query2(sbWhereClause.toString(), null);
        dmvo.appendBodyVO(dailyPlans);
      }
      else {
        if ((sbilltype == null) || (sbilltype.equals("30"))) {
          StringBuffer sb = new StringBuffer();
          if (bbillrowenddeliv != null) {
            String s = bbillrowenddeliv.getValue().equals("YES") ? "Y" : "N";
            sb.append(" inner join so_saleexecute so_exec on so_exec.dr=0 and so_exec.csale_bid=dm_delivdaypl.pkbillb ").append(" and so_exec.bifreceiptfinish='" + s + "' ");
          }

          if (bbillrowendout != null) {
            if (sb.length() == 0) {
              sb.append(" inner join so_saleexecute so_exec on so_exec.dr=0 and so_exec.csale_bid=dm_delivdaypl.pkbillb  ");
            }
            String s = bbillrowendout.getValue().equals("YES") ? "Y" : "N";

            sb.append(" and so_exec.bifinventoryfinish='" + s + "' ");
          }

          if (bbillrowclose != null) {
            sb.append("  inner join so_saleorder_b so_b on so_b.dr=0 and so_b.corder_bid=dm_delivdaypl.pkbillb  ").append(" and so_b.frowstatus" + (bbillrowclose.getValue().equals("YES") ? "=" : "<>") + "6 ");
          }

          DMDataVO[] dailyPlans = query2(sbWhereClause.toString(), sb.toString());
          dmvo.appendBodyVO(dailyPlans);
        }

        if ((sbilltype == null) || (sbilltype.equals("21"))) {
          StringBuffer sb = new StringBuffer();
          sb.append(" inner join po_order_b po_b on po_b.dr=0 and po_b.corder_bid=dm_delivdaypl.pkbillb ");
          if (bbillrowenddeliv != null) {
            sb.append(" and po_b.nordernum" + (bbillrowenddeliv.getValue().equals("YES") ? "=" : "<>") + "po_b.naccumdayplnum  ");
          }

          if (bbillrowclose != null) {
            sb.append(" and po_b.iisactive " + (bbillrowclose.getValue().equals("YES") ? " in " : " not in ")).append("(1,2) ");
          }

          if (bbillrowendout == null) {
            DMDataVO[] dailyPlans = query2(sbWhereClause.toString(), sb.toString());
            dmvo.appendBodyVO(dailyPlans);
          }
        }

        if ((sbilltype == null) || ((!sbilltype.equals("30")) && (!sbilltype.equals("21")))) {
          StringBuffer sb = new StringBuffer();
          sb.append(" inner join to_bill_b to_b on to_b.dr=0 and to_b.cbill_bid=dm_delivdaypl.pkbillb ");
          if (bbillrowenddeliv != null) {
            String s = bbillrowenddeliv.getValue().equals("YES") ? "Y" : "N";
            sb.append(" and to_b.bdelivendflag='" + s + "' ");
          }
          if (bbillrowendout != null) {
            String s = bbillrowendout.getValue().equals("YES") ? "Y" : "N";
            sb.append(" and to_b.boutendflag='" + s + "' ");
          }

          if (bbillrowclose != null) {
            sb.append(" and to_b.frowstatuflag" + (bbillrowclose.getValue().equals("YES") ? "=" : "<>") + "7 ");
          }

          DMDataVO[] dailyPlans = query2(sbWhereClause.toString(), sb.toString());
          dmvo.appendBodyVO(dailyPlans);
        }
      }
      DmDMO.objectReference(dmvo.getChildrenVO());
      return (DMDataVO[])dmvo.getChildrenVO();
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private DMDataVO[] query2(String sWhereClause, String sJoinSrcBill)
    throws BusinessException
  {
    try
    {
      StringBuffer sb = new StringBuffer();

      sb.append("\tSELECT dm_delivdaypl.pk_delivdaypl, dm_delivdaypl.vdelivdayplcode, dm_delivdaypl.vbilltype, ").append("\t\t\tdm_delivdaypl.pkbillb, dm_delivdaypl.pkbillh, dm_delivdaypl.vsrcbillnum, dm_delivdaypl.iplanstatus, ").append("\t    dm_delivdaypl.plandate, dm_delivdaypl.ordsndate, dm_delivdaypl.requiredate, dm_delivdaypl.snddate,").append("\t    dm_delivdaypl.pkinv, dm_delivdaypl.pkassistmeasure, dm_delivdaypl.dassistnum, ").append("\t    dm_delivdaypl.dnum, dm_delivdaypl.pkcust, dm_delivdaypl.pksendtype, dm_delivdaypl.vbatchcode,").append("\t    dm_delivdaypl.ibatchstatus,dm_delivdaypl.vfree1, dm_delivdaypl.vfree2, dm_delivdaypl.vfree3, ").append("\t    dm_delivdaypl.vfree4, dm_delivdaypl.vfree5,dm_delivdaypl.vfree6,dm_delivdaypl.vfree7,").append("\t    dm_delivdaypl.vfree8,dm_delivdaypl.vfree9,dm_delivdaypl.vfree10,dm_delivdaypl.pkdeststoreorg,").append("\t    dm_delivdaypl.pkdeststore,dm_delivdaypl.pksalecorp,dm_delivdaypl.pksaleorg,dm_delivdaypl.pksendstoreorg,").append("\t    dm_delivdaypl.pksendstore,dm_delivdaypl.dweight,dm_delivdaypl.dvolumn,").append("\t    dm_delivdaypl.dunitprice,dm_delivdaypl.bpresent,dm_delivdaypl.creceiptcorpid, dm_delivdaypl.vdestaddress, ").append("\t    dm_delivdaypl.dsendnum,dm_delivdaypl.dsignnum,dm_delivdaypl.doutnum,").append("\t    dm_delivdaypl.dbacknum,dm_delivdaypl.pkitem,dm_delivdaypl.pkitemperiod,").append("\t    dm_delivdaypl.pkoperator,dm_delivdaypl.pkoprdepart,dm_delivdaypl.pkplanperson,").append("\t    dm_delivdaypl.pkapprperson,dm_delivdaypl.apprdate,dm_delivdaypl.pkarrivearea,dm_delivdaypl.ts,dm_delivdaypl.vnote, ").append("\t    dm_delivdaypl.nfeedbacknum, ").append("\t    dm_delivdaypl.cquoteunitid, dm_delivdaypl.nquoteunitnum, dm_delivdaypl.nquoteunitrate, ");

      for (int i = 0; i < 20; i++) {
        sb.append("dm_delivdaypl.vuserdef" + i + ", ");
        sb.append("dm_delivdaypl.vuserdef_b_" + i + ", ");
        sb.append("dm_delivdaypl.pk_defdoc" + i + ", ");
        sb.append("dm_delivdaypl.pk_defdoc_b_" + i + ", ");
      }

      sb.append("\t\t\tdm_delivdaypl.cbiztype, ").append("\t\t\tdm_delivdaypl.pksendarea, dm_delivdaypl.cplansendtime,dm_delivdaypl.crequiretime,").append("dm_delivdaypl.dmoney, ").append("bd_delivorg.pk_delivorg AS pkdelivorg,  ").append("dm_delivdaypl.pksendaddress, dm_delivdaypl.pkarriveaddress, ").append("dm_delivdaypl.dwaylossnum, dm_delivdaypl.pkarrivecorp, ").append("dm_delivdaypl.pkorderplanid, dm_delivdaypl.borderreturn, ").append("dm_delivdaypl.csendcorpid, dm_delivdaypl.vsendaddr, ").append("dm_delivdaypl.iprintcount ").append("\tFROM dm_delivdaypl inner JOIN ").append(" bd_delivorg on bd_delivorg.pk_delivorg = dm_delivdaypl.pkdelivorg ");

      if (sJoinSrcBill != null) {
        sb.append(sJoinSrcBill);
      }

      if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
        sb.append(" where dm_delivdaypl.dr=0 and bd_delivorg.dr=0 and" + sWhereClause);
      }

      DMDataVO[] ddvos = super.query(sb);

      for (int i = 0; (ddvos != null) && (i < ddvos.length); i++) {
        Object obj = ddvos[i].getAttributeValue("borderreturn");
        ddvos[i].setAttributeValue("borderreturn", (obj != null) && (obj.toString().equals("Y")) ? new UFBoolean(true) : new UFBoolean(false));
      }

      return ddvos;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private String queryDefaultDelivorg(String sCorpID, String sAgentStoreOrgID, int ibizattribute)
    throws BusinessException
  {
    try
    {
      StringBuffer sbSql = new StringBuffer();

      sbSql.append("SELECT bd_delivorg.pk_delivorg, bd_doagent.pkcorp, ");
      sbSql.append("COALESCE(bd_doagent.pksendstoreorg, ' ')  AS pksendstoreorg ");
      sbSql.append("FROM bd_delivorg ");
      sbSql.append("inner join bd_doagent ");
      sbSql.append("on bd_delivorg.pk_delivorg = bd_doagent.pkdelivorg ");
      sbSql.append("WHERE bd_delivorg.dr = 0 and bd_doagent.dr =0  and bd_doagent.pkcorp = '" + sCorpID + "' and (bd_doagent.bseal is null or bd_doagent.bseal = 'N') ");

      sbSql.append("and (bd_doagent.pksendstoreorg = '" + sAgentStoreOrgID + "'  or bd_doagent.pksendstoreorg is null) ");
      switch (ibizattribute)
      {
      case 0:
        sbSql.append("and ibizattribute=0 ");
        break;
      case 1:
        sbSql.append("and ibizattribute<>2 ");
        break;
      case 2:
        sbSql.append("and ibizattribute<>1 ");
      }

      sbSql.append("order by pksendstoreorg DESC ");

      DMDataVO[] ddvos = super.query(sbSql);

      String sDelivOrg = null;
      if ((ddvos != null) && (ddvos.length > 0)) {
        sDelivOrg = ddvos[0].getAttributeValue("pk_delivorg").toString();
      }
      return sDelivOrg;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private HashMap queryAllDelivorg()
    throws BusinessException
  {
    try
    {
      StringBuffer sbSql = new StringBuffer();

      sbSql.append("SELECT bd_delivorg.pk_delivorg, bd_doagent.pkcorp, ");
      sbSql.append("bd_doagent.pksendstoreorg,bd_doagent.ibizattribute ");
      sbSql.append("FROM bd_delivorg ");
      sbSql.append("inner join bd_doagent ");
      sbSql.append("on bd_delivorg.pk_delivorg = bd_doagent.pkdelivorg ");
      sbSql.append("WHERE bd_delivorg.dr = 0 and bd_doagent.dr =0  and (bd_doagent.bseal is null or bd_doagent.bseal = 'N') ");

      DMDataVO[] ddvos = super.query(sbSql);

      HashMap hmResult = new HashMap();
      for (int i = 0; i < ddvos.length; i++) {
        String sCorpID = (String)ddvos[i].getAttributeValue("pkcorp");
        String sStoreorgID = (String)ddvos[i].getAttributeValue("pksendstoreorg");
        int iBizattribute = Integer.parseInt(ddvos[i].getAttributeValue("ibizattribute").toString());
        String sID = getDelivorgSID(sCorpID, sStoreorgID, iBizattribute);
        hmResult.put(sID, ddvos[i].getAttributeValue("pk_delivorg").toString());
      }
      return hmResult;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private String queryDefaultDelivorgFromAll(String sCorpID, String sAgentStoreOrgID, int ibizattribute, HashMap hmdelivorgvos)
    throws BusinessException
  {
    try
    {
      String sDelivOrgID = null;

      String sID = getDelivorgSID(sCorpID, sAgentStoreOrgID, ibizattribute);
      sDelivOrgID = (String)hmdelivorgvos.get(sID);
      if ((sDelivOrgID == null) && (ibizattribute != 0)) {
        sID = getDelivorgSID(sCorpID, sAgentStoreOrgID, 0);
        sDelivOrgID = (String)hmdelivorgvos.get(sID);
      }
      if (sDelivOrgID == null) {
        sID = getDelivorgSID(sCorpID, null, 0);
        sDelivOrgID = (String)hmdelivorgvos.get(sID);
      }
      return sDelivOrgID;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private String getDelivorgSID(String sCorpID, String sAgentStoreOrgID, int ibizattribute)
  {
    StringBuffer sID = new StringBuffer("^&@%$");

    if ((sCorpID != null) && (sCorpID.length() != 0)) {
      sID.append(sCorpID);
    }
    else {
      sID.append("nocorp");
    }
    sID.append("^&@%$");
    if ((sAgentStoreOrgID != null) && (sAgentStoreOrgID.length() != 0)) {
      sID.append(sAgentStoreOrgID);
    }
    else {
      sID.append("nosendstoreorg");
    }
    sID.append("^&@%$");
    sID.append(ibizattribute);
    return sID.toString();
  }

  public Hashtable queryDelivOrgsHtvo(Hashtable htDelivOrg, String sParam)
    throws BusinessException
  {
    try
    {
      String sDelivorgs = StringTools.getStrIDsOfArry(UsefulTools.hashtableKeysToArrayList(htDelivOrg));
      DMDataVO[] delivorgvos = query(new StringBuffer("select pk_delivorg, idelivsequence, pkcorp from bd_delivorg where bd_delivorg.dr = 0 and pk_delivorg in (" + sDelivorgs + ")"));

      Hashtable htDelivOrgvo = new Hashtable();
      List corps = new ArrayList();
      for (int i = 0; i < delivorgvos.length; i++) {
        htDelivOrgvo.put(delivorgvos[i].getAttributeValue("pk_delivorg").toString(), delivorgvos[i]);
        corps.add((String)delivorgvos[i].getAttributeValue("pkcorp"));
      }

      ISysInitQry iSysinitQry = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      List paravalues = new ArrayList();
      int i = 0; for (int len = corps.size(); i < len; i++) {
        String sCorpID = (String)corps.get(i);
        String sReturnValue = iSysinitQry.getParaString(sCorpID, sParam);
        String DM016 = "N";
        if (sReturnValue != null) {
          DM016 = sReturnValue;
        }
        paravalues.add(DM016);
      }
      for (int i1 = 0; i1 < delivorgvos.length; i1++) {
        DMDataVO delivorgvo = (DMDataVO)htDelivOrgvo.get(delivorgvos[i1].getAttributeValue("pk_delivorg").toString());
        delivorgvo.setAttributeValue(sParam, paravalues.get(i1));
      }
      return htDelivOrgvo;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public BillVO[] queryInnerAllocationOrderByVO(ConditionVO[] vos)
    throws BusinessException
  {
    try
    {
      long lStartTime = System.currentTimeMillis();

      DmFromIcDMO dmo = new DmFromIcDMO();
      BillVO[] alorders = dmo.queryInnerAllocationOrder(vos);

      long lTime = System.currentTimeMillis() - lStartTime;
      SCMEnv.info("执行<调拨订单查询>消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");

      return alorders;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  protected Hashtable queryInvProduceStoreByInvs(ArrayList alInvs)
    throws BusinessException
  {
    try
    {
      Hashtable ht = new Hashtable();
      StringBuffer sql = new StringBuffer();

      sql.append("select bd_produce.pk_invmandoc,bd_produce.pk_stordoc from bd_produce ").append(" inner join bd_stordoc on bd_produce.pk_stordoc=bd_stordoc.pk_stordoc and ").append(" bd_stordoc.isdirectstore='N' and bd_stordoc.gubflag='N'");

      sql.append(" where 1 = 1 " + SQLUtil.formInSQL("pk_invmandoc", alInvs));

      DMDataVO[] dmdvos = query(sql);

      for (int i = 0; i < dmdvos.length; i++) {
        if ((dmdvos[i].getAttributeValue("pk_invmandoc") != null) && (dmdvos[i].getAttributeValue("pk_stordoc") != null)) {
          ht.put(dmdvos[i].getAttributeValue("pk_invmandoc").toString(), dmdvos[i].getAttributeValue("pk_stordoc").toString());
        }

      }

      return ht;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public ArrayList queryOrders4DailyPlan(ConditionVO[] convos, DMDataVO voCondNormal)
    throws BusinessException
  {
    ClientLink clientLink = (ClientLink)voCondNormal.getAttributeValue("ClientLink");
    String strCorpID = clientLink.getCorp();
    ArrayList alAllOrderVOs = new ArrayList(6);
    try
    {
      ConditionVO[] vos = new ConditionVO[convos.length + 1];
      for (int i = 0; i < convos.length; i++) {
        vos[i] = convos[i];
      }
      vos[convos.length] = new ConditionVO();
      vos[convos.length].setFieldCode("发货库存组织");
      vos[convos.length].setOperaCode(" in ");
      vos[convos.length].setDataType(1);
      String sDataPower = null;
      ArrayList listDeputCorpPK = (ArrayList)voCondNormal.getAttributeValue("DeputCorpPKs");
      if (listDeputCorpPK != null) {
        if (listDeputCorpPK.size() != 0)
        {
          String strUserID = clientLink.getUser();

          sDataPower = deliverydailyplan_getDataPowerSubSql("bd_calbody", "库存组织", null, (String[])listDeputCorpPK.toArray(new String[listDeputCorpPK.size()]), clientLink.getUser());

          if (sDataPower != null) {
            vos[convos.length].setValue(sDataPower);
          }
          else {
            vos[convos.length].setFieldCode("1");
            vos[convos.length].setOperaCode("=");
            vos[convos.length].setValue("1");
          }
        }
        vos[convos.length].setValue(sDataPower);
      }

      String strTmpTabName = null;
      String sDelivorg = null;

      for (int i = 0; i < vos.length; i++)
      {
        if (!vos[i].getFieldCode().equals("pkdelivorg"))
          continue;
        sDelivorg = vos[i].getValue();
        StringBuffer sb = new StringBuffer();

        sb.append("select pksendstoreorg ").append("from bd_doagent ").append("where dr=0 and bseal='N' and pkdelivorg='" + sDelivorg + "' and (pksendstoreorg is not null and rtrim(pksendstoreorg) is not null) ").append("and ibizattribute<>2  ").append("union all ").append("select pk_calbody as pksendstoreorg from bd_calbody where ").append("pk_corp in (").append("\t\tselect pkcorp from bd_doagent ").append("\t\twhere dr=0 and bseal='N' and pkdelivorg='" + sDelivorg + "' and (pksendstoreorg is null or rtrim(pksendstoreorg) is null) ").append("   and ibizattribute<>2  ").append(") ").append("and pk_calbody not in (").append(" select pksendstoreorg ").append(" from bd_doagent ").append(" where dr=0 and bseal='N' and (pksendstoreorg is not null) and rtrim(pksendstoreorg) is not null ").append(") ");

        ConditionVO[] cvosnew = new ConditionVO[vos.length + 1];
        System.arraycopy(vos, 0, cvosnew, 0, vos.length);
        cvosnew[vos.length] = new ConditionVO();
        cvosnew[vos.length].setFieldCode("发货库存组织");
        cvosnew[vos.length].setValue(sb.toString());

        vos = cvosnew;
        break;
      }

      long lStartTime = System.currentTimeMillis();

      String sVbilltype = null;

      DMDataVO[] dmdata4U = (DMDataVO[])null;
      DMDataVO[] dmdataSO = (DMDataVO[])null;
      DMDataVO[] dmdata5C = (DMDataVO[])null;
      DMDataVO[] dmdata5D = (DMDataVO[])null;
      DMDataVO[] dmdata5E = (DMDataVO[])null;
      DMDataVO[] dmdata5I = (DMDataVO[])null;
      DMDataVO[] dmdataPO = (DMDataVO[])null;

      for (int i = 0; i < vos.length; i++) {
        if (!vos[i].getFieldCode().equals("vbilltype")) {
          continue;
        }
        SCMEnv.info(new Integer(vos[i].getDataType()));
        if (vos[i].getValue().equals("30")) {
          sVbilltype = "30";
          break;
        }
        if (vos[i].getValue().equals("tobill")) {
          sVbilltype = "tobill";
          break;
        }
        if (vos[i].getValue().equals("21")) {
          sVbilltype = "21";
          break;
        }

      }

      int iAllRowsCount = 0;
      boolean isExistOrderReturn = false;
      for (int i = 0; i < vos.length; i++) {
        if (vos[i].getFieldCode().equals("borderreturn")) {
          if (vos[i].getValue().equals("")) break;
          isExistOrderReturn = true;
          break;
        }

      }

      if ((!isExistOrderReturn) && ((sVbilltype == null) || (sVbilltype.equals("30")))) {
        Vector vNewCons = new Vector();
        for (int i = 0; i < vos.length; i++)
        {
          if (!vos[i].getFieldCode().equals("borderreturn")) {
            vNewCons.add((ConditionVO)vos[i].clone());
          }
        }

        ConditionVO[] newCons = (ConditionVO[])vNewCons.toArray(new ConditionVO[vNewCons.size()]);

        AggregatedValueObject[] saleorders = querySoOrderByString(newCons);
        if (saleorders == null)
          saleorders = new SaleOrderVO[0];
        for (int i = 0; i < saleorders.length; i++)
        {
          iAllRowsCount += saleorders[i].getChildrenVO().length;
          checkTotalRowCount(iAllRowsCount);
        }

        dmdataSO = convertSourceVO(saleorders, "30", "7D");
      }

      if ((!isExistOrderReturn) && ((sVbilltype == null) || (sVbilltype.equals("tobill"))))
      {
        boolean bTOModuleStarted = isModuleStarted(strCorpID, "TO");
        if (bTOModuleStarted)
        {
          Vector vNewCons = new Vector();

          for (int i = 0; i < vos.length; i++) {
            if ((vos[i].getFieldCode().equals("borderreturn")) || (vos[i].getFieldCode().equals("vbilltype")))
              continue;
            vNewCons.add((ConditionVO)vos[i].clone());
          }

          ConditionVO[] newCons = (ConditionVO[])vNewCons.toArray(new ConditionVO[vNewCons.size()]);
          BillVO[] orders = queryInnerAllocationOrderByVO(newCons);

          ArrayList al5CVOs = new ArrayList();
          ArrayList al5DVOs = new ArrayList();
          ArrayList al5EVOs = new ArrayList();
          ArrayList al5IVOs = new ArrayList();

          for (int i = 0; (orders != null) && (i < orders.length); i++)
          {
            iAllRowsCount += orders[i].getChildrenVO().length;
            checkTotalRowCount(iAllRowsCount);

            if (orders[i].getParentVO().getAttributeValue("ctypecode").toString().equals("5C")) {
              al5CVOs.add(orders[i]);
            }
            else if (orders[i].getParentVO().getAttributeValue("ctypecode").toString().equals("5D")) {
              al5DVOs.add(orders[i]);
            }
            else if (orders[i].getParentVO().getAttributeValue("ctypecode").toString().equals("5E")) {
              al5EVOs.add(orders[i]);
            }
            else if (orders[i].getParentVO().getAttributeValue("ctypecode").toString().equals("5I")) {
              al5IVOs.add(orders[i]);
            }
          }

          dmdata5C = convertSourceVO((BillVO[])al5CVOs.toArray(new BillVO[0]), "5C", "7D");
          dmdata5D = convertSourceVO((BillVO[])al5DVOs.toArray(new BillVO[0]), "5D", "7D");
          dmdata5E = convertSourceVO((BillVO[])al5EVOs.toArray(new BillVO[0]), "5E", "7D");
          dmdata5I = convertSourceVO((BillVO[])al5IVOs.toArray(new BillVO[0]), "5I", "7D");
        }

      }

      if ((sVbilltype == null) || (sVbilltype.equals("21")))
      {
        boolean bPUModuleStarted = isModuleStarted(strCorpID, "PO");
        if (bPUModuleStarted)
        {
          Vector vNewCons = new Vector();

          for (int i = 0; i < vos.length; i++)
          {
            if (vos[i].getFieldCode().equals("发货库存组织")) {
              if (vos[i].getDataType() == 1) {
                vos[i].setFieldCode("pksendstockorg");
              }

            }
            else
            {
              if ((vos[i].getFieldCode().equals("borderreturn")) && (vos[i].getValue().equals(""))) {
                continue;
              }
              vNewCons.add((ConditionVO)vos[i].clone());
            }
          }
          ConditionVO[] newCons = (ConditionVO[])vNewCons.toArray(new ConditionVO[vNewCons.size()]);
          IPuToDm_Order iPuToDm_OrderBO = (IPuToDm_Order)NCLocator.getInstance().lookup(IPuToDm_Order.class.getName());
          AggregatedValueObject[] orders = iPuToDm_OrderBO.queryOrderArrayForDaypl(sDelivorg, newCons, clientLink);
          for (int i = 0; (orders != null) && (i < orders.length); i++)
          {
            iAllRowsCount += orders[i].getChildrenVO().length;
            checkTotalRowCount(iAllRowsCount);
          }

          dmdataPO = convertSourceVO(orders, "21", "7D");
        }
      }

      alAllOrderVOs.add(0, dmdata4U);
      alAllOrderVOs.add(1, dmdataSO);
      alAllOrderVOs.add(2, dmdata5C);
      alAllOrderVOs.add(3, dmdata5D);
      alAllOrderVOs.add(4, dmdata5E);
      alAllOrderVOs.add(5, dmdata5I);
      alAllOrderVOs.add(6, dmdataPO);

      long lTime = System.currentTimeMillis() - lStartTime;
      SCMEnv.info("执行<调拨、采购和销售订单查询>消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
    }
    catch (Exception e)
    {
      ExceptionUtils.marsh(e);
    }
    return alAllOrderVOs;
  }

  private DMDataVO[] convertSourceVO(AggregatedValueObject[] vos, String sourceBillType, String destBillType)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return null;
    }
    ArrayList v = new ArrayList();

    DMVO[] dvo = (DMVO[])null;
    try {
      dvo = (DMVO[])PfUtilTools.runChangeDataAry(sourceBillType, destBillType, vos);
    }
    catch (Exception ex) {
      throw new BusinessException(ex);
    }

    for (int i = 0; i < dvo.length; i++) {
      for (int j = 0; j < dvo[i].getChildrenVO().length; j++) {
        v.add(dvo[i].getChildrenVO()[j]);
      }
    }
    DMDataVO[] dmdvos = new DMDataVO[v.size()];
    dmdvos = (DMDataVO[])v.toArray(dmdvos);
    v = null;

    if (sourceBillType.equals("21")) {
      return pushsavePrepareDailyPlanFromPO(dmdvos);
    }
    for (int i = 0; i < dmdvos.length; i++) {
      if (!dmdvos[i].getAttributeValue("vbilltype").equals("30"))
        continue;
      if ((dmdvos[i].getAttributeValue("pkdeststoreorg") == null) || (dmdvos[i].getAttributeValue("pkdeststoreorg").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstoreorg", dmdvos[i].getAttributeValue("pkdeststoreorg"));
      dmdvos[i].setAttributeValue("pkdeststoreorg", null);

      if ((dmdvos[i].getAttributeValue("pkdeststore") != null) && (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() > 0))
      {
        dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
        dmdvos[i].removeAttributeName("pkdeststore");
      }

      if ((dmdvos[i].getAttributeValue("pkdeststore") == null) || (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
      dmdvos[i].removeAttributeName("pkdeststore");
    }

    SuperVOUtil.execFormulaWithVOs(dmdvos, new String[] { "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)", "pksendaddress->iif(getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore) = \"\",getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore))" }, null);

    return dmdvos;
  }

  private AggregatedValueObject[] querySoOrderByString(ConditionVO[] vos)
    throws BusinessException
  {
    try
    {
      long lStartTime = System.currentTimeMillis();

      DmFromSoDMO dmo = new DmFromSoDMO();
      AggregatedValueObject[] saleorders = dmo.querySoOrder(vos);

      long lTime = System.currentTimeMillis() - lStartTime;
      SCMEnv.info("执行<销售订单>消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");

      return saleorders;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public DMDataVO[] saveAndWriteAndDelInterface(DMDataVO[] vos, DMDataVO[] planvos, boolean bIsDelInterface)
    throws BusinessException
  {
    IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
    for (int i = 0; i < planvos.length; i++)
    {
      DMDataVO vo = (DMDataVO)planvos[i].clone();
      vo.setAttributeValue("vbilltype", DMBillTypeConst.m_delivDelivDayPl);
      srv.checkDefDataType(vo);
    }
    String[] sAllKeys = (String[])null;
    try {
      long s = System.currentTimeMillis();
      showMethodTime("saveAndWriteAndDelInterfaceSTART", s);

      if (bIsDelInterface) {
        sAllKeys = checkLockPKsForDelInterface(planvos);
      }
      else {
        sAllKeys = checkLockPKs(planvos);
      }
      showMethodTime("加锁", s);

      saveCheck(planvos);
      showMethodTime("时间戳校验", s);

      Vector vTOOrder = new Vector();
      Vector vSOOrder = new Vector();
      Vector vPUOrder = new Vector();
      for (int i = 0; i < planvos.length; i++) {
        String sSourcebilltype = (String)planvos[i].getAttributeValue("vbilltype");
        if (sSourcebilltype.equals("30")) {
          vSOOrder.add((DMDataVO)planvos[i].clone());
        } else if (sSourcebilltype.equals("21")) {
          vPUOrder.add((DMDataVO)planvos[i].clone()); } else {
          if ((!sSourcebilltype.equals("5C")) && (!sSourcebilltype.equals("5D")) && (!sSourcebilltype.equals("5E")) && (!sSourcebilltype.equals("5I")))
            continue;
          vTOOrder.add((DMDataVO)planvos[i].clone());
        }
      }
      DMDataVO[] dmvoSOOrder = new DMDataVO[vSOOrder.size()];
      DMDataVO[] dmvoPUOrder = new DMDataVO[vPUOrder.size()];
      DMDataVO[] dmvoTOOrder = new DMDataVO[vTOOrder.size()];
      
      vSOOrder.copyInto(dmvoSOOrder);
      vPUOrder.copyInto(dmvoPUOrder);
      vTOOrder.copyInto(dmvoTOOrder);
//      //add by src 2018年3月12日20:46:02
//      String cvmflaf = dmvoSOOrder[0].getAttributeValue("cvmflag")==null?"":dmvoSOOrder[0].getAttributeValue("cvmflag").toString();
//      if(cvmflaf.equals("CVM")){
			if (dmvoSOOrder.length > 0
					&& (dmvoSOOrder[0].getPk_corp().equals("1016")
							|| dmvoSOOrder[0].getPk_corp().equals("1071")
							|| dmvoSOOrder[0].getPk_corp().equals("1103")
							|| dmvoSOOrder[0].getPk_corp().equals("1097") 
							|| dmvoSOOrder[0].getPk_corp().equals("1017")
							|| dmvoSOOrder[0].getPk_corp().equals("1018")
							|| dmvoSOOrder[0].getPk_corp().equals("1019")
							|| dmvoSOOrder[0].getPk_corp().equals("1107")
					)) {
    	  dmvoSOOrder[0].setAttributeValue("sourcebillts", planvos[0].getAttributeValue("sourcebillts"));
      }
//      }

      if (!bIsDelInterface)
      {
        if (dmvoSOOrder.length > 0) {
          checkSourceTsForNewAdd("vsrcbillnum", "pkbillh", "sourcebillts", new String[] { "so_sale" }, new String[] { "csaleid" }, new String[] { "ts" }, dmvoSOOrder);
        }

        if (dmvoTOOrder.length > 0) {
          checkSourceTsForNewAdd("vsrcbillnum", "pkbillh", "sourcebillts", new String[] { "to_bill" }, new String[] { "cbillid" }, new String[] { "ts" }, dmvoTOOrder);
        }

        if (dmvoPUOrder.length > 0) {
          checkSourceTsForNewAdd("vsrcbillnum", "pkbillh", "sourcehts", new String[] { "po_order" }, new String[] { "corderid" }, new String[] { "ts" }, dmvoPUOrder);
        }

        if (dmvoPUOrder.length > 0) {
          checkSourceTsForNewAdd("vsrcbillnum", "pkbillb", "sourcebts", new String[] { "po_order_b" }, new String[] { "corder_bid" }, new String[] { "ts" }, dmvoPUOrder);
        }

      }

      DMDataVO[] retvos = saveDailyPlans(planvos, bIsDelInterface);
      showMethodTime("保存日计划", s);

      ArrayList alSO = new ArrayList();
      ArrayList alPU = new ArrayList();
      ArrayList alIC = new ArrayList();
      for (int i = 0; i < vos.length; i++) {
        if (vos[i].getAttributeValue("vbilltype").equals("30")) {
          alSO.add(vos[i]);
        }

      }

      if (alSO.size() > 0) {
        DMDataVO[] sovos = (DMDataVO[])alSO.toArray(new DMDataVO[0]);
        DmToSoDMO dmo2 = new DmToSoDMO();
        dmo2.setDeliverNumToSO(sovos);
      }

      if (dmvoPUOrder.length > 0) {
        ParaDayplToPoRewriteVO rewriteVO = DMDelivdayplVOTool.getDayplWritePOData(dmvoPUOrder);
        getPUInter().rewriteDayplNum(rewriteVO);
      }

      ParaRewriteVO[] prvo = getParaRewriteVO(vos, "ndelivernum");
      if (prvo != null)
      {
        DmFromTODMO boNBDB = new DmFromTODMO();
        for (int i = 0; i < prvo.length; i++) {
          boNBDB.backDMToOrder(prvo[i], new Integer(2));
        }

      }

      showMethodTime("回写上游", s);

      if (!bIsDelInterface)
      {
        if (planvos[0].getStatus() == 2) {
          signButtonClickTimeS(planvos, false);
        }

        if (planvos[0].getStatus() == 3)
        {
          signButtonClickTimeS(planvos, true);
        }
      }

      showMethodTime("saveAndWriteAndDelInterfaceEND", s);

      DMDataVO[] arrayOfDMDataVO1 = retvos;
      return arrayOfDMDataVO1;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, planvos[0].getAttributeValue("userid").toString());
    }
  }

  public DMDataVO[] reopen(DMDataVO[] vos)
    throws BusinessException
  {
    String[] sAllKeys = (String[])null;
    try
    {
      sAllKeys = checkLockPKs(vos);

      saveCheck(vos);

      DMATP dmatp = new DMATP();

      reOpenFreeDelivplanNum(vos);

      DMVO dvo = new DMVO();
      dvo.setChildrenVO(vos);
      if (!vos[0].getAttributeValue("vbilltype").equals("21"))
      {
        Object iplanstatus = vos[0].getAttributeValue("iplanstatus");
        if ((iplanstatus != null) && (iplanstatus.toString().length() > 0) && (new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.Audit))
        {
          dmatp.modifyATPWhenOpenBill(dvo);
        }
      }

      DMDataVO[] vosRet = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);

      DMDataVO[] arrayOfDMDataVO1 = vosRet;
      return arrayOfDMDataVO1;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
  }

  protected OutbillHItemVO[] resortStore(DMDataVO delivItemVO, OutbillHItemVO[] outbillItemVOs, Hashtable htInvStore)
    throws BusinessException
  {
    Object pksendstock = delivItemVO.getAttributeValue("pksendstore");
    String pkinv = delivItemVO.getAttributeValue("pkinv").toString();
    if ((pksendstock == null) || (pksendstock.toString().trim().length() == 0)) {
      pksendstock = "";
    }

    DMVO dmvo = new DMVO();
    DMDataVO[] dmdvos = new DMDataVO[outbillItemVOs.length];
    for (int i = 0; i < dmdvos.length; i++) {
      dmdvos[i] = new DMDataVO();
      dmdvos[i] = dmdvos[i].translateFromOtherVO(outbillItemVOs[i]);
    }
    dmvo.setChildrenVO(dmdvos);
    dmvo.sortByKeys(new String[] { "donhandnum" }, new int[] { 2 });

    outbillItemVOs = new OutbillHItemVO[dmvo.getBodyVOs().length];
    for (int i = 0; i < dmvo.getBodyVOs().length; i++) {
      outbillItemVOs[i] = new OutbillHItemVO();
      dmvo.getBodyVOs()[i].translateToOtherVO(outbillItemVOs[i]);
    }

    Vector vOutBillItem = new Vector();

    for (int i = 0; i < outbillItemVOs.length; i++) {
      String sPkstroe = (String)outbillItemVOs[i].getAttributeValue("pkstroe");
      if ((sPkstroe != null) && (pksendstock.equals(sPkstroe))) {
        vOutBillItem.add(0, outbillItemVOs[i]);
      }
      else if ((htInvStore.containsKey(pkinv)) && (htInvStore.get(pkinv).toString().trim().equals(sPkstroe))) {
        if ((vOutBillItem.size() > 0) && (pksendstock.equals(((OutbillHItemVO)vOutBillItem.get(0)).getAttributeValue("pkstroe"))))
        {
          vOutBillItem.add(1, outbillItemVOs[i]);
        }
        else
        {
          vOutBillItem.add(0, outbillItemVOs[i]);
        }
      }
      else {
        vOutBillItem.add(outbillItemVOs[i]);
      }
    }
    OutbillHItemVO[] newOutbillItemVOs = new OutbillHItemVO[vOutBillItem.size()];
    vOutBillItem.copyInto(newOutbillItemVOs);
    return newOutbillItemVOs;
  }

  public void deliverydailyplan_returnBillCode(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, CircularlyAccessibleValueObject[] vos)
    throws BusinessException
  {
    try
    {
      if ((vos == null) || (vos.length == 0))
        return;
      DeliverydailyplanDMO dmo = new DeliverydailyplanDMO();
      BillCodeObjValueVO[] bvos = new BillCodeObjValueVO[vos.length];
      for (int i = 0; i < vos.length; i++) {
        vos[i].setAttributeValue(sBillTypeCodeKey, DMBillTypeConst.m_delivDelivDayPl);
        bvos[i] = getBillCodeObjVO(vos[i]);
      }
      dmo.returnCVOBillCode(sBillTypeCodeKey, sCorpKey, sBillCodeKey, bvos, vos);
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public void deliverydailyplan_returnBillCodeForUI(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, CircularlyAccessibleValueObject[] vos)
    throws BusinessException
  {
    try
    {
      if ((vos == null) || (vos.length == 0))
        return;
      DeliverydailyplanDMO dmo = new DeliverydailyplanDMO();
      BillCodeObjValueVO[] bvos = new BillCodeObjValueVO[vos.length];
      for (int i = 0; i < vos.length; i++) {
        vos[i].setAttributeValue(sBillTypeCodeKey, DMBillTypeConst.m_delivDelivDayPl);
        bvos[i] = getBillCodeObjVO(vos[i]);
      }
      dmo.returnCVOBillCodeForUI(sBillTypeCodeKey, sCorpKey, sBillCodeKey, bvos, vos);
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  protected DMDataVO[] save(DMDataVO[] vos)
    throws BusinessException
  {
    String[] sAllKeys = (String[])null;
    try
    {
      sAllKeys = checkLockPKs(vos);

      saveCheck(vos);

      DMDataVO[] arrayOfDMDataVO = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);
      DMDataVO[] arrayOfDMDataVO1 = arrayOfDMDataVO;
      return arrayOfDMDataVO1;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
  }

  protected DMDataVO[] save(DMDataVO[] vos, ValueRangeHashtable htRangeBody, boolean bNeedTS)
    throws BusinessException
  {
    try
    {
      long s = System.currentTimeMillis();
      showMethodTime("saveSTART", s);

      deliverydailyplan_returnBillCode("vdayplanbilltype", "pkcorp", "vdelivdayplcode", vos);
      showMethodTime("对删除和修改行释放单据号", s);

      DMVO vo = new DMVO();
      vo.setChildrenVO(vos);
      vo = super.save(null, "dm_delivdaypl", vo, null, htRangeBody, bNeedTS);
      showMethodTime("日计划保存", s);
      showMethodTime("saveEND", s);

      return vo.getBodyVOs();
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public DMDataVO[] saveAndWrite(DMDataVO[] vos, DMDataVO[] planvos)
    throws BusinessException
  {
    return saveAndWriteAndDelInterface(vos, planvos, false);
  }

  protected void saveCheck(DMDataVO[] vos)
    throws BusinessException, BusinessException, NamingException
  {
    checkTs(vos);
  }

  protected DMDataVO[] saveDailyPlans(DMDataVO[] vos, boolean bIsDelInterface)
    throws BusinessException
  {
    try
    {
      long s = getCurrentTime();
      showMethodTime("saveDailyPlansSTART", s);

      Hashtable htDelivOrg = new Hashtable();
      for (int i = 0; i < vos.length; i++) {
        String sDelivorgID = vos[i].getAttributeValue("pkdelivorg").toString();
        htDelivOrg.put(sDelivorgID, "");
      }
      Hashtable htDelivOrgvo = queryDelivOrgsHtvo(htDelivOrg, "DM016");
      showMethodTime("查发运组织属性", s);

      DMVO dvo = new DMVO();
      dvo.setChildrenVO(vos);

      DMATP dmatp = new DMATP();

      if ((vos[0].getStatus() == 2) || ((vos[0].getStatus() == 1) && (vos[0].getAttributeValue("thisaction") != null) && (vos[0].getAttributeValue("thisaction").equals("修改"))))
      {
        for (int i = 0; i < vos.length; i++) {
          vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dnum"));
        }
        if (!vos[0].getAttributeValue("vbilltype").equals("21")) {
          dmatp.modifyATP(dvo);
        }

      }

      if (vos[0].getStatus() == 3) {
        for (int i = 0; i < vos.length; i++) {
          vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dnum"));
        }
        if (!vos[0].getAttributeValue("vbilltype").equals("21")) {
          dmatp.modifyATPWhenDeleteBill(dvo);
        }

      }

      Object iplanstatus = vos[0].getAttributeValue("iplanstatus");
      if ((iplanstatus != null) && (iplanstatus.toString().length() > 0) && (new Integer(iplanstatus.toString()).intValue() == DailyPlanStatus.End))
      {
        for (int i = 0; i < vos.length; i++) {
          if (!((DMDataVO)htDelivOrgvo.get(vos[i].getAttributeValue("pkdelivorg").toString())).getAttributeValue("DM016").toString().equals("Y"))
            continue;
          if ((vos[i].getAttributeValue("vbilltype").equals("30")) && (((DMDataVO)htDelivOrgvo.get(vos[i].getAttributeValue("pkdelivorg").toString())).getAttributeValue("idelivsequence").toString().equals("1")))
          {
            vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("doutnum"));
          }
          else {
            vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dsendnum"));
          }
        }

        if (!vos[0].getAttributeValue("vbilltype").equals("21")) {
          dmatp.modifyATPWhenCloseBill(dvo);
        }
      }
      showMethodTime("可用量接口", s);

      boolean isNeedRetTS = !bIsDelInterface;
      DMDataVO[] retvo = save(vos, new ValueRangeHashtableDeliverydailyplan(), isNeedRetTS);
      showMethodTime("日计划保存", s);
      showMethodTime("saveDailyPlansEND", s);

      return retvo;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public DMDataVO[] saveOutDM(GeneralBillVO[] vos, DMDataVO[] planvos)
    throws BusinessException
  {
    DMDataVO[] resultTs = (DMDataVO[])null;
    String[] sAllKeys = (String[])null;
    try
    {
      sAllKeys = checkLockPKs(planvos);

      saveCheck(planvos);

      if (!getICInter().saveOutDM(vos).booleanValue()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000090"));
      }

      resultTs = save(planvos,new ValueRangeHashtableDeliverydailyplan(),true);
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, planvos[0].getAttributeValue("userid").toString());
    }
    return resultTs;
  }

  public DMDataVO[] setBillCode(DMDataVO[] vos, String sBillTypeCode, String sBillcodeField, String sCorpField) throws BusinessException
  {
    try
    {
      BillcodeRuleBO bo = new BillcodeRuleBO();
      BillCodeObjValueVO bcovo = null;
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getAttributeValue("vdelivdayplcode") != null) && (vos[i].getAttributeValue("vdelivdayplcode").toString().trim().length() != 0)) {
          continue;
        }
        String sBillCode = (String)vos[i].getAttributeValue(sBillcodeField);
        String sCorpID = (String)vos[i].getAttributeValue(sCorpField);
        bcovo = getBillCodeObjVO(vos[i]);
        if ((sBillCode == null) || (sBillCode.trim().length() == 0))
          sBillCode = null;
        try {
          if (sBillCode != null)
            sBillCode = sBillCode.trim();
          sBillCode = bo.getBillCode(sBillTypeCode, sCorpID, sBillCode, bcovo);
          vos[i].setAttributeValue(sBillcodeField, sBillCode);
        }
        catch (Exception e) {
          SCMEnv.info("系统获得单据号失败！");
          SCMEnv.info(e.getMessage());
          throw new BusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000091") + e.getMessage()));
        }

      }

    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000091") + e.getMessage()));
    }

    return vos;
  }

  public void setDeliverNum(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      DmToIcDMO dmo1 = new DmToIcDMO();
      dmo1.setDeliverNumToIC(vos);

      DmToSoDMO dmo2 = new DmToSoDMO();
      dmo2.setDeliverNumToSO(vos);
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public void setOutNum(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      Hashtable htAllPKs = new Hashtable();

      for (int i = 0; i < vos.length; i++)
      {
        htAllPKs.put(vos[i].getAttributeValue("pk_delivdaypl"), "");
      }
      String[] allpks = UsefulTools.hashtableKeysToStrings(htAllPKs);
      StringBuffer sb = new StringBuffer();
      sb.append("select pkinv, vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10, ").append("pksendstoreorg, pksendstore, nfeedbacknum, dsendnum,dnum, doutnum as doutnumold, pkdelivorg, iplanstatus,").append(" vbilltype, pksalecorp, snddate, vbatchcode, pk_delivdaypl from dm_delivdaypl ").append("where dr = 0 " + SQLUtil.formInSQL("pk_delivdaypl", allpks));

      DMDataVO[] dmdatavos = super.query(sb);
      DMVO planvo = new DMVO();
      DMVO delivvo = new DMVO();
      planvo.setChildrenVO(dmdatavos);
      delivvo.setChildrenVO(vos);
      delivvo.combineOtherVOByPK(planvo, "pk_delivdaypl");

      DMDataVO[] dmdvos = new DMDataVO[vos.length];
      for (int i = 0; i < vos.length; i++) {
        dmdvos[i] = new DMDataVO();

        dmdvos[i].setAttributeValue("pk_delivdaypl", vos[i].getAttributeValue("pk_delivdaypl"));
        dmdvos[i].setAttributeValue("pkcorp", vos[i].getAttributeValue("pkcorp"));
        dmdvos[i].setAttributeValue("pksalecorp", vos[i].getAttributeValue("pksalecorp"));
        dmdvos[i].setAttributeValue("pkbillh", vos[i].getAttributeValue("pkbillh"));
        dmdvos[i].setAttributeValue("pkbillb", vos[i].getAttributeValue("pkbillb"));
        dmdvos[i].setAttributeValue("pksendstoreorg", vos[i].getAttributeValue("pksendstoreorg"));
        dmdvos[i].setAttributeValue("pksendstore", vos[i].getAttributeValue("pksendstore"));
        dmdvos[i].setAttributeValue("pkinv", vos[i].getAttributeValue("pkinv"));
        dmdvos[i].setAttributeValue("dsendnum", vos[i].getAttributeValue("dsendnum"));
        dmdvos[i].setAttributeValue("dnum", vos[i].getAttributeValue("dnum"));
        dmdvos[i].setAttributeValue("userid", vos[i].getAttributeValue("userid"));
        dmdvos[i].setAttributeValue("snddate", vos[i].getAttributeValue("snddate"));
        dmdvos[i].setAttributeValue("pkdelivorg", vos[i].getAttributeValue("pkdelivorg"));
        dmdvos[i].setAttributeValue("iplanstatus", vos[i].getAttributeValue("iplanstatus"));
        dmdvos[i].setAttributeValue("snddate", vos[i].getAttributeValue("snddate"));
        dmdvos[i].setAttributeValue("vbilltype", vos[i].getAttributeValue("vbilltype"));
        dmdvos[i].setStatus(1);

        dmdvos[i].setAttributeValue("outnumreco", vos[i].getAttributeValue("noutnum"));

        UFDouble ufdOldSendNum = new UFDouble(vos[i].getAttributeValue("doutnumold") == null ? "0" : vos[i].getAttributeValue("doutnumold").toString());

        UFDouble ufdNewSendNum = new UFDouble(vos[i].getAttributeValue("noutnum") == null ? "0" : vos[i].getAttributeValue("noutnum").toString());

        UFDouble ufdEndSendNum = ufdOldSendNum.add(ufdNewSendNum);
        dmdvos[i].setAttributeValue("doutnum", ufdEndSendNum);

        for (int w = 1; w <= 10; w++) {
          dmdvos[i].setAttributeValue("vfree" + w, vos[i].getAttributeValue("vfree" + w));
        }
      }

      setSendNumEndFree(vos);

      for (int i = 0; i < vos.length; i++) {
        if (vos[i].getAttributeValue("vbilltype").equals(BillTypeConst.m_AllocationOrder)) {
          DmToIcDMO dmo1 = new DmToIcDMO();
          dmo1.setOutNumToIC(vos);
          break;
        }

      }

      save(dmdvos,new ValueRangeHashtableDeliverydailyplan(),false);
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public void setSendNum(DMDataVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    String[] sAllKeys = (String[])null;
    DMDataVO[] dmdvos = (DMDataVO[])null;
    try
    {
      sAllKeys = checkLockPKs(vos);

      boolean isAutoClose = false;
      double scale = getDelivScale(vos);
      if (vos.length > 0) {
        SysInitDMO sysinitdmo = new SysInitDMO();
        UFBoolean ufbIsAutoClose = sysinitdmo.getParaBoolean(vos[0].getAttributeValue("pkparamcorp").toString(), "DM007");
        if (ufbIsAutoClose != null) {
          isAutoClose = ufbIsAutoClose.booleanValue();
        }
      }
      dmdvos = new DMDataVO[vos.length];

      Hashtable htAllPKs = new Hashtable();
      for (int i = 0; i < vos.length; i++) {
        htAllPKs.put(vos[i].getAttributeValue("pk_delivdaypl"), "");
      }
      DMDataVO tooldatavo = new DMDataVO();
      StringBuffer sb = new StringBuffer();

      String[] allpks = UsefulTools.hashtableKeysToStrings(htAllPKs);
      sb.append("select pk_delivdaypl, nfeedbacknum,dsendnum AS dsendnumold, doutnum, dnum, iplanstatus, pkdelivorg, idelivsequence, ");
      sb.append("pksendstore, vbatchcode ");
      sb.append("from dm_delivdaypl inner join bd_delivorg on bd_delivorg.pk_delivorg = dm_delivdaypl.pkdelivorg ");
      sb.append("where dm_delivdaypl.dr = 0  and bd_delivorg.dr = 0" + SQLUtil.formInSQL("pk_delivdaypl", allpks));
      DMDataVO[] dmdatavos = super.query(sb);
      DMVO planvo = new DMVO();
      DMVO delivvo = new DMVO();
      planvo.setChildrenVO(dmdatavos);
      delivvo.setChildrenVO(vos);
      delivvo.combineOtherVOByPK(planvo, "pk_delivdaypl");

      for (int i = 0; i < vos.length; i++) {
        dmdvos[i] = new DMDataVO();

        dmdvos[i].setAttributeValue("ndelivernum", vos[i].getAttributeValue("ndelivernum"));
        dmdvos[i].setAttributeValue("pk_delivdaypl", vos[i].getAttributeValue("pk_delivdaypl"));
        dmdvos[i].setAttributeValue("pkcorp", vos[i].getAttributeValue("pkparamcorp"));
        dmdvos[i].setAttributeValue("pksalecorp", vos[i].getAttributeValue("pksalecorp"));
        dmdvos[i].setAttributeValue("pksendstoreorg", vos[i].getAttributeValue("pksendstockorg"));
        dmdvos[i].setAttributeValue("pksendstore", vos[i].getAttributeValue("pksendstore"));
        dmdvos[i].setAttributeValue("vbatchcode", vos[i].getAttributeValue("vbatchcode"));
        dmdvos[i].setAttributeValue("userid", vos[i].getAttributeValue("userid"));
        dmdvos[i].setAttributeValue("snddate", vos[i].getAttributeValue("snddate"));
        dmdvos[i].setAttributeValue("pkinv", vos[i].getAttributeValue("pkinv"));
        dmdvos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("nfeedbacknum"));
        dmdvos[i].setAttributeValue("vbilltype", vos[i].getAttributeValue("vbilltype"));
        dmdvos[i].setAttributeValue("pkdelivorg", vos[i].getAttributeValue("pkdelivorg"));
        dmdvos[i].setAttributeValue("iplanstatus", vos[i].getAttributeValue("iplanstatus"));
        dmdvos[i].setAttributeValue("pkbillh", vos[i].getAttributeValue("pkbillh"));
        dmdvos[i].setAttributeValue("pkbillb", vos[i].getAttributeValue("pkbillb"));
        dmdvos[i].setAttributeValue("idelivsequence", vos[i].getAttributeValue("idelivsequence"));
        dmdvos[i].setAttributeValue("dnum", vos[i].getAttributeValue("dnum"));

        dmdvos[i].setAttributeValue("sendnum", vos[i].getAttributeValue("ndelivernum"));
        for (int w = 1; w <= 10; w++) {
          dmdvos[i].setAttributeValue("vfree" + w, vos[i].getAttributeValue("vfree" + w));
        }

        dmdvos[i].setStatus(1);

        UFDouble ufdOldSendNum = new UFDouble(vos[i].getAttributeValue("dsendnumold") == null ? "0" : vos[i].getAttributeValue("dsendnumold").toString());

        UFDouble ufdNewSendNum = new UFDouble(vos[i].getAttributeValue("ndelivernum") == null ? "0" : vos[i].getAttributeValue("ndelivernum").toString());

        UFDouble ufdEndSendNum = ufdOldSendNum.add(ufdNewSendNum);
        dmdvos[i].setAttributeValue("dsendnum", ufdEndSendNum);

        UFDouble ufdDplNum = new UFDouble(vos[i].getAttributeValue("dnum").toString()).multiply(scale).div(100.0D);
        if (ufdEndSendNum.compareTo(ufdDplNum) > 0) {
          throw new Exception(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000092"));
        }
        if ((!isAutoClose) || 
          (!vos[i].getAttributeValue("idelivsequence").toString().equals("0")) || (!vos[i].getAttributeValue("iplanstatus").toString().equals(DailyPlanStatus.Audit)) || (ufdEndSendNum.compareTo(new UFDouble(vos[i].getAttributeValue("dnum").toString())) < 0)) {
          continue;
        }
        dmdvos[i].setAttributeValue("iplanstatus", DailyPlanStatus.End);
      }

      ArrayList sovosArray = new ArrayList();
      for (int i = 0; i < vos.length; i++) {
        if ((dmdvos[i].getAttributeValue("vbilltype") != null) && (
          dmdvos[i].getAttributeValue("vbilltype").toString().trim().equals("30")));
        sovosArray.add(dmdvos[i]);
      }

      DMDataVO[] so_vos = (DMDataVO[])sovosArray.toArray(new DMDataVO[0]);

      setSendNumEndFree(so_vos);

      DmToSoDMO dmo2 = new DmToSoDMO();
      for (int i = 0; i < so_vos.length; i++) {
        so_vos[i].setAttributeValue("ndelivernum", dmdvos[i].getAttributeValue("sendnum"));
      }

      dmo2.setSendNumToSO(so_vos);

      ParaRewriteVO[] prvo = getParaRewriteVO(vos, "ndelivernum");

      DmFromTODMO boNBDB = new DmFromTODMO();
      for (int i = 0; (prvo != null) && (i < prvo.length); i++) {
        boNBDB.backDMToOrder(prvo[i], new Integer(0));
      }

      ParaDelivToPoRewriteVO voPara = DMDelivdayplVOTool.getDelivWritePOData(vos);
      if (voPara != null) {
        DmFromPUDMO orderBO = new DmFromPUDMO();
        orderBO.rewriteDelivNum(voPara);
      }


      save(dmdvos,new ValueRangeHashtableDeliverydailyplan(),false);

      unLockPKs(sAllKeys, dmdvos[0].getAttributeValue("userid").toString());
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally {
      if (dmdvos != null)
        unLockPKs(sAllKeys, dmdvos[0].getAttributeValue("userid").toString());
    }
  }

  public void setSendNumEndFree(DMDataVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return;
    try {
      ArrayList alendvo = new ArrayList();
      for (int i = 0; i < vos.length; i++)
      {
        if (!vos[i].getAttributeValue("iplanstatus").toString().equals(DailyPlanStatus.End))
          continue;
        alendvo.add(vos[i]);
      }

      DMDataVO[] dmdvosEnd = (DMDataVO[])alendvo.toArray(new DMDataVO[0]);
      endFreeDelivplanNum(dmdvosEnd);
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public void setSignAndBackNum(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      DmFromTODMO boNBDB = new DmFromTODMO();

      ParaRewriteVO[] prvo = getParaRewriteVO(vos, "nsignnum");
      for (int i = 0; (prvo != null) && (i < prvo.length); i++) {
        boNBDB.backDMToOrder(prvo[i], new Integer(1));
      }

      prvo = getParaRewriteVO(vos, "nbacknum");
      for (int i = 0; (prvo != null) && (i < prvo.length); i++) {
        boNBDB.backDMToOrder(prvo[i], new Integer(3));
      }

      DmToSoDMO dmo2 = new DmToSoDMO();
      dmo2.setSignAndBackNumToSO(vos);

      DMDataVO[] dmdvos = new DMDataVO[vos.length];
      for (int i = 0; i < vos.length; i++) {
        dmdvos[i] = new DMDataVO();
        dmdvos[i].setAttributeValue("dsignnum", vos[i].getAttributeValue("nsignnum"));
        dmdvos[i].setAttributeValue("dbacknum", vos[i].getAttributeValue("nbacknum"));
        dmdvos[i].setAttributeValue("pk_delivdaypl", vos[i].getAttributeValue("pk_delivdaypl"));
        dmdvos[i].setAttributeValue("pkcorp", "1001");
        dmdvos[i].setStatus(1);
      }

      ValueRangeHashtableDeliverydailyplan vht = new ValueRangeHashtableDeliverydailyplan();
      ValueRange v1 = (ValueRange)vht.get("dsignnum");
      v1.setIsProgressive(true);
      vht.putValueRange(v1);
      ValueRange v2 = (ValueRange)vht.get("dbacknum");
      v2.setIsProgressive(true);
      vht.putValueRange(v2);

      save(dmdvos, vht, false);
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public void deliverydaily_setWayLossNum(DMDataVO[] dmdatavo)
    throws BusinessException
  {
    if ((dmdatavo == null) || (dmdatavo.length == 0)) {
      return;
    }

    DMVO vo = new DMVO(dmdatavo.length);
    vo.setStatusTo(1);

    String[] sPlanPK = new String[dmdatavo.length];

    for (int i = 0; i < dmdatavo.length; i++) {
      vo.getChildrenVO()[i].setAttributeValue("dwaylossnum", dmdatavo[i].getAttributeValue("dwaylossnum"));
      vo.getChildrenVO()[i].setAttributeValue("pk_delivdaypl", dmdatavo[i].getAttributeValue("pk_delivdaypl"));
      vo.getChildrenVO()[i].setAttributeValue("vbilltype", dmdatavo[i].getAttributeValue("vbilltype"));
      vo.getChildrenVO()[i].setAttributeValue("pkcorp", dmdatavo[i].getAttributeValue("pkcorp"));

      vo.getChildrenVO()[i].setAttributeValue("pksalecorp", dmdatavo[i].getAttributeValue("pksalecorp"));

      sPlanPK[i] = ((String)dmdatavo[i].getAttributeValue("pk_delivdaypl"));
    }
    try
    {
      StringBuffer sb = new StringBuffer();
      sb.append("select pk_delivdaypl,pkbillb,pkbillh from dm_delivdaypl where ").append(new DMDataVO().getStrPKs("pk_delivdaypl", sPlanPK));

      DMDataVO[] dmdvosPK = super.query(sb);
      DMVO dmvoTemp = new DMVO();
      dmvoTemp.setChildrenVO(dmdvosPK);
      vo.combineOtherVOByPK(dmvoTemp, "pk_delivdaypl");

      ValueRangeHashtableDeliverydailyplan htRangeBody = new ValueRangeHashtableDeliverydailyplan();
      ValueRange v1 = (ValueRange)htRangeBody.get("dwaylossnum");
      v1.setIsProgressive(true);
      htRangeBody.putValueRange(v1);

      super.save(null, "dm_delivdaypl", vo, null, htRangeBody, false);
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  protected void signButtonClickTime(String sOperateType, CircularlyAccessibleValueObject vo, boolean bIsUnDo)
    throws BusinessException
  {
    String sBillType = DMBillTypeConst.m_delivDelivDayPl;
    String sSourceFieldKeyInBodyVO = "pkbillh";

    String sBillID = vo.getAttributeValue("pk_delivdaypl") == null ? null : vo.getAttributeValue("pk_delivdaypl").toString();
    String sCorpID = vo.getAttributeValue("pkcorp") == null ? null : vo.getAttributeValue("pkcorp").toString();
    UFDateTime sBillTime = vo.getAttributeValue("billnewaddtime") == null ? null : (UFDateTime)vo.getAttributeValue("billnewaddtime");
    try
    {
      DelivbillHDMO dmo = new DelivbillHDMO();
      dmo.signButtonClickTimeVO(sOperateType, sBillTime, sBillType, sBillID, sCorpID, vo, sSourceFieldKeyInBodyVO, bIsUnDo);
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  protected void signButtonClickTime2(DMDataVO[] dmdvos)
    throws BusinessException
  {
    if ((dmdvos != null) && (dmdvos.length > 0))
    {
      DelivbillHDMO dmo = null;
      try {
        dmo = new DelivbillHDMO();
      }
      catch (Exception e) {
        throw new BusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000093")));
      }

      String sBillType = DMBillTypeConst.m_delivDelivDayPl;
      String sSourceFieldKeyInBodyVO = "pkbillh";
      String sBillID = null;
      String sCorpID = null;
      UFDateTime sBillTime = null;

      for (int i = 0; i < dmdvos.length; i++) {
        sBillID = dmdvos[i].getAttributeValue("pk_delivdaypl") == null ? null : dmdvos[i].getAttributeValue("pk_delivdaypl").toString();

        sCorpID = dmdvos[i].getAttributeValue("pkcorp") == null ? null : dmdvos[i].getAttributeValue("pkcorp").toString();
        sBillTime = dmdvos[i].getAttributeValue("billnewaddtime") == null ? null : (UFDateTime)dmdvos[i].getAttributeValue("billnewaddtime");

        if (dmdvos[i].getStatus() == 2) {
          try {
            dmo.signButtonClickTimeVO(RecordType.SAVESEND, sBillTime, sBillType, sBillID, sCorpID, dmdvos[i], sSourceFieldKeyInBodyVO, false);
          }
          catch (Exception e)
          {
            reportException(e);
            if ((e instanceof BusinessException)) {
              throw ((BusinessException)e);
            }
            throw new BusinessException(e);
          }
        }

        if (dmdvos[i].getStatus() != 3)
          continue;
        try {
          dmo.signButtonClickTimeVO(RecordType.SAVESEND, sBillTime, sBillType, sBillID, sCorpID, dmdvos[i], sSourceFieldKeyInBodyVO, true);
        }
        catch (Exception e)
        {
          reportException(e);
          if ((e instanceof BusinessException)) {
            throw ((BusinessException)e);
          }
          throw new BusinessException(e);
        }
      }
    }
  }

  protected void signButtonClickTimeS(CircularlyAccessibleValueObject[] vos, boolean bIsUnDo)
    throws BusinessException, BusinessException
  {
    if ((vos != null) && (vos.length > 0))
    {
      DelivbillHDMO dmo = null;
      try {
        dmo = new DelivbillHDMO();
      }
      catch (Exception e) {
        throw new BusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000093")));
      }

      String sBillType = DMBillTypeConst.m_delivDelivDayPl;
      String sSourceFieldKeyInBodyVO = "pkbillh";
      String sBillID = null;
      String sCorpID = null;
      UFDateTime sBillTime = null;
      DMDataVO[] dmdvos = new DMDataVO[vos.length];
      for (int i = 0; i < vos.length; i++) {
        sBillID = vos[i].getAttributeValue("pk_delivdaypl") == null ? null : vos[i].getAttributeValue("pk_delivdaypl").toString();

        sCorpID = vos[i].getAttributeValue("pkcorp") == null ? null : vos[i].getAttributeValue("pkcorp").toString();
        sBillTime = vos[i].getAttributeValue("billnewaddtime") == null ? null : (UFDateTime)vos[i].getAttributeValue("billnewaddtime");

        dmdvos[i] = new DMDataVO();
        dmdvos[i].setAttributeValue(TimeAttributeName.TIME, sBillTime);
        dmdvos[i].setAttributeValue(TimeAttributeName.BILLTYPE, sBillType);
        dmdvos[i].setAttributeValue(TimeAttributeName.BILLID, sBillID);
        dmdvos[i].setAttributeValue(TimeAttributeName.PKCORP, sCorpID);
        dmdvos[i].setAttributeValue(TimeAttributeName.FIRSTBILLID, vos[i].getAttributeValue(sSourceFieldKeyInBodyVO));
        if ((sBillType != null) && (sBillType.trim().length() != 0) && (sBillID != null) && (sBillID.trim().length() != 0) && (sCorpID != null) && (sCorpID.trim().length() != 0))
          continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000094"));
      }

      try
      {
        dmo.signButtonClickTimeVOS(RecordType.SAVESEND, dmdvos, false);
      }
      catch (Exception e) {
        reportException(e);
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e);
      }
    }
  }

  public OutbillVO[] splitVOForICOutBill(OutbillVO[] vos) throws BusinessException
  {
    OutbillVO[] resultVOs = (OutbillVO[])null;
    String pk_corp = null;
    try {
      DelivbillHDMO pubDmo = new DelivbillHDMO();

      String splitMode = "仓库";
      Hashtable htForSplitMode = new Hashtable();
      for (int i = 0; i < vos.length; i++) {
        pk_corp = vos[i].getChildrenVO()[0].getAttributeValue("pksalecorp").toString();
        if (htForSplitMode.containsKey(pk_corp)) {
          continue;
        }
        try
        {
          splitMode = getICParaString(pk_corp, new String[] { "IC035" })[0].trim();
        }
        catch (Exception e) {
          splitMode = "仓库";
        }

        htForSplitMode.put(pk_corp, splitMode);
      }

      Hashtable table = null;

      OutbillHHeaderVO[] items = (OutbillHHeaderVO[])null;

      if (splitMode.equalsIgnoreCase("仓库")) {
        resultVOs = (OutbillVO[])SplitBillVOs.getSplitVOs("nc.vo.dm.dm102.OutbillVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.dm102.OutbillHHeaderVO", vos, null, new String[] { "creceiptcorpid", "pkcust", "pkbillh", "pkdeststoreorg", "pksalecorp", "pksendstoreorg", "pkoprdepart", "vbilltype", "pksendtype", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "vuserdef10", "vuserdef11", "vuserdef12", "vuserdef13", "vuserdef14", "vuserdef15", "vuserdef16", "vuserdef17", "vuserdef18", "vuserdef19", "pk_defdoc19", "pk_defdoc18", "pk_defdoc17", "pk_defdoc16", "pk_defdoc15", "pk_defdoc14", "pk_defdoc13", "pk_defdoc12", "pk_defdoc11", "pk_defdoc10", "pk_defdoc9", "pk_defdoc8", "pk_defdoc7", "pk_defdoc6", "pk_defdoc5", "pk_defdoc4", "pk_defdoc3", "pk_defdoc2", "pk_defdoc1", "pk_defdoc0", "pkstroe", "pkdeststore" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+保管员")) {
        String cinvid = null;
        String cwarehouseid = null;

        DmFromIcDMO storedmo = new DmFromIcDMO();

        for (int i = 0; i < vos.length; i++) {
          items = (OutbillHHeaderVO[])vos[i].getChildrenVO();
          for (int j = 0; j < items.length; j++) {
            cinvid = items[j].getAttributeValue("pkinv").toString();
            cwarehouseid = items[j].getAttributeValue("pkstroe").toString();
            if ((cwarehouseid != null) && (cwarehouseid.trim() != "")) {
              items[j].setAttributeValue("pkwhmanager", storedmo.getWHManager(pk_corp, null, cwarehouseid, cinvid));
            }

          }

        }

        resultVOs = (OutbillVO[])SplitBillVOs.getSplitVOs("nc.vo.dm.dm102.OutbillVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.dm102.OutbillHHeaderVO", vos, null, new String[] { "creceiptcorpid", "pkcust", "pkbillh", "pkdeststoreorg", "pksalecorp", "pksendstoreorg", "pkoprdepart", "vbilltype", "pksendtype", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "vuserdef10", "vuserdef11", "vuserdef12", "vuserdef13", "vuserdef14", "vuserdef15", "vuserdef16", "vuserdef17", "vuserdef18", "vuserdef19", "pk_defdoc19", "pk_defdoc18", "pk_defdoc17", "pk_defdoc16", "pk_defdoc15", "pk_defdoc14", "pk_defdoc13", "pk_defdoc12", "pk_defdoc11", "pk_defdoc10", "pk_defdoc9", "pk_defdoc8", "pk_defdoc7", "pk_defdoc6", "pk_defdoc5", "pk_defdoc4", "pk_defdoc3", "pk_defdoc2", "pk_defdoc1", "pk_defdoc0", "pkstroe", "pkwhmanager", "pkdeststore" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+按单品")) {
        resultVOs = (OutbillVO[])SplitBillVOs.getSplitVOs("nc.vo.dm.dm102.OutbillVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.dm102.OutbillHHeaderVO", vos, null, new String[] { "creceiptcorpid", "pkcust", "pksalecorp", "pkbillh", "pkdeststoreorg", "pksendstoreorg", "pkoprdepart", "vbilltype", "pkinv", "pksendtype", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "vuserdef10", "vuserdef11", "vuserdef12", "vuserdef13", "vuserdef14", "vuserdef15", "vuserdef16", "vuserdef17", "vuserdef18", "vuserdef19", "pk_defdoc19", "pk_defdoc18", "pk_defdoc17", "pk_defdoc16", "pk_defdoc15", "pk_defdoc14", "pk_defdoc13", "pk_defdoc12", "pk_defdoc11", "pk_defdoc10", "pk_defdoc9", "pk_defdoc8", "pk_defdoc7", "pk_defdoc6", "pk_defdoc5", "pk_defdoc4", "pk_defdoc3", "pk_defdoc2", "pk_defdoc1", "pk_defdoc0", "pkstroe", "pkdeststore" });
      }
      else
      {
        Vector vBaseids = new Vector();
        String cinvid = null;
        String[] cinvids = (String[])null;

        for (int i = 0; i < vos.length; i++) {
          items = (OutbillHHeaderVO[])vos[i].getChildrenVO();
          for (int j = 0; j < items.length; j++) {
            cinvid = items[j].getAttributeValue("pkinv").toString();
            if (!vBaseids.contains(cinvid))
              vBaseids.addElement(cinvid);
          }
        }
        if (vBaseids.size() > 0) {
          cinvids = new String[vBaseids.size()];
          vBaseids.copyInto(cinvids);

          table = pubDmo.fetchArrayValue("bd_invbasdoc inner join bd_invmandoc on bd_invbasdoc.pk_invbasdoc=bd_invmandoc.pk_invbasdoc", "pk_invcl", "bd_invmandoc.pk_invmandoc", "bd_invmandoc.pk_invmandoc", cinvids);

          for (int i = 0; i < vos.length; i++) {
            items = (OutbillHHeaderVO[])vos[i].getChildrenVO();
            for (int j = 0; j < items.length; j++) {
              cinvid = items[j].getAttributeValue("pkinv").toString();
              if (vBaseids.contains(cinvid))
                items[j].setAttributeValue("pkinvsort", (String)table.get(cinvid));
            }
          }
        }
        resultVOs = (OutbillVO[])SplitBillVOs.getSplitVOs("nc.vo.dm.dm102.OutbillVO", "nc.vo.dm.pub.DMDataVO", "nc.vo.dm.dm102.OutbillHHeaderVO", vos, null, new String[] { "creceiptcorpid", "pkcust", "pkbillh", "pkdeststoreorg", "pksalecorp", "pksendstoreorg", "pkoprdepart", "vbilltype", "pksendtype", "vuserdef0", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", "vuserdef10", "vuserdef11", "vuserdef12", "vuserdef13", "vuserdef14", "vuserdef15", "vuserdef16", "vuserdef17", "vuserdef18", "vuserdef19", "pk_defdoc19", "pk_defdoc18", "pk_defdoc17", "pk_defdoc16", "pk_defdoc15", "pk_defdoc14", "pk_defdoc13", "pk_defdoc12", "pk_defdoc11", "pk_defdoc10", "pk_defdoc9", "pk_defdoc8", "pk_defdoc7", "pk_defdoc6", "pk_defdoc5", "pk_defdoc4", "pk_defdoc3", "pk_defdoc2", "pk_defdoc1", "pk_defdoc0", "pkstroe", "pkinvsort", "pkdeststore" });
      }

    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000095"), e);
    }

    return resultVOs;
  }

  public DMDataVO[] unAuditDeliverydailyplan(DMDataVO[] vos)
    throws BusinessException
  {
    String[] sAllKeys = (String[])null;
    try
    {
      sAllKeys = checkLockPKs(vos);

      saveCheck(vos);

      checkLowerBillExist("pk_delivdaypl", "vdelivdayplcode", new String[] { "ic_general_b" }, new String[] { "csourcebillbid" }, new String[] { "ts" }, vos);

      checkLowerBillExist("pk_delivdaypl", "vdelivdayplcode", new String[] { "dm_delivbill_b" }, new String[] { "pkdayplan" }, new String[] { "ts" }, vos);

      for (int i = 0; i < vos.length; i++)
      {
        signButtonClickTime(RecordType.AUDITSEND, vos[i], true);
      }

      DMDataVO[] arrayOfDMDataVO = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);
      return arrayOfDMDataVO;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
  }

  private String delAndCloseCheck(String[] sSourceBillRowPKs, boolean isDelCheck)
    throws BusinessException, BusinessException
  {
    try
    {
      StringBuffer sbfPlanExist = new StringBuffer();
      sbfPlanExist.append("select count(dm_delivdaypl.pk_delivdaypl) as num from dm_delivdaypl where dm_delivdaypl.dr =0 and dm_delivdaypl.pkbillb in (" + StringTools.getStringFromStrings(sSourceBillRowPKs) + ") and (dm_delivdaypl.iplanstatus <> " + DailyPlanStatus.End + ")");

      DMDataVO[] voscount = super.query(sbfPlanExist);

      if (Integer.parseInt(voscount[0].getAttributeValue("num").toString()) == 0) {
        if (isDelCheck) {
          return "return";
        }

        return null;
      }

      return NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000096");
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public String delAndCloseInter(String[] sSourceBillRowPKs, String sUserID, String sPkcorp, boolean bIsDelAndClose)
    throws BusinessException
  {
    try
    {
      if ((sSourceBillRowPKs == null) || (sSourceBillRowPKs.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000097"));
      }
      if ((sUserID == null) || (sUserID.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000098"));
      }
      if ((sPkcorp == null) || (sPkcorp.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000099"));
      }

      if (!bIsDelAndClose) {
        return delAndCloseCheck(sSourceBillRowPKs, false);
      }

      String sCheckMessage = delAndCloseCheck(sSourceBillRowPKs, true);
      if ((sCheckMessage != null) && (sCheckMessage.equals("return"))) {
        return null;
      }
      ArrayList alDelvos = new ArrayList();
      ArrayList alEndvos = new ArrayList();

      DMDataVO[] allplanvos = query2(" dm_delivdaypl.pkbillb in (" + StringTools.getStringFromStrings(sSourceBillRowPKs) + ") and (dm_delivdaypl.iplanstatus <> " + DailyPlanStatus.End + ") ", null);

      for (int i = 0; i < allplanvos.length; i++) {
        if (allplanvos[i].getAttributeValue("iplanstatus").toString().equals(DailyPlanStatus.Free)) {
          allplanvos[i].setStatus(3);
          allplanvos[i].setAttributeValue("userid", sUserID);
          allplanvos[i].setAttributeValue("pkcorp", sPkcorp);
          alDelvos.add(allplanvos[i]);
        }
        else {
          allplanvos[i].setStatus(1);
          allplanvos[i].setAttributeValue("iplanstatus", new Integer(DailyPlanStatus.End));
          allplanvos[i].setAttributeValue("userid", sUserID);
          allplanvos[i].setAttributeValue("pkcorp", sPkcorp);
          alEndvos.add(allplanvos[i]);
        }

      }

      if (alDelvos.size() > 0) {
        DMDataVO[] planDelVos = (DMDataVO[])alDelvos.toArray(new DMDataVO[0]);
        DMDataVO[] ordervos = DMDelivdayplVOTool.getWriteOrderData(planDelVos);
        saveAndWriteAndDelInterface(ordervos, planDelVos, true);
      }

      if (alEndvos.size() > 0) {
        DMDataVO[] planEndVos = (DMDataVO[])alEndvos.toArray(new DMDataVO[0]);
        end(planEndVos, true);
      }
      return null;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private String delCheck(String sSourceBillPK, boolean isDelCheck)
    throws BusinessException, BusinessException
  {
    try
    {
      StringBuffer sbfPlanExist = new StringBuffer();
      sbfPlanExist.append("select count(dm_delivdaypl.pk_delivdaypl) as num from dm_delivdaypl where dm_delivdaypl.dr =0 and dm_delivdaypl.pkbillh = '" + sSourceBillPK + "'");

      DMDataVO[] voscount = super.query(sbfPlanExist);

      if (Integer.parseInt(voscount[0].getAttributeValue("num").toString()) == 0) {
        if (isDelCheck) {
          return "return";
        }

        return null;
      }

      StringBuffer sbf = new StringBuffer();
      sbf.append("select count(dm_delivdaypl.pk_delivdaypl) as num from dm_delivdaypl ");
      sbf.append("where dm_delivdaypl.dr =0 and dm_delivdaypl.pkbillh = '");
      sbf.append(sSourceBillPK);
      sbf.append("' and dm_delivdaypl.iplanstatus > " + DailyPlanStatus.Free);
      DMDataVO[] vos = super.query(sbf);
      if (Integer.parseInt(vos[0].getAttributeValue("num").toString()) > 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000100"));
      }
      return NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000101");
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  public String delInterface(String sSourceBillPK, String sUserID, String sPkcorp, boolean bIsDel)
    throws BusinessException, BusinessException
  {
    try
    {
      long s = System.currentTimeMillis();
      showMethodTime("delinterSTART", s);

      if ((sSourceBillPK == null) || (sSourceBillPK.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000097"));
      }
      if ((sUserID == null) || (sUserID.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000098"));
      }

      if (!bIsDel) {
        return delCheck(sSourceBillPK, false);
      }

      String sCheckMessage = delCheck(sSourceBillPK, true);
      showMethodTime("删除前校验", s);
      if ((sCheckMessage != null) && (sCheckMessage.equals("return"))) {
        return null;
      }

      DMDataVO[] planvos = (DMDataVO[])null;
      DMDataVO[] ordervos = (DMDataVO[])null;
      planvos = query2(" dm_delivdaypl.pkbillh = '" + sSourceBillPK + "' and dm_delivdaypl.iplanstatus = 0 ", null);
      showMethodTime("查询未审批的日计划", s);
      if (planvos.length > 0) {
        for (int i = 0; i < planvos.length; i++) {
          planvos[i].setStatus(3);
          planvos[i].setAttributeValue("userid", sUserID);
          planvos[i].setAttributeValue("pkcorp", sPkcorp);
        }

        ordervos = DMDelivdayplVOTool.getWriteOrderData(planvos);
        showMethodTime("回写数据准备", s);
        saveAndWriteAndDelInterface(ordervos, planvos, true);
        showMethodTime("删除回写", s);
        showMethodTime("delinterEND", s);
      }
      return null;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }

  private DMDataVO[] end(DMDataVO[] vos, boolean bIsEndInterface)
    throws BusinessException
  {
    String[] sAllKeys = (String[])null;
    try
    {
      if (bIsEndInterface) {
        sAllKeys = checkLockPKsForDelInterface(vos);
      }
      else {
        sAllKeys = checkLockPKs(vos);
      }

      saveCheck(vos);

      DeliverydailyplanDMO dmo = new DeliverydailyplanDMO();
      if (dmo.isOutBillExist(vos)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40140404", "UPP40140404-000102"));
      }

      endFreeDelivplanNum(vos);

      DMDataVO[] retvo = save(vos, new ValueRangeHashtableDeliverydailyplan(), true);

      DMVO dvo = new DMVO();
      dvo.setChildrenVO(vos);

      DMATP dmatp = new DMATP();
      dmatp.modifyATPWhenCloseBill(dvo);

      dmatp.checkAtpInstantly(dvo, null);

      DMDataVO[] arrayOfDMDataVO1 = retvo;
      return arrayOfDMDataVO1;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    finally
    {
      unLockPKs(sAllKeys, vos[0].getAttributeValue("userid").toString());
    }
  }

  private void endFreeDelivplanNum(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      Hashtable htDelivOrg = new Hashtable();
      for (int i = 0; i < vos.length; i++) {
        String sDelivorgID = vos[i].getAttributeValue("pkdelivorg").toString();
        htDelivOrg.put(sDelivorgID, "");
      }

      Hashtable htDelivOrgvo = queryDelivOrgsHtvo(htDelivOrg, "DM016");

      ArrayList alSO = new ArrayList();
      DMDataVO votemp = null;
      for (int i = 0; i < vos.length; i++) {
        votemp = (DMDataVO)htDelivOrgvo.get(vos[i].getAttributeValue("pkdelivorg").toString());

        if (("Y".equals(votemp.getAttributeValue("DM016").toString())) && (vos[i].getAttributeValue("vbilltype").equals("30")))
        {
          UFDouble ufdNumReWrite = new UFDouble(0);

          UFDouble ufdNumOld = new UFDouble(vos[i].getAttributeValue("nfeedbacknum") == null ? "0.0" : vos[i].getAttributeValue("nfeedbacknum").toString());

          UFDouble ufdNumNew = null;

          if (votemp.getAttributeValue("idelivsequence").toString().equals("1"))
          {
            ufdNumNew = new UFDouble(vos[i].getAttributeValue("doutnum") == null ? "0.0" : vos[i].getAttributeValue("doutnum").toString());

            ufdNumReWrite = ufdNumNew.sub(ufdNumOld);

            vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("doutnum"));
          }
          else if (votemp.getAttributeValue("idelivsequence").toString().equals("0"))
          {
            ufdNumNew = new UFDouble(vos[i].getAttributeValue("dsendnum") == null ? "0.0" : vos[i].getAttributeValue("dsendnum").toString());

            ufdNumReWrite = ufdNumNew.sub(ufdNumOld);

            vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dsendnum"));
          }
          vos[i].setAttributeValue("ndelivernum", ufdNumReWrite);
          alSO.add(vos[i]);
        }
        else
        {
          vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dnum"));
        }

      }

      if (alSO.size() > 0)
      {
        DMDataVO[] sovos = (DMDataVO[])alSO.toArray(new DMDataVO[0]);
        for (int i = 0; i < sovos.length; i++) {
          sovos[i].setAttributeValue("iplanstatus", DailyPlanStatus.Free);
        }
        modifyATP(sovos);
        for (int i = 0; i < sovos.length; i++) {
          sovos[i].setAttributeValue("iplanstatus", DailyPlanStatus.End);
        }

        DmToSoDMO dmo2 = new DmToSoDMO();
        dmo2.setDeliverNumToSO(sovos);
      }
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  private void reOpenFreeDelivplanNum(DMDataVO[] vos)
    throws BusinessException
  {
    try
    {
      Hashtable htDelivOrg = new Hashtable();
      for (int i = 0; i < vos.length; i++) {
        String sDelivorgID = vos[i].getAttributeValue("pkdelivorg").toString();
        htDelivOrg.put(sDelivorgID, "");
      }
      Hashtable htDelivOrgvo = queryDelivOrgsHtvo(htDelivOrg, "DM016");

      ArrayList alSO = new ArrayList();
      for (int i = 0; i < vos.length; i++) {
        if ((((DMDataVO)htDelivOrgvo.get(vos[i].getAttributeValue("pkdelivorg").toString())).getAttributeValue("DM016").toString().equals("Y")) && (vos[i].getAttributeValue("vbilltype").toString().equals("30")))
        {
          UFDouble ufdNumReWrite = new UFDouble(0);
          UFDouble ufdNumOld = new UFDouble(vos[i].getAttributeValue("nfeedbacknum") == null ? "0.0" : vos[i].getAttributeValue("nfeedbacknum").toString());

          UFDouble ufdNumNew = new UFDouble(vos[i].getAttributeValue("dnum") == null ? "0.0" : vos[i].getAttributeValue("dnum").toString());

          ufdNumReWrite = ufdNumNew.sub(ufdNumOld);
          vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dnum"));
          vos[i].setAttributeValue("ndelivernum", ufdNumReWrite);
          alSO.add(vos[i]);
        }
        else {
          vos[i].setAttributeValue("nfeedbacknum", vos[i].getAttributeValue("dnum"));
        }
      }

      if (alSO.size() > 0) {
        DMDataVO[] sovos = (DMDataVO[])alSO.toArray(new DMDataVO[0]);
        for (int i = 0; i < sovos.length; i++) {
          sovos[i].setAttributeValue("iplanstatus", DailyPlanStatus.Free);
        }
        modifyATP(sovos);
        for (int i = 0; i < sovos.length; i++) {
          sovos[i].setAttributeValue("iplanstatus", DailyPlanStatus.Audit);
        }

        DmToSoDMO dmo2 = new DmToSoDMO();
        dmo2.setDeliverNumToSO(sovos);
      }
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
  }

  public String getParentCorpCode(String key) {
    String ParentCorp = new String();
    CorpBO corpbo = new CorpBO();
    try {
      CorpVO corpVO = corpbo.findByPrimaryKey(key);
      ParentCorp = corpVO.fathercorp;
      CorpVO f_corpVO = corpbo.findByPrimaryKey(ParentCorp);
      ParentCorp = f_corpVO.unitcode;
    }
    catch (BusinessException e) {
      e.printStackTrace();
    }
    return ParentCorp;
  }

  /**
   * FOR:5Ito7D 
   * @author shikun 2014-10-30 
   * bd_delivorg.idelivsequence = 0
   * */
  private HashMap queryAllDelivorgbyvbilltype() throws BusinessException {
    try {
      StringBuffer sbSql = new StringBuffer();

      sbSql.append("SELECT bd_delivorg.pk_delivorg, bd_doagent.pkcorp, ");
      sbSql.append("bd_doagent.pksendstoreorg,bd_doagent.ibizattribute ");
      sbSql.append("FROM bd_delivorg ");
      sbSql.append("inner join bd_doagent ");
      sbSql.append("on bd_delivorg.pk_delivorg = bd_doagent.pkdelivorg ");
      sbSql.append("WHERE bd_delivorg.idelivsequence = 0 and bd_delivorg.dr = 0 and bd_doagent.dr =0  and (bd_doagent.bseal is null or bd_doagent.bseal = 'N') ");

      DMDataVO[] ddvos = super.query(sbSql);

      HashMap hmResult = new HashMap();
      for (int i = 0; i < ddvos.length; i++) {
        String sCorpID = (String)ddvos[i].getAttributeValue("pkcorp");
        String sStoreorgID = (String)ddvos[i].getAttributeValue("pksendstoreorg");
        int iBizattribute = Integer.parseInt(ddvos[i].getAttributeValue("ibizattribute").toString());
        String sID = getDelivorgSID(sCorpID, sStoreorgID, iBizattribute);
        hmResult.put(sID, ddvos[i].getAttributeValue("pk_delivorg").toString());
      }
      return hmResult;
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
    }
    return null;
  }
  
  /**
   * FOR:5Ito7D 
   * @author shikun 2014-10-30 
   * bd_delivorg.idelivsequence = 0
   * */
  private String queryDefaultDelivorgby5I(String sCorpID, String sAgentStoreOrgID, int ibizattribute) throws BusinessException {
  try {
    StringBuffer sbSql = new StringBuffer();

    sbSql.append("SELECT bd_delivorg.pk_delivorg, bd_doagent.pkcorp, ");
    sbSql.append("COALESCE(bd_doagent.pksendstoreorg, ' ')  AS pksendstoreorg ");
    sbSql.append("FROM bd_delivorg ");
    sbSql.append("inner join bd_doagent ");
    sbSql.append("on bd_delivorg.pk_delivorg = bd_doagent.pkdelivorg ");
    sbSql.append("WHERE bd_delivorg.idelivsequence = 0 and bd_delivorg.dr = 0 and bd_doagent.dr =0  and bd_doagent.pkcorp = '" + sCorpID + "' and (bd_doagent.bseal is null or bd_doagent.bseal = 'N') ");

    sbSql.append("and (bd_doagent.pksendstoreorg = '" + sAgentStoreOrgID + "'  or bd_doagent.pksendstoreorg is null) ");
    switch (ibizattribute)
    {
    case 0:
      sbSql.append("and ibizattribute=0 ");
      break;
    case 1:
      sbSql.append("and ibizattribute<>2 ");
      break;
    case 2:
      sbSql.append("and ibizattribute<>1 ");
    }

    sbSql.append("order by pksendstoreorg DESC ");

    DMDataVO[] ddvos = super.query(sbSql);

    String sDelivOrg = null;
    if ((ddvos != null) && (ddvos.length > 0)) {
      sDelivOrg = ddvos[0].getAttributeValue("pk_delivorg").toString();
    }
    return sDelivOrg;
  }
  catch (Exception e) {
    reportException(e);
    if ((e instanceof BusinessException))
      throw ((BusinessException)e); 
  }
  return null;
}

}