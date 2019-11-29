/*
 * 创建日期 2005-8-11
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * @author：yangbo
 * 提供采购发票VMI汇总参照，支持流程配置
 */

package nc.ui.ic.pub.pf;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.busibean.ISysInit;
import nc.itf.uap.busibean.ISysInitQry;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.bill.bodyctl.ICBDefBusiCtl;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ic.pub.vmi.VMISplitModeDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.service.LocalCallService;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.ic.pub.vmi.VmiSumVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.service.ServcallVO;

public class QryVMISumDlg extends ICBillSourceDLG {
 	//小数位数精度
 	private int m_iaScale[] ;
	//private String m_sNodeBO_Client = "nc.ui.ic.ic201.GeneralHBO_Client";
	private String m_CorpID ;
	private String m_UserID ;
		
	//取消
	private UIButton ivjbtnCancel ;
	//确定
	private UIButton ivjbtnOk ;
	//查询
	private UIButton ivjbtnQuery ;
	//全选
	private UIButton ivjbtnSelAll ;
	//全消
	private UIButton ivjbtnUnSelAll ;
	//分单方式
	//private UIComboBox ivjComBoxSplitMode ;
	//分单方式
	
	//合计
	private UIButton ivjbtnTold;
	private UIButton ivjbtnSplitMode ;
	
	
	private UIPanel ivjPanlCmd;
	
	private UIPanel ivjUIDialogContentPane ;
	
	private Integer iSplitMode ;
	
	private VMISplitModeDlg ivjVMISplitModeDlg;
	

/**
 * QryPurBillDlg 构造子注解。
 * @param pkField java.lang.String
 * @param pkCorp java.lang.String
 * @param operator java.lang.String
 * @param funNode java.lang.String
 * @param queryWhere java.lang.String
 * @param billType java.lang.String
 * @param businessType java.lang.String
 * @param templateId java.lang.String
 * @param currentBillType java.lang.String
 * @param parent java.awt.Container
 *
 */
public QryVMISumDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, java.awt.Container parent) {
	super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);
}

/**
 * BillSourceUI 构造子注解。
 * 根据类名称，where语句构造参照界面
 * @param pkField java.lang.String
 * @param pkCorp java.lang.String
 * @param operator java.lang.String
 * @param funNode java.lang.String
 * @param queryWhere java.lang.String
 * @param billType java.lang.String
 * @param businessType java.lang.String
 * @param templateId java.lang.String
 * @param currentBillType java.lang.String
 * @param parent java.awt.Container
 */
//public QryVMISumDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere,
//		String billType, String businessType, String templateId, String currentBillType, String nodeKey, Object userObj,
//		java.awt.Container parent) {
//	super(pkField, pkCorp, operator, funNode, queryWhere,
//			billType, businessType, templateId, currentBillType, nodeKey, userObj,
//			parent);
//}


/**
 * 返回 btnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnCancel() {
 if (ivjbtnCancel == null) {
  try {
   ivjbtnCancel = new nc.ui.pub.beans.UIButton();
   ivjbtnCancel.setName("btnCancel");
   ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "取消"*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnCancel;
}

/**
 * 返回 btnOk 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnOk() {
 if (ivjbtnOk == null) {
  try {
   ivjbtnOk = new nc.ui.pub.beans.UIButton();
   ivjbtnOk.setName("btnOk");
   ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "确定"*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
  }
 }
 return ivjbtnOk;
}

/**
 * 返回 btnQuery 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnQuery() {
 if (ivjbtnQuery == null) {
  try {
   ivjbtnQuery = new nc.ui.pub.beans.UIButton();
   ivjbtnQuery.setName("btnQuery");
   ivjbtnQuery.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*@res "查询"*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnQuery;
}
// add told wkf start
/**
 * 返回 btnQuery 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnTold() {
 if (ivjbtnTold == null) {
  try {
	  ivjbtnTold = new nc.ui.pub.beans.UIButton();
	  ivjbtnTold.setName("btnTold");
	  ivjbtnTold.setText("合计");
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnTold;
}
//add told wkf end

/**
 * 返回 ivjbtnSelAll 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnSelAll() {
 if (ivjbtnSelAll == null) {
  try {
  	ivjbtnSelAll = new nc.ui.pub.beans.UIButton();
  	ivjbtnSelAll.setName("btnSelAll");
  	ivjbtnSelAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*@res "全选"*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnSelAll;
}

/**
 * 返回 ivjbtnUnSelAll 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnUnSelAll() {
 if (ivjbtnUnSelAll == null) {
  try {
  	ivjbtnUnSelAll = new nc.ui.pub.beans.UIButton();
  	ivjbtnUnSelAll.setName("btnUnSelAll");
  	ivjbtnUnSelAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*@res "全消"*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnUnSelAll;
}

/**
 * 返回 ivjComBoxSplitMode 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
///* 警告：此方法将重新生成。 */
//private UIComboBox getComBoxSplitMode() {
// if (ivjComBoxSplitMode == null) {
//  try {
//  	ivjComBoxSplitMode = new UIComboBox();
//  	ivjComBoxSplitMode.setName("ComBoxSplitMode");
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000275")/*@res "供应商"*/);
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000042")/*@res "供应商+存货"*/);
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000043")/*@res "消耗汇总纪录"*/);
//  	// user code begin {1}
//    // user code end
//  } catch (java.lang.Throwable ivjExc) {
//   // user code begin {2}
//   // user code end
//   //handleException(ivjExc);
//  }
// }
// return ivjComBoxSplitMode;
//}

/**
 * 返回 ivjComBoxSplitMode 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private UIButton getbtnSplitMode() {
 if (ivjbtnSplitMode == null) {
  try {
  	ivjbtnSplitMode = new UIButton();
  	ivjbtnSplitMode.setName("lbSplitMode");
  	ivjbtnSplitMode.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000181"));///*@res ""分单方式""*/);
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjbtnSplitMode;
}


/**
 * 返回 PanlCmd 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPanlCmd() {
 if (ivjPanlCmd == null) {
  try {
   ivjPanlCmd = new nc.ui.pub.beans.UIPanel();
   ivjPanlCmd.setName("PanlCmd");
   ivjPanlCmd.setPreferredSize(new java.awt.Dimension(0, 40));
   ivjPanlCmd.setLayout(new java.awt.FlowLayout());
   getPanlCmd().add(getbtnSelAll(), getbtnSelAll().getName());
   getPanlCmd().add(getbtnUnSelAll(), getbtnUnSelAll().getName());
   getPanlCmd().add(getbtnQuery(), getbtnQuery().getName());
   getPanlCmd().add(getbtnSplitMode(), getbtnSplitMode().getName());
   //getPanlCmd().add(getComBoxSplitMode(), getComBoxSplitMode().getName());
   
   
   getPanlCmd().add(getbtnOk(), getbtnOk().getName());
   getPanlCmd().add(getbtnCancel(), getbtnCancel().getName());
   getPanlCmd().add(getbtnTold(),getbtnTold().getName());//合计
  
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjPanlCmd;
}


/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
protected JPanel getUIDialogContentPane() {
 if (ivjUIDialogContentPane == null) {
  try {
   ivjUIDialogContentPane = new UIPanel();
   ivjUIDialogContentPane.setName("UIDialogContentPane");
   ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
   //2003-05-12平台进行显示调用
   //getUIDialogContentPane().add(getbillListPanel(), "Center");
   getUIDialogContentPane().add(getPanlCmd(), "South");
   // user code begin {1}
   // user code end
  } catch (java.lang.Throwable ivjExc) {
   // user code begin {2}
   // user code end
   //handleException(ivjExc);
  }
 }
 return ivjUIDialogContentPane;
}

public void addListenerEvent() {
	 //super.addListenerEvent();

	//getbillListPanel().addEditListener(this);
	//getbillListPanel().addMouseListener(this);
	
	//表头的列表行切换事件处理器
	ListSelectionListener lsl = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			for(int i=0,loop=getbillListPanel().getHeadTable().getRowCount();i<loop;i++){
				getbillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);
			}
			int[] selrows = getbillListPanel().getHeadTable().getSelectedRows();
			if(selrows!=null){
				for(int i=0,loop=selrows.length;i<loop;i++){
					getbillListPanel().getHeadBillModel().setRowState(selrows[i], BillModel.SELECTED);
				}
			}
//			if (!e.getValueIsAdjusting()) {
//				int row = ((javax.swing.ListSelectionModel) e.getSource()).getAnchorSelectionIndex();
//				if (row >= 0)
//					getbillListPanel().getHeadBillModel().setRowState(row, BillModel.SELECTED);
//			}
		}
	};
	getbillListPanel().getParentListPanel().getTable().getSelectionModel().addListSelectionListener(lsl);
	
	 getbtnOk().addActionListener(this);
	 getbtnCancel().addActionListener(this);
	 getbtnQuery().addActionListener(this);
	 getbtnSelAll().addActionListener(this);
	 getbtnUnSelAll().addActionListener(this);
	 getbtnSplitMode().addActionListener(this);
	 getbtnTold().addActionListener(this);//合计
	 
}


/**
 * Invoked when an action occurs.
 */
public void actionPerformed(ActionEvent e) {
 if(e.getSource() == getbtnOk()) {
   onOk();
 } 
 else if(e.getSource() == getbtnCancel()) {
   closeCancel();
 } 
 else if(e.getSource() == getbtnQuery()) {
   onQuery();
 } 
 else if(e.getSource() == getbtnSelAll()) {
    onSelAll();
 } 
 else if(e.getSource() == getbtnUnSelAll()) {
 	onUnSelAll();
 } 
 else if(e.getSource() == getbtnSplitMode()) {
 	onBtnSplitMode();
 } 
 else if(e.getSource() == getbtnTold()){
    onTold();//wkf add 2014-03-11
 }
}

/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.ui.pub.bill.BillListPanel
 */
public nc.ui.pub.bill.BillListPanel getbillListPanel() {
	
	
	if (ivjbillListPanel == null) {
		super.getbillListPanel();
		//获取相关参数
		initSysParam();
		//设置精度
		setBillListDigit();
		//设置分单方式
		//getComBoxSplitMode().setSelectedIndex(getSplitBillMode().intValue());
		
		ivjbillListPanel.getHeadTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	}
	return ivjbillListPanel;
	
}
/**
 * 创建者：王乃军
 * 功能：返回表体查询条件。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public String getBodyCondition() {
	return null;
}

/**
 * 创建者：王乃军
 * 功能：返回表头查询条件。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public String getHeadCondition() {

	String sHeadCondition = null;
	//加上公司，和3 签字标志，不能是 6 冻结和 5 关闭的调拨订单
	if (getPkCorp() != null && getPkCorp().trim().length() > 0)
		sHeadCondition =
			" pk_corp = '"
				+ getPkCorp().trim()
        //modified by liuzy 2008-02-29 家家购物NC
        //供应商寄存管理中,消耗汇总数为负数时,可以暂估,但无法参照生成红字发票
//                + "' and dr = 0 and coalesce(binvoiceendflag,'N') = 'N' ";//and coalesce(noutnum,0)>0 ";
				  + "' and dr = 0 and coalesce(binvoiceendflag,'N') = 'N' and coalesce(noutnum,0)<>0";//edit by zwx 2015-3-18 查询结果去除0数据
	else
//		sHeadCondition = " dr = 0 and coalesce(binvoiceendflag,'N') = 'N' ";//and coalesce(noutnum,0)>0 ";
	 	sHeadCondition = " dr = 0 and coalesce(binvoiceendflag,'N') = 'N' and coalesce(noutnum,0)<>0";//edit by zwx 2015-3-18
	 	
	return sHeadCondition;
}

/**
 * 创建者：王乃军
 * 功能：读系统参数
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期：2002-10-17
 * 修改人：赵宇煌
 * 修改原因：
 */
protected void initSysParam() {
	try {

		m_iaScale = new int[] { 8, 8, 8, 8, 8 };
		//参数编码	含义				缺省值
		//BD501	数量小数位			    2
		//BD502	辅计量数量小数位		2
		//BD503	换算率	小数位			2
		//BD505	单价小数位	            2
		//BD301	本币小数位	            2
		
		
		String para[] = { "BD501", "BD502", "BD503", "BD505", "BD301"};
		Hashtable h = null;
		try {
			h = nc.ui.pub.para.SysInitBO_Client.queryBatchParaValues(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),para);
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}

		Object otemp = null;

		otemp = h.get("BD501");
		//数量
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM] = Integer.parseInt(otemp.toString().trim());
		
		//辅数量
		otemp = h.get("BD502");
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.ASSIST_NUM] = Integer.parseInt(otemp.toString().trim());

		otemp = h.get("BD503");
		//换算率
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.CONVERT_RATE] = Integer.parseInt(otemp.toString().trim());
		
		
		otemp = h.get("BD505");
		//单价
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.PRICE] = Integer.parseInt(otemp.toString().trim());
		
		ICBcurrArithUI curr = new ICBcurrArithUI(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		m_iaScale[nc.vo.ic.pub.bill.DoubleScale.MNY] = curr.getBusiCurrDigit(curr.getLocalCurrPK());

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
		//if (e instanceof nc.vo.pub.BusinessException)
			//MessageDialog.showErrorDlg(this, "错误：", e.getMessage());
	}
}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-7-6 19:38:38)
 */
public void onOk() {
	//super.onOk();
	if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
//  modified by liuzy 2008-02-29 家家购物NC
    //供应商寄存管理中,消耗汇总数为负数时,可以暂估,但无法参照生成红字发票
//		try{
//			onCheckData();
//		}catch(ValidationException e){
//			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*错误*/,e.getMessage());
//			return;
//		}
				
		retBillVo = getRetVo();
		retBillVos = getRetVos();
		
	}else{
		retBillVo = null;
		retBillVos = null;
	}
	this.getAlignmentX();
	this.closeOK();
}

/**
 * 查询
 * 创建日期：(2001-7-6 19:38:51)
 */
public void onQuery() {
	
	QueryConditionClient queryCondition = getQueyDlg();
	queryCondition.showModal();
	if (queryCondition.isCloseOK()) {
		
		getbillListPanel().getHeadBillModel().clearBodyData();
		
		//返回查询条件
		//m_whereStr = voConditions[0].getWhereSQL()//queryCondition.getWhereSQL();
		loadHeadData();
		
	}
	//super.onQuery();
	retBillVo = null;
	retBillVos = null;
}

/*
 * add以料号小计，选择的料号合计功能
 * 王凯飞
 * 2013-03-11
 * start
 * */
private int m_RowCount;

public void onTold(){
	int m_nSelectedRowCoun=getbillListPanel().getHeadTable().getSelectedRowCount();
	if (m_nSelectedRowCoun==0) {
	      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), "请选择要合计的行！");
	      return;
	    }
	String vinvname =null;
	UFDouble noutnum = new UFDouble(0);//数量
//	BillItem[] ck=getbillListPanel().getHeadBillModel().getBodyItems();
	int[] selrows = getbillListPanel().getHeadTable().getSelectedRows();
	Map<String,UFDouble> told=new HashMap();
	UFDouble temp;
	UFDouble temp1= new UFDouble(0);
	UFDouble temp2= new UFDouble(0);
	UFDouble noutinnum = new UFDouble(0);//退库数
	UFDouble ninvoicenum = new UFDouble(0);//累计开票数
	for(int i =0;i<selrows.length;i++){
		int row = selrows[i];
//		if(getbillListPanel().getHeadBillModel().getRowState(row)==4){
			noutnum=(getbillListPanel().getHeadBillModel().getValueAt(row,"noutnum")==null)?new UFDouble(0):
				new UFDouble(getbillListPanel().getHeadBillModel().getValueAt(row,"noutnum").toString());
			vinvname=(getbillListPanel().getHeadBillModel().getValueAt(row,"vinvname")==null)?new String(""):
				new String(getbillListPanel().getHeadBillModel().getValueAt(row,"vinvname").toString());
			
			noutinnum = getbillListPanel().getHeadBillModel().getValueAt(row,"noutinnum")==null?new UFDouble(0):
				new UFDouble(getbillListPanel().getHeadBillModel().getValueAt(row,"noutinnum").toString());
			
			ninvoicenum = getbillListPanel().getHeadBillModel().getValueAt(row,"ninvoicenum")==null?new UFDouble(0):
				new UFDouble(getbillListPanel().getHeadBillModel().getValueAt(row,"ninvoicenum").toString());
			if(told.containsKey(vinvname)){
				temp=told.get(vinvname);
				told.put(vinvname, temp.add(noutnum));
			}else{
				told.put(vinvname,noutnum);
			}
			temp1 = temp1.add(noutinnum);
			temp2 = temp2.add(ninvoicenum);
//		}
	}
	UFDouble heji = new UFDouble(0);
	Iterator<String> it=told.keySet().iterator();
	String xiaoji="";
	while(it.hasNext()){
		vinvname=it.next();
		temp=told.get(vinvname);
		xiaoji=xiaoji+vinvname+"   数量:"+temp+"\n";
		heji=heji.add(temp);
	}
	//出库合计
	String hj="出库合计:"+heji.toString()+"\n";
	//退库数
	String tks ="退库数："+temp1.toString()+"\n";
	//实际消耗数
	String sj="实际消耗数："+temp2.toString();
	MessageDialog.showHintDlg(this, "合计", xiaoji+hj+tks+sj);
}

/*
 * add以料号小计，选择的料号合计功能
 * 王凯飞
 * 2013-03-11
 * end
 * */

/**
 * 以‘取消’模式关闭对话框 业务节点根据需要修改
 */
public void closeCancel() {
	super.closeCancel();
}

/**
 *
 */
public void onBtnSplitMode() {
	getVMISplitModeDlg().showModal();
}


/**
 * 全选
 * 创建日期：(2001-7-6 19:38:51)
 */
public void onSelAll() {
	
	
	BillModel headModel = getbillListPanel().getHeadBillModel();
	
	for(int i=0,loop=headModel.getRowCount();i<loop;i++){
		//选取处理
		headModel.setRowState(i, BillModel.SELECTED);
		
	}
	getbillListPanel().getHeadTable().selectAll();
}

/**
 * 全消
 * 创建日期：(2001-7-6 19:38:51)
 */
public void onUnSelAll() {
	BillModel headModel = getbillListPanel().getHeadBillModel();
	for(int i=0,loop=headModel.getRowCount();i<loop;i++){
		//选取处理
		headModel.setRowState(i, BillModel.UNSTATE);
		
	}
	getbillListPanel().getHeadTable().clearSelection();
}

/**
 * 设置显示精度
 * 创建日期：(2001-7-6 19:38:51)
 */
public void setBillListDigit() {
	String[] numitemname = {
//		//数量
//		"noutnum",
//		//已开票数量
//		"ntotalinvoicenum",
//		//本次开票数量
//		"ninvoicenum",
//		//累计结算数量
//		"naccountnum",
			
			"ninitnum", "ninnum", "ninoutnum", "noutnum", "noutinnum", "ntransnum",
	        "nfinalnum", "naccountnum", "ntotalinvoicenum","ninvoicenum"
		
	};
	BillItem bim = null;
	for(int i=0,loop=numitemname.length;i<loop;i++){
		bim = getbillListPanel().getHeadItem(numitemname[i]);
		if(bim!=null){
			bim.setDecimalDigits(m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM]);
		}
	}
//	累计结算金额
	bim = getbillListPanel().getHeadItem("naccountmny");
	if(bim!=null)
		bim.setDecimalDigits(m_iaScale[nc.vo.ic.pub.bill.DoubleScale.MNY]);
	
}


/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
public void loadBodyData(int row) {

}


/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
public void loadHeadData() {
	try {
		//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
//		穿透查询
		ConditionVO[] voConditions = getQueyDlg().getConditionVO();
		if(voConditions!=null && voConditions.length>0){
			ConvertQueryCondition.getConvertedVO(voConditions,
					getPkCorp());
			m_whereStr = voConditions[0].getWhereSQL(voConditions);
		}
		
		String tmpWhere = null;
		if (getHeadCondition() != null) {
			if (m_whereStr == null) {
				tmpWhere = " (" + getHeadCondition() + ")";
			} else {
				tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
			}
		} else {
			tmpWhere = m_whereStr;
		}
			
		CircularlyAccessibleValueObject[] headVos=(CircularlyAccessibleValueObject[])GenMethod.callICService("nc.bs.ic.pub.vmi.VmiSumDMO","qryVmiHeaderByWhere",new Class[] { String.class },new Object[] { tmpWhere });
		
		if(headVos!=null && headVos.length>0){

			getbillListPanel().setHeaderValueVO(headVos);
			getbillListPanel().getHeadBillModel().execLoadFormula();
			
			setItemEdit("ninvoicenum",true);
			
			setNinvoicenum();
		
		}

		//lj+ 2005-4-5
		//selectFirstHeadRow();
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("数据加载失败！");
		nc.vo.scm.pub.SCMEnv.error(e);
	}
}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
public AggregatedValueObject getRetVo() {
	AggregatedValueObject sumvo= null;
	AggregatedValueObject[] sumvos = getSelectVos();
	if(sumvos!=null && sumvos.length>0)
		sumvo = sumvos[0];

	return sumvo;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
public AggregatedValueObject[] getRetVos() {

	return getSelectVos();
}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
public AggregatedValueObject[] getSelectVos() {
	//分单
	//供应商 0 
  	//供应商+存货 1;
  	//消耗汇总纪录 2;
	ArrayList alist = new ArrayList();
	VmiSumVO[] sumvos = null;
	VmiSumVO sumvo= null;
	VmiSumHeaderVO headvo = null;
	
	Integer iIMode = getVMISplitModeDlg().getSelectedSplitMode();
	for(int i=0,loop=getbillListPanel().getHeadBillModel().getRowCount();i<loop;i++){
		if(getbillListPanel().getHeadBillModel().getRowState(i) != BillModel.SELECTED)
			continue;
		sumvo= new VmiSumVO();
		headvo =(VmiSumHeaderVO)getbillListPanel().getHeadBillModel().getBodyValueRowVO(i,"nc.vo.ic.pub.vmi.VmiSumHeaderVO");
		headvo.setAttributeValue("isplitmode",iIMode);
		sumvo.setParentVO(headvo);
		sumvo.setChildrenVO(null);
		alist.add(sumvo);
	}
	if(alist.size()>0)
		sumvos = (VmiSumVO[])alist.toArray(new VmiSumVO[alist.size()]);
	return sumvos;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
private void setItemEdit(String itemkey,boolean bedit){
	BillItem bi = getbillListPanel().getHeadBillModel().getItemByKey(itemkey);
	if(bi==null)
		return;
	bi.setEdit(bedit);
	bi.setEnabled(bedit);
	for(int i=0,loop=getbillListPanel().getHeadBillModel().getRowCount();i<loop;i++){
		getbillListPanel().getHeadBillModel().setCellEditable(i,itemkey,bedit);
	}
	
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
private void setNinvoicenum(){
	
	UFDouble noutnum = null,noutinnum=null;
	UFDouble ntatolinvoicenum = null;
	UFDouble d0=new UFDouble(0);
	Object otemp = null;
	
	// add by liuzy 2008-11-12 下午04:54:17 家家购物可开票数量计算有问题
	UFDouble dCouldInvoicenum = new UFDouble(0);
	for(int i=0,loop=getbillListPanel().getHeadBillModel().getRowCount();i<loop;i++){
		otemp = getbillListPanel().getHeadBillModel().getValueAt(i,"noutnum");
		if(otemp == null || otemp.toString().trim().length()<=0)
			noutnum = d0;
		else
			noutnum = new UFDouble(otemp.toString().trim());
		
		otemp = getbillListPanel().getHeadBillModel().getValueAt(i,"noutinnum");
		if(otemp == null || otemp.toString().trim().length()<=0)
			noutinnum = d0;
		else
			noutinnum = new UFDouble(otemp.toString().trim());
		
		otemp = getbillListPanel().getHeadBillModel().getValueAt(i,"ntotalinvoicenum");
		if(otemp == null || otemp.toString().trim().length()<=0)
			ntatolinvoicenum = d0;
		else
			ntatolinvoicenum = new UFDouble(otemp.toString().trim());
		
		if(noutinnum.compareTo(noutnum) > 0)
			dCouldInvoicenum = noutnum.sub(noutinnum).add(ntatolinvoicenum);
		else
			dCouldInvoicenum = noutnum.sub(noutinnum).sub(ntatolinvoicenum);
			
		
//		getbillListPanel().getHeadBillModel().setValueAt(noutnum.sub(noutinnum).sub(ntatolinvoicenum),i,"ninvoicenum");
		getbillListPanel().getHeadBillModel().setValueAt(dCouldInvoicenum,i,"ninvoicenum");
	}
	
}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-4-23 9:17:37)
 */
private void onCheckData() throws ValidationException{
	
	int[] selrows = getbillListPanel().getHeadTable().getSelectedRows();
	
	if(selrows==null || selrows.length<=0){
		return;
	}
	
	UFDouble noutnum = null,noutinnum=null;
	UFDouble ntotalinvoicenum = null,ninvoicenum=null;
	UFDouble d0=new UFDouble(0);
	for(int i=0,loop=selrows.length;i<loop;i++){
		//实出数量
		noutnum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"noutnum"));
		if(noutnum==null)
			noutinnum = d0;
		//本期退库
		noutinnum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"noutinnum"));
		if(noutinnum==null)
			noutinnum = d0;
		//累计开票数量
		ntotalinvoicenum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"ntotalinvoicenum"));
		if(ntotalinvoicenum==null)
			ntotalinvoicenum = d0;
		//发票数量
		ninvoicenum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"ninvoicenum"));
		if(ninvoicenum==null)
			ninvoicenum = d0;
		
		// commet by zip: 2014/1/4 No 109(临时用)
		if(noutnum.abs().compareTo(noutinnum.abs().add(ntotalinvoicenum.abs()).add(ninvoicenum.abs()))<0){
			throw new ValidationException(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000049",
							null,new String[]{(selrows[i]+1)+""})/*@res 行{0}本次开票数量超过VMI汇总可开票数量！*/);
		}
		// end
		
	}
	
}


/**
 * 鼠标双击事件。
 * 创建日期：(2001-5-9 8:52:00)
 * @param row int
 */
public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
	if (e.getPos() == BillItem.HEAD) {
		//只对表头的双击事件进行响应,表体的双击事件在BillListPanel.BodyMouseListener中响应
		int row = e.getRow();
		
		BillModel headModel = getbillListPanel().getHeadBillModel();
		//选取处理
		if (headModel.getRowState(row) == BillModel.SELECTED) {
			headModel.setRowState(row, BillModel.UNSTATE);
			
		} else {
			headModel.setRowState(row, BillModel.SELECTED);
			
		}
	}
}

/**
 * get。
 * 创建日期：(2001-5-9 8:52:00)
 * @param row int
 */

private VMISplitModeDlg getVMISplitModeDlg(){
	if(ivjVMISplitModeDlg==null){
		ivjVMISplitModeDlg = new VMISplitModeDlg(this);
	}
	return ivjVMISplitModeDlg;
}


}
