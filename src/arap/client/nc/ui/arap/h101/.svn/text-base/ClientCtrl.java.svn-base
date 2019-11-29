package nc.ui.arap.h101; 
  
import nc.ui.by.invapp.billmanage.AbstractCtrl;
import nc.ui.by.invapp.button.ButtonTool;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
  
/** 
 * @author 自动生成 
*/ 
public class ClientCtrl extends AbstractCtrl { 
  
 	public String getBillType() { 
 		return "H101"; 
 	} 
  
 	public String[] getBillVoName() { 
 		return new String[] {  
 				HYBillVO.class.getName(), 
 				nc.vo.arap.h101.FksqdVO.class.getName(), 
 				nc.vo.arap.h101.FksqdBVO.class.getName()}; 
 	} 
  //修改方法：取消业务类型（Busitype）查询条件  by zy  2019.08.07
 	public int[] getCardButtonAry() { 
 		return CARD_BUTTONS_M;
// 		return ButtonTool.insertButton(IBillButton.Busitype, CARD_BUTTONS_M, 0);
 	} 
  
 	public int[] getListButtonAry() { 
// 		return super.getListButtonAry();
// 		return ButtonTool.insertButtons(new int[]{IBillButton.Busitype,IBillButton.Print}, LIST_BUTTONS_M, 0); 
 		return ButtonTool.insertButtons(new int[]{IBillButton.Print}, LIST_BUTTONS_M, 0); 
 	} 
 	//end
  
 	public String getPkField() { 
 		return "pk_fksqd"; 
 	} 
  
 	@Override 
 	public String getChildPkField() { 
 		return null; 
 	} 

	/** 标准树卡片界面按钮*/
	public int[] TREECARD_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,IBillButton.Save,
			IBillButton.Cancel,IBillButton.Refresh};
	/** 标准树列表界面按钮*/
	public int[]TREELIST_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,
            IBillButton.Refresh,   };
    /** 标准管理列表按钮     */
    public int[] LIST_BUTTONS_M={ IBillButton.Query, IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Card,IBillButton.Refresh,IBillButton.Copy };
    /**  标准管理卡片按钮     */
    public int[] CARD_BUTTONS_M={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };  
} 
