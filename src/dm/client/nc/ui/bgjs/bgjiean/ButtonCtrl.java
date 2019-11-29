/*
 * $Id: ButtonCtrl.java,v 1.1 2012/02/13 06:31:20 zhwj Exp $
 * 功能：TODO
 * 履历：2008-9-4 创建 BY zhwj
 */
package nc.ui.bgjs.bgjiean;

import nc.ui.bgjs.bill.AbstractButtonCtrl;
import nc.ui.pub.ButtonObject;


public class ButtonCtrl extends AbstractButtonCtrl {
	protected ButtonObject m_query;//查询按纽
	protected ButtonObject m_jiean;//结案按纽
	protected ButtonObject m_unjiean;//取消结案按纽
	/**
	 * 
	 */
	public ButtonCtrl() {
		// TODO 自动生成构造函数存根
	}

	/* （非 Javadoc）
	 * @see nc.ui.loreal.bill.AbstractButtonCtrl#initbutt()
	 */
	@Override
	public void initbutt() {
		// TODO 自动生成方法存根
		m_query=new ButtonObject("查询");
		m_jiean=new ButtonObject("结案");
		m_unjiean=new ButtonObject("取消结案");
		m_boButtonGroup =new ButtonObject[]{
				m_query,
				m_jiean	,
				m_unjiean
		};
	}

}
