package nc.ui.ia.ia402;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;

@SuppressWarnings("serial")
public class YLCheckDlg extends UIDialog implements ActionListener {

	private UIPanel topPnl;
	private UIPanel centerPnl;

	// 查询条件组件
	private UIRefPane dateFrom;
	private UIRefPane dateTo;

	// 查询按钮
	private UIButton btnQuery;
	private UIButton btnExport;
	private UIButton btnCheck;
	private UIButton btnClose;
	private String[] header_key_yf = new String[] {
			"zgbz", "ytdjlxbm", "ytdjlxmc", "lydjlxbm", "lydjlxmc", "ksbm", "ksmc", "lh", "mc", "djh", "djrq", "sl", "je", "se", "hsje", "btouzy", "btizy", "fph", "yfbz", "pzh", "djlxbm", "djlxmc", "gsbm"
	};
	private String[] header_name_yf = new String[] {
			"暂估标志", "源头单据类型编码", "源头单据类型名称", "来源单据类型编码", "来源单据类型名称", "客商编码", "客商名称", "料号", "名称", "单据号", "单据日期", "数量", "无税金额", "税额", "含税金额", "表头摘要", "表体摘要", "发票号", "预付标志", "凭证号", "单据类型编码", "单据类型名称", "公司编码"
	};
	private String[] header_key_ch = new String[] {
			"zgbz", "djlxbm", "djlxmc", "ytdjlxbm", "ytdjlxmc", "lydjlxbm", "lydjlxmc", "ksbm", "ksmc", "lh", "mc", "djh", "djrq", "fph", "sl", "je"
	};
	private String[] header_name_ch = new String[] {
			"暂估标志", "单据类型编码", "单据类型名称", "源头单据类型编码", "源头单据类型名称", "来源单据类型编码", "来源单据类型名称", "客商编码", "客商名称", "料号", "名称", "单据号", "单据日期", "发票号", "数量", "金额"
	};

	private String[] header_key_hedui = new String[] {
			"fph", "djh", "pzh", "ksbm", "ksmc", "lh", "mc", "sl", "je"
	};
	private String[] header_name_hedui = new String[] {
			"发票号", "应付单号", "凭证号", "客商编码", "客商名称", "料号", "名称", "数量", "金额"
	};
	//edit by zwx 2014-11-17 修改公司为当前登录公司
	String pk_corp=ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	private String sql_yf = "select b.zgyf as zgbz,a.dfshl as sl,trim(a.ph) as ytdjlxbm,c.billtypename as ytdjlxmc,trim(a.jsfsbm) as lydjlxbm,d.billtypename as lydjlxmc,g.custcode as ksbm,g.custname as ksmc,j.invcode as lh,j.invname as mc,b.djbh as djh,b.djrq as djrq,a.dfbbwsje as je,a.dfbbsj as se,a.dfbbje as hsje,b.scomment btouzy,a.zy as btizy,a.fph as fph,b.prepay as yfbz,i.no as pzh,b.djlxbm as djlxbm,e.djlxjc as djlxmc, a.dwbm as gsbm from arap_djfb a left join arap_djzb b on b.vouchid = a.vouchid left outer join dap_finindex h on b.djbh = h.billcode left outer join gl_voucher i on h.pk_vouchentry = i.pk_voucher left join bd_billtype c on c.pk_billtypecode = trim(a.ph) left join bd_billtype d on d.pk_billtypecode = trim(a.jsfsbm) left join arap_djlx e on e.djlxoid = b.ywbm left join bd_cumandoc f on f.pk_cumandoc = a.ksbm_cl left join bd_cubasdoc g on g.pk_cubasdoc = f.pk_cubasdoc left join bd_invbasdoc j on a.cinventoryid=j.pk_invbasdoc where a.dwbm = '"+pk_corp+"' and b.dwbm = '"+pk_corp+"' and a.dr = 0 and b.dr = 0 and b.djlxbm = 'D1' "//
			+ "and b.djrq >= #datefrom# and b.djrq <= #dateto#";
	private String sql_ch = "select b.bestimateflag as zgbz , b.cbilltypecode djlxbm, g.billtypename djlxmc, a.cfirstbilltypecode ytdjlxbm, c.billtypename ytdjlxmc, a.csourcebilltypecode lydjlxbm, d.billtypename lydjlxmc,f.custcode ksbm, f.custname ksmc,m.invcode as lh,m.invname as mc, b.vbillcode djh,b.dbilldate djrq,k.vinvoicecode as fph, a.nnumber sl, a.nmoney je from ia_bill_b a  left join ia_bill b on b.cbillid = a.cbillid left join bd_billtype c on c.pk_billtypecode = a.cfirstbilltypecode left join bd_billtype d on d.pk_billtypecode = a.csourcebilltypecode left join bd_cumandoc e on e.pk_cumandoc = b.ccustomvendorid left join bd_cubasdoc f on f.pk_cubasdoc = e.pk_cubasdoc left join bd_billtype g on g.pk_billtypecode = b.cbilltypecode  left join po_settlebill_b h on a.csourcebillitemid=h.csettlebill_bid left join po_settlebill i on h.csettlebillid=i.csettlebillid left join po_invoice_b j on h.cinvoice_bid=j.cinvoice_bid left join po_invoice k on j.cinvoiceid=k.cinvoiceid left join bd_invmandoc l on a.cinventoryid=l.pk_invmandoc left join bd_invbasdoc m on l.pk_invbasdoc=m.pk_invbasdoc where 1 = 1 and a.pk_corp = '"+pk_corp+"' and b.pk_corp = '"+pk_corp+"' and a.dr = 0 and a.dr = 0 and (b.bestimateflag = 'Y' or (b.bestimateflag = 'N' and b.cbilltypecode in ('I2', 'I9','ID'))) "//
			+ "and b.dbilldate >= #datefrom# and b.dbilldate <= #dateto#";
	//end by zwx
	
	// 查询
	@SuppressWarnings("unchecked")
	public void onBoQuery() {
		if (dateFrom.getText() == null || "".equals(dateFrom.getText()) || dateTo.getText() == null || "".equals(dateTo.getText())) {
			MessageDialog.showHintDlg(this, null, "请填写查询日期范围");
			return;
		}
		try {
			IUAPQueryBS jdbc = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql_yf_query = sql_yf.replaceAll("#datefrom#", "'" + dateFrom.getText() + "'").replaceAll("#dateto#", "'" + dateTo.getText() + "'");
			List<Map<String, Object>> yfList = (List<Map<String, Object>>) jdbc.executeQuery(sql_yf_query, new MapListProcessor());
			if (yfList != null && yfList.size() > 0) {
				Object[][] data = new Object[yfList.size()][header_key_yf.length];
				for (int i = 0; i < yfList.size(); i++) {
					for (int j = 0; j < header_key_yf.length; j++) {
						data[i][j] = yfList.get(i).get(header_key_yf[j]);
					}
				}
				DefaultTableModel model = new DefaultTableModel(data, header_name_yf);
				tbls[0].setModel(model);
			}

			String sql_ch_query = sql_ch.replaceAll("#datefrom#", "'" + dateFrom.getText() + "'").replaceAll("#dateto#", "'" + dateTo.getText() + "'");
			List<Map<String, Object>> chList = (List<Map<String, Object>>) jdbc.executeQuery(sql_ch_query, new MapListProcessor());
			if (chList != null && chList.size() > 0) {
				Object[][] data = new Object[chList.size()][header_key_ch.length];
				for (int i = 0; i < chList.size(); i++) {
					for (int j = 0; j < header_key_ch.length; j++) {
						data[i][j] = chList.get(i).get(header_key_ch[j]);
					}
				}
				DefaultTableModel model = new DefaultTableModel(data, header_name_ch);
				tbls[1].setModel(model);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 导出
	@SuppressWarnings("unchecked")
	public void onBoExport() {
		ExcelExportTool eet = new ExcelExportTool();
		eet.setSheetNames(titles);
		List<Vector<Vector<Object>>> dataList = new ArrayList<Vector<Vector<Object>>>();
		String[][] colNames = new String[titles.length][];
		for (int i = 0; i < tbls.length; i++) {
			DefaultTableModel model = (DefaultTableModel) tbls[i].getModel();
			if (model == null) {
				dataList.add(null);
				colNames[i] = null;
			} else {
				dataList.add(model.getDataVector());
				int colCount = model.getColumnCount();
				String[] colName = new String[colCount];
				for (int j = 0; j < colName.length; j++) {
					colName[j] = model.getColumnName(j);
				}
				colNames[i] = colName;
			}
		}
		eet.setColNames(colNames);
		eet.setData(dataList);
		try {
			eet.exportExcelFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onBoClose() {
		try {
			Object obj = NCLocator.getInstance().lookup("nc.itf.ws.baoyin.IPDAServiceExecutor");
			Method m = obj.getClass().getMethod("execute", new Class[] {
				Object.class
			});
			Object ret = m.invoke(obj, new Object[] {
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><actiontype>login</actiontype><sender>test</sender><data><unitcode>10301</unitcode><usercode>db</usercode><userpwd>1</userpwd></data></root>"
			});
			System.out.println(">>>>>>>>>" + ret);
			Logger.error(">>>>>>>>>" + ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			Object obj = NCLocator.getInstance().lookup("nc.itf.pub.rino.IPubDMO");
			Method m = obj.getClass().getMethod("getObject", new Class[] {
				String.class
			});
			Object ret = m.invoke(obj, new Object[] {
				"select unitname from bd_corp where unitcode='10301'"
			});
			System.out.println(":::::::::::" + ret);
			Logger.error("::::::::::::" + ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.dispose();
	}

	// 核对
	public void onBoCheck() throws Exception {
		IUAPQueryBS jdbc = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object[][] data = null;
		String sql_yf_query = sql_yf.replaceAll("#datefrom#", "'" + dateFrom.getText() + "'").replaceAll("#dateto#", "'" + dateTo.getText() + "'");
		String sql_ch_query = sql_ch.replaceAll("#datefrom#", "'" + dateFrom.getText() + "'").replaceAll("#dateto#", "'" + dateTo.getText() + "'");

		// 第一组
		{
			String where_yf = " zgbz=1 and ytdjlxbm='21' and lydjlxbm='45' and btouzy like '%价外费暂估%'";
			String where_ch = " djlxbm='I9' and lydjlxbm='45'";
			Object obj_yf = jdbc.executeQuery("select sum(je) as rst from (" + sql_yf_query + ") where " + where_yf, new ColumnProcessor());
			Object obj_ch = jdbc.executeQuery("select sum(je) as rst from (" + sql_ch_query + ") where " + where_ch, new ColumnProcessor());
			double je_yf = obj_yf == null ? 0 : Double.parseDouble(obj_yf.toString());
			double je_ch = obj_ch == null ? 0 : Double.parseDouble(obj_ch.toString());
			if (je_yf - je_ch != 0) {
				// 未核对上
				StringBuilder sql = new StringBuilder();
				sql.append("select fph,djh,pzh,ksbm,ksmc,lh,mc,sl,je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append(" ) where fph in (");
				sql.append("   select distinct fph from (");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("     minus");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_ch_query + ") where ").append(where_ch);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("   )");
				sql.append(" )");
				List<Map<String, Object>> chayiData = (List<Map<String, Object>>) jdbc.executeQuery(sql.toString(), new MapListProcessor());
				if (chayiData != null && chayiData.size() > 0) {
					for (Map<String, Object> map : chayiData) {
						Object[] rowData = new Object[header_key_hedui.length];
						for (int i = 0; i < header_key_hedui.length; i++) {
							rowData[i] = map.get(header_key_hedui[i]);
						}
						data = (Object[][]) ArrayUtils.add(data, rowData);
					}
				}
			}
		}

		// 第二组
		{
			String where_yf = " zgbz=0 and ytdjlxbm='45'";
			String where_ch = " djlxbm='I2' and zgbz='N' and ytdjlxbm='21' and lydjlxbm='27'";
			Object obj_yf = jdbc.executeQuery("select sum(je) as rst from (" + sql_yf_query + ") where " + where_yf, new ColumnProcessor());
			Object obj_ch = jdbc.executeQuery("select sum(je) as rst from (" + sql_ch_query + ") where " + where_ch, new ColumnProcessor());
			double je_yf = obj_yf == null ? 0 : Double.parseDouble(obj_yf.toString());
			double je_ch = obj_ch == null ? 0 : Double.parseDouble(obj_ch.toString());
			if (je_yf - je_ch != 0) {
				// 未核对上
				StringBuilder sql = new StringBuilder();
				sql.append("select fph,djh,pzh,ksbm,ksmc,lh,mc,sl,je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append(" ) where fph in (");
				sql.append("   select distinct fph from (");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("     minus");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_ch_query + ") where ").append(where_ch);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("   )");
				sql.append(" )");
				List<Map<String, Object>> chayiData = (List<Map<String, Object>>) jdbc.executeQuery(sql.toString(), new MapListProcessor());
				if (chayiData != null && chayiData.size() > 0) {
					for (Map<String, Object> map : chayiData) {
						Object[] rowData = new Object[header_key_hedui.length];
						for (int i = 0; i < header_key_hedui.length; i++) {
							rowData[i] = map.get(header_key_hedui[i]);
						}
						data = (Object[][]) ArrayUtils.add(data, rowData);
					}
				}
			}
		}

		// 第三组
		{
			String where_yf = " zgbz=0 and ytdjlxbm is null and lydjlxbm='25'";
			String where_ch = " djlxbm='I9' and ytdjlxbm='21' and lydjlxbm='27'";
			Object obj_yf = jdbc.executeQuery("select sum(je) as rst from (" + sql_yf_query + ") where " + where_yf, new ColumnProcessor());
			Object obj_ch = jdbc.executeQuery("select sum(je) as rst from (" + sql_ch_query + ") where " + where_ch, new ColumnProcessor());
			double je_yf = obj_yf == null ? 0 : Double.parseDouble(obj_yf.toString());
			double je_ch = obj_ch == null ? 0 : Double.parseDouble(obj_ch.toString());
			if (je_yf - je_ch != 0) {
				// 未核对上
				StringBuilder sql = new StringBuilder();
				sql.append("select fph,djh,pzh,ksbm,ksmc,lh,mc,sl,je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append(" ) where fph in (");
				sql.append("   select distinct fph from (");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("     minus");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_ch_query + ") where ").append(where_ch);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("   )");
				sql.append(" )");
				List<Map<String, Object>> chayiData = (List<Map<String, Object>>) jdbc.executeQuery(sql.toString(), new MapListProcessor());
				if (chayiData != null && chayiData.size() > 0) {
					for (Map<String, Object> map : chayiData) {
						Object[] rowData = new Object[header_key_hedui.length];
						for (int i = 0; i < header_key_hedui.length; i++) {
							rowData[i] = map.get(header_key_hedui[i]);
						}
						data = (Object[][]) ArrayUtils.add(data, rowData);
					}
				}
			}
		}

		// 第四组
		{
			String where_yf = " zgbz=0 and ytdjlxbm='50'";
			String where_ch = " zgbz='N' and ytdjlxbm='50' and lydjlxbm='27'";
			Object obj_yf = jdbc.executeQuery("select sum(je) as rst from (" + sql_yf_query + ") where " + where_yf, new ColumnProcessor());
			Object obj_ch = jdbc.executeQuery("select sum(je) as rst from (" + sql_ch_query + ") where " + where_ch, new ColumnProcessor());
			double je_yf = obj_yf == null ? 0 : Double.parseDouble(obj_yf.toString());
			double je_ch = obj_ch == null ? 0 : Double.parseDouble(obj_ch.toString());
			if (je_yf - je_ch != 0) {
				// 未核对上
				StringBuilder sql = new StringBuilder();
				sql.append("select fph,djh,pzh,ksbm,ksmc,lh,mc,sl,je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append(" ) where fph in (");
				sql.append("   select distinct fph from (");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_yf_query + ") where ").append(where_yf);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("     minus");
				sql.append("     select fph,lh,ksbm,sum(je) as je from (");
				sql.append("select * from (" + sql_ch_query + ") where ").append(where_ch);
				sql.append("     ) group by fph,lh,ksbm");
				sql.append("   )");
				sql.append(" )");
				List<Map<String, Object>> chayiData = (List<Map<String, Object>>) jdbc.executeQuery(sql.toString(), new MapListProcessor());
				if (chayiData != null && chayiData.size() > 0) {
					for (Map<String, Object> map : chayiData) {
						Object[] rowData = new Object[header_key_hedui.length];
						for (int i = 0; i < header_key_hedui.length; i++) {
							rowData[i] = map.get(header_key_hedui[i]);
						}
						data = (Object[][]) ArrayUtils.add(data, rowData);
					}
				}
			}
		}
		DefaultTableModel model = new DefaultTableModel(data, header_name_hedui);
		tbls[2].setModel(model);
		MessageDialog.showHintDlg(this, null, "核对完成");
	}

	public YLCheckDlg(Container parent) {
		super(parent);
		setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setLocationRelativeTo(null);
		initPage();
	}

	private void initPage() {
		this.setLayout(new BorderLayout());
		initTopPnl();
		initCenterPnl();
		initListener();
	}

	private void initListener() {
		btnQuery.setActionCommand("onBoQuery");
		btnExport.setActionCommand("onBoExport");
		btnClose.setActionCommand("onBoClose");
		btnCheck.setActionCommand("onBoCheck");
		btnQuery.addActionListener(this);
		btnExport.addActionListener(this);
		btnClose.addActionListener(this);
		btnCheck.addActionListener(this);
	}

	private void initTopPnl() {
		topPnl = new UIPanel(new FlowLayout(FlowLayout.LEFT));
		topPnl.add(new UILabel("日期:"));
		dateFrom = new UIRefPane("日历");
		topPnl.add(dateFrom);
		topPnl.add(new UILabel("-"));
		dateTo = new UIRefPane("日历");
		topPnl.add(dateTo);

		btnQuery = new UIButton("查询");
		topPnl.add(btnQuery);
		btnCheck = new UIButton("核对");
		topPnl.add(btnCheck);
		btnExport = new UIButton("导出");
		topPnl.add(btnExport);
		btnClose = new UIButton("关闭");
		topPnl.add(btnClose);

		this.add(topPnl, BorderLayout.NORTH);
	}

	private void initCenterPnl() {
		centerPnl = new UIPanel(new BorderLayout());
		UITabbedPane tabPnl = new UITabbedPane();
		for (int i = 0; i < tbls.length; i++) {
			UIScrollPane sp = new UIScrollPane();
			sp.setViewportView(tbls[i]);
			sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			tabPnl.addTab(titles[i], sp);
		}
		centerPnl.add(tabPnl);
		this.add(centerPnl, BorderLayout.CENTER);
	}

	// 表格组件
	private UITable[] tbls = new UITable[] {
			new UITable(), new UITable(), new UITable()
	};
	// 页签标题
	private String[] titles = new String[] {
			"应付", "存货", "核对结果"
	};

	public void actionPerformed(ActionEvent e) {
		UIButton btn = (UIButton) e.getSource();
		String actionCommand = btn.getActionCommand();
		try {
			java.lang.reflect.Method actionMethod = this.getClass().getMethod(actionCommand);
			actionMethod.invoke(this);
		} catch (Exception ex) {
			MessageDialog.showHintDlg(this, null, "核对过程发生错误:" + ex.getCause().getMessage());
			ex.getCause().printStackTrace();
		}
	}

}
