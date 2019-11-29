package nc.bs.framework.naming;

import nc.bs.framework.exception.ComponentException;

public abstract interface Context
{
  public static final String LOCAL_EJB_JNDI_PREFIX = "java:comp/env/ejb/";
  public static final String LOCAL_JDBC_JNDI_PREFIX = "java:comp/env/jdbc/";
  public static final String LOCAL_JNDI_PREFIX = "java:comp/env/";
  public static final String SERVER = "nc.bs.framework.server.BusinessAppServer";
  public static final String REGIST_SERVICE = "nc.bs.framework.common.RegistService";
  public static final String REMOTE_META_SERVICE = "nc.bs.framework.server.RemoteMetaContext";

  public abstract Object lookup(String paramString)
    throws ComponentException;
}