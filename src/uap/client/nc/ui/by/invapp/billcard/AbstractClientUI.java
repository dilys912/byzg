
package nc.ui.by.invapp.billcard;

import nc.ui.by.invapp.button.ButtonFactory;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.by.invapp.pub.StateMachine;
import nc.vo.by.invapp.pub.StateVO;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:基础界面类,卡片型界面应继承此类
 */
public class AbstractClientUI extends BillCardUI {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractClientUI() {
        super();
        initilize();
    }
	private StateMachine m_stateMachine = null;
    public AbstractClientUI(String arg0, String arg1, String arg2, String arg3, String arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
    }

    protected ICardController createController() {
        return new AbstractCtrl();
    }

    protected AbstractEventHandler createEventHandler() {
        return new AbstractEventHandler(this,this.getUIControl());
    }

    public String getRefBillType() {
        return null;
    }

    protected void initSelfData() {
    }
    
    @Override
	public boolean onClosing() {
		return super.onClosing();
	}
    
    @SuppressWarnings("unchecked")
	public void setDefaultData() throws Exception {
        try {

            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //需要先清空
            getBufferData().clear();

            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setChildrenVO(vos);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //设置当前行
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    protected void initilize() {
//        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected String getBodyWherePart() {
        return "  pk_corp='"+_getCorp().getPk_corp()+"'";
    }
    
    protected String getStr(String key,int row){
    	return getBillCardPanel().getBodyValueAt(row, key)==null?null:getBillCardPanel().getBodyValueAt(row, key).toString();
    }
    
	 /**
     * 功能: 汇总表体金额
     * @param e 
     * @param key  
     * @param model
     * @param flag：0:BODY/1:HEAD
     * @return UFDouble
     */
    protected UFDouble sumMny(BillEditEvent e,String key,String model,int flag){
    	UFDouble mny=new UFDouble(0);
    	int rows=getBillCardPanel().getRowCount();
    	for(int i=0;i<rows;i++){
        	UFDouble temp=getUFDouble(key,i,e,model,flag);
    		mny=mny.add(temp);
    	}
    	return mny;
    }
    
	 /**
     * 功能: 汇总表体天数
     * @param e 
     * @param key  
     * @param model 
     * @param flag：0:BODY/1:HEAD
     * @return Integer
     */
    protected Integer sumDays(BillEditEvent e,String key,String model,int flag){
    	Integer mny=new Integer(0);
    	int rows=getBillCardPanel().getRowCount();
    	for(int i=0;i<rows;i++){
        	Integer temp=getInteger(key,i,e,model,flag);
    		mny=mny+temp;
    	}
    	return mny;
    }
    
	 /**
     * 功能: 汇总表体数量
     * @param e 
     * @param key  
     * @param model
     * @param flag：0:BODY/1:HEAD
     * @return Integer
     */
    protected UFDouble sumNum(BillEditEvent e,String key,String model,int flag){
    	UFDouble num=new UFDouble(0);
    	int rows=getBillCardPanel().getBillModel(model).getRowCount();
    	for(int i=0;i<rows;i++){
        	UFDouble temp=getUFDouble(key,i,e,model,flag);
        	num=num.add(temp);
    	}
    	return num;
    }
    
    protected UFDouble getUFDouble(String key,int row,BillEditEvent e,String model,int flag){
    	if(flag==BODY){
    		return Toolkits.isEmpty(getBillCardPanel().getBillModel(model).getValueAt(row, key))?new UFDouble(0):new UFDouble(getBillCardPanel().getBillModel(model).getValueAt(row, key).toString());
    	}else if(flag==HEAD){
    		return Toolkits.isEmpty(getBillCardPanel().getHeadItem(key).getValueObject())?new UFDouble(0):new UFDouble(getBillCardPanel().getHeadItem(key).getValueObject().toString());
    	}
    	return new UFDouble(0);
    }
    
    protected Integer getInteger(String key,int row,BillEditEvent e,String model,int flag){   	
    	if(flag==BODY){
       		return Toolkits.isEmpty(getBillCardPanel().getBillModel(model).getValueAt(row, key))?new Integer(0):new Integer(getBillCardPanel().getBillModel(model).getValueAt(row, key).toString());
    	}else if(flag==HEAD){
    		return Toolkits.isEmpty(getBillCardPanel().getHeadItem(key).getValueObject())?new Integer(0):new Integer(getBillCardPanel().getHeadItem(key).getValueObject().toString());
    	}
    	return new Integer(0);
    }
    
    /**
     * 功能: 初始化自定义按纽
     * @param id,code,name,newStatus
     * @author 高代通 2011-6-3 14:15:32
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
        AggregatedValueObject voBill = getBufferData().getCurrentVO();
        SuperVO voHead = null;
        Object billstatus=null;
        if(voBill!=null){
	        voHead = (SuperVO) voBill.getParentVO();
	       billstatus=voHead.getAttributeValue("vbillstatus");
        }
        int iStatus =IBillStatus.FREE;
        if(!Toolkits.isEmpty(billstatus)){
            iStatus = ((Integer) voHead.getAttributeValue("vbillstatus")).intValue();
        }
        // 初步完成状态检验
        StateVO voState = new StateVO(iStatus, -1);
        if(new AbstractEventHandler(this,this.getUIControl()).isAdding()){
	        // 修改
	        setButtonState(IBillButton.Edit, false);
	        // 删除
	        setButtonState(IBillButton.Delete, false); 
	        // 打印 
	        setButtonState(IBYButton.Print, false);
        }else{
	        // 修改
	        setButtonState(IBillButton.Edit, getStateMachine().isVaidAction(voState, IBillButton.Edit));
	        // 删除
	        setButtonState(IBillButton.Delete, getStateMachine().isVaidAction(voState, IBillButton.Delete)); 
	        // 保存
	        setButtonState(IBillButton.Save, getStateMachine().isVaidAction(voState, IBillButton.Save));
            // 打印 
	        setButtonState(IBYButton.Print, getStateMachine().isVaidAction(voState, IBYButton.Print));
	        //复制按钮
	        setButtonState(IBYButton.Copy, getStateMachine().isVaidAction(voState, IBYButton.Copy));
        }
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
    
    public StateMachine getStateMachine() {
        if (m_stateMachine == null)
            m_stateMachine = new StateMachine();
        return m_stateMachine;
    }
    
    protected void setButtonState(int iButton, boolean bEnable) {
        nc.ui.pub.ButtonObject bo = getButtonManager().getButton(iButton);
        if (bo == null)
            return;
        bo.setEnabled(bEnable);
    }
    
    
}