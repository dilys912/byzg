/*
 * $Id: AbstractClientUI.java,v 1.1 2012/02/13 06:29:18 zhwj Exp $
 * 功能：
 * 履历：2008-8-6 创建 BY Answer
 */
package nc.ui.bgjs.bill;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;


public abstract class AbstractClientUI extends ToftPanel {
	private ClientEnvironment m_ceSingleton =null;// 系统环境变量
	//公司Id
	private String m_pkcorp = null;
	//操作人id
	private String m_operator = null;
	//日期
	private UFDate m_ddate = null;
	//日期+时间
	private String m_sdatetime=null;
	//主体帐薄
	private String m_pkGlOrgBook=null;
	
	//单据状态
	protected static final int noEditState=0;
	protected static final int editState=1;
	protected static final int initState=2;
	
	
	
	/**
	 * 
	 */
	public AbstractClientUI() {
		// TODO 自动生成构造函数存根
		super();
		
	}

	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO 自动生成方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO 自动生成方法存根

	}
	
	
	/*******************系统信息*************************/
	/**
	 * <H3>方法作用</H3>操作员<BR>
	 * 
	 * @return
	 */
	protected String getOperator(){
		if (m_operator==null)
			m_operator=getClientEnvironment().getUser().getPrimaryKey();
		return m_operator;
	}
	/**
	 * <H3>方法作用</H3>公司<BR>
	 * 
	 * @return
	 */
	protected String getPkCorp(){
		if (m_pkcorp==null)
			m_pkcorp=getClientEnvironment().getCorporation().getPrimaryKey();
		return m_pkcorp;
	}
	/**
	 * <H3>方法作用</H3>日期<BR>
	 * 
	 * @return
	 */
	protected UFDate getDdate(){
		if (m_ddate==null)
			m_ddate=getClientEnvironment().getDate();
		return m_ddate;
	}
	/**
	 * 获得当前的环境信息。
	 * 
	 * @version (00-6-13 10:51:14)
	 * 
	 * @return ClientEnvironment
	 */
	protected ClientEnvironment getClientEnvironment() {
		if (m_ceSingleton == null) {
			m_ceSingleton = ClientEnvironment.getInstance();
		}
		return m_ceSingleton;
	}
	/**
	 * <H3>方法作用</H3>日期+时间<BR>
	 * 
	 * @return
	 */
	protected String getSdatetime(){
		if (m_sdatetime==null)
			m_sdatetime=ClientEnvironment.getServerTime().toString();
		return m_sdatetime;
	}
	/**
	 * <H3>方法作用</H3>TODO<BR>
	 * 
	 * @return
	 */
	protected String getPkGlOrgBook(){
		if (m_pkGlOrgBook==null){
			ValueObject vo = (ValueObject) ClientEnvironment.getInstance().getValue(ClientEnvironment.GLORGBOOKPK);
			if (vo!=null){
				 try {
					 m_pkGlOrgBook = vo.getPrimaryKey();
	                } catch (BusinessException ex) {
	                	handleException(ex);
	                }
			}
		}
		return m_pkGlOrgBook;
	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable ex) {
		ex.printStackTrace();
		Debug.error(ex.getMessage());
		showErrorMessage(ex.getMessage());
	}
	/*******************系统信息*************************/
	
	/********************监听**************************/
	public abstract void initialize();
	public abstract void addListenerEvent();
	public abstract void initButton();
	public abstract UIPanel getMainPanel();
	/********************监听**************************/
}
