package nc.itf.mm.scm.mm6600;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.mo.mo6600.GlBillVO;

public interface IMo6600
{
	//ȷ�ϵ���  �������ɲ���Ʒ��ⵥ  ���޸Ĳɹ�����ɡ��������
	public void confirm(GlBillVO billvo) throws Exception;
	//�޸ı��浥��
	public GlBillVO saveBill(GlBillVO billVO) throws Exception;
	//���ε��ݻ�д״̬
	public void Writeback(GeneralBillVO vobill)throws Exception;
}
