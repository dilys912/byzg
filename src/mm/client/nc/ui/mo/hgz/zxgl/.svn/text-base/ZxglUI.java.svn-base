package nc.ui.mo.hgz.zxgl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import nc.ui.mo.hgz.ShowHandResult;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
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
 * 在线隔离=合格证打印（制盖）界面入口类
 * @author 刘信彬
 *
 */

public class ZxglUI extends BillManageUI {
	
	//add by gt 2019-08-12
	BillManageUI billUI;
	AbstractManageController ctrl;
    //--gt end--

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ZxglController();
	}
	
	
	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new ZxglHandler(this,new ZxglController());
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
		//在线隔离 批号生成规则：年月日时分秒
		String time = _getServerTime().getDate().toString().replace("-", "").concat(_getServerTime().getUFTime().toString().replace(":", ""));
		getBillCardPanel().setHeadItem("batchnumbercode", time);
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
			/*//add by zy 2019-08-26
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
			*/
		
		//end by zy
			
		}else if(e.getKey().equals("productname")){//产品
			autoSelect();
		}
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
//			getBillCardPanel().getHeadItem("batchnumbercode").setValue(hgz.getBatchnumbercode());//批次号
			/*String tph = getNewTph(hgz.getPalletnumber()==null?"":hgz.getPalletnumber());
			getBillCardPanel().getHeadItem("palletnumber").setValue(tph);//托盘号
*/			//add by zy 2019-09-29 增加判断：用户重新选择“机台”后，“生产订单号”清空
			getBillCardPanel().getHeadItem("produceorder").setValue("");//生产订单号
			//end
		}else{
			getBillCardPanel().getHeadItem("palletnumber").setValue("");//无值则清空
		}
		
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
			.append("            and vdef7 = '在线隔离' ") 
			.append("          order by ts desc) ") 
			.append("  where rownum = 1 ") ;

		}else{
			sql.append(" select * ") 
			.append("   from (select * ") 
			.append("           from mm_hgz ") 
			.append("          where nvl(dr, 0) = 0 ") 
			.append("            and pk_corp = '"+corp+"' ") 
			.append("            and vdef7 = '在线隔离' ") 
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
	
	
	//add by zwx 2019-9-5 展示现存量结果及选择行推送库存隔离单
	public OnhandVO[] onQuery2(String zdslnum, String cpid, String pgy,ArrayList handvoList,String scx){
		 
		 try {
			 OnhandVO[] results;
	    	  ShowHandResult wriui=new ShowHandResult(this,handvoList);
			
				if (wriui.showModal()==1) {
					//newaccount=wriui.getUITextField1().getText();//得到新账号
					results=wriui.getResults();
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
						aggvo = (GlBillVO) nc.ui.pub.pf.PfUtilClient.processAction("SAVE", "53", new UFDate().toString(), aggvo);
							
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
						return results;
					}
					//end by zy
				}
				else{
				return null;
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
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
