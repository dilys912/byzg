package nc.vo.scm.pub.smart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

public class ObjectUtils
{
  public static Object serializableClone(Object oIn)
    throws Exception
  {
    try
    {
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      ObjectOutputStream o = new ObjectOutputStream(buf);
      o.writeObject(oIn);

      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));

      return in.readObject();
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
    catch (ClassNotFoundException ex)
    {
      ex.printStackTrace();
      throw ex;
    }
  }

  public static int getObjectOnNetSize(Object obj)
  {
    try
    {
      ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
      ObjectOutputStream objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);

      objectoutputstream.writeObject(obj);
      System.out.println("vo对象：" + bytearrayoutputstream.size() + "]byte");

      return bytearrayoutputstream.size();
    }
    catch (IOException ioexception)
    {
      ioexception.printStackTrace();
    }
    return 0;
  }

  public static void objectReference(Object o)
    throws Exception
  {
    SCMRuntimeDebug.showDebug("对象引用关系检查开始");
    HashMap m = new HashMap();
    objectReference(o, m);
    SCMRuntimeDebug.showDebug("对象引用关系检查结束");
  }

  private static void objectReference(Object o, HashMap m)
  {
    if (o == null)
      return;
    if ((o instanceof CircularlyAccessibleValueObject[]))
    {
      CircularlyAccessibleValueObject[] t = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])o;
      if (t != null) {
        int i = 0; for (int j = t.length; i < j; i++)
        {
          objectReference1(t[i], m);
        }
      }
    } else if ((o instanceof CircularlyAccessibleValueObject))
    {
      objectReference1((CircularlyAccessibleValueObject)o, m);
    }
    else if ((o instanceof AggregatedValueObject))
    {
      objectReference1((AggregatedValueObject)o, m);
    }
    else if ((o instanceof AggregatedValueObject[]))
    {
      AggregatedValueObject[] t = (AggregatedValueObject[])(AggregatedValueObject[])o;
      if (t != null) {
        int i = 0; for (int j = t.length; i < j; i++)
        {
          objectReference1(t[i], m);
        }
      }
    } else if ((o instanceof ArrayList))
    {
      objectReference1((ArrayList)o, m);
    }
    else if ((o instanceof Object[]))
    {
      objectReference1((Object[])(Object[])o, m);
    }
    else if ((o instanceof Object[][]))
    {
      objectReference1((Object[][])(Object[][])o, m);
    }
    else if ((o instanceof Vector))
    {
      objectReference1((Vector)o, m);
    }
    else if ((o instanceof Hashtable))
    {
      objectReference1((Hashtable)o, m);
    }
    else if ((o instanceof Map))
    {
      objectReference1((Map)o, m);
    }
    else {
      return;
    }
  }

  private static void objectReference1(AggregatedValueObject o, HashMap m) {
    AggregatedValueObject t = o;
    objectReference1(t.getParentVO(), m);
    CircularlyAccessibleValueObject[] b = (CircularlyAccessibleValueObject[])t.getChildrenVO();

    if (b != null) {
      int i = 0; for (int j = b.length; i < j; i++)
      {
        objectReference1(b[i], m);
      }
    }
  }

  private static void objectReference1(ArrayList o, HashMap m)
  {
    if (o == null) {
      return;
    }

    if (o.size() <= 0) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = o.size(); i < loop; i++)
    {
      ovalue = o.get(i);

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o.set(i, value);
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static void objectReference1(Object[] o, HashMap m)
  {
    if (o == null) {
      return;
    }

    if (o.length <= 0) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = o.length; i < loop; i++)
    {
      ovalue = o[i];

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o[i] = value;
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static void objectReference1(Object[][] o, HashMap m)
  {
    if (o == null) {
      return;
    }

    if (o.length <= 0) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int j = 0; for (int jloop = o.length; j < jloop; j++) {
      int i = 0; for (int loop = o[j].length; i < loop; i++)
      {
        ovalue = o[j][i];

        if (ovalue == null) {
          continue;
        }
        skey = objectCoding(ovalue);

        if (skey.equals("##"))
        {
          value = null;
        }
        else if (skey.equals("??"))
        {
          objectReference(ovalue, m);
        }
        else
        {
          value = m.get(skey);
          if (value != null)
          {
            o[j][i] = value;
          }
          else
          {
            m.put(skey, ovalue);
          }
        }
      }
    }
  }

  private static void objectReference1(Vector o, HashMap m)
  {
    if (o == null) {
      return;
    }

    if (o.size() <= 0) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = o.size(); i < loop; i++)
    {
      ovalue = o.get(i);

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o.set(i, value);
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static void objectReference1(Hashtable o, HashMap m)
  {
    if (o == null) {
      return;
    }
    Object[] keys = o.keySet().toArray();

    if (keys == null) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = keys.length; i < loop; i++)
    {
      ovalue = o.get(keys[i]);

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o.remove(keys[i]);
          o.put(keys[i], value);
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static void objectReference1(Map o, HashMap m)
  {
    if (o == null) {
      return;
    }
    Object[] keys = o.keySet().toArray();

    if (keys == null) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = keys.length; i < loop; i++)
    {
      ovalue = o.get(keys[i]);

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o.remove(keys[i]);
          o.put(keys[i], value);
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static void objectReference1(CircularlyAccessibleValueObject o, HashMap m)
  {
    if (o == null) {
      return;
    }
    String[] keys = o.getAttributeNames();

    if (keys == null) {
      return;
    }
    String skey = null;
    Object value = null; Object ovalue = null;

    int i = 0; for (int loop = keys.length; i < loop; i++)
    {
      ovalue = o.getAttributeValue(keys[i]);

      if (ovalue == null) {
        continue;
      }
      skey = objectCoding(ovalue);

      if (skey.equals("##"))
      {
        value = null;
      }
      else if (skey.equals("??"))
      {
        objectReference(ovalue, m);
      }
      else
      {
        value = m.get(skey);
        if (value != null)
        {
          o.setAttributeValue(keys[i], value);
        }
        else
        {
          m.put(skey, ovalue);
        }
      }
    }
  }

  private static String objectCoding(Object ovalue)
  {
    if (ovalue == null)
      return "##";
    String skey = null;

    if (ovalue.getClass() == String.class)
    {
      if (((String)ovalue).trim().toString().length() <= 0)
      {
        skey = "##";
      }
      else
      {
        skey = "0" + ovalue.toString();
      }
    }
    else if (ovalue.getClass() == UFDouble.class)
    {
      skey = "1" + ovalue.toString();
    }
    else if (ovalue.getClass() == UFDate.class)
    {
      skey = "2" + ovalue.toString();
    }
    else if (ovalue.getClass() == UFDateTime.class)
    {
      skey = "3" + ovalue.toString();
    }
    else if (ovalue.getClass() == UFBoolean.class)
    {
      skey = "4" + ovalue.toString();
    }
    else if (ovalue.getClass() == UFTime.class)
    {
      skey = "5" + ovalue.toString();
    }
    else if (ovalue.getClass() == Integer.class)
    {
      skey = "6" + ovalue.toString();
    }
    else if (ovalue.getClass() == Double.class)
    {
      skey = "7" + ovalue.toString();
    }
    else if (ovalue.getClass() == Long.class)
    {
      skey = "8" + ovalue.toString();
    }
    else if (ovalue.getClass() == Byte.class)
    {
      skey = "9" + ovalue.toString();
    }
    else if (ovalue.getClass() == Short.class)
    {
      skey = "A" + ovalue.toString();
    }
    else if (ovalue.getClass() == Float.class)
    {
      skey = "B" + ovalue.toString();
    }
    else if (ovalue.getClass() == Character.class)
    {
      skey = "C" + ovalue.toString();
    }
    else if (ovalue.getClass() == Boolean.class)
    {
      skey = "D" + ovalue.toString();
    }
    else if (ovalue.getClass() == BigDecimal.class)
    {
      skey = "E" + ovalue.toString();
    }
    else if (ovalue.getClass() == BigInteger.class)
    {
      skey = "F" + ovalue.toString();
    }
    else
    {
      skey = "??";
    }
    return skey;
  }

  public static void fullArray(Object[] src, Object[] dest)
  {
    if ((src == null) || (dest == null))
      return;
    System.arraycopy(src, 0, dest, 0, src.length > dest.length ? dest.length : src.length);
  }

  public static void main(String[] args)
  {
    String[] dest = { "sdsf" };

    fullArray(new String[] { "sdf" }, dest);

    System.out.println(dest[0]);
  }
}