package nc.bs.ic.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.pub.pf.PfUtilBO;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.ic.pub.AggFJ;
import nc.vo.ic.pub.FJJKBVO;
import nc.vo.ic.pub.FJJKHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pfxx.manualload.UfinterfaceVO;
import nc.vo.pfxx.xxchangereg.XsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class FJBillProcess extends AbstractPfxxPlugin {
	
	private static String RKWH = "1108111000000000007W";//入库仓库主键
	private static String HCWH = "1108111000000000007V";//回冲仓库主键
	PfUtilBO pf = new PfUtilBO();
	BaseDAO bdao = new BaseDAO();
	
	FJJKHVO fjhvo = new FJJKHVO();
	FJJKBVO[] fjbvo = null;
	
	protected Object processBill(Object vo, UfinterfaceVO ufvo, XsysregisterVO xsysvo) throws BusinessException {
			if (vo == null) {
				throw new BusinessException(NCLangResOnserver.getInstance()
						.getStrByID("4008other", "UPP4008other-000150"));
			}
			AggFJ billvo = (AggFJ) vo;
			fjhvo = billvo.getParentVO();
			fjbvo = billvo.getChildrenVO();
	        //其他入库单
			GeneralBillVO aggvo = new GeneralBillVO();
			//其他入库单（转废品）
			GeneralBillVO aggvo2 = new GeneralBillVO();
			//回冲其他入库单
			GeneralBillVO hcaggvo = new GeneralBillVO();
			try {
				isClosed();
				setGeneralHVO(hcaggvo,null,"hc");
				pf.processAction("SAVE", "4A", hcaggvo.getParentVO().getAttributeValue("dbilldate").toString(), null, hcaggvo, null);
				setGeneralHVO(aggvo,aggvo2,"rk");
				if(fjbvo[0].getVuserdef1().compareTo(new UFDouble(0)) > 0){					
					pf.processAction("SAVE", "4A", aggvo.getParentVO().getAttributeValue("dbilldate").toString(), null, aggvo, null);
				}
				if(fjbvo[0].getVuserdef2().compareTo(new UFDouble(0)) > 0){					
					pf.processAction("SAVE", "4A", aggvo2.getParentVO().getAttributeValue("dbilldate").toString(), null, aggvo2, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		return null;
	}
	//判断是否已结账
	private void isClosed() throws BusinessException {
		String date = fjhvo.getDbilldate();
		
		StringBuffer sql_rk = new StringBuffer();
		sql_rk.append(" select faccountflag from ic_accountctrl   ") 
		.append(" where nvl(dr,0)=0 ") 
		.append(" and cwarehouseid = '"+RKWH+"' and tendtime>='"+date+"' and tstarttime<='"+date+"' "); 
		Map sqlmap_rk = (Map) bdao.executeQuery(sql_rk.toString(), new MapProcessor());
		if(sqlmap_rk==null){
			Map a = new HashMap();
			a.put("faccountflag","Y");
			sqlmap_rk = a;
		}
		
		StringBuffer sql_fp = new StringBuffer();
		sql_fp.append(" select faccountflag from ic_accountctrl   ") 
		.append(" where nvl(dr,0)=0 ") 
		.append(" and cwarehouseid = '1078A210000000010J0U' and tendtime>='"+date+"' and tstarttime<='"+date+"' "); 
		Map sqlmap_fp = (Map) bdao.executeQuery(sql_fp.toString(), new MapProcessor());
		if(sqlmap_fp==null){
			Map a = new HashMap();
			a.put("faccountflag","Y");
			sqlmap_fp = a;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select faccountflag from ic_accountctrl   ") 
		.append(" where nvl(dr,0)=0 ") 
		.append(" and cwarehouseid = '"+HCWH+"' and tendtime>='"+date+"' and tstarttime<='"+date+"' "); 
		Map sqlmap = (Map) bdao.executeQuery(sql.toString(), new MapProcessor());
		if(sqlmap==null){
			Map a = new HashMap();
			a.put("faccountflag","Y");
			sqlmap = a;
		}
		
		if("N".equals(sqlmap_rk.get("faccountflag"))||"N".equals(sqlmap_fp.get("faccountflag"))||"N".equals(sqlmap.get("faccountflag"))){
			String nextmonth = "";
			int year = new UFDate(date).getYear();
			int month = new UFDate(date).getMonth();
			if(month == 12){
				year = year + 1;
				month = 1;
			}else{
				month = month+1;
			}
			if(month < 10){
				nextmonth = ""+year+"-0"+month+"-01";
			}else{
				nextmonth = ""+year+"-"+month+"-01";
			}
			fjhvo.setDbilldate(nextmonth);
		}
	}

	
	/**
	 * 构建其他入库单VO
	 * @throws Exception 
	 * */
	private void setGeneralHVO(GeneralBillVO aggvo,GeneralBillVO aggvo2,String type) throws Exception{
		GeneralBillHeaderVO hvo = new GeneralBillHeaderVO();
		hvo.setCbilltypecode("4A");
		hvo.setCdispatcherid("11081110000000000DX8");//收发类别 其他入库
		if("rk".equals(type)){			
			hvo.setCwarehouseid(RKWH);
		}else if("hc".equals(type)){			
			hvo.setCwarehouseid(HCWH);
		}
		hvo.setVdiliveraddress("PTS系统导入");
		hvo.setPk_corp("1108");
		hvo.setIsforeignstor(UFBoolean.FALSE);
		hvo.setIsgathersettle(UFBoolean.FALSE);
		hvo.setIscalculatedinvcost(UFBoolean.TRUE);
		hvo.setCoperatoridnow("0001A210000000076WD9");//zghg
		hvo.setCoperatorid("0001A210000000076WD9");//zghg
		hvo.setIsLocatorMgt(0);
		hvo.setClogdatenow(fjhvo.getDbilldate());
		hvo.setIsWasteWh(0);
		hvo.setIsdirectstore(UFBoolean.FALSE);
		hvo.setStatus(VOStatus.NEW);
		hvo.setDbilldate(new UFDate(fjhvo.getDbilldate()));
		aggvo.setParentVO(hvo);
		GeneralBillHeaderVO hvo2 = (GeneralBillHeaderVO) hvo.clone();
//		hvo2.setCwarehouseid("1078A210000000010J0U");
		if(aggvo2 !=null){			
			aggvo2.setParentVO(hvo2);
		}
		this.setGeneralBVO(aggvo,aggvo2,type);
	}

	
	@SuppressWarnings("unchecked")
	private void setGeneralBVO(GeneralBillVO aggvo, GeneralBillVO aggvo2,String type) throws Exception {
		List<GeneralBillItemVO> bvos = new  ArrayList<GeneralBillItemVO>();//良品尾数
		List<GeneralBillItemVO> bvos2 = new  ArrayList<GeneralBillItemVO>();//废品
		
		for (int i = 0; i < fjbvo.length; i++) {
			GeneralBillItemVO bvo = new GeneralBillItemVO();
			bvo.setIsmngstockbygrswt(0);
			bvo.setNunitweight(new UFDouble(0));
			bvo.setIsValidateMgt(0);
			bvo.setDiscountflag(UFBoolean.FALSE);
			bvo.setIssecondarybarcode(UFBoolean.FALSE);
			bvo.setOutpriority(0);
			bvo.setIsSerialMgt(0);
			bvo.setNunitvolume(new UFDouble(0));
			bvo.setIsprimarybarcode(UFBoolean.FALSE);
			bvo.setIsLotMgt(0);
			bvo.setOuttrackin(UFBoolean.FALSE);
			bvo.setIsAstUOMmgt(0);
			bvo.setNegallowed(UFBoolean.FALSE);
			bvo.setIsSellProxy(0);
			bvo.setLaborflag(UFBoolean.FALSE);
			bvo.setStatus(VOStatus.NEW);
			bvo.setCinventorycode(fjbvo[i].getCinventorycode());
			StringBuffer sql = new StringBuffer();
			sql.append("select * from bd_invbasdoc a where a.invcode = '"+fjbvo[i].getCinventorycode()+"';");
			Map a = null;
			try {
				a = (Map) bdao.executeQuery(sql.toString(),new MapProcessor());
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("找不到对应产品");
			}
			StringBuffer sql2 = new StringBuffer();
			sql2.append("select * from bd_invmandoc a where a.pk_invbasdoc = '"+a.get("pk_invbasdoc").toString()+"'and a.pk_corp = '1108';");
			Map a2 = null;
			try {
				a2 = (Map) bdao.executeQuery(sql2.toString(),new MapProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
				throw new BusinessException("公司未配置对应产品");
			}
			bvo.setCinvmanid(a2.get("pk_invmandoc").toString());
			bvo.setCinventoryid(a2.get("pk_invmandoc").toString());
			bvo.setCinvbasid(a.get("pk_invbasdoc").toString());
			bvo.setInvname(a.get("invname").toString());
			bvo.setVbatchcode(fjhvo.getVbatchcode());
			bvo.setNshouldinnum(fjbvo[i].getVuserdef1());
			bvo.setNinnum(fjbvo[i].getVuserdef1());
			bvo.setCrowno(String.valueOf((i+1)*10));
			bvo.setDbizdate(new UFDate(fjhvo.getDbilldate()));
			if("hc".equals(type)){
				UFDouble hcnum = new UFDouble(0).sub(fjbvo[i].getVuserdef1().add(fjbvo[i].getVuserdef2()));
				bvo.setNshouldinnum(hcnum);
				bvo.setNinnum(hcnum);
			}
			bvos.add(bvo);
			GeneralBillItemVO bvo2 = (GeneralBillItemVO) bvo.clone();
			bvo2.setNshouldinnum(fjbvo[i].getVuserdef2());
			bvo2.setNinnum(fjbvo[i].getVuserdef2());
			bvos2.add(bvo2);
		}
		aggvo.setChildrenVO(bvos.toArray(new GeneralBillItemVO[bvos.size()]));
		if(aggvo2 !=null){	
			aggvo2.setChildrenVO(bvos2.toArray(new GeneralBillItemVO[bvos2.size()]));
		}

	}
	
}