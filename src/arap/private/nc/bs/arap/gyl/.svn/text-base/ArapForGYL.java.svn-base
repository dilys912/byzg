/*
 * 创建日期 2005-12-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.bs.arap.gyl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.arap.verifynew.VerifyBO;
import nc.bs.arap.verifynew.VerifyServiceDMO;
import nc.bs.ep.dj.DJZBBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.pub.SystemException;
import nc.impl.arap.proxy.Proxy;
import nc.itf.fi.pub.Accperiod;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.arap.djlx.BillTypeVO;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.global.ArapDjCalculator;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.arap.gyl.VerifyParamVO;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.transaction.AccountInfo;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.arap.verifynew.Saver;
import nc.vo.arap.verifynew.VerifyFilter;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.verifynew.pub.ConditionVO;
import nc.vo.verifynew.pub.ConditionVOSqlTool;
import nc.vo.verifynew.pub.DefaultVerifyRuleVO;
import nc.vo.verifynew.pub.FenPeiUtil;
import nc.vo.verifynew.pub.ScriptVO;
import nc.vo.verifynew.pub.VerifyCom;
import nc.vo.verifynew.pub.VerifyLogVO;
import nc.vo.verifynew.pub.VerifyVO;

/**
 * @author xuhb
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 
 * zhwj
 */
public class ArapForGYL {

	/**
	 *
	 */
	public ArapForGYL() {
		super();
		// TODO 自动生成构造函数存根
	}

	public String getYeByZhangLing(String startdate, String enddate,
			String line, String ordercusmandoc, String pk_timecontrol,String pk_corp,int iArBillStat,int iAgBillStat)
			throws BusinessException {
		// TODO 自动生成方法存根
		String table= null;
		try
		{
			ArapForGYLDMO dmo=new ArapForGYLDMO();
			table = dmo.getBuleYSByZL(startdate,enddate,line,ordercusmandoc,pk_timecontrol,pk_corp,iArBillStat);
			dmo.getRedandSKByZL(startdate,enddate,line,ordercusmandoc,pk_timecontrol,pk_corp,table,iArBillStat,iAgBillStat);
		}
		catch(Exception e)
		{
			Log.getInstance(this.getClass()).error(e.getMessage());
			throw new BusinessException(e);
		}
		return table;
	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#getYeByZhangQi(java.lang.String, java.lang.String)
	 */
	public UFDouble[] getYeByZhangQi(String where, String date,String pk_corp,int iArBillStat,int iAgBillStat)
			throws BusinessException {
		// TODO 自动生成方法存根
		UFDouble[] result=new UFDouble[3];
		try
		{
			ArapForGYLDMO dmo=new ArapForGYLDMO();
			result=dmo.getBuleYSByZQ(where,date,pk_corp,iArBillStat);
			UFDouble[] temp=dmo.getRedandSKByZQ(where,pk_corp,iArBillStat,iAgBillStat);
			for(int i=0;i<3;i++)
			{
				result[i]=result[i].add(temp[i]);
			}
		}
		catch(Exception e)
		{
			Log.getInstance(this.getClass()).error(e.getMessage());
			throw new BusinessException(e);
		}
		return result;
	}

	public void Adjuest(Hashtable adjuest,String clr,String clrq,String pk_corp,int lylx,int ly) throws BusinessException
	{
		Set key=adjuest.keySet();
		Iterator it=key.iterator();
		while(it.hasNext())
		{
			String clbh=(String)it.next();
			AdjuestVO[] vo=(AdjuestVO[])adjuest.get(clbh);
			this.Adjuest(vo, clbh, clr, clrq, pk_corp, lylx, ly);
		}
	}

	public void Adjuest(AdjuestVO[] vo,String clbh,String clr,String clrq,String pk_corp,int lylx,int ly) throws BusinessException
	{
		if(ly==0)//销售
		{
			adjuestVos(vo, clbh, clr, clrq, pk_corp, lylx, ly);
		}
		else //采购
		{
			for (int i = 0; i < vo.length; i++) {
				adjuestVos(new AdjuestVO[]{vo[i]}, clbh, clr, clrq, pk_corp, lylx, ly);
			}
		}
	}

	private void adjuestVos(AdjuestVO[] vo, String clbh, String clr, String clrq, String pk_corp, int lylx, int ly) throws BusinessException {
		try
		{
			UFBoolean RB=null;
			DJZBVO zb=new DJZBVO();
			DJZBHeaderVO header=null;

			Vector items=new Vector();
			String bz=null;

			Hashtable<String,DJZBVO[]> cache=new Hashtable<String, DJZBVO[]>();

			for(int i=0;i<vo.length;i++)//对应的每个行id生成一个djzbvo，然后将所有的djzbvo合并成一个vo
			{
				DJZBVO[] djvo=null;
				Hashtable temp=null;
				if(cache.get(vo[i].getDdhh())!=null)
				{
					djvo=cache.get(vo[i].getDdhh());
				}
				else
				{
					if(lylx==0)//订单行id
				    {
						djvo=Proxy.getIArapBillPrivate().getDJByDDHID(vo[i].getDdhh());
				    }
				    else if(lylx==1)//出库单行id
				    {
				    	djvo=Proxy.getIArapBillPrivate().getDJByCKDHID(vo[i].getDdhh());
				    }
				    else if(lylx==2)//发票行id
				    {
				    	djvo=Proxy.getIArapBillPrivate().getDJByFPHID(vo[i].getDdhh());
				    }
					cache.put(vo[i].getDdhh(), djvo);
				}

				if(djvo==null ||djvo.length==0)
					return;
				List<DJZBVO> djzbVos=new ArrayList<DJZBVO>();

				
				for (int j = 0; j < djvo.length; j++) {
					if(djvo[j]!=null)
					{
						djzbVos.add(djvo[j]);
						int zgyf = djvo[j].header.getZgyf().intValue();
						int sxbz = djvo[j].header.getSxbz().intValue();
						if((zgyf==DJZBVOConsts.ZGYF_ZG || zgyf==DJZBVOConsts.ZGYF_HC ) && sxbz != DJZBVOConsts.m_intSXBZ_VALID) //存在未生效的暂估/回冲单据
						{
							throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v51-000292")/*@res "存在未生效暂估/回冲单据,无法进行回冲!"*/);
						}
					}
				}
			

				djvo=(DJZBVO[]) djzbVos.toArray(new DJZBVO[]{});

				if(djvo.length==0)
					return;

				bz=((DJZBItemVO)djvo[0].getChildrenVO()[0]).getBzbm();
				temp=this.getNewDJ(djvo,vo[i],clbh,clrq,ly,pk_corp);

				if(temp==null)
				{
					return;
				}

				Set key=temp.keySet();
				Iterator it=key.iterator();
				while(it.hasNext())
				{
					DJZBVO zbvo=(DJZBVO)it.next();
					UFBoolean isRB=(UFBoolean)temp.get(zbvo);
					RB=isRB;
					header=(DJZBHeaderVO)zbvo.getParentVO();
					for(int j=0;j<zbvo.getChildrenVO().length;j++)
					{
						items.add(zbvo.getChildrenVO()[j]);
					}
				}

			}
			UFDouble ybje=new UFDouble(0);
			UFDouble fbje=new UFDouble(0);
			UFDouble bbje=new UFDouble(0);
			for(int i=0;i<items.size();i++)//计算表投金额
			{
				ybje=ybje.add(((DJZBItemVO)items.get(i)).getYbye());
				fbje=fbje.add(((DJZBItemVO)items.get(i)).getFbye());
				bbje=bbje.add(((DJZBItemVO)items.get(i)).getBbye());
				((DJZBItemVO)items.get(i)).setDjbh(null);
				((DJZBItemVO)items.get(i)).setQxrq(new UFDate(clrq));
				((DJZBItemVO)items.get(i)).setBilldate(new UFDate(clrq));//begin-ncm-wanghbk

/*
 * 出库单  暂估  应收单
   发票    回冲  应收单
   要求暂估应收单和回冲应收单的起效日期相同 */
//				((DJZBItemVO)items.get(i)).setQxrq(new UFDate(clrq));
			}
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setDjrq(new UFDate(clrq));
			
			
			/** add by twei **/
//			Log logger = Log.getInstance(this.getClass());
//			logger.error("---adjuest Period---");
			AccountCalendar calendar = AccountCalendar.getInstance();
//			logger.error("---adjuest calendar---"+calendar);
			calendar.setDate(header.getDjrq());
			AccperiodVO  accVO = calendar.getYearVO();
//			logger.error("---adjuest accVO---"+accVO);
			header.setDjkjnd(accVO.getPeriodyear());
//			logger.error("---adjuest getVosMonth---"+accVO.getVosMonth());
			AccperiodmonthVO monthVO = calendar.getMonthVO();
//			logger.error("---adjuest monthVO---"+monthVO);
//			if(monthVO!=null){
			header.setDjkjqj(monthVO.getMonth());
//			}else{
//				header.setDjkjqj(accVO.getVosMonth()[0].getMonth());
//			}

			/*
			 * 出库单  暂估  应收单
			   发票    回冲  应收单
			   要求暂估应收单和回冲应收单的起效日期相同 */
//			header.setEffectdate(new UFDate(clrq));
//			header.setLybz(new Integer(0));
			DJZBItemVO[] it=new DJZBItemVO[items.size()];
			items.copyInto(it);
			zb.setParentVO(header);
			zb.setChildrenVO(it);

			UFDouble fbhl=it[0]!=null?it[0].getFbhl():ArapConstant.DOUBLE_ZERO;
			UFDouble bbhl=it[0]!=null?it[0].getBbhl():ArapConstant.DOUBLE_ZERO;


			if(!RB.booleanValue())//不需要红冲
			{
				this.saveEff(zb);
			}
			else
			{
				this.saveEff(zb);
				try
				{
					Hashtable vos=this.getVerifyVO(vo,lylx);
					String HxMode=null;
					Integer m_hxSeq=null;
	                if(ly==0)//销售
	                {
	                	 HxMode = SysInit.getParaString(pk_corp, "AR1");

	   				 if (HxMode.equals("最早余额法")){/*-=notranslate=-*/
	   					m_hxSeq = new Integer(0);
	   				}else{
	   					m_hxSeq = new Integer(1);
	   				}
	   				this.sortVerifyVO(vos,m_hxSeq.intValue());
	                }
	                else//采购
	                {
	                	 HxMode = SysInit.getParaString(pk_corp, "AP1");

	      				 if (HxMode.equals("最早余额法")){/*-=notranslate=-*/
	      					m_hxSeq = new Integer(0);
	      				}else{
	      					m_hxSeq = new Integer(1);
	      				}
	      				this.sortVerifyVO(vos,m_hxSeq.intValue());
	                }


					AccperiodVO accperiodvo=Accperiod.queryYearAndMonthByDate(new UFDate(clrq));
					VerifyCom com=new VerifyCom(new VerifyFilter(),new Saver(),null);
					ArrayList<VerifyLogVO> m_logs = new ArrayList<VerifyLogVO>(); 

					Set key=vos.keySet();
					Iterator itr=key.iterator();
					while(itr.hasNext())
					{
						String ddhh=(String)itr.next();
						VerifyVO[] jd=(VerifyVO[])vos.get(ddhh);
						DefaultVerifyRuleVO rb=null;
					    rb = new DefaultVerifyRuleVO();
					    rb.setM_verifyName("RED_BLUE");
					    String fbpk = Currency.getFracCurrPK(pk_corp);//从哪里取辅币pk?,红兰对冲，同币种核销，异币种核销
						String bbpk = Currency.getLocalCurrPK(pk_corp);
					    rb.setM_fbpk(fbpk);
					    rb.setM_bbpk(bbpk);
					    rb.setM_verifySeq(m_hxSeq);
					    rb.setM_shlPrecision(SysInit.getParaInt(pk_corp, "BD501"));
					    rb.setM_isFracInuse(new Boolean(fbpk==null?false:true));
					    rb.setM_dwbm(pk_corp);
					    rb.setM_Clrq(new UFDate(clrq));
					    rb.setM_Clnd(accperiodvo.getPeriodyear());
					    rb.setM_Clqj(accperiodvo.getVosMonth()[0].getMonth());
					    rb.setM_clr(clr);

					    rb.setM_debitCurr(bz);


					    rb.setM_debttoBBExchange_rate(bbhl);

					    rb.setM_debttoFBExchange_rate(fbhl);


						Integer jfbzjd = new Integer(2);




						   jfbzjd=Currency.getCurrInfo(bz).getCurrdigit();


						rb.setM_creditMnyPrecision(jfbzjd);

						rb.setM_debitMnyPrecision(jfbzjd);
//						rb.setM_newDJ(vo[i].getYbdj());
//						rb.setM_tzshl(vo[i].getShl());
						rb.setM_curr(new Currency());

						DefaultVerifyRuleVO[] rules=new DefaultVerifyRuleVO[1];
						rules[0]=rb;


					com.setM_creditdata(com.getM_filter().getCreditData(jd));
					com.setM_debitdata(com.getM_filter().getDebitData(jd));
					com.setM_verifySequence(ScriptVO.getVerifySequence(4,rules));
					com.setM_rule(rules);
					com.setM_creditSelected(com.getM_filter().getCreditData(jd));
					com.setM_debitSelected(com.getM_filter().getDebitData(jd));
					FenPeiUtil.fenTanJsybjeRB(ybje,jd,new UFDouble(0),pk_corp,new UFDate(clrq),ly==0?0:1,fbhl,bbhl);
					com.verify(com.getM_debitSelected(),com.getM_creditSelected());
					for(int i=0;i<com.getM_logs().size();i++)
						{
							((VerifyLogVO)com.getM_logs().get(i)).setM_clbh(clbh);
//							((VerifyLogVO)com.getM_logs().get(i)).setM_pph(new Integer(i+1));
							((VerifyLogVO)com.getM_logs().get(i)).setM_clbz(new Integer(10));//调查标志

						}
						m_logs.addAll(com.getM_logs());
					}
					com.setM_logs(m_logs);
					com.save();
				}
				catch(Exception e)
				{
					 Log.getInstance(VerifyBO.class).error(e);
//				    	try
//				    	{
//				    		DJZBBO bo=new DJZBBO();
//				    		bo.returnBillCode(zb);
//				    	}
//				    	catch(Exception ex)
//				    	{
//
//				    	}
					 	throw new BusinessException(e.getMessage(),e);


//					Proxy.getIArapBillPublic().sendArapBillDelMsg(zb);
				}

			}
		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			//throw new BusinessException(e.getMessage());
		}
	}



	private Hashtable getNewDJ(DJZBVO[] vo,AdjuestVO adjust,String clbh,String clrq,int ly,String pk_corp) throws Exception
	{
		Hashtable result=new Hashtable();
		DJZBVO zbvo=new DJZBVO();
		DJZBItemVO[] temp=null;
		UFDouble shlye=new UFDouble(0);
		DJZBVO source=null;
		Vector allitem=new Vector();
		for(int i=0;i<vo.length;i++)
		{
			if(vo[i]==null)
				continue;
			DJZBHeaderVO header=(DJZBHeaderVO)vo[i].getParentVO();
			DJZBItemVO[] items=(DJZBItemVO[])vo[i].getChildrenVO();
			if(header.getZgyf().intValue()==1)
			{
				source=vo[i];
			}
			for(int j=0;j<items.length;j++)
			{
				DJZBItemVO zbItem=items[j];
				shlye=shlye.add(zbItem.getShlye());
				zbItem.setIsverifyfinished(new UFBoolean(false));
				zbItem.setVerifyfinisheddate(null);
				allitem.add(zbItem);
			}
		}

		if(adjust.getIsdone().booleanValue() || adjust.getShl().doubleValue()== shlye.doubleValue())//最后一次调整
		{
			if(shlye.doubleValue()==0)
			{
				return null;
			}
			Vector con=new Vector();
			DJZBItemVO[] item=new DJZBItemVO[allitem.size()];
			allitem.copyInto(item);
			UFDouble ybje=new UFDouble(0);
			UFDouble fbje=new UFDouble(0);
			UFDouble bbje=new UFDouble(0);

			for(int i=0;i<item.length;i++)
			{

				if(item[i].getShlye().doubleValue()==0)
					continue;


				DJZBItemVO newItem=(DJZBItemVO)item[i].clone();
				con.add(newItem);
				newItem.setClbh(clbh);
				newItem.setJsfsbm(((DJZBHeaderVO)source.getParentVO()).getDjlxbm());
				newItem.setDdhh(((DJZBItemVO)((DJZBItemVO)source.getChildrenVO()[0])).getFb_oid());
				newItem.setDdlx(((DJZBItemVO)((DJZBItemVO)source.getChildrenVO()[0])).getVouchid());

				if(ly==0)//销售
				{

						newItem.setJfshl(newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setDj(null);
						newItem.setHsdj(null);
						newItem.setJfybje(newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setJffbje(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setJfbbje(newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"jfybje",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
						newItem.setYbye(newItem.getJfybje());
						newItem.setFbye(newItem.getJffbje());
						newItem.setBbye(newItem.getJfbbje());
						newItem.setShlye(newItem.getJfshl());

				}
				else
				{
						newItem.setDfshl(newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setDj(null);
						newItem.setHsdj(null);
						newItem.setDfybje(newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setDffbje(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem.setDfbbje(newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
						newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"dfybje",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
						newItem.setYbye(newItem.getDfybje());
						newItem.setFbye(newItem.getDffbje());
						newItem.setBbye(newItem.getDfbbje());
						newItem.setShlye(newItem.getDfshl());


				}
				ybje.add(newItem.getYbye());
				fbje.add(newItem.getFbye());
				bbje.add(newItem.getBbye());

			}
			temp=new DJZBItemVO[con.size()];
			con.copyInto(temp);
			DJZBHeaderVO header=(DJZBHeaderVO)source.getParentVO().clone();
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			zbvo.setParentVO(header);
			zbvo.setChildrenVO(temp);
			result.put(zbvo,new UFBoolean(true));//生成的DJZBVO为key，是否需要红冲为value
		}
		else
		{
			UFDouble tzshl=adjust.getShl().multiply(-1);//调整数量
			if(tzshl.doubleValue()==0)
			{
				return null;
			}
//			else if(tzshl.doubleValue()!=0 && shlye.doubleValue()==0)
//			{
//
//			}
			else if(tzshl.multiply(shlye).doubleValue()>=0)//同号
			{

					temp=new DJZBItemVO[1];
					temp[0]=(DJZBItemVO)((DJZBItemVO)source.getChildrenVO()[0]).clone();
					temp[0].setDdhh(((DJZBItemVO)((DJZBItemVO)source.getChildrenVO()[0])).getFb_oid());
					temp[0].setDdlx(((DJZBItemVO)((DJZBItemVO)source.getChildrenVO()[0])).getVouchid());
					temp[0].setClbh(clbh);
				    temp[0].setJsfsbm(((DJZBHeaderVO)source.getParentVO()).getDjlxbm());
					temp[0].setShlye(tzshl);
					temp[0].setIsverifyfinished(new UFBoolean(false));
					temp[0].setVerifyfinisheddate(null);

					if(ly==0)//销售
					{
						temp[0].setJfshl(tzshl);
						temp[0]=ArapDjCalculator.getInstance().calculateVO(temp[0],"jfshl",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
						temp[0].setYbye(temp[0].getJfybje());
						temp[0].setFbye(temp[0].getJffbje());
						temp[0].setBbye(temp[0].getJfbbje());
					}
					else
					{
						temp[0].setDfshl(tzshl);
						temp[0]=ArapDjCalculator.getInstance().calculateVO(temp[0],"dfshl",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
						temp[0].setYbye(temp[0].getDfybje());
						temp[0].setFbye(temp[0].getDffbje());
						temp[0].setBbye(temp[0].getDfbbje());
					}


					DJZBHeaderVO header=(DJZBHeaderVO)source.getParentVO().clone();
					header.setDjbh(null);
					header.setZgyf(new Integer(2));
					header.setYbje(temp[0].getYbye());
					header.setFbje(temp[0].getFbye());
					header.setBbje(temp[0].getBbye());
					zbvo.setParentVO(header);
					zbvo.setChildrenVO(temp);
					result.put(zbvo,new UFBoolean(false));//生成的DJZBVO为key，是否需要红冲为value


			}
			else//异号，需要红冲
			{
				Vector con=new Vector();

				if(tzshl.abs().doubleValue()>shlye.abs().doubleValue())
				{
					tzshl=tzshl.div(adjust.getShl().abs()).multiply(shlye.abs());
				}

				DJZBItemVO[] item=new DJZBItemVO[allitem.size()];
				allitem.copyInto(item);
				UFDouble ybje=new UFDouble(0);
				UFDouble fbje=new UFDouble(0);
				UFDouble bbje=new UFDouble(0);

				for(int i=0;i<item.length;i++)//分摊调整数量
				{
					if(item[i].getShlye().doubleValue()==0 && ( item[i].getYbye().doubleValue()!=0 || item[i].getFbye().doubleValue()!=0 || item[i].getBbye().doubleValue()!=0 ))
					{
						DJZBItemVO newItem=(DJZBItemVO)item[i].clone();
						con.add(newItem);
						newItem.setDdhh(item[i].getFb_oid());
						newItem.setDdlx(item[i].getVouchid());
						newItem.setClbh(clbh);

						if(ly==0)//销售
						{
								newItem.setJfshl(newItem.getJfshl()==null?null:newItem.getJfshl().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setShlye(newItem.getShlye()==null?null:newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDj(null);
								newItem.setHsdj(null);
								newItem.setJfybje(newItem.getJfybje()==null?null:newItem.getJfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJffbje(newItem.getJffbje()==null?null:newItem.getJffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJfbbje(newItem.getJfbbje()==null?null:newItem.getJfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setYbye(newItem.getYbye()==null?null:newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setFbye(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setBbye(newItem.getBbye()==null?null:newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJfybwsje(newItem.getJfybwsje()==null?null:newItem.getJfybwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setWbfybje(newItem.getWbffbje()==null?null:newItem.getWbfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setWbfbbje(newItem.getWbffbje()==null?null:newItem.getWbfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJfybsj(newItem.getJfybsj()==null?null:newItem.getJfybsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJffbsj(newItem.getJffbsj()==null?null:newItem.getJffbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setJfbbsj(newItem.getJfbbsj()==null?null:newItem.getJfbbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));

						}
						else
						{
								newItem.setDfshl(newItem.getDfshl()==null?null:newItem.getDfshl().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setShlye(newItem.getShlye()==null?null:newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDj(null);
								newItem.setHsdj(null);
								newItem.setDfybje(newItem.getDfybje()==null?null:newItem.getDfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDffbje(newItem.getDffbje()==null?null:newItem.getDffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDfbbje(newItem.getDfbbje()==null?null:newItem.getDfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setYbye(newItem.getYbye()==null?null:newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setFbye(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setBbye(newItem.getBbye()==null?null:newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDfybwsje(newItem.getDfybwsje()==null?null:newItem.getDfybwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setWbffbje(newItem.getWbffbje()==null?null:newItem.getWbffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDfbbwsje(newItem.getDfbbwsje()==null?null:newItem.getDfbbwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDfybsj(newItem.getDfybsj()==null?null:newItem.getDfybsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDffbsj(newItem.getDffbsj()==null?null:newItem.getDffbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
								newItem.setDfbbsj(newItem.getDfbbsj()==null?null:newItem.getDfbbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
						}
						ybje=ybje.add(newItem.getYbye());
						fbje=fbje.add(newItem.getFbye());
						bbje=bbje.add(newItem.getBbye());

						continue;
					}
					if(tzshl.doubleValue()==0)
					{
						continue;
					}
					if(item[i].getShlye().doubleValue()==0)
					{
						continue;
					}
					if(item[i].getShlye().abs().doubleValue()>tzshl.abs().doubleValue())
					{
						DJZBItemVO newItem=(DJZBItemVO)item[i].clone();
						con.add(newItem);
						newItem.setDdhh(item[i].getFb_oid());
						newItem.setDdlx(item[i].getVouchid());
						newItem.setClbh(clbh);
						newItem.setShlye(tzshl);
						newItem.setJsfsbm(((DJZBHeaderVO)source.getParentVO()).getDjlxbm());

//						item[i].setShlye(item[i].getShlye().add(tzshl));
						if(ly==0)//销售
						{
								newItem.setJfshl(tzshl);
								newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"jfshl",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
								newItem.setYbye(newItem.getJfybje());
								newItem.setFbye(newItem.getJffbje());
								newItem.setBbye(newItem.getJfbbje());

						}
						else
						{
								newItem.setDfshl(tzshl);
								newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"dfshl",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
								newItem.setYbye(newItem.getDfybje());
								newItem.setFbye(newItem.getDffbje());
								newItem.setBbye(newItem.getDfbbje());


//							item[i].setYbye(item[i].getYbye().add(newItem.getYbye()));
//							item[i].setFbye(item[i].getFbye().add(newItem.getFbye()));
//							item[i].setBbye(item[i].getBbye().add(newItem.getBbye()));

						}

						tzshl=new UFDouble(0);
						ybje=ybje.add(newItem.getYbye());
						fbje=fbje.add(newItem.getFbye());
						bbje=bbje.add(newItem.getBbye());

					}
					else
					{
						DJZBItemVO newItem=(DJZBItemVO)item[i].clone();
						con.add(newItem);
						newItem.setDdhh(item[i].getFb_oid());
						newItem.setDdlx(item[i].getVouchid());
						newItem.setClbh(clbh);
						newItem.setShlye(tzshl.div(tzshl.abs()).multiply((item[i].getShlye().abs())));
						newItem.setJsfsbm(((DJZBHeaderVO)source.getParentVO()).getDjlxbm());

//						item[i].setShlye(new UFDouble(0));


						if(ly==0)//销售
						{
							newItem.setJfshl(newItem.getShlye());
							newItem.setDj(null);
							newItem.setHsdj(null);
							newItem.setJfybje(newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem.setJffbje(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem.setJfbbje(newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"jfybje",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
							newItem.setYbye(newItem.getJfybje());
							newItem.setFbye(newItem.getJffbje());
							newItem.setBbye(newItem.getJfbbje());


						}
						else
						{
							newItem.setDfshl(newItem.getShlye());
							newItem.setDj(null);
							newItem.setHsdj(null);
							newItem.setDfybje(newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem.setDffbje(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem.setDfbbje(newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
							newItem=ArapDjCalculator.getInstance().calculateVO(newItem,"dfybje",clrq,((DJZBHeaderVO)source.getParentVO()).getDjdl(),this.getParam(pk_corp, ly));
							newItem.setYbye(newItem.getDfybje());
							newItem.setFbye(newItem.getDffbje());
							newItem.setBbye(newItem.getDfbbje());
						}
						tzshl=tzshl.sub(newItem.getShlye());
						ybje=ybje.add(newItem.getYbye());
						fbje=fbje.add(newItem.getFbye());
						bbje=bbje.add(newItem.getBbye());

//						item[i].setYbye(item[i].getYbye().add(newItem.getYbye()));
//						item[i].setFbye(item[i].getFbye().add(newItem.getFbye()));
//						item[i].setBbye(item[i].getBbye().add(newItem.getBbye()));

					}
				}
				temp=new DJZBItemVO[con.size()];
				con.copyInto(temp);
				DJZBHeaderVO header=(DJZBHeaderVO)source.getParentVO().clone();
				header.setDjbh(null);
				header.setZgyf(new Integer(2));
				header.setYbje(ybje);
				header.setFbje(fbje);
				header.setBbje(bbje);
				zbvo.setParentVO(header);
				zbvo.setChildrenVO(temp);
				result.put(zbvo,new UFBoolean(true));//生成的DJZBVO为key，是否需要红冲为value

			}
		}

		return result;
	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#unAdjuest(java.lang.String)
	 */

	public void unAdjuest(String[] clbh,String pk_corp) throws BusinessException {
		 for(int i=0;i<clbh.length;i++)
		    {
		    	this.unAdjuest(clbh[i],pk_corp);
		    }
	}
	public void unAdjuest(String clbh,String pk_corp) throws BusinessException {
		// TODO 自动生成方法存根
		try
		{
			UFBoolean isrb=new ArapForGYLDMO().isRB(clbh);
		    if(isrb.booleanValue())//经过红冲
		    {
		    	new VerifyBO().unSave(new String[]{clbh,pk_corp});
		    	String[] vouchid=new ArapForGYLDMO().getDj(clbh);
		    	for(int i=0;i<vouchid.length;i++)
		    	{
		    		DJZBVO zbvo=new DJZBBO().findByPrimaryKey(vouchid[i]);
		    		this.deleteEff(zbvo);
		    	}
		    }
		    else
		    {
		    	String[] vouchid=new ArapForGYLDMO().getDj(clbh);
		    	for(int i=0;i<vouchid.length;i++)
		    	{
		    		DJZBVO zbvo=new DJZBBO().findByPrimaryKey(vouchid[i]);
		    		this.deleteEff(zbvo);
		    	}
		    }
		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			throw new BusinessException(e);
		}

	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#saveEff(nc.vo.ep.dj.DJZBVO)
	 */
	public void saveEff(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm=((DJZBHeaderVO)vo.getParentVO()).getDjlxbm();
		String pk_corp=((DJZBHeaderVO)vo.getParentVO()).getDwbm();
		String date = ((DJZBHeaderVO)vo.getParentVO()).getDjrq().toString();

		String prodid = "AR";
		if(vo.header.getPzglh().intValue()==1){
			prodid = "AP";
		}
		
		boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp,prodid,new nc.vo.pub.lang.UFDate(date));
		if(!bPass){
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000434")/*@res "系统已经结帐，不能进行此操作。"*/);

		}
		BillTypeVO[] vos=Proxy.getIArapBillTypePrivate().queryBillTypeByBillTypeCode(djlxbm,pk_corp);
		if(vos==null || vos.length==0)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000435"));
		}
		else if(vos.length>1)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000436"));
		}

		new DJZBBO().saveDj(vo);

		((DJZBHeaderVO)vo.getParentVO()).setShkjnd(((DJZBHeaderVO)vo.getParentVO()).getDjkjnd());
		((DJZBHeaderVO)vo.getParentVO()).setShkjqj(((DJZBHeaderVO)vo.getParentVO()).getDjkjqj());
		((DJZBHeaderVO)vo.getParentVO()).setShr(((DJZBHeaderVO)vo.getParentVO()).getLrr());
		((DJZBHeaderVO)vo.getParentVO()).setShrq(((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		if(((DjLXVO)vos[0].getParentVO()).getIsqr()==null ||!((DjLXVO)vos[0].getParentVO()).getIsqr().booleanValue()) //审核态
		{
			((DJZBHeaderVO)vo.getParentVO()).setDjzt(new Integer(2));
			((DJZBHeaderVO)vo.getParentVO()).setSxbz(new Integer(10));
		}
		else //签字确认
		{
			((DJZBHeaderVO)vo.getParentVO()).setDjzt(new Integer(3));
			((DJZBHeaderVO)vo.getParentVO()).setSxbz(new Integer(10));

			((DJZBHeaderVO)vo.getParentVO()).setYhqrkjnd(((DJZBHeaderVO)vo.getParentVO()).getDjkjnd());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrkjqj(((DJZBHeaderVO)vo.getParentVO()).getDjkjqj());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrr(((DJZBHeaderVO)vo.getParentVO()).getLrr());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrrq(((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		}
//		((DJZBHeaderVO)vo.getParentVO()).setZgyf(new Integer(1));

		//保存单据


		Proxy.getIArapBillPublic().auditOneArapBill(vo);

//		new ApplayBillBO().sendMessage(vo);
	}


	public void saveEffForCG(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm=((DJZBHeaderVO)vo.getParentVO()).getDjlxbm();
		String pk_corp=((DJZBHeaderVO)vo.getParentVO()).getDwbm();
		String date = ((DJZBHeaderVO)vo.getParentVO()).getDjrq().toString();

		String prodid = "AP";
		boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp,prodid,new nc.vo.pub.lang.UFDate(date));
		if(!bPass){
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000434")/*@res "系统已经结帐，不能进行此操作。"*/);

		}
		BillTypeVO[] vos=Proxy.getIArapBillTypePrivate().queryBillTypeByBillTypeCode(djlxbm,pk_corp);
		if(vos==null || vos.length==0)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000435"));
		}
		else if(vos.length>1)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000436"));
		}

		new DJZBBO().saveDj(vo);

		((DJZBHeaderVO)vo.getParentVO()).setShkjnd(((DJZBHeaderVO)vo.getParentVO()).getDjkjnd());
		((DJZBHeaderVO)vo.getParentVO()).setShkjqj(((DJZBHeaderVO)vo.getParentVO()).getDjkjqj());
		((DJZBHeaderVO)vo.getParentVO()).setShr(((DJZBHeaderVO)vo.getParentVO()).getLrr());
		((DJZBHeaderVO)vo.getParentVO()).setShrq(((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		if(((DjLXVO)vos[0].getParentVO()).getIsqr()==null ||!((DjLXVO)vos[0].getParentVO()).getIsqr().booleanValue()) //审核态
		{
			((DJZBHeaderVO)vo.getParentVO()).setDjzt(new Integer(2));
			((DJZBHeaderVO)vo.getParentVO()).setSxbz(new Integer(10));
		}
		else //签字确认
		{
			((DJZBHeaderVO)vo.getParentVO()).setDjzt(new Integer(3));
			((DJZBHeaderVO)vo.getParentVO()).setSxbz(new Integer(10));

			((DJZBHeaderVO)vo.getParentVO()).setYhqrkjnd(((DJZBHeaderVO)vo.getParentVO()).getDjkjnd());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrkjqj(((DJZBHeaderVO)vo.getParentVO()).getDjkjqj());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrr(((DJZBHeaderVO)vo.getParentVO()).getLrr());
			((DJZBHeaderVO)vo.getParentVO()).setYhqrrq(((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		}
//		((DJZBHeaderVO)vo.getParentVO()).setZgyf(new Integer(1));

		//保存单据


		Proxy.getIArapBillPublic().auditOneArapBill(vo);

//		new ApplayBillBO().sendMessage(vo);
	}
	/**
	 * @author 王凯飞
	 * 功能：应付单保存
	 * 日期：2014-11-18
	 * */
	public void saveEffForCG2(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm=((DJZBHeaderVO)vo.getParentVO()).getDjlxbm();
		String pk_corp=((DJZBHeaderVO)vo.getParentVO()).getDwbm();
		String date = ((DJZBHeaderVO)vo.getParentVO()).getDjrq().toString();

		String prodid = "AP";
		boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp,prodid,new nc.vo.pub.lang.UFDate(date));
		if(!bPass){
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000434")/*@res "系统已经结帐，不能进行此操作。"*/);

		}
		BillTypeVO[] vos=Proxy.getIArapBillTypePrivate().queryBillTypeByBillTypeCode(djlxbm,pk_corp);
		if(vos==null || vos.length==0)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000435"));
		}
		else if(vos.length>1)
		{
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000436"));
		}

		new DJZBBO().saveDj(vo);

	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#deleteEff(nc.vo.ep.dj.DJZBVO)
	 */
	public void deleteEff(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

//		String ddhh=((DJZBItemVO)vo.getChildrenVO()[0]).getDdhh();
//		String ddlx=((DJZBItemVO)vo.getChildrenVO()[0]).getDdlx();
//		DJZBHeaderVO[] headers=new DJZBBO().queryHead(" inner join arap_djfb fb on zb.vouchid=fb.vouchid where fb.ddhh='"+ddhh+"' and fb.ddlx='"+ddlx+"' and fb.dr=0 and zb.dr=0");
//		vo.setParentVO(headers[0]);
//		DJZBItemVO[] item=Proxy.getIArapBillPrivate().queryDjzbitems(headers[0].getVouchid());
//		for(int i=0;i<item.length;i++)
//		{
//			item[i].setPausetransact(new UFBoolean(false));
//		}
//		vo.setChildrenVO(item);
		//删除单据
//		new ApplayBillBO().sendMessage_del(vo);
		Proxy.getIArapBillPublic().unAuditOneArapBill(vo);
		new DJZBBO().deleteDj(vo);
	}

	public void deleteEffForCG(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		//zhwj
		DJZBHeaderVO headvo = (DJZBHeaderVO)vo.getParentVO();
		String zyx29 = headvo.getZyx29()==null||headvo.getZyx29().equals("")?"N":headvo.getZyx29().toString();
		
		String ddhh=((DJZBItemVO)vo.getChildrenVO()[0]).getDdhh();
		String ddlx=((DJZBItemVO)vo.getChildrenVO()[0]).getDdlx();
//		DJZBHeaderVO[] headers=new DJZBBO().queryHead(" inner join arap_djfb fb on zb.vouchid=fb.vouchid where fb.ddhh='"+ddhh+"' and fb.ddlx='"+ddlx+"' and fb.dr=0 and zb.dr=0");
		
		//zhwj 系统暂估生成应付单 zyx29=null zyx30=null
//		                    运费暂估生成应付单 zyx29=Y zyx30=null
//             结案生成应付单 zyx29=N zyx30=Y
//		 所以  系统反暂时 删除应付单 zyx29=N  zyx30=N    运费反暂 删除应付单 zyx29=Y  zyx30=N
		DJZBHeaderVO[] headers=new DJZBBO().queryHead(" inner join arap_djfb fb on zb.vouchid=fb.vouchid where fb.ddhh='"+ddhh+"' and fb.ddlx='"+ddlx+"' and fb.dr=0 and zb.dr=0 and nvl(zb.zyx29,'N')='"+zyx29+"' and nvl(zb.zyx30,'N')='N'");
		
		vo.setParentVO(headers[0]);
		DJZBItemVO[] item=Proxy.getIArapBillPrivate().queryDjzbitems(headers[0].getVouchid());
		for(int i=0;i<item.length;i++)
		{
			item[i].setPausetransact(new UFBoolean(false));
		}
		vo.setChildrenVO(item);
		//删除单据
//		new ApplayBillBO().sendMessage_del(vo);
		Proxy.getIArapBillPublic().unAuditOneArapBill(vo);
		new DJZBBO().deleteDj(vo);
	}



	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#verify(null[], java.lang.String, java.lang.String, java.lang.String)
	 */
	public void verifyForGYL(VerifyParamVO[] vo, String clbh, String clr, String clrq,String pk_corp)
			throws BusinessException {
		// TODO 自动生成方法存根

		String sql="";
		VerifyFilter filter=new VerifyFilter();
		Saver saver=new Saver();
		VerifyCom com=new VerifyCom(filter,saver,null);
		try
		{
			int pph=0;
			for(int i=0;i<vo.length;i++)
			{
				if(vo[i].getDdhh()!=null)
				{
					sql=" fb.ddhh='"+vo[i].getDdhh()+"' or fb.ddhh='"+vo[i].getThdddhh()+"' ";
				}
				else
				{
					sql=" fb.cksqsh='"+vo[i].getDdlx()+"' or fb.ddhh='"+vo[i].getThdddhh()+"' ";
				}
				ConditionVO condition=new ConditionVO();
				condition.setAttributeValue("other",sql);
		        ConditionVOSqlTool sqltool = new ConditionVOSqlTool(condition);
				String sql1 = sqltool.getSql();
				VerifyServiceDMO dmo = null;
				dmo = new VerifyServiceDMO();
				VerifyVO[] vos = dmo.queryVerifyVO(sql1);
				com.setM_creditdata(filter.getCreditData(vos));
				com.setM_debitdata(filter.getDebitData(vos));
				com.setM_creditSelected(com.getM_creditdata());
				VerifyVO[] credit=com.getM_creditdata();
				VerifyVO[] debit=com.getM_debitdata();


				for(int i1=0;i1<credit.length;i1++)
				{
					credit[i1].setM_jsybje(credit[i1].getM_ybye());
					credit[i1].setM_jsfbje(credit[i1].getM_fbye());
					credit[i1].setM_jsbbje(credit[i1].getM_bbye());
				}
				for(int i1=0;i1<debit.length;i1++)
				{
					debit[i1].setM_jsybje(debit[i1].getM_ybye());
					debit[i1].setM_jsfbje(debit[i1].getM_fbye());
					debit[i1].setM_jsbbje(debit[i1].getM_bbye());
				}
				com.setM_debitSelected(com.getM_debitSelected());


				String HxMode = SysInit.getParaString(pk_corp, "AR1");
				 Integer m_hxSeq=null;
				 if (HxMode.equals("最早余额法")){/*-=notranslate=-*/
					m_hxSeq = new Integer(0);
				}else{
					m_hxSeq = new Integer(1);
				}
				 AccperiodVO accperiodvo=Accperiod.queryAccyearByDate(new UFDate(clrq));
				String bz=vos[0].getM_CurrPk();

				String fbbz=Currency.getFracCurrPK(pk_corp);
				String bbbz=Currency.getLocalCurrPK(pk_corp);
				UFDouble bbhl=Currency.getRateBoth(pk_corp,bz,clrq)[1];
				UFDouble fbhl=Currency.getRateBoth(pk_corp,bz,clrq)[0];
				DefaultVerifyRuleVO rb=null;
			    rb = new DefaultVerifyRuleVO();
			    rb.setM_verifyName("RED_BLUE");

			    rb.setM_shlPrecision(SysInit.getParaInt(pk_corp, "BD501"));
			    rb.setM_fbpk(fbbz);
			    rb.setM_bbpk(bbbz);
			    rb.setM_verifySeq(m_hxSeq);

			    rb.setM_isFracInuse(new Boolean(fbbz==null?false:true));
			    rb.setM_dwbm(pk_corp);
			    rb.setM_Clrq(new UFDate(clrq));
			    rb.setM_Clnd(accperiodvo.getPeriodyear());
			    rb.setM_Clqj(accperiodvo.getVosMonth()[0].getMonth());
			    rb.setM_clr(clr);

			    rb.setM_debitCurr(bz);


			    rb.setM_debttoBBExchange_rate(bbhl);

			    rb.setM_debttoFBExchange_rate(fbhl);



				Integer jfbzjd = new Integer(2);
				Integer dfbzjd = new Integer(2);


				if(fbbz!=null)
					jfbzjd=Currency.getCurrInfo(fbbz).getCurrdigit();
				dfbzjd=Currency.getCurrInfo(bbbz).getCurrdigit();




				rb.setM_debitMnyPrecision(jfbzjd);
				rb.setM_creditMnyPrecision(dfbzjd);
				rb.setM_curr(new Currency());

				DefaultVerifyRuleVO[] rules=new DefaultVerifyRuleVO[1];
				rules[0]=rb;

				com.setM_verifySequence(ScriptVO.getVerifySequence(7,rules));

				com.setM_rule(rules);

				com.verify(com.getM_debitSelected(),com.getM_creditSelected());

				//处理小号
				boolean iscomplete=false;

				for(int j=0;j<com.getM_logs().size();j++)
				{

					((VerifyLogVO)com.getM_logs().get(j)).setM_clbh(clbh);

					int temp=((VerifyLogVO)com.getM_logs().get(j)).getM_pph().intValue();
					((VerifyLogVO)com.getM_logs().get(j)).setM_pph(new Integer(pph));

					for(int k=i+1;k<com.getM_logs().size();k++)
					{

						if(temp==(((VerifyLogVO)com.getM_logs().get(k)).getM_pph()).intValue())
						{
							((VerifyLogVO)com.getM_logs().get(k)).setM_pph(new Integer(pph));
							((VerifyLogVO)com.getM_logs().get(k)).setM_clbh(clbh);
							if(k==com.getM_logs().size()-1)
							{
								iscomplete=true;
							}
						}
						else
						{
							j=k-1;
							break;
						}


					}
					if(iscomplete)
						break;
					pph++;
				}

			}

			com.save();
		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			throw new BusinessException(e);
		}
	}
	public void verifyForGYLBattch(Hashtable VerifyParam, String clr, String clrq,String pk_corp)
	throws BusinessException {
		Set key=VerifyParam.keySet();
		Iterator it=key.iterator();
		while(it.hasNext())
		{
			String clbh=(String)it.next();
			VerifyParamVO[] vo=(VerifyParamVO[])VerifyParam.get(clbh);
			this.verifyForGYL(vo, clbh, clr, clrq, pk_corp);
		}
	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#unVerify(java.lang.String)
	 */
	public void unVerifyForGYL(String clbh) throws BusinessException {
		// TODO 自动生成方法存根

		VerifyFilter filter=new VerifyFilter();
		Saver saver=new Saver();
		VerifyCom com=new VerifyCom(filter,saver,null);
		com.unSave(new String[]{clbh});
	}

	public void unVerifyForGYLBattch(String[] clbh) throws BusinessException {
	    for(int i=0;i<clbh.length;i++)
	    {
	    	this.unVerifyForGYL(clbh[i]);
	    }
	}

	/* （非 Javadoc）
	 * @see nc.itf.arap.pub.IArapForGYLPublic#canModify(java.lang.String)
	 */
	public UFBoolean canModify(String pk_corp) throws BusinessException {
		// TODO 自动生成方法存根
		UFBoolean result=null;
		try
		{
			result=new ArapForGYLDMO().canModify(pk_corp);
		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			throw new BusinessException(e);
		}
		return result;
	}



	//根据ddhh查询单据,返回hashtable,key=ddhh,value=VerifyVO[]
	private Hashtable getVerifyVO(AdjuestVO[] vo,int lylx) throws BusinessException
	{
		try
		{
			Hashtable result=new Hashtable();
			ConditionVO condition=new ConditionVO();
			VerifyVO[] vos=null;
			String fb="";
			if(lylx==0)//订单行id
		    {
		    	fb=" fb.ddhid ='";
		    }
		    else if(lylx==1)//出库单行id
		    {
		    	fb=" fb.ckdid ='";
		    }
		    else if(lylx==2)//发票行id
		    {
		    	fb=" fb.fphid ='";
		    }
			for(int i=0;i<vo.length;i++)
			{
				String sql3=fb+vo[i].getDdhh()+"' ";
		        condition.setAttributeValue("other",sql3);
		        ConditionVOSqlTool sqltool = new ConditionVOSqlTool(condition);
				String sql1 = sqltool.getSql();
//				if(vo[i].getYbdj()==null || vo[i].getYbdj().doubleValue()==0) //调数量
//				{
				if(sql1.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0")!=-1)
				{
					String sql2=sql1.substring(sql1.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0")+"and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0".length());
					sql1=sql1.substring(0,sql1.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0"));

				    sql1=sql1+" and zb.sxbz=10 "+sql2;

				}
//				}
//
				VerifyServiceDMO dmo = null;
				dmo = new VerifyServiceDMO();
				vos = dmo.queryVerifyVO(sql1);
				result.put(vo[i].getDdhh(),vos);
			}
			return result;

		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			throw new BusinessException(e);
		}
	}

//	private void checkYE(AdjuestVO[] vos,Hashtable verifyvo) throws BusinessException
//	{
//		for(int i=0;i<vos.length;i++)
//		{
//			Object verify1=verifyvo.get(vos[i].getDdhh());
//			Object[] verify=(Object[])verify1;
//			UFDouble ye=new UFDouble(0);
//			if(verify==null || verify.length==0)
//				continue;
//			for(int j=0;j<verify.length;j++)
//			{
//				ye=ye.add(((VerifyVO)verify[j]).getM_ybye());
//			}
//			UFDouble dj=null;
//			if(vos[i].getYbdj()==null || vos[i].getYbdj().doubleValue()==0) //调数量
//				dj=((VerifyVO)verify[0]).getM_dj();
//			else //调单价
//				dj=((VerifyVO)verify[0]).getM_dj().sub(vos[i].getYbdj());
//			UFDouble je=dj.multiply(vos[i].getShl());
//			if(ye.doubleValue()>0 && ye.sub(je).doubleValue()<0) //余额不足
//			{
//				throw new BusinessException("余额不足，不能调整");
//			}
//			if(ye.doubleValue()<0 && ye.sub(je).doubleValue()>0) //余额不足
//			{
//				throw new BusinessException("余额不足，不能调整");
//			}
//
//		}
//	}

	private void sortVerifyVO(Hashtable verifyvo,Integer mode) throws BusinessException
	{
		Set keys=verifyvo.keySet();
		Iterator it=keys.iterator();
		while(it.hasNext())
		{
			this.sortVector((VerifyVO[])verifyvo.get(it.next()),mode.intValue());
		}
	}

	private  void sortVector(VerifyVO[] vos,int flag){
		if(vos== null ||vos.length < 2){
			return;
		}
		for(int i = 0; i<vos.length;i++){
			//m_xydqr信用到期日
			UFDate xydqr = vos[i].getM_xydqr();
			for(int j = i+1; j < vos.length; j++){
				//m_xydqr信用到期日
				UFDate xydqr2 = vos[j].getM_xydqr();
				if(flag == 0){//顺排
					if(xydqr2.before(xydqr)){
						swap(vos,i, j);
					}
				}else if(flag == 1){//倒排
					if(xydqr2.after(xydqr)){
						swap(vos,i, j);
					}
				}
			}

		}
	}



	private static void swap(VerifyVO[] vos, int i,int j) {
		VerifyVO temp = vos[j];
		vos[j]=vos[i];
		vos[i] = temp;
	}


	public void saveAdjust(DJZBVO vo, AdjuestVO[] adjustvo, String clbh,
			String clr, String slrq, String pk_corp, int lylx,int ly)
			throws BusinessException {
		// TODO 自动生成方法存根
		new DJZBBO().save(vo);
		this.Adjuest(adjustvo,clbh,clr,slrq,pk_corp,lylx,ly);
	}

	public void unSaveAdjust(DJZBVO vo, String clbh,String pk_corp) throws BusinessException
	{
		this.unAdjuest(clbh,pk_corp);
	    new DJZBBO().deleteDj(vo);
	}


	private int[] getParam(String pk_corp,int pzglh) throws Exception
	{
		if(pzglh==0)
		{
			if(((ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName())).getParaString(pk_corp, "AR21").equals("含税价格优先"))/*-=notranslate=-*/
			{
				return new int[]{RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE};
			}
			else
			{
				return new int[]{RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE};
			}

		}
		else
		{
			if(((ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName())).getParaString(pk_corp, "AP21").equals("含税价格优先"))/*-=notranslate=-*/
			{
				return new int[]{RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE};
			}
			else
			{
				return new int[]{RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE};
			}
		}

	}


	public void doException(String clbh,String clr,String pk_corp) throws BusinessException
	{
		DJCLBVO vo=new DJCLBVO();
		vo.setDwbm(pk_corp);
		vo.setClbh(clbh);
		vo.setClr(clr);
		new nc.bs.arap.verify.VerifyBO().onLinkPFForDisOper(vo);
		try
		{
			String[] vouchid=new ArapForGYLDMO().getDj(clbh);
			for(int i=0;i<vouchid.length;i++)
	    	{
	    		DJZBVO zbvo=new DJZBBO().findByPrimaryKey(vouchid[i]);
	    		new DJZBBO().returnBillCode(zbvo);
	    	}
		}
		catch(Exception e)
		{
			Log.getInstance(ArapForGYL.class).error(e);
			throw new BusinessException(e);
		}

	}
	public String getSkBalance(String spk_Cumandoc,String sWhere,String pk_corp,int iAgBillStat) throws BusinessException{
		try {
			return new ArapForGYLDMO().getSkBalance(spk_Cumandoc, sWhere, pk_corp, iAgBillStat);
		} catch (SystemException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		} catch (NamingException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		} catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		}
	}
}