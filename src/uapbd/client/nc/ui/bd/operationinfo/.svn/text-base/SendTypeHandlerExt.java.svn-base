package nc.ui.bd.operationinfo;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.bd.operationinfo.SendtypeVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.rino.pda.BasicdocVO;

public class SendTypeHandlerExt extends nc.ui.bd.operationinfo.SendTypeHandler{

	
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	IUifService iserviceDao = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
	int billstatus = 0;
	
	public SendTypeHandlerExt(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		billstatus = 2;
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		billstatus = 1;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// add by yhj 2014-02-23
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	CircularlyAccessibleValueObject[] billVOs = getBillUI().getChangedVOFromUI().getChildrenVO();
		super.onBoSave();
		 /**************************add by yhj 2014-02-23 START*******************************/
        String primarykey = getBufferData().getCurrentVO().getChildrenVO()[row].getPrimaryKey();
        if (billstatus == 1) {// 新增保存操作
			SendtypeVO sendvo = null;
			if (billVOs != null && billVOs.length > 0) {
				List<BasicdocVO> volist = new ArrayList<BasicdocVO>();
				BasicdocVO bvo = null;
				for (int i = 0; i < billVOs.length; i++) {
					bvo = new BasicdocVO();
					sendvo = (SendtypeVO) billVOs[i];
					if(sendvo.getMemo() != null && sendvo.getMemo().startsWith("PDA")){
						bvo.setBdname(sendvo.getSendname());
						bvo.setBdid(primarykey);
						bvo.setBdtype("FYFS");
						bvo.setPk_corp(sendvo.getPk_corp());
						bvo.setProctype("add");
						bvo.setSysflag("Y");
						volist.add(bvo);
					}
					continue;
				}
				final int size = volist.size();
				BasicdocVO[] arr = volist.toArray(new BasicdocVO[size]);
				iserviceDao.insertAry(arr);
			}
		}else if (billstatus == 2) {// 修改保存操作
			if(billVOs != null && billVOs.length > 0 ){
				for (int i = 0; i < billVOs.length; i++) {
					SendtypeVO svo =  (SendtypeVO) billVOs[i];
					String sql = "select * from pda_basicdoc where bdid='" + svo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'"; 
					BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
					boolean pdaFlag = svo.getMemo() != null && svo.getMemo().startsWith("PDA");
					if (checkVO == null && pdaFlag) {
						BasicdocVO vo = new BasicdocVO();
						vo.setBdname(svo.getSendname());
						vo.setBdid(svo.getPrimaryKey());
						vo.setBdtype("FYFS");
						vo.setPk_corp(svo.getPk_corp());
						vo.setProctype("add");
						vo.setSysflag("Y");
						iserviceDao.insert(vo);
					}else if (checkVO != null && !checkVO.getBdname().equals(svo.getSendname()) && pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setBdname(svo.getSendname());
						iserviceDao.update(checkVO);
					} else if (checkVO != null && !pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setProctype("delete");
						iserviceDao.update(checkVO);
					}
				}
			}
		}
        /**************************add by yhj 2014-02-23 END*******************************/
		
		
	}
	
	//edit by yhj 2014-02-23
	@Override
	protected void onBoDelete() throws Exception {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    	String primarykey = getBufferData().getCurrentVO().getChildrenVO()[row].getPrimaryKey();
        
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

}
