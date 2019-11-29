package nc.ui.by.invapp.h0h001;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;

@SuppressWarnings("serial")
public class TreeCardUI extends BillTreeCardUI implements BillCardBeforeEditListener{
	
	@Override
	protected IVOTreeData createTableTreeData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		// TODO Auto-generated method stub
		return new TreeCardData();
	}
	
	@Override
	protected CardEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new TreeCardEventHandler(this, this.createController());
	}
	
	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new TreeCardController();
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		//保质期管理
		if (e.getKey().equals("qualitymanflag")){
			Object qualitymanflag = getBillCardPanel().getHeadItem("qualitymanflag").getValueObject();
			BillItem qualty = getBillCardPanel().getHeadItem("qualitydaynum");
			if(qualitymanflag.equals("false")){
				qualty.setValue("");
				qualty.setEdit(false);
			}else{
				qualty.setEdit(true);
			}
		}else if(e.getKey().equals("qualitydaynum")){
			Object qualitymanflag = getBillCardPanel().getHeadItem("qualitymanflag").getValueObject();
			BillItem qualty = getBillCardPanel().getHeadItem("qualitydaynum");
			if(qualitymanflag.equals("false")){
				showErrorMessage("保质期管理没有勾选，不能设置保质期天数！");
				qualty.setValue("");
				qualty.setEdit(false);
			}
		}
	}
	
	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("scdef1").getComponent();
		if(pane!=null){
			String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'DBZ062' )";
			pane.getRef().getRefModel().addWherePart(sql,true);
		}
		pane.updateUI();
		
	}
	
	@Override
	protected void postInit() {
		super.postInit();
	}

	public boolean beforeEdit(BillItemEvent arg0) {
		return false;
	}
	
	
}
