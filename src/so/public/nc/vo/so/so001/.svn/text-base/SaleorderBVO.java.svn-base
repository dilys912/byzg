package nc.vo.so.so001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import nc.itf.scm.so.pub.ISOInventoryDiscount;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.so016.SoVoTools;

public class SaleorderBVO extends CircularlyAccessibleValueObject
  implements Comparable, ISOInventoryDiscount
{
  public String m_corder_bid;
  public String m_csaleid;
  public String m_creceipttype;
  public String m_csourcebillid;
  public String m_csourcebillbodyid;
  public String m_cinventoryid;
  public String m_cunitid;
  public String m_cpackunitid;
  public String m_coperatorid;
  public Integer m_frowstatus;
  public String m_frownote;
  public UFDouble tsbl;
  private static transient SOField[] addSaleExecuteFields = null;

  private static transient SOField[] addSaleOrderBFields = null;

  private static transient HashMap arrhs = null;

  public static final String[] arrNames = {"corder_bid", "cstoreadmin", "cinvsort", "crowno", "ctinvclassid", "cinvbasdocid", "cbatchid", "csaleid", "pk_corp", "creceipttype", "csourcebillid", "csourcebillbodyid", "cinventoryid", "cunitid", "cpackunitid", "nnumber", "npacknumber", "cbodywarehouseid", "dconsigndate", "ddeliverdate", "blargessflag", "ceditsaleid", "beditflag", "veditreason", "ccurrencytypeid", "nitemdiscountrate", "ndiscountrate", "nexchangeotobrate", "nexchangeotoarate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "coperatorid", "frowstatus", "fbatchstatus", "frownote", "ntotalpaymny", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "bifinvoicefinish", "bifreceiptfinish", "bifinventoryfinish", "bifpayfinish", "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny", "nassistcurtaxnetprice", "nassistcurnetprice", "nassistcurtaxprice", "nassistcurprice", "cprojectid3", "cprojectphaseid", "cprojectid", "vfree5", "vfree4", "vfree3", "vfree2", "vfree1", "vfree0", "vdef6", "vdef5", "vdef4", "vdef3", "vdef2", "vdef1", "discountflag", "assistunit", "ct_manageid", "cbomorderid", "cfreezeid", "ts", "laborflag", "norgviaprice", "cadvisecalbodyid", "boosflag", "bsupplyflag", "creceiptareaid", "vreceiveaddress", "creceiptcorpid", "ntotalplanreceiptnumber", "isappendant", "invoiceauditnumber", "receiptauditnumber", "outstoreauditnumber", "ntotalreturnnumber", "ntotalcarrynumber", "ntotalsignnumber", "nlinenumber", "vfreeid1", "vfreeid2", "vfreeid3", "vfreeid4", "vfreeid5", "vfreeid6", "vfreeid7", "vfreeid8", "vfreeid9", "vfreeid10", "vfreename1", "vfreename2", "vfreename3", "vfreename4", "vfreename5", "vfreename6", "vfreename7", "vfreename8", "vfreename9", "vfreename10", "cstoreadmin", "cinvsort", "finished", "cadvisecalbody", "cbodywarehousename", "cbomordercode", "ccurrencytypename", "cinventorycode", "cinventoryname", "GGXX", "cpackunitname", "cprojectname", "cprojectphasename", "creceiptareaname", "creceiptcorpname", "ct_code", "ctaxitemid", "ctinvclass", "cunitname", "fixedflag", "isconfigable", "isspecialty", "norgviapricetax", "scalefactor", "sfqs", "wholemanaflag", "cconsigncorpid", "cconsigncorp", "nreturntaxrate", "creccalbodyid", "creccalbody", "crecwareid", "crecwarehouse", "bdericttrans", "tconsigntime", "tdelivertime", "bsafeprice", "ntaldcnum", "nasttaldcnum", "ntaldcmny", "breturnprofit", "nretprofnum", "nastretprofnum", "nretprofmny", "cpricepolicyid", "cpricepolicy", "cpriceitemid", "cpriceitem", "cpriceitemtable", "cpriceitemtablename", "cpricecalproc", "cpricecalprocname", "cquoteunitid", "cquoteunit", "nquoteunitnum", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc", "cprolineid", "cprolinename", "natp", "crecaddrnode", "crecaddrnodename", "ntalplconsigmny", "ntaltransnum", "ntaltransmny", "ntaloutmny", "ntalsignmny", "ntalbalancemny", "ntaltransretnum", "ntranslossnum", "biftransfinish", "dlastconsigdate", "dlasttransdate", "dlastoutdate", "dlastinvoicedt", "dlastpaydate", "nqtscalefactor", "bqtfixedflag", "cinventoryid1", "cchantypeid", "cchantype", "pkarrivecorp", "stempbody", "bifinventoryfinish_init", "noutmny_diff", "ntotalbalancenumber", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", "nqtorgprc", "nqtorgtaxprc", "cinvspec", "cinvtype", "narrangescornum", "narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "barrangedflag", "carrangepersonid", "tlastarrangetime", "colour", "nbalmny_diff", "narrangemonum", "cfreecustid", "ccustomerid", "batpcheck", "exets", "ntotalshouldoutnum", "nouttoplimit", "noutcloselimit", "clargessrowno", "ct_name", "ntotalcostmny", "ntotlbalcostnum","tsbl" };
  public UFBoolean bdericttrans;
  public UFBoolean biftransfinish;
  public UFBoolean bqtfixedflag;
  public UFBoolean breturnprofit;
  public UFBoolean bsafeprice;
  public String cadvisecalbody;
  public String cbodywarehousename;
  public transient String cbomordercode;
  public transient String cconsigncorp;
  public String clargessrowno;
  public String cconsigncorpid;
  public transient String ccurrencytypename;
  public String cinventorycode;
  public String cinventoryname;
  public transient String cpackunitname;
  public String cpricecalproc;
  public transient String cpricecalprocname;
  public transient String cpriceitem;
  public String cpriceitemid;
  public String cpriceitemtable;
  public transient String cpriceitemtablename;
  public transient String cpricepolicy;
  public String cpricepolicyid;
  public transient String cprojectname;
  public transient String cprojectphasename;
  public String cprolineid;
  public transient String cprolinename;
  public transient String cquoteunit;
  public String cquoteunitid;
  public String crecaddrnode;
  public transient String crecaddrnodename;
  public String creccalbody;
  public String creccalbodyid;
  public transient String creceiptareaname;
  public transient String creceiptcorpname;
  public String crecwarehouse;
  public String crecwareid;
  public transient String ct_code;
  public transient String ct_name;
  public transient String ctaxitemid;
  public transient String ctinvclass;
  public transient String cunitname;
  public UFDate dlastconsigdate;
  public UFDate dlastinvoicedt;
  public UFDate dlastoutdate;
  public UFDate dlastpaydate;
  public UFDate dlasttransdate;
  public transient UFBoolean fixedflag;
  public transient String GGXX;
  public transient UFBoolean isconfigable;
  public transient UFBoolean isspecialty;
  public UFBoolean m_assistunit = new UFBoolean(false);
  public UFBoolean m_bFinished;
  public UFBoolean m_bifinventoryfinish;
  public UFBoolean m_bifinvoicefinish;
  public UFBoolean m_bifpaybalance;
  public UFBoolean m_bifpayfinish;
  public UFBoolean m_bifpaysign;
  public UFBoolean m_bifreceiptfinish;
  public UFBoolean m_boosflag = new UFBoolean(false);

  public UFBoolean m_bsupplyflag = new UFBoolean(false);
  public String m_cadvisecalbodyid;
  public String m_cbatchid;
  public String m_cbomorderid;
  public String m_cfreezeid;
  public String m_cinvbasdocid;
  public String m_cInvSort;
  public String m_cprojectid;
  public String m_cprojectid3;
  public String m_cprojectphaseid;
  public String m_creceiptareaid;
  public String m_creceiptcorpid;
  public String m_crowno;
  public Double nrowno;
  public String m_cStoreAdmin;
  public String m_ct_manageid;
  public String m_ctinvclassid;
  public UFBoolean m_discountflag = new UFBoolean(false);
  public Integer m_fbatchstatus;
  public UFDateTime m_headts;
  public UFBoolean m_isappendant;
  public UFBoolean m_laborflag = new UFBoolean(false);
  public UFDouble m_nassistcurnetprice;
  public UFDouble m_nassistcurprice;
  public UFDouble m_nassistcursummny;
  public UFDouble m_nassistcurtaxmny;
  public UFDouble m_nassistcurtaxnetprice;
  public UFDouble m_nassistcurtaxprice;
  public Integer m_nlinenumber;
  public UFDouble m_nOrgViaPrice;
  public UFDouble m_ntotalbalancenumber;
  public UFDouble m_ntotlbalcostnum;
  public UFDouble m_ntotalcarrynumber;
  public UFDouble m_ntotalcostmny;
  public UFDouble m_ntotalinventorynumber;
  public UFDouble m_ntotalinvoicemny;
  public UFDouble m_ntotalinvoicenumber;
  public UFDouble m_ntotalpaymny;
  public UFDouble m_ntotalplanreceiptnumber;
  public UFDouble m_ntotalreceiptnumber;
  public UFDouble m_ntotalreturnnumber;
  public UFDouble m_ntotalsignnumber;
  public UFDouble m_outstoreauditnumber;
  public String m_pk_corp;
  public UFDouble m_receiptauditnumber;
  public UFDateTime m_ts;
  public UFDateTime m_exets;
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
  public String m_vfreeid1 = null;

  public String m_vfreeid10 = null;

  public String m_vfreeid2 = null;

  public String m_vfreeid3 = null;

  public String m_vfreeid4 = null;

  public String m_vfreeid5 = null;

  public String m_vfreeid6 = null;

  public String m_vfreeid7 = null;

  public String m_vfreeid8 = null;

  public String m_vfreeid9 = null;

  public String m_vfreename1 = null;

  public String m_vfreename10 = null;

  public String m_vfreename2 = null;

  public String m_vfreename3 = null;

  public String m_vfreename4 = null;

  public String m_vfreename5 = null;

  public String m_vfreename6 = null;

  public String m_vfreename7 = null;

  public String m_vfreename8 = null;

  public String m_vfreename9 = null;
  public String m_vreceiveaddress;
  public UFDouble nastretprofnum;
  public UFDouble nasttaldcnum;
  public UFDouble natp;
  public UFDouble norgqtnetprc;
  public UFDouble norgqtprc;
  public UFDouble norgqttaxnetprc;
  public UFDouble norgqttaxprc;
  public transient UFDouble norgviapricetax;
  public UFDouble nqtnetprc;
  public UFDouble nqtprc;
  public UFDouble nqtscalefactor;
  public UFDouble nqttaxnetprc;
  public UFDouble nqttaxprc;
  public UFDouble nquoteunitnum;
  public UFDouble nretprofmny;
  public UFDouble nretprofnum;
  public UFDouble nreturntaxrate;
  public UFDouble ntalbalancemny;
  public UFDouble ntaldcmny;
  public UFDouble ntaldcnum;
  public UFDouble ntaloutmny;
  public UFDouble ntalplconsigmny;
  public UFDouble ntalsignmny;
  public UFDouble ntaltransmny;
  public UFDouble ntaltransnum;
  public UFDouble ntaltransretnum;
  public UFDouble ntranslossnum;
  public UFDouble scalefactor;
  public transient UFBoolean sfqs;
  public String tconsigntime;
  public String tdelivertime;
  public transient UFBoolean wholemanaflag;
  public UFDouble narrangepoapplynum;
  public UFDouble narrangescornum;
  public UFDouble narrangetoornum;
  public UFDouble norrangetoapplynum;
  public UFDouble narrangemonum;
  public UFBoolean barrangedflag;
  public String carrangepersonid;
  public UFDateTime tlastarrangetime;
  public String colour;
  public UFDouble ntotalshouldoutnum;
  public String vdef7;
  public String vdef8;
  public String vdef9;
  public String vdef10;
  public String vdef11;
  public String vdef12;
  public String vdef13;
  public String vdef14;
  public String vdef15;
  public String vdef16;
  public String vdef17;
  public String vdef18;
  public String vdef19;
  public String vdef20;
  public String pk_defdoc1;
  public String pk_defdoc2;
  public String pk_defdoc3;
  public String pk_defdoc4;
  public String pk_defdoc5;
  public String pk_defdoc6;
  public String pk_defdoc7;
  public String pk_defdoc8;
  public String pk_defdoc9;
  public String pk_defdoc10;
  public String pk_defdoc11;
  public String pk_defdoc12;
  public String pk_defdoc13;
  public String pk_defdoc14;
  public String pk_defdoc15;
  public String pk_defdoc16;
  public String pk_defdoc17;
  public String pk_defdoc18;
  public String pk_defdoc19;
  public String pk_defdoc20;
  public UFDouble nqtorgprc;
  public UFDouble nqtorgtaxprc;
  public transient String cinvspec;
  public transient String cinvtype;
  public boolean isDel7D = false;
  public UFDouble nbalmny_diff;
  public SaleorderBVO oldbvo = null;

  public boolean bCheckATP = true;
  public String cfreecustid;
  public String ccustomerid;
  public UFDouble m_nnumber;
  public UFDouble m_npacknumber;
  public String m_cbodywarehouseid;
  public UFDate m_dconsigndate;
  public UFDate m_ddeliverdate;
  public UFBoolean m_blargessflag = new UFBoolean(false);
  public String m_ceditsaleid;
  public UFBoolean m_beditflag;
  public String m_veditreason;
  public String m_ccurrencytypeid;
  public UFDouble m_nitemdiscountrate;
  public UFDouble m_ndiscountrate;
  public UFDouble m_nexchangeotobrate;
  public UFDouble m_nexchangeotoarate;
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
  public UFDouble m_nassistcurdiscountmny;
  public UFDouble m_invoiceauditnumber;
  public UFDouble m_nassistcurmny;
  public UFDouble ntotalpaymny_old;
  public UFDouble m_nouttoplimit;
  public UFDouble m_noutcloselimit;
  public static transient String[] pricemnykeys = { "ntaxrate", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "noriginalcurtaxprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurnetprice", "noriginalcurmny", "noriginalcurtaxmny", "noriginalcurdiscountmny", "noriginalcursummny", "ndiscountrate", "nitemdiscountrate", "nexchangeotobrate", "nexchangeotoarate", "norgviaprice", "norgviapricetax", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "nmny", "ntaxmny", "ndiscountmny", "nsummny", "nreturntaxrate", "ntaldcmny", "nretprofmny", "ntotalpaymny", "ntotalcostmny", "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny", "nassistcurtaxnetprice", "nassistcurnetprice", "nassistcurtaxprice", "nassistcurprice", "ntalplconsigmny", "ntaltransmny", "ntaloutmny", "ntalsignmny", "ntalbalancemny", "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc", "nqtorgprc", "nqtorgtaxprc" };
  public static transient HashSet hspricemnykey;
  public UFDouble ntotalreceiptnumber_old;
  public UFDouble ntotalinvoicenumber_old;
  public UFDouble ntotalinventorynumber_old;
  public UFBoolean bifinventoryfinish_init;
  public String cchantype;
  public String cchantypeid;
  public String cinventoryid1;
  public int iAction = -1;
  public UFDouble noutmny_diff;
  public String pkarrivecorp;
  public String stempbody;
  private ClientLink clientLink;

  public static HashSet getHspricemnykey()
  {
    if (hspricemnykey == null) {
      hspricemnykey = new HashSet();
      int i = 0; for (int loop = pricemnykeys.length; i < loop; i++) {
        hspricemnykey.add(pricemnykeys[i]);
      }
    }
    return hspricemnykey;
  }

  public static boolean isPriceOrMny(String key)
  {
    if (SoVoTools.isEmptyString(key))
      return false;
    return getHspricemnykey().contains(key);
  }

  public UFDouble getNtotalinventorynumber_old()
  {
    return this.ntotalinventorynumber_old;
  }

  public void setNtotalinventorynumber_old(UFDouble ntotalinventorynumber_old)
  {
    this.ntotalinventorynumber_old = ntotalinventorynumber_old;
  }

  public UFDouble getNtotalinvoicenumber_old()
  {
    return this.ntotalinvoicenumber_old;
  }

  public void setNtotalinvoicenumber_old(UFDouble ntotalinvoicenumber_old)
  {
    this.ntotalinvoicenumber_old = ntotalinvoicenumber_old;
  }

  public UFDouble getNtotalpaymny_old()
  {
    return this.ntotalpaymny_old;
  }

  public void setNtotalpaymny_old(UFDouble ntotalpaymny_old)
  {
    this.ntotalpaymny_old = ntotalpaymny_old;
  }

  public UFDouble getNtotalreceiptnumber_old()
  {
    return this.ntotalreceiptnumber_old;
  }

  public void setNtotalreceiptnumber_old(UFDouble ntotalreceiptnumber_old)
  {
    this.ntotalreceiptnumber_old = ntotalreceiptnumber_old;
  }

  public Double getNrowno()
  {
    if (this.nrowno == null) {
      if ((this.m_crowno == null) || (this.m_crowno.trim().length() <= 0))
        this.nrowno = new Double(0.0D);
      else {
        try {
          this.nrowno = new Double(this.m_crowno);
        } catch (Exception e) {
          this.nrowno = new Double(0.0D);
          SCMEnv.out(e.getMessage());
        }
      }
    }
    return this.nrowno;
  }

  public SaleorderBVO()
  {
  }

  public SaleorderBVO(String newCorder_bid)
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
    SaleorderBVO saleorderB = (SaleorderBVO)o;

    return saleorderB;
  }

  public String getEntityName()
  {
    return "SaleorderB";
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

  public String getCsaleid()
  {
    return this.m_csaleid;
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

  public String getCbodywarehouseid()
  {
    return this.m_cbodywarehouseid;
  }

  public UFDate getDconsigndate()
  {
    return this.m_dconsigndate;
  }

  public UFDate getDdeliverdate()
  {
    return this.m_ddeliverdate;
  }

  public UFBoolean getBlargessflag()
  {
    return this.m_blargessflag;
  }

  public String getCeditsaleid()
  {
    return this.m_ceditsaleid;
  }

  public UFBoolean getBeditflag()
  {
    return this.m_beditflag;
  }

  public String getVeditreason()
  {
    return this.m_veditreason;
  }

  public String getCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public UFDouble getNitemdiscountrate()
  {
    return this.m_nitemdiscountrate;
  }

  public UFDouble getNdiscountrate()
  {
    return this.m_ndiscountrate;
  }

  public UFDouble getNexchangeotobrate()
  {
    return this.m_nexchangeotobrate;
  }

  public UFDouble getNexchangeotoarate()
  {
    return this.m_nexchangeotoarate;
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
    if (this.m_noriginalcursummny == null)
      this.m_noriginalcursummny = SoVoConst.duf0;
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
    if (this.m_nsummny == null)
      this.m_nsummny = SoVoConst.duf0;
    return this.m_nsummny;
  }

  public UFDouble getNdiscountmny()
  {
    return this.m_ndiscountmny;
  }

  public String getCoperatorid()
  {
    return this.m_coperatorid;
  }

  public Integer getFrowstatus()
  {
    return this.m_frowstatus;
  }

  public String getFrownote()
  {
    return this.m_frownote;
  }

  public void setCorder_bid(String newCorder_bid)
  {
    this.m_corder_bid = newCorder_bid;
  }

  public void setCsaleid(String newCsaleid)
  {
    this.m_csaleid = newCsaleid;
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

  public void setCbodywarehouseid(String newCbodywarehouseid)
  {
    this.m_cbodywarehouseid = newCbodywarehouseid;
  }

  public void setDconsigndate(UFDate newDconsigndate)
  {
    this.m_dconsigndate = newDconsigndate;
  }

  public void setDdeliverdate(UFDate newDdeliverdate)
  {
    this.m_ddeliverdate = newDdeliverdate;
  }

  public void setBlargessflag(UFBoolean newBlargessflag)
  {
    this.m_blargessflag = newBlargessflag;
  }

  public void setCeditsaleid(String newCeditsaleid)
  {
    this.m_ceditsaleid = newCeditsaleid;
  }

  public void setBeditflag(UFBoolean newBeditflag)
  {
    this.m_beditflag = newBeditflag;
  }

  public void setVeditreason(String newVeditreason)
  {
    this.m_veditreason = newVeditreason;
  }

  public void setCcurrencytypeid(String newCcurrencytypeid)
  {
    this.m_ccurrencytypeid = newCcurrencytypeid;
  }

  public void setNitemdiscountrate(UFDouble newNitemdiscountrate)
  {
    this.m_nitemdiscountrate = newNitemdiscountrate;
  }

  public void setNdiscountrate(UFDouble newNdiscountrate)
  {
    this.m_ndiscountrate = newNdiscountrate;
  }

  public void setNexchangeotobrate(UFDouble newNexchangeotobrate)
  {
    this.m_nexchangeotobrate = newNexchangeotobrate;
  }

  public void setNexchangeotoarate(UFDouble newNexchangeotoarate)
  {
    this.m_nexchangeotoarate = newNexchangeotoarate;
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

  public void setCoperatorid(String newCoperatorid)
  {
    this.m_coperatorid = newCoperatorid;
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

    if ((this.m_cinventoryid == null) || (this.m_cinventoryid.trim().length() <= 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001439"));
    }
    if ((this.m_ccurrencytypeid == null) || (this.m_ccurrencytypeid.trim().length() <= 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001755"));
    }
    if ((this.m_discountflag != null) && (!this.m_discountflag.booleanValue())) {
      if (this.m_nnumber == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002282"));
      }
      if (this.nquoteunitnum == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002104"));
      }
    }

    if (((this.m_discountflag == null) || (!this.m_discountflag.booleanValue())) && ((this.m_blargessflag == null) || (!this.m_blargessflag.booleanValue())) && ((this.m_isappendant == null) || (!this.m_isappendant.booleanValue())))
    {
      if (this.m_ntaxrate == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003078"));
      }
      if (this.m_noriginalcurtaxmny == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003084"));
      }
      if (this.m_noriginalcursummny == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000227"));
      }
      if (this.m_nsummny == null) {
        errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002594"));
      }
      if (errFields.size() == 0) {
        if (this.m_ndiscountrate == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002290"));
        }
        if (this.m_noriginalcurnetprice == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002305"));
        }
        if (this.m_noriginalcurtaxnetprice == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001159"));
        }
        if (this.norgqttaxnetprc == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002102"));
        }
        if (this.norgqtnetprc == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002105"));
        }
        if (this.m_noriginalcurmny == null) {
          errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002307"));
        }
      }
    }
    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301", "UPP40060301-000083"));

    if (errFields.size() > 0) {
      Object[] temp = errFields.toArray();

      message.append("[");
      message.append(temp[0].toString());
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));

        message.append(temp[i].toString());
      }
      message.append("]");

      throw new NullFieldException(message.toString());
    }

    if ((this.m_ndiscountmny != null) && (this.m_ndiscountmny.toString().length() > 16)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301", "UPP40060301-000092"));
    }

    if ((this.m_npacknumber != null) && (
      (this.m_cpackunitid == null) || (this.m_cpackunitid.length() == 0)))
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301", "UPP40060301-000093"));
  }

  public String[] getAttributeNames()
  {
    return arrNames;
  }

  public Object getAttributeValue(String attributeName)
  {
    HashMap hs = getArrHs();
    if ((hs == null) || (attributeName == null))
      return null;
    Integer index = null;
    index = (Integer)hs.get(attributeName);
    if (index == null)
      return null;
    return getAttributeValue(index.intValue());
  }

  public void setAttributeValue(String name, Object value)
  { 
    HashMap hs = getArrHs();
    if ((hs == null) || (name == null))
      return;
    Integer index = null;
    index = (Integer)hs.get(name);
    if (index == null)
      return;
    setAttributeValue(index.intValue(), value);
  }

  public static SOField[] getAddFields()
  {
    SOField[] fields1 = getSoSaleOrderBAddFields();
    SOField[] fields2 = getSoSaleExecuteAddFields();
    int count = 0;
    if (fields1 != null);
    count += fields1.length;
    if (fields2 != null);
    count += fields2.length;
    if (count <= 0)
      return null;
    SOField[] refields = new SOField[count];
    if (fields1 != null) {
      System.arraycopy(fields1, 0, refields, 0, fields1.length-1);
      System.arraycopy(fields1, fields1.length-1, refields, refields.length-1, 1);
    }
    if (fields2 != null) {
      System.arraycopy(fields2, 0, refields, fields1 == null ? 0 : fields1.length-1, fields2.length);
    }
 
    return refields;
  }

  public HashMap getArrHs()
  {
    if (arrhs == null) {
      String[] names = getAttributeNames();
      if ((names == null) || (names.length <= 0))
        return arrhs;
      arrhs = new HashMap(300);
      int i = 0; for (int loop = names.length; i < loop; i++) {
        arrhs.put(names[i], new Integer(i));
      }
    }
    return arrhs;
  }

  public UFBoolean getAssistunit()
  {
    return this.m_assistunit;
  }

  public Object getAttributeValue(int index)
  {
    if (index < 0)
      return null;
    if (index < 10) {
      if (index == 0)
        return this.m_corder_bid;
      if (index == 1)
        return this.m_cStoreAdmin;
      if (index == 2)
        return this.m_cInvSort;
      if (index == 3)
        return this.m_crowno;
      if (index == 4)
        return this.m_ctinvclassid;
      if (index == 5)
        return this.m_cinvbasdocid;
      if (index == 6)
        return this.m_cbatchid;
      if (index == 7)
        return this.m_csaleid;
      if (index == 8)
        return this.m_pk_corp;
      if (index == 9) {
        return this.m_creceipttype;
      }
      return null;
    }
    if (index < 20) {
      if (index == 10)
        return this.m_csourcebillid;
      if (index == 11)
        return this.m_csourcebillbodyid;
      if (index == 12)
        return this.m_cinventoryid;
      if (index == 13)
        return this.m_cunitid;
      if (index == 14)
        return this.m_cpackunitid;
      if (index == 15)
        return this.m_nnumber;
      if (index == 16)
        return this.m_npacknumber;
      if (index == 17)
        return this.m_cbodywarehouseid;
      if (index == 18)
        return this.m_dconsigndate;
      if (index == 19) {
        return this.m_ddeliverdate;
      }
      return null;
    }
    if (index < 30) {
      if (index == 20)
        return this.m_blargessflag;
      if (index == 21)
        return this.m_ceditsaleid;
      if (index == 22)
        return this.m_beditflag;
      if (index == 23)
        return this.m_veditreason;
      if (index == 24)
        return this.m_ccurrencytypeid;
      if (index == 25)
        return this.m_nitemdiscountrate;
      if (index == 26)
        return this.m_ndiscountrate;
      if (index == 27)
        return this.m_nexchangeotobrate;
      if (index == 28)
        return this.m_nexchangeotoarate;
      if (index == 29) {
        return this.m_ntaxrate;
      }
      return null;
    }
    if (index < 40) {
      if (index == 30)
        return this.m_noriginalcurprice;
      if (index == 31)
        return this.m_noriginalcurtaxprice;
      if (index == 32)
        return this.m_noriginalcurnetprice;
      if (index == 33)
        return this.m_noriginalcurtaxnetprice;
      if (index == 34)
        return this.m_noriginalcurtaxmny;
      if (index == 35)
        return this.m_noriginalcurmny;
      if (index == 36)
        return getNoriginalcursummny();
      if (index == 37)
        return this.m_noriginalcurdiscountmny;
      if (index == 38)
        return this.m_nprice;
      if (index == 39) {
        return this.m_ntaxprice;
      }
      return null;
    }
    if (index < 50) {
      if (index == 40)
        return this.m_nnetprice;
      if (index == 41)
        return this.m_ntaxnetprice;
      if (index == 42)
        return this.m_ntaxmny;
      if (index == 43)
        return this.m_nmny;
      if (index == 44)
        return getNsummny();
      if (index == 45)
        return this.m_ndiscountmny;
      if (index == 46)
        return this.m_coperatorid;
      if (index == 47)
        return this.m_frowstatus;
      if (index == 48)
        return this.m_fbatchstatus;
      if (index == 49) {
        return this.m_frownote;
      }
      return null;
    }
    if (index < 60) {
      if (index == 50)
        return this.m_ntotalpaymny;
      if (index == 51)
        return this.m_ntotalreceiptnumber;
      if (index == 52)
        return this.m_ntotalinvoicenumber;
      if (index == 53)
        return this.m_ntotalinventorynumber;
      if (index == 54)
        return this.m_bifinvoicefinish;
      if (index == 55)
        return this.m_bifreceiptfinish;
      if (index == 56)
        return this.m_bifinventoryfinish;
      if (index == 57)
        return this.m_bifpayfinish;
      if (index == 58)
        return this.m_nassistcurdiscountmny;
      if (index == 59) {
        return getNassistcursummny();
      }
      return null;
    }
    if (index < 70) {
      if (index == 60)
        return this.m_nassistcurmny;
      if (index == 61)
        return this.m_nassistcurtaxmny;
      if (index == 62)
        return this.m_nassistcurtaxnetprice;
      if (index == 63)
        return this.m_nassistcurnetprice;
      if (index == 64)
        return this.m_nassistcurtaxprice;
      if (index == 65)
        return this.m_nassistcurprice;
      if (index == 66)
        return this.m_cprojectid3;
      if (index == 67)
        return this.m_cprojectphaseid;
      if (index == 68)
        return this.m_cprojectid;
      if (index == 69) {
        return this.m_vfree5;
      }
      return null;
    }
    if (index < 80) {
      if (index == 70)
        return this.m_vfree4;
      if (index == 71)
        return this.m_vfree3;
      if (index == 72)
        return this.m_vfree2;
      if (index == 73)
        return this.m_vfree1;
      if (index == 74)
        return this.m_vfree0;
      if (index == 75)
        return this.m_vdef6;
      if (index == 76)
        return this.m_vdef5;
      if (index == 77)
        return this.m_vdef4;
      if (index == 78)
        return this.m_vdef3;
      if (index == 79) {
        return this.m_vdef2;
      }
      return null;
    }
    if (index < 90) {
      if (index == 80)
        return this.m_vdef1;
      if (index == 81)
        return this.m_discountflag;
      if (index == 82)
        return this.m_assistunit;
      if (index == 83)
        return this.m_ct_manageid;
      if (index == 84)
        return this.m_cbomorderid;
      if (index == 85)
        return this.m_cfreezeid;
      if (index == 86)
        return this.m_ts;
      if (index == 87)
        return this.m_laborflag;
      if (index == 88)
        return this.m_nOrgViaPrice;
      if (index == 89) {
        return this.m_cadvisecalbodyid;
      }
      return null;
    }
    if (index < 100) {
      if (index == 90)
        return this.m_boosflag;
      if (index == 91)
        return this.m_bsupplyflag;
      if (index == 92)
        return this.m_creceiptareaid;
      if (index == 93)
        return this.m_vreceiveaddress;
      if (index == 94)
        return this.m_creceiptcorpid;
      if (index == 95)
        return this.m_ntotalplanreceiptnumber;
      if (index == 96)
        return this.m_isappendant;
      if (index == 97)
        return this.m_invoiceauditnumber;
      if (index == 98)
        return this.m_receiptauditnumber;
      if (index == 99) {
        return this.m_outstoreauditnumber;
      }
      return null;
    }
    if (index < 110) {
      if (index == 100)
        return this.m_ntotalreturnnumber;
      if (index == 101)
        return this.m_ntotalcarrynumber;
      if (index == 102)
        return this.m_ntotalsignnumber;
      if (index == 103)
        return this.m_nlinenumber;
      if (index == 104)
        return this.m_vfreeid1;
      if (index == 105)
        return this.m_vfreeid2;
      if (index == 106)
        return this.m_vfreeid3;
      if (index == 107)
        return this.m_vfreeid4;
      if (index == 108)
        return this.m_vfreeid5;
      if (index == 109) {
        return this.m_vfreeid6;
      }
      return null;
    }
    if (index < 120) {
      if (index == 110)
        return this.m_vfreeid7;
      if (index == 111)
        return this.m_vfreeid8;
      if (index == 112)
        return this.m_vfreeid9;
      if (index == 113)
        return this.m_vfreeid10;
      if (index == 114)
        return this.m_vfreename1;
      if (index == 115)
        return this.m_vfreename2;
      if (index == 116)
        return this.m_vfreename3;
      if (index == 117)
        return this.m_vfreename4;
      if (index == 118)
        return this.m_vfreename5;
      if (index == 119) {
        return this.m_vfreename6;
      }
      return null;
    }
    if (index < 130) {
      if (index == 120)
        return this.m_vfreename7;
      if (index == 121)
        return this.m_vfreename8;
      if (index == 122)
        return this.m_vfreename9;
      if (index == 123)
        return this.m_vfreename10;
      if (index == 124)
        return this.m_cStoreAdmin;
      if (index == 125)
        return this.m_cInvSort;
      if (index == 126)
        return this.m_bFinished;
      if (index == 127)
        return this.cadvisecalbody;
      if (index == 128)
        return this.cbodywarehousename;
      if (index == 129) {
        return this.cbomordercode;
      }
      return null;
    }
    if (index < 140) {
      if (index == 130)
        return this.ccurrencytypename;
      if (index == 131)
        return this.cinventorycode;
      if (index == 132)
        return this.cinventoryname;
      if (index == 133)
        return this.GGXX;
      if (index == 134)
        return this.cpackunitname;
      if (index == 135)
        return this.cprojectname;
      if (index == 136)
        return this.cprojectphasename;
      if (index == 137)
        return this.creceiptareaname;
      if (index == 138)
        return this.creceiptcorpname;
      if (index == 139) {
        return this.ct_code;
      }
      return null;
    }
    if (index < 150) {
      if (index == 140)
        return this.ctaxitemid;
      if (index == 141)
        return this.ctinvclass;
      if (index == 142)
        return this.cunitname;
      if (index == 143)
        return this.fixedflag;
      if (index == 144)
        return this.isconfigable;
      if (index == 145)
        return this.isspecialty;
      if (index == 146)
        return this.norgviapricetax;
      if (index == 147)
        return this.scalefactor;
      if (index == 148)
        return this.sfqs;
      if (index == 149) {
        return this.wholemanaflag;
      }
      return null;
    }
    if (index < 160) {
      if (index == 150)
        return this.cconsigncorpid;
      if (index == 151)
        return this.cconsigncorp;
      if (index == 152)
        return this.nreturntaxrate;
      if (index == 153)
        return this.creccalbodyid;
      if (index == 154)
        return this.creccalbody;
      if (index == 155)
        return this.crecwareid;
      if (index == 156)
        return this.crecwarehouse;
      if (index == 157)
        return this.bdericttrans;
      if (index == 158)
        return this.tconsigntime;
      if (index == 159) {
        return this.tdelivertime;
      }
      return null;
    }
    if (index < 170) {
      if (index == 160)
        return this.bsafeprice;
      if (index == 161)
        return this.ntaldcnum;
      if (index == 162)
        return this.nasttaldcnum;
      if (index == 163)
        return this.ntaldcmny;
      if (index == 164)
        return this.breturnprofit;
      if (index == 165)
        return this.nretprofnum;
      if (index == 166)
        return this.nastretprofnum;
      if (index == 167)
        return this.nretprofmny;
      if (index == 168)
        return this.cpricepolicyid;
      if (index == 169) {
        return this.cpricepolicy;
      }
      return null;
    }
    if (index < 180) {
      if (index == 170)
        return this.cpriceitemid;
      if (index == 171)
        return this.cpriceitem;
      if (index == 172)
        return this.cpriceitemtable;
      if (index == 173)
        return this.cpriceitemtablename;
      if (index == 174)
        return this.cpricecalproc;
      if (index == 175)
        return this.cpricecalprocname;
      if (index == 176)
        return this.cquoteunitid;
      if (index == 177)
        return this.cquoteunit;
      if (index == 178)
        return this.nquoteunitnum;
      if (index == 179) {
        return this.norgqttaxprc;
      }
      return null;
    }
    if (index < 190) {
      if (index == 180)
        return this.norgqtprc;
      if (index == 181)
        return this.norgqttaxnetprc;
      if (index == 182)
        return this.norgqtnetprc;
      if (index == 183)
        return this.nqttaxnetprc;
      if (index == 184)
        return this.nqtnetprc;
      if (index == 185)
        return this.nqttaxprc;
      if (index == 186)
        return this.nqtprc;
      if (index == 187)
        return this.cprolineid;
      if (index == 188)
        return this.cprolinename;
      if (index == 189) {
        return this.natp;
      }
      return null;
    }
    if (index < 200) {
      if (index == 190)
        return this.crecaddrnode;
      if (index == 191)
        return this.crecaddrnodename;
      if (index == 192)
        return this.ntalplconsigmny;
      if (index == 193)
        return this.ntaltransnum;
      if (index == 194)
        return this.ntaltransmny;
      if (index == 195)
        return this.ntaloutmny;
      if (index == 196)
        return this.ntalsignmny;
      if (index == 197)
        return this.ntalbalancemny;
      if (index == 198)
        return this.ntaltransretnum;
      if (index == 199) {
        return this.ntranslossnum;
      }
      return null;
    }
    if (index < 210) {
      if (index == 200)
        return this.biftransfinish;
      if (index == 201)
        return this.dlastconsigdate;
      if (index == 202)
        return this.dlasttransdate;
      if (index == 203)
        return this.dlastoutdate;
      if (index == 204)
        return this.dlastinvoicedt;
      if (index == 205)
        return this.dlastpaydate;
      if (index == 206)
        return getNqtscalefactor();
      if (index == 207)
        return this.bqtfixedflag;
      if (index == 208)
        return getCinventoryid1();
      if (index == 209) {
        return this.cchantypeid;
      }
      return null;
    }
    if (index < 220) {
      if (index == 210)
        return this.cchantype;
      if (index == 211)
        return this.pkarrivecorp;
      if (index == 212)
        return this.stempbody;
      if (index == 213)
        return this.bifinventoryfinish_init;
      if (index == 214)
        return this.noutmny_diff;
      if (index == 215)
        return this.m_ntotalbalancenumber;
      if (index == 216)
        return this.vdef7;
      if (index == 217)
        return this.vdef8;
      if (index == 218)
        return this.vdef9;
      if (index == 219) {
        return this.vdef10;
      }
      return null;
    }

    if (index < 230)
    {
      if (index == 220)
        return this.vdef11;
      if (index == 221)
        return this.vdef12;
      if (index == 222)
        return this.vdef13;
      if (index == 223)
        return this.vdef14;
      if (index == 224)
        return this.vdef15;
      if (index == 225)
        return this.vdef16;
      if (index == 226)
        return this.vdef17;
      if (index == 227)
        return this.vdef18;
      if (index == 228)
        return this.vdef19;
      if (index == 229) {
        return this.vdef20;
      }
      return null;
    }

    if (index < 240)
    {
      if (index == 230)
        return this.pk_defdoc1;
      if (index == 231)
        return this.pk_defdoc2;
      if (index == 232)
        return this.pk_defdoc3;
      if (index == 233)
        return this.pk_defdoc4;
      if (index == 234)
        return this.pk_defdoc5;
      if (index == 235)
        return this.pk_defdoc6;
      if (index == 236)
        return this.pk_defdoc7;
      if (index == 237)
        return this.pk_defdoc8;
      if (index == 238)
        return this.pk_defdoc9;
      if (index == 239) {
        return this.pk_defdoc10;
      }
      return null;
    }
    if (index < 250)
    {
      if (index == 240)
        return this.pk_defdoc11;
      if (index == 241)
        return this.pk_defdoc12;
      if (index == 242)
        return this.pk_defdoc13;
      if (index == 243)
        return this.pk_defdoc14;
      if (index == 244)
        return this.pk_defdoc15;
      if (index == 245)
        return this.pk_defdoc16;
      if (index == 246)
        return this.pk_defdoc17;
      if (index == 247)
        return this.pk_defdoc18;
      if (index == 248)
        return this.pk_defdoc19;
      if (index == 249)
        return this.pk_defdoc20;
    }
    else if (index < 260) {
      if (index == 250)
        return this.nqtorgprc;
      if (index == 251)
        return this.nqtorgtaxprc;
      if (index == 252)
        return this.cinvspec;
      if (index == 253)
        return this.cinvtype;
      if (index == 254)
        return this.narrangescornum;
      if (index == 255)
        return this.narrangepoapplynum;
      if (index == 256)
        return this.narrangetoornum;
      if (index == 257)
        return this.norrangetoapplynum;
      if (index == 258)
        return this.barrangedflag;
      if (index == 259)
        return this.carrangepersonid;
    } else {
      if (index < 270) {
        if (index == 260)
          return this.tlastarrangetime;
        if (index == 261)
          return this.colour;
        if (index == 262)
          return this.nbalmny_diff;
        if (index == 263)
          return this.narrangemonum;
        if (index == 264)
          return this.cfreecustid;
        if (index == 265)
          return this.ccustomerid;
        if (index == 266)
          return getBatpcheck();
        if (index == 267)
          return this.m_exets;
        if (index == 268)
          return this.ntotalshouldoutnum;
        if (index == 269) {
          return this.m_nouttoplimit;
        }
        return null;
      }

      if (index < 280) {
        if (index == 270) {
          return this.m_noutcloselimit;
        }
        if (index == 271) {
          return this.clargessrowno;
        }
        if (index == 272) {
          return this.ct_name;
        }
        if (index == 273)
          return this.m_ntotalcostmny;
        if (index == 274) {
          return this.m_ntotlbalcostnum;
        }  if (index == 275) {
          return this.tsbl;
        }
      }
    }

    return null;
  }

  public UFBoolean getBdericttrans()
  {
    return this.bdericttrans;
  }

  public UFBoolean getBifinventoryfinish()
  {
    return this.m_bifinventoryfinish;
  }

  public UFBoolean getBifinvoicefinish()
  {
    return this.m_bifinvoicefinish;
  }

  public UFBoolean getBifpaybalance()
  {
    return this.m_bifpaybalance;
  }

  public UFBoolean getBifpayfinish()
  {
    return this.m_bifpayfinish;
  }

  public UFBoolean getBifreceiptfinish()
  {
    return this.m_bifreceiptfinish;
  }

  public UFBoolean getBoosflag()
  {
    return this.m_boosflag;
  }

  public UFBoolean getBqtfixedflag()
  {
    return this.bqtfixedflag;
  }

  public UFBoolean getBreturnprofit()
  {
    return this.breturnprofit;
  }

  public UFBoolean getBsafeprice()
  {
    return this.bsafeprice;
  }

  public UFBoolean getBsupplyflag()
  {
    return this.m_bsupplyflag;
  }

  public String getCadvisecalbodyid()
  {
    return this.m_cadvisecalbodyid;
  }

  public String getCbatchid()
  {
    return this.m_cbatchid;
  }

  public String getCbomorderid()
  {
    return this.m_cbomorderid;
  }

  public String getCconsigncorp()
  {
    return this.cconsigncorp;
  }

  public String getCconsigncorpid()
  {
    return this.cconsigncorpid;
  }

  public String getCfreezeid()
  {
    return this.m_cfreezeid;
  }

  public String getCinvbasdocid()
  {
    return this.m_cinvbasdocid;
  }

  public String getCpricecalproc()
  {
    return this.cpricecalproc;
  }

  public String getCpricecalprocname()
  {
    return this.cpricecalprocname;
  }

  public String getCpriceitem()
  {
    return this.cpriceitem;
  }

  public String getCpriceitemid()
  {
    return this.cpriceitemid;
  }

  public String getCpriceitemtable()
  {
    return this.cpriceitemtable;
  }

  public String getCpriceitemtablename()
  {
    return this.cpriceitemtablename;
  }

  public String getCpricepolicy()
  {
    return this.cpricepolicy;
  }

  public String getCpricepolicyid()
  {
    return this.cpricepolicyid;
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

  public String getCprolineid()
  {
    return this.cprolineid;
  }

  public String getCprolinename()
  {
    return this.cprolinename;
  }

  public String getCquoteunit()
  {
    return this.cquoteunit;
  }

  public String getCquoteunitid()
  {
    return this.cquoteunitid;
  }

  public String getCrecaddrnode()
  {
    return this.crecaddrnode;
  }

  public String getCrecaddrnodename()
  {
    return this.crecaddrnodename;
  }

  public String getCreccalbody()
  {
    return this.creccalbody;
  }

  public String getCreccalbodyid()
  {
    return this.creccalbodyid;
  }

  public String getCreceiptareaid()
  {
    return this.m_creceiptareaid;
  }

  public String getCreceiptcorpid()
  {
    return this.m_creceiptcorpid;
  }

  public String getCrecwarehouse()
  {
    return this.crecwarehouse;
  }

  public String getCrecwareid()
  {
    return this.crecwareid;
  }

  public String getCrowno()
  {
    return this.m_crowno;
  }

  public String getCt_manageid()
  {
    return this.m_ct_manageid;
  }

  public String getCtinvclassid()
  {
    return this.m_ctinvclassid;
  }

  public UFDate getDlastconsigdate()
  {
    return this.dlastconsigdate;
  }

  public UFDate getDlastinvoicedt()
  {
    return this.dlastinvoicedt;
  }

  public UFDate getDlastoutdate()
  {
    return this.dlastoutdate;
  }

  public UFDate getDlastpaydate()
  {
    return this.dlastpaydate;
  }

  public UFDate getDlasttransdate()
  {
    return this.dlasttransdate;
  }

  public Integer getFbatchstatus()
  {
    return this.m_fbatchstatus;
  }

  public UFBoolean getFinished()
  {
    return this.m_bFinished;
  }

  public UFDouble getInvoiceauditNumber()
  {
    return this.m_invoiceauditnumber;
  }

  public String getInvSort()
  {
    return this.m_cInvSort;
  }

  public UFBoolean getIsappendant()
  {
    return this.m_isappendant;
  }

  public UFBoolean getLaborflag()
  {
    return this.m_laborflag;
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
    if (this.m_nassistcursummny == null)
      this.m_nassistcursummny = SoVoConst.duf0;
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

  public UFDouble getNastretprofnum()
  {
    return this.nastretprofnum;
  }

  public UFDouble getNasttaldcnum()
  {
    return this.nasttaldcnum;
  }

  public UFDouble getNatp()
  {
    return this.natp;
  }

  public Integer getNlinenumber()
  {
    return this.m_nlinenumber;
  }

  public UFDouble getNorgqtnetprc()
  {
    return this.norgqtnetprc;
  }

  public UFDouble getNorgqtprc()
  {
    return this.norgqtprc;
  }

  public UFDouble getNorgqttaxnetprc()
  {
    return this.norgqttaxnetprc;
  }

  public UFDouble getNorgqttaxprc()
  {
    return this.norgqttaxprc;
  }

  public UFDouble getNqtnetprc()
  {
    return this.nqtnetprc;
  }

  public UFDouble getNqtprc()
  {
    return this.nqtprc;
  }

  public UFDouble getNqtscalefactor()
  {
    if (this.nqtscalefactor == null)
      return SoVoTools.div(getNnumber(), getNquoteunitnum());
    return this.nqtscalefactor;
  }

  public UFDouble getNqttaxnetprc()
  {
    return this.nqttaxnetprc;
  }

  public UFDouble getNqttaxprc()
  {
    return this.nqttaxprc;
  }

  public UFDouble getNquoteunitnum()
  {
    return this.nquoteunitnum;
  }

  public UFDouble getNretprofmny()
  {
    return this.nretprofmny;
  }

  public UFDouble getNretprofnum()
  {
    return this.nretprofnum;
  }

  public UFDouble getNtalbalancemny()
  {
    return this.ntalbalancemny;
  }

  public UFDouble getNtaldcmny()
  {
    return this.ntaldcmny;
  }

  public UFDouble getNtaldcnum()
  {
    return this.ntaldcnum;
  }

  public UFDouble getNtaloutmny()
  {
    return this.ntaloutmny;
  }

  public UFDouble getNtalplconsigmny()
  {
    return this.ntalplconsigmny;
  }

  public UFDouble getNtalsignmny()
  {
    return this.ntalsignmny;
  }

  public UFDouble getNtaltransmny()
  {
    return this.ntaltransmny;
  }

  public UFDouble getNtaltransnum()
  {
    return this.ntaltransnum;
  }

  public UFDouble getNtaltransretnum()
  {
    return this.ntaltransretnum;
  }
  public UFDouble getNtotalshouldoutnum() {
    return this.ntotalshouldoutnum;
  }

  public UFDouble getNtotalbalancenumber()
  {
    return this.m_ntotalbalancenumber;
  }

  public UFDouble getNtotlbalcostnum() {
    return this.m_ntotlbalcostnum;
  }

  public UFDouble getNtotalcarrynumber()
  {
    return this.m_ntotalcarrynumber;
  }

  public UFDouble getNtotalcostmny()
  {
    return this.m_ntotalcostmny;
  }

  public UFDouble getNtotalinventorynumber()
  {
    return this.m_ntotalinventorynumber;
  }

  public UFDouble getNtotalinvoicemny()
  {
    return this.m_ntotalinvoicemny;
  }

  public UFDouble getNtotalinvoicenumber()
  {
    return this.m_ntotalinvoicenumber;
  }

  public UFDouble getNtotalpaymny()
  {
    return this.m_ntotalpaymny;
  }

  public UFDouble getNtotalplanreceiptnumber()
  {
    return this.m_ntotalplanreceiptnumber;
  }

  public UFDouble getNtotalreceiptnumber()
  {
    return this.m_ntotalreceiptnumber;
  }

  public UFDouble getNtotalreturnnumber()
  {
    return this.m_ntotalreturnnumber;
  }

  public UFDouble getNtotalsignnumber()
  {
    return this.m_ntotalsignnumber;
  }

  public UFDouble getNtranslossnum()
  {
    return this.ntranslossnum;
  }

  public UFDouble getOrgViaPrice()
  {
    return this.m_nOrgViaPrice;
  }

  public UFDouble getOutstoreauditNumber()
  {
    return this.m_outstoreauditnumber;
  }

  public String getPkcorp()
  {
    return this.m_pk_corp;
  }

  public UFDouble getReceiptauditNumber()
  {
    return this.m_receiptauditnumber;
  }

  public UFDouble getScalefactor()
  {
    return this.scalefactor;
  }

  public static SOField[] getSoSaleExecuteAddFields()
  {
    if (addSaleExecuteFields == null) {
      SOField[] addSaleExecuteFields_t = new SOField[56];

      SOField sofield = new SOField();
      sofield.setVoname("ntalplconsigmny");
      sofield.setDatabasename("ntalplconsigmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[0] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaltransnum");
      sofield.setDatabasename("ntaltransnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[1] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaltransmny");
      sofield.setDatabasename("ntaltransmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[2] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaloutmny");
      sofield.setDatabasename("ntaloutmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[3] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntalsignmny");
      sofield.setDatabasename("ntalsignmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[4] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntalbalancemny");
      sofield.setDatabasename("ntalbalancemny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[5] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaltransretnum");
      sofield.setDatabasename("ntaltransretnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[6] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntranslossnum");
      sofield.setDatabasename("ntranslossnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[7] = sofield;

      sofield = new SOField();
      sofield.setVoname("biftransfinish");
      sofield.setDatabasename("biftransfinish");
      sofield.setUftype(5);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[8] = sofield;

      sofield = new SOField();
      sofield.setVoname("dlastconsigdate");
      sofield.setDatabasename("dlastconsigdate");
      sofield.setUftype(2);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[9] = sofield;

      sofield = new SOField();
      sofield.setVoname("dlasttransdate");
      sofield.setDatabasename("dlasttransdate");
      sofield.setUftype(2);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[10] = sofield;

      sofield = new SOField();
      sofield.setVoname("dlastoutdate");
      sofield.setDatabasename("dlastoutdate");
      sofield.setUftype(2);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[11] = sofield;

      sofield = new SOField();
      sofield.setVoname("dlastinvoicedt");
      sofield.setDatabasename("dlastinvoicedt");
      sofield.setUftype(2);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[12] = sofield;

      sofield = new SOField();
      sofield.setVoname("dlastpaydate");
      sofield.setDatabasename("dlastpaydate");
      sofield.setUftype(2);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[13] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef7");
      sofield.setDatabasename("vdef7");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[14] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef8");
      sofield.setDatabasename("vdef8");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[15] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef9");
      sofield.setDatabasename("vdef9");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[16] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef10");
      sofield.setDatabasename("vdef10");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[17] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef11");
      sofield.setDatabasename("vdef11");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[18] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef12");
      sofield.setDatabasename("vdef12");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[19] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef13");
      sofield.setDatabasename("vdef13");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[20] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef14");
      sofield.setDatabasename("vdef14");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[21] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef15");
      sofield.setDatabasename("vdef15");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[22] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef16");
      sofield.setDatabasename("vdef16");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[23] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef17");
      sofield.setDatabasename("vdef17");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[24] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef18");
      sofield.setDatabasename("vdef18");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[25] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef19");
      sofield.setDatabasename("vdef19");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[26] = sofield;

      sofield = new SOField();
      sofield.setVoname("vdef20");
      sofield.setDatabasename("vdef20");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[27] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc1");
      sofield.setDatabasename("pk_defdoc1");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[28] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc2");
      sofield.setDatabasename("pk_defdoc2");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[29] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc3");
      sofield.setDatabasename("pk_defdoc3");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[30] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc4");
      sofield.setDatabasename("pk_defdoc4");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[31] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc5");
      sofield.setDatabasename("pk_defdoc5");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[32] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc6");
      sofield.setDatabasename("pk_defdoc6");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[33] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc7");
      sofield.setDatabasename("pk_defdoc7");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[34] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc8");
      sofield.setDatabasename("pk_defdoc8");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[35] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc9");
      sofield.setDatabasename("pk_defdoc9");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[36] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc10");
      sofield.setDatabasename("pk_defdoc10");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[37] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc11");
      sofield.setDatabasename("pk_defdoc11");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[38] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc12");
      sofield.setDatabasename("pk_defdoc12");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[39] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc13");
      sofield.setDatabasename("pk_defdoc13");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[40] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc14");
      sofield.setDatabasename("pk_defdoc14");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[41] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc15");
      sofield.setDatabasename("pk_defdoc15");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[42] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc16");
      sofield.setDatabasename("pk_defdoc16");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[43] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc17");
      sofield.setDatabasename("pk_defdoc17");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[44] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc18");
      sofield.setDatabasename("pk_defdoc18");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[45] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc19");
      sofield.setDatabasename("pk_defdoc19");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[46] = sofield;

      sofield = new SOField();
      sofield.setVoname("pk_defdoc20");
      sofield.setDatabasename("pk_defdoc20");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[47] = sofield;

      sofield = new SOField();
      sofield.setVoname("narrangescornum");
      sofield.setDatabasename("narrangescornum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[48] = sofield;

      sofield = new SOField();
      sofield.setVoname("narrangepoapplynum");
      sofield.setDatabasename("narrangepoapplynum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[49] = sofield;

      sofield = new SOField();
      sofield.setVoname("narrangetoornum");
      sofield.setDatabasename("narrangetoornum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[50] = sofield;

      sofield = new SOField();
      sofield.setVoname("norrangetoapplynum");
      sofield.setDatabasename("norrangetoapplynum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[51] = sofield;

      sofield = new SOField();
      sofield.setVoname("barrangedflag");
      sofield.setDatabasename("barrangedflag");
      sofield.setUftype(5);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[52] = sofield;

      sofield = new SOField();
      sofield.setVoname("carrangepersonid");
      sofield.setDatabasename("carrangepersonid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[53] = sofield;

      sofield = new SOField();
      sofield.setVoname("tlastarrangetime");
      sofield.setDatabasename("tlastarrangetime");
      sofield.setUftype(3);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[54] = sofield;

      sofield = new SOField();
      sofield.setVoname("narrangemonum");
      sofield.setDatabasename("narrangemonum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleexecute");
      addSaleExecuteFields_t[55] = sofield;

      addSaleExecuteFields = addSaleExecuteFields_t;
    }
    return addSaleExecuteFields;
  }

  public static SOField[] getSoSaleOrderBAddFields()
  {
    if (addSaleOrderBFields == null) {
      SOField[] addSaleOrderBFields_t = new SOField[40];

      SOField sofield = new SOField();
      sofield.setVoname("cconsigncorpid");
      sofield.setDatabasename("cconsigncorpid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[0] = sofield;

      sofield = new SOField();
      sofield.setVoname("nreturntaxrate");
      sofield.setDatabasename("nreturntaxrate");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[1] = sofield;

      sofield = new SOField();
      sofield.setVoname("creccalbodyid");
      sofield.setDatabasename("creccalbodyid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[2] = sofield;

      sofield = new SOField();
      sofield.setVoname("crecwareid");
      sofield.setDatabasename("crecwareid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[3] = sofield;

      sofield = new SOField();
      sofield.setVoname("bdericttrans");
      sofield.setDatabasename("bdericttrans");
      sofield.setUftype(5);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[4] = sofield;

      sofield = new SOField();
      sofield.setVoname("tconsigntime");
      sofield.setDatabasename("tconsigntime");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[5] = sofield;

      sofield = new SOField();
      sofield.setVoname("tdelivertime");
      sofield.setDatabasename("tdelivertime");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[6] = sofield;

      sofield = new SOField();
      sofield.setVoname("bsafeprice");
      sofield.setDatabasename("bsafeprice");
      sofield.setUftype(5);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[7] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaldcnum");
      sofield.setDatabasename("ntaldcnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[8] = sofield;

      sofield = new SOField();
      sofield.setVoname("nasttaldcnum");
      sofield.setDatabasename("nasttaldcnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[9] = sofield;

      sofield = new SOField();
      sofield.setVoname("ntaldcmny");
      sofield.setDatabasename("ntaldcmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[10] = sofield;

      sofield = new SOField();
      sofield.setVoname("breturnprofit");
      sofield.setDatabasename("breturnprofit");
      sofield.setUftype(5);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[11] = sofield;

      sofield = new SOField();
      sofield.setVoname("nretprofnum");
      sofield.setDatabasename("nretprofnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[12] = sofield;

      sofield = new SOField();
      sofield.setVoname("nastretprofnum");
      sofield.setDatabasename("nastretprofnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[13] = sofield;

      sofield = new SOField();
      sofield.setVoname("nretprofmny");
      sofield.setDatabasename("nretprofmny");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[14] = sofield;

      sofield = new SOField();
      sofield.setVoname("cpricepolicyid");
      sofield.setDatabasename("cpricepolicyid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[15] = sofield;

      sofield = new SOField();
      sofield.setVoname("cpriceitemid");
      sofield.setDatabasename("cpriceitemid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[16] = sofield;

      sofield = new SOField();
      sofield.setVoname("cpriceitemtable");
      sofield.setDatabasename("cpriceitemtable");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[17] = sofield;

      sofield = new SOField();
      sofield.setVoname("cpricecalproc");
      sofield.setDatabasename("cpricecalproc");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[18] = sofield;

      sofield = new SOField();
      sofield.setVoname("cquoteunitid");
      sofield.setDatabasename("cquoteunitid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[19] = sofield;

      sofield = new SOField();
      sofield.setVoname("nquoteunitnum");
      sofield.setDatabasename("nquoteunitnum");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[20] = sofield;

      sofield = new SOField();
      sofield.setVoname("norgqttaxprc");
      sofield.setDatabasename("norgqttaxprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[21] = sofield;

      sofield = new SOField();
      sofield.setVoname("norgqtprc");
      sofield.setDatabasename("norgqtprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[22] = sofield;

      sofield = new SOField();
      sofield.setVoname("norgqttaxnetprc");
      sofield.setDatabasename("norgqttaxnetprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[23] = sofield;

      sofield = new SOField();
      sofield.setVoname("norgqtnetprc");
      sofield.setDatabasename("norgqtnetprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[24] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqttaxnetprc");
      sofield.setDatabasename("nqttaxnetprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[25] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqtnetprc");
      sofield.setDatabasename("nqtnetprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[26] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqttaxprc");
      sofield.setDatabasename("nqttaxprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[27] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqtprc");
      sofield.setDatabasename("nqtprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[28] = sofield;

      sofield = new SOField();
      sofield.setVoname("cprolineid");
      sofield.setDatabasename("cprolineid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[29] = sofield;

      sofield = new SOField();
      sofield.setVoname("crecaddrnode");
      sofield.setDatabasename("crecaddrnode");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[30] = sofield;

      sofield = new SOField();
      sofield.setVoname("cinventoryid1");
      sofield.setDatabasename("cinventoryid1");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[31] = sofield;

      sofield = new SOField();
      sofield.setVoname("cchantypeid");
      sofield.setDatabasename("cchantypeid");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[32] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqtorgprc");
      sofield.setDatabasename("nqtorgprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[33] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqtorgtaxprc");
      sofield.setDatabasename("nqtorgtaxprc");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[34] = sofield;

      sofield = new SOField();
      sofield.setVoname("nqtscalefactor");
      sofield.setDatabasename("nquoteunitrate");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[35] = sofield;

      sofield = new SOField();
      sofield.setVoname("nouttoplimit");
      sofield.setDatabasename("nouttoplimit");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[36] = sofield;

      sofield = new SOField();
      sofield.setVoname("noutcloselimit");
      sofield.setDatabasename("noutcloselimit");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[37] = sofield;

      sofield = new SOField();
      sofield.setVoname("clargessrowno");
      sofield.setDatabasename("clargessrowno");
      sofield.setUftype(1);
      sofield.setDatabasetype(1);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[38] = sofield;
      
      sofield = new SOField();
      sofield.setVoname("tsbl");
      sofield.setDatabasename("tsbl");
      sofield.setUftype(4);
      sofield.setDatabasetype(3);
      sofield.setTablename("so_saleorder_b");
      addSaleOrderBFields_t[39] = sofield;

      addSaleOrderBFields = addSaleOrderBFields_t;
    }

    return addSaleOrderBFields;
  }

  public String getStoreAdmin()
  {
    return this.m_cStoreAdmin;
  }

  public String getTconsigntime()
  {
    return this.tconsigntime;
  }

  public String getTdelivertime()
  {
    return this.tdelivertime;
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

  public String getVreceiveaddress()
  {
    return this.m_vreceiveaddress;
  }

  public void setAssistunit(UFBoolean newassistunit)
  {
    this.m_assistunit = newassistunit;
  }

  public void setAttributeValue(int index, Object value)
  {
    if (index < 0)
      return;
    try {
      if (index < 10) {
        if (index == 0)
          this.m_corder_bid = ((String)value);
        else if (index == 1)
          this.m_cStoreAdmin = ((String)value);
        else if (index == 2)
          this.m_cInvSort = ((String)value);
        else if (index == 3)
          this.m_crowno = ((String)value);
        else if (index == 4)
          this.m_ctinvclassid = ((String)value);
        else if (index == 5)
          this.m_cinvbasdocid = ((String)value);
        else if (index == 6)
          this.m_cbatchid = ((String)value);
        else if (index == 7)
          this.m_csaleid = ((String)value);
        else if (index == 8)
          this.m_pk_corp = ((String)value);
        else if (index == 9) {
          this.m_creceipttype = ((String)value);
        }
        return;
      }if (index < 20) {
        if (index == 10)
          this.m_csourcebillid = ((String)value);
        else if (index == 11)
          this.m_csourcebillbodyid = ((String)value);
        else if (index == 12)
          this.m_cinventoryid = ((String)value);
        else if (index == 13)
          this.m_cunitid = ((String)value);
        else if (index == 14)
          this.m_cpackunitid = ((String)value);
        else if (index == 15)
          this.m_nnumber = ((UFDouble)value);
        else if (index == 16)
          this.m_npacknumber = ((UFDouble)value);
        else if (index == 17)
          this.m_cbodywarehouseid = ((String)value);
        else if (index == 18)
          this.m_dconsigndate = ((UFDate)value);
        else if (index == 19) {
          this.m_ddeliverdate = ((UFDate)value);
        }
        return;
      }if (index < 30) {
        if (index == 20)
          this.m_blargessflag = ((UFBoolean)value);
        else if (index == 21)
          this.m_ceditsaleid = ((String)value);
        else if (index == 22)
          this.m_beditflag = ((UFBoolean)value);
        else if (index == 23)
          this.m_veditreason = ((String)value);
        else if (index == 24)
          this.m_ccurrencytypeid = ((String)value);
        else if (index == 25)
          this.m_nitemdiscountrate = ((UFDouble)value);
        else if (index == 26)
          this.m_ndiscountrate = ((UFDouble)value);
        else if (index == 27)
          this.m_nexchangeotobrate = ((UFDouble)value);
        else if (index == 28)
          this.m_nexchangeotoarate = ((UFDouble)value);
        else if (index == 29) {
          this.m_ntaxrate = ((UFDouble)value);
        }
        return;
      }if (index < 40) {
        if (index == 30)
          this.m_noriginalcurprice = ((UFDouble)value);
        else if (index == 31)
          this.m_noriginalcurtaxprice = ((UFDouble)value);
        else if (index == 32)
          this.m_noriginalcurnetprice = ((UFDouble)value);
        else if (index == 33)
          this.m_noriginalcurtaxnetprice = ((UFDouble)value);
        else if (index == 34)
          this.m_noriginalcurtaxmny = ((UFDouble)value);
        else if (index == 35)
          this.m_noriginalcurmny = ((UFDouble)value);
        else if (index == 36)
          this.m_noriginalcursummny = ((UFDouble)value);
        else if (index == 37)
          this.m_noriginalcurdiscountmny = ((UFDouble)value);
        else if (index == 38)
          this.m_nprice = ((UFDouble)value);
        else if (index == 39) {
          this.m_ntaxprice = ((UFDouble)value);
        }
        return;
      }if (index < 50) {
        if (index == 40)
          this.m_nnetprice = ((UFDouble)value);
        else if (index == 41)
          this.m_ntaxnetprice = ((UFDouble)value);
        else if (index == 42)
          this.m_ntaxmny = ((UFDouble)value);
        else if (index == 43)
          this.m_nmny = ((UFDouble)value);
        else if (index == 44)
          this.m_nsummny = ((UFDouble)value);
        else if (index == 45)
          this.m_ndiscountmny = ((UFDouble)value);
        else if (index == 46)
          this.m_coperatorid = ((String)value);
        else if (index == 47)
          this.m_frowstatus = ((Integer)value);
        else if (index == 48)
          this.m_fbatchstatus = ((Integer)value);
        else if (index == 49) {
          this.m_frownote = ((String)value);
        }
        return;
      }if (index < 60) {
        if (index == 50)
          this.m_ntotalpaymny = ((UFDouble)value);
        else if (index == 51)
          this.m_ntotalreceiptnumber = ((UFDouble)value);
        else if (index == 52)
          this.m_ntotalinvoicenumber = ((UFDouble)value);
        else if (index == 53)
          this.m_ntotalinventorynumber = ((UFDouble)value);
        else if (index == 54)
          this.m_bifinvoicefinish = ((UFBoolean)value);
        else if (index == 55)
          this.m_bifreceiptfinish = ((UFBoolean)value);
        else if (index == 56)
          this.m_bifinventoryfinish = ((UFBoolean)value);
        else if (index == 57)
          this.m_bifpayfinish = ((UFBoolean)value);
        else if (index == 58)
          this.m_nassistcurdiscountmny = ((UFDouble)value);
        else if (index == 59) {
          this.m_nassistcursummny = ((UFDouble)value);
        }
        return;
      }if (index < 70) {
        if (index == 60)
          this.m_nassistcurmny = ((UFDouble)value);
        else if (index == 61)
          this.m_nassistcurtaxmny = ((UFDouble)value);
        else if (index == 62)
          this.m_nassistcurtaxnetprice = ((UFDouble)value);
        else if (index == 63)
          this.m_nassistcurnetprice = ((UFDouble)value);
        else if (index == 64)
          this.m_nassistcurtaxprice = ((UFDouble)value);
        else if (index == 65)
          this.m_nassistcurprice = ((UFDouble)value);
        else if (index == 66)
          this.m_cprojectid3 = ((String)value);
        else if (index == 67)
          this.m_cprojectphaseid = ((String)value);
        else if (index == 68)
          this.m_cprojectid = ((String)value);
        else if (index == 69) {
          this.m_vfree5 = ((String)value);
        }
        return;
      }if (index < 80) {
        if (index == 70)
          this.m_vfree4 = ((String)value);
        else if (index == 71)
          this.m_vfree3 = ((String)value);
        else if (index == 72)
          this.m_vfree2 = ((String)value);
        else if (index == 73)
          this.m_vfree1 = ((String)value);
        else if (index == 74)
          this.m_vfree0 = ((String)value);
        else if (index == 75)
          this.m_vdef6 = ((String)value);
        else if (index == 76)
          this.m_vdef5 = ((String)value);
        else if (index == 77)
          this.m_vdef4 = ((String)value);
        else if (index == 78)
          this.m_vdef3 = ((String)value);
        else if (index == 79) {
          this.m_vdef2 = ((String)value);
        }
        return;
      }if (index < 90) {
        if (index == 80)
          this.m_vdef1 = ((String)value);
        else if (index == 81)
          this.m_discountflag = ((UFBoolean)value);
        else if (index == 82)
          this.m_assistunit = ((UFBoolean)value);
        else if (index == 83)
          this.m_ct_manageid = ((String)value);
        else if (index == 84)
          this.m_cbomorderid = ((String)value);
        else if (index == 85)
          this.m_cfreezeid = ((String)value);
        else if (index == 86) {
          this.m_ts = (value == null ? null : new UFDateTime(value.toString()));
        }
        else if (index == 87)
          this.m_laborflag = ((UFBoolean)value);
        else if (index == 88)
          this.m_nOrgViaPrice = ((UFDouble)value);
        else if (index == 89) {
          this.m_cadvisecalbodyid = ((String)value);
        }
        return;
      }if (index < 100) {
        if (index == 90)
          this.m_boosflag = ((UFBoolean)value);
        else if (index == 91)
          this.m_bsupplyflag = ((UFBoolean)value);
        else if (index == 92)
          this.m_creceiptareaid = ((String)value);
        else if (index == 93)
          this.m_vreceiveaddress = ((String)value);
        else if (index == 94)
          this.m_creceiptcorpid = ((String)value);
        else if (index == 95)
          this.m_ntotalplanreceiptnumber = ((UFDouble)value);
        else if (index == 96)
          this.m_isappendant = ((UFBoolean)value);
        else if (index == 97)
          this.m_invoiceauditnumber = ((UFDouble)value);
        else if (index == 98)
          this.m_receiptauditnumber = ((UFDouble)value);
        else if (index == 99) {
          this.m_outstoreauditnumber = ((UFDouble)value);
        }
        return;
      }if (index < 110) {
        if (index == 100)
          this.m_ntotalreturnnumber = ((UFDouble)value);
        else if (index == 101)
          this.m_ntotalcarrynumber = ((UFDouble)value);
        else if (index == 102)
          this.m_ntotalsignnumber = ((UFDouble)value);
        else if (index == 103)
          this.m_nlinenumber = ((Integer)value);
        else if (index == 104)
          this.m_vfreeid1 = ((String)value);
        else if (index == 105)
          this.m_vfreeid2 = ((String)value);
        else if (index == 106)
          this.m_vfreeid3 = ((String)value);
        else if (index == 107)
          this.m_vfreeid4 = ((String)value);
        else if (index == 108)
          this.m_vfreeid5 = ((String)value);
        else if (index == 109) {
          this.m_vfreeid6 = ((String)value);
        }
        return;
      }if (index < 120) {
        if (index == 110)
          this.m_vfreeid7 = ((String)value);
        else if (index == 111)
          this.m_vfreeid8 = ((String)value);
        else if (index == 112)
          this.m_vfreeid9 = ((String)value);
        else if (index == 113)
          this.m_vfreeid10 = ((String)value);
        else if (index == 114)
          this.m_vfreename1 = ((String)value);
        else if (index == 115)
          this.m_vfreename2 = ((String)value);
        else if (index == 116)
          this.m_vfreename3 = ((String)value);
        else if (index == 117)
          this.m_vfreename4 = ((String)value);
        else if (index == 118)
          this.m_vfreename5 = ((String)value);
        else if (index == 119) {
          this.m_vfreename6 = ((String)value);
        }
        return;
      }if (index < 130) {
        if (index == 120)
          this.m_vfreename7 = ((String)value);
        else if (index == 121)
          this.m_vfreename8 = ((String)value);
        else if (index == 122)
          this.m_vfreename9 = ((String)value);
        else if (index == 123)
          this.m_vfreename10 = ((String)value);
        else if (index == 124)
          this.m_cStoreAdmin = ((String)value);
        else if (index == 125)
          this.m_cInvSort = ((String)value);
        else if (index == 126)
          this.m_bFinished = ((UFBoolean)value);
        else if (index == 127)
          this.cadvisecalbody = ((String)value);
        else if (index == 128)
          this.cbodywarehousename = ((String)value);
        else if (index == 129) {
          this.cbomordercode = ((String)value);
        }
        return;
      }if (index < 140) {
        if (index == 130)
          this.ccurrencytypename = ((String)value);
        else if (index == 131)
          this.cinventorycode = ((String)value);
        else if (index == 132)
          this.cinventoryname = ((String)value);
        else if (index == 133)
          this.GGXX = ((String)value);
        else if (index == 134)
          this.cpackunitname = ((String)value);
        else if (index == 135)
          this.cprojectname = ((String)value);
        else if (index == 136)
          this.cprojectphasename = ((String)value);
        else if (index == 137)
          this.creceiptareaname = ((String)value);
        else if (index == 138)
          this.creceiptcorpname = ((String)value);
        else if (index == 139) {
          this.ct_code = ((String)value);
        }
        return;
      }if (index < 150) {
        if (index == 140)
          this.ctaxitemid = ((String)value);
        else if (index == 141)
          this.ctinvclass = ((String)value);
        else if (index == 142)
          this.cunitname = ((String)value);
        else if (index == 143)
          this.fixedflag = ((UFBoolean)value);
        else if (index == 144)
          this.isconfigable = ((UFBoolean)value);
        else if (index == 145)
          this.isspecialty = ((UFBoolean)value);
        else if (index == 146)
          this.norgviapricetax = ((UFDouble)value);
        else if (index == 147)
          this.scalefactor = ((UFDouble)value);
        else if (index == 148)
          this.sfqs = ((UFBoolean)value);
        else if (index == 149) {
          this.wholemanaflag = ((UFBoolean)value);
        }
        return;
      }if (index < 160) {
        if (index == 150)
          this.cconsigncorpid = ((String)value);
        else if (index == 151)
          this.cconsigncorp = ((String)value);
        else if (index == 152)
          this.nreturntaxrate = ((UFDouble)value);
        else if (index == 153)
          this.creccalbodyid = ((String)value);
        else if (index == 154)
          this.creccalbody = ((String)value);
        else if (index == 155)
          this.crecwareid = ((String)value);
        else if (index == 156)
          this.crecwarehouse = ((String)value);
        else if (index == 157)
          this.bdericttrans = ((UFBoolean)value);
        else if (index == 158)
          this.tconsigntime = ((String)value);
        else if (index == 159) {
          this.tdelivertime = ((String)value);
        }
        return;
      }if (index < 170) {
        if (index == 160)
          this.bsafeprice = ((UFBoolean)value);
        else if (index == 161)
          this.ntaldcnum = ((UFDouble)value);
        else if (index == 162)
          this.nasttaldcnum = ((UFDouble)value);
        else if (index == 163)
          this.ntaldcmny = ((UFDouble)value);
        else if (index == 164)
          this.breturnprofit = ((UFBoolean)value);
        else if (index == 165)
          this.nretprofnum = ((UFDouble)value);
        else if (index == 166)
          this.nastretprofnum = ((UFDouble)value);
        else if (index == 167)
          this.nretprofmny = ((UFDouble)value);
        else if (index == 168)
          this.cpricepolicyid = ((String)value);
        else if (index == 169) {
          this.cpricepolicy = ((String)value);
        }
        return;
      }if (index < 180) {
        if (index == 170)
          this.cpriceitemid = ((String)value);
        else if (index == 171)
          this.cpriceitem = ((String)value);
        else if (index == 172)
          this.cpriceitemtable = ((String)value);
        else if (index == 173)
          this.cpriceitemtablename = ((String)value);
        else if (index == 174)
          this.cpricecalproc = ((String)value);
        else if (index == 175)
          this.cpricecalprocname = ((String)value);
        else if (index == 176)
          this.cquoteunitid = ((String)value);
        else if (index == 177)
          this.cquoteunit = ((String)value);
        else if (index == 178)
          this.nquoteunitnum = ((UFDouble)value);
        else if (index == 179) {
          this.norgqttaxprc = ((UFDouble)value);
        }
        return;
      }if (index < 190) {
        if (index == 180)
          this.norgqtprc = ((UFDouble)value);
        else if (index == 181)
          this.norgqttaxnetprc = ((UFDouble)value);
        else if (index == 182)
          this.norgqtnetprc = ((UFDouble)value);
        else if (index == 183)
          this.nqttaxnetprc = ((UFDouble)value);
        else if (index == 184)
          this.nqtnetprc = ((UFDouble)value);
        else if (index == 185)
          this.nqttaxprc = ((UFDouble)value);
        else if (index == 186)
          this.nqtprc = ((UFDouble)value);
        else if (index == 187)
          this.cprolineid = ((String)value);
        else if (index == 188)
          this.cprolinename = ((String)value);
        else if (index == 189) {
          this.natp = ((UFDouble)value);
        }
        return;
      }if (index < 200) {
        if (index == 190)
          this.crecaddrnode = ((String)value);
        else if (index == 191)
          this.crecaddrnodename = ((String)value);
        else if (index == 192)
          this.ntalplconsigmny = ((UFDouble)value);
        else if (index == 193)
          this.ntaltransnum = ((UFDouble)value);
        else if (index == 194)
          this.ntaltransmny = ((UFDouble)value);
        else if (index == 195)
          this.ntaloutmny = ((UFDouble)value);
        else if (index == 196)
          this.ntalsignmny = ((UFDouble)value);
        else if (index == 197)
          this.ntalbalancemny = ((UFDouble)value);
        else if (index == 198)
          this.ntaltransretnum = ((UFDouble)value);
        else if (index == 199) {
          this.ntranslossnum = ((UFDouble)value);
        }
        return;
      }if (index < 210) {
        if (index == 200)
          this.biftransfinish = ((UFBoolean)value);
        else if (index == 201)
          this.dlastconsigdate = ((UFDate)value);
        else if (index == 202)
          this.dlasttransdate = ((UFDate)value);
        else if (index == 203)
          this.dlastoutdate = ((UFDate)value);
        else if (index == 204)
          this.dlastinvoicedt = ((UFDate)value);
        else if (index == 205)
          this.dlastpaydate = ((UFDate)value);
        else if (index == 206)
          this.nqtscalefactor = ((UFDouble)value);
        else if (index == 207)
          this.bqtfixedflag = ((UFBoolean)value);
        else if (index == 208)
          this.cinventoryid1 = ((String)value);
        else if (index == 209)
          this.cchantypeid = ((String)value);
      }
      else if (index < 220) {
        if (index == 210)
          this.cchantype = ((String)value);
        else if (index == 211)
          this.pkarrivecorp = ((String)value);
        else if (index == 212)
          this.stempbody = ((String)value);
        else if (index == 213)
          this.bifinventoryfinish_init = ((UFBoolean)value);
        else if (index == 214)
          this.noutmny_diff = ((UFDouble)value);
        else if (index == 215)
          this.m_ntotalbalancenumber = ((UFDouble)value);
        else if (index == 216)
          this.vdef7 = ((String)value);
        else if (index == 217)
          this.vdef8 = (value == null ? null : value.toString());
        else if (index == 218)
          this.vdef9 = ((String)value);
        else if (index == 219) {
          this.vdef10 = ((String)value);
        }
      }
      else if (index < 230)
      {
        if (index == 220)
          this.vdef11 = ((String)value);
        else if (index == 221)
          this.vdef12 = ((String)value);
        else if (index == 222)
          this.vdef13 = ((String)value);
        else if (index == 223)
          this.vdef14 = ((String)value);
        else if (index == 224)
          this.vdef15 = ((String)value);
        else if (index == 225)
          this.vdef16 = ((String)value);
        else if (index == 226)
          this.vdef17 = ((String)value);
        else if (index == 227)
          this.vdef18 = ((String)value);
        else if (index == 228)
          this.vdef19 = ((String)value);
        else if (index == 229)
          this.vdef20 = ((String)value);
      }
      else if (index < 240)
      {
        if (index == 230)
          this.pk_defdoc1 = ((String)value);
        else if (index == 231)
          this.pk_defdoc2 = ((String)value);
        else if (index == 232)
          this.pk_defdoc3 = ((String)value);
        else if (index == 233)
          this.pk_defdoc4 = ((String)value);
        else if (index == 234)
          this.pk_defdoc5 = ((String)value);
        else if (index == 235)
          this.pk_defdoc6 = ((String)value);
        else if (index == 236)
          this.pk_defdoc7 = ((String)value);
        else if (index == 237)
          this.pk_defdoc8 = ((String)value);
        else if (index == 238)
          this.pk_defdoc9 = ((String)value);
        else if (index == 239) {
          this.pk_defdoc10 = ((String)value);
        }
      }
      else if (index < 250)
      {
        if (index == 240)
          this.pk_defdoc11 = ((String)value);
        else if (index == 241)
          this.pk_defdoc12 = ((String)value);
        else if (index == 242)
          this.pk_defdoc13 = ((String)value);
        else if (index == 243)
          this.pk_defdoc14 = ((String)value);
        else if (index == 244)
          this.pk_defdoc15 = ((String)value);
        else if (index == 245)
          this.pk_defdoc16 = ((String)value);
        else if (index == 246)
          this.pk_defdoc17 = ((String)value);
        else if (index == 247)
          this.pk_defdoc18 = ((String)value);
        else if (index == 248)
          this.pk_defdoc19 = ((String)value);
        else if (index == 249) {
          this.pk_defdoc20 = ((String)value);
        }
      }
      else if (index < 260) {
        if (index == 250)
          this.nqtorgprc = ((UFDouble)value);
        else if (index == 251)
          this.nqtorgtaxprc = ((UFDouble)value);
        else if (index == 252)
          this.cinvspec = ((String)value);
        else if (index == 253)
          this.cinvtype = ((String)value);
        else if (index == 254)
          this.narrangescornum = ((UFDouble)value);
        else if (index == 255)
          this.narrangepoapplynum = ((UFDouble)value);
        else if (index == 256)
          this.narrangetoornum = ((UFDouble)value);
        else if (index == 257)
          this.norrangetoapplynum = ((UFDouble)value);
        else if (index == 258)
          this.barrangedflag = ((UFBoolean)value);
        else if (index == 259)
          this.carrangepersonid = ((String)value);
      }
      else if (index < 270) {
        if (index == 260)
          this.tlastarrangetime = (value == null ? null : new UFDateTime(value.toString()));
        else if (index == 261)
          this.colour = ((String)value);
        else if (index == 262)
          this.nbalmny_diff = ((UFDouble)value);
        else if (index == 263)
          this.narrangemonum = ((UFDouble)value);
        else if (index == 264)
          this.cfreecustid = ((String)value);
        else if (index == 265)
          this.ccustomerid = ((String)value);
        else if (index == 266)
          setBatpcheck((UFBoolean)value);
        else if (index == 267) {
          this.m_exets = (value == null ? null : new UFDateTime(value.toString()));
        }
        else if (index == 268)
          this.ntotalshouldoutnum = ((UFDouble)value);
        else if (index == 269) {
          this.m_nouttoplimit = ((UFDouble)value);
        }

      }
      else if (index < 280) {
        if (index == 270) {
          this.m_noutcloselimit = ((UFDouble)value);
        }
        else if (index == 271) {
          this.clargessrowno = ((String)value);
        }
        else if (index == 272)
          this.ct_name = ((String)value);
        else if (index == 273)
          this.m_ntotalcostmny = ((UFDouble)value);
        else if (index == 274) {
          this.m_ntotlbalcostnum = ((UFDouble)value);
        }
        else if (index == 275) {
            this.tsbl = ((UFDouble)value);
          }
      }

    }
    catch (Exception e)
    {
      throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060301", "UPP40060301-000096", null, new String[] { index + "", value + "" }));
    }
  }

  public void setBdericttrans(UFBoolean newBdericttrans)
  {
    this.bdericttrans = newBdericttrans;
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

  public void setBoosflag(UFBoolean newBeditflag)
  {
    this.m_boosflag = (newBeditflag == null ? new UFBoolean(false) : newBeditflag);
  }

  public void setBqtfixedflag(UFBoolean newBqtfixedflag)
  {
    this.bqtfixedflag = newBqtfixedflag;
  }

  public void setBreturnprofit(UFBoolean newBreturnprofit)
  {
    this.breturnprofit = newBreturnprofit;
  }

  public void setBsafeprice(UFBoolean newBsafeprice)
  {
    this.bsafeprice = newBsafeprice;
  }

  public void setBsupplyflag(UFBoolean newBeditflag)
  {
    this.m_bsupplyflag = newBeditflag;
  }

  public void setCadvisecalbodyid(String newCbatchid)
  {
    this.m_cadvisecalbodyid = newCbatchid;
  }

  public void setCbatchid(String newCbatchid)
  {
    this.m_cbatchid = newCbatchid;
  }

  public void setCbomorderid(String newcbomorderid)
  {
    this.m_cbomorderid = newcbomorderid;
  }

  public void setCconsigncorp(String newCconsigncorpname)
  {
    this.cconsigncorp = newCconsigncorpname;
  }

  public void setCconsigncorpid(String newCconsigncorpid)
  {
    this.cconsigncorpid = newCconsigncorpid;
  }

  public void setCfreezeid(String newcfreezeid)
  {
    this.m_cfreezeid = newcfreezeid;
  }

  public void setCinvbasdocid(String newCinvbasdocid)
  {
    this.m_cinvbasdocid = newCinvbasdocid;
  }

  public void setCpricecalproc(String newCpricecalproc)
  {
    this.cpricecalproc = newCpricecalproc;
  }

  public void setCpricecalprocname(String newCpricecalprocname)
  {
    this.cpricecalprocname = newCpricecalprocname;
  }

  public void setCpriceitem(String newCpriceitem)
  {
    this.cpriceitem = newCpriceitem;
  }

  public void setCpriceitemid(String newCpriceitemid)
  {
    this.cpriceitemid = newCpriceitemid;
  }

  public void setCpriceitemtable(String newCpriceitemtable)
  {
    this.cpriceitemtable = newCpriceitemtable;
  }

  public void setCpriceitemtablename(String newCpriceitemtablename)
  {
    this.cpriceitemtablename = newCpriceitemtablename;
  }

  public void setCpricepolicy(String newCpricepolicy)
  {
    this.cpricepolicy = newCpricepolicy;
  }

  public void setCpricepolicyid(String newCpricepolicyid)
  {
    this.cpricepolicyid = newCpricepolicyid;
  }

  public void setCprojectid(String newCprojectid1)
  {
    this.m_cprojectid = newCprojectid1;
  }

  public void setCprojectid3(String newCprojectid3)
  {
    this.m_cprojectid3 = newCprojectid3;
  }

  public void setCprojectphaseid(String newCprojectid2)
  {
    this.m_cprojectphaseid = newCprojectid2;
  }

  public void setCprolineid(String newCprolineid)
  {
    this.cprolineid = newCprolineid;
  }

  public void setCprolinename(String newCprolinename)
  {
    this.cprolinename = newCprolinename;
  }

  public void setCquoteunit(String newCquoteunit)
  {
    this.cquoteunit = newCquoteunit;
  }

  public void setCquoteunitid(String newCquoteunitid)
  {
    this.cquoteunitid = newCquoteunitid;
  }

  public void setCrecaddrnode(String newCrecaddrnode)
  {
    this.crecaddrnode = newCrecaddrnode;
  }

  public void setCrecaddrnodename(String newCrecaddrnodename)
  {
    this.crecaddrnodename = newCrecaddrnodename;
  }

  public void setCreccalbody(String newCreccalbody)
  {
    this.creccalbody = newCreccalbody;
  }

  public void setCreccalbodyid(String newCreccalbodyid)
  {
    this.creccalbodyid = newCreccalbodyid;
  }

  public void setCreceiptareaid(String newCbatchid)
  {
    this.m_creceiptareaid = newCbatchid;
  }

  public void setCreceiptcorpid(String newCbatchid)
  {
    this.m_creceiptcorpid = newCbatchid;
  }

  public void setCrecwarehouse(String newCrecwarehouse)
  {
    this.crecwarehouse = newCrecwarehouse;
  }

  public void setCrecwareid(String newCrecwareid)
  {
    this.crecwareid = newCrecwareid;
  }

  public void setCrowno(String newValue)
  {
    this.m_crowno = newValue;
  }

  public void setCt_manageid(String newct_manageid)
  {
    this.m_ct_manageid = newct_manageid;
  }

  public void setCtinvclassid(String newValue)
  {
    this.m_ctinvclassid = newValue;
  }

  public void setDiscountflag(UFBoolean newDiscountflag)
  {
    this.m_discountflag = newDiscountflag;
  }

  public void setDlastconsigdate(UFDate newDlastconsigdate)
  {
    this.dlastconsigdate = newDlastconsigdate;
  }

  public void setDlastinvoicedt(UFDate newDlastinvoicecdt)
  {
    this.dlastinvoicedt = newDlastinvoicecdt;
  }

  public void setDlastoutdate(UFDate newDlastoutdate)
  {
    this.dlastoutdate = newDlastoutdate;
  }

  public void setDlastpaydate(UFDate newDlastpaydate)
  {
    this.dlastpaydate = newDlastpaydate;
  }

  public void setDlasttransdate(UFDate newDlasttransdate)
  {
    this.dlasttransdate = newDlasttransdate;
  }

  public void setFbatchstatus(Integer newfbatchstatus)
  {
    this.m_fbatchstatus = newfbatchstatus;
  }

  public void setFinished(UFBoolean newFinished)
  {
    this.m_bFinished = newFinished;
  }

  public void setInvoiceauditNumber(UFDouble newInvoiceauditNumber)
  {
    this.m_invoiceauditnumber = newInvoiceauditNumber;
  }

  public void setNtotalshouldoutnum(UFDouble newInvoiceauditNumber) {
    this.ntotalshouldoutnum = newInvoiceauditNumber;
  }

  public void setInvSort(String newInvSort)
  {
    this.m_cInvSort = newInvSort;
  }

  public void setIsappendant(UFBoolean newValue)
  {
    this.m_isappendant = newValue;
  }

  public void setLaborflag(UFBoolean newflag)
  {
    this.m_laborflag = newflag;
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

  public void setNastretprofnum(UFDouble newNastretprofnum)
  {
    this.nastretprofnum = newNastretprofnum;
  }

  public void setNasttaldcnum(UFDouble newNasttaldcnum)
  {
    this.nasttaldcnum = newNasttaldcnum;
  }

  public void setNatp(UFDouble newNatp)
  {
    this.natp = newNatp;
  }

  public void setNlinenumber(Integer newM_nlinenumber)
  {
    this.m_nlinenumber = newM_nlinenumber;
  }

  public void setNorgqtnetprc(UFDouble newNorgqtnetprc)
  {
    this.norgqtnetprc = newNorgqtnetprc;
  }

  public void setNorgqtprc(UFDouble newNorgqtprc)
  {
    this.norgqtprc = newNorgqtprc;
  }

  public void setNorgqttaxnetprc(UFDouble newNorgqttaxnetprc)
  {
    this.norgqttaxnetprc = newNorgqttaxnetprc;
  }

  public void setNorgqttaxprc(UFDouble newNorgqttaxprc)
  {
    this.norgqttaxprc = newNorgqttaxprc;
  }

  public void setNqtnetprc(UFDouble newNqtnetprc)
  {
    this.nqtnetprc = newNqtnetprc;
  }

  public void setNqtprc(UFDouble newNqtprc)
  {
    this.nqtprc = newNqtprc;
  }

  public void setNqtscalefactor(UFDouble newNqtscalefactor)
  {
    this.nqtscalefactor = newNqtscalefactor;
  }

  public void setNqttaxnetprc(UFDouble newNqttaxnetprc)
  {
    this.nqttaxnetprc = newNqttaxnetprc;
  }

  public void setNqttaxprc(UFDouble newNqttaxprc)
  {
    this.nqttaxprc = newNqttaxprc;
  }

  public void setNquoteunitnum(UFDouble newNquoteunitnum)
  {
    this.nquoteunitnum = newNquoteunitnum;
  }

  public void setNretprofmny(UFDouble newNretprofmny)
  {
    this.nretprofmny = newNretprofmny;
  }

  public void setNretprofnum(UFDouble newNretprofnum)
  {
    this.nretprofnum = newNretprofnum;
  }

  public void setNtalbalancemny(UFDouble newNtalbalancemny)
  {
    this.ntalbalancemny = newNtalbalancemny;
  }

  public void setNtaldcmny(UFDouble newNtaldcmny)
  {
    this.ntaldcmny = newNtaldcmny;
  }

  public void setNtaldcnum(UFDouble newNtaldcnum)
  {
    this.ntaldcnum = newNtaldcnum;
  }

  public void setNtaloutmny(UFDouble newNtaloutmny)
  {
    this.ntaloutmny = newNtaloutmny;
  }

  public void setNtalplconsigmny(UFDouble newNtalplconsigmny)
  {
    this.ntalplconsigmny = newNtalplconsigmny;
  }

  public void setNtalsignmny(UFDouble newNtalsignmny)
  {
    this.ntalsignmny = newNtalsignmny;
  }

  public void setNtaltransmny(UFDouble newNtaltransmny)
  {
    this.ntaltransmny = newNtaltransmny;
  }

  public void setNtaltransnum(UFDouble newNtaltransnum)
  {
    this.ntaltransnum = newNtaltransnum;
  }

  public void setNtaltransretnum(UFDouble newNtaltransretnum)
  {
    this.ntaltransretnum = newNtaltransretnum;
  }

  public void setNtotalbalancenumber(UFDouble newNtotalbalancenumber)
  {
    this.m_ntotalbalancenumber = newNtotalbalancenumber;
  }

  public void setNtotlbalcostnum(UFDouble newNtotlbalcostnum) {
    this.m_ntotlbalcostnum = newNtotlbalcostnum;
  }

  public void setNtotalcarrynumber(UFDouble newNtotalcarrynumber)
  {
    this.m_ntotalcarrynumber = newNtotalcarrynumber;
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

  public void setNtotalplanreceiptnumber(UFDouble newValue)
  {
    this.m_ntotalplanreceiptnumber = newValue;
  }

  public void setNtotalreceiptnumber(UFDouble newNtotalreceiptnumber)
  {
    this.m_ntotalreceiptnumber = newNtotalreceiptnumber;
  }

  public void setNtotalreturnnumber(UFDouble newNtotalreturnnumber)
  {
    this.m_ntotalreturnnumber = newNtotalreturnnumber;
  }

  public void setNtotalsignnumber(UFDouble newNtotalsignnumber)
  {
    this.m_ntotalsignnumber = newNtotalsignnumber;
  }

  public void setNtranslossnum(UFDouble newNtranslossnum)
  {
    this.ntranslossnum = newNtranslossnum;
  }

  public void setOrgViaPrice(UFDouble newOrgViaPrice)
  {
    this.m_nOrgViaPrice = newOrgViaPrice;
  }

  public void setOutstoreauditNumber(UFDouble newOutstoreauditNumber)
  {
    this.m_outstoreauditnumber = newOutstoreauditNumber;
  }

  public void setPkcorp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setReceiptauditNumber(UFDouble newReceiptauditNumber)
  {
    this.m_receiptauditnumber = newReceiptauditNumber;
  }

  public void setScalefactor(UFDouble value)
  {
    this.scalefactor = value;
  }

  public void setStoreAdmin(String newStoreAdmin)
  {
    this.m_cStoreAdmin = newStoreAdmin;
  }

  public void setTconsigntime(String newTconsigntime)
  {
    this.tconsigntime = newTconsigntime;
  }

  public void setTdelivertime(String newTdelivertime)
  {
    this.tdelivertime = newTdelivertime;
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

  public void setVreceiveaddress(String newCbatchid)
  {
    this.m_vreceiveaddress = newCbatchid;
  }

  public String geCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public UFBoolean getBifinventoryfinish_init()
  {
    return this.bifinventoryfinish_init;
  }

  public UFBoolean getBiftransfinish()
  {
    return this.biftransfinish;
  }

  public String getCchantype()
  {
    return this.cchantype;
  }

  public String getCchantypeid()
  {
    return this.cchantypeid;
  }

  public String getCinventoryid1()
  {
    if ((this.cinventoryid1 == null) || (this.cinventoryid1.trim().length() <= 0)) {
      if (this.cconsigncorpid == null)
        this.cinventoryid1 = this.m_cinventoryid;
      else if (this.cconsigncorpid.equals(this.m_pk_corp)) {
        this.cinventoryid1 = this.m_cinventoryid;
      }
    }
    return this.cinventoryid1;
  }

  public UFBoolean getDiscountflag()
  {
    return this.m_discountflag;
  }

  public UFBoolean getFixedflag()
  {
    return this.fixedflag;
  }

  public int getIAction()
  {
    return this.iAction;
  }

  public UFDouble getNoutmny_diff()
  {
    return this.noutmny_diff;
  }

  public UFDouble getNreturntaxrate()
  {
    return this.nreturntaxrate;
  }

  public String getPkarrivecorp()
  {
    return this.pkarrivecorp;
  }

  public String getStempbody()
  {
    return this.stempbody;
  }

  public void setBifinventoryfinish_init(UFBoolean newBifinventoryfinish_init)
  {
    this.bifinventoryfinish_init = newBifinventoryfinish_init;
  }

  public void setBiftransfinish(UFBoolean newBiftransfinish)
  {
    this.biftransfinish = newBiftransfinish;
  }

  public void setCchantype(String newCchantype)
  {
    this.cchantype = newCchantype;
  }

  public void setCchantypeid(String newCchantypeid)
  {
    this.cchantypeid = newCchantypeid;
  }

  public void setCinventoryid1(String newCinventoryid1)
  {
    this.cinventoryid1 = newCinventoryid1;
  }

  public void setFixedflag(UFBoolean newFixedflag)
  {
    this.fixedflag = newFixedflag;
  }

  public void setIAction(int newIAction)
  {
    this.iAction = newIAction;
  }

  public void setNoutmny_diff(UFDouble newNoutmny_diff)
  {
    this.noutmny_diff = newNoutmny_diff;
  }

  public void setNreturntaxrate(UFDouble newNreturntaxrate)
  {
    this.nreturntaxrate = newNreturntaxrate;
  }

  public void setPkarrivecorp(String newPkarrivecorp)
  {
    this.pkarrivecorp = newPkarrivecorp;
  }

  public void setStempbody(String newStempbody)
  {
    this.stempbody = newStempbody;
  }

  public String getPk_defdoc1()
  {
    return this.pk_defdoc1;
  }

  public void setPk_defdoc1(String pk_defdoc1)
  {
    this.pk_defdoc1 = pk_defdoc1;
  }

  public String getPk_defdoc10()
  {
    return this.pk_defdoc10;
  }

  public void setPk_defdoc10(String pk_defdoc10)
  {
    this.pk_defdoc10 = pk_defdoc10;
  }

  public String getPk_defdoc11()
  {
    return this.pk_defdoc11;
  }

  public UFDouble getNqtorgprc()
  {
    return this.nqtorgprc;
  }

  public void setNqtorgprc(UFDouble nqtorgprc)
  {
    this.nqtorgprc = nqtorgprc;
  }

  public UFDouble getNqtorgtaxprc()
  {
    return this.nqtorgtaxprc;
  }

  public void setNqtorgtaxprc(UFDouble nqtorgtaxprc)
  {
    this.nqtorgtaxprc = nqtorgtaxprc;
  }

  public void setPk_defdoc11(String pk_defdoc11)
  {
    this.pk_defdoc11 = pk_defdoc11;
  }

  public String getPk_defdoc12()
  {
    return this.pk_defdoc12;
  }

  public void setPk_defdoc12(String pk_defdoc12)
  {
    this.pk_defdoc12 = pk_defdoc12;
  }

  public String getPk_defdoc13()
  {
    return this.pk_defdoc13;
  }

  public void setPk_defdoc13(String pk_defdoc13)
  {
    this.pk_defdoc13 = pk_defdoc13;
  }

  public String getPk_defdoc14()
  {
    return this.pk_defdoc14;
  }

  public void setPk_defdoc14(String pk_defdoc14)
  {
    this.pk_defdoc14 = pk_defdoc14;
  }

  public String getPk_defdoc15()
  {
    return this.pk_defdoc15;
  }

  public void setPk_defdoc15(String pk_defdoc15)
  {
    this.pk_defdoc15 = pk_defdoc15;
  }

  public String getPk_defdoc16()
  {
    return this.pk_defdoc16;
  }

  public void setPk_defdoc16(String pk_defdoc16)
  {
    this.pk_defdoc16 = pk_defdoc16;
  }

  public String getPk_defdoc17()
  {
    return this.pk_defdoc17;
  }

  public void setPk_defdoc17(String pk_defdoc17)
  {
    this.pk_defdoc17 = pk_defdoc17;
  }

  public String getPk_defdoc18()
  {
    return this.pk_defdoc18;
  }

  public void setPk_defdoc18(String pk_defdoc18)
  {
    this.pk_defdoc18 = pk_defdoc18;
  }

  public String getPk_defdoc19()
  {
    return this.pk_defdoc19;
  }

  public void setPk_defdoc19(String pk_defdoc19)
  {
    this.pk_defdoc19 = pk_defdoc19;
  }

  public String getPk_defdoc2()
  {
    return this.pk_defdoc2;
  }

  public void setPk_defdoc2(String pk_defdoc2)
  {
    this.pk_defdoc2 = pk_defdoc2;
  }

  public String getPk_defdoc20()
  {
    return this.pk_defdoc20;
  }

  public void setPk_defdoc20(String pk_defdoc20)
  {
    this.pk_defdoc20 = pk_defdoc20;
  }

  public String getPk_defdoc3()
  {
    return this.pk_defdoc3;
  }

  public void setPk_defdoc3(String pk_defdoc3)
  {
    this.pk_defdoc3 = pk_defdoc3;
  }

  public String getPk_defdoc4()
  {
    return this.pk_defdoc4;
  }

  public void setPk_defdoc4(String pk_defdoc4)
  {
    this.pk_defdoc4 = pk_defdoc4;
  }

  public String getPk_defdoc5()
  {
    return this.pk_defdoc5;
  }

  public void setPk_defdoc5(String pk_defdoc5)
  {
    this.pk_defdoc5 = pk_defdoc5;
  }

  public String getPk_defdoc6()
  {
    return this.pk_defdoc6;
  }

  public void setPk_defdoc6(String pk_defdoc6)
  {
    this.pk_defdoc6 = pk_defdoc6;
  }

  public String getPk_defdoc7()
  {
    return this.pk_defdoc7;
  }

  public void setPk_defdoc7(String pk_defdoc7)
  {
    this.pk_defdoc7 = pk_defdoc7;
  }

  public String getPk_defdoc8()
  {
    return this.pk_defdoc8;
  }

  public void setPk_defdoc8(String pk_defdoc8)
  {
    this.pk_defdoc8 = pk_defdoc8;
  }

  public String getPk_defdoc9()
  {
    return this.pk_defdoc9;
  }

  public void setPk_defdoc9(String pk_defdoc9)
  {
    this.pk_defdoc9 = pk_defdoc9;
  }

  public String getVdef10()
  {
    return this.vdef10;
  }

  public void setVdef10(String vdef10)
  {
    this.vdef10 = vdef10;
  }

  public String getVdef11()
  {
    return this.vdef11;
  }

  public void setVdef11(String vdef11)
  {
    this.vdef11 = vdef11;
  }

  public String getVdef12()
  {
    return this.vdef12;
  }

  public void setVdef12(String vdef12)
  {
    this.vdef12 = vdef12;
  }

  public String getVdef13()
  {
    return this.vdef13;
  }

  public void setVdef13(String vdef13)
  {
    this.vdef13 = vdef13;
  }

  public String getVdef14()
  {
    return this.vdef14;
  }

  public void setVdef14(String vdef14)
  {
    this.vdef14 = vdef14;
  }

  public String getVdef15()
  {
    return this.vdef15;
  }

  public void setVdef15(String vdef15)
  {
    this.vdef15 = vdef15;
  }

  public String getVdef16()
  {
    return this.vdef16;
  }

  public void setVdef16(String vdef16)
  {
    this.vdef16 = vdef16;
  }

  public String getVdef17()
  {
    return this.vdef17;
  }

  public void setVdef17(String vdef17)
  {
    this.vdef17 = vdef17;
  }

  public String getVdef18()
  {
    return this.vdef18;
  }

  public void setVdef18(String vdef18)
  {
    this.vdef18 = vdef18;
  }

  public String getVdef19()
  {
    return this.vdef19;
  }

  public void setVdef19(String vdef19)
  {
    this.vdef19 = vdef19;
  }

  public String getVdef20()
  {
    return this.vdef20;
  }

  public void setVdef20(String vdef20)
  {
    this.vdef20 = vdef20;
  }

  public String getVdef7()
  {
    return this.vdef7;
  }

  public void setVdef7(String vdef7)
  {
    this.vdef7 = vdef7;
  }

  public String getVdef8()
  {
    return this.vdef8;
  }

  public void setVdef8(String vdef8)
  {
    this.vdef8 = vdef8;
  }

  public String getVdef9()
  {
    return this.vdef9;
  }

  public void setVdef9(String vdef9)
  {
    this.vdef9 = vdef9;
  }

  public SaleorderBVO getOldbvo()
  {
    return this.oldbvo;
  }

  public void setOldbvo(SaleorderBVO newOldbvo)
  {
    this.oldbvo = newOldbvo;
  }

  public boolean isIsDel7D()
  {
    return this.isDel7D;
  }

  public void setIsDel7D(boolean newIsDel7D)
  {
    this.isDel7D = newIsDel7D;
  }

  public UFBoolean getBarrangedflag()
  {
    return this.barrangedflag;
  }

  public void setBarrangedflag(UFBoolean barrangedflag)
  {
    this.barrangedflag = barrangedflag;
  }

  public String getCarrangepersonid()
  {
    return this.carrangepersonid;
  }

  public void setCarrangepersonid(String carrangepersonid)
  {
    this.carrangepersonid = carrangepersonid;
  }

  public UFDouble getNarrangepoapplynum()
  {
    return this.narrangepoapplynum;
  }

  public void setNarrangepoapplynum(UFDouble narrangepoapplynum)
  {
    this.narrangepoapplynum = narrangepoapplynum;
  }

  public UFDouble getNarrangescornum()
  {
    return this.narrangescornum;
  }

  public void setNarrangescornum(UFDouble narrangescornum)
  {
    this.narrangescornum = narrangescornum;
  }

  public UFDouble getNarrangetoornum()
  {
    return this.narrangetoornum;
  }

  public void setNarrangetoornum(UFDouble narrangetoornum)
  {
    this.narrangetoornum = narrangetoornum;
  }

  public UFDouble getNorrangetoapplynum()
  {
    return this.norrangetoapplynum;
  }

  public void setNorrangetoapplynum(UFDouble norrangetoapplynum)
  {
    this.norrangetoapplynum = norrangetoapplynum;
  }

  public UFDateTime getTlastarrangetime()
  {
    return this.tlastarrangetime;
  }

  public void setTlastarrangetime(UFDateTime tlastarrangetime)
  {
    this.tlastarrangetime = tlastarrangetime;
  }

  public UFDouble getNbalmny_diff()
  {
    return this.nbalmny_diff;
  }

  public void setNbalmny_diff(UFDouble nbalmny_diff)
  {
    this.nbalmny_diff = nbalmny_diff;
  }

  public UFDouble getNarrangemonum()
  {
    return this.narrangemonum;
  }

  public void setNarrangemonum(UFDouble narrangemonum)
  {
    this.narrangemonum = narrangemonum;
  }

  public int compareTo(Object obj)
  {
    if ((obj == null) || (obj.getClass() != SaleorderBVO.class)) {
      return -1;
    }
    SaleorderBVO combvo = (SaleorderBVO)obj;
    return getNrowno().compareTo(combvo.getNrowno());
  }

  public boolean isBCheckATP()
  {
    return this.bCheckATP;
  }

  public void setBCheckATP(boolean checkATP)
  {
    this.bCheckATP = checkATP;
  }

  public String getCcustomerid()
  {
    return this.ccustomerid;
  }

  public void setCcustomerid(String ccustomerid)
  {
    this.ccustomerid = ccustomerid;
  }

  public String getCfreecustid()
  {
    return this.cfreecustid;
  }

  public void setCfreecustid(String cfreecustid)
  {
    this.cfreecustid = cfreecustid;
  }

  public UFBoolean getBatpcheck()
  {
    return this.bCheckATP ? SoVoConst.buftrue : SoVoConst.buffalse;
  }

  public void setBatpcheck(UFBoolean bcheckatp)
  {
    if ((bcheckatp == null) || (bcheckatp.booleanValue()))
      this.bCheckATP = true;
    else
      this.bCheckATP = false;
  }

  public UFDateTime getExets()
  {
    return this.m_exets;
  }

  public void setExets(UFDateTime m_exets)
  {
    this.m_exets = m_exets;
  }

  public String getInventory()
  {
    return getCinventoryid();
  }

  public UFDouble getDiscount()
  {
    return getNdiscountrate();
  }

  public UFDouble getItemDiscount()
  {
    return getNitemdiscountrate();
  }

  public UFDouble getTaxNetPrice()
  {
    return getNtaxnetprice();
  }

  public ClientLink getClientLink()
  {
    return this.clientLink;
  }

  public void setClientLink(ClientLink cl) {
    this.clientLink = cl;
  }

public UFDouble getTsbl()
{
	return tsbl;
}

public void setTsbl(UFDouble tsbl)
{
	this.tsbl = tsbl;
}
}