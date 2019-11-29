
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
 * ˵��:�����¼�������,һЩ���÷������ڴ���д,�����ͽ���Ӧ�̳д���
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

		//Ϊ����EH���ܵõ��������,�и�Ϊpublic
		public boolean   isClosing=false;
	     /*
	     * ���ڼ�¼��pk�Ƿ�����
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
	     * ����: ��ʼ��UI���������
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
	     * ����:���û����ֶ�ֵ,pk_corp�ڱ����¼�ʱ����
	     * ��ͷ:����״̬(����̬),���ݱ��� ��β:�Ƶ���,�Ƶ�����
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
	     * ����: ���ܱ��嵽��ͷ
	     * @param bodyedit Ҫ���ܵı�����
	     * @param headedit ���ܵı�ͷ��
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
	     * �жϵ�ǰ�������Ƿ��Ѵ��ڸ�key, ����Ѵ��ڣ���ȥ��.
	     * @param key
	     */
	    public void removeSortKey(String key) {
	        if (m_vhasOrdered.contains(key)) {
	            m_vhasOrdered.removeElement(key);
	        }
	    }
	    
	    /**
	     * �жϵ�ǰ�������Ƿ��Ѵ��ڸ�key, ����Ѵ��ڣ���ȥ��.
	     * @param key
	     */
	    public void removeAllSortKey() {
	        if (m_vhasOrdered.size() > 0) {
	            m_vhasOrdered.removeAllElements();
	        }
	    }
	    
	    /**
	     * ���ñ������ݣ��Խ����VO����
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
	             * ���ڼ�¼�����vector
	             * ����Ѿ�����Ͳ��� ������
	             */
	            if(m_vhasOrdered.contains(key)){
	                return;
	            }else{
	                m_vhasOrdered.add(key);
	            }
	            //for VO����
	            CircularlyAccessibleValueObject[] items = getBillListPanel().getBodyBillModel().getBodyValueVOs(className);
	            VOUtil.ascSort(items, strings);
	            AggregatedValueObject curVO=getBufferData().getCurrentVO();
	            curVO.setChildrenVO(items);
	            getBufferData().setCurrentVO(curVO);
	            //�������õ�������
	            getBillListPanel().setBodyValueVO(items);
	            getBillListPanel().getBodyBillModel().execLoadFormula();
	        }
	    }

		
	    /**
	     * ����: ��ʼ���Զ��尴Ŧ
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
	     * ����: ��ȡ��������
	     * @return:getBodyDataVO ����vo��
	     * 2011-6-3 14:15:17
	     */
		protected CircularlyAccessibleValueObject[]getBodyDataVO(){
	    	return getBillCardPanel().getBillModel().getBodyValueVOs(getUIControl().getBillVoName()[2]);
	    } 
	    
		 
		 /**
		  * ����:�������ֵ
		  * 2011-6-3 14:15:02
		  */
		 protected void setBodyValue(Object o,int row,String key){
			 getBillCardPanel().setBodyValueAt(o, row, key);
		 }

		 /**
		  * ����:�������ֵ
		  * @author �ߴ�ͨ
		  * 2011-6-3 14:15:02
		  */
		 protected void setBodyValueList(Object o,int row,String key){
			 getBillListPanel().getBodyBillModel().setValueAt(o, row, key);
		 }
		 
		 /**
		     * ����: ���ܱ�����
		     * @param e 
		     * @param key  
		     * @param model
		     * @param flag��0:BODY/1:HEAD
		     * @return UFDouble
		     * @author �ߴ�ͨ
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
		     * ����: ���ܱ�������
		     * @param e 
		     * @param key  
		     * @param model 
		     * @param flag��0:BODY/1:HEAD
		     * @return Integer
		     * @author �ߴ�ͨ
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
		     * ����: ���ܱ�������
		     * @param e 
		     * @param key  
		     * @param model
		     * @param flag��0:BODY/1:HEAD
		     * @return Integer
		     * @author �ߴ�ͨ
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
			//�����ǰ�༭���ǲ��ս���ˢ��
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
			//�����ǰ�༭���ǲ��ս���ˢ��
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
		        // ���°�ť״̬
		        updateBgtButtons();
		    }

		    public void initPrivateButton(){
		        super.initPrivateButton();
		        initButton(IBYButton.BTN_LOCK, "����", "����",
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
		        // �������״̬����
		        StateVO voState = new StateVO(iStatus, -1);
		        if(new AbstractEventHandler(this,this.getUIControl()).isAdding()){
			        // �޸�
			        setButtonState(IBillButton.Edit, false);
			        // ɾ��
			        setButtonState(IBillButton.Delete, false); 
			        // ��ӡ 
			        setButtonState(IBYButton.Print, false);
		        }else{
			        // �޸�
			        setButtonState(IBillButton.Edit, getStateMachine().isVaidAction(voState, IBillButton.Edit));
			        // ɾ��
			        setButtonState(IBillButton.Delete, getStateMachine().isVaidAction(voState, IBillButton.Delete)); 
			        // ����
			        setButtonState(IBillButton.Save, getStateMachine().isVaidAction(voState, IBillButton.Save));
		            // ��ӡ 
			        setButtonState(IBYButton.Print, getStateMachine().isVaidAction(voState, IBYButton.Print));
			        //���ư�ť
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
		        // ���ü������⶯���İ�ť״̬��
		        if (bo != null) {
		            int tag = Integer.parseInt(bo.getTag());
		            switch (tag) {
		            case IBillButton.Add:
		                // ���Ա��棬�������Ϻ��ύ
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
			 * ��ȡ��ͷֵ
			 * @param java.lang.String key
			 * @author shipeng
			 * @serialData 2011-6-3
			 * @return Object
			 * */
			protected Object getHeadValueObject(String key){
				return getBillCardPanel().getHeadItem(key).getValueObject();
			}
			
			/**
			 * ��ȡ����ֵ
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
			 * ��ȡ����ֵ
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
			 * ���ͷ��ֵ
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
			 * ��ձ���
			 * @author �ߴ�ͨ
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
			 * ��ձ���
			 * @author ʩ��
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
			 * ��ձ�ͷ
			 * @author �ߴ�ͨ
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
				* ���ͷ��ֵ
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
				* ����崫ֵ
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
					* ���β��ֵ
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
				  * SFClientUtil.openLinkedQueryDialog������
				 */
				public void doQueryAction(ILinkQueryData querydata) {
						String pk = querydata.getBillID();
						setCurrentPanel(BillTemplateWrapper.CARDPANEL);
						//��������
						try {
							getBufferData().addVOToBuffer(loadHeadData(pk));
							setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
							getBufferData().setCurrentRow(getBufferData().getCurrentRow());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
				} 
				
				
				/**
			     * ����: ���ܱ��嵽��ͷ(��������嵥��ʹ��)
			     * @param bodyedit Ҫ���ܵı�����
			     * @param headedit ���ܵı�ͷ��
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

