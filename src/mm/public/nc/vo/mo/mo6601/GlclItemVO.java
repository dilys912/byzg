package nc.vo.mo.mo6601;

import nc.vo.pub.SuperVO;

public class GlclItemVO extends SuperVO
{
	private String lydj; //单据来源
	private String ysdh; //原始垛号
	private Integer dclsl;//待处理数量
	private Integer hgsl; //合格数量
	private Integer bfsl; //报废数量
	private String cldh;  //处理垛号
	private String clph; //处理批号
	private Integer hclhgsl; //后处理合格数量
	private Integer hclbfsl; //后处理报废数量
	private String hcldh;  //后处理垛号
	private String hclph;  //后处理批号
	private String hclhw ; //后处理货位
	private String ysph; //原始批号
	private String ck ;  //仓库
	private String hw; //货位
	private String pk_glcl; //主表PK
	private String pk_glcl_b; //主键
	private String pk_glzb_b;// 隔离单表体主键
	private String pk_glzb; //隔离单主表
	private String lydjlx; //来源单据类型
	private Integer clzt ; // 单据状态
	private String cspaceid;//货位ID
	private String pk_corp; //当前公司
	
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -433320209527895023L;

	@Override
	public String getParentPKFieldName()
	{ 
		// TODO Auto-generated method stub 
		return "pk_glcl";
	}

	@Override
	public String getPKFieldName()
	{
		// TODO Auto-generated method stub
		return "pk_glcl_b";
	}

	@Override
	public String getTableName()
	{
		// TODO Auto-generated method stub
		return "mm_glcl_b";
	}

	public String getLydj()
	{
		return lydj;
	}

	public void setLydj(String lydj)
	{
		this.lydj = lydj;
	}

	public String getYsdh()
	{
		return ysdh;
	}

	public void setYsdh(String ysdh)
	{
		this.ysdh = ysdh;
	}

	public Integer getDclsl()
	{
		return dclsl;
	}

	public void setDclsl(Integer dclsl)
	{
		this.dclsl = dclsl;
	}

	public Integer getHgsl()
	{
		return hgsl;
	}

	public void setHgsl(Integer hgsl)
	{
		this.hgsl = hgsl;
	}

	public Integer getBfsl()
	{
		return bfsl;
	}

	public void setBfsl(Integer bfsl)
	{
		this.bfsl = bfsl;
	}

	public String getCldh()
	{
		return cldh;
	}

	public void setCldh(String cldh)
	{
		this.cldh = cldh;
	}

	public String getClph()
	{
		return clph;
	}

	public void setClph(String clph)
	{
		this.clph = clph;
	}

	public Integer getHclhgsl()
	{
		return hclhgsl;
	}

	public void setHclhgsl(Integer hclhgsl)
	{
		this.hclhgsl = hclhgsl;
	}

	public Integer getHclbfsl()
	{
		return hclbfsl;
	}

	public void setHclbfsl(Integer hclbfsl)
	{
		this.hclbfsl = hclbfsl;
	}

	public String getHcldh()
	{
		return hcldh;
	}

	public void setHcldh(String hcldh)
	{
		this.hcldh = hcldh;
	}

	public String getHclph()
	{
		return hclph;
	}

	public void setHclph(String hclph)
	{
		this.hclph = hclph;
	}

	public String getHclhw()
	{
		return hclhw;
	}

	public void setHclhw(String hclhw)
	{
		this.hclhw = hclhw;
	}

	public String getYsph()
	{
		return ysph;
	}

	public void setYsph(String ysph)
	{
		this.ysph = ysph;
	}

	public String getCk()
	{
		return ck;
	}

	public void setCk(String ck)
	{
		this.ck = ck;
	}

	public String getHw()
	{
		return hw;
	}

	public void setHw(String hw)
	{
		this.hw = hw;
	}

	public String getPk_glcl()
	{
		return pk_glcl;
	}

	public void setPk_glcl(String pk_glcl)
	{
		this.pk_glcl = pk_glcl;
	}

	public String getPk_glcl_b()
	{
		return pk_glcl_b;
	}

	public void setPk_glcl_b(String pk_glcl_b)
	{
		this.pk_glcl_b = pk_glcl_b;
	}

	public String getPk_glzb_b()
	{
		return pk_glzb_b;
	}

	public void setPk_glzb_b(String pk_glzb_b)
	{
		this.pk_glzb_b = pk_glzb_b;
	}

	public String getPk_glzb()
	{
		return pk_glzb;
	}

	public void setPk_glzb(String pk_glzb)
	{
		this.pk_glzb = pk_glzb;
	}

	public String getLydjlx()
	{
		return lydjlx;
	}

	public void setLydjlx(String lydjlx)
	{
		this.lydjlx = lydjlx;
	}

	public Integer getClzt()
	{
		return clzt;
	}

	public void setClzt(Integer clzt)
	{
		this.clzt = clzt;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}
	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
}
