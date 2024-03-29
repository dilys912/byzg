package nc.vo.by.invapp.pub;


import nc.ui.trade.button.IBillButton;
/**
 * 说明:基础常量
 */
public interface IBYConst {
	/** 标准树卡片界面按钮*/
	public int[] TREECARD_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,IBillButton.Save,
			IBillButton.Cancel,IBillButton.Refresh};
	/** 标准树列表界面按钮*/
	public int[]TREELIST_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,
            IBillButton.Refresh,   };
    /** 标准列表界面按钮     */
    public int[] LIST_BUTTONS={ IBillButton.Query, IBillButton.Add, IBillButton.Edit,IBillButton.Delete, 
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Refresh};
    /** 标准管理列表按钮     */
    public int[] LIST_BUTTONS_M={ IBillButton.Query, IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Card,IBillButton.Refresh,IBillButton.Copy };
    /**  标准管理卡片按钮--彩生活     */
    public int[] CARD_BUTTONS_M_CSH={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };   
    /**  标准管理卡片按钮     */
    public int[] CARD_BUTTONS_M={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };  
}
