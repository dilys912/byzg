/**
 * 
 */
package nc.ui.bgzg.b0003;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.bgzg.pub.IBgzgButton;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

/**
 * @author Administrator
 *
 */
public class ClientUI extends BillCardUI  implements BillCardBeforeEditListener{

	/**
	 * 
	 */
	public ClientUI() {
		// TODO Auto-generated constructor stub
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		// TODO Auto-generated constructor stub
	}	
    protected CardEventHandler createEventHandler() {
		return new ClientEH(this, getUIControl());
	}
	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	/* (non-Javadoc)
	 * @see nc.ui.trade.base.AbstractBillUI#setDefaultData()
	 */
	@Override
	public void setDefaultData() throws Exception {

	 
	}
	protected void initPrivateButton() {
		// TODO 自动生成方法存根
		 ButtonVO print = createButtonVO(IBgzgButton.print, "打印", "打印"); 
		 addPrivateButton(print);
		
	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}


	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		 getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(e.getKey()));
	}
	
	public  ButtonVO createButtonVO(int id, String code, String name) {
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(code);
		btn.setHintStr(name);
		btn.setBtnCode(name);
		btn.setBtnChinaName(code);
		return btn;
	}
	
	public boolean beforeEdit(BillItemEvent e) {
		if (e.getItem().getKey().equals("bgy")) {
			UIRefPane ref = (UIRefPane) e.getItem().getComponent();
			ref.getRefModel().addWherePart(" and pk_corp = '" + _getCorp().getPrimaryKey() + "' ");
		}
		return false;
	}
}
