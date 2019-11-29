
package nc.ui.by.invapp.h0h003;

import javax.swing.ListSelectionModel;

import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;

public class ClientUI extends AbstractBdBillCardUI{
	 /**
	 * ¥Êªı∂‘’’±Ìui
	 */
	private static final long serialVersionUID = 1L;

	public ClientUI() {
		super();
		
	}
	
	/**
	 * @param pk_corp
	 * @param pk_billType
	 * @param pk_busitype
	 * @param operater
	 * @param billId
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	
	@Override
     protected ICardController createController() {
		return new ClientCtrl();
	}
	
	@Override
	public String getRefBillType() {
		return null;
	}
	
	@Override
    protected void initSelfData() {		
		super.initSelfData();
		
		getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void setDefaultData() throws Exception {	   		
		
	}
	
	public void setDefaultData(int row) throws Exception {	
		 getBillCardPanel().setBodyValueAt(new Integer(0), row, "dr");
		 getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");

	}
	
	protected CardEventHandler createEventHandler(){
		return new ClientEventHandler(this,getUIControl());
	}

	public Object getUserObject() {
		return null;
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
//		if (e.getKey().equals("invcode")) {
//			UIRefPane pane = (UIRefPane) getBillCardPanel().getBillModel().getItemByKey("invcode").getComponent();
//			if(pane!=null){
//				pane.setAutoCheck(false);
//				pane.setStrPatch("distinct");
//				int row = getBillCardPanel().getBillTable().getSelectedRow();
//				Object pk_invcl = getBillCardPanel().getBodyValueAt(row, "pk_invcl");
//				if (pk_invcl!=null) {
//					pane.setWhereString(" pk_invcl = '"+pk_invcl+"' ");
//				}
//				pane.updateUI();
//			}
//		}
		return super.beforeEdit(e);
	}
	
	
	
}
