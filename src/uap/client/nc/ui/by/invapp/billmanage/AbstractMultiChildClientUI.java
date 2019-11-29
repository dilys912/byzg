package nc.ui.by.invapp.billmanage;

import javax.swing.JComponent;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.by.invapp.button.ButtonFactory;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.by.invapp.pub.IBYBillStatus;
import nc.vo.by.invapp.pub.StateMachine;
import nc.vo.by.invapp.pub.StateVO;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * @since 功能：同 管理型UI抽象类
 * @category 由于按钮等状态控制比较麻烦,就写这个公用类。
 * 专为多子表 UI初始化而建 
 * @version 版本：v1.0
 *
 */
public abstract class AbstractMultiChildClientUI extends MultiChildBillManageUI implements BillCardBeforeEditListener,ILinkQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected abstract AbstractManageController createController();

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
		

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
		

	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
		

	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
        getBillCardWrapper().initHeadComboBox("vbillstatus", IBYBillStatus.strStateRemark, true);
        getBillListWrapper().initHeadComboBox("vbillstatus", IBYBillStatus.strStateRemark, true);
        // 显示数据库中的0.
//        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
//        setShowRender(true);
		updateBgtButtons();
//		getBillListPanel().getHeadTable().addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				super.mousePressed(e);
//				int[] rows = getBillListPanel().getHeadTable()
//						.getSelectedRows();
//				BillItem[] all = getBillListPanel().getHeadBillModel()
//						.getBodyItems();
//				int rowcount = getBillListPanel().getHeadBillModel()
//						.getRowCount();
//				for (int i = 0; i < rowcount; i++) {
//					for (int k = 0; k < all.length; k++) {
//						getBillListPanel().getHeadBillModel()
//								.getRowAttribute(i).clearCellBackColor();
//					}
//				}
//				for (int k = 0; k < all.length; k++) {
//					for (int i = 0; i < rows.length; i++) {
//						getBillListPanel().getHeadBillModel().setBackground(
//								Color.GREEN, rows[i], k);
//					}
//				}
//			}
//		});
	}
	

	@Override
	public void setDefaultData() throws Exception {
		String pk_corp = _getCorp().getPrimaryKey();
        BillItem oper = getBillCardPanel().getTailItem("voperatorid");
        if (oper != null){
            oper.setValue(_getOperator());
        }
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null){
            date.setValue(_getDate());
        }
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null){
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
        }
        BillItem vbillcode = getBillCardPanel().getHeadItem("vbillcode");
        if (vbillcode != null){
	        String billNo = BillcodeRuleBO_Client.getBillCode(this.getUIControl().getBillType(), _getCorp().getPk_corp(),
			        null, null);
            if(billNo!=null){
				getBillCardPanel().getHeadItem("vbillcode").setValue(billNo);
			}
        }
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
        BillItem billtype = getBillCardPanel().getHeadItem("pk_billtype");
        if(billtype!=null){
        	getBillCardPanel().setHeadItem("pk_billtype", this.getUIControl().getBillType());
        }
        BillItem billstatus=getBillCardPanel().getHeadItem("vbillstatus");
        if(billstatus!=null){
        	getBillCardPanel().getHeadItem("vbillstatus").setValue(
                Integer.toString(IBillStatus.FREE));	
        }
		updateBgtButtons();
	}

	public boolean beforeEdit(BillItemEvent e) {
		//如果当前编辑的是参照进行刷新(GDT 修改)
		JComponent jcomp = e.getItem().getComponent();
		if(jcomp instanceof UIRefPane ){
			UIRefPane refpane = (UIRefPane) jcomp;
			AbstractRefModel refm = refpane.getRefModel();
			if(refm!=null)  
				refm.reloadData();
		}
		return true;
	}
	
	public boolean beforeEdit(BillEditEvent e) {
		//如果当前编辑的是参照进行刷新(GDT 修改)
		JComponent jcomp = getBillCardPanel().getBillModel(e.getTableCode()).getItemByKey(e.getKey()).getComponent();
		if(jcomp instanceof UIRefPane ){
			UIRefPane refpane = (UIRefPane) jcomp;
			AbstractRefModel refm = refpane.getRefModel();
			if(refm!=null)  
				refm.reloadData();
		}
		return true;
	}
	
    public void initPrivateButton(){
//    	initButton(IBYButton.BTN_ALLSTRING, "事件处理", "事件处理",
//				new int[] { nc.ui.trade.base.IBillOperate.OP_ALL},new int[]{IBYButton.BTN_ADDSTRING,IBYButton.BTN_SEESTRING});
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
	        // 提交
	        setButtonState(IBillButton.Commit, false); 
        }else{
	        // 修改
	        setButtonState(IBillButton.Edit, getStateMachine().isVaidAction(voState, IBillButton.Edit));
	        // 删除
	        setButtonState(IBillButton.Delete, getStateMachine().isVaidAction(voState, IBillButton.Delete)); 
	        // 保存
			setButtonState(IBillButton.Save, getStateMachine().isVaidAction(voState, IBillButton.Save));
	        // 打印 
	        setButtonState(IBYButton.Print, getStateMachine().isVaidAction(voState, IBYButton.Print));
	        // 审核
	        setButtonState(IBillButton.Audit, getStateMachine().isVaidAction(voState, IBillButton.Audit));
	        // 弃审
	        setButtonState(IBillButton.CancelAudit, getStateMachine().isVaidAction(voState, IBillButton.CancelAudit));
	        // 提交
	        setButtonState(IBillButton.Commit, getStateMachine().isVaidAction(voState, IBillButton.Commit));
        }
        resetButton(null);
    }
	
	private StateMachine m_stateMachine = null;
	
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
	public void afterUpdate() {
		super.afterUpdate();
		updateBgtButtons();
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
    
    protected abstract BusinessDelegator createBusinessDelegator();
    
    public void afterEdit(BillEditEvent e) {
        getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(e.getKey()));
        getBillCardPanel().execHeadTailLoadFormulas(getBillCardPanel().getHeadItem(e.getKey()));
    }
    
    /**
	 * 执行表本单行所有项 的公式
	 * @author GDT
	 */ 
	public void execAllBodyEditFormulas(String modelName,int row) {
		BillItem[] allbodyitems = getModelByName(modelName).getBodyItems();
		for(int j=0;j<allbodyitems.length;j++){
			String[] formulas = allbodyitems[j].getEditFormulas();
			if(formulas == null){
				continue;
			}
			getBillCardPanel().getBillModel(modelName).execFormula(row,formulas);
		}
	}
	
	/**
	 * 取表体数据
	 * @param modelName
	 * @param row
	 * @param key
	 * @return
	 */
	public Object getbodyValueByModel(String modelName, int row, String key){
		return getModelByName(modelName).getValueAt(row, key);
	}
	
	/**
	 * 设置表体数据
	 * @param modelName
	 * @param row
	 * @param key
	 * @return 
	 */
	public void setbodyValueByModel(String modelName, Object value, int row, String key){
		getModelByName(modelName).setValueAt(value, row, key);
	}
	
	/**
	 * @param modelName
	 * @return
	 */
	public BillModel getModelByName(String modelName){
		return getBillCardPanel().getBillModel(modelName);
	}
	
	/**
	 * 获取表头值
	 * @param java.lang.String key
	 * @author shipeng
	 * @serialData 2010-10-27
	 * @return Object
	 * */
	protected Object getHeadValueObject(String key){
		return getBillCardPanel().getHeadItem(key).getValueObject();
	}
	
	/**
	 * 向表头塞值
	 * @param java.lang.String key
	 * @param Object           value
	 * @author shipeng
	 * @serialData 2010-10-27
	 * @return Object
	 * */
	protected void setHeadValue(String key,Object value){
		 getBillCardPanel().setHeadItem(key, value);
	}
	
	
	protected UFDate getUFDate(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDate(obj.toString().trim());
			  }catch(Exception e){
				  return null;
			  }
		  }else{
			  return null;
		  }
	  }
	   
	  protected UFDouble getUFDouble(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDouble(obj.toString().trim());
			  }catch(Exception e){
				  return new UFDouble(0);
			  }
		  }else{
			  return new UFDouble(0);
		  }
	  }
	  
	  protected Integer getInteger(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return Integer.parseInt(obj.toString().trim());
			  }catch(Exception e){
				  return new Integer(0);
			  }
		  }else{
			  return new Integer(0);
		  }
	  }
	  
	  /**
		 * 多子表 清空表体
		 * @author 施鹏
	 * @param tablecode1 
		 * @serialData 2010-11-2
		 * @return boolean 
		 * */
		protected void delBodyValueAndLine(String tablecode){
			int row=getModelByName(tablecode).getRowCount();
			int[] rows = new int[row];
			for(int j=0;j<row;j++){
				rows[j]=j;
			}		
			getModelByName(tablecode).delLine(rows);
		}
		
		/**
		 * 清空表头
		 * @author 施鹏
		 * @serialData 2010-11-2
		 * @return boolean 
		 * */
		protected void delHeadValue(){
			 BillItem[]item=getBillCardPanel().getHeadItems();
			 for(int i=0;i<item.length;i++){
				 setHeadValue(item[i].getKey(), null);
			 }
			   try {
					setDefaultData();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

		}
	    
	    /**
	     * 功能: 汇总表体到表头(公供多表体单据使用)
	     * @param bodyedit 要汇总的表体项
	     * @param headedit 汇总的表头项
	     * @return void
	     */
	    protected void totalBodyToHead(String bodytablecode,String bodyedit,String headedit){
	        
	        int rowsNum=getBillCardPanel().getBillModel(bodytablecode).getRowCount();
	        UFDouble value = new UFDouble();
	        for(int i=0;i<rowsNum;i++){
	            if(getBillCardPanel().getBillModel(bodytablecode).getValueAt(i, bodyedit)!=null){
	            	value=value.add(new UFDouble(getBillCardPanel().getBillModel(bodytablecode).getValueAt(i, bodyedit).toString()));    
	            }
	       }
	       BillItem bi = getBillCardPanel().getHeadItem(headedit);
	       if(bi!=null){
	           getBillCardPanel().getHeadItem(headedit).setValue(value);
	       }
	    }
}
