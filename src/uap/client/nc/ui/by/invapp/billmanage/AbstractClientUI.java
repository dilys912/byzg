
package nc.ui.by.invapp.billmanage;

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.by.invapp.button.ButtonFactory;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.by.invapp.pub.IBYBillStatus;
import nc.vo.by.invapp.pub.StateMachine;
import nc.vo.by.invapp.pub.StateVO;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.voutils.VOUtil;



/**
 * 说明:基础事件处理类,一些常用方法已在此重写,管理型界面应继承此类
 */
 abstract public class AbstractClientUI extends BillManageUI implements ListSelectionListener,BillCardBeforeEditListener,ILinkQuery {


	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public  int rownums=0;
	    public  BillItem vagentid=null;
	    public  BillItem pk_deptdoc=null;
	    private StateMachine m_stateMachine = null;

		//为了在EH中能得到这个属性,列改为public
		public boolean   isClosing=false;
	     /*
	     * 用于记录该pk是否被排序
	     */
	    private Vector<String> m_vhasOrdered=new Vector<String>();
	    
	    public AbstractClientUI() {
	        super();
	    }

	    public AbstractClientUI(Boolean bool) {
	        super(bool);
	    }

	    public AbstractClientUI(String arg1, String arg2, String arg3, String arg4, String arg5) {
	        super(arg1, arg2, arg3, arg4, arg5);
	    }

	    public nc.ui.trade.manage.ManageEventHandler createEventHandler() {
	        return new AbstractEventHandler(this, getUIControl());
	    }

	    public String getBilltype() {
	        return getUIControl().getBillType();
	    }

	    /**
	     * 功能: 初始化UI界面的数据
	     * @author shipeng 2011-6-3 14:12:19
	     */
	    protected void initSelfData() {
	      	getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	        BillItem cstatuitm = getBillCardPanel().getHeadItem("vbillstatus");
	        if(!Toolkits.isEmpty(cstatuitm)){
	        	getBillCardWrapper().initHeadComboBox("vbillstatus",
	        			IBYBillStatus.strStateRemark, true);
	        	getBillListWrapper().initHeadComboBox("vbillstatus",
	        			IBYBillStatus.strStateRemark, true);
	        }
	        updateBgtButtons();
	    }
	    
	    

	    /**
	     * 功能:设置基本字段值,pk_corp在保存事件时加入
	     * 表头:单据状态(自由态),单据编码 表尾:制单人,制单日期
	     * @throws Exception
	     */
	    public void setDefaultData() throws Exception {
	        String pk_corp = _getCorp().getPrimaryKey();
	        BillItem oper = getBillCardPanel().getTailItem("pk_operator");
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
	        BillItem billtype = getBillCardPanel().getHeadItem("billtype");
	        if(billtype!=null){
	        	getBillCardPanel().setHeadItem("billtype", this.getUIControl().getBillType());
	        }
	        BillItem billstatus=getBillCardPanel().getHeadItem("vbillstatus");
	        if(billstatus!=null){
	        	getBillCardPanel().getHeadItem("vbillstatus").setValue(
	                Integer.toString(IBillStatus.FREE));	
	        }
	        updateBgtButtons();
	    }
	    public AbstractBillUI getbillui(){
	    	return this;
	    }
	    public void valueChanged(ListSelectionEvent arg0) {
	    }

	    public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
	    }

	    protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow)
	            throws Exception {
	    }

	    protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
	    }

	    protected void setButtonState(int iButton, boolean bEnable) {
	        nc.ui.pub.ButtonObject bo = getButtonManager().getButton(iButton);
	        if (bo == null)
	            return;
	        bo.setEnabled(bEnable);
	    }
	    
	    public void afterEdit(BillEditEvent e) {
	        getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(e.getKey()));
	        getBillCardPanel().execHeadTailLoadFormulas(getBillCardPanel().getHeadItem(e.getKey()));

	    }
	    /**
	     * 功能: 汇总表体到表头
	     * @param bodyedit 要汇总的表体项
	     * @param headedit 汇总的表头项
	     * @return void
	     */
	    public void edit(String strCol,String bodyedit,String headedit){
	        
	        int rowsNum=getBillCardPanel().getBillModel().getRowCount();
	        double count = 0;
	        for(int i=0;i<rowsNum;i++){
	            if(getBillCardPanel().getBodyValueAt(i, bodyedit)!=null){
	                count=count+ Double.parseDouble(getBillCardPanel().getBillModel().getValueAt(i, bodyedit).toString());    
	            }
	       }
	       BillItem bi = getBillCardPanel().getHeadItem(headedit);
	       if(bi!=null){
	           getBillCardPanel().getHeadItem(headedit).setValue(String.valueOf(count));
	       }
	    }
	    
	    /**
	     * 判断当前排序中是否已存在该key, 如果已存在，则去掉.
	     * @param key
	     */
	    public void removeSortKey(String key) {
	        if (m_vhasOrdered.contains(key)) {
	            m_vhasOrdered.removeElement(key);
	        }
	    }
	    
	    /**
	     * 判断当前排序中是否已存在该key, 如果已存在，则去掉.
	     * @param key
	     */
	    public void removeAllSortKey() {
	        if (m_vhasOrdered.size() > 0) {
	            m_vhasOrdered.removeAllElements();
	        }
	    }
	    
	    /**
	     * 设置表体数据；对界面的VO排序
	     * @param className
	     * @param strings
	     * @throws Exception
	     */
	    protected void setListBodyData(String className, String[] strings) throws Exception {
	        if (getBufferData().getCurrentVO() == null){
	            getBillListWrapper().setListBodyData(getBufferData()
	                    .getCurrentRow(), null);
	        } else {
	            getBillListWrapper().setListBodyData(getBufferData()
	                    .getCurrentRow(), getBufferData().getCurrentVO());
	            String key =getBufferData().getCurrentVO().getParentVO().getPrimaryKey();
	            
	            /**
	             * 用于记录排序的vector
	             * 如果已经排序就不必 再排序
	             */
	            if(m_vhasOrdered.contains(key)){
	                return;
	            }else{
	                m_vhasOrdered.add(key);
	            }
	            //for VO排序
	            CircularlyAccessibleValueObject[] items = getBillListPanel().getBodyBillModel().getBodyValueVOs(className);
	            VOUtil.ascSort(items, strings);
	            AggregatedValueObject curVO=getBufferData().getCurrentVO();
	            curVO.setChildrenVO(items);
	            getBufferData().setCurrentVO(curVO);
	            //重新设置到界面上
	            getBillListPanel().setBodyValueVO(items);
	            getBillListPanel().getBodyBillModel().execLoadFormula();
	        }
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
	    
	    /**
	     * 功能: 获取表体数据
	     * @return:getBodyDataVO 表体vo集
	     * 2011-6-3 14:15:17
	     */
		protected CircularlyAccessibleValueObject[]getBodyDataVO(){
	    	return getBillCardPanel().getBillModel().getBodyValueVOs(getUIControl().getBillVoName()[2]);
	    } 
	    
		 
		 /**
		  * 功能:向表体塞值
		  * 2011-6-3 14:15:02
		  */
		 protected void setBodyValue(Object o,int row,String key){
			 getBillCardPanel().setBodyValueAt(o, row, key);
		 }

		 /**
		  * 功能:向表体塞值
		  * @author 高代通
		  * 2011-6-3 14:15:02
		  */
		 protected void setBodyValueList(Object o,int row,String key){
			 getBillListPanel().getBodyBillModel().setValueAt(o, row, key);
		 }
		 
		 /**
		     * 功能: 汇总表体金额
		     * @param e 
		     * @param key  
		     * @param model
		     * @param flag：0:BODY/1:HEAD
		     * @return UFDouble
		     * @author 高代通
		     * 2011-6-3 14:16:02
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
		     * @author 高代通
		     * 2011-6-3 14:16:02
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
		     * @author 高代通
		     * 2011-6-3 14:16:02
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
		    
		
		@Override
		protected AbstractManageController createController() {
			return null;
		}

		public boolean beforeEdit(BillItemEvent e) {
			//如果当前编辑的是参照进行刷新
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
			//如果当前编辑的是参照进行刷新
			JComponent jcomp = getBillCardPanel().getBodyItem(e.getKey()).getComponent();
			if(jcomp instanceof UIRefPane ){
				UIRefPane refpane = (UIRefPane) jcomp;
				AbstractRefModel refm = refpane.getRefModel();
				if(refm!=null)  
					refm.reloadData();
			}
			return true;
		}
		   public void afterUpdate() {
		        super.afterUpdate();
		        // 更新按钮状态
		        updateBgtButtons();
		    }

		    public void initPrivateButton(){
		        super.initPrivateButton();
		        initButton(IBYButton.BTN_LOCK, "锁定", "锁定",
						new int[] { nc.ui.trade.base.IBillOperate.OP_NOADD_NOTEDIT},null);
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
		    
		    public StateMachine getStateMachine() {
		        if (m_stateMachine == null)
		            m_stateMachine = new StateMachine();
		        return m_stateMachine;
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
			 * 获取表头值
			 * @param java.lang.String key
			 * @author shipeng
			 * @serialData 2011-6-3
			 * @return Object
			 * */
			protected Object getHeadValueObject(String key){
				return getBillCardPanel().getHeadItem(key).getValueObject();
			}
			
			/**
			 * 获取表体值
			 * @param java.lang.String key
			 * @param int row
			 * @author shipeng
			 * @serialData 2011-6-3
			 * @return Object
			 * */
			protected Object getBodyValue(String key,int row){
				return getBillCardPanel().getBodyValueAt(row, key);
			}
			
			/**
			 * 获取表体值
			 * @param java.lang.String key
			 * @param int row
			 * @author shipeng
			 * @serialData 2011-6-3
			 * @return Object
			 * */
			protected Object getBodyValueList(String key,int row){
				return getBillListPanel().getBodyBillModel().getValueAt(row, key);
			}
			
			/**
			 * 向表头塞值
			 * @param java.lang.String key
			 * @param Object           value
			 * @author shipeng
			 * @serialData 2011-6-3
			 * @return Object
			 * */
			protected void setHeadValue(String key,Object value){
				 getBillCardPanel().setHeadItem(key, value);
			}
			
			/**
			 * 清空表体
			 * @author 高代通
			 * @serialData 2011-6-3
			 * @return boolean 
			 * */
			protected void delBodyValueAndLine(){
				int row=getBillCardPanel().getRowCount();
				for(int j=0;j<row;j++){
					getBillCardPanel().delLine();
				}		
			}

			/**
			 * 清空表体
			 * @author 施坤
			 * @time 2011-11-17
			 * */
			protected void delBodyValues() {
				int  rowCount = getBillCardPanel().getRowCount();
				int[] array=new int[rowCount];
				   for(int i=0;i<rowCount;i++){
					   array[i]=i;
				   }
				getBillCardPanel().getBillModel().delLine(array);
				
			}
			/**
			 * 清空表头
			 * @author 高代通
			 * @serialData 2011-6-3
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
			
			  protected String getString(Object obj){
				  if(!Toolkits.isEmpty(obj)){
					  try{
						  return obj.toString();
					  }catch(Exception e){
						  return "";
					  }
				  }else{
					  return "";
				  }
			  }
			  
			  protected Integer getInteger(String obj){
				  if(!Toolkits.isEmpty(obj)){
					  try{
						  return Integer.parseInt(obj.trim());
					  }catch(Exception e){
						  return new Integer(0);
					  }
				  }else{
					  return new Integer(0);
				  }
			  }
			  
				/**
				* 向表头传值
				* @param String key
				* @param SalesDeliveryBillVO hvo
				* @author shipeng
				* @date  2011-6-3
				* */
				 protected void setHeadValues(SuperVO hvo,String key){
					 BillItem[]item=getBillCardPanel().getHeadItems();
					 for(int i=0;i<item.length;i++){
						 if(!key.equalsIgnoreCase(item[i].getKey())){
							 setHeadValue(item[i].getKey(), hvo.getAttributeValue(item[i].getKey()));
						 }
					 }
				 }
				/**
				* 向表体传值
				* @param SalesDeliveryBillVO hvo
				* @author shipeng
				* @date  2011-6-3
				* */
				 protected void setBeadValues(SuperVO bvo,int row){
					 BillItem[]item=getBillCardPanel().getBodyItems();
					 for(int i=0;i<item.length;i++){
						 setBodyValue(bvo.getAttributeValue(item[i].getKey()),row, item[i].getKey());
					 }
				 }
				 
					/**
					* 向表尾传值
					* @param SalesDeliveryBillVO hvo
					* @author shipeng
					* @date  2011-6-3
					* */
				 protected void setHeadTailValues(SuperVO vo){
					 BillItem[]item = getBillCardPanel().getTailItems();
					 for(int i=0;i<item.length;i++){
						 getBillCardPanel().getTailItems()[i].setValue(vo.getAttributeValue(item[i].getKey()));
					 }
				 }
				 
				 /**
				  * SFClientUtil.openLinkedQueryDialog打开联查
				 */
				public void doQueryAction(ILinkQueryData querydata) {
						String pk = querydata.getBillID();
						setCurrentPanel(BillTemplateWrapper.CARDPANEL);
						//加载数据
						try {
							getBufferData().addVOToBuffer(loadHeadData(pk));
							setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
							getBufferData().setCurrentRow(getBufferData().getCurrentRow());
						} catch (Exception ex) {
							ex.printStackTrace();
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

