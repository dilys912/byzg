package nc.bs.ia.ia402;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.ia.ia402.AccountVO;

public class AccountDMO extends DataManageObject
{
  public AccountDMO()
    throws NamingException, SystemException
  {
  }

  public AccountDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public AccountVO findByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "findByPrimaryKey", new Object[] { key });

    String sql = "select pk_corp, caccountyear, caccountmonth from ia_account where caccoutid = ?";

    AccountVO account = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      rs = stmt.executeQuery();

      if (rs.next()) {
        account = new AccountVO(key);

        String pk_corp = rs.getString(1);
        account.setPk_corp((pk_corp == null) ? null : pk_corp.trim());

        String caccountyear = rs.getString(2);
        account.setCaccountyear((caccountyear == null) ? null : caccountyear.trim());

        String caccountmonth = rs.getString(3);
        account.setCaccountmonth((caccountmonth == null) ? null : caccountmonth.trim());
      }
    }
    finally {
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
      try {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "findByPrimaryKey", new Object[] { key });

    return account;
  }

  public String insert(AccountVO account)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "insert", new Object[] { account });

    String sql = "insert into ia_account(caccoutid, pk_corp, caccountyear, caccountmonth) values(?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID();
      stmt.setString(1, key);

      if (account.getPk_corp() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, account.getPk_corp());
      }
      if (account.getCaccountyear() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, account.getCaccountyear());
      }
      if (account.getCaccountmonth() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, account.getCaccountmonth());
      }

      stmt.executeUpdate();
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
    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "insert", new Object[] { account });

    return key;
  }

  public String[] insertArray(AccountVO[] accounts)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "insertArray", new Object[] { accounts });

    String sql = "insert into ia_account(caccoutid, pk_corp, caccountyear, caccountmonth) values(?, ?, ?, ?)";

    String[] keys = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      keys = getOIDs(accounts.length);
      for (int i = 0; i < accounts.length; ++i)
      {
        stmt.setString(1, keys[i]);

        if (accounts[i].getPk_corp() == null) {
          stmt.setNull(2, 1);
        }
        else {
          stmt.setString(2, accounts[i].getPk_corp());
        }
        if (accounts[i].getCaccountyear() == null) {
          stmt.setNull(3, 1);
        }
        else {
          stmt.setString(3, accounts[i].getCaccountyear());
        }
        if (accounts[i].getCaccountmonth() == null) {
          stmt.setNull(4, 1);
        }
        else {
          stmt.setString(4, accounts[i].getCaccountmonth());
        }

        stmt.executeUpdate();
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
    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "insertArray", new Object[] { accounts });

    return keys;
  }

  public void delete(AccountVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "delete", new Object[] { vo });

    String sql = "delete from ia_account";

    Vector vConditions = new Vector(1, 1);

    if ((vo.getCaccoutid() != null) && (vo.getCaccoutid().trim().length() != 0))
    {
      vConditions.addElement("caccoutid= '" + vo.getCaccoutid() + "'");
    }
    if ((vo.getPk_corp() != null) && (vo.getPk_corp().trim().length() != 0))
    {
      vConditions.addElement("pk_corp= '" + vo.getPk_corp() + "'");
    }
    if ((vo.getCaccountyear() != null) && (vo.getCaccountyear().trim().length() != 0))
    {
      vConditions.addElement("caccountyear= '" + vo.getCaccountyear() + "'");
    }
    if ((vo.getCaccountmonth() != null) && (vo.getCaccountmonth().trim().length() != 0))
    {
      vConditions.addElement("caccountmonth= '" + vo.getCaccountmonth() + "'");
    }

    if (vConditions.size() != 0)
    {
      sql = sql + " where ";
    }

    int iLength = vConditions.size();
    for (int i = 0; i < iLength; ++i)
    {
      sql = sql + ((String)vConditions.elementAt(i)).toString();
      if (i == vConditions.size() - 1)
        continue;
      sql = sql + " and ";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
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
    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "delete", new Object[] { vo });
  }

  public void update(AccountVO account)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "update", new Object[] { account });

    String sql = "update ia_account set pk_corp = ?, caccountyear = ?, caccountmonth = ? where caccoutid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (account.getPk_corp() == null) {
        stmt.setNull(1, 1);
      }
      else {
        stmt.setString(1, account.getPk_corp());
      }
      if (account.getCaccountyear() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, account.getCaccountyear());
      }
      if (account.getCaccountmonth() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, account.getCaccountmonth());
      }

      stmt.setString(4, account.getPrimaryKey());

      stmt.executeUpdate();
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
    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "update", new Object[] { account });
  }

  public AccountVO[] queryAll(String unitCode)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "queryAll", new Object[] { unitCode });

    String sql = "";
    if (unitCode != null) {
      sql = "select caccoutid, pk_corp, caccountyear, caccountmonth from ia_account where pk_corp = ?";
    }
    else {
      sql = "select caccoutid, pk_corp, caccountyear, caccountmonth from ia_account";
    }

    AccountVO[] accounts = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (unitCode != null) {
        stmt.setString(1, unitCode);
      }
      rs = stmt.executeQuery();

      while (rs.next()) {
        AccountVO account = new AccountVO();

        String caccoutid = rs.getString(1);
        account.setCaccoutid((caccoutid == null) ? null : caccoutid.trim());

        String pk_corp = rs.getString(2);
        account.setPk_corp((pk_corp == null) ? null : pk_corp.trim());

        String caccountyear = rs.getString(3);
        account.setCaccountyear((caccountyear == null) ? null : caccountyear.trim());

        String caccountmonth = rs.getString(4);
        account.setCaccountmonth((caccountmonth == null) ? null : caccountmonth.trim());

        v.addElement(account);
      }
    }
    finally {
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    accounts = new AccountVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(accounts);
    }

    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "queryAll", new Object[] { unitCode });

    return accounts;
  }

  public AccountVO[] queryByVO(AccountVO condAccountVO, Boolean isAnd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia402.AccountDMO", "queryByVO", new Object[] { condAccountVO, isAnd });

    String strSql = "select caccoutid, pk_corp, caccountyear, caccountmonth from ia_account";
    String strConditionNames = "";
    String strAndOr = "and ";
    if (!(isAnd.booleanValue())) {
      strAndOr = "or  ";
    }
    if (condAccountVO.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    if (condAccountVO.getCaccountyear() != null) {
      strConditionNames = strConditionNames + strAndOr + "caccountyear=? ";
    }
    if (condAccountVO.getCaccountmonth() != null) {
      strConditionNames = strConditionNames + strAndOr + "caccountmonth=? ";
    }
    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }
    else {
      return queryAll(null);
    }

    strSql = strSql + " where " + strConditionNames;

    int index = 0;
    AccountVO[] accounts = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (condAccountVO.getPk_corp() != null) {
        stmt.setString(++index, condAccountVO.getPk_corp());
      }
      if (condAccountVO.getCaccountyear() != null) {
        stmt.setString(++index, condAccountVO.getCaccountyear());
      }
      if (condAccountVO.getCaccountmonth() != null) {
        stmt.setString(++index, condAccountVO.getCaccountmonth());
      }

      rs = stmt.executeQuery();

      while (rs.next()) {
        AccountVO account = new AccountVO();

        String caccoutid = rs.getString(1);
        account.setCaccoutid((caccoutid == null) ? null : caccoutid.trim());

        String pk_corp = rs.getString(2);
        account.setPk_corp((pk_corp == null) ? null : pk_corp.trim());

        String caccountyear = rs.getString(3);
        account.setCaccountyear((caccountyear == null) ? null : caccountyear.trim());

        String caccountmonth = rs.getString(4);
        account.setCaccountmonth((caccountmonth == null) ? null : caccountmonth.trim());

        v.addElement(account);
      }
    }
    finally {
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    accounts = new AccountVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(accounts);
    }

    afterCallMethod("nc.bs.ia.ia402.AccountDMO", "queryByVO", new Object[] { condAccountVO, isAnd });

    return accounts;
  }
}