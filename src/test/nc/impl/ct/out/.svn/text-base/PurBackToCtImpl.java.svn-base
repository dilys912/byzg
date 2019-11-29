package nc.impl.ct.out;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import nc.bs.ct.pub.CTCheckDMO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.scm.inter.ArapHelper;
import nc.bs.scm.pub.bill.ScmImpl;
import nc.impl.ct.ct0101.TypeDMO;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.pu.inter.IPuToCt_PoToCt;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.arap.inter.ContractQueryVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.cenpur.service.ChgPriceMnyVO;
import nc.vo.scm.ctpo.CtAllotImprestPoVO;
import nc.vo.scm.ctpo.CtExecImprestVO;
import nc.vo.scm.ctpo.CtStatusToPoVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.SaveHintException;

public class PurBackToCtImpl extends ScmImpl
  implements ICtToPo_BackToCt
{
  private void caculateNeedPayMny(DMDataVO[] vo)
  {
    if ((vo != null) && (vo.length > 0)) {
      int iLen = vo.length;
      UFDouble ufdNeedPayMny = null;
      UFDouble ufdCtNeedPayMny = null;
      UFDouble ufdNotVerify = null;
      UFDouble ufd0 = new UFDouble(0);
      for (int i = 0; i < iLen; i++) {
        ufdNeedPayMny = (UFDouble)vo[i].getAttributeValue("dapmny");
        ufdNotVerify = (UFDouble)vo[i].getAttributeValue("dnotverifymny");

        if (ufdNeedPayMny == null)
          ufdNeedPayMny = ufd0;
        if (ufdNotVerify == null)
          ufdNotVerify = ufd0;
        ufdCtNeedPayMny = ufdNeedPayMny.sub(ufdNotVerify);

        vo[i].setAttributeValue("ctdapmny", ufdCtNeedPayMny);
      }
    }
  }

  private String getSqlToArap(String pk_corp, String sWhere)
  {
    StringBuffer sbWhere = new StringBuffer(" ct_manage.activeflag=0 and ct_manage.dr=0 and ct_manage_b.dr = 0 and ct_type.nbusitype = 0 and ct_manage.ifearly = 'N' ");

    if (pk_corp != null) {
      sbWhere.append(" and ct_manage.pk_corp='");
      sbWhere.append(pk_corp);
      sbWhere.append("' ");
    }
    if ((null != sWhere) && (sWhere.trim().length() > 0)) {
      sbWhere.append(" and (");
      sbWhere.append(sWhere);
      sbWhere.append(")");
    }
    StringBuffer sql = new StringBuffer("SELECT DISTINCT ct_manage.ct_code FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");

    sql.append(" where ");
    sql.append(sbWhere.toString());

    return sql.toString();
  }

  private String getSqlToPu(String pk_corp, String sWhere)
  {
    StringBuffer sbWhere = new StringBuffer(" ct_manage.activeflag=0 and ct_manage.dr=0 and ct_manage_b.dr = 0 and ct_type.nbusitype = 0 and ct_manage.ifearly = 'N' ");

    if (pk_corp != null) {
      sbWhere.append(" and ct_manage.pk_corp='");
      sbWhere.append(pk_corp);
      sbWhere.append("'");
    }
    if ((null != sWhere) && (sWhere.trim().length() > 0)) {
      sbWhere.append(" and (");
      sbWhere.append(sWhere);
      sbWhere.append(")");
    }
    StringBuffer sql = new StringBuffer("SELECT DISTINCT ct_manage.pk_ct_manage FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");

    sql.append(" where ");
    sql.append(sbWhere.toString());

    return sql.toString();
  }

  public CtStatusToPoVO isCtActive(CtStatusToPoVO vo)
    throws BusinessException
  {
    CtStatusToPoVO retVO = null;
    try {
      PurForCtDMO dmo = new PurForCtDMO();
      retVO = dmo.isCtActive(vo);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException("Caused by:", e);
    }
    return retVO;
  }

  public DMDataVO[] qryExecImprest(ArrayList alConds)
    throws BusinessException
  {
    DMDataVO[] voRet = null;
    if ((alConds == null) || (alConds.size() <= 0))
      return null;
    try
    {
      String sWhere = (String)alConds.get(0);
      String pk_corp = (String)alConds.get(1);

      ICreateCorpQueryService srv = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());

      boolean isPurUsed = srv.isEnabled(pk_corp, "PO");
      if (!isPurUsed) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000156"));
      }

      boolean isArap = srv.isEnabled(pk_corp, "AR");
      if (!isArap) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000157"));
      }

      PurBackToCtDMO dmo1 = new PurBackToCtDMO();
      voRet = dmo1.qryExecImprest(alConds);

      Hashtable htPo = null;
      IPuToCt_PoToCt po = (IPuToCt_PoToCt)NCLocator.getInstance().lookup(IPuToCt_PoToCt.class.getName());

      if (po != null) {
        htPo = po.queryCtPayExecInfo(pk_corp, getSqlToPu(pk_corp, sWhere));
      }

      ContractQueryVO voCond = new ContractQueryVO();
      voCond.setPk_corp(pk_corp);
      voCond.setSubcolumn("ct_code");
      voCond.setSubsql(getSqlToArap(pk_corp, sWhere));

      Hashtable htArap = ArapHelper.getContractPr(voCond);

      if ((voRet != null) && (voRet.length > 0)) {
        int iLen = voRet.length;

        String pk_ct_manage = null;

        CtExecImprestVO voPo = null;

        String ct_code = null;

        UFDouble ufdNotVerifyMny = null;

        for (int i = 0; i < iLen; i++) {
          pk_ct_manage = (String)voRet[i].getAttributeValue("pk_ct_manage");
          ct_code = (String)voRet[i].getAttributeValue("ct_code");
          if ((pk_ct_manage != null) && (htPo != null) && (htPo.size() > 0) && (htPo.containsKey(pk_ct_manage)))
          {
            voPo = (CtExecImprestVO)htPo.get(pk_ct_manage);
            if (voPo != null)
            {
              voRet[i].setAttributeValue("dapmny", voPo.getDapmny());
              voRet[i].setAttributeValue("dinvoicemny", voPo.getDinvoicemny());
              voRet[i].setAttributeValue("dpaymny", voPo.getDpaymny());
            }

            htPo.remove(pk_ct_manage);
          }
          if ((ct_code == null) || (htArap == null) || (htArap.size() <= 0) || (!htArap.containsKey(ct_code)))
          {
            continue;
          }
          ufdNotVerifyMny = (UFDouble)htArap.get(ct_code);
          if (ufdNotVerifyMny != null)
          {
            voRet[i].setAttributeValue("dnotverifymny", ufdNotVerifyMny);
          }

          htArap.remove(ct_code);
        }

      }

      caculateNeedPayMny(voRet);
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  public CtAllotImprestPoVO[] qryNeedAllotImprestBills(ArrayList alConds)
    throws BusinessException
  {
    CtAllotImprestPoVO[] voaRet = null;
    if ((alConds == null) || (alConds.size() <= 0)) {
      return null;
    }
    String sPK = (String)alConds.get(0);
    try {
      if (sPK != null)
      {
        IPuToCt_PoToCt po = (IPuToCt_PoToCt)NCLocator.getInstance().lookup(IPuToCt_PoToCt.class.getName());

        if (po != null)
          voaRet = po.queryOrderInfoForPrePay(sPK);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voaRet;
  }

  private ArrayList<String> convertMnyToCtCorp(PurBackToCtDMO pbtcdmo, ParaPoToCtRewriteVO[] pptcrvos)
    throws Exception
  {
    ArrayList alCtRowID = new ArrayList();

    for (int i = 0; i < pptcrvos.length; i++) {
      alCtRowID.add(pptcrvos[i].getCContractRowId());
    }
    HashMap hmCtCorpAndHeadID = null;
    hmCtCorpAndHeadID = pbtcdmo.queryCtCorpAndHeadIDByCtRowID(alCtRowID);

    ArrayList alHeadIDs = new ArrayList();

    String sCtCorp = null;
    String sOrderCorp = null;
    String sHeadID = null;
    ChgPriceMnyVO[] chgMnyVOs = null;

    for (int i = 0; i < pptcrvos.length; i++)
    {
      sOrderCorp = pptcrvos[i].getCorpId();

      String[] sValue = (String[])(String[])hmCtCorpAndHeadID.get(pptcrvos[i].getCContractRowId());
      sCtCorp = sValue[0];
      sHeadID = sValue[1];

      if (!alHeadIDs.contains(sHeadID)) {
        alHeadIDs.add(sHeadID);
      }
      if ((sOrderCorp == null) || (sCtCorp == null) || (sOrderCorp.trim().equals(sCtCorp))) {
        continue;
      }
      ChgPriceMnyVO mnyVO = new ChgPriceMnyVO();

      mnyVO.setSrcVal(pptcrvos[i].getDSummny());

      mnyVO.setSrcCorpId(sOrderCorp);

      mnyVO.setDstCorpId(sCtCorp);

      mnyVO.setDRateDate(pptcrvos[i].getBillDate());

      chgMnyVOs = ChgDataUtil.chgPriceByCorp(new ChgPriceMnyVO[] { mnyVO });

      pptcrvos[i].setDSummny(chgMnyVOs[0].getDstVal());
    }

    return alHeadIDs;
  }

  public void writeBackAccuOrdData(ParaPoToCtRewriteVO[] pptcrvos)
    throws BusinessException
  {
    try
    {
      if ((pptcrvos == null) || (pptcrvos.length == 0)) {
        return;
      }
      PurBackToCtDMO pbtcdmo = new PurBackToCtDMO();

      ArrayList alHeadIDs = convertMnyToCtCorp(pbtcdmo, pptcrvos);

      new CTCheckDMO().checkStatusBatchWhenRef(alHeadIDs, 2);

      pbtcdmo.writeBackAccuOrdData(pptcrvos);

      boolean bUserChoose = pptcrvos[0].isFirstTime();
      if (!bUserChoose) {
        return;
      }
      TypeDMO typedmo = new TypeDMO();
      String[] sContractBIDs = new String[pptcrvos.length];
      for (int i = 0; i < pptcrvos.length; i++) {
        sContractBIDs[i] = pptcrvos[i].getCContractRowId();
      }

      Hashtable ht = typedmo.queryByContractIDs(sContractBIDs);

      boolean bMustException = false;
      boolean bNeedException = false;
      Hashtable htMust = new Hashtable();
      Hashtable htNeedReply = new Hashtable();
      Hashtable htFullCheck = new Hashtable();

      DMDataVO dmdvo = null;
      int icontroltype = 0;
      UFBoolean bismustcontrol = null;
      String sCtCode = null; String sRowNo = null;
      UFDouble ufd0 = new UFDouble(0);
      UFDouble ufdNumMinus = null;
      UFDouble ufdMnyMinus = null;

      String sMessage = null;

      for (int i = 0; i < pptcrvos.length; i++) {
        if (ht.containsKey(pptcrvos[i].getCContractRowId())) {
          dmdvo = (DMDataVO)ht.get(pptcrvos[i].getCContractRowId());
          if (null == dmdvo.getAttributeValue("controltype"))
            icontroltype = 0;
          else {
            icontroltype = Integer.parseInt(dmdvo.getAttributeValue("controltype").toString());
          }

          bismustcontrol = new UFBoolean((dmdvo.getAttributeValue("ismustcontrol") == null) || (dmdvo.getAttributeValue("ismustcontrol").toString().trim().length() == 0) ? "N" : dmdvo.getAttributeValue("ismustcontrol").toString());

          sCtCode = (String)dmdvo.getAttributeValue("ct_code");
          sRowNo = (String)dmdvo.getAttributeValue("crowno");
          ufdNumMinus = (UFDouble)dmdvo.getAttributeValue("numminus");
          ufdMnyMinus = (UFDouble)dmdvo.getAttributeValue("moneyminus");

          switch (icontroltype)
          {
          case 1:
            if (ufdNumMinus.compareTo(ufd0) <= 0) {
              continue;
            }
            sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000210", null, new String[] { sCtCode, sRowNo });

            if (bismustcontrol.booleanValue()) {
              htMust.put(sCtCode, sMessage + "\r\n");
              bMustException = true;
            } else {
              htNeedReply.put(sCtCode, sMessage + "\r\n");
              bNeedException = true;
            }
            break;
          case 2:
            if (ufdMnyMinus.compareTo(ufd0) <= 0) {
              continue;
            }
            sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000211", null, new String[] { sCtCode, sRowNo });

            if (bismustcontrol.booleanValue()) {
              htMust.put(sCtCode, sMessage + "\r\n");
              bMustException = true;
            } else {
              htNeedReply.put(sCtCode, sMessage + "\r\n");
              bNeedException = true;
            }
            break;
          case 3:
            if (ufdNumMinus.compareTo(ufd0) > 0)
            {
              sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000210", null, new String[] { sCtCode, sRowNo });

              if (bismustcontrol.booleanValue()) {
                htMust.put(sCtCode, sMessage + "\r\n");
                bMustException = true;
              } else {
                htNeedReply.put(sCtCode, sMessage + "\r\n");
                bNeedException = true;
              }
            } else {
              if (ufdMnyMinus.compareTo(ufd0) <= 0) {
                continue;
              }
              sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000211", null, new String[] { sCtCode, sRowNo });

              if (bismustcontrol.booleanValue()) {
                htMust.put(sCtCode, sMessage + "\r\n");
                bMustException = true;
              } else {
                htNeedReply.put(sCtCode, sMessage + "\r\n");
                bNeedException = true;
              }
            }
            break;
          case 4:
            htFullCheck.put(dmdvo.getAttributeValue("pk_ct_manage"), dmdvo);
            break;
          case 5:
            htFullCheck.put(dmdvo.getAttributeValue("pk_ct_manage"), dmdvo);
          }

        }

      }

      if (!htFullCheck.isEmpty()) {
        String[] sFullCheckID = new DMDataVO().getAllStrKeysFromHashtable(htFullCheck);
        DMDataVO[] dmdvos = pbtcdmo.queryFullNumByContractHIDs(sFullCheckID);

        String sContractHID = null;
        for (int i = 0; i < dmdvos.length; i++) {
          sContractHID = dmdvos[i].getAttributeValue("pk_ct_manage").toString();
          dmdvo = (DMDataVO)htFullCheck.get(sContractHID);
          icontroltype = Integer.parseInt(dmdvo.getAttributeValue("controltype").toString());

          bismustcontrol = new UFBoolean(dmdvo.getAttributeValue("ismustcontrol").toString());

          bismustcontrol = bismustcontrol == null ? new UFBoolean(false) : bismustcontrol;

          sCtCode = (String)dmdvo.getAttributeValue("ct_code");
          switch (icontroltype)
          {
          case 4:
            if (new UFDouble(dmdvos[i].getAttributeValue("numminus").toString()).doubleValue() <= 0.0D) {
              continue;
            }
            sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000123", null, new String[] { sCtCode });

            if (bismustcontrol.booleanValue()) {
              htMust.put(sCtCode, sMessage + "\r\n");
              bMustException = true;
            } else {
              htNeedReply.put(sCtCode, sMessage + "\r\n");
              bNeedException = true;
            }
            break;
          case 5:
            if (new UFDouble(dmdvos[i].getAttributeValue("moneyminus").toString()).doubleValue() <= 0.0D) {
              continue;
            }
            sMessage = NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000124", null, new String[] { sCtCode });

            if (bismustcontrol.booleanValue()) {
              htMust.put(sCtCode, sMessage + "\r\n");
              bMustException = true;
            } else {
              htNeedReply.put(sCtCode, sMessage + "\r\n");
              bNeedException = true;
            }

          }

        }

      }

      if (bMustException) {
        StringBuffer sbMust = new StringBuffer();
        Enumeration enumError = htMust.elements();
        while (enumError.hasMoreElements()) {
          sbMust.append(enumError.nextElement().toString());
        }
        throw new BusinessException(sbMust.toString());
      }
      if ((bUserChoose) && (bNeedException)) {
        StringBuffer sbNeedReply = new StringBuffer();
        Enumeration enumAffirm = htNeedReply.elements();
        while (enumAffirm.hasMoreElements()) {
          sbNeedReply.append(enumAffirm.nextElement().toString());
        }
        sbNeedReply.append(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000125"));

        throw new SaveHintException(sbNeedReply.toString());
      }
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
  }

  public void writeBackImprest(CtAllotImprestPoVO[] voaBack)
    throws BusinessException
  {
    try
    {
      if ((voaBack != null) && (voaBack.length > 0))
      {
        IPuToCt_PoToCt po = (IPuToCt_PoToCt)NCLocator.getInstance().lookup(IPuToCt_PoToCt.class.getName());

        if (po != null)
          po.writeBackPrePayMny(voaBack);
      }
    } catch (Exception e) {
      reportException(e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException("合同回写订单预付款时出错! ");
    }
  }

  protected void reportException(Exception e)
  {
    SCMEnv.out(e);
  }
}