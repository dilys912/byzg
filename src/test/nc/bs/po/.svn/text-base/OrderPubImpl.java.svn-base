package nc.bs.po;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.po.base.OrderstatusDMO;
import nc.bs.po.status.OrderBbDMO;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.para.SysInitDMO;
import nc.vo.po.OrderstatusVO;
import nc.vo.pu.exception.RwtRcToPoException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.excp.RwtIcToPoException;

public class OrderPubImpl
{
  public void validateRewriteNum(String sPk_corp, String sBizType, String sTableName, String sNumField, String[] saBId_or_BBId, boolean bUserConfirm)
    throws BusinessException
  {
    String sPoPresKind = null;
    UFDouble dPoPresValue = null;

    String sRcPresKind = null;
    UFDouble dRcPresValue = null;

    int iUpStatus = -1;

    boolean bOrderBTable = sTableName.equals("po_order_b");

    Hashtable htPoNum = null;
    HashMap hmapIdAndOnWay = null;
    OrderstatusVO voBizStatus = null;
    try
    {
      if (sBizType != null) {
        voBizStatus = new OrderstatusDMO().queryStatusVOByBizTypeID(sBizType, sPk_corp);
        if (voBizStatus == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000202"));
        }
      }

      if ((bOrderBTable) && ((sNumField.equals("naccumarrvnum")) || (sNumField.equals("naccumstorenum"))))
      {
        iUpStatus = voBizStatus.getUpStatus(sNumField.equals("naccumarrvnum") ? 7 : 8, true);

        if ((iUpStatus >= 3) && (iUpStatus <= 6))
        {
          hmapIdAndOnWay = new OrderBbDMO().queryHashMapBId_OnWayNum(saBId_or_BBId, iUpStatus - 3);
        }

      }

      if (!sNumField.equals("nbackstorenum"))
      {
        SysInitDMO dmoInit = new SysInitDMO();
        sPoPresKind = dmoInit.getParaString(sPk_corp, "PO02");

        if ((sPoPresKind.equals("不保存")) || (sPoPresKind.equals("提示")))
        {
          dPoPresValue = dmoInit.getParaDbl(sPk_corp, "PO03");
          dPoPresValue = PuPubVO.getUFDouble_NullAsZero(dPoPresValue).div(100.0D);
        }

        sRcPresKind = dmoInit.getParaString(sPk_corp, "PO40");

        if ((sRcPresKind.equals("不保存")) || (sRcPresKind.equals("提示")))
        {
          dRcPresValue = dmoInit.getParaDbl(sPk_corp, "PO41");
          dRcPresValue = PuPubVO.getUFDouble_NullAsZero(dRcPresValue).div(100.0D);
        }

      }

      PubDMO dmoPuPub = new PubDMO();
      htPoNum = dmoPuPub.queryHtResultFromAnyTable(sTableName, bOrderBTable ? "corder_bid" : "corder_bb1id", new String[] { "nordernum", "naccumarrvnum", "naccumstorenum", "nbackarrvnum", "nbackstorenum" }, saBId_or_BBId);

      UFDouble dTemp = null;
      Object[] oaNum = null;
      UFDouble dOrderNum = null;
      UFDouble dRcNum = null;
      UFDouble dIcNum = null;
      UFDouble dBackRcNum = null;
      UFDouble dBackIcNum = null;
      UFDouble dOnWayNum = null;
      int iLen = saBId_or_BBId.length;
      for (int i = 0; i < iLen; i++) {
        Vector vecData = (Vector)htPoNum.get(saBId_or_BBId[i]);
        if (vecData == null) {
          continue;
        }
        oaNum = (Object[])(Object[])vecData.get(0);
        dOrderNum = PuPubVO.getUFDouble_NullAsZero(oaNum[0]);
        dRcNum = PuPubVO.getUFDouble_NullAsZero(oaNum[1]);
        dIcNum = PuPubVO.getUFDouble_NullAsZero(oaNum[2]);
        dBackRcNum = PuPubVO.getUFDouble_NullAsZero(oaNum[3]);
        dBackIcNum = PuPubVO.getUFDouble_NullAsZero(oaNum[4]);

        if (sNumField.equals("naccumarrvnum"))
        {
          dTemp = dRcNum.multiply(dOrderNum);
          if (dTemp.compareTo(VariableConst.ZERO) < 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000228"));
          }

          if (dBackRcNum.compareTo(dRcNum) > 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000229"));
          }

          if (sPoPresKind.equals("不保存")) {
            dTemp = dOrderNum.multiply(dPoPresValue.add(1.0D));
            if (dRcNum.compareTo(dTemp) > 0)
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000230"));
          }
          else if ((sPoPresKind.equals("提示")) && (!bUserConfirm)) {
            dTemp = dOrderNum.multiply(dPoPresValue.add(1.0D));
            if (dRcNum.compareTo(dTemp) > 0) {
              throw new RwtRcToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000268"));
            }
          }

          if (sRcPresKind.equals("不保存")) {
            dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
            if (dIcNum.compareTo(dTemp) > 0)
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000231"));
          }
          else if ((sRcPresKind.equals("提示")) && (!bUserConfirm)) {
            dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
            if (dIcNum.compareTo(dTemp) > 0) {
              throw new RwtRcToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000269"));
            }

          }

          if ((bOrderBTable) && (iUpStatus >= 3)) {
            dOnWayNum = VariableConst.ZERO;
            if (hmapIdAndOnWay != null) {
              dOnWayNum = PuPubVO.getUFDouble_NullAsZero(hmapIdAndOnWay.get(saBId_or_BBId[i]));
            }
            if (dRcNum.compareTo(dOnWayNum) > 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000232") + OrderstatusVO.getStatusNameByValue(iUpStatus + 3) + "");
            }

          }

        }
        else if (sNumField.equals("nbackarrvnum"))
        {
          if (dBackRcNum.compareTo(VariableConst.ZERO) < 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000233"));
          }
          if (dOrderNum.compareTo(VariableConst.ZERO) > 0)
          {
            if (dBackRcNum.compareTo(dRcNum) > 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000229"));
            }

            if (sRcPresKind.equals("不保存")) {
              dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0)
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000234"));
            }
            else if ((sRcPresKind.equals("提示")) && (!bUserConfirm)) {
              dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0) {
                throw new RwtRcToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000270"));
              }
            }
          }
          else
          {
            dTemp = dOrderNum.multiply(-1.0D);
            if (dTemp.compareTo(dBackRcNum.add(dBackIcNum)) < 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000235"));
            }

          }

        }
        else if (sNumField.equals("naccumstorenum"))
        {
          dTemp = dIcNum.multiply(dOrderNum);
          if (dTemp.compareTo(VariableConst.ZERO) < 0) {
            PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000236")));
          }

          if (dBackIcNum.compareTo(dIcNum) > 0) {
            PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000237")));
          }

          if ((bOrderBTable) && (iUpStatus >= 3) && (iUpStatus <= 6))
          {
            dOnWayNum = VariableConst.ZERO;
            if (hmapIdAndOnWay != null) {
              dOnWayNum = PuPubVO.getUFDouble_NullAsZero(hmapIdAndOnWay.get(saBId_or_BBId[i]));
            }
            if (dIcNum.compareTo(dOnWayNum) > 0) {
              PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000238", null, new String[] { OrderstatusVO.getStatusNameByValue(iUpStatus + 3) })));
            }
          }

          if (voBizStatus.isArrive())
          {
            if (sRcPresKind.equals("不保存")) {
              dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0) {
                PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000239")));
              }

            }
            else if ((sRcPresKind.equals("提示")) && (!bUserConfirm)) {
              dTemp = dRcNum.sub(dBackRcNum).multiply(dRcPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0) {
                PubDMO.throwBusinessException(new RwtIcToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000272")));
              }

            }

          }
          else
          {
            if (sPoPresKind.equals("不保存")) {
              dTemp = dOrderNum.multiply(dPoPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0) {
                PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000240")));
              }

            }

            if ((sPoPresKind.equals("提示")) && (!bUserConfirm)) {
              dTemp = dOrderNum.multiply(dPoPresValue.add(1.0D));
              if (dIcNum.compareTo(dTemp) > 0) {
                PubDMO.throwBusinessException(new RwtIcToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000271")));
              }
            }

          }

        }
        else
        {
          if (!sNumField.equals("nbackstorenum"))
            continue;
          if (dBackIcNum.compareTo(VariableConst.ZERO) < 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000241"));
          }
          if (dOrderNum.compareTo(VariableConst.ZERO) > 0)
          {
            if (dBackIcNum.compareTo(dIcNum) > 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000242"));
            }
          }
          else
          {
            dTemp = dOrderNum.multiply(-1.0D);
            if (dTemp.compareTo(dBackRcNum.add(dBackIcNum)) < 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000243"));
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
  }
}