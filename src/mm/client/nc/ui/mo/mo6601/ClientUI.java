package nc.ui.mo.mo6601;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.mm.pub.pub1010.WkRefModel;
import nc.ui.mo.mo0451.BzRefModel;
import nc.ui.mo.mo6601.IButton;
import nc.ui.pf.pub.Toolkit;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.mo.mo6601.GlclBillVO;
import nc.vo.mo.mo6601.GlclHeadVO;
import nc.vo.mo.mo6601.GlclItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IExtendStatus;

public class ClientUI extends BillManageUI implements BillEditListener,ILinkAdd
{

	public ClientUI()
	{
		super();
		initialComponent();
	}

	protected void initialComponent()
	{
		// 生产线初始化
		UIRefPane pane = (UIRefPane) this.getBillCardPanel().getHeadItem("scx")
				.getComponent();
		pane.setIsCustomDefined(true);
		pane.setRefModel(new WkRefModel());
		StringBuffer where = new StringBuffer(" and pd_wk.pk_corp = '");
		where.append(getUnitPK());
		where.append("'");
		pane.getRefModel().addWherePart(where.toString());

		// //班组初始化
		pane = (UIRefPane) this.getBillCardPanel().getHeadItem("bz")
				.getComponent();
		pane.setIsCustomDefined(true);
		pane.setRefModel(new BzRefModel(){
			@Override
			public String getWherePart()
			{
				return super.getWherePart();
			}
		});
		pane.getRefModel().setWherePart(
				(new StringBuilder()).append("where pk_corp = '").append(
						getUnitPK()).append("'").toString()); 
		 
		this.getBillCardPanel().setRowNOShow(true);
		this.getBillCardPanel().setTatolRowShow(true);
	}

	@Override
	public void afterEdit(BillEditEvent event)
	{
		try
		{
			if (event.getKey().equals("bz"))
			{
				UIRefPane pane = (UIRefPane) getHeadItem("bz").getComponent();
				String scx = (String) HYPubBO_Client.findColValue("pd_pga",
						"gzzxid", " pk_pgaid='" + pane.getRefPK() + "'");
				getHeadItem("scx").setValue(scx);   
				 
			
			}else if(event.getKey().equals("hgsl"))
			{
				event.getValue();
				Integer dclsl = (Integer) getBodyItemValue(event.getRow(),"dclsl");
				if(dclsl!=null)
				{
					String hgsl = (String)event.getValue();
					if(hgsl==null||hgsl.equals(""))
					{
						hgsl = "0";
					}
					Integer bfsl = dclsl.intValue()-Integer.parseInt(hgsl);
					setBodyItemValue(bfsl,event.getRow(),"bfsl");
				}
				
			}
		} catch (Exception ex)
		{
			Logger.error(ex);
			ex.printStackTrace();
		}
	}

	private void setBodyItemValue(Object value,int row,String strKey)
	{ 
		BillModel model = this.getBillCardPanel().getBillModel();
		model.setValueAt(value, row, strKey);
	}
	
	private Object getBodyItemValue(int row,String strKey)
	{ 
		BillModel model = this.getBillCardPanel().getBillModel();
		return model.getValueAt(row, strKey);
	}
	
	private BillItem getHeadItem(String key)
	{
		return this.getBillCardPanel().getHeadItem(key);
	}

	private String getUnitPK()
	{
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}

	@Override
	protected AbstractManageController createController()
	{
		// TODO Auto-generated method stub
		return new mo6601Ctrl();
	}

	protected ManageEventHandler createEventHandler()
	{
		return new mo6601EventHandler(this, getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0)
			throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void initPrivateButton()
	{

		ButtonVO btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("参照查询","Reference inquiries"));
		//btn.setBtnCode("参照查询");
		btn.setBtnChinaName("参照查询");
		btn.setBtnNo(IButton.Ref_Query);
		btn.setOperateStatus(new int[] { IBillOperate.OP_EDIT, IBillOperate.OP_ADD,IBillOperate.OP_REFADD });
		
	
		this.addPrivateButton(btn);
		btn.setChildAry(new int[] { IButton.Ref_XiaXian, IButton.Ref_KuNei });

		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("下线隔离单参照","Off the isolated form"));
	//btn.setBtnCode("下线隔离单参照");
		btn.setBtnChinaName("下线隔离单参照");
		btn.setBtnNo(IButton.Ref_XiaXian);
		btn.setOperateStatus(new int[] { IBillOperate.OP_EDIT, IBillOperate.OP_ADD,IBillOperate.OP_REFADD});
		this.addPrivateButton(btn);

		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("库内隔离单参照","Storehouse isolated"));
	//	btn.setBtnCode("库内隔离单参照");
		btn.setBtnChinaName("库内隔离单参照");
		btn.setBtnNo(IButton.Ref_KuNei);
		btn.setOperateStatus(new int[] { IBillOperate.OP_EDIT, IBillOperate.OP_ADD,IBillOperate.OP_REFADD});
		this.addPrivateButton(btn);

		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("下线隔离解除","Offline isolation lifted"));
	//	btn.setBtnCode("下线隔离解除");
		btn.setBtnChinaName("下线隔离解除");
		btn.setBtnNo(IButton.Xxgljc);
		btn.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,IBillOperate.OP_NOADD_NOTEDIT });
		btn.setBusinessStatus(new int[]{0});
		btn.setExtendStatus(new int[]{0});
		this.addPrivateButton(btn);

		btn = new ButtonVO();
		btn.setBtnName(Transformations.getLstrFromMuiStr("返工处理","Rework processing"));
		//btn.setBtnCode("返工处理");
		btn.setBtnChinaName("返工处理");
		btn.setBtnNo(IButton.FGCL);
		btn.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT,IBillOperate.OP_NOADD_NOTEDIT});
		btn.setBusinessStatus(new int[]{0});
		btn.setExtendStatus(new int[]{0});
		this.addPrivateButton(btn);
		this.addPrivateButton(createButtonVO(IButton.Print,"Print",Transformations.getLstrFromMuiStr("打印","Print")));
		this.addPrivateButton(createButtonVO(IButton.Delete,"Delete",Transformations.getLstrFromMuiStr("删除","Delete")));
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0,
			int arg1) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(
			CircularlyAccessibleValueObject[] arg0) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void initSelfData()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception
	{
		// TODO Auto-generated method stub
		setHeadItemValue("djh", Toolkit.getBillNO("GLCL", this.getUnitPK(), null, null));
		setHeadItemValue("xxsj",ClientEnvironment.getServerTime().getTime());
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
	protected void setHeadItemValue(String key,Object value)
	{
		this.getBillCardPanel().getHeadItem(key).setValue(value); 
	}
	public void bodyRowChange(BillEditEvent e)
	  {
	    super.bodyRowChange(e);
	    //IExtendStatus.
	  /*  try {
	    	GlclBillVO[] heads=(GlclBillVO[])this.getBufferData().;
			if(heads[e.getRow()].getParentVO().getDjzt()==null||heads[e.getRow()].getParentVO().getDjzt()>0)
			{
				return ;
			}
			else 
			{
			    getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
			    updateButton(getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			MessageDialog.showErrorDlg(this, "错误", e1.getMessage());
		}*/
	  }
	public void afterUpdate() {
		super.afterUpdate();
		if (this.isListPanelSelected())
	    {
			int index=getBillListPanel().getHeadTable().getSelectedRow()==-1?0:getBillListPanel().getHeadTable().getSelectedRow();
			GlclBillVO currvo=(GlclBillVO)getBillListPanel().getBillListData().getBillValueVO(index, GlclBillVO.class.getName(), GlclHeadVO.class.getName(), GlclItemVO.class.getName());
			String djzt=String.valueOf(currvo.getParentVO().getDjzt());
			djzt=djzt==null||djzt.equals("")||djzt.equalsIgnoreCase("null")?"0":djzt;
			if(Integer.parseInt(djzt)==1)
			{
			    getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
			    updateButton(getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit));
			}
			else
			{
			    getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(true);
			    updateButton(getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit));	
			}
	    }
		else 
		{
			String djzt=String.valueOf(getBillCardPanel().getHeadItem("djzt").getValueObject());
			djzt=djzt==null||djzt.equals("")||djzt.equalsIgnoreCase("null")?"0":djzt;
			if(Integer.parseInt(djzt)==1)
			{
			    getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
			    updateButton(getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit));
			    getButtonManager().getButton(nc.ui.mo.mo6601.IButton.FGCL).setEnabled(false);
			    updateButton(getButtonManager().getButton(nc.ui.mo.mo6601.IButton.FGCL));
			    getButtonManager().getButton(nc.ui.mo.mo6601.IButton.Xxgljc).setEnabled(false);
			    updateButton(getButtonManager().getButton(nc.ui.mo.mo6601.IButton.Xxgljc));
			}
			else
			{
			    getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(true);
			    updateButton(getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit));
			    getButtonManager().getButton(nc.ui.mo.mo6601.IButton.FGCL).setEnabled(true);
			    updateButton(getButtonManager().getButton(nc.ui.mo.mo6601.IButton.FGCL));
			    getButtonManager().getButton(nc.ui.mo.mo6601.IButton.Xxgljc).setEnabled(true);
			    updateButton(getButtonManager().getButton(nc.ui.mo.mo6601.IButton.Xxgljc));	
			}
		}
	}
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getKey().equalsIgnoreCase("hclhw")) {
	}
		String cwarehouseid = String.valueOf(getBillCardPanel().getBodyValueAt(e.getRow(), "ck"));
		if (cwarehouseid == null)
			return false ;
		UIRefPane pane = (UIRefPane) getBillCardPanel().getBodyItem(e.getKey()).getComponent();
		// 按照仓库过滤货位参照
		if(!(getUnitPK().equals("1108")||getUnitPK().equals("1078"))){//edit by zwx 2019-9-23 制盖公司不使用
			filterCargdocRefModel(pane, cwarehouseid);
		}
		return true;
	}
	private void filterCargdocRefModel(UIRefPane pane, String cwarehouseid) {
		if (pane == null || pane.getRefModel() == null)
			return;
		AbstractRefModel model = pane.getRefModel();
		model.addWherePart(" and bd_cargdoc.pk_stordoc = '" + cwarehouseid + "' ");
	}
	
	//add by zy 2018-08-13 
	public void doAddAction(ILinkAddData ilinkadddata) {
		onButtonClicked(getButtonManager().getButton(1));
		Object[] data = (Object[])ilinkadddata.getUserObject();
		getBillCardPanel().setHeadItem("clr",data[13]);
		getBillCardPanel().setHeadItem("bz", "110811100000000050Y1");
		getBillCardPanel().setHeadItem("pkCorp", data[0]);
		getBillCardPanel().setHeadItem("cinventoryid", data[1]);
		getBillCardPanel().setHeadItem("lh", data[2]);
		getBillCardPanel().setHeadItem("cpmc", data[3]);
		getBillCardPanel().setHeadItem("zdsl", data[6]);
		getBillCardPanel().setHeadItem("scx", data[12]);
		//getBillCardPanel().setHeadItem("djzt", data[8]);
//		getBillCardPanel().setHeadItem("llh", data[9]);
		ArrayList vbatlist = (ArrayList)data[4];
		ArrayList vfreeList = (ArrayList)data[5];
		ArrayList numList = (ArrayList)data[7];
		ArrayList carglist = (ArrayList)data[9];
		ArrayList glbList = (ArrayList)data[11];
		
		for (int i = 0; i < vbatlist.size(); i++) {
		      getBillCardPanel().addLine();
		      getBillCardPanel().setBodyValueAt(vfreeList.get(i), i, "ysdh");
		      getBillCardPanel().setBodyValueAt(numList.get(i), i, "dclsl");
		      getBillCardPanel().setBodyValueAt(numList.get(i), i, "bfsl");
		      getBillCardPanel().setBodyValueAt(data[8], i, "ck");
		      getBillCardPanel().setBodyValueAt(vbatlist.get(i), i, "clph");
		      getBillCardPanel().setBodyValueAt(vbatlist.get(i), i, "ysph");
//		      getBillCardPanel().setBodyValueAt(carglist.get(i), i, "hw");
		      getBillCardPanel().setBodyValueAt(vfreeList.get(i), i, "cldh");
//		      getBillCardPanel().setBodyValueAt(carglist.get(i), i, "cspaceid");
		      getBillCardPanel().setBodyValueAt("isolation", i, "lydjlx");
		      getBillCardPanel().setBodyValueAt(glbList.get(i), i, "pk_glzb_b");
		      getBillCardPanel().setBodyValueAt(data[10], i, "pk_glzb");
		    }
		onButtonClicked(getButtonManager().getButton(0));//参考IButton中的数字对应动作
//		this.getBillCardPanel().addLine();
//		getBillCardPanel().setBodyValueAt(data[4].toString(),0,"ysdh");
//		getBillCardPanel().setBodyValueAt(data[3].toString(),0,"hclph");
//		getBillCardPanel().setBodyValueAt(data[7].toString(),0,"bfsl");
	}
	//end by zy
	
	//add by zwx 2016-3-11 
	/*public void doAddAction(ILinkAddData ilinkadddata)
	 {
	    onButtonClicked(getButtonManager().getButton(1));
	    Object[] data = (Object[])ilinkadddata.getUserObject();
	    getBillCardPanel().setHeadItem("bz", data[0]);
	    getBillCardPanel().setHeadItem("scx", data[1]);
	    getBillCardPanel().setHeadItem("zdsl", data[2]);
	    getBillCardPanel().setHeadItem("cinventoryid", data[3]);
	    getBillCardPanel().setHeadItem("pk_corp", getUnitPK());
	    List vlist  = (List)data[4];
	    List numlist = (List)data[5];
	    List cwarelist = (List)data[6];
	    List carglist = (List)data[8];
	    List vbatlist = (List)data[9];
	    List cspaceidlist = (List)data[10];
	    String billno = (String)data[11];
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select h.pk_glzb,b.pk_glzb_b,b.dh from mm_glzb h ")
	      .append(" left join mm_glzb_b b ")
	      .append(" on h.pk_glzb = b.pk_glzb ")
	      .append(" where h.pk_corp = '" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' ")
	      .append(" and nvl(h.dr,0) = 0 ")
	      .append(" and nvl(b.dr,0) = 0 ")
	      .append(" and h.billno = '" + billno + "' ");

	    IUAPQueryBS iquery = (IUAPQueryBS)NCLocator.getInstance().lookup(
	      IUAPQueryBS.class.getName());
	    ArrayList list = null;
	    try
	    {
	      list = (ArrayList)iquery.executeQuery(sql.toString(), new MapListProcessor());
	    }
	    catch (BusinessException e1)
	    {
	      e1.printStackTrace();
	      return;
	    }
	    HashMap map = new HashMap();
	    String pk_glzb = "";
	    if ((list.size() > 0) && (list != null)) {
	      for (int j = 0; j < list.size(); j++) {
	        pk_glzb = (String)((HashMap)list.get(j)).get("pk_glzb");
	        String value = (String)((HashMap)list.get(j)).get("pk_glzb_b");
	        String key = (String)((HashMap)list.get(j)).get("dh");
	        map.put(key, value);
	      }

	    }

	    String cpbm = "";
	    try {
	      cpbm = (String)HYPubBO_Client.findColValue("bd_invmandoc a,bd_invbasdoc b", "b.invcode", "a.pk_invbasdoc=b.pk_invbasdoc and a.pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "' and a.dr=0 and b.dr=0 and a.pk_invmandoc='" + data[3] + "'");
	    }
	    catch (UifException e)
	    {
	      e.printStackTrace();
	      return;
	    }

	    for (int i = 0; i < vlist.size(); i++) {
	      getBillCardPanel().addLine();
	      getBillCardPanel().setBodyValueAt(vlist.get(i), i, "ysdh");
	      getBillCardPanel().setBodyValueAt(numlist.get(i), i, "dclsl");
	      getBillCardPanel().setBodyValueAt(numlist.get(i), i, "bfsl");
	      getBillCardPanel().setBodyValueAt(cwarelist.get(i), i, "ck");
	      getBillCardPanel().setBodyValueAt(vbatlist.get(i), i, "clph");
	      getBillCardPanel().setBodyValueAt(vbatlist.get(i), i, "ysph");
	      getBillCardPanel().setBodyValueAt(carglist.get(i), i, "hw");
	      getBillCardPanel().setBodyValueAt(vlist.get(i), i, "cldh");
	      getBillCardPanel().setBodyValueAt(cspaceidlist.get(i), i, "cspaceid");
	      getBillCardPanel().setBodyValueAt("isolation", i, "lydjlx");
	      getBillCardPanel().setBodyValueAt(map.get(vlist.get(i)), i, "pk_glzb_b");
	      getBillCardPanel().setBodyValueAt(pk_glzb, i, "pk_glzb");
	    }

	    getBillCardPanel().setHeadItem("cpmc", data[7]);
	    getBillCardPanel().setHeadItem("lh", cpbm);
	  }*/
	
	//end by zwx 
	
	
}
