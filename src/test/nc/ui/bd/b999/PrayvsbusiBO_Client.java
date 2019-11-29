package nc.ui.bd.b999;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.prayvsbusitype.IPrayvsBusiQry;

public class PrayvsbusiBO_Client
{
  public static Object[] getBusitypeDrp1(String arg0)
    throws Exception
  {
    Object[] types = null;
    IPrayvsBusiQry iIPrayvsBusiQry = (IPrayvsBusiQry)NCLocator.getInstance().lookup(IPrayvsBusiQry.class.getName());

    types = iIPrayvsBusiQry.getBusitypeDrp1(arg0);
    return types;
  }
}