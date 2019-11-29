package nc.ui.mo.hgz;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.mm.hgz.button.IBHgzZgButton;
import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mo.hgz.HgzHeadVO;
import nc.vo.mo.hgz.onhand.OnhandVO;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.button.ButtonVO;
/**
 * 合格证打印（制盖）界面入口类
 * @author 刘信彬
 *
 */

public class HgzUI extends BillManageUI{
	
	//add by gt 2019-08-12
	BillManageUI billUI;
	AbstractManageController ctrl;
    //--gt end--

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new HgzController();
	}
	
	
	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new HgzHandler(this,new HgzController());
	}

	protected void initPrivateButton() {
		ButtonVO btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("生成条码","sctm"));
		btn.setBtnChinaName("生成条码");
		btn.setBtnNo(IBHgzZgButton.sctm);
		this.addPrivateButton(btn);
		
		btn=new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("生成下线检验单","scxxjyd"));
		btn.setBtnChinaName("生成下线检验单");
		btn.setBtnNo(IBHgzZgButton.scxxjyd);
		this.addPrivateButton(btn);
	}


	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		getBillListPanel().getHeadTable().setSelectionMode(2);
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		getBillCardPanel().setHeadItem("productiontime", _getServerTime().getTime().substring(0,5));
		getScxScddh();
		this.getBillCardPanel().execHeadLoadFormulas();
	}
	
	//add by zy 2019-08-26
	private String getUnitPK()
	{
		return _getCorp().getPrimaryKey(); //ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
	}
	
	protected void setHeadItemValue(String key,Object value)
	{
		this.getBillCardPanel().getHeadItem(key).setValue(value); 
	}
	
	protected void updateBodyValue()
	{ 
		UITable table = this.getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanel().getBillModel();
		for(int i=0;i<table.getRowCount();i++)
		{
			UIRefPane pane = (UIRefPane) this.getBillCardPanel().getHeadItem("invname").getComponent();
			model.setValueAt(pane.getRefName(), i, "invname"); 
			model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), i,"lh");
			if(model.getValueAt(i, "pk_glzb_b")!=null||!String.valueOf(model.getValueAt(i, "pk_glzb_b")).equals("")||String.valueOf(model.getValueAt(i, "pk_glzb_b")).equalsIgnoreCase("null"))
			{
				model.setRowState(i, BillModel.MODIFICATION);
			}
			this.getBillCardPanel().execBodyFormula(i, "lh");
		}
	}
	

	@Override
	public void afterEdit(BillEditEvent e)
    {
		getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(e.getKey()));
		super.afterEdit(e);
		String platformnum = getBillCardPanel().getHeadItem("mplatform").getValueObject()==null?
				"": getBillCardPanel().getHeadItem("mplatform").getValueObject().toString();
		if(e.getKey().equals("mplatform")){//机台
			setDefaultInfo(platformnum,getCorpPrimaryKey());
			getScxScddh();
			getBillCardPanel().execHeadLoadFormulas();
		}else if(e.getKey().equals("produceorder")){//生产订单
			autoSelect();//自动勾选
//			String memo = setDefaultMemo();//获取默认备注
//			getBillCardPanel().setHeadItem("remarks",memo);
			queryDefaultKS();//获取默认客商
			String zdsl = "";
			//add by zy 2019-08-26
			try
			{  
				String pkValue  = ((UIRefPane) e.getSource()).getRefPK();
				if(pkValue!=null)
				{
					MoHeaderVO heads[] = MMProxy.getRemoteMO().queryMoByWhere(" and mm_mo.pk_moid='"+pkValue+"'"); 
					if(heads!=null&&heads.length==1)
					{
						String pk_invmandoc = (String)HYPubBO_Client.findColValue("bd_invmandoc", "pk_invmandoc", " nvl(dr,0)!=1 and pk_corp='"+this.getUnitPK()+"' and pk_invbasdoc='"+heads[0].getWlbmid()+"'");
						setHeadItemValue("invname",pk_invmandoc);
						this.getBillCardPanel().execHeadTailEditFormulas();
						
						
						//add by zwx 2019-8-15 制盖整垛数量通过生产订单数量传递
					
						if(getCorpPrimaryKey().equals("1108")&&heads!=null&&heads.length==1){
							zdsl = heads[0].getZdy2()==null?"0":heads[0].getZdy2();
						}
						//end by zwx
						
					}
					//add by zwx 判断是否合并垛 2016-3-3 
					isMerge(zdsl);
					//end by zwx 
				}else{
					setHeadItemValue("productname",null);
					setHeadItemValue("zdsl",null);
				}
			}catch (Exception err)
			{
				// TODO Auto-generated catch block
				err.printStackTrace();
			}
			
		
		//end by zy
			
		}else if(e.getKey().equals("productname")){//产品
			autoSelect();
		}else if(e.getKey().equals("vdef8")){//流水号，不能超过999，为整数
			String vdef8 = getBillCardPanel().getHeadItem("vdef8").getValueObject()==null?
					"": getBillCardPanel().getHeadItem("vdef8").getValueObject().toString();
			if(vdef8.length()>0){
				if(!isNumeric(vdef8)){
					showWarningMessage("流水号必须为正整数！");
					return;
				}
				if(Integer.valueOf(vdef8)>999){
					showWarningMessage("流水号必须小于999");
					return;
				}
				
			}
		}
    }

	/**
	 * 判断是否为正整数
	 * @param string
	 * @return
	 */
	public static boolean isNumeric(String string){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(string).matches();   
	}
	
	/**
	 * 设置历史最新机台等信息
	 * @return
	 */
	public void setDefaultInfo(String platformNum,String corp){
		HgzHeadVO hgz = queryHgz(platformNum,corp);
		if(hgz!=null){//为空则第一次手动输入
			getBillCardPanel().getHeadItem("mplatform").setValue(hgz.getMplatform());//机台
			getBillCardPanel().getHeadItem("inlibrarytype").setValue(hgz.getInlibrarytype());//入库种类
			getBillCardPanel().getHeadItem("layernumber").setValue(hgz.getLayernumber());//层数
			getBillCardPanel().getHeadItem("thickness").setValue(hgz.getThickness());//厚度
			getBillCardPanel().getHeadItem("nnumber").setValue(hgz.getNnumber());//数量
			getBillCardPanel().getHeadItem("stripnumber").setValue(hgz.getStripnumber());//条数
			getBillCardPanel().getHeadItem("shifts").setValue(hgz.getShifts());//班次
			getBillCardPanel().getHeadItem("qualitycontroller").setValue(hgz.getQualitycontroller());//品管员
			getBillCardPanel().getHeadItem("producttype").setValue(hgz.getProducttype());//产品种类
//			getBillCardPanel().getHeadItem("remarks").setValue(hgz.getRemarks());//备注
			getBillCardPanel().getHeadItem("material").setValue(hgz.getMaterial());//材料
			getBillCardPanel().getHeadItem("glue").setValue(hgz.getGlue());//胶种
			getBillCardPanel().getHeadItem("modules").setValue(hgz.getModules());//模别
			getBillCardPanel().getHeadItem("batchnumbercode").setValue(hgz.getBatchnumbercode());//批次号
			String tph = getNewTph(hgz.getPalletnumber()==null?"":hgz.getPalletnumber());
			getBillCardPanel().getHeadItem("palletnumber").setValue(tph);//托盘号
			getBillCardPanel().getHeadItem("vdef8").setValue(getNewSerialNum(hgz.getVdef8()==null?"":hgz.getVdef8()));//流水号
			//add by zy 2019-09-29 增加判断：用户重新选择“机台”后，“生产订单号”清空
			getBillCardPanel().getHeadItem("produceorder").setValue("");//生产订单号
			//end
		}else{
			getBillCardPanel().getHeadItem("palletnumber").setValue("");//无值则清空
		}
		
	}
	
	
	/**
	 * 获取最新流水号，根据上次流水号+1，范围0-999
	 * @param oldnum
	 * @return
	 */
	public String getNewSerialNum(String oldnum){
		if(oldnum.equals("")){
			oldnum = "0";
		}
		int aq = new Integer(oldnum).intValue()+1;
		while(aq>999){
			aq = aq-999;
		}
		DecimalFormat g1=new DecimalFormat("000");
        String newnum = g1.format(Integer.valueOf(String.valueOf(aq)));
        return newnum;
	}
	
	
	/**
	 * 获取当前机台最新机台号
	 * 机台=生产线+年+流水号
	 * @param oldtph
	 * @return
	 */
	public String getNewTph(String oldtph){
		if(oldtph.length()>0){
			String scx = oldtph.substring(0,1);
			String year = oldtph.substring(1,3);
			DecimalFormat g1=new DecimalFormat("000000");
			String lsh = g1.format(Integer.valueOf(oldtph.substring(3,9))+1);//当前机台的流水+1
			
			Calendar cale = null;  
	        cale = Calendar.getInstance();  
	        int yearnum = cale.get(Calendar.YEAR);
	        
			String newyear = String.valueOf(yearnum).substring(2,4);
			String newtph = "";
			if(newyear.equals(year)){
				newtph = scx+year+lsh;
			}else{
				newtph = scx+newyear+"000001";
			}
			return newtph;	
		}else{
			return "";
		}
			
	}
	
	/**
	 * 查询历史最新合格证信息
	 * @return
	 */
	public HgzHeadVO queryHgz(String platformNum,String corp){
		
		StringBuffer sql = new StringBuffer();
		if(platformNum.length()>0){
			sql.append(" select * ") 
			.append("   from (select * ") 
			.append("           from mm_hgz ") 
			.append("          where mplatform = '"+platformNum+"' ") 
			.append("            and nvl(dr, 0) = 0 ") 
			.append("            and pk_corp = '"+corp+"' ") 
			.append("            and vdef7 = '合格证' ") 
			.append("          order by ts desc) ") 
			.append("  where rownum = 1 ") ;

		}else{
			sql.append(" select * ") 
			.append("   from (select * ") 
			.append("           from mm_hgz ") 
			.append("          where nvl(dr, 0) = 0 ") 
			.append("            and pk_corp = '"+corp+"' ") 
			.append("            and vdef7 = '合格证' ") 
			.append("          order by ts desc) ") 
			.append("  where rownum = 1 ") ;
		}
		
		HgzHeadVO vo = new HgzHeadVO();
		IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			vo = (HgzHeadVO) uap.executeQuery(sql.toString(), new BeanProcessor(HgzHeadVO.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	/**
	 * 自动勾选物料名称中包含‘可口可乐’的是否打印
	 */
	public void autoSelect(){
		String produce = getBillCardPanel().getHeadItem("productname").getValueObject()==null?"":getBillCardPanel().getHeadItem("productname").getValueObject().toString();//存货名称
		if(produce.length()>0){
			StringBuffer sql = new StringBuffer();
			sql.append(" select bas.invname as invname from bd_produce pro ") 
			.append(" inner join bd_invbasdoc bas ") 
			.append(" on bas.pk_invbasdoc = pro.pk_invbasdoc ") 
			.append(" where pro.pk_produce = '"+produce+"' ") 
			.append(" and nvl(bas.dr,0) = 0 ") 
			.append(" and nvl(pro.dr,0) = 0 ") 
			.append(" and pro.pk_corp = '"+getCorpPrimaryKey()+"' ") ;
			Object invname = "";
			IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				invname = uap.executeQuery(sql.toString(), new ColumnProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if(invname!=null&&invname.toString().length()>0&&invname.toString().contains("可口可乐")){
				getBillCardPanel().setHeadItem("isprintcokecode", true);
			}else{
				getBillCardPanel().setHeadItem("isprintcokecode", false);
			}
		}
		
	}
	
	/**
	 * 默认备注：生成订单号+流水号
	 * 按照当前订单历史生成记录当作流水，所以不需要过滤删除的记录否则会导致重复流水号
	 */
	public String setDefaultMemo(){
		String produceorder = getBillCardPanel().getHeadItem("produceorder").getValueObject()==null?"":getBillCardPanel().getHeadItem("produceorder").getValueObject().toString();//存货名称
		String memo = "";
		if(produceorder.length()>0){
			StringBuffer sql = new StringBuffer();
			sql.append("   select count(*) as num,b.scddh ") 
			.append("     from mm_hgz h ") 
			.append("     inner join mm_mo b ") 
			.append("     on h.produceorder = b.pk_moid ") 
			.append("    where h.produceorder = '"+produceorder+"' ") 
			.append("      and h.pk_corp = '"+getCorpPrimaryKey()+"' ")
			.append("  group by b.scddh ") ;

			HashMap map = new HashMap();
			IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				map = (HashMap) uap.executeQuery(sql.toString(), new MapProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if(map!=null){
				Integer num = map.get("num")==null?new Integer(0):new Integer(map.get("num").toString());
				String scddh = map.get("scddh")==null?"":map.get("scddh").toString();
				if(num.intValue()==0){
					memo = scddh+"-001"; 
				}else{
					DecimalFormat g1=new DecimalFormat("000");
					memo = scddh+"-"+g1.format(Integer.valueOf(num.toString())+1);
				}
			}else{
				String express = "scddh-> getColValue( mm_mo,scddh ,pk_moid , \"" + produceorder + "\")";
				FormulaParse parse = new FormulaParse();
				parse.setExpress(express);
				String scddh = parse.getValue()==null?"":parse.getValue();
				memo = scddh+"-001"; 
			}
		}
		return memo;
	}
	
	
	public void queryDefaultKS(){
		String produceorder = getBillCardPanel().getHeadItem("produceorder").getValueObject()==null?"":getBillCardPanel().getHeadItem("produceorder").getValueObject().toString();//存货名称
		if(produceorder.length()>0){
			StringBuffer sql = new StringBuffer();
			sql.append("  ") 
			.append(" select distinct cuman.pk_cumandoc as pk_cumandoc ") 
			.append("   from bd_produce pro ") 
			.append("  inner join bd_invbasdoc bas ") 
			.append("     on pro.pk_invbasdoc = bas.pk_invbasdoc ") 
			.append("  inner join mm_mo mm ") 
			.append("     on mm.pk_produce = pro.pk_produce ") 
			.append("  inner join bd_invmandoc man ") 
			.append("     on bas.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("  inner join bd_cubasdoc cubas ") 
			.append("     on cubas.pk_cubasdoc = man.def17 ") 
			.append("  inner join bd_cumandoc cuman ") 
			.append("     on cuman.pk_cubasdoc = cubas.pk_cubasdoc ") 
			.append("  where mm.pk_moid = '"+produceorder+"' ") 
			.append("    and man.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("    and pro.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("    and nvl(pro.dr,0) = 0 ") 
			.append("    and nvl(bas.dr,0) = 0 ") 
			.append("    and nvl(mm.dr,0) = 0 ") 
			.append("    and nvl(man.dr,0) = 0 ") 
			.append("    and nvl(cubas.dr,0) = 0 ") 
			.append("    and nvl(cuman.dr,0) = 0 ") ;


			ArrayList list = null;
			IUAPQueryBS uap=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				list = (ArrayList) uap.executeQuery(sql.toString(), new ColumnListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(list!=null&&list.size()>0){
				String pk_cumandoc = list.get(0).toString();
				getBillCardPanel().setHeadItem("customername", pk_cumandoc);
			}
		}
		

	}
	
	//add by zy 2019-08-26
	//add by zwx 2016-3-3 根据选择的生产订单的料号，如果本仓库内有零头剁情况提示是否并剁，点“是”进入隔离处理界面，系统带入零头剁数据
	@SuppressWarnings("unchecked")
	public void isMerge(String zdsl)
  {
    String corp = getUnitPK();
    StringBuffer querysql = new StringBuffer();
    String cal = "";
    ArrayList list = new ArrayList();
    StringBuffer err = new StringBuffer();
    
    String scbill = getBillCardPanel().getHeadItem("produceorder").getValueObject() == null ? "0" : 
        getBillCardPanel().getHeadItem("produceorder").getValueObject().toString();
    String cpname = getBillCardPanel().getHeadItem("productname").getValueObject() == null ? "0" : 
        getBillCardPanel().getHeadItem("productname").getValueObject().toString();
    String warehouse = getBillCardPanel().getHeadItem("vdef2").getValueObject() == null ? "0" : 
        getBillCardPanel().getHeadItem("vdef2").getValueObject().toString();

    String pkinv = getBillCardPanel().getHeadItem("invname").getValueObject() == null ? "" : 
      getBillCardPanel().getHeadItem("invname").getValueObject().toString();
    /*String warehouse = getBillCardPanel().getHeadItem("vdef2").getValueObject() == null ? "" : 
        getBillCardPanel().getHeadItem("vdef2").getValueObject().toString();*/
    StringBuffer sql = new StringBuffer();
    sql.append(" select pk_calbody from bd_calbody ")
      .append(" where pk_corp = '" + corp + "' ");

    IUAPQueryBS sessionManager = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    try {
      cal = (String)sessionManager.executeQuery(sql.toString(), new ColumnProcessor()) == null ? "" : (String)sessionManager.executeQuery(sql.toString(), new ColumnProcessor());
    }
    catch (BusinessException e) {
      e.printStackTrace();
    }
    /**
     * add by gt 2019-08-15
     * 根据批号检验是否并垛
     */
    if("1108".equals(corp)){
    	querysql.append(" select a.cwarehouseid, ")
        .append("        a.cinventoryid, ")
        .append("        a.cspaceid, ")
        .append("        a.vbatchcode, ")
        .append("        a.num, ")
        .append("        a.invcode,  ")
        .append("        a.csname,a.cwarehouseid, a.vfree1 ")
        .append("   from (select kp.pk_corp, ")
        .append("                kp.ccalbodyid, ")
        .append("                kp.cwarehouseid, ")
        .append("                kp.cinventoryid, ")
        .append("                kp.cspaceid, ")
        .append("                kp.vbatchcode, ")
        .append("                SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num, ")
        .append("                SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum, ")
        .append("                inv.invcode, ")
        .append("                carg.csname , kp.vfree1")
        .append("           from v_ic_onhandnum6 kp, bd_invmandoc man, bd_invbasdoc inv,bd_cargdoc carg ")
        .append("          where kp.cinventoryid = man.pk_invmandoc ")
        .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ")
        .append("            and nvl(kp.cspaceid, ' ') = nvl(carg.pk_cargdoc(+), ' ')")
        .append("            and ((kp.ccalbodyid = '" + cal + "') and (0 = 0) and ")
        .append("                (kp.cwarehouseid = '1108111000000000007W') and (0 = 0) and")//添加仓库判定(良品尾数仓)
        .append("                (kp.cinventoryid = '" + pkinv + "') and (0 = 0) and (0 = 0) and ")
        .append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ")
        .append("                (kp.pk_corp = '" + corp + "')) ")
        .append("          group by kp.pk_corp, ")
        .append("                   kp.ccalbodyid, ")
        .append("                   kp.cwarehouseid, ")
        .append("                   kp.cinventoryid, ")
        .append("                   kp.cspaceid, ")
        .append("                   kp.vbatchcode, ")
        .append("                   inv.invcode,  ")
        .append("                   carg.csname, kp.vfree1 ")
        .append("                   ) a, ")
        .append("        (select kp.pk_corp, ")
        .append("                kp.ccalbodyid, ")
        .append("                kp.cwarehouseid, ")
        .append("                kp.cinventoryid, ")
        .append("                kp.cspaceid, ")
        .append("                kp.vbatchcode, kp.vfree1,")
        .append("                sum(nvl(nfreezenum, 0)) freezenum, ")
        .append("                sum(nvl(ngrossnum, 0)) ngrossnum ")
        .append("           from ic_freeze kp, bd_invmandoc man, bd_invbasdoc inv ")
        .append("          where kp.cinventoryid = man.pk_invmandoc ")
        .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ")
        .append("            and (cthawpersonid is null and ")
        .append("                (kp.ccalbodyid = '" + cal + "') and (0 = 0) and ")
        .append("                (kp.cwarehouseid = '1108111000000000007W') and (0 = 0) and")//添加仓库判定(良品尾数仓)
        .append("                (kp.cinventoryid = '" + pkinv + "') and (0 = 0) and (0 = 0) and ")
        .append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ")
        .append("                (kp.pk_corp = '" + corp + "')) ")
        .append("          group by kp.pk_corp, ")
        .append("                   kp.ccalbodyid, ")
        .append("                   kp.cwarehouseid, ")
        .append("                   kp.cinventoryid, ")
        .append("                   kp.cspaceid, ")
        .append("                   kp.vbatchcode, kp.vfree1)b ")
        .append("  where a.pk_corp = b.pk_corp(+) ")
        .append("    and a.ccalbodyid = b.ccalbodyid(+) ")
        .append("    and a.cwarehouseid = b.cwarehouseid(+) ")
        .append("    and a.cinventoryid = b.cinventoryid(+) ")
        .append("    and nvl(a.cspaceid, ' ') = nvl(b.cspaceid(+), ' ') ")
        .append("    and nvl(a.vbatchcode, ' ') = nvl(b.vbatchcode(+), ' ') ")
        .append("     and nvl(a.vfree1, ' ') = nvl(b.vfree1(+), ' ')")
        .append("    and (a.num > 0)   ");
//        .append("    and a.num != " + zdsl + " "); 
    	
    }
    else{
    // end by gt 2019-08-15
    	//根据垛号检验是否并垛    其中vfree1条件就是在现存量单据中根据垛号查询
    querysql.append(" select a.cwarehouseid, ")
      .append("        a.cinventoryid, ")
      .append("        a.cspaceid, ")
      .append("        a.vbatchcode, ")
      .append("        a.vfree1,  ")
      .append("        a.num, ")
      .append("        a.invcode,  ")
      .append("        a.csname,a.cwarehouseid ")
      .append("   from (select kp.pk_corp, ")
      .append("                kp.ccalbodyid, ")
      .append("                kp.cwarehouseid, ")
      .append("                kp.cinventoryid, ")
      .append("                kp.cspaceid, ")
      .append("                kp.vbatchcode, ")
      .append("                kp.vfree1, ")
      .append("                kp.vfree2, ")
      .append("                kp.vfree3, ")
      .append("                kp.vfree4, ")
      .append("                kp.vfree5, ")
      .append("                kp.vfree6, ")
      .append("                kp.vfree7, ")
      .append("                kp.vfree8, ")
      .append("                kp.vfree9, ")
      .append("                kp.vfree10, ")
      .append("                SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num, ")
      .append("                SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum, ")
      .append("                inv.invcode, ")
      .append("                carg.csname ")
      .append("           from v_ic_onhandnum6 kp, bd_invmandoc man, bd_invbasdoc inv,bd_cargdoc carg ")
      .append("          where kp.cinventoryid = man.pk_invmandoc ")
      .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ")
      .append("            and kp.cspaceid = carg.pk_cargdoc ")
      .append("            and ((kp.ccalbodyid = '" + cal + "') and (0 = 0) and ")
//      .append("                (kp.cwarehouseid = '1108111000000000007W') and (0 = 0) and")//添加仓库判定(良品尾数仓)
      .append("                (kp.cinventoryid = '" + pkinv + "') and (0 = 0) and (0 = 0) and ")
      .append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ")
      .append("                (kp.pk_corp = '" + corp + "')) ")
      .append("          group by kp.pk_corp, ")
      .append("                   kp.ccalbodyid, ")
      .append("                   kp.cwarehouseid, ")
      .append("                   kp.cinventoryid, ")
      .append("                   kp.cspaceid, ")
      .append("                   kp.vbatchcode, ")
      .append("                   kp.vfree1, ")
      .append("                   kp.vfree2, ")
      .append("                   kp.vfree3, ")
      .append("                   kp.vfree4, ")
      .append("                   kp.vfree5, ")
      .append("                   kp.vfree6, ")
      .append("                   kp.vfree7, ")
      .append("                   kp.vfree8, ")
      .append("                   kp.vfree9, ")
      .append("                   kp.vfree10, ")
      .append("                   inv.invcode,  ")
      .append("                   carg.csname ")
      .append("                   ) a, ")
      .append("        (select kp.pk_corp, ")
      .append("                kp.ccalbodyid, ")
      .append("                kp.cwarehouseid, ")
      .append("                kp.cinventoryid, ")
      .append("                kp.cspaceid, ")
      .append("                kp.vbatchcode, ")
      .append("                kp.vfree1, ")
      .append("                kp.vfree2, ")
      .append("                kp.vfree3, ")
      .append("                kp.vfree4, ")
      .append("                kp.vfree5, ")
      .append("                kp.vfree6, ")
      .append("                kp.vfree7, ")
      .append("                kp.vfree8, ")
      .append("                kp.vfree9, ")
      .append("                kp.vfree10, ")
      .append("                sum(nvl(nfreezenum, 0)) freezenum, ")
      .append("                sum(nvl(ngrossnum, 0)) ngrossnum ")
      .append("           from ic_freeze kp, bd_invmandoc man, bd_invbasdoc inv ")
      .append("          where kp.cinventoryid = man.pk_invmandoc ")
      .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ")
      .append("            and (cthawpersonid is null and ")
      .append("                (kp.ccalbodyid = '" + cal + "') and (0 = 0) and ")
//      .append("                (kp.cwarehouseid = '1108111000000000007W') and (0 = 0) and")//添加仓库判定(良品尾数仓)
      .append("                (kp.cinventoryid = '" + pkinv + "') and (0 = 0) and (0 = 0) and ")
      .append("                (0 = 0) and (0 = 0) and (0 = 0) and (0 = 0) and ")
      .append("                (kp.pk_corp = '" + corp + "')) ")
      .append("          group by kp.pk_corp, ")
      .append("                   kp.ccalbodyid, ")
      .append("                   kp.cwarehouseid, ")
      .append("                   kp.cinventoryid, ")
      .append("                   kp.cspaceid, ")
      .append("                   kp.vbatchcode, ")
      .append("                   kp.vfree1, ")
      .append("                   kp.vfree2, ")
      .append("                   kp.vfree3, ")
      .append("                   kp.vfree4, ")
      .append("                   kp.vfree5, ")
      .append("                   kp.vfree6, ")
      .append("                   kp.vfree7, ")
      .append("                   kp.vfree8, ")
      .append("                   kp.vfree9, ")
      .append("                   kp.vfree10) b ")
      .append("  where a.pk_corp = b.pk_corp(+) ")
      .append("    and a.ccalbodyid = b.ccalbodyid(+) ")
      .append("    and a.cwarehouseid = b.cwarehouseid(+) ")
      .append("    and a.cinventoryid = b.cinventoryid(+) ")
      .append("    and nvl(a.cspaceid, ' ') = nvl(b.cspaceid(+), ' ') ")
      .append("    and nvl(a.vbatchcode, ' ') = nvl(b.vbatchcode(+), ' ') ")
      .append("    and nvl(a.vfree1, ' ') = nvl(b.vfree1(+), ' ') ")
      .append("    and nvl(a.vfree2, ' ') = nvl(b.vfree2(+), ' ') ")
      .append("    and nvl(a.vfree3, ' ') = nvl(b.vfree3(+), ' ') ")
      .append("    and nvl(a.vfree4, ' ') = nvl(b.vfree4(+), ' ') ")
      .append("    and nvl(a.vfree5, ' ') = nvl(b.vfree5(+), ' ') ")
      .append("    and nvl(a.vfree6, ' ') = nvl(b.vfree6(+), ' ') ")
      .append("    and nvl(a.vfree7, ' ') = nvl(b.vfree7(+), ' ') ")
      .append("    and nvl(a.vfree8, ' ') = nvl(b.vfree8(+), ' ') ")
      .append("    and nvl(a.vfree9, ' ') = nvl(b.vfree9(+), ' ') ")
      .append("    and nvl(a.vfree10, ' ') = nvl(b.vfree10(+), ' ') ")
      .append("    and (a.num > 0)   ");
//      .append("    and a.num != " + zdsl + " "); 
    }
       
    try
    {
      list = (ArrayList)sessionManager.executeQuery(querysql.toString(), new MapListProcessor());
    }
    catch (BusinessException e) {
      e.printStackTrace();
    }
    
    if ((list != null) && (list.size() > 0)) {
      String invcode = "";

      final List vlist = new ArrayList();
      final List numlist = new ArrayList();
      final List cwarelist = new ArrayList();
      List cwarenamelist = new ArrayList();
      List csnamelist = new ArrayList();
      final List carglist = new ArrayList();
      final List cspaceidlist = new ArrayList();
      final List vbatlist = new ArrayList();
      Double sumnum = new Double(0.0D);
      
      ArrayList<OnhandVO> handvoList = new ArrayList<OnhandVO>();
      String invname = "";
      try
      {
        invname = (String)HYPubBO_Client.findColValue("bd_invmandoc a,bd_invbasdoc b", "b.invname", "a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and a.dr=0 and b.dr=0 and a.pk_invmandoc='" + pkinv + "'");
      }
      catch (UifException e)
      {
        e.printStackTrace();
      }
      for (int i = 0; i < list.size(); i++) {
        Map map = (Map)list.get(i);
        invcode = map.get("invcode") == null ? "" : map.get("invcode").toString();

        String vfree1 = map.get("vfree1") == null ? "" : map.get("vfree1").toString();
        String vbatchcode = map.get("vbatchcode") == null ? "" : map.get("vbatchcode").toString();
        String cwarehouseid = map.get("cwarehouseid") == null ? "" : map.get("cwarehouseid").toString();
        String csname = map.get("csname") == null ? "" : map.get("csname").toString();
        String num = map.get("num") == null ? "" : map.get("num").toString();
        String cspaceid = map.get("cspaceid") == null ? "" : map.get("cspaceid").toString();
        String shortname = "";
        StringBuffer sqlhouse = new StringBuffer();
        sqlhouse.append(" select storname from bd_stordoc ")
          .append(" where pk_stordoc = '" + cwarehouseid + "' ");

        String carg = "";
        StringBuffer cargsql = new StringBuffer();
        cargsql.append(" select cscode  from bd_cargdoc ")
          .append(" where pk_cargdoc = '" + cspaceid + "' ");
        try
        {
          shortname = (String)sessionManager.executeQuery(sqlhouse.toString(), new ColumnProcessor());
          carg = (String)sessionManager.executeQuery(cargsql.toString(), new ColumnProcessor());
        }
        catch (BusinessException e) {
          e.printStackTrace();
        }
        vlist.add(vfree1);
        numlist.add(num);
        sumnum = Double.valueOf(sumnum.doubleValue() + Double.valueOf(num).doubleValue());
        cwarelist.add(cwarehouseid);
        cwarenamelist.add(shortname);
        csnamelist.add(csname);
        carglist.add(carg);
        vbatlist.add(vbatchcode);
        cspaceidlist.add(cspaceid);
        
        if("1108".equals(corp)){
        	OnhandVO handZgVo = new OnhandVO();
            handZgVo.setSerialNum(i);
            handZgVo.setBodyname("上海宝钢制盖库存组织");
            handZgVo.setStorname(shortname);
            handZgVo.setInvname(invname);
            handZgVo.setNum(num);
            handZgVo.setVbatchcode(vbatchcode);
            handZgVo.setVfree(vfree1);
            handZgVo.setCinventoryid(pkinv);
            handZgVo.setCspaceid(cspaceid);
            handZgVo.setInvcode(invcode);
            handZgVo.setCwarehouseid(cwarehouseid);
            handvoList.add(handZgVo);
        }
      }

      

      final String inv = invname;
      int len = cwarenamelist.size();
      for (int i = 0; i < len - 1; i++) {
        String temp = (String)cwarenamelist.get(i);
        for (int j = i + 1; j < len; j++) {
          if (temp.equals(cwarenamelist.get(j))) {
            cwarenamelist.remove(j);
            j--;
            len--;
          }
        }
      }
      StringBuffer cname = new StringBuffer();
      for (int m = 0; m < len; m++) {
        cname.append((String)cwarenamelist.get(m) + ", ");
      }

      int len1 = csnamelist.size();
      for (int i1 = 0; i1 < len1 - 1; i1++) {
        String temp1 = (String)csnamelist.get(i1);
        for (int j = i1 + 1; j < len1; j++) {
          if (temp1.equals(csnamelist.get(j))) {
            csnamelist.remove(j);
            j--;
            len1--;
          }
        }
      }
      StringBuffer cn = new StringBuffer();
      for (int m = 0; m < len1; m++) {
        cn.append((String)csnamelist.get(m) + ", ");
      }
      //弹出是否并垛框内的显示信息
      if (list.size() > 0) {
        err.append("存货:")
          .append(invname + "，")
          .append("存在零头垛垛数:")
          .append(list.size() + "，")
          .append("总数量:")
          .append(sumnum + "，")
          .append("仓库:")
          .append(cname );
//          .append("库位:")
//          .append(cn);
      }
      //点击‘是’后的事件处理
      if (showYesNoMessage(err.toString() + "是否合并？") == 4) {
       /* final String bz = getBillCardPanel().getHeadItem("bz").getValueObject() == null ? "" : 
          getBillCardPanel().getHeadItem("bz").getValueObject().toString();*/
//        final String scx = getScx();
        /*final String scx = getBillCardPanel().getHeadItem("scx").getValueObject() == null ? "" : 
            getBillCardPanel().getHeadItem("scx").getValueObject().toString();*/
    	  String mplatform = getBillCardPanel().getHeadItem("mplatform").getValueObject() == null ? "" : 
              getBillCardPanel().getHeadItem("mplatform").getValueObject().toString();
            
    	  final String zdslnum = zdsl;
        final String cpid = getBillCardPanel().getHeadItem("invname").getValueObject() == null ? "" : 
          getBillCardPanel().getHeadItem("invname").getValueObject().toString();
        String pgy = getBillCardPanel().getHeadItem("qualitycontroller").getValueObject() == null ? "" : 
            getBillCardPanel().getHeadItem("qualitycontroller").getValueObject().toString();
        //add by zy 2018-08-15 
        /*String djzt = getBillCardPanel().getHeadItem("djzt").getValueObject() == null ? "" : 
            getBillCardPanel().getHeadItem("djzt").getValueObject().toString();*/
        /*String llh = getBillCardPanel().getHeadItem("llh").getValueObject() == null ? "" : 
            getBillCardPanel().getHeadItem("llh").getValueObject().toString();*/
        
        //end by zy
        PfLinkData linkData = new PfLinkData();
        linkData.setUserObject(new Object[] {zdslnum, cpid });
         /**
          * add by gt 2019-08-12
          */
        //判断当前公司是不是10309公司1108   1016
        if("1108".equals(corp)){
        	//若是则点击‘是’后弹出查询弹框
        try {
        	new HgzHandler(billUI, ctrl).onQueryProducts(zdslnum, cpid, pgy,handvoList,mplatform);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }/*else{
		//不是则跳转到库存隔离单界面
        SFClientUtil.openLinkedADDDialog("40081001", this, new ILinkAddData()
        {
          public String getSourceBillID()
          {
            return Toolkits.getString(HgzUI.this.getBillCardPanel().getHeadItem("pk_glzb").getValueObject());
          }

          public String getSourceBillType()
          {
            return "53";
          }

          public String getSourcePkOrg()
          {
            return null;
          }

          public Object getUserObject()
          {
            return new Object[] {zdslnum, cpid, vlist, numlist, cwarelist, inv, carglist, vbatlist, cspaceidlist };
          }
        });
      }*/
		/**--gt end--*/
      }
      else;
    }
  }
	//end by zy
	
	
	//add by zwx 2019-9-5 展示现存量结果及选择行推送库存隔离单
	public void onQuery2(String zdslnum, String cpid, String pgy,ArrayList handvoList,String scx) throws Exception{
		 
		 try {
			 OnhandVO[] results;
	    	  ShowHandResult wriui=new ShowHandResult(this,handvoList);
			
				if (wriui.showModal()==1) {
					//newaccount=wriui.getUITextField1().getText();//得到新账号
					results=wriui.getResults();
					if(results.length==0){
						MessageDialog.showErrorDlg(this, "错误","请选择行！");
						return;
					}
					//把选中弹框中的数据点击确定后直接插入到库存隔离单表中 add by zy 2019.8.12
					String corp = PoPublicUIClass.getLoginPk_corp();
					if("1108".equals(corp))
					{
						IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
						UFDate date = PoPublicUIClass.getLoginDate();//获取当前时间
						final String pkCorp = getCorpPrimaryKey();
						GlBillVO aggvo = new GlBillVO();
						final GlHeadVO glHeadVo = new GlHeadVO();//表头（表“mm_glzb”）
						glHeadVo.setXxrq(date);
						glHeadVo.setBillsign("isolation");//订单状态 应为isolation
						glHeadVo.setPk_corp(pkCorp);//主表pk_corp
						glHeadVo.setXxry(pgy);//品控员
						String dh = new HYPubBO().getBillNo("53", corp, null, null);//生成单号
						glHeadVo.setBillno(dh);//单号
//						String hid = ivo.insertVO(glHeadVo);
						aggvo.setParentVO(glHeadVo);
						GlItemBVO[] glvos = new GlItemBVO[results.length];
						
						String invname = "";
						String invcode = "";
						final ArrayList batchList = new ArrayList();
						final ArrayList vfreeList = new ArrayList();
						final ArrayList numList = new ArrayList();
						final ArrayList carglist = new ArrayList();
						final String zdnum = zdslnum;//整垛数量
						String ck = "";
						for(int m=0;m<results.length;m++){
							final String materialNo = results[m].getInvcode() == null ? "" : results[m].getInvcode();
							final String product = results[m].getInvname() == null ? "" : results[m].getInvname();
							invname = product;
							invcode = materialNo;
							
							final String batchNo = results[m].getVbatchcode() == null ? "" : results[m].getVbatchcode();
							batchList.add(batchNo);
							
							final String cribNo = results[m].getVfree() == null ? "" : results[m].getVfree();
							vfreeList.add(cribNo);
							
							String num = results[m].getNum();
							final int nummm = new Double(num).intValue();
							numList.add(nummm);
							
							String warehouseid = results[m].getCwarehouseid() == null ? "" : results[m].getCwarehouseid();
							ck = warehouseid;
							String chglid = results[m].getCinventoryid() == null ? "" : results[m].getCinventoryid();
							
							String carg = results[m].getCspaceid() == null ? "" : results[m].getCspaceid();
							carglist.add(carg);
							GlItemBVO glBodyVo = new GlItemBVO();//表体（表“mm_glzb_b”）
							glBodyVo.setLh(materialNo);//料号
							glBodyVo.setXxaglsl(nummm);//隔离数量
							glBodyVo.setCp(product);//产品名称
							glBodyVo.setIsolationcpid(chglid);//隔离产品id
							glBodyVo.setIsolationckid(warehouseid);//隔离仓库id
							glBodyVo.setPh(batchNo);//批号
							glBodyVo.setDh(cribNo);//垛号
							glBodyVo.setGlyy("并垛");//隔离原因
							glBodyVo.setPk_corp(pkCorp);//子表pk_corp
//							glBodyVo.setPk_glzb(hid);
							glvos[m]=glBodyVo;
//							final String djState = djzt;//单据状态
//							final String llNum = llh;//老料号
						}
						aggvo.setChildrenVO(glvos);
//						String[] bids = ivo.insertVOList(glvos);
						try {
							aggvo = (GlBillVO) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", "53", new UFDate().toString(), aggvo);
						} catch (BusinessException e) {
							MessageDialog.showErrorDlg(this, "库内隔离单保存异常",e.getMessage());
							return;
						}
							
						/*//根据cpid查询产品名称
						String sql = "select b.invname from bd_invmandoc m inner join bd_invbasdoc b " +
								"on m.pk_invbasdoc=b.pk_invbasdoc where m.pk_invmandoc='"+cpid+"'";
						IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						ColumnListProcessor alp = new ColumnListProcessor();
						String cpo = null;
						List cplist = null;
						cplist = (List) query.executeQuery(sql, alp);
						if(cplist!=null && cplist.size()>0){
				    		cpo = cplist.get(0) == null ? "" : cplist.get(0).toString();
				    	}*/
						final String cp = invname;//产品名称
						final String cpcode = invcode;//产品编码
						final String cinvid = cpid;
						final String ckstr = ck;
						final String sscx = scx;//生产线
						final String pgyid = pgy;
						final ArrayList glbList = new ArrayList();
						final String glhpk = aggvo.getParentVO().getPrimaryKey();
						GlItemBVO[] bvos = (GlItemBVO[]) aggvo.getChildrenVO();
						for(int k = 0;k<bvos.length;k++){
							GlItemBVO bvo = bvos[k];
							glbList.add(bvo.getPrimaryKey());
						}
						final String glh = glhpk;
						//打开良品尾数确认【隔离处理单】节点新增状态
						SFClientUtil.openLinkedADDDialog("50081002", this, new ILinkAddData()
						{
							public String getSourceBillID() {
								return Toolkits.getString(glHeadVo.getPk_glzb());
							}
							public String getSourceBillType() {
								return "GLCL";
							}
							public String getSourcePkOrg() {
								return null;
							}
							public Object getUserObject() {
								return new Object[] {pkCorp,cinvid,cpcode,cp,batchList,vfreeList,zdnum,numList,ckstr,carglist,glh,glbList,sscx,pgyid};
							}
						});
						
					}
					//end by zy
				}
				
			
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, "异常",e.getMessage());
			return;
		}
		
	
	  }
	//end by zwx
	
	
	/**
	 * 机台号定义对应值，用于条码组装
	 * @return
	 */
	public HashMap getMachine(){
		HashMap map = new HashMap();
		map.put("01", "S8-1");
		map.put("02", "S8-2");
		map.put("03", "T-2");
		map.put("04", "T-3");
		map.put("05", "T-4");
		return map;
	}
	
	
	/**
	 * 根据机台号=生产线名称过滤生产订单号
	 */
	public void getScxScddh(){
		
		String platformnum = getBillCardPanel().getHeadItem("mplatform").getValueObject()==null?
					"": getBillCardPanel().getHeadItem("mplatform").getValueObject().toString();
		String plat = getMachine().get(platformnum)==null?"":getMachine().get(platformnum).toString();
		if(plat.length()>0){
			StringBuffer sql = new StringBuffer();
			sql.append(" select pk_wkid ") 
			.append(" 		  from pd_wk ") 
			.append(" 		 where pk_corp = '1108' ") 
			.append(" 		   and gzzxbm = '"+plat+"' ") 
			.append(" 		   and nvl(dr, 0) = 0 ") ;
			
			IUAPQueryBS sessionManager = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				String scxid = sessionManager.executeQuery(sql.toString(), new ColumnProcessor())==null?"":(String)sessionManager.executeQuery(sql.toString(), new ColumnProcessor());
				if(scxid.length()>0){
					UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("produceorder").getComponent();
					refpane.getRefModel().addWherePart(" and mm_mokz.gzzxid  = '"+scxid+"' ");
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
