package nc.impl.mm.mm6600;
 
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import nc.bs.bd.languagetransformations.Transformations;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.trade.business.HYPubBO;
import nc.itf.mm.scm.mm6600.IMo6600;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.common.ListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class IMo6600Impl implements IMo6600
{
	public GlBillVO saveBill(GlBillVO billVO) throws Exception
	{
		HYPubBO pubbo = new HYPubBO();
		
		//删除行
		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
		String pkGlzb = ghead.getPk_glzb(); 
		if(pkGlzb!=null&&!pkGlzb.trim().equals(""))
		{
			GlItemBVO[] old = (GlItemBVO[])pubbo.queryByCondition(GlItemBVO.class,"pk_glzb='"+pkGlzb+"'");
			boolean flag ;
			if(old!=null&&old.length>0)
			{
				for(int i=0;i<old.length;i++)
				{
					flag = false;
					for(int j=0;j<vos.length;j++)
					{
						//edit by yhj 2014-03-27
						if(vos[j].getPk_glzb_b() != null){
							if(vos[j].getPk_glzb_b().equals(old[i].getPk_glzb_b()))
							{
								flag = true;
								break;
							}
						}
						//end
					}
					if(!flag)
					{
						pubbo.delete(old[i]);
					}
				}
			}
		}
		//保存单据
		billVO = (GlBillVO) pubbo.saveBill(billVO);
		
		return billVO;
	}
	public void confirm(GlBillVO billVO) throws Exception 
	{
		// TODO Auto-generated method stub
  
		if(billVO!=null)
		{
		  try
			{
			  GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
			  GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
			  if(checkFreeItemAndBatchAndSpaceInput( vos,ghead.getPk_corp(),ghead.getCk()))
				{
					
				}
			 
				 HYPubBO pubDao = new HYPubBO();
				 String calbody=(String) pubDao.findColValue("BD_CALBODY", "PK_CALBODY", "bodycode='01' AND pk_corp='"+ghead.getPk_corp()+"'");
			  if(!IsNotLockAccount(ghead.getLogindate().toString(),calbody))
				{
					throw new Exception("当期期间已关帐不能在入库！The current period is closed during the account can not be in storage!");
				}
			//生成入库单
				GeneralBillVO bo = this.getRkVO(billVO);			
				Object obj = new PfUtilBO().processAction("SAVE", "46", bo.getHeaderVO().getDbilldate().toString(), null, bo, null);//("SAVE","4C", voHead.getDbilldate().toString(), voTempBill);
			//add by zwx 2019-9-29 将生成成功的产成品入库单增加【制单人】
				if(ghead.getPk_corp().equals("1078")||ghead.getPk_corp().equals("1108")){
					if(obj!=null&&obj.toString().length()>0){
						String zdr = InvocationInfoProxy.getInstance().getUserCode();
						StringBuffer updSql = new StringBuffer();
						updSql.append(" update ic_general_h set coperatorid = '' ") 
						.append(" where cgeneralhid = '"+obj.toString()+"' ") 
						.append(" and pk_corp = '"+ghead.getPk_corp()+"' ") 
						.append(" and nvl(dr,0) = 0 ") ;
						JdbcSession session = PersistenceManager.getInstance().getJdbcSession();
						session.executeUpdate(updSql.toString());  
					}
				}
				
			//end by zwx
			
			//更新单据状态为已确认
			billVO.getParentVO().setAttributeValue("djzt", 1);
			HYPubBO pubbo = new HYPubBO();
			pubbo.saveBill(billVO);
			
			
			//更新生产订单入库数量、完工数量
//			double rksl = ghead.getZdsl()*vos.length;//入库数量
			double rksl = 0;
			for(GlItemBVO item : vos)
			{
				rksl += (item.getXxaglsl()==null?0:item.getXxaglsl().intValue());
			}
			String sql = "update mm_mo set rksl=nvl(rksl,0.0)+"+rksl+",sjwgsl=nvl(sjwgsl,0.0)+"+rksl+"  where pk_moid='"+billVO.getParentVO().getAttributeValue("scddh") + "'";
			JdbcSession session = PersistenceManager.getInstance().getJdbcSession();
			session.executeUpdate(sql);  
			
			//add by shikun 2014-05-26 检验生产订单完工数量与其下游的下线检验数量（确认态）是否相等，如果不等则说明该次的回写订单数量不正确。
			BaseDAO dao = new BaseDAO();
			//先查询订单实际完工数量
			StringBuffer sb1 = new StringBuffer();
			sb1.append(" select mo.sjwgsl from mm_mo mo where nvl(mo.dr,0) = 0 and mo.pk_moid = '"+billVO.getParentVO().getAttributeValue("scddh")+"'") ;
			Object num1 = dao.executeQuery(sb1.toString(), new ColumnProcessor());
			//再查询订单下游确认态的下线检验单的下线数量汇总
			StringBuffer sb2 = new StringBuffer();
			sb2.append(" select sum(nvl(b.xxaglsl, 0)) ") 
			.append("   from mm_glzb_b b ") 
			.append("  where nvl(b.dr, 0) = 0 ") 
			.append("    and b.pk_glzb in (select a.pk_glzb ") 
			.append("                        from mm_glzb a ") 
			.append("                       where nvl(a.dr, 0) = 0 ") 
			.append("                         and a.djzt = 1 ") 
			.append("                         and a.scddh = '"+billVO.getParentVO().getAttributeValue("scddh")+"') ") ;
			Object num2 = dao.executeQuery(sb2.toString(), new ColumnProcessor());
			if (num2!=null&&num1!=null) {
				UFDouble number1 = new UFDouble(num1.toString());
				UFDouble number2 = new UFDouble(num2.toString());
				if (number1.doubleValue()!=number2.doubleValue()) {
					
					//ediy by yqq 2017-02-17
				//	throw new BusinessException("该单据的生产订单实际完工数量与汇总下线数量不等，不能确认！");             		
                	String sq3 = "update mm_mo set sjwgsl="+num2+"  where pk_moid='"+billVO.getParentVO().getAttributeValue("scddh") + "' and pk_corp =  '"+ghead.getPk_corp()+"'";
                	JdbcSession session1 = PersistenceManager.getInstance().getJdbcSession();
                	session1.executeUpdate(sq3);   
                	//end by yqq 2017-02-17
				}
			}
			//end shikun 2014-05-26
			
			}
		  catch(Exception e)
		  {
			// if(new File("/ibm/NC5011/modules/mm/nclog.txt").exists())
			 {
				 
			 }
			//  BufferedReader in = new BufferedReader();
			  throw new BusinessException(e.getMessage());
			  
		  }
		  finally
		  {
			  
		  }
		}  
	}
	
	protected  GeneralBillVO getRkVO(GlBillVO billVO) throws Exception
	{
		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
		HYPubBO dao = new HYPubBO();
		
		GeneralBillVO bo = new GeneralBillVO();
		GeneralBillItemVO[] itemvos  = new GeneralBillItemVO[vos.length];
		bo.setLockOperatorid(ghead.getLrr());
		UFDate logindate=ghead.getLogindate();
		// CLASTMODIID
		GeneralBillHeaderVO head = bo.getHeaderVO(); 
		head.setStatus(2);// 新增
		bo.setParentVO(head);
		bo.setChildrenVO(itemvos);
		
		head.setCbilltypecode("46"); //库存单据类型编码  46-产成品入库单
		head.setCbizid(ghead.getXxry()); 	//业务员ID
		head.setCdispatcherid("1085A210000000001P2B"); //收发类型ID ? “产成品入库” 在产成品入库界面可参照   
//		head.setCdptid(ghead.getBz()); //部门ID  ？
//		head.setCgeneralhid(ghead.getPk_glzb()); //主键
		head.setCoperatorid(ghead.getLrr());//制单人
		head.setCwarehouseid(ghead.getCk());//仓库ID
//		head.setCwhsmanagerid(null);//库管员 
		head.setDbilldate(ClientEnvironment.getServerTime().getDate());//单据日期
		head.setFbillflag(null);//单据状态
//		head.setPk_calbody();//库存组织PK
		head.setPk_corp(ghead.getPk_corp());//公司ID
		head.setVbillcode(null); //单据号
		head.setVnote("成品下线Finished off the assembly line");//备注
		
		boolean iscsflag=Iscsflag(ghead.getCk());
		for(int i=0;i<vos.length;i++)
		{
			itemvos[i] = new GeneralBillItemVO();
			itemvos[i].setStatus(2);//新增;
			//表体 BONROADFLAG  BTOINZGFLAG BTOINZGFLAG BTOOUTTOIAFLAG
			itemvos[i].setBbarcodeclose(new UFBoolean(false)); //单据行是否条码关闭
			itemvos[i].setBreturnprofit(new UFBoolean(false));//是否返利
			itemvos[i].setBsafeprice(new UFBoolean(false));//是否价保 
			itemvos[i].setBsourcelargess(new UFBoolean(false));//上游是否赠品行
			itemvos[i].setBzgflag(new UFBoolean(false));//暂估标志 
//			itemvos[i].setCfirstbillbid(vos[i].getPk_glzb_b());//源头单据表体id
//			itemvos[i].setCfirstbillhid(vos[i].getPk_glzb());//源头单据表头id
//			itemvos[i].setCfirsttype("JYDJ");//源头单据类型
//			itemvos[i].setCfreezeid(vos[i].getPk_glzb_b());//锁定来源 
			itemvos[i].setCgeneralbid(null); //表体主键、新增赋值
			itemvos[i].setCgeneralhid(null); //表头主键、新增赋值为空
			
			String pk_invbasdoc = (String) dao.findColValue("bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"+ghead.getCp()+"'");
			itemvos[i].setCinvbasid(pk_invbasdoc);//存货基本id
			
			itemvos[i].setCinventoryid(ghead.getCp());//存货id
			itemvos[i].setCrowno(((i+1)*10)+"");//行号
//			itemvos[i].setCsourcebillbid(vos[i].getPk_glzb_b());//来源单据表体序列号
//			itemvos[i].setCsourcebillhid(vos[i].getPk_glzb());//来源单据表头序列号
//			itemvos[i].setCsourcetype("JYDJ");//来源单据类型
			itemvos[i].setCvendorid(ghead.getKs());//供应商id  ？跟客商有啥区别
			itemvos[i].setDbizdate(logindate);//业务日期
	//		itemvos[i].setDbizdate(null);//业务日期
			itemvos[i].setFchecked(0);//0  待检标记
			itemvos[i].setFlargess(new UFBoolean(false)); //N 是否赠品
			itemvos[i].setIsok(new UFBoolean(true));//Y
			itemvos[i].setNbarcodenum(null);//0  条码数量 
			itemvos[i].setNinnum(new UFDouble(vos[i].getXxaglsl().doubleValue()));//实入数量  
			itemvos[i].setNmny(null);//金额
			itemvos[i].setNprice(null);//单价
			itemvos[i].setNshouldinnum(new UFDouble(vos[i].getXxaglsl().doubleValue()));//应入数量
	//		itemvos[i].setPk_corp();
	//		itemvos[i].setPK_INVOICECORP();
	//		itemvos[i].setPK_REQCORP();
			itemvos[i].setVbatchcode(vos[i].getPh());//批次号   入库单界面可参照
			itemvos[i].setVfirstbillcode(null);// 源头单据号
			
			String scddh = (String)dao.findColValue("mm_mo", "scddh", "pk_moid='"+ghead.getScddh()+"'");
			itemvos[i].setVproductbatch(scddh);// 生产订单号
			
			itemvos[i].setCsourcetype("JYDJ"); //来源单据类型：JYDJ	
		//	itemvos[i].setVsourcerowno(vos[i].get);
			itemvos[i].setVsourcebillcode(vos[i].getPrimaryKey());//来源单据号
		//	itemvos[i].setCsourcebillhid(ghead.getPrimaryKey());//来源表体ID
		//	itemvos[i].setCsourcebillbid(vos[i].getPrimaryKey());
//			itemvos[i].setVsourcerowno(i+"");// 来源单据行号
			itemvos[i].setVfree1(vos[i].getDh());//垛号
		//	HYPubBO pubDao = new HYPubBO();
			String kh=(String)dao.findColValue("bd_cumandoc","pk_cubasdoc","pk_cumandoc='"+ghead.getKs()+"'");
			itemvos[i].setPk_cubasdoc(ghead.getKs());//客商基本档案  跟供应商有啥区别
			String csPaceid = (String)dao.findColValue("bd_cargdoc", "pk_cargdoc", "cscode='"+vos[i].getHw()+"' and pk_stordoc='"+ghead.getCk()+"'"); //货位
			
			if(iscsflag)
			{
				if(StringIsNullOrEmpty(csPaceid))
			  {
				 throw new Exception("货位不能为空！Cargo space can not be empty!");
			  }
			  else
			  {
				itemvos[i].setCspaceid(csPaceid); //货位
			    itemvos[i].setCspace2id(csPaceid);//货位 
			    String csPaceName = (String)dao.findColValue("bd_cargdoc", "csname", "cscode='"+vos[i].getHw()+"'and pk_stordoc='"+ghead.getCk()+"'"); //货位
			    itemvos[i].setVspacename(csPaceName);
			    itemvos[i].setVspace2name(csPaceName);
			    itemvos[i].setVspacecode(vos[i].getHw());
			    itemvos[i].setVspace2code(vos[i].getHw());
			 
			//设置货位信息
			    LocatorVO[] locVO = new LocatorVO[1];
			    locVO[0] = new LocatorVO();
			    itemvos[i].setLocator(locVO);
			    itemvos[i].setLocStatus(2); 
			    locVO[0].setCspaceid(csPaceid);
			    locVO[0].setCwarehouseid(ghead.getCk()); 
			    locVO[0].setVspacename(csPaceName);
			    locVO[0].setNinspacenum(new UFDouble(vos[i].getXxaglsl().doubleValue()));
			  }
			}
		} 
		return bo;
	}
	/**
	  * @功能:产成品入库删除时回写下线检验单
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    public void Writeback(GeneralBillVO vobill) throws Exception
    {
    	
    	if(vobill==null)
    	{
    	  return;
    	}
        ArrayList delbillkey=new ArrayList();
        ArrayList delbilldh=new ArrayList();
    	GeneralBillHeaderVO Header =vobill.getHeaderVO();
    	GeneralBillItemVO [] ItemVOs=(GeneralBillItemVO[])vobill.getChildrenVO();
    	for(GeneralBillItemVO itemvo:ItemVOs)
    	{
    		if(itemvo.getCsourcetype()==null)
    		{
    			continue;
    		}
    		else if(itemvo.getCsourcetype().equalsIgnoreCase("JYDJ"))
    	   {
    		 
    		    if(Header.getStatus()!=VOStatus.DELETED)
    		    {
    			 throw new BusinessException("单据中存在由下线检验单生成的，需要整单删除！Documents exist to generate by downline test single, entire single deleted!");
    		    }
    		    else 
    		    {
    			  if(!StringIsNullOrEmpty(itemvo.getCsourcebillbid()))
    			  {
    				  if(delbillkey.indexOf(itemvo.getCsourcebillbid())<=0)
    				  {
    					  delbillkey.add(itemvo.getCsourcebillhid().trim());
    				  }
    			  }
    			  else 
    			  {
    				  if(delbilldh.indexOf(itemvo.getVfree1())<=0)
    				  {
    					  delbilldh.add(itemvo.getVfree1());
    				  }
    			  }
    		    }
    		 
    	  }
    	}
    	//GeneralBillHeaderVO headvo= vobill.getHeaderVO();
    	try {
			UpdateStatus(delbillkey,delbilldh);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
    	
    }  
    /**
	  * @功能:判断字符串是否为空
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    public boolean StringIsNullOrEmpty(Object obj) 
    {
    	return obj==null?true:String.valueOf(obj).equals("")?true:
    		  String.valueOf(obj).equalsIgnoreCase("null")?true:String.valueOf(obj).equalsIgnoreCase("_________N/A________")?true:false;
    }
    /**
	  * @功能:回写下线检验单的单据状态
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    public void UpdateStatus(ArrayList itemkey,ArrayList dh) throws Exception
    {
    	Hashtable moid=new Hashtable();
    	StringBuffer sql=new StringBuffer();
    	if((dh==null&&itemkey==null)||(dh.size()<=0&&itemkey.size()<=0))
    	{
    		return;
    	}
    	PersistenceManager persistence=null;
    	try
    	{
   		    persistence= PersistenceManager.getInstance();
  	        JdbcSession  session = persistence.getJdbcSession();
    		if(dh.size()>0)
    	    {
    		  if(itemkey==null)
    		  {
    			 itemkey=new ArrayList();
    		  }
    		  String [] nitemkey=getBIDByDh(dh);
    		  for(int i=0;i<nitemkey.length;i++)
    
    		  {
    			  String curkey=nitemkey[i];
   
    			  if(itemkey.indexOf(curkey)<=0)
    			  {
    				  itemkey.add(curkey);
    			  }
    		  }
    	    }
             String[] headkey=getHIDByBID(itemkey);
    		 sql.setLength(0);
    		 sql.append("update mm_glzb ");
    		 sql.append("set djzt=0");
    		 sql.append(" where pk_glzb in(").append(ArrayToString(headkey)).append(")");
    	     Logger.debug(">>>>>>>>跟新下线检验单的状态");
    		 session.executeUpdate(sql.toString());
    		  Logger.debug(">>>>>>>>"+sql.toString());
    		 moid=getMoInNumByBID(itemkey);
      		 Enumeration moidkey=  moid.keys();
    		  while(moidkey.hasMoreElements())
    		  {
    			 String curkey=String.valueOf(moidkey.nextElement());
    			 UFDouble curnum=(UFDouble)moid.get(curkey);
    			if(!checkMoSjwgslError(curkey,curnum));
    			{ 
    				sql.setLength(0);
    			    sql.append("update mm_mo ");
    			    sql.append("set rksl=nvl(rksl,0.0)-nvl("+curnum.toString()+",0.0),  ");
    			    sql.append("sjwgsl=nvl(sjwgsl,0.0)-nvl("+curnum.toString()+",0.0) ");
    			    sql.append(" where pk_moid='"+curkey+"'");
    			    session.executeUpdate(sql.toString());
    			}
    		  }
    		 
    	  
    	}  
        catch (DbException e)
         {
				// TODO Auto-generated catch block
        	throw e;
	     }
        catch(BusinessException e)
        {
        	throw e; 
        }
        catch(Exception e)    
   	    {
 	    	throw e;
   	    }
  	     finally
  	     {
  	    	  if(persistence!=null)
  	    	  {
  	    		persistence.release();
  	    	  }
  	    }
    }
    /**
	  * @功能:判断扣减完工数量是否出现异常
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    private boolean checkMoSjwgslError(String key,UFDouble num) throws Exception
    {
    	PersistenceManager persistence=null;
    	try
    	{
   		    persistence= PersistenceManager.getInstance();
  	        JdbcSession  session = persistence.getJdbcSession();
  	        String Sql="select nvl(rksl,0.0)-nvl("+num.toString()+",0.0) as rksl , nvl(sjwgsl,0.0)-nvl("+num.toString()+",0.0) as sjwgsl from mm_mo where pk_moid='"+key+"'";
  	        HashMap hm=(HashMap) session.executeQuery(Sql,new MapProcessor());
  	        if(hm.size()==0)
  	        {
  	        	throw new BusinessException(Transformations.getLstrFromMuiStr("生产单订单号@The production of a single order number&:"+key+"&不存在！does not exist!"));
  	        }
  	        else 
  	        {
  	         UFDouble rksl=StringIsNullOrEmpty(hm.get("rksl"))?new UFDouble("0.0"):new UFDouble(hm.get("rksl").toString());
  	         UFDouble sjwgsl=StringIsNullOrEmpty(hm.get("sjwgsl"))?new UFDouble("0.0"):new UFDouble(hm.get("sjwgsl").toString());
  	         if(rksl.compareTo(new UFDouble(0.0))<0)
  	         {
  	        	throw new BusinessException(Transformations.getLstrFromMuiStr("生产单订单号@Storage quantity of&:"+key+"&入库数量@The production of a single order number&"+rksl.toString()+"&！@!"));
  	         }
  	         else if(sjwgsl.compareTo(new UFDouble(0.0))<0)
  	         {
  	        	throw new BusinessException("生产单订单号@The production of a single order number&:"+key+"&实际完工入库数量为@ the number of actual completion warehousing&"+sjwgsl.toString()+"&！@!");
  	        	
  	         }
  	         else 
  	         {
  	        	 return false;
  	         }
  	        }
    	}
  	    catch(DbException e)    
  	    {
	    	throw e;
  	    }
  	  catch(Exception e)    
	    {
	    	throw e;
	    }
  	    finally
  	    {
  	    	if(persistence!=null)
  	    	{
  	    		persistence.release();
  	    	}
  	    }
  	    	
    }
    
    /**
	  * @功能:根据表体查表头ID
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    private String [] getHIDByBID(ArrayList bid)throws Exception
    {
    	PersistenceManager persistence=null;
    	ArrayList rstAl=new ArrayList();
    	try
    	{
    		Logger.debug(">>>>>>>>根据表体ID查询表头ID");
    		if(bid==null||bid.size()==0)
    		{
    			return null; 
    			//throw new BusinessException("传入的表体主键有误!");
    		}
    		persistence=PersistenceManager.getInstance();	
    		JdbcSession  session = persistence.getJdbcSession();
    		StringBuffer sql=new StringBuffer();
    		sql.append("select b.pk_glzb  ");
    		sql.append("from mm_glzb_b b ");
    		sql.append(" where nvl(b.dr,0)=0 ")	;	
    		sql.append("and b.pk_glzb_b in("+ArrayListToString(bid)+") ");
    		sql.append("group by b.pk_glzb ");
    		Logger.debug(sql.toString());
    		rstAl= (ArrayList)session.executeQuery(sql.toString(), new ListProcessor());
    		return (String[]) rstAl.toArray(new String[]{});
    	}
    	catch(DbException e)    
   	    {
 	    	throw e;
   	    }
    	catch(Exception e)    
   	    {
 	    	throw e;
   	    }
   	    finally
   	    {
   	    	if(persistence!=null)
   	    	{
   	    		persistence.release();
   	    	}
   	    }
    }
    /**
	  * @功能:根据垛号查表体ID
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    private String [] getBIDByDh(ArrayList dh)throws Exception
    {
    	PersistenceManager persistence=null;
    	ArrayList rstAl=new ArrayList();
    	try
    	{
    		Logger.debug(">>>>>>>>根据垛号查询表体ID");
    		if(dh==null||dh.size()==0)
    		{
    			throw new BusinessException("传入的垛号有误!Incoming stack number!");
    		}
    		persistence=PersistenceManager.getInstance();	
    		JdbcSession  session = persistence.getJdbcSession();
    		StringBuffer sql=new StringBuffer();
    		sql.append("select b.pk_glzb_b  ");
    		sql.append("from mm_glzb_b b, mm_glzb a");
    		sql.append(" where a.pk_glzb=b.pk_glzb and nvl(b.dr,0)=0 and billsign='JYDJ' ")	;	
    		sql.append("and b.dh in("+ArrayListToString(dh)+")");
    		Logger.debug(sql.toString());
    		rstAl= (ArrayList)session.executeQuery(sql.toString(), new ListProcessor());
    		
    		return (String [])rstAl.toArray(new String[]{});
    	}
    	catch(DbException e)    
   	    {
 	    	throw e;
   	    }
    	catch(Exception e)    
   	    {
 	    	throw e;
   	    }
   	    finally
   	    {
   	    	if(persistence!=null)
   	    	{
   	    		persistence.release();
   	    	}
   	    }
    }
    /**
	  * @功能:统计需要扣减的生产订单的入库数量
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    private Hashtable getMoInNumByBID(ArrayList bid)throws Exception
    {
    	Hashtable mohm=new Hashtable();
    	PersistenceManager persistence=null;
    	try
    	{
    		Logger.debug(">>>>>>>>根据表体ID查询订单号和计算入库数量");
    		if(bid==null||bid.size()==0)
 		   {
 			 	throw  new BusinessException("传入的生产单订单有误!Incoming orders for production of single!");
 		   }
    	   persistence=PersistenceManager.getInstance();
    	   JdbcSession  session = persistence.getJdbcSession();
  		   StringBuffer sql=new StringBuffer();
  		   sql.append(" select a.scddh as moid,sum(nvl(b.xxaglsl,0.0)) as glsl ");
  		   sql.append("from mm_glzb a , mm_glzb_b b  ");
  		   sql.append("where a.pk_glzb=b.pk_glzb  and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 ");
  		   sql.append(" and  b.pk_glzb_b in("+ArrayListToString(bid)+")");
  		   sql.append("group by a.scddh ");
  		   Logger.debug(sql.toString());
		   ArrayList al=(ArrayList) session.executeQuery(sql.toString(),new MapListProcessor());
		   Iterator ie= al.iterator();
		   while(ie.hasNext())
		   {
		   	  HashMap hm=(HashMap)ie.next();
			  String mokey=String.valueOf(hm.get("moid"));
			  if(mohm.containsKey(mokey))
			  {
				UFDouble glsl=(UFDouble)mohm.get(mokey);
				UFDouble newsl=StringIsNullOrEmpty(hm.get("glsl"))?new UFDouble("0.0"):new UFDouble(hm.get("glsl").toString());
				glsl=glsl.add(newsl);
				mohm.put(mokey, glsl);
			  }	  
			  else
			  {
				  UFDouble newsl=StringIsNullOrEmpty(hm.get("glsl"))?new UFDouble("0.0"):new UFDouble(hm.get("glsl").toString());
				  mohm.put(mokey, newsl);
			  }
		   }
		   
		   return mohm;
    	}
    	catch(DbException e)    
   	    {
 	    	throw e;
   	    }
    	catch(Exception e)    
   	    {
 	    	throw e;
   	    }
   	    finally
   	    {
   	    	if(persistence!=null)
   	    	{
   	    		persistence.release();
   	    	}
   	    }
    }
    /**
	  * @功能:ArrayList转化为字符串，最终的格式为: 'a','b','c'.
	  * @author ：林桂莹
	  * @2012/11/2
	  * 
	  * @since v50
	  */
    private String ArrayListToString(ArrayList al)
    {
    	if(al==null||al.size()==0)
    	{
    		return null;
    	}
    	Iterator ie=al.iterator();
    	StringBuffer rst=new StringBuffer();
    	int i=0;
    	while(ie.hasNext())
    	{
    		String curvalue=String.valueOf(ie.next());
    		if(i==0)
    		{
    			rst.append("'"+curvalue+"'");
    			i++;
    		}
    		else
    		{
    			rst.append(",'"+curvalue+"'");	
    		}
    	}
    	return rst.toString();
    }
   private String ArrayToString(String[] arrystr)
   {
	   StringBuffer rst=new StringBuffer();
	   for(int i=0;i<arrystr.length;i++)
	   {
		   if(i==0)
		   {
			   rst.append("'"+arrystr[i]+"'");
		   }
		   else
		   {
			   rst.append(",'"+arrystr[i]+"'");	
		   }
	   }
	   return rst.toString();
   }
   private boolean IsNotLockAccount(String dbDate,String calbody) throws Exception
	{ 
		boolean rst=false ;
	  	PersistenceManager persistence=null; 
		try
		{
			if(new UFDate(dbDate).compareTo(ClientEnvironment.getServerTime().getDate())>0)
			{
				return true;
			}
			StringBuffer  Sql=new StringBuffer();
		    Sql.append("SELECT acc.faccountflag  ");
		    Sql.append("FROM ic_accountctrl acc, bd_calbody cal  ");
		    Sql.append("where acc.pk_calbody = cal.pk_calbody  AND acc.dr = 0  AND acc.pk_calbody = '").append(calbody).append("'  ");
	        Sql.append("and '"+dbDate+"' between  to_char(to_date(acc.tstarttime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') and  to_char(to_date(acc.tendtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') ");
	        persistence=PersistenceManager.getInstance();
	        JdbcSession  session= persistence.getJdbcSession();
	        ArrayList al=(ArrayList)session.executeQuery(Sql.toString(),  new ListProcessor());
	        if(al==null||al.size()<=0)
	        {
	        	return true;
	        }
	        rst= new UFBoolean(String.valueOf(al.get(0))).booleanValue();
	        return rst;
		}
		catch(Exception e)
		{
          throw e;
		}
	     finally
	     {
	    	 if(persistence!=null)
	    	 {
	    		 persistence.release();
	    	 }
	     }
	}
	public boolean Iscsflag(String primkey) throws Exception
	{
		boolean rst=false;
		String SQL="select csflag from bd_stordoc  where pk_stordoc ='"+primkey+"'";
	  	PersistenceManager persistence=null; 
	  	HashMap list = null;
try {
 
		persistence=PersistenceManager.getInstance();
	
    JdbcSession  session= persistence.getJdbcSession();
    list=(HashMap)session.executeQuery(SQL,  new MapProcessor());

	if (list.isEmpty()) {
		return rst;
	}
	else 
	{
		
		return  rst= (new UFBoolean(String.valueOf(list.get("csflag"))).booleanValue());	
	}

	}catch(DbException e)    
	    {
	    	throw e;
	    }
	catch(Exception e)    
	    {
	    	throw e;
	    }
	 finally
     {
    	 if(persistence!=null)
    	 {
    		 persistence.release();
    	 }
     }
	}

	private boolean checkFreeItemAndBatchAndSpaceInput(GlItemBVO[] vo,String pk_corp,String cw) throws Exception
	{
		 String errMessage="";
	      ArrayList errInventory=new ArrayList();
		 for(int i=0;i<vo.length;i++)
		 {
			 if(errInventory.indexOf(vo[i].cinventoryid)>=0)
			 {
				 continue;
			 }
			 errInventory.add(vo[i].cinventoryid);
			HashMap invhm= getInvdocHashtable(vo[i].cinventoryid,pk_corp);
			if((invhm==null)||(invhm.size()<=0))
			{
				throw new ValidationException("查询出异常存货信息!Query exception inventory information!");
				//return false ;
			}
			String invcode=String.valueOf(invhm.get("invcode"));
			String key=vo[i].getPrimaryKey();
			try {
				if(Iscsflag(cw))
				{
					String storeid=vo[i].getCspaceid();
					if(StringIsNullOrEmpty(storeid))
					{
						errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&的货位不能为空!@of cargo space can not be empty!")+"\n";
					}
					else 
					{
						String Msg=null;
                        	try {
								 Msg=checkstorectl(vo[i].cinventoryid,cw,storeid);
							} catch (Exception ex) {
								// TODO Auto-generated catch block
								throw new ValidationException(ex.getMessage()); 
							}
                        if(Msg!=null)
                        {
                        	Msg=Msg.indexOf("ff")>0?Msg.substring(Msg.indexOf("ff")+2, Msg.length())+"@requirements fixed cargo space":Msg.substring(Msg.indexOf("fs")+2, Msg.length())+"@require separate storage!";
                        	errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&"+Msg)+"\n";
                        }
					}
				}
				else 
				{
					String storeid=vo[i].getCspaceid();
					if(!StringIsNullOrEmpty(storeid))
					{
						errMessage+="仓库未启用货位管理，不能录入货位！The warehouse cargo space management is not enabled, and can not be recorded in the goods-bit!";
					}
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				throw new ValidationException(e.getMessage()); 
			}
			String IsBathcMgrt =String.valueOf(invhm.get("isbathcmgrt"));
			IsBathcMgrt=(IsBathcMgrt!=null&&!IsBathcMgrt.equals("")&&
					     !IsBathcMgrt.equals("null")&&!IsBathcMgrt.equals("N"))?"Y":"N";
			
			
			if(new UFBoolean(IsBathcMgrt).booleanValue())
			{
				String vbatch=String.valueOf(vo[i].getPh());
				if(StringIsNullOrEmpty(vbatch))
				{
	
					errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&已启用批次号管理，批次号不能为空!@Enable batch number management, batch number can not be empty")+"\n";
				}
		        
			}
			else 
			{	String vbatch=String.valueOf(vo[i].getPh());
			    if(!StringIsNullOrEmpty(vbatch))
				{
			    	errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用批次号管理,不能录入批次号！@is not enabled, the batch number management, can not be recorded in the batch number!");
				}
			}
			  	 String vfreeid=String.valueOf(invhm.get("free1"));
	        	 String vfree=String.valueOf(vo[i].getDh());
	        	 if(StringIsNullOrEmpty(vfreeid))
	        	 { 
	        		 if(!StringIsNullOrEmpty(vfree))
	        		 {
	        			 errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用自由项，不能录入自由项！@is not enabled free term1 can not be recorded in the free term1!");
	        		 }
	        	 }
	        	 else if(StringIsNullOrEmpty(vfree))
	        	 {
	   
	        		 errMessage+=Transformations.getLstrFromMuiStr("存货@The inventory&"+invcode+"&的自由项1不能为空@free term1 can not be empty!");
	        	 }
	         
			 if(!errMessage.equals("")&&!errMessage.equalsIgnoreCase("null"))
				  throw new ValidationException(errMessage);
		 }
		return true;
		
	}
	  public String checkstorectl(String inv,String wh,String space) throws Exception
	   {
			StringBuffer  Sql=new StringBuffer();
		    Sql.append("SELECT  fseparatespace,ffixedspace,cspaceid ");
		    Sql.append("FROM ic_storectl acc, ic_defaultspace cal  ");
		    Sql.append("where acc.cstorectlid=cal.cstorectlid  and cwarehouseid='"+wh+"' and cinventoryid='"+inv+"'");
		    HashMap invhm = null;
		    PersistenceManager persistence=null; 
	        try {
	        	 persistence=PersistenceManager.getInstance();	
	        	  JdbcSession  session= persistence.getJdbcSession();
	        	   	invhm = (HashMap)session.executeQuery(Sql.toString(),  new MapProcessor());
		        }
	        catch(DbException e)    
		    {
		    	throw e;
		    }
		catch(Exception e)    
		    {
		    	throw e;
		    }
		 finally
	     {
	    	 if(persistence!=null)
	    	 {
	    		 persistence.release();
	    	 }
	     }
		    if(invhm==null||invhm.size()<=0) 
		    {
		    	return null;
		    }
		    else 
		    {
		    	UFBoolean fseparatespace=new UFBoolean(StringIsNullOrEmpty(invhm.get("fseparatespace"))?"N":String.valueOf(invhm.get("fseparatespace")));//是否单独存放
		    	UFBoolean ffixedspace=new UFBoolean(StringIsNullOrEmpty(invhm.get("ffixedspace"))?"N":String.valueOf(invhm.get("ffixedspace")));//是否固定货位
		    	String cspaceid=String.valueOf(invhm.get("cspaceid"));
		    	if(ffixedspace.booleanValue()&&!space.equalsIgnoreCase(cspaceid))
		    	{
		    		return "ff要求存放固定货位！";
		    	}
		    	else if (fseparatespace.booleanValue())
		    	{
		    		Sql.setLength(0);
		    		Sql.append("select nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0) as onhandnum  ");
		    		Sql.append("from v_ic_onhandnum6 hand  where hand.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and  hand.cspaceid='"+space+"'  and nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0)>0");
		    		  //PersistenceManager persistence=null; 
		    		   try {
		    				persistence=PersistenceManager.getInstance();		    				
		    			    JdbcSession  session= persistence.getJdbcSession();
		    	        	 HashMap handnumhm =(HashMap)session.executeQuery(Sql.toString(),  new MapProcessor());
		    	        	 if(handnumhm==null)
		    	        	 {
		    	        		 return null;
		    	        	 }
		    	        	 else if(handnumhm.size()>0)
		    	        	 {
		    	        		 return "fs要求单独存放！";
		    	        	 }
		    		        }
		    		   catch(DbException e)    
		   		    {
		   		    	throw e;
		   		    }
		   		catch(Exception e)    
		   		    {
		   		    	throw e;
		   		    }
		   		 finally
		   	     {
		   	    	 if(persistence!=null)
		   	    	 {
		   	    		 persistence.release();
		   	    	 }
		   	     }
		    	}
		    }
		    return null;
	   }
	  private HashMap getInvdocHashtable(String key,String pk_corp) throws  Exception{
			// TODO Auto-generated method stub
			String sql="select a.invcode, a.free1,a.free2,a.free3,a.free4,a.free5,wholemanaflag as IsBathcMgrt,chkfreeflag,stockbycheck ";
			sql+=" from bd_invbasdoc a ,bd_invmandoc b,bd_produce c  " ;
			sql+="where a.pk_invbasdoc=b.pk_invbasdoc  and c.pk_invmandoc=b.pk_invmandoc and b.dr=0 and b.pk_corp='"+pk_corp+"' and b.pk_invmandoc ='"+key+"'";
			PersistenceManager persistence=null; 
	        HashMap inv = null;
	        try
	        {
	        	persistence=PersistenceManager.getInstance();		    				
			    JdbcSession  session= persistence.getJdbcSession();
	        	inv = (HashMap)session.executeQuery(sql.toString(),  new MapProcessor());
		      }
	        catch(DbException e)    
   		    {
   		    	throw e;
   		    }
   		    catch(Exception e)    
   		    {
   		    	throw e;
   		    }
   		    finally
   	     {
   	    	 if(persistence!=null)
   	    	 {
   	    		 persistence.release();
   	    	 }
   	     }
			return inv;
		}
//	private  GeneralBillVO getRkVO(GlBillVO billvo)
//	{
//		GeneralBillVO bo = new GeneralBillVO();
//		nc.vo.ic.pub.bill.GeneralBillItemVO itemvo  =null;
//		
//		// CLASTMODIID
//		GeneralBillHeaderVO head = null;
//		head.setCbilltypecode(null); //库存单据类型编码
//		head.setCbizid(null); 	//业务员ID
//		head.setCdispatcherid(null); //收发类型ID
//		head.setCdptid(null); //部门ID 
//		head.setCgeneralhid(null); //主键
//		head.setCoperatorid(null);//制单人
//		head.setCwarehouseid(null);//仓库ID
//		head.setCwhsmanagerid(null);//库管员
//		head.setDbilldate(null);//单据日期
//		head.setFbillflag(null);//单据状态
//		head.setPk_calbody(null);//库存组织PK
//		head.setPk_corp(null);//公司ID
//		head.setVbillcode(null); //单据号
//		head.setVnote(null);//备注
//		
//		
//		//表体 BONROADFLAG  BTOINZGFLAG BTOINZGFLAG BTOOUTTOIAFLAG
//		itemvo.setBbarcodeclose(null); //单据行是否条码关闭
//		itemvo.setBreturnprofit(null);//是否返利
//		itemvo.setBsafeprice(null);//是否价保 
//		itemvo.setBsourcelargess(null);//上游是否赠品行
//		itemvo.setBzgflag(null);//暂估标志 
//		itemvo.setCfirstbillbid(null);//源头单据表体id
//		itemvo.setCfirstbillhid(null);//源头单据表头id
//		itemvo.setCfirsttype(null);//源头单据类型
//		itemvo.setCfreezeid(null);//锁定来源 
//		itemvo.setCgeneralbid(null); //表体主键
//		itemvo.setCgeneralhid(null); //表头主键
//		itemvo.setCinvbasid(null);//存货基本id
//		itemvo.setCinventoryid(null);//存货id
//		itemvo.setCrowno(null);//行号
//		itemvo.setCsourcebillbid(null);//来源单据表体序列号
//		itemvo.setCsourcebillhid(null);//来源单据表头序列号
//		itemvo.setCsourcetype(null);//来源单据类型
//		itemvo.setCvendorid(null);//供应商id
//		itemvo.setDbizdate(null);//业务日期
////		itemvo.setDbizdate(null);//业务日期
//		itemvo.setFchecked(null);//0
//		itemvo.setFlargess(null); //N 是否赠品
//		itemvo.setIsok(null);//Y
//		itemvo.setNbarcodenum(null);//0  条码数量 
//		itemvo.setNinnum(null);//实入数量  
//		itemvo.setNmny(null);//金额
//		itemvo.setNprice(null);//单价
//		itemvo.setNshouldinnum(null);//应入数量
////		itemvo.setPk_corp();
////		itemvo.setPK_INVOICECORP();
////		itemvo.setPK_REQCORP();
//		itemvo.setVbatchcode(null);//批次号
//		itemvo.setVfirstbillcode(null);// 源头单据号
//		itemvo.setVproductbatch(null);// 生产批号？
//		itemvo.setVsourcebillcode(null);//来源单据号
//		itemvo.setVsourcerowno(null);// 来源单据行号
//		itemvo.setVuserdef1(null);//
//		itemvo.setPk_cubasdoc(null);//客商基本档案
//		
//		
//		
//		return null;
//	}
	
}
