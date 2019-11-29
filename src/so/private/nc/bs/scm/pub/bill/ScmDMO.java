package nc.bs.scm.pub.bill;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.ejb.EJBObject;
import javax.naming.NamingException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.billcodemanage.BillcodeRuleBO;
import nc.bs.pub.pf.IQueryData;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.ValueRange;
import nc.vo.dm.pub.ValueRangeHashtable;
import nc.vo.dm.pub.ValueRangeHashtableForTempVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.sql.DeleteSQL;
import nc.vo.scm.pub.sql.ExpressItem;
import nc.vo.scm.pub.sql.FieldItem;
import nc.vo.scm.pub.sql.FromCollection;
import nc.vo.scm.pub.sql.InsertSQL;
import nc.vo.scm.pub.sql.SelectCollection;
import nc.vo.scm.pub.sql.SelectItem;
import nc.vo.scm.pub.sql.SetCollection;
import nc.vo.scm.pub.sql.UpdateSQL;
import nc.vo.scm.pub.sql.WhereCollection;

public class ScmDMO extends DataManageObject
  implements IQueryData
{
  protected boolean bIsNeedDataStore = false;
  protected Connection con = null;
  protected String sConnectionID = null;

  public ScmDMO()
    throws NamingException, nc.bs.pub.SystemException
  {
  }

  public ScmDMO(String dbName)
    throws NamingException, nc.bs.pub.SystemException
  {
    super(dbName);
  }

  public final Hashtable checkRowExitOrChanged(String[] sPK, String[] sTs, String stablename, String sPkFieldName, String sTsFieldName)
    throws BusinessException
  {
    Hashtable ht = new Hashtable();

    if ((sPK == null) || (sPK.length == 0) || (sTs == null) || (sPK.length != sTs.length) || (stablename == null) || (stablename.trim().length() == 0) || (sPkFieldName == null) || (sPkFieldName.trim().length() == 0) || (sTsFieldName == null) || (sTsFieldName.trim().length() == 0))
    {
      return ht;
    }

    Vector vPK = new Vector();
    Vector vTs = new Vector();
    for (int i = 0; i < sPK.length; i++) {
      if ((null != sPK[i]) && (sPK[i].trim().length() != 0)) {
        vPK.add(sPK[i]);
        vTs.add(sTs[i]);
      }
    }
    if (vPK.size() == 0) {
      return ht;
    }

    String[] sPKNew = new String[vPK.size()];
    String[] sTsNew = new String[vPK.size()];
    vPK.copyInto(sPKNew);
    vTs.copyInto(sTsNew);

    StringBuffer sb = new StringBuffer();
    ArrayList al = new ArrayList();

    sb.append(" select " + sPkFieldName + "," + sTsFieldName + " from " + stablename).append(" where dr=0 ").append(SQLUtil.formInSQL(sPkFieldName, sPKNew));
    try
    {
      getConnection("checkRowExitOrChanged");

      al = queryExecute(sb);
    } catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        removeConnection("checkRowExitOrChanged");
      }
      catch (Exception e) {
      }
    }
    for (int j = 0; j < sPKNew.length; j++) {
      boolean bFound = false;
      for (int i = 0; i < al.size(); i++) {
        if (sPKNew[j].equals(((DMDataVO)al.get(i)).getAttributeValue(sPkFieldName))) {
          if ((sTsNew[j] != null) && (sTsNew[j].equals(((DMDataVO)al.get(i)).getAttributeValue(sTsFieldName))))
          {
            ht.put(sPKNew[j], new Integer(1));
          }
          else ht.put(sPKNew[j], new Integer(2));

          bFound = true;
          break;
        }
      }
      if (!bFound) {
        ht.put(sPKNew[j], new Integer(0));
      }
    }

    return ht;
  }

  public void dataStoreSave(String sHeadTable, String sBodyTable, AggregatedValueObject importvo, String sHeadPrimKeyField, String sBodyPrimKeyField, String sHeadPKInBodyField, String sGenOIDKeyField)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    DMVO vo = new DMVO();
    vo.translateFromOtherVO(importvo);
    ValueRangeHashtable htRangeHeader = new ValueRangeHashtableForTempVO();
    ValueRangeHashtable htRangeBody = new ValueRangeHashtableForTempVO();

    DMDataVO[] dmbodyvos = vo.getBodyVOs();
    DMDataVO dmheadvo = vo.getHeaderVO();
    StringBuffer sExecuteSQL = new StringBuffer();
    Object sHeaderPK = null;
    Object sGenOIDPK = null;
    String sHeaderPKField = sHeadPrimKeyField;
    String sGenOIDPKField = sGenOIDKeyField;
    DMVO dmvoForOldVO = new DMVO();
    ValueRange valueRange = new ValueRange();
    try
    {
      getConnection("dataStoreSave");

      if ((null != sHeadTable) && (sHeadTable.trim().length() > 0) && (null != sBodyTable) && (sBodyTable.trim().length() > 0)) {
        if (this.bIsNeedDataStore)
        {
          sGenOIDPK = dmheadvo.getAttributeValue(sGenOIDPKField);

          sHeaderPK = dmheadvo.getAttributeValue(sHeaderPKField);
          if (null == sHeaderPK) {
            for (int i = 0; i < dmbodyvos.length; i++) {
              if (null != dmbodyvos[i]) {
                sHeaderPK = dmbodyvos[i].getAttributeValue(sHeadPKInBodyField);
                if (null != sHeaderPK) {
                  break;
                }
              }
            }
          }
        }
        if (dmheadvo.getStatus() == 1) {
          if (this.bIsNeedDataStore) {
            if (null == sHeaderPK) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000028"));
            }

            sExecuteSQL = new StringBuffer();

            sExecuteSQL.append(" select * from ").append(sHeadTable).append(" where dr=0 and ").append(sHeaderPKField).append("='").append(sHeaderPK).append("'");
            DMDataVO[] dmdvoForOldHeaders = query(sExecuteSQL);
            if ((dmdvoForOldHeaders == null) || (dmdvoForOldHeaders.length == 0) || (dmdvoForOldHeaders[0] == null)) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
            }
            DMDataVO dmdvoForOldHeader = dmdvoForOldHeaders[0];

            sExecuteSQL = new StringBuffer();
            sExecuteSQL.append(" select * from ").append(sBodyTable).append(" where dr=0 and ").append(sHeadPKInBodyField).append("='").append(sHeaderPK).append("'");
            DMDataVO[] dmdvoForOldBodys = query(sExecuteSQL);

            htRangeHeader.setGenOIDKeyField(sGenOIDPKField);
            dmdvoForOldHeader.genValueRangeHashtableFromVO(importvo.getParentVO(), htRangeHeader);
            valueRange = new ValueRange();
            valueRange.setKey(sHeaderPKField);
            valueRange.setDataType(0);
            valueRange.setKeepInDB(true);
            valueRange.setPrimaryKey(true);
            htRangeHeader.putValueRange(valueRange);
            ((ValueRange)htRangeHeader.get(sGenOIDPKField)).setKeepInDB(false);
            ((ValueRange)htRangeHeader.get("ts")).setKeepInDB(false);

            htRangeBody.setGenOIDKeyField(sGenOIDPKField);
            if ((null != importvo.getChildrenVO()) && (importvo.getChildrenVO().length != 0))
            {
              dmdvoForOldHeader.genValueRangeHashtableFromVO(importvo.getChildrenVO()[0], htRangeBody);
            }
            else
            {
              for (int i = 0; i < dmdvoForOldBodys.length; i++) {
                dmdvoForOldHeader.genValueRangeHashtableFromVO(dmdvoForOldBodys[i], htRangeBody);
              }
            }
            valueRange = new ValueRange();
            valueRange.setKey(sBodyPrimKeyField);
            valueRange.setDataType(0);
            valueRange.setKeepInDB(true);
            valueRange.setPrimaryKey(true);
            htRangeBody.putValueRange(valueRange);
            if (null == htRangeBody.get(sGenOIDPKField)) {
              valueRange = new ValueRange();
              valueRange.setKey(sGenOIDPKField);
              valueRange.setDataType(0);
              valueRange.setKeepInDB(false);
              valueRange.setPrimaryKey(false);
              htRangeBody.putValueRange(valueRange);
            }
            ((ValueRange)htRangeBody.get(sGenOIDPKField)).setKeepInDB(false);
            ((ValueRange)htRangeBody.get("ts")).setKeepInDB(false);

            dmvoForOldVO.setParentVO(dmdvoForOldHeader);
            dmvoForOldVO.setChildrenVO(dmdvoForOldBodys);
            dmvoForOldVO.setStatusTo(2);
            dmvoForOldVO.setDrTo(1);

            if ((null != dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField).toString().length() != 0)) {
              sGenOIDPK = dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField);
            }
            dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

            normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, false);
          }

          importvo.getParentVO().setAttributeValue("dr", new Integer(0));

          if ((null != importvo.getChildrenVO()) && (importvo.getChildrenVO().length != 0)) {
            for (int i = 0; i < importvo.getChildrenVO().length; i++) {
              if (importvo.getChildrenVO()[i].getStatus() == 1) {
                importvo.getChildrenVO()[i].setAttributeValue("dr", new Integer(0));
              }
            }
          }

        }
        else if (dmheadvo.getStatus() == 3) {
          if (this.bIsNeedDataStore)
          {
            vo.setStatusTo(1);
            vo.setDrTo(1);
          }

          importvo.getParentVO().setStatus(1);
          importvo.getParentVO().setAttributeValue("dr", new Integer(1));

          for (int i = 0; i < importvo.getChildrenVO().length; i++) {
            importvo.getChildrenVO()[i].setStatus(1);
            importvo.getChildrenVO()[i].setAttributeValue("dr", new Integer(1));
          }

        }
        else if (dmheadvo.getStatus() != 2);
      }
      else if (null != sHeadTable) {
        if (this.bIsNeedDataStore)
        {
          sGenOIDPK = dmheadvo.getAttributeValue(sGenOIDPKField);

          sHeaderPK = dmheadvo.getAttributeValue(sHeaderPKField);
        }
        if (dmheadvo.getStatus() == 1) {
          if (this.bIsNeedDataStore)
          {
            sExecuteSQL = new StringBuffer();

            sExecuteSQL.append(" select * from ").append(sHeadTable).append(" where dr=0 and ").append(sHeaderPKField).append("='").append(sHeaderPK).append("'");
            DMDataVO[] dmdvoForOldHeaders = query(sExecuteSQL);
            if ((dmdvoForOldHeaders == null) || (dmdvoForOldHeaders.length == 0) || (dmdvoForOldHeaders[0] == null)) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
            }
            DMDataVO dmdvoForOldHeader = dmdvoForOldHeaders[0];

            htRangeHeader.setGenOIDKeyField(sGenOIDPKField);
            dmdvoForOldHeader.genValueRangeHashtableFromVO(importvo.getParentVO(), htRangeHeader);
            valueRange = new ValueRange();
            valueRange.setKey(sHeaderPKField);
            valueRange.setDataType(0);
            valueRange.setKeepInDB(true);
            valueRange.setPrimaryKey(true);
            htRangeHeader.putValueRange(valueRange);
            ((ValueRange)htRangeHeader.get(sGenOIDPKField)).setKeepInDB(false);
            ((ValueRange)htRangeHeader.get("ts")).setKeepInDB(false);

            dmvoForOldVO.setParentVO(dmdvoForOldHeader);
            dmvoForOldVO.setChildrenVO(null);
            dmvoForOldVO.setStatusTo(2);
            dmvoForOldVO.setDrTo(1);

            if ((null != dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField).toString().length() != 0)) {
              sGenOIDPK = dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField);
            }
            dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

            normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, false);
          }

          importvo.getParentVO().setAttributeValue("dr", new Integer(0));
        }
        else if (dmheadvo.getStatus() == 3) {
          if (this.bIsNeedDataStore)
          {
            vo.setStatusTo(1);
            vo.setDrTo(1);
          }

          importvo.getParentVO().setStatus(1);
          importvo.getParentVO().setAttributeValue("dr", new Integer(1));
        }
        else if (dmheadvo.getStatus() != 2);
      }
      else
      {
        String sTotalBodyKey = null;

        sHeaderPKField = sBodyPrimKeyField;
        for (int i = 0; i < dmbodyvos.length; i++) {
          sHeaderPK = dmbodyvos[i].getAttributeValue(sHeaderPKField);
          if (dmbodyvos[i].getStatus() == 1) {
            if (this.bIsNeedDataStore) {
              sGenOIDPK = dmbodyvos[i].getAttributeValue(sGenOIDPKField);

              if (null != sTotalBodyKey) {
                sTotalBodyKey = sTotalBodyKey + ",'" + sHeaderPK + "'";
              }
              else {
                sTotalBodyKey = "'" + sHeaderPK + "'";
              }
            }

            dmbodyvos[i].setAttributeValue("dr", new Integer(0));
          }
          else if (dmbodyvos[i].getStatus() == 3) {
            if (this.bIsNeedDataStore)
            {
              dmbodyvos[i].setStatus(1);
              dmbodyvos[i].setDrTo(1);
            }

            importvo.getChildrenVO()[i].setStatus(1);
            importvo.getChildrenVO()[i].setAttributeValue("dr", new Integer(1));
          } else {
            if (dmbodyvos[i].getStatus() != 2)
            {
              continue;
            }
          }
        }

        if ((this.bIsNeedDataStore) && 
          (null != sTotalBodyKey)) {
          sExecuteSQL = new StringBuffer();
          sExecuteSQL.append(" select * from ").append(sBodyTable).append(" where dr=0 and ").append(sHeaderPKField).append(" in (").append(sTotalBodyKey).append(")");
          DMDataVO[] dmdvoForOldBodys = query(sExecuteSQL);
          if ((dmdvoForOldBodys == null) || (dmdvoForOldBodys.length == 0) || (dmdvoForOldBodys[0] == null)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
          }

          htRangeBody.setGenOIDKeyField(sGenOIDPKField);
          new DMDataVO().genValueRangeHashtableFromVO(importvo.getChildrenVO()[0], htRangeBody);
          valueRange = new ValueRange();
          valueRange.setKey(sBodyPrimKeyField);
          valueRange.setDataType(0);
          valueRange.setKeepInDB(true);
          valueRange.setPrimaryKey(true);
          htRangeBody.putValueRange(valueRange);
          ((ValueRange)htRangeBody.get(sGenOIDPKField)).setKeepInDB(false);
          ((ValueRange)htRangeBody.get("ts")).setKeepInDB(false);

          dmvoForOldVO.setParentVO(null);
          dmvoForOldVO.setChildrenVO(dmdvoForOldBodys);
          dmvoForOldVO.setStatusTo(2);
          dmvoForOldVO.setDrTo(1);

          if ((null != dmvoForOldVO.getBodyVOs()) && (dmvoForOldVO.getBodyVOs().length >= 0) && (null != dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField).toString().length() != 0)) {
            sGenOIDPK = dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField);
          }
          dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

          normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, false);
        }

      }

    }
    finally
    {
      try
      {
        removeConnection("dataStoreSave");
      }
      catch (Exception e)
      {
      }
    }
  }

  protected Connection getConnection(String sConnectionID)
    throws BusinessException
  {
    try
    {
      if (null == this.con) {
        this.con = getConnection();
        this.sConnectionID = sConnectionID;
      }
      return this.con; } catch (Exception e) {
    
    throw new BusinessException(e.getMessage());}
  }

  protected long getCurrentTime()
  {
    return System.currentTimeMillis();
  }

  public String getDeleteSQL(String sTable, DMDataVO[] vos, ValueRangeHashtable htRange)
  {
    if (null == htRange)
      return "";
    if (htRange.getPrimaryKeyField() == null)
      return "";
    if ((vos == null) || (vos.length == 0)) {
      return "";
    }

    DeleteSQL dSQL = new DeleteSQL();
    WhereCollection whereCollection = new WhereCollection();
    FromCollection fromCollection = new FromCollection();
    fromCollection.setFromItems(sTable);
    dSQL.setFromCollection(fromCollection);

    StringBuffer sb = new StringBuffer();
    String pk = htRange.getPrimaryKeyField();

    int ooo = 0;
    for (int k = 0; k < vos.length; k++) {
      DMDataVO vo = vos[k];
      if (vo.getStatus() == 3) {
        ooo++;
        if (ooo == 1)
          sb.append(pk + " in ('" + vo.getAttributeValue(pk) + "'");
        else
          sb.append(",'" + vo.getAttributeValue(pk) + "'");
      }
    }
    sb.append(") ");
    if (ooo != 0) {
      whereCollection.setWhereItems(sb.toString());
      dSQL.setWhereCollection(whereCollection);
      return dSQL.toString();
    }
    return "";
  }

  public String getDRDeleteSQL(String sTable, DMDataVO[] vos, ValueRangeHashtable htRange)
  {
    if (null == htRange)
      return "";
    if (htRange.getPrimaryKeyField() == null)
      return "";
    if ((vos == null) || (vos.length == 0)) {
      return "";
    }

    StringBuffer sb = new StringBuffer();
    sb.append("UPDATE " + sTable + " SET dr = 1 WHERE dr = 0 ");
    String pk = htRange.getPrimaryKeyField();

    ArrayList alPKNew = new ArrayList();
    for (int k = 0; k < vos.length; k++) {
      DMDataVO vo = vos[k];
      if ((vo.getStatus() == 3) && (vo.getAttributeValue(pk) != null) && (vo.getAttributeValue(pk).toString().trim().length() != 0)) {
        alPKNew.add(vo.getAttributeValue(pk));
      }

    }

    String[] sPKNew = (String[])(String[])alPKNew.toArray(new String[0]);
    if (sPKNew.length != 0) {
      sb.append(SQLUtil.formInSQL(pk, sPKNew));

      return sb.toString();
    }
    return "";
  }

  public String getDeleteSQL(String sTable, DMDataVO vo, ValueRangeHashtable htRange)
  {
    if (null == htRange)
      return "";
    if (htRange.getPrimaryKeyField() == null) {
      return "";
    }
    DeleteSQL dSQL = new DeleteSQL();
    WhereCollection whereCollection = new WhereCollection();
    FromCollection fromCollection = new FromCollection();
    fromCollection.setFromItems(sTable);
    dSQL.setFromCollection(fromCollection);

    StringBuffer sb = new StringBuffer();
    String pk = htRange.getPrimaryKeyField();

    int ooo = 0;
    if (vo.getStatus() == 3) {
      ooo++;
      sb.append(pk + " = '" + vo.getAttributeValue(pk) + "'");
    }

    if (ooo != 0) {
      whereCollection.setWhereItems(sb.toString());
      dSQL.setWhereCollection(whereCollection);
      return dSQL.toString();
    }
    return "";
  }

  public String getInsertSQL(String sTable, DMDataVO vo, ValueRangeHashtable htRange)
    throws nc.bs.pub.SystemException
  {
    if (null == htRange) {
      return "";
    }
    String[] sFieldNames = vo.getAttributeNames();
    InsertSQL iSQL = new InsertSQL();
    iSQL.setName(sTable);

    SelectCollection selectCollection = new SelectCollection();
    ArrayList alselectItems = new ArrayList();
    SelectItem selectItem = null;
    FieldItem fieldItem = null;
    String pk = htRange.getPrimaryKeyField();
    for (int i = 0; i < sFieldNames.length; i++) {
      if (!sFieldNames[i].equals("ts")) {
        ValueRange valueRange = (ValueRange)htRange.get(sFieldNames[i]);
        if (sFieldNames[i].equals("dr")) {
          valueRange = new ValueRange();
          valueRange.setKeepInDB(true);
          valueRange.setKey("dr");
          valueRange.setDataType(1);
          valueRange.setIsProgressive(false);
        }
        if ((valueRange != null) && (valueRange.isKeepInDB()) && (!sFieldNames[i].equals(pk))) {
          selectItem = new SelectItem();
          fieldItem = new FieldItem();
          fieldItem.setName(sFieldNames[i]);
          Object value = vo.getAttributeValue(sFieldNames[i]);
          if ((value == null) || (value.toString().trim().length() == 0))
            fieldItem.setValue(null);
          else {
            fieldItem.setValue(value);
          }
          fieldItem.setType(valueRange.getDataType());
          selectItem.setItem(fieldItem);
          alselectItems.add(selectItem);
        }
      }
    }
    if (alselectItems.size() != 0) {
      selectItem = new SelectItem();
      fieldItem = new FieldItem();
      fieldItem.setName(pk);
      String OID = "";

      OID = getOID(vo.getAttributeValue(htRange.getGenOIDKeyField()).toString());
      fieldItem.setValue(OID);
      vo.setAttributeValue(htRange.getPrimaryKeyField(), OID);

      fieldItem.setType(0);
      selectItem.setItem(fieldItem);
      alselectItems.add(selectItem);

      selectCollection.setSelectItems(alselectItems);
      iSQL.setCollection(selectCollection);

      return iSQL.toString();
    }
    return " ";
  }

  public String[] getLockPKsArray(AggregatedValueObject vo, String sHeadPKFieldName, String sBodyPKFieldName, String sHeadPKFieldsNameInBody, String[] sHeadLockFieldsNames, String[] sBodyLockFieldsNames)
  {
    if (null == vo)
      return new String[0];
    Hashtable htPKs = new Hashtable();
    Object objPK = null;

    if ((null != sHeadPKFieldName) && (sHeadPKFieldName.trim().length() != 0) && (null != vo.getParentVO()))
    {
      if (vo.getParentVO().getStatus() != 2)
      {
        objPK = vo.getParentVO().getAttributeValue(sHeadPKFieldName);
        if ((null != objPK) && (objPK.toString().length() != 0)) {
          htPKs.put(objPK, objPK);
        }
      }
      if ((null != sHeadLockFieldsNames) && (sHeadLockFieldsNames.length != 0))
      {
        for (int i = 0; i < sHeadLockFieldsNames.length; i++) {
          objPK = vo.getParentVO().getAttributeValue(sHeadLockFieldsNames[i]);
          if ((null != objPK) && (objPK.toString().length() != 0)) {
            htPKs.put(objPK, objPK);
          }
        }
      }

    }

    if ((null != sBodyPKFieldName) && (sBodyPKFieldName.trim().length() != 0) && (null != vo.getChildrenVO()) && (vo.getChildrenVO().length != 0))
    {
      for (int row = 0; row < vo.getChildrenVO().length; row++) {
        if (null != vo.getChildrenVO()[row]) {
          if (vo.getChildrenVO()[row].getStatus() != 2)
          {
            objPK = vo.getChildrenVO()[row].getAttributeValue(sBodyPKFieldName);
            if ((null != objPK) && (objPK.toString().length() != 0)) {
              htPKs.put(objPK, objPK);
            }
          }
          if ((null != sHeadPKFieldsNameInBody) && (sHeadPKFieldsNameInBody.trim().length() != 0))
          {
            objPK = vo.getChildrenVO()[row].getAttributeValue(sHeadPKFieldsNameInBody);
            if ((null != objPK) && (objPK.toString().length() != 0)) {
              htPKs.put(objPK, objPK);
            }
          }
          if ((null == sBodyLockFieldsNames) || (sBodyLockFieldsNames.length == 0))
            continue;
          for (int i = 0; i < sBodyLockFieldsNames.length; i++) {
            objPK = vo.getChildrenVO()[row].getAttributeValue(sBodyLockFieldsNames[i]);
            if ((null != objPK) && (objPK.toString().length() != 0)) {
              htPKs.put(objPK, objPK);
            }
          }
        }

      }

    }

    Object[] objtemp = new DMDataVO().getAllKeysFromHashtable(htPKs);
    String[] sReturn = new String[objtemp.length];
    for (int i = 0; i < sReturn.length; i++) {
      sReturn[i] = objtemp[i].toString();
    }
    return sReturn;
  }

  public ArrayList getLockPKsArrayList(AggregatedValueObject vo, String sHeadPKFieldName, String sBodyPKFieldName, String sHeadPKFieldsNameInBody, String[] sHeadLockFieldsNames, String[] sBodyLockFieldsNames)
  {
    ArrayList alReturn = new ArrayList();
    String[] sReturn = getLockPKsArray(vo, sHeadPKFieldName, sBodyPKFieldName, sHeadPKFieldsNameInBody, sHeadLockFieldsNames, sBodyLockFieldsNames);

    for (int i = 0; i < sReturn.length; i++) {
      alReturn.add(sReturn[i]);
    }
    return alReturn;
  }

  public ArrayList getQueryResult(ResultSet rs)
    throws SQLException
  {
    ArrayList alResultData = new ArrayList();
    int ooo = 0;
    DMDataVO pivo = new DMDataVO();

    ResultSetMetaData meta = null;
    try {
      while (rs.next()) {
        ooo++;
        pivo = new DMDataVO();
        if (ooo == 1) {
          meta = rs.getMetaData();
        }
        setData(rs, pivo, meta);
        if (null == pivo) {
          System.out.println("无数据，应检查数据库...");
          return alResultData;
        }
        alResultData.add(pivo);
      }
      return alResultData; } catch (SQLException e) {
        throw e;
      }
  }

  public String getUpdateSQL(String sTable, DMDataVO vo, ValueRangeHashtable htRange)
  {
    if (null == htRange)
      return "";
    if (htRange.getPrimaryKeyField() == null) {
      return "";
    }
    String[] sFieldNames = vo.getAttributeNames();
    UpdateSQL uSQL = new UpdateSQL();
    uSQL.setTableName(sTable);
    WhereCollection whereCollection = new WhereCollection();

    SetCollection setCollection = new SetCollection();
    ArrayList alExpressItem = new ArrayList();
    ExpressItem expressItem = null;
    FieldItem fieldItem = null;
    String pk = htRange.getPrimaryKeyField();

    whereCollection.setWhereItems(pk + " = '" + vo.getAttributeValue(pk) + "'");
    uSQL.setWhereCollection(whereCollection);

    for (int i = 0; i < sFieldNames.length; i++) {
      if ((!sFieldNames[i].equals("ts")) && (!sFieldNames[i].equals(pk))) {
        ValueRange valueRange = (ValueRange)htRange.get(sFieldNames[i]);
        if (sFieldNames[i].equals("dr")) {
          valueRange = new ValueRange();
          valueRange.setKey("dr");
          valueRange.setKeepInDB(true);
          valueRange.setDataType(1);
          valueRange.setIsProgressive(false);
        }
        if ((valueRange != null) && (valueRange.isKeepInDB())) {
          expressItem = new ExpressItem();
          expressItem.setExpressType(0);
          expressItem.setLeft(sFieldNames[i]);
          if ((valueRange.getDataType() == 0) || (valueRange.getDataType() == 3))
          {
            Object oValue = vo.getAttributeValue(sFieldNames[i]);
            if ((oValue instanceof String)) {
              String value = (String)oValue;
              if ((value == null) || (value.length() == 0)) {
                expressItem.setRight("null");
              } else {
                value = value.toString().trim();
                expressItem.setRight("'" + value + "'");
              }
            }
            else {
              String value = null;
              if (oValue != null)
                value = oValue.toString();
              if (value == null) {
                expressItem.setRight("null");
              } else {
                value = value.toString().trim();
                expressItem.setRight("'" + value + "'");
              }
            }
          }
          else if (valueRange.isProgressive()) {
            if ((vo.getAttributeValue(sFieldNames[i]) == null) || (vo.getAttributeValue(sFieldNames[i]).toString().trim().length() == 0))
            {
              expressItem.setRight(sFieldNames[i]);
            }
            else expressItem.setRight(" isnull(" + sFieldNames[i] + ",0.0)+ " + vo.getAttributeValue(sFieldNames[i]).toString());

          }
          else if ((vo.getAttributeValue(sFieldNames[i]) == null) || (vo.getAttributeValue(sFieldNames[i]).toString().trim().length() == 0))
          {
            expressItem.setRight("null");
          } else {
            expressItem.setRight(vo.getAttributeValue(sFieldNames[i]).toString());
          }

          alExpressItem.add(expressItem);
        }
      }
    }
    setCollection.setExpressItems(alExpressItem);
    uSQL.setSetCollection(setCollection);

    return uSQL.toString();
  }

  public void isValueValidity(CircularlyAccessibleValueObject[] billVO, String tblName, String[] fieldNames, String[] excFieldNames)
    throws SQLException, BusinessException
  {
    String[] fieldValues = null;
    String[] excFieldValues = null;

    StringBuffer strSql = new StringBuffer();

    PreparedStatement stmt = null;

    if ((fieldNames != null) && (fieldNames.length > 0))
      for (int j = 0; j < billVO.length; j++)
      {
        fieldValues = new String[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
          fieldValues[i] = ((String)billVO[j].getAttributeValue(fieldNames[i]));
        }
        strSql.append("SELECT ");
        for (int i = 0; i < fieldNames.length - 1; i++)
          strSql.append(fieldNames[i] + ",");
        strSql.append(fieldNames[(fieldNames.length - 1)]);
        strSql.append(" FROM " + tblName + " WHERE dr=0 and ");
        for (int i = 0; i < fieldNames.length - 1; i++)
          strSql.append(fieldNames[i] + " = '" + fieldValues[i] + "' and ");
        strSql.append(fieldNames[(fieldNames.length - 1)] + " = '" + fieldValues[(fieldNames.length - 1)] + "'");

        if (billVO[j].getStatus() == 2) {
          if ((excFieldNames != null) && (excFieldNames.length > 0)) {
            excFieldValues = new String[excFieldNames.length];
            for (int i = 0; i < excFieldNames.length; i++) {
              excFieldValues[i] = ((String)billVO[j].getAttributeValue(excFieldNames[i]));
              if ((excFieldValues[i] != null) && (!excFieldValues[i].equals("")))
                strSql.append(" and " + excFieldNames[i] + " != '" + excFieldValues[i] + "'");
            }
          }
        } else if ((billVO[j].getStatus() == 1) && 
          (excFieldNames != null) && (excFieldNames.length > 0)) {
          excFieldValues = new String[excFieldNames.length];
          for (int i = 0; i < excFieldNames.length; i++) {
            excFieldValues[i] = ((String)billVO[j].getAttributeValue(excFieldNames[i]));
            if ((excFieldValues[i] != null) && (!excFieldValues[i].equals(""))) {
              strSql.append(" and " + excFieldNames[i] + " != '" + excFieldValues[i] + "'");
            }
          }
        }
        boolean isRepeatReceiptCode = false;
        try {
          this.con = getConnection("isValueValidity");
          stmt = this.con.prepareStatement(strSql.toString());

          ResultSet rstNumber = stmt.executeQuery();

          if (rstNumber.next())
            isRepeatReceiptCode = true;
          else
            isRepeatReceiptCode = false;
        }
        finally {
          try {
            if (stmt != null)
              stmt.close();
          }
          catch (Exception e) {
          }
          try {
            removeConnection("isValueValidity");
          }
          catch (Exception e) {
          }
        }
        if (isRepeatReceiptCode) {
          BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000030"));
          throw e;
        }
      }
  }

  public void isValueValidity(AggregatedValueObject billVO, String tblMainName, String tblChildName, String[] fieldNames, String[] excFieldNames)
    throws SQLException, BusinessException
  {
    CircularlyAccessibleValueObject[] heads = new CircularlyAccessibleValueObject[1];
    heads[0] = billVO.getParentVO();
    CircularlyAccessibleValueObject[] items = billVO.getChildrenVO();

    isValueValidity(heads, tblMainName, fieldNames, excFieldNames);
  }

  public DMVO normalSave(String sHeadTable, String sBodyTable, DMVO vo, ValueRangeHashtable htRangeHeader, ValueRangeHashtable htRangeBody, boolean bNeedTS)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    DMDataVO[] dmbodyvos = vo.getBodyVOs();
    DMDataVO dmheadvo = vo.getHeaderVO();
    StringBuffer sExecuteSQL = new StringBuffer();

    DMVO dvoReturnVO = new DMVO(0);
    if (null != dmbodyvos) {
      dvoReturnVO = new DMVO(dmbodyvos.length);
    }

    if ((!this.bIsNeedDataStore) && 
      (sHeadTable != null) && (sHeadTable.trim().length() != 0) && (dmheadvo != null) && (dmheadvo.getStatus() == 3))
    {
      dmheadvo.setStatus(1);
      dmheadvo.setDrTo(1);
    }

    try
    {
      getConnection("normalSave");

      Object sHeaderPK = null;
      String sHeaderPKField = null;

      if (null != sBodyTable) {
        sExecuteSQL = new StringBuffer();
        sExecuteSQL.append(getDRDeleteSQL(sBodyTable, dmbodyvos, htRangeBody));
        saveExecute(sExecuteSQL);
      }

      if ((null != sHeadTable) && (null != dmheadvo) && (dmheadvo.getStatus() == 3)) {
        if (null != sHeadTable) {
          sExecuteSQL = new StringBuffer();
          sExecuteSQL.append(getDeleteSQL(sHeadTable, dmheadvo, htRangeHeader));
          saveExecute(sExecuteSQL);
        }
       // Object localObject1 = null;
        return null;
      }
      if (null != sHeadTable) {
        sHeaderPKField = htRangeHeader.getPrimaryKeyField();
        if (dmheadvo.getStatus() == 2) {
          sExecuteSQL = new StringBuffer();
          sExecuteSQL.append(getInsertSQL(sHeadTable, dmheadvo, htRangeHeader));
          saveExecute(sExecuteSQL);

          dvoReturnVO.getHeaderVO().setAttributeValue(sHeaderPKField, dmheadvo.getAttributeValue(sHeaderPKField));
        }

        if (dmheadvo.getStatus() == 1) {
          sExecuteSQL = new StringBuffer();
          sExecuteSQL.append(getUpdateSQL(sHeadTable, dmheadvo, htRangeHeader));
          saveExecute(sExecuteSQL);
        }

        sHeaderPK = dmheadvo.getAttributeValue(sHeaderPKField);

        if (bNeedTS) {
          dvoReturnVO.getHeaderVO().setAttributeValue("ts", queryTimeStamp(sHeadTable, sHeaderPKField, (String)sHeaderPK));
        }
      }
      if ((null != sBodyTable) && (null != dmbodyvos)) {
        ArrayList alBodyPKs = new ArrayList();
        for (int i = 0; i < dmbodyvos.length; i++) {
          if (dmbodyvos[i].getStatus() == 1) {
            sExecuteSQL = new StringBuffer();
            sExecuteSQL.append(getUpdateSQL(sBodyTable, dmbodyvos[i], htRangeBody));
            saveExecute(sExecuteSQL);
          }
          else if (dmbodyvos[i].getStatus() == 2) {
            if (null != sHeaderPK)
            {
              dmbodyvos[i].setAttributeValue(sHeaderPKField, sHeaderPK);
            }
            sExecuteSQL = new StringBuffer();
            sExecuteSQL.append(getInsertSQL(sBodyTable, dmbodyvos[i], htRangeBody));
            saveExecute(sExecuteSQL);

            dvoReturnVO.getBodyVOs()[i].setAttributeValue(htRangeBody.getPrimaryKeyField(), dmbodyvos[i].getAttributeValue(htRangeBody.getPrimaryKeyField()));
            if (null != sHeaderPK)
            {
              dvoReturnVO.getBodyVOs()[i].setAttributeValue(sHeaderPKField, sHeaderPK);
            }
          }
          if ((dmbodyvos[i].getStatus() == 1) || (dmbodyvos[i].getStatus() == 2)) {
            ((ArrayList)alBodyPKs).add(dmbodyvos[i].getAttributeValue(htRangeBody.getPrimaryKeyField()));
          }
        }
        if ((bNeedTS) && 
          (((ArrayList)alBodyPKs).size() > 0)) {
          String[] sBodyPKs = new String[((ArrayList)alBodyPKs).size()];
          sBodyPKs = (String[])(String[])((ArrayList)alBodyPKs).toArray(sBodyPKs);
          String[] sTSs = queryTimeStamps(sBodyTable, htRangeBody.getPrimaryKeyField(), sBodyPKs);

          for (int i = 0; i < dvoReturnVO.getBodyVOs().length; i++) {
            dvoReturnVO.getBodyVOs()[i].setAttributeValue("ts", sTSs[i]);
          }
        }
      }
     // Object alBodyPKs = dvoReturnVO;
      return dvoReturnVO;
    }
    finally
    {
      try
      {
        removeConnection("normalSave");
      } catch (Exception e) {
      
    
    try {
		throw e;
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    }}
  }

  public DMDataVO[] query(StringBuffer sbSql)
    throws BusinessException
  {
    ArrayList al = queryExecute(sbSql);

    DMDataVO[] ddvos = new DMDataVO[al.size()];
    ddvos = (DMDataVO[])(DMDataVO[])al.toArray(ddvos);

    al = null;

    return ddvos;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    return null;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
    throws BusinessException
  {
    return null;
  }

  public ArrayList queryExecute(StringBuffer sbSql)
    throws BusinessException
  {
    if ((null == sbSql) || (sbSql.toString().trim().length() == 0)) {
      return new ArrayList();
    }

    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      this.con = getConnection("queryExecute");
      stmt = this.con.prepareStatement(sbSql.toString());

      rs = stmt.executeQuery();

      ArrayList localArrayList = getQueryResult(rs);
      return localArrayList;
    }
    catch (Exception e)
    {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        removeConnection("queryExecute"); } catch (Exception e) {
      }
    }
    //throw localObject;
  }

  public String queryTimeStamp(String sTableName, String sPKFieldName, String sPKFieldValue)
    throws SQLException, javax.transaction.SystemException, BusinessException
  {
    if ((sPKFieldValue == null) || (sPKFieldValue.trim().length() == 0))
      return "";
    StringBuffer sbSql = new StringBuffer();
    sbSql.append(" select ts from " + sTableName + " where dr=0 and " + sPKFieldName + " in ('" + sPKFieldValue + "')");

    ArrayList al = queryExecute(sbSql);
    if (al.size() != 0) {
      String sTimeStamp = (String)((DMDataVO)al.get(0)).getAttributeValue("ts");
      if (null == sTimeStamp)
        return "";
      return sTimeStamp;
    }
    return "";
  }

  public String[] queryTimeStamps(String sTableName, String sPKFieldName, String[] sPKFieldValues)
    throws SQLException, javax.transaction.SystemException, BusinessException
  {
    String sPKFieldValue = "";
    String[] sTs = new String[sPKFieldValues.length];
    for (int i = 0; i < sTs.length; i++) {
      sTs[i] = "";
    }

    StringBuffer sbSql = new StringBuffer();
    sbSql.append(" select ").append(sPKFieldName).append(",ts from ").append(sTableName).append(" where dr=0 ").append(SQLUtil.formInSQL(sPKFieldName, sPKFieldValues));

    ArrayList al = queryExecute(sbSql);
    if (al.size() != 0) {
      for (int i = 0; i < al.size(); i++) {
        String sTimeStamp = (String)((DMDataVO)al.get(i)).getAttributeValue("ts");
        if (sTimeStamp == null)
          sTimeStamp = "";
        String sValue = (String)((DMDataVO)al.get(i)).getAttributeValue(sPKFieldName);
        for (int j = 0; j < sPKFieldValues.length; j++) {
          if (sValue.equals(sPKFieldValues[j])) {
            sTs[j] = sTimeStamp;
            break;
          }
        }
      }
    }
    return sTs;
  }

  protected void removeConnection(String sConnectionID)
    throws BusinessException
  {
    try
    {
      if ((null != this.con) && (sConnectionID.equals(this.sConnectionID))) {
        this.con.close();
        this.con = null;
        this.sConnectionID = null;
      }
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void removeRemoteBO(EJBObject remote)
    throws RemoteException
  {
    try
    {
      if (null != remote)
        remote.remove();
    } catch (Exception e) {
      reportException(e);
      if ((e instanceof RemoteException)) {
        throw ((RemoteException)e);
      }
      throw new RemoteException("Remote Call", e);
    }
  }

  public void returnAVOBillCode(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, BillCodeObjValueVO[] bcvos, AggregatedValueObject[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return;
    try
    {
      BillcodeRuleBO bcrbo = new BillcodeRuleBO();

      BillcodeGenerater bgt = new BillcodeGenerater();
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getParentVO().getAttributeValue(sBillCodeKey) == null) || (vos[i].getParentVO().getAttributeValue(sBillCodeKey).toString().trim().length() == 0) || (vos[i].getParentVO().getAttributeValue(sCorpKey) == null) || (vos[i].getParentVO().getAttributeValue(sCorpKey).toString().trim().length() == 0) || (vos[i].getParentVO().getAttributeValue(sBillTypeCodeKey) == null) || (vos[i].getParentVO().getAttributeValue(sBillTypeCodeKey).toString().trim().length() == 0))
        {
          continue;
        }

        if ((vos[i].getParentVO().getStatus() == 1) && (vos[i].getParentVO().getAttributeValue("bmustreturnbillcode") != null) && (vos[i].getParentVO().getAttributeValue("bmustreturnbillcode").toString().trim().equals("Y")))
        {
          continue;
        }

        if (vos[i].getParentVO().getStatus() == 3) {
          bcrbo.returnBillCodeOnDelete(vos[i].getParentVO().getAttributeValue(sCorpKey).toString(), vos[i].getParentVO().getAttributeValue(sBillTypeCodeKey).toString(), vos[i].getParentVO().getAttributeValue(sBillCodeKey).toString(), bcvos[i]);
        }

      }

      return;
    }
    catch (Exception e) {
    }
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000019"));
  }

  public void returnAVOBillCodeForUI(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, BillCodeObjValueVO[] bcvos, AggregatedValueObject[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    try
    {
      BillcodeGenerater bgt = new BillcodeGenerater();
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getParentVO().getAttributeValue(sBillCodeKey) == null) || (vos[i].getParentVO().getAttributeValue(sBillCodeKey).toString().trim().length() == 0) || (vos[i].getParentVO().getAttributeValue(sCorpKey) == null) || (vos[i].getParentVO().getAttributeValue(sCorpKey).toString().trim().length() == 0) || (vos[i].getParentVO().getAttributeValue(sBillTypeCodeKey) == null) || (vos[i].getParentVO().getAttributeValue(sBillTypeCodeKey).toString().trim().length() == 0))
        {
          continue;
        }

        if (vos[i].getParentVO().getStatus() != 2)
        {
          continue;
        }

      }

      return;
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000019"));
  }

  public void returnCVOBillCode(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, BillCodeObjValueVO[] bcvos, CircularlyAccessibleValueObject[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return;
    try
    {
      BillcodeRuleBO bcrbo = new BillcodeRuleBO();

      BillcodeGenerater bgt = new BillcodeGenerater();
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getAttributeValue(sBillCodeKey) == null) || (vos[i].getAttributeValue(sBillCodeKey).toString().trim().length() == 0) || (vos[i].getAttributeValue(sCorpKey) == null) || (vos[i].getAttributeValue(sCorpKey).toString().trim().length() == 0) || (vos[i].getAttributeValue(sBillTypeCodeKey) == null) || (vos[i].getAttributeValue(sBillTypeCodeKey).toString().trim().length() == 0))
        {
          continue;
        }

        if ((vos[i].getStatus() == 1) && (vos[i].getAttributeValue("bmustreturnbillcode") != null) && (vos[i].getAttributeValue("bmustreturnbillcode").toString().trim().equals("Y")))
        {
          continue;
        }

        if (vos[i].getStatus() == 3) {
          bcrbo.returnBillCodeOnDelete(vos[i].getAttributeValue(sCorpKey).toString(), vos[i].getAttributeValue(sBillTypeCodeKey).toString(), vos[i].getAttributeValue(sBillCodeKey).toString(), bcvos[i]);
        }

      }

      return;
    }
    catch (Exception e) {
    }
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000019"));
  }

  public void returnCVOBillCodeForUI(String sBillTypeCodeKey, String sCorpKey, String sBillCodeKey, BillCodeObjValueVO[] bcvos, CircularlyAccessibleValueObject[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    try
    {
      BillcodeGenerater bgt = new BillcodeGenerater();
      for (int i = 0; i < vos.length; i++) {
        if ((vos[i].getAttributeValue(sBillCodeKey) == null) || (vos[i].getAttributeValue(sBillCodeKey).toString().trim().length() == 0) || (vos[i].getAttributeValue(sCorpKey) == null) || (vos[i].getAttributeValue(sCorpKey).toString().trim().length() == 0) || (vos[i].getAttributeValue(sBillTypeCodeKey) == null) || (vos[i].getAttributeValue(sBillTypeCodeKey).toString().trim().length() == 0))
        {
          continue;
        }

        if (vos[i].getStatus() != 2)
        {
          continue;
        }

      }

      return;
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000019"));
    }
    finally
    {
    }

    //throw localObject;
  }

  public DMVO save(String sHeadTable, String sBodyTable, DMVO vo, ValueRangeHashtable htRangeHeader, ValueRangeHashtable htRangeBody, boolean bNeedTS)
    throws SQLException, javax.transaction.SystemException, BusinessException, nc.bs.pub.SystemException
  {
    DMDataVO[] dmbodyvos = vo.getBodyVOs();
    DMDataVO dmheadvo = vo.getHeaderVO();
    StringBuffer sExecuteSQL = new StringBuffer();
    Object sHeaderPK = null;
    Object sGenOIDPK = null;
    String sHeaderPKField = null;
    String sGenOIDPKField = null;
    String sHeadPKInBodyField = null;
    DMVO dmvoForOldVO = new DMVO();
    try
    {
      getConnection("save");

      if (this.bIsNeedDataStore)
      {
        if ((null != sHeadTable) && (sHeadTable.trim().length() > 0) && (null != sBodyTable) && (sBodyTable.trim().length() > 0))
        {
          sGenOIDPKField = htRangeHeader.getGenOIDKeyField();
          sGenOIDPK = dmheadvo.getAttributeValue(sGenOIDPKField);
          sHeaderPKField = htRangeHeader.getPrimaryKeyField();
          sHeaderPK = dmheadvo.getAttributeValue(sHeaderPKField);
          if (null == sHeaderPK) {
            if ((null != htRangeBody.getHeadPKInBodyField()) && (htRangeBody.getHeadPKInBodyField().trim().length() != 0))
            {
              sHeadPKInBodyField = htRangeBody.getHeadPKInBodyField();
            } else if ((null != htRangeHeader.getPrimaryKeyField()) && (htRangeHeader.getPrimaryKeyField().trim().length() != 0))
            {
              sHeadPKInBodyField = htRangeBody.getPrimaryKeyField();
            }
            if ((null != sHeadPKInBodyField) && (sHeadPKInBodyField.trim().length() != 0)) {
              for (int i = 0; i < dmbodyvos.length; i++) {
                if (null != dmbodyvos[i]) {
                  sHeaderPK = dmbodyvos[i].getAttributeValue(sHeadPKInBodyField);
                  if ((null != sHeaderPK) && (sHeaderPK.toString().trim().length() != 0)) {
                    break;
                  }
                }
              }
            }
          }
          if (dmheadvo.getStatus() == 1)
          {
            if ((null == sHeaderPK) && (sHeaderPK.toString().trim().length() != 0)) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000028"));
            }

            sExecuteSQL = new StringBuffer();

            sExecuteSQL.append(" select * from ").append(sHeadTable).append(" where dr=0 and ").append(sHeaderPKField).append("='").append(sHeaderPK).append("'");

            DMDataVO[] dmdvoForOldHeaders = query(sExecuteSQL);
            if ((dmdvoForOldHeaders == null) || (dmdvoForOldHeaders.length == 0) || (dmdvoForOldHeaders[0] == null))
            {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
            }
            DMDataVO dmdvoForOldHeader = dmdvoForOldHeaders[0];

            sExecuteSQL = new StringBuffer();
            sExecuteSQL.append(" select * from ").append(sBodyTable).append(" where dr=0 and ").append(sHeadPKInBodyField).append("='").append(sHeaderPK).append("'");

            DMDataVO[] dmdvoForOldBodys = query(sExecuteSQL);

            dmvoForOldVO.setParentVO(dmdvoForOldHeader);
            dmvoForOldVO.setChildrenVO(dmdvoForOldBodys);
            dmvoForOldVO.setStatusTo(2);
            dmvoForOldVO.setDrTo(1);

            if ((null != dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField).toString().length() != 0))
            {
              sGenOIDPK = dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField);
            }
            dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

            normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, bNeedTS);
          }
          else if (dmheadvo.getStatus() == 3)
          {
            vo.setStatusTo(1);
            vo.setDrTo(1);
          }
          else if (dmheadvo.getStatus() != 2);
        }
        else if (null != sHeadTable) {
          sGenOIDPKField = htRangeHeader.getGenOIDKeyField();
          sGenOIDPK = dmheadvo.getAttributeValue(sGenOIDPKField);
          sHeaderPKField = htRangeHeader.getPrimaryKeyField();
          sHeaderPK = dmheadvo.getAttributeValue(sHeaderPKField);
          if (dmheadvo.getStatus() == 1)
          {
            sExecuteSQL = new StringBuffer();

            sExecuteSQL.append(" select * from ").append(sHeadTable).append(" where dr=0 and ").append(sHeaderPKField).append("='").append(sHeaderPK).append("'");

            DMDataVO[] dmdvoForOldHeaders = query(sExecuteSQL);
            if ((dmdvoForOldHeaders == null) || (dmdvoForOldHeaders.length == 0) || (dmdvoForOldHeaders[0] == null))
            {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
            }
            DMDataVO dmdvoForOldHeader = dmdvoForOldHeaders[0];

            dmvoForOldVO.setParentVO(dmdvoForOldHeader);
            dmvoForOldVO.setChildrenVO(null);
            dmvoForOldVO.setStatusTo(2);
            dmvoForOldVO.setDrTo(1);

            if ((null != dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField).toString().length() != 0))
            {
              sGenOIDPK = dmvoForOldVO.getParentVO().getAttributeValue(sGenOIDPKField);
            }
            dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

            normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, bNeedTS);
          }
          else if (dmheadvo.getStatus() == 3)
          {
            vo.setStatusTo(1);
            vo.setDrTo(1);
          }
          else if (dmheadvo.getStatus() != 2);
        }
        else
        {
          Object sTotalBodyKey = null;
          sGenOIDPKField = htRangeBody.getGenOIDKeyField();
          sHeaderPKField = htRangeBody.getPrimaryKeyField();
          for (int i = 0; i < dmbodyvos.length; i++) {
            sHeaderPK = dmbodyvos[i].getAttributeValue(sHeaderPKField);
            if (dmbodyvos[i].getStatus() == 1)
            {
              sGenOIDPK = dmbodyvos[i].getAttributeValue(sGenOIDPKField);

              if (null != sTotalBodyKey)
                sTotalBodyKey = sTotalBodyKey + ",'" + sHeaderPK + "'";
              else
                sTotalBodyKey = "'" + sHeaderPK + "'";
            }
            else if (dmbodyvos[i].getStatus() == 3)
            {
              dmbodyvos[i].setStatus(1);
              dmbodyvos[i].setDrTo(1); } else {
              if (dmbodyvos[i].getStatus() != 2)
              {
                continue;
              }
            }
          }

          if (null != sTotalBodyKey) {
            sExecuteSQL = new StringBuffer();
            sExecuteSQL.append(" select * from ").append(sBodyTable).append(" where dr=0 and ").append(sHeaderPKField).append(" in (").append(sTotalBodyKey).append(")");

            DMDataVO[] dmdvoForOldBodys = query(sExecuteSQL);
            if ((dmdvoForOldBodys == null) || (dmdvoForOldBodys.length == 0) || (dmdvoForOldBodys[0] == null))
            {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000029"));
            }

            dmvoForOldVO.setParentVO(null);
            dmvoForOldVO.setChildrenVO(dmdvoForOldBodys);
            dmvoForOldVO.setStatusTo(2);
            dmvoForOldVO.setDrTo(1);

            if ((null != dmvoForOldVO.getBodyVOs()) && (dmvoForOldVO.getBodyVOs().length >= 0) && (null != dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField)) && (dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField).toString().length() != 0))
            {
              sGenOIDPK = dmvoForOldVO.getBodyVOs()[0].getAttributeValue(sGenOIDPKField);
            }
            dmvoForOldVO.setGenOIDKeyValue(sGenOIDPKField, sGenOIDPK);

            normalSave(sHeadTable, sBodyTable, dmvoForOldVO, htRangeHeader, htRangeBody, bNeedTS);
          }
        }

      }

       DMVO sTotalBodyKey = normalSave(sHeadTable, sBodyTable, vo, htRangeHeader, htRangeBody, bNeedTS);
      return sTotalBodyKey;
    }
    finally
    {
      try
      {
        removeConnection("save"); } catch (Exception e) {
      }
    }
   // throw localObject1;
  }

  public void saveExecute(StringBuffer sbSql)
    throws SQLException, javax.transaction.SystemException, BusinessException
  {
    if ((null == sbSql) || (sbSql.toString().trim().length() == 0)) {
      return;
    }

    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      this.con = getConnection("saveExecute");
      stmt = this.con.prepareStatement(sbSql.toString());

      stmt.executeUpdate();
      return; } catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        removeConnection("saveExecute"); } catch (Exception e) {
      }
    }
    //throw localObject;
  }

  protected void setData(ResultSet rs, DMDataVO invvo, ResultSetMetaData meta)
    throws SQLException
  {
    Object itemID = new String();

    for (int i = 0; i < meta.getColumnCount(); i++) {
      String columnname = meta.getColumnName(i + 1).toLowerCase().trim();

      if ((meta.getColumnType(i + 1) == 12) || (meta.getColumnType(i + 1) == 1))
      {
        itemID = rs.getString(columnname);
      }
      else itemID = rs.getObject(columnname);

      if (itemID != null)
        invvo.setAttributeValue(columnname, itemID);
      else
        invvo.setAttributeValue(columnname, null);
    }
  }

  protected void setData(ResultSet rs, CircularlyAccessibleValueObject vo, ResultSetMetaData meta, int start, int end)
    throws SQLException
  {
    Object itemID = new String();

    if (meta.getColumnCount() < start)
      start = meta.getColumnCount();
    if (meta.getColumnCount() < end) {
      end = meta.getColumnCount();
    }
    for (int i = start; i < end; i++) {
      String columnname = meta.getColumnName(i).toLowerCase().trim();

      if ((meta.getColumnType(i) == 12) || (meta.getColumnType(i) == 1))
      {
        itemID = rs.getString(columnname);
      }
      else itemID = rs.getObject(columnname);

      if (itemID != null)
        vo.setAttributeValue(columnname, itemID);
      else
        vo.setAttributeValue(columnname, null);
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

  public Integer getLocalCurrDigit(String currname)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sp.pub.PrecisionDMO", "getLocalCurrDigit", new Object[] { currname });

    String sql = "select currdigit from bd_currtype where currtypename = ?";
    Integer digit = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, currname);
      ResultSet rs = stmt.executeQuery();
      Object tmp = null;
      while (rs.next()) {
        tmp = rs.getObject(1);
        digit = tmp == null ? new Integer(2) : new Integer(tmp.toString());
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.sp.pub.PrecisionDMO", "getLocalCurrDigit", new Object[] { currname });

    return digit;
  }

  public UFDouble getInvInvoiceNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select isnull(sum(so_saleinvoice_b.nnumber),0),so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid from so_saleinvoice_b left join so_saleinvoice on so_saleinvoice.csaleid = so_saleinvoice_b.csaleid where so_saleinvoice.dbilldate<='" + endDate.toString() + "' and so_saleinvoice.dbilldate>='" + startDate.toString() + "' and so_saleinvoice_b.cinventoryid='" + cinvmandocid + "' and so_saleinvoice.fstatus=2 and so_saleinvoice.pk_corp='" + pk_corp + "' group by so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invinvoicenumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble invoiceNum = new UFDouble(rs.getBigDecimal(1));
        if (invoiceNum != null)
          invinvoicenumber = invoiceNum;
        else
          invinvoicenumber = new UFDouble(0.0D);
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return invinvoicenumber;
  }

  public UFDouble getInvOrderNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select isnull(sum(so_saleorder_b.nnumber),0),so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid from so_saleorder_b left join so_sale on so_sale.csaleid = so_saleorder_b.csaleid where so_sale.dbilldate<='" + endDate.toString() + "' and so_sale.dbilldate>='" + startDate.toString() + "' and so_saleorder_b.cinventoryid='" + cinvmandocid + "' and so_sale.fstatus=2 and so_sale.pk_corp ='" + pk_corp + "' group by so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invordernumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble orderNum = new UFDouble(rs.getBigDecimal(1));
        if (orderNum != null)
          invordernumber = orderNum;
        else
          invordernumber = new UFDouble(0.0D);
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return invordernumber;
  }

  public UFDouble getInvOutNumber(String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;

    String strSql = "select isnull(sum(noutnum),0),b.cinventoryid from ic_general_b b ,ic_general_h  h where h.cgeneralhid=b.cgeneralhid and h.cbilltypecode='4C'and b.dr=0 and h.dr=0 and b.dbizdate>='" + startDate.toString() + "' and b.dbizdate<='" + endDate.toString() + "' and b.cinventoryid='" + cinvmandocid + "' and h.fbillflag=3 and h.pk_corp = '" + pk_corp + "' group by b.cinventoryid ";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invoutnumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble outNum = new UFDouble(rs.getBigDecimal(1));
        if (outNum != null)
          invoutnumber = outNum;
        else
          invoutnumber = new UFDouble(0.0D);
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return invoutnumber;
  }
}