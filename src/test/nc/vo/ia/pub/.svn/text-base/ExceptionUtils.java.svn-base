package nc.vo.ia.pub;

import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;

public class ExceptionUtils
{
  public static void marsh(Exception ex)
    throws BusinessException
  {
    Throwable cause = unmarsh(ex);
    Log.error(cause);

    if ((cause instanceof BusinessException)) {
      throw ((BusinessException)cause);
    }
    if ((cause instanceof BusinessRuntimeException)) {
      throw new BusinessException(cause);
    }
    if ((cause instanceof TransferException)) {
      throw new BusinessException(cause);
    }

    TransferException tex = new TransferException(cause);
    throw new BusinessException(tex);
  }

  public static Throwable unmarsh(Throwable ex)
  {
    Throwable cause = ex.getCause();
    if (cause != null) {
      cause = unmarsh(cause);
    }
    else {
      cause = ex;
    }
    return cause;
  }

  public static void wrappBusinessException(String message) {
    IABusinessException ex = new IABusinessException(message);
    throw new SystemAccessException(ex);
  }

  public static void wrappBusinessException(String message, Throwable exception) {
    IABusinessException ex = new IABusinessException(message, exception);
    throw new SystemAccessException(ex);
  }

  public static void wrappException(Exception ex)
  {
    throw new SystemAccessException(ex);
  }

  public static void unSupported() {
    String message = "不支持此种业务，请检查";
    IABusinessException ex = new IABusinessException(message);
    throw new SystemAccessException(ex);
  }
}