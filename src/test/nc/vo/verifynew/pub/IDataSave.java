package nc.vo.verifynew.pub;

import java.io.Serializable;
import java.util.ArrayList;
import nc.vo.pub.BusinessException;

public abstract interface IDataSave extends Serializable
{
  public abstract String onSave(VerifyLogVO[] paramArrayOfVerifyLogVO, ArrayList paramArrayList)
    throws BusinessException;

  public abstract String[] unSave(String[] paramArrayOfString)
    throws BusinessException;
}