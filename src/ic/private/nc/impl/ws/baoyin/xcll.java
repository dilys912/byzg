package nc.impl.ws.baoyin;

import java.util.*;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.itf.ws.baoyin.Ixcl;

public class xcll implements Ixcl {

	public String sayXcl(String pk_corp, String invcode,
			String storcode) {
		// TODO Auto-generated method stub		
			String nonhandnum=""; 		
		StringBuffer sql = new StringBuffer();
		sql.append(" select nonhandnum ") 
		.append("   from ic_onhandnum x ") 
		.append("   left join bd_invbasdoc c ") 
		.append("     on x.cinvbasid = c.pk_invbasdoc ") 
		.append("   left join bd_stordoc b ") 
		.append("     on x.cwarehouseid = b.pk_stordoc ") 
		.append("  where c.invcode = '" + invcode+ "' ") 
		.append("    and b.storcode = '" + storcode+ "' ") 
		.append("    and x.pk_corp='" + pk_corp+ "'; ") ;
		List list =  new ArrayList();
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		for(int i=0;i<list.size();i++){
			nonhandnum=list.get(i).toString();
		}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nonhandnum;
	}

}
