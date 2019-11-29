package nc.bs.bgjs.jiean;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.ia.bill.IBill;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bgjs.bill.CeVO;
import nc.vo.bgjs.jiean.JieanVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ps.settle.StockVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;

public class ServerDAO {
	
	private BaseDAO baseDAO = null;	//数据库操作基类
	

	//zhwj 
	private String[] status = new String[]{
			"未暂估且未结算",
			"已暂估且未结算",
			"已暂估部分结算",
			"已结算",
			"已结案"
	};
	
	
	/**
	 * 功能：数据库操作对象
	 * 
	 * @return 
	 */
	protected BaseDAO getBaseDAO() {
		if(baseDAO==null)
			baseDAO = new BaseDAO();
		return baseDAO;
	}
	
	
	
	public ArrayList getVOsfromSql(String sql,String voname) throws BusinessException, ClassNotFoundException{
		return (ArrayList)getBaseDAO().executeQuery(sql,new BeanListProcessor(Class.forName(voname))); 
	}
	
	

	// 保存VO数组带PK
	public void insertVOsWithPK(Object[] objs) throws BusinessException {
		getBaseDAO().insertVOArrayWithPK((SuperVO[]) objs);
	}

	// 保存VO数组不带PK
	public void insertVOsArr(Object[] objs) throws BusinessException {
		getBaseDAO().insertVOArray((SuperVO[]) objs);
	}

	// 删除VO数组
	public void deleteVOsArr(Object[] objs) throws BusinessException {
		getBaseDAO().deleteVOArray((SuperVO[]) objs);
	}

	// 删除VO
	public void deleteVO(Object obj) throws BusinessException {
		getBaseDAO().deleteVO((SuperVO) obj);
	}

	// 更新VO数组
	public void updateVOsArr(Object[] objs) throws BusinessException {
		getBaseDAO().updateVOArray((SuperVO[]) objs);
	}

	// 更新VO
	public void updateVO(Object obj) throws BusinessException {
		getBaseDAO().updateVO((SuperVO) obj);

	}

	// 保存VO返回PK
	public String insertVObackPK(Object obj) throws BusinessException {
		return getBaseDAO().insertVO((SuperVO) obj);
	}

	// 执行update语句
	public void updateBYsql(String sql) throws BusinessException {
		getBaseDAO().executeUpdate(sql);
	}

	
	
	public ArrayList getJieAnData(HashMap hminfo) throws BusinessException{
		String where = "";
		String isjiean = "";
		String vinvoicecode = "";
		if(hminfo!=null&&hminfo.size()>0){
			where = hminfo.get("where")==null?"":hminfo.get("where").toString();
			isjiean = hminfo.get("isjiean")==null?"":hminfo.get("isjiean").toString();
			vinvoicecode = hminfo.get("vinvoicecode")==null?"":hminfo.get("vinvoicecode").toString();
		}
		String sql = getJieAnSql(where,vinvoicecode);
		try {
			ArrayList list = getVOsfromSql(sql,JieanVO.class.getName());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					JieanVO jvo = (JieanVO)list.get(i);
					String cgeneralhis = jvo.getIcgeneralhid();
					UFBoolean isyj = isJieAn(cgeneralhis);
					jvo.setIsyj(isyj);
				    if(isjiean!=null&&!isjiean.equals("")){
				    	if(isjiean.equals("Y")){//查询所有已结的
				    		if(!isyj.booleanValue()){
				    			list.remove(i);
				    			i--;
				    		}
				    	}
				    	if(isjiean.equals("N")){//查询所有已未结的
				    		if(isyj.booleanValue()){
				    			list.remove(i);
				    			i--;
				    		}
				    	}
				    }
				    
				}
			}
			return list;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e.getException());
		}
	}
	
	
	public String getJieAnSql(String where,String vinvoicecode){
		StringBuffer sql = new StringBuffer();
		sql.append(" select tmp_zg.ordercode,tmp_zg.iccode,tmp_zg.corderid,tmp_zg.icgeneralhid,tmp_ivo.vinvoicecode,tmp_zg.zgmny,nvl(tmp_js.yjmny,0) yjmny,tmp_zg.zgmny-nvl(tmp_js.yjmny,0) deffmny ")
			.append(" from ( ")
			.append(" select po.vordercode ordercode,igh.vbillcode iccode,po.corderid ,igh.cgeneralhid icgeneralhid,sum(nvl(iab.nmoney,0)) zgmny ")
			.append(" from po_order po ")
			.append(" inner join po_order_b pob ")
			.append(" on po.corderid=pob.corderid ")
			.append(" inner join ic_general_b igb ")
			.append(" on pob.corder_bid=igb.cfirstbillbid ")
			.append(" inner join ic_general_h igh ")
			.append(" on igb.cgeneralhid=igh.cgeneralhid ")
			.append(" left join ia_bill_b iab ")
			.append(" on igb.cgeneralbid=iab.csourcebillitemid ")
			.append(" left join ia_bill ia ")
			.append(" on iab.cbillid=ia.cbillid ")
			.append(" where 1=1 ")//po.vordercode='CD12080020' ")
			.append(" and nvl(pob.vdef17,'否')='是' ")
			.append(" and nvl(ia.cbilltypecode,'I9')='I9' ")
			.append(" and nvl(ia.isjiean,'N')='N' ")//非结案生成
			.append(" and nvl(iab.nmoney,0)<>0 ")
			.append(" and nvl(po.dr,0)=0 ")
			.append(" and nvl(pob.dr,0)=0 ")
			.append(" and nvl(igb.dr,0)=0 ")
			.append(" and nvl(igh.dr,0)=0 ")
			.append(" and nvl(iab.dr,0)=0 ")
			.append(" and nvl(ia.dr,0)=0 ");
			
			if(where!=null&&!where.equals("")){
				sql.append(" and "+ where);
			}
			
			sql.append(" group by  po.vordercode,igh.vbillcode,po.corderid,igh.cgeneralhid ")
			.append(" ) tmp_zg ")
			.append(" left join ( ")
			.append(" select po.vordercode ordercode,igh.vbillcode iccode,po.corderid,igh.cgeneralhid icgeneralhid,sum(nvl(seb2.nmoney,0)) yjmny ")
			.append(" from po_order po ")
			.append(" inner join po_order_b pob ")
			.append(" on po.corderid=pob.corderid ")
			.append(" inner join ic_general_b igb ")
			.append(" on pob.corder_bid=igb.cfirstbillbid ")
			.append(" inner join ic_general_h igh ")
			.append(" on igb.cgeneralhid=igh.cgeneralhid ")
			.append(" left join po_settlebill_b seb ")
			.append(" on igb.cgeneralbid=seb.cstockrow ")
			.append(" left join po_settlebill se ")
			.append(" on seb.csettlebillid=se.csettlebillid ")
			.append(" left join po_settlebill_b seb2 on seb2.csettlebillid=se.csettlebillid ")//eric
			.append(" where 1=1 ")//po.vordercode='CD12080020' ")
			.append(" and nvl(pob.vdef17,'否')='是' ")
			.append(" and nvl(se.isyf,'N')='Y' ")
			.append(" and nvl(seb.nmoney,0)<>0 ")
			.append(" and seb2.cinvoiceid is not null ")//eric 
			.append(" and nvl(po.dr,0)=0 ")
			.append(" and nvl(pob.dr,0)=0 ")
			.append(" and nvl(igb.dr,0)=0 ")
			.append(" and nvl(igh.dr,0)=0 ")
			.append(" and nvl(seb.dr,0)=0 ")
			.append(" and nvl(se.dr,0)=0 ");
			
			if(where!=null&&!where.equals("")){
				sql.append(" and "+ where);
			}
			
			sql.append(" group by  po.vordercode,igh.vbillcode,po.corderid,igh.cgeneralhid ")
			.append(" ) tmp_js ")
			
			.append(" on tmp_zg.ordercode=tmp_js.ordercode ")
			.append(" and tmp_zg.iccode=tmp_js.iccode ")
			
			.append(" left join ( ")
			.append(" select po.vordercode ordercode,igh.vbillcode iccode,po.corderid,igh.cgeneralhid icgeneralhid,max(pio.vinvoicecode) vinvoicecode ")
			.append(" from po_order po ")
			.append(" inner join po_order_b pob ")
			.append(" on po.corderid=pob.corderid ")
			.append(" inner join ic_general_b igb ")
			.append(" on pob.corder_bid=igb.cfirstbillbid ")
			.append(" inner join ic_general_h igh ")
			.append(" on igb.cgeneralhid=igh.cgeneralhid ")
			.append(" left join po_settlebill_b seb ")
			.append(" on igb.cgeneralbid=seb.cstockrow ")
			.append(" left join po_settlebill se ")
			.append(" on seb.csettlebillid=se.csettlebillid ")
			
			.append(" left join po_settlebill_b seb2  ")
			.append(" on se.csettlebillid =seb2.csettlebillid  ")
			.append(" and seb2.cinvoiceid is not null ")
            .append(" left join po_invoice pio ")
			.append(" on seb2.cinvoiceid=pio.cinvoiceid ")
			
			.append(" where 1=1 ")//po.vordercode='CD12080020' ")
			.append(" and nvl(pob.vdef17,'否')='是' ")
			.append(" and nvl(se.isyf,'N')='Y' ")
			.append(" and nvl(seb.nmoney,0)<>0 ")
			.append(" and nvl(po.dr,0)=0 ")
			.append(" and nvl(pob.dr,0)=0 ")
			.append(" and nvl(igb.dr,0)=0 ")
			.append(" and nvl(igh.dr,0)=0 ")
			.append(" and nvl(seb.dr,0)=0 ")
			.append(" and nvl(se.dr,0)=0 ")
			.append(" and nvl(pio.dr, 0) = 0 ");
			
			if(vinvoicecode!=null&&!vinvoicecode.equals("")){
				sql.append(" and nvl(pio.vinvoicecode,'"+vinvoicecode+"')='"+vinvoicecode+"' ");
			}
			
			
			if(where!=null&&!where.equals("")){
				sql.append(" and "+ where);
			}
			
			sql.append(" group by  po.vordercode,igh.vbillcode,po.corderid,igh.cgeneralhid ")
			.append(" ) tmp_ivo ")
			
			.append(" on tmp_js.ordercode=tmp_ivo.ordercode ")
			.append(" and tmp_js.iccode=tmp_ivo.iccode ");
		
			if(vinvoicecode!=null&&!vinvoicecode.equals("")){
				sql.append(" where  tmp_ivo.vinvoicecode='"+vinvoicecode+"' ");
			}
			
		return sql.toString();
	}
	
	
	//获取存货差异值  
	public HashMap getInvDiffMny(String cgeneralhid) throws BusinessException{
		String sql = getJieAnInvSql(cgeneralhid);
		
		HashMap invdiffmny = new HashMap();
		ArrayList list = (ArrayList)getBaseDAO().executeQuery(sql, new MapListProcessor());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				HashMap hm = (HashMap)list.get(i);
				String cinventoryid = hm==null?"":hm.get("cinventoryid")==null?"":hm.get("cinventoryid").toString();
				UFDouble deffmny = hm==null?new UFDouble(0):hm.get("deffmny")==null?new UFDouble(0):new UFDouble(hm.get("deffmny").toString());
				invdiffmny.put(cinventoryid, deffmny);
				
			}
		}
		
		return invdiffmny;
	}
	
	
	//按存货算差异
	public String getJieAnInvSql(String cgeneralhid){
		StringBuffer sql = new StringBuffer();
		sql.append(" select tmp_zg.ordercode,tmp_zg.iccode,tmp_zg.corderid,tmp_zg.cgeneralhid,tmp_zg.cinventoryid,tmp_zg.zgmny,tmp_js.yjmny,tmp_js.yjmny-tmp_zg.zgmny deffmny ")
			.append(" from ( ")
			.append(" select po.vordercode ordercode,igh.vbillcode iccode,po.corderid,igh.cgeneralhid,igb.cinventoryid,sum(nvl(iab.nmoney,0)) zgmny ")
			.append(" from po_order po ")
			.append(" inner join po_order_b pob ")
			.append(" on po.corderid=pob.corderid ")
			.append(" inner join ic_general_b igb ")
			.append(" on pob.corder_bid=igb.cfirstbillbid ")
			.append(" inner join ic_general_h igh ")
			.append(" on igb.cgeneralhid=igh.cgeneralhid ")
			.append(" left join ia_bill_b iab ")
			.append(" on igb.cgeneralbid=iab.csourcebillitemid ")
			.append(" left join ia_bill ia ")
			.append(" on iab.cbillid=ia.cbillid ")
			.append(" where igh.cgeneralhid='"+cgeneralhid+"' ")
			.append(" and nvl(ia.cbilltypecode,'I9')='I9' ")
			.append(" and nvl(ia.isjiean,'N')='N' ")
			.append(" and nvl(pob.vdef17,'否')='是' ")
			.append(" and nvl(po.dr,0)=0 ")
			.append(" and nvl(pob.dr,0)=0 ")
			.append(" and nvl(igb.dr,0)=0 ")
			.append(" and nvl(igh.dr,0)=0 ")
			.append(" and nvl(iab.dr,0)=0 ")
			.append(" and nvl(ia.dr,0)=0 ")
			.append(" group by  po.vordercode,igh.vbillcode,po.corderid,igh.cgeneralhid,igb.cinventoryid ")
			.append(" ) tmp_zg ")
			.append(" left join ( ")
			.append(" select po.vordercode ordercode,igh.vbillcode iccode,po.corderid,igh.cgeneralhid,igb.cinventoryid,sum(nvl(seb.nmoney,0)) yjmny ")
			.append(" from po_order po ")
			.append(" inner join po_order_b pob ")
			.append(" on po.corderid=pob.corderid ")
			.append(" inner join ic_general_b igb ")
			.append(" on pob.corder_bid=igb.cfirstbillbid ")
			.append(" inner join ic_general_h igh ")
			.append(" on igb.cgeneralhid=igh.cgeneralhid ")
			.append(" left join po_settlebill_b seb ")
			.append(" on igb.cgeneralbid=seb.cstockrow ")
			.append(" left join po_settlebill se ")
			.append(" on seb.csettlebillid=se.csettlebillid ")
			.append(" where igh.cgeneralhid='"+cgeneralhid+"' ")
			.append(" and nvl(se.isyf,'N')='Y' ")
			.append(" and nvl(pob.vdef17,'否')='是' ")
			.append(" and nvl(po.dr,0)=0 ")
			.append(" and nvl(pob.dr,0)=0 ")
			.append(" and nvl(igb.dr,0)=0 ")
			.append(" and nvl(igh.dr,0)=0 ")
			.append(" and nvl(seb.dr,0)=0 ")
			.append(" and nvl(se.dr,0)=0 ")
			.append(" group by po.vordercode ,igh.vbillcode ,po.corderid,igh.cgeneralhid,igb.cinventoryid ")
			.append(" ) tmp_js ")
			.append(" on tmp_zg.ordercode=tmp_js.ordercode ")
			.append(" and tmp_zg.iccode=tmp_js.iccode ")
			.append(" and tmp_zg.cinventoryid=tmp_js.cinventoryid ");
		return sql.toString();
	}
	
	
	//判断是否结案
	public UFBoolean isJieAn(String cgeneralhid) throws BusinessException{
		String cbillid = getJieAnI9Date(cgeneralhid);
		if(cbillid!=null&&!cbillid.equals("")){
			return new UFBoolean(true);
		}
		return new UFBoolean(false);
	}
	
	public String getJieAnI9Date(String cgeneralhid) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct ia.cbillid from ia_bill ia ")
			.append(" inner join ia_bill_b iab ")
			.append(" on ia.cbillid=iab.cbillid ")
			.append(" where nvl(ia.isjiean,'N')='Y' ")
			.append(" and iab.cicbillid='"+cgeneralhid+"' ")
			.append(" and nvl(ia.dr,0)=0 ")
			.append(" and nvl(iab.dr,0)=0 ");
		Object obj = getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
		return obj==null?"":obj.toString();
	}
	
	public ArrayList getJieAnARAPDate(String cgeneralhid) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select zb.vouchid from arap_djzb zb ")
			.append(" inner join arap_djfb fb ")
			.append(" on zb.vouchid=fb.vouchid ")
			.append(" where nvl(zb.zyx30,'N')='Y' ")
			.append(" and fb.ddlx='"+cgeneralhid+"' ")
			.append(" and nvl(zb.dr,0)=0 ")
			.append(" and nvl(fb.dr,0)=0 ");
		ArrayList list = (ArrayList)getBaseDAO().executeQuery(sql.toString(), new MapListProcessor());
		return list;
	}
	
	
	public ArrayList doJieAn(ArrayList list) throws BusinessException {
		if(list!=null&&list.size()>0){
			JieanVO jvo = (JieanVO)list.get(0);
			CeVO cevo = (CeVO)list.get(1);
			String cgeneralhid = jvo.getIcgeneralhid();
			if(cgeneralhid==null||cgeneralhid.equals("")){
				throw new BusinessException("入库单主键为空！");
			}
			
			HashMap hm_head = getCgeneralHData(cgeneralhid); 
			ArrayList arr_bodys = getCgeneralBData(cgeneralhid);
			
			if(hm_head!=null&&hm_head.size()>0
			   &&arr_bodys!=null&&arr_bodys.size()>0){
				
				 HashMap invdiffmny = getInvDiffMny(cgeneralhid);
				
				doJieAnI9(hm_head,arr_bodys,cevo,invdiffmny);
				doJieAnARAP(hm_head,arr_bodys,cevo,invdiffmny);
				
				//回写
				backICStatus(cgeneralhid,status[4],cevo.getDate().toString());//zhwj 回写入库单状态
			}
			
			
			
		}
		return null;
	}
	
	public void doJieAnI9(HashMap hm_head,ArrayList arr_bodys,CeVO cevo,HashMap invdiffmny) throws BusinessException{
		

		 //构建主表
		 		BillHeaderVO headvo=new BillHeaderVO();
		 		headvo.setBauditedflag(UFBoolean.FALSE);
		 		headvo.setBdisableflag(UFBoolean.FALSE);
		 		headvo.setBestimateflag(UFBoolean.FALSE);
		 		headvo.setBwithdrawalflag(UFBoolean.FALSE);
		 		headvo.setCbilltypecode("I9");
		 		headvo.setCcustomvendorid(getValueStr(hm_head,"ccustomerid"));
		 		headvo.setClastoperatorid(cevo.getUser());
//		 		headvo.setCoperatorid(cl.getUser());
		 		headvo.setCrdcenterid(getValueStr(hm_head,"pk_calbody"));
//		 		headvo.setDbilldate(cl.getLogonDate());
//		 		headvo.setCdispatchid(pk_rdcl);
		 		headvo.setDr(0);
		 		headvo.setFdispatchflag(0);
//		 		headvo.setIdebtflag(0);
		 		headvo.setPk_corp(getValueStr(hm_head,"pk_corp"));
		 		
		 		headvo.setDbilldate(cevo.getDate());
		 		headvo.setCsourcemodulename("PO");
		 		headvo.setFdispatchflag(new Integer(0));

		 		headvo.setCoperatorid(cevo.getUser());

		 		headvo.setBestimateflag(new UFBoolean(false));//-----
		 		headvo.setCwarehousemanagerid(getValueStr(hm_head,"cwhsmanagerid"));
		 		headvo.setCemployeeid(getValueStr(hm_head,"coperatorid"));//

		 		headvo.setBwithdrawalflag(new UFBoolean(false));
		 		headvo.setBdisableflag(new UFBoolean(false));
		 		headvo.setBauditedflag(new UFBoolean(false));
		 		headvo.setDr(new Integer(0));
//		 		headvo.setVdef20("Y");//结案标识  通过后台更新字段 isjiean
		 		
		 		//构建子表
		 		BillItemVO[] bodyvos=new BillItemVO[arr_bodys.size()];
		 		BillItemVO itemvo=null;
		 		for (int i=0;i<arr_bodys.size();i++){
		 			HashMap hm_body = (HashMap)arr_bodys.get(i);
		 			itemvo=new BillItemVO();
		 			itemvo.setBadjustedItemflag(UFBoolean.FALSE);
		 			itemvo.setBauditbatchflag(UFBoolean.FALSE);
		 			itemvo.setBlargessflag(UFBoolean.FALSE);
		 			itemvo.setBretractflag(UFBoolean.FALSE);
		 			itemvo.setBrtvouchflag(UFBoolean.FALSE);
		 			itemvo.setBtransferincometax(UFBoolean.FALSE);
		 			itemvo.setCbilltypecode("IA");
//		 			itemvo.setCinvbasid(bvos[i].getCinvbasid());
		 			itemvo.setCinventoryid(getValueStr(hm_body,"cinventoryid"));
//		 			itemvo.setCrdcenterid(jfheadvo.getPk_calbody());
//		 			itemvo.setDbizdate(cl.getLogonDate());
		 			itemvo.setDr(0);
//		 			itemvo.setFcalcbizflag(0);
		 			itemvo.setFdatagetmodelflag(1);
		 			itemvo.setFolddatagetmodelflag(1);
		 			itemvo.setFoutadjustableflag(UFBoolean.FALSE);
		 			itemvo.setFpricemodeflag(4);
		 			itemvo.setIauditsequence(-1);
		 			itemvo.setIrownumber(1);
//		 			itemvo.setNmoney(getValueDou(hm_body,"ntaxmny"));
//		 			itemvo.setNsimulatemny(getValueDou(hm_body,"ntaxmny"));
		 			itemvo.setPk_corp(getValueStr(hm_head,"pk_corp"));
		 			
		 			itemvo.setCbilltypecode("I9");
		     
		    

		 			itemvo.setCsourcebillid(getValueStr(hm_body,"cgeneralhid"));
		 			itemvo.setCsourcebillitemid(getValueStr(hm_body,"cgeneralbid"));
		 			itemvo.setCsourcebilltypecode(getValueStr(hm_head,"cbilltypecode"));
		 			itemvo.setVsourcebillcode(getValueStr(hm_head,"vbillcode"));
		      
		      
					

					
					 UFDouble nnumber = getValueDou(hm_body,"ninnum");
					 itemvo.setNnumber(nnumber);
					 UFDouble nmoney = invdiffmny.get(getValueStr(hm_body,"cinventoryid"))==null?new UFDouble(0):
						 new UFDouble(invdiffmny.get(getValueStr(hm_body,"cinventoryid")).toString());
					 itemvo.setNmoney(nmoney);
					  
					
					UFDouble d1 = itemvo.getNmoney();
					UFDouble d2 = itemvo.getNnumber();
					if (d2 != null && d2.doubleValue() != 0.0) {
						double d = d1.doubleValue() / d2.doubleValue();
						itemvo.setNprice(new UFDouble(d));
					}

		 			itemvo.setCfirstbillid(getValueStr(hm_body,"cfirstbillhid"));
		 			itemvo.setCfirstbillitemid(getValueStr(hm_body,"cfirstbillbid"));
		 			itemvo.setCfirstbilltypecode(getValueStr(hm_body,"cfirsttype"));
		 			itemvo.setIrownumber(new Integer(i));
					
		 			itemvo.setDbizdate(getValueDate(hm_body,"dbizdate"));
		 			itemvo.setCvendorid(getValueStr(hm_body,"cvendorid"));
		 			itemvo.setDr(new Integer(0));
					
					//V5新增:库存入库单相关信息
		 			itemvo.setCicbilltype(getValueStr(hm_head,"cbilltypecode"));
		 			itemvo.setCicbillcode(getValueStr(hm_head,"vbillcode"));
		 			itemvo.setCicbillid(getValueStr(hm_body,"cgeneralhid"));
		 			itemvo.setCicitemid(getValueStr(hm_body,"cgeneralbid"));
		 			
		 			
		 			bodyvos[i]=itemvo;
		 		}
		 		BillVO billvo=new BillVO();
		 		billvo.setParentVO(headvo);
		 		billvo.setChildrenVO(bodyvos);
		 		billvo.setStatus(VOStatus.NEW);
		 		
		 		ClientLink cl = new ClientLink(cevo.getPk_corp(), cevo.getUser(), null, null, null, null,
		 		          null, null, null, false, null, null, null);

		 		
		 		IBill bo = (IBill) NCLocator.getInstance().lookup(IBill.class.getName());
		 		BillVO vo = bo.insert(cl,billvo);
		 		if(vo!=null){
		 			String cbillid = vo.getParentVO().getPrimaryKey();
		 			String sql = "update ia_bill set isjiean='Y' where cbillid='"+cbillid+"'";
		 			getBaseDAO().executeUpdate(sql);
		 		}
		
	}
	
	public HashMap getDjlx(String djlxbm,String pk_corp) throws BusinessException{
		String sql = "select djdl,djlxoid from arap_djlx where djlxbm='"+djlxbm+"' and dwbm='"+pk_corp+"' and nvl(dr,0)=0";
		return (HashMap)getBaseDAO().executeQuery(sql, new MapProcessor());
	}
	
	public void doJieAnARAP(HashMap hm_head,ArrayList arr_bodys,CeVO cevo,HashMap invdiffmny) throws BusinessException{
		

		  //表头VO
			DJZBHeaderVO djzbheadvo = new DJZBHeaderVO();
			djzbheadvo.setDjrq(cevo.getDate());         //单据日期
			djzbheadvo.setEffectdate(cevo.getDate()); //起算日期

			
			String djlxbm = "D1";
			String pk_busitype = getValueStr(hm_head,"cbiztype");
			HashMap<String, String> djlxMap = getDjlx(djlxbm, cevo.getPk_corp());
			
			if(djlxMap==null){
				throw new BusinessException("该公司没有分配交易类型");
			}
		    djzbheadvo.setDjlxbm(djlxbm);               //arap_djlx
		    djzbheadvo.setYwbm(djlxMap.get("djlxoid")); //arap_djlx djlxoid
//		    djzbheadvo.setBusitypename(djlxMap.get("djlxmc"));//arap_djlx djlxmc "销售收款单"
		    djzbheadvo.setDjdl(djlxMap.get("djdl")); ////arap_djlx djdl "sk"
		    
		    djzbheadvo.setXslxbm(pk_busitype);//add by wwl bd_busitype 00011110000000002RGT
//		    djzbheadvo.setXslxmc(getTableValue("bd_busitype", "businame", "pk_busitype", "arap")==null?null:getTableValue("bd_busitype", "businame", "pk_busitype", "arap").toString());//bd_busitype  收付通用流程
		    
		    djzbheadvo.setDeptid(getValueStr(hm_head,"cdptid"));//部门
//		    djzbheadvo.setDjzt(new Integer(1));
		    djzbheadvo.setDr(new Integer(0));
		    djzbheadvo.setFbhl(new UFDouble(0.00));
//		    djzbheadvo.setHzbz("-1");
		    djzbheadvo.setLybz(new Integer(4)); //加单据来源 by wwl
		    djzbheadvo.setDwbm(cevo.getPk_corp());//公司	: 对照      
//			djzbheadvo.setKmbm(skjsbvos[0].getPk_headsubject()); //表头科目
		    
			djzbheadvo.setBzbm("00010000000000000001");//币种	
			djzbheadvo.setDjkjnd(String.valueOf(cevo.getDate().getYear()));//年
			djzbheadvo.setDjkjqj(cevo.getDate().getMonth()<10?"0"+cevo.getDate().getMonth():String.valueOf(cevo.getDate().getMonth()));//月
			djzbheadvo.setDjzt(new Integer(1));//单据状态
			djzbheadvo.setLrr(cevo.getUser());//录入人PK 操作员
			djzbheadvo.setStatus(nc.vo.pub.VOStatus.NEW);//新增
			djzbheadvo.setPrepay(new UFBoolean(false));
			if(djlxMap.get("djdl").equals("ys")||djlxMap.get("djdl").equals("sk")){
			djzbheadvo.setPzglh(0);
			}else if(djlxMap.get("djdl").equals("yf")||djlxMap.get("djdl").equals("fk")){
			djzbheadvo.setPzglh(1);
			}
			djzbheadvo.setQcbz(new UFBoolean(false)); //期初标志
			djzbheadvo.setSsflag("1");
			djzbheadvo.setXtflag("保存");
			
			djzbheadvo.setZyx30("Y");//结案标识
			
			
			UFDouble sharemny = new UFDouble(0);//金额
			DJZBItemVO djzbitemvo = null;
			UFDouble rowMny = new UFDouble(0);
			ArrayList<DJZBItemVO> arr_DJZBVO = new ArrayList<DJZBItemVO>();
			
			

			for (int i=0;i<arr_bodys.size();i++){
	 			HashMap hm_body = (HashMap)arr_bodys.get(i);
			//表体VO
			 djzbitemvo=new DJZBItemVO();
			 //针对整单情况下，如果大于0,则在订单号前加"hz";
			//djzbitemvo.setDdh(saleHeadVo.getVreceiptcode()); // 自定义项19 EC订单号
//			djzbitemvo.setSzxmid(bvo.getPk_szxm());//收支项目 
	 		djzbitemvo.setBzmc("人民币");   //币种名称
			djzbitemvo.setBbhl(new UFDouble(1));        //汇率

			djzbitemvo.setJsfsbm("45");
			djzbitemvo.setCinventoryid(getValueStr(hm_body,"cinvbasid"));//存货基本档案
			djzbitemvo.setChbm_cl(getValueStr(hm_body,"cinventoryid"));//存货管理档案
			djzbitemvo.setCkdh(getValueStr(hm_head,"vbillcode"));
			djzbitemvo.setCkdid(getValueStr(hm_body,"cgeneralbid"));
			djzbitemvo.setDdhh(getValueStr(hm_body,"cgeneralbid"));
			djzbitemvo.setDdlx(getValueStr(hm_head,"cgeneralhid"));
			
			djzbitemvo.setDdhid(getValueStr(hm_body,"cfirstbillhid"));//订单主表主键
			djzbitemvo.setCksqsh(getValueStr(hm_body,"cfirstbillbid"));//订单子表主键
			djzbitemvo.setDdh(getValueStr(hm_body,"vfirstbillcode"));//订单号
			
			//djzbitemvo.setDdh(poapplyVO.getVbillno());//订单号 (采购申请单号)
			String pk_cubasdoc= getValueStr(hm_head,"pk_cubasdoc");//客商编号
			UFDouble ybmny = new UFDouble(0);
			UFDouble se = new UFDouble(0);
			
			 rowMny = invdiffmny.get(getValueStr(hm_body,"cinventoryid"))==null?new UFDouble(0):
				 new UFDouble(invdiffmny.get(getValueStr(hm_body,"cinventoryid")).toString());
			
			 //单差异金额大于等于0 不生成应付单
			 if(rowMny.doubleValue()>=0){
				 continue;
			 }
			 
			ybmny = rowMny.sub(se);
			//String pk_invbasdoc = bodyVo.getPk_invbasdoc();
			//if(pk_invbasdoc==null||pk_invbasdoc.toString().equals("")){
			  //djzbitemvo.setCinventoryid(pk_invbasdoc);
			  UFDouble ntaxrate = new UFDouble(0);//getNtaxrate(pk_invbasdoc);
			  //djzbitemvo.setSl(ntaxrate);
			  ;//不含税金额
			//}
			sharemny = sharemny.add(rowMny);//金额合计
			djzbitemvo.setBzbm("00010000000000000001");//币种PK	
//		    if(skjsbvos[0].getTranceobject().toString().equals("客商")){
//		    	djzbitemvo.setWldx(0);   //往来对象:客户
//			   }	 
//		    else if(skjsbvos[0].getTranceobject().toString().equals("供应商")){
		    	djzbitemvo.setWldx(1);   //往来对象:客户
//			    }	 
//			    else   if(skjsbvos[0].getTranceobject().toString().equals("部门")){
//			    	djzbitemvo.setWldx(2);   //往来对象:部门
//			    }
			
			if(djlxMap.get("djdl").equals("yf")||djlxMap.get("djdl").equals("sk")){
						djzbitemvo.setDfybsj(se);//贷方原币税金
//						djzbitemvo.setDffbsj(ybmny);//贷方辅币税金
						djzbitemvo.setDfbbsj(se);//贷方本币税金
						djzbitemvo.setDfybwsje(ybmny);//贷方原币无税金额
//						djzbitemvo.setWbffbje(sharemny.sub(ybmny));//贷方辅币无税金额 
						djzbitemvo.setDfbbwsje(ybmny);//贷方本币无税金额
						djzbitemvo.setDfybje(rowMny);//贷方原币金额
						djzbitemvo.setDfbbje(rowMny);//贷方本币金额
//						djzbitemvo.setDffbje(sharemny);//贷方辅币金额	
			            djzbitemvo.setDfshl(new UFDouble(0));	
				}else if(djlxMap.get("djdl").equals("ys")||djlxMap.get("djdl").equals("fk")){
			
			djzbitemvo.setJfybsj(se);//借方原币税金
			//djzbitemvo.setDffbsj(ybmny);//借方辅币税金
			djzbitemvo.setJfbbsj(se);//借方本币税金
			djzbitemvo.setJfybwsje(ybmny);//借方原币无税金额
			//djzbitemvo.setWbffbje(sharemny.sub(ybmny));//借方辅币无税金额 
			djzbitemvo.setWbfbbje(ybmny);//借方本币无税金额//wbfbbje
			djzbitemvo.setJfybje(rowMny);//借方原币金额
			djzbitemvo.setJfbbje(rowMny);//借方本币金额
				}
			//djzbitemvo.setDffbje(sharemny);//借方辅币金额	
	        //djzbitemvo.setDfshl(new UFDouble(0));	
						//djzbitemvo.setJfybje(ybmny);//借方原币金额
						//djzbitemvo.setJfybje(ybmny);//借方原币无税金额
					
						djzbitemvo.setBbye(rowMny);//本币余额
						djzbitemvo.setYbye(rowMny);//原币余额	
			djzbitemvo.setHbbm(pk_cubasdoc);    //客商: 
			djzbitemvo.setKsbm_cl(getValueStr(hm_head,"cproviderid"));//客商管理档案 
			djzbitemvo.setDeptid(getValueStr(hm_head,"cdptid"));   //部门:   
//			djzbitemvo.setDjxtflag(new Integer(0));
			djzbitemvo.setIsSFKXYChanged(new UFBoolean(false));
			djzbitemvo.setBjdwhsdj(new UFDouble(0));
			djzbitemvo.setBjdwsl(se);
			djzbitemvo.setBjdwwsdj(new UFDouble(0));
			//djzbitemvo.setDfbbsj(new UFDouble(0));
			//djzbitemvo.setDfybsj(new UFDouble(0));
			djzbitemvo.setDj(new UFDouble(0));		
			djzbitemvo.setDjzbitemsIndex(new Integer(1));
			djzbitemvo.setDr(new Integer(0));
			djzbitemvo.setDwbm(cevo.getPk_corp());//单位PK
			djzbitemvo.setFbhl(new UFDouble(0));
			djzbitemvo.setFbye(new UFDouble(0));		
			djzbitemvo.setFlbh(new Integer(1));//??
			djzbitemvo.setForelock(new UFDouble(0));
			djzbitemvo.setFx(new Integer(-1));//??
			djzbitemvo.setHsdj(new UFDouble(0));	//--------------------	
			djzbitemvo.setIsselected(new UFBoolean(false));
			djzbitemvo.setIsSqed(new UFBoolean(false));
			
			djzbitemvo.setKslb(new Integer(1));
			djzbitemvo.setOld_sys_flag(new UFBoolean(false));
			djzbitemvo.setOldbbye(new UFDouble(0));
			djzbitemvo.setOldfbye(new UFDouble(0));
			djzbitemvo.setOldybye(new UFDouble(0));
			djzbitemvo.setShlye(new UFDouble(0));
			djzbitemvo.setSl(new UFDouble(0));
			djzbitemvo.setSqflag(new Integer(0));
			djzbitemvo.setWbffbje(new UFDouble(0));		
			djzbitemvo.setWbfybje(new UFDouble(0));
			djzbitemvo.setXgbh(new Integer(-1));
			djzbitemvo.setYwbm(djlxMap.get("djlxoid"));//单据类型PK	
			djzbitemvo.setStatus(nc.vo.pub.VOStatus.NEW);		
			djzbitemvo.setTxlx_bbje(new UFDouble(0));
			djzbitemvo.setTxlx_fbje(new UFDouble(0));
			djzbitemvo.setTxlx_ybje(new UFDouble(0));
//			if(djlxMap.get("djdl").equals("sk")||djlxMap.get("djdl").equals("ys")){
//			djzbitemvo.setDfyhzh(bvo.getPk_custaccount());//付款帐号
//			djzbitemvo.setBfyhzh(bvo.getPk_bfaccount());//收款账户
//			}else if(djlxMap.get("djdl").equals("yf")||djlxMap.get("djdl").equals("fk")){
//				djzbitemvo.setDfyhzh(bvo.getPk_bfaccount());//付款帐号
//				djzbitemvo.setBfyhzh(bvo.getPk_custaccount());//收款账户
//			}
//			djzbitemvo.setKmbm(bvo.getPk_bodysubject()); //表体科目
//			djzbitemvo.setZy(bvo.getZy());//摘要
			
			djzbitemvo.setBilldate(cevo.getDate());
//			  for (int k = 1; k <= 20; k++) {
//				  djzbitemvo.setAttributeValue("zyx" + k, bvo.getAttributeValue("hzyx" + k));
//				  }
//			 djzbitemvo.setZyx30(bvo.getPrimaryKey());//设置表体pk
			arr_DJZBVO.add(djzbitemvo);
			}
			
			//下面会拆单 ，所以此处不设置表头金额
//			djzbheadvo.setYbje(sharemny);
//			djzbheadvo.setBbje(sharemny);
			
			
//		    DJZBVO djzbvo = new DJZBVO() ;
//			djzbvo.setParentVO(djzbheadvo);
//			djzbvo.setChildrenVO(arr_DJZBVO.toArray(new DJZBItemVO[0]));
			
			//一行一单
			if(arr_DJZBVO!=null&&arr_DJZBVO.size()>0){
				for(int i=0;i<arr_DJZBVO.size();i++){
				    DJZBVO djzbvo = new DJZBVO() ;
				    DJZBHeaderVO djzbheadvo_new =  (DJZBHeaderVO)djzbheadvo.clone();
				    
				    djzbheadvo_new.setYbje(((DJZBItemVO)arr_DJZBVO.get(i)).getDfybje());
				    djzbheadvo_new.setBbje(((DJZBItemVO)arr_DJZBVO.get(i)).getDfybje());
				    djzbheadvo_new.setFbje(new UFDouble(0));
				    
					djzbvo.setParentVO(djzbheadvo_new);
					djzbvo.setChildrenVO(new DJZBItemVO[]{(DJZBItemVO)arr_DJZBVO.get(i)});
					
					PfUtilBO remote = new PfUtilBO();
					String serverTime = nc.ui.pub.ClientEnvironment .getServerTime().toString();
				    try {
						remote.processAction("SAVE", djzbheadvo.getDjlxbm(), serverTime,null, djzbvo,null);
						int s=0;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						throw new BusinessException(e.getMessage());	
					}
					
				}
			}
			
		
		
	}
	
	
	public ArrayList doUNJieAn(ArrayList list) throws BusinessException {
		if(list!=null&&list.size()>0){
			JieanVO jvo = (JieanVO)list.get(0);
			CeVO cevo = (CeVO)list.get(1);
			String cgeneralhid = jvo.getIcgeneralhid();
			if(cgeneralhid==null||cgeneralhid.equals("")){
				throw new BusinessException("入库单主键为空！");
			}
			String cbillid = getJieAnI9Date(cgeneralhid);
			if(cbillid!=null&&!cbillid.equals("")){
				doUNJieAnI9(cbillid);
				
			}
			ArrayList vouchids = getJieAnARAPDate(cgeneralhid);
			if(vouchids!=null&&vouchids.size()>0){
				for(int i=0;i<vouchids.size();i++){
					HashMap hm = (HashMap)vouchids.get(i);
					String vouchid = hm==null?"":hm.get("vouchid")==null?"":hm.get("vouchid").toString();
					if(vouchid!=null&&!vouchid.equals("")){
						doUNJieAnARAP(vouchid);
					}
					
				}
				
				
			}
			
			backICStatus(cgeneralhid,status[2],cevo.getDate().toString());//zhwj 回写入库单状态
			
		}
		return null;
	}
	
	public void doUNJieAnI9(String cbillid) throws BusinessException{
		String sql = "update ia_bill set dr=1 where cbillid='"+cbillid+"'";
		getBaseDAO().executeUpdate(sql);
		sql = "update ia_bill_b set dr=1 where cbillid='"+cbillid+"'";
		getBaseDAO().executeUpdate(sql);
	}
	
	public void doUNJieAnARAP(String vouchid) throws BusinessException{
		String sql = "update arap_djzb set dr=1 where vouchid='"+vouchid+"'";
		getBaseDAO().executeUpdate(sql);
		sql = "update arap_djfb set dr=1 where vouchid='"+vouchid+"'";
		getBaseDAO().executeUpdate(sql);
	}
	
	
	
	public HashMap getCgeneralHData(String cgeneralhid) throws BusinessException{
		String sql = "select * from ic_general_h where cgeneralhid='"+cgeneralhid+"' and nvl(dr,0)=0";
		return (HashMap)getBaseDAO().executeQuery(sql, new MapProcessor());
	}
	
    public ArrayList getCgeneralBData(String cgeneralhid) throws BusinessException{
    	String sql = "select igb.* from ic_general_b igb inner join po_order_b pob on pob.corder_bid=igb.cfirstbillbid where igb.cgeneralhid='"+cgeneralhid+"' and nvl(pob.vdef17,'否')='是' and nvl(igb.dr,0)=0 and nvl(pob.dr,0)=0";
    	return (ArrayList)getBaseDAO().executeQuery(sql, new MapListProcessor());
	}
    
    public String getValueStr(HashMap hm,String key){
    	return hm==null?"":hm.get(key)==null?"":hm.get(key).toString();
    }
    
    public UFDouble getValueDou(HashMap hm,String key){
    	return hm==null?new UFDouble(0):hm.get(key)==null?new UFDouble(0):new UFDouble(hm.get(key).toString());
    }
    
    public UFDate getValueDate(HashMap hm,String key){
    	return hm==null?null:hm.get(key)==null?null:new UFDate(hm.get(key).toString());
    }
    

	//zhwj 结案后回写状态
	public void backICStatus(String cgeneralhid,String status,String date) throws BusinessException{
	
				//回写
//				String sql = "update ic_general_b set vuserdef19='"+status+"',vuserdef20='"+date+"' where cgeneralhid='"+cgeneralhid+"' where cgeneralbid in (select cgeneralbid )";
				StringBuffer sql = new StringBuffer();
				sql.append(" update ic_general_b igb set vuserdef19='"+status+"',vuserdef20='"+date+"' ")
					.append(" where cgeneralbid in ( ")
					.append(" select cgeneralbid from po_order_b pob  ")
					.append(" inner join ic_general_b igb  ")
					.append(" on igb.cfirstbillbid=pob.corder_bid  ")
					.append(" where igb.cgeneralhid='"+cgeneralhid+"'  ")
					.append(" and nvl(pob.vdef17,'否')='是'  ")
					.append(" and nvl(igb.dr,0)=0  ")
					.append(" and nvl(pob.dr,0)=0 ")
					.append(" ) ");
				getBaseDAO().executeUpdate(sql.toString());
	
		
		
	}
	
}
