package nc.vo.pub.querymodel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetData;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.queryengine.ITempTableHandler;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.reportquery.component.table.DSListenerEnable;
import nc.ui.reportquery.component.table.FormulaColDescriptor;
import nc.vo.com.utils.SystemProerty;
import nc.vo.com.utils.UUID;
import nc.vo.dbbase.tools.DataCrossProvider;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValueObject;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;

public class DatasetUtil
{
  public static FormulaParse parser = new FormulaParse();

  public static Hashtable getHashByDataset(StorageDataSet dataSet, String colKey, String colValue)
  {
    Hashtable hash = new Hashtable();
    int iRowCount = dataSet.getRowCount();
    if (iRowCount == 0) {
      return hash;
    }
    Object objKey = null;
    Object objValue = null;

    int iKeyType = dataSet.getColumn(colKey).getDataType();
    iKeyType = QueryUtil.variantTypeToSqlType(iKeyType);
    int iValueType = dataSet.getColumn(colValue).getDataType();
    iValueType = QueryUtil.variantTypeToSqlType(iValueType);

    dataSet.first();
    while (dataSet.inBounds())
    {
      objKey = fetchDataRow(dataSet, colKey, iKeyType);

      objValue = fetchDataRow(dataSet, colValue, iValueType);

      if ((objKey != null) && (objValue != null))
      {
        hash.put(objKey, objValue);
      }

      dataSet.next();
    }

    return hash;
  }

  public static FieldDef[] getFlddefByDataset(StorageDataSet dataSet)
  {
    int iColCount = dataSet.getColumnCount();
    FieldDef[] fds = new FieldDef[iColCount];
    for (int i = 0; i < iColCount; i++) {
      fds[i] = new FieldDef();
      fds[i].setGUID(new UUID().toString());

      String colname = dataSet.getColumn(i).getColumnName();

      String colname0 = colname;
      if (colname.length() != colname.getBytes().length)
        colname = "fld" + i;
      fds[i].setID(colname);

      fds[i].setDisplayName(dataSet.getColumn(i).getCaption());

      int iDataType = dataSet.getColumn(i).getDataType();
      iDataType = QueryUtil.variantTypeToSqlType(iDataType);
      fds[i].setDataType(iDataType);

      String str = QueryUtil.convDataType2DB(iDataType);
      boolean bChar = (str.endsWith("char")) || (str.endsWith("char2"));

      int iLength = dataSet.getColumn(i).getPrecision();
      if ((bChar) && (iLength == -1))
      {
        dataSet.first();
        while (dataSet.inBounds()) {
          Object objRow = fetchDataRow(dataSet, colname0, iDataType);
          int iTemp = objRow == null ? 0 : objRow.toString().getBytes().length;

          if (iTemp > iLength) {
            iLength = iTemp;
          }
          dataSet.next();
        }
        if (iLength == -1) {
          iLength = 100;
        }

      }

      fds[i].setLength(iLength);

      if (!str.equals("decimal")) {
        continue;
      }
      fds[i].setPrecision(dataSet.getColumn(i).getScale());
    }

    return fds;
  }

  public static Vector getObjArrayByDataset(StorageDataSet dataSet)
  {
    int iRowCount = dataSet.getRowCount();
    if (iRowCount == 0) {
      return null;
    }

    Vector vec = new Vector();
    int iColCount = dataSet.getColumnCount();
    int[] iTypes = new int[iColCount];

    for (int i = 0; i < iColCount; i++) {
      int iDataType = dataSet.getColumn(i).getDataType();
      iTypes[i] = QueryUtil.variantTypeToSqlType(iDataType);
    }

    dataSet.first();
    while (dataSet.inBounds())
    {
      Object[] objRows = new Object[iColCount];
      for (int i = 0; i < iColCount; i++)
        objRows[i] = fetchDataRow(dataSet, i, iTypes[i]);
      vec.addElement(objRows);

      dataSet.next();
    }

    return vec;
  }

  public static CircularlyAccessibleValueObject[] getCAVOsByDataset(StorageDataSet dataSet)
  {
    int iRowCount = dataSet.getRowCount();
    if (iRowCount == 0) {
      return null;
    }
    Vector vec = new Vector();
    dataSet.first();
    while (dataSet.inBounds()) {
      DataRowAdapter dra = new DataRowAdapter(dataSet);
      vec.addElement(dra);

      dataSet.next();
    }

    int iSize = vec.size();
    CircularlyAccessibleValueObject[] caVOs = new CircularlyAccessibleValueObject[iSize];
    vec.copyInto(caVOs);
    return caVOs;
  }

  public static SelectFldVO[] getSelectFldByDataset(Column[] cols)
  {
    int iColCount = cols == null ? 0 : cols.length;
    SelectFldVO[] sfs = new SelectFldVO[iColCount];
    for (int i = 0; i < iColCount; i++) {
      sfs[i] = new SelectFldVO();

      String colname = cols[i].getColumnName();
      sfs[i].setFldalias(colname);
      sfs[i].setExpression(colname);

      sfs[i].setFldname(cols[i].getCaption());

      int iDataType = QueryUtil.variantTypeToSqlType(cols[i].getDataType());

      sfs[i].setColtype(new Integer(iDataType));
    }
    return sfs;
  }

  public static Column[] getColumnByMrsMetadata(MemoryResultSetMetaData mrsmd)
  {
    Column[] columns = null;
    try {
      int iLen = mrsmd.getColumnCount();
      columns = new Column[iLen];
      for (int i = 0; i < iLen; i++) {
        columns[i] = new Column();
        columns[i].setColumnName(mrsmd.getColumnName(i + 1));
        columns[i].setCaption(mrsmd.getColumnName(i + 1));
        columns[i].setDataType(QueryUtil.sqlTypeToVariantType(mrsmd.getColumnType(i + 1)));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return columns;
  }

  public static void makeDataRow(DataRow row, String str, int iColIndex, int iColType)
  {
    boolean bNull = str == null;
    boolean bEmpty = (!bNull) && (str.trim().equals(""));
    if (bNull)
      row.setVariant(iColIndex, Variant.nullVariant);
    else if (iColType == 4)
      row.setInt(iColIndex, bEmpty ? 0 : new Integer(str).intValue());
    else if (iColType == 3)
      row.setShort(iColIndex, bEmpty ? 0 : new Integer(str).shortValue());
    else if (iColType == 2)
      row.setByte(iColIndex, bEmpty ? 0 : new Integer(str).byteValue());
    else if (iColType == 7) {
      row.setDouble(iColIndex, bEmpty ? 0.0D : new Double(str).doubleValue());
    }
    else if (iColType == 6)
      row.setFloat(iColIndex, bEmpty ? 0.0F : new Float(str).floatValue());
    else if (iColType == 10) {
      row.setBigDecimal(iColIndex, bEmpty ? new BigDecimal(0) : new BigDecimal(str));
    }
    else
      row.setString(iColIndex, str);
  }

  public static Object fetchDataRow(StorageDataSet dataSet, int iColIndex, int iColType)
  {
    Object obj = null;
    if (QueryUtil.isNumberType(iColType)) {
      switch (iColType) {
      case 4:
        obj = new Integer(dataSet.getInt(iColIndex));
        break;
      case -6:
        obj = new Integer(dataSet.getByte(iColIndex));
        break;
      case 5:
        obj = new Integer(dataSet.getShort(iColIndex));
        break;
      case 6:
        obj = new UFDouble(dataSet.getFloat(iColIndex));
        break;
      case 8:
        obj = new UFDouble(dataSet.getDouble(iColIndex));
        break;
      case -5:
      case -4:
      case -3:
      case -2:
      case -1:
      case 0:
      case 1:
      case 2:
      case 3:
      case 7:
      default:
        obj = new UFDouble(dataSet.getBigDecimal(iColIndex));
        break;
      }
    }
    else {
      obj = String.valueOf(dataSet.getString(iColIndex));
    }
    return obj;
  }

  public static Object fetchDataRow(StorageDataSet dataSet, String colKey, int iColType)
  {
    Object obj = null;
    if (dataSet.isNull(colKey))
      obj = null;
    else if (QueryUtil.isNumberType(iColType)) {
      switch (iColType) {
      case 4:
        obj = new Integer(dataSet.getInt(colKey));
        break;
      case -6:
        obj = new Integer(dataSet.getByte(colKey));
        break;
      case 5:
        obj = new Integer(dataSet.getShort(colKey));
        break;
      case 6:
        obj = new UFDouble(dataSet.getFloat(colKey));
        break;
      case 8:
        obj = new UFDouble(dataSet.getDouble(colKey));
        break;
      case -5:
      case -4:
      case -3:
      case -2:
      case -1:
      case 0:
      case 1:
      case 2:
      case 3:
      case 7:
      default:
        obj = new UFDouble(dataSet.getBigDecimal(colKey));
        break;
      }
    }
    else {
      obj = String.valueOf(dataSet.getString(colKey));
    }
    return obj;
  }

  public static void printDataset(StorageDataSet dataset)
  {
    if (!dataset.isOpen()) {
      dataset.open();
    }
    Column[] cols = dataset.getColumns();
    System.out.println("-------------Column------------");
    for (int i = 0; i < cols.length; i++)
      System.out.print(cols[i].getColumnName() + "     ");
    System.out.println();
    dataset.first();
    while (dataset.inBounds()) {
      for (int j = 0; j < cols.length; j++) {
        System.out.print(cols[j].getColumnName() + "= ");
        Variant variant = new Variant();
        dataset.getVariant(j, variant);
        if (variant == null)
          System.out.print("NULL  ");
        else
          System.out.print(variant + "  ");
      }
      System.out.println();
      dataset.next();
    }
  }

  public static Object[][] getObjArrayByDataset1(StorageDataSet dataSet)
  {
    Vector vec = getObjArrayByDataset(dataSet);
    if (vec == null) {
      return (Object[][])null;
    }
    Object[][] objArray = new Object[vec.size()][];
    vec.copyInto(objArray);
    return objArray;
  }

  public static Object changeObject2CorrectType(Object result, int type)
  {
    Object obj = null;
    if (result != null) {
      switch (type) {
      case 3:
        if ((result instanceof UFDouble)) {
          obj = new Short(((UFDouble)result).shortValue());
        } else if ((result instanceof Integer)) {
          obj = new Short(((Integer)result).shortValue());
        } else if ((result instanceof Double)) {
          obj = new Short(((Double)result).shortValue());
        } else if ((result instanceof Short)) {
          obj = result;
        } else {
          String str = result.toString();
          int index = str.indexOf(".");
          str = str.substring(0, index > 0 ? index : str.length());
          obj = Short.valueOf(str);
        }
        break;
      case 4:
        if ((result instanceof UFDouble)) {
          obj = new Integer(((UFDouble)result).intValue());
        } else if ((result instanceof Integer)) {
          obj = result;
        } else if ((result instanceof Double)) {
          obj = new Integer(((Double)result).intValue());
        } else {
          String str = result.toString();
          int index = str.indexOf(".");
          str = str.substring(0, index > 0 ? index : str.length());
          obj = Integer.valueOf(str);
        }
        break;
      case 7:
        if ((result instanceof UFDouble))
          obj = new Double(((UFDouble)result).getDouble());
        else if ((result instanceof Integer))
          obj = new Double(((Integer)result).intValue());
        else if ((result instanceof Double))
          obj = result;
        else {
          obj = Double.valueOf(result.toString());
        }
        break;
      case 10:
        if ((result instanceof UFDouble))
          obj = new BigDecimal(((UFDouble)result).getDouble());
        else if ((result instanceof Integer))
          obj = new BigDecimal(((Integer)result).intValue());
        else if ((result instanceof Double))
          obj = new BigDecimal(((Double)result).doubleValue());
        else {
          obj = new BigDecimal(result.toString());
        }
        break;
      case 11:
        if ((result instanceof Boolean))
          obj = result;
        else {
          obj = new Boolean(Double.parseDouble(result.toString()) > 0.0D);
        }
        break;
      case 16:
        obj = result.toString();
        break;
      case 5:
      case 6:
      case 8:
      case 9:
      case 12:
      case 13:
      case 14:
      case 15:
      default:
        obj = result;
      }
    }

    return obj;
  }

  public static Object getEquatingNullValue(int type)
  {
    Object obj = "";
    switch (type) {
    case 3:
        obj = new Short((short)0);
 //     obj = new Short(0);
      break;
    case 4:
      obj = new Integer(0);
      break;
    case 7:
      obj = new Double(0.0D);
      break;
    case 11:
      obj = new Boolean(false);
      break;
    case 10:
      obj = new BigDecimal(0.0D);
      break;
    case 6:
      obj = new Float(0.0F);
      break;
    case 5:
      obj = new Long(0L);
      break;
    case 8:
    case 9:
    }
    return obj;
  }

  public static StorageDataSet getDatasetByObjArray(Object[][] objsArray, Column[] cols)
  {
    int iRowCount = objsArray == null ? 0 : objsArray.length;

    int iColCount = cols == null ? 0 : cols.length;
    if (iColCount == 0) {
      return null;
    }
    StorageDataSet sds = new StorageDataSet();
    sds.setColumns(cols);
    sds.open();
    for (int i = 0; i < iRowCount; i++)
    {
      DataRow row = new DataRow(sds);
      int iRowLen = objsArray[i] == null ? 0 : objsArray[i].length;
      for (int j = 0; j < iColCount; j++) {
        String str = (j >= iRowLen) || (objsArray[i][j] == null) ? null : objsArray[i][j].toString();

        int iColType = cols[j].getDataType();

        makeDataRow(row, str, j, iColType);
      }

      sds.addRow(row);
    }
    sds.first();
    return sds;
  }

  public static StorageDataSet getDatasetByVector(Vector vec, Column[] cols)
  {
    int iRowCount = vec.size();  
    int iColCount = cols == null ? 0 : cols.length;
    if (iColCount == 0) {
      return null;
    }
    StorageDataSet sds = new StorageDataSet();
    sds.setColumns(cols);
    sds.open();
    for (int i = 0; i < iRowCount; i++) {
      Vector vecRow = (Vector)vec.elementAt(i);

      DataRow row = new DataRow(sds);
      for (int j = 0; j < iColCount; j++) {
        Object obj = vecRow.elementAt(j);
        String str = obj == null ? null : obj.toString();
        int iColType = cols[j].getDataType();

        makeDataRow(row, str, j, iColType);
      }

      sds.addRow(row);
    }
    sds.first();
    return sds;
  }

  public static StorageDataSet getDatasetByVOs(ValueObject[] vos)
  {
    int iRowCount = vos == null ? 0 : vos.length;
    if (iRowCount == 0) {
      return null;
    }
    Class cls = vos[0].getClass();

    StorageDataSet sds = null;
    try {
      BeanInfo info = getBeanInfo(cls);
      PropertyDescriptor[] pds = info.getPropertyDescriptors();

      pds = filterDesc(pds);

      int iColCount = pds == null ? 0 : pds.length;
      if (iColCount == 0) {
        return null;
      }
      Column[] cols = new Column[iColCount];
      Method[] methods = new Method[iColCount];
      for (int i = 0; i < iColCount; i++)
      {
        int iType = class2VariantType(pds[i].getPropertyType());

        String name = pds[i].getName();

        methods[i] = pds[i].getReadMethod();

        cols[i] = new Column();
        cols[i].setColumnName(name);
        cols[i].setCaption(name);
        cols[i].setDataType(iType);
      }

      sds = new StorageDataSet();
      sds.setColumns(cols);
      sds.open();
      for (int i = 0; i < iRowCount; i++)
      {
        DataRow row = new DataRow(sds);
        for (int j = 0; j < iColCount; j++) {
          Object obj = methods[j].invoke(vos[i], (Object[])null);
          String str = obj == null ? null : obj.toString();

          int iColType = cols[j].getDataType();

          makeDataRow(row, str, j, iColType);
        }

        sds.addRow(row);
      }
      sds.first();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sds;
  }

  public static StorageDataSet getDatasetByCAVOs(CircularlyAccessibleValueObject[] vos)
  {
    int iRowCount = vos == null ? 0 : vos.length;
    if (iRowCount == 0) {
      System.out.println("DatasetUtil.getDatasetByCAVOs：行数为0");
      return null;
    }

    Class cls = vos[0].getClass();

    StorageDataSet sds = null;
    try {
      String[] names = vos[0].getAttributeNames();

      int iColCount = names == null ? 0 : names.length;

      Vector vecName = new Vector();
      Vector vecCol = new Vector();
      for (int i = 0; i < iColCount; i++) {
        try
        {
          Column col = new Column();
          col.setColumnName(names[i]);
          col.setCaption(names[i]);

          vecName.addElement(names[i]);
          vecCol.addElement(col);

          Field fld = null;
          try {
            fld = cls.getDeclaredField("m_" + names[i]);
          } catch (NoSuchFieldException e) {
            fld = cls.getDeclaredField(names[i]);
          }
          int iType = class2VariantType(fld.getType());

          col.setDataType(iType);
        } catch (Exception e) {
          System.out.println("属性" + names[i] + "似不存在于类声明" + e);
        }
      }

      iColCount = vecName.size();
      if (iColCount == 0) {
        System.out.println("DatasetUtil.getDatasetByCAVOs：列数为0");
        return null;
      }

      names = new String[iColCount];
      vecName.copyInto(names);
      Column[] cols = new Column[iColCount];
      vecCol.copyInto(cols);

      sds = new StorageDataSet();
      sds.setColumns(cols);
      sds.open();
      for (int i = 0; i < iRowCount; i++)
      {
        DataRow row = new DataRow(sds);
        for (int j = 0; j < iColCount; j++) {
          Object obj = vos[i].getAttributeValue(names[j]);
          String str = obj == null ? null : obj.toString();
          int iColType = cols[j].getDataType();

          makeDataRow(row, str, j, iColType);
        }

        sds.addRow(row);
      }
      sds.first();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sds;
  }

  public static StorageDataSet getDatasetByMrs(MemoryResultSet mrs)
  {
    StorageDataSet sds = null;
    try {
      Column[] cols = getColumnByMrsMetadata(mrs.getMetaData0());
      int iColCount = cols == null ? 0 : cols.length;
      if (iColCount == 0)
        return null;
      ArrayList al = mrs.getResultArrayList();
      int iRowCount = al.size();

      sds = new StorageDataSet();
      sds.setColumns(cols);
      sds.open();
      for (int i = 0; i < iRowCount; i++) {
        ArrayList alRow = (ArrayList)al.get(i);
        int iRowSize = alRow.size();

        DataRow row = new DataRow(sds);
        for (int j = 0; j < iColCount; j++) {
          Object obj = j < iRowSize ? alRow.get(j) : null;
          String str = obj == null ? null : obj.toString();
          int iColType = cols[j].getDataType();

          makeDataRow(row, str, j, iColType);
        }

        sds.addRow(row);
      }
      sds.first();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sds;
  }

  public static boolean addFormulaCol2DS(StorageDataSet dataSet, FormulaColDescriptor formulaColDsp)
  {
    try
    {
      Column newCol = new Column(formulaColDsp.getColAlias(), formulaColDsp.getColName(), formulaColDsp.getColType());

      dataSet.addColumn(newCol);
      calColVal(dataSet, newCol.getColumnName(), formulaColDsp.getColType(), formulaColDsp.getColExp());

      return true;
    }
    catch (Exception e) {
      System.out.println("添加公式出错");
    }return false;
  }

  public static void aggGroupDataset(StorageDataSet ds, String groupCol, String[] aggCols, String hejiCol)
  {
    Column[] cols = ds.getColumns();
    int iColCount = cols == null ? 0 : cols.length;

    int[] iDataTypes = new int[iColCount];
    for (int i = 0; i < iColCount; i++) {
      iDataTypes[i] = cols[i].getDataType();
    }
    if (aggCols == null) {
      aggCols = getCrossFldnameByDataset(cols);

      if (aggCols != null) {
        for (int i = 0; i < iColCount; i++) {
          if (!cols[i].getCaption().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UC000-0001146")))
          {
            continue;
          }

          int iALen = aggCols.length;
          String[] tempAggCols = new String[iALen + 1];
          System.arraycopy(aggCols, 0, tempAggCols, 0, iALen);
          tempAggCols[iALen] = cols[i].getColumnName();
          aggCols = tempAggCols;
          break;
        }
      }
    }
    int iAggLen = aggCols == null ? 0 : aggCols.length;
    int[] iAggTypes = new int[iAggLen];
    for (int i = 0; i < iAggLen; i++) {
      iAggTypes[i] = ds.findOrdinal(aggCols[i]);
    }
    double[] dAggCols = null;
    Hashtable hashGroup = new Hashtable();
    Vector vecGroup = new Vector();

    ds.open();
    while (ds.inBounds()) {
      String group = ds.getString(groupCol);
      if (group == null)
        continue;
      if (hashGroup.containsKey(group)) {
        dAggCols = (double[])(double[])hashGroup.get(group);
      }
      else {
        dAggCols = new double[iAggLen];
        Arrays.fill(dAggCols, 0.0D);
        hashGroup.put(group, dAggCols);
        vecGroup.addElement(group);
      }
      for (int i = 0; i < iAggLen; i++) {
        int iDataType = QueryUtil.variantTypeToSqlType(iDataTypes[iAggTypes[i]]);

        Object obj = fetchDataRow(ds, aggCols[i], iDataType);

        if ((obj instanceof Integer)) {
          Integer iTemp = (Integer)obj;
          dAggCols[i] += iTemp.doubleValue();
        } else if ((obj instanceof UFDouble)) {
          UFDouble dTemp = (UFDouble)obj;
          dAggCols[i] += dTemp.doubleValue();
        }
      }

      ds.next();
    }

    int iSize = vecGroup.size();
    for (int i = 0; i < iSize; i++) {
      DataRow row = new DataRow(ds);

      String group = vecGroup.elementAt(i).toString();
      dAggCols = (double[])(double[])hashGroup.get(group);

      row.setString(groupCol, group);

      if ((i == 0) && (hejiCol != null)) {
        row.setString(hejiCol, NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UC000-0001146"));
      }

      for (int j = 0; j < iAggLen; j++) {
        int iDataType = iDataTypes[iAggTypes[j]];

        if ((iDataType == 4) || (iDataType == 5)) {
          int iAggCol = (int)dAggCols[j];
          makeDataRow(row, String.valueOf(iAggCol), iAggTypes[j], iDataType);
        }
        else {
          makeDataRow(row, String.valueOf(dAggCols[j]), iAggTypes[j], iDataType);
        }
      }

      ds.addRow(row);
    }
  }

  public static void calColVal(DataSet dataSet, String colName, int colType, String colExp)
    throws Exception
  {
    calColVal(dataSet, colName, colType, colExp, null);
  }

  public static void calColVal(DataSet dataSet, String colName, int colType, String colExp, DSListenerEnable listenerEnable)
    throws Exception
  {
    if ((colExp == null) || (colName == null))
      return;
    try {
      if (listenerEnable != null) {
        listenerEnable.enabled = false;
      }
      getFormulaParser().setExpress(colExp);
      VarryVO varry = getFormulaParser().getVarry();
      String[] params = varry.getVarry();
      int destOrdinal = dataSet.getColumn(colName).getOrdinal();
      Object[] results = null;
      if ((params != null) && (params.length > 0))
      {
        int rowCount = dataSet.getRowCount();

        for (int i = 0; i < params.length; i++) {
          ArrayList paramArray = new ArrayList(rowCount);

          if (dataSet.hasColumn(params[i]) == null)
            continue;
          int ordinal = dataSet.getColumn(params[i]).getOrdinal();
          int datatype = dataSet.getColumn(params[i]).getDataType();
          for (int j = 0; j < rowCount; j++) {
            Variant value = new Variant();
            dataSet.getVariant(ordinal, j, value);

            Object objValue = value.getAsObject();
            if (objValue == null)
              objValue = getEquatingNullValue(datatype);
            paramArray.add(objValue);
          }
          getFormulaParser().addVariable(params[i], paramArray);
        }

        results = getFormulaParser().getValueO();
      }
      else {
        Object result = getFormulaParser().getValueAsObject();
        results = new Object[dataSet.getRowCount()];
        Arrays.fill(results, result);
      }
      fillColVal(dataSet, destOrdinal, colType, results, listenerEnable);
    } catch (Exception e) {
      System.out.println("公式列出错");
      throw e;
    }
  }

  public static StorageDataSet convDatasetByCell(StorageDataSet ds, String[] cellFormulas, int[] iFormulaTypes)
  {
    Vector vec = getObjArrayByDataset(ds);
    int iSize = vec.size();
    Object[][] objs = new Object[iSize][];
    for (int i = 0; i < iSize; i++) {
      objs[i] = ((Object[])(Object[])vec.elementAt(i));
    }
    Hashtable hashIndexType = new Hashtable();
    FormulaTools.calCells(objs, cellFormulas, iFormulaTypes, hashIndexType);

    int iColCount = ds.getColumnCount();
    Column[] newCols = new Column[iColCount];
    for (int i = 0; i < iColCount; i++) {
      newCols[i] = ((Column)ds.getColumn(i).clone());
    }
    Enumeration keys = hashIndexType.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement().toString();
      int iKeyIndex = Integer.parseInt(key);
      int iNewType = ((Integer)hashIndexType.get(key)).intValue();
      iNewType = QueryUtil.sqlTypeToVariantType(iNewType);
      newCols[iKeyIndex].setDataType(iNewType);
      System.out.println("第" + iKeyIndex + "列类型转为" + iNewType);
    }

    StorageDataSet newDs = getDatasetByObjArray(objs, newCols);
    return newDs;
  }

  public static void createTable(StorageDataSet dataSet, String tableId, String dsNameForDef, String strDbtype)
    throws Exception
  {
    try
    {
      String sqlCreate = QueryUtil.getCreateSql(dataSet, tableId, strDbtype);

      String[] sqlInserts = QueryUtil.getInsertSqls(dataSet, tableId);

      ITempTableHandler name = (ITempTableHandler)NCLocator.getInstance().lookup(ITempTableHandler.class.getName());

      name.createTempTableBySQL(sqlCreate, sqlInserts, tableId, dsNameForDef);
    }
    catch (Exception e) {
      throw e;
    }
  }

  private static void fillColVal(DataSet ds, int ordinal, int type, Object[] results, DSListenerEnable listenerEnable)
    throws Exception
  {
    ds.first();
    int row = 0;

    int rowCount = results == null ? 0 : results.length;

    boolean berroroccur = false;
    while (ds.inBounds()) {
      Variant value = new Variant();
      Object obj = null;
      try {
        if (row >= rowCount) {
          break;
        }
        if ((row == rowCount - 1) && (listenerEnable != null)) {
          listenerEnable.enabled = true;
        }
        obj = changeObject2CorrectType(results[row], type);
        if (obj != null)
          value.setAsObject(obj, type);
        else {
          value.setNull(0);
        }
        ds.setVariant(ordinal, value);
      } catch (Exception e) {
        berroroccur = true;
      }
      ds.next();
      row++;
    }
    ds.first();

    if (berroroccur) {
      System.out.println("公式列类型有误");
      throw new DataSetException(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-001305"));
    }
  }

  private static String[] getCrossFldnameByDataset(Column[] cols)
  {
    int iColCount = cols == null ? 0 : cols.length;
    Vector vec = new Vector();
    for (int i = 0; i < iColCount; i++)
    {
      String colname = cols[i].getColumnName();
      if (colname.indexOf("ˉ") != -1)
        vec.addElement(colname);
    }
    String[] crossCols = new String[vec.size()];
    vec.copyInto(crossCols);
    return crossCols;
  }

  public static StorageDataSet getDatasetByDcp(DataCrossProvider dcp)
    throws Exception
  {
    StorageDataSet dataSet = new StorageDataSet();
    dataSet.setProvider(dcp);
    dataSet.open();

    int iColCount = dataSet.getColumnCount();
    dataSet.close();
    for (int i = 0; i < iColCount; i++) {
      Column col = dataSet.getColumn(i);

      String colname = col.getColumnName();

      if (colname.equals("&type")) {
        col.setCaption("");
      }
      col.setColumnName(colname);
    }
    dataSet.open();
    return dataSet;
  }

  public static StorageDataSet getDatasetForMergeElementCross(MergeHeaderVO mq, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    MergeElementTransformer met = new MergeElementTransformer();
    met.setMh(mq);
    met.setHashParam(hashParam);
    met.setDsName(dsNameForDef);
    met.execute();
    return met.getSds();
  }

  public static Object[] getDatasetForRotateCross(RotateCrossVO rc, StorageDataSet dataSet)
    throws Exception
  {
    FldgroupVO[] fldgroups = null;
    if (rc != null) {
      String[] strRows = rc.getStrRows();
      int iCrossRowCount = strRows == null ? 0 : getSplitRowCount(strRows);

      if (rc.isRowFh())
      {
        String strRow = QueryUtil.aggString(strRows, " ");
        strRows = new String[] { strRow };
      } else {
        System.out.println("未设置复合维度");
      }String[] strCols = rc.getStrCols();
      if (rc.isColFh())
      {
        String strCol = QueryUtil.aggString(strCols, " ");
        strCols = new String[] { strCol };
      }

      DataCrossProvider dcp = new DataCrossProvider(strRows, strCols, rc.getStrVals());

      String[] strSumCols = rc.getValTotals();
      dcp.setSumVals(strSumCols);
      dcp.setDataProviders(new Object[] { dataSet });

      if (rc.getColComparatorClass() != null) {
        try {
          Comparator comparator = (Comparator)Class.forName(rc.getColComparatorClass()).newInstance();

          dcp.setUserDefComparator(comparator);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      dataSet.first();
      dataSet = getDatasetByDcp(dcp);

      int iSumColCount = strSumCols == null ? 0 : strSumCols.length;
      String[] colNames = new String[dataSet.getColumnCount() - iCrossRowCount - iSumColCount];

      for (int i = 0; i < colNames.length; i++) {
        colNames[i] = dataSet.getColumn(i + iCrossRowCount).getColumnName();
      }
      fldgroups = ModelUtil.makeFldgroups(colNames, iCrossRowCount);
    }

    return new Object[] { dataSet, fldgroups };
  }

  private static int getSplitRowCount(String[] strRows) {
    int count = 0;
    for (int i = 0; i < strRows.length; i++) {
      String rowStr = strRows[i];
      if (rowStr != null) {
        StringTokenizer st = new StringTokenizer(rowStr, " ,|");
        count += st.countTokens();
      }
    }
    return count;
  }

  public static StorageDataSet getDatasetForSimpleCross(SimpleCrossVO[] scs, QueryBaseDef qbd, Hashtable hashParam)
    throws Exception
  {
    return getDatasetForSimpleCross(scs, qbd, hashParam, null);
  }

  public static StorageDataSet getDatasetForSimpleCross(SimpleCrossVO[] scs, QueryBaseDef qbd, Hashtable hashParam, Hashtable hashViewName)
    throws Exception
  {
    SimpleCrossTransformer sct = new SimpleCrossTransformer();
    sct.setScs(scs);
    sct.setQbd(qbd);
    sct.setHashParam(hashParam);
    sct.setHashViewName(hashViewName);
    sct.execute();
    return sct.getSds();
  }

  public static void levelSubDataset(StorageDataSet ds, String codeRule, String separator)
  {
    Column[] cols = ds.getColumns();
    int iColCount = cols == null ? 0 : cols.length;

    Vector vec = new Vector();
    Hashtable hashOldKeys = new Hashtable();
    for (int i = 0; i < iColCount; i++)
    {
      String colname = cols[i].getColumnName();
      String[] strs = QueryUtil.delimString(colname, "ˉ");

      int iLen = strs == null ? 0 : strs.length;
      if (iLen < 2)
        continue;
      CodeNameValueDescriptor cnvd = new CodeNameValueDescriptor();
      cnvd.setCode(strs[0]);
      cnvd.setName(strs[1]);
      cnvd.setValue(new Integer(0));
      vec.addElement(cnvd);
      hashOldKeys.put(colname, cnvd);

      cols[i].setCaption(strs[0] + "ˉ" + strs[1]);
    }

    CodeNameValueDescriptor[] cnvds = new CodeNameValueDescriptor[vec.size()];

    vec.copyInto(cnvds);

    CodeNameLevelTool cnlt = new CodeNameLevelTool();
    cnlt.setCnvds(cnvds);
    cnlt.setCodeRule(codeRule);
    Hashtable hashCode = cnlt.getHashStruc();
    String[] keys = cnlt.getColumnNames(hashCode);
    Vector vecNewkeys = new Vector();
    int iLen = keys == null ? 0 : keys.length;
    for (int i = 0; i < iLen; i++)
    {
      boolean bNewKey = true;
      for (int j = 0; j < iColCount; j++)
        if (cols[j].getColumnName().startsWith(keys[i])) {
          bNewKey = false;
          break;
        }
      if (bNewKey) {
        Column newCol = new Column(keys[i], keys[i], 10);
        ds.addColumn(newCol);
        vecNewkeys.addElement(keys[i]);
      }
    }

    int[] iDataTypes = new int[iColCount];
    for (int i = 0; i < iColCount; i++) {
      iDataTypes[i] = QueryUtil.variantTypeToSqlType(cols[i].getDataType());
    }

    while (ds.inBounds()) {
      vec = new Vector();
      for (int i = 0; i < iColCount; i++)
      {
        String colname = cols[i].getColumnName();
        if (hashOldKeys.containsKey(colname)) {
          CodeNameValueDescriptor cnvd = (CodeNameValueDescriptor)hashOldKeys.get(colname);

          cnvd.setValue(fetchDataRow(ds, colname, iDataTypes[i]));
          vec.addElement(cnvd);
        }
      }
      cnvds = new CodeNameValueDescriptor[vec.size()];
      vec.copyInto(cnvds);
      cnlt.setCnvds(cnvds);
      hashCode = cnlt.getHashStruc();
      for (int i = 0; i < vecNewkeys.size(); i++) {
        String newKey = vecNewkeys.elementAt(i).toString();

        String[] strs = QueryUtil.delimString(newKey, "ˉ");

        Hashtable hashName = (Hashtable)hashCode.get(strs[0]);

        BigDecimal bd = new BigDecimal(hashName.get(strs[1]).toString());
        bd = bd.setScale(4);
        ds.setBigDecimal(newKey, bd);
      }

      ds.next();
    }

    cols = ds.getColumns();
    iColCount = cols == null ? 0 : cols.length;

    int iOtherColCount = iColCount - iLen;

    int[] iColMaps = new int[iColCount];
    int s = 0;
    for (int i = 0; i < iColCount; i++) {
      String colName = cols[i].getColumnName();
      if (colName.indexOf("ˉ") == -1)
        iColMaps[i] = (s++);
      else {
        for (int j = 0; j < iLen; j++) {
          if (colName.startsWith(keys[j])) {
            iColMaps[i] = (iOtherColCount + j);
            break;
          }
        }
      }
    }
    Column[] newCols = new Column[iColCount];
    for (int i = 0; i < iColCount; i++) {
      newCols[iColMaps[i]] = cols[i];

      String caption = cols[i].getCaption();
      if (caption.indexOf("ˉ") != -1) {
        caption = StringUtil.replaceAllString(caption, "ˉ", separator);
      }
      else if (caption.endsWith(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UC000-0001146")))
      {
        caption = NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UC000-0001146");
      }
      newCols[iColMaps[i]].setCaption(caption);
    }
    ds.close();
    ds.setColumns(newCols);
  }

  public static StorageDataSet rotateDataset(StorageDataSet sds, String colHeader)
  {
    if (sds == null)
      return null;
    int iHeaderCol = sds.findOrdinal(colHeader);
    if (iHeaderCol == -1) {
      return null;
    }
    Vector vecArray = getObjArrayByDataset(sds);

    int iLen1 = vecArray.size();
    if (iLen1 == 0)
      return null;
    int iLen2 = ((Object[])(Object[])vecArray.elementAt(0)).length;
    if (iLen2 == 0)
      return null;
    Object[][] objsArray = new Object[iLen2 - 1][iLen1];
    Column[] cols = new Column[iLen1];
    for (int i = 0; i < iLen1; i++) {
      Object[] objTemps = (Object[])(Object[])vecArray.elementAt(i);
      boolean bNumberType = true;
      String strCaption = objTemps[iHeaderCol] == null ? "" : objTemps[iHeaderCol].toString();

      for (int j = 0; j < iLen2; j++) {
        if (j == iHeaderCol)
          continue;
        int j1 = j > iHeaderCol ? j - 1 : j;
        objsArray[j1][i] = objTemps[j];

        if ((objTemps[j] == null) || (objTemps[j].getClass().getSuperclass() == Number.class))
          continue;
        bNumberType = false;
      }

      cols[i] = new Column();
      cols[i].setColumnName("fld" + i);
      cols[i].setCaption(strCaption);
      cols[i].setDataType(bNumberType ? 10 : 16);
    }

    StorageDataSet newSds = getDatasetByObjArray(objsArray, cols);
    return newSds;
  }

  public static void tamperColname(StorageDataSet ds, int iRowCount)
  {
    int iColCount = ds.getColumnCount();
    ds.close();
    for (int i = iRowCount; i < iColCount; i++)
      ds.getColumn(i).setColumnName("fld_" + i);
    ds.open();
  }

  private static String getParentCode(String childCode, int[] iCodeRules, String rootDisp)
  {
    String parentCode = "";
    int iChildCodeLen = childCode == null ? 0 : childCode.length();
    if (iChildCodeLen != 0) {
      int s = 0;
      for (int i = 0; i < iCodeRules.length; i++) {
        s += iCodeRules[i];
        if (s == iChildCodeLen) {
          parentCode = childCode.substring(0, s - iCodeRules[i]);

          if (!parentCode.equals("")) break;
          parentCode = rootDisp; break;
        }
        if (s > iChildCodeLen)
          break;
      }
    }
    return parentCode;
  }

  public static StorageDataSet fillParentColAndRow(StorageDataSet dataSet, String parentCol, String childCol, String codeRule, String rootDisp)
  {
    int iRowCount = dataSet.getRowCount();
    int iColCount = dataSet.getColumnCount();
    if (dataSet.getRowCount() == 0)
      return null;
    Column[] cols = dataSet.getColumns();
    Object[][] objOrigins = getObjArrayByDataset1(dataSet);
    int iParentCol = dataSet.findOrdinal(parentCol);
    int iChildCol = dataSet.findOrdinal(childCol);
    String[] codeRules = QueryUtil.delimString(codeRule, "-");
    int iRuleLen = codeRules == null ? 0 : codeRules.length;
    int[] iCodeRules = new int[iRuleLen];
    for (int i = 0; i < iRuleLen; i++) {
      iCodeRules[i] = Integer.parseInt(codeRules[i]);
    }

    Hashtable hashCodeForAddRow = new Hashtable();
    for (int i = 0; i < iRowCount; i++)
    {
      String childCode = objOrigins[i][iChildCol] == null ? "" : objOrigins[i][iChildCol].toString();

      String parentCode = getParentCode(childCode, iCodeRules, rootDisp);

      objOrigins[i][iParentCol] = parentCode;

      if (parentCode.equals(""))
        continue;
      while ((!parentCode.equals(rootDisp)) && (!hashCodeForAddRow.containsKey(parentCode)))
      {
        String grandParentCode = getParentCode(parentCode, iCodeRules, rootDisp);

        if (grandParentCode.equals("")) {
          break;
        }
        hashCodeForAddRow.put(parentCode, grandParentCode);
        parentCode = grandParentCode;
      }

    }

    Vector vecNewRow = new Vector();
    Enumeration keys = hashCodeForAddRow.keys();
    while (keys.hasMoreElements())
    {
      String key = keys.nextElement().toString();
      String value = hashCodeForAddRow.get(key).toString();

      Object[] objs = new Object[iColCount];
      objs[iParentCol] = value;
      objs[iChildCol] = key;

      vecNewRow.addElement(objs);
    }
    int iSize = vecNewRow.size();
    Object[][] objNewRows = new Object[iSize][];
    vecNewRow.copyInto(objNewRows);

    Object[][] objArray = new Object[iRowCount + iSize][iColCount];
    System.arraycopy(objOrigins, 0, objArray, 0, iRowCount);
    if (iSize != 0) {
      System.arraycopy(objNewRows, 0, objArray, iRowCount, iSize);
    }
    Column[] newCols = new Column[iColCount];
    for (int i = 0; i < iColCount; i++) {
      newCols[i] = ((Column)cols[i].clone());
    }
    StorageDataSet ds = getDatasetByObjArray(objArray, newCols);

    return ds;
  }

  private static FormulaParse getFormulaParser()
  {
    if (SystemProerty.isOnServer()) {
      return new FormulaParse();
    }
    return parser;
  }

  public static void removeRowId(StorageDataSet dataset)
  {
    if (dataset != null) {
      int iColCount = dataset.getColumnCount();
      if (iColCount > 0) {
        Column col = dataset.getColumn(0);
        if (col.isRowId()) {
          System.out.println("Remove RowId");
          dataset.dropColumn(col);
        }
      }
    }
  }

  private static PropertyDescriptor[] filterDesc(PropertyDescriptor[] descriptors)
  {
    int iLen = descriptors == null ? 0 : descriptors.length;
    Vector vec = new Vector();
    for (int i = 0; i < iLen; i++) {
      PropertyDescriptor desc = descriptors[i];
      if ((desc.getWriteMethod() == null) || (desc.isExpert()) || (desc.isHidden())) {
        continue;
      }
      vec.addElement(desc);
    }
    int iSize = vec.size();
    PropertyDescriptor[] descriptorsNew = new PropertyDescriptor[iSize];
    if (iSize != 0)
      vec.copyInto(descriptorsNew);
    return descriptorsNew;
  }

  public static BeanInfo getBeanInfo(Class cls)
  {
    BeanInfo beanInfo = null;
    try
    {
      beanInfo = Introspector.getBeanInfo(cls, 3);
    }
    catch (IntrospectionException ex) {
      ex.printStackTrace();
    }
    return beanInfo;
  }

  private static int class2VariantType(Class cls)
  {
    int varType = 16;
    if (cls == Integer.class)
      varType = 4;
    else if (cls == UFDouble.class)
      varType = 7;
    return varType;
  }

  public static void reserveTopRow(StorageDataSet dataSet, int iTopRowCount)
  {
    int iRowCount = dataSet.getRowCount();
    if ((iRowCount == 0) || (iRowCount <= iTopRowCount))
      return;
    dataSet.first();
    while (dataSet.inBounds())
    {
      if (dataSet.getRow() >= iTopRowCount)
      {
        dataSet.deleteRow();
        continue;
      }
      dataSet.next();
    }
  }

  public static void assembleDataset(StorageDataSet dataset, SelectFldVO[] sfs)
  {
    if (sfs == null) {
      return;
    }

    Hashtable hashFldaliasDispname = new Hashtable();
    for (int i = 0; i < sfs.length; i++) {
      hashFldaliasDispname.put(sfs[i].getFldalias().toUpperCase(), sfs[i].getFldname());
    }

    int iColCount = dataset.getColumnCount();
    for (int i = 0; i < iColCount; i++) {
      String strColAlias = dataset.getColumn(i).getColumnName().toUpperCase();

      String strDispName = strColAlias;
      if (hashFldaliasDispname.containsKey(strColAlias))
        strDispName = hashFldaliasDispname.get(strColAlias).toString();
      else {
        System.out.println("未找到列<" + strColAlias + ">对应的显示名");
      }
      dataset.getColumn(i).setCaption(strDispName);
    }
  }

  public static StorageDataSet makeDataSet(DataSetDataWithColumn dsdwc)
  {
    StorageDataSet dataSet = new StorageDataSet();
    dsdwc.getDataSetData().loadDataSet(dataSet);

    for (int i = 0; i < dataSet.getColumnCount(); i++) {
      dataSet.getColumn(i).setCaption(dsdwc.getCaptions()[i]);
    }
    return dataSet;
  }
}