package nc.impl.scm.so.pub;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;

public class ParallelCheckDMO extends DataManageObject
{
  String sBillType = null;

  public ParallelCheckDMO()
    throws NamingException, SystemException
  {
  }

  public ParallelCheckDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private void checkParallel(Hashtable htSource, boolean isHead)
    throws BusinessException, SQLException
  {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Hashtable htTarget = new Hashtable();
    try {
      con = getConnection();
      stmt = con.createStatement();
      String[] aryTableStruct = getTableStruct(this.sBillType, isHead);
      StringBuffer sqlTs = getTsCheckSql(aryTableStruct, htSource);
      if (sqlTs != null)
      {
        rs = stmt.executeQuery(sqlTs.toString());
        while (rs.next()) {
          String sID = rs.getString(1).trim();
          String sTS = rs.getString(2).trim();
          if ((sID != null) && 
            (!htTarget.containsKey(sID)))
            htTarget.put(sID, sTS);
        }
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (htTarget.size() > 0) {
      Enumeration enKeyTarget = htTarget.keys();
      while (enKeyTarget.hasMoreElements()) {
        String sMatchKey = (String)enKeyTarget.nextElement();
        String sTargetTs = (String)htTarget.get(sMatchKey);
        String sSourceTs = (String)htSource.get(sMatchKey);
        if (!sTargetTs.equals(sSourceTs))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
      }
    }
  }

  private void checkTSEdit(CircularlyAccessibleValueObject voHead)
    throws BusinessException, SQLException
  {
    String sTmpIDKey = voHead.getPrimaryKey();
    if ((sTmpIDKey == null) || (sTmpIDKey.trim().length() == 0)) {
      SCMEnv.out("数据错误：未获取修改保存单据的表头ID");
      return;
    }

    String sTmpValue = getTimeString(voHead.getAttributeValue("ts"));
    if (sTmpValue == null) {
      SCMEnv.out("数据错误：未获取修改保存单据的表头时间戳");
      return;
    }
    Hashtable htHead = new Hashtable();
    htHead.put(sTmpIDKey, sTmpValue);

    checkParallel(htHead, true);
  }

  private void checkTSNew(AggregatedValueObject voSource)
    throws BusinessException, SQLException
  {
    Hashtable htHead = new Hashtable();

    CircularlyAccessibleValueObject[] voChildren = voSource.getChildrenVO();
    String sSourceBillIDKey = null;
    if (voSource.getClass().getName().equals("nc.vo.so.so001.SaleOrderVO"))
    {
      sSourceBillIDKey = "csourcebillid";
    }
    else {
      sSourceBillIDKey = "cupsourcebillid";
    }
    for (int i = 0; i < voChildren.length; i++)
    {
      String sTmpIDKey = (String)voChildren[i].getAttributeValue(sSourceBillIDKey);

      String sTmpValue = getTimeString(voChildren[i].getAttributeValue("ts"));
      if ((sTmpIDKey == null) || (sTmpValue == null) || 
        (htHead.containsKey(sTmpIDKey))) continue;
      htHead.put(sTmpIDKey, sTmpValue);
    }

    if (htHead.size() == 0) {
      SCMEnv.out("数据错误：未获取由参照单据转入的表头时间戳");
      return;
    }

    checkParallel(htHead, true);
  }

  public UFBoolean checkTsNoChanged(String sBillType, String[] saBillid, String[] saTsh, String[] saBill_bid, String[] saTsb, String[] saBill_bbid, String[] saTsBb)
    throws SQLException, BusinessException
  {
    boolean[] b = { false, false, false };

    if ((saBillid != null) && (saBillid.length > 0)) {
      b[0] = true;
      if ((saTsh == null) || (saTsh.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000116"));
      }
    }
    if ((saBill_bid != null) && (saBill_bid.length > 0)) {
      b[1] = true;
      if ((saTsb == null) || (saTsb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000116"));
      }
    }
    if ((saBill_bbid != null) && (saBill_bbid.length > 0)) {
      b[2] = true;
      if ((saTsBb == null) || (saTsBb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000116"));
      }
    }

    Object[] saObj = queryHBTsArrayByHBIDArray(sBillType, saBillid, saBill_bid, saBill_bbid);
    if ((saObj == null) || (saObj.length <= 0))
    {
      SCMEnv.out("没有查询到相应的时间戳，暂不处理，go on！");
      return new UFBoolean(true);
    }

    String[] saTmp = null;
    if (saObj[0] !=null ) {
      saTmp = (String[])(String[])saObj[0];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsh[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
        }

      }

    }

    return new UFBoolean(true);
  }

  public void checkVOisChanged(AggregatedValueObject vo)
    throws BusinessException, SQLException, SystemException, NamingException
  {
    if (vo == null)
      return;
    CircularlyAccessibleValueObject voParent = vo.getParentVO();
    CircularlyAccessibleValueObject[] voChildren = vo.getChildrenVO();
    if ((voParent == null) && ((voChildren == null) || (voChildren.length == 0)))
      return;
    boolean isNew = (voParent.getPrimaryKey() == null) || (voParent.getPrimaryKey().trim().length() == 0);

    String sSourceVOName = vo.getClass().getName();
    if (isNew)
    {
      boolean isNewByOther = getNewByOther(voChildren, sSourceVOName);

      if (!isNewByOther) {
        return;
      }

      if (this.sBillType.equals("21")) {
        return;
      }
      checkTSNew(vo);
    }
    else {
      if (sSourceVOName.equals("nc.vo.so.so001.SaleOrderVO"))
        this.sBillType = "30";
      else if (sSourceVOName.equals("nc.vo.so.so002.SaleinvoiceVO"))
        this.sBillType = "32";
      else if (sSourceVOName.equals("nc.vo.so.so005.SaleVO"))
        this.sBillType = "31";
      else if (sSourceVOName.equals("nc.vo.so.so012.SquareVO")) {
        this.sBillType = "33";
      }

      checkTSEdit(voParent);
      if ("30".equals(this.sBillType)) {
        new SaleOrderDMO(); SaleOrderDMO.checkSaleorderBVOTs(((SaleOrderVO)vo).getBodyVOs());
      }
    }
  }

  public UFBoolean checkVoNoChanged(AggregatedValueObject vo)
    throws BusinessException, RemoteException, SQLException, NamingException
  {
    checkVOisChanged(vo);
    return new UFBoolean(true);
  }

  public UFBoolean checkVoNoChanged1(AggregatedValueObject vo)
    throws BusinessException, RemoteException, SQLException
  {
    UFBoolean ufbReslt = new UFBoolean(false);

    if (vo == null)
      return ufbReslt;
    if ((vo.getParentVO() == null) && ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)))
    {
      return ufbReslt;
    }
    String sBillType = (String)vo.getParentVO().getAttributeValue("creceipttype");
    String[] saBillid = null;
    String[] saTsh = null;
    String[] saBill_bid = null;
    String[] saTsb = null;

    String strClassName = vo.getClass().getName();
    if (strClassName.equals("nc.vo.so.so001.SaleOrderVO"))
      sBillType = "30";
    else if (strClassName.equals("nc.vo.so.so002.SaleinvoiceVO"))
      sBillType = "32";
    else if (strClassName.equals("nc.vo.so.so005.SaleVO"))
      sBillType = "31";
    else if (strClassName.equals("nc.vo.so.so012.SquareVO")) {
      sBillType = "33";
    }

    boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null) || (vo.getParentVO().getPrimaryKey().trim().length() == 0);

    if (bIsNew) {
      saBillid = null;
      saTsh = null;
      if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000117"));
      }
      boolean bIsHaveRef = false;
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        if (sBillType.equals("30"))
        {
          if ((vo.getChildrenVO()[i].getAttributeValue("creceipttype") == null) || (vo.getChildrenVO()[i].getAttributeValue("creceipttype").toString().trim().equals("")))
          {
            continue;
          }

          sBillType = (String)vo.getChildrenVO()[i].getAttributeValue("creceipttype");
          bIsHaveRef = true;
          if (!sBillType.equals("21"))
            break;
          return new UFBoolean(true);
        }

        if ((vo.getChildrenVO()[i].getAttributeValue("cupreceipttype") == null) || (vo.getChildrenVO()[i].getAttributeValue("cupreceipttype").toString().trim().equals("")))
        {
          continue;
        }

        sBillType = (String)vo.getChildrenVO()[i].getAttributeValue("cupreceipttype");
        bIsHaveRef = true;
        break;
      }

      if (!bIsHaveRef) {
        return new UFBoolean(true);
      }
      Vector vUpId = new Vector();

      Vector vUpTs = new Vector();

      saBillid = new String[1];
      saTsh = new String[1];
      String strTmpId = null;
      String strTmpTs = null;
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        if (strClassName.equals("nc.vo.so.so001.SaleOrderVO"))
        {
          strTmpId = (String)vo.getChildrenVO()[i].getAttributeValue("csourcebillbodyid");

          if ((strTmpId != null) && (!strTmpId.trim().equals(""))) {
            strTmpTs = getTimeString(vo.getChildrenVO()[i].getAttributeValue("ts"));
            if ((strTmpTs == null) || (strTmpTs.trim().equals(""))) {
              SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
              return new UFBoolean(false);
            }

            vUpId.addElement(strTmpId);

            vUpTs.addElement(strTmpTs);

            if ((saBillid[0] == null) || (saBillid[0].trim().length() == 1)) {
              saBillid[0] = ((String)vo.getChildrenVO()[i].getAttributeValue("csourcebillid"));
              saTsh[0] = getTimeString(vo.getChildrenVO()[i].getAttributeValue("ts"));
              if ((saTsh[0] == null) || (saTsh[0].trim().equals(""))) {
                SCMEnv.out("数据错误：未获取由参照单据转入的表头时间戳");
                return new UFBoolean(false);
              }
            }
          }
        }
        else {
          strTmpId = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");

          if ((strTmpId != null) && (!strTmpId.trim().equals(""))) {
            strTmpTs = getTimeString(vo.getChildrenVO()[i].getAttributeValue("ts"));
            if ((strTmpTs == null) || (strTmpTs.trim().equals(""))) {
              SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
              return new UFBoolean(false);
            }

            vUpId.addElement(strTmpId);

            vUpTs.addElement(strTmpTs);

            if ((saBillid[0] == null) || (saBillid[0].trim().length() == 1)) {
              saBillid[0] = ((String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid"));

              saTsh[0] = getTimeString(vo.getChildrenVO()[i].getAttributeValue("ts"));
              if ((saTsh[0] == null) || (saTsh[0].trim().equals(""))) {
                SCMEnv.out("数据错误：未获取由参照单据转入的表头时间戳");
                return new UFBoolean(false);
              }
            }
          }
        }
      }

      if ((vUpId.size() > 0) && (vUpTs.size() > 0) && (vUpId.size() == vUpTs.size())) {
        saBill_bid = new String[vUpId.size()];
        saTsb = new String[vUpTs.size()];
        vUpId.copyInto(saBill_bid);
        vUpTs.copyInto(saTsb);
      } else {
        SCMEnv.out("数据错误：有上层转入的单据，但上层ID及上层TS不匹配");
        return new UFBoolean(false);
      }
    }
    else
    {
      saBillid = new String[1];
      saTsh = new String[1];

      saBillid[0] = vo.getParentVO().getPrimaryKey();

      saTsh[0] = getTimeString(vo.getParentVO().getAttributeValue("ts"));
      if ((saTsh[0] == null) || (saTsh[0].trim().equals(""))) {
        SCMEnv.out("数据错误：未获取修改保存单据的表头时间戳");
        return new UFBoolean(false);
      }

      Vector vBid = new Vector();
      Vector vBts = new Vector();
      if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0)) {
        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          if (vo.getChildrenVO()[i].getStatus() != 2) {
            vBid.addElement(vo.getChildrenVO()[i].getPrimaryKey());
            vBts.addElement(getTimeString(vo.getChildrenVO()[i].getAttributeValue("ts")));
          }
        }
        if (vBid.size() <= 0) {
          SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
          return new UFBoolean(false);
        }
        saBill_bid = new String[vBid.size()];
        saTsb = new String[vBid.size()];
        for (int i = 0; i < vBid.size(); i++) {
          saBill_bid[i] = ((String)vBid.elementAt(i));
          saTsb[i] = ((String)vBts.elementAt(i));
        }
      }
    }

    checkTsNoChanged(sBillType, saBillid, saTsh, saBill_bid, saTsb, null, null);

    return new UFBoolean(true);
  }

  private boolean getNewByOther(CircularlyAccessibleValueObject[] voChildren, String sVOName)
  {
    boolean bIsHaveRef = false;
    for (int i = 0; i < voChildren.length; i++) {
      if (sVOName.equals("nc.vo.so.so001.SaleOrderVO"))
      {
        if ((voChildren[i].getAttributeValue("creceipttype") == null) || (voChildren[i].getAttributeValue("creceipttype").toString().trim().equals(""))) {
          continue;
        }
        this.sBillType = ((String)voChildren[i].getAttributeValue("creceipttype"));
        bIsHaveRef = true;
        break;
      }

      if ((voChildren[i].getAttributeValue("cupreceipttype") == null) || (voChildren[i].getAttributeValue("cupreceipttype").toString().trim().equals(""))) {
        continue;
      }
      this.sBillType = ((String)voChildren[i].getAttributeValue("cupreceipttype"));
      bIsHaveRef = true;
      break;
    }

    return bIsHaveRef;
  }

  private String[] getTableStruct(String sTargetBillType, boolean isHead)
  {
    if (this.sBillType.equals("30")) {
      if (isHead) {
        return new String[] { "csaleid", "so_sale" };
      }
      return new String[] { "corder_bid", "so_saleorder_b" };
    }if (this.sBillType.equals("32")) {
      if (isHead) {
        return new String[] { "csaleid", "so_saleinvoice" };
      }
      return new String[] { "cinvoice_bid", "so_saleinvoice_b" };
    }if (this.sBillType.equals("31")) {
      if (isHead) {
        return new String[] { "csaleid", "so_salereceipt" };
      }
      return new String[] { "creceipt_bid", "so_salereceipt_b" };
    }if (this.sBillType.equals("33")) {
      if (isHead) {
        return new String[] { "csaleid", "so_square" };
      }
      return new String[] { "corder_bid", "so_square_b" };
    }if ((this.sBillType.equals("4C")) || (this.sBillType.equals("4H")) || (this.sBillType.equals("42")))
    {
      if (isHead) {
        return new String[] { "cgeneralhid", "ic_general_h" };
      }
      return new String[] { "cgeneralbid", "ic_general_b" };
    }if ((this.sBillType.equals("Z4")) || (this.sBillType.equals("Z3"))) {
      if (isHead) {
        return new String[] { "pk_ct_manage", "ct_manage" };
      }
      return new String[] { "pk_ct_manage_b", "ct_manage_b" };
    }if (this.sBillType.equals("37")) {
      if (isHead) {
        return new String[] { "csalequotationid", "prm_salequotation" };
      }
      return new String[] { "csalequotation_bid", "prm_salequotation_b" };
    }if (this.sBillType.equals("21")) {
      if (isHead) {
        return new String[] { "corderid", "po_order" };
      }
      return new String[] { "corder_bid", "po_order_b" };
    }if (this.sBillType.equals("38")) {
      if (isHead) {
        return new String[] { "pk_preorder", "so_preorder" };
      }
      return new String[] { "pk_preorder_b", "so_preorder_b" };
    }
    return null;
  }

  private String getTimeString(Object time)
  {
    if (time == null)
      return null;
    return time.toString();
  }

  private StringBuffer getTsCheckSql(String[] sTargetTableStruct, Hashtable htSourceTs)
  {
    StringBuffer sbSQL = null;
    if (sTargetTableStruct != null) {
      String sIDName = sTargetTableStruct[0];
      String sTableName = sTargetTableStruct[1];

      sbSQL = new StringBuffer(" SELECT ");
      sbSQL.append(sIDName);
      sbSQL.append(",ts ");
      sbSQL.append(" FROM ");
      sbSQL.append(sTableName);

      sbSQL.append(" WHERE ");
      Enumeration enumKey = htSourceTs.keys();
      boolean isone = true;
      while (enumKey.hasMoreElements()) {
        if (!isone) {
          sbSQL.append(" OR");
        }

        sbSQL.append(" " + sIDName + "='");
        sbSQL.append(enumKey.nextElement());
        sbSQL.append("'");
        isone = false;
      }
    }
    return sbSQL;
  }

  public Object[] queryHBTsArrayByHBIDArray(String sBillType, String[] saBillid, String[] saBill_bid, String[] saBill_bbid)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.so.pub.ParallelCheckDMO", "queryHTsArrayByIDArray", new Object[] { saBillid, saBill_bid, saBill_bbid });

    if ((sBillType == null) || (sBillType.trim().equals("")) || (((saBillid == null) || (saBillid.length <= 0)) && ((saBill_bid == null) || (saBill_bid.length <= 0)) && ((saBill_bbid == null) || (saBill_bbid.length <= 0))))
    {
      SCMEnv.out("nc.bs.so.pub.ParallelCheckDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确：表头ID数组、表体ID数组及子子表ID数组同时为空");

      return null;
    }

    int iLen = 0;

    if (saBillid != null) {
      iLen = saBillid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBillid[i] == null) || (saBillid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.so.pub.ParallelCheckDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");

          return null;
        }
      }
    }

    if (saBill_bid != null) {
      iLen = saBill_bid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBill_bid[i] == null) || (saBill_bid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.so.pub.ParallelCheckDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");

          return null;
        }
      }
    }

    if (saBill_bbid != null) {
      iLen = saBill_bbid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBill_bbid[i] == null) || (saBill_bbid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.so.pub.ParallelCheckDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");

          return null;
        }

      }

    }

    String strTableNameHead = null;
    String strTableNameBody = null;
    String strTableNameBBody = null;
    String strFieldNameHeadId = null;
    String strFieldNameBodyId = null;
    String strFieldNameBBodyId = null;
    if (sBillType.equals("30")) {
      strTableNameHead = "so_sale";
      strTableNameBody = "so_saleorder_b";
      strFieldNameHeadId = "csaleid";
      strFieldNameBodyId = "corder_bid";
    } else if (sBillType.equals("32")) {
      strTableNameHead = "so_saleinvoice";
      strTableNameBody = "so_saleinvoice_b";

      strFieldNameHeadId = "csaleid";
      strFieldNameBodyId = "cinvoice_bid";
    }
    else if (sBillType.equals("31")) {
      strTableNameHead = "so_salereceipt";
      strTableNameBody = "so_salereceipt_b";
      strFieldNameHeadId = "csaleid";
      strFieldNameBodyId = "creceipt_bid";
    }
    else if (sBillType.equals("33")) {
      strTableNameHead = "so_square";
      strTableNameBody = "so_square_b";
      strFieldNameHeadId = "csaleid";
      strFieldNameBodyId = "corder_bid";
    } else if ((sBillType.equals("4C")) || (sBillType.equals("4H")) || (sBillType.equals("42")))
    {
      strTableNameHead = "ic_general_h";
      strTableNameBody = "ic_general_b";
      strFieldNameHeadId = "cgeneralhid";
      strFieldNameBodyId = "cgeneralbid";
    } else if ((sBillType.equals("Z4")) || (sBillType.equals("Z3"))) {
      strTableNameHead = "ct_manage";
      strTableNameBody = "ct_manage_b";
      strFieldNameHeadId = "pk_ct_manage";
      strFieldNameBodyId = "pk_ct_manage_b";
    } else if (sBillType.equals("37")) {
      strTableNameHead = "prm_salequotation";
      strTableNameBody = "prm_salequotation_b";
      strFieldNameHeadId = "csalequotationid";
      strFieldNameBodyId = "csalequotation_bid";
    } else if (sBillType.equals("21")) {
      strTableNameHead = "po_order";
      strTableNameBody = "po_order_b";
      strFieldNameHeadId = "corderid";
      strFieldNameBodyId = "corder_bid";
    }

    StringBuffer sbufSql1 = null;
    if (saBillid != null) {
      sbufSql1 = new StringBuffer(" SELECT ");
      sbufSql1.append(strFieldNameHeadId);
      sbufSql1.append(",ts ");
      sbufSql1.append(" FROM ");
      sbufSql1.append(strTableNameHead);
      sbufSql1.append(" WHERE 1 < 0 ");
      iLen = saBillid.length;
      for (int i = 0; i < iLen; i++) {
        sbufSql1.append(" OR");
        sbufSql1.append(" " + strFieldNameHeadId + "='");
        sbufSql1.append(saBillid[i]);
        sbufSql1.append("'");
      }
    }

    StringBuffer sbufSql2 = null;
    if (saBill_bid != null) {
      sbufSql2 = new StringBuffer("SELECT ");
      sbufSql2.append(strFieldNameBodyId);
      sbufSql2.append(",ts ");
      sbufSql2.append(" FROM ");
      sbufSql2.append(strTableNameBody + " ");
      sbufSql2.append(" WHERE 1 < 0 ");
      iLen = saBill_bid.length;
      for (int i = 0; i < iLen; i++) {
        sbufSql2.append(" OR ");
        sbufSql2.append(" " + strFieldNameBodyId + "='");
        sbufSql2.append(saBill_bid[i]);
        sbufSql2.append("'");
      }
    }

    StringBuffer sbufSql3 = null;
    if ((saBill_bbid != null) && (strTableNameBBody != null)) {
      sbufSql3 = new StringBuffer("SELECT ");
      sbufSql3.append(strFieldNameBBodyId);
      sbufSql3.append(",ts ");
      sbufSql3.append(" FROM ");
      sbufSql3.append(strTableNameBBody);
      sbufSql3.append(" WHERE 1 < 0 ");
      iLen = saBill_bbid.length;
      for (int i = 0; i < iLen; i++) {
        sbufSql3.append(" OR ");
        sbufSql3.append(" " + strFieldNameBBodyId + "='");
        sbufSql3.append(saBill_bbid[i]);
        sbufSql3.append(" '");
      }

    }

    Hashtable hashHTs = new Hashtable();
    Hashtable hashBTs = new Hashtable();
    Hashtable hashBbTs = new Hashtable();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();

      if (sbufSql1 != null) {
        rs = stmt.executeQuery(sbufSql1.toString());
        while (rs.next()) {
          hashHTs.put(rs.getString(1).trim(), rs.getString(2));
        }

      }

      if (sbufSql2 != null) {
        rs = stmt.executeQuery(sbufSql2.toString());
        while (rs.next()) {
          hashBTs.put(rs.getString(1).trim(), rs.getString(2));
        }

      }

      if (sbufSql3 != null) {
        rs = stmt.executeQuery(sbufSql3.toString());
        while (rs.next())
          hashBbTs.put(rs.getString(1).trim(), rs.getString(2));
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    Object[] oRet = new Object[3];
    if (hashHTs.size() > 0) {
      iLen = saBillid.length;
      String[] saHTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saHTs[i] = ((String)hashHTs.get(saBillid[i].trim()));
      }
      oRet[0] = saHTs;
    }
    if (hashBTs.size() > 0) {
      iLen = saBill_bid.length;
      String[] saBTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saBTs[i] = ((String)hashBTs.get(saBill_bid[i].trim()));
      }
      oRet[1] = saBTs;
    }
    if (hashBbTs.size() > 0) {
      iLen = saBill_bbid.length;
      String[] saBbTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saBbTs[i] = ((String)hashBbTs.get(saBill_bbid[i].trim()));
      }
      oRet[2] = saBbTs;
    }

    afterCallMethod("nc.bs.so.pub.ParallelCheckDMO", "queryHTsArrayByIDArray", new Object[] { saBillid, saBill_bid, saBill_bbid });

    return oRet;
  }
}