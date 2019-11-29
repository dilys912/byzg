package nc.ui.ep.dj.controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.so.pub.FetchValueBO_Client;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * ClassName:StockMaterialListDialog
 * @author   Administrator
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-6-6		下午02:25:13
 * @description:采购材料页签数据选择分页显示
 * @see 	 
 */
public class DJZBHeaderListDialog  extends UIDialog  implements  ActionListener,
	BillEditListener, BillTableMouseListener, ListSelectionListener{
  
	private UIPanel mainPanel = null;
	private BillListPanel referncedBillListPanel = null;
	private List<String> selectedPkSet = new ArrayList<String>();
    private DJZBHeaderVO[] cmMaterialVo;
    private UIButton btnOK;    
    private UIButton btnCancel;
    private JPanel panelButton;
    //加载的数据模板ID
    private String billtempletCode = null;
    public List<DJZBHeaderVO> resultVO = new ArrayList<DJZBHeaderVO>();
    
    private String bodyVOName = null;
    private Map<String, DJZBHeaderVO> superValueMap = new HashMap<String, DJZBHeaderVO>();
	public DJZBHeaderListDialog(Container paren){
		super(paren);
		resultVO.clear();
		initialize();
		initHashMap();
	}
	public DJZBHeaderListDialog(Container paren, String billTempletCode){
		super(paren);
		resultVO.clear();
		this.billtempletCode = billTempletCode;
		initialize();
		initHashMap();
	}
	
	public DJZBHeaderListDialog(Container paren,DJZBHeaderVO[] djzbVO, String bodyValueClassName){
		super(paren);
		cmMaterialVo=djzbVO;
		this.bodyVOName = bodyValueClassName;
		initialize();
		initHashMap();
	}
	private void initialize() {
		this.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIPanel topPanel = new UIPanel(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(getAppBillListPanel(), BorderLayout.CENTER);
        getContentPane().add(getUIPnlCenterrrSouth(), BorderLayout.SOUTH);
		initDataPanel();
		
		setSize(1000, 500);
	}
	
	private javax.swing.JPanel getUIPnlCenterrrSouth() {
		javax.swing.JPanel aa= new javax.swing.JPanel();
		aa.setSize( 300, 200);
		aa.setName("adddaSouth");
		aa.setLayout(new BorderLayout());
		aa.add(getBtnPanel(),BorderLayout.CENTER);
		return aa;
	}
	
	private JPanel getBtnPanel()
    {
        if (panelButton == null)
        {
            panelButton = new JPanel();
            
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setVgap(10);
            flowLayout.setHgap(10);
            panelButton.setLayout(flowLayout);
            
            panelButton.add(getBtnOK());
            panelButton.add(getBtnCancel());
        }
        
        return panelButton;
    }
	
    private UIButton getBtnCancel()
    {
        if (btnCancel == null)
        {
            btnCancel = new UIButton();
            btnCancel.setText("取消" );
           
            btnCancel.addActionListener(this);
        }
        
        return btnCancel;
    }
    
    private UIButton getBtnOK()
    {
        if (btnOK == null)
        {
            btnOK = new UIButton();
            btnOK.setText("确定");
            
            btnOK.addActionListener(this);
        }
        
        return btnOK;
    }
	/**
	 * 初始化值
	 */
	private void initDataPanel() {
		getAppBillListPanel().getBodyTable().removeSortListener();
		getAppBillListPanel().addBodyEditListener(this);
		getAppBillListPanel().getHeadBillModel().setBodyDataVO(cmMaterialVo);
		getAppBillListPanel().addMouseListener(this);
		getAppBillListPanel().setMultiSelect(true);
//		getAppBillListPanel().getParentListPanel().setPageNavigationBarVisible(true);
		getAppBillListPanel().getBodyBillModel().execLoadFormula();
//		getAppBillListPanel().getBodyBillModel().removeRowStateChangeEventListener();
//		getAppBillListPanel().getBodyBillModel().addRowStateChangeEventListener(this);
		getAppBillListPanel().setEnabled(true);
		getAppBillListPanel().updateUI();
	}	
	private BillListPanel getAppBillListPanel() {
		if (referncedBillListPanel == null) {
			referncedBillListPanel = new BillListPanel();
			referncedBillListPanel.loadTemplet("D1", null,
					ClientEnvironment.getInstance().getUser().getPrimaryKey(),
					null);
		}
		return referncedBillListPanel;
	}
   
	/**
	 * 
	 * onBtnSelectAll:(这里用一句话描述这个方法的作用)
@decription:选择所有的数据
	 *
	 * @param  @throws BusinessException    设定文件
	 * @return void    DOM对象
	 * @throws 
	 * @since  CodingExample　Ver 1.1
	 */
	private void onBtnSelectAll() throws BusinessException {
	   if(selectedPkSet == null){
		   selectedPkSet = new ArrayList<String>();
	   }
	   selectedPkSet.clear();
       int rowCount=getAppBillListPanel().getBodyTable().getRowCount();
       for(int i=0;i<rowCount;i++){
    	   getAppBillListPanel().getBodyBillModel().setRowState(i,BillModel.SELECTED);
       }
       for(int i = 0; cmMaterialVo != null && i < cmMaterialVo.length; i ++){
    	   selectedPkSet.add(cmMaterialVo[i].getPrimaryKey());
       }
	}
	/**
	 * 
	 * onBtnDeselectAll:(这里用一句话描述这个方法的作用)
	 *@description:取消选择所有的文件
	 * @param      设定文件
	 * @return void    DOM对象
	 * @throws 
	 * @since  CodingExample　Ver 1.1
	 */
	private void onBtnDeselectAll() {
		int rowCount=getAppBillListPanel().getBodyTable().getRowCount();
        for(int i=0;i<rowCount;i++){
     	   getAppBillListPanel().getBodyBillModel().setRowState(i,BillModel.UNSTATE);
        }
        selectedPkSet.clear();
	}
	
	
	 public void actionPerformed(ActionEvent evt)
	    {
	        if (evt.getSource() == getBtnCancel()){
	            closeCancel();
	           
	            return;
	        }        
	        int iResult = 0;        
	        if (evt.getSource() == getBtnOK()){
	           for(DJZBHeaderVO tempValue : cmMaterialVo){
	        	   String primaryKey = tempValue.getPrimaryKey();
	        	   if(selectedPkSet.contains(primaryKey)){
	        		   resultVO.add(tempValue);
	        	   }
	           }
	        }  
	        
	        setResult(iResult);        
	        close();
	        fireUIDialogClosed(new UIDialogEvent(this, UIDialogEvent.WINDOW_OK));

	    }
	 
	 public List<DJZBHeaderVO> getSelectVo(){
		 return resultVO;
	 }
	
	 /**
	  * 
	  * initHashMap:(这里用一句话描述这个方法的作用)
		@description：初始化数据的VO，方便后面进行取数,提高效率
	  *
	  * @param      设定文件
	  * @return void    DOM对象
	  * @throws 
	  * @since  CodingExample　Ver 1.1
	  */
	 protected void initHashMap(){
		 superValueMap.clear();
		 for(int i = 0; cmMaterialVo != null && i < cmMaterialVo.length ; i++){
			 superValueMap.put(cmMaterialVo[i].getPrimaryKey(), cmMaterialVo[i]);
		 }
	 }
	 /**
	  * (non-Javadoc)
	  * @see nc.ui.pub.bill.BillEditListener#afterEdit(nc.ui.pub.bill.BillEditEvent)
	  */
	public void afterEdit(BillEditEvent e) 
	{
		if(e.getPos() == BillListPanel.BODY)
		{
			int row = e.getRow();
			String key = e.getKey();
			CircularlyAccessibleValueObject valueObject = getAppBillListPanel().getBodyBillModel().getBodyValueRowVO(row, bodyVOName);
			DJZBHeaderVO superVO;
			try {
				superVO = superValueMap.get(valueObject.getPrimaryKey());
				if(superVO != null){
					superVO.setAttributeValue(e.getKey(), e.getValue());
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}

	public void bodyRowChange(BillEditEvent e) {
		
		
	}

	public List<DJZBHeaderVO> getResultVO() {
		return resultVO;
	}
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int headRow = ((javax.swing.ListSelectionModel) e.getSource()).getAnchorSelectionIndex();
			if (headRow >= 0) {
				headRowChange(headRow);
			}
		}
	}
	/**
	 * 只对表头进行处理
	 * <li>行切换 事件
	 * <li>双击 事件
	 * <li>WARN::行切换事件发生在双击事件之前
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getAppBillListPanel().getHeadBillModel().getValueAt(iNewRow, "vouchid") != null) {
			if (!getAppBillListPanel().setBodyModelData(iNewRow)) {
				//1.初次载入表体数据
				loadBodyData(iNewRow);
				//2.备份到模型中
				getAppBillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getAppBillListPanel().repaint();
	}
	/**
	 * 根据主表获取子表数据
	 * @param row 选中的表头行
	 */
	@SuppressWarnings("restriction")
	public void loadBodyData(int row) {
		try {
			//获得主表ID
			String id = getAppBillListPanel().getHeadBillModel().getValueAt(row, "vouchid").toString();
			//查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData("D1",
					id, getBodyCondition());
			
			getAppBillListPanel().setBodyValueVO(tmpBodyVo);
			getAppBillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			System.out.println("数据加载失败!");
			e.printStackTrace(System.out);
		}

	}
	/**
	 * 子表条件语句
	 * @return
	 */
	public String getBodyCondition() {
		return null;
	}
	public void mouse_doubleclick(BillMouseEnent e) {
		int iNewRow = e.getRow();
		String pk = getAppBillListPanel().getHeadBillModel().getValueAt(iNewRow, "vouchid").toString();
		if (getAppBillListPanel().getHeadBillModel().getValueAt(iNewRow, "vouchid") != null) {
			if (!getAppBillListPanel().setBodyModelData(iNewRow)) {
				//1.初次载入表体数据
				loadBodyData(iNewRow);
				//2.备份到模型中
				getAppBillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getAppBillListPanel().repaint();
		
		BillModel bodyModel = getAppBillListPanel().getBodyBillModel();
		BillModel headModel = getAppBillListPanel().getHeadBillModel();
		//选取处理
		if (getAppBillListPanel().isMultiSelect()) {
			if (headModel.getRowState(iNewRow) == BillModel.SELECTED) {
				headModel.setRowState(iNewRow, BillModel.UNSTATE);
				selectedPkSet.remove(pk);
				for (int i = 0; i < bodyModel.getRowCount(); i++)
					bodyModel.setRowState(i, BillModel.UNSTATE);
			} else {
				headModel.setRowState(iNewRow, BillModel.SELECTED);
				selectedPkSet.add(pk);
				for (int i = 0; i < bodyModel.getRowCount(); i++)
					bodyModel.setRowState(i, BillModel.SELECTED);
			}
			//备份数据
			getAppBillListPanel().setBodyModelDataCopy(iNewRow);
		}
	}
}
