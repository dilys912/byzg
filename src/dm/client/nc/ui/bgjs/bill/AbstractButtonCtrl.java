/*
 * $Id: AbstractButtonCtrl.java,v 1.1 2012/02/13 06:29:18 zhwj Exp $
 * 功能：
 * 履历：2008-8-6 创建 BY Answer
 */
package nc.ui.bgjs.bill;

import nc.ui.pub.ButtonObject;


public abstract class AbstractButtonCtrl {
	protected ButtonObject[] m_boButtonGroup;
	
	/**
	 * 构造函数
	 * 
	 */
	public AbstractButtonCtrl() {
		initbutt();
	}
	
	public ButtonObject[] getButtonGroup(){
		return m_boButtonGroup;
	}

	/**
	 * 功能：
	 *  
	 */
	public abstract void initbutt();
	
	/**
	 * 功能：将给定的按纽组设置是否显示
	 * 
	 * @param boButtonGroup
	 * @param isVisible 
	 */
	protected void setButtonGroupStatus(ButtonObject[] boButtonGroup,Boolean isVisible){
		if(boButtonGroup!=null&&boButtonGroup.length>0){
			for(int i=0;i<boButtonGroup.length;i++){
				boButtonGroup[i].setVisible(isVisible);
			}
		}
	}
	
	/**
	 * 功能：将给定的按纽组设置是否可用
	 * 
	 * @param boButtonGroup
	 * @param isEnabled 
	 */
	public void initButtonGroupEnabled(ButtonObject[] boButtonGroup,Boolean isEnabled){
		if(boButtonGroup!=null&&boButtonGroup.length>0){
			for(int i=0;i<boButtonGroup.length;i++){
				boButtonGroup[i].setEnabled(isEnabled);
			}
		}
	}
}
