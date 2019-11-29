package nc.ui.dm.dm104;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class QueryXclVO extends SuperVO
{
//	"存货编码","存货名称",
//	"规格","型号","计量单位","每垛数量","库存量垛数","库存数量","可出库数量","可出库垛数"};
	private String pk_invbasdoc; //存货PK
	private String invcode; //存货编码
	private String invname; //存货名称
	private String invspec;//规格
	private String invtype;//型号
	private String jldw;//计量单位
	private Integer mdsl;//每垛数量
	private Integer kclds;//库存量垛数
	private UFDouble kcsl;//库存数量
	private UFDouble kcksl;//可出库数量
	private Integer kckds;//可出库垛数
	// add by LGY
	private String  cwarehouseid;//仓库id
	private String  cspaceid;//货位ID
	private String  cwarehousecode ;
	private String  cwarehousename ;
	private String  cspacecode;
	private String  cspacename;
	private boolean ishwflag;//是否货位管理
	private String  pk_corp ;//当前组织
	
	private String vbatchcode;//批次号   2014-7-14  add by zwx
	

	//2014-7-14  add by zwx
	public String getVbatchcode() {
		return vbatchcode;
	}


	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}


	// add by LGY
	@Override
	public String getParentPKFieldName()
	{
		// TODO Auto-generated method stub
		return null;
	}
    

	@Override	public String getPKFieldName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getPk_invbasdoc()
	{
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc)
	{
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getInvcode()
	{
		return invcode;
	}

	public void setInvcode(String invcode)
	{
		this.invcode = invcode;
	}

	public String getInvname()
	{
		return invname;
	}

	public void setInvname(String invname)
	{
		this.invname = invname;
	}

	public String getInvspec()
	{
		return invspec;
	}

	public void setInvspec(String invspec)
	{
		this.invspec = invspec;
	}

	public String getInvtype()
	{
		return invtype;
	}

	public void setInvtype(String invtype)
	{
		this.invtype = invtype;
	}

	public String getJldw()
	{
		return jldw;
	}

	public void setJldw(String jldw)
	{
		this.jldw = jldw;
	}

	public Integer getMdsl()
	{
		return mdsl;
	}

	public void setMdsl(Integer mdsl)
	{
		this.mdsl = mdsl;
	}

	public Integer getKclds()
	{
		return kclds;
	}

	public void setKclds(Integer kclds)
	{
		this.kclds = kclds;
	}

	public UFDouble getKcsl()
	{
		return kcsl;
	}

	public void setKcsl(UFDouble kcsl)
	{
		this.kcsl = kcsl;
	}
 
	public Integer getKckds()
	{
		return kckds;
	}

	public void setKckds(Integer kckds)
	{
		this.kckds = kckds;
	}

	public UFDouble getKcksl()
	{
		return kcksl;
	}

	public void setKcksl(UFDouble kcksl)
	{
		this.kcksl = kcksl;
	}
	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
		
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
		
	}

	public String getCwarehousecode() {
		return cwarehousecode;
	}

	public void setCwarehousecode(String cwarehousecode) {
		this.cwarehousecode = cwarehousecode;
	}

	public String getCwarehousename() {
		return cwarehousename;
	}

	public void setCwarehousename(String cwarehousename) {
		this.cwarehousename = cwarehousename;
	}

	public String getCspacecode() {
		return cspacecode;
	}

	public void setCspacecode(String cspacecode) {
		this.cspacecode = cspacecode;
	}

	public String getCspacename() {
		return cspacename;
	}

	public void setCspacename(String cspacename) {
		this.cspacename = cspacename;
	}
	public boolean StringIsNullOrEmpty(String str)
	{
		if(str==null||str.equals("")||str.equals("null")||str.equals("_________N/A________"))
		{
			return true;
		}
		return false;
	}
	public boolean StringIsNullOrEmpty(Object str)
	{
		if(str==null||str.toString().equals("")||str.toString().equals("null")||str.toString().equals("_________N/A________"))
		{
			return true;
		}
		return false;
	}
	public boolean isIshwflag() {
		return ishwflag;
	}

	public void setIshwflag(boolean ishwflag) {
		this.ishwflag = ishwflag;
	}
	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
    public boolean  StringEquals(String strName [],String strValue []) throws Exception
    {
    	boolean equal=true;
    	boolean curvalue=false;
    	if(strName.length==0)
    	{
    		throw new Exception("Class:querycxlVo,Method:StringEquals,Value:传入空值!");
    	}
    	for(int i=0;i<=strName.length;i++)
    	{
    		if(StringIsNullOrEmpty( getAttributeValue(strName[i])))
    		{
    			equal=(StringIsNullOrEmpty(strValue[i])?true:false);
    			if(!equal)
    			{
    				return equal;
    			}
    		}
    		else if(StringIsNullOrEmpty(strValue[i]))
    		{
    			return false;
    		}
    		else 
    		{
    			equal=String.valueOf(getAttributeValue(strName[i])).equals(strValue[i]);
    			if(!equal)
    			{
    				return equal;
    			}
    		}
    	}
    	return equal;
    }
    public void iniWhAndSpace()
    {
    	if(!StringIsNullOrEmpty(cwarehouseid))
		{
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql="select storcode,storname,csflag from bd_stordoc where pk_stordoc='"+cwarehouseid+"' and dr=0 and pk_corp='"+this.getPk_corp()+"'";
			
			try {
				HashMap hm  =  (HashMap) query.executeQuery(sql, new MapProcessor());
				if(hm.size()>0)
				{
					this.setIshwflag(new UFBoolean(String.valueOf(hm.get("csflag"))).booleanValue());
					this.setCwarehousecode(String.valueOf(hm.get("storcode")));
					this.setCwarehousename(String.valueOf(hm.get("storname")));
				}
				
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	if(!StringIsNullOrEmpty(cspaceid))
		{
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          String sql="select cscode,csname from bd_cargdoc where pk_cargdoc='"+cspaceid+"' and dr=0 ";
			
			try {
				HashMap hm  =  (HashMap) query.executeQuery(sql, new MapProcessor());
				if(hm.size()>0)
				{
				   this.setCspacecode(hm.get("cscode").toString());
				   this.setCspacename(hm.get("csname").toString());
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
