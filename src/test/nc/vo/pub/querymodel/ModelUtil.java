package nc.vo.pub.querymodel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSetData;
import com.borland.dx.dataset.MasterLinkDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.queryengine.IDataStructureInfo;
import nc.itf.uap.queryengine.IEmbedCodeUtil;
import nc.itf.uap.queryengine.IQEDataSourceInfo;
import nc.itf.uap.queryengine.IRecordsLoader;
import nc.itf.uap.queryengine.ITempTableHandler;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.JoinCondVO;
import nc.vo.iuforeport.businessquery.QEEnvParamBean;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryExecutor;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.core.ObjectNodeVO;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.dbbase.ScrollableDataSet;
import nc.vo.pub.ddc.datadict.DDCData;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.ForKeyVO;
import nc.vo.pub.ddc.datadict.TableDef;
import org.apache.commons.lang.ArrayUtils;

public class ModelUtil
{
  public static void refreshMaterTable(String tableId, Hashtable hashParam, String dsNameForDef, String dbType)
    throws Exception
  {
    try
    {
      if (tableId == null)
        return;
      int iIndex1 = tableId.indexOf("QIUE_");
      if (iIndex1 == -1)
        return;
      iIndex1 += "QIUE_".length();
      int iIndex2 = tableId.lastIndexOf("_");
      if (iIndex1 >= iIndex2)
        return;
      String queryId = tableId.substring(iIndex1, iIndex2);
      System.out.println(tableId + " -> " + queryId);

      String[] tableIdNameDbtypes = { tableId, null, dbType };
      ITempTableHandler name = (ITempTableHandler)NCLocator.getInstance().lookup(ITempTableHandler.class.getName());

      name.createMaterTable(queryId, hashParam, dsNameForDef, tableIdNameDbtypes);
    }
    catch (Exception e) {
      throw e;
    }
  }

  public static FldgroupVO[] makeFldgroups(String[] colNames, int iCrossRowCount)
    throws Exception
  {
    int iColCount = colNames == null ? 0 : colNames.length;
    if (iColCount == 0) {
      return new FldgroupVO[0];
    }

    String[][] headerNames = new String[iColCount][];
    int iLayerCount = 0;
    for (int j = 0; j < iColCount; j++)
    {
      headerNames[j] = QueryUtil.delimString(colNames[j], "ˉ");

      if (j == 0) {
        iLayerCount = headerNames[0].length;
      }
      for (int i = 0; i <= iLayerCount - 2; i++)
      {
        if ((headerNames[j][i] == null) || (headerNames[j][i].equals(""))) {
          headerNames[j][i] = " ";
        }
        for (int k = i - 1; k >= 0; k--) {
          headerNames[j][i] = (headerNames[j][k] + "ˉ" + headerNames[j][i]);
        }
      }

      headerNames[j][(iLayerCount - 1)] = String.valueOf(j + iCrossRowCount);
    }

    Vector vec = new Vector();
    int iGId = 0;
    for (int i = iLayerCount - 2; i >= 0; i--) {
      Vector vecTemp = new Vector();
      for (int j = 0; j <= iColCount; j++) {
        if ((j < iColCount) && ((j == 0) || (headerNames[j][i].equals(headerNames[(j - 1)][i]))))
        {
          if ((i != iLayerCount - 2) && (vecTemp.indexOf(headerNames[j][(i + 1)]) != -1))
            continue;
          vecTemp.addElement(headerNames[j][(i + 1)]);
        } else {
          int iSize = vecTemp.size();
          String strGroupName = headerNames[(j - 1)][i];

          if (iSize == 1) {
            String strItem1 = vecTemp.elementAt(0).toString();

            FldgroupVO fldGroup = new FldgroupVO();
            fldGroup.setGroupid(new Integer(iGId++));
            fldGroup.setGroupname(strGroupName);
            if (i == iLayerCount - 2)
              fldGroup.setGrouptype("0");
            else
              fldGroup.setGrouptype("2");
            fldGroup.setItem1(strItem1);
            if (i == 0)
              fldGroup.setToplevelflag("Y");
            else {
              fldGroup.setToplevelflag("N");
            }
            vec.addElement(fldGroup);
          } else {
            for (int k = 0; k < iSize - 1; k++) {
              String strItem1 = k == 0 ? vecTemp.elementAt(k).toString() : strGroupName;

              String strItem2 = vecTemp.elementAt(k + 1).toString();

              FldgroupVO fldGroup = new FldgroupVO();
              fldGroup.setGroupid(new Integer(iGId++));
              fldGroup.setGroupname(strGroupName);
              if ((k == 0) && (i == iLayerCount - 2))
                fldGroup.setGrouptype("0");
              else if ((k != 0) && (i == iLayerCount - 2))
                fldGroup.setGrouptype("2");
              else
                fldGroup.setGrouptype("3");
              fldGroup.setItem1(strItem1);
              fldGroup.setItem2(strItem2);
              if ((i == 0) && (k == iSize - 2))
                fldGroup.setToplevelflag("Y");
              else {
                fldGroup.setToplevelflag("N");
              }
              vec.addElement(fldGroup);
            }
          }
          if (j >= iColCount)
            continue;
          vecTemp.clear();

          vecTemp.addElement(headerNames[j][(i + 1)]);
        }

      }

    }

    FldgroupVO[] fldgroups = new FldgroupVO[vec.size()];
    vec.copyInto(fldgroups);
    return fldgroups;
  }

  public static boolean isSqlEmpty(String sql)
  {
    if (sql == null)
      return true;
    sql = sql.trim();

    return (sql.equals("")) || ((sql.startsWith("/*")) && (sql.endsWith("*/")));
  }

  public static int[] sortBubbleResult_int(int[] s1)
  {
    int length = s1.length;
    int[] temp = new int[length];

    for (int i = 0; i < length; i++)
      temp[i] = s1[i];
    for (int i = 0; i < length; i++) {
      int index = temp[i];
      for (int j = i; j < length; j++)
        if (index > temp[j]) {
          int tt = temp[j];
          temp[j] = temp[i];
          temp[i] = tt;
          index = tt;
        }
    }
    return temp;
  }

  public static String[] sortBubbleResult_String(String[] s1)
  {
    int length = s1.length;
    String[] temp = new String[length];

    for (int i = 0; i < length; i++)
      temp[i] = s1[i];
    for (int i = 0; i < length; i++) {
      String str = temp[i];
      for (int j = i; j < length; j++)
        if (str.compareTo(temp[j]) > 0) {
          String tt = temp[j];
          temp[j] = temp[i];
          temp[i] = tt;
          str = tt;
        }
    }
    return temp;
  }

  public static boolean isElement(String str, String[] strArray)
  {
    if (strArray != null)
      for (int i = 0; i < strArray.length; i++)
        if (str.equals(strArray[i]))
          return true;
    return false;
  }

  public static int isElement2(String str, String[] strArray)
  {
    if (strArray != null) {
      for (int i = 0; i < strArray.length; i++)
        if (str.equals(strArray[i]))
          return i;
    }
    return -1;
  }

  public static boolean isElement3(String str, String[] strArray)
  {
    if (strArray != null)
      for (int i = 0; i < strArray.length; i++)
        if (str.equalsIgnoreCase(strArray[i]))
          return true;
    return false;
  }

  public static boolean isCodeEmpty(EmbedCodeVO ec)
  {
    if (ec != null) {
      String code = ec.getEmbedCode().trim();
      if ((!code.equals("")) && ((!code.startsWith("/*")) || (!code.endsWith("*/"))))
      {
        return false;
      }
    }
    return true;
  }

  public static QueryModelDef getQueryDef(String id, String dsNameForDef)
  {
    QueryModelDef qmd = null;
    if (dsNameForDef == null) {
      qmd = (QueryModelDef)QueryModelTree.getDefaultInstance().findObject(id);
    }
    else {
      qmd = (QueryModelDef)QueryModelTree.getInstance(dsNameForDef).findObject(id);
    }
    return qmd;
  }

  public static QueryBaseDef getBaseDefAfterRepair(QueryModelDef qmd, Hashtable hashParam)
    throws Exception
  {
    return getBaseDefAfterRepair(qmd, hashParam, null);
  }

  public static QueryBaseDef getBaseDefAfterRepair(QueryModelDef qmd, Hashtable hashParam, String env)
    throws Exception
  {
    QueryBaseDef qbd = null;
    if (qmd != null)
    {
      SqlRepairVO sr = qmd.getSqlRepairVO();
      if (isCodeEmpty(sr))
      {
        qbd = qmd.getQueryBaseDef();
      }
      else
      {
        IEmbedCodeUtil name = (IEmbedCodeUtil)NCLocator.getInstance().lookup(IEmbedCodeUtil.class.getName());

        qbd = name.repair(qmd, hashParam, null);

        String handSql = qbd.getHandSql();
        if ((handSql != null) && (!handSql.equals(""))) {
          qbd.setHandSql("");
          handSql = QueryUtil.getSqlByBaseDef(qbd, hashParam);
          qbd.setHandSql(handSql);
        }
      }
    }
    return qbd;
  }

  public static int getCrossType(QueryModelDef qmd)
  {
    int iCrossType = 0;
    if (qmd != null) {
      SimpleCrossVO[] scs = qmd.getScs();
      if (scs != null)
      {
        iCrossType = 1;
      } else {
        RotateCrossVO rc = qmd.getQueryBaseVO() == null ? null : qmd.getQueryBaseVO().getRotateCross();

        if (rc != null)
          iCrossType = 2;
      }
    }
    return iCrossType;
  }

  public static Datadict getDataDictByDsn(String dsName, int iAdvantage)
  {
    Datadict dd = null;

    if ((dsName == null) || (dsName.equals("")))
    {
      dsName = InvocationInfoProxy.getInstance().getUserDataSource();
    }
    System.out.println("dsName = " + dsName);

    int iType = 0;
    try {
      IQEDataSourceInfo name = (IQEDataSourceInfo)NCLocator.getInstance().lookup(IQEDataSourceInfo.class.getName());

      iType = name.judgeDsType(dsName);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    Logger.warn("iType = " + String.valueOf(iType));

    if (iType == -2) {
      iType = iAdvantage == 2 ? 2 : 1;
    }
    Logger.warn("iType = " + String.valueOf(iType));
    switch (iType)
    {
    case 1:
      dd = Datadict.getInstance(dsName);

      break;
    case 2:
      try
      {
        Class classUfo = Class.forName("com.ufsoft.iufo.fmtplugin.businessquery.IUFODDCUtil");

        Object result = null;
        try {
          Method method = classUfo.getMethod("getMeasObjectNodes", new Class[] { Boolean.class });

          result = method.invoke(null, new Object[] { new Boolean(iAdvantage < 0) });
        }
        catch (InvocationTargetException e) {
          System.out.println("IUFODDCUtil版本不对");
          Logger.warn("IUFODDCUtil版本不对");

          Method method = classUfo.getMethod("getMeasObjectNodes", new Class[0]);

          result = method.invoke(null, new Object[0]);
        }
        ObjectNode[] nodes = (ObjectNode[])(ObjectNode[])result;

        dd = new DataDictForNode();
        dd.setDatabaseParam(dsName);
        ((DataDictForNode)dd).setIUFO(true);
        ((DataDictForNode)dd).setNodes(nodes);
        dd.loadTree();
      }
      catch (Exception e) {
        System.out.println(e);
        Logger.warn(e.getMessage(), e);
      }

    default:
      DDCData data = null;
      try
      {
        IQEDataSourceInfo name = (IQEDataSourceInfo)NCLocator.getInstance().lookup(IQEDataSourceInfo.class.getName());

        data = name.fromDBMetaData(dsName, 0);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }

      Vector vecNode = data.getNode();
      Vector vecTd = data.getTable();

      int iSize = vecNode.size();
      ObjectNode[] nodes = new ObjectNode[iSize];
      for (int i = 0; i < iSize; i++) {
        nodes[i] = ((ObjectNode)vecNode.elementAt(i));
        TableDef td = (TableDef)vecTd.elementAt(i);
        nodes[i].setObject(td);

        td.setNode(nodes[i]);

        nodes[i].setParentGUID("-1DatadictNode");
      }

      dd = new DataDictForNode();
      ((DataDictForNode)dd).setNodes(nodes);
      dd.loadTree();
      break;
    }

    return dd;
  }

  public static StorageDataSet getDatasetByBaseDef(QueryBaseDef qbd, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    StorageDataSet dataSet = null;

    if (qbd.getSelectFlds() != null) {
      QueryExecutor qe = new QueryExecutor(dsNameForDef);

      Integer iMaxRow = QEEnvParamBean.getDefaultInstance().getMaxRowCount();

      int maxRowCount = iMaxRow == null ? -1 : iMaxRow.intValue();
      if (maxRowCount > 0) {
        String sqlpatch = " top " + maxRowCount + " ";
        qe.setSqlPatch(sqlpatch);
      }
      qe.setQueryBaseDef(qbd);
      qe.setHashParam(hashParam);
      qe.setRefreshTemptable(true);

      qe.setNeedRecordCount(false);

      IRecordsLoader name = (IRecordsLoader)NCLocator.getInstance().lookup(IRecordsLoader.class.getName());

      DataSetContainer dsc = name.getDataSetOnce(qe);
      dataSet = dsc.getDataSet();
    }
    return dataSet;
  }

  public static String getDefaultDsname()
  {
    String strDefaultDsn = null;
    try
    {
      strDefaultDsn = InvocationInfoProxy.getInstance().getUserDataSource();
    }
    catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("DSN : " + strDefaultDsn);
    if (strDefaultDsn == null)
    {
      strDefaultDsn = "iufo";
    }return strDefaultDsn;
  }

  public static String getDefaultDsnameForDef()
  {
    String strDefaultDsn = getDefaultDsname();
    try {
      if (!strDefaultDsn.equalsIgnoreCase("iufo"))
      {
        IQEDataSourceInfo env = (IQEDataSourceInfo)NCLocator.getInstance().lookup(IQEDataSourceInfo.class.getName());

        QEEnvParamBean qeenv = env.getQEEnvParamBean();
        String dwDsn = qeenv.getDwDsn();
        if (dwDsn != null)
          strDefaultDsn = dwDsn;
      }
    }
    catch (Exception e) {
      Logger.error(e.getMessage(), e);
      System.out.println("配置文件未找到");
    }
    return strDefaultDsn;
  }

  /** @deprecated */
  public static String getEngineVerType()
  {
    String vertype = System.getProperty("ENGINE_VERTYPE_ENV");
    return vertype == null ? "" : vertype;
  }

  public static FormatModelDef getFormatDef(String id, String dsNameForDef)
  {
    FormatModelDef fmd = null;
    if (dsNameForDef == null) {
      fmd = (FormatModelDef)FormatModelTree.getDefaultInstance().findObject(id);
    }
    else {
      fmd = (FormatModelDef)FormatModelTree.getInstance(dsNameForDef).findObject(id);
    }
    return fmd;
  }

  public static String getHashString(Hashtable hashParam)
  {
    String str = "";
    if (hashParam != null) {
      Enumeration keys = hashParam.keys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement().toString();
        if ((hashParam.get(key) instanceof ParamVO)) {
          String value = ((ParamVO)hashParam.get(key)).getValue();
          str = str + key + ": " + value + ", ";
        }
      }
    }
    if (str.length() >= 2)
      str = str.substring(0, str.length() - 2);
    return str;
  }

  public static JoinCondVO[] getJoinCondBetweenTables(FromTableVO[] fts, Datadict dict, String dsNameForDDC)
  {
    int iLen = fts == null ? 0 : fts.length;
    if (iLen < 2) {
      return null;
    }
    Vector vec = new Vector();
    for (int i = 0; i < iLen; i++) {
      String tableId = fts[i].getTablecode();
      if (!QueryUtil.isTempTable(tableId)) {
        TableDef td = (TableDef)dict.findObject(fts[i].getTablecode());
        vec.addElement(td.getGUID());
      }
    }
    int iSize = vec.size();
    if (iSize < 2) {
      return null;
    }
    String[] guids = new String[iSize];
    vec.copyInto(guids);

    ForKeyVO[] fkvos = null;
    try
    {
      IDataStructureInfo name = (IDataStructureInfo)NCLocator.getInstance().lookup(IDataStructureInfo.class.getName());

      fkvos = name.queryForKeyVOs(guids, dsNameForDDC);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    if (fkvos == null) {
      return null;
    }
    vec = new Vector();
    for (int j = 0; j < fkvos.length; j++)
    {
      TableDef td = dict.findTableDefByGUID(fkvos[j].getTableGuid());
      FieldDef fd = td.getFieldDefByGUID(fkvos[j].getFldGuid());
      TableDef tdJn = dict.findTableDefByGUID(fkvos[j].getTableJnGuid());
      FieldDef fdJn = tdJn.getFieldDefByGUID(fkvos[j].getFldJnGuid());

      JoinCondVO jc = new JoinCondVO();
      jc.setLefttable(td.getID());
      jc.setLeftfld(fd.getID());
      jc.setRighttable(tdJn.getID());
      jc.setRightfld(fdJn.getID());
      jc.setTypeflag("I");
      jc.setOperator("=");
      vec.addElement(jc);
    }

    JoinCondVO[] jcs = new JoinCondVO[vec.size()];
    if (vec.size() != 0)
      vec.copyInto(jcs);
    return jcs;
  }

  /** @deprecated */
  public static String getLoginEnv()
  {
    return System.getProperty("ENGINE_LOGIN_ENV");
  }

  public static MultiDataSet getMultiQueryResult(QueryModelDef qmd, Hashtable hashParam, String dsNameForDef, String dsNameForExe)
    throws Exception
  {
    MultiDataSet mds = null;
    try {
      mds = getMultiDataSet(qmd, hashParam, dsNameForDef, dsNameForExe, null);
    }
    catch (Exception e) {
      e.printStackTrace();

      throw new Exception(e.getMessage());
    }
    return mds;
  }

  public static int[][] getParamRefDepends(ParamVO[] params)
  {
    int iLen = params == null ? 0 : params.length;
    Vector vec = new Vector();
    Hashtable hashParam = new Hashtable();
    for (int i = 0; i < iLen; i++) {
      hashParam.put(params[i].getParamCode(), new Integer(i));
    }
    for (int i = 0; i < iLen; i++) {
      int iDataType = params[i].getDataType().intValue();
      if ((iDataType != 4) && (iDataType != 5) && (iDataType != 6)) {
        continue;
      }
      String refDepend = params[i].getRefDepend();
      refDepend = refDepend == null ? "" : refDepend.trim();
      if (refDepend.equals(""))
        continue;
      String[] strDepends = getStrsInBracket(refDepend);
      int iDependLen = strDepends == null ? 0 : strDepends.length;

      for (int j = 0; j < iDependLen; j++) {
        if (!hashParam.containsKey(strDepends[j]))
          continue;
        int iDepending = i;

        int iDepended = ((Integer)hashParam.get(strDepends[j])).intValue();

        vec.addElement(new int[] { iDepending, iDepended });
      }

    }

    int iSize = vec.size();
    int[][] iRefDepends = (int[][])null;
    if (iSize != 0) {
      iRefDepends = new int[iSize][2];
      vec.copyInto(iRefDepends);
    }
    return iRefDepends;
  }

  public static ParamVO[] getParams(String id, String dsNameForDef)
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);
    if (qmd == null) {
      throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-000523") + id);
    }

    ParamVO[] params = qmd.getParamVOs();
    int iLen = params == null ? 0 : params.length;
    for (int i = 0; i < iLen; i++)
      params[i].setDsName(qmd.getDsName());
    return params;
  }

  public static StorageDataSet getQueryResult(String id, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    return getQueryResult(id, hashParam, dsNameForDef, null);
  }

  public static StorageDataSet getQueryResult(String id, Hashtable hashParam, String dsNameForDef, String dsNameForExe)
    throws Exception
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);
    if (qmd == null) {
      throw new Exception(QueryConst.ERR_1);
    }
    MultiDataSet mds = getMultiQueryResult(qmd, hashParam, dsNameForDef, dsNameForExe);

    return mds.getDataSet();
  }

  public static StorageDataSet getQueryResult_Sql(String id, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    return getQueryResult_Sql(id, hashParam, dsNameForDef, null);
  }

  public static StorageDataSet getQueryResult_Sql(String id, Hashtable hashParam, String dsNameForDef, String dsNameForExe)
    throws Exception
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);
    return getQueryResult_Sql(qmd, hashParam, dsNameForDef, dsNameForExe);
  }

  public static StorageDataSet getQueryResult_Sql(QueryModelDef qmd, Hashtable hashParam, String dsNameForDef, String dsNameForExe)
    throws Exception
  {
    StorageDataSet dataSet = null;
    if (qmd != null)
    {
      SimpleCrossVO[] scs = qmd.getScs();
      if (scs != null)
      {
        dataSet = getDataSetForSimple(qmd, hashParam, dsNameForDef, dsNameForExe, null);
      }
      else
      {
        Integer iMaxRow = QEEnvParamBean.getDefaultInstance().getMaxRowCount();

        int maxRowCount = iMaxRow == null ? -1 : iMaxRow.intValue();

        IRecordsLoader name = (IRecordsLoader)NCLocator.getInstance().lookup(IRecordsLoader.class.getName());

        DataSetContainer dsc = name.getDataSetOneTime(qmd, hashParam, dsNameForDef, dsNameForExe, null, maxRowCount);

        dataSet = dsc.getDataSet();

        QueryBaseDef qbd = dsc.getQbd();
        DatasetUtil.assembleDataset(dataSet, qbd.getSelectFlds());
      }
    }
    return dataSet;
  }

  public static ScrollableDataSet getScrollableDataSet(String id, Hashtable hashParam, int pageSize, int dbType, String dsNameForDef, String dsNameForExe)
    throws Exception
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);

    return getScrollableDataSet(qmd, hashParam, pageSize, dbType, dsNameForExe);
  }

  public static ScrollableDataSet getScrollableDataSet(QueryModelDef qmd, Hashtable hashParam, int pageSize, int dbType, String dsNameForExe)
    throws Exception
  {
    ScrollableDataSet dataSet = null;
    if (qmd != null) {
      if (dsNameForExe == null) {
        dsNameForExe = qmd.getDsName();
      }

      String sql = QueryUtil.getSqlByBaseDef(qmd.getQueryBaseDef(), hashParam);

      dataSet = new ScrollableDataSet(sql, pageSize, dbType, dsNameForExe);
    }
    return dataSet;
  }

  public static SimpleCrossVO[][] getRowColScs(SimpleCrossVO[] scs)
  {
    SimpleCrossVO[][] scsRowColArray = new SimpleCrossVO[2][];
    int iLen = scs == null ? 0 : scs.length;
    if (iLen != 0)
    {
      Vector vecRowHeader = new Vector();
      Vector vecColHeader = new Vector();
      for (int i = 0; i < iLen; i++) {
        int iLocate = scs[i].getLocate();
        if (iLocate == 1)
          vecRowHeader.addElement(scs[i]);
        else if (iLocate == 2)
          vecColHeader.addElement(scs[i]);
      }
      int iRowCount = vecRowHeader.size();
      int iColCount = vecColHeader.size();

      if (iRowCount != 0) {
        scsRowColArray[0] = new SimpleCrossVO[iRowCount];
        vecRowHeader.copyInto(scsRowColArray[0]);
      }
      if (iColCount != 0) {
        scsRowColArray[1] = new SimpleCrossVO[iColCount];
        vecColHeader.copyInto(scsRowColArray[1]);
      }
    }
    return scsRowColArray;
  }

  public static SimpleCrossVO[] getRowColScs(SimpleCrossVO[] scs, int iRow, int iCol)
  {
    SimpleCrossVO[][] scsRowColArray = getRowColScs(scs);
    int iLenX = scsRowColArray[0] == null ? 0 : scsRowColArray[0].length;
    int iLenY = scsRowColArray[1] == null ? 0 : scsRowColArray[1].length;
    if ((iRow >= iLenX) || (iCol >= iLenY) || (iRow == -1) || (iCol == -1))
      return null;
    SimpleCrossVO[] scsRowCol = { scsRowColArray[0][iRow], scsRowColArray[1][iCol] };

    return scsRowCol;
  }

  public static String[] getRowHeaders(SimpleCrossVO[] scs)
  {
    int iLen = scs == null ? 0 : scs.length;
    Vector vec = new Vector();
    for (int i = 0; i < iLen; i++) {
      if (scs[i].getLocate() == 1) {
        vec.addElement(scs[i].getFldName());
      }
    }
    int iSize = vec.size();
    if (iSize == 0)
      return null;
    String[] rowHeaders = new String[iSize];
    vec.copyInto(rowHeaders);
    return rowHeaders;
  }

  public static SelectFldVO[] getSelectFlds(String id, String dsNameForDef)
    throws Exception
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);
    return getSelectFlds(qmd);
  }

  public static SelectFldVO[] getSelectFlds(QueryModelDef qmd)
    throws Exception
  {
    SelectFldVO[] sfs = null;
    if (qmd != null)
    {
      MergeHeaderVO mh = qmd.getMh();
      if (mh != null) {
        return mh.getSelectFlds();
      }
      QueryBaseDef qbd = qmd.getQueryBaseDef();
      if (qbd != null)
      {
        SimpleCrossVO[] scs = qbd.getScs();
        if (scs == null) {
          sfs = qbd.getSelectFlds();
        } else {
          int iLen = scs == null ? 0 : scs.length;

          Vector vecColHeader = new Vector();
          for (int i = 0; i < iLen; i++)
            if (scs[i].getLocate() == 2)
              vecColHeader.addElement(scs[i]);
          int iColCount = vecColHeader.size();
          if (iColCount == 0) {
            return null;
          }
          SelectFldVO[] oldsfs = qbd.getSelectFlds();
          int iFldCount = oldsfs == null ? 0 : oldsfs.length;

          sfs = new SelectFldVO[iColCount * iFldCount];
          for (int i = 0; i < iColCount; i++) {
            SimpleCrossVO sc = (SimpleCrossVO)vecColHeader.elementAt(i);

            for (int j = 0; j < iFldCount; j++) {
              String fldalias = null;
              String fldname = null;
              if (iColCount == 1)
              {
                fldalias = oldsfs[j].getFldalias();
                fldname = oldsfs[j].getFldname();
              } else {
                fldalias = sc.getFldCode();
                fldname = sc.getFldName();
                if (iFldCount > 1)
                {
                  fldalias = fldalias + "_" + oldsfs[j].getFldalias();
                  fldname = fldname + "_" + oldsfs[j].getFldname();
                }
              }

              sfs[(i * iFldCount + j)] = new SelectFldVO();
              sfs[(i * iFldCount + j)].setFldalias(fldalias);
              sfs[(i * iFldCount + j)].setFldname(fldname);
              sfs[(i * iFldCount + j)].setColtype(oldsfs[j].getColtype());

              sfs[(i * iFldCount + j)].setPrecision(oldsfs[j].getPrecision());

              sfs[(i * iFldCount + j)].setScale(oldsfs[j].getScale());
            }
          }
        }
      }
    }

    return sfs;
  }

  public static SelectFldVO[] getSelectRows(QueryModelDef qmd)
    throws Exception
  {
    SelectFldVO[] sfs = null;
    if (qmd != null) {
      QueryBaseDef qbd = qmd.getQueryBaseDef();
      if (qbd != null)
      {
        SimpleCrossVO[] scs = qbd.getScs();
        if (scs == null) {
          return null;
        }
        int iLen = scs == null ? 0 : scs.length;

        Vector vecRowHeader = new Vector();
        for (int i = 0; i < iLen; i++)
          if (scs[i].getLocate() == 1)
            vecRowHeader.addElement(scs[i]);
        int iRowCount = vecRowHeader.size();
        if (iRowCount == 0) {
          return null;
        }
        sfs = new SelectFldVO[iRowCount];
        for (int i = 0; i < iRowCount; i++) {
          SimpleCrossVO sc = (SimpleCrossVO)vecRowHeader.elementAt(i);

          sfs[i] = new SelectFldVO();
          sfs[i].setFldalias(sc.getFldCode());
          sfs[i].setFldname(sc.getFldName());
        }

      }

    }

    return sfs;
  }

  public static String[] getStrsInBracket(String str)
  {
    Vector vec = new Vector();
    int iIndex1 = 0;
    int iIndex2 = 0;
    String strOrigin = str;
    while (true) {
      int iLen = strOrigin.length();
      iIndex1 = strOrigin.indexOf("[");
      if ((iIndex1 == -1) || (iIndex1 == iLen - 1))
        break;
      iIndex2 = strOrigin.indexOf("]");
      if (iIndex2 < iIndex1)
        break;
      String strTemp = strOrigin.substring(iIndex1 + 1, iIndex2);
      vec.addElement(strTemp);
      if (iIndex2 == iLen - 1)
        break;
      strOrigin = strOrigin.substring(iIndex2 + 1);
    }

    int iSize = vec.size();
    String[] strs = new String[iSize];
    vec.copyInto(strs);
    return strs;
  }

  public static String[] getValidDsNames()
    throws Exception
  {
    String[] dsns = null;

    String defaultDsn = "";

    String[][] strDsnameTypes = QEEnvParamBean.getDefaultInstance().getDsnDbType();

    int iLen = strDsnameTypes == null ? 0 : strDsnameTypes.length;
    if (iLen != 0) {
      dsns = new String[iLen];

      for (int i = 0; i < iLen; i++) {
        dsns[i] = strDsnameTypes[i][0];
      }
      int iIndex = -1;
      for (int i = 0; i < iLen; i++)
        if (dsns[i].equals(defaultDsn)) {
          iIndex = i;
          break;
        }
      if (iIndex == -1) {
        System.out.println("iIndex=-1, 不可能");
        return null;
      }
      String temp = dsns[iIndex];
      dsns[iIndex] = dsns[0];
      dsns[0] = temp;
    }

    return dsns;
  }

  public static boolean isMergeQuery(QueryModelDef qmd)
  {
    if (qmd == null)
      return false;
    return qmd.getMh() != null;
  }

  public static boolean isSimpleCrossQuery(QueryModelDef qmd)
  {
    if (qmd == null)
      return false;
    return !ArrayUtils.isEmpty(qmd.getScs());
  }

  public static boolean isRotateCrossQuery(QueryModelDef qmd)
  {
    if (qmd == null)
      return false;
    return qmd.getRotateCross() != null;
  }

  public static boolean isQueryDefValid(String id, String dsNameForDef)
  {
    return getQueryDef(id, dsNameForDef) != null;
  }

  public static int isQueryExeValid(String id, String dsNameForDef, String dsNameForExe)
  {
    QueryModelDef qmd = getQueryDef(id, dsNameForDef);
    if (qmd == null) {
      return -1;
    }

    QueryBaseDef qbd = qmd.getQueryBaseDef();
    if (dsNameForExe != null)
      qbd.setDsName(dsNameForExe);
    try
    {
      WhereCondVO wc = new WhereCondVO("1=0");
      wc.setrelationflag("_and");
      qbd.addWhere(wc);
      getDatasetByBaseDef(qbd, null, dsNameForDef);
    } catch (Exception e) {
      System.out.println(e);
      return -2;
    }
    return 0;
  }

  public static String removeSquareBrackets(String strSql)
  {
    if (strSql == null)
      return null;
    StringBuffer sbOld = new StringBuffer(strSql.trim());
    StringBuffer sbNew = new StringBuffer();

    boolean bOddQuote = false;

    for (int i = 0; i < sbOld.length(); i++) {
      char cTemp = sbOld.charAt(i);
      if (cTemp == '\'') {
        bOddQuote = !bOddQuote;
        sbNew.append(cTemp);
      } else if (bOddQuote) {
        sbNew.append(cTemp);
      } else if ((cTemp != '[') && (cTemp != ']')) {
        sbNew.append(cTemp);
      }
    }
    return new String(sbNew);
  }

  public static String[][] getEnvParams(IEnvParam iEnvParam)
  {
    int iDefaultLen = 4;
    int iEnvParamLen = 0;
    if (iEnvParam != null) {
      iEnvParamLen = iEnvParam.getEnvParamCodes().length;
    }
    Vector vec = new Vector();
    for (int i = 0; i < iDefaultLen; i++) {
      String[] strs = new String[2];
      strs[0] = QueryConst.BASIC_LOGIN_ENVS[i];
      strs[1] = strs[0].substring(1, strs[0].length() - 1);
      vec.addElement(strs);
    }
    for (int i = 0; i < iEnvParamLen; i++) {
      String[] strs = new String[2];
      strs[1] = iEnvParam.getEnvParamNotes()[i];
      if (strs[1].equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-001310")))
      {
        continue;
      }

      strs[0] = iEnvParam.getEnvParamCodes()[i];
      vec.addElement(strs);
    }

    String[][] envParams = new String[vec.size()][2];
    vec.copyInto(envParams);
    return envParams;
  }

  public static CreateTempReturnVO createTempView(CreateTempParamVO ctp)
    throws Exception
  {
    CreateTempReturnVO ctr = null;
    long t = System.currentTimeMillis();

    QueryBaseDef qbd = ctp.getQbd();
    if (qbd != null) {
      ITempTableHandler name = (ITempTableHandler)NCLocator.getInstance().lookup(ITempTableHandler.class.getName());

      ctr = name.createRelatedTable(ctp);
    }

    t = System.currentTimeMillis() - t;
    System.out.println("<getDataSet_create0>:" + t + " ms");

    return ctr;
  }

  public static void dropTempView(QueryBaseDef qbd, CreateTempReturnVO ctr)
  {
    Hashtable hashViewName = ctr.getHashViewName();
    try
    {
      String[] tempViewNames = (String[])(String[])hashViewName.values().toArray(new String[0]);

      ITempTableHandler name = (ITempTableHandler)NCLocator.getInstance().lookup(ITempTableHandler.class.getName());

      name.dropTempView(tempViewNames, qbd.getDsName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static StorageDataSet getDatasetForSimple(SimpleCrossVO[] scs, QueryBaseDef qbd, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    StorageDataSet dataSet = null;
    if (isComplexQuery(qbd)) {
      CreateTempReturnVO ctr = null;
      try
      {
        ctr = createTempView(new CreateTempParamVO(qbd, hashParam, dsNameForDef));

        dataSet = DatasetUtil.getDatasetForSimpleCross(scs, qbd, hashParam, ctr.getHashViewName());
      }
      catch (Exception e) {
        throw e;
      }
      finally {
        dropTempView(qbd, ctr);
      }
    }
    else {
      dataSet = DatasetUtil.getDatasetForSimpleCross(scs, qbd, hashParam);
    }
    return dataSet;
  }

  public static boolean isComplexQuery(QueryBaseDef qbd)
  {
    FromTableVO[] fts = qbd.getFromTables();
    int iLen = fts == null ? 0 : fts.length;
    for (int i = 0; i < iLen; i++) {
      if (QueryUtil.isTempTable(fts[i].getTablecode())) {
        return true;
      }
    }
    return false;
  }

  public static MultiDataSet getMultiDataSet(QueryModelDef qmd, Hashtable hashParam, String dsNameForDef, String dsNameForExe, QueryExecutor qe)
    throws Exception
  {
    long t = System.currentTimeMillis();
    MultiDataSet mds = new MultiDataSet();
    StorageDataSet dataSet = null;

    DataProcessVO dp = qmd.getDataProcessVO();
    if (!isCodeEmpty(dp))
    {
      IEmbedCodeUtil name = (IEmbedCodeUtil)NCLocator.getInstance().lookup(IEmbedCodeUtil.class.getName());

      DataSetDataWithColumn dsdwc = name.process(qmd, hashParam, null);
      if (dsdwc.getDataSetData() != null) {
        dataSet = new StorageDataSet();
        dsdwc.getDataSetData().loadDataSet(dataSet);
        if (qe == null)
        {
          DataSetData[] dsds = dsdwc.getDataSetDatas();
          int iLen = dsds == null ? 0 : dsds.length;
          if (iLen != 0) {
            StorageDataSet[] dsArray = new StorageDataSet[iLen];
            dataSet.close();
            for (int i = 0; i < iLen; i++) {
              dsArray[i] = new StorageDataSet();
              dsdwc.getDataSetDatas()[i].loadDataSet(dsArray[i]);

              dataSet.setMasterLink(new MasterLinkDescriptor(dsArray[i], dsdwc.getMasterCols(), dsdwc.getDetailCols(), false, false, false));

              for (int j = 0; j < dsArray[i].getColumnCount(); j++) {
                dsArray[i].getColumn(j).setCaption(dsdwc.getCaptionsArray()[i][j]);
              }
            }
            mds.setAssisDataSets(dsArray);
          }
        }

        for (int i = 0; i < dataSet.getColumnCount(); i++) {
          dataSet.getColumn(i).setCaption(dsdwc.getCaptions()[i]);
        }
        hashParam = dsdwc.getHashParam();
      }
    } else {
      SimpleCrossVO[] scs = qmd.getScs();
      if (scs != null) {
        dataSet = getDataSetForSimple(qmd, hashParam, dsNameForDef, dsNameForExe, null);
      }
      else {
        RotateCrossVO rc = null;
        if (qe == null)
        {
          dataSet = getQueryResult_Sql(qmd, hashParam, dsNameForDef, dsNameForExe);

          rc = qmd.getRotateCross();
        } else {
          QueryBaseDef qbd = qe.getQueryBaseDef();

          dataSet = qe.getDataSet_query();

          rc = qbd.getRotateCross();
        }
        if (rc != null)
        {
          mds.setDsBeforeRotate(dataSet);

          Object[] objs = null;
          try {
            objs = DatasetUtil.getDatasetForRotateCross(rc, dataSet);
          }
          catch (Exception e) {
            System.out.println("旋转交叉有误" + e);
          }
          if (objs != null)
          {
            dataSet = (StorageDataSet)objs[0];

            int iRowCount = rc.getStrRows() == null ? 0 : rc.getStrRows().length;

            DatasetUtil.tamperColname(dataSet, iRowCount);

            FldgroupVO[] fldgroups = (FldgroupVO[])(FldgroupVO[])objs[1];

            qmd.setFldgroups(fldgroups);
          }
        }
      }
    }

    mds.setDataSet(dataSet);
    mds.setHashParam(hashParam);

    t = System.currentTimeMillis() - t;
    System.out.println("<查询总耗时>:" + t + " ms");
    return mds;
  }

  public static StorageDataSet getDataSetForSimple(QueryModelDef qmd, Hashtable hashParam, String dsNameForDef, String dsNameForExe, QueryExecutor qe)
    throws Exception
  {
    StorageDataSet dataSet = null;
    MergeHeaderVO mh = qmd.getMh();
    if (mh != null)
    {
      dataSet = DatasetUtil.getDatasetForMergeElementCross(mh, hashParam, dsNameForDef);
    }
    else {
      QueryBaseDef qbd = null;

      if (qe == null) {
        qbd = getBaseDefAfterRepair(qmd, hashParam);
      }
      else {
        qbd = qe.getQueryBaseDef();
      }

      if (dsNameForExe != null) {
        qbd.setDsName(dsNameForExe);
      }

      dataSet = getDatasetForSimple(qmd.getScs(), qbd, hashParam, dsNameForDef);
    }

    return dataSet;
  }

  public static void buildQEObjectTree(String dsName)
  {
    if (FormatModelTree.getTreeFromCacheTable(dsName) == null)
      try
      {
        QueryModelTree qmdTree = new QueryModelTree();
        FormatModelTree fmdTree = new FormatModelTree();
        String[] storageClassName = { qmdTree.getStorageClassName(), fmdTree.getStorageClassName() };

        String[] sqlStr = { qmdTree.getLoadSQL(), fmdTree.getLoadSQL() };

        IDataStructureInfo name = (IDataStructureInfo)NCLocator.getInstance().lookup(IDataStructureInfo.class.getName());

        Object[] result = name.getAllQETreeNodeVO(dsName, storageClassName, sqlStr);

        if ((result == null) || (result.length < 2)) {
          return;
        }
        qmdTree.setNodeVO((ObjectNodeVO[])(ObjectNodeVO[])result[0]);
        qmdTree.setDatabaseParam(dsName);
        qmdTree.loadTree();
        QueryModelTree.putTree2CacheTable(dsName, qmdTree);

        fmdTree.setNodeVO((ObjectNodeVO[])(ObjectNodeVO[])result[1]);
        fmdTree.setDatabaseParam(dsName);
        fmdTree.loadTree();
        FormatModelTree.putTree2CacheTable(dsName, fmdTree);
      } catch (BusinessException e) {
        e.printStackTrace();
      }
  }

  public static void assembleDataset(StorageDataSet dataset, SelectFldVO[] sfs)
  {
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

  public static String checkIDandName(String id, String dispName)
  {
    if (StringUtil.isEmpty(dispName)) {
      return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-001228");
    }

    String err = QueryUtil.isValidId(id);
    if (err != null) {
      return err;
    }
    if (dispName.getBytes().length >= 64) {
      return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-001229");
    }

    if (!charCheck(dispName)) {
      return NCLangRes4VoTransl.getNCLangRes().getStrByID("10241201", "UPP10241201-001230");
    }

    return null;
  }

  private static boolean charCheck(String s)
  {
    byte[] chars = s.getBytes();
    for (int i = 0; i < chars.length; i++) {
      if ((chars[i] == 92) || (chars[i] == 47) || (chars[i] == 42) || (chars[i] == 63) || (chars[i] == 124) || (chars[i] == 60) || (chars[i] == 62) || (chars[i] == 34) || (chars[i] == 58))
      {
        return false;
      }
    }
    return true;
  }

  public static boolean isNull(String s)
  {
    boolean bo = true;
    if ((s == null) || (s.trim().equals(""))) {
      bo = false;
    }
    return bo;
  }

  public static boolean checkDateTime(String s)
  {
    boolean boRight = true;
    if ((s.length() != 19) || (!s.substring(4, 5).equals("-")) || (!s.substring(7, 8).equals("-")) || (!s.substring(10, 11).equals(" ")) || (!s.substring(13, 14).equals(":")) || (!s.substring(16, 17).equals(":")))
    {
      boRight = false;
    }
    return boRight;
  }

  public static EnvInfo getEnvInfo(Hashtable hashParam)
  {
    EnvInfo env = null;
    if (hashParam.containsKey("ENV_PARAM_KEY")) {
      Object[] objEnvParams = (Object[])(Object[])hashParam.get("ENV_PARAM_KEY");

      env = (EnvInfo)objEnvParams[2];
    }
    return env;
  }
}