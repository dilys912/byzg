package nc.bs.ia.ia306;

import java.rmi.RemoteException;
import java.util.Hashtable;
import nc.bs.ia.pub.CommonDataDMO;
import nc.impl.ia.pub.CommonDataImpl;
import nc.vo.ia.ia306.AccountCheckVO;
import nc.vo.ia.ia306.GLVO;
import nc.vo.ia.ia306.ParamVO308;
import nc.vo.ia.pub.Log;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class COperate
{
  public AccountCheckVO[] checkNab(ParamVO308 p)
    throws Exception
  {
    DataCheck check = new DataCheck();
    return check.checkData(p);
  }

  private Integer execDataNoTrans(String sQueryCaluse)
    throws RemoteException
  {
    Integer i = null;
    try {
      CommonDataDMO dmo = new CommonDataDMO();
      i = dmo.execData(sQueryCaluse, false);
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("CommonDataBO::execData Exception!", e);
    }
    return i;
  }

  private boolean isNotZero(UFDouble d1)
  {
    return ((d1 != null) && (d1.doubleValue() != 0.0D));
  }

  public void modifyIAGL(ParamVO308 p)
    throws Exception
  {
    try
    {
      print("调整存货总帐冗余数据");
      boolean b = false;

      while (!(b)) {
        b = modifyIAGLError(p);
      }
      b = false;
      while (!(b)) {
        b = modifyIAGLBatchError(p);
      }
      print("调整存货总账");
      CDMO dmo = new CDMO();
      ParamVO308 voRP = null;
      GLVO[] voaGL = null;
      try {
        voRP = dmo.getSumIOL(p);
      }
      catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
      try {
        voaGL = dmo.queryAll(p.getPkCorp());
      }
      catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
      ParamVO308[] voaSum = voRP.getVOA();
      if ((voaSum == null) || (voaSum.length < 1)) {
        print("调整结束");
        return;
      }
      if ((voaGL == null) || (voaGL.length < 1)) {
        print("调整结束");
        return;
      }
      modifyIAGL1(p, voaGL, voaSum);

      print("调整结束");
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
    finally
    {
    }
  }

  public void modifyIAPF(ParamVO308 p) throws Exception
  {
    try
    {
      CDMO dmo = new CDMO();
      StringBuffer sb = new StringBuffer();
      sb.append("SELECT procmsg  FROM dap_finindex WHERE pk_sys = 'IA' AND pk_corp = '" + p.getPkCorp() + "' AND ");

      sb.append(" NOT EXISTS (SELECT 1 FROM ia_bill_b ");
      sb.append(" WHERE pk_corp = '" + p.getPkCorp() + "' AND  dr = 0 ");
      sb.append(" AND (ia_bill_b.cbill_bid = SUBSTRING(procmsg,1,20) OR ia_bill_b.cbillid = SUBSTRING(procmsg,1,20) OR ia_bill_b.csumrtvouchid = SUBSTRING(procmsg,1,20)))");

      String[][] sa2 = query(sb.toString());
      if ((sa2 == null) || (sa2.length == 0)) {
        return;
      }
      String sSql = null;
      String[] sa = new String[6];
      sa[0] = p.getPkCorp();
      for (int i = 0; i < sa2.length; ++i)
      {
        sSql = "DELETE FROM dap_finindex WHERE procmsg = " + toS(sa2[i][0]);

        print(sSql);
        execDataTrans(sSql);
        sa[1] = sa2[i][0];
        sa[2] = "NULL";
        sa[3] = "NULL";
        sa[4] = sSql;
        sa[5] = "NULL";
        dmo.insert(sa);
      }
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
    finally
    {
    }
  }

  private void print(String param)
  {
    Log.info(param);
  }

  private String[][] query(String sSQL)
    throws RemoteException
  {
    try
    {
      String[][] s = (String[][])null;
      CommonDataDMO dmo = new CommonDataDMO();
      s = dmo.queryData(sSQL);
      return s;
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
  }

  private String toS(String param)
  {
    if (param == null) {
      return "''";
    }
    return "'" + param + "' ";
  }

  private Integer execDataTrans(String sQueryCaluse)
    throws RemoteException
  {
    Integer i = null;
    try {
      CommonDataDMO dmo = new CommonDataDMO();
      i = dmo.execData(sQueryCaluse, true);
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("CommonDataBO::execData Exception!");
    }
    return i;
  }

  private void modifyIAGL1(ParamVO308 p, GLVO[] voaGL, ParamVO308[] voaSum)
    throws Exception
  {
    try
    {
      CommonDataImpl cbo = new CommonDataImpl();
      Integer[] temps = cbo.getDataPrecision(p.getPkCorp());

      int mnyPrecision = temps[2].intValue();
      int numPrecision = temps[0].intValue();
      int pricePrecision = temps[1].intValue();

      CDMO dmo = new CDMO();
      Hashtable h = new Hashtable();
      String sKey = null;
      String sIndex = null;
      int idx = 0;
      int iFlag = 0;
      String sSql = null;
      String sOld = null;
      String[] sa = new String[6];
      sa[0] = p.getPkCorp();

      for (int i = 0; i < voaGL.length; ++i) {
        if (voaGL[i].getVbatch() == null) {
          h.put(voaGL[i].getCrdcenterid() + voaGL[i].getCinventoryid() + "NULL", "" + i);
        }
        else
        {
          h.put(voaGL[i].getCrdcenterid() + voaGL[i].getCinventoryid() + voaGL[i].getVbatch(), "" + i);
        }
      }

      String sYear = null;
      String sMonth = null;
      String str = "select caccountyear , caccountmonth  from ia_generalledger WHERE frecordtypeflag = 4 and pk_corp = " + toS(p.getPkCorp());

      String[][] sa11 = query(str);
      sYear = sa11[0][0];
      sMonth = sa11[0][1];

      GLVO voR = null;
      Object o;
      for (int i = 0; i < voaSum.length; ++i) {
        sSql = "";
        sOld = "";
        sKey = voaSum[i].getPkCalbody() + voaSum[i].getPkInv() + voaSum[i].getVBatch();

        o = h.get(sKey);
        if (o == null) {
          sIndex = null;
        }
        else {
          sIndex = "X";
        }
        if (sIndex == null) {
          iFlag = 1;
          GLVO[] voRA = dmo.findByCalbodyAndInv(voaSum[i].getPkCalbody(), voaSum[i].getPkInv(), voaSum[i].getVBatch());

          if ((voRA != null) && (voRA.length > 0)) {
            voR = voRA[0];
          }
          else {
            voR = null;
          }

          GLVO vo = new GLVO();
          vo.setCaccountmonth(sMonth);
          vo.setCaccountyear(sYear);
          vo.setCinventoryid(voaSum[i].getPkInv());
          vo.setCrdcenterid(voaSum[i].getPkCalbody());
          if ((voaSum[i].getVBatch() != null) && (!(voaSum[i].getVBatch().equals("NULL")))) {
            vo.setVbatch(voaSum[i].getVBatch());
          }
          vo.setPk_corp(p.getPkCorp());
          vo.setFrecordtypeflag(new Integer(4));
          if (voR != null) {
            vo.setBtryflag(voR.getBtryflag());
            vo.setFpricemodeflag(voR.getFpricemodeflag());
          }
          else {
            vo.setBtryflag(new UFBoolean("N"));
            String s = "select pricemethod from bd_produce where  pk_calbody = " + toS(voaSum[i].getPkCalbody());

            s = s + "and pk_invmandoc =  " + toS(voaSum[i].getPkInv());
            String[][] sa2 = query(s);
            vo.setFpricemodeflag(new Integer(sa2[0][0]));
          }

          CDMO gdmo = new CDMO();
          gdmo.insertHeader(vo);
          sa[1] = voaSum[i].getPkCalbody();
          sa[2] = voaSum[i].getPkInv();
          sa[3] = voaSum[i].getVBatch();
          sa[4] = "GeneralledgerDMO.insertHeader";
          sa[5] = sYear + sMonth;
          dmo.insert(sa);
        }
      }
      if (iFlag == 1) {
        try {
          voaGL = dmo.queryAll(p.getPkCorp());
        }
        catch (Exception e) {
          e.printStackTrace();
          throw e;
        }
      }

      h = new Hashtable();
      for (int i = 0; i < voaSum.length; ++i) {
        h.put(voaSum[i].getPkCalbody() + voaSum[i].getPkInv() + voaSum[i].getVBatch(), "" + i);
      }

      for (int i = 0; i < voaGL.length; ++i) {
        sSql = "";
        sOld = "";
        sKey = voaGL[i].getCrdcenterid() + voaGL[i].getCinventoryid();
        if (voaGL[i].getVbatch() == null) {
          sKey = sKey + "NULL";
        }
        else {
          sKey = sKey + voaGL[i].getVbatch();
        }
        o = h.get(sKey);
        if (o == null) {
          sIndex = null;
        }
        else {
          sIndex = o.toString();
          idx = new Integer(sIndex).intValue();
        }
        if (sIndex != null) {
          if (compare(voaGL[i].getNinmny(), voaSum[idx].getInMny(), mnyPrecision))
          {
            sSql = sSql + " ninmny =" + voaSum[idx].getInMny() + ",";
            sOld = sOld + " ninmny =" + voaGL[i].getNinmny() + ",";
          }
          if (compare(voaGL[i].getNinnum(), voaSum[idx].getInNum(), numPrecision))
          {
            sSql = sSql + " ninnum =" + voaSum[idx].getInNum() + ",";
            sOld = sOld + " ninnum =" + voaGL[i].getNinnum() + ",";
          }
          if (compare(voaGL[i].getNoutmny(), voaSum[idx].getOutMny(), mnyPrecision))
          {
            sSql = sSql + " noutmny =" + voaSum[idx].getOutMny() + ",";
            sOld = sOld + " noutmny =" + voaGL[i].getNoutmny() + ",";
          }
          if (compare(voaGL[i].getNoutnum(), voaSum[idx].getOutNum(), numPrecision))
          {
            sSql = sSql + " noutnum =" + voaSum[idx].getOutNum() + ",";
            sOld = sOld + " noutnum =" + voaGL[i].getNoutnum() + ",";
          }
          if (compare(voaGL[i].getNabmny(), voaSum[idx].getNabMny(), mnyPrecision))
          {
            sSql = sSql + " nabmny =" + voaSum[idx].getNabMny() + ",";
            sOld = sOld + " nabmny =" + voaGL[i].getNabmny() + ",";
          }
          if (compare(voaGL[i].getNabnum(), voaSum[idx].getNabNum(), numPrecision))
          {
            sSql = sSql + " nabnum =" + voaSum[idx].getNabNum() + ",";
            sOld = sOld + " nabnum =" + voaGL[i].getNabnum() + ",";
          }
          if ((!(voaSum[idx].getNabNum().equals(new UFDouble(0)))) && (compare(voaGL[i].getNabprice(), voaSum[idx].getNabMny().div(voaSum[idx].getNabNum()), pricePrecision)))
          {
            sSql = sSql + " nabprice =" + voaSum[idx].getNabMny().div(voaSum[idx].getNabNum()).setScale(-pricePrecision, 4) + ",";

            sOld = sOld + " nabprice =" + voaGL[i].getNabprice() + ",";
          }
          if (compare(voaGL[i].getNinvarymny(), voaSum[idx].getInvarymny(), mnyPrecision))
          {
            sSql = sSql + " ninvarymny =" + voaSum[idx].getInvarymny() + ",";
            sOld = sOld + " ninvarymny =" + voaGL[i].getNinvarymny() + ",";
          }
          if (compare(voaGL[i].getNoutvarymny(), voaSum[idx].getOutvarymny(), mnyPrecision))
          {
            sSql = sSql + " noutvarymny =" + voaSum[idx].getOutvarymny() + ",";
            sOld = sOld + " noutvarymny =" + voaGL[i].getNoutvarymny() + ",";
          }
          if (compare(voaGL[i].getNabvarymny(), voaSum[idx].getAbvarymny(), mnyPrecision))
          {
            sSql = sSql + " nabvarymny =" + voaSum[idx].getAbvarymny() + ",";
            sOld = sOld + " nabvarymny =" + voaGL[i].getNabvarymny() + ",";
          }
          if (sSql.length() > 5) {
            sSql = "UPDATE ia_generalledger SET " + sSql.substring(0, sSql.length() - 1);

            sSql = sSql + " WHERE cgeneralledgerid =" + toS(voaGL[i].getCgeneralledgerid());

            print(sSql);
            execDataNoTrans(sSql);
            sa[1] = voaGL[i].getCrdcenterid();
            sa[2] = voaGL[i].getCinventoryid();
            sa[3] = voaGL[i].getVbatch();
            sa[4] = sSql;
            sa[5] = sOld.substring(0, sOld.length() - 1) + " WHERE cgeneralledgerid =" + toS(voaGL[i].getCgeneralledgerid());

            dmo.insert(sa);
          }
        }
        else {
          if (isNotZero(voaGL[i].getNinmny())) {
            sSql = sSql + " ninmny = 0,";
            sOld = sOld + " ninmny =" + voaGL[i].getNinmny() + ",";
          }
          if (isNotZero(voaGL[i].getNinnum())) {
            sSql = sSql + " ninnum = 0,";
            sOld = sOld + " ninnum =" + voaGL[i].getNinnum() + ",";
          }
          if (isNotZero(voaGL[i].getNoutmny())) {
            sSql = sSql + " noutmny = 0,";
            sOld = sOld + " noutmny =" + voaGL[i].getNoutmny() + ",";
          }
          if (isNotZero(voaGL[i].getNoutnum())) {
            sSql = sSql + " noutnum = 0,";
            sOld = sOld + " noutnum =" + voaGL[i].getNoutnum() + ",";
          }
          if (isNotZero(voaGL[i].getNabmny())) {
            sSql = sSql + " nabmny = 0,";
            sOld = sOld + " nabmny =" + voaGL[i].getNabmny() + ",";
          }
          if (isNotZero(voaGL[i].getNabnum())) {
            sSql = sSql + " nabnum = 0,";
            sOld = sOld + " nabnum =" + voaGL[i].getNabnum() + ",";
          }
          if (isNotZero(voaGL[i].getNinvarymny())) {
            sSql = sSql + " ninvarymny = 0,";
            sOld = sOld + " ninvarymny =" + voaGL[i].getNinvarymny() + ",";
          }
          if (isNotZero(voaGL[i].getNoutvarymny())) {
            sSql = sSql + " noutvarymny = 0,";
            sOld = sOld + " noutvarymny =" + voaGL[i].getNoutvarymny() + ",";
          }
          if (isNotZero(voaGL[i].getNabvarymny())) {
            sSql = sSql + " nabvarymny = 0,";
            sOld = sOld + " nabvarymny =" + voaGL[i].getNoutvarymny() + ",";
          }
          if (sSql.length() > 5) {
            sSql = "UPDATE ia_generalledger SET " + sSql.substring(0, sSql.length() - 1);

            sSql = sSql + " WHERE cgeneralledgerid =" + toS(voaGL[i].getCgeneralledgerid());

            print(sSql);
            execDataNoTrans(sSql);
            sa[1] = voaGL[i].getCrdcenterid();
            sa[2] = voaGL[i].getCinventoryid();
            sa[3] = voaGL[i].getVbatch();
            sa[4] = sSql;
            sa[5] = sOld.substring(0, sOld.length() - 1) + " WHERE cgeneralledgerid =" + toS(voaGL[i].getCgeneralledgerid());

            dmo.insert(sa);
          }
        }
      }
      print("调整结束");
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
    finally
    {
    }
  }

  private boolean modifyIAGLError(ParamVO308 p) throws Exception
  {
    try
    {
      CDMO dmo = new CDMO();
      boolean flag;
      CommonDataDMO cdmo = new CommonDataDMO();
      StringBuffer sb = new StringBuffer();

      if (cdmo.getDatabaseType() == 1) {
        sb.append("select max(cgeneralledgerid) ID from ia_generalledger where (crdcenterid,cinventoryid) in( ");

        sb.append("select crdcenterid , cinventoryid from ( ");
        sb.append("select crdcenterid, cinventoryid , count(*) cc from ia_generalledger  ");

        sb.append("WHERE pk_corp = '" + p.getPkCorp() + "' AND  frecordtypeflag = 4 and vbatch is null ");

        sb.append("group by crdcenterid, cinventoryid ");
        sb.append(") a where cc > 1) group by crdcenterid , cinventoryid ");
      }
      else {
        sb.append("select max(cgeneralledgerid) ID from ia_generalledger where crdcenterid || cinventoryid in( ");

        sb.append("select crdcenterid || cinventoryid from ( ");
        sb.append("select crdcenterid, cinventoryid , count(*) cc from ia_generalledger  ");

        sb.append("WHERE pk_corp = '" + p.getPkCorp() + "' AND  frecordtypeflag = 4 and vbatch is null ");

        sb.append("group by crdcenterid, cinventoryid ");
        sb.append(") a where cc > 1) group by crdcenterid || cinventoryid ");
      }

      String[][] sa2 = query(sb.toString());
      if ((sa2 == null) || (sa2.length == 0)) {
    	  flag = true;
        return flag;
      }
      String sSql = null;
      String[] sa = new String[6];
      sa[0] = p.getPkCorp();
      for (int i = 0; i < sa2.length; ++i)
      {
        sSql = "DELETE FROM ia_generalledger WHERE cgeneralledgerid = " + toS(sa2[i][0]);

        print(sSql);
        execDataNoTrans(sSql);
        sa[1] = sa2[i][0];
        sa[2] = "NULL";
        sa[3] = "NULL";
        sa[4] = sSql;
        sa[5] = "NULL";
        dmo.insert(sa);
      }
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
    finally {
    }
    return false;
  }

  private boolean compare(UFDouble d1, UFDouble d2, int precision)
  {
    if ((d1 == null) && (d2 == null)) {
      return false;
    }
    if ((d1 == null) && (d2 != null) && (d2.doubleValue() == 0.0D)) {
      return false;
    }
    if ((d2 == null) && (d1 != null) && (d1.doubleValue() == 0.0D)) {
      return false;
    }

    return ((d2 == null) || (d1 == null) || (d1.setScale(-precision, 4).doubleValue() != d2.setScale(-precision, 4).doubleValue()));
  }

  private boolean modifyIAGLBatchError(ParamVO308 p)
    throws Exception
  {
    try
    {
      CDMO dmo = new CDMO();
      boolean flag;
      CommonDataDMO cdmo = new CommonDataDMO();
      StringBuffer sb = new StringBuffer();

      if (cdmo.getDatabaseType() == 1) {
        sb.append("select max(cgeneralledgerid) ID from ia_generalledger where (crdcenterid,cinventoryid,vbatch) in( ");

        sb.append("select crdcenterid , cinventoryid,vbatch from ( ");
        sb.append("select crdcenterid, cinventoryid ,vbatch , count(*) cc from ia_generalledger  ");

        sb.append("WHERE pk_corp = '" + p.getPkCorp() + "' AND  frecordtypeflag = 4 and vbatch is not null ");

        sb.append("group by crdcenterid, cinventoryid,vbatch ");
        sb.append(") a where cc > 1) group by crdcenterid , cinventoryid,vbatch ");
      }
      else
      {
        sb.append("select max(cgeneralledgerid) ID from ia_generalledger where crdcenterid || cinventoryid || vbatch in( ");

        sb.append("select crdcenterid || cinventoryid || vbatch from ( ");
        sb.append("select crdcenterid, cinventoryid ,vbatch , count(*) cc from ia_generalledger  ");

        sb.append("WHERE pk_corp = '" + p.getPkCorp() + "' AND  frecordtypeflag = 4 and vbatch is not null ");

        sb.append("group by crdcenterid, cinventoryid ,vbatch ");
        sb.append(") a where cc > 1) group by crdcenterid || cinventoryid || vbatch");
      }

      String[][] sa2 = query(sb.toString());
      if ((sa2 == null) || (sa2.length == 0)) {
    	  flag = true;
          return flag;
      }
      String sSql = null;
      String[] sa = new String[6];
      sa[0] = p.getPkCorp();
      for (int i = 0; i < sa2.length; ++i)
      {
        sSql = "DELETE FROM ia_generalledger WHERE cgeneralledgerid = " + toS(sa2[i][0]);

        print(sSql);
        execDataNoTrans(sSql);
        sa[1] = sa2[i][0];
        sa[2] = "NULL";
        sa[3] = "NULL";
        sa[4] = sSql;
        sa[5] = "NULL";
        dmo.insert(sa);
      }
    }
    catch (RemoteException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Remote Call", e);
    }
    finally {
    }
    return false;
  }

  public void modifyNabPrecision(ParamVO308 p) throws Exception {
    CommonDataImpl cbo = new CommonDataImpl();
    Integer[] temps = cbo.getDataPrecision(p.getPkCorp());

    int mnyPrecision = temps[2].intValue();
    int numPrecision = temps[0].intValue();
    int pricePrecision = temps[1].intValue();

    StringBuffer buffer = new StringBuffer();
    buffer.append("update ia_generalledger set nabmny = round(nabmny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(nabmny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=nabmny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set ninmny = round(ninmny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(ninmny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=ninmny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set noutmny = round(noutmny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(noutmny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=noutmny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set ninvarymny = round(ninvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(ninvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=ninvarymny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set noutvarymny=round(noutvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(noutvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=noutvarymny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set nabvarymny=round(nabvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") where round(nabvarymny,");
    buffer.append(mnyPrecision);
    buffer.append(") !=nabvarymny and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set nabnum = round(nabnum,");
    buffer.append(numPrecision);
    buffer.append(") where round(nabnum,");
    buffer.append(numPrecision);
    buffer.append(") !=nabnum and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set ninnum = round(ninnum,");
    buffer.append(numPrecision);
    buffer.append(") where round(ninnum,");
    buffer.append(numPrecision);
    buffer.append(") !=ninnum and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append("update ia_generalledger set noutnum = round(noutnum,");
    buffer.append(numPrecision);
    buffer.append(") where round(noutnum,");
    buffer.append(numPrecision);
    buffer.append(") !=noutnum and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append(" update ia_generalledger set nabprice = round(nabmny/nabnum,");

    buffer.append(pricePrecision);
    buffer.append(") where round(nabmny/nabnum,");
    buffer.append(pricePrecision);
    buffer.append(") !=nabprice and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" '  and  frecordtypeflag  =  4");
    buffer.append(" and (nabnum is not null and nabnum != 0) ");
    execDataTrans(buffer.toString());

    buffer = new StringBuffer();
    buffer.append(" update ia_generalledger set nabprice = null");
    buffer.append(" where nabprice is not null ");
    buffer.append(" and pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append(" ' and  frecordtypeflag  =  4");
    buffer.append(" and ( nabnum is  null or nabnum = 0 )");
    execDataTrans(buffer.toString());
  }

  public void modifyPriceModeFlag(ParamVO308 p)
    throws Exception
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(" update ia_generalledger set ");
    buffer.append(" ia_generalledger.fpricemodeflag= b.pricemethod ");
    buffer.append(" from bd_produce b ");
    buffer.append(" where ia_generalledger.pk_corp='");
    buffer.append(p.getPkCorp());
    buffer.append("' and ia_generalledger.frecordtypeflag  =  4");
    buffer.append(" and ia_generalledger.crdcenterid = b.pk_calbody ");
    buffer.append(" and ia_generalledger.cinventoryid= b.pk_invmandoc ");
    buffer.append(" and ia_generalledger.fpricemodeflag != b.pricemethod ");
    execDataTrans(buffer.toString());
  }
}