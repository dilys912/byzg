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
//		hideDefine();	// �����Զ����ѯҳǩ
		getUIPanelNormal().setLayout(new BorderLayout());
		getUIPanelNormal().add(getQueryPanel(), BorderLayout.NORTH);
	}
	/**
	 * ���� ��ѯ���
	 */
	public ReportQP getQueryPanel() {
		if (reportQP == null)
			reportQP = new ReportQP();
		return reportQP;
	}
	
	
}
