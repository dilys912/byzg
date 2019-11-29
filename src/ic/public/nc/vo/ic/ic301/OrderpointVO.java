package nc.vo.ic.ic301;

import java.util.ArrayList;
import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.*;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class OrderpointVO extends CircularlyAccessibleValueObject {

	public String getXhl() {
		return xhl;
	}

	public void setXhl(String xhl) {
		this.xhl = xhl;
	}

	public OrderpointVO() {
	}

	public OrderpointVO(String newCwarehouseid) {
		m_cwarehouseid = newCwarehouseid;
	}

	public String getEntityName() {
		return "Orderpoint";
	}

	public String getPrimaryKey() {
		return m_cwarehouseid;
	}

	public void setPrimaryKey(String newCwarehouseid) {
		m_cwarehouseid = newCwarehouseid;
	}

	public String getCwarehouseid() {
		return m_cwarehouseid;
	}

	public UFDate getBilldate() {
		return m_billdate;
	}

	public String getCinventoryid() {
		return m_cinventoryid;
	}

	public String getPk_corp() {
		return m_pk_corp;
	}

	public String getStorname() {
		return m_storname;
	}

	public String getInvcode() {
		return m_invcode;
	}

	public String getInvname() {
		return m_invname;
	}

	public String getInvspec() {
		return m_invspec;
	}

	public String getInvtype() {
		return m_invtype;
	}

	public String getMeasname() {
		return m_measname;
	}

	public String getAstmeaname() {
		return m_astmeaname;
	}

	public String getCastunitid() {
		return m_castunitid;
	}

	public String getCwhsmanager() {
		return m_cwhsmanager;
	}

	public String getCwhsmanagerid() {
		return m_cwhsmanagerid;
	}

	public UFDouble getRestnum() {
		return m_restnum;
	}

	public UFDouble getRestastnum() {
		return m_restastnum;
	}

	public UFDouble getNpraynum() {
		return m_npraynum;
	}

	public UFDouble getNordernum() {
		return m_nordernum;
	}

	public UFDouble getNaccumchecknum() {
		return m_naccumchecknum;
	}

	public UFDouble getNapplyordernum() {
		return m_napplyordernum;
	}

	public UFDouble getNapplyorderastnum() {
		return m_napplyorderastnum;
	}

	public String getApplypsnname() {
		return m_applypsnname;
	}

	public String getApplypsnid() {
		return m_applypsnid;
	}

	public String getMemo() {
		return m_memo;
	}

	public void setCwarehouseid(String newCwarehouseid) {
		m_cwarehouseid = newCwarehouseid;
	}

	public void setBilldate(UFDate newBilldate) {
		m_billdate = newBilldate;
	}

	public void setCinventoryid(String newCinventoryid) {
		m_cinventoryid = newCinventoryid;
	}

	public void setPk_corp(String newPk_corp) {
		m_pk_corp = newPk_corp;
	}

	public void setStorname(String newStorname) {
		m_storname = newStorname;
	}

	public void setInvcode(String newInvcode) {
		m_invcode = newInvcode;
	}

	public void setInvname(String newInvname) {
		m_invname = newInvname;
	}

	public void setInvspec(String newInvspec) {
		m_invspec = newInvspec;
	}

	public void setInvtype(String newInvtype) {
		m_invtype = newInvtype;
	}

	public void setMeasname(String newMeasname) {
		m_measname = newMeasname;
	}

	public void setAstmeaname(String newAstmeaname) {
		m_astmeaname = newAstmeaname;
	}

	public void setCastunitid(String newCastunitid) {
		m_castunitid = newCastunitid;
	}

	public void setCwhsmanager(String newCwhsmanager) {
		m_cwhsmanager = newCwhsmanager;
	}

	public void setCwhsmanagerid(String newCwhsmanagerid) {
		m_cwhsmanagerid = newCwhsmanagerid;
	}

	public void setRestnum(UFDouble newRestnum) {
		m_restnum = newRestnum;
	}

	public void setRestastnum(UFDouble newRestastnum) {
		m_restastnum = newRestastnum;
	}

	public void setNpraynum(UFDouble newNpraynum) {
		m_npraynum = newNpraynum;
	}

	public void setNordernum(UFDouble newNordernum) {
		m_nordernum = newNordernum;
	}

	public void setNaccumchecknum(UFDouble newNaccumchecknum) {
		m_naccumchecknum = newNaccumchecknum;
	}

	public void setNapplyordernum(UFDouble newNapplyordernum) {
		m_napplyordernum = newNapplyordernum;
	}

	public void setNapplyorderastnum(UFDouble newNapplyorderastnum) {
		m_napplyorderastnum = newNapplyorderastnum;
	}

	public void setApplypsnname(String newApplypsnname) {
		m_applypsnname = newApplypsnname;
	}

	public void setApplypsnid(String newApplypsnid) {
		m_applypsnid = newApplypsnid;
	}

	public void setMemo(String newMemo) {
		m_memo = newMemo;
	}

	public void validate() throws ValidationException {
		ArrayList errFields = new ArrayList();
		if (m_cwarehouseid == null) errFields.add(new String("m_cwarehouseid"));
		StringBuffer message = new StringBuffer();
		message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec", "UPP4008spec-000495"));
		if (errFields.size() > 0) {
			String temp[] = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec", "UPPSCMCommon-000000"));
				message.append(temp[i]);
			}

			throw new NullFieldException(message.toString());
		} else {
			return;
		}
	}

	public String[] getAttributeNames() {
		return (new String[] {
				"cwarehouseid", "billdate", "pk_invbasdoc", "cinventoryid", "pk_corp", "storeorgid", "storname", "invcode", "invname", "invspec", "invtype", "measname", "astmeaname", "castunitid", "cuname", "pk_cumandoc", "nlastoutnum", "purchasebusitype", "cwhsmanager", "cwhsmanagerid", "restnum", "restastnum", "xhl", "orderpointnum", "npraynum", "nordernum", "naccumchecknum", "napplyordernum", "napplyorderastnum", "nmaxstocknum", "neconomicnum", "nforeinnum", "nwwnum", "applypsnname", "applypsnid", "memo", "matertype", "nplanedprice"
				// add by zip: 2014/3/4 No 30
				,"xyy_reqdate_b"
				// add end
				// add by zip:2014/3/28 No 37 and 29
				,"xyy_prayundo","xyy_praydounorder","xyy_orderunarr","xyy_arruncheck","xyy_free","xyy_canuse"
				// add end
		});
	}

	public Object getAttributeValue(String attributeName) {
		// add by zip:2014/3/4
		if (attributeName.equals("xyy_reqdate_b")) return xyy_reqdate_b;
		// add end
		
		// add by zip:2014/3/28 No 37 and 29
		if (attributeName.equals("xyy_prayundo")) return xyy_prayundo;
		if (attributeName.equals("xyy_praydounorder")) return xyy_praydounorder;
		if (attributeName.equals("xyy_orderunarr")) return xyy_orderunarr;
		if (attributeName.equals("xyy_arruncheck")) return xyy_arruncheck;
		if (attributeName.equals("xyy_free")) return xyy_free;
		if (attributeName.equals("xyy_canuse")) return xyy_canuse;
		// add end
		if (attributeName.equals("cwarehouseid")) return m_cwarehouseid;
		if (attributeName.equals("billdate")) return m_billdate;
		if (attributeName.equals("xhl")) return xhl;
		if (attributeName.equals("nplanedprice")) return nplanedprice;
		if (attributeName.equals("pk_invbasdoc")) return m_pk_invbasdoc;
		if (attributeName.equals("cinventoryid")) return m_cinventoryid;
		if (attributeName.equals("pk_corp")) return m_pk_corp;
		if (attributeName.equals("storname")) return m_storname;
		if (attributeName.equals("storeorgid")) return m_storeorgid;
		if (attributeName.equals("invcode")) return m_invcode;
		if (attributeName.equals("invname")) return m_invname;
		if (attributeName.equals("invspec")) return m_invspec;
		if (attributeName.equals("invtype")) return m_invtype;
		if (attributeName.equals("measname")) return m_measname;
		if (attributeName.equals("astmeaname")) return m_astmeaname;
		if (attributeName.equals("castunitid")) return m_castunitid;
		if (attributeName.equals("cwhsmanager")) return m_cwhsmanager;
		if (attributeName.equals("cwhsmanagerid")) return m_cwhsmanagerid;
		if (attributeName.equals("purchasebusitype")) return m_cpurchasebusitype;
		if (attributeName.equals("cuname")) return m_cuname;
		if (attributeName.equals("pk_cumandoc")) return m_cpk_cumandoc;
		if (attributeName.equals("nlastoutnum")) return m_nlastoutnum;
		if (attributeName.equals("restnum")) return m_restnum;
		if (attributeName.equals("restastnum")) return m_restastnum;
		if (attributeName.equals("norderpointnum")) return m_norderpointnum;
		if (attributeName.equals("npraynum")) return m_npraynum;
		if (attributeName.equals("nordernum")) return m_nordernum;
		if (attributeName.equals("naccumchecknum")) return m_naccumchecknum;
		if (attributeName.equals("napplyordernum")) return m_napplyordernum;
		if (attributeName.equals("napplyorderastnum")) return m_napplyorderastnum;
		if (attributeName.equals("nmaxstocknum")) return m_nmaxstocknum;
		if (attributeName.equals("neconomicnum")) return m_neconomicnum;
		if (attributeName.equals("minbatchnum")) return m_nminbatchnum;
		if (attributeName.equals("nforeinnum")) return m_nforeinnum;
		if (attributeName.equals("nwwnum")) return m_nwwnum;
		if (attributeName.equals("applypsnname")) return m_applypsnname;
		if (attributeName.equals("applypsnid")) return m_applypsnid;
		if (attributeName.equals("memo")) return m_memo;
		if (attributeName.equals("matertype")) return m_matertype;
		if (attributeName.equals("ntranapplynum")) return m_ntranapplynum;
		else return null;
	}

	public UFDouble getNplanedprice() {
		return nplanedprice;
	}

	public void setNplanedprice(UFDouble nplanedprice) {
		this.nplanedprice = nplanedprice;
	}

	public void setAttributeValue(String name, Object value) {
		if (value != null) value = value.toString().trim();
		try {
			if (name.equals("cwarehouseid")) m_cwarehouseid = (String) value;
			else if (name.equals("billdate")) m_billdate = SwitchObject.switchObjToUFDate(value);
			else if (name.equals("nplanedprice")) nplanedprice = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("pk_invbasdoc")) m_pk_invbasdoc = (String) value;
			else if (name.equals("cinventoryid")) m_cinventoryid = (String) value;
			else if (name.equals("pk_corp")) m_pk_corp = (String) value;
			else if (name.equals("storeorgid")) m_storeorgid = (String) value;
			else if (name.equals("storname")) m_storname = (String) value;
			else if (name.equals("invcode")) m_invcode = (String) value;
			else if (name.equals("invname")) m_invname = (String) value;
			else if (name.equals("invspec")) m_invspec = (String) value;
			else if (name.equals("invtype")) m_invtype = (String) value;
			else if (name.equals("measname")) m_measname = (String) value;
			else if (name.equals("astmeaname")) m_astmeaname = (String) value;
			else if (name.equals("castunitid")) m_castunitid = (String) value;
			else if (name.equals("cwhsmanager")) m_cwhsmanager = (String) value;
			else if (name.equals("cwhsmanagerid")) m_cwhsmanagerid = (String) value;
			else if (name.equals("cuname")) m_cuname = (String) value;
			else if (name.equals("pk_cumandoc")) m_cpk_cumandoc = (String) value;
			else if (name.equals("purchasebusitype")) m_cpurchasebusitype = (String) value;
			else if (name.equals("nlastoutnum")) m_nlastoutnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("restnum")) m_restnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("restastnum")) m_restastnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("norderpointnum")) m_norderpointnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("npraynum")) m_npraynum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("nordernum")) m_nordernum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("naccumchecknum")) m_naccumchecknum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("napplyordernum")) m_napplyordernum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("napplyorderastnum")) m_napplyorderastnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("nmaxstocknum")) m_nmaxstocknum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("neconomicnum")) m_neconomicnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("minbatchnum")) m_nminbatchnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("nforeinnum")) m_nforeinnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("nwwnum")) m_nwwnum = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("applypsnname")) m_applypsnname = (String) value;
			else if (name.equals("applypsnid")) m_applypsnid = (String) value;
			else if (name.equals("memo")) m_memo = (String) value;
			else if (name.equals("matertype")) m_matertype = (String) value;
			else if (name.equals("ntranapplynum")) m_ntranapplynum = value != null ? new UFDouble(value.toString()) : null;
			// add by zip:2014/3/4
			else if (name.equals("xyy_reqdate_b")) xyy_reqdate_b = value != null ? new UFDate(value.toString()) : null;
			// add end
			// add by zip:2014/3/28 No 37 and 29
			else if (name.equals("xyy_prayundo")) xyy_prayundo = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("xyy_praydounorder")) xyy_praydounorder = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("xyy_orderunarr")) xyy_orderunarr = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("xyy_arruncheck")) xyy_arruncheck = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("xyy_free")) xyy_free = value != null ? new UFDouble(value.toString()) : null;
			else if (name.equals("xyy_canuse")) xyy_canuse = value != null ? new UFDouble(value.toString()) : null;
			// add end
		} catch (ClassCastException e) {
			throw new ClassCastException((new StringBuilder(String.valueOf(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec", "UPP4008spec-000502")))).append(name).append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec", "UPP4008spec-000496")).append(value).append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec", "UPP4008spec-000497")).toString());
		}
	}

	public String getMatertype() {
		return m_matertype;
	}

	public void setMatertype(String newM_matertype) {
		m_matertype = newM_matertype;
	}

	private String m_cwarehouseid;
	private UFDate m_billdate;
	private String m_cinventoryid;
	private String m_pk_corp;
	private String m_storname;
	private String m_invcode;
	private String m_invname;
	private String m_invspec;
	private String m_invtype;
	private String m_measname;
	private String m_astmeaname;
	private String m_castunitid;
	private String m_cwhsmanager;
	private String m_cwhsmanagerid;
	private UFDouble m_restnum;
	private UFDouble m_restastnum;
	private UFDouble m_npraynum;
	private UFDouble m_nordernum;
	private UFDouble m_naccumchecknum;
	private UFDouble m_napplyordernum;
	private UFDouble m_napplyorderastnum;
	private String m_applypsnname;
	private String m_applypsnid;
	private String m_memo;
	private String m_cpk_cumandoc;
	private String m_cpurchasebusitype;
	private String m_cuname;
	private String m_matertype;
	private UFDouble m_neconomicnum;
	private UFDouble m_nforeinnum;
	private UFDouble m_nlastoutnum;
	private UFDouble m_nmaxstocknum;
	private UFDouble m_nminbatchnum;
	private UFDouble m_norderpointnum;
	private UFDouble m_ntranapplynum;
	private UFDouble m_nwwnum;
	private String m_pk_invbasdoc;
	private String m_storeorgid;
	private String xhl;
	private UFDouble nplanedprice;

	// add by zip:2014/3/4 No 30
	private UFDate xyy_reqdate_b;

	public UFDate getXyy_reqdate_b() {
		return xyy_reqdate_b;
	}

	public void setXyy_reqdate_b(UFDate xyy_reqdate_b) {
		this.xyy_reqdate_b = xyy_reqdate_b;
	}

	// add end
	
	
	// add by zip:2014/3/28 No 37 and 29
	private UFDouble xyy_prayundo;// 已申购未审批
	private UFDouble xyy_praydounorder;// 已审批未订购
	private UFDouble xyy_orderunarr;// 已订购未到货
	private UFDouble xyy_arruncheck;// 已到货未检验
	private UFDouble xyy_free;// 已冻结
	private UFDouble xyy_canuse;// 可拨库存量

	public UFDouble getXyy_prayundo() {
		return xyy_prayundo;
	}

	public void setXyy_prayundo(UFDouble xyy_prayundo) {
		this.xyy_prayundo = xyy_prayundo;
	}

	public UFDouble getXyy_praydounorder() {
		return xyy_praydounorder;
	}

	public void setXyy_praydounorder(UFDouble xyy_praydounorder) {
		this.xyy_praydounorder = xyy_praydounorder;
	}

	public UFDouble getXyy_orderunarr() {
		return xyy_orderunarr;
	}

	public void setXyy_orderunarr(UFDouble xyy_orderunarr) {
		this.xyy_orderunarr = xyy_orderunarr;
	}

	public UFDouble getXyy_arruncheck() {
		return xyy_arruncheck;
	}

	public void setXyy_arruncheck(UFDouble xyy_arruncheck) {
		this.xyy_arruncheck = xyy_arruncheck;
	}

	public UFDouble getXyy_free() {
		return xyy_free;
	}

	public void setXyy_free(UFDouble xyy_free) {
		this.xyy_free = xyy_free;
	}

	public UFDouble getXyy_canuse() {
		return xyy_canuse;
	}

	public void setXyy_canuse(UFDouble xyy_canuse) {
		this.xyy_canuse = xyy_canuse;
	}
	
	// add end
	
	
	
	
	
}