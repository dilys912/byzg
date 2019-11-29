package nc.vo.mo.mo6601;

import nc.vo.pub.SuperVO;

public class GlclItemVO extends SuperVO
{
	private String lydj; //������Դ
	private String ysdh; //ԭʼ���
	private Integer dclsl;//����������
	private Integer hgsl; //�ϸ�����
	private Integer bfsl; //��������
	private String cldh;  //������
	private String clph; //��������
	private Integer hclhgsl; //����ϸ�����
	private Integer hclbfsl; //����������
	private String hcldh;  //������
	private String hclph;  //��������
	private String hclhw ; //�����λ
	private String ysph; //ԭʼ����
	private String ck ;  //�ֿ�
	private String hw; //��λ
	private String pk_glcl; //����PK
	private String pk_glcl_b; //����
	private String pk_glzb_b;// ���뵥��������
	private String pk_glzb; //���뵥����
	private String lydjlx; //��Դ��������
	private Integer clzt ; // ����״̬
	private String cspaceid;//��λID
	private String pk_corp; //��ǰ��˾
	
	

	

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
