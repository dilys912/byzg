package nc.bs.ic.pub.ictoia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.ic611.MachineAccountDMO;
import nc.bs.ic.ic637.StockAgeDMO;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.ModuleEnable;
import nc.bs.ic.pub.PriceDMO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.bill.ICLockBO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.ia.bill.IBill;
import nc.itf.ia.service.IBillService;
import nc.itf.ic.service.IICToIA;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.outter.AccountCheckVO;
import nc.vo.ia.outter.SettledInfoVO;
import nc.vo.ic.ic637.StockAgeItemVO;
import nc.vo.ic.ic637.StockAgeVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.PubVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.SwitchObject;
import nc.vo.scm.pub.SCMEnv;

public class Ic2IaDMO extends DataManageObject
  implements IICToIA
{
  private Hashtable m_htbBilltype = null;

  private final UFDouble UFD_ZERO = new UFDouble(0.0D);

  public Ic2IaDMO()
    throws NamingException, SystemException
  {
  }

  public Ic2IaDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public SettledInfoVO[] getOnHand(String pk_corp, UFDate dEndDate) throws BusinessException
  {
    try {
      MachineAccountDMO dmo = new MachineAccountDMO();
      SettledInfoVO[] sivos = dmo.queryTaiZhangInfo(pk_corp, dEndDate);

      if ((sivos != null) && (sivos.length > 0) && (sivos[0] != null)) {
        String pk_calbody = sivos[0].getSCalbodyID();

        PriceDMO dmoPrice = new PriceDMO();
        ArrayList alInvID = new ArrayList();
        for (int i = 0; i < sivos.length; i++)
          alInvID.add(sivos[i].getSInventoryID());
        Hashtable htPrice = dmoPrice.getPriceByStep(pk_corp, pk_calbody, null, alInvID);

        UFDouble ufdMoney = null;
        UFDouble ZERO = new UFDouble(0.0D);
        UFDouble ufdPrice = ZERO;

        if (sivos != null) {
          for (int i = 0; i < sivos.length; i++) {
            if (htPrice != null) {
              ufdPrice = (UFDouble)htPrice.get(sivos[i].getSInventoryID());
            }
            if (ufdPrice == null) {
              ufdPrice = ZERO;
            }
            ufdMoney = ufdPrice.multiply(sivos[i].getNnumber() == null ? ZERO : sivos[i].getNnumber());

            sivos[i].setM_nprice(ufdPrice);
            sivos[i].setM_nmoney(ufdMoney);
          }
        }
      }
      return sivos;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  private StockAgeItemVO[] containInvID(StockAgeItemVO[] voaChild, String sInvID)
  {
    ArrayList alRet = new ArrayList();
    StockAgeItemVO[] voaRet = null;

    if ((voaChild == null) || (voaChild.length <= 0))
      return null;
    int len = voaChild.length;
    String sInvIDGet = null;
    for (int i = 0; i < len; i++) {
      sInvIDGet = voaChild[i].getCinventoryid();
      if ((sInvIDGet == null) || (sInvIDGet.trim().length() == 0))
        continue;
      sInvIDGet = sInvIDGet.trim();
      if (sInvIDGet.equalsIgnoreCase(sInvID)) {
        alRet.add(voaChild[i]);
      }
    }
    if (alRet.size() <= 0)
      return null;
    voaRet = new StockAgeItemVO[alRet.size()];
    alRet.toArray(voaRet);

    return voaRet;
  }

  public UFDouble[] getNfixdisassemblymny(String[] sCspecialhids)
    throws BusinessException
  {
    if ((null == sCspecialhids) || (sCspecialhids.length == 0)) {
      SCMEnv.out("传入的特殊单ID不能为空。");
      return null;
    }
    String sCspecialhidIn = "''";
    for (int i = 0; i < sCspecialhids.length; i++) {
      if (null != sCspecialhids[i]) {
        sCspecialhidIn = sCspecialhidIn + ",'" + sCspecialhids[i].trim() + "'";
      }

    }

    ArrayList alNfixdisassemblymnys = new ArrayList();

    UFDouble[] ufdReturnValue = new UFDouble[sCspecialhids.length];

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    int ooo = 0;

    String sCspecialhID = null;
    UFDouble ufdValue = null;

    String sSql = "\tselect cspecialhid,coalesce(nfixdisassemblymny,0) as nfixdisassemblymny from ic_special_h where ic_special_h.dr=0 and cspecialhid in (" + sCspecialhidIn + ") ";
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      UFDouble aufdouble[];
      rs = stmt.executeQuery();

      while (rs.next())
      {
        ArrayList row = new ArrayList();
        sCspecialhID = rs.getString(1);
        ufdValue = new UFDouble(rs.getDouble(2));
        row.add(sCspecialhID);
        row.add(ufdValue);
        alNfixdisassemblymnys.add(row);
      }

      for (int j = 0; j < ufdReturnValue.length; j++) {
        if (null != sCspecialhids[j]) {
          for (int i = 0; i < alNfixdisassemblymnys.size(); i++) {
            if (!((ArrayList)(ArrayList)alNfixdisassemblymnys.get(i)).get(0).toString().trim().equals(sCspecialhids[j].trim())) {
              continue;
            }
            ufdReturnValue[j] = ((UFDouble)((ArrayList)(ArrayList)alNfixdisassemblymnys.get(i)).get(1));

            break;
          }
        }
        else {
          ufdReturnValue[j] = new UFDouble(0);
        }
      }
      aufdouble = ufdReturnValue;
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return null;
  }

  private String getIcBillTypeName(String sBilltypecode)
  {
    if (this.m_htbBilltype == null) {
      this.m_htbBilltype = new Hashtable();
      this.m_htbBilltype.put("40", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000038"));

      this.m_htbBilltype.put("41", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000039"));

      this.m_htbBilltype.put("42", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000040"));

      this.m_htbBilltype.put("43", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000041"));

      this.m_htbBilltype.put("44", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000042"));

      this.m_htbBilltype.put("45", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000043"));

      this.m_htbBilltype.put("46", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000044"));

      this.m_htbBilltype.put("47", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000045"));

      this.m_htbBilltype.put("48", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000046"));

      this.m_htbBilltype.put("49", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000047"));

      this.m_htbBilltype.put("4A", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000048"));

      this.m_htbBilltype.put("4B", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000049"));

      this.m_htbBilltype.put("4C", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000050"));

      this.m_htbBilltype.put("4D", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000051"));

      this.m_htbBilltype.put("4F", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000052"));

      this.m_htbBilltype.put("4G", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000053"));

      this.m_htbBilltype.put("4H", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000054"));

      this.m_htbBilltype.put("4I", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000055"));

      this.m_htbBilltype.put("4J", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000056"));

      this.m_htbBilltype.put("4K", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000057"));

      this.m_htbBilltype.put("4L", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000058"));

      this.m_htbBilltype.put("4M", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000059"));

      this.m_htbBilltype.put("4N", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000060"));

      this.m_htbBilltype.put("4O", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000061"));

      this.m_htbBilltype.put("4P", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000062"));

      this.m_htbBilltype.put("4Q", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000063"));

      this.m_htbBilltype.put("4R", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000064"));

      this.m_htbBilltype.put("4S", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000065"));

      this.m_htbBilltype.put("4T", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000066"));

      this.m_htbBilltype.put("4U", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000067"));

      this.m_htbBilltype.put("4V", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000068"));

      this.m_htbBilltype.put("4W", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000069"));

      this.m_htbBilltype.put("4X", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000070"));

      this.m_htbBilltype.put("4Z", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000071"));

      this.m_htbBilltype.put("4E", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000503"));

      this.m_htbBilltype.put("4Y", NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000504"));
    }

    String sBillTypename = null;
    if (sBilltypecode != null) {
      sBillTypename = (String)this.m_htbBilltype.get(sBilltypecode);
    }

    return sBillTypename;
  }

  public String[][] getSourceInfo(String[] s4EItemIDs)
    throws SQLException
  {
    if ((s4EItemIDs == null) || (s4EItemIDs.length == 0)) {
      SCMEnv.out("--->param null,no bill pk");
      return (String[][])null;
    }

    StringBuffer sql = new StringBuffer(" select cgeneralbid,csourcetype,csourcebillbid from ic_general_b where dr=0 ");

    sql.append(GeneralSqlString.formInSQL("cgeneralbid", s4EItemIDs));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    HashMap ht = new HashMap();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        String[] ss = new String[2];
        ss[0] = rs.getString(2);
        ss[1] = rs.getString(3);
        ht.put(s, ss);
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    String[][] sValues = new String[s4EItemIDs.length][2];

    for (int i = 0; i < s4EItemIDs.length; i++) {
      if (ht.containsKey(s4EItemIDs[i])) {
        String[] ss = (String[])(String[])ht.get(s4EItemIDs[i]);
        sValues[i] = ss;
      }

    }

    return sValues;
  }

  public AggregatedValueObject[] queryInitBills(String sCorpID)
    throws BusinessException
  {
    if ((sCorpID == null) || (sCorpID.trim().length() == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000072"));
    }

    try
    {
      ArrayList alRet = new GeneralBillDMO().queryBills(new QryConditionVO(" head.pk_corp='" + sCorpID + "' AND (head.fbillflag=" + "3" + " OR head.fbillflag=" + "4" + ") \n AND (head.cbilltypecode ='" + BillTypeConst.m_initIn + "') and head.cwarehouseid in (select pk_stordoc from bd_stordoc where pk_corp='" + sCorpID + "' and iscalculatedinvcost='Y')\n "));

      GeneralBillVO[] voaRet = null;

      if ((alRet != null) && (alRet.size() > 0)) {
        voaRet = new GeneralBillVO[alRet.size()];
        for (int i = 0; i < alRet.size(); i++)
          voaRet[i] = ((GeneralBillVO)alRet.get(i));
      }
      return voaRet;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public GeneralBillVO[] queryInitPureBills(String sCorpID)
    throws Exception
  {
    if ((sCorpID == null) || (sCorpID.trim().length() == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000072"));
    }

    GeneralBillVO[] voaRet = null;

    GeneralBillDMO dmoBill = new GeneralBillDMO();
    QryConditionVO voQc = new QryConditionVO(" head.pk_corp='" + sCorpID + "' AND (head.fbillflag=" + "3" + " OR head.fbillflag=" + "4" + ") \n AND (head.cbilltypecode ='" + BillTypeConst.m_initIn + "') \n ");

    voQc.setIntParam(0, 500);

    ArrayList alRet = dmoBill.queryBills(voQc);

    if ((alRet != null) && (alRet.size() > 0)) {
      voaRet = new GeneralBillVO[alRet.size()];
      for (int i = 0; i < alRet.size(); i++) {
        voaRet[i] = ((GeneralBillVO)alRet.get(i));
      }
    }
    return voaRet;
  }

  public AccountCheckVO[] queryUnAuditedBills_old(String sCorpID, UFDate dYear)
    throws BusinessException
  {
    if ((sCorpID == null) || (sCorpID.trim().length() == 0) || (dYear == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000073"));
    }

    beforeCallMethod("nc.bs.ic.pub.ictoia.Ic2IaDMO", "queryUnAuditedBills", new Object[] { sCorpID, dYear });

    Vector vQueryResult = new Vector();
    AccountCheckVO accountCheckVo = null;
    AccountCheckVO[] accountCheckVos = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer strSql = new StringBuffer(500);

    strSql.append(" select /*+ index(ic_general_b) */ ");
    strSql.append(" cbilltypecode,vbillcode,");
    strSql.append(" ninnum,noutnum,nprice,nmny,");
    strSql.append(" dbizdate,daccountdate,ic_general_h.pk_calbody,ic_general_h.cwarehouseid,ic_general_b.cinventoryid");

    strSql.append(" from ic_general_b");
    strSql.append(" inner join ic_general_h on ic_general_h.cgeneralhid = ic_general_b.cgeneralhid");

    strSql.append(" where ");
    strSql.append(" ic_general_b.dr = 0 and ic_general_h.dr = 0 ");

    strSql.append(" and ic_general_h.pk_corp = '");
    strSql.append(sCorpID);
    strSql.append("' and ic_general_h.fbillflag=2 \n AND ic_general_h.cbilltypecode in ('" + BillTypeConst.m_initIn + "','" + BillTypeConst.m_purchaseIn + "','" + BillTypeConst.m_productIn + "','" + BillTypeConst.m_consignMachiningIn + "','" + BillTypeConst.m_otherIn + "','" + BillTypeConst.m_materialOut + "','" + BillTypeConst.m_saleOut + "','" + BillTypeConst.m_otherOut + "','" + BillTypeConst.m_consignMachiningOut + "','" + BillTypeConst.m_discardOut + "') And ic_general_b.dbizdate <= '" + dYear.toString() + "' \n ");

    strSql.append(" ");

    SCMEnv.out(strSql.toString());
    long t = System.currentTimeMillis();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        accountCheckVo = new AccountCheckVO();

        accountCheckVo.setCmodulecode("IC");

        String sCbilltypecode = rs.getString(1);

        accountCheckVo.setCbilltype(sCbilltypecode == null ? "" : getIcBillTypeName(sCbilltypecode));

        String sVbillcode = rs.getString(2);
        accountCheckVo.setVbillcode(sVbillcode == null ? "" : sVbillcode.trim());

        Object oNinnum = rs.getObject(3);
        UFDouble ufNinnum = oNinnum == null ? new UFDouble(0.0D) : new UFDouble(oNinnum.toString());

        Object oNoutnum = rs.getObject(4);
        UFDouble ufNoutnum = oNoutnum == null ? new UFDouble(0.0D) : new UFDouble(oNoutnum.toString());

        if ((sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_purchaseIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_productIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_consignMachiningIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_otherIn)))
        {
          accountCheckVo.setNnumber(ufNinnum);
        }
        else accountCheckVo.setNnumber(ufNoutnum);

        Object oNprice = rs.getObject(5);
        accountCheckVo.setNprice(oNprice == null ? new UFDouble(0.0D) : new UFDouble(oNprice.toString()));

        Object oNmny = rs.getObject(6);
        UFDouble ufNmny = oNmny == null ? new UFDouble(0.0D) : new UFDouble(oNmny.toString());

        accountCheckVo.setNmoney(ufNmny);

        Object odbizdate = rs.getObject(7);
        accountCheckVo.setDbilldate(odbizdate == null ? new UFDate("", false) : new UFDate(odbizdate.toString(), false));

        Object oDaccountdate = rs.getObject(8);
        accountCheckVo.setDregisterdate(oDaccountdate == null ? new UFDate("", false) : new UFDate(oDaccountdate.toString(), false));

        String sPk_calbody = rs.getString(9);
        accountCheckVo.setPk_calbody(sPk_calbody == null ? "" : sPk_calbody.trim());

        String sCwarehouseid = rs.getString(10);
        accountCheckVo.setPk_stordoc(sCwarehouseid == null ? "" : sCwarehouseid.trim());

        String sCinventoryid = rs.getString(11);
        accountCheckVo.setPk_invmandoc(sCinventoryid == null ? "" : sCinventoryid.trim());

        vQueryResult.add(accountCheckVo);
      }
      accountCheckVos = new AccountCheckVO[vQueryResult.size()];
      if (vQueryResult.size() > 0) {
        vQueryResult.copyInto(accountCheckVos);
      }
      t = System.currentTimeMillis() - t;
      SCMEnv.out("执行方法:nc.bs.ic.pub.ictoia.Ic2IaDMO.queryUnAuditedBills()所消耗的时间为：" + t + " ms。");
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ic.pub.ictoia.Ic2IaDMO", "queryUnAuditedBills", new Object[] { sCorpID, dYear });

    return accountCheckVos;
  }

  private HashMap sumAllItemBySD(StockAgeItemVO[] voaItem, int len)
  {
    HashMap hmRet = new HashMap();

    String sConstant = "numberinday";
    String key = null;

    for (int i = 0; i < len; i++) {
      key = sConstant + i;
      UFDouble sumNum = new UFDouble(0);
      for (int j = 0; j < voaItem.length; j++) {
        if (voaItem[j].getAttributeValue(key) != null) {
          sumNum = sumNum.add((UFDouble)voaItem[j].getAttributeValue(key));
        }
      }
      hmRet.put(key, sumNum);
    }
    return hmRet;
  }

  public void setSettledFlag(AggregatedValueObject voBill)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.pu.ic.ToICDMO", "setSettledFlag", new Object[] { voBill });

    String sSqlQuery = "select ninnum, nmny from ic_general_b where cgeneralhid = ? and cgeneralbid = ?";
    String sSqlUpdate1 = "update ic_general_bb3 set naccountnum1 = ?, naccountmny = ? where cgeneralhid = ? and cgeneralbid = ?";
    String sSqlUpdate2 = "update ic_general_b set isok = ? where cgeneralhid = ? and cgeneralbid = ?";

    Connection con = null;
    PreparedStatement stmtQuery = null;
    PreparedStatement stmtUpdate1 = null;
    PreparedStatement stmtUpdate2 = null;

    CircularlyAccessibleValueObject[] itemVOs = voBill.getChildrenVO();
    if ((itemVOs == null) || (itemVOs.length == 0))
      return;
    try
    {
      con = getConnection();
      stmtQuery = con.prepareStatement(sSqlQuery);
      stmtUpdate1 = prepareStatement(con, sSqlUpdate1);
      stmtUpdate2 = prepareStatement(con, sSqlUpdate2);

      for (int i = 0; i < itemVOs.length; i++)
      {
        stmtQuery.setString(1, (String)itemVOs[i].getAttributeValue("csourcebillid"));

        stmtQuery.setString(2, (String)itemVOs[i].getAttributeValue("csourcebillitemid"));

        ResultSet rs = stmtQuery.executeQuery();
        UFDouble nInNum = new UFDouble(0.0D);
        UFDouble nMoney = new UFDouble(0.0D);
        if (rs.next()) {
          BigDecimal d1 = null;
          Object oTemp = rs.getObject(1);
          if ((oTemp != null) && (oTemp.toString().trim().length() > 0))
            d1 = new BigDecimal(oTemp.toString());
          if ((d1 != null) && (d1.toString().length() > 0)) {
            nInNum = new UFDouble(d1);
          }
          BigDecimal d2 = null;
          oTemp = rs.getObject(2);
          if ((oTemp != null) && (oTemp.toString().trim().length() > 0))
            d2 = new BigDecimal(oTemp.toString());
          if ((d2 != null) && (d2.toString().length() > 0))
            nMoney = new UFDouble(d2);
        }
        if (rs != null) {
          rs.close();
        }

        stmtUpdate1.setBigDecimal(1, nInNum.toBigDecimal());
        stmtUpdate1.setBigDecimal(2, nMoney.toBigDecimal());
        stmtUpdate1.setString(3, (String)itemVOs[i].getAttributeValue("csourcebillid"));

        stmtUpdate1.setString(4, (String)itemVOs[i].getAttributeValue("csourcebillitemid"));

        executeUpdate(stmtUpdate1);

        stmtUpdate2.setString(1, "Y");
        stmtUpdate2.setString(2, (String)itemVOs[i].getAttributeValue("csourcebillid"));

        stmtUpdate2.setString(3, (String)itemVOs[i].getAttributeValue("csourcebillitemid"));

        executeUpdate(stmtUpdate2);
      }

      executeBatch(stmtUpdate1);
      executeBatch(stmtUpdate2);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    finally {
      try {
        if (stmtQuery != null)
          stmtQuery.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmtUpdate1 != null)
          stmtUpdate1.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmtUpdate2 != null)
          stmtUpdate2.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.pu.ic.ToICDMO", "setSettledFlag", new Object[] { voBill });
  }

  private ArrayList getNumZero(int klLen)
  {
    ArrayList alNumZero = new ArrayList();
    for (int i = 0; i < klLen; i++) {
      alNumZero.add(this.UFD_ZERO);
    }
    return alNumZero;
  }

  public Map queryWhAge(String sLogDate, String sOtherCondition, ArrayList alAgeStage, Map mapInvNum)
    throws BusinessException
  {
    try
    {
      ArrayList alInvIDs = new ArrayList();
      Object[] oaMap = null;
      String sQryInvSubSql = null;

      if ((mapInvNum == null) || (mapInvNum.size() <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000074"));
      }

      oaMap = new Object[mapInvNum.size()];
      mapInvNum.keySet().toArray(oaMap);

      for (int i = 0; i < mapInvNum.size(); i++) {
        if (oaMap[i] == null)
          continue;
        alInvIDs.add((String)oaMap[i]);
      }
      String sTemptableSQL = GeneralSqlString.formInSQL("cinventoryid", alInvIDs);

      sQryInvSubSql = sOtherCondition + sTemptableSQL;

      StockAgeDMO dmoKl = new StockAgeDMO();

      String sKlsds = null;
      if ((alAgeStage == null) || (alAgeStage.size() <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000074"));
      }

      String[] saKlsd = new String[alAgeStage.size()];
      for (int i = 0; i < alAgeStage.size(); i++) {
        saKlsd[i] = ((String)alAgeStage.get(i));
      }

      ArrayList alFxfs = new ArrayList();
      alFxfs.add(new Integer(0));

      StockAgeVO voAge = dmoKl.queryOutInfo(alFxfs, saKlsd, sQryInvSubSql, sLogDate);

      StockAgeItemVO[] voaChild = (StockAgeItemVO[])(StockAgeItemVO[])voAge.getChildrenVO();

      if ((voaChild == null) || (voaChild.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000075"));
      }

      int iMapsize = mapInvNum.size();

      Map mapReturn = new HashMap();

      StringBuffer sbMsg = new StringBuffer();
      String sMsg = null;
      ArrayList alMsgError = new ArrayList();

      for (int i = 0; i < oaMap.length; i++) {
        if (oaMap[i] == null)
          continue;
        String sEachInvID = (String)oaMap[i];
        UFDouble dbEachInvNumIA = (UFDouble)mapInvNum.get(sEachInvID);

        ArrayList alNumGet = new ArrayList();

        StockAgeItemVO[] voaItem = null;
        voaItem = containInvID(voaChild, sEachInvID);
        if ((voaItem == null) || (voaItem.length <= 0)) {
          mapReturn.put(sEachInvID, getNumZero(saKlsd.length + 1));
        }
        else
        {
          String sConstant = null;

          for (int j = 0; j < saKlsd.length + 1; j++)
          {
            if ((dbEachInvNumIA == null) || (dbEachInvNumIA.doubleValue() <= 0.0D))
            {
              alNumGet.add(this.UFD_ZERO);
            }
            else {
              sConstant = "numberinday" + j;
              UFDouble ufdNum = null;

              HashMap hmSum = sumAllItemBySD(voaItem, saKlsd.length + 1);

              UFDouble ufdNumItem = (UFDouble)hmSum.get(sConstant);
              if (ufdNumItem == null) {
                alNumGet.add(this.UFD_ZERO);
              }
              else
              {
                if (dbEachInvNumIA.doubleValue() <= ufdNumItem.doubleValue())
                {
                  ufdNum = dbEachInvNumIA;
                } else if (dbEachInvNumIA.doubleValue() > ufdNumItem.doubleValue())
                {
                  ufdNum = ufdNumItem;
                }
                alNumGet.add(ufdNum);
                dbEachInvNumIA = dbEachInvNumIA.sub(ufdNum);
              }
            }
          }
          mapReturn.put(sEachInvID, alNumGet);
        }
      }

      if (alMsgError.size() > 0) {
        for (int i = 0; i < alMsgError.size(); i++) {
          sbMsg.append((String)alMsgError.get(i));
        }
        throw new BusinessException(sbMsg.toString());
      }

      return mapReturn;
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public void setAuditInfo(ArrayList alBillInfo)
    throws BusinessException
  {
    if ((alBillInfo == null) || (alBillInfo.size() == 0)) {
      return;
    }
    String sql = "update ic_general_h set cauditorid = ?, dauditdate = ? ,fbillflag= ? where cgeneralhid = ?";
    try {
      SmartDMO dmo = new SmartDMO();

      ArrayList alType = new ArrayList();
      alType.add(new Integer(3));
      alType.add(new Integer(3));
      alType.add(new Integer(2));
      alType.add(new Integer(3));

      ArrayList alValue = new ArrayList();
      int size = alBillInfo.size();
      for (int i = 0; i < size; i++) {
        if (alBillInfo.get(i) != null) {
          ArrayList alTemp = (ArrayList)alBillInfo.get(i);
          if ((alTemp != null) && (alTemp.size() == 3)) {
            ArrayList alBillValue = new ArrayList();
            alBillValue.add(alTemp.get(1));
            alBillValue.add(alTemp.get(2));
            if (alTemp.get(1) != null)
              alBillValue.add(new Integer("4"));
            else {
              alBillValue.add(new Integer("3"));
            }
            alBillValue.add(alTemp.get(0));
            alValue.add(alBillValue);
          }
        }
      }
      dmo.executeUpdateBatch(sql, alValue, alType);
    } catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000077"));
    }
  }

  public void setCostInfo(ArrayList alBillInfo)
    throws BusinessException
  {
    if ((alBillInfo == null) || (alBillInfo.size() == 0)) {
      return;
    }
    int size = alBillInfo.size();
    ArrayList alTemp = null;

    for (int i = 0; i < size; i++) {
      alTemp = (ArrayList)alBillInfo.get(i);
      if (alTemp.size() < 3) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000078"));
      }

      if (alTemp.get(2) == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000079"));
      }

    }

    String sql = "update ic_general_b set  nprice = ?, nmny = ?  where cgeneralbid = ? ";
    try
    {
      SmartDMO dmo = new SmartDMO();

      ArrayList alType = new ArrayList();
      alType.add(new Integer(1));
      alType.add(new Integer(1));
      alType.add(new Integer(3));

      dmo.executeUpdateBatch(sql, alBillInfo, alType);
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000080"));
    }
  }

  public void rewriteZg1(PubVO[] vos)
    throws BusinessException
  {
    rewriteZg(vos, 1);
  }

  public void rewriteZg2(PubVO[] vos)
    throws BusinessException
  {
    rewriteZg(vos, 2);
  }

  private void rewriteZg(PubVO[] vos, int iflag) throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    ArrayList alValueb = new ArrayList();
    ArrayList alValueh = new ArrayList();
    ArrayList alValue3b = new ArrayList();
    ArrayList alValue3h = new ArrayList();
    ArrayList alrow = null;
    for (int i = 0; i < vos.length; i++) {
      alrow = new ArrayList();
      alrow.add(SwitchObject.switchObjToUFBoolean(vos[i].getAttributeValue("btoinzgflag")));

      if (vos[i].getAttributeValue("cgeneralbid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralbid").toString());
        alValueb.add(alrow);
      } else if (vos[i].getAttributeValue("cgeneralhid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralhid").toString());
        alValueh.add(alrow);
      }

      alrow = new ArrayList();
      alrow.add(SwitchObject.switchObjToUFDouble(vos[i].getAttributeValue("nzgprice" + String.valueOf(iflag))));

      alrow.add(SwitchObject.switchObjToUFDouble(vos[i].getAttributeValue("nzgmny" + String.valueOf(iflag))));

      if (vos[i].getAttributeValue("cgeneralbid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralbid").toString());
        alValue3b.add(alrow);
      } else if (vos[i].getAttributeValue("cgeneralhid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralhid").toString());
        alValue3h.add(alrow);
      }
    }

    String sqlb = "update ic_general_b set   btoinzgflag=? where cgeneralbid = ?";
    String sqlh = "update ic_general_b set   btoinzgflag=? where cgeneralhid = ?";

    String sqlbb3b = "update ic_general_bb3 set  nzgprice" + String.valueOf(iflag) + " = ?, nzgmny" + String.valueOf(iflag) + " = ?  where cgeneralbid = ?";

    String sqlbb3h = "update ic_general_bb3 set  nzgprice" + String.valueOf(iflag) + " = ?, nzgmny" + String.valueOf(iflag) + " = ?  where cgeneralhid = ?";
    try
    {
      SmartDMO dmo = new SmartDMO();

      ArrayList alType = new ArrayList();

      alType.add(new Integer(0));
      alType.add(new Integer(3));

      if (alValueb.size() > 0)
        dmo.executeUpdateBatch(sqlb, alValueb, alType);
      if (alValueh.size() > 0) {
        dmo.executeUpdateBatch(sqlh, alValueh, alType);
      }
      alType.clear();
      alType.add(new Integer(1));
      alType.add(new Integer(1));
      alType.add(new Integer(3));
      if (alValue3b.size() > 0)
        dmo.executeUpdateBatch(sqlbb3b, alValue3b, alType);
      if (alValue3h.size() > 0)
        dmo.executeUpdateBatch(sqlbb3h, alValue3h, alType);
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000080"));
    }
  }

  public void rewrite4Y2IAflag(PubVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    ArrayList alValueb = new ArrayList();
    ArrayList alValueh = new ArrayList();

    ArrayList alrow = null;
    for (int i = 0; i < vos.length; i++) {
      alrow = new ArrayList();
      alrow.add(SwitchObject.switchObjToUFBoolean(vos[i].getAttributeValue("btoouttoiaflag")));

      if (vos[i].getAttributeValue("cgeneralbid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralbid").toString());
        alValueb.add(alrow);
      } else if (vos[i].getAttributeValue("cgeneralhid") != null) {
        alrow.add(vos[i].getAttributeValue("cgeneralhid").toString());
        alValueh.add(alrow);
      }

    }

    String sqlb = "update ic_general_b set   btoouttoiaflag=? where cgeneralbid = ?";

    String sqlh = "update ic_general_b set   btoouttoiaflag=? where cgeneralhid = ?";
    try
    {
      SmartDMO dmo = new SmartDMO();

      ArrayList alType = new ArrayList();

      alType.add(new Integer(0));
      alType.add(new Integer(3));

      if (alValueb.size() > 0)
        dmo.executeUpdateBatch(sqlb, alValueb, alType);
      if (alValueh.size() > 0)
        dmo.executeUpdateBatch(sqlh, alValueh, alType);
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000080"));
    }
  }

  public AccountCheckVO[] queryUnAuditedBills(String[] sSysinfo)
    throws BusinessException
  {
    if ((sSysinfo == null) || (sSysinfo.length != 6) || (sSysinfo[4] == null) || ((sSysinfo[3] == null) && (sSysinfo[5] == null)))
    {
      throw new BusinessException("查询未达存货核算明细时参数不完整。");
    }
    String sCorpID = sSysinfo[0];
    String sStartDate = sSysinfo[1];
    String sEndDate = sSysinfo[2];
    String sWHID = sSysinfo[3];
    String sInv = sSysinfo[4];
    String sCalID = sSysinfo[5];

    StringBuffer strSql = new StringBuffer(500);

    strSql.append(" select /*+ index(ic_general_b) */ ");
    strSql.append(" cbilltypecode,vbillcode,");
    strSql.append(" ninnum,noutnum,nprice,nmny,");
    strSql.append(" dbizdate,daccountdate,ic_general_h.pk_calbody,ic_general_h.cwarehouseid,ic_general_b.cinventoryid");

    strSql.append(" from ic_general_b");
    strSql.append(" inner join ic_general_h on ic_general_h.cgeneralhid = ic_general_b.cgeneralhid");

    strSql.append(" where ");
    strSql.append(" ic_general_b.dr = 0 and ic_general_h.dr = 0 ");

    strSql.append(" and ic_general_h.pk_corp = '");
    strSql.append(sCorpID);
    strSql.append("' and ic_general_h.fbillflag=2 \n AND ic_general_h.cbilltypecode in " + getBillType());

    if (sWHID != null)
      strSql.append(" and ic_general_h.cwarehouseid='" + sWHID + "'");
    else if (sCalID != null) {
      strSql.append(" and ic_general_h.pk_calbody='" + sCalID + "'");
    }

    strSql.append(" and ic_general_b.cinventoryid='" + sInv + "' ");
    if ((sStartDate != null) && (sStartDate.trim().length() > 0)) {
      strSql.append(" And ic_general_b.dbizdate >= '" + sStartDate + "' ");
    }

    if ((sEndDate != null) && (sEndDate.trim().length() > 0)) {
      strSql.append(" And ic_general_b.dbizdate <= '" + sEndDate + "' ");
    }

    strSql.append(" ");

    SCMEnv.out(strSql.toString());
    return dealUnAuditedBills(strSql.toString());
  }

  public AccountCheckVO[] queryUnAuditedBills(String sCorpID, UFDate dYear)
    throws BusinessException
  {
    if ((sCorpID == null) || (sCorpID.trim().length() == 0) || (dYear == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000073"));
    }

    StringBuffer strSql = new StringBuffer(500);

    strSql.append(" select /*+ index(ic_general_b) */ ");
    strSql.append(" cbilltypecode,vbillcode,");
    strSql.append(" ninnum,noutnum,nprice,nmny,");
    strSql.append(" dbizdate,daccountdate,ic_general_h.pk_calbody,ic_general_h.cwarehouseid,ic_general_b.cinventoryid");

    strSql.append(" from ic_general_b");
    strSql.append(" inner join ic_general_h on ic_general_h.cgeneralhid = ic_general_b.cgeneralhid");

    strSql.append(" where ");
    strSql.append(" ic_general_b.dr = 0 and ic_general_h.dr = 0 ");

    strSql.append(" and ic_general_h.pk_corp = '");
    strSql.append(sCorpID);
    strSql.append("' and ic_general_h.fbillflag=2 \n AND ic_general_h.cbilltypecode in " + getBillType() + " And ic_general_b.dbizdate <= '" + dYear.toString() + "' \n ");

    strSql.append(" ");

    SCMEnv.out(strSql.toString());

    return dealUnAuditedBills(strSql.toString());
  }

  private AccountCheckVO[] dealUnAuditedBills(String sSQL)
    throws BusinessException
  {
    Vector vQueryResult = new Vector();
    AccountCheckVO accountCheckVo = null;
    AccountCheckVO[] accountCheckVos = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSQL.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        accountCheckVo = new AccountCheckVO();

        accountCheckVo.setCmodulecode("IC");

        String sCbilltypecode = rs.getString(1);

        accountCheckVo.setCbilltype(sCbilltypecode == null ? "" : getIcBillTypeName(sCbilltypecode));

        String sVbillcode = rs.getString(2);
        accountCheckVo.setVbillcode(sVbillcode == null ? "" : sVbillcode.trim());

        Object oNinnum = rs.getObject(3);
        UFDouble ufNinnum = oNinnum == null ? new UFDouble(0.0D) : new UFDouble(oNinnum.toString());

        Object oNoutnum = rs.getObject(4);
        UFDouble ufNoutnum = oNoutnum == null ? new UFDouble(0.0D) : new UFDouble(oNoutnum.toString());

        if ((sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_purchaseIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_productIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_consignMachiningIn)) || (sCbilltypecode.equalsIgnoreCase(BillTypeConst.m_otherIn)))
        {
          accountCheckVo.setNnumber(ufNinnum);
        }
        else accountCheckVo.setNnumber(ufNoutnum);

        Object oNprice = rs.getObject(5);
        accountCheckVo.setNprice(oNprice == null ? new UFDouble(0.0D) : new UFDouble(oNprice.toString()));

        Object oNmny = rs.getObject(6);
        UFDouble ufNmny = oNmny == null ? new UFDouble(0.0D) : new UFDouble(oNmny.toString());

        accountCheckVo.setNmoney(ufNmny);

        Object odbizdate = rs.getObject(7);
        accountCheckVo.setDbilldate(odbizdate == null ? new UFDate("", false) : new UFDate(odbizdate.toString(), false));

        Object oDaccountdate = rs.getObject(8);
        accountCheckVo.setDregisterdate(oDaccountdate == null ? new UFDate("", false) : new UFDate(oDaccountdate.toString(), false));

        String sPk_calbody = rs.getString(9);
        accountCheckVo.setPk_calbody(sPk_calbody == null ? "" : sPk_calbody.trim());

        String sCwarehouseid = rs.getString(10);
        accountCheckVo.setPk_stordoc(sCwarehouseid == null ? "" : sCwarehouseid.trim());

        String sCinventoryid = rs.getString(11);
        accountCheckVo.setPk_invmandoc(sCinventoryid == null ? "" : sCinventoryid.trim());

        vQueryResult.add(accountCheckVo);
      }
      accountCheckVos = new AccountCheckVO[vQueryResult.size()];
      if (vQueryResult.size() > 0)
        vQueryResult.copyInto(accountCheckVos);
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return accountCheckVos;
  }

  public void rewriteNmaterialmoney(PubVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    ArrayList alValue3b = new ArrayList();
    ArrayList alrow = null;
    String[] bids = new String[vos.length];
    for (int i = 0; i < vos.length; i++)
    {
      alrow = new ArrayList();
      alrow.add(vos[i].getAttributeValue("nmaterialmoney"));
      alrow.add(vos[i].getAttributeValue("cgeneralbid").toString());
      alValue3b.add(alrow);

      bids[i] = vos[i].getAttributeValue("cgeneralbid").toString();
    }

    new ICLockBO().lockDynamicPks(bids);

    String sqlbb3b = "update ic_general_bb3 set  nmaterialmoney = ? where cgeneralbid = ? ";
    try
    {
      SmartDMO dmo = new SmartDMO();

      ArrayList alType = new ArrayList();

      alType.add(new Integer(1));
      alType.add(new Integer(3));
      if (alValue3b.size() > 0) {
        dmo.executeUpdateBatch(sqlbb3b, alValue3b, alType);
        dmo.executeUpdate("update ic_general_b set dr=0 where 0=0 " + GeneralSqlString.formInSQL("cgeneralbid", bids), new ArrayList(), new ArrayList());
      }

    }
    catch (Exception e)
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000080"));
    }
  }

  private String getBillType()
  {
    return " ('" + BillTypeConst.m_initIn + "','" + BillTypeConst.m_purchaseIn + "','" + BillTypeConst.m_productIn + "','" + BillTypeConst.m_consignMachiningIn + "','" + BillTypeConst.m_otherIn + "','" + BillTypeConst.m_materialOut + "','" + BillTypeConst.m_saleOut + "','" + BillTypeConst.m_otherOut + "','" + BillTypeConst.m_consignMachiningOut + "','" + BillTypeConst.m_allocationIn + "','" + BillTypeConst.m_allocationOut + "','" + BillTypeConst.m_discardOut + "') ";
  }

  public void saveIABills(GeneralBillVO[] icvos, String icBillType, String iaBillType, PfParameterVO pfvo)
    throws BusinessException
  {
    if ((icvos == null) || (icvos.length <= 0))
      return;
    try {
      String pk_corp = icvos[0].getHeaderVO().getPk_corp();
      if (!new ModuleEnable().isIAEnble(pk_corp))
        return;
      BillVO[] iaVOs = (BillVO[])(BillVO[])PfUtilTools.runChangeDataAry(icBillType, iaBillType, icvos, pfvo);

      IBillService bo = (IBillService)NCLocator.getInstance().lookup(IBillService.class.getName());

      bo.saveBills(iaVOs, "IC", icBillType);
    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
  }

  public void deleteIABills(GeneralBillVO inCurVO)
    throws BusinessException
  {
    if (inCurVO == null)
      return;
    try {
      String pk_corp = inCurVO.getHeaderVO().getPk_corp();
      if (!new ModuleEnable().isIAEnble(pk_corp))
        return;
      String cbilltype = (String)inCurVO.getHeaderVO().getAttributeValue("cbilltypecode");

      String operatorid = (String)inCurVO.getHeaderValue("coperatorid");
      if ((cbilltype.equals(BillTypeConst.m_consignMachiningIn)) || (cbilltype.equals(BillTypeConst.m_allocationIn)) || (cbilltype.equals(BillTypeConst.m_purchaseIn))) {
        operatorid = (String)inCurVO.getHeaderValue("coperatoridnow");
      }
      String pkBillid = (String)inCurVO.getHeaderValue("cgeneralhid");

      IBill bo = (IBill)NCLocator.getInstance().lookup(IBill.class.getName());

      bo.deleteBillFromOutter_bill(pkBillid, operatorid);
    } catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
  }
}