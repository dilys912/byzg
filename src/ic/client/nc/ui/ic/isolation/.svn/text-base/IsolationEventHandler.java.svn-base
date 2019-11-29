package nc.ui.ic.isolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.query.INormalQuery;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.ic.ic601.InvOnHandVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

public class IsolationEventHandler extends ManageEventHandler {
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	BillManageUI billUI;

	public IsolationEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		this.billUI = billUI;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected UIDialog createQueryUI() {
		// TODO Auto-generated method stub
		return super.createQueryUI();
	}

	@Override
	public void onButton(ButtonObject arg0) {
		// TODO Auto-generated method stub
		if (arg0.getCode().equals("解除隔离")) {
			try {
				onBoRemove();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if (arg0.getCode().equals("行解除隔离")) {
			try {
				onBoRemoveRow();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				MessageDialog.showErrorDlg(getBillUI(), "错误Error", "操作失败Operation fails："+e.getMessage());
				e.printStackTrace();
			}
			return;
		}
		if (arg0.getCode().equals("库存量查询") || arg0.getCode().equals("增加")) {
			try {
				onBoQueryProducts();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if (arg0.getCode().equals("销售退货查询")) {
			try {
				onBoSalesOut();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if (arg0.getCode().equals("调拨销售退货查询")) {
			try {
				onBoDiaoboSalesOut();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}else if(arg0.getCode().equals("删除行"))
		{
			onBoDelLine();
		}
		else if(arg0.getCode().equalsIgnoreCase("Print"))
		{
			try {
				onBoPrint();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		super.onButton(arg0);
	}

	private void onBoDelLine()
	{
		int[] rows = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if(rows==null||rows.length==0)
		{
			MessageDialog.showErrorDlg(getBillUI(), "错误Error", "没有选中行！Not selected row!");
			return;
		}
		//删除行操作 
		this.getBillCardPanelWrapper().deleteSelectedLines();
	}
	
	private void onBoSalesOut() throws Exception {
		// TODO Auto-generated method stub
		IsolationQuerySalesOutDlg dlg=new IsolationQuerySalesOutDlg();
		getBillUI().setBillOperate(1);
		dlg.onQuery();
		billUI.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		Vector tor=dlg.vcs;
		if (tor != null && tor.size()>0) {
			GlItemBVO[] vos = new GlItemBVO[dlg.length];
		int index=0;
			for (int i = 0;i<tor.size() ;i++) {
				Vector to=(Vector) tor.get(i);;
				for (int j = 0;j<to.size();j++) {
			
				GlItemBVO vo = new GlItemBVO();

				vo.setLh(to.get(0)==null?"":to.get(0).toString());//料号
				vo.setPh(to.get(1)==null?"":to.get(1).toString());//批次号
				vo.setCp(to.get(2)==null?"":to.get(2).toString());//产品
			//	vo.setHw(to.get(2).toString());//货位
				vo.setIsolationckid(to.get(3)==null?"":to.get(3).toString());//仓库id
				vo.setIsolationcpid(to.get(4)==null?"":to.get(4).toString());//产品ID

				vo.setXxaglsl((Integer)to.get(5)); //数量
				vo.setDh((String)to.get(6)); //垛号
				vo.setHw((String)to.get(7)); //货位
				
				vos[i] = vo;
				}
			}
			
			GlHeadVO headvo = new GlHeadVO();
			headvo.setPk_corp(ce.getCorporation().getPk_corp());
			headvo.setBillsign("isolation");
			headvo.setXxrq(ce.getDate());
			headvo.setXxry(ce.getUser().getPrimaryKey());
			// headvo.setCk(result.getChildrenVO()[0].getAttributeValue("storname")==null?"":result.getChildrenVO()[0].getAttributeValue("storname").toString());
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(headvo);
			aVo.setChildrenVO(vos);
			this.getBillCardPanelWrapper().setCardData(aVo);

			((BillManageUI) getBillUI()).getBillCardPanel().setEnabled(true);
			this.getButtonManager().getButton(0).setEnabled(true);
			this.getBillUI().updateButtons();
		}
	}

	private void onBoDiaoboSalesOut() throws Exception{
		// TODO Auto-generated method stub
		IsolationDiaoboSalesOutDlg dlg=new IsolationDiaoboSalesOutDlg();
		getBillUI().setBillOperate(1);
		dlg.onQuery();
		billUI.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		Vector tor=dlg.vcs;
		if (tor != null && tor.size()>0) {
			GlItemBVO[] vos = new GlItemBVO[dlg.length];
		int index=0;
			for (int i = 0;i<tor.size() ;i++) {
				Vector to=(Vector) tor.get(i);;
				for (int j = 0;j<to.size();j++) {
			
				GlItemBVO vo = new GlItemBVO();

				vo.setLh(to.get(0)==null?"":to.get(0).toString());//料号
				vo.setPh(to.get(1)==null?"":to.get(1).toString());//批次号
				vo.setCp(to.get(2)==null?"":to.get(2).toString());//产品
			//	vo.setHw(to.get(2).toString());//货位
				vo.setIsolationckid(to.get(3)==null?"":to.get(3).toString());//仓库id
				vo.setIsolationcpid(to.get(4)==null?"":to.get(4).toString());//产品ID
				
				vo.setXxaglsl((Integer)to.get(5)); //数量
				vo.setDh((String)to.get(6)); //垛号
				vo.setHw((String)to.get(7)); //货位
				vos[i] = vo;
				}
			}
			
			GlHeadVO headvo = new GlHeadVO();
			headvo.setPk_corp(ce.getCorporation().getPk_corp());
			headvo.setBillsign("isolation");
			headvo.setXxrq(ce.getDate());
			headvo.setXxry(ce.getUser().getPrimaryKey());
			// headvo.setCk(result.getChildrenVO()[0].getAttributeValue("storname")==null?"":result.getChildrenVO()[0].getAttributeValue("storname").toString());
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(headvo);
			aVo.setChildrenVO(vos);
			this.getBillCardPanelWrapper().setCardData(aVo);

			((BillManageUI) getBillUI()).getBillCardPanel().setEnabled(true);
			this.getButtonManager().getButton(0).setEnabled(true);
			this.getBillUI().updateButtons();
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		String billno=String.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject());
		StringBuffer qryWhere = new StringBuffer();
	    StringBuffer strWhere = new StringBuffer();
		strWhere.append("pk_glzb in( select mm_glzb.pk_glzb from mm_glzb ");//add LGY
		if (askForQueryCondition(qryWhere) == false)
			
			return;// 用户放弃了查询
		//add LGY
	if(qryWhere.indexOf("mm_glzb_b")>0)
	{
		strWhere.append("" +
				" inner join mm_glzb_b on mm_glzb_b.pk_glzb=mm_glzb.pk_glzb ");
	}
	else 
	{	
		strWhere.append(" where 1=1 ");
	}
		strWhere.append("and  billsign='isolation' and ").append(qryWhere.toString()+")");//add LGY
		
		SuperVO[] queryVos =HYPubBO_Client.queryByCondition(GlHeadVO.class, strWhere.toString());;

		//SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		getBufferData().clear();
		addDataToBuffer(queryVos);
             
		updateBuffer();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billno);
		this.getBillUI().updateButtons();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		if(!checkSave())
		{
			return;
		}
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		for(int i=0;i<billVO.getChildrenVO().length;i++)
		{
			if((billVO.getChildrenVO()[i]).getStatus()==VOStatus.NEW)
			{
				(billVO.getChildrenVO()[i]).setAttributeValue("pk_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			}
		}
		billVO  = (AggregatedValueObject) this.getBusinessAction().processAction("SAVE",billVO , "53", _getDate().toString(), this.getBillUI().getUserObject());
//		super.onBoSave();
		// 将VO添加到缓存
		addVoToBuffer(billVO); 
		this.updateBuffer();
		this.getBufferData().setCurrentVO(billVO);
		// 设置界面状态
		setSaveOperateState();
		// 刷新界面
		onBoRefresh(); 
		MessageDialog.showHintDlg(getBillUI(), "提示Prompt", "操作成功！Operation is successful");
		if (billUI.isListPanelSelected())
			billUI.setCurrentPanel(BillTemplateWrapper.LISTPANEL);
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
	
	protected boolean checkSave()
	{
		int rows = billUI.getBillCardPanel().getBillModel().getRowCount(); 
		GlItemBVO item = null;
		for (int i = 0; i < rows; i++) {
			item = (GlItemBVO) billUI.getBillCardPanel().getBillModel()
			.getBodyValueRowVO(i, GlItemBVO.class.getName());// 得到表体
			if(item.getXxaglsl()==null||item.getXxaglsl()==0)
			{
				MessageDialog.showErrorDlg(getBillUI(), "错误Error", "隔离数量不能为空！Isolation quantity can not be empty!");
				return false;
			}
		}
		return true;
	}
	
	

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
	}

	/*
	 * 库存量查贸易
	 */
	public void onBoQueryProducts() throws Exception {

		String billno=null;
		//getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billno);
		billUI.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBillUI().setBillOperate(1);
		IsolationQueryConditionDlg dlg = new IsolationQueryConditionDlg();
		 
		InvOnHandVO result = dlg.onQuery();
		if (result != null) {
			 billno=String.valueOf(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject());
			GlItemBVO[] vos = new GlItemBVO[result.getChildrenVO().length];
			InvOnHandItemVO item = null;
			for (int i = 0; i < result.getChildrenVO().length; i++) {
				GlItemBVO vo = new GlItemBVO();
				item = (InvOnHandItemVO) result.getChildrenVO()[i];
				vo.setLh(result.getChildrenVO()[i].getAttributeValue("invcode") == null ? ""
						: result.getChildrenVO()[i]
								.getAttributeValue("invcode").toString());//料号
				vo.setPh(result.getChildrenVO()[i]
						.getAttributeValue("vbatchcode") == null ? "" : result
						.getChildrenVO()[i].getAttributeValue("vbatchcode")
						.toString());//批次号
				vo.setCp(result.getChildrenVO()[i].getAttributeValue("invname") == null ? ""
						: result.getChildrenVO()[i]
								.getAttributeValue("invname").toString());//产品
//				vo.setHw(result.getChildrenVO()[i].getAttributeValue("csname") == null ? ""
//						: result.getChildrenVO()[i].getAttributeValue("csname")
//								.toString());//货位
				String cspaceid = (String)item.getAttributeValue("cspaceid");
				if(cspaceid!=null&&!cspaceid.trim().equals(""))
				{
					//String cscode = (String)HYPubBO_Client.findColValue("bd_cargdoc", "cscode", " pk_cargdoc='"+cspaceid+"'");
					//vo.setHw(cscode);
					
					vo.setHw(cspaceid);
					
//					String csname = (String)HYPubBO_Client.findColValue("bd_cargdoc", "csname", " pk_cargdoc='"+cspaceid+"'");
//					vo.setHwname(csname);
				}
				vo.setIsolationckid(result.getChildrenVO()[i]
						.getAttributeValue("cwarehouseid") == null ? ""
						: result.getChildrenVO()[i].getAttributeValue(
								"cwarehouseid").toString());//仓库id
				vo.setIsolationcpid(result.getChildrenVO()[i]
						.getAttributeValue("cinventoryid") == null ? ""
						: result.getChildrenVO()[i].getAttributeValue(
								"cinventoryid").toString());//产品ID
				vo.setDh(item.getFreeItemVO().getVfree1()); //垛号
				if(item.getNum()!=null) //下线数量
				{
					vo.setXxaglsl(item.getNum().intValue());
				}
//				vo.setHw(item.getCscode());

				vos[i] = vo;
			}
			GlHeadVO headvo = new GlHeadVO();
			headvo.setPk_corp(ce.getCorporation().getPk_corp());
			headvo.setBillsign("isolation");
			headvo.setXxrq(ce.getDate());
			headvo.setXxry(ce.getUser().getPrimaryKey());
		
			// headvo.setCk(result.getChildrenVO()[0].getAttributeValue("storname")==null?"":result.getChildrenVO()[0].getAttributeValue("storname").toString());
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(headvo);
			aVo.setChildrenVO(vos);
			this.getBillCardPanelWrapper().setCardData(aVo);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billno);
			((BillManageUI) getBillUI()).getBillCardPanel().setEnabled(true);
			this.getButtonManager().getButton(0).setEnabled(true);
			this.getBillUI().updateButtons();
		}
	}
	
	protected void addVoToBuffer(AggregatedValueObject billVO) throws Exception
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

	/*
	 * 行解除隔离
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onBoRemoveRow() throws Exception {
		int selerow[] = billUI.getBillCardPanel().getBillTable()
				.getSelectedRows();
		
		if(selerow!=null&&selerow.length==0)
		{
			MessageDialog.showWarningDlg(getBillUI(), "提示Prompt", "请选择需要解除隔离的行！Please select the need to lift the isolation of the line!");
			return;
		} 
		AggregatedValueObject billVO  = this.getBufferData().getCurrentVO();
		if(billVO==null||billVO.getParentVO()==null)
		{
			return;
		}  
		addVoToBuffer(billVO);
		GlItemBVO[] items = (GlItemBVO[]) billVO.getChildrenVO();
		List list  = new ArrayList();
		for(int i:selerow)
		{ 
			if(items[i].getClzt()==null||items[i].getClzt()==0)
			{
				list.add(items[i]);
			}
			else
			{
				MessageDialog.showErrorDlg(getBillUI(), "", Transformations.getLstrFromMuiStr("第@Line&"+(i+1)+"&行已经解除隔离@has been released from quarantine!"));
				return;
			}
		} 
		
		GlBillVO jcVO = new GlBillVO();
		jcVO.setParentVO(billVO.getParentVO());
		jcVO.setChildrenVO((GlItemBVO[])list.toArray(new GlItemBVO[0]));
		billVO = (AggregatedValueObject) getBusinessAction().processAction("JCGL", billVO, "53",_getDate().toString(), null);
		this.onBoRefresh();
		
		MessageDialog.showHintDlg(getBillUI(), "提示Prompt", "操作成功！Operation is successful!");
		
//		FreezeVO frees[] = new FreezeVO[selerow.length];
//		for (int i = 0; i < selerow.length; i++) {
//			GlItemBVO vo = (GlItemBVO) billUI.getBillCardPanel().getBillModel()
//					.getBodyValueRowVO(selerow[i], GlItemBVO.class.getName());// 得到表体
//			FreezeVO free = new FreezeVO();
//			free.setAttributeValue("nfreezenum", vo.getXxaglsl());// 冻解数量
//			free.setAttributeValue("cfreezerid ", ce.getUser().getPrimaryKey());// 冻解人
//			free.setPk_corp(ce.getCorporation().getPk_corp());
//			free.setCwarehouseid(vo.getIsolationckid());
//			free.setCfreezerid(ce.getUser().getPrimaryKey());
//			free.setDtfreezetime(ce.getDate());
//			free.setCinventoryid(vo.getIsolationcpid());// 存货ID
//			free.setInvname(vo.getCp());
//			frees[i] = free;
//		} 
//		FreezeHelper.unfreeze(frees);
	
	}

	/*
	 * 解除隔离
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onBoRemove() throws Exception {
		 
		AggregatedValueObject billVO  = this.getBufferData().getCurrentVO();
		if(billVO==null||billVO.getParentVO()==null)
		{
			return;
		} 
		addVoToBuffer(billVO);
		GlItemBVO[] items = (GlItemBVO[]) billVO.getChildrenVO();
		List list  = new ArrayList();
		for(GlItemBVO item:items)
		{
			if(item.getClzt()==null||item.getClzt()==0)
			{
				list.add(item);
			}
		}
		if(list.size()==0)
		{
			MessageDialog.showHintDlg(getBillUI(), "提示Prompt", "当前单据的所有行已经解除隔离！Documents all rows have been released from quarantine!");
			return;
		}
		GlBillVO jcVO = new GlBillVO();
		jcVO.setParentVO(billVO.getParentVO());
		jcVO.setChildrenVO((GlItemBVO[])list.toArray(new GlItemBVO[0]));
		billVO = (AggregatedValueObject) getBusinessAction().processAction("JCGL", billVO, "53",_getDate().toString(), null);
		this.onBoRefresh();
		
		MessageDialog.showHintDlg(getBillUI(), "提示Prompt", "操作成功！Operation is successful!");
		
//		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
//		getBusinessAction().delete(modelVo, getUIController().getBillType(),
//				getBillUI()._getDate().toString(), getBillUI().getUserObject());
//		if (PfUtilClient.isSuccess()) {
//			// 清除界面数据
//			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
//			if (getUIController() instanceof ISingleController) {
//				ISingleController sctl = (ISingleController) getUIController();
//				if (!sctl.isSingleDetail())
//					getBufferData().removeCurrentRow();
//			} else {
//				getBufferData().removeCurrentRow();
//			}
//
//		}
//		if (getBufferData().getVOBufferSize() == 0)
//			getBillUI().setBillOperate(IBillOperate.OP_INIT);
//		else
//			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
//		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
//
//		// 去更改现存量，
//		int selerow = billUI.getBillCardPanel().getBillTable().getRowCount();
//		FreezeVO frees[] = new FreezeVO[selerow];
//		for (int i = 0; i < selerow; i++) {
//			GlItemBVO vo = (GlItemBVO) billUI.getBillCardPanel().getBillModel()
//					.getBodyValueRowVO(i, GlItemBVO.class.getName());// 得到表体
//			FreezeVO free = new FreezeVO();
//			free.setAttributeValue("nfreezenum", vo.getXxaglsl());// 冻解数量
//			free.setAttributeValue("cfreezerid ", ce.getUser().getPrimaryKey());// 冻解人
//			free.setPk_corp(ce.getCorporation().getPk_corp());
//			free.setCwarehouseid(vo.getIsolationckid());
//			free.setCfreezerid(ce.getUser().getPrimaryKey());
//			free.setDtfreezetime(ce.getDate());
//			free.setCinventoryid(vo.getIsolationcpid());// 存货ID
//			free.setInvname(vo.getCp());
//			frees[i] = free;
//		}  
//		FreezeHelper.unfreeze(frees);
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
}
