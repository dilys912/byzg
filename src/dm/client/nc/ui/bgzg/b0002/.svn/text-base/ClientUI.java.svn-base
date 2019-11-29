/**
 * 
 */
package nc.ui.bgzg.b0002;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.ibm.db2.jcc.b.sc;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.bgzg.pub.IBgzgButton;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * @author Administrator
 *
 */
public class ClientUI extends BillCardUI implements BillCardBeforeEditListener{

	/**
	 * 
	 */
	public ClientUI() {
		// TODO Auto-generated constructor stub
		super();
	}
	String pk_doc=null;
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		// TODO Auto-generated constructor stub
	}	
    protected CardEventHandler createEventHandler() {
		return new ClientEH(this, getUIControl());
	}
	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	/* (non-Javadoc)
	 * @see nc.ui.trade.base.AbstractBillUI#setDefaultData()
	 */
	@Override
	public void setDefaultData() throws Exception {
	 getBillCardPanel().setHeadItem("xxsj", _getServerTime().getTime());
	 getBillCardPanel().setHeadItem("addr",_getCorp().getPostaddr());
	 getBillCardPanel().setHeadItem("phone1",_getCorp().getPhone1());
	 
	 try {
			setBillOperate(IBillOperate.OP_EDIT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	}
	protected void initPrivateButton() {
		// TODO 自动生成方法存根
		 ButtonVO print = createButtonVO(IBgzgButton.print, "打印", "打印"); 
		ButtonVO tm = createButtonVO(IBgzgButton.hgztm, "生成条码", "生成条码"); 
		 addPrivateButton(print);
	 addPrivateButton(tm);
		
	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}


	@SuppressWarnings("deprecation")
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		try {
		super.afterEdit(e);
		if (e.getKey().equals("sjjgnum")) {
			String sjjgnum =  getBillCardPanel().getHeadItem("sjjgnum").getValue();
			int jj=Integer.parseInt(sjjgnum);
			if(jj<2){
				throw new BusiBeanException("打印时间间隔小于2,请重新填写!Print time interval is less than 2, please fill in again!");	
			}
		    
		}
		//供应商统一条码---最佳使用期
/*		else if(e.getKey().equals("xzjh")){
//			String invcode = getBillCardPanel().getHeadItem("invcode").getValue();
			String pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
		pk_doc=pk_invbasdoc;
			if(pk_invbasdoc==""||pk_invbasdoc==null){
				showErrorMessage("存货编码不可为空，请输入后再试！");
				return;
			}
			String scrq = getBillCardPanel().getHeadItem("scrq").getValue();
			if(scrq==""||scrq==null){
				showErrorMessage("生产日期不可为空，请输入后再试！");
				return;
			}
			String scbc = getBillCardPanel().getHeadItem("scbc").getValue();
			if(scbc==""||scbc==null){
				showErrorMessage("生产班次不可为空，请输入后再试！");
				return;
			}
			String zjsyq = getBillCardPanel().getHeadItem("zjsyq").getValue();
			int zjmonths = 0;
			if(zjsyq==""||zjsyq==null){
				showErrorMessage("最佳使用期范围不可为空，请输入后再试！");
				return;
			}else{
				zjmonths = Integer.parseInt(zjsyq);
			}
			String zjsyqok = getZJSYQ(scrq,zjmonths);//生成最佳使用日期
			String jh = getBillCardPanel().getHeadItem("jh").getValue();
			if(jh==""||jh==null){
				showErrorMessage("机号不可为空，请输入后再试！");
				return;
			}
			String xzjh = getBillCardPanel().getHeadItem("xzjh").getValue();
			if(xzjh==""||xzjh==null){
				showErrorMessage("新增机号不可为空，请输入后再试！");
				return;
			}
			String sapinvcode = getInvSAPcode(pk_invbasdoc);//sap物料编码
			if(sapinvcode==""||sapinvcode==null){
				showErrorMessage("存货在基本档案中没有维护SAP物料编码，请维护！");
				return;
			}
			String corpcode = getCorpGCcode();//工厂编码
			if(corpcode==""||corpcode==null){
				showErrorMessage("本公司："+getClientEnvironment().getCorporation().getPKFieldName()+"在公司目录中没有维护工厂编码，请维护！");
				return;
			}
			//组装生产日期字段
			String scdate = getSCDate(scrq);
			String zjsyrq = getSCDate(zjsyqok);//最佳使用日期
			//组装供应商统一条码
			String gystytm = sapinvcode+scdate+scbc+corpcode+zjsyrq+jh+xzjh+"001";
			getBillCardPanel().setHeadItem("gystytm", gystytm);
		}*/
		//2015-03-11 宝翼制罐打印托盘起始顺序号要变为30位,校验是否全为数字,如果输入大于25位则提示必须为30位;后三位加上打印次数不能大于1000－－wkf－－start--
//		else if (e.getKey().equals("serialnum")){
//			Object ser = getBillCardPanel().getHeadItem("serialnum").getValueObject();
//			if(!ser.equals(null)){
//				String seri = ser.toString();
//				if(isNumber(seri)){
//					String seri3 = seri.substring(seri.length()-3,seri.length());
//					int last3 = Integer.parseInt(seri3);
//					String prin = getBillCardPanel().getHeadItem("printnum").getValueObject().toString();
//					int printnum = Integer.parseInt(prin);
//					if(last3+printnum<1000){
//						if(seri.length()>25&&seri.length()!=30){
//							showErrorMessage("托盘起始顺序号必须为30位!");
//						}
//					}else{
//						showErrorMessage("托盘起始顺序号后三位加打印次数不能大于1000！");
//					}
//				}else{
//					showErrorMessage("请输入正确的托盘起始顺序号(整数)!");
//				}
//				
//			}
//		}
		//2015-03-11 宝翼制罐打印托盘起始顺序号要变为30位,校验是否全为数字,如果输入大于25位则提示必须为30位－－wkf－－end--
		 getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(e.getKey()));
		} catch (Exception e1) {
			e1.printStackTrace();
			
			
		}
	}
	//wkf --2015-03-11 判断字符串是否为数字
	public boolean isNumber(String str){
		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public  ButtonVO createButtonVO(int id, String code, String name) {
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(code);
		btn.setHintStr(name);
		btn.setBtnCode(name);
		btn.setBtnChinaName(code);
		return btn;
	}

	public boolean beforeEdit(BillItemEvent e) {
		if (e.getItem().getKey().equals("jyy")) {
			UIRefPane ref = (UIRefPane) e.getItem().getComponent();//add bd_psndoc 2014-03-18
			ref.getRefModel().addWherePart(" and bd_psndoc.pk_corp = '" + _getCorp().getPrimaryKey() + "' ");
		}
//		if(e.getItem().getKey().equals("scbc")){
//			UIRefPane ref = (UIRefPane) e.getItem().getComponent();
//			ref.getRefModel().addWherePart(" and pk_corp = '" + _getCorp().getPrimaryKey() + "' ");
//		}
		return false;
	}
	//获得存货基本档案sap物料编码
	public String getInvSAPcode(String pk_invmandoc) {
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		StringBuffer sapsql = new StringBuffer();
		sapsql.append(" select def8 ") 
		.append("   from bd_invbasdoc ") 
		.append("  where nvl(dr, 0) = 0 ") 
		.append("    and pk_invbasdoc = '"+pk_invmandoc+"' ") ;


		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sapinvcode = "";
		HashMap al;
		try {
			al = (HashMap)sessionManager.executeQuery(sapsql.toString(),  new MapProcessor());
			if(al!=null||al.size()>0)
			{
				sapinvcode =al.get("def8").toString();
			}else{
				sapinvcode = "";
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return sapinvcode;
	}
	public String getCorpGCcode() {
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		StringBuffer corpsql = new StringBuffer();
		corpsql.append(" select def10 ") 
		.append("   from bd_corp ") 
		.append("  where nvl(dr, 0) = 0 ") 
		.append("    and pk_corp = '"+pk_corp+"' ") ;
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String corpcode = "";
		HashMap al;
		try {
			al = (HashMap)sessionManager.executeQuery(corpsql.toString(),  new MapProcessor());
			if(al!=null||al.size()>0)
			{
				corpcode =al.get("def10").toString();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return corpcode;
	}
	//组装生产日期字段
	public String getSCDate(String scdate) {
		String[] dates = scdate.split("-");
		String okdate ="";
		for (int i = 0; i < dates.length; i++) {
			okdate+=dates[i];
		}
		okdate = okdate.substring(3, okdate.length());
		return okdate;
	}
	//最佳使用期
	public String getZJSYQ(String scrq,int zjrq) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dNow = sdf.parse(scrq);
		
//		Date dNow = new Date(); //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(calendar.MONTH, zjrq); //设置为前9月
		dBefore = calendar.getTime(); //得到前3月的时间
		String defaultStartDate = sdf.format(dBefore); //格式化后9月的时间
		
		UFDate sdate = new UFDate(defaultStartDate);
		
		return defaultStartDate;
	}
	
}
