package nc.bs.pub.action;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.trade.business.HYPubBO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class N_53_JCGL extends AbstractCompiler2
{
	public Object runComClass(PfParameterVO vo)
    throws BusinessException
	 {
		GlBillVO billVO =  (GlBillVO) vo.m_preValueVo;
		//�޸ı��嵥��״̬Ϊ�Ѵ���
		GlItemBVO[] items = (GlItemBVO[]) billVO.getChildrenVO();
		for(GlItemBVO item :items)
		{
			item.setClzt(1);
			item.setStatus(VOStatus.UPDATED);
		}  
		HYPubBO bo = new HYPubBO();
		bo.saveBill(billVO);
		
		//�ⶳ����
		jdcl(billVO);
		return billVO;
	 }
	
	
	
	@SuppressWarnings("restriction")
	protected void jdcl(GlBillVO billVO) throws BusinessException
	{
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();
		GlItemBVO[] items = (GlItemBVO[]) billVO.getChildrenVO();
		FreezeVO[] freezes = new FreezeVO[items.length];
		HYPubBO dao = new HYPubBO();
		for(int i=0;i<items.length;i++)
		{
			FreezeVO free = new FreezeVO(); 
			
			free.setPk_corp(head.getPk_corp());  //��Ӧ��˾
			free.setCwarehouseid(items[i].getIsolationckid()); //�ֿ�ID
			free.setCspaceid(items[i].getHw());//��λ
			free.setCinventoryid(items[i].getIsolationcpid());// ���ID
			free.setVbatchcode(items[i].getPh()); //����
			free.setNdefrznum(new UFDouble(items[i].getXxaglsl()));// �������� 
			
			free.setCthawpersonid(InvocationInfoProxy.getInstance().getUserCode()); //�ⶳ��ID
			free.setDthawdate(new UFDate(System.currentTimeMillis())); //�ⶳʱ��
			
			free.setInvname(items[i].getCp());  //��Ʒ���� 
			free.setPrimaryKey(items[i].getPk_freeze());
//			free.setStatus(VOStatus.NEW);
			String ts = (String)dao.findColValue("IC_FREEZE", "ts", " CFREEZEID='"+free.getPrimaryKey()+"'");
			free.setTs(new UFDateTime(ts));
						
//			HYPubBO pubbo = new HYPubBO();
//			String cinvbasid = (String)pubbo.findColValue("bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"+free.getCinventoryid()+"'");
//			free.setCinvbasid(cinvbasid);
//			String calbodyid = (String)pubbo.findColValue("BD_STORDOC", "pk_calbody", " PK_STORDOC='"+free.getCwarehouseid()+"'");
//			free.setCcalbodyid(calbodyid);
		 			
			freezes[i] = free;
		}
		nc.bs.ic.pub.freeze.FreezeBO bo = new nc.bs.ic.pub.freeze.FreezeBO(); 
		bo.unfreezeInv(freezes);
	}
}
