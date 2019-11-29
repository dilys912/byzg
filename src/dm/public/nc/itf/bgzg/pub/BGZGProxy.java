package nc.itf.bgzg.pub;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.naming.Context;
import nc.itf.uap.IUAPQueryBS;

public class BGZGProxy { 
	private static Context getLocator() {
		return NCLocator.getInstance();
	}
	public static IUAPQueryBS getIUAPQueryBS(){
		try{ 
	 		return (IUAPQueryBS) getLocator().lookup(IUAPQueryBS.class.getName());  
	 	}catch(Exception e){ 
	 		e.printStackTrace(); 
	 		return null; 
	 	}
	}
 
}
