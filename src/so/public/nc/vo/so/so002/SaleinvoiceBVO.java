package nc.vo.so.so002;

import java.util.ArrayList;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.IBillItemVO;
import nc.vo.scm.ic.bill.FreeItemDeal;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;

public class SaleinvoiceBVO extends CircularlyAccessibleValueObject
  implements IBillItemVO, FreeItemDeal
{
  public String m_cinvoice_bid;
  public String m_csaleid;
  public String m_cinventoryid;
  public String m_cunitid;
  public String m_ccustomerid;
  public String m_cpackunitid;
  public UFDouble m_nnumber;
  public UFDouble m_npacknumber;
  public UFDouble m_nbalancenumber;
  public String m_cbodywarehouseid;
  public String m_cupreceipttype;
  public String m_cupsourcebillid;
  public String m_cupsourcebillbodyid;
  public String m_creceipttype;
  public String m_csourcebillid;
  public String m_csourcebillbodyid;
  public UFBoolean m_blargessflag;
  public String m_cbatchid;
  public String m_ccurrencytypeid;
  public UFDouble m_nexchangeotobrate;
  public UFDouble m_nexchangeotoarate;
  public UFDouble m_nitemdiscountrate;
  public UFDouble m_ntaxrate;
  public UFDouble m_noriginalcurprice;
  public UFDouble m_noriginalcurtaxprice;
  public UFDouble m_noriginalcurnetprice;
  public UFDouble m_noriginalcurtaxnetprice;
  public UFDouble m_noriginalcurtaxmny;
  public UFDouble m_noriginalcurmny;
  public UFDouble m_noriginalcursummny;
  public UFDouble m_noriginalcurdiscountmny;
  public UFDouble m_nprice;
  public UFDouble m_ntaxprice;
  public UFDouble m_nnetprice;
  public UFDouble m_ntaxnetprice;
  public UFDouble m_ntaxmny;
  public UFDouble m_nmny;
  public UFDouble m_nsummny;
  public UFDouble m_ndiscountmny;
  public UFDouble m_nsimulatecostmny;
  public UFDouble m_ncostmny;
  public UFDate m_ddeliverdate;
  public Integer m_frowstatus;
  public String m_frownote;
  public UFBoolean m_assistunit = new UFBoolean(false);
  public UFBoolean m_bifinventoryfinish;
  public UFBoolean m_bifinvoicefinish;
  public UFBoolean m_bifpaybalance;
  public UFBoolean m_bifpayfinish;
  public UFBoolean m_bifpaysign;
  public UFBoolean m_bifreceiptfinish;
  public String m_cadvisecalbodyid;
  public String m_cfreezeid;
  public String m_cinvbasdocid;
  public String m_cInvSort;
  public String m_coriginalbillcode;
  public String m_cprojectid;
  public String m_cprojectid3;
  public String m_cprojectphaseid;
  public String m_creceiptcorpid;
  public String m_crowno;
  public String m_csale_bid;
  public String m_cStoreAdmin;
  public String m_ct_manageid;
  public String m_cupupreceipttype;
  public String m_cupupsourcebillbodyid;
  public String m_cupupsourcebillid;
  public UFBoolean m_discountflag = new UFBoolean(false);
  public Integer m_fbatchstatus;
  public UFDouble m_nassistcurdiscountmny;
  public UFDouble m_nassistcurmny;
  public UFDouble m_nassistcurnetprice;
  public UFDouble m_nassistcurprice;
  public UFDouble m_nassistcursummny;
  public UFDouble m_nassistcurtaxmny;
  public UFDouble m_nassistcurtaxnetprice;
  public UFDouble m_nassistcurtaxprice;
  public UFDouble m_ndiscountrate;
  public UFDouble m_ntotalbalancenumber;
  public UFDouble m_ntotalcostmny;
  public UFDouble m_ntotalinventorynumber;
  public UFDouble m_ntotalinvoicemny;
  public UFDouble m_ntotalinvoicenumber;
  public UFDouble m_ntotalpaymny;
  public UFDouble m_ntotalreceiptnumber;
  public UFDouble m_ntotalsignnumber;
  public String m_pk_corp;
  public UFDateTime m_ts;
  public String m_vdef1;
  public String m_vdef2;
  public String m_vdef3;
  public String m_vdef4;
  public String m_vdef5;
  public String m_vdef6;
  public String m_vfree0;
  public String m_vfree1;
  public String m_vfree2;
  public String m_vfree3;
  public String m_vfree4;
  public String m_vfree5;
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
  public UFDouble m_nquoteunitrate = null;
  public String m_ccustbaseid;
  public UFDouble m_salequoteorgcurtaxnetprice = null;

  public UFDouble m_outquoteorgcurtaxnetprice = null;

  public FreeVO m_freevo = null;

  public Integer m_isFreeItemMgt = null;
  public UFBoolean m_bqtfixedflag;
  public String m_cadvisecalbodyname = null;

  public String m_castunitid = null;

  public String m_castunitname = null;

  public String m_cbodywarehousename = null;

  public String m_ccurrencytypename = null;

  public String m_cinventorycode = null;

  public String m_cinvmanid = null;

  public String m_cpackunitname = null;

  public String m_cprojectname = null;

  public String m_cprojectphasename = null;
  public String m_cprolineid;
  public String m_cprolinename = null;
  public String m_cquoteunitid;
  public String m_creceiptcorpname = null;

  public String m_csourcebillcode = null;

  public String m_ct_code = null;

  public String m_cunitname = null;

  public String m_cupsourcebillcode = null;

  public String m_GGXX = null;

  public UFDouble m_hsl = null;

  public String m_invname = null;

  public String m_invspec = null;

  public String m_invtype = null;

  public Integer m_isSolidConvRate = null;

  public String m_measdocname = null;
  public UFDouble m_nqtscalefactor;
  public UFDouble m_nquotenetprice;
  public UFDouble m_nquotenumber;
  public UFDouble m_nquoteoriginalcurnetprice;
  public UFDouble m_nquoteoriginalcurprice;
  public UFDouble m_nquoteoriginalcurtaxnetprice;
  public UFDouble m_nquoteoriginalcurtaxprice;
  public UFDouble m_nquoteprice;
  public UFDouble m_nquotetaxnetprice;
  public UFDouble m_nquotetaxprice;
  public UFDouble m_nsubcursummny = null;
  public UFDouble m_nsubquotenetprice;
  public UFDouble m_nsubquoteprice;
  public UFDouble m_nsubquotetaxnetprice;
  public UFDouble m_nsubquotetaxprice;
  public UFDouble m_nsubsummny;
  public UFDouble m_nsubtaxnetprice;
  public String m_pk_measdoc = null;
  
  public UFDouble httsbl;
  public Integer httssl;
  public UFDouble sjtsje;
  public Integer sjtssl;
  public UFDouble ztsje;


  public String b_cjje1;
  public String b_cjje2;
  public String b_cjje3;

  public SaleinvoiceBVO()
  {
  }
  
  public String getB_cjje1() {
		return b_cjje1;
	}

	public void setB_cjje1(String b_cjje1) {
		this.b_cjje1 = b_cjje1;
	}

	public String getB_cjje2() {
		return b_cjje2;
	}

	public void setB_cjje2(String b_cjje2) {
		this.b_cjje2 = b_cjje2;
	}

	public String getB_cjje3() {
		return b_cjje3;
	}

	public void setB_cjje3(String b_cjje3) {
		this.b_cjje3 = b_cjje3;
	}

  public SaleinvoiceBVO(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public String getCcustbaseid()
  {
    return this.m_ccustbaseid;
  }

  public void setCcustbaseid(String newCcustomerid)
  {
    this.m_ccustbaseid = newCcustomerid;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone();
    } catch (Exception e) {
    }
    SaleinvoiceBVO saleinvoiceB = (SaleinvoiceBVO)o;

    return saleinvoiceB;
  }

  public String getPk_defdoc1()
  {
    return this.m_pk_defdoc1;
  }

  public String getPk_defdoc2()
  {
    return this.m_pk_defdoc2;
  }

  public String getPk_defdoc3()
  {
    return this.m_pk_defdoc3;
  }

  public String getPk_defdoc4()
  {
    return this.m_pk_defdoc4;
  }

  public String getPk_defdoc5()
  {
    return this.m_pk_defdoc5;
  }

  public String getPk_defdoc6()
  {
    return this.m_pk_defdoc6;
  }

  public String getPk_defdoc7()
  {
    return this.m_pk_defdoc7;
  }

  public String getPk_defdoc8()
  {
    return this.m_pk_defdoc8;
  }

  public String getPk_defdoc9()
  {
    return this.m_pk_defdoc9;
  }

  public String getPk_defdoc10()
  {
    return this.m_pk_defdoc10;
  }

  public String getPk_defdoc11()
  {
    return this.m_pk_defdoc11;
  }

  public String getPk_defdoc12()
  {
    return this.m_pk_defdoc12;
  }

  public String getPk_defdoc13()
  {
    return this.m_pk_defdoc13;
  }

  public String getPk_defdoc14()
  {
    return this.m_pk_defdoc14;
  }

  public String getPk_defdoc15()
  {
    return this.m_pk_defdoc15;
  }

  public String getPk_defdoc16()
  {
    return this.m_pk_defdoc16;
  }

  public String getPk_defdoc17()
  {
    return this.m_pk_defdoc17;
  }

  public String getPk_defdoc18()
  {
    return this.m_pk_defdoc18;
  }

  public String getPk_defdoc19()
  {
    return this.m_pk_defdoc19;
  }

  public String getPk_defdoc20()
  {
    return this.m_pk_defdoc20;
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

  public String getEntityName()
  {
    return "SaleinvoiceB";
  }

  public String getPrimaryKey()
  {
    return this.m_cinvoice_bid;
  }

  public void setPrimaryKey(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public String getCinvoice_bid()
  {
    return this.m_cinvoice_bid;
  }

  public String getCsaleid()
  {
    return this.m_csaleid;
  }

  public String getCinventoryid()
  {
    return this.m_cinventoryid;
  }

  public String getCunitid()
  {
    return this.m_cunitid;
  }

  public String getCpackunitid()
  {
    return this.m_cpackunitid;
  }

  public UFDouble getNnumber()
  {
    return this.m_nnumber;
  }

  public UFDouble getNpacknumber()
  {
    return this.m_npacknumber;
  }

  public UFDouble getNbalancenumber()
  {
    return this.m_nbalancenumber;
  }

  public String getCbodywarehouseid()
  {
    return this.m_cbodywarehouseid;
  }

  public String getCupreceipttype()
  {
    return this.m_cupreceipttype;
  }

  public String getCupsourcebillid()
  {
    return this.m_cupsourcebillid;
  }

  public String getCupsourcebillbodyid()
  {
    return this.m_cupsourcebillbodyid;
  }

  public String getCreceipttype()
  {
    return this.m_creceipttype;
  }

  public String getCsourcebillid()
  {
    return this.m_csourcebillid;
  }

  public String getCsourcebillbodyid()
  {
    return this.m_csourcebillbodyid;
  }

  public UFBoolean getBlargessflag()
  {
    return this.m_blargessflag;
  }

  public String getCbatchid()
  {
    return this.m_cbatchid;
  }

  public String getCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public UFDouble getNexchangeotobrate()
  {
    return this.m_nexchangeotobrate;
  }

  public UFDouble getNexchangeotoarate()
  {
    return this.m_nexchangeotoarate;
  }

  public UFDouble getNitemdiscountrate()
  {
    return this.m_nitemdiscountrate;
  }

  public UFDouble getNtaxrate()
  {
    return this.m_ntaxrate;
  }

  public UFDouble getNoriginalcurprice()
  {
    return this.m_noriginalcurprice;
  }

  public UFDouble getNoriginalcurtaxprice()
  {
    return this.m_noriginalcurtaxprice;
  }

  public UFDouble getNoriginalcurnetprice()
  {
    return this.m_noriginalcurnetprice;
  }

  public UFDouble getNoriginalcurtaxnetprice()
  {
    return this.m_noriginalcurtaxnetprice;
  }

  public UFDouble getNoriginalcurtaxmny()
  {
    return this.m_noriginalcurtaxmny;
  }

  public UFDouble getNoriginalcurmny()
  {
    return this.m_noriginalcurmny;
  }

  public UFDouble getNoriginalcursummny()
  {
    return this.m_noriginalcursummny;
  }

  public UFDouble getNoriginalcurdiscountmny()
  {
    return this.m_noriginalcurdiscountmny;
  }

  public UFDouble getNprice()
  {
    return this.m_nprice;
  }

  public UFDouble getNtaxprice()
  {
    return this.m_ntaxprice;
  }

  public UFDouble getNnetprice()
  {
    return this.m_nnetprice;
  }

  public UFDouble getNtaxnetprice()
  {
    return this.m_ntaxnetprice;
  }

  public UFDouble getNtaxmny()
  {
    return this.m_ntaxmny;
  }

  public UFDouble getNmny()
  {
    return this.m_nmny;
  }

  public UFDouble getNsummny()
  {
    return this.m_nsummny;
  }

  public UFDouble getNdiscountmny()
  {
    return this.m_ndiscountmny;
  }

  public UFDouble getNsimulatecostmny()
  {
    return this.m_nsimulatecostmny;
  }

  public UFDouble getNcostmny()
  {
    return this.m_ncostmny;
  }

  public UFDate getDdeliverdate()
  {
    return this.m_ddeliverdate;
  }

  public Integer getFrowstatus()
  {
    return this.m_frowstatus;
  }

  public String getFrownote()
  {
    return this.m_frownote;
  }

  public void setCinvoice_bid(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public void setCsaleid(String newCsaleid)
  {
    this.m_csaleid = newCsaleid;
  }

  public void setCinventoryid(String newCinventoryid)
  {
    this.m_cinventoryid = newCinventoryid;
  }

  public void setCunitid(String newCunitid)
  {
    this.m_cunitid = newCunitid;
  }

  public void setCpackunitid(String newCpackunitid)
  {
    this.m_cpackunitid = newCpackunitid;
  }

  public void setNnumber(UFDouble newNnumber)
  {
    this.m_nnumber = newNnumber;
  }

  public void setNpacknumber(UFDouble newNpacknumber)
  {
    this.m_npacknumber = newNpacknumber;
  }

  public void setNbalancenumber(UFDouble newNbalancenumber)
  {
    this.m_nbalancenumber = newNbalancenumber;
  }

  public void setCbodywarehouseid(String newCbodywarehouseid)
  {
    this.m_cbodywarehouseid = newCbodywarehouseid;
  }

  public void setCupreceipttype(String newCupreceipttype)
  {
    this.m_cupreceipttype = newCupreceipttype;
  }

  public void setCupsourcebillid(String newCupsourcebillid)
  {
    this.m_cupsourcebillid = newCupsourcebillid;
  }

  public void setCupsourcebillbodyid(String newCupsourcebillbodyid)
  {
    this.m_cupsourcebillbodyid = newCupsourcebillbodyid;
  }

  public void setCreceipttype(String newCreceipttype)
  {
    this.m_creceipttype = newCreceipttype;
  }

  public void setCsourcebillid(String newCsourcebillid)
  {
    this.m_csourcebillid = newCsourcebillid;
  }

  public void setCsourcebillbodyid(String newCsourcebillbodyid)
  {
    this.m_csourcebillbodyid = newCsourcebillbodyid;
  }

  public void setBlargessflag(UFBoolean newBlargessflag)
  {
    this.m_blargessflag = newBlargessflag;
  }

  public void setCbatchid(String newCbatchid)
  {
    this.m_cbatchid = newCbatchid;
  }

  public void setCcurrencytypeid(String newCcurrencytypeid)
  {
    this.m_ccurrencytypeid = newCcurrencytypeid;
  }

  public void setNexchangeotobrate(UFDouble newNexchangeotobrate)
  {
    this.m_nexchangeotobrate = newNexchangeotobrate;
  }

  public void setNexchangeotoarate(UFDouble newNexchangeotoarate)
  {
    this.m_nexchangeotoarate = newNexchangeotoarate;
  }

  public void setNitemdiscountrate(UFDouble newNitemdiscountrate)
  {
    this.m_nitemdiscountrate = newNitemdiscountrate;
  }

  public void setHsl(UFDouble newHsl)
  {
    this.m_hsl = newHsl;
  }

  public void setNtaxrate(UFDouble newNtaxrate)
  {
    this.m_ntaxrate = newNtaxrate;
  }

  public void setNoriginalcurprice(UFDouble newNoriginalcurprice)
  {
    this.m_noriginalcurprice = newNoriginalcurprice;
  }

  public void setNoriginalcurtaxprice(UFDouble newNoriginalcurtaxprice)
  {
    this.m_noriginalcurtaxprice = newNoriginalcurtaxprice;
  }

  public void setNoriginalcurnetprice(UFDouble newNoriginalcurnetprice)
  {
    this.m_noriginalcurnetprice = newNoriginalcurnetprice;
  }

  public void setNoriginalcurtaxnetprice(UFDouble newNoriginalcurtaxnetprice)
  {
    this.m_noriginalcurtaxnetprice = newNoriginalcurtaxnetprice;
  }

  public void setNoriginalcurtaxmny(UFDouble newNoriginalcurtaxmny)
  {
    this.m_noriginalcurtaxmny = newNoriginalcurtaxmny;
  }

  public void setNoriginalcurmny(UFDouble newNoriginalcurmny)
  {
    this.m_noriginalcurmny = newNoriginalcurmny;
  }

  public void setNoriginalcursummny(UFDouble newNoriginalcursummny)
  {
    this.m_noriginalcursummny = newNoriginalcursummny;
  }

  public void setNoriginalcurdiscountmny(UFDouble newNoriginalcurdiscountmny)
  {
    this.m_noriginalcurdiscountmny = newNoriginalcurdiscountmny;
  }

  public void setNprice(UFDouble newNprice)
  {
    this.m_nprice = newNprice;
  }

  public void setNtaxprice(UFDouble newNtaxprice)
  {
    this.m_ntaxprice = newNtaxprice;
  }

  public void setNnetprice(UFDouble newNnetprice)
  {
    this.m_nnetprice = newNnetprice;
  }

  public void setNtaxnetprice(UFDouble newNtaxnetprice)
  {
    this.m_ntaxnetprice = newNtaxnetprice;
  }

  public void setNtaxmny(UFDouble newNtaxmny)
  {
    this.m_ntaxmny = newNtaxmny;
  }

  public void setNmny(UFDouble newNmny)
  {
    this.m_nmny = newNmny;
  }

  public void setNsummny(UFDouble newNsummny)
  {
    this.m_nsummny = newNsummny;
  }

  public void setNdiscountmny(UFDouble newNdiscountmny)
  {
    this.m_ndiscountmny = newNdiscountmny;
  }

  public void setNsimulatecostmny(UFDouble newNsimulatecostmny)
  {
    this.m_nsimulatecostmny = newNsimulatecostmny;
  }

  public void setNcostmny(UFDouble newNcostmny)
  {
    this.m_ncostmny = newNcostmny;
  }

  public void setDdeliverdate(UFDate newDdeliverdate)
  {
    this.m_ddeliverdate = newDdeliverdate;
  }

  public void setFrowstatus(Integer newFrowstatus)
  {
    this.m_frowstatus = newFrowstatus;
  }

  public void setFrownote(String newFrownote)
  {
    this.m_frownote = newFrownote;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.m_cinventoryid == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001439"));
    }
    else if (this.m_cinventoryid.equals("")) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001439"));
    }

    if (this.m_ccurrencytypeid == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001755"));
    }
    else if (this.m_ccurrencytypeid.equals("")) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001755"));
    }

    if (this.m_nexchangeotobrate == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002092"));
    }

    boolean checkNum = false;
    if ((this.m_discountflag != null) && (!this.m_discountflag.booleanValue()) && 
      (this.m_nnumber == null)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002282"));

      checkNum = true;
    }

    boolean checkSummny = false;
    if ((this.m_discountflag != null) && (!this.m_discountflag.booleanValue()))
    {
      if ((this.m_ntaxrate == null) || (this.m_ntaxrate.toString().equals(""))) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003078"));
      }

      if ((this.m_noriginalcurtaxmny == null) || (this.m_noriginalcurtaxmny.toString().equals("")))
      {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003084"));
      }

      if ((this.m_noriginalcursummny == null) || (this.m_noriginalcursummny.toString().equals("")))
      {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000227"));

        checkSummny = true;
      }
      if ((this.m_ndiscountrate == null) || (this.m_ndiscountrate.toString().equals(""))) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002290"));
      }

      if ((this.m_noriginalcurnetprice == null) || (this.m_noriginalcurnetprice.toString().equals("")))
      {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002305"));
      }

      if ((this.m_noriginalcurtaxnetprice == null) || (this.m_noriginalcurtaxnetprice.toString().equals("")))
      {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001159"));
      }

      if ((this.m_noriginalcurmny == null) || (this.m_noriginalcurmny.toString().equals("")))
      {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002307"));
      }

    }

    if (((this.m_nnumber == null) || (this.m_nnumber.doubleValue() == 0.0D)) && ((this.m_noriginalcursummny == null) || (this.m_noriginalcursummny.doubleValue() == 0.0D)))
    {
      if ((!checkNum) && (!checkSummny)) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002282"));

        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000227"));
      }

    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000137"));

    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);

      message.append(temp[0].toString());
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));

        message.append(temp[i].toString());
      }

      throw new NullFieldException(message.toString());
    }

    if ((this.m_ndiscountmny != null) && (this.m_ndiscountmny.toString().length() > 16)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000148"));
    }

    if ((this.m_npacknumber != null) && (
      (this.m_cpackunitid == null) || (this.m_cpackunitid.length() == 0)))
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000149"));
  }

  public String[] getAttributeNames()
  {
    return new String[] { "pk_corp", "ts", "csaleid", "cinventoryid", "cunitid", "cpackunitid", "nnumber", "npacknumber", "nbalancenumber", "cbodywarehouseid", "cupreceipttype", "cupsourcebillid", "cupsourcebillbodyid", "cupupreceipttype", "cupupsourcebillid", "cupupsourcebillbodyid", "creceipttype", "csourcebillid", "csourcebillbodyid", "blargessflag", "cbatchid", "ccurrencytypeid", "nexchangeotobrate", "nexchangeotoarate", "nitemdiscountrate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "ddeliverdate", "frowstatus", "fbatchstatus", "frownote", "cinvbasdocid", "ntotalpaymny", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "bifinvoicefinish", "bifreceiptfinish", "bifinventoryfinish", "bifpayfinish", "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny", "nassistcurtaxnetprice", "nassistcurnetprice", "nassistcurtaxprice", "nassistcurprice", "cprojectid3", "cprojectid2", "cprojectid1", "vfree5", "vfree4", "vfree3", "vfree2", "vfree1", "vfree0", "vdef6", "vdef5", "vdef4", "vdef3", "vdef2", "vdef1", "ndiscountrate", "ct_manageid", "cfreezeid", "creceiptcorpid", "cadvisecalbodyid", "crowno", "coriginalbillcode", "cinventorycode", "cinventoryname", "cadvisecalbodyname", "cbodywarehousename", "ccurrencytypename", "cpackunitname", "cprojectname", "cprojectphasename", "creceiptcorpname", "csourcebillcode", "ct_code", "cunitname", "cupsourcebillcode", "GGXX", "cquoteunitid", "nquotenumber", "nquoteoriginalcurprice", "nquoteoriginalcurtaxprice", "nquoteoriginalcurnetprice", "nquoteoriginalcurtaxnetprice", "nquoteprice", "nquotetaxprice", "nquotenetprice", "nquotetaxnetprice", "nsubquoteprice", "nsubquotetaxprice", "nsubquotenetprice", "nsubquotetaxnetprice", "nsubsummny", "nsubtaxnetprice", "cprolineid", "bqtfixedflag", "nqtscalefactor", "cprolinename", "nsubcursummny", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", "nquoteunitrate", "ccustbaseid" ,"httsbl","httssl","sjtsje","sjtssl","ztsje","b_cjje1","b_cjje2","b_cjje3"};
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("cinvoice_bid"))
      return this.m_cinvoice_bid;
    if (attributeName.equals("csaleid"))
      return this.m_csaleid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cinventoryid"))
      return this.m_cinventoryid;
    if (attributeName.equals("cunitid"))
      return this.m_cunitid;
    if (attributeName.equals("cpackunitid"))
      return this.m_cpackunitid;
    if (attributeName.equals("nnumber"))
      return this.m_nnumber;
    if (attributeName.equals("npacknumber"))
      return this.m_npacknumber;
    if (attributeName.equals("nbalancenumber"))
      return this.m_nbalancenumber;
    if (attributeName.equals("cbodywarehouseid"))
      return this.m_cbodywarehouseid;
    if (attributeName.equals("cupreceipttype"))
      return this.m_cupreceipttype;
    if (attributeName.equals("cupsourcebillid"))
      return this.m_cupsourcebillid;
    if (attributeName.equals("cupsourcebillbodyid"))
      return this.m_cupsourcebillbodyid;
    if (attributeName.equals("cupupreceipttype"))
      return this.m_cupupreceipttype;
    if (attributeName.equals("cupupsourcebillid"))
      return this.m_cupupsourcebillid;
    if (attributeName.equals("cupupsourcebillbodyid"))
      return this.m_cupupsourcebillbodyid;
    if (attributeName.equals("creceipttype"))
      return this.m_creceipttype;
    if (attributeName.equals("csourcebillid"))
      return this.m_csourcebillid;
    if (attributeName.equals("csourcebillbodyid"))
      return this.m_csourcebillbodyid;
    if (attributeName.equals("blargessflag"))
      return this.m_blargessflag;
    if (attributeName.equals("cbatchid"))
      return this.m_cbatchid;
    if (attributeName.equals("ccurrencytypeid"))
      return this.m_ccurrencytypeid;
    if (attributeName.equals("nexchangeotobrate"))
      return this.m_nexchangeotobrate;
    if (attributeName.equals("nexchangeotoarate"))
      return this.m_nexchangeotoarate;
    if (attributeName.equals("ndiscountrate"))
      return this.m_ndiscountrate;
    if (attributeName.equals("nitemdiscountrate"))
      return this.m_nitemdiscountrate;
    if (attributeName.equals("ntaxrate"))
      return this.m_ntaxrate;
    if (attributeName.equals("noriginalcurprice"))
      return this.m_noriginalcurprice;
    if (attributeName.equals("noriginalcurtaxprice"))
      return this.m_noriginalcurtaxprice;
    if (attributeName.equals("noriginalcurnetprice"))
      return this.m_noriginalcurnetprice;
    if (attributeName.equals("noriginalcurtaxnetprice"))
      return this.m_noriginalcurtaxnetprice;
    if (attributeName.equals("noriginalcurtaxmny"))
      return this.m_noriginalcurtaxmny;
    if (attributeName.equals("noriginalcurmny"))
      return this.m_noriginalcurmny;
    if (attributeName.equals("noriginalcursummny"))
      return this.m_noriginalcursummny;
    if (attributeName.equals("noriginalcurdiscountmny"))
      return this.m_noriginalcurdiscountmny;
    if (attributeName.equals("nprice"))
      return this.m_nprice;
    if (attributeName.equals("ntaxprice"))
      return this.m_ntaxprice;
    if (attributeName.equals("nnetprice"))
      return this.m_nnetprice;
    if (attributeName.equals("ntaxnetprice"))
      return this.m_ntaxnetprice;
    if (attributeName.equals("ntaxmny"))
      return this.m_ntaxmny;
    if (attributeName.equals("nmny"))
      return this.m_nmny;
    if (attributeName.equals("nsummny"))
      return this.m_nsummny;
    if (attributeName.equals("ndiscountmny"))
      return this.m_ndiscountmny;
    if (attributeName.equals("nsimulatecostmny"))
      return this.m_nsimulatecostmny;
    if (attributeName.equals("ncostmny"))
      return this.m_ncostmny;
    if (attributeName.equals("ddeliverdate"))
      return this.m_ddeliverdate;
    if (attributeName.equals("frowstatus"))
      return this.m_frowstatus;
    if (attributeName.equals("fbatchstatus"))
      return this.m_fbatchstatus;
    if (attributeName.equals("frownote"))
      return this.m_frownote;
    if (attributeName.equals("cinvbasdocid"))
      return this.m_cinvbasdocid;
    if (attributeName.equals("ntotalpaymny"))
      return this.m_ntotalpaymny;
    if (attributeName.equals("ndiscountrate"))
      return this.m_ndiscountrate;
    if (attributeName.equals("ntotalreceiptnumber"))
      return this.m_ntotalreceiptnumber;
    if (attributeName.equals("ntotalinvoicenumber"))
      return this.m_ntotalinvoicenumber;
    if (attributeName.equals("ntotalinventorynumber"))
      return this.m_ntotalinventorynumber;
    if (attributeName.equals("bifinvoicefinish"))
      return this.m_bifinvoicefinish;
    if (attributeName.equals("bifreceiptfinish"))
      return this.m_bifreceiptfinish;
    if (attributeName.equals("bifinventoryfinish"))
      return this.m_bifinventoryfinish;
    if (attributeName.equals("bifpayfinish"))
      return this.m_bifpayfinish;
    if (attributeName.equals("nassistcurdiscountmny"))
      return this.m_nassistcurdiscountmny;
    if (attributeName.equals("nassistcursummny"))
      return this.m_nassistcursummny;
    if (attributeName.equals("nassistcurmny"))
      return this.m_nassistcurmny;
    if (attributeName.equals("nassistcurtaxmny"))
      return this.m_nassistcurtaxmny;
    if (attributeName.equals("nassistcurtaxnetprice"))
      return this.m_nassistcurtaxnetprice;
    if (attributeName.equals("nassistcurnetprice"))
      return this.m_nassistcurnetprice;
    if (attributeName.equals("nassistcurtaxprice"))
      return this.m_nassistcurtaxprice;
    if (attributeName.equals("nassistcurprice"))
      return this.m_nassistcurprice;
    if (attributeName.equals("cprojectid3"))
      return this.m_cprojectid3;
    if (attributeName.equals("cprojectphaseid"))
      return this.m_cprojectphaseid;
    if (attributeName.equals("cprojectid"))
      return this.m_cprojectid;
    if (attributeName.equals("vfree5"))
      return this.m_vfree5;
    if (attributeName.equals("vfree4"))
      return this.m_vfree4;
    if (attributeName.equals("vfree3"))
      return this.m_vfree3;
    if (attributeName.equals("vfree2"))
      return this.m_vfree2;
    if (attributeName.equals("vfree1"))
      return this.m_vfree1;
    if (attributeName.equals("vdef6"))
      return this.m_vdef6;
    if (attributeName.equals("vdef5"))
      return this.m_vdef5;
    if (attributeName.equals("vdef4"))
      return this.m_vdef4;
    if (attributeName.equals("vdef3"))
      return this.m_vdef3;
    if (attributeName.equals("vdef2"))
      return this.m_vdef2;
    if (attributeName.equals("vdef1")) {
      return this.m_vdef1;
    }

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
    if (attributeName.equals("vdef20"))
      return this.m_vdef20;
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

    if (attributeName.equals("discountflag"))
      return this.m_discountflag;
    if (attributeName.equals("assistunit"))
      return this.m_assistunit;
    if (attributeName.equals("ct_manageid"))
      return this.m_ct_manageid;
    if (attributeName.equals("cfreezeid"))
      return this.m_cfreezeid;
    if (attributeName.equals("ts"))
      return this.m_ts;
    if (attributeName.equals("vfree0"))
      return this.m_vfree0;
    if (attributeName.equals("creceiptcorpid"))
      return this.m_creceiptcorpid;
    if (attributeName.equals("cadvisecalbodyid"))
      return this.m_cadvisecalbodyid;
    if (attributeName.equals("cstoreadmin"))
      return this.m_cStoreAdmin;
    if (attributeName.equals("cinvsort"))
      return this.m_cInvSort;
    if (attributeName.equals("crowno"))
      return this.m_crowno;
    if (attributeName.equals("coriginalbillcode")) {
      return this.m_coriginalbillcode;
    }

    if (attributeName.equals("cinventorycode"))
      return this.m_cinventorycode;
    if (attributeName.equals("cinventoryname")) {
      return this.m_invname;
    }

    if (attributeName.equals("cadvisecalbodyname"))
      return this.m_cadvisecalbodyname;
    if (attributeName.equals("cbodywarehousename"))
      return this.m_cbodywarehousename;
    if (attributeName.equals("ccurrencytypename"))
      return this.m_ccurrencytypename;
    if (attributeName.equals("cpackunitname"))
      return this.m_cpackunitname;
    if (attributeName.equals("cprojectname"))
      return this.m_cprojectname;
    if (attributeName.equals("cprojectphasename"))
      return this.m_cprojectphasename;
    if (attributeName.equals("creceiptcorpname"))
      return this.m_creceiptcorpname;
    if (attributeName.equals("csourcebillcode"))
      return this.m_csourcebillcode;
    if (attributeName.equals("ct_code"))
      return this.m_ct_code;
    if (attributeName.equals("cunitname"))
      return this.m_cunitname;
    if (attributeName.equals("cupsourcebillcode"))
      return this.m_cupsourcebillcode;
    if (attributeName.equals("GGXX")) {
      return this.m_GGXX;
    }

    if (attributeName.equals("cquoteunitid"))
      return this.m_cquoteunitid;
    if (attributeName.equals("nquotenumber"))
      return this.m_nquotenumber;
    if (attributeName.equals("nquoteoriginalcurprice"))
      return this.m_nquoteoriginalcurprice;
    if (attributeName.equals("nquoteoriginalcurtaxprice"))
      return this.m_nquoteoriginalcurtaxprice;
    if (attributeName.equals("nquoteoriginalcurnetprice"))
      return this.m_nquoteoriginalcurnetprice;
    if (attributeName.equals("nquoteoriginalcurtaxnetprice"))
      return this.m_nquoteoriginalcurtaxnetprice;
    if (attributeName.equals("nquoteprice"))
      return this.m_nquoteprice;
    if (attributeName.equals("nquotetaxprice"))
      return this.m_nquotetaxprice;
    if (attributeName.equals("nquotenetprice"))
      return this.m_nquotenetprice;
    if (attributeName.equals("nquotetaxnetprice"))
      return this.m_nquotetaxnetprice;
    if (attributeName.equals("nsubquoteprice"))
      return this.m_nsubquoteprice;
    if (attributeName.equals("nsubquotetaxprice"))
      return this.m_nsubquotetaxprice;
    if (attributeName.equals("nsubquotenetprice"))
      return this.m_nsubquotenetprice;
    if (attributeName.equals("nsubquotetaxnetprice"))
      return this.m_nsubquotetaxnetprice;
    if (attributeName.equals("nsubsummny"))
      return this.m_nsubsummny;
    if (attributeName.equals("nsubtaxnetprice")) {
      return this.m_nsubtaxnetprice;
    }

    if (attributeName.equals("cprolineid")) {
      return this.m_cprolineid;
    }

    if (attributeName.equals("bqtfixedflag"))
      return this.m_bqtfixedflag;
    if (attributeName.equals("nqtscalefactor")) {
      return this.m_nqtscalefactor;
    }

    if (attributeName.equals("cprolinename")) {
      return this.m_cprolinename;
    }

    if (attributeName.equals("nsubcursummny"))
      return this.m_nsubcursummny;
    if (attributeName.equals("nquoteunitrate")) {
      return this.m_nquoteunitrate;
    }
    if (attributeName.equals("ccustomerid")) {
      return this.m_ccustomerid;
    }
    if (attributeName.equals("ccustbaseid")) {
      return this.m_ccustbaseid;
    } if (attributeName.equals("httsbl")) {
      return this.httsbl;
    } if (attributeName.equals("httssl")) {
      return this.httssl;
    } if (attributeName.equals("sjtsje")) {
      return this.sjtsje;
    } if (attributeName.equals("sjtssl")) {
      return this.sjtssl;
    } if (attributeName.equals("ztsje")) {
      return this.ztsje;
    } if (attributeName.equals("b_cjje1")){
      return this.b_cjje1;
    } if (attributeName.equals("b_cjje2")){
      return this.b_cjje2;
    } if (attributeName.equals("b_cjje3")){
      return this.b_cjje3;
    }
 
    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.startsWith("vdef"))
        value = value == null ? (String)null : value.toString();
      if (name.equals("cinvoice_bid"))
        this.m_cinvoice_bid = ((String)value);
      else if (name.equals("csaleid"))
        this.m_csaleid = ((String)value);
      else if (name.equals("pk_corp"))
        this.m_pk_corp = ((String)value);
      else if (name.equals("cinventoryid"))
        this.m_cinventoryid = ((String)value);
      else if (name.equals("cunitid"))
        this.m_cunitid = ((String)value);
      else if (name.equals("cpackunitid"))
        this.m_cpackunitid = ((String)value);
      else if (name.equals("nnumber"))
        this.m_nnumber = ((UFDouble)value);
      else if (name.equals("npacknumber"))
        this.m_npacknumber = ((UFDouble)value);
      else if (name.equals("nbalancenumber"))
        this.m_nbalancenumber = ((UFDouble)value);
      else if (name.equals("cbodywarehouseid"))
        this.m_cbodywarehouseid = ((String)value);
      else if (name.equals("cupreceipttype"))
        this.m_cupreceipttype = ((String)value);
      else if (name.equals("cupsourcebillid"))
        this.m_cupsourcebillid = ((String)value);
      else if (name.equals("cupsourcebillbodyid"))
        this.m_cupsourcebillbodyid = ((String)value);
      else if (name.equals("cupupreceipttype"))
        this.m_cupupreceipttype = ((String)value);
      else if (name.equals("cupupsourcebillid"))
        this.m_cupupsourcebillid = ((String)value);
      else if (name.equals("cupupsourcebillbodyid"))
        this.m_cupupsourcebillbodyid = ((String)value);
      else if (name.equals("creceipttype"))
        this.m_creceipttype = ((String)value);
      else if (name.equals("csourcebillid"))
        this.m_csourcebillid = ((String)value);
      else if (name.equals("csourcebillbodyid"))
        this.m_csourcebillbodyid = ((String)value);
      else if (name.equals("blargessflag"))
        this.m_blargessflag = ((UFBoolean)value);
      else if (name.equals("cbatchid"))
        this.m_cbatchid = ((String)value);
      else if (name.equals("ccurrencytypeid"))
        this.m_ccurrencytypeid = ((String)value);
      else if (name.equals("nexchangeotobrate"))
        this.m_nexchangeotobrate = ((UFDouble)value);
      else if (name.equals("nexchangeotoarate"))
        this.m_nexchangeotoarate = ((UFDouble)value);
      else if (name.equals("nitemdiscountrate"))
        this.m_nitemdiscountrate = ((UFDouble)value);
      else if (name.equals("ntaxrate"))
        this.m_ntaxrate = ((UFDouble)value);
      else if (name.equals("noriginalcurprice"))
        this.m_noriginalcurprice = ((UFDouble)value);
      else if (name.equals("noriginalcurtaxprice"))
        this.m_noriginalcurtaxprice = ((UFDouble)value);
      else if (name.equals("noriginalcurnetprice"))
        this.m_noriginalcurnetprice = ((UFDouble)value);
      else if (name.equals("noriginalcurtaxnetprice"))
        this.m_noriginalcurtaxnetprice = ((UFDouble)value);
      else if (name.equals("noriginalcurtaxmny"))
        this.m_noriginalcurtaxmny = ((UFDouble)value);
      else if (name.equals("noriginalcurmny"))
        this.m_noriginalcurmny = ((UFDouble)value);
      else if (name.equals("noriginalcursummny"))
        this.m_noriginalcursummny = ((UFDouble)value);
      else if (name.equals("noriginalcurdiscountmny"))
        this.m_noriginalcurdiscountmny = ((UFDouble)value);
      else if (name.equals("nprice"))
        this.m_nprice = ((UFDouble)value);
      else if (name.equals("ntaxprice"))
        this.m_ntaxprice = ((UFDouble)value);
      else if (name.equals("nnetprice"))
        this.m_nnetprice = ((UFDouble)value);
      else if (name.equals("ntaxnetprice"))
        this.m_ntaxnetprice = ((UFDouble)value);
      else if (name.equals("ntaxmny"))
        this.m_ntaxmny = ((UFDouble)value);
      else if (name.equals("nmny"))
        this.m_nmny = ((UFDouble)value);
      else if (name.equals("nsummny"))
        this.m_nsummny = ((UFDouble)value);
      else if (name.equals("ndiscountmny"))
        this.m_ndiscountmny = ((UFDouble)value);
      else if (name.equals("nsimulatecostmny"))
        this.m_nsimulatecostmny = ((UFDouble)value);
      else if (name.equals("ncostmny"))
        this.m_ncostmny = ((UFDouble)value);
      else if (name.equals("ddeliverdate"))
        this.m_ddeliverdate = ((UFDate)value);
      else if (name.equals("frowstatus"))
        this.m_frowstatus = ((Integer)value);
      else if (name.equals("fbatchstatus"))
        this.m_fbatchstatus = ((Integer)value);
      else if (name.equals("frownote"))
        this.m_frownote = ((String)value);
      else if (name.equals("cinvbasdocid"))
        this.m_cinvbasdocid = ((String)value);
      else if (name.equals("ntotalpaymny"))
        this.m_ntotalpaymny = ((UFDouble)value);
      else if (name.equals("ndiscountrate"))
        this.m_ndiscountrate = ((UFDouble)value);
      else if (name.equals("ntotalpaymny"))
        this.m_ntotalpaymny = ((UFDouble)value);
      else if (name.equals("ntotalreceiptnumber"))
        this.m_ntotalreceiptnumber = ((UFDouble)value);
      else if (name.equals("ntotalinvoicenumber"))
        this.m_ntotalinvoicenumber = ((UFDouble)value);
      else if (name.equals("ntotalinventorynumber"))
        this.m_ntotalinventorynumber = ((UFDouble)value);
      else if (name.equals("bifinvoicefinish"))
        this.m_bifinvoicefinish = ((UFBoolean)value);
      else if (name.equals("bifreceiptfinish"))
        this.m_bifreceiptfinish = ((UFBoolean)value);
      else if (name.equals("bifinventoryfinish"))
        this.m_bifinventoryfinish = ((UFBoolean)value);
      else if (name.equals("bifpayfinish"))
        this.m_bifpayfinish = ((UFBoolean)value);
      else if (name.equals("nassistcurdiscountmny"))
        this.m_nassistcurdiscountmny = ((UFDouble)value);
      else if (name.equals("nassistcursummny"))
        this.m_nassistcursummny = ((UFDouble)value);
      else if (name.equals("nassistcurmny"))
        this.m_nassistcurmny = ((UFDouble)value);
      else if (name.equals("nassistcurtaxmny"))
        this.m_nassistcurtaxmny = ((UFDouble)value);
      else if (name.equals("nassistcurtaxnetprice"))
        this.m_nassistcurtaxnetprice = ((UFDouble)value);
      else if (name.equals("nassistcurnetprice"))
        this.m_nassistcurnetprice = ((UFDouble)value);
      else if (name.equals("nassistcurtaxprice"))
        this.m_nassistcurtaxprice = ((UFDouble)value);
      else if (name.equals("nassistcurprice"))
        this.m_nassistcurprice = ((UFDouble)value);
      else if (name.equals("cprojectid3"))
        this.m_cprojectid3 = ((String)value);
      else if (name.equals("cprojectphaseid"))
        this.m_cprojectphaseid = ((String)value);
      else if (name.equals("cprojectid"))
        this.m_cprojectid = ((String)value);
      else if (name.equals("vfree5"))
        this.m_vfree5 = ((String)value);
      else if (name.equals("vfree4"))
        this.m_vfree4 = ((String)value);
      else if (name.equals("vfree3"))
        this.m_vfree3 = ((String)value);
      else if (name.equals("vfree2"))
        this.m_vfree2 = ((String)value);
      else if (name.equals("vfree1"))
        this.m_vfree1 = ((String)value);
      else if (name.equals("vdef6"))
        this.m_vdef6 = (value == null ? null : value.toString());
      else if (name.equals("vdef5"))
        this.m_vdef5 = (value == null ? null : value.toString());
      else if (name.equals("vdef4"))
        this.m_vdef4 = (value == null ? null : value.toString());
      else if (name.equals("vdef3"))
        this.m_vdef3 = (value == null ? null : value.toString());
      else if (name.equals("vdef2"))
        this.m_vdef2 = (value == null ? null : value.toString());
      else if (name.equals("vdef1"))
        this.m_vdef1 = (value == null ? null : value.toString());
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
      else if (name.equals("discountflag"))
        this.m_discountflag = new UFBoolean(value == null ? "N" : value.toString());
      else if (name.equals("assistunit"))
        this.m_assistunit = new UFBoolean(value == null ? "N" : value.toString());
      else if (name.equals("ct_manageid"))
        this.m_ct_manageid = ((String)value);
      else if (name.equals("cfreezeid"))
        this.m_cfreezeid = ((String)value);
      else if (name.equals("ts"))
        this.m_ts = (value == null ? null : new UFDateTime(value.toString()));
      else if (name.equals("vfree0"))
        this.m_vfree0 = ((String)value);
      else if (name.equals("creceiptcorpid"))
        this.m_creceiptcorpid = ((String)value);
      else if (name.equals("cadvisecalbodyid"))
        this.m_cadvisecalbodyid = ((String)value);
      else if (name.equals("cstoreadmin"))
        this.m_cStoreAdmin = (value == null ? null : value.toString());
      else if (name.equals("cinvsort"))
        this.m_cInvSort = (value == null ? null : value.toString());
      else if (name.equals("crowno"))
        this.m_crowno = (value == null ? null : value.toString());
      else if (name.equals("coriginalbillcode")) {
        this.m_coriginalbillcode = (value == null ? null : value.toString());
      }
      else if (name.equals("cbodywarehousename"))
        this.m_cbodywarehousename = ((String)value);
      else if (name.equals("cadvisecalbodyname"))
        this.m_cadvisecalbodyname = ((String)value);
      else if (name.equals("cinventoryname"))
        this.m_invname = ((String)value);
      else if (name.equals("cinventorycode"))
        this.m_cinventorycode = ((String)value);
      else if (name.equals("ccurrencytypename"))
        this.m_ccurrencytypename = ((String)value);
      else if (name.equals("cpackunitname"))
        this.m_cpackunitname = ((String)value);
      else if (name.equals("cprojectname"))
        this.m_cprojectname = ((String)value);
      else if (name.equals("cprojectphasename"))
        this.m_cprojectphasename = ((String)value);
      else if (name.equals("creceiptcorpname"))
        this.m_creceiptcorpname = ((String)value);
      else if (name.equals("csourcebillcode"))
        this.m_csourcebillcode = ((String)value);
      else if (name.equals("ct_code"))
        this.m_ct_code = ((String)value);
      else if (name.equals("cunitname"))
        this.m_cunitname = ((String)value);
      else if (name.equals("cupsourcebillcode"))
        this.m_cupsourcebillcode = ((String)value);
      else if (name.equals("GGXX")) {
        this.m_GGXX = ((String)value);
      }
      else if (name.equals("cquoteunitid"))
        this.m_cquoteunitid = ((String)value);
      else if (name.equals("nquotenumber"))
        this.m_nquotenumber = ((UFDouble)value);
      else if (name.equals("nquoteoriginalcurprice"))
        this.m_nquoteoriginalcurprice = ((UFDouble)value);
      else if (name.equals("nquoteoriginalcurtaxprice"))
        this.m_nquoteoriginalcurtaxprice = ((UFDouble)value);
      else if (name.equals("nquoteoriginalcurnetprice"))
        this.m_nquoteoriginalcurnetprice = ((UFDouble)value);
      else if (name.equals("nquoteoriginalcurtaxnetprice"))
        this.m_nquoteoriginalcurtaxnetprice = ((UFDouble)value);
      else if (name.equals("nquoteprice"))
        this.m_nquoteprice = ((UFDouble)value);
      else if (name.equals("nquotetaxprice"))
        this.m_nquotetaxprice = ((UFDouble)value);
      else if (name.equals("nquotenetprice"))
        this.m_nquotenetprice = ((UFDouble)value);
      else if (name.equals("nquotetaxnetprice"))
        this.m_nquotetaxnetprice = ((UFDouble)value);
      else if (name.equals("nsubquoteprice"))
        this.m_nsubquoteprice = ((UFDouble)value);
      else if (name.equals("nsubquotetaxprice"))
        this.m_nsubquotetaxprice = ((UFDouble)value);
      else if (name.equals("nsubquotenetprice"))
        this.m_nsubquotenetprice = ((UFDouble)value);
      else if (name.equals("nsubquotetaxnetprice"))
        this.m_nsubquotetaxnetprice = ((UFDouble)value);
      else if (name.equals("nsubsummny")) {
        this.m_nsubsummny = ((UFDouble)value);
      }
      else if (name.equals("nsubtaxnetprice")) {
        this.m_nsubtaxnetprice = ((UFDouble)value);
      }
      else if (name.equals("cprolineid")) {
        this.m_cprolineid = ((String)value);
      }
      else if (name.equals("bqtfixedflag"))
        this.m_bqtfixedflag = ((value == null) || (value.toString().length() == 0) ? null : UFBoolean.valueOf(value.toString()));
      else if (name.equals("nqtscalefactor")) {
        this.m_nqtscalefactor = ((value == null) || (value.toString().length() == 0) ? null : new UFDouble(value.toString()));
      }
      else if (name.equals("cprolinename")) {
        this.m_cprolinename = ((String)value);
      }
      else if (name.equals("nsubcursummny"))
        this.m_nsubcursummny = ((UFDouble)value);
      else if (name.equals("nquoteunitrate")) {
        this.m_nquoteunitrate = ((UFDouble)value);
      }
      else if (name.equals("ccustomerid")) {
        this.m_ccustomerid = ((String)value);
      }
      else if (name.equals("ccustbaseid")) {
        this.m_ccustbaseid = ((String)value);
      }else if (name.equals("httsbl")) {
            this.httsbl= new UFDouble(value==null?"0.00":value.toString());
      }else  if (name.equals("httssl")) {
          this.httssl=new Integer(value==null?"0":value.toString());
      }else  if (name.equals("sjtsje")) {
          this.sjtsje=((UFDouble) value);
      }else  if (name.equals("sjtssl")) {
          this.sjtssl=((Integer) value);
      }else  if (name.equals("ztsje")) {
          this.ztsje=((UFDouble)value); 
      }else if (name.equals("b_cjje1")){
    	  this.b_cjje1=((String)value);
      }else if(name.equals("b_cjje2")){
    	  this.b_cjje2 = ((String)value);
      }else if(name.equals("b_cjje3")){
    	  this.b_cjje3 = ((String)value);
      }
    }
    catch (ClassCastException e)
    { 
    	e.printStackTrace();
      throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("sopub", "UPPsopub-000173", null, new String[] { name, value + "" }));
    }
  }

  public UFBoolean getAssistunit()
  {
    return this.m_assistunit;
  }

  public UFBoolean getBifinventoryfinish()
  {
    return this.m_bifinventoryfinish;
  }

  public UFBoolean getBifinvoicefinish()
  {
    return this.m_bifinvoicefinish;
  }

  public UFBoolean getBifpayfinish()
  {
    return this.m_bifpayfinish;
  }

  public UFBoolean getBifreceiptfinish()
  {
    return this.m_bifreceiptfinish;
  }

  public String getCcustomerid()
  {
    return this.m_ccustomerid;
  }

  public void setCcustomerid(String newCcustomerid)
  {
    this.m_ccustomerid = newCcustomerid;
  }

  public String getCadvisecalbodyid()
  {
    return this.m_cadvisecalbodyid;
  }

  public String getCfreezeid()
  {
    return this.m_cfreezeid;
  }

  public String getCinvbasdocid()
  {
    return this.m_cinvbasdocid;
  }

  public String getCoriginalbillcode()
  {
    return this.m_coriginalbillcode;
  }

  public String getCprojectid()
  {
    return this.m_cprojectid;
  }

  public String getCprojectid3()
  {
    return this.m_cprojectid3;
  }

  public String getCprojectphaseid()
  {
    return this.m_cprojectphaseid;
  }

  public String getCreceiptcorpid()
  {
    return this.m_creceiptcorpid;
  }

  public String getCsale_bid()
  {
    return this.m_csale_bid;
  }

  public String getCt_manageid()
  {
    return this.m_ct_manageid;
  }

  public String getCupupreceipttype()
  {
    return this.m_cupupreceipttype;
  }

  public String getCupupsourcebillbodyid()
  {
    return this.m_cupupsourcebillbodyid;
  }

  public String getCupupsourcebillid()
  {
    return this.m_cupupsourcebillid;
  }

  public UFBoolean getDiscountflag()
  {
    return this.m_discountflag;
  }

  public Integer getFbatchstatus()
  {
    return this.m_fbatchstatus;
  }

  public String getInvSort()
  {
    return this.m_cInvSort;
  }

  public UFDouble getNassistcurdiscountmny()
  {
    return this.m_nassistcurdiscountmny;
  }

  public UFDouble getNassistcurmny()
  {
    return this.m_nassistcurmny;
  }

  public UFDouble getNassistcurnetprice()
  {
    return this.m_nassistcurnetprice;
  }

  public UFDouble getNassistcurprice()
  {
    return this.m_nassistcurprice;
  }

  public UFDouble getNassistcursummny()
  {
    return this.m_nassistcursummny;
  }

  public UFDouble getNassistcurtaxmny()
  {
    return this.m_nassistcurtaxmny;
  }

  public UFDouble getNassistcurtaxnetprice()
  {
    return this.m_nassistcurtaxnetprice;
  }

  public UFDouble getNassistcurtaxprice()
  {
    return this.m_nassistcurtaxprice;
  }

  public UFDouble getNdiscountrate()
  {
    return this.m_ndiscountrate;
  }

  public UFDouble getNtotalcostmny()
  {
    return this.m_ntotalcostmny;
  }

  public UFDouble getNtotalinventorynumber()
  {
    return this.m_ntotalinventorynumber;
  }

  public UFDouble getNtotalinvoicenumber()
  {
    return this.m_ntotalinvoicenumber;
  }

  public UFDouble getNtotalpaymny()
  {
    return this.m_ntotalpaymny;
  }

  public UFDouble getNtotalreceiptnumber()
  {
    return this.m_ntotalreceiptnumber;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getrowno()
  {
    return this.m_crowno;
  }

  public String getStoreAdmin()
  {
    return this.m_cStoreAdmin;
  }

  public UFDateTime getTs()
  {
    return this.m_ts;
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

  public String getVfree0()
  {
    return this.m_vfree0;
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

  public void setAssistunit(UFBoolean newassistunit)
  {
    this.m_assistunit = newassistunit;
  }

  public void setBifinventoryfinish(UFBoolean newBifinventoryfinish)
  {
    this.m_bifinventoryfinish = newBifinventoryfinish;
  }

  public void setBifinvoicefinish(UFBoolean newBifinvoicefinish)
  {
    this.m_bifinvoicefinish = newBifinvoicefinish;
  }

  public void setBifpaybalance(UFBoolean newBifpaybalance)
  {
    this.m_bifpaybalance = newBifpaybalance;
  }

  public void setBifpayfinish(UFBoolean newBifpayfinish)
  {
    this.m_bifpayfinish = newBifpayfinish;
  }

  public void setBifpaysign(UFBoolean newBifpaysign)
  {
    this.m_bifpaysign = newBifpaysign;
  }

  public void setBifreceiptfinish(UFBoolean newBifreceiptfinish)
  {
    this.m_bifreceiptfinish = newBifreceiptfinish;
  }

  public void setCadvisecalbodyid(String newCbatchid)
  {
    this.m_cadvisecalbodyid = newCbatchid;
  }

  public void setCfreezeid(String newcfreezeid)
  {
    this.m_cfreezeid = newcfreezeid;
  }

  public void setCinvbasdocid(String newCinvbasdocid)
  {
    this.m_cinvbasdocid = newCinvbasdocid;
  }

  public void setCoriginalbillcode(String newCoriginalbillcode)
  {
    this.m_coriginalbillcode = newCoriginalbillcode;
  }

  public void setCprojectid(String newCprojectid)
  {
    this.m_cprojectid = newCprojectid;
  }

  public void setCprojectid3(String newCprojectid3)
  {
    this.m_cprojectid3 = newCprojectid3;
  }

  public void setCprojectphaseid(String newCprojectphaseid)
  {
    this.m_cprojectphaseid = newCprojectphaseid;
  }

  public void setCreceiptcorpid(String newValue)
  {
    this.m_creceiptcorpid = newValue;
  }

  public void setCsale_bid(String newCsale_bid)
  {
    this.m_csale_bid = newCsale_bid;
  }

  public void setCt_manageid(String newct_manageid)
  {
    this.m_ct_manageid = newct_manageid;
  }

  public void setCupupreceipttype(String newCupupreceipttype)
  {
    this.m_cupupreceipttype = newCupupreceipttype;
  }

  public void setCupupsourcebillbodyid(String newCupupsourcebillbodyid)
  {
    this.m_cupupsourcebillbodyid = newCupupsourcebillbodyid;
  }

  public void setCupupsourcebillid(String newCupupsourcebillid)
  {
    this.m_cupupsourcebillid = newCupupsourcebillid;
  }

  public void setDiscountflag(UFBoolean newDiscountflag)
  {
    this.m_discountflag = newDiscountflag;
  }

  public void setFbatchstatus(Integer newFbatchstatus)
  {
    this.m_fbatchstatus = newFbatchstatus;
  }

  public void setInvSort(String newInvSort)
  {
    this.m_cInvSort = newInvSort;
  }

  public void setNassistcurdiscountmny(UFDouble newNassistcurdiscountmny)
  {
    this.m_nassistcurdiscountmny = newNassistcurdiscountmny;
  }

  public void setNassistcurmny(UFDouble newNassistcurmny)
  {
    this.m_nassistcurmny = newNassistcurmny;
  }

  public void setNassistcurnetprice(UFDouble newNassistcurnetprice)
  {
    this.m_nassistcurnetprice = newNassistcurnetprice;
  }

  public void setNassistcurprice(UFDouble newNassistcurprice)
  {
    this.m_nassistcurprice = newNassistcurprice;
  }

  public void setNassistcursummny(UFDouble newNassistcursummny)
  {
    this.m_nassistcursummny = newNassistcursummny;
  }

  public void setNassistcurtaxmny(UFDouble newNassistcurtaxmny)
  {
    this.m_nassistcurtaxmny = newNassistcurtaxmny;
  }

  public void setNassistcurtaxnetprice(UFDouble newNassistcurtaxnetprice)
  {
    this.m_nassistcurtaxnetprice = newNassistcurtaxnetprice;
  }

  public void setNassistcurtaxprice(UFDouble newNassistcurtaxprice)
  {
    this.m_nassistcurtaxprice = newNassistcurtaxprice;
  }

  public void setNdiscountrate(UFDouble newndiscountrate)
  {
    this.m_ndiscountrate = newndiscountrate;
  }

  public void setNtotalbalancenumber(UFDouble newNtotalbalancenumber)
  {
    this.m_ntotalbalancenumber = newNtotalbalancenumber;
  }

  public void setNtotalcostmny(UFDouble newNtotalcostmny)
  {
    this.m_ntotalcostmny = newNtotalcostmny;
  }

  public void setNtotalinventorynumber(UFDouble newNtotalinventorynumber)
  {
    this.m_ntotalinventorynumber = newNtotalinventorynumber;
  }

  public void setNtotalinvoicemny(UFDouble newNtotalinvoicemny)
  {
    this.m_ntotalinvoicemny = newNtotalinvoicemny;
  }

  public void setNtotalinvoicenumber(UFDouble newNtotalinvoicenumber)
  {
    this.m_ntotalinvoicenumber = newNtotalinvoicenumber;
  }

  public void setNtotalpaymny(UFDouble newNtotalpaymny)
  {
    this.m_ntotalpaymny = newNtotalpaymny;
  }

  public void setNtotalreceiptnumber(UFDouble newNtotalreceiptnumber)
  {
    this.m_ntotalreceiptnumber = newNtotalreceiptnumber;
  }

  public void setNtotalsignnumber(UFDouble newNtotalsignnumber)
  {
    this.m_ntotalsignnumber = newNtotalsignnumber;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setrowno(String newM_crowno)
  {
    this.m_crowno = newM_crowno;
  }

  public void setStoreAdmin(String newStoreAdmin)
  {
    this.m_cStoreAdmin = newStoreAdmin;
  }

  public void setTs(UFDateTime newTs)
  {
    this.m_ts = newTs;
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

  public void setPk_defdoc1(String newPk_defdoc1)
  {
    this.m_pk_defdoc1 = newPk_defdoc1;
  }

  public void setPk_defdoc2(String newPk_defdoc2)
  {
    this.m_pk_defdoc2 = newPk_defdoc2;
  }

  public void setPk_defdoc3(String newPk_defdoc3)
  {
    this.m_pk_defdoc3 = newPk_defdoc3;
  }

  public void setPk_defdoc4(String newPk_defdoc4)
  {
    this.m_pk_defdoc4 = newPk_defdoc4;
  }

  public void setPk_defdoc5(String newPk_defdoc5)
  {
    this.m_pk_defdoc5 = newPk_defdoc5;
  }

  public void setPk_defdoc6(String newPk_defdoc6)
  {
    this.m_pk_defdoc6 = newPk_defdoc6;
  }

  public void setPk_defdoc7(String newPk_defdoc7)
  {
    this.m_pk_defdoc7 = newPk_defdoc7;
  }

  public void setPk_defdoc8(String newPk_defdoc8)
  {
    this.m_pk_defdoc8 = newPk_defdoc8;
  }

  public void setPk_defdoc9(String newPk_defdoc9)
  {
    this.m_pk_defdoc9 = newPk_defdoc9;
  }

  public void setPk_defdoc10(String newPk_defdoc10)
  {
    this.m_pk_defdoc10 = newPk_defdoc10;
  }

  public void setPk_defdoc11(String newPk_defdoc11)
  {
    this.m_pk_defdoc11 = newPk_defdoc11;
  }

  public void setPk_defdoc12(String newPk_defdoc12)
  {
    this.m_pk_defdoc12 = newPk_defdoc12;
  }

  public void setPk_defdoc13(String newPk_defdoc13)
  {
    this.m_pk_defdoc13 = newPk_defdoc13;
  }

  public void setPk_defdoc14(String newPk_defdoc14)
  {
    this.m_pk_defdoc14 = newPk_defdoc14;
  }

  public void setPk_defdoc15(String newPk_defdoc15)
  {
    this.m_pk_defdoc15 = newPk_defdoc15;
  }

  public void setPk_defdoc16(String newPk_defdoc16)
  {
    this.m_pk_defdoc16 = newPk_defdoc16;
  }

  public void setPk_defdoc17(String newPk_defdoc17)
  {
    this.m_pk_defdoc17 = newPk_defdoc17;
  }

  public void setPk_defdoc18(String newPk_defdoc18)
  {
    this.m_pk_defdoc18 = newPk_defdoc18;
  }

  public void setPk_defdoc19(String newPk_defdoc19)
  {
    this.m_pk_defdoc19 = newPk_defdoc19;
  }

  public void setPk_defdoc20(String newPk_defdoc20)
  {
    this.m_pk_defdoc20 = newPk_defdoc20;
  }

  public void setVfree0(String newVfree0)
  {
    this.m_vfree0 = newVfree0;
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

  public void setFreeItemVO(FreeVO newM_freevo)
  {
    this.m_isFreeItemMgt = new Integer(0);
    if (newM_freevo != null) {
      this.m_freevo = ((FreeVO)newM_freevo.clone());
      Object oFree = null;
      int i = 0;
      for (i = 1; i <= 10; i++) {
        oFree = newM_freevo.getAttributeValue("vfreeid" + i);
        if ((oFree != null) && (oFree.toString().trim().length() > 0)) {
          break;
        }
      }
      if (i <= 10)
        this.m_isFreeItemMgt = new Integer(1);
    }
  }

  public UFBoolean getBqtfixedflag()
  {
    return this.m_bqtfixedflag;
  }

  public String getCastunitid()
  {
    return this.m_castunitid;
  }

  public String getCprolineid() {
    return this.m_cprolineid;
  }

  public String getCquoteunitid() {
    return this.m_cquoteunitid;
  }

  public String getCupsourcebillcode()
  {
    return this.m_cupsourcebillcode;
  }

  public FreeVO getFreeItemVO()
  {
    if (this.m_freevo != null) {
      return (FreeVO)this.m_freevo.clone();
    }
    return null;
  }

  public InvVO getInv()
  {
    InvVO voInv = new InvVO();
    voInv.setCinventoryid(this.m_cinventoryid);
    voInv.setCinvmanid(this.m_cinvmanid);
    voInv.setCinventorycode(this.m_cinventorycode);
    voInv.setInvname(this.m_invname);
    voInv.setInvspec(this.m_invspec);
    voInv.setInvtype(this.m_invtype);
    voInv.setPk_measdoc(this.m_pk_measdoc);
    voInv.setMeasdocname(this.m_measdocname);
    voInv.setCastunitid(this.m_castunitid);
    voInv.setCastunitname(this.m_castunitname);

    voInv.setHsl(this.m_hsl);
    voInv.setIsSolidConvRate(this.m_isSolidConvRate);

    voInv.setFreeItemVO(getFreeItemVO());
    voInv.setCprojectid(this.m_cprojectid);

    voInv.setDiscountflag(this.m_discountflag);

    if (this.m_freevo != null) {
      voInv.setFreeItemValue("vfree0", this.m_freevo.getWholeFreeItem() == null ? null : this.m_freevo.getWholeFreeItem().toString());
    }

    return voInv;
  }

  public UFDouble getNqtscalefactor()
  {
    return this.m_nqtscalefactor;
  }

  public UFDouble getNquotenetprice() {
    return this.m_nquotenetprice;
  }

  public UFDouble getNquotenumber() {
    return this.m_nquotenumber;
  }

  public UFDouble getNquoteoriginalcurnetprice() {
    return this.m_nquoteoriginalcurnetprice;
  }

  public UFDouble getNquoteoriginalcurprice() {
    return this.m_nquoteoriginalcurprice;
  }

  public UFDouble getNquoteoriginalcurtaxnetprice() {
    return this.m_nquoteoriginalcurtaxnetprice;
  }

  public UFDouble getNquoteoriginalcurtaxprice() {
    return this.m_nquoteoriginalcurtaxprice;
  }

  public UFDouble getNquoteprice() {
    return this.m_nquoteprice;
  }

  public UFDouble getNquotetaxnetprice() {
    return this.m_nquotetaxnetprice;
  }

  public UFDouble getNquotetaxprice() {
    return this.m_nquotetaxprice;
  }

  public UFDouble getNsubcursummny() {
    return this.m_nsubcursummny;
  }

  public UFDouble getNsubquotenetprice() {
    return this.m_nsubquotenetprice;
  }

  public UFDouble getNsubquoteprice() {
    return this.m_nsubquoteprice;
  }

  public UFDouble getNsubquotetaxnetprice() {
    return this.m_nsubquotetaxnetprice;
  }

  public UFDouble getNsubquotetaxprice() {
    return this.m_nsubquotetaxprice;
  }

  public UFDouble getNsubsummny() {
    return this.m_nsubsummny;
  }

  public UFDouble getNsubtaxnetprice()
  {
    return this.m_nsubtaxnetprice;
  }

  public void setBqtfixedflag(UFBoolean newBqtfixedflag)
  {
    this.m_bqtfixedflag = newBqtfixedflag;
  }

  public void setCprolineid(String newCprolineid)
  {
    this.m_cprolineid = newCprolineid;
  }

  public void setCquoteunitid(String newCquoteunitid)
  {
    this.m_cquoteunitid = newCquoteunitid;
  }

  public void setCupsourcebillcode(String newCupsourcebillcode)
  {
    this.m_cupsourcebillcode = newCupsourcebillcode;
  }

  public void setInv(InvVO voInv)
  {
    if (voInv == null)
      return;
    try {
      this.m_cinventoryid = voInv.getCinventoryid();
      this.m_cinvmanid = voInv.getCinvmanid();
      this.m_cinventorycode = voInv.getCinventorycode();
      this.m_invname = voInv.getInvname();
      this.m_invspec = voInv.getInvspec();
      this.m_invtype = voInv.getInvtype();
      this.m_pk_measdoc = voInv.getPk_measdoc();
      this.m_measdocname = voInv.getMeasdocname();
      this.m_castunitid = voInv.getCastunitid();
      this.m_castunitname = voInv.getCastunitname();
      this.m_hsl = voInv.getHsl();
      this.m_isSolidConvRate = voInv.getIsSolidConvRate();
      this.m_discountflag = voInv.getDiscountflag();
      if (voInv.getFreeItemVO() != null)
        this.m_freevo = ((FreeVO)voInv.getFreeItemVO().clone());
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
  }

  public void setNqtscalefactor(UFDouble newNqtscalefactor)
  {
    this.m_nqtscalefactor = newNqtscalefactor;
  }

  public void setNquotenetprice(UFDouble newNquotenetprice)
  {
    this.m_nquotenetprice = newNquotenetprice;
  }

  public void setNquotenumber(UFDouble newNquotenumber)
  {
    this.m_nquotenumber = newNquotenumber;
  }

  public void setNquoteoriginalcurnetprice(UFDouble newNquoteoriginalcurnetprice)
  {
    this.m_nquoteoriginalcurnetprice = newNquoteoriginalcurnetprice;
  }

  public void setNquoteoriginalcurprice(UFDouble newNquoteoriginalcurprice)
  {
    this.m_nquoteoriginalcurprice = newNquoteoriginalcurprice;
  }

  public void setNquoteoriginalcurtaxnetprice(UFDouble newNquoteoriginalcurtaxnetprice)
  {
    this.m_nquoteoriginalcurtaxnetprice = newNquoteoriginalcurtaxnetprice;
  }

  public void setNquoteoriginalcurtaxprice(UFDouble newNquoteoriginalcurtaxprice)
  {
    this.m_nquoteoriginalcurtaxprice = newNquoteoriginalcurtaxprice;
  }

  public void setNquoteprice(UFDouble newNquoteprice)
  {
    this.m_nquoteprice = newNquoteprice;
  }

  public void setNquotetaxnetprice(UFDouble newNquotetaxnetprice)
  {
    this.m_nquotetaxnetprice = newNquotetaxnetprice;
  }

  public void setNquotetaxprice(UFDouble newNquotetaxprice)
  {
    this.m_nquotetaxprice = newNquotetaxprice;
  }

  public void setNsubcursummny(UFDouble newNsubcursummny)
  {
    this.m_nsubcursummny = newNsubcursummny;
  }

  public void setNsubquotenetprice(UFDouble newNsubquotenetprice)
  {
    this.m_nsubquotenetprice = newNsubquotenetprice;
  }

  public void setNsubquoteprice(UFDouble newNsubquoteprice)
  {
    this.m_nsubquoteprice = newNsubquoteprice;
  }

  public void setNsubquotetaxnetprice(UFDouble newNsubquotetaxnetprice)
  {
    this.m_nsubquotetaxnetprice = newNsubquotetaxnetprice;
  }

  public void setNsubquotetaxprice(UFDouble newNsubquotetaxprice)
  {
    this.m_nsubquotetaxprice = newNsubquotetaxprice;
  }

  public void setNsubsummny(UFDouble newNsubsummny)
  {
    this.m_nsubsummny = newNsubsummny;
  }

  public void setNsubtaxnetprice(UFDouble newNsubtaxnetprice)
  {
    this.m_nsubtaxnetprice = newNsubtaxnetprice;
  }

  public UFDouble getNquoteunitrate()
  {
    return this.m_nquoteunitrate;
  }

  public void setNquoteunitrate(UFDouble nquoteunitrate)
  {
    this.m_nquoteunitrate = nquoteunitrate;
  }

  public UFDouble getM_salequoteorgcurtaxnetprice()
  {
    return this.m_salequoteorgcurtaxnetprice;
  }

  public void setM_salequoteorgcurtaxnetprice(UFDouble m_salequoteorgcurtaxnetprice)
  {
    this.m_salequoteorgcurtaxnetprice = m_salequoteorgcurtaxnetprice;
  }

  public UFDouble getM_outquoteorgcurtaxnetprice()
  {
    return this.m_outquoteorgcurtaxnetprice;
  }

  public void setM_outquoteorgcurtaxnetprice(UFDouble m_outquoteorgcurtaxnetprice)
  {
    this.m_outquoteorgcurtaxnetprice = m_outquoteorgcurtaxnetprice;
  }

public UFDouble getHttsbl()
{
	return httsbl;
}

public void setHttsbl(UFDouble httsbl)
{
	this.httsbl = httsbl;
}

public Integer getHttssl()
{
	return httssl;
}

public void setHttssl(Integer httssl)
{
	this.httssl = httssl;
}

public UFDouble getSjtsje()
{
	return sjtsje;
}

public void setSjtsje(UFDouble sjtsje)
{
	this.sjtsje = sjtsje;
}

public Integer getSjtssl()
{
	return sjtssl;
}

public void setSjtssl(Integer sjtssl)
{
	this.sjtssl = sjtssl;
}

public UFDouble getZtsje()
{
	return ztsje;
}

public void setZtsje(UFDouble ztsje)
{
	this.ztsje = ztsje;
}
}