package nc.bs.ic.ic2a3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.ic.ic2a3.AccountctrlHeaderVO;
import nc.vo.ic.ic2a3.AccountctrlItemVO;
import nc.vo.ic.ic2a3.AccountctrlVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.SCMEnv;

public class AccountctrlDMO extends DataManageObject
{
  public static final int BY_CAL = 1;
  public static final int BY_WH = 2;

  public AccountctrlDMO()
    throws NamingException, SystemException
  {
  }

  public AccountctrlDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public AccountctrlVO findByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findByPrimaryKey", new Object[] { key });

    AccountctrlVO vo = new AccountctrlVO();

    AccountctrlHeaderVO header = findHeaderByPrimaryKey(key);
    AccountctrlItemVO[] items = null;
    if (header != null) {
      items = findItemsForHeader(header.getPrimaryKey());
    }

    vo.setParentVO(header);
    vo.setChildrenVO(items);

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findByPrimaryKey", new Object[] { key });

    return vo;
  }

  public AccountctrlHeaderVO findHeaderByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findHeaderByPrimaryKey", new Object[] { key });

    String sql = "select pk_calbody, tstarttime, tendtime, faccountflag, coperatorid, vremark, voperatorname, vcalbodyname, ts from ic_accountctrl where pk_accountctrl = ?";

    AccountctrlHeaderVO accountctrlHeader = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        accountctrlHeader = new AccountctrlHeaderVO(key);

        String pk_calbody = rs.getString(1);
        accountctrlHeader.setPk_calbody(pk_calbody == null ? null : pk_calbody.trim());

        String tstarttime = rs.getString(2);
        accountctrlHeader.setTstarttime(tstarttime == null ? null : new UFDateTime(tstarttime.trim()));

        String tendtime = rs.getString(3);
        accountctrlHeader.setTendtime(tendtime == null ? null : new UFDateTime(tendtime.trim()));

        String faccountflag = rs.getString(4);
        accountctrlHeader.setFaccountflag(faccountflag == null ? null : new UFBoolean(faccountflag.trim()));

        String coperatorid = rs.getString(5);
        accountctrlHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String vremark = rs.getString(6);
        accountctrlHeader.setVremark(vremark == null ? null : vremark.trim());

        String voperatorname = rs.getString(7);
        accountctrlHeader.setVoperatorname(voperatorname == null ? null : voperatorname.trim());

        String vcalbodyname = rs.getString(8);
        accountctrlHeader.setVcalbodyname(vcalbodyname == null ? null : vcalbodyname.trim());

        String ts = rs.getString(9);
        accountctrlHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findHeaderByPrimaryKey", new Object[] { key });

    return accountctrlHeader;
  }

  public AccountctrlItemVO findItemByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findItemByPrimaryKey", new Object[] { key });

    String sql = "select pk_accountctrl, factionflag, coperatorid, vremark, voperatorname, ts from ic_accountctrl_b where dr=0 and pk_accountctrl_b = ?";

    AccountctrlItemVO accountctrlItem = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        accountctrlItem = new AccountctrlItemVO(key);

        String pk_accountctrl = rs.getString(1);
        accountctrlItem.setPk_accountctrl(pk_accountctrl == null ? null : pk_accountctrl.trim());

        String factionflag = rs.getString(2);
        accountctrlItem.setFactionflag(factionflag == null ? null : new UFBoolean(factionflag.trim()));

        String coperatorid = rs.getString(3);
        accountctrlItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String vremark = rs.getString(4);
        accountctrlItem.setVremark(vremark == null ? null : vremark.trim());

        String voperatorname = rs.getString(5);
        accountctrlItem.setVoperatorname(voperatorname == null ? null : voperatorname.trim());

        String ts = rs.getString(6);
        accountctrlItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findItemByPrimaryKey", new Object[] { key });

    return accountctrlItem;
  }

  public AccountctrlItemVO[] findItemsForHeader(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findItemsForHeader", new Object[] { key });

    String sql = "select accb.pk_accountctrl_b, accb.pk_accountctrl, accb.factionflag, accb.coperatorid, accb.vremark, us.user_name  AS voperatorname, accb.ts from ic_accountctrl_b accb left outer join sm_user us on accb.coperatorid=us.cuserid where accb.pk_accountctrl =? and accb.dr=0 order by accb.ts";

    AccountctrlItemVO[] accountctrlItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        AccountctrlItemVO accountctrlItem = new AccountctrlItemVO();

        String pk_accountctrl_b = rs.getString("pk_accountctrl_b");
        accountctrlItem.setPk_accountctrl_b(pk_accountctrl_b == null ? null : pk_accountctrl_b.trim());

        String pk_accountctrl = rs.getString("pk_accountctrl");
        accountctrlItem.setPk_accountctrl(pk_accountctrl == null ? null : pk_accountctrl.trim());

        String factionflag = rs.getString("factionflag");
        accountctrlItem.setFactionflag(factionflag == null ? null : new UFBoolean(factionflag.trim()));

        String coperatorid = rs.getString("coperatorid");
        accountctrlItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String vremark = rs.getString("vremark");
        accountctrlItem.setVremark(vremark == null ? null : vremark.trim());

        String voperatorname = rs.getString("voperatorname");
        accountctrlItem.setVoperatorname(voperatorname == null ? null : voperatorname.trim());

        String ts = rs.getString("ts");
        accountctrlItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
        v.addElement(accountctrlItem);
      }
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    accountctrlItems = new AccountctrlItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(accountctrlItems);
    }

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "findItemsForHeader", new Object[] { key });

    return accountctrlItems;
  }

  public String insert(AccountctrlVO vo)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insert", new Object[] { vo });

    String key = insertHeader((AccountctrlHeaderVO)vo.getParentVO());

    AccountctrlItemVO[] items = (AccountctrlItemVO[])(AccountctrlItemVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      insertItem(items[i], key);
    }

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insert", new Object[] { vo });

    return key;
  }

  public String insertHeader(AccountctrlHeaderVO accountctrlHeader)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertHeader", new Object[] { accountctrlHeader });

    String sql = "insert into ic_accountctrl(pk_accountctrl, pk_calbody,cwarehouseid, tstarttime, tendtime, faccountflag, coperatorid,dr) values(?, ?, ?, ?, ?, ?, ?,0)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      key = getOID();
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);

      if (accountctrlHeader.getPk_calbody() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, accountctrlHeader.getPk_calbody());
      }

      if (accountctrlHeader.getPk_stordoc() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, accountctrlHeader.getPk_stordoc());
      }

      if (accountctrlHeader.getTstarttime() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, accountctrlHeader.getTstarttime().toString());
      }

      if (accountctrlHeader.getTendtime() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, accountctrlHeader.getTendtime().toString());
      }

      if (accountctrlHeader.getFaccountflag() == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, accountctrlHeader.getFaccountflag().toString());
      }
      if (accountctrlHeader.getCoperatorid() == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, accountctrlHeader.getCoperatorid());
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertHeader", new Object[] { accountctrlHeader });

    return key;
  }

  public String insertItem(AccountctrlItemVO accountctrlItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertItem", new Object[] { accountctrlItem });

    String sql = "insert into ic_accountctrl_b(pk_accountctrl_b, pk_accountctrl, factionflag, coperatorid,dr) values(?, ?, ?, ?,0)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      key = getOID();
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);

      if (accountctrlItem.getPk_accountctrl() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, accountctrlItem.getPk_accountctrl());
      }
      if (accountctrlItem.getFactionflag() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, accountctrlItem.getFactionflag().toString());
      }
      if (accountctrlItem.getCoperatorid() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, accountctrlItem.getCoperatorid());
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertItem", new Object[] { accountctrlItem });

    return key;
  }

  public String insertItem(AccountctrlItemVO accountctrlItem, String foreignKey)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertItem", new Object[] { accountctrlItem, foreignKey });

    accountctrlItem.setPk_accountctrl(foreignKey);
    String key = insertItem(accountctrlItem);

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "insertItem", new Object[] { accountctrlItem, foreignKey });

    return key;
  }

  public void delete(AccountctrlVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "delete", new Object[] { vo });

    deleteItemsForHeader(((AccountctrlHeaderVO)vo.getParentVO()).getPrimaryKey());
    deleteHeader((AccountctrlHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "delete", new Object[] { vo });
  }

  public void deleteHeader(AccountctrlHeaderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteHeader", new Object[] { vo });

    String sql = "delete from ic_accountctrl where pk_accountctrl = ?";

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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteHeader", new Object[] { vo });
  }

  public void deleteItem(AccountctrlItemVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteItem", new Object[] { vo });

    String sql = "delete from ic_accountctrl_b where pk_accountctrl_b = ?";

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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteItem", new Object[] { vo });
  }

  public void deleteItemsForHeader(String headerKey)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteItemsForHeader", new Object[] { headerKey });

    String sql = "delete from ic_accountctrl_b where pk_accountctrl = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, headerKey);
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "deleteItemsForHeader", new Object[] { headerKey });
  }

  public void update(AccountctrlVO vo)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "update", new Object[] { vo });

    AccountctrlItemVO[] items = (AccountctrlItemVO[])(AccountctrlItemVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      switch (items[i].getStatus()) {
      case 2:
        insertItem(items[i]);
        break;
      case 1:
        updateItem(items[i]);
        break;
      case 3:
        deleteItem(items[i]);
      }
    }
    updateHeader((AccountctrlHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "update", new Object[] { vo });
  }

  public void updateHeader(AccountctrlHeaderVO accountctrlHeader)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "updateHeader", new Object[] { accountctrlHeader });

    String pkCal = accountctrlHeader.getPk_calbody();
    String pkWh = accountctrlHeader.getPk_stordoc();
    int iChoose = 0;
    if (pkWh != null)
      iChoose = 2;
    else if (pkCal != null)
      iChoose = 1;
    else return;

    String sql1 = "update ic_accountctrl set faccountflag=?,coperatorid=? where pk_calbody=? and tstarttime<=?";

    String sql2 = "update ic_accountctrl set faccountflag=?,coperatorid=? where pk_calbody=? and tstarttime>=?";

    String sql3 = "update ic_accountctrl set faccountflag=?,coperatorid=? where cwarehouseid=? and tstarttime<=?";

    String sql4 = "update ic_accountctrl set faccountflag=?,coperatorid=? where cwarehouseid=? and tstarttime>=?";

    String sql1_ex = "update ic_accountctrl set tendtime=? where pk_calbody=? and tendtime is null";

    String sql3_ex = "update ic_accountctrl set tendtime=? where cwarehouseid=? and tendtime is null";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();

      if (iChoose == 1) {
        if ("N".equals(accountctrlHeader.getFaccountflag().toString()))
          stmt = con.prepareStatement(sql1);
        else {
          stmt = con.prepareStatement(sql2);
        }
      }
      else if (iChoose == 2) {
        if ("N".equals(accountctrlHeader.getFaccountflag().toString()))
          stmt = con.prepareStatement(sql3);
        else
          stmt = con.prepareStatement(sql4);
      }
      if (accountctrlHeader.getFaccountflag() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, accountctrlHeader.getFaccountflag().toString());
      }
      if (accountctrlHeader.getCoperatorid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, accountctrlHeader.getCoperatorid());
      }
      if (iChoose == 1) {
        if (accountctrlHeader.getPk_calbody() == null)
          stmt.setNull(3, 1);
        else
          stmt.setString(3, accountctrlHeader.getPk_calbody());
      }
      else if (iChoose == 2) {
        if (accountctrlHeader.getPk_stordoc() == null)
          stmt.setNull(3, 1);
        else
          stmt.setString(3, accountctrlHeader.getPk_stordoc());
      }
      if (accountctrlHeader.getTstarttime() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, accountctrlHeader.getTstarttime().toString());
      }
      stmt.executeUpdate();

      if ((accountctrlHeader.m_endtimeisnull) && 
        ("N".equals(accountctrlHeader.getFaccountflag().toString()))) {
        if (iChoose == 1) {
          stmt = con.prepareStatement(sql1_ex);
        }
        else if (iChoose == 2) {
          stmt = con.prepareStatement(sql3_ex);
        }
        if (accountctrlHeader.getTendtime() == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, accountctrlHeader.getTendtime().toString());
        }

        if (iChoose == 1) {
          if (accountctrlHeader.getPk_calbody() == null)
            stmt.setNull(2, 1);
          else
            stmt.setString(2, accountctrlHeader.getPk_calbody());
        }
        else if (iChoose == 2) {
          if (accountctrlHeader.getPk_stordoc() == null)
            stmt.setNull(2, 1);
          else
            stmt.setString(2, accountctrlHeader.getPk_stordoc());
        }
        stmt.executeUpdate();
      }
    }
    finally {
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

    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "updateHeader", new Object[] { accountctrlHeader });
  }

  public void updateItem(AccountctrlItemVO accountctrlItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "updateItem", new Object[] { accountctrlItem });

    String sql = "update ic_accountctrl_b set pk_accountctrl = ?, factionflag = ?, coperatorid = ?, vremark = ?, voperatorname = ?, ts = ? where pk_accountctrl_b = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (accountctrlItem.getPk_accountctrl() == null) {
        stmt.setNull(1, 1);
      }
      else {
        stmt.setString(1, accountctrlItem.getPk_accountctrl());
      }
      if (accountctrlItem.getFactionflag() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, accountctrlItem.getFactionflag().toString());
      }
      if (accountctrlItem.getCoperatorid() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, accountctrlItem.getCoperatorid());
      }
      if (accountctrlItem.getVremark() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, accountctrlItem.getVremark());
      }
      if (accountctrlItem.getVoperatorname() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, accountctrlItem.getVoperatorname());
      }
      if (accountctrlItem.getTs() == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, accountctrlItem.getTs().toString());
      }

      stmt.setString(7, accountctrlItem.getPrimaryKey());

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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "updateItem", new Object[] { accountctrlItem });
  }

  public void checkAccountStatus(GeneralBillVO[] voaBill)
    throws BusinessException, SQLException
  {
    String sAlter = getBetweenClosedAccPeriod(voaBill);
    if ((sAlter != null) && (sAlter.length() > 0))
      throw new BusinessException(sAlter);
  }

  public void checkAccountStatus(GeneralBillVO voBill)
    throws BusinessException, SQLException
  {
    String sAlter = getBetweenClosedAccPeriod(voBill);
    if ((sAlter != null) && (sAlter.length() > 0))
      throw new BusinessException(sAlter);
  }

  public void checkAccountStatus1(GeneralBillVO voBill)
    throws BusinessException
  {
    String sAlter = getBetweenClosedAccPeriod1(voBill);
    if ((sAlter != null) && (sAlter.length() > 0))
      throw new BusinessException(sAlter);
  }

  public ArrayList findAllWh(String sCorpID)
  {
    ArrayList alReturn = new ArrayList();
    String sql = "select distinct cwarehouseid from ic_accountctrl";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      String sWh = null;
      rs = stmt.executeQuery();
      while (rs.next()) {
        sWh = rs.getString(1);
        if (sWh != null)
          alReturn.add(sWh);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    return alReturn;
  }

  private String getBetweenClosedAccPeriod(GeneralBillVO[] billVOs)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod", new Object[] { billVOs });

    if (billVOs == null) {
      SCMEnv.out("no param..");
      return null;
    }

    StringBuffer sbAlter = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000107") + "\n");
    boolean bIsError = false;

    String sWhPk = null;
    Hashtable htWh = null;
    ArrayList alWhPK = null;
    Hashtable htItemDate = null;
    String sBillCode = null;
    ArrayList alBillCode = null;

    UFDate ufdCur = null;

    ArrayList alData = getData(billVOs);

    if ((alData == null) || (alData.size() <= 0)) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer("select max(tendtime) as tendtime from ic_accountctrl where cwarehouseid=? and faccountflag='N'");

    sbSql.append(" and dr=0");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDateTime ufdEndDateTime = null;
    try {
      con = getConnection();
      htWh = (Hashtable)alData.get(0);
      alWhPK = (ArrayList)alData.get(1);

      int iCalCount = alWhPK.size();

      for (int i = 0; i < iCalCount; i++) {
        sWhPk = (String)alWhPK.get(i);
        htItemDate = (Hashtable)htWh.get(sWhPk);
        Enumeration enuDate = htItemDate.keys();

        stmt = con.prepareStatement(sbSql.toString());

        stmt.setString(1, sWhPk);
        rs = stmt.executeQuery();

        if (rs.next()) {
          boolean bIsClose = false;
          String sEndTime = rs.getString(1);
          if (sEndTime != null) {
            ufdEndDateTime = new UFDateTime(sEndTime);
            while (enuDate.hasMoreElements()) {
              ufdCur = (UFDate)enuDate.nextElement();
              if (ufdCur.compareTo(ufdEndDateTime.getDate()) <= 0) {
                sbAlter.append("---------------------\n");
                sbAlter.append(ufdCur.toString());
                sbAlter.append(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000108"));
                alBillCode = (ArrayList)htItemDate.get(ufdCur);
                for (int k = 0; k < alBillCode.size(); k++) {
                  sbAlter.append("\n");
                  sBillCode = (String)alBillCode.get(k);
                  sbAlter.append(sBillCode);
                }
                sbAlter.append("\n");
                bIsClose = true;
              }
            }
            if (bIsClose) {
              sbAlter.append(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000109")).append(ufdEndDateTime).append("\n");
              bIsError = true;
            }
          }
        }
      }
    }
    finally
    {
      try
      {
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod", new Object[] { billVOs });

    if (bIsError == true) {
      return sbAlter.toString();
    }
    return null;
  }

  private String getBetweenClosedAccPeriod(GeneralBillVO billVO)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod", new Object[] { billVO });

    if (billVO == null) {
      SCMEnv.out("no param..");
      return null;
    }

    StringBuffer sbAlter = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000110") + "\n");
    boolean bIsClose = false;
    GeneralBillHeaderVO voHeader = billVO.getHeaderVO();
    GeneralBillItemVO[] voaItem = billVO.getItemVOs();

    String sWarehouseID = null;
    if (voHeader != null) {
      sWarehouseID = voHeader.getCwarehouseid();
    }
    if ((sWarehouseID == null) || (voaItem == null) || (voaItem.length == 0)) {
      SCMEnv.out("no param..");
      return null;
    }

    Hashtable htDate = new Hashtable();
    ArrayList alDate = new ArrayList();
    int iLen = voaItem.length;
    UFDate ufdItemDate = null;
    for (int i = 0; i < iLen; i++) {
      ufdItemDate = voaItem[i].getDbizdate();
      if ((ufdItemDate != null) && (!htDate.containsKey(ufdItemDate))) {
        alDate.add(ufdItemDate);
      }
    }

    if (alDate.size() <= 0) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer("select max(tendtime) as tendtime from ic_accountctrl where cwarehouseid=? and faccountflag='N' and dr=0");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDateTime ufdEndDateTime = null;
    int iLength = alDate.size();
    UFDate ufdCur = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      stmt.setString(1, sWarehouseID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        String sEndTime = rs.getString(1);
        if (sEndTime != null) {
          ufdEndDateTime = new UFDateTime(sEndTime);
          for (int i = 0; i < iLength; i++) {
            ufdCur = (UFDate)alDate.get(i);

            if (ufdCur.compareTo(ufdEndDateTime.getDate()) <= 0) {
              sbAlter.append("---------------------\n");
              sbAlter.append(ufdCur.toString());
              sbAlter.append("\n");
              bIsClose = true;
            }
          }
          sbAlter.append(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000109"));
          sbAlter.append("\n");
          sbAlter.append(ufdEndDateTime.toString() + "\n");
          sbAlter.append("---------------------\n");
        }
      }
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

    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod", new Object[] { billVO });

    if (bIsClose == true) {
      return sbAlter.toString();
    }
    return null;
  }

  public String getBetweenClosedAccPeriod1(GeneralBillVO billVO)
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod1", new Object[] { billVO });

    if (billVO == null) {
      SCMEnv.out("no param..");
      return null;
    }

    StringBuffer sbAlter = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000111") + "\n");
    boolean bIsClose = false;
    GeneralBillHeaderVO voHeader = billVO.getHeaderVO();

    String sLogDateTime = null;

    String sWhPK = null;

    if (voHeader != null) {
      sWhPK = voHeader.getCwarehouseid();

      String sTime = new UFDateTime(System.currentTimeMillis()).getTime().toString();
      sLogDateTime = voHeader.getClogdatenow() + " " + sTime;
    }

    if ((sLogDateTime == null) || (sWhPK == null)) {
      SCMEnv.out("no param..");
      return null;
    }

    StringBuffer sbSql = new StringBuffer("SELECT tstarttime,tendtime FROM ic_accountctrl WHERE dr=0 AND cwarehouseid=? AND faccountflag='N' AND tstarttime<=?  AND tendtime>=?");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      stmt.setString(1, sWhPK);
      stmt.setString(2, sLogDateTime);
      stmt.setString(3, sLogDateTime);
      rs = stmt.executeQuery();
      while (rs.next()) {
        bIsClose = true;
        sbAlter.append(sLogDateTime + "\n");
        UFDateTime ufdStartDateTime = new UFDateTime(rs.getString(1));
        UFDateTime ufdEndDateTime = new UFDateTime(rs.getString(2));
        sbAlter.append(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000112"));
        sbAlter.append("\n");
        sbAlter.append(ufdStartDateTime.toString() + "\n");
        sbAlter.append(ufdEndDateTime.toString() + "\n");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "getBetweenClosedAccPeriod1", new Object[] { billVO });

    if (bIsClose == true) {
      return sbAlter.toString();
    }
    return null;
  }

  public String getCalByWh(String sWhID, String sCorpID)
  {
    String sSql = "SELECT distinct pk_calbody FROM bd_stordoc WHERE (pk_stordoc = ?) AND (pk_corp = ?)";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sReturn = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sWhID);
      stmt.setString(2, sCorpID);

      rs = stmt.executeQuery();
      while (rs.next()) {
        sReturn = rs.getString(1);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return sReturn;
  }

  public ArrayList getWhByCal(String sCalID, String sCorpID)
  {
    ArrayList alReturn = new ArrayList();
    if ((sCalID == null) && (sCorpID == null))
      return null;
    String sSql = "select pk_stordoc from bd_stordoc where dr=0 ";
    String sIn = " select distinct cwarehouseid from ic_accountctrl where dr=0 and cwarehouseid is not null ";
    if (sCalID != null) {
      sSql = sSql + " and pk_calbody='" + sCalID + "' ";
    }
    if (sCorpID != null) {
      sSql = sSql + " and pk_corp='" + sCorpID + "' ";
    }

    sSql = sSql + " and pk_stordoc not in( " + sIn + ")";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      rs = stmt.executeQuery();
      while (rs.next())
        alReturn.add(rs.getString(1));
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return alReturn;
  }

  private ArrayList getData(GeneralBillVO[] billVOs)
  {
    ArrayList alRet = null;
    if ((billVOs == null) || (billVOs.length <= 0)) {
      return null;
    }
    GeneralBillHeaderVO voHeader = null;
    GeneralBillItemVO[] voaItem = null;
    String sWhID = null;
    String sBillCode = null;
    Hashtable htWh = new Hashtable();
    Hashtable htItemDate = null;
    ArrayList alBillCode = null;
    UFDate ufdItemDate = null;
    ArrayList alWh = new ArrayList();

    int iBillCount = billVOs.length;
    int iItemCount = 0;

    for (int i = 0; i < iBillCount; i++) {
      voHeader = (GeneralBillHeaderVO)billVOs[i].getParentVO();
      sBillCode = voHeader.getVbillcode();
      sWhID = voHeader.getCwarehouseid();

      if ((sWhID == null) || (sWhID.length() <= 0))
        continue;
      voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])billVOs[i].getChildrenVO();
      iItemCount = voaItem.length;
      if (!htWh.containsKey(sWhID)) {
        htItemDate = new Hashtable();
        for (int j = 0; j < iItemCount; j++) {
          ufdItemDate = voaItem[j].getDbizdate();
          if (ufdItemDate == null)
            continue;
          if (!htItemDate.containsKey(ufdItemDate)) {
            alBillCode = new ArrayList();
            alBillCode.add(sBillCode);
            htItemDate.put(ufdItemDate, alBillCode);
          }
        }
        htWh.put(sWhID, htItemDate);
        alWh.add(sWhID);
      }
      else {
        htItemDate = (Hashtable)htWh.get(sWhID);
        for (int j = 0; j < iItemCount; j++) {
          ufdItemDate = voaItem[j].getDbizdate();
          if (ufdItemDate == null)
            continue;
          if (!htItemDate.containsKey(ufdItemDate)) {
            alBillCode = new ArrayList();
            alBillCode.add(sBillCode);
            htItemDate.put(ufdItemDate, alBillCode);
          } else {
            alBillCode = (ArrayList)htItemDate.get(ufdItemDate);
            if (!alBillCode.contains(sBillCode)) {
              alBillCode.add(sBillCode);
            }
          }
        }
      }
    }
    if (htWh.size() > 0) {
      alRet = new ArrayList();
      alRet.add(0, htWh);
      alRet.add(1, alWh);
    }
    return alRet;
  }

  public ArrayList getNeedDetailPK(AccountctrlHeaderVO voHead, int iChoose)
    throws SQLException
  {
    ArrayList alRet = new ArrayList();

    String sql1 = "select pk_accountctrl from ic_accountctrl where tstarttime<? and pk_calbody=? and faccountflag='Y'";

    String sql2 = "select pk_accountctrl from ic_accountctrl where tstarttime>? and pk_calbody=? and faccountflag='N'";

    String sql3 = "select pk_accountctrl from ic_accountctrl where tstarttime<? and cwarehouseid=? and faccountflag='Y'";

    String sql4 = "select pk_accountctrl from ic_accountctrl where tstarttime>? and cwarehouseid=? and faccountflag='N'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();

      if ("N".equals(voHead.getFaccountflag().toString())) {
        if (iChoose == 1)
          stmt = con.prepareStatement(sql1);
        if (iChoose == 2) {
          stmt = con.prepareStatement(sql3);
        }
      }
      else if ("Y".equals(voHead.getFaccountflag().toString())) {
        if (iChoose == 1)
          stmt = con.prepareStatement(sql2);
        if (iChoose == 2) {
          stmt = con.prepareStatement(sql4);
        }
      }
      stmt.setString(1, voHead.getTstarttime().toString());
      if (iChoose == 1)
        stmt.setString(2, voHead.getPk_calbody());
      else if (iChoose == 2)
        stmt.setString(2, voHead.getPk_stordoc());
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
        alRet.add(rs.getString(1));
    }
    finally {
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
    return alRet;
  }

  public String getStartTime(String sPK, String sStartTime, int iChoose)
    throws SQLException
  {
    String sRet = null;
    if ((sStartTime == null) || (sStartTime.trim().length() <= 0)) {
      return null;
    }
    String sqlCal = "select min(tstarttime) as tstarttime from ic_accountctrl where pk_calbody=? and tstarttime<=? and faccountflag='Y' and dr=0";
    String sqlWh = "select min(tstarttime) as tstarttime from ic_accountctrl where cwarehouseid=? and tstarttime<=? and faccountflag='Y' and dr=0";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      if (iChoose == 1)
        stmt = con.prepareStatement(sqlCal);
      if (iChoose == 2) {
        stmt = con.prepareStatement(sqlWh);
      }
      stmt.setString(1, sPK);
      stmt.setString(2, sStartTime);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        sRet = rs.getString(1);
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return sRet;
  }

  public boolean isHaveFreeBill(String sPK, UFDateTime ufdStartTime, UFDateTime ufdEndTime, int iChoose)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isHaveFreeBill", new Object[] { sPK, ufdStartTime, ufdEndTime });

    String sStartDate = null;
    String sEndDate = null;
    if (ufdStartTime != null)
      sStartDate = ufdStartTime.getDate().toString();
    if (ufdEndTime != null) {
      sEndDate = ufdEndTime.getDate().toString();
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sSql1 = "select COUNT(*) from ic_general_h h \tINNER  JOIN ic_general_b b ON h.cgeneralhid=b.cgeneralhid where h.pk_calbody=? AND h.dr=0 AND(h.cbilltypecode='40'  or h.cbilltypecode='45' or h.cbilltypecode='46' or h.cbilltypecode='47' or h.cbilltypecode='4A' or h.cbilltypecode='4C' or h.cbilltypecode='4D' or h.cbilltypecode='4E' or h.cbilltypecode='4F' or h.cbilltypecode='4I' or h.cbilltypecode='4O' ) AND b.dr=0 and h.fbillflag=? and b.dbizdate>=? and b.dbizdate<=?";

    String sSql2 = "select COUNT(*) from ic_general_h h \tINNER  JOIN ic_general_b b ON h.cgeneralhid=b.cgeneralhid where h.cwarehouseid=? AND h.dr=0 AND(h.cbilltypecode='40'  or h.cbilltypecode='45' or h.cbilltypecode='46' or h.cbilltypecode='47' or h.cbilltypecode='4A' or h.cbilltypecode='4C' or h.cbilltypecode='4D' or h.cbilltypecode='4E' or h.cbilltypecode='4F' or h.cbilltypecode='4I' or h.cbilltypecode='4O' ) AND b.dr=0 and h.fbillflag=? and b.dbizdate>=? and b.dbizdate<=?";

    int iCount = 0;
    try
    {
      con = getConnection();

      if (iChoose == 1)
        stmt = con.prepareStatement(sSql1);
      else if (iChoose == 2) {
        stmt = con.prepareStatement(sSql2);
      }
      stmt.setString(1, sPK);

      stmt.setString(2, "2");
      stmt.setString(3, sStartDate);
      stmt.setString(4, sEndDate);
      rs = stmt.executeQuery();

      while (rs.next())
      {
        iCount = rs.getInt(1);
        SCMEnv.out("第" + iCount + "条记录");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isHaveFreeBill", new Object[] { sPK, ufdStartTime, ufdEndTime });

    return iCount >= 1;
  }

  public boolean isinitCalbody(String sCalbodyPK)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isinitCalbody", new Object[] { sCalbodyPK });

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sSql = "select COUNT(*) from ic_accountctrl where pk_calbody=? AND dr=0";

    int iCount = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sCalbodyPK);
      rs = stmt.executeQuery();

      while (rs.next())
      {
        iCount = rs.getInt(1);
        SCMEnv.out("第" + iCount + "条记录");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isinitCalbody", new Object[] { sCalbodyPK });

    return iCount >= 1;
  }

  public boolean isinitWarehouse(String sWarehousePK)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isinitWarehouse", new Object[] { sWarehousePK });

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sSql = "select COUNT(*) from ic_accountctrl where cwarehouseid=? AND dr=0";

    int iCount = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sWarehousePK);
      rs = stmt.executeQuery();

      while (rs.next())
      {
        iCount = rs.getInt(1);
        SCMEnv.out("第" + iCount + "条记录");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isinitWarehouse", new Object[] { sWarehousePK });

    return iCount >= 1;
  }

  public boolean isLastRecord(String sAccCtrlID, String sPK, int iChoose)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isLastRecord", new Object[] { sAccCtrlID, sPK });

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sSql1 = "select pk_accountctrl from ic_accountctrl where  dr=0 AND pk_calbody=? AND pk_accountctrl=? AND tstarttime=(SELECT MAX(tstarttime) FROM ic_accountctrl WHERE dr=0 AND pk_calbody=? )";

    String sSql2 = "select pk_accountctrl from ic_accountctrl where  dr=0 AND cwarehouseid=? AND pk_accountctrl=? AND tstarttime=(SELECT MAX(tstarttime) FROM ic_accountctrl WHERE dr=0 AND cwarehouseid=? )";

    boolean bIsLast = false;
    try
    {
      con = getConnection();
      if (iChoose == 1)
        stmt = con.prepareStatement(sSql1);
      else if (iChoose == 2)
        stmt = con.prepareStatement(sSql2);
      stmt.setString(1, sPK);
      stmt.setString(2, sAccCtrlID);
      stmt.setString(3, sPK);
      rs = stmt.executeQuery();

      if (rs.next()) {
        bIsLast = true;
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    finally {
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
      catch (Exception e)
      {
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
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "isLastRecord", new Object[] { sAccCtrlID, sPK });

    return bIsLast;
  }

  public boolean isWhBelongCal(String sWhID, String sCalID)
  {
    String sSql = "SELECT pk_calbody FROM bd_stordoc WHERE (pk_stordoc = ?)";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sCalIDGet = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sWhID);

      rs = stmt.executeQuery();
      while (rs.next())
        sCalIDGet = rs.getString(1);
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    return sCalID.equals(sCalIDGet);
  }

  public AccountctrlVO[] queryAcc(String sSQL)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAcc", new Object[] { sSQL });

    AccountctrlVO[] voaAcc = null;
    AccountctrlVO voAcc = null;
    AccountctrlHeaderVO voHead = null;
    Vector vAcc = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int iCount = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSQL);

      rs = stmt.executeQuery();

      while (rs.next()) {
        voAcc = new AccountctrlVO();
        voHead = new AccountctrlHeaderVO();
        voHead.setPk_accountctrl(rs.getString(1));
        voHead.setPk_calbody(rs.getString(2));
        voHead.setVcalbodyname(rs.getString(3));
        voHead.setTstarttime(new UFDateTime(rs.getString(4)));
        voHead.setTendtime(new UFDateTime(rs.getString(5)));
        voHead.setFaccountflag(new UFBoolean(rs.getString(6)));
        voHead.setCoperatorid(rs.getString(7));
        voHead.setVoperatorname(rs.getString(8));
        voHead.setVremark(rs.getString(9));
        voHead.setTs(new UFDateTime(rs.getString(10)));

        voHead.setPk_stordoc(rs.getString(11));
        voHead.setVstorname(rs.getString(12));

        voAcc.setParentVO(voHead);

        vAcc.addElement(voAcc);

        iCount++;
        SCMEnv.out("第" + iCount + "条记录");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAcc", new Object[] { sSQL });

    if (vAcc.size() > 0) {
      voaAcc = new AccountctrlVO[vAcc.size()];
      vAcc.copyInto(voaAcc);
      return voaAcc;
    }
    return null;
  }

  public AccountctrlVO[] queryAccCal(String sCalbodyPK, String sUserID)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAcc", new Object[] { sCalbodyPK, sUserID });

    AccountctrlVO[] voaAcc = null;
    AccountctrlVO voAcc = null;
    AccountctrlHeaderVO voHead = null;
    Vector vAcc = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int iCount = 0;
    try {
      StringBuffer sSQL = new StringBuffer("SELECT ");

      sSQL.append("acc.pk_accountctrl,acc.pk_calbody,cal.bodyname,acc.tstarttime,");
      sSQL.append("acc.tendtime,acc.faccountflag,acc.coperatorid,us.user_name AS voperatorname,");
      sSQL.append("acc.vremark,acc.ts");

      sSQL.append(" FROM ");
      sSQL.append("ic_accountctrl acc LEFT OUTER JOIN bd_calbody cal ");
      sSQL.append("ON acc.pk_calbody = cal.pk_calbody LEFT OUTER JOIN sm_user us ");
      sSQL.append("ON acc.coperatorid = us.cuserid ");

      sSQL.append(" WHERE ");
      sSQL.append("1=1 AND acc.dr=0 ");
      sSQL.append(" AND acc.pk_calbody=? ORDER BY acc.tstarttime");
      SCMEnv.out(sSQL.toString());
      con = getConnection();
      stmt = con.prepareStatement(sSQL.toString());
      stmt.setString(1, sCalbodyPK);
      rs = stmt.executeQuery();

      while (rs.next()) {
        voAcc = new AccountctrlVO();
        voHead = new AccountctrlHeaderVO();
        voHead.setPk_accountctrl(rs.getString(1));
        voHead.setPk_calbody(rs.getString(2));
        voHead.setVcalbodyname(rs.getString(3));
        voHead.setTstarttime(new UFDateTime(rs.getString(4)));
        voHead.setTendtime(new UFDateTime(rs.getString(5)));
        voHead.setFaccountflag(new UFBoolean(rs.getString(6)));
        voHead.setCoperatorid(rs.getString(7));
        voHead.setVoperatorname(rs.getString(8));
        voHead.setVremark(rs.getString(9));
        voHead.setTs(new UFDateTime(rs.getString(10)));

        voAcc.setParentVO(voHead);

        vAcc.addElement(voAcc);

        iCount++;
        SCMEnv.out("第" + iCount + "条记录");
      }

      if (sCalbodyPK != null)
      {
        String sWH = null;
        Vector v = new Vector();
        stmt = con.prepareStatement(sSQL.toString());
        stmt.setString(1, sCalbodyPK);
        rs = stmt.executeQuery();

        while (rs.next()) {
          sWH = rs.getString(1);
          v.add(sWH);
        }
      }
    } catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAcc", new Object[] { sCalbodyPK, sUserID });

    if (vAcc.size() > 0) {
      voaAcc = new AccountctrlVO[vAcc.size()];
      vAcc.copyInto(voaAcc);
      return voaAcc;
    }
    return null;
  }

  public AccountctrlVO[] queryAccWh(String sWarehousePK, String sUserID)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAccWh", new Object[] { sWarehousePK, sUserID });

    AccountctrlVO[] voaAcc = null;
    AccountctrlVO voAcc = null;
    AccountctrlHeaderVO voHead = null;
    Vector vAcc = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int iCount = 0;
    try {
      StringBuffer sSQL = new StringBuffer("SELECT ");

      sSQL.append("acc.pk_accountctrl,acc.cwarehouseid,wh.storname,acc.tstarttime,");
      sSQL.append("acc.tendtime,acc.faccountflag,acc.coperatorid,us.user_name AS voperatorname,");
      sSQL.append("acc.vremark,acc.ts");

      sSQL.append(" FROM ");
      sSQL.append("ic_accountctrl acc LEFT OUTER JOIN bd_stordoc wh ");
      sSQL.append("ON acc.cwarehouseid = wh.pk_stordoc LEFT OUTER JOIN sm_user us ");
      sSQL.append("ON acc.coperatorid = us.cuserid ");

      sSQL.append(" WHERE ");
      sSQL.append("1=1 AND acc.dr=0 ");
      sSQL.append(" AND acc.cwarehouseid=? ORDER BY acc.tstarttime");
      SCMEnv.out(sSQL.toString());
      con = getConnection();
      stmt = con.prepareStatement(sSQL.toString());
      stmt.setString(1, sWarehousePK);
      rs = stmt.executeQuery();

      while (rs.next()) {
        voAcc = new AccountctrlVO();
        voHead = new AccountctrlHeaderVO();
        voHead.setPk_accountctrl(rs.getString(1));
        voHead.setPk_stordoc(rs.getString(2));
        voHead.setVstorname(rs.getString(3));
        voHead.setTstarttime(new UFDateTime(rs.getString(4)));
        voHead.setTendtime(new UFDateTime(rs.getString(5)));
        voHead.setFaccountflag(new UFBoolean(rs.getString(6)));
        voHead.setCoperatorid(rs.getString(7));
        voHead.setVoperatorname(rs.getString(8));
        voHead.setVremark(rs.getString(9));
        voHead.setTs(new UFDateTime(rs.getString(10)));

        voAcc.setParentVO(voHead);

        vAcc.addElement(voAcc);

        iCount++;
        SCMEnv.out("第" + iCount + "条记录");
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    } finally {
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.ic2a3.AccountctrlDMO", "queryAcc", new Object[] { sWarehousePK, sUserID });

    if (vAcc.size() > 0) {
      voaAcc = new AccountctrlVO[vAcc.size()];
      vAcc.copyInto(voaAcc);
      return voaAcc;
    }
    return null;
  }
}