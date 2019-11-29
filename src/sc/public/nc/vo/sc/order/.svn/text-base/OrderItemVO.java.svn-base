package nc.vo.sc.order;

import java.util.ArrayList;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.pub.ScConstants;
import nc.vo.scm.ic.bill.FreeVO;

public class OrderItemVO extends CircularlyAccessibleValueObject
{
  public String m_corder_bid;
  public String m_corderid;
  public String m_pk_corp;
  public String m_cmangid;
  public String m_cbaseid;
  public UFDouble m_nordernum;
  public String m_cassistunit;
  public UFDouble m_nassistnum;
  public UFDouble m_ndiscountrate;
  public Integer m_idiscounttaxtype;
  public UFDouble m_ntaxrate;
  public String m_ccurrencytypeid;
  public UFDouble m_noriginalcurprice;
  public UFDouble m_noriginalcurmny;
  public UFDouble m_noriginaltaxmny;
  public UFDouble m_noriginalsummny;
  public UFDouble m_nexchangeotobrate;
  public UFDouble m_ntaxmny;
  public UFDouble m_nmoney;
  public UFDouble m_nsummny;
  public UFDouble m_nexchangeotoarate;
  public UFDouble m_nassistcurmny;
  public UFDouble m_nassisttaxmny;
  public UFDouble m_nassistsummny;
  public UFDouble m_naccumarrvnum;
  public UFDouble m_naccumstorenum;
  public UFDouble m_naccuminvoicenum;
  public UFDouble m_naccumwastnum;
  public UFDate m_dplanarrvdate;
  public String m_cwarehouseid;
  public String m_creceiveaddress;
  public String m_cprojectid;
  public String m_cprojectphaseid;
  public String m_coperator;
  public Integer m_forderrowstatus;
  public UFBoolean m_bisactive;
  public String m_cordersource;
  public String m_csourcebillid;
  public String m_csourcebillrow;
  public String m_cupsourcebilltype;
  public String m_cupsourcebillid;
  public String m_cupsourcebillrowid;
  public String m_vmemo;
  public String m_vfree1;
  public String m_vfree2;
  public String m_vfree3;
  public String m_vfree4;
  public String m_vfree5;
  public String m_vdef1;
  public String m_vdef2;
  public String m_vdef3;
  public String m_vdef4;
  public String m_vdef5;
  public String m_vdef6;
  public String m_vdef7;
  public String m_vdef8;
  public String m_vdef9;
  public String m_vdef10;
  public String m_vdef11;
  public String m_vdef12;
  public String m_vdef13;
  public String m_vdef14;
  public String m_vdef15;
  public String m_vdef16;
  public String m_vdef17;
  public String m_vdef18;
  public String m_vdef19;
  public String m_vdef20;
  public String m_pk_defdoc1;
  public String m_pk_defdoc2;
  public String m_pk_defdoc3;
  public String m_pk_defdoc4;
  public String m_pk_defdoc5;
  public String m_pk_defdoc6;
  public String m_pk_defdoc7;
  public String m_pk_defdoc8;
  public String m_pk_defdoc9;
  public String m_pk_defdoc10;
  public String m_pk_defdoc11;
  public String m_pk_defdoc12;
  public String m_pk_defdoc13;
  public String m_pk_defdoc14;
  public String m_pk_defdoc15;
  public String m_pk_defdoc16;
  public String m_pk_defdoc17;
  public String m_pk_defdoc18;
  public String m_pk_defdoc19;
  public String m_pk_defdoc20;
  public String m_ccontractid;
  public String m_ccontractrowid;
  public String m_ccontractrcode;
  public String m_vpriceauditcode;
  public String m_cpriceauditid;
  public String m_cpriceaudit_bid;
  public String m_cpriceaudit_bb1id;
  private UFDouble m_convertrate = null;
  private UFDouble m_narrvnum = null;
  private UFDouble m_nprice = null;
  public String cdeptid;
  public String cemployeeid;
  public String cvendorid;
  public String cwareid;
  public String m_bbid;
  public String m_crowno;
  private String m_cupsourcebts = null;

  private String m_cupsourcehts = null;
  public FreeVO m_freevo;
  public String m_invcode;
  public String m_invname;
  public String m_invshow;
  public String m_invspec;
  public String m_invtype;
  public String m_isassist;
  private UFDouble m_measrate;
  private UFDouble m_nbackarrvnum;
  private UFDouble m_nbackstorenum;
  private UFDouble m_nnotelignum;
  private UFDouble m_norgnettaxprice = null;

  private UFDouble m_norgtaxprice = null;
  public UFDouble m_noriginalnetprice;
  public UFDouble m_oldnum;
  private String m_ts;
  public String m_vassunitname;
  public String m_vcurrencyname;
  public String m_vfree0;
  public String m_vmeasdocname;
  public String m_vproducenum;
  public String m_vprojectname;
  public String m_vprojectphasename;
  public String m_vwarehousename;
  public String vordercode;
  public String m_bomvers;//shikun 

  public OrderItemVO()
  {
  }

  public OrderItemVO(String newCorder_bid)
  {
    this.m_corder_bid = newCorder_bid;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone();
    } catch (Exception e) {
    }
    OrderItemVO orderB = (OrderItemVO)o;

    String[] names = getAttributeNames();
    for (int i = 0; i < names.length; i++) {
      if ((getAttributeValue(names[i]) != null) && (!getAttributeValue(names[i]).toString().trim().equals(""))) {
        orderB.setAttributeValue(names[i], getAttributeValue(names[i]));
      }
    }

    return orderB;
  }

  public String getEntityName()
  {
    return "OrderB";
  }

  public String getPrimaryKey()
  {
    return this.m_corder_bid;
  }

  public void setPrimaryKey(String newCorder_bid)
  {
    this.m_corder_bid = newCorder_bid;
  }

  public String getCorder_bid()
  {
    return this.m_corder_bid;
  }

  public String getCorderid()
  {
    return this.m_corderid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getCmangid()
  {
    return this.m_cmangid;
  }

  public String getCbaseid()
  {
    return this.m_cbaseid;
  }

  public UFDouble getNordernum()
  {
    return this.m_nordernum;
  }

  public String getCassistunit()
  {
    return this.m_cassistunit;
  }

  public UFDouble getNassistnum()
  {
    return this.m_nassistnum;
  }

  public UFDouble getNdiscountrate()
  {
    return this.m_ndiscountrate;
  }

  public Integer getIdiscounttaxtype()
  {
    return this.m_idiscounttaxtype;
  }

  public UFDouble getNtaxrate()
  {
    return this.m_ntaxrate;
  }

  public String getCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public UFDouble getNoriginalcurprice()
  {
    return this.m_noriginalcurprice;
  }

  public UFDouble getNoriginalcurmny()
  {
    return this.m_noriginalcurmny;
  }

  public UFDouble getNoriginaltaxmny()
  {
    return this.m_noriginaltaxmny;
  }

  public UFDouble getNoriginalsummny()
  {
    return this.m_noriginalsummny;
  }

  public UFDouble getNexchangeotobrate()
  {
    return this.m_nexchangeotobrate;
  }

  public static String getPriceFieldByPricePolicy(int iPolicy)
  {
    if (iPolicy == 5)
    {
      return "norgtaxprice";
    }if (iPolicy == 6)
    {
      return "noriginalcurprice";
    }

    return null;
  }

  public UFDouble getNtaxmny()
  {
    return this.m_ntaxmny;
  }

  public UFDouble getNmoney()
  {
    return this.m_nmoney;
  }

  public UFDouble getNsummny()
  {
    return this.m_nsummny;
  }

  public UFDouble getNexchangeotoarate()
  {
    return this.m_nexchangeotoarate;
  }

  public UFDouble getNassistcurmny()
  {
    return this.m_nassistcurmny;
  }

  public UFDouble getNassisttaxmny()
  {
    return this.m_nassisttaxmny;
  }

  public UFDouble getNassistsummny()
  {
    return this.m_nassistsummny;
  }

  public UFDouble getNaccumarrvnum()
  {
    return this.m_naccumarrvnum;
  }

  public UFDouble getNaccumstorenum()
  {
    return this.m_naccumstorenum;
  }

  public UFDouble getNaccuminvoicenum()
  {
    return this.m_naccuminvoicenum;
  }

  public UFDouble getNaccumwastnum()
  {
    return this.m_naccumwastnum;
  }

  public UFDate getDplanarrvdate()
  {
    return this.m_dplanarrvdate;
  }

  public String getCwarehouseid()
  {
    return this.m_cwarehouseid;
  }

  public String getCreceiveaddress()
  {
    return this.m_creceiveaddress;
  }

  public String getCprojectid()
  {
    return this.m_cprojectid;
  }

  public String getCprojectphaseid()
  {
    return this.m_cprojectphaseid;
  }

  public String getCoperator()
  {
    return this.m_coperator;
  }

  public Integer getForderrowstatus()
  {
    return this.m_forderrowstatus;
  }

  public UFBoolean getBisactive()
  {
    return this.m_bisactive;
  }

  public String getCordersource()
  {
    return this.m_cordersource;
  }

  public String getCsourcebillid()
  {
    return this.m_csourcebillid;
  }

  public String getCsourcebillrow()
  {
    return this.m_csourcebillrow;
  }

  public String getCupsourcebilltype()
  {
    return this.m_cupsourcebilltype;
  }

  public String getCupsourcebillid()
  {
    return this.m_cupsourcebillid;
  }

  public String getCupsourcebillrowid()
  {
    return this.m_cupsourcebillrowid;
  }

  public String getCcontractid()
  {
    return this.m_ccontractid;
  }

  public String getCcontractrowid()
  {
    return this.m_ccontractrowid;
  }

  public String getCcontractrcode()
  {
    return this.m_ccontractrcode;
  }

  public String getVmemo()
  {
    return this.m_vmemo;
  }

  public String getPKDefDoc1()
  {
    return this.m_pk_defdoc1;
  }

  public String getPKDefDoc2()
  {
    return this.m_pk_defdoc2;
  }

  public String getPKDefDoc3()
  {
    return this.m_pk_defdoc3;
  }

  public String getPKDefDoc4()
  {
    return this.m_pk_defdoc4;
  }

  public String getPKDefDoc5()
  {
    return this.m_pk_defdoc5;
  }

  public String getPKDefDoc6()
  {
    return this.m_pk_defdoc6;
  }

  public String getPKDefDoc7()
  {
    return this.m_pk_defdoc7;
  }

  public String getPKDefDoc8()
  {
    return this.m_pk_defdoc8;
  }

  public String getPKDefDoc9()
  {
    return this.m_pk_defdoc9;
  }

  public String getPKDefDoc10()
  {
    return this.m_pk_defdoc10;
  }

  public String getPKDefDoc11()
  {
    return this.m_pk_defdoc11;
  }

  public String getPKDefDoc12()
  {
    return this.m_pk_defdoc12;
  }

  public String getPKDefDoc13()
  {
    return this.m_pk_defdoc13;
  }

  public String getPKDefDoc14()
  {
    return this.m_pk_defdoc14;
  }

  public String getPKDefDoc15()
  {
    return this.m_pk_defdoc15;
  }

  public String getPKDefDoc16()
  {
    return this.m_pk_defdoc16;
  }

  public String getPKDefDoc17()
  {
    return this.m_pk_defdoc17;
  }

  public String getPKDefDoc18()
  {
    return this.m_pk_defdoc18;
  }

  public String getPKDefDoc19()
  {
    return this.m_pk_defdoc19;
  }

  public String getPKDefDoc20()
  {
    return this.m_pk_defdoc20;
  }

  public void setPKDefDoc1(String newM_pk_defdoc1)
  {
    this.m_pk_defdoc1 = newM_pk_defdoc1;
  }

  public void setPKDefDoc2(String newM_pk_defdoc2)
  {
    this.m_pk_defdoc2 = newM_pk_defdoc2;
  }

  public void setPKDefDoc3(String newM_pk_defdoc3)
  {
    this.m_pk_defdoc3 = newM_pk_defdoc3;
  }

  public void setPKDefDoc4(String newM_pk_defdoc4)
  {
    this.m_pk_defdoc4 = newM_pk_defdoc4;
  }

  public void setPKDefDoc5(String newM_pk_defdoc5)
  {
    this.m_pk_defdoc5 = newM_pk_defdoc5;
  }

  public void setPKDefDoc6(String newM_pk_defdoc6)
  {
    this.m_pk_defdoc6 = newM_pk_defdoc6;
  }

  public void setPKDefDoc7(String newM_pk_defdoc7)
  {
    this.m_pk_defdoc7 = newM_pk_defdoc7;
  }

  public void setPKDefDoc8(String newM_pk_defdoc8)
  {
    this.m_pk_defdoc8 = newM_pk_defdoc8;
  }

  public void setPKDefDoc9(String newM_pk_defdoc9)
  {
    this.m_pk_defdoc9 = newM_pk_defdoc9;
  }

  public void setPKDefDoc10(String newM_pk_defdoc10)
  {
    this.m_pk_defdoc10 = newM_pk_defdoc10;
  }

  public void setPKDefDoc11(String newM_pk_defdoc11)
  {
    this.m_pk_defdoc11 = newM_pk_defdoc11;
  }

  public void setPKDefDoc12(String newM_pk_defdoc12)
  {
    this.m_pk_defdoc12 = newM_pk_defdoc12;
  }

  public void setPKDefDoc13(String newM_pk_defdoc13)
  {
    this.m_pk_defdoc13 = newM_pk_defdoc13;
  }

  public void setPKDefDoc14(String newM_pk_defdoc14)
  {
    this.m_pk_defdoc14 = newM_pk_defdoc14;
  }

  public void setPKDefDoc15(String newM_pk_defdoc15)
  {
    this.m_pk_defdoc15 = newM_pk_defdoc15;
  }

  public void setPKDefDoc16(String newM_pk_defdoc16)
  {
    this.m_pk_defdoc16 = newM_pk_defdoc16;
  }

  public void setPKDefDoc17(String newM_pk_defdoc17)
  {
    this.m_pk_defdoc17 = newM_pk_defdoc17;
  }

  public void setPKDefDoc18(String newM_pk_defdoc18)
  {
    this.m_pk_defdoc18 = newM_pk_defdoc18;
  }

  public void setPKDefDoc19(String newM_pk_defdoc19)
  {
    this.m_pk_defdoc19 = newM_pk_defdoc19;
  }

  public void setPKDefDoc20(String newM_pk_defdoc20)
  {
    this.m_pk_defdoc20 = newM_pk_defdoc20;
  }

  public String getVfree1()
  {
    return this.m_vfree1;
  }

  public String getVfree2()
  {
    return this.m_vfree2;
  }

  public String getVfree3()
  {
    return this.m_vfree3;
  }

  public String getVfree4()
  {
    return this.m_vfree4;
  }

  public String getVfree5()
  {
    return this.m_vfree5;
  }

  public String getVdef1()
  {
    return this.m_vdef1;
  }

  public String getVdef2()
  {
    return this.m_vdef2;
  }

  public String getVdef3()
  {
    return this.m_vdef3;
  }

  public String getVdef4()
  {
    return this.m_vdef4;
  }

  public String getVdef5()
  {
    return this.m_vdef5;
  }

  public String getVdef6()
  {
    return this.m_vdef6;
  }

  public String getVdef7()
  {
    return this.m_vdef7;
  }

  public String getVdef8()
  {
    return this.m_vdef8;
  }

  public String getVdef9()
  {
    return this.m_vdef9;
  }

  public String getVdef10()
  {
    return this.m_vdef10;
  }

  public String getVdef11()
  {
    return this.m_vdef11;
  }

  public String getVdef12()
  {
    return this.m_vdef12;
  }

  public String getVdef13()
  {
    return this.m_vdef13;
  }

  public String getVdef14()
  {
    return this.m_vdef14;
  }

  public String getVdef15()
  {
    return this.m_vdef15;
  }

  public String getVdef16()
  {
    return this.m_vdef16;
  }

  public String getVdef17()
  {
    return this.m_vdef17;
  }

  public String getVdef18()
  {
    return this.m_vdef18;
  }

  public String getVdef19()
  {
    return this.m_vdef19;
  }

  public String getVdef20()
  {
    return this.m_vdef20;
  }

  public void setCorder_bid(String newCorder_bid)
  {
    this.m_corder_bid = newCorder_bid;
  }

  public void setCorderid(String newCorderid)
  {
    this.m_corderid = newCorderid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setCmangid(String newCmangid)
  {
    this.m_cmangid = newCmangid;
  }

  public void setCbaseid(String newCbaseid)
  {
    this.m_cbaseid = newCbaseid;
  }

  public void setNordernum(UFDouble newNordernum)
  {
    this.m_nordernum = newNordernum;
  }

  public void setCassistunit(String newCassistunit)
  {
    this.m_cassistunit = newCassistunit;
  }

  public void setNassistnum(UFDouble newNassistnum)
  {
    this.m_nassistnum = newNassistnum;
  }

  public void setNdiscountrate(UFDouble newNdiscountrate)
  {
    this.m_ndiscountrate = newNdiscountrate;
  }

  public void setIdiscounttaxtype(Integer newIdiscounttaxtype)
  {
    this.m_idiscounttaxtype = newIdiscounttaxtype;
  }

  public void setNtaxrate(UFDouble newNtaxrate)
  {
    this.m_ntaxrate = newNtaxrate;
  }

  public void setCcurrencytypeid(String newCcurrencytypeid)
  {
    this.m_ccurrencytypeid = newCcurrencytypeid;
  }

  public void setNoriginalcurprice(UFDouble newNoriginalcurprice)
  {
    this.m_noriginalcurprice = newNoriginalcurprice;
  }

  public void setNoriginalcurmny(UFDouble newNoriginalcurmny)
  {
    this.m_noriginalcurmny = newNoriginalcurmny;
  }

  public void setNoriginaltaxmny(UFDouble newNoriginaltaxmny)
  {
    this.m_noriginaltaxmny = newNoriginaltaxmny;
  }

  public void setNoriginalsummny(UFDouble newNoriginalsummny)
  {
    this.m_noriginalsummny = newNoriginalsummny;
  }

  public void setNexchangeotobrate(UFDouble newNexchangeotobrate)
  {
    this.m_nexchangeotobrate = newNexchangeotobrate;
  }

  public void setNtaxmny(UFDouble newNtaxmny)
  {
    this.m_ntaxmny = newNtaxmny;
  }

  public void setNmoney(UFDouble newNmoney)
  {
    this.m_nmoney = newNmoney;
  }

  public void setNsummny(UFDouble newNsummny)
  {
    this.m_nsummny = newNsummny;
  }

  public void setNexchangeotoarate(UFDouble newNexchangeotoarate)
  {
    this.m_nexchangeotoarate = newNexchangeotoarate;
  }

  public void setNassistcurmny(UFDouble newNassistcurmny)
  {
    this.m_nassistcurmny = newNassistcurmny;
  }

  public void setNassisttaxmny(UFDouble newNassisttaxmny)
  {
    this.m_nassisttaxmny = newNassisttaxmny;
  }

  public void setNassistsummny(UFDouble newNassistsummny)
  {
    this.m_nassistsummny = newNassistsummny;
  }

  public void setNaccumarrvnum(UFDouble newNaccumarrvnum)
  {
    this.m_naccumarrvnum = newNaccumarrvnum;
  }

  public void setNaccumstorenum(UFDouble newNaccumstorenum)
  {
    this.m_naccumstorenum = newNaccumstorenum;
  }

  public void setNaccuminvoicenum(UFDouble newNaccuminvoicenum)
  {
    this.m_naccuminvoicenum = newNaccuminvoicenum;
  }

  public void setNaccumwastnum(UFDouble newNaccumwastnum)
  {
    this.m_naccumwastnum = newNaccumwastnum;
  }

  public void setDplanarrvdate(UFDate newDplanarrvdate)
  {
    this.m_dplanarrvdate = newDplanarrvdate;
  }

  public void setCwarehouseid(String newCwarehouseid)
  {
    this.m_cwarehouseid = newCwarehouseid;
  }

  public void setCreceiveaddress(String newCreceiveaddress)
  {
    this.m_creceiveaddress = newCreceiveaddress;
  }

  public void setCprojectid(String newCprojectid)
  {
    this.m_cprojectid = newCprojectid;
  }

  public void setCprojectphaseid(String newCprojectphaseid)
  {
    this.m_cprojectphaseid = newCprojectphaseid;
  }

  public void setCoperator(String newCoperator)
  {
    this.m_coperator = newCoperator;
  }

  public void setForderrowstatus(Integer newForderrowstatus)
  {
    this.m_forderrowstatus = newForderrowstatus;
  }

  public void setBisactive(UFBoolean newBisactive)
  {
    this.m_bisactive = newBisactive;
  }

  public void setCordersource(String newCordersource)
  {
    this.m_cordersource = newCordersource;
  }

  public void setCsourcebillid(String newCsourcebillid)
  {
    this.m_csourcebillid = newCsourcebillid;
  }

  public void setCsourcebillrow(String newCsourcebillrow)
  {
    this.m_csourcebillrow = newCsourcebillrow;
  }

  public void setCupsourcebilltype(String newCupsourcebilltype)
  {
    this.m_cupsourcebilltype = newCupsourcebilltype;
  }

  public void setCupsourcebillid(String newCupsourcebillid)
  {
    this.m_cupsourcebillid = newCupsourcebillid;
  }

  public void setCupsourcebillrowid(String newCupsourcebillrowid)
  {
    this.m_cupsourcebillrowid = newCupsourcebillrowid;
  }

  public void setCcontractid(String newCcontractid)
  {
    this.m_ccontractid = newCcontractid;
  }

  public void setCcontractrowid(String newCcontractrowid)
  {
    this.m_ccontractrowid = newCcontractrowid;
  }

  public void setCcontractrcode(String newCcontractrcode)
  {
    this.m_ccontractrcode = newCcontractrcode;
  }

  public void setVmemo(String newVmemo)
  {
    this.m_vmemo = newVmemo;
  }

  public void setVfree1(String newVfree1)
  {
    this.m_vfree1 = newVfree1;
  }

  public void setVfree2(String newVfree2)
  {
    this.m_vfree2 = newVfree2;
  }

  public void setVfree3(String newVfree3)
  {
    this.m_vfree3 = newVfree3;
  }

  public void setVfree4(String newVfree4)
  {
    this.m_vfree4 = newVfree4;
  }

  public void setVfree5(String newVfree5)
  {
    this.m_vfree5 = newVfree5;
  }

  public void setVdef1(String newVdef1)
  {
    this.m_vdef1 = newVdef1;
  }

  public void setVdef2(String newVdef2)
  {
    this.m_vdef2 = newVdef2;
  }

  public void setVdef3(String newVdef3)
  {
    this.m_vdef3 = newVdef3;
  }

  public void setVdef4(String newVdef4)
  {
    this.m_vdef4 = newVdef4;
  }

  public void setVdef5(String newVdef5)
  {
    this.m_vdef5 = newVdef5;
  }

  public void setVdef6(String newVdef6)
  {
    this.m_vdef6 = newVdef6;
  }

  public void setVdef7(String newVdef7)
  {
    this.m_vdef7 = newVdef7;
  }

  public void setVdef8(String newVdef8)
  {
    this.m_vdef8 = newVdef8;
  }

  public void setVdef9(String newVdef9)
  {
    this.m_vdef9 = newVdef9;
  }

  public void setVdef10(String newVdef10)
  {
    this.m_vdef10 = newVdef10;
  }

  public void setVdef11(String newVdef11)
  {
    this.m_vdef11 = newVdef11;
  }

  public void setVdef12(String newVdef12)
  {
    this.m_vdef12 = newVdef12;
  }

  public void setVdef13(String newVdef13)
  {
    this.m_vdef13 = newVdef13;
  }

  public void setVdef14(String newVdef14)
  {
    this.m_vdef14 = newVdef14;
  }

  public void setVdef15(String newVdef15)
  {
    this.m_vdef15 = newVdef15;
  }

  public void setVdef16(String newVdef16)
  {
    this.m_vdef16 = newVdef16;
  }

  public void setVdef17(String newVdef17)
  {
    this.m_vdef17 = newVdef17;
  }

  public void setVdef18(String newVdef18)
  {
    this.m_vdef18 = newVdef18;
  }

  public void setVdef19(String newVdef19)
  {
    this.m_vdef19 = newVdef19;
  }

  public void setVdef20(String newVdef20)
  {
    this.m_vdef20 = newVdef20;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if ((this.m_cbaseid == null) || (this.m_cbaseid.trim().equals(""))) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000695")));
    }
    if (this.m_nordernum == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003581")));
    }
    if (this.m_noriginalcurprice == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000741")));
    }
    if ((this.m_ccurrencytypeid == null) || (this.m_ccurrencytypeid.trim().equals(""))) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001755")));
    }

    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      String strTmp = "";
      strTmp = strTmp + temp[0];
      for (int i = 1; i < temp.length; i++) {
        strTmp = strTmp + NCLangRes4VoTransl.getNCLangRes().getStrByID("40120003", "UPPSCMCommon-000000");
        strTmp = strTmp + temp[i];
      }
      StringBuffer message = new StringBuffer();
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40120003", "UPP40120003-000006", null, new String[] { strTmp }));
      throw new NullFieldException(message.toString());
    }
    if (((this.m_isassist == null) || (this.m_isassist.equals("N"))) && 
      (this.m_nassistnum != null)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40120003", "UPP40120003-000007"));
    }

    if ((this.m_isassist != null) && (this.m_isassist.equals("Y")) && 
      (this.m_nassistnum == null)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40120003", "UPP40120003-000008"));
    }

    if (this.m_nordernum.doubleValue() == 0.0D)
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40120003", "UPP40120003-000009"));
  }

  public String[] getAttributeNames()
  {
    return new String[] { "measrate", "corderid", "pk_corp", "cmangid", "cbaseid", "nordernum", "cassistunit",
    		"nassistnum", "ndiscountrate", "idiscounttaxtype", "ntaxrate", "ccurrencytypeid", "voriginalnetprice",
    		"noriginalcurprice", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "nexchangeotobrate",
    		"ntaxmny", "nmoney", "nsummny", "nexchangeotoarate", "nassistcurmny", "nassisttaxmny", "nassistsummny",
    		"naccumarrvnum", "naccumstorenum", "naccuminvoicenum", "naccumwastnum", "dplanarrvdate", "cwarehouseid", 
    		"creceiveaddress", "cprojectid", "cprojectphaseid", "coperator", "forderrowstatus", "bisactive", "cordersource",
    		"csourcebillid", "csourcebillrow", "cupsourcebilltype", "cupsourcebillid", "cupsourcebillrowid", "vmemo",
    		"vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5",
    		"vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", 
    		"vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5",
    		"pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13",
    		"pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20",
    		"vordercode", "cdeptid", "cvendorid", "employeeid", "cwareid", "ts", "m_norgtaxprice", "m_norgnettaxprice", 
    		"cupsourcehts", "cupsourcebts", "crowno", "nbackarrvnum", "nbackstorenum", "nnotelignum", "vproducenum",
    		"ccontractid", "ccontractrowid", "ccontractrcode", "vpriceauditcode", "cpriceauditid", "cpriceaudit_bid",
    		"cpriceaudit_bb1id", "convertrate", "narrvnum", "nprice"
    		,"bomvers" };//shikun
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("vordercode"))
      return this.vordercode;
    if (attributeName.equals("cvendorid"))
      return this.cvendorid;
    if (attributeName.equals("cdeptid"))
      return this.cdeptid;
    if (attributeName.equals("cemployeeid"))
      return this.cemployeeid;
    if (attributeName.equals("cwareid")) {
      return this.cwareid;
    }

    if (attributeName.equals("corder_bid"))
      return this.m_corder_bid;
    if (attributeName.equals("corderid"))
      return this.m_corderid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cmangid"))
      return this.m_cmangid;
    if (attributeName.equals("cbaseid"))
      return this.m_cbaseid;
    if (attributeName.equals("nordernum"))
      return this.m_nordernum;
    if (attributeName.equals("cassistunit"))
      return this.m_cassistunit;
    if (attributeName.equals("nassistnum"))
      return this.m_nassistnum;
    if (attributeName.equals("isassist"))
      return this.m_isassist;
    if (attributeName.equals("ndiscountrate"))
      return this.m_ndiscountrate;
    if (attributeName.equals("idiscounttaxtype"))
    {
      return this.m_idiscounttaxtype;
    }if (attributeName.equals("ntaxrate"))
      return this.m_ntaxrate;
    if (attributeName.equals("ccurrencytypeid"))
      return this.m_ccurrencytypeid;
    if (attributeName.equals("noriginalnetprice"))
      return this.m_noriginalnetprice;
    if (attributeName.equals("noriginalcurprice"))
      return this.m_noriginalcurprice;
    if (attributeName.equals("noriginalcurmny"))
      return this.m_noriginalcurmny;
    if (attributeName.equals("noriginaltaxmny"))
      return this.m_noriginaltaxmny;
    if (attributeName.equals("noriginalsummny"))
      return this.m_noriginalsummny;
    if (attributeName.equals("nexchangeotobrate"))
      return this.m_nexchangeotobrate;
    if (attributeName.equals("ntaxmny"))
      return this.m_ntaxmny;
    if (attributeName.equals("nmoney"))
      return this.m_nmoney;
    if (attributeName.equals("nsummny"))
      return this.m_nsummny;
    if (attributeName.equals("nexchangeotoarate"))
      return this.m_nexchangeotoarate;
    if (attributeName.equals("nassistcurmny"))
      return this.m_nassistcurmny;
    if (attributeName.equals("nassisttaxmny"))
      return this.m_nassisttaxmny;
    if (attributeName.equals("nassistsummny"))
      return this.m_nassistsummny;
    if (attributeName.equals("naccumarrvnum"))
      return this.m_naccumarrvnum;
    if (attributeName.equals("naccumstorenum"))
      return this.m_naccumstorenum;
    if (attributeName.equals("naccuminvoicenum"))
      return this.m_naccuminvoicenum;
    if (attributeName.equals("naccumwastnum"))
      return this.m_naccumwastnum;
    if (attributeName.equals("dplanarrvdate"))
      return this.m_dplanarrvdate;
    if (attributeName.equals("cwarehouseid"))
      return this.m_cwarehouseid;
    if (attributeName.equals("creceiveaddress"))
      return this.m_creceiveaddress;
    if (attributeName.equals("cprojectid"))
      return this.m_cprojectid;
    if (attributeName.equals("cprojectphaseid"))
      return this.m_cprojectphaseid;
    if (attributeName.equals("coperator"))
      return this.m_coperator;
    if (attributeName.equals("forderrowstatus"))
      return this.m_forderrowstatus;
    if (attributeName.equals("bisactive"))
      return this.m_bisactive;
    if (attributeName.equals("cordersource"))
      return this.m_cordersource;
    if (attributeName.equals("csourcebillid"))
      return this.m_csourcebillid;
    if (attributeName.equals("csourcebillrow"))
      return this.m_csourcebillrow;
    if (attributeName.equals("cupsourcebilltype"))
      return this.m_cupsourcebilltype;
    if (attributeName.equals("cupsourcebillid"))
      return this.m_cupsourcebillid;
    if (attributeName.equals("cupsourcebillrowid"))
      return this.m_cupsourcebillrowid;
    if (attributeName.equals("vmemo"))
      return this.m_vmemo;
    if (attributeName.equals("vfree0"))
      return this.m_vfree0;
    if (attributeName.equals("vfree1"))
      return this.m_vfree1;
    if (attributeName.equals("vfree2"))
      return this.m_vfree2;
    if (attributeName.equals("vfree3"))
      return this.m_vfree3;
    if (attributeName.equals("vfree4"))
      return this.m_vfree4;
    if (attributeName.equals("vfree5"))
      return this.m_vfree5;
    if (attributeName.equals("vdef1"))
      return this.m_vdef1;
    if (attributeName.equals("vdef2"))
      return this.m_vdef2;
    if (attributeName.equals("vdef3"))
      return this.m_vdef3;
    if (attributeName.equals("vdef4"))
      return this.m_vdef4;
    if (attributeName.equals("vdef5"))
      return this.m_vdef5;
    if (attributeName.equals("vdef6"))
      return this.m_vdef6;
    if (attributeName.equals("vdef7"))
      return this.m_vdef7;
    if (attributeName.equals("vdef8"))
      return this.m_vdef8;
    if (attributeName.equals("vdef9"))
      return this.m_vdef9;
    if (attributeName.equals("vdef10"))
      return this.m_vdef10;
    if (attributeName.equals("vdef11"))
      return this.m_vdef11;
    if (attributeName.equals("vdef12"))
      return this.m_vdef12;
    if (attributeName.equals("vdef13"))
      return this.m_vdef13;
    if (attributeName.equals("vdef14"))
      return this.m_vdef14;
    if (attributeName.equals("vdef15"))
      return this.m_vdef15;
    if (attributeName.equals("vdef16"))
      return this.m_vdef16;
    if (attributeName.equals("vdef17"))
      return this.m_vdef17;
    if (attributeName.equals("vdef18"))
      return this.m_vdef18;
    if (attributeName.equals("vdef19"))
      return this.m_vdef19;
    if (attributeName.equals("vdef20")) {
      return this.m_vdef20;
    }
    if (attributeName.equals("pk_defdoc1"))
      return this.m_pk_defdoc1;
    if (attributeName.equals("pk_defdoc2"))
      return this.m_pk_defdoc2;
    if (attributeName.equals("pk_defdoc3"))
      return this.m_pk_defdoc3;
    if (attributeName.equals("pk_defdoc4"))
      return this.m_pk_defdoc4;
    if (attributeName.equals("pk_defdoc5"))
      return this.m_pk_defdoc5;
    if (attributeName.equals("pk_defdoc6"))
      return this.m_pk_defdoc6;
    if (attributeName.equals("pk_defdoc7"))
      return this.m_pk_defdoc7;
    if (attributeName.equals("pk_defdoc8"))
      return this.m_pk_defdoc8;
    if (attributeName.equals("pk_defdoc9"))
      return this.m_pk_defdoc9;
    if (attributeName.equals("pk_defdoc10"))
      return this.m_pk_defdoc10;
    if (attributeName.equals("pk_defdoc11"))
      return this.m_pk_defdoc11;
    if (attributeName.equals("pk_defdoc12"))
      return this.m_pk_defdoc12;
    if (attributeName.equals("pk_defdoc13"))
      return this.m_pk_defdoc13;
    if (attributeName.equals("pk_defdoc14"))
      return this.m_pk_defdoc14;
    if (attributeName.equals("pk_defdoc15"))
      return this.m_pk_defdoc15;
    if (attributeName.equals("pk_defdoc16"))
      return this.m_pk_defdoc16;
    if (attributeName.equals("pk_defdoc17"))
      return this.m_pk_defdoc17;
    if (attributeName.equals("pk_defdoc18"))
      return this.m_pk_defdoc18;
    if (attributeName.equals("pk_defdoc19"))
      return this.m_pk_defdoc19;
    if (attributeName.equals("pk_defdoc20")) {
      return this.m_pk_defdoc20;
    }
    if (attributeName.equals("bismaterial"))
    {
      return this.m_invshow;
    }if (attributeName.equals("bbid"))
      return this.m_bbid;
    if (attributeName.equals("oldnum")) {
      return this.m_oldnum;
    }

    if (attributeName.equals("cinventorycode"))
      return this.m_invcode;
    if (attributeName.equals("cinventoryname"))
      return this.m_invname;
    if (attributeName.equals("invspec"))
      return this.m_invspec;
    if (attributeName.equals("invtype"))
      return this.m_invtype;
    if (attributeName.equals("cmeasdocname"))
      return this.m_vmeasdocname;
    if (attributeName.equals("cassistunitname"))
      return this.m_vassunitname;
    if (attributeName.equals("ccurrencytype"))
      return this.m_vcurrencyname;
    if (attributeName.equals("cwarehousename"))
      return this.m_vwarehousename;
    if (attributeName.equals("bomvers"))
        return this.m_bomvers;//shikun 
    if (attributeName.equals("cprojectname"))
      return this.m_vprojectname;
    if (attributeName.equals("cprojectphasename")) {
      return this.m_vprojectphasename;
    }

    if (attributeName.equals("ts")) {
      return this.m_ts;
    }

    if (attributeName.equals("norgtaxprice"))
      return this.m_norgtaxprice;
    if (attributeName.equals("norgnettaxprice"))
      return this.m_norgnettaxprice;
    if (attributeName.equals("cupsourcehts"))
      return this.m_cupsourcehts;
    if (attributeName.equals("cupsourcebts"))
      return this.m_cupsourcebts;
    if (attributeName.equals("crowno")) {
      return this.m_crowno;
    }

    if (attributeName.equals("vproducenum")) {
      return this.m_vproducenum;
    }

    if (attributeName.equals("nbackarrvnum"))
      return this.m_nbackarrvnum;
    if (attributeName.equals("nbackstorenum")) {
      return this.m_nbackstorenum;
    }

    if (attributeName.equals("nnotelignum"))
      return this.m_nnotelignum;
    if (attributeName.equals("measrate"))
      return this.m_measrate;
    if (attributeName.equals("ccontractid"))
      return this.m_ccontractid;
    if (attributeName.equals("ccontractrowid"))
      return this.m_ccontractrowid;
    if (attributeName.equals("ccontractrcode"))
      return this.m_ccontractrcode;
    if (attributeName.equals("vpriceauditcode"))
      return this.m_vpriceauditcode;
    if (attributeName.equals("cpriceauditid"))
      return this.m_cpriceauditid;
    if (attributeName.equals("cpriceaudit_bid"))
      return this.m_cpriceaudit_bid;
    if (attributeName.equals("cpriceaudit_bb1id"))
      return this.m_cpriceaudit_bb1id;
    if (attributeName.equals("convertrate"))
      return this.m_convertrate;
    if (attributeName.equals("narrvnum"))
      return this.m_narrvnum;
    if (attributeName.equals("nprice")) {
      return this.m_nprice;
    }
    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("vordercode"))
        this.vordercode = ((String)value);
      else if (name.equals("cvendorid"))
        this.cvendorid = ((String)value);
      else if (name.equals("cdeptid"))
        this.cdeptid = ((String)value);
      else if (name.equals("cemployeeid"))
        this.cemployeeid = ((String)value);
      else if (name.equals("cwareid")) {
        this.cwareid = ((String)value);
      }
      else if (name.equals("cinventorycode"))
        this.m_invcode = ((String)value);
      else if (name.equals("cinventoryname"))
        this.m_invname = ((String)value);
      else if (name.equals("invspec"))
        this.m_invspec = ((String)value);
      else if (name.equals("invtype"))
        this.m_invtype = ((String)value);
      else if (name.equals("cmeasdocname"))
        this.m_vmeasdocname = ((String)value);
      else if (name.equals("cassistunitname"))
        this.m_vassunitname = ((String)value);
      else if (name.equals("ccurrencytype"))
        this.m_vcurrencyname = ((String)value);
      else if (name.equals("cwarehousename"))
        this.m_vwarehousename = ((String)value);
      else if (name.equals("bomvers"))
          this.m_bomvers = ((String)value);//shikun 
      else if (name.equals("cprojectname"))
        this.m_vprojectname = ((String)value);
      else if (name.equals("cprojectphasename")) {
        this.m_vprojectphasename = ((String)value);
      }
      else if (name.equals("corder_bid"))
        this.m_corder_bid = ((String)value);
      else if (name.equals("corderid"))
        this.m_corderid = ((String)value);
      else if (name.equals("pk_corp"))
        this.m_pk_corp = ((String)value);
      else if (name.equals("cmangid"))
        this.m_cmangid = ((String)value);
      else if (name.equals("cbaseid"))
        this.m_cbaseid = ((String)value);
      else if (name.equals("nordernum"))
        this.m_nordernum = ((UFDouble)value);
      else if (name.equals("cassistunit"))
        this.m_cassistunit = ((String)value);
      else if (name.equals("nassistnum"))
        this.m_nassistnum = ((UFDouble)value);
      else if (name.equals("isassist"))
        this.m_isassist = ((String)value);
      else if (name.equals("ndiscountrate"))
        this.m_ndiscountrate = ((UFDouble)value);
      else if (name.equals("idiscounttaxtype")) {
        if (value.toString().trim().equals(ScConstants.TaxType_Including))
          this.m_idiscounttaxtype = new Integer(0);
        else if (value.toString().trim().equals(ScConstants.TaxType_Not_Including))
          this.m_idiscounttaxtype = new Integer(1);
        else if (value.toString().trim().equals(ScConstants.TaxType_No))
          this.m_idiscounttaxtype = new Integer(2);
        else
          this.m_idiscounttaxtype = new Integer(1);
      }
      else if (name.equals("ntaxrate"))
        this.m_ntaxrate = ((UFDouble)value);
      else if (name.equals("ccurrencytypeid"))
        this.m_ccurrencytypeid = ((String)value);
      else if (name.equals("noriginalnetprice"))
        this.m_noriginalnetprice = ((UFDouble)value);
      else if (name.equals("noriginalcurprice"))
        this.m_noriginalcurprice = ((UFDouble)value);
      else if (name.equals("noriginalcurmny"))
        this.m_noriginalcurmny = ((UFDouble)value);
      else if (name.equals("noriginaltaxmny"))
        this.m_noriginaltaxmny = ((UFDouble)value);
      else if (name.equals("noriginalsummny"))
        this.m_noriginalsummny = ((UFDouble)value);
      else if (name.equals("nexchangeotobrate"))
        this.m_nexchangeotobrate = ((UFDouble)value);
      else if (name.equals("ntaxmny"))
        this.m_ntaxmny = ((UFDouble)value);
      else if (name.equals("nmoney"))
        this.m_nmoney = ((UFDouble)value);
      else if (name.equals("nsummny"))
        this.m_nsummny = ((UFDouble)value);
      else if (name.equals("nexchangeotoarate"))
        this.m_nexchangeotoarate = ((UFDouble)value);
      else if (name.equals("nassistcurmny"))
        this.m_nassistcurmny = ((UFDouble)value);
      else if (name.equals("nassisttaxmny"))
        this.m_nassisttaxmny = ((UFDouble)value);
      else if (name.equals("nassistsummny"))
        this.m_nassistsummny = ((UFDouble)value);
      else if (name.equals("naccumarrvnum"))
        this.m_naccumarrvnum = ((UFDouble)value);
      else if (name.equals("naccumstorenum"))
        this.m_naccumstorenum = ((UFDouble)value);
      else if (name.equals("naccuminvoicenum"))
        this.m_naccuminvoicenum = ((UFDouble)value);
      else if (name.equals("naccumwastnum"))
        this.m_naccumwastnum = ((UFDouble)value);
      else if (name.equals("dplanarrvdate"))
        this.m_dplanarrvdate = ((UFDate)value);
      else if (name.equals("cwarehouseid"))
        this.m_cwarehouseid = ((String)value);
      else if (name.equals("creceiveaddress"))
        this.m_creceiveaddress = ((String)value);
      else if (name.equals("cprojectid"))
        this.m_cprojectid = ((String)value);
      else if (name.equals("cprojectphaseid"))
        this.m_cprojectphaseid = ((String)value);
      else if (name.equals("coperator"))
        this.m_coperator = ((String)value);
      else if (name.equals("forderrowstatus"))
        this.m_forderrowstatus = ((Integer)value);
      else if (name.equals("bisactive"))
        this.m_bisactive = ((UFBoolean)value);
      else if (name.equals("cordersource"))
        this.m_cordersource = ((String)value);
      else if (name.equals("csourcebillid"))
        this.m_csourcebillid = ((String)value);
      else if (name.equals("csourcebillrow"))
        this.m_csourcebillrow = ((String)value);
      else if (name.equals("cupsourcebilltype"))
        this.m_cupsourcebilltype = ((String)value);
      else if (name.equals("cupsourcebillid"))
        this.m_cupsourcebillid = ((String)value);
      else if (name.equals("cupsourcebillrowid"))
        this.m_cupsourcebillrowid = ((String)value);
      else if (name.equals("vmemo"))
        this.m_vmemo = ((String)value);
      else if (name.equals("vfree0"))
        this.m_vfree0 = ((String)value);
      else if (name.equals("vfree1"))
        this.m_vfree1 = ((String)value);
      else if (name.equals("vfree2"))
        this.m_vfree2 = ((String)value);
      else if (name.equals("vfree3"))
        this.m_vfree3 = ((String)value);
      else if (name.equals("vfree4"))
        this.m_vfree4 = ((String)value);
      else if (name.equals("vfree5"))
        this.m_vfree5 = ((String)value);
      else if (name.equals("vdef1"))
        this.m_vdef1 = (value == null ? null : value.toString());
      else if (name.equals("vdef2"))
        this.m_vdef2 = (value == null ? null : value.toString());
      else if (name.equals("vdef3"))
        this.m_vdef3 = (value == null ? null : value.toString());
      else if (name.equals("vdef4"))
        this.m_vdef4 = (value == null ? null : value.toString());
      else if (name.equals("vdef5"))
        this.m_vdef5 = (value == null ? null : value.toString());
      else if (name.equals("vdef6"))
        this.m_vdef6 = (value == null ? null : value.toString());
      else if (name.equals("vdef7"))
        this.m_vdef7 = (value == null ? null : value.toString());
      else if (name.equals("vdef8"))
        this.m_vdef8 = (value == null ? null : value.toString());
      else if (name.equals("vdef9"))
        this.m_vdef9 = (value == null ? null : value.toString());
      else if (name.equals("vdef10"))
        this.m_vdef10 = (value == null ? null : value.toString());
      else if (name.equals("vdef11"))
        this.m_vdef11 = (value == null ? null : value.toString());
      else if (name.equals("vdef12"))
        this.m_vdef12 = (value == null ? null : value.toString());
      else if (name.equals("vdef13"))
        this.m_vdef13 = (value == null ? null : value.toString());
      else if (name.equals("vdef14"))
        this.m_vdef14 = (value == null ? null : value.toString());
      else if (name.equals("vdef15"))
        this.m_vdef15 = (value == null ? null : value.toString());
      else if (name.equals("vdef16"))
        this.m_vdef16 = (value == null ? null : value.toString());
      else if (name.equals("vdef17"))
        this.m_vdef17 = (value == null ? null : value.toString());
      else if (name.equals("vdef18"))
        this.m_vdef18 = (value == null ? null : value.toString());
      else if (name.equals("vdef19"))
        this.m_vdef19 = (value == null ? null : value.toString());
      else if (name.equals("vdef20"))
        this.m_vdef20 = (value == null ? null : value.toString());
      else if (name.equals("pk_defdoc1"))
        this.m_pk_defdoc1 = ((String)value);
      else if (name.equals("pk_defdoc2"))
        this.m_pk_defdoc2 = ((String)value);
      else if (name.equals("pk_defdoc3"))
        this.m_pk_defdoc3 = ((String)value);
      else if (name.equals("pk_defdoc4"))
        this.m_pk_defdoc4 = ((String)value);
      else if (name.equals("pk_defdoc5"))
        this.m_pk_defdoc5 = ((String)value);
      else if (name.equals("pk_defdoc6"))
        this.m_pk_defdoc6 = ((String)value);
      else if (name.equals("pk_defdoc7"))
        this.m_pk_defdoc7 = ((String)value);
      else if (name.equals("pk_defdoc8"))
        this.m_pk_defdoc8 = ((String)value);
      else if (name.equals("pk_defdoc9"))
        this.m_pk_defdoc9 = ((String)value);
      else if (name.equals("pk_defdoc10"))
        this.m_pk_defdoc10 = ((String)value);
      else if (name.equals("pk_defdoc11"))
        this.m_pk_defdoc11 = ((String)value);
      else if (name.equals("pk_defdoc12"))
        this.m_pk_defdoc12 = ((String)value);
      else if (name.equals("pk_defdoc13"))
        this.m_pk_defdoc13 = ((String)value);
      else if (name.equals("pk_defdoc14"))
        this.m_pk_defdoc14 = ((String)value);
      else if (name.equals("pk_defdoc15"))
        this.m_pk_defdoc15 = ((String)value);
      else if (name.equals("pk_defdoc16"))
        this.m_pk_defdoc16 = ((String)value);
      else if (name.equals("pk_defdoc17"))
        this.m_pk_defdoc17 = ((String)value);
      else if (name.equals("pk_defdoc18"))
        this.m_pk_defdoc18 = ((String)value);
      else if (name.equals("pk_defdoc19"))
        this.m_pk_defdoc19 = ((String)value);
      else if (name.equals("pk_defdoc20")) {
        this.m_pk_defdoc20 = ((String)value);
      }
      else if (name.equals("bismaterial"))
      {
        this.m_invshow = ((String)value);
      } else if (name.equals("bbid"))
        this.m_bbid = ((String)value);
      else if (name.equals("oldnum")) {
        this.m_oldnum = ((UFDouble)value);
      }
      else if (name.equals("vfree0")) {
        if (this.m_freevo != null) {
          this.m_freevo.setAttributeValue(name, value == null ? "" : value.toString());
        } else if (this.m_freevo == null) {
          this.m_freevo = new FreeVO();
          this.m_freevo.setAttributeValue(name, value);
        }

      }
      else if (name.equals("ts")) {
        this.m_ts = ((String)value);
      }
      else if (name.equals("vproducenum")) {
        this.m_vproducenum = ((String)value);
      }
      else if (name.equals("ccontractid")) {
        this.m_ccontractid = ((String)value);
      }
      else if (name.equals("ccontractrowid")) {
        this.m_ccontractrowid = ((String)value);
      }
      else if (name.equals("ccontractrcode")) {
        this.m_ccontractrcode = ((String)value);
      }
      else if (name.equals("vpriceauditcode")) {
        this.m_vpriceauditcode = ((String)value);
      }
      else if (name.equals("cpriceauditid")) {
        this.m_cpriceauditid = ((String)value);
      }
      else if (name.equals("cpriceaudit_bid")) {
        this.m_cpriceaudit_bid = ((String)value);
      }
      else if (name.equals("cpriceaudit_bb1id")) {
        this.m_cpriceaudit_bb1id = ((String)value);
      }
      else if (name.equals("norgtaxprice"))
        this.m_norgtaxprice = ((UFDouble)value);
      else if (name.equals("norgnettaxprice"))
        this.m_norgnettaxprice = ((UFDouble)value);
      else if (name.equals("cupsourcehts"))
        this.m_cupsourcehts = ((String)value);
      else if (name.equals("cupsourcebts"))
        this.m_cupsourcebts = ((String)value);
      else if (name.equals("crowno")) {
        this.m_crowno = ((String)value);
      }
      else if (name.equals("nbackarrvnum"))
        this.m_nbackarrvnum = ((UFDouble)value);
      else if (name.equals("nbackstorenum")) {
        this.m_nbackstorenum = ((UFDouble)value);
      }
      else if (name.equals("nnotelignum"))
        this.m_nnotelignum = ((UFDouble)value);
      else if (name.equals("measrate")) {
        this.m_measrate = ((UFDouble)value);
      }
      else if (name.equals("convertrate"))
        this.m_convertrate = ((UFDouble)value);
      else if (name.equals("narrvnum"))
        this.m_narrvnum = ((UFDouble)value);
      else if (name.equals("nprice"))
        this.m_nprice = ((UFDouble)value);
    }
    catch (ClassCastException e) {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
    }
  }

  public void calPriceIncTax()
  {
    if ((this.m_nordernum != null) && (this.m_nordernum.doubleValue() != 0.0D) && (this.m_noriginalsummny != null));
  }

  public String getCbbid()
  {
    return this.m_bbid;
  }

  public String getCinvshow()
  {
    return this.m_invshow;
  }

  public String getCrowno()
  {
    return this.m_crowno;
  }

  public String getCupsourcebts()
  {
    return this.m_cupsourcebts;
  }

  public String getCupsourcehts()
  {
    return this.m_cupsourcehts;
  }

  public String getCwareid()
  {
    return this.cwareid;
  }

  public UFDouble getNbackarrvnum()
  {
    return this.m_nbackarrvnum;
  }

  public UFDouble getNbackstorenum()
  {
    return this.m_nbackstorenum;
  }

  public UFDouble getNnotelignum()
  {
    return this.m_nnotelignum;
  }

  public UFDouble getNoldnum()
  {
    return this.m_oldnum;
  }

  public UFDouble getNorgnettaxprice()
  {
    return this.m_norgnettaxprice;
  }

  public UFDouble getNorgtaxprice()
  {
    return this.m_norgtaxprice;
  }

  public UFDouble getNoriginalnetprice()
  {
    return this.m_noriginalnetprice;
  }

  public String getTs()
  {
    return this.m_ts;
  }

  public String getVassunitname()
  {
    return this.m_vassunitname;
  }

  public String getVcurrencyname()
  {
    return this.m_vcurrencyname;
  }

  public String getVfree0()
  {
    return this.m_vfree0;
  }

  public String getVinvcode()
  {
    return this.m_invcode;
  }

  public String getVinvname()
  {
    return this.m_invname;
  }

  public String getVinvspec()
  {
    return this.m_invspec;
  }

  public String getVinvtype()
  {
    return this.m_invtype;
  }

  public String getVmeasdocname()
  {
    return this.m_vmeasdocname;
  }

  public String getVprjectname()
  {
    return this.m_vprojectname;
  }

  public String getVprjectphasename()
  {
    return this.m_vprojectphasename;
  }

  public String getVproducenum()
  {
    return this.m_vproducenum;
  }

  public String getVwarehousename()
  {
    return this.m_vwarehousename;
  }

  public String getBomvers()
  {
    return this.m_bomvers;//shikun 
  }

  public void setCbbid(String bbid)
  {
    this.m_bbid = bbid;
  }

  public void setCinvshow(String invshow)
  {
    this.m_invshow = invshow;
  }

  public void setCrowno(String newCrowno)
  {
    this.m_crowno = newCrowno;
  }

  public void setCupsourcebts(String sNewValue)
  {
    this.m_cupsourcebts = sNewValue;
  }

  public void setCupsourcehts(String sNewValue)
  {
    this.m_cupsourcehts = sNewValue;
  }

  public void setCwareid(String newcwareid)
  {
    this.cwareid = newcwareid;
  }

  public void setNbackarrvnum(UFDouble newM_nbackarrvnum)
  {
    this.m_nbackarrvnum = newM_nbackarrvnum;
  }

  public void setNbackstorenum(UFDouble newM_nbackstorenum)
  {
    this.m_nbackstorenum = newM_nbackstorenum;
  }

  public void setNnotelignum(UFDouble newM_nnotelignum)
  {
    this.m_nnotelignum = newM_nnotelignum;
  }

  public void setNoldnum(UFDouble newOldnum)
  {
    this.m_oldnum = newOldnum;
  }

  public void setNorgnettaxprice(UFDouble newNorgnettaxprice)
  {
    this.m_norgnettaxprice = newNorgnettaxprice;
  }

  public void setNorgtaxprice(UFDouble newNorgtaxprice)
  {
    this.m_norgtaxprice = newNorgtaxprice;
  }

  public void setNoriginalnetprice(UFDouble newNoriginalnetprice)
  {
    this.m_noriginalnetprice = newNoriginalnetprice;
  }

  public void setTs(String sNewTs)
  {
    this.m_ts = sNewTs;
  }

  public void setVassunitname(String newVassunitname)
  {
    this.m_vassunitname = newVassunitname;
  }

  public void setVcurrencyname(String newVcurrencyname)
  {
    this.m_vcurrencyname = newVcurrencyname;
  }

  public void setVfree0(String newVfree0)
  {
    this.m_vfree0 = newVfree0;
  }

  public void setVinvcode(String newVinvcode)
  {
    this.m_invcode = newVinvcode;
  }

  public void setVinvname(String newVinvname)
  {
    this.m_invname = newVinvname;
  }

  public void setVinvspec(String newVinvspec)
  {
    this.m_invspec = newVinvspec;
  }

  public void setVinvtype(String newVinvtype)
  {
    this.m_invtype = newVinvtype;
  }

  public void setVmeasdocname(String newVmeasdocname)
  {
    this.m_vmeasdocname = newVmeasdocname;
  }

  public void setVproducenum(String newVproducenum)
  {
    this.m_vproducenum = newVproducenum;
  }

  public void setVprojectname(String newVprojectname)
  {
    this.m_vprojectname = newVprojectname;
  }

  public void setVprojectphasename(String newVprojectphasename)
  {
    this.m_vprojectphasename = newVprojectphasename;
  }

  public void setVwarehousename(String newVwarehousename)
  {
    this.m_vwarehousename = newVwarehousename;
  }

  public void setBomvers(String newBomvers)
  {
    this.m_bomvers = newBomvers;//shikun 
  }

  public String getVpriceauditcode()
  {
    return this.m_vpriceauditcode;
  }

  public String getCpriceauditid()
  {
    return this.m_cpriceauditid;
  }

  public String getCpriceaudit_bid()
  {
    return this.m_cpriceaudit_bid;
  }

  public String getCpriceaudit_bb1id()
  {
    return this.m_cpriceaudit_bb1id;
  }

  public void setVpriceauditcode(String newVpriceauditcode)
  {
    this.m_vpriceauditcode = newVpriceauditcode;
  }

  public void setCpriceauditid(String newCpriceauditid)
  {
    this.m_cpriceauditid = newCpriceauditid;
  }

  public void setCpriceaudit_bid(String newCpriceaudit_bid)
  {
    this.m_cpriceaudit_bid = newCpriceaudit_bid;
  }

  public void setCpriceaudit_bb1id(String newCpriceaudit_bb1id)
  {
    this.m_cpriceaudit_bb1id = newCpriceaudit_bb1id;
  }
}