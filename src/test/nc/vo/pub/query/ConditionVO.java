package nc.vo.pub.query;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;

public class ConditionVO extends ValueObject
  implements IQueryConstants
{
  public static final String SEPARATOR_INNER = ":";
  public static final String SEPARATOR_OUTTER = ";";
  private boolean logic = true;
  private boolean noLeft = true;
  private boolean noRight = true;
  private UFBoolean ifDesc;
  private UFBoolean ifSysFunc;
  private String tableCode;
  private String tableName;
  private String fieldCode;
  private String fieldName;
  private String operaCode;
  private String operaName;
  private String value;
  private String comboType = "N";
  private RefResultVO refResult;
  private int dataType;
  private int orderSequence;
  private int comboIndex;
  private boolean fixCondition = false;
  private static final long serialVersionUID = -6061741910913318960L;
  public final String DISP_AND = NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template", "UPP_Template-000515");

  public final String DISP_OR = NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template", "UPP_Template-000516");

  public ConditionVO()
  {
  }

  public ConditionVO(QueryConditionVO queryVO)
  {
    setFieldCode(queryVO.getFieldCode());
    setFieldName(queryVO.getFieldName());
    setTableCode(queryVO.getTableCode());
    setTableName(queryVO.getTableName());
    setDataType(queryVO.getDataType().intValue());
    setIfDesc(queryVO.getIfDesc());
    setOrderSequence(queryVO.getOrderSequence().intValue());
  }

  public String getChStr()
  {
    return getChStr(false);
  }

  public String getCHStr(ConditionVO[] conditions)
  {
    if ((conditions == null) || (conditions.length == 0)) {
      return null;
    }
    String chStr = "";

    if (conditions != null)
      for (int i = 0; i < conditions.length; i++)
        chStr = chStr + conditions[i].getChStr();
    chStr = chStr.trim();

    if (chStr.indexOf(" ") > 0)
      chStr = chStr.substring(chStr.indexOf(" "));
    else {
      chStr = null;
    }
    return chStr;
  }

  public String getChStr(boolean isMultiTable)
  {
    String str = "";
    if (this.logic)
      str = str + " " + this.DISP_AND;
    else {
      str = str + " " + this.DISP_OR;
    }
    if (this.noLeft)
      str = str + "";
    else {
      str = str + " (";
    }
    if (isMultiTable) {
      str = str + " (" + this.tableName + "." + this.fieldName;
    }
    else
    {
      str = str + " (" + this.fieldName;
    }

    if ((this.operaCode.equalsIgnoreCase("is null")) || (this.operaCode.equals("ISNULL")))
    {
      str = str + " " + NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template", "UPP_Template-000511") + ")";
    }
    else if ((this.operaCode.equalsIgnoreCase("is not null")) || (this.operaCode.equals("ISNOTNULL")))
    {
      str = str + " " + NCLangRes4VoTransl.getNCLangRes().getStrByID("_Template", "UPP_Template-000512") + ")";
    }
    else
    {
      str = str + " " + this.operaName;
      if (this.operaCode.trim().equalsIgnoreCase("in")) {
        str = str + " (" + this.value + "))";
      }
      else if ((this.dataType == 1) || (this.dataType == 2) || ((this.dataType == 6) && (this.comboType.equals("I"))))
      {
        str = str + " " + this.value + ")";
      }
      else str = str + " '" + this.value + "')";
    }
    if (this.noRight)
      str = str + "";
    else {
      str = str + " )";
    }
    return str;
  }

  public int getComboIndex()
  {
    return this.comboIndex;
  }

  public String getComboType()
  {
    return this.comboType;
  }

  public int getDataType()
  {
    return this.dataType;
  }

  public String getEntityName()
  {
    return null;
  }

  public String getFieldCode()
  {
    return this.fieldCode;
  }

  public String getFieldName()
  {
    return this.fieldName;
  }

  public String getFieldNoT()
  {
    if ((this.fieldCode != null) && (this.fieldCode.length() != 0)) {
      int index = this.fieldCode.indexOf(".");
      String field = null;
      if (index >= 0)
        field = this.fieldCode.substring(index + 1, this.fieldCode.length());
      else
        field = this.fieldCode;
      return field;
    }

    return null;
  }

  public UFBoolean getIfDesc()
  {
    return this.ifDesc;
  }

  public UFBoolean getIfSysFunc()
  {
    return this.ifSysFunc;
  }

  public boolean getLogic()
  {
    return this.logic;
  }

  public boolean getNoLeft()
  {
    return this.noLeft;
  }

  public boolean getNoRight()
  {
    return this.noRight;
  }

  public String getOperaCode()
  {
    return this.operaCode;
  }

  public String getOperaName()
  {
    return this.operaName;
  }

  public int getOrderSequence()
  {
    return this.orderSequence;
  }

  public RefResultVO getRefResult()
  {
    return this.refResult;
  }

  public String getSQLStr()
  {
    return getSQLStr(false);
  }

  public String getSQLStr(ConditionVO[] conditions)
  {
    System.out.println("entering ConditionVO.getSQLStr");
    if ((conditions == null) || (conditions.length == 0))
      return null;
    return getWhereSQL(conditions);
  }

  private String getSQLStr(boolean isNullWithSpace)
  {
    StringBuffer str = new StringBuffer();
    if (this.logic)
      str.append(" and");
    else {
      str.append(" or");
    }
    if (!this.noLeft) {
      str.append(" (");
    }
    boolean isAllNull = false;
    boolean isAllNotNull = false;
    if ((this.operaCode.trim().equalsIgnoreCase("ISNULL")) || ((isNullWithSpace) && (this.operaCode.trim().equalsIgnoreCase("is null"))))
    {
      isAllNull = true;
    } else if ((this.operaCode.trim().equalsIgnoreCase("ISNOTNULL")) || ((isNullWithSpace) && (this.operaCode.trim().equalsIgnoreCase("is not null"))))
    {
      isAllNotNull = true;
    }if ((isAllNull) && (!isDigital())) {
      str.append(" ((" + this.fieldCode + " is null) or (ltrim(rtrim(" + this.fieldCode + ")) = ''))");
    }
    else if ((isAllNotNull) && (!isDigital())) {
      str.append(" ((" + this.fieldCode + " is not null) and (ltrim(rtrim(" + this.fieldCode + ")) <> ''))");
    }
    else {
      String strOpera = this.operaCode.trim();
      if (isAllNull)
        strOpera = "is null";
      else if (isAllNotNull) {
        strOpera = "is not null";
      }
      String newValue = this.value;
      if ((this.operaCode.equalsIgnoreCase("like")) && (this.value.indexOf("%") < 0))
        newValue = "%" + newValue + "%";
      if (!isDigital()) {
        newValue = "'" + newValue + "'";
      }
      str.append(" (" + this.fieldCode + " " + strOpera);
      if ((!isNullWithSpace) && ((strOpera.equalsIgnoreCase("is null")) || (strOpera.equalsIgnoreCase("is not null"))))
      {
        str.append(")");
      }
      else if ((this.operaCode.trim().equalsIgnoreCase("in")) || (this.operaCode.trim().indexOf(" in") != -1)) {
        if ((this.value.startsWith("(")) && (this.value.endsWith(")")))
        {
          str.append(" " + this.value + ")");
        }
        else {
          str.append(" (" + newValue + "))");
        }
      }
      else
      {
        str.append(" " + newValue + ")");
      }
    }
    if (!this.noRight)
      str.append(" )");
    return str.toString();
  }

  public String getSQLStrForNull()
  {
    return getSQLStr(true);
  }

  public String getTableCodeForMultiTable()
  {
    return this.tableCode;
  }

  public String getTableName()
  {
    if ((this.fieldCode != null) && (this.fieldCode.length() != 0)) {
      int index = this.fieldCode.indexOf(".");
      String tableName = null;
      if (index >= 0)
        tableName = this.fieldCode.substring(0, index);
      return tableName;
    }

    return null;
  }

  public String getTableNameForMultiTable()
  {
    return this.tableName;
  }

  public String getValue()
  {
    return this.value;
  }

  public String getWhereSQL(ConditionVO[] conditions)
  {
    System.out.println("entering conditionvo.getWhereSQL");
    if ((conditions == null) || (conditions.length == 0))
      return null;
    List fixConditions = new ArrayList();
    List customConditions = new ArrayList();
    for (ConditionVO vo : conditions)
    {
      if (vo.isFixCondition())
      {
        fixConditions.add(vo);
      }
      else
      {
        customConditions.add(vo);
      }
    }
    System.out.println("fixcondition size:" + fixConditions.size());
    System.out.println("customConditions size" + customConditions.size());
    if ((!fixConditions.isEmpty()) && (!customConditions.isEmpty()))
    {
      return getWhereSqlHelper((ConditionVO[])fixConditions.toArray(new ConditionVO[0])) + " and (" + getWhereSqlHelper((ConditionVO[])customConditions.toArray(new ConditionVO[0])) + ")";
    }

    if ((fixConditions.isEmpty()) && (!customConditions.isEmpty()))
    {
      return getWhereSqlHelper((ConditionVO[])customConditions.toArray(new ConditionVO[0]));
    }
    if ((!fixConditions.isEmpty()) && (customConditions.isEmpty()))
    {
      return getWhereSqlHelper((ConditionVO[])fixConditions.toArray(new ConditionVO[0]));
    }
    return null;
  }
  private String getWhereSqlHelper(ConditionVO[] conditions) {
    if ((conditions == null) || (conditions.length == 0)) {
      return null;
    }
    String str = "";
    for (int i = 0; i < conditions.length; i++) {
      str = str + conditions[i].getSQLStr();
    }

    if (str.length() > 3) {
      try {
        str = str.substring(str.indexOf(" ", 1));
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
    }
    else {
      str = null;
    }

    return str;
  }

  public boolean isDigital()
  {
    return (this.dataType == 1) || (this.dataType == 2) || ((this.dataType == 6) && (this.comboType.equals("I")));
  }

  public static ConditionVO[] parseString(String strConditions)
  {
    if ((strConditions == null) || (strConditions.length() == 0)) {
      return null;
    }
    StringTokenizer st = new StringTokenizer(strConditions, ";", false);

    int count = st.countTokens();
    ConditionVO[] conditions = new ConditionVO[count];
    for (int i = 0; i < count; i++) {
      conditions[i] = parseStringToOne(st.nextToken());
    }
    return conditions;
  }

  private static ConditionVO parseStringToOne(String describer)
  {
    if ((describer == null) || (describer.length() == 0))
      return null;
    try
    {
      ConditionVO condition = new ConditionVO();
      StringTokenizer st = new StringTokenizer(describer, ":", false);

      String boolVal = st.nextToken();
      condition.setLogic(boolVal.equals("Y"));

      boolVal = st.nextToken();
      condition.setNoLeft(boolVal.equals("Y"));

      boolVal = st.nextToken();
      condition.setNoRight(boolVal.equals("Y"));

      boolVal = st.nextToken();
      condition.setIfDesc(new UFBoolean(boolVal.equals("Y")));

      boolVal = st.nextToken();
      condition.setIfSysFunc(new UFBoolean(boolVal.equals("Y")));

      String str = st.nextToken();
      condition.setTableCode(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setTableName(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setFieldCode(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setFieldName(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setOperaCode(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setOperaName(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setValue(str.equals("null") ? null : str);

      str = st.nextToken();
      condition.setComboType(str.equals("null") ? "N" : str);

      RefResultVO ref = new RefResultVO();
      str = st.nextToken();
      ref.setRefCode(str.equals("null") ? null : str);
      str = st.nextToken();
      ref.setRefName(str.equals("null") ? null : str);
      str = st.nextToken();
      ref.setRefPK(str.equals("null") ? null : str);
      if ((ref.getRefCode() != null) || (ref.getRefName() != null) || (ref.getRefPK() != null))
      {
        condition.setRefResult(ref);
      }

      condition.setDataType(Integer.parseInt(st.nextToken()));

      condition.setOrderSequence(Integer.parseInt(st.nextToken()));

      condition.setComboIndex(Integer.parseInt(st.nextToken()));

      return condition;
    }
    catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public void setComboIndex(int index)
  {
    this.comboIndex = index;
  }

  public void setComboType(String newComboType)
  {
    if (newComboType == null)
      this.comboType = "N";
    else
      this.comboType = newComboType;
  }

  public void setDataType(int newDataType)
  {
    this.dataType = newDataType;
  }

  public void setFieldCode(String newFieldCode)
  {
    this.fieldCode = newFieldCode;
  }

  public void setFieldName(String newFieldName)
  {
    this.fieldName = newFieldName;
  }

  public void setIfDesc(UFBoolean newIfDesc)
  {
    this.ifDesc = newIfDesc;
  }

  public void setIfSysFunc(UFBoolean newIfSysFunc)
  {
    this.ifSysFunc = newIfSysFunc;
  }

  public void setLogic(boolean b)
  {
    this.logic = b;
  }

  public void setNoLeft(boolean b)
  {
    this.noLeft = b;
  }

  public void setNoRight(boolean b)
  {
    this.noRight = b;
  }

  public void setOperaCode(String newOpera)
  {
    this.operaCode = newOpera;
  }

  public void setOperaName(String newOpera)
  {
    this.operaName = newOpera;
  }

  public void setOrderSequence(int newOrder)
  {
    this.orderSequence = newOrder;
  }

  public void setRefResult(RefResultVO result)
  {
    this.refResult = result;
  }

  public void setTableCode(String newTableCode)
  {
    this.tableCode = newTableCode;
  }

  public void setTableName(String newTableName)
  {
    this.tableName = newTableName;
  }

  public void setValue(String newValue)
  {
    this.value = newValue;
  }

  public static String toStringInfo(ConditionVO[] vos)
  {
    if ((vos == null) || (vos.length == 0))
      return null;
    StringBuffer sbuffer = new StringBuffer();
    for (int i = 0; i < vos.length; i++) {
      sbuffer.append(toStringOneInfo(vos[i]));
      sbuffer.append(";");
    }
    return sbuffer.toString();
  }

  private static String toStringOneInfo(ConditionVO con)
  {
    StringBuffer strBuf = new StringBuffer();
    String space = ":";

    String boolVal = con.getLogic() ? "Y" : "N";
    strBuf.append(boolVal + space);

    boolVal = con.getNoLeft() ? "Y" : "N";
    strBuf.append(boolVal + space);

    boolVal = con.getNoRight() ? "Y" : "N";
    strBuf.append(boolVal + space);

    boolVal = (con.getIfDesc() != null) && (con.getIfDesc().booleanValue()) ? "Y" : "N";

    strBuf.append(boolVal + space);

    boolVal = (con.getIfSysFunc() != null) && (con.getIfSysFunc().booleanValue()) ? "Y" : "N";

    strBuf.append(boolVal + space);

    strBuf.append((con.getTableCodeForMultiTable() == null) || (con.getTableCodeForMultiTable().length() == 0) ? "null" : con.getTableCodeForMultiTable());

    strBuf.append(space);
    strBuf.append((con.getTableNameForMultiTable() == null) || (con.getTableNameForMultiTable().length() == 0) ? "null" : con.getTableNameForMultiTable());

    strBuf.append(space);
    strBuf.append((con.getFieldCode() == null) || (con.getFieldCode().length() == 0) ? "null" : con.getFieldCode());

    strBuf.append(space);
    strBuf.append((con.getFieldName() == null) || (con.getFieldName().length() == 0) ? "null" : con.getFieldName());

    strBuf.append(space);
    strBuf.append((con.getOperaCode() == null) || (con.getOperaCode().length() == 0) ? "null" : con.getOperaCode());

    strBuf.append(space);
    strBuf.append((con.getOperaName() == null) || (con.getOperaName().length() == 0) ? "null" : con.getOperaName());

    strBuf.append(space);
    strBuf.append((con.getValue() == null) || (con.getValue().length() == 0) ? "null" : con.getValue());

    strBuf.append(space);
    strBuf.append((con.getComboType() == null) || (con.getComboType().length() == 0) ? "null" : con.getComboType());

    strBuf.append(space);

    if (con.getRefResult() == null) {
      strBuf.append("null:null:null:");
    } else {
      RefResultVO ref = con.getRefResult();
      strBuf.append((ref.getRefCode() == null) || (ref.getRefCode().length() == 0) ? "null" : ref.getRefCode());

      strBuf.append(space);
      strBuf.append((ref.getRefName() == null) || (ref.getRefName().length() == 0) ? "null" : ref.getRefName());

      strBuf.append(space);
      strBuf.append((ref.getRefPK() == null) || (ref.getRefPK().length() == 0) ? "null" : ref.getRefPK());

      strBuf.append(space);
    }

    strBuf.append(con.getDataType() + space + con.getOrderSequence() + space + con.getComboIndex() + space);

    return strBuf.toString();
  }

  public void validate()
    throws ValidationException
  {
  }

  public Object clone()
  {
    ConditionVO vo = (ConditionVO)super.clone();
    if (vo.getRefResult() != null)
    {
      vo.setRefResult((RefResultVO)vo.getRefResult().clone());
    }
    return vo;
  }
  public boolean isFixCondition() {
    return this.fixCondition;
  }
  public void setFixCondition(boolean fixCondition) {
    this.fixCondition = fixCondition;
  }
}