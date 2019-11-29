package nc.bs.uap.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nc.bs.framework.common.IAttributeManager;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.component.RemoteProcessComponent;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.framework.exception.ComponentException;
import nc.bs.framework.exception.FrameworkRuntimeException;
import nc.bs.logging.Log;
import nc.itf.uap.IPKLockBS;
import nc.itf.uap.LockableVO;
import nc.vo.pub.BusinessRuntimeException;

public class PKLock
{
  protected static long MIN_WAIT = 100L;
  public static final String PKLOCK_ATTR = "pklock";
  protected Log logger = Log.getInstance("pklock");
  protected IPKLockBS pkLock;
  private IAttributeManager a;
  private RemoteProcessComponetFactory b;
  private static PKLock c = null;

  private PKLock()
  {
    try
    {
      this.pkLock = ((IPKLockBS)NCLocator.getInstance().lookup("pklock"));
      if (RuntimeEnv.getInstance().isRunningInServer()) {
        try {
          this.a = ((IAttributeManager)NCLocator.getInstance().lookup("ThreadLocalAttributeManager"));
        } catch (Exception localException) {
          this.logger.warn("Not found ThreadLocalAttributeManager, mayby server has not been started or you call PKLock in ui");
        }

      }
      else
      {
        return;
      }
    }
    catch (ComponentException localComponentException)
    {
      this.pkLock = null;
      this.logger.error("Cannot initialize uapBService.", localComponentException);
    }
  }

  public static PKLock getInstance()
  {
    return c;
  }

  /** @deprecated */
  public void acquireAsynLock(String paramString1, String paramString2, String paramString3)
  {
    a();

    this.logger.info("Deprecated method acquireAsynLock called, with params " + paramString1 + " " + paramString2 + " " + paramString3 + ".");

    while (!this.pkLock.acquireLock_RequiresNew(paramString1, paramString3))
      try {
        Thread.sleep(MIN_WAIT);
      } catch (InterruptedException localInterruptedException) {
        this.logger.error("Thread Interrupted exception error acquiring asyn lock on" + paramString1);
      }
  }

  public boolean acquireAsynLock(String paramString1, String paramString2, String paramString3, long paramLong)
  {
    a();
    this.logger.info("Method acquireAsynLock called, with params " + paramString1 + " " + paramString2 + " " + paramString3 + ".");
    if (paramLong < MIN_WAIT * 10L) {
      this.logger.info("LockManager.acquireAsynLock should wait longer.");
    }

    boolean bool = this.pkLock.acquireLock_RequiresNew(paramString1, paramString3);
    while (!bool)
    {
      try {
        if ((
          paramLong = paramLong - MIN_WAIT) <= 
          0L)
          break;
        Thread.sleep(MIN_WAIT);
      } catch (InterruptedException localInterruptedException) {
        this.logger.error("Thread Interrupted exception error acquiring asyn lock on" + paramString1);
      }
      bool = this.pkLock.acquireLock_RequiresNew(paramString1, paramString3);
    }

    if (bool) return bool; return this.pkLock.acquireLock_RequiresNew(paramString1, paramString3);
  }

  public boolean acquireLock(String paramString1, String paramString2, String paramString3)
  {
    a();

    return this.pkLock.acquireLock_RequiresNew(paramString1, paramString3);
  }

  public boolean isLocked(String paramString1, String paramString2, String paramString3)
  {
    a();

    return this.pkLock.isLocked_RequiresNew(paramString1, paramString2, paramString3);
  }

  public boolean acquireBatchLock(String[] paramArrayOfString, String paramString1, String paramString2)
  {
    a();

    return this.pkLock.acquireBatchLock_RequiresNew(paramArrayOfString, paramString2);
  }

  public void releaseLock(String paramString1, String paramString2, String paramString3)
  {
    a();
    this.pkLock.releaseLock_RequiresNew(paramString1, paramString2, paramString3);
  }

  public void releaseBatchLock(String[] paramArrayOfString, String paramString1, String paramString2)
  {
    a();
    this.pkLock.releaseBatchLock_RequiresNew(paramArrayOfString, paramString1, paramString2);
  }

  public void releaseLocks(String paramString1, String paramString2)
  {
    a();
    this.pkLock.releaseUserLocks_RequiresNew(paramString1, paramString2);
  }

  public void releaseLocks(String[] paramArrayOfString, String paramString)
  {
    a();
    if (null != paramArrayOfString)
      for (int i = 0; i < paramArrayOfString.length; i++) {
        String str = paramArrayOfString[i];
        releaseLocks(str, paramString);
      }
  }

  public LockableVO[] getUserLockVOs(String paramString1, String paramString2)
  {
    a();
    LockableVO[] arrayOfLockableVO;
    return arrayOfLockableVO = this.pkLock.getUserLockVOs_RequiresNew(paramString1, paramString2);
  }

  public LockableVO[] getAllLockVOs(String paramString)
  {
    a();
    LockableVO[] arrayOfLockableVO;
    return arrayOfLockableVO = this.pkLock.getAllLockVOs_RequiresNew(paramString);
  }

  private void a()
  {
    if (null == this.pkLock) {
      this.logger.error("No ILockManager instance.");
      throw new NullPointerException("Null pointer on ILockManager.");
    }
  }

  public LockableVO getLock(String paramString1, String paramString2) {
    LockableVO[] arrayOfLockableVO = getAllLockVOs(paramString1);
    for (int i = 0; i < arrayOfLockableVO.length; i++) {
      if (arrayOfLockableVO[i].getLockable().equals(paramString2))
        return arrayOfLockableVO[i];
    }
    return null;
  }

  public boolean addDynamicLock(String paramString)
  {
    if (RuntimeEnv.getInstance().isRunningInServer())
    {
      Object localObject1;
      if ((
        localObject1 = (Map)this.a.getAttribute("pklock")) == null)
      {
        localObject1 = new HashMap();
        getManager().setAttribute("pklock", localObject1);
      }
      RemoteProcessComponetFactory localRemoteProcessComponetFactory;
      if ((
        localRemoteProcessComponetFactory = getRemoteProcessComponetFactory())
        .getThreadScopePostProcess("PKLOCK") == null) {
        try {
          localRemoteProcessComponetFactory.addThreadScopePostProcess("PKLOCK", (RemoteProcessComponent)Class.forName("nc.bs.uap.lock.LockPostProcess").newInstance());
        }
        catch (Exception localException) {
          throw new FrameworkRuntimeException(" Can't add dynamiclock", localException);
        }
      }

      LockDsPair localLockDsPair = new LockDsPair(InvocationInfoProxy.getInstance().getUserDataSource(), paramString);
      String str = InvocationInfoProxy.getInstance().getUserCode();
      Object localObject2;
      if ((
        localObject2 = (Set)((Map)localObject1).get(str)) == null)
      {
        localObject2 = new HashSet();
        ((Map)localObject1).put(str, localObject2);
      }
      if (((Set)localObject2).contains(localLockDsPair))
        return true;
      boolean bool;
      if ((
        bool = acquireLock(paramString, null, null)))
      {
        ((Set)localObject2).add(localLockDsPair);
      }
      else return false;

      return true;
    }
    throw new BusinessRuntimeException("Don't call addDynamicLock in  ui end");
  }

  public boolean addBatchDynamicLock(String[] paramArrayOfString)
  {
    if (RuntimeEnv.getInstance().isRunningInServer())
    {
      Object localObject1;
      if ((
        localObject1 = (Map)this.a.getAttribute("pklock")) == null)
      {
        localObject1 = new HashMap();
        getManager().setAttribute("pklock", localObject1);
      }
      RemoteProcessComponetFactory localRemoteProcessComponetFactory;
      if ((
        localRemoteProcessComponetFactory = getRemoteProcessComponetFactory())
        .getThreadScopePostProcess("PKLOCK") == null) {
        try {
          localRemoteProcessComponetFactory.addThreadScopePostProcess("PKLOCK", (RemoteProcessComponent)Class.forName("nc.bs.uap.lock.LockPostProcess").newInstance());
        }
        catch (Exception localException) {
          throw new FrameworkRuntimeException(" Can't add dynamiclock", localException);
        }
      }

      String str1 = InvocationInfoProxy.getInstance().getUserDataSource();
      LockDsPair[] arrayOfLockDsPair = new LockDsPair[paramArrayOfString.length];
      for (int i = 0; i < paramArrayOfString.length; i++)
        arrayOfLockDsPair[i] = new LockDsPair(str1, paramArrayOfString[i]);
      String str2 = InvocationInfoProxy.getInstance().getUserCode();
      Object localObject2;
      if ((
        localObject2 = (Set)((Map)localObject1).get(str2)) == null)
      {
        localObject2 = new HashSet();
        ((Map)localObject1).put(str2, localObject2);
      }
      int j = 0;
      ArrayList localArrayList = new ArrayList();
      for (j = 0; j < arrayOfLockDsPair.length; j++) {
        if (!((Set)localObject2).contains(arrayOfLockDsPair[j])) {
          localArrayList.add(arrayOfLockDsPair[j]);
        }
      }

      if (localArrayList.size() == 0) {
        return true;
      }
      String[] arrayOfString = new String[localArrayList.size()];
      for (j = 0; j < localArrayList.size(); j++)
        arrayOfString[j] = ((LockDsPair)localArrayList.get(j)).b;
      boolean bool;
      if ((
        bool = acquireBatchLock(arrayOfString, null, null)))
      {
        ((Set)localObject2).addAll(localArrayList);
      }
      else return false;

      return true;
    }
    throw new BusinessRuntimeException("Don't call addbatchDynamicLock in  ui end");
  }

  private Map getDynamicLockMap()
  {
    return (Map)getManager().getAttribute("pklock");
  }

  public void releaseDynamicLocks()
  {
    if (RuntimeEnv.getInstance().isRunningInServer())
    {
      Map localMap;
      if ((
        localMap = getDynamicLockMap()) == null)
      {
        return;
      }Iterator localIterator1 = localMap.keySet().iterator();
      HashMap localHashMap;
      Object localObject;
      Iterator localIterator2;
      while (localIterator1.hasNext()) {
        String str = (String)localIterator1.next();
        Set localSet;
        if ((
          localSet = (Set)localMap.get(str)) != null)
        {
          LockDsPair[] arrayOfLockDsPair = new LockDsPair[localSet.size()];
          localSet.toArray(arrayOfLockDsPair);
          localHashMap = new HashMap();
          for (int i = 0; i < arrayOfLockDsPair.length; i++)
          {
            if ((
              localObject = (List)localHashMap.get(arrayOfLockDsPair[i].a)) == null)
            {
              localObject = new ArrayList();
              localHashMap.put(arrayOfLockDsPair[i].a, localObject);
            }
            ((List)localObject).add(arrayOfLockDsPair[i].b);
          }

          for (localIterator2 = localHashMap.keySet().iterator(); localIterator2.hasNext(); ) {
            localObject = (String)localIterator2.next();
            List localList;
            if (((
              localList = (List)localHashMap.get(localObject)) != null) && 
              (localList.size() > 0)) {
              releaseBatchLock((String[])(String[])localList.toArray(new String[localList.size()]), null, (String)localObject);
            }
          }
        }

      }

      localMap.clear();
      return;
    }throw new BusinessRuntimeException("Don't call addDynamicLock in  ui end");
  }

  private IAttributeManager getManager()
  {
    if (this.a != null) {
      return this.a;
    }
    this.a = ((IAttributeManager)NCLocator.getInstance().lookup("ThreadLocalAttributeManager"));
    return this.a;
  }

  private RemoteProcessComponetFactory getRemoteProcessComponetFactory()
  {
    if (this.b != null) {
      return this.b;
    }
    this.b = ((RemoteProcessComponetFactory)NCLocator.getInstance().lookup("RemoteProcessComponetFactory"));

    return this.b;
  }

  static
  {
    c = new PKLock();
  }

  private static class LockDsPair
  {
    public String a;
    public String b;

    public LockDsPair(String paramString1, String paramString2)
    {
      this.a = paramString1;
      this.b = paramString2;
    }

    public boolean equals(Object paramObject) {
      if ((paramObject instanceof LockDsPair)) {
        LockDsPair localLockDsPair = (LockDsPair)paramObject;
        return (this.a.equals(localLockDsPair.a)) && (this.b.equals(localLockDsPair.b));
      }
      return false;
    }

    public int hashCode() {
      return this.a.hashCode() * 37 + this.b.hashCode();
    }
  }
}