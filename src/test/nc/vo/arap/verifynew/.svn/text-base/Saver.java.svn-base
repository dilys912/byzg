package nc.vo.arap.verifynew;

import java.util.ArrayList;
import nc.bs.logging.Log;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.prv.IArapVerifyLogPrivate;
import nc.vo.pub.BusinessException;
import nc.vo.verifynew.pub.IDataSave;
import nc.vo.verifynew.pub.VerifyLogVO;

public class Saver
  implements IDataSave
{
  private static final long serialVersionUID = -2687453589823311299L;

  public String onSave(VerifyLogVO[] logvos, ArrayList jsvo)
    throws BusinessException
  {
    String result = null;
    try
    {
      String clbh = Proxy.getIArapVerifyLogPrivate().save(logvos, jsvo);
      String strclbh = clbh + "";
      for (int i = 0; i < logvos.length; i++) {
        logvos[i].setM_clbh(strclbh);
      }
      result = strclbh;
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error("Verify saver error!", e);
      throw new BusinessException(e.getMessage());
    }

    return result;
  }

  public String[] unSave(String[] hepch) throws BusinessException
  {
    String[] result = null;
    try
    {
      result = Proxy.getIArapVerifyLogPrivate().unSaveVerifyLog(hepch);
    }
    catch (Exception e)
    {
      throw new BusinessException(e);
    }
    return result;
  }
}