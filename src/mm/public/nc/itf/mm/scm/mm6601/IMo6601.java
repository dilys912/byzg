package nc.itf.mm.scm.mm6601;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.mo.mo6601.GlclBillVO;

public interface IMo6601
{
	//下线隔离处理
	public void doXiaXianGl(GlclBillVO billvo,int[] rows) throws Exception;
	//返工处理
	public void doFanGong(GlclBillVO billVO,int[] rows) throws Exception;
	

}
