package nc.bs.pq;

import java.util.Vector;
import nc.bs.pu.pub.PubDMO;
import nc.itf.pq.IEstSta;
import nc.vo.pq.EstStatVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class EstStaImpl
  implements IEstSta
{
  private EstStatVO[] combineVO4(EstStatVO[] VOs1, EstStatVO[] VOs2, EstStatVO[] VOs3, EstStatVO[] VOs4)
  {
    EstStatVO[] returnVO = (EstStatVO[])null;
    if ((VOs1 != null) && (VOs1.length > 0)) {
      returnVO = VOs1;
      if ((VOs2 != null) && (VOs2.length > 0)) returnVO = combineVOForVMIInSettle(returnVO, VOs2);
      if ((VOs3 != null) && (VOs3.length > 0)) returnVO = combineVOForVMIInEst(returnVO, VOs3);
      if ((VOs4 != null) && (VOs4.length > 0)) returnVO = combineVOForVMIEstNoSettle(returnVO, VOs4);
      return returnVO;
    }

    if (returnVO == null) {
      if ((VOs2 != null) && (VOs2.length > 0)) {
        returnVO = VOs2;
      }
      if ((VOs3 != null) && (VOs3.length > 0)) returnVO = combineVOForVMIInEst(returnVO, VOs3);
      if ((VOs4 != null) && (VOs4.length > 0)) returnVO = combineVOForVMIEstNoSettle(returnVO, VOs4);
      return returnVO;
    }

    if (returnVO == null) {
      if ((VOs3 != null) && (VOs3.length > 0)) {
        returnVO = VOs3;
      }
      if ((VOs4 != null) && (VOs4.length > 0)) returnVO = combineVOForVMIEstNoSettle(returnVO, VOs4);
      return returnVO;
    }

    if (returnVO == null) {
      if ((VOs4 != null) && (VOs4.length > 0)) {
        returnVO = VOs4;
      }
      return returnVO;
    }
    return null;
  }

  private EstStatVO[] combineVO1(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return null;

    for (int i = 0; i < VOs1.length; i++) {
      boolean b = false;

      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";

      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";
      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdInEstNum();
      if (num1 == null) num1 = new UFDouble(0);
      UFDouble mny1 = VOs1[i].getdInEstMny();
      if (mny1 == null) mny1 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";

        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdInEstNum();
        if (num2 == null) num2 = new UFDouble(0);
        UFDouble mny2 = VOs2[j].getdInEstMny();
        if (mny2 == null) mny2 = new UFDouble(0);

        if ((!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() - num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() - mny2.doubleValue());
        b = true;
      }

      if (b) {
        VOs1[i].setdInEstNum(num1);
        VOs1[i].setdInEstMny(mny1);
      }
    }

    return VOs1;
  }

  private EstStatVO[] combineVO3(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return null;

    for (int i = 0; i < VOs1.length; i++) {
      boolean b = false;

      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";
      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";

      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdEstNoSettleNum();
      if (num1 == null) num1 = new UFDouble(0);

      UFDouble mny1 = VOs1[i].getdEstNoSettleMny();
      if (mny1 == null) mny1 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++)
      {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";
        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdEstSettleNum();
        if (num2 == null) num2 = new UFDouble(0);

        UFDouble mny2 = VOs2[j].getdEstEstMny();
        if (mny2 == null) mny2 = new UFDouble(0);

        if ((!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() - num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() - mny2.doubleValue());
        b = true;
      }

      if (b) {
        VOs1[i].setdEstNoSettleNum(num1);
        VOs1[i].setdEstNoSettleMny(mny1);
      }
    }

    return VOs1;
  }

  private EstStatVO[] combineVO2(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return null;

    for (int i = 0; i < VOs1.length; i++) {
      boolean b = false;

      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";
      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";

      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdEstNoSettleNum();
      if (num1 == null) num1 = new UFDouble(0);

      UFDouble mny1 = VOs1[i].getdEstNoSettleMny();
      if (mny1 == null) mny1 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";
        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";

        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdEstNoSettleNum();
        if (num2 == null) num2 = new UFDouble(0);

        UFDouble mny2 = VOs2[j].getdEstNoSettleMny();
        if (mny2 == null) mny2 = new UFDouble(0);

        if ((!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() - num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() - mny2.doubleValue());
        b = true;
      }

      if (b) {
        VOs1[i].setdEstNoSettleNum(num1);
        VOs1[i].setdEstNoSettleMny(mny1);
      }
    }

    return VOs1;
  }

  private EstStatVO[] combineVOForVMIEstNoSettle(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return VOs2;

    boolean[] bSelected = new boolean[VOs2.length];
    for (int i = 0; i < VOs2.length; i++) bSelected[i] = false;

    for (int i = 0; i < VOs1.length; i++) {
      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";

      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";
      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdEstNoSettleNum();
      if (num1 == null) num1 = new UFDouble(0);
      UFDouble mny1 = VOs1[i].getdEstNoSettleMny();
      if (mny1 == null) mny1 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";

        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdEstNoSettleNum();
        if (num2 == null) num2 = new UFDouble(0);
        UFDouble mny2 = VOs2[j].getdEstNoSettleMny();
        if (mny2 == null) mny2 = new UFDouble(0);

      //edit by yqq 20170113     
        if ((!bSelected[j]) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
 //       if ((bSelected[j] != 0) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() + num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() + mny2.doubleValue());
        bSelected[j] = true;
      }

      VOs1[i].setdEstNoSettleNum(num1);
      VOs1[i].setdEstNoSettleMny(mny1);
    }

    Vector vTemp = new Vector();
    for (int i = 0; i < VOs1.length; i++) vTemp.addElement(VOs1[i]);
    for (int i = 0; i < VOs2.length; i++) {
    	
    	//edit by yqq 20170113
        if(!bSelected[i]) vTemp.addElement(VOs2[i]);
  //    if (bSelected[i] != 0) continue; vTemp.addElement(VOs2[i]);
    }

    EstStatVO[] VOs = new EstStatVO[vTemp.size()];
    vTemp.copyInto(VOs);
    return VOs;
  }

  private EstStatVO[] combineVOForVMIEstSettle(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return VOs2;

    boolean[] bSelected = new boolean[VOs2.length];
    for (int i = 0; i < VOs2.length; i++) bSelected[i] = false;

    for (int i = 0; i < VOs1.length; i++) {
      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";

      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";
      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdEstSettleNum();
      if (num1 == null) num1 = new UFDouble(0);
      UFDouble mny1 = VOs1[i].getdEstSettleMny();
      if (mny1 == null) mny1 = new UFDouble(0);
      UFDouble mny11 = VOs1[i].getdEstEstMny();
      if (mny11 == null) mny11 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";

        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdEstSettleNum();
        if (num2 == null) num2 = new UFDouble(0);
        UFDouble mny2 = VOs2[j].getdEstSettleMny();
        if (mny2 == null) mny2 = new UFDouble(0);
        UFDouble mny22 = VOs2[j].getdEstEstMny();
        if (mny22 == null) mny22 = new UFDouble(0);

      //edit by yqq 20170113
        if ((!bSelected[j]) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
     //   if ((bSelected[j] != 0) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() + num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() + mny2.doubleValue());
        mny11 = new UFDouble(mny11.doubleValue() + mny22.doubleValue());
        bSelected[j] = true;
      }

      VOs1[i].setdEstSettleNum(num1);
      VOs1[i].setdEstSettleMny(mny1);
      VOs1[i].setdEstEstMny(mny11);
    }

    Vector vTemp = new Vector();
    for (int i = 0; i < VOs1.length; i++) vTemp.addElement(VOs1[i]);
    for (int i = 0; i < VOs2.length; i++) {
    	
    	//edit by yqq 20170113
        if (!bSelected[i]) continue; vTemp.addElement(VOs2[i]);
   //   if (bSelected[i] != 0) continue; vTemp.addElement(VOs2[i]);
    }

    EstStatVO[] VOs = new EstStatVO[vTemp.size()];
    vTemp.copyInto(VOs);
    return VOs;
  }

  private EstStatVO[] combineVOForVMIInEst(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return VOs2;

    boolean[] bSelected = new boolean[VOs2.length];
    for (int i = 0; i < VOs2.length; i++) bSelected[i] = false;

    for (int i = 0; i < VOs1.length; i++) {
      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";

      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";
      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdInEstNum();
      if (num1 == null) num1 = new UFDouble(0);
      UFDouble mny1 = VOs1[i].getdInEstMny();
      if (mny1 == null) mny1 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";

        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdInEstNum();
        if (num2 == null) num2 = new UFDouble(0);
        UFDouble mny2 = VOs2[j].getdInEstMny();
        if (mny2 == null) mny2 = new UFDouble(0);

        
      //edit by yqq 20170113
        if ((!bSelected[j]) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
      //  if ((bSelected[j] != 0) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() + num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() + mny2.doubleValue());
        bSelected[j] = true;
      }

      VOs1[i].setdInEstNum(num1);
      VOs1[i].setdInEstMny(mny1);
    }

    Vector vTemp = new Vector();
    for (int i = 0; i < VOs1.length; i++) vTemp.addElement(VOs1[i]);
    for (int i = 0; i < VOs2.length; i++) {
    	
    	//edit by yqq 20170113
        if (!bSelected[i]) continue; vTemp.addElement(VOs2[i]);
    //  if (bSelected[i] != 0) continue; vTemp.addElement(VOs2[i]);
    }

    EstStatVO[] VOs = new EstStatVO[vTemp.size()];
    vTemp.copyInto(VOs);
    return VOs;
  }

  private EstStatVO[] combineVOForVMIInSettle(EstStatVO[] VOs1, EstStatVO[] VOs2)
  {
    if ((VOs1 == null) || (VOs1.length == 0)) return VOs2;

    boolean[] bSelected = new boolean[VOs2.length];
    for (int i = 0; i < VOs2.length; i++) bSelected[i] = false;

    for (int i = 0; i < VOs1.length; i++) {
      String sVendorName1 = VOs1[i].getcvendorname();
      if (sVendorName1 == null) sVendorName1 = "";
      String sBodyName1 = VOs1[i].getcbodyname();
      if (sBodyName1 == null) sBodyName1 = "";
      String sDeptName1 = VOs1[i].getcdeptname();
      if (sDeptName1 == null) sDeptName1 = "";
      String sPsnName1 = VOs1[i].getcpsnname();
      if (sPsnName1 == null) sPsnName1 = "";
      String sInvClName1 = VOs1[i].getinvclassname();
      if (sInvClName1 == null) sInvClName1 = "";
      String sInvCode1 = VOs1[i].getcinvcode();
      if (sInvCode1 == null) sInvCode1 = "";
      String sInvName1 = VOs1[i].getcinvname();
      if (sInvName1 == null) sInvName1 = "";

      String sInvSpec1 = VOs1[i].getcinvspec();
      if (sInvSpec1 == null) sInvSpec1 = "";
      String sInvType1 = VOs1[i].getcinvtype();
      if (sInvType1 == null) sInvType1 = "";
      String sWarehouseName1 = VOs1[i].getcwarehouseid();
      if (sWarehouseName1 == null) sWarehouseName1 = "";
      String sWarehouseMan1 = VOs1[i].getcwhsmanagerid();
      if (sWarehouseMan1 == null) sWarehouseMan1 = "";

      UFDouble num1 = VOs1[i].getdInSettleNum();
      if (num1 == null) num1 = new UFDouble(0);
      UFDouble mny1 = VOs1[i].getdInSettleMny();
      if (mny1 == null) mny1 = new UFDouble(0);
      UFDouble mny11 = VOs1[i].getdInSettleEstMny();
      if (mny11 == null) mny11 = new UFDouble(0);

      for (int j = 0; j < VOs2.length; j++) {
        String sVendorName2 = VOs2[j].getcvendorname();
        if (sVendorName2 == null) sVendorName2 = "";
        String sBodyName2 = VOs2[j].getcbodyname();
        if (sBodyName2 == null) sBodyName2 = "";
        String sDeptName2 = VOs2[j].getcdeptname();
        if (sDeptName2 == null) sDeptName2 = "";
        String sPsnName2 = VOs2[j].getcpsnname();
        if (sPsnName2 == null) sPsnName2 = "";
        String sInvClName2 = VOs2[j].getinvclassname();
        if (sInvClName2 == null) sInvClName2 = "";
        String sInvCode2 = VOs2[j].getcinvcode();
        if (sInvCode2 == null) sInvCode2 = "";
        String sInvName2 = VOs2[j].getcinvname();
        if (sInvName2 == null) sInvName2 = "";

        String sInvSpec2 = VOs2[j].getcinvspec();
        if (sInvSpec2 == null) sInvSpec2 = "";
        String sInvType2 = VOs2[j].getcinvtype();
        if (sInvType2 == null) sInvType2 = "";
        String sWarehouseName2 = VOs2[j].getcwarehouseid();
        if (sWarehouseName2 == null) sWarehouseName2 = "";
        String sWarehouseMan2 = VOs2[j].getcwhsmanagerid();
        if (sWarehouseMan2 == null) sWarehouseMan2 = "";

        UFDouble num2 = VOs2[j].getdInSettleNum();
        if (num2 == null) num2 = new UFDouble(0);
        UFDouble mny2 = VOs2[j].getdInSettleMny();
        if (mny2 == null) mny2 = new UFDouble(0);
        UFDouble mny22 = VOs2[j].getdInSettleEstMny();
        if (mny22 == null) mny22 = new UFDouble(0);

        
      //edit by yqq 2017011
        if ((!bSelected[j]) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
      //  if ((bSelected[j] != 0) || (!sVendorName1.equals(sVendorName2)) || (!sBodyName1.equals(sBodyName2)) || 
          (!sDeptName1.equals(sDeptName2)) || (!sPsnName1.equals(sPsnName2)) || 
          (!sInvClName1.equals(sInvClName2)) || (!sInvCode1.equals(sInvCode2)) || 
          (!sInvName1.equals(sInvName2)))
          continue;
        if ((!sInvSpec1.equals(sInvSpec2)) || 
          (!sInvType1.equals(sInvType2)) || 
          (!sWarehouseName1.equals(sWarehouseName2)) || 
          (!sWarehouseMan1.equals(sWarehouseMan2))) continue;
        num1 = new UFDouble(num1.doubleValue() + num2.doubleValue());
        mny1 = new UFDouble(mny1.doubleValue() + mny2.doubleValue());
        mny11 = new UFDouble(mny11.doubleValue() + mny22.doubleValue());
        bSelected[j] = true;
      }

      VOs1[i].setdInSettleNum(num1);
      VOs1[i].setdInSettleMny(mny1);
      VOs1[i].setdInSettleEstMny(mny11);
    }

    Vector vTemp = new Vector();
    for (int i = 0; i < VOs1.length; i++) vTemp.addElement(VOs1[i]);
    for (int i = 0; i < VOs2.length; i++) {
    	
    	//edit by yqq 2017011
        if (!bSelected[i]) continue; vTemp.addElement(VOs2[i]);   	
  //    if (bSelected[i] != 0) continue; vTemp.addElement(VOs2[i]);
    }

    EstStatVO[] VOs = new EstStatVO[vTemp.size()];
    vTemp.copyInto(VOs);
    return VOs;
  }

  private EstStatVO[] getBeforeMonthEst(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }
    StringBuffer sbSql = new StringBuffer(" SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(b.ninnum) , SUM(npmoney) ");
    sbSql.append("  FROM ic_general_h a INNER JOIN");

    sbSql.append("  ic_general_b b ON a.cgeneralhid = b.cgeneralhid ");
    sbSql.append(
      "  INNER JOIN ic_general_bb3 c ON a.cgeneralhid = c.cgeneralhid and b.cgeneralbid = c.cgeneralbid");

    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = b.cinvbasid");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_deptdoc f ON a.cdptid = f.pk_deptdoc");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc g ON a.cbizid = g.pk_psndoc");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc k ON a.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
      sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_calbody i ON a.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql.indexOf("bd_cubasdoc") < 0)
      {
        sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
        sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl=h.pk_areacl");
    }

    if (wherestr != null) {
      sbSql.append("  where  ");
      sbSql.append(wherestr);

      sbSql.append(" AND  UPPER(b.bzgflag) = 'Y' AND b.pk_invoicecorp = '");
      sbSql.append(pk_corp);

      sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) < '");
      sbSql.append(month);
      sbSql.append("' AND a.dr = 0 AND b.dr = 0");
    }
    else {
      sbSql.append("  where  ");

      sbSql.append("  UPPER(b.bzgflag) = 'Y' AND a.pk_corp = '");
      sbSql.append(pk_corp);

      sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) < '");
      sbSql.append(month);
      sbSql.append("' AND a.dr = 0 AND b.dr = 0");
    }

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode = '47' ");

    sbSql.append("  GROUP BY ");
    sbSql.append(groupStr);

    StringBuffer sbSql1 = new StringBuffer(" SELECT ");
    sbSql1.append(groupStr);
    sbSql1.append(", SUM(a.noutnum-a.noutinnum) , SUM(nprice * (a.noutnum-a.noutinnum)) ");
    sbSql1.append("  FROM ic_vmi_sum a");

    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN  bd_invmandoc invman ON invman.pk_invmandoc = a.cinventoryid  ");

      sbSql1.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = invman.pk_invbasdoc");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
      sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_calbody i ON a.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql1.indexOf("bd_cubasdoc") < 0)
      {
        sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
        sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql1.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl=h.pk_areacl");
    }

    if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
      sbSql1.append("  where  ");
      sbSql1.append(wherestrVMI);

      sbSql1.append(" AND  UPPER(a.bgaugeflag) = 'Y' AND a.pk_corp = '");
      sbSql1.append(pk_corp);

      sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) < '");
      sbSql1.append(month);
      sbSql1.append("' AND a.dr = 0");
    } else {
      sbSql1.append("  where  ");

      sbSql1.append("  UPPER(a.bgaugeflag) = 'Y' AND a.pk_corp = '");
      sbSql1.append(pk_corp);

      sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) < '");
      sbSql1.append(month);
      sbSql1.append("' AND a.dr = 0");
    }
    sbSql1.append("  GROUP BY ");
    sbSql1.append(groupStr);

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getEstNoSettle(sbSql, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getEstNoSettle(sbSql1, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIEstNoSettle(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }

  private EstStatVO[] getBeforeMonthEstBeforeMonthSettle(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }
    StringBuffer sbSql = new StringBuffer(" SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(c.nsettlenum) , SUM(ngaugemny) ");
    sbSql.append("  FROM ic_general_h a INNER JOIN");

    sbSql.append("  ic_general_b b ON a.cgeneralhid = b.cgeneralhid ");
    sbSql.append(
      "  INNER JOIN po_settlebill_b c ON a.cgeneralhid = c.cstockid and b.cgeneralbid = c.cstockrow and c.dr = 0");
    sbSql.append(
      "  INNER JOIN po_settlebill dd ON dd.csettlebillid = c.csettlebillid and SUBSTRING(dd.dsettledate, 1, 7) < '");
    sbSql.append(month);
    sbSql.append("' and dd.dr = 0");

    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = b.cinvbasid");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_deptdoc f ON a.cdptid = f.pk_deptdoc");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc g ON a.cbizid = g.pk_psndoc");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc k ON a.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
      sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_calbody i ON a.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql.indexOf("bd_cubasdoc") < 0)
      {
        sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
        sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl=h.pk_areacl");
    }

    if (wherestr != null) {
      sbSql.append("  where  ");
      sbSql.append(wherestr);

      sbSql.append(" AND  UPPER(b.bzgflag) = 'Y' AND a.pk_corp = '");
      sbSql.append(pk_corp);

      sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) < '");
      sbSql.append(month);
      sbSql.append("' AND a.dr = 0 AND b.dr = 0");
    }
    else {
      sbSql.append("  where  ");

      sbSql.append("  UPPER(b.bzgflag) = 'Y' AND a.pk_corp = '");
      sbSql.append(pk_corp);

      sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) < '");
      sbSql.append(month);
      sbSql.append("' AND a.dr = 0 AND b.dr = 0");
    }

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode = '47' ");

    sbSql.append("  GROUP BY ");
    sbSql.append(groupStr);

    StringBuffer sbSql1 = new StringBuffer(" SELECT ");
    sbSql1.append(groupStr);
    sbSql1.append(", SUM(c.nsettlenum) , SUM(ngaugemny) ");
    sbSql1.append("  FROM ic_vmi_sum a");
    sbSql1.append(
      "  INNER JOIN po_settlebill_b c ON a.cvmihid = c.cvmiid and c.dr = 0");
    sbSql1.append(
      "  INNER JOIN po_settlebill dd ON dd.csettlebillid = c.csettlebillid and SUBSTRING(dd.dsettledate, 1, 7) < '");
    sbSql1.append(month);
    sbSql1.append("' and dd.dr = 0");

    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN  bd_invmandoc invman ON invman.pk_invmandoc = a.cinventoryid  ");

      sbSql1.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = invman.pk_invbasdoc");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid AND d.custflag in ('1','3')");
      sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_calbody i ON a.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql1.indexOf("bd_cubasdoc") < 0)
      {
        sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid AND d.custflag in ('1','3')");
        sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql1.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl=h.pk_areacl");
    }

    if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
      sbSql1.append("  where  ");
      sbSql1.append(wherestrVMI);

      sbSql1.append(" AND  UPPER(a.bgaugeflag) = 'Y' AND a.pk_corp = '");
      sbSql1.append(pk_corp);

      sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) < '");
      sbSql1.append(month);
      sbSql1.append("' AND a.dr = 0");
    }
    else {
      sbSql1.append("  where  ");

      sbSql1.append("  UPPER(a.bgaugeflag) = 'Y' AND a.pk_corp = '");
      sbSql1.append(pk_corp);

      sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) < '");
      sbSql1.append(month);
      sbSql1.append("' AND a.dr = 0");
    }
    sbSql1.append("  GROUP BY ");
    sbSql1.append(groupStr);

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getEstNoSettle(sbSql, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getEstNoSettle(sbSql1, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIEstNoSettle(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }

  public String getDeptbyPsn(String pk_psndoc)
    throws BusinessException
  {
    StringBuffer sql = new StringBuffer("SELECT a.deptcode");
    sql.append("  FROM bd_deptdoc a INNER JOIN");
    sql.append("  bd_psndoc b ON a.pk_deptdoc = b.pk_deptdoc");
    sql.append("  WHERE b.pk_psndoc = '");
    sql.append(pk_psndoc);
    sql.append("'");
    String deptcode = null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      deptcode = dmo.getDeptbyPsn(sql.toString());
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return deptcode;
  }

  private EstStatVO[] getEstNoSettle(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    EstStatVO[] estVOs = getBeforeMonthEst(pk_corp, month, wherestr, wherestrVMI, groupItem, b);

    EstStatVO[] tempVOs = getBeforeMonthEstBeforeMonthSettle(pk_corp, month, wherestr, wherestrVMI, groupItem, b);

    if ((tempVOs != null) && (tempVOs.length > 0)) {
      estVOs = combineVO2(estVOs, tempVOs);
    }

    return estVOs;
  }

  private EstStatVO[] getEstSettle(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }
    StringBuffer sbSql = new StringBuffer("SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(a.nsettlenum) , SUM(a.nmoney), SUM(a.ngaugemny) ");
    sbSql.append("  FROM po_settlebill_b a INNER JOIN");

    sbSql.append("  po_settlebill b ON a.csettlebillid = b.csettlebillid AND b.pk_corp = '");
    sbSql.append(pk_corp);
    sbSql.append("' AND SUBSTRING(b.dsettledate, 1, 7) = '");
    sbSql.append(month);
    sbSql.append("' AND a.dr = 0 AND b.dr = 0 ");

    StringBuffer sbSql1 = new StringBuffer(" ");
    StringBuffer sbSql2 = new StringBuffer(" ");

    sbSql1.append("  INNER JOIN ic_general_b c ON a.cstockrow = c.cgeneralbid ");
    sbSql1.append("  AND SUBSTRING(c.dzgdate, 1, 7) < '");
    sbSql1.append(month);
    sbSql1.append("'");

    sbSql1.append("  AND UPPER(c.bzgflag) = 'Y'  AND c.dr = 0 ");

    sbSql1.append("  INNER JOIN ic_general_h d ON a.cstockid = d.cgeneralhid ");
    sbSql1.append("  AND d.dr = 0 ");

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode = '47' ");

    sbSql2.append("  INNER JOIN ic_vmi_sum c ON a.cvmiid= c.cvmihid ");
    sbSql2.append("  AND SUBSTRING(c.dgaugedate, 1, 7) < '");
    sbSql2.append(month);
    sbSql2.append("'");

    sbSql2.append("  AND UPPER(c.bgaugeflag) = 'Y'  AND c.dr = 0 ");

    StringBuffer sbSql3 = new StringBuffer(" ");
    StringBuffer sbSql4 = new StringBuffer(" ");
    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_invbasdoc e ON c.cinvbasid = e.pk_invbasdoc ");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_deptdoc f ON d.cdptid = f.pk_deptdoc ");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_psndoc g ON d.cbizid = g.pk_psndoc ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_stordoc stor ON d.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_psndoc k ON d.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cproviderid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
      sbSql3.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_calbody i ON d.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql3.indexOf("bd_cubasdoc") < 0)
      {
        sbSql3.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cproviderid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
        sbSql3.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
      }
      sbSql3.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0)) {
      sbSql4.append("  LEFT OUTER JOIN bd_invmandoc invman ON c.cinventoryid = invman.pk_invmandoc");
      sbSql4.append("  LEFT OUTER JOIN bd_invbasdoc e ON invman.pk_invbasdoc = e.pk_invbasdoc ");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_cumandoc cuman ON c.cvendorid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
      sbSql4.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_calbody i ON c.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_stordoc stor ON c.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql4.indexOf("bd_cubasdoc") < 0)
      {
        sbSql4.append("  LEFT OUTER JOIN bd_cumandoc cuman ON c.cvendorid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
        sbSql4.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
      }
      sbSql4.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if ((wherestr != null) && (wherestr.trim().length() > 0)) {
      sbSql3.append("  where  ");
      sbSql3.append(wherestr);
      if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
        sbSql4.append("  where  ");
        sbSql4.append(wherestrVMI);
      }
    }
    sbSql3.append("  GROUP BY ");
    sbSql3.append(groupStr);

    sbSql4.append("  GROUP BY ");
    sbSql4.append(groupStr);

    StringBuffer str1 = new StringBuffer("");
    StringBuffer str2 = new StringBuffer("");
    str1.append(sbSql.toString());
    str2.append(sbSql.toString());
    str1.append(sbSql1.toString());
    str2.append(sbSql2.toString());
    str1.append(sbSql3.toString());
    str2.append(sbSql4.toString());

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getEstSettle(str1, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getEstSettle(str2, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIEstSettle(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }

  public EstStatVO[] getEstStatVos(String pk_corp, String month, String whereStr, Vector vgroupItem, UFBoolean[] b)
    throws BusinessException
  {
    EstStatVO[] returnVO = (EstStatVO[])null;

    String whereStrVMI = (String)vgroupItem.elementAt(vgroupItem.size() - 1);
    if ((whereStrVMI == null) || (whereStrVMI.trim().length() == 0)) whereStrVMI = " ";
    if ((whereStr == null) || (whereStr.trim().length() == 0)) whereStr = " ";

    Vector vTemp = new Vector();
    for (int i = 0; i < vgroupItem.size() - 1; i++) vTemp.addElement(vgroupItem.elementAt(i));
    vgroupItem = vTemp;
    try
    {
      EstStatVO[] estSettleItems = (EstStatVO[])null;
      estSettleItems = getEstSettle(pk_corp, month, whereStr, whereStrVMI, vgroupItem, b);

      EstStatVO[] inSettleItems = (EstStatVO[])null;
      inSettleItems = getInSettle(pk_corp, month, whereStr, whereStrVMI, vgroupItem, b);

      EstStatVO[] inEstItems = (EstStatVO[])null;
      inEstItems = getInEst(pk_corp, month, whereStr, whereStrVMI, vgroupItem, b);

      EstStatVO[] estNoSettleItems = (EstStatVO[])null;
      estNoSettleItems = getEstNoSettle(pk_corp, month, whereStr, whereStrVMI, vgroupItem, b);

      if ((estSettleItems != null) && (estSettleItems.length > 0)) estNoSettleItems = combineVO3(estNoSettleItems, estSettleItems);

      returnVO = combineVO4(estSettleItems, inSettleItems, inEstItems, estNoSettleItems);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return returnVO;
  }

  private EstStatVO[] getInEst(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    EstStatVO[] estStatVOs = getMonthEst(pk_corp, month, wherestr, wherestrVMI, groupItem, b);

    EstStatVO[] tempVOs = getMonthEstAndSettle(pk_corp, month, wherestr, wherestrVMI, groupItem, b);

    if ((tempVOs != null) && (tempVOs.length > 0)) {
      estStatVOs = combineVO1(estStatVOs, tempVOs);
    }

    return estStatVOs;
  }

  private EstStatVO[] getInSettle(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }

    StringBuffer sbSql = new StringBuffer("SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(a.nsettlenum) , SUM(a.nmoney), SUM(a.ngaugemny) ");
    sbSql.append("  FROM po_settlebill_b a INNER JOIN");

    sbSql.append("  po_settlebill b ON a.csettlebillid = b.csettlebillid AND b.pk_corp = '");
    sbSql.append(pk_corp);
    sbSql.append("' AND SUBSTRING(b.dsettledate, 1, 7) = '");
    sbSql.append(month);
    sbSql.append("' AND a.dr = 0 AND b.dr = 0 ");

    StringBuffer sbSql1 = new StringBuffer("");
    StringBuffer sbSql2 = new StringBuffer("");

    sbSql1.append("  INNER JOIN ic_general_b c ON a.cstockrow = c.cgeneralbid ");
    sbSql1.append("  AND c.dr = 0");

    sbSql1.append("  INNER JOIN ");
    sbSql1.append("  ic_general_h d ON a.cstockid = d.cgeneralhid ");
    sbSql1.append("  AND SUBSTRING(c.dbizdate, 1, 7) = '");
    sbSql1.append(month);
    sbSql1.append("' AND d.dr = 0 ");

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql1.append(" AND d.cbilltypecode = '47' ");

    sbSql2.append("  INNER JOIN ");
    sbSql2.append("  ic_vmi_sum d ON a.cvmiid = d.cvmihid ");
    sbSql2.append("  AND SUBSTRING(d.dsumdate, 1, 7) = '");
    sbSql2.append(month);
    sbSql2.append("' AND d.dr = 0 ");

    StringBuffer sbSql3 = new StringBuffer("");
    StringBuffer sbSql4 = new StringBuffer("");
    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_invbasdoc e ON c.cinvbasid = e.pk_invbasdoc ");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_deptdoc f ON d.cdptid = f.pk_deptdoc ");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_psndoc g ON d.cbizid = g.pk_psndoc ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_stordoc stor ON d.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_psndoc k ON d.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cproviderid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
      sbSql3.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_calbody i ON d.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql3.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql3.indexOf("bd_cubasdoc") < 0)
      {
        sbSql3.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cproviderid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
        sbSql3.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
      }
      sbSql3.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0)) {
      sbSql4.append("  LEFT OUTER JOIN bd_invmandoc invman ON d.cinventoryid = invman.pk_invmandoc");
      sbSql4.append("  LEFT OUTER JOIN bd_invbasdoc e ON invman.pk_invbasdoc = e.pk_invbasdoc ");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cvendorid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
      sbSql4.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_calbody i ON d.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_stordoc stor ON d.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql4.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql4.indexOf("bd_cubasdoc") < 0)
      {
        sbSql4.append("  LEFT OUTER JOIN bd_cumandoc cuman ON d.cvendorid = cuman.pk_cumandoc AND cuman.custflag in ('1','3')");
        sbSql4.append("  LEFT OUTER JOIN bd_cubasdoc h ON cuman.pk_cubasdoc = h.pk_cubasdoc ");
      }
      sbSql4.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if ((wherestr != null) && (wherestr.trim().length() > 0)) {
      sbSql3.append("  where  ");
      sbSql3.append(wherestr);

      if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
        sbSql4.append("  where  ");
        sbSql4.append(wherestrVMI);
      }
    }
    sbSql3.append(" GROUP BY ");
    sbSql3.append(groupStr);

    sbSql4.append(" GROUP BY ");
    sbSql4.append(groupStr);

    StringBuffer str1 = new StringBuffer("");
    StringBuffer str2 = new StringBuffer("");
    str1.append(sbSql);
    str1.append(sbSql1);
    str1.append(sbSql3);

    str2.append(sbSql);
    str2.append(sbSql2);
    str2.append(sbSql4);

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getInSettle(str1, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getInSettle(str2, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIInSettle(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }

  public String getInvMatchInvcl(String pk_invmandoc)
    throws BusinessException
  {
    StringBuffer sql = new StringBuffer("SELECT b.invclasscode");
    sql.append("  FROM bd_invbasdoc a INNER JOIN");
    sql.append("  bd_invcl b ON a.pk_invcl = b.pk_invcl INNER JOIN");
    sql.append(" bd_invmandoc c ON a.pk_invbasdoc = c.pk_invbasdoc");
    sql.append("  WHERE (c.pk_invmandoc = '");
    sql.append(pk_invmandoc);
    sql.append("')");
    String invclcode = null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      invclcode = dmo.getInvMatchInvcl(sql.toString());
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return invclcode;
  }

  private EstStatVO[] getMonthEst(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }

    StringBuffer sbSql = new StringBuffer(" SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(b.ninnum) , SUM(c.npmoney) ");
    sbSql.append("  FROM ic_general_h a INNER JOIN");

    sbSql.append("  ic_general_b b ON a.cgeneralhid = b.cgeneralhid AND");

    sbSql.append("  UPPER(b.bzgflag) = 'Y' AND b.pk_invoicecorp = '");
    sbSql.append(pk_corp);

    sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) = '");
    sbSql.append(month);
    sbSql.append("' AND SUBSTRING(b.dbizdate, 1, 7) = '" + month + "' AND a.dr = 0 AND b.dr = 0 ");

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode = '47' ");

    sbSql.append(" INNER JOIN ic_general_bb3 c ON a.cgeneralhid = b.cgeneralhid AND c.dr = 0 AND b.cgeneralbid = c.cgeneralbid ");

    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = b.cinvbasid");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_deptdoc f ON a.cdptid = f.pk_deptdoc");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc g ON a.cbizid = g.pk_psndoc");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc k ON a.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
      sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_calbody i ON a.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql.indexOf("bd_cubasdoc") < 0)
      {
        sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
        sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if (wherestr != null) {
      sbSql.append("  where  ");
      sbSql.append(wherestr);
    }
    sbSql.append("  GROUP BY  ");
    sbSql.append(groupStr);

    StringBuffer sbSql1 = new StringBuffer(" SELECT ");
    sbSql1.append(groupStr);
    sbSql1.append(", SUM(a.noutnum-a.noutinnum) , SUM(a.nmoney) ");
    sbSql1.append("  FROM ic_vmi_sum a");
    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN  bd_invmandoc invman ON invman.pk_invmandoc = a.cinventoryid  ");

      sbSql1.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = invman.pk_invbasdoc");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
      sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_calbody i ON a.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql1.indexOf("bd_cubasdoc") < 0)
      {
        sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
        sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql1.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    sbSql1.append("  where UPPER(a.bgaugeflag) = 'Y' AND a.pk_corp = '");
    sbSql1.append(pk_corp);

    sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) = '");
    sbSql1.append(month);
    sbSql1.append("' AND a.dr = 0");

    if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
      sbSql1.append(" and " + wherestrVMI);
    }
    sbSql1.append("  GROUP BY  ");
    sbSql1.append(groupStr);

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getInEst(sbSql, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getInEst(sbSql1, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIInEst(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }

  private EstStatVO[] getMonthEstAndSettle(String pk_corp, String month, String wherestr, String wherestrVMI, Vector groupItem, UFBoolean[] b)
    throws BusinessException
  {
    if (groupItem.size() <= 0) {
      return null;
    }
    String groupStr = "";
    for (int i = 0; i < groupItem.size(); i++) {
      if (i > 0) {
        groupStr = groupStr + ",";
      }
      groupStr = groupStr + groupItem.elementAt(i).toString().trim();
    }

    StringBuffer sbSql = new StringBuffer(" SELECT ");
    sbSql.append(groupStr);
    sbSql.append(", SUM(c.nsettlenum) , SUM(c.ngaugemny) ");
    sbSql.append("  FROM ic_general_h a INNER JOIN");

    sbSql.append("  ic_general_b b ON a.cgeneralhid = b.cgeneralhid AND");

    sbSql.append("  UPPER(b.bzgflag) = 'Y' AND b.pk_invoicecorp = '");
    sbSql.append(pk_corp);

    sbSql.append("' AND SUBSTRING(b.dzgdate, 1, 7) = '");
    sbSql.append(month);
    sbSql.append("' AND SUBSTRING(b.dbizdate, 1, 7) = '" + month + "' AND a.dr = 0 AND b.dr = 0 ");

    if ((b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','47','4T') ");
    else if ((b[0].booleanValue()) && (!b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode IN ('45','4T') ");
    else if ((!b[0].booleanValue()) && (b[1].booleanValue())) sbSql.append(" AND a.cbilltypecode = '47' ");

    sbSql.append("  INNER JOIN po_settlebill_b c ON a.cgeneralhid = c.cstockid and b.cgeneralbid = c.cstockrow and c.dr = 0");
    sbSql.append("  INNER JOIN po_settlebill dd ON dd.csettlebillid = c.csettlebillid");

    sbSql.append(" AND SUBSTRING(dd.dsettledate, 1, 7) = '");
    sbSql.append(month);
    sbSql.append("' AND dd.dr = 0");

    if ((groupStr.indexOf("inv") >= 0) || (wherestr.indexOf("inv") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = b.cinvbasid");
    }

    if ((groupStr.indexOf("dept") >= 0) || (wherestr.indexOf("dept") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_deptdoc f ON a.cdptid = f.pk_deptdoc");
    }

    if ((groupStr.indexOf("g.psnname") >= 0) || (wherestr.indexOf("g.psnname") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc g ON a.cbizid = g.pk_psndoc");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestr.indexOf("stor") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }
    if ((groupStr.indexOf("k.psnname") >= 0) || (wherestr.indexOf("whsmanager") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_psndoc k ON a.cwhsmanagerid = k.pk_psndoc ");
      wherestr = wherestr.replaceAll("whsmanager", "k.psncode");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestr.indexOf("cust") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
      sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestr.indexOf("body") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_calbody i ON a.pk_calbody = i.pk_calbody ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestr.indexOf("invclass") >= 0))
    {
      sbSql.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestr.indexOf("areacl") >= 0)
    {
      if (sbSql.indexOf("bd_cubasdoc") < 0)
      {
        sbSql.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cproviderid  AND d.custflag in ('1','3')");
        sbSql.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }

    if (wherestr != null) {
      sbSql.append("  where  ");
      sbSql.append(wherestr);
    }
    sbSql.append("  GROUP BY  ");
    sbSql.append(groupStr);

    StringBuffer sbSql1 = new StringBuffer(" SELECT ");
    sbSql1.append(groupStr);
    sbSql1.append(", SUM(c.nsettlenum) , SUM(c.ngaugemny) ");
    sbSql1.append("  FROM ic_vmi_sum a");
    sbSql1.append("  INNER JOIN po_settlebill_b c ON a.cvmihid = c.cvmiid and c.dr = 0");

    sbSql1.append("  INNER JOIN po_settlebill dd ON dd.csettlebillid = c.csettlebillid");

    sbSql1.append(" AND SUBSTRING(dd.dsettledate, 1, 7) = '");
    sbSql1.append(month);
    sbSql1.append("' AND dd.dr = 0 and UPPER(a.bgaugeflag) = 'Y' and a.pk_corp = '");
    sbSql1.append(pk_corp);
    sbSql1.append("' AND SUBSTRING(a.dgaugedate, 1, 7) = '");
    sbSql1.append(month);
    sbSql1.append("' AND a.dr = 0");
    if ((groupStr.indexOf("inv") >= 0) || (wherestrVMI.indexOf("inv") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN  bd_invmandoc invman ON invman.pk_invmandoc = a.cinventoryid  ");

      sbSql1.append("  LEFT OUTER JOIN bd_invbasdoc e ON e.pk_invbasdoc = invman.pk_invbasdoc");
    }

    if ((groupStr.indexOf("cust") >= 0) || (wherestrVMI.indexOf("cust") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
      sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
    }

    if ((groupStr.indexOf("body") >= 0) || (wherestrVMI.indexOf("body") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_calbody i ON a.ccalbodyid = i.pk_calbody ");
    }

    if ((groupStr.indexOf("storname") >= 0) || (wherestrVMI.indexOf("stor") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_stordoc stor ON a.cwarehouseid = stor.pk_stordoc ");
    }

    if ((groupStr.indexOf("invclass") >= 0) || (wherestrVMI.indexOf("invclass") >= 0))
    {
      sbSql1.append("  LEFT OUTER JOIN bd_invcl j ON e.pk_invcl = j.pk_invcl");
    }

    if (wherestrVMI.indexOf("areacl") >= 0)
    {
      if (sbSql1.indexOf("bd_cubasdoc") < 0)
      {
        sbSql1.append("  LEFT OUTER JOIN bd_cumandoc d ON d.pk_cumandoc = a.cvendorid  AND d.custflag in ('1','3')");
        sbSql1.append("  LEFT OUTER JOIN bd_cubasdoc h ON h.pk_cubasdoc = d.pk_cubasdoc");
      }
      sbSql1.append("  LEFT OUTER JOIN bd_areacl ON bd_areacl.pk_areacl =h.pk_areacl ");
    }
    if ((wherestrVMI != null) && (wherestrVMI.trim().length() > 0)) {
      sbSql1.append("  where  ");
      sbSql1.append(wherestrVMI);
    }
    sbSql1.append("  GROUP BY  ");
    sbSql1.append(groupStr);

    EstStatVO[] estStatVOs = (EstStatVO[])null;
    try {
      EstStaDMO dmo = new EstStaDMO();
      if ((b[0].booleanValue()) || (b[1].booleanValue())) {
        estStatVOs = dmo.getInEst(sbSql, groupItem);
      }

      if (b[2].booleanValue()) {
        EstStatVO[] tempVOs = dmo.getInEst(sbSql1, groupItem);
        if ((tempVOs != null) && (tempVOs.length > 0)) estStatVOs = combineVOForVMIInEst(estStatVOs, tempVOs); 
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return estStatVOs;
  }
}