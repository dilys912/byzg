package nc.ui.bgzg.extButt;

import java.util.ArrayList;

import sun.java2d.loops.DrawGlyphListAA.General;

import nc.ui.ic.ic602.ClientUI;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.sf.menuext.FuncMenuExtends;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.scm.constant.ic.BillMode;

public class SaleOutButt extends FuncMenuExtends {
	private ButtonObject viewButt = null;
	private GeneralBillClientUI ui = null;
	
	public GeneralBillClientUI getClientUI(){
		if(ui==null){
			ui = (GeneralBillClientUI)getToftPanel();
		}
		return ui;
	}
	
	@Override
	public void addExtendsMenus(ArrayList<ButtonObject> bos) {
		if(getClientUI().getCurPanel()==BillMode.Card){
			bos.add(viewButt);
		}

	}

	@Override
	public void doExtendsAction(ButtonObject bo) {
		if (bo.equals(viewButt)) {
			if(getClientUI().getCurPanel()!=BillMode.Card)
				getClientUI().onSwitch();
			GeneralBillVO gbvo = getClientUI().getBillVO();
			if(gbvo!=null&&gbvo.getChildrenVO()!=null&&gbvo.getChildrenVO().length>0){
			nc.ui.ic.ic602.ClientUI toftpanel = (ClientUI) SFClientUtil.showNode("40083004", IFuncWindow.WINDOW_TYPE_FRAME);

				toftpanel.setGvo(gbvo);
				toftpanel.RefQuery();
				
			}else{
				MessageDialog.showErrorDlg(getClientUI(), "", "界面无数据,无法查询。");
				return;
			}
		}

	}

	public SaleOutButt() {
		super();
		viewButt = new ButtonObject("可用量查询及打印", "可用量查询及打印", "可用量查询及打印");
	}

}
