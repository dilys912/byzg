package nc.ui.pub.querymodel;

import com.borland.dx.dataset.StorageDataSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.querytemplate.IQueryTemplate;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.report.base.ReportUIBase;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.bd.CorpVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletVO;
import nc.vo.pub.querymodel.DataPowerUtil;
import nc.vo.pub.querymodel.DatasetUtil;
import nc.vo.pub.querymodel.DefaultEnvParam;
import nc.vo.pub.querymodel.DefaultQEDataPowerRefMap;
import nc.vo.pub.querymodel.EnvInfo;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.IQEDataPowerRefMap;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.MultiDataSet;
import nc.vo.pub.querymodel.OuterUtil;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.QueryModelTree;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

public class QEReportBase extends ReportUIBase
{
  private String m_queryId = null;

  private String m_dsNameForDef = null;

  private Hashtable hashParam = null;
  private QueryModelDef qmd_origin;

  public QEReportBase(FramePanel fp)
  {
    super(fp);
  }

  protected void onQuery()
    throws Exception
  {
    try
    {
      this.m_queryId = getParam_Query();
      if (this.m_queryId == null) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001437"));

        return;
      }

      this.m_dsNameForDef = getParam_Dsname();
      if (this.m_dsNameForDef == null) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001438"));

        return;
      }

      this.qmd_origin = getQueryModelDef(this.m_queryId, this.m_dsNameForDef);
      if (this.qmd_origin == null) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001439"));

        return;
      }

      QueryModelDef qmd = this.qmd_origin.cloneQmd();

      this.hashParam = getHashParam(qmd, this.m_dsNameForDef);
      if (this.hashParam == null)
      {
        return;
      }

      refreshMaterTable(this.m_queryId, this.hashParam, this.m_dsNameForDef);

      StorageDataSet dataSet = getDataSet(qmd, this.hashParam, this.m_dsNameForDef);
      if (dataSet == null) {
        MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001440"));

        return;
      }

      updateReportBase();

      updateTitle();

      setBodyData(dataSet);
    }
    catch (Exception e) {
      Logger.error(e.getMessage(), e);
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001315"));
    }
  }

  protected void onRefresh()
    throws Exception
  {
    if (this.qmd_origin != null)
    {
      QueryModelDef qmd = this.qmd_origin.cloneQmd();

      if (this.m_queryId != null)
      {
        StorageDataSet dataSet = getDataSet(qmd, this.hashParam, this.m_dsNameForDef);

        if (dataSet == null) {
          MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001440"));

          return;
        }

        updateReportBase();

        updateTitle();

        setBodyData(dataSet);

        if ((getGroupKeys() != null) && (!getGroupKeys().isEmpty()))
          onGroup(null);
      }
    }
  }

  protected String getParam_Query()
  {
    String queryId = getParameter("queryId");
    Logger.debug("queryId = " + queryId);
    if (queryId == null) {
      queryId = "gldetail";
    }

    return queryId;
  }

  protected String getParam_Dsname()
  {
    String dsNameForDef = getParameter("dsName");
    Logger.debug("dsNameForDef = " + dsNameForDef);
    if (dsNameForDef == null) {
      Logger.debug("未注册查询定义数据源参数，使用默认数据源");
      dsNameForDef = getClientEnvironment().getConfigAccount().getDataSourceName();
    }

    return dsNameForDef;
  }

  protected QueryModelDef getQueryModelDef(String queryId, String dsNameForDef)
  {
    QueryModelTree qmt = QueryModelTree.getInstance(dsNameForDef);
    if (qmt == null) {
      return null;
    }
    QueryModelDef qmd = (QueryModelDef)qmt.findObject(queryId);
    return qmd;
  }

  protected Hashtable getHashParam(QueryModelDef qmd, String dsNameForDef)
  {
    Hashtable hashParam = null;

    ParamVO[] originParams = qmd.getParamVOs();

    String strDsname = qmd.getDsName() == null ? "" : qmd.getDsName();

    int iLen = originParams == null ? 0 : originParams.length;
    ParamVO[] params = new ParamVO[iLen];
    for (int i = 0; i < iLen; i++) {
      params[i] = ((ParamVO)originParams[i].clone());
      params[i].setDsName(strDsname);
    }

    QEQueryDlg dlg = (QEQueryDlg)getQryDlg();
    if (dlg != null)
    {
      dlg.setUserDefParamVO(getuserDefParamVO(params));
      dlg.showModal();
      if (dlg.getResult() == 1)
      {
        ConditionVO[] conds = dlg.getConditionVOAfterProc();

        hashParam = OuterUtil.fillHashParam(conds, params);

        hashParam.putAll(dlg.getAllUserDefHash());

        hashParam.putAll(getDataPowerHash(hashParam, dlg));

        addWhereSQL(qmd, dlg.getWhereSQL(filterCondVO(params, conds)));
      } else {
        return null;
      }
    }
    else {
      hashParam = getHashParam(params, qmd.getDisplayName(), dsNameForDef);
      if (hashParam == null)
      {
        return null;
      }
    }

    hashParam = UIUtil.addEnvInfo(hashParam);

    EnvInfo env = UIUtil.makeEnvInfo();
    Object[] objEnvParams = { getIEnvParam().getClass().getName(), strDsname, env };

    hashParam.put("ENV_PARAM_KEY", objEnvParams);

    return hashParam;
  }

  protected void addWhereSQL(QueryModelDef qmd, String whereSQL)
  {
    QueryBaseDef qbd = qmd.getQueryBaseDef();
    WhereCondVO[] originWhere = qbd.getWhereConds();
    int iLen = originWhere == null ? 0 : originWhere.length;

    ArrayList list = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if (!originWhere[i].isTempCondition())
        list.add(originWhere[i]);
    }
    originWhere = (WhereCondVO[])(WhereCondVO[])list.toArray(new WhereCondVO[list.size()]);

    iLen = originWhere == null ? 0 : originWhere.length;
    WhereCondVO[] newWhere = new WhereCondVO[iLen + 1];

    if ((whereSQL != null) && (!whereSQL.trim().equals(""))) {
      for (int i = 0; i < iLen; i++) {
        newWhere[i] = originWhere[i];
      }
      newWhere[iLen] = new WhereCondVO("(" + whereSQL + ")");
      newWhere[iLen].setTempCondition(true);
      qbd.setWhereConds(newWhere);
    } else {
      qbd.setWhereConds(originWhere);
    }
    qmd.setQueryBaseVO(qbd);
  }

  private ConditionVO[] filterCondVO(ParamVO[] params, ConditionVO[] originConds)
  {
    int iLen = originConds == null ? 0 : originConds.length;
    int jLen = params == null ? 0 : params.length;
    String fieldCode = null;
    ArrayList list = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      fieldCode = originConds[i].getFieldCode();
      if ((fieldCode == null) || (fieldCode.trim().equals("")))
        continue;
      for (int j = 0; j < jLen; j++) {
        if (fieldCode.equalsIgnoreCase(params[j].getMapField()))
          list.add(originConds[i]);
      }
    }
    return (ConditionVO[])(ConditionVO[])list.toArray(new ConditionVO[list.size()]);
  }

  private Hashtable getDataPowerHash(Hashtable hashParams, QEQueryDlg dlg)
  {
    ParamVO[] dataPowerParamVO = DataPowerUtil.getDataPowerParamVO(hashParams);

    Object[] parameters = DataPowerUtil.getDPParamFromParamVO(dataPowerParamVO);

    String[] paramCode = (String[])(String[])parameters[0];
    String[] refName = (String[])(String[])parameters[1];
    int[] returnType = (int[])(int[])parameters[2];

    String currentCorp = ClientEnvironment.getInstance().getCorporation().getPk_corp();

    String cuserid = ClientEnvironment.getInstance().getUser().getPrimaryKey();

    QueryConditionVO[] qcVO = dlg.getConditionDatas();

    String[] pk_corps = getDataPower_PKCorps();
    IQEDataPowerRefMap refMap = new DefaultQEDataPowerRefMap();
    ConditionVO[] dataPowerCondVO = DataPowerUtil.setRefsDataPowerConVOs(currentCorp, cuserid, pk_corps, refName, paramCode, returnType, qcVO, refMap);

    Hashtable dataPowerHash = OuterUtil.fillHashParam(dataPowerCondVO, dataPowerParamVO);

    return dataPowerHash;
  }

  protected String[] getDataPower_PKCorps()
  {
    return new String[] { ClientEnvironment.getInstance().getCorporation().getPk_corp() };
  }

  private ParamVO[] getuserDefParamVO(ParamVO[] allParamVO)
  {
    if (allParamVO == null)
      return null;
    ArrayList result = new ArrayList();
    for (int i = 0; i < allParamVO.length; i++) {
      if ((allParamVO[i].getUserdefCondClazzName() == null) || (allParamVO[i].getUserdefCondClazzName() == ""))
        continue;
      result.add(allParamVO[i]);
    }

    return (ParamVO[])(ParamVO[])result.toArray(new ParamVO[result.size()]);
  }

  protected void refreshMaterTable(String queryId, Hashtable hashParam, String dsNameForDef)
  {
    String[] ids = { queryId };

    List vecMater = QueryUtil.getQuotedMaterTables(ids, dsNameForDef);
    int iSize = vecMater.size();
    if (iSize != 0) {
      StringBuffer strHintBuf = new StringBuffer(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000541", null, new String[] { "" + iSize }));

      for (int i = 0; i < iSize; i++) {
        String[] materTableInfo = (String[])(String[])vecMater.get(i);
        strHintBuf.append(materTableInfo[0]).append("@").append(materTableInfo[1]).append("\n");
      }

      if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000099"), strHintBuf.toString()) == 4)
      {
        QueryUtil.refreshMaterTable(ids, hashParam, dsNameForDef);
      }
    }
  }

  protected StorageDataSet getDataSet(QueryModelDef qmd, Hashtable hashParam, String dsNameForDef)
    throws Exception
  {
    MultiDataSet mds = ModelUtil.getMultiQueryResult(qmd, hashParam, dsNameForDef, null);

    return mds.getDataSet();
  }

  protected void setBodyData(StorageDataSet dataSet)
  {
    CircularlyAccessibleValueObject[] cavos = DatasetUtil.getCAVOsByDataset(dataSet);

    getReportBase().setBodyDataVO(cavos);

    String[] allColCodes = getAllColumnCodes();
    int iLen = allColCodes == null ? 0 : allColCodes.length;
    for (int i = 0; i < iLen; i++)
      if (dataSet.findOrdinal(allColCodes[i]) == -1)
        getReportBase().hideColumn(allColCodes[i]);
  }

  protected Hashtable getHashParam(ParamVO[] allParams, String id, String dsNameForDef)
  {
    int iLen = allParams == null ? 0 : allParams.length;
    Hashtable hashParam = new Hashtable();
    if (iLen != 0) {
      ParamSetDlg dlg = new ParamSetDlg(this, dsNameForDef);
      dlg.setIEnvParam(getIEnvParam());
      dlg.setParamsArray(new ParamVO[][] { allParams }, new String[] { id });

      dlg.showModal();
      dlg.destroy();
      if (dlg.getResult() == 1) {
        ParamVO[] params = dlg.getParamsArray()[0];
        iLen = params == null ? 0 : params.length;

        for (int i = 0; i < iLen; i++) {
          hashParam.put(params[i].getParamCode(), params[i]);
        }

        ParamVO[] invisibleParams = QueryUtil.getInvisibleParam(allParams);

        iLen = invisibleParams == null ? 0 : invisibleParams.length;
        for (int i = 0; i < iLen; i++)
          hashParam.put(invisibleParams[i].getParamCode(), invisibleParams[i]);
      }
      else
      {
        hashParam = null;
      }
    }
    return hashParam;
  }

  protected IEnvParam getIEnvParam()
  {
    return new DefaultEnvParam();
  }

  protected QueryDLG createQueryDLG()
  {
    try
    {
      String funCode = getModuleCode();
      QueryTempletVO qt = selectDefalutQueryTemplet(funCode);
      if (qt != null) {
        QEQueryDlg dlg = new QEQueryDlg(this);
        dlg.setTempletID(getUIControl()._getPk_corp(), getModuleCode(), getUIControl()._getOperator(), null);

        dlg.setNormalShow(false);
        return dlg;
      }
      Logger.debug("该节点对应的查询模板不存在");
    }
    catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    return null;
  }

  private QueryTempletVO selectDefalutQueryTemplet(String funCode)
    throws BusinessException
  {
    IQueryTemplate name = (IQueryTemplate)NCLocator.getInstance().lookup(IQueryTemplate.class.getName());

    return name.selectDefaultQueryTempletVO(funCode);
  }

  public String getModuleCode()
  {
    String funCode = super.getModuleCode();
    Logger.debug("funCode = " + funCode);
    if (funCode.equals("100000")) {
      funCode = "108077";
    }
    return funCode;
  }

  public String getDsNameForDef()
  {
    return this.m_dsNameForDef;
  }

  public void setDsNameForDef(String dsNameForDef)
  {
    this.m_dsNameForDef = dsNameForDef;
  }

  public String getQueryId()
  {
    return this.m_queryId;
  }

  public void setQueryId(String queryId)
  {
    this.m_queryId = queryId;
  }

  protected IReportCtl createIReportCtl()
  {
    return new IReportCtl()
    {
      public String[] getGroupKeys() {
        return null;
      }

      public boolean isShowCondition() {
        return false;
      }

      public String getTableJoinClause() {
        return "";
      }

      public String getDetailReportNode() {
        return null;
      }

      public String getDefaultSqlWhere() {
        return null;
      }

      public String[] getAllTableAlias() {
        return new String[] { "" };
      }

      public String _getPk_corp() {
        String pkCorp = QEReportBase.this.getClientEnvironment().getCorporation().getPrimaryKey();

        Logger.debug("pkCorp = " + pkCorp);
        if (pkCorp == null) {
          pkCorp = "0001";
        }
        return pkCorp;
      }

      public String _getOperator() {
        String userId = QEReportBase.this.getClientEnvironment().getUser().getPrimaryKey();

        Logger.debug("userId = " + userId);
        if (userId == null) {
          userId = "0001AA10000000000ZHY";
        }
        return userId;
      }

      public String _getModuleCode() {
        return QEReportBase.this.getModuleCode();
      }
    };
  }

  protected void onPrintTemplet()
  {
    IDataSource dataSource = new CardPanelPRTS(getUIControl()._getModuleCode(), getReportBase());

    PrintEntry print = new PrintEntry(null, dataSource);
    print.setTemplateID(getUIControl()._getPk_corp(), getUIControl()._getModuleCode(), getUIControl()._getOperator(), null);

    if (print.selectTemplate() == 1)
      print.preview();
  }
}