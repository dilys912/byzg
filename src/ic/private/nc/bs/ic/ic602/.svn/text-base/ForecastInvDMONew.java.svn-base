package nc.bs.ic.ic602;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.ic.pub.InvATPDMO;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.ic602.ForecastinvVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.ATPVO;

/**
 * 库存展望的DMO类。
 *
 * 创建日期：(2001-8-9)
 * @author：Zhang Xin
 */
public class ForecastInvDMONew extends DataManageObject {
	
	String m_sForedate = null;
	ArrayList m_alICParams = null;
	Hashtable m_htICParams = null;
	boolean m_balreadyModuleAvailable = false;
	boolean isPurUsed = false;
	boolean isSaleUsed = false;
	boolean isMMUsed = false;
	boolean isSCUsed = false;

	//库存展望中引用的视图常量
	final String v_ic = "v_ic_onhandnum4";
	boolean m_bFreeExtend=false;
	boolean m_bBatchExtend=false;
	boolean m_bWhExtend=false;
	Hashtable m_htSchemeCode=new Hashtable();

/**
 * 构造子注解。
 *
 * @exception javax.naming.NamingException 父类构造子抛出的异常。
 * @exception nc.bs.pub.SystemException 父类构造子抛出的异常。
 */
public ForecastInvDMONew() throws javax.naming.NamingException, SystemException {
	super();
}
/**
 * 构造子注解。
 *
 * @param dbName java.lang.String 在EJB Server中配置的数据库DataSource名称。
 * @exception javax.naming.NamingException 父类构造子抛出的异常。
 * @exception nc.bs.pub.SystemException 父类构造子抛出的异常。
 */
public ForecastInvDMONew(String dbName) throws javax.naming.NamingException, SystemException {
	super(dbName);
}













/**预计库存数量=现存量+预计入库数量-预计出库数量
	可用量=预计库存-（冻结量（根据定义参数确定是否参与运算）- 借入数量（根据定义参数确定是否参与运算））
 * 此处插入方法说明。
 * 创建日期：(2001-9-14 12:42:18)
 */
private void calculateQtyNew(CircularlyAccessibleValueObject cvos[]) {
	if (cvos != null && cvos.length > 0) {
		//Hashtable htKey = new Hashtable();

		UFDouble ufd0 = new UFDouble(0);
		UFDouble foreIn = null,
			foreOut = null,
			nforecastnum = null,
			restnum = null,
			navailablenum = null;
		UFDouble[] detailitem = null;
		/*{
			nforecastnum,
			restnum,
			navailablenum,
			npraynum,
			npurchaseordernum,
			naccumchecknum,
			nshldtransinnum,
			nplannedordernum,
			nmanufordernum,
			nborrownum,
			nfreezenum,
			nsaleordernum,
			ndelivernum,
			nshldtransoutnum,
			npreparematerialnum ,
			nwwnum }; */
		String[] keys = { "nforecastnum", "restnum", "navailablenum", "npraynum", //3
			"npurchaseordernum", "naccumchecknum", "nshldtraninnum", //6
			"nplannedordernum", "nmanufordernum", "nborrownum", //9
			"nfreezenum", "nsaleordernum", //11
			"ndelivernum", "nshldtranoutnum", //13
			"npreparematerialnum", "nwwnum", "nsafestocknum",//16
			"npreordernum","ntranpraynum"};
		ForecastinvVO cvo = null;
		for (int i = 0; i < cvos.length; i++) {
			cvo = (ForecastinvVO) cvos[i];

			/*UFDouble nforecastnum = null, restnum = null, navailablenum = null,
			// 预计入 
				npraynum = null,
				npurchaseordernum = null,
				naccumchecknum = null,
				nshldtransinnum = null,
				nplannedordernum = null,
				nmanufordernum = null,
				nwwnum = null,
			// freezenum and  borrownum 
			nborrownum = null, nfreezenum = null,
			// 预计出 
			nsaleordernum = null,
				ndelivernum = null,
				nshldtransoutnum = null,
				npreparematerialnum = null;*/

			detailitem = new UFDouble[keys.length];
			for (int j = 0; j < keys.length; j++) {
				detailitem[j] =
					cvo.getAttributeValue(keys[j]) == null
						? ufd0
						: (UFDouble) cvo.getAttributeValue(keys[j]);
			}
			/** to calculate the ForeInvIN */
			foreIn =
				detailitem[3]
					.add(detailitem[4])
					.add(detailitem[5])
					.add(detailitem[6])
					.add(detailitem[7])
					.add(detailitem[8])
					.add(detailitem[15])
						.add(detailitem[18]);
			/** to calculate the ForeInvOUT */
			foreOut =
				detailitem[11].add(detailitem[12]).add(detailitem[13]).add(detailitem[14]).add(detailitem[17]);
			/** to gain the restnum */
			restnum = detailitem[1];
			/** to calculate the forecastnum */
			nforecastnum = restnum.add(foreIn).sub(foreOut);

			/** to calculate the availablenum */
			/** 可用量中应根据用户定义的参数，减去：10 借入数量。9 冻结数量。 */
			navailablenum =
				nforecastnum.sub(detailitem[10]).sub(detailitem[9]).sub(detailitem[16]);
			//由于相同的存货不能同时减安全库存, 16 安全库存数量,根据htKey判断
			//if (!htKey.containsKey(cvo.getCinventoryid())) {
			//navailablenum = navailablenum.sub(detailitem[16]);
			//htKey.put(cvo.getCinventoryid(), cvo);
			//}
			//debug zhx 1212
			//nc.vo.scm.pub.SCMEnv.out("/** 可用量中应根据用户定义的参数，减去：10 借入数量。9 冻结数量。16 安全库存数量 */");

			/** put item qty to the VO */
			cvo.setAttributeValue("nforeIN", foreIn);
			cvo.setAttributeValue("nforeOUT", foreOut);
			cvo.setAttributeValue("nforecastnum", nforecastnum);
			cvo.setAttributeValue("navailablenum", navailablenum);
			
			//将为零的值设为空
			for (int j = 0; j < keys.length; j++) {
				if (cvo.getAttributeValue(keys[j]) == null) {
				} else if (((UFDouble) cvo.getAttributeValue(keys[j])).equals(ufd0))
					cvo.setAttributeValue(keys[j], null);
			}
		}
	}

}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-3-11 9:46:52)
 * @param vo nc.vo.scm.ic.ATPVO
 */
private void dealSchemeCode(ATPVO vo) {
				char a= '1';
				
						//获取方案号
					String schemecode = (String) vo.getAttributeValue("usableamount");
					if (schemecode == null)
						schemecode = "0000000000000000";
					//请购单0
					if (!(schemecode.charAt(0)==a))
						vo.setNonrequirenum(null);
		
					//生产定单5
					if (!(schemecode.charAt(5)==a))
						vo.setNmonum(null);
					//计划定单4	
					if (!(schemecode.charAt(4)==a))
						vo.setNmponum(null);
					//发货单8	
					if (!(schemecode.charAt(8)==a))
						vo.setNonreceiptnum(null);
					//采购定单1	
					if (!(schemecode.charAt(1)==a))
						vo.setNonponum(null);
					//到货待检2
					if (!(schemecode.charAt(2)==a))
						vo.setNonreceivenum(null);
					//委外	6
					if (!(schemecode.charAt(6)==a))
						vo.setNonwwnum(null);
					//销售定单7	
					if (!(schemecode.charAt(7)==a))
						vo.setNonsonum(null);
					//备料计划	10
					if (!(schemecode.charAt(10)==a))
						vo.setNpickmnum(null);
					//转入	3
					if (!(schemecode.charAt(3)==a))
						vo.setNtraninnum(null);
					//转出	9
					if (!(schemecode.charAt(9)==a))
						vo.setNtranoutnum(null);
					//调拨申请14
					if (!(schemecode.charAt(14)==a))
						vo.setNontranspraynum(null);		
					//预订单15
					if (!(schemecode.charAt(15)==a))
						vo.setNonpreordernum(null);				
		
					//如果要减去借入量11
					if (!(schemecode.charAt(11)==a))
						vo.setNborrownum(null);
					//如果要减去安全库存13
					if (!(schemecode.charAt(13)==a))
						vo.setNsafenum(null);
					//如果要减去冻结12
					if (!(schemecode.charAt(12)==a))
						vo.setNfreezenum(null);
					
		
	
	
	}

   private String getCorpWhere(String[] saCorp){
       if (saCorp == null||saCorp.length<1)
           return null;
       
   	StringBuffer sbWhere = new StringBuffer();
	
	sbWhere.append(" and ( ");
	for (int i=0;i<saCorp.length;i++){
		if (i==saCorp.length-1) {
			sbWhere.append("catpcorpid=").append("'").append(saCorp[i]).append("'");
			continue;
		}
		
		sbWhere.append("catpcorpid=").append("'").append(saCorp[i]).append("'").append(" or ");
		
	}
	sbWhere.append(" )");
	
	return sbWhere.toString();
   }
/**
 * 使用查询条件VO拼接SQL语句。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2001-8-9 20:55:41)
 * 修改日期，修改人，修改原因，注释标志：
 */
private ArrayList getATP(ArrayList alParams) throws BusinessException {
	//String pk_corp = null;
	

	ArrayList alRet = new ArrayList();
	/** 是否按自由项展开（默认为否） */

	//多公司条件
	String[] saCorp = (String[]) alParams.get(0);
	if (saCorp == null) return null;
	String whereCorp = getCorpWhere(saCorp);
	
	ConditionVO[] aryConVO = (ConditionVO[]) alParams.get(1);
	StringBuffer sbWhere = null;
	StringBuffer sbFreeWhere=null;
	if (aryConVO != null && aryConVO.length > 0) {
		sbWhere = new StringBuffer(" 0=0 ");
		sbWhere.append(whereCorp);
		for (int i = 0; i < aryConVO.length; i++) {
			if (aryConVO[i].getOperaCode().equals("like"))
				aryConVO[i].setValue(aryConVO[i].getValue() + "%");
			
	
			
//			
//			if (aryConVO[i].getFieldCode().equals("pk_corp")) {
//				pk_corp = aryConVO[i].getRefResult().getRefPK().trim();
//				ConditionVO voCon=(ConditionVO)aryConVO[i].clone();
//				voCon.setFieldCode("catpcorpid");
//				sbWhere.append(voCon.getSQLStr());
//				continue;
//			}
			
			else if (aryConVO[i].getFieldCode().equals("ccalbodyid")) {
				sbWhere.append(aryConVO[i].getSQLStr());
				continue;
			}else if (aryConVO[i].getFieldCode().startsWith("inv")) {
				sbWhere.append(aryConVO[i].getSQLStr());
				continue;
			}
							
			else if (aryConVO[i].getFieldCode().equals("vfree0")
				||aryConVO[i].getFieldCode().equals("iswhext")
				||aryConVO[i].getFieldCode().equals("isbatchext")) {
			
				continue;

			}
			if(sbFreeWhere==null){
				sbFreeWhere=new StringBuffer();
				String s=aryConVO[i].getSQLStr();
				int index=s.indexOf("and");
				if(index<0)
					index=s.indexOf("or");
				
				sbFreeWhere.append(s.substring(index+3));
				continue;
			}
			sbFreeWhere.append(aryConVO[i].getSQLStr());

		}
//		if (pk_corp == null || pk_corp.trim().length() <= 0) {
//			pk_corp = alParams.get(0) == null ? null : alParams.get(0).toString();
//
//		}

	}
	
	
	//已经构造完查询ATP的参数组
	ATPVO[] atpVOs = null;
	InvATPDMO atpDMO = null;
	ArrayList alvo=new ArrayList();
	try {
		atpDMO = new InvATPDMO();
	
		atpVOs=atpDMO.queryAtpAllNum(null,null,
				new UFBoolean(m_bFreeExtend),
				new UFBoolean(false),
				new UFBoolean(m_bBatchExtend),
				new UFBoolean(m_bWhExtend),
				sbWhere.toString(),
				sbFreeWhere==null?null:sbFreeWhere.toString());
			//处理自定义存货预计入,预计出;
			if (atpVOs != null) {
				
				for (int i = 0; i < atpVOs.length; i++) {
					
					dealSchemeCode(atpVOs[i]);
					if(atpVOs[i].isZero()
							&&(atpVOs[i].getNonhandnum()==null||atpVOs[i].getNonhandnum().doubleValue()==0.0)
							&&(atpVOs[i].getNborrownum()==null||atpVOs[i].getNborrownum().doubleValue()==0.0)
							&&(atpVOs[i].getNfreezenum()==null||atpVOs[i].getNfreezenum().doubleValue()==0.0)
							&&(atpVOs[i].getNsafenum()==null||atpVOs[i].getNsafenum().doubleValue()==0.0))
							continue;
					
					alvo.add(atpVOs[i]);
	
					
				}
		
			}
			
	} catch (Exception ex) {
		nc.vo.scm.pub.SCMEnv.error(ex);
    //库存组异常抛出规范
    throw nc.bs.ic.pub.GenMethod.handleException(ex.getMessage(), ex);
	}

	//将得到的三个ATPVO合并到atpVOs中
//	atpVOs = combineATPVOs(atpVOs, atpicVOs, atpsqVOs, m_bFreeExtend);
	//将atpVOs[] 转换成ForcastVO[] 
	ForecastinvVO[] fvos=null;
	if(alvo.size()>0){
		ATPVO[] vostmps=new ATPVO[alvo.size()];
		alvo.toArray(vostmps);
		fvos = transferVOs(vostmps);
	
	}
	
	//计算预计出,入数量,计算可用量;
	calculateQtyNew(fvos);
	if (fvos != null && fvos.length != 0) {
		for (int i = 0; i < fvos.length; i++) {

			if (fvos[i] != null && fvos[i].getAttributeValue("cinventoryid") != null) {
				fvos[i].setAttributeValue("querydate", (String) alParams.get(2));
				fvos[i].setAttributeValue("foredaynum", (String) alParams.get(3));
				alRet.add(fvos[i]);
			}
			
			//Eric  
			String pk = fvos[i].getCinventoryid();
			String batchcode = fvos[i].getVbatchcode();
			String warehouseid = fvos[i].getCwarehouseid();
			String sql = "select count(1) from ic_onhandnum where cinventoryid='"+pk+"' and nonhandnum<>0 ";
			if(m_bFreeExtend){
				fvos[i].setDs(1);
			}else{
				if(batchcode!=null&&!"".equals(batchcode)){
					sql = sql.concat("and vlot ='"+batchcode+"' ");
				}
				if(warehouseid!=null&&!"".equals(warehouseid)){
					sql = sql.concat("and cwarehouseid ='"+warehouseid+"' ");
				}
				BaseDAO dao = new BaseDAO();
				Object obj = dao.executeQuery(sql, new ColumnProcessor());
				Integer icount = obj==null?0:new Integer(obj.toString());
					fvos[i].setDs(icount);
				 
			}
			UFDouble navailablenum = fvos[i].getNavailablenum()==null?new UFDouble(0):fvos[i].getNavailablenum();
			//eric 根据可用量可用垛数
			if(navailablenum.doubleValue()>0)
				fvos[i].setAttributeValue("kyds", 1);
			else 
				fvos[i].setAttributeValue("kyds", 0);
		}
	}

	return alRet;

}

/**
 * 使用查询条件VO拼接SQL语句。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2001-8-9 20:55:41)
 * 修改日期，修改人，修改原因，注释标志：
 */
protected ForecastinvVO getATPDetail(ForecastinvVO fvo) throws BusinessException{
	String pk_corp = null, storeorg = null, cinventoryid = null;
	
	String[] free=null;
	
	if (fvo != null) {
		pk_corp = fvo.getPk_corp();
		storeorg = (String) fvo.getAttributeValue("pk_calbody");
		cinventoryid = fvo.getCinventoryid();
	
		if(fvo.m_isFreeExt&&fvo.getFreeItemVO()!=null){
			free=new String[]{
					fvo.getFreeItemVO().getVfree1(),
					fvo.getFreeItemVO().getVfree2(),
					fvo.getFreeItemVO().getVfree3(),
					fvo.getFreeItemVO().getVfree4(),
					fvo.getFreeItemVO().getVfree5()
					};
			
		
		}

	}

	//已经构造完查询ATP的参数组
	//调用公共类查询ATP数据
	ATPVO[] atpVOs = null;

	InvATPDMO atpDMO = null;
	try {
		atpDMO = new InvATPDMO();
		ArrayList alParam=new ArrayList();
		alParam.add(pk_corp);
		alParam.add(storeorg);
		alParam.add(cinventoryid);
		alParam.add(free);
		alParam.add(fvo.getCwarehouseid());
		alParam.add(fvo.getVbatchcode());
		UFDate startdate=new UFDate((String)fvo.getAttributeValue("querydate"));
		UFDate enddate=startdate.getDateAfter(Integer.parseInt((String) fvo.getAttributeValue("foredaynum")));
		//查询ATP
		atpVOs =atpDMO.getAtpNumByDate(alParam,startdate,enddate,new UFBoolean(fvo.m_isFreeExt),new UFBoolean(fvo.m_isBatchExt),new UFBoolean(fvo.m_isWhExt));
		
	} catch (Exception ex) {
		nc.vo.scm.pub.SCMEnv.error(ex);
    //库存组异常抛出规范
    throw nc.bs.ic.pub.GenMethod.handleException(ex.getMessage(), ex);
	}
	if (atpVOs != null && atpVOs.length > 0) {
		UFDouble[] aryshldtraninnum = new UFDouble[atpVOs.length];
		UFDouble[] aryshldtranoutnum = new UFDouble[atpVOs.length];
		UFDouble[] arypraynum = new UFDouble[atpVOs.length];
		UFDouble[] arypurchaseordernum = new UFDouble[atpVOs.length];
		UFDouble[] aryaccumchecknum = new UFDouble[atpVOs.length];
		UFDouble[] arysaleordernum = new UFDouble[atpVOs.length];
		UFDouble[] arydelivernum = new UFDouble[atpVOs.length];
		UFDouble[] arymanufordernum = new UFDouble[atpVOs.length];
		UFDouble[] arypreparematerialnum = new UFDouble[atpVOs.length];
		UFDouble[] aryplannedordernum = new UFDouble[atpVOs.length];
		UFDouble[] arywwnum = new UFDouble[atpVOs.length];
		UFDouble[] arypreordernum = new UFDouble[atpVOs.length];
		UFDouble[] arytranpraynum = new UFDouble[atpVOs.length];
		ATPVO vo=null;
		for (int i = 0;i<atpVOs.length;i++) {
			vo=atpVOs[i];
			if(i>0)
				vo.setNsafenum(null);
			dealSchemeCode(vo);
			//采购到货数量
			aryaccumchecknum[i] = vo.getNonreceivenum();
			
			//销售发货数量
			arydelivernum[i] = vo.getNonreceiptnum();
			//生产计划订单
			//fvos[i].setAttributeValue("nplannedordernum",atpVOs[i].getNmponum());
			aryplannedordernum[i] = vo.getNmponum();
			//生产订单数量
			arymanufordernum[i]=vo.getNmonum();
			//请购数量
			arypraynum[i] = vo.getNonrequirenum();
			//备料计划数量
			arypreparematerialnum[i]=vo.getNpickmnum();
			//销售订单数量
			arysaleordernum[i] = vo.getNonsonum();
			//转入数量
			aryshldtraninnum[i] = vo.getNtraninnum();
			//转出数量
			aryshldtranoutnum[i] = vo.getNtranoutnum();
			//委外订单数量
			arywwnum[i]=vo.getNonwwnum();
			//采购订单数量
			arypurchaseordernum[i] = vo.getNonponum();
			//调拨申请
			arytranpraynum[i] = vo.getNontranspraynum();
			//预订单
			arypreordernum[i] = vo.getNonpreordernum();

		}
		fvo.setM_aryaccumchecknum(aryaccumchecknum);
		fvo.setM_arydelivernum(arydelivernum);
		fvo.setM_arymanufordernum(arymanufordernum);
		fvo.setM_aryplannedordernum(aryplannedordernum);
		fvo.setM_arypraynum(arypraynum);
		fvo.setM_arypreparematerialnum(arypreparematerialnum);
		fvo.setM_arypurchaseordernum(arypurchaseordernum);
		fvo.setM_arysaleordernum(arysaleordernum);
		fvo.setM_aryshldtraninnum(aryshldtraninnum);
		fvo.setM_aryshldtranoutnum(aryshldtranoutnum);
		fvo.setM_arywwnum(arywwnum);
		fvo.setM_arypreordernum(arypreordernum);
		fvo.setM_arytranpraynum(arytranpraynum);
		

	}
	//将得到的三个ATPVO合并到atpVOs中
	
	
	//nc.vo.scm.pub.SCMEnv.out(sql.toString());
	return fvo;

}

/**
 * 
 *
 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。
 *			如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
 *			sql语句。
 * 创建日期：(2001-8-9)
 * @return ArrayList
 * @param ArrayList, ArrayList
 * @exception java.sql.SQLException ,RemoteException异常说明。
 */
protected ArrayList queryForecastInvQtyBatch(ArrayList alParams)
	throws BusinessException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.ic.ic602.ForecastInvDMONew",
		"queryForecastInvQtyBatch",
		new Object[] { alParams });
	/*************************************************************/
	ConditionVO[] convo = (ConditionVO[]) alParams.get(1);
	
	for (int i = 0; i < convo.length; i++) {
		if (convo[i].getFieldCode().equalsIgnoreCase("vfree0")) {
			if (convo[i].getValue().equalsIgnoreCase("Y")) {
				m_bFreeExtend = true;

			} else {
				m_bFreeExtend = false;
			}

		}
		else if (convo[i].getFieldCode().equals("isbatchext")) {
			if (convo[i].getValue().equalsIgnoreCase("Y")) {
				m_bBatchExtend = true;

			} 

		}
		else if (convo[i].getFieldCode().equals("iswhext")) {
			if (convo[i].getValue().equalsIgnoreCase("Y")) {
				m_bWhExtend = true;

			} 

		}
	}

	ArrayList alalldata = getATP(alParams);
	

	////
	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.ic.ic622.RefbookDMO",
		"queryAll",
		new Object[] { alParams });
	/*************************************************************/

	return alalldata;
}

/**
 * 将ATPVO数组转换为ForecastinvVO数组,主要转换下列属性:公司,库存组织,存货管理主键,十个自由项,起始日期,结束日期,数量字段
 * 功能描述:
 * 输入参数:ATPVO[]
 * 返回值:ForecastinvVO[]
 * 异常处理:
 * 日期:20030730
 */
private ForecastinvVO[] transferVOs(ATPVO[] atpVOs) {
	ForecastinvVO[] fvos = null;
	if (atpVOs != null && atpVOs.length > 0) {
		ArrayList alret=new ArrayList();
		
		for (int i = 0; i < atpVOs.length; i++) {
			
//			if(atpVOs[i].isZero())
//				continue;
			
			ForecastinvVO fvo= new ForecastinvVO();
			//公司主键
			fvo.setPk_corp(atpVOs[i].getPk_corp());
			//存货管理主键
			fvo.setCinventoryid(atpVOs[i].getCinventoryid());
			//库存组织
			fvo.setAttributeValue("pk_calbody", atpVOs[i].getCcalbodyid());
			//十个自由项
			fvo.setFreeItemValue("vfree1", atpVOs[i].getVfree1());
			fvo.setFreeItemValue("vfree2", atpVOs[i].getVfree2());
			fvo.setFreeItemValue("vfree3", atpVOs[i].getVfree3());
			fvo.setFreeItemValue("vfree4", atpVOs[i].getVfree4());
			fvo.setFreeItemValue("vfree5", atpVOs[i].getVfree5());
			fvo.setFreeItemValue("vfree6", atpVOs[i].getVfree6());
			fvo.setFreeItemValue("vfree7", atpVOs[i].getVfree7());
			fvo.setFreeItemValue("vfree8", atpVOs[i].getVfree8());
			fvo.setFreeItemValue("vfree9", atpVOs[i].getVfree9());
			fvo.setFreeItemValue("vfree10", atpVOs[i].getVfree10());
			fvo.setVbatchcode( atpVOs[i].getVbatchcode());
			fvo.setCwarehouseid(atpVOs[i].getCwarehouseid());
			//采购到货数量
			fvo.setNaccumchecknum(atpVOs[i].getNonreceivenum());
			//采购订单数量
			fvo.setAttributeValue("npurchaseordernum", atpVOs[i].getNonponum());
			//借入数量
			fvo.setNborrownum(atpVOs[i].getNborrownum());
			//销售发货数量
			fvo.setNdelivernum(atpVOs[i].getNonreceiptnum());
			//冻结数量
			fvo.setNfreezenum(atpVOs[i].getNfreezenum());
			//生产订单数量
			fvo.setNmanufordernum(atpVOs[i].getNmonum());
			//请购数量
			fvo.setNpraynum(atpVOs[i].getNonrequirenum());
			//备料计划数量
			fvo.setNpreparematerialnum(atpVOs[i].getNpickmnum());
			//生产计划订单
			fvo.setAttributeValue("nplannedordernum", atpVOs[i].getNmponum());
			//销售订单数量
			fvo.setNsaleordernum(atpVOs[i].getNonsonum());
			//转入数量
			fvo.setNshldtraninnum(atpVOs[i].getNtraninnum());
			//转出数量
			fvo.setNshldtranoutnum(atpVOs[i].getNtranoutnum());
			//委外订单数量
			fvo.setNwwnum(atpVOs[i].getNonwwnum());
			//现存量
			fvo.setRestnum(atpVOs[i].getNonhandnum());
			//安全库存
			fvo.setAttributeValue("nsafestocknum",atpVOs[i].getNsafenum());
			//调拨申请
			fvo.setAttributeValue("ntranpraynum",atpVOs[i].getNontranspraynum());
			//预订单
			fvo.setAttributeValue("npreordernum",atpVOs[i].getNonpreordernum());
			alret.add(fvo);
		}

		if(alret.size()>0){
			fvos = new ForecastinvVO[alret.size()];
			alret.toArray(fvos);
		}
	}
	return fvos;
}
}