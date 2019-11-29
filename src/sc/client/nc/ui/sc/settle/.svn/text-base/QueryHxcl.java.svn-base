package nc.ui.sc.settle;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.settleCltj.SettleCltjVO;

@SuppressWarnings("serial")
public class QueryHxcl extends UIDialog
{
	private UITable table ;
	Object cvendormangid = null;
	Object cprocessmangid = null;
	Object cmaterialmangid = null;
	Object[] vbatchdates = null;
	HashMap<String, UFDouble> hmap = null;
	public QueryHxcl(Container ui,Object cvendormangid,Object cprocessmangid,Object cmaterialmangid, HashMap<String, UFDouble> hmap)
	{ 
		super(ui);
		try
		{ 
			this.cvendormangid = cvendormangid;
			this.cprocessmangid = cprocessmangid;
			this.cmaterialmangid = cmaterialmangid;
			this.hmap = hmap;
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
		this.setSize(610, 400);
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
		UIButton btn = new UIButton("确认");
		btn.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e)
			{  
				int srow = getTable().getSelectedRow();
				if (srow<0) {
					return;
				}
				Object num = getTable().getModel().getValueAt(srow, 2);
				UFDouble number = num==null?new UFDouble(0):new UFDouble(num.toString());
				if (number.doubleValue()<=0) {
					MessageDialog.showWarningDlg(getTable(), "警告", "可核销数量必须大于零！");
					return;
				}
				Object[] ivalue = new Object[3];
				for (int i = 0; i < 3; i++) {
					Object iv = getTable().getModel().getValueAt(srow, i);
					ivalue[i] = iv;
				}
				setValue(ivalue);
				Closeok();
			}

			private void setValue(Object[] ivalue) {
				vbatchdates = ivalue;
			}
		});
		return btn;
	}
	protected void Closeok() {
		this.closeOK();
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
	
	@SuppressWarnings("unchecked")
	private void initialData() throws BusinessException
	{
		NCTableModel model = new NCTableModel();
		String[] head = new String[]{ 
				"批次号","自由项","可核销数量"};
		String[] field=new String []{
				"vbatch","vfree1","num"};
		List fieldList=new ArrayList();
		for(String subfield:field)
		{
			fieldList.add(subfield);
		}

		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		
		StringBuffer sb = new StringBuffer("");
		//查询语句
		sb.append(" select vbatch, ") 
		.append("        vfree1, ") 
		.append("        sum(case rtrim(isourcetype, ' ') ") 
		.append("              when '0' then ") 
		.append("               nnum ") 
		.append("              when '1' then ") 
		.append("               -nnum ") 
		.append("              when '2' then ") 
		.append("               -nnum ") 
		.append("              when '3' then ") 
		.append("               nnum ") 
		.append("            end) num, ") 
		.append("        cvendorid ") 
		.append("   from sc_materialledger ") 
		.append("  where pk_corp = '"+pk_corp+"' ") 
		.append("    and cmaterialmangid = '"+cmaterialmangid+"' ") 
		.append("    and cprocessmangid = '"+cprocessmangid+"' ") 
		.append("    and cvendormangid = '"+cvendormangid+"' ") 
		.append("    and nvl(dr,0) = 0 ") 
		.append("    and vbatch is not null ") 
		.append("    and vbatch in (select vbatch from sc_balance where nbalancenum <> 0) ") 
		.append("  group by cvendorid, cprocessbaseid, cmaterialbaseid, vbatch, vfree1 ") ;
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<SettleCltjVO> list = (List<SettleCltjVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(SettleCltjVO.class));
		if (list!=null&&list.size()>0) {
			Object[][] datas = new Object[list.size()][head.length];
			for(int i=0;i<list.size();i++) {
				SettleCltjVO tempVO = (SettleCltjVO) list.get(i);
	            int index=0;
				//批次号
				index=fieldList.indexOf("vbatch");
				datas[i][index] = tempVO.getVbatch();
				//自由项
				index=fieldList.indexOf("vfree1");
				datas[i][index] = tempVO.getVfree1();
				//可核销数量 
				String key = ""+cvendormangid+cprocessmangid+cmaterialmangid+tempVO.getVbatch()+tempVO.getVfree1();
				UFDouble keynum = hmap.get(key)==null?new UFDouble(0):hmap.get(key);
				index=fieldList.indexOf("num");
				datas[i][index] = (tempVO.getNum()).sub(keynum); 
			}
			model.setDataVector(datas, head);
		}else{
			model.setDataVector(null, head);
		}
		getTable().setModel(model);
		getTable().setColumnModel(getColumn(getTable(), new int[]{200,200,200}));
//		//行改变
//		FaTableCellRender  cell = (FaTableCellRender)getTable().getColumnModel().getColumn(1).getCellRenderer();
//		cell.setBackground(Color.RED, 1);

		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 
	}
	
	public Object[] getValue(){
		return vbatchdates;
	}
	/** 
	 * 
	 * */  
	public static TableColumnModel getColumn(JTable table, int[] width) {  
	    TableColumnModel columns = table.getColumnModel();  
	    for (int i = 0; i < width.length; i++) {  
	        TableColumn column = columns.getColumn(i);  
	        column.setPreferredWidth(width[i]);  
	    }  
	    return columns;  
	} 
}
