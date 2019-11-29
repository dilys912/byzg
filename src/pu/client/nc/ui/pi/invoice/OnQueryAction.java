package nc.ui.pi.invoice;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.vo.ic.pub.check.CHECKVO;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.lang.UFDouble;


	


	public class OnQueryAction  extends UIDialog {
		private BillCardPanel parent;
		/** UI JPanel */
		private JPanel uiContentPane;
	 
		
		private BillCardPanel billPanel;
		public OnQueryAction(BillCardPanel parent,List<CHECKVO> list) {
			super(parent);
			this.parent = parent;
			this.initialize();
			this.initData(list);
			
		}
		private void initialize() {
			this.setName("核销记录");
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setTitle("核销记录");
			int w = 800;
			int h = 600;
			this.setBounds(500, 350, w, h);
			this.setResizable(true);
			// 设置模板
			this.setContentPane(this.getUIContentPane());
	 
		}
		//
		public void initData(List<CHECKVO> list) {
					
			ArrayList<String []> vos =  new ArrayList<String[]>();
			
			
			int row = 0;
			for(int i=0;i<list.size();i++){			
		
			CHECKVO  vo = list.get(i);
			this.billPanel.addLine();
						
			this.billPanel.getBillModel().setBodyRowVO(vo,row);					
				
			
			row++;
				
			}	
//			}
			
			this.billPanel.getBillModel().execLoadFormula();//执行加载公式
	
		}
		private Container getUIContentPane() {
			if (null == this.uiContentPane) {
				this.uiContentPane = new JPanel();
				this.uiContentPane.setName("UIDialogContentPane");
				this.uiContentPane.setLayout(new BorderLayout());
				this.getUIContentPane().add(this.getBillPanel(), "Center");
			}
			return this.uiContentPane;
		}
		/**
		 * 加载单据模板初始化配置“核销”模板id
		 * @return
		 */
		private BillCardPanel getBillPanel() {
			if (this.billPanel == null) {
				this.billPanel = new BillCardPanel();
				this.billPanel.setName("cancelafterverification");
				this.billPanel.loadTemplet("0001AA100000000L4O9H");
				this.billPanel.getBillTable().setSelectionMode(
						javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//				this.billPanel.setTatolRowShow(true);
				this.billPanel.setBodyMenuShow(false);
				this.billPanel.setEnabled(false);
			}
			return this.billPanel;
		}

}
