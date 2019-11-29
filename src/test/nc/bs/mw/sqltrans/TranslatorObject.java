package nc.bs.mw.sqltrans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.logging.Logger;
import nc.jdbc.framework.util.DBConsts;
import nc.vo.pub.lang.UFDouble;

public class TranslatorObject
  implements ITranslator, DBConsts
{
  int[][] m_apiErrorList = (int[][])null;

  String[][] m_apsFunList = (String[][])null;

  String[] m_asOperationStr = { "=", "!=", "<>", "<", "<=", ">", ">=", "--", "\t" };

  String[] m_asSpecialStr = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<", ">", "||", "&&", " ", "--", m_sLineSep, "\t" };

  String[] m_asSqlWords = null;

  boolean m_bFinded = false;

  SQLException m_eSqlExp = null;

  int m_iBracket = 0;

  int m_iDestinationDatabaseType = 2;

  StringBuffer m_sbDestinationSql = null;

  String m_sLeftTable = "";

  String m_sLeftWhere = " where ";

  static String m_sLineSep = "\r\n";

  String m_sResorceSQL = null;

  String m_sSpecialChar = "-+()*=,? <>; \t" + m_sLineSep;

  public String[] dateTypes = { "datetime", "smalldatetime", "timestamp", "UFDatabasedate" };

  String[] numberTypes = { "decimal", "float", "real", "int", "money", "numeric", "smallint", "smallmoney", "tinyint", "UFNumber5", "UFFactor", "UFPercent", "UFRate", "UFRebate", "UFTaxRate", "UFInterestRate", "UFInteger", "UFSeq", "UFIndex", "UFIndexInteger", "UFVersionNO", "UFFlag", "UFSeqshort", "UFDirection", "UFWaItem", "UFPeriod", "UFLevel", "UFWidth", "UFApproveStatus", "UFDefaultset", "UFInt", "UFStatus" };

  public String[] charTypes = { "char", "varchar" };

  private Connection m_con = null;

  private int m_DbVersion = 0;

  public TranslatorObject()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject");

    setDestDbType(2);

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject Over");
  }

  public TranslatorObject(int dbType)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject");

    setDestDbType(dbType);
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject Over");
  }

  public int getDestDbType() {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDestDbType");

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDestDbType Over");
    return this.m_iDestinationDatabaseType;
  }

  public int getErrorCode(int iErrorCode)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getErrorCode");

    if (this.m_apiErrorList == null) {
      return iErrorCode;
    }
    for (int i = 0; i < this.m_apiErrorList.length; i++)
    {
      if (this.m_apiErrorList[i][0] == iErrorCode) {
        return this.m_apiErrorList[i][1];
      }
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getErrorCode Over");
    return iErrorCode;
  }

  public String getFunction(String sSourceFunction)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunction");

    if (this.m_apsFunList == null) {
      return sSourceFunction;
    }
    for (int i = 0; i < this.m_apsFunList.length; i++) {
      String st = this.m_apsFunList[i][0];
      if (st.equalsIgnoreCase(sSourceFunction)) {
        return this.m_apsFunList[i][1];
      }
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunction Over");
    return sSourceFunction;
  }

  public String getSourceSql()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSourceSql");
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSourceSql Over");
    return this.m_sResorceSQL;
  }

  public String getSql()
    throws Exception
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSql");
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSql Over");
    return this.m_sResorceSQL;
  }

  public SQLException getSqlException()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlException");

    if (this.m_eSqlExp == null) {
      return null;
    }
    if (this.m_apiErrorList == null) {
      return this.m_eSqlExp;
    }
    SQLException eSQL = new SQLException(this.m_eSqlExp.getMessage(), this.m_eSqlExp.getSQLState(), getErrorCode(this.m_eSqlExp.getErrorCode()));

    eSQL.setNextException(this.m_eSqlExp.getNextException());
    Logger.error("sql original exception", this.m_eSqlExp);
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlException Over");
    return eSQL;
  }

  protected int getStatementType()
    throws Exception
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStatementType");
    int iType = 0;

    if (this.m_asSqlWords.length < 1) {
      return iType;
    }

    if (this.m_asSqlWords[0].equalsIgnoreCase("SELECT"))
      iType = 1;
    else if (this.m_asSqlWords[0].equalsIgnoreCase("INSERT"))
      iType = 2;
    else if (this.m_asSqlWords[0].equalsIgnoreCase("CREATE")) {
      if ((this.m_asSqlWords.length > 1) && (this.m_asSqlWords[1].equalsIgnoreCase("view")))
      {
        iType = 1;
      }
      else
        iType = 3;
    }
    else if (this.m_asSqlWords[0].equalsIgnoreCase("DROP"))
      iType = 4;
    else if (this.m_asSqlWords[0].equalsIgnoreCase("DELETE"))
      iType = 5;
    else if (this.m_asSqlWords[0].equalsIgnoreCase("UPDATE"))
      iType = 6;
    else if (this.m_asSqlWords[0].equalsIgnoreCase("EXPLAIN")) {
      iType = 7;
    }
    else if ((this.m_asSqlWords[0].equalsIgnoreCase("if")) && (this.m_asSqlWords[1].equalsIgnoreCase("exists")))
      iType = 8;
    else {
      iType = 1;
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStatementType Over");
    return iType;
  }

  public boolean isCompareOperator(String s)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCompareOperator");
    for (int i = 0; i < this.m_asOperationStr.length; i++) {
      if (s.equals(this.m_asOperationStr[i]))
        return true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCompareOperator Over");
    return false;
  }

  public String[] parseSql(String sql)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseSql");

    if ((sql == null) || (sql.trim().length() == 0)) {
      return null;
    }
    String[] asKeyWords = null;

    Hashtable table = new Hashtable();

    int iCount = 0;
    int iOffSet = 0;

    String sWord = parseWord(sql.substring(iOffSet));

    while (sWord.length() > 0)
    {
      iOffSet += sWord.length();

      sWord = sWord.trim();

      if (sWord.length() > 0)
      {
        String s = sWord;

        if (s.equalsIgnoreCase("join")) {
          Object obj = table.get(new Integer(iCount - 1));

          if (obj == null) {
            table.put(new Integer(iCount), "inner");
            iCount++;
          } else {
            String stSql = obj.toString();
            if ((!stSql.equalsIgnoreCase("inner")) && (!stSql.equalsIgnoreCase("outer"))) {
              String joinType = "inner";
              if ((stSql.equalsIgnoreCase("right")) || (stSql.equalsIgnoreCase("left"))) {
                joinType = "outer";
              }
              table.put(new Integer(iCount), joinType);
              iCount++;
            }
          }
        }

        if (iCount > 0) {
          String st = table.get(new Integer(iCount - 1)).toString().trim();

          if ((st.endsWith(".")) || (s.trim().startsWith("."))) {
            table.put(new Integer(iCount - 1), st + s.trim());
          }
          else
          {
            table.put(new Integer(iCount), s.trim());

            iCount++;
          }
        }
        else {
          table.put(new Integer(iCount), s.trim());

          iCount++;
        }

      }

      sWord = parseWord(sql.substring(iOffSet));

      String s = sWord.trim();
      if (s.length() == 0) {
        sWord = s;
      }
    }

    asKeyWords = new String[iCount];

    for (int i = 0; i < iCount; i++) {
      asKeyWords[i] = ((String)table.get(new Integer(i)));
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseSql Over");
    return asKeyWords;
  }

  public String parseWord(String s)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseWord");

    if (s.length() == 0) {
      return "";
    }

    boolean bInSingle = false;
    boolean bInDouble = false;
    boolean bFound = false;

    int iOffSet = 0;

    while (((iOffSet < s.length()) && (s.charAt(iOffSet) == ' ')) || ((iOffSet < s.length()) && (s.charAt(iOffSet) == '\t')) || ((iOffSet < s.length()) && (m_sLineSep.indexOf(s.charAt(iOffSet)) >= 0)))
    {
      iOffSet++;

      if (iOffSet > s.length()) {
        return "";
      }
    }

    if (iOffSet >= s.length()) {
      return "";
    }

    char c = s.charAt(iOffSet);

    iOffSet++;

    if (iOffSet < s.length())
    {
      String ss = "" + c + s.charAt(iOffSet);

      for (int i = 0; i < this.m_asSpecialStr.length; i++) {
        if (ss.equals(this.m_asSpecialStr[i]))
        {
          return s.substring(0, iOffSet + 1);
        }
      }
    }

    iOffSet--;

    for (int i = 0; i < this.m_sSpecialChar.length(); i++) {
      if (c != this.m_sSpecialChar.charAt(i))
      {
        continue;
      }

      if ((c != '-') || (iOffSet <= 1) || ((s.charAt(iOffSet - 1) != 'E') && (s.charAt(iOffSet - 1) != 'e')) || (!isNumber(s.substring(0, iOffSet - 1))))
      {
        return s.substring(0, iOffSet + 1);
      }

    }

    while (iOffSet < s.length())
    {
      c = s.charAt(iOffSet);

      if (c == '\'')
      {
        if (!bInDouble)
        {
          if (bInSingle)
          {
            if ((iOffSet + 1 < s.length()) && (s.charAt(iOffSet + 1) == '\''))
            {
              iOffSet++;
            }
            else {
              iOffSet++;
              break;
            }
          }

          bInSingle = true;
        }

      }

      if (c == '"')
      {
        if (!bInSingle)
        {
          if (bInDouble)
          {
            iOffSet++;
            break;
          }

          bInDouble = true;
        }

      }

      if ((!bInDouble) && (!bInSingle))
      {
        iOffSet++;

        if (iOffSet < s.length())
        {
          String ss = "" + c + s.charAt(iOffSet);

          for (int i = 0; i < this.m_asSpecialStr.length; i++)
          {
            if (ss.equals(this.m_asSpecialStr[i])) {
              bFound = true;
              break;
            }
          }
        }

        iOffSet--;

        for (int i = 0; i < this.m_sSpecialChar.length(); i++) {
          if ((c != this.m_sSpecialChar.charAt(i)) || (
            (c == '-') && (iOffSet > 1) && ((s.charAt(iOffSet - 1) == 'E') || (s.charAt(iOffSet - 1) == 'e')) && (isNumber(s.substring(0, iOffSet - 1))))) {
            continue;
          }
          bFound = true;
          break;
        }

        if (bFound)
        {
          break;
        }
      }
      iOffSet++;
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.parseWord Over");
    return s.substring(0, iOffSet);
  }

  public void setSql(String sql)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSql");
    this.m_sResorceSQL = sql;

    setSqlArray(parseSql(this.m_sResorceSQL));
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSql Over");
  }

  public void setSqlException(SQLException e)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlException");
    this.m_eSqlExp = e;
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlException Over");
  }

  public String[] getFunParam(String[] asWords, int startIndex, int endIndex)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunParam");
    int iLBracket = 0;
    int iRBracket = 0;

    Vector vec = new Vector();
    String st = "";

    for (int iOff = startIndex; iOff < endIndex; iOff++)
    {
      if (asWords[iOff].equals("(")) {
        iLBracket++;
      }
      if (asWords[iOff].equals(")"))
        iRBracket++;
      if ((asWords[iOff].equals(",")) && (iLBracket == iRBracket)) {
        vec.addElement(st);
        st = "";
      } else {
        st = st + " " + asWords[iOff];
      }
    }
    vec.addElement(st);
    String[] re = new String[vec.size()];
    vec.copyInto(re);
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getFunParam Over");
    return re;
  }

  public int getIndexOf(String source, String dest)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getIndexOf");
    boolean find = false;
    int index = 0;
    while (!find) {
      index = source.indexOf(dest, index);

      if ((index < 0) || ((!inQuotation(source, index)) && (isSingleWord(source, dest, index)))) {
        find = true;
        continue;
      }index += dest.length();
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getIndexOf Over");
    return index;
  }

  public String[] getLeftSql(String[] asSqlWords, int iOffSet)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getLeftSql");
    boolean inLeft = true;
    Vector vec = new Vector();

    while ((iOffSet < asSqlWords.length) && (!asSqlWords[iOffSet].equalsIgnoreCase("and")) && (!asSqlWords[iOffSet].equalsIgnoreCase("or")) && (inLeft)) {
      if (isBiJiaoFu(asSqlWords[iOffSet])) {
        inLeft = false;
      }
      if ((inLeft) && (haveAloneSt(asSqlWords[iOffSet], "."))) {
        vec.addElement(asSqlWords[iOffSet]);
      }
      iOffSet++;
    }
    String[] leftSql = new String[vec.size()];
    vec.copyInto(leftSql);
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getLeftSql Over");
    return leftSql;
  }

  public TransUnit getRightSql(String[] asSqlWords, int iOffSet)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getRightSql");
    boolean inRight = false;
    String rightSql = "";
    int leftKuoHao = 0;
    int rightKouHao = 0;

    while ((iOffSet < asSqlWords.length) && (((!asSqlWords[iOffSet].equals(",")) && (joinNotEnd(asSqlWords[iOffSet]))) || ((asSqlWords[iOffSet].equals(",")) && (leftKuoHao != rightKouHao) && (!asSqlWords[iOffSet].equalsIgnoreCase("and")) && (!asSqlWords[iOffSet].equalsIgnoreCase("or")))))
    {
      if (asSqlWords[iOffSet].equals("(")) {
        leftKuoHao++;
      }
      if (asSqlWords[iOffSet].equals(")")) {
        rightKouHao++;
      }

      if ((inRight) && (leftKuoHao <= rightKouHao)) {
        rightSql = rightSql + " " + asSqlWords[iOffSet];
      }
      if (isBiJiaoFu(asSqlWords[iOffSet])) {
        inRight = true;
        if ((asSqlWords[iOffSet].equalsIgnoreCase("is")) && (iOffSet < asSqlWords.length - 1) && (asSqlWords[(iOffSet + 1)].equalsIgnoreCase("not"))) {
          iOffSet++;
        }
      }
      iOffSet++;
    }
    if (rightKouHao > leftKuoHao) {
      iOffSet--;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getRightSql Over");
    return new TransUnit(null, rightSql, iOffSet);
  }

  public int getStartIndex(String[] asSqlWords, int iOffSet)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStartIndex");
    boolean inRight = false;
    String rightSql = "";
    int leftKuoHao = 0;
    int rightKouHao = 0;

    while ((iOffSet > 0) && (!asSqlWords[(iOffSet - 1)].equalsIgnoreCase("on")) && (!asSqlWords[(iOffSet - 1)].equalsIgnoreCase("and")) && (!asSqlWords[(iOffSet - 1)].equalsIgnoreCase("or"))) {
      iOffSet--;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getStartIndex Over");
    return iOffSet;
  }

  public TransUnit getSubSql(String[] asSqlWords, String leftWord, String rightWord, int iOffSet)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSubSql");
    int left = 1;
    int right = 0;

    Vector vec = new Vector();

    vec.addElement(asSqlWords[iOffSet]);

    while ((iOffSet < asSqlWords.length) && (left != right)) {
      iOffSet++;

      if (asSqlWords[iOffSet].equalsIgnoreCase(leftWord)) {
        left++;
      }
      if (asSqlWords[iOffSet].equalsIgnoreCase(rightWord)) {
        right++;
      }

      vec.addElement(asSqlWords[iOffSet]);
    }

    String[] newCaseSql = new String[vec.size()];
    vec.copyInto(newCaseSql);

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSubSql Over");
    return new TransUnit(newCaseSql, null, iOffSet);
  }

  public boolean haveAloneSt(String source, String dest)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveAloneSt");
    boolean have = false;

    while (source.indexOf(dest) >= 0) {
      int singleNum = 0;
      for (int i = 0; i < source.indexOf(dest); i++) {
        char ch = source.charAt(i);
        if (ch == '\'') {
          singleNum++;
        }
      }
      if (singleNum % 2 == 0) {
        have = true;
        break;
      }
      source = source.substring(source.indexOf(dest) + 1);
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveAloneSt Over");
    return have;
  }

  public boolean haveOtherTable(String sql_old, Vector vecTable)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveOtherTable");
    boolean haveOtherTable = false;

    if ((vecTable != null) && (vecTable.size() > 0)) {
      int size = vecTable.size();

      for (int i = 0; i < size; i++) {
        Object obj = vecTable.elementAt(i);
        String sql = sql_old;

        if (obj != null) {
          String table = obj.toString();

          while (sql.indexOf(".") >= 0) {
            if ((table.trim().length() > 0) && ((sql.indexOf(table + ".") == 0) || ((sql.indexOf(table + ".") > 0) && ((sql.charAt(sql.indexOf(table + ".") - 1) == ' ') || (sql.charAt(sql.indexOf(table + ".") - 1) == '+') || (sql.charAt(sql.indexOf(table + ".") - 1) == '-') || (sql.charAt(sql.indexOf(table + ".") - 1) == '*') || (sql.charAt(sql.indexOf(table + ".") - 1) == '/') || (sql.charAt(sql.indexOf(table + ".") - 1) == '|')))))
            {
              haveOtherTable = true;

              break;
            }
            sql = sql.substring(sql.indexOf(".") + 1);
          }
        }
      }

    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.haveOtherTable Over");
    return haveOtherTable;
  }

  public boolean inQuotation(String formula, int endIndex)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.inQuotation");
    int quotationNum = 0;

    int left = 0;
    int right = 0;

    for (int i = 0; i < endIndex; i++) {
      if (formula.charAt(i) == '\'') {
        quotationNum++;
      }
      if ((formula.charAt(i) == '(') && (quotationNum % 2 == 0)) {
        left++;
      }
      if ((formula.charAt(i) == ')') && (quotationNum % 2 == 0)) {
        right++;
      }
    }
    if ((quotationNum % 2 == 0) && (left == right)) {
      return false;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.inQuotation Over");
    return true;
  }

  public boolean isBiJiaoFu(String st)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isBiJiaoFu");
    boolean isOprater = false;

    if ((st.equals("=")) || (st.equals("<=")) || (st.equals(">=")) || (st.equals("<")) || (st.equals(">")) || (st.equals("<>")) || (st.equals("!=")) || (st.equalsIgnoreCase("is")))
    {
      isOprater = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isBiJiaoFu Over");
    return isOprater;
  }

  public boolean isCharType(String dataType)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCharType");
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isCharType Over");
    return isType(this.charTypes, dataType);
  }

  public boolean isDateType(String dataType)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isDateType");
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isDateType Over");
    return isType(this.dateTypes, dataType);
  }

  public boolean isFunctionName(String sWord, String nextWord)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName");
    boolean isFunc = false;

    if (((sWord.equalsIgnoreCase("left")) && (!nextWord.equalsIgnoreCase("outer"))) || ((sWord.equalsIgnoreCase("right")) && (!nextWord.equalsIgnoreCase("outer"))) || (((sWord.equalsIgnoreCase("square")) || (sWord.equalsIgnoreCase("cast")) || (sWord.equalsIgnoreCase("coalesce")) || (sWord.equalsIgnoreCase("ltrim")) || (sWord.equalsIgnoreCase("rtrim")) || (sWord.equalsIgnoreCase("patindex")) || (sWord.equalsIgnoreCase("len")) || (sWord.equalsIgnoreCase("round")) || (sWord.equalsIgnoreCase("convert")) || (sWord.equalsIgnoreCase("dateadd")) || (sWord.equalsIgnoreCase("datediff"))) && (nextWord.equals("("))))
    {
      isFunc = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isFunctionName Over");
    return isFunc;
  }

  public boolean isInnerJoin(String first, String second)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isInnerJoin");
    boolean isInnerJoin = false;
    if ((first.equalsIgnoreCase("inner")) && (second.equalsIgnoreCase("join"))) {
      isInnerJoin = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isInnerJoin Over");
    return isInnerJoin;
  }

  public boolean isNumber(String st)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumber");
    boolean isNumber = false;

    if ((st != null) && (st.trim().length() > 0))
      try {
        UFDouble ufd = new UFDouble(st.trim());
        if (ufd != null)
          isNumber = true;
      }
      catch (Exception e)
      {
      }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumber Over");
    return isNumber;
  }

  public boolean isNumberType(String dataType)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumberType");
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isNumberType Over");
    return isType(this.numberTypes, dataType);
  }

  public boolean isOuterJoin(String first, String second, String third)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isOuterJoin");
    boolean isOuterJoin = false;
    if (((first.equalsIgnoreCase("left")) || (first.equalsIgnoreCase("right"))) && (second.equalsIgnoreCase("outer")) && (third.equalsIgnoreCase("join"))) {
      isOuterJoin = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isOuterJoin Over");
    return isOuterJoin;
  }

  public boolean isSingleWord(String source, String dest, int index)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isSingleWord");
    boolean isSingleWord = false;

    if (((index <= 0) || (source.charAt(index - 1) == ' ')) && (
      (index + dest.length() >= source.length() - 1) || (source.charAt(index + dest.length()) == ' '))) {
      isSingleWord = true;
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isSingleWord Over");
    return isSingleWord;
  }

  public boolean isTableOtherName(String st)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isTableOtherName");
    boolean isTableOtherName = false;

    if ((!st.equalsIgnoreCase("on")) && (!st.equalsIgnoreCase("where")) && (!st.equalsIgnoreCase("inner")) && (!st.equalsIgnoreCase("left")) && (!st.equalsIgnoreCase("right")) && (!st.equalsIgnoreCase(",")))
    {
      isTableOtherName = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isTableOtherName Over");
    return isTableOtherName;
  }

  public boolean isType(String[] dataTypes, String type)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isType");
    type = type.trim();
    boolean isType = false;

    for (int i = 0; i < dataTypes.length; i++) {
      if (!type.equalsIgnoreCase(dataTypes[i]))
        continue;
      isType = true;
      break;
    }

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.isType Over");
    return isType;
  }

  public boolean joinNotEnd(String st)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.joinNotEnd");
    boolean joinNotEnd = false;

    if ((!st.equalsIgnoreCase("left")) && (!st.equalsIgnoreCase("right")) && (!st.equalsIgnoreCase("where")) && (!st.equalsIgnoreCase("order")) && (!st.equalsIgnoreCase("group")) && (!st.equalsIgnoreCase("inner")) && (!st.equalsIgnoreCase("union")) && (!st.equalsIgnoreCase("on")) && (!st.equalsIgnoreCase(",")))
    {
      joinNotEnd = true;
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.joinNotEnd Over");
    return joinNotEnd;
  }

  public void setDestDbType(int dbType)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setDestDbType");
    if ((dbType >= 0) && (dbType <= 3))
      this.m_iDestinationDatabaseType = dbType;
    else
      this.m_iDestinationDatabaseType = 2;
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setDestDbType Over");
  }

  public void setSqlArray(String[] sqlArray)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlArray");

    this.m_asSqlWords = sqlArray;
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setSqlArray Over");
  }

  public String[] getSqlArray()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlArray");

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getSqlArray Over");
    return this.m_asSqlWords;
  }

  public String[] getTableNames()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getTableNames");
    String[] tablename = null;
    Vector v = new Vector();
    Hashtable ht = new Hashtable();
    if (this.m_asSqlWords == null)
      return null;
    if (!this.m_asSqlWords[0].equalsIgnoreCase("where"))
      return null;
    for (int i = 0; i < this.m_asSqlWords.length; i++) {
      if ((!Character.isLetter(this.m_asSqlWords[i].charAt(0))) || 
        (this.m_asSqlWords[i].indexOf(".") <= 0) || (this.m_asSqlWords[i].indexOf("'") >= 0) || (this.m_asSqlWords[i].indexOf("\"") >= 0))
        continue;
      ht.put(this.m_asSqlWords[i].substring(0, this.m_asSqlWords[i].indexOf(".")), this.m_asSqlWords[i].substring(0, this.m_asSqlWords[i].indexOf(".")));
    }

    if (!ht.isEmpty()) {
      Enumeration em = ht.elements();
      while (em.hasMoreElements()) {
        String table = (String)em.nextElement();
        v.addElement(table);
      }
      if (v.size() > 0) {
        tablename = new String[v.size()];
        v.copyInto(tablename);
      }
    }
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getTableNames Over");
    return tablename;
  }

  public static void main(String[] args)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.main");

    TranslatorObject transOb = new TranslatorObject();
    String sql = "where h.cbillid = b.cbillid and h.pk_corp = '1003' and upper(h.cbilltypecode) in ('I0', 'I1') and b.fpricemodeflag=5 and h.bdisableflag = 'N'";
    transOb.setSql(sql);
    transOb.getTableNames();
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.main Over");
  }

  public String[] parseTable(String[] asWords, String sTableName, String sTableAlias)
  {
    int iOffSet = 0;
    String othtable = "";
    String trueName = "";
    String[] sTable = null;
    boolean isOth = false;
    Vector vecTable = new Vector();
    while (iOffSet < asWords.length) {
      if ((iOffSet < asWords.length) && (asWords[iOffSet].equalsIgnoreCase("from"))) {
        int leftCount = 1;
        int rightCount = 0;
        String subfromSt = "";
        Vector tabVec = new Vector();
        iOffSet++;
        while ((iOffSet < asWords.length) && (!asWords[iOffSet].equalsIgnoreCase("where")))
        {
          if (asWords[iOffSet].equalsIgnoreCase("(")) {
            subfromSt = "(";
            while ((leftCount != rightCount) && (iOffSet < asWords.length)) {
              iOffSet++;

              if (asWords[iOffSet].equalsIgnoreCase("("))
                leftCount++;
              else if (asWords[iOffSet].equalsIgnoreCase(")")) {
                rightCount++;
              }

              subfromSt = subfromSt + " " + asWords[iOffSet];
            }

            tabVec.addElement(subfromSt);
            iOffSet++;
          }
          tabVec.addElement(asWords[iOffSet]);
          iOffSet++;
        }

        for (int newIndex = 0; newIndex < tabVec.size(); newIndex++) {
          othtable = tabVec.elementAt(newIndex).toString();
          trueName = othtable;
          isOth = false;

          if (!othtable.equalsIgnoreCase(sTableName)) {
            isOth = true;
            vecTable.addElement(othtable);
          }
          newIndex++;
          if (newIndex < tabVec.size()) {
            othtable = tabVec.elementAt(newIndex).toString();
            if (othtable.equalsIgnoreCase("as")) {
              newIndex++;
              othtable = tabVec.elementAt(newIndex).toString();
            }

            if (!othtable.equalsIgnoreCase(",")) {
              if (isOth) {
                vecTable.addElement(othtable);
              }
              else if ((sTableAlias != null) && (sTableAlias.trim().length() > 0) && 
                (!othtable.equalsIgnoreCase(sTableAlias))) {
                vecTable.addElement(trueName);
                vecTable.addElement(othtable);
              }

              newIndex++;
            }
          }
        }
      }

      iOffSet++;
    }
    sTable = new String[vecTable.size()];

    for (int i = 0; i < vecTable.size(); i++) {
      sTable[i] = ((String)vecTable.elementAt(i));
    }
    return sTable;
  }

  public boolean haveTab(String Sql, String[] v_table)
  {
    boolean MasterTab = false;
    int dotIndex = 0;
    int i = 0;
    String s = "";
    while (i < v_table.length) {
      s = v_table[i];
      dotIndex = Sql.indexOf(s.trim() + ".");
      if (dotIndex > 0)
      {
        MasterTab = true;
        return MasterTab;
      }

      i++;
    }
    return MasterTab;
  }

  public boolean haveTab(String Sql, String Table)
  {
    boolean MasterTab = false;
    if ((Table.trim() == null) || (Table.trim().length() == 0)) {
      return MasterTab;
    }

    int dotIndex = Sql.indexOf(Table + ".");
    if (dotIndex > 0)
    {
      MasterTab = true;
    }

    return MasterTab;
  }

  public boolean isMasterTab(String[] v_table, String sql)
  {
    boolean MasterTab = false;
    int i = 0;
    int dotIndex = 0;
    String s = "";
    dotIndex = sql.indexOf(".");
    if (dotIndex >= 0) {
      String tabName = sql.substring(0, dotIndex).trim().toLowerCase();
      while (i < v_table.length) {
        s = v_table[i];
        if (s.trim().equalsIgnoreCase(tabName)) {
          MasterTab = true;
          return MasterTab;
        }
        i++;
      }
    }
    return MasterTab;
  }

  public boolean isMasterTab(String Sql, String Table)
  {
    boolean MasterTab = false;

    int dotIndex = Sql.indexOf(".");
    if (dotIndex >= 0) {
      String tabName = Sql.substring(0, dotIndex).trim().toLowerCase();
      if (tabName.equalsIgnoreCase(Table)) {
        MasterTab = true;
      }

    }

    return MasterTab;
  }

  public int getDbVersion()
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDbVersion");

    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.getDbVersion Over");
    return this.m_DbVersion;
  }

  public void setDbVersion(int dbVersion)
  {
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setConnection");
    this.m_DbVersion = dbVersion;
    Logger.setThreadState("nc.bs.mw.sqltrans.TranslatorObject.setConnection Over");
  }
}