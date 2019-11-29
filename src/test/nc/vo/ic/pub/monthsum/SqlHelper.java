package nc.vo.ic.pub.monthsum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nc.vo.scm.pub.SCMEnv;

public class SqlHelper
{
  public static Object[] arrayAppend(Object[] sa, Object[] sa1)
  {
    if (sa == null)
      return sa1;
    if (sa1 == null) {
      return sa;
    }

    Object[] oa = (Object[])(Object[])Array.newInstance(sa[0].getClass(), sa.length + sa1.length);

    System.arraycopy(sa, 0, oa, 0, sa.length);
    System.arraycopy(sa1, 0, oa, sa.length, sa1.length);

    return oa;
  }

  public static void main(String[] args)
  {
    byte i1 = 8;
    byte i2 = 4;
    int i3 = i1 | i2;

    String s = " select i,j, , ,,,from a ,where  ";

    SCMEnv.out(deal_quote(s));

    String xxx = " ,test, ";
    String[] sa = splitString(xxx, ",");

    int y = 0;

    y = 1;

    String sss = "x and and and  y";
    String ooo = deal_and(sss);

    int z = 0;
    z = 1;
  }

  private static void test2()
  {
    String inputStr = "ab12 cd efg34";
    String patternStr = "([a-zA-Z]+[0-9]+)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(inputStr);

    StringBuffer buf = new StringBuffer();
    boolean found = false;
    while ((found = matcher.find())) {
      matcher.start();
      matcher.end();
      inputStr.substring(matcher.start(), matcher.end());

      String replaceStr = matcher.group();

      replaceStr = replaceStr.toUpperCase();

      matcher.appendReplacement(buf, replaceStr);
    }
    matcher.appendTail(buf);

    String result = buf.toString();
  }

  private static void test1()
  {
    CharSequence inputStr = "ab12 cd efg34";
    String patternStr = "([a-zA-Z]+[0-9]+)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(inputStr);

    StringBuffer buf = new StringBuffer();
    boolean found = false;
    while ((found = matcher.find()))
    {
      String replaceStr = matcher.group();

      replaceStr = replaceStr.toUpperCase();

      matcher.appendReplacement(buf, replaceStr);
    }
    matcher.appendTail(buf);

    String result = buf.toString();
  }

  public static String[] splitString(String dealStr, String delimiter)
  {
    StringTokenizer tmpStrToken = new StringTokenizer(dealStr, delimiter);
    String[] retString = new String[tmpStrToken.countTokens()];
    int index = 0;
    while (tmpStrToken.hasMoreTokens())
    {
      String s = tmpStrToken.nextElement().toString();
      retString[(index++)] = s.trim();
    }

    return retString;
  }

  public static String addTableAlias(String sql, String alias)
  {
    if ((sql == null) || (alias == null))
      return sql;
    String[] sa = splitString(sql, ",");

    for (int i = 0; i < sa.length; i++) {
      if ((sa[i] == null) || (sa[i].length() == 0))
        continue;
      int index = sa[i].indexOf(".");
      if (index == -1)
        sa[i] = (alias + "." + sa[i]);
      else {
        sa[i] = (alias + "." + sa[i].substring(index + 1));
      }
    }

    return getStr(sa);
  }

  public static String getStr(String[] saStr) {
    StringBuffer sbRet = new StringBuffer();
    int i = 0;
    for (; i < saStr.length - 1; i++) {
      sbRet.append(saStr[i]);
      sbRet.append(",");
    }
    sbRet.append(saStr[i]);
    return sbRet.toString();
  }

  public static String formInClause(String[] values)
  {
    if ((values == null) || (values.length <= 0)) return null;

    int size = values.length;
    if (size == 0) return null;
    StringBuffer sbRet = new StringBuffer("(");
    for (int i = 0; i < size; i++) {
      if (i == 0) {
        sbRet.append("'" + values[i] + "'");
      }
      else
        sbRet.append(",'" + values[i] + "'");
    }
    sbRet.append(")");
    return sbRet.toString();
  }

  public static String replace(String sql, QryCondStr keyValue)
  {
    if ((keyValue == null) || (sql == null))
      return sql;
    if ((keyValue.getNames() == null) || (keyValue.getNames().length == 0))
      return sql;
    if ((keyValue.getValues() == null) || (keyValue.getValues().length == 0)) {
      return sql;
    }
    String result = "";
    for (int i = 0; i < keyValue.getNames().length; i++)
    {
      Pattern p = Pattern.compile(keyValue.getNames()[i]);
      Matcher mat = p.matcher(sql);

      if (keyValue.getValues()[i] != null)
        result = mat.replaceAll(keyValue.getValues()[i]);
      else {
        result = mat.replaceAll("");
      }
      sql = result;
    }

    SCMEnv.out(sql);
    return result;
  }

  public static String replace(String sql, Map keyValue) {
    if ((keyValue == null) || (sql == null))
      return sql;
    if ((keyValue == null) || (keyValue.size() == 0)) {
      return sql;
    }

    String result = "";
    Set keySet = keyValue.keySet();
    String[] names = (String[])(String[])keySet.toArray(new String[keySet.size()]);

    for (int i = 0; i < names.length; i++)
    {
      Pattern p = Pattern.compile(names[i]);
      Matcher mat = p.matcher(sql);

      if (keyValue.get(names[i]) != null)
        result = mat.replaceAll((String)keyValue.get(names[i]));
      else {
        result = mat.replaceAll("");
      }
      sql = result;
    }

    SCMEnv.out(sql);
    return result;
  }

  public static String replaceAnddeal(String sql, QryCondStr keyValue) {
    String s = replace(sql, keyValue);
    s = deal_quote(s);

    return s;
  }

  public static String deal_and(String sql)
  {
    Pattern p = Pattern.compile("and\\s*?and", 10);
    Matcher m = p.matcher(sql);
    String k = m.replaceAll("and");

    m = p.matcher(k);
    k = m.replaceAll("and");

    Pattern px = Pattern.compile("where\\s*?and", 10);
    Matcher mx = px.matcher(k);
    k = mx.replaceAll("where ");

    Pattern py = Pattern.compile("\\(\\s*?and", 10);
    Matcher my = py.matcher(k);
    k = my.replaceAll("( ");

    Pattern pz = Pattern.compile("select\\s*?,", 10);
    Matcher mz = pz.matcher(k);
    k = mz.replaceAll("select ");

    Pattern p1 = Pattern.compile("and\\s*?group", 10);
    Matcher m1 = p1.matcher(k);
    k = m1.replaceAll(" group");

    Pattern p2 = Pattern.compile("by\\s*?,", 10);
    Matcher m2 = p2.matcher(k);
    k = m2.replaceAll("by ");

    return k;
  }

  public static String replaceFirst(String sql, String str1, String str2) {
    Pattern p = Pattern.compile(str1, 10);
    Matcher mat = p.matcher(sql);
    String result = mat.replaceFirst(str2);
    return result;
  }

  public static String replace(String sql, String str1, String str2) {
    Pattern p = Pattern.compile(str1, 10);
    Matcher mat = p.matcher(sql);
    String result = mat.replaceAll(str2);
    return result;
  }

  public static String replaceAnddeal(String sql, String str1, String str2) {
    String s = replace(sql, str1, str2);
    s = deal_quote(s);
    return deal_and(s);
  }

  public static String trim_right_sign(String s, String n)
  {
    if ((s == null) || (n == null))
      return null;
    String t = s.trim();

    int len_n = n.length();
    int len_s = t.length();
    if (len_n >= len_s) {
      return s;
    }
    if (t.substring(len_s - len_n).equalsIgnoreCase(n)) {
      return t.substring(0, len_s - len_n);
    }
    return s;
  }

  public static String deal_quote(String sql)
  {
    Pattern p = Pattern.compile(",\\s*?,", 10);
    Matcher m = p.matcher(sql);
    String k = m.replaceAll(",");

    Matcher m1 = p.matcher(k);
    k = m1.replaceAll(",");

    Pattern p2 = Pattern.compile(",\\s*from", 10);
    Matcher m2 = p2.matcher(k);
    k = m2.replaceAll(" from");

    Pattern p3 = Pattern.compile(",\\s*where", 10);
    Matcher m3 = p3.matcher(k);
    k = m3.replaceAll(" where");

    Pattern p4 = Pattern.compile(",\\s*\\)", 10);
    Matcher m4 = p4.matcher(k);
    k = m4.replaceAll(" )");

    String r = trim_right_sign(k, ",");
    return r;
  }

  public static String getInSubSql(String sKeyField, ArrayList alResult)
  {
    int size = alResult.size();
    if (size == 0) return null;
    StringBuffer sbRet = new StringBuffer(" " + sKeyField + " in (");
    for (int i = 0; i < size; i++) {
      if (i == 0) {
        sbRet.append("'" + (String)alResult.get(i) + "'");
      }
      else
        sbRet.append(",'" + (String)alResult.get(i) + "'");
    }
    sbRet.append(")");
    return sbRet.toString();
  }

  public static String getInSubSql(String sKeyField, String[] saResult) {
    int size = saResult.length;
    if (size == 0) return null;
    StringBuffer sbRet = new StringBuffer(" " + sKeyField + " in (");
    for (int i = 0; i < size; i++) {
      if (i == 0) {
        sbRet.append("'" + saResult[i] + "'");
      }
      else
        sbRet.append(",'" + saResult[i] + "'");
    }
    sbRet.append(")");
    return sbRet.toString();
  }
}