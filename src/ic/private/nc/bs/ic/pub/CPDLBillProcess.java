package nc.bs.ic.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.pub.pf.PfUtilBO;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.ic.pub.AggCpdl;
import nc.vo.ic.pub.CpdljkBVO;
import nc.vo.ic.pub.CpdljkVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pfxx.manualload.UfinterfaceVO;
import nc.vo.pfxx.xxchangereg.XsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class CPDLBillProcess extends AbstractPfxxPlugin {

	private static String WH = "1108111000000000007Q";//仓库主键
	CpdljkVO cvoh = new CpdljkVO();
	CpdljkBVO[] cvob = null;
	
	protected Object processBill(Object vo, UfinterfaceVO ufvo, XsysregisterVO xsysvo) throws BusinessException {
		if (vo == null) {
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("4008other", "UPP4008other-000150"));
		}
		AggCpdl billvo = (AggCpdl) vo;
		GeneralBillVO aggvo = new GeneralBillVO();
		cvoh =  (CpdljkVO) billvo.getParentVO();
		cvob = (CpdljkBVO[]) billvo.getChildrenVO();
		//add by zwx 2017-6-12 判断出入库表体自定义项15 xml导入文件名是否已经到导入过，如果导入过则返回异常信息
		String xmlname = cvoh.getXmlname()==null?"":cvoh.getXmlname();
		if(xmlname.length()==0){
			throw new BusinessException("未获得xml导入文件名称！");
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append(" select vuserdef15 from ic_general_b ") 
			.append(" where pk_corp = '1108' ") 
			.append(" and nvl(dr,0) = 0 ") 
			.append(" and vuserdef15 = '"+xmlname+"' ") ;
			Map nameMap = (Map) bdao.executeQuery(sql.toString(), new MapProcessor());
			if(nameMap!=null){
				throw new BusinessException("当前xml已导入过！");
			} 
			
		}

		//end by zwx
		if(cvoh.getVuserdef1().endsWith("退货")){
			
		}else{			
			if(cvoh.getCbilltypecode() == null || cvoh.getCbilltypecode().length() <= 0){
				throw new BusinessException("无生产订单号！");
			}
		}
		//产成品入库单//F
		if(cvoh.getVuserdef1().startsWith("成品")){
			WH="1108111000000000007Q";
		}else if(cvoh.getVuserdef1().startsWith("半成品")){
			WH="1108111000000000007P";
		}
		//线上和退货扣留   现场扣留品仓
		if(cvoh.getVuserdef1().endsWith("线上")||cvoh.getVuserdef1().endsWith("退货")){
			WH="1108111000000000007V";
		}
		//线上良品尾数入库  良品尾数仓
		else if(cvoh.getVuserdef2().startsWith("WS")&&(!cvoh.getVuserdef1().startsWith("半成品"))){
			WH = "1108111000000000007W";
		}
		isClosed();
		this.setGeneralHVO(billvo, aggvo);
		//add by zwx 2017-6-12 将xml文件名添加至出入库单表体自定义项15
		GeneralBillItemVO[] bodyvo = (GeneralBillItemVO[]) aggvo.getChildrenVO();
		if(bodyvo!=null){
			bodyvo[0].setAttributeValue("vuserdef15", cvoh.getXmlname());
		}
		//end by zwx
		PfUtilBO pf = new PfUtilBO();
		//如果没有生产订单 就走其他入库单，如果是退货扣留 就走其他入库单
		if(cvoh.getVuserdef1().endsWith("退货")){
			try {
				pf.processAction("SAVE", "4A", aggvo.getParentVO().getAttributeValue("dbilldate").toString(), null, aggvo, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}else{
			try {
				pf.processAction("SAVE", "46", aggvo.getParentVO().getAttributeValue("dbilldate").toString(), null, aggvo, null);
				
				//add by zwx 2017-7-12 存在多个相同ts的单据，删除多余的
				StringBuffer sqlcount = new StringBuffer();
				sqlcount.append(" select cgeneralhid from ic_general_b ") 
				.append(" where pk_corp = '1108' ") 
				.append(" and nvl(dr,0) = 0 ") 
				.append(" and nvl(dr,0) = 0 ") 
				.append(" and vuserdef15 = '"+xmlname+"' ") ;
				Object hidlist = bdao.executeQuery(sqlcount.toString(), new ColumnListProcessor());
				if(hidlist!=null){ 
					List list = (List)hidlist;
					if(list.size()>1){//大于1条就进行删除
						for(int i = 1;i<list.size();i++){//剩余一条不删除
							String hid = list.get(i)==null?"":list.get(i).toString();
							if(hid.length()>0){
								String headsql = "delete from ic_general_h where cgeneralhid = '"+hid+"'";
								String bodysql = "delete from ic_general_b where cgeneralhid = '"+hid+"'";
								bdao.executeUpdate(headsql.toString());
								bdao.executeUpdate(bodysql.toString());
							}
							
						}
					}
				}
				//end by zwx 
				//更新生成订单入库数量
				StringBuffer sql = new StringBuffer();
				sql.append("update mm_mo set rksl = nvl(rksl,0)+"+cvob[0].getNinnum()+",sjwgsl = nvl(sjwgsl,0)+"+cvob[0].getNinnum()+" where pk_corp = '1108' and scddh = '"+cvoh.getCbilltypecode()+"';");
				bdao.executeUpdate(sql.toString());
				//更新完工状态
				StringBuffer sql2 = new StringBuffer();
				if(cvob[0].getNinnum().compareTo(new UFDouble(0)) < 0){
					sql2.append("update mm_mo set zt='B' where  pk_corp = '1108' and scddh = '"+cvoh.getCbilltypecode()+"' and rksl < jhwgsl;");
					bdao.executeUpdate(sql2.toString());
				}
				//edit by zwx 2019-9-2 新制盖移除
				/*else{
					sql2.append("update mm_mo set zt='C' where  pk_corp = '1108' and scddh = '"+cvoh.getCbilltypecode()+"' and rksl >= jhwgsl;");
				}*/
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}
		return null;
	}

	//判断是否已结账
	private void isClosed() throws BusinessException {
		String date = cvoh.getDbilldate();
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
			cvoh.setDbilldate(nextmonth);
		}
	}

	/**
	 * 构建其他入库单VO
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")
	private void setGeneralHVO(AggCpdl billvo,GeneralBillVO aggvo) throws BusinessException{
		GeneralBillHeaderVO hvo = new GeneralBillHeaderVO();
		if(cvoh.getVuserdef1().startsWith("成品")){
			hvo.setCdispatcherid("11081110000000000DX5");//收发类别 产成品入库
		}else if(cvoh.getVuserdef1().startsWith("半成品")){
			hvo.setCdispatcherid("11081110000000000DX4");//收发类别 半成品入库
		}
		if(cvoh.getCbilltypecode() == null || cvoh.getCbilltypecode().length() <= 0){
			hvo.setCbilltypecode("4A");
			hvo.setCdispatcherid("11081110000000000DX8");//收发类别 其他入库
		}else{			
			//获取机台（部门）
			String sql = "select scbmid from mm_mo where pk_corp = '1108' and nvl(dr,0)=0 and scddh = '"+cvoh.getCbilltypecode()+"';";
			Map sqlmap = (Map) bdao.executeQuery(sql, new MapProcessor());
//			hvo.setCdptid(sqlmap.get("scbmid").toString());
			//add by zwx 2017-6-9 防止回执信息只写空指针异常
			try {

				hvo.setCdptid(sqlmap.get("scbmid").toString());
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("找不到对应生产订单");
			}
			//end by zwx 
			hvo.setCbilltypecode("46");
		}
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
		hvo.setDbilldate(new UFDate(cvoh.getDbilldate()));
		aggvo.setParentVO(hvo);
		this.setGeneralBVO(billvo,aggvo);
	}
	BaseDAO bdao = new BaseDAO();
	@SuppressWarnings("unchecked")
	private void setGeneralBVO(AggCpdl billvo, GeneralBillVO aggvo) throws BusinessException {
		List<GeneralBillItemVO> bvos = new  ArrayList<GeneralBillItemVO>();
		//关联生成订单
		Map scdd = null;
		String scddh = cvoh.getCbilltypecode();
		if(cvoh.getCbilltypecode() == null || cvoh.getCbilltypecode().length() <= 0){
			
		}else{
			StringBuffer scddsql = new StringBuffer();
			if(cvob[0].getNinnum().compareTo(new UFDouble(0)) < 0 ){
				scddsql.append("select * from mm_mo a where a.scddh = '"+scddh+"' and a.pk_corp = '1108' and nvl(a.dr,0)=0 ;");
			}else{
				scddsql.append("select * from mm_mo a where a.scddh = '"+scddh+"' and a.pk_corp = '1108' and nvl(a.dr,0)=0 and zt<>'C' ;");				
			}
			try {
				scdd = (Map) bdao.executeQuery(scddsql.toString(),new MapProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
				throw new BusinessException("找不到对应生产订单");
			}			
			if(scdd==null){
				throw new BusinessException("找不到对应生产订单");
			}
		}
		for (int i = 0; i < cvob.length; i++) {
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
			bvo.setCinventorycode(cvob[i].getCinventorycode());
			StringBuffer sql = new StringBuffer();
			sql.append("select * from bd_invbasdoc a where a.invcode = '"+cvob[i].getCinventorycode()+"' and nvl(a.dr,0)=0 ;");
			Map a = null;
			try {
				a = (Map) bdao.executeQuery(sql.toString(),new MapProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
				throw new BusinessException("找不到对应产品");
			}
			//add by zwx 2017-6-12 
			if(a == null){
				throw new BusinessException("找不到对应产品");
			}
			//end by zwx 
			StringBuffer sql2 = new StringBuffer();
			sql2.append("select * from bd_invmandoc a where a.pk_invbasdoc = '"+a.get("pk_invbasdoc").toString()+"'and a.pk_corp = '1108' and nvl(a.dr,0)=0 ;");
			Map a2 = null;
			try {
				a2 = (Map) bdao.executeQuery(sql2.toString(),new MapProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
				throw new BusinessException("公司未配置对应产品");
			}
			//add by zwx 2017-6-12 
			if(a2 == null){
				throw new BusinessException("公司未配置对应产品");
			}
			//end by zwx 
			bvo.setCinvmanid(a2.get("pk_invmandoc").toString());
			bvo.setCinventoryid(a2.get("pk_invmandoc").toString());
			bvo.setCinvbasid(a.get("pk_invbasdoc").toString());
			bvo.setInvname(a.get("invname").toString());
			if(cvoh.getCbilltypecode() == null || cvoh.getCbilltypecode().length() <= 0){

			}else{				
				String scddzj = scdd.get("pk_moid").toString();
				bvo.setCfirstbillhid(scddzj);
				bvo.setVproductbatch(scddh);
				bvo.setVfirstbillcode(scddh);
				bvo.setCfirsttype("A2");
			}
			
			bvo.setIsAstUOMmgt(0);
			bvo.setNegallowed(UFBoolean.FALSE);
			bvo.setIsSellProxy(0);
			bvo.setLaborflag(UFBoolean.FALSE);
			bvo.setStatus(VOStatus.NEW);
			bvo.setVbatchcode(cvoh.getVuserdef2());
			bvo.setNshouldinnum(cvob[i].getNshouldinnum());
			bvo.setNinnum(cvob[i].getNinnum());
			bvo.setCrowno(String.valueOf((i+1)*10));
			bvo.setDbizdate(new UFDate(cvoh.getDbilldate()));
			bvos.add(bvo);
		}
		aggvo.setChildrenVO(bvos.toArray(new GeneralBillItemVO[bvos.size()]));

	}
}