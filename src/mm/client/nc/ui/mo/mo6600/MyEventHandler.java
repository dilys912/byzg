package nc.ui.mo.mo6600;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.mm.scm.mm6600.IMo6600;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wrback.state.IWriteBackStateService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.sm.login.ShowDialog;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.uif.pub.exception.UifException;
import nc.utils.modify.is.IdetermineService;
import nc.vo.bd.CorpVO;
import nc.vo.mm.bzfs.BzfsBVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.config.Account;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends ManageEventHandler
{

	public MyEventHandler(BillManageUI billUI, IControllerBase control)
	{
		super(billUI, control);
	}
	@Override
	protected void onBoQuery() throws Exception
	{
		Account a = new Account();
		String b = a.getAccountCode();
		StringBuffer qryWhere = new StringBuffer();
	    StringBuffer strWhere = new StringBuffer();
		strWhere.append("pk_glzb in( select mm_glzb.pk_glzb from mm_glzb ");//add LGY
		if (askForQueryCondition(qryWhere) == false)
			
			return;// 用户放弃了查询
		//add LGY
	if(qryWhere.indexOf("mm_glzb_b")>0)
	{
		strWhere.append(" inner join mm_glzb_b on mm_glzb_b.pk_glzb=mm_glzb.pk_glzb ");
	}
	else 
	{	
		strWhere.append(" where 1=1");
	}
	//add LGY
		strWhere.append("  and billsign='JYDJ' and  ").append(qryWhere.toString()+")");//add LGY
		
		SuperVO[] queryVos =HYPubBO_Client.queryByCondition(GlHeadVO.class, strWhere.toString());//queryHeadVOs(strWhere.toString());
     
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
//		//add by src 2018年4月11日19:04:21
//		for(int i = 0; i<queryVos.length ;i++){
//			String scddh = queryVos[i].getAttributeValue("scddh") == null?"":queryVos[i].getAttributeValue("scddh").toString();
//			getBzfs(scddh);
//		}
        }

	private String getUnitPK()
	{
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}

	protected void buttonActionAfter(AbstractBillUI abstractbillui, int intBtn)
			throws Exception
	{
		switch (intBtn)
		{
		case IBillButton.Add:
			initialAdd();
			break;
		case IBillButton.AddLine:
			initialAddLine();
		case IBillButton.InsLine:
			initialAddLine();
		default:
		} 
	}

	protected void initialAddLine()
	{
		try
		{
			UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable();
			BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel();
			int curRow = table.getSelectedRow();
			
			//表体下线/隔离数量默认等于表头整垛数量
			model.setValueAt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue(), curRow, "xxaglsl");
			model.setValueAt(getUnitPK(), curRow, "pk_corp");
			// MoHeaderVO heads[] =
			// MMProxy.getRemoteMO().queryMoByWhere(" and mm_mo.pk_moid='"+((UIRefPane)getBillItem("scddh").getComponent()).getRefPK()+"'");
			UIRefPane pane = (UIRefPane) getHeadBillItem("cp").getComponent();
			model.setValueAt(pane.getRefName(), curRow, "cp");
			model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), curRow,
					"lh"); 
			String dhName = this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dh").getName();
			int colIndex = table.getColumnModel().getColumnIndex(dhName);
			table.editCellAt(curRow, colIndex);
			 
			this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(curRow, "lh");
			// model.setValueAt(pane.getRefName(), curRow, "lhmc");
			// }
			// this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("lh");
		} catch (Exception ex)
		{
			nc.bs.logging.Logger.error(ex);
			ex.printStackTrace();
		}

	} 

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
		//add by src 2018年4月11日19:04:21
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//        if(hvo.getPk_corp().equals("1016")){
//        	String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//        	getBzfs(scddh);
//		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		//by src 2018年4月11日19:05:43
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//		if(hvo.getPk_corp().equals("1016")){
//			String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//			getBzfs(scddh);
//		}
	}
	

	/**
	 * 确认;
	 */
	protected void onConfirm() throws Exception
	{
		// 修改单据状态为已检验
		// getHeadBillItem("djzt").setValue(new Integer(1));
		GlBillVO billVO=null;
		if(this.getBillUI().getBillOperate()==2)
		{
			billVO = (GlBillVO) this.getBufferData().getCurrentVO();
		}else
		{
		  //wkf--复制onBoSave()更改为回返是否保存成功--2015-01-09--start-
		  boolean stants = onBoSave2();
		  if(stants){
			  billVO = (GlBillVO) this.getBufferData().getCurrentVO();
		  }else{
			  return;
		  }
		  //--end--
		}
		//onBoRefresh();
		if (billVO != null)
		{
			if (!checkCofirmVO(billVO))
			{
				return;
			}
			// //生成入库单
			((GlHeadVO)billVO.getParentVO()).setLogindate(ClientEnvironment.getInstance().getDate());
			IMo6600 im = (IMo6600) NCLocator.getInstance().lookup(
					IMo6600.class.getName());
			
			try
			{ 
				im.confirm(billVO);
				// 设置界面状态
				setSaveOperateState();
				this.onBoRefresh();
				// 回到列表界面
				// onBoReturn();
				//by src 2018年4月11日15:14:18
//				GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
//				if(hvo.getPk_corp().equals("1016")){
//					saveBzfs(billVO);
//					String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//					getBzfs(scddh);
//				}

				/**
				 * 更新对应合格证主键对应的入库标识
				 * @author 刘信彬
				 * 2019-08-12
				 */
				//获取相应的备注字段的值，即合格证打印的主键
				GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
				if(getUnitPK().equals("1078")||getUnitPK().equals("1108")){
					if(hvo!=null){
						String pk_glzb=hvo.getPk_glzb();
						//修改合格证打印（制盖）的入库标识
						IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
						writeback.writeBackRK(pk_glzb);
					}
				}
				
				
			} catch (Exception ex)
			{
				
				MessageDialog.showErrorDlg(getBillUI(),"错误Error",ex.getMessage());
				Logger.error(ex);
			}
		}

	}	
	
	//生成批次号
	protected String generatePh(GlHeadVO head)
	{
		StringBuffer ph = new StringBuffer("");
		CorpVO curCorp = ClientEnvironment.getInstance().getCorporation();
		if(curCorp.getDef6()==null||curCorp.getDef6().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，当前登录公司的的批号代码不能为空！Unable to generate batch number, batch number code of the currently logged on the company can not be empty!");
			return null;
		}
		
		ph.append(curCorp.getDef6());
		
		//UFDate date = ClientEnvironment.getServerTime().getDate();		
		UFDate date = head.getPhrq();
		
		ph.append(date.toString().substring(2, 4));
		byte[] t = new byte[]{64};
		t[0] += date.getMonth();
		ph.append(new String(t));
		ph.append(date.toString().substring(8, 10));
		
		
		UIRefPane pane = (UIRefPane)this.getHeadItem("bz").getComponent();
		String bzmc = (String)pane.getRefValue("bzmc");
		if(bzmc==null||bzmc.trim().equals("")){
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，班组不能为空！Can not generate the batch number, team can not be empty!");
			return null;
		}else if(bzmc.indexOf('(')<0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "无法生成批次号，班组名不符合生成批次号的规则，应含有'(X',X表示字母！Unable to generate a batch number, team name does not comply with the rules that generate batch number should contain '(X', X represents the letter!");
			return null;
		}
		//ph.append(bzmc.substring(bzmc.indexOf("("),2));
		int bzIndex = bzmc.indexOf("(");
		ph.append(bzmc.substring(bzIndex+1, bzIndex + 2));
		
		pane = (UIRefPane)this.getHeadItem("scx").getComponent();
		String scxmc = (String)pane.getRefValue("gzzxmc");
		if(scxmc==null||scxmc.trim().equals(""))
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "生产线不能为空！The production line can not be empty!");
			return null;
		}
		ph.append(scxmc.contains(Transformations.getLstrFromMuiStr("钢","Steel")) ? "1":scxmc.contains(Transformations.getLstrFromMuiStr("铝2","aluminum")) ? "3":"2");	
		
//		UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
//		.getBillTable();
//		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
//		.getBillModel(); 
//		for(int i=0;i<table.getRowCount();i++)
//		{ 
//				model.setValueAt(ph.toString(), i, "ph"); 
//		}  
		
		return ph.toString();
	}

	protected GlBillVO save()
	{
		GlBillVO billVO = null;
		try
		{
			//add by yhj 2014-03-27
//			billVO = (GlBillVO) billvoChangedOld;
//			getBufferData().clear();
//			getBufferData().addVOToBuffer(billVO);
//			getBufferData().updateView();
			//end
			billVO = (GlBillVO) this.getBillCardPanelWrapper()
					.getBillVOFromUI();
			billVO.setParentVO(null);
			billVO.setChildrenVO(null);
			getBufferData().clear();
			billVO.setParentVO(billvoChangedOld.getParentVO()); 
			billVO.setChildrenVO(billvoChangedOld.getChildrenVO()); 
			
			getBufferData().addVOToBuffer(billVO);
			getBufferData().updateView();
			GlHeadVO head = (GlHeadVO) billVO.getParentVO();		
			GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();					
			
			if (vos == null)
			{
				return null;
			}
			
//			生成批次号
			//add by zwx 2019-8-16 制盖批号通过合格证带入
			/*String ph = generatePh(head);
			if(ph == null)
			{
				return null;
			}*/
			String ph = null;
			if(!(head.getPk_corp().equals("1078")||head.getPk_corp().equals("1108"))){
				ph = generatePh(head);
			}else{
				ph = vos[0].getPh();
			}
			if(ph == null)
			{
				return null;
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
			if (head.getBillsign() == null
					|| head.getBillsign().trim().equals(""))
			{
				head.setBillsign("JYDJ");
			}
			if (!checkVO(billVO))
			{
				return null;
			}
			int currXxsl=0;
			for (GlItemBVO temp : vos) 
			{
//				 判断是否零头垛
				
				if (temp.getXxaglsl() != head.getDdsl())
				{
					temp.setSfltd(new UFBoolean(true));
				} else
				{
					temp.setSfltd(new UFBoolean(false));
				}
				if(temp.getStatus()==VOStatus.UNCHANGED)
				{
					temp.setStatus(VOStatus.UPDATED);
				}
				temp.setPh(ph);
				currXxsl+=temp.getXxaglsl();
			}
            if(!CheckNumberOfShipments(head.getScddh(),currXxsl))
            {
            	return null;
            }
			// 保存单据
			IMo6600 im = (IMo6600) NCLocator.getInstance().lookup(
					IMo6600.class.getName());
			for(int i=0;i<billVO.getChildrenVO().length;i++)
			{
				if((billVO.getChildrenVO()[i]).getStatus()==VOStatus.NEW)
				{
					(billVO.getChildrenVO()[i]).setAttributeValue("pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
				}
			}
			billVO = im.saveBill(billVO);
			// billVO = (GlBillVO) HYPubBO_Client.saveBill(billVO); // 保存单据

			// 将VO添加到缓存
			getBufferData().clear();//下线检验单保存会生成两张相同的单据--wkf---2014-11-21
			addVoToBuffer(billVO);
			this.updateBuffer();
			this.getBufferData().setCurrentVO(billVO);
			return billVO;
		} catch (Exception e)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "保存失败Failed to save："
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	protected void onBoDelete()
	{
		AggregatedValueObject billVO = this.getBufferData().getCurrentVO();
		GlHeadVO glHeadVO=(GlHeadVO)getBufferData().getCurrentVO().getParentVO();
		try
		{
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
			/**
			 * 下线检验单删除数据时，回写是否下线状态
			 */
			if(getUnitPK().equals("1078")||getUnitPK().equals("1108")){
				if(glHeadVO!=null){
					IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
					writeback.delWriteBackXX(glHeadVO.getPk_glzb());
				}
			}
			
			
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			Logger.error(e);
			e.printStackTrace();
		}

	}

	protected void addVoToBuffer(GlBillVO billVO) throws Exception
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
	
	/**
	 * 表体垛号非空验证
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean checkNULL(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos = (GlItemBVO[]) aggvo.getChildrenVO();
			if(gbvos != null && gbvos.length > 0){
				for (int i = 0; i < gbvos.length; i++) {
					GlItemBVO bvo = gbvos[i];
					String dh = bvo.getDh();
					if(dh == null){
						Integer rowno = i + 1;
						MessageDialog.showErrorDlg(getBillUI(),"警告", "表体第"+rowno+"行，垛号不能为空,请修正！");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 跺号数据格式验证，只能为数字
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean checkDH(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos =  (GlItemBVO[]) aggvo.getChildrenVO(); 
			if(gbvos != null && gbvos.length > 0){
				List errorRow = new ArrayList();
				for (int i = 0; i < gbvos.length; i++) {
					GlItemBVO bvo = gbvos[i];
					String dh = bvo.getDh();
					java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9]+(.[0-9]+)?");
					java.util.regex.Matcher match=pattern.matcher(dh);
					if(match.matches()==false){
						Integer rowno = i + 1;
						errorRow.add(rowno);
//						MessageDialog.showErrorDlg(getBillUI(), "错误", "表体跺号第"+rowno+"行出现非法字符，垛号只能为数字,请修正！");
//						return false;
					}else{
						continue;
					}
				}
				if(errorRow != null && errorRow.size() > 0){
					StringBuilder str = new StringBuilder();
					for (int i = 0; i < errorRow.size(); i++) {
						if(i == 0){
							str.append(i);
						}else if(i == errorRow.size() - 1){
							str.append(",");
							str.append(i);
						}else{
							str.append(",");
							str.append(i);
							str.append(",");
						}
					}
					MessageDialog.showErrorDlg(getBillUI(), "错误", "表体跺号第"+errorRow.toString()+"行出现非法字符，垛号只能为数字,请修正！");
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 跺号排序
	 * @author yhj 2014-03-11
	 * @throws Exception
	 */
	public boolean onBoSortNo(AggregatedValueObject aggvo) throws Exception{
		if(aggvo != null){
			GlItemBVO[] gbvos = (GlItemBVO[]) aggvo.getChildrenVO();
			if(gbvos != null){
				if(!checkNULL(aggvo)) return false;//非空校验
				if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){//add by zwx 2019-8-15 制盖不校验
					if(!checkDH(aggvo)) return false;//垛号只能为数字，校验
				}
				
				GlItemBVO tempvo = null;
				if(gbvos != null && gbvos.length > 0){
					//取得界面上的VO，清除缓存，VO重新排序，在添加到缓存中
					for (int i = 0; i < gbvos.length; i++) {
						for (int j = i + 1; j < gbvos.length; j++) {
							if(BigInteger.valueOf(Long.valueOf(gbvos[i].getDh().trim().toString())).compareTo(
									BigInteger.valueOf(Long.valueOf(gbvos[j].getDh().trim().toString())))==1){//大于edit by shikun 2014-05-28 因为垛号太大，不能用Integer
								tempvo = gbvos[i];
								gbvos[i] = gbvos[j];
								gbvos[j] = tempvo;
							}
						}
					}
				}
				getBufferData().clear();
				aggvo.setChildrenVO(gbvos);
				getBufferData().addVOToBuffer(aggvo);
				getBufferData().updateView();
				
			}
		}
		return true;
		
	}

	public boolean onBeforeSave(AggregatedValueObject aggvo)throws Exception{
		return onBoSortNo(aggvo);
	}
	/************************add by yhj 2014-03-07 START****************************/
	AggregatedValueObject billvoChangedOld = null;
	public boolean beforeOnSaveSort(){
		try{
		AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
//		billvoChangedOld = aggvo;
		if(aggvo != null){
			boolean br = onBeforeSave(aggvo);
			if(!br)
			return false;
		}
		billvoChangedOld = aggvo;
		CircularlyAccessibleValueObject[] cvos = aggvo.getChildrenVO();
		if(cvos != null && cvos.length > 0){
			List list = new ArrayList();
			for (int i = 0; i < cvos.length; i++) {
				list.add(cvos[i].getAttributeValue("dh"));
			}
			if(list != null && list.size() > 0 ){
				BigInteger a1 = BigInteger.valueOf(Long.valueOf(list.get(0).toString()));
				List rowlist = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					a1 = BigInteger.valueOf(Long.valueOf(list.get(i).toString()));
					BigInteger a2 = new BigInteger("0");
					if(i == list.size() - 1){
						 a2 = BigInteger.valueOf(Long.valueOf(list.get(i).toString())) ;
					}else{
						a2 = BigInteger.valueOf(Long.valueOf(list.get(i+1).toString())) ;
					}
					if((a2.subtract(a1)).compareTo(new BigInteger("1"))==1){//edit by shikun 转用BigInteger a2-a1>1
						Integer ds = i + 2;
						rowlist.add(ds);
						
					}
				}
				if(rowlist != null && rowlist.size() > 0){
					StringBuilder str = new StringBuilder();
					for (int j = 0; j < rowlist.size(); j++) {
						if(j == 0){
							str.append(rowlist.get(j));
						}else if(j == rowlist.size() -1 ){
							if(str.toString().endsWith(",")){
								str.append(rowlist.get(j));
							}else{
								str.append(",");
								str.append(rowlist.get(j));
							}
						}else{
							str.append(",");
							str.append(rowlist.get(j));
							str.append(",");
						}
					}
					int ids = MessageDialog.showYesNoDlg(getBillUI(), "警告", "表体第"+"["+str.toString()+"]"+"行垛号不连续，是否保存？",MessageDialog.ID_NO);
					if(ids == MessageDialog.ID_NO){
						return false;
					}
				}
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return true;
	}
	/************************add by yhj 2014-03-07 END****************************/
	@SuppressWarnings("null")
	public void onBoSave()
	{
		//校验垛号是否重复：1.校验当前单据垛号是否重复；2.校验当前单据中垛号在数据库中是否存在--wkf---2015-01-08
		//add by zwx 2019-8-15 制盖垛号不校验
		if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){
			String[] listdh  = checkDHvo();
			if(listdh != null){
				StringBuffer errors = new StringBuffer();
				for (int i = 0; i < listdh.length-1; i++) {
					errors.append("'"+listdh[i]+"'");
				}
				String errstr = listdh[listdh.length-1];
				errors.append(errstr);
				getBillUI().showErrorMessage("垛号:\n"+errors);
				return;
			}
		}
		
		//wkf---end---
		//add by zwx 2016-1-14 佛山要求除零头垛数量外的下线隔离数与整垛数量一致，否则影响产量错误
		//添加首行的下线隔离数量与默认整垛数量一致校验
		if(getUnitPK().equals("1019")){
			if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>1){
				Object xxsl = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "xxaglsl");
				int xxnum = xxsl==null?0:Integer.valueOf(xxsl.toString());
				String zdsl = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue();
				int zdnum = zdsl==null?0:Integer.valueOf(zdsl);
				if(xxnum!= zdnum){
					getBillUI().showErrorMessage("下线/隔离数量与整垛数量不符！");
					return;
				}
			}
		}
		//end by zwx 
		if(!beforeOnSaveSort()){
			return;
		}
		
		/**
		 * 1、获取下线检验单上的数量；sjnum
		 * 2、获取生产计划上的数量；jhnum
		 * 3、比较a和b的大小
		 */
		//1、获取下线检验单上的数量；sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2、获取生产计划上的数量；jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("生产订单的数量不能大于下线检验单中生产完成的数量");
		}
		//add by shikun 2014-05-26 当前界面的订单未完工数量不可小于零，即订单的数量用完不可。ddwwgsl
		UFDouble ddwwgsl= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject()==null?new UFDouble(0):
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject().toString());
		if (ddwwgsl.doubleValue()<0) {
			getBillUI().showErrorMessage("当前界面的订单未完工数量不可小于零,请修改表体的下线数量!");
			return;
		}
		//end shikun 2014-05-26
		
		try
		{   
			//保存的时候判断顶盖，托盘，垫罐纸是否为空 by src 2018年4月25日19:22:50
			if (ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey().equals("1016")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1071")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1103")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1097")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1017")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1018")
					|| ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey().equals("1019")
					|| ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey().equals("1107")) {
				GlBillVO aggvo = (GlBillVO) this.getBillCardPanelWrapper().getBillVOFromUI();
				GlItemBVO[] bvos = (GlItemBVO[]) aggvo.getChildrenVO();
				for(int i = 0; i<bvos.length ;i++){
					String dinggai = bvos[i].getDinggai();
					String tuopan = bvos[i].getTuopan();
					String dianguanzhi = bvos[i].getDianguanzhi();
					if((dinggai==null||dinggai.equals(""))||(tuopan==null||tuopan.equals(""))||(dianguanzhi==null||dianguanzhi.equals(""))){
						MessageDialog.showErrorDlg(getBillUI(), "错误Error", "表体第"+(i+1)+"行顶盖，托盘，垫罐纸存在空值");
						return;
					}
				}
			}
			GlBillVO billVO = save();
			//整垛数量 add by src 2018年4月13日22:30:26
			String zdsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValueObject().toString();
			billVO.getParentVO().setAttributeValue("zdsl", zdsl);
			if (billVO != null)
			{
				// 设置界面状态
				setSaveOperateState();
				// 刷新界面
				onBoRefresh();

				// 回到列表界面
				// onBoReturn();
			}
			//add by src 2018年4月11日16:49:08
//			GlHeadVO hvo = (GlHeadVO) billVO.getParentVO();
//			if(hvo.getPk_corp().equals("1016")){
//				GlItemBVO [] bvo = (GlItemBVO[]) billVO.getChildrenVO();
//				String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//				getBzfs(scddh);
//			}
		} catch (Exception ex)
		{
			Logger.error(ex);
			ex.printStackTrace();
		}

	}
	/*
	 * 此方法为确定方法调用，返回false和true
	 * wkf---2015-01-09
	 * start
	 * */
	@SuppressWarnings("null")
	public boolean onBoSave2()
	{
		//校验垛号是否重复：1.校验当前单据垛号是否重复；2.校验当前单据中垛号在数据库中是否存在--wkf---2015-01-08
		String[] listdh  = checkDHvo();
		if(listdh != null){
			StringBuffer errors = new StringBuffer();
			for (int i = 0; i < listdh.length-1; i++) {
				errors.append("'"+listdh[i]+"'");
			}
			String errstr = listdh[listdh.length-1];
			errors.append(errstr);
			getBillUI().showErrorMessage("垛号:\n"+errors);
			return false;
		}
		//wkf---end---
		if(!beforeOnSaveSort()){
			return false;
		}
		
		/**
		 * 1、获取下线检验单上的数量；sjnum
		 * 2、获取生产计划上的数量；jhnum
		 * 3、比较a和b的大小
		 */
		//1、获取下线检验单上的数量；sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2、获取生产计划上的数量；jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("生产订单的数量不能大于下线检验单中生产完成的数量");
		}
		//add by shikun 2014-05-26 当前界面的订单未完工数量不可小于零，即订单的数量用完不可。ddwwgsl
		UFDouble ddwwgsl= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject()==null?new UFDouble(0):
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwwgsl").getValueObject().toString());
		if (ddwwgsl.doubleValue()<0) {
			getBillUI().showErrorMessage("当前界面的订单未完工数量不可小于零,请修改表体的下线数量!");
			return false;
		}
		//end shikun 2014-05-26
		boolean saveok = false;
		try
		{

			GlBillVO billVO = save();
			if (billVO != null)
			{
				// 设置界面状态
				setSaveOperateState();
				// 刷新界面
				onBoRefresh();
				saveok = true;
				// 回到列表界面
				// onBoReturn();
			}else{
				saveok = false;
			}
		} catch (Exception ex)
		{
			Logger.error(ex);
			ex.printStackTrace();
		}
		return saveok;

	}
	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
//		//add by src 列表切换到卡片，顶盖，垫罐纸，托盘这三个虚拟字段的值自动带出来 2018年4月11日17:32:57
//		GlBillVO aggvo = (GlBillVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(GlBillVO.class.getName(), GlHeadVO.class.getName(), GlItemBVO.class.getName());
//		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
//        if(hvo.getPk_corp().equals("1016")){
//        	String scddh =hvo.getScddh()==null?"":hvo.getScddh().toString();
//        	getBzfs(scddh);
//		}
	}
	/**
	 * 编辑后事件，根据生产订单号从中间表里面取数据带到界面
	 * by src 2018年4月11日15:40:17
	 * @param scddh
	 * @throws BusinessException
	 */
	public Map getBzfs(String pk_moid) throws BusinessException{
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select dinggai,tuopan,dianguanzhi,def1 from bd_bzfs where pk_moid = '"+pk_moid+"' and pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		List list = (List) uap.executeQuery(sql, new MapListProcessor());
		Map map = null;
		if(list.size()>0){
			map = (Map) list.get(0);
			return map;
//			Object dinggai = map.get("dinggai")==null?"":map.get("dinggai").toString();
//			Object tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();
//			Object dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();
//			Object zdsl = map.get("def1");
//			setBodyItemValueDg("dinggai", dinggai);
//			setBodyItemValueTp("tuopan", tuopan);
//			setBodyItemValueDgz("dianguanzhi", dianguanzhi);
//			String zdsl1 = getHeadItem("zdsl").getValue()==null?"":getHeadItem("zdsl").getValue().toString();
//			if(zdsl1.equals("")){
//			   setHeadItemValue("zdsl",zdsl);
//			}
//		}else{
//			setBodyItemValueDg("dinggai", null);
//			setBodyItemValueTp("tuopan", null);
//			setBodyItemValueDgz("dianguanzhi", null);
		}
		return map;
	}
	
	/*//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueDg(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"木顶盖","塑料顶盖"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}
	//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueTp(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"木托盘","塑料托盘","铝托盘"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}
	//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueDgz(String key,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"普通垫罐纸","塑料垫罐纸"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, 0, key);
	}*/
	//edit by zwx 2019-4-22 修改行改为动态
	//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueDg(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"木顶盖","塑料顶盖"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueTp(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"木托盘","塑料托盘","铝托盘"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	//add by src 2018年4月13日13:33:13
	protected void setBodyItemValueDgz(String key,int row,Object value)
	{  
		this.getBillCardPanelWrapper().initBodyComboBox(key, new String []{"普通垫罐纸","塑料垫罐纸"}, false);
		this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
	}
	/**
	 * add by src 根据订单号，把物料的货位，批号，跺号存放到中间表，方便出库单带出
	 * 2018年4月11日15:29:29
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void saveBzfs(GlBillVO aggvo) throws BusinessException{
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		IVOPersistence vo = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		GlHeadVO hvo = (GlHeadVO) aggvo.getParentVO();
		String sql = "select pk_bzfs from bd_bzfs where pk_moid='"+hvo.getScddh()+"' and pk_corp = '"+hvo.getPk_corp()+"'";
		String sqlscddh = "select scddh from bd_bzfs where pk_moid='"+hvo.getScddh()+"' and pk_corp = '"+hvo.getPk_corp()+"'";
		String pk_bzfs = (String) uap.executeQuery(sql,new ColumnProcessor());
		if(pk_bzfs != null && !pk_bzfs.equals("")){
			String scddh = (String) uap.executeQuery(sqlscddh,new ColumnProcessor());
			GlItemBVO[] bvos = (GlItemBVO[]) aggvo.getChildrenVO();
			List<BzfsBVO> listvo = new ArrayList<BzfsBVO>();
			for(int i = 0 ; i<bvos.length ;i++){
				GlItemBVO bvo = bvos[i];
				BzfsBVO bzfsbvo = new BzfsBVO();
				bzfsbvo.setPk_bzfs(pk_bzfs);
				bzfsbvo.setHuowei(bvo.getHw());//货位
				bzfsbvo.setHuoweiname(bvo.getHwname());//货位名称
				bzfsbvo.setPihao(bvo.getPh());//批号
				bzfsbvo.setDuohao(bvo.getDh());//跺号
				bzfsbvo.setInvcode(bvo.getLh());//物料
				bzfsbvo.setDr(0);
				bzfsbvo.setPk_corp(hvo.getPk_corp());
				bzfsbvo.setCk(hvo.getCk());//仓库
				bzfsbvo.setDinggai(bvo.getDinggai());//顶盖
				bzfsbvo.setTuopan(bvo.getTuopan());//托盘
				bzfsbvo.setDianguanzhi(bvo.getDianguanzhi());//垫罐纸
				bzfsbvo.setScddh(scddh);//生产订单号
				listvo.add(bzfsbvo);
			}
			if(listvo.size()>0){
				vo.insertVOList(listvo);
			}
		}
	}
	
	/*
	 * 检验当前界面是否有重复垛号
	 * wkf
	 * 2015-01-08
	 * */
	@SuppressWarnings({ "null", "unused", "rawtypes" })
	private String[] checkDHvo() {
		
		int indexi = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		String[] dhs = new String[indexi];//当前单据的所有的垛号
		for (int j = 0; j < indexi; j++) {
			Object dh = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(j, "dh");
			if(dh != null){
				dhs[j]=dh.toString();
			}
		}
		StringBuffer errors =  new StringBuffer();//当前单据据有重复的垛号
		for(int i = 0;i<dhs.length-1;i++){ 
			for(int j = i+1;j<dhs.length;j++){
				if(dhs[i] == dhs[j]){
					errors.append(dhs[i]+",");
				}
			}
		}
		if(errors.length()>0){
			String[] errs= errors.toString().split(",");
			int overii = errs.length;
			String[] errss = new String[overii+1];
			for (int i = 0; i < errs.length; i++) {
				errss[i]=errs[i];
			}
			String errorstr2 = "重复，请更新后再试！";
			errss[overii] = errorstr2;
			return errss;
		}else{
			String pk_corp = getUnitPK();
			StringBuffer sqlwhere = new StringBuffer();
			for (int i = 0; i < dhs.length; i++) {
				sqlwhere.append("'"+dhs[i]+"'");
				if(i<dhs.length-1){
					sqlwhere.append(",");
				}
			}
			Object pk_glzb = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_glzb").getValueObject();
//			String ddsql = "select dh from mm_glzb_b where nvl(dr,0)=0 and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) and  pk_corp = '"+pk_corp+"' and dh in ("+sqlwhere.toString()+")";
			StringBuffer ddsql = new StringBuffer();
			ddsql.append(" select dh ") 
			.append("   from mm_glzb_b ") 
			.append("  where nvl(dr, 0) = 0 ") 
			.append("    and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) ") 
			.append("    and pk_corp = '"+pk_corp+"' ") 
			.append("    and dh in ("+sqlwhere.toString()+") ") 
			.append(" union all ") 
			.append(" select hcldh dh ") 
			.append("   from mm_glcl_b ") 
			.append("  where nvl(dr, 0) = 0 ") 
			.append("    and (pk_glzb != '"+pk_glzb+"' or '"+pk_glzb+"' is null) ") 
			.append("    and pk_corp = '"+pk_corp+"' ") 
			.append("    and hcldh in ("+sqlwhere.toString()+") ") ;

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(ddsql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer error2 = new StringBuffer();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map addrMap = (Map) list.get(i);
					String dh = addrMap.get("dh") == null ? "" : addrMap.get("dh").toString();
					error2.append(dh+",");
				}
			}
//			String[] error = new String[list.size()+1];
//				error[overi] = errorstr1;
			
			if(error2.length()>0){
				String[] errs= error2.toString().split(",");
				int overii = errs.length;
				String[] errss = new String[overii+1];
				for (int i = 0; i < errs.length; i++) {
					errss[i]=errs[i];
				}
				String errorstr1 = "与系统中已有垛号重复，请更新后再试！";
				errss[overii] = errorstr1;
				return errss;
			}else{
				return null;
			}
		}
	}
	
	
	/**
	 * add by shikun 2014-03-28 
	 * 删行时：汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
	 * */
	@Override
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
		bodyToHeadNum();
	}

	/**
	 * add by shikun 2014-03-28 行操作时汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
	 * */
	public void bodyToHeadNum() {
		int ddsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject()==null?0
                :Integer.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int ddwgsl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject()==null?0
		                  :Integer.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		int ddwwgsl = ddsl-ddwgsl;
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for (int i = 0; i < row; i++) {
			Object oxxaglsl = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "xxaglsl");
			int xxaglsl = oxxaglsl==null?0:Integer.valueOf(oxxaglsl.toString());
			ddwwgsl = ddwwgsl-xxaglsl;
		}
		if (ddwwgsl<0) {
			getBillUI().showWarningMessage("已超出订单数量，请修改表体行“下线/隔离数量”。");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ddwwgsl", ddwwgsl);
	}

	
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		//add by src 如果表头没选生产订单，则提示请选择生产订单
		String scddh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue().toString();
		if(scddh == null||scddh.equals("")){
			getBillUI().showErrorMessage("请选择生产订单");
			return;
		}
		super.onBoLineAdd();
		//增行的时候根据生产订单主键去中间表联查CVM传过来的包装物信息 add by src 2018年4月16日10:00:56
		int num = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		if (ClientEnvironment.getInstance().getCorporation().getPk_corp()
				.equals("1016")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPk_corp().equals("1071")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPk_corp().equals("1103")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1097")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1017")
				|| ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1018")
			    || ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey().equals("1019")
				|| ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey().equals("1107")) {
			String dinggai = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "dinggai")==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "dinggai").toString();
			if(dinggai.equals("") && num == 1){
				Map map = getBzfs(scddh);
				if(map ==  null){
					setBodyItemValueDg("dinggai", num-1,"木顶盖");
					setBodyItemValueDgz("dianguanzhi",  num-1,"普通垫罐纸");
					setBodyItemValueTp("tuopan",  num-1,"木托盘");
				}else{
					String dinggai1 = map.get("dinggai")==null?"":map.get("dinggai").toString();//顶盖
					String tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();//托盘
					String dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();//垫罐纸
					setBodyItemValueDg("dinggai",  num-1,dinggai1);
					setBodyItemValueDgz("dianguanzhi",  num-1,dianguanzhi);
					setBodyItemValueTp("tuopan", num-1, tuopan);
				}
			}else{
				Map map = getBzfs(scddh);
				if(map ==  null){
					setBodyItemValueDg("dinggai", num-1, "木顶盖");
					setBodyItemValueDgz("dianguanzhi", num-1, "普通垫罐纸");
					setBodyItemValueTp("tuopan",  num-1,"木托盘");
				}else{
					String dinggai1 = map.get("dinggai")==null?"":map.get("dinggai").toString();//顶盖
					String tuopan = map.get("tuopan")==null?"":map.get("tuopan").toString();//托盘
					String dianguanzhi = map.get("dianguanzhi")==null?"":map.get("dianguanzhi").toString();//垫罐纸
					setBodyItemValueDg("dinggai", num-1, dinggai1);
					setBodyItemValueDgz("dianguanzhi", num-1, dianguanzhi);
					setBodyItemValueTp("tuopan", num-1, tuopan);
				}
			}
	     }
		//增行操作时汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
		bodyToHeadNum();
		//end shikun 
		/**
		 * 增行之后时候，校验表头的数据
		 * 1、获取下线检验单上的数量；sjnum
		 * 2、获取生产计划上的数量；jhnum
		 * 3、比较a和b的大小
		 */
		//1、获取下线检验单上的数量；sjnum 
		
		UFDouble sjnum=
				new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		//2、获取生产计划上的数量；jhnum
		UFDouble jhnum=new UFDouble();
				jhnum=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString())==null?new UFDouble():
					new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int bjnum=sjnum.compareTo(jhnum);
		if(bjnum>0){
			getBillUI().showErrorMessage("生产订单的数量不能大于下线检验单中生产完成的数量");
		}
	}
	protected boolean checkCofirmVO(GlBillVO billVO)
	{
		// GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();
		if (head.getDjzt() == 1)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "单据已处理！Documents have been processed!");
			return false;
		}
		if (head.getCk() == null)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "仓库不能为空！The warehouse not as empty!");
			return false;
		}
		if (head.getJyjg() != 0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "只有合格的单据才需要确认！Only qualified documents need to confirm!");
			return false;
		}
		if (head.getZdsl() == null || head.getZdsl() == 0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "整垛数量不能为空！The entire stack Quantity can not be empty!");
			return false;
		}
		
		return true;
	}

	protected boolean checkVO(GlBillVO billVO) throws Exception
	{ 
		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();

		if (head.getScddh() == null || head.getScddh().trim().equals(""))
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error", "生产订单号不能为空！Production order number can not be empty!");
			return false;
		}
		
		List<String> tempDh = new ArrayList<String>();
		//StringBuffer sqlWhere = new StringBuffer(" dh in('");
		StringBuffer errdh=new StringBuffer();
		int i=0;
		for (GlItemBVO temp : vos)
		{
			
			//MessageDialog.showErrorDlg(null,"下线隔离",String.valueOf(temp.hw.length()));
			
			if (head.getJyjg() == 1
					&& (temp.getGlyy() == null || temp.getGlyy().trim().equals(
							"")))
			{

				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
						"检验结果为隔离，必须输入隔离原因！The test results for the isolated isolation reasons, you must enter!");
				
				setColumn(i, "glyy");
				
				return false;
			}
			
			if(temp.getDh()==null||temp.getDh().trim().equals(""))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
				"垛号不能为空！Stack number can not be empty!");
				
				setColumn(i, "dh");
				
				return false;
			}
			if(tempDh.contains(temp.getDh()))
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
						Transformations.getLstrFromMuiStr(new StringBuilder("垛号@Stack number&[").append(temp.getDh().trim()).append("]&重复！@repeats!").toString()));
				
				setColumn(i, "dh");
				
				return false;
			}
			if(temp.getXxaglsl()==null||temp.getXxaglsl()==0)
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
				"下线\\隔离数量不能为空！The downline \\ isolate the number can not be empty!");
				
				setColumn(i, "xxaglsl");
				
				return false;
			}
			else if(!chekDh(temp.getDh()))
			{
				errdh.append("第["+(i+1)+"]行:["+temp.getDh()+"]\r\n");
			}
			tempDh.add(temp.getDh());
			//sqlWhere.append(temp.getDh()).append("','");
			
			i++;
		}
		if(errdh.length()>0)
		{
			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error","出现不符合要求的垛号@Does not meet the requirements crib No.&：\r\n"+errdh.toString());
			return false;
		}
//		sqlWhere = sqlWhere.delete(sqlWhere.length()-2, sqlWhere.length());
//		sqlWhere.append(")");
//		if(head.getPk_glzb()!=null&&!head.getPk_glzb().trim().equals(""))
//		{
//			sqlWhere.append(" and pk_glzb!='").append(head.getPk_glzb().trim()).append("' ");
//		}
//		//校验垛号不能重复
//		GlItemBVO[] items = (GlItemBVO[])HYPubBO_Client.queryByCondition(GlItemBVO.class,sqlWhere.toString());
//		if(items!=null&&items.length>0)
//		{
//			MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
//			"与以往单据垛号重复！");
//			return false;
//		}
		
		/*
		 *注： 垛号与数据库中垛号校验重复改至保存方法开头，故此处注释 wkf---2015-01-09
		 *  
		i=0;
		for (GlItemBVO temp : vos)
		{
			StringBuilder sql =null;
			if(temp.getStatus()==VOStatus.NEW)
			{
				  sql=new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getDh().trim()+"' ");
				
			}
			else
			{
				sql=new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getDh().trim()).append("' and pk_glzb_b!='"+temp.getPrimaryKey()+"' ");
			}
				sql.append(" union all select hcldh from mm_glcl_b where nvl(dr,0)=0 and hcldh ='").append(temp.getDh().trim()).append("'");
			
			List voList = null;
			try {
				IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				voList = (List)uAPQueryBS.executeQuery(sql.toString(), new ArrayListProcessor());
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this.getBillUI(),"错误Error",e1.getMessage());
				return false;
			}
			if(voList!=null&&voList.size()>0)
			{
				MessageDialog.showErrorDlg(this.getBillUI(), "错误Error",
				new StringBuilder("垛号@Stack number&[").append(temp.getDh().trim()).append("]已存在！@repeats!").toString());
				
				setColumn(i, "dh");
			
				return false;
			}			
			
			i++;
		}
		*/
		return true;
	}

	// protected boolean checkVO(GlBillVO billVO)
	// {
	// boolean flag = false;
	// GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
	// GlHeadVO head = (GlHeadVO) billVO.getParentVO();
	// return flag;
	// }

	protected BillItem getHeadBillItem(String key)
	{
		return this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				key);
	}

	protected void initialAdd()
	{
		setHeadItemValue("xxrq", ClientEnvironment.getServerTime().getDate());
		setHeadItemValue("phrq", getPihaoDate());
		setHeadItemValue("jyjg", 0);
		setHeadItemValue("djzt", 0);
		setHeadItemValue("ck",getDeafultCk());
	}
	
	protected String getDeafultCk()
	{
		String ck = null;
		try
		{
			ck = (String) HYPubBO_Client.findColValue("bd_stordoc", "pk_stordoc", " storcode = 'B01' and pk_corp='"+getUnitPK()+"'");
		} catch (UifException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ck;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception
	{
		switch (intBtn)
		{
		case IButton.Confirm: // 确认
			this.onConfirm();
			break;
		case IButton.Update_Num: // 批改货位
			onUpdateNum();
			break;
		case IButton.Print:
			onBoPrint();
			break;
			
			/** 2019-07-31
			 * 刘信彬
			 */
		case IButton.Offline://下线
			IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
			String corp = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_corp").getValue();
			Boolean result = idetermineService.check(corp);
			if(result){
				onOffline();
			}
			break;
			//add by yhj 2014-03-11
//		case IButton.SORT_NO://垛号排序
//			onBoSortNo();
//			break;
			//end
		default:

		}
	}

//	protected GeneralBillVO getRkVO(GlBillVO billVO) throws UifException
//	{
//		GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
//		GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
//
//		GeneralBillVO bo = new GeneralBillVO();
//		GeneralBillItemVO[] itemvos = new GeneralBillItemVO[vos.length];
//		bo.setLockOperatorid(ghead.getLrr());
//
//		// CLASTMODIID
//		GeneralBillHeaderVO head = bo.getHeaderVO();
//		head.setStatus(2);// 新增
//		bo.setParentVO(head);
//		bo.setChildrenVO(itemvos);
//
//		head.setCbilltypecode("46"); // 库存单据类型编码 46-产成品入库单
//		head.setCbizid(ghead.getXxry()); // 业务员ID
//		head.setCdispatcherid("1085A210000000001P2B"); // 收发类型ID ? “产成品入库”
//														// 在产成品入库界面可参照
//		// head.setCdptid(ghead.getBz()); //部门ID ？
//		// head.setCgeneralhid(ghead.getPk_glzb()); //主键
//		head.setCoperatorid(ghead.getLrr());// 制单人
//		head.setCwarehouseid(ghead.getCk());// 仓库ID
//		// head.setCwhsmanagerid(null);//库管员
//		head.setDbilldate(ClientEnvironment.getServerTime().getDate());// 单据日期
//		head.setFbillflag(null);// 单据状态
//		// head.setPk_calbody();//库存组织PK
//		head.setPk_corp(this.getUnitPK());// 公司ID
//		head.setVbillcode(null); // 单据号
//		head.setVnote("下线隔离单生成");// 备注
//
//		for (int i = 0; i < vos.length; i++)
//		{
//			itemvos[i] = new GeneralBillItemVO();
//			itemvos[i].setStatus(2);// 新增;
//			// 表体 BONROADFLAG BTOINZGFLAG BTOINZGFLAG BTOOUTTOIAFLAG
//			itemvos[i].setBbarcodeclose(new UFBoolean(false)); // 单据行是否条码关闭
//			itemvos[i].setBreturnprofit(new UFBoolean(false));// 是否返利
//			itemvos[i].setBsafeprice(new UFBoolean(false));// 是否价保
//			itemvos[i].setBsourcelargess(new UFBoolean(false));// 上游是否赠品行
//			itemvos[i].setBzgflag(new UFBoolean(false));// 暂估标志
//			// itemvos[i].setCfirstbillbid(vos[i].getPk_glzb_b());//源头单据表体id
//			// itemvos[i].setCfirstbillhid(vos[i].getPk_glzb());//源头单据表头id
//			// itemvos[i].setCfirsttype("JYDJ");//源头单据类型
//			// itemvos[i].setCfreezeid(vos[i].getPk_glzb_b());//锁定来源
//			itemvos[i].setCgeneralbid(null); // 表体主键、新增赋值
//			itemvos[i].setCgeneralhid(null); // 表头主键、新增赋值为空
//
//			String pk_invbasdoc = (String) HYPubBO_Client.findColValue(
//					"bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"
//							+ ghead.getCp() + "'");
//			itemvos[i].setCinvbasid(pk_invbasdoc);// 存货基本id
//
//			itemvos[i].setCinventoryid(ghead.getCp());// 存货id
//			itemvos[i].setCrowno(((i + 1) * 10) + "");// 行号
//			// itemvos[i].setCsourcebillbid(vos[i].getPk_glzb_b());//来源单据表体序列号
//			// itemvos[i].setCsourcebillhid(vos[i].getPk_glzb());//来源单据表头序列号
//			// itemvos[i].setCsourcetype("JYDJ");//来源单据类型
//			itemvos[i].setCvendorid(ghead.getKs());// 供应商id ？跟客商有啥区别
//			itemvos[i].setDbizdate(ClientEnvironment.getInstance().getDate());// 业务日期
//			// itemvos[i].setDbizdate(null);//业务日期
//			itemvos[i].setFchecked(0);// 0 待检标记
//			itemvos[i].setFlargess(new UFBoolean(false)); // N 是否赠品
//			itemvos[i].setIsok(new UFBoolean(true));// Y
//			itemvos[i].setNbarcodenum(null);// 0 条码数量
//			// itemvos[i].setNinnum(new
//			// UFDouble(vos[i].getXxaglsl().doubleValue()));//实入数量
//			itemvos[i].setNmny(null);// 金额
//			itemvos[i].setNprice(null);// 单价
//			itemvos[i].setNshouldinnum(null);// 应入数量
//			// itemvos[i].setPk_corp();
//			// itemvos[i].setPK_INVOICECORP();
//			// itemvos[i].setPK_REQCORP();
//			itemvos[i].setVbatchcode(null);// 批次号 入库单界面可参照
//			itemvos[i].setVfirstbillcode(null);// 源头单据号
//			itemvos[i].setVproductbatch(null);// 生产批号？
//			itemvos[i].setVsourcebillcode(null);// 来源单据号
//			// itemvos[i].setVsourcerowno(i+"");// 来源单据行号
//			itemvos[i].setVuserdef1(null);//
//			itemvos[i].setPk_cubasdoc(ghead.getKs());// 客商基本档案 跟供应商有啥区别
//		}
//		return bo;
//	}
	
	/**
	 * 下线检验单表头增加起始垛号、下线垛数，点下线按钮，自动生成表体明细行
	* 2019-08-01
	* @author 刘信彬
	*/
	protected void onOffline(){
		   try{
		      String ds=null;//表体的垛号字段
		      Integer jg=null;//后几位转为数字处理的结果
		      String dh=null;//已有的垛号
		      List<String> list=new ArrayList<String>();
		       UITable table = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable();
		       BillModel model = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		       int curRow = table.getRowCount();
		       /*
		        * 将已有的垛号放到集合中
		        */
		       for(int k=0;k<curRow;k++){
		    	  dh=(String)this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(k,"dh");
		    	 list.add(dh);
		       }
		       UIRefPane sdhpane = (UIRefPane) getHeadBillItem("startdh").getComponent();
		       String startdh=sdhpane.getRefName();//获取起始垛号的值
		       UIRefPane xxdspane = (UIRefPane) getHeadBillItem("xxds").getComponent();
		       String xxds=xxdspane.getRefName();//获取下线垛数的值
		       if("".equals(startdh.trim())){
		    	   getBillUI().showErrorMessage("起始垛号不能为空");
		    	   return;
		       }
		        if("".equals(xxds.trim())){
		        	getBillUI().showErrorMessage("下线垛数不能为空");
		        	return;
		        }
		        
		        String endfour=StringUtils.substring(startdh.trim(), -4);
		        if("0".equals(StringUtils.substring(endfour, -4,-3))){//判断截取的第四位是否为0，是判断后三位相加垛数是否需要进位
		        Integer d=Integer.parseInt(StringUtils.substring(endfour,-3))+Integer.parseInt(xxds.trim());
		        	if(d.toString().length()>StringUtils.substring(endfour,-3).length()){
		        		getBillUI().showErrorMessage("预留字段长度超出");
		        		return; 
		        	}
		        }else{
		        	Integer ko=Integer.parseInt(endfour)+Integer.parseInt(xxds.trim());
				       if(ko.toString().length()>endfour.length()){
				    	   getBillUI().showErrorMessage("预留字段长度超出");
				    	   return; 
				       }
		        }
		       UIRefPane pane = (UIRefPane) getHeadBillItem("cp").getComponent();  
		       UIRefPane hwpane = (UIRefPane) getHeadBillItem("pghw").getComponent();
		       UIRefPane ckpane = (UIRefPane) getHeadBillItem("ck").getComponent();
		       if(("".equals(hwpane.getRefName())) || (null==hwpane.getRefName())){
		    	   getBillUI().showErrorMessage("批改货位不能为空");
		    	   return;
		       }
		       if(("".equals(ckpane.getRefName()))|| (null==ckpane.getRefName())){
		    	   getBillUI().showErrorMessage("仓库不能为空");
		    	   return;
		       }
		       for(int i=0;i<Integer.parseInt(xxds.trim());i++){
		    	   int j=i+curRow;//新增的垛号的行号
		    	   if("0".equals(StringUtils.substring(endfour, -4,-3))){//判断截取的第四位是否为0,是，在相应的位置添加上
		    		   jg=Integer.parseInt(StringUtils.substring(startdh.trim(), -4))+i;
			    	   ds=StringUtils.substring(startdh.trim(), 0,startdh.trim().length()-4)+"0"+jg.toString();
		    	   }else{
		    		   jg=Integer.parseInt(StringUtils.substring(startdh.trim(), -4))+i;
			    	   ds=StringUtils.substring(startdh.trim(), 0,startdh.trim().length()-4)+jg.toString();
		    	   }
		             /*
		              * 判断垛号是否重复
		              */
		             if(list.contains(ds)){
		            	 getBillUI().showErrorMessage("垛号不能重复");
		            	 return;
		             }
		             onBoLineAdd();
		             model.setValueAt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zdsl").getValue(), j, "xxaglsl");
		 			 model.setValueAt(getUnitPK(), j, "pk_corp");
		             model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), j,"lh"); 
		             model.setValueAt(pane.getRefName(), j, "cp");
		             model.setValueAt(hwpane.getRefValue("bd_cargdoc.cscode "), j, "hw");
		             model.setValueAt(ds, j, "dh");
		             this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(j, "lh");
		              
		               }                    
		           }catch(Exception ex){
		               nc.bs.logging.Logger.error(ex);
		               ex.printStackTrace();
		           }                      
		       			        			        
			    }

	protected void onUpdateNum()
	{
		UIRefPane pane = (UIRefPane) getHeadItem("pghw").getComponent();

		UITable table = this.getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable();
		BillModel model = this.getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();
		int[] rows = table.getSelectedRows();

		for (int row : rows)
		{
			model.setValueAt(pane.getRefCode(), row, "hw");
			model.setValueAt(pane.getRefName(), row, "hwmc");
			model.setValueAt(pane.getRefPK(), row, "cspaceid");
			model.setRowState(row, 2); //设置行为已更新状态
		}

	}

	protected BillItem getHeadItem(String key)
	{
		return this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				key);
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
	

//	设置输入焦点
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
    strWhere = (new StringBuilder()).append("(").append(strWhere).append(") and (nvl(mm_glzb.dr,0)=0)").toString();
     if(getHeadCondition() != null)
        strWhere = (new StringBuilder()).append(strWhere).append(" and ").append("mm_glzb."+getHeadCondition()).toString();
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
//			 return false;
			 if(!(_getCorp().getPk_corp().equals("1078")||_getCorp().getPk_corp().equals("1108"))){//add by zwx 2019-8-15 制盖不校验
				 return false;
			 }else{
				 return true;
			 }
		 }
		 else 
		 {
			 Pattern pattern = Pattern.compile("\\d{10}");
			 Matcher matcher = pattern.matcher(dh);
			return  matcher.matches();
		 }
	  }
   private boolean CheckNumberOfShipments(String moid,int currQty)
   {
	   StringBuffer sql=new StringBuffer("select a.scddh, sum(nvl(b.xxaglsl, 0.0)) as CompletedQty from mm_glzb a, mm_glzb_b b where nvl(b.dr, 0) = 0 ");
	   sql.append("and nvl(a.djzt, 0) = 1 ").append("and a.pk_glzb = b.pk_glzb ")
	   .append("and a.billsign='JYDJ' and nvl(scddh,'')='"+moid+"'").append(" and nvl(a.jyjg,0)=0  group by a.scddh");
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
		HashMap hm = null;
		int totalnum=0;
	try {
		hm = (HashMap) sessionManager.executeQuery(sql.toString(),
			new MapProcessor());

	if (hm==null||hm.isEmpty()) {
		return true;
	}
	else 
	{
		String comnum=String.valueOf(hm.get("completedqty"));
		comnum=comnum==null||comnum.equals("")||comnum.equalsIgnoreCase("null")?"0":comnum;
		totalnum=currQty+new BigDecimal(comnum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		sql.setLength(0);
		sql.append("select nvl(jhwgsl,0.0)*1.01 as plannum from mm_mo where pk_moid='"+moid+"'");
		hm.clear();
		hm = (HashMap) sessionManager.executeQuery(sql.toString(),
				new MapProcessor());
		String plannum=String.valueOf(hm.get("plannum"));
		if(plannum==null||plannum.equals("")||plannum.equalsIgnoreCase("null"))
		{
			MessageDialog.showErrorDlg(getBillUI(),"错误Error","生产订单的计划生产数量为空！Planned production number of the production order is empty!");
			return false;
		}
		else 
		{
			if(totalnum>new BigDecimal(plannum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue())
			{
				MessageDialog.showErrorDlg(getBillUI(),"错误Error","下线数量已经超过生产订单的计划生产数量的1%！Referral number has exceeded the planned production number of the production order 1%!");
				return false;
			}
		}
	}
	}catch(BusinessException e)
	{
		MessageDialog.showErrorDlg(getBillUI(),"错误Error",e.getMessage());
		return false;
	}
	return true; 
   }
 
}