package nc.ui.by.invapp.h0h003;

import java.io.File;

import jxl.write.WritableWorkbook;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.bd.pub.ISortVOByFields;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.by.invapp.h0h003.InvContrastdocVO;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.voutils.VOUtil;
 
public class ClientEventHandler extends DefaultBDBillCardEventHandle{
	
	public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception
	{		
		super.onBoAdd(bo);
		int row= getBillCardPanelWrapper().getBillCardPanel().getRowCount()-1;
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new Integer(0), row, "dr");
	}

	public void onBoEdit() throws Exception{
		super.onBoEdit();
	}
	public void onBoLineAdd() throws Exception
	{
		super.onBoLineAdd();
		int row= getBillCardPanelWrapper().getBillCardPanel().getRowCount()-1;
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new Integer(0), row, "dr");
		
	}
		
	public void onBoSave() throws Exception{
		super.onBoSave();
	}
	
    
    /**
     * 功能: 返回字符串
     * @param row 行号
     * @param str 列字符
     */
    protected String getStr(int row,String str){
       return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, str).toString();
    }

	public static WritableWorkbook ww = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null;// 文本框,用于显示文件路径
    @SuppressWarnings("static-access")
	@Override
    protected void onBoImport() throws Exception {
    	try {
            nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            res = fileChooser.showOpenDialog(getBillUI());
            if (res == 0) {
            	getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
            	getBufferData().clear();
            	updateBuffer();
                getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
                txtFile = fileChooser.getSelectedFile();
                String filepath = txtFile.getAbsolutePath();
                final WriteToExcel exceldata=new WriteToExcel();
                exceldata.creatFile(filepath);
        		Runnable checkRun = new Runnable() {
        			public void run() {
        				BannerDialog dialog = new BannerDialog(getBillUI().getParent());
        				dialog.start();
        				try {
        		            exceldata.readData(0);
        		            InvContrastdocVO[] vos = WriteToExcel.wbvo;
        		       	    if(vos!=null && vos.length>0 ){
        		       	    	ButtonObject bo = getButtonManager().getButton(IBillButton.Add);
        		       	    	onBoAdd(bo);
        		       	    	for (int i = 0; i < vos.length; i++) {
        		       	    		if(i>0){
        		       	    		onBoLineAdd();
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getPk_invcl())){
        		       	    			setRowValue(i, vos[i].getPk_invcl(), "pk_invcl");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvclasscode())){
        		       	    			setRowValue(i, vos[i].getInvclasscode(), "invclasscode");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvclassname())){
        		       	    			setRowValue(i, vos[i].getInvclassname(), "invclassname");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getPk_invbasdoc())){
        		       	    			setRowValue(i, vos[i].getPk_invbasdoc(), "pk_invbasdoc");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvcode())){
        		       	    			setRowValue(i, vos[i].getInvcode(), "invcode");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvname())){
        		       	    			setRowValue(i, vos[i].getInvname(), "invname");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvspec())){
        		       	    			setRowValue(i, vos[i].getInvspec(), "invspec");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getInvtype())){
        		       	    			setRowValue(i, vos[i].getInvtype(), "invtype");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getOldinvcode())){
        		       	    			setRowValue(i, vos[i].getOldinvcode(), "oldinvcode");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getOldinvname())){
        		       	    			setRowValue(i, vos[i].getOldinvname(), "oldinvname");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getOldinvspec())){
        		       	    			setRowValue(i, vos[i].getOldinvspec(), "oldinvspec");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getOldinvtype())){
        		       	    			setRowValue(i, vos[i].getOldinvtype(), "oldinvtype");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getMemo())){
        		       	    			setRowValue(i, vos[i].getMemo(), "memo");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getVdef1())){
        		       	    			setRowValue(i, vos[i].getVdef1(), "vdef1");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getVdef2())){
        		       	    			setRowValue(i, vos[i].getVdef2(), "vdef2");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getVdef3())){
        		       	    			setRowValue(i, vos[i].getVdef3(), "vdef3");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getVdef4())){
        		       	    			setRowValue(i, vos[i].getVdef4(), "vdef4");
        		       	    		}
        		       	    		if(!Toolkits.isEmpty(vos[i].getVdef5())){
        		       	    			setRowValue(i, vos[i].getVdef5(), "vdef5");
        		       	    		}	
        		       	    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new Integer(0), i, "dr");
        		       	    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");
        		       			}
        		       	    	getBillUI().updateButtons();
        		       	    }else{
        		       	    	getBillUI().showErrorMessage("没有取到数据,请检查EXCEL表格!");
        		       	      	return;
        		       	    }
        				} catch (Exception e) {
        					getBillUI().showErrorMessage(e.getMessage());
        				} finally {
        					dialog.end();
        				}
        		    }
        	    };
        	    new Thread(checkRun).start();
            } else {
                return;
            }
        } catch (java.lang.Throwable ivjExc) {
            getBillUI().showErrorMessage(ivjExc.getMessage());
        }
    	
    }

	private nc.ui.pub.beans.UITextField getTFLocalFile() {
        if (txtfFileUrl == null) {
            try {
                txtfFileUrl = new nc.ui.pub.beans.UITextField();
                txtfFileUrl.setName("txtfFileUrl");
                txtfFileUrl.setBounds(270, 160, 230, 26);
                txtfFileUrl.setMaxLength(2000);
                txtfFileUrl.setEditable(false);
            } catch (java.lang.Throwable e) {
                getBillUI().showErrorMessage(e.getMessage());
            }
        }
        return txtfFileUrl;
    }
	
    public void setRowValue(int row,String value,String key){
    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, key);
    }
	
	@Override
	protected void doBodyQuery(String strWhere) throws Exception,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		strWhere = strWhere+" pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr,0)=0 order by invcode ";
		SuperVO[] queryVos = getBusiDelegator().queryByCondition(
				Class.forName(getUIController().getBillVoName()[2]),
				strWhere==null?"":strWhere);

		getBufferData().clear();

		AggregatedValueObject vo = (AggregatedValueObject) Class.forName(
				getUIController().getBillVoName()[0]).newInstance();
		if(getUIController() instanceof ISortVOByFields) {
			String[] fields=((ISortVOByFields)getUIController()).getSortFields();
			if(fields!=null&&queryVos!=null) {
				VOUtil.ascSort(queryVos,fields);
			}
		}
		
		vo.setChildrenVO(queryVos);
		getBufferData().addVOToBuffer(vo);
		
		updateBuffer();
	}
    
	
}
