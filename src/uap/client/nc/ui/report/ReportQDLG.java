package nc.ui.report;

import java.awt.BorderLayout;
import java.awt.Container;

import nc.ui.pub.querymodel.QEQueryDlg;

public class ReportQDLG extends QEQueryDlg {

	ReportQP reportQP = null;
	
	public ReportQDLG(Container arg0) {
		super(arg0);
		setNormalShow(true);
		hideUnitButton();
//		hideDefine();	// 隐藏自定义查询页签
		getUIPanelNormal().setLayout(new BorderLayout());
		getUIPanelNormal().add(getQueryPanel(), BorderLayout.NORTH);
	}
	/**
	 * 返回 查询面板
	 */
	public ReportQP getQueryPanel() {
		if (reportQP == null)
			reportQP = new ReportQP();
		return reportQP;
	}
	
	
}
