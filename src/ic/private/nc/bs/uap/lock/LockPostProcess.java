package nc.bs.uap.lock;

import nc.bs.framework.component.RemoteProcessComponent;
import nc.bs.logging.Logger;

public class LockPostProcess
  implements RemoteProcessComponent
{
  public void postProcess()
  {
    Logger.debug("Enter LockPostProcess.postProcess");
    try {
      PKLock.getInstance().releaseDynamicLocks();
    } catch (Throwable localThrowable) {
      Logger.error("LockPostProcess.postProcess Relase dynamic pklock error:", localThrowable);
    }
    Logger.debug("Leave LockPostProcess.postProcess");
  }

  public void preProcess() {
  }

  public void postErrorProcess(Throwable paramThrowable) {
    Logger.debug("Enter LockPostProcess.postErrorProcess");
    try {
      PKLock.getInstance().releaseDynamicLocks();
    } catch (Throwable localThrowable) {
      Logger.error("LockPostProcess.postErrorProcess Relase dynamic pklock error:", localThrowable);
    }
    Logger.debug("Leave LockPostProcess.postErrorProcess");
  }
}