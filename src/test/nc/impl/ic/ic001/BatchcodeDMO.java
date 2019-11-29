package nc.impl.ic.ic001;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.ICLockTool;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.bill.MiscDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class BatchcodeDMO extends SmartDMO
{
  public BatchcodeDMO()
    throws NamingException, SystemException
  {
  }

  private void deleteRecords(String sSQL)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSQL);
      SCMEnv.out(sSQL);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private boolean checkData(String sCorp, BatchcodeVO[] bvos)
    throws BusinessException
  {
    if ((bvos == null) || (bvos.length == 0)) {
      return true;
    }
    try
    {
      String[] sInvIDs = new String[bvos.length];
      for (int i = 0; i < bvos.length; i++) {
        sInvIDs[i] = bvos[i].getPk_invbasdoc();
      }

      MiscDMO miscdmo = new MiscDMO();
      InvVO[] invvos = miscdmo.getInvInfoBasDoc(sCorp, sInvIDs);
      for (int i = 0; i < bvos.length; i++) {
        if (bvos[i].getStatus() == 3)
          continue;
        if (bvos[i].getQualitymanflag() == null) {
          bvos[i].setQualitymanflag(String.valueOf(invvos[i].getIsValidateMgt()));
          bvos[i].setQualitydaynum(invvos[i].getQualityDay());
        }

        if ((bvos[i].getQualitymanflag().equals("1")) || (bvos[i].getQualitymanflag().equals("Y"))) {
          if (bvos[i].getDproducedate() == null) {
            if (bvos[i].getDinvalidate() != null) {
              UFDate dscrq = bvos[i].getDinvalidate().getDateBefore(bvos[i].getQualitydaynum().intValue());
              bvos[i].setDproducedate(dscrq);
            } else {
              throw new BusinessException(ResBase.get001Check6());
            }
          } else {
            UFDate dvalidate = bvos[i].getDproducedate().getDateAfter(bvos[i].getQualitydaynum().intValue());
            bvos[i].setDinvalidate(dvalidate);
          }
        } else {
          bvos[i].setDproducedate(null);
          bvos[i].setDinvalidate(null);
        }
      }
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }

    ArrayList alKeys = new ArrayList();
    ArrayList alPKs = new ArrayList();
    ArrayList alNewPKs = new ArrayList();
    String pk_invbasdoc = null;
    String vbatchcode = null;
    String sKey = null;
    String sPK = null;
    ArrayList alInvBatch = new ArrayList();
    String sSql = "select pk_batchcode from scm_batchcode where dr=0 ";
    for (int i = 0; i < bvos.length; i++) {
      int iStatus = bvos[i].getStatus();
      if (iStatus == 2) {
        pk_invbasdoc = bvos[i].getPk_invbasdoc();
        vbatchcode = bvos[i].getVbatchcode();
        sKey = pk_invbasdoc + vbatchcode;
        sPK = bvos[i].getPk_batchcode() == null ? " " : bvos[i].getPk_batchcode();
        alKeys.add(sKey);
        alPKs.add(sPK);
      }
      if (iStatus == 3) {
        alInvBatch.add(bvos[i].getPk_batchcode());
      }
    }

    try
    {
      if ((alInvBatch != null) && (alInvBatch.size() > 0) && 
        (isBatchBeUsed(alInvBatch))) {
        throw new BusinessException(ResBase.get001Check5());
      }

      sSql = sSql + GeneralSqlString.formInSQL("pk_invbasdoc||vbatchcode", alKeys);
      Object[] os = selectBy2(sSql);

      if ((os != null) && (os.length > 0)) {
        Object[] o = (Object[])(Object[])os[0];
        if ((o != null) && (o.length > 0) && (o[0] != null))
          return false;
      }
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }

    return true;
  }

  private boolean isBatchBeUsed(ArrayList alInvBatch) throws BusinessException {
    return isBatchUsedInXCL(alInvBatch) ? true : isBatchUsedInQC(alInvBatch);
  }

  private boolean isBatchUsedInQC(ArrayList alInvBatch) throws BusinessException {
    if ((alInvBatch == null) || (alInvBatch.size() == 0)) {
      return false;
    }
    StringBuffer sSql = new StringBuffer("select ccheckbill_b1id from qc_checkbill_b1 where dr = 0 ");
    sSql.append(" and (cbaseid || vinvbatchcode) in (select pk_invbasdoc||vbatchcode from scm_batchcode where 1=1 ");
    sSql.append(GeneralSqlString.formInSQL("pk_batchcode", alInvBatch));
    sSql.append(")");
    try {
      Object[] o = selectBy2(sSql.toString());
      if ((o != null) && (o.length > 0))
        return true;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return false;
  }

  private boolean isBatchUsedInXCL(ArrayList alInvBatch) throws BusinessException {
    if ((alInvBatch == null) || (alInvBatch.size() == 0))
      return false;
    StringBuffer sSql = new StringBuffer("select distinct (b.pk_invbasdoc||a.vbatchcode) vkey ");
    sSql.append(" from ic_general_b a,bd_invmandoc b ");
    sSql.append(" where a.cinventoryid=b.pk_invmandoc and a.dr=0 and b.dr=0 and vbatchcode is not null ");
    sSql.append(" and (b.pk_invbasdoc||a.vbatchcode) in (select pk_invbasdoc||vbatchcode from scm_batchcode where 1=1 ");
    sSql.append(GeneralSqlString.formInSQL("pk_batchcode", alInvBatch));
    sSql.append(")");
    try
    {
      Object[] o = selectBy2(sSql.toString());
      if ((o != null) && (o.length > 0))
        return true;
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return false;
  }

  public void checkandSaveBatchcode(AggregatedValueObject voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getChildrenVO() == null) || (voNowBill.getChildrenVO().length == 0)) {
      return;
    }
    GeneralBillItemVO[] bvos = (GeneralBillItemVO[])(GeneralBillItemVO[])voNowBill.getChildrenVO();
    Vector vbc = new Vector();
    String pk_invbasdoc = null;
    String vbatchcode = null;
    UFDate dvalidate = null;
    UFDate dproducedate = null;

    HashMap hm = new HashMap();
    String sKey = null;
    for (int i = 0; i < bvos.length; i++) {
      int iStatus = bvos[i].getStatus();
      vbatchcode = bvos[i].getVbatchcode();

      if (bvos[i].getInOutFlag() == -1) {
        continue;
      }
      if ((iStatus != 0) && (iStatus != 3) && (vbatchcode != null) && (vbatchcode.trim().length() > 0)) {
        pk_invbasdoc = bvos[i].getCinvbasid();
        dvalidate = bvos[i].getDvalidate();
        dproducedate = bvos[i].getScrq();

        sKey = pk_invbasdoc + vbatchcode;

        if (!hm.containsKey(sKey)) {
          hm.put(sKey, sKey);

          BatchcodeVO vo = new BatchcodeVO();
          vo.setPk_invbasdoc(pk_invbasdoc);
          vo.setVbatchcode(vbatchcode);
          vo.setDinvalidate(dvalidate);
          vo.setDproducedate(dproducedate);
          vo.setAttributeValue("pk_batchcode", bvos[i].getAttributeValue("pk_batchcode"));
          vo.setAttributeValue("cqualitylevelid", bvos[i].getAttributeValue("cqualitylevelid"));
          vo.setAttributeValue("vvendbatchcode", bvos[i].getAttributeValue("vvendbatchcode"));
          vo.setAttributeValue("tchecktime", bvos[i].getAttributeValue("tchecktime"));
          vo.setAttributeValue("ts", bvos[i].getAttributeValue("bcts"));
          vo.setAttributeValue("vnote", bvos[i].getAttributeValue("vnotebc"));
          if (bvos[i].getAttributeValue("tbatchtime") == null)
            vo.setTbatchtime(new UFDateTime(System.currentTimeMillis()));
          else {
            vo.setTbatchtime(new UFDateTime(bvos[i].getAttributeValue("tbatchtime").toString()));
          }
          if (bvos[i].getAttributeValue("bseal") == null)
            vo.setBseal(new UFBoolean("N"));
          vo.setAttributeValue("invname", bvos[i].getInvname());
          for (int j = 1; j < 21; j++) {
            vo.setAttributeValue("vdef" + String.valueOf(j), bvos[i].getAttributeValue("vbcuser" + String.valueOf(j)));
            vo.setAttributeValue("pk_defdoc" + String.valueOf(j), bvos[i].getAttributeValue("pk_defdocbc" + String.valueOf(j)));
          }

          vbc.add(vo);
        }
      }
    }

    if ((vbc == null) || (vbc.size() == 0)) {
      return;
    }
    BatchcodeVO[] vos = new BatchcodeVO[vbc.size()];
    vbc.copyInto(vos);
    GeneralBillHeaderVO hvo = (GeneralBillHeaderVO)voNowBill.getParentVO();

    checkandSaveBatchcode(vos, hvo.getCoperatoridnow(), hvo.getPk_corp());
  }

  private void checkandSaveBatchcode(BatchcodeVO[] vos, String sUserID, String sCorp)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    Vector vUnique = new Vector();
    Vector vKey = new Vector();
    String sKey = null;
    String pk_invbasdoc = null;
    String vbatchcode = null;
    for (int i = 0; i < vos.length; i++) {
      pk_invbasdoc = vos[i].getPk_invbasdoc();
      vbatchcode = vos[i].getVbatchcode();
      if ((pk_invbasdoc == null) || (vbatchcode == null) || (pk_invbasdoc.length() == 0) || (vbatchcode.length() == 0)) {
        throw new BusinessException("存货或批次号为空，不能保存！");
      }
      sKey = pk_invbasdoc + vbatchcode;
      if (!vKey.contains(sKey)) {
        vKey.add(sKey);
        vUnique.add(vos[i]);
      }
    }

    if ((vUnique == null) || (vUnique.size() == 0)) {
      return;
    }

    BatchcodeVO[] bcvos = new BatchcodeVO[vUnique.size()];
    vUnique.copyInto(bcvos);
    String[] sKeys = new String[vKey.size()];
    vKey.copyInto(sKeys);

    String sSql = "select (pk_invbasdoc||vbatchcode) as skey, pk_batchcode, ts from scm_batchcode where dr=0 ";
    sSql = sSql + GeneralSqlString.formInSQL("(pk_invbasdoc||vbatchcode)", sKeys);
    try
    {
      Object[] os = selectBy2(sSql);
      HashMap map = new HashMap();

      if ((os != null) && (os.length > 0)) {
        for (int i = 0; i < os.length; i++) {
          if (os[i] != null) {
            Object[] oo = (Object[])(Object[])os[i];
            map.put(oo[0].toString(), oo);
          }

        }

      }

      for (int i = 0; i < bcvos.length; i++) {
        String s = bcvos[i].getPk_invbasdoc() + bcvos[i].getVbatchcode();
        if (!map.containsKey(s)) {
          bcvos[i].setStatus(2);
          bcvos[i].setBseal(new UFBoolean("N"));
          bcvos[i].setPk_batchcode(getOIDs(sCorp, 1)[0]);
        }
        else
        {
          bcvos[i].setStatus(1);
          bcvos[i].setPk_batchcode(((Object[])map.get(s))[1].toString());
          if (bcvos[i].getTs() == null) {
            bcvos[i].setTs((UFDateTime)(UFDateTime)((Object[])map.get(s))[2]);
          }
        }
      }
      saveBatchcode(bcvos, sUserID, sCorp);
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }
  }

  public BatchcodeVO[] queryBatchcode(ConditionVO[] voConds, String pk_corp) throws BusinessException {
    if ((pk_corp == null) || (pk_corp.trim().length() == 0))
      return null;
    BatchcodeVO[] voRet = null;
    String sWhere = " where a.dr=0 and a.pk_invbasdoc in(select distinct pk_invbasdoc from bd_invmandoc where pk_corp='" + pk_corp + "' and wholemanaflag='Y') ";
    if (voConds != null) {
      for (int i = 0; i < voConds.length; i++) {
        if ((voConds[i] != null) && (voConds[i].getFieldCode() != null)) {
          if (i == 0) {
            voConds[i].setLogic(true);
          }
          String sFieldCode = voConds[i].getFieldCode();
          if ("pk_invbasdoc".equals(sFieldCode)) {
            voConds[i].setFieldCode("a.pk_invbasdoc in(select pk_invbasdoc from bd_invmandoc where pk_invmandoc");
            sWhere = sWhere + voConds[i].getSQLStr() + ")";
          } else {
            sWhere = sWhere + voConds[i].getSQLStr();
          }
        }
      }

    }

    StringBuffer sSql = new StringBuffer("select ");
    sSql.append("  a.pk_batchcode,a.pk_invbasdoc,a.vbatchcode,isnull(a.bseal,'N') as pk_invbasdoc_old,a.vbatchcode as vbatchcode_old,a.vvendbatchcode,a.tchecktime,a.cqualitylevelid,a.dproducedate,a.dvalidate,a.bseal,a.tbatchtime,a.vdef1,a.vdef2,a.vdef3,a.vdef4,a.vdef5,a.vdef6,a.vdef7,a.vdef8,a.vdef9,a.vdef10,a.vdef11,a.vdef12,a.vdef13,a.vdef14,a.vdef15,a.vdef16,a.vdef17,a.vdef18,a.vdef19,a.vdef20,a.pk_defdoc1,a.pk_defdoc2,a.pk_defdoc3,a.pk_defdoc4,a.pk_defdoc5,a.pk_defdoc6,a.pk_defdoc7,a.pk_defdoc8,a.pk_defdoc9,a.pk_defdoc10,a.pk_defdoc11,a.pk_defdoc12,a.pk_defdoc13,a.pk_defdoc14,a.pk_defdoc15,a.pk_defdoc16,a.pk_defdoc17,a.pk_defdoc18,a.pk_defdoc19,a.pk_defdoc20,a.vnote,a.ts,b.qualitymanflag,b.qualitydaynum from scm_batchcode a inner join bd_invmandoc b on a.pk_invbasdoc=b.pk_invbasdoc and b.pk_corp='" + pk_corp + "'");
    sSql.append(sWhere);
    try {
      voRet = (BatchcodeVO[])(BatchcodeVO[])selectBySql2(sSql.toString(), BatchcodeVO.class);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return voRet;
  }

  public BatchcodeVO[] saveBatchcode(BatchcodeVO[] bvos, ClientLink cl) throws BusinessException {
    BatchcodeVO[] voRet = null;
    if ((bvos == null) || (bvos.length == 0))
      return null;
    String sUserID = cl.getUser();
    String sCorp = cl.getCorp();

    Vector vDel = new Vector();
    Vector vUpdate = new Vector();
    for (int i = 0; i < bvos.length; i++) {
      if (bvos[i].getStatus() == 3)
        vDel.add(bvos[i]);
      else {
        vUpdate.add(bvos[i]);
      }
    }
    BatchcodeVO[] delvos = null;
    BatchcodeVO[] updatevos = null;
    if ((vDel != null) && (vDel.size() > 0)) {
      delvos = new BatchcodeVO[vDel.size()];
      vDel.copyInto(delvos);
      voRet = saveBatchcode(delvos, sUserID, sCorp);
      if ((vUpdate != null) && (vUpdate.size() > 0))
        return voRet;
    }
    if ((vUpdate != null) && (vUpdate.size() > 0)) {
      updatevos = new BatchcodeVO[vUpdate.size()];
      vUpdate.copyInto(updatevos);
    }
    if (updatevos == null)
      return null;
    return saveBatchcode(updatevos, sUserID, sCorp);
  }

  private BatchcodeVO[] saveBatchcode(BatchcodeVO[] bvos, String sUserID, String sCorp) throws BusinessException {
    String[] sPks = null;
    try {
      if (!checkData(sCorp, bvos))
        throw new BusinessException(ResBase.get001Check4());
      Vector vTs = new Vector();
      Vector vLocks = new Vector();
      for (int i = 0; i < bvos.length; i++) {
        if ((bvos[i].getAttributeValue("pk_invbasdoc_old") != null) && (bvos[i].getAttributeValue("pk_invbasdoc_old").toString().equals("Y")) && (bvos[i].getStatus() == 1) && (bvos[i].getBseal().booleanValue())) {
          bvos[i].setStatus(0);
        }
        if ((bvos[i].getStatus() == 2) || (bvos[i].getPk_batchcode() == null))
          bvos[i].setPk_batchcode(getOIDs(sCorp, 1)[0]);
        if ((bvos[i].getStatus() == 3) || (bvos[i].getStatus() == 1)) {
          vLocks.add(bvos[i].getPk_batchcode());
          vTs.add(bvos[i]);
        }
      }
      boolean b;
      if ((vLocks != null) && (vLocks.size() > 0)) {
        sPks = new String[vLocks.size()];
        vLocks.copyInto(sPks);
        BatchcodeVO[] vos = new BatchcodeVO[vTs.size()];
        vTs.copyInto(vos);

        String sOrgTs = null; String sNowTs = null;
        HashMap hm = queryTs(sPks);
        if ((hm != null) && (hm.size() > 0))
          for (int i = 0; i < vos.length; i++) {
            if (vos[i].getStatus() == 0)
              continue;
            sOrgTs = (String)hm.get(vos[i].getPk_batchcode());
            sNowTs = vos[i].getTs() == null ? null : vos[i].getTs().toString();

            if ((sNowTs == null) || ((sOrgTs != null) && (sNowTs != null) && (!sOrgTs.trim().equals(sNowTs.trim())))) {
              SCMEnv.out("i=" + i + "1====" + sNowTs + "2====" + sOrgTs);
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000006"));  
            }
          }
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000006"));
        }

        b = ICLockTool.setLockForPks(sPks, sUserID);
      }

      maintain(bvos);
      freshTs(bvos);

      deleteRecords("delete from scm_batchcode where dr=1 ");
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    finally
    {
      if (sPks != null)
        ICLockTool.releaseLockForPks(sPks, sUserID);
    }
    return bvos;
  }

  private HashMap queryTs(String[] sPKs) throws SQLException {
    if ((sPKs == null) || (sPKs.length == 0))
      return null;
    String sSQL = "select pk_batchcode,ts from scm_batchcode where dr=0 " + GeneralSqlString.formInSQL("pk_batchcode", sPKs);

    BatchcodeVO[] vos = (BatchcodeVO[])(BatchcodeVO[])selectBySql2(sSQL, BatchcodeVO.class);
    if ((vos == null) || (vos.length == 0))
      return null;
    HashMap hm = new HashMap();
    for (int i = 0; i < vos.length; i++) {
      if (!hm.containsKey(vos[i].getPk_batchcode())) {
        hm.put(vos[i].getPk_batchcode(), vos[i].getTs().toString());
      }
    }

    return hm;
  }

  public void checkandsaveQCBatchcode(BatchcodeVO[] vos, String sUserID, String sCorp)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return;
    BatchcodeVO[] existvos = null;
    ArrayList alKey = new ArrayList();
    String sKey = null;
    String pk_invbasdoc = null;
    String vbatchcode = null;
    for (int i = 0; i < vos.length; i++) {
      pk_invbasdoc = vos[i].getPk_invbasdoc();
      vbatchcode = vos[i].getVbatchcode();
      if ((pk_invbasdoc == null) || (vbatchcode == null) || (pk_invbasdoc.length() == 0) || (vbatchcode.length() == 0)) {
        throw new BusinessException("存货或批次号为空，不能保存！");
      }
      sKey = pk_invbasdoc + vbatchcode;

      alKey.add(sKey);
    }

    StringBuffer sSql = new StringBuffer("select ");
    sSql.append("   a.pk_batchcode,a.pk_invbasdoc,a.vbatchcode,a.vvendbatchcode,a.tchecktime,a.cqualitylevelid,a.dproducedate,a.dvalidate,a.bseal,a.tbatchtime,a.vdef1,a.vdef2,a.vdef3,a.vdef4,a.vdef5,a.vdef6,a.vdef7,a.vdef8,a.vdef9,a.vdef10,a.vdef11,a.vdef12,a.vdef13,a.vdef14,a.vdef15,a.vdef16,a.vdef17,a.vdef18,a.vdef19,a.vdef20,a.pk_defdoc1,a.pk_defdoc2,a.pk_defdoc3,a.pk_defdoc4,a.pk_defdoc5,a.pk_defdoc6,a.pk_defdoc7,a.pk_defdoc8,a.pk_defdoc9,a.pk_defdoc10,a.pk_defdoc11,a.pk_defdoc12,a.pk_defdoc13,a.pk_defdoc14,a.pk_defdoc15,a.pk_defdoc16,a.pk_defdoc17,a.pk_defdoc18,a.pk_defdoc19,a.pk_defdoc20,a.vnote,a.ts,b.qualitymanflag,b.qualitydaynum from scm_batchcode a inner join bd_invmandoc b on a.pk_invbasdoc=b.pk_invbasdoc and b.pk_corp='" + sCorp + "'");
    sSql.append(GeneralSqlString.formInSQL("a.pk_invbasdoc||a.vbatchcode", alKey));
    try
    {
      existvos = (BatchcodeVO[])(BatchcodeVO[])selectBySql2(sSql.toString(), BatchcodeVO.class);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }

    UFDate dproducedate = null;
    UFDate dvalidate = null;
    String cqualitylevelid = null;
    String vvendbatchcode = null;
    UFDateTime tchecktime = null;
    int qualitydaynum = 0;
    for (int i = 0; i < vos.length; i++) {
      pk_invbasdoc = vos[i].getPk_invbasdoc();
      vbatchcode = vos[i].getVbatchcode();
      dproducedate = vos[i].getDproducedate();
      cqualitylevelid = vos[i].getCqualitylevelid();
      vvendbatchcode = vos[i].getVvendbatchcode();
      tchecktime = vos[i].getTchecktime();
      vos[i].setTbatchtime(new UFDateTime(System.currentTimeMillis()));

      for (int j = 0; j < existvos.length; j++) {
        if ((pk_invbasdoc.equals(existvos[j].getPk_invbasdoc())) && (vbatchcode.equals(existvos[j].getVbatchcode()))) {
          vos[i] = existvos[j];
          if (dproducedate != null)
            dvalidate = dproducedate.getDateAfter(qualitydaynum);
          vos[i].setDproducedate(dproducedate);
          vos[i].setDinvalidate(dvalidate);
          vos[i].setCqualitylevelid(cqualitylevelid);
          vos[i].setVvendbatchcode(vvendbatchcode);
          vos[i].setTchecktime(tchecktime);
          vos[i].setStatus(1);
          break;
        }
        vos[i].setTbatchtime(new UFDateTime(System.currentTimeMillis()));
        vos[i].setStatus(2);
      }

    }

    checkandSaveBatchcode(vos, sUserID, sCorp);
  }

  private void freshTs(BatchcodeVO[] bvos) throws BusinessException
  {
    if ((bvos == null) || (bvos.length == 0)) {
      return;
    }
    Vector v = new Vector();
    for (int i = 0; i < bvos.length; i++) {
      if (bvos[i].getStatus() == 3)
        continue;
      v.add(bvos[i].getPk_batchcode());
    }
    String[] spks = new String[v.size()];
    v.copyInto(spks);
    try {
      HashMap hm = queryTs(spks);
      if ((hm == null) || (hm.size() == 0))
        return;
      for (int i = 0; i < bvos.length; i++)
        bvos[i].setTs(new UFDateTime((String)hm.get(bvos[i].getPk_batchcode())));
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
  }
}