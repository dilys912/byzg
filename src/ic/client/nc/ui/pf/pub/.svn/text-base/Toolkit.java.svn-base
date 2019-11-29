package nc.ui.pf.pub;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;


public class Toolkit {
	public static String getBillNO(String billTypecode, String pk_corp,
			String customBillCode, BillCodeObjValueVO vo)
			throws BusinessException {
		return new BillcodeGenerater().getBillCode(billTypecode, pk_corp,
				customBillCode, vo);
	}
}
