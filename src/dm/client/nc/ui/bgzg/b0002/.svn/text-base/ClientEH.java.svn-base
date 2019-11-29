package nc.ui.bgzg.b0002;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.bgjs.pub.IBGJSITF;
import nc.itf.bgzg.pub.BGZGProxy;
import nc.itf.bh.INcBhItf;
import nc.itf.mm.scm.mm6601.IMo6601;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.common.ListProcessor;
import nc.ui.pf.pub.Toolkit;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.sp.pub.ShowMsgDlg;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.bgzg.b0002.HGZVO;
import nc.vo.bgzg.b0002.MyBillVO;
import nc.vo.bgzg.pub.IBgzgButton;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * @author Administrator
 *
 */

public class ClientEH extends CardEventHandler {
    Hashtable Serialnumtb;
    private String MaxXxsj=null;
	public ClientEH(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	private ClientEnvironment ce = ClientEnvironment.getInstance();
 
    
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		
		if(intBtn == IBgzgButton.print){
			Print();
		}
		if(intBtn == IBgzgButton.hgztm){
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("gystytm", "");
			String key = ClientEnvironment.getInstance().getCorporation().getFathercorp();
			if(key.equals("1095")){
				addtm();
			}
		}
		super.onBoElse(intBtn);
	}
	@SuppressWarnings("deprecation")
	public void Print() throws Exception {
		try
		{
			getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		    AggregatedValueObject agvo  = getBillCardPanelWrapper().getBillVOFromUI();
		    HGZVO hvo = (HGZVO)agvo.getParentVO();
		    int printnum = hvo.getPrintnum();
		    String sjjgnum = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sjjgnum").getValue();
		    int jj=Integer.parseInt(sjjgnum);
		    if(jj<2){
			      throw new BusiBeanException("打印时间间隔小于2,请重新填写!Print time interval is less than 2, please fill in again!");
					
				}
		    if(printnum<=0)
		    	throw new BusiBeanException("打印次数小于1,请重新填写!Print the number is less than 1, please re-fill!");
		    nc.ui.pub.print.PrintEntry m_print = new nc.ui.pub.print.PrintEntry(null, null);
		    nc.ui.pub.print.IDataSource dataSource = null;
		    if(MaxXxsj!=null)
			{
		    	hvo.setXxsj(MaxXxsj);
			}
		    String gystytm = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("gystytm")==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("gystytm").getValue();//供应商统一条码
		    hvo.setGystytm(gystytm);//2015-06-10
		    
		    for(int i = 0 ; i<printnum ; i++){
			HGZVO temp = (HGZVO) hvo.clone();
			//String serialnum="00"+String.valueOf(i);//getSerialnum(temp);
			if(hvo.getSerialnum()==null){
				throw new BusiBeanException("托盘起始数量为空，请输入！");
			}
//			int serialnum=hvo.getSerialnum();//2015-03-11宝翼制罐打印托盘起始顺序号要变为30位.变更为String－－wkf－－start--
//			
//			temp.setSerialnum(i+serialnum);
			
			//edit by wbp  start 2017-10-20 
			String serialnum=hvo.getSerialnum();//2015-03-11宝翼制罐打印托盘起始顺序号要变为30位.变更为String－－wkf－－start--
			String numLg = String.valueOf(i+Integer.valueOf(serialnum));
			if(numLg.length()==1){
				numLg = "00"+numLg;
			}
			if(numLg.length()==2){
				numLg = "0"+numLg;
			}
			temp.setSerialnum(numLg);
			//edit by wbp end 
			
			temp.setGystytm(gystytm);
			//---------------------------------------------start----------
			int gystmpnum=0;//自增的变量
			String serstr=hvo.getGystytm();//得到字符型的托盘起始顺序号
			String tempser = "";//临时存放长度大于8位的字符型数字
			if(!serstr.equals("")){
			Checkser(serstr,hvo.getPrintnum());//校验字符串是否合格
			
			if(serstr.length()<=8){
				gystmpnum = Integer.parseInt(serstr);
			}else{
				tempser = serstr.substring(0, serstr.length()-3);
				String seri = serstr.substring(serstr.length()-3, serstr.length());
				gystmpnum = Integer.parseInt(seri);
			}
			int sumnums = gystmpnum+i;
			String sumnumss = String.valueOf(sumnums);
			if(sumnumss.length()==1){
				sumnumss="00"+sumnumss;
			}else if(sumnumss.length()==2){
				sumnumss="0"+sumnumss;
			}
			if(serstr.length()<=8){
				temp.setGystytm(sumnumss);
			}else{
				temp.setGystytm(tempser+sumnumss);
			}
			}
			//2015-03-11宝翼制罐打印托盘起始顺序号要变为30位.变更为String－－wkf－－end--
			//-----------------------end-------------------------
			
			BillCardPanel cp = new BillCardPanel();
			cp.loadTemplet("HGZ", null,_getOperator(),_getCorp().getPrimaryKey()); 
			
			if(i>0)
			{
				String xxsj=temp.getScrq()+" "+temp.getXxsj();
			    DateFormat  dFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 //   Random rand = new Random();
                Date date=dFormat.parse(xxsj) ;
                Random rand = new Random();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                //edit by shikun 2014-11-04 3分钟+正负30秒:先减去30秒，再加上随机0--59秒，以实现正负30秒。
                cal.add(Calendar.SECOND,-30);
                cal.add(Calendar.SECOND,(jj*60+1+rand.nextInt(59)));
//                cal.add(Calendar.SECOND,(jj*60*i+1+rand.nextInt(119)));
                //edit by shikun 2014-11-04 3分钟+正负30秒。
                dFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String xxrqsj=dFormat.format(cal.getTime());
                xxsj = xxrqsj.substring(11,19);
                String xxrq2 = xxrqsj.substring(0,10);
                temp.setScrq(new UFDate(xxrq2.trim()));
                temp.setXxsj(xxsj);
                //add by shikun 2014-11-04 打印日期在上一张打印日期基础上累计3分钟+正负30秒。
                hvo.setScrq(new UFDate(xxrq2.trim()));
                hvo.setXxsj(xxsj);
                //end shikun 2014-11-04 打印日期在上一张打印日期基础上累计3分钟+正负30秒。
				if(i==printnum-1)
				{
					cal.clear();
					cal.setTime(date);
//					cal.add(Calendar.SECOND,(jj*60*printnum+1+rand.nextInt(119)));
					//edit by zwx 2016-2-29 
		            cal.add(Calendar.SECOND,(jj*60*i+rand.nextInt(59)));
					MaxXxsj=(new SimpleDateFormat("HH:mm:ss")).format(cal.getTime());
				}
								
			}
			MyBillVO mvo = new MyBillVO();
			mvo.setParentVO(temp);
			cp.setBillValueVO(mvo);
			dataSource =  new CardPanelPRTS(((ClientUI)getBillUI()).getModuleCode(), cp);  
			m_print.setDataSource(dataSource);
			m_print.setTemplateID(ce.getCorporation().getPrimaryKey(),((ClientUI)getBillUI()).getModuleCode(),ce.getUser().getPrimaryKey(), null, null);
		 }
		  m_print.preview();
		  
		  //add by zwx 2016-1-6 点击打印时将打印次数更新到对应生产订单中
		  StringBuffer sql = new StringBuffer();
		  String code =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("gystytm").getValue();
		  String dycs =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("printnum").getValue();
		  String scddh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue();
		  String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		  if(code.length()>0&&(!code.equals(""))){
			  code = code.substring(code.length()-3);
			  Integer vdef = Integer.valueOf(code)+Integer.valueOf(dycs);
			   
			  sql.append(" update mm_mo set vdef10 = '"+vdef+"'  ") 
			  .append("  where pk_moid = '"+scddh+"' ") 
			  .append("    and pk_corp = '"+pk_corp+"' ") 
			  .append("    and nvl(dr, 0) = 0 ") ;

			  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
	    	  ipubdmo.executeUpdate(sql.toString());
		  }
		  
		  //end by zwx
		}
		catch(Exception e)
		{
			MaxXxsj=null;
			throw e;
		}
		
	}
	//校验托盘起始号顺序码--wkf--2015-06-17
	private void Checkser(String serstr,int printnum) throws BusiBeanException {
		if(!serstr.equals(null)){
			String seri = serstr.toString();
			if(isNumber(seri)){
				String seri3 = seri.substring(seri.length()-3,seri.length());
				int last3 = Integer.parseInt(seri3);
				if(last3+printnum<1000){
					if(seri.length()!=32){
						throw new BusiBeanException("供应商统一条码必须为32位!");
					}
				}else{
					throw new BusiBeanException("供应商统一条码后三位加打印次数不能大于1000！");
				}
			}else{
				throw new BusiBeanException("供应商统一条码格式有误，请检查!(必须为整数)");
			}
			
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
	private String getSerialnum(HGZVO vo)throws Exception
	{
		StringBuffer  Sql=new StringBuffer();
		Sql.append("select gzzxbm,(case when gzzxbm='001' then '1' when gzzxbm='02' then '2' end ) as zxbz ");
		Sql.append("from mm_mo  a  ");
		Sql.append("left  join mm_mokz c on a.pk_moid=c.pk_moid  ");
		Sql.append("left join pd_wk b on c.gzzxid =B.PK_WKID  ");
        Sql.append("where  a.pk_corp='1016' and a.pk_moid='"+vo.pk_moid+"'");
        String serialnum=null;
        IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
        HashMap al;
		try {
			al = (HashMap)sessionManager.executeQuery(Sql.toString(),  new MapProcessor());
		
        if(al==null||al.size()<=0||String.valueOf(al.get("gzzxbm"))==null||
        		String.valueOf(al.get("gzzxbm")).equals("")||String.valueOf(al.get("gzzxbm")).equalsIgnoreCase("null"))
        {
        	throw new Exception("生产订单中没生产线！No production line production orders!");
        }
       UFDate date=vo.getScrq();
        serialnum=String.valueOf(al.get("zxbz"));
        serialnum+=date.toString().substring(2, 4);
		byte[] t = new byte[]{64};
		t[0] += date.getMonth();
		serialnum+=(new String(t));
		serialnum+=date.toString().substring(8, 10);
	//	UIRefPane pane = (UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("scbc").getComponent();
		String bzmc = vo.getScbc();
		if(bzmc==null||bzmc.trim().equals("")){
		 throw new Exception("班组不能为空！Team can not be empty!");
		
		}else if(!checkbc(bzmc))
		{
			 throw new Exception("班组不符合规范！Team does not meet specifications!");
		}
		serialnum+=bzmc;
		serialnum+=getFlowNo(serialnum);
		return serialnum;

	} catch (Exception e) {
		// TODO Auto-generated catch block
		 throw e;
	}
	}
	private boolean checkbc(String bzmc) {
		// TODO Auto-generated method stub
		if( bzmc.length()!=1)
		 {
			 return false;
		 }
		 else 
		 {
			 Pattern pattern = Pattern.compile("[A-D]");
			 Matcher matcher = pattern.matcher(bzmc);
			return  matcher.matches();
		 }
		
	}
	private String getFlowNo(String serialnum)throws Exception
	{
		String Sql="select flowindex from mm_hgz_serialnum where serialnum='"+serialnum+"'";
		try
	     {
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
			.getInstance().lookup(IUAPQueryBS.class.getName());
	       HashMap al = (HashMap)sessionManager.executeQuery(Sql.toString(),  new MapProcessor());
	     if(al==null||al.size()>0||String.valueOf(al.get("flowindex"))==null||
	        		String.valueOf(al.get("flowindex")).equals("")||String.valueOf(al.get("flowindex")).equalsIgnoreCase("null"))
	     {
	    	 SaveFlowNo(serialnum,false,"1");
	    	return "001"; 
	    	
	     }
	     String index=String.valueOf(al.get("flowindex")).trim();
	     SaveFlowNo(serialnum,false,String.valueOf(Integer.parseInt(index)+1));
	     return "00"+String.valueOf(Integer.parseInt(index)+1);
	
	     }
		catch(Exception e)
		{
			throw e;
		}
	}
   private void SaveFlowNo(String serialnum,boolean isupdate,String index)throws Exception
   {
	  if(!isupdate)
	  {  
		  String pk = new SequenceGenerator(BGZGProxy.class.getName()).generate(_getCorp().pk_corp);
	      StringBuffer Sql =new StringBuffer();
	      Sql.append("insert into mm_hgz_serialnum(pk_serialnum,serialnum,flowindex)");
	      Sql.append("values('"+pk+"','"+serialnum+"',"+index+") ");
	    //  getBaseDAO().executeUpdate(Sql.toString());
	      
	  }
	  else 
	  {

		  StringBuffer Sql =new StringBuffer();
	      Sql.append("update mm_hgz_serialnum) ");
	      Sql.append("set flowindex=nvl(flowindex,0)+"+index+") ");
	      Sql.append("where serialnum='"+serialnum+"'");
	      
	    //  getBaseDAO().executeUpdate(Sql.toString());
	  }
	   //getBaseDAO().executeUpdate(s)
   }
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sjjgnum", 3);
	}
	
	
	public void addtm() throws Exception {
//		ClientUI ui=new ClientUI();
		String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
		if(pk_invbasdoc.equals("")||pk_invbasdoc==null){
			throw new BusiBeanException("存货编码不可为空，请输入后再试！");
		}
		String scrq = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scrq").getValue();
		if(scrq.equals("")||scrq==null){
			
			throw new BusiBeanException("生产日期不可为空，请输入后再试！");
		}
		
		String scbc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scbc").getValue();
		if(scbc.equals("")||scbc==null){
			throw new BusiBeanException("生产班次不可为空，请输入后再试！");
			
		}
		if(scbc.length()!=2){
			throw new BusiBeanException("生产班次必须为两位，请输入后再试！");
		}
		String zjsyq = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("zjsyq").getValue();
		int zjmonths = 0;
		if(zjsyq.equals("")||zjsyq==null){
			throw new BusiBeanException("最佳使用期范围不可为空，请输入后再试！");
			
		}else{
			zjmonths = Integer.parseInt(zjsyq);
		}
		String zjsyqok = getZJSYQ(scrq,zjmonths);//生成最佳使用日期
		String jh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("jh").getValue();
		if(jh.equals("")||jh==null){
			throw new BusiBeanException("机号不可为空，请输入后再试！");
			
		}
		if(jh.length()!=2){
			throw new BusiBeanException("机号必须为两位，请输入后再试！");
		}
		String corpid = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String xzjh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xzjh").getValue();
		if(!corpid.equals("1016")){//宝翼的不需要新增机号 src 2018年7月30日17:21:30
			if(xzjh.equals("")||xzjh==null){
				
				throw new BusiBeanException("新增机号不可为空，请输入后再试！");
			
			}
			if(xzjh.length()!=2){
				throw new BusiBeanException("新增机号必须为两位，请输入后再试！");
			}
		}
		String sapinvcode = getInvSAPcode(pk_invbasdoc);//sap物料编码
		//校验sap物料编码必须为8位或为Null
		if(sapinvcode.equals("")||sapinvcode.length()!=8){
			throw new BusiBeanException("存货在基本档案中没有维护SAP物料编码或SAP物料编码不是8位，请维护！");
		}
		String corpcode = getCorpGCcode();//工厂编码
		if(!corpid.equals("1016")){//宝翼的公司目录下工厂编码是八位 src 2018年7月30日17:21:30
			if(corpcode.equals("")||corpcode.length()!=5){
				throw new BusiBeanException("本公司：在公司目录中没有维护工厂编码或工厂编码不是5位，请维护！");
			}
		}else if(corpid.equals("1016")){
			if(corpcode.equals("")||corpcode.length()!=7){
				throw new BusiBeanException("本公司：在公司目录中没有维护工厂编码或工厂编码不是7位，请维护！");
			}
		}
		//组装生产日期字段
		String scdate =getSCDate(scrq);
		String zjsyrq =getSCDate(zjsyqok);//最佳使用日期
		//add by zwx 2016-1-6 点击生成条码时根据生产订单号获取已打印次数
		String scddh = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scddh").getValue();
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		StringBuffer sql = new StringBuffer();
		sql.append(" select vdef10 from mm_mo ") 
		.append(" where pk_moid = '"+scddh+"' ") 
		.append(" and pk_corp = '"+pk_corp+"' ") 
		.append(" and nvl(dr,0) = 0 ") ;
		
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object obj = sessionManager.executeQuery(sql.toString(),  new ColumnProcessor());
		Integer printnum = new Integer(1);
		String str_format = "000";
		if(obj!=null){
			printnum=Integer.valueOf(obj.toString());
		}
		DecimalFormat df = new DecimalFormat(str_format);
		String num = df.format(printnum);
		//add by zwx 2016-5-3 条码重新排序
		Integer serialnum = new Integer(1); 
		String qsnum = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("serialnum").getValue();
		if(qsnum!=null&&(!qsnum.equals(""))){
			serialnum=Integer.valueOf(qsnum);
		}else{
			throw new BusiBeanException("托盘起始号顺序码不能为空，请修改！");
		}
		qsnum = df.format(serialnum);
		if(qsnum.length()>3){
			throw new BusiBeanException("托盘起始号顺序码不能超出三位，请修改！");
		}
		//组装供应商统一条码
		String gystytm = "";//宝翼的不需要拼接新增机组
		if(!corpid.equals("1016")){
			gystytm = sapinvcode+corpcode+scdate+scbc+jh+xzjh+zjsyrq+qsnum;
		}else if(corpid.equals("1016")){
			gystytm = sapinvcode+corpcode+scdate+scbc+jh+zjsyrq+qsnum;
		}
		//end by zwx
		/*//组装供应商统一条码
		String gystytm = sapinvcode+scdate+scbc+corpcode+zjsyrq+jh+xzjh+num;*/
		//end by zwx 
/*		//组装供应商统一条码
		String gystytm = sapinvcode+scdate+scbc+corpcode+zjsyrq+jh+xzjh+"001";*/
		if(gystytm.length()!=32){
			throw new BusiBeanException("供应商统一条码长度不符合标准，请检查后重新生成！");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("gystytm", gystytm);
	
	}
	
	//获得存货基本档案sap物料编码
		public String getInvSAPcode(String pk_invmandoc) {
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			StringBuffer sapsql = new StringBuffer();
			sapsql.append(" select def8 ") 
			//sapsql.append(" select * ") 
			.append("   from bd_invbasdoc ") 
			.append("  where nvl(dr, 0) = 0 ") 
			.append("    and pk_invbasdoc = '"+pk_invmandoc+"' ") ;


			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sapinvcode = "";
			HashMap al;
			try {
				al = (HashMap)sessionManager.executeQuery(sapsql.toString(),  new MapProcessor());
				Boolean flg=false;
				Iterator iterator = al.keySet().iterator();
				while(iterator.hasNext()) {
				if(al.get(iterator.next())!=null){
					flg=true;
				}
				}
				//System.out.println(al.get("def8").toString());
				if( flg==true)
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
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
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
			
//			Date dNow = new Date(); //当前时间
			Date dBefore = new Date();
			Calendar calendar = Calendar.getInstance(); //得到日历
			calendar.setTime(dNow);//把当前时间赋给日历
			calendar.add(calendar.MONTH, zjrq); //设置为后9月
			dBefore = calendar.getTime(); //得到后9月的时间
			String defaultStartDate = sdf.format(dBefore); //格式化后9月的时间
			
			UFDate sdate = new UFDate(defaultStartDate);
			UFDate tmp = sdate.getDateAfter(-1);//最佳使用期向前提一天
			String backdate = tmp.toString();
			return backdate;
		}
}
