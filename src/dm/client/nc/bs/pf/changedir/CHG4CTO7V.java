package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.UserDefineFunction;

public class CHG4CTO7V extends VOConversion
{
  public String getAfterClassName()
  {
    return null;
  }

  public String getOtherClassName()
  {
    return null;
  }

  public String[] getField()
  {
    return new String[] { "H_pk_corp->H_pk_corp", "H_pk_defdoc19->H_pk_defdoc19", "H_pk_defdoc18->H_pk_defdoc18", "H_pk_defdoc17->H_pk_defdoc17", "H_pk_defdoc16->H_pk_defdoc16", "H_pk_defdoc15->H_pk_defdoc15", "H_pk_defdoc14->H_pk_defdoc14", "H_pk_defdoc13->H_pk_defdoc13", "H_vuserdef20->H_vuserdef20", "H_pk_defdoc12->H_pk_defdoc12", "H_pk_defdoc11->H_pk_defdoc11", "H_pk_defdoc10->H_pk_defdoc10", "H_vuserdef9->H_vuserdef9", "H_vuserdef8->H_vuserdef8", "H_vuserdef7->H_vuserdef7", "H_vuserdef6->H_vuserdef6", "H_vnote->H_vnote", "H_pk_defdoc9->H_pk_defdoc9", "H_vuserdef5->H_vuserdef5", "H_pk_defdoc8->H_pk_defdoc8", "H_vuserdef4->H_vuserdef4", "H_pk_defdoc7->H_pk_defdoc7", "H_vuserdef3->H_vuserdef3", "H_pk_defdoc6->H_pk_defdoc6", "H_vuserdef2->H_vuserdef2", "H_pk_defdoc5->H_pk_defdoc5", "H_vuserdef1->H_vuserdef1", "H_pk_defdoc4->H_pk_defdoc4", "H_pk_defdoc3->H_pk_defdoc3", "H_pk_defdoc2->H_pk_defdoc2", "H_pk_defdoc1->H_pk_defdoc1", "H_vuserdef19->H_vuserdef19", "H_vuserdef18->H_vuserdef18", "H_vuserdef17->H_vuserdef17", "H_vuserdef16->H_vuserdef16", "H_vuserdef15->H_vuserdef15", "H_vuserdef14->H_vuserdef14", "H_vuserdef13->H_vuserdef13", "H_vuserdef12->H_vuserdef12", "H_vuserdef11->H_vuserdef11", "H_vuserdef10->H_vuserdef10", "H_pk_defdoc20->H_pk_defdoc20", "B_pk_defdoc10->B_pk_defdoc10", "B_pk_defdoc9->B_pk_defdoc9", "B_pk_defdoc8->B_pk_defdoc8", "B_pk_defdoc7->B_pk_defdoc7", "B_pk_defdoc6->B_pk_defdoc6", "B_pk_defdoc5->B_pk_defdoc5", "B_pk_defdoc4->B_pk_defdoc4", "B_pk_defdoc3->B_pk_defdoc3", "B_pk_defdoc2->B_pk_defdoc2", "B_pk_defdoc1->B_pk_defdoc1", "B_vuserdef19->B_vuserdef19", "B_SCORP->SYSCORP", "B_vuserdef18->B_vuserdef18", "B_vuserdef17->B_vuserdef17", "B_vuserdef16->B_vuserdef16", "B_vuserdef15->B_vuserdef15", "B_vuserdef14->B_vuserdef14", "B_vuserdef13->B_vuserdef13", "B_vuserdef12->B_vuserdef12", "B_vuserdef11->B_vuserdef11", "B_vuserdef10->B_vuserdef10", "B_STIME->SYSTIME", "B_SACCOUNTYEAR->SYSACCOUNTYEAR", "B_vuserdef9->B_vuserdef9", "B_SDATE->SYSDATE", "B_vuserdef8->B_vuserdef8", "B_pk_defdoc20->B_pk_defdoc20", "B_vuserdef7->B_vuserdef7", "B_vuserdef6->B_vuserdef6", "B_vuserdef5->B_vuserdef5", "B_vuserdef4->B_vuserdef4", "B_vuserdef3->B_vuserdef3", "B_vuserdef2->B_vuserdef2", "B_vuserdef1->B_vuserdef1", "B_STBILLTYPE->DESTBILLTYPE", "B_pk_defdoc19->B_pk_defdoc19", "B_pk_defdoc18->B_pk_defdoc18", "B_pk_defdoc17->B_pk_defdoc17", "B_vuserdef20->B_vuserdef20", "B_pk_defdoc16->B_pk_defdoc16", "B_SOPERATOR->SYSOPERATOR", "B_pk_defdoc15->B_pk_defdoc15", "B_pk_defdoc14->B_pk_defdoc14", "B_pk_defdoc13->B_pk_defdoc13", "B_pk_defdoc12->B_pk_defdoc12", "B_pk_defdoc11->B_pk_defdoc11" };
  }

  public String[] getFormulas()
  {
    return new String[] { "B_dunitprice->B_ntaxprice  = dunitprice*(1+dtax/10)" };
  }

  public UserDefineFunction[] getUserDefineFunction()
  {
    return null;
  }
}