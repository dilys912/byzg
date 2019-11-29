package nc.bs.ic.pub.ictodm;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.ic291.ABInATP;
import nc.bs.ic.ic291.ABOutATP;
import nc.bs.ic.pub.RewriteDMO;
import nc.bs.ic.pub.bill.GeneralBillBO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.check.CheckDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.itf.dm.service.IDMToIC;
import nc.itf.ic.service.IICToDM;
import nc.itf.so.service.ISOToIC_DRP;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ic.ic291.AllocationHHeaderVO;
import nc.vo.ic.ic291.AllocationHItemVO;
import nc.vo.ic.ic291.AllocationHVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.ICGenVO;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.sort.SortMethod;

public class IC2DMInterface extends DataManageObject
  implements IICToDM
{
  private ArrayList m_alClosedbid = new ArrayList();
  private ArrayList m_alOpenedbid = new ArrayList();
private AggregatedValueObject vo;

  public IC2DMInterface()
    throws NamingException, SystemException
  {
  }

  public IC2DMInterface(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void checkAlloBillNum(ArrayList alParams)
    throws BusinessException
  {
    if (alParams == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000029"));
    }

    String SQL = " select isnull(nshouldnum,0.0)-isnull(noutnum,0.0) as nrestnum  from ic_allocation_b where dr = 0  And callocationhid =? And callocationbid = ?   ";
    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    String pk_corp = (String)((ArrayList)alParams.get(0)).get(3);
    if (pk_corp == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000030"));
    }

    try
    {
      String sIsBigSave = getICParaString(pk_corp, "IC008").trim();
      UFDouble ufdBigPermit = new UFDouble("1");
      if (sIsBigSave.equals("Y"))
      {
        ufdBigPermit = getICParaUFDouble(pk_corp, "IC009").div(new UFDouble("100"));
      }
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < alParams.size(); i++)
      {
        String sHid = (String)((ArrayList)alParams.get(i)).get(0);
        String sBid = (String)((ArrayList)alParams.get(i)).get(1);
        UFDouble nshouldnum = ((ArrayList)alParams.get(i)).get(2) == null ? new UFDouble(0) : (UFDouble)((ArrayList)alParams.get(i)).get(2);
        if ((sHid == null) || (sBid == null))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000029"));
        stmt.setString(1, sHid);

        stmt.setString(2, sBid);

        rs = stmt.executeQuery();
        while (rs.next())
        {
          Object oTemp = rs.getObject("nrestnum");
          UFDouble uTemp = new UFDouble(oTemp == null ? "0" : oTemp.toString().trim());
          if (sIsBigSave.equals("Y")) {
            uTemp = uTemp.multiply(new UFDouble(1).add(ufdBigPermit));
          }
          if (uTemp.doubleValue() < nshouldnum.doubleValue()) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000031") + uTemp.toString() + NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000032") + nshouldnum.toString());
          }
        }

      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
    finally
    {
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

  public void checkAlloBillOnWayNum(ArrayList alParams)
    throws BusinessException
  {
    if (alParams == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000029"));
    }

    String SQL = " select isnull(noutnum,0.0)-isnull(ninnum,0.0) as nrestnum  from ic_allocation_b where dr = 0  And callocationhid =? And callocationbid = ?   ";
    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < alParams.size(); i++)
      {
        String sHid = (String)((ArrayList)alParams.get(i)).get(0);
        String sBid = (String)((ArrayList)alParams.get(i)).get(1);
        UFDouble nshouldnum = ((ArrayList)alParams.get(i)).get(2) == null ? new UFDouble(0) : (UFDouble)((ArrayList)alParams.get(i)).get(2);
        if ((sHid == null) || (sBid == null))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000029"));
        stmt.setString(1, sHid);

        stmt.setString(2, sBid);

        rs = stmt.executeQuery();
        while (rs.next())
        {
          Object oTemp = rs.getObject("nrestnum");
          UFDouble uTemp = new UFDouble(oTemp == null ? "0" : oTemp.toString().trim());

          if (uTemp.doubleValue() + nshouldnum.doubleValue() < 0.0D) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000033") + uTemp.toString() + NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000032") + nshouldnum.toString());
          }
        }

      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
    finally
    {
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

  public void checkAlloBillOutNum(ArrayList alParams)
    throws BusinessException
  {
    if (alParams == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000034"));
    }

    String SQL = " select isnull(noutnum,0.0) as nrestnum  from ic_allocation_b where dr = 0  And callocationhid =? And callocationbid = ?   ";
    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < alParams.size(); i++)
      {
        String sHid = (String)((ArrayList)alParams.get(i)).get(0);
        String sBid = (String)((ArrayList)alParams.get(i)).get(1);
        UFDouble nshouldnum = ((ArrayList)alParams.get(i)).get(2) == null ? new UFDouble(0) : (UFDouble)((ArrayList)alParams.get(i)).get(2);
        if ((sHid == null) || (sBid == null))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000034"));
        stmt.setString(1, sHid);

        stmt.setString(2, sBid);

        rs = stmt.executeQuery();
        while (rs.next())
        {
          Object oTemp = rs.getObject("nrestnum");
          UFDouble uTemp = new UFDouble(oTemp == null ? "0" : oTemp.toString().trim());

          if (uTemp.doubleValue() + nshouldnum.doubleValue() < 0.0D) {
            throw new BusinessException("调拨订单累计出库数量出现负数！调拨订单累计出库数量为：" + uTemp.toString() + " 本次调拨数量为：" + nshouldnum.toString());
          }
        }

      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
    finally
    {
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

  private void checkAlloBillStatus(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000034"));
    String SQL = " select fbillflag from ic_allocation_h where callocationhid =?   ";

    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < dmvo.length; i++)
      {
        if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(1, dmvo[i].getAttributeValue("pkbillh").toString());
        else {
          stmt.setNull(1, 12);
        }

        rs = stmt.executeQuery();
        while (rs.next()) {
          Object oTemp = rs.getObject(1);
          if ((dmvo[i].getStatus() == 2) && (oTemp != null) && (!oTemp.toString().equals("3")))
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000035"));
          }if ((dmvo[i].getStatus() == 1) && (oTemp != null) && (!oTemp.toString().equals("3")) && (!oTemp.toString().equals("6")))
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000036"));
          }
        }
      }
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
    finally
    {
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
  }

  private void computeOutSignNum(ArrayList alResult, UFDouble nsignnum)
  {
    boolean bRestNumNull = false;
    if ((alResult != null) && (nsignnum != null))
    {
      int outbillsize = alResult.size();
      for (int i = 0; i < outbillsize; i++)
      {
        ArrayList alBid = (ArrayList)alResult.get(i);
        UFDouble noutnum = alBid.get(2) == null ? new UFDouble(0) : (UFDouble)alBid.get(2);

        if (!bRestNumNull)
        {
          nsignnum = nsignnum.sub(noutnum);
          if (nsignnum.doubleValue() > 0.0D)
          {
            if (outbillsize - 1 != i)
              continue;
            alBid.set(2, nsignnum.add(noutnum));
          }
          else
          {
            alBid.set(2, nsignnum.add(noutnum));
            bRestNumNull = true;
          }

        }
        else
        {
          alBid.set(2, null);
        }
      }
    }
  }

  private void computeOutSignNumNew(ArrayList alResult, UFDouble nsignnum)
  {
    if ((alResult != null) && (nsignnum != null))
    {
      for (int i = 0; i < alResult.size(); i++) {
        ArrayList alBid = (ArrayList)alResult.get(i);
        UFDouble nrestnum = alBid.get(2) == null ? new UFDouble(0) : (UFDouble)alBid.get(2);

        if ((nrestnum.doubleValue() > 0.0D) && (alResult.size() - 1 != i))
        {
          if (Math.abs(nrestnum.doubleValue()) >= Math.abs(nsignnum.doubleValue()))
          {
            alBid.set(2, nsignnum);

            nsignnum = new UFDouble(0);
          }
          else
          {
            nsignnum = nsignnum.sub(nrestnum);
          }

        }
        else if (alResult.size() - 1 == i)
          alBid.set(2, nsignnum);
      }
    }
  }

  private ArrayList filterUselessUOM(ArrayList alResultData)
  {
    if ((alResultData == null) || (alResultData.size() == 0)) {
      return null;
    }
    AllocationHItemVO[] voaTempBillItem = null;
    for (int bill = 0; bill < alResultData.size(); bill++) {
      voaTempBillItem = ((AllocationHVO)alResultData.get(bill)).getItemVOs();
      for (int item = 0; item < voaTempBillItem.length; item++)
      {
        if ((voaTempBillItem[item].getCastunitid() == null) || (voaTempBillItem[item].getCastunitid().trim().length() == 0)) {
          voaTempBillItem[item].setDhsl(null);
        }
      }

    }

    return alResultData;
  }

  private AllocationHVO[] getABVOs(DMDataVO[] datavos, boolean bIsClosedBill)
    throws Exception
  {
    StringBuffer sbWhere = null;
    if ((bIsClosedBill) && (this.m_alOpenedbid.size() > 0)) {
      sbWhere = new StringBuffer();
      sbWhere.append(" body.callocationbid='").append((String)this.m_alOpenedbid.get(0)).append("'");
      for (int i = 1; i < this.m_alOpenedbid.size(); i++) {
        sbWhere.append(" or body.callocationbid='").append((String)this.m_alOpenedbid.get(i)).append("'");
      }

    }
    else if ((!bIsClosedBill) && (this.m_alClosedbid.size() > 0)) {
      sbWhere = new StringBuffer();
      sbWhere.append(" body.callocationbid='").append((String)this.m_alClosedbid.get(0)).append("'");
      for (int i = 1; i < this.m_alClosedbid.size(); i++) {
        sbWhere.append(" or body.callocationbid='").append((String)this.m_alClosedbid.get(i)).append("'");
      }

    }

    if (sbWhere == null) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT DISTINCT ");
    sbSql.append(" head.callocationhid as callocationhidh, head.cbilltypecode,   \n");
    sbSql.append(" head.cincalbodyid, head.coutcalbodyid,  head.dbilldate,  \n");
    sbSql.append(" head.pk_corp as pk_corph, head.vbillcode, head.vnote, head.pk_sendtype,  \n");
    sbSql.append(" head.cauditorid, head.coperatorid,  head.dauditordate,head.fbillflag, head.ts AS tsh, \n");
    sbSql.append("  body.castunitid, body.dplanindate,body.dplanoutdate, body.cinventoryid,   \n");
    sbSql.append("  body.csourcebillbid, body.csourcebillhid,  body.csourcetype, body.pk_corp, \n");
    sbSql.append("  body.callocationbid, body.callocationhid, body.cinwarehouseid,    \n");
    sbSql.append("  body.coutwarehouseid, body.dvalidate,  body.fbillrowflag, body.nshouldastnum,  \n");
    sbSql.append("  body.nshouldnum, body.ninnum,body.nsignnum,  \n");
    sbSql.append("  body.noutnum, body.nprice, body.nmny,body.ndelivernum,body.nonwaynum, body.nwithdrawnum, \n");
    sbSql.append("  body.vbatchcode, body.vfree1, body.vfree2, body.vfree3,  \n");
    sbSql.append("  body.vfree4, body.vfree5, body.vfree6, body.vfree7, body.vfree8, body.vfree9,  \n");
    sbSql.append("  body.vfree10, body.vsourcebillcode,body.vnote as vnotebody, body.ts AS ts \n");
    sbSql.append(" FROM ic_allocation_h head  ");
    sbSql.append(" INNER JOIN  ic_allocation_b body \n");
    sbSql.append(" ON head.callocationhid = body.callocationhid \n");
    sbSql.append(" Where head.fbillflag = 3 AND head.dr=0 AND body.dr=0 \n ");
    if (sbWhere != null)
    {
      sbSql.append(" And (" + sbWhere.toString() + ")");
    }

    Connection con = null;
    PreparedStatement stmt = null;

    ArrayList alResultData = new ArrayList();
    ResultSet rs = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      Vector vResultItemData = null;
      UFDouble dZero = new UFDouble(0);

      AllocationHVO voBill = null;
      AllocationHHeaderVO voBillHead = null;
      AllocationHItemVO voBillItem = null;
      AllocationHItemVO[] voaTempBillItem = null;

      String sHeadPK = null; String sCurHeadPK = null;
      int ooo = 0;
      ResultSetMetaData meta = rs.getMetaData();
      AllocationHVO[] localObject1;
      while (rs.next()) {
        sCurHeadPK = rs.getString("callocationhidh");
        SCMEnv.out("查到了" + sCurHeadPK);

        if ((sCurHeadPK != null) && (!sCurHeadPK.equals(sHeadPK)))
        {
          if (voBill != null) {
            voBillHead.setPrimaryKey(sHeadPK);
            voBill.setParentVO(voBillHead);
            if (vResultItemData.size() < 1) {
              SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
              localObject1 = null;
              return localObject1;
            }
            voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
            vResultItemData.copyInto(voaTempBillItem);
            voBill.setChildrenVO(voaTempBillItem);
            alResultData.add(voBill);
          }
          voBill = new AllocationHVO();
          voBillHead = new AllocationHHeaderVO();
          vResultItemData = new Vector();

          setHeaderData(rs, voBillHead, meta, 2, 14);
          voBillHead.setFbillflag(null);
          sHeadPK = sCurHeadPK;
        }

        ooo++;

        voBillItem = new AllocationHItemVO();

        setData(rs, voBillItem, meta, 15, meta.getColumnCount());
        voBillItem.setCallocationhid(sCurHeadPK);
        voBillItem.setFbillrowflag(null);

        vResultItemData.addElement(voBillItem);
      }

      if (voBill != null) {
        voBillHead.setPrimaryKey(sCurHeadPK);
        voBill.setParentVO(voBillHead);
        if (vResultItemData.size() < 1) {
          SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
          localObject1 = null;
          return localObject1;
        }
        voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
        vResultItemData.copyInto(voaTempBillItem);
        voBill.setChildrenVO(voaTempBillItem);
        alResultData.add(voBill);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    AllocationHVO[] avoResult = null;

    if (alResultData != null) {
      avoResult = new AllocationHVO[alResultData.size()];
      alResultData.toArray(avoResult);
      SCMEnv.out("查到了" + alResultData.size() + "单据");
    }

    return avoResult;
  }

  protected String getICParaString(String corp, String checkInvQtyParam)
    throws Exception
  {
    String Invparam = null;

    SysInitDMO dmo = new SysInitDMO();
    Invparam = dmo.getParaString(corp, checkInvQtyParam);

    if ((Invparam == null) || (Invparam.trim().length() == 0)) {
      Invparam = "Y";
    }
    return Invparam.trim();
  }

  protected UFDouble getICParaUFDouble(String corp, String checkInvQtyParam)
    throws Exception
  {
    UFDouble Invparam = null;
    SysInitDMO dmo = new SysInitDMO();
    Invparam = dmo.getParaDbl(corp, checkInvQtyParam);
    if (Invparam == null) {
      Invparam = new UFDouble("0");
    }
    return Invparam;
  }

  private ArrayList getQueryResult(ResultSet rs)
    throws SQLException
  {
    ArrayList alResultData = new ArrayList();
    int ooo = 0;
    DMDataVO pivo = new DMDataVO();

    ResultSetMetaData meta = null;
    try {
      while (rs.next()) {
        ooo++;
        pivo = new DMDataVO();
        if (ooo == 1) {
          meta = rs.getMetaData();
        }
        setData(rs, pivo, meta);
        if (null == pivo) {
          SCMEnv.out("无数据，应检查数据库...");
          return alResultData;
        }
        alResultData.add(pivo);
      }
      return alResultData; 
      } catch (SQLException e) {
    	    throw e;
    }

  }

  public UFDouble[] getRestNum(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null)
      throw new BusinessException("params can not be null!");
    String SQL = " select  isnull(nshouldnum,0)-isnull(ndelivernum,0) from ic_allocation_b  where callocationhid =? and callocationbid = ?  ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alRestNum = null;
    UFDouble[] audNum = null;
    int iUpdatedRowNum = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      alRestNum = new ArrayList();
      for (int i = 0; i < dmvo.length; i++) {
        if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(1, dmvo[i].getAttributeValue("pkbillh").toString());
        else
          stmt.setNull(1, 12);
        if (dmvo[i].getAttributeValue("pkbillb") != null)
          stmt.setString(2, dmvo[i].getAttributeValue("pkbillb").toString());
        else {
          stmt.setNull(2, 12);
        }
        rs = stmt.executeQuery();
        if (rs.next()) {
          Object o = rs.getObject(1);
          alRestNum.add(o == null ? null : new UFDouble(o.toString()));
        }
        else {
          alRestNum.add(null);
        }
        if (rs != null) {
          rs.close();
        }
      }
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
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
    if (alRestNum != null) {
      audNum = new UFDouble[alRestNum.size()];
      alRestNum.toArray(audNum);
    }
    return audNum;
  }

  private void initInitIds(DMDataVO[] datavos, boolean bIsClosedBill)
    throws SQLException
  {
    if ((datavos == null) || (datavos.length == 0)) {
      return;
    }
    StringBuffer sbWhere = null;

    for (int i = 0; i < datavos.length; i++)
    {
      if (datavos[i].getAttributeValue("orderstatus") == null)
        continue;
      if ((bIsClosedBill) && (datavos[i].getAttributeValue("orderstatus").toString().equalsIgnoreCase("Y"))) {
        if (sbWhere == null)
          sbWhere = new StringBuffer(" fbillrowflag!=5  and fbillflag!=5 and (");
        sbWhere.append(" body.callocationhid = '" + datavos[i].getAttributeValue("pkbillh") + "' ").append(" And body.callocationbid= '" + datavos[i].getAttributeValue("pkbillb") + "' OR ");
      }
      else if ((!bIsClosedBill) && (datavos[i].getAttributeValue("orderstatus").toString().equalsIgnoreCase("N"))) {
        if (sbWhere == null)
          sbWhere = new StringBuffer(" (fbillrowflag=5 or fbillflag=5) and (");
        sbWhere.append(" body.callocationhid = '" + datavos[i].getAttributeValue("pkbillh") + "' ").append(" And body.callocationbid= '" + datavos[i].getAttributeValue("pkbillb") + "' OR ");
      }

    }

    if ((sbWhere != null) && (sbWhere.length() > 3)) {
      sbWhere.delete(sbWhere.length() - 3, sbWhere.length());
      sbWhere.append(")");
    }
    else {
      return;
    }StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT ");
    sbSql.append("  body.callocationbid,  \n");
    sbSql.append("  body.fbillrowflag \n");
    sbSql.append(" FROM ic_allocation_h head  ");
    sbSql.append(" INNER JOIN  ic_allocation_b body \n");
    sbSql.append(" ON head.callocationhid = body.callocationhid \n");
    sbSql.append(" Where head.fbillflag = 3 AND head.dr=0 AND body.dr=0 \n ");
    if (sbWhere != null)
    {
      sbSql.append(" And " + sbWhere.toString());
    }
    Connection con = null;
    PreparedStatement stmt = null;

    ArrayList alResultData = new ArrayList();
    ResultSet rs = null;
    Hashtable ht = new Hashtable();
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      String bid = null;
      Object b = null;
      Integer flag = null;
      while (rs.next()) {
        bid = rs.getString(1);
        b = rs.getObject(2);
        if ((bid == null) || (b == null) || 
          (ht.containsKey(bid))) continue;
        flag = new Integer(b.toString());
        ht.put(bid, flag);
        if ((flag.intValue() == 5) && (!bIsClosedBill)) {
          this.m_alClosedbid.add(bid); continue;
        }if ((flag.intValue() != 5) && (bIsClosedBill)) {
          this.m_alOpenedbid.add(bid);
        }
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private ArrayList insertThisShldBill(GeneralBillVO voNewBill)
    throws Exception
  {
    GeneralBillBO bo = new GeneralBillBO();
    if (voNewBill != null)
      voNewBill.setStatus(2);
    bo.fillPlanPrice(voNewBill);
    ArrayList alRet = bo.insertThisBill(voNewBill);
    return alRet;
  }

  public UFBoolean[] isBillsClosed(String[] pkbillbs)
    throws BusinessException
  {
    if (pkbillbs == null)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000034"));
    String SQL = " select h.fbillflag from ic_allocation_h h inner join ic_allocation_b b on(h.callocationhid = b.callocationhid ) where h.dr = 0 And b.dr= 0 And b.callocationbid =?   ";
    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    ArrayList alResult = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      alResult = new ArrayList();
      for (int i = 0; i < pkbillbs.length; i++)
      {
        if (pkbillbs[i] != null) {
          stmt.setString(1, pkbillbs[i]);
        }
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000037"));
        }

        rs = stmt.executeQuery();
        if (rs.next())
        {
          Object oTemp = rs.getObject(1);
          if ((oTemp != null) && (oTemp.toString().equals("5"))) {
            alResult.add(new UFBoolean(true));
          }
          else {
            alResult.add(new UFBoolean(false));
          }
        }
        else
        {
          alResult.add(null);
        }

      }

    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try
      {
        if (rs != null)
        {
          rs.close();
        }
      }
      catch (Exception e)
      {
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
    UFBoolean[] bResult = null;
    if (alResult.size() > 0)
    {
      bResult = new UFBoolean[alResult.size()];
      alResult.toArray(bResult);
    }

    return bResult;
  }

  public UFBoolean isBillUpdated(AggregatedValueObject avo)
    throws Exception
  {
    if (avo == null) {
      return new UFBoolean(false);
    }
    UFDouble nsignnum = null;
    String sSourceBid = null;
    String sSourceHid = null;
    String sSourceBillType = null;
    String sHid = (String)avo.getChildrenVO()[0].getAttributeValue("cgeneralhid");
    String sBid = (String)avo.getChildrenVO()[0].getAttributeValue("cgeneralbid");
    sSourceHid = (String)avo.getChildrenVO()[0].getAttributeValue("csourcebillhid");
    sSourceBid = (String)avo.getChildrenVO()[0].getAttributeValue("csourcebillbid");
    sSourceBillType = (String)avo.getChildrenVO()[0].getAttributeValue("csourcetype");

    if ((sSourceBillType == null) || (!sSourceBillType.equals("7F")))
      return new UFBoolean(true);
    StringBuffer sql = new StringBuffer(" Select bb3.ndmsignnum From ic_general_b b inner join ic_general_bb3  bb3 on (b.cgeneralbid = bb3.cgeneralbid ) where b.csourcebillhid ='" + sSourceHid + "' And b.csourcebillbid = '" + sSourceBid + "' And b.dr = 0 And bb3.dr=0 And bb3.cgeneralbid = '" + sBid + "' ");

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      pstmt = con.prepareStatement(sql.toString());
      rs = pstmt.executeQuery();
      while (rs.next())
      {
        Object oTemp = rs.getObject("ndmsignnum");
        nsignnum = oTemp == null ? null : new UFDouble(oTemp.toString());
      }

    }
    finally
    {
      try
      {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
      }
      try
      {
        if (pstmt != null)
        {
          pstmt.close();
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

    if ((nsignnum == null) || (nsignnum.doubleValue() == 0.0D)) {
      return new UFBoolean(true);
    }
    return new UFBoolean(false);
  }

  private DMDataVO[] query(StringBuffer sbSql)
    throws Exception
  {
    ArrayList al = queryExecute(sbSql);

    DMDataVO[] ddvos = new DMDataVO[al.size()];
    for (int i = 0; i < al.size(); i++) {
      ddvos[i] = ((DMDataVO)al.get(i));
      ddvos[i].convertToArrayList();
    }
    return ddvos;
  }

  public AllocationHVO[] queryBillsForDeliver(ConditionVO[] aryConVO)
    throws Exception
  {
    String sWhereHead = "  1=1";

    String sWhereBody = "  1=1";

    String sDeliverOrg = null;

    if ((aryConVO != null) && (aryConVO.length > 0))
    {
      for (int i = 0; i < aryConVO.length; i++)
      {
        if (aryConVO[i].getFieldCode().equals("pkdelivorg")) {
          sDeliverOrg = aryConVO[i].getValue().trim();
        }
        else if (aryConVO[i].getFieldCode().equals("datefrom")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " body.dplanoutdate " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "' " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("dateto")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " body.dplanoutdate " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "' " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkcust")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " 0 > 1 " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pksendstockorg")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.coutcalbodyid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "' " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkdeststoreorg")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.cincalbodyid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "' " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkinv")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " body.cinventoryid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("invcl")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + "inv.pk_invbasdoc IN (SELECT bd_invbasdoc.pk_invbasdoc AS invbas FROM bd_invbasdoc INNER JOIN bd_invcl bd_invcl1  " + " ON  bd_invbasdoc.pk_invcl=bd_invcl1.pk_invcl  WHERE bd_invcl1.invclasscode LIKE '" + aryConVO[i].getValue().trim() + "%') " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("vallocationbillcode")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.vbillcode " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkreceiptcorpid")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " 0 > 1 " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkoperator")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.cbsmanid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkbillb")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " body.callocationbid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkarrivearea")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " wh1.pk_areacl " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("billoutdate")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " body.dplanoutdate " + aryConVO[i].getOperaCode() + " '" + (aryConVO[i].getValue() == null ? "" : aryConVO[i].getValue().trim()) + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("vbillcode")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.vbillcode " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("userid")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.coperatorid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }

      }

    }

    if ((sDeliverOrg != null) && (sDeliverOrg.length() > 0))
    {
      IDMToIC aDmDMO = (IDMToIC)NCLocator.getInstance().lookup(IDMToIC.class.getName());
      ArrayList alCorps = aDmDMO.queryCorpIDsByDelivOrgID(sDeliverOrg);

      if ((alCorps != null) && (alCorps.size() > 0)) {
        String sWhereCorp = " And ( ";
        for (int i = 0; i < alCorps.size(); i++) {
          if (i == alCorps.size() - 1)
            sWhereCorp = sWhereCorp + " head.pk_corp ='" + (String)alCorps.get(i) + "' ";
          else {
            sWhereCorp = sWhereCorp + " head.pk_corp ='" + (String)alCorps.get(i) + "' OR ";
          }
        }
        sWhereCorp = sWhereCorp + ") ";
        sWhereHead = sWhereHead + sWhereCorp;
      }

    }

    StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT * \n");
    sbSql.append(" FROM (SELECT ");

    sbSql.append("\tDISTINCT ");
    sbSql.append(" head.callocationhid as callocationhidh, head.cbilltypecode, head.cbsmanid,  \n");

    sbSql.append("               psn1.psnname AS cbsmanname, head.cdeptid,  \n");
    sbSql.append("              \t\t\t dept1.deptname AS cdeptname, head.cincalbodyid,  \n");

    sbSql.append("               wh1.bodyname AS cincalbodyname,   \n");

    sbSql.append("               head.coutcalbodyid, \n");
    sbSql.append("               wh2.bodyname AS coutcalbodyname,   \n");
    sbSql.append("               head.dbilldate,  wh1.pk_areacl,\n");
    sbSql.append("               head.pk_corp as pk_corph, head.vbillcode, head.vnote, head.pk_sendtype, bd_sendtype.sendname as sendtype, \n");

    sbSql.append(" wh1.area as receiveaddr,  \n");

    sbSql.append("               head.vuserdef1 AS vuserdef1h,  \n");
    sbSql.append("               head.vuserdef2 AS vuserdef2h, head.vuserdef3 AS vuserdef3h,  \n");

    sbSql.append("               head.vuserdef4 AS vuserdef4h, head.vuserdef5 AS vuserdef5h,  \n");

    sbSql.append("               head.vuserdef6 AS vuserdef6h, head.vuserdef7 AS vuserdef7h,  \n");

    sbSql.append("               head.vuserdef8 AS vuserdef8h, head.vuserdef9 AS vuserdef9h,  \n");

    sbSql.append("               head.vuserdef10 AS vuserdef10h, head.cauditorid,  \n");

    sbSql.append("      psn3.user_name AS cauditorname, head.coperatorid,  \n");
    sbSql.append("               psn4.user_name AS coperatorname,head.dauditordate,head.fbillflag, head.ts AS tsh, \n");

    sbSql.append("      \t body.castunitid, body.dplanindate,body.dplanoutdate, \n");
    sbSql.append("               meas2.measname AS castunitname, inv.invcode cinventorycode,  \n");

    sbSql.append("               body.cinventoryid, invman.wholemanaflag AS isLotMgt,  \n");

    sbSql.append("               invman.serialmanaflag AS isSerialMgt,  \n");
    sbSql.append("               invman.qualitymanflag AS isValidateMgt, inv.setpartsflag AS isSet,  \n");

    sbSql.append("               inv.storeunitnum AS standStoreUOM, inv.pk_measdoc3 AS defaultAstUOM,  \n");

    sbSql.append("               invman.sellproxyflag AS isSellProxy, invman.qualitydaynum AS qualityDay,  \n");

    sbSql.append("               conv102.fixedflag AS isSolidConvRate, body.csourcebillbid, body.csourcebillhid,  \n");

    sbSql.append("               body.csourcetype, body.pk_corp, \n");
    sbSql.append("                body.callocationbid, body.callocationhid, \n");
    sbSql.append("              body.cinwarehouseid, wh3.storname AS cinwarehousename, wh3.csflag AS inisLocatorMgt,  \n");

    sbSql.append("     body.coutwarehouseid, wh4.storname AS coutwarehousename,wh4.csflag AS outislocatorMgt,wh3.gubflag AS inisWasteWh,  body.dvalidate,  \n");

    sbSql.append("               wh4.gubflag AS outisWasteWh,body.fbillrowflag, conv102.mainmeasrate AS hsl, inv.invname, inv.invspec,  \n");

    sbSql.append("               inv.invtype, meas1.measname AS measdocname, body.nshouldastnum,  \n");

    sbSql.append("               body.nshouldnum, body.ninnum,body.nsignnum,  \n");
    sbSql.append("               body.noutnum, body.nprice, body.nmny,body.ndelivernum,body.nonwaynum, body.nwithdrawnum, \n");

    sbSql.append("               meas1.pk_measdoc, body.vbatchcode, body.vfree1, body.vfree2, body.vfree3,  \n");

    sbSql.append("               body.vfree4, body.vfree5, body.vfree6, body.vfree7, body.vfree8, body.vfree9,  \n");

    sbSql.append("               body.vfree10, body.vsourcebillcode,body.vnote as vnotebody, body.vuserdef1, body.vuserdef2,  \n");

    sbSql.append("               body.vuserdef3, body.vuserdef4, body.vuserdef5, body.vuserdef6,  \n");

    sbSql.append("               inv.assistunit AS isAstUOMmgt, invman.pk_invmandoc \n");

    sbSql.append("               \t ,substring(CONVERT(varchar, dateadd(day, - invman.qualitydaynum,body.dvalidate), 21), 1, 10) AS scrq   \n");

    sbSql.append("\t,bt.billtypename AS csourcetypename ,body.ts AS ts \n");

    sbSql.append(" FROM ic_allocation_h head LEFT OUTER JOIN \n");
    sbSql.append(" bd_deptdoc dept1 ON head.cdeptid = dept1.pk_deptdoc LEFT OUTER JOIN \n");

    sbSql.append(" bd_calbody wh1 ON  \n");
    sbSql.append(" head.cincalbodyid = wh1.pk_calbody LEFT OUTER JOIN \n");
    sbSql.append(" bd_calbody wh2 ON  \n");
    sbSql.append(" head.coutcalbodyid = wh2.pk_calbody LEFT OUTER JOIN \n");
    sbSql.append(" bd_psndoc psn1 ON head.cbsmanid = psn1.pk_psndoc LEFT OUTER JOIN \n");

    sbSql.append(" sm_user psn3 ON head.cauditorid = psn3.cUserID LEFT OUTER JOIN \n");

    sbSql.append(" sm_user psn4 ON head.coperatorid = psn4.cUserID  \n");
    sbSql.append(" Left outer join bd_sendtype ON head.pk_sendtype = bd_sendtype.pk_sendtype \n");

    sbSql.append(" INNER JOIN  ic_allocation_b body \n");
    sbSql.append(" ON head.callocationhid = body.callocationhid \n");
    sbSql.append("  LEFT OUTER JOIN  bd_stordoc wh3 ON body.cinwarehouseid = wh3.pk_stordoc  \n");

    sbSql.append("               LEFT OUTER JOIN  bd_stordoc wh4 ON body.coutwarehouseid = wh4.pk_stordoc LEFT OUTER JOIN \n");

    sbSql.append(" bd_invmandoc invman LEFT OUTER JOIN \n");
    sbSql.append(" bd_invbasdoc inv ON  \n");
    sbSql.append(" invman.pk_invbasdoc = inv.pk_invbasdoc  LEFT OUTER JOIN \n");
    sbSql.append(" bd_measdoc meas1 ON  \n");
    sbSql.append(" inv.pk_measdoc = meas1.pk_measdoc LEFT OUTER JOIN \n");
    sbSql.append(" bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl ON  \n");
    sbSql.append(" body.cinventoryid = invman.pk_invmandoc LEFT OUTER JOIN \n");
    sbSql.append(" bd_measdoc meas2 ON  \n");

    sbSql.append(" \tbody.castunitid = meas2.pk_measdoc\n");

    sbSql.append("\tLEFT OUTER JOIN bd_billtype bt ON body.csourcetype=bt.pk_billtypecode \n");

    sbSql.append(" LEFT OUTER JOIN \t" + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc \n");

    sbSql.append(" LEFT OUTER JOIN \t" + GeneralSqlString.sBd_Convert + " conv102 ON body.castunitid=conv102.pk_measdoc \n");

    if (sWhereBody != null) {
      sbSql.append(" INNER JOIN  ic_allocation_b body2 ON body.callocationhid=body2.callocationhid \n");

      sbSql.append(" \tLEFT OUTER JOIN bd_invmandoc invman2 \n");
      sbSql.append(" \tLEFT OUTER  JOIN  bd_invbasdoc inv2 \n");
      sbSql.append(" \tON invman2.pk_invbasdoc=inv2.pk_invbasdoc  LEFT OUTER JOIN \n");
      sbSql.append("  bd_measdoc meas12 ON  \n");
      sbSql.append("  inv2.pk_measdoc = meas12.pk_measdoc LEFT OUTER JOIN \n");
      sbSql.append("  bd_invcl invcl2 ON inv2.pk_invcl = invcl2.pk_invcl ON  \n");
      sbSql.append("  body2.cinventoryid = invman2.pk_invmandoc LEFT OUTER JOIN \n");
      sbSql.append("  bd_measdoc meas22 ON  \n");
      sbSql.append("  body2.castunitid = meas22.pk_measdoc  \n");
      sbSql.append("  LEFT OUTER JOIN \t\t" + GeneralSqlString.sBd_ConvertMainUom + " conv2 ON inv2.pk_invbasdoc = conv2.pk_invbasdoc  \n");

      sbSql.append("  LEFT OUTER JOIN \t\t" + GeneralSqlString.sBd_Convert + " conv202 ON body2.castunitid = conv202.pk_measdoc  \n");

      sbSql.append("\n \t\tWHERE body.dr=0 AND (");
      sbSql.append(sWhereBody + " ) ");
    }

    if (sWhereBody != null)
      sbSql.append(" AND ");
    else {
      sbSql.append(" WHERE ");
    }
    sbSql.append(" head.dr=0 AND (isnull(body.nshouldnum,0)-isnull(body.noutnum,0))>0 And body.dr=0 AND head.fbillflag = 3 AND head.fbillflag <> 5 AND head.fbillflag <> 6 AND (bd_sendtype.issendarranged = 'Y' or bd_sendtype.issendarranged = 'y') AND (body.fbillrowflag<>5 or body.fbillrowflag is null ) AND " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102"));

    if ((sWhereHead != null) && (sWhereHead.length() > 0))
    {
      sbSql.append(" And " + sWhereHead + ")b ");
    }

    Connection con = null;
    PreparedStatement stmt = null;

    ArrayList alResultData = new ArrayList();
    ResultSet rs = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      Vector vResultItemData = null;
      UFDouble dZero = new UFDouble(0);

      AllocationHVO voBill = null;
      AllocationHHeaderVO voBillHead = null;
      AllocationHItemVO voBillItem = null;
      AllocationHItemVO[] voaTempBillItem = null;

      String sHeadPK = null; String sCurHeadPK = null;
      int ooo = 0;
      ResultSetMetaData meta = rs.getMetaData();
      AllocationHVO[] localObject1;
      while (rs.next()) {
        sCurHeadPK = rs.getString("callocationhidh");
        SCMEnv.out("查到了" + sCurHeadPK);

        if ((sCurHeadPK != null) && (!sCurHeadPK.equals(sHeadPK)))
        {
          if (voBill != null) {
            voBillHead.setPrimaryKey(sHeadPK);
            voBill.setParentVO(voBillHead);
            if (vResultItemData.size() < 1) {
              SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
              localObject1 = null;
              return localObject1;
            }
            voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
            vResultItemData.copyInto(voaTempBillItem);
            voBill.setChildrenVO(voaTempBillItem);
            alResultData.add(voBill);
          }
          voBill = new AllocationHVO();
          voBillHead = new AllocationHHeaderVO();
          vResultItemData = new Vector();

          setHeaderData(rs, voBillHead, meta, 2, 35);
          sHeadPK = sCurHeadPK;
        }

        ooo++;

        voBillItem = new AllocationHItemVO();
        setData(rs, voBillItem, meta, 36, meta.getColumnCount());
        voBillItem.setCallocationhid(sCurHeadPK);

        vResultItemData.addElement(voBillItem);
      }

      if (voBill != null) {
        voBillHead.setPrimaryKey(sCurHeadPK);
        voBill.setParentVO(voBillHead);
        if (vResultItemData.size() < 1) {
          SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
          localObject1 = null;
          return localObject1;
        }
        voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
        vResultItemData.copyInto(voaTempBillItem);
        voBill.setChildrenVO(voaTempBillItem);
        alResultData.add(voBill);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    filterUselessUOM(alResultData);

    AllocationHVO[] avoResult = null;

    if (alResultData != null) {
      avoResult = new AllocationHVO[alResultData.size()];
      alResultData.toArray(avoResult);
      SCMEnv.out("查到了" + alResultData.size() + "单据");
    }

    return avoResult;
  }

  public AllocationHVO[] queryBillsForDeliver2(ConditionVO[] aryOldConVO)
    throws Exception
  {
    String sWhereHead = "  1=1";

    String sWhereBody = "  1=1";

    String sDeliverOrg = null;

    ConditionVO[] aryConVO = new ConditionVO[aryOldConVO.length];
    for (int i = 0; i < aryOldConVO.length; i++) {
      aryConVO[i] = new ConditionVO();
      aryConVO[i] = ((ConditionVO)aryOldConVO[i].clone());
    }

    if ((aryConVO != null) && (aryConVO.length > 0))
    {
      for (int i = 0; i < aryConVO.length; i++)
      {
        if (aryConVO[i].getFieldCode().equals("pkdelivorg")) {
          sDeliverOrg = aryConVO[i].getValue().trim();
        }
        else if (aryConVO[i].getFieldCode().equals("datefrom")) {
          aryConVO[i].setFieldCode("body.dplanoutdate");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("dateto")) {
          aryConVO[i].setFieldCode("body.dplanoutdate");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("pkcust")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " 0 > 1 " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pksendstockorg")) {
          aryConVO[i].setFieldCode("head.coutcalbodyid");
          sWhereHead = sWhereHead + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("pkdeststoreorg")) {
          aryConVO[i].setFieldCode("head.cincalbodyid");
          sWhereHead = sWhereHead + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("pkinv")) {
          aryConVO[i].setFieldCode("body.cinventoryid");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("invcl")) {
          aryConVO[i].setFieldCode("body.cinventoryid");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("vallocationbillcode")) {
          aryConVO[i].setFieldCode("head.vbillcode");
          sWhereHead = sWhereHead + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("cbiztype")) {
          aryConVO[i].setFieldCode("head.cbiztypeid");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("pkreceiptcorpid")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " 0 > 1 " + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("pkoperator")) {
          aryConVO[i].setFieldCode("head.cbsmanid");
          sWhereBody = sWhereBody + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("pkarrivearea")) {
          sWhereBody = sWhereBody + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " wh1.pk_areacl " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }
        else if (aryConVO[i].getFieldCode().equals("billoutdate")) {
          aryConVO[i].setFieldCode("body.dplanoutdate");
          sWhereHead = sWhereHead + aryConVO[i].getSQLStr();
        }
        else if (aryConVO[i].getFieldCode().equals("userid")) {
          sWhereHead = sWhereHead + (aryConVO[i].getLogic() ? " AND " : " OR ") + (aryConVO[i].getNoLeft() ? "" : " ( ") + " head.coperatorid " + aryConVO[i].getOperaCode() + " '" + aryConVO[i].getValue().trim() + "'" + (aryConVO[i].getNoRight() ? "" : " ) ");
        }

      }

    }

    if ((sDeliverOrg != null) && (sDeliverOrg.length() > 0))
    {
      IDMToIC aDmDMO = (IDMToIC)NCLocator.getInstance().lookup(IDMToIC.class.getName());
      ArrayList alCorps = aDmDMO.queryCorpIDsByDelivOrgID(sDeliverOrg);

      if ((alCorps != null) && (alCorps.size() > 0)) {
        String sWhereCorp = " And ( ";
        for (int i = 0; i < alCorps.size(); i++) {
          if (i == alCorps.size() - 1)
            sWhereCorp = sWhereCorp + " head.pk_corp ='" + (String)alCorps.get(i) + "' ";
          else {
            sWhereCorp = sWhereCorp + " head.pk_corp ='" + (String)alCorps.get(i) + "' OR ";
          }
        }
        sWhereCorp = sWhereCorp + ") ";
        sWhereHead = sWhereHead + sWhereCorp;
      }

    }

    StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT * \n");
    sbSql.append(" FROM (SELECT ");

    sbSql.append("\tDISTINCT ");
    sbSql.append(" head.callocationhid as callocationhidh, head.cbilltypecode, head.cbsmanid,  \n");

    sbSql.append(" head.cincalbodyid, \n");

    sbSql.append("               head.coutcalbodyid, \n");

    sbSql.append("               head.dbilldate,  wh1.pk_areacl,\n");
    sbSql.append("               head.pk_corp as pk_corph, head.vbillcode, head.vnote, head.pk_sendtype,  \n");

    sbSql.append(" wh1.area as receiveaddr,  \n");
    sbSql.append("               head.vuserdef1 AS vuserdef1h,  \n");
    sbSql.append("               head.vuserdef2 AS vuserdef2h, head.vuserdef3 AS vuserdef3h,  \n");

    sbSql.append("               head.vuserdef4 AS vuserdef4h, head.vuserdef5 AS vuserdef5h,  \n");

    sbSql.append("               head.vuserdef6 AS vuserdef6h, head.vuserdef7 AS vuserdef7h,  \n");

    sbSql.append("               head.vuserdef8 AS vuserdef8h, head.vuserdef9 AS vuserdef9h,  \n");

    sbSql.append("               head.vuserdef10 AS vuserdef10h,   \n");

    sbSql.append("\thead.ts AS ts, \n");

    sbSql.append("      \t body.castunitid, body.dplanindate,body.dplanoutdate, \n");

    sbSql.append("               body.cinventoryid,  \n");

    sbSql.append("                body.pk_corp, \n");
    sbSql.append("                body.callocationbid, body.callocationhid, \n");
    sbSql.append("              body.cinwarehouseid, \n");

    sbSql.append("     body.coutwarehouseid,   \n");

    sbSql.append("               body.nshouldastnum,  \n");

    sbSql.append("               body.nshouldnum, body.ninnum,body.nsignnum,  \n");
    sbSql.append("               body.noutnum, body.nprice, body.nmny,body.ndelivernum,body.nonwaynum, body.nwithdrawnum, \n");

    sbSql.append("               body.vbatchcode, body.vfree1, body.vfree2, body.vfree3,  \n");

    sbSql.append("               body.vfree4, body.vfree5, body.vfree6, body.vfree7, body.vfree8, body.vfree9,  \n");

    sbSql.append("               body.vfree10, body.vsourcebillcode,body.vnote as vnotebody, body.vuserdef1, body.vuserdef2,  \n");

    sbSql.append("               body.vuserdef3, body.vuserdef4, body.vuserdef5, body.vuserdef6  \n");

    sbSql.append(" FROM ic_allocation_h head LEFT OUTER JOIN \n");

    sbSql.append(" bd_calbody wh1 ON  \n");
    sbSql.append(" head.cincalbodyid = wh1.pk_calbody LEFT OUTER JOIN \n");

    sbSql.append(" bd_sendtype ON head.pk_sendtype = bd_sendtype.pk_sendtype \n");

    sbSql.append(" INNER JOIN  ic_allocation_b body \n");
    sbSql.append(" ON head.callocationhid = body.callocationhid \n");

    if (sWhereBody != null)
    {
      sbSql.append("\n \t\tWHERE body.dr=0 AND (");
      sbSql.append(sWhereBody + " ) ");
    }

    if (sWhereBody != null)
      sbSql.append(" AND ");
    else {
      sbSql.append(" WHERE ");
    }
    sbSql.append(" head.dr=0 AND body.dr=0 AND (isnull(body.nshouldnum,0)-COALESCE(body.ndelivernum,body.noutnum,0))>0 AND head.fbillflag = 3  AND (bd_sendtype.issendarranged = 'Y' or bd_sendtype.issendarranged = 'y') AND (body.fbillrowflag<>5 or body.fbillrowflag is null )  ");

    if ((sWhereHead != null) && (sWhereHead.length() > 0))
    {
      sbSql.append(" And " + sWhereHead + ")b ");
    }

    Connection con = null;
    PreparedStatement stmt = null;

    ArrayList alResultData = new ArrayList();
    ResultSet rs = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      Vector vResultItemData = null;
      UFDouble dZero = new UFDouble(0);

      AllocationHVO voBill = null;
      AllocationHHeaderVO voBillHead = null;
      AllocationHItemVO voBillItem = null;
      AllocationHItemVO[] voaTempBillItem = null;

      String sHeadPK = null; String sCurHeadPK = null;
      int ooo = 0;
      ResultSetMetaData meta = rs.getMetaData();
      AllocationHVO[] localObject1;
      while (rs.next()) {
        sCurHeadPK = rs.getString("callocationhidh");
        SCMEnv.out("查到了" + sCurHeadPK);

        if ((sCurHeadPK != null) && (!sCurHeadPK.equals(sHeadPK)))
        {
          if (voBill != null) {
            voBillHead.setPrimaryKey(sHeadPK);
            voBill.setParentVO(voBillHead);
            if (vResultItemData.size() < 1) {
              SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
              localObject1 = null;
              return localObject1;
            }
            voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
            vResultItemData.copyInto(voaTempBillItem);
            voBill.setChildrenVO(voaTempBillItem);
            alResultData.add(voBill);
          }
          voBill = new AllocationHVO();
          voBillHead = new AllocationHHeaderVO();
          vResultItemData = new Vector();

          setHeaderData(rs, voBillHead, meta, 2, 23);
          sHeadPK = sCurHeadPK;
        }

        ooo++;

        voBillItem = new AllocationHItemVO();
        setData(rs, voBillItem, meta, 24, meta.getColumnCount());
        voBillItem.setCallocationhid(sCurHeadPK);

        vResultItemData.addElement(voBillItem);
      }

      if (voBill != null) {
        voBillHead.setPrimaryKey(sCurHeadPK);
        voBill.setParentVO(voBillHead);
        if (vResultItemData.size() < 1) {
          SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
          localObject1 = null;
          return localObject1;
        }
        voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
        vResultItemData.copyInto(voaTempBillItem);
        voBill.setChildrenVO(voaTempBillItem);
        alResultData.add(voBill);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    filterUselessUOM(alResultData);
    AllocationHVO[] avoResult = null;

    if (alResultData != null) {
      avoResult = new AllocationHVO[alResultData.size()];
      alResultData.toArray(avoResult);
      SCMEnv.out("查到了" + alResultData.size() + "单据");
    }

    return avoResult;
  }

  private AllocationHVO[] queryBillsForDeliver2(String sWhere)
    throws Exception
  {
    StringBuffer sbSql = new StringBuffer();

    sbSql.append(" SELECT DISTINCT ");
    sbSql.append(" head.callocationhid as callocationhidh, head.cbilltypecode,   \n");
    sbSql.append(" head.cincalbodyid, head.coutcalbodyid,  head.dbilldate,  \n");
    sbSql.append(" head.pk_corp as pk_corph, head.vbillcode, head.vnote, head.pk_sendtype,  \n");
    sbSql.append(" head.cauditorid, head.coperatorid,  head.dauditordate,head.fbillflag, head.ts AS tsh, \n");
    sbSql.append("  body.castunitid, body.dplanindate,body.dplanoutdate, body.cinventoryid,   \n");
    sbSql.append("  body.csourcebillbid, body.csourcebillhid,  body.csourcetype, body.pk_corp, \n");
    sbSql.append("  body.callocationbid, body.callocationhid, body.cinwarehouseid,    \n");
    sbSql.append("  body.coutwarehouseid, body.dvalidate,  body.fbillrowflag, body.nshouldastnum,  \n");
    sbSql.append("  body.nshouldnum, body.ninnum,body.nsignnum,  \n");
    sbSql.append("  body.noutnum, body.nprice, body.nmny,body.ndelivernum,body.nonwaynum, body.nwithdrawnum, \n");
    sbSql.append("  body.vbatchcode, body.vfree1, body.vfree2, body.vfree3,  \n");
    sbSql.append("  body.vfree4, body.vfree5, body.vfree6, body.vfree7, body.vfree8, body.vfree9,  \n");
    sbSql.append("  body.vfree10, body.vsourcebillcode,body.vnote as vnotebody, body.ts AS ts \n");
    sbSql.append(" FROM ic_allocation_h head  ");
    sbSql.append(" INNER JOIN  ic_allocation_b body \n");
    sbSql.append(" ON head.callocationhid = body.callocationhid \n");
    sbSql.append(" Where head.fbillflag = 3 AND head.dr=0 AND body.dr=0 \n ");
    if (sWhere != null)
    {
      sbSql.append(" And " + sWhere);
    }
    Connection con = null;
    PreparedStatement stmt = null;

    ArrayList alResultData = new ArrayList();
    ResultSet rs = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      Vector vResultItemData = null;
      UFDouble dZero = new UFDouble(0);

      AllocationHVO voBill = null;
      AllocationHHeaderVO voBillHead = null;
      AllocationHItemVO voBillItem = null;
      AllocationHItemVO[] voaTempBillItem = null;

      String sHeadPK = null; String sCurHeadPK = null;
      int ooo = 0;
      ResultSetMetaData meta = rs.getMetaData();
      AllocationHVO[] localObject1;
      while (rs.next()) {
        sCurHeadPK = rs.getString("callocationhidh");
        SCMEnv.out("查到了" + sCurHeadPK);

        if ((sCurHeadPK != null) && (!sCurHeadPK.equals(sHeadPK)))
        {
          if (voBill != null) {
            voBillHead.setPrimaryKey(sHeadPK);
            voBill.setParentVO(voBillHead);
            if (vResultItemData.size() < 1) {
              SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
              localObject1 = null;
              return localObject1;
            }
            voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
            vResultItemData.copyInto(voaTempBillItem);
            voBill.setChildrenVO(voaTempBillItem);
            alResultData.add(voBill);
          }
          voBill = new AllocationHVO();
          voBillHead = new AllocationHHeaderVO();
          vResultItemData = new Vector();

          setHeaderData(rs, voBillHead, meta, 2, 14);
          sHeadPK = sCurHeadPK;
        }

        ooo++;

        voBillItem = new AllocationHItemVO();

        setData(rs, voBillItem, meta, 15, meta.getColumnCount());
        voBillItem.setCallocationhid(sCurHeadPK);

        vResultItemData.addElement(voBillItem);
      }

      if (voBill != null) {
        voBillHead.setPrimaryKey(sCurHeadPK);
        voBill.setParentVO(voBillHead);
        if (vResultItemData.size() < 1) {
          SCMEnv.out(voBillHead.getPrimaryKey() + "bill has no item ERROR!");
          localObject1 = null;
          return localObject1;
        }
        voaTempBillItem = new AllocationHItemVO[vResultItemData.size()];
        vResultItemData.copyInto(voaTempBillItem);
        voBill.setChildrenVO(voaTempBillItem);
        alResultData.add(voBill);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    AllocationHVO[] avoResult = null;

    if (alResultData != null) {
      avoResult = new AllocationHVO[alResultData.size()];
      alResultData.toArray(avoResult);
      SCMEnv.out("查到了" + alResultData.size() + "单据");
    }

    return avoResult;
  }

  public DMDataVO[] queryDeliv2OutSumShouldOutNum(String[] sDelivItemPKs)
    throws BusinessException
  {
    if (sDelivItemPKs.length == 0) {
      return new DMDataVO[0];
    }
    DMDataVO[] dmdvos = new DMDataVO[sDelivItemPKs.length];

    StringBuffer sb = new StringBuffer();
    sb.append("select ic_general_b.csourcebillbid as pk_delivbill_b,").append(" sum(coalesce(ic_general_b.nshouldoutnum,0)) as sumyf ").append(" from ic_general_h left outer join ic_general_b ").append(" on ic_general_h.cgeneralhid=ic_general_b.cgeneralhid ").append(" where ").append(" ic_general_h.dr=0 and ic_general_b.dr=0 ").append(" and ic_general_h.fbillflag =").append("2").append(" and (ic_general_b.noutnum is null or ic_general_b.noutnum=0) ").append(SQLUtil.formInSQL("ic_general_b.csourcebillbid", sDelivItemPKs)).append(" group by ic_general_b.csourcebillbid ");
    try
    {
      dmdvos = query(sb);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    return dmdvos;
  }

  public DMDataVO[] queryDeliv2OutSumShouldOutNum2(String sDelivPK)
    throws BusinessException
  {
    StringBuffer sbWhere = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    sb.append("select ic_general_b.csourcebillbid as pk_delivbill_b,").append(" sum(coalesce(ic_general_b.nshouldoutnum,0)) as sumyf ").append(" from ic_general_h left outer join ic_general_b ").append(" on ic_general_h.cgeneralhid=ic_general_b.cgeneralhid ").append(" where ").append(" ic_general_h.dr=0 and ic_general_b.dr=0 ").append(" and ic_general_h.fbillflag =").append("2").append(" and (ic_general_b.noutnum is null or ic_general_b.noutnum=0) ").append(" and (ic_general_b.csourcebillhid = '").append(sDelivPK).append("') ").append(" group by ic_general_b.csourcebillbid ");

    DMDataVO[] dmdvos = null;
    try
    {
      dmdvos = query(sb);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    return dmdvos;
  }

  private ArrayList queryExecute(StringBuffer sbSql)
    throws SQLException
  {
    if ((null == sbSql) || (sbSql.toString().trim().length() == 0)) {
      return new ArrayList();
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    ArrayList alResultData = new ArrayList();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());

      rs = stmt.executeQuery();

      ArrayList localArrayList1 = getQueryResult(rs);
      return localArrayList1;
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close(); 
      } catch (Exception e) {
      }
      
    }

  }

  public UFBoolean reverseOutSignNum(DMDataVO[] dmVO)
    throws BusinessException
  {
    if (dmVO == null)
      return new UFBoolean(false);
    try
    {
      UFDouble nsignnum = null;
      String sBid = null;
      String sHid = null;
      String sSourceBid = null;
      String sSourceHid = null;
      String sPk_corp = null;
      UFDouble nTotalSignNum = null;
      for (int i = 0; i < dmVO.length; i++)
      {
        sSourceHid = (String)dmVO[i].getAttributeValue("pk_delivbill_h");
        sSourceBid = (String)dmVO[i].getAttributeValue("pk_delivbill_b");

        sPk_corp = (String)dmVO[i].getAttributeValue("pkcorpforgenoid");
        StringBuffer sql1 = new StringBuffer(" Select ic_general_b.cgeneralhid, ic_general_b.cgeneralbid ,isnull(noutnum,0.0) as noutnum From ic_general_b  where csourcebillhid ='" + sSourceHid + "' And csourcebillbid = '" + sSourceBid + "' And ic_general_b.dr = 0  And ic_general_b.noutnum is not null  ");

        StringBuffer sql3 = new StringBuffer(" update ic_general_bb3 set dr = 1 where cgeneralbid = ? ");

        StringBuffer sql4 = new StringBuffer(" insert into ic_general_bb3 (cgeneralbb3,cgeneralhid,cgeneralbid,ndmsignnum,nsignnum) values(?,?,?,?,?)");

        StringBuffer sql5 = new StringBuffer(" Select caccountunitid, cpk1,cpk2,naccountmny, naccountnum1,naccountnum2, nmaterialmoney,npmoney,npprice,nsignnum,ndmsignnum from ic_general_bb3 where  cgeneralbid = ?");

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int iLines = 0;
        ArrayList alResult = new ArrayList();
        ArrayList alBid = null;
        UFDouble nsignednum = null;
        try
        {
          con = getConnection();

          pstmt = con.prepareStatement(sql1.toString());

          rs = pstmt.executeQuery();
          Object oTemp;
          while (rs.next())
          {
            alBid = new ArrayList();
            sHid = rs.getString("cgeneralhid");
            sBid = rs.getString("cgeneralbid");
            oTemp = rs.getObject("noutnum");
            UFDouble uNum = oTemp == null ? null : new UFDouble(oTemp.toString().trim());
            alBid.add(sHid);
            alBid.add(sBid);
            alBid.add(uNum);
            alResult.add(alBid);
          }

          if (rs != null)
            rs.close();
          if ((sHid == null) || (sBid == null))
          {
            if (pstmt != null)
              pstmt.close();
            if (con != null)
              con.close();
            oTemp = new UFBoolean(false);
            return (UFBoolean) oTemp;
          }
          if (alResult != null)
          {
            for (int j = 0; j < alResult.size(); j++)
            {
              ArrayList alBidtemp = (ArrayList)alResult.get(j);
              sHid = (String)alBidtemp.get(0);
              sBid = (String)alBidtemp.get(1);
              UFDouble ndmsignnum = alBidtemp.get(2) == null ? new UFDouble(0) : (UFDouble)alBidtemp.get(2);

              pstmt = con.prepareStatement(sql3.toString());

              pstmt.setString(1, sBid);
              iLines = pstmt.executeUpdate();

              pstmt = con.prepareStatement(sql5.toString());
              pstmt.setString(1, sBid);
              rs = pstmt.executeQuery();
              ArrayList albb3 = new ArrayList();
              while (rs.next()) {
                albb3.add(rs.getObject("nsignnum"));
              }

              pstmt = con.prepareStatement(sql4.toString());
              String sBB3 = getOID(sPk_corp);
              pstmt.setString(1, sBB3);
              pstmt.setString(2, sHid);
              pstmt.setString(3, sBid);

              pstmt.setNull(4, 3);
              if (albb3.get(0) == null)
                pstmt.setNull(5, 3);
              else
                pstmt.setBigDecimal(5, new BigDecimal(albb3.get(0).toString()));
              pstmt.executeUpdate();
            }

          }

        }
        finally
        {
          try
          {
            if (rs != null) {
              rs.close();
            }
          }
          catch (Exception e)
          {
          }
          try
          {
            if (pstmt != null)
            {
              pstmt.close();
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

        if (alResult == null)
          continue;
        for (int k = 0; k < alResult.size(); k++)
        {
          ArrayList alBidtemp = (ArrayList)alResult.get(k);
          sHid = (String)alBidtemp.get(0);
          sBid = (String)alBidtemp.get(1);
          UFDouble ndmsignnum = new UFDouble(0);

          ISOToIC_DRP saleDMO = (ISOToIC_DRP)NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());
          saleDMO.setSignNum(sPk_corp, sHid, sBid, ndmsignnum);
        }

      }

      return new UFBoolean(true);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return new UFBoolean(false);
  }

  public UFBoolean saveOutDM(AggregatedValueObject[] vosAgg)
    throws BusinessException
  {
    if ((vosAgg == null) || (vosAgg.length == 0)) {
      return new UFBoolean(false);
    }
    GeneralBillVO[] vos = (GeneralBillVO[])(GeneralBillVO[])vosAgg;

    ArrayList alreturn = null;
    try
    {
      FormulaParse f = new FormulaParse();

      String[] headformulas = { "cdispatcherid->getColValue(bd_busitype,receipttype,pk_busitype,cbiztype)" };

      SmartVOUtilExt.execFormulas(headformulas, (GeneralBillHeaderVO[])(GeneralBillHeaderVO[])SmartVOUtilExt.getHeadVOs(vos), f);

      String[] formulas = { "cinvbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)", "cquotecurrency->getColValue(so_saleorder_b,ccurrencytypeid,corder_bid,cfirstbillbid)", "nquoteprice->getColValue(so_saleorder_b,norgqttaxnetprc,corder_bid,cfirstbillbid)" };

      GeneralBillItemVO[] itemvos = (GeneralBillItemVO[])(GeneralBillItemVO[])SmartVOUtilExt.getBodyVOs(vos);

      SmartVOUtilExt.execFormulas(formulas, itemvos, f);

      ArrayList lastitemvos = new ArrayList();
      for (int i = 0; i < itemvos.length; i++) {
        if ((itemvos[i].getCastunitid() != null) && (itemvos[i].getCastunitid().length() > 0)) {
          lastitemvos.add(itemvos[i]);
        }
      }
      if (lastitemvos.size() > 0) {
        itemvos = (GeneralBillItemVO[])(GeneralBillItemVO[])lastitemvos.toArray(new GeneralBillItemVO[lastitemvos.size()]);

        formulas = new String[] { "fixedflag->getColValue2(bd_convert,fixedflag,pk_invbasdoc,cinvbasid,pk_measdoc,castunitid)", "mainmeasrate->getColValue2(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasid,pk_measdoc,castunitid)" };

        SmartVOUtilExt.execFormulas(formulas, itemvos, f);
        for (int i = 0; i < itemvos.length; i++) {
          Object otemp = itemvos[i].getAttributeValue("fixedflag");
          if ((otemp != null) && (otemp.toString().trim().equalsIgnoreCase("Y"))) {
            UFDouble mainmeasrate = CheckTools.toUFDouble(itemvos[i].getAttributeValue("mainmeasrate"));
            if (mainmeasrate != null) {
              itemvos[i].setHsl(mainmeasrate);
              itemvos[i].setNshouldoutassistnum(ICGenVO.div(itemvos[i].getNshouldoutnum(), mainmeasrate));
            }
          }
          else if (itemvos[i].getNshouldoutassistnum() != null) {
            itemvos[i].setHsl(ICGenVO.div(itemvos[i].getNshouldoutnum(), itemvos[i].getNshouldoutassistnum()));
          }
        }
      }

      CheckDMO cdmo = new CheckDMO();

      nc.vo.ic.pub.GenMethod.convertICAssistNumAtBs(vos);
      vos = nc.bs.ic.pub.GenMethod.splitTrgVOByIC(vos, "cwarehouseid", "4C");

      RewriteDMO redmo = new RewriteDMO();
      for (int i = 0; i < vos.length; i++)
      {
          String vBillcode = vos[i].getHeaderVO().getVbillcode();
          if(vBillcode == null)
              cdmo.setBillCode(vo);

        GeneralBillItemVO[] bodyvos = vos[i].getItemVOs();
        String vfirstrowno = null;
        UFDouble nfirstrowno = null;
        int k = 0; for (int loopk = bodyvos.length; k < loopk; k++) {
          vfirstrowno = (String)bodyvos[k].getAttributeValue("vfirstrowno");

          if ((vfirstrowno == null) || (vfirstrowno.trim().length() <= 0))
            nfirstrowno = ICGenVO.duf0;
          else {
            try {
              nfirstrowno = new UFDouble(vfirstrowno.trim());
            } catch (Exception e) {
              SCMEnv.error(e);
              nfirstrowno = ICGenVO.duf0;
            }
          }
          bodyvos[k].setTempValue("nfirstrowno", nfirstrowno);
        }
        ArrayList al = SortMethod.sortByKeys(new String[] { "nfirstrowno" }, null, bodyvos);

        if ((al != null) && (al.size() > 1) && (al.get(1) != null)) {
          vos[i].setChildrenVO((CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])al.get(1));
        }

        BillRowNoDMO.setVORowNoByRule(vos[i], vos[i].getParentVO().getAttributeValue("cbilltypecode").toString(), "crowno");

        CheckDMO.appendInvWhInfo(vos[i]);
        cdmo.checkInvalidateDate(vos[i]);
        cdmo.checkOutVO(vos[i]);
        alreturn = insertThisShldBill(vos[i]);

        if (BillTypeConst.m_saleOut.equals(vos[i].getParentVO().getAttributeValue("cbilltypecode")))
        {
          redmo.reWriteSaleNewBatch(vos[i], null);
        } else if (BillTypeConst.m_allocationOut.equals(vos[i].getParentVO().getAttributeValue("cbilltypecode")))
        {
          redmo.reWriteTranOrder(vos[i], null);
        }
        cdmo.checkBillCode(vos[i]);

        if (i < vosAgg.length)
          vosAgg[i] = vos[i];
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    return alreturn != null ? new UFBoolean(true) : new UFBoolean(false);
  }

  private void setData(ResultSet rs, DMDataVO invvo, ResultSetMetaData meta)
    throws SQLException
  {
    Object itemID = new String();

    for (int i = 0; i < meta.getColumnCount(); i++) {
      String columnname = meta.getColumnName(i + 1).toLowerCase().trim();

      if ((meta.getColumnType(i + 1) == 12) || (meta.getColumnType(i + 1) == 1))
      {
        itemID = rs.getString(columnname);
      }
      else itemID = rs.getObject(columnname);

      if (itemID != null)
        invvo.setAttributeValue(columnname, itemID);
      else
        invvo.setAttributeValue(columnname, null);
    }
  }

  private void setData(ResultSet rs, CircularlyAccessibleValueObject voBillItem, ResultSetMetaData meta, int iStart, int iEnd)
    throws SQLException
  {
    Object oValue = null;
    String sColumnName = null;
    for (int i = iStart; i <= iEnd; i++) {
      oValue = null;
      sColumnName = meta.getColumnName(i).trim().toLowerCase();
      oValue = rs.getObject(i);

      if ((sColumnName != null) && (oValue != null))
        voBillItem.setAttributeValue(sColumnName, oValue);
    }
  }

  private void setHeaderData(ResultSet rs, CircularlyAccessibleValueObject voBillHead, ResultSetMetaData meta, int iStart, int iEnd)
    throws SQLException
  {
    Object oValue = null;
    String sColumnName = null;
    String sColumnNameVO = null;
    for (int i = iStart; i <= iEnd; i++) {
      oValue = null;
      sColumnName = meta.getColumnName(i).trim().toLowerCase();
      if (sColumnName.endsWith("h"))
        sColumnNameVO = sColumnName.substring(0, sColumnName.lastIndexOf("h"));
      else {
        sColumnNameVO = sColumnName;
      }
      oValue = rs.getObject(i);

      if (oValue != null)
        voBillHead.setAttributeValue(sColumnNameVO, oValue);
    }
  }

  public UFBoolean setIssueNum(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null)
      return new UFBoolean(false);
    String SQL = " update ic_allocation_b set nreservednum = isnull(nreservednum,0) + ? where callocationhid =? and callocationbid = ?  and dr = 0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    int iUpdatedRowNum = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < dmvo.length; i++) {
        if (dmvo[i].getAttributeValue("ndelivernum") != null) {
          stmt.setBigDecimal(1, ((UFDouble)dmvo[i].getAttributeValue("ndelivernum")).toBigDecimal());
        }
        else
        {
          stmt.setNull(1, 3);
        }if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(2, dmvo[i].getAttributeValue("pkbillh").toString());
        else
          stmt.setNull(2, 12);
        if (dmvo[i].getAttributeValue("pkbillb") != null)
          stmt.setString(3, dmvo[i].getAttributeValue("pkbillb").toString());
        else {
          stmt.setNull(3, 12);
        }
        iUpdatedRowNum = stmt.executeUpdate();
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    } finally {
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
    if (iUpdatedRowNum != 0) {
      return new UFBoolean(true);
    }
    return new UFBoolean(false);
  }

  public UFBoolean setOutNum(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null) {
      return new UFBoolean(false);
    }

    ArrayList alrow = null;
    ArrayList aldata = new ArrayList();
    for (int i = 0; i < dmvo.length; i++)
    {
      alrow = new ArrayList();
      alrow.add(dmvo[i].getAttributeValue("pkbillh"));
      alrow.add(dmvo[i].getAttributeValue("pkbillb"));
      alrow.add(dmvo[i].getAttributeValue("noutnum"));
      alrow.add(dmvo[i].getAttributeValue("pk_corp"));

      aldata.add(alrow);
    }

    checkAlloBillOutNum(aldata);

    checkAlloBillNum(aldata);

    checkAlloBillOnWayNum(aldata);

    String SQL = " update ic_allocation_b set noutnum = ISNULL(noutnum,0.0) + ? where callocationhid =? and callocationbid = ?  ";
    Connection con = null;
    PreparedStatement stmt = null;
    int iUpdatedRowNum = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < dmvo.length; i++)
      {
        if (dmvo[i].getAttributeValue("noutnum") != null)
          stmt.setBigDecimal(1, ((UFDouble)dmvo[i].getAttributeValue("noutnum")).toBigDecimal());
        else
          stmt.setNull(1, 3);
        if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(2, dmvo[i].getAttributeValue("pkbillh").toString());
        else
          stmt.setNull(2, 12);
        if (dmvo[i].getAttributeValue("pkbillb") != null)
          stmt.setString(3, dmvo[i].getAttributeValue("pkbillb").toString());
        else {
          stmt.setNull(3, 12);
        }
        iUpdatedRowNum = stmt.executeUpdate();
      }
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    finally
    {
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
    if (iUpdatedRowNum != 0) {
      return new UFBoolean(true);
    }
    return new UFBoolean(false);
  }

  public UFBoolean setOutSignAndBackNum(DMDataVO[] dmvo, boolean isWriteBackDelivBill)
    throws BusinessException
  {
    if (dmvo == null) {
      return new UFBoolean(false);
    }

    String sql2 = " update ic_general_bb3 set ndmsignnum = isnull(ndmsignnum,0) + ? where cgeneralbid =?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sql2);
      for (int i = 0; i < dmvo.length; i++)
      {
        if (dmvo[i].getAttributeValue("dsignnum") != null) {
          stmt.setBigDecimal(1, ((UFDouble)dmvo[i].getAttributeValue("dsignnum")).toBigDecimal());
        }
        else
        {
          stmt.setNull(1, 3);
        }

        stmt.setString(2, dmvo[i].getAttributeValue("pk_sourcebill_b").toString());

        stmt.executeUpdate();
      }
      stmt.executeBatch();

      if (isWriteBackDelivBill)
      {
        IDMToIC admo = (IDMToIC)NCLocator.getInstance().lookup(IDMToIC.class.getName());
        admo.setSignNum(dmvo, false, false);
      }
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    } finally {
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
    return new UFBoolean(false);
  }

  public UFBoolean setOutSignNum(DMDataVO[] dmVO)
    throws BusinessException
  {
    if (dmVO == null) {
      return new UFBoolean(false);
    }
    UFDouble nsignnum = null;
    String sBid = null;
    String sHid = null;
    String sSourceBid = null;
    String sSourceHid = null;
    String sPk_corp = null;
    UFDouble nTotalSignNum = null;
    UFDouble nZero = new UFDouble(0);
    for (int i = 0; i < dmVO.length; i++) {
      sSourceHid = (String)dmVO[i].getAttributeValue("pk_delivbill_h");
      sSourceBid = (String)dmVO[i].getAttributeValue("pk_delivbill_b");
      nsignnum = dmVO[i].getAttributeValue("nsignnum") == null ? nZero : (UFDouble)dmVO[i].getAttributeValue("nsignnum");

      sPk_corp = (String)dmVO[i].getAttributeValue("pkcorpforgenoid");
      StringBuffer sql1 = new StringBuffer(" Select ic_general_b.cgeneralhid, ic_general_b.cgeneralbid ,isnull(noutnum,0.0) as noutnum From ic_general_b  where csourcebillhid ='" + sSourceHid + "' And csourcebillbid = '" + sSourceBid + "' And ic_general_b.dr = 0 and  ic_general_b.noutnum is not null  order by ic_general_b.cgeneralhid,cast(ic_general_b.crowno as numeric(20,8)) ");

      StringBuffer sql2 = new StringBuffer(" insert into ic_general_bb3 (cgeneralbb3,cgeneralhid,cgeneralbid,ndmsignnum,nsignnum) values(?,?,?,?,?)");

      StringBuffer sql3 = new StringBuffer(" update ic_general_bb3 set dr = 1 where cgeneralbid = ? ");

      StringBuffer sql4 = new StringBuffer(" Select sum(isnull(ndmsignnum,0.0)) as ndmsignnum From ic_general_b left outer join ic_general_bb3 on(ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid ) where csourcebillhid ='" + sSourceHid + "' And csourcebillbid = '" + sSourceBid + "' And ic_general_b.dr = 0 And (ic_general_bb3.dr = 0 or ic_general_bb3.dr is null )  ");

      StringBuffer sql5 = new StringBuffer(" Select caccountunitid, cpk1,cpk2,naccountmny, naccountnum1,naccountnum2, nmaterialmoney,npmoney,npprice,nsignnum from ic_general_bb3 where  cgeneralbid = ?");

      Connection con = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int iLines = 0;
      ArrayList alResult = new ArrayList();
      ArrayList alBid = null;
      UFDouble nsignednum = null;
      try
      {
        con = getConnection();
        pstmt = con.prepareStatement(sql4.toString());
        rs = pstmt.executeQuery();
        while (rs.next()) {
          Object oNum = rs.getObject("ndmsignnum");
          nsignednum = oNum == null ? nZero : new UFDouble(oNum.toString());
        }
        pstmt = con.prepareStatement(sql1.toString());

        rs = pstmt.executeQuery();
        Object oTemp;
        while (rs.next()) {
          alBid = new ArrayList();
          sHid = rs.getString("cgeneralhid");
          sBid = rs.getString("cgeneralbid");
          oTemp = rs.getObject("noutnum");
          UFDouble uNum = oTemp == null ? null : new UFDouble(oTemp.toString().trim());
          alBid.add(sHid);
          alBid.add(sBid);
          alBid.add(uNum);
          alResult.add(alBid);
        }

        if (rs != null)
          rs.close();
        if ((sHid == null) || (sBid == null)) {
          if (pstmt != null)
            pstmt.close();
          if (con != null)
            con.close();
          oTemp = new UFBoolean(false);
          return (UFBoolean) oTemp;
        }
        nTotalSignNum = nsignnum.add(nsignednum == null ? nZero : nsignednum);
        computeOutSignNum(alResult, nsignnum.add(nsignednum));

        if (alResult != null)
          for (int j = 0; j < alResult.size(); j++) {
            ArrayList alBidtemp = (ArrayList)alResult.get(j);
            sHid = (String)alBidtemp.get(0);
            sBid = (String)alBidtemp.get(1);
            UFDouble ndmsignnum = alBidtemp.get(2) == null ? nZero : (UFDouble)alBidtemp.get(2);

            pstmt = con.prepareStatement(sql3.toString());

            pstmt.setString(1, sBid);
            iLines = pstmt.executeUpdate();

            pstmt = con.prepareStatement(sql5.toString());
            pstmt.setString(1, sBid);
            rs = pstmt.executeQuery();
            ArrayList albb3 = new ArrayList();
            while (rs.next()) {
              albb3.add(rs.getObject("nsignnum"));
            }

            pstmt = con.prepareStatement(sql2.toString());
            String sBB3 = getOID(sPk_corp);
            pstmt.setString(1, sBB3);
            pstmt.setString(2, sHid);
            pstmt.setString(3, sBid);
            pstmt.setBigDecimal(4, ndmsignnum.toBigDecimal());
            if ((albb3.size() == 0) || (albb3.get(0) == null))
              pstmt.setNull(5, 3);
            else
              pstmt.setBigDecimal(5, new BigDecimal(albb3.get(0).toString()));
            pstmt.executeUpdate();
          }
      }
      catch (Exception e)
      {
        nc.bs.ic.pub.GenMethod.throwBusiException(e);
      } finally {
        try {
          if (rs != null)
            rs.close();
        }
        catch (Exception e) {
        }
        try {
          if (pstmt != null)
            pstmt.close();
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
      if (alResult != null) {
        for (int k = 0; k < alResult.size(); k++) {
          ArrayList alBidtemp = (ArrayList)alResult.get(k);
          sHid = (String)alBidtemp.get(0);
          sBid = (String)alBidtemp.get(1);
          UFDouble ndmsignnum = alBidtemp.get(2) == null ? nZero : (UFDouble)alBidtemp.get(2);
          try
          {
            ISOToIC_DRP saleDMO = (ISOToIC_DRP)NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());
            saleDMO.setSignNum(sPk_corp, sHid, sBid, ndmsignnum);
          }
          catch (Exception e) {
            nc.bs.ic.pub.GenMethod.throwBusiException(e);
          }
        }
      }
    }

    return new UFBoolean(true);
  }

  public UFBoolean setPlanIssueNum(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null)
      return new UFBoolean(false);
    try
    {
      checkAlloBillStatus(dmvo);

      initInitIds(dmvo, true);

      initInitIds(dmvo, false);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    boolean borderstatus = false;

    boolean isclose = false;
    ArrayList alAtp = new ArrayList();
    ArrayList alrow = null;

    String SQL = " update ic_allocation_b set ndelivernum = isnull(ndelivernum,0) + ?, fbillrowflag = ? where dr = 0 and callocationhid =? and callocationbid = ?  ";

    String SQLcopy = " update ic_allocation_b set ndelivernum = isnull(ndelivernum,0) + ? where dr = 0 and callocationhid =? and callocationbid = ?  ";

    String SQL1 = " select Distinct fbillrowflag from ic_allocation_b where  dr = 0 and callocationhid = ?  and fbillrowflag = ? ";

    String SQL2 = "update ic_allocation_h set fbillflag = ? where dr = 0 and callocationhid = ? ";
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int iUpdatedRowNum = 0;
    try {
      con = getConnection();

      for (int i = 0; i < dmvo.length; i++) {
        if (dmvo[i].getAttributeValue("orderstatus") != null)
          borderstatus = true;
        else {
          borderstatus = false;
        }
        stmt = con.prepareStatement(borderstatus ? SQL : SQLcopy);
        if (dmvo[i].getAttributeValue("ndelivernum") != null)
          stmt.setBigDecimal(1, ((UFDouble)dmvo[i].getAttributeValue("ndelivernum")).toBigDecimal());
        else
          stmt.setNull(1, 3);
        if (borderstatus)
        {
          stmt.setInt(2, dmvo[i].getAttributeValue("orderstatus").toString().equalsIgnoreCase("Y") ? 5 : 4);
        }

        if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(borderstatus ? 3 : 2, dmvo[i].getAttributeValue("pkbillh").toString());
        else
          stmt.setNull(borderstatus ? 3 : 2, 12);
        if (dmvo[i].getAttributeValue("pkbillb") != null)
          stmt.setString(borderstatus ? 4 : 3, dmvo[i].getAttributeValue("pkbillb").toString());
        else {
          stmt.setNull(borderstatus ? 4 : 3, 12);
        }
        iUpdatedRowNum = stmt.executeUpdate();
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
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
    try
    {
      updateABATP(dmvo);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    if (iUpdatedRowNum != 0) {
      return new UFBoolean(true);
    }
    return new UFBoolean(false);
  }

  public UFBoolean setSignAndBackNum(DMDataVO[] dmvo)
    throws BusinessException
  {
    if (dmvo == null)
      return new UFBoolean(false);
    String SQL = " update ic_allocation_b set nsignnum = isnull(nsignnum,0) + ? , nwithdrawnum = isnull(nwithdrawnum,0)+ ? where callocationhid =? and callocationbid = ?  ";

    Connection con = null;
    PreparedStatement stmt = null;
    int iUpdatedRowNum = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(SQL);
      for (int i = 0; i < dmvo.length; i++) {
        if (dmvo[i].getAttributeValue("nsignnum") != null) {
          stmt.setBigDecimal(1, ((UFDouble)dmvo[i].getAttributeValue("nsignnum")).toBigDecimal());
        }
        else
        {
          stmt.setNull(1, 3);
        }if (dmvo[i].getAttributeValue("nbacknum") != null) {
          stmt.setBigDecimal(2, ((UFDouble)dmvo[i].getAttributeValue("nbacknum")).toBigDecimal());
        }
        else
        {
          stmt.setNull(2, 3);
        }if (dmvo[i].getAttributeValue("pkbillh") != null)
          stmt.setString(3, dmvo[i].getAttributeValue("pkbillh").toString());
        else
          stmt.setNull(3, 12);
        if (dmvo[i].getAttributeValue("pkbillb") != null)
          stmt.setString(4, dmvo[i].getAttributeValue("pkbillb").toString());
        else {
          stmt.setNull(4, 12);
        }
        iUpdatedRowNum = stmt.executeUpdate();
      }
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    } finally {
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
    if (iUpdatedRowNum != 0) {
      return new UFBoolean(true);
    }
    return new UFBoolean(false);
  }

  private void updateABATP(DMDataVO[] addvos)
    throws Exception
  {
    if (addvos == null) {
      return;
    }
    AllocationHVO[] ABVOs = null;

    ABVOs = getABVOs(addvos, true);
    ABInATP aABInATP = null;
    ABOutATP aABOutATP = null;

    if ((ABVOs != null) && (ABVOs.length > 0)) {
      aABOutATP = new ABOutATP();
      aABInATP = new ABInATP();
      for (int i = 0; i < ABVOs.length; i++) {
        aABOutATP.modifyATPWhenCloseBill(ABVOs[i]);
      }

    }

    ABVOs = getABVOs(addvos, false);
    if ((ABVOs != null) && (ABVOs.length > 0)) {
      if (aABOutATP == null)
        aABOutATP = new ABOutATP();
      if (aABInATP == null)
        aABInATP = new ABInATP();
      for (int i = 0; i < ABVOs.length; i++)
        aABOutATP.modifyATPWhenOpenBill(ABVOs[i]);
    }
  }
}