package nc.ui.ps.estimate;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.ListSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pps.PricStlHelper;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.vo.pps.PricParaVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.saleEstimateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.scm.service.ServcallVO;

 
/**
 * 功能描述:暂估UI   销售运费暂估
 * 作者:王凯飞
 * 创建日期:2014-10-10
 *
 */
public class saleEstimateYFUI extends nc.ui.pub.ToftPanel 
	implements BillEditListener,javax.swing.event.ListSelectionListener,
				BillEditListener2,IDataSource{
	//界面控制按钮
	private ButtonObject m_buttons[]=null;

	//按钮状态：0 正常；1 置灰；2 不可视
	private int m_nButtonState[]=null;

	//单据
	private BillCardPanel m_billPanel=null;

	//查询模板新增查询条件
	private UIRadioButton m_rbEst=null;
	private UIRadioButton m_rbUnEst=null;

	//单位编码，系统应提供方法获取
	private String m_sUnitCode=getCorpPrimaryKey();

	//查询条件
	private PoQueryCondition m_condClient=null;

	//缓存
	private saleEstimateVO m_estimate1VOs[]=null;
	private saleEstimateVO m_estimate2VOs[]=null;
	private String m_sZG="N";

	//暂估方式
	private String m_sEstimateMode=null;
	//差异转入方式
	private String m_sDiffMode = null;
	//暂估单价来源
	private String m_sEstPriceSource = null;

	//库存是否启用
	private boolean m_bICStartUp=false;

	//系统初始化参数 "BD501","BD505","BD301"
	private int m_measure[] = null;

	//是否暂估应付
	private UFBoolean m_bZGYF = new UFBoolean(false);
	
	//本币币种
	private String m_sCurrTypeID = null;
/**
 * Estimate 构造子注解。
 */
public saleEstimateYFUI() {
	super();
	init();
}
/**
* 功能描述：编辑后事件处理
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2014-10-10
*/
public void afterEdit(BillEditEvent event) {
	computeBodyData(event);
	if(event.getKey().equals("nprice")){
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		UFDouble nprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "nprice").toString());
		String sl = getBillCardPanel().getBodyValueAt(row, "ntaxrate").toString();
		if(sl == "" || sl == null){
			return;
		}
		UFDouble ntaxrate = new UFDouble(getBillCardPanel().getBodyValueAt(row, "ntaxrate").toString());//税率
		UFDouble ntaxprice = nprice.add(nprice.multiply(ntaxrate).div(new UFDouble(100)));
		getBillCardPanel().setBodyValueAt(nprice, row, "nmoney");//金额
		getBillCardPanel().setBodyValueAt(nprice, row, "ntaxmoney");//金额
		getBillCardPanel().setBodyValueAt(ntaxprice, row, "ntaxprice");//含税单价
		getBillCardPanel().setBodyValueAt(ntaxprice, row, "ntotalmoney");//价税合计
	}
}
/**
* 功能描述：行变换事件处理
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public void bodyRowChange(BillEditEvent event) {
}
/**
* 功能描述：改变界面按钮状态
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
private void changeButtonState() {
	for (int i = 0; i < m_nButtonState.length; i++) {
		if (m_nButtonState[i] == 0) {
			m_buttons[i].setVisible(true);
			m_buttons[i].setEnabled(true);
		} else if (m_nButtonState[i] == 1) {
			m_buttons[i].setVisible(true);
			m_buttons[i].setEnabled(false);
		} else if (m_nButtonState[i] == 2) {
			m_buttons[i].setVisible(false);
		}
		this.updateButton(m_buttons[i]);
	}
}
/**
* 功能描述：查询模板增加查询条件
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
private void changeQueryModelLayout() {
	if (m_rbEst != null && m_rbUnEst != null)
		return;

	UILabel label1 = new UILabel("选择入库单：");
	label1.setBounds(30, 65, 100, 25);

	m_rbEst = new UIRadioButton();
	m_rbEst.setBounds(130, 65, 16, 16);
	m_rbEst.setSelected(true);
	UILabel label2 = new UILabel("未暂估");
	label2.setBounds(146, 65, 60, 25);

	m_rbUnEst = new UIRadioButton();
	m_rbUnEst.setBounds(130, 95, 16, 16);
	UILabel label3 = new UILabel("已暂估");
	label3.setBounds(146, 95, 60, 25);

	javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	buttonGroup.add(m_rbEst);
	buttonGroup.add(m_rbUnEst);

	m_condClient.getUIPanelNormal().add(label1);
	m_condClient.getUIPanelNormal().add(label2);
	m_condClient.getUIPanelNormal().add(label3);
	m_condClient.getUIPanelNormal().add(m_rbEst);
	m_condClient.getUIPanelNormal().add(m_rbUnEst);
}
/**
* 功能描述：修改单价,金额
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
private void computeBodyData(BillEditEvent event) {

	int iRowPos = event.getRow();
	if (iRowPos < 0)
		return;
	Object oMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nmoney");
	UFDouble ufdMoney =
		(oMoney == null || oMoney.toString().trim().length() == 0) ? new UFDouble(0.0) : new UFDouble(oMoney.toString().trim());
		
	int[] descriptions = new int[] {
			RelationsCal.DISCOUNT_TAX_TYPE_NAME,	//扣税类别名(应税内含，应税外加，不计税)
			RelationsCal.DISCOUNT_TAX_TYPE_KEY,//扣税类别
			RelationsCal.NUM,//主数量
			RelationsCal.NET_PRICE_ORIGINAL,//净单价	
			RelationsCal.MONEY_ORIGINAL,//金额
			RelationsCal.NET_TAXPRICE_ORIGINAL,//净含税单价
			RelationsCal.SUMMNY_ORIGINAL, //价税合计		--原币
			RelationsCal.TAXRATE,	//税率
			RelationsCal.DISCOUNT_RATE,//扣率
			RelationsCal.PRICE_ORIGINAL,//单价
			RelationsCal.TAXPRICE_ORIGINAL,//含税单价
			RelationsCal.TAX_ORIGINAL      //税额
	};

	String s = "应税内含";
	if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
	if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s = "不计税";

	String[] keys = new String[] {
			s,
			"idiscounttaxtype",
			"ninnum",
			"nprice",
			"nmoney",
			"ntaxprice",
			"ntotalmoney", 
			"ntaxrate",
			"ndiscountrate",
			"nnetprice",
			"nnettaxprice",
			"ntaxmoney"
	};
	
	if (event.getKey() != null && "nmoney".equalsIgnoreCase(event.getKey().trim())) {
		//修改金额,单价自动变化
		if (m_estimate1VOs[iRowPos].getNoutnum() != null
			&& ufdMoney.multiply(m_estimate1VOs[iRowPos].getNoutnum()).doubleValue() < 0.0) {
			MessageDialog.showErrorDlg(this, "修改金额", "修改金额不能改变符号！");
			getBillCardPanel().setBodyValueAt(event.getOldValue(), iRowPos, "nmoney");
			return;
		}
	}
	
	RelationsCal.calculate(event, getBillCardPanel(), new int[] { PuTool.getPricePriorPolicy(m_sUnitCode)}, descriptions, keys, saleEstimateVO.class.getName());
		
	Object oPrice = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nprice");
	oMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "nmoney");
	Object oTaxPrice = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntaxprice");
	Object oTaxRate = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntaxrate");
	Object oTotalMoney = getBillCardPanel().getBillModel().getValueAt(iRowPos, "ntotalmoney");
	
	if(oPrice == null || oPrice.toString().trim().length() == 0) m_estimate1VOs[iRowPos].setNprice(new UFDouble(0));
	else  m_estimate1VOs[iRowPos].setNprice(new UFDouble(oPrice.toString()));
	
	if(oMoney == null || oMoney.toString().trim().length() == 0) m_estimate1VOs[iRowPos].setNmoney(new UFDouble(0));
	else  m_estimate1VOs[iRowPos].setNmoney(new UFDouble(oMoney.toString()));

	if(oTaxPrice == null || oTaxPrice.toString().trim().length() == 0) m_estimate1VOs[iRowPos].setNtaxprice(new UFDouble(0));
	else  m_estimate1VOs[iRowPos].setNtaxprice(new UFDouble(oTaxPrice.toString()));

	if(oTaxRate == null || oTaxRate.toString().trim().length() == 0) m_estimate1VOs[iRowPos].setNtaxrate(new UFDouble(0));
	else  m_estimate1VOs[iRowPos].setNtaxrate(new UFDouble(oTaxRate.toString()));

	if(oTotalMoney == null || oTotalMoney.toString().trim().length() == 0) m_estimate1VOs[iRowPos].setNtotalmoney(new UFDouble(0));
	else  m_estimate1VOs[iRowPos].setNtotalmoney(new UFDouble(oTotalMoney.toString()));
	
	return;
} 

/**
* 功能描述：获得要打印的字段主键
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public String[] getAllDataItemExpress() {
	BillItem bodyItems[] = getBillCardPanel().getBodyShowItems();

	Vector v = new Vector();
	for (int i = 0; i < bodyItems.length; i++)
		v.addElement(bodyItems[i].getKey());

	if (v.size() > 0) {
		String sKey[] = new String[v.size()];
		v.copyInto(sKey);
		return sKey;
	}
	return null;
}
/**
* 功能描述：获得要打印的字段名称
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public String[] getAllDataItemNames() {
	BillItem bodyItems[] = getBillCardPanel().getBodyShowItems();

	Vector v = new Vector();
	for (int i = 0; i < bodyItems.length; i++)
		v.addElement(bodyItems[i].getName());

	if (v.size() > 0) {
		String sName[] = new String[v.size()];
		v.copyInto(sName);
		return sName;
	}
	return null;
}
/**
* 功能描述：获得单据卡片模板控件
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
private BillCardPanel getBillCardPanel() {
	if (m_billPanel == null) {
		try {
			m_billPanel = new BillCardPanel();
			// user code begin {1}
			// 加载模板
			BillData bd = new BillData(m_billPanel.getTempletData("0001AA10000000077030"));

			//m_billPanel.loadTemplet("40141001010000000000");

			bd = initDecimal(bd);

			m_billPanel.setBillData(bd);
			m_billPanel.setShowThMark(true);
			m_billPanel.setTatolRowShow(true);

			// 增加单据编辑监听
			m_billPanel.addEditListener(this);
			m_billPanel.addBodyEditListener2(this);
			m_billPanel.setBodyMenuShow(false);

			//表体选择监听
			m_billPanel.getBillTable().setCellSelectionEnabled(false);
			m_billPanel.getBillTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			m_billPanel.getBillTable().getSelectionModel().addListSelectionListener(this);

			// user code end
		} catch (java.lang.Throwable exception) {
			SCMEnv.out(exception);
			MessageDialog.showErrorDlg(this, "加载模板", "模板不存在!");
			return null;

		}
	}
	return m_billPanel;
}
/**
* 功能描述：
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public String[] getDependentItemExpressByExpress(String itemName) {
	return null; 
}
/**
* 功能描述：获得要打印的字段的值
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public String[] getItemValuesByExpress(String itemKey) {
	itemKey = itemKey.trim();

	if (m_sZG.equals("N")) {
		if (m_estimate1VOs != null && m_estimate1VOs.length > 0) {
			String sValues[] = new String[m_estimate1VOs.length];
			for (int i = 0; i < m_estimate1VOs.length; i++) {
				Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
				if (o != null)
					sValues[i] = o.toString();
				else
					sValues[i] = "";
			}
			return sValues;
		}

	} else {
		if (m_estimate2VOs != null && m_estimate2VOs.length > 0) {
			String sValues[] = new String[m_estimate2VOs.length];
			for (int i = 0; i < m_estimate2VOs.length; i++) {
				Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
				if (o != null)
					sValues[i] = o.toString();
				else
					sValues[i] = "";
			}
			return sValues;
		}
	}

	return null;
}
/**
* 功能描述：获得打印模板名称
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public String getModuleName() {
	return "销售费暂估DAY";
}
/**
 * 子类实现该方法，返回业务界面的标题。
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return "销售费暂估";
}
/**
* 功能描述：初始化
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public void init() {
	initpara();
	//显示按钮
	m_buttons = new ButtonObject[8];
	m_buttons[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000041")/*@res "全选"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000041")/*@res "全选"*/,2,"全选");
	m_buttons[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000042")/*@res "全消"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000042")/*@res "全消"*/,2,"全消");
	m_buttons[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000003")/*@res "暂估"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000003")/*@res "暂估"*/,2,"暂估");
	m_buttons[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000004")/*@res "反暂"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000004")/*@res "反暂"*/,2,"反暂");
	m_buttons[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/,2,"查询");
	m_buttons[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000005")/*@res "打印预览"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000005")/*@res "打印预览"*/,2,"打印预览");
	m_buttons[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/,2,"打印");
	m_buttons[7] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000007")/*@res "优质优价计算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301","UPT4004050301-000007")/*@res "优质优价计算"*/,2,"优质优价计算");
	this.setButtons(m_buttons);

	//显示单据
	setLayout(new java.awt.BorderLayout());
	add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
	getBillCardPanel().setEnabled(false);

	//扣税类别	0应税内含 1应税外加 2不计税	
	UIComboBox comItem = (UIComboBox)getBillCardPanel().getBodyItem("idiscounttaxtype").getComponent();
	getBillCardPanel().getBodyItem("idiscounttaxtype").setWithIndex(true);
	comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000105")/*@res "应税内含"*/);
	comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000106")/*@res "应税外加"*/);
	comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000107")/*@res "不计税"*/);
	comItem.setTranslate(true);

	//初始按钮状态
	m_nButtonState = new int[m_buttons.length];

	//设置除查询外的所有按钮为灰
	for (int i = 0; i < 5; i++) {
		m_nButtonState[i] = 1;
	}
	m_nButtonState[4] = 0;
	m_nButtonState[5] = 0;
	m_nButtonState[6] = 0;
	m_nButtonState[7] = 0;
	changeButtonState();

}
/**
* 功能描述:初始化小数位
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
private BillData initDecimal(BillData bd) {
	//获得系统初始化参数

	if (m_measure == null || m_measure.length == 0) {
		MessageDialog.showErrorDlg(this, "获得系统初始化参数", "无法获得系统初始化参数！");
		return null;
	}

	//获得系统初始化参数
	//获得主计量小数位
	int nMeasDecimal = m_measure[0];
	//获得采购单价小数位
	int nPriceDecimal = m_measure[1];
	//获得采购金额小数位
	int nMoneyDecimal = m_measure[2];

	//获得单据元素对应的控件,并修改控件的属性
	BillItem item1 = bd.getBodyItem("nprice");
	item1.setDecimalDigits(nPriceDecimal);
	
	item1 = bd.getBodyItem("ntaxprice");
	item1.setDecimalDigits(nPriceDecimal);


	BillItem item2 = bd.getBodyItem("noutnum");
	item2.setDecimalDigits(nMeasDecimal);

	BillItem item3 = bd.getBodyItem("nsettlenum");
	item3.setDecimalDigits(nMeasDecimal);

	BillItem item4 = bd.getBodyItem("nmoney");
	item4.setDecimalDigits(nMoneyDecimal);

	BillItem item5 = bd.getBodyItem("nsettlemny");
	item5.setDecimalDigits(nMoneyDecimal);

	item5 = bd.getBodyItem("ntotalmoney");
	item5.setDecimalDigits(nMoneyDecimal);

	item5 = bd.getBodyItem("ntaxmoney");
	item5.setDecimalDigits(nMoneyDecimal);

	return bd;
}
/**
 * 为了减少初始化时前后台交互的次数，一次性获取多个系统参数
 * 作者:袁野
 * 日期：2004-09-09
 *
 */
public void initpara() {
	try {

		Object[] objs = null;
		ServcallVO[] scds = new ServcallVO[2];

		//获得系统初始化参数
		scds[0] = new ServcallVO();
		scds[0].setBeanName("nc.itf.pu.pub.IPub");
		scds[0].setMethodName("getDigitBatch");
		scds[0].setParameter(new Object[] { m_sUnitCode, new String[] { "BD501", "BD505", "BD301" }});
		scds[0].setParameterTypes(new Class[] { String.class, String[].class });

		//库存是否启用
		scds[1] = new ServcallVO();
		scds[1].setBeanName("nc.itf.pu.pub.IPub");
		scds[1].setMethodName("isEnabled");
		scds[1].setParameter(new Object[] { m_sUnitCode, "IC" });
		scds[1].setParameterTypes(new Class[] { String.class, String.class });

		//获得系统设置的暂估方式和差异转入方式		
		Hashtable hTemp = SysInitBO_Client.queryBatchParaValues(m_sUnitCode, new String[]{"PO12","PO13","PO27","PO52","BD301"});
		if (hTemp == null || hTemp.size() == 0){
			SCMEnv.out("未获取初始化参数PO12,PO13,PO27,PO52,BD301 请检查...");
			return;
		}
		if(hTemp.get("PO12") == null){
			SCMEnv.out("未获取初始化参数PO12, 请检查...");
			return;			
		}else{
			Object temp =  hTemp.get("PO12");
			m_sEstimateMode = temp.toString();
		}

		if(hTemp.get("PO13") == null){
			SCMEnv.out("未获取初始化参数PO13, 请检查...");
			return;			
		}else{
			Object temp =  hTemp.get("PO12");
			m_sDiffMode = temp.toString();
		}

		if(hTemp.get("PO27") == null){
			SCMEnv.out("未获取初始化参数PO27, 请检查...");
			return;			
		}else{
			Object temp = hTemp.get("PO27");
			m_sEstPriceSource = temp.toString();
		}
		
		if(hTemp.get("PO52") == null){
			SCMEnv.out("未获取初始化参数PO52, 请检查...");
			return;			
		}else{
			Object temp = hTemp.get("PO52");			
			m_bZGYF = new UFBoolean(temp.toString());
		}
		
		if(hTemp.get("BD301") == null){
			SCMEnv.out("未获取初始化参数BD301, 请检查...");
			return;			
		}else{
			Object temp = hTemp.get("BD301");			
			m_sCurrTypeID = temp.toString();
		}
		objs = nc.ui.scm.service.LocalCallService.callService(scds);

		m_measure = (int[]) objs[0];//获得系统初始化参数
		m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();//库存是否启用

	} catch (Exception e) {
		SCMEnv.out(e);
		MessageDialog.showErrorDlg(this, "获取系统初始化参数出错", e.getMessage());
		return;
	}
}
/**
 * 查询模版初始化
 * 作者:袁野
 * 日期：2004-09-10
 *
 */
public void initQueryModel() {
	if (m_condClient == null) {
		//初始化查询模板    
    m_condClient = new PoQueryCondition(this,"暂估出库单查询");
    m_condClient.setTempletID(m_sUnitCode, "40141001", getClientEnvironment().getUser().getPrimaryKey(), null, "40141001");    

		changeQueryModelLayout();
		m_condClient.setValueRef("dbilldate", "日历");
		m_condClient.setValueRef("cbillmaker", "操作员");
		//部门名称
		UIRefPane deptRef = new UIRefPane();
		deptRef.setRefNodeName("部门档案");
		m_condClient.setValueRef("cdeptid", deptRef);
		//人员名称
		UIRefPane psnRef = new UIRefPane();
		psnRef.setRefNodeName("人员档案");
		m_condClient.setValueRef("coperator", psnRef);

		UIRefPane vendorRef = new UIRefPane();
		vendorRef.setRefNodeName("供应商档案");
		vendorRef.getRefModel().addWherePart(" and frozenflag = 'N' ");
		m_condClient.setValueRef("cvendorbaseid", vendorRef);

		UIRefPane biztypeRef = new UIRefPane();
		biztypeRef.setRefNodeName("业务类型");
		m_condClient.setValueRef("cbiztype", biztypeRef);

		m_condClient.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

		//加载自定义项名称
		nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_condClient, m_sUnitCode, "icbill", "vuserdef", "vuserdef");
		m_condClient.setIsWarningWithNoInput(true);
		/*封存的基础数据能被参照*/
		m_condClient.setSealedDataShow(true);

    //数据权限控制
    m_condClient.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
        new String[]{nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey()}, 
        new String[]{"供应商档案","部门档案","人员档案","存货档案","存货分类","仓库档案","项目管理档案"},
        new String[]{"cvendorbaseid","cdeptid","coperator","invcode","cinvclassid","cwarehouseid","cprojectid"},
        new int[]{0,0,0,0,0,0,0});
	}
}
/**
* 功能描述:获得要打印的字段是否为数字
* 参数：
* 返回：
* 作者：王凯飞
* 创建：
*/
public boolean isNumber(String itemKey) {
	itemKey = itemKey.trim();
	itemKey = itemKey.substring(0, 1);
	if (itemKey.equals("n"))
		return true;
	else
		return false;
}
/**
 * 子类实现该方法，响应按钮事件。
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	if(bo==m_buttons[0]){
		onSelectAll();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000033")/*@res "全选成功"*/);		
	}

	if(bo==m_buttons[1]){
		onSelectNo();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000034")/*@res "全消成功"*/);		
	}

	if(bo==m_buttons[2]) onEstimate();

	if(bo==m_buttons[3]) onUnEstimate();

	if(bo==m_buttons[4]){
		onQuery();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH009")/*@res "查询完成"*/);		
	}

	if(bo==m_buttons[5]){
		onPreview();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000021")/*@res "打印预览完成"*/);		
	}

	if(bo==m_buttons[6]){
		onPrint();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH041")/*@res "打印成功"*/);		
	}
	
	if(bo==m_buttons[7]) onHQHP();
}
/**
* 功能描述:暂估
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2001-6-22 14:41:50
* 修改：晁志平　FOR　V30
*/
public void onEstimate() {

	nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
	timer.start();

	Integer nSelected[] = null;
	Vector v = new Vector();
	Vector vv = new Vector();
	int nRow = getBillCardPanel().getRowCount();
	for (int i = 0; i < nRow; i++) {
		int nStatus = getBillCardPanel().getBillModel().getRowState(i);
		if (nStatus == BillModel.SELECTED)
			v.addElement(new Integer(i));
		else
			vv.addElement(new Integer(i));
	}
	nSelected = new Integer[v.size()];
	v.copyInto(nSelected);

	if (nSelected == null || nSelected.length == 0) {
		MessageDialog.showErrorDlg(this, "销售运费暂估", "未选中出库库单！");
		return;
	}

	Vector vTemp = new Vector();
	StringBuffer sMessage = new StringBuffer();
	for (int i = 0; i < nSelected.length; i++) {
		saleEstimateVO vo = m_estimate1VOs[nSelected[i].intValue()];
		vTemp.addElement(vo);

		UFDouble nOutNum = vo.getNoutnum();
		if (nOutNum == null || nOutNum.doubleValue() == 0.0)
			sMessage.append(vo.getVbillcode() + "号出库库单的出库数量为零，不能暂估！");
	}

	//如果出库单的出库数量为空或为零，出库单不能进行暂估处理
	if (sMessage.length() > 0) {
		MessageDialog.showErrorDlg(this, "暂估处理", sMessage.toString());
		return;
	}

	saleEstimateVO[] VOs = new saleEstimateVO[vTemp.size()];
	vTemp.copyInto(VOs);

	try {
		for (int i = 0; i < VOs.length; i++)
			VOs[i].validate();

		timer.addExecutePhase("数据库更新$$$$前$$$处理时间#######");

		java.util.ArrayList paraList = new java.util.ArrayList();
		paraList.add(getClientEnvironment().getUser().getPrimaryKey());
		paraList.add(getClientEnvironment().getDate());
		paraList.add(m_sEstimateMode);
		paraList.add(m_sDiffMode);
//		paraList.add(m_bZGYF);
		paraList.add(new UFBoolean(true));
		paraList.add(m_sCurrTypeID);
		
		
		ClientLink cl=new ClientLink(ClientEnvironment.getInstance());
		
		EstimateHelper.estimate_saleyf(VOs,paraList,cl);

		timer.addExecutePhase("数据库更新$$$$操作$$$处理时间#######");

	} catch (java.sql.SQLException e) {
		MessageDialog.showErrorDlg(this, "出库单暂估", "SQL语句错误！");
		SCMEnv.out(e);
		return;
	} catch (ArrayIndexOutOfBoundsException e) {
		MessageDialog.showErrorDlg(this, "出库单暂估", "数组越界错误！");
		SCMEnv.out(e);
		return;
	} catch (NullPointerException e) {
		MessageDialog.showErrorDlg(this, "出库单暂估", "空指针错误！");
		SCMEnv.out(e);
		return;
	} catch (nc.vo.pub.BusinessException e) {
		MessageDialog.showErrorDlg(this, "出库单暂估", e.getMessage());
		SCMEnv.out(e);
		return;
	} catch (Exception e) {
		MessageDialog.showErrorDlg(this, "出库单暂估", e.getMessage());
		SCMEnv.out(e);
		return;
	}
	this.showHintMessage("出库单暂估成功！");

	//根据暂估方式，向存货传送数据
	//if(m_sEstimateMode.equals("单到补差")){}
	//else if(m_sEstimateMode.equals("单到回冲")){}

	//暂估后的入库单不再显示在界面
	if (vv == null || vv.size() == 0) {
		//所有入库单已暂估完毕
		getBillCardPanel().getBillData().clearViewData();
		getBillCardPanel().updateUI();
		//除查询外所有为灰
		for (int i = 0; i < 5; i++)
			m_nButtonState[i] = 1;
		m_nButtonState[4] = 0;
		changeButtonState();
		return;
	}

	Vector v0 = new Vector();
	for (int i = 0; i < vv.size(); i++) {
		int n = ((Integer) vv.elementAt(i)).intValue();
		v0.addElement(m_estimate1VOs[n]);
	}
	m_estimate1VOs = new saleEstimateVO[v0.size()];
	v0.copyInto(m_estimate1VOs);

	getBillCardPanel().getBillModel().setBodyDataVO(m_estimate1VOs);
	getBillCardPanel().getBillModel().execLoadFormula();
	getBillCardPanel().getBillModel().updateValue();
	getBillCardPanel().updateUI();

	//设置按钮状态：除暂估，反暂，全消外全部为正常
	for (int i = 0; i < 5; i++) {
		m_nButtonState[i] = 0;
	}
	m_nButtonState[1] = 1;
	m_nButtonState[2] = 1;
	m_nButtonState[3] = 1;
	m_nButtonState[7] = 0;
	changeButtonState();
	timer.addExecutePhase("数据库更新$$$$后$$$处理时间#######");
	//
	timer.showAllExecutePhase("暂估处理UI时间分布：");
}


/**
* 功能描述:打印预览
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：晁志平　FOR　V30
*/
private void onPreview() {
	PrintEntry print = new PrintEntry(null, null);

	print.setTemplateID(m_sUnitCode, "40141001", getClientEnvironment().getUser().getPrimaryKey(), null);
	int ret = print.selectTemplate();
	if (ret > 0) {
		print.setDataSource(this);
		print.preview();
	}
}
/**
* 功能描述:打印
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：晁志平　FOR　V30
*/
private void onPrint() {
	PrintEntry print = new PrintEntry(null, null);

	print.setTemplateID(m_sUnitCode, "40141001", getClientEnvironment().getUser().getPrimaryKey(), null);
	int ret = print.selectTemplate();
	if (ret > 0) {
		print.setDataSource(this);
		print.print();
	}
}

/*
 * 优质优价计算
 */
private void onHQHP(){
	
	if(m_sZG.equals("N") && m_estimate1VOs != null && m_estimate1VOs.length > 0){
		int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
		if(nSelected != null && nSelected.length > 0){
			//调用优质优价提供的接口,获取无税单价
			Vector vTemp = new Vector();
			for(int i = 0; i < nSelected.length; i++){
				int j = nSelected[i];
				PricParaVO tempVO = new PricParaVO();
				tempVO.setCgeneralbid(m_estimate1VOs[j].getCgeneralbid());
				vTemp.addElement(tempVO);
			}
			PricParaVO VOs[] = new PricParaVO[vTemp.size()];
			vTemp.copyInto(VOs);
			try{
				VOs = PricStlHelper.queryPricStlPrices(VOs);
			}catch(Exception e){
				SCMEnv.out(e);
			}
			
			//获取无税单价,自动计算其它数据项目
			if(VOs != null && VOs.length > 0){
				for(int i = 0; i < nSelected.length; i++){
					int j = nSelected[i];
					if(VOs[i].getNprice() != null){
						getBillCardPanel().setBodyValueAt(VOs[i].getNprice(),j,"nprice");
						BillEditEvent event = new BillEditEvent(getBillCardPanel().getBillTable(),VOs[i].getNprice(),"nprice",j);
						
						computeBodyData(event);
					}
				}
			}
		}
	}
}
/**
* 功能描述:出库单查询
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：2004-09-10 袁野
*/
public void onQuery() {
	if (!m_bICStartUp) {
		//库存未启用
		MessageDialog.showErrorDlg(this, "出库单查询", "库存未启用，不存在出库单！");
		return;
	}

	initQueryModel();

  m_condClient.hideCorp();
  m_condClient.hideUnitButton();
	m_condClient.showModal();

	if (m_condClient.isCloseOK()) {
		//获取入库单查询条件
		ConditionVO[] conditionVO = m_condClient.getConditionVO();
		
		//设置按钮状态：除暂估，反暂，全消外全部为正常
		for (int i = 0; i < 5; i++) {
			m_nButtonState[i] = 0;
		}
		m_nButtonState[1] = 1;
		m_nButtonState[2] = 1;
		m_nButtonState[3] = 1;

		if (m_rbEst.isSelected()) {
			m_sZG = "N";
		} else {
			m_sZG = "Y";
		}

		//查询
		try {
			long tTime = System.currentTimeMillis();

			if (m_sZG.toUpperCase().equals("N")) {
				m_estimate1VOs = EstimateHelper.queryEstimate_saleyf(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
				
				if (m_estimate1VOs == null || m_estimate1VOs.length == 0) {
					MessageDialog.showHintDlg(this,"出库单查询", "没有符合条件的出库单！");

					//清空数据
					getBillCardPanel().getBillModel().clearBodyData();
					getBillCardPanel().updateUI();
					//全选为空
					m_nButtonState[0] = 1;
					changeButtonState();
					return;
				}
				tTime = System.currentTimeMillis() - tTime;
				SCMEnv.out("入库单查询时间：" + tTime + " ms!");
			
			} else {
				m_estimate2VOs = EstimateHelper.queryEstimate_saleyf(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
				if (m_estimate2VOs == null || m_estimate2VOs.length == 0) {
					MessageDialog.showHintDlg(this, "出库单查询", "没有符合条件的出库单！");

					//清空数据
					getBillCardPanel().getBillModel().clearBodyData();
					getBillCardPanel().updateUI();
					//全选为空
					m_nButtonState[0] = 1;
					changeButtonState();
					return;
				}
				tTime = System.currentTimeMillis() - tTime;
				SCMEnv.out("入库单查询时间：" + tTime + " ms!");
			}
			
			//加载自由项
			nc.ui.scm.pub.FreeVOParse freeParse = new nc.ui.scm.pub.FreeVOParse();
			Vector vTemp = new Vector();			
			if (m_sZG.toUpperCase().equals("N")){
				for(int i = 0; i < m_estimate1VOs.length; i++){
					if(m_estimate1VOs[i].getVfree1() != null || m_estimate1VOs[i].getVfree2() != null
							|| m_estimate1VOs[i].getVfree3() != null || m_estimate1VOs[i].getVfree4() != null
							|| m_estimate1VOs[i].getVfree5() != null){
						vTemp.addElement(m_estimate1VOs[i]);
					}
				}
				if(vTemp.size() > 0){
					saleEstimateVO tempVO[] = new saleEstimateVO[vTemp.size()];
					vTemp.copyInto(tempVO);
					freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "pk_invmandoc", false);
				}
			}else{
				for(int i = 0; i < m_estimate2VOs.length; i++){
					if(m_estimate2VOs[i].getVfree1() != null || m_estimate2VOs[i].getVfree2() != null
							|| m_estimate2VOs[i].getVfree3() != null || m_estimate2VOs[i].getVfree4() != null
							|| m_estimate2VOs[i].getVfree5() != null){
						vTemp.addElement(m_estimate2VOs[i]);
					}
				}
				if(vTemp.size() > 0){
					saleEstimateVO tempVO[] = new saleEstimateVO[vTemp.size()];
					vTemp.copyInto(tempVO);
					freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "pk_invmandoc", false);
				}				
			}

			//设置税率和价税合计, 计算含税单价和价税合计
			//如果入库单非来源于订单,取存货对应的税目税率,扣税类别为应税外加
			vTemp = new Vector();
			if (m_sZG.toUpperCase().equals("N")){
				for(int i = 0; i < m_estimate1VOs.length; i++){
					m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
					if(m_estimate1VOs[i].getCfirsttype() == null){
						if(!vTemp.contains(m_estimate1VOs[i].getCbaseid())) vTemp.addElement(m_estimate1VOs[i].getCbaseid());
					}
				}
			}else{
				for(int i = 0; i < m_estimate2VOs.length; i++){
					if(m_estimate2VOs[i].getCfirsttype() == null){
						if(!vTemp.contains(m_estimate2VOs[i].getCbaseid())) vTemp.addElement(m_estimate2VOs[i].getCbaseid());
					}
				}				
			}
			
			if(vTemp.size() > 0){
				String sBaseID[] = new String[vTemp.size()];
				vTemp.copyInto(sBaseID);
				Object oTemp = CacheTool.getColumnValue("bd_invbasdoc","pk_invbasdoc","pk_taxitems",sBaseID);
				if(oTemp != null){
					Object oResult[] = (Object[]) oTemp;
					if(oResult != null && oResult.length > 0){
						vTemp = new Vector();
						for(int i = 0; i < oResult.length; i++){
							vTemp.addElement(oResult[i]);
						}
						if(vTemp.size() > 0){
							String s[] = new String[vTemp.size()];
							vTemp.copyInto(s);
							oTemp = CacheTool.getColumnValue("bd_taxitems","pk_taxitems","taxratio",s);
							if(oTemp != null){
								oResult = (Object[]) oTemp;
								Hashtable hTaxRate = new Hashtable(); 
								for(int i = 0; i < sBaseID.length; i++){
									if(oResult[i] != null) hTaxRate.put(sBaseID[i],oResult[i]);
									else hTaxRate.put(sBaseID[i],new UFDouble(0));
								}
								if(m_sZG.toUpperCase().equals("N")){
									for(int i = 0; i < m_estimate1VOs.length; i++){
										if(m_estimate1VOs[i].getCfirsttype() == null){
											UFDouble nTaxRate = new UFDouble(0);
											oTemp = hTaxRate.get(m_estimate1VOs[i].getCbaseid());
											if(oTemp != null) nTaxRate = new UFDouble(oTemp.toString());
											m_estimate1VOs[i].setNtaxrate(nTaxRate);
											m_estimate1VOs[i].setIdiscounttaxtype(new Integer(1));
										}
									}
								}else{
									for(int i = 0; i < m_estimate2VOs.length; i++){
										if(m_estimate2VOs[i].getCfirsttype() == null){
											UFDouble nTaxRate = new UFDouble(0);
											oTemp = hTaxRate.get(m_estimate2VOs[i].getCbaseid());
											if(oTemp != null) nTaxRate = new UFDouble(oTemp.toString());
											m_estimate2VOs[i].setNtaxrate(nTaxRate);
											m_estimate2VOs[i].setIdiscounttaxtype(new Integer(1));
										}
									}									
								}
							}
						}
					}
				}
			}
			//取客商管理ID,客商名称。
			if(m_sZG.toUpperCase().equals("N")){
				m_estimate1VOs = setPk_cubasdc(m_estimate1VOs);
			}else{
				m_estimate2VOs = setPk_cubasdc(m_estimate2VOs);
			}
			//获取运费单价--取运费价格表数据
			if(m_sZG.toUpperCase().equals("N")){
				m_estimate1VOs = setNprice(m_estimate1VOs);
			}else{
				m_estimate2VOs = setNprice(m_estimate2VOs);
			}
			//执行单价计算公式
			if(m_sZG.toUpperCase().equals("N")){
				//m_estimate1VOs = calculateTaxPriceForEstimateVO(m_estimate1VOs);
			}else{
				//m_estimate2VOs = calculateTaxPriceForEstimateVO(m_estimate2VOs);
			}

			getBillCardPanel().getBillModel().clearBodyData();
			if (m_sZG.toUpperCase().equals("N")) getBillCardPanel().getBillData().setBodyValueVO(m_estimate1VOs);
			else getBillCardPanel().getBillData().setBodyValueVO(m_estimate2VOs);

			getBillCardPanel().getBillModel().execLoadFormula();
//			getBillCardPanel().getBillModel().execLoadFormulaByKey("cprovidername");
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();

			changeButtonState();

			boolean bSetFlag = m_sZG.toUpperCase().equals("N");

			getBillCardPanel().setEnabled(bSetFlag);

			if (bSetFlag)
				setPartEditable();

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this, "出库单查询", "SQL语句错误！");
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog.showErrorDlg(this, "出库单查询", "数组越界错误！");
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			MessageDialog.showErrorDlg(this, "出库单查询", "空指针错误！");
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, "出库单查询", e.getMessage());
			SCMEnv.out(e);
			return;
		}
	}
}
//设置客商档案ID,客商名称。
private saleEstimateVO[] setPk_cubasdc(saleEstimateVO VO[]) {
	// TODO Auto-generated method stub
	Hashtable<String, String> litable =  new Hashtable<String, String>();
	for (int i = 0; i < VO.length; i++) {
		String ccustid = VO[i].getCcustomerid();
		if(ccustid=="" || ccustid == null){
			ccustid = "null";
		}
		if(ccustid != "null"){
			if(litable.get(ccustid) == null || litable.get(ccustid) == ""){
				String pkcunam = VO[i].getCcustomerid();
				litable.put(ccustid, pkcunam);
			}
		}
	}
	String ccsql = "";
	Set<String> keys = litable.keySet();  
    for(String key: keys){
    	ccsql +="'";
        ccsql += litable.get(key);
        ccsql +="',";
    } 
    int indexint = ccsql.length();
    String sqlhh = ccsql.substring(0, indexint-1);
	//
//	String sql= "select pk_cumandoc,pk_cubasdoc from bd_cumandoc where pk_cumandoc in ("+sqlhh+")";
    String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey().toString(); 
	StringBuffer sqlq = new StringBuffer();
	sqlq.append(" select bd_cumandoc.pk_cubasdoc,bd_cumandoc.pk_cumandoc,dm_trancust.pk_trancust ") 
	.append("   from bd_cubasdoc ") 
	.append("  inner join dm_trancust ") 
	.append("     on bd_cubasdoc.pk_cubasdoc = dm_trancust.pkcusmandoc ") 
	.append("  inner join bd_cumandoc ") 
	.append("     on bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ") 
	.append("  where nvl(dm_trancust.dr,0) = 0 ") 
	.append("    and bd_cumandoc.custflag in ('0', '2') ") 
	.append("    and bd_cumandoc.pk_corp = '"+pk_corp+"' ") 
	.append("    and dm_trancust.pk_trancust in ("+sqlhh+") ") ;

	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());//使用IUAPQueryBS服务组件，进得查询操作
	MapListProcessor alp = new MapListProcessor();//集合处理器
	Object obj = null;
	try {
		obj = query.executeQuery(sqlq.toString(), alp);//执得查询
	} catch (BusinessException e1) {
		e1.printStackTrace();
	}
	HashMap<String, String[]> listmap = new HashMap<String, String[]>();
	ArrayList addrList = (ArrayList) obj;
	if (addrList != null && addrList.size() > 0) {
		for (int i = 0; i < addrList.size(); i++) {
			Map addrMap = (Map) addrList.get(i);
			String pk_cumandoc = addrMap.get("pk_cumandoc").toString();
			String pk_cubasdoc = addrMap.get("pk_cubasdoc").toString();
			String pk_trancust = addrMap.get("pk_trancust").toString();
			String[] listpk = new String[2];
			listpk[0] = pk_cumandoc;
			listpk[1] = pk_cubasdoc;
			listmap.put(pk_trancust, listpk);
		}
		
		for (int i = 0; i < VO.length; i++) {
			String tmp = VO[i].getCcustomerid();
			if(tmp != null){
				if(listmap.containsKey(tmp)){
					String[] listpp = listmap.get(tmp);
					String pk_cuman = listpp[0];
					String pk_cubas = listpp[1];
					VO[i].setCcustomerid(pk_cuman);
					VO[i].setPk_cubasdoc(pk_cubas);
				}else{
					VO[i].setCcustomerid(null);
				}
	        }
		}
		
	}
	return VO;
}
//根据发货地点和到货地点来
private saleEstimateVO[] setNprice(saleEstimateVO VO[]) {
	// TODO Auto-generated method stub
	Hashtable<String, String> litable =  new Hashtable<String, String>();
	for (int i = 0; i < VO.length; i++) {
		String cgeneralhid = VO[i].getCgeneralhid();//销售出库单主键
		String vuserdefh17 = VO[i].getVuserdefh17();
		String vuserdefh19 = VO[i].getVuserdefh19();
		String pk_defdoch17 = VO[i].getPk_defdoch17();
		String pk_defdoch19 = VO[i].getPk_defdoch19();
		
		if(litable.get(cgeneralhid)==null){
			String pkdef  = " and pkfromaddress = '"+pk_defdoch17+"' and pktoaddress = '"+pk_defdoch19+"' ";
			litable.put(cgeneralhid, pkdef);
		}
	}
	//查询单价及税率
	HashMap<String, UFDouble[]> listmap = new HashMap<String, UFDouble[]>();
	for(Iterator itr = litable.keySet().iterator(); itr.hasNext();){
		String key = (String) itr.next();
		String value = (String) litable.get(key);
		StringBuffer tempsql = new StringBuffer();
		tempsql.append(" select taxprice,dbaseprice,rate from dm_baseprice ") 
		.append("        where nvl(dr,0)=0 ");
		tempsql.append(value);
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());//使用IUAPQueryBS服务组件，进得查询操作
		MapListProcessor alp = new MapListProcessor();//集合处理器
		Object obj = null;
		try {
			obj = query.executeQuery(tempsql.toString(), alp);//执得查询
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
		ArrayList addrList = (ArrayList) obj;
		if (addrList != null && addrList.size() > 0) {
				Map addrMap = (Map) addrList.get(0);
				UFDouble[] tmprice = new UFDouble[3];
				UFDouble taxprice = new UFDouble(addrMap.get("taxprice").toString());
				UFDouble dbaseprice = new UFDouble(addrMap.get("dbaseprice").toString());
				UFDouble rate = new UFDouble(addrMap.get("rate").toString());
				tmprice[0] = taxprice;
				tmprice[1] = dbaseprice;
				tmprice[2] = rate;
				listmap.put(key, tmprice);
		}else{
			UFDouble[] tprice = new UFDouble[3];
			tprice[0]=new UFDouble(0);
			tprice[1]=new UFDouble(0);
			tprice[2]=new UFDouble(11);
			listmap.put(key, tprice);
		}
	}
	//将查询出来的单价和税率入VO
	for (int i = 0; i < VO.length; i++) {
		String chid = VO[i].getCgeneralhid();
		UFDouble[] tmpl = listmap.get(chid);
		UFDouble taxprice = tmpl[0];//含税单价
		UFDouble dbaseprice = tmpl[1];//无税单价
		UFDouble rate = tmpl[2];//税率
		VO[i].setNprice(dbaseprice);
		VO[i].setNmoney(dbaseprice);
		VO[i].setNtaxprice(taxprice);
		VO[i].setNtotalmoney(taxprice);
		VO[i].setNtaxrate(rate);
	}
	return VO;
}

/*
 * 计算需暂估的采购入库单的含税单价和价税合计
 */
private saleEstimateVO[] calculateTaxPriceForEstimateVO(saleEstimateVO VO[]){
	int[] descriptions = new int[] {
			RelationsCal.DISCOUNT_TAX_TYPE_NAME,	//扣税类别名(应税内含，应税外加，不计税)
			RelationsCal.DISCOUNT_TAX_TYPE_KEY,//扣税类别
			RelationsCal.NUM,//主数量
			RelationsCal.NET_PRICE_ORIGINAL,//净单价	
			RelationsCal.MONEY_ORIGINAL,//金额
			RelationsCal.NET_TAXPRICE_ORIGINAL,//净含税单价
			RelationsCal.SUMMNY_ORIGINAL, //价税合计		--原币
			RelationsCal.TAXRATE,	//税率
			RelationsCal.DISCOUNT_RATE,//扣率
			RelationsCal.PRICE_ORIGINAL,//单价
			RelationsCal.TAXPRICE_ORIGINAL,//含税单价
			RelationsCal.TAX_ORIGINAL      //税额
	};

	int nPricePolicy = PuTool.getPricePriorPolicy(m_sUnitCode);
	String sChangedKey = "nprice";

	for(int i = 0; i < VO.length; i++){
		String s = "应税内含";
		if(VO[i].getIdiscounttaxtype().intValue() == 1) s = "应税外加";
		if(VO[i].getIdiscounttaxtype().intValue() == 2) s = "不计税";
	
		String[] keys = new String[] {
				s,
				"idiscounttaxtype",
				"noutnum",
				"nprice",
				"nmoney",
				"ntaxprice",
				"ntotalmoney", 
				"ntaxrate",
				"ndiscountrate",
				"nnetprice",
				"nnettaxprice",
				"ntaxmoney"
		};

		if(VO[i].getNPricePolicy() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE){
			if(VO[i].getBMoney() != null && VO[i].getBMoney().booleanValue()) sChangedKey = "ntotalmoney";
			else sChangedKey = "ntaxprice";
		}else{
			if(VO[i].getBMoney() != null && VO[i].getBMoney().booleanValue()) sChangedKey = "nmoney";
			else sChangedKey = "nprice";			
		}
		
		SCMRelationsCal.calculate(VO[i], new int[] {nPricePolicy}, sChangedKey, descriptions, keys);
	}
	
	return VO;
}
/**
* 功能描述:全选
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：晁志平　FOR　V30
*/
public void onSelectAll() {
	int nRow = getBillCardPanel().getBillModel().getRowCount();
	for (int i = 0; i < nRow; i++)
		getBillCardPanel().getBillModel().setRowState(i, BillModel.SELECTED);

	getBillCardPanel().updateUI();
	//设置按钮状态：除全选外为正常
	for (int i = 0; i < 5; i++) {
		m_nButtonState[i] = 0;
	}
	m_nButtonState[0] = 1;
	if (m_sZG.toUpperCase().equals("N")){
		m_nButtonState[3] = 1;
		m_nButtonState[7] = 0;
	}else{
		m_nButtonState[2] = 1;
		m_nButtonState[7] = 1;
	}
	changeButtonState();
}
/**
* 功能描述:全消
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：晁志平　FOR　V30
*/
public void onSelectNo() {
	int nRow = getBillCardPanel().getBillModel().getRowCount();
	for (int i = 0; i < nRow; i++)
		getBillCardPanel().getBillModel().setRowState(i, BillModel.NORMAL);

	getBillCardPanel().updateUI();
	//设置按钮状态：除全消外全部为正常
	for (int i = 0; i < 5; i++) {
		m_nButtonState[i] = 0;
	}
	m_nButtonState[1] = 1;
	m_nButtonState[3] = 1;
	m_nButtonState[2] = 1;
	m_nButtonState[7] = 1;
	changeButtonState();
}
/**
* 功能描述:取消暂估
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2004-3-24 14:41:50
* 修改：晁志平　FOR　V30
*/
public void onUnEstimate() {

	nc.vo.scm.pu.Timer timerDebug = new nc.vo.scm.pu.Timer();
	timerDebug.start();

	Integer nSelected[] = null;
	Vector v = new Vector();
	Vector vv = new Vector();
	int nRow = getBillCardPanel().getRowCount();
	for (int i = 0; i < nRow; i++) {
		int nStatus = getBillCardPanel().getBillModel().getRowState(i);
		if (nStatus == BillModel.SELECTED)
			v.addElement(new Integer(i));
		else
			vv.addElement(new Integer(i));
	}
	nSelected = new Integer[v.size()];
	v.copyInto(nSelected);

	if (nSelected == null || nSelected.length == 0) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估", "未选中出库单！");
		return;
	}
	Vector vTemp = new Vector();
	for (int i = 0; i < nSelected.length; i++) {
		saleEstimateVO vo = m_estimate2VOs[nSelected[i].intValue()];
		vTemp.addElement(vo);
	}
	saleEstimateVO[] VOs = new saleEstimateVO[vTemp.size()];
	vTemp.copyInto(VOs);

	timerDebug.addExecutePhase("组织数据");

	try {
		EstimateHelper.antiEstimate_saleyf(VOs, getClientEnvironment().getUser().getPrimaryKey());
	} catch (java.sql.SQLException e) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估","SQL语句错误！");
		SCMEnv.out(e);
		return;
	} catch (ArrayIndexOutOfBoundsException e) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估", "数组越界错误！");
		SCMEnv.out(e);
		return;
	} catch (NullPointerException e) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估", "空指针错误！");
		SCMEnv.out(e);
		return;
	} catch (nc.vo.pub.BusinessException e) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估", e.getMessage());
		SCMEnv.out(e);
		return;
	} catch (Exception e) {
		MessageDialog.showErrorDlg(this, "出库单取消暂估", e.getMessage());
		SCMEnv.out(e);
		return;
	}
	timerDebug.addExecutePhase("远程调用 antiEstimate(EstimateVO[],String)");

	this.showHintMessage( "取消暂估成功！");

	//取消暂估后的入库单不再显示在界面
	if (vv == null || vv.size() == 0) {
		//所有入库单已取消暂估完毕
		getBillCardPanel().getBillData().clearViewData();
		getBillCardPanel().updateUI();
		//除查询外所有为灰
		for (int i = 0; i < 5; i++)
			m_nButtonState[i] = 1;
		m_nButtonState[4] = 0;
		changeButtonState();
		return;
	}

	Vector v0 = new Vector();
	for (int i = 0; i < vv.size(); i++) {
		int n = ((Integer) vv.elementAt(i)).intValue();
		v0.addElement(m_estimate2VOs[n]);
	}
	m_estimate2VOs = new saleEstimateVO[v0.size()];
	v0.copyInto(m_estimate2VOs);

	getBillCardPanel().getBillModel().setBodyDataVO(m_estimate2VOs);
	getBillCardPanel().getBillModel().execLoadFormula();
	getBillCardPanel().getBillModel().updateValue();
	getBillCardPanel().updateUI();

	//设置按钮状态：除暂估，反暂，全消外全部为正常
	for (int i = 0; i < 5; i++) {
		m_nButtonState[i] = 0;
	}
	m_nButtonState[1] = 1;
	m_nButtonState[2] = 1;
	m_nButtonState[3] = 1;
	m_nButtonState[7] = 1;
	changeButtonState();

	timerDebug.addExecutePhase("取消暂估后处理(不再显示单据及按钮逻辑)");

	timerDebug.showAllExecutePhase("取消暂估UI时间分布：");
}
/**
* 功能描述:暂估时,除无税单价, 无税金额, 含税单价, 价税合计和税率可编辑外, 其余数据项均不可编辑,
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2014-10-10
*/
private void setPartEditable() {
	//
	BillItem items[] = getBillCardPanel().getBodyShowItems();
	//
	for (int i = 0; i < items.length; i++) {
		items[i].setEnabled("nprice".equals(items[i].getKey().trim()) 
				|| "nmoney".equals(items[i].getKey().trim())
				|| "ntaxprice".equals(items[i].getKey().trim())
				|| "ntotalmoney".equals(items[i].getKey().trim())
				|| "ntaxrate".equals(items[i].getKey().trim()));
		items[i].setEdit("nprice".equals(items[i].getKey().trim()) 
				|| "nmoney".equals(items[i].getKey().trim())
				|| "ntaxprice".equals(items[i].getKey().trim())
				|| "ntotalmoney".equals(items[i].getKey().trim())
				|| "ntaxrate".equals(items[i].getKey().trim()));

	}
	//
	UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("nprice").getComponent();
	UITextField nPriceUI = (UITextField) nRefPanel.getUITextField();
	nPriceUI.setMaxLength(16);
	nPriceUI.setDelStr("-");
	
	nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("ntaxprice").getComponent();
	nPriceUI = (UITextField) nRefPanel.getUITextField();
	nPriceUI.setMaxLength(16);
	nPriceUI.setDelStr("-");	
}
/**
* 功能描述:处理列表选择
* 参数：
* 返回：
* 作者：王凯飞
* 创建：2002-9-26 14:41:50
*/
public void valueChanged(javax.swing.event.ListSelectionEvent event) {
	if ((ListSelectionModel) event.getSource() == getBillCardPanel().getBillTable().getSelectionModel()) {
		int nRow = getBillCardPanel().getBillModel().getRowCount();

		//表体所有行恢复正常（不选择）
		int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
		for (int i = 0; i < nRow; i++) {
			getBillCardPanel().getBillModel().setRowState(i, BillModel.NORMAL);
		}

		//获得表体选择行数
		if (nSelected != null && nSelected.length > 0) {
			for (int i = 0; i < nSelected.length; i++) {
				int j = nSelected[i];
				getBillCardPanel().getBillModel().setRowState(j, BillModel.SELECTED);
			}
		}

		//设置按钮状态：全部为正常
		for (int i = 0; i < 5; i++) {
			m_nButtonState[i] = 0;
		}
		if (m_sZG.toUpperCase().equals("N")){
			m_nButtonState[3] = 1;
			m_nButtonState[7] = 0;
		}else{
			m_nButtonState[2] = 1;
			m_nButtonState[7] = 1;
		}

		int m = 0;
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED)
				m++;
		}
		//选中行数大于0，全消正常，否则为灰
		if (m > 0){
			m_nButtonState[1] = 0;
			m_nButtonState[7] = 0;
			if(m == nRow) m_nButtonState[0] = 1;
			else m_nButtonState[0] = 0;
		}else {
			m_nButtonState[0] = 0;
			m_nButtonState[1] = 1;
			m_nButtonState[7] = 1;
			if (m_sZG.toUpperCase().equals("N"))
				m_nButtonState[2] = 1;
			else
				m_nButtonState[3] = 1;
		}
		changeButtonState();

	}
}
public boolean beforeEdit(BillEditEvent e) {
	// TODO 自动生成方法存根
	Object oTaxRate = getBillCardPanel().getBillModel().getValueAt(e.getRow(), "ntaxrate");
	if(oTaxRate == null || oTaxRate.toString().trim().length() == 0){
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(0), e.getRow(), "ntaxrate");
	}
	return true;
}
}