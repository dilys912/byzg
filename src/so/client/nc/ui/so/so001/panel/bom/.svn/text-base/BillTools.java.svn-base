package nc.ui.so.so001.panel.bom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;
import nc.vo.so.so001.ComActionDescVO;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so800.SaleForeignVO;

public class BillTools
{
  public static final Properties itemkeys = new Properties();
  public static final int add = 0;
  public static final int sub = 1;
  public static final int mult = 2;
  public static final int div = 3;
  public static final UFDouble uf100 = new UFDouble(100);

  public static final UFDouble uf1 = new UFDouble(1);

  public static HashMap so40hs = new HashMap();

  public static final Properties itemkeys38 = new Properties();

  public static final Properties itemkeys33 = new Properties();

  public static final Properties itemkeys3U = new Properties();

  public static final Properties itemkeys3V = new Properties();

  public static FormulaParse f = new FormulaParse();

  public static final HashMap hbilltype = new HashMap();

  public static final Properties itemkeys32 = new Properties();

  public static final UFDouble uf0 = new UFDouble(0);

  public static UFBoolean getSO40(BillModel bm)
  {
    if (bm == null)
      return null;
    String skey = "" + bm;

    UFBoolean So40 = (UFBoolean)so40hs.get(skey);
    if (So40 == null)
    {
      so40hs.clear();
      try
      {
        String stemp = SysInitBO_Client.getParaString(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), "SO40");

        if (stemp != null)
          stemp = stemp.trim();
        if ("调整单价".endsWith(stemp))
          So40 = SoVoConst.buftrue;
        else
          So40 = SoVoConst.buffalse;
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
        So40 = SoVoConst.buffalse;
      }
      so40hs.put(skey, So40);
    }
    return So40;
  }

  public static UFDouble calc(UFDouble lvalue, UFDouble rvalue, int opera)
  {
    if ((lvalue == null) || (rvalue == null))
      return null;
    if ((opera == 3) && (rvalue.doubleValue() == 0.0D))
      return null;
    switch (opera) {
    case 0:
      return lvalue.add(rvalue);
    case 1:
      return lvalue.sub(rvalue);
    case 2:
      return lvalue.multiply(rvalue);
    case 3:
      return lvalue.div(rvalue);
    }
    return null;
  }

  public static UFDouble calc(int row, String lkey, String rkey, int opera, BillModel bm)
  {
    if (bm == null)
      return null;
    if ((lkey == null) || (rkey == null))
      return null;
    UFDouble olu = value(row, lkey, bm);
    UFDouble oru = value(row, rkey, bm);
    return calc(olu, oru, opera);
  }

  public static UFDouble calc(int row, String key, UFDouble value, int opera, BillModel bm)
  {
    if (bm == null)
      return null;
    if (value == null)
      return null;
    if ((opera == 3) && (value.doubleValue() == 0.0D))
      return null;
    UFDouble ou = value(row, key, bm);
    return calc(ou, value, opera);
  }

  public static UFDouble calc(int row, UFDouble value, String key, int opera, BillModel bm)
  {
    if (bm == null)
      return null;
    if (value == null)
      return null;
    UFDouble ou = value(row, key, bm);
    return calc(value, ou, opera);
  }

  public static InvVO cloneInvVo(InvVO vo) {
    InvVO retvo = new InvVO();
    if (vo == null)
      return retvo;
    String[] names = vo.getAttributeNames();
    if (names == null)
      return retvo;
    for (int i = 0; i < names.length; i++) {
      retvo.setAttributeValue(names[i], vo.getAttributeValue(names[i]));
    }
    if (vo.m_freevo != null)
    {
      FreeVO fvo = new FreeVO();
      FreeVO fvo0 = vo.m_freevo;

      fvo.setCinventoryid(fvo0.getCinventoryid());
      fvo.setPrimaryKey(fvo0.getPrimaryKey());
      fvo.setStatus(fvo0.getStatus());
      fvo.setVfree0(fvo0.getVfree0());
      fvo.setVfree1(fvo0.getVfree1());
      fvo.setVfree2(fvo0.getVfree2());
      fvo.setVfree3(fvo0.getVfree3());
      fvo.setVfree4(fvo0.getVfree4());
      fvo.setVfree5(fvo0.getVfree5());
      fvo.setVfree6(fvo0.getVfree6());
      fvo.setVfree7(fvo0.getVfree7());
      fvo.setVfree8(fvo0.getVfree8());
      fvo.setVfree9(fvo0.getVfree9());
      fvo.setVfree10(fvo0.getVfree10());
      fvo.setVfreeid1(fvo0.getVfreeid1());
      fvo.setVfreeid2(fvo0.getVfreeid2());
      fvo.setVfreeid3(fvo0.getVfreeid3());
      fvo.setVfreeid4(fvo0.getVfreeid4());
      fvo.setVfreeid5(fvo0.getVfreeid5());
      fvo.setVfreeid6(fvo0.getVfreeid6());
      fvo.setVfreeid7(fvo0.getVfreeid7());
      fvo.setVfreeid8(fvo0.getVfreeid8());
      fvo.setVfreeid9(fvo0.getVfreeid9());
      fvo.setVfreeid10(fvo0.getVfreeid10());
      fvo.setVfreename1(fvo0.getVfreename1());
      fvo.setVfreename2(fvo0.getVfreename2());
      fvo.setVfreename3(fvo0.getVfreename3());
      fvo.setVfreename4(fvo0.getVfreename4());
      fvo.setVfreename5(fvo0.getVfreename5());
      fvo.setVfreename6(fvo0.getVfreename6());
      fvo.setVfreename7(fvo0.getVfreename7());
      fvo.setVfreename8(fvo0.getVfreename8());
      fvo.setVfreename9(fvo0.getVfreename9());
      fvo.setVfreename10(fvo0.getVfreename10());
      fvo.setVfreevalue1(fvo0.getVfreevalue1());
      fvo.setVfreevalue2(fvo0.getVfreevalue2());
      fvo.setVfreevalue3(fvo0.getVfreevalue3());
      fvo.setVfreevalue4(fvo0.getVfreevalue4());
      fvo.setVfreevalue5(fvo0.getVfreevalue5());
      fvo.setVfreevalue6(fvo0.getVfreevalue6());
      fvo.setVfreevalue7(fvo0.getVfreevalue7());
      fvo.setVfreevalue8(fvo0.getVfreevalue8());
      fvo.setVfreevalue9(fvo0.getVfreevalue9());
      fvo.setVfreevalue10(fvo0.getVfreevalue10());

      retvo.m_freevo = fvo;
    }

    return retvo;
  }

  public static UFDouble discount(int row, BillModel bm)
  {
    if (bm == null)
      return null;
    UFDouble o = value(row, "ndiscountrate", bm);

    if (o == null)
      o = uf100;
    return calc(o, uf100, 3);
  }

  public static String getColValue2(String table, String field, String pkfield1, String pkvalue1, String pkfield2, String pkvalue2)
  {
    if ((table == null) || (field == null) || (pkfield1 == null) || (pkvalue1 == null) || (pkfield2 == null) || (pkvalue2 == null))
    {
      return null;
    }
    StringBuffer formula = new StringBuffer();

    formula.append("getColValue2(");
    formula.append(table);
    formula.append("," + field);
    formula.append("," + pkfield1);
    formula.append(",pkvalue1");
    formula.append("," + pkfield2);
    formula.append(",pkvalue2");
    formula.append(")");

    if (f == null) {
      f = new FormulaParse();
    }
    int itype = f.getCacheType();

    String[] result = null;
    try
    {
      f.setCacheType(0);

      f.setExpress(formula.toString());

      VarryVO varry = f.getVarry();

      Hashtable h = new Hashtable();
      String[][] value = new String[2][];
      value[0] = new String [] { "\"" + pkvalue1 + "\"" };
      value[1] =new String [] { "\"" + pkvalue2 + "\"" };

      if (varry.getVarry() != null) {
        for (int j = 0; j < varry.getVarry().length; j++) {
          String key = varry.getVarry()[j];
          h.put(key, value[j]);
        }

        f.setDataS(h);
        result = f.getValueS();
      }
    } finally {
      f.setCacheType(itype);
    }

    if ((result != null) && (result.length > 0))
      return result[0];
    return null;
  }

  public static UFDouble itemDiscount(int row, BillModel bm)
  {
    if (bm == null)
      return null;
    UFDouble o = value(row, "nitemdiscountrate", bm);

    if (o == null)
      o = uf100;
    return calc(o, uf100, 3);
  }

  public static String[] parseErrStr(String errStr) {
    if ((errStr == null) || (errStr.length() <= 0)) {
      return null;
    }
    if (errStr.indexOf(":") > 0)
      errStr = errStr.substring(errStr.indexOf(":") + 1);
    else {
      return null;
    }
    if (errStr == null) {
      return null;
    }
    if (errStr.length() <= 0) {
      return null;
    }
    String[] opera1 = { ">=", "<=", "!=" };
    String[] restrs = new String[3];

    for (int i = 0; i < opera1.length; i++) {
      int ipos = errStr.indexOf(opera1[i]);
      if (ipos > 0) {
        restrs[0] = errStr.substring(0, ipos);
        restrs[1] = opera1[i];
        errStr = errStr.substring(ipos);
        restrs[2] = errStr.substring(opera1[i].length());
        break;
      }
    }
    if (restrs[1] != null)
      return restrs;
    String[] opera2 = { ">", "<", "=" };
    for (int i = 0; i < opera1.length; i++) {
      int ipos = errStr.indexOf(opera2[i]);
      if (ipos > 0) {
        restrs[0] = errStr.substring(0, ipos);
        restrs[1] = opera2[i];
        errStr = errStr.substring(ipos);
        restrs[2] = errStr.substring(opera2[i].length());
        break;
      }
    }

    if (restrs[1] == null)
      return null;
    return restrs;
  }

  public static String[] parseErrStr2(String errstr)
  {
    if (errstr == null) {
      return null;
    }
    StringTokenizer strtoken = new StringTokenizer(errstr, "\n");

    ArrayList alist = new ArrayList();
    while (strtoken.hasMoreElements()) {
      alist.add(strtoken.nextElement());
    }
    if (alist.size() > 0)
      return (String[])(String[])alist.toArray(new String[0]);
    return null;
  }

  public static void setColValue2(int row, String setkey, String table, String field, String pkfield1, String pkvalue1, String pkfield2, String pkvalue2)
  {
  }

  public static UFDouble value(int row, String key, BillModel bm)
  {
    if (key == null)
      return null;
    Object o = null;
    try {
      o = bm.getValueAt(row, key);
    } catch (Exception e) {
      SCMEnv.out(key + " not find ");
      return null;
    }
    if (o == null)
      return null;
    if (o.getClass() == UFDouble.class)
      return (UFDouble)o;
    o = o.toString().trim();
    if (o.equals(""))
      return null;
    UFDouble d = new UFDouble((String)o);
    return d;
  }

  public static UFDouble value(int row, String key, UFDouble defaultvalue, BillModel bm)
  {
    UFDouble o = value(row, key, bm);
    if (o == null)
      return defaultvalue;
    return o;
  }

  public static void calcEditFun(String billdate, Integer BD505, boolean isTax, String isEditProject, int row, BillCardPanel panel)
  {
    if (panel == null) {
      return;
    }
    calcEditFun(isTax, null, isEditProject, 0, row, panel.getBillModel(), panel.getBillType());

    calcForeign(panel.getCorp(), panel.getBillType(), billdate, BD505, panel.getBillModel(), row, panel.getBillType());
  }

  public static void calcEditFunFor33(String billdate, Integer BD505, boolean isTax, String isEditProject, int row, BillListPanel panel)
  {
    if (panel == null) {
      return;
    }
    calcEditFun(isTax, null, isEditProject, 0, row, panel.getHeadBillModel(), panel.getBillType());

    calcForeign(panel.getCorp(), panel.getBillType(), billdate, BD505, panel.getHeadBillModel(), row, panel.getBillType());
  }

  public static void calcEditFun(String pk_corp, String billType, String billdate, Integer BD505, boolean isTax, int row, String isEditProject, BillModelVOS bmvos)
  {
    if ((bmvos == null) || (bmvos.getRowCount() <= 0)) {
      return;
    }
    calcEditFun(isTax, null, isEditProject, 0, row, bmvos, billType);

    calcForeign(pk_corp, billType, billdate, BD505, bmvos, row, billType);
  }

  public static void calcEditFun(String pk_corp, String billType, String billdate, Integer BD505, boolean isTax, String isEditProject, CircularlyAccessibleValueObject[] vos)
  {
    if ((vos == null) || (vos.length <= 0)) {
      return;
    }
    BillModelVOS bmvos = new BillModelVOS();
    bmvos.setVos(vos);

    calcEditFun(pk_corp, billType, billdate, BD505, isTax, isEditProject, bmvos);
  }

  public static void calcEditFun(String pk_corp, String billType, String billdate, Integer BD505, boolean isTax, String isEditProject, BillModelVOS bmvos)
  {
    if ((bmvos == null) || (bmvos.getRowCount() <= 0)) {
      return;
    }
    int i = 0; for (int loop = bmvos.getRowCount(); i < loop; i++)
    {
      calcEditFun(pk_corp, billType, billdate, BD505, isTax, i, isEditProject, bmvos);
    }
  }

  public static void calcEditFun(boolean isTax, UFBoolean isFindPrice, String isEditProject, int FunType, int row, BillModel bm, String billtype)
  {
    boolean btax = isTax;
    if ((key(billtype, "norgqtprc").equals(isEditProject)) || (key(billtype, "norgqtnetprc").equals(isEditProject)) || (key(billtype, "noriginalcurprice").equals(isEditProject)) || (key(billtype, "noriginalcurnetprice").equals(isEditProject)))
    {
      btax = false;
    } else if ((key(billtype, "norgqttaxprc").equals(isEditProject)) || (key(billtype, "norgqttaxnetprc").equals(isEditProject)) || (key(billtype, "noriginalcurtaxprice").equals(isEditProject)) || (key(billtype, "noriginalcurtaxnetprice").equals(isEditProject)))
    {
      btax = true;
    }
 
    if (!btax)
      calcNoTax(row, isEditProject, bm, billtype);
    else {
      calcTax(row, isEditProject, bm, billtype);
    }
  
    if ((discount(row, bm).doubleValue() == 1.0D) && (itemDiscount(row, bm).doubleValue() == 1.0D))
    {
      bm.setValueAt(new UFDouble(0.0D), row, key(billtype, "noriginalcurdiscountmny"));
    }
  }

  public static void calcForeign(String pk_corp, String strbillType, String ForeignDate, Integer BD505, BillModel bm, int row, String billtype)
  {
    if (bm == null) {
      return;
    }

    String ForeignID = (String)bm.getValueAt(row, key(billtype, "ccurrencytypeid"));

    UFDouble NRATE = value(row, key(billtype, "nexchangeotobrate"), bm);

    UFDouble ARATE = value(row, key(billtype, "nexchangeotoarate"), bm);

    SaleForeignVO ForeignVO = new SaleForeignVO();
    ForeignVO.setNdiscountmny(value(row, key(billtype, "noriginalcurdiscountmny"), bm));

    ForeignVO.setNmny(value(row, key(billtype, "noriginalcurmny"), bm));
    ForeignVO.setNnetprice(value(row, key(billtype, "noriginalcurnetprice"), bm));

    ForeignVO.setNprice(value(row, key(billtype, "noriginalcurprice"), bm));
    ForeignVO.setNsummny(value(row, key(billtype, "noriginalcursummny"), bm));

    ForeignVO.setNtaxmmny(value(row, key(billtype, "noriginalcurtaxmny"), bm));

    ForeignVO.setNtaxnetprice(value(row, key(billtype, "noriginalcurtaxnetprice"), bm));

    ForeignVO.setNtaxprice(value(row, key(billtype, "noriginalcurtaxprice"), bm));

    calcForeign(pk_corp, strbillType, ForeignVO, ForeignID, NRATE, ARATE, ForeignDate, BD505, bm, row, billtype);
  }

  public static UFDouble getAmountByOpp(BusinessCurrencyRateUtil bcurr, String ForeignID, UFDouble amout, UFDouble NRATE, UFDouble ARATE, String ForeignDate, boolean blnLocalFrac)
    throws BusinessException
  {
    UFDouble result = null;
    if (!blnLocalFrac) {
      result = bcurr.getAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), amout, NRATE, ForeignDate);
    }
    else {
      result = bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), amout, ARATE, ForeignDate);

      result = bcurr.getAmountByOpp(bcurr.getFracCurrPK(), bcurr.getLocalCurrPK(), result, NRATE, ForeignDate);
    }

    return result;
  }

  public static void calcForeign(String pk_corp, String strbillType, SaleForeignVO ForeignVO, String ForeignID, UFDouble NRATE, UFDouble ARATE, String ForeignDate, Integer BD505, BillModel bm, int row, String billtype)
  {
    if (bm == null) {
      return;
    }
    int digits = BD505.intValue();
    CurrtypeQuery currtypeQuery = CurrtypeQuery.getInstance();

    CurrtypeVO localcurrtypeVO = null;
    BusinessCurrencyRateUtil currtype = null;
    try {
      currtype = new BusinessCurrencyRateUtil(pk_corp);
      localcurrtypeVO = currtypeQuery.getCurrtypeVO(currtype.getLocalCurrPK());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    int localdigit = 4;
    localdigit = localcurrtypeVO.getCurrbusidigit() == null ? 4 : localcurrtypeVO.getCurrbusidigit().intValue();

    if (ForeignID == null) {
      return;
    }
    try
    {
      UFDouble norgqtprc = value(row, key(billtype, "norgqtprc"), bm);
      BusinessCurrencyRateUtil bcurr = new BusinessCurrencyRateUtil(pk_corp);

      boolean blnLocalFrac = false;
      if ((bcurr.isBlnLocalFrac()) && (!bcurr.isFracCurrType(ForeignID)) && (!bcurr.isLocalCurrType(ForeignID)))
      {
        blnLocalFrac = true;
      }

      if (norgqtprc == null) {
        bm.setValueAt(null, row, key(billtype, "nqtprc"));
      }
      else {
        UFDouble nqtprc = getAmountByOpp(bcurr, ForeignID, norgqtprc, NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nqtprc, row, key(billtype, "nqtprc"));
      }

      UFDouble norgqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);

      if (norgqttaxprc == null) {
        bm.setValueAt(null, row, key(billtype, "nqttaxprc"));
      } else {
        UFDouble nqttaxprc = getAmountByOpp(bcurr, ForeignID, norgqttaxprc, NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nqttaxprc, row, key(billtype, "nqttaxprc"));
      }

      UFDouble norgqttaxnetprc = value(row, key(billtype, "norgqttaxnetprc"), bm);

      if (norgqttaxnetprc == null) {
        bm.setValueAt(null, row, key(billtype, "nqttaxnetprc"));
      } else {
        UFDouble nqttaxnetprc = getAmountByOpp(bcurr, ForeignID, norgqttaxnetprc, NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nqttaxnetprc, row, key(billtype, "nqttaxnetprc"));
      }

      UFDouble norgqtnetprc = value(row, key(billtype, "norgqtnetprc"), bm);

      if (norgqtnetprc == null) {
        bm.setValueAt(null, row, key(billtype, "nqtnetprc"));
      } else {
        UFDouble nqtnetprc = getAmountByOpp(bcurr, ForeignID, norgqtnetprc, NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nqtnetprc, row, key(billtype, "nqtnetprc"));
      }

      if (ForeignVO.getNtaxprice() == null) {
        bm.setValueAt(null, row, key(billtype, "ntaxprice"));
      } else {
        UFDouble ntaxprice = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNtaxprice(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(ntaxprice, row, key(billtype, "ntaxprice"));
      }

      if (ForeignVO.getNprice() == null) {
        bm.setValueAt(null, row, key(billtype, "nprice"));
      } else {
        UFDouble nprice = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNprice(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nprice, row, key(billtype, "nprice"));
      }

      if (ForeignVO.getNtaxmmny() == null) {
        bm.setValueAt(null, row, key(billtype, "ntaxmny"));
      } else {
        UFDouble ntaxmny = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNtaxmmny(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(localdigit, 0);

        bm.setValueAt(ntaxmny, row, key(billtype, "ntaxmny"));
      }

      if (ForeignVO.getNsummny() == null) {
        bm.setValueAt(null, row, key(billtype, "nsummny"));
      } else {
        UFDouble nsummny = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNsummny(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(localdigit, 0);

        bm.setValueAt(nsummny, row, key(billtype, "nsummny"));
      }

      if (ForeignVO.getNmny() == null) {
        bm.setValueAt(null, row, key(billtype, "nmny"));
      } else {
        Object nsummny = bm.getValueAt(row, key(billtype, "nsummny"));
        if ((nsummny == null) || (nsummny.toString().equals(""))) {
          nsummny = new UFDouble(0);
        }
        Object ntaxmny = bm.getValueAt(row, key(billtype, "ntaxmny"));
        if ((ntaxmny == null) || (ntaxmny.toString().equals("")))
          ntaxmny = new UFDouble(0);
        bm.setValueAt(((UFDouble)nsummny).sub((UFDouble)ntaxmny), row, key(billtype, "nmny"));
      }

      if (ForeignVO.getNnetprice() == null) {
        bm.setValueAt(null, row, key(billtype, "nnetprice"));
      } else {
        UFDouble nnetprice = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNnetprice(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(nnetprice, row, key(billtype, "nnetprice"));
      }

      if (ForeignVO.getNtaxnetprice() == null) {
        bm.setValueAt(null, row, key(billtype, "ntaxnetprice"));
      } else {
        UFDouble ntaxnetprice = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNtaxnetprice(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(digits, 0);

        bm.setValueAt(ntaxnetprice, row, key(billtype, "ntaxnetprice"));
      }

      if (ForeignVO.getNdiscountmny() == null) {
        bm.setValueAt(null, row, key(billtype, "ndiscountmny"));
      }
      else {
        UFDouble ndiscountmny = getAmountByOpp(bcurr, ForeignID, ForeignVO.getNdiscountmny(), NRATE, ARATE, ForeignDate, blnLocalFrac).setScale(localdigit, 0);

        bm.setValueAt(ndiscountmny, row, key(billtype, "ndiscountmny"));
      }

      if ((bcurr.isBlnLocalFrac()) && (bcurr.isLocalCurrType(ForeignID)))
      {
        bm.setValueAt(null, row, key(billtype, "nassistcurtaxprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurmny"));

        bm.setValueAt(null, row, key(billtype, "nassistcursummny"));

        bm.setValueAt(null, row, key(billtype, "nassistcurtaxmny"));

        bm.setValueAt(null, row, key(billtype, "nassistcurnetprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurtaxnetprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurdiscountmny"));
      }
      else if (bcurr.isBlnLocalFrac())
      {
        if (ForeignVO.getNtaxprice() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurtaxprice"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNtaxprice(), ARATE, ForeignDate).setScale(digits, 0), row, key(billtype, "nassistcurtaxprice"));
        }

        if (ForeignVO.getNprice() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurprice"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNprice(), ARATE, ForeignDate).setScale(digits, 0), row, key(billtype, "nassistcurprice"));
        }

        if (ForeignVO.getNtaxmmny() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurtaxmny"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNtaxmmny(), ARATE, ForeignDate), row, key(billtype, "nassistcurtaxmny"));
        }

        if (ForeignVO.getNsummny() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcursummny"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNsummny(), ARATE, ForeignDate), row, key(billtype, "nassistcursummny"));
        }

        if (ForeignVO.getNmny() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurmny"));
        }
        else
        {
          Object nassistcursummny = bm.getValueAt(row, key(billtype, "nassistcursummny"));

          if ((nassistcursummny == null) || (nassistcursummny.toString().equals("")))
          {
            nassistcursummny = new UFDouble(0);
          }Object nassistcurtaxmny = bm.getValueAt(row, key(billtype, "nassistcurtaxmny"));

          if ((nassistcurtaxmny == null) || (nassistcurtaxmny.toString().equals("")))
          {
            nassistcurtaxmny = new UFDouble(0);
          }bm.setValueAt(((UFDouble)nassistcursummny).sub((UFDouble)nassistcurtaxmny), row, key(billtype, "nassistcurmny"));
        }

        if (ForeignVO.getNnetprice() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurnetprice"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNnetprice(), ARATE, ForeignDate).setScale(digits, 0), row, key(billtype, "nassistcurnetprice"));
        }

        if (ForeignVO.getNtaxnetprice() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurtaxnetprice"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNtaxnetprice(), ARATE, ForeignDate).setScale(digits, 0), row, key(billtype, "nassistcurtaxnetprice"));
        }

        if (ForeignVO.getNdiscountmny() == null) {
          bm.setValueAt(null, row, key(billtype, "nassistcurdiscountmny"));
        }
        else {
          bm.setValueAt(bcurr.getAmountByOpp(ForeignID, bcurr.getFracCurrPK(), ForeignVO.getNdiscountmny(), ARATE, ForeignDate), row, key(billtype, "nassistcurdiscountmny"));
        }

      }
      else
      {
        bm.setValueAt(null, row, key(billtype, "nassistcurtaxprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurmny"));

        bm.setValueAt(null, row, key(billtype, "nassistcursummny"));

        bm.setValueAt(null, row, key(billtype, "nassistcurtaxmny"));

        bm.setValueAt(null, row, key(billtype, "nassistcurnetprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurtaxnetprice"));

        bm.setValueAt(null, row, key(billtype, "nassistcurdiscountmny"));
      }

    }
    catch (Exception e)
    {
      bm.setValueAt(null, row, key(billtype, "nqtprc"));

      bm.setValueAt(null, row, key(billtype, "nqttaxprc"));

      bm.setValueAt(null, row, key(billtype, "nqttaxnetprc"));

      bm.setValueAt(null, row, key(billtype, "nqtnetprc"));

      bm.setValueAt(null, row, key(billtype, "ntaxprice"));

      bm.setValueAt(null, row, key(billtype, "nprice"));

      bm.setValueAt(null, row, key(billtype, "nmny"));

      bm.setValueAt(null, row, key(billtype, "nsummny"));

      bm.setValueAt(null, row, key(billtype, "ntaxmny"));

      bm.setValueAt(null, row, key(billtype, "nnetprice"));

      bm.setValueAt(null, row, key(billtype, "ntaxnetprice"));

      bm.setValueAt(null, row, key(billtype, "ndiscountmny"));

      bm.setValueAt(null, row, key(billtype, "nassistcurtaxprice"));

      bm.setValueAt(null, row, key(billtype, "nassistcurprice"));

      bm.setValueAt(null, row, key(billtype, "nassistcurmny"));

      bm.setValueAt(null, row, key(billtype, "nassistcursummny"));

      bm.setValueAt(null, row, key(billtype, "nassistcurtaxmny"));

      bm.setValueAt(null, row, key(billtype, "nassistcurnetprice"));

      bm.setValueAt(null, row, key(billtype, "nassistcurtaxnetprice"));

      bm.setValueAt(null, row, key(billtype, "nassistcurdiscountmny"));
    }
  }

  protected static void calcMPrice(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));
    
    String cquoteunitid = (String)bm.getValueAt(row, key(billtype, "cquoteunitid"));

    if ((cunitid != null) && (cunitid.equals(cquoteunitid))) {
      bm.setValueAt(value(row, key(billtype, "norgqtprc"), bm), row, key(billtype, "noriginalcurprice"));

      bm.setValueAt(value(row, key(billtype, "norgqttaxprc"), bm), row, key(billtype, "noriginalcurtaxprice"));

      bm.setValueAt(value(row, key(billtype, "norgqtnetprc"), bm), row, key(billtype, "noriginalcurnetprice"));

      bm.setValueAt(value(row, key(billtype, "norgqttaxnetprc"), bm), row, key(billtype, "noriginalcurtaxnetprice"));

      return;
    }

    UFDouble noriginalcurprice = calc(row, key(billtype, "norgqtprc"), key(billtype, "nqtscalefactor"), 3, bm);

    bm.setValueAt(noriginalcurprice, row, key(billtype, "noriginalcurprice"));

    UFDouble noriginalcurtaxprice = calc(row, key(billtype, "norgqttaxprc"), key(billtype, "nqtscalefactor"), 3, bm);

    bm.setValueAt(noriginalcurtaxprice, row, key(billtype, "noriginalcurtaxprice"));

    UFDouble noriginalcurnetprice = calc(row, key(billtype, "norgqtnetprc"), key(billtype, "nqtscalefactor"), 3, bm);

    bm.setValueAt(noriginalcurnetprice, row, key(billtype, "noriginalcurnetprice"));

    UFDouble noriginalcurtaxnetprice = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nqtscalefactor"), 3, bm);

    bm.setValueAt(noriginalcurtaxnetprice, row, key(billtype, "noriginalcurtaxnetprice"));
  }

  protected static void calcMPriceByMny(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));

    String cquoteunitid = (String)bm.getValueAt(row, key(billtype, "cquoteunitid"));

    if ((cunitid != null) && (cunitid.equals(cquoteunitid))) {
      bm.setValueAt(value(row, key(billtype, "norgqtprc"), bm), row, key(billtype, "noriginalcurprice"));

      bm.setValueAt(value(row, key(billtype, "norgqttaxprc"), bm), row, key(billtype, "noriginalcurtaxprice"));

      bm.setValueAt(value(row, key(billtype, "norgqtnetprc"), bm), row, key(billtype, "noriginalcurnetprice"));

      bm.setValueAt(value(row, key(billtype, "norgqttaxnetprc"), bm), row, key(billtype, "noriginalcurtaxnetprice"));

      return;
    }

    if (value(row, key(billtype, "norgqttaxprc"), bm) != null)
    {
      UFDouble noriginalcurtaxprice = calc(calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nnumber"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(noriginalcurtaxprice, row, key(billtype, "noriginalcurtaxprice"));
    }
    else {
      bm.setValueAt(null, row, key(billtype, "noriginalcurtaxprice"));
    }

    if (value(row, key(billtype, "norgqtprc"), bm) != null)
    {
      UFDouble noriginalcurprice = calc(row, key(billtype, "noriginalcurtaxprice"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(noriginalcurprice, row, key(billtype, "noriginalcurprice"));
    }
    else {
      bm.setValueAt(null, row, key(billtype, "noriginalcurprice"));
    }

    UFDouble noriginalcurtaxnetprice = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nnumber"), 3, bm);

    bm.setValueAt(noriginalcurtaxnetprice, row, key(billtype, "noriginalcurtaxnetprice"));

    UFDouble noriginalcurnetprice = calc(row, key(billtype, "noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

    bm.setValueAt(noriginalcurnetprice, row, key(billtype, "noriginalcurnetprice"));
  }

  protected static void calcNoTax(int row, String isEditProject, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    UFDouble scalefactor = value(row, key(billtype, "scalefactor"), bm);
    if (scalefactor == null) {
      bm.setValueAt(calc(value(row, key(billtype, "nnumber"), bm), value(row, key(billtype, "npacknumber"), bm), 3), row, key(billtype, "scalefactor"));
    }
    else if (scalefactor.doubleValue() < 0.0D) {
      bm.setValueAt(scalefactor.abs(), row, key(billtype, "scalefactor"));
    }

    UFDouble nqtscalefactor = value(row, key(billtype, "nqtscalefactor"), bm);

    if (nqtscalefactor == null) {
      bm.setValueAt(calc(value(row, key(billtype, "nnumber"), bm), value(row, key(billtype, "nquoteunitnum"), bm), 3), row, key(billtype, "nqtscalefactor"));
    }
    else if (nqtscalefactor.doubleValue() < 0.0D) {
      bm.setValueAt(nqtscalefactor.abs(), row, key(billtype, "nqtscalefactor"));
    }

    scalefactor = value(row, key(billtype, "scalefactor"), bm);
    nqtscalefactor = value(row, key(billtype, "nqtscalefactor"), bm);

    if ((isEditProject.equals(key(billtype, "npacknumber"))) || (isEditProject.equals(key(billtype, "cpackunitname"))) || (isEditProject.equals(key(billtype, "nnumber"))) || (isEditProject.equals(key(billtype, "cunitname"))) || (isEditProject.equals(key(billtype, "cquoteunit"))) || (isEditProject.equals(key(billtype, "nquoteunitnum"))))
    {
      calcUnitNum(row, bm, isEditProject, billtype);
      calcNoTaxEditFunByQt(row, key(billtype, "nquoteunitnum"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "cquoteunitname"))) {
      calcUnitNum(row, bm, isEditProject, billtype);
      calcNoTaxEditFunByQt(row, key(billtype, "noriginalcursummny"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurprice")))
    {
      calcQPrice(row, bm, billtype);
      calcNoTaxEditFunByQt(row, key(billtype, "norgqtprc"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurnetprice")))
    {
      calcQPrice(row, bm, billtype);
      calcNoTaxEditFunByQt(row, key(billtype, "norgqtnetprc"), bm, billtype);
    }
    else
    {
      calcNoTaxEditFunByQt(row, isEditProject, bm, billtype);
    }

    if (nqtscalefactor != null) {
      if (nqtscalefactor.doubleValue() > nqtscalefactor.intValue() + 0.0D)
        calcMPriceByMny(row, bm, billtype);
      else {
        calcMPrice(row, bm, billtype);
      }
    }

    if (scalefactor != null)
      if (scalefactor.doubleValue() > scalefactor.intValue() + 0.0D)
        calcViaPriceByMny(row, bm, billtype);
      else
        calcViaPrice(row, bm, billtype);
  }

  public static void calcNoTaxEditFunByQt(int row, String isEditProject, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key(billtype, "nquoteunitnum"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble noriginalcurmny = calc(row, key(billtype, "norgqtnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurmny")))
    {
      if (("32".equals(billtype)) && (getSO40(bm).booleanValue()))
      {
        UFDouble norgqtnetprc = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

        bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

        UFDouble norgqtprc = calc(calc(row, key(billtype, "norgqtnetprc"), itemDiscount(row, bm), 3, bm), discount(row, bm), 3);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));

        UFDouble norgqttaxprc = calc(row, key(billtype, "norgqtprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }
      else
      {
        UFDouble norgqtnetprc = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

        bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

        UFDouble nitemdiscountrate = calc(row, key(billtype, "norgqtnetprc"), calc(row, key(billtype, "norgqtprc"), discount(row, bm), 2, bm), 3, bm);

        if (value(row, key(billtype, "nitemdiscountrate"), bm) == null) {
          bm.setValueAt(uf100, row, key(billtype, "nitemdiscountrate"));
        }
        else {
          bm.setValueAt(calc(nitemdiscountrate, uf100, 2), row, key(billtype, "nitemdiscountrate"));
        }

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }

    }
    else if (isEditProject.equals(key(billtype, "noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble norgqttaxprc = calc(calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcursummny")))
    {
      if (("32".equals(billtype)) && (getSO40(bm).booleanValue()))
      {
        UFDouble norgqtprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));

        UFDouble norgqtnetprc = calc(row, key(billtype, "norgqtprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble noriginalcurmny = calc(row, key(billtype, "norgqtnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

        bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

        UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "noriginalcurmny"), 1, bm);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble norgqttaxprc = calc(row, key(billtype, "norgqtprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }
      else
      {
        UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

        bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

        UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble norgqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);

        if ((norgqttaxprc == null) || (norgqttaxprc.doubleValue() == 0.0D)) {
          norgqttaxprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

          bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
        }

        UFDouble norgqtprc = value(row, key(billtype, "norgqtprc"), bm);
        if ((norgqtprc == null) || (norgqtprc.doubleValue() == 0.0D)) {
          norgqtprc = calc(row, key(billtype, "norgqtnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

          bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
        }

        UFDouble nitemdiscountrate = calc(uf100, calc(row, key(billtype, "norgqttaxnetprc"), calc(row, key(billtype, "norgqttaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

        if (nitemdiscountrate == null) {
          bm.setValueAt(uf100, row, key(billtype, "nitemdiscountrate"));
        }
        else {
          bm.setValueAt(nitemdiscountrate, row, key(billtype, "nitemdiscountrate"));
        }

        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }

    }
    else if ((isEditProject.equals(key(billtype, "ndiscountrate"))) || (isEditProject.equals(key(billtype, "nitemdiscountrate"))))
    {
      UFDouble norgqtnetprc = calc(calc(row, key(billtype, "norgqtprc"), discount(row, bm), 2, bm), itemDiscount(row, bm), 2);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqttaxnetprc = calc(calc(row, key(billtype, "norgqttaxprc"), discount(row, bm), 2, bm), itemDiscount(row, bm), 2);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

      if (norgqttaxnetprc != null) {
        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));
      }

      UFDouble norgqttaxprc = calc(row, key(billtype, "norgqtprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

      if (norgqttaxprc != null) {
        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "norgqtnetprc")))
    {
      UFDouble norgqtprc = value(row, key(billtype, "norgqtprc"), bm);
      if ((norgqtprc == null) || (norgqtprc.doubleValue() == 0.0D)) {
        norgqtprc = calc(row, key(billtype, "norgqtnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key(billtype, "norgqtnetprc"), calc(row, key(billtype, "norgqtprc"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key(billtype, "nitemdiscountrate"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "norgqtnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble norgqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);

      if ((norgqttaxprc == null) || (norgqttaxprc.doubleValue() == 0.0D)) {
        norgqttaxprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "norgqtprc")))
    {
      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqtprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "norgqtnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble norgqttaxprc = calc(row, key(billtype, "norgqtprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

      bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurdiscountmny")))
    {
      UFDouble noriginalcursummny = calc(row, calc(row, key("nquoteunitnum"), key("norgqttaxprc"), 2, bm), key("noriginalcurdiscountmny"), 1, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      if ((norgqttaxnetprc != null) && (norgqttaxnetprc.doubleValue() < 0.0D))
        norgqttaxnetprc = norgqttaxnetprc.abs();
      bm.setValueAt(norgqttaxnetprc, row, key("norgqttaxnetprc"));

      UFDouble norgqtnetprc = calc(row, key("norgqttaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((norgqtnetprc != null) && (norgqtnetprc.doubleValue() < 0.0D))
        norgqtnetprc = norgqtnetprc.abs();
      bm.setValueAt(norgqtnetprc, row, key("norgqtnetprc"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgqttaxnetprc"), calc(row, key("norgqttaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));
    }
  }

  public static void calcQPrice(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));

    String cquoteunitid = (String)bm.getValueAt(row, key(billtype, "cquoteunitid"));

    if ((cunitid != null) && (cunitid.equals(cquoteunitid))) {
      bm.setValueAt(value(row, key(billtype, "noriginalcurprice"), bm), row, key(billtype, "norgqtprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxprice"), bm), row, key(billtype, "norgqttaxprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurnetprice"), bm), row, key(billtype, "norgqtnetprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxnetprice"), bm), row, key(billtype, "norgqttaxnetprc"));

      return;
    }

    UFDouble norgqtprc = calc(row, key(billtype, "noriginalcurprice"), key(billtype, "nqtscalefactor"), 2, bm);

    bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));

    UFDouble norgqttaxprc = calc(row, key(billtype, "noriginalcurtaxprice"), key(billtype, "nqtscalefactor"), 2, bm);

    bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

    UFDouble norgqtnetprc = calc(row, key(billtype, "noriginalcurnetprice"), key(billtype, "nqtscalefactor"), 2, bm);

    bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

    UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcurtaxnetprice"), key(billtype, "nqtscalefactor"), 2, bm);

    bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));
  }

  protected static void calcQPriceByMny(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));

    String cquoteunitid = (String)bm.getValueAt(row, key(billtype, "cquoteunitid"));

    if ((cunitid != null) && (cunitid.equals(cquoteunitid))) {
      bm.setValueAt(value(row, key(billtype, "noriginalcurprice"), bm), row, key(billtype, "norgqtprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxprice"), bm), row, key(billtype, "norgqttaxprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurnetprice"), bm), row, key(billtype, "norgqtnetprc"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxnetprice"), bm), row, key(billtype, "norgqttaxnetprc"));

      return;
    }

    if (value(row, key(billtype, "noriginalcurtaxprice"), bm) != null) {
      UFDouble norgqttaxprc = calc(calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
    } else {
      bm.setValueAt(null, row, key(billtype, "norgqttaxprc"));
    }

    if (value(row, key(billtype, "noriginalcurprice"), bm) != null) {
      UFDouble norgqtprc = calc(row, key(billtype, "norgqttaxprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
    } else {
      bm.setValueAt(null, row, key(billtype, "norgqtprc"));
    }

    UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

    bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

    UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

    bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));
  }

  protected static void calcTax(int row, String isEditProject, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    UFDouble scalefactor = value(row, key(billtype, "scalefactor"), bm);
    
    if (scalefactor == null) {
      bm.setValueAt(calc(value(row, key(billtype, "nnumber"), bm), value(row, key(billtype, "npacknumber"), bm), 3), row, key(billtype, "scalefactor"));
    }
    else if (scalefactor.doubleValue() < 0.0D) {
      bm.setValueAt(scalefactor.abs(), row, key(billtype, "scalefactor"));
    }

    UFDouble nqtscalefactor = value(row, key(billtype, "nqtscalefactor"), bm);
    bm.getValueAt(row, "noriginalcurdiscountmny");
    if (nqtscalefactor == null) {
      bm.setValueAt(calc(value(row, key(billtype, "nnumber"), bm), value(row, key(billtype, "nquoteunitnum"), bm), 3), row, key(billtype, "nqtscalefactor"));
    }
    else if (nqtscalefactor.doubleValue() < 0.0D) {
      bm.setValueAt(nqtscalefactor.abs(), row, key(billtype, "nqtscalefactor"));
    }

    scalefactor = value(row, key(billtype, "scalefactor"), bm);
    nqtscalefactor = value(row, key(billtype, "nqtscalefactor"), bm);
   
    if ((isEditProject.equals(key(billtype, "npacknumber"))) || (isEditProject.equals(key(billtype, "cpackunitname"))) || (isEditProject.equals(key(billtype, "nnumber"))) || (isEditProject.equals(key(billtype, "cunitname"))) || (isEditProject.equals(key(billtype, "cquoteunit"))) || (isEditProject.equals(key(billtype, "nquoteunitnum"))))
    {
      calcUnitNum(row, bm, isEditProject, billtype);

      calcTaxEditFunByQt(row, key(billtype, "nquoteunitnum"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "cquoteunitname"))) {
      calcUnitNum(row, bm, isEditProject, billtype);
      calcNoTaxEditFunByQt(row, key(billtype, "noriginalcursummny"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurtaxprice")))
    {
      calcQPrice(row, bm, billtype);

      calcTaxEditFunByQt(row, key(billtype, "norgqttaxprc"), bm, billtype);
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurtaxnetprice")))
    {
      calcQPrice(row, bm, billtype);

      calcTaxEditFunByQt(row, key(billtype, "norgqttaxnetprc"), bm, billtype);
    }
    else
    {
      calcTaxEditFunByQt(row, isEditProject, bm, billtype);
    }
    bm.getValueAt(row, "noriginalcurdiscountmny");
    if (nqtscalefactor != null) {
      if (nqtscalefactor.doubleValue() > nqtscalefactor.intValue() + 0.0D)
        calcMPriceByMny(row, bm, billtype);
      else {
        calcMPrice(row, bm, billtype);
      }
    }
    bm.getValueAt(row, "noriginalcurdiscountmny");
    if (scalefactor != null)
      if (scalefactor.doubleValue() > scalefactor.intValue() + 0.0D)
        calcViaPriceByMny(row, bm, billtype);
      else
        calcViaPrice(row, bm, billtype);
  }

  public static void calcTaxEditFunByQt(int row, String isEditProject, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key(billtype, "nquoteunitnum"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqtprc = calc(row, key(billtype, "norgqttaxprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurmny")))
    {
      if (("32".equals(billtype)) && (getSO40(bm).booleanValue()))
      {
        UFDouble norgqtnetprc = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

        bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

        UFDouble norgqtprc = calc(calc(row, key(billtype, "norgqtnetprc"), itemDiscount(row, bm), 3, bm), discount(row, bm), 3);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));

        UFDouble norgqttaxprc = calc(row, key(billtype, "norgqtprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));
        bm.getValueAt(row, "noriginalcurdiscountmny");
        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }
      else
      {
        UFDouble norgqtnetprc = calc(row, key(billtype, "noriginalcurmny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble noriginalcurtaxmny = calc(row, key(billtype, "noriginalcurmny"), taxRate(row, bm, billtype), 2, bm);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

        bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

        UFDouble nitemdiscountrate = calc(uf100, calc(row, key(billtype, "norgqtnetprc"), calc(row, key(billtype, "norgqtprc"), discount(row, bm), 2, bm), 3, bm), 2);

        if (nitemdiscountrate == null) {
          bm.setValueAt(uf100, row, key(billtype, "nitemdiscountrate"));
        }
        else {
          bm.setValueAt(nitemdiscountrate, row, key(billtype, "nitemdiscountrate"));
        }

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqtnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));
        bm.getValueAt(row, "noriginalcurdiscountmny");
        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }

    }
    else if (isEditProject.equals(key(billtype, "noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key(billtype, "noriginalcurtaxmny"), key(billtype, "noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble norgqttaxprc = calc(calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcursummny"))) {
      if (("32".equals(billtype)) && (getSO40(bm).booleanValue()))
      {
        UFDouble norgqttaxprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));

        UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqttaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

        bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

        UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble norgqtprc = calc(row, key(billtype, "norgqttaxprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
        bm.getValueAt(row, "noriginalcurdiscountmny");
        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }
      else
      {
        UFDouble norgqttaxnetprc = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "nquoteunitnum"), 3, bm);

        bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

        UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

        bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

        UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

        bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

        UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

        UFDouble norgqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);

        if ((norgqttaxprc == null) || (norgqttaxprc.doubleValue() == 0.0D)) {
          norgqttaxprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

          bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
        }

        UFDouble norgqtprc = value(row, key(billtype, "norgqtprc"), bm);
        if ((norgqtprc == null) || (norgqtprc.doubleValue() == 0.0D)) {
          norgqtprc = calc(row, key(billtype, "norgqtnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

          bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
        }

        UFDouble nitemdiscountrate = calc(uf100, calc(row, key(billtype, "norgqttaxnetprc"), calc(row, key(billtype, "norgqttaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

        if (nitemdiscountrate == null) {
          bm.setValueAt(uf100, row, key(billtype, "nitemdiscountrate"));
        }
        else {
          bm.setValueAt(nitemdiscountrate, row, key(billtype, "nitemdiscountrate"));
        }
        bm.getValueAt(row, "noriginalcurdiscountmny");
        UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

        bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
      }

    }
    else if ((isEditProject.equals(key(billtype, "ndiscountrate"))) || (isEditProject.equals(key(billtype, "nitemdiscountrate"))))
    {
      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqtprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqttaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    
    //add by zwx 2014-11-14  修改途损比例时，自动修改单品折扣对应的单价
    else if (isEditProject.equals("tsbl"))
    {
      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqtprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqttaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble noriginalcursummny = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    
    //end by zwx
    else if (isEditProject.equals(key(billtype, "ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      if (norgqtnetprc != null) {
        bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));
      }

      UFDouble norgqtprc = calc(row, key(billtype, "norgqttaxprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      if (norgqtprc != null) {
        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
      }
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "norgqttaxnetprc")))
    {
      UFDouble norgqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);

      if ((norgqttaxprc == null) || (norgqttaxprc.doubleValue() == 0.0D)) {
        norgqttaxprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(norgqttaxprc, row, key(billtype, "norgqttaxprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key(billtype, "norgqttaxnetprc"), calc(row, key(billtype, "norgqttaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key(billtype, "nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key(billtype, "nitemdiscountrate"));
      }

      UFDouble noriginalcursummny = calc(row, key(billtype, "norgqttaxnetprc"), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqtprc = value(row, key(billtype, "norgqtprc"), bm);
      if ((norgqtprc == null) || (norgqtprc.doubleValue() == 0.0D)) {
        norgqtprc = calc(row, key(billtype, "norgqtnetprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
      }
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "norgqttaxprc")))
    {
      UFDouble norgqttaxnetprc = calc(row, key(billtype, "norgqttaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgqttaxnetprc, row, key(billtype, "norgqttaxnetprc"));

      UFDouble noriginalcursummny = calc(row, calc(row, key(billtype, "norgqttaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm), key(billtype, "nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key(billtype, "noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key(billtype, "noriginalcursummny"), taxRate(row, bm, billtype), 2, bm), calc(uf1, taxRate(row, bm, billtype), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key(billtype, "noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key(billtype, "noriginalcursummny"), key(billtype, "noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key(billtype, "noriginalcurmny"));

      UFDouble norgqtnetprc = calc(row, key(billtype, "norgqttaxnetprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtnetprc, row, key(billtype, "norgqtnetprc"));

      UFDouble norgqtprc = calc(row, key(billtype, "norgqttaxprc"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

      bm.setValueAt(norgqtprc, row, key(billtype, "norgqtprc"));
      bm.getValueAt(row, "noriginalcurdiscountmny");
      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nquoteunitnum"), key(billtype, "norgqttaxprc"), 2, bm), key(billtype, "noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key(billtype, "noriginalcurdiscountmny"));
    }
    else if (isEditProject.equals(key(billtype, "noriginalcurdiscountmny")))
    {
      UFDouble noriginalcursummny = calc(row, calc(row, key("nquoteunitnum"), key("norgqttaxprc"), 2, bm), key("noriginalcurdiscountmny"), 1, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgqttaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      if ((norgqttaxnetprc != null) && (norgqttaxnetprc.doubleValue() < 0.0D))
        norgqttaxnetprc = norgqttaxnetprc.abs();
      bm.setValueAt(norgqttaxnetprc, row, key("norgqttaxnetprc"));

      UFDouble norgqtnetprc = calc(row, key("norgqttaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((norgqtnetprc != null) && (norgqtnetprc.doubleValue() < 0.0D))
        norgqtnetprc = norgqtnetprc.abs();
      bm.setValueAt(norgqtnetprc, row, key("norgqtnetprc"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgqttaxnetprc"), calc(row, key("norgqttaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));
    }
  }

  public static void calcUnitNum(int row, BillModel bm, String editkey, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));
    String cunitname = (String)bm.getValueAt(row, key(billtype, "cunitname"));

    String cpackunitid = (String)bm.getValueAt(row, key(billtype, "cpackunitid"));

    String cquoteunitid = (String)bm.getValueAt(row, key(billtype, "cquoteunitid"));

    Object otemp = bm.getValueAt(row, key(billtype, "assistunit"));

    UFBoolean assistunit = new UFBoolean(otemp == null ? "N" : otemp.toString());

    if (!SoVoTools.isEmptyString(cpackunitid)) {
      assistunit = SoVoConst.buftrue;
    }
    if ((!assistunit.booleanValue()) || (cquoteunitid == null) || (cquoteunitid.trim().length() <= 0) || (cunitid == null) || (cunitid.trim().length() <= 0))
    {
      bm.setValueAt(null, row, key(billtype, "scalefactor"));
      bm.setValueAt(null, row, key(billtype, "npacknumber"));
    }
    else
    {
      if ((cunitid != null) && (cunitid.equals(cquoteunitid)))
      {
        bm.setValueAt(new UFDouble(1.0D), row, key(billtype, "nqtscalefactor"));

        bm.setValueAt(new UFBoolean("Y"), row, key(billtype, "bqtfixedflag"));

        bm.setValueAt(cunitname, row, key(billtype, "cquoteunit"));
      }

      if ((cunitid != null) && (cunitid.equals(cpackunitid)))
      {
        bm.setValueAt(new UFDouble(1.0D), row, key(billtype, "scalefactor"));

        bm.setValueAt(new UFBoolean("Y"), row, key(billtype, "fixedflag"));

        bm.setValueAt(cunitname, row, key(billtype, "cpackunitname"));
      }
      else if ((cpackunitid == null) || (cpackunitid.trim().length() <= 0))
      {
        bm.setValueAt(null, row, key(billtype, "scalefactor"));
        bm.setValueAt(null, row, key(billtype, "fixedflag"));
      }

    }

    otemp = bm.getValueAt(row, key(billtype, "bqtfixedflag"));

    UFBoolean nqtfixedflag = new UFBoolean(otemp == null ? "N" : otemp.toString());

    otemp = bm.getValueAt(row, key(billtype, "fixedflag"));

    UFBoolean fixedflag = new UFBoolean(otemp == null ? "N" : otemp.toString());

    UFDouble scalefactor = value(row, key(billtype, "scalefactor"), bm);

    if ((scalefactor != null) && (scalefactor.doubleValue() < 0.0D)) {
      bm.setValueAt(scalefactor.abs(), row, key(billtype, "scalefactor"));
    }
    UFDouble nqtscalefactor = value(row, key(billtype, "nqtscalefactor"), bm);

    if ((nqtscalefactor != null) && (nqtscalefactor.doubleValue() < 0.0D)) {
      bm.setValueAt(nqtscalefactor.abs(), row, key(billtype, "nqtscalefactor"));
    }

    if (!assistunit.booleanValue())
    {
      if ((key(billtype, "nnumber").equals(editkey)) || (key(billtype, "cunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "nquoteunitnum"));
      }
      else if ((key(billtype, "nquoteunitnum").equals(editkey)) || (key(billtype, "cquoteunit").equals(editkey)) || (key(billtype, "cquoteunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "nnumber"));
      }
      else if (value(row, key(billtype, "nnumber"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "nquoteunitnum"));
      }
      else if (value(row, key(billtype, "nquoteunitnum"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "nnumber"));
      }

      return;
    }

    if ((cunitid != null) && (cunitid.equals(cpackunitid)) && (cunitid.equals(cquoteunitid)))
    {
      if ((key(billtype, "nnumber").equals(editkey)) || (key(billtype, "cunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "npacknumber"));

        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "nquoteunitnum"));
      }
      else if ((key(billtype, "npacknumber").equals(editkey)) || (key(billtype, "cpackunitname").equals(editkey)))
      {
        UFDouble uf_num = value(row, key(billtype, "npacknumber"), bm);
        if (uf_num != null) {
          bm.setValueAt(uf_num, row, key(billtype, "nnumber"));
          bm.setValueAt(uf_num, row, key(billtype, "nquoteunitnum"));
        } else {
          uf_num = value(row, key(billtype, "nnumber"), bm);
          bm.setValueAt(uf_num, row, key(billtype, "npacknumber"));
          bm.setValueAt(uf_num, row, key(billtype, "nquoteunitnum"));
        }
      } else if ((key(billtype, "nquoteunitnum").equals(editkey)) || (key(billtype, "cquoteunit").equals(editkey)) || (key(billtype, "cquoteunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "nnumber"));

        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "npacknumber"));
      }
      else if (value(row, key(billtype, "nnumber"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "npacknumber"));

        bm.setValueAt(value(row, key(billtype, "nnumber"), bm), row, key(billtype, "nquoteunitnum"));
      }
      else if (value(row, key(billtype, "npacknumber"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "npacknumber"), bm), row, key(billtype, "nnumber"));

        bm.setValueAt(value(row, key(billtype, "npacknumber"), bm), row, key(billtype, "nquoteunitnum"));
      }
      else if (value(row, key(billtype, "nquoteunitnum"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "nnumber"));

        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "npacknumber"));
      }

      return;
    }

    if (editkey != null)
    {
      if (key(billtype, "nnumber").equals(editkey)) {
        if (fixedflag.booleanValue()) {
          bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
        }
        else
        {
          if ((value(row, key(billtype, "npacknumber"), bm) == null) || (value(row, key(billtype, "npacknumber"), bm).doubleValue() == 0.0D))
          {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
          }

          if ((value(row, key(billtype, "npacknumber"), bm) != null) && (value(row, key(billtype, "npacknumber"), bm).compareTo(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm).setScale(value(row, key(billtype, "npacknumber"), bm).getPower(), 0)) != 0))
          {
            bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "npacknumber"), new UFDouble(1), bm), 3), row, key(billtype, "scalefactor"));
          }

        }

        if (nqtfixedflag.booleanValue()) {
          bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
        }
        else
        {
          if ((value(row, key(billtype, "nquoteunitnum"), bm) == null) || (value(row, key(billtype, "nquoteunitnum"), bm).doubleValue() == 0.0D))
          {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
          }

          if (value(row, key(billtype, "nqtscalefactor"), bm) == null) {
            bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "nquoteunitnum"), new UFDouble(1), bm), 3), row, key(billtype, "nqtscalefactor"));
          }
        }

      }
      else if (key(billtype, "scalefactor").equals(editkey)) {
        bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));

        if (nqtfixedflag.booleanValue()) {
          bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
        }
        else
        {
          if ((value(row, key(billtype, "nquoteunitnum"), bm) == null) || (value(row, key(billtype, "nquoteunitnum"), bm).doubleValue() == 0.0D))
          {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
          }

          bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "nquoteunitnum"), new UFDouble(1), bm), 3), row, key(billtype, "nqtscalefactor"));
        }

      }
      else if (!key(billtype, "cunitname").equals(editkey))
      {
        if (key(billtype, "cpackunitname").equals(editkey))
        {
          if ((value(row, key(billtype, "npacknumber"), bm) == null) || (value(row, key(billtype, "npacknumber"), bm).doubleValue() == 0.0D))
          {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
          }
          else
          {
            bm.setValueAt(calc(row, key(billtype, "npacknumber"), key(billtype, "scalefactor"), 2, bm), row, key(billtype, "nnumber"));

            if (nqtfixedflag.booleanValue()) {
              bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
            }
            else
            {
              if ((value(row, key(billtype, "nquoteunitnum"), bm) == null) || (value(row, key(billtype, "nquoteunitnum"), bm).doubleValue() == 0.0D))
              {
                bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
              }

              bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "nquoteunitnum"), new UFDouble(1), bm), 3), row, key(billtype, "nqtscalefactor"));
            }

            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
          }

        }
        else if ((key(billtype, "cquoteunit").equals(editkey)) || (key(billtype, "cquoteunitname").equals(editkey)))
        {
          if ((value(row, key(billtype, "nquoteunitnum"), bm) == null) || (value(row, key(billtype, "nquoteunitnum"), bm).doubleValue() == 0.0D))
          {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
          }
          else
          {
            bm.setValueAt(calc(row, key(billtype, "nquoteunitnum"), key(billtype, "nqtscalefactor"), 2, bm), row, key(billtype, "nnumber"));

            if (fixedflag.booleanValue()) {
              bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
            }
            else
            {
              if ((value(row, key(billtype, "npacknumber"), bm) == null) || (value(row, key(billtype, "npacknumber"), bm).doubleValue() == 0.0D))
              {
                bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
              }

              bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "npacknumber"), new UFDouble(1), bm), 3), row, key(billtype, "scalefactor"));
            }

          }

        }
        else if (key(billtype, "npacknumber").equals(editkey))
        {
          bm.setValueAt(calc(row, key(billtype, "npacknumber"), key(billtype, "scalefactor"), 2, bm), row, key(billtype, "nnumber"));

          if (nqtfixedflag.booleanValue()) {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
          }
          else
          {
            if ((value(row, key(billtype, "nquoteunitnum"), bm) == null) || (value(row, key(billtype, "nquoteunitnum"), bm).doubleValue() == 0.0D))
            {
              bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "nqtscalefactor"), 3, bm), row, key(billtype, "nquoteunitnum"));
            }

            bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "nquoteunitnum"), new UFDouble(1), bm), 3), row, key(billtype, "nqtscalefactor"));
          }

        }
        else if (key(billtype, "nquoteunitnum").equals(editkey))
        {
          bm.setValueAt(calc(row, key(billtype, "nquoteunitnum"), key(billtype, "nqtscalefactor"), 2, bm), row, key(billtype, "nnumber"));

          if (fixedflag.booleanValue()) {
            bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
          }
          else
          {
            if ((value(row, key(billtype, "npacknumber"), bm) == null) || (value(row, key(billtype, "npacknumber"), bm).doubleValue() == 0.0D))
            {
              bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
            }

            bm.setValueAt(calc(value(row, key(billtype, "nnumber"), new UFDouble(1), bm), value(row, key(billtype, "npacknumber"), new UFDouble(1), bm), 3), row, key(billtype, "scalefactor"));
          }

        }
        else if (key(billtype, "norgqttaxprc").equals(editkey)) {
          bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "nnumber"));

          bm.setValueAt(calc(row, key(billtype, "nnumber"), key(billtype, "scalefactor"), 3, bm), row, key(billtype, "npacknumber"));
        }

      }

    }

    if ((cpackunitid != null) && (cpackunitid.trim().length() > 0) && (cpackunitid.equals(cquoteunitid)))
    {
      if ((key(billtype, "nnumber").equals(editkey)) || (key(billtype, "cunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "npacknumber"), bm), row, key(billtype, "nquoteunitnum"));

        bm.setValueAt(value(row, key(billtype, "scalefactor"), bm), row, key(billtype, "nqtscalefactor"));
      }
      else if ((key(billtype, "npacknumber").equals(editkey)) || (key(billtype, "cpackunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "npacknumber"), bm), row, key(billtype, "nquoteunitnum"));

        bm.setValueAt(value(row, key(billtype, "scalefactor"), bm), row, key(billtype, "nqtscalefactor"));
      }
      else if ((key(billtype, "nquoteunitnum").equals(editkey)) || (key(billtype, "cquoteunit").equals(editkey)) || (key(billtype, "cquoteunitname").equals(editkey)))
      {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "npacknumber"));

        bm.setValueAt(value(row, key(billtype, "nqtscalefactor"), bm), row, key(billtype, "scalefactor"));
      }
      else if (value(row, key(billtype, "npacknumber"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "npacknumber"), bm), row, key(billtype, "nquoteunitnum"));

        bm.setValueAt(value(row, key(billtype, "scalefactor"), bm), row, key(billtype, "nqtscalefactor"));
      }
      else if (value(row, key(billtype, "nquoteunitnum"), bm) != null) {
        bm.setValueAt(value(row, key(billtype, "nquoteunitnum"), bm), row, key(billtype, "npacknumber"));

        bm.setValueAt(value(row, key(billtype, "nqtscalefactor"), bm), row, key(billtype, "scalefactor"));
      }
    }
  }

  public static void calcViaPrice(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));

    String cpackunitid = (String)bm.getValueAt(row, key(billtype, "cpackunitid"));

    if (SoVoTools.isEmptyString(cpackunitid)) {
      bm.setValueAt(null, row, key(billtype, "norgviapricetax"));
      bm.setValueAt(null, row, key(billtype, "norgviaprice"));
      return;
    }

    if ((cunitid != null) && (cunitid.equals(cpackunitid))) {
      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxprice"), bm), row, key(billtype, "norgviapricetax"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurprice"), bm), row, key(billtype, "norgviaprice"));

      return;
    }

    UFDouble norgviapricetax = calc(row, key(billtype, "noriginalcurtaxprice"), key(billtype, "scalefactor"), 2, bm);

    bm.setValueAt(norgviapricetax, row, key(billtype, "norgviapricetax"));

    UFDouble norgviaprice = calc(row, key(billtype, "noriginalcurprice"), key(billtype, "scalefactor"), 2, bm);

    bm.setValueAt(norgviaprice, row, key(billtype, "norgviaprice"));
  }

  public static void calcViaPrice1(int row, BillModel bm, String billtype)
  {
    calcViaPrice(row, bm, billtype);
  }

  public static void calcViaPriceAll(BillModel bm, String billtype)
  {
    if (bm == null) {
      return;
    }
    int i = 0; for (int loop = bm.getRowCount(); i < loop; i++)
      calcViaPrice1(i, bm, billtype);
  }

  public static void calcViaPriceByMny(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }
    String cunitid = (String)bm.getValueAt(row, key(billtype, "cunitid"));

    String cpackunitid = (String)bm.getValueAt(row, key(billtype, "cpackunitid"));

    if (SoVoTools.isEmptyString(cpackunitid)) {
      bm.setValueAt(null, row, key(billtype, "norgviapricetax"));
      bm.setValueAt(null, row, key(billtype, "norgviaprice"));
      return;
    }

    if ((cunitid != null) && (cunitid.equals(cpackunitid))) {
      bm.setValueAt(value(row, key(billtype, "noriginalcurtaxprice"), bm), row, key(billtype, "norgviapricetax"));

      bm.setValueAt(value(row, key(billtype, "noriginalcurprice"), bm), row, key(billtype, "norgviaprice"));

      return;
    }

    UFDouble norgviapricetax = calc(calc(row, key(billtype, "noriginalcursummny"), key(billtype, "npacknumber"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

    bm.setValueAt(norgviapricetax, row, key(billtype, "norgviapricetax"));

    UFDouble norgviaprice = calc(row, key(billtype, "norgviapricetax"), calc(uf1, taxRate(row, bm, billtype), 0), 3, bm);

    bm.setValueAt(norgviaprice, row, key(billtype, "norgviaprice"));
  }

  public static UFDouble getBodyValueAtByDigit(BillModel bm, int row, String key, int digit)
  {
    if ((bm == null) || (row < 0) || (key == null) || (row >= bm.getRowCount())) {
      return null;
    }
    Object otemp = bm.getValueAt(row, key);
    if (otemp == null)
      return null;
    if (otemp.getClass() == UFDouble.class) {
      return ((UFDouble)otemp).setScale(0 - digit, 4);
    }

    if (otemp.toString().trim().length() <= 0)
      return null;
    return new UFDouble(otemp.toString(), digit);
  }

  public static String[] getColValues2(String table, String field, String pkfield1, String[] pkvalues1, String pkfield2, String[] pkvalues2)
  {
    if ((table == null) || (field == null) || (pkfield1 == null) || (pkvalues1 == null) || (pkfield2 == null) || (pkvalues2 == null))
    {
      return null;
    }
    StringBuffer formula = new StringBuffer();

    formula.append("getColValue2(");
    formula.append(table);
    formula.append("," + field);
    formula.append("," + pkfield1);
    formula.append(",pkvalue1");
    formula.append("," + pkfield2);
    formula.append(",pkvalue2");
    formula.append(")");

    if (f == null) {
      f = new FormulaParse();
    }
    int itype = f.getCacheType();

    String[] result = null;
    try
    {
      f.setCacheType(0);

      f.setExpress(formula.toString());

      VarryVO varry = f.getVarry();

      Hashtable h = new Hashtable();
      String[][] value = new String[2][];
      int i = 0; for (int loop = pkvalues1.length; i < loop; i++) {
        pkvalues1[i] = ("\"" + pkvalues1[i] + "\"");
        pkvalues2[i] = ("\"" + pkvalues2[i] + "\"");
      }
      value[0] = pkvalues1;
      value[1] = pkvalues2;

      if (varry.getVarry() != null) {
        for (int j = 0; j < varry.getVarry().length; j++) {
          String key = varry.getVarry()[j];
          h.put(key, value[j]);
        }

        f.setDataS(h);
        result = f.getValueS();
      }
    } finally {
      f.setCacheType(itype);
    }

    return result;
  }

  public static void getMnyByCurrencyFromModel(BillModel bm, CircularlyAccessibleValueObject[] vos, String pk_corp, String curridkey, String[] mnykey)
    throws BusinessException, Exception
  {
    if ((bm == null) || (vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (curridkey == null))
    {
      return;
    }String sCurrid = null;

    Object otemp = null;

    int i = 0; for (int loop = vos.length; i < loop; i++) {
      sCurrid = (String)vos[i].getAttributeValue(curridkey);
      if ((sCurrid == null) || (sCurrid.trim().length() <= 0)) {
        int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
          otemp = bm.getValueAt(i, mnykey[k]);
          if ((otemp == null) || (otemp.toString().trim().length() <= 0))
            continue;
          vos[i].setAttributeValue(mnykey[k], otemp);
        }
      }
      else {
        getMnyByCurrencyFromModel(bm, vos[i], i, pk_corp, sCurrid, mnykey);
      }
    }
  }

  public static void getMnyByCurrencyFromModel(BillModel bm, CircularlyAccessibleValueObject vo, int row, String pk_corp, String currid, String[] mnykey)
    throws BusinessException, Exception
  {
    if ((bm == null) || (vo == null) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (currid == null) || (row <= 0) || (row >= bm.getRowCount()))
    {
      return;
    }
    int iDigit = 4;

    BusinessCurrencyRateUtil ccurr = new BusinessCurrencyRateUtil(pk_corp);
    CurrinfoVO currVO = ccurr.getCurrinfoVO(currid, null);
    if ((currVO == null) || (currVO.getCurrdigit() == null)) {
      return;
    }
    iDigit = currVO.getCurrdigit().intValue();
    int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
      if (mnykey[k] == null) {
        continue;
      }
      vo.setAttributeValue(mnykey[k], getBodyValueAtByDigit(bm, row, mnykey[k], iDigit));
    }
  }

  public static void initItemKeys()
  {
    String[] EditKey = new String[55];
    EditKey[0] = "ccurrencytypeid";

    EditKey[1] = "norgqtprc";
    EditKey[2] = "norgqttaxprc";
    EditKey[3] = "norgqtnetprc";
    EditKey[4] = "norgqttaxnetprc";

    EditKey[5] = "nqtprc";
    EditKey[6] = "nqttaxprc";
    EditKey[7] = "nqtnetprc";
    EditKey[8] = "nqttaxnetprc";

    EditKey[9] = "nquoteunitnum";

    EditKey[10] = "cquoteunit";
    EditKey[11] = "cquoteunitid";

    EditKey[12] = "bqtfixedflag";
    EditKey[13] = "nqtscalefactor";

    EditKey[14] = "noriginalcurprice";
    EditKey[15] = "noriginalcurtaxprice";
    EditKey[16] = "noriginalcurnetprice";
    EditKey[17] = "noriginalcurtaxnetprice";

    EditKey[18] = "nprice";
    EditKey[19] = "ntaxprice";
    EditKey[20] = "nnetprice";
    EditKey[21] = "ntaxnetprice";
    EditKey[22] = "assistunit";

    EditKey[23] = "nnumber";
    EditKey[24] = "npacknumber";
    EditKey[25] = "cunitname";
    EditKey[26] = "cunitid";
    EditKey[27] = "cpackunitid";
    EditKey[28] = "cpackunitname";
    EditKey[29] = "fixedflag";
    EditKey[30] = "scalefactor";
    EditKey[31] = "ndiscountrate";
    EditKey[32] = "nitemdiscountrate";

    EditKey[33] = "nassistcurprice";
    EditKey[34] = "nassistcurtaxprice";
    EditKey[35] = "nassistcurnetprice";
    EditKey[36] = "nassistcurtaxnetprice";

    EditKey[37] = "nexchangeotobrate";
    EditKey[38] = "nexchangeotoarate";

    EditKey[39] = "noriginalcurmny";
    EditKey[40] = "noriginalcurtaxmny";
    EditKey[41] = "noriginalcurdiscountmny";
    EditKey[42] = "noriginalcursummny";

    EditKey[43] = "nmny";
    EditKey[44] = "ntaxmny";
    EditKey[45] = "ndiscountmny";
    EditKey[46] = "nsummny";

    EditKey[47] = "nassistcurmny";
    EditKey[48] = "nassistcurtaxmny";
    EditKey[49] = "nassistcurdiscountmny";
    EditKey[50] = "nassistcursummny";

    EditKey[51] = "norgviapricetax";
    EditKey[52] = "norgviaprice";

    EditKey[53] = "ntaxrate";
    EditKey[54] = "cquoteunitname";

    String[] EditValue32 = new String[14];

    EditValue32[0] = "ccurrencytypeid";

    EditValue32[1] = "nquoteoriginalcurprice";
    EditValue32[2] = "nquoteoriginalcurtaxprice";
    EditValue32[3] = "nquoteoriginalcurnetprice";
    EditValue32[4] = "nquoteoriginalcurtaxnetprice";

    EditValue32[5] = "nquoteprice";
    EditValue32[6] = "nquotetaxprice";
    EditValue32[7] = "nquotenetprice";
    EditValue32[8] = "nquotetaxnetprice";

    EditValue32[9] = "nquotenumber";

    EditValue32[10] = "cquoteunitname";
    EditValue32[11] = "cquoteunitid";
    EditValue32[12] = "bqtfixedflag";
    EditValue32[13] = "nqtscalefactor";

    for (int i = 0; i < EditKey.length; i++) {
      itemkeys.put(EditKey[i], EditKey[i]);
    }
    for (int i = 0; i < EditKey.length; i++) {
      if (i <= 13)
        itemkeys32.put(EditKey[i], EditValue32[i]);
      else
        itemkeys32.put(EditKey[i], EditKey[i]);
    }
    for (int i = 0; i < EditKey.length; i++)
      if (i != 23)
        itemkeys38.put(EditKey[i], EditKey[i]);
      else
        itemkeys38.put(EditKey[i], "narrnum");
  }

  public static void initItemKeys(String billType)
  {
    String[] EditKey = new String[55];
    EditKey[0] = "ccurrencytypeid";

    EditKey[1] = "norgqtprc";
    EditKey[2] = "norgqttaxprc";
    EditKey[3] = "norgqtnetprc";
    EditKey[4] = "norgqttaxnetprc";

    EditKey[5] = "nqtprc";
    EditKey[6] = "nqttaxprc";
    EditKey[7] = "nqtnetprc";
    EditKey[8] = "nqttaxnetprc";

    EditKey[9] = "nquoteunitnum";

    EditKey[10] = "cquoteunit";
    EditKey[11] = "cquoteunitid";

    EditKey[12] = "bqtfixedflag";
    EditKey[13] = "nqtscalefactor";

    EditKey[14] = "noriginalcurprice";
    EditKey[15] = "noriginalcurtaxprice";
    EditKey[16] = "noriginalcurnetprice";
    EditKey[17] = "noriginalcurtaxnetprice";

    EditKey[18] = "nprice";
    EditKey[19] = "ntaxprice";
    EditKey[20] = "nnetprice";
    EditKey[21] = "ntaxnetprice";
    EditKey[22] = "assistunit";

    EditKey[23] = "nnumber";
    EditKey[24] = "npacknumber";
    EditKey[25] = "cunitname";
    EditKey[26] = "cunitid";
    EditKey[27] = "cpackunitid";
    EditKey[28] = "cpackunitname";
    EditKey[29] = "fixedflag";
    EditKey[30] = "scalefactor";
    EditKey[31] = "ndiscountrate";
    EditKey[32] = "nitemdiscountrate";

    EditKey[33] = "nassistcurprice";
    EditKey[34] = "nassistcurtaxprice";
    EditKey[35] = "nassistcurnetprice";
    EditKey[36] = "nassistcurtaxnetprice";

    EditKey[37] = "nexchangeotobrate";
    EditKey[38] = "nexchangeotoarate";

    EditKey[39] = "noriginalcurmny";
    EditKey[40] = "noriginalcurtaxmny";
    EditKey[41] = "noriginalcurdiscountmny";
    EditKey[42] = "noriginalcursummny";

    EditKey[43] = "nmny";
    EditKey[44] = "ntaxmny";
    EditKey[45] = "ndiscountmny";
    EditKey[46] = "nsummny";

    EditKey[47] = "nassistcurmny";
    EditKey[48] = "nassistcurtaxmny";
    EditKey[49] = "nassistcurdiscountmny";
    EditKey[50] = "nassistcursummny";

    EditKey[51] = "norgviapricetax";
    EditKey[52] = "norgviaprice";

    EditKey[53] = "ntaxrate";
    EditKey[54] = "cquoteunitname";

    String[] EditValue32 = new String[14];

    EditValue32[0] = "ccurrencytypeid";

    EditValue32[1] = "nquoteoriginalcurprice";
    EditValue32[2] = "nquoteoriginalcurtaxprice";
    EditValue32[3] = "nquoteoriginalcurnetprice";
    EditValue32[4] = "nquoteoriginalcurtaxnetprice";

    EditValue32[5] = "nquoteprice";
    EditValue32[6] = "nquotetaxprice";
    EditValue32[7] = "nquotenetprice";
    EditValue32[8] = "nquotetaxnetprice";

    EditValue32[9] = "nquotenumber";

    EditValue32[10] = "cquoteunitname";
    EditValue32[11] = "cquoteunitid";
    EditValue32[12] = "bqtfixedflag";
    EditValue32[13] = "nqtscalefactor";

    for (int i = 0; i < EditKey.length; i++) {
      itemkeys.put(EditKey[i], EditKey[i]);
    }

    for (int i = 0; i < EditKey.length; i++) {
      if (i <= 13)
        itemkeys32.put(EditKey[i], EditValue32[i]);
      else
        itemkeys32.put(EditKey[i], EditKey[i]);
    }
    for (int i = 0; i < EditKey.length; i++) {
      if (i != 23)
        itemkeys38.put(EditKey[i], EditKey[i]);
      else {
        itemkeys38.put(EditKey[i], "narrnum");
      }
    }
    for (int i = 0; i < EditKey.length; i++)
    {
      if (i == 13)
        itemkeys33.put(EditKey[i], "nquoteunitrate");
      else if (i == 23)
        itemkeys33.put(EditKey[i], "nnewbalancenum");
      else if (i == 43)
        itemkeys33.put(EditKey[i], "nmny");
      else {
        itemkeys33.put(EditKey[i], EditKey[i]);
      }
    }
    for (int i = 0; i < EditKey.length; i++)
    {
      if (i == 13)
        itemkeys3U.put(EditKey[i], "nquoteunitrate");
      else {
        itemkeys3U.put(EditKey[i], EditKey[i]);
      }
    }
    for (int i = 0; i < EditKey.length; i++)
    {
      if (i == 13)
        itemkeys3V.put(EditKey[i], "nquoteunitrate");
      else if (i == 23)
        itemkeys3V.put(EditKey[i], "ntakenumber");
      else if (i == 24)
        itemkeys3V.put(EditKey[i], "ntakepacknumber");
      else
        itemkeys3V.put(EditKey[i], EditKey[i]);
    }
  }

  public static String key(String billtype, String name)
  {
    if ((name == null) || (billtype == null))
      return null;
    if ("30".equals(billtype))
      return itemkeys.getProperty(name);
    if ("3U".equals(billtype))
      return itemkeys3U.getProperty(name);
    if ("3V".equals(billtype))
      return itemkeys3V.getProperty(name);
    if ("32".equals(billtype))
      return itemkeys32.getProperty(name);
    if ("38".equals(billtype))
      return itemkeys38.getProperty(name);
    if ("33".equals(billtype)) {
      return itemkeys33.getProperty(name);
    }
    return itemkeys.getProperty(name);
  }

  public static void reLoadBillState(SaleBillUI ui, ClientEnvironment evn)
  {
    long s = System.currentTimeMillis();

    BillCardPanel cardpanel = null;
    BillListPanel listpanel = null;

    if ("列表".equals(ui.strShowState))
    {
      listpanel = ui.getBillListPanel();
    }
    else
    {
      cardpanel = ui.getBillCardPanel();
    }

    String csaleid = null;

    if (cardpanel != null)
    {
      csaleid = cardpanel.getHeadItem("csaleid").getValue();
    }
    else if (listpanel != null)
    {
      csaleid = (String)listpanel.getHeadBillModel().getValueAt(listpanel.getHeadTable().getSelectedRow(), "csaleid");
    }

    if ((csaleid == null) || (csaleid.length() <= 0)) {
      return;
    }
    String sql = "select so_sale.csaleid,so_saleorder_b.corder_bid,so_sale.fstatus,so_sale.breceiptendflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.ibalanceflag,so_sale.bpayendflag,so_sale.btransendflag,so_sale.ts,so_sale.capproveid,so_saleorder_b.frowstatus,so_saleorder_b.ts,so_saleexecute.bifinvoicefinish,so_saleexecute.bifpaybalance,so_saleexecute.bifpayfinish,so_saleexecute.bifreceiptfinish,so_saleexecute.bifinventoryfinish,so_saleexecute.bifpaysign,so_saleexecute.biftransfinish,so_saleexecute.dlastconsigdate,so_saleexecute.dlasttransdate,so_saleexecute.dlastoutdate,so_saleexecute.dlastinvoicedt,so_saleexecute.dlastpaydate,so_saleexecute.ntotalreturnnumber,so_saleexecute.ntotalcarrynumber,so_saleexecute.ntaltransnum,so_saleexecute.ntaltransretnum,so_saleexecute.ntranslossnum,so_saleexecute.ntotalplanreceiptnumber,so_saleexecute.ntotalreceiptnumber,so_saleexecute.ntotalinvoicenumber,so_saleexecute.ntotalbalancenumber,so_saleexecute.ntotalpaymny,so_saleexecute.ntotalinventorynumber,so_saleexecute.ntotalsignnumber,so_saleexecute.ntotalcostmny,so_saleexecute.narrangescornum,so_saleexecute.narrangepoapplynum,so_saleexecute.narrangetoornum,so_saleexecute.norrangetoapplynum,so_saleexecute.barrangedflag,so_saleexecute.carrangepersonid,so_saleexecute.narrangemonum,  so_saleexecute.ts , so_sale.dbilltime , so_sale.daudittime,  so_sale.dmoditime,  so_saleexecute.ntotlbalcostnum  from so_sale,so_saleorder_b,so_saleexecute where so_sale.csaleid=so_saleorder_b.csaleid and so_sale.csaleid=so_saleexecute.csaleid and so_saleorder_b.csaleid=so_saleexecute.csaleid and so_saleorder_b.beditflag='N' and so_saleexecute.creceipttype='30' and so_saleorder_b.corder_bid=so_saleexecute.csale_bid and so_sale.csaleid='" + csaleid.trim() + "'";

    SORowData[] rows = null;
    try
    {
      rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    if ((rows == null) || (rows.length <= 0)) {
      return;
    }
    HashMap hs = new HashMap(rows.length * 3 / 2);
    int i = 0; for (int loop = rows.length; i < loop; i++) {
      hs.put(rows[i].getString(1), rows[i]);
    }

    String capproveid = evn.getUser().getPrimaryKey();
    String capprovename = evn.getUser().getUserName();

    String dapprovedate = evn.getDate().toString();

    Integer fstatus = rows[0].getInteger(2);

    if (cardpanel != null)
    {
      boolean bisCalculate = cardpanel.getBillModel().isNeedCalculate();
      cardpanel.getBillModel().setNeedCalculate(false);
      try
      {
        BillItem bm = null;
        bm = cardpanel.getHeadItem("fstatus");
        if (bm != null) {
          bm.setValue(rows[0].getInteger(2));
        }

        bm = cardpanel.getHeadItem("breceiptendflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(3));
        }

        bm = cardpanel.getHeadItem("boutendflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(4));
        }

        bm = cardpanel.getHeadItem("binvoicendflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(5));
        }

        bm = cardpanel.getHeadItem("ibalanceflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(6));
        }

        bm = cardpanel.getHeadItem("bpayendflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(7));
        }

        bm = cardpanel.getHeadItem("btransendflag");
        if (bm != null) {
          bm.setValue(rows[0].getUFBoolean(8));
        }

        bm = cardpanel.getHeadItem("ts");
        if (bm != null) {
          bm.setValue(rows[0].getUFDateTime(9));
        }
        bm = cardpanel.getTailItem("dbilltime");
        if (bm != null)
          bm.setValue(rows[0].getUFDateTime(46));
        bm = cardpanel.getTailItem("daudittime");
        if (bm != null)
          bm.setValue(rows[0].getUFDateTime(47));
        bm = cardpanel.getTailItem("dmoditime");
        if (bm != null) {
          bm.setValue(rows[0].getUFDateTime(48));
        }

        if ((fstatus != null) && ((fstatus.intValue() == 2) || (fstatus.intValue() == 6) || (fstatus.intValue() == 3)))
        {
          String stemp = cardpanel.getTailItem("capproveid").getValue();

          if ((stemp == null) || (stemp.trim().length() <= 0)) {
            cardpanel.setTailItem("capproveid", capproveid);
          }

          stemp = cardpanel.getTailItem("dapprovedate").getValue();
          if ((stemp == null) || (stemp.trim().length() <= 0))
            cardpanel.setTailItem("dapprovedate", dapprovedate);
        }
        else
        {
          cardpanel.setTailItem("capproveid", null);

          cardpanel.setTailItem("dapprovedate", null);
        }

        SORowData row = null;
        int i1 = 0; for (int loop = cardpanel.getRowCount(); i1 < loop; i1++) {
          row = (SORowData)hs.get(cardpanel.getBodyValueAt(i1, "corder_bid"));

          if (row == null) {
            continue;
          }
          cardpanel.setBodyValueAt(row.getInteger(11), i1, "frowstatus");

          cardpanel.setBodyValueAt(row.getUFDateTime(12), i1, "ts");

          cardpanel.setBodyValueAt(row.getUFBoolean(13), i1, "bifinvoicefinish");

          cardpanel.setBodyValueAt(row.getUFBoolean(14), i1, "bifpaybalance");

          cardpanel.setBodyValueAt(row.getUFBoolean(15), i1, "bifpayfinish");

          cardpanel.setBodyValueAt(row.getUFBoolean(16), i1, "bifreceiptfinish");

          cardpanel.setBodyValueAt(row.getUFBoolean(17), i1, "bifinventoryfinish");

          cardpanel.setBodyValueAt(row.getUFBoolean(18), i1, "bifpaysign");

          cardpanel.setBodyValueAt(row.getUFBoolean(19), i1, "biftransfinish");

          cardpanel.setBodyValueAt(row.getUFDate(20), i1, "dlastconsigdate");

          cardpanel.setBodyValueAt(row.getUFDate(21), i1, "dlasttransdate");

          cardpanel.setBodyValueAt(row.getUFDate(22), i1, "dlastoutdate");

          cardpanel.setBodyValueAt(row.getUFDate(23), i1, "dlastinvoicedt");

          cardpanel.setBodyValueAt(row.getUFDate(24), i1, "dlastpaydate");

          cardpanel.setBodyValueAt(row.getUFDouble(25), i1, "ntotalreturnnumber");

          cardpanel.setBodyValueAt(row.getUFDouble(26), i1, "ntotalcarrynumber");

          cardpanel.setBodyValueAt(row.getUFDouble(27), i1, "ntaltransnum");

          cardpanel.setBodyValueAt(row.getUFDouble(28), i1, "ntaltransretnum");

          cardpanel.setBodyValueAt(row.getUFDouble(29), i1, "ntranslossnum");

          cardpanel.setBodyValueAt(row.getUFDouble(30), i1, "ntotalplanreceiptnumber");

          cardpanel.setBodyValueAt(row.getUFDouble(31), i1, "ntotalreceiptnumber");

          cardpanel.setBodyValueAt(row.getUFDouble(32), i1, "ntotalinvoicenumber");

          cardpanel.setBodyValueAt(row.getUFDouble(33), i1, "ntotalbalancenumber");

          cardpanel.setBodyValueAt(row.getUFDouble(34), i1, "ntotalpaymny");

          cardpanel.setBodyValueAt(row.getUFDouble(35), i1, "ntotalinventorynumber");

          cardpanel.setBodyValueAt(row.getUFDouble(36), i1, "ntotalsignnumber");

          cardpanel.setBodyValueAt(row.getUFDouble(37), i1, "ntotalcostmny");

          cardpanel.setBodyValueAt(row.getUFDouble(38), i1, "narrangescornum");

          cardpanel.setBodyValueAt(row.getUFDouble(39), i1, "narrangepoapplynum");

          cardpanel.setBodyValueAt(row.getUFDouble(40), i1, "narrangetoornum");

          cardpanel.setBodyValueAt(row.getUFDouble(41), i1, "norrangetoapplynum");

          cardpanel.setBodyValueAt(row.getUFBoolean(42), i1, "barrangedflag");

          cardpanel.setBodyValueAt(row.getString(43), i1, "carrangepersonid");

          cardpanel.setBodyValueAt(row.getUFDouble(44), i1, "narrangemonum");

          cardpanel.setBodyValueAt(row.getUFDateTime(45), i1, "exets");

          cardpanel.setBodyValueAt(row.getUFDouble(49), i1, "ntotlbalcostnum");
        }
      }
      catch (Exception e)
      {
        cardpanel.getBillModel().setNeedCalculate(bisCalculate);
        SCMEnv.out(e.getMessage());
      }
    }
    else if (listpanel != null)
    {
      boolean bisCalculate = listpanel.getBodyBillModel().isNeedCalculate();

      listpanel.getBodyBillModel().setNeedCalculate(false);
      try
      {
        int selrow = listpanel.getHeadTable().getSelectedRow();

        listpanel.getHeadBillModel().setValueAt(fstatus, selrow, "fstatus");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFBoolean(3), selrow, "breceiptendflag");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFBoolean(4), selrow, "boutendflag");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFBoolean(5), selrow, "binvoicendflag");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFBoolean(6), selrow, "ibalanceflag");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFBoolean(8), selrow, "btransendflag");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFDateTime(9), selrow, "ts");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFDateTime(46), selrow, "dbilltime");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFDateTime(47), selrow, "daudittime");

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFDateTime(48), selrow, "dmoditime");

        if ((fstatus != null) && ((fstatus.intValue() == 2) || (fstatus.intValue() == 6) || (fstatus.intValue() == 3)))
        {
          String stemp = (String)listpanel.getHeadBillModel().getValueAt(selrow, "capproveid");

          if ((stemp == null) || (stemp.trim().length() <= 0))
          {
            listpanel.getHeadBillModel().setValueAt(capproveid, selrow, "capproveid");

            listpanel.getHeadBillModel().setValueAt(capprovename, selrow, "capprovename");

            listpanel.getHeadBillModel().setValueAt(dapprovedate, selrow, "dapprovedate");
          }

        }
        else
        {
          listpanel.getHeadBillModel().setValueAt(null, selrow, "capproveid");

          listpanel.getHeadBillModel().setValueAt(null, selrow, "capprovename");

          listpanel.getHeadBillModel().setValueAt(null, selrow, "dapprovedate");
        }

        SORowData row = null;
        int i1 = 0; int loop = listpanel.getBodyBillModel().getRowCount();
        for (; i1 < loop; i1++) {
          row = (SORowData)hs.get(listpanel.getBodyBillModel().getValueAt(i1, "corder_bid"));

          if (row == null) {
            continue;
          }
          listpanel.getBodyBillModel().setValueAt(row.getInteger(11), i1, "frowstatus");

          listpanel.getBodyBillModel().setValueAt(row.getUFDateTime(12), i1, "ts");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(13), i1, "bifinvoicefinish");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(14), i1, "bifpaybalance");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(15), i1, "bifpayfinish");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(16), i1, "bifreceiptfinish");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(17), i1, "bifinventoryfinish");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(18), i1, "bifpaysign");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(19), i1, "biftransfinish");

          listpanel.getBodyBillModel().setValueAt(row.getUFDate(20), i1, "dlastconsigdate");

          listpanel.getBodyBillModel().setValueAt(row.getUFDate(21), i1, "dlasttransdate");

          listpanel.getBodyBillModel().setValueAt(row.getUFDate(22), i1, "dlastoutdate");

          listpanel.getBodyBillModel().setValueAt(row.getUFDate(23), i1, "dlastinvoicedt");

          listpanel.getBodyBillModel().setValueAt(row.getUFDate(24), i1, "dlastpaydate");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(25), i1, "ntotalreturnnumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(26), i1, "ntotalcarrynumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(27), i1, "ntaltransnum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(28), i1, "ntaltransretnum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(29), i1, "ntranslossnum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(30), i1, "ntotalplanreceiptnumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(31), i1, "ntotalreceiptnumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(32), i1, "ntotalinvoicenumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(33), i1, "ntotalbalancenumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(34), i1, "ntotalpaymny");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(35), i1, "ntotalinventorynumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(36), i1, "ntotalsignnumber");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(37), i1, "ntotalcostmny");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(38), i1, "narrangescornum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(39), i1, "narrangepoapplynum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(40), i1, "narrangetoornum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(41), i1, "norrangetoapplynum");

          listpanel.getBodyBillModel().setValueAt(row.getUFBoolean(42), i1, "barrangedflag");

          listpanel.getBodyBillModel().setValueAt(row.getString(43), i1, "carrangepersonid");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(44), i1, "narrangemonum");

          listpanel.getBodyBillModel().setValueAt(row.getUFDateTime(45), i1, "exets");

          listpanel.getBodyBillModel().setValueAt(row.getUFDouble(49), i1, "ntotlbalcostnum");
        }
      }
      catch (Exception e)
      {
        listpanel.getBodyBillModel().setNeedCalculate(bisCalculate);
        SCMEnv.out(e.getMessage());
      }

    }

    SCMEnv.out("nc.ui.so.so001.BillTools.reLoadBillState:" + (System.currentTimeMillis() - s));
  }

  public static void setBillModelDigit(BillModel bm, String pk_corp, String[] numkeys, String[] astnumkeys, String[] ratekeys, String[] pricekeys)
  {
    if ((bm == null) || (pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      return;
    }
    String[] para = { "BD302", "BD501", "BD502", "BD503", "BD505" };
    Hashtable h = null;
    try {
      h = SysInitBO_Client.queryBatchParaValues(pk_corp, para);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    Object otemp = null;
    otemp = h.get("BD302");

    otemp = h.get("BD501");

    Integer BD501 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD502");

    Integer BD502 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD503");

    Integer BD503 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(4) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD505");

    Integer BD505 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    BillItem bi = null;

    if (numkeys != null) {
      int i = 0; for (int loop = numkeys.length; i < loop; i++) {
        bi = bm.getItemByKey(numkeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD501.intValue());
        }
      }
    }

    if (astnumkeys != null) {
      int i = 0; for (int loop = astnumkeys.length; i < loop; i++) {
        bi = bm.getItemByKey(astnumkeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD502.intValue());
        }
      }
    }

    if (ratekeys != null) {
      int i = 0; for (int loop = ratekeys.length; i < loop; i++) {
        bi = bm.getItemByKey(ratekeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD503.intValue());
        }
      }
    }

    if (pricekeys != null) {
      int i = 0; for (int loop = pricekeys.length; i < loop; i++) {
        bi = bm.getItemByKey(pricekeys[i]);
        if (bi != null)
          bi.setDecimalDigits(BD505.intValue());
      }
    }
  }

  public static void setBodyValueAtByDigit(BillModel bm, UFDouble value, int row, String key, int digit)
  {
    if ((bm == null) || (row < 0) || (key == null) || (row >= bm.getRowCount()))
      return;
    if (value == null) {
      bm.setValueAt(value, row, key);
    }
    BillItem bi = bm.getItemByKey(key);
    if (bi == null) {
      return;
    }

    bi.setDecimalDigits(digit);
    bm.setValueAt(value, row, key);
  }

  public static void setMnyDigitByCurrency(CircularlyAccessibleValueObject[] vos, String pk_corp, String curridkey, String[] setmnykey)
    throws BusinessException, Exception
  {
    if ((vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (setmnykey == null) || (curridkey == null))
    {
      return;
    }String sCurrid = null;

    CurrinfoVO currVO = null;

    int iDigit = 4;
    UFDouble mny = null;

    int i = 0; for (int loop = vos.length; i < loop; i++) {
      sCurrid = (String)vos[i].getAttributeValue(curridkey);
      if ((sCurrid == null) || (sCurrid.trim().length() <= 0)) {
        continue;
      }
      BusinessCurrencyRateUtil ccurr = new BusinessCurrencyRateUtil(pk_corp);

      currVO = ccurr.getCurrinfoVO(sCurrid, null);

      if ((currVO == null) || (currVO.getCurrdigit() == null)) {
        continue;
      }
      iDigit = currVO.getCurrdigit().intValue();
      int k = 0; for (int loopk = setmnykey.length; k < loopk; k++) {
        mny = (UFDouble)vos[i].getAttributeValue(setmnykey[k]);
        if (mny == null)
          continue;
        mny = mny.setScale(0 - iDigit, 4);
        vos[i].setAttributeValue(setmnykey[k], mny);
      }
    }
  }

  public static void setMnyToHeadModelByCurrency(BillModel bm, CircularlyAccessibleValueObject[] vos, String pk_corp, String curridkey, String[] mnykey)
  {
    if ((bm == null) || (vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (curridkey == null))
    {
      return;
    }
    String sCurrid = null;

    CurrtypeQuery currquery = CurrtypeQuery.getInstance();

    CurrtypeVO currtypevo = null;

    int iDigit = 4;
    UFDouble mny = null;

    int i = 0; for (int loop = vos.length; i < loop; i++) {
      sCurrid = (String)vos[i].getAttributeValue(curridkey);
      if ((sCurrid == null) || (sCurrid.trim().length() <= 0)) {
        continue;
      }
      currtypevo = currquery.getCurrtypeVO(sCurrid);

      iDigit = currtypevo.getCurrbusidigit() == null ? 4 : currtypevo.getCurrbusidigit().intValue();

      int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
        if (mnykey[k] == null)
          continue;
        mny = (UFDouble)vos[i].getAttributeValue(mnykey[k]);
        if (mny == null)
          continue;
        mny = mny.setScale(0 - iDigit, 4);
        vos[i].setAttributeValue(mnykey[k], mny);

        setBodyValueAtByDigit(bm, mny, i, mnykey[k], iDigit);
      }
    }
  }

  public static void setMnyToModelByCurrency(BillModel bm, CircularlyAccessibleValueObject vo, int row, String pk_corp, String currid, String[] mnykey)
    throws BusinessException
  {
    if ((bm == null) || (vo == null) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (currid == null) || (row <= 0) || (row >= bm.getRowCount()))
    {
      return;
    }
    CurrinfoVO currVO = null;

    int iDigit = 4;
    UFDouble mny = null;

    BusinessCurrencyRateUtil ccurr = null;
    try {
      ccurr = new BusinessCurrencyRateUtil(pk_corp);
    } catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
    currVO = ccurr.getCurrinfoVO(currid, null);

    if ((currVO == null) || (currVO.getCurrdigit() == null)) {
      return;
    }

    iDigit = currVO.getCurrdigit().intValue();
    int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
      if (mnykey[k] == null)
        continue;
      mny = (UFDouble)vo.getAttributeValue(mnykey[k]);
      if (mny == null)
        continue;
      mny = mny.setScale(0 - iDigit, 4);
      vo.setAttributeValue(mnykey[k], mny);

      setBodyValueAtByDigit(bm, mny, row, mnykey[k], iDigit);
    }
  }

  public static void setZBZFByCurrency(BillModel bm, CircularlyAccessibleValueObject[] vos, String pk_corp, String curridkey, String[] mnykey)
    throws BusinessException
  {
    if ((bm == null) || (vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (curridkey == null))
    {
      return;
    }String sCurrid = null;

    CurrinfoVO currtypevo = null;

    CurrencyRateUtil currArith = new CurrencyRateUtil(pk_corp);

    int fracdigit = 4;

    String pk_frac = currArith.getFracCurrPK();
    if (pk_frac != null) {
      currtypevo = currArith.getCurrinfoVO(pk_frac, null);

      fracdigit = currtypevo.getRatedigit() == null ? 4 : currtypevo.getRatedigit().intValue();
    }

    int iDigit = 4;
    UFDouble mny = null;

    int i = 0; for (int loop = vos.length; i < loop; i++) {
      sCurrid = (String)vos[i].getAttributeValue(curridkey);
      if ((sCurrid == null) || (sCurrid.trim().length() <= 0))
        continue;
      currtypevo = currArith.getCurrinfoVO(sCurrid, null);

      iDigit = currtypevo.getRatedigit() == null ? 4 : currtypevo.getRatedigit().intValue();

      if (pk_frac == null)
        fracdigit = iDigit;
      int index = 0;
      mny = (UFDouble)vos[i].getAttributeValue(mnykey[index]);
      if (mny != null) {
        mny = mny.setScale(0 - fracdigit, 4);
        vos[i].setAttributeValue(mnykey[index], mny);
        setBodyValueAtByDigit(bm, mny, i, mnykey[index], fracdigit);
      }

      index = 1;
      mny = (UFDouble)vos[i].getAttributeValue(mnykey[index]);
      if (mny != null) {
        mny = mny.setScale(0 - iDigit, 4);
        vos[i].setAttributeValue(mnykey[index], mny);

        setBodyValueAtByDigit(bm, mny, i, mnykey[index], iDigit);
      }
    }
  }

  public static UFDouble taxRate(int row, BillModel bm, String billtype)
  {
    if (bm == null)
      return null;
    UFDouble o = value(row, key(billtype, "ntaxrate"), bm);
    if (o == null)
      return uf0;
    return calc(row, key(billtype, "ntaxrate"), uf100, 3, bm);
  }

  public static Object[] batchRemoteCall(String[] classname, String[] funcname, ArrayList paramclasses, ArrayList paramObjects)
    throws Exception
  {
    Object[] retobjs = null;
    if ((classname == null) || (funcname == null) || (classname.length <= 0) || (funcname.length <= 0) || (classname.length != funcname.length))
    {
      return retobjs;
    }
    ComActionDescVO[] comdesc = new ComActionDescVO[classname.length];

    Boolean uffalse = new Boolean(false);
    int i = 0; for (int loop = comdesc.length; i < loop; i++)
    {
      comdesc[i] = new ComActionDescVO();
      comdesc[i].setComName(classname[i]);
      comdesc[i].setIsBo(uffalse);
      comdesc[i].setFuncName(funcname[i]);
    }

    return nc.ui.so.so001.SOToolsBO_Client.processActionExt(comdesc, paramclasses, paramObjects);
  }

  public static Object[] batchRemoteCallQuery(String[] classname, String[] funcname, ArrayList paramclasses, ArrayList paramObjects)
    throws Exception
  {
    Object[] retobjs = null;
    if ((classname == null) || (funcname == null) || (classname.length <= 0) || (funcname.length <= 0) || (classname.length != funcname.length))
    {
      return retobjs;
    }
    ComActionDescVO[] comdesc = new ComActionDescVO[classname.length];

    Boolean uffalse = new Boolean(false);
    int i = 0; for (int loop = comdesc.length; i < loop; i++)
    {
      comdesc[i] = new ComActionDescVO();
      comdesc[i].setComName(classname[i]);
      comdesc[i].setIsBo(uffalse);
      comdesc[i].setFuncName(funcname[i]);
    }

    return nc.ui.so.so001.SOToolsBO_Client.processQueryExt(comdesc, paramclasses, paramObjects);
  }

  public static Object[] batchRemoteCallQueryX(String[] classname, String[] funcname, ArrayList paramObjects)
    throws Exception
  {
    Object[] retobjs = null;
    if ((classname == null) || (funcname == null) || (classname.length <= 0) || (funcname.length <= 0) || (classname.length != funcname.length))
    {
      return retobjs;
    }
    ComActionDescVO[] comdesc = new ComActionDescVO[classname.length];

    Boolean uffalse = new Boolean(false);
    int i = 0; for (int loop = comdesc.length; i < loop; i++)
    {
      comdesc[i] = new ComActionDescVO();
      comdesc[i].setComName(classname[i]);
      comdesc[i].setIsBo(uffalse);
      comdesc[i].setFuncName(funcname[i]);
    }

    return nc.ui.so.so001.SOToolsBO_Client.processQueryExtX(comdesc, paramObjects);
  }

  public static Object[] batchRemoteCallX(String[] classname, String[] funcname, ArrayList paramObjects)
    throws Exception
  {
    Object[] retobjs = null;
    if ((classname == null) || (funcname == null) || (classname.length <= 0) || (funcname.length <= 0) || (classname.length != funcname.length))
    {
      return retobjs;
    }
    ComActionDescVO[] comdesc = new ComActionDescVO[classname.length];

    Boolean uffalse = new Boolean(false);
    int i = 0; for (int loop = comdesc.length; i < loop; i++)
    {
      comdesc[i] = new ComActionDescVO();
      comdesc[i].setComName(classname[i]);
      comdesc[i].setIsBo(uffalse);
      comdesc[i].setFuncName(funcname[i]);
    }

    return nc.ui.so.so001.SOToolsBO_Client.processActionExtX(comdesc, paramObjects);
  }

  public static void calcEditFunFor38(boolean isTax, UFBoolean isFindPrice, String isEditProject, int FunType, int row, BillModel bm, String billtype)
  {
    calcTaxEditFunFor38(row, isEditProject, bm, billtype);
    calcUnitNum(row, bm, isEditProject, billtype);
    if ((discount(row, bm).doubleValue() == 1.0D) && (itemDiscount(row, bm).doubleValue() == 1.0D))
    {
      bm.setValueAt(new UFDouble(0.0D), row, key(billtype, "noriginalcurdiscountmny"));
    }

    calcQPriceByMny(row, bm, billtype);
    calcViaPrice1(row, bm, billtype);
  }

  public static void calcNoTaxEditFun(int row, String isEditProject, BillModel bm)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key("nquoteunitnum"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble noriginalcurmny = calc(row, key("norgquotenetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurmny"), key("noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurmny")))
    {
      UFDouble norgquotenetprc = calc(row, key("noriginalcurmny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble nitemdiscountrate = calc(row, key("norgquotenetprc"), calc(row, key("norgquoteprc"), discount(row, bm), 2, bm), 3, bm);

      if (value(row, key("nitemdiscountrate"), bm) == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(calc(nitemdiscountrate, uf100, 2), row, key("nitemdiscountrate"));
      }

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotenetprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgquotetaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble norgquotetaxprc = calc(calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(norgquotetaxprc, row, key("norgquotetaxprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcursummny")))
    {
      UFDouble norgquotetaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble norgquotenetprc = calc(row, key("norgquotetaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgquotetaxnetprc"), calc(row, key("norgquotetaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("ndiscountrate"))) || (isEditProject.equals(key("nitemdiscountrate"))))
    {
      UFDouble norgquotenetprc = calc(calc(row, key("norgquoteprc"), discount(row, bm), 2, bm), itemDiscount(row, bm), 2);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble norgquotetaxnetprc = calc(calc(row, key("norgquotetaxprc"), discount(row, bm), 2, bm), itemDiscount(row, bm), 2);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcursummny = calc(row, key("norgquotetaxnetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurmny"), key("noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotenetprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      if (norgquotetaxnetprc != null) {
        bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));
      }

      UFDouble norgquotetaxprc = calc(row, key("norgquoteprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      if (norgquotetaxprc != null) {
        bm.setValueAt(norgquotetaxprc, row, key("norgquotetaxprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("norgquotenetprc")))
    {
      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgquotenetprc"), calc(row, key("norgquoteprc"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));

      UFDouble noriginalcurmny = calc(row, key("norgquotenetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurmny"), key("noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotenetprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("norgquoteprc")))
    {
      UFDouble norgquotenetprc = calc(row, key("norgquoteprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble noriginalcurmny = calc(row, key("norgquotenetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurmny"), key("noriginalcurtaxmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotenetprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble norgquotetaxprc = calc(row, key("norgquoteprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      bm.setValueAt(norgquotetaxprc, row, key("norgquotetaxprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurdiscountmny")))
    {
      UFDouble noriginalcursummny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcurdiscountmny"), 1, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key("nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));
    }
  }

  public static void calcTaxEditFun(int row, String isEditProject, BillModel bm)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key("nquoteunitnum"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble noriginalcursummny = calc(row, key("norgquotetaxnetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurmny")))
    {
      UFDouble norgquotenetprc = calc(row, key("noriginalcurmny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgquotenetprc"), calc(row, key("norgquoteprc"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotenetprc"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble norgquotetaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble norgquotetaxprc = calc(calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      bm.setValueAt(norgquotetaxprc, row, key("norgquotetaxprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcursummny")))
    {
      UFDouble norgquotetaxnetprc = calc(row, key("noriginalcursummny"), key("nquoteunitnum"), 3, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble norgquotenetprc = calc(row, key("norgquotetaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgquotetaxnetprc"), calc(row, key("norgquotetaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("ndiscountrate"))) || (isEditProject.equals(key("nitemdiscountrate"))))
    {
      UFDouble norgquotenetprc = calc(row, key("norgquoteprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble norgquotetaxnetprc = calc(row, key("norgquotetaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcursummny = calc(row, key("norgquotetaxnetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble norgquotenetprc = calc(row, key("norgquotetaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (norgquotenetprc != null) {
        bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));
      }

      UFDouble norgquoteprc = calc(row, key("norgquotetaxprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (norgquoteprc != null) {
        bm.setValueAt(norgquoteprc, row, key("norgquoteprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("norgquotetaxnetprc")))
    {
      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("norgquotetaxnetprc"), calc(row, key("norgquotetaxprc"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcursummny = calc(row, key("norgquotetaxnetprc"), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble norgquotenetprc = calc(row, key("norgquotetaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("norgquotetaxprc")))
    {
      UFDouble norgquotetaxnetprc = calc(row, key("norgquotetaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      bm.setValueAt(norgquotetaxnetprc, row, key("norgquotetaxnetprc"));

      UFDouble noriginalcursummny = calc(row, calc(row, key("norgquotetaxprc"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm), key("nquoteunitnum"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble norgquotenetprc = calc(row, key("norgquotetaxnetprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      bm.setValueAt(norgquotenetprc, row, key("norgquotenetprc"));

      UFDouble norgquoteprc = calc(row, key("norgquotetaxprc"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      bm.setValueAt(norgquoteprc, row, key("norgquoteprc"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nquoteunitnum"), key("norgquotetaxprc"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurdiscountmny")))
    {
      UFDouble noriginalcursummny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcurdiscountmny"), 1, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key("nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));
    }
  }

  protected static void calcTaxEditFunFor38(int row, String isEditProject, BillModel bm)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key("nnumber"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key("nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("npacknumber"))) || (isEditProject.equals("cpackunitname")))
    {
      UFDouble nnumber = calc(row, key("npacknumber"), "scalefactor", 2, bm);

      bm.setValueAt(nnumber, row, key("nnumber"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key("nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurmny")))
    {
      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurmny"), key("nnumber"), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurnetprice"), calc(row, key("noriginalcurprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurnetprice"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key("nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurtaxprice = calc(calc(row, key("noriginalcursummny"), key("nnumber"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      if ((noriginalcurtaxprice != null) && (noriginalcurtaxprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxprice = noriginalcurtaxprice.abs();
      }bm.setValueAt(noriginalcurtaxprice, row, key("noriginalcurtaxprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcursummny")))
    {
      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key("nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxprice = value(row, key("noriginalcurtaxprice"), bm);

      if (noriginalcurtaxprice == null) {
        noriginalcurtaxprice = calc(row, key("noriginalcurtaxnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurtaxprice, row, key("norgqttaxprc"));
      }

      UFDouble noriginalcurprice = value(row, key("noriginalcurprice"), bm);

      if (noriginalcurprice == null) {
        noriginalcurprice = calc(row, key("noriginalcurnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurprice, row, key("norgqtprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("ndiscountrate"))) || (isEditProject.equals(key("nitemdiscountrate"))))
    {
      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key("nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (noriginalcurnetprice != null) {
        if (noriginalcurnetprice.doubleValue() < 0.0D)
          noriginalcurnetprice = noriginalcurnetprice.abs();
        bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));
      }

      UFDouble noriginalcurprice = calc(row, key("noriginalcurtaxprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (noriginalcurprice != null) {
        if (noriginalcurprice.doubleValue() < 0.0D)
          noriginalcurprice = noriginalcurprice.abs();
        bm.setValueAt(noriginalcurprice, row, key("noriginalcurprice"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxnetprice")))
    {
      UFDouble noriginalcurtaxprice = value(row, key("noriginalcurtaxprice"), bm);

      if (noriginalcurtaxprice == null) {
        noriginalcurtaxprice = calc(row, key("noriginalcurtaxnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurtaxprice, row, key("norgqttaxprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key("nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurprice = value(row, key("noriginalcurprice"), bm);

      if (noriginalcurprice == null) {
        noriginalcurprice = calc(row, key("noriginalcurnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurprice, row, key("norgqtprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxprice")))
    {
      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcursummny = calc(row, calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm), key("nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurprice = calc(row, key("noriginalcurtaxprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurprice != null) && (noriginalcurprice.doubleValue() < 0.0D))
      {
        noriginalcurprice = noriginalcurprice.abs();
      }bm.setValueAt(noriginalcurprice, row, key("noriginalcurprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key("nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }
  }

  protected static void calcTaxEditFunFor38(int row, String isEditProject, BillModel bm, String billtype)
  {
    if (bm == null)
      return;
    if ((row < 0) || (row >= bm.getRowCount())) {
      return;
    }

    if ((isEditProject.equals(key(billtype, "nnumber"))) || (isEditProject.equals("ccurrencytypename")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key(billtype, "nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("npacknumber"))) || (isEditProject.equals("cpackunitname")))
    {
      UFDouble nnumber = calc(row, key("npacknumber"), "scalefactor", 2, bm);

      bm.setValueAt(nnumber, row, key(billtype, "nnumber"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key(billtype, "nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurmny")))
    {
      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurmny"), key(billtype, "nnumber"), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxmny = calc(row, key("noriginalcurmny"), taxRate(row, bm), 2, bm);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurnetprice"), calc(row, key("noriginalcurprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurnetprice"), calc(uf1, taxRate(row, bm), 0), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxmny")))
    {
      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxmny"), key("noriginalcurmny"), 0, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key(billtype, "nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurtaxprice = calc(calc(row, key("noriginalcursummny"), key(billtype, "nnumber"), 3, bm), calc(discount(row, bm), itemDiscount(row, bm), 2), 3);

      if ((noriginalcurtaxprice != null) && (noriginalcurtaxprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxprice = noriginalcurtaxprice.abs();
      }bm.setValueAt(noriginalcurtaxprice, row, key("noriginalcurtaxprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcursummny")))
    {
      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcursummny"), key(billtype, "nnumber"), 3, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxprice = value(row, key("noriginalcurtaxprice"), bm);

      if (noriginalcurtaxprice == null) {
        noriginalcurtaxprice = calc(row, key("noriginalcurtaxnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurtaxprice, row, key("norgqttaxprc"));
      }

      UFDouble noriginalcurprice = value(row, key("noriginalcurprice"), bm);

      if (noriginalcurprice == null) {
        noriginalcurprice = calc(row, key("noriginalcurnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurprice, row, key("norgqtprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if ((isEditProject.equals(key("ndiscountrate"))) || (isEditProject.equals(key("nitemdiscountrate"))))
    {
      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key(billtype, "nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("ntaxrate")))
    {
      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (noriginalcurnetprice != null) {
        if (noriginalcurnetprice.doubleValue() < 0.0D)
          noriginalcurnetprice = noriginalcurnetprice.abs();
        bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));
      }

      UFDouble noriginalcurprice = calc(row, key("noriginalcurtaxprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if (noriginalcurprice != null) {
        if (noriginalcurprice.doubleValue() < 0.0D)
          noriginalcurprice = noriginalcurprice.abs();
        bm.setValueAt(noriginalcurprice, row, key("noriginalcurprice"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxnetprice")))
    {
      UFDouble noriginalcurtaxprice = value(row, key("noriginalcurtaxprice"), bm);

      if (noriginalcurtaxprice == null) {
        noriginalcurtaxprice = calc(row, key("noriginalcurtaxnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurtaxprice, row, key("norgqttaxprc"));
      }

      UFDouble nitemdiscountrate = calc(uf100, calc(row, key("noriginalcurtaxnetprice"), calc(row, key("noriginalcurtaxprice"), discount(row, bm), 2, bm), 3, bm), 2);

      if (nitemdiscountrate == null)
        bm.setValueAt(uf100, row, key("nitemdiscountrate"));
      else {
        bm.setValueAt(nitemdiscountrate, row, key("nitemdiscountrate"));
      }

      UFDouble noriginalcursummny = calc(row, key("noriginalcurtaxnetprice"), key(billtype, "nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurprice = value(row, key("noriginalcurprice"), bm);

      if (noriginalcurprice == null) {
        noriginalcurprice = calc(row, key("noriginalcurnetprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 3, bm);

        bm.setValueAt(noriginalcurprice, row, key("norgqtprc"));
      }

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }

    if (isEditProject.equals(key("noriginalcurtaxprice")))
    {
      UFDouble noriginalcurtaxnetprice = calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm);

      if ((noriginalcurtaxnetprice != null) && (noriginalcurtaxnetprice.doubleValue() < 0.0D))
      {
        noriginalcurtaxnetprice = noriginalcurtaxnetprice.abs();
      }bm.setValueAt(noriginalcurtaxnetprice, row, key("noriginalcurtaxnetprice"));

      UFDouble noriginalcursummny = calc(row, calc(row, key("noriginalcurtaxprice"), calc(discount(row, bm), itemDiscount(row, bm), 2), 2, bm), key(billtype, "nnumber"), 2, bm);

      bm.setValueAt(noriginalcursummny, row, key("noriginalcursummny"));

      UFDouble noriginalcurtaxmny = calc(calc(row, key("noriginalcursummny"), taxRate(row, bm), 2, bm), calc(uf1, taxRate(row, bm), 0), 3);

      bm.setValueAt(noriginalcurtaxmny, row, key("noriginalcurtaxmny"));

      UFDouble noriginalcurmny = calc(row, key("noriginalcursummny"), key("noriginalcurtaxmny"), 1, bm);

      bm.setValueAt(noriginalcurmny, row, key("noriginalcurmny"));

      UFDouble noriginalcurnetprice = calc(row, key("noriginalcurtaxnetprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurnetprice != null) && (noriginalcurnetprice.doubleValue() < 0.0D))
      {
        noriginalcurnetprice = noriginalcurnetprice.abs();
      }bm.setValueAt(noriginalcurnetprice, row, key("noriginalcurnetprice"));

      UFDouble noriginalcurprice = calc(row, key("noriginalcurtaxprice"), calc(uf1, taxRate(row, bm), 0), 3, bm);

      if ((noriginalcurprice != null) && (noriginalcurprice.doubleValue() < 0.0D))
      {
        noriginalcurprice = noriginalcurprice.abs();
      }bm.setValueAt(noriginalcurprice, row, key("noriginalcurprice"));

      UFDouble noriginalcurdiscountmny = calc(row, calc(row, key(billtype, "nnumber"), key("noriginalcurtaxprice"), 2, bm), key("noriginalcursummny"), 1, bm);

      bm.setValueAt(noriginalcurdiscountmny, row, key("noriginalcurdiscountmny"));
    }
  }

  public static String key(String name)
  {
    if (name == null)
      return null;
    return itemkeys.getProperty(name);
  }

  public static void setBillModelDigitForArColumn(BillModel bm, String pk_corp, String[] locmny)
  {
    if ((bm == null) || (pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      return;
    }
    CurrinfoVO currVO = null;
    int locdigit = 2;
    try
    {
      BusinessCurrencyRateUtil ccurr = new BusinessCurrencyRateUtil(pk_corp);

      currVO = ccurr.getCurrinfoVO(ccurr.getLocalCurrPK(), null);

      locdigit = currVO.getCurrdigit().intValue();
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    BillItem bi = null;

    if (locmny != null) {
      int i = 0; for (int loop = locmny.length; i < loop; i++) {
        bi = bm.getItemByKey(locmny[i]);
        if (bi != null)
          bi.setDecimalDigits(locdigit);
      }
    }
  }

  public static void setBillModelDigit(BillModel bm, String pk_corp, String[] numkeys, String[] astnumkeys, String[] ratekeys, String[] pricekeys, String[] astmny, String[] locmny)
  {
    if ((bm == null) || (pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      return;
    }
    String[] para = { "BD302", "BD501", "BD502", "BD503", "BD505" };
    Hashtable h = null;
    try {
      h = SysInitBO_Client.queryBatchParaValues(pk_corp, para);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    Object otemp = null;
    otemp = h.get("BD302");

    UFBoolean BD302 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new UFBoolean(false) : new UFBoolean(otemp.toString());

    otemp = h.get("BD501");

    Integer BD501 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD502");

    Integer BD502 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD503");

    Integer BD503 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(4) : Integer.valueOf(otemp.toString());

    otemp = h.get("BD505");

    Integer BD505 = (otemp == null) || (otemp.toString().trim().length() <= 0) ? new Integer(2) : Integer.valueOf(otemp.toString());

    BillItem bi = null;

    if (numkeys != null) {
      int i = 0; for (int loop = numkeys.length; i < loop; i++) {
        bi = bm.getItemByKey(numkeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD501.intValue());
        }
      }
    }

    if (astnumkeys != null) {
      int i = 0; for (int loop = astnumkeys.length; i < loop; i++) {
        bi = bm.getItemByKey(astnumkeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD502.intValue());
        }
      }
    }

    if (ratekeys != null) {
      int i = 0; for (int loop = ratekeys.length; i < loop; i++) {
        bi = bm.getItemByKey(ratekeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD503.intValue());
        }
      }
    }

    if (pricekeys != null) {
      int i = 0; for (int loop = pricekeys.length; i < loop; i++) {
        bi = bm.getItemByKey(pricekeys[i]);
        if (bi != null) {
          bi.setDecimalDigits(BD505.intValue());
        }
      }
    }
    int locdigit = 2;

    int astdigit = 2;
    try
    {
      CurrtypeQuery currquery = CurrtypeQuery.getInstance();
      CurrencyRateUtil currArith = new CurrencyRateUtil(pk_corp);
      CurrtypeVO localcurrtypeVO = null;
      CurrtypeVO astcurrtypeVO = null;
      if (currArith.getLocalCurrPK() != null) {
        localcurrtypeVO = currquery.getCurrtypeVO(currArith.getLocalCurrPK());
      }
      locdigit = localcurrtypeVO.getCurrbusidigit() == null ? 4 : localcurrtypeVO.getCurrbusidigit().intValue();

      if (currArith.getFracCurrPK() != null) {
        astcurrtypeVO = currquery.getCurrtypeVO(currArith.getFracCurrPK());
      }
      if (astcurrtypeVO != null)
        astdigit = astcurrtypeVO.getCurrbusidigit() == null ? 4 : astcurrtypeVO.getCurrbusidigit().intValue();
    }
    catch (Exception e)
    {
      SCMEnv.out("digit = currVO.getCurrdigit().intValue() erro!");
    }

    if (locmny != null) {
      int i = 0; for (int loop = locmny.length; i < loop; i++) {
        bi = bm.getItemByKey(locmny[i]);
        if (bi != null) {
          bi.setDecimalDigits(locdigit);
        }
      }
    }

    if ((BD302 != null) && (BD302.booleanValue()) && 
      (astmny != null)) {
      int i = 0; for (int loop = astmny.length; i < loop; i++) {
        bi = bm.getItemByKey(astmny[i]);
        if (bi != null)
          bi.setDecimalDigits(astdigit);
      }
    }
  }

  public static void setMnyModelByCurrencyForSo12(BillModel bm, CircularlyAccessibleValueObject[] vos, String pk_corp, String curridkey, BusinessCurrencyRateUtil currtype, UFBoolean BD302, String[] mnykey)
    throws BusinessException
  {
    if ((bm == null) || (vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (curridkey == null) || (currtype == null))
    {
      return;
    }String sCurrid = null;

    int iDigit = 4;
    UFDouble mny = null;

    int locRateDigit = 4;
    int astRateDigit = 4;

    CurrinfoVO currVO = null;

    CurrinfoVO currVOa = null;

    CurrtypeVO typevo = null;

    int i = 0; for (int loop = vos.length; i < loop; i++) {
      sCurrid = (String)vos[i].getAttributeValue(curridkey);
      if ((sCurrid == null) || (sCurrid.trim().length() <= 0))
        continue;
      currVO = currtype.getCurrinfoVO(sCurrid, null);
      typevo = CurrtypeQuery.getInstance().getCurrtypeVO(sCurrid);
      if ((typevo == null) || (typevo.getCurrdigit() == null)) {
        continue;
      }
      if (typevo.getCurrbusidigit() != null)
        iDigit = typevo.getCurrbusidigit().intValue();
      else {
        iDigit = 4;
      }
      int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
        if (mnykey[k] == null)
          continue;
        mny = (UFDouble)vos[i].getAttributeValue(mnykey[k]);
        if (mny == null)
          continue;
        mny = mny.setScale(0 - iDigit, 4);

        setBodyValueAtByDigit(bm, mny, i, mnykey[k], iDigit);
      }

      try
      {
        if ((BD302 == null) || (!BD302.booleanValue())) {
          locRateDigit = currVO.getRatedigit().intValue();
          astRateDigit = 4;
        } else {
          astRateDigit = currVO.getRatedigit().intValue();
          currVOa = currtype.getCurrinfoVO(currtype.getFracCurrPK(), null);

          locRateDigit = currVOa.getRatedigit().intValue();
        }
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      setBodyValueAtByDigit(bm, (UFDouble)vos[i].getAttributeValue("nexchangeotobrate"), i, "nexchangeotobrate", locRateDigit);

      setBodyValueAtByDigit(bm, (UFDouble)vos[i].getAttributeValue("nexchangeotoarate"), i, "nexchangeotoarate", astRateDigit);
    }
  }

  public static void setMnyToHeadModelByCurrency1(BillModel bm, CircularlyAccessibleValueObject[] vos, String pk_corp, String sCurrid, String[] mnykey)
    throws BusinessException
  {
    if ((bm == null) || (vos == null) || (vos.length <= 0) || (pk_corp == null) || (pk_corp.trim().length() <= 0) || (mnykey == null) || (sCurrid == null))
    {
      return;
    }
    CurrinfoVO currVO = null;

    int iDigit = 4;
    UFDouble mny = null;

    BusinessCurrencyRateUtil bcurr = null;
    try {
      new BusinessCurrencyRateUtil(pk_corp);
    } catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
    currVO = bcurr.getCurrinfoVO(sCurrid, null);

    if ((currVO == null) || (currVO.getCurrdigit() == null))
      return;
    int i = 0; for (int loop = vos.length; i < loop; i++)
    {
      iDigit = currVO.getCurrdigit().intValue();
      int k = 0; for (int loopk = mnykey.length; k < loopk; k++) {
        if (mnykey[k] == null)
          continue;
        mny = (UFDouble)vos[i].getAttributeValue(mnykey[k]);
        if (mny == null)
          continue;
        mny = mny.setScale(0 - iDigit, 4);
        vos[i].setAttributeValue(mnykey[k], mny);

        setBodyValueAtByDigit(bm, mny, i, mnykey[k], iDigit);
      }
    }
  }

  public static UFDouble taxRate(int row, BillModel bm)
  {
    UFDouble ntax = calc(row, key("ntaxrate"), uf100, 3, bm);
    if (ntax == null)
      return SoVoConst.duf0;
    return ntax;
  }

  public static Integer getBodyComBoxIndex(int row, String key, BillModel billmodel)
  {
    Integer index = SoVoConst.i0;
    if ((billmodel == null) || (key == null) || (row < 0) || (row >= billmodel.getRowCount()))
    {
      return index;
    }Object otemp = billmodel.getValueAt(row, key);
    if (otemp != null) {
      BillItem bm = billmodel.getItemByKey(key);
      if (bm != null) {
        index = (Integer)bm.converType(otemp);
      }
    }
    return index;
  }

  public static void reLoadTs(SaleBillUI ui, ClientEnvironment evn)
  {
    long s = System.currentTimeMillis();

    BillCardPanel cardpanel = null;
    BillListPanel listpanel = null;

    if ("列表".equals(ui.strShowState))
    {
      listpanel = ui.getBillListPanel();
    }
    else
    {
      cardpanel = ui.getBillCardPanel();
    }

    String csaleid = null;

    if (cardpanel != null)
    {
      csaleid = cardpanel.getHeadItem("csaleid").getValue();
    }
    else if (listpanel != null)
    {
      csaleid = (String)listpanel.getHeadBillModel().getValueAt(listpanel.getHeadTable().getSelectedRow(), "csaleid");
    }

    if ((csaleid == null) || (csaleid.length() <= 0)) {
      return;
    }
    String sql = "select so_saleorder_b.corder_bid, so_sale.ts, so_saleorder_b.ts, so_saleexecute.ts  from  so_sale,so_saleorder_b,so_saleexecute where so_sale.csaleid=so_saleorder_b.csaleid and so_sale.csaleid=so_saleexecute.csaleid and so_saleorder_b.csaleid=so_saleexecute.csaleid and so_saleorder_b.corder_bid=so_saleexecute.csale_bid and so_saleorder_b.beditflag='N' and so_saleexecute.creceipttype='30' and so_sale.csaleid='" + csaleid.trim() + "' ";

    SORowData[] rows = null;
    try
    {
      rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }

    if ((rows == null) || (rows.length <= 0)) {
      return;
    }
    HashMap hs = new HashMap(rows.length * 3 / 2);
    int i = 0; for (int loop = rows.length; i < loop; i++) {
      hs.put(rows[i].getString(0), rows[i]);
    }

    if (cardpanel != null)
    {
      boolean bisCalculate = cardpanel.getBillModel().isNeedCalculate();
      cardpanel.getBillModel().setNeedCalculate(false);
      try
      {
        BillItem bm = null;

        bm = cardpanel.getHeadItem("ts");
        if (bm != null) {
          bm.setValue(rows[0].getUFDateTime(1));
        }
        SORowData row = null;
        int i1 = 0; for (int loop = cardpanel.getRowCount(); i1 < loop; i1++) {
          row = (SORowData)hs.get(cardpanel.getBodyValueAt(i1, "corder_bid"));

          if (row == null)
          {
            continue;
          }
          cardpanel.setBodyValueAt(row.getUFDateTime(2), i1, "ts");

          cardpanel.setBodyValueAt(row.getUFDateTime(3), i1, "exets");
        }
      }
      catch (Exception e) {
        cardpanel.getBillModel().setNeedCalculate(bisCalculate);
        SCMEnv.out(e.getMessage());
      }
    }
    else if (listpanel != null)
    {
      boolean bisCalculate = listpanel.getBodyBillModel().isNeedCalculate();

      listpanel.getBodyBillModel().setNeedCalculate(false);
      try
      {
        int selrow = listpanel.getHeadTable().getSelectedRow();

        listpanel.getHeadBillModel().setValueAt(rows[0].getUFDateTime(1), selrow, "ts");

        SORowData row = null;
        int i1 = 0; int loop = listpanel.getBodyBillModel().getRowCount();
        for (; i1 < loop; i1++) {
          row = (SORowData)hs.get(listpanel.getBodyBillModel().getValueAt(i1, "corder_bid"));

          if (row == null)
          {
            continue;
          }
          listpanel.getBodyBillModel().setValueAt(row.getUFDateTime(2), i1, "ts");

          listpanel.getBodyBillModel().setValueAt(row.getUFDateTime(3), i1, "exets");
        }
      }
      catch (Exception e)
      {
        listpanel.getBodyBillModel().setNeedCalculate(bisCalculate);
        SCMEnv.out(e.getMessage());
      }

    }

    SCMEnv.out("nc.ui.so.so001.BillTools.reLoadBillState:" + (System.currentTimeMillis() - s));
  }

  public static void calcForeigndw(String pk_corp, String strbillType, String ForeignDate, Integer BD505, BillModel bm, int row, String billtype)
  {
    if (bm == null) {
      return;
    }
    String ForeignID = (String)bm.getValueAt(row, key(billtype, "ccurrencytypeid"));

    UFDouble ARATE = value(row, key(billtype, "nexchangeotobrate"), bm);

    UFDouble NRATE = value(row, key(billtype, "nexchangeotoarate"), bm);

    SaleForeignVO ForeignVO = new SaleForeignVO();
    BusinessCurrencyRateUtil bcurr = null;
    try {
      bcurr = new BusinessCurrencyRateUtil(pk_corp);
    } catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }

    try
    {
      bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nsummny"), bm), ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "noriginalcursummny"));

      ForeignVO.setNsummny(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nsummny"), bm), ARATE, new UFDate(ForeignDate).toString()));

      ForeignVO.setNdiscountmny(value(row, key(billtype, "noriginalcurdiscountmny"), bm));

      ForeignVO.setNmny(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nmny"), bm), ARATE, new UFDate(ForeignDate).toString()));

      bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nmny"), bm), ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "noriginalcurmny"));

      ForeignVO.setNnetprice(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nnetprice"), bm), ARATE, new UFDate(ForeignDate).toString()));

      bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "nnetprice"), bm), ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "noriginalcurnetprice"));

      ForeignVO.setNtaxnetprice(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "ntaxnetprice"), bm), ARATE, new UFDate(ForeignDate).toString()));

      bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "ntaxnetprice"), bm), ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "noriginalcurtaxnetprice"));

      ForeignVO.setNtaxmmny(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "ntaxmny"), bm), ARATE, new UFDate(ForeignDate).toString()));

      bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), value(row, key(billtype, "ntaxmny"), bm), ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "noriginalcurtaxmny"));

      UFDouble nqttaxprc = value(row, key(billtype, "norgqttaxprc"), bm);
      if (nqttaxprc == null)
        bm.setValueAt(null, row, key(billtype, "norgqttaxprc"));
      else {
        bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), nqttaxprc, ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "norgqttaxprc"));
      }

      UFDouble nnetprice = value(row, key(billtype, "norgqtnetprc"), bm);
      if (nnetprice == null)
        bm.setValueAt(null, row, key(billtype, "norgqtnetprc"));
      else {
        bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), nnetprice, ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "norgqtnetprc"));
      }

      UFDouble nqtprc = value(row, key(billtype, "norgqtprc"), bm);
      if (nqtprc == null)
        bm.setValueAt(null, row, key(billtype, "norgqtprc"));
      else {
        bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), nqtprc, ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "norgqtprc"));
      }

      UFDouble ntaxnetprice = value(row, key(billtype, "norgqttaxnetprc"), bm);

      if (ntaxnetprice == null)
        bm.setValueAt(null, row, key(billtype, "norgqttaxnetprc"));
      else {
        bm.setValueAt(bcurr.getOriginAmountByOpp(ForeignID, bcurr.getLocalCurrPK(), ntaxnetprice, ARATE, new UFDate(ForeignDate).toString()), row, key(billtype, "norgqttaxnetprc"));
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }

    calcForeign(pk_corp, strbillType, ForeignVO, ForeignID, NRATE, ARATE, ForeignDate, BD505, bm, row, billtype);

    SCMEnv.out("d");
  }

  public static void calcEditFunFor33dw(String billdate, Integer BD505, boolean isTax, String isEditProject, int row, BillListPanel panel)
  {
    if (panel == null) {
      return;
    }
    calcEditFun(isTax, null, isEditProject, 0, row, panel.getHeadBillModel(), panel.getBillType());

    calcForeigndw(panel.getCorp(), panel.getBillType(), billdate, BD505, panel.getHeadBillModel(), row, panel.getBillType());
  }

  public static class BillModelVOS extends BillModel
  {
    public CircularlyAccessibleValueObject[] vos = null;

    public BillModelVOS()
    {
    }

    public BillModelVOS(CircularlyAccessibleValueObject[] avos)
    {
      this.vos = avos;
    }

    public Object getValueAt(int rowIndex, String strKey) {
      return this.vos[rowIndex].getAttributeValue(strKey);
    }

    public void setValueAt(Object aValue, int row, String strKey) {
      if ((aValue == null) || (aValue.getClass() == UFDouble.class)) {
        this.vos[row].setAttributeValue(strKey, aValue);
      }
      else if (aValue.getClass() == Double.class) {
        this.vos[row].setAttributeValue(strKey, new UFDouble(aValue.toString().trim()));
      }
      else
        this.vos[row].setAttributeValue(strKey, aValue);
    }

    public void setVos(CircularlyAccessibleValueObject[] avos)
    {
      this.vos = avos;
    }

    public CircularlyAccessibleValueObject[] getVos() {
      return this.vos;
    }

    public int getRowCount() {
      if (this.vos == null)
        return 0;
      return this.vos.length;
    }
  }
}