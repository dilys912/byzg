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
 * ˵��:�����¼�������,��Ƭ�ͽ���Ӧ�̳д���
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
         //�ǿյ���Ч���ж�
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //���ù�˾����
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
         }
         //����ʱ������Ϊ��
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
            // �������ݵ�Buffer
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }

     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((AbstractClientUI)getBillUI()).setDefaultData();
    }
     
 	 /**
      * ����: ��ͬһ��ǰ̨�ظ��е�У��
      * @param column �ֶ�
      * @param rows  ����
      * @return boolean
      */
     protected boolean isPreRepeat(String column,int rows){
         for(int i=0;i<rows-1;i++){
             String preValue=getStr(i,column);
             for(int j=i+1;j<rows;j++){
                 String curValue=getStr(j,column);                  
                 if(preValue.compareTo(curValue)==0){
                 	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(column).getName();
                     showErrorMessage("��"+(j+1)+"�У�"+key+"��"+curValue+" �ظ���");
                     return true;
                 }
             }
         }
         return false;
     }
     
	 /**
      * ����: �Զ���ǰ��̨�ظ��е�У��
      * @param table ����
      * @param column �ֶ�
      * @param rows  ����
      * @return boolean
      */
     protected boolean isRepeat(String tabName,String[] columns,int rows){
         boolean isExist=false;
         //ǰ̨�ظ�У��
         isExist=isPreRepeat(columns,rows);
         if(isExist){
             return isExist;
         }
         //��̨�ظ�У�� 
         isExist=isBackRepeat(tabName,columns,rows);
         if(isExist){
             return isExist;
         }
         return isExist;
     }
 	 /**
      * ����: �Զ���ǰ̨�ظ��е�У��
      * @param column �ֶ�
      * @param rows  ����
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
            				 sb.append("��"+(j+1)+"��:");
            			 }
                       	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(columns[k]).getName();
                       	sb.append("\n"+key+"��"+curValues[k]+"\t");
                       	isAll=true;
            		 }else{
            			 isAll=false;
            			 break;
            		 }
            	 }    
                 if(isAll){
                     sb.append("\n\t�Ѵ��ڣ�");
                     showErrorMessage(sb.toString());
                     return true;
                 }
             }
         }
         return false;
     }
     
	 /**
      * ����: �Զ��к�̨�ظ��е�У��
      * @param table ����
      * @param columns[] �ֶ�
      * @param rows  ����
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
//                	 StringBuffer sb=new StringBuffer("��"+(k+1)+"�У�");
//                	 for(int j=0;j<len;j++){
//                      	String key=getBillCardPanelWrapper().getBillCardPanel().getBodyItem(columns[j]).getName();
//                      	sb.append("\n"+key+"��"+values[j]+"\t");
//                	 }
//                     sb.append("\n\t�Ѵ��ڣ�");
//                     showErrorMessage(sb.toString());
//                     return true;
//                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 System.out.println("���̨�ظ�У���쳣��");
             }
         }
         return false;
     }
     
     /**
      * ����: �����ַ���
      * @param row �к�
      * @param str ���ַ�
      */
     protected String getStr(int row,String str){
        return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str).toString();
     }
     
     /**
      * ����: ��ʾ������Ϣ
      * @param errorMessage
      */
     protected void showErrorMessage(String errorMessage){
         getBillUI().showErrorMessage(errorMessage);
         return;
     }
     
     /**
      * ����: ��������
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
     * ����: �Զ�����˰�ť��������
     * @return 
     */
	public void onBoCheckPass() throws Exception {
    	AggregatedValueObject avo=getBufferData().getCurrentVO();
    	if(avo==null)return ;
        int result=getBillUI().showYesNoMessage("ȷ���˲�����?");
        if(result==UIDialog.ID_YES){
	        SuperVO vo=(SuperVO) avo.getParentVO();
			// �����
	        vo.setAttributeValue("vapproveid" , _getOperator());
			// �������
			vo.setAttributeValue("vapprovedate", _getDate());
        	// ������
 	        vo.setAttributeValue("vunapproveid" , null);
 			// ��������
 			vo.setAttributeValue("vunapprovedate", null);
			// ����״̬
			vo.setAttributeValue("vbillstatus", IBYBillStatus.CHECKPASS);
			HYPubBO bo = new HYPubBO();
			bo.update(vo);
	        getBufferData().updateView(); 
	        super.onBoRefresh();
        }
	}
	
	 /**
     * ����: �Զ�������ť��������
     * @return 
     */
	public void onBoCheckNoPass() throws Exception {
    	AggregatedValueObject avo=getBufferData().getCurrentVO();
    	if(avo==null)return ;
        int result=getBillUI().showYesNoMessage("ȷ���˲�����?");
        if(result==UIDialog.ID_YES){
	        SuperVO vo=(SuperVO) avo.getParentVO();
	         String  vpid= (String) vo.getAttributeValue("vapproveid");
	         if(_getOperator().equals(vpid.trim())){
	    
	        	// ������
	 	        vo.setAttributeValue("vunapproveid" , _getOperator());
	 			// ��������
	 			vo.setAttributeValue("vunapprovedate", _getDate());
	 			// ����״̬
	 			vo.setAttributeValue("vbillstatus", IBYBillStatus.FREE);
				// �����
		        vo.setAttributeValue("vapproveid" , "");
				// �������
				vo.setAttributeValue("vapprovedate", null);
				HYPubBO bo = new HYPubBO();
				bo.update(vo);
	 	        getBufferData().updateView(); 
	 	        super.onBoRefresh();
	         }else {
	        	 showErrorMessage("������������˱�����ͬһ���ˣ�");
	         }
			
        }
	}
}
