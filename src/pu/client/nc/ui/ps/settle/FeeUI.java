package nc.ui.ps.settle;

import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.*;
import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ListSelectionModel;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ps.settle.*;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.query.*;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.lang.*;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;


/**
 * 功能描述:费用单独结算
 *
 * 作者: 熊海情
 *
 * 创建日期:2001-6-1
 *
 * 修改：2004-09-10 袁野
 */
public class FeeUI extends nc.ui.pub.ToftPanel implements BillEditListener,javax.swing.event.ListSelectionListener, ValueChangedListener{
	//界面控制按钮
	private ButtonObject m_buttons1[]=null;
	private ButtonObject m_buttons2[]=null;

	//按钮状态：0 正常；1 置灰；2 不可视
	private int m_nButtonState[]=null;

	//列表单据
	private BillListPanel m_listPanel=null;
	private UIComboBox m_comDistribute=null;

	//结算汇总表
	private BillListPanel m_totalPanel=null;

	//单位编码，系统应提供方法获取
	private String m_sUnitCode=getCorpPrimaryKey();

	//查询条件
	private PoQueryCondition m_condClient1=null;
	private PoQueryCondition m_condClient2=null;

	//缓存
	private StockVO m_stockVOs[]=null;
	private SettletotalVO m_totalVOs[]=null;
	private FeeinvoiceVO m_feeVOs[]=null;
	private FeeinvoiceVO m_discountVOs[]=null;
	private FeeinvoiceVO m_specialVOs[]=null;
	private SettlebillItemVO m_settleVOs[]=null;

	//缓存,以执行返回操作
	private StockVO m_mStockVOs[]=null;
	private FeeinvoiceVO m_mFeeVOs[]=null;
	private FeeinvoiceVO m_mDiscountVOs[]=null;
	private FeeinvoiceVO m_mSpecialVOs[]=null;

	//费用发票是否进入成本
	private UFBoolean m_bIsentercost[]=null;
	//费用发票的存货ID
	private Vector m_vID=null;

	//按钮标志，以确定是否可结算
	private boolean m_bStock=false;
	private boolean m_bInvoice=false;

	//差异转入方式
	private String m_sDifferenceMode=null;

	//主计量小数位
	private int m_unitDecimal = 2;
	//采购单价小数位
	private int m_priceDecimal = 2;
	//采购金额小数位(本位币小数位)
	private int m_moneyDecimal = 2;

	//已加锁
	private boolean m_bSettling=false;

	//成本要素序号
	private Integer m_nCostNO[]=null;

	//库存是否启用
	private boolean m_bICStartUp=false;

	//合同是否启用
	private boolean m_bCTStartUp=false;

	//系统初始化参数 "BD501","BD505","BD301"
	private int measure[] = null;

	//系统设置的暂估方式和差异转入方式
	private nc.vo.pub.para.SysInitVO initVO[] = null;

	//成本要素
	private nc.vo.ps.factor.CostfactorVO CostfactorVOtempVO[] = null;

	//是否已费用结算
	private UIComboBox m_showBox = null;

	//收货公司
	private UIRefPane m_corpPane = null;

	//收货仓库
	private UIRefPane m_warePane = null;


/**
 * FeeUI 构造子注解。
 */
public FeeUI() {
	super();
	init();
}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
public void afterEdit(BillEditEvent event) {
	if(isFocusListBody(event)) computeBodyData(event);
}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
public void bodyRowChange(BillEditEvent event) {
}
/**
 * 此处插入方法说明。
 * 功能描述:改变界面按钮状态
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
private void changeButtonState() {
	if(getBillListPanel().isVisible()){
		for(int i=0;i<m_nButtonState.length;i++){
			if(m_nButtonState[i]==0){
				m_buttons1[i].setVisible(true);
				m_buttons1[i].setEnabled(true);
			}
			if(m_nButtonState[i]==1){
				m_buttons1[i].setVisible(true);
				m_buttons1[i].setEnabled(false);
			}
			if(m_nButtonState[i]==2){
				m_buttons1[i].setVisible(false);
			}

			this.updateButton(m_buttons1[i]);
		}
	}else{
		for(int i=0;i<m_nButtonState.length;i++){
			if(m_nButtonState[i]==0){
				m_buttons2[i].setVisible(true);
				m_buttons2[i].setEnabled(true);
			}
			if(m_nButtonState[i]==1){
				m_buttons2[i].setVisible(true);
				m_buttons2[i].setEnabled(false);
			}
			if(m_nButtonState[i]==2){
				m_buttons2[i].setVisible(false);
			}

			this.updateButton(m_buttons2[i]);
		}
	}
}
/**
 * 此处插入方法说明。
 * 功能描述:改变成本要素显示名称
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private void changeFactorShowName() {
	if(m_feeVOs==null || m_feeVOs.length==0){
		//隐藏所有成本要素
		for(int i=0;i<5;i++){
			getBillTotalPanel().hideHeadTableCol("nfactor"+(i+1));
		}
		return;
	}

	//成本要素ID，是否进入成本唯一组合
	Vector vName=new Vector();
	m_vID=new Vector();
	m_vID.addElement(m_mFeeVOs[0].getCcostfactorid().trim());
	for(int i = 1; i < m_mFeeVOs.length; i++){
		String s1 = m_mFeeVOs[i].getCcostfactorid().trim();
		if(!m_vID.contains(s1)) m_vID.addElement(s1);
	}

	//获得成本要素序号
	String sCostPK[] = null;
	if(m_vID.size()>0){
		String cId[]=new String[m_vID.size()];
		m_vID.copyInto(cId);
		try{
			Vector v=SettleHelper.queryCostfactorNO(m_sUnitCode,cId);
			if(v==null || v.size()==0) return;
			Vector v1=(Vector)v.elementAt(0);
			if(v1!=null && v1.size()>0){
				m_nCostNO=new Integer[v1.size()];
				v1.copyInto(m_nCostNO);
			}
			vName=(Vector)v.elementAt(1);
			Vector v2 = (Vector) v.elementAt(2);
			if(v2 != null && v2.size() > 0){
				sCostPK = new String[v2.size()];
				v2.copyInto(sCostPK);
			}
		}catch(Exception e){
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000059")/*@res "获得成本要素序号、名称和主键"*/,e.getMessage());
			return;
		}
	}

	if(sCostPK == null || sCostPK.length == 0) return;
	String sName[] = new String[sCostPK.length];

	for(int i=0;i<sCostPK.length;i++){
		String s1 = sCostPK[i].trim();
		for(int j = 0; j < m_vID.size(); j++){
			String s2 = (String)m_vID.elementAt(j);
			s2 = s2.trim();
			if(s1.equals(s2)){
				sName[i]=(String)vName.elementAt(j);
			}
		}
	}

	//改变成本要素显示名称
	for(int i=1;i<=5;i++){
		String sKey="nfactor"+i;
		getBillTotalPanel().showHeadTableCol(sKey);
	}

	if(sName!=null && sName.length>0){
		BillItem items[]=getBillTotalPanel().getHeadBillModel().getBodyItems();
		for(int i=0;i<sName.length;i++){
			String sName0=sName[i];
			String sKey="nfactor"+(m_nCostNO[i].intValue()+1);
			for(int j=0;j<items.length;j++){
				String s=items[j].getKey().trim();
				if(s.equals(sKey)) items[j].setName(sName0);
			}
		}
		BillListData data=new BillListData(getBillTotalPanel().getTempletData("40040502020200000000"));
		data=initTotalDecimal(data);
		data.setHeadItems(items);
		getBillTotalPanel().setListData(data);

		getBillTotalPanel().getParentListPanel().setShowThMark(true);
		getBillTotalPanel().getChildListPanel().setShowThMark(true);

		getBillTotalPanel().getParentListPanel().setTotalRowShow(true);
		getBillTotalPanel().getChildListPanel().setTotalRowShow(true);

		//重新加载监听
		getBillTotalPanel().addEditListener(this);
		getBillTotalPanel().addBodyEditListener(this);
		
		getBillTotalPanel().getHeadTable().setSortEnabled(false);
		getBillTotalPanel().getBodyTable().setSortEnabled(false);
	}

	//若成本要素显示名称没有改变,隐藏它
	for(int i=0;i<5;i++){
		boolean b=false;
		for(int j=0;j<m_nCostNO.length;j++){
			int k=m_nCostNO[j].intValue();
			if(i==k){
				b=true;
				break;
			}
		}
		if(!b) getBillTotalPanel().hideHeadTableCol("nfactor"+(i+1));
	}

}
/**
 * 此处插入方法说明。
 * 功能描述:检查:如果合同未启用,则查询条件中不应包含合同号
 * 输入参数:
 * 返回值:
 * 异常处理:2003/10/28 xhq
 */
private boolean checkQueryCondition(ConditionVO VOs[]) {
	boolean b = true;
	if(VOs == null || VOs.length == 0) return b;

	if(m_bCTStartUp) return b;

	for(int i = 0; i < VOs.length; i++){
		String sName = VOs[i].getFieldCode().trim();
		String sValue = VOs[i].getValue();

		if(sName.equals("vcontractcode") && sValue != null && sValue.trim().length() > 0){
			b = false;
			break;
		}
	}

	return b;
}
/**
 * 此处插入方法说明。
 *  功能描述:费用发票中，修改本次结算金额
 *  输入参数:
 *  返回值:
 *  异常处理:
 */
private void computeBodyData(BillEditEvent event) {
	int nRow=getBillTotalPanel().getBodyBillModel().getRowCount();
	FeeinvoiceVO tempVOs[]=new FeeinvoiceVO[nRow];
	for(int i=0;i<nRow;i++) tempVOs[i]=new FeeinvoiceVO();

	getBillTotalPanel().getBodyBillModel().getBodyValueVOs(tempVOs);
	nRow=event.getRow();

	if(tempVOs[nRow].getNsettlemny()==null || tempVOs[nRow].getNsettlemny().toString().length()==0){
		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000061")/*@res "结算金额不能为空！"*/);
		if(nRow<m_feeVOs.length){
			getBillTotalPanel().getBodyBillModel().setValueAt(m_feeVOs[nRow].getNnosettlemny(),nRow,"nsettlemny");
		}else{
			getBillTotalPanel().getBodyBillModel().setValueAt(m_discountVOs[nRow-m_feeVOs.length].getNnosettlemny(),nRow,"nsettlemny");
		}
		return;
	}

	int nFee=0;
	int nDiscount=0;
	if(m_feeVOs!=null && m_feeVOs.length>0) nFee=m_feeVOs.length;
	if(m_discountVOs!=null && m_discountVOs.length>0) nDiscount=m_discountVOs.length;

	if(nRow<nFee){
		if(Math.abs(tempVOs[nRow].getNsettlemny().doubleValue())>Math.abs(m_feeVOs[nRow].getNnosettlemny().doubleValue())){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000062")/*@res "本次费用发票结算金额的绝对值不能大于未结算金额的绝对值！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_feeVOs[nRow].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}
		if(tempVOs[nRow].getNsettlemny().doubleValue()*m_feeVOs[nRow].getNnosettlemny().doubleValue()<=0.0){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000063")/*@res "本次费用发票结算金额不能改变符号！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_feeVOs[nRow].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}

		m_feeVOs[nRow].setNsettlemny(tempVOs[nRow].getNsettlemny());
	}
	else if(nRow<nFee+nDiscount){
		int n=nFee;
		if(Math.abs(tempVOs[nRow].getNsettlemny().doubleValue())>Math.abs(m_discountVOs[nRow-n].getNnosettlemny().doubleValue())){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000064")/*@res "本次折扣结算金额的绝对值不能大于未结算金额的绝对值！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_discountVOs[nRow-n].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}
		if(tempVOs[nRow].getNsettlemny().doubleValue()*m_discountVOs[nRow-n].getNnosettlemny().doubleValue()<=0.0){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000065")/*@res "本次折扣结算金额不能改变符号！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_discountVOs[nRow-n].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}

		m_discountVOs[nRow-n].setNsettlemny(tempVOs[nRow].getNsettlemny());
	}
	else{
		int n=nFee+nDiscount;
		if(Math.abs(tempVOs[nRow].getNsettlemny().doubleValue())>Math.abs(m_specialVOs[nRow-n].getNnosettlemny().doubleValue())){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000102")/*@res "本次发票结算金额的绝对值不能大于未结算金额的绝对值！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_specialVOs[nRow-n].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}
		if(tempVOs[nRow].getNsettlemny().doubleValue()*m_specialVOs[nRow-n].getNnosettlemny().doubleValue()<=0.0){
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000060")/*@res "修改结算金额"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000072")/*@res "本次发票结算金额不能改变符号！"*/);
			getBillTotalPanel().getBodyBillModel().setValueAt(m_specialVOs[nRow-n].getNnosettlemny(),nRow,"nsettlemny");
			return;
		}

		m_specialVOs[nRow-n].setNsettlemny(tempVOs[nRow].getNsettlemny());
	}

	return;
}
/**
 * 此处插入方法说明。
 * 功能描述:折扣分摊到已结入库单（按金额）
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private void distributeDiscount() {
	if(m_discountVOs==null || m_discountVOs.length==0){
		//MessageDialog.showHintDlg(this,"分摊","不存在折扣!");
		return;
	}

	//折扣分摊到所有已结入库单
	for(int i=0;i<m_discountVOs.length;i++){
		double dd=m_discountVOs[i].getNsettlemny().doubleValue();
		double total=0.0;
		double sum=0.0;
		for(int j=0;j<m_stockVOs.length;j++){
			total+=m_stockVOs[j].getNaccumsettlemny().doubleValue();
		}
		if(total==0.0) continue;
		for(int j=0;j<m_stockVOs.length-1;j++){
			double d=m_stockVOs[j].getNaccumsettlemny().doubleValue();
			double ddd=m_totalVOs[j].getNdiscountmny().doubleValue();
			ddd+=dd/total*d;
			m_totalVOs[j].setNdiscountmny(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,ddd)));
			sum+=PuTool.getRoundDouble(m_moneyDecimal,dd/total*d);
		}

		//最后一张，分摊方法有所不同
		double ddd=m_totalVOs[m_stockVOs.length-1].getNdiscountmny().doubleValue();
		ddd+=dd-sum;
		m_totalVOs[m_stockVOs.length-1].setNdiscountmny(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,ddd)));
	}
	return;
}
/**
 * 此处插入方法说明。
 * 功能描述:费用分摊到已结入库单
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private void distributeFee() {
	if(m_feeVOs==null || m_feeVOs.length==0){
		//MessageDialog.showHintDlg(this,"分摊","不存在成本要素!");
		return;
	}

	//获得存货的单位重量，单位体积
	Vector v=new Vector();
	try{
		v=SettleHelper.getSUnitWeightAndVolume(m_stockVOs);
	}catch(Exception e){
		SCMEnv.out(e);
		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000077")/*@res "获得存货的单位重量，单位体积"*/,e.getMessage());
		return;
	}
	UFDouble unitWeight[]=new UFDouble[v.size()];
	UFDouble unitVolume[]=new UFDouble[v.size()];
	for(int i=0;i<v.size();i++){
		Vector vTemp=new Vector();
		vTemp=(Vector)v.elementAt(i);
		String s=(String)vTemp.elementAt(0);
		if (s==null||"".equals(s)) {//add by shikun 2015-01-28 进行是否为空的判断
			s = "0";
		}
		unitWeight[i]=new UFDouble(new Double(s));
		s=(String)vTemp.elementAt(1);
		if (s==null||"".equals(s)) {//add by shikun 2015-01-28 进行是否为空的判断
			s = "0";
		}
		unitVolume[i]=new UFDouble(new Double(s));
	}

	//判断费用隶属第几个成本要素
	int n[]=new int[m_feeVOs.length];
	for(int i=0;i<m_feeVOs.length;i++){
		String s1=m_feeVOs[i].getCcostfactorid().trim();
		for(int j=0;j<m_vID.size();j++){
			String s2=(String)m_vID.elementAt(j);
			s2=s2.trim();
			if(s1.equals(s2)){
				n[i]=m_nCostNO[j].intValue();
				break;
			}
		}
	}

	//费用分摊到所有发票
	for(int i=0;i<m_feeVOs.length;i++){
		distributeFeeAll(i,n[i],unitWeight,unitVolume);
	}

	return;
}
/**
 * 此处插入方法说明。
 * 功能描述:费用分摊到所有已结入库单
 * 输入参数:nFee 费用发票序号；nCost 隶属成本要素序号；
 *          unitWeight 存货单位重量；unitVolume 存货单位体积
 * 返回值:
 * 异常处理:
 */
private void distributeFeeAll(int nFee,int nCost,UFDouble unitWeight[],UFDouble unitVolume[]) {
	if (m_feeVOs == null || m_feeVOs.length==0) return;
	int nDistribute=m_feeVOs[nFee].getFapportionmode().intValue();
	double total=m_feeVOs[nFee].getNsettlemny().doubleValue();
	double sum=0.0;

	if(nDistribute==0){
		//按数量分摊
		double nNum=0.0;
		for(int i=0;i<m_stockVOs.length;i++) nNum+=m_stockVOs[i].getNaccumsettlenum().doubleValue();
		if(nNum==0.0) return;
		for(int i=0;i<m_stockVOs.length-1;i++){
			if(nCost==0){
				double dd=m_totalVOs[i].getNfactor1().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue();
				m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue());
			}
			if(nCost==1){
				double dd=m_totalVOs[i].getNfactor2().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue();
				m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue());
			}
			if(nCost==2){
				double dd=m_totalVOs[i].getNfactor3().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue();
				m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue());
			}
			if(nCost==3){
				double dd=m_totalVOs[i].getNfactor4().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue();
				m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue());
			}
			if(nCost==4){
				double dd=m_totalVOs[i].getNfactor5().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue();
				m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue());
			}
		}
	}

	else if(nDistribute==1){
		//按金额分摊
		double nNum=0.0;
		for(int i=0;i<m_stockVOs.length;i++) nNum+=m_stockVOs[i].getNaccumsettlemny().doubleValue();
		if(nNum==0.0) return;
		for(int i=0;i<m_stockVOs.length-1;i++){
			if(nCost==0){
				double dd=m_totalVOs[i].getNfactor1().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue();
				m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue());
			}
			if(nCost==1){
				double dd=m_totalVOs[i].getNfactor2().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue();
				m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue());
			}
			if(nCost==2){
				double dd=m_totalVOs[i].getNfactor3().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue();
				m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue());
			}
			if(nCost==3){
				double dd=m_totalVOs[i].getNfactor4().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue();
				m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue());
			}
			if(nCost==4){
				double dd=m_totalVOs[i].getNfactor5().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue();
				m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlemny().doubleValue());
			}
		}
	}

	else if(nDistribute==2){
		//按重量分摊
		double nNum=0.0;
		for(int i=0;i<m_stockVOs.length;i++) nNum+=m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue();
		if(nNum==0.0) return;
		for(int i=0;i<m_stockVOs.length-1;i++){
			if(nCost==0){
				double dd=m_totalVOs[i].getNfactor1().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue();
				m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue());
			}
			if(nCost==1){
				double dd=m_totalVOs[i].getNfactor2().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue();
				m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue());
			}
			if(nCost==2){
				double dd=m_totalVOs[i].getNfactor3().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue();
				m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue());
			}
			if(nCost==3){
				double dd=m_totalVOs[i].getNfactor4().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue();
				m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue());
			}
			if(nCost==4){
				double dd=m_totalVOs[i].getNfactor5().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNinnum().doubleValue()*unitWeight[i].doubleValue();
				m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitWeight[i].doubleValue());
			}
		}
	}

	else{
		//按体积分摊
		double nNum=0.0;
		for(int i=0;i<m_stockVOs.length;i++) nNum+=m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
		if(nNum==0.0) return;
		for(int i=0;i<m_stockVOs.length-1;i++){
			if(nCost==0){
				double dd=m_totalVOs[i].getNfactor1().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
				m_totalVOs[i].setNfactor1(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue());
			}
			if(nCost==1){
				double dd=m_totalVOs[i].getNfactor2().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
				m_totalVOs[i].setNfactor2(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue());
			}
			if(nCost==2){
				double dd=m_totalVOs[i].getNfactor3().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
				m_totalVOs[i].setNfactor3(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue());
			}
			if(nCost==3){
				double dd=m_totalVOs[i].getNfactor4().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
				m_totalVOs[i].setNfactor4(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue());
			}
			if(nCost==4){
				double dd=m_totalVOs[i].getNfactor5().doubleValue();
				dd+=total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue();
				m_totalVOs[i].setNfactor5(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,total/nNum*m_stockVOs[i].getNaccumsettlenum().doubleValue()*unitVolume[i].doubleValue());
			}
		}
	}

	//最后一张，分摊方法有所不同
	if(nCost==0){
		double dd=m_totalVOs[m_stockVOs.length-1].getNfactor1().doubleValue();
		dd+=total-sum;
		m_totalVOs[m_stockVOs.length-1].setNfactor1(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
	}
	if(nCost==1){
		double dd=m_totalVOs[m_stockVOs.length-1].getNfactor2().doubleValue();
		dd+=total-sum;
		m_totalVOs[m_stockVOs.length-1].setNfactor2(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
	}
	if(nCost==2){
		double dd=m_totalVOs[m_stockVOs.length-1].getNfactor3().doubleValue();
		dd+=total-sum;
		m_totalVOs[m_stockVOs.length-1].setNfactor3(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
	}
	if(nCost==3){
		double dd=m_totalVOs[m_stockVOs.length-1].getNfactor4().doubleValue();
		dd+=total-sum;
		m_totalVOs[m_stockVOs.length-1].setNfactor4(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
	}
	if(nCost==4){
		double dd=m_totalVOs[m_stockVOs.length-1].getNfactor5().doubleValue();
		dd+=total-sum;
		m_totalVOs[m_stockVOs.length-1].setNfactor5(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,dd)));
	}
}
/**
 * 此处插入方法说明。
 * 功能描述:一般发票分摊到已结入库单（按金额）
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private void distributeSpecial() {
	if(m_specialVOs==null || m_specialVOs.length==0){
		return;
	}

	//一般发票分摊到同存货的已结入库单
	for(int i=0;i<m_specialVOs.length;i++){
		String s1=m_specialVOs[i].getCmangid().trim();
		double dd=m_specialVOs[i].getNsettlemny().doubleValue();
		double total=0.0;
		double sum=0.0;
		for(int j=0;j<m_stockVOs.length;j++){
			String s2=m_stockVOs[j].getCmangid().trim();
			if(s1.equals(s2)) total+=m_stockVOs[j].getNaccumsettlemny().doubleValue();
		}
		if(total==0.0) continue;
		for(int j=0;j<m_stockVOs.length-1;j++){
			String s2=m_stockVOs[j].getCmangid().trim();
			if(s1.equals(s2)){
				double d=m_stockVOs[j].getNaccumsettlemny().doubleValue();
				double ddd=m_totalVOs[j].getNsettlemny().doubleValue();
				ddd+=dd/total*d;
				m_totalVOs[j].setNsettlemny(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,ddd)));
				sum+=PuTool.getRoundDouble(m_moneyDecimal,dd/total*d);
			}
		}

		//最后一张，分摊方法有所不同
		String s2=m_stockVOs[m_stockVOs.length-1].getCmangid().trim();
		if(s1.equals(s2)){
			double ddd=m_totalVOs[m_stockVOs.length-1].getNsettlemny().doubleValue();
			ddd+=dd-sum;
			m_totalVOs[m_stockVOs.length-1].setNsettlemny(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,ddd)));
		}
	}
	return;
}
/**
 * 此处插入方法说明。
 *  功能描述:将选中的入库单进行归类
 *  输入参数:
 *  返回值:
 *  异常处理:
 */
private boolean doClassification() {
	//入库单归类
	Vector v=new Vector();
	int nRow=getBillListPanel().getHeadBillModel().getRowCount();
	for(int i=0;i<nRow;i++){
		int nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
		if(nStatus==BillModel.SELECTED) v.addElement(m_stockVOs[i]);
	}

	if(v.size()==0){
		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000079")/*@res "未选中入库单！"*/);
		return false;
	}

	//入库单归类前进行缓存,以便返回操作
	Vector v0=new Vector();
	for(int i=0;i<m_stockVOs.length;i++) v0.addElement(m_stockVOs[i]);
	m_mStockVOs=new StockVO[v0.size()];
	v0.copyInto(m_mStockVOs);

	m_stockVOs=new StockVO[v.size()];
	v.copyInto(m_stockVOs);

	return true;
}
/**
 * 此处插入方法说明。
 *  功能描述:费用发票，折扣归类
 *  输入参数:
 *  返回值:
 *  异常处理:
 */
private boolean doFeeClassification() {
	//费用发票，折扣归类
	Integer nSelected[]=null;
	Vector v=new Vector();
	int nRow=getBillListPanel().getBodyBillModel().getRowCount();
	for(int i=0;i<nRow;i++){
		int nStatus=getBillListPanel().getBodyBillModel().getRowState(i);
		if(nStatus==BillModel.SELECTED) v.addElement(new Integer(i));
	}
	nSelected=new Integer[v.size()];
	v.copyInto(nSelected);

	if(nSelected==null || nSelected.length==0) {
		MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000094")/*@res "分摊"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000104")/*@res "即无费用分摊，又无折扣分摊！不能结算！"*/);
		return false;
	}

	//费用发票，折扣归类前进行缓存,以便返回操作
	Vector v0=new Vector();
	int iLen = m_feeVOs == null ? 0 : m_feeVOs.length;
	for(int i=0;i<iLen;i++) v0.addElement(m_feeVOs[i]);
	m_mFeeVOs=new FeeinvoiceVO[v0.size()];
	v0.copyInto(m_mFeeVOs);

	Vector v00=new Vector();
	iLen = m_discountVOs == null ? 0 : m_discountVOs.length;
	for(int i=0;i<iLen;i++) v00.addElement(m_discountVOs[i]);
	m_mDiscountVOs=new FeeinvoiceVO[v00.size()];
	v00.copyInto(m_mDiscountVOs);

	Vector v000=new Vector();
	iLen = m_specialVOs == null ? 0 : m_specialVOs.length;
	for(int i=0;i<m_specialVOs.length;i++) v000.addElement(m_specialVOs[i]);
	m_mSpecialVOs=new FeeinvoiceVO[v000.size()];
	v000.copyInto(m_mSpecialVOs);

	//整理费用发票和折扣
	Vector vv1=new Vector();
	Vector vv2=new Vector();
	Vector vv3=new Vector();
	int n = m_mFeeVOs == null ? 0 : m_mFeeVOs.length;
	int nn = m_mDiscountVOs == null ? 0 : m_mDiscountVOs.length;
	for(int i=0;i<nSelected.length;i++){
		int j=nSelected[i].intValue();
		if(j<n) vv1.addElement(m_feeVOs[j]);
		else if(j<n+nn) vv2.addElement(m_discountVOs[j-n]);
		else vv3.addElement(m_specialVOs[j-n-nn]);
	}

	m_feeVOs=new FeeinvoiceVO[vv1.size()];
	if(vv1.size()>0){
		vv1.copyInto(m_feeVOs);
	}

	m_discountVOs=new FeeinvoiceVO[vv2.size()];
	if(vv2.size()>0){
		vv2.copyInto(m_discountVOs);
	}

	m_specialVOs=new FeeinvoiceVO[vv3.size()];
	if(vv3.size()>0){
		vv3.copyInto(m_specialVOs);
	}

	return true;
}
/**
 * 此处插入方法说明。
 * 功能描述:成功结算后，根据结算单修改费用发票和折扣和一般发票数据
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private void doModification() {
	//费用发票和折扣行主键归类
	Vector vFeeKey=new Vector();
	for(int i=0;i<m_settleVOs.length-1;i++){
		String s=m_settleVOs[i].getCinvoice_bid();
		UFDouble d=m_settleVOs[i].getNsettlenum();
		if(d!=null) continue;
		if(s==null || s.length()==0) continue;
		s=s.trim();
		boolean b=false;
		for(int j=i+1;j<m_settleVOs.length;j++){
			String ss=m_settleVOs[j].getCinvoice_bid();
			if(ss==null || ss.length()==0) continue;
			ss=ss.trim();
			if(s.equals(ss)){
				b=true;
				break;
			}
		}
		if(!b) vFeeKey.addElement(s);
	}
	String sss=m_settleVOs[m_settleVOs.length-1].getCinvoice_bid();
	UFDouble ddd=m_settleVOs[m_settleVOs.length-1].getNsettlenum();
	if(sss!=null && sss.length()>0 && ddd==null) vFeeKey.addElement(sss);

	//修改费用发票和折扣数据
	Vector v=modifyFee(vFeeKey);
	Vector vTemp1=(Vector)v.elementAt(0);
	Vector vTemp2=(Vector)v.elementAt(1);
	Vector vTemp3=(Vector)v.elementAt(2);
	FeeinvoiceVO feeVOs[]=new FeeinvoiceVO[vTemp1.size()];
	vTemp1.copyInto(feeVOs);
	FeeinvoiceVO discountVOs[]=new FeeinvoiceVO[vTemp2.size()];
	vTemp2.copyInto(discountVOs);
	FeeinvoiceVO specialVOs[]=new FeeinvoiceVO[vTemp3.size()];
	vTemp3.copyInto(specialVOs);

	//设置结算单表头
	SettlebillHeaderVO head=new SettlebillHeaderVO();
	head.setPk_corp(m_sUnitCode);
	head.setCaccountyear(getClientEnvironment().getAccountYear());
	head.setDsettledate(getClientEnvironment().getDate());
	head.setIbillstatus(new Integer(0));
	head.setCbilltype("27");
	head.setCoperator(getClientEnvironment().getUser().getPrimaryKey());

	//设置结算单
	SettlebillVO settlebillVO=new SettlebillVO(m_settleVOs.length);
	settlebillVO.setParentVO(head);
	settlebillVO.setChildrenVO(m_settleVOs);

	//插入结算单，更新费用发票和折扣
	long tTime=System.currentTimeMillis();
	try{
		SettleHelper.doFeeBalance(settlebillVO,m_stockVOs,feeVOs,discountVOs,specialVOs,m_sDifferenceMode,getClientEnvironment().getUser().getPrimaryKey());
		tTime=System.currentTimeMillis()-tTime;
		SCMEnv.out("费用单独结算时间："+tTime+" ms!");
	}catch(java.sql.SQLException e){
		//数据解锁
		m_bSettling = false;

		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000412")/*@res "SQL语句错误！"*/);
		SCMEnv.out(e);
		return;
	}catch(ArrayIndexOutOfBoundsException e){
		//数据解锁
		m_bSettling = false;

		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000426")/*@res "数组越界错误！"*/);
		SCMEnv.out(e);
		return;
	}catch(NullPointerException e){
		//数据解锁
		m_bSettling = false;

		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000427")/*@res "空指针错误！"*/);
		SCMEnv.out(e);
		return;
	}catch(nc.vo.pub.BusinessException e){
		//数据解锁
		m_bSettling = false;

		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,e.getMessage());
		SCMEnv.out(e);
		return;
	}catch(Exception e){
		//数据解锁
		m_bSettling = false;

		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,e.getMessage());
		SCMEnv.out(e);
		return;
	}

	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000105")/*@res "费用单独结算完毕！"*/);
	//数据解锁
	m_bSettling = false;

	return;
}
/**
 * 此处插入方法说明。
 * 功能描述:生成结算单
 * 输入参数:
 * 返回值:
 * 异常处理:
 * @param vSettle java.util.Vector
 */
private void generateSettlebill() {
	//设置表体
	SettlebillItemVO body[]=new SettlebillItemVO[m_stockVOs.length];
	Vector v=new Vector();
	for(int i=0;i<body.length;i++){
		body[i]=new SettlebillItemVO();
		body[i].setPk_corp(m_sUnitCode);
		body[i].setPk_stockcorp(m_stockVOs[i].getPk_stockcorp());
		body[i].setCstockrow(m_stockVOs[i].getCgeneralbid());
		body[i].setCstockid(m_stockVOs[i].getCgeneralhid());
		body[i].setCmangid(m_stockVOs[i].getCmangid());
		body[i].setCbaseid(m_stockVOs[i].getCbaseid());
		body[i].setNsettledisctmny(m_totalVOs[i].getNdiscountmny());

		//成本要素
		body[i].setNfactor1(m_totalVOs[i].getNfactor1());
		body[i].setNfactor2(m_totalVOs[i].getNfactor2());
		body[i].setNfactor3(m_totalVOs[i].getNfactor3());
		body[i].setNfactor4(m_totalVOs[i].getNfactor4());
		body[i].setNfactor5(m_totalVOs[i].getNfactor5());

		body[i].setNmoney(m_totalVOs[i].getNsettlemny());
		body[i].setNsettlenum(new UFDouble(0.0));
		body[i].setNgaugemny(new UFDouble(0.0));
		body[i].setNprice(new UFDouble(0.0));
		//获得入库单号
		body[i].setVbillcode(m_stockVOs[i].getVbillcode());

		v.addElement(body[i]);
	}
	//增加本次结算生成的结算单
	m_settleVOs=new SettlebillItemVO[v.size()];
	v.copyInto(m_settleVOs);

}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
private BillListPanel getBillListPanel() {
	if (m_listPanel == null) {
		try {
			m_listPanel = new BillListPanel(false);
			m_listPanel.setName("ListPanel");
			m_listPanel.loadTemplet("40040502050200000000");

			BillListData bd=m_listPanel.getBillListData();
			bd=initListDecimal(bd);
			m_listPanel.setListData(bd);

			m_listPanel.getParentListPanel().setShowThMark(true);
			m_listPanel.getChildListPanel().setShowThMark(true);
			m_listPanel.getChildListPanel().setTotalRowShow(true);
			m_listPanel.getParentListPanel().setTotalRowShow(true);

			m_listPanel.addEditListener(this);
			m_listPanel.addBodyEditListener(this);

			//表头选择监听
			m_listPanel.getHeadTable().setCellSelectionEnabled(false);
			m_listPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this) ;

			//表体选择监听
			m_listPanel.getBodyTable().setCellSelectionEnabled(false);
			m_listPanel.getBodyTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			m_listPanel.getBodyTable().getSelectionModel().addListSelectionListener(this) ;
			
			m_listPanel.getHeadTable().setSortEnabled(false);
			m_listPanel.getBodyTable().setSortEnabled(false);

			m_listPanel.updateUI();
		} catch (java.lang.Throwable ivjExc) {
			SCMEnv.out(ivjExc);
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000050")/*@res "加载模板"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000081")/*@res "模板不存在！"*/);
			return null;
		}
	}
	return m_listPanel;
}
/**
 * 功能描述:获得卡片单据控制
 *
 * 作者: 熊海情
 *
 * 创建日期:2001-6-1
 *
 * 修改：2004-5-26 晁志平 For V30
 */
private BillListPanel getBillTotalPanel() {
	if (m_totalPanel == null) {
		try {
			m_totalPanel = new BillListPanel(false);
			m_totalPanel.loadTemplet("40040502020200000000");

			BillListData bd = m_totalPanel.getBillListData();
			bd = initTotalDecimal(bd);

			//隐藏暂估金额字段 V30 调整
			BillItem item15 = bd.getHeadItem("ngaugemny");
			item15.setShow(false);

			m_totalPanel.setListData(bd);

			m_totalPanel.getParentListPanel().setShowThMark(true);
			m_totalPanel.getChildListPanel().setShowThMark(true);
			m_totalPanel.getChildListPanel().setTotalRowShow(true);
			m_totalPanel.getParentListPanel().setTotalRowShow(true);

			// 增加单据编辑监听
			m_totalPanel.addEditListener(this);
			m_totalPanel.addBodyEditListener(this);
			
			m_totalPanel.getHeadTable().setSortEnabled(false);
			m_totalPanel.getBodyTable().setSortEnabled(false);

			m_totalPanel.updateUI();
		} catch (java.lang.Throwable ivjExc) {
			SCMEnv.out(ivjExc);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000050")/*@res "加载模板"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000081")/*@res "模板不存在！"*/);
			return null;
		}
	}
	return m_totalPanel;
}

/**
 * 子类实现该方法，返回业务界面的标题。
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000000")/*@res "费用单独结算"*/;
}
/**
 * 此处插入方法说明。
 * 功能描述:初始化
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 修改：2004-09-10 袁野
 */
public void init() {
	initpara();
	//显示按钮
	m_buttons1=new ButtonObject[5];
	m_buttons1[0]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000041")/*@res "全选"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000041")/*@res "全选"*/,"全选");
	m_buttons1[1]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000042")/*@res "全消"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000042")/*@res "全消"*/,"全消");
	m_buttons1[2]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000009")/*@res "已结入库单"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000009")/*@res "已结入库单"*/,"已结入库单");
	m_buttons1[3]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000008")/*@res "费用发票"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000008")/*@res "费用发票"*/,"费用发票");
	m_buttons1[4]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000010")/*@res "确认"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000010")/*@res "确认"*/,"确认");

	m_buttons2=new ButtonObject[3];
	m_buttons2[0]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000011")/*@res "分摊"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000011")/*@res "分摊"*/,"分摊");
	m_buttons2[1]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000012")/*@res "结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050205-000012")/*@res "结算"*/,"结算");
	m_buttons2[2]=new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000038")/*@res "返回"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000038")/*@res "返回"*/,"返回");
	this.setButtons(m_buttons1);

	//显示单据
	setLayout(new java.awt.BorderLayout());
	add(getBillListPanel(),java.awt.BorderLayout.CENTER);
	getBillListPanel().setEnabled(false);

	initConfigure();

	//初始化按钮：已结入库单，费用发票正常，其它为灰
	m_nButtonState=new int[m_buttons1.length];

	for(int i=0;i<5;i++){
		m_nButtonState[i]=1;
	}
	m_nButtonState[2]=0;
	m_nButtonState[3]=0;
	changeButtonState();
}
/**
 * 此处插入方法说明。
 * 功能描述:初始化
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 修改：2004-09-10 袁野
 */
private void initConfigure() {

	//获得系统初始化参数
	try {
		//获得系统设置的暂估方式和差异转入方式
		//nc.vo.pub.para.SysInitVO initVO[] = SysInitBO_Client.querySysInit(m_sUnitCode, "PO33");
		if (initVO == null || initVO.length == 0)
			return;
		//m_sEstimateMode = initVO[0].getValue().trim();
		m_sDifferenceMode = initVO[1].getValue().trim();

	} catch (Exception e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000048")/*@res "获得系统初始化参数"*/, e.getMessage());
		SCMEnv.out(e.toString());
	}

	//库存是否启用
	//m_bICStartUp = nc.ui.sm.user.UserPowerUI.isEnabled(m_sUnitCode, "IC");

	//合同是否启用
	//m_bCTStartUp = nc.ui.sm.user.UserPowerUI.isEnabled(m_sUnitCode, "CT");

	try {
		//nc.vo.ps.factor.CostfactorVO tempVO[] = nc.ui.ps.factor.CostfactorBO_Client.queryAll(m_sUnitCode);
		if (CostfactorVOtempVO != null && CostfactorVOtempVO.length > 0) {
			//成本要素ID，是否进入成本唯一组合
			Vector vBCost = new Vector();
			Vector vTemp0 = new Vector();
			vBCost.addElement(CostfactorVOtempVO[0].getHeadVO().getBisentercost());
			vTemp0.addElement(CostfactorVOtempVO[0].getHeadVO().getCcostfactorid().trim());
			for (int i = 1; i < CostfactorVOtempVO.length; i++) {
				String s1 = CostfactorVOtempVO[i].getHeadVO().getCcostfactorid().trim();
				if (!vTemp0.contains(s1)) {
					vTemp0.addElement(s1);
					vBCost.addElement(CostfactorVOtempVO[i].getHeadVO().getBisentercost());
				}
			}

			//记录费用是否进入成本
			m_bIsentercost = new UFBoolean[vBCost.size()];
			vBCost.copyInto(m_bIsentercost);
		}
	} catch (Exception e) {
		SCMEnv.out(e);
	}

	return;
}
/**
 * 此处插入方法说明。
 * 功能描述:初始化小数位
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 修改：2004-09-10 袁野
 */
private BillListData initListDecimal(BillListData bd) {
	//获得系统初始化参数
	//int measure[] = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode, new String[]{"BD501","BD505","BD301"});

	if(measure == null || measure.length == 0){
		MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000048")/*@res "获得系统初始化参数"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000049")/*@res "无法获得系统初始化参数！"*/);
		return null;
	}

	//获得主计量小数位
	m_unitDecimal = measure[0];

	//获得采购单价小数位
	m_priceDecimal = measure[1];

	//获得采购金额小数位
	m_moneyDecimal = measure[2];

	//设置系统初始化参数
	int nMeasDecimal = m_unitDecimal;
	int nPriceDecimal = m_priceDecimal;

	//获得单据元素对应的控件,并修改控件的属性
	BillItem item1 = bd.getHeadItem("ninnum");
 	item1.setDecimalDigits(nMeasDecimal) ;

 	BillItem item2 = bd.getHeadItem("nnosettlenum");
 	item2.setDecimalDigits(nMeasDecimal) ;

 	BillItem item3 = bd.getHeadItem("nprice");
 	item3.setDecimalDigits(nPriceDecimal) ;

 	BillItem item4 = bd.getHeadItem("nnosettlemny");
 	item4.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item20 = bd.getBodyItem("nnum");
 	item20.setDecimalDigits(nMeasDecimal) ;

 	BillItem item21 = bd.getBodyItem("nmny");
 	item21.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item22 = bd.getBodyItem("nnosettlemny");
 	item22.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item23 = bd.getBodyItem("nsettlemny");
 	item23.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item24 = bd.getHeadItem("naccumsettlenum");
 	item24.setDecimalDigits(nMeasDecimal) ;

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
		ServcallVO[] scds = new ServcallVO[4];

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

		//合同是否启用
		scds[2] = new ServcallVO();
		scds[2].setBeanName("nc.itf.pu.pub.IPub");
		scds[2].setMethodName("isEnabled");
		scds[2].setParameter(new Object[] { m_sUnitCode, "CT" });
		scds[2].setParameterTypes(new Class[] { String.class, String.class });

		//成本要素
		scds[3] = new ServcallVO();
		scds[3].setBeanName("nc.itf.ps.factor.ICostfactor");
		scds[3].setMethodName("queryAllFactors");
		scds[3].setParameter(new Object[] { m_sUnitCode });
		scds[3].setParameterTypes(new Class[] { String.class });

		objs = nc.ui.scm.service.LocalCallService.callService(scds);

		measure = (int[]) objs[0];//获得系统初始化参数
		m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();//库存是否启用
		m_bCTStartUp = ((UFBoolean) objs[2]).booleanValue();//合同是否启用
		CostfactorVOtempVO = (nc.vo.ps.factor.CostfactorVO[]) objs[3];//成本要素

		//获得系统设置的暂估方式和差异转入方式
		initVO = SysInitBO_Client.querySysInit(m_sUnitCode, "PO33");

	} catch (Exception e) {
		SCMEnv.out(e);
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000052")/*@res "获取系统初始化参数出错"*/, e.getMessage());
		return;
	}
}
/**
 * 查询模版初始化
 * 作者:袁野
 * 日期：2004-09-10
 *
 */
public void initQueryModelForInvoice() {
	if (m_condClient2 == null) {
    
    m_condClient2 = new PoQueryCondition(this);
    m_condClient2.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000001")/*@res "费用单独结算查询"*/);
    m_condClient2.setTempletID(m_sUnitCode, "400405020201", getClientEnvironment().getUser().getPrimaryKey(), null, "40040502002");

		m_condClient2.setValueRef("dinvoicedate", "日历");
		m_condClient2.setValueRef("cinvclassid", "存货分类");

		UIRefPane vendorRef = new UIRefPane();
		//vendorRef.setRefType(2);//树表结构
		//vendorRef.setIsCustomDefined(true);
		//vendorRef.setRefModel(new nc.ui.ps.pub.VendorRef(m_sUnitCode,false));
		vendorRef.setRefNodeName("供应商档案");

		UIRefPane inventoryRef = new UIRefPane();
		inventoryRef.setRefType(2);//树表结构
		inventoryRef.setIsCustomDefined(true);
		inventoryRef.setRefModel(new nc.ui.ps.pub.InventoryRef(m_sUnitCode,false,false,false));

		m_condClient2.setValueRef("cvendorbaseid", vendorRef);

		m_condClient2.setValueRef("cbaseid", inventoryRef);
		m_condClient2.setValueRef("coperator", "操作员");

		m_condClient2.setDefaultValue("dinvoicedate", "dinvoicedate", getClientEnvironment().getDate().toString());

		//加载自定义项显示名称-采购发票
		nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_condClient2, m_sUnitCode, ScmConst.PO_Invoice, "po_invoice.vdef", null, 0, "po_invoice_b.vdef", null, 0);

		m_condClient2.setIsWarningWithNoInput(true);
		/*封存的基础数据能被参照*/
		m_condClient2.setSealedDataShow(true);

    //数据权限控制
    m_condClient2.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
        new String[]{nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey()}, 
        new String[]{"供应商档案","仓库档案","存货档案","存货分类"},
        new String[]{"cvendorbaseid","cwarehouseid","cbaseid","cinvclassid"},
        new int[]{0,0,0,0});

	}
}
/**
 * 查询模版初始化
 * 作者:袁野
 * 日期：2004-09-10
 *
 */
public void initQueryModelForStock() {
	if (m_condClient1 == null) {
		//初始化查询模板
    m_condClient1 = new PoQueryCondition(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000001")/*@res "费用单独结算查询"*/);
    m_condClient1.setTempletID(m_sUnitCode, "400405020201", getClientEnvironment().getUser().getPrimaryKey(), null, "40040502001");    
		
		m_corpPane = new UIRefPane();
		m_corpPane.setRefNodeName("公司目录");
		m_corpPane.addValueChangedListener(this);
		m_condClient1.setValueRef("pk_stockcorp",m_corpPane);

		m_warePane = new UIRefPane();
		m_warePane.setRefNodeName("仓库档案");
		m_condClient1.setValueRef("cwarehouseid", m_warePane);
		
		m_condClient1.setValueRef("dbilldate", "日历");
		m_condClient1.setValueRef("cinvclassid", "存货分类");

		UIRefPane vendorRef = new UIRefPane();
		//vendorRef.setRefType(2);//树表结构
		//vendorRef.setIsCustomDefined(true);
		//vendorRef.setRefModel(new nc.ui.ps.pub.VendorRef(m_sUnitCode,false));
		vendorRef.setRefNodeName("供应商档案");
		m_condClient1.setValueRef("cvendorbaseid", vendorRef);

		UIRefPane inventoryRef = new UIRefPane();
		//inventoryRef.setRefType(2);//树表结构
		//inventoryRef.setIsCustomDefined(true);
		//inventoryRef.setRefModel(new nc.ui.ps.pub.InventoryRef(m_sUnitCode,false,false,false));
		inventoryRef.setRefNodeName("存货档案");
		m_condClient1.setValueRef("cbaseid", inventoryRef);
		m_condClient1.setValueRef("coperatorid", "操作员");

		m_condClient1.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

		//加载自定义项显示名称-入库单
		nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_condClient1, m_sUnitCode, "icbill", "vuserdef", null, 0, "ic_general_b.vuserdef", null, 0);

		m_condClient1.setIsWarningWithNoInput(true);
		/*封存的基础数据能被参照*/
		m_condClient1.setSealedDataShow(true);

		UILabel label = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000225")/*@res "是否已费用结算"*/);
		label.setBounds(50,45,100,25);
		m_showBox = new UIComboBox();
		m_showBox.setBounds(150,45,60,20);
		m_showBox.addItem(null);
		m_showBox.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000226")/*@res "是"*/);
		m_showBox.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000227")/*@res "否"*/);

		m_condClient1.getUIPanelNormal().setLayout(null);

		m_condClient1.getUIPanelNormal().add(m_showBox);
		m_condClient1.getUIPanelNormal().add(label);
    
    //数据权限控制
    m_condClient1.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
        new String[]{nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey()}, 
        new String[]{"供应商档案","仓库档案","存货档案","存货分类"},
        new String[]{"cvendorbaseid","cwarehouseid","cbaseid","cinvclassid"},
        new int[]{0,2,0,0});
	}
}
/**
 * 此处插入方法说明。
 * 功能描述:初始化小数位
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
private BillListData initTotalDecimal(BillListData bd) {
	//设置系统初始化参数
	int nMeasDecimal = m_unitDecimal;
	int nPriceDecimal = m_priceDecimal;

	//获得单据元素对应的控件,并修改控件的属性
	BillItem item1 = bd.getHeadItem("nstocknum");
 	item1.setDecimalDigits(nMeasDecimal) ;

 	BillItem item111 = bd.getHeadItem("nstockaccumsettlenum");
 	item111.setDecimalDigits(nMeasDecimal) ;

	BillItem item2 = bd.getHeadItem("ninvoicenum");
 	item2.setDecimalDigits(nMeasDecimal) ;

 	BillItem item3 = bd.getHeadItem("nnosettlenum");
 	item3.setDecimalDigits(nMeasDecimal) ;

 	BillItem item4 = bd.getHeadItem("nreasonwastenum");
 	item4.setDecimalDigits(nMeasDecimal) ;

 	BillItem item5 = bd.getHeadItem("nprice");
 	item5.setDecimalDigits(nPriceDecimal) ;

 	BillItem item6 = bd.getHeadItem("nnosettlemny");
 	item6.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item7 = bd.getHeadItem("ninvoicemny");
 	item7.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item8 = bd.getHeadItem("ndiscountmny");
 	item8.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item9 = bd.getHeadItem("nfactor1");
 	item9.setDecimalDigits(m_moneyDecimal) ;
 	BillItem item10 = bd.getHeadItem("nfactor2");
 	item10.setDecimalDigits(m_moneyDecimal) ;
 	BillItem item11 = bd.getHeadItem("nfactor3");
 	item11.setDecimalDigits(m_moneyDecimal) ;
 	BillItem item12 = bd.getHeadItem("nfactor4");
 	item12.setDecimalDigits(m_moneyDecimal) ;
 	BillItem item13 = bd.getHeadItem("nfactor5");
 	item13.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item14 = bd.getHeadItem("nsettlemny");
 	item14.setDecimalDigits(m_moneyDecimal) ;

 	//BillItem item15 = bd.getHeadItem("ngaugemny");
 	//item15.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item20 = bd.getBodyItem("nnum");
 	item20.setDecimalDigits(nMeasDecimal) ;

 	BillItem item21 = bd.getBodyItem("nmny");
 	item21.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item22 = bd.getBodyItem("nnosettlemny");
 	item22.setDecimalDigits(m_moneyDecimal) ;

 	BillItem item23 = bd.getBodyItem("nsettlemny");
 	item23.setDecimalDigits(m_moneyDecimal) ;

 	return bd;
}
/**
 * 此处插入方法说明。
 * 功能描述:判断焦点是否在费用发票
 * 输入参数:无
 * 返回值:无
 * 异常处理:无
 */
private boolean isFocusListBody(BillEditEvent event) {
	String sKey=event.getKey().trim();

	if(sKey.equals("nsettlemny")) return true;

	return false;
}

/**
 * 支持数据权限处理，拆分查询条件是用户录入自定义查询条件，还是UI端系统拼接的数据权限查询条件
 * @param voaCond
 * @return{0、非权限条件VO[]；1、权限条件查询串}
 */
private ArrayList dealCondVosForPower(ConditionVO[] voaCond){
  
  //拆分用户录入VO、数据权限VO
  ArrayList listUserInputVos = new ArrayList();
  ArrayList listPowerVos = new ArrayList();
  
  int iLen = voaCond.length;
  
  for(int i=0; i<iLen; i++){
    if(voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS") && voaCond[i].getValue().trim().equalsIgnoreCase("NULL")){
      listPowerVos.add(voaCond[i]);
      i++;
      listPowerVos.add(voaCond[i]);
    }else{
      listUserInputVos.add(voaCond[i]);
    }
  }
  
  //组织返回VO
  ArrayList listRet = new ArrayList();
  
  //用户录入
  ConditionVO[] voaCondUserInput = null;
  if(listUserInputVos.size() > 0){
    voaCondUserInput = new ConditionVO[listUserInputVos.size()];
    listUserInputVos.toArray(voaCondUserInput);
  }
  listRet.add(voaCondUserInput);
  
  //数据权限VO==>查询条件串
  ConditionVO[] voaCondPower = null;
  if(listPowerVos.size() > 0){
    voaCondPower = new ConditionVO[listPowerVos.size()];
    listPowerVos.toArray(voaCondPower);
    String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
    //将非标准的字段替换掉
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "custcode", "b.pk_cubasdoc");
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "invcode", "b.pk_invbasdoc");
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "B.cbaseid");
    
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "B.cbaseid");
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "invclasscode", "pk_invbasdoc");
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "bd_invcl where 0=0  and pk_invcl", "bd_invcl, bd_invbasdoc where bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl and bd_invcl.pk_invcl");
   
    strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "storcode", "pk_stordoc");
    //
    listRet.add(strPowerWherePart);
  }else{
    listRet.add(null);
  }
  
  return listRet;
  
}

/**
 * 功能描述:加载发票
 *
 */
private void loadInvoiceData() {
	//获取发票查询条件
	ConditionVO conditionVO[] = m_condClient2.getConditionVO();

	if (!checkQueryCondition(conditionVO)) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000082")/*@res "检查查询条件"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000083")/*@res "合同未启用,查询条件不能包含合同号!"*/);
		return;
	}

  ArrayList listRet = dealCondVosForPower(conditionVO);
  conditionVO = (ConditionVO[]) listRet.get(0);
  String strDataPowerSql = (String) listRet.get(1);
	
	//查询费用发票及折扣
	try {
		long tTime = System.currentTimeMillis();
		boolean bFee = false;
		boolean bDiscount = false;
		//优化效率，三次查询发票改为一次
		java.util.ArrayList listPara = new java.util.ArrayList();
		listPara.add(m_sUnitCode);
		listPara.add(conditionVO);
		listPara.add(new UFBoolean(false));
    listPara.add(strDataPowerSql);

		java.util.ArrayList list = SettleHelper.queryAllInvoiceForFee(listPara);
		if (list == null || list.size() == 0) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000106")/*@res "既没有符合条件的费用发票,也没有符合条件的折扣发票,费用单独结算不能进行！"*/);
			//清空数据
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
			//设置按钮状态：全部为正常
			for (int i = 0; i < 5; i++) {
				m_nButtonState[i] = 0;
			}
			m_bInvoice = false;
			if (m_bStock && m_bInvoice)
				m_nButtonState[4] = 0;
			else
				m_nButtonState[4] = 1;
			changeButtonState();
			return;
		} else {
			m_feeVOs = (FeeinvoiceVO[]) list.get(0);
			m_discountVOs = (FeeinvoiceVO[]) list.get(1);
			m_specialVOs = (FeeinvoiceVO[]) list.get(2);
		}

		if (m_feeVOs == null || m_feeVOs.length == 0)
			bFee = true;

		//m_discountVOs=SettleBO_Client.queryDiscount(m_sUnitCode,conditionVO);
		//m_specialVOs=SettleBO_Client.queryInvoiceNoNum(m_sUnitCode,conditionVO);
		if ((m_discountVOs == null || m_discountVOs.length == 0) && (m_specialVOs == null || m_specialVOs.length == 0))
			bDiscount = true;

		if (bFee && bDiscount) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000106")/*@res "既没有符合条件的费用发票,也没有符合条件的折扣发票,费用单独结算不能进行！"*/);
			//清空数据
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
			//设置按钮状态：全部为正常
			for (int i = 0; i < 5; i++) {
				m_nButtonState[i] = 0;
			}
			m_bInvoice = false;
			if (m_bStock && m_bInvoice)
				m_nButtonState[4] = 0;
			else
				m_nButtonState[4] = 1;
			changeButtonState();
			return;
		}

		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("费用发票查询时间：" + tTime + " ms!");

		//加载自由项
		nc.ui.scm.pub.FreeVOParse freeParse = new nc.ui.scm.pub.FreeVOParse();		
		Vector vTemp = new Vector();
		if (m_feeVOs != null && m_feeVOs.length > 0){
			vTemp = new Vector();
			for(int i = 0; i < m_feeVOs.length; i++){
				if(m_feeVOs[i].getVfree1() != null || m_feeVOs[i].getVfree2() != null
						|| m_feeVOs[i].getVfree3() != null || m_feeVOs[i].getVfree4() != null
						|| m_feeVOs[i].getVfree5() != null){
					vTemp.addElement(m_feeVOs[i]);
				}
			}
			if(vTemp.size() > 0){
				IinvoiceVO tempVO[] = new IinvoiceVO[vTemp.size()];
				vTemp.copyInto(tempVO);
				freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
			}
		}
		
		if (m_discountVOs != null && m_discountVOs.length > 0){
			vTemp = new Vector();
			for(int i = 0; i < m_discountVOs.length; i++){
				if(m_discountVOs[i].getVfree1() != null || m_discountVOs[i].getVfree2() != null
						|| m_discountVOs[i].getVfree3() != null || m_discountVOs[i].getVfree4() != null
						|| m_discountVOs[i].getVfree5() != null){
					vTemp.addElement(m_discountVOs[i]);
				}
			}
			if(vTemp.size() > 0){
				IinvoiceVO tempVO[] = new IinvoiceVO[vTemp.size()];
				vTemp.copyInto(tempVO);
				freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
			}			
		}

		if (m_specialVOs != null && m_specialVOs.length > 0){
			vTemp = new Vector();
			for(int i = 0; i < m_specialVOs.length; i++){
				if(m_specialVOs[i].getVfree1() != null || m_specialVOs[i].getVfree2() != null
						|| m_specialVOs[i].getVfree3() != null || m_specialVOs[i].getVfree4() != null
						|| m_specialVOs[i].getVfree5() != null){
					vTemp.addElement(m_specialVOs[i]);
				}
			}
			if(vTemp.size() > 0){
				IinvoiceVO tempVO[] = new IinvoiceVO[vTemp.size()];
				vTemp.copyInto(tempVO);
				freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
			}			
		}
		
		//显示下拉框
		getBillListPanel().getBillListData().getBodyItem("fapportionmode").setWithIndex(true);
		m_comDistribute = (UIComboBox) getBillListPanel().getBillListData().getBodyItem("fapportionmode").getComponent();
		m_comDistribute.setTranslate(true);
		m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000002")/*@res "按数量"*/);
		m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000003")/*@res "按金额"*/);
		m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000004")/*@res "按重量"*/);
		m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000005")/*@res "按体积"*/);

		//费用发票和折扣发票显示
		Vector v = new Vector();
		if (m_feeVOs != null && m_feeVOs.length > 0) {
			for (int i = 0; i < m_feeVOs.length; i++)
				v.addElement(m_feeVOs[i]);
		}
		if (m_discountVOs != null && m_discountVOs.length > 0) {
			for (int i = 0; i < m_discountVOs.length; i++)
				v.addElement(m_discountVOs[i]);
		}
		if (m_specialVOs != null && m_specialVOs.length > 0) {
			for (int i = 0; i < m_specialVOs.length; i++)
				v.addElement(m_specialVOs[i]);
		}
		FeeinvoiceVO vo[] = new FeeinvoiceVO[v.size()];
		v.copyInto(vo);

		getBillListPanel().getBodyBillModel().setBodyDataVO(vo);
		getBillListPanel().getBodyBillModel().execLoadFormula();
		//上层
//		nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel().getBodyBillModel(), "ZP");
		//源头
//		nc.ui.pu.pub.PuTool.loadAncestorBillData(getBillListPanel().getBodyBillModel(), "ZP");
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillListPanel().getBodyBillModel(), "ZP");
		//
		getBillListPanel().getBodyBillModel().updateValue();
		getBillListPanel().updateUI();

		//设置按钮状态：所有按钮正常
		for (int i = 0; i < 5; i++) {
			m_nButtonState[i] = 0;
		}

		m_bInvoice = true;
		if (m_bStock && m_bInvoice)
			m_nButtonState[4] = 0;
		else
			m_nButtonState[4] = 1;

		changeButtonState();
		getBillListPanel().setEnabled(false);
	} catch (java.sql.SQLException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000412")/*@res "SQL语句错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (ArrayIndexOutOfBoundsException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000426")/*@res "数组越界错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (NullPointerException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000427")/*@res "空指针错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (Exception e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000084")/*@res "发票查询"*/, e.getMessage());
		SCMEnv.out(e);
		return;
	}
}
/**
 * 功能描述:加载已结入库单
 *
 */
private void loadStockData() {
	//获取入库单查询条件
	ConditionVO conditionVO[] = m_condClient1.getConditionVO();

	if (!checkQueryCondition(conditionVO)) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000082")/*@res "检查查询条件"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000083")/*@res "合同未启用,查询条件不能包含合同号!"*/);
		return;
	}
	//查询
	try {
		long tTime = System.currentTimeMillis();
		ConditionVO tempVO = new ConditionVO();
		tempVO.setFieldCode("bFeeSettled");
		tempVO.setOperaCode("=");
		if(m_showBox.getSelectedIndex() == 0) tempVO.setValue("0");
		if(m_showBox.getSelectedIndex() == 1) tempVO.setValue("1");
		if(m_showBox.getSelectedIndex() == 2) tempVO.setValue("2");
		Vector vTemp = new Vector();
		for(int i = 0; i < conditionVO.length; i++) vTemp.addElement(conditionVO[i]);
		vTemp.addElement(tempVO);
		conditionVO = new ConditionVO[vTemp.size()];
		vTemp.copyInto(conditionVO);
		
		m_stockVOs = SettleHelper.queryEndStock(m_sUnitCode, conditionVO);
		if (m_stockVOs == null || m_stockVOs.length == 0) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000107")/*@res "已结入库单查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000108")/*@res "没有符合条件的已结入库单！"*/);

			//清空数据
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().updateUI();
			//设置按钮状态：全部为正常
			for (int i = 0; i < 5; i++) {
				m_nButtonState[i] = 0;
			}
			m_bStock = false;
			if (m_bStock && m_bInvoice)
				m_nButtonState[4] = 0;
			else
				m_nButtonState[4] = 1;
			changeButtonState();
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("已结入库单查询时间：" + tTime + " ms!");

		//加载自由项
		nc.ui.scm.pub.FreeVOParse freeParse = new nc.ui.scm.pub.FreeVOParse();
		vTemp = new Vector();
		for(int i = 0; i < m_stockVOs.length; i++){
			if(m_stockVOs[i].getVfree1() != null || m_stockVOs[i].getVfree2() != null
					|| m_stockVOs[i].getVfree3() != null || m_stockVOs[i].getVfree4() != null
					|| m_stockVOs[i].getVfree5() != null){
				vTemp.addElement(m_stockVOs[i]);
			}
		}
		if(vTemp.size() > 0){
			StockVO tempVOs[] = new StockVO[vTemp.size()];
			vTemp.copyInto(tempVOs);
			freeParse.setFreeVO(tempVOs, "vfree", "vfree", null, "cmangid", false);
		}

		getBillListPanel().getHeadBillModel().setBodyDataVO(m_stockVOs);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		//上层
//		nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel().getHeadBillModel(), "ZP");
		//源头
//		nc.ui.pu.pub.PuTool.loadAncestorBillData(getBillListPanel().getHeadBillModel(), "ZP");
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillListPanel().getHeadBillModel(), "ZP");
		//
		getBillListPanel().getHeadBillModel().updateValue();
		getBillListPanel().updateUI();

		//设置按钮状态：所有按钮正常
		for (int i = 0; i < 5; i++) {
			m_nButtonState[i] = 0;
		}

		m_bStock = true;
		if (m_bStock && m_bInvoice)
			m_nButtonState[4] = 0;
		else
			m_nButtonState[4] = 1;

		changeButtonState();
	} catch (java.sql.SQLException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000107")/*@res "已结入库单查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000412")/*@res "SQL语句错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (ArrayIndexOutOfBoundsException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000107")/*@res "已结入库单查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000426")/*@res "数组越界错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (NullPointerException e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000107")/*@res "已结入库单查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000427")/*@res "空指针错误！"*/);
		SCMEnv.out(e);
		return;
	} catch (Exception e) {
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000107")/*@res "已结入库单查询"*/, e.getMessage());
		SCMEnv.out(e);
		return;
	}
}
/**
 * 此处插入方法说明。
 * 功能描述:成功结算后，根据结算单修改费用发票和折扣数据
 * 输入参数:
 * 返回值:需要更新的费用发票VO和折扣VO,一般发票VO
 * 异常处理:
 */
private Vector modifyFee(Vector vFeeKey) {
	Vector v=new Vector();
	Vector v1=new Vector();
	Vector v2=new Vector();
	Vector v3=new Vector();

	for(int i=0;i<vFeeKey.size();i++){
		String s=(String)vFeeKey.elementAt(i);
		double nSettlemny=0.0;
		String s0="";
		for(int j=0;j<m_settleVOs.length;j++){
			String ss=m_settleVOs[j].getCinvoice_bid();
			if(ss==null || ss.length()==0) continue;
			ss=ss.trim();
			if(s.equals(ss)){
				nSettlemny+=m_settleVOs[j].getNmoney().doubleValue();
				s0=m_settleVOs[j].getCinvoiceid();
			}
		}

		//查询对应的费用发票和折扣
		int k1=-1;
		int k2=-1;
		int k3=-1;
		if(m_feeVOs!=null && m_feeVOs.length>0){
			for(int j=0;j<m_feeVOs.length;j++){
				String s1=m_feeVOs[j].getCinvoice_bid().trim();
				String s2=m_feeVOs[j].getCinvoiceid().trim();
				if(s.equals(s1) && s0.equals(s2)){
					k1=j;
					break;
				}
			}
		}
		if(m_discountVOs!=null && m_discountVOs.length>0){
			for(int j=0;j<m_discountVOs.length;j++){
				String s1=m_discountVOs[j].getCinvoice_bid().trim();
				String s2=m_discountVOs[j].getCinvoiceid().trim();
				if(s.equals(s1) && s0.equals(s2)){
					k2=j;
					break;
				}
			}
		}
		if(m_specialVOs!=null && m_specialVOs.length>0){
			for(int j=0;j<m_specialVOs.length;j++){
				String s1=m_specialVOs[j].getCinvoice_bid().trim();
				String s2=m_specialVOs[j].getCinvoiceid().trim();
				if(s.equals(s1) && s0.equals(s2)){
					k3=j;
					break;
				}
			}
		}

		if(k3>=0){
			//修改一般发票的累计结算数量和累计结算金额
			double d=m_specialVOs[k3].getNaccumsettlemny().doubleValue();
			d+=nSettlemny;
			m_specialVOs[k3].setNaccumsettlemny(new UFDouble(d));

			//需要更新的一般发票VO返回
			v3.addElement(m_specialVOs[k3]);
		}else if(k2>=0){
			//修改折扣的累计结算数量和累计结算金额
			double d=m_discountVOs[k2].getNaccumsettlemny().doubleValue();
			d+=nSettlemny;
			m_discountVOs[k2].setNaccumsettlemny(new UFDouble(d));

			//需要更新的折扣VO返回
			v2.addElement(m_discountVOs[k2]);
		}else if(k1>=0){
			//修改费用发票的累计结算数量和累计结算金额
			double d=m_feeVOs[k1].getNaccumsettlemny().doubleValue();
			d+=nSettlemny;
			m_feeVOs[k1].setNaccumsettlemny(new UFDouble(d));

			//需要更新的费用发票返回
			v1.addElement(m_feeVOs[k1]);
		}else{
			MessageDialog.showErrorDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000088")/*@res "修改费用发票和折扣数据"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000089")/*@res "数据错误！"*/);
			return null;
		}
	}

	v.addElement(v1);
	v.addElement(v2);
	v.addElement(v3);
	return v;
}
/**
 * 此处插入方法说明。
 * 功能描述:费用单独结算
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
public void onBalance() {
	generateSettlebill();
	if(m_settleVOs==null || m_settleVOs.length==0){
//		MessageDialog.showHintDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000103")/*@res "费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000093")/*@res "结算失败！"*/);
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000093")/*@res "结算失败！"*/);
		return;
	}

	//计算结算金额
	//for(int i=0;i<m_totalVOs.length;i++){
		//double sum=0.0;
		////if(m_totalVOs[i].getNsettlemny()!=null) sum=m_totalVOs[i].getNsettlemny().doubleValue();
		//if(m_bIsentercost!=null && m_bIsentercost.length>0){
			//for(int j=0;j<m_bIsentercost.length;j++){
				//if(j==0 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor1().doubleValue();
				//if(j==1 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor2().doubleValue();
				//if(j==2 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor3().doubleValue();
				//if(j==3 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor4().doubleValue();
				//if(j==4 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor5().doubleValue();
			//}
		//}
		//if(m_totalVOs[i].getNdiscountmny()!=null) sum+=m_totalVOs[i].getNdiscountmny().doubleValue();
		//m_totalVOs[i].setNsettlemny(new UFDouble(getRoundDouble(sum)));
	//}

	//更新界面
	getBillTotalPanel().getHeadBillModel().setBodyDataVO(m_totalVOs);
	getBillTotalPanel().getHeadBillModel().execLoadFormula();
	getBillTotalPanel().getHeadBillModel().updateValue();
	getBillTotalPanel().updateUI();

	//费用发票和折扣和一般发票生成结算单
	Vector v=new Vector();
	for(int i=0;i<m_settleVOs.length;i++) v.addElement(m_settleVOs[i]);
	if(m_feeVOs!=null && m_feeVOs.length>0){
		SettlebillItemVO body[]=new SettlebillItemVO[m_feeVOs.length];
		for(int i=0;i<body.length;i++){
			body[i]=new SettlebillItemVO();
			body[i].setPk_corp(m_sUnitCode);
			body[i].setCinvoice_bid(m_feeVOs[i].getCinvoice_bid());
			body[i].setCinvoiceid(m_feeVOs[i].getCinvoiceid());
			body[i].setCmangid(m_feeVOs[i].getCmangid());
			body[i].setCbaseid(m_feeVOs[i].getCbaseid());
			body[i].setNmoney(m_feeVOs[i].getNsettlemny());
			v.addElement(body[i]);
		}
	}
	if(m_discountVOs!=null && m_discountVOs.length>0){
		SettlebillItemVO body1[]=new SettlebillItemVO[m_discountVOs.length];
		for(int i=0;i<body1.length;i++){
			body1[i]=new SettlebillItemVO();
			body1[i].setPk_corp(m_sUnitCode);
			body1[i].setCinvoice_bid(m_discountVOs[i].getCinvoice_bid());
			body1[i].setCinvoiceid(m_discountVOs[i].getCinvoiceid());
			body1[i].setCmangid(m_discountVOs[i].getCmangid());
			body1[i].setCbaseid(m_discountVOs[i].getCbaseid());
			body1[i].setNmoney(m_discountVOs[i].getNsettlemny());
			v.addElement(body1[i]);
		}
	}
	if(m_specialVOs!=null && m_specialVOs.length>0){
		SettlebillItemVO body2[]=new SettlebillItemVO[m_specialVOs.length];
		for(int i=0;i<body2.length;i++){
			body2[i]=new SettlebillItemVO();
			body2[i].setPk_corp(m_sUnitCode);
			body2[i].setCinvoice_bid(m_specialVOs[i].getCinvoice_bid());
			body2[i].setCinvoiceid(m_specialVOs[i].getCinvoiceid());
			body2[i].setCmangid(m_specialVOs[i].getCmangid());
			body2[i].setCbaseid(m_specialVOs[i].getCbaseid());
			body2[i].setNmoney(m_specialVOs[i].getNsettlemny());
			v.addElement(body2[i]);
		}
	}
	m_settleVOs=new SettlebillItemVO[v.size()];
	v.copyInto(m_settleVOs);

	//修改费用发票和折扣
	doModification();

	//设置按钮状态：除输出外其它按钮为灰
	for(int i=0;i<3;i++){
		m_nButtonState[i]=1;
	}
	changeButtonState();

	//结算完毕，不管成功或失败，均返回到主界面
	refreshSettleUI();
}
/**
 * 子类实现该方法，响应按钮事件。
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	if (bo == m_buttons1[0]){
		onSelectAll();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000033")/*@res "全选成功"*/);				
	}
	else if (bo == m_buttons1[1]){
		onSelectNo();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000034")/*@res "全消成功"*/);				
	}
	else if (bo == m_buttons1[2]){
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH046"));						
		onStockQuery();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH009"));						
	}
	else if (bo == m_buttons1[3]){
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH046"));						
		onInvoiceQuery();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH009"));						
	}
	else if (bo == m_buttons1[4])
		onConfirm();
	else if (bo == m_buttons2[0])
		onDistribute();
	else if (bo == m_buttons2[1])
		onBalance();
	else if (bo == m_buttons2[2]){
		onReturn();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000053")/*@res "返回成功"*/);				
	}
}
/**
 * 此处插入方法说明。
 * 功能描述:退出系统
 * 输入参数:
 * 返回值:
 * 异常处理:
 * @return boolean
 */
public boolean onClosing() {
	if(m_bSettling){
		//数据已加锁
		int nReturn = MessageDialog.showYesNoCancelDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000006")/*@res "退出费用单独结算"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000007")/*@res "结算未完毕，是否退出费用单独结算？"*/);
		if(nReturn != MessageDialog.ID_YES){
			return false;
		}else{
			m_bSettling = false;
		}
	}
	return true;
}
/**
 * 功能:确认
 *
 * 作者：熊海情
 *
 * 创建：2001-05-15
 *
 * 修改：2004-05-26 晁志平 FOR V30
 *
 * 1）、未结算金额=入库金额-累计结算金额
 * 2）、暂估金额隐藏
 * 3）、本次结算入库数量＝入库未结算数量
 * 4）、结算金额=未结算金额
 *
 */
public void onConfirm() {
	if(!doClassification()) return;
	if(!doFeeClassification()) return;

	//对数据加锁
	m_bSettling = true;

	//入库单写入结算汇总表
	int nStock=m_stockVOs.length;
	m_totalVOs=new SettletotalVO[nStock];
	for(int i=0;i<nStock;i++) m_totalVOs[i]=new SettletotalVO();
	UFDouble dNoSettNum = null;
	for(int i=0;i<nStock;i++){
		m_totalVOs[i].setCbilltype(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPT4004050204-000002")/*@res "入库单"*/);
		m_totalVOs[i].setCbill_bid(m_stockVOs[i].getCgeneralbid());
		m_totalVOs[i].setCbillid(m_stockVOs[i].getCgeneralhid());
		m_totalVOs[i].setVbillcode(m_stockVOs[i].getVbillcode());
		m_totalVOs[i].setCmangid(m_stockVOs[i].getCmangid());
		m_totalVOs[i].setCbaseid(m_stockVOs[i].getCbaseid());
		m_totalVOs[i].setVbatchcode(m_stockVOs[i].getVbatchcode());
		m_totalVOs[i].setVfree(m_stockVOs[i].getVfree());
		//收货公司
		m_totalVOs[i].setPk_stockcorp(m_stockVOs[i].getPk_stockcorp());
		//入库未结算数量,金额,单价
		dNoSettNum = m_stockVOs[i].getNinnum().sub(m_stockVOs[i].getNaccumsettlenum());
		m_totalVOs[i].setNnosettlenum(dNoSettNum);
		m_totalVOs[i].setNnosettlemny(m_stockVOs[i].getNmoney().sub(m_stockVOs[i].getNaccumsettlemny()));
		m_totalVOs[i].setNprice(m_stockVOs[i].getNprice());
		m_totalVOs[i].setNstockaccumsettlenum(m_stockVOs[i].getNaccumsettlenum());
		//本次结算入库数量＝入库未结算数量
		m_totalVOs[i].setNstocknum(dNoSettNum);

		/*
		//本次结算暂估金额 (V30 界面隐藏)
		m_totalVOs[i].setNgaugemny(new UFDouble(0.0));
		*/

		//成本要素1~5分摊初始为0，折扣初始为0
		m_totalVOs[i].setNfactor1(new UFDouble(0.0));
		m_totalVOs[i].setNfactor2(new UFDouble(0.0));
		m_totalVOs[i].setNfactor3(new UFDouble(0.0));
		m_totalVOs[i].setNfactor4(new UFDouble(0.0));
		m_totalVOs[i].setNfactor5(new UFDouble(0.0));
		m_totalVOs[i].setNdiscountmny(new UFDouble(0.0));
		//结算金额=未结算金额
//		m_totalVOs[i].setNsettlemny(m_totalVOs[i].getNnosettlemny());
		m_totalVOs[i].setNsettlemny(new UFDouble(0.0));
	}

	//费用发票和折扣发票显示
	Vector v=new Vector();
	if(m_feeVOs!=null && m_feeVOs.length>0){
		for(int i=0;i<m_feeVOs.length;i++) v.addElement(m_feeVOs[i]);
	}
	if(m_discountVOs!=null && m_discountVOs.length>0){
		for(int i=0;i<m_discountVOs.length;i++) v.addElement(m_discountVOs[i]);
	}
	if(m_specialVOs!=null && m_specialVOs.length>0){
		for(int i=0;i<m_specialVOs.length;i++) v.addElement(m_specialVOs[i]);
	}
	FeeinvoiceVO vo[]=new FeeinvoiceVO[v.size()];
	v.copyInto(vo);

	//显示单据
	getBillListPanel().setVisible(false);
	this.setButtons(m_buttons2);
	m_nButtonState=new int[m_buttons2.length];

	setLayout(new java.awt.BorderLayout());
	add(getBillTotalPanel(),java.awt.BorderLayout.CENTER);
	getBillTotalPanel().setEnabled(false);

	changeFactorShowName();

	//显示下拉框
	getBillTotalPanel().getBillListData().getBodyItem("fapportionmode").setWithIndex(true);
	m_comDistribute=(UIComboBox)getBillTotalPanel().getBillListData().getBodyItem("fapportionmode").getComponent();
	m_comDistribute.setTranslate(true);
	m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000002")/*@res "按数量"*/);
	m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000003")/*@res "按金额"*/);
	m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000004")/*@res "按重量"*/);
	m_comDistribute.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050205","UPP4004050205-000005")/*@res "按体积"*/);

	getBillTotalPanel().getHeadBillModel().setBodyDataVO(m_totalVOs);
	getBillTotalPanel().getHeadBillModel().execLoadFormula();
	getBillTotalPanel().getHeadBillModel().updateValue();
	getBillTotalPanel().getBodyBillModel().setBodyDataVO(vo);
	getBillTotalPanel().getBodyBillModel().execLoadFormula();
	getBillTotalPanel().getBodyBillModel().updateValue();
	getBillTotalPanel().updateUI();

	//设置按钮状态：除结算外所有按钮为正常
	for(int i=0;i<3;i++){
		m_nButtonState[i]=0;
	}
	m_nButtonState[1]=1;
	changeButtonState();

	getBillTotalPanel().setEnabled(true);
	setPartEditable();
}
/**
 * 此处插入方法说明。
 * 功能描述:分摊成本要素到已结入库单
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
public void onDistribute() {
	long tTime=System.currentTimeMillis();
	distributeFee();
	distributeDiscount();
	distributeSpecial();
	tTime=System.currentTimeMillis()-tTime;
	SCMEnv.out("费用分摊时间："+tTime+" ms!");

	//计算结算金额
	for(int i=0;i<m_totalVOs.length;i++){
		double sum = 0.0;
		if(m_totalVOs[i].getNsettlemny()!=null) sum=m_totalVOs[i].getNsettlemny().doubleValue();
		if(m_bIsentercost!=null && m_bIsentercost.length>0){
			for(int j=0;j<m_bIsentercost.length;j++){
				if(j==0 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor1().doubleValue();
				if(j==1 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor2().doubleValue();
				if(j==2 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor3().doubleValue();
				if(j==3 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor4().doubleValue();
				if(j==4 && m_bIsentercost[j].booleanValue()) sum+=m_totalVOs[i].getNfactor5().doubleValue();
			}
		}
		if(m_totalVOs[i].getNdiscountmny()!=null) sum+=m_totalVOs[i].getNdiscountmny().doubleValue();
		m_totalVOs[i].setNsettlemny(new UFDouble(PuTool.getRoundDouble(m_moneyDecimal,sum)));
	}

	//更新界面
	getBillTotalPanel().getHeadBillModel().setBodyDataVO(m_totalVOs);
	getBillTotalPanel().getHeadBillModel().execLoadFormula();
	getBillTotalPanel().getHeadBillModel().updateValue();
	getBillTotalPanel().updateUI();

	//设置按钮状态：除分摊，返回外所有按钮为正常
	for(int i=0;i<3;i++){
		m_nButtonState[i]=0;
	}
	m_nButtonState[0]=1;
	m_nButtonState[2]=1;
	changeButtonState();

	getBillTotalPanel().setEnabled(false);
}
/**
 * 功能描述:发票查询
 * 修改：2004-09-10 袁野
 */
public void onInvoiceQuery() {

	initQueryModelForInvoice();

	m_condClient2.hideNormal();
  m_condClient2.hideCorp();
	m_condClient2.showModal();

	if (m_condClient2.isCloseOK()) {
		loadInvoiceData();
	}

	setBtnsState();
}
/**
 * 此处插入方法说明。
 * 功能描述:返回
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
public void onReturn() {
	showHintMessage("");
	//数据解锁
	m_bSettling = false;

	//缓存恢复操作，恢复入库单，费用发票，折扣为确认前的数据
	Vector v0=new Vector();
	for(int i=0;i<m_mStockVOs.length;i++) v0.addElement(m_mStockVOs[i]);
	m_stockVOs=new StockVO[v0.size()];
	v0.copyInto(m_stockVOs);

	Vector v=new Vector();
	Vector v00=new Vector();
	for(int i=0;i<m_mFeeVOs.length;i++){
		v00.addElement(m_mFeeVOs[i]);
		v.addElement(m_mFeeVOs[i]);
	}
	m_feeVOs=new FeeinvoiceVO[v00.size()];
	v00.copyInto(m_feeVOs);

	Vector v000=new Vector();
	for(int i=0;i<m_mDiscountVOs.length;i++){
		v000.addElement(m_mDiscountVOs[i]);
		v.addElement(m_mDiscountVOs[i]);
	}
	m_discountVOs=new FeeinvoiceVO[v000.size()];
	v000.copyInto(m_discountVOs);

	Vector v0000=new Vector();
	for(int i=0;i<m_mSpecialVOs.length;i++){
		v0000.addElement(m_mSpecialVOs[i]);
		v.addElement(m_mSpecialVOs[i]);
	}
	m_specialVOs=new FeeinvoiceVO[v0000.size()];
	v0000.copyInto(m_specialVOs);

	this.setButtons(m_buttons1);
	m_nButtonState=new int[m_buttons1.length];
	this.remove(getBillTotalPanel());
	getBillListPanel().setVisible(true);

	//m_bStock=false;
	//m_bInvoice=false;
}
/**
 * 此处插入方法说明。
 * 功能描述:全选
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
public void onSelectAll() {
	int nRow=getBillListPanel().getHeadBillModel().getRowCount();
	for(int i=0;i<nRow;i++) getBillListPanel().getHeadBillModel().setRowState(i,BillModel.SELECTED);

	nRow=getBillListPanel().getBodyBillModel().getRowCount();
	for(int i=0;i<nRow;i++) getBillListPanel().getBodyBillModel().setRowState(i,BillModel.SELECTED);

	getBillListPanel().updateUI();

	setBtnsState();
}
/**
 * 此处插入方法说明。
 * 功能描述:全消
 * 输入参数:
 * 返回值:
 * 异常处理:
 *
 */
public void onSelectNo() {
	int nRow=getBillListPanel().getHeadBillModel().getRowCount();
	for(int i=0;i<nRow;i++) getBillListPanel().getHeadBillModel().setRowState(i,BillModel.NORMAL);

	nRow=getBillListPanel().getBodyBillModel().getRowCount();
	for(int i=0;i<nRow;i++) getBillListPanel().getBodyBillModel().setRowState(i,BillModel.NORMAL);

	getBillListPanel().updateUI();

	setBtnsState();
}
/**
 * 功能描述:已结入库单查询
 * 修改：2004-09-10 袁野
 */
public void onStockQuery() {
	if (!m_bICStartUp) {
		//库存未启用
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040502","UPP40040502-000109")/*@res "库存未启用，不能进行费用单独结算！"*/);
		return;
	}

	initQueryModelForStock();
//	m_condClient1.hideNormal();
  m_condClient1.hideCorp();
  m_condClient1.hideUnitButton();
	m_condClient1.showModal();

	if (m_condClient1.isCloseOK()) {
		loadStockData();
	}

	setBtnsState();
}
/**
 * 参数： 刷新发票
 * 返回：
 * 例外：
 * 日期：(2002-12-6 10:55:38)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void refreshInvoice() {
	loadInvoiceData();
}
/**
 * 参数： 结算完毕，返回主界面，并刷新入库单/发票查询结果
 * 返回：
 * 例外：
 * 日期：(2002-12-6 10:54:01)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void refreshSettleUI() {
	//数据解锁
	m_bSettling = false;

	//缓存恢复操作
	m_stockVOs = null;
	m_totalVOs = null;
	m_feeVOs = null;
	m_discountVOs = null;
	m_settleVOs = null;
	m_specialVOs = null;

	m_mStockVOs = null;
	m_mSpecialVOs = null;
	m_mFeeVOs = null;
	m_mDiscountVOs = null;

	this.setButtons(m_buttons1);
	m_nButtonState=new int[m_buttons1.length];
	this.remove(getBillTotalPanel());

	getBillListPanel().setVisible(true);

	m_bStock=false;
	m_bInvoice=false;

	//刷新入库单
	refreshStock();

	//刷新发票
	refreshInvoice();
	//
	setBtnsState();
}
/**
 * 参数： 刷新入库单
 * 返回：
 * 例外：
 * 日期：(2002-12-6 10:55:38)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void refreshStock() {
	loadStockData();
}
/**
 * 功能描述:设置按钮状态
 *
 *	全选：	 	m_nButtonState[0]
 *	全消：	 	m_nButtonState[1]
 *	已结入库单：	m_nButtonState[2]
 *	费用发票：	m_nButtonState[3]
 *	确认：	 	m_nButtonState[4]
 */
private void setBtnsState() {
	//
	int iHCnt = getBillListPanel().getHeadBillModel().getRowCount();
	int iBCnt = getBillListPanel().getBodyBillModel().getRowCount();
	int iHSelCnt = 0;
	for (int i = 0; i < iHCnt; i++) {
		if (BillModel.SELECTED == getBillListPanel().getHeadBillModel().getRowState(i))
			iHSelCnt += 1;
	}
	//iHSelCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
	int iBSelCnt = 0;
	for (int i = 0; i < iBCnt; i++) {
		if (BillModel.SELECTED == getBillListPanel().getBodyBillModel().getRowState(i))
			iBSelCnt += 1;
	}
	//iBSelCnt = getBillListPanel().getBodyTable().getSelectedRowCount();

	/*没数据*/

	if (iHCnt <= 0 && iBCnt <= 0) {
		m_nButtonState[0] = 1;
		m_nButtonState[1] = 1;
		m_nButtonState[2] = 0;
		m_nButtonState[3] = 0;
		m_nButtonState[4] = 1;
		changeButtonState();
		return;
	}

	/*有数据*/

	/*全选*/
	if (iHCnt == iHSelCnt && iBCnt == iBSelCnt) {
		m_nButtonState[0] = 1;
	} else {
		m_nButtonState[0] = 0;
	}
	/*全消*/
	if (iHSelCnt > 0 || iBSelCnt > 0) {
		m_nButtonState[1] = 0;
	} else {
		m_nButtonState[1] = 1;
	}
	/*已结入库单*/
	m_nButtonState[2] = 0;
	/*费用发票*/
	m_nButtonState[3] = 0;
	/*确认*/
	if (iHSelCnt > 0 && iBSelCnt > 0) {
		m_nButtonState[4] = 0;
	} else {
		m_nButtonState[4] = 1;
	}
	//
	changeButtonState();
	return;
}
/**
 *  功能描述:设置入库单不可编辑
 *			 设置费用发票：本次结算金额可编辑
 *
 */
private void setPartEditable() {
	//设置汇总字段可编辑性
	BillItem[] items = getBillTotalPanel().getHeadBillModel().getBodyItems();
	int iLen = 0;
	if (items != null) {
		iLen = items.length;
		for (int i = 0; i < iLen; i++) {
			if (items[i] == null || !items[i].isShow())
				continue;
			items[i].setEnabled(false);
		}
	}
	//设置费用发票，折扣部分字段可编辑性
	items = getBillTotalPanel().getBodyBillModel().getBodyItems();
	if (items != null) {
		iLen = items.length;
		String sKey = null;
		boolean bVal = false;
		for (int i = 0; i < iLen; i++) {
			if (items[i] == null || !items[i].isShow())
				continue;
			sKey = items[i].getKey().trim();
			bVal = "nsettlemny".equalsIgnoreCase(sKey);
			items[i].setEnabled(bVal);
		}
	}
	//
	UIRefPane nRefPanel = (UIRefPane) getBillTotalPanel().getBodyItem("nsettlemny").getComponent();
	UITextField nMoneyUI = (UITextField) nRefPanel.getUITextField();
	nMoneyUI.setMaxLength(16);
}
/**
 * 此处插入方法说明。
 * 功能描述:处理列表选择
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:2002/09/26
 */
public void valueChanged(javax.swing.event.ListSelectionEvent event){
	if((ListSelectionModel)event.getSource() == getBillListPanel().getHeadTable().getSelectionModel()){
		//入库单表（表头）
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();

		//表头所有行恢复正常（不选择）
		for(int i = 0; i < nRow; i++){
			getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
		}

		//获得表头选择行数
		int nSelected[] = getBillListPanel().getHeadTable().getSelectedRows();
		if(nSelected != null && nSelected.length > 0){
			for(int i = 0; i < nSelected.length; i++){
				int j = nSelected[i];
				getBillListPanel().getHeadBillModel().setRowState(j,BillModel.SELECTED);
			}
		}

	}else if((ListSelectionModel)event.getSource() == getBillListPanel().getBodyTable().getSelectionModel()){
		//发票表（表体）
		int nRow = getBillListPanel().getBodyBillModel().getRowCount();

		//表体所有行恢复正常（不选择）
		for(int i = 0; i < nRow; i++){
			getBillListPanel().getBodyBillModel().setRowState(i, BillModel.NORMAL);
		}

		//获得表体选择行数
		int nSelected[] = getBillListPanel().getBodyTable().getSelectedRows();
		if(nSelected != null && nSelected.length > 0){
			for(int i = 0; i < nSelected.length; i++){
				int j = nSelected[i];
				getBillListPanel().getBodyBillModel().setRowState(j,BillModel.SELECTED);
			}
		}
	}

	setBtnsState();
}

public void valueChanged(ValueChangedEvent event) {
	// TODO 自动生成方法存根
	String pk_corp = m_corpPane.getRefPK();
	if(pk_corp != null){
		m_warePane.setPk_corp(pk_corp);
	}else{
		m_warePane.setPk_corp(m_sUnitCode);
	}
}
}