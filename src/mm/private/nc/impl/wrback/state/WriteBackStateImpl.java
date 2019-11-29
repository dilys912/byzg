package nc.impl.wrback.state;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.wrback.state.IWriteBackStateService;
import nc.vo.pub.BusinessException;

public class WriteBackStateImpl implements IWriteBackStateService{

	/**
	 * 生成产品入库，回写合格证打印 是否已生成入库的状态
	 * @author 刘信彬
	 * 2019-08-12
	 */
	public void writeBackRK(String pk_glzb) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		String sql = "update mm_hgz set bsrk='"+"Y"+"'  where  pk_hgz in (select distinct note from mm_glzb where pk_glzb = '"+pk_glzb+"')";
		try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除下线检验单，回写合格证打印 是否已生成下线检验单的状态
	 * @author 刘信彬
	 * 2019-08-12
	 */
	public void delWriteBackXX(String pk_glzb) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		String sql = "update mm_hgz set bsxx='N' where pk_hgz in (select distinct note from mm_glzb where pk_glzb = '"+pk_glzb+"')";
		try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除入库成品单，回写合格证打印 是否已生成入库成品单的状态
	 * @author 刘信彬
	 * 2019-08-12
	 */
	public void delWriteBackRK(String ph,String pk_corp) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		sql.append("update mm_hgz set bsrk='"+"N"+"' where batchnumbercode='"+ph+"' and pk_corp = '"+pk_corp+"' and nvl(dr,0) = 0");
		try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 更新下线检验单与合格证关联关系
	 * @author 刘信彬
	 * 2019-08-12
	 */
	public void updateXX(String pk_hgz,String pk_glzb) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		 sql.append("update mm_glzb set note='"+pk_hgz+"' where pk_glzb='"+pk_glzb+"' and nvl(dr,0) = 0 ");
		try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询批次号是否已经存在
	 * 不存在默认为1
	 * 存在+1
	 */
	public int findpch(String pch,String pk_corp,String pk_hgz) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		sql.append(" select count(batchnumbercode) as num ") 
		.append("   from mm_hgz ") 
		.append("  where batchnumbercode = '"+pch+"' ") 
		.append("    and pk_corp = '"+pk_corp+"' ") 
		.append("    and nvl(dr, 0) = 0 ") ;
		if(pk_hgz.length()>0){
			sql.append("and pk_hgz != '"+pk_hgz+"'");//保存状态不计入
		}
		List list = null;
		try {
			list = ipubdmo.getMapList(sql.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			HashMap map = (HashMap) list.get(0);
			int batchcode = Integer.parseInt(map.get("num").toString());
			return batchcode+1;
		}else{
			return 1;
		}
	}
	
	/**
	 * 批次号存在，查询出整箱垛号，并将垛号进行增加
	 */
	public String findSapcode(String pch,String pk_corp) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		String maxend=null;
		String minend=null;
		 sql.append("select sapcode from mm_hgz   where batchnumbercode='"+pch+"' and pk_corp = '"+pk_corp+"' and nvl(dr,0) = 0");
		try {
		Object[] ob=ipubdmo.getArray(sql.toString());
		
			for(int j=0;j<1;j++){
				String end=ob[j].toString();
				maxend=end.substring(29,32);
				for(int i=1;i<ob.length;i++){
					String end1=ob[i].toString();
					minend=end1.substring(29,32);
					if(Integer.parseInt(minend)>Integer.parseInt(maxend)){
						maxend=minend;
					}
				}
			}
			return maxend;
		
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 更新模板图片加载
	 */
	public void updatePrint(String img, String node, String printcell) {
		StringBuffer sql = new StringBuffer();
		sql.append("  update pub_print_cell set  vvar= '"+img+"'  where ctemplateid  in (select distinct h.ctemplateid from pub_print_template h")
		.append(" where vnodecode = '"+node+"') and  vtext = '"+printcell+"' ");
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		try {
			ipubdmo.executeUpdate(sql.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 根据机台+年份，001-999，如果当年超过999则按照001重新开始
	 */
	public int getSerialNum(String pk_corp,String platnum,String year) {
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		StringBuffer sql=new StringBuffer();
		sql.append(" select count(pk_hgz) as num ") 
		.append("   from mm_hgz ") 
		.append("  where MPLATFORM = '"+platnum+"' ") 
		.append("    and pk_corp = '"+pk_corp+"' ") 
		.append("    and ts like '"+year+"%' ");
		List list = null;
		try {
			list = ipubdmo.getMapList(sql.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			HashMap map = (HashMap) list.get(0);
			int batchcode = Integer.parseInt(map.get("num").toString());
			return batchcode+1;
		}else{
			return 1;
		}
	}
	
}
