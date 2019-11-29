package nc.ui.by.invapp.button;

import nc.ui.trade.button.IBillButton;

/**
 * 说明: 按钮常量
 */
public interface IBYButton extends IBillButton {
	/** 锁定 */
	public int BTN_LOCK = 200;
	/** 库存导入 */
	public int BTN_KCDR = 201;
	/** 库存校验 */
	public int BTN_KCJY = 202;
	/** 隔离库存导入 */
	public int BTN_GLDR = 203;
	/** 隔离库存校验 */
	public int BTN_GLJY = 204;
	/** 清空导入库存 */
	public int BTN_QKDRKC = 205;
	
}
