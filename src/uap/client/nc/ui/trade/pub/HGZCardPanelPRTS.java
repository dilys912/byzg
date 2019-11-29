package nc.ui.trade.pub;

import javax.swing.ImageIcon;

import nc.bs.framework.common.NCLocator;
import nc.itf.mm.hgz.IPrintHgz;
import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;
import nc.ui.pub.print.IExDataSource;

/**
 * 卡片合格证打印
 * 创建日期：2019-8-20
 * @author：zwx
 */
public class HGZCardPanelPRTS implements nc.ui.pub.print.IExDataSource {

	private String m_sModuleName= "";
	private String barcode = "";
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-10 19:39:41)
 * @param m_sModuleName java.lang.String
 * @param billcardpanel nc.ui.pub.bill.BillCardPanel
 */
public HGZCardPanelPRTS(String m_sModuleName, BillCardPanel billcardpanel,String barcode) {
	super();
	this.m_billcardpanel = billcardpanel;
	this.m_sModuleName = m_sModuleName;
	this.barcode = barcode;
}
/**
 *
 * 得到所有的数据项表达式数组
 * 也就是返回所有定义的数据项的表达式
 *
 */
public String[] getAllDataItemExpress() {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null && m_billcardpanel.getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	int count= headCount + bodyCount + tailCount ;
	String[] expfields = new String[count];
	try{
		for (int i = 0; i < headCount; i++){
			expfields[i] = "h_"+m_billcardpanel.getHeadItems()[i].getKey();
		}
		for (int j = 0; j < bodyCount ; j++){
			expfields[j+headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getKey();
		}
		for (int k = 0; k < tailCount ; k++){
			expfields[k+headCount+bodyCount] = "t_" + m_billcardpanel.getTailItems()[k].getKey();
		}
	}catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemExpress()");
	}
	return expfields;
}
/**
 *
 * 得到所有的数据项表达式数组
 * 也就是返回所有定义的数据项的打印字段名
 *
 */
public String[] getAllDataItemNames() {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	int count = headCount + bodyCount + tailCount;
	String[] namefields = new String[count];
	try{
		for (int i = 0; i < headCount; i++){
			namefields[i] = m_billcardpanel.getHeadItems()[i].getName();
		}
		for (int j = 0; j < bodyCount ; j++){
			namefields[j + headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getName();
		}
		for (int k = 0 ; k < tailCount ; k++){
			namefields[k + headCount+bodyCount] = m_billcardpanel.getTailItems()[k].getName();
		}
	}catch (Throwable e) {
		e.printStackTrace();
		System.out.print("error at  getAllDataItemNames()");
	}
	return namefields;
}
/**
 *
 * 返回依赖项的名称数组，该数据项长度只能为 1 或者 2
 * 返回 null : 		没有依赖
 * 长度 1 :			单项依赖
 * 长度 2 :			双向依赖
 *
 */
public String[] getDependentItemExpressByExpress(String itemName) {
	return null;
}
/*
 * 返回所有的数据项对应的内容
 * 参数： 数据项的名字
 * 返回： 数据项对应的内容，只能为 String[]；

 */
public String[] getItemValuesByExpress(String itemExpress) {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	int rowCount  = m_billcardpanel.getRowCount();
	try {
		//表头
		if(itemExpress.startsWith("h_")){
			BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
			if(item==null) return null;
			if (item.getKey().equals(itemExpress.substring(2))) {
				//UICheckbox
				if(item.getDataType()==4){
					if(item.getValue()==null){
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
					}else{
						if(item.getValue().equals("false")){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
						}else{
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/};
						}
					}
				}
				//UICombox
				else if(item.getDataType()==6){
					String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
					return new String[] { sc };
				}
				//UIRefPane
				else if (item.getDataType() == 5){
  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
	  				return new String[] { sr };
				}
               //	大文本
				else if (item.getDataType() == 9) {
				String wb = ((UITextAreaScrollPane)item.getComponent()).getText();
				return new String[] { wb };
				}

				//其它文本
				else{
					String wb = ((UIRefPane)item.getComponent()).getText();
				  	//cf add 如果是数植型则根据小数位数format
				   	try{
					   	if(item.getDataType() == 2){
							UIRefPane item_h=(UIRefPane)item.getComponent();
							nc.vo.pub.lang.UFDouble value=new nc.vo.pub.lang.UFDouble(wb);
							value=value.setScale(item_h.getNumPoint(),4);
							wb=value.toString();
						}
					}catch(Exception e){
						System.out.println("如果是数植型则根据小数位数format出错:"+e);
				  	}
				   	//cf add
				 	return new String[] { wb };
				}
			}

		}
		//表尾
		else if(itemExpress.startsWith("t_")){
			BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
			if(item==null) return null;
			if (item.getKey().equals(itemExpress.substring(2))) {
				//UICheckbox
				if(item.getDataType()==4){
					if(item.getValue()==null){
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
					}else{
						if(item.getValue().equals("false")){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
						}else{
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/};
						}
					}
				}
				//UICombox
				else if(item.getDataType()==6){
					String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
					return new String[] { sc };
				}
				//UIRefPane
				else if (item.getDataType() == 5){
  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
	  				return new String[] { sr };
				}
				//其它文本
				else{
					String wb = ((UIRefPane)item.getComponent()).getText();
				    return new String[] { wb };
				}
			}
		}
		//表体
		else{
			for (int i = 0; i < bodyCount; i++){
				BillItem item = m_billcardpanel.getBillModel().getBodyItems()[i];
				if(item==null) return null;
				String[] rslt = new String[rowCount];
				if (item.getKey().equals(itemExpress)) {
					//UICheckbox
					if(item.getDataType()==4){
						for (int j = 0; j < rowCount; j++) {
							if(m_billcardpanel.getBodyValueAt(j, item.getKey())==null){
								rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/;
							}else{
								if(m_billcardpanel.getBodyValueAt(j, item.getKey()).toString().equals("false")){
									rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/;
								}else{
									rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/;
								}
							}
						}
					}
					//UIRefPane or UICombox
					else{
						for (int j = 0; j < rowCount; j++) {
							rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
						}
					}
					return rslt;
				}
			}
		}

	} catch (Throwable e) {
		e.printStackTrace();
	    System.out.print("error at getItemValueByExpress()");
	    return null;
	}
	return null;
	}

/*
 *  返回该数据源对应的节点编码
 */
public String getModuleName() {
	return m_sModuleName;
}
/*
 * 返回该数据项是否为数字项
 * 数字项可参与运算；非数字项只作为字符串常量
 * 如“数量”为数字项、“存货编码”为非数字项
 */
public boolean isNumber(String itemExpress) {
	/**数据类型 */
	//int headCount = 0;
	//int bodyCount = 0;
	//int tailCount = 0;
	//if (m_billcardpanel.getHeadItems() != null){
		//headCount = m_billcardpanel.getHeadItems().length;
	//}
	//if (m_billcardpanel.getBodyItems() != null){
		//bodyCount =m_billcardpanel.getBillModel().getBodyItems().length;
	//}
	//if (m_billcardpanel.getTailItems() != null){
		//tailCount = m_billcardpanel.getTailItems().length;
	//}

	try {
		if (itemExpress.startsWith("h_")){
			BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
			if (item==null) return false;
			if (item.getDataType() == 1 || item.getDataType() == 2){
				return true;
			}
		}
		else if (itemExpress.startsWith("t_")){
			BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
			if (item==null) return false;
			if (item.getDataType() == 1 || item.getDataType() == 2){
				return true;
			}
		}
		else {
			//需要保证billModel不为空
			if(m_billcardpanel.getBillModel()==null)
				return false;
			BillItem[] items=m_billcardpanel.getBillModel().getBodyItems();
			BillItem item=null;
			for(int i=0;i<items.length;i++){
				if (items[i].getKey().equals(itemExpress)){
					item=items[i];
					break;
				}
			}
			if (item==null) return false;
			if (item ==null){
				return false;
			}
			else if (item.getDataType() == 1 || item.getDataType() == 2){
				return true;
			}
		}
	} catch (Throwable e) {
		e.printStackTrace();
		System.out.print("error at  isNumber()");
		return false;
	}
	return false;
}


	private BillCardPanel m_billcardpanel = null;
	
	public Object[] getObjectByExpress(String itemExpress) {
		if(itemExpress.equals("h_vdef1")){
			Object[] pics = new Object[1];
			IPrintHgz ipr=(IPrintHgz)NCLocator.getInstance().lookup(IPrintHgz.class.getName());
			ImageIcon icon = ipr.createImg(barcode);
			pics[0] = icon;

			return pics;
		}
		return null;
	}

	public int getObjectTypeByExpress(String itemExpress) {
		if(itemExpress.equals("h_vdef1"))
			return IExDataSource.IMAGE_TYPE;
		return 0;
	}
}