/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package nc.ui.pf.changedir;

import nc.bs.pf.change.VOConversion;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pf.change.UserDefineFunction;

public class CHG7XTO4C extends VOConversion {

	public CHG7XTO4C() {
	}

	public String getAfterClassName() {
		return "nc.bs.ic.pub.pfconv.HardLockChgVO";
	}

	public String getOtherClassName() {
		return "nc.ui.ic.pub.pfconv.HardLockChgVO";
	}

	// add by zip: 2014/3/20
	// hidden exception message
	// comment by zip:2014/4/10
	// 发现返回null会引发问题,注释此方法
//	public IchangeVO getAfterClass() {
//		if (getAfterClassName() == null || getAfterClassName().trim().length() == 0) return null;
//		try {
//			Object afterClassInstance = PfUtilTools.newVoMappingObject(getSourceBilltype(), getDestBilltype(), getAfterClassName());
//			return (IchangeVO) afterClassInstance;
//		} catch (Exception ex) {
//			return null;
//		}
//	}
	// add end

	public String[] getField() {
		return (new String[] {
				"H_vdiliveraddress->B_vdestaddress", "B_vdiliveraddress->B_vdestaddress", "H_ccustomerid->B_pkcusmandoc", "H_coperatorid->H_userid", "H_dbilldate->H_billdate", "H_cwarehouseid->B_pkstroe", "B_cwarehouseid->B_pkstroe", "H_pk_corp->B_pksalecorp", "H_pk_calbody->B_pksendstockorg", "H_cdispatcherid->B_cdispatcherid", "H_cbilltypecode->B_voutbilltype", "H_cbizid->B_pkoperator", "H_cdptid->B_pkoprdepart", "H_cdilivertypeid->H_pkdelivmode", "H_vdiliveraddress->B_vdestaddress", "H_cbiztype->B_cbiztype", "H_cwastewarehouseid->H_pktrancust", "H_cwhmanagerid->B_pkwhmanager", "B_csourcetype->B_csourcetype", "B_nshouldoutnum->B_dinvnum", "B_nshouldoutassistnum->B_dinvassist", "B_csourcebillhid->B_pk_delivbill_h", "B_csourcebillbid->B_pk_delivbill_b", "B_cfirstbillbid->B_pkorderrow", "B_cfirstbillhid->B_pkorder", "B_cfirsttype->B_vbilltype", "B_csourcetype->B_csourcetype", "B_vsourcebillcode->H_vdelivbillcode", "B_cinventoryid->B_pkinv", "B_castunitid->B_pkassistmeasure", "B_vfree1->B_vfree1", "B_vfree2->B_vfree2", "B_vfree3->B_vfree3", "B_vfree4->B_vfree4", "B_vfree5->B_vfree5", "B_vfree6->B_vfree6", "B_vfree7->B_vfree7", "B_vfree8->B_vfree8", "B_vfree9->B_vfree9", "B_vfree10->B_vfree10", "B_vbatchcode->B_vbatchcode",
				//begin ncm cuijf1 201302271410149679_包装实施_发运单生成销售出库单赠品标志没有跟过来
				//    		"B_flargess->B_bpresent",
				"B_flargess->B_blargess",
				//end ncm cuijf1 201302271410149679_包装实施_发运单生成销售出库单赠品标志没有跟过来 
				"B_bsourcelargess->B_bpresent",
				"B_creceieveid->B_creceiptcorpid",
				"B_vnotebody->B_vnote",
				"B_vsourcerowno->B_irownumber",
				"B_cprojectid->B_pkitem",
				"B_cprojectphaseid->B_pkitemperiod",
				"B_cquoteunitid->B_cquoteunitid",
				"B_nquoteunitrate->B_nquoteunitrate",
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
				"H_pk_defdoc13->H_pk_defdoc12",
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
				"B_pk_defdoc20->B_pk_defdoc19"
		});
	}

	public String[] getFormulas() {
		return (new String[] {
			"B_vfirstrowno->iif(B_vbilltype=\"30\",getColValue(so_saleorder_b, crowno, corder_bid, B_pkorderrow),getColValue(to_bill_b, crowno, cbill_bid, B_pkorderrow))"
		});
	}

	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}

/*
	DECOMPILATION REPORT

	Decompiled from: D:\UFIDA\NC5011_old\modules\ic\META-INF\classes/nc/bs/pf/changedir/CHG7XTO4C.class
	Total time: 258 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/