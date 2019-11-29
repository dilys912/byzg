package nc.impl.ia.bill;
 
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.ia.bill.BillDMO;
import nc.bs.ia.bill.SimulateCost;
import nc.bs.ia.bill.action.ImporBegintBillAction;
import nc.bs.ia.bill.outersave.ProcessJICAI;
import nc.bs.ia.bill.process.BillDeleteFacade;
import nc.bs.ia.bill.process.BillSaveFacade;
import nc.bs.ia.boundary.LockUtils;
import nc.bs.ia.outter.OutterDMO;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.DataCheckUtil;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IAMessage;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scm.pub.bill.BillCodeDMO;
import nc.bs.scm.pub.smart.RichDMO;
import nc.impl.ia.ia301.AuditImpl;
import nc.impl.ia.ia402.AccountImpl;
import nc.impl.ia.pub.CommonDataImpl;
import nc.impl.ia.service.check.CheckerForIA;
import nc.impl.ia.service.check.IBillCheck;
import nc.impl.ia.service.process.IBillBusinessProcesser;
import nc.impl.ia.service.process.ProcesserBuilder;
import nc.itf.ia.bill.IBill;
import nc.itf.ia.bill.IBillQuery;
import nc.itf.ia.service.IBillService;
import nc.itf.ia.service.IIAToPUBill;
import nc.itf.ic.service.IICToIA;
import nc.itf.scm.ic.freeitem.IDefine;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.to.service.IOuter;
import nc.itf.scm.to.to201.ITransferBill;
import nc.itf.so.service.ISOToIA;
import nc.vo.ia.bill.AddQueryVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.bill.SimulateVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.Log;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.TempTableVO;
import nc.vo.ic.pub.PubVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.transrela.AggregatedTransrelaVO;
import nc.vo.scm.transrela.TransrelaItemVO;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareVO;


/**
 * 功能描述:单据业务对象
 * 
 * 作者:崔勇
 * 
 * 创建日期:2001-05-29
 * 
 * 修改记录及日期:
 * 
 * 修改人:
 *  
 *  zhwj
 */
public class BillImpl implements IchangeVO, IIAToPUBill, IBill, IBillService,IBillQuery{

  /**
   * BillBO 构造子注解。
   */
  public BillImpl() {
    super();
  }

  /**
   * 函数功能:检查批次是否正确
   * 
   * 参数: BillHeaderVO checkbhvo ----- 单据表头VO BillItemVO checkbtvo ----- 单据分录VO
   * double dNumber ----- 出库数量 boolean bIsAuditBatch ----- 是否按批次核算判断
   * 
   * 返回值:是否正确
   * 
   * 异常:
   * 
   *  
   */
  private boolean checkBatchNumber(BillHeaderVO checkbhvo,
      BillItemVO checkbtvo, double dNumber, boolean bIsAuditBatch)
      throws Exception {
    boolean bRight = false;
    CommonDataImpl cbo = new CommonDataImpl();

    if (checkbtvo.getStatus() == nc.vo.pub.VOStatus.DELETED) {
      return true;
    }

    double dCanSendNum = 0;

    //获得业务类型
    String[] sBizs = new String[2];
    sBizs[0] = ConstVO.m_sBizFQSK;
    sBizs[1] = ConstVO.m_sBizWTDX;

    java.util.Hashtable ht = cbo.getBizTypeIDs(checkbtvo.getPk_corp(), sBizs);
    String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
    String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

    StringBuffer sSQL = new StringBuffer(" select ");
    sSQL = sSQL
        .append(" sum(coalesce(case when fdispatchflag=0 then nnumber end,0)) - sum(coalesce(case when fdispatchflag=1 then nnumber end,0)) ");
    sSQL = sSQL.append(" from ");
    sSQL = sSQL.append(" v_ia_inoutledger v ");
    sSQL = sSQL.append(" where ");
    sSQL = sSQL.append(" v.pk_corp = '" + checkbtvo.getPk_corp() + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" v.crdcenterid = '" + checkbhvo.getCrdcenterid() + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" v.cinventoryid = '" + checkbtvo.getCinventoryid() + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" v.vbatch = '" + checkbtvo.getVbatch() + "'");

    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" (");
    sSQL = sSQL.append(" (");
    sSQL = sSQL.append(" v.cbilltypecode != '" + ConstVO.m_sBillXSCBJZD + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" v.cbilltypecode != '" + ConstVO.m_sBillQCXSCBJZD + "'");
    sSQL = sSQL.append(" )");

    sSQL = sSQL.append(" or ");
    sSQL = sSQL.append(" v.cbiztypeid is null ");
    sSQL = sSQL.append(" or ");
    sSQL = sSQL.append(" ( ");
    sSQL = sSQL.append(" v.cbiztypeid not in (" + sFQSK + ")");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" v.cbiztypeid not in(" + sWTDX + ")");
    sSQL = sSQL.append(" ) ");
    sSQL = sSQL.append(" ) ");

    if (checkbhvo.getCwarehouseid() != null) {
      sSQL = sSQL.append(" and ");
      sSQL = sSQL.append(" v.cwarehouseid = '" + checkbhvo.getCwarehouseid() + "'");
    }

    if (checkbtvo.getPrimaryKey() != null) {
      //如果有记录，说明此行是修改的，查询批次负结存时应该不考虑此行单据上的数量
      sSQL = sSQL.append(" and ");
      sSQL = sSQL.append(" v.cbill_bid != '" + checkbtvo.getPrimaryKey() + "'");
    }

    for (int i = 1; i < 6; i++) {
      String sAttrName = "vfree" + i;
      if (checkbtvo.getAttributeValue(sAttrName) != null) {
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(" v." + sAttrName + " = '"
            + checkbtvo.getAttributeValue(sAttrName) + "'");
      }
    }

    String[][] sResult = cbo.queryData(sSQL.toString());

    if (sResult.length != 0) {
      String[] sTemp = sResult[0];
      double dRestNumber = 0;

      if (sTemp.length != 0) {
        dRestNumber = new UFDouble(sTemp[0]).doubleValue();
        dCanSendNum = dRestNumber;
      }

      //数量够了
      if (dRestNumber >= dNumber) {
        bRight = true;
      }
    }

    if (bRight == false) {
      Integer iRowIndex = checkbtvo.getIrownumber();
      String sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "20143010", "UPP20143010-000000")/* @res "批次管理存货在存货核算系统中出现负出库，请调整" */;

      if (bIsAuditBatch) {
        sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
            "20143010", "UPP20143010-000001")/* @res "批次核算存货在存货核算系统中出现负出库，请调整" */;
      }

      if ((iRowIndex != null) && (dCanSendNum != 0)) {
        if (bIsAuditBatch) {
          //sMessage = "第" + iRowIndex + "行的存货为批次核算，最大出库数量为" + dCanSendNum +
          // "，请调整";/*-=notranslate=-*/
          String[] value = new String[] { String.valueOf(iRowIndex),
              String.valueOf(dCanSendNum) };
          sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
              "20143010", "UPP20143010-000178", null, value);
        } else {
          //sMessage = "第" + iRowIndex + "行的存货为批次管理，最大出库数量为" + dCanSendNum +
          // "，请调整";;/*-=notranslate=-*/
          String[] value = new String[] { String.valueOf(iRowIndex),
              String.valueOf(dCanSendNum) };
          sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
              "20143010", "UPP20143010-000179", null, value);
        }
      }

      BusinessException e = new BusinessException(sMessage);
      throw e;
    }

    return bRight;
  }

  /**
   * 函数功能:检查数据合法性
   * 
   * 
   * 参数: BillVO voCurBill ----- 当前VO BillItemVO btvo ----- 要检查的VO boolean bIsOut
   * ----- 是否出库 boolean bIsAdjustBill ----- 是否是调整单（其他单据要检查批次） boolean
   * bIsPlanedPriceAdjustBill ----- 是否是计划价调整单 int iRowIndex ----- 状态 String
   * sDate ----- 业务日期
   * 
   * 返回值:是否合法
   * 
   * 异常:
   * 
   *  
   */
  private boolean checkData(BillVO voCurBill, BillVO bvo, boolean bIsOut,
      boolean bIsAdjustBill, boolean bIsPlanedPriceAdjustBill, int iStatus,
      String sDate) throws Exception {
    //初始化BO
    CommonDataImpl cbo = new CommonDataImpl();

    AccountImpl abo = new AccountImpl();

    //判断是否已经月末结帐
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

    String sCorpID = bhvo.getPk_corp();
    String sRDID = bhvo.getCrdcenterid();
    String sBillType = bhvo.getCbilltypecode();

    String sPeriod = cbo.getPeriod(sCorpID, sDate);

    int iIndex = sPeriod.indexOf("-");
    if (iIndex != -1) {
      String sAccountYear = sPeriod.substring(0, iIndex);
      String sAccountMonth = sPeriod.substring(iIndex + 1);

      nc.vo.ia.ia402.AccountVO avo = new nc.vo.ia.ia402.AccountVO();
      avo.setPk_corp(sCorpID);
      avo.setCaccountyear(sAccountYear);
      avo.setCaccountmonth(sAccountMonth);

      nc.vo.ia.ia402.AccountVO[] avoResult = abo.queryByVO(avo, Boolean
          .valueOf(true));

      if ((avoResult != null) && (avoResult.length != 0)) {
        //已月末结帐
        BusinessException e = null;
        if ((sAccountMonth.equals("00") == false)
            && (iStatus == ConstVO.ADD_STATUS)
            && (sBillType.equals(ConstVO.m_sBillQCRKD) == false)
            && (sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false)) {
          //e = new BusinessException("当前会计期间" + sAccountYear + "年" +
          // sAccountMonth + "期间已月末结账，不能再增加单据，请重新登录或修改单据日期");/*-=notranslate=-*/
          String[] value = new String[] { sAccountYear, sAccountMonth };
          e = new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
              .getStrByID("20143010", "UPP20143010-000180", null, value));

          throw e;
        } else if (iStatus == ConstVO.ADD_STATUS) {
          e = new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
              .getStrByID("20143010", "UPP20143010-000003")/*
                                                            * @res
                                                            * "已经期初记帐，不能再增加启用日期前的单据，请重新登录或修改单据日期"
                                                            */);
          throw e;
        }
      }
    }

    //检查库存组织对应是否合理
    if ((bhvo.getCstockrdcenterid() != null) && (bhvo.getCrdcenterid() != null)) {
      OutterDMO dmo = new OutterDMO();

      //	ArrayList alICCalbodys ----- 记录库存组织(仓储)+仓库，内部是string[] 0是库存组织(仓储)，1是仓库
      ArrayList al = new ArrayList();
      al
          .add(new String[] { bhvo.getCstockrdcenterid(),
              bhvo.getCwarehouseid() });
      String[] sBodyIDs = dmo.chgCalbodyICToIA(sCorpID, al);

      if ((sBodyIDs == null) || (sBodyIDs.length == 0)
          || (sBodyIDs[0].equals(bhvo.getCrdcenterid()) == false)) {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000004")/*
                                                                        * @res
                                                                        * "仓储库存组织和仓库与成本库存组织之间没有对应关系，请调整"
                                                                        */);
        throw e;
      }
    }

    //如果不是跌价提取单，差异率结转单，损益调整单，且是调整单标志为否
    if ((sBillType.equals(ConstVO.m_sBillDJTQD) == false)
        && (sBillType.equals(ConstVO.m_sBillCYLJZD) == false)
        && (sBillType.equals(ConstVO.m_sBillSYTZD) == false)
        && (bIsAdjustBill == false)) {
      Hashtable ht = new Hashtable(); //记录是否批次管理
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];

        //检查是否必须输入批次号
        String sBill_bID = btvo.getPrimaryKey();
        String sInv = btvo.getCinventoryid();
        String sBatch = btvo.getVbatch();
        String sSourceBillID = btvo.getCsourcebillid();
        String sSourceBillType = btvo.getCsourcebilltypecode();

        if ((sSourceBillID == null) || (sSourceBillID.trim().length() == 0)) {
          //如果是存货核算生成的单据，检查批次号
          Object oTemp = ht.get(sInv);
          UFBoolean bIsManageForBatch = new UFBoolean(false);
          if (oTemp == null) {
            bIsManageForBatch = cbo.isManageForBatch(sCorpID, sInv);
            ht.put(sInv, bIsManageForBatch);
          } else {
            bIsManageForBatch = new UFBoolean(oTemp.toString());
          }

          if (bIsManageForBatch.booleanValue()
              && ((sBatch == null) || (sBatch.trim().length() == 0))) {
            //BusinessException e = new BusinessException("第" +
            // btvo.getIrownumber() +
            // "行的存货是批次管理，必须指定批次号，请调整");/*-=notranslate=-*/
            String[] value = new String[] { btvo.getIrownumber().toString() };
            BusinessException e = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                    "UPP20143010-000181", null, value));
            throw e;
          } else if (bIsManageForBatch.booleanValue()
              && ((sBatch != null) && (sBatch.trim().length() != 0))) {
            //如果是出库单，必须够出库数量
            if (bIsOut) {
              //处理来源发票的销售成本结转单
              if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
                if ((sSourceBillType == null)
                    || (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false)) {
                  //来源不是销售发票
                  if (btvo.getNnumber() != null) {
                    double dNumber = btvo.getNnumber().doubleValue();
                    if ((dNumber > 0)
                        && (this.checkBatchNumber(bhvo, btvo, dNumber, false) == false)) {
                      return false;
                    }
                  }
                }
              } else {
                if (btvo.getNnumber() != null) {
                  double dNumber = btvo.getNnumber().doubleValue();
                  if ((dNumber > 0)
                      && (this.checkBatchNumber(bhvo, btvo, dNumber, false) == false)) {
                    return false;
                  }
                }
              }
            } else if (btvo.getNnumber() != null) {
              //入库红冲单也判断
              double dNumber = btvo.getNnumber().doubleValue();
              if ((dNumber < 0)
                  && (this.checkBatchNumber(bhvo, btvo, -dNumber, false) == false)) {
                return false;
              }
            }
          }
        }

        //处理数量的判断
        if (bhvo.getBwithdrawalflag().booleanValue()) {
          //假退料
          UFDouble ufdNumber = btvo.getNnumber();
          double dNumber = 0;

          if (ufdNumber != null) {
            dNumber = ufdNumber.doubleValue();
          }

          if (dNumber > 0) {
            //BusinessException e = new BusinessException("当前单据是假退料单据，第" +
            // btvo.getIrownumber() + "行的存货数量大于0，请调整");/*-=notranslate=-*/
            String[] value = new String[] { btvo.getIrownumber().toString() };
            BusinessException e = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                    "UPP20143010-000182", null, value));
            throw e;
          }
        }

        //换算率不能为负
        UFDouble ufdRate = btvo.getNchangerate();
        double dAstRate = 0;

        if (ufdRate != null) {
          dAstRate = ufdRate.doubleValue();
        } else if ((btvo.getCastunitid() != null)
            && (bhvo.getCsourcemodulename().equals(ConstVO.m_sModulePO) == false)) {
          //采购管理可能无法传入换算率，所以不作换算率为空的判断
          //BusinessException e = new BusinessException("第" +
          // btvo.getIrownumber() +
          // "行录入了辅计量单位，但没有换算率,不能保存，请调整");/*-=notranslate=-*/
          String[] value = new String[] { btvo.getIrownumber().toString() };
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000183", null, value));
          throw e;
        }

        if (dAstRate < 0) {
          //BusinessException e = new BusinessException("第" +
          // btvo.getIrownumber() + "行换算率为负数,不能保存，请调整");/*-=notranslate=-*/
          String[] value = new String[] { btvo.getIrownumber().toString() };
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000184", null, value));
          throw e;
        }

        if (voCurBill != null) {
          BillItemVO[] btvosOld = (BillItemVO[]) voCurBill.getChildrenVO();
          for (int j = 0; j < btvosOld.length; j++) {
            BillItemVO btvoOld = btvosOld[j];
            if (btvoOld.getPrimaryKey().equals(sBill_bID) == false) {
              continue;
            }


            UFDouble dSendNumber = btvoOld.getNsettledsendnum();
            UFDouble dRetractNumber = btvoOld.getNsettledretractnum();

            UFDouble dNewNumber = btvo.getNnumber();

            //修改后的数量不能小于累计回冲数量 ＋ 累计发出数量
            if ((dNewNumber != null) && (dSendNumber != null)
                && (dRetractNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dSendNumber.add(dRetractNumber).doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计发出数量为" +
                // dSendNumber.doubleValue() + "，累计回冲数量为" +
                // dRetractNumber.doubleValue() + "，修改后的数量必须不小于累计发出数量与累计回冲数量之和"
                // +
                // dSendNumber.add(dRetractNumber).doubleValue());/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString(),
                    dRetractNumber.toString(),
                    dSendNumber.add(dRetractNumber).toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000185", null, value));
                throw e;
              } else if (d1 * d2 < 0) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计发出数量为" +
                // dSendNumber.doubleValue() + "，累计回冲数量为" +
                // dRetractNumber.doubleValue() +
                // "，修改后的数量与累计发出数量与累计回冲数量之和异号，请调整");/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString(),
                    dRetractNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000186", null, value));
                throw e;
              }
            }

            //入库单数量不能小于累计出库数量
            if ((dNewNumber != null) && (dSendNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dSendNumber.doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计发出数量为" +
                // dSendNumber.doubleValue() + "，修改后的数量必须不小于累计发出数量" +
                // dSendNumber.doubleValue());/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString(),
                    dSendNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000187", null, value));

                throw e;
              } else if (d1 * d2 < 0) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计发出数量为" +
                // dSendNumber.doubleValue() +
                // "，修改后的数量与累计发出数量异号，请调整");/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000188", null, value));
                throw e;
              }
            }

            //修改后的数量不能小于累计回冲数量
            if ((dNewNumber != null) && (dRetractNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dRetractNumber.doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计回冲数量为" +
                // dRetractNumber.doubleValue() + "，修改后的数量必须不小于累计回冲数量" +
                // dRetractNumber.doubleValue());/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dRetractNumber.toString(),
                    dRetractNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000189", null, value));
                throw e;
              } else if (d1 * d2 < 0) {
                //BusinessException e = new BusinessException("第" +
                // btvo.getIrownumber() + "行的存货累计回冲数量为" +
                // dRetractNumber.doubleValue() +
                // "，修改后的数量与累计回冲数量异号，请调整");/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dRetractNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000190", null, value));

                throw e;
              }
            }
          }
        }
      }
    }
//不再通过保存控制，而是通过成本计算的时机来控制，
//即只有晚于本张计划价调整单进行成本计算的单据上的计划价会取本次调整后的计划价
//    //如果是计划价调整单，判断是否此库存组织＋存货的本月的其它分录都已审核
//    if (bIsPlanedPriceAdjustBill) {
//      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
//        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
//
//        String sInv = btvo.getCinventoryid();
//
//        StringBuffer sSQL = new StringBuffer(" select ");
//        sSQL = sSQL.append(" distinct vbillcode ");
//        sSQL = sSQL.append(" from ");
//        sSQL = sSQL.append(" v_ia_inoutledger ");
//        sSQL = sSQL.append(" where ");
//        sSQL = sSQL.append(" pk_corp = '" + sCorpID + "'");
//        sSQL = sSQL.append(" and ");
//        sSQL = sSQL.append(" crdcenterid = '" + sRDID + "'");
//        sSQL = sSQL.append(" and ");
//        sSQL = sSQL.append(" cinventoryid = '" + sInv + "'");
//        sSQL = sSQL.append(" and ");
//        sSQL = sSQL.append(" iauditsequence < 0 ");
//
//        String[][] sResult = cbo.queryData(sSQL.toString());
//
//        String sHintString = "";
//        for (int m = 0; m < sResult.length; m++) {
//          String[] s = sResult[m];
//          sHintString = sHintString
//              + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000005")/* @res "单据" */+ s[0] + ";";
//        }
//
//        if (sHintString.length() != 0) {
//          BusinessException e = new BusinessException(sHintString
//              + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000006")/*
//                                        * @res
//                                        * "存在在同库存组织＋存货的分录未进行成本计算，此计划价调整单不能保存，请调整"
//                                        */);
//          throw e;
//        }
//      }
//    }

    return true;
  }

  /**
   * 函数功能:检查数据合法性
   * 
   * 
   * 参数: BillVO voCurBill ----- 当前VO BillItemVO btvo ----- 要检查的VO boolean bIsOut
   * ----- 是否出库 boolean bIsAdjustBill ----- 是否是调整单（其他单据要检查批次） boolean
   * bIsPlanedPriceAdjustBill ----- 是否是计划价调整单 int iRowIndex ----- 状态 String
   * sDate ----- 业务日期 Hashtable ht ----- 存货信息 Integer[] iPeci ----- 数据精度
   * 
   * 返回值:是否合法
   * 
   * 异常:
   * 
   *  
   */
//  private boolean checkDataforOutterBill(boolean bIsOut, BillVO bvo,
//      int iStatus, Hashtable htInvs, boolean bIsFQSK, Integer[] iPeci)
//      throws Exception {
//    CommonDataImpl cbo = new CommonDataImpl();
//
//    //判断
//    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
//
//    String sCorpID = bhvo.getPk_corp();
//    String sBillType = bhvo.getCbilltypecode();
//
//    //将会计年度、会计月份设为空
//    bhvo.setCaccountyear(null);
//    bhvo.setCaccountmonth(null);
//
//    if (iStatus == ConstVO.ADD_STATUS) {
//      if (bhvo.getBestimateflag() == null) {
//        bhvo.setBestimateflag(new UFBoolean("N"));
//      }
//      if (bhvo.getBwithdrawalflag() == null) {
//        bhvo.setBwithdrawalflag(new UFBoolean("N"));
//      }
//      bhvo.setBauditedflag(new UFBoolean("N"));
//    }
//
//    bhvo.setBdisableflag(new UFBoolean("N"));
//
//    //是否假退料
//    boolean bIsWithdrawalflag = bhvo.getBwithdrawalflag().booleanValue();
//
//    //整理表体数据
//    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
//
//
//
//    for (int i = 0; i < btvos.length; i++) {
//      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
//
//      String sInv = btvo.getCinventoryid();
//      String sBatch = btvo.getVbatch();
//      UFDouble dPrice = btvo.getNprice();
//      UFDouble dPlanedMny = btvo.getNplanedmny();
//
//      //设置数据精度
//      UFDouble dNumber = btvo.getNnumber();
//      if (dNumber != null) {
//        dNumber = dNumber.setScale(iPeci[0].intValue(), UFDouble.ROUND_HALF_UP);
//        btvo.setNnumber(dNumber);
//      }
//
//      UFDouble dMny = btvo.getNmoney();
//      if (dMny != null) {
//        dMny = dMny.setScale(iPeci[2].intValue(), UFDouble.ROUND_HALF_UP);
//        btvo.setNmoney(dMny);
//      }
//
//      if (dPrice != null) {
//        dPrice = dPrice.setScale(iPeci[1].intValue(), UFDouble.ROUND_HALF_UP);
//        btvo.setNprice(dPrice);
//      }
//
//      if (dPlanedMny != null) {
//        dPlanedMny = dPlanedMny.setScale(iPeci[2].intValue(),
//            UFDouble.ROUND_HALF_UP);
//        btvo.setNplanedmny(dPlanedMny);
//      }
//
//      UFDouble dRate = btvo.getNchangerate();
//      if (dRate != null) {
//        dRate = dRate.setScale(iPeci[5].intValue(), UFDouble.ROUND_HALF_UP);
//        btvo.setNchangerate(dRate);
//      }
//
//      //是删除行不用检查合法性
//      if (iStatus == ConstVO.UPDATE_STATUS
//          && btvo.getStatus() == VOStatus.DELETED) {
//        continue;
//      }
//
//      if (iStatus == ConstVO.ADD_STATUS) {
//        //设置行号
//        btvo.setIrownumber(new Integer(i + 1));
//
//        //将审核人置为空
//        btvo.setCauditorid(null);
//        btvo.setDauditdate(null);
//        btvo.setIauditsequence(null);
//      }
//
//      //获得计价方式编码
//      String[][] sResults = null;
//      if (htInvs == null) {
//        String s[] = new String[1];
//        s[0] = sInv;
//        sResults = cbo.getPrices(sCorpID, bhvo.getCrdcenterid(), s);
//      } else {
//        sResults = (String[][]) htInvs.get(bhvo.getCrdcenterid() + "," + sInv);
//      }
//
//      UFBoolean bAuditBatch = new UFBoolean(false);
//      UFBoolean bIsManageForBatch = new UFBoolean(false);
//
//      //返回值:存货、计价方式、计划价、是否批次核算、库存组织、参考成本、是否批次管理、物料形态、存货管理档案计划价
//      if (sResults != null && sResults.length > 0
//          && sResults[0][1].trim().length() != 0
//          && sResults[0][1].equals("0") == false) {
//        Integer iPriceCode = new Integer(sResults[0][1]);
//        if (iPriceCode != null && iPriceCode.intValue() > 0
//            && iPriceCode.intValue() < 7) {
//          btvo.setFpricemodeflag(iPriceCode);
//        } else {
//          //BusinessException e = new BusinessException("当前库存组织、第" + (i+1) +
//          // "行的存货没有定义计价方式，请到物料生产档案调整数据");/*-=notranslate=-*/
//          String[] value = new String[] { String.valueOf(i + 1) };
//          BusinessException e = new BusinessException(
//              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000191", null, value));
//          throw e;
//        }
//
//
//        UFDouble dPlanedPrice = null;
//        //首先取物料生产档案上的计划价
//        if (sResults[0][2] != null
//            && sResults[0][2].toString().trim().length() != 0) {
//          dPlanedPrice = new UFDouble(sResults[0][2]);        
//        }
//        if (iPriceCode.intValue() == ConstVO.JHJ) {
//          //是计划价计价，判断是否在物料生产档案定义了计划价
//          if (dPlanedPrice == null) {
//            //BusinessException e = new BusinessException("当前库存组织、第" + (i+1) +
//            // "行的存货是计划价计价，但没有定义计划价，请到物料生产档案调整数据");/*-=notranslate=-*/
//            String[] value = new String[] { String.valueOf(i + 1) };
//            BusinessException e = new BusinessException(
//                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                    "UPP20143010-000192", null, value));
//            throw e;
//          }
//        }
//        //对于非计划价的存货如果物料生产档案上计划价未定义，则取存货管理档案上的计划价
//        if (dPlanedPrice == null && sResults[0][8] != null
//            && sResults[0][8].toString().trim().length() != 0){
//        	dPlanedPrice = new UFDouble(sResults[0][8]);
//        }
//
//        btvo.setNplanedprice(dPlanedPrice);
//
//        if (dNumber != null && dPlanedPrice != null) {
//          dPlanedMny = dNumber.multiply(dPlanedPrice);
//
//          dPlanedMny = dPlanedMny.setScale(iPeci[2].intValue(),
//              UFDouble.ROUND_HALF_UP);
//
//          btvo.setNplanedmny(dPlanedMny);
//        }
//
//        //是否批次核算
//        bAuditBatch = new UFBoolean(sResults[0][3]);
//
//        btvo.setBauditbatchflag(bAuditBatch);
//
//        if (bAuditBatch.booleanValue()
//            && (sBatch == null || sBatch.trim().length() == 0)) {
//          //BusinessException e = new BusinessException("当前库存组织、第" + (i+1) +
//          // "行的存货是按批次核算，但没有输入批次号，请调整数据");/*-=notranslate=-*/
//          String[] value = new String[] { String.valueOf(i + 1) };
//          BusinessException e = new BusinessException(
//              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000193", null, value));
//          throw e;
//        }
//
//        //是否批次管理，必须录入批次号
//        bIsManageForBatch = new UFBoolean(sResults[0][6]);
//
//        if (bIsManageForBatch.booleanValue()
//            && (sBatch == null || sBatch.trim().length() == 0)) {
//          //为解决采购发票不知道批次号，批次管理可以不录Log.info(       
//        	Log.info("第" + btvo.getIrownumber()
//              + "行的存货是批次管理，必须指定批次号，请调整");/*-=notranslate=-*/
//          //BusinessException e = new BusinessException("第" +
//          // btvo.getIrownumber() + "行的存货是批次管理，必须指定批次号，请调整");
//          //throw e;
//        }
//
//        //物料形态
//        btvo.setCinvkind(sResults[0][7]);
//      } else {
//        //BusinessException e = new BusinessException("当前库存组织、第" + (i+1) +
//        // "行的存货没有定义计价方式，请到物料生产档案调整数据");/*-=notranslate=-*/
//        String[] value = new String[] { String.valueOf(i + 1) };
//        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
//            .getInstance().getStrByID("20143010", "UPP20143010-000191", null,
//                value));
//        throw e;
//      }
//
//      int iDataGetMode = ConstVO.WQDSJ;
//      String sAdjustable = "Y";
//      if (btvos[i].getNprice() != null) {
//        iDataGetMode = ConstVO.YHLR;
//        sAdjustable = "N";
//      } else if (btvos[i].getNmoney() != null) {
//        iDataGetMode = ConstVO.YHLR;
//        sAdjustable = "N";
//      }
//      btvos[i].setFdatagetmodelflag(new Integer(iDataGetMode));
//      btvos[i].setFolddatagetmodelflag(new Integer(iDataGetMode));
//      btvos[i].setFoutadjustableflag(new UFBoolean(sAdjustable));
//
//      //是否调整了分录
//      btvos[i].setBadjustedItemflag(new UFBoolean("N"));
//
//      //是否赠品
//      if (btvos[i].getBlargessflag() == null) {
//        btvos[i].setBlargessflag(new UFBoolean("N"));
//      }
//
//      //是否生成了实时凭证
//      btvos[i].setBrtvouchflag(new UFBoolean("N"));
//
//      //如果是假退料，数量必须为负数
//      UFDouble ufdNumber = btvo.getNnumber();
//
//      if (bIsWithdrawalflag && ufdNumber != null && ufdNumber.doubleValue() > 0) {
//        //BusinessException e = new BusinessException("当前单据是假退料单据，出库数量必须小于0，第"
//        // + (i+1) + "行的存货不符合条件，请调整");/*-=notranslate=-*/
//        String[] value = new String[] { String.valueOf(i + 1) };
//        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
//            .getInstance().getStrByID("20143010", "UPP20143010-000156", null,
//                value));
//        throw e;
//      }
//    }
//
//    //合法性检查
//    //bvo.validate();
//    //由于validate方法名与父类smartVO中throws子句冲突，所以改名verify
//    bvo.verify();
//
//    return true;
//  }

  /**
   * 函数功能:检查是否可删除单据，已成本计算不能删除
   * 
   * 参数: BillVO bvo ----- 单据VO
   * 
   * 返回值:是否可删除
   * 
   * 异常:
   *  
   */
  private boolean checkDeleteData(BillVO bvo) throws Exception {
    CommonDataImpl cbo = new CommonDataImpl();

    if (bvo == null) {
      return true;
    }


    TempTableDMO dmo = new TempTableDMO();

    String[] sItems = new String[bvo.getChildrenVO().length];
    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      sItems[i] = bvo.getChildrenVO()[i].getPrimaryKey();
    }

    String sItemPKs = dmo.insertTempTable(sItems,
        TempTableVO.TEMPTABLE_BILL_ITEM, "id");

    int iCount = 0;
    String[][] sResult = null;

    StringBuffer sSQL = new StringBuffer(" select ");
    sSQL = sSQL.append(" count(*) ");
    sSQL = sSQL.append(" from ");
    sSQL = sSQL.append(" v_ia_inoutledger ");
    sSQL = sSQL.append(" where ");
    sSQL = sSQL.append(" cbill_bid in " + sItemPKs);
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" bauditedflag = 'N' ");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" brtvouchflag = 'N' ");

    sResult = cbo.queryData(sSQL.toString());

    if (sResult.length != 0) {
      String[] sTemp = sResult[0];
      if (sTemp.length != 0) {
        iCount = new Integer(sTemp[0]).intValue();
      }
    }

    if (iCount != bvo.getChildrenVO().length) {
      BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000007")/*
                                                                      * @res
                                                                      * "此单据可能已经被删除或成本计算，不能删除"
                                                                      */);
      throw e;
    }

    //2、是否是来源出库单的销售成本结转单，并且已经有部分被结算
    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
    if (btvos.length != 0) {
      String sSourceBillType = btvos[0].getCsourcebilltypecode();
      String sBillType = btvos[0].getCbilltypecode();
      if ((sBillType.equals(ConstVO.m_sBillXSCBJZD) || sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD))
          && (sSourceBillType != null)
          && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
        //是否有部分已经结算
        for (int i = 0; i < btvos.length; i++) {
          String sBill_bid = btvos[i].getCbill_bid();

          //可能在之前的单据删除中把来源发票的销售成本结转单删除了，所以要重新查询
          sSQL = new StringBuffer(" select ");
          sSQL = sSQL.append(" nsettledsendnum");
          sSQL = sSQL.append(" from ");
          sSQL = sSQL.append(" ia_bill_b ");
          sSQL = sSQL.append(" where ");
          sSQL = sSQL.append(" dr = 0 ");
          sSQL = sSQL.append(" and ");
          sSQL = sSQL.append(" cbill_bid = '" + sBill_bid + "'");

          sResult = cbo.queryData(sSQL.toString());

          //结算数量
          UFDouble dSettleNum = new UFDouble(0);
          if (sResult.length != 0) {
            String sTemp[] = sResult[0];
            if ((sTemp.length != 0) && (sTemp[0].trim().length() != 0)) {
              dSettleNum = new UFDouble(sTemp[0]);
            }
          }

          if ((dSettleNum != null) && (dSettleNum.doubleValue() > 0)) {
            //BusinessException e = new BusinessException("此单据的第" + (i+1) +
            // "行存货有部分已经结算，不能删除，请调整");/*-=notranslate=-*/
            String[] value = new String[] { String.valueOf(i + 1) };
            BusinessException e = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                    "UPP20143010-000194", null, value));
            throw e;
          }
        }
      }
    }

    return true;
  }

  /**
   * 函数功能:检查是否可修改单据，已成本计算不能修改
   * 
   * 参数: BillVO bvo ----- 单据VO
   * 
   * 返回值:是否可修改单据
   * 
   * 异常:
   *  
   */
  private boolean checkUpdateData(BillVO bvo, BillVO bcurVO) throws Exception {
    int iCount = 0;
    String[][] sResult = null;
    CommonDataImpl cbo = new CommonDataImpl();

    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    String sPK = bhvo.getPrimaryKey();
    UFBoolean bHeadAuditFlag = new UFBoolean(false);

    //先判断单据是否已被成本计算
    //因为并发控制，只能查数据库

    StringBuffer sSQL = new StringBuffer(" select ");
    sSQL = sSQL.append(" count(*) ");
    sSQL = sSQL.append(" from ");
    sSQL = sSQL.append(" ia_bill ");
    sSQL = sSQL.append(" where ");
    sSQL = sSQL.append(" cbillid = '" + sPK + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" dr = 0 ");

    sResult = cbo.queryData(sSQL.toString());

    if (sResult.length != 0) {
      String[] sTemp = sResult[0];
      if (sTemp.length != 0) {
        iCount = new Integer(sTemp[0]).intValue();
      }
    }

    if (iCount == 0) {
      //已被删除
      BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000008")/*
                                                                      * @res
                                                                      * "此单据已经被删除，不能再修改"
                                                                      */);
      throw e;
    }

    sSQL = new StringBuffer(" select ");
    sSQL = sSQL.append(" count(*) ");
    sSQL = sSQL.append(" from ");
    sSQL = sSQL.append(" ia_bill ");
    sSQL = sSQL.append(" where ");
    sSQL = sSQL.append(" cbillid = '" + sPK + "'");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" bauditedflag = 'N' ");
    sSQL = sSQL.append(" and ");
    sSQL = sSQL.append(" dr = 0 ");

    sResult = cbo.queryData(sSQL.toString());

    iCount = new Integer(sResult[0][0]).intValue();

    if (iCount == 0) {
      //已被成本计算，继续判断分录
      bHeadAuditFlag = new UFBoolean(true);
    } else {
      ////未被成本计算，继续判断是否已经生成实时凭证
      sSQL = new StringBuffer(" select ");
      sSQL = sSQL.append(" count(*) ");
      sSQL = sSQL.append(" from ");
      sSQL = sSQL.append(" ia_bill_b ");
      sSQL = sSQL.append(" where ");
      sSQL = sSQL.append(" cbillid = '" + sPK + "'");
      sSQL = sSQL.append(" and ");
      sSQL = sSQL.append(" brtvouchflag = 'Y' ");
      sSQL = sSQL.append(" and ");
      sSQL = sSQL.append(" dr = 0 ");

      sResult = cbo.queryData(sSQL.toString());

      iCount = new Integer(sResult[0][0]).intValue();

      if (iCount != 0) {
        //已有分录生成实时凭证
        bHeadAuditFlag = new UFBoolean(true);
      }
    }

    //判断分录
    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
      Integer iRowNumber = btvo.getIrownumber();

      if ((btvo.getStatus() == VOStatus.UPDATED)
          || (btvo.getStatus() == VOStatus.DELETED)) {
        String sItemID = btvo.getPrimaryKey();

        //是修改的，判断是否已被删除或成本计算或已经生成实时凭证
        sSQL = new StringBuffer(" select ");
        sSQL = sSQL.append(" count(*) ");
        sSQL = sSQL.append(" from ");
        sSQL = sSQL.append(" ia_bill_b ");
        sSQL = sSQL.append(" where ");
        sSQL = sSQL.append(" cbill_bid = '" + sItemID + "'");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(" iauditsequence < 0 ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(" brtvouchflag = 'N' ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(" dr = 0 ");

        sResult = cbo.queryData(sSQL.toString());

        if (sResult.length != 0) {
          String[] sTemp = sResult[0];
          if (sTemp.length != 0) {
            iCount = new Integer(sTemp[0]).intValue();
          }
        }

        if (iCount == 0) {
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000195", null,
                  new String[] { String.valueOf(iRowNumber) })/* 第{0}行的分录可能已经被删除或成本计算或生成实时凭证，不能再修改 */);
          throw e;
        }
      } else if (bHeadAuditFlag.booleanValue()
          && (btvo.getStatus() == VOStatus.NEW)) {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000009")/*
                                                                        * @res
                                                                        * "此单据已经有分录进行了成本计算，不能再增加新行"
                                                                        */);
        throw e;
      }
    }

    if (bHeadAuditFlag.booleanValue()) {
      /*@res 此单据已经有分录进行了成本计算或生成了实时凭证，不能再修改表头信息 */
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000199"));
        throw e;

//
//      boolean bChanged = false;
//      String sTemp1 = "";
//      String sTemp2 = "";
//
//      //比较单据号
//      sTemp1 = oldbhvo.getVbillcode();
//      sTemp2 = newbhvo.getVbillcode();
//      if (sTemp1 != null || sTemp2 != null) {
//        if ((sTemp1 == null && sTemp2 != null)
//            || (sTemp1 != null && sTemp2 == null)) {
//          //不一样
//          bChanged = true;
//        } else if (sTemp1.equals(sTemp2) == false) {
//          //不一样
//          bChanged = true;
//        }
//      }
//
//      if (bChanged == false) {
//        //比较日期
//        sTemp1 = oldbhvo.getDbilldate().toString();
//        sTemp2 = newbhvo.getDbilldate().toString();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //比较库存组织
//        sTemp1 = oldbhvo.getCrdcenterid();
//        sTemp2 = newbhvo.getCrdcenterid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //比较仓库
//        sTemp1 = oldbhvo.getCwarehouseid();
//        sTemp2 = newbhvo.getCwarehouseid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //比较业务类型
//        sTemp1 = oldbhvo.getCbiztypeid();
//        sTemp2 = newbhvo.getCbiztypeid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //比较收发类别
//        sTemp1 = oldbhvo.getCdispatchid();
//        sTemp2 = newbhvo.getCdispatchid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //部门
//        sTemp1 = oldbhvo.getCdeptid();
//        sTemp2 = newbhvo.getCdeptid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //业务员
//        sTemp1 = oldbhvo.getCemployeeid();
//        sTemp2 = newbhvo.getCemployeeid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //不一样
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //不一样
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged) {
//        //单据号、库存组织、仓库、日期、业务类型、收发类别、部门、业务员
//        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
//            .getInstance().getStrByID("20143010", "UPP20143010-000199")/* 此单据已经有分录进行了成本计算或生成了实时凭证，不能再修改表头信息 */);
//        throw e;
//      }

     }

    return true;
  }

  /**
   * 函数功能:外部系统删除单据
   * 
   * 参数: String sSourceHID ----- 来源系统单据主表ID String sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteBillFromOutter(String sSourceHID, String sUserID)
      throws BusinessException {
    try {
      if (sSourceHID == null) {
        throw new BusinessException(
            "Error:Null SourceHID is not allowed in deleteBillFromOutter");
      }
      CommonDataImpl cbo = new CommonDataImpl();

      BillVO[] bvos = this.queryByVOForOutter(sSourceHID, true, null);

      if ((bvos == null) || (bvos.length == 0)) {
        Log.info("没有符合条件的存货核算单据");
        return;
      }

      //获得单位编码
      String sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
      String sbilltype = ((BillHeaderVO) bvos[0].getParentVO()).getCbilltypecode();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
//		存货核算是否启用
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); 
      if (bIAStart.booleanValue() == false) {
          //存货核算没有启用
          Log.info("存货核算没有启用，不处理单据，返回");
          return;
      }

      //判断是否已关账
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //ClientLink(String pk_corp, String user, UFDate date, String
      // accountYear, String accountMonth, UFDate yearMonth, UFDate monthStart,
      // UFDate monthEnd, String language, boolean isDebug, String moduleName,
      // String moduleCode, String moduleID)
      //构建连接环境
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);


      //获得业务类型
      String[] sBizs = new String[2];
      sBizs[0] = ConstVO.m_sBizFQSK;
      sBizs[1] = ConstVO.m_sBizWTDX;

      java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
      String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
      String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];
        this.delete(cl, bvo, sUserID, sFQSK, sWTDX);
      }

      BillDeleteFacade process = new BillDeleteFacade();
      process.process( bvos );
      //回写库存的暂估和是否传存货标记
      ArrayList alICVOs = new ArrayList();
     	PubVO pubVO = new PubVO();
			pubVO.setAttributeValue("cgeneralhid",sSourceHID);
			pubVO.setAttributeValue("btoinzgflag", new UFBoolean("N"));
			pubVO.setAttributeValue("nzgprice1", new UFDouble(0));
			pubVO.setAttributeValue( "nzgmny1", new UFDouble(0));
			pubVO.setAttributeValue("btoouttoiaflag",	new UFBoolean("N"));
			alICVOs.add(pubVO);
      
      PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs.size()]);
      Object oICInstance = NCLocator.getInstance().lookup(IICToIA.class.getName());
      if( ConstVO.m_sBillDBCKD.equals(sbilltype)){
      	((IICToIA) oICInstance).rewrite4Y2IAflag(pubVOs);
      }else if(ConstVO.m_sBillDBRKD.equals(sbilltype)){
      	((IICToIA) oICInstance).rewriteZg1(pubVOs);
      }
      
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
  }

  public void deleteBillFromOutter_bill(String sSourceHID, String sUserID)
  throws BusinessException{
	  this.deleteBillFromOutter( sSourceHID, sUserID);
  }
  /**
   * 函数功能:外部系统批删除单据
   * 
   * 参数: String sSourceHID ----- 来源系统单据主表ID String sCorpID ----- 公司ID String
   * sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteBillFromOutterArray(String[] sSourceHIDs, String sCorpID,
      String sUserID) throws BusinessException {
    try {
      if ((sSourceHIDs == null) || (sSourceHIDs.length == 0)) {
        throw new BusinessException(
            "Error:Null SourceHIDs is not allowed in deleteBillFromOutterArray");
      }

      for (int i = 0; i < sSourceHIDs.length; i++) {
        if (sSourceHIDs[i] == null) {
          throw new BusinessException(
              "Error:Null SourceHID is not allowed in deleteBillFromOutterArray");
        }
      }
      Timer timer = new Timer();
      timer.start();
      //***************************************************
      //add by hanwei 2004-03-26 使用临时表方案
      ArrayList alSourceBillID = new ArrayList();
      Hashtable htbSourceBillID = new Hashtable();
      String strSourceBillID = null;
      String sSubSql = null;
      TempTableDMO tempTableDMO = new TempTableDMO();

      //对每个单据分别判断是否可记入存货核算单据
      //同时获得存货ID
      for (int i = 0; i < sSourceHIDs.length; i++) {
        strSourceBillID = sSourceHIDs[i];
        if ((strSourceBillID != null)
            && !htbSourceBillID.containsKey(strSourceBillID)) {
          alSourceBillID.add(strSourceBillID);
          htbSourceBillID.put(strSourceBillID, strSourceBillID);
        }
      }

      if (alSourceBillID.size() == 0) {
        //没有要处理的单据
        return;
      }

      //***************************************************
      //add by hanwei 2004-03-26 使用临时表方案
      //插入临时表
      sSubSql = tempTableDMO.insertTempTable(alSourceBillID,
          nc.vo.ia.pub.TempTableVO.TEMPTABLE_SOURCE,
          nc.vo.ia.pub.TempTableVO.TEMPPKFIELD_SOURCE);
      //***************************************************

      String sSQL = " csourcebillid in " + sSubSql;

      BillVO[] bvos = this.queryByVOForOutter(null, true, sSQL);
      

      //zhwj 系统反暂估时 不能删除运费暂估生成的入库调整单
      if(bvos!=null&&bvos.length>0){
    	  ArrayList arrbvos = new ArrayList();
    	  for(int i=0;i<bvos.length;i++){
    		  String cbilltypecode = bvos[i].getCbilltypecode();
    		  if(cbilltypecode!=null&&cbilltypecode.equals("I9")){
    			  continue;
    		  }
    		  
    		  arrbvos.add(bvos[i]);
    		  
    	  }
    	  
    	  bvos = (BillVO[])arrbvos.toArray(new BillVO[0]);
    	  
      }
      //zhwj end
      
      
      
      timer.addExecutePhase("存货核算查询由外系统生成的存货单据"/*notranslate*/);
      //没有存货核算的单据，直接返回
      if( bvos.length == 0 ){
        return ;
      }
      
      String sbilltype = ((BillHeaderVO) bvos[0].getParentVO()).getCbilltypecode();
      
      if( ConstVO.m_sBillCGRKD.equals(sbilltype) || ConstVO.m_sBillRKTZD.equals(sbilltype)){//采购取消结算
	      this.deleteCheckForPU(bvos);
      }
      timer.addExecutePhase("存货核算检查采购取消结算时未做费用结算"/*notranslate*/);
      
      this.deleteBillArrayForOutterBill(bvos, sCorpID, sUserID);
      timer.addExecutePhase("存货核算删除单据"/*notranslate*/);

      BillDeleteFacade process = new BillDeleteFacade();
      process.process( bvos );
      //回写库存的暂估和是否传存货标记
      ArrayList alICVOs = new ArrayList();
      for (int i = 0; i < sSourceHIDs.length; i++) {
      	PubVO pubVO = new PubVO();
				pubVO.setAttributeValue("cgeneralhid",sSourceHIDs[i]);
				pubVO.setAttributeValue("btoinzgflag", new UFBoolean("N"));
				pubVO.setAttributeValue("nzgprice1", null);
				pubVO.setAttributeValue( "nzgmny1", null);
				pubVO.setAttributeValue("btoouttoiaflag",	new UFBoolean("N"));
				alICVOs.add(pubVO);
      }
      PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs.size()]);
      Object oICInstance = NCLocator.getInstance().lookup(IICToIA.class.getName());
      if( ConstVO.m_sBillDBCKD.equals(sbilltype)){
      	((IICToIA) oICInstance).rewrite4Y2IAflag(pubVOs);
      }else if(ConstVO.m_sBillDBRKD.equals(sbilltype)){
      	((IICToIA) oICInstance).rewriteZg1(pubVOs);
      }
      timer.addExecutePhase("存货核算回写库存暂估标记"/*notranslate*/);
      timer.showAllExecutePhase("存货核算deletebillfromoutterarray");
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }
  }

	/**
	 * 检查采购发票结算后作了费用分摊，则不能取消采购发票结算
	 * @param bvos
	 * @throws BusinessException
	 */
	private void deleteCheckForPU(BillVO[] bvos) throws Exception {
		//查询此行的子表ID，是否出现在某个入库调整单的cadjustbillitemid字段上
		String sbilltype = ((BillHeaderVO) bvos[0].getParentVO()).getCbilltypecode();
		if (ConstVO.m_sBillCGRKD.equals(sbilltype) 
				|| ConstVO.m_sBillRKTZD.equals(sbilltype)) {
			TempTableDMO dmo = new TempTableDMO();
			ArrayList bids = new ArrayList();
			for (int i = 0; i < bvos.length; i++) {
				for (int mm = 0; mm < bvos[i].getChildrenVO().length; mm++) {
					bids.add(((BillItemVO) bvos[i].getChildrenVO()[mm]).getCbill_bid());
				}
			}
			// 插入临时表
			String bidsql = dmo.insertTempTable(bids,
					TempTableVO.TEMPTABLE_BILL_INV, "id");
			StringBuffer sql = new StringBuffer();
			sql.append(" select count(*) from ia_bill_b where dr = 0 and ");
			sql.append("cbilltypecode = '" + ConstVO.m_sBillRKTZD + "'");
			sql.append(" and ");
			sql.append("cadjustbillitemid in " + bidsql);
			IRowSet rowSet = DataAccessUtils.query(sql.toString());
			if (rowSet.next() && (rowSet.getInt(0) > 0)) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
            "20143010", "UPP20143010-000298")/*@"正在取消结算的结算单行中存在已经作了"
						+ "费用分摊的行，不能取消采购发票结算"*/ );
			}
		}
	}

  public void deleteBillFromOutterArray_bill(String[] sSourceHIDs, String sCorpID,
	      String sUserID) throws BusinessException{
	  this.deleteBillFromOutterArray(sSourceHIDs, sCorpID, sUserID);
  }
  
  
  //private boolean deleteCheckForPU(){}
  
  /**
   * 对查询出的外系统单据执行删除和后续操作
   * 
   * @param bvos
   * @param sCorpID
   * @param sUserID
   * @throws java.rmi.BusinessException
   */
  private void deleteBillArrayForOutterBill(BillVO[] bvos, String sCorpID, String sUserID)
      throws BusinessException {

    try {
      if ((bvos == null) || (bvos.length == 0)) {
        Log.info("没有符合条件的存货核算单据");
        return;
      }
      CommonDataImpl cbo = new CommonDataImpl();
      //获得单位编码
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); //存货核算是否启用
      if (bIAStart.booleanValue() == false) {
          //存货核算没有启用
          Log.info("存货核算没有启用，不处理单据，返回");
          return;
      }
      //判断是否已关账
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //获得业务类型
      String[] sBizs = new String[2];
      sBizs[0] = ConstVO.m_sBizFQSK;
      sBizs[1] = ConstVO.m_sBizWTDX;

      java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
      String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
      String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

      //固定资产是否启用
      //bFAStart = cbo.isModuleStarted(sCorpID, ConstVO.m_sModuleCodeFA,
      //    ((BillHeaderVO) bvos[0].getParentVO()).getCaccountyear() + "-"
      //        + ((BillHeaderVO) bvos[0].getParentVO()).getCaccountmonth());

      for (int j = 0; j < bvos.length; j++) {
        //还要分别处理每张单据的业务

        //获得收发标志
        Integer iRDFlag = ((BillHeaderVO) bvos[j].getParentVO())
            .getFdispatchflag();
        int iRD = -1;

        boolean bIsOut = true;

        if (iRDFlag != null) {
          iRD = iRDFlag.intValue();
        }

        if (iRD == 0) {
          //是入库单
          bIsOut = false;
        }

        //处理与固定资产接口//20050527_关闭存货与固定资产接口
        //                if (bFAStart.booleanValue()) {
        //                    doWithFa(bvos[j], null, bIsOut, ConstVO.DELETE_STATUS);
        //                }

        //处理出库单批次号的方法
        if (bIsOut) {
          this.doWithOutBatch(bvos[j], ConstVO.DELETE_STATUS);
        }
      }

      //ClientLink(String pk_corp, String user, UFDate date, String
      // accountYear, String accountMonth, UFDate yearMonth, UFDate
      // monthStart, UFDate monthEnd, String language, boolean isDebug,
      // String moduleName, String moduleCode, String moduleID)
      //构建连接环境
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      //从数据库中删除数据
      this.deleteArray(cl, bvos, sUserID);

      for (int j = 0; j < bvos.length; j++) {
        String sBillType = ((BillHeaderVO) bvos[j].getParentVO())
            .getCbilltypecode();
        String sBizTypeID = ((BillHeaderVO) bvos[j].getParentVO())
            .getCbiztypeid();

        //处理来源发票的销售成本结转单
        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //是销售成本结转单
          for (int k = 0; k < bvos[j].getChildrenVO().length; k++) {
            BillItemVO btvo = (BillItemVO) bvos[j].getChildrenVO()[k];
            String sSourceBillType = btvo.getCsourcebilltypecode();

            boolean bIsFQWT = false;

            if (sBizTypeID != null) {
              if ((sFQSK.indexOf(sBizTypeID) != -1)
                  || (sWTDX.indexOf(sBizTypeID) != -1)) {
                bIsFQWT = true;
              }
            }

            if (bIsFQWT) {
              if ((sSourceBillType != null)
                  && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
                //来源是销售发票
                this.doWithSOFaPiao(btvo, ConstVO.DELETE_STATUS,null);
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }

  }

  /**
   * 功能：根据存货单据的主键删除外系统传入的单据
   * 
   * @param headIDs
   *          String[] 主表id数组
   *  
   */
  public void deleteIABillsByIAPks(String[] headIDs, String sCorpID,
      String sUserID) throws BusinessException {
    try {
      if ((headIDs == null) || (headIDs.length == 0) || (sCorpID == null)
          || (sUserID == null)) {
        throw new BusinessException(
            "Error:Null headIDs is not allowed in deleteIABillsByIAPks");
      }
      HashMap hmTemp = new HashMap();
      for (int i = 0; i < headIDs.length; i++) {
        if (headIDs[i] == null) {
          throw new BusinessException(
              "Error:Null headIDs[i] is not allowed in deleteIABillsByIAPks");
        }
        hmTemp.put(headIDs[i], null);
      }
      Set keySet = hmTemp.keySet();
      ArrayList alBillIDs = new ArrayList();
      alBillIDs.addAll(keySet);

      if (alBillIDs.size() == 0) {
        //没有要处理的单据
        return;
      }

      TempTableDMO tempTableDMO = new TempTableDMO();
      //***************************************************
      //add by hanwei 2004-03-26 使用临时表方案
      //插入临时表
      String sSubSql = tempTableDMO.insertTempTable(alBillIDs,
          nc.vo.ia.pub.TempTableVO.TEMPTABLE_SOURCE,
          nc.vo.ia.pub.TempTableVO.TEMPPKFIELD_SOURCE);
      //***************************************************

      sSubSql = " cbillid in " + sSubSql;

      BillVO[] bvos = this.queryByVOForOutter(null, true, sSubSql);

      this.deleteBillArrayForOutterBill(bvos, sCorpID, sUserID);

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }

  }
  
  
  public void deleteIABillsByIAPks_bill(String[] headIDs, String sCorpID,
	      String sUserID) throws BusinessException{
	  this.deleteIABillsByIAPks( headIDs, sCorpID,sUserID); 
  }

  /**
   * 函数功能:采购暂估删除单据(只删除此表体行)
   * 
   * 参数: String sSourceBID ----- 来源系统单据子表ID String sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteBillItemForPU(String sSourceBID, String sUserID)
      throws BusinessException {
    String[] sItemIDs = null;
    boolean bIfCanDO = false;
    Timer timer  = new Timer();
    timer.start();
    try {
      if (sSourceBID == null) {
        throw new BusinessException(
            "Error:Null SourceHID is not allowed in deleteBillItemForPU");
      }
      BillVO[] bvos = this.queryByVOForOutter(sSourceBID, false, null);

      //zhwj 系统反暂估时 不能删除运费暂估生成的入库调整单
      if(bvos!=null&&bvos.length>0){
    	  ArrayList arrbvos = new ArrayList();
    	  for(int i=0;i<bvos.length;i++){
    		  String cbilltypecode = bvos[i].getCbilltypecode();
    		  if(cbilltypecode!=null&&cbilltypecode.equals("I9")){
    			  continue;
    		  }
    		  
    		  arrbvos.add(bvos[i]);
    		  
    	  }
    	  
    	  bvos = (BillVO[])arrbvos.toArray(new BillVO[0]);
    	  
      }
      //zhwj end
      
      if ((bvos == null) || (bvos.length == 0)) {
        Log.info("没有符合条件的存货核算单据");
        return;
      }

      //获得单位编码
      String sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      CommonDataImpl cbo = new CommonDataImpl();
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
			//存货核算是否启用
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); 
      if (bIAStart.booleanValue() == false) {
          //存货核算没有启用
          Log.info("存货核算没有启用，不处理单据，返回");
          return;
      }
      //判断是否已关账
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //构建连接环境
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];

        if ((bvo.getChildrenVO() == null) || (bvo.getChildrenVO().length == 0)) {
          continue;
        }

        try {
          //锁记录
          String sPK = bvo.getParentVO().getPrimaryKey();

          sItemIDs = new String[bvo.getChildrenVO().length + 1];
          sItemIDs[0] = sPK;

          for (int j = 0; j < bvo.getChildrenVO().length; j++) {
            String sID = bvo.getChildrenVO()[j].getPrimaryKey();
            sItemIDs[j + 1] = sID;
          }

          bIfCanDO = LockUtils.lock(sItemIDs, sUserID);

          if (bIfCanDO == false) {
            BusinessException be = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                    "SCMCOMMON", "UPPSCMCommon-000316")/* @res "正在进行相关操作，请稍后再试" */);
            throw be;
          }

          boolean bIsUpdate = true;
          BillVO voCurBill = (BillVO) bvo.clone();

          for (int mm = 0; mm < bvo.getChildrenVO().length; mm++) {
            BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[mm];
            if (btvo.getCsourcebillitemid().equals(sSourceBID)) {
              //设此行为删除
              bvo.getChildrenVO()[mm].setStatus(nc.vo.pub.VOStatus.DELETED);

              if (bvo.getChildrenVO().length == 1) {
                //此单据只有此一行分录,直接删除即可
                bIsUpdate = false;
              }

              break;
            }
          }

          if (bIsUpdate) {
            //修改此行分录状态
            voCurBill = this.updateBill(cl, voCurBill, bvo, false, false, sUserID,
                false, cbo.getParaValue(((BillHeaderVO) bvo.getParentVO())
                    .getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB)));
          } else {
            //直接删除此单据
            //收
            this.deleteIn(cl, bvo, false, sUserID,true);
          }
        } finally {
          try {
            if (bIfCanDO) {
              LockUtils.unLock(sItemIDs, sUserID);
            }
          } catch (Exception e) {
            throw new BusinessException(nc.bs.ml.NCLangResOnserver
                .getInstance().getStrByID("20143010", "UPP20143010-000010")/*
                                                                            * @res
                                                                            * "释放业务锁出错"
                                                                            */);
          }
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    timer.addExecutePhase("存货核算删除采购暂估生成的单据行"/*notranslate*/);
    timer.showAllExecutePhase("存活核算deleteBillItemForPU");
  }

  public void deleteBillItemForPU_bill(String sSourceBID, String sUserID)
  throws BusinessException {
	  this.deleteBillItemForPU(sSourceBID,sUserID);
  }
  /**
   * 函数功能:采购暂估删除单据(只删除此表体行)
   * 
   * 参数: String sSourceBID ----- 来源系统单据子表ID String sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteBillItemForPUs(String[] sSourceBIDs, String sUserID)
      throws BusinessException {
    try {
      if ((sSourceBIDs == null) || (sSourceBIDs.length == 0)) {
        throw new BusinessException(
            "Error:Null SourceHIDs is not allowed in deleteBillItemForPUs");
      }

      for (int i = 0; i < sSourceBIDs.length; i++) {
        this.deleteBillItemForPU(sSourceBIDs[i], sUserID);
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }
  }

  
  public void deleteBillItemForPUs_bill(String[] sSourceBIDs, String sUserID)
  throws BusinessException {
	  this.deleteBillItemForPUs(sSourceBIDs, sUserID);
  }
  /**
   * 函数功能:处理与固定资产接口
   * 
   * 参数: BillVO bvo ----- 单据VO BillVO bupdatevo ----- 修改后的VO 删除时用 boolean bIsOut
   * ----- 是否是出库单 int iStatus ----- 增加、修改还是删除
   * 
   * 返回值:
   * 
   * 异常:
   * 
   * 创建日期：(2002-10-9 14:33:26)
   */
  //20050527_关闭存货与固定资产接口
  //private void doWithFa(BillVO bvo,BillVO bupdatevo,boolean bIsOut,int
  // iStatus) throws java.lang.Exception
  //{
  //	if (bvo.getChildrenVO() == null || bvo.getChildrenVO().length == 0)
  //	{
  //		return;
  //	}
  //
  //	if (iStatus == ConstVO.ADD_STATUS)
  //	{
  //		//增加
  //		Vector vTemp = new Vector(1,1);
  //		nc.vo.fa.outer.FaSubequipmentVO[] fvos = null;
  //
  //		if (bIsOut)
  //		{
  //			//出库单
  //			BillHeaderVO bhvo = (BillHeaderVO)bvo.getParentVO();
  //			for (int i=0;i<bvo.getChildrenVO().length;i++)
  //			{
  //				BillItemVO btvo = (BillItemVO)bvo.getChildrenVO()[i];
  //
  //				String sFaName = "";
  //
  //				if (btvo.getCfacardid() != null)
  //				{
  //					boolean bFind = false;
  //					if (bupdatevo != null && bupdatevo.getChildrenVO() != null)
  //					{
  //						for (int j=0;j<bupdatevo.getChildrenVO().length;j++)
  //						{
  //							BillItemVO bupdatetvo = (BillItemVO)bupdatevo.getChildrenVO()[j];
  //							if (bupdatetvo.getVOFas() != null)
  //							{
  //								for (int k=0;k<bupdatetvo.getVOFas().length;k++)
  //								{
  //									String sUpdateBillitemID = bupdatetvo.getVOFas()[k].getPk_billrow();
  //									if (sUpdateBillitemID != null &&
  // btvo.getPrimaryKey().equals(sUpdateBillitemID))
  //									{
  //										bFind = true;
  //										break;
  //									}
  //								}
  //							}
  //
  //							if (bFind)
  //							{
  //								break;
  //							}
  //						}
  //					}
  //
  //					if (bFind)
  //					{
  //						//就是此单据行的，不能增加
  //						continue;
  //					}
  //
  //					//构建此次的
  //					nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //					fvo.setStatus(2); //状态为增加
  //					fvo.setPk_corp(btvo.getPk_corp());
  //					fvo.setPkinv(btvo.getCinventoryid());
  //					fvo.setEquip_code(btvo.getCinventorycode());
  //					fvo.setEquip_name(btvo.getCinventoryname());
  //					fvo.setSpec(btvo.getCinventoryspec());
  //					fvo.setUnit(btvo.getCinventorymeasname());
  //					fvo.setInstorage_flag(new Integer(0)); //未入库
  //					fvo.setAssetname(btvo.getCfadevicename()); //资产名称
  //					fvo.setPk_billrow(btvo.getPrimaryKey());
  //					fvo.setFk_card(btvo.getCfacardid());
  //					fvo.setOriginvalue(btvo.getNmoney());
  //					fvo.setBegindate(bhvo.getDbilldate().toString());
  //					fvo.setRemark(bhvo.getVnote());
  //
  //					if (btvo.getNnumber() != null)
  //					{
  //						int iNum = Math.round(btvo.getNnumber().floatValue());
  //						fvo.setQuantity(new Integer(iNum));
  //					}
  //
  //					vTemp.addElement(fvo);
  //				}
  //			}
  //
  //			for (int i=0;i<bvo.getChildrenVO().length;i++)
  //			{
  //				BillItemVO btvo = (BillItemVO)bvo.getChildrenVO()[i];
  //
  //				String sFaName = "";
  //
  //				if (btvo.getVOFas() != null)
  //				{
  //					//增加原来的
  //					for (int j=0;j<btvo.getVOFas().length;j++)
  //					{
  //						if (sFaName.length() == 0)
  //						{
  //							sFaName = btvo.getVOFas()[j].getAssetname();
  //						}
  //						btvo.getVOFas()[j].setPk_billrow(btvo.getPrimaryKey());
  //// btvo.getVOFas()[j].setStatus(nc.vo.pub.VOStatus.NEW);
  //						vTemp.addElement(btvo.getVOFas()[j]);
  //					}
  //				}
  //			}
  //		}
  //		else
  //		{
  //			//入库单修改标志
  //			for (int i=0;i<bvo.getChildrenVO().length;i++)
  //			{
  //				BillItemVO btvo = (BillItemVO)bvo.getChildrenVO()[i];
  //				if (btvo.getCfadeviceid() == null)
  //				{
  //					continue;
  //				}
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo.setStatus(nc.vo.pub.VOStatus.UPDATED); //状态为修改
  //				fvo.setInstorage_flag(new Integer(1)); //已入库
  //				fvo.setPk_subequipment(btvo.getCfadeviceid());
  //
  //				vTemp.addElement(fvo);
  //			}
  //		}
  //
  //		if (vTemp.size() != 0)
  //		{
  //			fvos = new nc.vo.fa.outer.FaSubequipmentVO[vTemp.size()];
  //			vTemp.copyInto(fvos);
  //			nc.bs.fa.outer.FaSubequipmentDMO dmo = new
  // nc.bs.fa.outer.FaSubequipmentDMO();
  //			if (bIsOut)
  //				dmo.save(fvos);
  //			else
  //				dmo.updateInstorageFlag(fvos);
  //		}
  //	}
  //	else if (iStatus == ConstVO.DELETE_STATUS)
  //	{
  //		//删除
  //		Vector vTemp = new Vector(1,1);
  //		nc.vo.fa.outer. FaSubequipmentVO[] fvos = null;
  //
  //		if (bIsOut)
  //		{
  //			//出库单
  //			for (int i=0;i<bvo.getChildrenVO().length;i++)
  //			{
  //				BillItemVO btvo = (BillItemVO)bvo.getChildrenVO()[i];
  //				if (btvo.getCfacardid() == null)
  //				{
  //					continue;
  //				}
  //
  //				boolean bFind = false;
  //				if (bupdatevo != null && bupdatevo.getChildrenVO() != null)
  //				{
  //					for (int j=0;j<bupdatevo.getChildrenVO().length;j++)
  //					{
  //						BillItemVO bupdatetvo = (BillItemVO)bupdatevo.getChildrenVO()[j];
  //						if (bupdatetvo.getVOFas() != null)
  //						{
  //							for (int k=0;k<bupdatetvo.getVOFas().length;k++)
  //							{
  //								String sUpdateBillitemID = bupdatetvo.getVOFas()[k].getPk_billrow();
  //								if (sUpdateBillitemID != null &&
  // btvo.getPrimaryKey().equals(sUpdateBillitemID))
  //								{
  //									bFind = true;
  //									break;
  //								}
  //							}
  //						}
  //
  //						if (bFind)
  //						{
  //							break;
  //						}
  //					}
  //				}
  //
  //				if (bFind)
  //				{
  //					//就是此单据行的，不能删除
  //					continue;
  //				}
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo2 = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo2.setPk_billrow(btvo.getCbill_bid());
  //				fvo2.setStatus(3); //状态为删除
  //				vTemp.addElement(fvo2);
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo.setPk_subequipment(btvo.getCfadeviceid());
  //				fvo.setStatus(1); //状态为修改
  //				vTemp.addElement(fvo);
  //			}
  //		}
  //		else
  //		{
  //			//入库单修改标志
  //			for (int i=0;i<bvo.getChildrenVO().length;i++)
  //			{
  //				BillItemVO btvo = (BillItemVO)bvo.getChildrenVO()[i];
  //				if (btvo.getCfadeviceid() == null)
  //				{
  //					continue;
  //				}
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo.setStatus(1); //状态为修改
  //				fvo.setInstorage_flag(new Integer(0)); //未入库
  //				fvo.setPk_subequipment(btvo.getCfadeviceid());
  //
  //				vTemp.addElement(fvo);
  //			}
  //		}
  //
  //		if (vTemp.size() != 0)
  //		{
  //			fvos = new nc.vo.fa.outer.FaSubequipmentVO[vTemp.size()];
  //			vTemp.copyInto(fvos);
  //			nc.bs.fa.outer.FaSubequipmentDMO dmo = new
  // nc.bs.fa.outer.FaSubequipmentDMO();
  //			if (bIsOut)
  //				dmo.save(fvos);
  //			else
  //				dmo.updateInstorageFlag(fvos);
  //		}
  //	}
  //}
  /**
   * 函数功能:处理批次号入库单据累计发出数量
   * 
   * 参数: BillVO bvo ----- 要处理的单据
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  private void doWithOutBatch(BillVO bvo, int iStatus) throws Exception {

    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

    if ((btvos != null) && (btvos.length != 0)) {
      //处理红字单据，判断是否有批次号
      for (int i = 0; i < btvos.length; i++) {
        BillItemVO btvo = btvos[i];

        String sVBatch = btvo.getVbatch();

        if ((sVBatch == null) || (sVBatch.toString().trim().length() == 0)) {
          //没有批次号，不处理
          continue;
        }

        //有批次号
        String sBill_bID = btvo.getCinbillitemid();
        UFDouble dNumber = btvo.getNnumber();

        if ((sBill_bID != null) && (sBill_bID.trim().length() != 0)) {
          if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
            //如果是增加或删除，直接修改被调整单据的数量
            double dNum = dNumber.doubleValue();

            String sOper = "+";

            if (iStatus == ConstVO.DELETE_STATUS) {
              //删除单据，减去被调整单据的累计发出数量
              sOper = "-";
            }

            this.updateBatchNumber(sBill_bID, sOper, dNum);
          }
        }
      }
    }
  }

  /**
   * 函数功能:处理红字单据累计回冲数量
   * 
   * 参数: BillVO bvo ----- 要处理的单据
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  private void doWithRedBill(BillVO bvo, int iStatus) throws Exception {
    CommonDataImpl cbo = new CommonDataImpl();

    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

    String sBillID = bhvo.getPrimaryKey();
    String sFunc = " isnull( ";
    boolean bFind = false;

    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
      if ((btvo.getCadjustbillitemid() != null)
          && (btvo.getCadjustbillitemid().trim().length() != 0)) {
        bFind = true;
        break;
      }
    }

    if (bFind == false) {
      //没有要调整的单据
      return;
    }

    //合法性检查
    if (iStatus == ConstVO.ADD_STATUS) {
      //如果是增加，判断数量是否够回冲的
      StringBuffer sSQL = new StringBuffer(" select ");
      sSQL.append(" a.irownumber,b.nnumber-" + sFunc
          + "b.nsettledretractnum,0) -" + sFunc + "b.nsettledsendnum,0)");
      sSQL.append(" from ");
      sSQL.append(" ia_bill_b a,ia_bill_b b");
      sSQL.append(" where ");
      sSQL.append(" a.cbillid = '" + sBillID + "'");
      sSQL.append(" and ");
      sSQL.append(" a.cadjustbillitemid = b.cbill_bid ");
      sSQL.append(" and ");
      sSQL
          .append(" a.nnumber > b.nnumber-isnull(b.nsettledretractnum,0) -isnull(b.nsettledsendnum,0) ");
      sSQL.append(" and ");
      sSQL.append(" a.nnumber < 0");
      sSQL.append(" and ");
      sSQL.append(" a.dr = 0");
      sSQL.append(" and ");
      sSQL.append(" b.dr = 0");

      String sResult[][] = cbo.queryData(sSQL.toString());
      String sRows = "";

      for (int i = 0; i < sResult.length; i++) {
        if (i != 0) {
          sRows = sRows + ";";
        }

        //sRows = sRows + "第" + sResult[i][0] + "行的存货当前允许的最大回冲数量为" +
        // sResult[i][1] + "\n";/*-=notranslate=-*/
        String[] value = new String[] { sResult[i][0], sResult[i][1] };
        sRows = sRows
            + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                "UPP20143010-000196", null, value) + "\n";
      }

      if (sRows.length() != 0) {
        BusinessException e = new BusinessException(sRows
            + ", "
            + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                "UPP20143010-000002")/* @res "请调整" */);
        throw e;
      }
    }

    String sOper = "+";

    if (iStatus == ConstVO.DELETE_STATUS) {
      //删除红冲单数据，减去被调整单据的累计回冲数量
      sOper = "-";
    }

    //update ia_bill_b set ia_bill_b.nsettledretractnum =
    // isnull(ia_bill_b.nsettledretractnum,0) +10
    //from ia_bill_b,v_ia_inoutledger b where ia_bill_b.cbill_bid =
    // b.cadjustbillitemid and b.cbillid = '1' and b.nnumber < 0 and
    // ia_bill_b.dr = 0

    //修改对应单据的累计回冲数量
    StringBuffer sSQL = new StringBuffer(" update ");
    sSQL.append(" ia_bill_b ");
    sSQL.append(" set ");
    sSQL.append(" ia_bill_b.nsettledretractnum = " + sFunc
        + " ia_bill_b.nsettledretractnum,0) " + sOper + " b.nnumber ");
    sSQL.append(" from ");
    sSQL.append(" ia_bill_b,v_ia_inoutledger b ");
    sSQL.append(" where ");
    sSQL.append(" ia_bill_b.cbill_bid = b.cadjustbillitemid ");
    sSQL.append(" and ");
    sSQL.append(" b.cbillid = '" + sBillID + "'");
    sSQL.append(" and ");
    sSQL.append(" b.nnumber < 0 ");
    sSQL.append(" and ");
    sSQL.append(" ia_bill_b.dr = 0 ");

    cbo.execData(sSQL.toString());
  }

  /**
   * 函数功能:获得模拟成本
   * 
   * 参数: BillVO bvo ----- 单据VO(无模拟成本) int iStatus ----- 状态 String sExistString
   * ----- 子查询语句（用于提高效率） String sPeriod ----- 当前会计期间 String sFQSK ----- 分期收款
   * String sWTDX ----- 委托代销 String sPeriodPerv ----- 前一个月 String sBeginDate
   * ----- 本月第一天 String sEndDate ----- 本月最后一天 Hashtable htt ----- 库存组织信息
   * Hashtable htInv ----- 存货信息
   * 
   * 返回值:单据VO(有模拟成本)
   * 
   * 异常:
   * 
   *  
   */
  private BillVO[] doWithSimulateCost(SimulateVO svo, int iStatus)
      throws Exception {
    BillVO[] bvos = svo.getBills();

    if (iStatus == ConstVO.UPDATE_STATUS) {
      //修改，将金额写入模拟成本
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

        //修改，将金额写入模拟成本
        for (int j = 0; j < btvos.length; j++) {
          UFDouble dMny = btvos[j].getNmoney();
          if (dMny != null) {
            btvos[j].setNsimulatemny(dMny);
          }
        }
      }
    } else if (iStatus == ConstVO.ADD_STATUS) {
      //是增加状态
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
        String sBillType = bhvo.getCbilltypecode();
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

        if (sBillType.equals(ConstVO.m_sBillJHJTZD)
            || sBillType.equals(ConstVO.m_sBillDJTQD)
            || sBillType.equals(ConstVO.m_sBillCYLJZD)) {
          //计划价调整单、特殊单据
          //将金额写入模拟成本
          for (int j = 0; j < btvos.length; j++) {
            UFDouble dMny = btvos[j].getNmoney();
            if (dMny != null) {
              btvos[j].setNsimulatemny(dMny);
            }
          }
        }
      }

      SimulateCost simuCost = new SimulateCost();
      bvos = simuCost.calcSimulateCost(svo);
    }

    return bvos;
  }

  /**
   * 函数功能:将存货核算单据VO用计划价写入实际价（为SEG提供）
   * 
   * 参数:
   * 
   * 返回值:
   * 
   * 异常:
   * 
   * 创建日期：(2002-12-10 9:01:17)
   */
  public BillVO fillVOByPlaned(BillVO bvo) throws BusinessException {
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //整理表体数据
      BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
      BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

      Integer[] iPeci = cbo.getDataPrecision(bhvo.getPk_corp());

      //得到单价为空需要进一步查询计划价的存货
      //Hashtable(key:存货主键+表体VO数组下标，value:表体VO数组中的值)
      Hashtable htInvID = new Hashtable();
      for (int i = 0; i < btvos.length; i++) {
        if (btvos[i].getNprice() == null) {
          htInvID.put(btvos[i].getCinventoryid() + "," + i, btvos[i]);
        }
      }
      if (htInvID.size() <= 0) {
        Log.info("没有单价为空的存货！");
        return bvo;
      }
      Enumeration e = htInvID.keys();
      String sKey = null;
      Vector vID = new Vector(1, 1);
      while (e.hasMoreElements()) {
        sKey = (String) e.nextElement();
        if (vID.indexOf(sKey.substring(0, sKey.indexOf(","))) < 0) {
          vID.addElement(sKey.substring(0, sKey.indexOf(",")));
        }
      }
      String[] s = new String[vID.size()];
      vID.copyInto(s);
      //查询存货信息，返回值:存货、计价方式、计划价、是否批次核算
      String[][] sResults = cbo.getPrices(bhvo.getPk_corp(), bhvo
          .getCrdcenterid(), s);

      //用计划价写入实际价
      if ((sResults != null) && (sResults.length > 0)) {
        UFDouble dPlanedPrice = null;
        UFDouble dNumber = null;
        UFDouble dPlanedMny = null;
        BillItemVO btvo = null;
        for (int i = 0; i < sResults.length; i++) {
          //获得计价方式编码

          //if (iPriceCode.intValue() == ConstVO.JHJ) //不再限制是计划价的
          {
            //是计划价计价，把计划价写入实际单价,并把计划金额写入实际金额
            dPlanedPrice = new UFDouble(sResults[i][2]);

            e = htInvID.keys();
            while (e.hasMoreElements()) {
              sKey = (String) e.nextElement();
              if (sKey.indexOf(sResults[i][0]) != -1) {
                btvo = (BillItemVO) htInvID.get(sKey);
                btvo.setNprice(dPlanedPrice);

                dNumber = btvo.getNnumber();
                if (dNumber != null) {
                  dPlanedMny = dNumber.multiply(dPlanedPrice);

                  dPlanedMny = dPlanedMny.setScale(iPeci[2].intValue(),
                      UFDouble.ROUND_HALF_UP);
                  btvo.setNmoney(dPlanedMny);
                }
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }

    return bvo;
  }

  /**
   * 函数功能:
   * 
   * 参数:
   * 
   * 返回值:
   * 
   * 异常:
   * 
   * 创建日期：(2002-10-30 16:41:55)
   * 
   * @return nc.vo.ia.bill.BillItemVO
   * @param btvo
   *          nc.vo.ia.bill.BillItemVO
   */
  //20050527_关闭存货与固定资产接口
  //private BillItemVO getFaCardInfo(BillItemVO btvo) throws Exception
  //{
  //	//获得固定资产数据
  //	if (btvo.getCfacardid() != null)
  //	{
  //		nc.bs.fa.fa20120201.CardDMO cdmo = new nc.bs.fa.fa20120201.CardDMO();
  //		nc.vo.fa.fa20120201.CardVO cvo =
  // cdmo.findByPrimaryKey(btvo.getCfacardid());
  //
  //		if (cvo != null)
  //		{
  //			btvo.setCfadevicecode(cvo.getAsset_code());
  //			btvo.setCfadevicename(cvo.getAsset_name());
  //		}
  //	}
  //	return btvo;
  //}

  /**
   * 函数功能:获得销售期初数据
   * 
   * 参数: String sCorpID ----- 单位编码 nc.vo.pub.lang.UFDate dDate ----- 截止日期
   * 
   * 返回值:
   * 
   * 异常:
   *   
   */
  public nc.vo.ia.outter.SettledInfoVO[] getSaleInit(String sCorpID,
      nc.vo.pub.lang.UFDate dDate) throws BusinessException {
    nc.vo.ia.outter.SettledInfoVO[] svos = null;

    try {
      Object oInstance = NCLocator.getInstance().lookup(ISOToIA.class.getName());
      svos = ((ISOToIA) oInstance).getInitSale(sCorpID, dDate);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return svos;
  }

  /**
   * 函数功能:向数据库中插入一批VO对象
   * 
   * 参数: BillVO[] bills ----- 单据
   * 
   * 返回值:单据数组
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] insertArray(BillVO[] bills) throws BusinessException {

    if ((bills != null) && (bills.length != 0)) {
      String sCorpID = ((BillHeaderVO) bills[0].getParentVO()).getPk_corp();
      String sUserID = ((BillHeaderVO) bills[0].getParentVO()).getCoperatorid();

      //ClientLink(String pk_corp, String user, UFDate date, String
      // accountYear, String accountMonth, UFDate yearMonth, UFDate monthStart,
      // UFDate monthEnd, String language, boolean isDebug, String moduleName,
      // String moduleCode, String moduleID)
      //构建连接环境
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      return this.insertArray(cl, bills);

    }
    return null;

  }

  /**
   * 获取自由项
   * 
   * @param bills
   * @return
   */
  private BillVO[] setFreeItems(BillVO[] bills) throws Exception {
    if (bills != null) {
      //整理自由项数据
      ArrayList al = new ArrayList();

      for (int i = 0; i < bills.length; i++) {
        ArrayList tempal = new ArrayList();
        BillItemVO[] items = (BillItemVO[]) bills[i].getChildrenVO();
        int iLen = items != null ? items.length : 0;
        for (int j = 0; j < iLen; j++) {
          BillItemVO btvo = items[j];

          //处理与固定资产接口//20050527_关闭存货与固定资产接口
          // btvo = getFaCardInfo(btvo);

          String sInvID = btvo.getCinventoryid();

          tempal.add(sInvID);
        }

        al.add(tempal);
      }

      IDefine defbo = (IDefine)NCLocator.getInstance().lookup(IDefine.class.getName());
      ArrayList alResult = defbo.queryFreeVOByInvIDsGroupByBills(al);

      int iLength = alResult.size();
      for (int i = 0; i < iLength; i++) {
        ArrayList tempal = (ArrayList) alResult.get(i);
        int iLength2 = tempal.size();
        for (int j = 0; j < iLength2; j++) {
          BillItemVO btvo = (BillItemVO) bills[i].getChildrenVO()[j];
          FreeVO fvo = (FreeVO) tempal.get(j);

          for (int h = 1; h <= 10; h++) {
            String sName = "vfree" + h;
            fvo.setAttributeValue(sName, btvo.getAttributeValue(sName));
          }

          btvo.setVOFree(fvo);
          btvo.setVfree0(fvo.getVfree0());
        }
      }
    }

    return bills;
  }

  /**
   * 函数功能:获得单据，与其它表关联
   * 
   * 参数: String[] sTable ----- 要连接的表 String[] sConnectParam ----- 连接条件 String[]
   * sCondition ----- 其它条件 BillVO condBillVO ------ 单据条件VO String sOrderPart
   * ------ 排序字段 BillVO bResultVO ----- 已查询出的VO Boolean bHasInv -----
   * 是否要查询出存货的编码等信息
   * 
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public BillVO[] querybillWithOtherTable(String[] sTable,
      String[] sConnectParam, String[] sCondition, BillVO condBillVO,
      String sOrderPart, BillVO bResultVO, Boolean bHasInv, ClientLink cl)
      throws BusinessException {
    BillVO[] bills = null;
    try {
      BillDMO dmo = new BillDMO();

      bills = dmo.querybillWithOtherTable(sTable, sConnectParam, sCondition,
          condBillVO, sOrderPart, bResultVO, bHasInv, cl);

      bills = this.setFreeItems(bills);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return bills;
  }

  /**
   * 函数功能:根据VO中所设定的条件返回所有符合条件的VO数组
   * 
   * 参数: BillItemVO condBillVO ----- VO数组 boolean isAnd ----- 是否是与的条件 String
   * sOrder ----- 排序条件
   * 
   * 返回值:单据数值对象
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] queryByVO(BillVO condBillVO, Boolean isAnd, String sOrder,
      ClientLink cl) throws BusinessException {
    BillVO[] bills = null;

    try {
      BillDMO dmo = new BillDMO();
      bills = dmo.queryByVO(condBillVO, isAnd, sOrder, cl);
      bills = this.setFreeItems(bills);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return bills;
  }

  /**
   * 根据表头主键查询表体某些字段,此方法只限查询少量字段时使用，查询表体或大量字段用其他方法
   */
  public HashMap queryBodyItems(String[] keys, String item, ClientLink cl)
      throws BusinessException {
    HashMap hmValue = null;
    try {
      BillDMO dmo = new BillDMO();
      hmValue = dmo.queryBodyItems(keys, item, cl);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return hmValue;
  }

  /**
   * 函数功能:为外系统查询单据
   * 
   * 参数: String sSourceHID ----- 来源单据ID boolean bIsHeader ----- 是否是表头 String
   * sSQL ----- 自定义查询语句
   * 
   * 返回值:单据数值对象
   * 
   * 异常:Exception
   *  
   */
  private BillVO[] queryByVOForOutter(String sSourceID, boolean bIsHeader,
      String sSQL) throws Exception {
    BillVO[] bill = null;

    BillVO conVO = new BillVO();
    BillItemVO[] contvos = new BillItemVO[1];

    BillItemVO contvo = new BillItemVO();
    if (bIsHeader) {
      contvo.setCsourcebillid(sSourceID);
    } else {
      //是表体ID
      contvo.setCsourcebillitemid(sSourceID);
    }

    if ((sSQL != null) && (sSQL.trim().length() != 0)) {
      contvo.setCSQLClause(sSQL);
    }

    contvos[0] = contvo;

    conVO.setChildrenVO(contvos);

    BillDMO dmo = new BillDMO();

    //	if (bIsHeader)
    //	{
    //ClientLink(String pk_corp, String user, UFDate date, String accountYear,
    // String accountMonth, UFDate yearMonth, UFDate monthStart, UFDate
    // monthEnd, String language, boolean isDebug, String moduleName, String
    // moduleCode, String moduleID)
    //构建连接环境
    ClientLink cl = new ClientLink(null, null, null, null, null, null, null,
        null, null, false, null, null, null);

    bill = dmo.queryByVO(conVO, null, "", cl);
    //	}
    //	else
    //	{
    //		bill = dmo.queryWithOtherTableHaveInv(null,null,null,conVO,null);
    //	}

    //查询外系统单据时，将表头ts置空，用旧的检查方法
    if (bill != null) {
      for (int i = 0; i < bill.length; i++) {
        ((BillHeaderVO) bill[i].getParentVO()).setTs(null);
      }
    }

    return bill;
  }

  /**
   * 函数功能:根据VO中所设定的条件返回所有符合条件的VO数组
   * 
   * 参数: BillItemVO condBillVO ----- VO数组 boolean isAnd ----- 是否是与的条件 String
   * tempTableName ----- 临时表名
   * 
   * 返回值:单据数值对象
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] queryMonthBillByVO(BillVO condBillVO, Boolean isAnd,
      String tempTableName) throws BusinessException {
    BillVO[] bills = null;

    try {
      BillDMO dmo = new BillDMO();
      bills = dmo.queryMonthBillByVO(condBillVO, isAnd, tempTableName);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return bills;
  }

  /**
   * 函数功能:获得单据，与其它表关联
   * 
   * 参数: String[] sTable ----- 要连接的表 String[] sConnectParam ----- 连接条件 String[]
   * sCondition ----- 其它条件 BillVO condBillVO ------ 单据条件VO String sOrderPart
   * ------ 排序字段
   * 
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public BillVO[] queryWithOtherTableHaveInv(String[] sTable,
      String[] sConnectParam, String[] sCondition, BillVO condBillVO,
      String sOrderPart) throws BusinessException {
    BillVO[] bills = null;

    try {
      BillDMO dmo = new BillDMO();

      bills = dmo.queryWithOtherTableHaveInv(sTable, sConnectParam, sCondition,
          condBillVO, sOrderPart);

      bills = this.setFreeItems(bills);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }

    return bills;
  }

  /**
   * 函数功能:根据产品组自己的需求，把源VO中信息通过运算， 利用平台转换后VO,加上自己的特设需求进行转换。
   * 
   * 参数: AggregatedValueObject preVo ----- 来源VO AggregatedValueObject nowVo
   * ----- 目标VO
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
      AggregatedValueObject nowVo) throws BusinessException {
    //记录时间花费
    Timer timer = new Timer();
    timer.start();
    timer.addExecutePhase("存活核算   VO交换后续类处理"/*notranslate*/);
    try {
      if ((preVo == null) || (nowVo == null)) {
        return nowVo;
      }
      if (nowVo instanceof BillVO) {
        //是存货核算的单据VO
        BillHeaderVO bhvo = (BillHeaderVO) nowVo.getParentVO();
        BillItemVO[] btvos = (BillItemVO[]) nowVo.getChildrenVO();
        //针对不同来源作转换
        if (preVo instanceof nc.vo.ic.pub.bill.GeneralBillVO) {
          this.changeBusiVOForICNew(preVo, bhvo, btvos);
        } else if (preVo instanceof nc.vo.so.so012.SquareVO) {
          this.changeBusiVOForSONew(preVo, bhvo, btvos);
        } else if (preVo instanceof nc.vo.to.to303.SFinanceVO) {
          //this.changeBusiVOForTO(preVo, bhvo, btvos);
        }
      }
      timer.showAllExecutePhase("存货核算后续类：");
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return nowVo;
  }
  
  /**
   * 函数功能:根据产品组自己的需求，把源VO中信息通过运算， 利用平台转换后VO,加上自己的特设需求进行转换。
   * 
   * 参数: AggregatedValueObject preVo ----- 来源VO AggregatedValueObject nowVo
   * ----- 目标VO
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public AggregatedValueObject[] retChangeBusiVOs(
      AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
      throws BusinessException {
    try {
      if ((preVo == null) || (nowVo == null) || (preVo.length != nowVo.length)
          || (nowVo.length == 0)) {
        return nowVo;
      }
      for (int i = 0; i < preVo.length; i++) {
        nowVo[i] = this.retChangeBusiVO(preVo[i], nowVo[i]);
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return nowVo;
  }

  /**
	 * 功能：对从销售传递过来的VO执行转换
	 * @param preVo 转换前ＶＯ（外系统ＶＯ）
	 * @param bhvo　转换后表头ＶＯ
	 * @param btvos　转换后表体ＶＯ
	 * @throws BusinessException　
	 */
	private void changeBusiVOForSONew(AggregatedValueObject preVo, 
			BillHeaderVO bhvo, BillItemVO[] btvos) throws BusinessException {
		// 是销售管理传入的单据
		SquareVO svo = (SquareVO) preVo;
		SquareHeaderVO shvo = (SquareHeaderVO) svo.getParentVO();
		SquareItemVO[] stvos = (SquareItemVO[]) svo.getChildrenVO();
		CommonDataImpl cbo = new CommonDataImpl();
		// 数据精度
		Integer[] iPrecision = cbo.getDataPrecision(bhvo.getPk_corp());
		// 获得销售单据数据
		String sCorpID = shvo.getPk_corp();
		String sBillType = shvo.getCreceipttype(); // 单据类型
		String sBillID = shvo.getCsaleid(); // 单据ID

		if ((sBillType == null) || (sBillType.trim().length() == 0)
				|| (sBillID == null) || (sBillID.trim().length() == 0)) {
			BusinessException be = new BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
							"20143010", "UPP20143010-000012")/*"没有来源单据表头信息，请调整"*/);
			throw be;
		}

		// 获得业务类型
		String sBizTypeID = bhvo.getCbiztypeid();

		// 获得分期收款、委托代销、直运的业务类型
		String[] sBizs = new String[3];
		sBizs[0] = ConstVO.m_sBizFQSK;
		sBizs[1] = ConstVO.m_sBizWTDX;
		sBizs[2] = ConstVO.m_sBizZY;

		java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
		String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
		String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);
		String sZY = (String) ht.get(ConstVO.m_sBizZY);

		// 处理表体
		for (int i = 0; i < btvos.length; i++) {

			if ((stvos[i].getCorder_bid() == null)
					|| (stvos[i].getCorder_bid().trim().length() == 0)) {
				BusinessException be = new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"20143010", "UPP20143010-000013")/*"没有来源单据表体信息，请调整"*/);
				throw be;
			}

			if (sBillType.equals(ConstVO.m_sBillXSFP)) {
				// 是销售发票
				if ((sFQSK.indexOf(sBizTypeID) != -1)
						|| (sWTDX.indexOf(sBizTypeID) != -1)) {
					// 是分期收款、委托代销业务，来源为发票
					String sSaleBillID = stvos[i].getCupbillid(); // 单据ID
					String sSaleBodyID = stvos[i].getCupbillbodyid();

					if ((sSaleBillID == null)
							|| (sSaleBillID.trim().length() == 0)
							|| (sSaleBodyID == null)
							|| (sSaleBodyID.trim().length() == 0)) {
						BusinessException be = new BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("20143010",
												"UPP20143010-000014")/*"此单据是发票，但没有指定对应的销售出库单，请调整"*/);
						throw be;
					}

					// 来源是销售的销售出库单
					btvos[i].setCsaleadviceid(sSaleBillID);
					btvos[i].setCcsaleadviceitemid(sSaleBodyID);
				}
			} else if (sBillType.equals(ConstVO.m_sBillXSCKD)) {
				// 是销售出库单
				btvos[i].setCsaleadviceid(stvos[i].getCsaleid());
				btvos[i].setCcsaleadviceitemid(stvos[i].getCorder_bid());

				// 判断数量
				if ((sBizTypeID != null) && (sBizTypeID.trim().length() != 0)) {
					if ((sFQSK.indexOf(sBizTypeID) != -1)
							|| (sWTDX.indexOf(sBizTypeID) != -1)) {
						// 是分期收款、委托代销业务，来源为出库单
						if ((stvos[i].getNoutnum() != null)
								&& (stvos[i].getNnewbalancenum() != null)
								&& (stvos[i].getNoutnum().doubleValue() != stvos[i]
										.getNnewbalancenum().doubleValue())) {
							// 不是对出库单一次结清
							BusinessException be = new BusinessException(
									nc.bs.ml.NCLangResOnserver.getInstance()
											.getStrByID("20143010",
													"UPP20143010-000015")/*"此单据是出库单，且是分期收款或委托代销业务，但没有结算完所有数量，请调整"*/);
							throw be;
						}
					}
				}
			}

			// 设置源头单据信息
			if (stvos[i].getCreceipttype() != null) {
				// 单据没有源头，单据的来源是存货核算单据的源头
				btvos[i].setCfirstbilltypecode(stvos[i].getCreceipttype());
				btvos[i].setCfirstbillid(stvos[i].getCsourcebillid());
				btvos[i].setCfirstbillitemid(stvos[i].getCsourcebillbodyid());
			} else if (stvos[i].getCupreceipttype() != null) {
				// 没有源头，单据的来源是存货核算单据的源头
				btvos[i].setCfirstbilltypecode(stvos[i].getCupreceipttype());
				btvos[i].setCfirstbillid(stvos[i].getCupbillid());
				btvos[i].setCfirstbillitemid(stvos[i].getCupbillbodyid());
			} else {
				// 单据没有源头，单据的来源是存货核算单据的源头
				btvos[i].setCfirstbilltypecode(null);
				btvos[i].setCfirstbillid(null);
				btvos[i].setCfirstbillitemid(null);
			}

			// 是否是直运采购销售
			if ((sBizTypeID != null) && (sBizTypeID.trim().length() != 0)
					&& (sZY.indexOf(sBizTypeID) != -1)) {
				// 是直运采购销售，将金额写入成本
				UFDouble d = stvos[i].getNcostmny();

				if (d != null) {
					d = d.setScale(iPrecision[2].intValue(),
							UFDouble.ROUND_HALF_UP);
				}

				btvos[i].setNmoney(d);

				if ((stvos[i].getNcostmny() != null)
						&& (stvos[i].getNnewbalancenum() != null)
						&& (stvos[i].getNnewbalancenum().doubleValue() != 0)) {
					d = d.div(stvos[i].getNnewbalancenum());
					d = d.setScale(iPrecision[1].intValue(),
							UFDouble.ROUND_HALF_UP);

					btvos[i].setNprice(d);
				}
			} else {// 如果不是直运采购销售，则传过来的是销售价格，存货价格需要重新计算
				// 单价、金额为空
				btvos[i].setNprice(null);
				btvos[i].setNmoney(null);
			}

		}
	}
	
  /**
	 * 功能：对从库存传递过来的VO执行转换
	 * @param preVo 转换前ＶＯ（外系统ＶＯ）
	 * @param bhvo　转换后表头ＶＯ
	 * @param btvos　转换后表体ＶＯ
	 * @throws BusinessException　
	 */
	private void changeBusiVOForICNew(AggregatedValueObject preVo, 
			BillHeaderVO bhvo, BillItemVO[] btvos) throws BusinessException {
    // 是库存管理传入的单据
    GeneralBillVO gvo = (GeneralBillVO) preVo;
    GeneralBillHeaderVO ghvo = (GeneralBillHeaderVO) gvo.getParentVO();
    GeneralBillItemVO[] gtvos = (GeneralBillItemVO[]) gvo.getChildrenVO();

    String sBillType = ghvo.getCbilltypecode(); // 单据类型

    if ((sBillType == null) || (sBillType.trim().length() == 0)) {
      BusinessException be = new BusinessException(nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000011")/* "没有来源单据类型，请调整" */);
      throw be;
    }

    if (sBillType.equals(ConstVO.m_sBillKCQTRKD)
        || sBillType.equals(ConstVO.m_sBillKCQTCKD)) {
      // 是库存的 其他入库单 或 其他出库单
      String sSourceBillType = gtvos[0].getCsourcetype();
      if ((sSourceBillType != null)
          && (sSourceBillType.equals(ConstVO.m_sBillKCZZD) 
              || sSourceBillType.equals(ConstVO.m_sBillKCCXD))) {
        // 来源是组装或拆卸单
        String sSourceBillID = gtvos[0].getCsourcebillhid();
        String sIDs[] = new String[1];
        sIDs[0] = sSourceBillID;
        Object oInstance = NCLocator.getInstance().lookup(IICToIA.class.getName());
        UFDouble[] dCost = ((IICToIA) oInstance).getNfixdisassemblymny(sIDs);
        if ((dCost != null) && (dCost.length > 0)) {
          bhvo.setNcost(dCost[0]);
        }
      }
    }

    for (int i = 0; i < btvos.length; i++) {
      // 设置源头单据信息
      if ((gtvos[i].getCfirsttype() != null)
          && (!bhvo.getCbilltypecode().equals(ConstVO.m_sBillDBRKD)
              || gtvos[i].getCfirsttype().equals(ConstVO.m_sBillSFDBDD) || gtvos[i]
              .getCfirsttype().equals(ConstVO.m_sBillGSJDBDD))) {
        // 库存单据的源头是存货核算单据的源头
        btvos[i].setCfirstbilltypecode(gtvos[i].getCfirsttype());
        btvos[i].setCfirstbillid(gtvos[i].getCfirstbillhid());
        btvos[i].setCfirstbillitemid(gtvos[i].getCfirstbillbid());
        btvos[i].setVfirstbillcode(gtvos[i].getVfirstbillcode());
      }
      else if (gtvos[i].getCsourcetype() != null) {
        // 库存没有源头，库存单据的来源是存货核算单据的源头
        // 对来源不是三方和公司间的调拨入库单，源头记为调拨入库单的来源，便于成本计算处理
        btvos[i].setCfirstbilltypecode(gtvos[i].getCsourcetype());
        btvos[i].setCfirstbillid(gtvos[i].getCsourcebillhid());
        btvos[i].setCfirstbillitemid(gtvos[i].getCsourcebillbid());
        btvos[i].setVfirstbillcode(gtvos[i].getVsourcebillcode());
      }
    }
  }


  
	/**
	 * 查询仓储库存组织对应的成本库存组织，并检查单据中的出入库仓库是否属于同一成本库存组织
	 * 如果是则存货核算不接收该单据 
	 * @param bvos
	 */
  private BillVO[] changeAndCheckCalbodys(BillVO[] bvos, String sSourceModule,String sCorpID) throws BusinessException {
  	if( (bvos == null) || (bvos.length == 0) ){
  		return new BillVO[0];
  	}
  	//组织数据，他同一公司的库存组织+仓库组织到一起
  	Map mCorp2CalBodyWareHouses = new HashMap();
    CommonDataImpl cbo = new CommonDataImpl();
  	for( int i = 0; i < bvos.length; i ++){
  		/*  处理本公司库存组织 +　仓库*/
  		BillHeaderVO bhvo = (BillHeaderVO)bvos[i].getParentVO();
  		String corp  = bhvo.getPk_corp();
      String[] bodyHouse = new String[2];
      bodyHouse[0] = bhvo.getCstockrdcenterid();
      bodyHouse[1] = bhvo.getCwarehouseid();
      if( !mCorp2CalBodyWareHouses.containsKey(corp)){
      	ArrayList arrayBodyHouses = new ArrayList();
      	arrayBodyHouses.add(bodyHouse);
      	mCorp2CalBodyWareHouses.put(corp, arrayBodyHouses);
      }else{
      	ArrayList arrayBodyHouses = (ArrayList)mCorp2CalBodyWareHouses.get(corp);
      	arrayBodyHouses.add(bodyHouse);
      	mCorp2CalBodyWareHouses.put(corp, arrayBodyHouses);
      }
      /* 处理对方公司库存组织 + 仓库*/
			String[] otherBodyHouse = new String[2];
			String othercorp = bhvo.getCothercorpid();
      if ((othercorp != null) && (bhvo.getCothercalbodyid() != null)){
				otherBodyHouse[0] = bhvo.getCothercalbodyid();
				otherBodyHouse[1] = bhvo.getCotherwarehouseid();
			}else{//zlq edit 库存有可能没有设置其它库存组织和其他公司
				if( bhvo.getCotherwarehouseid() != null) {
					String sql = " select pk_corp, pk_calbody from bd_stordoc where " +
							"pk_stordoc ='" + bhvo.getCotherwarehouseid() +"'";
					String [][] rst = cbo.queryData(sql);
					bhvo.setCothercorpid(rst[0][0]);
					othercorp = rst[0][0];
					bhvo.setCothercalbodyid(rst[0][1]);
					otherBodyHouse[0] = bhvo.getCothercalbodyid();
					otherBodyHouse[1] = bhvo.getCotherwarehouseid();
				}else{
					continue;
				}
			}
      if( !mCorp2CalBodyWareHouses.containsKey(othercorp)){
      	ArrayList arrayBodyHouses = new ArrayList();
      	arrayBodyHouses.add(otherBodyHouse);
      	mCorp2CalBodyWareHouses.put(othercorp, arrayBodyHouses);
      }else{
      	ArrayList arrayBodyHouses = (ArrayList)mCorp2CalBodyWareHouses.get(othercorp);
      	arrayBodyHouses.add(otherBodyHouse);
      	mCorp2CalBodyWareHouses.put(othercorp, arrayBodyHouses);
      }
  	}
  	
    //转换库存组织,将仓储库存组织转为成本库存组织
  	OutterDMO dmo4 = null;
  	try{
  		dmo4 = new OutterDMO();  		
  	}catch(Exception e){
  		throw new BusinessException(e);
  	}
  	Set keys = mCorp2CalBodyWareHouses.keySet();
		String[] sCorps = (String[])keys.toArray(new String[keys.size()]); 
		Map mCorpCalBodyWareHouse2CostBody = new HashMap();
  	for( int i = 0 ; i < sCorps.length ; i++){
  		//获取成本库存组织
			String[] sCostBodys = dmo4.chgCalbodyICToIA(sCorps[i],
					(ArrayList)mCorp2CalBodyWareHouses.get(sCorps[i]));
			//以公司+库存组织+仓库为键，以成本库存组织为值保存结果
			if (sCostBodys != null){
        for (int j=0;j<sCostBodys.length;j++){
          String[] sBodyHouses = (String[])((ArrayList)mCorp2CalBodyWareHouses
          		.get(sCorps[i])).get(j);
          mCorpCalBodyWareHouse2CostBody.put(sCorps[i] + "," 
          		+ sBodyHouses[0] + "," + sBodyHouses[1],sCostBodys[j]);
        }
      }
  	}
  	
    //判断是否是同一成本域库存组织间的转换
    ArrayList alInsertBills = new ArrayList();
    for (int i = 0; i < bvos.length; i++) {
			BillHeaderVO bhvo = (BillHeaderVO) bvos[i].getParentVO();
			String corp = bhvo.getPk_corp();
			String othercorp = bhvo.getCothercorpid();
			String bodykey = corp + "," + bhvo.getCstockrdcenterid() + ","
					+ bhvo.getCwarehouseid();
			String costbody = (String) mCorpCalBodyWareHouse2CostBody.get(bodykey);
			//IA0033:库存同一成本域出入库仓库单据是否传入存货核算
			Hashtable ht = cbo.getParaValues(corp, new String[]{"IA0033"});
			String value = ht.get("IA0033") != null ? ((String)ht.get("IA0033")).trim() : "";
			if( value.equalsIgnoreCase("N")){
				//来源是库存且对方公司不为空且本公司不等于对方公司的情况才进行比较
				if (sSourceModule.equals(ConstVO.m_sModuleIC) && (othercorp != null)
						&& corp.equals(othercorp)) {
					String otherbodykey = othercorp + "," + bhvo.getCothercalbodyid() + ","
							+ bhvo.getCotherwarehouseid();
					String othercostbody = (String) mCorpCalBodyWareHouse2CostBody
							.get(otherbodykey);
					if (costbody.equals(othercostbody)) {
						// 是同一个库存组织的转换，存货核算不接收
						Log.info("------------IASaveBill------------单据出入成本域库存组织是同一个库存组织，不处理");
						continue;
					}
				}
			}
			bhvo.setCrdcenterid(costbody);
			alInsertBills.add(bvos[i]);
		}
    BillVO[] binsrtedVOs = new BillVO[alInsertBills.size()];
    binsrtedVOs = (BillVO[]) alInsertBills.toArray(binsrtedVOs);

    if (binsrtedVOs.length == 0) {
      //所有单据同一成本域
      Log.info("所有单据的出入库成本域库存组织相同，存货核算未接收");
    }
  	return binsrtedVOs;
  	 
  }

  /**
   * 函数功能:外部系统一批保存单据，为库存批签字用
   * 
   * 参数: BillVO[] bvos ----- 单据VO数组 sSourceModule ----- 来源模块 String
   * sSourceBillType ----- 来源单据类型
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public HashMap saveBillFromOutterArray(AggregatedValueObject[] bills,
      String sSourceModule, String sSourceBillType) throws BusinessException {
	 Log.debug("发票审核时执行此方法执行saveBillFromOutterArray"); 
    HashMap hmSourceBodyId2IaHeadBodyId = new HashMap();
    /*if (!(bills instanceof BillVO[])) {//红冲发票审核不会进入此判断
      System.out.println("saveBillFromOutterArray：参数ArregatedValueObject[]应为子类BillVO[]");
      throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
          .getStrByID("20143010", "UPP20143010-000016") @res "程序错误：请查看日志" );
    }        */
    //保存前按来源对单据预处理
    IBillBusinessProcesser processer = ProcesserBuilder.build(sSourceModule);
    if( processer != null){
      processer.preSaveProcess((BillVO[])bills,null);
    }
    //进行集中采购的相关处理
    ProcessJICAI jicai = new ProcessJICAI(); 
    ArrayList list = jicai.execute(bills, sSourceModule);
    System.out.println("list的长度为："+list.size());
    for (int l = 0; l < list.size(); l++) {
      //记录时间花费
      Timer timer = new Timer();
      timer.start();
			BillVO[] bvos = (BillVO[]) list.get(l);
			boolean bAccountLocked = false; 
			String sUserID = "";
			try {
				// 1、基本判断
				// 单据数组
				if ((bvos == null) || (bvos.length == 0)) {
					continue;
				}
				// 来源模块(由于对冲单审核的是销售发票，不执行下面的if判断)
			/*	if ((sSourceModule == null) || (sSourceModule.trim().length() == 0)) {
					BusinessException e = new BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
									"UPP20143010-000017")
																				 * @res "来源模块不能为空，请调整"
																				 );
					throw e;
				}*/
				// 用第一个单据的头VO获得公司、日期等
				BillHeaderVO curbhvo = (BillHeaderVO) bvos[0].getParentVO();
				String sCorpID = curbhvo.getPk_corp();
				sUserID = curbhvo.getCoperatorid();//0001A2100000000F7X61
				String sDate = curbhvo.getDbilldate().toString();
				curbhvo.setVbillcode(null);
				// 构建连接环境
				ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null,
						null, null, null, null, false, null, null, null);
				AccountImpl abo = new AccountImpl();
				CommonDataImpl cbo = new CommonDataImpl();
				// 处理月末结帐和日常录入单据之间的并发操作
				bAccountLocked = this.lockAccount((BillVO) bvos[0]);//false
				// 获得当前的会计期间
				String sPeriod = cbo.getPeriod(sCorpID, sDate);//2019-07
				// 对单据进行判断
				// 来源系统是否正确，将可以处理的单据进行下步处理
				String[] sModuleCode = new String[5];
				sModuleCode[0] = ConstVO.m_sModuleCodeSO;
				sModuleCode[1] = ConstVO.m_sModuleCodePO;
				sModuleCode[2] = ConstVO.m_sModuleCodeSC;
				sModuleCode[3] = ConstVO.m_sModuleCodeIA;
				sModuleCode[4] = ConstVO.m_sModuleCodeTO;//[4006, 4004, 4012, 2014, 4009]
				Hashtable httt = cbo
						.isModuleArrayStarted(sCorpID, sModuleCode, sPeriod);
				UFBoolean bSOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeSO); // 销售是否启用，Y
				UFBoolean bPOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodePO); // 采购是否启用，Y
				UFBoolean bSCStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeSC); // 委外加工是否启用，Y
				UFBoolean bIAStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeIA); // 存货核算是否启用，Y
				UFBoolean bTOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeTO); // 内部交易是否启用，Y
				//bIAStart为Y，不进下面的if判断
				/*if (bIAStart.booleanValue() == false) {
					// 存货核算没有启用
					System.out.println("------------IASaveBill------------存货核算没有启用，不接收单据，返回");
					return null;
				}*/
				// 检查库存是否有重复单据
				this.checkICDupBills((BillVO[]) bvos, sSourceModule, sSourceBillType);//sSourceModule='so'，sSourceBillType='32'
				// 获得业务类型
				String[] sBizs = new String[2];//[FQSK, WTDX]
				sBizs[0] = ConstVO.m_sBizFQSK;//分期收款
				sBizs[1] = ConstVO.m_sBizWTDX;//委托代销
				java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);//{WTDX='110711100000000000SI','110711100000000000SE','0001AA000000000003H9', FQSK='0001AA000000000003HA'}
				String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);//'0001AA000000000003HA'
				String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);//'110711100000000000SI','110711100000000000SE','0001AA000000000003H9'
				// 获得数据精度
				Integer[] iPeci = cbo.getDataPrecision(sCorpID);//[8, 8, 2, 6, 4, 2]
				// 是否计算虚拟成本
				String sSimulate = cbo.getParaValue(sCorpID, new Integer(
						ConstVO.m_iPara_SFJSXNCB));//sSimulate="Y"
				SimulateVO svo = new SimulateVO();
				svo.setFQSK(sFQSK);
				svo.setWTDX(sWTDX);
				svo.setPeci(iPeci);
				Vector vBills = new Vector(1, 1); // 记录要处理的单据
				Vector vAuditBills = new Vector(1, 1);
				// ArrayList alCalbodys = new ArrayList();
				// ArrayList alOtherCalbodys = new ArrayList();
				// //记录另一方的成本库存组织，看是否是一致的，如果一致就不传入存货核算
				// 对每个单据分别判断是否可记入存货核算单据
				// 同时获得存货ID
				System.out.println("bvos的长度为："+bvos.length);
				for (int j = 0; j < bvos.length; j++) {
					BillVO bvo = (BillVO) bvos[j];
					if ((bvo.getParentVO() == null) || (bvo.getChildrenVO() == null)
							|| (bvo.getChildrenVO().length == 0)) {
						continue;
					}
					BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
					BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[0];
          IAContext context = IAContext.create(bhvo.getPk_corp(), bhvo.getDbilldate().toString(), bhvo.getCoperatorid());
					btvo.setVbillcode(null);
					if (bhvo.getCstockrdcenterid() == null) {
						bhvo.setCstockrdcenterid(bhvo.getCrdcenterid());
					}
					if ((bhvo.getBcalculatedinvcost() != null)
							&& (bhvo.getBcalculatedinvcost().booleanValue() == false)) {
						// 不计算存货核算成本
						continue;
					}
					String sBillTypeCode = bhvo.getCbilltypecode();//I5
					String sBizTypeID = bhvo.getCbiztypeid();
					String sFirstBillTypeCode = btvo.getCfirstbilltypecode();
					if (bPOStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillCGRKD)
							&& (sSourceModule.equals(ConstVO.m_sModulePO) == false)) {//红冲审核不进一下if判断
						// 采购启用，不是采购传入的
						if ((bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()) {
							// 入库单，且入库仓库是VMI结算
							Log.info("-----IASaveBill------入库单，且入库仓库是VMI结算，不处理");
							continue;
						}
						BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
						Vector vBtvo = new Vector(1, 1);
						UFBoolean ufbLargess = null;
						for (int ii = 0; ii < btvos.length; ii++) {
							// 是否赠品:如果是赠品且数量不为0，只保留数量，清空单价和金额
							ufbLargess = btvos[ii].getBlargessflag();
							if ((ufbLargess != null) && ufbLargess.booleanValue()
									&& (btvos[ii].getNnumber() != null)
									&& (btvos[ii].getNnumber().doubleValue() != 0.0)) {
								btvos[ii].setNmoney(new UFDouble(0));
								btvos[ii].setNprice(new UFDouble(0));
								vBtvo.addElement(btvos[ii]);
							}
						}
						if (vBtvo.size() > 0) {
							BillItemVO[] btvosTemp = new BillItemVO[vBtvo.size()];
							vBtvo.copyInto(btvosTemp);
							bvo.setChildrenVO(btvosTemp);
						} else {
							Log.info("--IASaveBill--采购系统启用，此单据不是采购传入的，且不包含赠品，返回");
							continue;
						}
					} else if (bSCStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillWWJGSHD)
							&& (sSourceModule.equals(ConstVO.m_sModulePO) == false)) {//红冲审核不进此判断
						// 委外加工启用，不是采购传入的
						Log.info("---IASaveBill--委外加工系统已经启用，但单据不是采购传入的，继续");
						continue;
					} else if (bSOStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)
							&& (sSourceModule.equals(ConstVO.m_sModuleSO) == false)) {//红冲审核不进此判断
						
						String U8BillType = (String)btvo.getAttributeValue("vbilltypeu8rm");
						
						if (U8BillType==null || U8BillType.length()==0){
						// 销售启用，不是销售传入的
							Log.info("----IASaveBill-----销售系统已经启用，但单据不是销售传入的，又不是来源于U8零售接口，继续");
							continue;
						}
					} else if (bTOStart.booleanValue()
							&& (sSourceModule.equals(ConstVO.m_sModuleTO) == false)
							&& (sBillTypeCode.equals(ConstVO.m_sBillDBRKD) || sBillTypeCode
									.equals(ConstVO.m_sBillDBCKD))
							&& (sFirstBillTypeCode.equals(ConstVO.m_sBillSFDBDD) || sFirstBillTypeCode
									.equals(ConstVO.m_sBillGSJDBDD))
							&& !"4453".equals(btvo.getCsourcebilltypecode())/* 不是来源于途损单 */) {
						// 库存签字传入的调拨单，来源于三方调拨订单和公司间调拨订单,且不是来源于途损单
						if (sBillTypeCode.equals(ConstVO.m_sBillDBRKD)) {// 调拨入库单情况
							Object oTOTrans = NCLocator.getInstance().lookup(
									ITransferBill.class.getName());
							Object oTOPrice = NCLocator.getInstance().lookup(
									IOuter.class.getName());
							BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
							ArrayList alIANormalVOs = new ArrayList();// 准备接收的VO
							ArrayList alIALargessVOs = new ArrayList();// 存货接收的赠品
							ArrayList alICVOs = new ArrayList();// 回写库存的VO
							String icprice = "nzgprice1";
							String icmny = "nzgmny1";
							UFBoolean ufbLargess = null;
							for (int ii = 0; ii < btvos.length; ii++) {// 赠品，接收并置单价为零，不回写库存
								ufbLargess = btvos[ii].getBlargessflag();
								if ((ufbLargess != null) && ufbLargess.booleanValue()) {
									btvos[ii].setNmoney(new UFDouble(0));
									btvos[ii].setNprice(new UFDouble(0));
									alIALargessVOs.add(btvos[ii]);
								} else {// 非赠品，要查询调拨关系中参数是否暂估，暂估的接收，不暂估的不接收
									String orderID = btvos[ii].getCfirstbillitemid();
									if ((orderID == null) || (orderID.length() == 0)) {
										throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
												"20143010", "UPP20143010-000299") /*@"异常错误：调拨订单标识为空"*/);
									}
									String[] sIds = new String[] { orderID };
									AggregatedTransrelaVO[] relaVOs = ((ITransferBill) oTOTrans)
											.getTransInfoFromOrder(sIds);
									if ((relaVOs != null) && (relaVOs.length != 0)) {
										UFBoolean b = ((TransrelaItemVO) relaVOs[0].getChildrenVO()[0])
												.getBestimate();
										if ((b != null) && b.booleanValue()) {// 暂估的
											// 取暂估价格
											UFDouble[] prices = ((IOuter) oTOPrice)
													.getEstimatePriceForIA(sIds);
											//调整精度
                      UFDouble price = context.adjustPrice(prices[0]);
											UFDouble num = btvos[ii].getNnumber();
											UFDouble mny = num.multiply(prices[0]);
                      //调整精度
                      mny = context.adjustMoney(mny);
											btvos[ii].setNprice(price);
											btvos[ii].setNmoney(mny);
											alIANormalVOs.add(btvos[ii]);
											// 回写库存的VO
											PubVO pubVO = new PubVO();
											pubVO.setAttributeValue("cgeneralbid", btvos[ii]
													.getCicitemid());
											pubVO
													.setAttributeValue("btoinzgflag", new UFBoolean("Y"));
											pubVO.setAttributeValue(icprice, price);
											pubVO.setAttributeValue(icmny, mny);
											alICVOs.add(pubVO);
										} else {// 非暂估
											// 回写库存的VO
											PubVO pubVO = new PubVO();
											pubVO.setAttributeValue("cgeneralbid", btvos[ii]
													.getCicitemid());
											pubVO
													.setAttributeValue("btoinzgflag", new UFBoolean("N"));
											pubVO.setAttributeValue(icprice, null);
											pubVO.setAttributeValue(icmny, null);
											alICVOs.add(pubVO);
										}
									} else {
										throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
												"20143010", "UPP20143010-000300")/*@"异常错误：取调拨关系失败"*/);
									}
								}
							}
							// 将赠品行单据组成一个单据
							BillVO largessBvo = (BillVO) bvo.clone();
							BillItemVO[] largessItems = (BillItemVO[]) alIALargessVOs
									.toArray(new BillItemVO[alIALargessVOs.size()]);
							if (largessItems.length != 0) {
								largessBvo.setChildrenVO(largessItems);
								vBills.addElement(largessBvo);
								//在源头单据是三方调拨订单的情况下,生成调出公司的调拨出库单
								if( sFirstBillTypeCode.equals(ConstVO.m_sBillSFDBDD)) {
									BillVO outbill = (BillVO)largessBvo.clone();
									BillHeaderVO header = (BillHeaderVO)outbill.getParentVO();
									String srcCorp = header.getPk_corp();
									String srcStockRdcenterid = header.getCstockrdcenterid();
									String dstCorp = header.getCoutcorpid();
									String dstStockRdcenterid = header.getCoutcalbodyid();
									//String warehouse = header.getCwarehouseid();
									
									header.setPk_corp(dstCorp);
									//header.setCrdcenterid(header.getCoutcalbodyid());
									header.setCstockrdcenterid(dstStockRdcenterid);
									
									header.setCothercorpid(srcCorp);
									header.setCothercalbodyid(srcStockRdcenterid);
									//header.setCotherwarehouseid(warehouse);
									header.setCbilltypecode(ConstVO.m_sBillDBCKD);
									BillItemVO[] items = (BillItemVO[])outbill.getChildrenVO();
									for( int k = 0 ; k < items.length; k++){
										items[k].setPk_corp(dstCorp);
										items[k].setCbilltypecode(ConstVO.m_sBillDBCKD);
									}
									//对表体存货管理档案等信息进行公司间转换
									BillTool.mapDocsBetweenCorps(outbill, srcCorp, dstCorp);
									vBills.addElement(outbill);
								}
							}
							// 将整理后的表体放回存货单据中
							BillItemVO[] newItems = (BillItemVO[]) alIANormalVOs
									.toArray(new BillItemVO[alIANormalVOs.size()]);
							if (newItems.length != 0) {
								((BillHeaderVO) bvo.getParentVO())
										.setBestimateflag(UFBoolean.TRUE);
								bvo.setChildrenVO(newItems);
							} else {
								continue;
							}
							// 回写库存
							PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs
									.size()]);
							Object oICInstance = NCLocator.getInstance().lookup(
									IICToIA.class.getName());
							((IICToIA) oICInstance).rewriteZg1(pubVOs);
						} else if (sBillTypeCode.equals(ConstVO.m_sBillDBCKD)) {// 调拨出库单情况
							Object oTOTrans = NCLocator.getInstance().lookup(
									ITransferBill.class.getName());
							BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
							//准备接收的VO
							ArrayList alIAVOs = new ArrayList();
							//回写库存的VO
							ArrayList alICVOs = new ArrayList();
							String[] orderIDs = new String[btvos.length];
							for (int ii = 0; ii < btvos.length; ii++) {
								//获取调拨订单ID
								orderIDs[ii] = btvos[ii].getCfirstbillitemid();
							}
							AggregatedTransrelaVO[] relaVOs = null;
							try {//获取调拨关系
								relaVOs = ((ITransferBill) oTOTrans)
										.getTransInfoFromOrder(orderIDs);
							} catch (Exception e) {
								throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
										"20143010", "UPP20143010-000300")/*@"异常错误：取调拨关系失败"*/);
							}
							if ((relaVOs != null) && (relaVOs.length != 0)) {
								for (int i = 0; i < relaVOs.length; i++) {
									UFBoolean b = ((TransrelaItemVO) relaVOs[i].getChildrenVO()[0])
											.getBpushia();
									//如果是公司间调拨订单,所有赠品不受"出库签字是否传存货核算"参数影响,都传存货核算 
									UFBoolean ufbLargess = btvos[i].getBlargessflag();
									if ((ufbLargess != null) && ufbLargess.booleanValue() && 
											ConstVO.m_sBillGSJDBDD.equals(btvos[i].getCfirstbilltypecode())) {
										//btvos[i].setNmoney(new UFDouble(0));
										//btvos[i].setNprice(new UFDouble(0));
										alIAVOs.add(btvos[i]);
									} else 	if ((b != null) && b.booleanValue()) {
										//非公司间调拨订单赠品与普通行传存货
										alIAVOs.add(btvos[i]);// 写入存货
										//回写库存
										PubVO pubVO = new PubVO();
										pubVO.setAttributeValue("cgeneralbid", btvos[i]
												.getCicitemid());
										pubVO.setAttributeValue("btoouttoiaflag",
												new UFBoolean("Y"));
										alICVOs.add(pubVO);
									} else {
										//不传存货
										//回写库存
										PubVO pubVO = new PubVO();
										pubVO.setAttributeValue("cgeneralbid", btvos[i]
												.getCicitemid());
										pubVO.setAttributeValue("btoouttoiaflag",
												new UFBoolean("N"));
										alICVOs.add(pubVO);
									}
								}
							} else {
								throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
										"20143010", "UPP20143010-000300")/*@"异常错误：取调拨关系失败"*/);
							}
							// 将整理后的表体放回存货单据中
							BillItemVO[] newItems = (BillItemVO[]) alIAVOs
									.toArray(new BillItemVO[alIAVOs.size()]);
							bvo.setChildrenVO(newItems);
							// 回写库存
							PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs
									.size()]);
							Object oICInstance = NCLocator.getInstance().lookup(
									IICToIA.class.getName());
							((IICToIA) oICInstance).rewrite4Y2IAflag(pubVOs);
						}
					} else if (sSourceModule.equals(ConstVO.m_sModuleSO)
							&& sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)) {//红票审核时执行此判断
						if ((bvo.getChildrenVO() != null) && (bvo.getChildrenVO().length > 0)) {
							if (sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
								// 是销售发票
								String sSaleBodyID = btvo.getCcsaleadviceitemid();
								if (((sFQSK != null) && (sFQSK.indexOf(sBizTypeID) != -1))
										|| ((sWTDX != null) && (sWTDX.indexOf(sBizTypeID) != -1))) {
									// 分期收款或委托代销
									if ((sSaleBodyID != null) && (sSaleBodyID.trim().length() != 0)) {
										// 来源是销售的销售出库单
										StringBuffer sSQL = new StringBuffer(" select ");
										sSQL = sSQL.append(" count(*) ");
										sSQL = sSQL.append(" from ");
										sSQL = sSQL.append(" ia_bill_b ");
										sSQL = sSQL.append(" where ");
										sSQL = sSQL.append(" ccsaleadviceitemid = '" + sSaleBodyID
												+ "'");
										String sResult[][] = cbo.queryData(sSQL.toString());
										int iNum = 0;
										if (sResult.length != 0) {
											String sTemp[] = sResult[0];
											if (sTemp.length != 0) {
												iNum = new Integer(sTemp[0]).intValue();
											}
										}
										if (iNum == 0) {//红冲审核不进此判断
											BusinessException e = new BusinessException(
													nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
															"20143010", "UPP20143010-000018")
															/*@res "此单据来源是销售发票，且是分期收款或委托代销业务，
															 * 但没有找到对应的来源于出库单的单据，请调整"*/);
											throw e;
										}
									}
								}
							}
						}
					} else if (sSourceModule.equals(ConstVO.m_sModuleIC)) {//红冲审核不进此判断
						/*
						 * 库存单据签字时， 
						 * 入库单：入库仓库是vmi仓库的不能传存货核算。否则，消耗汇总时会再传一次，
						 * 重复，目前只对采购入库单进行了控制。
						 * 出库单：如果出库仓库和入库仓库都是vmi仓库，不进行消耗汇总的，
						 * 也不能传存货核算。否则存货核算将出现，入库一次，出库两次。
						 */
						if ((bhvo.getFdispatchflag() != null)
								&& (bhvo.getFdispatchflag().intValue() == 0)
								&& (bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()) {
							// 入库单，且入库仓库是VMI结算
							Log.info("-----IASaveBill-----入库单，且入库仓库是VMI结算，不处理");
							continue;
						}
						if ((bhvo.getFdispatchflag() != null)
								&& (bhvo.getFdispatchflag().intValue() == 1)
								&& (bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()
								&& (bhvo.getBoutgathersettle() != null)
								&& bhvo.getBoutgathersettle().booleanValue()) {
							// 出库单，且入库仓库是VMI结算且出库仓库是VMI结算
							Log.info("--IASaveBill--出库单，且入库仓库是VMI结算且出库仓库是VMI结算，不处理");
							continue;
						}
						// 过滤掉数量为0的纪录
						BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
						Vector vBtvo = new Vector(1, 1);
						for (int ii = 0; ii < btvos.length; ii++) {
							// 过滤掉数量为0的纪录
							if ((btvos[ii].getNnumber() != null)
									&& (btvos[ii].getNnumber().doubleValue() != 0.0)) {
								vBtvo.addElement(btvos[ii]);
							}
						}
						if (vBtvo.size() > 0) {
							BillItemVO[] btvosTemp = new BillItemVO[vBtvo.size()];
							vBtvo.copyInto(btvosTemp);
							bvo.setChildrenVO(btvosTemp);
						} else {
							Log.info("----IASaveBill---此单据所有表体行数量全为0，存货核算不处理，返回");
							continue;
						}
					} else if (sBillTypeCode.equals(ConstVO.m_sBillRKTZD)) {//红冲审核不进此判断
						/* 入库调整单 */
						BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
						int len = (btvos == null ? 0 : btvos.length);
						for (int i = 0; i < len; i++) {
							UFDouble adjustnum = btvos[i].getNadjustnum();
							if (adjustnum != null) {
								if (adjustnum.doubleValue() < 0) {
									btvos[i].setNadjustnum(null);
								} else {
									String sAdjbillid = btvos[i].getCadjustbillid();
									String sAdjbillitemid = btvos[i].getCadjustbillitemid();
									/*
									 * 如果结算方式为费用结算，调整单据ID和调整单据分录ID存放的是采购结算清单ID和采购结算清单分录ID；
									 * 如果结算方式为采购结算，调整单据ID和调整单据分录ID存放的是库存单据ID和库存单据分录ID；
									 */
									String sql = "select cbillid, cbill_bid from ia_bill_b where dr = 0 and csourcebillid ='"
											+ sAdjbillid
											+ "'"
											+ " and csourcebillitemid = '"
											+ sAdjbillitemid + "'";
									String sResult[][] = cbo.queryData(sql.toString());
									if (sResult.length == 0) {
										throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
										"UPP20143010-000301")/*@"没有查询到待调整的单据"*/);
									} else if (sResult.length > 1) {
										// 上述查询出来的结果多于1条，表明采购结算模式为单到回冲结算模式，将字段清空：调整数量
										btvos[i].setNadjustnum(null);
									} else if (sResult.length == 1) {
										btvos[i].setCadjustbillid(sResult[0][0]);
										btvos[i].setCadjustbillitemid(sResult[0][1]);
									} else {
									}
								}
							}
						}
					}
					vBills.addElement(bvo);
				}
				BillVO[] binsrtedVOs = new BillVO[vBills.size()];
				binsrtedVOs = (BillVO[]) vBills.toArray(binsrtedVOs);
				
				timer.addExecutePhase("存活核算对外系统单据进行 业务检查"/*notranslate*/);
				
				// 仓储库存组织转换到成本库存组织，并检查出入库库存组织是否是同一成本库存组织
				binsrtedVOs = this.changeAndCheckCalbodys(binsrtedVOs, sSourceModule,
						sCorpID);
				
				timer.addExecutePhase("存活核算对外系统单据所有库存组织转换库存组织"/*notranslate*/);
				// add by hanwei 2004-03-26 使用临时表方案
				
				ArrayList alInvIDs = new ArrayList();
				ArrayList alSourceBillItemIDs = new ArrayList();
				ArrayList alAdjustBillItemID = new ArrayList();
				for (int j = 0; j < binsrtedVOs.length; j++) {
					BillVO bvo = binsrtedVOs[j];
					// 构建临时表记录
					for (int ii = 0; ii < bvo.getChildrenVO().length; ii++) {//对表体进行遍历，bvo.getChildrenVO().length长度等于表体的行数
						String sInvID = ((BillItemVO) bvo.getChildrenVO()[ii])
								.getCinventoryid();
						if (alInvIDs.contains(sInvID) == false) {
							alInvIDs.add(sInvID);
						}
						String sSourceBillItemID = ((BillItemVO) bvo.getChildrenVO()[ii])
								.getCsourcebillitemid();
						if (alSourceBillItemIDs.contains(sSourceBillItemID) == false) {
							alSourceBillItemIDs.add(sSourceBillItemID);
						}
						String sAdjustBillItemID = ((BillItemVO) bvo.getChildrenVO()[ii])
								.getCadjustbillitemid();
						if ((sAdjustBillItemID != null)
								&& (alAdjustBillItemID.contains(sAdjustBillItemID) == false)) {//红冲审核不进此判断
							alAdjustBillItemID.add(sAdjustBillItemID);
						}
					}
				}
				// 没有要处理的单据
				if ((alInvIDs == null) || (alInvIDs.size() == 0)) {//红冲审核不进此判断
          return null;
        }
				TempTableDMO dmo = new TempTableDMO();
				String pk_corp = binsrtedVOs[0].getPk_corp();
				String billtype = binsrtedVOs[0].getBillTypeCode();

				// ***************************************************
				if (sSourceModule.equals(ConstVO.m_sModuleIC)//此if判断在红票审核时不执行
						&& (alSourceBillItemIDs.size() != 0)) {
					String sSubSql = dmo.insertTempTable(alSourceBillItemIDs,
							nc.vo.ia.pub.TempTableVO.TEMPTABLE_SOURCE,
							nc.vo.ia.pub.TempTableVO.TEMPPKFIELD_SOURCE);
					// 库存传入的单据，增加
					StringBuffer sql = new StringBuffer(" select ");
					sql.append(" count(csourcebillitemid) ");
					sql.append(" from ");
					sql.append(" ia_bill_b ");
					sql.append(" where ");
					// **********************************
					// add by hanwei 2004-03-26 使用临时表方案
					sql.append(" csourcebillitemid in " + sSubSql);
					//不同单据类型的单据可以来源于同一分录行// zlq 20060608
					sql.append(" and cbilltypecode = '" + billtype + "'");
																																
					sql.append(" and pk_corp = '" + pk_corp + "'");
					sql.append(" and ");
					sql.append(" dr = 0 ");
					// **********************************
					String[][] sResult = cbo.queryDataNoTranslate(sql.toString());
					int iCount = new Integer(sResult[0][0]).intValue();
					if (iCount != 0) {
						BusinessException e = new BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
										"UPP20143010-000019")/* @res "传入存货核算的单据重复，请检查" */);
						throw e;
					}
				}
				if (sSourceModule.equals(ConstVO.m_sModulePO)//此if判断在红票审核时不执行
						&& (alAdjustBillItemID.size() != 0)) {
					// **********************************
					// add by hanwei 2004-03-26 使用临时表方案
					String sAdjustSql = "";
					if (alAdjustBillItemID.size() != 0) {
						sAdjustSql = dmo.insertTempTable(alAdjustBillItemID,
								nc.vo.ia.pub.TempTableVO.TEMPTABLE_ADJUST, "id");
					}
					// **********************************
					/* 采购传入的单据，增加入库调整单(根据采购传入的原暂估的库存单据的单据头id，
					 * 行id得到存货的采购入库单据头id，行id)*/
					StringBuffer sql = new StringBuffer(" select ");
					sql.append(" cbillid,cbill_bid,csourcebillitemid ");
					sql.append(" from ");
					sql.append(" ia_bill_b ");
					sql.append(" where ");
					sql.append(" dr = 0 ");
					sql.append(" and ");
					// **********************************
					// add by hanwei 2004-03-26 使用临时表方案
					sql.append(" csourcebillitemid in " + sAdjustSql);
					// **********************************
					String[][] sResult = cbo.queryDataNoTranslate(sql.toString());
					if ((sResult != null) && (sResult.length != 0)) {
						// String sCbillid = "";
						// String sCbillbid = "";
						// String sCsourcebillitemid = "";
						HashMap hm = new HashMap();
						// 把查出来的结果保存在临时的hash表中
						for (int ii = 0; ii < sResult.length; ii++) {
							hm.put(sResult[ii][2], new String[] { sResult[ii][0],
									sResult[ii][1] });
						}
						// 给对应的被调整单据头id和行id赋值
						for (int i = 0; i < bvos.length; i++) {
							for (int j = 0; j < bvos[i].getChildrenVO().length; j++) {
								BillItemVO btvo = (BillItemVO) bvos[i].getChildrenVO()[j];
								if (btvo.getCadjustbillitemid() != null) {
									Object objID = hm.get(btvo.getCadjustbillitemid());
									if (objID != null) {
										btvo.setCadjustbillid(((String[]) objID)[0]);
										btvo.setCadjustbillitemid(((String[]) objID)[1]);
									}
								}
							}
						}
					}
				}
				timer.addExecutePhase("存活核算对外系统单据进行 库存和采购的业务检查"/*notranslate*/);
				// 是否关账
				BillTool.ensureAccountIsOpen(sCorpID, curbhvo.getDbilldate());
				
				timer.addExecutePhase("存活核算对外系统单据进行 关账检查"/*notranslate*/);
				// 判断是否已经月末结帐
				int iIndex = sPeriod.indexOf("-");
				if (iIndex != -1) {
					String sAccountYear = sPeriod.substring(0, iIndex);
					String sAccountMonth = sPeriod.substring(iIndex + 1);
					nc.vo.ia.ia402.AccountVO avo = new nc.vo.ia.ia402.AccountVO();
					avo.setPk_corp(sCorpID);
					avo.setCaccountyear(sAccountYear);
					avo.setCaccountmonth(sAccountMonth);
					nc.vo.ia.ia402.AccountVO[] avoResult = abo.queryByVO(avo,
							new Boolean(true));
					if ((avoResult != null) && (avoResult.length != 0)) {//红冲审核不进此判断
						// 已月末结帐
						BusinessException e = null;
						if (sAccountMonth.equals("00") == false) {
							// "期间已月末结账，不能再增加单据，请重新登录或修改单据日期");
							String[] value = new String[] { sAccountYear, sAccountMonth };
							e = new BusinessException(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("20143010", "UPP20143010-000180",
											null, value));
							throw e;
						} else {
							e = new BusinessException(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("20143010", "UPP20143010-000003")
									/* @res"已经期初记帐，不能再增加启用日期前的单据，请重新登录或修改单据日期"*/);
							throw e;
						}
					}
				}
				timer.addExecutePhase("存活核算进行 是否月末结帐检查"/*notranslate*/);
				// 插入单据
				binsrtedVOs = this.saveOutterArray(cl, binsrtedVOs, iPeci, sSourceModule,sSimulate, svo);
				timer.addExecutePhase("存活核算对外系统单据进行 保存操作"/*notranslate*/);
				BillSaveFacade process = new BillSaveFacade();
				binsrtedVOs = process.process(binsrtedVOs);
				for (int h = 0; h < binsrtedVOs.length; h++) {
					vAuditBills.addElement(binsrtedVOs[h]);
				}
				// 成本计算
				this.auditOneBill(binsrtedVOs);
				timer.addExecutePhase("存活核算对外系统单据进行 保存后成本计算"/*notranslate*/);
				ArrayList alIaHeadBodyId = null;
				for (int i = 0; i < binsrtedVOs.length; i++) {
					BillVO bvo = binsrtedVOs[i];
					String sBillTypeCode = ((BillHeaderVO) bvo.getParentVO())
							.getCbilltypecode();
					if (sBillTypeCode.equals(ConstVO.m_sBillCGRKD)
							&& sSourceModule.equals(ConstVO.m_sModuleIC)
							&& (bPOStart.booleanValue() == false)) {//此if判断在红票审核时不执行
						// 是库存传入的采购入库单且采购没有启用
						Object oInstance = NCLocator.getInstance().lookup(
								IICToIA.class.getName());
						((IICToIA) oInstance).setSettledFlag(bvo);
					}
					// 设置返回值
					String sIaHeadId = bvo.getParentVO().getPrimaryKey();
					BillItemVO[] items = (BillItemVO[]) bvo.getChildrenVO();
					int len = items == null ? 0 : items.length;
					for (int j = 0; j < len; j++) {
						alIaHeadBodyId = new ArrayList();
						alIaHeadBodyId.add(sIaHeadId);
						alIaHeadBodyId.add(items[j].getCbill_bid());
						hmSourceBodyId2IaHeadBodyId.put(items[j].getCsourcebillitemid(),
								alIaHeadBodyId);
					}
				}
			} catch (Exception ex) {
				ExceptionUtils.marsh(ex);
			} finally {
				try {
					if (bAccountLocked) {
						this.releaseAccountLock((BillVO) bvos[0]);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			timer.showAllExecutePhase("存货核算savebillfromoutterarray：");
		}
    
    return hmSourceBodyId2IaHeadBodyId;
  }
  
  
  public HashMap saveBillFromOutterArray_bill(AggregatedValueObject[] bvos,
	      String sSourceModule, String sSourceBillType) throws BusinessException{
	  Log.debug("发票审核时执行此方法saveBillFromOutterArray_bill"); 
	  return this.saveBillFromOutterArray(bvos,sSourceModule,sSourceBillType);
  }
  

  /**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 */
  private void updateBatchNumber(String sBillItemID, String sOper,
      double dNumber) throws Exception {
    String sFunc = " isnull( ";

    StringBuffer sSQL = new StringBuffer(" update ");
    sSQL = sSQL.append(" ia_bill_b ");
    sSQL = sSQL.append(" set ");
    sSQL = sSQL.append(" nsettledsendnum = " + sFunc + " nsettledsendnum,0) "
        + sOper + dNumber);
    sSQL = sSQL.append(" where ");
    sSQL = sSQL.append(" cbill_bid = '" + sBillItemID + "'");

    CommonDataImpl cbo = new CommonDataImpl();
    cbo.execData(sSQL.toString());
  }

  /**
	 * 函数功能:外部系统推入单据时自动成本计算
	 * 
	 * 参数: BillVO[] bvos ----- 要成本计算的单据
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 */
  public void auditOneBill(BillVO[] bvos) throws BusinessException {
    try {
      // 初始化BO
      AuditImpl abo = new AuditImpl();

      // 初始化BO
      CommonDataImpl cbo = new CommonDataImpl();

      Vector vAuditTemp = new Vector(1, 1);

      String sCorpID = "";
      String sBillDate = "";
      String sUser = "";

      if ((bvos != null) && (bvos.length != 0)) {
        sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      }
      // 是否保存后立刻审核(成本计算)
      // ConstVO.m_iPara_ZDSHJZ = 1 --- 制单是否自动审核记账
      String sAudit = cbo.getParaValue(sCorpID, new Integer(
          ConstVO.m_iPara_ZDSHJZ));

      // 调用成本计算的方法审核单据
      boolean bSuccessed = true;

      for (int j = 0; j < bvos.length; j++) {
        BillVO bvo = bvos[j];

        // 补充数据
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        // 获得单位编码
        sCorpID = bhvo.getPk_corp();

        // 获得单据类型
        String sBillType = bhvo.getCbilltypecode();

        // 获得单据日期
        sBillDate = bhvo.getDbilldate().toString();

        // 获得操作员
        sUser = bhvo.getCoperatorid();

        // 源头单据类型
        // String sFirstBillType = ((BillItemVO)
				// bvo.getChildrenVO()[0]).getCfirstbilltypecode();

        // 来源单据类型
        String sSourceBillType = ((BillItemVO) bvo.getChildrenVO()[0])
            .getCsourcebilltypecode();

        // 20050616 xiugai zlq
        // if (sBillType.equals(ConstVO.m_sBillQTRKD)
        // && sFirstBillType != null &&
        // (sFirstBillType.equals(ConstVO.m_sBillKCXTZHD) ||
        // sFirstBillType.equals(ConstVO.m_sBillKCCXD) ||
        // sFirstBillType.equals(ConstVO.m_sBillKCZKD)))
        //			{
        //				//由库存管理形态转换和库存系统拆卸和库存系统转库生成的其他入库单，不自动成本计算
        //				Log.info("由库存管理形态转换或拆卸或转库生成的其他入库单，不能自动成本计算");
        //				continue;
        //			}

        if ((sAudit != null) && sAudit.equalsIgnoreCase("Y")) {
          //以前版本中只标记是否自动进行记帐，现在版本中区分下面三种分类
          //0：产成品，１：半成品 2：原材料
          sAudit = "012";
        } else if ((sAudit != null) && sAudit.equalsIgnoreCase("N")) {
          //以前版本
          return;
        }

        //将计价方式是个别指定的过滤掉
        Integer iRDFlag = bhvo.getFdispatchflag();
        String sBizTypeID = bhvo.getCbiztypeid();

        //获得业务类型
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        //成本是否启用 m_sModuleCodeCA = "3006" --- 成本管理的标志
        UFBoolean bStart = cbo.isModuleStarted(sCorpID,
            ConstVO.m_sModuleCodeCA, bhvo.getCaccountyear() + "-"
                + bhvo.getCaccountmonth());

        //满足自动成本计算的分录
        Vector vTemp = new Vector(1, 1);

        for (int i = 0; i < bvo.getChildrenVO().length; i++) {
          BillItemVO bAudittVO = (BillItemVO) bvo.getChildrenVO()[i];
          Integer iPricemode = bAudittVO.getFpricemodeflag();//计价方式
          UFDouble dNumber = bAudittVO.getNnumber();
          String sInvkind = bAudittVO.getCinvkind();//存货类型：产成品、半成品、原材料

          if ((sAudit == null)
              || ((sInvkind != null) && (sAudit.indexOf(sInvkind) == -1))
              || (sInvkind == null) || (sInvkind.trim().length() == 0)) {
            //保存后成本计算的参数中没有此存货类型
            continue;
          }

          String s[] = new String[1];
          s[0] = bAudittVO.getCinventoryid();
          String[][] sResults = cbo
              .getPrices(sCorpID, bhvo.getCrdcenterid(), s);

          if ((sResults.length != 0) && (sResults[0].length == 5)) {
            if (sResults[0][4].equals("Y")
                && bStart.booleanValue()
                && (sBillType.equals(ConstVO.m_sBillCCPRKD) || sBillType
                    .equals(ConstVO.m_sBillCLCKD))) {
              //是成本对象且成本启用，是产成品入库单或材料出库单
              continue;
            }
          }

          if ((iPricemode != null) && (iPricemode.intValue() == ConstVO.GBJJ)
              && (dNumber != null)) {
            //是个别计价未成本计算,且有数量
            double ddnum = dNumber.doubleValue();

            if ((iRDFlag != null) && (iRDFlag.intValue() == 1) && (ddnum > 0)) {
              if (sSourceBillType == null) {
                //是蓝字出库单，且来源不是发票，不能成本计算
              } else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
                //是蓝字出库单，且来源不是发票，不能成本计算
              } else if ((sFQSK != null) && (sBizTypeID != null)
                  && (sFQSK.indexOf(sBizTypeID) == -1)) {
                //是蓝字出库单，且来源是发票，但不是分期收款，不能成本计算
              } else if ((sWTDX != null) && (sBizTypeID != null)
                  && (sWTDX.indexOf(sBizTypeID) == -1)) {
                //是蓝字出库单，且来源是发票，但不是委托代销，不能成本计算
              } else {
                vTemp.addElement(bAudittVO);
              }
            } else if ((iRDFlag != null) && (iRDFlag.intValue() == 0) && (ddnum < 0)) {
              //是红字入库单，不能成本计算
            } else {
              //其它不可以指定
              vTemp.addElement(bAudittVO);
            }
          } else {
            //其它不可以指定
            vTemp.addElement(bAudittVO);
          }
        }

        if (vTemp.size() != 0) {
          BillItemVO[] bbtvos = new BillItemVO[vTemp.size()];
          vTemp.copyInto(bbtvos);

          BillVO bComputeVO = new BillVO();
          bComputeVO.setParentVO(bhvo);
          bComputeVO.setChildrenVO(bbtvos);

          IaInoutledgerVO[] voAudits = bComputeVO.changeToView();

          if (voAudits.length != 0) {
            for (int h = 0; h < voAudits.length; h++) {
              vAuditTemp.add(voAudits[h]);
            }
          }
        }
      }

      if (vAuditTemp.size() != 0) {
        IaInoutledgerVO[] voAudits = new IaInoutledgerVO[vAuditTemp.size()];
        vAuditTemp.copyInto(voAudits);

        //获得会计期间
        String sPeriod = cbo.getPeriod(sCorpID, sBillDate);

        String[] systemInfo = new String[5];
        systemInfo[0] = sCorpID;
        systemInfo[1] = sBillDate;
        systemInfo[2] = sUser;
        systemInfo[3] = sPeriod.substring(0, 4);
        systemInfo[4] = sPeriod.substring(5, 7);

        bSuccessed = abo.billaccountbyVO(systemInfo, voAudits, new Integer(1));

        if (bSuccessed == false) {
          BusinessException be = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000022")/* @res "成本计算失败" */);
          throw be;
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }

  }

  /**
   * 函数功能:判断传入的单据中是否有重复的
   * 
   * 参数: BillVO[] bvos ----- 单据VO数组 sSourceModule ----- 来源模块 String
   * sSourceBillType ----- 来源单据类型
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  private void checkICDupBills(BillVO[] bvos, String sSourceModule,
      String sSourceBillType) throws Exception {
    Map mp = new HashMap();

    if (bvos == null) {
      return;
    }

    if (sSourceModule.equals(ConstVO.m_sModuleIC) == false) {
      return;
    }

    //检查库存传入的单据是否有重复的    
    for (int i = 0; i < bvos.length; i++) {
    	String billtype = bvos[i].getBillTypeCode();
    	ArrayList sourceids = new ArrayList();
    	if(!mp.containsKey(billtype)){
    		mp.put(billtype,sourceids);
    	}else{
    		sourceids = (ArrayList)mp.get(billtype);
    	}
      for (int j = 0; j < bvos[i].getChildrenVO().length; j++) {
        BillItemVO btvo = (BillItemVO) bvos[i].getChildrenVO()[j];
        if(btvo == null ){
        	continue;
        }
        if ((btvo.getCsourcebillitemid() != null)
            && sourceids.contains(btvo.getCsourcebillitemid())) {
          throw new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000019")/* @res "传入存货核算的单据重复，请检查" */);
        }

        sourceids.add(btvo.getCsourcebillitemid());
      }
    }
  }

  /**
     * 函数功能:处理由发票生成的销售成本结转单
     */
    private void doWithSaleBillsFromFaPiao(String sSourceModuleName, BillItemVO btvo,
        int iStatus, CommonDataImpl outercbo) throws Exception {
      CommonDataImpl cbo = null;
  
      if (outercbo == null) {
        cbo = new CommonDataImpl();
      }
  
      //数量
      UFDouble dNumber = btvo.getNnumber();
  
      String sFunc = " isnull( ";
  
      if (dNumber == null) {
        //没有数量或数量>0，不处理
        return;
      }
  
      String sCompCode = "cbill_bid";
      if ((sSourceModuleName != null)
          && sSourceModuleName.trim().equals(ConstVO.m_sModuleSO)) {
        sCompCode = "csourcebillitemid";
      }
  
      //有数量
      String sSaleAdviceItemID = btvo.getCcsaleadviceitemid();
      if ((sSaleAdviceItemID != null) && (sSaleAdviceItemID.trim().length() != 0)) {
        if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
          //如果是增加或删除，直接修改来源出库单单据的数量
          double dNum = dNumber.doubleValue();
  
          if (iStatus == ConstVO.ADD_STATUS) {
            double dTotalNum = 0;
            double dSettledsendNum = 0;
            double dSupplyNum = 0;
  
            //如果是增加，判断数量是否够出库的
            StringBuffer sSQL = new StringBuffer(" select nnumber, nsettledsendnum,  ");
            sSQL = sSQL.append(" nnumber-" + sFunc + "nsettledretractnum,0) -"
                + sFunc + "nsettledsendnum,0)");
            sSQL = sSQL.append(" from ");
            sSQL = sSQL.append(" ia_bill_b ");
            sSQL = sSQL.append(" where ");
            sSQL = sSQL.append(" dr = 0 ");
            sSQL = sSQL.append(" and ");
            sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "'");
  
            String sResult[][] = null;
  
            if (cbo != null) {
              sResult = cbo.queryData(sSQL.toString());
            } else {
              sResult = outercbo.queryData(sSQL.toString());
            }
  
            if (sResult.length != 0) {
              String sTemp[] = sResult[0];
              if ((sTemp.length != 0) && (sTemp[0].trim().length() != 0)) {
                dTotalNum = new UFDouble(sTemp[0]).doubleValue();
              }
              if ((sTemp.length != 0) && (sTemp[1].trim().length() != 0)) {
                dSettledsendNum = new UFDouble(sTemp[1]).doubleValue();
              }
              if ((sTemp.length != 0) && (sTemp[2].trim().length() != 0)) {
                dSupplyNum = new UFDouble(sTemp[2]).doubleValue();
              }
            } else {
              //此单据来源是销售发票，且是分期收款或委托代销业务，
              //但没有找到对应的来源于出库单的单据，请调整
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                      "UPP20143010-000018"));
            }
  
            // 发票数量和总数量同号，则发票是正向发票；异号，则发票时反向回冲发票
            if (((dTotalNum <= 0) && (dNum <= 0)) 
                || ((dTotalNum >= 0) && (dNum >= 0))) {
              // 同号，用剩余可开票数量与发票数量比较
              if (Math.abs(dSupplyNum) < Math.abs(dNum)) {
                // @res第{0}行的存货当前允许的最大结算数量为{1},请调整")
                String[] value = new String[] { btvo.getIrownumber().toString(),
                    String.valueOf(dSupplyNum) };
                throw new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().
                    getStrByID("20143010", "UPP20143010-000197", null, value));
              }
            } else {
              // 异号，用累计已开票数量与发票数量比较
              if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {
            	  Log.debug("doWithSaleBillsFromFaPiao执行了");
                // @res第{0}行的存货当前允许的最大回冲数量为{1},请调整")
                String[] value = new String[] { btvo.getIrownumber().toString(),
                    String.valueOf(dSettledsendNum) };
                throw new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().
                    getStrByID("20143010", "UPP20143010-000196", null, value));
              }
            }
          }
  
          String sOper = " + ";
  
          if (iStatus == ConstVO.DELETE_STATUS) {
            //删除红冲单数据，减去被调整单据的累计回冲数量
            sOper = " - ";
          }
  
          //修改对应单据的累计回冲数量
          StringBuffer sSQL = new StringBuffer(" update ");
          sSQL = sSQL.append(" ia_bill_b ");
          sSQL = sSQL.append(" set ");
          sSQL = sSQL.append(" nsettledsendnum = " + sFunc
              + " nsettledsendnum,0) " + sOper + dNum);
          sSQL = sSQL.append(" where ");
          sSQL = sSQL.append(" dr = 0 ");
          sSQL = sSQL.append(" and ");
          sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "'");
  
          if (cbo != null) {
            cbo.execData(sSQL.toString());
          }
          else {
            outercbo.execData(sSQL.toString());
          }
        }
      }
    }

  /**
   * 函数功能:处理由销售系统发票生成的销售成本结转单，业务类型为分期收款，委托代销
   */
  private void doWithSOFaPiao( BillItemVO btvo, int iStatus,CommonDataImpl outercbo) 
  throws Exception {
    CommonDataImpl cbo = null;

    if (outercbo == null) {
      cbo = new CommonDataImpl();
    }

    // 数量
    UFDouble dNumber = btvo.getNnumber();//得到成本结转单的开票数量，-5835

    String sFunc = " isnull( ";

    if (dNumber == null) {
      // 没有数量，不处理
      return;
    }

    String sCompCode = "csourcebillitemid";
    StringBuffer sSQL = new StringBuffer();
    // 有数量
    String sSaleAdviceItemID = btvo.getCcsaleadviceitemid();
    if((sSaleAdviceItemID != null) && (sSaleAdviceItemID.trim().length() != 0)) {
      if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
        // 如果是增加或删除，直接修改来源出库单单据的数量
        double dNum = dNumber.doubleValue();//-5835
        double dTotalNum = 0;
        double dSettledsendNum = 0;
        double dSupplyNum = 0;

        // 如果是增加，判断数量是否够出库的

        sSQL = sSQL.append(" select nnumber, nsettledsendnum, ");
        sSQL = sSQL.append(" nnumber-" + sFunc + "nsettledsendnum,0)");
        sSQL = sSQL.append(" from ");
        sSQL = sSQL.append(" ia_bill_b ");
        sSQL = sSQL.append(" where ");
        sSQL = sSQL.append(" dr = 0 ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "' and cbilltypecode = 'I5'");//edit by shikun 2014-05-29 I5=销售成本结转单
        String sResult[][] = null;
        if (cbo != null) {
          sResult = cbo.queryData(sSQL.toString());
        } else {
          sResult = outercbo.queryData(sSQL.toString());//[[5835, 5835, 0]]
        }
        if (sResult.length != 0) {
          String sTemp[] = sResult[0];
          if ((sTemp.length != 0) && (sTemp[0].trim().length() != 0)) {
            dTotalNum = new UFDouble(sTemp[0]).doubleValue();//5835
          }
          if ((sTemp.length != 0) && (sTemp[1].trim().length() != 0)) {
            dSettledsendNum = new UFDouble(sTemp[1]).doubleValue();//5835
          }
          if ((sTemp.length != 0) && (sTemp[2].trim().length() != 0)) {
            dSupplyNum = new UFDouble(sTemp[2]).doubleValue();//0
          }
        } else {//红冲发票审核此处不执行
          /*
           * @res此单据来源是销售发票，且是分期收款或委托代销业务， 但没有找到对应的来源于出库单的单据，请调整
           */
          throw new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
              "UPP20143010-000018"));
        }
        String sOper ="";
        if (iStatus == ConstVO.ADD_STATUS) {
          // 发票数量和总数量同号，则发票是正向发票；异号，则发票是反向回冲发票
          if (((dTotalNum <= 0) && (dNum <= 0)) 
              || ((dTotalNum >= 0) && (dNum >= 0))) {//此if判断在红票审核时不执行
            // 同号，用剩余可开票数量与发票数量比较
            if (Math.abs(dSupplyNum) < Math.abs(dNum)) {
              // @res第{0}行的存货当前允许的最大结算数量为{1},请调整")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSupplyNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000197", null, value));
              
            }
          } else {//红冲发票审核在此处判断后报错，报错出现在4431行
            // 异号，用累计已开票数量与发票数量比较
            if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {//math.abs()返回参数的绝对值，此if判断在红票审核时不执行
              // @res第{0}行的存货当前允许的ou最大回冲数量为{1},请调整")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000196", null, value));
            }
          }
          sOper = " + ";//这里会执行
        }
        else if (iStatus == ConstVO.DELETE_STATUS) {//红冲审核此处不会执行
          //发票数量和总数量同号，则发票是正向发票；异号，则发票时反向回冲发票
          if (((dTotalNum <= 0) && (dNum <= 0)) 
              || ((dTotalNum >= 0) && (dNum >= 0))) {
            //删除红冲单数据，减去被调整单据的累计回冲数量
            if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {
              // @res第{0}行的存货累计结算数量 {1} 小于当前要取消结算的数量{2},请调整")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum),String.valueOf(dNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000321", null, value));
            }
          }else{
            if(Math.abs(dTotalNum) < Math.abs(dSettledsendNum-dNum)){
              //@res 第{0}行的存货取消回冲发票结算后数量 {1} 超过原始数量 {2},请调整
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum-dNum),String.valueOf(dTotalNum)};
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000322", null, value));
            }
          }
          sOper = " - ";
        }
        // 修改对应单据的累计回冲数量
        sSQL = new StringBuffer(" update ");
        sSQL = sSQL.append(" ia_bill_b ");
        sSQL = sSQL.append(" set ");
        sSQL = sSQL.append(" nsettledsendnum = " + sFunc
            + " nsettledsendnum,0) " + sOper + dNum);
        sSQL = sSQL.append(" where ");
        sSQL = sSQL.append(" dr = 0 ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "' and cbilltypecode = 'I5'");//edit by shikun 2014-05-29 I5=销售成本结转单

        if (cbo != null) {
          cbo.execData(sSQL.toString());
        }
        else {
          outercbo.execData(sSQL.toString());
        }
      }
    }

  }

  /**
   * 函数功能:将存货核算单据VO用计划价写入实际价（为SEG提供）
   * 
   * 参数:
   * 
   * 返回值:
   * 
   * 异常:
   * 
   * 创建日期：(2002-12-10 9:01:17)
   */
  public BillVO[] fillVOByPlanedBatch(BillVO[] bvos) throws BusinessException {
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //整理表体数据
      String sPk_corp = null;
      Integer[] iPeci = null;
      for (int ii = 0; ii < bvos.length; ii++) {
        BillHeaderVO bhvo = (BillHeaderVO) bvos[ii].getParentVO();
        BillItemVO[] btvos = (BillItemVO[]) bvos[ii].getChildrenVO();

        //公司变了，要重新查询数据精度
        if ((sPk_corp == null) || !sPk_corp.equals(bhvo.getPk_corp())) {
          sPk_corp = bhvo.getPk_corp();
          iPeci = cbo.getDataPrecision(bhvo.getPk_corp());
        }

        //得到单价为空需要进一步查询计划价的存货
        //Hashtable(key:存货主键+表体VO数组下标，value:表体VO数组中的值)
        Hashtable htInvID = new Hashtable();
        for (int i = 0; i < btvos.length; i++) {
          if (btvos[i].getNprice() == null) {
            htInvID.put(btvos[i].getCinventoryid() + "," + i, btvos[i]);
          }
        }

        if (htInvID.size() <= 0) {
          continue;
        }
        Enumeration e = htInvID.keys();
        String sKey = null;
        Vector vID = new Vector(1, 1);
        while (e.hasMoreElements()) {
          sKey = (String) e.nextElement();
          if (vID.indexOf(sKey.substring(0, sKey.indexOf(","))) < 0) {
            vID.addElement(sKey.substring(0, sKey.indexOf(",")));
          }
        }
        String[] s = new String[vID.size()];
        vID.copyInto(s);
        //查询存货信息，返回值:存货、计价方式、计划价、是否批次核算
        String[][] sResults = cbo.getPrices(sPk_corp, bhvo.getCrdcenterid(), s);

        //用计划价写入实际价
        if ((sResults != null) && (sResults.length > 0)) {
          UFDouble dPlanedPrice = null;
          UFDouble dNumber = null;
          UFDouble dPlanedMny = null;
          BillItemVO btvo = null;
          for (int i = 0; i < sResults.length; i++) {
            //获得计价方式编码

            //if (iPriceCode.intValue() == ConstVO.JHJ) //不再限制是计划价的
            {
              //是计划价计价，把计划价写入实际单价,并把计划金额写入实际金额
              dPlanedPrice = new UFDouble(sResults[i][2]);

              e = htInvID.keys();
              while (e.hasMoreElements()) {
                sKey = (String) e.nextElement();
                if (sKey.indexOf(sResults[i][0]) != -1) {
                  btvo = (BillItemVO) htInvID.get(sKey);
                  btvo.setNprice(dPlanedPrice);

                  dNumber = btvo.getNnumber();
                  if (dNumber != null) {
                    dPlanedMny = dNumber.multiply(dPlanedPrice);

                    dPlanedMny = dPlanedMny.setScale(iPeci[2].intValue(),
                        UFDouble.ROUND_HALF_UP);
                    btvo.setNmoney(dPlanedMny);
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return bvos;
  }


  public void delete(ClientLink cl, BillVO bvo, String sUserID, String sFQSK,
	    String sWTDX) throws BusinessException {
  	this.delete(cl,bvo,sUserID,sFQSK,sWTDX,true);
	}

	public void delete(ClientLink cl, BillVO bvo, String sUserID, String sFQSK,
      String sWTDX,boolean bReturnBillCode) throws BusinessException {
    String[] sItemIDs = null;
    boolean bIfCanDO = false;

    try {
      //锁数据
      String sPK = bvo.getParentVO().getPrimaryKey();

      sItemIDs = new String[bvo.getChildrenVO().length + 1];
      sItemIDs[0] = sPK;

      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        sItemIDs[i + 1] = bvo.getChildrenVO()[i].getPrimaryKey();
      }

      bIfCanDO = LockUtils.lock(sItemIDs, sUserID);

      if (bIfCanDO == false) {
        BusinessException be = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000023")/*
                                                                        * @res
                                                                        * "相关单据正在进行处理，请稍后再试"
                                                                        */);
        throw be;
      }
      BillHeaderVO header = (BillHeaderVO )bvo.getParentVO();
      String cbillid = header.getCbillid();
      SqlBuilder sql = new SqlBuilder();
      sql.append(" select count(*) from ia_bill_b where dr=0 ");
      sql.append(" and cbillid",cbillid );
      sql.append(" and ");
      sql.startParentheses();
      sql.append(" iauditsequence >0  ");
      sql.append(" or brtvouchflag","Y");
      sql.endParentheses();
      
      IRowSet rowset = DataAccessUtils.query( sql.toString() );
      if( rowset.next() ){
        int count = rowset.getInt( 0 );
        String[] args = new String[1];
        args[0] = header.getVbillcode();
        if( count > 0 ){
          String message = nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000293",null,args);
          /*
           * @res "单据{0}已经生成实时凭证或者已经成本计算，不能删除"
           */
          throw new BusinessException ( message );
        }
      }
      
      
      //获得收发标志
      Integer iRDFlag = ((BillHeaderVO) bvo.getParentVO()).getFdispatchflag();
      int iRD = -1;

      if ((iRDFlag != null) && (iRDFlag.toString().length() != 0)) {
        iRD = iRDFlag.intValue();
      }

      if (iRD == 0) {
        //收
        this.deleteIn(cl, bvo, false, sUserID,bReturnBillCode);
      } else if (iRD == 1) {
        //发
        this.deleteIn(cl, bvo, true, sUserID,bReturnBillCode);

        String sBillType = ((BillHeaderVO) bvo.getParentVO())
            .getCbilltypecode();
        String sBizTypeID = ((BillHeaderVO) bvo.getParentVO()).getCbiztypeid();
        //String sCorpID = ((BillHeaderVO) bvo.getParentVO()).getPk_corp();

        //处理来源发票的销售成本结转单
        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //是销售成本结转单
          for (int j = 0; j < bvo.getChildrenVO().length; j++) {
            BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[j];
            String sSourceBillType = btvo.getCsourcebilltypecode();
            String sSourceModuleName = ((BillHeaderVO) bvo.getParentVO())
                .getCsourcemodulename();

            boolean bIsFQWT = false;

            if (sBizTypeID != null && sFQSK != null && sWTDX != null) {
              if ((sFQSK.indexOf(sBizTypeID) != -1)
                  || (sWTDX.indexOf(sBizTypeID) != -1)) {
                bIsFQWT = true;
              }
            }

            if (bIsFQWT) {
              if ((sSourceBillType != null)
                  && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
                //来源是销售发票
                this.doWithSaleBillsFromFaPiao(sSourceModuleName, btvo, ConstVO.DELETE_STATUS,
                    null);
              }
            }
          }
        }
      } else {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000024")/*
                                                                        * @res
                                                                        * "收发标志不符合规范"
                                                                        */);
        throw e;
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }

    finally {
      try {
        if (bIfCanDO) {
          LockUtils.unLock(sItemIDs, sUserID);
        }
      } catch (Exception ex) {
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("20143010", "UPP20143010-000010")/* @res "释放业务锁出错" */);
      }
    }
  }

  /**
   * 函数功能:删除单据删除跌价提取单和差异结转单和外系统单据
   * 
   * 参数: BillVO[] bvos ----- 单据VOs String sUserID ----- 操作员id boolean
   * bReturnCode ----- 是否删除单据号
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  private void deleteArrayIn(ClientLink cl, BillVO[] bvos, String sUserID,
      boolean bReturnCode) throws BusinessException {
    String[] sItemIDs = null;
    boolean bIfCanDO = false;

    try {
      Vector vItemIDs = new Vector(1, 1);

      for (int i = 0; i < bvos.length; i++) {
        vItemIDs.addElement(bvos[i].getParentVO().getPrimaryKey());

        for (int j = 0; j < bvos[i].getChildrenVO().length; j++) {
          vItemIDs.addElement(bvos[i].getChildrenVO()[j].getPrimaryKey());
        }
      }

      sItemIDs = new String[vItemIDs.size()];
      vItemIDs.copyInto(sItemIDs);

      bIfCanDO = LockUtils.lock(sItemIDs, sUserID);

      if (bIfCanDO == false) {
        BusinessException be = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000023")/*
                                                                        * @res
                                                                        * "相关单据正在进行处理，请稍后再试"
                                                                        */);
        throw be;
      }

      for (int i = 0; i < bvos.length; i++) {
        BillVO vo = bvos[i];

        String sBillTypeCode = ((BillHeaderVO) vo.getParentVO())
            .getCbilltypecode();

        if ((sBillTypeCode.equals(ConstVO.m_sBillCYLJZD) == false)
            && (sBillTypeCode.equals(ConstVO.m_sBillDJTQD) == false)
            && (sBillTypeCode.equals(ConstVO.m_sBillTSTZD) == false)) {
          //不是差异结转单或跌价提取单或退税调整单
          //是否可删除
          String sBillType = ((BillHeaderVO) vo.getParentVO())
              .getCbilltypecode();

          boolean bCheck = true; //是否检查

          if (sBillType.equals(ConstVO.m_sBillCKTZD)) {
            //期末处理生成的出库调整单可直接删除，即使已审核
            bCheck = false;

            for (int h = 0; h < vo.getChildrenVO().length; h++) {
              BillItemVO btvo = (BillItemVO) vo.getChildrenVO()[h];
              Integer iDataGetMode = btvo.getFdatagetmodelflag();

              if ((iDataGetMode == null)
                  || (iDataGetMode.intValue() != ConstVO.QMCL)) {
                //不是期末处理生成的
                bCheck = true;
                break;
              }
            }
          }

          //检查是否可删除
          if (bCheck && (this.checkDeleteData(vo) == false)) {
            return;
          }
        }
      }

      //执行数据库删除操作
      this.execDelete(cl, bvos);

      if (bReturnCode) {
        //把单据号退回，以供今后使用
        BillCodeDMO bcDmo = new BillCodeDMO();
        for (int i = 0; i < bvos.length; i++) {
          bcDmo.returnBillCodeWhenDelete(bvos[i]);
        }
      }

    }
    catch(Exception ex){
      ExceptionUtils.marsh( ex );
    }
    finally {
        if (bIfCanDO) {
          LockUtils.unLock(sItemIDs, sUserID);
        }
    }
  }

  /**
   * 函数功能:删除单据删除跌价提取单和差异结转单和外系统单据
   * 
   * 参数: BillVO[] bvos ----- 单据VOs String sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteArray(ClientLink cl, BillVO[] bvos, String sUserID)
      throws BusinessException {
    try {
      this.deleteArrayIn(cl, bvos, sUserID, true);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
  }

  /**
   * 函数功能:删除单据删除跌价提取单和差异结转单和外系统单据
   * 
   * 参数: BillVO[] bvos ----- 单据VOs String sUserID ----- 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  public void deleteArrayNotReturnCode(ClientLink cl, BillVO[] bvos,
      String sUserID) throws BusinessException {
    try {
      this.deleteArrayIn(cl, bvos, sUserID, false);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
  }

  /**
   * 函数功能:删除数据
   * 
   * 参数: BillVO vo ----- 单据VO boolean bIsOut ----- 是否出库 String sUserID -----
   * 操作员id
   * 
   * 返回值:
   * 
   * 异常:
   * 
   *  
   */
  private void deleteIn(ClientLink cl, BillVO vo, boolean bIsOut, String sUserID,boolean bReturnBillCode)
      throws Exception {
    //获得单据类型，差异结转单审核后也可删除
    String sBillTypeCode = ((BillHeaderVO) vo.getParentVO()).getCbilltypecode();

    if ((sBillTypeCode.equals(ConstVO.m_sBillCYLJZD) == false)
        && (sBillTypeCode.equals(ConstVO.m_sBillDJTQD) == false)
        && (sBillTypeCode.equals(ConstVO.m_sBillTSTZD) == false)) {
      //不是差异结转单或跌价提取单或退税调整单
      //是否可删除
      String sBillType = ((BillHeaderVO) vo.getParentVO()).getCbilltypecode();

      boolean bCheck = true; //是否检查

      if (sBillType.equals(ConstVO.m_sBillCKTZD)) {
        //期末处理生成的出库调整单可直接删除，即使已审核
        bCheck = false;

        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          BillItemVO btvo = (BillItemVO) vo.getChildrenVO()[i];
          Integer iDataGetMode = btvo.getFdatagetmodelflag();

          if ((iDataGetMode == null) || (iDataGetMode.intValue() != ConstVO.QMCL)) {
            //不是期末处理生成的
            bCheck = true;
            break;
          }
        }
      }

      //检查是否可删除
      if (bCheck && (this.checkDeleteData(vo) == false)) {
        return;
      }

      //先处理红冲单
      this.doWithRedBill(vo, ConstVO.DELETE_STATUS);

      //处理出库单批次号的方法
      if (bIsOut) {
        this.doWithOutBatch(vo, ConstVO.DELETE_STATUS);
      }
    }

    //执行数据库删除操作
    this.execDelete(cl, new BillVO[] { vo });

    //把单据号退回，以供今后使用
    if(bReturnBillCode){
    	BillCodeDMO bcDmo = new BillCodeDMO();
    	bcDmo.returnBillCodeWhenDelete(vo);
    }
  }

  /**
   * 函数功能:执行删除操作，包括把作废标志置为Y，把dr置为1
   * 
   * 参数:BillVO数组--要删除的单据数组
   * 
   * 返回值:
   * 
   * 异常:当单据数组中有null时抛出NullPointerException
   * 新建RichDMO时可能抛出javax.naming.NamingException ,nc.bs.pub.SystemException
   * dmo.model2_EditBatch可能抛出java.sql.SQLException
   * 
   * @param bvos
   *          nc.vo.ia.bill_new.BillVO[]
   * @exception java.lang.NullPointerException
   *              异常说明。
   */
  private void execDelete(ClientLink cl, BillVO[] bvos)
      throws java.lang.NullPointerException, javax.naming.NamingException,
      nc.bs.pub.SystemException, java.sql.SQLException {

    RichDMO dmo = new RichDMO();

    for (int i = 0; i < bvos.length; i++) {

      //把作废标志置为Y
      ((BillHeaderVO) bvos[i].getParentVO())
          .setBdisableflag(new UFBoolean("Y"));

      //把dr置为1,状态设为修改
      ((BillHeaderVO) bvos[i].getParentVO()).setDr(new Integer(1));//表头
      ((BillHeaderVO) bvos[i].getParentVO()).setStatus(VOStatus.UPDATED);
      bvos[i].setStatus(VOStatus.UPDATED);

      BillItemVO[] bvoitems = (BillItemVO[]) bvos[i].getChildrenVO();//表体
      for (int j = 0; j < bvoitems.length; j++) {
        bvoitems[j].setDr(new Integer(1));
        bvoitems[j].setStatus(VOStatus.UPDATED);
      }
    }
    //执行修改
    dmo.model2_EditBatch(cl, bvos, ((BillHeaderVO) bvos[0].getParentVO())
        .getVOMeta().getPkColName(), null, null, false);

  }

  /**
   * 函数功能:执行更新操作，先把voCurBill复制一份副本并将副本的作废标志置为Y,dr置为1 再对bvo执行更新操作
   * 
   * 参数:voCurBill：未修改前的vo; bvo：将要修改的vo(将把变化写到数据库中）
   * 
   * 返回值:
   * 
   * 异常:当单据数组中有null时抛出NullPointerException
   * 新建RichDMO时可能抛出javax.naming.NamingException ,nc.bs.pub.SystemException
   * dmo.model2_EditBatch可能抛出java.sql.SQLException
   * 
   * @param bvo
   *          nc.vo.ia.bill.BillVO
   * @exception java.lang.NullPointerException
   *              异常说明。
   * @throws CloneNotSupportedException
   */
  private void execUpdate(ClientLink cl, BillVO voCurBill, BillVO bvo)
      throws java.lang.NullPointerException, javax.naming.NamingException,
      nc.bs.pub.SystemException, java.sql.SQLException,
      CloneNotSupportedException {

    RichDMO rDmo = new RichDMO();

    //制作副本
    BillVO backbvo = (BillVO) voCurBill.clone();

    //把作废标志置为Y
    ((BillHeaderVO) backbvo.getParentVO()).setBdisableflag(new UFBoolean("Y"));

    //把dr置为1
    ((BillHeaderVO) backbvo.getParentVO()).setDr(new Integer(1));
    BillItemVO[] backbvoitem = (BillItemVO[]) backbvo.getChildrenVO();
    for (int i = 0; i < backbvoitem.length; i++) {
      backbvoitem[i].setDr(new Integer(1));
    }
    //将副本插入数据库
    rDmo.model2_CreateBatch(cl, new BillVO[] { backbvo },
        ((BillHeaderVO) backbvo.getParentVO()).getVOMeta().getPkColName(),
        null, null, true);

    //设置审核顺序默认值
    this.setDefaultAuditSequence(bvo);
    //把修改过的VO信息更新到数据库
    rDmo.model2_EditBatch(cl, new BillVO[] { bvo }, ((BillHeaderVO) bvo
        .getParentVO()).getVOMeta().getPkColName(), null, null, true);

  }


  /**
   * 函数功能:插入数据
   * 
   * 参数: BillVO bill ----- 单据
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  public BillVO insert(ClientLink cl, BillVO bill) throws BusinessException {
    boolean bAccountLocked = false;
    BillVO bvo = null;
    try {
      IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
      srv.checkDefDataType(bill);

      CommonDataImpl cbo = new CommonDataImpl();

      //处理月末结帐和日常录入单据之间的并发操作
      bAccountLocked = this.lockAccount(bill);

      //处理标志
      boolean bIsOut = false;
      boolean bIsPlanedPriceAdjustBill = false;
      boolean bIsAdjustBill = false;

      String sBillType = ((BillHeaderVO) bill.getParentVO()).getCbilltypecode();
      if (sBillType.equals(ConstVO.m_sBillQCRKD)
          || sBillType.equals(ConstVO.m_sBillCGRKD)
          || sBillType.equals(ConstVO.m_sBillCCPRKD)
          || sBillType.equals(ConstVO.m_sBillQTRKD)
          || sBillType.equals(ConstVO.m_sBillRKTZD)
          || sBillType.equals(ConstVO.m_sBillJHJTZD)
          || sBillType.equals(ConstVO.m_sBillWWJGSHD)
          || sBillType.equals(ConstVO.m_sBillDBRKD)) {
        bIsOut = false;
      } else if (sBillType.equals(ConstVO.m_sBillQCXSCBJZD)
          || sBillType.equals(ConstVO.m_sBillXSCBJZD)
          || sBillType.equals(ConstVO.m_sBillCLCKD)
          || sBillType.equals(ConstVO.m_sBillQTCKD)
          || sBillType.equals(ConstVO.m_sBillBFD)
          || sBillType.equals(ConstVO.m_sBillCKTZD)
          || sBillType.equals(ConstVO.m_sBillWWJGFLD)
          || sBillType.equals(ConstVO.m_sBillDBCKD)) {
        bIsOut = true;
      }

      if (sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        //是计划价调整单
        bIsPlanedPriceAdjustBill = true;
      }

      if (sBillType.equals(ConstVO.m_sBillRKTZD)
          || sBillType.equals(ConstVO.m_sBillCKTZD)
          || sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        bIsAdjustBill = true;
      }

      boolean bFQSK = false;
      if ((bill != null) && (bill.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();

        //获得业务类型
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(bhvo.getPk_corp(), sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }
      }

      bvo = this.insertBill(cl, bill, bIsOut, bIsAdjustBill,
          bIsPlanedPriceAdjustBill, bFQSK, null, null, cbo.getParaValue(
              ((BillHeaderVO) bill.getParentVO()).getPk_corp(), new Integer(
                  ConstVO.m_iPara_SFJSXNCB)));

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    } finally {
      try {
        if (bAccountLocked) {
          this.releaseAccountLock(bill);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return bvo;
  }

  /**
	 * 函数功能:向数据库中插入一批VO对象
	 * 
	 * 参数: BillVO[] bills ----- 单据
	 * 
	 * 返回值:单据数组
	 * 
	 * 异常:BusinessException
	 *  
	 */
	public BillVO[] insertArray(ClientLink cl, BillVO[] bills)
	    throws BusinessException {
    int length = bills.length;
    for( int i=0;i<length;i++){
      BillHeaderVO header = (BillHeaderVO) bills[i].getParentVO();
      UFDateTime time = new UFDateTime( System.currentTimeMillis() );
      header.setTlastmaketime( time.toString() );
      header.setTmaketime( time.toString() );
      header.setClastoperatorid( cl.getUser() );
    }
		return this.insertArray(cl,bills,true);
	}

	/**
   * 函数功能:向数据库中插入一批VO对象
   * 
   * 参数: BillVO[] bills ----- 单据
   * 
   * 返回值:单据数组
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] insertArray(ClientLink cl, BillVO[] bills, boolean bNeedNewBillCode)
      throws BusinessException {

    boolean bAccountLocked = false;
    ArrayList alBillCodes = null;
    try {

      if ((bills == null) || (bills.length == 0)) {
        return bills;
      }

      CommonDataImpl cbo = new CommonDataImpl();

      //处理月末结帐和日常录入单据之间的并发操作
      bAccountLocked = this.lockAccount(bills[0]);

      String sSimulate = cbo.getParaValue(((BillHeaderVO) bills[0]
          .getParentVO()).getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB));

      if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
        //处理模拟成本
        SimulateVO svo = new SimulateVO();
        svo.setBills(bills);

        bills = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
      }

      //向每一张单据中插入单据号
      if(bNeedNewBillCode){
      	alBillCodes = this.setBillCodes(bills);
      }

      //设置审核顺序默认值
      for (int i = 0; i < bills.length; i++) {
        this.setDefaultAuditSequence(bills[i]);
      }

      //插入数据
      RichDMO rDmo = new RichDMO();
      rDmo.model2_CreateBatch(cl, bills,
          ((BillHeaderVO) bills[0].getParentVO()).getVOMeta().getPkColName(),
          null, null, true);

    } catch (Exception ex) {
      //如果已经分配了就退回单据号
      this.returnBillCodes(alBillCodes, bills);
      ExceptionUtils.marsh( ex );
    }

    finally {
      try {
        if (bAccountLocked) {
          this.releaseAccountLock(bills[0]);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return bills;
  }

  /**
   * 函数功能:向数据库中插入一批VO对象
   * 
   * 参数: BillVO[] bills ----- 单据
   * 
   * 返回值:单据数组
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] insertArrayForAccount(ClientLink cl, BillVO[] bills)
      throws BusinessException {

    try {

      ArrayList alBillCodes = null;
      try {
        CommonDataImpl cbo = new CommonDataImpl();

        if ((bills == null) || (bills.length == 0)) {
          return bills;
        }

        String sSimulate = cbo
            .getParaValue(((BillHeaderVO) bills[0].getParentVO()).getPk_corp(),
                new Integer(ConstVO.m_iPara_SFJSXNCB));

        if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
          //处理模拟成本
          SimulateVO svo = new SimulateVO();
          svo.setBills(bills);

          bills = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
        }

        //向每一张单据中插入单据号
        alBillCodes = this.setBillCodes(bills);
        //设置审核顺序默认值
        for (int i = 0; i < bills.length; i++) {
          this.setDefaultAuditSequence(bills[i]);
        }
        //插入数据
        RichDMO rDmo = new RichDMO();
        rDmo.model2_CreateBatch(cl, bills, ((BillHeaderVO) bills[0]
            .getParentVO()).getVOMeta().getPkColName(), null, null, true);

      } catch (Exception ex) {
        //如果已经分配了就退回单据号
        this.returnBillCodes(alBillCodes, bills);
        ExceptionUtils.marsh( ex );
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return bills;
  }

  /**
   * 函数功能:向数据库中插入一批VO对象
   * 
   * 参数: BillVO[] bills ----- 单据
   * 
   * 返回值:单据数组
   * 
   * 异常:BusinessException
   *  
   */
  public BillVO[] insertArrayForBeginBills(ClientLink cl, BillVO[] bills)
      throws BusinessException {

    boolean bAccountLocked = false;

    try {
      //用于在insertBill插入单据号,在调用完delete后退回单据号
      BillCodeDMO bcDmo = new BillCodeDMO();
      if ((bills == null) || (bills.length == 0)) {
        return bills;
      }

      //处理月末结帐和日常录入单据之间的并发操作
      bAccountLocked = this.lockAccount(bills[0]);

      CommonDataImpl cbo = new CommonDataImpl();

      if (!(bills.length > 0)) {
        return bills;
      }

      String sCorpID = ((BillHeaderVO) bills[0].getParentVO()).getPk_corp();

      //删除以前导入的库存、销售期初单据
      BillVO conVO = new BillVO();
      BillHeaderVO conbhvo = new BillHeaderVO();
      conbhvo.setPk_corp(sCorpID);
      conbhvo.setCSQLClause("cbilltypecode in ('" + ConstVO.m_sBillQCRKD
          + "','" + ConstVO.m_sBillQCXSCBJZD + "') and csourcemodulename in ('"
          + ConstVO.m_sModuleIC + "','" + ConstVO.m_sModuleSO + "')");
      conVO.setParentVO(conbhvo);

      BillVO[] bvos = this.queryWithOtherTableHaveInv(null, null, null, conVO, null);
      if (bvos != null) {
        for (int i = 0; i < bvos.length; i++) {

          this.checkDeleteData(bvos[i]);

          //执行数据库删除操作
          this.execDelete(cl, new BillVO[] { bvos[i] });

          //20050406为保证事务，将回退单据号放在后面
          //				//把单据号退回，以供今后使用
          //				bcDmo.returnBillCode(bvos[i]);

        }
      }

     
      for (int i = 0; i < bills.length; i++) {
        BillVO bvo = bills[i];
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        ///////////////////////////////////
        //计算数量单价金额的精度
        IAMessage msg = new IAMessage();
        msg.put(bhvo);
        IAContext context = IAContext.create(bhvo.getPk_corp(), bhvo.getDbilldate().toString(), bhvo.getCoperatorid());
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
        for (int j = 0; j  < btvos.length; j++) {
          BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[j];
          msg.put(btvo);
          //检查数据精度 参数为 false 表示暂不检查精度
          btvo.setNnumber(DataCheckUtil.getInstance().checkNumber(context, msg, btvo.getNnumber(), false));
          btvo.setNprice(DataCheckUtil.getInstance().checkPrice(context, msg, btvo.getNprice(), false));
          btvo.setNmoney(DataCheckUtil.getInstance().checkMoney(context, msg, btvo.getNmoney(), false));
          btvo.setNplanedmny(DataCheckUtil.getInstance().checkMoney(context, msg, btvo.getNplanedmny(), false));
          btvo.setNchangerate( DataCheckUtil.getInstance().checkChangeRate(context, 
             msg, btvo.getNchangerate(), false));
          //补全单价和金额
          UFDouble[] priceAndMoney = DataCheckUtil.getInstance().fillPriceAndMnoney(
              btvo.getNnumber(), btvo.getNprice(), btvo.getNmoney(), context,bhvo.getCbilltypecode(), msg);
          btvo.setNprice(priceAndMoney[0]);
          btvo.setNmoney(priceAndMoney[1]);
          //检查数量单价金额之间的换算关系
          btvo.setNprice(DataCheckUtil.getInstance().checkNumPriceMoneyRelation(
              btvo.getNnumber(), btvo.getNprice(), btvo.getNmoney(), context, msg,false));
        }
        //////////////////////////////////
        
        boolean bFQSK = false;
        if ((bvo != null) && (bvo.getParentVO() != null)) {
          //获得业务类型
          String[] sBizs = new String[2];
          sBizs[0] = ConstVO.m_sBizFQSK;
          sBizs[1] = ConstVO.m_sBizWTDX;

          java.util.Hashtable ht = cbo.getBizTypeIDs(bhvo.getPk_corp(), sBizs);
          String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
          String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

          String sBizTypeID = bhvo.getCbiztypeid();

          if (sBizTypeID != null) {
            if ((sFQSK.indexOf(sBizTypeID) != -1)
                || (sWTDX.indexOf(sBizTypeID) != -1)) {
              bFQSK = true;
            }
          }
        }

        BillVO bReturnvo = this.insertBill(cl, bvo, false, false, false, bFQSK,
            null, null, cbo.getParaValue(((BillHeaderVO) bvo.getParentVO())
                .getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB)));
        bills[i] = bReturnvo;
      }

      //20050406为保证事务，将回退单据号放在后面
      int len = (bvos == null ? 0 : bvos.length);
      for (int i = 0; i < len; i++) {
        //把单据号退回，以供今后使用
        bcDmo.returnBillCodeWhenDelete(bvos[i]);
      }

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    } finally {
      try {
        if (bAccountLocked) {
          this.releaseAccountLock(bills[0]);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return bills;
  }

  /**
   * 函数功能:插入数据
   * 
   * 参数: BillVO bill ----- 单据 boolean bIsOut ----- 是否是出库单 boolean bIsAdjustBill
   * ----- 是否是调整单（其他单据要检查批次） boolean bIsPlanedPriceAdjustBill ----- 是否是计划价调整单
   * bIsFQWT ----- 是否是分期收款或委托代销 String sExistString ----- 子查询语句（用于提高效率） String
   * sSimulate ----- 是否计算虚拟成本
   * 
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  private BillVO insertBill(ClientLink cl, BillVO bill, boolean bIsOut,
      boolean bIsAdjustBill, boolean bIsPlanedPriceAdjustBill, boolean bIsFQWT,
      String sExistString, String sProduceString, String sSimulate)
      throws Exception {

    String sBillCode = null;
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //获得业务日期
      String sDate = ((BillHeaderVO) bill.getParentVO()).getDbilldate()
          .toString();

      BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();


      String sBillType = bhvo.getCbilltypecode();

      //合法性检查
      this.checkData(null, bill, bIsOut, bIsAdjustBill, bIsPlanedPriceAdjustBill,
          ConstVO.ADD_STATUS, sDate);

      //long t1 =0, t2=0;
      if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
        //处理模拟成本
        SimulateVO svo = new SimulateVO();
        svo.setBills(new BillVO[] { bill });
        //t1 = System.currentTimeMillis();

        bill = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS)[0];

        //t2 = System.currentTimeMillis();
        //Log.info("---------------------time to do simulateCost:
        // "+(t2-t1));
      }

      //t2 = System.currentTimeMillis();
      //向单据vo中插入单据号
      sBillCode = this.setBillCode(bill);
      //设置审核顺序默认值
      this.setDefaultAuditSequence(bill);

      //插入数据
      RichDMO rDmo = new RichDMO();
      rDmo.model2_CreateBatch(cl, new BillVO[] { bill }, ((BillHeaderVO) bill
          .getParentVO()).getVOMeta().getPkColName(), null, null, true);

      //long t3 = System.currentTimeMillis();
      //Log.info("---------------------time to do insert2DataBase:
      // "+(t3-t2));

      //处理红冲单
      this.doWithRedBill(bill, ConstVO.ADD_STATUS);

      //UFBoolean bStart = cbo.isModuleStarted(sCorpID, ConstVO.m_sModuleCodeFA,
      //    ((BillHeaderVO) bill.getParentVO()).getCaccountyear() + "-"
      //        + ((BillHeaderVO) bill.getParentVO()).getCaccountmonth());

      //处理与固定资产接口//20050527_关闭存货与固定资产接口
      //		if (bIsAdjustBill == false && bStart.booleanValue())
      //		{
      //			doWithFa(bill, null, bIsOut, ConstVO.ADD_STATUS);
      //		}

      String sSourceModuleName = ((BillHeaderVO) bill.getParentVO())
          .getCsourcemodulename();

      if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        //是销售成本结转单
        for (int i = 0; i < bill.getChildrenVO().length; i++) {
          BillItemVO btvo = (BillItemVO) bill.getChildrenVO()[i];
          String sSourceBillType = btvo.getCsourcebilltypecode();

          if (bIsFQWT) {
            //分期收款或委托代销
            if ((sSourceBillType != null)
                && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
              //来源是销售发票
              this.doWithSaleBillsFromFaPiao(sSourceModuleName, btvo, ConstVO.ADD_STATUS, cbo);
            }
          }
        }
      }
    } catch (Exception e) {
      //如果已经给单据分配了单据号则退回它
      ArrayList alBillCodes = new ArrayList();
      alBillCodes.add(sBillCode);
      this.returnBillCode(alBillCodes, bill);

      throw e;
    }
    return bill;
  }

  /**
   * 函数功能:插入数据
   * 
   * 参数: BillVO bill ----- 单据
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  public BillVO insertForAccount(ClientLink cl, BillVO bill, String sFQSK,
      String sWTDX) throws BusinessException {
    BillVO bvo = null;

    try {
      CommonDataImpl cbo = new CommonDataImpl();
      
      //处理标志
      boolean bIsOut = false;
      boolean bIsPlanedPriceAdjustBill = false;
      boolean bIsAdjustBill = false;

      String sBillType = ((BillHeaderVO) bill.getParentVO()).getCbilltypecode();
      if (sBillType.equals(ConstVO.m_sBillQCRKD)
          || sBillType.equals(ConstVO.m_sBillCGRKD)
          || sBillType.equals(ConstVO.m_sBillCCPRKD)
          || sBillType.equals(ConstVO.m_sBillQTRKD)
          || sBillType.equals(ConstVO.m_sBillRKTZD)
          || sBillType.equals(ConstVO.m_sBillJHJTZD)
          || sBillType.equals(ConstVO.m_sBillWWJGSHD)
          || sBillType.equals(ConstVO.m_sBillDBRKD)) {
        bIsOut = false;
      } else if (sBillType.equals(ConstVO.m_sBillQCXSCBJZD)
          || sBillType.equals(ConstVO.m_sBillXSCBJZD)
          || sBillType.equals(ConstVO.m_sBillCLCKD)
          || sBillType.equals(ConstVO.m_sBillQTCKD)
          || sBillType.equals(ConstVO.m_sBillBFD)
          || sBillType.equals(ConstVO.m_sBillCKTZD)
          || sBillType.equals(ConstVO.m_sBillWWJGFLD)
          || sBillType.equals(ConstVO.m_sBillDBCKD)) {
        bIsOut = true;
      }

      if (sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        //是计划价调整单
        bIsPlanedPriceAdjustBill = true;
      }

      if (sBillType.equals(ConstVO.m_sBillRKTZD)
          || sBillType.equals(ConstVO.m_sBillCKTZD)
          || sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        bIsAdjustBill = true;
      }

      boolean bFQSK = false;
      if ((bill != null) && (bill.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }
      }

      bvo = this.insertBill(cl, bill, bIsOut, bIsAdjustBill,
          bIsPlanedPriceAdjustBill, bFQSK, null, null, cbo.getParaValue(
              ((BillHeaderVO) bill.getParentVO()).getPk_corp(), new Integer(
                  ConstVO.m_iPara_SFJSXNCB)));

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return bvo;
  }

  /**
   * 函数功能:插入数据（期初单据），不生成实时凭证
   * 
   * 参数: BillVO bill ----- 单据
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  public BillVO insertForBeginBills(ClientLink cl, BillVO bill)
      throws BusinessException {
    boolean bAccountLocked = false;
    BillVO bvo = null;
    try {
      IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
      srv.checkDefDataType(bill);

       CommonDataImpl cbo = new CommonDataImpl();

      //处理月末结帐和日常录入单据之间的并发操作
      bAccountLocked = this.lockAccount(bill);

      boolean bFQSK = false;
      if ((bill != null) && (bill.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();

        //获得业务类型
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(bhvo.getPk_corp(), sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }
      }

      bvo = this.insertBill(cl, bill, false, false, false, bFQSK, null, null,
          cbo.getParaValue(((BillHeaderVO) bill.getParentVO()).getPk_corp(),
              new Integer(ConstVO.m_iPara_SFJSXNCB)));
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    } finally {
      try {
        if (bAccountLocked) {
          this.releaseAccountLock(bill);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return bvo;
  }
  /**
   * 函数功能:计划价调整单中，根据用户条件选取存货，以修改其计划价
   * 
   * 参数:AddQueryVO 用户输入的查询条件
   * 
   * 返回值:AddQueryVO 查询得到的结果：存货，计划价，基准价
   * 
   * 异常: SQL 异常
   * 
   * @return nc.vo.ia.bill.AddQueryVO
   * @param condAddQueryVO
   *          nc.vo.ia.bill.AddQueryVO
   */
  public AddQueryVO queryAddBillVOs(AddQueryVO condAddQueryVO)
      throws BusinessException {
    AddQueryVO rstAddQueryVO = null;
    try {
      BillDMO dmo = new BillDMO();
      rstAddQueryVO = dmo.queryAddBillVOs(condAddQueryVO);
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return rstAddQueryVO;
  }

  /**
   * 函数功能: 在发生异常时调用，退回已经分配的单据号
   * 
   * 参数: sBillCode java.lang.String 已经分配的单据号 bill nc.vo.ia.bill.BillVO
   * 单据，分配单据号的对象 返回值: 无
   * 
   * 异常:
   * 
   * @exception BusinessException
   *              异常说明。
   */
  private void returnBillCode(ArrayList billCodes, BillVO bill)
      throws BusinessException {
    this.returnBillCodes(billCodes, new BillVO[] { bill });
  }

  /**
   * 函数功能: 在发生异常时调用，退回已经分配的单据号(批操作）
   * 
   * 参数: sBillCodes java.lang.String[] 记录已经分配的单据号的数组 bills BillVO[]
   * 单据数组，分配单据号的对象 返回值:
   * 
   * 异常:
   * 
   * @param billCodes
   *          ArrayList
   * @exception nc.vo.pub.BusinessException
   *              异常说明。
   */
  private void returnBillCodes(ArrayList billCodes, BillVO[] bills)
      throws BusinessException {

    if ((bills != null) && (billCodes != null)) {
      BillHeaderVO bhvo = null;
      BillItemVO[] bivo = null;
      for (int i = 0; i < bills.length; i++) {
        if (bills[i] == null) {
          continue;
        }
        //如果不是此次分配的则跳过
        if (!billCodes.contains(bills[i].getVBillCode())) {
          continue;
        }

        //退回系统分配的单据号
        //bcDmo.returnBillCode(bills[i]);

        //删除VO的表头表体中已经分配的单据号
        bhvo = (BillHeaderVO) bills[i].getParentVO();
        if (bhvo != null) {
          bhvo.setVbillcode(null);
        }
        bivo = (BillItemVO[]) bills[i].getChildrenVO();
        int len = 0;
        if (bivo != null) {
          len = bivo.length;
        }
        for (int j = 0; j < len; j++) {
          bivo[j].setVbillcode(null);
        }

      }

    }
  }

  /**
   * 函数功能:用独立事务插入单据
   * 
   * 参数: BillVO[] bvos ----- 单据信息 Hashtable ht ----- 存货信息 String sExistString
   * ----- 子查询语句（用于提高效率） Integer[] iPeci ----- 数据精度 String sPeriod ----- 会计期间
   * String sPeriodPerv ----- 前一个月 String sBeginDate ----- 本月开始天 String sEndDate
   * ----- 本月结束天 Hashtable ht ----- 参数查询结果 Hashtable htInv ----- 存货信息
   * 
   * binsrtedVOs,iPeci,sSourceModule,sSimulate,htttt,svo
   * 
   * 返回值:
   * 
   * 异常:
   *  
   */
  private BillVO[] saveOutterArray(ClientLink cl, BillVO[] bvos,
      Integer[] iPeci, String sSourceModuleName, String sSimulate,
      SimulateVO svo) throws Exception {
	  Log.debug("saveOutterArray执行了");
    CommonDataImpl cbo = new CommonDataImpl();
    Timer timer  = new Timer();
    timer.start();
    ArrayList alBillCodes = new ArrayList(); //记录设置到VO中的单据号
    String sFQSK = svo.getFQSK();
    String sWTDX = svo.getWTDX();
    try {
      //对每个单据补充数据
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];

        //补充数据
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        bhvo.setCbillid(null);

        //1、库存组织

        //获得单位编码
        String sCorpID = bhvo.getPk_corp();

        //获得库存组织
        String sRDID = bhvo.getCrdcenterid();
        if ((sRDID == null) || (sRDID.trim().length() == 0)) {//此if判断在红票审核时不执行
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000025")/* @res "无法获得库存组织，请调整" */);
          throw e;
        }

        //2、单据号

        //获得单据类型
        String sBillType = bhvo.getCbilltypecode();

        //设置单据号（表头表体)
        alBillCodes.add(this.setBillCode(bvo));
        timer.addExecutePhase("存货核算单据保存  获取单据号");

        //设置表体单位编码、单据类型、单据号、换算率
        for (int j = 0; j < bvo.getChildrenVO().length; j++) {
          BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[j];

          btvo.setCbill_bid(null);
          btvo.setCbillid(null);
          btvo.setPk_corp(sCorpID);
        }

        //获得收发标志
        Integer iRDFlag = bhvo.getFdispatchflag();
        if (iRDFlag == null) {
          iRDFlag = new Integer(1);

          for (int j = 0; j < ConstVO.m_sInBills.length; j++) {
            if (sBillType.equals(ConstVO.m_sInBills[j])) {
              iRDFlag = new Integer(0);
              break;
            }
          }
        } else if (iRDFlag.intValue() == 0) {
        }

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
          }
        }

        //合法性检查
        //checkDataforOutterBill(bIsOut, bvo, ConstVO.ADD_STATUS, htInv, bFQSK,
        //    iPeci);
        IBillCheck check = new CheckerForIA(); 
        bvo = check.checkForSave(new BillVO[]{bvo})[0];
        timer.addExecutePhase("存货核算单据保存 保存前检查");

        if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
          svo.setBills(bvos);

          //处理模拟成本
          //20070317 yuanhm 采购传存货核算慢，注掉算虚拟成本的算法
         // bvos = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
        }
        //timer.addExecutePhase("存货核算单据保存 处理模拟成本");
        //设置审核顺序默认值
        this.setDefaultAuditSequence(bvo);
        RichDMO rDmo = new RichDMO();
        //为解决南京浦镇库存签字在广域网上效率慢放开最后一个参数（设为false）
        rDmo.model2_CreateBatch(cl, new BillVO[] { bvo }, ((BillHeaderVO) bvo
            .getParentVO()).getVOMeta().getPkColName(), null, null, false);
        timer.addExecutePhase("存货核算单据保存 保存");
      }

      //特殊处理
      for (int i = 0; i < bvos.length; i++) {
        BillVO bill = bvos[i];

        String sBillType = ((BillHeaderVO) bill.getParentVO())
            .getCbilltypecode();

        //判断分期收款
        boolean bFQSK = false;

        String sBizTypeID = ((BillHeaderVO) bill.getParentVO()).getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }

        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //是销售成本结转单
          for (int j = 0; j < bill.getChildrenVO().length; j++) {
            BillItemVO btvo = (BillItemVO) bill.getChildrenVO()[j];
            String sSourceBillType = btvo.getCsourcebilltypecode();

            if (bFQSK) {
              //分期收款或委托代销
              if ((sSourceBillType != null)
                  && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
                //来源是销售发票
                this.doWithSOFaPiao(btvo, ConstVO.ADD_STATUS,cbo);
              }
            }
          }
        }
      }
      timer.addExecutePhase("存货核算单据保存 所有单据保存后续处理");
      
      timer.showAllExecutePhase("存货核算单据保存");

    } catch (Exception e) {
      //如果已经分配了就退回单据号
      this.returnBillCodes(alBillCodes, bvos);
      throw e;
    }
    return bvos;
  }

  /**
   * 设置审核顺序的默认值，如果为空设为-1，这样可以提高成本计算时查询的速度
   * 
   * @param bill
   */
  private void setDefaultAuditSequence(BillVO bill) {
    if ((bill != null) && (bill.getChildrenVO() != null)
        && (bill.getChildrenVO().length != 0)) {
      BillItemVO[] items = (BillItemVO[]) bill.getChildrenVO();
      int iLen = items.length;
      Integer iSeq = null;
      for (int i = 0; i < iLen; i++) {
        if (items[i] != null) {
          iSeq = (Integer) items[i].getIauditsequence();
          if (iSeq == null) {
            items[i].setIauditsequence(new Integer(-1));
          }
        }
      }
    }
  }

  /**
   * 函数功能:为一个单据VO设置单据号，包括单据的表头和表体
   * 
   * 参数: voBill --单据；
   * 
   * 返回值:String 成功设置的单据号， 如果是null表示没有被正确的设置单据号
   * 
   * 异常: setBillCodes函数会抛出BusinessException
   * 
   * @param voBill
   *          nc.vo.ia.pub.IBillCode
   */

  private String setBillCode(BillVO voBill) throws BusinessException {
    ArrayList result = this.setBillCodes(new BillVO[] { voBill });
    String s = null;
    if (result != null) {
      s = (String) result.get(0);
    }
    return s;
  }

  /**
   * 函数功能:为一组单据VO设置单据号，包括每张单据的表头和表体
   * 
   * 参数: voBills --单据数组；
   * 
   * 返回值:String 数组其中每一个元素都为设置成功的单据号， 如果是null表示该元素对应的单据没有被正确的设置单据号
   * 
   * 异常: sBillCode[i] = bcDmo.setBillCode(voBills[i]);将抛出BusinessException异常
   * 
   * @param voBills
   *          nc.vo.ia.pub.IBillCode[]O
   */

  private ArrayList setBillCodes(BillVO voBills[]) throws BusinessException {

    BillCodeDMO bcDmo = null;
    try {
      bcDmo = new BillCodeDMO();
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }

    if (voBills == null) {
      return null;
    }
    //确保voBills中没有空元素
    for (int i = 0; i < voBills.length; i++) {
      if (voBills[i] == null) {
        return null;
      }
    }

    //批取单据号并置入vo中
    HashMap hm_billCodes = bcDmo.setBillCodes(voBills);

    if (hm_billCodes == null) {
      return null;
    }

    ArrayList codes = new ArrayList();

    Set entries = hm_billCodes.entrySet();
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      Object value = entry.getValue();
      codes.add(value);
    }

    return codes;
  }

  /**
   * 算法：月末结帐锁，单据录入锁 在月末结帐时：尝试对月末结帐加锁，如果不成功说明有单据正在录入，需等候；
   * 如果成功锁定，则继续对单据录入加锁，这样就无法再录入单据 在单据录入时：尝试对月末结帐加锁，如果不成功可能是在月末结帐或单据录入，继续
   * 对单据录入加锁，如果成功说明是其他单据录入在进行中，如果不成功说明月末结帐
   * 在进行中，对单据录入成功加锁后要马上释放该锁，以使其他单据录入线程可以同时工作 函数功能:控制单据录入和月末结帐之间的并发
   * 
   * 
   * 参数:String[] passAccount --- 锁定月末结帐的参数， LockBOAccess lbo --- 进行加锁操作的对象
   * BillVO bill --- 正在录入的单据 返回值:boolean --- 是否在本方法中锁定了月末结帐：true ---
   * 在调用本操作方法的最后要释放该锁 fasle --- 月末结帐是在其他地方被锁定的，不需释放
   * 
   * 异常:
   *  
   */
  private boolean lockAccount(BillVO bill) throws BusinessException {
    //锁数据，锁月末结帐
    //m_sAccountLock = "ACCOUNT" --- 月末结帐的并发锁标志
    String[] passAccount = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp()
        + ConstVO.m_sAccountLock };

    //m_sBillAdd = "BILLADD" --- 增加单据的并发锁标志
    String[] passAddBill = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp()
        + ConstVO.m_sBillAdd };

    String sOperID = ((BillHeaderVO) bill.getParentVO()).getCoperatorid();

    //先尝试锁定月末结帐锁
    boolean bAccountLocked = LockUtils.lock(passAccount, sOperID);

    if (!bAccountLocked) {
      //可能在做月末结帐，也可能是单据录入锁住了
      //尝试锁定单据录入锁
      boolean bCanLockAddBill = LockUtils.lock(passAddBill, sOperID);

      if (!bCanLockAddBill) {
        //不能锁单据录入，说明是正在进行月末结帐或其他用户的单据录入锁还没来得及释放
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("20143010", "UPP20143010-000023")/*
                                                          * @res
                                                          * "相关单据正在进行处理，请稍后再试"
                                                          */);
      } else {
        //是单据录入操作锁的月末结帐锁，可以继续录入单据，但要将单据录入锁释放，使下一个单据来时可以锁单据录入锁
        LockUtils.unLock(passAddBill, sOperID);
      }
    }
    return bAccountLocked;
  }

  /**
   * 函数功能：释放月末结账锁
   * 
   * @param bill
   *          应该是加锁时使用的bill
   * @throws BusinessException
   */
  private void releaseAccountLock(BillVO bill) throws BusinessException {
    String[] passAccount = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp() + ConstVO.m_sAccountLock };
    String sOperID = ((BillHeaderVO) bill.getParentVO()).getCoperatorid();
    
    LockUtils.unLock(passAccount, sOperID);
    
  }

  /**
   * 函数功能:修改数据
   * 
   * 参数: BillVO voCurBill ----- 当前单据 BillVO bvo ----- 要修改的单据 boolean bIsOut
   * ----- 是否是出库单 boolean bIsAdjustBill ----- 是否是调整单 String sUserID ----- 操作员id
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  public BillVO update(ClientLink cl, BillVO voCurBill, BillVO bvo,
      UFBoolean ufbIsOut, UFBoolean ufbIsAdjustBill, String sUserID)
      throws BusinessException {
    String[] sItemIDs = null;
    boolean bIfCanDO = false;
    boolean bIsOut = ufbIsOut.booleanValue();
    boolean bIsAdjustBill = ufbIsAdjustBill.booleanValue();

    try {
      IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
      srv.checkDefDataType(bvo);

      CommonDataImpl cbo = new CommonDataImpl();

      String sPK = voCurBill.getParentVO().getPrimaryKey();

      //锁数据
      sItemIDs = null;
      Vector vItemIDs = new Vector(1, 1);

      vItemIDs.addElement(sPK);

      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        String sID = bvo.getChildrenVO()[i].getPrimaryKey();
        if (sID != null) {
          vItemIDs.addElement(sID);
        }
      }

      sItemIDs = new String[vItemIDs.size()];
      vItemIDs.copyInto(sItemIDs);

      bIfCanDO = LockUtils.lock(sItemIDs, sUserID);

      if (bIfCanDO == false) {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000023")/*
                                                                        * @res
                                                                        * "相关单据正在进行处理，请稍后再试"
                                                                        */);

        throw e;
      }

      boolean bFQSK = false;
      if ((bvo != null) && (bvo.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        //获得业务类型
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(bhvo.getPk_corp(), sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }
      }

      voCurBill = this.updateBill(cl, voCurBill, bvo, bIsOut, bIsAdjustBill,
          sUserID, bFQSK, cbo.getParaValue(((BillHeaderVO) bvo.getParentVO())
              .getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB)));

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    } finally {
      try {
        if (bIfCanDO) {
          LockUtils.unLock(sItemIDs, sUserID);
        }
      } catch (Exception e) {
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("20143010", "UPP20143010-000010")/* @res "释放业务锁出错" */);
      }
    }
    return voCurBill;
  }

  /**
   * 函数功能:插入数据
   * 
   * 参数: BillVO voCurBill ----- 当前单据 BillVO bvo ----- 要修改的单据 boolean bIsOut
   * ----- 是否是出库单 boolean bIsAdjustBill ----- 是否是调整单 String sUserID ----- 操作员id
   * boolean bIsFQWT ----- 是否分期收款或委托代销 String sSimulate ----- 是否计算虚拟成本
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  private BillVO updateBill(ClientLink cl, BillVO voCurBill, BillVO bvo,
      boolean bIsOut, boolean bIsAdjustBill, String sUserID, boolean bIsFQWT,
      String sSimulate) throws Exception {
    //获得业务日期
    String sDate = ((BillHeaderVO) bvo.getParentVO()).getDbilldate().toString();

    if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
      //处理模拟成本
      SimulateVO svo = new SimulateVO();
      svo.setBills(new BillVO[] { bvo });
      bvo = this.doWithSimulateCost(svo, ConstVO.UPDATE_STATUS)[0];
    }

    //对修改数据进行合法性检查
    if (this.checkUpdateData(bvo, voCurBill) == false) {
      return voCurBill;
    }

    //合法性检查
    this.checkData(voCurBill, bvo, bIsOut, bIsAdjustBill, false,
        ConstVO.UPDATE_STATUS, sDate);

    //更新数据库，先插入，再修改
    this.execUpdate(cl, voCurBill, bvo);

    //修改后红冲单，认为是删除后的处理，将原被调整单据上的累计回冲数量恢复
    this.doWithRedBill(voCurBill, ConstVO.DELETE_STATUS);

    //处理与固定资产接口，认为是删除后的处理
    CommonDataImpl cbo = new CommonDataImpl();
    //20050527_关闭存货与固定资产接口
    //	UFBoolean bStart =
    // cbo.isModuleStarted(((BillHeaderVO)bvo.getParentVO()).getPk_corp(),ConstVO.m_sModuleCodeFA,cl.getAccountYear()
    // + "-" + cl.getAccountMonth());
    //
    //	if (bIsAdjustBill == false && bStart.booleanValue())
    //	{
    //		doWithFa(voCurBill,bvo,bIsOut,ConstVO.DELETE_STATUS);
    //	}

    //处理来源发票的销售成本结转单
    String sBillType = ((BillHeaderVO) voCurBill.getParentVO())
        .getCbilltypecode();
    

    if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      //是销售成本结转单
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        String sSourceModuleName = ((BillHeaderVO) bvo.getParentVO())
            .getCsourcemodulename();

        if ((sSourceBillType != null)
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          //来源是销售发票

          //如果原来有，删除原数据
          for (int j = 0; j < voCurBill.getChildrenVO().length; j++) {
            if (voCurBill.getChildrenVO()[j].getPrimaryKey().equals(
                btvo.getPrimaryKey())) {
              BillItemVO voCurItem = (BillItemVO) voCurBill.getChildrenVO()[j];
              this.doWithSaleBillsFromFaPiao(sSourceModuleName, voCurItem,
                  ConstVO.DELETE_STATUS, cbo);

              break;
            }
          }
        }
      }
    }

    //更新当前VO的数据
    voCurBill.setParentVO(bvo.getParentVO());

    Vector vTemp = new Vector(1, 1);
    BillItemVO[] newbtvos = null;

    //将原数据设置入Vector
    for (int i = 0; i < voCurBill.getChildrenVO().length; i++) {
      vTemp.addElement((BillItemVO) voCurBill.getChildrenVO()[i]);
    }

    if ((bvo.getChildrenVO() != null) && (bvo.getChildrenVO().length != 0)) {
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        //新的分录VO
        BillItemVO updatebtvo = (BillItemVO) bvo.getChildrenVO()[i];

        //获得单据状态
        int iStatus = updatebtvo.getStatus();

        if (iStatus == nc.vo.pub.VOStatus.NEW) {
          //增加
          if (updatebtvo.getIrownumber().intValue() < vTemp.size()) {
            vTemp.insertElementAt(updatebtvo, updatebtvo.getIrownumber()
                .intValue() - 1);
          }
          else {
            vTemp.addElement(updatebtvo);
          }
        } else {
          //是修改或删除
          int iLength = vTemp.size();
          for (int j = 0; j < iLength; j++) {
            BillItemVO btvo = (BillItemVO) vTemp.elementAt(j);

            if (btvo.getCbill_bid().equals(updatebtvo.getCbill_bid())) {
              if (iStatus == nc.vo.pub.VOStatus.UPDATED) {
                //修改
                vTemp.setElementAt(updatebtvo, j);
              } else if (iStatus == nc.vo.pub.VOStatus.DELETED) {
                //删除
                vTemp.removeElementAt(j);
                j = j - 1;
              }
              break;
            }
          }
        }
      }
    }

    //设置入分录VO数组，按行号排序
    newbtvos = new BillItemVO[vTemp.size()];
    vTemp.copyInto(newbtvos);

    voCurBill.setChildrenVO(newbtvos);

    //修改后红冲单，认为是增加后的处理，将原被调整单据上的累计回冲数量增加
    this.doWithRedBill(voCurBill, ConstVO.ADD_STATUS);

    //处理与固定资产接口，认为是增加后的处理//20050527_关闭存货与固定资产接口
    //	if (bIsAdjustBill == false && bStart.booleanValue())
    //	{
    //		doWithFa(voCurBill,bvo,bIsOut,ConstVO.ADD_STATUS);
    //	}

    //处理来源发票的销售成本结转单
    if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      //是销售成本结转单
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        String sSourceModuleName = ((BillHeaderVO) bvo.getParentVO())
            .getCsourcemodulename();

        if (bIsFQWT) {
          if ((sSourceBillType != null)
              && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
            //来源是销售发票
            this.doWithSaleBillsFromFaPiao(sSourceModuleName, btvo, ConstVO.ADD_STATUS, cbo);
          }
        }
      }
    }

    return voCurBill;
  }

  /**
   * 函数功能:修改期初单据数据（不处理实时凭证）
   * 
   * 参数: BillVO voCurBill ----- 当前单据 BillVO bill ----- 单据 String sUserID -----
   * 操作员id
   * 
   * 返回值:单据
   * 
   * 异常:
   *  
   */
  public BillVO updateForBeginBill(ClientLink cl, BillVO voCurBill, BillVO bvo,
      String sUserID) throws BusinessException {
    String[] sItemIDs = null;
    boolean bIfCanDO = false;

    try {
      IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
      srv.checkDefDataType(bvo);

      CommonDataImpl cbo = new CommonDataImpl();

      String sPK = voCurBill.getParentVO().getPrimaryKey();

      //锁数据
      sItemIDs = new String[voCurBill.getChildrenVO().length + 1];
      sItemIDs[0] = sPK;

      for (int i = 0; i < voCurBill.getChildrenVO().length; i++) {
        String sID = voCurBill.getChildrenVO()[i].getPrimaryKey();
        sItemIDs[i + 1] = sID;
      }

      bIfCanDO = LockUtils.lock(sItemIDs, sUserID);

      if (bIfCanDO == false) {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000023")/*
                                                                        * @res
                                                                        * "相关单据正在进行处理，请稍后再试"
                                                                        */);
        throw e;
      }

      boolean bFQSK = false;
      if ((bvo != null) && (bvo.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        //获得业务类型
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(bhvo.getPk_corp(), sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        String sBizTypeID = bhvo.getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }
      }

      voCurBill = this.updateBill(cl, voCurBill, bvo, false, false, sUserID, bFQSK,
          cbo.getParaValue(((BillHeaderVO) bvo.getParentVO()).getPk_corp(),
              new Integer(ConstVO.m_iPara_SFJSXNCB)));

    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    } finally {
      try {
        if ((sItemIDs != null) && bIfCanDO) {
          LockUtils.unLock(sItemIDs, sUserID);
        }
      } catch (Exception e) {
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("20143010", "UPP20143010-000010")/* @res "释放业务锁出错" */);
      }
    }
    return voCurBill;
    
  }

	public void deleteBillsByIABillIds(String[] headIDs, String sCorpID, String sUserID) throws BusinessException {
		// TODO 自动生成方法存根
		this.deleteIABillsByIAPks(headIDs, sCorpID,  sUserID);
	}

	public void deleteBillsBySourceIds(String[] sSourceHIDs, String sCorpID, String sUserID) throws BusinessException {
		// TODO 自动生成方法存根
		this.deleteBillFromOutterArray(sSourceHIDs,sCorpID,sUserID);
	}

	public void deleteItemsBySourceItemIds(String[] sSourceBIDs, String sUserID) throws BusinessException {
		// TODO 自动生成方法存根
		this.deleteBillItemForPUs(sSourceBIDs,sUserID);
	}

	public Object saveBills(BillVO[] bvos, String sSourceModuleName, String sSourceBillType) throws BusinessException {
		// TODO 自动生成方法存根
		return this.saveBillFromOutterArray(bvos,sSourceModuleName,sSourceBillType);
	}
  
  public BillVO[] importBills(String pk_corp,String date,String userID,
      String modulename)throws BusinessException{
    BillVO[] bills = null;
    ImporBegintBillAction action = new ImporBegintBillAction();
    try {
      bills = action.importBills(pk_corp,date,userID,modulename );
    }
    catch (RuntimeException ex) {
      ExceptionUtils.marsh( ex );
    }
    return bills;
  }
  
}