package nc.bs.pr.pray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.po.OrderDMO;
import nc.bs.po.OrderPubDMO;
import nc.bs.pp.ask.AskbillDMO;
import nc.bs.pp.ask.AskbillImpl;
import nc.bs.ps.cost.CostanalyseDMO;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pu.pub.PubImpl;
import nc.bs.pub.SystemException;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.pub.para.SysInitBO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.rc.receive.ArriveorderDMO;
import nc.bs.scm.datapower.ScmDps;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scminter.service.InterServBO;
import nc.itf.ic.service.IICPub_InvATPDMO;
import nc.itf.ic.service.IICPub_InvOnHandDMO;
import nc.itf.mr.service.IMRWriteDownBillInfo;
import nc.itf.pr.pray.IPraybill;
import nc.itf.pu.inter.IPuToIc_PraybillImpl;
import nc.itf.pu.inter.IPuToSc_PraybillBO;
import nc.itf.pu.inter.IPuToTO_PuToTO;
import nc.itf.sc.IOrder;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.scm.cenpurchase.ICentralPurRule;
import nc.itf.scm.cenpurchase.IScmPosInv;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.so.service.ISOToPUTO_BillConvertDMO;
import nc.itf.uap.bd.produce.IProduceQry;
import nc.itf.uap.bd.structure.IPsntoPurorgQry;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.bd.b431.BatchlistVO;
import nc.vo.bd.b431.ProduceExAggVO;
import nc.vo.bd.b431.ProduceVO;
import nc.vo.bd.structure.PsntopurorgVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.pp.ask.EffectPriceParaVO;
import nc.vo.pr.pray.AdDayVO;
import nc.vo.pr.pray.BudgetItemVO;
import nc.vo.pr.pray.ImplementHeaderVO;
import nc.vo.pr.pray.ImplementItemVO;
import nc.vo.pr.pray.ImplementVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pr.pray.PriceInfosVO;
import nc.vo.pu.exception.RwtPoToPrException;
import nc.vo.pu.exception.RwtScToPrException;
import nc.vo.pu.pr.PrayPubVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.cenpurchase.CentralResultVO;
import nc.vo.scm.cenpurchase.IsCentralVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.ClassForLimitBody;
import nc.vo.scm.pu.ParaVO21WriteNumTo20;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.query.ConvertQueryCondition;
import nc.vo.scm.puplbillprocess.BillInvokeVO;
import nc.vo.scm.rewrite.ParaRewriteVO;

public class PraybillImpl
  implements IPraybill, IPuToIc_PraybillImpl, IPuToSc_PraybillBO, IPuToTO_PuToTO
{
  public UFBoolean checkBeforeSave(PraybillVO VO)
    throws BusinessException
  {
    boolean b = false;
    try {
      PraybillDMO dmo = new PraybillDMO();

      PraybillHeaderVO headVO = VO.getHeadVO();
      String sUnitCode = headVO.getPk_corp();
      String vBillCode = headVO.getVpraycode();
      String sKey = headVO.getCpraybillid();
      b = dmo.isCodeDuplicate(sUnitCode, vBillCode, sKey);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::checkBeforeSave(PraybillVO) Exception!", e);
    }

    return new UFBoolean(!b);
  }

  public String[] getPurOrgForInv(String[] cmangid)
    throws BusinessException
  {
    String[] s = null;
    try {
      PraybillDMO dmo = new PraybillDMO();

      s = dmo.getPurOrgForInv(cmangid);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::checkBeforeSave(PraybillVO) Exception!", e);
    }

    return s;
  }

  public UFBoolean checkGenBill(String[] keys)
    throws BusinessException
  {
    boolean b = false;
    try {
      PraybillDMO dmo = new PraybillDMO();

      b = dmo.isGenBill(keys);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::checkGenBill(PraybillVO) Exception!", e);
    }

    return new UFBoolean(b);
  }

  private ArrayList checkVo(PraybillVO vo)
    throws Exception
  {
    ArrayList arrSaveVO = new ArrayList();

    PubDMO dmoPuPub = new PubDMO();
    PraybillDMO dmo = new PraybillDMO();

    PraybillItemVO[] bodyVO = vo.getBodyVO();

    int iRowCount = bodyVO.length;
    int bodyLength = 0;
    String[] sCmanids = new String[iRowCount];
    String[] sStoreorganization = new String[iRowCount];
    for (int i = 0; i < iRowCount; i++) {
      sCmanids[i] = bodyVO[i].getCmangid();
      sStoreorganization[i] = bodyVO[i].getPk_reqstoorg();
    }
    Object[][] oIsCanPurchased = (Object[][])null;

    oIsCanPurchased = dmoPuPub.queryArrayValue("bd_invmandoc", "pk_invmandoc", new String[] { "iscanpurchased" }, sCmanids, "bd_invmandoc.dr=0");

    Hashtable hMaterTypes = dmo.getMaterTypes(sCmanids, sStoreorganization);
    for (int i = 0; i < iRowCount; i++) {
      if (oIsCanPurchased != null) {
        if ((oIsCanPurchased[i] == null) || (oIsCanPurchased[i][0] == null) || (oIsCanPurchased[i][0].toString().toUpperCase().equals("N"))) {
          bodyLength++;
          continue;
        }

        if ((hMaterTypes != null) && (hMaterTypes.size() > 0)) {
          Object oTemp = hMaterTypes.get(bodyVO[i].getCmangid() + bodyVO[i].getPk_reqstoorg());
          if ((oTemp != null) && (!oTemp.equals("MR")) && (bodyVO[i].getCupsourcebilltype() != null) && (bodyVO[i].getCupsourcebilltype().equals("30")))
          {
            bodyLength++;
            continue;
          }
        }
      }
      arrSaveVO.add(bodyVO[i]);
    }
    return arrSaveVO;
  }

  private String getSQLForPrayMy(ConditionVO[] conds, String pk_corp, boolean isForHeader)
  {
    String strSQL = " ";

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
    conds = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    StringBuffer from = new StringBuffer(" from ");
    if (isForHeader)
      from.append("po_praybill ");
    else {
      from.append("po_praybill_b ");
    }
    if (isForHeader) {
      from.append("inner JOIN po_praybill_b ON po_praybill_b.cpraybillid = po_praybill.cpraybillid ");
    }
    else {
      from.append("inner JOIN po_praybill ON po_praybill.cpraybillid = po_praybill_b.cpraybillid ");
    }

    StringBuffer where = new StringBuffer(" where ");

    where.append(" po_praybill.dr = 0 and po_praybill_b.dr = 0 ");

    where.append("and po_praybill.ibillstatus = " + BillStatus.AUDITED + " ");

    if (conds != null) {
      String strSql2 = getSQLByConditionVOsForAsk(conds, pk_corp);
      strSql2 = processFirst(strSql2);
      where.append(strSql2);
    }
    where.append(" ");

    String[] arySql = getSourceSql("po_praybill", null, where.toString());

    boolean bExistsClsDoc = arySql[0].indexOf("bd_invcl") >= 0;
    boolean bExistsBasDoc = arySql[0].indexOf("bd_invbasdoc") >= 0;
    boolean bJoinInvClassFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invcl.invclasscode") >= 0) || (strDataPowerSql.indexOf("bd_invcl.invclassname") >= 0));

    bJoinInvClassFlag &= !bExistsClsDoc;

    boolean bJoinInvFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invbasdoc.invcode") >= 0) || (strDataPowerSql.indexOf("bd_invbasdoc.invname") >= 0));

    bJoinInvFlag &= !bExistsBasDoc;

    if (bJoinInvClassFlag) {
      if (!bExistsBasDoc) {
        from.append(" left outer join bd_invbasdoc on po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
      }

      from.append(" left outer join bd_invcl on bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl ");
    }
    else if (bJoinInvFlag) {
      from.append(" left outer join bd_invbasdoc on po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
    }

    boolean bExistsVendorDoc = arySql[0].indexOf("bd_cubasdoc") >= 0;
    boolean bJoinVenFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_cubasdoc.custcode") >= 0) || (strDataPowerSql.indexOf("bd_cubasdoc.custname") >= 0));

    bJoinVenFlag &= !bExistsVendorDoc;
    if (bJoinVenFlag) {
      from.append(" left outer join bd_cubasdoc on po_praybill_b.cvendorbaseid is null or po_praybill_b.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
    }

    if ((strDataPowerSql != null) && (strDataPowerSql.trim().length() > 0)) {
      where.append(" and (" + strDataPowerSql + ") ");
    }

    strSQL = strSQL + from;
    strSQL = strSQL + where;
    return strSQL;
  }

  private String getSQLForPriceAudit(ConditionVO[] conds, String pk_corp, boolean isForHeader)
  {
    String strSQL = " ";

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
    conds = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    StringBuffer from = new StringBuffer(" from ");
    if (isForHeader)
      from.append("po_praybill ");
    else {
      from.append("po_praybill_b ");
    }
    if (isForHeader) {
      from.append("inner JOIN po_praybill_b ON po_praybill_b.cpraybillid = po_praybill.cpraybillid ");
    }
    else {
      from.append("inner JOIN po_praybill ON po_praybill.cpraybillid = po_praybill_b.cpraybillid ");
    }

    StringBuffer where = new StringBuffer(" where ");

    where.append(" po_praybill_b.pk_purcorp='" + pk_corp + "' ");

    where.append("and po_praybill.dr = 0 ");
    where.append("and po_praybill_b.dr = 0 ");

    where.append("and po_praybill.ibillstatus = " + BillStatus.AUDITED + " ");

    where.append("and  (po_praybill_b.npriceauditbill is null or po_praybill_b.npriceauditbill = 0) ");

    if (conds != null) {
      String strSql2 = getSQLByConditionVOsForPriceAudit(conds, pk_corp);
      strSql2 = processFirst(strSql2);
      where.append(strSql2);
    }
    where.append(" ");

    if ((strDataPowerSql != null) && (strDataPowerSql.trim().length() > 0)) {
      where.append(" and (" + strDataPowerSql + ") ");
    }

    String orderby = " order by po_praybill.cpraybillid ";

    boolean bJoinInvClassFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invcl.invclasscode") >= 0) || (strDataPowerSql.indexOf("bd_invcl.invclassname") >= 0));

    boolean bJoinInvFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invbasdoc.invcode") >= 0) || (strDataPowerSql.indexOf("bd_invbasdoc.invname") >= 0));

    if (bJoinInvClassFlag) {
      from.append(" left outer join bd_invbasdoc on po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");

      from.append(" left outer join bd_invcl on bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl ");
    }
    else if (bJoinInvFlag) {
      from.append(" left outer join bd_invbasdoc on po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ");
    }

    boolean bJoinVenFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_cubasdoc.custcode") >= 0) || (strDataPowerSql.indexOf("bd_cubasdoc.custname") >= 0));

    if (bJoinVenFlag) {
      from.append(" left outer join bd_cubasdoc on po_praybill_b.cvendorbaseid is null or po_praybill_b.cvendorbaseid = bd_cubasdoc.pk_cubasdoc ");
    }

    strSQL = strSQL + from;
    strSQL = strSQL + where;
    if (isForHeader) {
      strSQL = strSQL + orderby;
    }
    return strSQL;
  }

  private String getSQLForPriceAuditForBody(ConditionVO[] conds, String pk_corp, boolean isForHeader)
  {
    String strSQL = " ";

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(conds);
    conds = (ConditionVO[])(ConditionVO[])listRet.get(0);

    StringBuffer from = new StringBuffer(" from ");
    if (isForHeader)
      from.append("po_praybill ");
    else {
      from.append("po_praybill_b ");
    }
    if (isForHeader) {
      from.append("inner JOIN po_praybill_b ON po_praybill_b.cpraybillid = po_praybill.cpraybillid ");
    }
    else {
      from.append("inner JOIN po_praybill ON po_praybill.cpraybillid = po_praybill_b.cpraybillid ");
    }

    StringBuffer where = new StringBuffer(" where ");

    where.append(" po_praybill_b.pk_purcorp='" + pk_corp + "' ");

    where.append("and po_praybill.dr = 0 ");
    where.append("and po_praybill_b.dr = 0 ");

    where.append("and po_praybill.ibillstatus = " + BillStatus.AUDITED + " ");

    where.append("and  (po_praybill_b.npriceauditbill is null or po_praybill_b.npriceauditbill = 0) ");

    if (conds != null) {
      String strSql2 = getSQLByConditionVOsForPriceAudit(conds, pk_corp);
      strSql2 = processFirst(strSql2);
      where.append(strSql2);
    }
    where.append(" ");

    String orderby = " order by po_praybill.cpraybillid ";

    strSQL = strSQL + from;
    strSQL = strSQL + where;
    if (isForHeader) {
      strSQL = strSQL + orderby;
    }
    return strSQL;
  }

  private String getSQLByConditionVOsForAsk(ConditionVO[] conditionVO, String unitCode)
  {
    String sHCondition = "";

    String sName = null; String sOpera = null; String sValue = null; String sSQL = null; String sReplace = null;
    boolean isConGenBillType = false;

    for (int i = 0; i < conditionVO.length; i++) {
      if (((conditionVO[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (conditionVO[i].getValue().trim().equalsIgnoreCase("NULL"))) || (conditionVO[i].getValue().trim().indexOf("(select distinct power.resource_data_id") >= 0))
      {
        continue;
      }
      sName = conditionVO[i].getFieldCode().trim();
      sOpera = conditionVO[i].getOperaCode().trim();
      sValue = conditionVO[i].getValue();
      if ((sOpera.equalsIgnoreCase("like")) && (!sName.equals("bd_invcl.invclasscode")))
      {
        sValue = "%" + sValue + "%";
      }sSQL = conditionVO[i].getSQLStr();

      sReplace = "";

      if ((sName.equals("po_praybill.dpraydate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.dpraydate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.vpraycode")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.vpraycode " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.cdeptid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = "po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where pk_corp = '" + unitCode + "' and deptname " + sOpera + "  '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where deptname " + sOpera + "  '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill.cpraypsn")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill.cpraypsn in (select pk_psndoc from bd_psndoc where pk_corp = '" + unitCode + "' and psnname " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " cpraypsn in (select pk_psndoc from bd_psndoc where psnname " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill.coperator")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill.coperator in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("po_praybill.ipraysource")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
      {
        int k = 0;
        if (sValue.trim().equals("MRP"))
          k = 0;
        else if (sValue.trim().equals("MO"))
          k = 1;
        else if (sValue.trim().equals("SCF"))
          k = 2;
        else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000037")))
        {
          k = 3;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000038")))
        {
          k = 4;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000039")))
        {
          k = 5;
        } else if (sValue.trim().equals("DRP"))
          k = 6;
        else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000040")))
        {
          k = 7;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000204")))
        {
          k = 8;
        } else if (sValue.trim().equals("")) {
          k = -1;
        }
        if (k >= 0) {
          sReplace = " po_praybill.ipraysource = " + k;
        }

      }
      else if ((sName.equals("po_praybill.ipraytype")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
      {
        int k = 0;
        if (sValue.trim().equals("外包_代料"))
          k = 0;
        if (sValue.trim().equals("外包_不代料"))
          k = 1;
        if (sValue.trim().equals("采购"))
          k = 2;
        if (sValue.trim().equals("外协"))
          k = 3;
        if (sValue.trim().equals(""))
          k = -1;
        if (k >= 0) {
          sReplace = " po_praybill.ipraytype = " + k;
        }

      }
      else if ((sName.equals("po_praybill.cbiztype")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where (pk_corp = '" + unitCode + "' or pk_corp = '@@@@') and businame " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where businame " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill_b.cpurorganization")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill_b.cpurorganization in (select pk_purorg from bd_purorg where name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("bd_invcl.invclasscode")) && (sValue != null) && (sValue.length() > 0))
      {
        try {
          CostanalyseDMO ddmo = new CostanalyseDMO();
          String[] sClassCode = ddmo.getSubInvClassCode(sValue, sOpera);

          sOpera = "=";
          if ((sClassCode != null) && (sClassCode.length > 0)) {
            sValue = "(";
            for (int j = 0; j < sClassCode.length; j++) {
              if (j < sClassCode.length - 1) {
                sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "' or ";
              }
              else {
                sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "')";
              }
            }
            if ((unitCode != null) && (unitCode.trim().length() > 0)) {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
            }
            else
            {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
            }

          }
          else if ((unitCode != null) && (unitCode.trim().length() > 0)) {
            sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
          }

        }
        catch (Exception e)
        {
          SCMEnv.out("获取存货分类子编码时出错:查询结果可能不正确");
          SCMEnv.out(e);
        }

      }
      else if (sName.equals("bd_invbasdoc.invcode")) {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";

          sReplace = sReplace + "and bd_invbasdoc.invcode ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        } else {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invbasdoc.invcode ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        }

      }
      else if (sName.equals("bd_invbasdoc.invname")) {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";

          sReplace = sReplace + "and bd_invbasdoc.invname ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        } else {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invbasdoc.invname ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        }

      }
      else if ((sName.equals("po_praybill_b.vproducenum")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill_b.vproducenum " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.ddemanddate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill_b.ddemanddate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.dsuggestdate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill_b.dsuggestdate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.cauditpsn")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill.cauditpsn in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("po_praybill.dauditdate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.dauditdate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.cprojectid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and bd_jobmngfil.pk_corp ='" + unitCode + "' and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil  and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill_b.cprojectphaseid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cprojectphaseid in (select pk_jobobjpha from bd_jobobjpha,bd_jobmngfil,bd_jobphase where bd_jobobjpha.pk_jobmngfil = bd_jobobjpha.pk_jobmngfil and bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase and bd_jobmngfil.pk_corp ='" + unitCode + "' and bd_jobphase.jobphasename " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill_b.cprojectphaseid in (select pk_jobobjpha from bd_jobobjpha,bd_jobmngfil,bd_jobphase where bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase ' and bd_jobphase.jobphasename " + sOpera + " '" + sValue + "')";
        }

      }
      else if (sName.indexOf("po_praybill.vdef") >= 0) {
        if (sName.length() >= "po_praybill.vdef".length() + 1) {
          sReplace = "po_praybill.vdef" + sName.substring("po_praybill.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
        }

      }
      else if (sName.indexOf("po_praybill_b.vdef") >= 0) {
        if (sName.length() >= "po_praybill_b.vdef".length() + 1) {
          sReplace = "po_praybill_b.vdef" + sName.substring("po_praybill_b.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
        }

      }
      else if (sName.indexOf("po_praybill_b.vfree") >= 0) {
        if (sName.length() >= "po_praybill_b.vfree".length() + 1) {
          sReplace = "po_praybill_b.vfree" + sName.substring("po_praybill_b.vfree".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
        }

      }
      else if (sName.indexOf("genaskbill") >= 0) {
        if ((sValue != null) && (sValue.equals("是"))) {
          sReplace = "   po_praybill_b.nquotebill > 0 ";
        }
        else if ((sValue != null) && (sValue.equals("否"))) {
          sReplace = " (po_praybill_b.nquotebill is  null or  po_praybill_b.nquotebill = 0) ";
        }
        isConGenBillType = true;
      }

      if ((sReplace != null) && (!sReplace.trim().equals(""))) {
        sHCondition = sHCondition + getReplacedSQL(sSQL, sName, sReplace);
      }
    }
    if (!isConGenBillType) {
      sHCondition = sHCondition + " and (po_praybill_b.nquotebill is  null or  po_praybill_b.nquotebill = 0) ";
    }
    return processFirst(sHCondition);
  }

  private String getSQLByConditionVOsForPriceAudit(ConditionVO[] conditionVO, String unitCode)
  {
    String sHCondition = "";

    String sName = null; String sOpera = null; String sValue = null; String sSQL = null; String sReplace = null;

    for (int i = 0; i < conditionVO.length; i++) {
      sName = conditionVO[i].getFieldCode().trim();
      sOpera = conditionVO[i].getOperaCode().trim();
      sValue = conditionVO[i].getValue();
      if ((sOpera.equalsIgnoreCase("like")) && (!sName.equals("bd_invcl.invclasscode")))
      {
        sValue = "%" + sValue + "%";
      }sSQL = conditionVO[i].getSQLStr();

      sReplace = "";

      if ((sName.equals("po_praybill.dpraydate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.dpraydate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.vpraycode")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.vpraycode " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.cdeptid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = "po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where pk_corp = '" + unitCode + "' and deptname " + sOpera + "  '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where deptname " + sOpera + "  '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill.cpraypsn")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill.cpraypsn in (select pk_psndoc from bd_psndoc where pk_corp = '" + unitCode + "' and psnname " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " cpraypsn in (select pk_psndoc from bd_psndoc where psnname " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill.coperator")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill.coperator in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("po_praybill.ipraysource")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
      {
        int k = 0;
        if (sValue.trim().equals("MRP"))
          k = 0;
        else if (sValue.trim().equals("MO"))
          k = 1;
        else if (sValue.trim().equals("SCF"))
          k = 2;
        else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000037")))
        {
          k = 3;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000038")))
        {
          k = 4;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000039")))
        {
          k = 5;
        } else if (sValue.trim().equals("DRP"))
          k = 6;
        else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000040")))
        {
          k = 7;
        } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000204")))
        {
          k = 8;
        } else if (sValue.trim().equals("")) {
          k = -1;
        }
        if (k >= 0) {
          sReplace = " po_praybill.ipraysource = " + k;
        }

      }
      else if ((sName.equals("po_praybill.ipraytype")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
      {
        int k = 0;
        if (sValue.trim().equals("外包_代料"))
          k = 0;
        if (sValue.trim().equals("外包_不代料"))
          k = 1;
        if (sValue.trim().equals("采购"))
          k = 2;
        if (sValue.trim().equals("外协"))
          k = 3;
        if (sValue.trim().equals(""))
          k = -1;
        if (k >= 0) {
          sReplace = " po_praybill.ipraytype = " + k;
        }

      }
      else if ((sName.equals("po_praybill.cbiztype")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where (pk_corp = '" + unitCode + "' or pk_corp = '@@@@') and businame " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where businame " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill_b.cpurorganization")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill_b.cpurorganization in (select pk_purorg from bd_purorg where name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("bd_invcl.invclasscode")) && (sValue != null) && (sValue.length() > 0))
      {
        try {
          CostanalyseDMO ddmo = new CostanalyseDMO();
          String[] sClassCode = ddmo.getSubInvClassCode(sValue, sOpera);

          sOpera = "=";
          if ((sClassCode != null) && (sClassCode.length > 0)) {
            sValue = "(";
            for (int j = 0; j < sClassCode.length; j++) {
              if (j < sClassCode.length - 1) {
                sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "' or ";
              }
              else {
                sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "')";
              }
            }
            if ((unitCode != null) && (unitCode.trim().length() > 0)) {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
            }
            else
            {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
            }

          }
          else if ((unitCode != null) && (unitCode.trim().length() > 0)) {
            sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
          }

        }
        catch (Exception e)
        {
          SCMEnv.out("获取存货分类子编码时出错:查询结果可能不正确");
          SCMEnv.out(e);
        }

      }
      else if (sName.equals("bd_invbasdoc.invcode")) {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invmandoc.pk_corp = '";
          sReplace = sReplace + unitCode + "' ";
          sReplace = sReplace + "and bd_invbasdoc.invcode ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        } else {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invbasdoc.invcode ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        }

      }
      else if (sName.equals("bd_invbasdoc.invname")) {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invmandoc.pk_corp = '";
          sReplace = sReplace + unitCode + "' ";
          sReplace = sReplace + "and bd_invbasdoc.invname ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        } else {
          sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
          sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
          sReplace = sReplace + "and bd_invbasdoc.invname ";
          sReplace = sReplace + sOpera;
          sReplace = sReplace + "'";
          sReplace = sReplace + sValue;
          sReplace = sReplace + "') ";
        }

      }
      else if ((sName.equals("po_praybill_b.vproducenum")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill_b.vproducenum " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.ddemanddate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill_b.ddemanddate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.dsuggestdate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill_b.dsuggestdate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill.cauditpsn")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = " po_praybill.cauditpsn in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
      }
      else if ((sName.equals("po_praybill.dauditdate")) && (sValue != null) && (sValue.length() > 0))
      {
        sReplace = "po_praybill.dauditdate " + sOpera + " '" + sValue + "'";
      }
      else if ((sName.equals("po_praybill_b.cprojectid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and bd_jobmngfil.pk_corp ='" + unitCode + "' and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil  and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
        }

      }
      else if ((sName.equals("po_praybill_b.cprojectphaseid")) && (sValue != null) && (sValue.length() > 0))
      {
        if (unitCode != null) {
          sReplace = " po_praybill_b.cprojectphaseid in (select pk_jobobjpha from bd_jobobjpha,bd_jobmngfil,bd_jobphase where bd_jobobjpha.pk_jobmngfil = bd_jobobjpha.pk_jobmngfil and bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase and bd_jobmngfil.pk_corp ='" + unitCode + "' and bd_jobphase.jobphasename " + sOpera + " '" + sValue + "')";
        }
        else
        {
          sReplace = " po_praybill_b.cprojectphaseid in (select pk_jobobjpha from bd_jobobjpha,bd_jobmngfil,bd_jobphase where bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase ' and bd_jobphase.jobphasename " + sOpera + " '" + sValue + "')";
        }

      }
      else if (sName.indexOf("po_praybill.vdef") >= 0) {
        if (sName.length() >= "po_praybill.vdef".length() + 1) {
          sReplace = "po_praybill.vdef" + sName.substring("po_praybill.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
        }

      }
      else if (sName.indexOf("po_praybill_b.vdef") >= 0) {
        if (sName.length() >= "po_praybill_b.vdef".length() + 1) {
          sReplace = "po_praybill_b.vdef" + sName.substring("po_praybill_b.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
        }

      }
      else if ((sName.indexOf("po_praybill_b.vfree") >= 0) && 
        (sName.length() >= "po_praybill_b.vfree".length() + 1)) {
        sReplace = "po_praybill_b.vfree" + sName.substring("po_praybill_b.vfree".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
      }

      if ((sReplace != null) && (!sReplace.trim().equals(""))) {
        sHCondition = sHCondition + getReplacedSQL(sSQL, sName, sReplace);
      }
    }
    return processFirst(sHCondition);
  }

  public PraybillHeaderVO[] queryHeadsForAsk(ArrayList listPara)
    throws BusinessException
  {
    String strSQL = null;

    PraybillHeaderVO[] heads = null;

    ConditionVO[] conds = null;
    String sOperator = null;
    String ss = null;
    String strPkCorp = null;
    try
    {
      strPkCorp = (String)listPara.get(0);
      conds = (ConditionVO[])(ConditionVO[])listPara.get(1);

      AskbillDMO dmo = new AskbillDMO();
      strSQL = getSQLForPrayMy(conds, strPkCorp, true);

      ss = ScmDps.getSubSql("po_praybill", "po_praybill", sOperator, new String[] { strPkCorp });

      if ((ss != null) && (ss.trim().length() > 0)) {
        strSQL = strSQL + " and " + ss + " ";
      }
      strSQL = strSQL + " and po_praybill_b.pk_purcorp='" + strPkCorp + "' ";

      strSQL = strSQL + " order by po_praybill.cpraybillid ";
      heads = dmo.queryPraybillHeaderVOsMy(strSQL);
      if (heads == null)
        return null;
      if (heads.length == 0)
        return null;
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return heads;
  }

  public PraybillHeaderVO[] queryHeadsForPriceAudit(ArrayList listPara)
    throws BusinessException
  {
    String strSQL = null;

    PraybillHeaderVO[] heads = null;

    ConditionVO[] condsDef = null;
    ConditionVO[] conds = null;
    String sOperator = null;
    String ss = null;
    String strPkCorp = null;
    try {
      strPkCorp = (String)listPara.get(0);
      conds = (ConditionVO[])(ConditionVO[])listPara.get(1);

      AskbillDMO dmo = new AskbillDMO();
      strSQL = getSQLForPriceAudit(conds, strPkCorp, true);

      ss = ScmDps.getSubSql("po_praybill", "po_praybill", sOperator, new String[] { strPkCorp });

      if ((ss != null) && (ss.trim().length() > 0)) {
        strSQL = strSQL + " and " + ss + " ";
      }
      heads = dmo.queryPraybillHeaderVOsMy(strSQL);
      if (heads == null)
        return null;
      if (heads.length == 0) {
        return null;
      }

      ClassForLimitBody c = new ClassForLimitBody();
      if (c.getIsLimitedByBody().booleanValue()) {
        strSQL = getSQLForPrayMy(condsDef, strPkCorp, false);
        String strSQL1 = strSQL.toLowerCase();
        int i = strSQL1.indexOf("where");
        String strTmp = strSQL.substring(0, i + 5);
        strTmp = strTmp + " po_praybill_b.dr = 0 ";
        strTmp = strTmp + " and " + strSQL.substring(i + 5, strSQL.length());
        strSQL = strTmp;
      } else {
        strSQL = "from po_praybill_b ";
        strSQL = strSQL + "where po_praybill_b.dr = 0 ";
      }

      ss = ScmDps.getSubSql("po_praybill", "po_praybill", sOperator, new String[] { strPkCorp });

      if ((ss != null) && (ss.trim().length() > 0))
        strSQL = strSQL + " and " + ss + " ";
      ss = ScmDps.getSubSql("po_praybill_b", "po_praybill_b", sOperator, new String[] { strPkCorp });

      if ((ss != null) && (ss.trim().length() > 0)) {
        strSQL = strSQL + " and " + ss + " ";
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return heads;
  }

  public PraybillItemVO[] queryBodysForAsk(ArrayList listPara)
    throws BusinessException
  {
    String strSQL = null;
    PraybillVO[] praybills = null;
    PraybillHeaderVO[] heads = null;
    PraybillItemVO[] items = null;
    PraybillItemVO[] itemsTemp = null;
    ConditionVO[] condsDef = null;
    ConditionVO[] conds = null;
    String strPkCorp = null;
    String pk = null;
    Vector vv = new Vector();

    int genType = 0;
    try {
      strPkCorp = (String)listPara.get(0);
      conds = (ConditionVO[])(ConditionVO[])listPara.get(1);
      pk = (String)listPara.get(2);

      AskbillDMO dmo = new AskbillDMO();

      if ((conds != null) && (conds.length > 0)) {
        Vector v = new Vector();
        for (int i = 0; i < conds.length - 1; i++) {
          v.addElement(conds[i]);
        }
        condsDef = new ConditionVO[v.size()];
        v.copyInto(condsDef);

        String sName = null; String sValue = null;

        for (int i = 0; i < conds.length; i++) {
          sName = conds[i].getFieldCode().trim();
          sValue = conds[i].getValue();
          if (sName.indexOf("genaskbill") < 0)
            continue;
          if ((sValue != null) && (sValue.equals("是"))) {
            genType = 1;
          }
          else if ((sValue != null) && (sValue.equals("否")))
            genType = 0;
          else if ((sValue != null) && (sValue.equals("全部"))) {
            genType = 2;
          }

        }

      }

      strSQL = getSQLForPrayMy(conds, strPkCorp, true);
      heads = dmo.queryPraybillHeaderVOsMy(" from po_praybill where po_praybill.dr =0 and  po_praybill.cpraybillid = '" + pk + "' ");

      praybills = new PraybillVO[heads.length];

      ClassForLimitBody c = new ClassForLimitBody();
      if (c.getIsLimitedByBody().booleanValue()) {
        strSQL = getSQLForPrayMy(conds, strPkCorp, false);
        String strSQL1 = strSQL.toLowerCase();
        int i = strSQL1.indexOf("where");
        String strTmp = strSQL.substring(0, i + 5);
        strTmp = strTmp + " po_praybill_b.dr = 0 ";
        strTmp = strTmp + " and " + strSQL.substring(i + 5, strSQL.length());
        strSQL = strTmp;
      } else {
        strSQL = "from po_praybill_b ";
        strSQL = strSQL + "where po_praybill_b.dr = 0 ";
      }
      strSQL = strSQL + "and po_praybill_b.cpraybillid = '" + pk + "' ";

      strSQL = strSQL + " and po_praybill_b.pk_purcorp='" + strPkCorp + "' ";
      praybills = dmo.findItemsForPrayHeaderMyArray(heads, strSQL, genType);
      if ((praybills != null) && (praybills.length > 0)) {
        for (int i = 0; i < praybills.length; i++) {
          itemsTemp = praybills[i].getBodyVO();
          if ((itemsTemp != null) && (itemsTemp.length > 0)) {
            for (int j = 0; j < itemsTemp.length; j++) {
              vv.add(itemsTemp[j]);
            }
          }
        }
      }
      if (vv.size() > 0) {
        items = new PraybillItemVO[vv.size()];
        for (int i = 0; i < items.length; i++) {
          items[i] = new PraybillItemVO();
        }
        vv.copyInto(items);
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    return items;
  }

  public PraybillItemVO[] queryBodysForPriceAudit(ArrayList listPara)
    throws BusinessException
  {
    String strSQL = null;
    PraybillVO[] praybills = null;
    PraybillHeaderVO[] heads = null;
    PraybillItemVO[] items = null;
    PraybillItemVO[] itemsTemp = null;
    ConditionVO[] condsDef = null;
    ConditionVO[] conds = null;
    String strPkCorp = null;
    String pk = null;
    Vector vv = new Vector();
    try {
      strPkCorp = (String)listPara.get(0);
      conds = (ConditionVO[])(ConditionVO[])listPara.get(1);
      pk = (String)listPara.get(2);

      AskbillDMO dmo = new AskbillDMO();

      strSQL = getSQLForPriceAudit(conds, strPkCorp, true);

      heads = dmo.queryPraybillHeaderVOsMy(" from po_praybill where po_praybill.dr =0 and  po_praybill.cpraybillid = '" + pk + "' ");

      praybills = new PraybillVO[heads.length];

      ClassForLimitBody c = new ClassForLimitBody();
      if (c.getIsLimitedByBody().booleanValue()) {
        strSQL = getSQLForPriceAudit(conds, strPkCorp, false);
        String strSQL1 = strSQL.toLowerCase();
        int i = strSQL1.indexOf("where");
        String strTmp = strSQL.substring(0, i + 5);
        strTmp = strTmp + " po_praybill_b.dr = 0 ";
        strTmp = strTmp + " and " + strSQL.substring(i + 5, strSQL.length());
        strSQL = strTmp;
      } else {
        strSQL = "from po_praybill_b ";
        strSQL = strSQL + "where po_praybill_b.dr = 0 ";
      }

      strSQL = strSQL + "and po_praybill_b.cpraybillid = '" + pk + "' and  (po_praybill_b.npriceauditbill is null or po_praybill_b.npriceauditbill = 0) ";

      praybills = dmo.findItemsForPrayHeaderMyArrayForPriceAudit(heads, strSQL);
      if ((praybills != null) && (praybills.length > 0)) {
        for (int i = 0; i < praybills.length; i++) {
          itemsTemp = praybills[i].getBodyVO();
          if ((itemsTemp != null) && (itemsTemp.length > 0)) {
            for (int j = 0; j < itemsTemp.length; j++) {
              vv.add(itemsTemp[j]);
            }
          }
        }
      }
      if (vv.size() > 0) {
        items = new PraybillItemVO[vv.size()];
        for (int i = 0; i < items.length; i++) {
          items[i] = new PraybillItemVO();
        }
        vv.copyInto(items);
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    return items;
  }

  public ArrayList closeBill(PraybillVO vo)
    throws BusinessException, BusinessException
  {
    PraybillVO newVO = new PraybillVO();
    ArrayList array = new ArrayList();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      dmo.closeBill(vo);

      StringBuffer sCondition = new StringBuffer("");
      sCondition.append(" and (po_praybill_b.cpraybillid = '" + ((PraybillHeaderVO)vo.getParentVO()).getCpraybillid() + "' )");

      PraybillHeaderVO head = dmo.queryAllHead(null, sCondition.toString(), null)[0];
      newVO.setParentVO(head);
      PraybillItemVO[] items = (PraybillItemVO[])(PraybillItemVO[])vo.getChildrenVO();

      if ((items != null) && (items.length > 0)) {
        ArrayList listBid = new ArrayList();
        for (int i = 0; i < items.length; i++) {
          listBid.add(items[i].getCpraybill_bid());
        }
        sCondition.append(" and po_praybill_b.cpraybill_bid in " + new TempTableUtil().getSubSql(listBid) + " ");
      }
      items = dmo.queryAllBody(sCondition.toString());

      items = getItemWithMny(items);

      newVO.setChildrenVO(items);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    array.add(newVO);

    return array;
  }

  public Vector computeBudget(PraybillHeaderVO[] headVO, PraybillItemVO[] bodyVO, Vector vBudget, String refPK)
    throws BusinessException
  {
    Vector vResult = new Vector();
    try
    {
      int[] iBillStatus = new int[bodyVO.length];

      String[] saVendor = new String[bodyVO.length];
      for (int i = 0; i < bodyVO.length; i++) {
        String sBody = bodyVO[i].getCpraybillid().trim();
        saVendor[i] = bodyVO[i].getCvendormangid();
        for (int j = 0; j < headVO.length; j++) {
          String sHead = headVO[j].getCpraybillid().trim();
          if (sHead.equals(sBody)) {
            iBillStatus[i] = headVO[j].getIbillstatus().intValue();
            break;
          }
        }
      }

      ArrayList aryRet = getDefaultProtocolKeyArray(saVendor);

      Vector vTemp = (Vector)vBudget.elementAt(0);
      UFDate dBeginPeriod = (UFDate)vTemp.elementAt(0);
      UFDate dEndPeriod = (UFDate)vTemp.elementAt(1);

      UFDate[] dPeriod = null;
      int nBudgetPeriod = ((Integer)vBudget.elementAt(2)).intValue();
      if (nBudgetPeriod == 1)
        dPeriod = getMonthPeriods(dBeginPeriod, dEndPeriod);
      else if (nBudgetPeriod == 2)
        dPeriod = getTendaysPeriods(dBeginPeriod, dEndPeriod);
      else if (nBudgetPeriod == 3) {
        dPeriod = getWeekPeriods(dBeginPeriod, dEndPeriod);
      }

      int nPeriod = -1;
      if ((dPeriod != null) || (dPeriod.length > 0))
        nPeriod = dPeriod.length;
      else {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000394"));
      }

      String[] sProtocolKeys = null;
      HashMap hProtocol = null;
      HashMap hFixDate = null;
      ArrayList arrTemp = new ArrayList();
      if ((aryRet != null) && (aryRet.size() > 0))
      {
        for (int i = 0; i < aryRet.size(); i++) {
          String sTemp = (String)aryRet.get(i);
          if ((sTemp != null) && (sTemp.length() > 0))
            arrTemp.add(sTemp);
        }
        sProtocolKeys = new String[arrTemp.size()];
        arrTemp.toArray(sProtocolKeys);

        hFixDate = queryFixedData(sProtocolKeys);

        hProtocol = queryProtocol(sProtocolKeys);
      }
      HashMap hProtocolFrmQuery = null;
      HashMap hFixDateFrmQuery = null;
      if ((refPK != null) && (refPK.length() > 0))
      {
        hFixDateFrmQuery = queryFixedData(new String[] { refPK });

        hProtocolFrmQuery = queryProtocol(new String[] { refPK });
      }

      for (int i = 0; i < bodyVO.length; i++)
      {
        vTemp = (Vector)vBudget.elementAt(1);
        UFDouble dPrice = null;
        if ((bodyVO[i].getNsuggestprice() != null) && (bodyVO[i].getNsuggestprice().doubleValue() > 0.0D))
        {
          dPrice = bodyVO[i].getNsuggestprice();
        }
        else dPrice = (UFDouble)vTemp.elementAt(i);

        UFDate[] dPayDay = null;

        UFDouble[] dPayMoney = null;

        String sProtocolKey = aryRet == null ? null : (String)aryRet.get(i);

        if ((sProtocolKey != null) && (sProtocolKey.length() > 0))
        {
          ArrayList v0 = (ArrayList)hFixDate.get(sProtocolKey);
          String ss = null;
          if ((v0 != null) && (v0.size() > 0)) {
            ss = (String)v0.get(0);
            ss = ss.toUpperCase();
          }
          if ((ss != null) && (ss.equals("Y")))
          {
            dPayDay = new UFDate[1];
            dPayMoney = new UFDouble[1];
            dPayDay[0] = getFixedSettleDate((String)v0.get(1), bodyVO[i].getDdemanddate());

            if (dPayDay[0] == null) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000395"));
            }

            if (iBillStatus[i] == 1)
            {
              double d = dPrice.doubleValue() * bodyVO[i].getNaccumulatenum().doubleValue();

              dPayMoney[0] = new UFDouble(d);
            } else {
              double d = dPrice.doubleValue() * bodyVO[i].getNpraynum().doubleValue();

              dPayMoney[0] = new UFDouble(d);
            }
          }
          else {
            ArrayList v = (ArrayList)hProtocol.get(sProtocolKey);
            if ((v != null) && (v.size() > 0)) {
              dPayDay = new UFDate[v.size()];
              dPayMoney = new UFDouble[v.size()];
              UFDate[] tempDate = new UFDate[v.size()];
              for (int j = 0; j < v.size(); j++) {
                ArrayList vTemp0 = (ArrayList)v.get(j);
                Integer s = (Integer)vTemp0.get(2);
                dPayDay[j] = bodyVO[i].getDdemanddate().getDateAfter(s.intValue());

                dPayMoney[j] = ((UFDouble)vTemp0.get(3));
                tempDate[j] = dPayDay[j];

                if (iBillStatus[i] == 1)
                {
                  double d = dPayMoney[j].doubleValue() / 100.0D * dPrice.doubleValue() * bodyVO[i].getNaccumulatenum().doubleValue();

                  dPayMoney[j] = new UFDouble(d);
                } else {
                  double d = dPayMoney[j].doubleValue() / 100.0D * dPrice.doubleValue() * bodyVO[i].getNpraynum().doubleValue();

                  dPayMoney[j] = new UFDouble(d);
                }

                Vector vv = queryPreference((String)vTemp0.get(0), (String)vTemp0.get(1));

                if ((vv != null) && (vv.size() > 0)) {
                  for (int k = 0; k < vv.size(); k++) {
                    Vector vvv = (Vector)vv.elementAt(k);
                    Integer nStep = (Integer)vvv.elementAt(0);

                    Integer nDay = (Integer)vvv.elementAt(1);

                    UFDouble dRate = (UFDouble)vvv.elementAt(2);

                    if (nStep.intValue() == 1) {
                      if (j == 0) {
                        dPayDay[j] = bodyVO[i].getDdemanddate().getDateAfter(nDay.intValue());
                      }
                      else
                      {
                        dPayDay[j] = tempDate[(j - 1)].getDateAfter(nDay.intValue());
                      }

                      dPayMoney[j] = new UFDouble(dPayMoney[j].doubleValue() * (1.0D - dRate.doubleValue() / 100.0D));

                      break;
                    }
                  }
                }
              }
            }
          }
        }
        else if ((refPK != null) && (refPK.length() > 0))
        {
          ArrayList v0 = (ArrayList)hFixDateFrmQuery.get(refPK);
          String ss = null;
          if ((v0 != null) && (v0.size() > 0)) {
            ss = (String)v0.get(0);
            ss = ss.toUpperCase();
          }
          if ((ss != null) && (ss.equals("Y")))
          {
            dPayDay = new UFDate[1];
            dPayMoney = new UFDouble[1];
            dPayDay[0] = getFixedSettleDate((String)v0.get(1), bodyVO[i].getDdemanddate());

            if (dPayDay[0] == null) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000395"));
            }

            if (iBillStatus[i] == 1)
            {
              double d = dPrice.doubleValue() * bodyVO[i].getNaccumulatenum().doubleValue();

              dPayMoney[0] = new UFDouble(d);
            } else {
              double d = dPrice.doubleValue() * bodyVO[i].getNpraynum().doubleValue();

              dPayMoney[0] = new UFDouble(d);
            }
          }
          else {
            ArrayList vQuery = (ArrayList)hProtocolFrmQuery.get(refPK);

            dPayDay = new UFDate[vQuery.size()];
            dPayMoney = new UFDouble[vQuery.size()];
            UFDate[] tempDate = new UFDate[vQuery.size()];
            for (int j = 0; j < vQuery.size(); j++) {
              ArrayList vTemp0 = (ArrayList)vQuery.get(j);
              Integer s = (Integer)vTemp0.get(2);
              dPayDay[j] = bodyVO[i].getDdemanddate().getDateAfter(s.intValue());

              dPayMoney[j] = ((UFDouble)vTemp0.get(3));
              tempDate[j] = dPayDay[j];

              if (iBillStatus[i] == 1)
              {
                double d = dPayMoney[j].doubleValue() / 100.0D * dPrice.doubleValue() * bodyVO[i].getNaccumulatenum().doubleValue();

                dPayMoney[j] = new UFDouble(d);
              } else {
                double d = dPayMoney[j].doubleValue() / 100.0D * dPrice.doubleValue() * bodyVO[i].getNpraynum().doubleValue();

                dPayMoney[j] = new UFDouble(d);
              }

              Vector vv = queryPreference((String)vTemp0.get(0), (String)vTemp0.get(1));

              if ((vv != null) && (vv.size() > 0)) {
                for (int k = 0; k < vv.size(); k++) {
                  Vector vvv = (Vector)vv.elementAt(k);
                  Integer nStep = (Integer)vvv.elementAt(0);
                  Integer nDay = (Integer)vvv.elementAt(1);
                  UFDouble dRate = (UFDouble)vvv.elementAt(2);

                  if (nStep.intValue() == 1) {
                    if (j == 0) {
                      dPayDay[j] = bodyVO[i].getDdemanddate().getDateAfter(nDay.intValue());
                    }
                    else
                    {
                      dPayDay[j] = tempDate[(j - 1)].getDateAfter(nDay.intValue());
                    }

                    dPayMoney[j] = new UFDouble(dPayMoney[j].doubleValue() * (1.0D - dRate.doubleValue() / 100.0D));

                    break;
                  }
                }
              }
            }
          }
        }
        else
        {
          dPayDay = new UFDate[1];
          dPayMoney = new UFDouble[1];
          dPayDay[0] = bodyVO[i].getDdemanddate();

          if (iBillStatus[i] == 1)
          {
            double d = dPrice.doubleValue() * bodyVO[i].getNaccumulatenum().doubleValue();

            dPayMoney[0] = new UFDouble(d);
          } else {
            double d = dPrice.doubleValue() * bodyVO[i].getNpraynum().doubleValue();

            dPayMoney[0] = new UFDouble(d);
          }
        }

        UFDouble[] dMoney = new UFDouble[nPeriod];
        UFDate dTemp = dBeginPeriod;
        for (int j = 0; j < nPeriod; j++) {
          double sum = 0.0D;
          if ((dPayDay != null) && (dPayDay.length > 0)) {
            for (int k = 0; k < dPayDay.length; k++) {
              if (j == 0) {
                if ((dPayDay[k].compareTo(dTemp) < 0) || (dPeriod[j].compareTo(dPayDay[k]) < 0))
                  continue;
                sum += dPayMoney[k].doubleValue();
              } else {
                if ((dPayDay[k].compareTo(dTemp) <= 0) || (dPeriod[j].compareTo(dPayDay[k]) < 0))
                  continue;
                sum += dPayMoney[k].doubleValue();
              }
            }
          }
          dMoney[j] = new UFDouble(sum);
          dTemp = dPeriod[j];
        }

        if (i == 0) {
          Vector vName = new Vector();
          for (int j = 0; j < nPeriod; j++) {
            vName.addElement(dPeriod[j]);
          }
          vResult.addElement(vName);
        }
        Vector vData = new Vector();
        for (int j = 0; j < nPeriod; j++) {
          vData.addElement(dMoney[j]);
        }
        vResult.addElement(vData);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.computeBudget", e);
    }

    return vResult;
  }

  public void discardPraybillArr(String[] hIDs, String[] bIDs, String sBilltype, String dDate, String sUserId)
    throws BusinessException
  {
    String sMethodName = "nc.bs.pr.PraybillImpl.discardPraybillArr()";

    if (((hIDs == null) || (hIDs.length == 0)) && ((bIDs == null) || (bIDs.length == 0)))
    {
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000000")));
    }

    try
    {
      PubDMO dmoPuPub = new PubDMO();

      PraybillDMO dmo = new PraybillDMO();
      if (((hIDs == null) || (hIDs.length == 0)) && 
        (bIDs != null) && (bIDs.length > 0)) {
        Object[][] ob = dmoPuPub.queryArrayValue("po_praybill_b", "cupsourcebillrowid", new String[] { "cpraybillid" }, bIDs, "po_praybill_b.dr=0");

        if (ob == null) {
          PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000001")));
        }

        ArrayList arr = new ArrayList();
        String temp = "";
        for (int i = 0; i < bIDs.length; i++) {
          if (!temp.equals(ob[i][0].toString())) {
            arr.add(ob[i][0]);
            temp = ob[i][0].toString();
          }
        }
        if (arr.size() > 0) {
          hIDs = new String[arr.size()];
          arr.toArray(hIDs);
        }
      }

      PraybillVO[] vos = dmo.queryVosByPrimaryKeys(hIDs, sBilltype);
      if ((vos == null) || (vos.length == 0)) {
        return;
      }

      ArrayList arr = new ArrayList();
      StringBuffer sMessage = new StringBuffer();
      PraybillVO vo = null;
      int nBillStatus = 0;
      for (int i = 0; i < vos.length; i++) {
        vo = vos[i];
        vo.getHeadVO().setCuserid(sUserId);
        arr.add(vo);
        nBillStatus = vo.getHeadVO().getIbillstatus().intValue();
        if (nBillStatus == 1)
        {
          String[] value = { vo.getHeadVO().getVpraycode() };

          sMessage.append(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000429", null, value));
        }
        else if (nBillStatus == 2)
        {
          String[] value = { vo.getHeadVO().getVpraycode() };

          sMessage.append(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000430", null, value));
        }
        else if (nBillStatus == 3)
        {
          String[] value = { vo.getHeadVO().getVpraycode() };

          sMessage.append(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000431", null, value));
        }

        nBillStatus = vo.getHeadVO().getDr();
        if (nBillStatus <= 0) {
          continue;
        }
        String[] value = { vo.getHeadVO().getVpraycode() };

        sMessage.append(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000432", null, value));
      }

      if (sMessage.length() > 0) {
        throw new BusinessException(sMessage.toString());
      }
      PraybillVO[] tempVOs = new PraybillVO[arr.size()];
      arr.toArray(tempVOs);

      PfUtilBO beanPfUtil = new PfUtilBO();

      beanPfUtil.processBatch("DISCARD", "20", dDate, tempVOs, null, null);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private void setParaFromInv(PraybillVO voPray, Integer iPraySource)
    throws BusinessException
  {
    Integer intPrayType = null;
    String strBizType = null;
    try
    {
      PraybillHeaderVO head = (PraybillHeaderVO)voPray.getParentVO();
      PraybillItemVO[] items = (PraybillItemVO[])(PraybillItemVO[])voPray.getChildrenVO();

      String pk_corp = head.getPk_corp();
      String beanName = ICentralPurRule.class.getName();
      ICentralPurRule bo = (ICentralPurRule)NCLocator.getInstance().lookup(beanName);
      String beanNameA = IScmPosInv.class.getName();
      IScmPosInv boA = (IScmPosInv)NCLocator.getInstance().lookup(beanNameA);
      IsCentralVO[] para = null;
      IsCentralVO paraTemp = null;
      CentralResultVO[] result = null;
      CentralResultVO resultTemp = null;

      Vector v = new Vector(); Vector v1 = new Vector(); Vector v2 = new Vector();
      String[] cbaseids = null; String[] pk_calbody = null;
      Hashtable h = new Hashtable(); Hashtable hForPrayType = new Hashtable(); Hashtable hForBizType = new Hashtable();

      PubDMO dmo = new PubDMO();
      PraybillDMO prayDmo = new PraybillDMO();

      for (int i = 0; i < items.length; i++) {
        paraTemp = new IsCentralVO();
        paraTemp.setCrowid(new Integer(i).toString());
        paraTemp.setPk_corp(items[i].getPk_reqcorp());
        paraTemp.setPk_invbasdoc(items[i].getCbaseid());
        v.addElement(paraTemp);
        v1.addElement(items[i].getCbaseid());
        v2.addElement(items[i].getPk_reqstoorg());
      }
      if (v1.size() > 0) {
        cbaseids = new String[v1.size()];
        v1.copyInto(cbaseids);
        pk_calbody = new String[v2.size()];
        v2.copyInto(pk_calbody);
      }

      Vector results = new PraybillDMO().queryPrayTypeAndBizTypeBatch(pk_calbody, cbaseids, pk_corp, iPraySource);
      if ((results == null) || (results.size() < 2)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000064"));
      }

      hForPrayType = (Hashtable)results.get(0);
      hForBizType = (Hashtable)results.get(1);

      if (v.size() > 0) {
        para = new IsCentralVO[v.size()];
        v.copyInto(para);
        result = bo.isCentralPur(para);
        if ((result != null) && (result.length > 0)) {
          for (int i = 0; i < result.length; i++) {
            resultTemp = result[i];

            if (resultTemp.getIsCentralPur())
            {
              strBizType = resultTemp.getPk_busitype();
              if (strBizType == null) {
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000064"));
              }

              head.setCbiztype(resultTemp.getPk_busitype());

              head.setIpraytype(new Integer(2));

              items[i].setCemployeeid(boA.getPurchaser(resultTemp.getPk_corp(), items[i].getCbaseid()));

              items[i].setCpurorganization(resultTemp.getPk_purorg());

              items[i].setPk_purcorp(resultTemp.getPk_corp());
            }
            else {
              intPrayType = (Integer)hForPrayType.get(items[i].getCbaseid());
              strBizType = (String)hForBizType.get(items[i].getCbaseid());
              if ((intPrayType == null) || (strBizType == null)) {
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000064"));
              }

              head.setCbiztype(strBizType);
              head.setIpraytype(intPrayType);

              items[i].setCemployeeid(boA.getPurchaser(head.getPk_corp(), items[i].getCbaseid()));

              String[] s = new PraybillDMO().getPurOrgForInv(new String[] { items[i].getCmangid() });
              if ((s == null) || (s.length <= 0)) continue; items[i].setCpurorganization(s[0]);
            }
          }
        }
      }

      v1 = new Vector();
      v2 = new Vector();
      for (int i = 0; i < items.length; i++) {
        if ((items[i].getPk_purcorp() == null) && (items[i].getCpurorganization() != null) && (!v1.contains(items[i].getCpurorganization()))) v1.addElement(items[i].getCpurorganization());
        if (v2.contains(items[i].getCmangid())) continue; v2.addElement(items[i].getCmangid());
      }

      if (v1.size() > 0) {
        String sWhere = " pk_purorg in ('";
        for (int i = 0; i < v1.size(); i++) {
          if (i < v1.size() - 1) sWhere = sWhere + v1.elementAt(i) + "','"; else {
            sWhere = sWhere + v1.elementAt(i) + "')";
          }
        }

        h = dmo.queryHtResultFromAnyTable("bd_purorg", "ownercorp", new String[] { "pk_purorg" }, sWhere);

        if ((h != null) && (h.size() > 0)) {
          for (int i = 0; i < items.length; i++) {
            Object oTemp = h.get(items[i].getCpurorganization());
            if (oTemp != null) {
              Vector vTemp = (Vector)oTemp;
              if ((vTemp != null) && (vTemp.size() > 0)) {
                Object o = vTemp.elementAt(0);
                if (o != null) {
                  Object[] oo = (Object[])(Object[])o;
                  if ((oo == null) || (oo.length <= 0) || (oo[0] == null) || (items[i].getPk_purcorp() != null)) continue; items[i].setPk_purcorp(oo[0].toString());
                }
              }
            }
          }
        }
      }

      if (v2.size() > 0) {
        String sWhere = " pk_invmandoc in ('";
        for (int i = 0; i < v2.size(); i++) {
          if (i < v2.size() - 1) sWhere = sWhere + v2.elementAt(i) + "','"; else {
            sWhere = sWhere + v2.elementAt(i) + "')";
          }
        }

        h = dmo.queryHtResultFromAnyTable("bd_invmandoc", "pk_cumandoc", new String[] { "pk_invmandoc" }, sWhere);

        if ((h != null) && (h.size() > 0)) {
          for (int i = 0; i < items.length; i++) {
            Object oTemp = h.get(items[i].getCmangid());
            if (oTemp != null) {
              Vector vTemp = (Vector)oTemp;
              if ((vTemp != null) && (vTemp.size() > 0)) {
                Object o = vTemp.elementAt(0);
                if (o != null) {
                  Object[] oo = (Object[])(Object[])o;
                  if ((oo == null) || (oo.length <= 0) || (oo[0] == null)) continue; items[i].setCvendormangid(oo[0].toString());
                }
              }
            }
          }
        }

        v1 = new Vector();
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getCvendormangid() == null) || (v1.contains(items[i].getCvendormangid()))) continue; v1.addElement(items[i].getCvendormangid());
        }

        sWhere = " pk_cumandoc in ('";
        for (int i = 0; i < v1.size(); i++) {
          if (i < v1.size() - 1) sWhere = sWhere + v1.elementAt(i) + "','"; else {
            sWhere = sWhere + v1.elementAt(i) + "')";
          }
        }
        if (v1.size() > 0) h = dmo.queryHtResultFromAnyTable("bd_cumandoc", "pk_cubasdoc", new String[] { "pk_cumandoc" }, sWhere);

        if ((h != null) && (h.size() > 0)) {
          for (int i = 0; i < items.length; i++) {
            if ((items[i] == null) || (items[i].getCvendormangid() == null)) {
              continue;
            }
            Object oTemp = h.get(items[i].getCvendormangid());
            if (oTemp != null) {
              Vector vTemp = (Vector)oTemp;
              if ((vTemp != null) && (vTemp.size() > 0)) {
                Object o = vTemp.elementAt(0);
                if (o != null) {
                  Object[] oo = (Object[])(Object[])o;
                  if ((oo == null) || (oo.length <= 0) || (oo[0] == null)) continue; items[i].setCvendorbaseid(oo[0].toString());
                }
              }
            }
          }
        }

      }

      head.setCplannerid(prayDmo.getClerkFromUserID(head.getCplannerid()));
      head.setTaudittime(null);
      head.setTmaketime(new UFDateTime(new Date()).toString());
      head.setTlastmaketime(head.getTmaketime());

      if ((items != null) && (items.length > 0)) {
        for (int i = 0; i < items.length; i++) {
          if ((items[i].getPk_purcorp() != null) && (items[i].getPk_purcorp().trim().length() != 0)) continue; items[i].setPk_purcorp(head.getPk_corp());
        }
      }
      voPray.setChildrenVO(items);
    } catch (Exception e) {
      SCMEnv.out("参数设置失败！");
      PubDMO.throwBusinessException(e);
    }
  }

  public void deleteMRPlanData(PraybillVO[] VOs)
    throws BusinessException
  {
    if ((VOs == null) || (VOs.length == 0)) return;

    Vector vTemp = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      PraybillItemVO[] bodyVO = VOs[i].getBodyVO();
      if ((bodyVO != null) && (bodyVO.length != 0)) {
        for (int j = 0; j < bodyVO.length; j++) {
          if ("422X".equals(bodyVO[j].getCupsourcebilltype())) {
            vTemp.addElement(bodyVO[j].getCpraybill_bid());
          }
        }
      }
    }
    if (vTemp.size() > 0) {
      String[] s = new String[vTemp.size()];
      vTemp.copyInto(s);
      IMRWriteDownBillInfo iinfo = (IMRWriteDownBillInfo)NCLocator.getInstance().lookup(IMRWriteDownBillInfo.class.getName());
      iinfo.deleteDownBillInfoByRowID("20", s);
    }
  }

  public ArrayList doSave(PraybillVO VO)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start("请购单保存doSave操作开始");

    IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
    srv.checkDefDataType(VO);

    ISOToPUTO_BillConvertDMO instance = null;
    ICreateCorpQueryService isCorpEnableSrv = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
    if (isCorpEnableSrv.isEnabled(VO.getHeadVO().getPk_corp(), "SO")) {
      instance = (ISOToPUTO_BillConvertDMO)NCLocator.getInstance().lookup(ISOToPUTO_BillConvertDMO.class.getName());
    }

    PraybillHeaderVO headVO = VO.getHeadVO();
    PraybillItemVO[] bodyVO = VO.getBodyVO();

    ArrayList arrSend = new ArrayList();
    ArrayList arrReturn = new ArrayList();

    String sHeadKey = headVO.getCpraybillid();
    GetSysBillCode codeDmo = null;

    String key = "";
    String vPrayCode = "";
    String[] rowids = new String[bodyVO.length];
    boolean isFrmIc = false;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      if ((sHeadKey == null) || (sHeadKey.length() == 0))
      {
        PubDMO pubDmo = new PubDMO();

        String memo = headVO.getVmemo();
        String temp = null;
        if ((memo != null) && (memo.trim().length() >= 4)) {
          temp = memo.substring(memo.trim().length() - 4);
        }
        if ((temp != null) && (temp.equals("#@$^"))) {
          isFrmIc = true;
          memo = memo.substring(0, memo.trim().length() - 4);
          headVO.setVmemo(memo);

          pubDmo.setDefaultMeas(new PraybillVO[] { VO }, null);
        }

        if ((bodyVO[0].getCupsourcebilltype() != null) && ((bodyVO[0].getCupsourcebilltype().equals("A2")) || (bodyVO[0].getCupsourcebilltype().equals("A1"))))
        {
          pubDmo.setDefaultMeas(new PraybillVO[] { VO }, null);
        }
        timer.addExecutePhase("请购单辅计量单位和辅计量数量setDefaultMeas");

        ArrayList arrSaveVO = checkVo(VO);
        timer.addExecutePhase("检验合法的单据表体checkVo");

        if ((arrSaveVO == null) || (arrSaveVO.size() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000002"));
        }

        if (isPushSave(VO)) {
          BillRowNoDMO.setVORowNoByRule(VO, "20", "crowno");

          timer.addExecutePhase("行号处理setVORowNoByRule");
        }

        codeDmo = new GetSysBillCode();
        if (VO.isNeedCreateNewBillCode())
          vPrayCode = codeDmo.getSysBillNO(VO);
        else {
          vPrayCode = VO.getHeadVO().getVpraycode();
        }
        timer.addExecutePhase("获取单据号 getSysBillNO" + vPrayCode);

        headVO.setVpraycode(vPrayCode);

        headVO.setTmaketime(new UFDateTime(System.currentTimeMillis()).toString());
        headVO.setTlastmaketime(headVO.getTmaketime().toString());
        headVO.setTaudittime(null);

        key = dmo.insertHead(headVO);
        headVO.setCpraybillid(key);

        for (int j = 0; j < bodyVO.length; j++) {
          String sUpsourceBillId = bodyVO[j].getCupsourcebillid();
          String sSourceBillId = bodyVO[j].getCsourcebillid();
          if ((sUpsourceBillId == null) || (sUpsourceBillId.trim().length() <= 0))
            continue;
          if ((sSourceBillId != null) && (sSourceBillId.trim().length() != 0))
            continue;
          bodyVO[j].setCsourcebillid(bodyVO[j].getCupsourcebillid());

          bodyVO[j].setCsourcebillrowid(bodyVO[j].getCupsourcebillrowid());

          bodyVO[j].setCsourcebilltype(bodyVO[j].getCupsourcebilltype());
        }

        if ((instance != null) && (bodyVO[0].getCupsourcebilltype() != null) && (bodyVO[0].getCupsourcebilltype().equals("30")))
        {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[bodyVO.length][5];
          for (int k = 0; k < bodyVO.length; k++) {
            rowinfo[k][0] = bodyVO[k].getCupsourcebillrowid();
            rowinfo[k][2] = bodyVO[k].getNpraynum();
          }

          instance.writeBackArrangeInfo(rowinfo, headVO.getCoperator(), new UFDateTime(headVO.getDpraydate().toString()));
        }

        rowids = dmo.insertBody(bodyVO, key);
        timer.addExecutePhase("插入表体 insertBody");
      }
      else
      {
        if (isPushSave(VO)) {
          BillRowNoDMO.setVORowNoByRule(VO, "20", "crowno");

          timer.addExecutePhase("行号处理 setVORowNoByRule");
        }
        key = (String)headVO.getAttributeValue("cpraybillid");

        new GetSysBillCode().setBillNoWhenModify(VO, VO.getOldVo(), "vpraycode");
        vPrayCode = headVO.getVpraycode();

        if (headVO.getHeadEditStatus() == 2)
        {
          headVO.setCauditpsn(null);
          headVO.setDauditdate(null);

          headVO.setTlastmaketime(new UFDateTime(System.currentTimeMillis()).toString());
          headVO.setTaudittime(null);

          dmo.updateHead(headVO);
          timer.addExecutePhase("更改表头 updateHead");
        }
        Vector vInsert = new Vector();
        Vector vDelete = new Vector();
        Vector vUpdate = new Vector();

        int iRowCount = bodyVO.length;
        for (int i = 0; i < iRowCount; i++) {
          if (bodyVO[i].getBodyEditStatus() == 1)
          {
            vInsert.addElement(bodyVO[i]);
          } else if (bodyVO[i].getBodyEditStatus() == 3)
          {
            vDelete.addElement(bodyVO[i]); } else {
            if (bodyVO[i].getBodyEditStatus() != 2)
              continue;
            vUpdate.addElement(bodyVO[i]);
          }
        }

        PraybillItemVO[] itemVO = null;

        itemVO = new PraybillItemVO[vDelete.size()];
        vDelete.copyInto(itemVO);
        dmo.deleteBody(itemVO);

        PraybillVO voToMr = new PraybillVO();
        voToMr.setParentVO(headVO);
        voToMr.setChildrenVO(itemVO);
        deleteMRPlanData(new PraybillVO[] { voToMr });

        if ((instance != null) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
        {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[itemVO.length][5];
          for (int k = 0; k < itemVO.length; k++) {
            rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
            rowinfo[k][2] = itemVO[k].getNpraynum().multiply(-1.0D);
          }
          instance.writeBackArrangeInfo(rowinfo, null, null);
        }

        itemVO = new PraybillItemVO[vUpdate.size()];
        vUpdate.copyInto(itemVO);
        Hashtable hIsRel = new Hashtable();
        PraybillItemVO[] itemvos = queryBody(key);
        UFDouble num = null;
        if ((itemvos != null) && (itemvos.length > 0) && (itemVO != null) && (itemVO.length > 0))
        {
          for (int i = 0; i < itemvos.length; i++) {
            PraybillItemVO tempItem = itemvos[i];
            for (int j = 0; j < itemVO.length; j++) {
              if (tempItem.getCpraybill_bid().equals(itemVO[j].getCpraybill_bid())) {
                num = itemVO[j].getNpraynum().sub(tempItem.getNpraynum());
              }
              if (num == null) {
                num = new UFDouble(0);
              }
              hIsRel.put(tempItem.getCpraybill_bid(), num);
            }
          }
        }
        dmo.updateBody(itemVO);
        if ((instance != null) && (itemvos != null) && (itemvos.length > 0) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
        {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[itemVO.length][5];
          for (int k = 0; k < itemVO.length; k++) {
            rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
            rowinfo[k][2] = hIsRel.get(itemVO[k].getCpraybill_bid());
          }

          instance.writeBackArrangeInfo(rowinfo, headVO.getCoperator(), new UFDateTime(headVO.getDpraydate().toString()));
        }

        itemVO = new PraybillItemVO[vInsert.size()];
        vInsert.copyInto(itemVO);
        rowids = dmo.insertBody(itemVO, key);
        if ((instance != null) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
        {
          Object[][] rowinfo = (Object[][])null;
          rowinfo = new Object[itemVO.length][5];
          for (int k = 0; k < itemVO.length; k++) {
            rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
            rowinfo[k][2] = itemVO[k].getNpraynum();
          }

          instance.writeBackArrangeInfo(rowinfo, headVO.getCoperator(), new UFDateTime(headVO.getDpraydate().toString()));
        }

        timer.addExecutePhase("更改时的表体处理");
      }

      arrReturn.add(key);

      if ((rowids != null) && (rowids.length > 0)) {
        for (int i = 0; i < rowids.length; i++) {
          arrReturn.add(rowids[i]);
        }
      }

      PraybillVO praybill = queryPrayVoByHid(key);

      if (((bodyVO[0].getCupsourcebilltype() != null) && (bodyVO[0].getCupsourcebilltype().trim().length() > 0)) || (isFrmIc))
      {
        arrReturn.add(new PraybillVO[] { praybill });
      }
      else arrReturn.add(null);

      String tsH = (String)praybill.getParentVO().getAttributeValue("ts");
      String[] ts = null;
      if ((praybill.getBodyVO() != null) && (praybill.getBodyVO().length > 0)) {
        int itemsLength = praybill.getBodyVO().length;
        ts = new String[itemsLength + 1];
        for (int i = 0; i < itemsLength; i++) {
          String tsB = (String)praybill.getBodyVO()[i].getAttributeValue("ts");

          ts[(i + 1)] = (praybill.getBodyVO()[i].getCpraybill_bid() + tsB);
        }
      }
      ts[0] = tsH;
      arrReturn.add(ts);

      arrSend = dmo.queryForSaveAudit(key);
      arrReturn.add(arrSend);

      arrReturn.add(vPrayCode);

      timer.addExecutePhase("queryVoByHid");
      timer.showAllExecutePhase("请购单保存doSave操作结束");
    } catch (BusinessException e) {
      SCMEnv.out(e);
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000003"), e);
    }
    return arrReturn;
  }

  private Hashtable getBatchRule(PraybillItemVO[] itemVOs)
    throws BusinessException
  {
    IProduceQry ccSrv = (IProduceQry)NCLocator.getInstance().lookup(IProduceQry.class.getName());

    ProduceExAggVO[] resultVOs = null;

    Hashtable results = new Hashtable();

    Vector tempBatchInfo = null;

    String pk_corp = null;

    String pk_calbody = null;

    String pk_invmandoc = null;

    String condition = null;

    String batchRule = null;

    UFDouble ecobatch = null;

    ProduceVO produceVO = null;

    String cvendormangid = null;

    BatchlistVO[] batchLists = null;
    try {
      for (int i = 0; i < itemVOs.length; i++) {
        pk_corp = itemVOs[i].getPk_corp();
        pk_calbody = itemVOs[i].getPk_reqstoorg();
        pk_invmandoc = itemVOs[i].getCmangid();
        if ((pk_corp == null) || (pk_corp.trim().length() <= 0) || (pk_calbody == null) || (pk_calbody.trim().length() <= 0) || (pk_invmandoc == null) || (pk_invmandoc.trim().length() <= 0))
        {
          continue;
        }
        condition = "c.pk_invmandoc='" + pk_invmandoc + "' and c.pk_corp = '" + pk_corp + "' And  (batchrule ='B' or  batchrule ='C')";

        resultVOs = ccSrv.queryProduceAggVOsByInvPK(null, pk_calbody, condition);
        produceVO = resultVOs[0].getProduceVO();
        cvendormangid = produceVO.getZgys();
        batchRule = produceVO.getBatchrule();

        if ((batchRule != null) && (batchRule.trim().equalsIgnoreCase("JJ"))) {
          ecobatch = produceVO.getEcobatch();
          tempBatchInfo = new Vector();
          tempBatchInfo.add(batchRule);
          tempBatchInfo.add(cvendormangid);
          tempBatchInfo.add(ecobatch);
          results.put(pk_corp + pk_calbody + pk_invmandoc, tempBatchInfo);
        } else if ((batchRule != null) && (batchRule.trim().equalsIgnoreCase("GD"))) {
          tempBatchInfo = new Vector();
          tempBatchInfo.add(batchRule);
          tempBatchInfo.add(cvendormangid);
          batchLists = produceVO.getBatchlist();
          if ((batchLists != null) && (batchLists.length > 0)) {
            for (int j = 0; j < batchLists.length; j++) {
              tempBatchInfo.add(batchLists[j].getBatchnum());
            }
          }
          if (tempBatchInfo.size() > 0) {
            results.put(pk_corp + pk_calbody + pk_invmandoc, tempBatchInfo);
          }
        }
      }
    }
    catch (BusinessException e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::getBatchRule(PraybillItemVO[]) Exception!", e);
    }

    return results;
  }

  private void computerByBatchRuleFromStock(PraybillVO pvo)
    throws BusinessException
  {
    PraybillItemVO[] itemVOs = pvo.getBodyVO();

    UFDate tmaketime = pvo.getHeadVO().getDpraydate();

    Hashtable batchRules = new Hashtable();
    batchRules = getBatchRule(itemVOs);

    Vector tempBatchInfo = null;

    String pk_corp = null;

    String pk_calbody = null;

    String pk_invmandoc = null;

    String banthRule = null;

    UFDouble ecobatch = null;

    UFDouble praynum = null;
    for (int i = 0; i < itemVOs.length; i++) {
      pk_corp = itemVOs[i].getPk_corp();
      pk_calbody = itemVOs[i].getPk_reqstoorg();
      pk_invmandoc = itemVOs[i].getCmangid();
      praynum = itemVOs[i].getNpraynum();
      if ((pk_corp == null) || (pk_corp.trim().length() <= 0) || (pk_calbody == null) || (pk_calbody.trim().length() <= 0) || (pk_invmandoc == null) || (pk_invmandoc.trim().length() <= 0)) {
        continue;
      }
      tempBatchInfo = new Vector();

      tempBatchInfo = (Vector)batchRules.get(pk_corp + pk_calbody + pk_invmandoc);
      banthRule = (String)tempBatchInfo.get(0);

      if ((banthRule == null) || (banthRule.trim().length() <= 0))
      {
        continue;
      }

      if (banthRule.trim().equalsIgnoreCase("ZJ")) {
        itemVOs[i].setDsuggestdate(tmaketime);
        itemVOs[i].setDdemanddate(new UFDate(tmaketime.toDate().getTime() + getAdvanceDaysForbatchRules(itemVOs[i])));
      }
      else if (banthRule.trim().equalsIgnoreCase("JJ")) {
        ecobatch = (UFDouble)tempBatchInfo.get(2);
        itemVOs[i].setDsuggestdate(tmaketime);
        itemVOs[i].setNpraynum(ecobatch.multiply(praynum.mod(ecobatch).add(new UFDouble(1))));
      }
      else if (banthRule.trim().equalsIgnoreCase("GD")) {
        itemVOs[i].setDsuggestdate(tmaketime);
        itemVOs[i].setDdemanddate(new UFDate(tmaketime.toDate().getTime() + getAdvanceDaysForbatchRules(itemVOs[i])));

        itemVOs[i].setCvendormangid((String)tempBatchInfo.get(1));

        ecobatch = computerConstBatchNum(tempBatchInfo, ecobatch, praynum);
        itemVOs[i].setNpraynum(ecobatch.multiply(praynum.mod(ecobatch).add(new UFDouble(1))));
      }
    }
  }

  private UFDouble computerConstBatchNum(Vector tempBatchInfo, UFDouble ecobatch, UFDouble praynum)
    throws BusinessException
  {
    double[] sortDates = null;
    sortDates = new double[tempBatchInfo.size() - 1];
    for (int k = 2; k < tempBatchInfo.size(); k++) {
      sortDates[(k - 2)] = ((UFDouble)tempBatchInfo.get(k)).toDouble().doubleValue();
    }
    Arrays.sort(sortDates);
    if (praynum.doubleValue() > sortDates[(sortDates.length - 1)])
      ecobatch = new UFDouble(sortDates[(sortDates.length - 1)]);
    else {
      for (int j = 0; j < sortDates.length; j++) {
        if (sortDates[j] > praynum.doubleValue()) {
          continue;
        }
        ecobatch = new UFDouble(sortDates[j]);
        break;
      }
    }

    return ecobatch;
  }

  private void computerByBanthRuleFromMR(PraybillVO pvo)
    throws BusinessException
  {
    PraybillItemVO[] itemVOs = pvo.getBodyVO();

    Hashtable batchRules = new Hashtable();
    batchRules = getBatchRule(itemVOs);

    Vector tempBanthInfo = null;

    String pk_corp = null;

    String pk_calbody = null;

    String pk_invmandoc = null;

    String banthRule = null;

    UFDouble ecobatch = null;

    UFDouble praynum = null;
    for (int i = 0; i < itemVOs.length; i++) {
      pk_corp = itemVOs[i].getPk_reqcorp();
      pk_calbody = itemVOs[i].getPk_reqstoorg();
      pk_invmandoc = itemVOs[i].getCmangid();
      praynum = itemVOs[i].getNpraynum();
      if ((pk_corp == null) || (pk_corp.trim().length() <= 0) || (pk_calbody == null) || (pk_calbody.trim().length() <= 0) || (pk_invmandoc == null) || (pk_invmandoc.trim().length() <= 0)) {
        continue;
      }
      tempBanthInfo = new Vector();

      tempBanthInfo = (Vector)batchRules.get(pk_corp + pk_calbody + pk_invmandoc);
      banthRule = (String)tempBanthInfo.get(0);

      if ((banthRule == null) || (banthRule.trim().length() <= 0))
      {
        continue;
      }

      if (banthRule.trim().equalsIgnoreCase("ZJ"))
      {
        continue;
      }

      if (banthRule.trim().equalsIgnoreCase("JJ")) {
        ecobatch = (UFDouble)tempBanthInfo.get(2);
        itemVOs[i].setNpraynum(ecobatch.multiply(praynum.mod(ecobatch).add(new UFDouble(1))));
      }
      else if (banthRule.trim().equalsIgnoreCase("GD")) {
        ecobatch = computerConstBatchNum(tempBanthInfo, ecobatch, praynum);
        itemVOs[i].setNpraynum(ecobatch.multiply(praynum.mod(ecobatch).add(new UFDouble(1))));
      }
    }
  }

  private int getAdvanceDaysForbatchRules(PraybillItemVO bodyVO)
    throws BusinessException
  {
    Vector v = new Vector();
    try {
      String s0 = bodyVO.getPk_reqcorp();
      String s1 = bodyVO.getPk_reqstoorg();
      String s2 = bodyVO.getCbaseid();
      if ((s1 != null) && (s1.length() > 0) && (s2 != null) && (s2.length() > 0))
        v = queryAdvanceDays(s0, s1, s2);
      else
        return 0;
    } catch (BusinessException e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::getAdvanceDaysForbatchRules(PraybillItemVO) Exception!", e);

      return -1;
    }
    if (v.size() == 0)
      return 0;
    UFDouble dFixedahead = (UFDouble)v.elementAt(0);
    UFDouble dAheadcoff = (UFDouble)v.elementAt(1);
    UFDouble dAheadbatch = (UFDouble)v.elementAt(2);
    UFDouble d = bodyVO.getNpraynum();

    return AdDayVO.getAdDaysArith(d, dFixedahead, dAheadcoff, dAheadbatch);
  }

  public String[] getAssistUnitFlag(PraybillItemVO[] itemVOs)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cBaseID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[itemVOs.length];
    for (int i = 0; i < itemVOs.length; i++) {
      sParam[i] = itemVOs[i].getCbaseid();
    }
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sFlagID = f.getValueS();

    if ((sFlagID != null) && (sFlagID.length > 0))
      return sFlagID;
    return null;
  }

  public UFDouble getATPNum(String pk_corp, String cStoreOrgID, String cMangID, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String hopeDate)
    throws BusinessException
  {
    UFDouble d = null;
    try
    {
      IICPub_InvATPDMO dmo = (IICPub_InvATPDMO)NCLocator.getInstance().lookup("nc.itf.ic.service.IICPub_InvATPDMO");

      d = dmo.getATPNum(pk_corp, cStoreOrgID, cMangID, vfree1, vfree2, vfree3, vfree4, vfree5, null, null, null, null, null, hopeDate);
    }
    catch (BusinessException e)
    {
      SCMEnv.out(e);
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::getATPNum(String,String,String,String,String1~10,String,String) Exception!", e);
    }

    return d;
  }

  public String[] queryBackMultiple(String pk_corp, String pk_calbody, String pk_invbasdoc, String date)
    throws BusinessException
  {
    if ((pk_corp == null) && (date == null))
      return null;
    Vector tempRet = new Vector();
    InterServBO srv = new InterServBO();
    ArrayList listRet = null;
    GeneralSuperVO[] superVOs = null;
    Object pk_invmandoc = null;
    Object ob = null;
    String invmandoc = null;
    String[] retInvmandocs = null;
    try
    {
      listRet = srv.getInterResult("MM", "MM0011", null, "MM0011_queryBackMultiple", new Object[] { pk_corp, null, pk_invbasdoc, date });

      if ((listRet != null) && (listRet.size() > 0)) {
        ob = listRet.get(0);
        if (ob != null) {
          superVOs = (GeneralSuperVO[])(GeneralSuperVO[])listRet.get(0);
        }

        if ((superVOs != null) && (superVOs.length > 0)) {
          for (int i = 0; i < superVOs.length; i++) {
            pk_invmandoc = superVOs[i].getAttributeValue("pk_invmandoc");

            if ((pk_invmandoc == null) || (pk_invmandoc.toString().trim().length() <= 0) || (tempRet.contains(pk_invmandoc))) {
              continue;
            }
            invmandoc = pk_invmandoc.toString().trim();
            tempRet.add(invmandoc);
          }
        }

        if ((tempRet != null) && (tempRet.size() > 0)) {
          retInvmandocs = new String[tempRet.size()];
          tempRet.copyInto(retInvmandocs);
        }
      }
    } catch (BusinessException e) {
      SCMEnv.out(e);
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::queryBackMultiple(String,String,String,String) Exception!", e);
    }

    return retInvmandocs;
  }

  public String[] queryForwardMultiple(String pk_corp, String pk_calbody, String pk_invbasdoc, String date)
    throws BusinessException
  {
    if ((pk_corp == null) && (date == null))
      return null;
    Vector tempRet = new Vector();
    ArrayList listRet = null;
    GeneralSuperVO[] superVOs = null;
    Object pk_invmandoc = null;
    Object ob = null;
    String invmandoc = null;
    String[] retInvmandocs = null;
    try
    {
      listRet = new InterServBO().getInterResult("MM", "MM0011", null, "MM0011_queryForwardMultiple", new Object[] { pk_corp, null, pk_invbasdoc, date });

      if ((listRet != null) && (listRet.size() > 0)) {
        ob = listRet.get(0);
        if (ob != null) {
          superVOs = (GeneralSuperVO[])(GeneralSuperVO[])listRet.get(0);
        }

        if ((superVOs != null) && (superVOs.length > 0)) {
          for (int i = 0; i < superVOs.length; i++) {
            pk_invmandoc = superVOs[i].getAttributeValue("pk_invmandoc");

            if ((pk_invmandoc == null) || (pk_invmandoc.toString().trim().length() <= 0) || (tempRet.contains(pk_invmandoc))) {
              continue;
            }
            invmandoc = pk_invmandoc.toString().trim();
            tempRet.add(invmandoc);
          }
        }

        if ((tempRet != null) && (tempRet.size() > 0)) {
          retInvmandocs = new String[tempRet.size()];
          tempRet.copyInto(retInvmandocs);
        }
      }
    } catch (BusinessException e) {
      SCMEnv.out(e);
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::queryForwardMultiple(String,String,String,String) Exception!", e);
    }

    return retInvmandocs;
  }

  public ArrayList getBudgetPrice(ArrayList aryPara)
    throws BusinessException
  {
    ArrayList aryRslt = new ArrayList();
    if ((aryPara == null) || (aryPara.size() < 4))
      return new ArrayList();
    try {
      String strPrayCorp = (String)aryPara.get(0);
      String[] saCmangid = (String[])(String[])aryPara.get(1);
      String[] saCbaseid = (String[])(String[])aryPara.get(2);
      String[] saReqStoreOrgId = (String[])(String[])aryPara.get(3);
      String[] saPurCorpId = (String[])(String[])aryPara.get(4);

      if ((strPrayCorp == null) || (strPrayCorp.length() == 0))
        return new ArrayList();
      if ((saCmangid == null) || (saCmangid.length == 0))
        return new ArrayList();
      if ((saCbaseid == null) || (saCbaseid.length == 0))
        return new ArrayList();
      if ((saReqStoreOrgId == null) || (saReqStoreOrgId.length == 0))
        return new ArrayList();
      Vector v = null;

      UFDouble[] d = null;

      getPlanCostPrices(aryRslt, strPrayCorp, saCbaseid, saReqStoreOrgId);

      PraybillDMO dmo = new PraybillDMO();
      v = new Vector();
      d = dmo.queryLowPriceArray(saPurCorpId, saCbaseid);

      if ((d != null) && (d.length > 0)) {
        for (int i = 0; i < d.length; i++) {
          if (d[i] == null)
            d[i] = new UFDouble(0);
          v.addElement(d[i]);
        }
      }
      aryRslt.add(v);

      v = new Vector();
      d = dmo.queryNewPriceArray(saPurCorpId, saCbaseid);

      if ((d != null) && (d.length > 0)) {
        for (int i = 0; i < d.length; i++) {
          if (d[i] == null)
            d[i] = new UFDouble(0);
          v.addElement(d[i]);
        }
      }
      aryRslt.add(v);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000004"), e);
    }

    return aryRslt;
  }

  public BudgetItemVO[] getBudgetRefResult(BudgetItemVO[] vo)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,invname,pk_invbasdoc,cBaseid)";

    String sExpress3 = "getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cBaseid)";

    String sExpress4 = "getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cBaseid)";

    String sExpress5 = "getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cBaseid)";

    String sExpress6 = "getColValue(bd_calbody,bodyname,pk_calbody,cStoreorganization)";

    String sExpress7 = "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cMangid)";

    String sExpress8 = "getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptname)";

    String sExpress9 = "getColValue(bd_purorg,name,pk_purorg,pk_purorg)";

    String sExpress10 = "getColValue(bd_corp,unitname,pk_corp,pk_corp)";

    String sExpress11 = "getColValue(bd_stordoc,storname,pk_stordoc,pk_stordoc)";

    f.setExpressArray(new String[] { sExpress1, sExpress2, sExpress3, sExpress4, sExpress5, sExpress6, sExpress7, sExpress8, sExpress9, sExpress10, sExpress11 });

    VarryVO[] varry = f.getVarryArray();

    String[] sParam1 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam1[i] = vo[i].getCbaseid();
    }
    String[] sParam2 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam2[i] = vo[i].getPk_reqstoorg();
    }
    String[] sParam3 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam3[i] = vo[i].getCmangid();
    }
    String[] sParam4 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam4[i] = vo[i].getDeptname();
    }
    String[] sParam5 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam5[i] = vo[i].getCpurorganization();
    }
    String[] sParam6 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam6[i] = vo[i].getPk_reqcorp();
    }
    String[] sParam7 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam7[i] = vo[i].getCwarehouseid();
    }
    f.addVariable(varry[0].getVarry()[0], sParam1);
    f.addVariable(varry[1].getVarry()[0], sParam1);
    f.addVariable(varry[2].getVarry()[0], sParam1);
    f.addVariable(varry[3].getVarry()[0], sParam1);
    f.addVariable(varry[4].getVarry()[0], sParam1);
    f.addVariable(varry[5].getVarry()[0], sParam2);
    f.addVariable(varry[6].getVarry()[0], sParam3);
    f.addVariable(varry[7].getVarry()[0], sParam4);
    f.addVariable(varry[8].getVarry()[0], sParam5);
    f.addVariable(varry[9].getVarry()[0], sParam6);
    f.addVariable(varry[10].getVarry()[0], sParam7);

    String[] sInvCode = f.getValueSArray()[0];
    String[] sInvName = f.getValueSArray()[1];
    String[] sInvSpec = f.getValueSArray()[2];
    String[] sInvType = f.getValueSArray()[3];
    String[] sMeasKey = f.getValueSArray()[4];
    String[] sStore = f.getValueSArray()[5];
    String[] sProdArea = f.getValueSArray()[6];
    String[] sDeptName = f.getValueSArray()[7];
    String[] sPurOrgName = f.getValueSArray()[8];
    String[] sReqCorpName = f.getValueSArray()[9];
    String[] sWareHouseName = f.getValueSArray()[10];

    String sExpress = "getColValue(bd_measdoc,measname,pk_measdoc,sMeasKey)";
    f.setExpress(sExpress);
    VarryVO varry0 = f.getVarry();
    Hashtable table0 = new Hashtable();
    table0.put(varry0.getVarry()[0], sMeasKey);
    f.setDataS(table0);
    String[] sMeasName = f.getValueS();

    for (int i = 0; i < vo.length; i++) {
      vo[i].setCinventorycode(sInvCode[i]);
      vo[i].setCinventoryname(sInvName[i]);
      vo[i].setCinventoryspec(sInvSpec[i]);
      vo[i].setCinventorytype(sInvType[i]);
      vo[i].setCprodarea(sProdArea[i]);
      vo[i].setCinventoryunit(sMeasName[i]);
      vo[i].setPk_reqstoorg(sStore[i]);
      vo[i].setDeptname(sDeptName[i]);
      vo[i].setCpurorganization(sPurOrgName[i]);
      vo[i].setPk_reqstoorg(sReqCorpName[i]);
      vo[i].setCwarehouseid(sWareHouseName[i]);
    }
    return vo;
  }

  public String[] getOutWarehouseid(String pk_corp)
    throws BusinessException
  {
    try
    {
      String[] warehouseids = null;

      PraybillDMO dmo = new PraybillDMO();
      warehouseids = dmo.getOutWarehouseid(pk_corp);
      if (warehouseids == null)
        return null;
      return warehouseids;
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }return null;
  }

  private ArrayList getDefaultProtocolKeyArray(String[] saVendorKey)
    throws BusinessException
  {
    PubDMO dmo = null;
    Hashtable h = null;
    ArrayList aryRet = null;
    try {
      dmo = new PubDMO();
      h = dmo.fetchArrayValue("bd_cumandoc", "pk_payterm", "pk_cumandoc", saVendorKey);

      if ((h != null) && (h.size() > 0)) {
        aryRet = new ArrayList();
        Object oVal = null;
        for (int i = 0; i < saVendorKey.length; i++)
          if ((saVendorKey[i] != null) && (h.containsKey(saVendorKey[i]))) {
            oVal = h.get(saVendorKey[i]);
            aryRet.add(oVal);
          } else {
            aryRet.add(null);
          }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000005"), e);
    }

    return aryRet;
  }

  public PraybillVO[] getDistributedVOs(PraybillVO VO)
    throws Exception
  {
    String sHeadKey = VO.getHeadVO().getCpraybillid();
    if ((sHeadKey != null) && (sHeadKey.trim().length() > 0)) {
      return new PraybillVO[] { VO };
    }

    PraybillItemVO[] bodyVO = VO.getBodyVO();
    PraybillVO[] vos = null;

    if ((bodyVO[0].getCadviseCalbodyId() != null) && (bodyVO[0].getCadviseCalbodyId().trim().length() > 0))
    {
      vos = getDistributedPraybillVOs(VO);
    }
    else {
      if ("422X".equals(bodyVO[0].getCupsourcebilltype()))
      {
        return new PraybillVO[] { VO };
      }

      if ((bodyVO[0].getCupsourcebilltype() != null) && (bodyVO[0].getCupsourcebilltype().trim().length() > 0) && (bodyVO[0].getCupsourcebilltype().equals("5A")))
      {
        return new PraybillVO[] { VO };
      }

      if ((bodyVO[0].getCupsourcebilltype() != null) && (bodyVO[0].getCupsourcebilltype().trim().length() > 0) && ((bodyVO[0].getCupsourcebilltype().equals("A1")) || (bodyVO[0].getCupsourcebilltype().equals("A2"))))
      {
        setParaFromInv(VO, new Integer(0));
        vos = getDistributedPraybillVOsFromMRP(VO);
      }
      else
      {
        setParaFromInv(VO, new Integer(4));
        vos = getDistributedPraybillVOsFromInv(VO);
      }
    }
    return vos;
  }

  private PraybillVO[] getDistributedPraybillVOs(PraybillVO vo)
  {
    Hashtable hVO = new Hashtable();
    ArrayList arrVOs = new ArrayList();

    String sAdvisecalbodyid = null;

    String cBizType = null;
    PraybillHeaderVO headVO = vo.getHeadVO();
    PraybillItemVO[] bodyVOs = vo.getBodyVO();

    for (int i = 0; i < bodyVOs.length; i++) {
      PraybillItemVO bodyVO = bodyVOs[i];
      sAdvisecalbodyid = bodyVO.getCadviseCalbodyId();
      cBizType = headVO.getCbiztype();
      if (!hVO.containsKey(sAdvisecalbodyid + cBizType)) {
        PraybillHeaderVO billHeadVO = new PraybillHeaderVO();
        billHeadVO = (PraybillHeaderVO)headVO.clone();

        billHeadVO.setCbiztype(cBizType);
        ArrayList arr = new ArrayList();
        arr.add(billHeadVO);
        arr.add(bodyVO);
        hVO.put(sAdvisecalbodyid + cBizType, arr);
      } else {
        ArrayList arr = (ArrayList)hVO.get(sAdvisecalbodyid + cBizType);
        arr.add(bodyVO);
      }
    }

    if (hVO.size() > 0) {
      Enumeration elems = hVO.keys();
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        ArrayList arr = (ArrayList)hVO.get(curKey);
        int len = arr.size() - 1;

        PraybillHeaderVO praybillHeadVO = (PraybillHeaderVO)arr.get(0);
        PraybillItemVO[] itemVOs = new PraybillItemVO[len];
        arr.remove(0);
        arr.toArray(itemVOs);

        PraybillVO praybillVO = new PraybillVO();
        praybillVO.setParentVO(praybillHeadVO);
        praybillVO.setChildrenVO(itemVOs);
        arrVOs.add(praybillVO);
      }
    }
    PraybillVO[] praybillVOs = null;
    if (arrVOs.size() > 0) {
      praybillVOs = new PraybillVO[arrVOs.size()];
      arrVOs.toArray(praybillVOs);
    }
    return praybillVOs;
  }

  private PraybillVO[] getDistributedPraybillVOsFromInv(PraybillVO vo)
  {
    Hashtable hVO = new Hashtable();
    ArrayList arrVOs = new ArrayList();

    String cBizType = null;
    Integer iPrayType = null;
    PraybillHeaderVO headVO = vo.getHeadVO();
    PraybillItemVO[] bodyVOs = vo.getBodyVO();

    for (int i = 0; i < bodyVOs.length; i++) {
      PraybillItemVO bodyVO = bodyVOs[i];
      cBizType = headVO.getCbiztype();
      iPrayType = headVO.getIpraytype();
      if (!hVO.containsKey(cBizType)) {
        PraybillHeaderVO billHeadVO = new PraybillHeaderVO();
        billHeadVO = (PraybillHeaderVO)headVO.clone();

        billHeadVO.setCbiztype(cBizType);
        billHeadVO.setIpraytype(iPrayType);
        ArrayList arr = new ArrayList();
        arr.add(billHeadVO);
        arr.add(bodyVO);
        hVO.put(cBizType, arr);
      } else {
        ArrayList arr = (ArrayList)hVO.get(cBizType);
        arr.add(bodyVO);
      }
    }

    if (hVO.size() > 0) {
      Enumeration elems = hVO.keys();
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        ArrayList arr = (ArrayList)hVO.get(curKey);
        int len = arr.size() - 1;

        PraybillHeaderVO praybillHeadVO = (PraybillHeaderVO)arr.get(0);
        PraybillItemVO[] itemVOs = new PraybillItemVO[len];
        arr.remove(0);
        arr.toArray(itemVOs);

        PraybillVO praybillVO = new PraybillVO();
        praybillVO.setParentVO(praybillHeadVO);
        praybillVO.setChildrenVO(itemVOs);
        arrVOs.add(praybillVO);
      }
    }
    PraybillVO[] praybillVOs = null;
    if (arrVOs.size() > 0) {
      praybillVOs = new PraybillVO[arrVOs.size()];
      arrVOs.toArray(praybillVOs);
    }
    return praybillVOs;
  }

  private PraybillVO[] getDistributedPraybillVOsFromMRP(PraybillVO vo)
  {
    Hashtable hVO = new Hashtable();
    ArrayList arrVOs = new ArrayList();

    String cBizType = null;
    Integer iPrayType = null;
    PraybillHeaderVO headVO = vo.getHeadVO();
    PraybillItemVO[] bodyVOs = vo.getBodyVO();

    for (int i = 0; i < bodyVOs.length; i++) {
      PraybillItemVO bodyVO = bodyVOs[i];
      cBizType = headVO.getCbiztype();
      iPrayType = headVO.getIpraytype();
      if (!hVO.containsKey(cBizType)) {
        PraybillHeaderVO billHeadVO = new PraybillHeaderVO();
        billHeadVO = (PraybillHeaderVO)headVO.clone();

        billHeadVO.setCbiztype(cBizType);
        billHeadVO.setIpraytype(iPrayType);
        ArrayList arr = new ArrayList();
        arr.add(billHeadVO);
        arr.add(bodyVO);
        hVO.put(cBizType, arr);
      } else {
        ArrayList arr = (ArrayList)hVO.get(cBizType);
        arr.add(bodyVO);
      }
    }

    if (hVO.size() > 0) {
      Enumeration elems = hVO.keys();
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        ArrayList arr = (ArrayList)hVO.get(curKey);
        int len = arr.size() - 1;

        PraybillHeaderVO praybillHeadVO = (PraybillHeaderVO)arr.get(0);
        PraybillItemVO[] itemVOs = new PraybillItemVO[len];
        arr.remove(0);
        arr.toArray(itemVOs);

        PraybillVO praybillVO = new PraybillVO();
        praybillVO.setParentVO(praybillHeadVO);
        praybillVO.setChildrenVO(itemVOs);
        arrVOs.add(praybillVO);
      }
    }
    PraybillVO[] praybillVOs = null;
    if (arrVOs.size() > 0) {
      praybillVOs = new PraybillVO[arrVOs.size()];
      arrVOs.toArray(praybillVOs);
    }
    return praybillVOs;
  }

  private UFDate getFixedSettleDate(String sFixedDay, UFDate dDemandDate)
  {
    int nFixedDay = Integer.parseInt(sFixedDay.trim());
    if ((nFixedDay > 31) || (nFixedDay < 1)) {
      return null;
    }
    int nYear = dDemandDate.getYear();
    int nMonth = dDemandDate.getMonth();
    int nDay = dDemandDate.getDay();

    if (nDay > nFixedDay) {
      nMonth++;
      if (nMonth > 12) {
        nYear++;
        nMonth = 1;
      }
    }

    int nMonthDay = UFDate.getDaysMonth(nYear, nMonth);
    if (nFixedDay > nMonthDay)
      nDay = nMonthDay;
    else {
      nDay = nFixedDay;
    }
    String s = nYear + "-" + nMonth + "-" + nDay;
    return new UFDate(s);
  }

  public ImplementItemVO[] getImplementDRefResult(ImplementItemVO[] vo)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,invname,pk_invbasdoc,cBaseid)";

    String sExpress3 = "getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cBaseid)";

    String sExpress4 = "getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cBaseid)";

    String sExpress5 = "getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cBaseid)";

    String sExpress6 = "getColValue(bd_cubasdoc,custname,pk_cubasdoc,cVendorBaseID)";

    String sExpress7 = "getColValue(bd_cubasdoc,custname,pk_cubasdoc,cVendorBaseID)";

    String sExpress8 = "getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cProjectID)";

    String sExpress9 = "getColValue(bd_jobobjpha,pk_jobphase,pk_jobobjpha,cProjectphaseID)";

    String sExpress10 = "getColValue(bd_stordoc,storname,pk_stordoc,cWarehouseID)";

    String sExpress11 = "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cMangid)";

    f.setExpressArray(new String[] { sExpress1, sExpress2, sExpress3, sExpress4, sExpress5, sExpress6, sExpress7, sExpress8, sExpress9, sExpress10, sExpress11 });

    VarryVO[] varry = f.getVarryArray();

    String[] sParam1 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam1[i] = vo[i].getCbaseid();
    String[] sParam2 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam2[i] = vo[i].getCvendorbaseid_o();
    String[] sParam3 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam3[i] = vo[i].getCvendorbaseid_p();
    String[] sParam4 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam4[i] = vo[i].getCprojectid();
    String[] sParam5 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam5[i] = vo[i].getCprojectphaseid();
    String[] sParam6 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam6[i] = vo[i].getCwarehouseid();
    String[] sParam7 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam7[i] = vo[i].getCmangid();
    }
    f.addVariable(varry[0].getVarry()[0], sParam1);
    f.addVariable(varry[1].getVarry()[0], sParam1);
    f.addVariable(varry[2].getVarry()[0], sParam1);
    f.addVariable(varry[3].getVarry()[0], sParam1);
    f.addVariable(varry[4].getVarry()[0], sParam1);
    f.addVariable(varry[5].getVarry()[0], sParam2);
    f.addVariable(varry[6].getVarry()[0], sParam3);
    f.addVariable(varry[7].getVarry()[0], sParam4);
    f.addVariable(varry[8].getVarry()[0], sParam5);
    f.addVariable(varry[9].getVarry()[0], sParam6);
    f.addVariable(varry[10].getVarry()[0], sParam7);

    String[] sInvCode = f.getValueSArray()[0];
    String[] sInvName = f.getValueSArray()[1];
    String[] sInvSpec = f.getValueSArray()[2];
    String[] sInvType = f.getValueSArray()[3];
    String[] sMeasKey = f.getValueSArray()[4];
    String[] sVendorName = f.getValueSArray()[5];
    String[] sSuggestVendorName = f.getValueSArray()[6];
    String[] sProjectBaseID = f.getValueSArray()[7];
    String[] sProjectPhaseName = f.getValueSArray()[8];
    String[] sWarehouseName = f.getValueSArray()[9];
    String[] sProdArea = f.getValueSArray()[10];

    String sExpress = "getColValue(bd_measdoc,measname,pk_measdoc,sMeasKey)";
    f.setExpress(sExpress);
    VarryVO varry0 = f.getVarry();
    Hashtable table0 = new Hashtable();
    table0.put(varry0.getVarry()[0], sMeasKey);
    f.setDataS(table0);
    String[] sMeasName = f.getValueS();

    sExpress = "getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cProjectID)";
    f.setExpress(sExpress);
    varry0 = f.getVarry();
    table0.put(varry0.getVarry()[0], sProjectBaseID);
    f.setDataS(table0);
    String[] sProjectName = f.getValueS();

    sExpress = "getColValue(bd_jobphase,jobphasename,pk_jobphase,cProjectphaseID)";
    f.setExpress(sExpress);
    varry0 = f.getVarry();
    table0.put(varry0.getVarry()[0], sProjectPhaseName);
    f.setDataS(table0);
    sProjectPhaseName = f.getValueS();

    for (int i = 0; i < vo.length; i++) {
      vo[i].setCinventorycode(sInvCode[i]);
      vo[i].setCinventoryname(sInvName[i]);
      vo[i].setCinventoryspec(sInvSpec[i]);
      vo[i].setCinventorytype(sInvType[i]);
      vo[i].setCinventoryunit(sMeasName[i]);
      vo[i].setCprodarea(sProdArea[i]);
      vo[i].setCvendormangid_o(sVendorName[i]);
      vo[i].setCvendormangid_p(sSuggestVendorName[i]);
      vo[i].setCprojectid(sProjectName[i]);
      vo[i].setCprojectphaseid(sProjectPhaseName[i]);
      vo[i].setCwarehouseid(sWarehouseName[i]);
    }
    return vo;
  }

  public ImplementItemVO[] getImplementTRefResult(ImplementItemVO[] vo)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,invname,pk_invbasdoc,cBaseid)";

    String sExpress3 = "getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cBaseid)";

    String sExpress4 = "getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cBaseid)";

    String sExpress5 = "getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cBaseid)";

    String sExpress6 = "getColValue(bd_cubasdoc,custname,pk_cubasdoc,cVendorBaseID)";

    String sExpress7 = "getColValue(bd_cubasdoc,custname,pk_cubasdoc,cVendorBaseID)";

    String sExpress8 = "getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cProjectID)";

    String sExpress9 = "getColValue(bd_jobobjpha,pk_jobphase,pk_jobobjpha,cProjectphaseID)";

    String sExpress10 = "getColValue(bd_stordoc,storname,pk_stordoc,cWarehouseID)";

    String sExpress11 = "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cMangid)";

    f.setExpressArray(new String[] { sExpress1, sExpress2, sExpress3, sExpress4, sExpress5, sExpress6, sExpress7, sExpress8, sExpress9, sExpress10, sExpress11 });

    VarryVO[] varry = f.getVarryArray();

    String[] sParam1 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam1[i] = vo[i].getCbaseid();
    String[] sParam2 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam2[i] = vo[i].getCvendorbaseid_o();
    String[] sParam3 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam3[i] = vo[i].getCvendorbaseid_p();
    String[] sParam4 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam4[i] = vo[i].getCprojectid();
    String[] sParam5 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam5[i] = vo[i].getCprojectphaseid();
    String[] sParam6 = new String[vo.length];
    for (int i = 0; i < vo.length; i++)
      sParam6[i] = vo[i].getCwarehouseid();
    String[] sParam7 = new String[vo.length];
    for (int i = 0; i < vo.length; i++) {
      sParam7[i] = vo[i].getCmangid();
    }
    f.addVariable(varry[0].getVarry()[0], sParam1);
    f.addVariable(varry[1].getVarry()[0], sParam1);
    f.addVariable(varry[2].getVarry()[0], sParam1);
    f.addVariable(varry[3].getVarry()[0], sParam1);
    f.addVariable(varry[4].getVarry()[0], sParam1);
    f.addVariable(varry[5].getVarry()[0], sParam2);
    f.addVariable(varry[6].getVarry()[0], sParam3);
    f.addVariable(varry[7].getVarry()[0], sParam4);
    f.addVariable(varry[8].getVarry()[0], sParam5);
    f.addVariable(varry[9].getVarry()[0], sParam6);
    f.addVariable(varry[10].getVarry()[0], sParam7);

    String[] sInvCode = f.getValueSArray()[0];
    String[] sInvName = f.getValueSArray()[1];
    String[] sInvSpec = f.getValueSArray()[2];
    String[] sInvType = f.getValueSArray()[3];
    String[] sMeasKey = f.getValueSArray()[4];
    String[] sVendorName = f.getValueSArray()[5];
    String[] sSuggestVendorName = f.getValueSArray()[6];
    String[] sProjectBaseID = f.getValueSArray()[7];
    String[] sProjectPhaseName = f.getValueSArray()[8];
    String[] sWarehouseName = f.getValueSArray()[9];
    String[] sProdArea = f.getValueSArray()[10];

    String sExpress = "getColValue(bd_measdoc,measname,pk_measdoc,sMeasKey)";
    f.setExpress(sExpress);
    VarryVO varry0 = f.getVarry();
    Hashtable table0 = new Hashtable();
    table0.put(varry0.getVarry()[0], sMeasKey);
    f.setDataS(table0);
    String[] sMeasName = f.getValueS();

    sExpress = "getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cProjectID)";
    f.setExpress(sExpress);
    varry0 = f.getVarry();
    table0.put(varry0.getVarry()[0], sProjectBaseID);
    f.setDataS(table0);
    String[] sProjectName = f.getValueS();

    sExpress = "getColValue(bd_jobphase,jobphasename,pk_jobphase,cProjectphaseID)";
    f.setExpress(sExpress);
    varry0 = f.getVarry();
    table0.put(varry0.getVarry()[0], sProjectPhaseName);
    f.setDataS(table0);
    sProjectPhaseName = f.getValueS();

    for (int i = 0; i < vo.length; i++) {
      vo[i].setCinventorycode(sInvCode[i]);
      vo[i].setCinventoryname(sInvName[i]);
      vo[i].setCinventoryspec(sInvSpec[i]);
      vo[i].setCinventorytype(sInvType[i]);
      vo[i].setCinventoryunit(sMeasName[i]);
      vo[i].setCprodarea(sProdArea[i]);
      vo[i].setCvendormangid_o(sVendorName[i]);
      vo[i].setCvendormangid_p(sSuggestVendorName[i]);
      vo[i].setCprojectid(sProjectName[i]);
      vo[i].setCprojectphaseid(sProjectPhaseName[i]);
      vo[i].setCwarehouseid(sWarehouseName[i]);
    }
    return vo;
  }

  private PraybillItemVO[] getItemWithMny(PraybillItemVO[] item)
    throws Exception
  {
    PraybillItemVO[] tempBodyVO = item;
    if ((item == null) || (item.length <= 0))
      return null;
    try
    {
      Vector vCbaseids = new Vector();
      String[] arrStrCbaseids = null;
      String strCbaseid = null;
      Vector vCassids = new Vector();
      String[] arrStrCassids = null;
      String strCassid = null;
      Vector vKeys = new Vector();
      ArrayList aryConvertIsfixed = null;
      Hashtable m_hConvert = new Hashtable();
      Hashtable m_hIsfixed = new Hashtable();
      String strKey = null;
      for (int i = 0; i < item.length; i++) {
        strKey = "";
        strCbaseid = (String)item[i].getAttributeValue("cbaseid");
        strKey = strKey + strCbaseid;
        strCassid = (String)item[i].getAttributeValue("cassistunit");

        if ((strCassid != null) && (!strCassid.trim().equals(""))) {
          strKey = strKey + strCassid;
          if (!vKeys.contains(strKey)) {
            vKeys.addElement(strKey);
            vCbaseids.addElement(strCbaseid);
            vCassids.addElement(strCassid);
          }
        }
      }
      ArriveorderDMO dmo = null;
      if (vKeys.size() > 0) {
        dmo = new ArriveorderDMO();
        arrStrCbaseids = new String[vKeys.size()];
        arrStrCassids = new String[vKeys.size()];
        vCbaseids.copyInto(arrStrCbaseids);
        vCassids.copyInto(arrStrCassids);
        try {
          aryConvertIsfixed = dmo.getConvertIsfixedFlags(arrStrCbaseids, arrStrCassids);

          for (int i = 0; i < vKeys.size(); i++) {
            if ((aryConvertIsfixed != null) && (aryConvertIsfixed.get(i) != null) && (((Object[])(Object[])aryConvertIsfixed.get(i))[0] != null))
            {
              m_hConvert.put(vKeys.elementAt(i), ((Object[])(Object[])aryConvertIsfixed.get(i))[0]);
            }
            else
            {
              m_hConvert.put(vKeys.elementAt(i), new UFDouble(1));
            }
            if ((aryConvertIsfixed != null) && (aryConvertIsfixed.get(i) != null) && (((Object[])(Object[])aryConvertIsfixed.get(i))[1] != null))
            {
              m_hIsfixed.put(vKeys.elementAt(i), ((Object[])(Object[])aryConvertIsfixed.get(i))[1]);
            }
            else
            {
              m_hIsfixed.put(vKeys.elementAt(i), new UFBoolean(true));
            }
          }
        }
        catch (Exception ex) {
          SCMEnv.out(ex);
          PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000006"), ex);
        }

      }

      UFDouble d1 = null;
      UFDouble d3 = null;
      for (int i = 0; i < tempBodyVO.length; i++) {
        if ((m_hConvert != null) && (m_hConvert.size() > 0)) {
          strKey = "";
          strCbaseid = (String)item[i].getAttributeValue("cbaseid");
          strKey = strKey + strCbaseid;
          strCassid = (String)item[i].getAttributeValue("cassistunit");
          if ((strCassid != null) && (!strCassid.trim().equals(""))) {
            strKey = strKey + strCassid;
            if (m_hConvert.containsKey(strKey))
            {
              if (((UFBoolean)m_hIsfixed.get(strKey)).booleanValue()) {
                tempBodyVO[i].setNexchangerate((UFDouble)m_hConvert.get(strKey));
              }
              else
              {
                d1 = tempBodyVO[i].getNpraynum();
                d3 = tempBodyVO[i].getNassistnum();
                if ((d3 != null) && (d3.doubleValue() != 0.0D)) {
                  tempBodyVO[i].setNexchangerate(d1.div(d3));
                }
                else {
                  tempBodyVO[i].setNexchangerate(null);
                }
              }
            }
            else {
              tempBodyVO[i].setNexchangerate(new UFDouble(1));
            }

          }

        }

      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return tempBodyVO;
  }

  private UFDouble[] getLowLocalPricesForPr(String[] saBaseId, String[] saPurCorpId)
    throws BusinessException
  {
    UFDouble[] lowPrice = null;
    try {
      OrderDMO orderDMO = new OrderDMO();
      lowPrice = orderDMO.getLowLocalPricesForPr(saBaseId, saPurCorpId);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getLowLocalPricesForPr(String [])", e);
    }

    return lowPrice;
  }

  private UFDate[] getMonthPeriods(UFDate dBegin, UFDate dEnd)
  {
    if ((dBegin == null) || (dBegin.toString().length() == 0))
      return null;
    if ((dEnd == null) || (dEnd.toString().length() == 0)) {
      return null;
    }
    Vector v = new Vector();

    int nBYear = dBegin.getYear();
    int nEYear = dEnd.getYear();
    int nBMonth = dBegin.getMonth();
    int nEMonth = dEnd.getMonth();

    if (nEYear - nBYear == 0)
    {
      int nYear = nBYear;
      int nMonth = nBMonth;
      int nDay = UFDate.getDaysMonth(nYear, nMonth);
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      for (int i = nBMonth + 1; i <= nEMonth - 1; i++) {
        nYear = nBYear;
        nMonth = i;
        nDay = UFDate.getDaysMonth(nYear, nMonth);
        s = nYear + "-" + nMonth + "-" + nDay;
        v.addElement(new UFDate(s));
      }

      nYear = nBYear;
      nMonth = nEMonth;
      nDay = dEnd.getDay();
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }
    else if (nEYear - nBYear == 1)
    {
      int nYear = nBYear;
      int nMonth = nBMonth;
      int nDay = UFDate.getDaysMonth(nYear, nMonth);
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      for (int i = nBMonth + 1; i <= 12; i++) {
        nYear = nBYear;
        nMonth = i;
        nDay = UFDate.getDaysMonth(nYear, nMonth);
        s = nYear + "-" + nMonth + "-" + nDay;
        v.addElement(new UFDate(s));
      }

      for (int i = 1; i <= nEMonth - 1; i++) {
        nYear = nEYear;
        nMonth = i;
        nDay = UFDate.getDaysMonth(nYear, nMonth);
        s = nYear + "-" + nMonth + "-" + nDay;
        v.addElement(new UFDate(s));
      }

      nYear = nEYear;
      nMonth = nEMonth;
      nDay = dEnd.getDay();
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }
    else
    {
      int nYear = nBYear;
      int nMonth = nBMonth;
      int nDay = UFDate.getDaysMonth(nYear, nMonth);
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      for (int i = nBMonth + 1; i <= 12; i++) {
        nYear = nBYear;
        nMonth = i;
        nDay = UFDate.getDaysMonth(nYear, nMonth);
        s = nYear + "-" + nMonth + "-" + nDay;
        v.addElement(new UFDate(s));
      }

      for (int i = nBYear + 1; i <= nEYear - 1; i++) {
        for (int j = 1; j <= 12; j++) {
          nYear = i;
          nMonth = j;
          nDay = UFDate.getDaysMonth(nYear, nMonth);
          s = nYear + "-" + nMonth + "-" + nDay;
          v.addElement(new UFDate(s));
        }

      }

      for (int i = 1; i <= nEMonth - 1; i++) {
        nYear = nEYear;
        nMonth = i;
        nDay = UFDate.getDaysMonth(nYear, nMonth);
        s = nYear + "-" + nMonth + "-" + nDay;
        v.addElement(new UFDate(s));
      }

      nYear = nEYear;
      nMonth = nEMonth;
      nDay = dEnd.getDay();
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }

    if (v.size() > 0)
    {
      Vector vv = new Vector();
      for (int i = 0; i < v.size() - 1; i++) {
        boolean b = false;
        UFDate d1 = (UFDate)v.elementAt(i);
        int nMonth1 = d1.getMonth();
        int nYear1 = d1.getYear();
        for (int j = i + 1; j < v.size(); j++) {
          UFDate d2 = (UFDate)v.elementAt(j);
          int nMonth2 = d2.getMonth();
          int nYear2 = d2.getYear();
          if ((nYear1 == nYear2) && (nMonth1 == nMonth2)) {
            b = true;
            break;
          }
        }
        if (!b)
          vv.addElement(d1);
      }
      vv.addElement(v.elementAt(v.size() - 1));

      if (vv.size() > 0) {
        UFDate[] d = new UFDate[vv.size()];
        vv.copyInto(d);
        return d;
      }
      return null;
    }
    return null;
  }

  private UFDouble[] getNewLocalPricesForPr(String[] saBaseId, String[] saPurCorpId)
    throws BusinessException
  {
    UFDouble[] newPrice = null;
    try {
      OrderDMO orderDMO = new OrderDMO();
      newPrice = orderDMO.getNewLocalPricesForPr(saBaseId, saPurCorpId);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getNewLocalPricesForPr(String [])", e);
    }

    return newPrice;
  }

  public UFDouble getOnHandNum(String pk_corp, String cStoreOrgID, String cWarehouseID, String cMangID, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String pch)
    throws BusinessException
  {
    UFDouble d = null;
    try
    {
      IICPub_InvOnHandDMO dmo = (IICPub_InvOnHandDMO)NCLocator.getInstance().lookup("nc.itf.ic.service.IICPub_InvOnHandDMO");

      d = dmo.getOnHandNum(pk_corp, cStoreOrgID, null, cMangID, null, null, null, null, null, null);
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillImpl::getOnHandNum(String,String,String,String,String,String,String,String,String,String) Exception!", e);
    }

    return d;
  }

  private UFDouble[] getPlanPricesForPr(String[] sBaseIds, String sStoreOrgId, String sCorpId)
    throws BusinessException
  {
    if ((sBaseIds == null) || (sBaseIds.length == 0) || (sCorpId == null)) {
      return null;
    }

    UFDouble[] uPlanPrice = null;

    HashMap hResult = new HashMap();
    try {
      int size = sBaseIds.length;
      uPlanPrice = new UFDouble[size];
      PraybillDMO dmo = new PraybillDMO();

      if ((sStoreOrgId == null) || (sStoreOrgId.trim().length() == 0))
        hResult = dmo.getPlanPricesFrmInvMan(sBaseIds, sCorpId);
      else {
        hResult = dmo.getPlanPricesForPr(sBaseIds, sStoreOrgId, sCorpId);
      }
      if ((hResult == null) || (hResult.size() == 0))
        return null;
      for (int i = 0; i < size; i++) {
        if (sBaseIds[i] == null)
          continue;
        uPlanPrice[i] = ((UFDouble)hResult.get(sBaseIds[i]));
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getPlanPricesForPr(String [], String, String)", e);
    }

    return uPlanPrice;
  }

  private String getPrayBillPrice(String corpId)
    throws BusinessException
  {
    SysInitVO m_prayBillPriceVO = null;

    String sPrayBillPrice = null;
    try {
      SysInitBO myService = new SysInitBO();
      m_prayBillPriceVO = myService.queryByParaCode(corpId, "PO29");
      sPrayBillPrice = m_prayBillPriceVO.getValue();
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000007"), new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000007")));
    }

    return sPrayBillPrice;
  }

  public String[] getRefDeptIDName(String cPsnID)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();
    Vector v = new Vector();

    String sExpress = "getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,cPsnID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[1];
    sParam[0] = StringUtil.toString(cPsnID);
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sDeptID = f.getValueS();
    sDeptID[0] = StringUtil.toString(sDeptID[0]);

    sExpress = "getColValue(bd_deptdoc,deptname,pk_deptdoc,cDeptID)";
    f.setExpress(sExpress);
    varry = f.getVarry();
    table.put(varry.getVarry()[0], sDeptID);
    f.setDataS(table);
    String[] sDeptName = f.getValueS();

    if ((sDeptID != null) && (sDeptID.length > 0))
      v.addElement(sDeptID[0]);
    else {
      v.addElement(null);
    }
    if ((sDeptName != null) && (sDeptName.length > 0))
      v.addElement(sDeptName[0]);
    else {
      v.addElement(null);
    }
    String[] s = new String[v.size()];
    v.copyInto(s);

    return s;
  }

  public String[] getRefOperatorAuditorName(String cOperatorID, String cAuditPsnID)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(sm_user,user_name,cUserId,cOperatorID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[2];
    sParam[0] = StringUtil.toString(cOperatorID);
    sParam[1] = StringUtil.toString(cAuditPsnID);
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sPsnName = f.getValueS();

    if ((sPsnName != null) && (sPsnName.length > 0))
      return sPsnName;
    return null;
  }

  public String[] getRefValues(String cOperatorID)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();
    Vector v = new Vector();

    String sExpress1 = "getColValue(sm_userandclerk,pk_psndoc,userid,cOperatorID)";

    String sExpress2 = "getColValue(sm_user,user_name,cUserId,cOperatorID)";

    f.setExpressArray(new String[] { sExpress1, sExpress2 });

    VarryVO[] varry = f.getVarryArray();

    String[] sParam1 = new String[1];
    sParam1[0] = cOperatorID;

    f.addVariable(varry[0].getVarry()[0], sParam1);
    f.addVariable(varry[1].getVarry()[0], sParam1);

    String[] sPsnKey = f.getValueSArray()[0];
    String[] sOperatorName = f.getValueSArray()[1];

    sExpress1 = "getColValue(bd_psndoc,psnname,pk_psndoc,cauditpsn)";

    sExpress2 = "getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,cPsnID)";

    f.setExpressArray(new String[] { sExpress1, sExpress2 });

    varry = f.getVarryArray();

    f.addVariable(varry[0].getVarry()[0], sPsnKey);
    f.addVariable(varry[1].getVarry()[0], sPsnKey);

    String[] sPsnName = f.getValueSArray()[0];
    String[] sDeptID = f.getValueSArray()[1];

    String sExpress = "getColValue(bd_deptdoc,deptname,pk_deptdoc,cDeptID)";
    f.setExpress(sExpress);
    VarryVO varry0 = f.getVarry();
    Hashtable table0 = new Hashtable();
    table0.put(varry0.getVarry()[0], sDeptID);
    f.setDataS(table0);
    String[] sDeptName = f.getValueS();

    if ((sPsnKey != null) && (sPsnKey.length > 0))
      v.addElement(sPsnKey[0]);
    else {
      v.addElement(null);
    }
    if ((sPsnName != null) && (sPsnName.length > 0))
      v.addElement(sPsnName[0]);
    else {
      v.addElement(null);
    }
    if ((sOperatorName != null) && (sOperatorName.length > 0))
      v.addElement(sOperatorName[0]);
    else {
      v.addElement(null);
    }
    if ((sDeptID != null) && (sDeptID.length > 0))
      v.addElement(sDeptID[0]);
    else {
      v.addElement(null);
    }
    if ((sDeptName != null) && (sDeptName.length > 0))
      v.addElement(sDeptName[0]);
    else {
      v.addElement(null);
    }
    String[] s = new String[v.size()];
    v.copyInto(s);

    return s;
  }

  private String getReplacedSQL(String sSource, String sOld, String sReplace)
  {
    if ((sReplace == null) || (sReplace.trim().length() == 0)) {
      return sSource;
    }
    int nStart = sSource.indexOf(sOld);
    if (nStart < 0)
      return sSource;
    int nMiddle = sSource.indexOf("'", nStart + 1);
    if (nMiddle < 0)
      return sSource;
    int nEnd = sSource.indexOf("'", nMiddle + 1);

    String s1 = sSource.substring(0, nStart);
    String s2 = sSource.substring(nEnd + 1);

    String s = s1 + sReplace + s2;

    return s;
  }

  public UFDouble[] getRulePrice(String[] saPurCorpId, String[] saBaseId, String sStoreOrgId, String sReqCorpId)
    throws BusinessException
  {
    if ((saBaseId == null) || (saBaseId.length == 0)) {
      return null;
    }
    String sPrayBillPrice = null;
    UFDouble[] dPrice = null;
    try
    {
      sPrayBillPrice = getPrayBillPrice(sReqCorpId);

      if ("无默认".equals(sPrayBillPrice)) {
        return new UFDouble[saBaseId.length];
      }

      if (sPrayBillPrice.equalsIgnoreCase("计划价")) {
        dPrice = getPlanPricesForPr(saBaseId, sStoreOrgId, sReqCorpId);
      }
      else if (sPrayBillPrice.equalsIgnoreCase("订单最低价")) {
        if ((saPurCorpId != null) && (saPurCorpId.length > 0) && (saPurCorpId.length == saBaseId.length))
        {
          dPrice = getLowLocalPricesForPr(saBaseId, saPurCorpId);
        }

      }
      else if ((sPrayBillPrice.equalsIgnoreCase("订单最新价")) && 
        (saPurCorpId != null) && (saPurCorpId.length > 0) && (saPurCorpId.length == saBaseId.length))
      {
        dPrice = getNewLocalPricesForPr(saBaseId, saPurCorpId);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getRulePrice(String [], String [], String, String)", e);
    }

    return dPrice;
  }

  private String[] getSourceSql(String sourceTable1, String sourceTable2, String strsql)
  {
    String tablename = null;
    String wherepart = null;

    if (strsql != null) {
      wherepart = "(1=1)";

      if (strsql.indexOf("bd_corp") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "bd_corp";
          else
            tablename = tablename + "," + "po_praybill,bd_corp";
        }
        else {
          tablename = "po_praybill,bd_corp";
        }
        wherepart = wherepart + " and (po_praybill.pk_corp = bd_corp.pk_corp) and (bd_corp.dr = 0)";
      }

      if (strsql.indexOf("bd_calbody") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "bd_calbody";
          else
            tablename = tablename + "," + "po_praybill,bd_calbody";
        }
        else {
          tablename = "po_praybill,bd_calbody";
        }
        wherepart = wherepart + " and (po_praybill_b.cpurorganization = bd_calbody.pk_calbody and po_praybill.pk_corp = bd_calbody.pk_corp) and (bd_calbody.dr = 0)";
      }

      if (strsql.indexOf("bd_deptdoc") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "bd_deptdoc";
          else
            tablename = tablename + "," + "po_praybill,bd_deptdoc";
        }
        else {
          tablename = "po_praybill,bd_deptdoc";
        }
        wherepart = wherepart + " and (po_praybill.cdeptid = bd_deptdoc.pk_deptdoc and po_praybill.pk_corp = bd_deptdoc.pk_corp) and (bd_deptdoc.dr = 0)";
      }

      if (strsql.indexOf("bd_psndoc") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "bd_psndoc";
          else
            tablename = tablename + "," + "po_praybill,bd_psndoc";
        }
        else {
          tablename = "po_praybill,bd_psndoc";
        }
        wherepart = wherepart + " and (po_praybill.cpraypsn = bd_psndoc.pk_psndoc and po_praybill.pk_corp = bd_psndoc.pk_corp) and (bd_psndoc.dr = 0)";
      }

      if ((strsql.indexOf("businame") >= 0) || (strsql.indexOf("busicode") >= 0))
      {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "bd_busitype";
          else
            tablename = tablename + "," + "po_praybill,bd_busitype";
        }
        else {
          tablename = "po_praybill,bd_busitype";
        }
        wherepart = wherepart + " and (po_praybill.cbiztype = bd_busitype.pk_busitype) and (bd_busitype.dr = 0)";
      }

      if (strsql.indexOf("sm_user.user_code") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") >= 0)
            tablename = tablename + "," + "sm_user";
          else
            tablename = tablename + "," + "po_praybill,sm_user";
        }
        else {
          tablename = "po_praybill,sm_user";
        }
        wherepart = wherepart + " and (po_praybill.coperator = sm_user.cuserid) and (sm_user.dr = 0)";
      }

      if (strsql.indexOf("bd_invcl") >= 0)
      {
        if (tablename != null) {
          if (tablename.indexOf("bd_invbasdoc") >= 0) {
            tablename = tablename + "," + "bd_invcl";
          }
          else if (tablename.indexOf("po_praybill_b") >= 0)
            tablename = "bd_invbasdoc,bd_invcl";
          else {
            tablename = tablename + "," + "po_praybill_b,bd_invbasdoc,bd_invcl";
          }
        }
        else
        {
          tablename = "po_praybill_b,bd_invbasdoc,bd_invcl";
        }
        wherepart = wherepart + " and ((po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc) and (bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl) and (bd_invbasdoc.dr = 0) and (bd_invcl.dr = 0))";
      }

      if (strsql.indexOf("bd_purorg") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill_b") >= 0)
            tablename = tablename + "," + "bd_purorg";
          else
            tablename = tablename + "," + "po_praybill_b,bd_purorg";
        }
        else {
          tablename = "po_praybill_b,bd_purorg";
        }
        wherepart = wherepart + " and (po_praybill_b.cpurorganization = bd_purorg.pk_purorg) and (bd_purorg.dr = 0)";
      }

      if (strsql.indexOf("bd_invmandoc.def1") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill_b,") >= 0)
            tablename = tablename + "," + "bd_busitype";
          else {
            tablename = tablename + "," + "po_praybill_b,bd_invmandoc";
          }
        }
        else {
          tablename = "po_praybill_b,bd_invmandoc";
        }
        wherepart = wherepart + " and (po_praybill_b.cmangid = bd_invmandoc.pk_invmandoc) and (bd_invmandoc.dr = 0)";
      }

      if (strsql.indexOf("po_praybill.") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill,") < 0)
            tablename = "po_praybill," + tablename;
        }
        else {
          tablename = "po_praybill";
        }
        wherepart = wherepart + " and (po_praybill.dr = 0)";
      }

      if (strsql.indexOf("po_praybill_b") >= 0) {
        if (tablename != null) {
          if (tablename.indexOf("po_praybill_b") < 0)
            tablename = tablename + ",po_praybill_b";
        }
        else {
          tablename = "po_praybill_b";
        }
        wherepart = wherepart + " and (po_praybill_b.dr = 0)";
      }
    }

    if (tablename == null) {
      tablename = "po_praybill";
    }
    else if (((tablename.indexOf("po_praybill") < 0) || (tablename.length() != 11)) && (tablename.indexOf("po_praybill,") < 0))
    {
      tablename = "po_praybill," + tablename;
    }

    if (tablename != null) {
      if ((tablename.indexOf("po_praybill,") >= 0) || ((tablename.indexOf("po_praybill") >= 0) && (tablename.length() == 11)))
      {
        if (wherepart != null) {
          if (wherepart.indexOf("po_praybill.dr") < 0)
            wherepart = wherepart + " and (po_praybill.dr = 0)";
        }
        else {
          wherepart = "(po_praybill.dr = 0)";
        }
      }

      if ((tablename.indexOf("po_praybill,") >= 0) && (tablename.indexOf("po_praybill_b") >= 0))
      {
        wherepart = wherepart + " and (po_praybill.cpraybillid = po_praybill_b.cpraybillid)";
      }
    }
    else {
      wherepart = strsql;
    }

    String[] sql = new String[2];
    sql[0] = tablename;
    if ((wherepart != null) && (!wherepart.trim().equals("")) && (strsql != null) && (!strsql.trim().equals("")))
    {
      sql[1] = (wherepart + " and " + strsql);
    } else if (((wherepart == null) || (wherepart.trim().equals(""))) && (strsql != null) && (!strsql.trim().equals("")))
    {
      sql[1] = strsql;
    } else if (((strsql == null) || (strsql.trim().equals(""))) && (wherepart != null) && (!wherepart.trim().equals("")))
    {
      sql[1] = wherepart;
    }
    else sql[1] = null;

    return sql;
  }

  public PraybillVO getSpecifiedPraybill(String sHeadKey)
    throws BusinessException
  {
    PraybillVO VO = null;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      String sWhere = " dr = 0 and cpraybillid = '" + sHeadKey + "'";
      PraybillHeaderVO[] headVO = (PraybillHeaderVO[])(PraybillHeaderVO[])dmo.queryAllHeadData(sWhere);

      if ((headVO != null) && (headVO.length > 0)) {
        PraybillItemVO[] bodyVO = (PraybillItemVO[])(PraybillItemVO[])dmo.queryAllBodyData(sHeadKey);

        VO = new PraybillVO(bodyVO.length);
        VO.setParentVO(headVO[0]);
        VO.setChildrenVO(bodyVO);
        return VO;
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::getSpecifiedPraybill(String) Exception!", e);
    }

    return null;
  }

  private String[] getSQLByConditionVOs(ConditionVO[] conditionVO, String unitCode)
    throws BusinessException
  {
    String sHCondition = "";
    String sBCondition = "";

    String sName = null;
    String sOpera = null;
    String sValue = null;
    String sSQL = null;
    String sReplace = null;

    String ss = null;
    for (int i = 0; i < conditionVO.length; i++) {
      sName = conditionVO[i].getFieldCode().trim();
      sOpera = conditionVO[i].getOperaCode().trim();
      sValue = conditionVO[i].getValue();
      if ((sOpera.equalsIgnoreCase("like")) && (!sName.equals("bd_invcl.invclasscode")))
      {
        sValue = "%" + sValue + "%";
      }sSQL = conditionVO[i].getSQLStr();
      int nTableName = sSQL.indexOf("tablename");

      sReplace = "";

      if (sOpera.equalsIgnoreCase("in"))
      {
        if ((sName.equals("po_praybill_b.cemployeeid")) || (sName.equals("po_praybill_b.cprojectid")) || (sName.equals("po_praybill.cpraypsn")) || (sName.equals("po_praybill.cdeptid")) || (sName.equals("po_praybill_b.cwarehouseid")) || (sName.equals("po_praybill.ccustomerid")))
        {
          conditionVO[i].setOperaCode(" is null or " + sName + " in ");

          conditionVO[i].setDataType(1);
        }
        sSQL = conditionVO[i].getSQLStr();
        if ((sSQL != null) && (!sSQL.trim().equals(""))) {
          sHCondition = sHCondition + sSQL;
        }
      }
      else
      {
        if ((sName.equals("po_praybill.ccustomerid")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.ccustomerid " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill.dpraydate")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.dpraydate " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill.vpraycode")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.vpraycode " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.pk_reqcorp")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill_b.pk_reqcorp " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.pk_reqstoorg")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill_b.pk_reqstoorg " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill.vpraycode")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.vpraycode " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.cwarehouseid")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill_b.cwarehouseid " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("iprintcount")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.iprintcount " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill.cdeptid")) && (sValue != null) && (sValue.length() > 0))
        {
          if (unitCode != null) {
            sReplace = "po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where pk_corp = '" + unitCode + "' and deptname " + sOpera + "  '" + sValue + "')";
          }
          else
          {
            sReplace = " po_praybill.cdeptid in (select pk_deptdoc from bd_deptdoc where deptname " + sOpera + "  '" + sValue + "')";
          }

          if (sOpera.equals("<>")) {
            sReplace = "(" + sReplace + "or po_praybill.cdeptid is null)";
          }

        }
        else if ((sName.equals("po_praybill.cpraypsn")) && (sValue != null) && (sValue.length() > 0))
        {
          if (unitCode != null) {
            sReplace = " po_praybill.cpraypsn in (select pk_psndoc from bd_psndoc where pk_corp = '" + unitCode + "' and psnname " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = " cpraypsn in (select pk_psndoc from bd_psndoc where psnname " + sOpera + " '" + sValue + "')";
          }

          if (sOpera.equals("<>")) {
            sReplace = "(" + sReplace + " or po_praybill.cpraypsn is null)";
          }

        }
        else if ((sName.equals("po_praybill.coperator")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = " po_praybill.coperator in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
        }
        else if ((sName.equals("po_praybill.ipraysource")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
        {
          int k = 0;
          if (sValue.trim().equals("MRP"))
            k = 0;
          else if (sValue.trim().equals("MO"))
            k = 1;
          else if (sValue.trim().equals("SCF"))
            k = 2;
          else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000037")))
          {
            k = 3;
          } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000038")))
          {
            k = 4;
          } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000039")))
          {
            k = 5;
          } else if (sValue.trim().equals("DRP"))
            k = 6;
          else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000040")))
          {
            k = 7;
          } else if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000204")))
          {
            k = 8;
          } else if (sValue.trim().equals("")) {
            k = -1;
          }
          if (k >= 0) {
            sReplace = " po_praybill.ipraysource = " + k;
          }

        }
        else if ((sName.equals("po_praybill.ipraytype")) && (sOpera.equals("=")) && (sValue != null) && (sValue.length() > 0))
        {
          int k = 0;
          if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000041")))
          {
            k = 0;
          }if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000042")))
          {
            k = 1;
          }if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000043")))
          {
            k = 2;
          }if (sValue.trim().equals(NCLangResOnserver.getInstance().getStrByID("40040102", "UPP40040102-000044")))
          {
            k = 3;
          }if (sValue.trim().equals(""))
            k = -1;
          if (k >= 0) {
            sReplace = " po_praybill.ipraytype = " + k;
          }

        }
        else if ((sName.equals("po_praybill.cbiztype")) && (sValue != null) && (sValue.length() > 0))
        {
          String biztype = "直运销售采购";
          if (sValue.equals(biztype)) {
            sValue = "直运%采购";
            sOpera = "like";
          }
          if (unitCode != null) {
            sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where pk_corp = '" + unitCode + "' and businame " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = " po_praybill.cbiztype in (select pk_busitype from bd_busitype where businame " + sOpera + " '" + sValue + "')";
          }

        }
        else if ((sName.equals("po_praybill_b.cpurorganization")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = " po_praybill_b.cpurorganization in (select pk_purorg from bd_purorg where name " + sOpera + " '" + sValue + "')";
        }
        else if ((sName.equals("bd_invcl.invclasscode")) && (sValue != null) && (sValue.length() > 0))
        {
          try {
            CostanalyseDMO ddmo = new CostanalyseDMO();
            String[] sClassCode = ddmo.getSubInvClassCode(sValue, sOpera);

            sOpera = "=";
            if ((sClassCode != null) && (sClassCode.length > 0)) {
              sValue = "(";
              for (int j = 0; j < sClassCode.length; j++) {
                if (j < sClassCode.length - 1) {
                  sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "' or ";
                }
                else {
                  sValue = sValue + "invclasscode " + sOpera + " '" + sClassCode[j] + "')";
                }
              }
              if ((unitCode != null) && (unitCode.trim().length() > 0)) {
                sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
              }
              else
              {
                sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";
              }

            }
            else if ((unitCode != null) && (unitCode.trim().length() > 0)) {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + unitCode + "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
            }
            else
            {
              sReplace = "po_praybill_b.cbaseid in (select A.pk_invbasdoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";
            }

          }
          catch (Exception e)
          {
            SCMEnv.out(e);
            PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000008"), e);
          }

        }
        else if (sName.equals("bd_invbasdoc.invcode")) {
          if (unitCode != null) {
            sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
            sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
            sReplace = sReplace + "and bd_invmandoc.pk_corp = '";
            sReplace = sReplace + unitCode + "' ";
            sReplace = sReplace + "and bd_invbasdoc.invcode ";
            sReplace = sReplace + sOpera;
            sReplace = sReplace + "'";
            sReplace = sReplace + sValue;
            sReplace = sReplace + "') ";
          } else {
            sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
            sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
            sReplace = sReplace + "and bd_invbasdoc.invcode ";
            sReplace = sReplace + sOpera;
            sReplace = sReplace + "'";
            sReplace = sReplace + sValue;
            sReplace = sReplace + "') ";
          }

        }
        else if (sName.equals("bd_invbasdoc.invname")) {
          if (unitCode != null) {
            sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
            sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
            sReplace = sReplace + "and bd_invmandoc.pk_corp = '";
            sReplace = sReplace + unitCode + "' ";
            sReplace = sReplace + "and bd_invbasdoc.invname ";
            sReplace = sReplace + sOpera;
            sReplace = sReplace + "'";
            sReplace = sReplace + sValue;
            sReplace = sReplace + "') ";
          } else {
            sReplace = " po_praybill_b.cmangid in (select pk_invmandoc from bd_invmandoc,bd_invbasdoc ";
            sReplace = sReplace + "where bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
            sReplace = sReplace + "and bd_invbasdoc.invname ";
            sReplace = sReplace + sOpera;
            sReplace = sReplace + "'";
            sReplace = sReplace + sValue;
            sReplace = sReplace + "') ";
          }

        }
        else if ((sName.equals("po_praybill_b.vproducenum")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = " po_praybill_b.vproducenum " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.ddemanddate")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill_b.ddemanddate " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.dsuggestdate")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill_b.dsuggestdate " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill.cauditpsn")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = " po_praybill.cauditpsn in (select cuserid  from sm_user where user_name " + sOpera + " '" + sValue + "')";
        }
        else if ((sName.equals("po_praybill.dauditdate")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = "po_praybill.dauditdate " + sOpera + " '" + sValue + "'";
        }
        else if ((sName.equals("po_praybill_b.cprojectid")) && (sValue != null) && (sValue.length() > 0))
        {
          if (unitCode != null) {
            sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and bd_jobmngfil.pk_corp ='" + unitCode + "' and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = " po_praybill_b.cprojectid in (select pk_jobmngfil from bd_jobmngfil,bd_jobbasfil where bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil  and bd_jobbasfil.jobname " + sOpera + " '" + sValue + "')";
          }

        }
        else if ((sName.equals("po_praybill_b.cprojectphaseid")) && (sValue != null) && (sValue.length() > 0))
        {
          sReplace = " po_praybill_b.cprojectphaseid " + sOpera + " '" + sValue + "'";
        }
        else if (sName.indexOf("po_praybill.vdef") >= 0) {
          if (sName.length() >= "po_praybill.vdef".length() + 1) {
            sReplace = "po_praybill.vdef" + sName.substring("po_praybill.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
          }

        }
        else if (sName.indexOf("po_praybill_b.vdef") >= 0) {
          if (sName.length() >= "po_praybill_b.vdef".length() + 1) {
            sReplace = "po_praybill_b.vdef" + sName.substring("po_praybill_b.vdef".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
          }

        }
        else if (sName.indexOf("po_praybill_b.vfree") >= 0) {
          if (sName.length() >= "po_praybill_b.vfree".length() + 1) {
            sReplace = "po_praybill_b.vfree" + sName.substring("po_praybill_b.vfree".length(), sName.length()) + " " + sOpera + " '" + sValue + "'";
          }

        }
        else if ((sName.equals("po_praybill_b.cemployeeid")) && (sValue != null) && (sValue.length() > 0))
        {
          if (unitCode != null) {
            sReplace = " po_praybill_b.cemployeeid in (select pk_psndoc from bd_psndoc where pk_corp = '" + unitCode + "' and psnname " + sOpera + " '" + sValue + "')";
          }
          else
          {
            sReplace = " po_praybill_b.cemployeeid in (select pk_psndoc from bd_psndoc where psnname " + sOpera + " '" + sValue + "')";
          }
        }
        else if (sName.equals("po_praybill.cpraybillid")) {
          sHCondition = sHCondition + sSQL;
        }

        if ((sReplace != null) && (!sReplace.trim().equals(""))) {
          sHCondition = sHCondition + getReplacedSQL(sSQL, sName, sReplace);
        }

        if ((sReplace != null) && (!sReplace.trim().equals("")) && (sReplace.indexOf("po_praybill_b") >= 0))
        {
          sBCondition = sBCondition + getReplacedSQL(sSQL, sName, sReplace);
        }

        if (ss != null)
          sHCondition = sHCondition + ss; 
      }
    }
    return new String[] { processFirst(sHCondition), processFirst(sBCondition) };
  }

  private String getSQLByConditionVOs(ConditionVO[] conditionVO, String unitCode, String strSubSql)
    throws BusinessException
  {
    String sHCondition = "";
    try {
      if (conditionVO != null)
        sHCondition = sHCondition + getSQLByConditionVOs(conditionVO, unitCode)[0];
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getSQLByConditionVOs(ConditionVO [], String, String)", e);
    }

    if (strSubSql != null) {
      sHCondition = sHCondition + " and " + strSubSql;
    }
    return sHCondition;
  }

  private UFDate[] getTendaysPeriods(UFDate dBegin, UFDate dEnd)
  {
    if ((dBegin == null) || (dBegin.toString().length() == 0))
      return null;
    if ((dEnd == null) || (dEnd.toString().length() == 0)) {
      return null;
    }

    UFDate[] temp = getMonthPeriods(dBegin, dEnd);
    if ((temp == null) || (temp.length == 0)) {
      return null;
    }
    Vector v = new Vector();

    if (dBegin.getDay() < 11) {
      int nYear = dBegin.getYear();
      int nMonth = dBegin.getMonth();

      int nDay = 10;
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = 20;
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = UFDate.getDaysMonth(nYear, nMonth);
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    } else if (dBegin.getDay() < 21) {
      int nYear = dBegin.getYear();
      int nMonth = dBegin.getMonth();

      int nDay = 20;
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = UFDate.getDaysMonth(nYear, nMonth);
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    } else {
      int nYear = dBegin.getYear();
      int nMonth = dBegin.getMonth();
      int nDay = UFDate.getDaysMonth(nYear, nMonth);
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }

    for (int i = 1; i <= temp.length - 2; i++) {
      int nYear = temp[i].getYear();
      int nMonth = temp[i].getMonth();
      int nDay = 10;
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = 20;
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = UFDate.getDaysMonth(nYear, nMonth);
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }

    if (dEnd.getDay() < 11) {
      int nYear = dEnd.getYear();
      int nMonth = dEnd.getMonth();

      int nDay = dEnd.getDay();
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    } else if (dEnd.getDay() < 21) {
      int nYear = dEnd.getYear();
      int nMonth = dEnd.getMonth();

      int nDay = 10;
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = dEnd.getDay();
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    } else {
      int nYear = dEnd.getYear();
      int nMonth = dEnd.getMonth();

      int nDay = 10;
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = 20;
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));

      nDay = dEnd.getDay();
      s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }

    if (v.size() > 0)
    {
      Vector vvv = new Vector();
      for (int i = 0; i < v.size(); i++) {
        UFDate d1 = (UFDate)v.elementAt(i);
        if ((d1.compareTo(dEnd) <= 0) && (d1.compareTo(dBegin) >= 0)) {
          vvv.addElement(d1);
        }
      }

      Vector vv = new Vector();
      for (int i = 0; i < vvv.size() - 1; i++) {
        boolean b = false;
        UFDate d1 = (UFDate)vvv.elementAt(i);
        int nMonth1 = d1.getMonth();
        int nYear1 = d1.getYear();
        int nDay1 = d1.getDay();
        for (int j = i + 1; j < vvv.size(); j++) {
          UFDate d2 = (UFDate)vvv.elementAt(j);
          int nMonth2 = d2.getMonth();
          int nYear2 = d2.getYear();
          int nDay2 = d2.getDay();
          if ((nYear1 == nYear2) && (nMonth1 == nMonth2)) {
            int nDays = UFDate.getDaysMonth(nYear1, nMonth1);
            int nDiffer = 10;
            if (nDays == 31)
              nDiffer = 11;
            if ((nDay1 <= 10) && (nDay2 <= 10) && (Math.abs(nDay1 - nDay2) < 10))
            {
              b = true;
              break;
            }if ((nDay1 <= 20) && (nDay1 > 10) && (nDay2 <= 20) && (nDay2 > 10) && (Math.abs(nDay1 - nDay2) < 10))
            {
              b = true;
              break;
            }if ((nDay1 > nDays) || (nDay1 <= 20) || (nDay2 > nDays) || (nDay2 <= 20) || (Math.abs(nDay1 - nDay2) >= nDiffer)) {
              continue;
            }
            b = true;
            break;
          }
        }

        if (!b)
          vv.addElement(d1);
      }
      vv.addElement(v.elementAt(v.size() - 1));

      if (vv.size() > 0) {
        UFDate[] d = new UFDate[vv.size()];
        vv.copyInto(d);
        return d;
      }
      return null;
    }

    return null;
  }

  public String getUnit(String cBaseID)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cBaseID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[1];
    sParam[0] = cBaseID;
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sMeasID = f.getValueS();

    if ((sMeasID != null) && (sMeasID.length > 0))
      return sMeasID[0];
    return null;
  }

  public PraybillItemVO[] getVOForUse(PraybillItemVO[] bodyVO)
    throws BusinessException
  {
    FormulaParse f = new FormulaParse();

    String sExpress = "getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cBaseID)";
    f.setExpress(sExpress);
    VarryVO varry = f.getVarry();
    Hashtable table = new Hashtable();
    String[] sParam = new String[bodyVO.length];
    for (int i = 0; i < bodyVO.length; i++)
      sParam[i] = bodyVO[i].getCbaseid();
    table.put(varry.getVarry()[0], sParam);
    f.setDataS(table);
    String[] sAttr = f.getValueS();

    Vector v = new Vector();
    for (int i = 0; i < sAttr.length; i++) {
      String s = sAttr[i].toUpperCase();
      if (s.equals("N"))
        v.addElement(bodyVO[i]);
    }
    if (v.size() > 0) {
      PraybillItemVO[] tempVO = new PraybillItemVO[v.size()];
      v.copyInto(tempVO);
      return tempVO;
    }
    return null;
  }

  private UFDate[] getWeekPeriods(UFDate dBegin, UFDate dEnd)
  {
    if ((dBegin == null) || (dBegin.toString().length() == 0))
      return null;
    if ((dEnd == null) || (dEnd.toString().length() == 0)) {
      return null;
    }
    Vector v = new Vector();

    int nBWeek = dBegin.getWeek();
    if (nBWeek > 0) {
      int nYear = dBegin.getYear();
      int nMonth = dBegin.getMonth();

      while (dBegin.getWeek() != 6) {
        dBegin = dBegin.getDateAfter(1);
      }

      int nDay = dBegin.getDay();
      String s = nYear + "-" + nMonth + "-" + nDay;
      v.addElement(new UFDate(s));
    }

    UFDate d = null;
    int nEWeek = dEnd.getWeek();
    if (nEWeek > 0) {
      int nYear = dEnd.getYear();
      int nMonth = dEnd.getMonth();
      int nDay = dEnd.getDay();
      String s = nYear + "-" + nMonth + "-" + nDay;
      d = new UFDate(s);

      while (dEnd.getWeek() != 6) {
        dEnd = dEnd.getDateBefore(1);
      }

    }

    int nPeriod = UFDate.getDaysBetween(dBegin, dEnd) / 7;
    for (int i = 1; i <= nPeriod; i++) {
      UFDate temp = dBegin.getDateAfter(i * 7);
      v.addElement(temp);
    }
    if (d != null) {
      v.addElement(d);
    }
    if (v.size() > 0)
    {
      Vector vv = new Vector();
      for (int i = 0; i < v.size() - 1; i++) {
        boolean b = false;
        UFDate d1 = (UFDate)v.elementAt(i);
        int nMonth1 = d1.getMonth();
        int nYear1 = d1.getYear();
        int nDay1 = d1.getDay();
        for (int j = i + 1; j < v.size(); j++) {
          UFDate d2 = (UFDate)v.elementAt(j);
          int nMonth2 = d2.getMonth();
          int nYear2 = d2.getYear();
          int nDay2 = d2.getDay();
          if ((nYear1 == nYear2) && (nMonth1 == nMonth2)) {
            int nDays = UFDate.getDaysMonth(nYear1, nMonth1);
            if ((nDay1 <= 10) && (nDay2 <= 10) && (Math.abs(nDay1 - nDay2) < 7))
            {
              b = true;
              break;
            }if ((nDay1 <= 20) && (nDay1 > 10) && (nDay2 <= 20) && (nDay2 > 10) && (Math.abs(nDay1 - nDay2) < 7))
            {
              b = true;
              break;
            }if ((nDay1 > nDays) || (nDay1 <= 20) || (nDay2 > nDays) || (nDay2 <= 20) || (Math.abs(nDay1 - nDay2) >= 7)) {
              continue;
            }
            b = true;
            break;
          }
        }

        if (!b)
          vv.addElement(d1);
      }
      vv.addElement(v.elementAt(v.size() - 1));

      if (vv.size() > 0) {
        UFDate[] dd = new UFDate[vv.size()];
        vv.copyInto(dd);
        return dd;
      }
      return null;
    }

    return null;
  }

  public UFBoolean isInvBelongStoreOrg(PraybillVO VO)
    throws BusinessException
  {
    UFBoolean b = new UFBoolean(true);
    try {
      PraybillDMO dmo = new PraybillDMO();

      UFBoolean[] bb = dmo.isInvBelongStoreOrg(VO);
      if (bb == null)
        return new UFBoolean(false);
      for (int i = 0; i < bb.length; i++) {
        b = bb[i];
        if (!b.booleanValue())
          break;
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::isInvBelongStoreOrg(PraybillVO) Exception!", e);
    }

    return b;
  }

  public UFBoolean isPsnBelongDept(PraybillVO VO)
    throws BusinessException
  {
    boolean b = false;
    try {
      PraybillDMO dmo = new PraybillDMO();

      PraybillHeaderVO headVO = VO.getHeadVO();
      String sUnitCode = headVO.getPk_corp();
      String sDeptID = headVO.getCdeptid();
      String sPsnID = headVO.getCpraypsn();
      if ((sDeptID != null) && (sDeptID.length() > 0) && (sPsnID != null) && (sPsnID.length() > 0))
      {
        b = dmo.isPsnBelongDept(sUnitCode, sDeptID, sPsnID);
      }
      else return new UFBoolean(!b); 
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::isPsnBelongDept(PraybillVO) Exception!", e);
    }

    return new UFBoolean(b);
  }

  private boolean isPushSave(PraybillVO vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0))
    {
      return false;
    }for (int i = 0; i < vo.getChildrenVO().length; i++) {
      if ((vo.getChildrenVO()[i].getAttributeValue("crowno") == null) || (vo.getChildrenVO()[i].getAttributeValue("crowno").toString().trim().equals("")))
      {
        return true;
      }
    }
    return false;
  }

  public void onRearOrderDelete(String[] cPraybillIds)
    throws BusinessException
  {
    String sMethodName = "nc.bs.pr.PraybillImpl.onRearOrderDelete()";
  }

  public ArrayList openBill(PraybillVO vo)
    throws BusinessException
  {
    PraybillVO newVO = new PraybillVO();
    ArrayList array = new ArrayList();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      dmo.openBill(vo);

      StringBuffer sCondition = new StringBuffer("");
      sCondition.append(" and po_praybill_b.cpraybillid = '" + ((PraybillHeaderVO)vo.getParentVO()).getCpraybillid() + "' ");

      PraybillHeaderVO head = dmo.queryAllHead(null, sCondition.toString(), null)[0];
      newVO.setParentVO(head);
      PraybillItemVO[] items = (PraybillItemVO[])(PraybillItemVO[])vo.getChildrenVO();
      if ((items != null) && (items.length > 0)) {
        ArrayList listBid = new ArrayList();
        for (int i = 0; i < items.length; i++) {
          listBid.add(items[i].getCpraybill_bid());
        }
        sCondition.append(" and po_praybill_b.cpraybill_bid in " + new TempTableUtil().getSubSql(listBid) + " ");
      }
      items = dmo.queryAllBody(sCondition.toString());

      items = getItemWithMny(items);

      newVO.setChildrenVO(items);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.openBill(PraybillVO)", e);
    }

    array.add(newVO);
    return array;
  }

  private String processFirst(String s)
  {
    s = s.trim();

    int nNull = s.indexOf(" ");
    if (nNull > 0)
    {
      String s2 = s.substring(nNull);
      s = " and (" + s2 + ")";
      return " " + s + " ";
    }

    return s;
  }

  public Vector queryAdvanceDays(String sUnitCode, String sKey1, String sKey2)
    throws BusinessException
  {
    Vector v = new Vector();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      v = dmo.queryAdvanceDays(sUnitCode, sKey1, sKey2);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryAdvanceDays(String,String,String) Exception!", e);
    }

    return v;
  }

  public PraybillVO[] queryAll(String unitCode, ConditionVO[] conditionVO, String sAudit, String sOperPsn, String strSubSql)
    throws BusinessException
  {
    PraybillVO[] VO = null;
    String sOperator = null;
    String ss = null;
    try {
      PraybillDMO dmo = new PraybillDMO();

      if ((conditionVO != null) && (conditionVO.length > 0)) {
        for (int i = 0; i < conditionVO.length; i++) {
          if ((!conditionVO[i].getOperaCode().equalsIgnoreCase("in")) || (PrayPubVO._Hash_PrayUI.get(conditionVO[i].getFieldCode()) == null))
          {
            continue;
          }
          conditionVO[i].setFieldCode((String)PrayPubVO._Hash_PrayUI.get(conditionVO[i].getFieldCode()));
        }

      }

      ArrayList listRet = OrderPubDMO.dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])(ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      String sHCondition = getSQLByConditionVOs(conditionVO, null, strSubSql);

      if (strDataPowerSql != null) {
        if (sHCondition == null)
          sHCondition = " and " + strDataPowerSql;
        else {
          sHCondition = sHCondition + " and " + strDataPowerSql;
        }
      }

      StringBuffer strStatus = new StringBuffer("");
      if ((sAudit != null) && (sAudit.trim().length() > 0)) {
        strStatus.append(" and (" + sAudit + ") ");
      }

      sHCondition = processFirst(sHCondition);
      if ((sHCondition.trim().startsWith("()")) || (sHCondition.trim().startsWith("( )")))
      {
        sHCondition = "";
      }

      sHCondition = strStatus.toString() + sHCondition;

      String strPrintCnd = null;
      if ((conditionVO != null) && (conditionVO[(conditionVO.length - 1)] != null) && (conditionVO[(conditionVO.length - 1)].getFieldName() == null) && (conditionVO[(conditionVO.length - 1)].getFieldCode() != null) && (conditionVO[(conditionVO.length - 1)].getFieldCode().indexOf("iprintcount") >= 0))
      {
        strPrintCnd = " and " + conditionVO[(conditionVO.length - 1)].getFieldCode() + conditionVO[(conditionVO.length - 1)].getOperaCode() + conditionVO[(conditionVO.length - 1)].getValue();
      }

      if (strPrintCnd != null) {
        sHCondition = sHCondition + strPrintCnd + " ";
      }
      StringBuffer sbCondition = new StringBuffer(sHCondition);

      PraybillHeaderVO[] headVO = null;
      if (sOperPsn != null)
      {
        sbCondition.append("and (");
        if (sAudit.trim().equalsIgnoreCase("ibillstatus = " + BillStatus.FREE))
        {
          sbCondition.append("(pub_workflownote.receivedeleteflag = 'N' and pub_workflownote.ischeck = 'N') ");

          sbCondition.append("AND (pub_workflownote.checkman = '" + sOperPsn + "') ");

          sbCondition.append("OR ");

          //edit by yqq 2017-05-17 审批请购单查询未审核的单据时需将送审的单据也一起查询处理
     //     sbCondition.append("(NOT exists ");
    //      sbCondition.append("(SELECT billid FROM pub_workflownote  where po_praybill.cpraybillid = billid) and");

          if (unitCode != null) {
       //     sbCondition.append("and po_praybill.pk_corp in ");
            sbCondition.append(" po_praybill.pk_corp in ");

            sbCondition.append(unitCode);
            sbCondition.append(" ");
          }
     //     sbCondition.append(") AND ");         
            sbCondition.append("AND ");
     //end by yqq 2017-05-17 

          sbCondition.append("(po_praybill.ibillstatus = " + BillStatus.FREE + ") AND (po_praybill.dr = 0) ");
        }
        else
        {
          sbCondition.append("(pub_workflownote.receivedeleteflag = 'N' and pub_workflownote.approvestatus = 1) ");

          sbCondition.append("AND (pub_workflownote.checkman = '" + sOperPsn + "') ");

          sbCondition.append("OR ");

        //edit by yqq 2017-05-17 审批请购单查询未审核的单据时需将送审的单据也一起查询处理
    //      sbCondition.append("(NOT exists ");
    //      sbCondition.append("(SELECT billid FROM pub_workflownote  where po_praybill.cpraybillid = billid)and ");

          if (unitCode != null) {
     //       sbCondition.append("and po_praybill.pk_corp in ");
            sbCondition.append(" po_praybill.pk_corp in ");

            sbCondition.append(unitCode);
            sbCondition.append(" ");
          }
      //    sbCondition.append(") AND ");
          sbCondition.append("AND ");

     //     sbCondition.append("(po_praybill.ibillstatus = " + BillStatus.AUDITED + ") AND (po_praybill.dr = 0) ");
          sbCondition.append(" (po_praybill.dr = 0) ");
        //end by yqq 2017-05-17 
        }

        sbCondition.append(") ");
        headVO = dmo.queryAllHeadWorkFlow(null, sbCondition.toString(), strDataPowerSql);
      }
      else {
        headVO = dmo.queryAllHead(unitCode, sbCondition.toString(), strDataPowerSql);
      }
      if ((headVO == null) || (headVO.length == 0)) {
        return null;
      }
      StringBuffer sBCondition = new StringBuffer();
      sBCondition.append(" and po_praybill_b.cpraybillid = '");
      sBCondition.append(headVO[0].getCpraybillid());
      sBCondition.append("' ");
      sOperator = null;
      ss = null;
      if ((conditionVO != null) && (conditionVO[(conditionVO.length - 1)] != null) && ("操作员".equals(conditionVO[(conditionVO.length - 1)].getFieldName())) && (unitCode != null))
      {
        sOperator = conditionVO[(conditionVO.length - 1)].getValue();
        ss = ScmDps.getSubSql("po_praybill_b", "po_praybill_b", sOperator, new String[] { unitCode });

        if ((ss != null) && (ss.trim().length() > 0))
          sBCondition.append(" and " + ss + " ");
      }
      PraybillItemVO[] bodyVO = dmo.queryAllBody(sBCondition.toString());
      if ((bodyVO == null) || (bodyVO.length == 0)) {
        return null;
      }
      bodyVO = getItemWithMny(bodyVO);

      VO = new PraybillVO[headVO.length];
      for (int i = 0; i < headVO.length; i++) {
        VO[i] = new PraybillVO();
        VO[i].setParentVO(headVO[i]);
        if (i == 0)
          VO[i].setChildrenVO(bodyVO);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryAll() Exception!", e);
    }

    return VO;
  }

  public String[] queryCalbodyForPs(String[] saCorderId)
    throws BusinessException
  {
    String[] sID = null;
    try {
      sID = new PraybillDMO().queryCalbodyForPs(saCorderId);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return sID;
  }

  public ArrayList queryAllPrayBodys(ArrayList aryPara)
    throws BusinessException
  {
    if ((aryPara == null) || (aryPara.size() < 2)) {
      SCMEnv.out("传入参数不正确: 参数为空或参数的个数不足");
      return new ArrayList();
    }
    String[] saHId = (String[])(String[])aryPara.get(0);
    String[] saHTs = (String[])(String[])aryPara.get(1);

    if ((saHId == null) || (saHId.length < 1) || (saHTs == null) || (saHTs.length < 1) || (saHId.length != saHTs.length))
    {
      SCMEnv.out("传入参数不正确：请购单头ID与请购单头时间截不匹配");
      return new ArrayList();
    }

    ArrayList aryRet = new ArrayList();
    PraybillItemVO[] items = null;
    try {
      PraybillDMO dmo = new PraybillDMO();

      items = dmo.queryBodyByHidsHts(saHId, saHTs);

      if ((items == null) || (items.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }

      Hashtable h = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

      if ((h == null) || (h.size() < saHId.length)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }

      items = getItemWithMny(items);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000009"), e);
    }

    aryRet.add(items);
    return aryRet;
  }

  public Vector queryAssistUnitData(String sBaseID, String sMeasID)
    throws BusinessException
  {
    Vector v = new Vector();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      v = dmo.queryAssistUnitData(sBaseID, sMeasID);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryAssistUnitData(String,String) Exception!", e);
    }

    return v;
  }

  public PraybillVO[] queryBills(ArrayList aryPara)
    throws BusinessException
  {
    if ((aryPara == null) || (aryPara.size() < 0))
      return null;
    Vector v = new Vector();
    for (int j = 0; j < aryPara.size(); j++) {
      v.addElement(aryPara.get(j));
    }
    if ((v == null) || (v.size() < 0)) {
      return null;
    }
    PraybillVO[] vos = null;
    String[] saHid = new String[v.size()];
    v.copyInto(saHid);
    PraybillHeaderVO[] heads = null;
    PraybillItemVO[] items = null;

    StringBuffer sb = new StringBuffer("");
    if ((saHid != null) && (saHid.length > 0)) {
      sb.append(" and po_praybill.cpraybillid in ");
      sb.append(new TempTableUtil().getSubSql(saHid));
    }
    String strHCondition = sb.toString();
    try {
      PraybillDMO dmo = new PraybillDMO();
      heads = dmo.queryAllHead(null, strHCondition, null);
      if ((heads == null) || (heads.length < saHid.length)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }

      for (int i = 0; i < heads.length; i++) {
        if (heads[i] == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        }

      }

      items = dmo.queryBodyByHids(saHid);
      if ((items != null) && (items.length > 0)) {
        for (int i = 0; i < items.length; i++) {
          if (items[i] == null) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
          }

        }

      }

      items = getItemWithMny(items);

      Hashtable h = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

      if (h == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }

      vos = new PraybillVO[heads.length];
      String key = null;
      for (int i = 0; i < heads.length; i++) {
        vos[i] = new PraybillVO();
        vos[i].setParentVO(heads[i]);
        key = heads[i].getPrimaryKey();
        if ((!h.containsKey(key)) || (h.get(key) == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
        }

        items = (PraybillItemVO[])(PraybillItemVO[])h.get(key);
        vos[i].setChildrenVO(items);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000010"), e);
    }

    return vos;
  }

  public PraybillItemVO[] queryBody(String sCondition)
    throws BusinessException
  {
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      String sBCondition = " and cpraybillid = '" + sCondition + "'";
      PraybillItemVO[] bodyVO = dmo.queryAllBody(sBCondition);
      if ((bodyVO == null) || (bodyVO.length == 0)) {
        return null;
      }

      return getItemWithMny(bodyVO);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException(e);
    }//throw new BusinessException(e); 编译错后放置
  }

  public PraybillVO[] queryBudget(String unitCode, ConditionVO[] conditionVO)
    throws BusinessException
  {
    PraybillVO[] VO = null;
    Vector v = new Vector();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      ArrayList listRet = OrderPubDMO.dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])(ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      String[] sTemp = getSQLByConditionVOs(conditionVO, null);
      String sHCondition = sTemp[0];

      PraybillHeaderVO[] headVO = dmo.queryHeadBudget(unitCode, sHCondition, strDataPowerSql);
      if ((headVO == null) || (headVO.length == 0)) {
        return null;
      }
      String[] saHid = new String[headVO.length];
      for (int i = 0; i < headVO.length; i++) {
        saHid[i] = headVO[i].getCpraybillid();
      }

      String[] saHidTemp = new String[headVO.length];
      for (int i = 0; i < saHidTemp.length; i++) {
        saHidTemp[i] = saHid[i];
      }

      PraybillItemVO[] items = dmo.queryBodyBudget(unitCode, saHidTemp, sTemp[1], strDataPowerSql);
      if ((items == null) || (items.length <= 0))
        return null;
      Hashtable hBodyByHid = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

      if ((hBodyByHid == null) || (hBodyByHid.size() <= 0)) {
        return null;
      }
      String strHId = null;
      PraybillItemVO[] bodyVO = null;
      for (int i = 0; i < headVO.length; i++) {
        strHId = headVO[i].getPrimaryKey();
        bodyVO = (PraybillItemVO[])(PraybillItemVO[])hBodyByHid.get(strHId);
        if ((bodyVO != null) && (bodyVO.length > 0)) {
          PraybillVO tempVO = new PraybillVO(bodyVO.length);
          tempVO.setParentVO(headVO[i]);
          tempVO.setChildrenVO(bodyVO);
          v.addElement(tempVO);
        }
      }
      if (v.size() > 0) {
        VO = new PraybillVO[v.size()];
        v.copyInto(VO);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryBudget(String,ConditionVO) Exception!", e);
    }

    return VO;
  }

  private HashMap queryFixedData(String[] sKey)
    throws BusinessException
  {
    HashMap hRet = new HashMap();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      hRet = dmo.queryFixedData(sKey);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryFixedData(String) Exception!", e);
    }

    return hRet;
  }

  public Hashtable getPray2OrderRestrict(PraybillVO[] VOs)
    throws BusinessException
  {
    Hashtable t = null;
    try {
      PraybillDMO dmo = new PraybillDMO();
      t = dmo.queryPr2PoRestrictMode(VOs);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return t;
  }

  public PraybillVO[] queryForOrderBill(NormalCondVO[] voaNormal, ConditionVO[] voaDefined, UFDate logdate)
    throws BusinessException
  {
    String sQueryCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);
    String beanName = IScmPosInv.class.getName();
    IScmPosInv bo = (IScmPosInv)NCLocator.getInstance().lookup(beanName);

    String[] saQueryCorp = OrderPubDMO.getPkCorpsQueryFromNormalVOs(voaNormal);

    String loginCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);

    String operator = OrderPubDMO.getOperatorFromNormalVOs(voaNormal);
    String sBizName = OrderPubDMO.getBizTypeNameFromNormalVOs(voaNormal);

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    int iLen = voaDefined.length;
    String tablecode = null;
    for (int i = 0; i < iLen; i++) {
      tablecode = voaDefined[i].getTableCodeForMultiTable();
      if ((tablecode == null) || (tablecode.trim().length() == 0)) {
        continue;
      }
      if ((tablecode.indexOf("invcode") >= 0) || (tablecode.indexOf("custcode") >= 0))
        voaDefined[i].setValue(voaDefined[i].getRefResult().getRefCode());
      else if ((tablecode.indexOf("invname") >= 0) || (tablecode.indexOf("custname") >= 0)) {
        voaDefined[i].setValue(voaDefined[i].getRefResult().getRefName());
      }

    }

    voaDefined = ConvertQueryCondition.getConvertedVO(voaDefined, sQueryCorp);
    String sDefinedSql = null;
    try {
      sDefinedSql = OrderPubDMO.getWhereSQL(new String[] { sQueryCorp }, voaDefined);

      String strBeRepKey = "AND bd_invmandoc.pk_corp";
      int iBeReplacedLen = "AND bd_invmandoc.pk_corp = '1001'".length();
      int iBeRepBgnPos = -1;
      String strLeft = null;
      String strRight = null;
      while (true) {
        iBeRepBgnPos = sDefinedSql.indexOf(strBeRepKey);
        if (iBeRepBgnPos < 0) {
          break;
        }
        strLeft = sDefinedSql.substring(0, iBeRepBgnPos);
        strRight = sDefinedSql.substring(iBeRepBgnPos + iBeReplacedLen, sDefinedSql.length());
        sDefinedSql = strLeft + strRight;
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    PraybillVO[] praybills = null;
    Vector v = new Vector();

    if (sDefinedSql == null)
      sDefinedSql = "(po_praybill.ipraytype = 1 or po_praybill.ipraytype = 2)";
    else {
      sDefinedSql = "(" + sDefinedSql + ") and (po_praybill.ipraytype = 1 or po_praybill.ipraytype = 2)";
    }
    if (sBizName != null) {
      sDefinedSql = sDefinedSql + " and (bd_busitype.businame = '" + sBizName + "')";
    }
    sDefinedSql = sDefinedSql + " AND (ISNULL(po_praybill_b.npraynum, 0) > ISNULL(po_praybill_b.naccumulatenum, 0))";

    if (saQueryCorp.length == 1) {
      sDefinedSql = sDefinedSql + " and po_praybill_b.pk_purcorp ='" + saQueryCorp[0] + "' ";
    } else {
      sDefinedSql = sDefinedSql + " and po_praybill_b.pk_purcorp in ('" + saQueryCorp[0] + "' ";
      for (int i = 1; i < saQueryCorp.length; i++) {
        sDefinedSql = sDefinedSql + ",'" + saQueryCorp[i] + "' ";
      }
      sDefinedSql = sDefinedSql + ") ";
    }

    String[] arySql = getSourceSql("po_praybill", null, sDefinedSql);

    String strWhere = "";
    String strTables = "";

    boolean bExistsClsDoc = arySql[0].indexOf("bd_invcl") >= 0;
    boolean bExistsBasDoc = arySql[0].indexOf("bd_invbasdoc") >= 0;
    boolean bJoinInvClassFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invcl.invclasscode") >= 0) || (strDataPowerSql.indexOf("bd_invcl.invclassname") >= 0));

    bJoinInvClassFlag &= !bExistsClsDoc;

    boolean bJoinInvFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_invbasdoc.invcode") >= 0) || (strDataPowerSql.indexOf("bd_invbasdoc.invname") >= 0));

    bJoinInvFlag &= !bExistsBasDoc;

    if (bJoinInvClassFlag) {
      strTables = strTables + ",bd_invcl";
      strWhere = strWhere + "bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl ";
      if (!bExistsBasDoc) {
        strTables = strTables + ",bd_invbasdoc";
        if (strWhere.trim().length() > 0) {
          strWhere = strWhere + "and ";
        }
        strWhere = strWhere + " po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ";
      }
    } else if (bJoinInvFlag) {
      strTables = strTables + ",bd_invbasdoc ";
      strWhere = strWhere + "po_praybill_b.cbaseid = bd_invbasdoc.pk_invbasdoc ";
    }
    boolean bExistsVendorDoc = arySql[0].indexOf("bd_cubasdoc") >= 0;
    boolean bJoinVenFlag = (strDataPowerSql != null) && ((strDataPowerSql.indexOf("bd_cubasdoc.custcode") >= 0) || (strDataPowerSql.indexOf("bd_cubasdoc.custname") >= 0));

    bJoinVenFlag &= !bExistsVendorDoc;
    if (bJoinVenFlag) {
      strTables = strTables + ",bd_cubasdoc";
      if (strWhere.trim().length() > 0) {
        strWhere = strWhere + "and ";
      }
      strWhere = strWhere + "(po_praybill_b.cvendorbaseid is null or po_praybill_b.cvendorbaseid = bd_cubasdoc.pk_cubasdoc) ";
    }
    if ((strDataPowerSql != null) && (strDataPowerSql.indexOf("bd_jobbasfil") >= 0) && (arySql[0].indexOf("bd_jobbasfil") < 0))
    {
      strTables = strTables + ",bd_jobmngfil,bd_jobbasfil ";
      if (strWhere.trim().length() > 0) {
        strWhere = strWhere + "and ";
      }
      strWhere = strWhere + "(po_praybill_b.cprojectid is null or po_praybill_b.cprojectid = bd_jobmngfil.pk_jobmngfil) ";
      strWhere = strWhere + "and bd_jobbasfil.pk_jobbasfil = bd_jobmngfil.pk_jobbasfil ";
    }
    int tmp1247_1246 = 0;
    String[] tmp1247_1244 = arySql; tmp1247_1244[tmp1247_1246] = (tmp1247_1244[tmp1247_1246] + strTables + " ");
    arySql[1] = ("where " + arySql[1] + " ");
    if (strWhere.trim().length() > 0)
    {
      int tmp1319_1318 = 1;
      String[] tmp1319_1316 = arySql; tmp1319_1316[tmp1319_1318] = (tmp1319_1316[tmp1319_1318] + "and " + strWhere + " ");
    }
    if (strDataPowerSql != null)
    {
      int tmp1359_1358 = 1;
      String[] tmp1359_1356 = arySql; tmp1359_1356[tmp1359_1358] = (tmp1359_1356[tmp1359_1358] + "and " + strDataPowerSql + " ");
    }

    try
    {
      PraybillDMO dmo = new PraybillDMO();

      CircularlyAccessibleValueObject[] hvos = dmo.queryAllHeadData(arySql[0], arySql[1]);

      if ((hvos != null) && (hvos.length > 0))
      {
        sDefinedSql = sDefinedSql + " and (po_praybill_b.npraynum is not null)";

        arySql = getSourceSql("po_praybill", "po_praybill_b", sDefinedSql);
        int tmp1464_1463 = 0;
        String[] tmp1464_1461 = arySql; tmp1464_1461[tmp1464_1463] = (tmp1464_1461[tmp1464_1463] + strTables + " ");
        arySql[1] = ("where " + arySql[1] + " ");
        if (strWhere.trim().length() > 0)
        {
          int tmp1536_1535 = 1;
          String[] tmp1536_1533 = arySql; tmp1536_1533[tmp1536_1535] = (tmp1536_1533[tmp1536_1535] + "and " + strWhere + " ");
        }
        if (strDataPowerSql != null)
        {
          int tmp1576_1575 = 1;
          String[] tmp1576_1573 = arySql; tmp1576_1573[tmp1576_1575] = (tmp1576_1573[tmp1576_1575] + "and " + strDataPowerSql + " ");
        }

        String s = bo.getInvSqlByPurchaser(loginCorp, operator);
        if ((s != null) && (s.trim().length() > 0)) {
          arySql[1] = (arySql[1] + " and po_praybill_b.cbaseid in(" + s + ") ");
        }

        PraybillItemVO[] bvos = (PraybillItemVO[])(PraybillItemVO[])dmo.queryAllBodyData(arySql[0], arySql[1]);
        if ((bvos == null) || (bvos.length == 0)) {
          SCMEnv.out("符合条件表体数据为空，直接返回，请检查集采规则设置或用户表体查询条件设置!");
          return null;
        }

        UFBoolean ufbFilter = OrderPubDMO.getFilterFlagFromNormalVOs(voaNormal);
        if (ufbFilter.booleanValue()) {
          bvos = getHasNassNumBody(bvos);
        }
        if ((bvos == null) || (bvos.length == 0)) return null;

        for (int i = 0; i < hvos.length; i++) {
          PraybillHeaderVO headVO = (PraybillHeaderVO)hvos[i];
          Vector v1 = new Vector();
          for (int j = 0; j < bvos.length; j++) {
            PraybillItemVO bodyVO = bvos[j];
            if (!bodyVO.getCpraybillid().equals(headVO.getCpraybillid())) continue; v1.addElement(bodyVO);
          }
          if (v1.size() > 0) {
            PraybillItemVO[] bodyVOs = new PraybillItemVO[v1.size()];
            v1.copyInto(bodyVOs);
            PraybillVO VO = new PraybillVO(v1.size());
            VO.setParentVO(headVO);
            VO.setChildrenVO(bodyVOs);
            v.addElement(VO);
          }
        }

        if (v.size() == 0) return null;
        praybills = new PraybillVO[v.size()];
        v.copyInto(praybills);
        v = new Vector();
        Vector v1 = new Vector(); Vector v2 = new Vector();
        ArrayList list = new ArrayList();
        for (int i = 0; i < praybills.length; i++) {
          v.addElement(praybills[i].getHeadVO());
          PraybillItemVO[] bodyVO = praybills[i].getBodyVO();
          for (int j = 0; j < bodyVO.length; j++) {
            v1.addElement(bodyVO[j].getCmangid());
            v2.addElement(bodyVO[j].getPk_purcorp());
            list.add(bodyVO[j].getCpraybill_bid());
          }
        }
        PraybillHeaderVO[] headVOs = new PraybillHeaderVO[v.size()];
        v.copyInto(headVOs);

        Hashtable hPray2OrderRestrict = dmo.queryPr2PoRestrictMode(praybills);

        EffectPriceParaVO tempVO = new EffectPriceParaVO();
        String[] cmangid = new String[v1.size()];
        v1.copyInto(cmangid);
        String[] pk_purcorp = new String[v2.size()];
        v2.copyInto(pk_purcorp);
        tempVO.setCmangid(cmangid);
        tempVO.setPkcorps(pk_purcorp);
        tempVO.setDate(logdate);
        tempVO.setPk_corp(loginCorp);

        Hashtable result1 = new AskbillImpl().checkIsEffectPrice(tempVO);
        Hashtable result2 = new AskbillImpl().queryIsGenPriceAudit(list);

        v = new Vector();
        for (int i = 0; i < praybills.length; i++) {
          v1 = new Vector();
          PraybillHeaderVO headVO = praybills[i].getHeadVO();
          PraybillItemVO[] bodyVO = praybills[i].getBodyVO();
          for (int j = 0; j < bodyVO.length; j++) {
            Integer k = (Integer)hPray2OrderRestrict.get(headVO.getCbiztype() + bodyVO[j].getPk_purcorp());
            if ((k == null) || (k.intValue() == 0))
            {
              v1.addElement(bodyVO[j]);
            } else if (k.intValue() == 1)
            {
              if ((result1 == null) || (result1.size() <= 0) || 
                (result1.get(bodyVO[j].getCbaseid()) == null)) continue;
              Object oTemp = result1.get(bodyVO[j].getCbaseid());
              if ((oTemp != null) && (((UFBoolean)oTemp).booleanValue())) {
                v1.addElement(bodyVO[j]);
              }

            }
            else
            {
              if ((result2 == null) || (result2.size() <= 0) || 
                (result2.get(bodyVO[j].getCpraybill_bid()) == null)) continue;
              Object oTemp = result2.get(bodyVO[j].getCpraybill_bid());
              if ((oTemp != null) && (((UFBoolean)oTemp).booleanValue())) {
                v1.addElement(bodyVO[j]);
              }

            }

          }

          if (v1.size() > 0) {
            PraybillItemVO[] bodyVOs = new PraybillItemVO[v1.size()];
            v1.copyInto(bodyVOs);
            PraybillVO VO = new PraybillVO(v1.size());
            VO.setParentVO(headVO);
            VO.setChildrenVO(bodyVOs);
            v.addElement(VO);
          }
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    if (v.size() > 0) {
      praybills = new PraybillVO[v.size()];
      v.copyInto(praybills);
      UFBoolean ufbSplitFlag = OrderPubDMO.getSplitFlagFromNormalVOs(voaNormal);
      if ((ufbSplitFlag != null) && (ufbSplitFlag.booleanValue())) {
        try {
          praybills = new PraybillDMO().splitVOForPO(praybills);
        } catch (NamingException e) {
          SCMEnv.out(e);
          throw new BusinessException(e.getMessage());
        } catch (SystemException e) {
          SCMEnv.out(e);
          throw new BusinessException(e.getMessage());
        }
      }
      return praybills;
    }

    return null;
  }

  private PraybillItemVO[] getHasNassNumBody(PraybillItemVO[] voaBody)
    throws Exception
  {
    if ((voaBody == null) || (voaBody.length == 0)) return null;

    String strPrayCorpId = voaBody[0].getAttributeValue("pk_corp").toString();

    int[] iaDigit = new PubImpl().getDigitBatch(strPrayCorpId, new String[] { "BD501", "BD502", "BD503" });
    int iDigitNum = iaDigit[0];
    int iDigitAssNum = iaDigit[1];
    int iDigitExchg = iaDigit[0];

    double dBaseNum = 0.1D;
    for (int i = 1; i < iDigitAssNum; i++) {
      dBaseNum *= 0.1D;
    }

    voaBody = getItemWithMny(voaBody);

    int iLen = voaBody.length;
    UFDouble ufdNotExecNum = null;
    UFDouble ufdNotExecAssNum = null;
    UFDouble ufdExchg = null;
    ArrayList listRetVos = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if ((voaBody[i].getCassistunit() == null) || (voaBody[i].getNexchangerate() == null) || (voaBody[i].getNexchangerate().doubleValue() == 0.0D))
      {
        listRetVos.add(voaBody[i]);
      }
      else {
        ufdNotExecNum = voaBody[i].getNpraynum().sub(PuPubVO.getUFDouble_NullAsZero(voaBody[i].getNaccumulatenum()));
        ufdExchg = voaBody[i].getNexchangerate();
        ufdNotExecAssNum = ufdNotExecNum.div(ufdExchg);
        if (ufdNotExecAssNum.doubleValue() >= dBaseNum)
          listRetVos.add(voaBody[i]);
      }
    }
    if (listRetVos.size() == 0) {
      return null;
    }
    if (listRetVos.size() == iLen) {
      return voaBody;
    }
    PraybillItemVO[] voaRet = (PraybillItemVO[])listRetVos.toArray(new PraybillItemVO[listRetVos.size()]);
    return voaRet;
  }

  private UFDouble getUFDByDigit(UFDouble ufd, int digit_exchange)
  {
    String s_value = ufd.toString();

    int i_dotpos = s_value.indexOf(".");
    if (i_dotpos < 0) {
      return new UFDouble(s_value);
    }
    if (s_value.substring(i_dotpos + 1).length() <= digit_exchange) {
      return new UFDouble(s_value);
    }
    char c_last = s_value.charAt(s_value.length() - 1);
    char c_lasttwo = s_value.charAt(s_value.length() - 2);
    if (c_last >= '5') {
      c_lasttwo = (char)(c_lasttwo + '\001');
    }
    return new UFDouble(s_value.substring(0, s_value.length() - 2) + String.valueOf(c_lasttwo));
  }

  private boolean compareNumAndDigit(UFDouble ufd, int digit)
  {
    String s_value = ufd.toString();

    int i_dotpos = s_value.indexOf(".");
    if (i_dotpos < 0) {
      return true;
    }
    String s_beforedot = s_value.substring(0, i_dotpos);
    if ((s_beforedot.length() > 1) || (s_beforedot.charAt(0) > '0')) {
      return true;
    }

    int i_zeronum = 0;
    String s_afterdot = s_value.substring(i_dotpos + 1);
    int i = 0; for (int len = s_afterdot.length(); (i < len) && 
      (s_afterdot.charAt(i) <= '0'); i++)
    {
      i_zeronum++;
    }

    if (i_zeronum < digit) {
      return true;
    }
    if (i_zeronum > digit) {
      return false;
    }

    return s_afterdot.charAt(i_zeronum) >= '5';
  }

  public ArrayList queryPrayForSaveAudit(String key)
    throws BusinessException
  {
    ArrayList arr = new ArrayList();
    ArrayList arrBodyTs = new ArrayList();
    try {
      PraybillDMO dmo = new PraybillDMO();
      arr = dmo.queryForSaveAudit(key);
      arrBodyTs = dmo.queryForSaveAuditBody(key);
      arr.add(arrBodyTs);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.queryForSaveAudit(String)!", e);
    }

    return arr;
  }

  public ImplementVO queryImplementDetail(String unitCode, ConditionVO[] conditionVO, Boolean bPOSelected, Integer iBillStatus, String subSql)
    throws BusinessException
  {
    ImplementItemVO[] bVO = null;
    ImplementItemVO[] retItems = null;
    ImplementHeaderVO hhVO = null;
    Vector v = new Vector();

    String sOperator = null;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      Timer timer = new Timer();
      timer.start("订单执行明细");

      ArrayList listRet = OrderPubDMO.dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])(ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      String[] sTemp = getSQLByConditionVOs(conditionVO, null);
      String strSqlPart = sTemp[0] + " ";

      if (subSql != null) {
        strSqlPart = strSqlPart + " and ";
        strSqlPart = strSqlPart + subSql + " ";
      }

      if (iBillStatus.intValue() < 5) {
        strSqlPart = strSqlPart + " and ";
        strSqlPart = strSqlPart + "po_praybill.ibillstatus = " + iBillStatus.intValue() + " ";
      }

      PraybillHeaderVO[] headVO = dmo.queryHeadImplement_p(unitCode, strSqlPart, bPOSelected, strDataPowerSql);

      if ((headVO == null) || (headVO.length == 0)) {
        return null;
      }
      hhVO = new ImplementHeaderVO();
      UFDate dPrayDate = headVO[0].getDpraydate();
      for (int i = 1; i < headVO.length; i++) {
        UFDate dTemp = headVO[i].getDpraydate();
        if ((dTemp == null) || (dTemp.toString().trim().equals("")) || (dPrayDate.compareTo(dTemp) <= 0))
          continue;
        dPrayDate = dTemp;
      }
      hhVO.setDpraydate(dPrayDate);
      dPrayDate = headVO[0].getDpraydate();
      for (int i = 1; i < headVO.length; i++) {
        UFDate dTemp = headVO[i].getDpraydate();
        if ((dTemp == null) || (dTemp.toString().trim().equals("")) || (dPrayDate.compareTo(dTemp) >= 0))
          continue;
        dPrayDate = dTemp;
      }
      hhVO.setDpraydate_t(dPrayDate);

      Vector vv1 = new Vector();
      for (int i = 0; i < headVO.length; i++) {
        vv1.addElement(headVO[i].getAttributeValue("cpraybillid").toString());
      }

      String[] saHid = new String[vv1.size()];
      vv1.copyInto(saHid);

      ImplementItemVO[] implementBs = dmo.queryBodyImpDetail(unitCode, saHid, bPOSelected, strSqlPart, sOperator, strDataPowerSql);

      ImplementItemVO[] replenishBs = null;

      if (bPOSelected.booleanValue()) {
        replenishBs = dmo.queryBodyReplenishDetail(null, sOperator);
      }

      if ((replenishBs != null) && (replenishBs.length > 0)) {
        for (int i = 0; i < replenishBs.length; i++) {
          String sCsourcebillid = replenishBs[i].getCsourcebillid();
          if ((sCsourcebillid == null) || (sCsourcebillid.trim().length() == 0)) {
            continue;
          }
          String sCsourcerowid = replenishBs[i].getCsourcerowid();
          if ((sCsourcerowid == null) || (sCsourcerowid.trim().length() == 0)) {
            continue;
          }
          UFDouble orderNum = replenishBs[i].getNordernum();
          if (orderNum == null)
            continue;
          if (implementBs == null)
            continue;
          int count = implementBs.length;
          if (count == 0)
            continue;
          for (int j = 0; j < count; j++) {
            if ((!sCsourcebillid.equals(implementBs[j].getCorderid())) || (!sCsourcerowid.equals(implementBs[j].getCorder_bid()))) {
              continue;
            }
            implementBs[j].setNreplenishnum(orderNum);
            break;
          }
        }

      }

      Hashtable hIsRel = PubDMO.getHashBodyByHeadKey(implementBs, "cpraybillid");

      implementBs = dmo.queryBodyImplementDetail_e(unitCode, saHid, strSqlPart, sOperator, strDataPowerSql);

      Hashtable hNoRel = PubDMO.getHashBodyByHeadKey(implementBs, "cpraybillid");

      String strDeptName = null;
      String strPsnName = null;

      ImplementItemVO[] bodyVO = null;
      for (int i = 0; i < headVO.length; i++)
      {
        strDeptName = headVO[i].getCdeptid();
        strPsnName = headVO[i].getCpraypsn();

        String s1 = headVO[i].getCpraybillid();
        bodyVO = null;
        if ((hIsRel != null) && (hIsRel.size() > 0)) {
          bodyVO = (ImplementItemVO[])(ImplementItemVO[])hIsRel.get(headVO[i].getPrimaryKey());
        }

        if ((bodyVO != null) && (bodyVO.length > 0)) {
          Hashtable t = new Hashtable();
          for (int j = 0; j < bodyVO.length; j++)
          {
            bodyVO[j].setCdeptname(strDeptName);
            bodyVO[j].setCpsnname(strPsnName);

            UFDouble d1 = bodyVO[j].getNpraynum();
            UFDouble d2 = bodyVO[j].getNsuggestprice();
            if ((d1 != null) && (d2 != null)) {
              double d = d2.doubleValue() * d1.doubleValue();
              bodyVO[j].setNsuggestmoney(new UFDouble(d));
            }

            String s2 = bodyVO[j].getCpraybillid();

            if ((headVO[i].getIbillstatus().intValue() != 1) || (!s1.equals(s2)))
              continue;
            d2 = bodyVO[j].getNordernum();
            if (d2 == null)
              d2 = new UFDouble(0);
            if (t.containsKey(bodyVO[j].getCpraybill_bid())) {
              UFDouble dd = (UFDouble)t.get(bodyVO[j].getCpraybill_bid());

              dd = new UFDouble(d2.doubleValue() + dd.doubleValue());

              t.put(bodyVO[j].getCpraybill_bid(), dd);
            } else {
              t.put(bodyVO[j].getCpraybill_bid(), d2);
            }

          }

          for (int j = 0; j < bodyVO.length; j++) {
            UFDouble d1 = bodyVO[j].getNpraynum();
            String s2 = bodyVO[j].getCpraybillid();
            if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
            {
              if (t.containsKey(bodyVO[j].getCpraybill_bid())) {
                UFDouble dd = (UFDouble)t.get(bodyVO[j].getCpraybill_bid());

                if (d1.doubleValue() - dd.doubleValue() >= 0.0D) {
                  bodyVO[j].setNnopraynum(new UFDouble(d1.doubleValue() - dd.doubleValue()));
                }
                else
                {
                  bodyVO[j].setNnopraynum(new UFDouble(0));
                }
              }
            }
            v.addElement(bodyVO[j]);
          }
        }

        bodyVO = null;
        if ((hNoRel != null) && (hNoRel.size() > 0)) {
          bodyVO = (ImplementItemVO[])(ImplementItemVO[])hNoRel.get(headVO[i].getPrimaryKey());
        }

        if ((bodyVO != null) && (bodyVO.length > 0)) {
          Hashtable t = new Hashtable();
          for (int j = 0; j < bodyVO.length; j++)
          {
            bodyVO[j].setCdeptname(strDeptName);
            bodyVO[j].setCpsnname(strPsnName);

            UFDouble d1 = bodyVO[j].getNpraynum();
            UFDouble d2 = bodyVO[j].getNsuggestprice();
            if ((d1 != null) && (d2 != null)) {
              double d = d2.doubleValue() * d1.doubleValue();
              bodyVO[j].setNsuggestmoney(new UFDouble(d));
            }

            String s2 = bodyVO[j].getCpraybillid();
            if ((headVO[i].getIbillstatus().intValue() != 1) || (!s1.equals(s2)))
              continue;
            d2 = bodyVO[j].getNordernum();
            if (d2 == null) {
              d2 = new UFDouble(0);
            }

            if (t.containsKey(bodyVO[j].getCpraybill_bid())) {
              UFDouble dd = (UFDouble)t.get(bodyVO[j].getCpraybill_bid());

              dd = new UFDouble(d2.doubleValue() + dd.doubleValue());

              t.put(bodyVO[j].getCpraybill_bid(), dd);
            } else {
              t.put(bodyVO[j].getCpraybill_bid(), d2);
            }

          }

          for (int j = 0; j < bodyVO.length; j++) {
            UFDouble d1 = bodyVO[j].getNpraynum();
            String s2 = bodyVO[j].getCpraybillid();
            if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
            {
              if (t.containsKey(bodyVO[j].getCpraybill_bid())) {
                UFDouble dd = (UFDouble)t.get(bodyVO[j].getCpraybill_bid());

                if (d1.doubleValue() - dd.doubleValue() >= 0.0D) {
                  bodyVO[j].setNnopraynum(new UFDouble(d1.doubleValue() - dd.doubleValue()));
                }
                else
                {
                  bodyVO[j].setNnopraynum(new UFDouble(0));
                }
              }
            }
            v.addElement(bodyVO[j]);
          }
        }
      }
      bVO = new ImplementItemVO[v.size()];
      v.copyInto(bVO);
      if ((bVO == null) || (bVO.length == 0)) {
        return null;
      }

      String[] sArriveDate = dmo.queryHeadImplement_a(bVO, sOperator, unitCode);

      String strTmp = null;
      if ((sArriveDate != null) && (sArriveDate.length > 0)) {
        for (int i = 0; i < bVO.length; i++) {
          String sKey1 = bVO[i].getCorderid();
          String sKey2 = bVO[i].getCorder_bid();
          if ((sKey1 == null) || (sKey1.trim().length() == 0) || (sKey2 == null) || (sKey2.trim().length() == 0)) {
            continue;
          }
          strTmp = sArriveDate[i];
          if ((strTmp != null) && (strTmp.trim().length() > 0)) {
            bVO[i].setDarrvdate(new UFDate(strTmp));
          }

        }

      }

      String cpraybillidtmp = null;
      String sTmpKey2 = null;
      int bVOLength = bVO.length;

      if ((bVO != null) && (bVOLength > 0))
      {
        String sBid = null;

        String[] sbid = new String[bVO.length];

        for (int i = 0; i < bVOLength; i++) {
          sBid = bVO[i].getCorder_bid();
          if ((sBid != null) && (!sBid.trim().equals(""))) {
            sbid[i] = sBid;
          }
        }

        String[] sInvDate = dmo.queryHeadImplement_i(sbid, bVO, bVOLength, sOperator, unitCode);

        if ((sInvDate != null) && (sInvDate.length > 0))
        {
          for (int i = 0; i < bVOLength; i++) {
            sTmpKey2 = bVO[i].getCorder_bid();
            if ((sTmpKey2 == null) || (sTmpKey2.trim().length() == 0))
              continue;
            cpraybillidtmp = sInvDate[i];
            if ((cpraybillidtmp == null) || (cpraybillidtmp.trim().length() <= 0))
              continue;
            bVO[i].setDaccumstoredate(new UFDate(cpraybillidtmp));
          }

        }

      }

      ArrayList list3 = dmo.queryHeadImplement_o(unitCode, bVO, bPOSelected, sOperator);

      ArrayList list4 = null;
      Object o = null;
      if ((list3 != null) && (list3.size() > 0)) {
        for (int i = 0; i < bVO.length; i++) {
          String sKey = bVO[i].getCorderid();
          if ((sKey == null) || (sKey.trim().equals("")))
            continue;
          list4 = (ArrayList)list3.get(i);
          if ((list4 == null) || (list4.size() == 0))
            continue;
          o = list4.get(0);
          if (o != null)
            bVO[i].setCorderid(o.toString());
          o = list4.get(1);
          if (o != null)
            bVO[i].setDorderdate(new UFDate(o.toString()));
          o = list4.get(2);
          if (o != null)
            bVO[i].setCvendormangid_o(o.toString());
          o = list4.get(3);
          if (o != null) {
            bVO[i].setCvendorbaseid_o(o.toString());
          }
        }
      }
      for (int i = 0; i < bVO.length; i++) {
        String sKey1 = bVO[i].getCpraybillid();
        for (int j = 0; j < headVO.length; j++) {
          String sKey2 = headVO[j].getCpraybillid();
          if ((sKey1 == null) || (sKey2 == null) || (sKey1.toString().trim().length() <= 0) || (sKey2.toString().trim().length() <= 0)) {
            continue;
          }
          if (sKey1.equals(sKey2)) {
            bVO[i].setCpraybillid(headVO[j].getVpraycode());
            break;
          }
        }
      }

      Vector vId = new Vector();
      String sBid = null;
      vId.addElement(bVO[0].getCpraybill_bid());
      for (int j = 1; j < bVO.length; j++) {
        sBid = bVO[j].getCpraybill_bid();
        if (!vId.contains(sBid)) {
          vId.add(sBid);
        }
        else {
          bVO[j].setCrowno(null);
          bVO[j].setCmangid(null);
          bVO[j].setCbaseid(null);
          bVO[j].setCpsnname(null);
          bVO[j].setCdeptname(null);

          bVO[j].setCpraybillid(null);
          bVO[j].setCprojectid(null);
          bVO[j].setCprojectphaseid(null);
          bVO[j].setCvendorbaseid_p(null);
          bVO[j].setCvendormangid_p(null);
          bVO[j].setDdemanddate(null);
          bVO[j].setDsuggestdate(null);
          bVO[j].setNpraynum(null);
          bVO[j].setNsuggestmoney(null);
          bVO[j].setNsuggestprice(null);
        }
      }

      Vector vRetBodyVOs = new Vector();
      if ((bVO != null) && (bVO.length > 0)) {
        for (int i = 0; i < bVO.length; i++) {
          if ((bVO[i].getCmangid() == null) && ((bVO[i].getCorder_bid() == null) || (bVO[i].getCorder_bid().trim().equals("")))) {
            continue;
          }
          vRetBodyVOs.addElement(bVO[i]);
        }
      }

      if (vRetBodyVOs.size() > 0) {
        retItems = new ImplementItemVO[vRetBodyVOs.size()];
        vRetBodyVOs.copyInto(retItems);
      }

      timer.stopAndShow("订单执行明细");
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryImplement(String,String) Exception!", e);
    }

    if ((hhVO != null) && (retItems != null) && (retItems.length > 0)) {
      ImplementVO returnVO = new ImplementVO();
      returnVO.setParentVO(hhVO);
      returnVO.setChildrenVO(retItems);
      return returnVO;
    }
    return null;
  }

  public ImplementVO queryImplementTotal(String[] unitCode, ConditionVO[] conditionVO, int nTotal, Boolean bPOSelected, Integer iBillStatus, String subSql)
    throws BusinessException
  {
    ImplementItemVO[] bVO = null;
    ImplementHeaderVO hhVO = null;
    ImplementItemVO[] bgVO = null;
    Vector v = new Vector();
    Vector vg = new Vector();

    String sOperator = null;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      Timer timer = new Timer();
      timer.start("订单执行汇总");

      ArrayList listRet = OrderPubDMO.dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])(ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      String[] sTemp = getSQLByConditionVOs(conditionVO, null);
      String strSqlPart = sTemp[0] + " ";

      if (subSql != null) {
        strSqlPart = strSqlPart + " and ";
        strSqlPart = strSqlPart + subSql + " ";
      }

      if (iBillStatus.intValue() < 5) {
        strSqlPart = strSqlPart + " and ";
        strSqlPart = strSqlPart + "po_praybill.ibillstatus = " + iBillStatus.intValue() + " ";
      }

      StringBuffer sUnit = new StringBuffer("");
      if ((unitCode != null) && (unitCode.length > 0)) {
        sUnit.append(" ('");
        for (int i = 0; i < unitCode.length; i++) {
          if (i != unitCode.length - 1)
            sUnit.append(unitCode[i] + "', '");
          else {
            sUnit.append(unitCode[i] + "') ");
          }
        }
      }
      PraybillHeaderVO[] headVO = dmo.queryHeadImplement_p(sUnit.toString(), strSqlPart, bPOSelected, strDataPowerSql);

      if ((headVO == null) || (headVO.length == 0)) {
        return null;
      }
      hhVO = new ImplementHeaderVO();
      UFDate dPrayDate = headVO[0].getDpraydate();
      for (int i = 1; i < headVO.length; i++) {
        UFDate dTemp = headVO[i].getDpraydate();
        if ((dTemp == null) || (dTemp.toString().trim().equals("")) || (dPrayDate.compareTo(dTemp) <= 0))
          continue;
        dPrayDate = dTemp;
      }
      hhVO.setDpraydate(dPrayDate);
      dPrayDate = headVO[0].getDpraydate();
      for (int i = 1; i < headVO.length; i++) {
        UFDate dTemp = headVO[i].getDpraydate();
        if ((dTemp == null) || (dTemp.toString().trim().equals("")) || (dPrayDate.compareTo(dTemp) >= 0))
          continue;
        dPrayDate = dTemp;
      }
      hhVO.setDpraydate_t(dPrayDate);

      if (nTotal == 0)
      {
        Vector vv1 = new Vector();
        for (int i = 0; i < headVO.length; i++) {
          vv1.addElement(headVO[i].getPrimaryKey());
        }
        String[] saHid = new String[vv1.size()];
        vv1.copyInto(saHid);

        ImplementItemVO[] items = dmo.queryBodyImplementFirst0(sUnit.toString(), saHid, bPOSelected, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hFirst0 = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");
        items = dmo.queryBodyImplementTotal0(sUnit.toString(), saHid, bPOSelected, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hTotal0 = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

        ImplementItemVO[] replenishBs = null;
        if (bPOSelected.booleanValue()) {
          replenishBs = dmo.queryBodyReplenishDetail(null, sOperator);
        }

        items = dmo.queryBodyImplementTotal0_e(sUnit.toString(), saHid, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hTotal0e = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

        ImplementItemVO[] bodyVO = null;
        ImplementItemVO[] bodygVO = null;
        for (int i = 0; i < headVO.length; i++)
        {
          bodyVO = null;
          if ((hFirst0 != null) && (hFirst0.size() > 0)) {
            bodyVO = (ImplementItemVO[])(ImplementItemVO[])hFirst0.get(headVO[i].getPrimaryKey());
          }

          if ((bodyVO != null) && (bodyVO.length > 0))
          {
            String[] sArriveDate = dmo.queryPlanArriveDate(bodyVO, bPOSelected, sUnit.toString(), sOperator);

            for (int j = 0; j < bodyVO.length; j++) {
              String s = sArriveDate[j];
              if ((s != null) && (s.length() > 0)) {
                bodyVO[j].setDplanarrvdate(new UFDate(s));
              }
              bodyVO[j].setVpraycode(headVO[i].getVpraycode());

              UFDouble d1 = bodyVO[j].getNpraynum();
              if (d1 == null)
                d1 = new UFDouble(0);
              UFDouble d2 = new UFDouble(0);
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodyVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
              {
                d2 = bodyVO[j].getNordernum();
                if (d2 == null)
                  d2 = new UFDouble(0);
                double d3 = d1.doubleValue() - d2.doubleValue();

                if (d3 < 0.0D) {
                  d3 = 0.0D;
                }
                bodyVO[j].setNnopraynum(new UFDouble(d3));
              }
              v.addElement(bodyVO[j]);
            }
          }

          bodygVO = null;
          if ((hTotal0 != null) && (hTotal0.size() > 0)) {
            bodygVO = (ImplementItemVO[])(ImplementItemVO[])hTotal0.get(headVO[i].getPrimaryKey());
          }

          if ((bodygVO != null) && (bodygVO.length > 0)) {
            for (int j = 0; j < bodygVO.length; j++)
            {
              UFDouble dPrayNum = bodygVO[j].getNpraynum();
              if (dPrayNum == null)
                dPrayNum = new UFDouble(0);
              UFDouble dOrderNum = new UFDouble(0);
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodygVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
              {
                dOrderNum = bodygVO[j].getNordernum();
                if (dOrderNum == null)
                  dOrderNum = new UFDouble(0);
                double d3 = dPrayNum.doubleValue() - dOrderNum.doubleValue();

                if (d3 < 0.0D) {
                  d3 = 0.0D;
                }
                bodygVO[j].setNnopraynum(new UFDouble(d3));
              }
              vg.addElement(bodygVO[j]);
            }
          }

          bodygVO = null;
          if ((hTotal0e != null) && (hTotal0e.size() > 0)) {
            bodygVO = (ImplementItemVO[])(ImplementItemVO[])hTotal0e.get(headVO[i].getPrimaryKey());
          }

          Hashtable t = new Hashtable();

          if ((bodygVO != null) && (bodygVO.length > 0)) {
            for (int j = 0; j < bodygVO.length; j++)
            {
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodygVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() != 1) || (!s1.equals(s2)))
                continue;
              UFDouble d2 = bodygVO[j].getNordernum();
              if (d2 == null)
                d2 = new UFDouble(0);
              if (t.containsKey(bodygVO[j].getCpraybill_bid())) {
                UFDouble dd = (UFDouble)t.get(bodygVO[j].getCpraybill_bid());

                double d3 = d2.doubleValue() + dd.doubleValue();

                if (d3 < 0.0D) {
                  d3 = 0.0D;
                }
                dd = new UFDouble(d3);
                t.put(bodygVO[j].getCpraybill_bid(), dd);
              } else {
                t.put(bodygVO[j].getCpraybill_bid(), d2);
              }

            }

            for (int j = 0; j < bodygVO.length; j++) {
              UFDouble d1 = bodygVO[j].getNpraynum();
              if (d1 == null)
                d1 = new UFDouble(0);
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodygVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
              {
                if (t.containsKey(bodygVO[j].getCpraybill_bid()))
                {
                  UFDouble dd = (UFDouble)t.get(bodygVO[j].getCpraybill_bid());

                  if (dd == null)
                    dd = new UFDouble(0);
                  bodygVO[j].setNnopraynum(new UFDouble(d1.doubleValue() - dd.doubleValue()));
                }

              }

              vg.addElement(bodygVO[j]);
            }
          }
        }
        bVO = new ImplementItemVO[v.size()];
        v.copyInto(bVO);
        bgVO = new ImplementItemVO[vg.size()];
        vg.copyInto(bgVO);

        for (int i = 0; i < bgVO.length; i++) {
          String sKey1 = bgVO[i].getCpraybill_bid().trim();
          boolean flag = true;

          UFDouble replenishNum = new UFDouble(0.0D);
          for (int j = 0; j < bVO.length; j++) {
            String sKey2 = bVO[j].getCpraybill_bid().trim();
            if ((flag) && (sKey1.equals(sKey2)))
            {
              bgVO[i].setCrowno(bVO[j].getCrowno());

              bgVO[i].setVpraycode(bVO[j].getVpraycode());

              bgVO[i].setCmangid(bVO[j].getCmangid());
              bgVO[i].setCbaseid(bVO[j].getCbaseid());
              bgVO[i].setVdef1(bVO[j].getVdef1());
              bgVO[i].setVfree1(bVO[j].getVfree1());
              bgVO[i].setNpraynum(bVO[j].getNpraynum());
              bgVO[i].setNsuggestprice(bVO[j].getNsuggestprice());
              bgVO[i].setDdemanddate(bVO[j].getDdemanddate());
              bgVO[i].setDsuggestdate(bVO[j].getDsuggestdate());
              bgVO[i].setCvendormangid_p(bVO[j].getCvendormangid_p());

              bgVO[i].setCvendorbaseid_p(bVO[j].getCvendorbaseid_p());

              bgVO[i].setCprojectid(bVO[j].getCprojectid());
              bgVO[i].setCprojectphaseid(bVO[j].getCprojectphaseid());

              bgVO[i].setCwarehouseid(bVO[j].getCwarehouseid());
              bgVO[i].setDplanarrvdate(bVO[j].getDplanarrvdate());

              bgVO[i].setCupsourcebilltype(bVO[j].getCupsourcebilltype());

              bgVO[i].setCupsourcebillid(bVO[j].getCupsourcebillid());

              bgVO[i].setCupsourcebillrowid(bVO[j].getCupsourcebillrowid());

              bgVO[i].setCvendormangid_o(bVO[j].getCvendormangid_o());

              bgVO[i].setVordercode(bVO[j].getVordercode());
              bgVO[i].setDorderdate(bVO[j].getDorderdate());

              UFDouble d1 = bVO[j].getNpraynum();
              UFDouble d2 = bVO[j].getNsuggestprice();
              if ((d1 != null) && (d2 != null)) {
                double d = d1.doubleValue() * d2.doubleValue();
                bgVO[i].setNsuggestmoney(new UFDouble(d));
              }

              d1 = bgVO[i].getNmoney();
              d2 = bgVO[i].getNordernum();
              if ((d1 != null) && (d2 != null) && 
                (d2.doubleValue() > 0.0D)) {
                double d = d1.doubleValue() / d2.doubleValue();

                bgVO[i].setNprice(new UFDouble(d));
              }
              flag = false;

              bgVO[i].setCpurorganization(bVO[j].getCpurorganization());
              bgVO[i].setPk_purcorp(bVO[j].getPk_purcorp());
              bgVO[i].setPk_reqcorp(bVO[j].getPk_reqcorp());
              bgVO[i].setPk_reqstoorg(bVO[j].getPk_reqstoorg());
            }

            if (!sKey1.equals(sKey2))
              continue;
            if (bVO[j].getNreplenishnum() != null) {
              UFDouble ufReplenishNum = bgVO[i].getNreplenishnum();

              if (ufReplenishNum != null) {
                ufReplenishNum = ufReplenishNum.add(bVO[j].getNreplenishnum());
              }
              else
                ufReplenishNum = bVO[j].getNreplenishnum();
              bgVO[i].setNreplenishnum(ufReplenishNum);
            }

            if ((replenishBs == null) || (replenishBs.length == 0))
              continue;
            int count = replenishBs.length;
            for (int k = 0; k < count; k++) {
              String sCsourcerowid = replenishBs[k].getCsourcerowid();

              if ((sCsourcerowid == null) || (sCsourcerowid.trim().length() == 0)) {
                continue;
              }
              UFDouble orderNum = replenishBs[k].getNordernum();
              if (orderNum == null)
                continue;
              if (sCsourcerowid.equals(bVO[j].getCorder_bid())) {
                replenishNum = replenishNum.add(orderNum);
              }
            }
          }

          UFDouble d1 = bgVO[i].getNordernum();
          UFDouble d2 = bgVO[i].getNreplenishnum();
          if ((d1 != null) && (d2 != null)) {
            bgVO[i].setNordernum(d1.sub(d2));
          }
          if (replenishNum != null)
            if (d2 == null)
              bgVO[i].setNreplenishnum(replenishNum);
            else
              bgVO[i].setNreplenishnum(d2.add(replenishNum));
        }
      }
      else
      {
        Vector v1 = new Vector();
        for (int i = 0; i < headVO.length; i++) {
          v1.addElement(headVO[i].getPrimaryKey());
        }
        String[] sCondition = new String[v1.size()];
        v1.copyInto(sCondition);

        ImplementItemVO[] items = dmo.queryBodyImplementFirst1(sUnit.toString(), sCondition, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hFirst1 = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

        items = dmo.queryBodyImplementTotal1(sUnit.toString(), sCondition, bPOSelected, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hTotal1 = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

        Hashtable hReplenish = new Hashtable();
        ImplementItemVO[] replenishBs = null;
        if (bPOSelected.booleanValue())
        {
          items = dmo.queryBodyReplenishTotal1(sUnit.toString(), sCondition, strSqlPart, sOperator, strDataPowerSql);
          hReplenish = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

          replenishBs = dmo.queryBodyReplenishs(null, sOperator);
        }

        items = dmo.queryBodyImplementTotal1_e(sUnit.toString(), sCondition, strSqlPart, sOperator, strDataPowerSql);

        Hashtable hTotal1e = PubDMO.getHashBodyByHeadKey(items, "cpraybillid");

        ImplementItemVO[] bodyVO = null;
        ImplementItemVO[] bodygVO = null;

        ImplementItemVO[] replenishVO = null;

        for (int i = 0; i < headVO.length; i++)
        {
          bodyVO = null;
          if ((hFirst1 != null) && (hFirst1.size() > 0)) {
            bodyVO = (ImplementItemVO[])(ImplementItemVO[])hFirst1.get(headVO[i].getPrimaryKey());
          }

          if ((bodyVO != null) && (bodyVO.length > 0)) {
            for (int j = 0; j < bodyVO.length; j++) {
              v.addElement(bodyVO[j]);
            }
          }

          bodygVO = null;
          if ((hTotal1 != null) && (hTotal1.size() > 0)) {
            bodygVO = (ImplementItemVO[])(ImplementItemVO[])hTotal1.get(headVO[i].getPrimaryKey());
          }

          replenishVO = null;
          if ((hReplenish != null) && (hReplenish.size() > 0)) {
            replenishVO = (ImplementItemVO[])(ImplementItemVO[])hReplenish.get(headVO[i].getPrimaryKey());
          }

          if ((bodygVO != null) && (bodygVO.length > 0)) {
            for (int j = 0; j < bodygVO.length; j++)
            {
              if ((replenishVO != null) && (replenishVO[j] != null) && 
                (replenishVO[j].getCbaseid().equals(bodygVO[j].getCbaseid())))
              {
                UFDouble d1 = replenishVO[j].getNreplenishnum();

                UFDouble d2 = bodygVO[j].getNordernum();
                UFDouble d3 = null;
                if ((d1 != null) && (d2 != null))
                  d3 = d2.sub(d1);
                bodygVO[j].setNreplenishnum(replenishVO[j].getNreplenishnum());

                bodygVO[j].setNordernum(d3);

                String s1 = headVO[i].getCpraybillid();
                String s2 = bgVO[i].getCpraybillid();
                d1 = bodyVO[i].getNpraynum();
                if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
                {
                  UFDouble d4 = bgVO[i].getNordernum();
                  if (d4 == null)
                    d4 = new UFDouble(0);
                  bgVO[i].setNnopraynum(new UFDouble(d1.doubleValue() - d4.doubleValue()));
                }

              }

              vg.addElement(bodygVO[j]);
            }

          }

          bodygVO = null;
          if ((hTotal1e != null) && (hTotal1e.size() > 0)) {
            bodygVO = (ImplementItemVO[])(ImplementItemVO[])hTotal1e.get(headVO[i].getPrimaryKey());
          }

          Hashtable t = new Hashtable();

          if ((bodygVO != null) && (bodygVO.length > 0)) {
            for (int j = 0; j < bodygVO.length; j++)
            {
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodygVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() != 1) || (!s1.equals(s2)))
                continue;
              UFDouble d2 = bodygVO[j].getNordernum();
              if (d2 == null) {
                d2 = new UFDouble(0);
              }

              if (bodygVO[j].getCpraybill_bid() != null) {
                if (t.containsKey(bodygVO[j].getCpraybill_bid())) {
                  UFDouble dd = (UFDouble)t.get(bodygVO[j].getCpraybill_bid());

                  double d3 = d2.doubleValue() + dd.doubleValue();

                  if (d3 < 0.0D) {
                    d3 = 0.0D;
                  }
                  dd = new UFDouble(d3);

                  t.put(bodygVO[j].getCpraybill_bid(), dd);
                } else {
                  t.put(bodygVO[j].getCpraybill_bid(), d2);
                }

              }

            }

            for (int j = 0; j < bodygVO.length; j++) {
              UFDouble d1 = bodygVO[j].getNpraynum();
              if (d1 == null)
                d1 = new UFDouble(0);
              String s1 = headVO[i].getCpraybillid();
              String s2 = bodygVO[j].getCpraybillid();
              if ((headVO[i].getIbillstatus().intValue() == 1) && (s1.equals(s2)))
              {
                if ((bodygVO[j].getCpraybill_bid() != null) && (t.containsKey(bodygVO[j].getCpraybill_bid()))) {
                  UFDouble dd = (UFDouble)t.get(bodygVO[j].getCpraybill_bid());

                  if (dd == null)
                    dd = new UFDouble(0);
                  bodygVO[j].setNnopraynum(new UFDouble(d1.doubleValue() - dd.doubleValue()));
                }

              }

              bodygVO[j].setNordernum(null);
              vg.addElement(bodygVO[j]);
            }
          }
        }
        bVO = new ImplementItemVO[v.size()];
        v.copyInto(bVO);
        bgVO = new ImplementItemVO[vg.size()];
        vg.copyInto(bgVO);

        if ((replenishBs != null) && (replenishBs.length > 0)) {
          for (int i = 0; i < bgVO.length; i++) {
            String sKey1 = bgVO[i].getCpraybillid() + bgVO[i].getCbaseid();

            UFDouble replenishNum = new UFDouble(0.0D);
            for (int j = 0; j < bVO.length; j++) {
              String sKey2 = bVO[j].getCpraybillid() + bVO[j].getCbaseid();

              if (sKey1.equals(sKey2)) {
                String sKey3 = bVO[j].getCorderid() + bVO[j].getCbaseid();

                for (int k = 0; k < replenishBs.length; k++) {
                  String sKey4 = replenishBs[k].getCsourcebillid() + replenishBs[k].getCbaseid();

                  if ((!sKey4.equals(sKey3)) || (replenishBs[k].getNordernum() == null))
                    continue;
                  replenishNum = replenishNum.add(replenishBs[k].getNordernum());
                }
              }

            }

            UFDouble d1 = bgVO[i].getNreplenishnum();

            if (d1 == null)
              bgVO[i].setNreplenishnum(replenishNum);
            else {
              bgVO[i].setNreplenishnum(d1.add(replenishNum));
            }
          }
        }

        Vector vInvID = new Vector();
        for (int i = 0; i < bgVO.length - 1; i++) {
          String s1 = bgVO[i].getCbaseid().trim();
          boolean b = false;
          for (int j = i + 1; j < bgVO.length; j++) {
            String s2 = bgVO[j].getCbaseid().trim();
            if (s1.equals(s2)) {
              b = true;
              break;
            }
          }
          if (!b) {
            vInvID.addElement(bgVO[i].getCbaseid());
          }

        }

        if (bgVO.length > 0) {
          vInvID.addElement(bgVO[(bgVO.length - 1)].getCbaseid());
        }

        Vector vTemp = new Vector();
        for (int i = 0; i < vInvID.size(); i++) {
          String s1 = (String)vInvID.elementAt(i);
          s1 = s1.trim();
          double d1 = 0.0D;
          double d2 = 0.0D;
          double d3 = 0.0D;
          double d4 = 0.0D;
          double d5 = 0.0D;
          double d6 = 0.0D;

          double d7 = 0.0D;

          double d8 = 0.0D;

          double d9 = 0.0D;

          double d10 = 0.0D;

          ImplementItemVO tempVO = new ImplementItemVO();
          tempVO.setCbaseid(s1);
          for (int j = 0; j < bgVO.length; j++) {
            String s2 = bgVO[j].getCbaseid().trim();
            if (s1.equals(s2)) {
              UFDouble d = bgVO[j].getNordernum();
              if (d != null)
                d1 += d.doubleValue();
              d = bgVO[j].getNmoney();
              if (d != null)
                d2 += d.doubleValue();
              d = bgVO[j].getNaccumarrnum();
              if (d != null)
                d3 += d.doubleValue();
              d = bgVO[j].getNaccumstorenum();
              if (d != null)
                d4 += d.doubleValue();
              d = bgVO[j].getNpraynum();
              if (d != null)
                d5 += d.doubleValue();
              d = bgVO[j].getNsuggestmoney();
              if (d != null)
                d6 += d.doubleValue();
              d = bgVO[j].getNreplenishnum();
              if (d != null)
                d7 += d.doubleValue();
              d = bgVO[j].getNbackarrvnum();
              if (d != null)
                d8 += d.doubleValue();
              d = bgVO[j].getNbackstorenum();
              if (d != null)
                d9 += d.doubleValue();
              d = bgVO[j].getNnopraynum();
              if (d != null) {
                d10 += d.doubleValue();
              }
            }
          }
          tempVO.setNordernum(new UFDouble(d1));
          tempVO.setNmoney(new UFDouble(d2));
          tempVO.setNaccumarrnum(new UFDouble(d3));
          tempVO.setNaccumstorenum(new UFDouble(d4));
          tempVO.setNpraynum(new UFDouble(d5));
          tempVO.setNsuggestmoney(new UFDouble(d6));
          tempVO.setNreplenishnum(new UFDouble(d7));
          tempVO.setNbackarrvnum(new UFDouble(d8));
          tempVO.setNbackstorenum(new UFDouble(d9));
          tempVO.setNnopraynum(new UFDouble(d10));
          vTemp.addElement(tempVO);
        }
        bgVO = new ImplementItemVO[vTemp.size()];
        vTemp.copyInto(bgVO);

        bgVO = getVoImplementTotal_Inv(bgVO, sCondition);
      }

      timer.stopAndShow("订单执行汇总");
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryImplementTotal() Exception!", e);
    }

    if ((hhVO != null) && (bgVO != null) && (bgVO.length > 0)) {
      ImplementVO returnVO = new ImplementVO();
      returnVO.setParentVO(hhVO);
      returnVO.setChildrenVO(bgVO);
      return returnVO;
    }
    return null;
  }

  private ImplementItemVO[] getVoImplementTotal_Inv(ImplementItemVO[] items, String[] saHid)
    throws BusinessException
  {
    if ((items == null) || (items.length == 0)) {
      return items;
    }
    int iLen = items.length;
    String[] saInvId = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      saInvId[i] = items[i].getCbaseid();
    }
    UFDouble[][] uaValue = (UFDouble[][])null;
    try {
      PraybillDMO dmo = new PraybillDMO();
      uaValue = dmo.queryPriceMoney(saInvId, saHid);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if ((uaValue != null) && (uaValue.length == iLen)) {
      for (int i = 0; i < iLen; i++) {
        if ((uaValue[i] != null) && (uaValue[i].length >= 3)) {
          items[i].setNpraynum(uaValue[i][0]);
          items[i].setNsuggestprice(uaValue[i][2]);
          items[i].setNsuggestmoney(uaValue[i][1]);
        }
      }
    }

    return items;
  }

  public Vector queryPraybillStatus(PraybillVO[] VOs)
    throws BusinessException
  {
    Vector v = new Vector();
    try {
      PraybillDMO dmo = new PraybillDMO();
      v = dmo.queryPraybillStatus(VOs);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryPraybillStatus(PraybillVO[]) Exception!", e);
    }

    return v;
  }

  private Vector queryPreference(String headKey, String bodyKey)
    throws BusinessException
  {
    Vector v = new Vector();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      v = dmo.queryPreference(headKey, bodyKey);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryPreference(String,String) Exception!", e);
    }

    return v;
  }

  public PriceInfosVO[] queryPriceInfos(String[] saInvBaseId, UFDate date, String[] saPurCorpId, String wherePart)
    throws BusinessException
  {
    PriceInfosVO[] infos = null;

    if ((saInvBaseId == null) || (saInvBaseId.length == 0) || (date == null))
    {
      return null;
    }
    StringBuffer sql = new StringBuffer("");
    sql.append("select bd_invmandoc.pk_corp, bd_invmandoc.pk_invbasdoc, bd_invmandoc.pk_invmandoc, bd_invbasdoc.invcode, ");
    sql.append("bd_invbasdoc.invname, bd_invbasdoc.invspec, bd_invbasdoc.invtype,");
    sql.append(" bd_cubasdoc.custname,");
    sql.append(" po_askbill_bb1.nquotetaxprice,po_askbill.ccurrencytypeid");
    sql.append(" from bd_invbasdoc");
    sql.append(" inner join bd_invmandoc on bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc");

    TempTableDMO dmoTmpTbl = null;
    if ((saPurCorpId == null) || (saPurCorpId.length == 0) || (saInvBaseId == null) || (saInvBaseId.length == 0) || (saPurCorpId.length != saInvBaseId.length))
    {
      return null;
    }
    int iLen = saPurCorpId.length;
    String strTblNameCor = null;
    String strTblNameInv = null;

    ArrayList listTblDataCor = new ArrayList();
    ArrayList listTblDataInv = new ArrayList();
    ArrayList listTmp = null;
    for (int i = 0; i < iLen; i++) {
      listTmp = new ArrayList();
      listTmp.add(new Integer(i));
      listTmp.add(saPurCorpId[i]);
      listTblDataCor.add(listTmp);

      listTmp = new ArrayList();
      listTmp.add(new Integer(i));
      listTmp.add(saInvBaseId[i]);
      listTblDataInv.add(listTmp);
    }
    try {
      dmoTmpTbl = new TempTableDMO();

      strTblNameCor = dmoTmpTbl.getTempStringTable("t_pu_corp_01", new String[] { "pk_pu", "ccorpid" }, new String[] { "int", "char(4)" }, "pk_pu", listTblDataCor);

      strTblNameInv = dmoTmpTbl.getTempStringTable("t_pu_ps_39", new String[] { "pk_pu", "cmangid" }, new String[] { "int", "char(20)" }, "pk_pu", listTblDataInv);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    if ((strTblNameCor == null) || (strTblNameInv == null))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000258"));
    }
    sql.append(" inner join " + strTblNameCor + " on  bd_invmandoc.pk_corp = " + strTblNameCor + ".ccorpid ");
    sql.append(" inner join " + strTblNameInv + " on  bd_invmandoc.pk_invbasdoc = " + strTblNameInv + ".cmangid ");
    sql.append(" left outer join po_askbill_b on po_askbill_b.cbaseid = bd_invmandoc.pk_invbasdoc");
    sql.append(" left outer join po_askbill on po_askbill.caskbillid = po_askbill_b.caskbillid");
    sql.append(" left outer join po_askbill_bb1 on po_askbill_bb1.caskbillid = po_askbill_b.caskbillid");
    sql.append(" left outer join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = po_askbill_bb1.cvendorbaseid");

    sql.append(" where po_askbill.dr = 0 and po_askbill_b.dr = 0 and po_askbill_bb1.dr=0 ");
    sql.append(" and " + strTblNameCor + "." + "pk_pu" + " = " + strTblNameInv + "." + "pk_pu" + " ");

    if ((wherePart != null) && (wherePart.trim().length() > 0))
      sql.append(" and " + wherePart + " ");
    sql.append(" order by invcode");
    try
    {
      PraybillDMO dmo = new PraybillDMO();
      infos = dmo.queryPriceInfos(sql.toString());
      if ((infos != null) && (infos.length > 0))
      {
        for (int i = 0; i < infos.length; i++) {
          if (infos[i].getQuota1() == null)
            continue;
          UFDouble[] daBothRate = PubDMO.getExchangeRate(infos[i].getCurrTypeID(), date.toString(), infos[i].getpk_purcorp());

          if ((daBothRate == null) || (daBothRate[0] == null)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000011"));
          }

          if (daBothRate[0].compareTo(VariableConst.ZERO) == 0)
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000012"));
          }

          infos[i].setQuota1(new UFDouble(infos[i].getQuota1().doubleValue() * daBothRate[0].doubleValue()));
        }

        int ind = 0;
        Hashtable tempTable = new Hashtable();
        String strCorpId = infos[ind].getpk_purcorp();
        String strBaseId = infos[ind].getcbaseid();
        String strKeySeed = strCorpId + strBaseId;
        String strKeyCurr = null;
        Vector v = new Vector();
        boolean flag = false;
        for (int i = ind; i < infos.length; i++) {
          strKeyCurr = infos[i].getpk_purcorp() + infos[i].getcbaseid();
          if (!strKeyCurr.equals(strKeySeed)) {
            if (v.size() > 0) {
              tempTable.put(strKeySeed, v);
            }
            strKeySeed = strKeyCurr;
            v = new Vector();
            flag = false;
            i--;
          }
          else if (flag) {
            if ((infos[i].getQuota1() == null) || (infos[i].getQuota1().doubleValue() <= 0.0D))
              continue;
            v.addElement(infos[i]);
          }
          else if ((infos[i].getQuota1() != null) && (infos[i].getQuota1().doubleValue() > 0.0D))
          {
            flag = true;
            v.removeAllElements();
            v.addElement(infos[i]);
          }
          else if (v.size() == 0) {
            v.addElement(infos[i]);
          }
        }

        if (v.size() > 0) {
          tempTable.put(strKeySeed, v);
        }

        Enumeration keys = tempTable.keys();
        while (keys.hasMoreElements()) {
          String key = keys.nextElement().toString();
          v = (Vector)tempTable.get(key);
          if (v.size() == 1) {
            PriceInfosVO tempvo = new PriceInfosVO();
            tempvo = (PriceInfosVO)v.get(0);
            tempTable.remove(key);
            tempTable.put(key, tempvo);
          } else if (v.size() > 1)
          {
            PriceInfosVO[] sortvos = new PriceInfosVO[3];
            int indx = 0;
            while ((v.size() > 0) && (indx < 3)) {
              int xx = 0;
              UFDouble price = ((PriceInfosVO)v.get(0)).getQuota1();

              for (int i = 0; i < v.size(); i++) {
                UFDouble price1 = ((PriceInfosVO)v.get(i)).getQuota1();

                if (price.doubleValue() > price1.doubleValue()) {
                  xx = i;
                  price = price1;
                }
              }
              indx++;
              sortvos[(indx - 1)] = ((PriceInfosVO)v.get(xx));
              v.remove(xx);
            }

            if (sortvos[1] != null) {
              sortvos[0].setVendor2(sortvos[1].getVendor1());
              sortvos[0].setQuota2(sortvos[1].getQuota1());
            }
            if (sortvos[2] != null) {
              sortvos[0].setVendor3(sortvos[2].getVendor1());
              sortvos[0].setQuota3(sortvos[2].getQuota1());
            }
            tempTable.remove(key);
            tempTable.put(key, sortvos[0]);
          }
        }

        Vector allV = new Vector();
        for (int i = 0; i < saInvBaseId.length; i++) {
          strKeyCurr = saPurCorpId[i] + saInvBaseId[i];
          if (tempTable.containsKey(strKeyCurr)) {
            allV.addElement(tempTable.get(strKeyCurr));
          }
        }
        if (allV.size() > 0) {
          infos = new PriceInfosVO[allV.size()];
          allV.copyInto(infos);
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return infos;
  }

  private HashMap queryProtocol(String[] sKey)
    throws BusinessException
  {
    HashMap hRet = new HashMap();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      hRet = dmo.queryProtocol(sKey);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryProtocol(String) Exception!", e);
    }

    return hRet;
  }

  public BillInvokeVO[] queryPrayDiff(String[] prayKeys, UFDouble[] orderRowsDiff)
    throws BusinessException
  {
    if ((prayKeys == null) || (prayKeys.length == 0) || (orderRowsDiff == null) || (orderRowsDiff.length == 0))
    {
      return null;
    }
    BillInvokeVO[] hRet = new BillInvokeVO[prayKeys.length];
    StringBuffer subCon = new StringBuffer();
    HashMap OrderInfo = new HashMap();
    for (int i = 0; i < prayKeys.length; i++) {
      subCon.append(" '");
      if (i < prayKeys.length - 1)
        subCon.append(prayKeys[i] + "',");
      else if (i == prayKeys.length - 1) {
        subCon.append(prayKeys[i] + "' ");
      }
      OrderInfo.put(prayKeys[i], orderRowsDiff[i]);
    }
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      hRet = dmo.queryPrayDiff(subCon.toString(), OrderInfo);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryProtocol(String) Exception!", e);
    }

    return hRet;
  }

  public ArrayList queryPurOrgAndVendor(String[] saMangId, String[] saBaseId, String sStoOrgId, String strReqCorpId)
    throws BusinessException
  {
    if ((saMangId == null) || (saMangId.length == 0) || (saBaseId == null) || (saBaseId.length == 0) || (strReqCorpId == null))
    {
      SCMEnv.out("nc.bs.pr.pray.PraybillImpl.queryPurOrgAndVendor(String[], String[], String, String)传入参数不正确！");
      return null;
    }
    int iLen = saBaseId.length;
    ArrayList arrRet = null;
    try
    {
      String beanName = ICentralPurRule.class.getName();
      ICentralPurRule queryRuleSrv = (ICentralPurRule)NCLocator.getInstance().lookup(beanName);
      IsCentralVO[] paras = new IsCentralVO[iLen];
      for (int i = 0; i < iLen; i++) {
        paras[i] = new IsCentralVO();
        paras[i].setPk_corp(strReqCorpId);
        paras[i].setCrowid(i + "");
        paras[i].setPk_invbasdoc(saBaseId[i]);
      }

      CentralResultVO[] result = queryRuleSrv.isCentralPur(paras);
      ArrayList listYes = new ArrayList();
      ArrayList listNo = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        result[i].setInvBaseID(paras[i].getPk_invbasdoc());
        if (result[i].getIsCentralPur())
        {
          listYes.add(result[i]);
        }
        else {
          result[i].setPk_corp(strReqCorpId);
          listNo.add(result[i]);
        }
      }

      IScmPosInv queryScmPos = (IScmPosInv)NCLocator.getInstance().lookup(IScmPosInv.class.getName());
      PraybillDMO dmo = new PraybillDMO();

      HashMap hVendorNo = null;
      int iSizeNo = 0;
      String[] saBaseIdTmp = null;
      String[] saCorpId = null;
      String[] saPsnIdNo = null;
      ArrayList listVal = null;
      Hashtable tablePsndocNo = null;
      if (listNo.size() > 0) {
        iSizeNo = listNo.size();
        saBaseIdTmp = new String[iSizeNo];
        saCorpId = new String[iSizeNo];
        saPsnIdNo = new String[iSizeNo];
        for (int i = 0; i < iSizeNo; i++) {
          saBaseIdTmp[i] = ((CentralResultVO)listNo.get(i)).getInvBaseID();
          saCorpId[i] = ((CentralResultVO)listNo.get(i)).getPk_corp();

          saPsnIdNo[i] = queryScmPos.getPurchaser(saCorpId[i], saBaseIdTmp[i]);
        }

        if ((sStoOrgId != null) && (sStoOrgId.trim().length() > 0)) {
          HashMap hOrg = dmo.queryPurOrg(saBaseIdTmp, sStoOrgId, strReqCorpId);
          if (hOrg == null) {
            hOrg = new HashMap();
          }
          for (int i = 0; i < iSizeNo; i++) {
            listVal = (ArrayList)hOrg.get(saBaseIdTmp[i]);
            if ((listVal == null) || (listVal.size() == 0) || (listVal.size() < 3)) {
              continue;
            }
            ((CentralResultVO)listNo.get(i)).setPk_purorg((String)listVal.get(0));
            ((CentralResultVO)listNo.get(i)).setPk_corp((String)listVal.get(2));
            saCorpId[i] = ((String)listVal.get(2));
          }
        }

        hVendorNo = dmo.queryPurVendorByBaseCorp(saBaseIdTmp, saCorpId);
      }

      HashMap hVendorYes = null;
      String[] saPsnIdYes = null;
      int iSizeYes = 0;
      if (listYes.size() > 0) {
        iSizeYes = listYes.size();
        saBaseIdTmp = new String[iSizeYes];
        saCorpId = new String[iSizeYes];
        saPsnIdYes = new String[iSizeYes];

        for (int i = 0; i < iSizeYes; i++) {
          saBaseIdTmp[i] = ((CentralResultVO)listYes.get(i)).getInvBaseID();
          saCorpId[i] = ((CentralResultVO)listYes.get(i)).getPk_corp();
          saPsnIdYes[i] = queryScmPos.getPurchaser(saCorpId[i], saBaseIdTmp[i]);
        }

        hVendorYes = dmo.queryPurVendorByBaseCorp(saBaseIdTmp, saCorpId);
      }

      HashMap mapRetList = new HashMap();
      for (int i = 0; i < iSizeNo; i++) {
        listVal = new ArrayList();
        listVal.add(((CentralResultVO)listNo.get(i)).getPk_purorg());
        listVal.add(((CentralResultVO)listNo.get(i)).getPk_corp());
        listVal.add(saPsnIdNo[i]);
        listVal.add(hVendorNo.get(((CentralResultVO)listNo.get(i)).getInvBaseID()));
        mapRetList.put(((CentralResultVO)listNo.get(i)).getCrowid(), listVal);
      }
      for (int i = 0; i < iSizeYes; i++) {
        listVal = new ArrayList();
        listVal.add(((CentralResultVO)listYes.get(i)).getPk_purorg());
        listVal.add(((CentralResultVO)listYes.get(i)).getPk_corp());
        listVal.add(saPsnIdYes[i]);
        listVal.add(hVendorYes.get(((CentralResultVO)listYes.get(i)).getInvBaseID()));
        mapRetList.put(((CentralResultVO)listYes.get(i)).getCrowid(), listVal);
      }
      arrRet = new ArrayList(iLen);
      for (int i = 0; i < iLen; i++)
        arrRet.add((ArrayList)mapRetList.get(i + ""));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    return arrRet;
  }

  public Hashtable queryPsnByPurOrgCorps(String[] saCorpId, String[] saPurOrgId)
    throws BusinessException
  {
    Hashtable hashRet = new Hashtable();
    try {
      IPsntoPurorgQry qrySrv = (IPsntoPurorgQry)NCLocator.getInstance().lookup(IPsntoPurorgQry.class.getName());
      PsntopurorgVO[] voaPsnToOrg = qrySrv.retrieveByPk_purorg(saPurOrgId);
      if ((voaPsnToOrg == null) || (voaPsnToOrg.length == 0)) {
        return null;
      }
      for (int i = 0; i < saPurOrgId.length; i++) {
        if ((voaPsnToOrg[i].getPk_purorg() == null) || (voaPsnToOrg[i].getPk_psndoc() == null))
        {
          continue;
        }
        hashRet.put(voaPsnToOrg[i].getPk_purorg(), voaPsnToOrg[i].getPk_psndoc());
      }
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return hashRet;
  }

  private Hashtable queryPsnByCrVos(CentralResultVO[] vos)
    throws BusinessException
  {
    int iLen = vos == null ? 0 : vos.length;
    if (iLen == 0) {
      return null;
    }
    ArrayList listPurOrgId = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if (vos[i].getPk_purorg() == null) {
        continue;
      }
      listPurOrgId.add(vos[i].getPk_purorg());
    }
    if (listPurOrgId.size() == 0) {
      return null;
    }
    String[] saPurOrgId = (String[])(String[])listPurOrgId.toArray(new String[listPurOrgId.size()]);

    IPsntoPurorgQry qrySrv = (IPsntoPurorgQry)NCLocator.getInstance().lookup(IPsntoPurorgQry.class.getName());
    PsntopurorgVO[] voaPsnToOrg = qrySrv.retrieveByPk_purorg(saPurOrgId);
    if ((voaPsnToOrg == null) || (voaPsnToOrg.length == 0)) {
      return null;
    }
    iLen = voaPsnToOrg.length;
    Hashtable hashRet = new Hashtable();
    for (int i = 0; i < iLen; i++) {
      if ((voaPsnToOrg[i].getPk_purorg() == null) || (voaPsnToOrg[i].getPk_psndoc() == null))
      {
        continue;
      }
      hashRet.put(voaPsnToOrg[i].getPk_purorg(), voaPsnToOrg[i].getPk_psndoc());
    }

    return hashRet;
  }

  public PraybillVO queryPrayVoByHid(String sHId)
    throws BusinessException
  {
    PraybillVO vo = null;
    PraybillHeaderVO[] heads = null;
    PraybillItemVO[] bodys = null;

    Timer timer = new Timer();
    timer.start("queryVoByHid操作开始");
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      heads = dmo.queryAllHead(null, " and po_praybill.cpraybillid = '" + sHId + "'", null);
      timer.addExecutePhase("queryAllHead");

      if (heads == null) {
        return null;
      }
      bodys = dmo.queryAllBody(" and po_praybill_b.cpraybillid = '" + sHId + "'");

      timer.addExecutePhase("queryAllBody");

      if ((bodys == null) || (bodys.length <= 0)) {
        return null;
      }

      bodys = getItemWithMny(bodys);

      vo = new PraybillVO();
      vo.setParentVO(heads[0]);
      vo.setChildrenVO(bodys);
      timer.addExecutePhase("其他处理");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    timer.showAllExecutePhase("queryVoByHid操作结束");
    return vo;
  }

  public FreeVO queryVOForFreeItem(String cBaseID)
    throws BusinessException
  {
    if (cBaseID == null) {
      SCMEnv.out("nc.bs.pr.pray.PraybillImpl.queryVOForFreeItem(String)传入参数不正确！");
      return null;
    }

    FreeVO freeVO = new FreeVO();
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      Vector vID = dmo.queryFreeItemID(cBaseID);

      for (int i = 0; i < vID.size(); i++) {
        String sID = (String)vID.elementAt(i);

        String sName = dmo.queryFreeItemName(sID);

        if (i == 0) {
          freeVO.setVfreeid1(sID);
          freeVO.setVfreename1(sName);
        }
        if (i == 1) {
          freeVO.setVfreeid2(sID);
          freeVO.setVfreename2(sName);
        }
        if (i == 2) {
          freeVO.setVfreeid3(sID);
          freeVO.setVfreename3(sName);
        }
        if (i == 3) {
          freeVO.setVfreeid4(sID);
          freeVO.setVfreename4(sName);
        }
        if (i == 4) {
          freeVO.setVfreeid5(sID);
          freeVO.setVfreename5(sName);
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryVOForFreeItem(String) Exception!", e);
    }

    return freeVO;
  }

  public void updateAccumNumFrmSc(ParaVO21WriteNumTo20[] vos)
    throws BusinessException
  {
    int iLength = vos.length;
    if ((vos == null) || (iLength <= 0)) {
      return;
    }
    String sMethodName = "nc.bs.pr.PraybillImpl.updateAccumNumFrmSc()";
    String pk_corp = null;
    String sExceptionKind = "";

    UFDouble[] ufPraybillNums = new UFDouble[iLength];

    UFDouble[] ufAccumOrderNums = new UFDouble[iLength];
    try
    {
      PubDMO dmoPuPub = new PubDMO();

      PraybillDMO dmo = new PraybillDMO();

      SysInitDMO initDMO = new SysInitDMO();

      String[] rowIds = new String[iLength];
      UFDouble[] numOld = new UFDouble[iLength];
      UFDouble[] numNew = new UFDouble[iLength];

      for (int i = 0; i < iLength; i++) {
        if (vos[i] == null)
          continue;
        if (pk_corp == null)
          pk_corp = vos[i].getPkCorp();
        rowIds[i] = vos[i].getRowbid();
        numOld[i] = vos[i].getNumOld();
        numNew[i] = vos[i].getNumNew();
      }
      if (pk_corp == null) {
        pk_corp = "";
      }

      String strNumContKind = initDMO.getParaString(pk_corp, "PO47");

      if (strNumContKind == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
      }

      UFDouble dNumContValue = null;
      if ((strNumContKind.equals("不保存")) || (strNumContKind.equals("提示")))
      {
        dNumContValue = initDMO.getParaDbl(pk_corp, "PO48");

        if (dNumContValue == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
        }

        Object[][] ob = dmoPuPub.queryArrayValue("po_praybill_b", "cpraybill_bid", new String[] { "npraynum", "naccumulatenum" }, rowIds, "po_praybill_b.dr=0");

        if (ob == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
        }

        for (int i = 0; (i < ob.length) && (i < iLength); i++) {
          if ((ob[i] == null) || (ob[i][0] == null)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
          }

          ufPraybillNums[i] = new UFDouble(ob[i][0].toString());
          if (ob[i][1] == null)
            ufAccumOrderNums[i] = new UFDouble("0.0");
          else {
            ufAccumOrderNums[i] = new UFDouble(ob[i][1].toString());
          }

          if ((numNew[i] == null) || (numOld[i] == null))
            continue;
          UFDouble ufOrderNum = numNew[i].sub(numOld[i]);

          UFDouble d1 = ufAccumOrderNums[i].add(ufOrderNum);

          UFDouble d2 = new UFDouble("1.0").add(dNumContValue.div(100.0D));

          if (d1.compareTo(ufPraybillNums[i].multiply(d2)) > 0) {
            sExceptionKind = NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000479");

            break;
          }
        }

      }

      if (sExceptionKind.length() > 0) {
        if (strNumContKind.equals("提示")) {
          if (!vos[0].isUserConfirm()) {
            throw new RwtScToPrException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000433"));
          }

        }
        else
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000433"));
        }

      }

      dmo.updateAccumulateNum(rowIds, numOld, numNew);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private ParaVO21WriteNumTo20[] getStatVos(ParaVO21WriteNumTo20[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return null;
    HashMap mapRet = new HashMap();
    ParaVO21WriteNumTo20 voTmp = null;
    int iLen = vos.length;
    for (int i = 0; i < iLen; i++) {
      if ((vos[i] == null) || (vos[i].getRowbid() == null)) {
        continue;
      }
      if (mapRet.containsKey(vos[i].getRowbid())) {
        voTmp = (ParaVO21WriteNumTo20)mapRet.get(vos[i].getRowbid());
        voTmp.setNumNew(PuPubVO.getUFDouble_NullAsZero(voTmp.getNumNew()).add(vos[i].getNumNew()));

        voTmp.setNumOld(PuPubVO.getUFDouble_NullAsZero(voTmp.getNumOld()).add(vos[i].getNumOld()));
      }
      else {
        mapRet.put(vos[i].getRowbid(), vos[i]);
      }
    }
    if (mapRet.size() == 0) {
      return null;
    }
    vos = (ParaVO21WriteNumTo20[])(ParaVO21WriteNumTo20[])mapRet.values().toArray(new ParaVO21WriteNumTo20[mapRet.size()]);

    return vos;
  }

  public void updateAccumulateNum(ParaVO21WriteNumTo20[] vos)
    throws BusinessException
  {
    vos = getStatVos(vos);

    if ((vos == null) || (vos.length == 0)) return;
    int iLength = vos.length;

    String sMethodName = "nc.bs.pr.PraybillImpl.updateAccumulateNum()";
    String sExceptionKind = "";

    UFDouble[] ufPraybillNums = new UFDouble[iLength];

    UFDouble[] ufAccumOrderNums = new UFDouble[iLength];
    try
    {
      PubDMO dmoPuPub = new PubDMO();

      PraybillDMO dmo = new PraybillDMO();

      SysInitDMO initDMO = new SysInitDMO();

      String[] rowIds = new String[iLength];
      UFDouble[] numOld = new UFDouble[iLength];
      UFDouble[] numNew = new UFDouble[iLength];
      String[] pk_corps = new String[iLength];

      for (int i = 0; i < iLength; i++) {
        if (vos[i] != null) {
          pk_corps[i] = vos[i].getPkCorp();
          rowIds[i] = vos[i].getRowbid();
          numOld[i] = vos[i].getNumOld();
          numNew[i] = vos[i].getNumNew();
        }
      }
      StringBuffer corpCon = new StringBuffer();
      for (int i = 0; i < pk_corps.length; i++) {
        if (i < pk_corps.length - 1)
          corpCon.append("'" + pk_corps[i] + "',");
        else if (i == pk_corps.length - 1) {
          corpCon.append("'" + pk_corps[i] + "'");
        }

      }

      if (corpCon == null) corpCon.append(" 1=2 ");

      Hashtable hPara = initDMO.queryBatchParaValues(pk_corps[0], new String[] { "PO47", "PO48" });
      if ((hPara == null) || (hPara.size() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
      }

      String strNumContKind = null;
      UFDouble numContValue = null;
      Object oTemp = hPara.get("PO47");
      if (oTemp == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
      }

      strNumContKind = oTemp.toString();
      oTemp = hPara.get("PO48");
      if (oTemp == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
      }

      numContValue = new UFDouble(oTemp.toString());
      numContValue = numContValue.div(100.0D);

      Object[][] ob = dmoPuPub.queryArrayValue("po_praybill_b", "cpraybill_bid", new String[] { "npraynum", "naccumulatenum" }, rowIds, "po_praybill_b.dr=0");

      if (ob == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
      }

      for (int i = 0; (i < ob.length) && (i < iLength); i++) {
        if ((ob[i] == null) || (ob[i][0] == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
        }

        if (strNumContKind.equals("不控制"))
          continue;
        if (numContValue == null) {
          numContValue = new UFDouble("0.0");
        }
        ufPraybillNums[i] = new UFDouble(ob[i][0].toString());
        if (ob[i][1] == null)
          ufAccumOrderNums[i] = new UFDouble("0.0");
        else {
          ufAccumOrderNums[i] = new UFDouble(ob[i][1].toString());
        }

        if ((numNew[i] != null) && (numOld[i] != null)) {
          UFDouble ufOrderNum = numNew[i].sub(numOld[i]);

          UFDouble d1 = ufAccumOrderNums[i].add(ufOrderNum);

          UFDouble d3 = ufPraybillNums[i].sub(d1);

          UFDouble d2 = new UFDouble("1.0").add(numContValue);
          if ((d1.compareTo(ufPraybillNums[i].multiply(d2)) > 0) || (d3.compareTo(ufPraybillNums[i].multiply(d2)) > 0)) {
            sExceptionKind = NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000480");
            break;
          }
        }

      }

      if (sExceptionKind.length() > 0) {
        if (strNumContKind.equals("提示")) {
          if (!vos[0].isUserConfirm())
            throw new RwtPoToPrException(sExceptionKind + NCLangResOnserver.getInstance().getStrByID("common", "4004COMMON000000002"));
        }
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000434"));
        }
      }

      dmo.updateAccumulateNum(rowIds, numOld, numNew);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public void updateAccumulateNum(String bodyRowId, UFDouble numOld, UFDouble numNew, String pk_corp)
    throws BusinessException, BusinessException
  {
    if ((bodyRowId == null) || (bodyRowId.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000014"));
    }

    if ((pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000015"));
    }

    String sMethodName = "nc.bs.pr.PraybillImpl.updateAccumulateNum()";
    String sExceptionKind = "";

    UFDouble ufPraybillNum = new UFDouble();

    UFDouble ufAccumOrderNum = new UFDouble();
    try
    {
      PubDMO dmoPuPub = new PubDMO();

      PraybillDMO dmo = new PraybillDMO();

      SysInitDMO initDMO = new SysInitDMO();

      String strNumContKind = initDMO.getParaString(pk_corp, "PO47");

      if (strNumContKind == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
      }

      UFDouble dNumContValue = null;
      if (strNumContKind.equals("不保存"))
      {
        dNumContValue = initDMO.getParaDbl(pk_corp, "PO48");

        if (dNumContValue == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000396"));
        }

        Object[][] ob = dmoPuPub.queryArrayValue("po_praybill_b", "cpraybill_bid", new String[] { "npraynum", "naccumulatenum" }, new String[] { bodyRowId }, "po_praybill_b.dr=0");

        if (ob == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
        }

        if ((ob[0] == null) || (ob[0][0] == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000013"));
        }

        ufPraybillNum = new UFDouble(ob[0][0].toString());
        if (ob[0][1] == null)
          ufAccumOrderNum = new UFDouble("0.0");
        else {
          ufAccumOrderNum = new UFDouble(ob[0][1].toString());
        }

        if ((numNew == null) || (numOld == null))
          return;
        UFDouble ufOrderNum = numNew.sub(numOld);

        UFDouble d1 = ufAccumOrderNum.add(ufOrderNum);

        UFDouble d2 = new UFDouble("1.0").add(dNumContValue.div(100.0D));

        if (d1.compareTo(ufPraybillNum.multiply(d2)) > 0) {
          sExceptionKind = NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000479");
        }

      }

      if (sExceptionKind.length() > 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040101", "UPP40040101-000433"));
      }

      dmo.updateAccumulateNum(bodyRowId, numOld, numNew);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private void getPlanCostPrices(ArrayList arr, String sCorpId, String[] sBaseIds, String[] sStoreOrgId)
    throws BusinessException
  {
    String[] sNotNullStoreOrgId = null;
    String[] sNullBaseIds = null;
    String[] sNotNullBaseIds = null;

    ArrayList arrNotNullStoreOrgId = new ArrayList();
    ArrayList arrNullBaseIds = new ArrayList();
    ArrayList arrNotNullBaseIds = new ArrayList();

    for (int i = 0; i < sBaseIds.length; i++) {
      if ((sStoreOrgId[i] != null) && (sStoreOrgId[i].trim().length() > 0)) {
        arrNotNullStoreOrgId.add(sStoreOrgId[i]);
        arrNotNullBaseIds.add(sBaseIds[i]);
      } else {
        arrNullBaseIds.add(sBaseIds[i]);
      }
    }

    if (arrNotNullStoreOrgId.size() > 0) {
      sNotNullStoreOrgId = new String[arrNotNullStoreOrgId.size()];
      arrNotNullStoreOrgId.toArray(sNotNullStoreOrgId);
    }
    if (arrNullBaseIds.size() > 0) {
      sNullBaseIds = new String[arrNullBaseIds.size()];
      arrNullBaseIds.toArray(sNullBaseIds);
    }
    if (arrNotNullBaseIds.size() > 0) {
      sNotNullBaseIds = new String[arrNotNullBaseIds.size()];
      arrNotNullBaseIds.toArray(sNotNullBaseIds);
    }
    try
    {
      PraybillDMO dmo = new PraybillDMO();
      HashMap hCostPrice = null;
      HashMap hPlanPrice = null;

      if ((sNotNullStoreOrgId != null) && (sNotNullStoreOrgId.length > 0)) {
        hCostPrice = dmo.queryCostPrices(sCorpId, sNotNullBaseIds, sNotNullStoreOrgId);

        hPlanPrice = dmo.queryPlanPrices(sCorpId, sNotNullBaseIds, sNotNullStoreOrgId);
      }

      HashMap hCostPrice1 = null;
      HashMap hPlanPrice1 = null;

      if ((sNullBaseIds != null) && (sNullBaseIds.length > 0)) {
        hCostPrice1 = dmo.queryCostPricesFrmInvman(sCorpId, sNullBaseIds);

        hPlanPrice1 = dmo.getPlanPricesFrmInvMan(sNullBaseIds, sCorpId);
      }

      Vector vPlanPrice = new Vector();
      Vector vCostPrice = new Vector();
      Object temp = null;
      for (int i = 0; i < sBaseIds.length; i++) {
        if ((sStoreOrgId[i] != null) && (sStoreOrgId[i].trim().length() > 0))
        {
          if (hCostPrice != null) {
            temp = hCostPrice.get(sBaseIds[i] + sStoreOrgId[i]);
            if (temp != null)
              vCostPrice.add(temp);
            else
              vCostPrice.add(new UFDouble(0));
          } else {
            vCostPrice.add(new UFDouble(0));
          }if (hPlanPrice != null) {
            temp = hPlanPrice.get(sBaseIds[i] + sStoreOrgId[i]);
            if (temp != null)
              vPlanPrice.add(temp);
            else
              vPlanPrice.add(new UFDouble(0));
          } else {
            vPlanPrice.add(new UFDouble(0));
          }
        } else {
          if (hCostPrice1 != null) {
            temp = hCostPrice1.get(sBaseIds[i]);
            if (temp != null)
              vCostPrice.add(temp);
            else
              vCostPrice.add(new UFDouble(0));
          } else {
            vCostPrice.add(new UFDouble(0));
          }if (hPlanPrice1 != null) {
            temp = hPlanPrice1.get(sBaseIds[i]);
            if (temp != null)
              vPlanPrice.add(temp);
            else
              vPlanPrice.add(new UFDouble(0));
          } else {
            vPlanPrice.add(new UFDouble(0));
          }
        }
      }

      arr.add(vPlanPrice);

      arr.add(vCostPrice);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("nc.bs.pr.pray.PraybillImpl.getPlanCostPrices", e);
    }
  }

  public void checkNum(PraybillVO vo)
    throws BusinessException
  {
    try
    {
      Hashtable table = new SysInitBO().queryBatchParaValues(vo.getHeadVO().getPk_corp(), new String[] { "PO47", "PO48" });

      String s1 = (String)table.get("PO47");
      String s2 = (String)table.get("PO48");

      if (s1.equals("不控制")) {
        return;
      }
      double ufdRate = Double.parseDouble(s2);
      PraybillItemVO[] vos = vo.getBodyVO();
      int i = 0; for (int len = vos.length; i < len; i++) {
        if ((vos[i].getStatus() == 3) || (vos[i].getNpraynum().doubleValue() * (1.0D + ufdRate / 100.0D) >= vos[i].getNaccumulatenum().doubleValue())) {
          continue;
        }
        if (("不保存".equals(s1)) && (!vo.isUserConfirm())) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("common", "4004COMMON000000063"));
        }
        if (("提示".equals(s1)) && (vo.isUserConfirm())) {
          continue;
        }
        throw new RwtPoToPrException(NCLangResOnserver.getInstance().getStrByID("common", "4004COMMON000000064"));
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public void savePrayBak(PraybillVO vo)
    throws BusinessException
  {
    PraybillItemVO[] pbivos = vo.getBodyVO();
    String[] ids = new String[pbivos.length + 1];
    ids[0] = vo.getHeadVO().getCpraybillid();
    int i = 0; for (int len = pbivos.length; i < len; i++)
      ids[(i + 1)] = pbivos[i].getCpraybill_bid();
    try
    {
      new PraybillDMO().savePrayBak(vo.getHeadVO().getPk_corp(), ids);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public PraybillVO savePrayRevise(PraybillVO vo)
    throws BusinessException
  {
    PraybillVO voRet = null;
    try {
      PraybillDMO dmo = new PraybillDMO();

      ISOToPUTO_BillConvertDMO instance = null;
      ICreateCorpQueryService isCorpEnableSrv = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (isCorpEnableSrv.isEnabled(vo.getHeadVO().getPk_corp(), "SO")) {
        instance = (ISOToPUTO_BillConvertDMO)NCLocator.getInstance().lookup(ISOToPUTO_BillConvertDMO.class.getName());
      }

      PraybillHeaderVO head = vo.getHeadVO();
      dmo.updateHead(vo.getHeadVO());

      String key = (String)head.getAttributeValue("cpraybillid");

      PraybillItemVO[] items = vo.getBodyVO();
      Vector vInsert = new Vector();
      Vector vDelete = new Vector();
      Vector vUpdate = new Vector();

      int bodyRows = items.length;
      for (int i = 0; i < bodyRows; i++) {
        if (items[i].getBodyEditStatus() == 1)
        {
          vInsert.add(items[i]);
        } else if (items[i].getBodyEditStatus() == 3)
        {
          vDelete.add(items[i]); } else {
          if (items[i].getBodyEditStatus() != 2)
            continue;
          vUpdate.add(items[i]);
        }
      }

      PraybillItemVO[] itemVO = null;

      itemVO = new PraybillItemVO[vDelete.size()];
      vDelete.copyInto(itemVO);
      dmo.deleteBody(itemVO);

      if ((instance != null) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
      {
        Object[][] rowinfo = new Object[itemVO.length][5];
        int k = 0; for (int loopk = itemVO.length; k < loopk; k++) {
          rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
          rowinfo[k][2] = itemVO[k].getNpraynum().multiply(-1.0D);
        }
        instance.writeBackArrangeInfo(rowinfo, null, null);
      }

      itemVO = new PraybillItemVO[vUpdate.size()];
      vUpdate.copyInto(itemVO);
      Hashtable hIsRel = new Hashtable();
      PraybillItemVO[] itemvos = queryBody(key);
      UFDouble num = null;
      if ((itemvos != null) && (itemvos.length > 0) && (itemVO != null) && (itemVO.length > 0))
      {
        for (int i = 0; i < itemvos.length; i++) {
          PraybillItemVO tempItem = itemvos[i];
          for (int j = 0; j < itemVO.length; j++) {
            if (tempItem.getCpraybill_bid().equals(itemVO[j].getCpraybill_bid()))
            {
              num = itemVO[j].getNpraynum().sub(tempItem.getNpraynum());
            }

            if (num == null) {
              num = new UFDouble(0);
            }
            hIsRel.put(tempItem.getCpraybill_bid(), num);
          }
        }
      }
      dmo.updateBody(itemVO);

      if ((instance != null) && (itemvos != null) && (itemvos.length > 0) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
      {
        Object[][] rowinfo = new Object[itemVO.length][5];

        int k = 0; for (int loopk = itemVO.length; k < loopk; k++) {
          rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
          rowinfo[k][2] = hIsRel.get(itemVO[k].getCpraybill_bid());
        }
        instance.writeBackArrangeInfo(rowinfo, head.getCoperator(), new UFDateTime(head.getDpraydate().toString()));
      }

      itemVO = new PraybillItemVO[vInsert.size()];
      vInsert.copyInto(itemVO);
      dmo.insertBody(itemVO, key);

      if ((instance != null) && (itemVO != null) && (itemVO.length > 0) && (itemVO[0] != null) && (itemVO[0].getCupsourcebilltype() != null) && (itemVO[0].getCupsourcebilltype().equals("30")))
      {
        Object[][] rowinfo = new Object[itemVO.length][5];
        int k = 0; for (int loopk = itemVO.length; k < loopk; k++) {
          rowinfo[k][0] = itemVO[k].getCupsourcebillrowid();
          rowinfo[k][2] = itemVO[k].getNpraynum();
        }
        instance.writeBackArrangeInfo(rowinfo, head.getCoperator(), new UFDateTime(head.getDpraydate().toString()));
      }

      voRet = dmo.queryVObyIdForRevise(head.getCpraybillid());
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return voRet;
  }

  public PraybillVO queryOldPrayBillVo(String pk_corp, String cpraybillid)
    throws BusinessException
  {
    PraybillVO voRet = null;
    try {
      voRet = queryAll("('" + pk_corp + "')", null, null, null, "po_praybill.cpraybillid = '" + cpraybillid + "'")[0];
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return voRet;
  }

  public PraybillVO[] queryBak(String sHid)
    throws BusinessException
  {
    PraybillVO[] voaRet = null;
    try {
      voaRet = new PraybillDMO().queryBak(sHid);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return voaRet;
  }

  public UFBoolean[] queryIfExecPrayFromSC(String[] ids)
    throws BusinessException
  {
    UFBoolean[] uaRet = null;
    try {
      uaRet = ((IOrder)NCLocator.getInstance().lookup(IOrder.class.getName())).queryIfExecPray(ids);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return uaRet;
  }

  public void updateNaccAskNum(ParaRewriteVO paraVo, String flag)
    throws BusinessException
  {
    try
    {
      PraybillDMO dmo = new PraybillDMO();
      dmo.updateNaccAskNum(paraVo, flag);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public void updateNaccPriceAuditNum(ParaRewriteVO paraVo, String flag)
    throws BusinessException
  {
    try
    {
      PraybillDMO dmo = new PraybillDMO();
      dmo.updateNaccPriceAuditNum(paraVo, flag);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
  }

  public UFDouble[] queryNewPriceArray(String sUnitCode, String[] saInvID)
    throws BusinessException
  {
    UFDouble[] d = null;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      d = dmo.queryNewPriceArray(sUnitCode, saInvID);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException("PraybillBean::queryNewPrice(String,String) Exception!", e);
    }

    return d;
  }

  public UFDouble[] queryNewPriceArray(String[] saPurCorpId, String[] saInvBaseId)
    throws BusinessException
  {
    UFDouble[] d = null;
    try
    {
      PraybillDMO dmo = new PraybillDMO();

      d = dmo.queryNewPriceArray(saPurCorpId, saInvBaseId);
    } catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return d;
  }

  public UFBoolean checkPraytypeBusitype(PraybillVO voCheck)
    throws BusinessException
  {
    String strBilltype = "21";
    String strBilltypeName = NCLangResOnserver.getInstance().getStrByID("billtype", "D21");
    if ((voCheck != null) && (voCheck.getHeadVO() != null) && (voCheck.getHeadVO().getPk_corp() != null) && (voCheck.getHeadVO().getCbiztype() != null) && (voCheck.getHeadVO().getIpraytype() != null))
    {
      String strCorpId = voCheck.getHeadVO().getPk_corp();
      String strBiztypeId = voCheck.getHeadVO().getCbiztype();
      if ((voCheck.getHeadVO().getIpraytype().intValue() == 0) || (voCheck.getHeadVO().getIpraytype().intValue() == 3))
      {
        strBilltype = "61";
        strBilltypeName = NCLangResOnserver.getInstance().getStrByID("billtype", "D61");
      }
      IPFConfig qrySrv = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
      BillbusinessVO[] voaRet = qrySrv.querybillSource(strCorpId, strBilltype, strBiztypeId);

      if ((voaRet == null) || (voaRet.length == 0))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("billtype", "4004COMMON000000115", null, new String[] { strBilltype, strBilltypeName }));
      }

      int iLen = voaRet.length;
      boolean bExistSrcPray = false;
      for (int i = 0; i < iLen; i++)
      {
        if ((voaRet[i] != null) && ("20".equals(voaRet[i].getPk_billtype()))) {
          return UFBoolean.TRUE;
        }
      }
    }

    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("billtype", "4004COMMON000000115", null, new String[] { strBilltype, strBilltypeName }));
  }
}