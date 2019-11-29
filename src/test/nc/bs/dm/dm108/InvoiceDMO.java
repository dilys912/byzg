package nc.bs.dm.dm108;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import nc.bs.dm.pub.DmDMO;
import nc.vo.dm.dm108.ValueRangeHashtableInvoiceBody;
import nc.vo.dm.dm108.ValueRangeHashtableInvoiceHead;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DelivBillStatus;
import nc.vo.pub.BusinessException;

public class InvoiceDMO extends DmDMO
{
  public InvoiceDMO()
    throws NamingException, nc.bs.pub.SystemException
  {
  }

  public void auditInvoice(String headpk, String auditor, String auditdate)
    throws SQLException
  {
    beforeCallMethod("nc.bs.dm.dm108.InvoiceDMO", "auditInvoice", new Object[] { headpk, auditor, auditdate });

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      StringBuffer sb = new StringBuffer();

      sb.append("update dm_delivinvoice_h set pkapprperson = '" + auditor + "'," + "apprdate = '" + auditdate + "'" + "where pk_delivinvoice_h = '" + headpk + "'");

      con = getConnection();
      stmt = con.prepareStatement(sb.toString());
      stmt.executeUpdate();
    } catch (Exception e) {
      reportException(e);
      throw new SQLException(e.getMessage());
    } finally {
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

    afterCallMethod("nc.bs.dm.dm108.InvoiceDMO", "auditInvoice", new Object[] { headpk, auditor, auditdate });
  }

  public DMDataVO[] queryCust(String pktrancust, String pkcorp)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT dm_trancust.pkcusmandoc AS pkcusbasdoc ,bd_cumandoc.pk_cumandoc AS pkcusmandoc ").append("FROM dm_trancust INNER JOIN bd_cumandoc ON dm_trancust.pkcusmandoc = bd_cumandoc.pk_cubasdoc ").append("WHERE dm_trancust.pk_trancust = '" + pktrancust + "' and bd_cumandoc.pk_corp='" + pkcorp + "' and bd_cumandoc.custflag in ('1','3')");

    DMDataVO[] bodyvos = super.query(sb);
    return bodyvos;
  }

  public DMDataVO[] queryDelivBillByVerify(String sWhereClause)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT pkdelivorg,dm_delivbill_h.pk_delivbill_h, dm_delivbill_h.vdelivbillcode, dm_delivbill_h.pktrancust, ").append("SUM(dm_delivfeebill_b.dfeeitemplan) AS planfee, ").append("SUM(dm_delivfeebill_b.dfeeitemfact) AS factfee, dm_delivbill_b.pk_delivbill_b ").append(" ,dm_delivbill_h.billdate,dm_delivbill_b.irownumber ").append("FROM  ").append("dm_delivbill_b LEFT OUTER JOIN dm_delivfeebill_b ON dm_delivbill_b.pk_delivbill_b=dm_delivfeebill_b.pk_delivbill_b ").append(" , dm_delivbill_h LEFT OUTER JOIN dm_delivfeebill_h ON dm_delivbill_h.pk_delivbill_h=dm_delivfeebill_h.pk_delivbill_h ").append(" where dm_delivbill_h.dr=0 and dm_delivbill_b.dr=0 AND dm_delivbill_b.pk_delivbill_h = dm_delivbill_h.pk_delivbill_h ").append("   and dm_delivfeebill_h.pk_delivfeebill_h=dm_delivfeebill_b.pk_delivfeebill_h and dm_delivfeebill_h.dr=0 and dm_delivfeebill_b.dr=0 ").append(" and ( dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.Audit) + " or dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.End) + " or dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.Out) + ")");

    if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
      sb.append(" and " + sWhereClause);
    }

    sb.append(" GROUP BY pkdelivorg,dm_delivbill_h.pk_delivbill_h, dm_delivbill_h.vdelivbillcode, dm_delivbill_h.pktrancust, dm_delivbill_b.pk_delivbill_b");
    sb.append(" ,dm_delivbill_h.billdate,dm_delivbill_b.irownumber ");

    DMDataVO[] vos = super.query(sb);
    return vos;
  }

  public DMDataVO[] queryDeliveryBill(String sWhereClause)
    throws SQLException, javax.transaction.SystemException, BusinessException
  {
    StringBuffer sb = new StringBuffer();

    sb.append("SELECT pk_delivbill_b,pk_delivbill_h, vdelivbillcode, pkdelivorg, pktrancust, pktranorg, ").append("senddate, SUM(CASE WHEN dallfeefact IS NULL THEN COALESCE (dfeeitemplan, 0.0) ELSE COALESCE (dfeeitemfact, 0.0) END) AS totalfee ").append(" ,isendtype,pkcustinvoice ").append("FROM (SELECT DISTINCT dm_delivbill_h.pk_delivbill_h, dm_delivbill_b.pk_delivbill_b, ").append("dm_delivfeebill_b.pk_delivfeebill_b, dm_delivbill_h.vdelivbillcode, ").append("dm_delivfeebill_h.dallfeefact, ").append("dm_delivbill_h.pkdelivorg, dm_delivfeebill_h.isendtype,dm_delivbill_h.pktrancust, ").append("dm_delivbill_h.pktranorg, dm_delivbill_h.senddate, ").append("dm_delivbill_b.btestbyinvoice, dm_delivbill_h.pkapprperson, ").append("dm_delivfeebill_b.dfeeitemplan, dm_delivfeebill_b.dfeeitemfact,dm_delivfeebill_b.pkcustinvoice ").append("FROM  dm_delivfeebill_h INNER JOIN dm_delivbill_h ON dm_delivfeebill_h.pk_delivbill_h = dm_delivbill_h.pk_delivbill_h ").append("INNER JOIN dm_delivbill_b ON dm_delivbill_h.pk_delivbill_h = dm_delivbill_b.pk_delivbill_h ").append("INNER JOIN dm_delivfeebill_b ON dm_delivbill_b.pk_delivbill_b = dm_delivfeebill_b.pk_delivbill_b AND dm_delivfeebill_b.pk_delivfeebill_h = dm_delivfeebill_h.pk_delivfeebill_h ").append(" WHERE dm_delivbill_h.dr=0 and dm_delivbill_b.dr=0 and dm_delivfeebill_b.dr=0 and dm_delivfeebill_h.dr=0").append(" AND (dm_delivbill_b.btestbyinvoice = 'N' OR ").append("dm_delivbill_b.btestbyinvoice IS NULL) ").append(" AND dm_delivfeebill_h.isendtype in ( ");

    sb.append("1,2");
    sb.append(") ");
    sb.append("   AND ( dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.Audit) + " OR dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.End) + " OR dm_delivbill_b.irowstatus=" + new Integer(DelivBillStatus.Out) + ")) aa ");

    if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
      sb.append(" where " + sWhereClause);
    }

    sb.append("GROUP BY pk_delivbill_b,pk_delivbill_h, vdelivbillcode, senddate, pkdelivorg, pktrancust, pktranorg ");
    sb.append(" ,isendtype,pkcustinvoice ");

    DMDataVO[] ddvos = super.query(sb);

    return ddvos;
  }

  public DMDataVO[] queryInvoiceBody(String sWhereClause)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    StringBuffer sb = new StringBuffer();
    sb.append("select * from dm_delivinvoice_b where dr=0 ");
    if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
      sb.append(" and " + sWhereClause);
    }
    DMDataVO[] bodyvos = super.query(sb);
    return bodyvos;
  }

  public DMDataVO[] queryInvoiceByVerify(String sWhereClause)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT dm_delivinvoice_h.pk_delivinvoice_h, dm_delivinvoice_h.vinvoicenumber, ").append("dm_delivinvoice_b.pk_delivinvoice_b, dm_delivinvoice_b.dtaxmoney, dm_delivinvoice_b.dtranmoney,dm_delivinvoice_b.crowno, ").append("dtaxmoney - COALESCE (dtranmoney, 0.0) AS dremains,dm_delivinvoice_h.billdate ").append("FROM dm_delivinvoice_h LEFT OUTER JOIN ").append("dm_delivinvoice_b ON ").append("dm_delivinvoice_h.pk_delivinvoice_h = dm_delivinvoice_b.pk_delivinvoice_h and dm_delivinvoice_h.dr=0 ");

    if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
      sb.append(" where " + sWhereClause);
    }

    DMDataVO[] vos = super.query(sb);
    return vos;
  }

  public DMDataVO[] queryInvoiceHead(String sWhereClause)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    StringBuffer sb = new StringBuffer();
    sb.append("select * from dm_delivinvoice_h where dr=0 ");
    if ((sWhereClause != null) && (sWhereClause.trim().length() != 0)) {
      sb.append(" and " + sWhereClause);
    }
    DMDataVO[] headvos = super.query(sb);
    return headvos;
  }

  public DMVO save(DMVO vo)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    DMVO dvo = new DMVO();
    dvo = super.save("dm_delivinvoice_h", "dm_delivinvoice_b", vo, new ValueRangeHashtableInvoiceHead(), new ValueRangeHashtableInvoiceBody(), true);

    return dvo;
  }

  public String[] getDilvBillPks(StringBuffer where)
  {
    String[] pks = null;
    try {
      ArrayList al = new ArrayList();
      StringBuffer fix = new StringBuffer();
      fix.append("select distinct pk_delivbill_h from dm_delivbill_b where dr=0 and ");
      fix.append(where);
      al = super.queryExecute(fix);
      pks = new String[al.size()];
      for (int i = 0; i < al.size(); i++)
        pks[i] = ((DMDataVO)al.get(i)).getAttributeValue("pk_delivbill_h").toString();
    }
    catch (BusinessException e)
    {
      reportException(e);
    }

    return pks;
  }

  public String[] getDilvBillFeePks(StringBuffer where)
  {
    String[] pks = null;
    try {
      ArrayList al = new ArrayList();
      ArrayList al1 = new ArrayList();
      StringBuffer fix = new StringBuffer();
      fix.append("select pk_delivfeebill_b from dm_delivfeebill_b where dr=0 and ");
      fix.append(where);
      al = super.queryExecute(fix);
      StringBuffer fix1 = new StringBuffer();
      fix1.append("select distinct pk_delivfeebill_h from dm_delivfeebill_b where dr=0 and ");
      fix1.append(where);
      al1 = super.queryExecute(fix1);
      pks = new String[al.size() + al1.size()];
      for (int i = 0; i < al.size(); i++)
        pks[i] = ((DMDataVO)al.get(i)).getAttributeValue("pk_delivfeebill_b").toString();
      for (int j = 0; j < al1.size(); j++)
        pks[(j + al.size())] = ((DMDataVO)al1.get(j)).getAttributeValue("pk_delivfeebill_h").toString();
    }
    catch (BusinessException e) {
      reportException(e);
    }

    return pks;
  }

  public void setInvoiceVerifyNum(DMVO vo)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    super.save("dm_delivinvoice_h", "dm_delivinvoice_b", vo, new ValueRangeHashtableInvoiceHead(), new ValueRangeHashtableInvoiceBody(), false);
  }


}