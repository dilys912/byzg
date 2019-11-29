package nc.impl.mm.mm6601;
  
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nc.bs.bd.languagetransformations.Transformations;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.trade.business.HYPubBO;
import nc.itf.mm.scm.mm6601.IMo6601;
import nc.itf.scm.service.IScmEJBService;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.common.ListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.mo.mo6601.GlclBillVO;
import nc.vo.mo.mo6601.GlclHeadVO;
import nc.vo.mo.mo6601.GlclItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.service.ServcallVO;

public class IMo6601Impl implements IMo6601
{
	public void doXiaXianGl(GlclBillVO billvo,int[] rows) throws Exception
	{
		// TODO Auto-generated method stub
		PersistenceManager persistence=null;
		try
		{
			GlclHeadVO head =billvo.getParentVO(); 
			persistence=PersistenceManager.getInstance();//.getJdbcSession(); 
		   JdbcSession session = persistence.getJdbcSession();
		   GlclItemVO[] items = billvo.getChildrenVO();
		  String sql = null;
		  for(int row:rows)
		  { 
		 	sql = "update mm_glcl_b set clzt=1 where pk_glcl='"+items[row].getPk_glcl_b()+"'";
			session.executeUpdate(sql); //更新隔离处理单表体数据为已处理
			
			//更新下线检验单据表体状态为已处理 
			sql = "update mm_glzb_b set clzt=1 where pk_glzb_b ='"+items[row].getPk_glzb_b()+"'";
			session.executeUpdate(sql);  
	    	}
		 boolean flag = true;	
		 for(int i=0;i<items.length;i++)
		  {
			boolean bFlag = false;
			for(int row:rows)
			{
				if(row==i)
				{
					bFlag = true;
					break;
				}
			}
			if(bFlag)
			{
				continue;
			}
			if(items[i].getClzt()==null||items[i].getClzt()==0)
			{
				flag = false;
				break;
			} 
		 }
		
		if(flag)
		{
		 	sql = "update mm_glcl set djzt=1 where pk_glcl='"+head.getPk_glcl()+"'";
			session.executeUpdate(sql); //更新隔离处理单据状态为已处理   如果已经全部处理
		}
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

//	@SuppressWarnings("restriction")
	public void doFanGong(GlclBillVO billvo,int[] rows) throws Exception
	{
		// TODO Auto-generated method stub
		GlclHeadVO head =billvo.getParentVO();
		JdbcSession session = PersistenceManager.getInstance().getJdbcSession();
		
		GlclItemVO[] items = billvo.getChildrenVO();
		ArrayList errInventory=new ArrayList();
		for(int row:rows)
		{
			boolean flag = IsNeedUpdateBillStatus(head.getPrimaryKey(),rows.length);
			/*for(int i=0;i<items.length;i++)
			{
				if(i==row)
				{
					continue;
				}
				if(items[i].getClzt()==null||items[i].getClzt()==0)
				{
					flag = false;
					break;
				} 
			}*/
			String sql;
			GlclItemVO item = billvo.getChildrenVO()[row];
			HYPubBO pubDao = new HYPubBO();
			String cinventoryid=head.getCinventoryid();
			if(StringIsNullOrEmpty(cinventoryid))
			{
				cinventoryid=(String) pubDao.findColValue("bd_invmandoc a,bd_invbasdoc b","a.pk_invmandoc","a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='"+head.getPk_corp()+"' and and a.dr=0 and b.dr=0 and b.invcode='"+head.getLh()+"'");
			}
			  
				 if(errInventory.indexOf(cinventoryid)>=0)
		 {
			 continue;
		 }
		 errInventory.add(cinventoryid);
		  boolean isCheckHcl=item.getHclhgsl()==null||item.getHclhgsl()<=0?false:true;
			if(!checkFreeItemAndBatchAndSpaceInput(item,head.getPk_corp(),cinventoryid,isCheckHcl))
			{
				continue;
			}
	
			//生成产成品入库单
			try
			{
				 
				String calbody=(String) pubDao.findColValue("BD_CALBODY", "PK_CALBODY", "bodycode='01' AND pk_corp='"+billvo.getParentVO().getPk_corp()+"'");
				if(!IsNotLockAccount(billvo.getParentVO().getLogindate().toString(),calbody))
				{
					throw new Exception("当期期间已关帐不能在入库！The current period is closed during the account can not be in storage!");
				}
				 GeneralBillVO geneVO =new GeneralBillVO();
				  if(item.getHclhgsl()!=null&&item.getHclhgsl()>0)
				  {								  
				      geneVO = this.getRkHzVO(billvo, row, 1,false);
					 new PfUtilBO().processAction("SAVE", "46", geneVO.getHeaderVO().getDbilldate().toString(), null, geneVO, null);//("SAVE","4C", voHead.getDbilldate().toString(), voTempBill);
					  
				  }

		    	if(item.getLydjlx()!=null&&item.getLydjlx().equals("isolation"))
			   {
				 geneVO = this.getRkHzVO(billvo,row, 0,false);
				 GeneralBillHeaderVO geneHead = geneVO.getHeaderVO();
				 //HYPubBO pubDao = new HYPubBO();
				
				//根据待处理数量  生成红字入库单 
				 GeneralBillItemVO geneItem = (GeneralBillItemVO) geneVO.getChildrenVO()[0];
				
				//解冻处理
				 FreezeVO free = new FreezeVO(); 
				free.setNdefrznum(new UFDouble(item.getDclsl()));// 冻解数量 
				free.setPk_corp(head.getPk_corp());  //对应公司
				free.setCwarehouseid(geneHead.getCwarehouseid()); //仓库ID
				free.setCspaceid(geneItem.getCspaceid());//货位		
				free.setCthawpersonid(InvocationInfoProxy.getInstance().getUserCode()); //解冻人ID
				free.setDthawdate(new UFDate(System.currentTimeMillis())); //解冻时间
				free.setCinventoryid(geneItem.getCinventoryid());// 存货ID
				
				String cp = (String)pubDao.findColValue("mm_glzb_b", "cp", " pk_glzb_b='"+item.getPk_glzb_b()+"'");
				free.setInvname(cp); //产品名称  
				String pkFreeze = (String)pubDao.findColValue("mm_glzb_b", "pk_freeze", " pk_glzb_b='"+item.getPk_glzb_b()+"'");
				free.setPrimaryKey(pkFreeze); //冻结VO主键
				String ts = (String)pubDao.findColValue("IC_FREEZE", "ts", " CFREEZEID='"+free.getPrimaryKey()+"'");
				free.setTs(new UFDateTime(ts));
							
				IScmEJBService bo = (IScmEJBService)NCLocator.getInstance().lookup(nc.itf.scm.service.IScmEJBService.class.getName());
		        ServcallVO scd[] = new ServcallVO[1]; 
		        scd[0] = new ServcallVO();
		        scd[0].setBeanName("nc.bs.ic.pub.freeze.FreezeBO");
		        scd[0].setMethodName("unfreezeInv");
		        scd[0].setParameterTypes(new Class[]{new FreezeVO[0].getClass()});
		        scd[0].setParameter(new Object[]{new FreezeVO[]{free}});
		        bo.callEJBService("ic", scd);
	//			nc.bs.ic.pub.freeze.FreezeBO bo = new nc.bs.ic.pub.freeze.FreezeBO();
	//			bo.unfreezeInv(new FreezeVO[]{free}); 
		        
                //根据待处理数量  生成红字入库单 
				geneItem.setNinnum(new UFDouble(item.getDclsl()-2*item.getDclsl())); //负数 红字产成品入库单
				geneHead.setCdispatcherid(null);
				if(!StringIsNullOrEmpty(item.getYsph()))
		    	{
					geneItem.setVbatchcode(item.getYsph());
		    	}
				String csPaceid =geneItem.getCspaceid(); //(String)pubDao.findColValue("bd_cargdoc", "pk_cargdoc", "cscode='"+item.getHw()+"'"); //原货位 
				if(!StringIsNullOrEmpty(csPaceid))
				{
					//geneItem.setCspaceid(csPaceid);//原货位
			       // geneItem.getLocator()[0].setCspaceid(csPaceid);
				 geneItem.getLocator()[0].setNinspacenum(new UFDouble(item.getDclsl()-2*item.getDclsl()));
				}
				new PfUtilBO().processAction("SAVE", "46", geneVO.getHeaderVO().getDbilldate().toString(), null, geneVO, null);//("SAVE","4C", voHead.getDbilldate().toString(), voTempBill);
               
				//如果存在后处理报废数量  生成其他入库单和其他出单
				if(item.getHclbfsl()!=null&&item.getHclbfsl()!=0)
				{
					
					//根据后处理报废数量生成产成品入库单
					geneVO = this.getRkHzVO(billvo,row, 0,false);
					geneItem = (GeneralBillItemVO) geneVO.getChildrenVO()[0];
					
					String cdispatcherid = (String) pubDao.findColValue("bd_rdcl", "pk_frdcl", "(rdname='其他入库' OR rdname='其它入库') AND pk_corp='"+geneHead.getPk_corp()+"'");
				    //String csPaceName = (String)pubDao.findColValue("bd_cargdoc", "csname", "cscode='"+item.getHw()+"'"); //货位名称
					geneHead.setCdispatcherid(cdispatcherid);//收发类型ID ? “其他入库” 在产成品入库界面可参照
					geneItem.setNinnum(new UFDouble(item.getHclbfsl()));//实际入库数量 
					if(!StringIsNullOrEmpty(csPaceid))
					{
					  geneItem.getLocator()[0].setNinspacenum(new UFDouble(item.getHclbfsl()));
					 }
					new PfUtilBO().processAction("SAVE", "46", geneVO.getHeaderVO().getDbilldate().toString(), null, geneVO, null);//("SAVE","4C", voHead.getDbilldate().toString(), voTempBill);
					
					//根据 后处理报废数量  生成其他出库单
					geneVO = this.getRkHzVO(billvo,row, 0,true);
					geneHead = geneVO.getHeaderVO();
					geneHead.setCbilltypecode("4I"); 
					csPaceid=geneItem.getCspaceid();
					geneItem = (GeneralBillItemVO) geneVO.getChildrenVO()[0];
					
					//geneItem.setCspace2id(csPaceid);
				
					geneItem.setNoutnum(new UFDouble(item.getHclbfsl()));//实际入库数量 
					cdispatcherid = (String) pubDao.findColValue("bd_rdcl", "pk_frdcl", "(rdname='其他出库' OR rdname='其它出库') AND pk_corp='"+geneHead.getPk_corp()+"'");
					geneHead.setCdispatcherid(cdispatcherid); //收发类型ID ? “其他出库” 在产成品入库界面可参照   
					//csPaceid
					csPaceid=geneItem.getCspaceid();
				    if(!StringIsNullOrEmpty(csPaceid))
					{
					  geneItem.getLocator()[0].setNoutspacenum(new UFDouble(item.getHclbfsl()));
					 }
					new PfUtilBO().processAction("SAVE", "4I", geneVO.getHeaderVO().getDbilldate().toString(), null, geneVO, null);//("SAVE","4C", voHead.getDbilldate().toString(), voTempBill);
				}
			}
				
			    if(flag)
				{
					sql = "update mm_glcl set djzt=1 where pk_glcl='"+head.getPk_glcl()+"'";
					session.executeUpdate(sql); //更新隔离处理单单据状态为已处理  如果已经全部处理完
				} 
				sql = "update mm_glcl_b set clzt=2 where pk_glcl_b='"+item.getPk_glcl_b()+"'";
				session.executeUpdate(sql); //更新隔离处理单表体数据为已更新
			
			//更新库内隔离单状态为已处理
			sql = "update mm_glzb_b set clzt=1 where pk_glzb_b='"+item.getPk_glzb_b()+"'";
			session.executeUpdate(sql); //更新表体数据为已更新	
	
		} 
		
			 catch(Exception e)
			 {
				throw e;
			 }
		}
		
		
		 
	} 
	 //生成入库红字VO
	protected  GeneralBillVO getRkHzVO(GlclBillVO billVO,int curRow, int dhType,boolean outstore) throws Exception
	{ 
		GlclItemVO[] vos = (GlclItemVO[]) billVO.getChildrenVO();
		GlclHeadVO ghead = (GlclHeadVO) billVO.getParentVO();
		
		HYPubBO pubDao = new HYPubBO(); 
		
		GlHeadVO knHead  =  (GlHeadVO) pubDao.queryByPrimaryKey(GlHeadVO.class,vos[curRow].getPk_glzb());
		GlItemBVO item = (GlItemBVO) pubDao.queryByPrimaryKey(GlItemBVO.class,vos[curRow].getPk_glzb_b());
		if(vos[curRow].getLydjlx()!=null&&vos[curRow].getLydjlx().equals("isolation"))
		{
			knHead.setCk(item.getIsolationckid());
			knHead.setCp(item.getIsolationcpid());
		} 
 
		GeneralBillVO bo = new GeneralBillVO();
		GeneralBillItemVO[] itemvos  = new GeneralBillItemVO[1];
		bo.setLockOperatorid(getCurUser());
		
		// CLASTMODIID
		GeneralBillHeaderVO head = bo.getHeaderVO(); 
		head.setStatus(2);// 新增
		bo.setParentVO(head);
		bo.setChildrenVO(itemvos);
		
		head.setCbilltypecode("46"); //库存单据类型编码  46-产成品入库单
		head.setCbizid(ghead.getClr()); 	//业务员ID
		
		String cdispatcherid = (String) pubDao.findColValue("bd_rdcl", "pk_frdcl", "rdname='产成品入库' AND pk_corp='"+ghead.getPk_corp()+"'");
		head.setCdispatcherid(cdispatcherid); //收发类型ID ? “产成品入库” 在产成品入库界面可参照   
		
//		head.setCdptid(ghead.getBz()); //部门ID  ？
//		head.setCgeneralhid(ghead.getPk_glzb()); //主键
		head.setCoperatorid(getCurUser());//制单人
		head.setCwarehouseid(knHead.getCk());//仓库ID
//		head.setCwhsmanagerid(null);//库管员 
		head.setDbilldate(ClientEnvironment.getServerTime().getDate());//单据日期
		head.setFbillflag(null);//单据状态
		//
		String pk_calbody = (String) pubDao.findColValue("BD_CALBODY", "PK_CALBODY", "bodycode='01' AND pk_corp='"+ghead.getPk_corp()+"'");
		head.setPk_calbody(pk_calbody);//库存组织PK
		
		head.setPk_corp(ghead.getPk_corp());//公司ID
		head.setVbillcode(null); //单据号
		head.setVnote("隔离处理单生成Isolated handle single generation");//备注
		
		bo.getParentVO().setAttributeValue("pk_corp", ghead.getPk_corp());
		
		
//		for(int i=0;i<vos.length;i++)
//		{
			itemvos[0] = new GeneralBillItemVO();
			itemvos[0].setStatus(VOStatus.NEW);//新增;
			//表体 BONROADFLAG  BTOINZGFLAG BTOINZGFLAG BTOOUTTOIAFLAG
			itemvos[0].setBbarcodeclose(new UFBoolean(false)); //单据行是否条码关闭
			itemvos[0].setBreturnprofit(new UFBoolean(false));//是否返利
			itemvos[0].setBsafeprice(new UFBoolean(false));//是否价保 
			itemvos[0].setBsourcelargess(new UFBoolean(false));//上游是否赠品行
			itemvos[0].setBzgflag(new UFBoolean(false));//暂估标志 
			itemvos[0].setAttributeValue("bonroadflag", new UFBoolean(false));
//			itemvos[0].setCfirstbillbid(vos[curRow].getPk_glzb_b());//源头单据表体id
//			itemvos[0].setCfirstbillhid(vos[curRow].getPk_glzb());//源头单据表头id
//			itemvos[0].setCfirsttype("JYDJ");//源头单据类型
//			itemvos[0].setCfreezeid(vos[curRow].getPk_glzb_b());//锁定来源 
			itemvos[0].setCgeneralbid(null); //表体主键、新增赋值
			itemvos[0].setCgeneralhid(null); //表头主键、新增赋值为空
			
			String pk_invbasdoc = (String) pubDao.findColValue("bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"+knHead.getCp()+"'");
			itemvos[0].setCinvbasid(pk_invbasdoc);//存货基本id
			
			String chbm = (String) pubDao.findColValue("bd_invbasdoc", "invcode", " pk_invbasdoc='"+itemvos[0].getCinvbasid()+"'");
			itemvos[0].setCinventorycode(chbm);//存货编码
			
			String chmc = (String) pubDao.findColValue("bd_invbasdoc", "invname", " pk_invbasdoc='"+itemvos[0].getCinvbasid()+"'");
			itemvos[0].setInvname(chmc);//存货名称			
			itemvos[0].setCinventoryid(knHead.getCp());//存货id
			itemvos[0].setCrowno(((curRow+1)*10)+"");//行号
			itemvos[0].setVsourcebillcode(vos[curRow].getPk_glzb_b());//来源单据表体序列号
		  //  itemvos[0].setCsourcebillhid(vos[i].getPk_glzb());//来源单据表头序列号
//			itemvos[i].setCsourcetype("JYDJ");//来源单据类型
			itemvos[0].setCvendorid(knHead.getKs());//供应商id  ？跟客商有啥区别
			itemvos[0].setDbizdate(ghead.getLogindate());//入库日期
	//		itemvos[0].setDbizdate(null);//业务日期
			itemvos[0].setFchecked(0);//0  待检标记
			itemvos[0].setFlargess(new UFBoolean(false)); //N 是否赠品
			itemvos[0].setIsok(new UFBoolean(true));//Y
			itemvos[0].setNbarcodenum(null);//0  条码数量 
			Integer Hclhgsl=vos[curRow].getHclhgsl()==null?0:vos[curRow].getHclhgsl();
			if(!outstore)
			{
				itemvos[0].setNinnum(new UFDouble(Hclhgsl));//实入数量   
			}
			else 
			{
				itemvos[0].setNinnum(null);
			}
//			itemvos[0].setNinnum(new UFDouble(vos[curRow].getDclsl()-2*vos[curRow].getDclsl()));//实入数量
			
			itemvos[0].setNmny(null);//金额
			itemvos[0].setNprice(null);//单价
			itemvos[0].setNshouldinnum(null);//应入数量
			itemvos[0].setNshouldoutnum(null);
			
			itemvos[0].setVfirstbillcode(null);// 源头单据号
			
//			itemvos[0].setVproductbatch(vos[curRow].getHclph());// 生产批号？
			itemvos[0].setVsourcebillcode(vos[curRow].getPrimaryKey());//来源单据号
//			itemvos[0].setVsourcerowno(i+"");// 来源单据行号
//			itemvos[0].setVuserdef1(vos[curRow].getCldh());//垛号
			String kh=(String)pubDao.findColValue("bd_cumandoc","pk_cubasdoc","pk_cumandoc='"+knHead.getKs()+"'");
			itemvos[0].setPk_cubasdoc(kh);//客商基本档案  跟供应商有啥区别

			String csPaceid =null;
			String csPaceName=null;
			if(dhType==0)//原垛号
			{
				itemvos[0].setVfree1(vos[curRow].getCldh());//垛号
				itemvos[0].setVbatchcode(vos[curRow].getClph());//批次号   入库单界面可参照
			    csPaceid = (String)pubDao.findColValue("bd_cargdoc", "pk_cargdoc", "pk_cargdoc='"+vos[curRow].getCspaceid()+"'"); //处理货位
			    if(StringIsNullOrEmpty(csPaceid))
			    {
			    	 csPaceid = (String)pubDao.findColValue("bd_cargdoc", "pk_cargdoc", "cscode='"+vos[curRow].getHw()+"' and pk_stordoc='"+knHead.getCk()+"'"); //处理货位
			    }
			    if(StringIsNullOrEmpty(csPaceid))
			    {
			    	csPaceName = (String)pubDao.findColValue("bd_cargdoc", "csname", "pk_cargdoc='"+csPaceid+"'"); //货位名称
			    }
			}else{ //后处理垛号
				itemvos[0].setVfree1(vos[curRow].getHcldh());//后处理垛号
				itemvos[0].setVbatchcode(vos[curRow].getHclph());//批次号   入库单界面可参照
			    csPaceid = vos[curRow].getHclhw();
			    csPaceName = (String)pubDao.findColValue("bd_cargdoc", "csname", "pk_cargdoc='"+vos[curRow].getHclhw()+"'"); //货位名称
			}
			
			try {
				if(Iscsflag(knHead.getCk()))
				{
					if(StringIsNullOrEmpty(csPaceid))
				  {
					 throw new Exception("货位不能为空！Cargo space can not be empty!");
				  }
				 
					else
				//设置货位信息
				  {
//				itemvos[0].setCspaceid(csPaceid);
//				itemvos[0].setCspace2id(csPaceid);//货位 
					LocatorVO[] locVO = new LocatorVO[1];
					locVO[0] = new LocatorVO();
					itemvos[0].setLocator(locVO);
					itemvos[0].setLocStatus(VOStatus.NEW); 
					itemvos[0].setCspaceid(csPaceid);
					itemvos[0].setVspacename(csPaceName);
					locVO[0].setCspaceid(csPaceid);
					locVO[0].setVspacename(csPaceName);
					locVO[0].setCwarehouseid(knHead.getCk());
					if(!outstore)
					{
						locVO[0].setNinspacenum(new UFDouble(Hclhgsl));
						locVO[0].setNoutspacenum(null);
					}
					else
					{
						locVO[0].setNinspacenum(null);
						locVO[0].setNoutspacenum(new UFDouble("0.0"));
					}
					locVO[0].setNinspaceassistnum(null);
					locVO[0].setNingrossnum(null);
				 	
				  }
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw e;
			}
			
			
//		} 
		return bo;
	} 

	private String getCurUser()
	{
		return InvocationInfoProxy.getInstance().getUserCode();
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
	
	   public boolean StringIsNullOrEmpty(Object obj) 
	    {
	    	return obj==null?true:String.valueOf(obj).equals("")?true:
	    		  String.valueOf(obj).equalsIgnoreCase("null")?true:String.valueOf(obj).equalsIgnoreCase("_________N/A________")?true:false;
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
		    Sql.append("SELECT  acc.faccountflag  ");
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
	private boolean IsNeedUpdateBillStatus(String key,int curcount)throws Exception
	{
		boolean rst=false ;
	  	PersistenceManager persistence=null; 
		try
		{
			int rework=0;
			//int unrework=0;
			String sql1="";
			StringBuffer  Sql=new StringBuffer();
		    Sql.append("SELECT  nvl(clzt,0) as clzt  ");
		    Sql.append("FROM mm_glcl_b where pk_glcl='"+key+"' and nvl(dr,0)=0");
	        persistence=PersistenceManager.getInstance();
	        JdbcSession  session= persistence.getJdbcSession();
	        ArrayList al=(ArrayList)session.executeQuery(Sql.toString(),  new ListProcessor());
	        if(al==null||al.size()<=0)
	        {
	          throw new Exception("单据不存在！Document does not exist!");
	        }
	        for(int i=0;i<al.size();i++)
	        {
	        	String zt= String.valueOf(al.get(i));
	        	zt=(zt==null||zt.equals("")||zt.equalsIgnoreCase("null"))?"0":zt;
	        	if(Integer.parseInt(zt)>0)
	        	{
	        		rework++;
	        	}
	        	/*else if(Integer.parseInt(zt)==0)
	        	{
	        		unrework++;
	        	}*/
	        		
	        }
	        if((rework+curcount)<al.size())
	        {
	        	rst=false;
	        }
	        else if(rework+curcount==al.size())
	        {
	        	rst=true;
	        }
	        else if(rework+curcount>al.size())
	        {
	        	throw new Exception("更新单据状态出异常！@Update documents state the abnormal!&"+rework+curcount+">"+al.size());
	        }
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
	
	}

	private boolean checkFreeItemAndBatchAndSpaceInput(GlclItemVO vo,String pk_corp,String cinventoryid,boolean isCheckHcl) throws Exception
	{
		 String errMessage="";
	     HashMap invhm= getInvdocHashtable(cinventoryid,pk_corp);
			if((invhm==null)||(invhm.size()<=0))
			{
				throw new ValidationException("查询出异常存货信息!Query exception inventory information!");
				//return false ;
			}
			String invcode=String.valueOf(invhm.get("invcode"));
			String key=vo.getPrimaryKey();
			try {
				if(Iscsflag(vo.getCk()))
				{
					String storeid=vo.getCspaceid();
					if(StringIsNullOrEmpty(storeid))
					{
						errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&的货位不能为空!@of cargo space can not be empty!")+"\n";
					}
					else if(isCheckHcl&&StringIsNullOrEmpty(vo.getHclhw()))
					{
						errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&的货位不能为空!@of cargo space can not be empty!")+"\n";
					} 
					String [] storeids=new String[]{storeid,vo.getHclhw()};
					for(int i=0;i<storeids.length;i++)
					{
						
						String Msg=null;
                        	try {
								 Msg=checkstorectl(cinventoryid,vo.getCk(),storeids[i]);
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
					String storeid=vo.getCspaceid();
					if(!StringIsNullOrEmpty(storeid))//原始货位，后处理货位不能有值
					{
						errMessage+="仓库未启用货位管理，不能录入货位！The warehouse cargo space management is not enabled, and can not be recorded in the goods-bit!";
					}
					else if(isCheckHcl&&!StringIsNullOrEmpty(vo.getHclhw()))
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
				String vbatch=String.valueOf(vo.getClph());
				if(StringIsNullOrEmpty(vbatch))
				{
	
					errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&已启用批次号管理，批次号不能为空!@Enable batch number management, batch number can not be empty")+"\n";
				}
				else if(isCheckHcl&&StringIsNullOrEmpty(vo.getHclph()))
				{
					errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&已启用批次号管理，批次号不能为空!@Enable batch number management, batch number can not be empty")+"\n";
				}
		        
			}
			else 
			{	String vbatch=String.valueOf(vo.getClph());
			    if(!StringIsNullOrEmpty(vbatch))
				{
			    	errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用批次号管理,不能录入批次号！@is not enabled, the batch number management, can not be recorded in the batch number!");
				}
			    else if(isCheckHcl&&!StringIsNullOrEmpty(vo.getHclph()))
			    {
			    	errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用批次号管理,不能录入批次号！@is not enabled, the batch number management, can not be recorded in the batch number!");
			    }
			}
			  	 String vfreeid=String.valueOf(invhm.get("free1"));
	        	 String vfree=String.valueOf(vo.getCldh());
	        	 if(StringIsNullOrEmpty(vfreeid))
	        	 { 
	        		 if(!StringIsNullOrEmpty(vfree))
	        		 {
	        			 errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用自由项，不能录入自由项！@is not enabled free term1 can not be recorded in the free term1!");
	        		 }
	        		 else if(isCheckHcl&&!StringIsNullOrEmpty(vo.getHcldh()))
	        		 {
	        			 errMessage+=Transformations.getLstrFromMuiStr("存货@Inventories&"+invcode+"&未启用自由项，不能录入自由项！@is not enabled free term1 can not be recorded in the free term1!");
	        		 }
	        	 }
	        	 else if(!StringIsNullOrEmpty(vfreeid))
	        	 {
	        		 if(StringIsNullOrEmpty(vfree))
	        	    {
	   
	        		  errMessage+=Transformations.getLstrFromMuiStr("存货@The inventory&"+invcode+"&的自由项1不能为空@free term1 can not be empty!");
	        	    }
	        		 else if(isCheckHcl&&StringIsNullOrEmpty(vo.getHcldh()))
	        		 {
	        			 errMessage+=Transformations.getLstrFromMuiStr("存货@The inventory&"+invcode+"&的自由项1不能为空@free term1 can not be empty!");
	        		 }
	        	 }
	        	 
	         
			 if(!errMessage.equals("")&&!errMessage.equalsIgnoreCase("null"))
				  throw new ValidationException(errMessage);
		 
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
}
