package nc.ui.scm.service;

import java.util.ArrayList;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.service.ServcallVO;

public class LocalCallService
{
  public static Object[] callService(ServcallVO[] scds)
    throws Exception
  {
    if ((scds == null) || (scds.length == 0)) {
      return null;
    }
    ArrayList ps = new ArrayList();
    int i = 0; for (int j = scds.length; i < j; i++) {
      ps.add(scds[i].getParameter());
    }

    ObjectUtils.objectReference(ps);

    return ServiceProxyHelper.call(scds);
  }

  public static Object[] callEJBService(String modulename, ServcallVO[] scds)
    throws Exception
  {
    if ((scds == null) || (scds.length == 0)) {
      return null;
    }
    ArrayList ps = new ArrayList();
    int i = 0; for (int j = scds.length; i < j; i++) {
      ps.add(scds[i].getParameter());
    }

    ObjectUtils.objectReference(ps);

    return ServiceProxyHelper.callEJBService(modulename, scds);
  }

  public static Object[] callService(String modulename, ServcallVO[] scds)
    throws Exception
  {
    if ((scds == null) || (scds.length == 0)) {
      return null;
    }
    ArrayList ps = new ArrayList();
    int i = 0; for (int j = scds.length; i < j; i++) {
      ps.add(scds[i].getParameter());
    }

    ObjectUtils.objectReference(ps);

    return ServiceProxyHelper.callService(modulename, scds);
  }
}