package nc.ui.by.invapp.h0h001;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.pub.IVOTreeDataByCode;
import nc.vo.by.invapp.h0h001.SHOWVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class TreeCardData implements IVOTreeDataByCode {
	
	@SuppressWarnings("unchecked")
	public SuperVO[] getTreeVO() {
		SuperVO[] treeVOs = null;
		StringBuffer sb = new StringBuffer();
		sb = getSql(null);
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			List<SHOWVO> list = (List<SHOWVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(SHOWVO.class));
			treeVOs = list.toArray(new SHOWVO[0]);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return treeVOs;
	}

	public StringBuffer getSql(Object pk_invcl) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select cl.pk_invcl, ") 
		.append("        cl.invclasscode||' '||cl.invclassname invclassnameaaa , ") 
		.append("        cl.invclasscode, ") 
		.append("        cl.invclassname, ") 
		.append("        cl.invclasscode invclasscode1, ") 
		.append("        cl.invclassname invclassname1, ") 
		.append("        cl.invclasscode invclasscode2, ") 
		.append("        cl.invclassname invclassname2, ") 
		.append("        cl.invclasscode invclasscode3, ") 
		.append("        cl.invclassname invclassname3, ") 
		.append("        cl.invclasslev,  ")
		.append("        cl.dr,att.pk_invclattribute, ") 
		.append("        att.abcfundeg, ") 
		.append("        att.abcgrosspft, ") 
		.append("        att.abcpurchase, ") 
		.append("        att.abctype, ") 
		.append("        att.abssales, ") 
		.append("        att.accflag, ") 
		.append("        att.accquiretime, ") 
		.append("        att.aheadbatch, ") 
		.append("        att.aheadcoff, ") 
		.append("        att.assistunit, ") 
		.append("        att.autobalancemeas, ") 
		.append("        att.batchperiodnum, ") 
		.append("        att.batchrule, ") 
		.append("        att.batchrule_show, ") 
		.append("        att.blgdsczkxs, ") 
		.append("        att.bomtype, ") 
		.append("        att.bomtype_show, ") 
		.append("        att.cgzz, ") 
		.append("        att.chkfreeflag, ") 
		.append("        att.chngamount, ") 
		.append("        att.ckcb, ") 
		.append("        att.ckjlcname, ") 
		.append("        att.cksj, ") 
		.append("        att.combineflag, ") 
		.append("        att.confirmtime, ") 
		.append("        att.converseflag, ") 
		.append("        att.costprice, ") 
		.append("        att.currentamount, ") 
		.append("        att.datumofsend, ") 
		.append("        att.def1, ") 
		.append("        att.def10, ") 
		.append("        att.def11, ") 
		.append("        att.def12, ") 
		.append("        att.def13, ") 
		.append("        att.def14, ") 
		.append("        att.def15, ") 
		.append("        att.def16, ") 
		.append("        att.def17, ") 
		.append("        att.def18, ") 
		.append("        att.def19, ") 
		.append("        att.def2, ") 
		.append("        att.def20, ") 
		.append("        att.def3, ") 
		.append("        att.def4, ") 
		.append("        att.def5, ") 
		.append("        att.def6, ") 
		.append("        att.def7, ") 
		.append("        att.def8, ") 
		.append("        att.def9, ") 
		.append("        att.discountflag, ") 
		.append("        att.ecobatch, ") 
		.append("        att.endahead, ") 
		.append("        att.expaybacktax, ") 
		.append("        att.fcpclgsfa, ") 
		.append("        att.fcpclgsfa_show, ") 
		.append("        att.fgys, ") 
		.append("        att.fixedahead, ") 
		.append("        att.fixperiodbegin, ") 
		.append("        att.flanlennum, ") 
		.append("        att.flanmadenum, ") 
		.append("        att.flanwidenum, ") 
		.append("        att.forinvname, ") 
		.append("        att.free1, ") //集团自由项1
		.append("        att.free2, ") 
		.append("        att.free3, ") 
		.append("        att.free4, ") 
		.append("        att.free5, ") 
		.append("        att.gfwlbm, ") 
		.append("        att.gldef1, ") 
		.append("        att.gldef10, ") 
		.append("        att.gldef11, ") 
		.append("        att.gldef12, ") 
		.append("        att.gldef13, ") 
		.append("        att.gldef14, ") 
		.append("        att.gldef15, ") 
		.append("        att.gldef16, ") 
		.append("        att.gldef17, ") 
		.append("        att.gldef18, ") 
		.append("        att.gldef19, ") 
		.append("        att.gldef2, ") 
		.append("        att.gldef20, ") 
		.append("        att.gldef3, ") 
		.append("        att.gldef4, ") 
		.append("        att.gldef5, ") 
		.append("        att.gldef6, ") 
		.append("        att.gldef7, ") 
		.append("        att.gldef8, ") 
		.append("        att.gldef9, ") 
		.append("        att.glfree1, ") 
		.append("        att.glfree2, ") 
		.append("        att.glfree3, ") 
		.append("        att.glfree4, ") 
		.append("        att.glfree5, ") 
		.append("        att.graphid, ") 
		.append("        att.grosswtnum, ") 
		.append("        att.height, ") 
		.append("        att.invbarcode, ") 
		.append("        att.invcode, ") 
		.append("        att.invlifeperiod, ") 
		.append("        att.invmnecode, ") 
		.append("        att.invname, ") 
		.append("        att.invpinpai, ") 
		.append("        att.invshortname, ") 
		.append("        att.invspec, ") 
		.append("        att.invtype, ") 
		.append("        att.isappendant, ") 
		.append("        att.isautoatpcheck, ") 
		.append("        att.iscancalculatedinvcost, ") 
		.append("        att.iscanpurchased, ") 
		.append("        att.iscansaleinvoice, ") 
		.append("        att.iscansold, ") 
		.append("        att.isconfigable, ") 
		.append("        att.iscostbyorder, ") 
		.append("        att.iscreatesonprodorder, ") 
		.append("        att.isctlbyfixperiod, ") 
		.append("        att.isctlprodplanprice, ") 
		.append("        att.isctoutput, ") 
		.append("        att.iselementcheck, ") 
		.append("        att.isfatherofbom, ") 
		.append("        att.isfxjz, ") 
		.append("        att.isinvretfreeofchk, ") 
		.append("        att.isinvretinstobychk, ") 
		.append("        att.isinvreturned, ") 
		.append("        att.ismngstockbygrswt, ") 
		.append("        att.isnoconallowed, ") 
		.append("        att.isprimarybarcode, ") 
		.append("        att.issalable, ") 
		.append("        att.issecondarybarcode, ") 
		.append("        att.isselfapprsupplier, ") 
		.append("        att.issend, ") 
		.append("        att.issendbydatum, ") 
		.append("        att.isspecialty, ") 
		.append("        att.isstorebyconvert, ") 
		.append("        att.issupplierstock, ") 
		.append("        att.isuseroute, ") 
		.append("        att.isvirtual, ") 
		.append("        att.iswholesetsend, ") 
		.append("        att.jhj, ") 
		.append("        att.jyrhzdyw, ") 
		.append("        att.jyrhzdyw_show, ") 
		.append("        att.keepwasterate, ") 
		.append("        att.laborflag, ") 
		.append("        att.length, ") 
		.append("        att.lowestprice, ") 
		.append("        att.lowlevelcode, ") 
		.append("        att.lowstocknum, ") 
		.append("        att.mainmeasuredoc, ") 
		.append("        att.mantaxitem, ") 
		.append("        att.materclass, ") 
		.append("        att.materclass_show, ") 
		.append("        att.materstate, ") 
		.append("        att.materstate_show, ") 
		.append("        att.matertype, ") 
		.append("        att.matertype_show, ") 
		.append("        att.maxprice, ") 
		.append("        att.maxstornum, ") 
		.append("        att.minmulnum, ") 
		.append("        att.nbzyj, ") 
		.append("        att.negallowed, ") 
		.append("        att.netwtnum, ") 
		.append("        att.nyzbmxs, ") 
		.append("        att.outnumhistorydays, ") 
		.append("        att.outpriority, ") 
		.append("        att.outtrackin, ") 
		.append("        att.outtype, ") 
		.append("        att.outtype_show, ") 
		.append("        att.pchscscd, ") 
		.append("        att.pchscscd_show, ") 
		.append("        att.pebegin, ") 
		.append("        att.peend, ") 
		.append("        att.pk_calbody, ") 
		.append("        att.pk_ckjlcid, ") 
		.append("        att.pk_cumandoc, ") 
		.append("        att.pk_deptdoc3, ") 
		.append("        att.pk_dftfactory, ") 
		.append("        att.pk_measdoc, ") 
		.append("        att.pk_measdoc1, ") 
		.append("        att.pk_measdoc2, ") 
		.append("        att.pk_measdoc3, ") 
		.append("        att.pk_measdoc5, ") 
		.append("        att.pk_prodline, ") 
		.append("        att.pk_psndoc3, ") 
		.append("        att.pk_psndoc4, ") 
		.append("        att.pk_rkjlcid, ") 
		.append("        att.pk_stordoc, ") 
		.append("        att.pk_taxitems, ") 
		.append("        att.planprice, ") 
		.append("        att.prevahead, ") 
		.append("        att.pricemethod, ") 
		.append("        att.pricemethod_show, ") 
		.append("        att.primaryflag, ") 
		.append("        att.primnessnum, ") 
		.append("        att.prodarea, ") 
		.append("        att.purchasestge, ") 
		.append("        att.purwasterate, ") 
		.append("        att.qualitymanflag, ") 
		.append("        att.rationwtnum, ") 
		.append("        att.realusableamount, ") 
		.append("        att.refsaleprice, ") 
		.append("        att.rkjlcname, ") 
		.append("        att.roadtype, ") 
		.append("        att.roadtype_show, ") 
		.append("        att.roundingnum, ") 
		.append("        att.safetystocknum, ") 
		.append("        att.scdef1, ") 
		.append("        att.scdef2, ") 
		.append("        att.scdef3, ") 
		.append("        att.scdef4, ") 
		.append("        att.scdef5, ") 
		.append("        att.scheattr, ") 
		.append("        att.scheattr_show, ") 
		.append("        att.scscddms, ") 
		.append("        att.scscddms_show, ") 
		.append("        att.scxybzsfzk, ") 
		.append("        att.sellproxyflag, ") 
		.append("        att.serialmanaflag, ") 
		.append("        att.setpartsflag, ") 
		.append("        att.sfcbdx, ") 
		.append("        att.sffzfw, ") 
		.append("        att.sfpchs, ") 
		.append("        att.sfrpc, ") 
		.append("        att.sfzb, ") 
		.append("        att.sfzzcp, ") 
		.append("        att.shipunitnum, ") 
		.append("        att.stockbycheck, ") 
		.append("        att.stocklowerdays, ") 
		.append("        att.stockupperdays, ") 
		.append("        att.storeunitnum, ") 
		.append("        att.sumahead, ") 
		.append("        att.supplytype, ") 
		.append("        att.supplytype_show, ") 
		.append("        att.unitvolume, ") 
		.append("        att.unitweight, ") 
		.append("        att.usableamount0, ") 
		.append("        att.usableamount1, ") 
		.append("        att.usableamount10, ") 
		.append("        att.usableamount11, ") 
		.append("        att.usableamount12, ") 
		.append("        att.usableamount13, ") 
		.append("        att.usableamount14, ") 
		.append("        att.usableamount15, ") 
		.append("        att.usableamount2, ") 
		.append("        att.usableamount3, ") 
		.append("        att.usableamount4, ") 
		.append("        att.usableamount5, ") 
		.append("        att.usableamount6, ") 
		.append("        att.usableamount7, ") 
		.append("        att.usableamount8, ") 
		.append("        att.usableamount9, ") 
		.append("        att.usableamountbyfree0, ") 
		.append("        att.usableamountbyfree1, ") 
		.append("        att.usableamountbyfree2, ") 
		.append("        att.virtualflag, ") 
		.append("        att.wasterrate, ") 
		.append("        att.weitunitnum, ") 
		.append("        att.wggdsczkxs, ") 
		.append("        att.wghxcl, ") 
		.append("        att.wghxcl_show, ") 
		.append("        att.wholemanaflag, ") 
		.append("        att.width, ") 
		.append("        att.ybgys, ") 
		.append("        att.zbczjyxm, ") 
		.append("        att.zbczjyxmname, ") 
		.append("        att.zbxs, ") 
		.append("        att.zdhd, ") 
		.append("        att.zgys, ") 
		.append("        att.isused, ")//是否出入库
		.append("        att.qualitydaynum ")//默认保质期
		.append("    from bd_invcl cl   ") 
		.append("    left join bd_invcl_attribute att   ") 
		.append("      on att.pk_invcl = cl.pk_invcl   ") 
		.append("     and nvl(att.dr,0) = 0  ") 
		.append("   where nvl(cl.dr,0) = 0  ") ;
		if (pk_invcl!=null) {
			sb.append(" and cl.pk_invcl = '"+pk_invcl+"' ");
		}
		return sb;
	}

	public String getCodeFieldName() {
		// TODO Auto-generated method stub
		return "invclasscode";
	}

	public String getCodeRule() {
		// TODO Auto-generated method stub
		return "2.2.2.2";
	}

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "invclassnameaaa";
	}

	public String getIDFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParentIDFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
}
