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
		 //冻结 VO
		 List pks = DongJie(billVO);
		 
		//保存单据
		 saveBill(billVO,pks);
	     return billVO;
	 }
	 
	 //冻结库存
	 @SuppressWarnings("restriction")
	protected List DongJie(GlBillVO billVO) throws BusinessException
	{  
		FreezeVO frees[] = new FreezeVO[billVO.getChildrenVO().length];
		GlItemBVO item = null;
		GlHeadVO head = (GlHeadVO) billVO.getParentVO();
		for (int i = 0; i < frees.length; i++) {
			item =  (GlItemBVO) billVO.getChildrenVO()[i];
			FreezeVO free = new FreezeVO();
			free.setPk_corp(head.getPk_corp()); //公司
			free.setCwarehouseid(item.getIsolationckid()); //仓库ID
			free.setCspaceid(item.getHw());//货位
			free.setCinventoryid(item.getIsolationcpid());// 存货ID
			free.setVbatchcode(item.getPh()); //批号
			free.setDvalidate(null);
			free.setNfreezenum(new UFDouble(item.getXxaglsl()));  //冻结数量
			free.setCastunitid(null);
			free.setNfreezeastnum(null);
			free.setNgrossnum(null);
			free.setVfree1(item.getDh()); //垛号
			
			free.setCcorrespondtype("53");
			free.setCcorrespondhid(head.getPrimaryKey());
			free.setCcorrespondbid(item.getPrimaryKey());
			free.setCcorrespondcode(null);

			free.setCfreezerid(InvocationInfoProxy.getInstance().getUserCode()); //冻结人
			free.setDtfreezetime(new UFDate(System.currentTimeMillis())); //冻结日期
			
			free.setDthawdate(null);
			free.setCthawpersonid(null);
			
			free.setInvname(item.getCp()); //产品名称
			
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
			
			//删除行
			GlItemBVO[] vos = (GlItemBVO[]) billVO.getChildrenVO();
			GlHeadVO ghead = (GlHeadVO) billVO.getParentVO();
			for(int i=0;i<vos.length;i++)
			{
				 vos[i].setPk_freeze((String)pks.get(i));
			}
			
			//可能的更新处理
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
			//保存单据
			billVO = (GlBillVO) pubbo.saveBill(billVO);
			
			return billVO;
	 }
}
