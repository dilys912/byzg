package nc.bs.ic.pub.check;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import javax.naming.NamingException;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.SCMEnv;

public class CheckBusiDMO extends DataManageObject
{
  Timer m_timer = new Timer();

  public CheckBusiDMO()
    throws NamingException, SystemException
  {
  }

  public CheckBusiDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public UFBoolean checkCancelSignOper(AggregatedValueObject vo)
    throws BusinessException
  {
    if (vo != null)
    {
      CircularlyAccessibleValueObject voHeader = vo.getParentVO();

      if ((voHeader.getAttributeValue("cregister") != null) && (!voHeader.getAttributeValue("cregister").toString().equals(voHeader.getAttributeValue("coperatoridnow").toString())))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000012"));
      }

      return UFBoolean.TRUE;
    }

    return UFBoolean.FALSE;
  }

  public void checkCancelSigningUser(AggregatedValueObject vo)
    throws BusinessException
  {
  }

  public void checkRelativeBill(AggregatedValueObject vo)
    throws BusinessException
  {
    String sBillPK = null;
    if ((vo != null) && (vo.getParentVO() != null) && (vo.getParentVO().getPrimaryKey() != null))
    {
      sBillPK = vo.getParentVO().getPrimaryKey().trim();
    }

    if (sBillPK == null) {
      return;
    }
    this.m_timer.start();

    Hashtable htBid = queryRelativeBill(sBillPK, 2);

    if ((htBid == null) || (htBid.size() == 0))
      return;
    ArrayList alErr = new ArrayList();
    GeneralBillHeaderVO billHeaderVO = (GeneralBillHeaderVO)vo.getParentVO();

    GeneralBillItemVO[] billItemVO = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();

    int ibillStatues = billHeaderVO.getStatus();

    if (ibillStatues == 3) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000013"));
    }

    if (billItemVO != null) {
      int iItemStatues = 0;
      String sCgeneralbid = null;
      int iLen = billItemVO.length;
      int iInOut = 0;
      String s = null;
      String sInvCode = null;
      String sInvErr = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000104");

      String sHint1 = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000105");

      String sHint2 = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000106");

      for (int i = 0; i < iLen; i++) {
        sCgeneralbid = billItemVO[i].getPrimaryKey();
        iItemStatues = billItemVO[i].getStatus();

        iInOut = billItemVO[i].getInOutFlag();

        if ((iItemStatues != 3) && (iItemStatues != 1))
          continue;
        if (htBid.containsKey(sCgeneralbid)) {
          s = (String)htBid.get(sCgeneralbid);
          sInvCode = billItemVO[i].getInvname() + "[" + billItemVO[i].getCinventorycode() + "]";

          if (s.equals("SOURCEID")) {
            alErr.add(sInvErr + sInvCode + sHint1); } else {
            if (iItemStatues != 3)
              continue;
            alErr.add(sInvErr + sInvCode + sHint2);
          }
        }
      }

    }

    if (alErr.size() > 0) {
      StringBuffer sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000014"));

      sErr.append("\n");
      for (int i = 0; i < alErr.size(); i++) {
        sErr.append((String)alErr.get(i) + "\n");
      }
      throw new BusinessException(sErr.toString());
    }
    this.m_timer.stopAndShow("check relative bill ");
  }

  public void checkNullSpace(GeneralBillVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    this.m_timer.start();
    ArrayList alhid = new ArrayList();
    GeneralBillHeaderVO voHead = null;

    HashMap hmhidvo = new HashMap();
    for (int i = 0; i < vos.length; i++) {
      if (vos[i] != null) {
        voHead = vos[i].getHeaderVO();
        if ((voHead == null) || (voHead.getCgeneralhid() == null))
          continue;
        alhid.add(voHead.getCgeneralhid());

        hmhidvo.put(voHead.getCgeneralhid(), vos[i]);
      }

    }

    if (alhid.size() == 0) {
      return;
    }

    String[] shids = new String[alhid.size()];
    alhid.toArray(shids);
    HashMap hm = queryNullSpace(shids);

    if ((hm == null) || (hm.size() == 0)) {
      return;
    }
    ArrayList alErr = new ArrayList();

    String[] hids = new String[hm.size()];
    hm.keySet().toArray(hids);

    ArrayList albid = null;
    GeneralBillVO vo = null;
    GeneralBillItemVO[] voItems = null;
    HashMap hmbidvo = new HashMap();

    for (int i = 0; i < hids.length; i++) {
      vo = (GeneralBillVO)hmhidvo.get(hids[i]);
      voItems = vo.getItemVOs();
      for (int j = 0; j < voItems.length; j++) {
        hmbidvo.put(voItems[j].getCgeneralbid(), voItems[j]);
      }

      albid = (ArrayList)hm.get(hids[i]);
      String bid = null;
      for (int j = 0; j < albid.size(); j++) {
        bid = (String)albid.get(j);
        if (hmbidvo.containsKey(bid)) {
          GeneralBillItemVO voBody = (GeneralBillItemVO)hmbidvo.get(bid);
          alErr.add(ResBase.getBillcode() + ":" + vo.getHeaderVO().getVbillcode() + "," + ResBase.getInv() + voBody.getCinventorycode() + "[" + ResBase.getCrowno() + "]:" + voBody.getCrowno());
        }

      }

    }

    if (alErr.size() > 0) {
      StringBuffer sErr = new StringBuffer(ResBase.getIsnull() + ResBase.getSpace());

      sErr.append("\n");
      for (int i = 0; i < alErr.size(); i++) {
        sErr.append((String)alErr.get(i) + "\n");
      }
      throw new BusinessException(sErr.toString());
    }
    this.m_timer.stopAndShow("checkNullSpace");
  }

  public void checkRelativeRespondBill(AggregatedValueObject vo)
    throws BusinessException
  {
    String sBillPK = null;
    if ((vo != null) && (vo.getParentVO() != null) && (vo.getParentVO().getPrimaryKey() != null))
    {
      sBillPK = vo.getParentVO().getPrimaryKey().trim();
    }

    if (sBillPK == null)
      return;
    this.m_timer.start();

    Hashtable htBid = queryRelativeBill(sBillPK, 1);

    if ((htBid == null) || (htBid.size() == 0))
      return;
    ArrayList alErr = new ArrayList();
    GeneralBillHeaderVO billHeaderVO = (GeneralBillHeaderVO)vo.getParentVO();

    GeneralBillItemVO[] billItemVO = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();

    int ibillStatues = billHeaderVO.getStatus();

    if (ibillStatues == 3) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000015"));
    }

    if (billItemVO != null) {
      int iItemStatues = 0;
      String sCgeneralbid = null;
      int iLen = billItemVO.length;
      int iInOut = 0;
      String sErro = null;
      String sInvCode = null;
      for (int i = 0; i < iLen; i++) {
        sCgeneralbid = billItemVO[i].getPrimaryKey();
        iItemStatues = billItemVO[i].getStatus();

        iInOut = billItemVO[i].getInOutFlag();

        if ((iInOut != 1) || (iItemStatues != 3))
          continue;
        if (htBid.containsKey(sCgeneralbid)) {
          sInvCode = billItemVO[i].getInvname() + "[" + billItemVO[i].getCinventorycode() + "]";

          alErr.add(sInvCode);
        }

      }

    }

    if (alErr.size() > 0) {
      StringBuffer sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000016"));

      sErr.append("\n");
      for (int i = 0; i < alErr.size(); i++) {
        sErr.append((String)alErr.get(i) + " ;");
      }
      throw new BusinessException(sErr.toString());
    }
    this.m_timer.stopAndShow("check respond bill ");
  }

  public void checkRelativeSourceBill(AggregatedValueObject vo)
    throws BusinessException
  {
    String sBillPK = null;
    if ((vo != null) && (vo.getParentVO() != null) && (vo.getParentVO().getPrimaryKey() != null))
    {
      sBillPK = vo.getParentVO().getPrimaryKey().trim();
    }

    if (sBillPK == null)
      return;
    this.m_timer.start();
    Hashtable htBid = queryRelativeBill(sBillPK, 0);

    if ((htBid == null) || (htBid.size() == 0))
      return;
    ArrayList alErr = new ArrayList();
    GeneralBillHeaderVO billHeaderVO = (GeneralBillHeaderVO)vo.getParentVO();

    GeneralBillItemVO[] billItemVO = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();

    int ibillStatues = billHeaderVO.getStatus();

    if (ibillStatues == 3) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000013"));
    }

    if (billItemVO != null) {
      int iItemStatues = 0;
      String sCgeneralbid = null;
      int iLen = billItemVO.length;
      int iInOut = 0;
      String sErro = null;
      String sInvCode = null;
      for (int i = 0; i < iLen; i++) {
        sCgeneralbid = billItemVO[i].getPrimaryKey();
        iItemStatues = billItemVO[i].getStatus();

        iInOut = billItemVO[i].getInOutFlag();

        if ((iItemStatues != 3) && (iItemStatues != 1))
          continue;
        if (htBid.containsKey(sCgeneralbid)) {
          sInvCode = billItemVO[i].getInvname() + "[" + billItemVO[i].getCinventorycode() + "]";

          alErr.add(sInvCode);
        }

      }

    }

    if (alErr.size() > 0) {
      StringBuffer sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000017"));

      sErr.append("\n");
      for (int i = 0; i < alErr.size(); i++) {
        sErr.append((String)alErr.get(i) + " ;");
      }
      throw new BusinessException(sErr.toString());
    }
    this.m_timer.stopAndShow("check src bill ");
  }

  private HashMap queryNullSpace(String[] shids)
    throws BusinessException
  {
    if ((shids == null) || (shids.length == 0)) {
      return null;
    }
    StringBuffer sql = new StringBuffer("select b.cgeneralhid,b.cgeneralbid from ic_general_b b,ic_general_bb1 bb1 where b.cgeneralbid=bb1.cgeneralbid and b.dr=0 and bb1.dr=0 and (bb1.cspaceid='" + GenMethod.STRING_NULL + "' or bb1.cspaceid is null )");

    sql.append(GeneralSqlString.formInSQL("b.cgeneralhid", shids));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    HashMap hm = new HashMap();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      rs = stmt.executeQuery();
      String sbid = null; String shid = null;
      ArrayList albid = null;
      while (rs.next()) {
        shid = rs.getString(1);
        sbid = rs.getString(2);
        if (hm.containsKey(shid)) {
          albid = (ArrayList)hm.get(shid);
        } else {
          albid = new ArrayList();
          hm.put(shid, albid);
        }
        albid.add(sbid);
      }
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hm;
  }

  private String[] getNumInfo(GeneralBillVO voBill, int[] iOperator)
  {
    String[] sRet = null;
    GeneralBillItemVO[] voItems = null;
    StringBuffer[] sbMsg = null;
    if ((iOperator != null) && (iOperator.length > 0)) {
      sbMsg = new StringBuffer[iOperator.length];
      sRet = new String[iOperator.length];
    } else {
      return null;
    }String crowno = null;
    voItems = voBill.getItemVOs();
    if ((voItems != null) && (voItems.length > 0)) {
      for (int i = 0; i < voItems.length; i++)
      {
        if (voItems[i].getStatus() == 3)
          continue;
        int iResult = voItems[i].compareNum();
        crowno = voItems[i].getCrowno();

        for (int j = 0; j < iOperator.length; j++) {
          if (iResult == iOperator[j]) {
            if (sbMsg[j] == null) {
              if (iResult == 3) {
                sbMsg[j] = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000018"));

                sbMsg[j].append("\n");
              } else if (iResult == 1) {
                sbMsg[j] = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000019"));

                sbMsg[j].append("\n");
              }
            }
            sbMsg[j].append(crowno);
            sbMsg[j].append(" ");
            sbMsg[j].append(voItems[i].getCinventorycode());
            sbMsg[j].append(voItems[i].getInvname());
            sbMsg[j].append("\n");
          }
        }
      }
    }
    for (int i = 0; i < iOperator.length; i++) {
      if ((sbMsg[i] != null) && (sbMsg[i].length() > 0))
        sRet[i] = sbMsg[i].toString();
    }
    return sRet;
  }

  public UFBoolean isInvValidate(AggregatedValueObject vo)
    throws BusinessException
  {
    String sHint = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000020");

    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      throw new BusinessException(sHint);
    }

    GeneralBillVO voBill = (GeneralBillVO)vo;
    int iLength = voBill.getItemCount();

    String cinventoryid = null; String pk_corp = null; String pk_calbody = null;

    pk_corp = (String)voBill.getHeaderValue("pk_corp");
    pk_calbody = (String)voBill.getHeaderValue("pk_calbody");
    if ((pk_corp == null) || (pk_calbody == null)) {
      SCMEnv.out(sHint);
      throw new BusinessException(sHint);
    }

    this.m_timer.start();

    String sInFieldSQL = " isnull(bd_produce.pk_invmandoc,'') ";
    Hashtable htPK = null;
    ArrayList alINSQLValue = new ArrayList();
    StringBuffer sbKey = null;
    for (int i = 0; i < iLength; i++) {
      sbKey = new StringBuffer();
      cinventoryid = (String)voBill.getItemValue(i, "cinventoryid");

      if (cinventoryid == null) {
        SCMEnv.out(sHint);
        throw new BusinessException(sHint);
      }

      sbKey.append(cinventoryid);

      alINSQLValue.add(sbKey.toString());
    }

    String sINSQL = GeneralSqlString.formInSQL(sInFieldSQL, alINSQLValue);

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String sInvCode = null;
    StringBuffer sbSql = null;
    String subSQL = "select bd_invbasdoc.invcode from bd_produce inner join bd_invbasdoc on  (bd_produce.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc )  where 1=1 and bd_produce.pk_corp = '" + pk_corp + "' And bd_produce.pk_calbody = '" + pk_calbody + "' And (bd_produce.isused = 'N' OR bd_produce.isused = 'n' OR bd_produce.isused is null ) ";

    StringBuffer sbErrorMsg = null;
    try {
      if (sINSQL != null) {
        con = getConnection();

        sbErrorMsg = new StringBuffer();

        sbSql = new StringBuffer();

        sbSql.append(subSQL).append(sINSQL);
        stmt = con.prepareStatement(sbSql.toString());
        rs = stmt.executeQuery();
        while (rs.next())
        {
          sInvCode = rs.getString("invcode");

          sbErrorMsg.append(sInvCode + ";  ");
        }
      }
    }
    catch (Exception ex)
    {
      SCMEnv.error(ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
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

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000021"));

    if ((sbErrorMsg == null) || (sbErrorMsg.length() == 0)) {
      return new UFBoolean(true);
    }
    sbErrorMsg.insert(0, NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000022"));

    throw new BusinessException(sbErrorMsg.toString());
  }

  public UFBoolean isInvValidateAfterSaveBill(AggregatedValueObject vo)
    throws BusinessException
  {
    String sHint = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000020");

    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      throw new BusinessException(sHint);
    }

    GeneralBillVO voBill = (GeneralBillVO)vo;

    String cinventoryid = null; String pk_corp = null; String pk_calbody = null; String pkbillhid = null;

    pk_corp = (String)voBill.getHeaderValue("pk_corp");
    pk_calbody = (String)voBill.getHeaderValue("pk_calbody");
    pkbillhid = (String)voBill.getHeaderValue("cgeneralhid");
    if ((pkbillhid == null) || (pk_corp == null) || (pk_calbody == null)) {
      SCMEnv.out(sHint);
      throw new BusinessException(sHint);
    }

    this.m_timer.start();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String sInvCode = null;

    String subSQL = "select bd_invbasdoc.invcode from bd_produce inner join bd_invbasdoc on  (bd_produce.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc )  where 1=1 and bd_produce.pk_corp = '" + pk_corp + "' And bd_produce.pk_calbody = '" + pk_calbody + "' And (bd_produce.isused = 'N' OR bd_produce.isused = 'n' OR bd_produce.isused is null ) And bd_produce.pk_invmandoc in ( Select cinventoryid from ic_general_b where dr = 0 and cgeneralhid = '" + pkbillhid + "') ";

    StringBuffer sbErrorMsg = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(subSQL);
      rs = stmt.executeQuery();
      while (rs.next())
      {
        sInvCode = rs.getString("invcode");

        sbErrorMsg.append(sInvCode + ";  ");
      }

    }
    catch (Exception ex)
    {
      SCMEnv.error(ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000023"));

    if ((sbErrorMsg == null) || (sbErrorMsg.length() == 0)) {
      return new UFBoolean(true);
    }
    sbErrorMsg.insert(0, NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000022"));

    throw new BusinessException(sbErrorMsg.toString());
  }

  public UFBoolean isNumMorethenShouldnum(AggregatedValueObject vo)
    throws BusinessException
  {
    return VOCheck.isNumMorethenShouldnum(vo);
  }

  private Hashtable queryRelativeBill(String sBillPK, int flag)
    throws BusinessException
  {
    if (sBillPK == null) {
      return null;
    }
    String sSql = null;

    if (flag == 0)
      sSql = " select  csourcebillbid  from ic_general_b    where  csourcebillhid=? and (idesatype <>4 and (cparentid is null or cparentid<>ccorrespondhid)) and (ninnum is not null or noutnum is not null) and dr=0";
    else if (flag == 1)
      sSql = " select  ccorrespondbid  from ic_general_b    where  ccorrespondhid=? and (idesatype <>4 and (cparentid is null or cparentid<>ccorrespondhid)) and (ninnum is not null or noutnum is not null) and dr=0";
    else if (flag == 2) {
      sSql = " select  csourcebillbid,ccorrespondbid  from ic_general_b    where  csourcebillhid=?  and (idesatype <>4 and (cparentid is null or cparentid<>ccorrespondhid)) and (ninnum is not null or noutnum is not null) and dr=0  union all  select  csourcebillbid,ccorrespondbid  from ic_general_b    where  ccorrespondhid=?  and (idesatype <>4 and (cparentid is null or cparentid<>ccorrespondhid)) and (ninnum is not null or noutnum is not null) and dr=0 ";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable htbID = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sBillPK);
      if (flag == 2) {
        stmt.setString(2, sBillPK);
      }
      rs = stmt.executeQuery();
      String sCsourcebillbid = null; String sCcorrespondbid = null;
      while (rs.next())
      {
        if (flag == 0) {
          sCsourcebillbid = rs.getString(1);
          if ((sCsourcebillbid == null) || (sCsourcebillbid.trim().length() <= 0) || (htbID.containsKey(sCsourcebillbid))) {
            continue;
          }
          htbID.put(sCsourcebillbid, "SOURCEID"); continue;
        }if (flag == 1) {
          sCcorrespondbid = rs.getString(1);
          if ((sCcorrespondbid == null) || (sCcorrespondbid.trim().length() <= 0) || (htbID.containsKey(sCcorrespondbid))) {
            continue;
          }
          htbID.put(sCcorrespondbid, "CORRESPONDID"); continue;
        }
        if (flag == 2) {
          sCsourcebillbid = rs.getString(1);
          sCcorrespondbid = rs.getString(2);
          if ((sCsourcebillbid != null) && (sCsourcebillbid.trim().length() > 0) && (!htbID.containsKey(sCsourcebillbid)))
          {
            htbID.put(sCsourcebillbid, "SOURCEID");
          }if ((sCcorrespondbid == null) || (sCcorrespondbid.trim().length() <= 0) || (htbID.containsKey(sCcorrespondbid))) {
            continue;
          }
          htbID.put(sCcorrespondbid, "CORRESPONDID");
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
      }
    }
    return htbID;
  }

  public void checkCorNum(String sBillPK)
    throws BusinessException
  {
    if (sBillPK == null)
      return;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();

      StringBuffer sMsg = new StringBuffer();
      String sql = " select vbillcode ,crowno,(abs(isnull(ncorrespondnum,0))-abs(COALESCE(ninnum,noutnum))) as num from ic_general_h h,ic_general_b b where  h.cgeneralhid=b.cgeneralhid   and h.cgeneralhid='" + sBillPK + "' and h.dr=0 and abs(COALESCE(ninnum,noutnum))<abs(isnull(ncorrespondnum,0)) and idesatype <>4 and (cparentid is null or cparentid<>ccorrespondhid) ";
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        sMsg.append("\n" + NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000491") + rs.getString(1));
        sMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000492") + rs.getString(2));
        sMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000493") + rs.getBigDecimal(3).abs().toString());
      }

      if (sMsg.length() > 0) {
        throw new BusinessException(sMsg.toString());
      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }
}