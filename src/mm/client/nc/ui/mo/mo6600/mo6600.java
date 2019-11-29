package nc.ui.mo.mo6600;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.mm.pub.pub1010.WkRefModel;
import nc.ui.mo.mo0451.BzRefModel;
import nc.ui.pf.pub.Toolkit;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.uif.pub.exception.UifException;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class mo6600 extends BillManageUI
{ 


	public mo6600() throws Exception
	{
		initialComponent();
		Object[] obj = this.getButtons();
		this.setBillOperate(IBillOperate.OP_INIT); 
	}
	
//	public void setTotalUIState(int intOpType)throws Exception
//	{ 
//		super.setTotalUIState(intOpType);
//		GlBillVO billvo = (GlBillVO) this.getBufferData().getCurrentVO();
//		if(billvo!=null)
//		{
//			GlHeadVO head = (GlHeadVO) billvo.getParentVO();
//			if(head!=null)
//			{
//				head.setVbillStatus(intOpType);
//			}
//		}
		 
//	}
	
	private void initialComponent() throws Exception
	{
		
		//生产线(工作中心)初始化
//		BillItem item = this.getBillCardPanel().getHeadItem("scx");
		UIRefPane pane =  (UIRefPane) this.getBillCardPanel().getHeadItem("scx").getComponent();
		pane.setIsCustomDefined(true);
		pane.setRefModel(new WkRefModel());
		StringBuffer where = new StringBuffer(" and pd_wk.pk_corp = '"); 
		where.append(getUnitPK());
//		where.append("' and pd_wk.gcbm = '");//.append(getCalbodyPK()).append("'");
//		where.append(ClientEnvironment.getInstance().getFactoryVO().getPrimaryKey());
		where.append("'");
		pane.getRefModel().addWherePart(where.toString());
 
//		//班组初始化
		pane =  (UIRefPane) this.getBillCardPanel().getHeadItem("bz").getComponent();
		pane.setIsCustomDefined(true);
		pane.setRefModel(new BzRefModel());
		pane.getRefModel().setWherePart((new StringBuilder()).append("where pk_corp = '").append(getUnitPK()).append("'").toString());
//				.append("' and gcbm = '").append(getCalbodyPK()).append("'").toString());
        
		this.getBillCardWrapper().initHeadComboBox("djzt", new String[]{Transformations.getLstrFromMuiStr("新建","New"),Transformations.getLstrFromMuiStr("已确认","Confirm")}, true);
		this.getBillListWrapper().initHeadComboBox("djzt", new String[]{Transformations.getLstrFromMuiStr("新建","New"),Transformations.getLstrFromMuiStr("已确认","Confirm")}, true);
		
		this.getBillCardWrapper().initHeadComboBox("jyjg", new String[]{Transformations.getLstrFromMuiStr("合格","Qualified"),Transformations.getLstrFromMuiStr("隔离","Isolation")}, true);
		this.getBillListWrapper().initHeadComboBox("jyjg", new String[]{Transformations.getLstrFromMuiStr("合格","Qualified"),Transformations.getLstrFromMuiStr("隔离","Isolation")}, true);
		
		
		this.getBillCardWrapper().initHeadComboBox("clzt", new String[]{Transformations.getLstrFromMuiStr("已处理","Processed"),Transformations.getLstrFromMuiStr("未处理","Unprocessed")}, true);
		this.getBillListWrapper().initHeadComboBox("clzt", new String[]{Transformations.getLstrFromMuiStr("已处理","Processed"),Transformations.getLstrFromMuiStr("未处理","Unprocessed")}, true);
		
	    pane =  (UIRefPane) this.getBillCardPanel().getHeadItem("scddh").getComponent(); //产品单据号
	    nc.ui.ref.OrderRefModel  refModel= new nc.ui.ref.OrderRefModel(){
	    	@Override
	    	public String getWherePart()
	        {
	            String where = (new StringBuilder(" nvl(mm_mo.dr,0)=0  and mm_mo.zt = 'B' and mm_mo.pk_cORp='")).append(getUnitPK()).append("'")
//	            .append(" and scxid='").append(getScx()).append("'")
	            .toString(); 
	            return where;
	        } 
	    };
	    pane.setRefModel(refModel);
	      
	    this.getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    
	    this.getBillCardPanel().setRowNOShow(true);
		this.getBillCardPanel().setTatolRowShow(true);
	}
	private String getScx()
	{
		return this.getBillCardPanel().getHeadItem("scx").getValue(); 
		 
	}
	private String getUnitPK()
	{
		return _getCorp().getPrimaryKey(); //ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
	}
	  
	@Override 
	protected void initPrivateButton()
	{
		ButtonVO btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("货位批改","Cargo space marking"));
		btn.setBtnCode("货位批改");
		btn.setBtnChinaName("货位批改");
		btn.setBtnNo(IButton.Update_Num); 
		btn.setOperateStatus(new int[]{0,1,3});
		this.addPrivateButton(btn);
		
		//add by yhj 2014-03-11
//		btn = new ButtonVO();
//		btn.setBtnName("垛号排序");
//		btn.setBtnChinaName("垛号排序");
//		btn.setBtnNo(IButton.SORT_NO);
//		this.addPrivateButton(btn);
		//end
		
		/*2019-07-31
		 * 刘信彬
		 * 
		 */
		btn=new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("快速增行","Offline"));
		btn.setBtnChinaName("快速增行");
		btn.setBtnNo(IButton.Offline);
		this.addPrivateButton(btn);
		
		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("确认","Confirm"));
//		btn.setBtnCode("确认");
		btn.setBtnChinaName("确认");
		btn.setBtnNo(IButton.Confirm);
 
		btn.setOperateStatus(new int[]{0,1,2,IBillOperate.OP_REFADD});
		btn.setBusinessStatus(new int[]{8});
		btn.setExtendStatus(new int[]{8});
		this.addPrivateButton(btn);
		this.addPrivateButton(createButtonVO(IButton.Print,"Print",Transformations.getLstrFromMuiStr("打印","Print")));

		
	}
	protected void setHeadItemValue(String key,Object value)
	{
		this.getBillCardPanel().getHeadItem(key).setValue(value); 
	}
	
	protected BillItem getHeadItem(String key)
	{
		return this.getBillCardPanel().getHeadItem(key);
	}
 
	
	protected void updateBodyValue()
	{ 
		UITable table = this.getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanel().getBillModel();
		for(int i=0;i<table.getRowCount();i++)
		{
			UIRefPane pane = (UIRefPane) this.getBillCardPanel().getHeadItem("cp").getComponent();
			model.setValueAt(pane.getRefName(), i, "cp"); 
			model.setValueAt(pane.getRefValue("bd_invbasdoc.invcode"), i,"lh");
			if(model.getValueAt(i, "pk_glzb_b")!=null||!String.valueOf(model.getValueAt(i, "pk_glzb_b")).equals("")||String.valueOf(model.getValueAt(i, "pk_glzb_b")).equalsIgnoreCase("null"))
			{
				model.setRowState(i, BillModel.MODIFICATION);
			}
			this.getBillCardPanel().execBodyFormula(i, "lh");
		}
	}
	/**
	 * 编辑后事件，根据生产订单号从中间表里面取数据带到界面
	 * by src 2018年4月11日15:40:17
	 * @param scddh
	 * @throws BusinessException
	 */
	public void getBzfs(String pk_moid) throws BusinessException{
		IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select def1 from bd_bzfs where pk_moid = '"+pk_moid+"' and pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		List list = (List) uap.executeQuery(sql, new MapListProcessor());
		Map map = null;
		if(list.size()>0){
			map = (Map) list.get(0);
			Object zdsl = map.get("def1");
			String zdsl1 = getHeadItem("zdsl").getValue()==null?"":getHeadItem("zdsl").getValue().toString();
			if(zdsl1.equals("")){
			   setHeadItemValue("zdsl",zdsl);
			}
		}
	}
	
	@Override
	public void afterEdit(BillEditEvent event) 
    {
		if(event.getKey().equalsIgnoreCase("scddh"))
		{
			try
			{  
				String pkValue  = ((UIRefPane) event.getSource()).getRefPK();
				if(pkValue!=null)
				{
					MoHeaderVO heads[] = MMProxy.getRemoteMO().queryMoByWhere(" and mm_mo.pk_moid='"+pkValue+"'"); 
					if(heads!=null&&heads.length==1)
					{
						String pk_invmandoc = (String)HYPubBO_Client.findColValue("bd_invmandoc", "pk_invmandoc", " nvl(dr,0)!=1 and pk_corp='"+this.getUnitPK()+"' and pk_invbasdoc='"+heads[0].getWlbmid()+"'");
						setHeadItemValue("cp",pk_invmandoc);
					
						setHeadItemValue("ddsl",heads[0].getJhwgsl().intValue());
						setHeadItemValue("ddwgsl",heads[0].getSjwgsl().intValue());
						setHeadItemValue("ddwwgsl",heads[0].getJhwgsl().sub(heads[0].getSjwgsl()).intValue());
						this.getBillCardPanel().execHeadTailEditFormulas();
						/*if(heads[0].getKsid()!=null&&!heads[0].getKsid().equals(""))
						{
							setHeadItemValue("ks",heads[0].getKsid());						
							String zdsl = (String)HYPubBO_Client.findColValue(" bd_cumandoc a,bd_cubasdoc b", "b.def5", " a.pk_cubasdoc=b.pk_cubasdoc and a.pk_cumandoc='"+heads[0].getKsid()+"'");
							if(zdsl!=null)
							{
								setHeadItemValue("zdsl",Integer.parseInt(zdsl));
							}else
							{
								setHeadItemValue("zdsl",null);
							}
						}*/
						
						//add by zwx 2019-8-15 制盖整垛数量通过生产订单数量传递
						if((getCorpPrimaryKey().equals("1078")||getCorpPrimaryKey().equals("1108"))&&heads!=null&&heads.length==1){
							String zdsl = heads[0].getZdy2()==null?"0":heads[0].getZdy2();
							setHeadItemValue("zdsl",Integer.parseInt(zdsl));
						}
						//end by zwx
						//更新表体数据
						updateBodyValue();
						
					}
				}
				else 
				{
					setHeadItemValue("ddsl",null);
					setHeadItemValue("ddwgsl",null);
					setHeadItemValue("ddwwgsl",null);
					setHeadItemValue("ks",null);
					setHeadItemValue("cp",null);
					setHeadItemValue("zdsl",null);
					//更新表体数据
					updateBodyValue();
				}
				//add by src 2018年4月16日17:44:11
				getBzfs(pkValue);
			}catch (Exception e)
			{
				// TODO Auto-generated catch block
				Logger.error(e);
				e.printStackTrace();
			}
			//add by zwx 判断是否合并垛 2016-3-3 
			isMerge();
			//end by zwx 
		}else if(event.getKey().equalsIgnoreCase("scx"))
		{ 
			try
			{
				UIRefPane pane = (UIRefPane) this.getBillCardPanel().getHeadItem("scddh").getComponent(); //产品单据号
			    nc.ui.ref.OrderRefModel  refModel= new nc.ui.ref.OrderRefModel(){
			    	@Override
			    	public String getWherePart()
			        {
			            String where = (new StringBuilder(" nvl(mm_mo.dr,0)=0  and mm_mo.zt = 'B' and mm_mo.pk_corp='")).append(getUnitPK()).append("'")
			            .append(" and pk_moid in (select pk_moid from mm_mokz where gzzxid='").append(getScx()).append("')")
			            .toString(); 
			            return where;
			        } 
			    };
			    pane.setRefModel(refModel);
				
				StringBuilder where  = new StringBuilder(" a.pk_moid=b.pk_moid and nvl(a.dr,0)=0  and a.zt = 'B' and a.pk_corp='");
				where.append(getUnitPK()).append("'");
				where.append(" and b.gzzxid='").append(getScx()).append("' order by a.jhkgrq,a.jhkssj");
				String pk_moid = (String)HYPubBO_Client.findColValue("mm_mo a,mm_mokz b", "a.pk_moid", where.toString());
				setHeadItemValue("scddh",pk_moid);
				//触发项目编辑后事件
				BillEditEvent events = new BillEditEvent(getHeadItem("scddh").getComponent(),pk_moid,"scddh");
				afterEdit(events); 
				 
			} catch (UifException e)
			{
				// TODO Auto-generated catch block
				Logger.error(e);
				e.printStackTrace();
			}
		}else if(event.getKey().equalsIgnoreCase("hw"))
		{
			UIRefPane pane = (UIRefPane) this.getBillCardPanel().getBodyItem("hw").getComponent(); 
			
			UITable table = this.getBillCardPanel().getBillTable();
			BillModel model = this.getBillCardPanel().getBillModel();
			int row = table.getSelectedRow();
			  
			model.setValueAt(pane.getRefName(), row, "hwmc"); 
		}else if(event.getKey().equalsIgnoreCase("dh"))
		{
			if (checkDh()==true)
			{
				addNewLine(event);
				//add by shikun 2014-03-28 表体垛号编辑后汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
				bodyToHeadNum();
				//end shikun 
			}
		}

		//add by shikun 2014-03-28 表体下线/隔离数量xxaglsl编辑后汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
		else if(event.getKey().equalsIgnoreCase("xxaglsl")) {
			bodyToHeadNum();
		}
		//end shikun 
    }
	
	/**
	 * add by shikun 2014-03-28 表体垛号编辑后汇总表体下线/隔离数量xxaglsl递增表头订单未完工数量ddwwgsl
	 * */
	public void bodyToHeadNum() {
		int ddsl = getBillCardPanel().getHeadItem("ddsl").getValueObject()==null?0
                :Integer.valueOf(getBillCardPanel().getHeadItem("ddsl").getValueObject().toString());
		int ddwgsl = getBillCardPanel().getHeadItem("ddwgsl").getValueObject()==null?0
		                  :Integer.valueOf(getBillCardPanel().getHeadItem("ddwgsl").getValueObject().toString());
		int ddwwgsl = ddsl-ddwgsl;
		int row = getBillCardPanel().getBillTable().getRowCount();
		for (int i = 0; i < row; i++) {
			Object oxxaglsl = getBillCardPanel().getBodyValueAt(i, "xxaglsl");
			int xxaglsl = oxxaglsl==null?0:Integer.valueOf(oxxaglsl.toString());
			ddwwgsl = ddwwgsl-xxaglsl;
		}
		if (ddwwgsl<0) {
			showWarningMessage("已超出订单数量，请修改表体行“下线/隔离数量”。");
		}
		getBillCardPanel().setHeadItem("ddwwgsl", ddwwgsl);
	}

	protected void addNewLine(BillEditEvent event)
	{
		UITable table = this.getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanel().getBillModel();
		this.getBillCardPanel().addLine();
		int curCount = table.getRowCount();
		int curRow = event.getRow();
		int colCount = model.getColumnCount();
		int curColumn = model.getBodyColByKey(event.getKey());

		for(int i=0;i<colCount;i++)
		{
			if(i==curColumn)
			{
				continue;
			}
			Object oldValue = model.getValueAt(curRow, i);
			model.setValueAt(oldValue, curCount-1, i);
		} 
		//垛号自动获取焦点
		String dhName = this.getBillCardPanel().getBodyItem("dh").getName();
		int colIndex = table.getColumnModel().getColumnIndex(dhName);
		table.editCellAt(curCount-1, colIndex);
		
		Thread thread = new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(200);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//垛号自动获取焦点
				UITable table = getBillCardPanel().getBillTable(); 
				String dhName = getBillCardPanel().getBodyItem("dh").getName();
				int colIndex = table.getColumnModel().getColumnIndex(dhName);
				table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
				table.setColumnSelectionInterval(colIndex, colIndex);
				table.editCellAt(table.getRowCount()-1, colIndex);
			}
		};
		thread.start();
		
	}
	protected ManageEventHandler createEventHandler()
	{ 
		return new MyEventHandler(this, getUIControl());
	}

	 
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception
	{
		
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception
	{
		 
	}
	
	protected void setListBodyData()  throws Exception
	{ 
		super.setListBodyData();
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception
	{
	}

	protected void initSelfData()
	{
	}

	public void setDefaultData() throws Exception
	{
		setHeadItemValue("pk_corp", this.getUnitPK());
		setHeadItemValue("billno", Toolkit.getBillNO("JYDJ", this.getUnitPK(), null, null));
		getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("scx"));
		setHeadItemValue("xxsj",ClientEnvironment.getServerTime().getTime());
	}

	@Override
	protected AbstractManageController createController()
	{
		// TODO Auto-generated method stub
		return new mo6600Ctrl();
	}
	
	protected boolean checkDh()
	{ 
		UITable table = this.getBillCardPanel().getBillTable();
		BillModel model = this.getBillCardPanel().getBillModel();
		
		int row = table.getSelectedRow();		
		String newDh = (String)model.getValueAt(row, "dh");
		
		if(newDh!=null){
			for(int i=0; i<table.getRowCount(); i++)
			{
				if(i!=row)
				{
					String dh = (String)model.getValueAt(i, "dh");
					if (dh !=null){
						if(dh.trim().equals(newDh.trim()))
						{
							MessageDialog.showErrorDlg(this, "错误Error",
									Transformations.getLstrFromMuiStr(new StringBuilder("垛号@Stack number&[").append(newDh.trim()).append(Transformations.getLstrFromMuiStr("]&重复！@Repeat!")).toString()));
								
							    model.setValueAt("", row, "dh"); 
								setColumn(row, "dh");
								
								return false;
						}
					}
				}			
			}	
			
			return true;
		}
		
		return false;
	}
	
	   //设置输入焦点
	private void setColumn(int row, String col)
	{
		UITable table = this.getBillCardPanel().getBillTable();
		//BillModel model = this.getBillCardPanel().getBillModel();
		
		String colName = this.getBillCardPanel().getBodyItem(col).getName();
		
		int colIndex = table.getColumnModel().getColumnIndex(colName);
		
		table.setRowSelectionInterval(row, row);
		table.setColumnSelectionInterval(colIndex, colIndex);
		table.editCellAt(row, colIndex);
		table.setEditingRow(row);
		table.setEditingColumn(colIndex);
	}
	public  ButtonVO createButtonVO(int id, String code, String name) {
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(name);
		btn.setHintStr(name);
		btn.setBtnCode(code);
		btn.setBtnChinaName(code);
		if(code.equalsIgnoreCase("print"))
		{
			btn.setOperateStatus(new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
			//btn.setBusinessStatus(new int[]{});
		}
		return btn;
	}
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getKey().equalsIgnoreCase("hw")) {
	
		String cwarehouseid = (String) getBillCardPanel().getHeadItem("ck").getValueObject();
		if (cwarehouseid == null)
			return false ;
		UIRefPane pane = (UIRefPane) getBillCardPanel().getBodyItem(e.getKey()).getComponent();
		// 按照仓库过滤货位参照
		filterCargdocRefModel(pane, cwarehouseid);
		return true;
	}
		return true;
	}
	private void filterCargdocRefModel(UIRefPane pane, String cwarehouseid) {
		if (pane == null || pane.getRefModel() == null)
			return;
		AbstractRefModel model = pane.getRefModel();
		model.addWherePart(" and bd_cargdoc.pk_stordoc = '" + cwarehouseid + "' ");
	}
	
	//add by zwx 2016-3-3 根据选择的生产订单的料号，如果本仓库内有零头剁情况提示是否并剁，点“是”进入隔离处理界面，系统带入零头剁数据
	@SuppressWarnings("unchecked")
	public void isMerge()
  {
    String corp = getUnitPK();
    StringBuffer querysql = new StringBuffer();
    String cal = "";
    ArrayList list = new ArrayList();
    StringBuffer err = new StringBuffer();
    String zdsl = getBillCardPanel().getHeadItem("zdsl").getValueObject() == null ? "0" : 
      getBillCardPanel().getHeadItem("zdsl").getValueObject().toString();
    String pkinv = getBillCardPanel().getHeadItem("cp").getValueObject() == null ? "" : 
      getBillCardPanel().getHeadItem("cp").getValueObject().toString();
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
      .append("    and (a.num > 0)   ")
      .append("    and a.num != " + zdsl + " ");
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
      }

      String invname = "";
      try
      {
        invname = (String)HYPubBO_Client.findColValue("bd_invmandoc a,bd_invbasdoc b", "b.invname", "a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and a.dr=0 and b.dr=0 and a.pk_invmandoc='" + pkinv + "'");
      }
      catch (UifException e)
      {
        e.printStackTrace();
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
      if (list.size() > 0) {
        err.append("存货:")
          .append(invname + "，")
          .append("存在零头垛垛数:")
          .append(list.size() + "，")
          .append("总数量:")
          .append(sumnum + "，")
          .append("仓库:")
          .append(cname + "\n")
          .append("库位:")
          .append(cn);
      }

      if (showYesNoMessage(err.toString() + "是否合并？") == 4) {
        final String bz = getBillCardPanel().getHeadItem("bz").getValueObject() == null ? "" : 
          getBillCardPanel().getHeadItem("bz").getValueObject().toString();
        final String scx = getScx();
        final String zdslnum = getBillCardPanel().getHeadItem("zdsl").getValueObject() == null ? "" : 
          getBillCardPanel().getHeadItem("zdsl").getValueObject().toString();
        final String cpid = getBillCardPanel().getHeadItem("cp").getValueObject() == null ? "" : 
          getBillCardPanel().getHeadItem("cp").getValueObject().toString();
        PfLinkData linkData = new PfLinkData();
        linkData.setUserObject(new Object[] { bz, scx, zdslnum, cpid });

        SFClientUtil.openLinkedADDDialog("40081001", this, new ILinkAddData()
        {
          public String getSourceBillID()
          {
            return Toolkits.getString(mo6600.this.getBillCardPanel().getHeadItem("pk_glzb").getValueObject());
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
            return new Object[] { bz, scx, zdslnum, cpid, vlist, numlist, cwarelist, inv, carglist, vbatlist, cspaceidlist };
          }
        });
      }
      else;
    }
  }
}
