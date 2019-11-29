/**
 * 
 */
package nc.ui.bgjs.bgjiean;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import nc.ui.bgjs.bill.AbstractClientUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.bgjs.bill.CeVO;
import nc.vo.bgjs.jiean.JieanVO;
import nc.vo.bgjs.proxy.BgjsProxy;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author zhwj
 *
 */
public class ClientUI extends AbstractClientUI{

	private QueryConditionClient dlg;
	
	protected ButtonCtrl m_ButtonCtrl;//按纽控制类
	
	private UIPanel mainPanel;
	
	private BillListPanel billListPanel;//
	
	/**
	 * 
	 */
	public ClientUI() {
		// TODO Auto-generated constructor stub
		initialize();
	}
	
	/* （非 Javadoc）
	 * @see nc.ui.loreal.bill.AbstractClientUI#addListenerEvent()
	 */
	@Override
	public void addListenerEvent() {
		// TODO 自动生成方法存根

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected QueryConditionClient getQueryDLG() {

		if (dlg == null) {
			dlg = createQueryDLG();
		}
		return dlg;
	}
	
	protected QueryConditionClient createQueryDLG() {
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.setTempletID(nc.ui.pub.ClientEnvironment.getInstance()
				.getCorporation().getPk_corp(), this.getModuleCode(), null, null);
		dlg.setNormalShow(false);
		
		return dlg;
	}
	
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO Auto-generated method stub
		try {
			if(bo==getButtonCtrl().m_query){
					onQuery();
			  } else if(bo==getButtonCtrl().m_jiean){
				  onJieAn();
			  } else if(bo==getButtonCtrl().m_unjiean){
				  onUNJieAn();
			  }
			} catch (BusinessException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
				MessageDialog.showErrorDlg(this, "错误", e.getMessage());
			}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		setLayout(new BorderLayout());
		add(getMainPanel());
		
		getButtonCtrl();
		initButton();
		
        
        addListenerEvent();
	}

	@Override
	public void initButton() {
		// TODO Auto-generated method stub
		setButtons(getButtonCtrl().getButtonGroup());
		updateButtons();
	}


	/* （非 Javadoc）
	 * @see nc.ui.loreal.bill.AbstractClientUI#getMainPanel()
	 */
	@Override
	public UIPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new UIPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getBillListPanel());
			
		}
		return mainPanel;
	}


	protected BillListPanel getBillListPanel() {
		if (billListPanel == null) {
			billListPanel = new BillListPanel();
			billListPanel.loadTemplet("JIEAN", null, getOperator(), getPkCorp());
			
		}
		return billListPanel;
	}
	
	
	public ButtonCtrl getButtonCtrl(){
		if(m_ButtonCtrl==null){
			m_ButtonCtrl=new ButtonCtrl();
		}
		return m_ButtonCtrl;
	}
	
	
	public void onQuery() throws BusinessException{
		
        getQueryDLG().showModal();
		
		if(getQueryDLG().getResult()!=getQueryDLG().ID_OK)
			return;
		
		showHintMessage("正在查询");
		nc.vo.pub.query.ConditionVO[] conVO = getQueryDLG()
        .getConditionVO();
		
		ArrayList convos = new ArrayList();
        String isjiean = "";
        String vinvoicecode = "";
		if(conVO!=null&&conVO.length>0){
			for(int i=0;i<conVO.length;i++){
				if(conVO[i].getFieldCode().equals("isjiean")){
					 isjiean=conVO[i].getValue()==null?"":conVO[i].getValue().toString();
					
					
				}else if(conVO[i].getFieldCode().equals("vinvoicecode")){
					vinvoicecode = conVO[i].getValue()==null?"":conVO[i].getValue().toString();
				}
				else {
					convos.add(conVO[i]);
				}
			}
		}
		String wherep = "";
		
		if(convos!=null&&convos.size()>0){
			getQueryDLG().changeDefaultConditions((nc.vo.pub.query.ConditionVO[])convos.toArray(new nc.vo.pub.query.ConditionVO[0]));
			 wherep = getQueryDLG().getWhereSQL();
		}else {
			 wherep = "";
		}
		
		
		String where = " po.pk_corp='"+getPkCorp()+"'";
		if(wherep!=null&&!wherep.equals("")){
			where = where+" and "+wherep;
		}
		
		HashMap hm = new HashMap();
		hm.put("where", where);
		hm.put("isjiean", isjiean);
		hm.put("vinvoicecode", vinvoicecode);
		
		ArrayList list = BgjsProxy.getInstance().getIBGJSITF().getJieAnData(hm);
		 if(list!=null&&list.size()>0){
			 JieanVO[] vo = (JieanVO[])list.toArray(new JieanVO[0]);
		    	
		    	getBillListPanel().setHeaderValueVO(vo);
		    	 showHintMessage("查询完成,共查出"+list.size()+"条数据");
		    }else {
		    	getBillListPanel().setHeaderValueVO(null);
		    	showHintMessage("查询完成,共查出0条数据");
		    }
		
	}
	
	//结案
	public void onJieAn() throws BusinessException{
		int row = getBillListPanel().getHeadTable().getSelectedRow();
		if(row<0){
			throw new BusinessException("请选择要结案的数据");
		}
		
		int result = MessageDialog.showOkCancelDlg(null, "提示", "是否确认要结案");
		if(result!=MessageDialog.ID_OK){
			return;
		}
		
		JieanVO jvo = (JieanVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, JieanVO.class.getName());
		UFBoolean isyj = jvo.getIsyj()==null?new UFBoolean(false):jvo.getIsyj();
		if(isyj.booleanValue()){
			throw new BusinessException("该行数据已结案，请检查！");
		}
		
		CeVO cevo = new CeVO();
		cevo.setPk_corp(getPkCorp());
		cevo.setUser(getOperator());
		cevo.setDate(getDdate());
		
		ArrayList list = new ArrayList();
		list.add(jvo);
		list.add(cevo);
		BgjsProxy.getInstance().getIBGJSITF().doJieAn(list);
		
		getBillListPanel().getHeadBillModel().setValueAt(new UFBoolean(true), row, "isyj");
		
		MessageDialog.showHintDlg(null, "提示", "结案处理完成");
	}
	
	//取消结案
    public void onUNJieAn() throws BusinessException{
    	int row = getBillListPanel().getHeadTable().getSelectedRow();
		if(row<0){
			throw new BusinessException("请选择要取消结案的数据");
		}
		

		int result = MessageDialog.showOkCancelDlg(null, "提示", "是否确认要取消结案");
		if(result!=MessageDialog.ID_OK){
			return;
		}
		
		JieanVO jvo = (JieanVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, JieanVO.class.getName());
		UFBoolean isyj = jvo.getIsyj()==null?new UFBoolean(false):jvo.getIsyj();
		if(!isyj.booleanValue()){
			throw new BusinessException("该行数据未结案，请检查！");
		}
		
		CeVO cevo = new CeVO();
		cevo.setPk_corp(getPkCorp());
		cevo.setUser(getOperator());
		cevo.setDate(getDdate());
		
		ArrayList list = new ArrayList();
		list.add(jvo);
		list.add(cevo);
		BgjsProxy.getInstance().getIBGJSITF().doUNJieAn(list);
		getBillListPanel().getHeadBillModel().setValueAt(new UFBoolean(false), row, "isyj");
		
		MessageDialog.showHintDlg(null, "提示", "取消结案处理完成");
	}
	
}
