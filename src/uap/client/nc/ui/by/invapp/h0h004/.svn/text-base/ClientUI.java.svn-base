
package nc.ui.by.invapp.h0h004;

import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.by.invapp.button.ButtonFactory;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends AbstractBdBillCardUI{
	 /**
	 * 库存期初导入
	 */
	private static final long serialVersionUID = 1L;

	public ClientUI() {
		super();
		
	}
	
	/**
	 * @param pk_corp
	 * @param pk_billType
	 * @param pk_busitype
	 * @param operater
	 * @param billId
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	
	@Override
     protected ICardController createController() {
		return new ClientCtrl();
	}
	
	@Override
	public String getRefBillType() {
		return null;
	}
	
	@Override
    protected void initSelfData() {		
		super.initSelfData();
//		
//		getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void setDefaultData() throws Exception {	   		
		
	}
	
	public void setDefaultData(int row) throws Exception {	
		 getBillCardPanel().setBodyValueAt(new Integer(0), row, "dr");
		 getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");

	}
	
	protected CardEventHandler createEventHandler(){
		return new ClientEventHandler(this,getUIControl());
	}

	public Object getUserObject() {
		return null;
	}

    public void initPrivateButton(){
        super.initPrivateButton();
        initButton(IBYButton.BTN_KCDR, "库存导入", "库存导入",
				new int[] { nc.ui.trade.base.IBillOperate.OP_INIT},null);
        initButton(IBYButton.BTN_KCJY, "库存校验", "库存校验",
				new int[] { nc.ui.trade.base.IBillOperate.OP_INIT},null);
        initButton(IBYButton.BTN_GLDR, "隔离库存导入", "隔离库存导入",
				new int[] { nc.ui.trade.base.IBillOperate.OP_INIT},null);
        initButton(IBYButton.BTN_GLJY, "隔离库存校验", "隔离库存校验",
				new int[] { nc.ui.trade.base.IBillOperate.OP_INIT},null);
    }
    /**
     * 功能: 初始化自定义按纽
     * @param id,code,name,newStatus
     */
    public  void initButton(int id,String code,String name,int[]newStatus,int[]childButton){
        ButtonVO btnvo = ButtonFactory.createButtonVO(id, code, name);
        if(childButton!=null){
        	btnvo.setChildAry(childButton);
        }
        btnvo.setOperateStatus(newStatus);
        addPrivateButton(btnvo);
        super.initPrivateButton();
    }

    public void updateBgtButtons() {
        resetButton(null);
    }
    
    public void resetButton(ButtonObject bo) {
        // 设置几个特殊动作的按钮状态。
        if (bo != null) {
            int tag = Integer.parseInt(bo.getTag());
            switch (tag) {
            case IBillButton.Add:
                // 可以保存，不能作废和提交
                setButtonState(IBillButton.Save, true);
                setButtonState(IBillButton.Commit, false);
                setButtonState(IBillButton.Del, false);
                break;
            case IBillButton.Edit:
                setButtonState(IBillButton.Save, true);
                setButtonState(IBillButton.Commit, false);
                setButtonState(IBillButton.Del, false);
                break;
            case IBillButton.Return:
                if (!getBillCardPanel().isShowing()) {
                    return;
                }
            default:
                break;
            }
        }
        setButtons(getButtons());
        updateButtons();
    }
    @Override
    public void updateButtons() {
        setButtonState(IBYButton.BTN_KCDR, true);
        setButtonState(IBYButton.BTN_KCJY, true); 
        setButtonState(IBYButton.BTN_GLDR, true);
        setButtonState(IBYButton.BTN_GLJY, true); 
    	super.updateButtons();
    }
    
    protected void setButtonState(int iButton, boolean bEnable) {
        nc.ui.pub.ButtonObject bo = getButtonManager().getButton(iButton);
        if (bo == null)
            return;
        bo.setEnabled(bEnable);
    }
}
