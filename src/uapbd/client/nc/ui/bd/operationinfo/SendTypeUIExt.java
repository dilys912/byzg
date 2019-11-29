package nc.ui.bd.operationinfo;

import nc.ui.trade.card.CardEventHandler;

public class SendTypeUIExt extends nc.ui.bd.operationinfo.SendTypeUI{

	
	@Override
	protected CardEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new SendTypeHandlerExt(this,getUIControl());
	}
}
