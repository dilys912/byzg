package nc.ui.report;

import javax.swing.SpringLayout;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;

public class ReportQP extends UIPanel {
	
	private UITextField period  = null;	// 期间
	private UITextField year  = null;	// 年度
	
	private UIRefPane wd_ref = null;	// 网点
	private UIRefPane xsy_ref = null;	// 销售员
	private UIRefPane bDate_ref = null;	// 开始日期
	private UIRefPane eDate_ref = null;	// 结束日期
	private UICheckBox isxj_ckb = null;	// 是否包含下级公司
	
	public ReportQP()
	{
		super();
		initlize();	// 初始化界面
		initData();	// 初始化数据
	}

	/**
	 * 初始化数据
	 */
	public void initData()
	{
		//getBDate_ref().setValue( ClientEnvironment.getInstance().getDate().toString() );	// 开始日期 = 当前日期
		//getEDate_ref().setValue( ClientEnvironment.getInstance().getDate().toString() );
		getperiod_txt().setText(ClientEnvironment.getInstance().getAccountMonth().toString());
		getyear_txt().setText(ClientEnvironment.getInstance().getAccountYear().toString());
		
	}
	
	/**
	 * 初始化界面
	 */
	private void initlize()
	{
		this.setLayout( new SpringLayout() );
		this.add(getyear_lab());
		this.add(getyear_txt());
		//this.add(getlab1());
		//this.add(getlab2());
		//this.add( new UIPanel() );
		this.add(getperiod_lab());
		this.add(getperiod_txt());
		
		//this.add( getWd_ref_Lab() ); 
		//this.add( getWd_ref() );
		//this.add( getXsy_ref_Lab() );
		//this.add( getXsy_ref() );
		
		//this.add( getBDate_ref_Lab() );
		//this.add( getBDate_ref() );
		//this.add( getEDate_ref_Lab() );
		//this.add( getEDate_ref() );
		
		//this.add( new UIPanel() );
		//this.add( getIsxj_ckb() );
		this.add( new UIPanel() );
		this.add( new UIPanel() );
		
		nc.ui.pub.beans.layout.SpringUtilities.makeCompactGrid(this, 1, 4, 5, 5, 5, 5);
		//nc.ui.pub.beans.layout.SpringUtilities.makeCompactGrid(this,  3, 4, 5, 5, 5, 5);
		
	}
	/*
	*/
	public UILabel getperiod_lab()
	{
		return new UILabel("期间");
	}
	/*
	*/
	public UITextField getperiod_txt()
	{
		if( period == null )
		{
			period = new UITextField();
			//period.setTextType("TextInt");
			//period.setText(ClientEnvironment.getInstance().getAccountMonth());
		}
		return period;
	}
	public UILabel getyear_lab()
	{
		return new UILabel("年度");
	}
	/*
	*/
	public UITextField getyear_txt()
	{
		if( year == null )
		{
			year = new UITextField();
			//period.setTextType("TextInt");
			//period.setText(ClientEnvironment.getInstance().getAccountMonth());
		}
		return year;
	}
	public UILabel getlab1()
	{
		return new UILabel("");
	}
	public UILabel getlab2()
	{
		return new UILabel("");
	}
	public UILabel getlab3()
	{
		return new UILabel("");
	}
	/**
	 * 网点
	 */
	public UILabel getWd_ref_Lab()
	{
		return new UILabel("网点");
	}
	public UIRefPane getWd_ref()
	{
		if( wd_ref == null )
		{
			wd_ref = new UIRefPane();
//			wd_ref.setRefNodeName("网点编码");
			//wd_ref.setRefModel( new WdRefModel("网点编码") );
		}
		return wd_ref;
	}
	/**
	 * 销售员
	 */
	public UILabel getXsy_ref_Lab()
	{
		return new UILabel("销售员");
	}
	public UIRefPane getXsy_ref()
	{
		if( xsy_ref == null )
		{
			xsy_ref = new UIRefPane();
			xsy_ref.setRefNodeName("人员档案");
		}
		return xsy_ref;
	}
	/**
	 * 开始日期
	 */
	public UILabel getBDate_ref_Lab()
	{
		return new UILabel("开始日期");
	}
	public UIRefPane getBDate_ref()
	{
		if( bDate_ref == null )
		{
			bDate_ref = new UIRefPane();
			bDate_ref.setRefNodeName("日历");
			
		}
		return bDate_ref;
	}
	/**
	 * 结束日期
	 */
	public UILabel getEDate_ref_Lab()
	{
		return new UILabel("结束日期");
	}
	public UIRefPane getEDate_ref()
	{
		if( eDate_ref == null )
		{
			eDate_ref = new UIRefPane();
			eDate_ref.setRefNodeName("日历");
			
		}
		return eDate_ref;
	}
	/**
	 * 是否包含下级公司
	 */
//	public UILabel getIsxj_ckb_Lab()
//	{
//		return new UILabel("是否包含下级公司");
//	}
	public UICheckBox getIsxj_ckb()
	{
		if( isxj_ckb == null )
		{
			isxj_ckb = new UICheckBox("是否包含下级公司");
		}
		return isxj_ckb;
	}
	
}
