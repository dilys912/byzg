package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHG4CTO32 extends VOConversionUI
{
  public String getAfterClassName()
  {
    return "nc.vo.so.so002.OutToInvoiceChangeVO";
  }

  public String getOtherClassName() {
    return "nc.impl.scm.so.so002.OutStockTOSaleInvoice";
  }

  public String[] getField() {
    return new String[] { "H_dmakedate->SYSDATE", "H_creceiptcustomerid->H_ccustomerid", "H_pk_corp->H_pk_corp", 
    		"H_cbiztype->H_cbiztype", "H_coperatorid->SYSOPERATOR", "H_vnote->H_vnote", "H_vdef9->H_vuserdef9", 
    		"H_vdef8->H_vuserdef8", "H_vdef7->H_vuserdef7", "H_vreceiveaddress->H_vdiliveraddress", 
    		"H_vdef6->H_vuserdef6", "H_vdef5->H_vuserdef5", "H_vdef4->H_vuserdef4", 
    		"H_vdef3->H_vuserdef3", "H_vdef2->H_vuserdef2", "H_vdef1->H_vuserdef1", 
    		"H_ts->H_ts", "H_ccalbodyid->H_pk_calbody", "H_cdispatcherid->H_cdispatcherid", 
    		"H_vdef10->H_vuserdef10", "H_cwarehouseid->H_cwarehouseid", "H_ctermprotocolid->H_cdispatcherid", 
    		"H_dbilldate->SYSDATE", "B_ccustomerid->H_ccustomerid", "H_ctransmodeid->H_cdilivertypeid", 
    		"B_cbodywarehouseid->H_cwarehouseid", "B_cpackunitid->B_castunitid", "B_vfree1->B_vfree1", 
    		"B_vfree2->B_vfree2", "B_vfree3->B_vfree3", "B_vfree4->B_vfree4", 
    		"B_vfree5->B_vfree5", "B_cupreceipttype->H_cbilltypecode", "B_cupsourcebillid->H_cgeneralhid", 
    		"B_cupsourcebillbodyid->B_cgeneralbid", "B_cupupreceipttype->B_csourcetype", "B_cupupsourcebillid->B_csourcebillhid", 
    		"B_cupupsourcebillbodyid->B_csourcebillbid", "B_csourcebillid->B_cfirstbillhid", "B_csourcebillbodyid->B_cfirstbillbid", 
    		"B_cprojectid->B_cprojectid", "B_cprojectphaseid->B_cprojectphaseid", "B_vdef1->B_vuserdef1", 
    		"B_vdef2->B_vuserdef2", "B_vdef3->B_vuserdef3", "B_vdef4->B_vuserdef4", 
    		"B_vdef5->B_vuserdef5", "B_vdef6->B_vuserdef6", "B_creceipttype->B_cfirsttype", 
    		"B_cinventoryid->B_cinventoryid", "B_cunitid->B_caccountunitid", "B_cbatchid->B_vbatchcode", 
    		"B_blargessflag->B_flargess", "B_ts->H_ts", "B_frownote->B_vnotebody", 
    		"B_cadvisecalbodyid->H_pk_calbody", "B_npacknumber->B_noutassistnum", "B_nnumber->B_noutnum", 
    		"B_nquotenumber->B_noutnum", "B_nsubsummny->B_ntaxmny", "B_nsummny->B_ntaxmny", 
    		"B_noriginalcursummny->B_ntaxmny", "B_noriginalcurmny->B_nsalemny", "B_nmny->B_nsalemny", 
    		"B_creceiptcorpid->B_creceieveid", "B_nquoteunitrate->B_nquoteunitrate", "B_cquoteunitid->B_cquoteunitid", 
    		"B_cupsourcebillcode->H_vbillcode", "B_coriginalbillcode->B_vfirstbillcode", "H_vdef11->H_vuserdef11", 
    		"H_vdef12->H_vuserdef12", "H_vdef13->H_vuserdef13", "H_vdef14->H_vuserdef14", "H_vdef15->H_vuserdef15", 
    		"H_vdef16->H_vuserdef16", "H_vdef17->H_vuserdef17", "H_vdef18->H_vuserdef18", "H_vdef19->H_vuserdef19", 
    		"H_vdef20->H_vuserdef20", "B_vdef7->B_vuserdef7", "B_vdef8->B_vuserdef8", "B_vdef9->B_vuserdef9", 
    		"B_vdef10->B_vuserdef10", "B_vdef11->B_vuserdef11", "B_vdef12->B_vuserdef12", "B_vdef13->B_vuserdef13", 
    		"B_vdef14->B_vuserdef14", "B_vdef15->B_vuserdef15", "B_vdef16->B_vuserdef16", "B_vdef17->B_vuserdef17", 
    		"B_vdef18->B_vuserdef18", "B_vdef19->B_vuserdef19", "B_vdef20->B_vuserdef20", "H_pk_defdoc1->H_pk_defdoc1", 
    		"H_pk_defdoc2->H_pk_defdoc2", "H_pk_defdoc3->H_pk_defdoc3", "H_pk_defdoc4->H_pk_defdoc4", 
    		"H_pk_defdoc5->H_pk_defdoc5", "H_pk_defdoc6->H_pk_defdoc6", "H_pk_defdoc7->H_pk_defdoc7", 
    		"H_pk_defdoc8->H_pk_defdoc8", "H_pk_defdoc9->H_pk_defdoc9", "H_pk_defdoc10->H_pk_defdoc10", 
    		"H_pk_defdoc11->H_pk_defdoc11", "H_pk_defdoc12->H_pk_defdoc12", "H_pk_defdoc13->H_pk_defdoc13", 
    		"H_pk_defdoc14->H_pk_defdoc14", "H_pk_defdoc15->H_pk_defdoc15", "H_pk_defdoc16->H_pk_defdoc16", 
    		"H_pk_defdoc17->H_pk_defdoc17", "H_pk_defdoc18->H_pk_defdoc18", "H_pk_defdoc19->H_pk_defdoc19", 
    		"H_pk_defdoc20->H_pk_defdoc20", "B_pk_defdoc1->B_pk_defdoc1", "B_pk_defdoc2->B_pk_defdoc2", 
    		"B_pk_defdoc3->B_pk_defdoc3", "B_pk_defdoc4->B_pk_defdoc4", "B_pk_defdoc5->B_pk_defdoc5", 
    		"B_pk_defdoc6->B_pk_defdoc6", "B_pk_defdoc7->B_pk_defdoc7", "B_pk_defdoc8->B_pk_defdoc8", 
    		"B_pk_defdoc9->B_pk_defdoc9", "B_pk_defdoc10->B_pk_defdoc10", "B_pk_defdoc11->B_pk_defdoc11", 
    		"B_pk_defdoc12->B_pk_defdoc12", "B_pk_defdoc13->B_pk_defdoc13", "B_pk_defdoc14->B_pk_defdoc14", 
    		"B_pk_defdoc15->B_pk_defdoc15", "B_pk_defdoc16->B_pk_defdoc16", "B_pk_defdoc17->B_pk_defdoc17", 
    		"B_pk_defdoc18->B_pk_defdoc18", "B_pk_defdoc19->B_pk_defdoc19", "B_ccurrencytypeid->B_cquotecurrency", 
    		"B_pk_defdoc20->B_pk_defdoc20", "H_ctermprotocolid->H_cdispatcherid", "B_cupsourcebillcode->H_vbillcode", 
    		"B_coriginalbillcode->B_vfirstbillcode", "B_hsl->B_nqtscalefactor" };
  }

  public String[] getFormulas()
  {
    return new String[] { "H_finvoicetype->0", "H_ccustbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc, H_ccustomerid  )",
    		"H_vprintcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc, H_ccustbaseid  )",
    		"H_ctermprotocolid->iif(B_cfirsttype=\"30\",getColValue(so_sale, ctermprotocolid, csaleid, B_cfirstbillhid),null)" };
  }

  public UserDefineFunction[] getUserDefineFunction()
  {
    return null;
  }
}