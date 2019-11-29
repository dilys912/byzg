package nc.ui.dm.dm104;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.fi_print.data.TempletData;
import nc.ui.fi_print.entry.FiPrintEntry;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

@SuppressWarnings("serial")
public class QueryXcl extends UIDialog
{
	private UITable table ;
	String[] pkInv = null;
	String[] pkStor = null;
	private QueryXclVO[] datavos;
	private UIDialog dialog = this;
	public QueryXcl(Container ui,String[] pkInv)
	{ 
		super(ui);
		try
		{ 
			this.pkInv = pkInv;
			initialDialog();
			initialUI();
			initialData();
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Logger.error(ex.getMessage(), ex);
		}
	}
	//add by zwx 2014年12月23日
	public QueryXcl(Container ui,String[] pkInv, String[] pkStor)
	{ 
		super(ui);
		try
		{ 
			this.pkInv = pkInv;
			this.pkStor = pkStor;
			initialDialog();
			initialUI();
			initialData();
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Logger.error(ex.getMessage(), ex);
		}
	}
	//end by zwx 
	public QueryXcl(ClientUI ui, String[] pkInv, String[] pkStor) {
		super(ui);
		try
		{ 
			this.pkInv = pkInv;
			this.pkStor = pkStor;
			initialDialog();
			initialUI();
			initialData();
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Logger.error(ex.getMessage(), ex);
		}
	}
	private void initialDialog()
	{
		this.setSize(900, 500);
	}
	private void initialUI()
	{
		this.setLayout(new BorderLayout());
		
		JScrollPane ak = new JScrollPane(getTable());
		this.add(ak,BorderLayout.CENTER);
		this.add(getPrintBtn(),BorderLayout.SOUTH);
	}
	private UIButton getPrintBtn()
	{
		UIButton btn = new UIButton("打印");
		btn.addActionListener(new ActionListener(){ 
			@SuppressWarnings("restriction")
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub  
				FiPrintEntry printEntry = new FiPrintEntry(dialog);
				printEntry.addData(new TempletData(null,datavos));
				String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
				String nodeId = "40140408";
				String nodekey = "pxcl"; 
				printEntry.print(pk_corp, nodeId, nodekey);   
			}
			
		});
		return btn;
	}
	private UITable getTable()
	{ 
		if(table==null)
		{
		    table = new UITable();
		    table.setDefaultHeader(false);
		}
		
		return table;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialData() throws BusinessException
	{
		NCTableModel model = new NCTableModel();
		String[] head = new String[]{ 
				"存货编码","存货名称","仓库编码","仓库名称","货位编码","货位名称","批次号",		//"批次号" 2014-7-14 add by zwx
				"规格","型号","计量单位","每垛数量","库存量垛数","库存数量","可出库数量","可出库垛数"};
		String[] field=new String []{
				"invcode","invname","cwarehousecode","cwarehousename","cspacecode","cspacename","vbatchcode",	//"vbatchcode" 2014-7-14 add by zwx
				"invspec","invtype","jldw","mdsl","kclds","kcsl","kcksl","kckds"};
		//FiledClass  fvo=new FiledClass(head,file);
		ArrayList fieldList=new ArrayList();
		for(String subfield:field)
		{
			fieldList.add(subfield);
		}
		StringBuffer pkWhere = new StringBuffer("'");
		for(String str:pkInv)
		{
			pkWhere.append(str).append("','");
		}

		StringBuffer pkStors = new StringBuffer("'");
		if (pkStor!=null) {
			for(String stor:pkStor)
			{
				pkStors.append(stor).append("','");
			}
		}

		String pk_corp= ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		//查询现存量
//		String sql = "SELECT cinvbasid as pk_invbasdoc,kp.cwarehouseid,kp.cspaceid,kp.vbatchcode,kp.pk_corp,sum(KP.NINSPACENUM)/COUNT(*) AS mdsl,count(*) AS kclds,SUM(KP.NINSPACENUM) AS kcsl FROM v_ic_onhandnum6 kp   "
//				+ "WHERE kp.pk_corp = '"+pk_corp+"' and nvl(KP.NINSPACENUM,0.0)>0 AND cinvbasid in (SELECT pk_invbasdoc FROM bd_invmandoc WHERE pk_invmandoc in ("//add by shikun 2014-11-02 pk_corp
//				+ pkWhere.substring(0, pkWhere.length() - 2)
//				+ ")) "
//				+ "GROUP BY cinvbasid,KP.NINSPACENUM,kp.cwarehouseid,kp.cspaceid,kp.vbatchcode,kp.pk_corp " //"kp.vbatchcode" 2014-7-14 add by zwx
//				+ "ORDER BY kp.vbatchcode"; //add by zwx 2014-10-27 
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT cinvbasid as pk_invbasdoc, ") 
		.append("        kp.cwarehouseid, ") 
		.append("        kp.cspaceid, ") 
		.append("        kp.vbatchcode, ") 
		.append("        kp.pk_corp, ") 
		.append("        sum(KP.NINSPACENUM) / COUNT(*) AS mdsl, ") 
		.append("        count(*) AS kclds, ") 
		.append("        SUM(KP.NINSPACENUM) AS kcsl ") 
		.append("   FROM v_ic_onhandnum6 kp ") 
		.append("  WHERE kp.pk_corp = '"+pk_corp+"' ") 
		.append("    and nvl(KP.NINSPACENUM, 0.0) > 0 ") 
		.append("    AND cinvbasid in ") 
		.append("        (SELECT pk_invbasdoc ") 
		.append("           FROM bd_invmandoc ") 
		.append("          WHERE pk_invmandoc in ("+pkWhere.substring(0, pkWhere.length() - 2)+")) ") ;
		if (pkStor!=null) {
			sql.append("    and kp.cwarehouseid in ("+pkStors.substring(0, pkStors.length() - 2)+") ");
		}
		sql.append("  GROUP BY cinvbasid, ") 
		.append("           KP.NINSPACENUM, ") 
		.append("           kp.cwarehouseid, ") 
		.append("           kp.cspaceid, ") 
		.append("           kp.vbatchcode, ") 
		.append("           kp.pk_corp ") 
		.append("  ORDER BY kp.vbatchcode ") ;
		
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	
		
		List list =  (List) query.executeQuery(sql.toString(), new BeanListProcessor(QueryXclVO.class));
		
		StringBuffer whInbasid = new StringBuffer("'");
		StringBuffer whZdsl = new StringBuffer();
		QueryXclVO tempVO=null;
//		String pk_corp= ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		for(int i=0;i<list.size();i++)
		{
			tempVO = (QueryXclVO) list.get(i);
			tempVO.setPk_corp(pk_corp);
			Object mdsl = tempVO.getMdsl();
			if (mdsl!=null&&whZdsl.indexOf(mdsl.toString())==-1) {
				whZdsl.append(mdsl).append(",");
			}
			if(whInbasid.indexOf(tempVO.getPk_invbasdoc())>0)
			{
				continue;
			}
			whInbasid.append(tempVO.getPk_invbasdoc()).append("','");
		}
		
		//查询冻结数量
//		sql = "SELECT cinvbasid as pk_invbasdoc,cwarehouseid,cspaceid,vbatchcode,nfreezenum as mdsl,sum(nfreezenum) as freeNum " +
//				" FROM ic_freeze WHERE pk_corp = '"+pk_corp+"' and cinvbasid in("//add by shikun 2014-11-02 pk_corp
//				+ whInbasid.substring(0, whInbasid.length() - 2)
//				+ ")  GROUP BY cinvbasid,nfreezenum,cwarehouseid,cspaceid,vbatchcode"; // "vbatchcode" 2014-7-14 add by zwx
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" SELECT cinvbasid as pk_invbasdoc, ") 
		.append("        cwarehouseid, ") 
		.append("        cspaceid, ") 
		.append("        vbatchcode, ") 
		.append("        nfreezenum as mdsl, ") 
		.append("        sum(nfreezenum) as freeNum ") 
		.append("   FROM ic_freeze ") 
		.append("  WHERE pk_corp = '"+pk_corp+"' ") ;
		//edit by zwx 2014-12-23 
		if(whInbasid.toString().equals("'")){
			sql2.append("    and cinvbasid in ('') ") ;
		}else{
			sql2.append("    and cinvbasid in ("+whInbasid.substring(0, whInbasid.length() - 2)+") ") ;
		}
		//end by zwx 
		if (pkStor!=null) {
			sql2.append("    and cwarehouseid in ("+pkStors.substring(0, pkStors.length() - 2)+") ") ;
		}
		sql2.append("  GROUP BY cinvbasid, nfreezenum, cwarehouseid, cspaceid, vbatchcode ") ;
		List listFree = (List) query.executeQuery(sql2.toString(), new MapListProcessor());
		
		//查询已出库数量
//		sql="SELECT c.pk_invbasdoc,a.vuserdef10 as mdsl,SUM(b.dinvnum)-SUM(b.doutnum) AS outNum FROM DM_DELIVBILL_H a,DM_DELIVBILL_B b,bd_invmandoc c " +
//				"WHERE a.pk_delivbill_h=b.pk_delivbill_h  and b.pkinv=c.pk_invmandoc AND a.vuserdef10 IN("+whZdsl.substring(0, whZdsl.length()-1)+") AND b.dinvnum!=b.doutnum " +
//				"AND b.pkinv IN("+pkWhere.substring(0, pkWhere.length()-2)+") and c.pk_corp = '"+pk_corp+"' GROUP BY c.pk_invbasdoc,a.vuserdef10";//add by shikun 2014-11-02 pk_corp
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" SELECT c.pk_invbasdoc, ") 
		.append("        a.vuserdef10 as mdsl, ") 
		.append("        SUM(b.dinvnum) - SUM(b.doutnum) AS outNum ") 
		.append("   FROM DM_DELIVBILL_H a, DM_DELIVBILL_B b, bd_invmandoc c ") 
		.append("  WHERE a.pk_delivbill_h = b.pk_delivbill_h ") 
		.append("    and b.pkinv = c.pk_invmandoc ");
		//edit by zwx 2014-12-23 
		if(whZdsl.length()>0){
			sql3.append("    AND a.vuserdef10 IN ("+whZdsl.substring(0, whZdsl.length()-1)+") ") ;
		}else{
			sql3.append("    AND a.vuserdef10 IN ('') ") ;
		}
		sql3.append("    AND b.dinvnum != b.doutnum ") ;
		if(pkWhere.toString().equals("'")){//edit by zwx 2014-12-23 
			sql3.append("    AND b.pkinv IN ('') ") ;
		}else{
			sql3.append("    AND b.pkinv IN ("+pkWhere.substring(0, pkWhere.length()-2)+") ") ;
		}
		sql3.append("    and c.pk_corp = '"+pk_corp+"' ") ;
		if (pkStor!=null) {
			sql3.append(" and b.pksendstock in ("+pkStors.substring(0, pkStors.length()-2)+") ");
		}
		sql3.append("  GROUP BY c.pk_invbasdoc, a.vuserdef10 ") ;
		List listOut =  (List) query.executeQuery(sql3.toString(), new MapListProcessor());
		UFDouble temFree = new UFDouble(0);
		UFDouble temOut = new UFDouble(0);
		Map temMap = null;
		Object[][] datas = new Object[list.size()][head.length];
 
		//查询存货基本信息
		//edit by zwx 2014-12-23 
		String sql4=new String();
		if(whInbasid.toString().equals("'")){
			 sql4 = "SELECT a.pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,b.measname as jldw  FROM bd_invbasdoc a,bd_measdoc b WHERE a.pk_measdoc=b.pk_measdoc and pk_invbasdoc in('')";
		}else{
			 sql4 = "SELECT a.pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,b.measname as jldw  FROM bd_invbasdoc a,bd_measdoc b WHERE a.pk_measdoc=b.pk_measdoc and pk_invbasdoc in("+whInbasid.substring(0, whInbasid.length()-2)+")";
		}
		List invList =  (List) query.executeQuery(sql4, new MapListProcessor());
		Map<String,Map> invMap = new HashMap<String,Map>();
		for(int i=0;i<invList.size();i++)
		{	
			temMap = (Map) invList.get(i);
			invMap.put((String)temMap.get("pk_invbasdoc"), temMap);
		}
	
		//组装数据 ，根据 存货PK、垛数 关联
		String []  freestrName= new String[]{"pk_invbasdoc","mdsl","cwarehouseid","cspaceid","vbatchcode"};		//"vbatchcode" 2014-7-14 add by zwx
		for(int i=0;i<list.size();i++)
		{
			tempVO = (QueryXclVO) list.get(i);
			tempVO.iniWhAndSpace();
			for(int j=0;j<listFree.size();j++)
			{
				temMap = (Map) listFree.get(j);
				
		/*		if(temMap.get("pk_invbasdoc").equals(tempVO.getPk_invbasdoc())&&tempVO.getMdsl().equals(temMap.get("mdsl")))
				{
					temFree = (UFDouble) temMap.get("nfreezenum");
				}*/

				String[] strValue = new String[] {
						String.valueOf(temMap.get("pk_invbasdoc")),
						String.valueOf(temMap.get("mdsl")),
						String.valueOf(temMap.get("cwarehouseid")),
						String.valueOf(temMap.get("cspaceid")),
						String.valueOf(temMap.get("vbatchcode")) }; //"String.valueOf(temMap.get("vbatchcode"))" 2014-7-14  add by zwx
				try {
					if(tempVO.StringEquals(freestrName, strValue))
					{
						temFree = (UFDouble) temMap.get("nfreezenum");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(int j=0;j<listOut.size();j++)
			{
				temMap = (Map) listOut.get(j);
				if(temMap.get("pk_invbasdoc").equals(tempVO.getPk_invbasdoc())&&tempVO.getMdsl().equals(temMap.get("mdsl")))
				{
					temOut = (UFDouble) temMap.get("outNum");
				}
			} 
			
			temFree = temFree==null?new UFDouble(0):temFree;
			temOut = temOut==null?new UFDouble(0):temOut;
			tempVO.setKcksl(tempVO.getKcsl().sub(temFree).sub(temOut));
			tempVO.setKckds(tempVO.getKcksl().div(tempVO.getMdsl()).intValue());
			
			//存货编码
           int index=0;
           index=fieldList.indexOf("invcode");
		   datas[i][index] = invMap.get(tempVO.getPk_invbasdoc()).get("invcode"); 
		
			tempVO.setInvcode((String) datas[i][index]);
			//存货名称
			index=fieldList.indexOf("invname");
			datas[i][index] = invMap.get(tempVO.getPk_invbasdoc()).get("invname");
			tempVO.setInvname((String) datas[i][index]);
			//规格
			index=fieldList.indexOf("invspec");
			datas[i][index] = invMap.get(tempVO.getPk_invbasdoc()).get("invspec");
			tempVO.setInvspec((String) datas[i][index]);
			//型号
			index=fieldList.indexOf("invtype");
			datas[i][index] = invMap.get(tempVO.getPk_invbasdoc()).get("invtype");
			tempVO.setInvtype((String) datas[i][index]);
			//计量单位
			index=fieldList.indexOf("jldw");
			datas[i][index] = invMap.get(tempVO.getPk_invbasdoc()).get("jldw");
			tempVO.setJldw((String) datas[i][index]);
			//每垛数量
			index=fieldList.indexOf("mdsl");
			datas[i][index] = tempVO.getMdsl();
			//可存量垛数
			index=fieldList.indexOf("kclds");
			datas[i][index] = tempVO.getKclds();
			//库存数量
			index=fieldList.indexOf("kcsl");
			datas[i][index] = tempVO.getKcsl();
			//可出库数量
			index=fieldList.indexOf("kcksl");
			datas[i][index] = tempVO.getKcksl();
			//可出库垛数
			index=fieldList.indexOf("kckds");
			datas[i][index] = tempVO.getKckds();
			//仓库编码
			index=fieldList.indexOf("cwarehousecode");
			datas[i][index] = tempVO.getCwarehousecode();
			//仓库名称
			index=fieldList.indexOf("cwarehousename");
			datas[i][index] = tempVO.getCwarehousename();
			//货位编码
			index=fieldList.indexOf("cspacecode");
			datas[i][index] = tempVO.getCspacecode();
			//货位名称
			index=fieldList.indexOf("cspacename");
			datas[i][index] = tempVO.getCspacename();
			//"批次号"  2014-7-14 add  by zwx
			index=fieldList.indexOf("vbatchcode");
			datas[i][index] = tempVO.getVbatchcode();
		}
		datavos = (QueryXclVO[]) list.toArray(new QueryXclVO[]{}); 
		model.setDataVector(datas, head);
		getTable().setModel(model);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
	}
	
	
	}
