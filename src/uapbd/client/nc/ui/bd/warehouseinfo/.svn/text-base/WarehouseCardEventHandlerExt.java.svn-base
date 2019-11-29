package nc.ui.bd.warehouseinfo;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.bd.warehouseinfo.StordocVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.rino.pda.BasicdocVO;

/**
 * 仓库基本档案EventHandler扩展类（PDA）
 * 
 * @author yhj 2014-02-19
 * 
 */
public class WarehouseCardEventHandlerExt extends nc.ui.bd.warehouseinfo.WarehouseCardEventHandler {

	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	IUifService iserviceDao = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());

	public WarehouseCardEventHandlerExt(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		billstatus = 1;// 新增状态：1
		super.onBoAdd(bo);
	}

	int billstatus = 0;

	@Override
	protected void onBoSave() throws Exception {

		int rowno = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		CircularlyAccessibleValueObject[] billVOs = getBillUI().getChangedVOFromUI().getChildrenVO();
		super.onBoSave();
		/**
		 * add by yhj 20140-02-19
		 */
		String newpk = getBufferData().getCurrentVO().getChildrenVO()[rowno].getPrimaryKey();
		if (billstatus == 1) {// 新增保存操作
			StordocVO storvo = null;
			if (billVOs != null && billVOs.length > 0) {
				//				BasicdocVO[] voarrays = new BasicdocVO[billVOs.length];
				List<BasicdocVO> volist = new ArrayList<BasicdocVO>();
				BasicdocVO bvo = null;
				for (int i = 0; i < billVOs.length; i++) {
					bvo = new BasicdocVO();
					storvo = (StordocVO) billVOs[i];
					if (storvo.getDef5() != null && storvo.getDef5().trim().equals("PDA_WK")) {
						bvo.setBdname(storvo.getStorname());
						//						String sql = "select pk_stordoc from bd_stordoc where nvl(dr,0) = 0 and pk_corp='"+storvo.getPk_corp()+"' and storname='"+storvo.getStorname()+"'";
						//						String newpk = (String)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
						bvo.setBdid(newpk);
						bvo.setBdtype("CK");
						bvo.setPk_corp(storvo.getPk_corp());
						bvo.setProctype("add");
						bvo.setSysflag("Y");
						//						voarrays[i] = bvo;
						bvo.setDef1("外库");
						volist.add(bvo);
					} else if (storvo.getDef5() != null && storvo.getDef5().trim().equals("PDA_NK")) {
						bvo.setBdname(storvo.getStorname());
						//						String sql = "select pk_stordoc from bd_stordoc where nvl(dr,0) = 0 and pk_corp='"+storvo.getPk_corp()+"' and storname='"+storvo.getStorname()+"'";
						//						String newpk = (String)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
						bvo.setBdid(newpk);
						bvo.setBdtype("CK");
						bvo.setPk_corp(storvo.getPk_corp());
						bvo.setProctype("add");
						bvo.setDef1("内库");
						bvo.setSysflag("Y");
						//						voarrays[i] = bvo;
						volist.add(bvo);
					}
					continue;
				}
				final int size = volist.size();
				BasicdocVO[] arr = volist.toArray(new BasicdocVO[size]);
				iserviceDao.insertAry(arr);
			}
		} else if (billstatus == 2) {// 修改保存操作
			if (billVOs != null && billVOs.length > 0) {
				for (int i = 0; i < billVOs.length; i++) {
					StordocVO svo = (StordocVO) billVOs[i];
					String sql = "select * from pda_basicdoc where bdid='" + svo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
					BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
					boolean pdaFlag = svo.getDef5() != null && (svo.getDef5().trim().equals("PDA_WK") || svo.getDef5().trim().equals("PDA_NK"));
					if (checkVO == null && pdaFlag) {
						BasicdocVO vo = new BasicdocVO();
						vo.setBdname(svo.getStorname());
						vo.setBdid(svo.getPrimaryKey());
						vo.setBdtype("CK");
						vo.setPk_corp(svo.getPk_corp());
						vo.setProctype("add");
						vo.setSysflag("Y");
						vo.setDef1(svo.getDef5().trim().equals("PDA_WK") ? "外库" : "内库");
						iserviceDao.insert(vo);
					} else if (checkVO != null && !checkVO.getBdname().equals(svo.getStorname()) && pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setProctype("update");
						checkVO.setBdname(svo.getStorname());
						iserviceDao.update(checkVO);
					} else if (checkVO != null && !pdaFlag) {
						iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + svo.getPrimaryKey() + "' and sysflag='N'");
						checkVO.setProctype("delete");
						iserviceDao.update(checkVO);
					}
				}
			}
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		billstatus = 2;// 修改状态：2
		super.onBoEdit();
	}

	/**
	 * edit by yhj 2013-02-19
	 */
	@Override
	protected void onBoDelete() throws Exception {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		StordocVO vo = (StordocVO) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodyValueRowVO(row, nc.vo.bd.warehouseinfo.StordocVO.class.getName());
		super.onBoDelete();
		if (vo != null) {
			String sql = "select * from pda_basicdoc where bdid='" + vo.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
			BasicdocVO checkVO = (BasicdocVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BasicdocVO.class));
			if (checkVO != null) {
				iserviceDao.deleteByWhereClause(BasicdocVO.class, "bdid='" + vo.getPrimaryKey() + "' and sysflag='N'");
				checkVO.setProctype("delete");
				iserviceDao.update(checkVO);
			}
		}
	}

}
