package nc.ui.bgzg.b0003;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.bgzg.b0002.HGZVO;
import nc.vo.bgzg.b0002.MyBillVO;
import nc.vo.bgzg.pub.IBgzgButton;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * @author Administrator
 *
 */

public class ClientEH extends CardEventHandler {

	public ClientEH(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	private ClientEnvironment ce = ClientEnvironment.getInstance();
 
 
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		
		if(intBtn == IBgzgButton.print){
			Print();
		}
		super.onBoElse(intBtn);
	}
	public void Print() throws Exception {
		super.onBoPrint();
	}
	

}
