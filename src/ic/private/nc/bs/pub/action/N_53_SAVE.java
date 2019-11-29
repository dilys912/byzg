package nc.bs.pub.action;

import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.trade.business.HYPubBO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class N_53_SAVE  extends AbstractCompiler2
{
	 public Object runComClass(PfParameterVO vo)
     throws BusinessException
	 {
		 
		 GlBillVO billVO = (GlBillVO) vo.m_preValueVo;
		 //���� VO
		 List pks = DongJie(billVO);
		 
		//���浥��
		 saveBill(billVO,pks);
	     return billVO;
	 }
	 
	 //������
	 @SuppressWarnings("restriction")
	protected List DongJie(GlBillVO billVO) throws BusinessException
	{  
		FreezeVO frees[] = new FreezeVO[billVO.getChildrenVO().length];
		GlItemBVO item = null;
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();
		for (int i = 0; i < frees.length; i++) {
			item =  (GlItemBVO) billVO.getChildrenVO()[i];
			FreezeVO free = new FreezeVO();
			free.setPk_corp(head.getPk_corp()); //��˾
			free.setCwarehouseid(item.getIsolationckid()); //�ֿ�ID
			free.setCspaceid(item.getHw());//��λ
			free.setCinventoryid(item.getIsolationcpid());// ���ID
			free.setVbatchcode(item.getPh()); //����
			free.setDvalidate(null);
			free.setNfreezenum(new UFDouble(item.getXxaglsl()));  //��������
			free.setCastunitid(null);
			free.setNfreezeastnum(null);
			free.setNgrossnum(null);
			free.setVfree1(item.getDh()); //���
			
			free.setCcorrespondtype("53");
			free.setCcorrespondhid(head.getPrimaryKey());
			free.setCcorrespondbid(item.getPrimaryKey());
			free.setCcorrespondcode(null);

			free.setCfreezerid(InvocationInfoProxy.getInstance().getUserCode()); //������
			free.setDtfreezetime(new UFDate(System.currentTimeMillis())); //��������
			
			free.setDthawdate(null);
			free.setCthawpersonid(null);
			
			free.setInvname(item.getCp()); //��Ʒ����
			
			HYPubBO pubbo = new HYPubBO();
			String cinvbasid = (String)pubbo.findColValue("bd_invmandoc", "pk_invbasdoc", " pk_invmandoc='"+free.getCinventoryid()+"'");
			free.setCinvbasid(cinvbasid);
			String calbodyid = (String)pubbo.findColValue("BD_STORDOC", "pk_calbody", " PK_STORDOC='"+free.getCwarehouseid()+"'");
			free.setCcalbodyid(calbodyid);
		 
			free.setDunlockdate(null);
			free.setHsl(null);
			
			free.setDr(new Integer(0));
			free.setStatus(2);

			frees[i] = free;
		}  
		nc.bs.ic.pub.freeze.FreezeBO bo = new nc.bs.ic.pub.freeze.FreezeBO();
		return bo.freezeInv(frees);
	}
	 
	 protected GlBillVO saveBill(GlBillVO billVO,List pks) throws BusinessException
	 {
		  HYPubBO pubbo = new HYPubBO();
			
			//ɾ����
			GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
			GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
			for(int i=0;i<vos.length;i++)
			{
				 vos[i].setPk_freeze((String)pks.get(i));
			}
			
			//���ܵĸ��´���
//			String pkGlzb = ghead.getPk_glzb(); 
//			if(pkGlzb!=null&&!pkGlzb.trim().equals(""))
//			{
//				GlItemBVO[] old = (GlItemBVO[])pubbo.queryByCondition(GlItemBVO.class,"   pk_glzb='"+pkGlzb+"'");
//				boolean flag ;
//				if(old!=null&&old.length>0)
//				{
//					for(int i=0;i<old.length;i++)
//					{
//						flag = false;
//						for(int j=0;j<vos.length;j++)
//						{
//							if(vos[j].getPk_glzb_b().equals(old[i].getPk_glzb_b()))
//							{
//								flag = true;
//								break;
//							}
//						}
//						if(!flag)
//						{
//							pubbo.delete(old[i]);
//						}
//					}
//				}
//			}
			//���浥��
			billVO = (GlBillVO) pubbo.saveBill(billVO);
			
			return billVO;
	 }
}
