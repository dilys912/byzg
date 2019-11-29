package nc.ui.ic.pub.tools;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic700.ICDataSet;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.ScaleValue;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class GenMethod
{
  private static Hashtable m_hashProductEnabled = null;

  public static boolean DEBUG = true;

  private static HashMap<String, ScaleValue> m_hmScaleValue = new HashMap();

  private static HashMap m_hmCorps = new HashMap();
  private static HashSet<String> m_hdatapowerrefnames;

  public static String getSysParams(String sCorpID, String sParamsCode)
    throws Exception
  {
    String sparams = null;
    try
    {
      Object[][] sparamss = CacheTool.getMultiColValue2("pub_sysinit ", "pk_corp", "initcode", new String[] { "value" }, new String[] { sCorpID }, new String[] { sParamsCode });

      sparams = (String)sparamss[0][0];
      if (sparams != null)
        sparams = sparams.trim();
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }

    return sparams;
  }

  public static boolean isProductEnabled(String pk_corp, String sProdCode)
  {
    if ((pk_corp == null) || (sProdCode == null)) {
      SCMEnv.out("isProductEnabled(String, String)传入参数不正确！");
      return false;
    }

    if (m_hashProductEnabled == null) {
      m_hashProductEnabled = new Hashtable();
    }

    String sKey = pk_corp + sProdCode;
    if (m_hashProductEnabled.containsKey(sKey)) {
      return ((UFBoolean)m_hashProductEnabled.get(sKey)).booleanValue();
    }

    boolean bEnabled = false;
    try {
      ICreateCorpQueryService srv = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      bEnabled = srv.isEnabled(pk_corp, sProdCode);
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      if ((e instanceof BusinessException))
        SCMEnv.out(e.getMessage());
      else {
        SCMEnv.out("判断产品编码为" + sProdCode + "的产品是否启用时出现系统错误，默认为未启用！");
      }
      return false;
    }

    m_hashProductEnabled.put(sKey, new UFBoolean(bEnabled));
    return bEnabled;
  }

  public static Object callICService(String classname, String methodname, Class[] ParameterTypes, Object[] ParameterValues) throws Exception {
    ServcallVO[] scd = new ServcallVO[1];
    Object oret = null;
    scd[0] = new ServcallVO();
    scd[0].setBeanName(classname);
    scd[0].setMethodName(methodname);
    scd[0].setParameterTypes(ParameterTypes);
    scd[0].setParameter(ParameterValues);
    Object[] otemps = LocalCallService.callEJBService("ic", scd);
    if ((otemps != null) && (otemps.length > 0)) {
      oret = otemps[0];
    }

    return oret;
  }
  public static Object callICEJBService(String classname, String methodname, Class[] ParameterTypes, Object[] ParameterValues) throws Exception {
    ServcallVO[] scd = new ServcallVO[1];
    Object oret = null;
    scd[0] = new ServcallVO();
    scd[0].setBeanName(classname);
    scd[0].setMethodName(methodname);
    scd[0].setParameterTypes(ParameterTypes);
    scd[0].setParameter(ParameterValues);
    Object[] otemps = LocalCallService.callEJBService("ic", scd);
    if ((otemps != null) && (otemps.length > 0)) {
      oret = otemps[0];
    }

    return oret;
  }

  public static boolean closeFunWindow(String funcode)
  {
    if (funcode == null) {
      return false;
    }
    try
    {
      List openModules = ClientEnvironment.getInstance().getOpenModules();
      Iterator it = openModules.iterator();
      while (it.hasNext()) {
        IFuncWindow window = (IFuncWindow)it.next();
        if (window.getFuncPanel().getModuleCode().equals(funcode))
          return window.closeWindow();
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
      return false;
    }
    return true;
  }

  public static ICDataSet queryData(String[] pknames, String[] fieldnames, int[] fieldtypes, String sql)
  {
    if ((sql == null) || (sql.trim().length() <= 0)) {
      return null;
    }
    try
    {
      return (ICDataSet)callICService("nc.bs.ic.pub.vmi.ICSmartToolsDmo", "getDataSet", new Class[] { java.lang.String.class, java.lang.String.class, GenMethod.class, String.class }, new Object[] { pknames, fieldnames, fieldtypes, sql });
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    return null;
  }

  public static ICDataSet queryData(String table, String pkname, String[] fieldnames, int[] fieldtypes, String[] pkvalues, String swhere)
  {
    if ((table == null) || (pkname == null) || (fieldnames == null)) {
      return null;
    }
    try
    {
      return (ICDataSet)callICService("nc.bs.ic.pub.vmi.ICSmartToolsDmo", "getDataSet", new Class[] { String.class, String.class, java.lang.String.class, GenMethod.class, java.lang.String.class, String.class }, new Object[] { table, pkname, fieldnames, fieldtypes, pkvalues, swhere });
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    return null;
  }

  public static BusinessException handleException(Container tfui, String msg, Throwable e)
  {
    Logger.error(msg, e);

    if (tfui != null) {
      BusinessException be = nc.vo.ic.pub.GenMethod.getRealBusiException(e);
      if (((msg == null) || (msg.trim().length() <= 0)) && (be != null))
        msg = be.getMessage();
      if (msg == null)
        msg = ResBase.getStopAction();
      if (be == null)
        be = new BusinessException(e.getMessage(), e);
      showErro(tfui, be.getMessage());
      return be;
    }

    BusinessException be = nc.vo.ic.pub.GenMethod.getRealBusiException(e);
    if (be != null) {
      return be;
    }
    return new BusinessException(msg == null ? e.getMessage() : msg, e);
  }

  public static void showErro(Container parent, String msg)
  {
    if ((parent != null) && ((parent instanceof ToftPanel)))
      ((ToftPanel)parent).showErrorMessage(msg);
    else
      MessageDialog.showErrorDlg(parent, NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019"), msg);
  }

  public static ScaleValue getScaleValue(String pk_corp)
  {
    if (!m_hmScaleValue.containsKey(pk_corp))
    {
      initSysParam(pk_corp, null);
    }

    return (ScaleValue)m_hmScaleValue.get(pk_corp);
  }

  public static ArrayList getCorpArryByUser(String user)
  {
    if (!m_hmCorps.containsKey(user))
    {
      initSysParam(null, user);
    }

    return (ArrayList)m_hmCorps.get(user);
  }

  protected static void initSysParam(String pk_corp, String user)
  {
    try
    {
      if (pk_corp == null)
        pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
      if (user == null) {
        user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
      }

      String[] saParam = { "BD501", "BD502", "BD503", "BD504", "BD301" };

      ArrayList alAllParam = new ArrayList();

      ArrayList alParam = new ArrayList();
      alParam.add(pk_corp);
      alParam.add(saParam);
      alAllParam.add(alParam);

      alAllParam.add(user);

      ArrayList alRetData = (ArrayList)ICReportHelper.queryInfo(new Integer(11), alAllParam);

      if ((alRetData == null) || (alRetData.size() < 2)) {
        throw new BusinessException(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000284"));
      }

      ScaleValue sv = new ScaleValue();

      String[] saParamValue = (String[])(String[])alRetData.get(0);
      if ((saParamValue != null) && (saParamValue.length > 4))
      {
        sv.setNumScale(Integer.parseInt(saParamValue[0]));

        sv.setAssistNumScale(Integer.parseInt(saParamValue[1]));

        sv.setHslScale(Integer.parseInt(saParamValue[2]));

        sv.setPriceScale(Integer.parseInt(saParamValue[3]));

        sv.setMnyScale(Integer.parseInt(saParamValue[4]));
        m_hmScaleValue.put(pk_corp, sv);
      }

      m_hmCorps.put(user, (ArrayList)alRetData.get(1));
    }
    catch (Exception e) {
      SCMEnv.out("can not get para" + e.getMessage());
    }
  }

  public static HashSet<String> getHDataPowerRefNames()
  {
    if (m_hdatapowerrefnames == null) {
      m_hdatapowerrefnames = new HashSet();
      for (int i = 0; i < ICConst.datapowerrefnames.length; i++)
        m_hdatapowerrefnames.add(ICConst.datapowerrefnames[i]);
    }
    return m_hdatapowerrefnames;
  }

  public static boolean isDataPowerRef(String refname)
  {
    if (refname == null)
      return false;
    return getHDataPowerRefNames().contains(refname);
  }

  public static String[] getDataPowerFieldFromDlgByProp(QueryConditionClient dlg)
  {
    return getDataPowerFieldFromDlg(dlg, true, null);
  }

  public static String[] getDataPowerFieldFromDlgNotByProp(QueryConditionClient dlg)
  {
    return getDataPowerFieldFromDlg(dlg, false, null);
  }

  public static String[] getDataPowerFieldFromDlg(QueryConditionClient dlg, boolean isbypower, String[] notincludefield)
  {
    if (dlg == null)
      return null;
    QueryConditionVO[] vos = dlg.getConditionDatas();
    if ((vos == null) || (vos.length <= 0))
      return null;
    ArrayList codelist = new ArrayList(20);

    HashSet hsnotinclude = new HashSet(30);
    if (notincludefield != null) {
      for (int i = 0; i < notincludefield.length; i++)
        hsnotinclude.add(notincludefield[i]);
    }
    for (int i = 0; i < vos.length; i++) {
      if ((!vos[i].getIfUsed().booleanValue()) || ((isbypower) && (!dlg.isDataPower()) && (!vos[i].getIfDataPower().booleanValue())) || (vos[i].getDataType().intValue() != 5) || (vos[i].getConsultCode() == null) || (!isDataPowerRef(vos[i].getConsultCode().trim())) || (vos[i].getFieldCode() == null) || (hsnotinclude.contains(vos[i].getFieldCode().trim())) || (codelist.contains(vos[i].getFieldCode().trim())))
      {
        continue;
      }

      codelist.add(vos[i].getFieldCode().trim());
    }
    if (codelist.size() > 0)
      return (String[])codelist.toArray(new String[codelist.size()]);
    return null;
  }

  public static void setDataPowerFlag(QueryConditionClient dlg, boolean ispower, String[] fields)
  {
    if ((dlg == null) || (fields == null) || (fields.length <= 0))
      return;
    QueryConditionVO[] vos = dlg.getConditionDatas();
    if ((vos == null) || (vos.length <= 0)) {
      return;
    }
    HashSet fs = new HashSet(10);
    for (int i = 0; i < fields.length; i++) {
      fs.add(fields[i]);
    }
    UFBoolean b = new UFBoolean(ispower);
    for (int i = 0; i < vos.length; i++)
      if ((vos[i].getFieldCode() != null) && (fs.contains(vos[i].getFieldCode())))
        vos[i].setIfDataPower(b);
  }

  public static void setDataPowerFlagByRefName(QueryConditionClient dlg, boolean ispower, String[] refnames)
  {
    if ((dlg == null) || (refnames == null) || (refnames.length <= 0))
      return;
    QueryConditionVO[] vos = dlg.getConditionDatas();
    if ((vos == null) || (vos.length <= 0)) {
      return;
    }
    HashSet fs = new HashSet(10);
    for (int i = 0; i < refnames.length; i++) {
      fs.add(refnames[i]);
    }
    UFBoolean b = new UFBoolean(ispower);
    for (int i = 0; i < vos.length; i++) {
      if ((!vos[i].getIfUsed().booleanValue()) || (vos[i].getDataType().intValue() != 5) || (vos[i].getConsultCode() == null) || (!fs.contains(vos[i].getConsultCode().trim())))
      {
        continue;
      }
      vos[i].setIfDataPower(b);
    }
  }

  public static boolean isEdit(BillCardPanel cardpanel)
  {
    if (cardpanel == null)
      return false;
    return cardpanel.getBillData().getEnabled();
  }

  public static ConditionVO[] procMultCorpDeptBizDP(ConditionVO[] allcons, String billtype, String logpkcorp)
  {
    if ((allcons == null) || (allcons.length <= 0) || (logpkcorp == null))
      return allcons;
    if ((BillTypeConst.m_purchaseIn.equals(billtype)) || (BillTypeConst.m_saleOut.equals(billtype)) || (BillTypeConst.m_allocationIn.equals(billtype)) || (BillTypeConst.m_allocationOut.equals(billtype)))
    {
      ArrayList volist = new ArrayList(allcons.length + 2);
      ConditionVO addvo = null;
      for (int i = 0; i < allcons.length; i++) {
        volist.add(allcons[i]);
        if ((allcons[i].getFieldCode() == null) || ((!allcons[i].getFieldCode().trim().toLowerCase().endsWith("cdptid")) && (!allcons[i].getFieldCode().trim().toLowerCase().endsWith("cbizid"))) || (allcons[i].getOperaCode() == null) || (!allcons[i].getOperaCode().trim().equals("is")))
          continue;
        try
        {
          addvo = (ConditionVO)ObjectUtils.serializableClone(allcons[i]);
          if (allcons[i].getFieldCode().trim().toLowerCase().endsWith("cdptid")) {
            addvo.setValue(" ( select pk_deptdoc from bd_deptdoc where pk_corp!='" + logpkcorp + "') ");
            addvo.setOperaCode(" in ");
          } else {
            addvo.setValue(" ( select pk_psndoc from bd_psndoc where pk_corp!='" + logpkcorp + "') ");
            addvo.setOperaCode(" in ");
          }
          addvo.setLogic(false);
          addvo.setNoLeft(true);
          addvo.setNoRight(true);
          volist.add(addvo);
        } catch (Exception ex) {
          handleException(null, null, ex);
        }
      }

      return (ConditionVO[])volist.toArray(new ConditionVO[volist.size()]);
    }
    return allcons;
  }
}