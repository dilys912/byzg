/*
 * �������� 2005-8-11
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * @author��yangbo
 * �ṩ�ɹ���ƱVMI���ܲ��գ�֧����������
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
 	//С��λ������
 	private int m_iaScale[] ;
	//private String m_sNodeBO_Client = "nc.ui.ic.ic201.GeneralHBO_Client";
	private String m_CorpID ;
	private String m_UserID ;
		
	//ȡ��
	private UIButton ivjbtnCancel ;
	//ȷ��
	private UIButton ivjbtnOk ;
	//��ѯ
	private UIButton ivjbtnQuery ;
	//ȫѡ
	private UIButton ivjbtnSelAll ;
	//ȫ��
	private UIButton ivjbtnUnSelAll ;
	//�ֵ���ʽ
	//private UIComboBox ivjComBoxSplitMode ;
	//�ֵ���ʽ
	
	//�ϼ�
	private UIButton ivjbtnTold;
	private UIButton ivjbtnSplitMode ;
	
	
	private UIPanel ivjPanlCmd;
	
	private UIPanel ivjUIDialogContentPane ;
	
	private Integer iSplitMode ;
	
	private VMISplitModeDlg ivjVMISplitModeDlg;
	

/**
 * QryPurBillDlg ������ע�⡣
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
 * BillSourceUI ������ע�⡣
 * ���������ƣ�where��乹����ս���
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
 * ���� btnCancel ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnCancel() {
 if (ivjbtnCancel == null) {
  try {
   ivjbtnCancel = new nc.ui.pub.beans.UIButton();
   ivjbtnCancel.setName("btnCancel");
   ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*@res "ȡ��"*/);
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
 * ���� btnOk ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnOk() {
 if (ivjbtnOk == null) {
  try {
   ivjbtnOk = new nc.ui.pub.beans.UIButton();
   ivjbtnOk.setName("btnOk");
   ivjbtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000044")/*@res "ȷ��"*/);
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
 * ���� btnQuery ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnQuery() {
 if (ivjbtnQuery == null) {
  try {
   ivjbtnQuery = new nc.ui.pub.beans.UIButton();
   ivjbtnQuery.setName("btnQuery");
   ivjbtnQuery.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*@res "��ѯ"*/);
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
 * ���� btnQuery ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnTold() {
 if (ivjbtnTold == null) {
  try {
	  ivjbtnTold = new nc.ui.pub.beans.UIButton();
	  ivjbtnTold.setName("btnTold");
	  ivjbtnTold.setText("�ϼ�");
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
 * ���� ivjbtnSelAll ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnSelAll() {
 if (ivjbtnSelAll == null) {
  try {
  	ivjbtnSelAll = new nc.ui.pub.beans.UIButton();
  	ivjbtnSelAll.setName("btnSelAll");
  	ivjbtnSelAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*@res "ȫѡ"*/);
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
 * ���� ivjbtnUnSelAll ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getbtnUnSelAll() {
 if (ivjbtnUnSelAll == null) {
  try {
  	ivjbtnUnSelAll = new nc.ui.pub.beans.UIButton();
  	ivjbtnUnSelAll.setName("btnUnSelAll");
  	ivjbtnUnSelAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*@res "ȫ��"*/);
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
 * ���� ivjComBoxSplitMode ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
///* ���棺�˷������������ɡ� */
//private UIComboBox getComBoxSplitMode() {
// if (ivjComBoxSplitMode == null) {
//  try {
//  	ivjComBoxSplitMode = new UIComboBox();
//  	ivjComBoxSplitMode.setName("ComBoxSplitMode");
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000275")/*@res "��Ӧ��"*/);
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000042")/*@res "��Ӧ��+���"*/);
//  	ivjComBoxSplitMode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000043")/*@res "���Ļ��ܼ�¼"*/);
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
 * ���� ivjComBoxSplitMode ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private UIButton getbtnSplitMode() {
 if (ivjbtnSplitMode == null) {
  try {
  	ivjbtnSplitMode = new UIButton();
  	ivjbtnSplitMode.setName("lbSplitMode");
  	ivjbtnSplitMode.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000181"));///*@res ""�ֵ���ʽ""*/);
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
 * ���� PanlCmd ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
   getPanlCmd().add(getbtnTold(),getbtnTold().getName());//�ϼ�
  
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
 * ���� UIDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
protected JPanel getUIDialogContentPane() {
 if (ivjUIDialogContentPane == null) {
  try {
   ivjUIDialogContentPane = new UIPanel();
   ivjUIDialogContentPane.setName("UIDialogContentPane");
   ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
   //2003-05-12ƽ̨������ʾ����
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
	
	//��ͷ���б����л��¼�������
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
	 getbtnTold().addActionListener(this);//�ϼ�
	 
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
 * �˴����뷽��˵����
 * ��������:
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:
 * @return nc.ui.pub.bill.BillListPanel
 */
public nc.ui.pub.bill.BillListPanel getbillListPanel() {
	
	
	if (ivjbillListPanel == null) {
		super.getbillListPanel();
		//��ȡ��ز���
		initSysParam();
		//���þ���
		setBillListDigit();
		//���÷ֵ���ʽ
		//getComBoxSplitMode().setSelectedIndex(getSplitBillMode().intValue());
		
		ivjbillListPanel.getHeadTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	}
	return ivjbillListPanel;
	
}
/**
 * �����ߣ����˾�
 * ���ܣ����ر����ѯ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public String getBodyCondition() {
	return null;
}

/**
 * �����ߣ����˾�
 * ���ܣ����ر�ͷ��ѯ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public String getHeadCondition() {

	String sHeadCondition = null;
	//���Ϲ�˾����3 ǩ�ֱ�־�������� 6 ����� 5 �رյĵ�������
	if (getPkCorp() != null && getPkCorp().trim().length() > 0)
		sHeadCondition =
			" pk_corp = '"
				+ getPkCorp().trim()
        //modified by liuzy 2008-02-29 �Ҽҹ���NC
        //��Ӧ�̼Ĵ������,���Ļ�����Ϊ����ʱ,�����ݹ�,���޷��������ɺ��ַ�Ʊ
//                + "' and dr = 0 and coalesce(binvoiceendflag,'N') = 'N' ";//and coalesce(noutnum,0)>0 ";
				  + "' and dr = 0 and coalesce(binvoiceendflag,'N') = 'N' and coalesce(noutnum,0)<>0";//edit by zwx 2015-3-18 ��ѯ���ȥ��0����
	else
//		sHeadCondition = " dr = 0 and coalesce(binvoiceendflag,'N') = 'N' ";//and coalesce(noutnum,0)>0 ";
	 	sHeadCondition = " dr = 0 and coalesce(binvoiceendflag,'N') = 'N' and coalesce(noutnum,0)<>0";//edit by zwx 2015-3-18
	 	
	return sHeadCondition;
}

/**
 * �����ߣ����˾�
 * ���ܣ���ϵͳ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ�2002-10-17
 * �޸��ˣ������
 * �޸�ԭ��
 */
protected void initSysParam() {
	try {

		m_iaScale = new int[] { 8, 8, 8, 8, 8 };
		//��������	����				ȱʡֵ
		//BD501	����С��λ			    2
		//BD502	����������С��λ		2
		//BD503	������	С��λ			2
		//BD505	����С��λ	            2
		//BD301	����С��λ	            2
		
		
		String para[] = { "BD501", "BD502", "BD503", "BD505", "BD301"};
		Hashtable h = null;
		try {
			h = nc.ui.pub.para.SysInitBO_Client.queryBatchParaValues(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),para);
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}

		Object otemp = null;

		otemp = h.get("BD501");
		//����
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.NUM] = Integer.parseInt(otemp.toString().trim());
		
		//������
		otemp = h.get("BD502");
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.ASSIST_NUM] = Integer.parseInt(otemp.toString().trim());

		otemp = h.get("BD503");
		//������
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.CONVERT_RATE] = Integer.parseInt(otemp.toString().trim());
		
		
		otemp = h.get("BD505");
		//����
		if (otemp != null && otemp.toString().trim().length()>0 )
			m_iaScale[nc.vo.ic.pub.bill.DoubleScale.PRICE] = Integer.parseInt(otemp.toString().trim());
		
		ICBcurrArithUI curr = new ICBcurrArithUI(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		m_iaScale[nc.vo.ic.pub.bill.DoubleScale.MNY] = curr.getBusiCurrDigit(curr.getLocalCurrPK());

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
		//if (e instanceof nc.vo.pub.BusinessException)
			//MessageDialog.showErrorDlg(this, "����", e.getMessage());
	}
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-7-6 19:38:38)
 */
public void onOk() {
	//super.onOk();
	if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
//  modified by liuzy 2008-02-29 �Ҽҹ���NC
    //��Ӧ�̼Ĵ������,���Ļ�����Ϊ����ʱ,�����ݹ�,���޷��������ɺ��ַ�Ʊ
//		try{
//			onCheckData();
//		}catch(ValidationException e){
//			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*����*/,e.getMessage());
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
 * ��ѯ
 * �������ڣ�(2001-7-6 19:38:51)
 */
public void onQuery() {
	
	QueryConditionClient queryCondition = getQueyDlg();
	queryCondition.showModal();
	if (queryCondition.isCloseOK()) {
		
		getbillListPanel().getHeadBillModel().clearBodyData();
		
		//���ز�ѯ����
		//m_whereStr = voConditions[0].getWhereSQL()//queryCondition.getWhereSQL();
		loadHeadData();
		
	}
	//super.onQuery();
	retBillVo = null;
	retBillVos = null;
}

/*
 * add���Ϻ�С�ƣ�ѡ����Ϻźϼƹ���
 * ������
 * 2013-03-11
 * start
 * */
private int m_RowCount;

public void onTold(){
	int m_nSelectedRowCoun=getbillListPanel().getHeadTable().getSelectedRowCount();
	if (m_nSelectedRowCoun==0) {
	      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"), "��ѡ��Ҫ�ϼƵ��У�");
	      return;
	    }
	String vinvname =null;
	UFDouble noutnum = new UFDouble(0);//����
//	BillItem[] ck=getbillListPanel().getHeadBillModel().getBodyItems();
	int[] selrows = getbillListPanel().getHeadTable().getSelectedRows();
	Map<String,UFDouble> told=new HashMap();
	UFDouble temp;
	UFDouble temp1= new UFDouble(0);
	UFDouble temp2= new UFDouble(0);
	UFDouble noutinnum = new UFDouble(0);//�˿���
	UFDouble ninvoicenum = new UFDouble(0);//�ۼƿ�Ʊ��
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
		xiaoji=xiaoji+vinvname+"   ����:"+temp+"\n";
		heji=heji.add(temp);
	}
	//����ϼ�
	String hj="����ϼ�:"+heji.toString()+"\n";
	//�˿���
	String tks ="�˿�����"+temp1.toString()+"\n";
	//ʵ��������
	String sj="ʵ����������"+temp2.toString();
	MessageDialog.showHintDlg(this, "�ϼ�", xiaoji+hj+tks+sj);
}

/*
 * add���Ϻ�С�ƣ�ѡ����Ϻźϼƹ���
 * ������
 * 2013-03-11
 * end
 * */

/**
 * �ԡ�ȡ����ģʽ�رնԻ��� ҵ��ڵ������Ҫ�޸�
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
 * ȫѡ
 * �������ڣ�(2001-7-6 19:38:51)
 */
public void onSelAll() {
	
	
	BillModel headModel = getbillListPanel().getHeadBillModel();
	
	for(int i=0,loop=headModel.getRowCount();i<loop;i++){
		//ѡȡ����
		headModel.setRowState(i, BillModel.SELECTED);
		
	}
	getbillListPanel().getHeadTable().selectAll();
}

/**
 * ȫ��
 * �������ڣ�(2001-7-6 19:38:51)
 */
public void onUnSelAll() {
	BillModel headModel = getbillListPanel().getHeadBillModel();
	for(int i=0,loop=headModel.getRowCount();i<loop;i++){
		//ѡȡ����
		headModel.setRowState(i, BillModel.UNSTATE);
		
	}
	getbillListPanel().getHeadTable().clearSelection();
}

/**
 * ������ʾ����
 * �������ڣ�(2001-7-6 19:38:51)
 */
public void setBillListDigit() {
	String[] numitemname = {
//		//����
//		"noutnum",
//		//�ѿ�Ʊ����
//		"ntotalinvoicenum",
//		//���ο�Ʊ����
//		"ninvoicenum",
//		//�ۼƽ�������
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
//	�ۼƽ�����
	bim = getbillListPanel().getHeadItem("naccountmny");
	if(bim!=null)
		bim.setDecimalDigits(m_iaScale[nc.vo.ic.pub.bill.DoubleScale.MNY]);
	
}


/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
public void loadBodyData(int row) {

}


/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
public void loadHeadData() {
	try {
		//���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
//		��͸��ѯ
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
		nc.vo.scm.pub.SCMEnv.out("���ݼ���ʧ�ܣ�");
		nc.vo.scm.pub.SCMEnv.error(e);
	}
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
public AggregatedValueObject getRetVo() {
	AggregatedValueObject sumvo= null;
	AggregatedValueObject[] sumvos = getSelectVos();
	if(sumvos!=null && sumvos.length>0)
		sumvo = sumvos[0];

	return sumvo;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
public AggregatedValueObject[] getRetVos() {

	return getSelectVos();
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
public AggregatedValueObject[] getSelectVos() {
	//�ֵ�
	//��Ӧ�� 0 
  	//��Ӧ��+��� 1;
  	//���Ļ��ܼ�¼ 2;
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
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
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
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
 */
private void setNinvoicenum(){
	
	UFDouble noutnum = null,noutinnum=null;
	UFDouble ntatolinvoicenum = null;
	UFDouble d0=new UFDouble(0);
	Object otemp = null;
	
	// add by liuzy 2008-11-12 ����04:54:17 �Ҽҹ���ɿ�Ʊ��������������
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
 * �˴����뷽��˵����
 * �������ڣ�(2001-4-23 9:17:37)
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
		//ʵ������
		noutnum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"noutnum"));
		if(noutnum==null)
			noutinnum = d0;
		//�����˿�
		noutinnum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"noutinnum"));
		if(noutinnum==null)
			noutinnum = d0;
		//�ۼƿ�Ʊ����
		ntotalinvoicenum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"ntotalinvoicenum"));
		if(ntotalinvoicenum==null)
			ntotalinvoicenum = d0;
		//��Ʊ����
		ninvoicenum = nc.vo.ic.pub.bill.SwitchObject.switchObjToUFDouble(getbillListPanel().getHeadBillModel().getValueAt(selrows[i],"ninvoicenum"));
		if(ninvoicenum==null)
			ninvoicenum = d0;
		
		// commet by zip: 2014/1/4 No 109(��ʱ��)
		if(noutnum.abs().compareTo(noutinnum.abs().add(ntotalinvoicenum.abs()).add(ninvoicenum.abs()))<0){
			throw new ValidationException(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40083802","UPT40083802-000049",
							null,new String[]{(selrows[i]+1)+""})/*@res ��{0}���ο�Ʊ��������VMI���ܿɿ�Ʊ������*/);
		}
		// end
		
	}
	
}


/**
 * ���˫���¼���
 * �������ڣ�(2001-5-9 8:52:00)
 * @param row int
 */
public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
	if (e.getPos() == BillItem.HEAD) {
		//ֻ�Ա�ͷ��˫���¼�������Ӧ,�����˫���¼���BillListPanel.BodyMouseListener����Ӧ
		int row = e.getRow();
		
		BillModel headModel = getbillListPanel().getHeadBillModel();
		//ѡȡ����
		if (headModel.getRowState(row) == BillModel.SELECTED) {
			headModel.setRowState(row, BillModel.UNSTATE);
			
		} else {
			headModel.setRowState(row, BillModel.SELECTED);
			
		}
	}
}

/**
 * get��
 * �������ڣ�(2001-5-9 8:52:00)
 * @param row int
 */

private VMISplitModeDlg getVMISplitModeDlg(){
	if(ivjVMISplitModeDlg==null){
		ivjVMISplitModeDlg = new VMISplitModeDlg(this);
	}
	return ivjVMISplitModeDlg;
}


}
