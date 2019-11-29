package nc.bs.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.jdbc.framework.ConnectionFactory;
import nc.jdbc.framework.DataSourceCenter;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.vo.pub.lang.UFDouble;

public class DataManageObject
{
  private String a;
  private HashSet b = null;

  private int c = 0;

  private String d = null;

  public DataManageObject()
    throws NamingException
  {
    this.a = null;
    this.d = InvocationInfoProxy.getInstance().getCorpCode();
    if (a(this.d))
      this.d = "0001";
  }

  public DataManageObject(String paramString)
    throws NamingException
  {
    this.a = paramString;

    this.d = InvocationInfoProxy.getInstance().getCorpCode();

    if (a(this.d))
      this.d = "0001";
  }

  protected final void afterCallMethod(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
  }

  protected final void beforeCallMethod(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
  }

  /** @deprecated */
  protected Vector executeQuery(String paramString)
    throws SQLException
  {
    Connection localConnection = getConnection();
    Vector localVector1 = new Vector();
    Statement localStatement = null;
    ResultSet localResultSet = null;
    try
    {
      ResultSetMetaData localResultSetMetaData;
      int i;
      int[] arrayOfInt = new int[(
        i = (
        localResultSetMetaData = (
        localResultSet = (
        localStatement = localConnection.createStatement())
        .executeQuery(paramString))
        .getMetaData())
        .getColumnCount()) + 
        1];

      for (int j = 1; j <= i; j++) {
        arrayOfInt[j] = localResultSetMetaData.getColumnType(j);
      }
      while (localResultSet.next()) {
        Vector localVector2 = new Vector();
        for (int k = 1; k <= i; k++) {
          Object localObject1 = localResultSet.getObject(k);
          if ((arrayOfInt[k] == 1) && (localObject1 != null))
            localObject1 = localObject1.toString().trim();
          localVector2.addElement(localObject1);
        }
        localVector1.addElement(localVector2);
      }
    } finally {
      if (localResultSet != null) {
        localResultSet.close();
      }
      if (localStatement != null) {
        localStatement.close();
      }
    }

    return (Vector)localVector1;
  }

  /** @deprecated */
  protected int executeUpdate(String paramString)
    throws SQLException
  {
    int i = 0;

    Connection localConnection = null;
    Statement localStatement = null;
    try
    {
      i = (
        localStatement = (
        localConnection = getConnection())
        .createStatement())
        .executeUpdate(paramString);
    } finally {
      try {
        if (localStatement != null)
          localStatement.close();
      }
      catch (Exception localException3) {
      }
      try {
        if (localConnection != null)
          localConnection.close();
      }
      catch (Exception localException4) {
      }
    }
    return i;
  }

  protected Connection getConnection()
    throws SQLException
  {
    return ConnectionFactory.getConnection(getDataSourceName());
  }

  private String getDataSourceName()
  {
    if (this.a != null)
      return this.a;
    String str;
    if (a(str = InvocationInfoProxy.getInstance().getUserDataSource()))
    {
      str = InvocationInfoProxy.getInstance().getDefaultDataSource();
    }if (a(str))
    {
      str = "design";
    }
    return str;
  }

  protected String getOID()
  {
    if (a(this.d)) {
      this.d = "0001";
    }
    return getOID(this.d);
  }

  protected String getOID(String paramString)
  {
    String str = null;
    try {
      if (a(paramString))
        paramString = "0001";
      str = new SequenceGenerator(getDataSourceName()).generate(paramString);
    }
    catch (Exception localException)
    {
    }
    return str;
  }

  protected String[] getOIDs(int paramInt)
  {
    if ((this.d == null) || (this.d.trim().length() <= 0)) {
      this.d = "0001";
    }
    return getOIDs(this.d, paramInt);
  }

  protected String[] getOIDs(String paramString, int paramInt) {
    String[] arrayOfString = null;
    try {
      arrayOfString = new SequenceGenerator(getDataSourceName()).generate(paramString, paramInt);
    }
    catch (Exception localException)
    {
    }
    return arrayOfString;
  }

  protected int[] executeBatch(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    boolean bool = getBatchPsSet().contains(paramPreparedStatement);

    int[] arrayOfInt = null;
    if (bool) {
      arrayOfInt = paramPreparedStatement.executeBatch();
    }
    try
    {
      paramPreparedStatement.close();
      getBatchPsSet().remove(paramPreparedStatement);
    }
    catch (Exception localException) {
    }
    return arrayOfInt;
  }

  protected int executeUpdate(PreparedStatement paramPreparedStatement)
    throws SQLException
  {
    boolean bool;
    if ((
      bool = getBatchPsSet().contains(paramPreparedStatement)))
    {
      paramPreparedStatement.addBatch();
      this.c += 1;

      if ((this.c != 0) && (this.c % 2000 == 0) && 
        (getBatchPsSet().contains(paramPreparedStatement))) {
        paramPreparedStatement.executeBatch();
      }

      return -1;
    }
    return paramPreparedStatement.executeUpdate();
  }

  private HashSet getBatchPsSet()
  {
    if (this.b == null) {
      this.b = new HashSet();
    }
    return this.b;
  }

  protected PreparedStatement prepareStatement(Connection paramConnection, String paramString)
    throws SQLException
  {
    return prepareStatement(paramConnection, paramString, true);
  }

  protected PreparedStatement prepareStatement(Connection paramConnection, String paramString, boolean paramBoolean)
    throws SQLException
  {
    PreparedStatement localPreparedStatement = paramConnection.prepareStatement(paramString);
    if (paramBoolean)
    {
      getBatchPsSet().add(localPreparedStatement);
    }
    return localPreparedStatement;
  }

  protected PreparedStatement prepareStatementWithSQLTransDisabled(Connection paramConnection, String paramString)
    throws SQLException
  {
    throw new SQLException("not supported this operation");
  }

  protected void reportException(Exception paramException)
  {
  }

  public static UFDouble getUFDouble(ResultSet paramResultSet, int paramInt1, int paramInt2)
    throws SQLException
  {
    double d1 = paramResultSet.getDouble(paramInt1);
    if (paramResultSet.wasNull())
      return null;
    return new UFDouble(d1, -paramInt2);
  }

  public static UFDouble getUFDouble(ResultSet paramResultSet, String paramString, int paramInt)
    throws SQLException
  {
    double d1 = paramResultSet.getDouble(paramString);
    if (paramResultSet.wasNull())
      return null;
    return new UFDouble(d1, -paramInt);
  }

  public int getDatabaseType() {
    return DataSourceCenter.getInstance().getDatabaseType(getDataSourceName());
  }

  private static boolean a(String paramString) {
    return (paramString == null) || ("".equals(paramString.trim())) || ("null".equals(paramString.trim()));
  }
}