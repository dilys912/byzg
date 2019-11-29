package nc.bp.impl.uap.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import nc.bp.itf.template.ITemplateBP;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.uap.bd.multibook.IGLOrgBookAcc;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pftemplate.SystemplateVO;
import nc.vo.pub.template.TemplateException;
import nc.vo.pub.template.TemplateRuntimeException;
import nc.vo.uap.pf.TemplateParaVO;

public class TemplateBpImpl
  implements ITemplateBP
{
  public String queryTemplateId(TemplateParaVO tptParaVo)
    throws BusinessException
  {
    String templateId = getTemplateId(tptParaVo);
    if (templateId == null) {
      throw new TemplateException(NCLangResOnserver.getInstance().getStrByID("_Template", "UPP_Template-000499") + ":" + tptParaVo.getTemplateType() + "," + NCLangResOnserver.getInstance().getStrByID("_Template", "UPP_Template-000500") + ":" + tptParaVo.getPk_orgUnit() + NCLangResOnserver.getInstance().getStrByID("_Template", "UPP_Template-000501") + ":" + tptParaVo.getOperator() + NCLangResOnserver.getInstance().getStrByID("_Template", "UPP_Template-000233") + ":" + tptParaVo.getFunNode());
    }

    return templateId;
  }

  private String getTemplateId(TemplateParaVO tptParaVo)
  {
    if (tptParaVo.getPk_orgUnit() == null) {
      throw new TemplateRuntimeException("错误：组织PK不可为空！");
    }

    if ((tptParaVo.getOperator() == null) || (tptParaVo.getOperator().trim().equals("")))
    {
      return getDefaultTemplate(tptParaVo);
    }

    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();

      String baseSql = "select templateflag,templateid,operator,pk_corp,nodekey from pub_systemplate where funnode = ? and tempstyle = ? ";

      SQLParameter userParam = new SQLParameter();
      StringBuffer userSqlBuf = new StringBuffer();
      userSqlBuf.append(baseSql);

      userSqlBuf.append("and (templateflag='Y' or ");

      constructUserParams(tptParaVo, userParam, userSqlBuf, true);
      userSqlBuf.append(")");
      List userTemplateList = (List)jdbc.executeQuery(userSqlBuf.toString(), userParam, new BeanListProcessor(SystemplateVO.class));

      for (Iterator iter = userTemplateList.iterator(); iter.hasNext(); ) {
        SystemplateVO tempVO = (SystemplateVO)iter.next();

        if (tptParaVo.getOperator().equals(tempVO.getOperator())) {
          String str1 = tempVO.getTemplateid();
          return str1;
        }
      }
      SystemplateVO[] defaultTemplates = (SystemplateVO[])(SystemplateVO[])userTemplateList.toArray(new SystemplateVO[userTemplateList.size()]);

      SQLParameter roleParam = new SQLParameter();
      StringBuffer roleSqlBuf = new StringBuffer();
      roleSqlBuf.append(baseSql);

      constructRoleParams(tptParaVo, roleParam, roleSqlBuf, true);

      List roleTemplateList = (List)jdbc.executeQuery(roleSqlBuf.toString(), roleParam, new BeanListProcessor(SystemplateVO.class));

      if ((roleTemplateList != null) && (!roleTemplateList.isEmpty()))
      {
        Iterator privateIter = roleTemplateList.iterator();
        while (privateIter.hasNext()) {
          SystemplateVO tempVO = (SystemplateVO)privateIter.next();
          if (tempVO.getPk_corp().equals(tptParaVo.getPk_Corp())) {
            String str2 = tempVO.getTemplateid();
            return str2;
          }
        }
        Iterator publicIter = roleTemplateList.iterator();
        if (publicIter.hasNext()) {
          SystemplateVO tempVO = (SystemplateVO)publicIter.next();
          String str3 = tempVO.getTemplateid();
          return str3;
        }
      }
      if ((defaultTemplates != null) && (defaultTemplates.length > 0))
      {
       String defaultTempId = null;
        int priorityLevel = 0;
        boolean isSysTemplate = false;
        boolean isCorpDefalutTemplate = false;
        boolean isMatchNodekey = false;
        for (int i = defaultTemplates.length - 1; i >= 0; i--) {
          isCorpDefalutTemplate = isCorpDefaultTemplate(defaultTemplates[i], tptParaVo.getPk_Corp());

          isMatchNodekey = matchNodekey(tptParaVo, defaultTemplates[i]);

          if ((isCorpDefalutTemplate) && (isMatchNodekey)) {
            defaultTempId = defaultTemplates[i].getTemplateid();
            break;
          }

          isSysTemplate = isSysTemplate(defaultTemplates[i]);
          if ((isSysTemplate) && (isMatchNodekey)) {
            defaultTempId = defaultTemplates[i].getTemplateid();
            priorityLevel = 3;
          }

          if ((priorityLevel < 2) && (isCorpDefalutTemplate) && (defaultTemplates[i].getNodekey() == null))
          {
            defaultTempId = defaultTemplates[i].getTemplateid();
            priorityLevel = 2;
          }

          if ((priorityLevel >= 1) || (!isSysTemplate) || (defaultTemplates[i].getNodekey() != null))
            continue;
          defaultTempId = defaultTemplates[i].getTemplateid();
          priorityLevel = 1;
        }

       //i = defaultTempId;
        return defaultTempId;
      }
      String defaultTempId = null;
      return defaultTempId;
    }
    catch (DbException e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
  //  throw localObject;
  }

  private boolean isCorpDefaultTemplate(SystemplateVO systemplateVO, String pkorg)
  {
    return (systemplateVO.getTemplateflag().booleanValue()) && (systemplateVO.getPk_corp() != null) && (systemplateVO.getPk_corp().equals(pkorg));
  }

  private boolean isSysTemplate(SystemplateVO systemplateVO)
  {
    return (systemplateVO.getTemplateflag().booleanValue()) && ((systemplateVO.getPk_corp() == null) || (systemplateVO.getPk_corp().equals("@@@@")) || (systemplateVO.getPk_corp().equals("0001")));
  }

  private boolean matchNodekey(TemplateParaVO tptParaVo, SystemplateVO systemplateVO)
  {
    if ((tptParaVo.getNodeKey() == null) && (systemplateVO.getNodekey() == null))
    {
      return true;
    }

    return (systemplateVO.getNodekey() != null) && (systemplateVO.getNodekey().equals(tptParaVo.getNodeKey()));
  }

  public ArrayList queryTemplateVOs(TemplateParaVO tptParaVo)
    throws BusinessException
  {
    if (tptParaVo.getPk_orgUnit() == null) {
      throw new TemplateRuntimeException("错误：组织PK不可为空！");
    }

    if ((tptParaVo.getOperator() == null) || (tptParaVo.getOperator().trim().equals("")))
    {
      throw new TemplateRuntimeException("错误：用户PK不可为空！");
    }
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();

      String baseSql = "select * from pub_systemplate where funnode = ? and tempstyle = ? ";

      SQLParameter userParam = new SQLParameter();
      StringBuffer userSqlBuf = new StringBuffer();
      userSqlBuf.append(baseSql);

      userSqlBuf.append("and ( ");
      constructUserParams(tptParaVo, userParam, userSqlBuf, false);
      userSqlBuf.append("or (templateflag='Y' ");

      if ((tptParaVo.getOrgType() == null) || ("1".equals(tptParaVo.getOrgType())))
      {
        userSqlBuf.append("and (pk_corp='@@@@'  or pk_corp is null or pk_org='0001' or pk_org=?)");
      }
      else {
        userSqlBuf.append("and (pk_corp='@@@@' or pk_corp is null or pk_org='0001'  or (pk_corp=? and pk_org=?))");
      }
      userSqlBuf.append("))");
      setCorpOrgParam(tptParaVo, userParam);

      List userTemplateList = (List)jdbc.executeQuery(userSqlBuf.toString(), userParam, new BeanListProcessor(SystemplateVO.class));

      HashMap mapNodekey = new HashMap();
      for (Iterator iter = userTemplateList.iterator(); iter.hasNext(); ) {
        SystemplateVO tempVO = (SystemplateVO)iter.next();

        if (tptParaVo.getOperator().equals(tempVO.getOperator())) {
          String nodekey = tempVO.getNodekey();
          if (!mapNodekey.containsKey(nodekey)) {
            mapNodekey.put(nodekey, tempVO);
          }

        }

      }

      SystemplateVO[] defaultTemplates = (SystemplateVO[])(SystemplateVO[])userTemplateList.toArray(new SystemplateVO[userTemplateList.size()]);

      SQLParameter roleParam = new SQLParameter();
      StringBuffer roleSqlBuf = new StringBuffer();
      roleSqlBuf.append(baseSql);

      constructRoleParams(tptParaVo, roleParam, roleSqlBuf, false);

      List roleTemplateList = (List)jdbc.executeQuery(roleSqlBuf.toString(), roleParam, new BeanListProcessor(SystemplateVO.class));

      if ((roleTemplateList != null) && (!roleTemplateList.isEmpty()))
      {
        Iterator iter = roleTemplateList.iterator();
        while (iter.hasNext()) {
          SystemplateVO tempVO = (SystemplateVO)iter.next();
          if (tempVO.getPk_corp().equals(tptParaVo.getPk_Corp())) {
            String nodekey = tempVO.getNodekey();
            if (!mapNodekey.containsKey(nodekey)) {
              mapNodekey.put(nodekey, tempVO);
            }
          }
        }

      }
      SystemplateVO   foundTemplate = null;
      if ((defaultTemplates != null) && (defaultTemplates.length > 0))
      {
    	 
        int priorityLevel = 0;
        boolean isSysTemplate = false;
        boolean isCorpDefalutTemplate = false;
        boolean isMatchNodekey = false;
        for (int i = defaultTemplates.length - 1; i >= 0; i--) {
          String nodekey = defaultTemplates[i].getNodekey();
          if (!mapNodekey.containsKey(nodekey)) {
            tptParaVo.setNodeKey(nodekey);
            foundTemplate = findMatchTemplate(tptParaVo, defaultTemplates);

            mapNodekey.put(nodekey, foundTemplate);
          }

        }

      }

   //   SystemplateVO foundTemplate = new ArrayList(mapNodekey.values());
      return new ArrayList(mapNodekey.values());
    }
    catch (DbException e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
   // throw localObject;
  }

  private SystemplateVO findMatchTemplate(TemplateParaVO tptParaVo, SystemplateVO[] defaultTemplates)
  {
    SystemplateVO foundTemplatevo = null;
    if ((defaultTemplates != null) && (defaultTemplates.length > 0)) {
      int priorityLevel = 0;
      boolean isSysTemplate = false;
      boolean isCorpDefalutTemplate = false;
      boolean isMatchNodekey = false;
      for (int i = defaultTemplates.length - 1; i >= 0; i--) {
        isCorpDefalutTemplate = isCorpDefaultTemplate(defaultTemplates[i], tptParaVo.getPk_Corp());

        isMatchNodekey = matchNodekey(tptParaVo, defaultTemplates[i]);

        if ((isCorpDefalutTemplate) && (isMatchNodekey)) {
          foundTemplatevo = defaultTemplates[i];
          break;
        }

        isSysTemplate = isSysTemplate(defaultTemplates[i]);
        if ((isSysTemplate) && (isMatchNodekey)) {
          foundTemplatevo = defaultTemplates[i];
          priorityLevel = 3;
        }

        if ((priorityLevel < 2) && (isCorpDefalutTemplate) && (defaultTemplates[i].getNodekey() == null))
        {
          foundTemplatevo = defaultTemplates[i];
          priorityLevel = 2;
        }

        if ((priorityLevel >= 1) || (!isSysTemplate) || (defaultTemplates[i].getNodekey() != null))
          continue;
        foundTemplatevo = defaultTemplates[i];
        priorityLevel = 1;
      }
    }

    return foundTemplatevo;
  }

  private String getDefaultTemplate(TemplateParaVO tptParaVo)
  {
    String[] ids = queryDefaTemplateIds(tptParaVo);
    if (ids != null) {
      return ids[0];
    }
    return null;
  }

  public String[] queryDefaTemplateIds(TemplateParaVO tempVo)
  {
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession session = persist.getJdbcSession();
      StringBuffer sqlBuf = new StringBuffer();
      sqlBuf.append("Select templateid From pub_systemplate WHERE funnode = ? and tempstyle = ? and templateflag='Y' ");

      if (tempVo.getNodeKey() != null) {
        sqlBuf.append("and nodekey=? ");
      }
      if ((tempVo.getOrgType() == null) || ("1".equals(tempVo.getOrgType())))
      {
        sqlBuf.append("and (pk_corp='@@@@' or pk_org='0001' or pk_corp is null  or pk_org=?)");
      }
      else {
        sqlBuf.append("and (pk_corp='@@@@' or pk_corp is null or pk_org='0001'  or (pk_corp=? and pk_org=?))");
      }
      sqlBuf.append(" order by ts desc");
      SQLParameter param = new SQLParameter();
      param.addParam(tempVo.getFunNode());
      param.addParam(tempVo.getTemplateType());

      if (tempVo.getNodeKey() != null) {
        param.addParam(tempVo.getNodeKey());
      }

      setCorpOrgParam(tempVo, param);

      List result = (List)session.executeQuery(sqlBuf.toString(), param, new ColumnListProcessor("templateid"));

      if (result.size() == 0) {
        //arrayOfString = null;
        return null;
      }
      String[] arrayOfString = (String[])(String[])result.toArray(new String[0]);
      return arrayOfString;
    }
    catch (DbException e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
   // throw localObject;
  }

  private void setCorpOrgParam(TemplateParaVO tempVo, SQLParameter param)
  {
    if ((tempVo.getOrgType() == null) || ("1".equals(tempVo.getOrgType())))
    {
      param.addParam(tempVo.getPk_orgUnit());
    } else {
      IGLOrgBookAcc iOrgBook = (IGLOrgBookAcc)NCLocator.getInstance().lookup(IGLOrgBookAcc.class.getName());
      try
      {
        String pkCorp = iOrgBook.getPk_corp(tempVo.getPk_orgUnit());
        param.addParam(pkCorp);
        param.addParam(tempVo.getPk_orgUnit());
      } catch (BusinessException e) {
        Logger.error(e.getMessage());
        throw new TemplateRuntimeException("严重错误：根据组织PK查找公司PK出错！" + e.getMessage());
      }
    }
  }

  public String[] queryUserTemplateIds(TemplateParaVO tptParaVo)
  {
    if (tptParaVo.getPk_orgUnit() == null) {
      throw new TemplateRuntimeException("错误：组织PK不可为空！");
    }
    PersistenceManager persist = null;
    try {
      persist = PersistenceManager.getInstance();
      JdbcSession jdbc = persist.getJdbcSession();

      String baseSql = "select templateid from pub_systemplate where funnode = ? and tempstyle = ? ";

      SQLParameter userParam = new SQLParameter();
      StringBuffer userSqlBuf = new StringBuffer();
      userSqlBuf.append(baseSql);

      userSqlBuf.append("and ");
      constructUserParams(tptParaVo, userParam, userSqlBuf, false);

      List userTemplateList = (List)jdbc.executeQuery(userSqlBuf.toString(), userParam, new ColumnListProcessor(1));

      SQLParameter roleParam = new SQLParameter();

      StringBuffer roleSqlBuf = new StringBuffer();
      roleSqlBuf.append(baseSql);
      constructRoleParams(tptParaVo, roleParam, roleSqlBuf, false);

      List roleTemplateList = (List)jdbc.executeQuery(roleSqlBuf.toString(), roleParam, new ColumnListProcessor(1));

      HashSet hSet = new HashSet();
      hSet.addAll(userTemplateList);
      hSet.addAll(roleTemplateList);
      if (hSet.size() == 0) {
      //  arrayOfString = null;
        return null;
      }
      String[] arrayOfString = (String[])(String[])hSet.toArray(new String[hSet.size()]);
      return arrayOfString;
    }
    catch (DbException e)
    {
      Logger.error(e.getMessage(), e);
      throw new TemplateRuntimeException(e.getSQLState() + e.getMessage());
    } finally {
      if (persist != null)
        persist.release(); 
    }
    //throw localObject;
  }

  private void constructUserParams(TemplateParaVO tptParaVo, SQLParameter userParam, StringBuffer userSqlBuf, boolean isIncludeNodeKey)
  {
    String orgType = tptParaVo.getOrgType();
    if ((orgType == null) || ("1".equals(orgType)))
      userSqlBuf.append("(pk_org=? ");
    else {
      userSqlBuf.append("(pk_corp=? and pk_org=? ");
    }

    if (tptParaVo.getNodeKey() != null)
      userSqlBuf.append("and nodekey=? ");
    else if (isIncludeNodeKey) {
      userSqlBuf.append("and nodekey is null ");
    }

    userSqlBuf.append("and operator_type=1 and operator=? )");

    userParam.addParam(tptParaVo.getFunNode());
    userParam.addParam(tptParaVo.getTemplateType());

    if ((orgType == null) || ("1".equals(orgType)))
    {
      tptParaVo.setPk_Corp(tptParaVo.getPk_orgUnit());
      userParam.addParam(tptParaVo.getPk_orgUnit());
    } else {
      IGLOrgBookAcc iOrgBook = (IGLOrgBookAcc)NCLocator.getInstance().lookup(IGLOrgBookAcc.class.getName());
      try
      {
        String pkCorp = iOrgBook.getPk_corp(tptParaVo.getPk_orgUnit());
        tptParaVo.setPk_Corp(pkCorp);
        userParam.addParam(pkCorp);
        userParam.addParam(tptParaVo.getPk_orgUnit());
      } catch (BusinessException e) {
        Logger.error(e.getMessage());
        throw new TemplateRuntimeException("严重错误：根据组织PK查找公司PK出错！" + e.getMessage());
      }

    }

    if (tptParaVo.getNodeKey() != null)
      userParam.addParam(tptParaVo.getNodeKey());
    userParam.addParam(tptParaVo.getOperator());
  }

  private void constructRoleParams(TemplateParaVO tptParaVo, SQLParameter roleParam, StringBuffer roleSqlBuf, boolean isIncludeNodekey)
  {
    if (tptParaVo.getNodeKey() != null)
      roleSqlBuf.append("and nodekey=? ");
    else if (isIncludeNodekey) {
      roleSqlBuf.append("and nodekey is null ");
    }

    String orgType = tptParaVo.getOrgType();
    if ((orgType == null) || ("1".equals(orgType)))
      roleSqlBuf.append("and (iscomm = 'Y' or pk_org=?) ");
    else {
      roleSqlBuf.append("and (iscomm = 'Y' or (pk_corp=? and pk_org=?)) ");
    }

    roleSqlBuf.append("and operator_type = 2 and operator in (select pk_role from sm_user_role where cuserid=? and pk_corp=?) ");

    roleParam.addParam(tptParaVo.getFunNode());
    roleParam.addParam(tptParaVo.getTemplateType());

    if (tptParaVo.getNodeKey() != null) {
      roleParam.addParam(tptParaVo.getNodeKey());
    }

    if ((orgType == null) || ("1".equals(orgType))) {
      roleParam.addParam(tptParaVo.getPk_orgUnit());
      roleParam.addParam(tptParaVo.getOperator());
      roleParam.addParam(tptParaVo.getPk_orgUnit());
    }
    else
    {
      roleParam.addParam(tptParaVo.getPk_Corp());
      roleParam.addParam(tptParaVo.getPk_orgUnit());
      roleParam.addParam(tptParaVo.getOperator());
      roleParam.addParam(tptParaVo.getPk_Corp());
    }
  }
}