package nc.impl.scm.so.so003;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.pub.lang.UFDate;
import nc.vo.so.so003.AlarmVO;

public class AlarmDMO extends DataManageObject
{
  public AlarmDMO()
    throws NamingException, SystemException
  {
  }

  public AlarmDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public AlarmVO[] query(String pk_corp)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so003.AlarmDMO", "query", new Object[] { pk_corp });

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT so_sale.vreceiptcode, custname, invcode,invname, so_saleorder_b.dconsigndate ");
    sql.append("FROM so_sale ");
    sql.append("LEFT OUTER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid ");
    sql.append("LEFT OUTER JOIN bd_cumandoc ON so_sale.ccustomerid = bd_cumandoc.pk_cumandoc ");
    sql.append("LEFT OUTER JOIN bd_cubasdoc ON bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");
    sql.append("LEFT OUTER JOIN bd_sendtype ON so_sale.ctransmodeid = bd_sendtype.pk_sendtype ");
    sql.append("LEFT OUTER JOIN bd_invmandoc ON so_saleorder_b.cinventoryid = bd_invmandoc.pk_invmandoc ");
    sql.append("LEFT OUTER JOIN bd_invbasdoc ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
    sql.append("LEFT OUTER JOIN so_saleexecute ON so_saleorder_b.corder_bid = so_saleexecute.csale_bid ");
    sql.append("WHERE so_saleorder_b.beditflag = 'N' AND so_sale.fstatus = 2 AND so_sale.pk_corp = ? ");
    sql.append("AND bd_sendtype.issendarranged = 'Y' AND ISNULL(ntotalreceiptnumber,0) < nnumber AND so_saleorder_b.dconsigndate <= ?");

    AlarmVO[] sales = null;
    AlarmVO sale = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      UFDate date = new UFDate(System.currentTimeMillis());
      stmt.setString(2, date.toString());
      ResultSet rs = stmt.executeQuery();

      String value = null;
      BigDecimal data = null;
      Object oInt = null;
      while (rs.next()) {
        sale = new AlarmVO();

        value = rs.getString(1);
        sale.setAttributeValue("vreceiptcode", value == null ? null : value.trim());

        value = rs.getString(2);
        sale.setAttributeValue("customer", value == null ? null : value.trim());

        value = rs.getString(3);
        sale.setAttributeValue("invcode", value == null ? null : value.trim());

        value = rs.getString(4);
        sale.setAttributeValue("invname", value == null ? null : value.trim());

        value = rs.getString(5);
        sale.setAttributeValue("dconsigndate", value == null ? null : new UFDate(value));

        v.addElement(sale);
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
    if (v.size() > 0) {
      sales = new AlarmVO[v.size()];
      v.copyInto(sales);
    }

    afterCallMethod("nc.bs.so.so003.AlarmDMO", "query", new Object[] { pk_corp });

    return sales;
  }
}