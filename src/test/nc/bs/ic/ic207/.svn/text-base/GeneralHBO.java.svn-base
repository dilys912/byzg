package nc.bs.ic.ic207;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.ic.ic221.SpecialHDMO;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.bill.GeneralBillBO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ic.pub.bill.MiscDMO;
import nc.bs.ic.pub.check.RebuildDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.IBillItemBarcodeVO;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;

/** @deprecated */
public class GeneralHBO extends GeneralBillBO
{
  public GeneralBillVO[] getOutBillsFromIn(GeneralBillVO[] voIns, GeneralBillVO[] voOuts)
    throws BusinessException
  {
    if ((voOuts == null) || (voOuts.length == 0)) {
      return null;
    }

    try
    {
      GeneralBillDMO dmo1 = new GeneralBillDMO();

      for (int i = 0; i < voIns.length; i++)
      {
        ArrayList alRet = dmo1.getLocSNInfo(voIns[i].getHeaderVO().getCgeneralhid());

        if ((alRet != null) && (alRet.size() > 0))
          voIns[i].setLocators((ArrayList)alRet.get(0));
        if ((alRet != null) && (alRet.size() > 1))
          voIns[i].setSNs((ArrayList)alRet.get(1));
        dmo1.queryBillItemBarCode((IBillItemBarcodeVO[])voIns[i].getItemVOs(), new String[] { voIns[i].getHeaderVO().getCgeneralhid() });
      }

      HashMap hmLoc = new HashMap();
      HashMap hmSN = new HashMap();
      HashMap hmBar = new HashMap();

      for (int i = 0; i < voIns.length; i++) {
        GeneralBillItemVO[] voItems = voIns[i].getItemVOs();
        for (int j = 0; j < voItems.length; j++) {
          if ((voItems[j].getAttributeValue("ncorrespondnum") != null) && (((UFDouble)voItems[j].getAttributeValue("ncorrespondnum")).doubleValue() != 0.0D))
            continue;
          if ((voItems[j].getLocator() != null) && (voItems[j].getLocator().length > 0))
          {
            hmLoc.put(voItems[j].getCgeneralbid(), voItems[j].getLocator());
          }
          if ((voItems[j].getSerial() != null) && (voItems[j].getSerial().length > 0))
          {
            hmSN.put(voItems[j].getCgeneralbid(), voItems[j].getSerial());
          }
          if ((voItems[j].getBarCodeVOs() == null) || (voItems[j].getBarCodeVOs().length <= 0))
            continue;
          hmBar.put(voItems[j].getCgeneralbid(), voItems[j].getBarCodeVOs());
        }

      }

      if ((hmLoc.size() == 0) && (hmSN.size() == 0) && (hmBar.size() == 0)) {
        return voOuts;
      }

      UFDouble uftmp = null;
      UFDouble ufasttmp = null;

      for (int i = 0; i < voOuts.length; i++) {
        GeneralBillItemVO[] voItems = voOuts[i].getItemVOs();
        for (int j = 0; j < voItems.length; j++)
        {
          LocatorVO[] voLocs = (LocatorVO[])(LocatorVO[])hmLoc.get(voItems[j].getCcorrespondbid());

          if ((voLocs != null) && (voLocs.length > 0)) {
            for (int k = 0; k < voLocs.length; k++) {
              uftmp = voLocs[k].getNinspacenum();
              if (uftmp != null) {
                voLocs[k].setNinspacenum(null);
                voLocs[k].setNoutspacenum(uftmp);
              } else {
                uftmp = voLocs[k].getNoutspacenum();
                voLocs[k].setNinspacenum(uftmp);
                voLocs[k].setNoutspacenum(uftmp);
              }

              ufasttmp = voLocs[k].getNinspaceassistnum();
              if (uftmp != null) {
                voLocs[k].setNinspaceassistnum(null);
                voLocs[k].setNoutspaceassistnum(ufasttmp);
              } else {
                ufasttmp = voLocs[k].getNoutspaceassistnum();
                voLocs[k].setNinspaceassistnum(ufasttmp);
                voLocs[k].setNoutspaceassistnum(null);
              }

              uftmp = voLocs[k].getNingrossnum();
              if (uftmp != null) {
                voLocs[k].setNingrossnum(null);
                voLocs[k].setNoutgrossnum(uftmp);
              } else {
                uftmp = voLocs[k].getNoutgrossnum();
                voLocs[k].setNingrossnum(uftmp);
                voLocs[k].setNoutgrossnum(null);
              }

            }

            voItems[j].setLocator(voLocs);
          }

          SerialVO[] vosn = (SerialVO[])(SerialVO[])hmSN.get(voItems[j].getCcorrespondbid());
          if ((vosn != null) && (vosn.length > 0))
          {
            voItems[j].setSerial(vosn);
          }

          BarCodeVO[] vobars = (BarCodeVO[])(BarCodeVO[])hmBar.get(voItems[j].getCcorrespondbid());
          if ((vobars == null) || (vobars.length <= 0))
            continue;
          voItems[j].setBarCodeVOs(vobars);
        }

      }

    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }

    return voOuts;
  }

  protected Hashtable addToHashtable(String SpecialBid, Object ufdValue, Hashtable addinHashtable, int voStatus)
  {
    if ((null == ufdValue) || (ufdValue.toString().trim().length() == 0) || (((UFDouble)ufdValue).doubleValue() == 0.0D))
    {
      return addinHashtable;
    }

    SpecialBid = SpecialBid.trim();
    UFDouble ufd = (UFDouble)ufdValue;
    if (voStatus == 3) {
      ufd = ufd.multiply(-1.0D);
    }

    if (addinHashtable.containsKey(SpecialBid)) {
      UFDouble ufdOld = (UFDouble)addinHashtable.get(SpecialBid);

      ufd = ufd.add(ufdOld);
    }

    addinHashtable.put(SpecialBid, ufd);

    return addinHashtable;
  }

  public ArrayList cancelSignBill(GeneralBillVO voCancelAuditBill)
    throws BusinessException
  {
    try
    {
      if (voCancelAuditBill == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000030"));
      }

      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voCancelAuditBill.getParentVO();

      if (voHead == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000030"));
      }

      GeneralBillItemVO[] voaBody = voCancelAuditBill.getItemVOs();
      if ((voaBody == null) || (voaBody.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000081"));
      }

      String sCorpID = voHead.getPk_corp();

      String sWhID = voHead.getCwarehouseid();

      String sBillPK = voHead.getCgeneralhid();

      MiscDMO dmoMisc = new MiscDMO();

      ArrayList alRet = dmoMisc.getCalBody(sWhID, null, sCorpID);
      if ((alRet == null) || (alRet.get(0) == null) || (alRet.get(0).toString().trim().length() == 0))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000082"));
      }

      ArrayList alParam = new ArrayList();

      String sOperatorID = voHead.getCregister();

      String sCurDate = null;
      if (voHead.getDaccountdate() != null) {
        sCurDate = voHead.getDaccountdate().toString();
      }
      alParam.add(sBillPK);
      alParam.add(sOperatorID);
      alParam.add(sCorpID);
      alParam.add(sCurDate);
      alParam.add(voHead.getCbilltypecode());

      ArrayList alResult = new ArrayList();

      GeneralHDMO dmoBill = new GeneralHDMO();
      alResult.add(dmoBill.cancelSign(alParam));

      return alResult;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public void deleteBill(GeneralBillVO vo)
    throws BusinessException
  {
    super.deleteBill(vo);
  }

  public ArrayList insertBillsNOLock(GeneralBillVO[] vos)
    throws BusinessException
  {
    try
    {
      return super.insertBillsWithNoLock(vos);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public ArrayList insertThisBill(GeneralBillVO voNewBill)
    throws BusinessException
  {
    try
    {
      ArrayList alRet = super.insertThisBill(voNewBill);

      if (voNewBill.getHeaderVO().getAttributeValue("isPeiTao") != null)
      {
        if (voNewBill.getHeaderVO().getAttributeValue("isPeiTao").toString().equals("N")) {
          return alRet;
        }
      }

      String srctype = voNewBill.getItemVOs()[0].getCsourcetype();
      if ((srctype != null) && ((srctype.equals("4C")) || (srctype.equals("45")))) {
        nc.bs.ic.ic211.GeneralHDMO dmo = new nc.bs.ic.ic211.GeneralHDMO();
        dmo.setDispense(voNewBill.getHeaderVO().getCgeneralhid());
      }

      return alRet;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public ArrayList queryBills(QryConditionVO voQC)
    throws BusinessException
  {
    try
    {
      GeneralHDMO dmo = new GeneralHDMO();
      return dmo.queryBills(voQC);
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public ArrayList queryBills1(QryConditionVO qryVO)
    throws BusinessException
  {
    ArrayList alRet = null;
    if (qryVO != null) {
      try {
        EfficiencyTestDMO dmo = new EfficiencyTestDMO();
        alRet = dmo.queryBills(qryVO);
      } catch (Exception e) {
        GenMethod.throwBusiException(e);
      }
    }

    return alRet;
  }

  public ArrayList saveBill(GeneralBillVO vo)
    throws BusinessException
  {
    try
    {
      if (vo != null) {
        String sHid = vo.getHeaderVO().getPrimaryKey();
        if (null != sHid) {
          RebuildDMO redmo = new RebuildDMO();
          ArrayList alBill = redmo.getBills(new String[] { vo.getPrimaryKey() });

          if ((alBill == null) || (alBill.size() != 1)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000083"));
          }

          writeBackToTransferBill(vo, 1);
        }
        else {
          writeBackToTransferBill(vo, 2);
        }
      }
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }

    return super.saveBill(vo);
  }

  public ArrayList saveBill(GeneralBillVO voCur, Object voPre)
    throws BusinessException
  {
    return super.saveBill(voCur, voPre);
  }

  public ArrayList signBill(GeneralBillVO voAuditBill)
    throws BusinessException
  {
    return signBills(new GeneralBillVO[] { voAuditBill });
  }

  public ArrayList signBills(GeneralBillVO[] voAuditBills)
    throws BusinessException
  {
    if ((voAuditBills == null) || (voAuditBills.length == 0)) {
      return null;
    }

    try
    {
      Vector v = new Vector();
      String[] sourcetypes = { "4L", "4M", "4N" };
      for (int i = 0; i < voAuditBills.length; i++) {
        if ((voAuditBills[i] == null) || (voAuditBills[i].getHeaderVO() == null) || (!voAuditBills[i].isHaveSourceBill(sourcetypes))) {
          continue;
        }
        v.add(voAuditBills[i].getHeaderVO().getCgeneralhid());
      }

      if (v.size() > 0) {
        String[] sBillPKs = new String[v.size()];
        v.copyInto(sBillPKs);

        GeneralHDMO dmoBill = new GeneralHDMO();

        dmoBill.signCheckOtherOut(sBillPKs);
      }
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }

    return super.signBills(voAuditBills);
  }

  public ArrayList signBillWithNoLock(GeneralBillVO vo)
    throws BusinessException
  {
    return super.signBill(vo);
  }

  protected void writeBackToTransferBill(GeneralBillVO gbvo, int OperationStatus)
    throws BusinessException
  {
    if ((gbvo == null) || (gbvo.getChildrenVO() == null) || (gbvo.getChildrenVO().length == 0))
    {
      return;
    }

    GeneralBillItemVO[] gbivos = gbvo.getItemVOs();
    if ((gbivos[0].getCsourcetype() == null) || (!gbivos[0].getCsourcetype().trim().equals(BillTypeConst.m_transfer)))
    {
      return;
    }
    UFDouble ZERO = new UFDouble("0.0");
    try
    {
      GeneralBillItemVO[] gbivoOlds = null;
      if (OperationStatus == 1) {
        String sBillHid = gbvo.getPrimaryKey();
        GeneralHDMO gbdmo = new GeneralHDMO();
        gbivoOlds = gbdmo.queryBillItemForCopy(sBillHid);
        if (null == gbivoOlds) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000085"));
        }

      }

      boolean bIsOtherinBill = gbvo.getHeaderValue("cbilltypecode").equals(BillTypeConst.m_otherIn);

      Hashtable htForSpecialBid = new Hashtable();

      for (int i = 0; i < gbvo.getItemCount(); i++) {
        GeneralBillItemVO gbivonew = gbvo.getItemVOs()[i];
        String sSpecialBID = gbivonew.getCsourcebillbid().trim();
        Object oValueOld = null;
        if (bIsOtherinBill)
          oValueOld = gbivonew.getNinnum();
        else {
          oValueOld = gbivonew.getNoutnum();
        }
        if ((OperationStatus == 3) || (gbivonew.getStatus() == 3))
        {
          htForSpecialBid = addToHashtable(sSpecialBID, oValueOld, htForSpecialBid, 3);
        }
        else if (gbivonew.getStatus() == 1)
        {
          for (int j = 0; j < gbivoOlds.length; j++) {
            if (!gbivoOlds[j].getCgeneralbid().equals(gbivonew.getCgeneralbid()))
              continue;
            Object oValueNew = null;
            if (bIsOtherinBill)
              oValueNew = gbivoOlds[j].getNinnum();
            else {
              oValueNew = gbivoOlds[j].getNoutnum();
            }
            oValueOld = oValueOld == null ? ZERO : new UFDouble(oValueOld.toString());

            oValueNew = oValueNew == null ? ZERO : new UFDouble(oValueNew.toString());

            if (oValueOld == oValueNew)
              break;
            htForSpecialBid = addToHashtable(sSpecialBID, ((UFDouble)oValueOld).sub((UFDouble)oValueNew), htForSpecialBid, 1);

            break;
          }

        }
        else if (gbivonew.getStatus() == 2)
        {
          htForSpecialBid = addToHashtable(sSpecialBID, oValueOld, htForSpecialBid, 2);
        }
        else if (gbivonew.getStatus() != 0)
          {
            continue;
          }
      }

      if (htForSpecialBid.isEmpty())
      {
        return;
      }

      String sSpecialHID = gbvo.getItemValue(0, "csourcebillhid").toString().trim();

      RebuildDMO redmo = new RebuildDMO();
      SpecialBillVO sbvo = redmo.getSBills(sSpecialHID);
      if (null == sbvo) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000086"));
      }

      for (int i = 0; i < sbvo.getItemVOs().length; i++) {
        SpecialBillItemVO sbivo = sbvo.getItemVOs()[i];
        String sBillBID = sbivo.getCspecialbid();
        if (!htForSpecialBid.containsKey(sBillBID)) {
          continue;
        }
        UFDouble ufdValueNew = ZERO;
        ufdValueNew = (UFDouble)htForSpecialBid.get(sBillBID);
        if (bIsOtherinBill)
        {
          Object oValueOld = sbivo.getNadjustnum();
          ufdValueNew = ufdValueNew.add((UFDouble)(oValueOld == null ? ZERO : oValueOld));

          sbivo.setNadjustnum(ufdValueNew);
          sbvo.setHeaderValue("vadjuster", gbvo.getHeaderValue("coperatorid"));
        }
        else
        {
          Object oValueOld = sbivo.getNchecknum();
          ufdValueNew = ufdValueNew.add((UFDouble)(oValueOld == null ? ZERO : oValueOld));

          sbivo.setNchecknum(ufdValueNew);
          sbvo.setHeaderValue("cauditorid", gbvo.getHeaderValue("coperatorid"));
        }

        sbivo.setStatus(1);
      }

      sbvo.getHeaderVO().setStatus(1);

      boolean bHaveIn = false; boolean bHaveOut = false;
      for (int i = 0; i < sbvo.getItemVOs().length; i++) {
        SpecialBillItemVO sbivo = sbvo.getItemVOs()[i];

        if ((sbivo.getNadjustnum() != null) && (sbivo.getNadjustnum().doubleValue() != 0.0D))
        {
          bHaveIn = true;
        }

        if ((sbivo.getNchecknum() == null) || (sbivo.getNchecknum().doubleValue() == 0.0D))
          continue;
        bHaveOut = true;
      }

      if ((bIsOtherinBill) && (!bHaveIn)) {
        sbvo.setHeaderValue("vadjuster", null);
      }
      if ((!bIsOtherinBill) && (!bHaveOut)) {
        sbvo.setHeaderValue("cauditorid", null);
      }
      SCMEnv.out("in=" + sbvo.getHeaderValue("vadjuster"));
      SCMEnv.out("out=" + sbvo.getHeaderValue("cauditorid"));

      SpecialHDMO sdmo = new SpecialHDMO();

      sdmo.isAlreadyOutNum(sbvo);

      sdmo.updateBillold(sbvo);
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void writeSpecialBill(GeneralBillVO voNew, GeneralBillVO voPre)
    throws BusinessException
  {
    GeneralBillVO voCur = null;
    int OperationStatus = 0;
    if ((voNew != null) && (voPre == null)) {
      OperationStatus = 2;
      voCur = voNew;
    } else if ((voNew != null) && (voPre != null)) {
      OperationStatus = 1;
      voCur = voNew;
    } else if ((voNew == null) && (voPre != null)) {
      OperationStatus = 3;
      voCur = voPre;
    }

    GeneralBillItemVO[] gbivos = voCur.getItemVOs();
    if ((gbivos[0].getCsourcetype() == null) || (!gbivos[0].getCsourcetype().trim().equals(BillTypeConst.m_transfer)))
    {
      return;
    }

    UFDouble ZERO = new UFDouble("0.0");
    try
    {
      GeneralBillItemVO[] gbivoOlds = null;
      if (OperationStatus == 1) {
        gbivoOlds = voPre.getItemVOs();
      }

      boolean bIsOtherinBill = voCur.getHeaderValue("cbilltypecode").equals(BillTypeConst.m_otherIn);

      Hashtable htForSpecialBid = new Hashtable();

      for (int i = 0; i < voCur.getItemCount(); i++) {
        GeneralBillItemVO gbivonew = voCur.getItemVOs()[i];
        String sSpecialBID = gbivonew.getCsourcebillbid().trim();
        Object oValueOld = null;
        if (bIsOtherinBill)
          oValueOld = gbivonew.getNinnum();
        else {
          oValueOld = gbivonew.getNoutnum();
        }
        if ((OperationStatus == 3) || (gbivonew.getStatus() == 3))
        {
          htForSpecialBid = addToHashtable(sSpecialBID, oValueOld, htForSpecialBid, 3);
        }
        else if (gbivonew.getStatus() == 1)
        {
          for (int j = 0; j < gbivoOlds.length; j++) {
            if (!gbivoOlds[j].getCgeneralbid().equals(gbivonew.getCgeneralbid()))
              continue;
            Object oValueNew = null;
            if (bIsOtherinBill)
              oValueNew = gbivoOlds[j].getNinnum();
            else {
              oValueNew = gbivoOlds[j].getNoutnum();
            }
            oValueOld = oValueOld == null ? ZERO : new UFDouble(oValueOld.toString());

            oValueNew = oValueNew == null ? ZERO : new UFDouble(oValueNew.toString());

            if (oValueOld == oValueNew)
              break;
            htForSpecialBid = addToHashtable(sSpecialBID, ((UFDouble)oValueOld).sub((UFDouble)oValueNew), htForSpecialBid, 1);

            break;
          }

        }
        else if (gbivonew.getStatus() == 2)
        {
          htForSpecialBid = addToHashtable(sSpecialBID, oValueOld, htForSpecialBid, 2);
        }
        else if (gbivonew.getStatus() != 0)
          {
            continue;
          }
      }

      if (htForSpecialBid.isEmpty())
      {
        return;
      }

      String sSpecialHID = voCur.getItemValue(0, "csourcebillhid").toString().trim();

      RebuildDMO redmo = new RebuildDMO();
      SpecialBillVO sbvo = redmo.getSBills(sSpecialHID);
      if (null == sbvo) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008busi", "UPP4008busi-000086"));
      }

      for (int i = 0; i < sbvo.getItemVOs().length; i++) {
        SpecialBillItemVO sbivo = sbvo.getItemVOs()[i];
        String sBillBID = sbivo.getCspecialbid();
        if (!htForSpecialBid.containsKey(sBillBID)) {
          continue;
        }
        UFDouble ufdValueNew = ZERO;
        ufdValueNew = (UFDouble)htForSpecialBid.get(sBillBID);
        if (bIsOtherinBill)
        {
          Object oValueOld = sbivo.getNadjustnum();
          ufdValueNew = ufdValueNew.add((UFDouble)(oValueOld == null ? ZERO : oValueOld));

          sbivo.setNadjustnum(ufdValueNew);
          sbvo.setHeaderValue("vadjuster", voCur.getHeaderValue("coperatorid"));
        }
        else
        {
          Object oValueOld = sbivo.getNchecknum();
          ufdValueNew = ufdValueNew.add((UFDouble)(oValueOld == null ? ZERO : oValueOld));

          sbivo.setNchecknum(ufdValueNew);
          sbvo.setHeaderValue("cauditorid", voCur.getHeaderValue("coperatorid"));
        }

        sbivo.setStatus(1);
      }

      sbvo.getHeaderVO().setStatus(1);

      boolean bHaveIn = false; boolean bHaveOut = false;
      for (int i = 0; i < sbvo.getItemVOs().length; i++) {
        SpecialBillItemVO sbivo = sbvo.getItemVOs()[i];

        if ((sbivo.getNadjustnum() != null) && (sbivo.getNadjustnum().doubleValue() != 0.0D))
        {
          bHaveIn = true;
        }

        if ((sbivo.getNchecknum() == null) || (sbivo.getNchecknum().doubleValue() == 0.0D))
          continue;
        bHaveOut = true;
      }

      if ((bIsOtherinBill) && (!bHaveIn)) {
        sbvo.setHeaderValue("vadjuster", null);
      }
      if ((!bIsOtherinBill) && (!bHaveOut)) {
        sbvo.setHeaderValue("cauditorid", null);
      }
      SCMEnv.out("in=" + sbvo.getHeaderValue("vadjuster"));
      SCMEnv.out("out=" + sbvo.getHeaderValue("cauditorid"));

      SpecialHDMO sdmo = new SpecialHDMO();

      sdmo.isAlreadyOutNum(sbvo);

      sdmo.updateBillold(sbvo);
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }
}