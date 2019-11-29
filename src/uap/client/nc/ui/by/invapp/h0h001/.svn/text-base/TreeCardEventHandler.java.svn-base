package nc.ui.by.invapp.h0h001;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.by.invapp.h0h001.INVCLATTRIBUTEVO01;
import nc.vo.by.invapp.h0h001.SHOWVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class TreeCardEventHandler extends nc.ui.trade.treecard.TreeCardEventHandler {

	public TreeCardEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	protected void onBoSave() throws Exception {
		Object pk_invcl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invcl").getValueObject();
		if(isendflog(pk_invcl)){
			if (pk_invcl!=null) {
				String sql = "select * from bd_invcl_attribute where pk_invcl = '"+pk_invcl+"'";
				IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					HYPubBO_Client hy = new HYPubBO_Client();
					List<INVCLATTRIBUTEVO01> list = (List<INVCLATTRIBUTEVO01>) qurey.executeQuery(sql,new BeanListProcessor(INVCLATTRIBUTEVO01.class));
					INVCLATTRIBUTEVO01 hvo = null;
					if (list!=null&&list.size()>0) {
						AggregatedValueObject vo = getBillCardPanelWrapper().getChangedVOFromUI();
						hvo = (INVCLATTRIBUTEVO01) vo.getParentVO();
						hy.update(hvo);
					}else{
						AggregatedValueObject vo = getBillCardPanelWrapper().getBillVOFromUI();
						hvo = (INVCLATTRIBUTEVO01) vo.getParentVO();
						hvo.setPk_corp(getBillUI()._getCorp().getPk_corp());
						hy.insert(hvo);
					}
					VOTreeNode selectnode = getBillTreeCardUI().getBillTreeSelectNode();
					onQueryHeadData(selectnode);
				} catch (BusinessException e) {
					throw new BusinessException(e.getMessage());
				}
			}
		}else{
			getBillUI().showErrorMessage("当前分类不是末级，不能修改保存,请重新选择！");
			
		}
	}
	//判断是存货分类是否末级
	 private boolean isendflog(Object pk_invcl) {
			// TODO Auto-generated method stub
	 		boolean endflog = false;
	 		if(pk_invcl != null){
	 			String esql = "select invclasscode from bd_invcl where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"'";
	 			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				MapListProcessor alp = new MapListProcessor();
				ArrayList invclasscode = null;
				try {
					invclasscode = (ArrayList) query.executeQuery(esql.toString(), alp);
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				String value = null;
				if (invclasscode != null && invclasscode.size() > 0) {
					for (Object map : invclasscode) {
						Map row = (Map) map;
						Iterator<String> keys = row.keySet().iterator();
						while(keys.hasNext()){
							String key = keys.next(); 
							value = row.get(key).toString();
						}
					}
					String issql = "select invclasscode from bd_invcl where invclasscode like '"+value+"%' and invclasscode != '"+value+"'";
					IUAPQueryBS query1 = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					MapListProcessor alp1 = new MapListProcessor();
					ArrayList  isend= null;
					try {
						isend = (ArrayList) query.executeQuery(issql.toString(), alp1);
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
					if(isend !=null && isend.size()>0){
						endflog = false;
					}else{
						endflog = true;
					}
				}
	 		}
	 		return endflog;
	}
	 
	 @Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-02-04 9:41:03)
	 * @param node nc.ui.pub.card.treetableex.VOTreeNode
	 */
	@SuppressWarnings("unchecked")
	private void onQueryHeadData(VOTreeNode selectnode) throws Exception{
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sb = new nc.ui.by.invapp.h0h001.TreeCardData().getSql(selectnode.getNodeID());
		List<SHOWVO> listsb = (List<SHOWVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(SHOWVO.class));
		SuperVO[] queryVos = new SuperVO[1];
		queryVos[0] = listsb.get(0);
		if (queryVos != null && queryVos.length != 0){
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(queryVos[0]);
			getBufferData().addVOToBuffer(aVo);
		    int num = getBufferData().getVOBufferSize();
		    if(num == -1){
			     num = 0;
		    }else{
			     num = num - 1;
		    }
		    getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(),num+"");
		    getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		}else{
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		}
	}


}
