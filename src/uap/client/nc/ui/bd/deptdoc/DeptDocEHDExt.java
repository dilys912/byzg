package nc.ui.bd.deptdoc;


import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.rino.pda.BasicdocVO;
import nc.vo.trade.button.ButtonVO;

public class DeptDocEHDExt extends nc.ui.bd.deptdoc.DeptDocEHD {

	
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	IUifService iserviceDao = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
	int billstatus = 0;
	
	public DeptDocEHDExt(BillTreeCardUI billUI, ICardController control,
			boolean isHrUse, ButtonVO hrFatherVO) {
		super(billUI, control, isHrUse, hrFatherVO);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// edit by yhj 2014-02-23
		CircularlyAccessibleValueObject billvo = getBillUI().getChangedVOFromUI().getParentVO();
		super.onBoSave();
		 /**************************add by yhj 2014-02-23 START*******************************/
        if (billstatus == 1) {// 新增保存操作
        	String primarykey = getBufferData().getCurrentVO().getParentVO().getPrimaryKey();
			if (billvo != null) {
				DeptdocVO deptvo = (DeptdocVO) billvo;
				BasicdocVO bvo = new BasicdocVO();
					if(deptvo.getMemo() != null && deptvo.getMemo().startsWith("PDA")){
						bvo.setBdname(deptvo.getDeptname());
						bvo.setBdid(primarykey);
						bvo.setBdtype("BM");
						bvo.setPk_corp(deptvo.getPk_corp());
						bvo.setProctype("add");
						bvo.setSysflag("Y");
					}
				iserviceDao.insert(bvo);
			}
		}else if (billstatus == 2) {// 修改保存操作
			if(billvo != null ){
					DeptdocVO svo =  (DeptdocVO) billvo;
					String sql = "select * from pda_basicdoc where bdid='" + svo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'"; 
					BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
					boolean pdaFlag = svo.getMemo() != null && svo.getMemo().startsWith("PDA");
					if (checkVO == null && pdaFlag) {
						BasicdocVO vo = new BasicdocVO();
						vo.setBdname(svo.getDeptname());
						vo.setBdid(svo.getPrimaryKey());
						vo.setBdtype("BM");
						vo.setPk_corp(svo.getPk_corp());
						vo.setProctype("add");
						vo.setSysflag("Y");
						iserviceDao.insert(vo);
					}else if (checkVO != null && !checkVO.getBdname().equals(svo.getDeptname()) && pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setBdname(svo.getDeptname());
						iserviceDao.update(checkVO);
					} else if (checkVO != null && !pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setProctype("delete");
						iserviceDao.update(checkVO);
					}
			}
		}
        /**************************add by yhj 2014-02-23 END*******************************/
		
	}
	/**
	 * edit by yhj 2014-02-23
	 */
	@Override
	protected void onBoDelete() throws Exception {
    	String primarykey = getBufferData().getCurrentVO().getParentVO().getPrimaryKey();
        
    	super.onBoDelete();
    	
    	iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid is null and sysflag='Y' and bdname='"+primarykey+"'");
        String sql = "select * from pda_basicdoc where bdid='" + primarykey + "' and nvl(dr,0)=0 and sysflag='Y'";
        BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
        if(checkVO != null){
        	iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + primarykey + "' and sysflag='N'");
        	checkVO.setProctype("delete");
        	iserviceDao.update(checkVO);
        }
	}

	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		billstatus = 1;
		super.onBoAdd(bo);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		billstatus = 2;
		super.onBoEdit();
	}
	

}
