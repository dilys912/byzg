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
import nc.vo.ic.pub.AggLPWS;
import nc.vo.ic.pub.LPWSJKBVO;
import nc.vo.ic.pub.LPWSJKHVO;
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

public class LPWSBillProcess extends AbstractPfxxPlugin {
	
	private static String WH = "1108111000000000007W";//仓库主键
	LPWSJKHVO wshvo = new LPWSJKHVO();
	LPWSJKBVO[] wsbvos = null;
	
	protected Object processBill(Object vo, UfinterfaceVO ufvo, XsysregisterVO xsysvo) throws BusinessException {
			if (vo == null) {
				throw new BusinessException(NCLangResOnserver.getInstance()
						.getStrByID("4008other", "UPP4008other-000150"));
			}
			AggLPWS billvo = (AggLPWS) vo;
			wshvo = billvo.getParentVO();
			wsbvos = billvo.getChildrenVO();
			for (int i = 0; i < wsbvos.length; i++) {
				if(wsbvos[i].getVproductbatch() == null || wsbvos[i].getVproductbatch().length() <= 0){
					throw new BusinessException("存在无生产订单号的数据！");
				}
			}
			GeneralBillVO newaggvo = new GeneralBillVO();
			PfUtilBO pf = new PfUtilBO();
			
			try {
				isClosed();
				setGeneralHVO(newaggvo);
				pf.processAction("SAVE", "46", wshvo.getAttributeValue("dbilldate").toString(), null, newaggvo, null);
				//更新生成订单入库数量
				StringBuffer sql = new StringBuffer();
				sql.append("update mm_mo set rksl = nvl(rksl,0)-"+wsbvos[0].getVuserdef1()
						+",sjwgsl = nvl(sjwgsl,0)-"+wsbvos[0].getVuserdef1()
						+" where pk_corp = '1108' and scddh = '"+wsbvos[0].getVproductbatch()+"';");
				bdao.executeUpdate(sql.toString());
				//更新完工状态
//				StringBuffer sql2 = new StringBuffer();
//				sql2.append("update mm_mo set zt='B' where pk_corp = '1108' and scddh = '"+wsbvos[0].getVproductbatch()+"' and rksl < jhwgsl+160000;");
//				bdao.executeUpdate(sql2.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		return null;
		}
	//判断是否已结账
	private void isClosed() throws BusinessException {
		String date = wshvo.getDbilldate();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select faccountflag from ic_accountctrl   ") 
		.append(" where nvl(dr,0)=0 ") 
		.append(" and cwarehouseid = '"+WH+"' and tendtime>='"+date+"' and tstarttime<='"+date+"' "); 
		
		Map sqlmap = (Map) bdao.executeQuery(sql.toString(), new MapProcessor());
		if(sqlmap==null){
			Map a = new HashMap();
			a.put("faccountflag","Y");
			sqlmap = a;
		}
		
		if("N".equals(sqlmap.get("faccountflag"))){
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
			wshvo.setDbilldate(nextmonth);
		}
	}


	/**
	 * 构建其他入库单VO
	 * @param newaggvo 
	 * @param dlaggvo 
	 * @throws BusinessException 
	 * */
	private void setGeneralHVO(GeneralBillVO newaggvo) throws BusinessException{
		GeneralBillHeaderVO hvo = new GeneralBillHeaderVO();
		hvo.setCbilltypecode("46");
		hvo.setCdispatcherid("11081110000000000DX5");//收发类别 产成品入库
		hvo.setCwarehouseid(WH);
		hvo.setVdiliveraddress("PTS系统导入");
		hvo.setPk_corp("1108");
		hvo.setIsforeignstor(UFBoolean.FALSE);
		hvo.setIsgathersettle(UFBoolean.FALSE);
		hvo.setIscalculatedinvcost(UFBoolean.TRUE);
		hvo.setCoperatoridnow("0001A210000000076WD9");//zghg
		hvo.setCoperatorid("0001A210000000076WD9");//zghg
		hvo.setIsLocatorMgt(0);
		hvo.setClogdatenow(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		hvo.setIsWasteWh(0);
		hvo.setIsdirectstore(UFBoolean.FALSE);
		hvo.setStatus(VOStatus.NEW);
		hvo.setDbilldate(new UFDate(wshvo.getDbilldate()));
		newaggvo.setParentVO(hvo);
		this.setGeneralBVO(newaggvo);
	}
	BaseDAO bdao = new BaseDAO();
	@SuppressWarnings("unchecked")
	private void setGeneralBVO(GeneralBillVO newaggvo) throws BusinessException {
		List<GeneralBillItemVO> bvos = new  ArrayList<GeneralBillItemVO>();//产成品导入
		//关联生成订单
		for (int i = 0; i < wsbvos.length; i++) {
			GeneralBillItemVO bvo = new GeneralBillItemVO();
			//获取生产订单信息
			String scddh = wsbvos[i].getVproductbatch();
			String scddsql = "select scbmid,pk_moid from mm_mo where pk_corp = '1108' and nvl(dr,0)=0 and scddh = '"+scddh+"';";
			Map sqlmap = (Map) bdao.executeQuery(scddsql, new MapProcessor());
			if(sqlmap==null||sqlmap.size()==0){
				throw new BusinessException("找不到对应生成订单");
			}
			((GeneralBillHeaderVO)newaggvo.getParentVO()).setCdptid(sqlmap.get("scbmid").toString());
			String scddzj = sqlmap.get("pk_moid").toString();
			bvo.setCfirstbillhid(scddzj);
			bvo.setVproductbatch(scddh);
			bvo.setVfirstbillcode(scddh);
			bvo.setCfirsttype("A2");
			
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
			bvo.setCinventorycode(wsbvos[i].getCinventorycode());
			StringBuffer sql = new StringBuffer();
			sql.append("select * from bd_invbasdoc a where a.invcode = '"+wsbvos[i].getCinventorycode()+"' and nvl(a.dr,0)=0 ;");
			Map a = null;
			try {
				a = (Map) bdao.executeQuery(sql.toString(),new MapProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
				throw new BusinessException("找不到对应产品");
			}
			StringBuffer sql2 = new StringBuffer();
			sql2.append("select * from bd_invmandoc a where a.pk_invbasdoc = '"+a.get("pk_invbasdoc").toString()+"'and a.pk_corp = '1108' and nvl(a.dr,0)=0 ;");
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
			bvo.setIsAstUOMmgt(0);
			bvo.setNegallowed(UFBoolean.FALSE);
			bvo.setIsSellProxy(0);
			bvo.setLaborflag(UFBoolean.FALSE);
			bvo.setStatus(VOStatus.NEW);
			bvo.setVbatchcode(wsbvos[i].getVbatchcode());
			bvo.setNshouldinnum(getObValue(wsbvos[i].getVuserdef1()));
			bvo.setNinnum(getObValue(wsbvos[i].getVuserdef1()));
			bvo.setCrowno(String.valueOf((i+1)*10));
			bvo.setDbizdate(new UFDate(wshvo.getDbilldate()));
			bvos.add(bvo);
		}
		newaggvo.setChildrenVO(bvos.toArray(new GeneralBillItemVO[bvos.size()]));

	}
	/**
	 * @param oldvalue
	 * @return
	 * 取得相反
	 */
	public UFDouble getObValue(UFDouble oldvalue){
		return new UFDouble(0-oldvalue.doubleValue());
	}
	}
