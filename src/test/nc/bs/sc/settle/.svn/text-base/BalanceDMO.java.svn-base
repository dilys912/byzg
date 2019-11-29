package nc.bs.sc.settle;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.settle.BalanceVO;

public class BalanceDMO extends DataManageObject
{
  public BalanceDMO()
    throws NamingException, SystemException
  {
  }

  public BalanceDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public String[] insertArray(BalanceVO[] balances)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "insertArray", new Object[] { balances });

    String sql = "insert into sc_balance(cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String[] keys = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      keys = getOIDs(balances.length);
      con = getConnection();
      stmt = prepareStatement(con, sql);
      for (int i = 0; i < balances.length; i++)
      {
        stmt.setString(1, keys[i]);

        if (balances[i].getPk_corp() == null) {
          stmt.setNull(2, 1);
        }
        else {
          stmt.setString(2, balances[i].getPk_corp());
        }
        if (balances[i].getCvendormangid() == null) {
          stmt.setNull(3, 1);
        }
        else {
          stmt.setString(3, balances[i].getCvendormangid());
        }
        if (balances[i].getCvendorid() == null) {
          stmt.setNull(4, 1);
        }
        else {
          stmt.setString(4, balances[i].getCvendorid());
        }
        if (balances[i].getCprocessmangid() == null) {
          stmt.setNull(5, 1);
        }
        else {
          stmt.setString(5, balances[i].getCprocessmangid());
        }
        if (balances[i].getCprocessbaseid() == null) {
          stmt.setNull(6, 1);
        }
        else {
          stmt.setString(6, balances[i].getCprocessbaseid());
        }
        if (balances[i].getCmaterialmangid() == null) {
          stmt.setNull(7, 1);
        }
        else {
          stmt.setString(7, balances[i].getCmaterialmangid());
        }
        if (balances[i].getCmaterialbaseid() == null) {
          stmt.setNull(8, 1);
        }
        else {
          stmt.setString(8, balances[i].getCmaterialbaseid());
        }
        if (balances[i].getNbalanceprice() == null) {
          stmt.setNull(9, 4);
        }
        else {
          stmt.setBigDecimal(9, balances[i].getNbalanceprice().toBigDecimal());
        }
        if (balances[i].getNbalancenum() == null) {
          stmt.setNull(10, 4);
        }
        else {
          stmt.setBigDecimal(10, balances[i].getNbalancenum().toBigDecimal());
        }
        if (balances[i].getNbalancemny() == null) {
          stmt.setNull(11, 4);
        }
        else {
          stmt.setBigDecimal(11, balances[i].getNbalancemny().toBigDecimal());
        }
        if (balances[i].getVbatch() == null) {
          stmt.setNull(12, 1);
        }
        else {
          stmt.setString(12, balances[i].getVbatch());
        }
        if (balances[i].getVfree1() == null) {
          stmt.setNull(13, 1);
        }
        else {
          stmt.setString(13, balances[i].getVfree1());
        }
        if (balances[i].getVfree2() == null) {
          stmt.setNull(14, 1);
        }
        else {
          stmt.setString(14, balances[i].getVfree2());
        }
        if (balances[i].getVfree3() == null) {
          stmt.setNull(15, 1);
        }
        else {
          stmt.setString(15, balances[i].getVfree3());
        }
        if (balances[i].getVfree4() == null) {
          stmt.setNull(16, 1);
        }
        else {
          stmt.setString(16, balances[i].getVfree4());
        }
        if (balances[i].getVfree5() == null) {
          stmt.setNull(17, 1);
        }
        else {
          stmt.setString(17, balances[i].getVfree5());
        }

        executeUpdate(stmt);
      }
      executeBatch(stmt);
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
    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "insertArray", new Object[] { balances });

    return keys;
  }

  public void delete(BalanceVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "delete", new Object[] { vo });

    String sql = "delete from sc_balance where cbalanceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
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
    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "delete", new Object[] { vo });
  }

  public void update(BalanceVO balance)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "update", new Object[] { balance });

    String sql = "update sc_balance set pk_corp = ?, cvendormangid = ?, cvendorid = ?, cprocessmangid = ?, cprocessbaseid = ?, cmaterialmangid = ?, cmaterialbaseid = ?, nbalanceprice = ?, nbalancenum = ?, nbalancemny = ?, vbatch = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?, vfree5 = ? where cbalanceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (balance.getPk_corp() == null) {
        stmt.setNull(1, 1);
      }
      else {
        stmt.setString(1, balance.getPk_corp());
      }
      if (balance.getCvendormangid() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, balance.getCvendormangid());
      }
      if (balance.getCvendorid() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, balance.getCvendorid());
      }
      if (balance.getCprocessmangid() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, balance.getCprocessmangid());
      }
      if (balance.getCprocessbaseid() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, balance.getCprocessbaseid());
      }
      if (balance.getCmaterialmangid() == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, balance.getCmaterialmangid());
      }
      if (balance.getCmaterialbaseid() == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, balance.getCmaterialbaseid());
      }
      if (balance.getNbalanceprice() == null) {
        stmt.setNull(8, 4);
      }
      else {
        stmt.setBigDecimal(8, balance.getNbalanceprice().toBigDecimal());
      }
      if (balance.getNbalancenum() == null) {
        stmt.setNull(9, 4);
      }
      else {
        stmt.setBigDecimal(9, balance.getNbalancenum().toBigDecimal());
      }
      if (balance.getNbalancemny() == null) {
        stmt.setNull(10, 4);
      }
      else {
        stmt.setBigDecimal(10, balance.getNbalancemny().toBigDecimal());
      }
      if (balance.getVbatch() == null) {
        stmt.setNull(11, 1);
      }
      else {
        stmt.setString(11, balance.getVbatch());
      }
      if (balance.getVfree1() == null) {
        stmt.setNull(12, 1);
      }
      else {
        stmt.setString(12, balance.getVfree1());
      }
      if (balance.getVfree2() == null) {
        stmt.setNull(13, 1);
      }
      else {
        stmt.setString(13, balance.getVfree2());
      }
      if (balance.getVfree3() == null) {
        stmt.setNull(14, 1);
      }
      else {
        stmt.setString(14, balance.getVfree3());
      }
      if (balance.getVfree4() == null) {
        stmt.setNull(15, 1);
      }
      else {
        stmt.setString(15, balance.getVfree4());
      }
      if (balance.getVfree5() == null) {
        stmt.setNull(16, 1);
      }
      else {
        stmt.setString(16, balance.getVfree5());
      }

      stmt.setString(17, balance.getPrimaryKey());

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
    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "update", new Object[] { balance });
  }

  public BalanceVO[] queryAll(String pk_corp)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "queryAll", new Object[] { pk_corp });

    String sql = "";
    if (pk_corp != null) {
      sql = "select cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5 from sc_balance where pk_corp = ?";
    }
    else {
      sql = "select cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5 from sc_balance";
    }

    BalanceVO[] balances = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (pk_corp != null) {
        stmt.setString(1, pk_corp);
      }
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BalanceVO balance = new BalanceVO();

        String cbalanceid = rs.getString(1);
        balance.setCbalanceid(cbalanceid == null ? null : cbalanceid.trim());

        String pk_corp1 = rs.getString(2);
        balance.setPk_corp(pk_corp1 == null ? null : pk_corp1.trim());

        String cvendormangid = rs.getString(3);
        balance.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorid = rs.getString(4);
        balance.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String cprocessmangid = rs.getString(5);
        balance.setCprocessmangid(cprocessmangid == null ? null : cprocessmangid.trim());

        String cprocessbaseid = rs.getString(6);
        balance.setCprocessbaseid(cprocessbaseid == null ? null : cprocessbaseid.trim());

        String cmaterialmangid = rs.getString(7);
        balance.setCmaterialmangid(cmaterialmangid == null ? null : cmaterialmangid.trim());

        String cmaterialbaseid = rs.getString(8);
        balance.setCmaterialbaseid(cmaterialbaseid == null ? null : cmaterialbaseid.trim());

        BigDecimal nbalanceprice = rs.getBigDecimal(9);
        balance.setNbalanceprice(nbalanceprice == null ? null : new UFDouble(nbalanceprice));

        BigDecimal nbalancenum = rs.getBigDecimal(10);
        balance.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nbalancemny = rs.getBigDecimal(11);
        balance.setNbalancemny(nbalancemny == null ? null : new UFDouble(nbalancemny));

        String vbatch = rs.getString(12);
        balance.setVbatch(vbatch == null ? null : vbatch.trim());

        String vfree1 = rs.getString(13);
        balance.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(14);
        balance.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(15);
        balance.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(16);
        balance.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(17);
        balance.setVfree5(vfree5 == null ? null : vfree5.trim());

        v.addElement(balance);
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
    }
    balances = new BalanceVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(balances);
    }

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "queryAll", new Object[] { pk_corp });

    return balances;
  }

  public BalanceVO[] queryByVO(BalanceVO condBalanceVO, Boolean isAnd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "queryByVO", new Object[] { condBalanceVO, isAnd });

    String strSql = "select cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5 from sc_balance";
    String strConditionNames = "";
    String strAndOr = "and ";
    if (!isAnd.booleanValue()) {
      strAndOr = "or  ";
    }
    if (condBalanceVO.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    if (condBalanceVO.getCvendormangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendormangid=? ";
    }
    if (condBalanceVO.getCvendorid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendorid=? ";
    }
    if (condBalanceVO.getCprocessmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessmangid=? ";
    }
    if (condBalanceVO.getCprocessbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessbaseid=? ";
    }
    if (condBalanceVO.getCmaterialmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialmangid=? ";
    }
    if (condBalanceVO.getCmaterialbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialbaseid=? ";
    }
    if (condBalanceVO.getNbalanceprice() != null) {
      strConditionNames = strConditionNames + strAndOr + "nbalanceprice=? ";
    }
    if (condBalanceVO.getNbalancenum() != null) {
      strConditionNames = strConditionNames + strAndOr + "nbalancenum=? ";
    }
    if (condBalanceVO.getNbalancemny() != null) {
      strConditionNames = strConditionNames + strAndOr + "nbalancemny=? ";
    }
    if (condBalanceVO.getVbatch() != null) {
      strConditionNames = strConditionNames + strAndOr + "vbatch=? ";
    }
    if (condBalanceVO.getVfree1() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree1=? ";
    }
    if (condBalanceVO.getVfree2() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree2=? ";
    }
    if (condBalanceVO.getVfree3() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree3=? ";
    }
    if (condBalanceVO.getVfree4() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree4=? ";
    }
    if (condBalanceVO.getVfree5() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree5=? ";
    }
    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }
    else {
      return queryAll(null);
    }

    strSql = strSql + " where dr=0 ";
    strSql = strSql + " and " + strConditionNames;

    int index = 0;
    BalanceVO[] balances = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (condBalanceVO.getPk_corp() != null) {
        index++; stmt.setString(index, condBalanceVO.getPk_corp());
      }
      if (condBalanceVO.getCvendormangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendormangid());
      }
      if (condBalanceVO.getCvendorid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendorid());
      }
      if (condBalanceVO.getCprocessmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessmangid());
      }
      if (condBalanceVO.getCprocessbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessbaseid());
      }
      if (condBalanceVO.getCmaterialmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialmangid());
      }
      if (condBalanceVO.getCmaterialbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialbaseid());
      }
      if (condBalanceVO.getNbalanceprice() != null) {
        index++; stmt.setBigDecimal(index, condBalanceVO.getNbalanceprice().toBigDecimal());
      }
      if (condBalanceVO.getNbalancenum() != null) {
        index++; stmt.setBigDecimal(index, condBalanceVO.getNbalancenum().toBigDecimal());
      }
      if (condBalanceVO.getNbalancemny() != null) {
        index++; stmt.setBigDecimal(index, condBalanceVO.getNbalancemny().toBigDecimal());
      }
      if (condBalanceVO.getVbatch() != null) {
        index++; stmt.setString(index, condBalanceVO.getVbatch());
      }
      if (condBalanceVO.getVfree1() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree1());
      }
      if (condBalanceVO.getVfree2() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree2());
      }
      if (condBalanceVO.getVfree3() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree3());
      }
      if (condBalanceVO.getVfree4() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree4());
      }
      if (condBalanceVO.getVfree5() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree5());
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BalanceVO balance = new BalanceVO();

        String cbalanceid = rs.getString(1);
        balance.setCbalanceid(cbalanceid == null ? null : cbalanceid.trim());

        String pk_corp = rs.getString(2);
        balance.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cvendormangid = rs.getString(3);
        balance.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorid = rs.getString(4);
        balance.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String cprocessmangid = rs.getString(5);
        balance.setCprocessmangid(cprocessmangid == null ? null : cprocessmangid.trim());

        String cprocessbaseid = rs.getString(6);
        balance.setCprocessbaseid(cprocessbaseid == null ? null : cprocessbaseid.trim());

        String cmaterialmangid = rs.getString(7);
        balance.setCmaterialmangid(cmaterialmangid == null ? null : cmaterialmangid.trim());

        String cmaterialbaseid = rs.getString(8);
        balance.setCmaterialbaseid(cmaterialbaseid == null ? null : cmaterialbaseid.trim());

        BigDecimal nbalanceprice = rs.getBigDecimal(9);
        balance.setNbalanceprice(nbalanceprice == null ? null : new UFDouble(nbalanceprice));

        BigDecimal nbalancenum = rs.getBigDecimal(10);
        balance.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nbalancemny = rs.getBigDecimal(11);
        balance.setNbalancemny(nbalancemny == null ? null : new UFDouble(nbalancemny));

        String vbatch = rs.getString(12);
        balance.setVbatch(vbatch == null ? null : vbatch.trim());

        String vfree1 = rs.getString(13);
        balance.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(14);
        balance.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(15);
        balance.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(16);
        balance.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(17);
        balance.setVfree5(vfree5 == null ? null : vfree5.trim());

        v.addElement(balance);
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
    }
    balances = new BalanceVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(balances);
    }

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "queryByVO", new Object[] { condBalanceVO, isAnd });

    return balances;
  }

  public String findPrimaryKey(BalanceVO balance)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "findPrimaryKey", new Object[] { balance });

    String strSql = "select cbalanceid from sc_balance";
    String strConditionNames = "";
    String strAndOr = "and ";

    if (balance.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "pk_corp is null ";
    }
    if (balance.getCvendormangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendormangid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cvendormangid is null ";
    }
    if (balance.getCvendorid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendorid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cvendorid is null ";
    }
    if (balance.getCprocessmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessmangid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cprocessmangid is null  ";
    }
    if (balance.getCprocessbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessbaseid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cprocessbaseid is null  ";
    }
    if (balance.getCmaterialmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialmangid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cmaterialmangid is null  ";
    }
    if (balance.getCmaterialbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialbaseid=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "cmaterialbaseid is null  ";
    }
    if (balance.getVbatch() != null) {
      strConditionNames = strConditionNames + strAndOr + "vbatch=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vbatch is null  ";
    }
    if (balance.getVfree1() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree1=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vfree1 is null  ";
    }
    if (balance.getVfree2() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree2=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vfree2 is null ";
    }
    if (balance.getVfree3() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree3=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vfree3 is null  ";
    }
    if (balance.getVfree4() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree4=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vfree4 is null  ";
    }
    if (balance.getVfree5() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree5=? ";
    }
    else {
      strConditionNames = strConditionNames + strAndOr + "vfree5 is null  ";
    }
    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }

    strSql = strSql + " where " + strConditionNames;

    int index = 0;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (balance.getPk_corp() != null) {
        index++; stmt.setString(index, balance.getPk_corp());
      }
      if (balance.getCvendormangid() != null) {
        index++; stmt.setString(index, balance.getCvendormangid());
      }
      if (balance.getCvendorid() != null) {
        index++; stmt.setString(index, balance.getCvendorid());
      }
      if (balance.getCprocessmangid() != null) {
        index++; stmt.setString(index, balance.getCprocessmangid());
      }
      if (balance.getCprocessbaseid() != null) {
        index++; stmt.setString(index, balance.getCprocessbaseid());
      }
      if (balance.getCmaterialmangid() != null) {
        index++; stmt.setString(index, balance.getCmaterialmangid());
      }
      if (balance.getCmaterialbaseid() != null) {
        index++; stmt.setString(index, balance.getCmaterialbaseid());
      }
      if (balance.getVbatch() != null) {
        index++; stmt.setString(index, balance.getVbatch());
      }
      if (balance.getVfree1() != null) {
        index++; stmt.setString(index, balance.getVfree1());
      }
      if (balance.getVfree2() != null) {
        index++; stmt.setString(index, balance.getVfree2());
      }
      if (balance.getVfree3() != null) {
        index++; stmt.setString(index, balance.getVfree3());
      }
      if (balance.getVfree4() != null) {
        index++; stmt.setString(index, balance.getVfree4());
      }
      if (balance.getVfree5() != null) {
        index++; stmt.setString(index, balance.getVfree5());
      }

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String str1 = rs.getString(1);
        return str1;
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "findPrimaryKey", new Object[] { balance });

    return null;
  }

  public String insert(BalanceVO balance, UFBoolean isAdd)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "insert", new Object[] { balance });

    String sql = "insert into sc_balance(cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    boolean isPlus = isAdd.booleanValue();
    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      key = getOID();
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);

      if (balance.getPk_corp() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, balance.getPk_corp());
      }
      if (balance.getCvendormangid() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, balance.getCvendormangid());
      }
      if (balance.getCvendorid() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, balance.getCvendorid());
      }
      if (balance.getCprocessmangid() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, balance.getCprocessmangid());
      }
      if (balance.getCprocessbaseid() == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, balance.getCprocessbaseid());
      }
      if (balance.getCmaterialmangid() == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, balance.getCmaterialmangid());
      }
      if (balance.getCmaterialbaseid() == null) {
        stmt.setNull(8, 1);
      }
      else {
        stmt.setString(8, balance.getCmaterialbaseid());
      }
      if (balance.getNbalanceprice() == null) {
        stmt.setNull(9, 4);
      }
      else {
        stmt.setBigDecimal(9, balance.getNbalanceprice().toBigDecimal());
      }
      if (balance.getNbalancenum() == null) {
        stmt.setNull(10, 4);
      }
      else {
        stmt.setBigDecimal(10, isPlus ? balance.getNbalancenum().toBigDecimal() : new UFDouble("-" + balance.getNbalancenum() + "").toBigDecimal());
      }
      if (balance.getNbalancemny() == null) {
        stmt.setNull(11, 4);
      }
      else {
        stmt.setBigDecimal(11, isPlus ? balance.getNbalancemny().toBigDecimal() : new UFDouble("-" + balance.getNbalancemny() + "").toBigDecimal());
      }
      if (balance.getVbatch() == null) {
        stmt.setNull(12, 1);
      }
      else {
        stmt.setString(12, balance.getVbatch());
      }
      if (balance.getVfree1() == null) {
        stmt.setNull(13, 1);
      }
      else {
        stmt.setString(13, balance.getVfree1());
      }
      if (balance.getVfree2() == null) {
        stmt.setNull(14, 1);
      }
      else {
        stmt.setString(14, balance.getVfree2());
      }
      if (balance.getVfree3() == null) {
        stmt.setNull(15, 1);
      }
      else {
        stmt.setString(15, balance.getVfree3());
      }
      if (balance.getVfree4() == null) {
        stmt.setNull(16, 1);
      }
      else {
        stmt.setString(16, balance.getVfree4());
      }
      if (balance.getVfree5() == null) {
        stmt.setNull(17, 1);
      }
      else {
        stmt.setString(17, balance.getVfree5());
      }

      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "insert", new Object[] { balance });

    return key;
  }

  public String insertArray(BalanceVO[] balances, UFBoolean isAdd)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "insertArray", new Object[] { balances, isAdd });

    String sql = "insert into sc_balance(cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    String pk_corp = balances[0].getPk_corp();
    boolean isPlus = isAdd.booleanValue();
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < balances.length; i++) {
        key = getOID(pk_corp);

        stmt.setString(1, key);

        if (balances[i].getPk_corp() == null) {
          stmt.setNull(2, 1);
        }
        else {
          stmt.setString(2, balances[i].getPk_corp());
        }
        if (balances[i].getCvendormangid() == null) {
          stmt.setNull(3, 1);
        }
        else {
          stmt.setString(3, balances[i].getCvendormangid());
        }
        if (balances[i].getCvendorid() == null) {
          stmt.setNull(4, 1);
        }
        else {
          stmt.setString(4, balances[i].getCvendorid());
        }
        if (balances[i].getCprocessmangid() == null) {
          stmt.setNull(5, 1);
        }
        else {
          stmt.setString(5, balances[i].getCprocessmangid());
        }
        if (balances[i].getCprocessbaseid() == null) {
          stmt.setNull(6, 1);
        }
        else {
          stmt.setString(6, balances[i].getCprocessbaseid());
        }
        if (balances[i].getCmaterialmangid() == null) {
          stmt.setNull(7, 1);
        }
        else {
          stmt.setString(7, balances[i].getCmaterialmangid());
        }
        if (balances[i].getCmaterialbaseid() == null) {
          stmt.setNull(8, 1);
        }
        else {
          stmt.setString(8, balances[i].getCmaterialbaseid());
        }
        if (balances[i].getNbalanceprice() == null) {
          stmt.setNull(9, 4);
        }
        else {
          stmt.setBigDecimal(9, balances[i].getNbalanceprice().toBigDecimal());
        }
        if (balances[i].getNbalancenum() == null) {
          stmt.setNull(10, 4);
        }
        else {
          stmt.setBigDecimal(10, isPlus ? balances[i].getNbalancenum().toBigDecimal() : new UFDouble("-" + balances[i].getNbalancenum() + "").toBigDecimal());
        }
        if (balances[i].getNbalancemny() == null) {
          stmt.setNull(11, 4);
        }
        else {
          stmt.setBigDecimal(11, isPlus ? balances[i].getNbalancemny().toBigDecimal() : new UFDouble("-" + balances[i].getNbalancemny() + "").toBigDecimal());
        }
        if (balances[i].getVbatch() == null) {
          stmt.setNull(12, 1);
        }
        else {
          stmt.setString(12, balances[i].getVbatch());
        }
        if (balances[i].getVfree1() == null) {
          stmt.setNull(13, 1);
        }
        else {
          stmt.setString(13, balances[i].getVfree1());
        }
        if (balances[i].getVfree2() == null) {
          stmt.setNull(14, 1);
        }
        else {
          stmt.setString(14, balances[i].getVfree2());
        }
        if (balances[i].getVfree3() == null) {
          stmt.setNull(15, 1);
        }
        else {
          stmt.setString(15, balances[i].getVfree3());
        }
        if (balances[i].getVfree4() == null) {
          stmt.setNull(16, 1);
        }
        else {
          stmt.setString(16, balances[i].getVfree4());
        }
        if (balances[i].getVfree5() == null) {
          stmt.setNull(17, 1);
        }
        else {
          stmt.setString(17, balances[i].getVfree5());
        }

        executeUpdate(stmt);
      }
      executeBatch(stmt);
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "insertArray", new Object[] { balances, isAdd });

    return key;
  }

  public BalanceVO[] queryByVONormally(BalanceVO condBalanceVO, Boolean isAnd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "queryByVONormally", new Object[] { condBalanceVO, isAnd });

    String strSql = "select cbalanceid, pk_corp, cvendormangid, cvendorid, cprocessmangid, cprocessbaseid, cmaterialmangid, cmaterialbaseid, nbalanceprice, nbalancenum, nbalancemny, vbatch, vfree1, vfree2, vfree3, vfree4, vfree5 from sc_balance";
    String strConditionNames = "";
    String strAndOr = "and ";
    if (!isAnd.booleanValue()) {
      strAndOr = "or  ";
    }
    if (condBalanceVO.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    if (condBalanceVO.getCvendormangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendormangid=? ";
    }
    if (condBalanceVO.getCvendorid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendorid=? ";
    }
    if (condBalanceVO.getCprocessmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessmangid=? ";
    }
    if (condBalanceVO.getCprocessbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessbaseid=? ";
    }
    if (condBalanceVO.getCmaterialmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialmangid=? ";
    }
    if (condBalanceVO.getCmaterialbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialbaseid=? ";
    }
    if (condBalanceVO.getVbatch() != null) {
      strConditionNames = strConditionNames + strAndOr + "vbatch=? ";
    }
    if (condBalanceVO.getVfree1() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree1=? ";
    }
    if (condBalanceVO.getVfree2() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree2=? ";
    }
    if (condBalanceVO.getVfree3() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree3=? ";
    }
    if (condBalanceVO.getVfree4() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree4=? ";
    }
    if (condBalanceVO.getVfree5() != null) {
      strConditionNames = strConditionNames + strAndOr + "vfree5=? ";
    }
    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }
    else {
      return queryAll(null);
    }

    strSql = strSql + " where " + strConditionNames;

    int index = 0;
    BalanceVO[] balances = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (condBalanceVO.getPk_corp() != null) {
        index++; stmt.setString(index, condBalanceVO.getPk_corp());
      }
      if (condBalanceVO.getCvendormangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendormangid());
      }
      if (condBalanceVO.getCvendorid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendorid());
      }
      if (condBalanceVO.getCprocessmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessmangid());
      }
      if (condBalanceVO.getCprocessbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessbaseid());
      }
      if (condBalanceVO.getCmaterialmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialmangid());
      }
      if (condBalanceVO.getCmaterialbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialbaseid());
      }
      if (condBalanceVO.getVbatch() != null) {
        index++; stmt.setString(index, condBalanceVO.getVbatch());
      }
      if (condBalanceVO.getVfree1() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree1());
      }
      if (condBalanceVO.getVfree2() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree2());
      }
      if (condBalanceVO.getVfree3() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree3());
      }
      if (condBalanceVO.getVfree4() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree4());
      }
      if (condBalanceVO.getVfree5() != null) {
        index++; stmt.setString(index, condBalanceVO.getVfree5());
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BalanceVO balance = new BalanceVO();

        String cbalanceid = rs.getString(1);
        balance.setCbalanceid(cbalanceid == null ? null : cbalanceid.trim());

        String pk_corp = rs.getString(2);
        balance.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cvendormangid = rs.getString(3);
        balance.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorid = rs.getString(4);
        balance.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String cprocessmangid = rs.getString(5);
        balance.setCprocessmangid(cprocessmangid == null ? null : cprocessmangid.trim());

        String cprocessbaseid = rs.getString(6);
        balance.setCprocessbaseid(cprocessbaseid == null ? null : cprocessbaseid.trim());

        String cmaterialmangid = rs.getString(7);
        balance.setCmaterialmangid(cmaterialmangid == null ? null : cmaterialmangid.trim());

        String cmaterialbaseid = rs.getString(8);
        balance.setCmaterialbaseid(cmaterialbaseid == null ? null : cmaterialbaseid.trim());

        BigDecimal nbalanceprice = rs.getBigDecimal(9);
        balance.setNbalanceprice(nbalanceprice == null ? null : new UFDouble(nbalanceprice));

        BigDecimal nbalancenum = rs.getBigDecimal(10);
        balance.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nbalancemny = rs.getBigDecimal(11);
        balance.setNbalancemny(nbalancemny == null ? null : new UFDouble(nbalancemny));

        String vbatch = rs.getString(12);
        balance.setVbatch(vbatch == null ? null : vbatch.trim());

        String vfree1 = rs.getString(13);
        balance.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(14);
        balance.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(15);
        balance.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(16);
        balance.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(17);
        balance.setVfree5(vfree5 == null ? null : vfree5.trim());

        v.addElement(balance);
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
    }
    balances = new BalanceVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(balances);
    }

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "queryByVONormally", new Object[] { condBalanceVO, isAnd });

    return balances;
  }

  public BalanceVO queryPrice(BalanceVO condBalanceVO)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "queryPrice", new Object[] { condBalanceVO });

    String strSql = "select sum(nbalancenum), sum(nbalancemny) from sc_balance";
    String strConditionNames = "";
    String strAndOr = "and ";

    if (condBalanceVO.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    if (condBalanceVO.getCvendormangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendormangid=? ";
    }
    if (condBalanceVO.getCvendorid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendorid=? ";
    }
    if (condBalanceVO.getCprocessmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessmangid=? ";
    }
    if (condBalanceVO.getCprocessbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cprocessbaseid=? ";
    }
    if (condBalanceVO.getCmaterialmangid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialmangid=? ";
    }
    if (condBalanceVO.getCmaterialbaseid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cmaterialbaseid=? ";
    }
    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }

    strSql = strSql + " where " + strConditionNames;

    int index = 0;
    BalanceVO balance = new BalanceVO();

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (condBalanceVO.getPk_corp() != null) {
        index++; stmt.setString(index, condBalanceVO.getPk_corp());
      }
      if (condBalanceVO.getCvendormangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendormangid());
      }
      if (condBalanceVO.getCvendorid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCvendorid());
      }
      if (condBalanceVO.getCprocessmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessmangid());
      }
      if (condBalanceVO.getCprocessbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCprocessbaseid());
      }
      if (condBalanceVO.getCmaterialmangid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialmangid());
      }
      if (condBalanceVO.getCmaterialbaseid() != null) {
        index++; stmt.setString(index, condBalanceVO.getCmaterialbaseid());
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BigDecimal nbalancenum = rs.getBigDecimal(1);
        balance.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nbalancemny = rs.getBigDecimal(2);
        balance.setNbalancemny(nbalancemny == null ? null : new UFDouble(nbalancemny));
      }
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "queryPrice", new Object[] { condBalanceVO });

    return balance;
  }

  public void updateArray(BalanceVO[] balances, UFBoolean isAdd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "updateArray", new Object[] { balances, isAdd });

    String sql = "update sc_balance set  nbalancenum = isnull(nbalancenum,0)-(?), nbalancemny = isnull(nbalancemny,0)-(?) where cbalanceid = ?";

    if (isAdd.booleanValue()) {
      sql = "update sc_balance set  nbalancenum = isnull(nbalancenum,0)+(?), nbalancemny = isnull(nbalancemny,0)+(?) where cbalanceid = ?";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);
      for (int i = 0; i < balances.length; i++)
      {
        if (balances[i].getNbalancenum() == null)
        {
          stmt.setBigDecimal(1, new UFDouble(0).toBigDecimal());
        }
        else {
          stmt.setBigDecimal(1, balances[i].getNbalancenum().toBigDecimal());
        }
        if (balances[i].getNbalancemny() == null)
        {
          stmt.setBigDecimal(2, new UFDouble(0).toBigDecimal());
        }
        else {
          stmt.setBigDecimal(2, balances[i].getNbalancemny().toBigDecimal());
        }

        stmt.setString(3, balances[i].getPrimaryKey());

        executeUpdate(stmt);
      }
      executeBatch(stmt);
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "updateArray", new Object[] { balances, isAdd });
  }

  public void updateBalanceData(BalanceVO balance, UFBoolean isAdd)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "updateBalanceData", new Object[] { balance });

    String cbalanceid = findPrimaryKey(balance);
    if (cbalanceid == null) {
      insert(balance, isAdd);
    } else {
      balance.setPrimaryKey(cbalanceid);
      updateData(balance, isAdd);
    }

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "updateBalanceData", new Object[] { balance });
  }

  public void updateBalanceDatas(BalanceVO[] balances, UFBoolean isAdd)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "updateBalanceDatas", new Object[] { balances });

    if (balances == null) {
      return;
    }
    Vector vInsert = new Vector();
    Vector vUpdate = new Vector();
    for (int i = 0; i < balances.length; i++)
    {
      BalanceVO balance = balances[i];

      String cbalanceid = findPrimaryKey(balance);
      if (cbalanceid == null)
      {
        vInsert.addElement(balance);
      } else {
        balance.setPrimaryKey(cbalanceid);

        vUpdate.addElement(balance);
      }
    }

    if (vInsert.size() > 0) {
      BalanceVO[] tempVO = new BalanceVO[vInsert.size()];
      vInsert.copyInto(tempVO);
      insertArray(tempVO, isAdd);
    }

    if (vUpdate.size() > 0) {
      BalanceVO[] tempVO = new BalanceVO[vUpdate.size()];
      vUpdate.copyInto(tempVO);
      updateArray(tempVO, isAdd);
    }

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "updateBalanceDatas", new Object[] { balances });
  }

  public void updateData(BalanceVO balance, UFBoolean isAdd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.balance.BalanceDMO", "updateData", new Object[] { balance });

    String sql = "update sc_balance set  nbalancenum = isnull(nbalancenum,0)-(?), nbalancemny = isnull(nbalancemny,0)-(?) where cbalanceid = ?";
    if (isAdd.booleanValue()) {
      sql = "update sc_balance set  nbalancenum = isnull(nbalancenum,0)+(?), nbalancemny = isnull(nbalancemny,0)+(?) where cbalanceid = ?";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (balance.getNbalancenum() == null)
      {
        stmt.setBigDecimal(1, new UFDouble(0).toBigDecimal());
      }
      else {
        stmt.setBigDecimal(1, balance.getNbalancenum().toBigDecimal());
      }
      if (balance.getNbalancemny() == null)
      {
        stmt.setBigDecimal(2, new UFDouble(0).toBigDecimal());
      }
      else {
        stmt.setBigDecimal(2, balance.getNbalancemny().toBigDecimal());
      }

      stmt.setString(3, balance.getPrimaryKey());

      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
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

    afterCallMethod("nc.bs.sc.balance.BalanceDMO", "updateData", new Object[] { balance });
  }
}