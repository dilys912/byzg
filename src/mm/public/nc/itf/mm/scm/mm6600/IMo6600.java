package nc.itf.mm.scm.mm6600;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.mo.mo6600.GlBillVO;

public interface IMo6600
{
	//确认单据  用于生成产成品入库单  并修改采购单完成、入库数量
	public void confirm(GlBillVO billvo) throws Exception;
	//修改保存单据
	public GlBillVO saveBill(GlBillVO billVO) throws Exception;
	//下游单据回写状态
	public void Writeback(GeneralBillVO vobill)throws Exception;
}
