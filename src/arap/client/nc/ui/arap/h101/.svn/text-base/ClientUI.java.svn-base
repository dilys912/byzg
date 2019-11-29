package nc.ui.arap.h101; 
  
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.by.invapp.billmanage.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ep.dj.DJFBVO;
import nc.vo.pub.BusinessException;
 
/** 
 * @author  
 * 名称: UI类 
*/ 
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI { 
  
	protected UIRefPane corpRef = null;//公司参照
	
	public ClientUI() { 
	} 
  
 	public ManageEventHandler createEventHandler() { 
 		return new ClientEventHandler(this, this.createController()); 
 	} 
  
 	protected AbstractManageController createController() { 
 		return new ClientCtrl(); 
 	} 
 	
 	@Override
 	protected void initSelfData() {
 		super.initSelfData();
 	}

 	IUAPQueryBS qbs = ((IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()));
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}


	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}

	@Override
	public void initPrivateButton() {
		super.initPrivateButton();
	}
	
} 
