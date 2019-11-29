package nc.bs.framework.server.util;

import nc.bs.framework.server.BusinessAppServer;

public class NewObjectService
{
  public static Object newInstance(String paramString1, String paramString2)
  {
    return BusinessAppServer.getInstance().newBizObject(paramString1, paramString2);
  }

  public static Object newInstance(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    return BusinessAppServer.getInstance().newBizObject(paramString1, paramString2, paramArrayOfObject);
  }

  public static Object newInstance(String paramString, Object[] paramArrayOfObject) {
    return BusinessAppServer.getInstance().newBizObject(paramString, paramArrayOfObject);
  }

  public static Object newInstance(String paramString) {
    return newInstance(paramString, new Object[0]);
  }
}