package nc.vo.by.invapp.h0h002;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 * ��������:2014-07-08 21:30:36
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class INVBASDOCVO extends SuperVO {
	private UFDouble storeunitnum;
	private String pk_corp;
	private UFDateTime ts;
	private String def17;
	private String pk_prodline;
	private String def16;
	private String free2;
	private String pk_taxitems;
	private UFDateTime modifytime;
	private String sealflag;
	private String free5;
	private String def11;
	private String invtype;
	private String def2;
	private UFBoolean asset;
	private UFBoolean ismngstockbygrswt;
	private String def1;
	private String graphid;
	private String forinvname;
	private String free3;
	private String invmnecode;
	private String pk_invbasdoc;
	private String memo;
	private UFDateTime createtime;
	private String pk_invcl;
	private String pk_assetcategory;
	private String def18;
	private UFBoolean setpartsflag;
	private String pk_measdoc2;
	private String pk_measdoc;
	private String def7;
	private UFDouble weitunitnum;
	private String invpinpai;
	private UFBoolean discountflag;
	private String pk_measdoc1;
	private String def8;
	private String length;
	private String def5;
	private String invspec;
	private String def14;
	private UFBoolean isstorebyconvert;
	private UFBoolean iselectrans;
	private String def9;
	private UFBoolean assistunit;
	private String def6;
	private String def3;
	private String def19;
	private String free4;
	private String invcode;
	private UFDouble shipunitnum;
	private UFBoolean autobalancemeas;
	private String def10;
	private UFBoolean isretail;
	private String def12;
	private String invbarcode;
	private String invname;
	private String invshortname;
	private String pk_measdoc5;
	private String def13;
	private String creator;
	private UFDouble unitvolume;
	private String pk_measdoc6;
	private UFDouble unitweight;
	private String def15;
	private String pk_measdoc3;
	private String def4;
	private String height;
	private UFDouble dr;
	private String modifier;
	private UFBoolean laborflag;
	private String width;
	private String free1;
	private String def20;

	public static final String STOREUNITNUM = "storeunitnum";
	public static final String PK_CORP = "pk_corp";
	public static final String DEF17 = "def17";
	public static final String PK_PRODLINE = "pk_prodline";
	public static final String DEF16 = "def16";
	public static final String FREE2 = "free2";
	public static final String PK_TAXITEMS = "pk_taxitems";
	public static final String MODIFYTIME = "modifytime";
	public static final String SEALFLAG = "sealflag";
	public static final String FREE5 = "free5";
	public static final String DEF11 = "def11";
	public static final String INVTYPE = "invtype";
	public static final String DEF2 = "def2";
	public static final String ASSET = "asset";
	public static final String ISMNGSTOCKBYGRSWT = "ismngstockbygrswt";
	public static final String DEF1 = "def1";
	public static final String GRAPHID = "graphid";
	public static final String FORINVNAME = "forinvname";
	public static final String FREE3 = "free3";
	public static final String INVMNECODE = "invmnecode";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String MEMO = "memo";
	public static final String CREATETIME = "createtime";
	public static final String PK_INVCL = "pk_invcl";
	public static final String PK_ASSETCATEGORY = "pk_assetcategory";
	public static final String DEF18 = "def18";
	public static final String SETPARTSFLAG = "setpartsflag";
	public static final String PK_MEASDOC2 = "pk_measdoc2";
	public static final String PK_MEASDOC = "pk_measdoc";
	public static final String DEF7 = "def7";
	public static final String WEITUNITNUM = "weitunitnum";
	public static final String INVPINPAI = "invpinpai";
	public static final String DISCOUNTFLAG = "discountflag";
	public static final String PK_MEASDOC1 = "pk_measdoc1";
	public static final String DEF8 = "def8";
	public static final String LENGTH = "length";
	public static final String DEF5 = "def5";
	public static final String INVSPEC = "invspec";
	public static final String DEF14 = "def14";
	public static final String ISSTOREBYCONVERT = "isstorebyconvert";
	public static final String ISELECTRANS = "iselectrans";
	public static final String DEF9 = "def9";
	public static final String ASSISTUNIT = "assistunit";
	public static final String DEF6 = "def6";
	public static final String DEF3 = "def3";
	public static final String DEF19 = "def19";
	public static final String FREE4 = "free4";
	public static final String INVCODE = "invcode";
	public static final String SHIPUNITNUM = "shipunitnum";
	public static final String AUTOBALANCEMEAS = "autobalancemeas";
	public static final String DEF10 = "def10";
	public static final String ISRETAIL = "isretail";
	public static final String DEF12 = "def12";
	public static final String INVBARCODE = "invbarcode";
	public static final String INVNAME = "invname";
	public static final String INVSHORTNAME = "invshortname";
	public static final String PK_MEASDOC5 = "pk_measdoc5";
	public static final String DEF13 = "def13";
	public static final String CREATOR = "creator";
	public static final String UNITVOLUME = "unitvolume";
	public static final String PK_MEASDOC6 = "pk_measdoc6";
	public static final String UNITWEIGHT = "unitweight";
	public static final String DEF15 = "def15";
	public static final String PK_MEASDOC3 = "pk_measdoc3";
	public static final String DEF4 = "def4";
	public static final String HEIGHT = "height";
	public static final String MODIFIER = "modifier";
	public static final String LABORFLAG = "laborflag";
	public static final String WIDTH = "width";
	public static final String FREE1 = "free1";
	public static final String DEF20 = "def20";
			
	/**
	 * ����storeunitnum��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getStoreunitnum () {
		return storeunitnum;
	}   
	/**
	 * ����storeunitnum��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newStoreunitnum UFDouble
	 */
	public void setStoreunitnum (UFDouble newStoreunitnum ) {
	 	this.storeunitnum = newStoreunitnum;
	} 	  
	/**
	 * ����pk_corp��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * ����pk_corp��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����def17��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef17 () {
		return def17;
	}   
	/**
	 * ����def17��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef17 String
	 */
	public void setDef17 (String newDef17 ) {
	 	this.def17 = newDef17;
	} 	  
	/**
	 * ����pk_prodline��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_prodline () {
		return pk_prodline;
	}   
	/**
	 * ����pk_prodline��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_prodline String
	 */
	public void setPk_prodline (String newPk_prodline ) {
	 	this.pk_prodline = newPk_prodline;
	} 	  
	/**
	 * ����def16��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef16 () {
		return def16;
	}   
	/**
	 * ����def16��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef16 String
	 */
	public void setDef16 (String newDef16 ) {
	 	this.def16 = newDef16;
	} 	  
	/**
	 * ����free2��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getFree2 () {
		return free2;
	}   
	/**
	 * ����free2��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newFree2 String
	 */
	public void setFree2 (String newFree2 ) {
	 	this.free2 = newFree2;
	} 	  
	/**
	 * ����pk_taxitems��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_taxitems () {
		return pk_taxitems;
	}   
	/**
	 * ����pk_taxitems��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_taxitems String
	 */
	public void setPk_taxitems (String newPk_taxitems ) {
	 	this.pk_taxitems = newPk_taxitems;
	} 	  
	/**
	 * ����modifytime��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDateTime
	 */
	public UFDateTime getModifytime () {
		return modifytime;
	}   
	/**
	 * ����modifytime��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newModifytime UFDateTime
	 */
	public void setModifytime (UFDateTime newModifytime ) {
	 	this.modifytime = newModifytime;
	} 	  
	/**
	 * ����sealflag��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getSealflag () {
		return sealflag;
	}   
	/**
	 * ����sealflag��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newSealflag String
	 */
	public void setSealflag (String newSealflag ) {
	 	this.sealflag = newSealflag;
	} 	  
	/**
	 * ����free5��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getFree5 () {
		return free5;
	}   
	/**
	 * ����free5��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newFree5 String
	 */
	public void setFree5 (String newFree5 ) {
	 	this.free5 = newFree5;
	} 	  
	/**
	 * ����def11��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef11 () {
		return def11;
	}   
	/**
	 * ����def11��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef11 String
	 */
	public void setDef11 (String newDef11 ) {
	 	this.def11 = newDef11;
	} 	  
	/**
	 * ����invtype��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvtype () {
		return invtype;
	}   
	/**
	 * ����invtype��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvtype String
	 */
	public void setInvtype (String newInvtype ) {
	 	this.invtype = newInvtype;
	} 	  
	/**
	 * ����def2��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef2 String
	 */
	public void setDef2 (String newDef2 ) {
	 	this.def2 = newDef2;
	} 	  
	/**
	 * ����asset��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getAsset () {
		return asset;
	}   
	/**
	 * ����asset��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newAsset UFBoolean
	 */
	public void setAsset (UFBoolean newAsset ) {
	 	this.asset = newAsset;
	} 	  
	/**
	 * ����ismngstockbygrswt��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getIsmngstockbygrswt () {
		return ismngstockbygrswt;
	}   
	/**
	 * ����ismngstockbygrswt��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newIsmngstockbygrswt UFBoolean
	 */
	public void setIsmngstockbygrswt (UFBoolean newIsmngstockbygrswt ) {
	 	this.ismngstockbygrswt = newIsmngstockbygrswt;
	} 	  
	/**
	 * ����def1��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef1 String
	 */
	public void setDef1 (String newDef1 ) {
	 	this.def1 = newDef1;
	} 	  
	/**
	 * ����graphid��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getGraphid () {
		return graphid;
	}   
	/**
	 * ����graphid��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newGraphid String
	 */
	public void setGraphid (String newGraphid ) {
	 	this.graphid = newGraphid;
	} 	  
	/**
	 * ����forinvname��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getForinvname () {
		return forinvname;
	}   
	/**
	 * ����forinvname��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newForinvname String
	 */
	public void setForinvname (String newForinvname ) {
	 	this.forinvname = newForinvname;
	} 	  
	/**
	 * ����free3��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getFree3 () {
		return free3;
	}   
	/**
	 * ����free3��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newFree3 String
	 */
	public void setFree3 (String newFree3 ) {
	 	this.free3 = newFree3;
	} 	  
	/**
	 * ����invmnecode��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvmnecode () {
		return invmnecode;
	}   
	/**
	 * ����invmnecode��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvmnecode String
	 */
	public void setInvmnecode (String newInvmnecode ) {
	 	this.invmnecode = newInvmnecode;
	} 	  
	/**
	 * ����pk_invbasdoc��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * ����pk_invbasdoc��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * ����memo��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getMemo () {
		return memo;
	}   
	/**
	 * ����memo��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newMemo String
	 */
	public void setMemo (String newMemo ) {
	 	this.memo = newMemo;
	} 	  
	/**
	 * ����createtime��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDateTime
	 */
	public UFDateTime getCreatetime () {
		return createtime;
	}   
	/**
	 * ����createtime��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newCreatetime UFDateTime
	 */
	public void setCreatetime (UFDateTime newCreatetime ) {
	 	this.createtime = newCreatetime;
	} 	  
	/**
	 * ����pk_invcl��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_invcl () {
		return pk_invcl;
	}   
	/**
	 * ����pk_invcl��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_invcl String
	 */
	public void setPk_invcl (String newPk_invcl ) {
	 	this.pk_invcl = newPk_invcl;
	} 	  
	/**
	 * ����pk_assetcategory��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_assetcategory () {
		return pk_assetcategory;
	}   
	/**
	 * ����pk_assetcategory��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_assetcategory String
	 */
	public void setPk_assetcategory (String newPk_assetcategory ) {
	 	this.pk_assetcategory = newPk_assetcategory;
	} 	  
	/**
	 * ����def18��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef18 () {
		return def18;
	}   
	/**
	 * ����def18��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef18 String
	 */
	public void setDef18 (String newDef18 ) {
	 	this.def18 = newDef18;
	} 	  
	/**
	 * ����setpartsflag��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getSetpartsflag () {
		return setpartsflag;
	}   
	/**
	 * ����setpartsflag��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newSetpartsflag UFBoolean
	 */
	public void setSetpartsflag (UFBoolean newSetpartsflag ) {
	 	this.setpartsflag = newSetpartsflag;
	} 	  
	/**
	 * ����pk_measdoc2��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc2 () {
		return pk_measdoc2;
	}   
	/**
	 * ����pk_measdoc2��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc2 String
	 */
	public void setPk_measdoc2 (String newPk_measdoc2 ) {
	 	this.pk_measdoc2 = newPk_measdoc2;
	} 	  
	/**
	 * ����pk_measdoc��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc () {
		return pk_measdoc;
	}   
	/**
	 * ����pk_measdoc��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc String
	 */
	public void setPk_measdoc (String newPk_measdoc ) {
	 	this.pk_measdoc = newPk_measdoc;
	} 	  
	/**
	 * ����def7��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef7 String
	 */
	public void setDef7 (String newDef7 ) {
	 	this.def7 = newDef7;
	} 	  
	/**
	 * ����weitunitnum��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getWeitunitnum () {
		return weitunitnum;
	}   
	/**
	 * ����weitunitnum��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newWeitunitnum UFDouble
	 */
	public void setWeitunitnum (UFDouble newWeitunitnum ) {
	 	this.weitunitnum = newWeitunitnum;
	} 	  
	/**
	 * ����invpinpai��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvpinpai () {
		return invpinpai;
	}   
	/**
	 * ����invpinpai��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvpinpai String
	 */
	public void setInvpinpai (String newInvpinpai ) {
	 	this.invpinpai = newInvpinpai;
	} 	  
	/**
	 * ����discountflag��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getDiscountflag () {
		return discountflag;
	}   
	/**
	 * ����discountflag��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDiscountflag UFBoolean
	 */
	public void setDiscountflag (UFBoolean newDiscountflag ) {
	 	this.discountflag = newDiscountflag;
	} 	  
	/**
	 * ����pk_measdoc1��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc1 () {
		return pk_measdoc1;
	}   
	/**
	 * ����pk_measdoc1��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc1 String
	 */
	public void setPk_measdoc1 (String newPk_measdoc1 ) {
	 	this.pk_measdoc1 = newPk_measdoc1;
	} 	  
	/**
	 * ����def8��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef8 String
	 */
	public void setDef8 (String newDef8 ) {
	 	this.def8 = newDef8;
	} 	  
	/**
	 * ����length��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getLength () {
		return length;
	}   
	/**
	 * ����length��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newLength String
	 */
	public void setLength (String newLength ) {
	 	this.length = newLength;
	} 	  
	/**
	 * ����def5��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef5 String
	 */
	public void setDef5 (String newDef5 ) {
	 	this.def5 = newDef5;
	} 	  
	/**
	 * ����invspec��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvspec () {
		return invspec;
	}   
	/**
	 * ����invspec��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvspec String
	 */
	public void setInvspec (String newInvspec ) {
	 	this.invspec = newInvspec;
	} 	  
	/**
	 * ����def14��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef14 () {
		return def14;
	}   
	/**
	 * ����def14��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef14 String
	 */
	public void setDef14 (String newDef14 ) {
	 	this.def14 = newDef14;
	} 	  
	/**
	 * ����isstorebyconvert��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getIsstorebyconvert () {
		return isstorebyconvert;
	}   
	/**
	 * ����isstorebyconvert��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newIsstorebyconvert UFBoolean
	 */
	public void setIsstorebyconvert (UFBoolean newIsstorebyconvert ) {
	 	this.isstorebyconvert = newIsstorebyconvert;
	} 	  
	/**
	 * ����iselectrans��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getIselectrans () {
		return iselectrans;
	}   
	/**
	 * ����iselectrans��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newIselectrans UFBoolean
	 */
	public void setIselectrans (UFBoolean newIselectrans ) {
	 	this.iselectrans = newIselectrans;
	} 	  
	/**
	 * ����def9��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef9 () {
		return def9;
	}   
	/**
	 * ����def9��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef9 String
	 */
	public void setDef9 (String newDef9 ) {
	 	this.def9 = newDef9;
	} 	  
	/**
	 * ����assistunit��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getAssistunit () {
		return assistunit;
	}   
	/**
	 * ����assistunit��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newAssistunit UFBoolean
	 */
	public void setAssistunit (UFBoolean newAssistunit ) {
	 	this.assistunit = newAssistunit;
	} 	  
	/**
	 * ����def6��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef6 String
	 */
	public void setDef6 (String newDef6 ) {
	 	this.def6 = newDef6;
	} 	  
	/**
	 * ����def3��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef3 String
	 */
	public void setDef3 (String newDef3 ) {
	 	this.def3 = newDef3;
	} 	  
	/**
	 * ����def19��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef19 () {
		return def19;
	}   
	/**
	 * ����def19��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef19 String
	 */
	public void setDef19 (String newDef19 ) {
	 	this.def19 = newDef19;
	} 	  
	/**
	 * ����free4��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getFree4 () {
		return free4;
	}   
	/**
	 * ����free4��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newFree4 String
	 */
	public void setFree4 (String newFree4 ) {
	 	this.free4 = newFree4;
	} 	  
	/**
	 * ����invcode��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvcode () {
		return invcode;
	}   
	/**
	 * ����invcode��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvcode String
	 */
	public void setInvcode (String newInvcode ) {
	 	this.invcode = newInvcode;
	} 	  
	/**
	 * ����shipunitnum��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getShipunitnum () {
		return shipunitnum;
	}   
	/**
	 * ����shipunitnum��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newShipunitnum UFDouble
	 */
	public void setShipunitnum (UFDouble newShipunitnum ) {
	 	this.shipunitnum = newShipunitnum;
	} 	  
	/**
	 * ����autobalancemeas��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getAutobalancemeas () {
		return autobalancemeas;
	}   
	/**
	 * ����autobalancemeas��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newAutobalancemeas UFBoolean
	 */
	public void setAutobalancemeas (UFBoolean newAutobalancemeas ) {
	 	this.autobalancemeas = newAutobalancemeas;
	} 	  
	/**
	 * ����def10��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef10 () {
		return def10;
	}   
	/**
	 * ����def10��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef10 String
	 */
	public void setDef10 (String newDef10 ) {
	 	this.def10 = newDef10;
	} 	  
	/**
	 * ����isretail��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getIsretail () {
		return isretail;
	}   
	/**
	 * ����isretail��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newIsretail UFBoolean
	 */
	public void setIsretail (UFBoolean newIsretail ) {
	 	this.isretail = newIsretail;
	} 	  
	/**
	 * ����def12��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef12 () {
		return def12;
	}   
	/**
	 * ����def12��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef12 String
	 */
	public void setDef12 (String newDef12 ) {
	 	this.def12 = newDef12;
	} 	  
	/**
	 * ����invbarcode��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvbarcode () {
		return invbarcode;
	}   
	/**
	 * ����invbarcode��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvbarcode String
	 */
	public void setInvbarcode (String newInvbarcode ) {
	 	this.invbarcode = newInvbarcode;
	} 	  
	/**
	 * ����invname��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvname () {
		return invname;
	}   
	/**
	 * ����invname��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvname String
	 */
	public void setInvname (String newInvname ) {
	 	this.invname = newInvname;
	} 	  
	/**
	 * ����invshortname��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getInvshortname () {
		return invshortname;
	}   
	/**
	 * ����invshortname��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newInvshortname String
	 */
	public void setInvshortname (String newInvshortname ) {
	 	this.invshortname = newInvshortname;
	} 	  
	/**
	 * ����pk_measdoc5��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc5 () {
		return pk_measdoc5;
	}   
	/**
	 * ����pk_measdoc5��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc5 String
	 */
	public void setPk_measdoc5 (String newPk_measdoc5 ) {
	 	this.pk_measdoc5 = newPk_measdoc5;
	} 	  
	/**
	 * ����def13��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef13 () {
		return def13;
	}   
	/**
	 * ����def13��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef13 String
	 */
	public void setDef13 (String newDef13 ) {
	 	this.def13 = newDef13;
	} 	  
	/**
	 * ����creator��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getCreator () {
		return creator;
	}   
	/**
	 * ����creator��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newCreator String
	 */
	public void setCreator (String newCreator ) {
	 	this.creator = newCreator;
	} 	  
	/**
	 * ����unitvolume��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getUnitvolume () {
		return unitvolume;
	}   
	/**
	 * ����unitvolume��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newUnitvolume UFDouble
	 */
	public void setUnitvolume (UFDouble newUnitvolume ) {
	 	this.unitvolume = newUnitvolume;
	} 	  
	/**
	 * ����pk_measdoc6��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc6 () {
		return pk_measdoc6;
	}   
	/**
	 * ����pk_measdoc6��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc6 String
	 */
	public void setPk_measdoc6 (String newPk_measdoc6 ) {
	 	this.pk_measdoc6 = newPk_measdoc6;
	} 	  
	/**
	 * ����unitweight��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getUnitweight () {
		return unitweight;
	}   
	/**
	 * ����unitweight��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newUnitweight UFDouble
	 */
	public void setUnitweight (UFDouble newUnitweight ) {
	 	this.unitweight = newUnitweight;
	} 	  
	/**
	 * ����def15��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef15 () {
		return def15;
	}   
	/**
	 * ����def15��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef15 String
	 */
	public void setDef15 (String newDef15 ) {
	 	this.def15 = newDef15;
	} 	  
	/**
	 * ����pk_measdoc3��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getPk_measdoc3 () {
		return pk_measdoc3;
	}   
	/**
	 * ����pk_measdoc3��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newPk_measdoc3 String
	 */
	public void setPk_measdoc3 (String newPk_measdoc3 ) {
	 	this.pk_measdoc3 = newPk_measdoc3;
	} 	  
	/**
	 * ����def4��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef4 String
	 */
	public void setDef4 (String newDef4 ) {
	 	this.def4 = newDef4;
	} 	  
	/**
	 * ����height��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getHeight () {
		return height;
	}   
	/**
	 * ����height��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newHeight String
	 */
	public void setHeight (String newHeight ) {
	 	this.height = newHeight;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFDouble
	 */
	public UFDouble getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDr UFDouble
	 */
	public void setDr (UFDouble newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����modifier��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getModifier () {
		return modifier;
	}   
	/**
	 * ����modifier��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newModifier String
	 */
	public void setModifier (String newModifier ) {
	 	this.modifier = newModifier;
	} 	  
	/**
	 * ����laborflag��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return UFBoolean
	 */
	public UFBoolean getLaborflag () {
		return laborflag;
	}   
	/**
	 * ����laborflag��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newLaborflag UFBoolean
	 */
	public void setLaborflag (UFBoolean newLaborflag ) {
	 	this.laborflag = newLaborflag;
	} 	  
	/**
	 * ����width��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getWidth () {
		return width;
	}   
	/**
	 * ����width��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newWidth String
	 */
	public void setWidth (String newWidth ) {
	 	this.width = newWidth;
	} 	  
	/**
	 * ����free1��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getFree1 () {
		return free1;
	}   
	/**
	 * ����free1��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newFree1 String
	 */
	public void setFree1 (String newFree1 ) {
	 	this.free1 = newFree1;
	} 	  
	/**
	 * ����def20��Getter����.
	 * ��������:2014-07-08 21:30:36
	 * @return String
	 */
	public String getDef20 () {
		return def20;
	}   
	/**
	 * ����def20��Setter����.
	 * ��������:2014-07-08 21:30:36
	 * @param newDef20 String
	 */
	public void setDef20 (String newDef20 ) {
	 	this.def20 = newDef20;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2014-07-08 21:30:36
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2014-07-08 21:30:36
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_invbasdoc";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2014-07-08 21:30:36
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_invbasdoc";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2014-07-08 21:30:36
	  */
     public INVBASDOCVO() {
		super();	
	}    
} 
