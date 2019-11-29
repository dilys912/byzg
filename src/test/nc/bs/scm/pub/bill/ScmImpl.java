package nc.bs.scm.pub.bill;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.ejb.EJBObject;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.para.SysInitDMO;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.pub.lock.LockTool;
import nc.itf.uap.bd.def.IDef;
import nc.itf.uap.busibean.ISysInitQry;
import nc.ui.dbcache.DBCacheFacade;
import nc.vo.bd.def.DefVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.ValueRangeHashtable;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.exp.ScmDefCheckException;
import nc.vo.scm.pub.IscmDefCheckVO;
import nc.vo.scm.pub.ScmDataSet;
import nc.vo.scm.pub.report.AvgSaleQueryVO;

public class ScmImpl
  implements IScm
{
  protected boolean checkLowerBillExist(String sThisBillPKField, String sThisBillCodeField, String[] sLowerTableName, String[] sLowerTableSourceBillPKFieldName, String[] sLowerTableTsFieldName, CircularlyAccessibleValueObject[] dmdvos)
    throws BusinessException
  {
    try
    {
      String[] sPKs = null;
      String[] sTSs = null;
      String[] sCodes = null;
      Vector vPK = new Vector();
      Vector vTS = new Vector();
      Vector vCode = new Vector();
      for (int i = 0; i < dmdvos.length; i++) {
        if ((dmdvos[i].getStatus() != 3) && (dmdvos[i].getStatus() != 1))
          continue;
        vPK.add(dmdvos[i].getAttributeValue(sThisBillPKField));
        vTS.add(null);
        vCode.add(dmdvos[i].getAttributeValue(sThisBillCodeField));
      }

      sPKs = new String[vPK.size()];
      sTSs = new String[vTS.size()];
      sCodes = new String[vCode.size()];
      vPK.copyInto(sPKs);
      vTS.copyInto(sTSs);
      vCode.copyInto(sCodes);

      StringBuffer sb = new StringBuffer();
      String sCode = new String();
      int ooo = 0;
      Hashtable htTotal = new Hashtable();
      Hashtable htOnceCheck = new Hashtable();
      Hashtable bht = new Hashtable();

      ScmDMO ddmo = new ScmDMO();
      for (int w = 0; w < sLowerTableName.length; w++) {
        htOnceCheck = ddmo.checkRowExitOrChanged(sPKs, sTSs, sLowerTableName[w], sLowerTableSourceBillPKFieldName[w], sLowerTableTsFieldName[w]);

        Object[] objKeys = new DMDataVO().getAllKeysFromHashtable(htOnceCheck);

        for (int i = 0; i < objKeys.length; i++) {
          if (!htTotal.containsKey(objKeys[i].toString())) {
            htTotal.put(objKeys[i].toString(), htOnceCheck.get(objKeys[i]));
          }
          else {
            Integer iCheck = new Integer(htTotal.get(objKeys[i].toString()).toString());

            if (iCheck.intValue() == 0) {
              htTotal.put(objKeys[i].toString(), htOnceCheck.get(objKeys[i]));
            }
          }
        }

        htOnceCheck = null;
      }

      for (int i = 0; i < sPKs.length; i++) {
        if (sPKs[i] == null)
          continue;
        if (null != htTotal.get(sPKs[i])) {
          Integer iCheck = new Integer(htTotal.get(sPKs[i]).toString());

          if (iCheck.intValue() != 0) {
            sCode = sCodes[i];
            if (!bht.containsValue(sCode)) {
              if (ooo != 0)
                sb.append("、" + sCode);
              else {
                sb.append(sCode);
              }
              ooo++;
              bht.put(sCode, sCode);
            }
          }
        }
      }

      if (sb.toString().trim().length() > 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000025") + sb.toString());
      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    return true;
  }

  protected boolean checkSourceTsForNewAdd(String sSourceBillCodeField, String sSourceBillField, String sSourceBillTsField, String[] sSourceTableName, String[] sSourceTablePKFieldName, String[] sSourceTableTsFieldName, CircularlyAccessibleValueObject[] dmdvos)
    throws BusinessException
  {
    try
    {
      String[] sPKs = null;
      String[] sTSs = null;
      String[] sCodes = null;
      Vector vPK = new Vector();
      Vector vTS = new Vector();
      Vector vCode = new Vector();

      for (int i = 0; i < dmdvos.length; i++) {
        if (dmdvos[i].getStatus() == 2) {
          Object obj = dmdvos[i].getAttributeValue(sSourceBillField);
          if ((obj == null) || (obj.toString().length() == 0)) {
            continue;
          }
          vPK.add(obj);

          obj = dmdvos[i].getAttributeValue(sSourceBillTsField);
          vTS.add(obj != null ? obj.toString() : null);

          vCode.add(dmdvos[i].getAttributeValue(sSourceBillCodeField));
        }

      }

      sPKs = new String[vPK.size()];
      sTSs = new String[vTS.size()];
      sCodes = new String[vCode.size()];
      vPK.copyInto(sPKs);
      vTS.copyInto(sTSs);
      vCode.copyInto(sCodes);

      StringBuffer sb = new StringBuffer();
      String sCode = new String();
      int ooo = 0;
      Hashtable htTotal = new Hashtable();
      Hashtable htOnceCheck = new Hashtable();
      Hashtable bht = new Hashtable();

      ScmDMO ddmo = new ScmDMO();
      for (int w = 0; w < sSourceTableName.length; w++) {
        htOnceCheck = ddmo.checkRowExitOrChanged(sPKs, sTSs, sSourceTableName[w], sSourceTablePKFieldName[w], sSourceTableTsFieldName[w]);

        Object[] objKeys = new DMDataVO().getAllKeysFromHashtable(htOnceCheck);

        for (int i = 0; i < objKeys.length; i++) {
          if (!htTotal.containsKey(objKeys[i].toString())) {
            htTotal.put(objKeys[i].toString(), htOnceCheck.get(objKeys[i]));
          }
          else {
            Integer iCheck = new Integer(htTotal.get(objKeys[i].toString()).toString());

            if (iCheck.intValue() == 0) {
              htTotal.put(objKeys[i].toString(), htOnceCheck.get(objKeys[i]));
            }
          }
        }

        htOnceCheck = null;
      }

      for (int i = 0; i < sPKs.length; i++) {
        if (sPKs[i] == null)
          continue;
        if (null != htTotal.get(sPKs[i])) {
          Integer iCheck = new Integer(htTotal.get(sPKs[i]).toString());

          if ((iCheck.intValue() != 2) && (iCheck.intValue() != 0))
            continue;
          sCode = sCodes[i];
          if (!bht.containsValue(sCode)) {
            if (ooo != 0)
              sb.append("、" + sCode);
            else {
              sb.append(sCode);
            }
            ooo++;
            bht.put(sCode, sCode);
          }
        }

      }

      if (sb.toString().trim().length() > 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000026") + sb.toString());
      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e);
    }
    return true;
  }

  protected DMVO converVO(AggregatedValueObject sourceVO, String[] sourceHeadKeys, String[] sourceItemKeys, String[] destHeadKeys, String[] destItemKeys)
  {
    return converVO(sourceVO, sourceHeadKeys, sourceItemKeys, destHeadKeys, destItemKeys, null, null);
  }

  protected DMVO converVO(AggregatedValueObject sourceVO, String[] sourceHeadKeys, String[] sourceItemKeys, String[] destHeadKeys, String[] destItemKeys, Integer HeadStatus, Integer BodyStatus)
  {
    if (null == HeadStatus)
      HeadStatus = new Integer(0);
    if (null == BodyStatus) {
      BodyStatus = new Integer(0);
    }

    CircularlyAccessibleValueObject headVO = sourceVO.getParentVO();

    CircularlyAccessibleValueObject[] itemVOs = sourceVO.getChildrenVO();

    DMVO dvo = new DMVO();
    DMDataVO dmHeadVO = new DMDataVO();
    DMDataVO[] dmItemVOs = new DMDataVO[0];
    if (itemVOs != null) {
      dmItemVOs = new DMDataVO[itemVOs.length];
    }

    if ((headVO != null) && (sourceHeadKeys != null) && (sourceHeadKeys.length != 0))
    {
      dmHeadVO.setStatus(HeadStatus.intValue());
      for (int i = 0; i < sourceHeadKeys.length; i++)
        dmHeadVO.setAttributeValue(destHeadKeys[i], headVO.getAttributeValue(sourceHeadKeys[i]));
    }
    else
    {
      dmHeadVO = null;
    }

    if ((itemVOs != null) && (itemVOs.length != 0) && (sourceItemKeys != null) && (sourceItemKeys.length != 0))
    {
      for (int i = 0; i < itemVOs.length; i++) {
        dmItemVOs[i] = new DMDataVO();
        dmItemVOs[i].setStatus(BodyStatus.intValue());
        for (int j = 0; j < sourceItemKeys.length; j++)
          dmItemVOs[i].setAttributeValue(destItemKeys[j], itemVOs[i].getAttributeValue(sourceItemKeys[j]));
      }
    }
    else {
      dmItemVOs = null;
    }
    dvo.setParentVO(dmHeadVO);
    dvo.setChildrenVO(dmItemVOs);
    return dvo;
  }

  protected long getCurrentTime()
  {
    return System.currentTimeMillis();
  }

  protected void lockPKs(String[] lockpks, String userid)
    throws BusinessException
  {
    try
    {
      if ((userid == null) || (userid.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000027"));
      }

      boolean isLock = false;

      if ((lockpks != null) && (lockpks.length > 0))
        isLock = LockTool.setLockForPks(lockpks, userid);
      else {
        isLock = true;
      }
      if (!isLock) {
        lockpks = null;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000316"));
      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Remote Call", e);
    }
  }

  protected DMDataVO[] query(StringBuffer sbSql)
    throws BusinessException
  {
    try
    {
      ScmDMO dmo = new ScmDMO();

      ArrayList al = dmo.queryExecute(sbSql);

      DMDataVO[] ddvos = new DMDataVO[al.size()];
      ddvos = (DMDataVO[])(DMDataVO[])al.toArray(ddvos);

      al = null;

      return ddvos;
    }
    catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
   
    throw new BusinessException(e);
    }
  }

  protected DMDataVO queryOneLine(StringBuffer sbSql)
    throws BusinessException
  {
    try
    {
      ScmDMO dmo = new ScmDMO();

      ArrayList al = dmo.queryExecute(sbSql);
      if (al.size() == 1) {
        DMDataVO ddvo = (DMDataVO)al.get(0);
        ddvo.convertToArrayList();
        return ddvo;
      }
      return null;
    }
    catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);

      throw new BusinessException(e);
    }
  }

  protected void showCurrentTime(String methodName)
  {
    long lTime = System.currentTimeMillis();
    System.out.println("========================执行<" + methodName + ">当前的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒===============");
  }

  protected void showMethodTime(String methodName, long begintime)
  {
    long lTime = System.currentTimeMillis() - begintime;
    System.out.println("========================执行<" + methodName + ">占用的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒===============");
  }

  protected void unLockPKs(String[] lockpks, String userid)
    throws BusinessException
  {
    try
    {
      if ((userid == null) || (userid.trim().length() == 0)) {
        return;
      }
      if ((lockpks != null) && (lockpks.length > 0))
        LockTool.releaseLockForPks(lockpks, userid);
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Remote Call", e);
    }
  }

  protected String[] getSYSParaString(String corp, String[] checkInvQtyParam)
    throws BusinessException
  {
    String[] Invparam = new String[checkInvQtyParam.length];
    try
    {
      ISysInitQry bo = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());

      for (int i = 0; i < checkInvQtyParam.length; i++)
      {
        Invparam[i] = bo.getParaString(corp, checkInvQtyParam[i]);

        if ((Invparam[i] == null) || (Invparam[i].trim().length() == 0)) {
          Invparam[i] = "Y";
        }
      }
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Cannot get NewParam!", e);
    }

    return Invparam;
  }

  public void removeRemoteBO(EJBObject remote)
    throws BusinessException
  {
    try
    {
      if (null != remote)
        remote.remove();
    }
    catch (Exception e)
    {
      throw new BusinessException(e);
    }
  }

  protected DMVO[] save(String sHeadTable, String sBodyTable, DMVO[] vos, ValueRangeHashtable htRangeHeader, ValueRangeHashtable htRangeBody, Object oNull, boolean bNeedTS)
    throws BusinessException, BusinessException
  {
    DMVO[] returnvos = new DMVO[vos.length];
    for (int number = 0; number < vos.length; number++) {
      returnvos[number] = save(sHeadTable, sBodyTable, vos[number], htRangeHeader, htRangeBody, bNeedTS);
    }

    return returnvos;
  }

  protected DMVO save(String sHeadTable, String sBodyTable, DMVO vo, ValueRangeHashtable htRangeHeader, ValueRangeHashtable htRangeBody, boolean bNeedTS)
    throws BusinessException
  {
    try
    {
      ScmDMO dmo = new ScmDMO();

      return dmo.save(sHeadTable, sBodyTable, vo, htRangeHeader, htRangeBody, bNeedTS);
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);

      throw new BusinessException(e);
    }
  }

  public Integer getLocalCurrDigit(String pk_corp)
    throws BusinessException
  {
    try
    {
      ScmDMO dmo = new ScmDMO();
      SysInitDMO dmoInit = new SysInitDMO();
      String currname = dmoInit.getParaString(pk_corp, "BD301");
      return dmo.getLocalCurrDigit(currname);
    } catch (Exception e) {

        throw new BusinessException(e);
    }
  }

  public AvgSaleQueryVO getAvgSaleData(AvgSaleQueryVO voQuery, String strCorpID)
    throws BusinessException
  {
    AvgSaleQueryVO queryData = new AvgSaleQueryVO();
    queryData.setIqueryday(voQuery.getIqueryday());
    queryData.setDqueryDate(voQuery.getDquerydate());
    queryData.setCinvbasdocid(voQuery.getCinvbasdocid());
    queryData.setCinvmandocid(voQuery.getCinvmandocid());
    queryData.setCinvcode(voQuery.getCinvcode());
    queryData.setCinvname(voQuery.getCinvname());
    queryData.setCinvspec(voQuery.getCinvspec());
    queryData.setCinvtype(voQuery.getCinvtype());
    queryData.setCunitid(voQuery.getCunitid());
    queryData.setCunitname(voQuery.getCunitname());
    String pk_corp = strCorpID;
    if (pk_corp == null)
      return null;
    if (queryData.getCinvbasdocid() == null)
      return null;
    try {
      ScmDMO dmo = new ScmDMO();
      queryData.setNinvoicenum(dmo.getInvInvoiceNumber(queryData.getCinvbasdocid(), queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));

      queryData.setNordernum(dmo.getInvOrderNumber(queryData.getCinvbasdocid(), queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));

      queryData.setNoutnum(dmo.getInvOutNumber(queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));
    }
    catch (Exception e)
    {
      throw new BusinessException(e);
    }
    return queryData;
  }

  public void checkDefDataType(IscmDefCheckVO voBill)
    throws ScmDefCheckException
  {
    int DEFNUM = 20;

    IDef defquery = (IDef)NCLocator.getInstance().lookup(IDef.class.getName());

    DefVO[] voHeadDefs = null;
    DefVO[] voBodyDefs = null;
    try
    {
      String headname = getDefObjName(ScmConst.getHeadObjCode(voBill.getCbilltypedef()));

      String bodyname = getDefObjName(ScmConst.getBodyObjCode(voBill.getCbilltypedef()));

      voHeadDefs = defquery.queryDefVO(headname, null);

      voBodyDefs = defquery.queryDefVO(bodyname, null);
    }
    catch (Exception e) {
      throw new ScmDefCheckException(e.getMessage());
    }

    Object value = null;

    ArrayList alHeadErrCol = new ArrayList();
    ArrayList alBodyErr = new ArrayList();

    for (int i = 1; i <= DEFNUM; i++) {
      value = voBill.getHeadDefValue(i);

      if ((value == null) || (value.toString().trim().length() == 0) || (voHeadDefs == null))
      {
        continue;
      }
      if ((i <= voHeadDefs.length) && (voHeadDefs[(i - 1)] != null) && (isCorrectDefType(value, voHeadDefs[(i - 1)])))
        continue;
      alHeadErrCol.add(new Integer(i));
    }

    Object[] values = null;
    for (int i = 1; i <= DEFNUM; i++) {
      values = voBill.getBodyDefValues(i);

      if ((values == null) || (values.length == 0)) {
        continue;
      }
      for (int row = 0; row < values.length; row++) {
        if ((values[row] == null) || (values[row].toString().trim().length() == 0) || (voBodyDefs == null))
        {
          continue;
        }

        if ((i > voBodyDefs.length) || (voBodyDefs[(i - 1)] == null))
        {
          alBodyErr.add(new Point(-1, i));
        }
        else if (!isCorrectDefType(values[row], voBodyDefs[(i - 1)])) {
          alBodyErr.add(new Point(row + 1, i));
        }

      }

    }

    StringBuffer msg = new StringBuffer();
    if (alHeadErrCol.size() > 0)
    {
      msg.append("表头自定义项值错误，请检查:");

      for (int i = 0; i < alHeadErrCol.size(); i++) {
        if (i > 0)
          msg.append(",");
        msg.append(alHeadErrCol.get(i));
      }

    }

    if (alBodyErr.size() > 0)
    {
      msg.append("\n表体自定义项值错误，请检查:");

      for (int i = 0; i < alBodyErr.size(); i++) {
        Point p = (Point)alBodyErr.get(i);
        if (i > 0)
          msg.append(",");
        msg.append("(" + p.x + "," + p.y + ")");
      }

    }

//    if (msg.length() > 0) {
//      ScmDefCheckException e = new ScmDefCheckException(msg.toString());
//      if (alHeadErrCol.size() > 0) {
//        Integer[] err = new Integer[alHeadErrCol.size()];
//        alHeadErrCol.toArray(err);
//        e.setHeadErrCols(err);
//      }
//
//      if (alBodyErr.size() > 0) {
//        Point[] points = new Point[alBodyErr.size()];
//        alBodyErr.toArray(points);
//        e.setBodyErrPos(points);
//      }
////      throw e;
//    }
  }

  private boolean isCorrectDefType(Object value, DefVO voDef)
  {
    if (value == null)
    {
      return true;
    }
    if ((voDef == null) || (voDef.getType() == null)) {
      return false;
    }
    if (voDef.getType().equals("数字")) {
      try {
        if ((!(value instanceof UFDouble)) && (!(value instanceof Integer))) {
          new UFDouble(value.toString());
        }

      }
      catch (Exception e)
      {
        return false;
      }

    }
    else if (voDef.getType().equals("日期")) {
      try
      {
        if (!(value instanceof UFDate)) {
//         	new UFDate(value.toString()); 
        	
          	//edit by zwx 2015-5-27 value不为空判断
         	if(!(value.equals("null"))){ 
                 new UFDate(value.toString());
        	}
        }

      }
      catch (Exception e)
      {
        return false;
      }

    }

    return true;
  }

  private String getDefObjName(String objcode)
  {
    ScmDataSet data = new ScmDataSet();
    Object ohead = DBCacheFacade.runQuery(" select objname from bd_defused where  ( objcode= '" + objcode + "')", data);

    data = (ScmDataSet)ohead;
    String objname = null;
    if ((data != null) && (data.getRowCount() > 0)) {
      objname = (String)data.getValueAt(0, 0);
    }
    return objname;
  }
}