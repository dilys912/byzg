package nc.vo.by.invapp.h0h002;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 * ��������:2014-07-08 21:29:57
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class PRODUCEVO extends SuperVO {
	private String pk_corp;
	private UFDateTime ts;
	private String pk_ckjlcid;
	private UFBoolean abssales;
	private UFDouble zdhd;
	private UFBoolean virtualflag;
	private UFBoolean stockbycheck;
	private UFDouble jhj;
	private UFDouble scscddms;
	private UFDouble safetystocknum;
	private UFDouble wghxcl;
	private UFBoolean sfcbdx;
	private UFBoolean primaryflag;
	private UFDouble nbzyj;
	private UFDouble outnumhistorydays;
	private UFDouble fixedahead;
	private String def2;
	private UFBoolean isctoutput;
	private String batchrule;
	private String def1;
	private UFDouble endahead;
	private UFDouble flanwidenum;
	private String pk_invbasdoc;
	private UFDouble cksj;
	private UFBoolean sffzfw;
	private UFDouble currentamount;
	private String matertype;
	private UFBoolean iscancalculatedinvcost;
	private UFDouble scpl;
	private UFDouble batchnum;
	private String outtype;
	private String usableamount;
	private UFBoolean isfatherofbom;
	private String zbczjyxm;
	private UFDouble stockupperdays;
	private UFDouble accquiretime;
	private UFDouble nyzbmxs;
	private UFDouble roadtype;
	private UFDouble grosswtnum;
	private String scheattr;
	private UFDouble wggdsczkxs;
	private UFBoolean chkfreeflag;
	private UFDouble blgdsczkxs;
	private UFDouble stocklowerdays;
	private UFDouble datumofsend;
	private UFBoolean combineflag;
	private UFBoolean abcgrosspft;
	private UFBoolean iscostbyorder;
	private String ybgys;
	private UFDouble lowlevelcode;
	private UFDouble materclass;
	private UFBoolean scxybzsfzk;
	private String pk_produce;
	private UFDouble chngamount;
	private String pk_sealuser;
	private UFDouble netwtnum;
	private UFDouble zbxs;
	private UFDouble ckcb;
	private UFBoolean isfxjz;
	private UFBoolean isctlbyfixperiod;
	private String pk_rkjlcid;
	private UFDouble materstate;
	private UFDate sealdate;
	private UFDouble minmulnum;
	private UFBoolean isctlbyprimarycode;
	private UFDouble realusableamount;
	private String zgys;
	private UFBoolean iswholesetsend;
	private String usableamountbyfree;
	private String fgys;
	private UFDouble fcpclgsfa;
	private UFDouble batchperiodnum;
	private UFDouble aheadbatch;
	private UFDateTime modifytime;
	private UFBoolean sealflag;
	private UFDouble confirmtime;
	private UFDouble pchscscd;
	private UFBoolean sfscx;
	private UFBoolean iselementcheck;
	private UFBoolean abcpurchase;
	private String pk_calbody;
	private UFDouble flanmadenum;
	private UFDouble flanlennum;
	private UFBoolean converseflag;
	private UFBoolean abcfundeg;
	private UFDouble batchincrnum;
	private UFDateTime createtime;
	private String pk_psndoc3;
	private UFDouble sumahead;
	private String cgzz;
	private String pk_deptdoc3;
	private UFDouble primnessnum;
	private UFDouble prevahead;
	private UFDouble roundingnum;
	private UFDouble aheadcoff;
	private UFBoolean isused;
	private UFDouble lowstocknum;
	private UFBoolean issend;
	private UFDouble minbatchnum;
	private String def5;
	private UFBoolean abcfl;
	private UFDouble bomtype;
	private String pk_psndoc4;
	private UFBoolean iscreatesonprodorder;
	private UFBoolean sfbj;
	private String gfwlbm;
	private UFDouble wasterrate;
	private UFBoolean sfzb;
	private UFDouble ecobatch;
	private String def3;
	private UFDate fixperiodbegin;
	private UFBoolean isctlbysecondarycode;
	private String supplytype;
	private UFDouble jyrhzdyw;
	private UFBoolean sfpchs;
	private UFDouble rationwtnum;
	private String pk_stordoc;
	private UFBoolean issendbydatum;
	private UFDouble producemethod;
	private String creator;
	private UFBoolean sfzzcp;
	private UFDouble maxstornum;
	private String def4;
	private UFBoolean isuseroute;
	private UFDouble dr;
	private String modifier;
	private String pk_invmandoc;
	private UFDouble pricemethod;

	public static final String PK_CORP = "pk_corp";
	public static final String PK_CKJLCID = "pk_ckjlcid";
	public static final String ABSSALES = "abssales";
	public static final String ZDHD = "zdhd";
	public static final String VIRTUALFLAG = "virtualflag";
	public static final String STOCKBYCHECK = "stockbycheck";
	public static final String JHJ = "jhj";
	public static final String SCSCDDMS = "scscddms";
	public static final String SAFETYSTOCKNUM = "safetystocknum";
	public static final String WGHXCL = "wghxcl";
	public static final String SFCBDX = "sfcbdx";
	public static final String PRIMARYFLAG = "primaryflag";
	public static final String NBZYJ = "nbzyj";
	public static final String OUTNUMHISTORYDAYS = "outnumhistorydays";
	public static final String FIXEDAHEAD = "fixedahead";
	public static final String DEF2 = "def2";
	public static final String ISCTOUTPUT = "isctoutput";
	public static final String BATCHRULE = "batchrule";
	public static final String DEF1 = "def1";
	public static final String ENDAHEAD = "endahead";
	public static final String FLANWIDENUM = "flanwidenum";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String CKSJ = "cksj";
	public static final String SFFZFW = "sffzfw";
	public static final String CURRENTAMOUNT = "currentamount";
	public static final String MATERTYPE = "matertype";
	public static final String ISCANCALCULATEDINVCOST = "iscancalculatedinvcost";
	public static final String SCPL = "scpl";
	public static final String BATCHNUM = "batchnum";
	public static final String OUTTYPE = "outtype";
	public static final String USABLEAMOUNT = "usableamount";
	public static final String ISFATHEROFBOM = "isfatherofbom";
	public static final String ZBCZJYXM = "zbczjyxm";
	public static final String STOCKUPPERDAYS = "stockupperdays";
	public static final String ACCQUIRETIME = "accquiretime";
	public static final String NYZBMXS = "nyzbmxs";
	public static final String ROADTYPE = "roadtype";
	public static final String GROSSWTNUM = "grosswtnum";
	public static final String SCHEATTR = "scheattr";
	public static final String WGGDSCZKXS = "wggdsczkxs";
	public static final String CHKFREEFLAG = "chkfreeflag";
	public static final String BLGDSCZKXS = "blgdsczkxs";
	public static final String STOCKLOWERDAYS = "stocklowerdays";
	public static final String DATUMOFSEND = "datumofsend";
	public static final String COMBINEFLAG = "combineflag";
	public static final String ABCGROSSPFT = "abcgrosspft";
	public static final String ISCOSTBYORDER = "iscostbyorder";
	public static final String YBGYS = "ybgys";
	public static final String LOWLEVELCODE = "lowlevelcode";
	public static final String MATERCLASS = "materclass";
	public static final String SCXYBZSFZK = "scxybzsfzk";
	public static final String PK_PRODUCE = "pk_produce";
	public static final String CHNGAMOUNT = "chngamount";
	public static final String PK_SEALUSER = "pk_sealuser";
	public static final String NETWTNUM = "netwtnum";
	public static final String ZBXS = "zbxs";
	public static final String CKCB = "ckcb";
	public static final String ISFXJZ = "isfxjz";
	public static final String ISCTLBYFIXPERIOD = "isctlbyfixperiod";
	public static final String PK_RKJLCID = "pk_rkjlcid";
	public static final String MATERSTATE = "materstate";
	public static final String SEALDATE = "sealdate";
	public static final String MINMULNUM = "minmulnum";
	public static final String ISCTLBYPRIMARYCODE = "isctlbyprimarycode";
	public static final String REALUSABLEAMOUNT = "realusableamount";
	public static final String ZGYS = "zgys";
	public static final String ISWHOLESETSEND = "iswholesetsend";
	public static final String USABLEAMOUNTBYFREE = "usableamountbyfree";
	public static final String FGYS = "fgys";
	public static final String FCPCLGSFA = "fcpclgsfa";
	public static final String BATCHPERIODNUM = "batchperiodnum";
	public static final String AHEADBATCH = "aheadbatch";
	public static final String MODIFYTIME = "modifytime";
	public static final String SEALFLAG = "sealflag";
	public static final String CONFIRMTIME = "confirmtime";
	public static final String PCHSCSCD = "pchscscd";
	public static final String SFSCX = "sfscx";
	public static final String ISELEMENTCHECK = "iselementcheck";
	public static final String ABCPURCHASE = "abcpurchase";
	public static final String PK_CALBODY = "pk_calbody";
	public static final String FLANMADENUM = "flanmadenum";
	public static final String FLANLENNUM = "flanlennum";
	public static final String CONVERSEFLAG = "converseflag";
	public static final String ABCFUNDEG = "abcfundeg";
	public static final String BATCHINCRNUM = "batchincrnum";
	public static final String CREATETIME = "createtime";
	public static final String PK_PSNDOC3 = "pk_psndoc3";
	public static final String SUMAHEAD = "sumahead";
	public static final String CGZZ = "cgzz";
	public static final String PK_DEPTDOC3 = "pk_deptdoc3";
	public static final String PRIMNESSNUM = "primnessnum";
	public static final String PREVAHEAD = "prevahead";
	public static final String ROUNDINGNUM = "roundingnum";
	public static final String AHEADCOFF = "aheadcoff";
	public static final String ISUSED = "isused";
	public static final String LOWSTOCKNUM = "lowstocknum";
	public static final String ISSEND = "issend";
	public static final String MINBATCHNUM = "minbatchnum";
	public static final String DEF5 = "def5";
	public static final String ABCFL = "abcfl";
	public static final String BOMTYPE = "bomtype";
	public static final String PK_PSNDOC4 = "pk_psndoc4";
	public static final String ISCREATESONPRODORDER = "iscreatesonprodorder";
	public static final String SFBJ = "sfbj";
	public static final String GFWLBM = "gfwlbm";
	public static final String WASTERRATE = "wasterrate";
	public static final String SFZB = "sfzb";
	public static final String ECOBATCH = "ecobatch";
	public static final String DEF3 = "def3";
	public static final String FIXPERIODBEGIN = "fixperiodbegin";
	public static final String ISCTLBYSECONDARYCODE = "isctlbysecondarycode";
	public static final String SUPPLYTYPE = "supplytype";
	public static final String JYRHZDYW = "jyrhzdyw";
	public static final String SFPCHS = "sfpchs";
	public static final String RATIONWTNUM = "rationwtnum";
	public static final String PK_STORDOC = "pk_stordoc";
	public static final String ISSENDBYDATUM = "issendbydatum";
	public static final String PRODUCEMETHOD = "producemethod";
	public static final String CREATOR = "creator";
	public static final String SFZZCP = "sfzzcp";
	public static final String MAXSTORNUM = "maxstornum";
	public static final String DEF4 = "def4";
	public static final String ISUSEROUTE = "isuseroute";
	public static final String MODIFIER = "modifier";
	public static final String PK_INVMANDOC = "pk_invmandoc";
	public static final String PRICEMETHOD = "pricemethod";
			
	/**
	 * ����pk_corp��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * ����pk_corp��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����pk_ckjlcid��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_ckjlcid () {
		return pk_ckjlcid;
	}   
	/**
	 * ����pk_ckjlcid��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_ckjlcid String
	 */
	public void setPk_ckjlcid (String newPk_ckjlcid ) {
	 	this.pk_ckjlcid = newPk_ckjlcid;
	} 	  
	/**
	 * ����abssales��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getAbssales () {
		return abssales;
	}   
	/**
	 * ����abssales��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAbssales UFBoolean
	 */
	public void setAbssales (UFBoolean newAbssales ) {
	 	this.abssales = newAbssales;
	} 	  
	/**
	 * ����zdhd��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getZdhd () {
		return zdhd;
	}   
	/**
	 * ����zdhd��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newZdhd UFDouble
	 */
	public void setZdhd (UFDouble newZdhd ) {
	 	this.zdhd = newZdhd;
	} 	  
	/**
	 * ����virtualflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getVirtualflag () {
		return virtualflag;
	}   
	/**
	 * ����virtualflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newVirtualflag UFBoolean
	 */
	public void setVirtualflag (UFBoolean newVirtualflag ) {
	 	this.virtualflag = newVirtualflag;
	} 	  
	/**
	 * ����stockbycheck��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getStockbycheck () {
		return stockbycheck;
	}   
	/**
	 * ����stockbycheck��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newStockbycheck UFBoolean
	 */
	public void setStockbycheck (UFBoolean newStockbycheck ) {
	 	this.stockbycheck = newStockbycheck;
	} 	  
	/**
	 * ����jhj��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getJhj () {
		return jhj;
	}   
	/**
	 * ����jhj��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newJhj UFDouble
	 */
	public void setJhj (UFDouble newJhj ) {
	 	this.jhj = newJhj;
	} 	  
	/**
	 * ����scscddms��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getScscddms () {
		return scscddms;
	}   
	/**
	 * ����scscddms��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newScscddms UFDouble
	 */
	public void setScscddms (UFDouble newScscddms ) {
	 	this.scscddms = newScscddms;
	} 	  
	/**
	 * ����safetystocknum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getSafetystocknum () {
		return safetystocknum;
	}   
	/**
	 * ����safetystocknum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSafetystocknum UFDouble
	 */
	public void setSafetystocknum (UFDouble newSafetystocknum ) {
	 	this.safetystocknum = newSafetystocknum;
	} 	  
	/**
	 * ����wghxcl��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getWghxcl () {
		return wghxcl;
	}   
	/**
	 * ����wghxcl��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newWghxcl UFDouble
	 */
	public void setWghxcl (UFDouble newWghxcl ) {
	 	this.wghxcl = newWghxcl;
	} 	  
	/**
	 * ����sfcbdx��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfcbdx () {
		return sfcbdx;
	}   
	/**
	 * ����sfcbdx��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfcbdx UFBoolean
	 */
	public void setSfcbdx (UFBoolean newSfcbdx ) {
	 	this.sfcbdx = newSfcbdx;
	} 	  
	/**
	 * ����primaryflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getPrimaryflag () {
		return primaryflag;
	}   
	/**
	 * ����primaryflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPrimaryflag UFBoolean
	 */
	public void setPrimaryflag (UFBoolean newPrimaryflag ) {
	 	this.primaryflag = newPrimaryflag;
	} 	  
	/**
	 * ����nbzyj��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getNbzyj () {
		return nbzyj;
	}   
	/**
	 * ����nbzyj��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newNbzyj UFDouble
	 */
	public void setNbzyj (UFDouble newNbzyj ) {
	 	this.nbzyj = newNbzyj;
	} 	  
	/**
	 * ����outnumhistorydays��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getOutnumhistorydays () {
		return outnumhistorydays;
	}   
	/**
	 * ����outnumhistorydays��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newOutnumhistorydays UFDouble
	 */
	public void setOutnumhistorydays (UFDouble newOutnumhistorydays ) {
	 	this.outnumhistorydays = newOutnumhistorydays;
	} 	  
	/**
	 * ����fixedahead��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getFixedahead () {
		return fixedahead;
	}   
	/**
	 * ����fixedahead��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFixedahead UFDouble
	 */
	public void setFixedahead (UFDouble newFixedahead ) {
	 	this.fixedahead = newFixedahead;
	} 	  
	/**
	 * ����def2��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDef2 String
	 */
	public void setDef2 (String newDef2 ) {
	 	this.def2 = newDef2;
	} 	  
	/**
	 * ����isctoutput��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsctoutput () {
		return isctoutput;
	}   
	/**
	 * ����isctoutput��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsctoutput UFBoolean
	 */
	public void setIsctoutput (UFBoolean newIsctoutput ) {
	 	this.isctoutput = newIsctoutput;
	} 	  
	/**
	 * ����batchrule��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getBatchrule () {
		return batchrule;
	}   
	/**
	 * ����batchrule��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBatchrule String
	 */
	public void setBatchrule (String newBatchrule ) {
	 	this.batchrule = newBatchrule;
	} 	  
	/**
	 * ����def1��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDef1 String
	 */
	public void setDef1 (String newDef1 ) {
	 	this.def1 = newDef1;
	} 	  
	/**
	 * ����endahead��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getEndahead () {
		return endahead;
	}   
	/**
	 * ����endahead��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newEndahead UFDouble
	 */
	public void setEndahead (UFDouble newEndahead ) {
	 	this.endahead = newEndahead;
	} 	  
	/**
	 * ����flanwidenum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getFlanwidenum () {
		return flanwidenum;
	}   
	/**
	 * ����flanwidenum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFlanwidenum UFDouble
	 */
	public void setFlanwidenum (UFDouble newFlanwidenum ) {
	 	this.flanwidenum = newFlanwidenum;
	} 	  
	/**
	 * ����pk_invbasdoc��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * ����pk_invbasdoc��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * ����cksj��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getCksj () {
		return cksj;
	}   
	/**
	 * ����cksj��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCksj UFDouble
	 */
	public void setCksj (UFDouble newCksj ) {
	 	this.cksj = newCksj;
	} 	  
	/**
	 * ����sffzfw��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSffzfw () {
		return sffzfw;
	}   
	/**
	 * ����sffzfw��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSffzfw UFBoolean
	 */
	public void setSffzfw (UFBoolean newSffzfw ) {
	 	this.sffzfw = newSffzfw;
	} 	  
	/**
	 * ����currentamount��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getCurrentamount () {
		return currentamount;
	}   
	/**
	 * ����currentamount��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCurrentamount UFDouble
	 */
	public void setCurrentamount (UFDouble newCurrentamount ) {
	 	this.currentamount = newCurrentamount;
	} 	  
	/**
	 * ����matertype��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getMatertype () {
		return matertype;
	}   
	/**
	 * ����matertype��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMatertype String
	 */
	public void setMatertype (String newMatertype ) {
	 	this.matertype = newMatertype;
	} 	  
	/**
	 * ����iscancalculatedinvcost��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIscancalculatedinvcost () {
		return iscancalculatedinvcost;
	}   
	/**
	 * ����iscancalculatedinvcost��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIscancalculatedinvcost UFBoolean
	 */
	public void setIscancalculatedinvcost (UFBoolean newIscancalculatedinvcost ) {
	 	this.iscancalculatedinvcost = newIscancalculatedinvcost;
	} 	  
	/**
	 * ����scpl��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getScpl () {
		return scpl;
	}   
	/**
	 * ����scpl��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newScpl UFDouble
	 */
	public void setScpl (UFDouble newScpl ) {
	 	this.scpl = newScpl;
	} 	  
	/**
	 * ����batchnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getBatchnum () {
		return batchnum;
	}   
	/**
	 * ����batchnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBatchnum UFDouble
	 */
	public void setBatchnum (UFDouble newBatchnum ) {
	 	this.batchnum = newBatchnum;
	} 	  
	/**
	 * ����outtype��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getOuttype () {
		return outtype;
	}   
	/**
	 * ����outtype��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newOuttype String
	 */
	public void setOuttype (String newOuttype ) {
	 	this.outtype = newOuttype;
	} 	  
	/**
	 * ����usableamount��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getUsableamount () {
		return usableamount;
	}   
	/**
	 * ����usableamount��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newUsableamount String
	 */
	public void setUsableamount (String newUsableamount ) {
	 	this.usableamount = newUsableamount;
	} 	  
	/**
	 * ����isfatherofbom��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsfatherofbom () {
		return isfatherofbom;
	}   
	/**
	 * ����isfatherofbom��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsfatherofbom UFBoolean
	 */
	public void setIsfatherofbom (UFBoolean newIsfatherofbom ) {
	 	this.isfatherofbom = newIsfatherofbom;
	} 	  
	/**
	 * ����zbczjyxm��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getZbczjyxm () {
		return zbczjyxm;
	}   
	/**
	 * ����zbczjyxm��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newZbczjyxm String
	 */
	public void setZbczjyxm (String newZbczjyxm ) {
	 	this.zbczjyxm = newZbczjyxm;
	} 	  
	/**
	 * ����stockupperdays��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getStockupperdays () {
		return stockupperdays;
	}   
	/**
	 * ����stockupperdays��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newStockupperdays UFDouble
	 */
	public void setStockupperdays (UFDouble newStockupperdays ) {
	 	this.stockupperdays = newStockupperdays;
	} 	  
	/**
	 * ����accquiretime��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getAccquiretime () {
		return accquiretime;
	}   
	/**
	 * ����accquiretime��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAccquiretime UFDouble
	 */
	public void setAccquiretime (UFDouble newAccquiretime ) {
	 	this.accquiretime = newAccquiretime;
	} 	  
	/**
	 * ����nyzbmxs��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getNyzbmxs () {
		return nyzbmxs;
	}   
	/**
	 * ����nyzbmxs��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newNyzbmxs UFDouble
	 */
	public void setNyzbmxs (UFDouble newNyzbmxs ) {
	 	this.nyzbmxs = newNyzbmxs;
	} 	  
	/**
	 * ����roadtype��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getRoadtype () {
		return roadtype;
	}   
	/**
	 * ����roadtype��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newRoadtype UFDouble
	 */
	public void setRoadtype (UFDouble newRoadtype ) {
	 	this.roadtype = newRoadtype;
	} 	  
	/**
	 * ����grosswtnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getGrosswtnum () {
		return grosswtnum;
	}   
	/**
	 * ����grosswtnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newGrosswtnum UFDouble
	 */
	public void setGrosswtnum (UFDouble newGrosswtnum ) {
	 	this.grosswtnum = newGrosswtnum;
	} 	  
	/**
	 * ����scheattr��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getScheattr () {
		return scheattr;
	}   
	/**
	 * ����scheattr��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newScheattr String
	 */
	public void setScheattr (String newScheattr ) {
	 	this.scheattr = newScheattr;
	} 	  
	/**
	 * ����wggdsczkxs��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getWggdsczkxs () {
		return wggdsczkxs;
	}   
	/**
	 * ����wggdsczkxs��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newWggdsczkxs UFDouble
	 */
	public void setWggdsczkxs (UFDouble newWggdsczkxs ) {
	 	this.wggdsczkxs = newWggdsczkxs;
	} 	  
	/**
	 * ����chkfreeflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getChkfreeflag () {
		return chkfreeflag;
	}   
	/**
	 * ����chkfreeflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newChkfreeflag UFBoolean
	 */
	public void setChkfreeflag (UFBoolean newChkfreeflag ) {
	 	this.chkfreeflag = newChkfreeflag;
	} 	  
	/**
	 * ����blgdsczkxs��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getBlgdsczkxs () {
		return blgdsczkxs;
	}   
	/**
	 * ����blgdsczkxs��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBlgdsczkxs UFDouble
	 */
	public void setBlgdsczkxs (UFDouble newBlgdsczkxs ) {
	 	this.blgdsczkxs = newBlgdsczkxs;
	} 	  
	/**
	 * ����stocklowerdays��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getStocklowerdays () {
		return stocklowerdays;
	}   
	/**
	 * ����stocklowerdays��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newStocklowerdays UFDouble
	 */
	public void setStocklowerdays (UFDouble newStocklowerdays ) {
	 	this.stocklowerdays = newStocklowerdays;
	} 	  
	/**
	 * ����datumofsend��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getDatumofsend () {
		return datumofsend;
	}   
	/**
	 * ����datumofsend��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDatumofsend UFDouble
	 */
	public void setDatumofsend (UFDouble newDatumofsend ) {
	 	this.datumofsend = newDatumofsend;
	} 	  
	/**
	 * ����combineflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getCombineflag () {
		return combineflag;
	}   
	/**
	 * ����combineflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCombineflag UFBoolean
	 */
	public void setCombineflag (UFBoolean newCombineflag ) {
	 	this.combineflag = newCombineflag;
	} 	  
	/**
	 * ����abcgrosspft��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getAbcgrosspft () {
		return abcgrosspft;
	}   
	/**
	 * ����abcgrosspft��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAbcgrosspft UFBoolean
	 */
	public void setAbcgrosspft (UFBoolean newAbcgrosspft ) {
	 	this.abcgrosspft = newAbcgrosspft;
	} 	  
	/**
	 * ����iscostbyorder��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIscostbyorder () {
		return iscostbyorder;
	}   
	/**
	 * ����iscostbyorder��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIscostbyorder UFBoolean
	 */
	public void setIscostbyorder (UFBoolean newIscostbyorder ) {
	 	this.iscostbyorder = newIscostbyorder;
	} 	  
	/**
	 * ����ybgys��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getYbgys () {
		return ybgys;
	}   
	/**
	 * ����ybgys��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newYbgys String
	 */
	public void setYbgys (String newYbgys ) {
	 	this.ybgys = newYbgys;
	} 	  
	/**
	 * ����lowlevelcode��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getLowlevelcode () {
		return lowlevelcode;
	}   
	/**
	 * ����lowlevelcode��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newLowlevelcode UFDouble
	 */
	public void setLowlevelcode (UFDouble newLowlevelcode ) {
	 	this.lowlevelcode = newLowlevelcode;
	} 	  
	/**
	 * ����materclass��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getMaterclass () {
		return materclass;
	}   
	/**
	 * ����materclass��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMaterclass UFDouble
	 */
	public void setMaterclass (UFDouble newMaterclass ) {
	 	this.materclass = newMaterclass;
	} 	  
	/**
	 * ����scxybzsfzk��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getScxybzsfzk () {
		return scxybzsfzk;
	}   
	/**
	 * ����scxybzsfzk��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newScxybzsfzk UFBoolean
	 */
	public void setScxybzsfzk (UFBoolean newScxybzsfzk ) {
	 	this.scxybzsfzk = newScxybzsfzk;
	} 	  
	/**
	 * ����pk_produce��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_produce () {
		return pk_produce;
	}   
	/**
	 * ����pk_produce��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_produce String
	 */
	public void setPk_produce (String newPk_produce ) {
	 	this.pk_produce = newPk_produce;
	} 	  
	/**
	 * ����chngamount��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getChngamount () {
		return chngamount;
	}   
	/**
	 * ����chngamount��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newChngamount UFDouble
	 */
	public void setChngamount (UFDouble newChngamount ) {
	 	this.chngamount = newChngamount;
	} 	  
	/**
	 * ����pk_sealuser��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_sealuser () {
		return pk_sealuser;
	}   
	/**
	 * ����pk_sealuser��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_sealuser String
	 */
	public void setPk_sealuser (String newPk_sealuser ) {
	 	this.pk_sealuser = newPk_sealuser;
	} 	  
	/**
	 * ����netwtnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getNetwtnum () {
		return netwtnum;
	}   
	/**
	 * ����netwtnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newNetwtnum UFDouble
	 */
	public void setNetwtnum (UFDouble newNetwtnum ) {
	 	this.netwtnum = newNetwtnum;
	} 	  
	/**
	 * ����zbxs��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getZbxs () {
		return zbxs;
	}   
	/**
	 * ����zbxs��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newZbxs UFDouble
	 */
	public void setZbxs (UFDouble newZbxs ) {
	 	this.zbxs = newZbxs;
	} 	  
	/**
	 * ����ckcb��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getCkcb () {
		return ckcb;
	}   
	/**
	 * ����ckcb��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCkcb UFDouble
	 */
	public void setCkcb (UFDouble newCkcb ) {
	 	this.ckcb = newCkcb;
	} 	  
	/**
	 * ����isfxjz��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsfxjz () {
		return isfxjz;
	}   
	/**
	 * ����isfxjz��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsfxjz UFBoolean
	 */
	public void setIsfxjz (UFBoolean newIsfxjz ) {
	 	this.isfxjz = newIsfxjz;
	} 	  
	/**
	 * ����isctlbyfixperiod��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsctlbyfixperiod () {
		return isctlbyfixperiod;
	}   
	/**
	 * ����isctlbyfixperiod��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsctlbyfixperiod UFBoolean
	 */
	public void setIsctlbyfixperiod (UFBoolean newIsctlbyfixperiod ) {
	 	this.isctlbyfixperiod = newIsctlbyfixperiod;
	} 	  
	/**
	 * ����pk_rkjlcid��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_rkjlcid () {
		return pk_rkjlcid;
	}   
	/**
	 * ����pk_rkjlcid��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_rkjlcid String
	 */
	public void setPk_rkjlcid (String newPk_rkjlcid ) {
	 	this.pk_rkjlcid = newPk_rkjlcid;
	} 	  
	/**
	 * ����materstate��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getMaterstate () {
		return materstate;
	}   
	/**
	 * ����materstate��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMaterstate UFDouble
	 */
	public void setMaterstate (UFDouble newMaterstate ) {
	 	this.materstate = newMaterstate;
	} 	  
	/**
	 * ����sealdate��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDate
	 */
	public UFDate getSealdate () {
		return sealdate;
	}   
	/**
	 * ����sealdate��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSealdate UFDate
	 */
	public void setSealdate (UFDate newSealdate ) {
	 	this.sealdate = newSealdate;
	} 	  
	/**
	 * ����minmulnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getMinmulnum () {
		return minmulnum;
	}   
	/**
	 * ����minmulnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMinmulnum UFDouble
	 */
	public void setMinmulnum (UFDouble newMinmulnum ) {
	 	this.minmulnum = newMinmulnum;
	} 	  
	/**
	 * ����isctlbyprimarycode��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsctlbyprimarycode () {
		return isctlbyprimarycode;
	}   
	/**
	 * ����isctlbyprimarycode��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsctlbyprimarycode UFBoolean
	 */
	public void setIsctlbyprimarycode (UFBoolean newIsctlbyprimarycode ) {
	 	this.isctlbyprimarycode = newIsctlbyprimarycode;
	} 	  
	/**
	 * ����realusableamount��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getRealusableamount () {
		return realusableamount;
	}   
	/**
	 * ����realusableamount��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newRealusableamount UFDouble
	 */
	public void setRealusableamount (UFDouble newRealusableamount ) {
	 	this.realusableamount = newRealusableamount;
	} 	  
	/**
	 * ����zgys��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getZgys () {
		return zgys;
	}   
	/**
	 * ����zgys��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newZgys String
	 */
	public void setZgys (String newZgys ) {
	 	this.zgys = newZgys;
	} 	  
	/**
	 * ����iswholesetsend��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIswholesetsend () {
		return iswholesetsend;
	}   
	/**
	 * ����iswholesetsend��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIswholesetsend UFBoolean
	 */
	public void setIswholesetsend (UFBoolean newIswholesetsend ) {
	 	this.iswholesetsend = newIswholesetsend;
	} 	  
	/**
	 * ����usableamountbyfree��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getUsableamountbyfree () {
		return usableamountbyfree;
	}   
	/**
	 * ����usableamountbyfree��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newUsableamountbyfree String
	 */
	public void setUsableamountbyfree (String newUsableamountbyfree ) {
	 	this.usableamountbyfree = newUsableamountbyfree;
	} 	  
	/**
	 * ����fgys��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getFgys () {
		return fgys;
	}   
	/**
	 * ����fgys��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFgys String
	 */
	public void setFgys (String newFgys ) {
	 	this.fgys = newFgys;
	} 	  
	/**
	 * ����fcpclgsfa��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getFcpclgsfa () {
		return fcpclgsfa;
	}   
	/**
	 * ����fcpclgsfa��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFcpclgsfa UFDouble
	 */
	public void setFcpclgsfa (UFDouble newFcpclgsfa ) {
	 	this.fcpclgsfa = newFcpclgsfa;
	} 	  
	/**
	 * ����batchperiodnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getBatchperiodnum () {
		return batchperiodnum;
	}   
	/**
	 * ����batchperiodnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBatchperiodnum UFDouble
	 */
	public void setBatchperiodnum (UFDouble newBatchperiodnum ) {
	 	this.batchperiodnum = newBatchperiodnum;
	} 	  
	/**
	 * ����aheadbatch��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getAheadbatch () {
		return aheadbatch;
	}   
	/**
	 * ����aheadbatch��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAheadbatch UFDouble
	 */
	public void setAheadbatch (UFDouble newAheadbatch ) {
	 	this.aheadbatch = newAheadbatch;
	} 	  
	/**
	 * ����modifytime��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDateTime
	 */
	public UFDateTime getModifytime () {
		return modifytime;
	}   
	/**
	 * ����modifytime��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newModifytime UFDateTime
	 */
	public void setModifytime (UFDateTime newModifytime ) {
	 	this.modifytime = newModifytime;
	} 	  
	/**
	 * ����sealflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSealflag () {
		return sealflag;
	}   
	/**
	 * ����sealflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSealflag UFBoolean
	 */
	public void setSealflag (UFBoolean newSealflag ) {
	 	this.sealflag = newSealflag;
	} 	  
	/**
	 * ����confirmtime��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getConfirmtime () {
		return confirmtime;
	}   
	/**
	 * ����confirmtime��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newConfirmtime UFDouble
	 */
	public void setConfirmtime (UFDouble newConfirmtime ) {
	 	this.confirmtime = newConfirmtime;
	} 	  
	/**
	 * ����pchscscd��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getPchscscd () {
		return pchscscd;
	}   
	/**
	 * ����pchscscd��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPchscscd UFDouble
	 */
	public void setPchscscd (UFDouble newPchscscd ) {
	 	this.pchscscd = newPchscscd;
	} 	  
	/**
	 * ����sfscx��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfscx () {
		return sfscx;
	}   
	/**
	 * ����sfscx��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfscx UFBoolean
	 */
	public void setSfscx (UFBoolean newSfscx ) {
	 	this.sfscx = newSfscx;
	} 	  
	/**
	 * ����iselementcheck��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIselementcheck () {
		return iselementcheck;
	}   
	/**
	 * ����iselementcheck��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIselementcheck UFBoolean
	 */
	public void setIselementcheck (UFBoolean newIselementcheck ) {
	 	this.iselementcheck = newIselementcheck;
	} 	  
	/**
	 * ����abcpurchase��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getAbcpurchase () {
		return abcpurchase;
	}   
	/**
	 * ����abcpurchase��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAbcpurchase UFBoolean
	 */
	public void setAbcpurchase (UFBoolean newAbcpurchase ) {
	 	this.abcpurchase = newAbcpurchase;
	} 	  
	/**
	 * ����pk_calbody��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_calbody () {
		return pk_calbody;
	}   
	/**
	 * ����pk_calbody��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_calbody String
	 */
	public void setPk_calbody (String newPk_calbody ) {
	 	this.pk_calbody = newPk_calbody;
	} 	  
	/**
	 * ����flanmadenum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getFlanmadenum () {
		return flanmadenum;
	}   
	/**
	 * ����flanmadenum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFlanmadenum UFDouble
	 */
	public void setFlanmadenum (UFDouble newFlanmadenum ) {
	 	this.flanmadenum = newFlanmadenum;
	} 	  
	/**
	 * ����flanlennum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getFlanlennum () {
		return flanlennum;
	}   
	/**
	 * ����flanlennum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFlanlennum UFDouble
	 */
	public void setFlanlennum (UFDouble newFlanlennum ) {
	 	this.flanlennum = newFlanlennum;
	} 	  
	/**
	 * ����converseflag��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getConverseflag () {
		return converseflag;
	}   
	/**
	 * ����converseflag��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newConverseflag UFBoolean
	 */
	public void setConverseflag (UFBoolean newConverseflag ) {
	 	this.converseflag = newConverseflag;
	} 	  
	/**
	 * ����abcfundeg��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getAbcfundeg () {
		return abcfundeg;
	}   
	/**
	 * ����abcfundeg��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAbcfundeg UFBoolean
	 */
	public void setAbcfundeg (UFBoolean newAbcfundeg ) {
	 	this.abcfundeg = newAbcfundeg;
	} 	  
	/**
	 * ����batchincrnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getBatchincrnum () {
		return batchincrnum;
	}   
	/**
	 * ����batchincrnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBatchincrnum UFDouble
	 */
	public void setBatchincrnum (UFDouble newBatchincrnum ) {
	 	this.batchincrnum = newBatchincrnum;
	} 	  
	/**
	 * ����createtime��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDateTime
	 */
	public UFDateTime getCreatetime () {
		return createtime;
	}   
	/**
	 * ����createtime��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCreatetime UFDateTime
	 */
	public void setCreatetime (UFDateTime newCreatetime ) {
	 	this.createtime = newCreatetime;
	} 	  
	/**
	 * ����pk_psndoc3��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_psndoc3 () {
		return pk_psndoc3;
	}   
	/**
	 * ����pk_psndoc3��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_psndoc3 String
	 */
	public void setPk_psndoc3 (String newPk_psndoc3 ) {
	 	this.pk_psndoc3 = newPk_psndoc3;
	} 	  
	/**
	 * ����sumahead��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getSumahead () {
		return sumahead;
	}   
	/**
	 * ����sumahead��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSumahead UFDouble
	 */
	public void setSumahead (UFDouble newSumahead ) {
	 	this.sumahead = newSumahead;
	} 	  
	/**
	 * ����cgzz��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getCgzz () {
		return cgzz;
	}   
	/**
	 * ����cgzz��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCgzz String
	 */
	public void setCgzz (String newCgzz ) {
	 	this.cgzz = newCgzz;
	} 	  
	/**
	 * ����pk_deptdoc3��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_deptdoc3 () {
		return pk_deptdoc3;
	}   
	/**
	 * ����pk_deptdoc3��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_deptdoc3 String
	 */
	public void setPk_deptdoc3 (String newPk_deptdoc3 ) {
	 	this.pk_deptdoc3 = newPk_deptdoc3;
	} 	  
	/**
	 * ����primnessnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getPrimnessnum () {
		return primnessnum;
	}   
	/**
	 * ����primnessnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPrimnessnum UFDouble
	 */
	public void setPrimnessnum (UFDouble newPrimnessnum ) {
	 	this.primnessnum = newPrimnessnum;
	} 	  
	/**
	 * ����prevahead��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getPrevahead () {
		return prevahead;
	}   
	/**
	 * ����prevahead��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPrevahead UFDouble
	 */
	public void setPrevahead (UFDouble newPrevahead ) {
	 	this.prevahead = newPrevahead;
	} 	  
	/**
	 * ����roundingnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getRoundingnum () {
		return roundingnum;
	}   
	/**
	 * ����roundingnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newRoundingnum UFDouble
	 */
	public void setRoundingnum (UFDouble newRoundingnum ) {
	 	this.roundingnum = newRoundingnum;
	} 	  
	/**
	 * ����aheadcoff��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getAheadcoff () {
		return aheadcoff;
	}   
	/**
	 * ����aheadcoff��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAheadcoff UFDouble
	 */
	public void setAheadcoff (UFDouble newAheadcoff ) {
	 	this.aheadcoff = newAheadcoff;
	} 	  
	/**
	 * ����isused��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsused () {
		return isused;
	}   
	/**
	 * ����isused��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsused UFBoolean
	 */
	public void setIsused (UFBoolean newIsused ) {
	 	this.isused = newIsused;
	} 	  
	/**
	 * ����lowstocknum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getLowstocknum () {
		return lowstocknum;
	}   
	/**
	 * ����lowstocknum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newLowstocknum UFDouble
	 */
	public void setLowstocknum (UFDouble newLowstocknum ) {
	 	this.lowstocknum = newLowstocknum;
	} 	  
	/**
	 * ����issend��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIssend () {
		return issend;
	}   
	/**
	 * ����issend��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIssend UFBoolean
	 */
	public void setIssend (UFBoolean newIssend ) {
	 	this.issend = newIssend;
	} 	  
	/**
	 * ����minbatchnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getMinbatchnum () {
		return minbatchnum;
	}   
	/**
	 * ����minbatchnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMinbatchnum UFDouble
	 */
	public void setMinbatchnum (UFDouble newMinbatchnum ) {
	 	this.minbatchnum = newMinbatchnum;
	} 	  
	/**
	 * ����def5��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDef5 String
	 */
	public void setDef5 (String newDef5 ) {
	 	this.def5 = newDef5;
	} 	  
	/**
	 * ����abcfl��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getAbcfl () {
		return abcfl;
	}   
	/**
	 * ����abcfl��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newAbcfl UFBoolean
	 */
	public void setAbcfl (UFBoolean newAbcfl ) {
	 	this.abcfl = newAbcfl;
	} 	  
	/**
	 * ����bomtype��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getBomtype () {
		return bomtype;
	}   
	/**
	 * ����bomtype��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newBomtype UFDouble
	 */
	public void setBomtype (UFDouble newBomtype ) {
	 	this.bomtype = newBomtype;
	} 	  
	/**
	 * ����pk_psndoc4��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_psndoc4 () {
		return pk_psndoc4;
	}   
	/**
	 * ����pk_psndoc4��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_psndoc4 String
	 */
	public void setPk_psndoc4 (String newPk_psndoc4 ) {
	 	this.pk_psndoc4 = newPk_psndoc4;
	} 	  
	/**
	 * ����iscreatesonprodorder��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIscreatesonprodorder () {
		return iscreatesonprodorder;
	}   
	/**
	 * ����iscreatesonprodorder��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIscreatesonprodorder UFBoolean
	 */
	public void setIscreatesonprodorder (UFBoolean newIscreatesonprodorder ) {
	 	this.iscreatesonprodorder = newIscreatesonprodorder;
	} 	  
	/**
	 * ����sfbj��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfbj () {
		return sfbj;
	}   
	/**
	 * ����sfbj��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfbj UFBoolean
	 */
	public void setSfbj (UFBoolean newSfbj ) {
	 	this.sfbj = newSfbj;
	} 	  
	/**
	 * ����gfwlbm��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getGfwlbm () {
		return gfwlbm;
	}   
	/**
	 * ����gfwlbm��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newGfwlbm String
	 */
	public void setGfwlbm (String newGfwlbm ) {
	 	this.gfwlbm = newGfwlbm;
	} 	  
	/**
	 * ����wasterrate��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getWasterrate () {
		return wasterrate;
	}   
	/**
	 * ����wasterrate��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newWasterrate UFDouble
	 */
	public void setWasterrate (UFDouble newWasterrate ) {
	 	this.wasterrate = newWasterrate;
	} 	  
	/**
	 * ����sfzb��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfzb () {
		return sfzb;
	}   
	/**
	 * ����sfzb��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfzb UFBoolean
	 */
	public void setSfzb (UFBoolean newSfzb ) {
	 	this.sfzb = newSfzb;
	} 	  
	/**
	 * ����ecobatch��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getEcobatch () {
		return ecobatch;
	}   
	/**
	 * ����ecobatch��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newEcobatch UFDouble
	 */
	public void setEcobatch (UFDouble newEcobatch ) {
	 	this.ecobatch = newEcobatch;
	} 	  
	/**
	 * ����def3��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDef3 String
	 */
	public void setDef3 (String newDef3 ) {
	 	this.def3 = newDef3;
	} 	  
	/**
	 * ����fixperiodbegin��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDate
	 */
	public UFDate getFixperiodbegin () {
		return fixperiodbegin;
	}   
	/**
	 * ����fixperiodbegin��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newFixperiodbegin UFDate
	 */
	public void setFixperiodbegin (UFDate newFixperiodbegin ) {
	 	this.fixperiodbegin = newFixperiodbegin;
	} 	  
	/**
	 * ����isctlbysecondarycode��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsctlbysecondarycode () {
		return isctlbysecondarycode;
	}   
	/**
	 * ����isctlbysecondarycode��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsctlbysecondarycode UFBoolean
	 */
	public void setIsctlbysecondarycode (UFBoolean newIsctlbysecondarycode ) {
	 	this.isctlbysecondarycode = newIsctlbysecondarycode;
	} 	  
	/**
	 * ����supplytype��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getSupplytype () {
		return supplytype;
	}   
	/**
	 * ����supplytype��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSupplytype String
	 */
	public void setSupplytype (String newSupplytype ) {
	 	this.supplytype = newSupplytype;
	} 	  
	/**
	 * ����jyrhzdyw��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getJyrhzdyw () {
		return jyrhzdyw;
	}   
	/**
	 * ����jyrhzdyw��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newJyrhzdyw UFDouble
	 */
	public void setJyrhzdyw (UFDouble newJyrhzdyw ) {
	 	this.jyrhzdyw = newJyrhzdyw;
	} 	  
	/**
	 * ����sfpchs��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfpchs () {
		return sfpchs;
	}   
	/**
	 * ����sfpchs��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfpchs UFBoolean
	 */
	public void setSfpchs (UFBoolean newSfpchs ) {
	 	this.sfpchs = newSfpchs;
	} 	  
	/**
	 * ����rationwtnum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getRationwtnum () {
		return rationwtnum;
	}   
	/**
	 * ����rationwtnum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newRationwtnum UFDouble
	 */
	public void setRationwtnum (UFDouble newRationwtnum ) {
	 	this.rationwtnum = newRationwtnum;
	} 	  
	/**
	 * ����pk_stordoc��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_stordoc () {
		return pk_stordoc;
	}   
	/**
	 * ����pk_stordoc��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_stordoc String
	 */
	public void setPk_stordoc (String newPk_stordoc ) {
	 	this.pk_stordoc = newPk_stordoc;
	} 	  
	/**
	 * ����issendbydatum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIssendbydatum () {
		return issendbydatum;
	}   
	/**
	 * ����issendbydatum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIssendbydatum UFBoolean
	 */
	public void setIssendbydatum (UFBoolean newIssendbydatum ) {
	 	this.issendbydatum = newIssendbydatum;
	} 	  
	/**
	 * ����producemethod��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getProducemethod () {
		return producemethod;
	}   
	/**
	 * ����producemethod��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newProducemethod UFDouble
	 */
	public void setProducemethod (UFDouble newProducemethod ) {
	 	this.producemethod = newProducemethod;
	} 	  
	/**
	 * ����creator��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getCreator () {
		return creator;
	}   
	/**
	 * ����creator��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newCreator String
	 */
	public void setCreator (String newCreator ) {
	 	this.creator = newCreator;
	} 	  
	/**
	 * ����sfzzcp��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getSfzzcp () {
		return sfzzcp;
	}   
	/**
	 * ����sfzzcp��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newSfzzcp UFBoolean
	 */
	public void setSfzzcp (UFBoolean newSfzzcp ) {
	 	this.sfzzcp = newSfzzcp;
	} 	  
	/**
	 * ����maxstornum��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getMaxstornum () {
		return maxstornum;
	}   
	/**
	 * ����maxstornum��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newMaxstornum UFDouble
	 */
	public void setMaxstornum (UFDouble newMaxstornum ) {
	 	this.maxstornum = newMaxstornum;
	} 	  
	/**
	 * ����def4��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDef4 String
	 */
	public void setDef4 (String newDef4 ) {
	 	this.def4 = newDef4;
	} 	  
	/**
	 * ����isuseroute��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFBoolean
	 */
	public UFBoolean getIsuseroute () {
		return isuseroute;
	}   
	/**
	 * ����isuseroute��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newIsuseroute UFBoolean
	 */
	public void setIsuseroute (UFBoolean newIsuseroute ) {
	 	this.isuseroute = newIsuseroute;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newDr UFDouble
	 */
	public void setDr (UFDouble newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����modifier��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getModifier () {
		return modifier;
	}   
	/**
	 * ����modifier��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newModifier String
	 */
	public void setModifier (String newModifier ) {
	 	this.modifier = newModifier;
	} 	  
	/**
	 * ����pk_invmandoc��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return String
	 */
	public String getPk_invmandoc () {
		return pk_invmandoc;
	}   
	/**
	 * ����pk_invmandoc��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPk_invmandoc String
	 */
	public void setPk_invmandoc (String newPk_invmandoc ) {
	 	this.pk_invmandoc = newPk_invmandoc;
	} 	  
	/**
	 * ����pricemethod��Getter����.
	 * ��������:2014-07-08 21:29:57
	 * @return UFDouble
	 */
	public UFDouble getPricemethod () {
		return pricemethod;
	}   
	/**
	 * ����pricemethod��Setter����.
	 * ��������:2014-07-08 21:29:57
	 * @param newPricemethod UFDouble
	 */
	public void setPricemethod (UFDouble newPricemethod ) {
	 	this.pricemethod = newPricemethod;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2014-07-08 21:29:57
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2014-07-08 21:29:57
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_produce";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2014-07-08 21:29:57
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_produce";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2014-07-08 21:29:57
	  */
     public PRODUCEVO() {
		super();	
	}    
} 
