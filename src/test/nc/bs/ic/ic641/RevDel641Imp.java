package nc.bs.ic.ic641;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.PriceDMO;
import nc.bs.ic.pub.monthsum.MonthExecImp;
import nc.bs.ic.pub.monthsum.MonthQuery;
import nc.bs.ic.pub.monthsum.QryConditionCtrl;
import nc.bs.ic.pub.monthsum.QryZone;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ia.service.ic.IIAParameterForIC;
import nc.vo.ic.ic641.QueryCondCtl;
import nc.vo.ic.ic641.RcvDlvOnhandItemVO;
import nc.vo.ic.ic641.RdsumVO;
import nc.vo.ic.pub.bill.DynamicJoinSql;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.monthsum.MonthMode;
import nc.vo.ic.pub.monthsum.MonthVO;
import nc.vo.ic.pub.monthsum.QryCondStr;
import nc.vo.ic.pub.monthsum.SqlHelper;
import nc.vo.ic.pub.monthsum.SqlMonthSum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.datatype.DataTypeConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.sort.SortMethod;

public class RevDel641Imp extends DataManageObject
{
  private static String[] sa = { "ntermbeginnum", "ntermbeginastnum", "ntermbegingrossnum", "nterminnum", "nterminastnum", "ntermingrossnum", "ntermoutnum", "ntermoutastnum", "ntermoutgrossnum" };

  private static String[] saBenQi = { "nterminnum", "nterminastnum", "ntermingrossnum", "ntermoutnum", "ntermoutastnum", "ntermoutgrossnum" };

  private double[] doubleValue = new double[sa.length];

  private static String[] samny = { "ntermbeginplanmny", "ntermbegincankaomny", "ntermbeginmny", "nterminplanmny", "ntermincankaomny", "nterminmny", "ntermoutplanmny", "ntermoutcankaomny", "ntermoutmny" };

  private double[] doubleValueMny = new double[samny.length];

  public final String JOIN_INV = " inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n";

  public final String JOIN_INVCL = " inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n";

  public String[] m_aryOrderby = null;

  public char m_cType = '1';

  public UFDate m_dtEndDate = null;

  public UFDate m_dtStartDate = null;

  public int m_iInvClassLevel = 1;
  public String m_INNUM;
  public String m_INASSISTNUM;
  public String m_OUTNUM;
  public String m_OUTASSISTNUM;
  public String m_sGroupFields2Varry = null;

  public String m_sGroupFields1Varry = null;

  public String m_sHidwarehtransferWhereStr = null;

  public String m_sOrderby = null;

  public String m_sSelectFields1Varry = null;

  public String m_sSelectFields2Varry = null;

  public String m_sSelectFields3Varry = null;

  public String m_sViewName = "ic_keep_detail4";

  public String m_sVoCondWhereStr = null;

  public String m_sWarehouselevel = null;

  public String m_sWhlevelAssisant = null;

  public final String PERIODBEGIN = " ( cbilltypecode='40' or cbilltypecode='41' or cbilltypecode='44') ";
  public static String PERIODBEGIN_IN = " and ((cbilltypecode = '40' OR cbilltypecode = '41' OR  cbilltypecode = '44') AND ninnum IS NOT NULL)";

  public final String PERIODBEGINNOT = " ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ";
  public final String PERIODINOUT = " and ((( cbilltypecode = '4E' OR  cbilltypecode = '45' OR  cbilltypecode = '46' OR  cbilltypecode = '47' OR  cbilltypecode = '48' OR  cbilltypecode = '49' OR  cbilltypecode = '4A' OR  cbilltypecode = '4B' )AND ninnum IS NOT NULL) OR   ((cbilltypecode = '4C' OR   cbilltypecode = '4D' OR   cbilltypecode = '4Y' OR   cbilltypecode = '4F' OR   cbilltypecode = '4G' OR   cbilltypecode = '4H' OR   cbilltypecode = '4I' OR   cbilltypecode = '4J' OR  cbilltypecode = '4O'     ) AND noutnum IS NOT NULL) or cbilltypecode = '4Q' )";

  public Timer m_timer = new Timer();

  QueryCondCtl m_queryParam = null;

  public String sUnionSQL = "";

  public String sJoinRdType = "";

  public String sSelectFields3 = "";

  public String sGroupFields2 = "";

  public String sGroupFields1 = "";

  public String sSelectFields1 = "";

  public String sSelectFields2 = "";

  public String sOrderFields = "";

  private static String CORP_GROUP = "pk_corp";

  private static String CALBODY_GROUP = "pk_calbody";

  private static String WAREHOUSE_GROUP = "cwarehouseid";

  private static String CSPACE_GROUP = "cspaceid";

  private static String CINVMANID_GROUP = "cinventoryid";

  private static String CASTUNIT_GROUP = "castunitid";

  private static String VBATCHCODE_GROUP = "vbatchcode";

  private static String[] FREEITEM_GROUP = { "free1", "free2", "free3", "free4", "free5" };

  private static String CVENDORID_GROUP = "cvendorid";

  Map m_hmParamCtn = new HashMap();

  private ArrayList m_alDateWhere = new ArrayList();

  public RevDel641Imp()
    throws NamingException, SystemException
  {
  }

  public RevDel641Imp(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private HashMap getMapParam(String[] saWh, String[] saCal, String[] saCorp, String[] saBatch, String[] saInv, int iChoice)
  {
    HashMap map = new HashMap();

    if (iChoice == 0) {
      for (int i = 0; i < saWh.length; i++) {
        if (saWh[i] == null)
          continue;
        if (!map.containsKey(saWh[i])) {
          ArrayList al1 = new ArrayList();
          al1.add(0, saCal[i]);
          al1.add(1, saCorp[i]);
          ArrayList alBatch1 = new ArrayList();
          alBatch1.add(saBatch[i]);
          ArrayList alInvID1 = new ArrayList();
          alInvID1.add(saInv[i]);
          al1.add(2, alBatch1);
          al1.add(3, alInvID1);
          map.put(saWh[i], al1);
        } else {
          ArrayList al2 = (ArrayList)map.get(saWh[i]);
          ArrayList alBatch2 = (ArrayList)al2.get(2);
          ArrayList alInvID2 = (ArrayList)al2.get(3);
          alBatch2.add(saBatch[i]);
          alInvID2.add(saInv[i]);
        }
      }

    }
    else if (iChoice == 1) {
      for (int i = 0; i < saCal.length; i++) {
        if (saCal[i] == null)
          continue;
        if (!map.containsKey(saCal[i])) {
          ArrayList al1 = new ArrayList();
          al1.add(0, saCal[i]);
          al1.add(1, saCorp[i]);
          ArrayList alBatch1 = new ArrayList();
          alBatch1.add(saBatch[i]);
          ArrayList alInvID1 = new ArrayList();
          alInvID1.add(saInv[i]);
          al1.add(2, alBatch1);
          al1.add(3, alInvID1);
          map.put(saCal[i], al1);
        } else {
          ArrayList al2 = (ArrayList)map.get(saCal[i]);
          ArrayList alBatch2 = (ArrayList)al2.get(2);
          ArrayList alInvID2 = (ArrayList)al2.get(3);
          alBatch2.add(saBatch[i]);
          alInvID2.add(saInv[i]);
        }
      }
    }
    else
    {
      for (int i = 0; i < saCorp.length; i++) {
        if (saCorp[i] == null)
          continue;
        if (!map.containsKey(saCorp[i])) {
          ArrayList al1 = new ArrayList();
          al1.add(0, null);
          al1.add(1, saCorp[i]);
          ArrayList alBatch1 = new ArrayList();
          alBatch1.add(saBatch[i]);
          ArrayList alInvID1 = new ArrayList();
          alInvID1.add(saInv[i]);
          al1.add(2, alBatch1);
          al1.add(3, alInvID1);
          map.put(saCorp[i], al1);
        } else {
          ArrayList al2 = (ArrayList)map.get(saCorp[i]);
          ArrayList alBatch2 = (ArrayList)al2.get(2);
          ArrayList alInvID2 = (ArrayList)al2.get(3);
          alBatch2.add(saBatch[i]);
          alInvID2.add(saInv[i]);
        }
      }
    }
    return map;
  }

  private ArrayList getLineWhCalCorpInfo(RcvDlvOnhandItemVO[] voaItem, boolean bExtBatch)
  {
    if ((voaItem == null) || (voaItem.length <= 0))
      return null;
    if (voaItem[0] == null) {
      return null;
    }
    int len = voaItem.length;
    String[] saWhs = null;
    String[] saCals = null;
    String[] saCorps = null;
    String[] saBatch = new String[len];
    String[] saInv = new String[len];

    if (voaItem[0].getCwarehouseid() != null) {
      saWhs = new String[len];
      saCals = new String[len];
      saCorps = new String[len];
    } else if (voaItem[0].getCalbodyid() != null) {
      saWhs = null;
      saCals = new String[len];
      saCorps = new String[len];
    } else {
      saWhs = null;
      saCals = null;
      saCorps = new String[len];
    }

    ArrayList al = new ArrayList();
    for (int i = 0; i < voaItem.length; i++) {
      if (saWhs != null)
        saWhs[i] = voaItem[i].getCwarehouseid();
      if (saCals != null)
        saCals[i] = voaItem[i].getCalbodyid();
      if (saCorps != null) {
        saCorps[i] = voaItem[i].getPk_corp();
      }
      saBatch[i] = ((String)voaItem[i].getAttributeValue("vbatchcode"));
      saInv[i] = voaItem[i].getCinventoryid();
    }
    al.add(saWhs);
    al.add(saCals);
    al.add(saCorps);
    al.add(saBatch);
    al.add(saInv);
    return al;
  }

  private static Hashtable replaceKey(Hashtable ht, String keyExt)
  {
    if ((ht == null) || (keyExt == null)) {
      return ht;
    }
    Hashtable htRet = new Hashtable();
    Set setKeyValue = ht.entrySet();
    Iterator iter = setKeyValue.iterator();
    while (iter.hasNext()) {
      Map.Entry enty = (Map.Entry)iter.next();
      Object obj = enty.getKey();
      String newKey = obj == null ? "" : obj.toString();
      newKey = newKey + keyExt;
      htRet.put(newKey, enty.getValue());
    }
    return htRet;
  }

  private static Hashtable qryPrice(Map map, int iChoice, PriceDMO dmoPrice, int type)
    throws Exception
  {
    Set setKeyValue = map.entrySet();
    Iterator iter = setKeyValue.iterator();
    ArrayList alPriceEach = new ArrayList();

    Hashtable ht1 = new Hashtable();
    while (iter.hasNext()) {
      Map.Entry enty = (Map.Entry)iter.next();
      String warehouseid = null;
      ArrayList alValue = (ArrayList)enty.getValue();
      String calbodyid = (String)alValue.get(0);
      String corpid = (String)alValue.get(1);

      String keyExt = "";
      if (iChoice == 0) {
        warehouseid = (String)enty.getKey();
        keyExt = warehouseid == null ? "" : warehouseid;
      }
      if (iChoice == 1) {
        calbodyid = (String)enty.getKey();
        keyExt = calbodyid == null ? "" : calbodyid;
      }
      if (iChoice == 2) {
        corpid = (String)enty.getKey();
        keyExt = corpid == null ? "" : corpid;
      }

      ArrayList alInvIDEach = (ArrayList)alValue.get(3);
      ArrayList alBatchEach = (ArrayList)alValue.get(2);

      if (type != 1)
      {
        if (type == 2) {
          ht1 = dmoPrice.getPlanPriceByStep(corpid, calbodyid, warehouseid, alInvIDEach);
        }
        else {
          ht1 = dmoPrice.getCostPriceByStep(corpid, calbodyid, warehouseid, alInvIDEach);
        }
      }
      ht1 = replaceKey(ht1, keyExt);

      alPriceEach.add(ht1);
    }

    Hashtable htPrice = new Hashtable();
    for (int g = 0; g < alPriceEach.size(); g++) {
      Hashtable ht = (Hashtable)alPriceEach.get(g);
      if (ht == null)
        continue;
      Set set1 = ht.entrySet();
      Iterator iter1 = set1.iterator();
      while (iter1.hasNext()) {
        Map.Entry enty1 = (Map.Entry)iter1.next();
        htPrice.put(enty1.getKey(), enty1.getValue());
      }
    }

    return htPrice;
  }

  private void getSetCanKaoPrice(RcvDlvOnhandItemVO[] voaItem)
  {
  }

  public void caculateMnyAll(RcvDlvOnhandItemVO[] voaItem, boolean bExtBatch)
    throws Exception
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      SCMEnv.out("@@@scan wh inv -- param null.");
      return;
    }

    ArrayList al = getLineWhCalCorpInfo(voaItem, bExtBatch);
    if (al == null)
      return;
    String[] saWh = (String[])(String[])al.get(0);
    String[] saCal = (String[])(String[])al.get(1);
    String[] saCorp = (String[])(String[])al.get(2);
    String[] saBatch = (String[])(String[])al.get(3);
    String[] saInv = (String[])(String[])al.get(4);

    int iChoice = 0;
    if ((saWh != null) && (saWh[0] != null))
      iChoice = 0;
    else if ((saCal != null) && (saCal[0] != null))
      iChoice = 1;
    else if ((saCorp != null) && (saCorp[0] != null)) {
      iChoice = 2;
    }
    PriceDMO dmoPrice = new PriceDMO();

    HashMap map = getMapParam(saWh, saCal, saCorp, saBatch, saInv, iChoice);
    if ((map == null) || (map.size() <= 0)) {
      return;
    }

    Hashtable htPrice_Balance = null;
    Hashtable htPrice_CanKao = null;
    Hashtable htPrice_Plan = null;

    if (this.m_queryParam.isMnyShow()) {
      htPrice_Balance = qryPrice(map, iChoice, dmoPrice, 1);
      setPriceForItems(voaItem, htPrice_Balance, "nprice", iChoice);
    }
    if (this.m_queryParam.isJHJEShow()) {
      htPrice_Plan = qryPrice(map, iChoice, dmoPrice, 2);
      setPriceForItems(voaItem, htPrice_Plan, "nplannedprice", iChoice);
    }
    if (this.m_queryParam.isCanKaoMnyShow()) {
      htPrice_CanKao = qryPrice(map, iChoice, dmoPrice, 3);
      setPriceForItems(voaItem, htPrice_CanKao, "ncankaoprice", iChoice);
    }
  }

  private static void setPriceForItems(CircularlyAccessibleValueObject[] voa, Hashtable htPrice, String priceName, int iChoice)
  {
    if ((htPrice == null) || (htPrice.size() <= 0)) {
      return;
    }
    for (int row = 0; row < voa.length; row++) {
      if (voa[row] == null)
        continue;
      String sInvID = (String)voa[row].getAttributeValue("cinventoryid");
      String warehouseid = (String)voa[row].getAttributeValue("cwarehouseid");

      String corpid = (String)voa[row].getAttributeValue("pk_corp");
      String calbodyid = (String)voa[row].getAttributeValue("ccalbodyid");
      String key = sInvID;
      if (iChoice == 0) {
        key = key + (warehouseid == null ? "" : warehouseid);
      }
      if (iChoice == 1) {
        key = key + (calbodyid == null ? "" : calbodyid);
      }
      if (iChoice == 2) {
        key = corpid == null ? "" : corpid;
      }
      if ((key == null) || (!htPrice.containsKey(key)))
        continue;
      UFDouble dPrice = (UFDouble)htPrice.get(key);
      if (dPrice != null)
        voa[row].setAttributeValue(priceName, dPrice);
      else
        voa[row].setAttributeValue(priceName, dPrice);
    }
  }

  protected RcvDlvOnhandItemVO[] filterUselessUOM(RcvDlvOnhandItemVO[] voaItem)
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      return null;
    }
    for (int item = 0; item < voaItem.length; item++)
    {
      if ((voaItem[item].getCastunitid() != null) && (voaItem[item].getCastunitid().trim().length() != 0)) {
        continue;
      }
      voaItem[item].setHsl(null);
    }

    return voaItem;
  }

  private void setSelectField()
  {
    if (((this.m_queryParam.isMnyShow()) || (this.m_queryParam.isJHJEShow())) && (this.m_cType != '0'))
    {
      this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,ccalbodyid,cwarehouseid ", ",cproviderid");

      this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp, ccalbodyid, cwarehouseid ", ",cproviderid");

      this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp, kp.ccalbodyid,kp.cwarehouseid ", ",kp.cproviderid");

      this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid ", ",cproviderid");

      this.m_sGroupFields2Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid ", ",cproviderid");

      if (("仓库".equalsIgnoreCase(this.m_sWarehouselevel)) && ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)))
      {
        this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,ccalbodyid,cwarehouseid , cspaceid ", ",cproviderid");

        this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp, ccalbodyid,cwarehouseid , cspaceid ", ",cproviderid");

        this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp, kp.ccalbodyid,kp.cwarehouseid,cspaceid ", ",kp.cproviderid");

        this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid,cspaceid ", ",cproviderid");

        this.m_sGroupFields2Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid,cspaceid ", ",cproviderid");

        this.m_sViewName = "ic_keep_detail6";
        this.m_INNUM = "ninspacenum";
        this.m_INASSISTNUM = "ninspaceassistnum";
        this.m_OUTNUM = "noutspacenum";
        this.m_OUTASSISTNUM = "noutspaceassistnum";
      }
      return;
    }

    this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,cwarehouseid", ",cproviderid");

    this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,cwarehouseid", ",cproviderid");

    this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.cwarehouseid", "kp.cproviderid");

    this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,cwarehouseid", ",cproviderid");

    this.m_sGroupFields2Varry = getIfAddProviderField("pk_corp,cwarehouseid", ",cproviderid");

    if ("公司".equalsIgnoreCase(this.m_sWarehouselevel)) {
      this.m_sSelectFields1Varry = getIfAddProviderField(" rdosum.pk_corp ", ",cproviderid");

      this.m_sSelectFields2Varry = getIfAddProviderField(" pk_corp", ",cproviderid");

      this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp", ",kp.cproviderid");

      this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp", ",cproviderid");

      this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp", ",cproviderid");

      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,ccalbodyid", ",cproviderid");

        this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,ccalbodyid", ",cproviderid");

        this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid", ",kp.cproviderid");

        this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,ccalbodyid", ",cproviderid");

        this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid", ",kp.cproviderid");
      }

    }
    else if ("库存组织".equalsIgnoreCase(this.m_sWarehouselevel))
    {
      this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,ccalbodyid", ",cproviderid");

      this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,ccalbodyid", ",cproviderid");

      this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid", ",kp.cproviderid");

      this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,ccalbodyid", ",cproviderid");

      this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid", ",kp.cproviderid");

      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant))
      {
        this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,ccalbodyid,cwarehouseid", ",cproviderid");

        this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid", ",cproviderid");

        this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid,kp.cwarehouseid", ",kp.cproviderid");

        this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,ccalbodyid,cwarehouseid", ",cproviderid");

        this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp,kp.ccalbodyid,kp.cwarehouseid", ",kp.cproviderid");
      }

    }
    else if ("仓库".equalsIgnoreCase(this.m_sWarehouselevel))
    {
      this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,cwarehouseid", ",cproviderid");

      this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,cwarehouseid", ",cproviderid");

      this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.cwarehouseid", ",kp.cproviderid");

      this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,cwarehouseid", ",cproviderid");

      this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp,kp.cwarehouseid", ",kp.cproviderid");

      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        this.m_sViewName = "ic_keep_detail6";
        this.m_INNUM = "ninspacenum";
        this.m_INASSISTNUM = "ninspaceassistnum";
        this.m_OUTNUM = "noutspacenum";
        this.m_OUTASSISTNUM = "noutspaceassistnum";

        this.m_sSelectFields1Varry = getIfAddProviderField("rdosum.pk_corp,cwarehouseid,cspaceid", ",cproviderid");

        this.m_sSelectFields2Varry = getIfAddProviderField("pk_corp,cwarehouseid,cspaceid", ",cproviderid");

        this.m_sSelectFields3Varry = getIfAddProviderField("kp.pk_corp,kp.cwarehouseid,kp.cspaceid", ",kp.cproviderid");

        this.m_sGroupFields1Varry = getIfAddProviderField("pk_corp,cwarehouseid,cspaceid", ",cproviderid");

        this.m_sGroupFields2Varry = getIfAddProviderField("kp.pk_corp,kp.cwarehouseid,kp.cspaceid", ",kp.cproviderid");
      }
    }
  }

  private String getSql_invcl_qichu(int qry_stat)
    throws BusinessException
  {
    String sql_qichu_skele = "select 收发类别,普通汇总,数量字段 from (select 收发X,字段X,数量汇总X from 月结表 关联收发 关联表SQL,(存货分类SQL) okinvcl where 公司条件 and 单据状态条件 and 日期条件 and 分类汇总级次条件 and 其他查询条件 and 是否屏蔽收发列转库 存量单据类型条件 group by RD 分组条件) invsum  ";

    String sDateWhere = null;
    if (qry_stat == 0)
      sDateWhere = (String)this.m_alDateWhere.get(2);
    else if (qry_stat == 1)
      sDateWhere = (String)this.m_alDateWhere.get(1);
    else {
      sDateWhere = (String)this.m_alDateWhere.get(0);
    }

    if (sDateWhere == null) {
      return null;
    }
    Map m = new HashMap();
    if (this.m_queryParam.isSplitRd())
      m.put("收发类别", " cdispatcherid,9 AS rdflag");
    else {
      m.put("收发类别", "");
    }

    String selectStr1 = null;
    String selectStr2 = null;
    String selectStr3 = null;
    if ((qry_stat == 0) || (qry_stat == 1)) {
      selectStr1 = this.m_sSelectFields2Varry;
      selectStr2 = SqlHelper.replace(this.m_sSelectFields3Varry, "cproviderid", "cvendorid as cproviderid ");

      selectStr2 = SqlHelper.replace(selectStr2, "ccalbodyid", "pk_calbody as ccalbodyid");

      selectStr3 = SqlHelper.replace(this.m_sGroupFields2Varry, "cproviderid", "cvendorid ");

      selectStr3 = SqlHelper.replace(selectStr3, "ccalbodyid", "pk_calbody");
    }
    else
    {
      selectStr1 = this.m_sSelectFields2Varry;
      selectStr2 = this.m_sSelectFields3Varry;
      selectStr3 = this.m_sGroupFields2Varry;
    }

    m.put("普通汇总", selectStr1 + ",invclasscode ,invclassname,avgprice");

    m.put("数量字段", getNumSqlFld(0));

    if (this.m_queryParam.isSplitRd())
      m.put("收发X", "cdispatcherid, ");
    else {
      m.put("收发X", "");
    }
    m.put("字段X", selectStr2 + ",okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice");

    if (qry_stat == 0) {
      m.put("数量汇总X", "isnull(sum(nonhandnum),0.0) ntermbeginnum, isnull(sum(nonhandassistnum),0.0) ntermbeginastnum, isnull(sum(ngrossnum),0.0) ntermbegingrossnum");
    }
    else if (qry_stat == 1) {
      m.put("数量汇总X", "isnull(sum(ninnum),0.0)-isnull(sum(noutnum),0.0) ntermbeginnum,isnull(sum(ninassistnum),0.0)-isnull(sum(noutassistnum),0.0) ntermbeginastnum,isnull(sum(ningrossnum),0.0)-isnull(sum(noutgrossnum),0.0) ntermbegingrossnum");
    }
    else
    {
      m.put("数量汇总X", "isnull(sum(" + this.m_INNUM + "),0.0)-isnull(sum(" + this.m_OUTNUM + "),0.0) ntermbeginnum," + "isnull(sum(" + this.m_INASSISTNUM + "),0.0)-isnull(sum(" + this.m_OUTASSISTNUM + "),0.0) ntermbeginastnum," + "isnull(sum(ningrossnum),0.0)-isnull(sum(noutgrossnum),0.0) ntermbegingrossnum");
    }

    if (qry_stat == 0)
    {
      if ((this.m_queryParam.getParam("日期选择") != null) && (this.m_queryParam.getParam("日期选择").equals("dbizdate")))
        m.put("月结表", "ic_month_hand kp ");
      else
        m.put("月结表", "ic_month_handsign kp");
    }
    else if (qry_stat == 1)
    {
      if ((this.m_queryParam.getParam("日期选择") != null) && (this.m_queryParam.getParam("日期选择").equals("dbizdate")))
        m.put("月结表", "ic_month_record kp");
      else
        m.put("月结表", "ic_month_recordsign kp");
    }
    else {
      m.put("月结表", this.m_sViewName + " kp");
    }
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue()))
      m.put("关联收发", this.sJoinRdType);
    else {
      m.put("关联收发", "");
    }
    m.put("关联表SQL", this.sUnionSQL + " inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n" + " inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n");

    String corpWhere = SqlHelper.replace(this.m_queryParam.getCorpWhere("kp"), "kp.pk_corp", "pk_corp");

    String sql_invcl = "SELECT invclasscode,invclassname,avgprice,len(invclasscode) as ilevellength  FROM bd_invcl  WHERE (1=1) and (" + corpWhere + " or pk_corp='0001') and invclasslev=" + this.m_iInvClassLevel;

    m.put("存货分类SQL", sql_invcl);

    m.put("公司条件", this.m_queryParam.getCorpWhere("kp"));
    if ((this.m_queryParam.getParam("日期选择") != null) && (this.m_queryParam.getParam("日期选择").equals("dbizdate")))
      m.put("单据状态条件", "");
    else
      m.put("单据状态条件", this.m_queryParam.getSignBillOnlyWhere("kp"));
    m.put("日期条件", sDateWhere);

    m.put("分类汇总级次条件", " invclasslev >=" + this.m_iInvClassLevel + " and substring(invcl.invclasscode,1,ilevellength)=okinvcl.invclasscode");

    String sVoCondWhere = SqlHelper.replace(this.m_sVoCondWhereStr, "wh1.pk_stordoc", "kp.cwarehouseid");

    if (qry_stat != 2)
    {
      sVoCondWhere = SqlHelper.replace(sVoCondWhere, "ccalbodyid", "pk_calbody");
      sVoCondWhere = SqlHelper.replace(sVoCondWhere, "cproviderid", "cvendorid");
    }

    m.put("其他查询条件", sVoCondWhere);
    m.put("是否屏蔽收发列转库", this.m_sHidwarehtransferWhereStr);

    boolean isIncludeInit = false;
    if (qry_stat == 2)
    {
      if ((this.m_alDateWhere.get(1) != null) || (this.m_alDateWhere.get(2) != null))
        isIncludeInit = true;
    }
    if ((qry_stat == 2) && (isIncludeInit == true))
    {
      m.put("存量单据类型条件", " and  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");
    }
    if ((qry_stat == 2) && (!isIncludeInit))
    {
      m.put("存量单据类型条件", "");
    }
    if (qry_stat == 1)
    {
      m.put("存量单据类型条件", SqlMonthSum.where_xcl_billtype_rec);
    }
    if (qry_stat == 0)
    {
      m.put("存量单据类型条件", "");
    }

    if (this.m_queryParam.isSplitRd())
      m.put("RD", "cdispatcherid,");
    else {
      m.put("RD", "");
    }
    m.put("分组条件", selectStr3 + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice");

    String s = SqlHelper.replace(sql_qichu_skele, m);
    s = SqlHelper.deal_and(s);
    return SqlHelper.deal_quote(s);
  }

  private String getNumSqlFld(int iType) {
    String numFld = null;
    if (iType == 0) {
      numFld = "ntermbeginnum,ntermbeginastnum,ntermbegingrossnum,0.0 AS nterminnum,0.0 AS nterminastnum, 0.0 as ntermingrossnum,0.0 AS ntermoutnum,0.0 AS ntermoutastnum,0.0 as ntermoutgrossnum";
    }
    else if (iType == 1)
    {
      numFld = "0.0 AS ntermbeginnum,0.0 AS ntermbeginastnum,0.0 ntermbegingrossnum,nterminnum,nterminastnum,ntermingrossnum,0.0 AS ntermoutnum,0.0 AS ntermoutastnum,0.0 as ntermoutgrossnum";
    } else if (iType == 2) {
      numFld = "0.0 AS ntermbeginnum,0.0 AS ntermbeginastnum,0.0 ntermbegingrossnum,0.0 nterminnum,0.0 nterminastnum,0.0 ntermingrossnum, ntermoutnum, ntermoutastnum, ntermoutgrossnum";
    }

    return numFld;
  }

  private String getNumSqlFld2(int iType) {
    String numFld = null;
    if (iType != 0)
    {
      if (iType == 1)
      {
        numFld = "sum(COALESCE(" + this.m_INNUM + ",0.0)) nterminnum, sum(COALESCE(" + this.m_INASSISTNUM + ",0.0)) nterminastnum, sum(ningrossnum) ntermingrossnum, 0.0 ntermoutnum , 0.0 ntermoutastnum, 0.0 ntermoutgrossnum ";
      }
      else if (iType == 2) {
        numFld = " 0.0 nterminnum, 0.0 nterminastnum, 0.0 ntermingrossnum, sum(COALESCE(" + this.m_OUTNUM + ",0.0)) ntermoutnum , sum(COALESCE(" + this.m_OUTASSISTNUM + ",0.0)) ntermoutastnum, sum(noutgrossnum) ntermoutgrossnum ";
      }

    }

    return numFld;
  }

  private ArrayList<RcvDlvOnhandItemVO> getInvClass_QiChu(QryConditionVO voCond)
    throws BusinessException
  {
    this.sJoinRdType = " LEFT OUTER JOIN bd_rdcl rdcl ON kp.cdispatcherid = rdcl.pk_rdcl ";

    ConditionVO[] conditionVO = (ConditionVO[])(ConditionVO[])voCond.getParam(10);

    Hashtable htbNotInclude = new Hashtable();
    htbNotInclude.put("inv", "inv");
    htbNotInclude.put("invcl", "invcl");
    DynamicJoinSql dynamic = new DynamicJoinSql("kp", "kp");
    this.sUnionSQL = dynamic.getUnionSQL(conditionVO, htbNotInclude);

    String sqlInvClassQiChu = null;
    try
    {
      sqlInvClassQiChu = getSqlInvClass_Begin(voCond);
    }
    catch (Exception ex)
    {
      GenMethod.throwBusiException(ex);
    }
    return execSQL(sqlInvClassQiChu);
  }

  private ArrayList<RcvDlvOnhandItemVO> execSQL(String sql)
    throws BusinessException
  {
    RcvDlvOnhandItemVO voFai = null;
    ArrayList alItem = new ArrayList();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      rs = null;
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      ResultSetMetaData meta = null;
      meta = rs.getMetaData();
      GenMethod gm = new GenMethod();
      while (rs.next())
      {
        voFai = new RcvDlvOnhandItemVO();
        gm.setData(rs, voFai, meta);
        alItem.add(voFai);
      }
    }
    catch (Exception e)
    {
      GenMethod.throwBusiException(e);
    }
    finally {
      try {
        if (stmt != null)
        {
          stmt.close();
        }
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return alItem;
  }

  private ArrayList<RcvDlvOnhandItemVO> getInvClass_BenQi(QryConditionVO voCond)
    throws BusinessException
  {
    StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd())
      sbSql.append(" cdispatcherid,rdcl.rdname AS cdispatchername,rdosum.rdflag,\n");
    sbSql.append(" " + this.m_sSelectFields1Varry + ",invclasscode ,invclassname, ntermbeginnum,ntermbeginastnum,ntermbegingrossnum, avgprice as nprice, \n");

    sbSql.append(" nterminnum,nterminastnum,ntermingrossnum,ntermoutnum,ntermoutastnum, ntermoutgrossnum, ");

    sbSql.append(" (ntermbeginnum+nterminnum-ntermoutnum) ntermonhandnum,");

    sbSql.append(" (ntermbeginastnum+nterminastnum-ntermoutastnum) ntermonhandastnum,(ntermbegingrossnum+ntermingrossnum-ntermoutgrossnum) ntermonhandgrossnum,");

    sbSql.append(" COALESCE(avgprice,0.0)* ntermbeginnum AS ntermbeginmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* nterminnum AS nterminmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* ntermoutnum AS ntermoutmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* (ntermbeginnum+nterminnum-ntermoutnum) AS ntermonhandmny \n");

    sbSql.append(" FROM ( \n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid, rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + ",invclasscode ,invclassname,avgprice," + "SUM(ntermbeginnum) AS ntermbeginnum, \n");

    sbSql.append("SUM(ntermbeginastnum) AS ntermbeginastnum,SUM(ntermbegingrossnum) as ntermbegingrossnum,SUM(nterminnum) AS nterminnum, \n");

    sbSql.append("SUM(nterminastnum) AS nterminastnum, sum(ntermingrossnum) as ntermingrossnum,SUM(ntermoutnum) AS ntermoutnum, \n");

    sbSql.append(" SUM(ntermoutastnum) AS ntermoutastnum, sum(ntermoutgrossnum) as ntermoutgrossnum \n");

    sbSql.append(" FROM( (\n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,0 AS rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + " ,invclasscode ,invclassname,avgprice,");

    sbSql.append(getNumSqlFld(1));

    sbSql.append(" FROM ( \n");
    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,");
    }
    sbSql.append(this.m_sSelectFields3Varry + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice, \n");

    sbSql.append(getNumSqlFld2(1));

    sbSql.append(" FROM " + this.m_sViewName + "   kp \n");
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue()))
      sbSql.append(this.sJoinRdType);
    sbSql.append(this.sUnionSQL);
    sbSql.append(" inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n");
    sbSql.append(" inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n");

    sbSql.append(" ,(SELECT invclasscode,invclassname,avgprice,len(invclasscode) as ilevellength \n");

    sbSql.append(" FROM bd_invcl WHERE ").append(" invclasslev = ");
    sbSql.append(this.m_iInvClassLevel);
    String[] sCorps = (String[])(String[])this.m_queryParam.getParam("公司条件");
    sbSql.append(formInvClassCorps(sCorps));
    sbSql.append(") okinvcl \n");

    sbSql.append(" WHERE (1=1) ");

    sbSql.append(this.m_queryParam.getCorpWhere("kp"));

    sbSql.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      } else {
        sbSql.append(" AND kp.dbizdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
      else {
        sbSql.append(" AND kp.dbizdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }

    }

    sbSql.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");
    sbSql.append(" AND invclasslev >= ");

    sbSql.append(this.m_iInvClassLevel);
    sbSql.append(" AND substring(invcl.invclasscode,1,ilevellength)=okinvcl.invclasscode  \n");

    sbSql.append(this.m_sVoCondWhereStr);

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(" and  kp.cbilltypecode in ('4E','45','46','47','48','49','4A','4B','4Q') ");
    }

    sbSql.append(this.m_sHidwarehtransferWhereStr);

    sbSql.append("\n GROUP BY ");

    sbSql.append(this.m_sGroupFields2Varry + ",okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice \n");

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" ,cdispatcherid ");
    }
    sbSql.append(" HAVING (SUM(" + this.m_INNUM + ") IS NOT NULL) ");

    sbSql.append(") AS invsum ");

    sbSql.append(" )UNION ALL ( \n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,1 AS rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + " ,invclasscode ,invclassname,avgprice,").append(getNumSqlFld(2));

    sbSql.append(" FROM( \n");
    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) sbSql.append(" cdispatcherid,");
    sbSql.append(this.m_sSelectFields3Varry + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice, \n");

    sbSql.append(getNumSqlFld2(2));
    sbSql.append(" FROM " + this.m_sViewName + " kp \n");
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) sbSql.append(this.sJoinRdType);
    sbSql.append(this.sUnionSQL);
    sbSql.append(" inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n");
    sbSql.append(" inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n");
    sbSql.append(" ,(SELECT invclasscode,invclassname,avgprice,len(invclasscode) as ilevellength \n");
    sbSql.append(" FROM bd_invcl WHERE").append(" invclasslev = ");
    sbSql.append(this.m_iInvClassLevel);

    sbSql.append(formInvClassCorps(sCorps));

    sbSql.append(") okinvcl \n");

    sbSql.append(" where (1=1) ");

    sbSql.append(this.m_queryParam.getCorpWhere("kp"));

    sbSql.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      } else {
        sbSql.append(" AND kp.dbizdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
      else {
        sbSql.append(" AND kp.dbizdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }

    }

    sbSql.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");
    sbSql.append(" AND invclasslev >= ");

    sbSql.append(this.m_iInvClassLevel);
    sbSql.append(" AND substring(invcl.invclasscode,1,ilevellength)=okinvcl.invclasscode \n");

    sbSql.append(this.m_sVoCondWhereStr);

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(" and  kp.cbilltypecode in ('4C','4D','4Y','4F','4G','4H','4I','4J','4O','4Q') ");
    }

    sbSql.append(this.m_sHidwarehtransferWhereStr);
    sbSql.append("\n GROUP BY  ");

    sbSql.append(this.m_sGroupFields2Varry + ",okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice \n");

    if (this.m_queryParam.isSplitRd()) sbSql.append(" ,cdispatcherid ");

    sbSql.append(" HAVING (SUM(" + this.m_OUTNUM + ") IS NOT NULL) ");
    sbSql.append(" ) invsum \n");

    sbSql.append("  ) ) rdosum1 \n");
    sbSql.append(" GROUP BY ");

    sbSql.append(this.m_sGroupFields1Varry + " ,invclasscode ,invclassname,avgprice \n");
    if (this.m_queryParam.isSplitRd()) sbSql.append(" ,cdispatcherid,rdflag ");
    sbSql.append(" )  rdosum \n");

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" LEFT OUTER JOIN   bd_rdcl rdcl ON rdosum.cdispatcherid = rdcl.pk_rdcl  \n");
    }
    sbSql.append(" ORDER BY " + this.m_sSelectFields1Varry + ",invclasscode ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(", cdispatcherid ");
    }
    return execSQL(sbSql.toString());
  }

  protected String getInvClassSQL(QryConditionVO voCond)
    throws BusinessException
  {
    this.sJoinRdType = " LEFT OUTER JOIN bd_rdcl rdcl ON kp.cdispatcherid = rdcl.pk_rdcl ";

    ConditionVO[] conditionVO = (ConditionVO[])(ConditionVO[])voCond.getParam(10);

    Hashtable htbNotInclude = new Hashtable();
    htbNotInclude.put("inv", "inv");
    htbNotInclude.put("invcl", "invcl");
    DynamicJoinSql dynamic = new DynamicJoinSql("kp", "kp");
    this.sUnionSQL = dynamic.getUnionSQL(conditionVO, htbNotInclude);

    StringBuffer sbSql = new StringBuffer();
    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd())
      sbSql.append(" cdispatcherid,rdcl.rdname AS cdispatchername,rdosum.rdflag,\n");
    sbSql.append(" " + this.m_sSelectFields1Varry + ",invclasscode ,invclassname, ntermbeginnum,ntermbeginastnum,ntermbegingrossnum, avgprice as nprice, \n");

    sbSql.append(" nterminnum,nterminastnum,ntermingrossnum,ntermoutnum,ntermoutastnum, ntermoutgrossnum, ");

    sbSql.append(" (ntermbeginnum+nterminnum-ntermoutnum) ntermonhandnum,");

    sbSql.append(" (ntermbeginastnum+nterminastnum-ntermoutastnum) ntermonhandastnum,(ntermbegingrossnum+ntermingrossnum-ntermoutgrossnum) ntermonhandgrossnum,");

    sbSql.append(" COALESCE(avgprice,0.0)* ntermbeginnum AS ntermbeginmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* nterminnum AS nterminmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* ntermoutnum AS ntermoutmny, \n");

    sbSql.append(" COALESCE(avgprice,0.0)* (ntermbeginnum+nterminnum-ntermoutnum) AS ntermonhandmny \n");

    sbSql.append(" FROM ( \n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid, rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + ",invclasscode ,invclassname,avgprice," + "SUM(ntermbeginnum) AS ntermbeginnum, \n");

    sbSql.append("SUM(ntermbeginastnum) AS ntermbeginastnum,SUM(ntermbegingrossnum) as ntermbegingrossnum,SUM(nterminnum) AS nterminnum, \n");

    sbSql.append("SUM(nterminastnum) AS nterminastnum, sum(ntermingrossnum) as ntermingrossnum,SUM(ntermoutnum) AS ntermoutnum, \n");

    sbSql.append(" SUM(ntermoutastnum) AS ntermoutastnum, sum(ntermoutgrossnum) as ntermoutgrossnum \n");

    sbSql.append(" FROM( \n");

    String sqlInvClassQiChu = null;
    try {
      sqlInvClassQiChu = getSqlInvClass_Begin(voCond);
    } catch (Exception ex) {
      SCMEnv.error(ex);
      throw new BusinessException(ex.getMessage());
    }
    sbSql.append(sqlInvClassQiChu);

    sbSql.append(" UNION ALL ( \n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,0 AS rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + " ,invclasscode ,invclassname,avgprice,");

    sbSql.append(getNumSqlFld(1));

    sbSql.append(" FROM ( \n");
    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,");
    }
    sbSql.append(this.m_sSelectFields3Varry + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice, \n");

    sbSql.append(getNumSqlFld2(1));

    sbSql.append(" FROM " + this.m_sViewName + "   kp \n");
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue()))
      sbSql.append(this.sJoinRdType);
    sbSql.append(this.sUnionSQL);
    sbSql.append(" inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n");
    sbSql.append(" inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n");

    sbSql.append(" ,(SELECT invclasscode,invclassname,avgprice,len(invclasscode) as ilevellength \n");

    sbSql.append(" FROM bd_invcl WHERE ").append(" invclasslev = ");
    sbSql.append(this.m_iInvClassLevel);
    String[] sCorps = (String[])(String[])this.m_queryParam.getParam("公司条件");
    sbSql.append(formInvClassCorps(sCorps));
    sbSql.append(") okinvcl \n");

    sbSql.append(" WHERE (1=1) ");

    sbSql.append(this.m_queryParam.getCorpWhere("kp"));

    sbSql.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      } else {
        sbSql.append(" AND kp.dbizdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
      else {
        sbSql.append(" AND kp.dbizdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
    }

    sbSql.append(" AND invclasslev >= ");

    sbSql.append(this.m_iInvClassLevel);
    sbSql.append(" AND substring(invcl.invclasscode,1,ilevellength)=okinvcl.invclasscode  \n");

    sbSql.append(this.m_sVoCondWhereStr);

    sbSql.append(this.m_sHidwarehtransferWhereStr);

    sbSql.append("\n GROUP BY ");
    if (this.m_queryParam.isSplitRd())
      sbSql.append(" cdispatcherid,");
    sbSql.append(this.m_sGroupFields2Varry + ",okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice \n");

    sbSql.append(" HAVING (SUM(" + this.m_INNUM + ") IS NOT NULL) ");

    sbSql.append(") AS invsum ");

    sbSql.append(" )UNION ALL ( \n");

    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,1 AS rdflag,");
    }
    sbSql.append(this.m_sSelectFields2Varry + " ,invclasscode ,invclassname,avgprice,").append(getNumSqlFld(2));

    sbSql.append(" FROM( \n");
    sbSql.append(" SELECT ");
    if (this.m_queryParam.isSplitRd()) sbSql.append(" cdispatcherid,");
    sbSql.append(this.m_sSelectFields3Varry + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice, \n");

    sbSql.append(getNumSqlFld2(2));
    sbSql.append(" FROM " + this.m_sViewName + " kp \n");
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) sbSql.append(this.sJoinRdType);
    sbSql.append(this.sUnionSQL);
    sbSql.append(" inner JOIN  bd_invmandoc invman  ON kp.cinventoryid = invman.pk_invmandoc  inner  JOIN    bd_invbasdoc inv   ON  invman.pk_invbasdoc=inv.pk_invbasdoc  \n");
    sbSql.append(" inner JOIN  bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl  \n");
    sbSql.append(" ,(SELECT invclasscode,invclassname,avgprice,len(invclasscode) as ilevellength \n");
    sbSql.append(" FROM bd_invcl WHERE").append(" invclasslev = ");
    sbSql.append(this.m_iInvClassLevel);

    sbSql.append(formInvClassCorps(sCorps));

    sbSql.append(") okinvcl \n");

    sbSql.append(" where (1=1) ");

    sbSql.append(this.m_queryParam.getCorpWhere("kp"));

    sbSql.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      } else {
        sbSql.append(" AND kp.dbizdate>='");
        sbSql.append(this.m_dtStartDate.toString().trim());
        sbSql.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sbSql.append(" AND kp.daccountdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
      else {
        sbSql.append(" AND kp.dbizdate<='");
        sbSql.append(this.m_dtEndDate.toString().trim());
        sbSql.append("' ");
      }
    }

    sbSql.append(" AND invclasslev >= ");

    sbSql.append(this.m_iInvClassLevel);
    sbSql.append(" AND substring(invcl.invclasscode,1,ilevellength)=okinvcl.invclasscode \n");

    sbSql.append(this.m_sVoCondWhereStr);

    sbSql.append(this.m_sHidwarehtransferWhereStr);
    sbSql.append("\n GROUP BY  ");
    if (this.m_queryParam.isSplitRd()) sbSql.append(" cdispatcherid,");
    sbSql.append(this.m_sGroupFields2Varry + ",okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice \n");

    sbSql.append(" HAVING (SUM(" + this.m_OUTNUM + ") IS NOT NULL) ");
    sbSql.append(" ) invsum \n");

    sbSql.append(" ) ) rdosum1 \n");
    sbSql.append(" GROUP BY ");
    if (this.m_queryParam.isSplitRd()) sbSql.append(" cdispatcherid,rdflag,");
    sbSql.append(this.m_sGroupFields1Varry + " ,invclasscode ,invclassname,avgprice \n");
    sbSql.append(" ) AS rdosum \n");

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" LEFT OUTER JOIN   bd_rdcl rdcl ON rdosum.cdispatcherid = rdcl.pk_rdcl  \n");
    }
    sbSql.append(" ORDER BY " + this.m_sSelectFields1Varry + ",invclasscode ");
    if (this.m_queryParam.isSplitRd())
      sbSql.append(", cdispatcherid ");
    return sbSql.toString();
  }

  private void setInvField()
  {
    this.sSelectFields1 = (this.m_sSelectFields1Varry + ",");
    this.sSelectFields2 = (this.m_sSelectFields2Varry + ",");
    this.sSelectFields3 = (this.m_sSelectFields3Varry + ",");
    this.sGroupFields1 = this.m_sGroupFields1Varry;
    this.sGroupFields2 = this.m_sGroupFields2Varry;
    setSelectFieldInv();
    if ((this.m_queryParam.isMnyShow()) && (this.m_cType != '2') && (this.m_cType != '5'))
    {
      this.sSelectFields1 += " vbatchcode ,";
      this.sSelectFields2 += " vbatchcode ,";
      this.sSelectFields3 += " kp.vbatchcode ,";
      this.sGroupFields1 += " ,vbatchcode ";
      this.sGroupFields2 += " , vbatchcode ";
    }
  }

  private void setInvClassField()
  {
    this.sGroupFields1 = (this.m_sGroupFields1Varry + " ,invclasscode,invclassname,avgprice ");
    if (this.m_queryParam.isSplitRd()) this.sGroupFields1 += ",cdispatcherid,rdflag "; 
  }

  public ArrayList query(QryConditionVO voCond) throws Exception
  {
    this.m_timer.start();

    beforeCallMethod("nc.bs.ic.ic641.RcvDlvOnhandDMO", "query", new Object[] { voCond });

    if (voCond == null)
    {
      SCMEnv.out("cond para null.");
      return null;
    }
    long lTime = System.currentTimeMillis();

    getConditonValue(voCond);

    ArrayList alItem_QiChu = new ArrayList();
    ArrayList alItem_BenQi = new ArrayList();
    
    if (this.m_cType == '0')
    {
      alItem_QiChu = getInvClass_QiChu(voCond);
      alItem_BenQi = getInvClass_BenQi(voCond);
      setInvClassField();
    }
    else
    {
      setInvField();
      alItem_QiChu = getInv_QiChu(voCond);   //期初
      alItem_BenQi = getInv_BenQi(voCond);   
    }

    this.m_timer.showExecuteTime("641改造后查询时间：");
    SCMEnv.showTime(lTime, "业务查询");

    ArrayList alItem = new ArrayList();
    String[] asSelectedGroupFlds = SqlHelper.splitString(this.sGroupFields1, ",");
    this.m_timer.showExecuteTime("开始合并期初和本期：");
    alItem = combineQiChuAndBenQi(asSelectedGroupFlds, alItem_QiChu, alItem_BenQi);
    this.m_timer.showExecuteTime("合并期初和本期：");
    alItem = calculateOnHandItemVO(alItem);
    this.m_timer.showExecuteTime("计算期末数量：");

    RcvDlvOnhandItemVO[] voaRcvDlvOnhandItem = null;
    ArrayList alRet = new ArrayList();
    if (alItem.size() > 0)
    {
      voaRcvDlvOnhandItem = new RcvDlvOnhandItemVO[alItem.size()];
      alItem.toArray(voaRcvDlvOnhandItem);

      if (this.m_cType != '0')
      {
        int iflag = -1;
        if (this.m_queryParam.isMnyShow())
          iflag = 1;
        if (this.m_queryParam.isJHJEShow())
          iflag = 2;
        if ((this.m_queryParam.isJHJEShow()) && (this.m_queryParam.isMnyShow()))
          iflag = 3;
        PriceDMO dmoPrice = new PriceDMO();
        if (iflag != -1) {
          dmoPrice.getBalancePriceAndPlanPrice((IIAParameterForIC[])voaRcvDlvOnhandItem, this.m_dtStartDate.toString(), this.m_dtEndDate.toString(), iflag);
        }

        if (this.m_queryParam.isCanKaoMnyShow())
        {
          try
          {
            dmoPrice.getPlanOrCostPrice(voaRcvDlvOnhandItem, "cinventoryid", "ncankaoprice", false);
          }
          catch (SQLException es)
          {
            GenMethod.throwBusiException(es);
          }
        }

      }

      this.m_timer.showExecuteTime("汇总至查询纬度开始：");
      RcvDlvOnhandItemVO[] voaItemSum = sumNumMny(getGroupFld_V5(), voaRcvDlvOnhandItem);
      this.m_timer.showExecuteTime("汇总至查询纬度：");

      if (!this.m_queryParam.isSplitRd())
      {
        alRet.add(voaItemSum);
      }
      else
      {
        long lTime2 = System.currentTimeMillis();
        alRet = splitRdType(this.m_cType, voaItemSum);
        SCMEnv.showTime(lTime2, "拆分收发类别");
      }

      if (alRet != null) alRet.add(new Boolean(this.m_queryParam.isSplitRd()));

    }

    afterCallMethod("nc.bs.ic.ic641.RcvDlvOnhandDMO", "query", new Object[] { voCond });

    return alRet;
  }

  private ArrayList<RcvDlvOnhandItemVO> calculateOnHandItemVO(ArrayList<RcvDlvOnhandItemVO> voalItem)
  {
    UFDouble D0 = new UFDouble(0.0D);
    for (RcvDlvOnhandItemVO itemVO : voalItem)
    {
      UFDouble dTermOnHandNum = D0;
      dTermOnHandNum.add(itemVO.getNtermbeginnum() == null ? D0 : itemVO.getNtermbeginnum());
      dTermOnHandNum.add(itemVO.getNterminnum() == null ? D0 : itemVO.getNterminnum());
      dTermOnHandNum.sub(itemVO.getNtermoutnum() == null ? D0 : itemVO.getNtermoutnum());
      itemVO.setAttributeValue("ntermonhandnum", dTermOnHandNum);

      UFDouble dTermOnHandAstNum = D0;
      dTermOnHandAstNum.add(itemVO.getNtermbeginnum() == null ? D0 : itemVO.getNtermbeginnum());
      dTermOnHandAstNum.add(itemVO.getNterminnum() == null ? D0 : itemVO.getNterminnum());
      dTermOnHandAstNum.sub(itemVO.getNtermoutnum() == null ? D0 : itemVO.getNtermoutnum());
      itemVO.setAttributeValue("ntermonhandastnum", dTermOnHandAstNum);

      UFDouble dTermOnHandGrossNum = D0;
      dTermOnHandGrossNum.add(itemVO.getNtermbeginnum() == null ? D0 : itemVO.getNtermbeginnum());
      dTermOnHandGrossNum.add(itemVO.getNterminnum() == null ? D0 : itemVO.getNterminnum());
      dTermOnHandGrossNum.sub(itemVO.getNtermoutnum() == null ? D0 : itemVO.getNtermoutnum());
      itemVO.setAttributeValue("ntermonhandgrossnum", dTermOnHandGrossNum);
      if (this.m_cType == '0')
      {
        UFDouble dTermOnHandMny = D0;
        dTermOnHandMny.add(itemVO.getNtermbeginmny() == null ? D0 : itemVO.getNtermbeginmny());
        dTermOnHandMny.add(itemVO.getNterminmny() == null ? D0 : itemVO.getNterminmny());
        dTermOnHandMny.sub(itemVO.getNtermoutmny() == null ? D0 : itemVO.getNtermoutmny());
        itemVO.setAttributeValue("ntermonhandmny", dTermOnHandMny);
      }
    }
    return voalItem;
  }

  public void scanRdType(String[] saGroupField, RcvDlvOnhandItemVO[] voaItem, Hashtable htRdIn, Hashtable htRdOut, ArrayList alRdNameIn, ArrayList alRdNameOut)
  {
    if ((saGroupField == null) || (saGroupField.length == 0) || (voaItem == null) || (voaItem.length == 0) || (htRdIn == null) || (htRdOut == null) || (alRdNameIn == null) || (alRdNameOut == null))
    {
      SCMEnv.out("@@@scan -- param null.");
      return;
    }

    StringBuffer sbGroupString = new StringBuffer();
    Object oTempField = null; Object oTempID = null; Object oTempName = null; Object oTempRd = null;
    String sLastGroup = null;
    int iRdInCount = 0; int iRdOutCount = 0;
    boolean bHaveRdInNullID = false; boolean bHaveRdOutNullID = false;

    for (int row = 0; row < voaItem.length; row++) {
      if (voaItem[row] == null)
        continue;
      sbGroupString = new StringBuffer();
      for (int gf = 0; gf < saGroupField.length; gf++) {
        oTempField = voaItem[row].getAttributeValue(saGroupField[gf]);

        if ((oTempField == null) || (oTempField.toString().trim().length() == 0))
        {
          sbGroupString.append(RcvDlvOnhandItemVO.RD_NULL_ID);
        }
        else sbGroupString.append(oTempField.toString().trim());

      }

      oTempID = voaItem[row].getCdispatcherid();
      oTempName = voaItem[row].getCdispatchername();
      oTempRd = voaItem[row].getRdflag();
      if (row == 0)
        sLastGroup = sbGroupString.toString();
      if (!sbGroupString.toString().equals(sLastGroup))
      {
        if ((bHaveRdOutNullID) && (!htRdOut.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID)))
        {
          htRdOut.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdOutCount));

          alRdNameOut.add(RcvDlvOnhandItemVO.RD_NULL_NAME_D);
          iRdOutCount++;
        } else if ((bHaveRdInNullID) && (!htRdIn.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID)))
        {
          htRdIn.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdInCount));

          alRdNameIn.add(RcvDlvOnhandItemVO.RD_NULL_NAME_R);
          iRdInCount++;
        }

        sLastGroup = sbGroupString.toString();
        bHaveRdInNullID = false;
        bHaveRdOutNullID = false;
      }

      if ((oTempID == null) || (oTempID.toString().trim().length() == 0)) {
        if (oTempRd != null) {
          if (oTempRd.toString().trim().equals("1"))
          {
            bHaveRdOutNullID = true;
          }
          else if (oTempRd.toString().trim().equals("0"))
          {
            bHaveRdInNullID = true;
          }
        }
      }
      else if (oTempRd != null) {
        if (oTempRd.toString().trim().equals("1"))
        {
          if (!htRdOut.containsKey(oTempID.toString().trim())) {
            htRdOut.put(oTempID.toString().trim(), new Integer(iRdOutCount));

            alRdNameOut.add(oTempName);
            iRdOutCount++;
          }
        } else if (oTempRd.toString().trim().equals("0"))
        {
          if (!htRdIn.containsKey(oTempID.toString().trim())) {
            htRdIn.put(oTempID.toString().trim(), new Integer(iRdInCount));

            alRdNameIn.add(oTempName);
            iRdInCount++;
          }

        }

      }

      if ((bHaveRdOutNullID) && (!htRdOut.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID)))
      {
        htRdOut.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdOutCount));

        alRdNameOut.add(RcvDlvOnhandItemVO.RD_NULL_NAME_D);
        iRdOutCount++;
      } else if ((bHaveRdInNullID) && (!htRdIn.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID)))
      {
        htRdIn.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdInCount));

        alRdNameIn.add(RcvDlvOnhandItemVO.RD_NULL_NAME_R);
        iRdInCount++;
      }

      if (voaItem.length == 1)
        if ((bHaveRdOutNullID) && (!htRdOut.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID)))
        {
          htRdOut.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdOutCount));

          alRdNameOut.add(RcvDlvOnhandItemVO.RD_NULL_NAME_D);
          iRdOutCount++; } else {
          if ((!bHaveRdInNullID) || (htRdIn.containsKey(RcvDlvOnhandItemVO.RD_NULL_ID))) {
            continue;
          }
          htRdIn.put(RcvDlvOnhandItemVO.RD_NULL_ID, new Integer(iRdInCount));

          alRdNameIn.add(RcvDlvOnhandItemVO.RD_NULL_NAME_R);
          iRdInCount++;
        }
    }
  }

  protected void showTime(long lStartTime, String sTaskHint)
  {
    long lTime = System.currentTimeMillis() - lStartTime;
    SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为：" + lTime / 60000L + "分" + lTime / 1000L % 60L + "秒" + lTime % 1000L + "毫秒");
  }

  public ArrayList splitRdType(char cGroupType, RcvDlvOnhandItemVO[] voaItem)
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      SCMEnv.out("@@@ split rd no param...");
      return null;
    }

    String[] saGroupField = getGroupFld_V5();
    Hashtable htRdIn = new Hashtable();
    ArrayList alRdNameIn = new ArrayList();

    Hashtable htRdOut = new Hashtable();

    ArrayList alRdNameOut = new ArrayList();

    scanRdType(saGroupField, voaItem, htRdIn, htRdOut, alRdNameIn, alRdNameOut);

    saGroupField = this.m_aryOrderby;

    RcvDlvOnhandItemVO[] voaRetItems = sumRdType(saGroupField, voaItem, htRdIn, htRdOut);

    ArrayList alRet = new ArrayList();
    alRet.add(voaRetItems);
    alRet.add(alRdNameIn);
    alRet.add(alRdNameOut);
    return alRet;
  }

  public RcvDlvOnhandItemVO[] sumRdType(String[] saGroupField, RcvDlvOnhandItemVO[] voaItem, Hashtable htRdIn, Hashtable htRdOut)
  {
    if ((saGroupField == null) || (saGroupField.length == 0) || (voaItem == null) || (voaItem.length == 0) || (htRdOut == null) || (htRdIn == null))
    {
      SCMEnv.out("@@@param null.");
      return null;
    }

    StringBuffer sbGroupString = new StringBuffer();
    Object oTempField = null; Object oTempID = null; Object oTempRd = null;
    String sLastGroup = null;
    int iRdInCount = htRdIn.size(); int iRdOutCount = htRdOut.size();

    RcvDlvOnhandItemVO voRdo = new RcvDlvOnhandItemVO();
    voRdo.initRdTypeSize(iRdInCount, iRdOutCount);
    initDoubleValue();
    Vector vTempItem = new Vector();
    int iIdIndex = -1;

    for (int row = 0; row < voaItem.length; row++) {
      if (voaItem[row] == null) {
        continue;
      }
      sbGroupString = new StringBuffer();
      for (int gf = 0; gf < saGroupField.length; gf++) {
        oTempField = voaItem[row].getAttributeValue(saGroupField[gf]);

        if ((oTempField == null) || (oTempField.toString().trim().length() == 0))
        {
          sbGroupString.append(RcvDlvOnhandItemVO.RD_NULL_ID);
        }
        else sbGroupString.append(oTempField.toString().trim());
      }

      if (row == 0) {
        sLastGroup = sbGroupString.toString();

        voRdo = (RcvDlvOnhandItemVO)voaItem[row].clone();
        voRdo.initRdTypeSize(iRdInCount, iRdOutCount);
      }

      if (!sbGroupString.toString().equals(sLastGroup))
      {
        addUpAndSet(voRdo, this.doubleValue, this.doubleValueMny, sa, samny);

        vTempItem.add(voRdo);

        voRdo = (RcvDlvOnhandItemVO)voaItem[row].clone();
        voRdo.initRdTypeSize(iRdInCount, iRdOutCount);

        initDoubleValue();

        iIdIndex = -1;

        sLastGroup = sbGroupString.toString();
      }

      UFDouble ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbeginnum");

      double d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValue[0] += d;

      ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbeginastnum");

      d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValue[1] += d;

      ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbegingrossnum");

      d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValue[2] += d;

      ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbeginplanmny");

      d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValueMny[0] += d;

      ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbegincankaomny");

      d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValueMny[1] += d;

      ufd = (UFDouble)voaItem[row].getAttributeValue("ntermbeginmny");

      d = ufd == null ? 0.0D : ufd.doubleValue();
      this.doubleValueMny[2] += d;

      RdsumVO voSum = new RdsumVO();

      oTempID = voaItem[row].getCdispatcherid();
      oTempRd = voaItem[row].getRdflag();

      if ((oTempID == null) || (oTempID.toString().trim().length() == 0))
        oTempID = RcvDlvOnhandItemVO.RD_NULL_ID;
      oTempID = oTempID.toString().trim();
      voSum.setRdtypeid(oTempID.toString());

      if ((oTempRd != null) && (oTempRd.toString().toUpperCase().trim().equals("1")))
      {
        String[] saOutNumMny = { "ntermoutnum", "ntermoutastnum", "ntermoutmny", "ntermoutgrossnum", "ntermoutplanmny", "ntermoutcankaomny" };

        setSumInfo(voSum, voaItem[row], saOutNumMny, 2);

        iIdIndex = htRdOut.size();
        if (htRdOut.get(oTempID) != null) {
          iIdIndex = Integer.valueOf(htRdOut.get(oTempID).toString()).intValue();
        }

        voRdo.setRdType(-1, iIdIndex, voSum);
      } else {
        if ((oTempRd == null) || (!oTempRd.toString().toUpperCase().trim().equals("0")))
        {
          continue;
        }
        String[] saInNumMny = { "nterminnum", "nterminastnum", "nterminmny", "ntermingrossnum", "nterminplanmny", "ntermincankaomny" };

        setSumInfo(voSum, voaItem[row], saInNumMny, 1);

        iIdIndex = htRdIn.size();
        if (htRdIn.get(oTempID) != null) {
          iIdIndex = Integer.valueOf(htRdIn.get(oTempID).toString()).intValue();
        }
        voRdo.setRdType(1, iIdIndex, voSum);
      }

    }

    addUpAndSet(voRdo, this.doubleValue, this.doubleValueMny, sa, samny);

    vTempItem.add(voRdo);

    RcvDlvOnhandItemVO[] voaRetItems = null;
    if (vTempItem.size() > 0) {
      voaRetItems = new RcvDlvOnhandItemVO[vTempItem.size()];
      vTempItem.copyInto(voaRetItems);
    }
    return voaRetItems;
  }

  private void setSumInfo(RdsumVO vosum, CircularlyAccessibleValueObject voItem, String[] saKey, int in_out)
  {
    for (int i = 0; i < saKey.length; i++) {
      UFDouble ufd = (UFDouble)voItem.getAttributeValue(saKey[i]);
      double d = ufd == null ? 0.0D : ufd.doubleValue();
      if (saKey[i].endsWith("astnum")) {
        vosum.setAttributeValue("ntermastnum", ufd);
        if (in_out == 1)
          this.doubleValue[4] += d;
        else
          this.doubleValue[7] += d;
      }
      else if (saKey[i].endsWith("grossnum")) {
        vosum.setAttributeValue("ntermgrossnum", ufd);
        if (in_out == 1)
          this.doubleValue[5] += d;
        else
          this.doubleValue[8] += d;
      } else if (saKey[i].endsWith("num")) {
        vosum.setAttributeValue("ntermnum", ufd);
        if (in_out == 1)
          this.doubleValue[3] += d;
        else
          this.doubleValue[6] += d;
      } else if (saKey[i].endsWith("cankaomny")) {
        vosum.setAttributeValue("ntermcankaomny", ufd);
        if (in_out == 1)
          this.doubleValueMny[4] += d;
        else
          this.doubleValueMny[7] += d;
      } else if (saKey[i].endsWith("planmny")) {
        vosum.setAttributeValue("ntermplanmny", ufd);
        if (in_out == 1)
          this.doubleValueMny[3] += d;
        else
          this.doubleValueMny[6] += d;
      } else if (saKey[i].endsWith("mny")) {
        vosum.setAttributeValue("ntermmny", ufd);
        if (in_out == 1)
          this.doubleValueMny[5] += d;
        else
          this.doubleValueMny[8] += d;
      }
    }
  }

  private static double numPrice(CircularlyAccessibleValueObject vo, String priceFld, String numFld)
  {
    double price = vo.getAttributeValue(priceFld) == null ? 0.0D : ((UFDouble)vo.getAttributeValue(priceFld)).doubleValue();

    UFDouble ufdnum = (UFDouble)vo.getAttributeValue(numFld);
    double num = ufdnum == null ? 0.0D : ufdnum.doubleValue();
    return num * price;
  }

  private static void addUpAndSet(CircularlyAccessibleValueObject voRdo, double[] doubleValue, double[] doubleValueMny, String[] sa, String[] samny)
  {
    for (int g = 0; g < doubleValue.length; g++) {
      voRdo.setAttributeValue(sa[g], doubleValue[g] == 0.0D ? null : new UFDouble(doubleValue[g]));
    }

    for (int g = 0; g < doubleValueMny.length; g++) {
      voRdo.setAttributeValue(samny[g], doubleValueMny[g] == 0.0D ? null : new UFDouble(doubleValueMny[g]));
    }

    double handnum = doubleValue[0] + doubleValue[3] - doubleValue[6];
    double handastnum = doubleValue[1] + doubleValue[4] - doubleValue[7];
    double handgrossnum = doubleValue[2] + doubleValue[5] - doubleValue[8];
    voRdo.setAttributeValue("ntermonhandnum", handnum == 0.0D ? null : new UFDouble(handnum));

    voRdo.setAttributeValue("ntermonhandastnum", handastnum == 0.0D ? null : new UFDouble(handastnum));

    voRdo.setAttributeValue("ntermonhandgrossnum", handgrossnum == 0.0D ? null : new UFDouble(handgrossnum));

    double handmny = doubleValueMny[2] + doubleValueMny[5] - doubleValueMny[8];

    voRdo.setAttributeValue("ntermonhandmny", handmny == 0.0D ? null : new UFDouble(handmny));

    double cankaomny = doubleValueMny[1] + doubleValueMny[4] - doubleValueMny[7];

    voRdo.setAttributeValue("ntermonhandcankaomny", cankaomny == 0.0D ? null : new UFDouble(cankaomny));

    if (voRdo.getAttributeValue("nplannedprice") != null)
      voRdo.setAttributeValue("ntermonhandplanmny", Double.valueOf(handnum * ((UFDouble)voRdo.getAttributeValue("nplannedprice")).doubleValue()));
    else
      voRdo.setAttributeValue("ntermonhandplanmny", DataTypeConst.UFDOUBLE_0);
  }

  private void initDoubleValue()
  {
    for (int g = 0; g < this.doubleValue.length; g++) {
      this.doubleValue[g] = 0.0D;
    }
    for (int g = 0; g < this.doubleValueMny.length; g++)
      this.doubleValueMny[g] = 0.0D;
  }

  private ArrayList<RcvDlvOnhandItemVO> combineQiChuAndBenQi(String[] saGroupField, ArrayList<RcvDlvOnhandItemVO> voalItemQiChu, ArrayList<RcvDlvOnhandItemVO> voalItemBenQi)
  {
    if ((saGroupField == null) || (saGroupField.length == 0))
      return null;
    Object oTempField = null;
    HashMap sGroupField_Map = new HashMap();
    ArrayList voalItem = new ArrayList();

    if ((voalItemQiChu != null) && (!voalItemQiChu.isEmpty())) {
      for (RcvDlvOnhandItemVO voItemQiChu : voalItemQiChu)
      {
        StringBuffer sbGroupString = new StringBuffer();
        for (int gf = 0; gf < saGroupField.length; gf++)
        {
          oTempField = voItemQiChu.getAttributeValue(saGroupField[gf]);

          if ((oTempField == null) || (oTempField.toString().trim().length() == 0))
            sbGroupString.append(RcvDlvOnhandItemVO.RD_NULL_ID);
          else
            sbGroupString.append(oTempField.toString().trim());
        }
        sGroupField_Map.put(sbGroupString.toString(), voItemQiChu);
      }
    }

    if ((voalItemBenQi != null) && (!voalItemBenQi.isEmpty())) {
      for (RcvDlvOnhandItemVO voItemBenQi : voalItemBenQi)
      {
        StringBuffer sbGroupString = new StringBuffer();
        for (int gf = 0; gf < saGroupField.length; gf++)
        {
          oTempField = voItemBenQi.getAttributeValue(saGroupField[gf]);

          if ((oTempField == null) || (oTempField.toString().trim().length() == 0))
            sbGroupString.append(RcvDlvOnhandItemVO.RD_NULL_ID);
          else
            sbGroupString.append(oTempField.toString().trim());
        }
        if (sGroupField_Map.containsKey(sbGroupString.toString()))
        {
          RcvDlvOnhandItemVO voItemQiChu = (RcvDlvOnhandItemVO)sGroupField_Map.get(sbGroupString.toString());
          sGroupField_Map.remove(sbGroupString.toString());
          for (String sNumField : saBenQi)
            voItemQiChu.setAttributeValue(sNumField, voItemBenQi.getAttributeValue(sNumField));
          if (this.m_queryParam.isSplitRd())
          {
            voItemQiChu.setAttributeValue("cdispatcherid", voItemBenQi.getAttributeValue("cdispatcherid"));
            voItemQiChu.setAttributeValue("cdispatchername", voItemBenQi.getAttributeValue("cdispatchername"));
            voItemQiChu.setAttributeValue("rdflag", voItemBenQi.getAttributeValue("rdflag"));
          }
          voItemQiChu.setAttributeValue("nterminplanmny", voItemBenQi.getAttributeValue("nterminplanmny"));
          voItemQiChu.setAttributeValue("ntermoutplanmny", voItemBenQi.getAttributeValue("ntermoutplanmny"));
          if (this.m_cType == '0')
          {
            voItemQiChu.setAttributeValue("nterminmny", voItemBenQi.getAttributeValue("nterminmny"));
            voItemQiChu.setAttributeValue("ntermoutmny", voItemBenQi.getAttributeValue("ntermoutmny"));
          }
          voalItem.add(voItemQiChu);
        }
        else
        {
          voalItem.add(voItemBenQi);
        }
      }
    }
    if (!sGroupField_Map.isEmpty())
    {
      Iterator itKeySet = sGroupField_Map.keySet().iterator();
      while (itKeySet.hasNext()) {
        voalItem.add(sGroupField_Map.get(itKeySet.next()));
      }
    }
    ArrayList alResult = SortMethod.sortByKeys(saGroupField, null, voalItem);
    voalItem = (ArrayList)alResult.get(1);

    return voalItem;
  }

  public RcvDlvOnhandItemVO[] sumNumMny(String[] saGroupField, RcvDlvOnhandItemVO[] voaItem)
  {
    if ((saGroupField == null) || (saGroupField.length == 0) || (voaItem == null) || (voaItem.length == 0))
    {
      return null;
    }
    StringBuffer sbGroupString = new StringBuffer();
    String sLastGroup = null;
    RcvDlvOnhandItemVO voRdo = new RcvDlvOnhandItemVO();
    ArrayList vTempItem = new ArrayList();
    initDoubleValue();
    Object oTempField = null;
    for (int row = 0; row < voaItem.length; row++) {
      if (voaItem[row] == null) {
        continue;
      }
      sbGroupString = new StringBuffer();
      for (int gf = 0; gf < saGroupField.length; gf++) {
        oTempField = voaItem[row].getAttributeValue(saGroupField[gf]);

        if ((oTempField == null) || (oTempField.toString().trim().length() == 0))
          sbGroupString.append(RcvDlvOnhandItemVO.RD_NULL_ID);
        else {
          sbGroupString.append(oTempField.toString().trim());
        }
      }

      if (row == 0) {
        sLastGroup = sbGroupString.toString();
        voRdo = (RcvDlvOnhandItemVO)voaItem[row].clone();
      }

      if (!sbGroupString.toString().equals(sLastGroup))
      {
        addUpAndSet(voRdo, this.doubleValue, this.doubleValueMny, sa, samny);

        vTempItem.add(voRdo);

        voRdo = (RcvDlvOnhandItemVO)voaItem[row].clone();
        initDoubleValue();

        sLastGroup = sbGroupString.toString();
      }

      for (int j = 0; j < sa.length; j++) {
        this.doubleValue[j] += (voaItem[row].getAttributeValue(sa[j]) == null ? 0.0D : ((UFDouble)voaItem[row].getAttributeValue(sa[j])).doubleValue());
      }

      if (this.m_queryParam.isMnyShow())
      {
        this.doubleValueMny[2] += numPrice(voaItem[row], "nprice", "ntermbeginnum");

        this.doubleValueMny[5] += numPrice(voaItem[row], "nprice", "nterminnum");

        this.doubleValueMny[8] += numPrice(voaItem[row], "nprice", "ntermoutnum");
      }

      if (this.m_queryParam.isJHJEShow()) {
        this.doubleValueMny[0] += numPrice(voaItem[row], "nlastplanprice", "ntermbeginnum");

        this.doubleValueMny[3] += (voaItem[row].getAttributeValue("nterminplanmny") == null ? 0.0D : ((UFDouble)voaItem[row].getAttributeValue("nterminplanmny")).doubleValue());

        this.doubleValueMny[6] += (voaItem[row].getAttributeValue("ntermoutplanmny") == null ? 0.0D : ((UFDouble)voaItem[row].getAttributeValue("ntermoutplanmny")).doubleValue());
      }

      if (this.m_queryParam.isCanKaoMnyShow()) {
        this.doubleValueMny[1] += numPrice(voaItem[row], "ncankaoprice", "ntermbeginnum");

        this.doubleValueMny[4] += numPrice(voaItem[row], "ncankaoprice", "nterminnum");

        this.doubleValueMny[7] += numPrice(voaItem[row], "ncankaoprice", "ntermoutnum");
      }

    }

    addUpAndSet(voRdo, this.doubleValue, this.doubleValueMny, sa, samny);
    initDoubleValue();
    vTempItem.add(voRdo);

    RcvDlvOnhandItemVO[] voaRetItems = null;
    if (vTempItem.size() > 0) {
      voaRetItems = new RcvDlvOnhandItemVO[vTempItem.size()];
      vTempItem.toArray(voaRetItems);
    }
    return voaRetItems;
  }

  private String getTranBillCode()
  {
    if ("公司".equalsIgnoreCase(this.m_sWarehouselevel)) {
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant))
      {
        return "4U";
      } } else {
      if ("库存组织".equalsIgnoreCase(this.m_sWarehouselevel))
      {
        return "4K";
      }if ("仓库".equalsIgnoreCase(this.m_sWarehouselevel))
      {
        if (this.m_sWhlevelAssisant.equals("Y"))
          return null;
        return "4Q";
      }
    }
    return null;
  }

  private String formInvClassCorps(String[] sCorps) {
    String[] corps = new String[sCorps.length + 1];
    System.arraycopy(sCorps, 0, corps, 0, sCorps.length);
    corps[sCorps.length] = "0001";
    return SQLUtil.formInSQL("pk_corp", corps);
  }

  private void getConditonValue(QryConditionVO voCond)
    throws Exception
  {
    Object oTemp = null;

    this.m_queryParam = ((QueryCondCtl)voCond.getParam(22));

    oTemp = voCond.getParam(1);
    if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
      this.m_cType = oTemp.toString().trim().charAt(0);
    }

    oTemp = voCond.getParam(4);
    this.m_dtStartDate = null;
    if (oTemp != null)
    {
      this.m_dtStartDate = new UFDate(oTemp.toString());
      MonthQuery.isCorrectBeginDate(this.m_dtStartDate);
    }

    oTemp = voCond.getParam(5);
    this.m_dtEndDate = null;
    if (oTemp != null) {
      this.m_dtEndDate = new UFDate(oTemp.toString());
    }

    oTemp = voCond.getParam(7);

    this.m_iInvClassLevel = 1;
    if ((oTemp != null) && (oTemp.toString().trim().length() > 0)) {
      try {
        this.m_iInvClassLevel = Integer.parseInt(oTemp.toString());
      }
      catch (Exception e) {
        SCMEnv.error(e);
      }
    }

    this.m_sWarehouselevel = voCond.getParamStrValue(15);

    this.m_sWhlevelAssisant = voCond.getParamStrValue(16);

    this.m_sViewName = "ic_keep_detail4";
    this.m_INNUM = "ninnum";
    this.m_INASSISTNUM = "ninassistnum";
    this.m_OUTNUM = "noutnum";
    this.m_OUTASSISTNUM = "noutassistnum";

    setSelectField();

    this.m_sHidwarehtransferWhereStr = "";
    String sTransBillCode = getTranBillCode();
    String ishidwarehtransfer = "N";
    ishidwarehtransfer = voCond.getParamStrValue(19);

    if (("Y".equalsIgnoreCase(ishidwarehtransfer)) && (sTransBillCode != null))
    {
      this.m_sHidwarehtransferWhereStr = (" and ( csourcetype is null or csourcetype<>'" + sTransBillCode + "') ");
    }

    this.m_aryOrderby = getGroupFields(this.m_cType, this.m_sWarehouselevel, this.m_sWhlevelAssisant);

    this.m_sOrderby = "";
    for (int i = 0; i < this.m_aryOrderby.length; i++) {
      if (i > 0)
        this.m_sOrderby += ",";
      this.m_sOrderby += this.m_aryOrderby[i];
    }

    this.m_sVoCondWhereStr = "";
    if ((voCond.getQryCond(1) != null) && (voCond.getQryCond(1).trim().length() > 0))
    {
      this.m_sVoCondWhereStr = (" AND " + voCond.getQryCond(1));
    }
  }

  private String[] getGroupFields(char cGroupType, String sWarehouselevel, String sWhlevelAssisant)
  {
    String[] saGroupField = null;
    ArrayList alGroup = new ArrayList();
    if ("公司".equalsIgnoreCase(this.m_sWarehouselevel))
    {
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        alGroup.add("ccalbodyid");
      }
    }
    else if ("库存组织".equalsIgnoreCase(this.m_sWarehouselevel)) {
      alGroup.add("ccalbodyid");
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        alGroup.add("cwarehouseid");
      }
    }
    else if ("仓库".equalsIgnoreCase(this.m_sWarehouselevel)) {
      alGroup.add("cwarehouseid");
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        alGroup.add("cspaceid");
      }
    }

    switch (cGroupType)
    {
    case '0':
      alGroup.add("invclasscode");
      break;
    case '1':
    default:
      alGroup.add("cinventoryid");
      break;
    case '2':
      alGroup.add("cinventoryid");

      alGroup.add("vbatchcode");
      break;
    case '3':
      alGroup.add("cinventoryid");
      alGroup.add("vfree1");
      alGroup.add("vfree2");
      alGroup.add("vfree3");
      alGroup.add("vfree4");
      alGroup.add("vfree5");
      alGroup.add("vfree6");
      alGroup.add("vfree7");
      alGroup.add("vfree8");
      alGroup.add("vfree9");
      alGroup.add("vfree10");
      break;
    case '4':
      alGroup.add("cinventoryid");
      alGroup.add("castunitid");
      alGroup.add("hsl");
      break;
    case '5':
      alGroup.add("cinventoryid");
      alGroup.add("castunitid");
      alGroup.add("hsl");

      alGroup.add("vbatchcode");
      alGroup.add("vfree1");
      alGroup.add("vfree2");
      alGroup.add("vfree3");
      alGroup.add("vfree4");
      alGroup.add("vfree5");
      alGroup.add("vfree6");
      alGroup.add("vfree7");
      alGroup.add("vfree8");
      alGroup.add("vfree9");
      alGroup.add("vfree10");
    }

    if (this.m_queryParam.isProvider())
      alGroup.add("cproviderid");
    saGroupField = new String[alGroup.size()];
    alGroup.toArray(saGroupField);
    return saGroupField;
  }

  private String[] getGroupFld_V5()
  {
    if (!this.m_queryParam.isSplitRd()) {
      return this.m_aryOrderby;
    }
    return (String[])(String[])SqlHelper.arrayAppend(this.m_aryOrderby, new String[] { "cdispatcherid", "rdflag" });
  }

  private String getIfAddProviderField(String sField, String appendField)
  {
    if (this.m_queryParam.isProvider()) {
      return sField + appendField;
    }
    return sField;
  }

  private String getDateWhere(int qry_stat)
    throws Exception
  {
    if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
      return " and kp.daccountdate<'" + this.m_dtStartDate + "'";
    }

    MonthMode ms = new MonthMode();
    ms.alterTableName(0);
    MonthExecImp imp = new MonthExecImp(ms);
    QryZone qz = imp.getUFDateZoneQiChuSplit(this.m_dtStartDate);
    String sHandDate = qz.getDate_hand();
    String[] saRecDate = qz.getDate_record();
    String[] saICDate = qz.getDate_ic();
    String sDateWhere = null;
    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if ((0 == qry_stat) && 
        (sHandDate != null)) {
        sDateWhere = " and kp.dyearmonth='" + sHandDate + "'";
      }
      if ((1 == qry_stat) && 
        (saRecDate != null)) {
        if (saRecDate[0] != null) {
          sDateWhere = " and kp.dyearmonth>='" + saRecDate[0] + "' and kp.dyearmonth<='" + saRecDate[1] + "'";
        }
        else {
          sDateWhere = " and kp.dyearmonth>='" + saRecDate[1] + "'";
        }
      }

      if ((2 == qry_stat) && 
        (saICDate != null)) {
        if (saICDate[0] != null) {
          sDateWhere = " and kp.dbizdate>'" + saICDate[0] + "' and kp.dbizdate<'" + saICDate[1] + "'";
        }
        else {
          sDateWhere = " and kp.dbizdate<'" + saICDate[1] + "'";
        }
      }
    }

    if (sDateWhere == null)
      return null;
    return sDateWhere;
  }

  private String getSql_inv_qichu(int qry_stat)
    throws Exception
  {
    String sqlQiChu = " select SEL_FLD3 RD_FLAG3 SUM_QICHU from TABLE_QICHU JOIN_UNION_SQL JOIN_RD_TYPE where DATE_WHERE BILL_WHERE CORP_WHERE  VOCOND_WHERE SPECIAL_WHERE BILLTYPE_WHERE group by GROUP_FLD RD_GROUP";

    String sDateWhere = null;
    if (qry_stat == 0)
      sDateWhere = (String)this.m_alDateWhere.get(2);
    else if (qry_stat == 1)
      sDateWhere = (String)this.m_alDateWhere.get(1);
    else {
      sDateWhere = (String)this.m_alDateWhere.get(0);
    }

    if (sDateWhere == null) {
      return null;
    }
    QryCondStr p = new QryCondStr();
    String sx = null;
    if ((qry_stat == 0) || (qry_stat == 1)) {
      sx = SqlHelper.replace(this.sSelectFields3, "cproviderid", "cvendorid as cproviderid");
    }
    else
      sx = this.sSelectFields3;
    sx = SqlHelper.replace(sx, "ccalbodyid", " ccalbodyid as pk_calbody");
    p.set("SEL_FLD3", sx);

    if (this.m_queryParam.isSplitRd())
      p.set("RD_FLAG3", "cdispatcherid,9 AS rdflag, ");
    else {
      p.set("RD_FLAG3", " ");
    }

    String sum_qichu_ic = "isnull(sum(" + this.m_INNUM + "),0.0)-isnull(sum(" + this.m_OUTNUM + "),0.0)  AS ntermbeginnum1, " + "isnull(sum(" + this.m_INASSISTNUM + "),0.0)-isnull(sum(" + this.m_OUTASSISTNUM + "),0.0) as ntermbeginastnum1," + " 0.0 AS  nterminnum1,0.0 AS  nterminastnum1, 0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1," + "isnull(sum(ningrossnum),0.0)-isnull(sum(noutgrossnum),0.0) as ntermbegingrossnum, 0.0 as ntermingrossnum1, 0.0 as ntermoutgrossnum1 ";

    if (qry_stat == 0) {
      sum_qichu_ic = "isnull(sum(nonhandnum),0.0) ntermbeginnum1, isnull(sum(nonhandassistnum),0.0) ntermbeginastnum1, 0.0 AS  nterminnum1,0.0 AS  nterminastnum1, 0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1, isnull(sum(ngrossnum),0.0) as ntermbegingrossnum,0.0 as ntermingrossnum1,0.0 as ntermoutgrossnum1";
    }
    else if (qry_stat == 1) {
      sum_qichu_ic = "isnull(sum(ninnum),0.0)-isnull(sum(noutnum),0.0) ntermbeginnum1, isnull(sum(ninassistnum),0.0)-isnull(sum(noutassistnum),0.0) ntermbeginastnum1,0.0 AS  nterminnum1,0.0 AS  nterminastnum1, 0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1, isnull(sum(ningrossnum),0.0)-isnull(sum(noutgrossnum),0.0) ntermbegingrossnum,0.0 as ntermingrossnum1,0.0 as ntermoutgrossnum1";
    }

    p.set("SUM_QICHU", sum_qichu_ic);

    if (qry_stat == 2)
      p.set("TABLE_QICHU", this.m_sViewName + " kp ");
    else if (qry_stat == 0) {
      if ((this.m_queryParam.getParam("日期选择") != null) && (this.m_queryParam.getParam("日期选择").equals("dbizdate")))
        p.set("TABLE_QICHU", " ic_month_hand kp ");
      else
        p.set("TABLE_QICHU", " ic_month_handsign kp ");
    }
    else if (qry_stat == 1) {
      if ((this.m_queryParam.getParam("日期选择") != null) && (this.m_queryParam.getParam("日期选择").equals("dbizdate")))
        p.set("TABLE_QICHU", "ic_month_record kp ");
      else {
        p.set("TABLE_QICHU", "ic_month_recordsign kp ");
      }
    }
    p.set("JOIN_UNION_SQL", this.sUnionSQL);
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue()))
      p.set("JOIN_RD_TYPE", this.sJoinRdType);
    else {
      p.set("JOIN_RD_TYPE", " ");
    }
    p.set("DATE_WHERE", sDateWhere);

    p.set("BILL_WHERE", this.m_queryParam.getSignBillOnlyWhere("kp"));
    p.set("CORP_WHERE", this.m_queryParam.getCorpWhere("kp"));

    String cproviderWhere = null;
    if ((qry_stat == 0) || (qry_stat == 1))
      cproviderWhere = this.m_sVoCondWhereStr.replaceAll("cproviderid", "cvendorid");
    else
      cproviderWhere = this.m_sVoCondWhereStr;
    p.set("VOCOND_WHERE", cproviderWhere);

    p.set("SPECIAL_WHERE", this.m_sHidwarehtransferWhereStr);

    if (qry_stat == 1)
      p.set("BILLTYPE_WHERE", SqlMonthSum.where_xcl_billtype_rec);
    else {
      p.set("BILLTYPE_WHERE", "");
    }
    String sg = SqlHelper.replace(this.sGroupFields2, "cproviderid", "cvendorid");

    if (qry_stat == 0)
      p.set("GROUP_FLD", addTableAlias(sg, "hand"));
    else if (qry_stat == 1)
      p.set("GROUP_FLD", addTableAlias(sg, "rec"));
    else {
      p.set("GROUP_FLD", addTableAlias(this.sGroupFields2, "kp"));
    }
    if (this.m_queryParam.isSplitRd())
      p.set("RD_GROUP", ",cdispatcherid ");
    else {
      p.set("RD_GROUP", "");
    }
    return SqlHelper.replaceAnddeal(sqlQiChu, p);
  }

  private static String addTableAlias(String sql, String alias)
  {
    if ((sql == null) || (alias == null))
      return sql;
    String[] sa = SqlHelper.splitString(sql, ",");

    for (int i = 0; i < sa.length; i++) {
      if ((sa[i] == null) || (sa[i].length() == 0))
        continue;
      int index = sa[i].indexOf(".");
      if (index == -1)
        sa[i] = (alias + "." + sa[i]);
      else {
        sa[i] = (alias + "." + sa[i].substring(index + 1));
      }
    }

    return SQLUtil.getStr(sa);
  }

  private void setSelectFieldInv()
  {
    switch (this.m_cType) {
    case '1':
      this.sSelectFields1 += "cinventoryid,\n";
      this.sSelectFields2 += "cinventoryid, ";
      this.sSelectFields3 += "kp.cinventoryid,";
      this.sGroupFields1 = ("cinventoryid, " + this.sGroupFields1);
      this.sGroupFields2 = ("kp.cinventoryid, " + this.sGroupFields2);
      break;
    case '2':
      this.sSelectFields1 += "cinventoryid, vbatchcode,\n";
      this.sSelectFields2 += "cinventoryid, vbatchcode,";
      this.sSelectFields3 += "kp.cinventoryid,vbatchcode,";
      this.sGroupFields1 = ("cinventoryid, vbatchcode, " + this.sGroupFields1);
      this.sGroupFields2 = ("kp.cinventoryid, kp.vbatchcode, " + this.sGroupFields2);
      this.sOrderFields = ", vbatchcode";

      break;
    case '3':
      this.sSelectFields1 += "cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,\n";

      this.sSelectFields2 += "cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,";

      this.sSelectFields3 += "kp.cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,";

      this.sGroupFields1 = ("cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10," + this.sGroupFields1);
      this.sGroupFields2 = ("kp.cinventoryid,kp.vfree1,kp.vfree2,kp.vfree3,kp.vfree4,kp.vfree5,kp.vfree6,kp.vfree7,kp.vfree8,kp.vfree9,kp.vfree10," + this.sGroupFields2);

      break;
    case '4':
      this.sSelectFields1 += "cinventoryid, castunitid, hsl, \n";
      this.sSelectFields2 += "cinventoryid, castunitid, hsl, ";
      this.sSelectFields3 += "kp.cinventoryid, castunitid, hsl, ";
      this.sGroupFields1 = ("cinventoryid, castunitid, hsl, " + this.sGroupFields1);
      this.sGroupFields2 = ("kp.cinventoryid, kp.castunitid, hsl, " + this.sGroupFields2);

      break;
    case '5':
      this.sSelectFields1 += "cinventoryid,castunitid, hsl,vbatchcode,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,\n";

      this.sSelectFields2 += "cinventoryid, castunitid, hsl,vbatchcode,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,\n";

      this.sSelectFields3 += "kp.cinventoryid,castunitid, hsl,vbatchcode,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,\n";

      this.sGroupFields1 = ("cinventoryid,castunitid, hsl,vbatchcode,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10," + this.sGroupFields1 + " \n");
      this.sGroupFields2 = ("kp.cinventoryid,kp.castunitid, hsl,kp.vbatchcode,kp.vfree1,kp.vfree2,kp.vfree3,kp.vfree4,kp.vfree5,kp.vfree6,kp.vfree7,kp.vfree8,kp.vfree9,kp.vfree10," + this.sGroupFields2 + " \n");
      this.sOrderFields = ",vbatchcode";
    }
  }

  private String[] getSelectGroupFlds(QryConditionVO voCond)
  {
    ArrayList al = new ArrayList();

    if ("仓库".equalsIgnoreCase(this.m_sWarehouselevel)) {
      al.add(CORP_GROUP);
      al.add(CALBODY_GROUP);
      al.add(WAREHOUSE_GROUP);
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        al.add(CSPACE_GROUP);
      }
    }
    else if ("库存组织".equalsIgnoreCase(this.m_sWarehouselevel)) {
      al.add(CORP_GROUP);
      al.add(CALBODY_GROUP);
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        al.add(WAREHOUSE_GROUP);
      }
    }
    else if ("公司".equalsIgnoreCase(this.m_sWarehouselevel)) {
      al.add(CORP_GROUP);
      if ("Y".equalsIgnoreCase(this.m_sWhlevelAssisant)) {
        al.add(CALBODY_GROUP);
      }

    }

    if (this.m_cType != '0')
    {
      boolean bMny = this.m_queryParam.isMnyShow();

      if (this.m_cType == '1') {
        al.add(CINVMANID_GROUP);
        if (bMny)
          al.add(VBATCHCODE_GROUP);
      } else if (this.m_cType == '2') {
        al.add(CINVMANID_GROUP);
        al.add(VBATCHCODE_GROUP);
      } else if (this.m_cType == '3') {
        al.add(CINVMANID_GROUP);
        for (int x = 0; x < FREEITEM_GROUP.length; x++)
          al.add(FREEITEM_GROUP[x]);
        if (bMny)
          al.add(VBATCHCODE_GROUP);
      } else if (this.m_cType == '4') {
        al.add(CINVMANID_GROUP);
        al.add(CASTUNIT_GROUP);
        if (bMny) {
          al.add(VBATCHCODE_GROUP);
        }
      }
    }

    if (this.m_queryParam.isProvider() == true) {
      al.add("cvendorid");
    }

    if (this.m_queryParam.isSplitRd() == true) {
      al.add("cdispatcherid");
    }
    if (al.size() > 0) {
      return (String[])(String[])al.toArray(new String[al.size()]);
    }
    return null;
  }

  private ArrayList<RcvDlvOnhandItemVO> getInv_QiChu(QryConditionVO voCond)
    throws BusinessException
  {
    if ((voCond == null) || (this.m_dtStartDate == null)) return null;
//    ConditionVO[] voaNew = dealConditionVOs(voCond);
      //edit by zwx 2019-11-14 增加过滤印铁的仓库：宝印剪切库（作废）、宝印成品库(作废)、宝印半成品库(封存)
      ConditionVO[] voaOld = dealConditionVOs(voCond);
      ConditionVO condition = new ConditionVO();
	  condition.setFieldCode("cwarehouseid");
	  condition.setOperaCode("not in ");
	  condition.setDataType(0);
	  condition.setValue("('1020A110000000001WQG','1020A110000000001WQI','1020A110000000001WQM')");
	  ConditionVO[] voaNew = new ConditionVO[voaOld.length+1];
	  System.arraycopy(voaOld, 0, voaNew, 0, voaOld.length);
	  voaNew[voaOld.length]=condition;
	  //end by zwx 
    QryConditionCtrl ctrl = new QryConditionCtrl(voaNew);

    String[] asSelectedGroupFlds = SqlHelper.splitString(this.sGroupFields1, ",");
    for (int i = 0; i < asSelectedGroupFlds.length; i++)
    {
      if (asSelectedGroupFlds[i].trim().equals("ccalbodyid"))
      {
        asSelectedGroupFlds[i] = "pk_calbody";
      }
      if (!asSelectedGroupFlds[i].trim().equals("cproviderid"))
        continue;
      asSelectedGroupFlds[i] = "cvendorid";
    }
ctrl.setWhereField(new String[] { "dept.deptcode" }, new String[] { "cdptid" });
    ctrl.setPureSelectGroupField(asSelectedGroupFlds);
    MonthVO monthVO = new MonthVO();
    ctrl.setVOAndSumField(monthVO.getAttributeNames(), monthVO.getAttributeNames());
    List list;
    try { MonthQuery mq = new MonthQuery();
//      List list;
      if (this.m_queryParam.getParam("日期选择").equals("dbizdate"))
      {
        list = mq.qryQiChu_Kernel(ctrl, this.m_dtStartDate, "nc.vo.ic.ic641.RcvDlvOnhandItemVO");
      }
      else
      {
        list = mq.qryQiChu_Kernel_Sign(ctrl, this.m_dtStartDate, "nc.vo.ic.ic641.RcvDlvOnhandItemVO");
      }
    } catch (Exception ex) {
      SCMEnv.error(ex);
      throw new BusinessException(ex.getMessage());
    }
    if ((list == null) || (list.size() == 0))
    {
      SCMEnv.out("无期初数据...");
      return null;
    }

    UFDouble D0 = new UFDouble(0.0D);

    ArrayList<RcvDlvOnhandItemVO> alItem = (ArrayList<RcvDlvOnhandItemVO>)list;

    for (RcvDlvOnhandItemVO voaItems : alItem) {
      for (String asSelectedFlds : asSelectedGroupFlds)
      {
        if (asSelectedFlds.equals("pk_calbody"))
        {
          voaItems.setAttributeValue("ccalbodyid", voaItems.getAttributeValue("pk_calbody"));
        }
        else if (asSelectedFlds.equals("cvendorid"))
        {
          voaItems.setAttributeValue("cproviderid", voaItems.getAttributeValue("cvendorid"));
        }
        else
        {
          voaItems.setAttributeValue(asSelectedFlds, voaItems.getAttributeValue(asSelectedFlds));
        }
      }
      voaItems.setAttributeValue("hsl", voaItems.getAttributeValue("hsl"));
      voaItems.setNtermbeginnum(voaItems.getAttributeValue("nonhandnum") == null ? D0 : (UFDouble)voaItems.getAttributeValue("nonhandnum"));

      voaItems.setNtermbeginastnum(voaItems.getAttributeValue("nonhandassistnum") == null ? D0 : (UFDouble)voaItems.getAttributeValue("nonhandassistnum"));

      voaItems.setNtermbegingrossnum(voaItems.getAttributeValue("ngrossnum") == null ? D0 : (UFDouble)voaItems.getAttributeValue("ngrossnum"));

      voaItems.setPrevjhj(D0);
      voaItems.setCurrentjhj(D0);
    }

    return alItem;
  }

  private ConditionVO[] dealConditionVOs(QryConditionVO voCond)
  {
    ArrayList al = new ArrayList();

    String[] sCorps = (String[])(String[])this.m_queryParam.getParam("公司条件");
    if ((sCorps != null) || (sCorps.length > 0)) {
      ConditionVO vo = new ConditionVO();
      vo.setFieldCode("pk_corp");
      vo.setOperaCode("in");
      vo.setValue(SqlHelper.formInClause(sCorps));
      al.add(vo);
    }

    String sOnlysignedbill = "N";
    sOnlysignedbill = voCond.getParamStrValue(17);
    if ((sOnlysignedbill != null) && ("Y".equals(sOnlysignedbill.trim()))) {
      ConditionVO vo1 = new ConditionVO();
      vo1.setFieldCode("fbillflag");
      vo1.setOperaCode("in");
      vo1.setDataType(1);
      vo1.setValue("3,4");

      al.add(vo1);
    }

    ConditionVO[] voaOld = (ConditionVO[])(ConditionVO[])voCond.getParam(10);
    for (int i = 0; i < voaOld.length; i++)
    {
      if (voaOld[i].getFieldCode().trim().endsWith("ccalbodyid"))
      {
        voaOld[i].setFieldCode("pk_calbody");
      }
      else if (voaOld[i].getFieldCode().trim().endsWith("cproviderid"))
      {
        voaOld[i].setFieldCode("cvendorid");
      }
      else {
        if (!voaOld[i].getFieldCode().trim().endsWith("rdcl.pk_rdcl"))
          continue;
        voaOld[i].setFieldCode("cdispatcherid");
      }

    }

    return combineVOs(voaOld, al);
  }

  private static ConditionVO[] combineVOs(ConditionVO[] voaOld, ArrayList al)
  {
    if ((al == null) || (al.size() <= 0)) {
      try {
        ConditionVO[] voa = (ConditionVO[])(ConditionVO[])ObjectUtils.serializableClone(voaOld);

        return voa;
      } catch (Exception ex) {
        SCMEnv.error(ex);
      }
    }

    ConditionVO[] voaNew = (ConditionVO[])(ConditionVO[])al.toArray(new ConditionVO[al.size()]);

    return (ConditionVO[])(ConditionVO[])SqlHelper.arrayAppend(voaOld, voaNew);
  }

  private int getTableFrom(QryConditionVO voCond)
    throws Exception
  {
    int mode = this.m_queryParam.getParam("日期选择").toString().equals("dbizdate") == true ? 0 : 1;
    MonthMode ms = new MonthMode();
    ms.alterTableName(mode);
    MonthExecImp imp = new MonthExecImp(ms);
    boolean bExistMonth = imp.isExistRecord();
    if (!bExistMonth) {
      return 3;
    }

    String[] saQueryTemplet = { "cprojectid", "cprojectphaseid", "dept.deptcode", "biz.pk_busitype", "cdispatcherid", "rdcl.pk_rdcl" };

    if (this.m_queryParam.isSplitRd()) {
      return 1;
    }

    ConditionVO[] voa = (ConditionVO[])(ConditionVO[])voCond.getParam(10);
    for (int i = 0; i < voa.length; i++) {
      if (voa[i] == null)
        continue;
      String s = voa[i].getFieldCode();
      if ((s == null) || (s.trim().length() == 0))
        continue;
      for (int j = 0; j < saQueryTemplet.length; j++) {
        if (s.endsWith(saQueryTemplet[j])) {
          return 1;
        }

      }

    }

    return 2;
  }

  private void setSplitTime(int tableFrom, boolean bAudit, UFDate startDate)
    throws Exception
  {
    if (startDate == null) {
      return;
    }
    String dateFld = bAudit == true ? "daccountdate" : "dbizdate";
    int iMode = bAudit == true ? 1 : 0;

    MonthMode sv = new MonthMode();
    sv.alterTableName(iMode);
    MonthExecImp imp = new MonthExecImp(sv);

    if (tableFrom == 3) {
      this.m_alDateWhere.add(dateFld + "<'" + startDate.toString() + "'");
      this.m_alDateWhere.add(null);
      this.m_alDateWhere.add(null);
    } else if (tableFrom == 1)
    {
      String maxrec = imp.queryMaxRecordYearMonth(startDate);
      if (maxrec == null)
      {
        this.m_alDateWhere.add(dateFld + "<'" + startDate.toString() + "'");
        this.m_alDateWhere.add(null);
        this.m_alDateWhere.add(null);
        return;
      }

      UFDate dMaxRec = new UFDate(maxrec, false);
      dMaxRec = new UFDate(maxrec + '-' + new Integer(dMaxRec.getDaysMonth()).toString());

      this.m_alDateWhere.add(dateFld + ">'" + dMaxRec.toString() + "' and " + dateFld + "<'" + startDate.toString() + "' ");

      this.m_alDateWhere.add(" dyearmonth<='" + maxrec + "' ");
      this.m_alDateWhere.add(null);
    }
    else if (tableFrom == 2)
    {
      String maxymhand = imp.queryMaxHandByUFDate(startDate);
      if (maxymhand == null) {
        String maxrec = imp.queryMaxRecordYearMonth(startDate);
        if (maxrec == null) {
          this.m_alDateWhere.add(dateFld + "<'" + startDate.toString() + "'");
          this.m_alDateWhere.add(null);
          this.m_alDateWhere.add(null);
          return;
        }

        UFDate dMaxRec = new UFDate(maxrec, false);
        dMaxRec = new UFDate(maxrec + '-' + new Integer(dMaxRec.getDaysMonth()).toString());

        this.m_alDateWhere.add(dateFld + ">'" + dMaxRec.toString() + "' and " + dateFld + "<'" + startDate.toString() + "' ");

        this.m_alDateWhere.add(" dyearmonth <= '" + maxrec + "' ");
        this.m_alDateWhere.add(null);

        dMaxRec = null;
      }
      else {
        QryZone zone = imp.getUFDateZoneQiChuSplit(startDate);

        String[] saic = zone.getDate_ic();
        if (saic[0] == null)
          this.m_alDateWhere.add(dateFld + "<'" + saic[1] + "' ");
        else {
          this.m_alDateWhere.add(dateFld + ">'" + saic[0] + "' and " + dateFld + "<'" + saic[1] + "' ");
        }

        String[] sarec = zone.getDate_record();
        if (sarec != null) {
          if (sarec[0] != null) {
            this.m_alDateWhere.add(" dyearmonth>='" + sarec[0] + "' and dyearmonth<='" + sarec[1] + "' ");
          }
          else
            this.m_alDateWhere.add(" dyearmonth>='" + sarec[1] + "' ");
        }
        else this.m_alDateWhere.add(" 1=0 ");

        String handym = zone.getDate_hand();
        if (handym != null)
          this.m_alDateWhere.add(" dyearmonth='" + handym + "' ");
      }
    }
  }

  private String getSqlInvClass_Begin(QryConditionVO voCond)
    throws Exception
  {
    int tableFrom = getTableFrom(voCond);

    boolean bDaccountDate = false;
    if (this.m_queryParam.getParam("日期选择").equals("daccountdate"))
      bDaccountDate = true;
    setSplitTime(tableFrom, bDaccountDate, this.m_dtStartDate);

    String sql_hand = getSql_invcl_qichu(0);
    String sql_rec = getSql_invcl_qichu(1);
    String sql_ic = getSql_invcl_qichu(2);

    QryCondStr qc = new QryCondStr();

    if (this.m_queryParam.isSplitRd())
      qc.set("收发类别", "cdispatcherid,9 as rdflag,");
    else
      qc.set("收发类别", "");
    qc.set("普通汇总字段", this.m_sSelectFields2Varry + ",invclasscode ,invclassname,avgprice");

    qc.set("数量字段", "isnull(sum(ntermbeginnum),0.0) ntermbeginnum,isnull(sum(ntermbeginastnum),0.0) ntermbeginastnum,isnull(sum(ntermbegingrossnum),0.0) ntermbegingrossnum,0.0 AS nterminnum,0.0 AS nterminastnum, 0.0 as ntermingrossnum,0.0 AS ntermoutnum,0.0 AS ntermoutastnum, 0.0 as ntermoutgrossnum");

    if (this.m_queryParam.isSplitRd())
      qc.set("分组收发", "cdispatcherid");
    else {
      qc.set("分组收发", "");
    }
    String sGroup = this.m_sGroupFields2Varry + " ,okinvcl.invclasscode,okinvcl.invclassname,okinvcl.avgprice";

    sGroup = SqlHelper.replace(sGroup, "kp.", "");
    sGroup = SqlHelper.replace(sGroup, "okinvcl.", "");

    qc.set("分组字段", sGroup);

    String sql_skelet = " select 收发类别,普通汇总字段, 数量字段 from (";
    if (sql_hand != null) {
      sql_skelet = sql_skelet + "结存表SQL union all ";
    }
    if (sql_rec != null)
      sql_skelet = sql_skelet + "月发生表SQL union all ";
    if (sql_ic != null)
      sql_skelet = sql_skelet + "业务表期初SQL) as qichu";
    sql_skelet = sql_skelet + " group by 分组收发,分组字段";

    qc.set("结存表SQL", sql_hand);
    qc.set("月发生表SQL", sql_rec);
    qc.set("业务表期初SQL", sql_ic);

    String sql_qichu = SqlHelper.replace(sql_skelet, qc);
    sql_qichu = SqlHelper.deal_quote(sql_qichu);
    sql_qichu = SqlHelper.deal_and(sql_qichu);

    return sql_qichu;
  }

  private String getSqlInv_Begin(QryConditionVO voCond)
    throws Exception
  {
    int tableFrom = getTableFrom(voCond);

    boolean bDaccountDate = false;
    if (this.m_queryParam.getParam("日期选择").equals("daccountdate"))
      bDaccountDate = true;
    setSplitTime(tableFrom, bDaccountDate, this.m_dtStartDate);

    String sqlQiChu_Hand = null; String sqlQiChu_Record = null; String sqlQiChu_IC = null;

    sqlQiChu_Hand = getSql_inv_qichu(0);
    if (sqlQiChu_Hand != null) {
      sqlQiChu_Hand = " (" + sqlQiChu_Hand + ") " + " union all ";
      sqlQiChu_Hand = SqlHelper.replace(sqlQiChu_Hand, "kp", "hand");
      sqlQiChu_Hand = SqlHelper.replace(sqlQiChu_Hand, "ccalbodyid", "pk_calbody");
    }
    else if (sqlQiChu_Hand == null) {
      sqlQiChu_Hand = "";
    }

    sqlQiChu_Record = getSql_inv_qichu(1);
    if (sqlQiChu_Record != null) {
      sqlQiChu_Record = " (" + sqlQiChu_Record + ") " + " union all ";
      sqlQiChu_Record = SqlHelper.replace(sqlQiChu_Record, "kp", "rec");
      sqlQiChu_Record = SqlHelper.replace(sqlQiChu_Record, "ccalbodyid", "pk_calbody");
    }
    else {
      sqlQiChu_Record = "";
    }

    sqlQiChu_IC = " (" + getSql_inv_qichu(2) + " ) ";

    String sql_qichu = "(select SEL_FLD3,RD_FLAG3,SUM_FLD from (" + sqlQiChu_Hand + sqlQiChu_Record + sqlQiChu_IC + ") qichu group by GROUP_FLD RD_GROUP)";

    QryCondStr m = new QryCondStr();
    String sS = SqlHelper.replace(this.sSelectFields3, "kp.", "");

    sS = SqlHelper.replace(sS, "ccalbodyid", "pk_calbody as ccalbodyid");

    m.set("SEL_FLD3", sS);
    if (this.m_queryParam.isSplitRd())
      m.set("RD_FLAG3", "cdispatcherid,9 AS rdflag,");
    else
      m.set("RD_FLAG3", " ");
    sS = SqlHelper.replace(sS, "pk_calbody as ccalbodyid", "pk_calbody");

    m.set("GROUP_FLD", sS);
    if (this.m_queryParam.isSplitRd())
      m.set("RD_GROUP", " cdispatcherid ,rdflag ");
    else {
      m.set("RD_GROUP", "");
    }
    String sumFld = "sum(ntermbeginnum1) ntermbeginnum1 ,sum(ntermbeginastnum1) ntermbeginastnum1,0.0 AS  nterminnum1,0.0 AS  nterminastnum1, 0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1 ,sum(ntermbegingrossnum) ntermbegingrossnum ,0.0 as ntermingrossnum1,0.0 as ntermoutgrossnum1 ";

    if (this.m_queryParam.isJHJEShow()) {
      sumFld = sumFld + ", 0.0 as nterminplanmny, 0.0 as ntermoutplanmny ";
    }
    m.set("SUM_FLD", sumFld);
    sql_qichu = SqlHelper.replace(sql_qichu, m);
    sql_qichu = SqlHelper.deal_quote(sql_qichu);

    return sql_qichu;
  }

  protected String getInvSQL_Old(QryConditionVO voCond)
    throws SQLException, Exception
  {
    ConditionVO[] conditionVO = (ConditionVO[])(ConditionVO[])voCond.getParam(10);

    DynamicJoinSql dynamic = new DynamicJoinSql("kp", "kp");
    this.sUnionSQL = dynamic.getUnionSQL(conditionVO);

    this.sSelectFields1 = (this.m_sSelectFields1Varry + ",");
    this.sSelectFields2 = (this.m_sSelectFields2Varry + ",");
    this.sSelectFields3 = (this.m_sSelectFields3Varry + ",");
    this.sGroupFields1 = this.m_sGroupFields1Varry;
    this.sGroupFields2 = this.m_sGroupFields2Varry;

    setSelectFieldInv();

    String sJoinRdType = " LEFT OUTER JOIN   bd_rdcl rdcl ON kp.cdispatcherid = rdcl.pk_rdcl ";

    StringBuffer sbSql = new StringBuffer("SELECT ");
    sbSql.append(this.sSelectFields1);

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,rdosum.rdflag,rdcl.rdname as cdispatchername, ");
    }

    sbSql.append(" ntermbeginnum,ntermbeginastnum,nterminnum,nterminastnum,ntermoutnum,ntermoutastnum,ntermonhandnum,ntermonhandastnum, ntermbegingrossnum, ntermingrossnum,ntermoutgrossnum,ntermonhandgrossnum ");

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(",nterminplanmny,ntermoutplanmny");
    }
    sbSql.append(" FROM (");
    sbSql.append("     SELECT ");
    sbSql.append(this.sSelectFields2);

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid, rdflag ,");
    }
    sbSql.append(" SUM(COALESCE(ntermbeginnum1,0.0)) AS ntermbeginnum,SUM(COALESCE(ntermbeginastnum1,0.0)) AS ntermbeginastnum, ");

    sbSql.append("     SUM(COALESCE(nterminnum1,0.0)) AS nterminnum,SUM(COALESCE(nterminastnum1,0.0)) AS nterminastnum, ");

    sbSql.append("     SUM(COALESCE(ntermoutnum1,0.0)) AS ntermoutnum,SUM(COALESCE(ntermoutastnum1,0.0)) AS ntermoutastnum, ");

    sbSql.append("     SUM(COALESCE(ntermbeginnum1,0.0)+COALESCE(nterminnum1,0.0)-COALESCE(ntermoutnum1,0.0)) AS ntermonhandnum, ");

    sbSql.append("     SUM(COALESCE(ntermbeginastnum1,0.0)+COALESCE(nterminastnum1,0.0)-COALESCE(ntermoutastnum1,0.0)) AS ntermonhandastnum,sum(ntermbegingrossnum) ntermbegingrossnum,sum(ntermingrossnum1) ntermingrossnum,sum(ntermoutgrossnum1) ntermoutgrossnum,sum(ntermbegingrossnum)+sum(ntermingrossnum1)-sum(ntermoutgrossnum1) ntermonhandgrossnum");

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(",sum(nterminplanmny) as nterminplanmny,sum(ntermoutplanmny) as ntermoutplanmny ");
    }

    sbSql.append("     FROM    (    ");

    String sqlQICHU = getSqlInv_Begin(voCond);
    sbSql.append(sqlQICHU);

    sbSql.append(" UNION ALL ( \n");

    StringBuffer sb_in = new StringBuffer();
    sb_in.append("            SELECT ");
    sb_in.append(this.sSelectFields3);
    if (this.m_queryParam.isSplitRd())
      sb_in.append(" cdispatcherid,0 AS rdflag ,");
    sb_in.append("                0.0 AS  ntermbeginnum1, ");
    sb_in.append("            0.0 AS  ntermbeginastnum1, SUM(" + this.m_INNUM + ") AS nterminnum1,SUM(" + this.m_INASSISTNUM + ") AS nterminastnum1, 0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1," + "0.0 as ntermbegingrossnum, isnull(sum(ningrossnum),0.0) ntermingrossnum1, 0.0 ntermoutgrossnum1");

    if (this.m_queryParam.isJHJEShow()) {
      sb_in.append(",isnull(sum(nplannedmny),0.0) as nterminplanmny,0.0 as ntermoutplanmny ");
    }

    sb_in.append("               FROM " + this.m_sViewName + "  kp ");

    sb_in.append(this.sUnionSQL);
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) {
      sb_in.append(sJoinRdType);
    }

    sb_in.append(" WHERE (1=1) ");
    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_in.append(" AND kp.daccountdate>='");
        sb_in.append(this.m_dtStartDate.toString().trim());
        sb_in.append("'");
      } else {
        sb_in.append(" AND kp.dbizdate>='");
        sb_in.append(this.m_dtStartDate.toString().trim());
        sb_in.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_in.append(" AND kp.daccountdate<='");
        sb_in.append(this.m_dtEndDate.toString().trim());
        sb_in.append("'");
      } else {
        sb_in.append(" AND kp.dbizdate<='");
        sb_in.append(this.m_dtEndDate.toString().trim());
        sb_in.append("' ");
      }
    }

    sb_in.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");

    sb_in.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    sb_in.append(this.m_queryParam.getCorpWhere("kp"));

    sb_in.append(this.m_sVoCondWhereStr);

    sb_in.append(this.m_sHidwarehtransferWhereStr);

    if (this.m_queryParam.isJHJEShow()) {
      sb_in.append(" and  kp.cbilltypecode in ('4E','45','46','47','48','49','4A','4B','4Q') ");
    }
    sb_in.append("\n GROUP BY \n");

    sb_in.append(addTableAlias(this.sGroupFields2, "kp"));
    if (this.m_queryParam.isSplitRd()) {
      sb_in.append(" ,cdispatcherid ");
    }
    sb_in.append(" HAVING (SUM(" + this.m_INNUM + ") IS NOT NULL) ");

    String sInSql = sb_in.toString();
    sInSql = SqlHelper.deal_quote(sInSql);
    sbSql.append(sInSql);

    StringBuffer sb_out = new StringBuffer();

    sb_out.append("    )        UNION ALL ( ");
    sb_out.append("            SELECT ");
    sb_out.append(this.sSelectFields3);
    if (this.m_queryParam.isSplitRd())
      sb_out.append(" cdispatcherid,1 AS rdflag ,");
    sb_out.append("                0.0 AS  ntermbeginnum1, ");
    sb_out.append("            0.0 AS  ntermbeginastnum1,0.0 AS  nterminnum1,0.0 AS  nterminastnum1,SUM(" + this.m_OUTNUM + ") AS ntermoutnum1,SUM(" + this.m_OUTASSISTNUM + ") AS ntermoutastnum1 ," + "0.0 ntermbegingrossnum,0.0 ntermingrossnum1,isnull(sum(noutgrossnum),0.0) ntermoutgrossnum1");

    if (this.m_queryParam.isJHJEShow()) {
      sb_out.append(",0.0 as  nterminplanmny, isnull(sum(nplannedmny),0.0) as ntermoutplanmny ");
    }

    sb_out.append("               FROM " + this.m_sViewName + "  kp ");

    sb_out.append(this.sUnionSQL);
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) {
      sb_out.append(sJoinRdType);
    }

    sb_out.append(" WHERE (1=1) ");
    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_out.append("AND kp.daccountdate>='");
        sb_out.append(this.m_dtStartDate.toString().trim());
        sb_out.append("'");
      }
      else {
        sb_out.append("AND kp.dbizdate>='");
        sb_out.append(this.m_dtStartDate.toString().trim());
        sb_out.append("'");
      }
    }

    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_out.append(" AND kp.daccountdate<='");
        sb_out.append(this.m_dtEndDate.toString().trim());
        sb_out.append("' ");
      } else {
        sb_out.append(" AND kp.dbizdate<='");
        sb_out.append(this.m_dtEndDate.toString().trim());
        sb_out.append("' ");
      }
    }

    sb_out.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");

    sb_out.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    sb_out.append(this.m_queryParam.getCorpWhere("kp"));

    sb_out.append(this.m_sVoCondWhereStr);

    sb_out.append(this.m_sHidwarehtransferWhereStr);

    if (this.m_queryParam.isJHJEShow()) {
      sb_out.append(" and  kp.cbilltypecode in ('4C','4D','4Y','4F','4G','4H','4I','4J','4O','4Q') ");
    }

    sb_out.append(" GROUP BY \n");
    sb_out.append(addTableAlias(this.sGroupFields2, "kp"));
    if (this.m_queryParam.isSplitRd()) {
      sb_out.append(" ,cdispatcherid ");
    }
    sb_out.append(" HAVING (SUM(" + this.m_OUTNUM + ") IS NOT NULL) ");

    String sSqlOut = sb_out.toString();
    sSqlOut = SqlHelper.deal_quote(sSqlOut);
    sbSql.append(sSqlOut);

    sbSql.append(")) AS rdo \n");

    sbSql.append("GROUP BY \n");
    sbSql.append(this.sGroupFields1);
    if (this.m_queryParam.isSplitRd())
      sbSql.append(" ,cdispatcherid,rdflag ");
    sbSql.append(") AS rdosum  ");

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append("LEFT OUTER JOIN   bd_rdcl rdcl ON rdosum.cdispatcherid = rdcl.pk_rdcl  ");
    }

    sbSql.append(" ORDER BY ");
    sbSql.append(this.m_sOrderby);
    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" ,cdispatcherid ");
    }
    return SqlHelper.deal_quote(sbSql.toString());
  }

  private ArrayList<RcvDlvOnhandItemVO> getInv_BenQi(QryConditionVO voCond)
    throws SQLException, Exception
  {
    ConditionVO[] conditionVO = (ConditionVO[])(ConditionVO[])voCond.getParam(10);
    for (int i = 0; i < conditionVO.length; i++)
    {
      if (!conditionVO[i].getFieldCode().trim().endsWith("cdispatcherid"))
        continue;
      conditionVO[i].setFieldCode("rdcl.pk_rdcl");
      break;
    }

    DynamicJoinSql dynamic = new DynamicJoinSql("kp", "kp");
    this.sUnionSQL = dynamic.getUnionSQL(conditionVO);

    String sJoinRdType = " LEFT OUTER JOIN bd_rdcl rdcl ON kp.cdispatcherid = rdcl.pk_rdcl ";

    StringBuffer sbSql = new StringBuffer(" SELECT ");
    sbSql.append(this.sSelectFields1);

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(",nterminplanmny,ntermoutplanmny,");
    }

    if (this.m_queryParam.isSplitRd()) {
      sbSql.append(" cdispatcherid,rdosum.rdflag,rdcl.rdname as cdispatchername, ");
    }
    sbSql.append(" nterminnum,nterminastnum,ntermingrossnum,ntermoutnum,ntermoutastnum,ntermoutgrossnum ");
    sbSql.append(" FROM (");
    sbSql.append(" SELECT ");
    sbSql.append(this.sSelectFields2);

    if (this.m_queryParam.isSplitRd()) sbSql.append(" cdispatcherid, rdflag ,");

    sbSql.append(" SUM(COALESCE(nterminnum1,0.0)) AS nterminnum,SUM(COALESCE(nterminastnum1,0.0)) AS nterminastnum, ");
    sbSql.append(" SUM(COALESCE(ntermoutnum1,0.0)) AS ntermoutnum,SUM(COALESCE(ntermoutastnum1,0.0)) AS ntermoutastnum, ");
    sbSql.append(" SUM(ntermingrossnum1) ntermingrossnum,SUM(ntermoutgrossnum1) ntermoutgrossnum");

    if (this.m_queryParam.isJHJEShow()) {
      sbSql.append(",sum(nterminplanmny) as nterminplanmny,sum(ntermoutplanmny) as ntermoutplanmny ");
    }
    sbSql.append(" FROM( ");

    StringBuffer sb_in = new StringBuffer();
    sb_in.append("(SELECT ");
    sb_in.append(this.sSelectFields3);
    if (this.m_queryParam.isSplitRd()) sb_in.append(" cdispatcherid,0 AS rdflag ,");
    sb_in.append(" SUM(" + this.m_INNUM + ") AS nterminnum1,SUM(" + this.m_INASSISTNUM + ") AS nterminastnum1, " + "0.0 AS  ntermoutnum1,0.0 AS  ntermoutastnum1," + "isnull(sum(ningrossnum),0.0) ntermingrossnum1, 0.0 ntermoutgrossnum1");

    if (this.m_queryParam.isJHJEShow()) {
      sb_in.append(",isnull(sum(nplannedmny),0.0) as nterminplanmny,0.0 as ntermoutplanmny ");
    }
    sb_in.append(" FROM " + this.m_sViewName + "  kp ");

    sb_in.append(this.sUnionSQL);
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) sb_in.append(sJoinRdType);
    sb_in.append(" WHERE (1=1) ");
    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_in.append(" AND kp.daccountdate>='");
        sb_in.append(this.m_dtStartDate.toString().trim());
        sb_in.append("'");
      } else {
        sb_in.append(" AND kp.dbizdate>='");
        sb_in.append(this.m_dtStartDate.toString().trim());
        sb_in.append("'");
      }
    }
    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_in.append(" AND kp.daccountdate<='");
        sb_in.append(this.m_dtEndDate.toString().trim());
        sb_in.append("'");
      } else {
        sb_in.append(" AND kp.dbizdate<='");
        sb_in.append(this.m_dtEndDate.toString().trim());
        sb_in.append("' ");
      }
    }

    sb_in.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");

    sb_in.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    sb_in.append(this.m_queryParam.getCorpWhere("kp"));

    sb_in.append(this.m_sVoCondWhereStr);

    if (this.m_queryParam.isJHJEShow()) {
      sb_in.append(" and  kp.cbilltypecode in ('4E','45','46','47','48','49','4A','4B','4Q') ");
    }

    sb_in.append(this.m_sHidwarehtransferWhereStr);
    sb_in.append("\n GROUP BY \n");
    sb_in.append(addTableAlias(this.sGroupFields2, "kp"));
    if (this.m_queryParam.isSplitRd()) sb_in.append(" ,cdispatcherid ");
    sb_in.append(" HAVING (SUM(" + this.m_INNUM + ") IS NOT NULL) ");
    String sInSql = sb_in.toString();
    sInSql = SqlHelper.deal_quote(sInSql);
    sbSql.append(sInSql);

    StringBuffer sb_out = new StringBuffer();
    sb_out.append(" )UNION ALL ( ");
    sb_out.append(" SELECT ");
    sb_out.append(this.sSelectFields3);
    if (this.m_queryParam.isSplitRd()) sb_out.append(" cdispatcherid,1 AS rdflag ,");
    sb_out.append(" 0.0 AS  nterminnum1,0.0 AS  nterminastnum1,SUM(" + this.m_OUTNUM + ") AS ntermoutnum1,SUM(" + this.m_OUTASSISTNUM + ") AS ntermoutastnum1 ," + " 0.0 ntermingrossnum1,isnull(sum(noutgrossnum),0.0) ntermoutgrossnum1");

    if (this.m_queryParam.isJHJEShow()) {
      sb_out.append(",0.0 as  nterminplanmny, isnull(sum(nplannedmny),0.0) as ntermoutplanmny ");
    }
    sb_out.append(" FROM " + this.m_sViewName + "  kp ");

    sb_out.append(this.sUnionSQL);
    if ((this.m_queryParam.isSplitRd()) && (!this.m_queryParam.hasRDValue())) {
      sb_out.append(sJoinRdType);
    }
    sb_out.append(" WHERE (1=1) ");
    if ((this.m_dtStartDate != null) && (this.m_dtStartDate.toString().trim().length() > 0))
    {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_out.append("AND kp.daccountdate>='");
        sb_out.append(this.m_dtStartDate.toString().trim());
        sb_out.append("'");
      }
      else {
        sb_out.append("AND kp.dbizdate>='");
        sb_out.append(this.m_dtStartDate.toString().trim());
        sb_out.append("'");
      }
    }

    if ((this.m_dtEndDate != null) && (this.m_dtEndDate.toString().trim().length() > 0)) {
      if (this.m_queryParam.getParam("日期选择").equals("daccountdate")) {
        sb_out.append(" AND kp.daccountdate<='");
        sb_out.append(this.m_dtEndDate.toString().trim());
        sb_out.append("' ");
      } else {
        sb_out.append(" AND kp.dbizdate<='");
        sb_out.append(this.m_dtEndDate.toString().trim());
        sb_out.append("' ");
      }
    }

    sb_out.append(" AND  ( cbilltypecode<>'40' and  cbilltypecode<>'41' and cbilltypecode<>'44') ");

    sb_out.append(this.m_queryParam.getSignBillOnlyWhere("kp"));

    sb_out.append(this.m_queryParam.getCorpWhere("kp"));

    sb_out.append(this.m_sVoCondWhereStr);
    if (this.m_queryParam.isJHJEShow()) {
      sb_out.append(" and  kp.cbilltypecode in ('4C','4D','4Y','4F','4G','4H','4I','4J','4O','4Q') ");
    }

    sb_out.append(this.m_sHidwarehtransferWhereStr);
    sb_out.append(" GROUP BY \n");
    sb_out.append(addTableAlias(this.sGroupFields2, "kp"));
    if (this.m_queryParam.isSplitRd()) sb_out.append(" ,cdispatcherid ");
    sb_out.append(" HAVING (SUM(" + this.m_OUTNUM + ") IS NOT NULL) ");
    String sSqlOut = sb_out.toString();
    sSqlOut = SqlHelper.deal_quote(sSqlOut);
    sbSql.append(sSqlOut);
    sbSql.append(")) rdo \n");

    sbSql.append(" GROUP BY \n");
    sbSql.append(this.sGroupFields1);
    if (this.m_queryParam.isSplitRd()) sbSql.append(" ,cdispatcherid,rdflag ");
    sbSql.append(")  rdosum  ");

    if (this.m_queryParam.isSplitRd())
      sbSql.append(" LEFT OUTER JOIN   bd_rdcl rdcl ON rdosum.cdispatcherid = rdcl.pk_rdcl ");
    sbSql.append(" ORDER BY ");
    sbSql.append(this.m_sOrderby);
    if (this.m_queryParam.isSplitRd()) sbSql.append(" ,cdispatcherid ");
    return execSQL(SqlHelper.deal_quote(sbSql.toString()));
  }
}