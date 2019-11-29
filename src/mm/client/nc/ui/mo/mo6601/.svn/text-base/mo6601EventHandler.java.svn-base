package nc.ui.mo.mo6601;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.mm.scm.mm6601.IMo6601;
import nc.itf.pub.rino.IPubDMO;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.mo.mo6601.IButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.CorpVO;
import nc.vo.fp.fp401.CrossVO;
import nc.vo.mo.hgz.HgzHeadVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.mo.mo6601.GlclBillVO;
import nc.vo.mo.mo6601.GlclHeadVO;
import nc.vo.mo.mo6601.GlclItemVO;
import nc.vo.ic.pub.bill.OnhandnumItemVO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import java.util.regex.*;

import org.apache.commons.lang.StringUtils;

public class mo6601EventHandler extends ManageEventHandler
{

	//下线隔离参照
	private UIRefPane refXx ;
	//库内隔离参照
	private UIRefPane refGl ;
	
	public mo6601EventHandler(BillManageUI billUI, IControllerBase control)
	{
		super(billUI, control);
		// TODO Auto-generated constructor stub
		getRefXx();
		getRefGl();
	}

	private UIRefPane getRefXx()
	{
		if(refXx == null)
		{
			refXx = new UIRefPane(this.getBillUI()); 
			refXx.setIsCustomDefined(true); 
			refXx.setRefModel(new RefGlModel("下线隔离单参照Downline isolated single reference")); 
			refXx.getRefUIConfig().setMutilSelected(true);  
			refXx.getRef().setRefUIConfig(refXx.getRefUIConfig());
		}
		return refXx;
	}
	 
	@Override
	protected void onBoQuery() throws Exception
	{

		//StringBuffer strWhere = new StringBuffer();

/*		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		// strWhere.append(" and (ebank_managebalance.pk_corp='").append(getBillUI().getEnvironment().getCorporation().getPk_corp());
		// strWhere.append("')");
		strWhere.append("  and pk_corp='" ).append(getUnitPK()).append("'");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
*/
		StringBuffer qryWhere = new StringBuffer();
	    StringBuffer strWhere = new StringBuffer();
		strWhere.append(" PK_GLCL in( select mm_glcl.PK_GLCL  from mm_glcl ");//add LGY
		if (askForQueryCondition(qryWhere) == false)
			
			return;// 用户放弃了查询
		//add LGY
	if(qryWhere.indexOf("mm_glcl_b")>0)
	{
		strWhere.append(" inner join mm_glcl_b on mm_glcl_b.PK_GLCL=mm_glcl.PK_GLCL  where ");
	}
	else 
	{	
		strWhere.append(" where 1=1 and ");
	}
	//add LGY
		strWhere.append(qryWhere.toString()+")");//add LGY
		
		SuperVO[] queryVos =HYPubBO_Client.queryByCondition(GlclHeadVO.class, strWhere.toString());//queryHeadVOs(strWhere.toString());
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);
		//getButtonManager().setButtonByOperate(0);
		
		updateBuffer();
		

	}
	
	private UIRefPane getRefGl()
	{
		if(refGl == null)
		{
			refGl = new UIRefPane(this.getBillUI()); 
			refGl.setIsCustomDefined(true); 
			refGl.setRefModel(new RefGlModel("库内隔离单参照Storehouse isolated single reference")); 
			refGl.getRefUIConfig().setMutilSelected(true);  
			refGl.getRef().setRefUIConfig(refGl.getRefUIConfig());
		}
		return refGl;
	}
	protected void buttonActionAfter(AbstractBillUI abstractbillui, int intBtn)
			throws Exception
	{
		switch (intBtn)
		{
			case IBillButton.Add:
				initialAdd();
				break;
			default:
		}
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception
	{
		switch (intBtn)
		{
			case IButton.Ref_XiaXian:  //下线检验单参照
				onBoXiaXianRef(); 
				break;
			case IButton.Ref_KuNei: //库内隔离单参照
				onBoKuNeiRef();
				break;
			case IButton.Xxgljc: //下线隔离处理
				onXxGlJc();
				break;
			case IButton.FGCL: //返工处理
				onFgcl();
				break;
			case IButton.Print:
				onBoPrint();
				break;
			case IButton.Delete:
			   onBoDelete();
			   break;
			default:
				break;

		}
	}
	//下线隔离解除
	protected void onXxGlJc() throws Exception
	{
		int[] rows = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if(rows==null||rows.length==0)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "请选择需要处理的表体行！Please select the desired processing table body row!");
			return ;
		}
		GlclBillVO billVO = (GlclBillVO) this.getBufferData().getCurrentVO();
		GlclItemVO[] items = billVO.getChildrenVO();
		for(int row:rows)
		{
//			this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
//			if(row<0)
//			{
//				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "请选择需要处理的表体行！");
//				return ;
//			}
			String clzt = String.valueOf(this.getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, "clzt"));
			if(clzt!=null&&clzt.trim().equals(1))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",Transformations.getLstrFromMuiStr("行@Line &"+(row+1)+"& 已经下线隔离解除！@is lifted off the assembly line has been isolated!"));
				return;
			}
			if(clzt!=null&&clzt.trim().equals(2))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", Transformations.getLstrFromMuiStr("行@Line & "+(row+1)+"& 已经返工处理！@has been reworked to handle!"));
				return;
			}
			
			if(items[0].getLydjlx()==null||!items[0].getLydjlx().equalsIgnoreCase("JYDJ"))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "行@Line & "+(row+1)+"& 来源单据类型不是下线检验单，不需要进行下线隔离处理！@Sources document type is not off the assembly line a single test, does not require a downline isolation processing!");
				return;
			}
//			if(items[0].getLydjlx()==null||!items[0].getLydjlx().equalsIgnoreCase("JYDJ"))
//			{
//				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "行@Line & "+(row+1)+"& 来源单据类型不是下线检验单，不需要进行下线隔离处理！");
//				return;
//			}
		}
		IMo6601 imp = (IMo6601) NCLocator.getInstance().lookup(IMo6601.class.getName());
		try
		{
			imp.doXiaXianGl(billVO,rows);
			MessageDialog.showErrorDlg(this.getBillUI(),"提示Prompt","解除成功！Lifting of success!");
		}
		catch(Exception e)
		{
			MessageDialog.showErrorDlg(this.getBillUI(),"错误Error","解除失败Lift failure:"+e.getStackTrace().toString()+","+e.getMessage());
		}
		this.onBoRefresh();
		
		
	}
	//返工处理
	protected void onFgcl() throws Exception
	{
		int[] rows = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if(rows==null||rows.length==0)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "请选择需要处理的表体行！Please select the desired processing table body row!");
			return ;
		}
		
		for(int row:rows)
		{
//			int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			
			String clzt = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, "clzt")+"";
			if(clzt!=null&&clzt.trim().equals("1"))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "行 @Line &"+(row+1)+" 已经下线隔离解除！@has been off the assembly line isolation lifted");
				return;
			}
			if(clzt!=null&&clzt.trim().equals("2"))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "行 @Line &"+(row+1)+" 已经返工处理！has been reworked!");
				return;
			}
		}
		try
		{
			IMo6601 imp = (IMo6601) NCLocator.getInstance().lookup(IMo6601.class.getName());
		    GlclBillVO billVO = (GlclBillVO) this.getBufferData().getCurrentVO();
		    billVO.getParentVO().setLogindate(ClientEnvironment.getInstance().getDate());
		    imp.doFanGong(billVO,rows);
		    this.onBoRefresh();
		    MessageDialog.showErrorDlg(this.getBillUI(),"提示Prompt","返工处理成功！Rework process is successful!");
		    //getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
		//	setSaveOperateState(2);
		}
		 catch(Exception e)
		 {
			 MessageDialog.showErrorDlg(this.getBillUI(),"错误Error",e.getMessage());
		 }
		
	}
	
	private String getUnitPK()
	{
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}
	@Override
	protected void onBoEdit() throws Exception
	{
		if (((BillManageUI) getBillUI()).isListPanelSelected())
		{
			((BillManageUI) getBillUI()).setCurrentPanel("CARDPANEL");
			getBufferData().updateView();
		}
		if (getBufferData().getCurrentVO() == null)
			return;
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		getBillUI().setBillOperate(0);
	}
	
	protected boolean checkVO(GlclBillVO billVO) throws Exception
	{ 
		GlclHeadVO head = billVO.getParentVO();
		GlclItemVO[] items = billVO.getChildrenVO();
		if(items==null||items.length==0)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "表体没有数据！The table body not data!");
			return false;
		}
		if(StringUtils.isEmpty(head.getBz()))
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "班组不能为空！Team can not be empty!");
			return false;
		}
		if(StringUtils.isEmpty(head.getScx()))
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "生产线不能为空！The production line can not be empty!");
			return false;
		}
		if((head.getZdsl()==null||head.getZdsl()==0)&&items[0].getLydjlx()=="JYDJ")  //来源为下线隔离单的才检验
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "整垛数量不能为空！The whole crib quantity can not be empty!");
			return false;
		}
		if(StringUtils.isEmpty(head.getClr()))
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "处理人不能为空！Processing can not be empty!");
			return false;
		} 
		
		List<String> tempDh = new ArrayList<String>();
		int i=0;
		StringBuffer errdh=new StringBuffer();
		for (GlclItemVO temp : items)
		{
//			if(temp.getHcldh()==null||temp.getHcldh().trim().equals(""))
//			{
//				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
//				"垛号不能为空！");
//				
//				setColumn(i, "dh");
//				
//				return false;
//			}
		
			if(temp.getHcldh()!=null&&!temp.getHcldh().trim().equals(""))
			{
				if(tempDh.contains(temp.getHcldh()))
				{
					MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
							Transformations.getLstrFromMuiStr(new StringBuilder("垛号@Stack number&[").append(temp.getHcldh().trim()).append("]&重复！@repeats!").toString()));
					
					setColumn(i, "hcldh");
					
					return false;
				}
				else if(!chekDh(temp.getHcldh()))
				{
					 errdh.append(temp.getHcldh());
				}
			
				tempDh.add(temp.getHcldh());
			}
			
			i++;
		}
		//add by zwx 2019年9月16日 制盖去除校验
		String corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		if(corp.equals("1078")||corp.equals("1108")){
			
		}else{
			if(errdh.length()>0)
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",Transformations.getLstrFromMuiStr("出现不符合要求的垛号@Does not meet the requirements crib No.&："+errdh.toString()));
				return false;
			}
			i=0;
			for (GlclItemVO temp : items)
			{
				if(temp.getHcldh()!=null&&!temp.getHcldh().trim().equals(""))
				{
					//校验垛号不能重复
					//String sql = new StringBuilder("select vfree1 from ic_onhandnum where vfree1 ='").append(temp.getHcldh().trim()).append("'").toString();
					if(temp.getStatus()==VOStatus.UNCHANGED||temp.getStatus()==VOStatus.DELETED)//如果没改变或者删除就不处理  add by LGY
					{
						continue;
					}
					
					StringBuilder sql = new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getHcldh().trim()+"' ");
					if(temp.getStatus()==VOStatus.NEW)
					{
						 sql.append(" union all select hcldh from mm_glcl_b where nvl(dr,0)=0 and hcldh ='").append(temp.getHcldh().trim()).append("'");	
					}
					else
						{
						 sql.append(" union all select hcldh from mm_glcl_b where nvl(dr,0)=0 and hcldh ='").append(temp.getHcldh().trim()).append("'").append(" and pk_glcl_b!='"+temp.getPrimaryKey()+"' ");
						}

					
					List voList = null;
					try {
						IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						voList = (List)uAPQueryBS.executeQuery(sql.toString(), new ArrayListProcessor());
					} catch (Exception e1) {
						MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",e1.getMessage());
						return false;
					}
					if(voList!=null&&voList.size()>0)
					{
						MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
								Transformations.getLstrFromMuiStr(new StringBuilder("垛号@Stack number&[").append(temp.getHcldh().trim()).append("]&重复！@repeats!").toString()));
						
						setColumn(i, "hcldh");
						return false;
					}		
				}
				
				i++;
				
			}
		}
		
		
		
//		for(GlclItemVO item : items)
//		{
//			if(item.getHclhgsl()==null||item.getHclhgsl()==0)
//			{
//				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "后处理合格数量不能为空！");
//				return false;
//			}
//		}
		
		return true;
	}
	@Override
	protected void onBoSave() throws Exception
	{
		GlclBillVO billVO = null;
		try
		{  
			billVO = (GlclBillVO) this.getBillCardPanelWrapper()
			.getBillVOFromUI();
			GlclItemVO[] vos = (GlclItemVO[]) billVO.getChildrenVO();
			GlclHeadVO head = (GlclHeadVO) billVO.getParentVO();
			String corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			for(int i=0;i<vos.length;i++)
			{
				//add by zwx 2019年9月16日 增加仓库编码转换主键存储
				if(corp.equals("1078")||corp.equals("1108")){
					String ckcode = vos[i].getCk()==null?"":vos[i].getCk().toString();
					if(ckcode.length()>0){
						String ckid = getCkid(ckcode);
						vos[i].setCk(ckid);
					}
				}
				//end by zwx
				
				if(vos[i].getClzt()==null||vos[i].getClzt()==0)
				{
					continue;
				}
				else if(vos[i].getStatus()==VOStatus.UPDATED)
				{
					MessageDialog.showErrorDlg(getBillUI(), "错误Error", Transformations.getLstrFromMuiStr("行@Line&"+String.valueOf(i)+"&已经处理过，不能在修改，请确认！@has been treated, and can not be modified, make sure that!"));
					return ;
				}
				
			}
			//生成批次号
			//edit by zwx 2019年9月16日 增加公司校验制盖的不用批号生成
			if(corp.equals("1078")||corp.equals("1108")){
				
			}else{
				if(!generatePh())
				{
					return ;
				}
			}
			

			if (vos == null)
			{
				return ;
			}
			if (head.getPk_corp() == null
					|| head.getPk_corp().trim().equals("")) // 设置当前公司
			{
				head.setPk_corp(getUnitPK());
			}
			if (head.getLrr() == null || head.getLrr().trim().equals("")) // 设置制单人
			{
				head.setLrr(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
			} 
			if (!checkVO(billVO))
			{
				return;
			}  
			 
			for(int i=0;i<billVO.getChildrenVO().length;i++)
			{
				if((billVO.getChildrenVO()[i]).getStatus()==VOStatus.NEW)
				{
					(billVO.getChildrenVO()[i]).setAttributeValue("pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
				}
			}
			billVO = (GlclBillVO) HYPubBO_Client.saveBill(billVO);
			/**
			 * add by zwx 2019-9-23 隔离处理单回写【单据类型=在线隔离】单据信息
			 * “处理良品尾数数量”根据隔离处理单上的“后处理合格数量”反写（后处理合格数量为空就反写为0），
			 * “处理日期”根据隔离处理单上的“单据日期”；
			 * “报废数量”=“数量”-“处理良品尾数数量”；
			 */
			writeBackToZxgl(billVO);
			
			//end by zwx 
			// 将VO添加到缓存
			addVoToBuffer(billVO);
			this.updateBuffer();
			this.getBufferData().setCurrentVO(billVO);
			// 设置界面状态
			setSaveOperateState();
			// 刷新界面
			onBoRefresh();
 
		} catch (Exception e)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "保存失败Failed to save："
					+ e.getMessage());
			e.printStackTrace();
		} 
	}
	
	protected void addVoToBuffer(GlclBillVO billVO) throws Exception
	{
		CircularlyAccessibleValueObject[] cirs = this.getBufferData()
				.getAllHeadVOsFromBuffer();
		if (cirs == null || cirs.length == 0)
		{
			this.getBufferData().addVOToBuffer(billVO);
			return;
		}
		for (int i = 0; i < cirs.length; i++)
		{
			if (cirs[i].getPrimaryKey().equals(
					billVO.getParentVO().getPrimaryKey()))
			{
				return;
			}
		}
		this.getBufferData().addVOToBuffer(billVO);
	}

	//检验当前单据是否是下线隔离单
	protected boolean checkKuNeiGeLi()
	{
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		String temp ;
		if(model.getRowCount()>0)
		{
			temp = (String) model.getValueAt(0, "lydjlx");
			if(temp!=null&&!temp.equals("isolation"))
			{
				MessageDialog.showErrorDlg(getBillUI(),"错误Error",  "当前单据不是来源于库内隔离单，请先处理！This document is not derived from the database isolation of single, please!");
				return false;
			}
		}
		return true;
	}
	
	//检验当前单据是否是下线隔离单
	protected boolean checkXiaXian()
	{
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		String temp ;
		if(model.getRowCount()>0)
		{
			temp = (String) model.getValueAt(0, "lydjlx");
			if(temp!=null&&!temp.equals("JYDJ"))
			{
				MessageDialog.showErrorDlg(getBillUI(), "错误Error", "当前单据不是来源于下线隔离单，请先处理！Current single documents not derived from the downline isolation, please!");
				return false;
			}
		}
		return true;
	}
	
	protected void onBoKuNeiRef() throws Exception
	{
		if(!checkKuNeiGeLi())
		{
			return;
		}
		
		getRefGl().getRef().showModal();
		
		//检验是否属于同一下存货
		if(!checkOneProduct((Object[]) getRefGl().getRefValues("b.lh")))
		{
			return ;
		}
		
		//检验是否重复选择了隔离数据
		if(!checkRepeat((Object[]) getRefGl().getRefValues("b.pk_glzb_b")))
		{
			return ;
		}
		addKuNeiToLine(getRefGl().getRefModel().getVOs());
	}
	@SuppressWarnings({ "serial", "rawtypes" })
	protected void onBoXiaXianRef() throws Exception
	{
		
		if(!checkXiaXian())
		{
			return;
		}
		
		getRefXx().getRef().showModal();
		
		//检验是否属于同一下线隔离单
		if(!checkOneProduct((Object[]) getRefXx().getRefValues("b.lh")))
		{
			return ;
		}
		
		//检验是否重复选择了隔离数据
		if(!checkRepeat((Object[]) getRefXx().getRefValues("b.pk_glzb_b")))
		{
			return ;
		}
		addXiaXianToLine(getRefXx().getRefModel().getVOs());
	}
	
	private boolean checkOneProduct(Object[] pkGlzbs)
	{
		if(pkGlzbs==null)
		{
			return false;
		}
		
		for(int i=0;i<pkGlzbs.length;i++)
		{
			if(!pkGlzbs[i].equals(pkGlzbs[0]))
			{
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "所选的数据存货不一致！Selected inventory data are inconsistent!");
				return false;
			} 
		}
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		//BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		String tPk= String.valueOf(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lh"));
		if(table.getRowCount()>0)
		{
		//	tPk = (String) model.getValueAt(0, "lh");
			if(!tPk.equals(pkGlzbs[0]))
			{
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "选择的数据的存货与表体数据的存货不一致！The inconsistency Availability of the inventory table body data of the selected data!");
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkRepeat(Object[] pkValue) throws Exception
	{
		if(pkValue==null||pkValue.length==0)
		{
			return false;
		}
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int rows = table.getRowCount();
		String tPk ;
		StringBuffer sqlWhere = new StringBuffer(" pk_glzb_b in('"); 
		for(int j=0;j<pkValue.length;j++)
		{
			for(int i=0;i<rows;i++)
			{
				tPk = (String) model.getValueAt(i, "pk_glzb_b");
				if(pkValue[j].equals(tPk)){
					MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "选择的数据已经存在于表体！The selection of the data already exists in the meter body!");
					return false;
				}
			}
			sqlWhere.append(pkValue[j]).append("','");
		}
		sqlWhere.delete(sqlWhere.length()-2, sqlWhere.length());
		sqlWhere.append(" ) and nvl(dr,0) = 0");//add by zwx 2015-11-13 
		Object obj = HYPubBO_Client.findColValue("mm_glcl_b", "pk_glzb_b", sqlWhere.toString());
		if(obj!=null)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "选择的数据已经被以往单据使用！The selected data have been previous documents use!");
			return false;
		}
		return true;
	}
	//生成批次号
	protected boolean generatePh()
	{
		StringBuffer ph = new StringBuffer("");
		CorpVO curCorp = ClientEnvironment.getInstance().getCorporation();
		if(curCorp.getDef6()==null||curCorp.getDef6().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，当前登录公司的的批号代码不能为空！Unable to generate batch number, batch number code of the currently logged on the company can not be empty!");
			return false;
		}
		ph.append(curCorp.getDef6());
		String phrq=String.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("phrq").getValueObject());
		if(phrq==null||phrq.equals("")||phrq.equalsIgnoreCase("null"))
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，批次号日期不能为空！Can not generate the batch number, team can not be empty!");
			return false;
		}
		
		UFDate date =new UFDate(phrq);
		ph.append(date.toString().substring(2, 4));
		byte[] t = new byte[]{64};
		t[0] += date.getMonth();
		ph.append(new String(t));
		ph.append(date.toString().substring(8, 10));
		UIRefPane pane = (UIRefPane)this.getHeadItem("bz").getComponent();
		String bzmc = (String)pane.getRefValue("bzmc");
		if(bzmc==null||bzmc.trim().equals("")){
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，班组不能为空！Can not generate the batch number, team can not be empty!");
			return false;
		}else if(bzmc.indexOf('(')<0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，班组名不符合生成批次号的规则，应含有'(X',X表示字母！Unable to generate a batch number, team name does not comply with the rules that generate batch number should contain '(X', X represents the letter!");
			return false;
		}
		int bzIndex = bzmc.indexOf("(");
		ph.append(bzmc.substring(bzIndex+1, bzIndex + 2));
		
		pane = (UIRefPane)this.getHeadItem("scx").getComponent();
		String scxmc = (String)pane.getRefValue("gzzxmc");
		if(scxmc==null||scxmc.trim().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "生产线不能为空！The production line can not be empty!");
			return false;
		}
		ph.append(scxmc.contains("钢") ? "1":scxmc.contains("铝2") ? "3":"2");	
		
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
		.getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
		.getBillModel(); 
		for(int i=0;i<table.getRowCount();i++)
		{ 
				model.setValueAt(ph.toString(), i, "hclph"); 
				model.setRowState(i, 2);
		}  
		return true;
	}
	
	
	
	protected BillItem getHeadItem(String key)
	{
		return this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				key);
	}
	
	@SuppressWarnings("rawtypes")
	private void addKuNeiToLine(ValueObject[] objs) throws Exception
	{
		if(objs==null||objs.length==0)
		{
			return;
		}
		CrossVO vo = (CrossVO) objs[0];  
		String dh = (String) vo.getAttributeValue("dh");
		Object zdsl = HYPubBO_Client.findColValue("mm_glzb_b b,mm_glzb a", "to_char(a.zdsl)", " a.pk_glzb=b.pk_glzb and a.zdsl is not null and b.dh='"+dh+"'");
		setHeadItemValue("zdsl",zdsl);
		
		Object cinventoryid=HYPubBO_Client.findColValue("bd_invmandoc a,bd_invbasdoc b","a.pk_invmandoc","a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and a.dr=0 and b.dr=0 and b.invcode='"+String.valueOf(vo.getAttributeValue("lh"))+"'");
		setHeadItemValue("cinventoryid",cinventoryid);
		setHeadItemValue("lh",vo.getAttributeValue("lh"));
		setHeadItemValue("cpmc",vo.getAttributeValue("cp"));
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int row = -1;
		for(int i=0;i<objs.length;i++)
		{
			vo = (CrossVO) objs[i];
			this.getBillCardPanelWrapper().addLine();
			row = table.getSelectedRow();
			model.setValueAt(Transformations.getLstrFromMuiStr("库内隔离单","Bank isolated single"), row, "lydj");//来源单据
			model.setValueAt("isolation", row, "lydjlx");  //来源单据类型
			model.setValueAt(vo.getAttributeValue("pk_glzb"), row, "pk_glzb");//隔离单主表主键
			model.setValueAt(vo.getAttributeValue("pk_glzb_b"), row, "pk_glzb_b");// 隔离单子表主键
			model.setValueAt(vo.getAttributeValue("dh"), row, "ysdh");//原始垛号
			model.setValueAt(vo.getAttributeValue("xxaglsl"), row, "dclsl");//待处理数量
			model.setValueAt(vo.getAttributeValue("dh"), row, "cldh");//处理垛号
			model.setValueAt(vo.getAttributeValue("ph"), row, "clph");//处理批号
			model.setValueAt(vo.getAttributeValue("ph"), row, "ysph");	//原始批号
			model.setValueAt(vo.getAttributeValue("isolationckid"), row, "ck");//仓库
			Object hw=HYPubBO_Client.findColValue("bd_cargdoc","to_char(cscode)","pk_cargdoc='"+String.valueOf(vo.getAttributeValue("hw"))+"'");
			model.setValueAt(hw, row, "hw");//货位
			model.setValueAt(zdsl, row, "hclhgsl");//后处理合格数量	
			model.setValueAt(vo.getAttributeValue("hw"), row, "cspaceid");
			model.setValueAt(0,row,"clzt");
			this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(row, "ck");
			//add by zwx 2019年9月16日 制盖增加默认带出字段功能 原始批号、后处理、处理三个一致
			String corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			if(corp.equals("1078")||corp.equals("1108")){
				model.setValueAt(vo.getAttributeValue("ph"), row, "ysph");	//原始批号
				model.setValueAt(vo.getAttributeValue("ph"), row, "clph");//处理批号
				model.setValueAt(vo.getAttributeValue("ph"), row, "hclph");//后处理批号
				model.setValueAt(vo.getAttributeValue("dh"), row, "hcldh");//后处理垛号
			}
			//end by zwx
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void addXiaXianToLine(ValueObject[] objs) throws Exception
	{
		if(objs==null||objs.length==0)
		{
			return;
		}
		CrossVO vo = (CrossVO) objs[0]; 
		setHeadItemValue("zdsl",vo.getAttributeValue("zdsl"));
		setHeadItemValue("lh",vo.getAttributeValue("lh"));
		Object cinventoryid=null;
		if(vo.getAttributeValue("cinventoryid")==null||vo.getAttributeValue("cinventoryid").equals("")||vo.getAttributeValue("cinventoryid").toString().equalsIgnoreCase("null"))
		{
			 cinventoryid=HYPubBO_Client.findColValue("bd_invmandoc a,bd_invbasdoc b","a.pk_invmandoc","a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and a.dr=0 and b.dr=0 and b.invcode='"+String.valueOf(vo.getAttributeValue("lh"))+"'");
		}
		else 
		{
			cinventoryid=vo.getAttributeValue("cinventoryid");
		}
		setHeadItemValue("cinventoryid",cinventoryid);
		setHeadItemValue("cpmc",vo.getAttributeValue("cp"));
		//add by zwx 2019-9-29 制盖参照下线检验单增加自动带出班组、生产线
		String corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		if(corp.equals("1078")||corp.equals("1108")){
			setHeadItemValue("scx",vo.getAttributeValue("scx"));
			setHeadItemValue("bz",vo.getAttributeValue("bz"));
		}
		//end by zwx
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int row = -1;
		for(int i=0;i<objs.length;i++)
		{
			vo = (CrossVO) objs[i];
			this.getBillCardPanelWrapper().addLine();
			row = table.getSelectedRow();
			model.setValueAt(Transformations.getLstrFromMuiStr("下线隔离单","Downline isolated single"), row, "lydj");//来源单据
			model.setValueAt("JYDJ", row, "lydjlx");  //来源单据类型
			model.setValueAt(vo.getAttributeValue("pk_glzb"), row, "pk_glzb");//隔离单主表主键
			model.setValueAt(vo.getAttributeValue("pk_glzb_b"), row, "pk_glzb_b");// 隔离单子表主键
			model.setValueAt(vo.getAttributeValue("dh"), row, "ysdh");//原始垛号
			model.setValueAt(vo.getAttributeValue("xxaglsl"), row, "dclsl");//待处理数量
			model.setValueAt(vo.getAttributeValue("dh"), row, "cldh");//处理垛号
			model.setValueAt(vo.getAttributeValue("ph"), row, "clph");//处理批号
			model.setValueAt(vo.getAttributeValue("ph"), row, "ysph");	//原始批号
			model.setValueAt(vo.getAttributeValue("ck"), row, "ck");//仓库
			model.setValueAt(vo.getAttributeValue("hw"), row, "hw");//货位
			model.setValueAt(vo.getAttributeValue("zdsl"), row, "hclhgsl");//后处理合格数量
			model.setValueAt(vo.getAttributeValue("cspaceid"), row, "cspaceid");
			model.setValueAt(0,row,"clzt");
			this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(row, "ck");
			//add by zwx 2019年9月16日 制盖增加默认带出字段功能 原始批号、后处理、处理三个一致
//			String corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			if(corp.equals("1078")||corp.equals("1108")){
				model.setValueAt(vo.getAttributeValue("ph"), row, "ysph");	//原始批号
				model.setValueAt(vo.getAttributeValue("ph"), row, "clph");//处理批号
				model.setValueAt(vo.getAttributeValue("ph"), row, "hclph");//后处理批号
				model.setValueAt(vo.getAttributeValue("dh"), row, "hcldh");//后处理垛号
			}
			//end by zwx
		}
	}
	
	

	protected void initialAdd()
	{
		setHeadItemValue("clrq", ClientEnvironment.getServerTime().getDate());
		setHeadItemValue("phrq", getPihaoDate());
	}

	/**
	 * 获取服务器时间
	 * 
	 * @return
	 */
	protected UFDate getPihaoDate()
	{
		UFDateTime date = ClientEnvironment.getServerTime();
		if (date.getTime().compareTo("08:30") < 0)
		{
			return ClientEnvironment.getServerTime().getDateBefore(1).getDate();
		} else
		{
			return ClientEnvironment.getServerTime().getDate();
		}
	}

	protected void setHeadItemValue(String key, Object value)
	{
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(key)
				.setValue(value);
	}
	
    //设置输入焦点
	private void setColumn(int row, String col)
	{
		UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		//BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		
		String colName = this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem(col).getName();
		
		int colIndex = table.getColumnModel().getColumnIndex(colName);
		
		table.setRowSelectionInterval(row, row);
		table.setColumnSelectionInterval(colIndex, colIndex);
		table.editCellAt(row, colIndex);
		table.setEditingRow(row);
		table.setEditingColumn(colIndex);
	}
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)throws Exception
	{
		if(sqlWhereBuf == null)
        throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf cann't be null");
        UIDialog querydialog = getQueryUI();
         if(querydialog.showModal() != 1)
        return false;
    INormalQuery query = (INormalQuery)querydialog;
    String strWhere = query.getWhereSql();
    if(strWhere == null)
        strWhere = "1=1";
    if(getButtonManager().getButton(2) != null)
        if(getBillIsUseBusiCode().booleanValue())
            strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and ").append(getBillField().getField_BusiCode()).append("='").append(getBillUI().getBusicode()).append("'").toString();
        else
            strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and ").append(getBillField().getField_Busitype()).append("='").append(getBillUI().getBusinessType()).append("'").toString();
    strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and (nvl(mm_glcl.dr,0)=0)").toString();
     if(getHeadCondition() != null)
        strWhere = (new StringBuilder()).append(strWhere).append(" and ").append("mm_glcl."+getHeadCondition()).toString();
     sqlWhereBuf.append(strWhere);
      return true;
	}
  private boolean chekDh(String dh)
  {
	 if(dh==null||dh.equals("")||dh.equalsIgnoreCase("null"))
	 {
		 return  false;
	 }
	 else if( dh.length()!=10)
	 {
		 return false;
	 }
	 else 
	 {
		 Pattern pattern = Pattern.compile("\\d{10}");
		 Matcher matcher = pattern.matcher(dh);
		return  matcher.matches();
	 }
  }
  protected void onBoDelete()
  {
	 
	try
	{
		 AggregatedValueObject billVO = this.getBufferData().getCurrentVO();
	
		HYPubBO_Client.deleteBill(billVO);
		this.getBufferData().removeCurrentRow();

		getBillUI().removeListHeadData(getBufferData().getCurrentRow());
		getBufferData().removeCurrentRow();
		if (getBufferData().getVOBufferSize() == 0)
		{
			getBillUI().setBillOperate(4);
		}else
		{
			getBillUI().setBillOperate(2); //IBillOperate.OP_NOTEDIT
		}
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	} catch (Exception e)
	{
		// TODO Auto-generated catch block
		Logger.error(e);
		e.printStackTrace();
	}
  }
  
  	//add by zwx 2019年9月16日 根据仓库编码获得主键
	private String getCkid(String code)
	{
		Object ck = null;
		try
		{
			ck =  HYPubBO_Client.findColValue("bd_stordoc", "pk_stordoc", " storcode = '"+code+"' and pk_corp='"+getUnitPK()+"'");
		} catch (UifException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ck==null?"":ck.toString();
	}
	
	/**
	 * add by zwx 2019-9-23 隔离处理单回写【单据类型=在线隔离】单据信息
	 * “处理良品尾数数量”根据隔离处理单上的“后处理合格数量”反写（后处理合格数量为空就反写为0），
	 * “处理日期”根据隔离处理单上的“单据日期”；
	 * “报废数量”=【在线隔离单】“数量”-“处理良品尾数数量”；
	 */
	private void writeBackToZxgl(GlclBillVO vo){
		GlclItemVO[] bvos = vo.getChildrenVO();
		String corp = vo.getParentVO().getPk_corp();
		if(bvos!=null&&bvos.length>0){
			String pk_glzb = bvos[0].getPk_glzb()==null?"":bvos[0].getPk_glzb();
			if(pk_glzb.length()>0){

				StringBuffer sql = new StringBuffer();
				sql.append(" select hgz.* from  mm_glcl_b b ") 
				.append(" inner join mm_glzb glh ") 
				.append(" on b.pk_glzb = glh.pk_glzb ") 
				.append(" inner join mm_hgz hgz ") 
				.append(" on hgz.pk_hgz = glh.note ") 
				.append(" where glh.pk_corp = '"+corp+"' ") 
				.append(" and hgz.pk_corp = '"+corp+"' ") 
				.append(" and nvl(b.dr,0) = 0 ") 
				.append(" and nvl(glh.dr,0) = 0  ") 
				.append(" and nvl(hgz.dr,0) = 0 ") 
				.append(" and b.pk_glzb = '"+pk_glzb+"' ") 
				.append(" and hgz.vdef7 = '在线隔离' ") ;
				IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
				List list = null;
				try {
					list = ipubdmo.getBeanList(sql.toString(),HgzHeadVO.class);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if(list!=null&&list.size()>0){
					HgzHeadVO hgzVo = (HgzHeadVO) list.get(0);
					Integer hclhgsl = bvos[0].getHclhgsl()==null?new Integer(0):bvos[0].getHclhgsl();
					hgzVo.setVdef9(hclhgsl.toString());
					
					hgzVo.setVdef10(vo.getParentVO().getClrq().toString());
					try {
						HYPubBO_Client.update(hgzVo);
					} catch (UifException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}
		}
	}
	
	//end by zwx
	
	
}
