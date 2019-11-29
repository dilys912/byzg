package nc.impl.ct.out;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.bill.ScmDMO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.pub.SCMEnv;

public class SoQryCtDMO extends ScmDMO
{
  public SoQryCtDMO()
    throws NamingException, SystemException
  {
  }

  public ArrayList querySoCtInfoAllBatch(String sPk_corp, String[] sInvIDs, String sCustID, UFDate date)
    throws SQLException, Exception
  {
    ArrayList alResult = new ArrayList();

    CTOutPubDAO pubDMO = new CTOutPubDAO();
    Vector vInvclIds = pubDMO.getInvclBatchByInvManID(sPk_corp, sInvIDs);

    StringBuffer sbSql = new StringBuffer();
    sbSql.append("SELECT a.pk_ct_manage , b.pk_ct_manage_b, a.ct_code,a.currid,b.oriprice,b.oritaxprice,10000 as invclasslev,b.invclid ,a.ct_name,a.actualvalidate,a.invallidate, ")
    .append(" a.projectid, b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, invman.pk_invbasdoc, invman.pk_invmandoc, ")
    .append(" a.pk_corp ")
    .append(" FROM  ct_manage a ")
    .append(" INNER JOIN ct_manage_b b ON a.pk_ct_manage=b.pk_ct_manage ")
    .append(" JOIN ct_type type ON a.pk_ct_type=type.pk_ct_type ")
    .append(" LEFT JOIN bd_invmandoc invman ON invman.pk_invmandoc = b.invid ")
    .append(" WHERE a.dr=0 and a.activeflag=0 and a.ctflag=2 ")
    .append(" and b.dr=0 and type.dr=0 and type.nbusitype =  ")
    .append(1).append(" and isnull(b.amount, 0) > isnull(b.ordnum, 0) ")
    .append(" and a.pk_corp = '")
    .append(sPk_corp)
    .append("' and a.custid=? and b.invid=? and a.actualvalidate <= ?  ")
    .append(" UNION ( ")
    .append(" SELECT c.pk_ct_manage,d.pk_ct_manage_b, c.ct_code,c.currid ,d.oriprice ,d.oritaxprice ,invclasslev ,d.invclid ,c.ct_name,c.actualvalidate,c.invallidate, ")
    .append(" c.projectid, d.vfree1, d.vfree2, d.vfree3, d.vfree4, d.vfree5, bd_invmandoc.pk_invbasdoc, bd_invmandoc.pk_invmandoc, ")
    .append(" c.pk_corp ")
    .append(" FROM  ct_manage c ")
    .append(" INNER JOIN ct_manage_b d ON c.pk_ct_manage=d.pk_ct_manage ")
    .append(" INNER JOIN bd_invcl e ON d.invclid=e.pk_invcl ")
    .append(" INNER JOIN ct_type type ON c.pk_ct_type=type.pk_ct_type ")
    .append(" LEFT JOIN bd_invmandoc ON bd_invmandoc.pk_invmandoc = d.invid ")
    .append(" WHERE c.dr=0 and c.activeflag=0 and c.ctflag=2 ")
    .append(" and d.dr=0 and e.dr=0 and type.dr=0 and type.nbusitype = ").append(1)
    .append(" and isnull(d.amount, 0) > isnull(d.ordnum, 0) ")
    .append(" and c.pk_corp = '")
    .append(sPk_corp)
    .append("' and c.custid=? and c.actualvalidate <= ? and ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();

      for (int i = 0; i < sInvIDs.length; i++) {
        StringBuffer sbSqlExec = new StringBuffer(sbSql.toString());

        Vector vCurInvcId = (Vector)vInvclIds.elementAt(i);
        for (int k = 0; k < vCurInvcId.size(); k++) {
          if (k == 0) {
            sbSqlExec.append("( ");
          }
          if (k != 0) {
            sbSqlExec.append(" or ");
          }
          sbSqlExec.append(" d.invclid='").append(vCurInvcId.elementAt(k).toString()).append("'");

          if (k == vCurInvcId.size() - 1) {
            sbSqlExec.append("  ) ) order by invclasslev desc ,actualvalidate desc ");
          }
        }
        stmt = con.prepareStatement(sbSqlExec.toString());

        if (sCustID == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, sCustID);
        }
        if (sInvIDs[i] == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, sInvIDs[i]);
        }
        if (date == null)
          stmt.setNull(3, 91);
        else {
          stmt.setString(3, date.toString());
        }

        if (sCustID == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, sCustID);
        }
        if (date == null)
          stmt.setNull(5, 91);
        else {
          stmt.setString(5, date.toString());
        }

        rs = stmt.executeQuery();

        Vector vCt = new Vector();
        while (rs.next()) {
          RetCtToPoQueryVO tempVo = new RetCtToPoQueryVO();

          String sContractrId = rs.getString(1);
          tempVo.setCContractID(sContractrId == null ? null : sContractrId.trim());

          String sContractRowId = rs.getString(2);
          tempVo.setCContractRowID(sContractRowId == null ? null : sContractRowId.trim());

          String sContractCode = rs.getString(3);
          tempVo.setCContractCode(sContractCode == null ? null : sContractCode.trim());

          String sCurrId = rs.getString(4);
          tempVo.setCCurrencyId(sCurrId == null ? null : sCurrId.trim());

          double oOriPrice = rs.getDouble(5);
          tempVo.setDOrgPrice(oOriPrice == 0.0D ? null : new UFDouble(oOriPrice));

          double oOriTaxPrice = rs.getDouble(6);
          tempVo.setDOrgTaxPrice(oOriTaxPrice == 0.0D ? null : new UFDouble(oOriTaxPrice));

          int iinvclasslevel = rs.getInt(7);

          if (iinvclasslevel == 10000)
            tempVo.setIinvCtl(RetCtToPoQueryVO.INVCTL);
          else
            tempVo.setIinvCtl(RetCtToPoQueryVO.INVCLASSCTL);
          String sInvclid = rs.getString(8);
          tempVo.setCInvClass(sInvclid == null ? null : sInvclid.trim());

          String sCtname = rs.getString(9);
          tempVo.setCtname(sCtname);

          String sactrualvalidate = rs.getString(10);
          tempVo.setActrualValidate(sactrualvalidate == null ? null : new UFDate(sactrualvalidate));

          String sinvalidate = rs.getString(11);
          tempVo.setInValidate(sinvalidate == null ? null : new UFDate(sinvalidate));

          String sJobID = rs.getString("projectid");
          tempVo.setJobID(sJobID);

          String sFreeItem = rs.getString("vfree1");
          tempVo.setAttributeValue("vfree1", sFreeItem);
          sFreeItem = rs.getString("vfree2");
          tempVo.setAttributeValue("vfree2", sFreeItem);
          sFreeItem = rs.getString("vfree3");
          tempVo.setAttributeValue("vfree3", sFreeItem);
          sFreeItem = rs.getString("vfree4");
          tempVo.setAttributeValue("vfree4", sFreeItem);
          sFreeItem = rs.getString("vfree5");
          tempVo.setAttributeValue("vfree5", sFreeItem);

          String sInvbasID = rs.getString("pk_invbasdoc");
          tempVo.setAttributeValue("pk_invbasdoc", sInvbasID);

          String sInvmanID = rs.getString("pk_invmandoc");
          tempVo.setAttributeValue("pk_invmandoc", sInvmanID);

          String sRecordCorp = rs.getString("pk_corp");
          tempVo.setPk_Corp(sRecordCorp);

          vCt.addElement(tempVo);
        }

        RetCtToPoQueryVO[] retCttoPurVO = null;
        if (vCt.size() > 0) {
          retCttoPurVO = new RetCtToPoQueryVO[vCt.size()];
          vCt.copyInto(retCttoPurVO);
        }

        alResult.add(i, retCttoPurVO);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    return alResult;
  }

  public RetCtToPoQueryVO[] queryInvCtrlNullCnt(String sPk_corp, String sCustID, UFDate date)
    throws SQLException
  {
    StringBuffer sbSql = new StringBuffer();

    sbSql.append("select a.pk_ct_manage , b.pk_ct_manage_b, a.ct_code,a.currid,b.oriprice,b.oritaxprice, ")
    .append(" a.ct_name,a.actualvalidate,a.invallidate, a.pk_corp ")
    .append(" from ct_manage a ")
    .append(" INNER JOIN ct_manage_b b ON a.pk_ct_manage=b.pk_ct_manage ")
    .append(" JOIN ct_type type ON a.pk_ct_type=type.pk_ct_type ")
    .append(" where a.dr=0 and a.activeflag=0 and b.dr=0 ")
    .append(" and type.dr=0 and type.nbusitype =  ")
    .append(1)
    .append(" and isnull(b.amount, 0) > isnull(b.ordnum, 0) ")
    .append(" and a.pk_corp = '")
    .append(sPk_corp)
    .append("' and a.custid=? and type.ninvctlstyle=2 and a.actualvalidate <= ? and a.ctflag = ?");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector vCt = new Vector();
    RetCtToPoQueryVO[] allRetVO = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());

      if (sCustID == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, sCustID);
      }
      if (date == null)
        stmt.setNull(2, 91);
      else
        stmt.setString(2, date.toString());
      stmt.setInt(3, 2);

      rs = stmt.executeQuery();

      while (rs.next()) {
        RetCtToPoQueryVO tempVo = new RetCtToPoQueryVO();

        String sContractrId = rs.getString(1);
        tempVo.setCContractID(sContractrId == null ? null : sContractrId.trim());

        String sContractRowId = rs.getString(2);
        tempVo.setCContractRowID(sContractRowId == null ? null : sContractRowId.trim());

        String sContractCode = rs.getString(3);
        tempVo.setCContractCode(sContractCode == null ? null : sContractCode.trim());

        String sCurrId = rs.getString(4);
        tempVo.setCCurrencyId(sCurrId == null ? null : sCurrId.trim());

        double oOriPrice = rs.getDouble(5);
        tempVo.setDOrgPrice(oOriPrice == 0.0D ? null : new UFDouble(oOriPrice));

        double oOriTaxPrice = rs.getDouble(6);
        tempVo.setDOrgTaxPrice(oOriTaxPrice == 0.0D ? null : new UFDouble(oOriTaxPrice));

        String sCtname = rs.getString(7);
        tempVo.setCtname(sCtname);

        String sactrualvalidate = rs.getString(8);
        tempVo.setActrualValidate(sactrualvalidate == null ? null : new UFDate(sactrualvalidate));

        String sinvalidate = rs.getString(9);
        tempVo.setInValidate(sinvalidate == null ? null : new UFDate(sinvalidate));

        String sRecordCorp = rs.getString("pk_corp");
        tempVo.setPk_Corp(sRecordCorp);

        tempVo.setIinvCtl(RetCtToPoQueryVO.INVNULLCTL);
        tempVo.setCInvClass(null);

        vCt.addElement(tempVo);
      }

      if (vCt.size() > 0)
      {
        allRetVO = new RetCtToPoQueryVO[vCt.size()];
        vCt.copyInto(allRetVO);
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    return allRetVO;
  }

  public Hashtable queryCtCodeAndNameByIDSql(String sCtIDSubSQL)
    throws SQLException
  {
    String sQrySql = "SELECT pk_ct_manage, ct_code, ct_name FROM ct_manage WHERE dr = 0 AND pk_ct_manage IN (" + sCtIDSubSQL + ")";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    Hashtable htQryResult = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sQrySql);
      rs = stmt.executeQuery();

      String sPk_ct_manage = null;
      String sCt_code = null;
      String sCt_name = null;

      while (rs.next()) {
        sPk_ct_manage = rs.getString("pk_ct_manage");
        sCt_code = rs.getString("ct_code");
        sCt_name = rs.getString("ct_name");

        htQryResult.put(sPk_ct_manage, new String[] { sCt_code, sCt_name });
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    if (htQryResult.size() == 0) {
      return null;
    }
    return htQryResult;
  }
}