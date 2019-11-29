package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHG7XTO4Y extends VOConversionUI
{
  public String getAfterClassName()
  {
    return "nc.ui.ic.pub.pfconv.ChgAftTo2Ic";
  }

  public String getOtherClassName()
  {
    return "nc.bs.ic.pub.pfconv.ChgAftTo2Ic";
  }

  public String[] getField()
  {
    return new String[] { "H_dbilldate->H_billdate", 
    "H_cdptid->B_pkoprdepart", 
    "H_cbizid->B_pkoperator", 
    "H_pk_corp->B_pksalecorp", 
    "H_pk_calbody->B_pksendstockorg", 
    "H_cwarehouseid->B_pkstroe", 
    "H_cotherwhid->B_pkdestrep", 
    "H_cdispatcherid->B_cdispatcherid", 
    "H_coperatorid->H_userid", 
    "H_cbilltypecode->B_voutbilltype", 
//    "H_cwastewarehouseid->H_pktrancust", 
    "H_cwhmanagerid->B_pkwhmanager", 
    "H_cdilivertypeid->H_pkdelivmode", 
    "H_vdiliveraddress->B_vdestaddress", 
    "B_vdiliveraddress->B_vdestaddress", 
    "B_vbatchcode->B_vbatchcode", 
    "B_nshouldoutnum->B_dinvnum", 
    "B_nshouldoutassistnum->B_dinvassist", 
    "B_csourcebillbid->B_pk_delivbill_b", 
    "B_csourcebillhid->B_pk_delivbill_h", 
    "B_vsourcebillcode->B_vdelivbillcode", 
    "B_cfirstbillbid->B_pkorderrow", 
    "B_cfirstbillhid->B_pkorder", 
    "B_cfirsttype->B_vbilltype", 
    "B_cinventoryid->B_pkinv", 
    "B_castunitid->B_pkassistmeasure", 
    "B_vfree1->B_vfree1", 
    "B_vfree2->B_vfree2", 
    "B_vfree3->B_vfree3", 
    "B_vfree4->B_vfree4", 
    "B_vfree5->B_vfree5", 
    "B_vfree6->B_vfree6", 
    "B_vfree7->B_vfree7", 
    "B_vfree8->B_vfree8", 
    "B_vfree9->B_vfree9", 
    "B_vfree10->B_vfree10", 
    "B_vbatchcode->B_vbatchcode", 
    "B_creceieveid->B_creceiptcorpid", 
    "B_vnotebody->B_vnote", 
    "B_vsourcerowno->B_irownumber", 
    "B_cprojectid->B_pkitem", 
    "B_cprojectphaseid->B_pkitemperiod", 
    "B_cquoteunitid->B_cquoteunitid", 
    "B_nquoteunitrate->B_nquoteunitrate", 
    "B_flargess->B_blargess", 
    "B_bsourcelargess->B_blargess", 
    "H_vuserdef1->H_vuserdef0", 
    "H_vuserdef2->H_vuserdef1", 
    "H_vuserdef3->H_vuserdef2", 
    "H_vuserdef4->H_vuserdef3", 
    "H_vuserdef5->H_vuserdef4", 
    "H_vuserdef6->H_vuserdef5", 
    "H_vuserdef7->H_vuserdef6", 
    "H_vuserdef8->H_vuserdef7", 
    "H_vuserdef9->H_vuserdef8", 
    "H_vuserdef10->H_vuserdef9", 
    "H_vuserdef11->H_vuserdef10", 
    "H_vuserdef12->H_vuserdef11", 
    "H_vuserdef13->H_vuserdef12", 
    "H_vuserdef14->H_vuserdef13", 
    "H_vuserdef15->H_vuserdef14", 
    "H_vuserdef16->H_vuserdef15", 
    "H_vuserdef17->H_vuserdef16", 
    "H_vuserdef18->H_vuserdef17", 
    "H_vuserdef19->H_vuserdef18", 
    "H_vuserdef20->H_vuserdef19", 
    "H_pk_defdoc1->H_pk_defdoc0", 
    "H_pk_defdoc2->H_pk_defdoc1", 
    "H_pk_defdoc3->H_pk_defdoc2", 
    "H_pk_defdoc4->H_pk_defdoc3", 
    "H_pk_defdoc5->H_pk_defdoc4", 
    "H_pk_defdoc6->H_pk_defdoc5", 
    "H_pk_defdoc7->H_pk_defdoc6", 
    "H_pk_defdoc8->H_pk_defdoc7", 
    "H_pk_defdoc9->H_pk_defdoc8", 
    "H_pk_defdoc10->H_pk_defdoc9", 
    "H_pk_defdoc11->H_pk_defdoc10", 
    "H_pk_defdoc12->H_pk_defdoc11", 
//    "H_pk_defdoc13->H_pk_defdoc12", 
    "H_pk_defdoc13->H_pktrancust", //edit by shikun 2014-11-02 承运商传值并显示
    "H_pk_defdoc14->H_pk_defdoc13", 
    "H_pk_defdoc15->H_pk_defdoc14", 
    "H_pk_defdoc16->H_pk_defdoc15", 
    "H_pk_defdoc17->H_pk_defdoc16", 
    "H_pk_defdoc18->H_pk_defdoc17", 
    "H_pk_defdoc19->H_pk_defdoc18", 
    "H_pk_defdoc20->H_pk_defdoc19", 
    "B_vuserdef1->B_vuserdef0", 
    "B_vuserdef2->B_vuserdef1", 
    "B_vuserdef3->B_vuserdef2", 
    "B_vuserdef4->B_vuserdef3", 
    "B_vuserdef5->B_vuserdef4", 
    "B_vuserdef6->B_vuserdef5", 
    "B_vuserdef7->B_vuserdef6", 
    "B_vuserdef8->B_vuserdef7", 
    "B_vuserdef9->B_vuserdef8", 
    "B_vuserdef10->B_vuserdef9", 
    "B_vuserdef11->B_vuserdef10", 
    "B_vuserdef12->B_vuserdef11", 
    "B_vuserdef13->B_vuserdef12", 
    "B_vuserdef14->B_vuserdef13", 
    "B_vuserdef15->B_vuserdef14", 
    "B_vuserdef16->B_vuserdef15", 
    "B_vuserdef17->B_vuserdef16", 
    "B_vuserdef18->B_vuserdef17", 
    "B_vuserdef19->B_vuserdef18", 
    "B_vuserdef20->B_vuserdef19", 
    "B_pk_defdoc1->B_pk_defdoc0", 
    "B_pk_defdoc2->B_pk_defdoc1",
     "B_pk_defdoc3->B_pk_defdoc2", 
     "B_pk_defdoc4->B_pk_defdoc3", 
     "B_pk_defdoc5->B_pk_defdoc4", 
     "B_pk_defdoc6->B_pk_defdoc5", 
     "B_pk_defdoc7->B_pk_defdoc6", 
     "B_pk_defdoc8->B_pk_defdoc7", 
     "B_pk_defdoc9->B_pk_defdoc8", 
     "B_pk_defdoc10->B_pk_defdoc9",
      "B_pk_defdoc11->B_pk_defdoc10", 
      "B_pk_defdoc12->B_pk_defdoc11", 
      "B_pk_defdoc13->B_pk_defdoc12", 
      "B_pk_defdoc14->B_pk_defdoc13", 
      "B_pk_defdoc15->B_pk_defdoc14", 
      "B_pk_defdoc16->B_pk_defdoc15", 
      "B_pk_defdoc17->B_pk_defdoc16", 
      "B_pk_defdoc18->B_pk_defdoc17", 
      "B_pk_defdoc19->B_pk_defdoc18", 
      "B_pk_defdoc20->B_pk_defdoc19" };
  }

  public String[] getFormulas()
  {
    return new String[] { "B_csourcetype->\"7F\"", "B_vfirstrowno->iif(B_vbilltype=\"30\",getColValue(so_saleorder_b, crowno, corder_bid, B_pkorderrow),getColValue(to_bill_b, crowno, cbill_bid, B_pkorderrow))" };
  }

  public UserDefineFunction[] getUserDefineFunction()
  {
    return null;
  }
}