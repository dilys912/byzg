/*
 * $Id: AbstractClientUI.java,v 1.1 2012/02/13 06:29:18 zhwj Exp $
 * ���ܣ�
 * ������2008-8-6 ���� BY Answer
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
	private ClientEnvironment m_ceSingleton =null;// ϵͳ��������
	//��˾Id
	private String m_pkcorp = null;
	//������id
	private String m_operator = null;
	//����
	private UFDate m_ddate = null;
	//����+ʱ��
	private String m_sdatetime=null;
	//�����ʱ�
	private String m_pkGlOrgBook=null;
	
	//����״̬
	protected static final int noEditState=0;
	protected static final int editState=1;
	protected static final int initState=2;
	
	
	
	/**
	 * 
	 */
	public AbstractClientUI() {
		// TODO �Զ����ɹ��캯�����
		super();
		
	}

	/* ���� Javadoc��
	 * @see nc.ui.pub.ToftPanel#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO �Զ����ɷ������
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO �Զ����ɷ������

	}
	
	
	/*******************ϵͳ��Ϣ*************************/
	/**
	 * <H3>��������</H3>����Ա<BR>
	 * 
	 * @return
	 */
	protected String getOperator(){
		if (m_operator==null)
			m_operator=getClientEnvironment().getUser().getPrimaryKey();
		return m_operator;
	}
	/**
	 * <H3>��������</H3>��˾<BR>
	 * 
	 * @return
	 */
	protected String getPkCorp(){
		if (m_pkcorp==null)
			m_pkcorp=getClientEnvironment().getCorporation().getPrimaryKey();
		return m_pkcorp;
	}
	/**
	 * <H3>��������</H3>����<BR>
	 * 
	 * @return
	 */
	protected UFDate getDdate(){
		if (m_ddate==null)
			m_ddate=getClientEnvironment().getDate();
		return m_ddate;
	}
	/**
	 * ��õ�ǰ�Ļ�����Ϣ��
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
	 * <H3>��������</H3>����+ʱ��<BR>
	 * 
	 * @return
	 */
	protected String getSdatetime(){
		if (m_sdatetime==null)
			m_sdatetime=ClientEnvironment.getServerTime().toString();
		return m_sdatetime;
	}
	/**
	 * <H3>��������</H3>TODO<BR>
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
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable ex) {
		ex.printStackTrace();
		Debug.error(ex.getMessage());
		showErrorMessage(ex.getMessage());
	}
	/*******************ϵͳ��Ϣ*************************/
	
	/********************����**************************/
	public abstract void initialize();
	public abstract void addListenerEvent();
	public abstract void initButton();
	public abstract UIPanel getMainPanel();
	/********************����**************************/
}
