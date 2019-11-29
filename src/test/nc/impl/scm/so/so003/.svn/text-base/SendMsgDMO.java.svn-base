package nc.impl.scm.so.so003;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;

public class SendMsgDMO extends DataManageObject
{
  public SendMsgDMO()
    throws NamingException, SystemException
  {
  }

  public SendMsgDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public String getCustName(String pk)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so003.SendMsgDMO", "getCustName", new Object[] { pk });

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT custname ");
    sql.append("FROM bd_cumandoc LEFT OUTER JOIN bd_cubasdoc ON bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
    sql.append(" WHERE pk_cumandoc = ?");

    String custname = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        custname = rs.getString(1);
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.so003.SendMsgDMO", "getCustName", new Object[] { pk });

    return custname;
  }

  public String[] getInvCodeName(String pk)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so003.SendMsgDMO", "getInvCodeName", new Object[] { pk });

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT invcode, invname ");
    sql.append("FROM bd_invmandoc LEFT OUTER JOIN bd_invbasdoc ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc");
    sql.append(" WHERE pk_invmandoc = ?");

    String[] cn = new String[2];
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        cn[0] = rs.getString(1);
        cn[1] = rs.getString(2);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.so003.SendMsgDMO", "getInvCodeName", new Object[] { pk });

    return cn;
  }

  public String[] getOperator(String pk)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so003.SendMsgDMO", "getOperator", new Object[] { pk });

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT user_code,user_name ");
    sql.append("FROM sm_user");
    sql.append(" WHERE cuserid = ?");

    String[] operator = new String[2];
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        operator[0] = rs.getString(1);
        operator[1] = rs.getString(2);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.so003.SendMsgDMO", "getOperator", new Object[] { pk });

    return operator;
  }

  public String getSOBillCode(String pk)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so003.SendMsgDMO", "getSOBillCode", new Object[] { pk });

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT vreceiptcode ");
    sql.append("FROM so_sale");
    sql.append(" WHERE csaleid = ?");

    String billcode = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        billcode = rs.getString(1);
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.so003.SendMsgDMO", "getSOBillCode", new Object[] { pk });

    return billcode;
  }
}