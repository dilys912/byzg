package nc.vo.mo.mo6601;

import nc.ui.trade.base.IBillOperate;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFTime;

public class GlclHeadVO extends SuperVO
{
	private String bz; //����
	private String scx; //������
	private UFDate clrq; //��������
	private Integer zdsl; //��������
	private UFDate phrq; //��������
	private String lh; //�Ϻ�
	private String cpmc; //��Ʒ����
	private String djh; //���ݺ�
	private String clr; //������
	private String lrr; //¼����
	private String pk_glcl; //����
	private String pk_corp; //��ǰ��˾
	private Integer djzt; //����״̬
	private Integer vbillStatus=0;
	private String cinventoryid;//��ƷID
    private UFTime xxsj;
	private UFDate logindate;//��¼����
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -5935538994230803914L;

	@Override
	public String getParentPKFieldName()
	{
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public String getPKFieldName()
	{
		// TODO Auto-generated method stub
		return "pk_glcl";
	}

	@Override
	public String getTableName()
	{
		// TODO Auto-generated method stub
		return "mm_glcl";
	}

	public String getBz()
	{
		return bz;
	}

	public void setBz(String bz)
	{
		this.bz = bz;
	}

	public String getScx()
	{
		return scx;
	}

	public void setScx(String scx)
	{
		this.scx = scx;
	}

	public UFDate getClrq()
	{
		return clrq;
	}

	public void setClrq(UFDate clrq)
	{
		this.clrq = clrq;
	}

	public Integer getZdsl()
	{
		return zdsl;
	}

	public void setZdsl(Integer zdsl)
	{
		this.zdsl = zdsl;
	}

	public UFDate getPhrq()
	{
		return phrq;
	}

	public void setPhrq(UFDate phrq)
	{
		this.phrq = phrq;
	}

	public String getLh()
	{
		return lh;
	}

	public void setLh(String lh)
	{
		this.lh = lh;
	}

	public String getCpmc()
	{
		return cpmc;
	}

	public void setCpmc(String cpmc)
	{
		this.cpmc = cpmc;
	}

	public String getDjh()
	{
		return djh;
	}

	public void setDjh(String djh)
	{
		this.djh = djh;
	}

	public String getClr()
	{
		return clr;
	}

	public void setClr(String clr)
	{
		this.clr = clr;
	}

	public String getLrr()
	{
		return lrr;
	}

	public void setLrr(String lrr)
	{
		this.lrr = lrr;
	}

	public String getPk_glcl()
	{
		return pk_glcl;
	}

	public void setPk_glcl(String pk_glcl)
	{
		this.pk_glcl = pk_glcl;
	}

	public String getPk_corp()
	{
		return pk_corp;
	}

	public void setPk_corp(String pk_corp)
	{
		this.pk_corp = pk_corp;
	}

	public Integer getDjzt()
	{
		return djzt;
	}

	public void setDjzt(Integer djzt)
	{
		this.djzt = djzt;
	}

	public Integer getVbillStatus()
	{
		if(this.getDjzt()==1)
		{
			return IBillOperate.OP_NO_ADDANDEDIT;
		}
		return 8;
	}

	public void setVbillStatus(Integer vbillStatus)
	{
		this.vbillStatus = vbillStatus;
	}
	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}
	public UFDate getLogindate() {
		return logindate;
	}

	public void setLogindate(UFDate logindate) {
		this.logindate = logindate;
	}
	public UFTime getXxsj() {
		return xxsj;
	}

	public void setXxsj(UFTime xxsj) {
		this.xxsj = xxsj;
	}

}
