package nc.ui.by.invapp.billcard;

import nc.bs.trade.business.HYPubBO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.by.invapp.pub.IBYBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 说明:基础事件处理类,卡片型界面应继承此类
 */
public class AbstractEventHandler extends CardEventHandler {

	public AbstractEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	 protected void onBoSave() throws Exception {
         //非空的有效性判断
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //设置公司编码
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
         }
         //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         super.onBoSave();
         ((AbstractClientUI)getBillUI()).setDefaultData();
	 }
	 
	 protected void onBoSelfSave() throws Exception {
         super.onBoSave();
         ((AbstractClientUI)getBillUI()).setDefaultData();
	 }
	 protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		BillItem oper = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("coperatorid");
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(oper!=null){
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), row,
				"coperatorid");
		}
		BillItem dmakedate = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dmakedate");
		if(dmakedate!=null){
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), row, "dmakedate");
		}
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp(), row, "pk_corp");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("0", row, "dr");
	 }
		
     protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            String pk_corp = _getCorp().getPrimaryKey();
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and (pk_corp = '"+pk_corp+"') ");

            getBufferData().clear();
            // 增加数据到Buffer
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }

     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((AbstractClientUI)getBillUI()).setDefaultData();
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
      * 功能: 对多列前后台重复行的校验
      * @param table 表名
      * @param column 字段
      * @param rows  行数
      * @return boolean
      */
     protected boolean isRepeat(String tabName,String[] columns,int rows){
         boolean isExist=false;
         //前台重复校验
         isExist=isPreRepeat(columns,rows);
         if(isExist){
             return isExist;
         }
         //后台重复校验 
         isExist=isBackRepeat(tabName,columns,rows);
         if(isExist){
             return isExist;
         }
         return isExist;
     }
 	 /**
      * 功能: 对多列前台重复行的校验
      * @param column 字段
      * @param rows  行数
      * @return boolean
      */
     protected boolean isPreRepeat(String []columns,int rows){
         for(int i=0;i<rows-1;i++){
        	 int len=columns.length;
        	 String[]preValues=new String[len];
        	 for(int k=0;k<len;k++){
        		 preValues[k]=getStr(i,columns[k]);
        	 }
             for(int j=i+1;j<rows;j++){
            	 boolean isAll=false;
                 StringBuffer sb=new StringBuffer();
            	 String[]curValues=new String[len];
            	 for(int k=0;k<len;k++){
            		 curValues[k]=getStr(j,columns[k]);
            		 if(preValues[k].compareTo(curValues[k])==0){
            			 if(sb.length()==0){
            				 sb.append("第"+(j+1)+"行:");
            			 }
                       	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(columns[k]).getName();
                       	sb.append("\n"+key+"："+curValues[k]+"\t");
                       	isAll=true;
            		 }else{
            			 isAll=false;
            			 break;
            		 }
            	 }    
                 if(isAll){
                     sb.append("\n\t已存在！");
                     showErrorMessage(sb.toString());
                     return true;
                 }
             }
         }
         return false;
     }
     
	 /**
      * 功能: 对多列后台重复行的校验
      * @param table 表名
      * @param columns[] 字段
      * @param rows  行数
      * @return boolean
      */
     protected boolean isBackRepeat(String tabName,String[] columns,int rows){
         for(int k=0;k<rows;k++){
        	 int len=columns.length;
        	 String[]values=new String[len];
        	 for(int i=0;i<len;i++){
        		 values[i]=getStr(k,columns[i]);
        	 }
             try {
//                 boolean isExist=((ICBITF)Proxy.getInstance().getItf(ICBITF.class.getName())).existValues(tabName, columns, values,"and");
//                 if(isExist){
//                	 StringBuffer sb=new StringBuffer("第"+(k+1)+"行：");
//                	 for(int j=0;j<len;j++){
//                      	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(columns[j]).getName();
//                      	sb.append("\n"+key+"："+values[j]+"\t");
//                	 }
//                     sb.append("\n\t已存在！");
//                     showErrorMessage(sb.toString());
//                     return true;
//                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 System.out.println("向后台重复校验异常！");
             }
         }
         return false;
     }
     
     /**
      * 功能: 返回字符串
      * @param row 行号
      * @param str 列字符
      */
     protected String getStr(int row,String str){
        return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str).toString();
     }
     
     /**
      * 功能: 显示出错信息
      * @param errorMessage
      */
     protected void showErrorMessage(String errorMessage){
         getBillUI().showErrorMessage(errorMessage);
         return;
     }
     
     /**
      * 功能: 返回行数
      */
     protected int getRowCount(){
    	 return getBillCardPanelWrapper().getBillCardPanel().getRowCount();
     }
     
     @Override
    protected void onBoElse(int intBtn) throws Exception {
    	super.onBoElse(intBtn);
    }
    
    @Override
    protected void onBoQuery() throws Exception {
    	super.onBoQuery();
    }
    
    /**
     * 功能: 自定义审核按钮触发方法
     * @return 
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
}
