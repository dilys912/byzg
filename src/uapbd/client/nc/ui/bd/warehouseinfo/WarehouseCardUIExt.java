package nc.ui.bd.warehouseinfo;

import nc.ui.trade.card.CardEventHandler;

/**
 * �ֿ��������UI��չ�ࣨPDA��
 * @author yhj 2014-02-19
 *
 */
public class WarehouseCardUIExt extends nc.ui.bd.warehouseinfo.WarehouseCardUI{
	
	private static final long serialVersionUID = 1L;

	protected CardEventHandler createEventHandler()
    {
        return new WarehouseCardEventHandlerExt(this, getUIControl());
    }
	
}
