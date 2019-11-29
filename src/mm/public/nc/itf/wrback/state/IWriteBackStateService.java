package nc.itf.wrback.state;

public interface IWriteBackStateService {
	public void writeBackRK(String pk_hgz);
	
	public void delWriteBackXX(String bz);
	
	public void delWriteBackRK(String ph,String pk_corp);
	
	public int findpch(String pch,String pk_corp,String pk_hgz);
	
	public String findSapcode(String pch,String pk_corp);
	
	public void updatePrint(String img,String node,String printcell);
	
	public void updateXX(String pk_hgz,String pk_glzb);
	
	public int getSerialNum(String pk_corp,String platnum,String date);
}
