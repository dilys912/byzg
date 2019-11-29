/*
 * $Id: LorealProxy.java,v 1.1 2012/02/13 06:31:39 zhwj Exp $
 * ���ܣ�
 * ������2008-1-31 ���� BY answer
 */
package nc.vo.bgjs.proxy;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.naming.Context;
import nc.itf.bgjs.pub.IBGJSITF;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;

/**
 * @author 
 * @������  : BgjsProxy
 * @���� : ģ�������,�ӿڵ����ڴ�����
 * @�汾 : v1.0
 */
public class BgjsProxy {
	private static BgjsProxy m_luckosProxy=new BgjsProxy();
	/**
	 * 
	 */
	private BgjsProxy() {
		super();
	}
	/**
	 * <H3>����һ��ģ�������ʵ��</H3><BR>
	 * 
	 * @return
	 */
	public static BgjsProxy getInstance() {
		return m_luckosProxy;
	}
	/**
	 * <H3>��������</H3>TODO<BR>
	 * 
	 * @return
	 */
	public Context getLocator() {
		return NCLocator.getInstance();
	}
	/**
	 * <H3>���ò�ѯ������</H3><BR>
	 * 
	 * @return
	 */
	public IUAPQueryBS getIUAPQueryBS(){
	 	return (IUAPQueryBS) getLocator().lookup(IUAPQueryBS.class.getName());  
	}
	/**
	 * <H3>���õ������ݿ������</H3><BR>
	 * 
	 * @return
	 */
	public IVOPersistence getIVOPersistence(){
	 	return (IVOPersistence) getLocator().lookup(IVOPersistence.class.getName());  
	}
	/**
	 * <H3>ҵ��ƽ̨������</H3><BR>
	 * 
	 * @return
	 */
	public IUifService getIUifService(){ 
	 	return (IUifService) getLocator().lookup(IUifService.class.getName());  
	}
	
	/**
	 * <H3>ŷ����ҵ�������</H3><BR>
	 * 
	 * @return
	 */
	public IBGJSITF getIBGJSITF(){ 
	 	return (IBGJSITF) getLocator().lookup(IBGJSITF.class.getName());  
	}
	
}
