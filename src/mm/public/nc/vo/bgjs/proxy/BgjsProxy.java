/*
 * $Id: LorealProxy.java,v 1.1 2012/02/13 06:31:39 zhwj Exp $
 * 功能：
 * 履历：2008-1-31 创建 BY answer
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
 * @类型名  : BgjsProxy
 * @功能 : 模块代理类,接口调用在此增加
 * @版本 : v1.0
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
	 * <H3>返回一个模块代理类实例</H3><BR>
	 * 
	 * @return
	 */
	public static BgjsProxy getInstance() {
		return m_luckosProxy;
	}
	/**
	 * <H3>方法作用</H3>TODO<BR>
	 * 
	 * @return
	 */
	public Context getLocator() {
		return NCLocator.getInstance();
	}
	/**
	 * <H3>公用查询代理类</H3><BR>
	 * 
	 * @return
	 */
	public IUAPQueryBS getIUAPQueryBS(){
	 	return (IUAPQueryBS) getLocator().lookup(IUAPQueryBS.class.getName());  
	}
	/**
	 * <H3>公用单表数据库代理类</H3><BR>
	 * 
	 * @return
	 */
	public IVOPersistence getIVOPersistence(){
	 	return (IVOPersistence) getLocator().lookup(IVOPersistence.class.getName());  
	}
	/**
	 * <H3>业务平台代理类</H3><BR>
	 * 
	 * @return
	 */
	public IUifService getIUifService(){ 
	 	return (IUifService) getLocator().lookup(IUifService.class.getName());  
	}
	
	/**
	 * <H3>欧莱雅业务代理类</H3><BR>
	 * 
	 * @return
	 */
	public IBGJSITF getIBGJSITF(){ 
	 	return (IBGJSITF) getLocator().lookup(IBGJSITF.class.getName());  
	}
	
}
