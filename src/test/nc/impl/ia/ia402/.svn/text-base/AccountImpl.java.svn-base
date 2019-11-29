package nc.impl.ia.ia402;

import java.util.HashMap;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.bs.ia.ia402.AccountDMO;
import nc.bs.ia.ia402.PeriodController;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.LockOperator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.sm.createcorp.CreatecorpBO;
import nc.impl.ia.bill.BillImpl;
import nc.impl.ia.ia306.DataRightImpl;
import nc.impl.ia.ia504.GeneralledgerImpl;
import nc.impl.ia.ia504.MonthledgerImpl;
import nc.impl.ia.pub.CommonDataImpl;
import nc.itf.ia.ia402.IAccount;
import nc.itf.ia.ia402.IAccountQuery;
import nc.itf.ia.ia504.IMonthledger;
import nc.itf.ic.service.IICToIA;
import nc.itf.pu.inter.IPuToIa_PO2IA;
import nc.itf.so.service.ISOToIA;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.ia306.ParamVO308;
import nc.vo.ia.ia402.AccountVO;
import nc.vo.ia.ia504.GeneralledgerVO;
import nc.vo.ia.ia504.MonthledgerVO;
import nc.vo.ia.outter.AccountCheckVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;

public class AccountImpl
  implements IAccountQuery, IAccount
{
  public String insert(AccountVO account)
    throws BusinessException
    {
    try
    {
      AccountDMO dmo = new AccountDMO();
      String key = dmo.insert(account);
      return key;
    } catch (Exception e) {

    throw new BusinessException("AccountBO::insert(AccountVO) Exception!", e);
    }
  }
  
  
  
  
  public String[] insertArray(AccountVO[] accounts) throws BusinessException {

	    try {
	      AccountDMO dmo = new AccountDMO();
	      String[] keys = dmo.insertArray(accounts);
	      return keys;
	    }
	    catch (Exception e) {
	      throw new BusinessException(
	          "AccountBO::insertArray(AccountVO[]) Exception!", e);
	    }
	  }
  
  
  
  


  public void delete(AccountVO vo)
    throws BusinessException
  {
    try
    {
      AccountDMO dmo = new AccountDMO();
      dmo.delete(vo);
    }
    catch (Exception e) {
      throw new BusinessException("AccountBO::delete(AccountPK) Exception!", e);
    }
  }

  public AccountVO[] queryByVO(AccountVO condAccountVO, Boolean isAnd)
    throws BusinessException
  {
    AccountVO[] accounts = (AccountVO[])null;
    try {
      AccountDMO dmo = new AccountDMO();
      accounts = dmo.queryByVO(condAccountVO, isAnd);
    }
    catch (Exception e) {
      throw new BusinessException(
        "AccountBean::queryByVO(AccountVO condAccountVO, Boolean isAnd) Exception!", 
        e);
    }
    return accounts;
  }

  public boolean checkAccount(String sCorpID, String sAccountYear, String sAccountMonth)
    throws BusinessException
  {
    String[][] sQueryResult = (String[][])null;
    try
    {
      CommonDataImpl cbo = new CommonDataImpl();

      boolean bIsAccount = cbo.isBeginAccount(sCorpID).booleanValue();

      if (!bIsAccount) {
        BusinessException be = new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000034"));

        throw be;
      }

      String sUnPeriod = cbo.getUnClosePeriod(sCorpID);
      if (!sUnPeriod.equals(sAccountYear + "-" + sAccountMonth)) {
        String[] value = { 
          sAccountYear + "-" + sAccountMonth, sUnPeriod };

        BusinessException be = new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000035", null, 
          value));
        throw be;
      }

      String sNextPeriod = cbo.getNextPeriod(sCorpID, sAccountYear + "-" + 
        sAccountMonth);
      int iIndex = sNextPeriod.indexOf("-");

      String sNextMonth = sNextPeriod.substring(iIndex + 1);

      if (sNextMonth.equals(ConstVO.m_sLastMonth)) {
        String[] value = { 
          sAccountYear + "-" + sAccountMonth };

        BusinessException be = new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000036", null, 
          value));
        throw be;
      }

      String sStartPeriod = cbo.getStartPeriod(sCorpID);
      String sAccountPeriod = sAccountYear + "-" + sAccountMonth;

      UFDate[] sDates = cbo.getMonthDates(sCorpID, sAccountPeriod);
      String sBeginDate = sDates[0].toString();
      String sEndDate = sDates[1].toString();

      if (sAccountPeriod.compareTo(sStartPeriod) >= 0)
      {
        String sInOutBillType = "('";
        sInOutBillType = sInOutBillType + "I2" + "','";
        sInOutBillType = sInOutBillType + "I3" + "','";
        sInOutBillType = sInOutBillType + "I4" + "','";
        sInOutBillType = sInOutBillType + "I5" + "','";
        sInOutBillType = sInOutBillType + "I6" + "','";
        sInOutBillType = sInOutBillType + "I7" + "','";
        sInOutBillType = sInOutBillType + "IJ" + "','";
        sInOutBillType = sInOutBillType + "II" + "','";
        sInOutBillType = sInOutBillType + "I8" + "','";
        sInOutBillType = sInOutBillType + "ID" + "','";
        sInOutBillType = sInOutBillType + "IC" + "')";

        StringBuffer sSQL = new StringBuffer(" select ");
        sSQL.append(" cinventoryid,vbillcode ");
        sSQL.append(" from ");
        sSQL.append(" v_ia_inoutledger v ");
        sSQL.append(" where ");
        sSQL.append(" v.pk_corp = '" + sCorpID + "'");
        sSQL.append(" and ");
        sSQL.append(" v.dbilldate <= '" + sEndDate + "'");

        sSQL.append(" and ( v.nprice is null or v.fdatagetmodelflag=12 ) ");

        sSQL.append(" and ");
        sSQL.append(" v.cbilltypecode in " + sInOutBillType);
        sSQL.append(" and ");
        sSQL.append(" v.nnumber != 0 ");
        sSQL.append(" and ");
        sSQL.append(" v.iauditsequence > 0");
        sSQL.append(" and caccountyear='" + sAccountYear + 
          "' and caccountmonth='" + sAccountMonth + "' ");
        sSQL.append(" and v.dauditdate <= '" + sEndDate + "'");
        sSQL.append(" and v.dauditdate >= '" + sBeginDate + "'");

        sQueryResult = cbo.queryData(sSQL.toString());

        String sHintString = "";
        for (int i = 0; i < sQueryResult.length; i++) {
          String[] s = sQueryResult[i];

          sSQL = new StringBuffer(" select ");
          sSQL.append(" k.invcode,k.invname ");
          sSQL.append(" from ");
          sSQL.append(" bd_invmandoc j,  bd_invbasdoc k ");
          sSQL.append(" where ");
          sSQL.append(" j.pk_invmandoc = '" + s[0] + "'");
          sSQL.append(" and ");
          sSQL.append(" j.pk_invbasdoc=k.pk_invbasdoc ");
          String[][] sInvResult = cbo.queryData(sSQL.toString());

          sHintString = sHintString + 
            NCLangResOnserver.getInstance().getStrByID("common", 
            "UC000-0001480") + 
            ":" + 
            sInvResult[0][0] + 
            "," + 
            NCLangResOnserver.getInstance().getStrByID("common", 
            "UC000-0001453") + 
            ":" + 
            sInvResult[0][1] + 
            "," + 
            NCLangResOnserver.getInstance().getStrByID("20147020", 
            "UPP20147020-000037") + ":" + s[1] + ");";
        }

        if (sHintString.length() != 0) {
          sHintString = sHintString.substring(0, sHintString.length() - 1);
          String[] value = { 
            sHintString };

          BusinessException be = new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("20147020", 
            "UPP20147020-000038", null, value));

          throw be;
        }

        sSQL = new StringBuffer(" select ");
        sSQL.append(" distinct vbillcode ");
        sSQL.append(" from ");
        sSQL.append(" v_ia_inoutledger v ");
        sSQL.append(" where ");
        sSQL.append(" v.pk_corp = '" + sCorpID + "'");
        sSQL.append(" and ");

        sSQL.append(" v.dbilldate <= '" + sEndDate + "'");
        sSQL.append(" and ");
        sSQL.append(" v.iauditsequence < 0");
        sSQL.append(" and caccountyear='" + sAccountYear + 
          "' and caccountmonth='" + sAccountMonth + "' ");

        sQueryResult = cbo.queryData(sSQL.toString());

        sHintString = "";
        for (int i = 0; i < sQueryResult.length; i++) {
          String[] s = sQueryResult[i];
          sHintString = sHintString + 
            NCLangResOnserver.getInstance().getStrByID("20147020", 
            "UPP20147020-000037") + ":" + s[0] + ";";
        }

        if (sHintString.length() != 0) {
          sHintString = sHintString.substring(0, sHintString.length() - 1);
          String[] value = { 
            sHintString };

          BusinessException be = new BusinessException(
            NCLangResOnserver.getInstance().getStrByID("20147020", 
            "UPP20147020-000039", null, value));

          throw be;
        }
      }
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }

    return true;
  }

  private boolean checkReaccount(String sCorpID, String sAccountYear, String sAccountMonth)
    throws Exception
  {
    CommonDataImpl cbo = new CommonDataImpl();
    String sPeriod = sAccountYear + "-" + sAccountMonth;

    if (cbo.isModuleStarted(sCorpID, "2002", sPeriod)
      .booleanValue()) {
      boolean bIsEnd = cbo.isModuleAccount(sCorpID, sPeriod, 
        "2002").booleanValue();
      if (bIsEnd) {
        BusinessException be = new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000040"));

        throw be;
      }

    }

    if (cbo.isModuleStarted(sCorpID, "3004", sPeriod)
      .booleanValue()) {
      boolean bIsEnd = cbo.isModuleAccount(sCorpID, sPeriod, 
        "3004").booleanValue();
      if (bIsEnd) {
        BusinessException be = new BusinessException(
          NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000041"));

        throw be;
      }

    }

    String sNextPeriod = cbo.getNextPeriod(sCorpID, sPeriod);
    String sNextAccountYear = sNextPeriod.substring(0, 4);
    String sNextAccountMonth = sNextPeriod.substring(5, 7);

    String sUnPeriod = cbo.getUnClosePeriod(sCorpID);
    if (!sUnPeriod.equals(sNextPeriod)) {
      String[] value = { 
        sPeriod };

      BusinessException be = new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000042", null, 
        value));
      throw be;
    }

    StringBuffer sSQL = new StringBuffer(" select ");
    sSQL.append(" cinventoryid,vbillcode ");
    sSQL.append(" from ");
    sSQL.append(" v_ia_inoutledger v ");
    sSQL.append(" where ");
    sSQL.append(" v.pk_corp = '" + sCorpID + "'");
    sSQL.append(" and ");
    sSQL.append(" v.caccountyear = '" + sNextAccountYear + "'");
    sSQL.append(" and ");
    sSQL.append(" v.caccountmonth = '" + sNextAccountMonth + "'");
    sSQL.append(" and ");
    sSQL.append(" v.iauditsequence > 0 ");

    String[][] sQueryResult = cbo.queryData(sSQL.toString());

    String sHintString = "";
    for (int i = 0; i < sQueryResult.length; i++) {
      String[] s = sQueryResult[i];

      sSQL = new StringBuffer(" select ");
      sSQL.append(" k.invcode,k.invname ");
      sSQL.append(" from ");
      sSQL.append(" bd_invmandoc j,  bd_invbasdoc k ");
      sSQL.append(" where ");
      sSQL.append(" j.pk_invmandoc = '" + s[0] + "'");
      sSQL.append(" and ");
      sSQL.append(" j.pk_invbasdoc=k.pk_invbasdoc ");
      String[][] sInvResult = cbo.queryData(sSQL.toString());

      sHintString = sHintString + 
        NCLangResOnserver.getInstance().getStrByID("common", 
        "UC000-0001480") + 
        ": " + 
        sInvResult[0][0] + 
        ", " + 
        NCLangResOnserver.getInstance().getStrByID("common", 
        "UC000-0001453") + 
        ": " + 
        sInvResult[0][1] + 
        ", " + 
        NCLangResOnserver.getInstance().getStrByID("common", 
        "UC000-0000794") + ": " + s[1] + "\n";
    }

    if (sHintString.length() != 0) {
      String[] value = { 
        sHintString };

      BusinessException be = new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000043", null, 
        value));
      throw be;
    }

    sSQL = new StringBuffer(" select ");
    sSQL.append(" m.crdcenterid, m.cinventoryid ");
    sSQL.append(" from bd_produce p, ia_monthledger m ");
    sSQL.append(" where ");
    sSQL.append(" m.pk_corp = '" + sCorpID + "'");
    sSQL.append(" and ");
    sSQL.append(" m.caccountyear = '" + sAccountYear + "'");
    sSQL.append(" and ");
    sSQL.append(" m.caccountmonth = '" + sAccountMonth + "'");
    sSQL.append(" and ");
    sSQL.append(" p.pk_calbody = m.crdcenterid ");
    sSQL.append(" and ");
    sSQL.append(" p.pk_invmandoc = m.cinventoryid ");
    sSQL.append(" and ");
    sSQL.append(" m.frecordtypeflag = 3 ");
    sSQL.append(" and ");
    sSQL.append(" p.pricemethod <> m.fpricemodeflag ");

    sQueryResult = cbo.queryData(sSQL.toString());

    StringBuffer sbHintString = new StringBuffer("");
    for (int i = 0; i < sQueryResult.length; i++) {
      String[] s = sQueryResult[i];

      sSQL = new StringBuffer(" select ");
      sSQL.append(" r.bodyname");
      sSQL.append(" from ");
      sSQL.append(" bd_calbody r ");
      sSQL.append(" where ");
      sSQL.append(" r.pk_calbody = '" + s[0] + "'");
      String[][] sRdResult = cbo.queryData(sSQL.toString());

      sSQL = new StringBuffer(" select ");
      sSQL.append(" k.invcode,k.invname");
      sSQL.append(" from ");
      sSQL.append(" bd_invmandoc j,  bd_invbasdoc k ");
      sSQL.append(" where ");
      sSQL.append(" j.pk_invmandoc = '" + s[1] + "'");
      sSQL.append(" and ");
      sSQL.append(" j.pk_invbasdoc=k.pk_invbasdoc ");
      String[][] sInvResult = cbo.queryData(sSQL.toString());

      sbHintString.append(NCLangResOnserver.getInstance()
        .getStrByID("common", "UC000-0001825") + 
        ": " + 
        sRdResult[0][0] + 
        ", " + 
        NCLangResOnserver.getInstance().getStrByID("common", 
        "UC000-0001480") + 
        ": " + 
        sInvResult[0][0] + 
        ", " + 
        NCLangResOnserver.getInstance().getStrByID("common", 
        "UC000-0001453") + 
        ": " + 
        sInvResult[0][1] + 
        "\n");
    }

    if (sbHintString.length() != 0) {
      String[] value = { 
        sbHintString.toString() };

      BusinessException be = new BusinessException(
        NCLangResOnserver.getInstance().getStrByID("20147020", "UPP20147020-000044", null, 
        value));
      throw be;
    }

    return true;
  }

  public AccountCheckVO[] getUnauditBills(String[] sSysinfos)
    throws BusinessException
  {
    AccountCheckVO[] avos = (AccountCheckVO[])null;
    String sCorpID = sSysinfos[0];
    String sAccountPeriod = sSysinfos[1];
    String sModuleCode = sSysinfos[2];
    String logindate = sSysinfos[3];
    String cuserid = sSysinfos[4];
    try
    {
      CommonDataImpl cbo = new CommonDataImpl();
      IAContext context = IAContext.create(sCorpID, logindate, cuserid);

      UFDate sBeginDate = cbo.getMonthBeginDate(sCorpID, sAccountPeriod);
      UFDate sEndDate = cbo.getMonthEndDate(sCorpID, sAccountPeriod);

      if (sModuleCode.equals("2014"))
      {
        StringBuffer sSQL = new StringBuffer(" select ");
        sSQL
          .append(" g.billtypename,a.vbillcode,b.bodycode,b.bodyname,c.storcode,c.storname,d.invcode,d.invname,d.invspec,d.invtype,f.measname,a.nnumber,a.nprice,a.nmoney,a.dbilldate, a.cdispatchid ");
        sSQL.append(" from ");
        sSQL
          .append(" v_ia_inoutledger a left outer join bd_stordoc c on a.cwarehouseid = c.pk_stordoc,bd_calbody b,bd_invbasdoc d,bd_invmandoc e,bd_measdoc f,bd_billtype g ");
        sSQL.append(" where ");
        sSQL.append(" a.crdcenterid = b.pk_calbody ");
        sSQL.append(" and ");
        sSQL.append(" a.cinventoryid = e.pk_invmandoc ");
        sSQL.append(" and ");
        sSQL.append(" e.pk_invbasdoc = d.pk_invbasdoc ");
        sSQL.append(" and ");
        sSQL.append(" d.pk_measdoc = f.pk_measdoc ");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode = g.pk_billtypecode ");
        sSQL.append(" and ");
        sSQL.append(" a.iauditsequence < 0 ");
        sSQL.append(" and ");
        sSQL.append(" a.pk_corp = '" + sCorpID + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate >= '" + sBeginDate + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate <= '" + sEndDate + "'");
        sSQL.append(" order by ");
        sSQL.append(" a.cbilltypecode,a.vbillcode ");

        String[][] sResult = cbo.queryData(sSQL.toString());

        avos = new AccountCheckVO[sResult.length];
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];

          AccountCheckVO avo = new AccountCheckVO();
          avo.setCbilltype(sTemp[0]);
          avo.setVbillcode(sTemp[1]);

          avo.setCcalbodycode(sTemp[2]);
          avo.setCcalbodyname(sTemp[3]);
          avo.setCstorcode(sTemp[4]);
          avo.setCstorname(sTemp[5]);

          avo.setCinventorycode(sTemp[6]);
          avo.setCinventoryname(sTemp[7]);
          avo.setCinventoryspec(sTemp[8]);
          avo.setCinventorytype(sTemp[9]);
          avo.setCinventorymeasname(sTemp[10]);

          if (sTemp[11].trim().length() != 0) {
            avo.setNnumber(new UFDouble(sTemp[11]));
          }

          if (sTemp[12].trim().length() != 0) {
            avo.setNprice(new UFDouble(sTemp[12]));
          }

          if (sTemp[13].trim().length() != 0) {
            avo.setNmoney(new UFDouble(sTemp[13]));
          }

          avo.setDbilldate(new UFDate(sTemp[14]));
  //       avo.setDispatchid(sTemp[15]);

          avo.setCmodulecode("IA");

          avos[i] = avo;
        }
      }
      else if (sModuleCode.equals("m_sIAUnRtvoucher"))
      {
        StringBuffer sSQL = new StringBuffer(" select ");
        sSQL
          .append(" g.billtypename,a.vbillcode,b.bodycode,b.bodyname,c.storcode,c.storname,d.invcode,d.invname,d.invspec,d.invtype,f.measname,a.nnumber,a.nprice,a.nmoney,a.dbilldate,sm_user.user_name,a.dauditdate, a.cdispatchid ");
        sSQL.append(" from ");
        sSQL
          .append(" v_ia_inoutledger a left outer join bd_stordoc c on a.cwarehouseid = c.pk_stordoc,sm_user,bd_calbody b,bd_invbasdoc d,bd_invmandoc e,bd_measdoc f,bd_billtype g ");
        sSQL.append(" where ");
        sSQL.append(" a.crdcenterid = b.pk_calbody ");
        sSQL.append(" and ");
        sSQL.append(" a.cinventoryid = e.pk_invmandoc ");
        sSQL.append(" and ");
        sSQL.append(" e.pk_invbasdoc = d.pk_invbasdoc ");
        sSQL.append(" and ");
        sSQL.append(" d.pk_measdoc = f.pk_measdoc ");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode <> 'I0' and a.cbilltypecode <> 'I1'");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode = g.pk_billtypecode ");
        sSQL.append(" and ");
        sSQL.append(" a.iauditsequence > 0 ");
        sSQL.append(" and a.caccountyear = '" + context.getAuditYear() + "' ");
        sSQL
          .append(" and a.caccountmonth = '" + context.getAuditMonth() + "' ");
        sSQL.append(" and a.dauditdate >= '" + context.getBeginDate() + "' ");
        sSQL.append(" and a.dauditdate <= '" + context.getEndDate() + "' ");
        sSQL.append(" and a.nmoney is not null and a.nmoney <> 0 ");

        sSQL.append(" and ");
        sSQL.append(" a.cauditorid = sm_user.cuserid");
        sSQL.append(" and ");
        sSQL.append(" a.brtvouchflag = 'N' ");
        sSQL.append(" and ");
        sSQL.append(" a.pk_corp = '" + sCorpID + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate >= '" + sBeginDate + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate <= '" + sEndDate + "'");
        sSQL.append(" order by ");
        sSQL.append(" a.cbilltypecode,a.vbillcode ");

        String[][] sResult = cbo.queryData(sSQL.toString());

        avos = new AccountCheckVO[sResult.length];
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];

          AccountCheckVO avo = new AccountCheckVO();
          avo.setCbilltype(sTemp[0]);
          avo.setVbillcode(sTemp[1]);

          avo.setCcalbodycode(sTemp[2]);
          avo.setCcalbodyname(sTemp[3]);
          avo.setCstorcode(sTemp[4]);
          avo.setCstorname(sTemp[5]);

          avo.setCinventorycode(sTemp[6]);
          avo.setCinventoryname(sTemp[7]);
          avo.setCinventoryspec(sTemp[8]);
          avo.setCinventorytype(sTemp[9]);
          avo.setCinventorymeasname(sTemp[10]);

          if (sTemp[11].trim().length() != 0) {
            avo.setNnumber(new UFDouble(sTemp[11]));
          }

          if (sTemp[12].trim().length() != 0) {
            avo.setNprice(new UFDouble(sTemp[12]));
          }

          if (sTemp[13].trim().length() != 0) {
            avo.setNmoney(new UFDouble(sTemp[13]));
          }

          avo.setDbilldate(new UFDate(sTemp[14]));
          avo.setCauditorname(sTemp[15]);

          if (sTemp[16].trim().length() != 0) {
            avo.setDauditdate(new UFDate(sTemp[16]));
          }

   //       avo.setDispatchid(sTemp[17]);

          avo.setCmodulecode("IA");

          avos[i] = avo;
        }
      }
      else if (sModuleCode.equals("m_sIAUnVoucher"))
      {
        StringBuffer sSQL = new StringBuffer(" select ");
        sSQL
          .append(" g.billtypename,a.vbillcode,b.bodycode,b.bodyname,c.storcode,c.storname,d.invcode,d.invname,d.invspec,d.invtype,f.measname,a.nnumber,a.nprice,a.nmoney,a.dbilldate,sm_user.user_name,a.dauditdate, a.cdispatchid ");
        sSQL.append(" from ");
        sSQL
          .append(" v_ia_inoutledger a left outer join bd_stordoc c on a.cwarehouseid = c.pk_stordoc ,sm_user,bd_calbody b,bd_invbasdoc d,bd_invmandoc e,bd_measdoc f,bd_billtype g ");
        sSQL.append(" where ");
        sSQL.append(" a.crdcenterid = b.pk_calbody ");
        sSQL.append(" and ");
        sSQL.append(" a.cinventoryid = e.pk_invmandoc ");
        sSQL.append(" and ");
        sSQL.append(" e.pk_invbasdoc = d.pk_invbasdoc ");
        sSQL.append(" and ");
        sSQL.append(" d.pk_measdoc = f.pk_measdoc ");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode <> 'I0' and a.cbilltypecode <> 'I1'");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode = g.pk_billtypecode ");
        sSQL.append(" and ");
        sSQL.append(" a.iauditsequence > 0 ");
        sSQL.append(" and ");
        sSQL.append(" a.cauditorid = sm_user.cuserid");
        sSQL.append(" and ");
        sSQL.append(" a.cvoucherid is null ");
        sSQL.append(" and ");
        sSQL.append(" a.pk_corp = '" + sCorpID + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate >= '" + sBeginDate + "'");
        sSQL.append(" and ");
        sSQL.append(" a.dbilldate <= '" + sEndDate + "'");
        sSQL.append(" order by ");
        sSQL.append(" a.cbilltypecode,a.vbillcode ");

        String[][] sResult = cbo.queryData(sSQL.toString());

        avos = new AccountCheckVO[sResult.length];
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];

          AccountCheckVO avo = new AccountCheckVO();
          avo.setCbilltype(sTemp[0]);
          avo.setVbillcode(sTemp[1]);

          avo.setCcalbodycode(sTemp[2]);
          avo.setCcalbodyname(sTemp[3]);
          avo.setCstorcode(sTemp[4]);
          avo.setCstorname(sTemp[5]);

          avo.setCinventorycode(sTemp[6]);
          avo.setCinventoryname(sTemp[7]);
          avo.setCinventoryspec(sTemp[8]);
          avo.setCinventorytype(sTemp[9]);
          avo.setCinventorymeasname(sTemp[10]);

          if (sTemp[11].trim().length() != 0) {
            avo.setNnumber(new UFDouble(sTemp[11]));
          }

          if (sTemp[12].trim().length() != 0) {
            avo.setNprice(new UFDouble(sTemp[12]));
          }

          if (sTemp[13].trim().length() != 0) {
            avo.setNmoney(new UFDouble(sTemp[13]));
          }

          avo.setDbilldate(new UFDate(sTemp[14]));

          avo.setCauditorname(sTemp[15]);

          if (sTemp[16].trim().length() != 0) {
            avo.setDauditdate(new UFDate(sTemp[16]));
          }
   //       avo.setDispatchid(sTemp[17]);

          avo.setCmodulecode("IA");

          avos[i] = avo;
        }
      }
      else if (sModuleCode.equals("m_sIAMonthPrice"))
      {
        StringBuffer sSQL = new StringBuffer(" select ");
        sSQL
          .append(" g.billtypename,a.vbillcode,b.bodycode,b.bodyname,c.storcode,c.storname,d.invcode,d.invname,d.invspec,d.invtype,f.measname,a.nnumber,a.nprice,a.nmoney,a.dbilldate,ge.nabprice,sm_user.user_name,a.dauditdate ");
        sSQL.append(" from ");
        sSQL
          .append(" v_ia_inoutledger a left outer join bd_stordoc c on a.cwarehouseid = c.pk_stordoc ,sm_user,bd_calbody b,bd_invbasdoc d,bd_invmandoc e,bd_measdoc f,bd_billtype g,ia_generalledger ge ");
        sSQL.append(" where ");
        sSQL.append(" a.crdcenterid = b.pk_calbody ");
        sSQL.append(" and ");
        sSQL.append(" a.cinventoryid = e.pk_invmandoc ");
        sSQL.append(" and ");
        sSQL.append(" e.pk_invbasdoc = d.pk_invbasdoc ");
        sSQL.append(" and ");
        sSQL.append(" d.pk_measdoc = f.pk_measdoc ");
        sSQL.append(" and ");
        sSQL.append(" a.cbilltypecode = g.pk_billtypecode ");
        sSQL.append(" and ");
        sSQL.append(" a.iauditsequence > 0 ");
        sSQL.append(" and ");
        sSQL.append(" a.cauditorid = sm_user.cuserid");
        sSQL.append(" and ");
        sSQL.append(" a.fpricemodeflag = 4 ");
        sSQL.append(" and ");
        sSQL.append(" a.crdcenterid = ge.crdcenterid ");
        sSQL.append(" and ");
        sSQL.append(" a.cinventoryid = ge.cinventoryid ");
        sSQL.append(" and ");
        sSQL.append(" a.nprice != ge.nabprice ");
        sSQL.append(" and ");
        sSQL.append(" ge.frecordtypeflag = 4 ");
        sSQL.append(" and ");
        sSQL.append(" a.fdatagetmodelflag =7 ");
        sSQL.append(" and ");
        sSQL.append(" a.pk_corp = '" + sCorpID + "'");

        sSQL.append(" and ");
        sSQL.append(" a.dbilldate <= '" + sEndDate + "'");
        sSQL.append(" order by ");
        sSQL.append(" a.cbilltypecode,a.vbillcode ");

        String[][] sResult = cbo.queryData(sSQL.toString());

        avos = new AccountCheckVO[sResult.length];
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];

          AccountCheckVO avo = new AccountCheckVO();
          avo.setCbilltype(sTemp[0]);
          avo.setVbillcode(sTemp[1]);

          avo.setCcalbodycode(sTemp[2]);
          avo.setCcalbodyname(sTemp[3]);
          avo.setCstorcode(sTemp[4]);
          avo.setCstorname(sTemp[5]);

          avo.setCinventorycode(sTemp[6]);
          avo.setCinventoryname(sTemp[7]);
          avo.setCinventoryspec(sTemp[8]);
          avo.setCinventorytype(sTemp[9]);
          avo.setCinventorymeasname(sTemp[10]);

          if (sTemp[11].trim().length() != 0) {
            avo.setNnumber(new UFDouble(sTemp[11]));
          }

          if (sTemp[12].trim().length() != 0) {
            avo.setNprice(new UFDouble(sTemp[12]));
          }

          if (sTemp[13].trim().length() != 0) {
            avo.setNmoney(new UFDouble(sTemp[13]));
          }

          avo.setDbilldate(new UFDate(sTemp[14]));

          if (sTemp[15].trim().length() != 0) {
            avo.setNabprice(new UFDouble(sTemp[15]));
          }

          avo.setCauditorname(sTemp[16]);

          if (sTemp[17].trim().length() != 0) {
            avo.setDauditdate(new UFDate(sTemp[17]));
          }

          avo.setCmodulecode("IA");

          avos[i] = avo;
        }

      }
      else if (sModuleCode.equals("4008"))
      {
        Object oInstance = NCLocator.getInstance().lookup(
          IICToIA.class.getName());
        avos = ((IICToIA)oInstance).queryUnAuditedBills(sCorpID, sEndDate);
      }
      else if (sModuleCode.equals("4004"))
      {
        Object oInstance = NCLocator.getInstance().lookup(
          IPuToIa_PO2IA.class.getName());
        avos = ((IPuToIa_PO2IA)oInstance).queryNotSettledBills(sCorpID, 
          sEndDate);
      }
      else if (sModuleCode.equals("4006"))
      {
        Object oInstance = NCLocator.getInstance().lookup(
          ISOToIA.class.getName());
        avos = ((ISOToIA)oInstance).getAccountCheck(sCorpID, sEndDate);
      }

      return avos;
    }
    catch (Exception e2) {

    throw new BusinessException("Remote Call", e2);
    }
  }

  private String getAuditKey(String crdcenterid, String cinventoryid, String vbatch)
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(crdcenterid);
    buffer.append(cinventoryid);
    buffer.append(vbatch == null ? "" : vbatch);
    return buffer.toString();
  }

  private MonthledgerVO[] getWriteVO(MonthledgerVO[] cvos, String[] sSysInfo, int iFlag, int iOpea, IMonthledger mbo)
    throws Exception
  {
    String sCorpID = sSysInfo[0];
    String sHandleYear = sSysInfo[1];
    String sHandleMonth = sSysInfo[2];
    String sPeriousYear = sSysInfo[3];
    String sPeriousMonth = sSysInfo[4];
    String sStartYear = sSysInfo[5];

    String sLastYear = sSysInfo[7];
    String sFlag = sSysInfo[8];
    String sLastMonth = sSysInfo[9];

    if (iFlag == 3)
    {
      if (sHandleYear.equals(sStartYear))
      {
        for (int i = 0; i < cvos.length; i++) {
          MonthledgerVO oneVO = cvos[i];
          oneVO.setFrecordtypeflag(new Integer(iFlag));
          oneVO.setCaccountyear(sHandleYear);
          oneVO.setCaccountmonth(sHandleMonth);
        }

        return cvos;
      }
    }

    MonthledgerVO cvo = new MonthledgerVO();
    cvo.setPk_corp(sCorpID);

    if (iFlag == 3)
    {
      cvo.setCaccountyear(sLastYear);
      cvo.setCaccountmonth(sLastMonth);
      cvo.setFrecordtypeflag(new Integer(5));
    }
    else
    {
      cvo.setCaccountyear(sPeriousYear);
      cvo.setCaccountmonth(sPeriousMonth);
      if ((sFlag != null) && (sFlag.trim().length() != 0)) {
        cvo.setFrecordtypeflag(new Integer(sFlag));
      }

    }

    MonthledgerVO[] resultallvos = mbo.queryByVO(cvo, new Boolean(true));
    Map map = new HashMap();
    if ((resultallvos != null) && (resultallvos.length != 0)) {
      for (int j = 0; j < resultallvos.length; j++) {
        String crdcenterid = resultallvos[j].getCrdcenterid();
        String cinventoryid = resultallvos[j].getCinventoryid();
        String vbatch = resultallvos[j].getVbatch();
        String key = getAuditKey(crdcenterid, 
          cinventoryid, vbatch);
        map.put(key, resultallvos[j]);
      }
    }

    for (int i = 0; i < cvos.length; i++) {
      MonthledgerVO oneHeaderVO = cvos[i];

      String crdcenterid = oneHeaderVO.getCrdcenterid();
      String cinventoryid = oneHeaderVO.getCinventoryid();
      String vbatch = oneHeaderVO.getVbatch();

      String key = getAuditKey(crdcenterid, cinventoryid, vbatch);
      MonthledgerVO ghvo = (MonthledgerVO)map.get(key);

      if (ghvo == null)
      {
        oneHeaderVO.setFrecordtypeflag(new Integer(iFlag));
      }
      else
      {
        UFDouble dcurRKSL = new UFDouble("0");
        UFDouble dcurRKJE = new UFDouble("0");
        UFDouble dcurCKSL = new UFDouble("0");
        UFDouble dcurCKJE = new UFDouble("0");
        UFDouble dcurJCSL = new UFDouble("0");
        UFDouble dcurJCJE = new UFDouble("0");

        UFDouble dcurRKCY = new UFDouble("0");
        UFDouble dcurCKCY = new UFDouble("0");
        UFDouble dcurJCCY = new UFDouble("0");

        UFDouble dcurFCJFSL = new UFDouble("0");
        UFDouble dcurFCDFSL = new UFDouble("0");
        UFDouble dcurFCJCSL = new UFDouble("0");
        UFDouble dcurFCJFJE = new UFDouble("0");
        UFDouble dcurFCDFJE = new UFDouble("0");
        UFDouble dcurFCJCJE = new UFDouble("0");

        UFDouble dcurWTJFSL = new UFDouble("0");
        UFDouble dcurWTDFSL = new UFDouble("0");
        UFDouble dcurWTJCSL = new UFDouble("0");
        UFDouble dcurWTJFJE = new UFDouble("0");
        UFDouble dcurWTDFJE = new UFDouble("0");
        UFDouble dcurWTJCJE = new UFDouble("0");

        if (oneHeaderVO.getNinnum() != null) {
          dcurRKSL = oneHeaderVO.getNinnum();
        }

        if (oneHeaderVO.getNinmny() != null) {
          dcurRKJE = oneHeaderVO.getNinmny();
        }

        if (oneHeaderVO.getNoutnum() != null) {
          dcurCKSL = oneHeaderVO.getNoutnum();
        }

        if (oneHeaderVO.getNoutmny() != null) {
          dcurCKJE = oneHeaderVO.getNoutmny();
        }

        if (oneHeaderVO.getNabnum() != null) {
          dcurJCSL = oneHeaderVO.getNabnum();
        }

        if (oneHeaderVO.getNabmny() != null) {
          dcurJCJE = oneHeaderVO.getNabmny();
        }

        if (oneHeaderVO.getNinvarymny() != null) {
          dcurRKCY = oneHeaderVO.getNinvarymny();
        }

        if (oneHeaderVO.getNoutvarymny() != null) {
          dcurCKCY = oneHeaderVO.getNoutvarymny();
        }

        if (oneHeaderVO.getNabvarymny() != null) {
          dcurJCCY = oneHeaderVO.getNabvarymny();
        }

        if (oneHeaderVO.getNemitdebitnum() != null) {
          dcurFCJFSL = oneHeaderVO.getNemitdebitnum();
        }

        if (oneHeaderVO.getNemitcreditnum() != null) {
          dcurFCDFSL = oneHeaderVO.getNemitcreditnum();
        }

        if (oneHeaderVO.getNemitabnum() != null) {
          dcurFCJCSL = oneHeaderVO.getNemitabnum();
        }

        if (oneHeaderVO.getNemitdebitmny() != null) {
          dcurFCJFJE = oneHeaderVO.getNemitdebitmny();
        }

        if (oneHeaderVO.getNemitcreditmny() != null) {
          dcurFCDFJE = oneHeaderVO.getNemitcreditmny();
        }

        if (oneHeaderVO.getNemitabmny() != null) {
          dcurFCJCJE = oneHeaderVO.getNemitabmny();
        }

        if (oneHeaderVO.getNentrustdebitnum() != null) {
          dcurWTJFSL = oneHeaderVO.getNentrustdebitnum();
        }

        if (oneHeaderVO.getNentrustcreditnum() != null) {
          dcurWTDFSL = oneHeaderVO.getNentrustcreditnum();
        }

        if (oneHeaderVO.getNentrustabnum() != null) {
          dcurWTJCSL = oneHeaderVO.getNentrustabnum();
        }

        if (oneHeaderVO.getNentrustdebitmny() != null) {
          dcurWTJFJE = oneHeaderVO.getNentrustdebitmny();
        }

        if (oneHeaderVO.getNentrustcreditmny() != null) {
          dcurWTDFJE = oneHeaderVO.getNentrustcreditmny();
        }

        if (oneHeaderVO.getNentrustabmny() != null) {
          dcurWTJCJE = oneHeaderVO.getNentrustabmny();
        }

        UFDouble dLastRKSL = new UFDouble("0");
        UFDouble dLastRKJE = new UFDouble("0");
        UFDouble dLastCKSL = new UFDouble("0");
        UFDouble dLastCKJE = new UFDouble("0");
        UFDouble dLastJCSL = new UFDouble("0");
        UFDouble dLastJCJE = new UFDouble("0");

        UFDouble dLastRKCY = new UFDouble("0");
        UFDouble dLastCKCY = new UFDouble("0");
        UFDouble dLastJCCY = new UFDouble("0");

        UFDouble dLastFCJFSL = new UFDouble("0");
        UFDouble dLastFCDFSL = new UFDouble("0");
        UFDouble dLastFCJCSL = new UFDouble("0");
        UFDouble dLastFCJFJE = new UFDouble("0");
        UFDouble dLastFCDFJE = new UFDouble("0");
        UFDouble dLastFCJCJE = new UFDouble("0");

        UFDouble dLastWTJFSL = new UFDouble("0");
        UFDouble dLastWTDFSL = new UFDouble("0");
        UFDouble dLastWTJCSL = new UFDouble("0");
        UFDouble dLastWTJFJE = new UFDouble("0");
        UFDouble dLastWTDFJE = new UFDouble("0");
        UFDouble dLastWTJCJE = new UFDouble("0");

        if (!sPeriousMonth.equals("00"))
        {
          if (ghvo.getNinnum() != null) {
            dLastRKSL = ghvo.getNinnum();
          }

          if (ghvo.getNinmny() != null) {
            dLastRKJE = ghvo.getNinmny();
          }

          if (ghvo.getNoutnum() != null) {
            dLastCKSL = ghvo.getNoutnum();
          }

          if (ghvo.getNoutmny() != null) {
            dLastCKJE = ghvo.getNoutmny();
          }
        }

        if (ghvo.getNabnum() != null) {
          dLastJCSL = ghvo.getNabnum();
        }

        if (ghvo.getNabmny() != null) {
          dLastJCJE = ghvo.getNabmny();
        }

        if (!sPeriousMonth.equals("00"))
        {
          if (ghvo.getNinvarymny() != null) {
            dLastRKCY = ghvo.getNinvarymny();
          }

          if (ghvo.getNoutvarymny() != null) {
            dLastCKCY = ghvo.getNoutvarymny();
          }
        }

        if (ghvo.getNabvarymny() != null) {
          dLastJCCY = ghvo.getNabvarymny();
        }

        if (!sPeriousMonth.equals("00"))
        {
          if (ghvo.getNemitdebitnum() != null) {
            dLastFCJFSL = ghvo.getNemitdebitnum();
          }

          if (ghvo.getNemitcreditnum() != null) {
            dLastFCDFSL = ghvo.getNemitcreditnum();
          }

          if (ghvo.getNemitdebitmny() != null) {
            dLastFCJFJE = ghvo.getNemitdebitmny();
          }

          if (ghvo.getNemitcreditmny() != null) {
            dLastFCDFJE = ghvo.getNemitcreditmny();
          }
        }

        if (ghvo.getNemitabnum() != null) {
          dLastFCJCSL = ghvo.getNemitabnum();
        }

        if (ghvo.getNemitabmny() != null) {
          dLastFCJCJE = ghvo.getNemitabmny();
        }

        if (!sPeriousMonth.equals("00"))
        {
          if (ghvo.getNentrustdebitnum() != null) {
            dLastWTJFSL = ghvo.getNentrustdebitnum();
          }

          if (ghvo.getNentrustcreditnum() != null) {
            dLastWTDFSL = ghvo.getNentrustcreditnum();
          }

          if (ghvo.getNentrustdebitmny() != null) {
            dLastWTJFJE = ghvo.getNentrustdebitmny();
          }

          if (ghvo.getNentrustcreditmny() != null) {
            dLastWTDFJE = ghvo.getNentrustcreditmny();
          }
        }

        if (ghvo.getNentrustabnum() != null) {
          dLastWTJCSL = ghvo.getNentrustabnum();
        }

        if (ghvo.getNentrustabmny() != null) {
          dLastWTJCJE = ghvo.getNentrustabmny();
        }

        UFDouble dWriteRKSL = new UFDouble("0");
        UFDouble dWriteRKJE = new UFDouble("0");
        UFDouble dWriteCKSL = new UFDouble("0");
        UFDouble dWriteCKJE = new UFDouble("0");

        UFDouble dWriteJCSL = new UFDouble("0");
        UFDouble dWriteJCJE = new UFDouble("0");
        UFDouble dWriteJCDJ = new UFDouble("0");
        UFDouble dWriteRKCY = new UFDouble("0");
        UFDouble dWriteCKCY = new UFDouble("0");
        UFDouble dWriteJCCY = new UFDouble("0");

        UFDouble dWriteFCJFSL = new UFDouble("0");
        UFDouble dWriteFCDFSL = new UFDouble("0");
        UFDouble dWriteFCJCSL = new UFDouble("0");
        UFDouble dWriteFCJFJE = new UFDouble("0");
        UFDouble dWriteFCDFJE = new UFDouble("0");
        UFDouble dWriteFCJCJE = new UFDouble("0");

        UFDouble dWriteFCJCDJ = new UFDouble("0");
        UFDouble dWriteWTJFSL = IATool.getInstance().sub(dcurWTJFSL, dLastWTJFSL);
        UFDouble dWriteWTDFSL = IATool.getInstance().sub(dcurWTDFSL, dLastWTDFSL);
        UFDouble dWriteWTJCSL = IATool.getInstance().sub(dcurWTJCSL, dLastWTJCSL);
        UFDouble dWriteWTJFJE = IATool.getInstance().sub(dcurWTJFJE, dLastWTJFJE);
        UFDouble dWriteWTDFJE = IATool.getInstance().sub(dcurWTDFJE, dLastWTDFJE);
        UFDouble dWriteWTJCJE = IATool.getInstance().sub(dcurWTJCJE, dLastWTJCJE);

        UFDouble dWriteWTJCDJ = new UFDouble("0");

        if (iOpea == 0) {
          dWriteRKSL = IATool.getInstance().sub(dcurRKSL, dLastRKSL);
          dWriteRKJE = IATool.getInstance().sub(dcurRKJE, dLastRKJE);
          dWriteCKSL = IATool.getInstance().sub(dcurCKSL, dLastCKSL);
          dWriteCKJE = IATool.getInstance().sub(dcurCKJE, dLastCKJE);

          if (iFlag != 3)
          {
            dWriteJCSL = IATool.getInstance().sub(dcurJCSL, dLastJCSL);
            dWriteJCJE = IATool.getInstance().sub(dcurJCJE, dLastJCJE);

            dWriteJCCY = IATool.getInstance().sub(dcurJCCY, dLastJCCY);

            dWriteFCJCSL = IATool.getInstance().sub(dcurFCJCSL, dLastFCJCSL);

            dWriteFCJCJE = IATool.getInstance().sub(dcurFCJCJE, dLastFCJCJE);

            dWriteWTJCSL = IATool.getInstance().sub(dcurWTJCSL, dLastWTJCSL);

            dWriteWTJCJE = IATool.getInstance().sub(dcurWTJCJE, dLastWTJCJE);
          }
          else
          {
            dWriteJCSL = dcurJCSL;
            dWriteJCJE = dcurJCJE;

            dWriteJCCY = dcurJCCY;

            dWriteFCJCSL = dcurFCJCSL;

            dWriteFCJCJE = dcurFCJCJE;

            dWriteWTJCSL = dcurWTJCSL;

            dWriteWTJCJE = dcurWTJCJE;
          }

          dWriteJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNabprice() != null) {
            dWriteJCDJ = oneHeaderVO.getNabprice();
          }
          if (dWriteJCSL.doubleValue() != 0.0D) {
            dWriteJCDJ = dWriteJCJE.div(dWriteJCSL);
          }

          dWriteRKCY = IATool.getInstance().sub(dcurRKCY, dLastRKCY);
          dWriteCKCY = IATool.getInstance().sub(dcurCKCY, dLastCKCY);

          dWriteFCJFSL = IATool.getInstance().sub(dcurFCJFSL, dLastFCJFSL);
          dWriteFCDFSL = IATool.getInstance().sub(dcurFCDFSL, dLastFCDFSL);
          dWriteFCJFJE = IATool.getInstance().sub(dcurFCJFJE, dLastFCJFJE);
          dWriteFCDFJE = IATool.getInstance().sub(dcurFCDFJE, dLastFCDFJE);
          dWriteFCJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNemitprice() != null) {
            dWriteFCJCDJ = oneHeaderVO.getNemitprice();
          }
          if (dWriteFCJCSL.doubleValue() != 0.0D) {
            dWriteFCJCDJ = dWriteFCJCJE.div(dWriteFCJCSL);
          }

          dWriteWTJFSL = IATool.getInstance().sub(dcurWTJFSL, dLastWTJFSL);
          dWriteWTDFSL = IATool.getInstance().sub(dcurWTDFSL, dLastWTDFSL);

          dWriteWTJFJE = IATool.getInstance().sub(dcurWTJFJE, dLastWTJFJE);
          dWriteWTDFJE = IATool.getInstance().sub(dcurWTDFJE, dLastWTDFJE);

          dWriteWTJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNentrustprice() != null) {
            dWriteWTJCDJ = oneHeaderVO.getNentrustprice();
          }
          if (dWriteWTJCSL.doubleValue() != 0.0D) {
            dWriteWTJCDJ = dWriteWTJCJE.div(dWriteWTJCSL);
          }
        }
        else if (iOpea == 1)
        {
          dWriteRKSL = IATool.getInstance().add(dcurRKSL, dLastRKSL);
          dWriteRKJE = IATool.getInstance().add(dcurRKJE, dLastRKJE);
          dWriteCKSL = IATool.getInstance().add(dcurCKSL, dLastCKSL);
          dWriteCKJE = IATool.getInstance().add(dcurCKJE, dLastCKJE);

          dWriteJCSL = dcurJCSL;
          dWriteJCJE = dcurJCJE;

          dWriteJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNabprice() != null) {
            dWriteJCDJ = oneHeaderVO.getNabprice();
          }
          if (dWriteJCSL.doubleValue() != 0.0D) {
            dWriteJCDJ = dWriteJCJE.div(dWriteJCSL);
          }

          dWriteRKCY = IATool.getInstance().add(dcurRKCY, dLastRKCY);
          dWriteCKCY = IATool.getInstance().add(dcurCKCY, dLastCKCY);
          dWriteJCCY = IATool.getInstance().add(dcurJCCY, dLastJCCY);

          dWriteFCJFSL = IATool.getInstance().add(dcurFCJFSL, dLastFCJFSL);
          dWriteFCDFSL = IATool.getInstance().add(dcurFCDFSL, dLastFCDFSL);
          dWriteFCJCSL = dcurFCJCSL;

          dWriteFCJFJE = IATool.getInstance().add(dcurFCJFJE, dLastFCJFJE);
          dWriteFCDFJE = IATool.getInstance().add(dcurFCDFJE, dLastFCDFJE);
          dWriteFCJCJE = dcurFCJCJE;

          dWriteFCJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNemitprice() != null) {
            dWriteFCJCDJ = oneHeaderVO.getNemitprice();
          }
          if (dWriteFCJCSL.doubleValue() != 0.0D) {
            dWriteFCJCDJ = dWriteFCJCJE.div(dWriteFCJCSL);
          }

          dWriteWTJFSL = IATool.getInstance().add(dcurWTJFSL, dLastWTJFSL);
          dWriteWTDFSL = IATool.getInstance().add(dcurWTDFSL, dLastWTDFSL);
          dWriteWTJCSL = dcurWTJCSL;

          dWriteWTJFJE = IATool.getInstance().add(dcurWTJFJE, dLastWTJFJE);
          dWriteWTDFJE = IATool.getInstance().add(dcurWTDFJE, dLastWTDFJE);
          dWriteWTJCJE = dcurWTJCJE;

          dWriteWTJCDJ = new UFDouble("0");
          if (oneHeaderVO.getNentrustprice() != null) {
            dWriteWTJCDJ = oneHeaderVO.getNentrustprice();
          }
          if (dWriteWTJCSL.doubleValue() != 0.0D) {
            dWriteWTJCDJ = dWriteWTJCJE.div(dWriteWTJCSL);
          }

        }

        oneHeaderVO.setNinnum(dWriteRKSL);
        oneHeaderVO.setNinmny(dWriteRKJE);

        oneHeaderVO.setNoutnum(dWriteCKSL);
        oneHeaderVO.setNoutmny(dWriteCKJE);

        oneHeaderVO.setNabnum(dWriteJCSL);
        oneHeaderVO.setNabprice(dWriteJCDJ);
        oneHeaderVO.setNabmny(dWriteJCJE);

        oneHeaderVO.setNinvarymny(dWriteRKCY);
        oneHeaderVO.setNoutvarymny(dWriteCKCY);
        oneHeaderVO.setNabvarymny(dWriteJCCY);

        oneHeaderVO.setNemitprice(dWriteFCJCDJ);
        oneHeaderVO.setNemitdebitnum(dWriteFCJFSL);
        oneHeaderVO.setNemitcreditnum(dWriteFCDFSL);
        oneHeaderVO.setNemitabnum(dWriteFCJCSL);
        oneHeaderVO.setNemitdebitmny(dWriteFCJFJE);
        oneHeaderVO.setNemitcreditmny(dWriteFCDFJE);
        oneHeaderVO.setNemitabmny(dWriteFCJCJE);

        oneHeaderVO.setNentrustprice(dWriteWTJCDJ);
        oneHeaderVO.setNentrustdebitnum(dWriteWTJFSL);
        oneHeaderVO.setNentrustcreditnum(dWriteWTDFSL);
        oneHeaderVO.setNentrustabnum(dWriteWTJCSL);
        oneHeaderVO.setNentrustdebitmny(dWriteWTJFJE);
        oneHeaderVO.setNentrustcreditmny(dWriteWTDFJE);
        oneHeaderVO.setNentrustabmny(dWriteWTJCJE);

        oneHeaderVO.setFrecordtypeflag(new Integer(iFlag));
      }
    }
    return cvos;
  }

  private boolean writeAccountFlag(boolean isAccount, String sCorpID, String sYear, String sMonth, String sPerYear, String sPerMonth)
    throws Exception
  {
    AccountVO avo = new AccountVO();
    avo.setPk_corp(sCorpID);
    avo.setCaccountyear(sYear);
    avo.setCaccountmonth(sMonth);

    CreatecorpBO ccbo = new CreatecorpBO();

    if (isAccount) {
      insert(avo);
      ccbo.updateSettledPeriod(sCorpID, "IA", sYear, sMonth);
    }
    else {
      delete(avo);
      ccbo.updateSettledPeriod(sCorpID, "IA", sPerYear, 
        sPerMonth);
    }

    return true;
  }

  public void account(String[] sSysinfo, ClientLink cl)
    throws BusinessException
  {
    String pk_corp = sSysinfo[0];
    String caccountyear = sSysinfo[3];
    String caccountmonth = sSysinfo[4];
    String userID = sSysinfo[5];

    BillVO[] bvos = (BillVO[])null;
    LockOperator lock = new LockOperator(userID);
    try
    {
      BillImpl bbo = new BillImpl();
      CommonDataImpl cbo = new CommonDataImpl();
      GeneralledgerImpl gbo = new GeneralledgerImpl();
      MonthledgerImpl mbo = new MonthledgerImpl();

      String message = NCLangResOnserver.getInstance().getStrByID(
        "20146010", "UPP20146010-000055");

      lock.lock(pk_corp + "ACCOUNT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "BILLADD", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "ALLOCACCOUNT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "AUDIT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "ENDHANDLEACCOUNT", message);
      String sBeginDate = cbo.getMonthBeginDate(pk_corp, 
        caccountyear + "-" + caccountmonth).toString();

      StringBuffer sSQL = new StringBuffer();

      
      checkAccount(pk_corp, caccountyear, caccountmonth);

      String sStartPeriod = cbo.getStartPeriod(pk_corp);
      int iIndex = sStartPeriod.indexOf("-");
      String sStartYear = sStartPeriod.substring(0, iIndex);
      String sStartMonth = sStartPeriod.substring(iIndex + 1);

      String sPeriousPeriod = cbo.getPerviousPeriod(pk_corp, caccountyear + "-" + 
        caccountmonth);
      iIndex = sPeriousPeriod.indexOf("-");
      String sPeriousYear = sPeriousPeriod.substring(0, iIndex);
      String sPeriousMonth = sPeriousPeriod.substring(iIndex + 1);

      String sLastYearPeriod = cbo.getPerviousPeriod(pk_corp, caccountyear + 
        "-01");
      iIndex = sLastYearPeriod.indexOf("-");
      String sLastYear = sLastYearPeriod.substring(0, iIndex);

      String sLastMonth = cbo.getLastMonthInYear(pk_corp, caccountyear);

      String sNextPeriod = cbo.getNextPeriod(pk_corp, caccountyear + "-" + 
        caccountmonth);
      iIndex = sNextPeriod.indexOf("-");
      String sNextYear = sNextPeriod.substring(0, iIndex);
      String sNextMonth = sNextPeriod.substring(iIndex + 1);

      String sFlag = "3";
      if ((caccountyear + "-" + caccountmonth).equals(sStartPeriod)) {
        sFlag = "1";
      }

      String[] sNewSysInfo = new String[10];
      sNewSysInfo[0] = pk_corp;
      sNewSysInfo[1] = caccountyear;
      sNewSysInfo[2] = caccountmonth;
      sNewSysInfo[3] = sPeriousYear;
      sNewSysInfo[4] = sPeriousMonth;
      sNewSysInfo[5] = sStartYear;
      sNewSysInfo[6] = sStartMonth;
      sNewSysInfo[7] = sLastYear;
      sNewSysInfo[8] = sFlag;
      sNewSysInfo[9] = sLastYearPeriod.substring(5, 7);

      GeneralledgerVO cvo = new GeneralledgerVO();
      cvo.setPk_corp(pk_corp);
      cvo.setCaccountyear(caccountyear);
      cvo.setCaccountmonth(caccountmonth);
      cvo.setFrecordtypeflag(new Integer(4));

      GeneralledgerVO[] cvos = gbo.queryByVO(cvo, new Boolean(true));

      MonthledgerVO[] mvos = new MonthledgerVO[cvos.length];
      GeneralledgerVO[] backcvos = new GeneralledgerVO[cvos.length];
      for (int i = 0; i < cvos.length; i++) {
        backcvos[i] = ((GeneralledgerVO)cvos[i].clone());
        mvos[i] = cvos[i].changeToMonthVO();
      }

      if (cvos.length != 0)
      {
        sSQL = new StringBuffer(" update ");
        sSQL.append(" ia_generalledger ");
        sSQL.append(" set ");
        sSQL.append(" caccountyear = '" + sNextYear + "',");
        sSQL.append(" caccountmonth = '" + sNextMonth + "',");
        sSQL.append(" btryflag = 'N' ");
        sSQL.append(" where ");
        sSQL.append(" pk_corp = '" + pk_corp + "'");
        sSQL.append(" and ");
        sSQL.append(" caccountyear = '" + caccountyear + "'");
        sSQL.append(" and ");
        sSQL.append(" caccountmonth = '" + caccountmonth + "'");
        sSQL.append(" and ");
        sSQL.append(" frecordtypeflag = 4 ");

        cbo.execDataNoTranslate(sSQL.toString());
      }

      if ((caccountmonth.equals(sLastMonth)) && (cvos.length != 0)) {
        for (int i = 0; i < mvos.length; i++) {
          MonthledgerVO oneVO = mvos[i];
          oneVO.setCaccountyear(caccountyear);
          oneVO.setCaccountmonth(caccountmonth);
          oneVO.setFrecordtypeflag(new Integer(5));
        }

        mbo.insertArray(mvos);
      }

      if (mvos.length != 0) {
        mvos = getWriteVO(mvos, sNewSysInfo, 3, 0, mbo);

        mbo.insertArray(mvos);
      }

      if (mvos.length != 0) {
        if ((sPeriousMonth.equals("00")) || (!caccountmonth.equals("01")))
        {
          mvos = getWriteVO(mvos, sNewSysInfo, 2, 0, mbo);
        }
        else
        {
          for (int i = 0; i < mvos.length; i++) {
            MonthledgerVO oneVO = mvos[i];
            oneVO.setFrecordtypeflag(new Integer(2));
          }

        }

        mbo.insertArray(mvos);
      }

      sSQL = new StringBuffer(" update ");
      sSQL.append(" ia_generalledger ");
      sSQL.append(" set ");
      sSQL.append(" nplanedprice = a.jhj ");
      sSQL.append(" from ");
      sSQL.append(" bd_produce a,ia_generalledger b ");
      sSQL.append(" where ");
      sSQL.append(" a.pk_corp = '" + pk_corp + "'");
      sSQL.append(" and ");
      sSQL.append(" a.pricemethod = 5");
      sSQL.append(" and ");
      sSQL.append(" a.pk_calbody = b.crdcenterid ");
      sSQL.append(" and ");
      sSQL.append(" a.pk_invmandoc = b.cinventoryid ");
      sSQL.append(" and ");
      sSQL.append(" b.frecordtypeflag = 4 ");
      sSQL.append(" and ");
      sSQL.append(" b.caccountyear = '" + caccountyear + "'");
      sSQL.append(" and ");
      sSQL.append(" b.caccountmonth = '" + caccountmonth + "'");

      cbo.execData(sSQL.toString());

      writeAccountFlag(true, sSysinfo[0], sSysinfo[3], sSysinfo[4], null, 
        null);

      BillVO conditionbvo = new BillVO();
      BillHeaderVO conditionbhvo = new BillHeaderVO();
      BillItemVO[] conditionbtvos = new BillItemVO[1];
      conditionbtvos[0] = new BillItemVO();

      conditionbhvo.setPk_corp(pk_corp);
      conditionbhvo.setCaccountyear(caccountyear);
      conditionbhvo.setCaccountmonth(caccountmonth);
      conditionbhvo.setCbilltypecode("I6");
      conditionbhvo.setBwithdrawalflag(new UFBoolean("Y"));

      conditionbtvos[0].setCSQLClause("nnumber<0");

      conditionbvo.setParentVO(conditionbhvo);
      conditionbvo.setChildrenVO(conditionbtvos);

      bvos = bbo.queryByVO(conditionbvo, null, null, 
        new ClientLink(null, null, 
        null, null, null, null, null, null, null, false, null, null, null));

      if ((bvos != null) && (bvos.length != 0))
      {
        UFDate sAccountDate = cbo.getMonthBeginDate(pk_corp, sNextPeriod);

        for (int i = 0; i < bvos.length; i++) {
          BillVO oneBillVO = bvos[i];

          BillHeaderVO bhvo = (BillHeaderVO)oneBillVO.getParentVO();

          bhvo.setCbillid(null);

          bhvo.setDbilldate(sAccountDate);

          bhvo.setCaccountyear(null);
          bhvo.setCaccountmonth(null);

          bhvo.setBauditedflag(new UFBoolean("N"));

          bhvo.setBwithdrawalflag(new UFBoolean("Y"));

          bhvo.setVbillcode(null);

          BillItemVO[] btvos = (BillItemVO[])oneBillVO.getChildrenVO();
          for (int j = 0; j < btvos.length; j++) {
            BillItemVO oneItemVO = btvos[j];

            oneItemVO.setCbill_bid(null);

            oneItemVO.setVbillcode(null);

            UFDouble dNumber = oneItemVO.getNnumber();
            if (dNumber != null) {
              oneItemVO.setNnumber(new UFDouble(-1).multiply(dNumber));
            }

            oneItemVO.setDbizdate(sAccountDate);

            oneItemVO.setNmoney(null);
            oneItemVO.setNprice(null);

            oneItemVO.setNinvarymny(null);
            oneItemVO.setNoutvarymny(null);

            UFDouble dAstNumber = oneItemVO.getNassistnum();
            if (dAstNumber != null) {
              oneItemVO.setNassistnum(new UFDouble(-1).multiply(dAstNumber));
            }

            oneItemVO.setFdatagetmodelflag(new Integer(5));
            oneItemVO.setFolddatagetmodelflag(new Integer(5));

            UFDouble dPlanedMny = oneItemVO.getNplanedmny();
            if (dPlanedMny != null) {
              oneItemVO.setNplanedmny(new UFDouble(-1).multiply(dPlanedMny));
            }

            oneItemVO.setNsettledretractnum(null);

            oneItemVO.setCadjustbillitemid(null);

            oneItemVO.setCinbillitemid(null);

            oneItemVO.setCauditorid(null);
            oneItemVO.setDauditdate(null);
            oneItemVO.setIauditsequence(null);

            oneItemVO.setBrtvouchflag(new UFBoolean("N"));

            oneItemVO.setCvoucherid(null);
            oneItemVO.setCsumrtvouchid(null);
          }

        }

        bvos = bbo.insertArrayForAccount(cl, bvos);
      }

      PeriodController.doPeriodAccount(pk_corp, caccountyear + "-" + 
        caccountmonth);

      if (PeriodController.IsPeriodAB(pk_corp, caccountyear + "-" + 
        caccountmonth)) {
        PeriodController.doABAccount(pk_corp, caccountyear + "-" + 
          caccountmonth);
      }

      ParamVO308 param = new ParamVO308();
      param.setPkCorp(pk_corp);
      param.setMethodID(0);
      param.setPeriod(caccountyear + "-" + caccountmonth);

    //注释2018年7月16日10:06:51 彭佳佳  结账数据校验
    DataRightImpl dbo = new DataRightImpl();
    ParamVO308 pvo = dbo.pPortal(param);
    if ((pvo != null) && (pvo.getErrorAccounts() != null) &&  (pvo.getErrorAccounts().length != 0)) {
    	message = NCLangResOnserver.getInstance().getStrByID( "20141060", "UPP20141060-000007");
    	throw new BusinessException(message);
    }
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
      lock.unlock();
    }
  }

  public UFBoolean reAccount(String[] sSysinfo, ClientLink cl)
    throws BusinessException
  {
    String pk_corp = sSysinfo[0];
    String caccountyear = sSysinfo[3];
    String caccountmonth = sSysinfo[4];
    String userID = sSysinfo[5];

    String[] sPK = (String[])null;
    String[] sItemIDs = (String[])null;

    LockOperator lock = new LockOperator(userID);
    try
    {
      BillImpl bbo = new BillImpl();
      CommonDataImpl cbo = new CommonDataImpl();

      String message = NCLangResOnserver.getInstance().getStrByID(
        "20146010", "UPP20146010-000055");

      lock.lock(pk_corp + "ACCOUNT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "BILLADD", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "ALLOCACCOUNT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "AUDIT", message);

      message = NCLangResOnserver.getInstance().getStrByID("20147020", 
        "UPP20147020-000026");

      lock.lock(pk_corp + "ENDHANDLEACCOUNT", message);

      String sPeriousPeriod = cbo.getPerviousPeriod(pk_corp, caccountyear + "-" + 
        caccountmonth);
      int iIndex = sPeriousPeriod.indexOf("-");
      String sPeriousYear = sPeriousPeriod.substring(0, iIndex);
      String sPeriousMonth = sPeriousPeriod.substring(iIndex + 1);

      String sNextPeriod = cbo.getNextPeriod(pk_corp, caccountyear + "-" + 
        caccountmonth);
      iIndex = sNextPeriod.indexOf("-");
      String sNextYear = sNextPeriod.substring(0, iIndex);
      String sNextMonth = sNextPeriod.substring(iIndex + 1);

      UFDate dNextBeginDate = cbo.getMonthBeginDate(pk_corp, sNextPeriod);
      UFDate dNextEndDate = cbo.getMonthEndDate(pk_corp, sNextPeriod);

      String sStartPeriod = cbo.getStartPeriod(pk_corp);
      iIndex = sStartPeriod.indexOf("-");

      String sLastYearPeriod = cbo.getPerviousPeriod(pk_corp, caccountyear + 
        "-01");
      iIndex = sLastYearPeriod.indexOf("-");

      StringBuffer sSQL = new StringBuffer();

      //checkReaccount(pk_corp, caccountyear, caccountmonth);

      if (PeriodController.IsPeriodAB(pk_corp, caccountyear + "-" + 
        caccountmonth))
      {
        PeriodController.redoABAccount(pk_corp, caccountyear + "-" + 
          caccountmonth);
      }

      PeriodController.redoPeriodAccount(pk_corp, caccountyear + "-" + 
        caccountmonth);

      BillVO bvo = new BillVO();
      BillHeaderVO bhvo = new BillHeaderVO();
      bhvo.setPk_corp(pk_corp);
      bhvo.setBwithdrawalflag(new UFBoolean("Y"));
      bhvo.setCSQLClause("dbilldate >= '" + dNextBeginDate + 
        "' and dbilldate <= '" + dNextEndDate + "' and nnumber > 0 ");

      bvo.setParentVO(bhvo);

      BillVO[] bJTLVOs = bbo.queryByVO(bvo, null, null, 
        new ClientLink(null, 
        null, null, null, null, null, null, null, null, false, null, null, 
        null));

      if ((bJTLVOs != null) && (bJTLVOs.length != 0))
      {
        int iLength = bJTLVOs.length;
        for (int i = 0; i < iLength; i++)
        {
          bbo.delete(cl, bJTLVOs[i], userID, null, null);
        }

      }

      sSQL = new StringBuffer(" delete from ");
      sSQL.append(" ia_monthledger ");
      sSQL.append(" where ");
      sSQL.append(" pk_corp = '" + pk_corp + "'");
      sSQL.append(" and ");
      sSQL.append(" caccountyear = '" + caccountyear + "'");
      sSQL.append(" and ");
      sSQL.append(" caccountmonth = '" + caccountmonth + "'");
      sSQL.append(" and ");
      sSQL.append(" ( ");
      sSQL.append(" frecordtypeflag = 2 ");
      sSQL.append(" or ");
      sSQL.append(" frecordtypeflag = 3 ");
      sSQL.append(" or ");
      sSQL.append(" frecordtypeflag = 5 ");
      sSQL.append(" ) ");

      cbo.execData(sSQL.toString());

      sSQL = new StringBuffer(" update ");
      sSQL.append(" ia_generalledger ");
      sSQL.append(" set ");
      sSQL.append(" btryflag = 'N', ");
      sSQL.append(" caccountyear = '" + caccountyear + "',");
      sSQL.append(" caccountmonth = '" + caccountmonth + "'");
      sSQL.append(" where ");
      sSQL.append(" pk_corp = '" + pk_corp + "'");
      sSQL.append(" and ");
      sSQL.append(" caccountyear = '" + sNextYear + "'");
      sSQL.append(" and ");
      sSQL.append(" caccountmonth = '" + sNextMonth + "'");
      sSQL.append(" and ");
      sSQL.append(" frecordtypeflag = 4 ");

      cbo.execData(sSQL.toString());

      writeAccountFlag(false, sSysinfo[0], caccountyear, caccountmonth, 
        sPeriousYear, sPeriousMonth);

      ParamVO308 param = new ParamVO308();
      param.setPkCorp(pk_corp);
      param.setMethodID(0);
      param.setPeriod(caccountyear + '-' + caccountmonth);

      DataRightImpl dbo = new DataRightImpl();
      dbo.pPortal(param);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
      lock.unlock();
    }

    return new UFBoolean(true);
  }
}