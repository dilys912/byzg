package nc.ui.ic.isolation;

import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.pf.pub.Toolkit;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.tm.framework.button.IButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;



public class IsolationUI extends BillManageUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ClientEnvironment ce = ClientEnvironment.getInstance();

	public IsolationUI() {
		// TODO Auto-generated constructor stub
		
		super();
		 getBillCardPanel().getHeadItem("xxrq").setValue(ce.getDate());
		 getBillCardPanel().getHeadItem("pk_corp").setValue(ce.getCorporation().getPk_corp());
		 getBillCardPanel().getHeadItem("billsign").setValue("isolation"); 
	}
	
	@Override 
	protected void initPrivateButton()
	{
		ButtonVO btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("解除隔离","ReleasedFromQuarantine"));
		btn.setBtnCode("解除隔离");
		btn.setBtnChinaName("解除隔离");
		btn.setBtnNo(222); 
		btn.setOperateStatus(new int[]{2,4,5});
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("行解除隔离","LineReleasedFromQuarantine"));
		btn.setBtnCode("行解除隔离");
		btn.setBtnChinaName("行解除隔离");
		btn.setBtnNo(223);
		btn.setOperateStatus(new int[]{2,4,5});
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("库存量查询","InventoryQuery"));
		btn.setBtnCode("库存量查询");
		btn.setBtnChinaName("库存量查询");
		btn.setBtnNo(224);
		btn.setOperateStatus(new int[]{0,1,2,3,4,5,6,7});
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("销售退货查询","SalesReturnsQueries"));
		btn.setBtnCode("销售退货查询");
		btn.setBtnChinaName("销售退货查询");
		btn.setBtnNo(225);
		btn.setOperateStatus(new int[]{0,1,2,3,4,5,6,7});
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("调拨销售退货查询","AllocateSalesReturnsQuery"));
		btn.setBtnCode("调拨销售退货查询");
		btn.setBtnChinaName("调拨销售退货查询");
		btn.setBtnNo(226);
		btn.setOperateStatus(new int[]{0,1,2,3,4,5,6,7});
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("删除行","LineDelete"));
//		btn.setBtnCode("删除行");
		btn.setBtnChinaName("删除行");
		btn.setBtnNo(227);
		btn.setOperateStatus(new int[]{1,0,3});
		this.addPrivateButton(btn);
		this.addPrivateButton(createButtonVO(228,"Print",Transformations.getLstrFromMuiStr("打印","Print")));
	}

	
	@Override
	protected BillCardPanelWrapper createBillCardPanelWrapper()
			throws Exception {
		// TODO Auto-generated method stub
		return super.createBillCardPanelWrapper();
	}
	@Override
	protected BillListPanelWrapper createBillListPanelWrapper()
			throws Exception {
		// TODO Auto-generated method stub
		return super.createBillListPanelWrapper();
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		AbstractManageController controller=new IsolationController();
		
		return controller;
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		ManageEventHandler evenHandler=new IsolationEventHandler(this,getUIControl());
		return evenHandler;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		setHeadItemValue("billno", Toolkit.getBillNO("53", this.getCorpPrimaryKey(), null, null));
	}

	public void loadListHeadData(String arg0, QueryConditionClient arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	public  ButtonVO createButtonVO(int id, String code, String name) {
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(name);
		btn.setHintStr(name);
		btn.setBtnCode(code);
		btn.setBtnChinaName(code);
		if(code.equalsIgnoreCase("print"))
		{
			btn.setOperateStatus(new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
			//btn.setBusinessStatus(new int[]{});
		}
		return btn;
	}
	protected void setHeadItemValue(String key,Object value)
	{
		this.getBillCardPanel().getHeadItem(key).setValue(value); 
	}
}
