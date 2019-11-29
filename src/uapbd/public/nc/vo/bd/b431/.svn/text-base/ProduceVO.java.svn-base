package nc.vo.bd.b431;

import java.util.ArrayList;
import nc.vo.bd.def.IDefAccess;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.general.GeneralExVO;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
 
@SuppressWarnings("serial")
public class ProduceVO extends CircularlyAccessibleValueObject
  implements IDefAccess
{
  private String m_pk_produce;
  private String m_pk_corp;
  private String m_pk_calbody;
  private String m_pk_invbasdoc;
  private String m_matertype;
  private String m_outtype;
  private UFBoolean m_virtualflag;
  private UFDouble m_safetystocknum;
  private String m_pk_stordoc;
  private UFBoolean m_converseflag;
  private String m_supplytype;
  private UFBoolean m_primaryflag;
  private UFDouble m_fixedahead;
  private UFDouble m_aheadcoff;
  private UFDouble m_aheadbatch;
  private String m_batchrule;
  private UFDouble m_batchnum;
  private UFDouble m_batchincrnum;
  private UFDouble m_minbatchnum;
  private String m_scheattr;
  private String m_pk_psndoc4;
  private String m_pk_psndoc3;
  private String m_pk_deptdoc3;
  private UFDouble m_minmulnum;
  private UFDouble m_roundingnum;
  private UFDouble m_wasterrate;
  private UFDouble m_prevahead;
  private UFDouble m_sumahead;
  private UFDouble m_endahead;
  private UFDouble m_accquiretime;
  private UFDouble m_confirmtime;
  private Integer m_producemethod;
  private UFBoolean m_combineflag;
  private Integer m_lowlevelcode;
  private Integer m_flanmadenum;
  private UFDouble m_flanwidenum;
  private UFDouble m_flanlennum;
  private UFDouble m_rationwtnum;
  private Integer m_pricemethod;
  private UFDouble m_maxstornum;
  private UFDouble m_lowstocknum;
  private UFDouble m_primnessnum;
  private UFDouble m_grosswtnum;
  private UFDouble m_netwtnum;
  private String m_pk_invmandoc;
  private UFBoolean m_sfscx;
  private UFBoolean m_sfzzcp;
  private UFBoolean m_sfbj;
  private UFBoolean m_sfcbdx;
  private UFDouble m_zdhd;
  private String m_cgzz;
  private String m_zgys;
  private String m_fgys;
  private String m_ybgys;
  private String m_gfwlbm;
  private String m_abcfl;
  private UFDouble m_jhj;
  private UFDouble m_ckcb;
  private UFDouble m_cksj;
  private UFBoolean m_isused;
  private String abcFundeg;
  private String abcGrosspft;
  private String abcPurchase;
  private String abcsales;
  private UFBoolean chkFreeFlag;
  private UFBoolean chkStockByCheck;
  private UFDouble chngAmount;
  private UFDouble currentAmount;
  private UFDouble nbzyj;
  private UFDouble realUsableAmount;
  private Integer roadType;
  private UFBoolean sffzfw;
  private UFBoolean sfpchs;
  private String usableAmount;
  private String usableamountbyfree;
  private UFDouble ecobatch;
  private Integer bomtype;
  private UFBoolean iswholesetsend;
  private UFBoolean issend;
  private UFBoolean issendbydatum;
  private UFDouble datumofsend;
  private Integer materclass;
  private Integer materstate;
  private Integer batchperiodnum;
  private UFBoolean isctlbyfixperiod;
  private UFBoolean iscreatesonorder;
  private UFDate fixperiodbegin;
  private BatchlistVO[] batchlist;
  private UFBoolean isctlbyprimarycode;
  private UFBoolean isctlbysecondarycode;
  private String def1;
  private String def2;
  private String def3;
  private String def4;
  private String def5;
  private Integer stocklowerdays;
  private Integer stockupperdays;
  private UFBoolean iscostbyorder;
  private UFBoolean isfxjz;
  private Integer pchscscd;
  private String pk_ckjlcid;
  private String pk_rkjlcid;
  private Integer scscddms;
  private Integer jyrhzdyw;
  private UFDouble nyzbmxs;
  private Integer wghxcl;
  private UFBoolean scxybzsfzk;
  private UFBoolean sfzb;
  private UFDouble wggdsczkxs;
  private String zbczjyxm;
  private UFDouble zbxs;
  private UFDouble blgdsczkxs;
  private Integer outnumhistorydays;
  private String jyrhzdyw_show;
  private String materclass_show;
  private String materstate_show;
  private String matertype_show;
  private String outtype_show;
  private String pchscscd_show;
  private String pricemethod_show;
  private String roadtype_show;
  private String scheattr_show;
  private String scscddms_show;
  private String supplytype_show;
  private String wghxcl_show;
  private String batchrule_show;
  private String bomtype_show;
  private String pk_sealuser;
  private String sealuserName;
  private UFDate sealdate;
  private UFBoolean sealflag;
  private UFBoolean iselementcheck;
  private Integer fcpclgsfa;
  private String fcpclgsfa_show;
  private UFBoolean isuseroute;
  private UFBoolean isctoutput;
  private Integer dr;
  private String pk_cargdoc;//add by zwx 2016-1-20 

  
  public ProduceVO()
  {
  }

  public ProduceVO(String newPk_produce)
  {
    this.m_pk_produce = newPk_produce;
  }

  public ProduceVO(GeneralExVO defaultValueVO)
  {
    initDefaultValue(defaultValueVO);
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone();
    } catch (Exception e) {
    }
    ProduceVO produce = (ProduceVO)o;

    return produce;
  }

  public String getEntityName()
  {
    return "Produce";
  }

  public String getPrimaryKey()
  {
    return this.m_pk_produce;
  }

  public void setPrimaryKey(String newPk_produce)
  {
    this.m_pk_produce = newPk_produce;
  }

  public String getPk_produce()
  {
    return this.m_pk_produce;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getPk_calbody()
  {
    return this.m_pk_calbody;
  }

  public String getPk_invbasdoc()
  {
    return this.m_pk_invbasdoc;
  }

  public String getMatertype()
  {
    return this.m_matertype;
  }

  public String getOuttype()
  {
    return this.m_outtype;
  }

  public UFBoolean getVirtualflag()
  {
    return this.m_virtualflag;
  }

  public UFDouble getSafetystocknum()
  {
    return this.m_safetystocknum;
  }

  public String getPk_stordoc()
  {
    return this.m_pk_stordoc;
  }

  public UFBoolean getConverseflag()
  {
    return this.m_converseflag;
  }

  public String getSupplytype()
  {
    return this.m_supplytype;
  }

  public UFBoolean getPrimaryflag()
  {
    return this.m_primaryflag;
  }

  public UFDouble getFixedahead()
  {
    return this.m_fixedahead;
  }

  public UFDouble getAheadcoff()
  {
    return this.m_aheadcoff;
  }

  public UFDouble getAheadbatch()
  {
    return this.m_aheadbatch;
  }

  public String getBatchrule()
  {
    return this.m_batchrule;
  }

  public UFDouble getBatchnum()
  {
    return this.m_batchnum;
  }

  public UFDouble getBatchincrnum()
  {
    return this.m_batchincrnum;
  }

  public UFDouble getMinbatchnum()
  {
    return this.m_minbatchnum;
  }

  public String getScheattr()
  {
    return this.m_scheattr;
  }

  public String getPk_psndoc4()
  {
    return this.m_pk_psndoc4;
  }

  public String getPk_psndoc3()
  {
    return this.m_pk_psndoc3;
  }

  public String getPk_deptdoc3()
  {
    return this.m_pk_deptdoc3;
  }

  public UFDouble getMinmulnum()
  {
    return this.m_minmulnum;
  }

  public UFDouble getRoundingnum()
  {
    return this.m_roundingnum;
  }

  public UFDouble getWasterrate()
  {
    return this.m_wasterrate;
  }

  public UFDouble getPrevahead()
  {
    return this.m_prevahead;
  }

  public UFDouble getSumahead()
  {
    return this.m_sumahead;
  }

  public UFDouble getEndahead()
  {
    return this.m_endahead;
  }

  public UFDouble getAccquiretime()
  {
    return this.m_accquiretime;
  }

  public UFDouble getConfirmtime()
  {
    return this.m_confirmtime;
  }

  public Integer getProducemethod()
  {
    return this.m_producemethod;
  }

  public UFBoolean getCombineflag()
  {
    return this.m_combineflag;
  }

  public Integer getLowlevelcode()
  {
    return this.m_lowlevelcode;
  }

  public Integer getFlanmadenum()
  {
    return this.m_flanmadenum;
  }

  public UFDouble getFlanwidenum()
  {
    return this.m_flanwidenum;
  }

  public UFDouble getFlanlennum()
  {
    return this.m_flanlennum;
  }

  public UFDouble getRationwtnum()
  {
    return this.m_rationwtnum;
  }

  public Integer getPricemethod()
  {
    return this.m_pricemethod;
  }

  public UFDouble getMaxstornum()
  {
    return this.m_maxstornum;
  }

  public UFDouble getLowstocknum()
  {
    return this.m_lowstocknum;
  }

  public UFDouble getPrimnessnum()
  {
    return this.m_primnessnum;
  }

  public UFDouble getGrosswtnum()
  {
    return this.m_grosswtnum;
  }

  public UFDouble getNetwtnum()
  {
    return this.m_netwtnum;
  }

  public String getPk_invmandoc()
  {
    return this.m_pk_invmandoc;
  }

  public UFBoolean getSfscx()
  {
    return this.m_sfscx;
  }

  public UFBoolean getSfzzcp()
  {
    return this.m_sfzzcp;
  }

  public UFBoolean getSfbj()
  {
    return this.m_sfbj;
  }

  public UFBoolean getSfcbdx()
  {
    return this.m_sfcbdx;
  }

  public UFDouble getZdhd()
  {
    return this.m_zdhd;
  }

  public String getCgzz()
  {
    return this.m_cgzz;
  }

  public String getZgys()
  {
    return this.m_zgys;
  }

  public String getFgys()
  {
    return this.m_fgys;
  }

  public String getYbgys()
  {
    return this.m_ybgys;
  }

  public String getGfwlbm()
  {
    return this.m_gfwlbm;
  }

  public UFDouble getJhj()
  {
    return this.m_jhj;
  }

  public UFDouble getCkcb()
  {
    return this.m_ckcb;
  }

  public UFDouble getCksj()
  {
    return this.m_cksj;
  }

  public void setPk_produce(String newPk_produce)
  {
    this.m_pk_produce = newPk_produce;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setPk_calbody(String newPk_calbody)
  {
    this.m_pk_calbody = newPk_calbody;
  }

  public void setPk_invbasdoc(String newPk_invbasdoc)
  {
    this.m_pk_invbasdoc = newPk_invbasdoc;
  }

  public void setMatertype(String newMatertype)
  {
    this.m_matertype = newMatertype;
  }

  public void setOuttype(String newOuttype)
  {
    this.m_outtype = newOuttype;
  }

  public void setVirtualflag(UFBoolean newVirtualflag)
  {
    this.m_virtualflag = newVirtualflag;
  }

  public void setSafetystocknum(UFDouble newSafetystocknum)
  {
    this.m_safetystocknum = newSafetystocknum;
  }

  public void setPk_stordoc(String newPk_stordoc)
  {
    this.m_pk_stordoc = newPk_stordoc;
  }

  public void setConverseflag(UFBoolean newConverseflag)
  {
    this.m_converseflag = newConverseflag;
  }

  public void setSupplytype(String newSupplytype)
  {
    this.m_supplytype = newSupplytype;
  }

  public void setPrimaryflag(UFBoolean newPrimaryflag)
  {
    this.m_primaryflag = newPrimaryflag;
  }

  public void setFixedahead(UFDouble newFixedahead)
  {
    this.m_fixedahead = newFixedahead;
  }

  public void setAheadcoff(UFDouble newAheadcoff)
  {
    this.m_aheadcoff = newAheadcoff;
  }

  public void setAheadbatch(UFDouble newAheadbatch)
  {
    this.m_aheadbatch = newAheadbatch;
  }

  public void setBatchrule(String newBatchrule)
  {
    this.m_batchrule = newBatchrule;
  }

  public void setBatchnum(UFDouble newBatchnum)
  {
    this.m_batchnum = newBatchnum;
  }

  public void setBatchincrnum(UFDouble newBatchincrnum)
  {
    this.m_batchincrnum = newBatchincrnum;
  }

  public void setMinbatchnum(UFDouble newMinbatchnum)
  {
    this.m_minbatchnum = newMinbatchnum;
  }

  public void setScheattr(String newScheattr)
  {
    this.m_scheattr = newScheattr;
  }

  public void setPk_psndoc4(String newPk_psndoc4)
  {
    this.m_pk_psndoc4 = newPk_psndoc4;
  }

  public void setPk_psndoc3(String newPk_psndoc3)
  {
    this.m_pk_psndoc3 = newPk_psndoc3;
  }

  public void setPk_deptdoc3(String newPk_deptdoc3)
  {
    this.m_pk_deptdoc3 = newPk_deptdoc3;
  }

  public void setMinmulnum(UFDouble newMinmulnum)
  {
    this.m_minmulnum = newMinmulnum;
  }

  public void setRoundingnum(UFDouble newRoundingnum)
  {
    this.m_roundingnum = newRoundingnum;
  }

  public void setWasterrate(UFDouble newWasterrate)
  {
    this.m_wasterrate = newWasterrate;
  }

  public void setPrevahead(UFDouble newPrevahead)
  {
    this.m_prevahead = newPrevahead;
  }

  public void setSumahead(UFDouble newSumahead)
  {
    this.m_sumahead = newSumahead;
  }

  public void setEndahead(UFDouble newEndahead)
  {
    this.m_endahead = newEndahead;
  }

  public void setAccquiretime(UFDouble newAccquiretime)
  {
    this.m_accquiretime = newAccquiretime;
  }

  public void setConfirmtime(UFDouble newConfirmtime)
  {
    this.m_confirmtime = newConfirmtime;
  }

  public void setProducemethod(Integer newProducemethod)
  {
    this.m_producemethod = newProducemethod;
  }

  public void setCombineflag(UFBoolean newCombineflag)
  {
    this.m_combineflag = newCombineflag;
  }

  public void setLowlevelcode(Integer newLowlevelcode)
  {
    this.m_lowlevelcode = newLowlevelcode;
  }

  public void setFlanmadenum(Integer newFlanmadenum)
  {
    this.m_flanmadenum = newFlanmadenum;
  }

  public void setFlanwidenum(UFDouble newFlanwidenum)
  {
    this.m_flanwidenum = newFlanwidenum;
  }

  public void setFlanlennum(UFDouble newFlanlennum)
  {
    this.m_flanlennum = newFlanlennum;
  }

  public void setRationwtnum(UFDouble newRationwtnum)
  {
    this.m_rationwtnum = newRationwtnum;
  }

  public void setPricemethod(Integer newPricemethod)
  {
    this.m_pricemethod = newPricemethod;
  }

  public void setMaxstornum(UFDouble newMaxstornum)
  {
    this.m_maxstornum = newMaxstornum;
  }

  public void setLowstocknum(UFDouble newLowstocknum)
  {
    this.m_lowstocknum = newLowstocknum;
  }

  public void setPrimnessnum(UFDouble newPrimnessnum)
  {
    this.m_primnessnum = newPrimnessnum;
  }

  public void setGrosswtnum(UFDouble newGrosswtnum)
  {
    this.m_grosswtnum = newGrosswtnum;
  }

  public void setNetwtnum(UFDouble newNetwtnum)
  {
    this.m_netwtnum = newNetwtnum;
  }

  public void setPk_invmandoc(String newPk_invmandoc)
  {
    this.m_pk_invmandoc = newPk_invmandoc;
  }

  public void setSfscx(UFBoolean newSfscx)
  {
    this.m_sfscx = newSfscx;
  }

  public void setSfzzcp(UFBoolean newSfzzcp)
  {
    this.m_sfzzcp = newSfzzcp;
  }

  public void setSfbj(UFBoolean newSfbj)
  {
    this.m_sfbj = newSfbj;
  }

  public void setSfcbdx(UFBoolean newSfcbdx)
  {
    this.m_sfcbdx = newSfcbdx;
  }

  public void setZdhd(UFDouble newZdhd)
  {
    this.m_zdhd = newZdhd;
  }

  public void setCgzz(String newCgzz)
  {
    this.m_cgzz = newCgzz;
  }

  public void setZgys(String newZgys)
  {
    this.m_zgys = newZgys;
  }

  public void setFgys(String newFgys)
  {
    this.m_fgys = newFgys;
  }

  public void setYbgys(String newYbgys)
  {
    this.m_ybgys = newYbgys;
  }

  public void setGfwlbm(String newGfwlbm)
  {
    this.m_gfwlbm = newGfwlbm;
  }

  public void setJhj(UFDouble newJhj)
  {
    this.m_jhj = newJhj;
  }

  public void setCkcb(UFDouble newCkcb)
  {
    this.m_ckcb = newCkcb;
  }

  public void setCksj(UFDouble newCksj)
  {
    this.m_cksj = newCksj;
  }

  @SuppressWarnings("unchecked")
public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.m_pk_produce == null) {
      errFields.add(new String("m_pk_produce"));
    }
    if (this.m_pk_corp == null) {
      errFields.add(new String("m_pk_corp"));
    }
    if (this.m_pk_calbody == null) {
      errFields.add(new String("m_pk_calbody"));
    }
    if (this.m_matertype == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UC000-0002910")));
    }

    if (this.m_outtype == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UC000-0001404")));
    }

    if (this.m_virtualflag == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UC000-0002422")));
    }

    if (this.m_supplytype == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UC000-0001371")));
    }

    if (this.materclass == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UPT10081210-000022")));
    }

    if (this.m_batchrule == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UC000-0002067")));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UPP10081210-000000"));

    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("10081210", "UPP10081210-000001"));

        message.append(temp[i]);
      }

      throw new NullFieldException(message.toString());
    }
  }

  public String getAbcfl()
  {
    return this.m_abcfl;
  }

  public String getAbcFundeg()
  {
    return this.abcFundeg;
  }

  public String getAbcGrosspft()
  {
    return this.abcGrosspft;
  }

  public String getAbcPurchase()
  {
    return this.abcPurchase;
  }

  public String getAbcsales()
  {
    return this.abcsales;
  }

  public String getAbssales()
  {
    return this.abcsales;
  }

  public void setAbssales(String newAbs) {
    this.abcsales = newAbs;
  }

  public UFBoolean getIscreatesonprodorder()
  {
    return this.iscreatesonorder;
  }
  public void setIscreatesonprodorder(UFBoolean newIscreatesonprodorder) {
    this.iscreatesonorder = newIscreatesonprodorder;
  }

  public String[] getAttributeNames()
  {
    return new String[] { "pk_produce", "pk_invbasdoc", "pk_invmandoc", "pk_calbody", "isused", "sfpchs", "pk_stordoc", "safetystocknum", "maxstornum", "lowstocknum", "pricemethod", "jhj", "ckcb", "cksj", "primnessnum", "zdhd", "cgzz", "zgys", "fgys", "ybgys", "chngamount", "gfwlbm", "abssales", "abcgrosspft", "abcpurchase", "abcfundeg", "stocklowerdays", "stockupperdays", "nbzyj", "matertype", "outtype", "materstate", "bomtype", "roadtype", "materclass", "supplytype", "iscreatesonprodorder", "producemethod", "issendbydatum", "datumofsend", "virtualflag", "combineflag", "primaryflag", "sfzzcp", "sfcbdx", "sffzfw", "iswholesetsend", "issend", "converseflag", "chkfreeflag", "stockbycheck", "pk_deptdoc3", "pk_psndoc3", "pk_psndoc4", "scheattr", "accquiretime", "confirmtime", "batchrule", "ecobatch", "isctlbyfixperiod", "batchperiodnum", "sumahead", "fixperiodbegin", "aheadbatch", "aheadcoff", "fixedahead", "roundingnum", "prevahead", "minmulnum", "wasterrate", "endahead", "lowlevelcode", "usableamount", "usableamountbyfree", "flanlennum", "flanwidenum", "flanmadenum", "rationwtnum", "grosswtnum", "netwtnum", "isctlbyprimarycode", "isctlbysecondarycode", "def1", "def2", "def3", "def4", "def5", "iscostbyorder", "isfxjz", "pchscscd", "pk_rkjlcid", "pk_ckjlcid", "wghxcl", "scscddms", "nyzbmxs", "jyrhzdyw", "sfzb", "scxybzsfzk", "wggdsczkxs", "blgdsczkxs", "zbxs", "zbczjyxm", "outnumhistorydays", "sfrpc", "usableamount0", "usableamount1", "usableamount2", "usableamount3", "usableamount4", "usableamount5", "usableamount6", "usableamount7", "usableamount8", "usableamount9", "usableamount10", "usableamount11", "usableamount12", "usableamount13", "usableamount14", "usableamount15", "batchrule_show", "bomtype_show", "jyrhzdyw_show", "materclass_show", "materstate_show", "matertype_show", "outtype_show", "pchscscd_show", "pricemethod_show", "roadtype_show", "scheattr_show", "scscddms_show", "supplytype_show", "wghxcl_show", "pk_sealuser", "sealuserName", "sealdate", "sealflag", "iselementcheck", "fcpclgsfa", "fcpclgsfa_show", "isuseroute", "isctoutput", "usableamountbyfree0", "usableamountbyfree1", "usableamountbyfree2", "pk_corp", "dr" ,"pk_cargdoc"};//add by zwx 2016-1-20 
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("pk_calbody"))
      return this.m_pk_calbody;
    if (attributeName.equals("pk_produce"))
      return this.m_pk_produce;
    if (attributeName.equals("pk_invbasdoc"))
      return this.m_pk_invbasdoc;
    if (attributeName.equals("pk_invmandoc")) {
      return this.m_pk_invmandoc;
    }

    if (attributeName.equals("isused"))
      return this.m_isused;
    if (attributeName.equals("sfpchs"))
      return this.sfpchs;
    if (attributeName.equals("pk_stordoc"))
      return this.m_pk_stordoc;
    if (attributeName.equals("safetystocknum"))
      return this.m_safetystocknum;
    if (attributeName.equals("maxstornum"))
      return this.m_maxstornum;
    if (attributeName.equals("lowstocknum"))
      return this.m_lowstocknum;
    if (attributeName.equals("pricemethod"))
      return this.m_pricemethod;
    if (attributeName.equals("pricemethod_show"))
      return this.pricemethod_show;
    if (attributeName.equals("jhj"))
      return this.m_jhj;
    if (attributeName.equals("ckcb"))
      return this.m_ckcb;
    if (attributeName.equals("cksj"))
      return this.m_cksj;
    if (attributeName.equals("primnessnum"))
      return this.m_primnessnum;
    if (attributeName.equals("zdhd"))
      return this.m_zdhd;
    if (attributeName.equals("cgzz"))
      return this.m_cgzz;
    if (attributeName.equals("zgys"))
      return this.m_zgys;
    if (attributeName.equals("fgys"))
      return this.m_fgys;
    if (attributeName.equals("ybgys"))
      return this.m_ybgys;
    if (attributeName.equals("chngamount"))
      return this.chngAmount;
    if (attributeName.equals("gfwlbm"))
      return this.m_gfwlbm;
    if (attributeName.equals("abssales"))
      return this.abcsales;
    if (attributeName.equals("abcgrosspft"))
      return this.abcGrosspft;
    if (attributeName.equals("abcpurchase"))
      return this.abcPurchase;
    if (attributeName.equals("abcfundeg"))
      return this.abcFundeg;
    if (attributeName.equals("stocklowerdays"))
      return this.stocklowerdays;
    if (attributeName.equals("stockupperdays"))
      return this.stockupperdays;
    if (attributeName.equals("nbzyj")) {
      return this.nbzyj;
    }

    if (attributeName.equals("matertype"))
      return this.m_matertype;
    if (attributeName.equals("matertype_show"))
      return this.matertype_show;
    if (attributeName.equals("outtype"))
      return this.m_outtype;
    if (attributeName.equals("outtype_show"))
      return this.outtype_show;
    if (attributeName.equals("materstate"))
      return this.materstate;
    if (attributeName.equals("materstate_show"))
      return this.materstate_show;
    if (attributeName.equals("bomtype"))
      return this.bomtype;
    if (attributeName.equals("bomtype_show"))
      return this.bomtype_show;
    if (attributeName.equals("roadtype"))
      return this.roadType;
    if (attributeName.equals("roadtype_show"))
      return this.roadtype_show;
    if (attributeName.equals("materclass"))
      return this.materclass;
    if (attributeName.equals("materclass_show"))
      return this.materclass_show;
    if (attributeName.equals("supplytype"))
      return this.m_supplytype;
    if (attributeName.equals("supplytype_show"))
      return this.supplytype_show;
    if (attributeName.equals("iscreatesonprodorder"))
      return this.iscreatesonorder;
    if (attributeName.equals("producemethod"))
      return this.m_producemethod;
    if (attributeName.equals("issendbydatum"))
      return this.issendbydatum;
    if (attributeName.equals("datumofsend"))
      return this.datumofsend;
    if (attributeName.equals("virtualflag"))
      return this.m_virtualflag;
    if (attributeName.equals("combineflag"))
      return this.m_combineflag;
    if (attributeName.equals("primaryflag"))
      return this.m_primaryflag;
    if (attributeName.equals("sfzzcp"))
      return this.m_sfzzcp;
    if (attributeName.equals("sfcbdx"))
      return this.m_sfcbdx;
    if (attributeName.equals("sffzfw"))
      return this.sffzfw;
    if (attributeName.equals("iswholesetsend"))
      return this.iswholesetsend;
    if (attributeName.equals("issend"))
      return this.issend;
    if (attributeName.equals("converseflag"))
      return this.m_converseflag;
    if (attributeName.equals("chkfreeflag"))
      return this.chkFreeFlag;
    if (attributeName.equals("stockbycheck")) {
      return this.chkStockByCheck;
    }

    if (attributeName.equals("pk_deptdoc3"))
      return this.m_pk_deptdoc3;
    if (attributeName.equals("pk_psndoc3"))
      return this.m_pk_psndoc3;
    if (attributeName.equals("pk_psndoc4"))
      return this.m_pk_psndoc4;
    if (attributeName.equals("scheattr"))
      return this.m_scheattr;
    if (attributeName.equals("scheattr_show"))
      return this.scheattr_show;
    if (attributeName.equals("accquiretime"))
      return this.m_accquiretime;
    if (attributeName.equals("confirmtime"))
      return this.m_confirmtime;
    if (attributeName.equals("batchrule"))
      return this.m_batchrule;
    if (attributeName.equals("batchrule_show"))
      return this.batchrule_show;
    if (attributeName.equals("ecobatch"))
      return this.ecobatch;
    if (attributeName.equals("isctlbyfixperiod"))
      return this.isctlbyfixperiod;
    if (attributeName.equals("batchperiodnum"))
      return this.batchperiodnum;
    if (attributeName.equals("sumahead"))
      return this.m_sumahead;
    if (attributeName.equals("fixperiodbegin"))
      return this.fixperiodbegin;
    if (attributeName.equals("aheadbatch"))
      return this.m_aheadbatch;
    if (attributeName.equals("aheadcoff"))
      return this.m_aheadcoff;
    if (attributeName.equals("fixedahead"))
      return this.m_fixedahead;
    if (attributeName.equals("roundingnum"))
      return this.m_roundingnum;
    if (attributeName.equals("prevahead"))
      return this.m_prevahead;
    if (attributeName.equals("minmulnum"))
      return this.m_minmulnum;
    if (attributeName.equals("wasterrate"))
      return this.m_wasterrate;
    if (attributeName.equals("endahead"))
      return this.m_endahead;
    if (attributeName.equals("lowlevelcode")) {
      return this.m_lowlevelcode;
    }

    if (attributeName.equals("usableamount"))
      return this.usableAmount;
    if (attributeName.equals("usableamountbyfree")) {
      return this.usableamountbyfree;
    }

    if (attributeName.equals("flanlennum"))
      return this.m_flanlennum;
    if (attributeName.equals("flanwidenum"))
      return this.m_flanwidenum;
    if (attributeName.equals("flanmadenum"))
      return this.m_flanmadenum;
    if (attributeName.equals("rationwtnum"))
      return this.m_rationwtnum;
    if (attributeName.equals("grosswtnum"))
      return this.m_grosswtnum;
    if (attributeName.equals("netwtnum")) {
      return this.m_netwtnum;
    }

    if (attributeName.equals("isctlbyprimarycode"))
      return this.isctlbyprimarycode;
    if (attributeName.equals("isctlbysecondarycode")) {
      return this.isctlbysecondarycode;
    }

    if (attributeName.equals("def1"))
      return this.def1;
    if (attributeName.equals("def2"))
      return this.def2;
    if (attributeName.equals("def3"))
      return this.def3;
    if (attributeName.equals("def4"))
      return this.def4;
    if (attributeName.equals("def5")) {
      return this.def5;
    }

    if (attributeName.equals("iscostbyorder"))
      return this.iscostbyorder;
    if (attributeName.equals("isfxjz"))
      return this.isfxjz;
    if (attributeName.equals("pchscscd"))
      return this.pchscscd;
    if (attributeName.equals("pchscscd_show"))
      return this.pchscscd_show;
    if (attributeName.equals("pk_rkjlcid"))
      return this.pk_rkjlcid;
    if (attributeName.equals("pk_ckjlcid"))
      return this.pk_ckjlcid;
    if (attributeName.equals("wghxcl"))
      return this.wghxcl;
    if (attributeName.equals("wghxcl_show"))
      return this.wghxcl_show;
    if (attributeName.equals("scscddms"))
      return this.scscddms;
    if (attributeName.equals("scscddms_show"))
      return this.scscddms_show;
    if (attributeName.equals("nyzbmxs"))
      return this.nyzbmxs;
    if (attributeName.equals("jyrhzdyw"))
      return this.jyrhzdyw;
    if (attributeName.equals("jyrhzdyw_show"))
      return this.jyrhzdyw_show;
    if (attributeName.equals("sfzb"))
      return this.sfzb;
    if (attributeName.equals("scxybzsfzk"))
      return this.scxybzsfzk;
    if (attributeName.equals("wggdsczkxs"))
      return this.wggdsczkxs;
    if (attributeName.equals("blgdsczkxs"))
      return this.blgdsczkxs;
    if (attributeName.equals("zbxs"))
      return this.zbxs;
    if (attributeName.equals("zbczjyxm")) {
      return this.zbczjyxm;
    }

    if (attributeName.equals("outnumhistorydays"))
      return this.outnumhistorydays;
    if (attributeName.equals("sfrpc"))
      return (this.m_producemethod == null) || (this.m_producemethod.intValue() == 0) ? new UFBoolean(false) : new UFBoolean(true);
    if (attributeName.startsWith("usableamountbyfree")) {
      String xh = attributeName.substring("usableamountbyfree".length());
      int idx = Integer.parseInt(xh);
      return new UFBoolean(getUsableamountbyfree().charAt(idx));
    }if ((attributeName.startsWith("usableamount")) && ((attributeName.length() == 13) || (attributeName.length() == 14))) {
      String xh = attributeName.substring("usableamount".length());
      int idx = Integer.parseInt(xh);
      return new UFBoolean(getUsableAmount().charAt(idx) == '1');
    }if (attributeName.equals("pk_sealuser"))
      return this.pk_sealuser;
    if (attributeName.equals("sealuserName"))
      return this.sealuserName;
    if (attributeName.equals("sealdate"))
      return this.sealdate;
    if (attributeName.equals("sealflag"))
      return this.sealflag;
    if (attributeName.equals("iselementcheck"))
      return this.iselementcheck;
    if (attributeName.equals("fcpclgsfa"))
      return this.fcpclgsfa;
    if (attributeName.equals("fcpclgsfa_show"))
      return this.fcpclgsfa_show;
    if (attributeName.equals("isuseroute"))
      return this.isuseroute;
    if (attributeName.equals("isctoutput")) {
      return this.isctoutput;
    }
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("dr")) {
      return this.dr;
    }
    //add by zwx 2016-1-20 
    if (attributeName.equals("pk_cargdoc")) {
        return this.pk_cargdoc;
      }
    return null;
  }

  public BatchlistVO[] getBatchlist()
  {
    return this.batchlist;
  }

  public Integer getBatchperiodnum()
  {
    return this.batchperiodnum;
  }

  public String getBatchrule_show()
  {
    return this.batchrule_show;
  }

  public UFDouble getBlgdsczkxs()
  {
    return this.blgdsczkxs;
  }

  public Integer getBomtype()
  {
    return this.bomtype;
  }

  public String getBomtype_show()
  {
    return this.bomtype_show;
  }

  public UFBoolean getChkFreeFlag()
  {
    return this.chkFreeFlag;
  }

  public UFDouble getChngAmount()
  {
    return this.chngAmount;
  }

  public UFDouble getCurrentAmount()
  {
    return this.currentAmount;
  }

  public UFDouble getDatumofsend()
  {
    return this.datumofsend;
  }

  public String getDef1()
  {
    return this.def1;
  }

  public String getDef2()
  {
    return this.def2;
  }

  public String getDef3()
  {
    return this.def3;
  }

  public String getDef4()
  {
    return this.def4;
  }

  public String getDef5()
  {
    return this.def5;
  }

  public String getDefValue(int index)
  {
    switch (index) {
    case 1:
      return getDef1();
    case 2:
      return getDef2();
    case 3:
      return getDef3();
    case 4:
      return getDef4();
    case 5:
      return getDef5();
    }
    return null;
  }

  public UFDouble getEcobatch()
  {
    return this.ecobatch;
  }

  public UFDate getFixperiodbegin()
  {
    return this.fixperiodbegin;
  }

  public UFBoolean getIscostbyorder()
  {
    return this.iscostbyorder;
  }

  public UFBoolean getIscreatesonorder()
  {
    return this.iscreatesonorder;
  }

  public UFBoolean getIsctlbyfixperiod()
  {
    return this.isctlbyfixperiod;
  }

  public UFBoolean getIsctlbyprimarycode()
  {
    return this.isctlbyprimarycode;
  }

  public UFBoolean getIsctlbysecondarycode()
  {
    return this.isctlbysecondarycode;
  }

  public UFBoolean getIsfxjz()
  {
    return this.isfxjz;
  }

  public UFBoolean getIssend()
  {
    return this.issend;
  }

  public UFBoolean getIssendbydatum()
  {
    return this.issendbydatum;
  }

  public UFBoolean getIsused()
  {
    return this.m_isused;
  }

  public UFBoolean getIswholesetsend()
  {
    return this.iswholesetsend;
  }

  public Integer getJyrhzdyw()
  {
    return this.jyrhzdyw;
  }

  public String getJyrhzdyw_show()
  {
    return this.jyrhzdyw_show;
  }

  public Integer getMaterclass()
  {
    return this.materclass;
  }

  public String getMaterclass_show()
  {
    return this.materclass_show;
  }

  public Integer getMaterstate()
  {
    return this.materstate;
  }

  public String getMaterstate_show()
  {
    return this.materstate_show;
  }

  public String getMatertype_show()
  {
    return this.matertype_show;
  }

  public UFDouble getNbzyj()
  {
    return this.nbzyj;
  }

  public UFDouble getNyzbmxs()
  {
    return this.nyzbmxs;
  }

  public Integer getOutnumhistorydays()
  {
    return this.outnumhistorydays;
  }

  public String getOuttype_show()
  {
    return this.outtype_show;
  }

  public Integer getPchscscd()
  {
    return this.pchscscd;
  }

  public String getPchscscd_show()
  {
    return this.pchscscd_show;
  }

  public String getPk_ckjlcid()
  {
    return this.pk_ckjlcid;
  }

  public String getPk_rkjlcid()
  {
    return this.pk_rkjlcid;
  }

  public String getPricemethod_show()
  {
    return this.pricemethod_show;
  }

  public UFDouble getRealUsableAmount()
  {
    return this.realUsableAmount;
  }

  public Integer getRoadType()
  {
    return this.roadType;
  }

  public String getRoadtype_show()
  {
    return this.roadtype_show;
  }

  public String getScheattr_show()
  {
    return this.scheattr_show;
  }

  public Integer getScscddms()
  {
    return this.scscddms;
  }

  public String getScscddms_show()
  {
    return this.scscddms_show;
  }

  public UFBoolean getScxybzsfzk()
  {
    return this.scxybzsfzk;
  }

  public UFBoolean getSffzfw()
  {
    return this.sffzfw;
  }

  public UFBoolean getSfpchs()
  {
    return this.sfpchs;
  }

  public UFBoolean getSfzb()
  {
    return this.sfzb;
  }

  public UFBoolean getStockByCheck()
  {
    return this.chkStockByCheck;
  }

  public Integer getStocklowerdays()
  {
    return this.stocklowerdays;
  }

  public Integer getStockupperdays()
  {
    return this.stockupperdays;
  }

  public String getSupplytype_show()
  {
    return this.supplytype_show;
  }

  public String getUsableAmount()
  {
    if ((this.usableAmount == null) || (this.usableAmount.length() != 16)) {
      this.usableAmount = "1111111111111111";
    }
    return this.usableAmount;
  }

  public String getUsableamountbyfree()
  {
    if ((this.usableamountbyfree == null) || (this.usableamountbyfree.length() != 3)) {
      this.usableamountbyfree = "NNN";
    }
    return this.usableamountbyfree;
  }

  public UFDouble getWggdsczkxs()
  {
    return this.wggdsczkxs;
  }

  public Integer getWghxcl()
  {
    return this.wghxcl;
  }

  public String getWghxcl_show()
  {
    return this.wghxcl_show;
  }

  public String getZbczjyxm()
  {
    return this.zbczjyxm;
  }

  public UFDouble getZbxs()
  {
    return this.zbxs;
  }

  public void setAbcfl(String newAbcfl)
  {
    this.m_abcfl = newAbcfl;
  }

  public void setAbcFundeg(String newAbcFundeg)
  {
    this.abcFundeg = newAbcFundeg;
  }

  public void setAbcGrosspft(String newAbcGrosspft)
  {
    this.abcGrosspft = newAbcGrosspft;
  }

  public void setAbcPurchase(String newAbcPurchase)
  {
    this.abcPurchase = newAbcPurchase;
  }

  public void setAbcsales(String newAbcsales)
  {
    this.abcsales = newAbcsales;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("pk_calbody")) {
        this.m_pk_calbody = ((String)value);
      } else if (name.equals("pk_produce")) {
        this.m_pk_produce = ((String)value);
      } else if (name.equals("pk_invbasdoc")) {
        this.m_pk_invbasdoc = ((String)value);
      } else if (name.equals("pk_invmandoc")) {
        this.m_pk_invmandoc = ((String)value);
      }
      else if (name.equals("isused")) {
        this.m_isused = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("sfpchs")) {
        this.sfpchs = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("pk_stordoc")) {
        this.m_pk_stordoc = ((String)value);
      } else if (name.equals("safetystocknum")) {
        this.m_safetystocknum = ((UFDouble)value);
      } else if (name.equals("maxstornum")) {
        this.m_maxstornum = ((UFDouble)value);
      } else if (name.equals("lowstocknum")) {
        this.m_lowstocknum = ((UFDouble)value);
      } else if (name.equals("pricemethod")) {
        this.m_pricemethod = ((Integer)value);
      } else if (name.equals("pricemethod_show")) {
        this.pricemethod_show = ((String)value);
      } else if (name.equals("jhj")) {
        this.m_jhj = ((UFDouble)value);
      } else if (name.equals("ckcb")) {
        this.m_ckcb = ((UFDouble)value);
      } else if (name.equals("cksj")) {
        this.m_cksj = ((UFDouble)value);
      } else if (name.equals("primnessnum")) {
        this.m_primnessnum = ((UFDouble)value);
      } else if (name.equals("zdhd")) {
        this.m_zdhd = ((UFDouble)value);
      } else if (name.equals("cgzz")) {
        this.m_cgzz = ((String)value);
      } else if (name.equals("zgys")) {
        this.m_zgys = ((String)value);
      } else if (name.equals("fgys")) {
        this.m_fgys = ((String)value);
      } else if (name.equals("ybgys")) {
        this.m_ybgys = ((String)value);
      } else if (name.equals("chngamount")) {
        this.chngAmount = ((UFDouble)value);
      } else if (name.equals("gfwlbm")) {
        this.m_gfwlbm = ((String)value);
      } else if (name.equals("abssales")) {
        this.abcsales = ((String)value);
      } else if (name.equals("abcgrosspft")) {
        this.abcGrosspft = ((String)value);
      } else if (name.equals("abcpurchase")) {
        this.abcPurchase = ((String)value);
      } else if (name.equals("abcfundeg")) {
        this.abcFundeg = ((String)value);
      } else if (name.equals("stocklowerdays")) {
        this.stocklowerdays = ((Integer)value);
      } else if (name.equals("stockupperdays")) {
        this.stockupperdays = ((Integer)value);
      } else if (name.equals("nbzyj")) {
        this.nbzyj = ((UFDouble)value);
      }
      else if (name.equals("matertype")) {
        this.m_matertype = ((String)value);
      } else if (name.equals("matertype_show")) {
        this.matertype_show = ((String)value);
      } else if (name.equals("outtype")) {
        this.m_outtype = ((String)value);
      } else if (name.equals("outtype_show")) {
        this.outtype_show = ((String)value);
      } else if (name.equals("materstate")) {
        this.materstate = ((Integer)value);
      } else if (name.equals("materstate_show")) {
        this.materstate_show = ((String)value);
      } else if (name.equals("bomtype")) {
        this.bomtype = ((Integer)value);
      } else if (name.equals("bomtype_show")) {
        this.bomtype_show = ((String)value);
      } else if (name.equals("roadtype")) {
        this.roadType = ((Integer)value);
      } else if (name.equals("roadtype_show")) {
        this.roadtype_show = ((String)value);
      } else if (name.equals("materclass")) {
        this.materclass = ((Integer)value);
      } else if (name.equals("materclass_show")) {
        this.materclass_show = ((String)value);
      } else if (name.equals("supplytype")) {
        this.m_supplytype = ((String)value);
      } else if (name.equals("supplytype_show")) {
        this.supplytype_show = ((String)value);
      } else if (name.equals("iscreatesonprodorder")) {
        this.iscreatesonorder = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("producemethod")) {
        this.m_producemethod = ((value == null) || (value.toString().equalsIgnoreCase("N")) || (value.toString().equalsIgnoreCase("false")) ? new Integer(0) : new Integer(1));
      } else if (name.equals("issendbydatum")) {
        this.issendbydatum = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("datumofsend")) {
        this.datumofsend = ((UFDouble)value);
      } else if (name.equals("virtualflag")) {
        this.m_virtualflag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("combineflag")) {
        this.m_combineflag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("primaryflag")) {
        this.m_primaryflag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("sfzzcp")) {
        this.m_sfzzcp = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("sfcbdx")) {
        this.m_sfcbdx = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("sffzfw")) {
        this.sffzfw = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("iswholesetsend")) {
        this.iswholesetsend = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("issend")) {
        this.issend = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("converseflag")) {
        this.m_converseflag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("chkfreeflag")) {
        this.chkFreeFlag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("stockbycheck")) {
        this.chkStockByCheck = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      }
      else if (name.equals("pk_deptdoc3")) {
        this.m_pk_deptdoc3 = ((String)value);
      } else if (name.equals("pk_psndoc3")) {
        this.m_pk_psndoc3 = ((String)value);
      } else if (name.equals("pk_psndoc4")) {
        this.m_pk_psndoc4 = ((String)value);
      } else if (name.equals("scheattr")) {
        this.m_scheattr = ((String)value);
      } else if (name.equals("scheattr_show")) {
        this.scheattr_show = ((String)value);
      } else if (name.equals("accquiretime")) {
        this.m_accquiretime = ((UFDouble)value);
      } else if (name.equals("confirmtime")) {
        this.m_confirmtime = ((UFDouble)value);
      } else if (name.equals("batchrule")) {
        this.m_batchrule = ((String)value);
      } else if (name.equals("batchrule_show")) {
        this.batchrule_show = ((String)value);
      } else if (name.equals("ecobatch")) {
        this.ecobatch = ((UFDouble)value);
      } else if (name.equals("isctlbyfixperiod")) {
        this.isctlbyfixperiod = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("batchperiodnum")) {
        this.batchperiodnum = ((Integer)value);
      } else if (name.equals("sumahead")) {
        this.m_sumahead = ((UFDouble)value);
      } else if (name.equals("fixperiodbegin")) {
        this.fixperiodbegin = ((UFDate)value);
      } else if (name.equals("aheadbatch")) {
        this.m_aheadbatch = ((UFDouble)value);
      } else if (name.equals("aheadcoff")) {
        this.m_aheadcoff = ((UFDouble)value);
      } else if (name.equals("fixedahead")) {
        this.m_fixedahead = ((UFDouble)value);
      } else if (name.equals("roundingnum")) {
        this.m_roundingnum = ((UFDouble)value);
      } else if (name.equals("prevahead")) {
        this.m_prevahead = ((UFDouble)value);
      } else if (name.equals("minmulnum")) {
        this.m_minmulnum = ((UFDouble)value);
      } else if (name.equals("wasterrate")) {
        this.m_wasterrate = ((UFDouble)value);
      } else if (name.equals("endahead")) {
        this.m_endahead = ((UFDouble)value);
      } else if (name.equals("lowlevelcode")) {
        this.m_lowlevelcode = ((Integer)value);
      }
      else if (name.equals("usableamount")) {
        this.usableAmount = ((String)value);
      } else if (name.equals("usableamountbyfree")) {
        this.usableamountbyfree = ((String)value);
      }
      else if (name.equals("flanlennum")) {
        this.m_flanlennum = ((UFDouble)value);
      } else if (name.equals("flanwidenum")) {
        this.m_flanwidenum = ((UFDouble)value);
      } else if (name.equals("flanmadenum")) {
        this.m_flanmadenum = ((Integer)value);
      } else if (name.equals("rationwtnum")) {
        this.m_rationwtnum = ((UFDouble)value);
      } else if (name.equals("grosswtnum")) {
        this.m_grosswtnum = ((UFDouble)value);
      } else if (name.equals("netwtnum")) {
        this.m_netwtnum = ((UFDouble)value);
      }
      else if (name.equals("isctlbyprimarycode")) {
        this.isctlbyprimarycode = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("isctlbysecondarycode")) {
        this.isctlbysecondarycode = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      }
      else if (name.equals("def1")) {
        this.def1 = (value == null ? null : value.toString());
      } else if (name.equals("def2")) {
        this.def2 = (value == null ? null : value.toString());
      } else if (name.equals("def3")) {
        this.def3 = (value == null ? null : value.toString());
      } else if (name.equals("def4")) {
        this.def4 = (value == null ? null : value.toString());
      } else if (name.equals("def5")) {
        this.def5 = (value == null ? null : value.toString());
      }
      else if (name.equals("iscostbyorder")) {
        this.iscostbyorder = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("isfxjz")) {
        this.isfxjz = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("pchscscd")) {
        this.pchscscd = ((Integer)value);
      } else if (name.equals("pchscscd_show")) {
        this.pchscscd_show = ((String)value);
      } else if (name.equals("pk_rkjlcid")) {
        this.pk_rkjlcid = ((String)value);
      } else if (name.equals("pk_ckjlcid")) {
        this.pk_ckjlcid = ((String)value);
      } else if (name.equals("wghxcl")) {
        this.wghxcl = ((Integer)value);
      } else if (name.equals("wghxcl_show")) {
        this.wghxcl_show = ((String)value);
      } else if (name.equals("scscddms")) {
        this.scscddms = ((Integer)value);
      } else if (name.equals("scscddms_show")) {
        this.scscddms_show = ((String)value);
      } else if (name.equals("nyzbmxs")) {
        this.nyzbmxs = ((UFDouble)value);
      } else if (name.equals("jyrhzdyw")) {
        this.jyrhzdyw = ((Integer)value);
      } else if (name.equals("jyrhzdyw_show")) {
        this.jyrhzdyw_show = ((String)value);
      } else if (name.equals("sfzb")) {
        this.sfzb = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("scxybzsfzk")) {
        this.scxybzsfzk = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("wggdsczkxs")) {
        this.wggdsczkxs = ((UFDouble)value);
      } else if (name.equals("blgdsczkxs")) {
        this.blgdsczkxs = ((UFDouble)value);
      } else if (name.equals("zbxs")) {
        this.zbxs = ((UFDouble)value);
      } else if (name.equals("zbczjyxm")) {
        this.zbczjyxm = ((String)value);
      }
      else if (name.equals("outnumhistorydays")) {
        this.outnumhistorydays = ((Integer)value);
      } else if (name.equals("sfrpc")) {
        this.m_producemethod = ((value == null) || (value.toString().equalsIgnoreCase("N")) || (value.toString().equalsIgnoreCase("false")) ? new Integer(0) : new Integer(1));
      } else if (name.startsWith("usableamountbyfree")) {
        String xh = name.substring("usableamountbyfree".length());
        int idx = Integer.parseInt(xh);
        boolean bValue = value == null ? false : ((UFBoolean)value).booleanValue();
        StringBuffer buffer = new StringBuffer(getUsableamountbyfree());
        buffer.replace(idx, idx + 1, bValue ? "Y" : "N");
        setUsableamountbyfree(buffer.toString());
      } else if ((name.startsWith("usableamount")) && ((name.length() == 13) || (name.length() == 14))) {
        String xh = name.substring("usableamount".length());
        int idx = Integer.parseInt(xh);
        boolean bValue = value == null ? false : ((UFBoolean)value).booleanValue();
        StringBuffer buffer = new StringBuffer(getUsableAmount());
        buffer.replace(idx, idx + 1, bValue ? "1" : "0");
        setUsableAmount(buffer.toString());
      } else if (name.equals("pk_sealuser")) {
        this.pk_sealuser = ((String)value);
      } else if (name.equals("sealuserName")) {
        this.sealuserName = ((String)value);
      } else if (name.equals("sealdate")) {
        this.sealdate = ((UFDate)value);
      } else if (name.equals("sealflag")) {
        this.sealflag = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("iselementcheck")) {
        this.iselementcheck = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("fcpclgsfa")) {
        this.fcpclgsfa = ((Integer)value);
      } else if (name.equals("fcpclgsfa_show")) {
        this.fcpclgsfa_show = ((String)value);
      } else if (name.equals("isuseroute")) {
        this.isuseroute = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      } else if (name.equals("isctoutput")) {
        this.isctoutput = (value == null ? new UFBoolean(false) : (UFBoolean)value);
      }
      else if (name.equals("pk_corp")) {
        this.m_pk_corp = ((String)value);
      } else if (name.equals("dr")) {
        this.dr = ((Integer)value);
      }
      //add by zwx 2016-1-20 
      else if (name.equals("pk_cargdoc")) {
          this.pk_cargdoc = ((String)value);
      } 
      

    }
    catch (ClassCastException e)
    {
      throw new ClassCastException(value + "->" + name);
    }
  }

  public void setBatchlist(BatchlistVO[] newBatchlist)
  {
    this.batchlist = newBatchlist;
  }

  public void setBatchperiodnum(Integer newBatchperiodnum)
  {
    this.batchperiodnum = newBatchperiodnum;
  }

  public void setBatchrule_show(String newBatchrule_show)
  {
    this.batchrule_show = newBatchrule_show;
  }

  public void setBlgdsczkxs(UFDouble newBlgdsczkxs)
  {
    this.blgdsczkxs = newBlgdsczkxs;
  }

  public void setBomtype(Integer newBomtype)
  {
    this.bomtype = newBomtype;
  }

  public void setBomtype_show(String newBomtype_show)
  {
    this.bomtype_show = newBomtype_show;
  }

  public void setChkFreeFlag(UFBoolean newChkFreeFlag)
  {
    this.chkFreeFlag = newChkFreeFlag;
  }

  public void setChngAmount(UFDouble newChngAmount)
  {
    this.chngAmount = newChngAmount;
  }

  public void setCurrentAmount(UFDouble newCurrentAmount)
  {
    this.currentAmount = newCurrentAmount;
  }

  public void setDatumofsend(UFDouble newDatumofsend)
  {
    this.datumofsend = newDatumofsend;
  }

  public void setDef1(String newDef1)
  {
    this.def1 = newDef1;
  }

  public void setDef2(String newDef2)
  {
    this.def2 = newDef2;
  }

  public void setDef3(String newDef3)
  {
    this.def3 = newDef3;
  }

  public void setDef4(String newDef4)
  {
    this.def4 = newDef4;
  }

  public void setDef5(String newDef5)
  {
    this.def5 = newDef5;
  }

  public void setDefValue(String value, int index)
  {
    switch (index) {
    case 1:
      setDef1(value);
      break;
    case 2:
      setDef2(value);
      break;
    case 3:
      setDef3(value);
      break;
    case 4:
      setDef4(value);
      break;
    case 5:
      setDef5(value);
    }
  }

  public void setEcobatch(UFDouble newEcobatch)
  {
    this.ecobatch = newEcobatch;
  }

  public void setFixperiodbegin(UFDate newFixperiodbegin)
  {
    this.fixperiodbegin = newFixperiodbegin;
  }

  public void setIscostbyorder(UFBoolean newIscostbyorder)
  {
    this.iscostbyorder = newIscostbyorder;
  }

  public void setIscreatesonorder(UFBoolean newIscreatesonorder)
  {
    this.iscreatesonorder = newIscreatesonorder;
  }

  public void setIsctlbyfixperiod(UFBoolean newIsctlbyfixperiod)
  {
    this.isctlbyfixperiod = newIsctlbyfixperiod;
  }

  public void setIsctlbyprimarycode(UFBoolean newIsctlbyprimarycode)
  {
    this.isctlbyprimarycode = newIsctlbyprimarycode;
  }

  public void setIsctlbysecondarycode(UFBoolean newIsctlbysecondarycode)
  {
    this.isctlbysecondarycode = newIsctlbysecondarycode;
  }

  public void setIsfxjz(UFBoolean newIsfxjz)
  {
    this.isfxjz = newIsfxjz;
  }

  public void setIssend(UFBoolean newIssend)
  {
    this.issend = newIssend;
  }

  public void setIssendbydatum(UFBoolean newIssendbydatum)
  {
    this.issendbydatum = newIssendbydatum;
  }

  public void setIsused(UFBoolean newM_isused)
  {
    this.m_isused = newM_isused;
  }

  public void setIswholesetsend(UFBoolean newIswholesetsend)
  {
    this.iswholesetsend = newIswholesetsend;
  }

  public void setJyrhzdyw(Integer newJyrhzdyw)
  {
    this.jyrhzdyw = newJyrhzdyw;
  }

  public void setJyrhzdyw_show(String newJyrhzdyw_show)
  {
    this.jyrhzdyw_show = newJyrhzdyw_show;
  }

  public void setMaterclass(Integer newMaterclass)
  {
    this.materclass = newMaterclass;
  }

  public void setMaterclass_show(String newMaterclass_show)
  {
    this.materclass_show = newMaterclass_show;
  }

  public void setMaterstate(Integer newMaterstate)
  {
    this.materstate = newMaterstate;
  }

  public void setMaterstate_show(String newMaterstate_show)
  {
    this.materstate_show = newMaterstate_show;
  }

  public void setMatertype_show(String newMatertype_show)
  {
    this.matertype_show = newMatertype_show;
  }

  public void setNbzyj(UFDouble newNbzyj)
  {
    this.nbzyj = newNbzyj;
  }

  public void setNyzbmxs(UFDouble newNyzbmxs)
  {
    this.nyzbmxs = newNyzbmxs;
  }

  public void setOutnumhistorydays(Integer newOutnumhistorydays)
  {
    this.outnumhistorydays = newOutnumhistorydays;
  }

  public void setOuttype_show(String newOuttype_show)
  {
    this.outtype_show = newOuttype_show;
  }

  public void setPchscscd(Integer newPchscscd)
  {
    this.pchscscd = newPchscscd;
  }

  public void setPchscscd_show(String newPchscscd_show)
  {
    this.pchscscd_show = newPchscscd_show;
  }

  public void setPk_ckjlcid(String newPk_ckjlcid)
  {
    this.pk_ckjlcid = newPk_ckjlcid;
  }

  public void setPk_rkjlcid(String newPk_rkjlcid)
  {
    this.pk_rkjlcid = newPk_rkjlcid;
  }

  public void setPricemethod_show(String newPricemethod_show)
  {
    this.pricemethod_show = newPricemethod_show;
  }

  public void setRealUsableAmount(UFDouble newRealUsableAmount)
  {
    this.realUsableAmount = newRealUsableAmount;
  }

  public void setRoadType(Integer newRoadType)
  {
    this.roadType = newRoadType;
  }

  public void setRoadtype_show(String newRoadtype_show)
  {
    this.roadtype_show = newRoadtype_show;
  }

  public void setScheattr_show(String newScheattr_show)
  {
    this.scheattr_show = newScheattr_show;
  }

  public void setScscddms(Integer newScscddms)
  {
    this.scscddms = newScscddms;
  }

  public void setScscddms_show(String newScscddms_show)
  {
    this.scscddms_show = newScscddms_show;
  }

  public void setScxybzsfzk(UFBoolean newScxybzsfzk)
  {
    this.scxybzsfzk = newScxybzsfzk;
  }

  public void setSffzfw(UFBoolean newSffzfw)
  {
    this.sffzfw = newSffzfw;
  }

  public void setSfpchs(UFBoolean newSfpchs)
  {
    this.sfpchs = newSfpchs;
  }

  public void setSfzb(UFBoolean newSfzb)
  {
    this.sfzb = newSfzb;
  }

  public void setStockByCheck(UFBoolean newChkStockByCheck)
  {
    this.chkStockByCheck = newChkStockByCheck;
  }

  public void setStocklowerdays(Integer newStocklowerdays)
  {
    this.stocklowerdays = newStocklowerdays;
  }

  public void setStockupperdays(Integer newStockupperdays)
  {
    this.stockupperdays = newStockupperdays;
  }

  public void setSupplytype_show(String newSupplytype_show)
  {
    this.supplytype_show = newSupplytype_show;
  }

  public void setUsableAmount(String newUsableAmount)
  {
    this.usableAmount = newUsableAmount;
  }

  public void setUsableamountbyfree(String newUsableamountbyfree)
  {
    this.usableamountbyfree = newUsableamountbyfree;
  }

  public void setWggdsczkxs(UFDouble newWggdsczkxs)
  {
    this.wggdsczkxs = newWggdsczkxs;
  }

  public void setWghxcl(Integer newWghxcl)
  {
    this.wghxcl = newWghxcl;
  }

  public void setWghxcl_show(String newWghxcl_show)
  {
    this.wghxcl_show = newWghxcl_show;
  }

  public void setZbczjyxm(String newZbczjyxm)
  {
    this.zbczjyxm = newZbczjyxm;
  }

  public void setZbxs(UFDouble newZbxs)
  {
    this.zbxs = newZbxs;
  }

  public String getPk_sealuser()
  {
    return this.pk_sealuser;
  }

  public void setPk_sealuser(String pk_sealuser)
  {
    this.pk_sealuser = pk_sealuser;
  }

  public UFDate getSealdate()
  {
    return this.sealdate;
  }

  public void setSealdate(UFDate sealdate)
  {
    this.sealdate = sealdate;
  }

  public UFBoolean getSealflag()
  {
    return this.sealflag;
  }

  public void setSealflag(UFBoolean sealflag)
  {
    this.sealflag = sealflag;
  }

  public String getSealuserName()
  {
    return this.sealuserName;
  }

  public void setSealuserName(String sealuserName)
  {
    this.sealuserName = sealuserName;
  }

  public UFBoolean getIselementcheck()
  {
    return this.iselementcheck;
  }

  public void setIselementcheck(UFBoolean iselementcheck)
  {
    this.iselementcheck = iselementcheck;
  }

  public Integer getFcpclgsfa() {
    return this.fcpclgsfa;
  }

  public void setFcpclgsfa(Integer fcpclgsfa) {
    this.fcpclgsfa = fcpclgsfa;
  }

  public UFBoolean getIsctoutput() {
    return this.isctoutput;
  }

  public void setIsctoutput(UFBoolean isctoutput) {
    this.isctoutput = isctoutput;
  }

  public UFBoolean getIsuseroute() {
    return this.isuseroute;
  }

  public void setIsuseroute(UFBoolean isuseroute) {
    this.isuseroute = isuseroute;
  }

  public String getFcpclgsfa_show() {
    return this.fcpclgsfa_show;
  }

  public void setFcpclgsfa_show(String fcpclgsfa_show) {
    this.fcpclgsfa_show = fcpclgsfa_show;
  }

  public void initDefaultValue(GeneralExVO defaultValueVO)
  {
    if ((defaultValueVO == null) || (defaultValueVO.getParentVO() == null)) {
      return;
    }
    GeneralSuperVO mainVO = (GeneralSuperVO)defaultValueVO.getParentVO();

    String[] changeFields = null;

    changeFields = mainVO.getAttributeNames();
    try
    {
      if (changeFields != null)
        for (int i = 0; i < changeFields.length; i++)
        {
          setAttributeValue(changeFields[i], mainVO.getAttributeValue(changeFields[i]));
        }
    }
    catch (Exception ex)
    {
      System.out.println("为ProduceVO赋默认值时出错：" + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public Integer getDr()
  {
    return this.dr;
  }

  public void setDr(Integer dr) {
    this.dr = dr;
  }
  
  //add by zwx 2016-1-20 
  public String getPk_cargdoc() {
	return pk_cargdoc;
  }

  public void setPk_cargdoc(String pk_cargdoc) {
	this.pk_cargdoc = pk_cargdoc;
  }
  //end by zwx
}