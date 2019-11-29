package nc.bs.ic.pub.lot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.naming.NamingException;
import nc.bs.ic.pub.bill.MiscDMO;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;

public class LotNumbRefDMO extends DataManageObject
{
  public LotNumbRefDMO()
    throws NamingException, SystemException
  {
  }

  public LotNumbRefDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  protected GeneralBillVO queryAllLotData(GeneralBillVO gvo)
    throws Exception
  {
    Hashtable htData = null;
    String[] saItemKeys = { "cinventoryid", "vbatchcode" };
    htData = new Hashtable();
    String pk_corp = (String)gvo.getHeaderValue("pk_corp");

    StringBuffer sbKey = null;
    StringBuffer sbWhereSQL = new StringBuffer();

    for (int i = 0; i < gvo.getItemCount(); i++) {
      sbKey = new StringBuffer(pk_corp);

      for (int j = 0; j < saItemKeys.length; j++) {
        sbKey.append(gvo.getItemValue(i, saItemKeys[j]) == null ? "" : (String)gvo.getItemValue(i, saItemKeys[j]));
      }

      if (!htData.containsKey(sbKey.toString())) {
        htData.put(sbKey.toString(), i + "");
        sbWhereSQL.append("'").append(sbKey.toString()).append("',");
      }

    }

    if (sbWhereSQL.length() > 0) {
      sbWhereSQL = sbWhereSQL.deleteCharAt(sbWhereSQL.length() - 1);
    }
    StringBuffer sbSQL = new StringBuffer("SELECT pk_corp,cinventoryid,vbatchcode, dvalidate ");

    sbSQL.append("FROM ic_keep_detail11 ").append("where pk_corp||cinventoryid||isnull(vbatchcode,'') ").append("in(" + sbWhereSQL.toString() + ") ").append("group by pk_corp,cinventoryid,vbatchcode,dvalidate ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String dvalidate = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSQL.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        sbKey = new StringBuffer();
        sbKey.append(pk_corp).append(rs.getString("cinventoryid"));
        String vbatchcode = rs.getString("vbatchcode");
        sbKey.append(vbatchcode == null ? "" : vbatchcode);
        dvalidate = rs.getString("dvalidate");
        if (htData.containsKey(sbKey.toString())) {
          htData.put(sbKey.toString(), dvalidate == null ? "" : dvalidate);
        }
      }

    }
    finally
    {
      if (rs != null)
        rs.close();
      if (stmt != null)
        stmt.close();
      if (con != null) {
        con.close();
      }
    }
    UFDate invalidate = null;

    for (int i = 0; i < gvo.getItemCount(); i++) {
      sbKey = new StringBuffer(pk_corp);

      for (int j = 0; j < saItemKeys.length; j++) {
        sbKey.append(gvo.getItemValue(i, saItemKeys[j]) == null ? "" : (String)gvo.getItemValue(i, saItemKeys[j]));
      }

      if (htData.containsKey(sbKey.toString())) {
        dvalidate = (String)htData.get(sbKey.toString());
        gvo.setItemValue(i, "dvalidate", new UFDate(dvalidate));

        invalidate = (UFDate)gvo.getItemValue(i, "dvalidate");
        if (dvalidate != null) {
          gvo.setItemValue(i, "scrq", invalidate.getDateBefore(gvo.getItemInv(i).getQualityDay() == null ? 0 : gvo.getItemInv(i).getQualityDay().intValue()));
        }

      }

    }

    return gvo;
  }

  private void queryAllLotNum(String clockbillid, String cwarehouseid, Integer[] iScale, Hashtable htOnhand) throws Exception {
    if ((cwarehouseid == null) || (clockbillid == null) || (clockbillid.trim().length() <= 0) || (htOnhand == null)) {
      return;
    }
    String sql = " SELECT f.pk_corp,f.ccalbodyid,f.cwarehouseid,f.cspaceid,f.cinventoryid, f.vfree1,f.vfree2,f.vfree3,  f.vfree4,f.vfree5, f.vfree6,f.vfree7,f.vfree8,f.vfree9,f.vfree10, f.vbatchcode,s.dvalidate,  f.castunitid,b.measname,f.nfreezenum ,f.nfreezeastnum ,f.cvendorid,f.hsl,f.ngrossnum ,f.cinvbasid , s.cqualitylevelid  FROM ic_freeze f  left outer join scm_batchcode s on f.cinvbasid=s.pk_invbasdoc  and f.vbatchcode=s.vbatchcode  left outer join bd_measdoc b on f.castunitid=b.pk_measdoc  WHERE f.ccorrespondbid = ? and f.cwarehouseid = ? and f.cthawpersonid IS NULL AND f.dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, clockbillid);
      stmt.setString(2, cwarehouseid);
      rs = stmt.executeQuery();

      LotNumbRefVO aLotNumbRefVO = null;
      ArrayList altemp = null;
      UFDouble innum = null; UFDouble inassistnum = null;
      while (rs.next())
      {
        aLotNumbRefVO = new LotNumbRefVO();

        String vbatchcode = rs.getString("vbatchcode");
        aLotNumbRefVO.setVbatchcode(vbatchcode == null ? null : vbatchcode.trim());
        String dvalidate = rs.getString("dvalidate");
        aLotNumbRefVO.setDvalidate(dvalidate == null ? null : new UFDate(dvalidate.trim()));
        Object otemp0 = rs.getObject("nfreezenum");
        innum = otemp0 == null ? new UFDouble("0") : new UFDouble(otemp0.toString(), iScale == null ? 2 : iScale[0].intValue());

        aLotNumbRefVO.setNinnum(innum);

        Object otemp1 = rs.getObject("nfreezeastnum");
        if (otemp1 == null)
          otemp1 = "0";
        inassistnum = otemp1 == null ? new UFDouble("0") : new UFDouble(otemp1.toString(), iScale == null ? 2 : iScale[1].intValue());
        aLotNumbRefVO.setNinassistnum(inassistnum);

        String castunitname = rs.getString("measname");
        aLotNumbRefVO.setCastunitname(castunitname);
        String castunitid = rs.getString("castunitid");
        aLotNumbRefVO.setCastunitid(castunitid);

        String cqualitylevelid = rs.getString("cqualitylevelid");
        aLotNumbRefVO.setCqualitylevelid(cqualitylevelid);

        String vfree1 = rs.getString("vfree1");
        aLotNumbRefVO.setVfree1(vfree1);
        String vfree2 = rs.getString("vfree2");
        aLotNumbRefVO.setVfree2(vfree2);
        String vfree3 = rs.getString("vfree3");
        aLotNumbRefVO.setVfree3(vfree3);
        String vfree4 = rs.getString("vfree4");
        aLotNumbRefVO.setVfree4(vfree4);
        String vfree5 = rs.getString("vfree5");
        aLotNumbRefVO.setVfree5(vfree5);
        String vfree6 = rs.getString("vfree6");
        aLotNumbRefVO.setVfree6(vfree6);
        String vfree7 = rs.getString("vfree7");
        aLotNumbRefVO.setVfree7(vfree7);
        String vfree8 = rs.getString("vfree8");
        aLotNumbRefVO.setVfree8(vfree8);
        String vfree9 = rs.getString("vfree9");
        aLotNumbRefVO.setVfree9(vfree9);
        String vfree10 = rs.getString("vfree10");
        aLotNumbRefVO.setVfree10(vfree10);

        Object otemp = rs.getObject("hsl");
        if (otemp != null) {
          UFDouble hsl = new UFDouble(otemp.toString(), iScale == null ? 2 : iScale[2].intValue());
          aLotNumbRefVO.setHsl(hsl);
        }

        otemp = rs.getObject("ngrossnum");
        if (otemp == null)
          otemp = "0";
        if (otemp != null) {
          UFDouble ngrossnum = new UFDouble(otemp.toString(), iScale == null ? 2 : iScale[0].intValue());
          aLotNumbRefVO.setNgrossnum(ngrossnum);
        }

        aLotNumbRefVO.setOnhandnumType(new Integer(0));

        if (vbatchcode != null) {
          if (htOnhand.containsKey(vbatchcode)) {
            altemp = (ArrayList)htOnhand.get(vbatchcode);
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          } else {
            altemp = new ArrayList();
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          }
        }
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
        SCMEnv.error(e);
      }
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
  }

  protected ArrayList queryAllLotNum(Object[] params, ArrayList FreeItem)
    throws Exception
  {
    String corp = null; String WHID = null; String InvID = null; String AstMeaUnitID = null;
    Integer iSortOrder = null;
    boolean bisWasteWH = false;
    Boolean IsQueryZeroLot = null;

    if (params[0] != null)
      corp = params[0].toString();
    if (params[1] != null)
      WHID = params[1].toString();
    if (params[2] != null)
      InvID = params[2].toString();
    if (params[3] != null)
      AstMeaUnitID = params[3].toString();
    if (params[4] != null) {
      bisWasteWH = ((Boolean)params[4]).booleanValue();
    }
    if (bisWasteWH) {
      return qureyWastWhBatchcode(params, FreeItem);
    }

    iSortOrder = (Integer)params[6];
    if (iSortOrder == null) {
      iSortOrder = queryOutpriorty(InvID);
    }
    if (params[8] != null)
      IsQueryZeroLot = (Boolean)params[8];
    else {
      IsQueryZeroLot = new Boolean(false);
    }
    String StrNowSrcBid = null;
    if (params.length >= 11) {
      StrNowSrcBid = (String)params[10];
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alQueryResult = null;
    LotNumbRefVO aLotNumbRefVO = null;
    UFDouble innum = null; UFDouble inassistnum = null;

    Integer[] iScale = null;
    String[] saParam = { "BD501", "BD502", "BD503" };
    iScale = new Integer[saParam.length];
    for (int i = 0; i < saParam.length; i++) {
      iScale[i] = new Integer(2);
    }

    MiscDMO dmo = new MiscDMO();
    String[] saRetData = dmo.getSysParam(corp, saParam);
    if (saRetData != null)
    {
      for (int i = 0; i < saRetData.length; i++)
      {
        if (saRetData[i] != null) {
          iScale[i] = Integer.valueOf(saRetData[i]);
        }
      }
    }
    StringBuffer sql = new StringBuffer(1024);
    sql.append("select bc.vbatchcode,bc.dvalidate,bc.cqualitylevelid,coalesce(a.nnum,0) nnum,coalesce(a.nastnum,0) nastnum,b.measname,a.castunitid,")
                .append("a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5,a.vfree6,a.vfree7,a.vfree8,a.vfree9,a.vfree10,a.hsl,a.ngrossnum ,a.cspaceid ,a.csname")//add by zwx 2014年12月26日 ,a.cspaceid ,a.csname
                .append(" from scm_batchcode bc ");

    if (IsQueryZeroLot.booleanValue())
      sql.append(" left outer join ");
    else {
      sql.append(" inner join ");
    }
    /*sql.append("(select hand.pk_corp,hand.ccalbodyid,hand.cwarehouseid,hand.cinventoryid,inv.pk_invbasdoc, hand.vbatchcode,hand.castunitid,")
    .append(" hand.vfree1,hand.vfree2,hand.vfree3,hand.vfree4,hand.vfree5,")
    .append(" hand.vfree6,hand.vfree7,hand.vfree8,hand.vfree9,hand.vfree10, ")
    .append(" sum(coalesce(ninspacenum,0)) - sum(coalesce(noutspacenum,0)) as nnum,")
    .append(" sum(coalesce(ninspaceassistnum,0)) - sum(coalesce(noutspaceassistnum,0)) as nastnum")
    .append(" ,hand.hsl,sum(hand.ngrossnum) as ngrossnum ")
    .append(" from v_ic_onhandnum3 hand left outer join bd_cargdoc car")
    .append(" on hand.cspaceid=car.pk_cargdoc ")
    .append(" left outer join bd_invmandoc inv on hand.cinventoryid=inv.pk_invmandoc ")
    .append(" where hand.cinventoryid=? and hand.cwarehouseid=? and hand.pk_corp=? ")
    .append(" and (car.isrmplace<>'Y' or car.isrmplace is null) ");*/
    
    sql.append("(select hand.pk_corp,hand.ccalbodyid,hand.cwarehouseid,hand.cinventoryid,inv.pk_invbasdoc, hand.vbatchcode,hand.castunitid,")
    .append(" hand.vfree1,hand.vfree2,hand.vfree3,hand.vfree4,hand.vfree5,")
    .append(" hand.vfree6,hand.vfree7,hand.vfree8,hand.vfree9,hand.vfree10, ")
    .append(" sum(coalesce(ninspacenum,0)) - sum(coalesce(noutspacenum,0)) as nnum,")
    .append(" sum(coalesce(ninspaceassistnum,0)) - sum(coalesce(noutspaceassistnum,0)) as nastnum")
    .append(" ,hand.hsl,sum(hand.ngrossnum) as ngrossnum,hand.cspaceid,car.csname ")//edit by zwx 2014-12-26 ,hand.cspaceid,car.csname
    .append(" from v_ic_onhandnum3 hand left outer join bd_cargdoc car")
    .append(" on hand.cspaceid=car.pk_cargdoc ")
    .append(" left outer join bd_invmandoc inv on hand.cinventoryid=inv.pk_invmandoc ")
    .append(" where hand.cinventoryid=? and hand.cwarehouseid=? and hand.pk_corp=? ")
    .append(" and (car.isrmplace<>'Y' or car.isrmplace is null) ");

    if ((FreeItem != null) && (FreeItem.size() > 0)) {
      for (int x = 0; x < 10; x++) {
        if ((FreeItem.get(x) != null) && (FreeItem.get(x).toString().trim().length() > 0)) {
          sql.append(" and hand.vfree" + (x + 1) + "= ? ");
        }
      }
    }
    if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0))
      sql.append(" and hand.castunitid=?");
    sql.append(" group by hand.pk_corp,hand.ccalbodyid,hand.cwarehouseid,hand.cinventoryid,inv.pk_invbasdoc,")
    .append("hand.vbatchcode,hand.castunitid,hand.vfree1,hand.vfree2,hand.vfree3,hand.vfree4,hand.vfree5,")
    .append("hand.vfree6,hand.vfree7,hand.vfree8,hand.vfree9,hand.vfree10,hand.hsl,hand.cspaceid,car.csname ) a ");//add by zwx 2014年12月26日 ,hand.cspaceid ,car.csname

    sql.append(" on bc.pk_invbasdoc=a.pk_invbasdoc and bc.vbatchcode=a.vbatchcode ")
    .append(" left outer join bd_measdoc b on a.castunitid=b.pk_measdoc ")
    .append(" where bc.dr=0 and isnull(bc.bseal,'N')='N' and bc.pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc=?) ");

    if (!IsQueryZeroLot.booleanValue()) {
      sql.append(" and a.nnum>0 or a.nastnum>0");
    }

    /*StringBuffer sql1 = new StringBuffer("select bc.vbatchcode from scm_batchcode bc ");
    sql1.append(" left outer join ").append("(select b.vbatchcode,b.dbizdate from ic_general_b b inner join ic_general_h h on h.cgeneralhid=b.cgeneralhid ").append(" where b.cinventoryid=? and h.cwarehouseid=? and h.pk_corp=? ").append(" and b.dr=0 ) a ").append(" on bc.vbatchcode=a.vbatchcode ").append(" where bc.pk_invbasdoc =(select pk_invbasdoc from bd_invmandoc where pk_invmandoc =?) ");
    if (iSortOrder.intValue() == 0)
    {
      sql1.append(" ORDER BY a.dbizdate ASC");
    }
    else if (iSortOrder.intValue() == 1)
    {
      sql1.append(" ORDER BY a.dbizdate DESC");
    }*/
    
    //edit by zwx 2019年3月26日 rs结果集只接收100000行数据，超出无法返回到rs中
    StringBuffer sql1 = new StringBuffer("select distinct vbatchcode from( select bc.vbatchcode from scm_batchcode bc ");
    sql1.append(" left outer join ").append("(select b.vbatchcode,b.dbizdate from ic_general_b b inner join ic_general_h h on h.cgeneralhid=b.cgeneralhid ").append(" where b.cinventoryid=? and h.cwarehouseid=? and h.pk_corp=? ").append(" and b.dr=0 ) a ").append(" on bc.vbatchcode=a.vbatchcode ").append(" where bc.pk_invbasdoc =(select pk_invbasdoc from bd_invmandoc where pk_invmandoc =?) ");
   
    if (iSortOrder.intValue() == 0)
    {
      sql1.append(" ORDER BY a.dbizdate ASC)");
    }
    else if (iSortOrder.intValue() == 1)
    {
      sql1.append(" ORDER BY a.dbizdate DESC)");
    }
    //end by zwx
   
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, InvID);
      stmt.setString(2, WHID);
      stmt.setString(3, corp);

      int iINDEX = 4;

      if ((FreeItem != null) && (FreeItem.size() > 0))
      {
        for (int w = 0; w < 10; w++)
        {
          if ((FreeItem.get(w) == null) || (FreeItem.get(w).toString().trim().length() <= 0))
            continue;
          stmt.setString(iINDEX++, FreeItem.get(w).toString());
        }

      }

      if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0))
      {
        stmt.setString(iINDEX++, AstMeaUnitID);
      }
      stmt.setString(iINDEX++, InvID);

      rs = stmt.executeQuery();
      alQueryResult = new ArrayList();
      Hashtable htOnhand = new Hashtable();
      ArrayList altemp = null;
      while (rs.next())
      {
        aLotNumbRefVO = new LotNumbRefVO();

        String vbatchcode = rs.getString("vbatchcode");
        aLotNumbRefVO.setVbatchcode(vbatchcode == null ? null : vbatchcode.trim());
        String dvalidate = rs.getString("dvalidate");
        aLotNumbRefVO.setDvalidate(dvalidate == null ? null : new UFDate(dvalidate.trim()));
        Object otemp0 = rs.getObject("nnum");
        innum = otemp0 == null ? new UFDouble("0") : new UFDouble(otemp0.toString(), iScale == null ? 2 : iScale[0].intValue());

        aLotNumbRefVO.setNinnum(innum);

        Object otemp1 = rs.getObject("nastnum");
        inassistnum = otemp1 == null ? new UFDouble("0") : new UFDouble(otemp1.toString(), iScale == null ? 2 : iScale[1].intValue());
        aLotNumbRefVO.setNinassistnum(inassistnum);

        String castunitname = rs.getString("measname");
        aLotNumbRefVO.setCastunitname(castunitname);
        String castunitid = rs.getString("castunitid");
        aLotNumbRefVO.setCastunitid(castunitid);

        String cqualitylevelid = rs.getString("cqualitylevelid");
        aLotNumbRefVO.setCqualitylevelid(cqualitylevelid);

        String vfree1 = rs.getString("vfree1");
        aLotNumbRefVO.setVfree1(vfree1);
        String vfree2 = rs.getString("vfree2");
        aLotNumbRefVO.setVfree2(vfree2);
        String vfree3 = rs.getString("vfree3");
        aLotNumbRefVO.setVfree3(vfree3);
        String vfree4 = rs.getString("vfree4");
        aLotNumbRefVO.setVfree4(vfree4);
        String vfree5 = rs.getString("vfree5");
        aLotNumbRefVO.setVfree5(vfree5);
        String vfree6 = rs.getString("vfree6");
        aLotNumbRefVO.setVfree6(vfree6);
        String vfree7 = rs.getString("vfree7");
        aLotNumbRefVO.setVfree7(vfree7);
        String vfree8 = rs.getString("vfree8");
        aLotNumbRefVO.setVfree8(vfree8);
        String vfree9 = rs.getString("vfree9");
        aLotNumbRefVO.setVfree9(vfree9);
        String vfree10 = rs.getString("vfree10");
        aLotNumbRefVO.setVfree10(vfree10);

        Object otemp = rs.getObject("hsl");
        //add by zwx 2014-12-26 货位id 货位名称
        String cspaceid = rs.getString("cspaceid");
        aLotNumbRefVO.setCspaceid(cspaceid);
        
        String csname= rs.getString("csname");
        aLotNumbRefVO.setCsname(csname);
        //end by zwx
        if (otemp != null) {
          UFDouble hsl = new UFDouble(otemp.toString(), iScale == null ? 2 : iScale[2].intValue());
          aLotNumbRefVO.setHsl(hsl);
        }

        otemp = rs.getObject("ngrossnum");
        if (otemp != null) {
          UFDouble ngrossnum = new UFDouble(otemp.toString(), iScale == null ? 2 : iScale[0].intValue());
          aLotNumbRefVO.setNgrossnum(ngrossnum);
        }

        aLotNumbRefVO.setOnhandnumType(new Integer(0));

        if (vbatchcode != null) {
          if (htOnhand.containsKey(vbatchcode)) {
            altemp = (ArrayList)htOnhand.get(vbatchcode);
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          } else {
            altemp = new ArrayList();
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          }

        }

      }

      queryAllLotNum(StrNowSrcBid, WHID, iScale, htOnhand);

      stmt = con.prepareStatement(sql1.toString());
      stmt.setString(1, InvID);
      stmt.setString(2, WHID);
      stmt.setString(3, corp);
      stmt.setString(4, InvID);

      rs = stmt.executeQuery();
      String vbatchcode = null;
      Hashtable htTemp = new Hashtable();
      ArrayList albatch = new ArrayList();
      while (rs.next()) {
        vbatchcode = rs.getString(1);
        if ((vbatchcode != null) && (!htTemp.containsKey(vbatchcode))) {
          albatch.add(vbatchcode);
          htTemp.put(vbatchcode, vbatchcode);
        }

      }

      Hashtable htOutZron = queryAllLotNumOutZorn(params, FreeItem, iScale);

      int size = albatch.size();

      for (int i = 0; i < size; i++) {
        vbatchcode = (String)albatch.get(i);
        if (htOnhand.containsKey(vbatchcode)) {
          altemp = (ArrayList)htOnhand.get(vbatchcode);
          for (int j = 0; j < altemp.size(); j++)
            alQueryResult.add(altemp.get(j));
          if (htOutZron.containsKey(vbatchcode)) {
            altemp = (ArrayList)htOutZron.get(vbatchcode);
            for (int k = 0; k < altemp.size(); k++)
              alQueryResult.add(altemp.get(k));
          }
        }
        else if (htOutZron.containsKey(vbatchcode)) {
          altemp = (ArrayList)htOutZron.get(vbatchcode);
          for (int k = 0; k < altemp.size(); k++) {
            alQueryResult.add(altemp.get(k));
          }
        }

      }

    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
        SCMEnv.error(e);
      }
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return alQueryResult;
  }

  protected ArrayList qureyWastWhBatchcode(Object[] params, ArrayList FreeItem)
    throws Exception
  {
    String corp = null;
    String WHID = null;
    String InvID = null;
    String AstMeaUnitID = null;
    Integer iSortOrder = null;
    Boolean IsQueryZeroLot = null;

    if (params[0] != null)
      corp = params[0].toString();
    if (params[1] != null)
      WHID = params[1].toString();
    if (params[2] != null)
      InvID = params[2].toString();
    if (params[3] != null)
      AstMeaUnitID = params[3].toString();
    iSortOrder = params[6] == null ? new Integer(0) : (Integer)params[6];

    if (params[8] != null)
      IsQueryZeroLot = (Boolean)params[8];
    else {
      IsQueryZeroLot = new Boolean(false);
    }

    StringBuffer sbSQL = new StringBuffer();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alQueryResult = null;
    LotNumbRefVO aLotNumbRefVO = null;
    UFDouble innum = null; UFDouble inassistnum = null;

    Integer[] iScale = null;

    String[] saParam = { "BD501", "BD502", "BD503" };
    iScale = new Integer[saParam.length];
    for (int i = 0; i < saParam.length; i++) {
      iScale[i] = new Integer(2);
    }

    MiscDMO dmo = new MiscDMO();

    String[] saRetData = dmo.getSysParam(corp, saParam);

    if (saRetData != null) {
      for (int i = 0; i < saRetData.length; i++) {
        if (saRetData[i] != null) {
          iScale[i] = Integer.valueOf(saRetData[i]);
        }
      }
    }

    sbSQL.append("select bc.vbatchcode,bc.dvalidate,bc.cqualitylevelid, innum,inassistnum,measname,CASTUNITID,VFREE1, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VFREE10").append(" from scm_batchcode bc ");

    if (IsQueryZeroLot.booleanValue())
      sbSQL.append(" left outer join ");
    else {
      sbSQL.append(" inner join ");
    }
    sbSQL.append("(").append(" SELECT vbatchcode,  ");

    sbSQL.append("SUM(COALESCE (ninnum, 0.0)) - SUM(COALESCE (noutnum, 0.0)) as innum, ");

    sbSQL.append("SUM(COALESCE (ninassistnum, 0.0)) - SUM(COALESCE (noutassistnum, 0.0)) as inassistnum,");

    sbSQL.append(" bd_measdoc.measname , min(daccountdate) as daccountdate,CASTUNITID,VFREE1, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VFREE10  ").append(" ,cinvbasid ").append(" FROM v_ic_waste_refbook ref LEFT OUTER JOIN bd_measdoc ").append(" ON ref.castunitid = bd_measdoc.pk_measdoc ").append(" LEFT OUTER JOIN bd_billtype b ON(ref.cbilltypecode = b.pk_billtypecode) ").append(" WHERE ref.pk_corp = ? and cwastewarehouseid = ? and cinventoryid = ? ");

    if ((FreeItem != null) && (FreeItem.size() > 0)) {
      for (int x = 0; x < 10; x++) {
        if ((FreeItem.get(x) == null) || (FreeItem.get(x).toString().trim().length() <= 0))
        {
          continue;
        }
        sbSQL.append(" and vfree" + (x + 1) + "= ? ");
      }

    }

    if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0))
    {
      sbSQL.append(" and castunitid= ?  ");
    }
    sbSQL.append(" GROUP BY vbatchcode, bd_measdoc.measname,CASTUNITID,VFREE1, VFREE2, VFREE3, VFREE4, VFREE5, VFREE6, VFREE7, VFREE8, VFREE9, VFREE10,cinvbasid ");
    sbSQL.append(" ) v2 on bc.pk_invbasdoc=v2.cinvbasid and bc.vbatchcode=v2.vbatchcode ");
    sbSQL.append(" inner join bd_invmandoc inv on bc.pk_invbasdoc = inv.pk_invbasdoc ");
    sbSQL.append(" where inv.pk_corp = '" + corp + "' and inv.pk_invmandoc = '" + InvID + "' ");

    if (!IsQueryZeroLot.booleanValue()) {
      sbSQL.append(" and innum>0 or inassistnum>0");
    }

    if (iSortOrder.intValue() == 0) {
      sbSQL.append(" ORDER BY v2.daccountdate ASC");
    }
    else if (iSortOrder.intValue() == 1)
      sbSQL.append(" ORDER BY v2.daccountdate DESC");
    else
      sbSQL.append(" ORDER BY bc.dvalidate ASC ");
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSQL.toString());
      stmt.setString(1, corp);
      stmt.setString(2, WHID);
      stmt.setString(3, InvID);
      int iINDEX = 4;

      if ((FreeItem != null) && (FreeItem.size() > 0)) {
        for (int w = 0; w < 10; w++) {
          if ((FreeItem.get(w) == null) || (FreeItem.get(w).toString().trim().length() <= 0))
            continue;
          stmt.setString(iINDEX++, FreeItem.get(w).toString());
        }

      }

      if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0))
      {
        stmt.setString(iINDEX++, AstMeaUnitID);
      }

      rs = stmt.executeQuery();
      alQueryResult = new ArrayList();

      while (rs.next()) {
        aLotNumbRefVO = new LotNumbRefVO();

        String vbatchcode = rs.getString("vbatchcode");
        aLotNumbRefVO.setVbatchcode(vbatchcode == null ? null : vbatchcode.trim());
        String dvalidate = rs.getString("dvalidate");
        aLotNumbRefVO.setDvalidate(dvalidate == null ? null : new UFDate(dvalidate.trim()));
        String cqualitylevelid = rs.getString("cqualitylevelid");
        aLotNumbRefVO.setCqualitylevelid(cqualitylevelid == null ? null : cqualitylevelid.trim());

        Object otemp0 = rs.getObject("innum");
        innum = otemp0 == null ? new UFDouble("0") : new UFDouble(otemp0.toString(), iScale == null ? 2 : iScale[0].intValue());

        aLotNumbRefVO.setNinnum(innum);

        Object otemp1 = rs.getObject("inassistnum");
        inassistnum = otemp1 == null ? new UFDouble("0") : new UFDouble(otemp1.toString(), iScale == null ? 2 : iScale[1].intValue());

        aLotNumbRefVO.setNinassistnum(inassistnum);

        String castunitname = rs.getString("measname");
        aLotNumbRefVO.setCastunitname(castunitname);
        String castunitid = rs.getString("castunitid");
        aLotNumbRefVO.setCastunitid(castunitid);

        if (castunitid == null) {
          aLotNumbRefVO.setNinassistnum(null);
        }

        String vfree1 = rs.getString("vfree1");
        aLotNumbRefVO.setVfree1(vfree1);
        String vfree2 = rs.getString("vfree2");
        aLotNumbRefVO.setVfree2(vfree2);
        String vfree3 = rs.getString("vfree3");
        aLotNumbRefVO.setVfree3(vfree3);
        String vfree4 = rs.getString("vfree4");
        aLotNumbRefVO.setVfree4(vfree4);
        String vfree5 = rs.getString("vfree5");
        aLotNumbRefVO.setVfree5(vfree5);
        String vfree6 = rs.getString("vfree6");
        aLotNumbRefVO.setVfree6(vfree6);
        String vfree7 = rs.getString("vfree7");
        aLotNumbRefVO.setVfree7(vfree7);
        String vfree8 = rs.getString("vfree8");
        aLotNumbRefVO.setVfree8(vfree8);
        String vfree9 = rs.getString("vfree9");
        aLotNumbRefVO.setVfree9(vfree9);
        String vfree10 = rs.getString("vfree10");
        aLotNumbRefVO.setVfree10(vfree10);

        alQueryResult.add(aLotNumbRefVO);
      }

    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
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
      catch (Exception e) {
      }
    }
    return alQueryResult;
  }

  protected Hashtable queryAllLotNumOutZorn(Object[] params, ArrayList FreeItem, Integer[] iScale)
    throws Exception
  {
    Hashtable htOnhand = new Hashtable();
    String WHID = null; String InvID = null; String AstMeaUnitID = null;

    if (params[1] != null)
      WHID = params[1].toString();
    if (params[2] != null)
      InvID = params[2].toString();
    if (params[3] != null) {
      AstMeaUnitID = params[3].toString();
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    LotNumbRefVO aLotNumbRefVO = null;
    UFDouble innum = null; UFDouble inassistnum = null;

    StringBuffer sql2 = new StringBuffer(1024);
    sql2.append("select a.vlotb as vbatchcode,a.nnum,a.nastnum,b.measname,a.castunitidb as castunitid,").append("a.vfreeb1 as vfree1,a.vfreeb2 as vfree2,a.vfreeb3 as vfree3,a.vfreeb4 as vfree4,a.vfreeb5 as vfree5,a.vfreeb6 as vfree6,a.vfreeb7 as vfree7,a.vfreeb8 as vfree8,a.vfreeb9 as vfree9,a.vfreeb10 as vfree10 ").append(" from ").append("(select hand.cwarehouseidb,hand.cinventoryidb,").append(" hand.vlotb,hand.castunitidb,").append(" hand.vfreeb1,hand.vfreeb2,hand.vfreeb3,hand.vfreeb4,hand.vfreeb5,").append(" hand.vfreeb6,hand.vfreeb7,hand.vfreeb8,hand.vfreeb9,hand.vfreeb10, ").append(" sum(coalesce(nnum,0)) as nnum,").append(" sum(coalesce(nastnum,0)) as nastnum").append(" from ic_onhandnum_b hand left outer join bd_cargdoc car").append(" on hand.cspaceid=car.pk_cargdoc ").append(" where hand.cinventoryidb=? and hand.cwarehouseidb=?").append(" and car.isrmplace='Y' ");

    if ((FreeItem != null) && (FreeItem.size() > 0)) {
      for (int x = 0; x < 10; x++) {
        if ((FreeItem.get(x) != null) && (FreeItem.get(x).toString().trim().length() > 0)) {
          sql2.append(" and hand.vfreeb" + (x + 1) + "= ? ");
        }
      }
    }
    if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0))
      sql2.append(" and hand.castunitidb=?");
    sql2.append(" group by hand.cwarehouseidb,hand.cinventoryidb,").append("hand.vlotb,hand.castunitidb,hand.vfreeb1,hand.vfreeb2,hand.vfreeb3,hand.vfreeb4,hand.vfreeb5,").append("hand.vfreeb6,hand.vfreeb7,hand.vfreeb8,hand.vfreeb9,hand.vfreeb10) a left outer join bd_measdoc b on a.castunitidb=b.pk_measdoc");
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql2.toString());
      stmt.setString(1, InvID);
      stmt.setString(2, WHID);

      int iINDEX = 3;

      if ((FreeItem != null) && (FreeItem.size() > 0)) {
        for (int w = 0; w < 10; w++) {
          if ((FreeItem.get(w) != null) && (FreeItem.get(w).toString().trim().length() > 0)) {
            stmt.setString(iINDEX++, FreeItem.get(w).toString());
          }
        }
      }

      if ((AstMeaUnitID != null) && (AstMeaUnitID.trim().length() > 0)) {
        stmt.setString(iINDEX++, AstMeaUnitID);
      }

      rs = stmt.executeQuery();
      ArrayList altemp = null;
      while (rs.next()) {
        aLotNumbRefVO = new LotNumbRefVO();

        String vbatchcode = rs.getString("vbatchcode");
        aLotNumbRefVO.setVbatchcode(vbatchcode == null ? null : vbatchcode.trim());

        Object otemp0 = rs.getObject("nnum");
        innum = otemp0 == null ? new UFDouble("0") : new UFDouble(otemp0.toString(), iScale == null ? 2 : iScale[0].intValue());

        aLotNumbRefVO.setNinnum(innum);

        Object otemp1 = rs.getObject("nastnum");
        inassistnum = otemp1 == null ? new UFDouble("0") : new UFDouble(otemp1.toString(), iScale == null ? 2 : iScale[1].intValue());

        aLotNumbRefVO.setNinassistnum(inassistnum);

        String castunitname = rs.getString("measname");
        aLotNumbRefVO.setCastunitname(castunitname);
        String castunitid = rs.getString("castunitid");
        aLotNumbRefVO.setCastunitid(castunitid);

        String vfree1 = rs.getString("vfree1");
        aLotNumbRefVO.setVfree1(vfree1);

        String vfree2 = rs.getString("vfree2");
        aLotNumbRefVO.setVfree2(vfree2);

        String vfree3 = rs.getString("vfree3");
        aLotNumbRefVO.setVfree3(vfree3);

        String vfree4 = rs.getString("vfree4");
        aLotNumbRefVO.setVfree4(vfree4);

        String vfree5 = rs.getString("vfree5");
        aLotNumbRefVO.setVfree5(vfree5);

        String vfree6 = rs.getString("vfree6");
        aLotNumbRefVO.setVfree6(vfree6);

        String vfree7 = rs.getString("vfree7");
        aLotNumbRefVO.setVfree7(vfree7);

        String vfree8 = rs.getString("vfree8");
        aLotNumbRefVO.setVfree8(vfree8);

        String vfree9 = rs.getString("vfree9");
        aLotNumbRefVO.setVfree9(vfree9);

        String vfree10 = rs.getString("vfree10");
        aLotNumbRefVO.setVfree10(vfree10);

        aLotNumbRefVO.setOnhandnumType(new Integer(1));

        if (vbatchcode != null)
          if (htOnhand.containsKey(vbatchcode)) {
            altemp = (ArrayList)htOnhand.get(vbatchcode);
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          } else {
            altemp = new ArrayList();
            altemp.add(aLotNumbRefVO);
            htOnhand.put(vbatchcode, altemp);
          }
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
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
      catch (Exception e) {
      }
    }
    return htOnhand;
  }

  private Integer queryOutpriorty(String cinventoryid)
    throws Exception
  {
    if (cinventoryid == null)
      return new Integer(0);
    String sql = "select outpriority from bd_invmandoc where pk_invmandoc=?";
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int iresult = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, cinventoryid);
      rs = stmt.executeQuery();
      if (rs.next())
        iresult = rs.getInt(1);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
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
      catch (Exception e) {
      }
    }
    return new Integer(iresult);
  }

  protected ArrayList queryAllLot(String[] params)
    throws SQLException
  {
    String InvID = null;

    if (params[0] != null) {
      InvID = params[0];
    }

    StringBuffer sbSQL = new StringBuffer();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alQueryResult = null;

    sbSQL.append(" select distinct vbatchcode ").append(" from( ").append(" select vbatchcode,cinventoryid ").append(" ,sum(isnull(ninnum,0))-sum(isnull(noutnum,0)) as restnum ").append(" from ic_keep_detail1  ").append(" group by vbatchcode,cinventoryid ").append(" )aa ").append(" Where cinventoryid = ? and restnum>=0 ");
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSQL.toString());

      if (InvID == null) {
        stmt.setNull(1, 1);
      }
      else {
        stmt.setString(1, InvID);
      }

      rs = stmt.executeQuery();
      alQueryResult = new ArrayList();
      while (rs.next()) {
        String sLot = rs.getString("vbatchcode");
        if (sLot != null)
          sLot = sLot.trim();
        alQueryResult.add(sLot);
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
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
      catch (Exception e) {
      }
    }
    return alQueryResult;
  }
}