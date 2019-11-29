package nc.bs.scm.pub;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;

public class ScmPubDMO extends DataManageObject
{
  public static final String DEPTDOC = "00010000000000000002";
  public static final String AREACL = "00010000000000000012";

  public ScmPubDMO()
    throws NamingException, SystemException
  {
  }

  public ScmPubDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public static void throwRemoteException(Exception e)
    throws RemoteException
  {
    if (e instanceof RemoteException) {
      throw ((RemoteException)e);
    }
    throw new RemoteException("Remote Call", e);
  }

  public static void throwBusinessException(Exception e)
    throws BusinessException
  {
    if (e instanceof BusinessException) {
      throw ((BusinessException)e);
    }
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }

  private static ArrayList getTotalSubPkAndNames(String sPkBdInfo, String sCurPk, String sCurName, String[] saPk_corp)
    throws Exception
  {
    ArrayList resultArray = new ArrayList();

    if (sPkBdInfo == null) {
      return null;
    }

    int iPk_corpCount = 1;
    if (saPk_corp != null) {
      iPk_corpCount = saPk_corp.length;
    }

    Vector vecPk = new Vector(10);
    vecPk.addElement(sCurPk);

    Vector vecName = new Vector(10);
    vecName.addElement(sCurName);

    for (int i = 0; i < iPk_corpCount; ++i) {
      IBDAccessor ibdaAcc = null;
      if (saPk_corp == null)
        ibdaAcc = AccessorManager.getAccessor(sPkBdInfo);
      else {
        ibdaAcc = AccessorManager.getAccessor(sPkBdInfo, saPk_corp[i]);
      }

      BddataVO[] voaBdData = ibdaAcc.getChildDocs(sCurPk);
      if (voaBdData != null) {
        int iRetLen = voaBdData.length;
        for (int j = 0; j < iRetLen; ++j) {
          vecPk.addElement(voaBdData[j].getPk());
          vecName.addElement(voaBdData[j].getName());
        }
      }
    }

    int iSize = vecPk.size();
    resultArray.add((String[])(String[])vecPk.toArray(new String[iSize]));
    resultArray.add((String[])(String[])vecName.toArray(new String[iSize]));
    return resultArray;
  }

  public static String[] getTotalSubPks(String sPkBdInfo, String sCurPk, String[] saPk_corp)
    throws Exception
  {
    if (sPkBdInfo == null) {
      return null;
    }

    int iPk_corpCount = 1;
    if (saPk_corp != null) {
      iPk_corpCount = saPk_corp.length;
    }

    Vector vecPk = new Vector(10);
    vecPk.addElement(sCurPk);

    for (int i = 0; i < iPk_corpCount; ++i) {
      IBDAccessor ibdaAcc = null;
      if (saPk_corp == null)
        ibdaAcc = AccessorManager.getAccessor(sPkBdInfo);
      else {
        ibdaAcc = AccessorManager.getAccessor(sPkBdInfo, saPk_corp[i]);
      }

      BddataVO[] voaBdData = ibdaAcc.getChildDocs(sCurPk);
      if (voaBdData != null) {
        int iRetLen = voaBdData.length;
        for (int j = 0; j < iRetLen; ++j) {
          vecPk.addElement(voaBdData[j].getPk());
        }
      }
    }

    int iSize = vecPk.size();
    return ((String[])(String[])vecPk.toArray(new String[iSize]));
  }

  public HashMap loadBatchInvConvRateInfo(String[] saBaseId, String[] saAssistUnit)
    throws Exception
  {
    HashMap hInvConvRate = new HashMap();

    if ((saBaseId == null) || (saAssistUnit == null) || (saBaseId.length == 0)) {
      return hInvConvRate;
    }

    int iLen = saBaseId.length;

    ArrayList listTempTableValue = new ArrayList();
    for (int i = 0; i < iLen; ++i) {
      if (PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null) continue; if (PuPubVO.getString_TrimZeroLenAsNull(saAssistUnit[i]) == null) {
        continue;
      }

      ArrayList listElement = new ArrayList();
      listElement.add(saBaseId[i]);
      listElement.add(saAssistUnit[i]);

      listTempTableValue.add(listElement);
    }

    try
    {
      String sTempTableName = new TempTableDMO().getTempStringTable("t_pu_clms_01", new String[] { "cbaseid", "cassistunit" }, new String[] { "char(20) not null ", "char(20) not null " }, null, listTempTableValue);

      Object[][] ob = (Object[][])null;
      ob = queryResultsFromAnyTable(sTempTableName + " JOIN bd_convert ON " + sTempTableName + ".cbaseid=bd_convert.pk_invbasdoc" + " AND pk_measdoc=" + sTempTableName + ".cassistunit", new String[] { "pk_invbasdoc", "pk_measdoc", "mainmeasrate", "fixedflag" }, null);

      if (ob != null)
      {
        iLen = ob.length;
        for (int i = 0; i < iLen; ++i) {
          String sTempKey = ((String)ob[i][0]) + ((String)ob[i][1]);
          hInvConvRate.put(sTempKey, new Object[] { new UFDouble(ob[i][2].toString()), new UFBoolean((ob[i][3] == null) ? "N" : ob[i][3].toString()) });
        }
      }
    }
    catch (Exception e)
    {
      reportException(e);
      throw e;
    }
    return hInvConvRate;
  }

  public Object[][] queryResultsFromAnyTable(String sTable, String[] saFields, String sWhere)
    throws SQLException
  {
    if ((sTable == null) || (sTable.trim().length() < 1) || (saFields == null) || (saFields.length < 1))
    {
      System.out.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)传入参数错误！");

      return ((Object[][])null);
    }

    int iLen = saFields.length;
    for (int i = 0; i < iLen; ++i) {
      if ((saFields[i] == null) || (saFields[i].trim().length() < 1)) {
        System.out.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)传入参数错误！");

        return ((Object[][])null);
      }
    }

    StringBuffer sbufSql = new StringBuffer("SELECT DISTINCT ");
    sbufSql.append(saFields[0]);
    for (int i = 1; i < saFields.length; ++i) {
      sbufSql.append(",");
      sbufSql.append(saFields[i]);
    }
    sbufSql.append(" FROM ");
    sbufSql.append(sTable);
    if (PuPubVO.getString_TrimZeroLenAsNull(sWhere) != null) {
      sbufSql.append(" WHERE ");
      sbufSql.append(sWhere);
    }

    Object[][] rets = (Object[][])null;
    Vector vec = new Vector();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      boolean flag = false;
      Object o = null;
      while (rs.next()) {
        Object[] ob = new Object[saFields.length];
        for (int i = 0; i < saFields.length; ++i) {
          o = rs.getObject(i + 1);
          if ((o != null) && (o.toString().trim().length() > 0)) {
            ob[i] = o;
            flag = true;
          }
        }
        if (flag) {
          vec.addElement(ob);
        }
        flag = false;
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (vec.size() > 0) {
      rets = new Object[vec.size()][];
      for (int i = 0; i < vec.size(); ++i) {
        rets[i] = ((Object[])(Object[])vec.elementAt(i));
      }
    }

    return rets;
  }

  public Object[][] queryArrayValue(String sTable, String sIdName, String[] saFields, String[] saId, String sWhere)
    throws Exception
  {
    StringBuffer sbufSql = new StringBuffer("select ");
    sbufSql.append(sIdName);
    sbufSql.append(",");
    sbufSql.append(saFields[0]);
    int iLen = saFields.length;
    for (int i = 1; i < iLen; ++i) {
      sbufSql.append(",");
      sbufSql.append(saFields[i]);
    }
    sbufSql.append(" from ");
    sbufSql.append(sTable);
    sbufSql.append(" where ");
    sbufSql.append(sIdName + " in ");
    String strIdSet = "";
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      strIdSet = dmoTmpTbl.insertTempTable(saId, "t_pu_ask_03", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0))
        strIdSet = "('TempTableDMOError')";
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    sbufSql.append(strIdSet + " ");

    if ((sWhere != null) && (sWhere.trim().length() > 1)) {
      sbufSql.append("and ");
      sbufSql.append(sWhere);
    }

    Hashtable hashRet = new Hashtable();
    Vector vec = new Vector();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      while (rs.next()) {
        String sId = rs.getString(1);
        Object[] ob = new Object[saFields.length];
        for (int i = 1; i < saFields.length + 1; ++i) {
          ob[(i - 1)] = rs.getObject(i + 1);
        }
        hashRet.put(sId, ob);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    Object[][] oRet = (Object[][])null;
    if (hashRet.size() > 0) {
      iLen = saId.length;
      oRet = new Object[iLen][saFields.length];
      for (int i = 0; i < iLen; ++i) {
        oRet[i] = ((Object[])(Object[])hashRet.get(saId[i]));
      }
    }

    return oRet;
  }

  public ConditionVO[] getTotalSubPkVO(ConditionVO[] condvo, String[] saPk_corp)
    throws Exception
  {
    int lenold = condvo.length;
    if (lenold <= 0) {
      return condvo;
    }

    String sCurPk = null;
    boolean flag = false;
    ArrayList rowdept = new ArrayList();

    for (int i = 0; i < lenold; ++i) {
      if ((condvo[i].getFieldName() == null) || ((!(condvo[i].getFieldName().equals("部门"))) && (!(condvo[i].getFieldName().equals("部门档案")))) || (!("=".equals(condvo[i].getOperaCode())))) {
        continue;
      }

      sCurPk = condvo[i].getValue();
      if ((sCurPk != null) && (sCurPk.length() > 0)) {
        Integer newint = new Integer(i);
        rowdept.add(newint);
        flag = true;
      }

    }

    if (!(flag)) {
      return condvo;
    }

    ArrayList arAll = new ArrayList();
    int rowindex;
    for (int j = 0; j < rowdept.size(); ++j)
    {
      ArrayList arPer = new ArrayList();

      rowindex = ((Integer)rowdept.get(j)).intValue();
      sCurPk = condvo[rowindex].getValue();
      String sCurName;
      if (condvo[rowindex].getRefResult() != null)
        sCurName = condvo[rowindex].getRefResult().getRefName();
      else
        sCurName = NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000054"); String[] sRetName = null;
      String[] sRetPk;
      try {
        ArrayList retArray = getTotalSubPkAndNames("00010000000000000002", sCurPk, sCurName, saPk_corp);

        sRetPk = (String[])(String[])retArray.get(0);
        sRetName = (String[])(String[])retArray.get(1);
      } catch (Exception e) {
        e.printStackTrace();

        sRetPk = null;
      }

      if ((sRetPk == null) || (sRetPk.length <= 1)) {
        arAll.add(null);
      }
      else
      {
        ConditionVO vo;
        for (int i = 0; i < sRetPk.length; ++i) {
          vo = new ConditionVO();
          vo.setDataType(condvo[rowindex].getDataType());
          vo.setDirty(condvo[rowindex].isDirty());
          vo.setFieldCode(condvo[rowindex].getFieldCode());
          vo.setFieldName(condvo[rowindex].getFieldName());
          vo.setOperaCode(condvo[rowindex].getOperaCode());
          vo.setOperaName(condvo[rowindex].getOperaName());
          vo.setTableCode(condvo[rowindex].getTableCodeForMultiTable());
          vo.setTableName(condvo[rowindex].getTableName());
          vo.setValue(sRetPk[i]);

          RefResultVO refresultvo = new RefResultVO();
          refresultvo.setRefPK(sRetPk[i]);
          refresultvo.setRefName(sRetName[i]);
          vo.setRefResult(refresultvo);

          vo.setLogic(false);

          if (i == 0) {
            vo.setNoLeft(false);

            if (!(condvo[rowindex].getNoLeft()))
              vo.setLogic(true);
            else
              vo.setLogic(condvo[rowindex].getLogic());
          }
          else if (i == sRetPk.length - 1) {
            vo.setNoRight(false);
          }
          arPer.add(vo);
        }

        if (!(condvo[rowindex].getNoLeft())) {
          vo = new ConditionVO();

          vo.setDataType(1);
          vo.setFieldCode("1");
          vo.setOperaCode("=");
          vo.setValue("1");
          vo.setLogic(condvo[rowindex].getLogic());
          vo.setNoLeft(false);
          arPer.add(0, vo);
        }

        if (!(condvo[rowindex].getNoRight())) {
          vo = new ConditionVO();

          vo.setDataType(1);
          vo.setFieldCode("1");
          vo.setOperaCode("=");
          vo.setValue("1");
          vo.setLogic(true);
          vo.setNoRight(false);
          arPer.add(vo);
        }

        arAll.add(arPer);
      }
    }

    ArrayList arRes = new ArrayList();

    for (int i = 0; i < lenold; ++i) {
      arRes.add(condvo[i]);
    }

    for (int j = rowdept.size() - 1; j >= 0; --j) {
      rowindex = ((Integer)rowdept.get(j)).intValue();
      ArrayList arPer = (ArrayList)arAll.get(j);
      if (arPer == null) {
        continue;
      }
      arRes.remove(rowindex);
      int k;
      if (rowindex >= arRes.size())
      {
        for (k = 0; k < arPer.size(); ++k) {
          arRes.add(arPer.get(k));
        }
      }
      else {
        for (k = arPer.size() - 1; k >= 0; --k) {
          arRes.add(rowindex, arPer.get(k));
        }
      }
    }

    int iSize = arRes.size();
    return ((ConditionVO[])(ConditionVO[])arRes.toArray(new ConditionVO[iSize]));
  }
}