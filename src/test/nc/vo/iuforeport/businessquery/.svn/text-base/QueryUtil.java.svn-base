package nc.vo.iuforeport.businessquery;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.StorageDataSet;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.queryengine.IDataStructureInfo;
import nc.itf.uap.queryengine.IQEDataSourceInfo;
import nc.itf.uap.queryengine.ITempTableHandler;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.CorpVO;
import nc.vo.com.utils.SystemProerty;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValueObject;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.ChildObject;
import nc.vo.pub.core.ChildObjectList;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.FieldDefList;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.ddc.datadict.ViewDef;
import nc.vo.pub.ddc.datadict.ViewFld;
import nc.vo.pub.ddc.datadict.ViewFldList;
import nc.vo.pub.ddc.userdefine.IUserTableDefCreator;
import nc.vo.pub.ddc.userdefine.IUserTableName;
import nc.vo.pub.ddc.userdefine.UserTbDefInfo;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.querymodel.DataDictForNode;
import nc.vo.pub.querymodel.DatasetUtil;
import nc.vo.pub.querymodel.EnvInfo;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.PackageLoader;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.QueryModelTree;
import nc.vo.pub.querymodel.SqlParseMain;
import net.sf.jsqlparser.statement.select.Select;

public class QueryUtil
{
  private static HashMap db2TypeMap = new HashMap();

  private static IUserTableDefCreator[] tableDefCreator = null;

  public static void autoAddGroup(QueryBaseDef qbd)
  {
    SelectFldVO[] sfs = qbd.getSelectFlds();
    int iLen = sfs == null ? 0 : sfs.length;
    if (iLen != 0) {
      boolean[] bAggs = new boolean[iLen];
      boolean bFlag = false;
      for (int i = 0; i < iLen; i++) {
        bAggs[i] = withAggFunc(sfs[i].getExpression());
        if (bAggs[i]) {
          bFlag = true;
        }
      }
      if (bFlag)
      {
        Vector vecGroup = new Vector();
        for (int i = 0; i < iLen; i++) {
          if (!(bAggs[i]) && (!isConst(sfs[i].getExpression()))) {
            vecGroup.addElement(sfs[i]);
          }
        }
        int iSize = vecGroup.size();
        if (iSize != 0) {
          GroupbyFldVO[] gfs = new GroupbyFldVO[iSize];
          for (int i = 0; i < iSize; i++) {
            SelectFldVO sf = (SelectFldVO)vecGroup.elementAt(i);
            gfs[i] = new GroupbyFldVO();
            gfs[i].setExpression(sf.getExpression());
            gfs[i].setFldname(sf.getFldname());
            gfs[i].setFldalias(sf.getFldalias());
          }
          qbd.setGroupbyFlds(gfs);
        } else {
          qbd.setGroupbyFlds(null);
        }
      } else {
        qbd.setGroupbyFlds(null);
      }
    }
  }

  public static void autoAddGroup(QueryBaseVO qb)
  {
    SelectFldVO[] sfs = qb.getSelectFlds();
    int iLen = sfs == null ? 0 : sfs.length;
    if (iLen != 0) {
      boolean[] bAggs = new boolean[iLen];
      boolean bFlag = false;
      for (int i = 0; i < iLen; i++) {
        bAggs[i] = withAggFunc(sfs[i].getExpression());
        if (bAggs[i]) {
          bFlag = true;
        }
      }
      if (bFlag)
      {
        Vector vecGroup = new Vector();
        for (int i = 0; i < iLen; i++) {
          if (!(bAggs[i]) && (!isConst(sfs[i].getExpression()))) {
            vecGroup.addElement(sfs[i]);
          }
        }
        int iSize = vecGroup.size();
        if (iSize != 0) {
          GroupbyFldVO[] gfs = new GroupbyFldVO[iSize];
          for (int i = 0; i < iSize; i++) {
            SelectFldVO sf = (SelectFldVO)vecGroup.elementAt(i);
            gfs[i] = new GroupbyFldVO();
            gfs[i].setExpression(sf.getExpression());
            gfs[i].setFldname(sf.getFldname());
            gfs[i].setFldalias(sf.getFldalias());
          }
          qb.setGroupbyFlds(gfs);
        } else {
          qb.setGroupbyFlds(null);
        }
      } else {
        qb.setGroupbyFlds(null);
      }
    }
  }

  public static String getSqlByBaseDef(QueryBaseDef qbd, Hashtable hashParam)
  {
    String handSql = qbd.getHandSql();
    if ((handSql != null) && (!handSql.trim().equals(""))) {
      handSql = replaceHandSql(handSql, hashParam);
      return handSql;
    }

    StringBuffer sb = new StringBuffer("");

    if (qbd != null)
    {
      SelectFldVO[] sfs = qbd.getSelectFlds();
      int iLen = sfs == null ? 0 : sfs.length;

      sb.append("select ");

      String strBit = qbd.getTempletId();
      if (strBit != null) {
        strBit = strBit.trim();
        if ((strBit.startsWith("top")) || (strBit.startsWith("distinct"))) {
          sb.append(strBit + " ");
        }

      }

      for (int i = 0; i < iLen; i++) {
        sb.append(sfs[i].getExpression());
        if ((sfs[i].getExpression() != null) && (sfs[i].getExpression().toLowerCase().indexOf(" as ") == -1))
        {
          sb.append(" as ");
          sb.append(sfs[i].getFldalias());
        }
        if (i < iLen - 1) {
          sb.append(", ");
        }
      }

      JoinCondVO[] jcs = qbd.getJoinConds();
      iLen = jcs == null ? 0 : jcs.length;
      sb.append(" from ");
      if (iLen == 0) {
        FromTableVO[] fts = qbd.getFromTables();
        iLen = fts == null ? 0 : fts.length;
        for (int i = 0; i < iLen; i++) {
          sb.append(fts[i].getTablecode());
          if (fts[i].getTablealias() != null) {
            sb.append(" as ");
            sb.append(fts[i].getTablealias());
          }
          if (i < iLen - 1)
            sb.append(", ");
        }
      }
      else {
        for (int i = 0; i < iLen; i++) {
          if ((i == 0) && 
            (jcs[i].getLefttable() != null))
          {
            sb.append(jcs[i].getLefttable());
          }

          sb.append(" " + jcs[i].getRealType() + " ");
          sb.append(jcs[i].getRighttable());
          sb.append(" on ");
          sb.append(jcs[i].getExpression());
        }
      }
    }

    WhereCondVO[] wcs = qbd.getWhereConds();
    int iLen = wcs == null ? 0 : wcs.length;
    if (iLen != 0) {
      sb.append(" where ");
      StringBuffer whereStrBuf = new StringBuffer();
      for (int i = 0; i < iLen; i++) {
        String wcexp = wcs[i].getExpression(hashParam);

        if ((!wcexp.trim().startsWith("(")) && (!wcexp.trim().endsWith(")")))
        {
          wcexp = "(" + wcexp + ")";
        }
        if (i == 0) {
          whereStrBuf.append(wcexp);
        } else {
          String relation = wcs[i].getRelationflag() == null ? "" : wcs[i].getRelationflag();

          if (relation.equals("or")) {
            whereStrBuf.append(" or ");
            whereStrBuf.append(wcexp);
          } else if (relation.equals("_and"))
          {
            sb.append("(");
            whereStrBuf.append(") and (");
            whereStrBuf.append(wcexp);
            whereStrBuf.append(") ");
          } else if (relation.equals("_or"))
          {
            sb.append("(");
            whereStrBuf.append(") or (");
            whereStrBuf.append(wcexp);
            whereStrBuf.append(") ");
          }
          else {
            whereStrBuf.append(" and ");
            whereStrBuf.append(wcexp);
          }
        }
      }
      sb.append(whereStrBuf.toString());
    }

    GroupbyFldVO[] gfs = qbd.getGroupbyFlds();
    iLen = gfs == null ? 0 : gfs.length;
    if (iLen != 0) {
      sb.append(" group by ");
      for (int i = 0; i < iLen; i++) {
        sb.append(gfs[i].getExpression());
        if (i < iLen - 1)
          sb.append(", ");
      }
    }
    else
    {
      try {
        autoAddGroup(qbd);

        gfs = qbd.getGroupbyFlds();
        iLen = gfs == null ? 0 : gfs.length;
        if (iLen != 0) {
          sb.append(" group by ");
          for (int i = 0; i < iLen; i++) {
            sb.append(gfs[i].getExpression());
            if (i < iLen - 1)
              sb.append(", ");
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    HavingCondVO[] hcs = qbd.getHavingConds();
    iLen = hcs == null ? 0 : hcs.length;
    if (iLen != 0) {
      sb.append(" having ");
      for (int i = 0; i < iLen; i++) {
        if (i != 0) {
          sb.append(" " + hcs[i].getRelationflag() + " ");
        }
        sb.append(hcs[i].getExpression(null));
      }
    }

    OrderbyFldVO[] ofs = qbd.getOrderbyFlds();
    iLen = ofs == null ? 0 : ofs.length;
    if (iLen != 0) {
      sb.append(" order by ");
      for (int i = 0; i < iLen; i++) {
        sb.append(ofs[i].getFldalias());
        String isAsc = (ofs[i].getAsc() != null) && (!ofs[i].getAsc().booleanValue()) ? " desc " : " asc ";

        sb.append(isAsc);
        if (i < iLen - 1) {
          sb.append(", ");
        }
      }
    }
    String sql = sb.toString();
    sql = replaceHandSql(sql, hashParam);
    return sql;
  }

  public static int containsGroupby(String strSql)
  {
    String patternStr = "\\s+group\\s+by\\s+";
    Pattern pattern = Pattern.compile(patternStr, 66);

    Matcher matcher = pattern.matcher(strSql);
    if (matcher.find()) {
      return matcher.start();
    }
    return -1;
  }

  public static int containsHaving(String strSql)
  {
    String patternStr = "\\s+having\\s+";
    Pattern pattern = Pattern.compile(patternStr, 66);

    Matcher matcher = pattern.matcher(strSql);
    if (matcher.find()) {
      return matcher.start();
    }
    return -1;
  }

  public static int containsOrderby(String strSql)
  {
    String patternStr = "\\s+order\\s+by\\s+";
    Pattern pattern = Pattern.compile(patternStr, 66);

    Matcher matcher = pattern.matcher(strSql);
    if (matcher.find()) {
      return matcher.start();
    }
    return -1;
  }

  public static boolean isInType(String operaCode)
  {
    return (operaCode.endsWith("in")) || (operaCode.indexOf("多选") != -1);
  }

  public static boolean isLikeType(String operaCode)
  {
    return (operaCode.endsWith("like")) || (operaCode.indexOf("相似") != -1);
  }

  public static Hashtable addEnvInfo(Hashtable hashParam, String[] envs)
  {
    if (hashParam == null) {
      hashParam = new Hashtable();
    }

    int iLen = QueryConst.BASIC_LOGIN_ENVS.length;

    if (envs == null)
    {
      envs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        envs[i] = "";
      }

    }

    ParamVO param = null;
    for (int i = 0; i < iLen; i++) {
      param = new ParamVO();
      param.setParamCode(QueryConst.BASIC_LOGIN_ENVS[i]);
      param.setParamName(QueryConst.BASIC_LOGIN_ENVS[i]);
      param.setValue(envs[i]);
      param.setDataType(new Integer(0));
      hashParam.put(param.getParamCode(), param);
    }

    return hashParam;
  }

  public static ValueObject[] addToVOs(ValueObject[] oldVOs, ValueObject addVO)
  {
    int iLen = oldVOs == null ? 0 : oldVOs.length;
    ValueObject[] newVOs = new ValueObject[iLen + 1];
    for (int i = 0; i < iLen; i++) {
      newVOs[i] = oldVOs[i];
    }
    newVOs[iLen] = addVO;
    return newVOs;
  }

  public static String aggString(String[] strs, String strDelim)
  {
    StringBuffer strBuf = new StringBuffer();
    int iLen = strs == null ? 0 : strs.length;

    for (int i = 0; i < iLen; i++) {
      if (i > 0) {
        strBuf.append(strDelim);
      }
      strBuf.append(strs[i]);
    }
    return strBuf.toString();
  }

  public static String convDataType2DB(int iDataType, String dbType)
  {
    String str = "UNKNOWNDATABASE";
    if (dbType.equals("ORACLE"))
    {
      str = "varchar2";
      switch (iDataType) {
      case -5:
      case 4:
        str = "int";
        break;
      case -6:
      case 5:
        str = "smallint";
        break;
      case 2:
      case 3:
      case 6:
      case 8:
        str = "number";
        break;
      case 1:
        str = "char";
      case -4:
      case -3:
      case -2:
      case -1:
      case 0:
      case 7: }  } else if (dbType.equals("DB2"))
    {
      str = "varchar";
      switch (iDataType) {
      case -5:
      case 4:
        str = "integer";
        break;
      case -6:
      case 5:
        str = "smallint";
        break;
      case 2:
      case 3:
      case 6:
      case 8:
        str = "decimal";
        break;
      case 1:
        str = "char";
        break;
      case -4:
      case -3:
      case -2:
      case 2004:
        str = "blob(32k)";
      }

    }
    else
    {
      str = "varchar";
      switch (iDataType) {
      case -5:
      case 4:
        str = "int";
        break;
      case -6:
      case 5:
        str = "smallint";
        break;
      case 2:
      case 3:
      case 6:
      case 8:
        str = "decimal";
        break;
      case 1:
        str = "char";
        break;
      case -4:
      case -3:
      case -2:
      case 2004:
        str = "image";
      }

    }

    return str;
  }

  public static String convDataType2DB(int iDataType)
  {
    return convDataType2DB(iDataType, "SQLSERVER");
  }

  public static int convDB2DataType(String strDataType)
  {
    int iType = 12;
    if (strDataType.equalsIgnoreCase("int"))
      iType = 4;
    else if (strDataType.equalsIgnoreCase("smallint"))
      iType = 5;
    else if (strDataType.equalsIgnoreCase("decimal"))
      iType = 3;
    else if (strDataType.equalsIgnoreCase("char"))
      iType = 1;
    else if (strDataType.equalsIgnoreCase("image")) {
      iType = 2004;
    }
    return iType;
  }

  public static ValueObject[] delFromVOs(ValueObject[] oldVOs, int iDelIndex)
  {
    int iLen = oldVOs == null ? 0 : oldVOs.length;
    if (iLen == 0) {
      return new ValueObject[0];
    }
    ValueObject[] newVOs = new ValueObject[iLen - 1];
    for (int i = 0; i < iLen - 1; i++) {
      if (i < iDelIndex)
        newVOs[i] = oldVOs[i];
      else if (i >= iDelIndex) {
        newVOs[i] = oldVOs[(i + 1)];
      }
    }
    return newVOs;
  }

  public static String[] delimString(String str, String strDelim)
  {
    if (str == null) {
      return null;
    }
    if (str.startsWith(strDelim)) {
      str = " " + str;
    }
    if (str.endsWith(strDelim)) {
      str = str + " ";
    }

    while (str.indexOf(strDelim + strDelim) != -1) {
      str = StringUtil.replaceAllString(str, strDelim + strDelim, strDelim + " " + strDelim);
    }

    Vector vec = new Vector();
    StringTokenizer st = new StringTokenizer(str, strDelim);
    while (st.hasMoreTokens()) {
      String strTemp = st.nextToken().trim();

      vec.addElement(strTemp);
    }

    String[] strs = null;
    if (vec.size() != 0) {
      strs = new String[vec.size()];
      vec.copyInto(strs);
    }
    return strs;
  }

  public static String replaceSql(String sql, Hashtable hashViewName)
  {
    if (hashViewName != null) {
      try {
        int iSize = hashViewName.size();
        String[] strKeys = new String[iSize];
        String[] strValues = new String[iSize];
        int[] iLens = new int[iSize];
        int s = 0;

        Enumeration keys = hashViewName.keys();
        while (keys.hasMoreElements())
        {
          strKeys[s] = keys.nextElement().toString();
          iLens[s] = strKeys[s].length();

          strValues[s] = hashViewName.get(strKeys[s]).toString();
          s++;
        }

        int[] iIndices = sortBubble(iLens);

        for (int i = iSize - 1; i >= 0; i--) {
          String key = strKeys[iIndices[i]];
          String value = strValues[iIndices[i]];
          System.out.println("[查询引擎]临时视图名称替换" + key + " → " + value);

          Logger.debug("[查询引擎]临时视图名称替换" + key + " → " + value);
          sql = StringUtil.replaceAllString(sql, key, value);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sql;
  }

  public static String simplifiedSql(String handSql)
  {
    int iSelectIndex = handSql.toLowerCase().indexOf("select");
    int iFromIndex = handSql.indexOf("from");
    int iOldIndex = iSelectIndex + 18;

    while ((iSelectIndex != -1) && (iFromIndex != -1) && (iSelectIndex < iFromIndex)) {
      handSql = handSql.substring(0, iSelectIndex + 6) + " 1 as a " + handSql.substring(iFromIndex, handSql.length());

      iSelectIndex = handSql.toLowerCase().indexOf("select", iOldIndex);
      iFromIndex = handSql.indexOf("from", iOldIndex);

      iOldIndex = iSelectIndex + 18;
    }

    return handSql;
  }

  public static int[] sortBubble(int[] s1)
  {
    int length = s1.length;
    int[] temp = new int[length];
    int[] s2 = new int[length];

    for (int i = 0; i < length; i++) {
      temp[i] = s1[i];
      s2[i] = i;
    }
    for (int i = 0; i < length; i++) {
      int index = temp[i];
      for (int j = i; j < length; j++) {
        int idx = temp[j];
        if (index > idx) {
          int tt = temp[j];
          int tt2 = s2[j];
          temp[j] = temp[i];
          s2[j] = s2[i];
          temp[i] = tt;
          s2[i] = tt2;
          index = idx;
        }
      }
    }
    return s2;
  }

  public static int[] sortBubble(String[] s1)
  {
    int length = s1.length;
    String[] temp = new String[length];
    int[] s2 = new int[length];

    for (int i = 0; i < length; i++) {
      temp[i] = s1[i];
      s2[i] = i;
    }
    for (int i = 0; i < length; i++) {
      String index = temp[i];
      for (int j = i; j < length; j++) {
        String idx = temp[j];
        if (index.compareTo(idx) > 0) {
          String tt = temp[j];
          int tt2 = s2[j];
          temp[j] = temp[i];
          s2[j] = s2[i];
          temp[i] = tt;
          s2[i] = tt2;
          index = idx;
        }
      }
    }
    return s2;
  }

  public static int variantTypeToSqlType(int variantType)
  {
    switch (variantType) {
    case 16:
      return 12;
    case 10:
      return 2;
    case 11:
      return -7;
    case 2:
      return -6;
    case 3:
      return 5;
    case 4:
      return 4;
    case 5:
      return -5;
    case 6:
      return 6;
    case 7:
      return 8;
    case 12:
      return -2;
    case 15:
      return 93;
    case 13:
      return 91;
    case 14:
      return 92;
    case 17:
      return 1111;
    case 8:
    case 9: } DataSetException.unrecognizedDataType();

    return 0;
  }

  public static int sqlTypeToVariantType(int sqlType)
  {
    switch (sqlType) {
    case 1:
    case 12:
      return 16;
    case 2:
    case 3:
      return 10;
    case -7:
      return 11;
    case -6:
      return 2;
    case 5:
      return 3;
    case 4:
      return 4;
    case -5:
      return 5;
    case 6:
    case 7:
      return 6;
    case 8:
      return 7;
    case -2:
      return 12;
    case 93:
      return 15;
    case 91:
      return 13;
    case 92:
      return 14;
    case 1111:
      return 17;
    }
    return 1111;
  }

  public static boolean withAggFunc(String strExp)
  {
    if (strExp == null) {
      return false;
    }

    strExp = StringUtil.replaceAllString(strExp, " ", "").toLowerCase();

    if (strExp.startsWith("(select")) {
      return false;
    }

    return (strExp.indexOf("sum(") != -1) || (strExp.indexOf("avg(") != -1) || (strExp.indexOf("max(") != -1) || (strExp.indexOf("min(") != -1) || (strExp.indexOf("count(") != -1);
  }

  public static String removeTop(String sql)
  {
    try
    {
      int[] iIndices = indexOf(sql, "select ");
      if (iIndices == null) {
        return sql;
      }
      for (int i = iIndices.length - 1; i >= 0; i--)
        sql = removeTop(sql, iIndices[i]);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return sql;
  }

  public static String removeTop(String sql, int iIndexSelect)
  {
    String sql1 = sql.substring(iIndexSelect + 7).trim();
    if (!sql1.startsWith("top ")) {
      return sql;
    }
    int iIndexTop = sql1.indexOf("top ");
    sql1 = sql1.substring(iIndexTop + 4).trim();
    int iIndexSpace = sql1.indexOf(" ");
    if (iIndexSpace == -1) {
      return sql;
    }
    sql1 = sql1.substring(iIndexSpace + 1).trim();
    sql = sql.substring(0, iIndexSelect + 7) + sql1;
    return sql;
  }

  public static String replaceHandSql(String sql, IEnvParam iEnvParam, String dsName, EnvInfo env)
  {
    String[] allEnvParamCodes = iEnvParam.getEnvParamCodes();
    int iLen = allEnvParamCodes == null ? 0 : allEnvParamCodes.length;
    Vector vec = new Vector();
    for (int i = 0; i < iLen; i++)
      if (sql.indexOf(allEnvParamCodes[i]) != -1)
        vec.addElement(allEnvParamCodes[i]);
    String[] envParamCodes = new String[vec.size()];
    vec.copyInto(envParamCodes);

    String[] envParamValues = iEnvParam.getEnvParamValues(envParamCodes, dsName, env);

    for (int i = 0; i < envParamCodes.length; i++)
    {
      sql = StringUtil.replaceAllString(sql, envParamCodes[i], envParamValues[i]);
    }

    return sql;
  }

  public static int[] indexOf(String str, String subStr)
  {
    if ((str == null) || (subStr == null)) {
      return null;
    }
    int iIndex = -1;
    int iFromIndex = 0;
    Vector vec = new Vector();
    while (true) {
      iIndex = str.indexOf(subStr, iFromIndex);
      if (iIndex == -1) {
        break;
      }
      vec.addElement(new Integer(iIndex));
      iFromIndex = iIndex + subStr.length();
    }
    int iSize = vec.size();
    if (iSize == 0) {
      return null;
    }
    int[] iIndices = new int[iSize];
    for (int i = 0; i < iSize; i++) {
      iIndices[i] = ((Integer)vec.elementAt(i)).intValue();
    }
    return iIndices;
  }

  public static boolean isConst(String exp)
  {
    if ((exp.startsWith("'")) && (exp.endsWith("'")))
      return true;
    try
    {
      Double.valueOf(exp);
      return true;
    }
    catch (Exception e)
    {
      if (exp.indexOf(".") == -1)
        return true;
    }
    return false;
  }

  public static boolean isNumberType(int iDataType)
  {
    boolean b = false;
    switch (iDataType) {
    case -6:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 8:
      b = true;
    case -5:
    case -4:
    case -3:
    case -2:
    case -1:
    case 0:
    case 1:
    case 7: } return b;
  }

  public static boolean isPersistentTable(String tablename)
  {
    return (tablename != null) && (tablename.startsWith("QIUE_"));
  }

  public static boolean isTempTable(String tablename)
  {
    return (tablename != null) && (tablename.startsWith("TEMQ_"));
  }

  public static String isValidId(String str)
  {
    int iLen = str == null ? 0 : str.length();
    if (iLen == 0) {
      return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000879");
    }

    str = str.toLowerCase();
    for (int i = 0; i < iLen; i++) {
      char c = str.charAt(i);

      if (((c < '0') || (c > '9')) && ((c < 'a') || (c > 'z')) && (c != '_'))
      {
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000943", null, new String[] { "" + c });
      }

    }

    if (iLen > 20) {
      return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000944");
    }

    return null;
  }

  public static String[] getInsertSqls(StorageDataSet dataSet, String tablename)
  {
    int iRowCount = dataSet.getRowCount();
    if (iRowCount == 0) {
      return null;
    }

    int iColCount = dataSet.getColumnCount();
    int[] iTypes = new int[iColCount];
    String sql = "insert into " + tablename + " (";
    String[] colnames = new String[iColCount];
    for (int i = 0; i < iColCount; i++)
    {
      colnames[i] = dataSet.getColumn(i).getColumnName();

      if (colnames[i].equalsIgnoreCase("rowid"))
      {
        continue;
      }
      if (colnames[i].length() != colnames[i].getBytes().length) {
        colnames[i] = ("fld" + i);
      }
      sql = sql + colnames[i];
      if (i < iColCount - 1) {
        sql = sql + ", ";
      }

      int iDataType = dataSet.getColumn(i).getDataType();
      iTypes[i] = variantTypeToSqlType(iDataType);
    }
    sql = sql + ") values(";

    int s = 0;

    String[] sqlInserts = new String[iRowCount];

    dataSet.first();
    while (dataSet.inBounds()) {
      String str = "";
      for (int i = 0; i < iColCount; i++)
      {
        if (colnames[i].equalsIgnoreCase("rowid"))
        {
          continue;
        }
        if (isNumberType(iTypes[i])) {
          if (iTypes[i] == 4) {
            str = str + String.valueOf(dataSet.getInt(i));
          } else if (iTypes[i] == -6) {
            str = str + String.valueOf(dataSet.getByte(i));
          } else if (iTypes[i] == 5) {
            str = str + String.valueOf(dataSet.getShort(i));
          } else if (iTypes[i] == 6) {
            str = str + String.valueOf(dataSet.getFloat(i));
          } else if (iTypes[i] == 8) {
            str = str + String.valueOf(dataSet.getDouble(i));
          } else {
            String strDecimal = String.valueOf(dataSet.getBigDecimal(i));

            int iDotIndex = strDecimal.indexOf(".");
            if ((iDotIndex != -1) && (iDotIndex + 39 < strDecimal.length()))
            {
              strDecimal = strDecimal.substring(0, iDotIndex + 9);
            }

            str = str + strDecimal;
          }
        } else {
          String strTemp = null;
          try
          {
            strTemp = dataSet.getString(i);
          } catch (Exception e) {
            System.out.println(e);
          }
          if (strTemp == null) {
            str = str + "null";
          }
          else {
            if (strTemp.indexOf("'") != -1) {
              strTemp = StringUtil.replaceAllString(strTemp, "'", "''");
            }

            str = str + "'" + strTemp + "'";
          }
        }
        if (i < iColCount - 1)
          str = str + ",";
        else {
          str = str + ")";
        }
      }
      sqlInserts[(s++)] = (sql + str);

      dataSet.next();
    }

    return sqlInserts;
  }

  public static String getCreateSql(StorageDataSet dataSet, String tablename, String dbType)
  {
    int iColCount = dataSet.getColumnCount();
    String sql = "create table " + tablename + " (\n";
    for (int i = 0; i < iColCount; i++)
    {
      String colname = dataSet.getColumn(i).getColumnName();

      if (colname.equalsIgnoreCase("rowid"))
      {
        continue;
      }
      String colname0 = colname;
      if (colname.length() != colname.getBytes().length) {
        colname = "fld" + i;
      }
      sql = sql + "  " + colname + " ";

      int iDataType = dataSet.getColumn(i).getDataType();
      iDataType = variantTypeToSqlType(iDataType);
      String datatype = convDataType2DB(iDataType, dbType);
      sql = sql + datatype;

      boolean bChar = (datatype.endsWith("char")) || (datatype.endsWith("char2"));

      int iLength = dataSet.getColumn(i).getPrecision();
      if ((bChar) && (iLength == -1))
      {
        dataSet.first();
        while (dataSet.inBounds()) {
          Object objRow = DatasetUtil.fetchDataRow(dataSet, colname0, iDataType);

          int iTemp = objRow == null ? 0 : objRow.toString().getBytes().length;

          if (iTemp > iLength) {
            iLength = iTemp;
          }

          dataSet.next();
        }
      }
      if (iLength <= 0) {
        iLength = 100;
      }

      int iScale = dataSet.getColumn(i).getScale();
      if (bChar) {
        sql = sql + "(" + iLength + ")";
      } else if (datatype.equals("decimal")) {
        if ((dbType.equals("DB2")) && (iLength > 31)) {
          iLength = 31;
        }
        if ((iLength == -1) || (iScale == -1))
          sql = sql + "(38, 8)";
        else {
          sql = sql + "(" + iLength + "," + iScale + ")";
        }
      }

      if (!dbType.equalsIgnoreCase("DB2")) {
        sql = sql + " null";
      }
      if (i < iColCount - 1) {
        sql = sql + ",";
      }
      sql = sql + "\n";
    }
    sql = sql + ")";
    return sql;
  }

  public static String getNoRecordSql(String sql)
  {
    String patternStr = "\\s+where\\s+";
    Pattern pattern = Pattern.compile(patternStr, 66);

    Matcher matcher = pattern.matcher(sql);
    int iIndex;
//    int iIndex;
    if (matcher.find())
      iIndex = matcher.start();
    else {
      iIndex = -1;
    }

    if (iIndex == -1) {
      iIndex = containsGroupby(sql);
      if (iIndex == -1) {
        iIndex = containsOrderby(sql);
        if (iIndex == -1) {
          iIndex = containsHaving(sql);
          if (iIndex == -1)
            sql = sql + " where 1=0";
          else
            sql = sql.substring(0, iIndex) + " where 1=0 " + sql.substring(iIndex);
        }
        else
        {
          sql = sql.substring(0, iIndex) + " where 1=0 " + sql.substring(iIndex);
        }
      }
      else {
        sql = sql.substring(0, iIndex) + " where 1=0 " + sql.substring(iIndex);
      }

    }
    else
    {
      sql = sql.replaceAll(patternStr, " where 1=0 and ");
    }
    return sql;
  }

  public static void writeFile(String fileURL, String strText)
    throws Exception
  {
    FileOutputStream fis = null;
    try {
      fis = new FileOutputStream(fileURL);
      byte[] b = strText.getBytes();
      fis.write(b);
    }
    catch (Exception e)
    {
      throw e;
    } finally {
      fis.close();
    }
  }

  public static String readFile(String fileURL)
    throws Exception
  {
    String strFile = null;
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(fileURL);
      byte[] b = new byte[fis.available()];
      fis.read(b);
      strFile = new String(b);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        fis.close();
      } catch (Exception e) {
        System.out.println("关闭文件失败！" + e);
      }
    }
    return strFile;
  }

  public static QueryBaseDef getBaseDefBySql(QueryBaseDef qbd, String sql, Hashtable hashAliasDisp)
    throws Exception
  {
    String tempTablename = qbd.getTemptablename();
    if ((sql == null) || (tempTablename == null) || (hashAliasDisp == null)) {
      throw new Exception(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000941"));
    }

    SelectFldVO[] sfs = null;

    IDataStructureInfo name = (IDataStructureInfo)NCLocator.getInstance().lookup(IDataStructureInfo.class.getName());

    sfs = name.getFldInfo(sql, tempTablename, qbd.getDsName());

    int iLen = sfs == null ? 0 : sfs.length;
    if (iLen == 0) {
      throw new Exception(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000942"));
    }

    for (int i = 0; i < iLen; i++) {
      Object obj = hashAliasDisp.get(sfs[i].getFldalias());
      String strDisp = obj == null ? sfs[i].getFldalias() : obj.toString();

      sfs[i].setFldname(strDisp);
    }

    qbd.setSelectFlds(sfs);
    try
    {
      sql = removeTop(sql);
      Select select = SqlParseMain.doParse(sql);
      QueryBaseDef tempqbd = SqlParseMain.transform(select);

      SelectFldVO[] sfsAna = tempqbd.getSelectFlds();
      int iLenAna = sfsAna == null ? 0 : sfsAna.length;
      if (iLenAna == 0)
        System.out.println("解析出的字段数为0，估计使用了select *");
      else {
        for (int i = 0; i < iLen; i++) {
          sfs[i].setExpression(sfsAna[i].getExpression());
        }
      }
      qbd.setSelectFlds(sfs);

      qbd.setFromTables(tempqbd.getFromTables());
      qbd.setWhereConds(tempqbd.getWhereConds());
    } catch (Exception e) {
      System.out.println(e);
      try
      {
        sql = simplifiedSql(sql);

        Select select = SqlParseMain.doParse(sql);
        QueryBaseDef tempqbd = SqlParseMain.transform(select);

        qbd.setFromTables(tempqbd.getFromTables());
        qbd.setWhereConds(tempqbd.getWhereConds());
      } catch (Exception e1) {
        System.out.println(e1);
      }
    }
    return qbd;
  }

  public static QueryModelDef getBaseDefByTablename(String tablename, String dsName)
  {
    QueryModelDef qmd = null;
    boolean bTempTable = isTempTable(tablename);
    if (bTempTable) {
      String id = tablename.substring("TEMQ_".length());

      qmd = ModelUtil.getQueryDef(id, dsName);
    }
    return qmd;
  }

  public static FromTableVO[] getBDTables(FromTableVO[] fts)
  {
    Vector vec = new Vector();
    int iLen = fts == null ? 0 : fts.length;
    for (int i = 0; i < iLen; i++) {
      if (fts[i].getTablecode().startsWith("bd_")) {
        vec.addElement(fts[i]);
      }
    }

    FromTableVO[] bdfts = null;
    if (vec.size() != 0) {
      bdfts = new FromTableVO[vec.size()];
      vec.copyInto(bdfts);
    }
    return bdfts;
  }

  public static SelectFldVO[] getFldsFromTable(String tableId, String tableAlias, ObjectTree tree)
  {
    SelectFldVO[] sfs = null;
    if ((tree instanceof Datadict))
    {
      if ((tree instanceof DataDictForNode)) {
        DataDictForNode treeIUFO = (DataDictForNode)tree;
        boolean bIUFO = treeIUFO.isIUFO();
        if (bIUFO)
        {
          tableId = tableAlias;
        }
      }

      Datadict dd = (Datadict)tree;
      BizObject td = null;
      if (!SystemProerty.isOnServer()) {
        IUserTableDefCreator[] creators = getTableDefCreator();
        UserTbDefInfo info = new UserTbDefInfo();
        info.setCorp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

        info.setTableName(tableId);
        for (int i = 0; i < creators.length; i++) {
          td = creators[i].getUserDefineTableDef(info);
          if (td != null) {
            break;
          }
        }
      }
      if (td == null)
        td = dd.findObject(tableId);
      if (td != null) {
        ChildObjectList fdl = null;
        if ((td instanceof ViewDef))
          fdl = ((ViewDef)td).getFieldList();
        else {
          fdl = ((TableDef)td).getFieldDefs();
        }
        if (fdl != null) {
          sfs = new SelectFldVO[fdl.getCount()];
          for (int i = 0; i < sfs.length; i++) {
            ChildObject fd = null;
            if ((fdl instanceof ViewFldList))
              fd = ((ViewFldList)fdl).getViewFld(i);
            else {
              fd = ((FieldDefList)fdl).getFieldDef(i);
            }
            sfs[i] = new SelectFldVO();
            sfs[i].setExpression(fd.getID());

            String display = fd.getDisplayName();
            sfs[i].setFldname(display);

            sfs[i].setFldalias(fd.getID());
            if ((fd instanceof ViewFld)) {
              ViewFld vfd = (ViewFld)fd;
              sfs[i].setColtype(new Integer(vfd.getDataType()));
            } else {
              FieldDef fdd = (FieldDef)fd;
              sfs[i].setColtype(new Integer(fdd.getDataType()));
            }
          }
        }
      }
    } else if ((tree instanceof QueryModelTree))
    {
      QueryModelTree qmt = (QueryModelTree)tree;
      String id = tableId.substring("TEMQ_".length());
      QueryModelDef qmd = (QueryModelDef)qmt.findObject(id);
      if (qmd != null) {
        sfs = qmd.getQueryBaseVO().getSelectFlds();
      }
    }
    return sfs;
  }

  public static void getQuotedMaterTable(String tempTableId, String dsNameForDef, Vector vec)
  {
    QueryModelDef qmd = getBaseDefByTablename(tempTableId, dsNameForDef);
    if (qmd == null) {
      return;
    }
    QueryBaseDef qbd = qmd.getQueryBaseDef();
    if (qbd == null) {
      System.out.println(">>>getQuotedMaterTable没找到查询基本定义！");
      return;
    }

    String dsExe = qbd.getDsName();

    FromTableVO[] fts = qbd.getFromTables();
    int iLen = fts == null ? 0 : fts.length;
    for (int i = 0; i < iLen; i++) {
      String tableId = fts[i].getTablecode();

      if ((tableId.startsWith("QIUE_")) && (vec.indexOf(tableId) == -1))
      {
        vec.addElement(new String[] { tableId, dsExe });

        int iIndex1 = tableId.indexOf("QIUE_");
        iIndex1 += "QIUE_".length();
        int iIndex2 = tableId.lastIndexOf("_");
        if (iIndex1 >= iIndex2) {
          continue;
        }
        String queryId = tableId.substring(iIndex1, iIndex2);

        getQuotedMaterTable("TEMQ_" + queryId, dsExe, vec);
      } else {
        if (!isTempTable(tableId))
          continue;
        getQuotedMaterTable(tableId, dsNameForDef, vec);
      }
    }
  }

  public static ValueObject[] getTableFldVO(String tableCode, String tableName, String tableAlias, String fldId, ObjectTree tree)
  {
    ValueObject[] vos = null;
    if ((tree instanceof Datadict))
    {
      if ((tree instanceof DataDictForNode)) {
        DataDictForNode treeIUFO = (DataDictForNode)tree;
        boolean bIUFO = treeIUFO.isIUFO();
        if (bIUFO)
        {
          tableCode = tableAlias;
        }
      }

      Datadict dd = (Datadict)tree;
      BizObject td = dd.findObject(tableCode);
      if (td != null)
      {
        if ((td instanceof TableDefWithAlias)) {
          tableCode = ((TableDefWithAlias)td).getRealName();
        }
        FromTableVO ft = new FromTableVO();
        ft.setTablecode(tableCode);
        ft.setTabledisname(tableName);
        ft.setTablealias(tableAlias);

        SelectFldVO sf = null;
        ChildObject fd = null;
        if ((td instanceof ViewDef))
        {
          fd = fldId == null ? null : ((ViewDef)td).getViewField(fldId);
        }
        else {
          fd = fldId == null ? null : ((TableDef)td).getFieldDef(fldId);
        }

        if (fd != null)
        {
          sf = new SelectFldVO();
          sf.setExpression(fd.getID());
          sf.setFldname(fd.getDisplayName());
          sf.setFldalias(fd.getID());

          if ((fd instanceof ViewFld)) {
            ViewFld vfd = (ViewFld)fd;
            sf.setColtype(new Integer(vfd.getDataType()));
          } else {
            FieldDef fdd = (FieldDef)fd;
            sf.setColtype(new Integer(fdd.getDataType()));
          }
        }

        vos = new ValueObject[] { ft, sf };
      }
    } else if ((tree instanceof QueryModelTree))
    {
      QueryModelTree qmt = (QueryModelTree)tree;
      String id = tableCode.substring("TEMQ_".length());
      QueryModelDef qmd = (QueryModelDef)qmt.findObject(id);
      if (qmd != null)
      {
        FromTableVO ft = new FromTableVO();
        ft.setTablecode("TEMQ_" + qmd.getID());
        ft.setTabledisname(qmd.getDisplayName());
        ft.setTablealias("TEMQ_" + qmd.getID());
        if (fldId == null) {
          return new ValueObject[] { ft, null };
        }

        SelectFldVO[] sfs = qmd.getQueryBaseVO().getSelectFlds();
        int iIndex = -1;
        for (int i = 0; i < sfs.length; i++) {
          if (sfs[i].getFldalias().equals(fldId)) {
            iIndex = i;
            break;
          }
        }

        if (iIndex != -1) {
          vos = new ValueObject[] { ft, sfs[iIndex] };
        }
      }
    }
    return vos;
  }

  public static int[] getTempTableIndex(FromTableVO[] fts)
  {
    int[] iIndices = null;
    if (fts != null) {
      Vector vec = new Vector();
      for (int i = 0; i < fts.length; i++) {
        if (isTempTable(fts[i].getTablecode())) {
          vec.addElement(new Integer(i));
        }
      }
      int iSize = vec.size();
      if (iSize != 0) {
        iIndices = new int[iSize];
        for (int i = 0; i < iSize; i++) {
          iIndices[i] = ((Integer)vec.elementAt(i)).intValue();
        }
      }
    }
    return iIndices;
  }

  public static Hashtable makeHashForReplaceParam(ParamVO[] params)
  {
    Hashtable hashParam = new Hashtable();
    int iLen = params == null ? 0 : params.length;
    for (int i = 0; i < iLen; i++) {
      String key = params[i].getParamCode();
      if ((!key.startsWith("#")) || (!key.endsWith("#")))
        continue;
      String value = params[i].getValue();
      if ((value == null) || (value.equals(""))) {
        int iType = params[i].getDataType().intValue();

        switch (iType) {
        case 1:
        case 3:
          value = "0";
          break;
        default:
          value = " ";
        }
      }

      ParamVO paramClone = (ParamVO)params[i].clone();
      paramClone.setValue(value);
      hashParam.put(key, paramClone);
    }

    return hashParam;
  }

  public static void makeRelatedStruc(String tempTableName, DefaultMutableTreeNode parent, String dsName)
  {
    QueryModelDef qmd = getBaseDefByTablename(tempTableName, dsName);
    if (qmd == null)
    {
      return;
    }

    QueryBaseDef qbd = qmd.getQueryBaseDef();
    if (qbd == null) {
      System.out.println("没找到查询基本定义！");
      return;
    }

    FromTableVO[] fts = qbd.getFromTables();
    int iLen = fts == null ? 0 : fts.length;
    for (int i = 0; i < iLen; i++) {
      String id = fts[i].getTablecode();

      DefaultMutableTreeNode child = new DefaultMutableTreeNode(fts[i].getTabledisname());

      parent.add(child);

      if (isTempTable(id))
        makeRelatedStruc(id, child, dsName);
    }
  }

  public static String replaceHandSql(String sql, Hashtable hashParam)
  {
    if (hashParam != null) {
      try {
        Enumeration keys = hashParam.keys();
        while (keys.hasMoreElements())
        {
          String key = keys.nextElement().toString();
          if ((key.startsWith("#")) && (key.endsWith("#")))
          {
            ParamVO param = (ParamVO)hashParam.get(key);
            String value = param.getValue();

            int iDataType = param.getDataType() == null ? 0 : param.getDataType().intValue();

            boolean bNumberParam = (iDataType == 1) || (iDataType == 3);

            String strOpr = param.getOperaCode() == null ? "" : param.getOperaCode().trim();

            if (value != null)
            {
              if (isInType(strOpr))
              {
                int iBeginIndex = value.indexOf("(");
                int iEndIndex = value.lastIndexOf(")");
                if ((iBeginIndex != -1) && (iEndIndex != -1) && (iBeginIndex < iEndIndex))
                {
                  value = value.substring(iBeginIndex + 1, iEndIndex);
                }

                if (value.trim().equals(""))
                  value = bNumberParam ? "0" : "' '";
              }
              else if ((!bNumberParam) && (!value.startsWith("'")) && (!value.endsWith("'")))
              {
                if (!ModelUtil.isElement(value, QueryConst.BASIC_LOGIN_ENVS))
                {
                  value = "'" + value + "'";
                }
              }

            }

            sql = StringUtil.replaceAllString(sql, key, value);
          }

        }

        if (hashParam.containsKey("ENV_PARAM_KEY")) {
          Object[] objEnvParams = (Object[])(Object[])hashParam.get("ENV_PARAM_KEY");

          String strEnvParam = (String)objEnvParams[0];
          IEnvParam iEnvParam = (IEnvParam)Class.forName(strEnvParam).newInstance();

          String dsName = (String)objEnvParams[1];
          EnvInfo env = (EnvInfo)objEnvParams[2];
          sql = replaceHandSql(sql, iEnvParam, dsName, env);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sql;
  }

  public static void refreshMaterTable(String[] ids, Hashtable hashParam, String dsNameForDef)
  {
    try
    {
      List vecMater = getQuotedMaterTables(ids, dsNameForDef);

      int iSize = vecMater.size();
      if (iSize != 0)
        for (int i = iSize - 1; i >= 0; i--) {
          String[] materTableInfo = (String[])(String[])vecMater.get(i);
          String materTableId = materTableInfo[0];
          String dsName = materTableInfo[1];
          System.out.println("刷新" + materTableId + "@" + dsName + "...");

          String dbType = getDbType(dsName);

          ModelUtil.refreshMaterTable(materTableId, hashParam, dsName, dbType);
        }
    }
    catch (Exception e)
    {
      System.out.println("处理物化表刷新有误");
      e.printStackTrace();
    }
  }

  public static List getQuotedMaterTables(String[] ids, String dsNameForDef)
  {
    List vecMater = null;
    try
    {
      boolean bComplex = false;
      int iLen = ids == null ? 0 : ids.length;
      for (int i = 0; i < iLen; i++)
      {
        QueryModelDef qmd = ModelUtil.getQueryDef(ids[i], dsNameForDef);
        if (qmd != null) {
          QueryBaseDef qbd = qmd.getQueryBaseDef();    //yqq   pk_corp=1017   ???????
          if (qbd == null)
            continue;
          FromTableVO[] fts = qbd.getFromTables();
          int jLen = fts == null ? 0 : fts.length;
          for (int j = 0; j < jLen; j++) {
            String tableId = fts[j].getTablecode();

            if ((!tableId.startsWith("TEMQ_")) && (!tableId.startsWith("QIUE_"))) {
              continue;
            }
            bComplex = true;
            break;
          }

          if (bComplex)
          {
            break;
          }
        }
      }

      if (bComplex) {
        ITempTableHandler name = (ITempTableHandler)NCLocator.getInstance().lookup(ITempTableHandler.class.getName());

        vecMater = name.getQuotedMaterTables(ids, dsNameForDef);
      } else {
        vecMater = new Vector();
      }
    } catch (Exception e) {
      e.printStackTrace();
      vecMater = new Vector();
    }
    return vecMater;
  }

  public static String getDbType(String dsName)
  {
    if (db2TypeMap.containsKey(dsName)) {
      return (String)db2TypeMap.get(dsName);
    }
    String strDbType = "SQLSERVER";
    try
    {
      String[] strDbInfos = null;

      IQEDataSourceInfo name = (IQEDataSourceInfo)NCLocator.getInstance().lookup(IQEDataSourceInfo.class.getName());

      strDbInfos = name.getDsnDbtype(dsName);

      strDbType = strDbInfos[1];
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    db2TypeMap.put(dsName, strDbType);
    return strDbType;
  }

  public static synchronized IUserTableDefCreator[] getTableDefCreator()
  {
    if (tableDefCreator == null) {
      try {
        Class.forName("nc.vo.pub.ddc.userdefine.IUserTableDefCreator");
        Class[] cls = PackageLoader.getAllClassInPackage("nc.vo.pub.ddc.userdefine");

        List creatorList = new ArrayList();
        for (int i = 0; i < cls.length; i++) {
          if ((!IUserTableDefCreator.class.isAssignableFrom(cls[i])) || (IUserTableDefCreator.class.equals(cls[i])))
            continue;
          try {
            creatorList.add(cls[i].newInstance());
          }
          catch (Exception e1) {
          }
        }
        tableDefCreator = new IUserTableDefCreator[creatorList.size()];
        creatorList.toArray(tableDefCreator);
        Arrays.sort(tableDefCreator);
      } catch (Exception e) {
        e.printStackTrace();
        tableDefCreator = new IUserTableDefCreator[0];
      }
    }
    return tableDefCreator;
  }

  public static synchronized IUserTableName[] getUserDefTableName() {
    IUserTableName[] names = null;
    List namelist = new ArrayList();
    try {
      Class.forName("nc.vo.pub.ddc.userdefine.IUserTableName");
      Class[] cls = PackageLoader.getAllClassInPackage("nc.vo.pub.ddc.userdefine");

      for (int i = 0; i < cls.length; i++) {
        if ((!IUserTableName.class.isAssignableFrom(cls[i])) || (IUserTableName.class.equals(cls[i])))
          continue;
        namelist.add(cls[i].newInstance());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    names = new IUserTableName[namelist.size()];
    namelist.toArray(names);
    return names;
  }

  public static ParamVO[] getInvisibleParam(ParamVO[] allparams)
  {
    if (allparams == null)
      return null;
    ArrayList paramlist = new ArrayList();
    for (int i = 0; i < allparams.length; i++) {
      if (allparams[i].isInvisible())
        paramlist.add(allparams[i]);
    }
    return (ParamVO[])(ParamVO[])paramlist.toArray(new ParamVO[paramlist.size()]);
  }

  public static String replaceOldEnv(String strText, QueryModelDef qmd, boolean[] bChangeds)
  {
    String[] OLD_ENVS = { "#登录帐套#", "#登录公司#", "#登录用户#", "#登录日期#" };

    String[] NEW_ENVS = { "#LoginAccount#", "#LoginCorp#", "#LoginUser#", "#LoginDate#" };

    for (int i = 0; i < OLD_ENVS.length; i++) {
      if (strText.indexOf(OLD_ENVS[i]) != -1) {
        System.out.println("查询" + qmd.getID() + "(" + qmd.getDisplayName() + ")：" + OLD_ENVS[i] + " → " + NEW_ENVS[i]);

        strText = StringUtil.replaceAllString(strText, OLD_ENVS[i], NEW_ENVS[i]);

        bChangeds[0] = true;
      }
    }
    return strText;
  }

  public static boolean changeOldEnv(QueryModelDef qmd)
  {
    boolean[] bChangeds = { false };

    QueryBaseVO qb = qmd.getQueryBaseVO();
    if (qb != null)
    {
      WhereCondVO[] wcs = qb.getWhereConds();
      if (wcs != null) {
        for (int i = 0; i < wcs.length; i++) {
          String rightFld = wcs[i].getRightfld() == null ? null : wcs[i].getRightfld().toString();

          if (rightFld != null) {
            rightFld = replaceOldEnv(rightFld, qmd, bChangeds);
            wcs[i].setRightfld(rightFld);
          }
          String exp = wcs[i].getExpression0();
          if (exp != null) {
            exp = replaceOldEnv(exp, qmd, bChangeds);
            wcs[i].setExpression0(exp);
          }
        }
      }

      SelectFldVO[] sfs = qb.getSelectFlds();
      if (sfs != null) {
        for (int i = 0; i < sfs.length; i++) {
          String exp = sfs[i].getExpression();
          if (exp != null) {
            exp = replaceOldEnv(exp, qmd, bChangeds);
            sfs[i].setExpression(exp);
          }
        }
      }

      String handSql = qb.getHandSql();
      if (handSql != null) {
        handSql = replaceOldEnv(handSql, qmd, bChangeds);
        qb.setHandSql(handSql);
      }

    }

    ParamVO[] params = qmd.getParamVOs();
    if (params != null) {
      for (int i = 0; i < params.length; i++)
      {
        String value = params[i].getValue();
        if (value != null) {
          value = replaceOldEnv(value, qmd, bChangeds);
          params[i].setValue(value);
        }

        String consultCode = params[i].getConsultCode();
        if (consultCode != null) {
          consultCode = replaceOldEnv(consultCode, qmd, bChangeds);
          params[i].setConsultCode(consultCode);
        }
      }
    }
    return bChangeds[0];
  }

  public static String getOperatorList(String strOperator)
  {
    String strOperatorList = null;
    int iBeginIndex = strOperator.indexOf("{");
    int iEndIndex = strOperator.indexOf("}");
    if ((iBeginIndex != -1) && (iEndIndex != -1) && (iBeginIndex + 1 < iEndIndex)) {
      strOperatorList = strOperator.substring(iBeginIndex + 1, iEndIndex);
    }
    return strOperatorList;
  }

  public static void findMaxTableCount(String tempTableName, String dsName, int[] iMax)
  {
    QueryModelDef qmd = getBaseDefByTablename(tempTableName, dsName);
    if (qmd == null) {
      return;
    }
    QueryBaseDef qbd = qmd.getQueryBaseDef();
    if (qbd == null) {
      return;
    }

    FromTableVO[] fts = qbd.getFromTables();
    int iLen = fts == null ? 0 : fts.length;

    if (iMax[0] < iLen) {
      iMax[0] = iLen;
    }
    for (int i = 0; i < iLen; i++) {
      String id = fts[i].getTablecode();

      if (isTempTable(id))
        findMaxTableCount(id, dsName, iMax);
    }
  }
}