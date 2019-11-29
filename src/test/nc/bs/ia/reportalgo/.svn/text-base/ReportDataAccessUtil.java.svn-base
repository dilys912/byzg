package nc.bs.ia.reportalgo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.pub.lang.UFDouble;

public class ReportDataAccessUtil extends DataManageObject
{
	   public ReportDataAccessUtil()
       throws NamingException
   {
   }
  public InvInOutSumVO[] getInOutSumVO(String sSql)
    throws Exception
  {
    InvInOutSumVO[] vosResult = null;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      System.out.println("Ö´ÐÐÓï¾äÎª:"+sSql);
      rs = stmt.executeQuery();
      ResultSetMetaData meta = rs.getMetaData();
      int iLen = meta.getColumnCount();
      InvInOutSumVO voTemp = null;
      Object oValue = null;
      UFDouble ufdValue = null;

      Vector vResult = new Vector();
      while (rs.next()) {
        voTemp = new InvInOutSumVO();
        for (int i = 1; i <= iLen; ++i) {
          String sName = meta.getColumnName(i).trim().toLowerCase();
          oValue = rs.getObject(i);
          if (sName.startsWith("n"))
          {
            if (oValue != null) {
              ufdValue = new UFDouble(oValue.toString());
              voTemp.setAttributeValue(sName, ufdValue);
            }
          }
          else {
            voTemp.setAttributeValue(sName, oValue);
          }
        }

        UFDouble noutnum = voTemp.getNoutnum();
        UFDouble noutmny = voTemp.getNoutmny();

        if ((noutnum != null) && (noutnum.getDouble() != 0.0D) && (noutmny != null)) {
          voTemp.setNoutprice(noutmny.div(noutnum));
        }

        vResult.addElement(voTemp);
      }
      vosResult = new InvInOutSumVO[vResult.size()];
      vResult.copyInto(vosResult);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
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
    return vosResult;
  }
}