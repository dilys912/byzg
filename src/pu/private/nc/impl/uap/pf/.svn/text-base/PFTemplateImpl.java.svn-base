package nc.impl.uap.pf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import nc.bp.impl.uap.template.TemplateBpImpl;
import nc.bp.itf.template.ITemplateBP;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.billtemplate.IBillTemplateQry;
import nc.itf.uap.pf.IPFTemplate;
import nc.itf.uap.print.IPrintTemplateQry;
import nc.itf.uap.querytemplate.IQueryTemplate;
import nc.itf.uap.reporttemplate.IReportTemplateQry;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pftemplate.AssignTemplateInfo;
import nc.vo.pub.pftemplate.FunccodetocodeVO;
import nc.vo.pub.pftemplate.SystemplateVO;
import nc.vo.pub.print.PrintTemplateVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.pub.query.QueryTempletVO;
import nc.vo.pub.report.ReportTempletVO;
import nc.vo.pub.template.TemplateRuntimeException;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.TemplateParaVO;

public class PFTemplateImpl
  implements IPFTemplate
{
  private ITemplateBP tempBp = null;

  private IUAPQueryBS uapQryBS = null;

  private IBillTemplateQry billTemplateQry = null;

  private IQueryTemplate qryTemplate = null;

  private IPrintTemplateQry printTemplateQry = null;

  private IReportTemplateQry reportTemplateQry = null;

  public String getTemplateId(TemplateParaVO tptParaVo)
    throws BusinessException
  {
    try
    {
      return getTempBp().queryTemplateId(tptParaVo);
    } catch (BusinessException e) {
      Logger.error(e.getMessage());
    throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList getTemplateVOs(TemplateParaVO paravo)
    throws BusinessException
  {
    return getTempBp().queryTemplateVOs(paravo);
  }

  public String[] getSingleTempIdAry(TemplateParaVO tptParaVo)
    throws BusinessException
  {
    String[] retAry = getTempBp().queryUserTemplateIds(tptParaVo);

    if (retAry == null) {
      retAry = getTempBp().queryDefaTemplateIds(tptParaVo);
    }
    return retAry;
  }

  public String[] getComplexTempIdAry(TemplateParaVO tptParaVo)
    throws BusinessException
  {
    String[] retAryDef = getTempBp().queryUserTemplateIds(tptParaVo);

    String[] retAryDefa = getTempBp().queryDefaTemplateIds(tptParaVo);
    ArrayList al = new ArrayList();
    if (retAryDef != null) {
      al.addAll(Arrays.asList(retAryDef));
    }
    if (retAryDefa != null) {
      al.addAll(Arrays.asList(retAryDefa));
    }
    if (al.size() > 0) {
      return (String[])(String[])al.toArray(new String[al.size()]);
    }
    throw new BusinessException("未设置默认模板");
  }

  public boolean isTemplateUsed(String templateid, int tempstyle)
    throws BusinessException
  {
    String conditon = "templateid = '" + templateid + "' and tempstyle =" + tempstyle;

    Collection c = null;
    try {
      BaseDAO baseDao = new BaseDAO();
      c = baseDao.retrieveByClause(SystemplateVO.class, conditon);
    } catch (BusinessException e) {
      Logger.error(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return (c != null) && (c.size() != 0);
  }

  public String[] changeSystemplates(SystemplateVO[] removedVOs, SystemplateVO[] newVOs, SystemplateVO[] updateVOs)
    throws BusinessException
  {
    String[] keys = null;
    try {
      BaseDAO dao = new BaseDAO();

      if ((removedVOs != null) && (removedVOs.length != 0)) {
        dao.deleteVOArray(removedVOs);
      }

      if ((newVOs != null) && (newVOs.length != 0)) {
        keys = dao.insertVOArray(newVOs);
      }
      if ((updateVOs != null) && (updateVOs.length != 0))
        dao.updateVOArray(updateVOs);
    } catch (Exception e) {
      Logger.error(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    return keys;
  }

  public ArrayList queryNodekeys(String funCode, int[] templateTypes)
    throws BusinessException
  {
    String sql = "select distinct nodekey from pub_systemplate where funnode=? and tempstyle=? and templateflag='Y'";

    PersistenceManager persist = null;
    ArrayList alRet = new ArrayList();
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession session = persist.getJdbcSession();
      for (int i = 0; i < templateTypes.length; i++) {
        SQLParameter param = new SQLParameter();
        param.addParam(funCode);
        param.addParam(templateTypes[i]);
        List result = (List)session.executeQuery(sql, param, new ColumnListProcessor("nodekey"));

        String[] keys = (String[])(String[])result.toArray(new String[result.size()]);

        alRet.add(keys);
      }
    } catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release();
    }
    return alRet;
  }

  public ArrayList querySystemplatesByOperator(String funnode, String operatorpk, String pk_org, int operatortype)
    throws BusinessException
  {
    String sql = "select * from pub_systemplate where funnode='" + funnode + "' and operator_type = " + operatortype + " and operator='" + operatorpk + "' and pk_org='" + pk_org + "'";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      ArrayList alSys = (ArrayList)jdbc.executeQuery(sql, new BeanListProcessor(SystemplateVO.class));

      for (Iterator iter = alSys.iterator(); iter.hasNext(); ) {
        SystemplateVO stVO = (SystemplateVO)iter.next();
        int tempstyle = stVO.getTempstyle().intValue();
        String tempid = stVO.getTemplateid();
        String nameSQL = null;
        switch (tempstyle) {
        case 0:
          nameSQL = "select bill_templetname from pub_billtemplet where\tpk_billtemplet ='" + tempid + "'";

          break;
        case 3:
          nameSQL = "select vtemplatename from pub_print_template where ctemplateid ='" + tempid + "'";

          break;
        case 1:
          nameSQL = "select model_name from pub_query_templet where id ='" + tempid + "'";

          break;
        case 2:
          nameSQL = "select node_name from pub_report_templet where pk_templet ='" + tempid + "'";

          break;
        }

        Object obj = jdbc.executeQuery(nameSQL, new ColumnProcessor(1));
        stVO.setTemplatename(String.valueOf(obj));
      }
   //   iter = alSys;
      return alSys;
    }
    catch (DbException e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
   // throw localObject1;
  }

  public OrganizeUnit[] getAssignedRoleUsers(String corppk, String funcnode, String nodekey, int templatestyle, String templateid)
    throws BusinessException
  {
    String sqlNodekey = " nodekey ='" + nodekey + "'";

    String sqlUser = "select cuserid,user_code,user_name from sm_user where cuserid in(select operator from pub_systemplate where templateid=? and funnode=? and pk_org=? and tempstyle=? and " + sqlNodekey + ")";

    String sqlRole = "select pk_role,role_code,role_name from sm_role where pk_role in(select operator from pub_systemplate where templateid=? and funnode=? and pk_org=? and tempstyle=? and" + sqlNodekey + ")";

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();
      SQLParameter para = new SQLParameter();
      para.addParam(templateid);
      para.addParam(funcnode);
      para.addParam(corppk);
      para.addParam(templatestyle);
      ArrayList aryResult = (ArrayList)jdbc.executeQuery(sqlUser, para, new OrgUnitProcessor(1, corppk));

      aryResult.addAll((ArrayList)jdbc.executeQuery(sqlRole, para, new OrgUnitProcessor(2, corppk)));

      OrganizeUnit[] arrayOfOrganizeUnit = (OrganizeUnit[])(OrganizeUnit[])aryResult.toArray(new OrganizeUnit[aryResult.size()]);
      return arrayOfOrganizeUnit;
    }
    catch (Exception e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
 //   throw localObject;
  }

  public boolean changeRoleUserTemplate(String funcnode, String templateid, int templateStyle, String corppk, String orgpk, String orgTypeCode, String nowNodekey, Object[] results)
    throws BusinessException
  {
    boolean succee = false;
    if (results != null) {
      OrganizeUnit[] adds = (OrganizeUnit[])(OrganizeUnit[])results[0];
      OrganizeUnit[] dels = (OrganizeUnit[])(OrganizeUnit[])results[1];
      PersistenceManager persist = null;
      try {
        persist = PersistenceManager.getInstance();
        if ((adds != null) && (adds.length > 0)) {
          ArrayList aryInsertvos = new ArrayList();
          SystemplateVO insertvo = null;
          for (int i = 0; i < adds.length; i++) {
            insertvo = new SystemplateVO();
            insertvo.setFunnode(funcnode);
            insertvo.setTemplateid(templateid);
            insertvo.setTempstyle(new Integer(templateStyle));
            insertvo.setNodekey(nowNodekey);
            insertvo.setOperator(adds[i].getPk());
            insertvo.setOperator_type(new Integer(adds[i].getOrgUnitType()));

            insertvo.setOrgtypecode(orgTypeCode);
            insertvo.setPk_org(orgpk);
            insertvo.setPk_corp(corppk);
            insertvo.setTemplateflag(UFBoolean.FALSE);
            insertvo.setIscomm(UFBoolean.FALSE);
            aryInsertvos.add(insertvo);
          }

          persist.insert((SystemplateVO[])(SystemplateVO[])aryInsertvos.toArray(new SystemplateVO[aryInsertvos.size()]));
        }

        if ((dels != null) && (dels.length > 0)) {
          String commonsql = "templateid='" + templateid + "' and tempstyle=" + templateStyle + " and funnode ='" + funcnode + "' and pk_org='" + orgpk + "' and pk_corp='" + corppk + "' and orgtypecode ='" + orgTypeCode + "'";

          if (nowNodekey == null)
            commonsql = commonsql + " and nodekey is null";
          else
            commonsql = commonsql + " and nodekey='" + nowNodekey + "' ";
          for (int i = 0; i < dels.length; i++) {
            String wheresql = commonsql + " and operator='" + dels[i].getPk() + "' and operator_type=" + dels[i].getOrgUnitType();

            persist.deleteByClause(SystemplateVO.class, wheresql);
          }
        }
        succee = true;
      } catch (Exception e) {
        succee = false;
        Logger.error(e.getMessage(), e);
        throw new TemplateRuntimeException(e.getMessage());
      } finally {
        if (persist != null)
          persist.release();
      }
    }
    return succee;
  }

  public void copyInsertTemplateByBillType(String mainBillType, String newBillType)
    throws BusinessException
  {
    Collection cc = querySysTemplateByBillType(mainBillType, -1);
    if ((cc != null) && (cc.size() > 0)) {
      SystemplateVO[] sysvos = new SystemplateVO[cc.size()];
      int i = 0;
      for (Iterator ii = cc.iterator(); ii.hasNext(); i++) {
        sysvos[i] = ((SystemplateVO)ii.next());
        sysvos[i].setNodekey(newBillType);
        sysvos[i].setSysflag(new Integer(1));
        sysvos[i].setDr(new Integer(0));
      }
      BaseDAO dao = new BaseDAO();
      dao.insertVOArray(sysvos);
    }
    Logger.debug(">>>>成功拷贝插入默认模板<<<<");
  }

  public void deleteSysTemplateForARAP(String nodekey) throws BusinessException
  {
    IVOPersistence vopersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());

    String sql = "nodekey = '" + nodekey + "'";
    vopersistence.deleteByClause(SystemplateVO.class, sql);
  }

  public void copyInsertCorpDefaultTemplate(String mainBillType, String newBillType, String corppk, String templateid)
    throws BusinessException
  {
    Collection cc = querySysTemplateByBillType(mainBillType, 0);

    if ((cc != null) && (cc.size() > 0)) {
      SystemplateVO[] sysvos = new SystemplateVO[cc.size()];
      int i = 0;
      for (Iterator ii = cc.iterator(); ii.hasNext(); i++) {
        sysvos[i] = ((SystemplateVO)ii.next());
        sysvos[i].setTemplateflag(UFBoolean.TRUE);
        sysvos[i].setNodekey(newBillType);
        sysvos[i].setPk_corp(corppk);
        sysvos[i].setPk_org(corppk);
        sysvos[i].setTemplateid(templateid);
        sysvos[i].setSysflag(new Integer(1));
        sysvos[i].setDr(new Integer(0));
      }
      BaseDAO dao = new BaseDAO();
      dao.insertVOArray(sysvos);
    }
    Logger.debug(">>>>成功拷贝插入公司默认单据模板<<<<");
  }

  public void copyInsertCorpPrintDefaultTemplate(String mainBillType, String newBillType, String corppk, String templateid)
    throws BusinessException
  {
    Collection cc = querySysTemplateByBillType(mainBillType, 3);

    if ((cc != null) && (cc.size() > 0)) {
      SystemplateVO[] sysvos = new SystemplateVO[cc.size()];
      int i = 0;
      for (Iterator ii = cc.iterator(); ii.hasNext(); i++) {
        sysvos[i] = ((SystemplateVO)ii.next());
        sysvos[i].setTemplateflag(UFBoolean.TRUE);
        sysvos[i].setNodekey(newBillType);
        sysvos[i].setPk_corp(corppk);
        sysvos[i].setPk_org(corppk);
        sysvos[i].setTemplateid(templateid);
        sysvos[i].setSysflag(new Integer(1));
        sysvos[i].setDr(new Integer(0));
      }
      BaseDAO dao = new BaseDAO();
      dao.insertVOArray(sysvos);
    }
    Logger.debug(">>>>成功拷贝插入公司默认打印模板<<<<");
  }

  private Collection querySysTemplateByBillType(String mainBillType, int templatestyle)
    throws BusinessException
  {
    IUAPQueryBS query = getUapQryBS();
    String billtypecodition = "pk_billtypecode='" + mainBillType + "'";
    Collection collection = query.retrieveByClause(BilltypeVO.class, billtypecodition, new String[] { "nodecode" });

    if ((collection != null) && (collection.size() > 0))
    {
      String inNodecode = "";
      for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
        inNodecode = inNodecode + "'" + ((BilltypeVO)iterator.next()).getNodecode() + "' ,";
      }

      inNodecode = inNodecode.substring(0, inNodecode.length() - 1);
      String codtion = "mainnodecode in(" + inNodecode + ")";
      Collection assNodecode = query.retrieveByClause(FunccodetocodeVO.class, codtion);
      Iterator iter;
      if ((assNodecode != null) && (assNodecode.size() > 0)) {
        inNodecode = inNodecode + " ,";
        for (iter = assNodecode.iterator(); iter.hasNext(); ) {
          inNodecode = inNodecode + "'" + ((FunccodetocodeVO)iter.next()).getNodecode() + "' ,";
        }

      }

      inNodecode = inNodecode.substring(0, inNodecode.length() - 1);
      String templateCondtion = "";
      if (templatestyle < 0) {
        Logger.debug(">>>>查询符合条件的系统默认模板!");
        templateCondtion = "templateflag ='Y' and (pk_corp='0001' or pk_corp='@@@@') and nodekey = '" + mainBillType + "' and funnode in(" + inNodecode + ")";
      }
      else if (templatestyle == 0) {
        Logger.debug(">>>>查询符合条件的单据模板!");
        templateCondtion = "templateflag ='Y' and tempstyle=0 and (pk_corp='0001' or pk_corp='@@@@') and nodekey = '" + mainBillType + "' and funnode in(" + inNodecode + ")";
      }
      else if (templatestyle == 3) {
        Logger.debug(">>>>查询符合条件的打印模板!");
        templateCondtion = "templateflag ='Y' and tempstyle=3 and (pk_corp='0001' or pk_corp='@@@@') and nodekey = '" + mainBillType + "' and funnode in(" + inNodecode + ")";
      }

      return query.retrieveByClause(SystemplateVO.class, templateCondtion);
    }

    Logger.debug("没有查询到对应单据类型" + mainBillType + "的功能节点");

    return null;
  }

  public ArrayList getAssignTemplateInfos(String funcode, String corppk)
    throws BusinessException
  {
    ArrayList aryAssignTempalteInfo = new ArrayList();

    String mainNode = getMainNodeCodeByNodecode(funcode);
    BillTempletHeadVO[] billTemplateVOsSelfAndMain = null;

    ReportTempletVO[] reportTemplateVOsSelf = null;
    ReportTempletVO[] reportTemplateVOsMain = null;
    PrintTemplateVO[] printTemplateVOsSelf = null;
    PrintTemplateVO[] printTemplateVOsMain = null;

    reportTemplateVOsSelf = getReportTemplateQry().queryTempletsByParentCode(funcode, corppk);

    printTemplateVOsSelf = getPrintTemplateQry().getTempletByModule(funcode, corppk);

    String qryTempSQL = null;
    if (mainNode != null)
    {
      billTemplateVOsSelfAndMain = getBillTemplateQry().findTempletIDsByNodeCodesAndPkCorp(new String[] { funcode, mainNode }, corppk);

      qryTempSQL = "node_code in ('" + funcode + "' , '" + mainNode + "' ) and (pk_corp = '" + corppk + "'" + " or pk_corp='0001')";

      reportTemplateVOsMain = getReportTemplateQry().queryTempletsByParentCode(mainNode, corppk);

      printTemplateVOsMain = getPrintTemplateQry().getTempletByModule(mainNode, corppk);
    }
    else
    {
      billTemplateVOsSelfAndMain = getBillTemplateQry().findTempletIDsByNodeCodeAndPkCorp(funcode, corppk);

      qryTempSQL = "node_code = '" + funcode + "' and (pk_corp = '" + corppk + "'" + " or pk_corp='0001')";
    }

    QueryTempletTotalVO[] qryTemplateAggVOs = getQryTemplate().queryQueryTempletTotalVOByWherePart(qryTempSQL);

    tranlateVO2AssignInfo(billTemplateVOsSelfAndMain, aryAssignTempalteInfo);

    tranlateVO2AssignInfo(qryTemplateAggVOs, aryAssignTempalteInfo);
    tranlateVO2AssignInfo(reportTemplateVOsSelf, aryAssignTempalteInfo);
    tranlateVO2AssignInfo(reportTemplateVOsMain, aryAssignTempalteInfo);
    tranlateVO2AssignInfo(printTemplateVOsSelf, corppk, aryAssignTempalteInfo);

    tranlateVO2AssignInfo(printTemplateVOsMain, corppk, aryAssignTempalteInfo);

    return aryAssignTempalteInfo;
  }

  private ITemplateBP getTempBp()
  {
    if (this.tempBp == null)
      this.tempBp = new TemplateBpImpl();
    return this.tempBp;
  }

  private IUAPQueryBS getUapQryBS() {
    if (this.uapQryBS == null) {
      this.uapQryBS = ((IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()));
    }

    return this.uapQryBS;
  }

  private IBillTemplateQry getBillTemplateQry() {
    if (this.billTemplateQry == null) {
      this.billTemplateQry = ((IBillTemplateQry)NCLocator.getInstance().lookup(IBillTemplateQry.class.getName()));
    }

    return this.billTemplateQry;
  }

  private IQueryTemplate getQryTemplate() {
    if (this.qryTemplate == null) {
      this.qryTemplate = ((IQueryTemplate)NCLocator.getInstance().lookup(IQueryTemplate.class.getName()));
    }

    return this.qryTemplate;
  }

  private IPrintTemplateQry getPrintTemplateQry() {
    if (this.printTemplateQry == null) {
      this.printTemplateQry = ((IPrintTemplateQry)NCLocator.getInstance().lookup(IPrintTemplateQry.class.getName()));
    }

    return this.printTemplateQry;
  }

  private IReportTemplateQry getReportTemplateQry() {
    if (this.reportTemplateQry == null) {
      this.reportTemplateQry = ((IReportTemplateQry)NCLocator.getInstance().lookup(IReportTemplateQry.class.getName()));
    }

    return this.reportTemplateQry;
  }

  private String getMainNodeCodeByNodecode(String nodecode)
    throws BusinessException
  {
    String conditons = "nodecode ='" + nodecode + "'";
    Collection collection = getUapQryBS().retrieveByClause(FunccodetocodeVO.class, conditons, new String[] { "mainnodecode" });

    if ((collection != null) && (collection.size() > 0)) {
      return ((FunccodetocodeVO)collection.iterator().next()).getMainnodecode();
    }
    return null;
  }

  private void tranlateVO2AssignInfo(ReportTempletVO[] reportTemplateVOs, ArrayList aryAssignTempalteInfo)
  {
    if ((reportTemplateVOs != null) && (reportTemplateVOs.length > 0))
      for (int i = 0; i < reportTemplateVOs.length; i++) {
        AssignTemplateInfo assignInfo = new AssignTemplateInfo();
        assignInfo.setTemplateStyle(2);
        assignInfo.setTemplateId(reportTemplateVOs[i].getPrimaryKey());
        assignInfo.setTemplateName(reportTemplateVOs[i].getNodeName());
        assignInfo.setPkCorp(reportTemplateVOs[i].getPkCorp());
        assignInfo.setGroup(reportTemplateVOs[i].getPkCorp().equals("0001"));

        assignInfo.setSelected(false);
        aryAssignTempalteInfo.add(assignInfo);
      }
  }

  private void tranlateVO2AssignInfo(PrintTemplateVO[] printTemplateVOs, String corppk, ArrayList aryAssignTempalteInfo)
  {
    if ((printTemplateVOs != null) && (printTemplateVOs.length > 0))
      for (int i = 0; i < printTemplateVOs.length; i++) {
        if ((!printTemplateVOs[i].getPk_corp().equals(corppk)) && (!"0001".equals(printTemplateVOs[i].getPk_corp()))) {
          continue;
        }
        AssignTemplateInfo assignInfo = new AssignTemplateInfo();
        assignInfo.setTemplateStyle(3);
        assignInfo.setTemplateId(printTemplateVOs[i].getPrimaryKey());
        assignInfo.setTemplateName(printTemplateVOs[i].getVtemplatename());

        assignInfo.setPkCorp(printTemplateVOs[i].getPk_corp());
        assignInfo.setGroup(assignInfo.getPkCorp().equals("0001"));

        assignInfo.setSelected(false);
        aryAssignTempalteInfo.add(assignInfo);
      }
  }

  private void tranlateVO2AssignInfo(QueryTempletTotalVO[] qryTemplateAggVOs, ArrayList aryAssignTempalteInfo)
  {
    if ((qryTemplateAggVOs != null) && (qryTemplateAggVOs.length > 0))
      for (int i = 0; i < qryTemplateAggVOs.length; i++) {
        QueryTempletVO aryTemVO = qryTemplateAggVOs[i].getTempletVO();
        AssignTemplateInfo assignInfo = new AssignTemplateInfo();
        assignInfo.setTemplateStyle(1);
        assignInfo.setTemplateId(aryTemVO.getPrimaryKey());
        assignInfo.setTemplateName(aryTemVO.getModelName());
        assignInfo.setPkCorp(aryTemVO.getPkCorp());
        assignInfo.setGroup(assignInfo.getPkCorp().equals("0001"));

        assignInfo.setSelected(false);
        aryAssignTempalteInfo.add(assignInfo);
      }
  }

  private void tranlateVO2AssignInfo(BillTempletHeadVO[] billTemplateVOs, ArrayList aryAssignTempalteInfo)
  {
    if ((billTemplateVOs != null) && (billTemplateVOs.length > 0))
      for (int i = 0; i < billTemplateVOs.length; i++) {
        AssignTemplateInfo assignInfo = new AssignTemplateInfo();
        assignInfo.setTemplateStyle(0);
        assignInfo.setTemplateId(billTemplateVOs[i].getPrimaryKey());
        assignInfo.setTemplateName(billTemplateVOs[i].getBillTempletName());

        assignInfo.setPkCorp(billTemplateVOs[i].getPkCorp());
        assignInfo.setGroup(billTemplateVOs[i].getPkCorp().equals("0001"));

        assignInfo.setSelected(false);
        aryAssignTempalteInfo.add(assignInfo);
      }
  }

  class OrgUnitProcessor extends BaseProcessor {
    int operType = 0;
    String corppk;

    public OrgUnitProcessor(int operator_INT, String corppk) {
      this.operType = operator_INT;
      this.corppk = corppk;
    }

    public Object processResultSet(ResultSet rs) throws SQLException {
      if (null == rs) {
        return null;
      }
      ArrayList al = new ArrayList();
      while (rs.next()) {
        OrganizeUnit orgUnit = new OrganizeUnit();
        orgUnit.setPk(rs.getString(1));
        orgUnit.setCode(rs.getString(2));
        orgUnit.setName(rs.getString(3));
        orgUnit.setPkCorp(this.corppk);
        orgUnit.setOrgUnitType(this.operType);
        al.add(orgUnit);
      }
      return al;
    }
  }
}