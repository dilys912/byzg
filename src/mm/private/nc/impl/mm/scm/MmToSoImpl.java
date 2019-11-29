package nc.impl.mm.scm;

import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.bs.mo.mo1020.MoBO;
import nc.itf.mm.scm.IMmToSo;
import nc.vo.mm.pub.MMBusinessException;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class MmToSoImpl
  implements IMmToSo
{
  public ArrayList createMOSO(MoVO mo, ArrayList polist, UFBoolean isPlatForm)
    throws BusinessException
  {
    try
    {
      return new MoBO().createMO(mo, polist, isPlatForm);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    throw new MMBusinessException(e.getMessage());
    }
  }

  public void createMOBatchSO(MoVO[] mos) throws BusinessException
  {
    try {
      new MoBO().createMO(mos);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      throw new MMBusinessException(e.getMessage());
    }
  }
}