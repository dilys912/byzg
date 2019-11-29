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
 * ��������:����ҵ�����
 * 
 * ����:����
 * 
 * ��������:2001-05-29
 * 
 * �޸ļ�¼������:
 * 
 * �޸���:
 *  
 *  zhwj
 */
public class BillImpl implements IchangeVO, IIAToPUBill, IBill, IBillService,IBillQuery{

  /**
   * BillBO ������ע�⡣
   */
  public BillImpl() {
    super();
  }

  /**
   * ��������:��������Ƿ���ȷ
   * 
   * ����: BillHeaderVO checkbhvo ----- ���ݱ�ͷVO BillItemVO checkbtvo ----- ���ݷ�¼VO
   * double dNumber ----- �������� boolean bIsAuditBatch ----- �Ƿ����κ����ж�
   * 
   * ����ֵ:�Ƿ���ȷ
   * 
   * �쳣:
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

    //���ҵ������
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
      //����м�¼��˵���������޸ĵģ���ѯ���θ����ʱӦ�ò����Ǵ��е����ϵ�����
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

      //��������
      if (dRestNumber >= dNumber) {
        bRight = true;
      }
    }

    if (bRight == false) {
      Integer iRowIndex = checkbtvo.getIrownumber();
      String sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "20143010", "UPP20143010-000000")/* @res "���ι������ڴ������ϵͳ�г��ָ����⣬�����" */;

      if (bIsAuditBatch) {
        sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
            "20143010", "UPP20143010-000001")/* @res "���κ������ڴ������ϵͳ�г��ָ����⣬�����" */;
      }

      if ((iRowIndex != null) && (dCanSendNum != 0)) {
        if (bIsAuditBatch) {
          //sMessage = "��" + iRowIndex + "�еĴ��Ϊ���κ��㣬����������Ϊ" + dCanSendNum +
          // "�������";/*-=notranslate=-*/
          String[] value = new String[] { String.valueOf(iRowIndex),
              String.valueOf(dCanSendNum) };
          sMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
              "20143010", "UPP20143010-000178", null, value);
        } else {
          //sMessage = "��" + iRowIndex + "�еĴ��Ϊ���ι�������������Ϊ" + dCanSendNum +
          // "�������";;/*-=notranslate=-*/
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
   * ��������:������ݺϷ���
   * 
   * 
   * ����: BillVO voCurBill ----- ��ǰVO BillItemVO btvo ----- Ҫ����VO boolean bIsOut
   * ----- �Ƿ���� boolean bIsAdjustBill ----- �Ƿ��ǵ���������������Ҫ������Σ� boolean
   * bIsPlanedPriceAdjustBill ----- �Ƿ��Ǽƻ��۵����� int iRowIndex ----- ״̬ String
   * sDate ----- ҵ������
   * 
   * ����ֵ:�Ƿ�Ϸ�
   * 
   * �쳣:
   * 
   *  
   */
  private boolean checkData(BillVO voCurBill, BillVO bvo, boolean bIsOut,
      boolean bIsAdjustBill, boolean bIsPlanedPriceAdjustBill, int iStatus,
      String sDate) throws Exception {
    //��ʼ��BO
    CommonDataImpl cbo = new CommonDataImpl();

    AccountImpl abo = new AccountImpl();

    //�ж��Ƿ��Ѿ���ĩ����
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
        //����ĩ����
        BusinessException e = null;
        if ((sAccountMonth.equals("00") == false)
            && (iStatus == ConstVO.ADD_STATUS)
            && (sBillType.equals(ConstVO.m_sBillQCRKD) == false)
            && (sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false)) {
          //e = new BusinessException("��ǰ����ڼ�" + sAccountYear + "��" +
          // sAccountMonth + "�ڼ�����ĩ���ˣ����������ӵ��ݣ������µ�¼���޸ĵ�������");/*-=notranslate=-*/
          String[] value = new String[] { sAccountYear, sAccountMonth };
          e = new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
              .getStrByID("20143010", "UPP20143010-000180", null, value));

          throw e;
        } else if (iStatus == ConstVO.ADD_STATUS) {
          e = new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
              .getStrByID("20143010", "UPP20143010-000003")/*
                                                            * @res
                                                            * "�Ѿ��ڳ����ʣ�������������������ǰ�ĵ��ݣ������µ�¼���޸ĵ�������"
                                                            */);
          throw e;
        }
      }
    }

    //�������֯��Ӧ�Ƿ����
    if ((bhvo.getCstockrdcenterid() != null) && (bhvo.getCrdcenterid() != null)) {
      OutterDMO dmo = new OutterDMO();

      //	ArrayList alICCalbodys ----- ��¼�����֯(�ִ�)+�ֿ⣬�ڲ���string[] 0�ǿ����֯(�ִ�)��1�ǲֿ�
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
                                                                        * "�ִ������֯�Ͳֿ���ɱ������֮֯��û�ж�Ӧ��ϵ�������"
                                                                        */);
        throw e;
      }
    }

    //������ǵ�����ȡ���������ʽ�ת������������������ǵ�������־Ϊ��
    if ((sBillType.equals(ConstVO.m_sBillDJTQD) == false)
        && (sBillType.equals(ConstVO.m_sBillCYLJZD) == false)
        && (sBillType.equals(ConstVO.m_sBillSYTZD) == false)
        && (bIsAdjustBill == false)) {
      Hashtable ht = new Hashtable(); //��¼�Ƿ����ι���
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];

        //����Ƿ�����������κ�
        String sBill_bID = btvo.getPrimaryKey();
        String sInv = btvo.getCinventoryid();
        String sBatch = btvo.getVbatch();
        String sSourceBillID = btvo.getCsourcebillid();
        String sSourceBillType = btvo.getCsourcebilltypecode();

        if ((sSourceBillID == null) || (sSourceBillID.trim().length() == 0)) {
          //����Ǵ���������ɵĵ��ݣ�������κ�
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
            //BusinessException e = new BusinessException("��" +
            // btvo.getIrownumber() +
            // "�еĴ�������ι�������ָ�����κţ������");/*-=notranslate=-*/
            String[] value = new String[] { btvo.getIrownumber().toString() };
            BusinessException e = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                    "UPP20143010-000181", null, value));
            throw e;
          } else if (bIsManageForBatch.booleanValue()
              && ((sBatch != null) && (sBatch.trim().length() != 0))) {
            //����ǳ��ⵥ�����빻��������
            if (bIsOut) {
              //������Դ��Ʊ�����۳ɱ���ת��
              if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
                if ((sSourceBillType == null)
                    || (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false)) {
                  //��Դ�������۷�Ʊ
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
              //����嵥Ҳ�ж�
              double dNumber = btvo.getNnumber().doubleValue();
              if ((dNumber < 0)
                  && (this.checkBatchNumber(bhvo, btvo, -dNumber, false) == false)) {
                return false;
              }
            }
          }
        }

        //�����������ж�
        if (bhvo.getBwithdrawalflag().booleanValue()) {
          //������
          UFDouble ufdNumber = btvo.getNnumber();
          double dNumber = 0;

          if (ufdNumber != null) {
            dNumber = ufdNumber.doubleValue();
          }

          if (dNumber > 0) {
            //BusinessException e = new BusinessException("��ǰ�����Ǽ����ϵ��ݣ���" +
            // btvo.getIrownumber() + "�еĴ����������0�������");/*-=notranslate=-*/
            String[] value = new String[] { btvo.getIrownumber().toString() };
            BusinessException e = new BusinessException(
                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                    "UPP20143010-000182", null, value));
            throw e;
          }
        }

        //�����ʲ���Ϊ��
        UFDouble ufdRate = btvo.getNchangerate();
        double dAstRate = 0;

        if (ufdRate != null) {
          dAstRate = ufdRate.doubleValue();
        } else if ((btvo.getCastunitid() != null)
            && (bhvo.getCsourcemodulename().equals(ConstVO.m_sModulePO) == false)) {
          //�ɹ���������޷����뻻���ʣ����Բ���������Ϊ�յ��ж�
          //BusinessException e = new BusinessException("��" +
          // btvo.getIrownumber() +
          // "��¼���˸�������λ����û�л�����,���ܱ��棬�����");/*-=notranslate=-*/
          String[] value = new String[] { btvo.getIrownumber().toString() };
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000183", null, value));
          throw e;
        }

        if (dAstRate < 0) {
          //BusinessException e = new BusinessException("��" +
          // btvo.getIrownumber() + "�л�����Ϊ����,���ܱ��棬�����");/*-=notranslate=-*/
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

            //�޸ĺ����������С���ۼƻس����� �� �ۼƷ�������
            if ((dNewNumber != null) && (dSendNumber != null)
                && (dRetractNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dSendNumber.add(dRetractNumber).doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƷ�������Ϊ" +
                // dSendNumber.doubleValue() + "���ۼƻس�����Ϊ" +
                // dRetractNumber.doubleValue() + "���޸ĺ���������벻С���ۼƷ����������ۼƻس�����֮��"
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
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƷ�������Ϊ" +
                // dSendNumber.doubleValue() + "���ۼƻس�����Ϊ" +
                // dRetractNumber.doubleValue() +
                // "���޸ĺ���������ۼƷ����������ۼƻس�����֮����ţ������");/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString(),
                    dRetractNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000186", null, value));
                throw e;
              }
            }

            //��ⵥ��������С���ۼƳ�������
            if ((dNewNumber != null) && (dSendNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dSendNumber.doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƷ�������Ϊ" +
                // dSendNumber.doubleValue() + "���޸ĺ���������벻С���ۼƷ�������" +
                // dSendNumber.doubleValue());/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString(),
                    dSendNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000187", null, value));

                throw e;
              } else if (d1 * d2 < 0) {
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƷ�������Ϊ" +
                // dSendNumber.doubleValue() +
                // "���޸ĺ���������ۼƷ���������ţ������");/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dSendNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000188", null, value));
                throw e;
              }
            }

            //�޸ĺ����������С���ۼƻس�����
            if ((dNewNumber != null) && (dRetractNumber != null)) {
              double d1 = dNewNumber.doubleValue();
              double d2 = dRetractNumber.doubleValue();
              if ((d1 * d2 >= 0) && (Math.abs(d1) < Math.abs(d2))) {
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƻس�����Ϊ" +
                // dRetractNumber.doubleValue() + "���޸ĺ���������벻С���ۼƻس�����" +
                // dRetractNumber.doubleValue());/*-=notranslate=-*/
                String[] value = new String[] {
                    btvo.getIrownumber().toString(), dRetractNumber.toString(),
                    dRetractNumber.toString() };
                BusinessException e = new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
                        "20143010", "UPP20143010-000189", null, value));
                throw e;
              } else if (d1 * d2 < 0) {
                //BusinessException e = new BusinessException("��" +
                // btvo.getIrownumber() + "�еĴ���ۼƻس�����Ϊ" +
                // dRetractNumber.doubleValue() +
                // "���޸ĺ���������ۼƻس�������ţ������");/*-=notranslate=-*/
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
//����ͨ��������ƣ�����ͨ���ɱ������ʱ�������ƣ�
//��ֻ�����ڱ��żƻ��۵��������гɱ�����ĵ����ϵļƻ��ۻ�ȡ���ε�����ļƻ���
//    //����Ǽƻ��۵��������ж��Ƿ�˿����֯������ı��µ�������¼�������
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
//                  "UPP20143010-000005")/* @res "����" */+ s[0] + ";";
//        }
//
//        if (sHintString.length() != 0) {
//          BusinessException e = new BusinessException(sHintString
//              + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000006")/*
//                                        * @res
//                                        * "������ͬ�����֯������ķ�¼δ���гɱ����㣬�˼ƻ��۵��������ܱ��棬�����"
//                                        */);
//          throw e;
//        }
//      }
//    }

    return true;
  }

  /**
   * ��������:������ݺϷ���
   * 
   * 
   * ����: BillVO voCurBill ----- ��ǰVO BillItemVO btvo ----- Ҫ����VO boolean bIsOut
   * ----- �Ƿ���� boolean bIsAdjustBill ----- �Ƿ��ǵ���������������Ҫ������Σ� boolean
   * bIsPlanedPriceAdjustBill ----- �Ƿ��Ǽƻ��۵����� int iRowIndex ----- ״̬ String
   * sDate ----- ҵ������ Hashtable ht ----- �����Ϣ Integer[] iPeci ----- ���ݾ���
   * 
   * ����ֵ:�Ƿ�Ϸ�
   * 
   * �쳣:
   * 
   *  
   */
//  private boolean checkDataforOutterBill(boolean bIsOut, BillVO bvo,
//      int iStatus, Hashtable htInvs, boolean bIsFQSK, Integer[] iPeci)
//      throws Exception {
//    CommonDataImpl cbo = new CommonDataImpl();
//
//    //�ж�
//    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
//
//    String sCorpID = bhvo.getPk_corp();
//    String sBillType = bhvo.getCbilltypecode();
//
//    //�������ȡ�����·���Ϊ��
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
//    //�Ƿ������
//    boolean bIsWithdrawalflag = bhvo.getBwithdrawalflag().booleanValue();
//
//    //�����������
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
//      //�������ݾ���
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
//      //��ɾ���в��ü��Ϸ���
//      if (iStatus == ConstVO.UPDATE_STATUS
//          && btvo.getStatus() == VOStatus.DELETED) {
//        continue;
//      }
//
//      if (iStatus == ConstVO.ADD_STATUS) {
//        //�����к�
//        btvo.setIrownumber(new Integer(i + 1));
//
//        //���������Ϊ��
//        btvo.setCauditorid(null);
//        btvo.setDauditdate(null);
//        btvo.setIauditsequence(null);
//      }
//
//      //��üƼ۷�ʽ����
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
//      //����ֵ:������Ƽ۷�ʽ���ƻ��ۡ��Ƿ����κ��㡢�����֯���ο��ɱ����Ƿ����ι���������̬������������ƻ���
//      if (sResults != null && sResults.length > 0
//          && sResults[0][1].trim().length() != 0
//          && sResults[0][1].equals("0") == false) {
//        Integer iPriceCode = new Integer(sResults[0][1]);
//        if (iPriceCode != null && iPriceCode.intValue() > 0
//            && iPriceCode.intValue() < 7) {
//          btvo.setFpricemodeflag(iPriceCode);
//        } else {
//          //BusinessException e = new BusinessException("��ǰ�����֯����" + (i+1) +
//          // "�еĴ��û�ж���Ƽ۷�ʽ���뵽��������������������");/*-=notranslate=-*/
//          String[] value = new String[] { String.valueOf(i + 1) };
//          BusinessException e = new BusinessException(
//              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000191", null, value));
//          throw e;
//        }
//
//
//        UFDouble dPlanedPrice = null;
//        //����ȡ�������������ϵļƻ���
//        if (sResults[0][2] != null
//            && sResults[0][2].toString().trim().length() != 0) {
//          dPlanedPrice = new UFDouble(sResults[0][2]);        
//        }
//        if (iPriceCode.intValue() == ConstVO.JHJ) {
//          //�Ǽƻ��ۼƼۣ��ж��Ƿ��������������������˼ƻ���
//          if (dPlanedPrice == null) {
//            //BusinessException e = new BusinessException("��ǰ�����֯����" + (i+1) +
//            // "�еĴ���Ǽƻ��ۼƼۣ���û�ж���ƻ��ۣ��뵽��������������������");/*-=notranslate=-*/
//            String[] value = new String[] { String.valueOf(i + 1) };
//            BusinessException e = new BusinessException(
//                nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                    "UPP20143010-000192", null, value));
//            throw e;
//          }
//        }
//        //���ڷǼƻ��۵Ĵ������������������ϼƻ���δ���壬��ȡ����������ϵļƻ���
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
//        //�Ƿ����κ���
//        bAuditBatch = new UFBoolean(sResults[0][3]);
//
//        btvo.setBauditbatchflag(bAuditBatch);
//
//        if (bAuditBatch.booleanValue()
//            && (sBatch == null || sBatch.trim().length() == 0)) {
//          //BusinessException e = new BusinessException("��ǰ�����֯����" + (i+1) +
//          // "�еĴ���ǰ����κ��㣬��û���������κţ����������");/*-=notranslate=-*/
//          String[] value = new String[] { String.valueOf(i + 1) };
//          BusinessException e = new BusinessException(
//              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
//                  "UPP20143010-000193", null, value));
//          throw e;
//        }
//
//        //�Ƿ����ι�������¼�����κ�
//        bIsManageForBatch = new UFBoolean(sResults[0][6]);
//
//        if (bIsManageForBatch.booleanValue()
//            && (sBatch == null || sBatch.trim().length() == 0)) {
//          //Ϊ����ɹ���Ʊ��֪�����κţ����ι�����Բ�¼Log.info(       
//        	Log.info("��" + btvo.getIrownumber()
//              + "�еĴ�������ι�������ָ�����κţ������");/*-=notranslate=-*/
//          //BusinessException e = new BusinessException("��" +
//          // btvo.getIrownumber() + "�еĴ�������ι�������ָ�����κţ������");
//          //throw e;
//        }
//
//        //������̬
//        btvo.setCinvkind(sResults[0][7]);
//      } else {
//        //BusinessException e = new BusinessException("��ǰ�����֯����" + (i+1) +
//        // "�еĴ��û�ж���Ƽ۷�ʽ���뵽��������������������");/*-=notranslate=-*/
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
//      //�Ƿ�����˷�¼
//      btvos[i].setBadjustedItemflag(new UFBoolean("N"));
//
//      //�Ƿ���Ʒ
//      if (btvos[i].getBlargessflag() == null) {
//        btvos[i].setBlargessflag(new UFBoolean("N"));
//      }
//
//      //�Ƿ�������ʵʱƾ֤
//      btvos[i].setBrtvouchflag(new UFBoolean("N"));
//
//      //����Ǽ����ϣ���������Ϊ����
//      UFDouble ufdNumber = btvo.getNnumber();
//
//      if (bIsWithdrawalflag && ufdNumber != null && ufdNumber.doubleValue() > 0) {
//        //BusinessException e = new BusinessException("��ǰ�����Ǽ����ϵ��ݣ�������������С��0����"
//        // + (i+1) + "�еĴ�������������������");/*-=notranslate=-*/
//        String[] value = new String[] { String.valueOf(i + 1) };
//        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
//            .getInstance().getStrByID("20143010", "UPP20143010-000156", null,
//                value));
//        throw e;
//      }
//    }
//
//    //�Ϸ��Լ��
//    //bvo.validate();
//    //����validate�������븸��smartVO��throws�Ӿ��ͻ�����Ը���verify
//    bvo.verify();
//
//    return true;
//  }

  /**
   * ��������:����Ƿ��ɾ�����ݣ��ѳɱ����㲻��ɾ��
   * 
   * ����: BillVO bvo ----- ����VO
   * 
   * ����ֵ:�Ƿ��ɾ��
   * 
   * �쳣:
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
                                                                      * "�˵��ݿ����Ѿ���ɾ����ɱ����㣬����ɾ��"
                                                                      */);
      throw e;
    }

    //2���Ƿ�����Դ���ⵥ�����۳ɱ���ת���������Ѿ��в��ֱ�����
    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
    if (btvos.length != 0) {
      String sSourceBillType = btvos[0].getCsourcebilltypecode();
      String sBillType = btvos[0].getCbilltypecode();
      if ((sBillType.equals(ConstVO.m_sBillXSCBJZD) || sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD))
          && (sSourceBillType != null)
          && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
        //�Ƿ��в����Ѿ�����
        for (int i = 0; i < btvos.length; i++) {
          String sBill_bid = btvos[i].getCbill_bid();

          //������֮ǰ�ĵ���ɾ���а���Դ��Ʊ�����۳ɱ���ת��ɾ���ˣ�����Ҫ���²�ѯ
          sSQL = new StringBuffer(" select ");
          sSQL = sSQL.append(" nsettledsendnum");
          sSQL = sSQL.append(" from ");
          sSQL = sSQL.append(" ia_bill_b ");
          sSQL = sSQL.append(" where ");
          sSQL = sSQL.append(" dr = 0 ");
          sSQL = sSQL.append(" and ");
          sSQL = sSQL.append(" cbill_bid = '" + sBill_bid + "'");

          sResult = cbo.queryData(sSQL.toString());

          //��������
          UFDouble dSettleNum = new UFDouble(0);
          if (sResult.length != 0) {
            String sTemp[] = sResult[0];
            if ((sTemp.length != 0) && (sTemp[0].trim().length() != 0)) {
              dSettleNum = new UFDouble(sTemp[0]);
            }
          }

          if ((dSettleNum != null) && (dSettleNum.doubleValue() > 0)) {
            //BusinessException e = new BusinessException("�˵��ݵĵ�" + (i+1) +
            // "�д���в����Ѿ����㣬����ɾ���������");/*-=notranslate=-*/
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
   * ��������:����Ƿ���޸ĵ��ݣ��ѳɱ����㲻���޸�
   * 
   * ����: BillVO bvo ----- ����VO
   * 
   * ����ֵ:�Ƿ���޸ĵ���
   * 
   * �쳣:
   *  
   */
  private boolean checkUpdateData(BillVO bvo, BillVO bcurVO) throws Exception {
    int iCount = 0;
    String[][] sResult = null;
    CommonDataImpl cbo = new CommonDataImpl();

    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    String sPK = bhvo.getPrimaryKey();
    UFBoolean bHeadAuditFlag = new UFBoolean(false);

    //���жϵ����Ƿ��ѱ��ɱ�����
    //��Ϊ�������ƣ�ֻ�ܲ����ݿ�

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
      //�ѱ�ɾ��
      BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000008")/*
                                                                      * @res
                                                                      * "�˵����Ѿ���ɾ�����������޸�"
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
      //�ѱ��ɱ����㣬�����жϷ�¼
      bHeadAuditFlag = new UFBoolean(true);
    } else {
      ////δ���ɱ����㣬�����ж��Ƿ��Ѿ�����ʵʱƾ֤
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
        //���з�¼����ʵʱƾ֤
        bHeadAuditFlag = new UFBoolean(true);
      }
    }

    //�жϷ�¼
    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
      Integer iRowNumber = btvo.getIrownumber();

      if ((btvo.getStatus() == VOStatus.UPDATED)
          || (btvo.getStatus() == VOStatus.DELETED)) {
        String sItemID = btvo.getPrimaryKey();

        //���޸ĵģ��ж��Ƿ��ѱ�ɾ����ɱ�������Ѿ�����ʵʱƾ֤
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
                  new String[] { String.valueOf(iRowNumber) })/* ��{0}�еķ�¼�����Ѿ���ɾ����ɱ����������ʵʱƾ֤���������޸� */);
          throw e;
        }
      } else if (bHeadAuditFlag.booleanValue()
          && (btvo.getStatus() == VOStatus.NEW)) {
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000009")/*
                                                                        * @res
                                                                        * "�˵����Ѿ��з�¼�����˳ɱ����㣬��������������"
                                                                        */);
        throw e;
      }
    }

    if (bHeadAuditFlag.booleanValue()) {
      /*@res �˵����Ѿ��з�¼�����˳ɱ������������ʵʱƾ֤���������޸ı�ͷ��Ϣ */
        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
            .getInstance().getStrByID("20143010", "UPP20143010-000199"));
        throw e;

//
//      boolean bChanged = false;
//      String sTemp1 = "";
//      String sTemp2 = "";
//
//      //�Ƚϵ��ݺ�
//      sTemp1 = oldbhvo.getVbillcode();
//      sTemp2 = newbhvo.getVbillcode();
//      if (sTemp1 != null || sTemp2 != null) {
//        if ((sTemp1 == null && sTemp2 != null)
//            || (sTemp1 != null && sTemp2 == null)) {
//          //��һ��
//          bChanged = true;
//        } else if (sTemp1.equals(sTemp2) == false) {
//          //��һ��
//          bChanged = true;
//        }
//      }
//
//      if (bChanged == false) {
//        //�Ƚ�����
//        sTemp1 = oldbhvo.getDbilldate().toString();
//        sTemp2 = newbhvo.getDbilldate().toString();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //�ȽϿ����֯
//        sTemp1 = oldbhvo.getCrdcenterid();
//        sTemp2 = newbhvo.getCrdcenterid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //�Ƚϲֿ�
//        sTemp1 = oldbhvo.getCwarehouseid();
//        sTemp2 = newbhvo.getCwarehouseid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //�Ƚ�ҵ������
//        sTemp1 = oldbhvo.getCbiztypeid();
//        sTemp2 = newbhvo.getCbiztypeid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //�Ƚ��շ����
//        sTemp1 = oldbhvo.getCdispatchid();
//        sTemp2 = newbhvo.getCdispatchid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //����
//        sTemp1 = oldbhvo.getCdeptid();
//        sTemp2 = newbhvo.getCdeptid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged == false) {
//        //ҵ��Ա
//        sTemp1 = oldbhvo.getCemployeeid();
//        sTemp2 = newbhvo.getCemployeeid();
//        if (sTemp1 != null || sTemp2 != null) {
//          if ((sTemp1 == null && sTemp2 != null)
//              || (sTemp1 != null && sTemp2 == null)) {
//            //��һ��
//            bChanged = true;
//          } else if (sTemp1.equals(sTemp2) == false) {
//            //��һ��
//            bChanged = true;
//          }
//        }
//      }
//
//      if (bChanged) {
//        //���ݺš������֯���ֿ⡢���ڡ�ҵ�����͡��շ���𡢲��š�ҵ��Ա
//        BusinessException e = new BusinessException(nc.bs.ml.NCLangResOnserver
//            .getInstance().getStrByID("20143010", "UPP20143010-000199")/* �˵����Ѿ��з�¼�����˳ɱ������������ʵʱƾ֤���������޸ı�ͷ��Ϣ */);
//        throw e;
//      }

     }

    return true;
  }

  /**
   * ��������:�ⲿϵͳɾ������
   * 
   * ����: String sSourceHID ----- ��Դϵͳ��������ID String sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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
        Log.info("û�з��������Ĵ�����㵥��");
        return;
      }

      //��õ�λ����
      String sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
      String sbilltype = ((BillHeaderVO) bvos[0].getParentVO()).getCbilltypecode();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
//		��������Ƿ�����
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); 
      if (bIAStart.booleanValue() == false) {
          //�������û������
          Log.info("�������û�����ã��������ݣ�����");
          return;
      }

      //�ж��Ƿ��ѹ���
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //ClientLink(String pk_corp, String user, UFDate date, String
      // accountYear, String accountMonth, UFDate yearMonth, UFDate monthStart,
      // UFDate monthEnd, String language, boolean isDebug, String moduleName,
      // String moduleCode, String moduleID)
      //�������ӻ���
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);


      //���ҵ������
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
      //��д�����ݹ����Ƿ񴫴�����
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
   * ��������:�ⲿϵͳ��ɾ������
   * 
   * ����: String sSourceHID ----- ��Դϵͳ��������ID String sCorpID ----- ��˾ID String
   * sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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
      //add by hanwei 2004-03-26 ʹ����ʱ����
      ArrayList alSourceBillID = new ArrayList();
      Hashtable htbSourceBillID = new Hashtable();
      String strSourceBillID = null;
      String sSubSql = null;
      TempTableDMO tempTableDMO = new TempTableDMO();

      //��ÿ�����ݷֱ��ж��Ƿ�ɼ��������㵥��
      //ͬʱ��ô��ID
      for (int i = 0; i < sSourceHIDs.length; i++) {
        strSourceBillID = sSourceHIDs[i];
        if ((strSourceBillID != null)
            && !htbSourceBillID.containsKey(strSourceBillID)) {
          alSourceBillID.add(strSourceBillID);
          htbSourceBillID.put(strSourceBillID, strSourceBillID);
        }
      }

      if (alSourceBillID.size() == 0) {
        //û��Ҫ����ĵ���
        return;
      }

      //***************************************************
      //add by hanwei 2004-03-26 ʹ����ʱ����
      //������ʱ��
      sSubSql = tempTableDMO.insertTempTable(alSourceBillID,
          nc.vo.ia.pub.TempTableVO.TEMPTABLE_SOURCE,
          nc.vo.ia.pub.TempTableVO.TEMPPKFIELD_SOURCE);
      //***************************************************

      String sSQL = " csourcebillid in " + sSubSql;

      BillVO[] bvos = this.queryByVOForOutter(null, true, sSQL);
      

      //zhwj ϵͳ���ݹ�ʱ ����ɾ���˷��ݹ����ɵ���������
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
      
      
      
      timer.addExecutePhase("��������ѯ����ϵͳ���ɵĴ������"/*notranslate*/);
      //û�д������ĵ��ݣ�ֱ�ӷ���
      if( bvos.length == 0 ){
        return ;
      }
      
      String sbilltype = ((BillHeaderVO) bvos[0].getParentVO()).getCbilltypecode();
      
      if( ConstVO.m_sBillCGRKD.equals(sbilltype) || ConstVO.m_sBillRKTZD.equals(sbilltype)){//�ɹ�ȡ������
	      this.deleteCheckForPU(bvos);
      }
      timer.addExecutePhase("���������ɹ�ȡ������ʱδ�����ý���"/*notranslate*/);
      
      this.deleteBillArrayForOutterBill(bvos, sCorpID, sUserID);
      timer.addExecutePhase("�������ɾ������"/*notranslate*/);

      BillDeleteFacade process = new BillDeleteFacade();
      process.process( bvos );
      //��д�����ݹ����Ƿ񴫴�����
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
      timer.addExecutePhase("��������д����ݹ����"/*notranslate*/);
      timer.showAllExecutePhase("�������deletebillfromoutterarray");
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );;
    }
  }

	/**
	 * ���ɹ���Ʊ��������˷��÷�̯������ȡ���ɹ���Ʊ����
	 * @param bvos
	 * @throws BusinessException
	 */
	private void deleteCheckForPU(BillVO[] bvos) throws Exception {
		//��ѯ���е��ӱ�ID���Ƿ������ĳ������������cadjustbillitemid�ֶ���
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
			// ������ʱ��
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
            "20143010", "UPP20143010-000298")/*@"����ȡ������Ľ��㵥���д����Ѿ�����"
						+ "���÷�̯���У�����ȡ���ɹ���Ʊ����"*/ );
			}
		}
	}

  public void deleteBillFromOutterArray_bill(String[] sSourceHIDs, String sCorpID,
	      String sUserID) throws BusinessException{
	  this.deleteBillFromOutterArray(sSourceHIDs, sCorpID, sUserID);
  }
  
  
  //private boolean deleteCheckForPU(){}
  
  /**
   * �Բ�ѯ������ϵͳ����ִ��ɾ���ͺ�������
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
        Log.info("û�з��������Ĵ�����㵥��");
        return;
      }
      CommonDataImpl cbo = new CommonDataImpl();
      //��õ�λ����
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); //��������Ƿ�����
      if (bIAStart.booleanValue() == false) {
          //�������û������
          Log.info("�������û�����ã��������ݣ�����");
          return;
      }
      //�ж��Ƿ��ѹ���
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //���ҵ������
      String[] sBizs = new String[2];
      sBizs[0] = ConstVO.m_sBizFQSK;
      sBizs[1] = ConstVO.m_sBizWTDX;

      java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
      String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
      String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

      //�̶��ʲ��Ƿ�����
      //bFAStart = cbo.isModuleStarted(sCorpID, ConstVO.m_sModuleCodeFA,
      //    ((BillHeaderVO) bvos[0].getParentVO()).getCaccountyear() + "-"
      //        + ((BillHeaderVO) bvos[0].getParentVO()).getCaccountmonth());

      for (int j = 0; j < bvos.length; j++) {
        //��Ҫ�ֱ���ÿ�ŵ��ݵ�ҵ��

        //����շ���־
        Integer iRDFlag = ((BillHeaderVO) bvos[j].getParentVO())
            .getFdispatchflag();
        int iRD = -1;

        boolean bIsOut = true;

        if (iRDFlag != null) {
          iRD = iRDFlag.intValue();
        }

        if (iRD == 0) {
          //����ⵥ
          bIsOut = false;
        }

        //������̶��ʲ��ӿ�//20050527_�رմ����̶��ʲ��ӿ�
        //                if (bFAStart.booleanValue()) {
        //                    doWithFa(bvos[j], null, bIsOut, ConstVO.DELETE_STATUS);
        //                }

        //������ⵥ���κŵķ���
        if (bIsOut) {
          this.doWithOutBatch(bvos[j], ConstVO.DELETE_STATUS);
        }
      }

      //ClientLink(String pk_corp, String user, UFDate date, String
      // accountYear, String accountMonth, UFDate yearMonth, UFDate
      // monthStart, UFDate monthEnd, String language, boolean isDebug,
      // String moduleName, String moduleCode, String moduleID)
      //�������ӻ���
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      //�����ݿ���ɾ������
      this.deleteArray(cl, bvos, sUserID);

      for (int j = 0; j < bvos.length; j++) {
        String sBillType = ((BillHeaderVO) bvos[j].getParentVO())
            .getCbilltypecode();
        String sBizTypeID = ((BillHeaderVO) bvos[j].getParentVO())
            .getCbiztypeid();

        //������Դ��Ʊ�����۳ɱ���ת��
        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //�����۳ɱ���ת��
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
                //��Դ�����۷�Ʊ
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
   * ���ܣ����ݴ�����ݵ�����ɾ����ϵͳ����ĵ���
   * 
   * @param headIDs
   *          String[] ����id����
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
        //û��Ҫ����ĵ���
        return;
      }

      TempTableDMO tempTableDMO = new TempTableDMO();
      //***************************************************
      //add by hanwei 2004-03-26 ʹ����ʱ����
      //������ʱ��
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
   * ��������:�ɹ��ݹ�ɾ������(ֻɾ���˱�����)
   * 
   * ����: String sSourceBID ----- ��Դϵͳ�����ӱ�ID String sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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

      //zhwj ϵͳ���ݹ�ʱ ����ɾ���˷��ݹ����ɵ���������
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
        Log.info("û�з��������Ĵ�����㵥��");
        return;
      }

      //��õ�λ����
      String sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      CommonDataImpl cbo = new CommonDataImpl();
      UFDate date = ((BillHeaderVO) bvos[0].getParentVO()).getDbilldate();
			String sPeriod = cbo.getPeriod(sCorpID, date.toString());
			//��������Ƿ�����
      UFBoolean bIAStart = cbo
          .isModuleStarted(sCorpID, ConstVO.m_sModuleCodeIA, sPeriod); 
      if (bIAStart.booleanValue() == false) {
          //�������û������
          Log.info("�������û�����ã��������ݣ�����");
          return;
      }
      //�ж��Ƿ��ѹ���
      BillTool.ensureAccountIsOpen(sCorpID, date);

      //�������ӻ���
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];

        if ((bvo.getChildrenVO() == null) || (bvo.getChildrenVO().length == 0)) {
          continue;
        }

        try {
          //����¼
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
                    "SCMCOMMON", "UPPSCMCommon-000316")/* @res "���ڽ�����ز��������Ժ�����" */);
            throw be;
          }

          boolean bIsUpdate = true;
          BillVO voCurBill = (BillVO) bvo.clone();

          for (int mm = 0; mm < bvo.getChildrenVO().length; mm++) {
            BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[mm];
            if (btvo.getCsourcebillitemid().equals(sSourceBID)) {
              //�����Ϊɾ��
              bvo.getChildrenVO()[mm].setStatus(nc.vo.pub.VOStatus.DELETED);

              if (bvo.getChildrenVO().length == 1) {
                //�˵���ֻ�д�һ�з�¼,ֱ��ɾ������
                bIsUpdate = false;
              }

              break;
            }
          }

          if (bIsUpdate) {
            //�޸Ĵ��з�¼״̬
            voCurBill = this.updateBill(cl, voCurBill, bvo, false, false, sUserID,
                false, cbo.getParaValue(((BillHeaderVO) bvo.getParentVO())
                    .getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB)));
          } else {
            //ֱ��ɾ���˵���
            //��
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
                                                                            * "�ͷ�ҵ��������"
                                                                            */);
          }
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    timer.addExecutePhase("�������ɾ���ɹ��ݹ����ɵĵ�����"/*notranslate*/);
    timer.showAllExecutePhase("������deleteBillItemForPU");
  }

  public void deleteBillItemForPU_bill(String sSourceBID, String sUserID)
  throws BusinessException {
	  this.deleteBillItemForPU(sSourceBID,sUserID);
  }
  /**
   * ��������:�ɹ��ݹ�ɾ������(ֻɾ���˱�����)
   * 
   * ����: String sSourceBID ----- ��Դϵͳ�����ӱ�ID String sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:������̶��ʲ��ӿ�
   * 
   * ����: BillVO bvo ----- ����VO BillVO bupdatevo ----- �޸ĺ��VO ɾ��ʱ�� boolean bIsOut
   * ----- �Ƿ��ǳ��ⵥ int iStatus ----- ���ӡ��޸Ļ���ɾ��
   * 
   * ����ֵ:
   * 
   * �쳣:
   * 
   * �������ڣ�(2002-10-9 14:33:26)
   */
  //20050527_�رմ����̶��ʲ��ӿ�
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
  //		//����
  //		Vector vTemp = new Vector(1,1);
  //		nc.vo.fa.outer.FaSubequipmentVO[] fvos = null;
  //
  //		if (bIsOut)
  //		{
  //			//���ⵥ
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
  //						//���Ǵ˵����еģ���������
  //						continue;
  //					}
  //
  //					//�����˴ε�
  //					nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //					fvo.setStatus(2); //״̬Ϊ����
  //					fvo.setPk_corp(btvo.getPk_corp());
  //					fvo.setPkinv(btvo.getCinventoryid());
  //					fvo.setEquip_code(btvo.getCinventorycode());
  //					fvo.setEquip_name(btvo.getCinventoryname());
  //					fvo.setSpec(btvo.getCinventoryspec());
  //					fvo.setUnit(btvo.getCinventorymeasname());
  //					fvo.setInstorage_flag(new Integer(0)); //δ���
  //					fvo.setAssetname(btvo.getCfadevicename()); //�ʲ�����
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
  //					//����ԭ����
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
  //			//��ⵥ�޸ı�־
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
  //				fvo.setStatus(nc.vo.pub.VOStatus.UPDATED); //״̬Ϊ�޸�
  //				fvo.setInstorage_flag(new Integer(1)); //�����
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
  //		//ɾ��
  //		Vector vTemp = new Vector(1,1);
  //		nc.vo.fa.outer. FaSubequipmentVO[] fvos = null;
  //
  //		if (bIsOut)
  //		{
  //			//���ⵥ
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
  //					//���Ǵ˵����еģ�����ɾ��
  //					continue;
  //				}
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo2 = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo2.setPk_billrow(btvo.getCbill_bid());
  //				fvo2.setStatus(3); //״̬Ϊɾ��
  //				vTemp.addElement(fvo2);
  //
  //				nc.vo.fa.outer.FaSubequipmentVO fvo = new
  // nc.vo.fa.outer.FaSubequipmentVO();
  //				fvo.setPk_subequipment(btvo.getCfadeviceid());
  //				fvo.setStatus(1); //״̬Ϊ�޸�
  //				vTemp.addElement(fvo);
  //			}
  //		}
  //		else
  //		{
  //			//��ⵥ�޸ı�־
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
  //				fvo.setStatus(1); //״̬Ϊ�޸�
  //				fvo.setInstorage_flag(new Integer(0)); //δ���
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
   * ��������:�������κ���ⵥ���ۼƷ�������
   * 
   * ����: BillVO bvo ----- Ҫ����ĵ���
   * 
   * ����ֵ:
   * 
   * �쳣:
   *  
   */
  private void doWithOutBatch(BillVO bvo, int iStatus) throws Exception {

    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

    if ((btvos != null) && (btvos.length != 0)) {
      //������ֵ��ݣ��ж��Ƿ������κ�
      for (int i = 0; i < btvos.length; i++) {
        BillItemVO btvo = btvos[i];

        String sVBatch = btvo.getVbatch();

        if ((sVBatch == null) || (sVBatch.toString().trim().length() == 0)) {
          //û�����κţ�������
          continue;
        }

        //�����κ�
        String sBill_bID = btvo.getCinbillitemid();
        UFDouble dNumber = btvo.getNnumber();

        if ((sBill_bID != null) && (sBill_bID.trim().length() != 0)) {
          if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
            //��������ӻ�ɾ����ֱ���޸ı��������ݵ�����
            double dNum = dNumber.doubleValue();

            String sOper = "+";

            if (iStatus == ConstVO.DELETE_STATUS) {
              //ɾ�����ݣ���ȥ���������ݵ��ۼƷ�������
              sOper = "-";
            }

            this.updateBatchNumber(sBill_bID, sOper, dNum);
          }
        }
      }
    }
  }

  /**
   * ��������:������ֵ����ۼƻس�����
   * 
   * ����: BillVO bvo ----- Ҫ����ĵ���
   * 
   * ����ֵ:
   * 
   * �쳣:
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
      //û��Ҫ�����ĵ���
      return;
    }

    //�Ϸ��Լ��
    if (iStatus == ConstVO.ADD_STATUS) {
      //��������ӣ��ж������Ƿ񹻻س��
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

        //sRows = sRows + "��" + sResult[i][0] + "�еĴ����ǰ��������س�����Ϊ" +
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
                "UPP20143010-000002")/* @res "�����" */);
        throw e;
      }
    }

    String sOper = "+";

    if (iStatus == ConstVO.DELETE_STATUS) {
      //ɾ����嵥���ݣ���ȥ���������ݵ��ۼƻس�����
      sOper = "-";
    }

    //update ia_bill_b set ia_bill_b.nsettledretractnum =
    // isnull(ia_bill_b.nsettledretractnum,0) +10
    //from ia_bill_b,v_ia_inoutledger b where ia_bill_b.cbill_bid =
    // b.cadjustbillitemid and b.cbillid = '1' and b.nnumber < 0 and
    // ia_bill_b.dr = 0

    //�޸Ķ�Ӧ���ݵ��ۼƻس�����
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
   * ��������:���ģ��ɱ�
   * 
   * ����: BillVO bvo ----- ����VO(��ģ��ɱ�) int iStatus ----- ״̬ String sExistString
   * ----- �Ӳ�ѯ��䣨�������Ч�ʣ� String sPeriod ----- ��ǰ����ڼ� String sFQSK ----- �����տ�
   * String sWTDX ----- ί�д��� String sPeriodPerv ----- ǰһ���� String sBeginDate
   * ----- ���µ�һ�� String sEndDate ----- �������һ�� Hashtable htt ----- �����֯��Ϣ
   * Hashtable htInv ----- �����Ϣ
   * 
   * ����ֵ:����VO(��ģ��ɱ�)
   * 
   * �쳣:
   * 
   *  
   */
  private BillVO[] doWithSimulateCost(SimulateVO svo, int iStatus)
      throws Exception {
    BillVO[] bvos = svo.getBills();

    if (iStatus == ConstVO.UPDATE_STATUS) {
      //�޸ģ������д��ģ��ɱ�
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

        //�޸ģ������д��ģ��ɱ�
        for (int j = 0; j < btvos.length; j++) {
          UFDouble dMny = btvos[j].getNmoney();
          if (dMny != null) {
            btvos[j].setNsimulatemny(dMny);
          }
        }
      }
    } else if (iStatus == ConstVO.ADD_STATUS) {
      //������״̬
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
        String sBillType = bhvo.getCbilltypecode();
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

        if (sBillType.equals(ConstVO.m_sBillJHJTZD)
            || sBillType.equals(ConstVO.m_sBillDJTQD)
            || sBillType.equals(ConstVO.m_sBillCYLJZD)) {
          //�ƻ��۵����������ⵥ��
          //�����д��ģ��ɱ�
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
   * ��������:��������㵥��VO�üƻ���д��ʵ�ʼۣ�ΪSEG�ṩ��
   * 
   * ����:
   * 
   * ����ֵ:
   * 
   * �쳣:
   * 
   * �������ڣ�(2002-12-10 9:01:17)
   */
  public BillVO fillVOByPlaned(BillVO bvo) throws BusinessException {
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //�����������
      BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
      BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();

      Integer[] iPeci = cbo.getDataPrecision(bhvo.getPk_corp());

      //�õ�����Ϊ����Ҫ��һ����ѯ�ƻ��۵Ĵ��
      //Hashtable(key:�������+����VO�����±꣬value:����VO�����е�ֵ)
      Hashtable htInvID = new Hashtable();
      for (int i = 0; i < btvos.length; i++) {
        if (btvos[i].getNprice() == null) {
          htInvID.put(btvos[i].getCinventoryid() + "," + i, btvos[i]);
        }
      }
      if (htInvID.size() <= 0) {
        Log.info("û�е���Ϊ�յĴ����");
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
      //��ѯ�����Ϣ������ֵ:������Ƽ۷�ʽ���ƻ��ۡ��Ƿ����κ���
      String[][] sResults = cbo.getPrices(bhvo.getPk_corp(), bhvo
          .getCrdcenterid(), s);

      //�üƻ���д��ʵ�ʼ�
      if ((sResults != null) && (sResults.length > 0)) {
        UFDouble dPlanedPrice = null;
        UFDouble dNumber = null;
        UFDouble dPlanedMny = null;
        BillItemVO btvo = null;
        for (int i = 0; i < sResults.length; i++) {
          //��üƼ۷�ʽ����

          //if (iPriceCode.intValue() == ConstVO.JHJ) //���������Ǽƻ��۵�
          {
            //�Ǽƻ��ۼƼۣ��Ѽƻ���д��ʵ�ʵ���,���Ѽƻ����д��ʵ�ʽ��
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
   * ��������:
   * 
   * ����:
   * 
   * ����ֵ:
   * 
   * �쳣:
   * 
   * �������ڣ�(2002-10-30 16:41:55)
   * 
   * @return nc.vo.ia.bill.BillItemVO
   * @param btvo
   *          nc.vo.ia.bill.BillItemVO
   */
  //20050527_�رմ����̶��ʲ��ӿ�
  //private BillItemVO getFaCardInfo(BillItemVO btvo) throws Exception
  //{
  //	//��ù̶��ʲ�����
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
   * ��������:��������ڳ�����
   * 
   * ����: String sCorpID ----- ��λ���� nc.vo.pub.lang.UFDate dDate ----- ��ֹ����
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:�����ݿ��в���һ��VO����
   * 
   * ����: BillVO[] bills ----- ����
   * 
   * ����ֵ:��������
   * 
   * �쳣:BusinessException
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
      //�������ӻ���
      ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null, null,
          null, null, null, false, null, null, null);

      return this.insertArray(cl, bills);

    }
    return null;

  }

  /**
   * ��ȡ������
   * 
   * @param bills
   * @return
   */
  private BillVO[] setFreeItems(BillVO[] bills) throws Exception {
    if (bills != null) {
      //��������������
      ArrayList al = new ArrayList();

      for (int i = 0; i < bills.length; i++) {
        ArrayList tempal = new ArrayList();
        BillItemVO[] items = (BillItemVO[]) bills[i].getChildrenVO();
        int iLen = items != null ? items.length : 0;
        for (int j = 0; j < iLen; j++) {
          BillItemVO btvo = items[j];

          //������̶��ʲ��ӿ�//20050527_�رմ����̶��ʲ��ӿ�
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
   * ��������:��õ��ݣ������������
   * 
   * ����: String[] sTable ----- Ҫ���ӵı� String[] sConnectParam ----- �������� String[]
   * sCondition ----- �������� BillVO condBillVO ------ ��������VO String sOrderPart
   * ------ �����ֶ� BillVO bResultVO ----- �Ѳ�ѯ����VO Boolean bHasInv -----
   * �Ƿ�Ҫ��ѯ������ı������Ϣ
   * 
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:����VO�����趨�������������з���������VO����
   * 
   * ����: BillItemVO condBillVO ----- VO���� boolean isAnd ----- �Ƿ���������� String
   * sOrder ----- ��������
   * 
   * ����ֵ:������ֵ����
   * 
   * �쳣:BusinessException
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
   * ���ݱ�ͷ������ѯ����ĳЩ�ֶ�,�˷���ֻ�޲�ѯ�����ֶ�ʱʹ�ã���ѯ���������ֶ�����������
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
   * ��������:Ϊ��ϵͳ��ѯ����
   * 
   * ����: String sSourceHID ----- ��Դ����ID boolean bIsHeader ----- �Ƿ��Ǳ�ͷ String
   * sSQL ----- �Զ����ѯ���
   * 
   * ����ֵ:������ֵ����
   * 
   * �쳣:Exception
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
      //�Ǳ���ID
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
    //�������ӻ���
    ClientLink cl = new ClientLink(null, null, null, null, null, null, null,
        null, null, false, null, null, null);

    bill = dmo.queryByVO(conVO, null, "", cl);
    //	}
    //	else
    //	{
    //		bill = dmo.queryWithOtherTableHaveInv(null,null,null,conVO,null);
    //	}

    //��ѯ��ϵͳ����ʱ������ͷts�ÿգ��þɵļ�鷽��
    if (bill != null) {
      for (int i = 0; i < bill.length; i++) {
        ((BillHeaderVO) bill[i].getParentVO()).setTs(null);
      }
    }

    return bill;
  }

  /**
   * ��������:����VO�����趨�������������з���������VO����
   * 
   * ����: BillItemVO condBillVO ----- VO���� boolean isAnd ----- �Ƿ���������� String
   * tempTableName ----- ��ʱ����
   * 
   * ����ֵ:������ֵ����
   * 
   * �쳣:BusinessException
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
   * ��������:��õ��ݣ������������
   * 
   * ����: String[] sTable ----- Ҫ���ӵı� String[] sConnectParam ----- �������� String[]
   * sCondition ----- �������� BillVO condBillVO ------ ��������VO String sOrderPart
   * ------ �����ֶ�
   * 
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ�����㣬 ����ƽ̨ת����VO,�����Լ��������������ת����
   * 
   * ����: AggregatedValueObject preVo ----- ��ԴVO AggregatedValueObject nowVo
   * ----- Ŀ��VO
   * 
   * ����ֵ:
   * 
   * �쳣:
   *  
   */
  public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
      AggregatedValueObject nowVo) throws BusinessException {
    //��¼ʱ�仨��
    Timer timer = new Timer();
    timer.start();
    timer.addExecutePhase("������   VO���������ദ��"/*notranslate*/);
    try {
      if ((preVo == null) || (nowVo == null)) {
        return nowVo;
      }
      if (nowVo instanceof BillVO) {
        //�Ǵ������ĵ���VO
        BillHeaderVO bhvo = (BillHeaderVO) nowVo.getParentVO();
        BillItemVO[] btvos = (BillItemVO[]) nowVo.getChildrenVO();
        //��Բ�ͬ��Դ��ת��
        if (preVo instanceof nc.vo.ic.pub.bill.GeneralBillVO) {
          this.changeBusiVOForICNew(preVo, bhvo, btvos);
        } else if (preVo instanceof nc.vo.so.so012.SquareVO) {
          this.changeBusiVOForSONew(preVo, bhvo, btvos);
        } else if (preVo instanceof nc.vo.to.to303.SFinanceVO) {
          //this.changeBusiVOForTO(preVo, bhvo, btvos);
        }
      }
      timer.showAllExecutePhase("�����������ࣺ");
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }
    return nowVo;
  }
  
  /**
   * ��������:���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ�����㣬 ����ƽ̨ת����VO,�����Լ��������������ת����
   * 
   * ����: AggregatedValueObject preVo ----- ��ԴVO AggregatedValueObject nowVo
   * ----- Ŀ��VO
   * 
   * ����ֵ:
   * 
   * �쳣:
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
	 * ���ܣ��Դ����۴��ݹ�����VOִ��ת��
	 * @param preVo ת��ǰ�֣ϣ���ϵͳ�֣ϣ�
	 * @param bhvo��ת�����ͷ�֣�
	 * @param btvos��ת�������֣�
	 * @throws BusinessException��
	 */
	private void changeBusiVOForSONew(AggregatedValueObject preVo, 
			BillHeaderVO bhvo, BillItemVO[] btvos) throws BusinessException {
		// �����۹�����ĵ���
		SquareVO svo = (SquareVO) preVo;
		SquareHeaderVO shvo = (SquareHeaderVO) svo.getParentVO();
		SquareItemVO[] stvos = (SquareItemVO[]) svo.getChildrenVO();
		CommonDataImpl cbo = new CommonDataImpl();
		// ���ݾ���
		Integer[] iPrecision = cbo.getDataPrecision(bhvo.getPk_corp());
		// ������۵�������
		String sCorpID = shvo.getPk_corp();
		String sBillType = shvo.getCreceipttype(); // ��������
		String sBillID = shvo.getCsaleid(); // ����ID

		if ((sBillType == null) || (sBillType.trim().length() == 0)
				|| (sBillID == null) || (sBillID.trim().length() == 0)) {
			BusinessException be = new BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
							"20143010", "UPP20143010-000012")/*"û����Դ���ݱ�ͷ��Ϣ�������"*/);
			throw be;
		}

		// ���ҵ������
		String sBizTypeID = bhvo.getCbiztypeid();

		// ��÷����տί�д�����ֱ�˵�ҵ������
		String[] sBizs = new String[3];
		sBizs[0] = ConstVO.m_sBizFQSK;
		sBizs[1] = ConstVO.m_sBizWTDX;
		sBizs[2] = ConstVO.m_sBizZY;

		java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
		String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
		String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);
		String sZY = (String) ht.get(ConstVO.m_sBizZY);

		// �������
		for (int i = 0; i < btvos.length; i++) {

			if ((stvos[i].getCorder_bid() == null)
					|| (stvos[i].getCorder_bid().trim().length() == 0)) {
				BusinessException be = new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"20143010", "UPP20143010-000013")/*"û����Դ���ݱ�����Ϣ�������"*/);
				throw be;
			}

			if (sBillType.equals(ConstVO.m_sBillXSFP)) {
				// �����۷�Ʊ
				if ((sFQSK.indexOf(sBizTypeID) != -1)
						|| (sWTDX.indexOf(sBizTypeID) != -1)) {
					// �Ƿ����տί�д���ҵ����ԴΪ��Ʊ
					String sSaleBillID = stvos[i].getCupbillid(); // ����ID
					String sSaleBodyID = stvos[i].getCupbillbodyid();

					if ((sSaleBillID == null)
							|| (sSaleBillID.trim().length() == 0)
							|| (sSaleBodyID == null)
							|| (sSaleBodyID.trim().length() == 0)) {
						BusinessException be = new BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("20143010",
												"UPP20143010-000014")/*"�˵����Ƿ�Ʊ����û��ָ����Ӧ�����۳��ⵥ�������"*/);
						throw be;
					}

					// ��Դ�����۵����۳��ⵥ
					btvos[i].setCsaleadviceid(sSaleBillID);
					btvos[i].setCcsaleadviceitemid(sSaleBodyID);
				}
			} else if (sBillType.equals(ConstVO.m_sBillXSCKD)) {
				// �����۳��ⵥ
				btvos[i].setCsaleadviceid(stvos[i].getCsaleid());
				btvos[i].setCcsaleadviceitemid(stvos[i].getCorder_bid());

				// �ж�����
				if ((sBizTypeID != null) && (sBizTypeID.trim().length() != 0)) {
					if ((sFQSK.indexOf(sBizTypeID) != -1)
							|| (sWTDX.indexOf(sBizTypeID) != -1)) {
						// �Ƿ����տί�д���ҵ����ԴΪ���ⵥ
						if ((stvos[i].getNoutnum() != null)
								&& (stvos[i].getNnewbalancenum() != null)
								&& (stvos[i].getNoutnum().doubleValue() != stvos[i]
										.getNnewbalancenum().doubleValue())) {
							// ���ǶԳ��ⵥһ�ν���
							BusinessException be = new BusinessException(
									nc.bs.ml.NCLangResOnserver.getInstance()
											.getStrByID("20143010",
													"UPP20143010-000015")/*"�˵����ǳ��ⵥ�����Ƿ����տ��ί�д���ҵ�񣬵�û�н��������������������"*/);
							throw be;
						}
					}
				}
			}

			// ����Դͷ������Ϣ
			if (stvos[i].getCreceipttype() != null) {
				// ����û��Դͷ�����ݵ���Դ�Ǵ�����㵥�ݵ�Դͷ
				btvos[i].setCfirstbilltypecode(stvos[i].getCreceipttype());
				btvos[i].setCfirstbillid(stvos[i].getCsourcebillid());
				btvos[i].setCfirstbillitemid(stvos[i].getCsourcebillbodyid());
			} else if (stvos[i].getCupreceipttype() != null) {
				// û��Դͷ�����ݵ���Դ�Ǵ�����㵥�ݵ�Դͷ
				btvos[i].setCfirstbilltypecode(stvos[i].getCupreceipttype());
				btvos[i].setCfirstbillid(stvos[i].getCupbillid());
				btvos[i].setCfirstbillitemid(stvos[i].getCupbillbodyid());
			} else {
				// ����û��Դͷ�����ݵ���Դ�Ǵ�����㵥�ݵ�Դͷ
				btvos[i].setCfirstbilltypecode(null);
				btvos[i].setCfirstbillid(null);
				btvos[i].setCfirstbillitemid(null);
			}

			// �Ƿ���ֱ�˲ɹ�����
			if ((sBizTypeID != null) && (sBizTypeID.trim().length() != 0)
					&& (sZY.indexOf(sBizTypeID) != -1)) {
				// ��ֱ�˲ɹ����ۣ������д��ɱ�
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
			} else {// �������ֱ�˲ɹ����ۣ��򴫹����������ۼ۸񣬴���۸���Ҫ���¼���
				// ���ۡ����Ϊ��
				btvos[i].setNprice(null);
				btvos[i].setNmoney(null);
			}

		}
	}
	
  /**
	 * ���ܣ��Դӿ�洫�ݹ�����VOִ��ת��
	 * @param preVo ת��ǰ�֣ϣ���ϵͳ�֣ϣ�
	 * @param bhvo��ת�����ͷ�֣�
	 * @param btvos��ת�������֣�
	 * @throws BusinessException��
	 */
	private void changeBusiVOForICNew(AggregatedValueObject preVo, 
			BillHeaderVO bhvo, BillItemVO[] btvos) throws BusinessException {
    // �ǿ�������ĵ���
    GeneralBillVO gvo = (GeneralBillVO) preVo;
    GeneralBillHeaderVO ghvo = (GeneralBillHeaderVO) gvo.getParentVO();
    GeneralBillItemVO[] gtvos = (GeneralBillItemVO[]) gvo.getChildrenVO();

    String sBillType = ghvo.getCbilltypecode(); // ��������

    if ((sBillType == null) || (sBillType.trim().length() == 0)) {
      BusinessException be = new BusinessException(nc.bs.ml.NCLangResOnserver
          .getInstance().getStrByID("20143010", "UPP20143010-000011")/* "û����Դ�������ͣ������" */);
      throw be;
    }

    if (sBillType.equals(ConstVO.m_sBillKCQTRKD)
        || sBillType.equals(ConstVO.m_sBillKCQTCKD)) {
      // �ǿ��� ������ⵥ �� �������ⵥ
      String sSourceBillType = gtvos[0].getCsourcetype();
      if ((sSourceBillType != null)
          && (sSourceBillType.equals(ConstVO.m_sBillKCZZD) 
              || sSourceBillType.equals(ConstVO.m_sBillKCCXD))) {
        // ��Դ����װ���ж��
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
      // ����Դͷ������Ϣ
      if ((gtvos[i].getCfirsttype() != null)
          && (!bhvo.getCbilltypecode().equals(ConstVO.m_sBillDBRKD)
              || gtvos[i].getCfirsttype().equals(ConstVO.m_sBillSFDBDD) || gtvos[i]
              .getCfirsttype().equals(ConstVO.m_sBillGSJDBDD))) {
        // ��浥�ݵ�Դͷ�Ǵ�����㵥�ݵ�Դͷ
        btvos[i].setCfirstbilltypecode(gtvos[i].getCfirsttype());
        btvos[i].setCfirstbillid(gtvos[i].getCfirstbillhid());
        btvos[i].setCfirstbillitemid(gtvos[i].getCfirstbillbid());
        btvos[i].setVfirstbillcode(gtvos[i].getVfirstbillcode());
      }
      else if (gtvos[i].getCsourcetype() != null) {
        // ���û��Դͷ����浥�ݵ���Դ�Ǵ�����㵥�ݵ�Դͷ
        // ����Դ���������͹�˾��ĵ�����ⵥ��Դͷ��Ϊ������ⵥ����Դ�����ڳɱ����㴦��
        btvos[i].setCfirstbilltypecode(gtvos[i].getCsourcetype());
        btvos[i].setCfirstbillid(gtvos[i].getCsourcebillhid());
        btvos[i].setCfirstbillitemid(gtvos[i].getCsourcebillbid());
        btvos[i].setVfirstbillcode(gtvos[i].getVsourcebillcode());
      }
    }
  }


  
	/**
	 * ��ѯ�ִ������֯��Ӧ�ĳɱ������֯������鵥���еĳ����ֿ��Ƿ�����ͬһ�ɱ������֯
	 * ������������㲻���ոõ��� 
	 * @param bvos
	 */
  private BillVO[] changeAndCheckCalbodys(BillVO[] bvos, String sSourceModule,String sCorpID) throws BusinessException {
  	if( (bvos == null) || (bvos.length == 0) ){
  		return new BillVO[0];
  	}
  	//��֯���ݣ���ͬһ��˾�Ŀ����֯+�ֿ���֯��һ��
  	Map mCorp2CalBodyWareHouses = new HashMap();
    CommonDataImpl cbo = new CommonDataImpl();
  	for( int i = 0; i < bvos.length; i ++){
  		/*  ������˾�����֯ +���ֿ�*/
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
      /* ����Է���˾�����֯ + �ֿ�*/
			String[] otherBodyHouse = new String[2];
			String othercorp = bhvo.getCothercorpid();
      if ((othercorp != null) && (bhvo.getCothercalbodyid() != null)){
				otherBodyHouse[0] = bhvo.getCothercalbodyid();
				otherBodyHouse[1] = bhvo.getCotherwarehouseid();
			}else{//zlq edit ����п���û���������������֯��������˾
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
  	
    //ת�������֯,���ִ������֯תΪ�ɱ������֯
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
  		//��ȡ�ɱ������֯
			String[] sCostBodys = dmo4.chgCalbodyICToIA(sCorps[i],
					(ArrayList)mCorp2CalBodyWareHouses.get(sCorps[i]));
			//�Թ�˾+�����֯+�ֿ�Ϊ�����Գɱ������֯Ϊֵ������
			if (sCostBodys != null){
        for (int j=0;j<sCostBodys.length;j++){
          String[] sBodyHouses = (String[])((ArrayList)mCorp2CalBodyWareHouses
          		.get(sCorps[i])).get(j);
          mCorpCalBodyWareHouse2CostBody.put(sCorps[i] + "," 
          		+ sBodyHouses[0] + "," + sBodyHouses[1],sCostBodys[j]);
        }
      }
  	}
  	
    //�ж��Ƿ���ͬһ�ɱ�������֯���ת��
    ArrayList alInsertBills = new ArrayList();
    for (int i = 0; i < bvos.length; i++) {
			BillHeaderVO bhvo = (BillHeaderVO) bvos[i].getParentVO();
			String corp = bhvo.getPk_corp();
			String othercorp = bhvo.getCothercorpid();
			String bodykey = corp + "," + bhvo.getCstockrdcenterid() + ","
					+ bhvo.getCwarehouseid();
			String costbody = (String) mCorpCalBodyWareHouse2CostBody.get(bodykey);
			//IA0033:���ͬһ�ɱ�������ֿⵥ���Ƿ���������
			Hashtable ht = cbo.getParaValues(corp, new String[]{"IA0033"});
			String value = ht.get("IA0033") != null ? ((String)ht.get("IA0033")).trim() : "";
			if( value.equalsIgnoreCase("N")){
				//��Դ�ǿ���ҶԷ���˾��Ϊ���ұ���˾�����ڶԷ���˾������Ž��бȽ�
				if (sSourceModule.equals(ConstVO.m_sModuleIC) && (othercorp != null)
						&& corp.equals(othercorp)) {
					String otherbodykey = othercorp + "," + bhvo.getCothercalbodyid() + ","
							+ bhvo.getCotherwarehouseid();
					String othercostbody = (String) mCorpCalBodyWareHouse2CostBody
							.get(otherbodykey);
					if (costbody.equals(othercostbody)) {
						// ��ͬһ�������֯��ת����������㲻����
						Log.info("------------IASaveBill------------���ݳ���ɱ�������֯��ͬһ�������֯��������");
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
      //���е���ͬһ�ɱ���
      Log.info("���е��ݵĳ����ɱ�������֯��ͬ���������δ����");
    }
  	return binsrtedVOs;
  	 
  }

  /**
   * ��������:�ⲿϵͳһ�����浥�ݣ�Ϊ�����ǩ����
   * 
   * ����: BillVO[] bvos ----- ����VO���� sSourceModule ----- ��Դģ�� String
   * sSourceBillType ----- ��Դ��������
   * 
   * ����ֵ:
   * 
   * �쳣:
   *  
   */
  public HashMap saveBillFromOutterArray(AggregatedValueObject[] bills,
      String sSourceModule, String sSourceBillType) throws BusinessException {
	 Log.debug("��Ʊ���ʱִ�д˷���ִ��saveBillFromOutterArray"); 
    HashMap hmSourceBodyId2IaHeadBodyId = new HashMap();
    /*if (!(bills instanceof BillVO[])) {//��巢Ʊ��˲��������ж�
      System.out.println("saveBillFromOutterArray������ArregatedValueObject[]ӦΪ����BillVO[]");
      throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
          .getStrByID("20143010", "UPP20143010-000016") @res "���������鿴��־" );
    }        */
    //����ǰ����Դ�Ե���Ԥ����
    IBillBusinessProcesser processer = ProcesserBuilder.build(sSourceModule);
    if( processer != null){
      processer.preSaveProcess((BillVO[])bills,null);
    }
    //���м��вɹ�����ش���
    ProcessJICAI jicai = new ProcessJICAI(); 
    ArrayList list = jicai.execute(bills, sSourceModule);
    System.out.println("list�ĳ���Ϊ��"+list.size());
    for (int l = 0; l < list.size(); l++) {
      //��¼ʱ�仨��
      Timer timer = new Timer();
      timer.start();
			BillVO[] bvos = (BillVO[]) list.get(l);
			boolean bAccountLocked = false; 
			String sUserID = "";
			try {
				// 1�������ж�
				// ��������
				if ((bvos == null) || (bvos.length == 0)) {
					continue;
				}
				// ��Դģ��(���ڶԳ嵥��˵������۷�Ʊ����ִ�������if�ж�)
			/*	if ((sSourceModule == null) || (sSourceModule.trim().length() == 0)) {
					BusinessException e = new BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
									"UPP20143010-000017")
																				 * @res "��Դģ�鲻��Ϊ�գ������"
																				 );
					throw e;
				}*/
				// �õ�һ�����ݵ�ͷVO��ù�˾�����ڵ�
				BillHeaderVO curbhvo = (BillHeaderVO) bvos[0].getParentVO();
				String sCorpID = curbhvo.getPk_corp();
				sUserID = curbhvo.getCoperatorid();//0001A2100000000F7X61
				String sDate = curbhvo.getDbilldate().toString();
				curbhvo.setVbillcode(null);
				// �������ӻ���
				ClientLink cl = new ClientLink(sCorpID, sUserID, null, null, null,
						null, null, null, null, false, null, null, null);
				AccountImpl abo = new AccountImpl();
				CommonDataImpl cbo = new CommonDataImpl();
				// ������ĩ���ʺ��ճ�¼�뵥��֮��Ĳ�������
				bAccountLocked = this.lockAccount((BillVO) bvos[0]);//false
				// ��õ�ǰ�Ļ���ڼ�
				String sPeriod = cbo.getPeriod(sCorpID, sDate);//2019-07
				// �Ե��ݽ����ж�
				// ��Դϵͳ�Ƿ���ȷ�������Դ���ĵ��ݽ����²�����
				String[] sModuleCode = new String[5];
				sModuleCode[0] = ConstVO.m_sModuleCodeSO;
				sModuleCode[1] = ConstVO.m_sModuleCodePO;
				sModuleCode[2] = ConstVO.m_sModuleCodeSC;
				sModuleCode[3] = ConstVO.m_sModuleCodeIA;
				sModuleCode[4] = ConstVO.m_sModuleCodeTO;//[4006, 4004, 4012, 2014, 4009]
				Hashtable httt = cbo
						.isModuleArrayStarted(sCorpID, sModuleCode, sPeriod);
				UFBoolean bSOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeSO); // �����Ƿ����ã�Y
				UFBoolean bPOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodePO); // �ɹ��Ƿ����ã�Y
				UFBoolean bSCStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeSC); // ί��ӹ��Ƿ����ã�Y
				UFBoolean bIAStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeIA); // ��������Ƿ����ã�Y
				UFBoolean bTOStart = (UFBoolean) httt.get(ConstVO.m_sModuleCodeTO); // �ڲ������Ƿ����ã�Y
				//bIAStartΪY�����������if�ж�
				/*if (bIAStart.booleanValue() == false) {
					// �������û������
					System.out.println("------------IASaveBill------------�������û�����ã������յ��ݣ�����");
					return null;
				}*/
				// ������Ƿ����ظ�����
				this.checkICDupBills((BillVO[]) bvos, sSourceModule, sSourceBillType);//sSourceModule='so'��sSourceBillType='32'
				// ���ҵ������
				String[] sBizs = new String[2];//[FQSK, WTDX]
				sBizs[0] = ConstVO.m_sBizFQSK;//�����տ�
				sBizs[1] = ConstVO.m_sBizWTDX;//ί�д���
				java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);//{WTDX='110711100000000000SI','110711100000000000SE','0001AA000000000003H9', FQSK='0001AA000000000003HA'}
				String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);//'0001AA000000000003HA'
				String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);//'110711100000000000SI','110711100000000000SE','0001AA000000000003H9'
				// ������ݾ���
				Integer[] iPeci = cbo.getDataPrecision(sCorpID);//[8, 8, 2, 6, 4, 2]
				// �Ƿ��������ɱ�
				String sSimulate = cbo.getParaValue(sCorpID, new Integer(
						ConstVO.m_iPara_SFJSXNCB));//sSimulate="Y"
				SimulateVO svo = new SimulateVO();
				svo.setFQSK(sFQSK);
				svo.setWTDX(sWTDX);
				svo.setPeci(iPeci);
				Vector vBills = new Vector(1, 1); // ��¼Ҫ����ĵ���
				Vector vAuditBills = new Vector(1, 1);
				// ArrayList alCalbodys = new ArrayList();
				// ArrayList alOtherCalbodys = new ArrayList();
				// //��¼��һ���ĳɱ������֯�����Ƿ���һ�µģ����һ�¾Ͳ�����������
				// ��ÿ�����ݷֱ��ж��Ƿ�ɼ��������㵥��
				// ͬʱ��ô��ID
				System.out.println("bvos�ĳ���Ϊ��"+bvos.length);
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
						// ������������ɱ�
						continue;
					}
					String sBillTypeCode = bhvo.getCbilltypecode();//I5
					String sBizTypeID = bhvo.getCbiztypeid();
					String sFirstBillTypeCode = btvo.getCfirstbilltypecode();
					if (bPOStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillCGRKD)
							&& (sSourceModule.equals(ConstVO.m_sModulePO) == false)) {//�����˲���һ��if�ж�
						// �ɹ����ã����ǲɹ������
						if ((bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()) {
							// ��ⵥ�������ֿ���VMI����
							Log.info("-----IASaveBill------��ⵥ�������ֿ���VMI���㣬������");
							continue;
						}
						BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
						Vector vBtvo = new Vector(1, 1);
						UFBoolean ufbLargess = null;
						for (int ii = 0; ii < btvos.length; ii++) {
							// �Ƿ���Ʒ:�������Ʒ��������Ϊ0��ֻ������������յ��ۺͽ��
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
							Log.info("--IASaveBill--�ɹ�ϵͳ���ã��˵��ݲ��ǲɹ�����ģ��Ҳ�������Ʒ������");
							continue;
						}
					} else if (bSCStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillWWJGSHD)
							&& (sSourceModule.equals(ConstVO.m_sModulePO) == false)) {//�����˲������ж�
						// ί��ӹ����ã����ǲɹ������
						Log.info("---IASaveBill--ί��ӹ�ϵͳ�Ѿ����ã������ݲ��ǲɹ�����ģ�����");
						continue;
					} else if (bSOStart.booleanValue()
							&& sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)
							&& (sSourceModule.equals(ConstVO.m_sModuleSO) == false)) {//�����˲������ж�
						
						String U8BillType = (String)btvo.getAttributeValue("vbilltypeu8rm");
						
						if (U8BillType==null || U8BillType.length()==0){
						// �������ã��������۴����
							Log.info("----IASaveBill-----����ϵͳ�Ѿ����ã������ݲ������۴���ģ��ֲ�����Դ��U8���۽ӿڣ�����");
							continue;
						}
					} else if (bTOStart.booleanValue()
							&& (sSourceModule.equals(ConstVO.m_sModuleTO) == false)
							&& (sBillTypeCode.equals(ConstVO.m_sBillDBRKD) || sBillTypeCode
									.equals(ConstVO.m_sBillDBCKD))
							&& (sFirstBillTypeCode.equals(ConstVO.m_sBillSFDBDD) || sFirstBillTypeCode
									.equals(ConstVO.m_sBillGSJDBDD))
							&& !"4453".equals(btvo.getCsourcebilltypecode())/* ������Դ��;�� */) {
						// ���ǩ�ִ���ĵ���������Դ���������������͹�˾���������,�Ҳ�����Դ��;��
						if (sBillTypeCode.equals(ConstVO.m_sBillDBRKD)) {// ������ⵥ���
							Object oTOTrans = NCLocator.getInstance().lookup(
									ITransferBill.class.getName());
							Object oTOPrice = NCLocator.getInstance().lookup(
									IOuter.class.getName());
							BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
							ArrayList alIANormalVOs = new ArrayList();// ׼�����յ�VO
							ArrayList alIALargessVOs = new ArrayList();// ������յ���Ʒ
							ArrayList alICVOs = new ArrayList();// ��д����VO
							String icprice = "nzgprice1";
							String icmny = "nzgmny1";
							UFBoolean ufbLargess = null;
							for (int ii = 0; ii < btvos.length; ii++) {// ��Ʒ�����ղ��õ���Ϊ�㣬����д���
								ufbLargess = btvos[ii].getBlargessflag();
								if ((ufbLargess != null) && ufbLargess.booleanValue()) {
									btvos[ii].setNmoney(new UFDouble(0));
									btvos[ii].setNprice(new UFDouble(0));
									alIALargessVOs.add(btvos[ii]);
								} else {// ����Ʒ��Ҫ��ѯ������ϵ�в����Ƿ��ݹ����ݹ��Ľ��գ����ݹ��Ĳ�����
									String orderID = btvos[ii].getCfirstbillitemid();
									if ((orderID == null) || (orderID.length() == 0)) {
										throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
												"20143010", "UPP20143010-000299") /*@"�쳣���󣺵���������ʶΪ��"*/);
									}
									String[] sIds = new String[] { orderID };
									AggregatedTransrelaVO[] relaVOs = ((ITransferBill) oTOTrans)
											.getTransInfoFromOrder(sIds);
									if ((relaVOs != null) && (relaVOs.length != 0)) {
										UFBoolean b = ((TransrelaItemVO) relaVOs[0].getChildrenVO()[0])
												.getBestimate();
										if ((b != null) && b.booleanValue()) {// �ݹ���
											// ȡ�ݹ��۸�
											UFDouble[] prices = ((IOuter) oTOPrice)
													.getEstimatePriceForIA(sIds);
											//��������
                      UFDouble price = context.adjustPrice(prices[0]);
											UFDouble num = btvos[ii].getNnumber();
											UFDouble mny = num.multiply(prices[0]);
                      //��������
                      mny = context.adjustMoney(mny);
											btvos[ii].setNprice(price);
											btvos[ii].setNmoney(mny);
											alIANormalVOs.add(btvos[ii]);
											// ��д����VO
											PubVO pubVO = new PubVO();
											pubVO.setAttributeValue("cgeneralbid", btvos[ii]
													.getCicitemid());
											pubVO
													.setAttributeValue("btoinzgflag", new UFBoolean("Y"));
											pubVO.setAttributeValue(icprice, price);
											pubVO.setAttributeValue(icmny, mny);
											alICVOs.add(pubVO);
										} else {// ���ݹ�
											// ��д����VO
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
												"20143010", "UPP20143010-000300")/*@"�쳣����ȡ������ϵʧ��"*/);
									}
								}
							}
							// ����Ʒ�е������һ������
							BillVO largessBvo = (BillVO) bvo.clone();
							BillItemVO[] largessItems = (BillItemVO[]) alIALargessVOs
									.toArray(new BillItemVO[alIALargessVOs.size()]);
							if (largessItems.length != 0) {
								largessBvo.setChildrenVO(largessItems);
								vBills.addElement(largessBvo);
								//��Դͷ�������������������������,���ɵ�����˾�ĵ������ⵥ
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
									//�Ա���������������Ϣ���й�˾��ת��
									BillTool.mapDocsBetweenCorps(outbill, srcCorp, dstCorp);
									vBills.addElement(outbill);
								}
							}
							// �������ı���Żش��������
							BillItemVO[] newItems = (BillItemVO[]) alIANormalVOs
									.toArray(new BillItemVO[alIANormalVOs.size()]);
							if (newItems.length != 0) {
								((BillHeaderVO) bvo.getParentVO())
										.setBestimateflag(UFBoolean.TRUE);
								bvo.setChildrenVO(newItems);
							} else {
								continue;
							}
							// ��д���
							PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs
									.size()]);
							Object oICInstance = NCLocator.getInstance().lookup(
									IICToIA.class.getName());
							((IICToIA) oICInstance).rewriteZg1(pubVOs);
						} else if (sBillTypeCode.equals(ConstVO.m_sBillDBCKD)) {// �������ⵥ���
							Object oTOTrans = NCLocator.getInstance().lookup(
									ITransferBill.class.getName());
							BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
							//׼�����յ�VO
							ArrayList alIAVOs = new ArrayList();
							//��д����VO
							ArrayList alICVOs = new ArrayList();
							String[] orderIDs = new String[btvos.length];
							for (int ii = 0; ii < btvos.length; ii++) {
								//��ȡ��������ID
								orderIDs[ii] = btvos[ii].getCfirstbillitemid();
							}
							AggregatedTransrelaVO[] relaVOs = null;
							try {//��ȡ������ϵ
								relaVOs = ((ITransferBill) oTOTrans)
										.getTransInfoFromOrder(orderIDs);
							} catch (Exception e) {
								throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
										"20143010", "UPP20143010-000300")/*@"�쳣����ȡ������ϵʧ��"*/);
							}
							if ((relaVOs != null) && (relaVOs.length != 0)) {
								for (int i = 0; i < relaVOs.length; i++) {
									UFBoolean b = ((TransrelaItemVO) relaVOs[i].getChildrenVO()[0])
											.getBpushia();
									//����ǹ�˾���������,������Ʒ����"����ǩ���Ƿ񴫴������"����Ӱ��,����������� 
									UFBoolean ufbLargess = btvos[i].getBlargessflag();
									if ((ufbLargess != null) && ufbLargess.booleanValue() && 
											ConstVO.m_sBillGSJDBDD.equals(btvos[i].getCfirstbilltypecode())) {
										//btvos[i].setNmoney(new UFDouble(0));
										//btvos[i].setNprice(new UFDouble(0));
										alIAVOs.add(btvos[i]);
									} else 	if ((b != null) && b.booleanValue()) {
										//�ǹ�˾�����������Ʒ����ͨ�д����
										alIAVOs.add(btvos[i]);// д����
										//��д���
										PubVO pubVO = new PubVO();
										pubVO.setAttributeValue("cgeneralbid", btvos[i]
												.getCicitemid());
										pubVO.setAttributeValue("btoouttoiaflag",
												new UFBoolean("Y"));
										alICVOs.add(pubVO);
									} else {
										//�������
										//��д���
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
										"20143010", "UPP20143010-000300")/*@"�쳣����ȡ������ϵʧ��"*/);
							}
							// �������ı���Żش��������
							BillItemVO[] newItems = (BillItemVO[]) alIAVOs
									.toArray(new BillItemVO[alIAVOs.size()]);
							bvo.setChildrenVO(newItems);
							// ��д���
							PubVO[] pubVOs = (PubVO[]) alICVOs.toArray(new PubVO[alICVOs
									.size()]);
							Object oICInstance = NCLocator.getInstance().lookup(
									IICToIA.class.getName());
							((IICToIA) oICInstance).rewrite4Y2IAflag(pubVOs);
						}
					} else if (sSourceModule.equals(ConstVO.m_sModuleSO)
							&& sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)) {//��Ʊ���ʱִ�д��ж�
						if ((bvo.getChildrenVO() != null) && (bvo.getChildrenVO().length > 0)) {
							if (sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
								// �����۷�Ʊ
								String sSaleBodyID = btvo.getCcsaleadviceitemid();
								if (((sFQSK != null) && (sFQSK.indexOf(sBizTypeID) != -1))
										|| ((sWTDX != null) && (sWTDX.indexOf(sBizTypeID) != -1))) {
									// �����տ��ί�д���
									if ((sSaleBodyID != null) && (sSaleBodyID.trim().length() != 0)) {
										// ��Դ�����۵����۳��ⵥ
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
										if (iNum == 0) {//�����˲������ж�
											BusinessException e = new BusinessException(
													nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
															"20143010", "UPP20143010-000018")
															/*@res "�˵�����Դ�����۷�Ʊ�����Ƿ����տ��ί�д���ҵ��
															 * ��û���ҵ���Ӧ����Դ�ڳ��ⵥ�ĵ��ݣ������"*/);
											throw e;
										}
									}
								}
							}
						}
					} else if (sSourceModule.equals(ConstVO.m_sModuleIC)) {//�����˲������ж�
						/*
						 * ��浥��ǩ��ʱ�� 
						 * ��ⵥ�����ֿ���vmi�ֿ�Ĳ��ܴ�������㡣�������Ļ���ʱ���ٴ�һ�Σ�
						 * �ظ���Ŀǰֻ�Բɹ���ⵥ�����˿��ơ�
						 * ���ⵥ���������ֿ�����ֿⶼ��vmi�ֿ⣬���������Ļ��ܵģ�
						 * Ҳ���ܴ�������㡣���������㽫���֣����һ�Σ��������Ρ�
						 */
						if ((bhvo.getFdispatchflag() != null)
								&& (bhvo.getFdispatchflag().intValue() == 0)
								&& (bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()) {
							// ��ⵥ�������ֿ���VMI����
							Log.info("-----IASaveBill-----��ⵥ�������ֿ���VMI���㣬������");
							continue;
						}
						if ((bhvo.getFdispatchflag() != null)
								&& (bhvo.getFdispatchflag().intValue() == 1)
								&& (bhvo.getBingathersettle() != null)
								&& bhvo.getBingathersettle().booleanValue()
								&& (bhvo.getBoutgathersettle() != null)
								&& bhvo.getBoutgathersettle().booleanValue()) {
							// ���ⵥ�������ֿ���VMI�����ҳ���ֿ���VMI����
							Log.info("--IASaveBill--���ⵥ�������ֿ���VMI�����ҳ���ֿ���VMI���㣬������");
							continue;
						}
						// ���˵�����Ϊ0�ļ�¼
						BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
						Vector vBtvo = new Vector(1, 1);
						for (int ii = 0; ii < btvos.length; ii++) {
							// ���˵�����Ϊ0�ļ�¼
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
							Log.info("----IASaveBill---�˵������б���������ȫΪ0��������㲻��������");
							continue;
						}
					} else if (sBillTypeCode.equals(ConstVO.m_sBillRKTZD)) {//�����˲������ж�
						/* �������� */
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
									 * ������㷽ʽΪ���ý��㣬��������ID�͵������ݷ�¼ID��ŵ��ǲɹ������嵥ID�Ͳɹ������嵥��¼ID��
									 * ������㷽ʽΪ�ɹ����㣬��������ID�͵������ݷ�¼ID��ŵ��ǿ�浥��ID�Ϳ�浥�ݷ�¼ID��
									 */
									String sql = "select cbillid, cbill_bid from ia_bill_b where dr = 0 and csourcebillid ='"
											+ sAdjbillid
											+ "'"
											+ " and csourcebillitemid = '"
											+ sAdjbillitemid + "'";
									String sResult[][] = cbo.queryData(sql.toString());
									if (sResult.length == 0) {
										throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
										"UPP20143010-000301")/*@"û�в�ѯ���������ĵ���"*/);
									} else if (sResult.length > 1) {
										// ������ѯ�����Ľ������1���������ɹ�����ģʽΪ�����س����ģʽ�����ֶ���գ���������
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
				
				timer.addExecutePhase("���������ϵͳ���ݽ��� ҵ����"/*notranslate*/);
				
				// �ִ������֯ת�����ɱ������֯���������������֯�Ƿ���ͬһ�ɱ������֯
				binsrtedVOs = this.changeAndCheckCalbodys(binsrtedVOs, sSourceModule,
						sCorpID);
				
				timer.addExecutePhase("���������ϵͳ�������п����֯ת�������֯"/*notranslate*/);
				// add by hanwei 2004-03-26 ʹ����ʱ����
				
				ArrayList alInvIDs = new ArrayList();
				ArrayList alSourceBillItemIDs = new ArrayList();
				ArrayList alAdjustBillItemID = new ArrayList();
				for (int j = 0; j < binsrtedVOs.length; j++) {
					BillVO bvo = binsrtedVOs[j];
					// ������ʱ���¼
					for (int ii = 0; ii < bvo.getChildrenVO().length; ii++) {//�Ա�����б�����bvo.getChildrenVO().length���ȵ��ڱ��������
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
								&& (alAdjustBillItemID.contains(sAdjustBillItemID) == false)) {//�����˲������ж�
							alAdjustBillItemID.add(sAdjustBillItemID);
						}
					}
				}
				// û��Ҫ����ĵ���
				if ((alInvIDs == null) || (alInvIDs.size() == 0)) {//�����˲������ж�
          return null;
        }
				TempTableDMO dmo = new TempTableDMO();
				String pk_corp = binsrtedVOs[0].getPk_corp();
				String billtype = binsrtedVOs[0].getBillTypeCode();

				// ***************************************************
				if (sSourceModule.equals(ConstVO.m_sModuleIC)//��if�ж��ں�Ʊ���ʱ��ִ��
						&& (alSourceBillItemIDs.size() != 0)) {
					String sSubSql = dmo.insertTempTable(alSourceBillItemIDs,
							nc.vo.ia.pub.TempTableVO.TEMPTABLE_SOURCE,
							nc.vo.ia.pub.TempTableVO.TEMPPKFIELD_SOURCE);
					// ��洫��ĵ��ݣ�����
					StringBuffer sql = new StringBuffer(" select ");
					sql.append(" count(csourcebillitemid) ");
					sql.append(" from ");
					sql.append(" ia_bill_b ");
					sql.append(" where ");
					// **********************************
					// add by hanwei 2004-03-26 ʹ����ʱ����
					sql.append(" csourcebillitemid in " + sSubSql);
					//��ͬ�������͵ĵ��ݿ�����Դ��ͬһ��¼��// zlq 20060608
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
										"UPP20143010-000019")/* @res "����������ĵ����ظ�������" */);
						throw e;
					}
				}
				if (sSourceModule.equals(ConstVO.m_sModulePO)//��if�ж��ں�Ʊ���ʱ��ִ��
						&& (alAdjustBillItemID.size() != 0)) {
					// **********************************
					// add by hanwei 2004-03-26 ʹ����ʱ����
					String sAdjustSql = "";
					if (alAdjustBillItemID.size() != 0) {
						sAdjustSql = dmo.insertTempTable(alAdjustBillItemID,
								nc.vo.ia.pub.TempTableVO.TEMPTABLE_ADJUST, "id");
					}
					// **********************************
					/* �ɹ�����ĵ��ݣ�������������(���ݲɹ������ԭ�ݹ��Ŀ�浥�ݵĵ���ͷid��
					 * ��id�õ�����Ĳɹ���ⵥ��ͷid����id)*/
					StringBuffer sql = new StringBuffer(" select ");
					sql.append(" cbillid,cbill_bid,csourcebillitemid ");
					sql.append(" from ");
					sql.append(" ia_bill_b ");
					sql.append(" where ");
					sql.append(" dr = 0 ");
					sql.append(" and ");
					// **********************************
					// add by hanwei 2004-03-26 ʹ����ʱ����
					sql.append(" csourcebillitemid in " + sAdjustSql);
					// **********************************
					String[][] sResult = cbo.queryDataNoTranslate(sql.toString());
					if ((sResult != null) && (sResult.length != 0)) {
						// String sCbillid = "";
						// String sCbillbid = "";
						// String sCsourcebillitemid = "";
						HashMap hm = new HashMap();
						// �Ѳ�����Ľ����������ʱ��hash����
						for (int ii = 0; ii < sResult.length; ii++) {
							hm.put(sResult[ii][2], new String[] { sResult[ii][0],
									sResult[ii][1] });
						}
						// ����Ӧ�ı���������ͷid����id��ֵ
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
				timer.addExecutePhase("���������ϵͳ���ݽ��� ���Ͳɹ���ҵ����"/*notranslate*/);
				// �Ƿ����
				BillTool.ensureAccountIsOpen(sCorpID, curbhvo.getDbilldate());
				
				timer.addExecutePhase("���������ϵͳ���ݽ��� ���˼��"/*notranslate*/);
				// �ж��Ƿ��Ѿ���ĩ����
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
					if ((avoResult != null) && (avoResult.length != 0)) {//�����˲������ж�
						// ����ĩ����
						BusinessException e = null;
						if (sAccountMonth.equals("00") == false) {
							// "�ڼ�����ĩ���ˣ����������ӵ��ݣ������µ�¼���޸ĵ�������");
							String[] value = new String[] { sAccountYear, sAccountMonth };
							e = new BusinessException(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("20143010", "UPP20143010-000180",
											null, value));
							throw e;
						} else {
							e = new BusinessException(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("20143010", "UPP20143010-000003")
									/* @res"�Ѿ��ڳ����ʣ�������������������ǰ�ĵ��ݣ������µ�¼���޸ĵ�������"*/);
							throw e;
						}
					}
				}
				timer.addExecutePhase("��������� �Ƿ���ĩ���ʼ��"/*notranslate*/);
				// ���뵥��
				binsrtedVOs = this.saveOutterArray(cl, binsrtedVOs, iPeci, sSourceModule,sSimulate, svo);
				timer.addExecutePhase("���������ϵͳ���ݽ��� �������"/*notranslate*/);
				BillSaveFacade process = new BillSaveFacade();
				binsrtedVOs = process.process(binsrtedVOs);
				for (int h = 0; h < binsrtedVOs.length; h++) {
					vAuditBills.addElement(binsrtedVOs[h]);
				}
				// �ɱ�����
				this.auditOneBill(binsrtedVOs);
				timer.addExecutePhase("���������ϵͳ���ݽ��� �����ɱ�����"/*notranslate*/);
				ArrayList alIaHeadBodyId = null;
				for (int i = 0; i < binsrtedVOs.length; i++) {
					BillVO bvo = binsrtedVOs[i];
					String sBillTypeCode = ((BillHeaderVO) bvo.getParentVO())
							.getCbilltypecode();
					if (sBillTypeCode.equals(ConstVO.m_sBillCGRKD)
							&& sSourceModule.equals(ConstVO.m_sModuleIC)
							&& (bPOStart.booleanValue() == false)) {//��if�ж��ں�Ʊ���ʱ��ִ��
						// �ǿ�洫��Ĳɹ���ⵥ�Ҳɹ�û������
						Object oInstance = NCLocator.getInstance().lookup(
								IICToIA.class.getName());
						((IICToIA) oInstance).setSettledFlag(bvo);
					}
					// ���÷���ֵ
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
			timer.showAllExecutePhase("�������savebillfromoutterarray��");
		}
    
    return hmSourceBodyId2IaHeadBodyId;
  }
  
  
  public HashMap saveBillFromOutterArray_bill(AggregatedValueObject[] bvos,
	      String sSourceModule, String sSourceBillType) throws BusinessException{
	  Log.debug("��Ʊ���ʱִ�д˷���saveBillFromOutterArray_bill"); 
	  return this.saveBillFromOutterArray(bvos,sSourceModule,sSourceBillType);
  }
  

  /**
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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
	 * ��������:�ⲿϵͳ���뵥��ʱ�Զ��ɱ�����
	 * 
	 * ����: BillVO[] bvos ----- Ҫ�ɱ�����ĵ���
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 */
  public void auditOneBill(BillVO[] bvos) throws BusinessException {
    try {
      // ��ʼ��BO
      AuditImpl abo = new AuditImpl();

      // ��ʼ��BO
      CommonDataImpl cbo = new CommonDataImpl();

      Vector vAuditTemp = new Vector(1, 1);

      String sCorpID = "";
      String sBillDate = "";
      String sUser = "";

      if ((bvos != null) && (bvos.length != 0)) {
        sCorpID = ((BillHeaderVO) bvos[0].getParentVO()).getPk_corp();
      }
      // �Ƿ񱣴���������(�ɱ�����)
      // ConstVO.m_iPara_ZDSHJZ = 1 --- �Ƶ��Ƿ��Զ���˼���
      String sAudit = cbo.getParaValue(sCorpID, new Integer(
          ConstVO.m_iPara_ZDSHJZ));

      // ���óɱ�����ķ�����˵���
      boolean bSuccessed = true;

      for (int j = 0; j < bvos.length; j++) {
        BillVO bvo = bvos[j];

        // ��������
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        // ��õ�λ����
        sCorpID = bhvo.getPk_corp();

        // ��õ�������
        String sBillType = bhvo.getCbilltypecode();

        // ��õ�������
        sBillDate = bhvo.getDbilldate().toString();

        // ��ò���Ա
        sUser = bhvo.getCoperatorid();

        // Դͷ��������
        // String sFirstBillType = ((BillItemVO)
				// bvo.getChildrenVO()[0]).getCfirstbilltypecode();

        // ��Դ��������
        String sSourceBillType = ((BillItemVO) bvo.getChildrenVO()[0])
            .getCsourcebilltypecode();

        // 20050616 xiugai zlq
        // if (sBillType.equals(ConstVO.m_sBillQTRKD)
        // && sFirstBillType != null &&
        // (sFirstBillType.equals(ConstVO.m_sBillKCXTZHD) ||
        // sFirstBillType.equals(ConstVO.m_sBillKCCXD) ||
        // sFirstBillType.equals(ConstVO.m_sBillKCZKD)))
        //			{
        //				//�ɿ�������̬ת���Ϳ��ϵͳ��ж�Ϳ��ϵͳת�����ɵ�������ⵥ�����Զ��ɱ�����
        //				Log.info("�ɿ�������̬ת�����ж��ת�����ɵ�������ⵥ�������Զ��ɱ�����");
        //				continue;
        //			}

        if ((sAudit != null) && sAudit.equalsIgnoreCase("Y")) {
          //��ǰ�汾��ֻ����Ƿ��Զ����м��ʣ����ڰ汾�������������ַ���
          //0������Ʒ���������Ʒ 2��ԭ����
          sAudit = "012";
        } else if ((sAudit != null) && sAudit.equalsIgnoreCase("N")) {
          //��ǰ�汾
          return;
        }

        //���Ƽ۷�ʽ�Ǹ���ָ���Ĺ��˵�
        Integer iRDFlag = bhvo.getFdispatchflag();
        String sBizTypeID = bhvo.getCbiztypeid();

        //���ҵ������
        String[] sBizs = new String[2];
        sBizs[0] = ConstVO.m_sBizFQSK;
        sBizs[1] = ConstVO.m_sBizWTDX;

        java.util.Hashtable ht = cbo.getBizTypeIDs(sCorpID, sBizs);
        String sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
        String sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);

        //�ɱ��Ƿ����� m_sModuleCodeCA = "3006" --- �ɱ�����ı�־
        UFBoolean bStart = cbo.isModuleStarted(sCorpID,
            ConstVO.m_sModuleCodeCA, bhvo.getCaccountyear() + "-"
                + bhvo.getCaccountmonth());

        //�����Զ��ɱ�����ķ�¼
        Vector vTemp = new Vector(1, 1);

        for (int i = 0; i < bvo.getChildrenVO().length; i++) {
          BillItemVO bAudittVO = (BillItemVO) bvo.getChildrenVO()[i];
          Integer iPricemode = bAudittVO.getFpricemodeflag();//�Ƽ۷�ʽ
          UFDouble dNumber = bAudittVO.getNnumber();
          String sInvkind = bAudittVO.getCinvkind();//������ͣ�����Ʒ�����Ʒ��ԭ����

          if ((sAudit == null)
              || ((sInvkind != null) && (sAudit.indexOf(sInvkind) == -1))
              || (sInvkind == null) || (sInvkind.trim().length() == 0)) {
            //�����ɱ�����Ĳ�����û�д˴������
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
              //�ǳɱ������ҳɱ����ã��ǲ���Ʒ��ⵥ����ϳ��ⵥ
              continue;
            }
          }

          if ((iPricemode != null) && (iPricemode.intValue() == ConstVO.GBJJ)
              && (dNumber != null)) {
            //�Ǹ���Ƽ�δ�ɱ�����,��������
            double ddnum = dNumber.doubleValue();

            if ((iRDFlag != null) && (iRDFlag.intValue() == 1) && (ddnum > 0)) {
              if (sSourceBillType == null) {
                //�����ֳ��ⵥ������Դ���Ƿ�Ʊ�����ܳɱ�����
              } else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
                //�����ֳ��ⵥ������Դ���Ƿ�Ʊ�����ܳɱ�����
              } else if ((sFQSK != null) && (sBizTypeID != null)
                  && (sFQSK.indexOf(sBizTypeID) == -1)) {
                //�����ֳ��ⵥ������Դ�Ƿ�Ʊ�������Ƿ����տ���ܳɱ�����
              } else if ((sWTDX != null) && (sBizTypeID != null)
                  && (sWTDX.indexOf(sBizTypeID) == -1)) {
                //�����ֳ��ⵥ������Դ�Ƿ�Ʊ��������ί�д��������ܳɱ�����
              } else {
                vTemp.addElement(bAudittVO);
              }
            } else if ((iRDFlag != null) && (iRDFlag.intValue() == 0) && (ddnum < 0)) {
              //�Ǻ�����ⵥ�����ܳɱ�����
            } else {
              //����������ָ��
              vTemp.addElement(bAudittVO);
            }
          } else {
            //����������ָ��
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

        //��û���ڼ�
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
                  "UPP20143010-000022")/* @res "�ɱ�����ʧ��" */);
          throw be;
        }
      }
    } catch (Exception ex) {
      ExceptionUtils.marsh( ex );
    }

  }

  /**
   * ��������:�жϴ���ĵ������Ƿ����ظ���
   * 
   * ����: BillVO[] bvos ----- ����VO���� sSourceModule ----- ��Դģ�� String
   * sSourceBillType ----- ��Դ��������
   * 
   * ����ֵ:
   * 
   * �쳣:
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

    //����洫��ĵ����Ƿ����ظ���    
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
                  "UPP20143010-000019")/* @res "����������ĵ����ظ�������" */);
        }

        sourceids.add(btvo.getCsourcebillitemid());
      }
    }
  }

  /**
     * ��������:�����ɷ�Ʊ���ɵ����۳ɱ���ת��
     */
    private void doWithSaleBillsFromFaPiao(String sSourceModuleName, BillItemVO btvo,
        int iStatus, CommonDataImpl outercbo) throws Exception {
      CommonDataImpl cbo = null;
  
      if (outercbo == null) {
        cbo = new CommonDataImpl();
      }
  
      //����
      UFDouble dNumber = btvo.getNnumber();
  
      String sFunc = " isnull( ";
  
      if (dNumber == null) {
        //û������������>0��������
        return;
      }
  
      String sCompCode = "cbill_bid";
      if ((sSourceModuleName != null)
          && sSourceModuleName.trim().equals(ConstVO.m_sModuleSO)) {
        sCompCode = "csourcebillitemid";
      }
  
      //������
      String sSaleAdviceItemID = btvo.getCcsaleadviceitemid();
      if ((sSaleAdviceItemID != null) && (sSaleAdviceItemID.trim().length() != 0)) {
        if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
          //��������ӻ�ɾ����ֱ���޸���Դ���ⵥ���ݵ�����
          double dNum = dNumber.doubleValue();
  
          if (iStatus == ConstVO.ADD_STATUS) {
            double dTotalNum = 0;
            double dSettledsendNum = 0;
            double dSupplyNum = 0;
  
            //��������ӣ��ж������Ƿ񹻳����
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
              //�˵�����Դ�����۷�Ʊ�����Ƿ����տ��ί�д���ҵ��
              //��û���ҵ���Ӧ����Դ�ڳ��ⵥ�ĵ��ݣ������
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                      "UPP20143010-000018"));
            }
  
            // ��Ʊ������������ͬ�ţ���Ʊ������Ʊ����ţ���Ʊʱ����س巢Ʊ
            if (((dTotalNum <= 0) && (dNum <= 0)) 
                || ((dTotalNum >= 0) && (dNum >= 0))) {
              // ͬ�ţ���ʣ��ɿ�Ʊ�����뷢Ʊ�����Ƚ�
              if (Math.abs(dSupplyNum) < Math.abs(dNum)) {
                // @res��{0}�еĴ����ǰ���������������Ϊ{1},�����")
                String[] value = new String[] { btvo.getIrownumber().toString(),
                    String.valueOf(dSupplyNum) };
                throw new BusinessException(
                    nc.bs.ml.NCLangResOnserver.getInstance().
                    getStrByID("20143010", "UPP20143010-000197", null, value));
              }
            } else {
              // ��ţ����ۼ��ѿ�Ʊ�����뷢Ʊ�����Ƚ�
              if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {
            	  Log.debug("doWithSaleBillsFromFaPiaoִ����");
                // @res��{0}�еĴ����ǰ��������س�����Ϊ{1},�����")
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
            //ɾ����嵥���ݣ���ȥ���������ݵ��ۼƻس�����
            sOper = " - ";
          }
  
          //�޸Ķ�Ӧ���ݵ��ۼƻس�����
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
   * ��������:����������ϵͳ��Ʊ���ɵ����۳ɱ���ת����ҵ������Ϊ�����տί�д���
   */
  private void doWithSOFaPiao( BillItemVO btvo, int iStatus,CommonDataImpl outercbo) 
  throws Exception {
    CommonDataImpl cbo = null;

    if (outercbo == null) {
      cbo = new CommonDataImpl();
    }

    // ����
    UFDouble dNumber = btvo.getNnumber();//�õ��ɱ���ת���Ŀ�Ʊ������-5835

    String sFunc = " isnull( ";

    if (dNumber == null) {
      // û��������������
      return;
    }

    String sCompCode = "csourcebillitemid";
    StringBuffer sSQL = new StringBuffer();
    // ������
    String sSaleAdviceItemID = btvo.getCcsaleadviceitemid();
    if((sSaleAdviceItemID != null) && (sSaleAdviceItemID.trim().length() != 0)) {
      if ((iStatus == ConstVO.ADD_STATUS) || (iStatus == ConstVO.DELETE_STATUS)) {
        // ��������ӻ�ɾ����ֱ���޸���Դ���ⵥ���ݵ�����
        double dNum = dNumber.doubleValue();//-5835
        double dTotalNum = 0;
        double dSettledsendNum = 0;
        double dSupplyNum = 0;

        // ��������ӣ��ж������Ƿ񹻳����

        sSQL = sSQL.append(" select nnumber, nsettledsendnum, ");
        sSQL = sSQL.append(" nnumber-" + sFunc + "nsettledsendnum,0)");
        sSQL = sSQL.append(" from ");
        sSQL = sSQL.append(" ia_bill_b ");
        sSQL = sSQL.append(" where ");
        sSQL = sSQL.append(" dr = 0 ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "' and cbilltypecode = 'I5'");//edit by shikun 2014-05-29 I5=���۳ɱ���ת��
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
        } else {//��巢Ʊ��˴˴���ִ��
          /*
           * @res�˵�����Դ�����۷�Ʊ�����Ƿ����տ��ί�д���ҵ�� ��û���ҵ���Ӧ����Դ�ڳ��ⵥ�ĵ��ݣ������
           */
          throw new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
              "UPP20143010-000018"));
        }
        String sOper ="";
        if (iStatus == ConstVO.ADD_STATUS) {
          // ��Ʊ������������ͬ�ţ���Ʊ������Ʊ����ţ���Ʊ�Ƿ���س巢Ʊ
          if (((dTotalNum <= 0) && (dNum <= 0)) 
              || ((dTotalNum >= 0) && (dNum >= 0))) {//��if�ж��ں�Ʊ���ʱ��ִ��
            // ͬ�ţ���ʣ��ɿ�Ʊ�����뷢Ʊ�����Ƚ�
            if (Math.abs(dSupplyNum) < Math.abs(dNum)) {
              // @res��{0}�еĴ����ǰ���������������Ϊ{1},�����")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSupplyNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000197", null, value));
              
            }
          } else {//��巢Ʊ����ڴ˴��жϺ󱨴����������4431��
            // ��ţ����ۼ��ѿ�Ʊ�����뷢Ʊ�����Ƚ�
            if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {//math.abs()���ز����ľ���ֵ����if�ж��ں�Ʊ���ʱ��ִ��
              // @res��{0}�еĴ����ǰ�����ou���س�����Ϊ{1},�����")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000196", null, value));
            }
          }
          sOper = " + ";//�����ִ��
        }
        else if (iStatus == ConstVO.DELETE_STATUS) {//�����˴˴�����ִ��
          //��Ʊ������������ͬ�ţ���Ʊ������Ʊ����ţ���Ʊʱ����س巢Ʊ
          if (((dTotalNum <= 0) && (dNum <= 0)) 
              || ((dTotalNum >= 0) && (dNum >= 0))) {
            //ɾ����嵥���ݣ���ȥ���������ݵ��ۼƻس�����
            if (Math.abs(dSettledsendNum) < Math.abs(dNum)) {
              // @res��{0}�еĴ���ۼƽ������� {1} С�ڵ�ǰҪȡ�����������{2},�����")
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum),String.valueOf(dNum) };
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000321", null, value));
            }
          }else{
            if(Math.abs(dTotalNum) < Math.abs(dSettledsendNum-dNum)){
              //@res ��{0}�еĴ��ȡ���س巢Ʊ��������� {1} ����ԭʼ���� {2},�����
              String[] value = new String[] { btvo.getIrownumber().toString(),
                  String.valueOf(dSettledsendNum-dNum),String.valueOf(dTotalNum)};
              throw new BusinessException(
                  nc.bs.ml.NCLangResOnserver.getInstance().
                  getStrByID("20143010", "UPP20143010-000322", null, value));
            }
          }
          sOper = " - ";
        }
        // �޸Ķ�Ӧ���ݵ��ۼƻس�����
        sSQL = new StringBuffer(" update ");
        sSQL = sSQL.append(" ia_bill_b ");
        sSQL = sSQL.append(" set ");
        sSQL = sSQL.append(" nsettledsendnum = " + sFunc
            + " nsettledsendnum,0) " + sOper + dNum);
        sSQL = sSQL.append(" where ");
        sSQL = sSQL.append(" dr = 0 ");
        sSQL = sSQL.append(" and ");
        sSQL = sSQL.append(sCompCode + " = '" + sSaleAdviceItemID + "' and cbilltypecode = 'I5'");//edit by shikun 2014-05-29 I5=���۳ɱ���ת��

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
   * ��������:��������㵥��VO�üƻ���д��ʵ�ʼۣ�ΪSEG�ṩ��
   * 
   * ����:
   * 
   * ����ֵ:
   * 
   * �쳣:
   * 
   * �������ڣ�(2002-12-10 9:01:17)
   */
  public BillVO[] fillVOByPlanedBatch(BillVO[] bvos) throws BusinessException {
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //�����������
      String sPk_corp = null;
      Integer[] iPeci = null;
      for (int ii = 0; ii < bvos.length; ii++) {
        BillHeaderVO bhvo = (BillHeaderVO) bvos[ii].getParentVO();
        BillItemVO[] btvos = (BillItemVO[]) bvos[ii].getChildrenVO();

        //��˾���ˣ�Ҫ���²�ѯ���ݾ���
        if ((sPk_corp == null) || !sPk_corp.equals(bhvo.getPk_corp())) {
          sPk_corp = bhvo.getPk_corp();
          iPeci = cbo.getDataPrecision(bhvo.getPk_corp());
        }

        //�õ�����Ϊ����Ҫ��һ����ѯ�ƻ��۵Ĵ��
        //Hashtable(key:�������+����VO�����±꣬value:����VO�����е�ֵ)
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
        //��ѯ�����Ϣ������ֵ:������Ƽ۷�ʽ���ƻ��ۡ��Ƿ����κ���
        String[][] sResults = cbo.getPrices(sPk_corp, bhvo.getCrdcenterid(), s);

        //�üƻ���д��ʵ�ʼ�
        if ((sResults != null) && (sResults.length > 0)) {
          UFDouble dPlanedPrice = null;
          UFDouble dNumber = null;
          UFDouble dPlanedMny = null;
          BillItemVO btvo = null;
          for (int i = 0; i < sResults.length; i++) {
            //��üƼ۷�ʽ����

            //if (iPriceCode.intValue() == ConstVO.JHJ) //���������Ǽƻ��۵�
            {
              //�Ǽƻ��ۼƼۣ��Ѽƻ���д��ʵ�ʵ���,���Ѽƻ����д��ʵ�ʽ��
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
      //������
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
                                                                        * "��ص������ڽ��д������Ժ�����"
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
           * @res "����{0}�Ѿ�����ʵʱƾ֤�����Ѿ��ɱ����㣬����ɾ��"
           */
          throw new BusinessException ( message );
        }
      }
      
      
      //����շ���־
      Integer iRDFlag = ((BillHeaderVO) bvo.getParentVO()).getFdispatchflag();
      int iRD = -1;

      if ((iRDFlag != null) && (iRDFlag.toString().length() != 0)) {
        iRD = iRDFlag.intValue();
      }

      if (iRD == 0) {
        //��
        this.deleteIn(cl, bvo, false, sUserID,bReturnBillCode);
      } else if (iRD == 1) {
        //��
        this.deleteIn(cl, bvo, true, sUserID,bReturnBillCode);

        String sBillType = ((BillHeaderVO) bvo.getParentVO())
            .getCbilltypecode();
        String sBizTypeID = ((BillHeaderVO) bvo.getParentVO()).getCbiztypeid();
        //String sCorpID = ((BillHeaderVO) bvo.getParentVO()).getPk_corp();

        //������Դ��Ʊ�����۳ɱ���ת��
        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //�����۳ɱ���ת��
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
                //��Դ�����۷�Ʊ
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
                                                                        * "�շ���־�����Ϲ淶"
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
            .getStrByID("20143010", "UPP20143010-000010")/* @res "�ͷ�ҵ��������" */);
      }
    }
  }

  /**
   * ��������:ɾ������ɾ��������ȡ���Ͳ����ת������ϵͳ����
   * 
   * ����: BillVO[] bvos ----- ����VOs String sUserID ----- ����Աid boolean
   * bReturnCode ----- �Ƿ�ɾ�����ݺ�
   * 
   * ����ֵ:
   * 
   * �쳣:
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
                                                                        * "��ص������ڽ��д������Ժ�����"
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
          //���ǲ����ת���������ȡ������˰������
          //�Ƿ��ɾ��
          String sBillType = ((BillHeaderVO) vo.getParentVO())
              .getCbilltypecode();

          boolean bCheck = true; //�Ƿ���

          if (sBillType.equals(ConstVO.m_sBillCKTZD)) {
            //��ĩ�������ɵĳ����������ֱ��ɾ������ʹ�����
            bCheck = false;

            for (int h = 0; h < vo.getChildrenVO().length; h++) {
              BillItemVO btvo = (BillItemVO) vo.getChildrenVO()[h];
              Integer iDataGetMode = btvo.getFdatagetmodelflag();

              if ((iDataGetMode == null)
                  || (iDataGetMode.intValue() != ConstVO.QMCL)) {
                //������ĩ�������ɵ�
                bCheck = true;
                break;
              }
            }
          }

          //����Ƿ��ɾ��
          if (bCheck && (this.checkDeleteData(vo) == false)) {
            return;
          }
        }
      }

      //ִ�����ݿ�ɾ������
      this.execDelete(cl, bvos);

      if (bReturnCode) {
        //�ѵ��ݺ��˻أ��Թ����ʹ��
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
   * ��������:ɾ������ɾ��������ȡ���Ͳ����ת������ϵͳ����
   * 
   * ����: BillVO[] bvos ----- ����VOs String sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:ɾ������ɾ��������ȡ���Ͳ����ת������ϵͳ����
   * 
   * ����: BillVO[] bvos ----- ����VOs String sUserID ----- ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
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
   * ��������:ɾ������
   * 
   * ����: BillVO vo ----- ����VO boolean bIsOut ----- �Ƿ���� String sUserID -----
   * ����Աid
   * 
   * ����ֵ:
   * 
   * �쳣:
   * 
   *  
   */
  private void deleteIn(ClientLink cl, BillVO vo, boolean bIsOut, String sUserID,boolean bReturnBillCode)
      throws Exception {
    //��õ������ͣ������ת����˺�Ҳ��ɾ��
    String sBillTypeCode = ((BillHeaderVO) vo.getParentVO()).getCbilltypecode();

    if ((sBillTypeCode.equals(ConstVO.m_sBillCYLJZD) == false)
        && (sBillTypeCode.equals(ConstVO.m_sBillDJTQD) == false)
        && (sBillTypeCode.equals(ConstVO.m_sBillTSTZD) == false)) {
      //���ǲ����ת���������ȡ������˰������
      //�Ƿ��ɾ��
      String sBillType = ((BillHeaderVO) vo.getParentVO()).getCbilltypecode();

      boolean bCheck = true; //�Ƿ���

      if (sBillType.equals(ConstVO.m_sBillCKTZD)) {
        //��ĩ�������ɵĳ����������ֱ��ɾ������ʹ�����
        bCheck = false;

        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          BillItemVO btvo = (BillItemVO) vo.getChildrenVO()[i];
          Integer iDataGetMode = btvo.getFdatagetmodelflag();

          if ((iDataGetMode == null) || (iDataGetMode.intValue() != ConstVO.QMCL)) {
            //������ĩ�������ɵ�
            bCheck = true;
            break;
          }
        }
      }

      //����Ƿ��ɾ��
      if (bCheck && (this.checkDeleteData(vo) == false)) {
        return;
      }

      //�ȴ����嵥
      this.doWithRedBill(vo, ConstVO.DELETE_STATUS);

      //������ⵥ���κŵķ���
      if (bIsOut) {
        this.doWithOutBatch(vo, ConstVO.DELETE_STATUS);
      }
    }

    //ִ�����ݿ�ɾ������
    this.execDelete(cl, new BillVO[] { vo });

    //�ѵ��ݺ��˻أ��Թ����ʹ��
    if(bReturnBillCode){
    	BillCodeDMO bcDmo = new BillCodeDMO();
    	bcDmo.returnBillCodeWhenDelete(vo);
    }
  }

  /**
   * ��������:ִ��ɾ�����������������ϱ�־��ΪY����dr��Ϊ1
   * 
   * ����:BillVO����--Ҫɾ���ĵ�������
   * 
   * ����ֵ:
   * 
   * �쳣:��������������nullʱ�׳�NullPointerException
   * �½�RichDMOʱ�����׳�javax.naming.NamingException ,nc.bs.pub.SystemException
   * dmo.model2_EditBatch�����׳�java.sql.SQLException
   * 
   * @param bvos
   *          nc.vo.ia.bill_new.BillVO[]
   * @exception java.lang.NullPointerException
   *              �쳣˵����
   */
  private void execDelete(ClientLink cl, BillVO[] bvos)
      throws java.lang.NullPointerException, javax.naming.NamingException,
      nc.bs.pub.SystemException, java.sql.SQLException {

    RichDMO dmo = new RichDMO();

    for (int i = 0; i < bvos.length; i++) {

      //�����ϱ�־��ΪY
      ((BillHeaderVO) bvos[i].getParentVO())
          .setBdisableflag(new UFBoolean("Y"));

      //��dr��Ϊ1,״̬��Ϊ�޸�
      ((BillHeaderVO) bvos[i].getParentVO()).setDr(new Integer(1));//��ͷ
      ((BillHeaderVO) bvos[i].getParentVO()).setStatus(VOStatus.UPDATED);
      bvos[i].setStatus(VOStatus.UPDATED);

      BillItemVO[] bvoitems = (BillItemVO[]) bvos[i].getChildrenVO();//����
      for (int j = 0; j < bvoitems.length; j++) {
        bvoitems[j].setDr(new Integer(1));
        bvoitems[j].setStatus(VOStatus.UPDATED);
      }
    }
    //ִ���޸�
    dmo.model2_EditBatch(cl, bvos, ((BillHeaderVO) bvos[0].getParentVO())
        .getVOMeta().getPkColName(), null, null, false);

  }

  /**
   * ��������:ִ�и��²������Ȱ�voCurBill����һ�ݸ����������������ϱ�־��ΪY,dr��Ϊ1 �ٶ�bvoִ�и��²���
   * 
   * ����:voCurBill��δ�޸�ǰ��vo; bvo����Ҫ�޸ĵ�vo(���ѱ仯д�����ݿ��У�
   * 
   * ����ֵ:
   * 
   * �쳣:��������������nullʱ�׳�NullPointerException
   * �½�RichDMOʱ�����׳�javax.naming.NamingException ,nc.bs.pub.SystemException
   * dmo.model2_EditBatch�����׳�java.sql.SQLException
   * 
   * @param bvo
   *          nc.vo.ia.bill.BillVO
   * @exception java.lang.NullPointerException
   *              �쳣˵����
   * @throws CloneNotSupportedException
   */
  private void execUpdate(ClientLink cl, BillVO voCurBill, BillVO bvo)
      throws java.lang.NullPointerException, javax.naming.NamingException,
      nc.bs.pub.SystemException, java.sql.SQLException,
      CloneNotSupportedException {

    RichDMO rDmo = new RichDMO();

    //��������
    BillVO backbvo = (BillVO) voCurBill.clone();

    //�����ϱ�־��ΪY
    ((BillHeaderVO) backbvo.getParentVO()).setBdisableflag(new UFBoolean("Y"));

    //��dr��Ϊ1
    ((BillHeaderVO) backbvo.getParentVO()).setDr(new Integer(1));
    BillItemVO[] backbvoitem = (BillItemVO[]) backbvo.getChildrenVO();
    for (int i = 0; i < backbvoitem.length; i++) {
      backbvoitem[i].setDr(new Integer(1));
    }
    //�������������ݿ�
    rDmo.model2_CreateBatch(cl, new BillVO[] { backbvo },
        ((BillHeaderVO) backbvo.getParentVO()).getVOMeta().getPkColName(),
        null, null, true);

    //�������˳��Ĭ��ֵ
    this.setDefaultAuditSequence(bvo);
    //���޸Ĺ���VO��Ϣ���µ����ݿ�
    rDmo.model2_EditBatch(cl, new BillVO[] { bvo }, ((BillHeaderVO) bvo
        .getParentVO()).getVOMeta().getPkColName(), null, null, true);

  }


  /**
   * ��������:��������
   * 
   * ����: BillVO bill ----- ����
   * 
   * ����ֵ:����
   * 
   * �쳣:
   *  
   */
  public BillVO insert(ClientLink cl, BillVO bill) throws BusinessException {
    boolean bAccountLocked = false;
    BillVO bvo = null;
    try {
      IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());    
      srv.checkDefDataType(bill);

      CommonDataImpl cbo = new CommonDataImpl();

      //������ĩ���ʺ��ճ�¼�뵥��֮��Ĳ�������
      bAccountLocked = this.lockAccount(bill);

      //�����־
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
        //�Ǽƻ��۵�����
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

        //���ҵ������
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
	 * ��������:�����ݿ��в���һ��VO����
	 * 
	 * ����: BillVO[] bills ----- ����
	 * 
	 * ����ֵ:��������
	 * 
	 * �쳣:BusinessException
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
   * ��������:�����ݿ��в���һ��VO����
   * 
   * ����: BillVO[] bills ----- ����
   * 
   * ����ֵ:��������
   * 
   * �쳣:BusinessException
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

      //������ĩ���ʺ��ճ�¼�뵥��֮��Ĳ�������
      bAccountLocked = this.lockAccount(bills[0]);

      String sSimulate = cbo.getParaValue(((BillHeaderVO) bills[0]
          .getParentVO()).getPk_corp(), new Integer(ConstVO.m_iPara_SFJSXNCB));

      if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
        //����ģ��ɱ�
        SimulateVO svo = new SimulateVO();
        svo.setBills(bills);

        bills = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
      }

      //��ÿһ�ŵ����в��뵥�ݺ�
      if(bNeedNewBillCode){
      	alBillCodes = this.setBillCodes(bills);
      }

      //�������˳��Ĭ��ֵ
      for (int i = 0; i < bills.length; i++) {
        this.setDefaultAuditSequence(bills[i]);
      }

      //��������
      RichDMO rDmo = new RichDMO();
      rDmo.model2_CreateBatch(cl, bills,
          ((BillHeaderVO) bills[0].getParentVO()).getVOMeta().getPkColName(),
          null, null, true);

    } catch (Exception ex) {
      //����Ѿ������˾��˻ص��ݺ�
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
   * ��������:�����ݿ��в���һ��VO����
   * 
   * ����: BillVO[] bills ----- ����
   * 
   * ����ֵ:��������
   * 
   * �쳣:BusinessException
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
          //����ģ��ɱ�
          SimulateVO svo = new SimulateVO();
          svo.setBills(bills);

          bills = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
        }

        //��ÿһ�ŵ����в��뵥�ݺ�
        alBillCodes = this.setBillCodes(bills);
        //�������˳��Ĭ��ֵ
        for (int i = 0; i < bills.length; i++) {
          this.setDefaultAuditSequence(bills[i]);
        }
        //��������
        RichDMO rDmo = new RichDMO();
        rDmo.model2_CreateBatch(cl, bills, ((BillHeaderVO) bills[0]
            .getParentVO()).getVOMeta().getPkColName(), null, null, true);

      } catch (Exception ex) {
        //����Ѿ������˾��˻ص��ݺ�
        this.returnBillCodes(alBillCodes, bills);
        ExceptionUtils.marsh( ex );
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return bills;
  }

  /**
   * ��������:�����ݿ��в���һ��VO����
   * 
   * ����: BillVO[] bills ----- ����
   * 
   * ����ֵ:��������
   * 
   * �쳣:BusinessException
   *  
   */
  public BillVO[] insertArrayForBeginBills(ClientLink cl, BillVO[] bills)
      throws BusinessException {

    boolean bAccountLocked = false;

    try {
      //������insertBill���뵥�ݺ�,�ڵ�����delete���˻ص��ݺ�
      BillCodeDMO bcDmo = new BillCodeDMO();
      if ((bills == null) || (bills.length == 0)) {
        return bills;
      }

      //������ĩ���ʺ��ճ�¼�뵥��֮��Ĳ�������
      bAccountLocked = this.lockAccount(bills[0]);

      CommonDataImpl cbo = new CommonDataImpl();

      if (!(bills.length > 0)) {
        return bills;
      }

      String sCorpID = ((BillHeaderVO) bills[0].getParentVO()).getPk_corp();

      //ɾ����ǰ����Ŀ�桢�����ڳ�����
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

          //ִ�����ݿ�ɾ������
          this.execDelete(cl, new BillVO[] { bvos[i] });

          //20050406Ϊ��֤���񣬽����˵��ݺŷ��ں���
          //				//�ѵ��ݺ��˻أ��Թ����ʹ��
          //				bcDmo.returnBillCode(bvos[i]);

        }
      }

     
      for (int i = 0; i < bills.length; i++) {
        BillVO bvo = bills[i];
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        ///////////////////////////////////
        //�����������۽��ľ���
        IAMessage msg = new IAMessage();
        msg.put(bhvo);
        IAContext context = IAContext.create(bhvo.getPk_corp(), bhvo.getDbilldate().toString(), bhvo.getCoperatorid());
        BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
        for (int j = 0; j  < btvos.length; j++) {
          BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[j];
          msg.put(btvo);
          //������ݾ��� ����Ϊ false ��ʾ�ݲ���龫��
          btvo.setNnumber(DataCheckUtil.getInstance().checkNumber(context, msg, btvo.getNnumber(), false));
          btvo.setNprice(DataCheckUtil.getInstance().checkPrice(context, msg, btvo.getNprice(), false));
          btvo.setNmoney(DataCheckUtil.getInstance().checkMoney(context, msg, btvo.getNmoney(), false));
          btvo.setNplanedmny(DataCheckUtil.getInstance().checkMoney(context, msg, btvo.getNplanedmny(), false));
          btvo.setNchangerate( DataCheckUtil.getInstance().checkChangeRate(context, 
             msg, btvo.getNchangerate(), false));
          //��ȫ���ۺͽ��
          UFDouble[] priceAndMoney = DataCheckUtil.getInstance().fillPriceAndMnoney(
              btvo.getNnumber(), btvo.getNprice(), btvo.getNmoney(), context,bhvo.getCbilltypecode(), msg);
          btvo.setNprice(priceAndMoney[0]);
          btvo.setNmoney(priceAndMoney[1]);
          //����������۽��֮��Ļ����ϵ
          btvo.setNprice(DataCheckUtil.getInstance().checkNumPriceMoneyRelation(
              btvo.getNnumber(), btvo.getNprice(), btvo.getNmoney(), context, msg,false));
        }
        //////////////////////////////////
        
        boolean bFQSK = false;
        if ((bvo != null) && (bvo.getParentVO() != null)) {
          //���ҵ������
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

      //20050406Ϊ��֤���񣬽����˵��ݺŷ��ں���
      int len = (bvos == null ? 0 : bvos.length);
      for (int i = 0; i < len; i++) {
        //�ѵ��ݺ��˻أ��Թ����ʹ��
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
   * ��������:��������
   * 
   * ����: BillVO bill ----- ���� boolean bIsOut ----- �Ƿ��ǳ��ⵥ boolean bIsAdjustBill
   * ----- �Ƿ��ǵ���������������Ҫ������Σ� boolean bIsPlanedPriceAdjustBill ----- �Ƿ��Ǽƻ��۵�����
   * bIsFQWT ----- �Ƿ��Ƿ����տ��ί�д��� String sExistString ----- �Ӳ�ѯ��䣨�������Ч�ʣ� String
   * sSimulate ----- �Ƿ��������ɱ�
   * 
   * 
   * ����ֵ:����
   * 
   * �쳣:
   *  
   */
  private BillVO insertBill(ClientLink cl, BillVO bill, boolean bIsOut,
      boolean bIsAdjustBill, boolean bIsPlanedPriceAdjustBill, boolean bIsFQWT,
      String sExistString, String sProduceString, String sSimulate)
      throws Exception {

    String sBillCode = null;
    try {
      CommonDataImpl cbo = new CommonDataImpl();

      //���ҵ������
      String sDate = ((BillHeaderVO) bill.getParentVO()).getDbilldate()
          .toString();

      BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();


      String sBillType = bhvo.getCbilltypecode();

      //�Ϸ��Լ��
      this.checkData(null, bill, bIsOut, bIsAdjustBill, bIsPlanedPriceAdjustBill,
          ConstVO.ADD_STATUS, sDate);

      //long t1 =0, t2=0;
      if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
        //����ģ��ɱ�
        SimulateVO svo = new SimulateVO();
        svo.setBills(new BillVO[] { bill });
        //t1 = System.currentTimeMillis();

        bill = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS)[0];

        //t2 = System.currentTimeMillis();
        //Log.info("---------------------time to do simulateCost:
        // "+(t2-t1));
      }

      //t2 = System.currentTimeMillis();
      //�򵥾�vo�в��뵥�ݺ�
      sBillCode = this.setBillCode(bill);
      //�������˳��Ĭ��ֵ
      this.setDefaultAuditSequence(bill);

      //��������
      RichDMO rDmo = new RichDMO();
      rDmo.model2_CreateBatch(cl, new BillVO[] { bill }, ((BillHeaderVO) bill
          .getParentVO()).getVOMeta().getPkColName(), null, null, true);

      //long t3 = System.currentTimeMillis();
      //Log.info("---------------------time to do insert2DataBase:
      // "+(t3-t2));

      //�����嵥
      this.doWithRedBill(bill, ConstVO.ADD_STATUS);

      //UFBoolean bStart = cbo.isModuleStarted(sCorpID, ConstVO.m_sModuleCodeFA,
      //    ((BillHeaderVO) bill.getParentVO()).getCaccountyear() + "-"
      //        + ((BillHeaderVO) bill.getParentVO()).getCaccountmonth());

      //������̶��ʲ��ӿ�//20050527_�رմ����̶��ʲ��ӿ�
      //		if (bIsAdjustBill == false && bStart.booleanValue())
      //		{
      //			doWithFa(bill, null, bIsOut, ConstVO.ADD_STATUS);
      //		}

      String sSourceModuleName = ((BillHeaderVO) bill.getParentVO())
          .getCsourcemodulename();

      if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        //�����۳ɱ���ת��
        for (int i = 0; i < bill.getChildrenVO().length; i++) {
          BillItemVO btvo = (BillItemVO) bill.getChildrenVO()[i];
          String sSourceBillType = btvo.getCsourcebilltypecode();

          if (bIsFQWT) {
            //�����տ��ί�д���
            if ((sSourceBillType != null)
                && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
              //��Դ�����۷�Ʊ
              this.doWithSaleBillsFromFaPiao(sSourceModuleName, btvo, ConstVO.ADD_STATUS, cbo);
            }
          }
        }
      }
    } catch (Exception e) {
      //����Ѿ������ݷ����˵��ݺ����˻���
      ArrayList alBillCodes = new ArrayList();
      alBillCodes.add(sBillCode);
      this.returnBillCode(alBillCodes, bill);

      throw e;
    }
    return bill;
  }

  /**
   * ��������:��������
   * 
   * ����: BillVO bill ----- ����
   * 
   * ����ֵ:����
   * 
   * �쳣:
   *  
   */
  public BillVO insertForAccount(ClientLink cl, BillVO bill, String sFQSK,
      String sWTDX) throws BusinessException {
    BillVO bvo = null;

    try {
      CommonDataImpl cbo = new CommonDataImpl();
      
      //�����־
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
        //�Ǽƻ��۵�����
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
   * ��������:�������ݣ��ڳ����ݣ���������ʵʱƾ֤
   * 
   * ����: BillVO bill ----- ����
   * 
   * ����ֵ:����
   * 
   * �쳣:
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

      //������ĩ���ʺ��ճ�¼�뵥��֮��Ĳ�������
      bAccountLocked = this.lockAccount(bill);

      boolean bFQSK = false;
      if ((bill != null) && (bill.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bill.getParentVO();

        //���ҵ������
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
   * ��������:�ƻ��۵������У������û�����ѡȡ��������޸���ƻ���
   * 
   * ����:AddQueryVO �û�����Ĳ�ѯ����
   * 
   * ����ֵ:AddQueryVO ��ѯ�õ��Ľ����������ƻ��ۣ���׼��
   * 
   * �쳣: SQL �쳣
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
   * ��������: �ڷ����쳣ʱ���ã��˻��Ѿ�����ĵ��ݺ�
   * 
   * ����: sBillCode java.lang.String �Ѿ�����ĵ��ݺ� bill nc.vo.ia.bill.BillVO
   * ���ݣ����䵥�ݺŵĶ��� ����ֵ: ��
   * 
   * �쳣:
   * 
   * @exception BusinessException
   *              �쳣˵����
   */
  private void returnBillCode(ArrayList billCodes, BillVO bill)
      throws BusinessException {
    this.returnBillCodes(billCodes, new BillVO[] { bill });
  }

  /**
   * ��������: �ڷ����쳣ʱ���ã��˻��Ѿ�����ĵ��ݺ�(��������
   * 
   * ����: sBillCodes java.lang.String[] ��¼�Ѿ�����ĵ��ݺŵ����� bills BillVO[]
   * �������飬���䵥�ݺŵĶ��� ����ֵ:
   * 
   * �쳣:
   * 
   * @param billCodes
   *          ArrayList
   * @exception nc.vo.pub.BusinessException
   *              �쳣˵����
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
        //������Ǵ˴η����������
        if (!billCodes.contains(bills[i].getVBillCode())) {
          continue;
        }

        //�˻�ϵͳ����ĵ��ݺ�
        //bcDmo.returnBillCode(bills[i]);

        //ɾ��VO�ı�ͷ�������Ѿ�����ĵ��ݺ�
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
   * ��������:�ö���������뵥��
   * 
   * ����: BillVO[] bvos ----- ������Ϣ Hashtable ht ----- �����Ϣ String sExistString
   * ----- �Ӳ�ѯ��䣨�������Ч�ʣ� Integer[] iPeci ----- ���ݾ��� String sPeriod ----- ����ڼ�
   * String sPeriodPerv ----- ǰһ���� String sBeginDate ----- ���¿�ʼ�� String sEndDate
   * ----- ���½����� Hashtable ht ----- ������ѯ��� Hashtable htInv ----- �����Ϣ
   * 
   * binsrtedVOs,iPeci,sSourceModule,sSimulate,htttt,svo
   * 
   * ����ֵ:
   * 
   * �쳣:
   *  
   */
  private BillVO[] saveOutterArray(ClientLink cl, BillVO[] bvos,
      Integer[] iPeci, String sSourceModuleName, String sSimulate,
      SimulateVO svo) throws Exception {
	  Log.debug("saveOutterArrayִ����");
    CommonDataImpl cbo = new CommonDataImpl();
    Timer timer  = new Timer();
    timer.start();
    ArrayList alBillCodes = new ArrayList(); //��¼���õ�VO�еĵ��ݺ�
    String sFQSK = svo.getFQSK();
    String sWTDX = svo.getWTDX();
    try {
      //��ÿ�����ݲ�������
      for (int i = 0; i < bvos.length; i++) {
        BillVO bvo = bvos[i];

        //��������
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        bhvo.setCbillid(null);

        //1�������֯

        //��õ�λ����
        String sCorpID = bhvo.getPk_corp();

        //��ÿ����֯
        String sRDID = bhvo.getCrdcenterid();
        if ((sRDID == null) || (sRDID.trim().length() == 0)) {//��if�ж��ں�Ʊ���ʱ��ִ��
          BusinessException e = new BusinessException(
              nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("20143010",
                  "UPP20143010-000025")/* @res "�޷���ÿ����֯�������" */);
          throw e;
        }

        //2�����ݺ�

        //��õ�������
        String sBillType = bhvo.getCbilltypecode();

        //���õ��ݺţ���ͷ����)
        alBillCodes.add(this.setBillCode(bvo));
        timer.addExecutePhase("������㵥�ݱ���  ��ȡ���ݺ�");

        //���ñ��嵥λ���롢�������͡����ݺš�������
        for (int j = 0; j < bvo.getChildrenVO().length; j++) {
          BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[j];

          btvo.setCbill_bid(null);
          btvo.setCbillid(null);
          btvo.setPk_corp(sCorpID);
        }

        //����շ���־
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

        //�Ϸ��Լ��
        //checkDataforOutterBill(bIsOut, bvo, ConstVO.ADD_STATUS, htInv, bFQSK,
        //    iPeci);
        IBillCheck check = new CheckerForIA(); 
        bvo = check.checkForSave(new BillVO[]{bvo})[0];
        timer.addExecutePhase("������㵥�ݱ��� ����ǰ���");

        if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
          svo.setBills(bvos);

          //����ģ��ɱ�
          //20070317 yuanhm �ɹ��������������ע��������ɱ����㷨
         // bvos = this.doWithSimulateCost(svo, ConstVO.ADD_STATUS);
        }
        //timer.addExecutePhase("������㵥�ݱ��� ����ģ��ɱ�");
        //�������˳��Ĭ��ֵ
        this.setDefaultAuditSequence(bvo);
        RichDMO rDmo = new RichDMO();
        //Ϊ����Ͼ�������ǩ���ڹ�������Ч�����ſ����һ����������Ϊfalse��
        rDmo.model2_CreateBatch(cl, new BillVO[] { bvo }, ((BillHeaderVO) bvo
            .getParentVO()).getVOMeta().getPkColName(), null, null, false);
        timer.addExecutePhase("������㵥�ݱ��� ����");
      }

      //���⴦��
      for (int i = 0; i < bvos.length; i++) {
        BillVO bill = bvos[i];

        String sBillType = ((BillHeaderVO) bill.getParentVO())
            .getCbilltypecode();

        //�жϷ����տ�
        boolean bFQSK = false;

        String sBizTypeID = ((BillHeaderVO) bill.getParentVO()).getCbiztypeid();

        if (sBizTypeID != null) {
          if ((sFQSK.indexOf(sBizTypeID) != -1)
              || (sWTDX.indexOf(sBizTypeID) != -1)) {
            bFQSK = true;
          }
        }

        if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          //�����۳ɱ���ת��
          for (int j = 0; j < bill.getChildrenVO().length; j++) {
            BillItemVO btvo = (BillItemVO) bill.getChildrenVO()[j];
            String sSourceBillType = btvo.getCsourcebilltypecode();

            if (bFQSK) {
              //�����տ��ί�д���
              if ((sSourceBillType != null)
                  && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
                //��Դ�����۷�Ʊ
                this.doWithSOFaPiao(btvo, ConstVO.ADD_STATUS,cbo);
              }
            }
          }
        }
      }
      timer.addExecutePhase("������㵥�ݱ��� ���е��ݱ����������");
      
      timer.showAllExecutePhase("������㵥�ݱ���");

    } catch (Exception e) {
      //����Ѿ������˾��˻ص��ݺ�
      this.returnBillCodes(alBillCodes, bvos);
      throw e;
    }
    return bvos;
  }

  /**
   * �������˳���Ĭ��ֵ�����Ϊ����Ϊ-1������������߳ɱ�����ʱ��ѯ���ٶ�
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
   * ��������:Ϊһ������VO���õ��ݺţ��������ݵı�ͷ�ͱ���
   * 
   * ����: voBill --���ݣ�
   * 
   * ����ֵ:String �ɹ����õĵ��ݺţ� �����null��ʾû�б���ȷ�����õ��ݺ�
   * 
   * �쳣: setBillCodes�������׳�BusinessException
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
   * ��������:Ϊһ�鵥��VO���õ��ݺţ�����ÿ�ŵ��ݵı�ͷ�ͱ���
   * 
   * ����: voBills --�������飻
   * 
   * ����ֵ:String ��������ÿһ��Ԫ�ض�Ϊ���óɹ��ĵ��ݺţ� �����null��ʾ��Ԫ�ض�Ӧ�ĵ���û�б���ȷ�����õ��ݺ�
   * 
   * �쳣: sBillCode[i] = bcDmo.setBillCode(voBills[i]);���׳�BusinessException�쳣
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
    //ȷ��voBills��û�п�Ԫ��
    for (int i = 0; i < voBills.length; i++) {
      if (voBills[i] == null) {
        return null;
      }
    }

    //��ȡ���ݺŲ�����vo��
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
   * �㷨����ĩ������������¼���� ����ĩ����ʱ�����Զ���ĩ���ʼ�����������ɹ�˵���е�������¼�룬��Ⱥ�
   * ����ɹ�������������Ե���¼��������������޷���¼�뵥�� �ڵ���¼��ʱ�����Զ���ĩ���ʼ�����������ɹ�����������ĩ���ʻ򵥾�¼�룬����
   * �Ե���¼�����������ɹ�˵������������¼���ڽ����У�������ɹ�˵����ĩ����
   * �ڽ����У��Ե���¼��ɹ�������Ҫ�����ͷŸ�������ʹ��������¼���߳̿���ͬʱ���� ��������:���Ƶ���¼�����ĩ����֮��Ĳ���
   * 
   * 
   * ����:String[] passAccount --- ������ĩ���ʵĲ����� LockBOAccess lbo --- ���м��������Ķ���
   * BillVO bill --- ����¼��ĵ��� ����ֵ:boolean --- �Ƿ��ڱ���������������ĩ���ʣ�true ---
   * �ڵ��ñ��������������Ҫ�ͷŸ��� fasle --- ��ĩ�������������ط��������ģ������ͷ�
   * 
   * �쳣:
   *  
   */
  private boolean lockAccount(BillVO bill) throws BusinessException {
    //�����ݣ�����ĩ����
    //m_sAccountLock = "ACCOUNT" --- ��ĩ���ʵĲ�������־
    String[] passAccount = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp()
        + ConstVO.m_sAccountLock };

    //m_sBillAdd = "BILLADD" --- ���ӵ��ݵĲ�������־
    String[] passAddBill = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp()
        + ConstVO.m_sBillAdd };

    String sOperID = ((BillHeaderVO) bill.getParentVO()).getCoperatorid();

    //�ȳ���������ĩ������
    boolean bAccountLocked = LockUtils.lock(passAccount, sOperID);

    if (!bAccountLocked) {
      //����������ĩ���ʣ�Ҳ�����ǵ���¼����ס��
      //������������¼����
      boolean bCanLockAddBill = LockUtils.lock(passAddBill, sOperID);

      if (!bCanLockAddBill) {
        //����������¼�룬˵�������ڽ�����ĩ���ʻ������û��ĵ���¼������û���ü��ͷ�
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("20143010", "UPP20143010-000023")/*
                                                          * @res
                                                          * "��ص������ڽ��д������Ժ�����"
                                                          */);
      } else {
        //�ǵ���¼�����������ĩ�����������Լ���¼�뵥�ݣ���Ҫ������¼�����ͷţ�ʹ��һ��������ʱ����������¼����
        LockUtils.unLock(passAddBill, sOperID);
      }
    }
    return bAccountLocked;
  }

  /**
   * �������ܣ��ͷ���ĩ������
   * 
   * @param bill
   *          Ӧ���Ǽ���ʱʹ�õ�bill
   * @throws BusinessException
   */
  private void releaseAccountLock(BillVO bill) throws BusinessException {
    String[] passAccount = new String[] { ((BillHeaderVO) bill.getParentVO())
        .getPk_corp() + ConstVO.m_sAccountLock };
    String sOperID = ((BillHeaderVO) bill.getParentVO()).getCoperatorid();
    
    LockUtils.unLock(passAccount, sOperID);
    
  }

  /**
   * ��������:�޸�����
   * 
   * ����: BillVO voCurBill ----- ��ǰ���� BillVO bvo ----- Ҫ�޸ĵĵ��� boolean bIsOut
   * ----- �Ƿ��ǳ��ⵥ boolean bIsAdjustBill ----- �Ƿ��ǵ����� String sUserID ----- ����Աid
   * 
   * ����ֵ:����
   * 
   * �쳣:
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

      //������
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
                                                                        * "��ص������ڽ��д������Ժ�����"
                                                                        */);

        throw e;
      }

      boolean bFQSK = false;
      if ((bvo != null) && (bvo.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        //���ҵ������
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
            .getStrByID("20143010", "UPP20143010-000010")/* @res "�ͷ�ҵ��������" */);
      }
    }
    return voCurBill;
  }

  /**
   * ��������:��������
   * 
   * ����: BillVO voCurBill ----- ��ǰ���� BillVO bvo ----- Ҫ�޸ĵĵ��� boolean bIsOut
   * ----- �Ƿ��ǳ��ⵥ boolean bIsAdjustBill ----- �Ƿ��ǵ����� String sUserID ----- ����Աid
   * boolean bIsFQWT ----- �Ƿ�����տ��ί�д��� String sSimulate ----- �Ƿ��������ɱ�
   * 
   * ����ֵ:����
   * 
   * �쳣:
   *  
   */
  private BillVO updateBill(ClientLink cl, BillVO voCurBill, BillVO bvo,
      boolean bIsOut, boolean bIsAdjustBill, String sUserID, boolean bIsFQWT,
      String sSimulate) throws Exception {
    //���ҵ������
    String sDate = ((BillHeaderVO) bvo.getParentVO()).getDbilldate().toString();

    if ((sSimulate == null) || sSimulate.equalsIgnoreCase("Y")) {
      //����ģ��ɱ�
      SimulateVO svo = new SimulateVO();
      svo.setBills(new BillVO[] { bvo });
      bvo = this.doWithSimulateCost(svo, ConstVO.UPDATE_STATUS)[0];
    }

    //���޸����ݽ��кϷ��Լ��
    if (this.checkUpdateData(bvo, voCurBill) == false) {
      return voCurBill;
    }

    //�Ϸ��Լ��
    this.checkData(voCurBill, bvo, bIsOut, bIsAdjustBill, false,
        ConstVO.UPDATE_STATUS, sDate);

    //�������ݿ⣬�Ȳ��룬���޸�
    this.execUpdate(cl, voCurBill, bvo);

    //�޸ĺ��嵥����Ϊ��ɾ����Ĵ�����ԭ�����������ϵ��ۼƻس������ָ�
    this.doWithRedBill(voCurBill, ConstVO.DELETE_STATUS);

    //������̶��ʲ��ӿڣ���Ϊ��ɾ����Ĵ���
    CommonDataImpl cbo = new CommonDataImpl();
    //20050527_�رմ����̶��ʲ��ӿ�
    //	UFBoolean bStart =
    // cbo.isModuleStarted(((BillHeaderVO)bvo.getParentVO()).getPk_corp(),ConstVO.m_sModuleCodeFA,cl.getAccountYear()
    // + "-" + cl.getAccountMonth());
    //
    //	if (bIsAdjustBill == false && bStart.booleanValue())
    //	{
    //		doWithFa(voCurBill,bvo,bIsOut,ConstVO.DELETE_STATUS);
    //	}

    //������Դ��Ʊ�����۳ɱ���ת��
    String sBillType = ((BillHeaderVO) voCurBill.getParentVO())
        .getCbilltypecode();
    

    if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      //�����۳ɱ���ת��
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        String sSourceModuleName = ((BillHeaderVO) bvo.getParentVO())
            .getCsourcemodulename();

        if ((sSourceBillType != null)
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          //��Դ�����۷�Ʊ

          //���ԭ���У�ɾ��ԭ����
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

    //���µ�ǰVO������
    voCurBill.setParentVO(bvo.getParentVO());

    Vector vTemp = new Vector(1, 1);
    BillItemVO[] newbtvos = null;

    //��ԭ����������Vector
    for (int i = 0; i < voCurBill.getChildrenVO().length; i++) {
      vTemp.addElement((BillItemVO) voCurBill.getChildrenVO()[i]);
    }

    if ((bvo.getChildrenVO() != null) && (bvo.getChildrenVO().length != 0)) {
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        //�µķ�¼VO
        BillItemVO updatebtvo = (BillItemVO) bvo.getChildrenVO()[i];

        //��õ���״̬
        int iStatus = updatebtvo.getStatus();

        if (iStatus == nc.vo.pub.VOStatus.NEW) {
          //����
          if (updatebtvo.getIrownumber().intValue() < vTemp.size()) {
            vTemp.insertElementAt(updatebtvo, updatebtvo.getIrownumber()
                .intValue() - 1);
          }
          else {
            vTemp.addElement(updatebtvo);
          }
        } else {
          //���޸Ļ�ɾ��
          int iLength = vTemp.size();
          for (int j = 0; j < iLength; j++) {
            BillItemVO btvo = (BillItemVO) vTemp.elementAt(j);

            if (btvo.getCbill_bid().equals(updatebtvo.getCbill_bid())) {
              if (iStatus == nc.vo.pub.VOStatus.UPDATED) {
                //�޸�
                vTemp.setElementAt(updatebtvo, j);
              } else if (iStatus == nc.vo.pub.VOStatus.DELETED) {
                //ɾ��
                vTemp.removeElementAt(j);
                j = j - 1;
              }
              break;
            }
          }
        }
      }
    }

    //�������¼VO���飬���к�����
    newbtvos = new BillItemVO[vTemp.size()];
    vTemp.copyInto(newbtvos);

    voCurBill.setChildrenVO(newbtvos);

    //�޸ĺ��嵥����Ϊ�����Ӻ�Ĵ�����ԭ�����������ϵ��ۼƻس���������
    this.doWithRedBill(voCurBill, ConstVO.ADD_STATUS);

    //������̶��ʲ��ӿڣ���Ϊ�����Ӻ�Ĵ���//20050527_�رմ����̶��ʲ��ӿ�
    //	if (bIsAdjustBill == false && bStart.booleanValue())
    //	{
    //		doWithFa(voCurBill,bvo,bIsOut,ConstVO.ADD_STATUS);
    //	}

    //������Դ��Ʊ�����۳ɱ���ת��
    if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      //�����۳ɱ���ת��
      for (int i = 0; i < bvo.getChildrenVO().length; i++) {
        BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        String sSourceModuleName = ((BillHeaderVO) bvo.getParentVO())
            .getCsourcemodulename();

        if (bIsFQWT) {
          if ((sSourceBillType != null)
              && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
            //��Դ�����۷�Ʊ
            this.doWithSaleBillsFromFaPiao(sSourceModuleName, btvo, ConstVO.ADD_STATUS, cbo);
          }
        }
      }
    }

    return voCurBill;
  }

  /**
   * ��������:�޸��ڳ��������ݣ�������ʵʱƾ֤��
   * 
   * ����: BillVO voCurBill ----- ��ǰ���� BillVO bill ----- ���� String sUserID -----
   * ����Աid
   * 
   * ����ֵ:����
   * 
   * �쳣:
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

      //������
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
                                                                        * "��ص������ڽ��д������Ժ�����"
                                                                        */);
        throw e;
      }

      boolean bFQSK = false;
      if ((bvo != null) && (bvo.getParentVO() != null)) {
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();

        //���ҵ������
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
            .getStrByID("20143010", "UPP20143010-000010")/* @res "�ͷ�ҵ��������" */);
      }
    }
    return voCurBill;
    
  }

	public void deleteBillsByIABillIds(String[] headIDs, String sCorpID, String sUserID) throws BusinessException {
		// TODO �Զ����ɷ������
		this.deleteIABillsByIAPks(headIDs, sCorpID,  sUserID);
	}

	public void deleteBillsBySourceIds(String[] sSourceHIDs, String sCorpID, String sUserID) throws BusinessException {
		// TODO �Զ����ɷ������
		this.deleteBillFromOutterArray(sSourceHIDs,sCorpID,sUserID);
	}

	public void deleteItemsBySourceItemIds(String[] sSourceBIDs, String sUserID) throws BusinessException {
		// TODO �Զ����ɷ������
		this.deleteBillItemForPUs(sSourceBIDs,sUserID);
	}

	public Object saveBills(BillVO[] bvos, String sSourceModuleName, String sSourceBillType) throws BusinessException {
		// TODO �Զ����ɷ������
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