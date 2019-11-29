
package nc.ui.by.invapp.billmanage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.itf.uap.sf.IFuncRegisterQueryService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTempletVOGetter;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.by.invapp.pub.IBYBillStatus;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;
import nc.vo.wfengine.definition.IApproveflowConst;
import nc.vo.wfengine.definition.WorkflowDefinitionVO;

/**
 * 说明:基础事件处理类,一些常用方法已在此重写,管理型界面应继承此类
 */
public class AbstractEventHandler extends ManageEventHandler {


	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
	}


	@Override
	protected void onBoReturn() throws Exception {
		super.onBoReturn();
	}

	protected void onBoBasReturn() throws Exception {
		super.onBoReturn();
	}
	

	public AbstractEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}
	
	public void onboshowhouse() {
		SFClientUtil.openLinkedQueryDialog("H0H310", getBillUI(), new ILinkQueryData(){

			public String getBillID() {
				return Toolkits.getString(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fh").getValueObject());
			}

			public String getBillType() {
				return "TB10";
			}

			public String getPkOrg() {
				return null;
			}

			public Object getUserObject() {
				return false;
			}
			
		});
		
	}
	
	protected BillData anotherBD = null;
	
	/**
	 * 重新获取一个billData
	 */
	public BillData getAnotherBillDate() {
		if (anotherBD == null) {
			anotherBD = new BillData(BillTempletVOGetter.getDefaultTemplet(
					getUIController().getBillType(), getBillUI()
							.getBusinessType(), _getOperator(), _getCorp()
							.getPk_corp(), getBillUI().getNodeKey()));
		}
		return anotherBD;
	}
	
	BillCardPanel showCardPanel = null;
	
	protected BillCardPanel getShowCardPanel(){
		if(showCardPanel == null){
			showCardPanel = new BillCardPanel();
			showCardPanel.setName("showCardPanel");
			showCardPanel.setBillData(getAnotherBillDate());
		}
		return showCardPanel;
	}


	/**
	 * 设置显示数据
	 */
	protected void setShowCardData() {
		getAnotherBillDate().setBillValueVO(getBufferData().getCurrentVO());
	}
	
	/**
	 * 显示面板里各个滚动面板的大小
	 */
	protected void setShowDetailSize(String tablecode,int width,int height){
		BillTabbedPane tabp = getShowCardPanel().getHeadTabbedPane();
		BillTabVO[] tabvos = getAnotherBillDate().getAllTabVos();
		for (int i = 0; i < tabvos.length; i++) {
			UIScrollPane spane = getHeadScrollPane(tabvos[i],tabp);
			if(spane == null){
				continue;
			}
			if(tabvos[i].getTabcode().equals(tablecode)){
				spane.setPreferredSize(new Dimension(width,height));
			}
		}
	}


	protected UIScrollPane getHeadScrollPane(BillTabVO tabvos, BillTabbedPane tabp) {
		int tindex = tabp.getIndexofTableCode(tabvos);
		if(tindex>=0){
			return (UIScrollPane) tabp.getComponentAt(tindex);
		}
		return null;
	}
	
	
	
	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillcode", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("voperatorid",  _getOperator());
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("dmakedate",  _getDate());
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapprovedate", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vunapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vunapprovedate", null);
	}

	protected void onBoCopybyEdit(String pk_bill) throws Exception {
		onBoEdit();
	}
	
	 /**
     * 功能: 自定义审核按钮触发方法
     * @return 
     * @author 高代通
     * 2011-6-3 
     */
	public void onBoCheckPass() throws Exception {
    	AggregatedValueObject avo=getBufferData().getCurrentVO();
    	if(avo==null)return ;
        int result=getBillUI().showYesNoMessage("确定此操作吗?");
        if(result==UIDialog.ID_YES){
	        SuperVO vo=(SuperVO) avo.getParentVO();
			// 审核人
	        vo.setAttributeValue("vapproveid" , _getOperator());
			// 审核日期
			vo.setAttributeValue("vapprovedate", _getDate());
        	// 弃审人
 	        vo.setAttributeValue("vunapproveid" , null);
 			// 弃审日期
 			vo.setAttributeValue("vunapprovedate", null);
			// 单据状态
			vo.setAttributeValue("vbillstatus", IBYBillStatus.CHECKPASS);
			HYPubBO bo = new HYPubBO();
			bo.update(vo);
	        getBufferData().updateView(); 
	        super.onBoRefresh();
        }
	}
	

	 /**
     * 功能: 自定义弃审按钮触发方法
     * @return 
     * @author 高代通
     * 2011-6-3 
     */
	public void onBoCheckNoPass() throws Exception {
    	AggregatedValueObject avo=getBufferData().getCurrentVO();
    	if(avo==null)return ;
        int result=getBillUI().showYesNoMessage("确定此操作吗?");
        if(result==UIDialog.ID_YES){
	        SuperVO vo=(SuperVO) avo.getParentVO();
	         String  vpid= (String) vo.getAttributeValue("vapproveid");
	         if(_getOperator().equals(vpid.trim())){
	    
	        	// 弃审人
	 	        vo.setAttributeValue("vunapproveid" , _getOperator());
	 			// 弃审日期
	 			vo.setAttributeValue("vunapprovedate", _getDate());
	 			// 单据状态
	 			vo.setAttributeValue("vbillstatus", IBYBillStatus.FREE);
				// 审核人
		        vo.setAttributeValue("vapproveid" , "");
				// 审核日期
				vo.setAttributeValue("vapprovedate", null);
				HYPubBO bo = new HYPubBO();
				bo.update(vo);
	 	        getBufferData().updateView(); 
	 	        super.onBoRefresh();
	         }else {
	        	 showErrorMessage("审核人与弃审人必须是同一个人！");
	         }
			
        }
	}
	
	 /**
     * 功能: 对同一列前台重复行的校验
     * @param column 字段
     * @param rows  行数
     * @return boolean
     */
    protected boolean isPreRepeat(String column,int rows){
        for(int i=0;i<rows-1;i++){
            String preValue=getStr(i,column);
            for(int j=i+1;j<rows;j++){
                String curValue=getStr(j,column);                  
                if(preValue.compareTo(curValue)==0){
                	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(column).getName();
                    showErrorMessage("第"+(j+1)+"行："+key+"："+curValue+" 重复！");
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * 功能: 返回字符串
     * @param row 行号
     * @param str 列字符
     * @author 高代通
     * 2011-6-3 
     */
    protected String getStr(int row,String str){
       return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str).toString();
    }
    
    /**
     * 功能: 显示出错信息
     * @param errorMessage
     * @author 高代通
     * 2011-6-3 
     */
    protected void showErrorMessage(String errorMessage){
        getBillUI().showErrorMessage(errorMessage);
        return;
    }
    
    /**
     * 功能:初始化UFDouble型数据
     * * @author 高代通
     * 2011-6-3 
     */
    protected void onBoLineAdd() throws Exception
    {
	
        super.onBoLineAdd();
        
    }

	@Override
	protected void onBoSave() throws Exception {
		
        //保存时不允许为空
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		BillItem billcode=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode");
		//保存时生成单据号
		if(needBillNo()){
	        String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(), getBillUI()._getCorp().getPk_corp(),
			        null, null);
            if(billcode!=null){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode").setValue(billNo);
			}
		}
		super.onBoSave();
	}
		

	//解决编辑时从后台修改数据后，系统不保存问题
	public void onBoSaveSpecial() throws Exception {
//		保存时不允许为空
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		BillItem billcode=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode");
		//保存时生成单据号
		if(needBillNo()){
	        String billNo = BillcodeRuleBO_Client.getBillCode(getUIController().getBillType(), getBillUI()._getCorp().toString(),
			        null, null);
            if(billcode!=null){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode").setValue(billNo);
			}
		}
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		billVO.getParentVO().setStatus(1);
		if(billVO.getChildrenVO()!=null && billVO.getChildrenVO().length>0)
			for(int i=0; i<billVO.getChildrenVO().length; i++)
				billVO.getChildrenVO()[i].setStatus(1);
		
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// 设置保存后状态
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}
	
	/**
	 * NC基本保存方法
	 * @author shipeng 
	 * @throws Exception 
	 * @time 2011-6-8
	 * */
	protected void onBoSaveBas() throws Exception{
	    //保存时不允许为空
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
	    super.onBoSave();
	}
	
	/**
	 * 判断表头是否已经有单据号
	 * */
	protected boolean needBillNo(){
		BillItem billcode=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode");
		 Object value=null;
		if(billcode!=null){
			value=billcode.getValueObject();
		}
		 return Toolkits.isEmpty(value);
	}
	
    /**
     * 功能: 返回行数
     * @author 高代通
     * 2011-6-3 
     */
    protected int getRowCount(){
   	 return getBillCardPanelWrapper().getBillCardPanel().getRowCount();
    }

	public String addCondtion() {
    	return null;
    }
	
	protected String strCon="";
	/**
     * 功能: 查询加条件,按制单日期排序
     * @throws Exception
     */
    public void onBoQuery() throws Exception {
    	StringBuffer strWhere = getWhere();
        if (askForQueryCondition(strWhere) == false)
            return;// 用户放弃了查询
        Object makedate = getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate");
        Object vbillcode = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode");
        strWhere.append(" and pk_corp='"+_getCorp().getPk_corp()+"' ");
        if(makedate!=null&&vbillcode!=null)
        	strWhere.append(" order by dmakedate desc,vbillcode desc");
        else if(makedate!=null)
        	strWhere.append(" order by dmakedate desc");
        else if(vbillcode!=null)
        	strWhere.append(" order by vbillcode desc");
        SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
        strCon=strWhere.toString();
        getBufferData().clear();
        addDataToBuffer(queryVos);
        updateBuffer();
    }

	
	
	/**
	 * 返回查询条件
	 * */
	protected StringBuffer getWhere(){
		return new StringBuffer();
	}
	
	/**
	 * 直接修改,不用填写申请单(系统原方法)
	 */
	protected void onBoDirectEdit() throws Exception {
		super.onBoRefresh();
    	AggregatedValueObject avo=getBufferData().getCurrentVO();
    	if(avo==null)return ;
		super.onBoEdit();
	}
    
	/** 
	 * 修改之前,先填写一张申请单,申请单被审批通过后,才能修改
	 * 2012-10-16 此方法提到基类来
	 */
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoRefresh();
		super.onBoEdit();
//		//当前无缓存数据直接返回
//    	AggregatedValueObject avo=getBufferData().getCurrentVO();
//    	if(avo==null)return ;
//    	
//		String islock=Toolkits.getString(getHeadValue("islock"));
//		if(!islock.equals("Y")){
//			
//			String hvoname = getUIController().getBillVoName()[1];
//			SuperVO svo = (SuperVO) Class.forName(hvoname).newInstance();
//			String hpk = svo.getPKFieldName();
//			
//			String pk_bill=Toolkits.getString(getHeadValue(hpk));
//			String vbillcode=Toolkits.getString(getHeadValue("vbillcode"));
//			StringBuffer sb=new StringBuffer("");
//			sb.append(" select vbillstatus from csh_event where nvl(dr,0)=0 and version=0 and sbillcode='"+vbillcode+"' group by vbillstatus ");
//			List list=Proxy.getIFPItf().queryArrayBySql(sb.toString());
//			if(list.size()>0&&list!=null){
//				HashMap<String, Object> map=(HashMap<String, Object>) list.get(0);
//				int eventbillstatus =Toolkits.getInteger(map.get("vbillstatus"));
//				if(eventbillstatus==3){
//					showErrorMessage("上次修改事件申请单未审批，不可修改！");
//				}else if(eventbillstatus==1){
//				    super.onBoEdit();
//				}else if(eventbillstatus==8){
//					showErrorMessage("上次修改事件申请单被驳回或未提交，不可修改！");
//				}else{
//					super.onBoEdit();
//				}
//			}else{
//				String billtype=Toolkits.getString(getHeadValue("billtype"));
//				String billname=getBillUI().getTitle();
//				String strFuncode=getBillUI()._getModuleCode();
//				String useid=_getOperator();
//				geth701ClientUI(getBillUI(),strFuncode,vbillcode,billtype,billname,pk_bill,useid);
//			}
//		}else{
//			showErrorMessage("当前单据被锁定，不可修改！");
//		}
	}
    

	/**
     * 功能: 做上下游单据时,调用setDefaultData()
     * @param bo
     * 2011-6-3 
     */
    public void onButton(ButtonObject bo){
    	 super.onButton(bo);
         try {
             ButtonObject parent=bo.getParent();
             if(!Toolkits.isEmpty(parent)){
                 String code=bo.getParent().getCode();
                 if("增加".equalsIgnoreCase(code) && 
                         !"自制单据".equalsIgnoreCase(code)){
                     getBillUI().setDefaultData();
                 }
                 String s=bo.getCode();
                 BillModel bi = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
                 if (bi != null) {
                   if ("自制单据".equalsIgnoreCase(s)){
                     super.onBoLineAdd();
                   }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
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
	  protected UFDouble getUFDouble(String obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDouble(obj.trim());
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
	  
	  /**
	   * 获取表头数据
	   * */
	  protected Object getHeadValue(String key){
		  return getBillCardPanelWrapper().getBillCardPanel().getHeadItem(key).getValueObject();
	  }
	  
	  /**
	   * 获取表体数据
	   * */
	  protected Object getBodyValue(int row,String key){
		  return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, key);
	  }
	  
	  /**
	   * 向表体塞值
	   * */
	  protected void setBodyValue(Object obj,int row,String key){
		   getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(obj,row, key);
	  }
	  
	  /**
	   * 多子表向表体塞值
	   * */
	  protected void setBodyValueByTableCode(String tablecode,Object obj,int row,String key){
		   getBillCardPanelWrapper().getBillCardPanel().getBillModel(tablecode).setValueAt(obj,row, key);
	  }
	  
	  /**
	   * 向表体塞值
	   * */
	  protected void setBodyValue(Object[] obj,int row,String[] key){
		  for(int i=0;i<key.length;i++){
			  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(obj[i],row, key[i]);
		  }
	  }
	  
	  /**
	   * 设置所有项不可编辑
	   * */
		protected void setAllItemEditFalse() {
			//表头全不编辑
			BillItem[] headits = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
			for(int i=0;i<headits.length;i++){
				headits[i].setEnabled(false);
			}
			//表体全不可编辑
			BillItem[] bodyits = getBillCardPanelWrapper().getBillCardPanel().getBodyItems();
			for(int i=0;i<bodyits.length;i++){
				bodyits[i].setEdit(true);
				bodyits[i].setEnabled(false);
			}
		}
		
	  /**
	   * 设置所有项可编辑
	   * @param String[] headkey
	   * @param String []bodykey
	   * @serialData 2011-6-3
	   * */
		protected void setAllItemEditTrue(String[] headkey,String []bodykey) {
			//表头可编辑
			for(int i=0;i<headkey.length;i++){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(headkey[i]).setEdit(true);
			}
			//表体可编辑
			for(int i=0;i<bodykey.length;i++){
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(bodykey[i]).setEdit(true);
			}
		}
		
		/**
		 * 关闭
		 * @throws Exception 
		 * */
		protected void onBoClose() throws Exception {
			AggregatedValueObject avo = getBufferData().getCurrentVO();
			if (avo == null)
				return;
			int result = getBillUI().showYesNoMessage("确定关闭吗?");
			if (result == UIDialog.ID_YES) {
				SuperVO vo = (SuperVO) avo.getParentVO();
				// 关闭态
				vo.setAttributeValue("vbillstatus", IBYBillStatus.CLOSE);
				// 关闭人
				vo.setAttributeValue("pk_closer", _getOperator());
				// 关闭时间
				vo.setAttributeValue("closedate", _getDate());
				HYPubBO bo = new HYPubBO();
				bo.update(vo);
				getBufferData().updateView();
			}
		}
		
		/**
		 * 锁定
		 * @throws Exception 
		 * */
		protected void onBoLock() throws Exception {
			AggregatedValueObject avo = getBufferData().getCurrentVO();
			if (avo == null)
				return;
			int result = getBillUI().showYesNoMessage("确定锁定吗?");
			if (result == UIDialog.ID_YES) {
				SuperVO vo = (SuperVO) avo.getParentVO();
				// 锁定态
				vo.setAttributeValue("islock", new UFBoolean(true));
				// 锁定人
				vo.setAttributeValue("lockman", _getOperator());
				// 锁定时间
				vo.setAttributeValue("lockdate", _getDate());
				HYPubBO bo = new HYPubBO();
				bo.update(vo);
				getBufferData().updateView();
			}
		}
		
		/**
		 * 解锁
		 * @throws Exception 
		 * */
		protected void onBoUNLock() throws Exception {
			AggregatedValueObject avo = getBufferData().getCurrentVO();
			if (avo == null)
				return;
			int result = getBillUI().showYesNoMessage("确定解锁吗?");
			if (result == UIDialog.ID_YES) {
				SuperVO vo = (SuperVO) avo.getParentVO();
				// 锁定态
				vo.setAttributeValue("islock", new UFBoolean(false));
				// 锁定人
				vo.setAttributeValue("lockman", null);
				// 锁定时间
				vo.setAttributeValue("lockdate",null);
				HYPubBO bo = new HYPubBO();
				bo.update(vo);
				getBufferData().updateView();
			}
		}
		
		/**
		 * 修改行状态
		 * */
		public void setrowstatus(String modelname){
			BillModel bm = getBillCardPanelWrapper().getBillCardPanel().getBillModel(modelname);
			int rows = bm.getRowCount();
	        for(int i=0;i<rows;i++){
	            bm.setRowState(i, BillModel.MODIFICATION);
	        }
		}
		
		 /**
		  * 功能:隐藏行
		  * 2011-6-3 14:30:02
		  */
		 public void  hidLine(){
			 this.getButtonManager().getButton( IBillButton.Line).setVisible(false);
		 }
		 /**
		  * 功能:显示行
		  * 2011-6-3 14:30:02
		  */
		 public void  showLine(){
			 this.getButtonManager().getButton( IBillButton.Line).setVisible(true);
		 }
		 
		 /**
		 * @功能：汇总表体项值的总和到表头项
		 * @param: headitemname 表头项名称
		 * @param: bodyitemname 表体项名称
		 * 
		 */
		public void groupBodyValueToHead(String headitemname,String bodyitemname){
			UFDouble sum = new UFDouble(0.00);
			int rowcount = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
			for(int i=0;i<rowcount;i++){
				UFDouble itemvalue = getBodyValue(i, bodyitemname)==null?new UFDouble(0.00):new UFDouble(getBodyValue(i, bodyitemname).toString());
				sum = sum.add(itemvalue);
			}
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(headitemname).setValue(sum);
		}	
		
		
		/**
		 * 
		* @功能: 得到表体某列
		* @创建时间 2011-7-7 上午10:46:40
		* @throws
		 */
		public String[] getColValueArr(String tablemodel,String key){
			
			int count = getBillCardPanelWrapper().getBillCardPanel().getBillTable(""+tablemodel+"").getRowCount();
			String[] code = new String[count];
			for (int i = 0; i < code.length; i++) {
				code[i] = getBillCardPanelWrapper().getBillCardPanel().getBillModel(""+tablemodel+"").getValueAt(i, ""+key+"")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel(""+tablemodel+"").getValueAt(i, ""+key+"").toString();
			}
			
			return code;
		}
		/**
		 * 
		* @功能: 得到表体某列
		* @创建时间 2011-7-7 上午10:46:40
		* @throws
		 */
		public String getColValue(String tablemodel,String key){
			
			int count = getBillCardPanelWrapper().getBillCardPanel().getBillTable(""+tablemodel+"").getRowCount();
			String[] code = new String[count];
			for (int i = 0; i < code.length; i++) {
				code[i] = getBillCardPanelWrapper().getBillCardPanel().getBillModel(""+tablemodel+"").getValueAt(i, ""+key+"")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel(""+tablemodel+"").getValueAt(i, ""+key+"").toString();
			}
			String bdcodes = Toolkits.combinArrayToString(code);
			
			return bdcodes;
		}
		/**
		 * 获取表体值
		 * @param java.lang.String key
		 * @param int row
		 * @return Object
		 * */
		protected Object getBodyValue(String key,int row){
			return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, key);
		}

		/**
		 * @说明：<p>打开相应节点，必须有相应的节点权限</p>
		 * @param funCode
		 * @return1
		 * @throws BusinessException
		 */
		@SuppressWarnings("unchecked")
		protected static ToftPanel openFuncNode(String funCode) throws BusinessException {
			if (funCode != null) {
	        	IFuncWindow fp = getOpenNode(funCode);
	    		if (fp!=null) {
	    			boolean isCanceled = fp.closeWindow();
	    			if(!isCanceled){
	    				return null;
	    			}
	    		}
				FuncRegisterVO fvo= SFClientUtil.findFRVOFromMenuTree(funCode);
	    		if(fvo!=null){
	                return SFClientUtil.showNode(funCode,1);
	    		}else{
	    			FuncRegisterVO vo= ((IFuncRegisterQueryService) NCLocator.getInstance().lookup(IFuncRegisterQueryService.class.getName())).queryByFunCode(funCode)[0];
	                FuncNodeStarter.openDialog(vo, -1, null, null, true, false);
		    		// 从打开的结点中找到刚打开的结点，返回其TOFTPANEL
		            ClientEnvironment ce = ClientEnvironment.getInstance();
		            List modules = ce.getOpenModules();
		            Iterator it = modules.iterator();
		            while (it.hasNext()) {
		                IFuncWindow frame = (IFuncWindow) it.next();
		                if (funCode.equals(frame.getModuleCode()))
		                    return frame.getFuncPanel().getToftPanel();
		            }
	    		}
	        } else {
	        	throw new BusinessException("传入数据非法：节点号为空！");
	        }
			return null;
		}
		/**
		 * @说明：<p>打开相应节点，必须有相应的节点权限</p>
		 * @param funCode
		 * @return
		 * @throws BusinessException
		 */
	 	public static IFuncWindow getOpenNode(String funCode){
			List<?> openModules = ClientEnvironment.getInstance().getOpenModules();
	        Iterator<?> it = openModules.iterator();
	        while (it.hasNext()) {
	            IFuncWindow window = (IFuncWindow) it.next();
	            if (window.getFuncPanel().getModuleCode().equals(funCode)){
	            		return window;
	            	}
	            }
			return null;
		}



		
		
		/**
	     * 功能: 汇总表体到表头(公供多表体单据使用)
	     * @param bodyedit 要汇总的表体项
	     * @param headedit 汇总的表头项
	     * @return void
	     */
	    protected void totalBodyToHead(String bodytablecode,String bodyedit,String headedit){
	        
	        int rowsNum=getBillCardPanelWrapper().getBillCardPanel().getBillModel(bodytablecode).getRowCount();
	        UFDouble value = new UFDouble();
	        for(int i=0;i<rowsNum;i++){
	            if(getBillCardPanelWrapper().getBillCardPanel().getBillModel(bodytablecode).getValueAt(i, bodyedit)!=null){
	            	value=value.add(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBillModel(bodytablecode).getValueAt(i, bodyedit).toString()));    
	            }
	       }
	       BillItem bi = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(headedit);
	       if(bi!=null){
	    	   getBillCardPanelWrapper().getBillCardPanel().getHeadItem(headedit).setValue(value);
	       }
	    }
	    
	    /**
		 * @param hvo
		 * @throws BusinessException
		 * 是否已定义审批流程
		 */
		protected boolean hashDefProce(String pk_corp,String billtype,String pk_operator) throws BusinessException {
			IWorkflowDefine wfDefine = (IWorkflowDefine) NCLocator.getInstance().lookup(IWorkflowDefine.class.getName());
			WorkflowDefinitionVO[] existProcDefs = wfDefine.findDefinitionsWithoutContent(pk_corp,IPFConfigInfo.STATEBUSINESSTYPE, billtype,
							false);
			List<String> userpro = new ArrayList<String>();//启动者为用户的审批流程
			List<String> rolepro = new ArrayList<String>();//启动者为角色的审批流程
			for (int i = 0; i < existProcDefs.length; i++) {
				if(!existProcDefs[i].getBillmaker_type().equals("ROLE")){
					userpro.add(existProcDefs[i].getBillmaker());
				}else{
					rolepro.add(existProcDefs[i].getBillmaker());
				}
			}
			
			if(userpro.contains(pk_operator)){
				return true;
			}else{
				if(rolepro.size() == 0){
					return false;
				}
				//判断启动者为角色的是否包含当前单据的操作人
				String[] pks = rolepro.toArray(new String[rolepro.size()]); 
				String sql = " select 1 from sm_user_role a where a.pk_role in ("+Toolkits.combinArrayToString3(pks)+") and a.cuserid='"+pk_operator+"'";
				IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				Object str = qurey.executeQuery(sql,new ColumnProcessor());
				if(!Toolkits.isEmpty(str)){
					return true;
				}
			}
			
			return false;
			
		}

		/**
		 * 设置Buffer中的TS到当前设置VO。 创建日期：(2004-5-14 18:04:59)
		 * 
		 * @param setVo
		 *            nc.vo.pub.AggregatedValueObject
		 * @exception java.lang.Exception
		 *                异常说明。
		 */
		protected void setTSFormBufferToVO(AggregatedValueObject setVo)
				throws java.lang.Exception {
			if (setVo == null)
				return;
			AggregatedValueObject vo = getBufferData().getCurrentVO();
			if (vo == null)
				return;
			if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {

				if (vo.getParentVO() != null && setVo.getParentVO() != null)
					setVo.getParentVO().setAttributeValue("ts",
							vo.getParentVO().getAttributeValue("ts"));
				// SuperVO[] changedvos = (SuperVO[]) setVo.getChildrenVO();
				SuperVO[] changedvos = (SuperVO[]) getChildVO(setVo);

				if (changedvos != null && changedvos.length != 0) {
					// 哈西化缓存中的字表数据
					HashMap bufferedVOMap = null;
					// SuperVO[] bufferedVOs = (SuperVO[]) vo.getChildrenVO();
					SuperVO[] bufferedVOs = (SuperVO[]) getChildVO(vo);
					if (bufferedVOs != null && bufferedVOs.length != 0) {
						bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs,
								new VOHashPrimaryKeyAdapter());
						for (int i = 0; i < changedvos.length; i++) {
							if (changedvos[i].getPrimaryKey() != null) {
								ArrayList bufferedAl = (ArrayList) bufferedVOMap
										.get(changedvos[i].getPrimaryKey());

								if (bufferedAl != null) {
									SuperVO bufferedVO = (SuperVO) bufferedAl
											.get(0);
									changedvos[i].setAttributeValue("ts",
											bufferedVO.getAttributeValue("ts"));
								}
							}
						}
					}
				}
			}
		}

		/**
		 * 获得子表数据。 创建日期：(2004-3-11 17:44:14)
		 * 
		 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
		 */
		private CircularlyAccessibleValueObject[] getChildVO(
				AggregatedValueObject retVo) {
			CircularlyAccessibleValueObject[] childVos = null;
			if (retVo instanceof IExAggVO)
				childVos = ((IExAggVO) retVo).getAllChildrenVO();
			else
				childVos = retVo.getChildrenVO();
			return childVos;
		}
		
}