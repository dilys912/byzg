package nc.impl.scm.so.so001;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.para.SysInitBO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.IBackCheckState;
import nc.bs.pub.pf.ICheckState;
import nc.bs.pub.pf.IPfPersonFilter2;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.ic.freeitem.DefdefDMO;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.CommonDataDMO;
import nc.bs.scm.pub.redun.IRedunSource;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.uap.lock.PKLock;
import nc.impl.scm.so.pub.BusinessControlDMO;
import nc.impl.scm.so.pub.CheckValueValidityImpl;
import nc.impl.scm.so.pub.DataControlDMO;
import nc.impl.scm.so.pub.DoInterfaceForSO;
import nc.impl.scm.so.pub.FetchValueDMO;
import nc.impl.scm.so.pub.GeneralSqlString;
import nc.impl.scm.so.so012.IncomeAdjust;
import nc.impl.scm.so.so012.SquareDMO;
import nc.impl.scm.so.so016.BalanceDMO;
import nc.impl.scm.so.so016.BillConvertDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.impl.scm.so.so017.ChannelGroupDMO;
import nc.impl.scm.so.so103.BuyLargessImpl;
import nc.impl.scm.sp.sp015.PreorderDMO;
import nc.impl.so.sointerface.SOATP;
import nc.impl.so.sointerface.SaleToICDRPDMO;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.dm.service.IDMToTO;
import nc.itf.ic.service.IICPub_GeneralBillDMO;
import nc.itf.ic.service.IICPub_InvATPDMO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.ic.service.IICToSO;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.sf.IServiceProviderSerivce;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.vo.dm.pub.DMVO;
import nc.vo.ic.ic004.StoreadminBodyVO;
import nc.vo.pf.change.IChangeVOCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.pf.IPFSourceBillFinder;
import nc.vo.pub.pf.SourceBillInfo;
import nc.vo.scm.ct.TypeVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.exp.DelDlvPlanException;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;
import nc.vo.sm.log.OperatelogVO;
import nc.vo.so.credit.BillCreditOriginVO;
import nc.vo.so.pub.ProdNotInstallException;
import nc.vo.so.service.ToRetOrdVO;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so001.Deal7DException;
import nc.vo.so.so001.SOField;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.OrdVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so103.BuylargessBVO;
import nc.vo.so.so103.BuylargessHVO;
import nc.vo.so.so103.BuylargessVO;
import nc.vo.to.pub.BillVO;
import nc.vo.uap.rbac.RoleVO;

public class SaleOrderDMO extends DataManageObject
  implements IQueryData, IQueryData2, ICheckState, IBackCheckState, IRedunSource, IPfPersonFilter2, IChangeVOCheck, IPFSourceBillFinder
{
  UFBoolean SO39 = null;

  public SaleOrderDMO() throws NamingException
  {
  }

  public SaleOrderDMO(String dbName) throws NamingException, nc.bs.pub.SystemException
  {
    super(dbName);
  }

  public boolean checkGoing(String strBillID, String strApproveId, String strApproveDate, String checkNote)
    throws BusinessException
  {
    try
    {
      long s = System.currentTimeMillis();
      SCMEnv.out("审批订单后台更改审批状态开始...[正在审批]：");

      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("30", strBillID, null, null, 7);

      SCMEnv.out("审批订单后台更改审批状态用时[正在审批]：[" + (System.currentTimeMillis() - s) + "]" + "ms");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return true;
  }

  public boolean checkNoPass(String strBillID, String strApproveID, String strApproveDate, String checkNote)
    throws BusinessException
  {
    try
    {
      long s = System.currentTimeMillis();
      SCMEnv.out("审批订单后台更改审批状态开始...[审批未通过]：");

      SOATP atp = new SOATP();
      SaleOrderVO saleorder = queryData(strBillID);
      if (saleorder != null)
      {
        int i = 0; for (int loop = saleorder.getBodyVOs().length; i < loop; i++) {
          saleorder.getBodyVOs()[i].setIAction(12);
        }

        IBillInvokeCreditManager armanager = DoInterfaceForSO.getBillInvokeCreditManager();

        saleorder.setIAction(11);
        renovateAR(armanager, saleorder);
        SaleOrderVO vo = saleorder.getOrdVOOfSaleCorp();
        if ((vo != null) && (vo.getBodyVOs() != null) && (vo.getBodyVOs().length > 0))
        {
          atp.modifyATPWhenCloseBill(vo);
        }

      }

      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("30", strBillID, null, null, 8);

      SCMEnv.out("审批订单后台更改审批状态用时[审批未通过]：[" + (System.currentTimeMillis() - s) + "]" + "ms");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return true;
  }

  public boolean checkPass(String strBillID, String strApproveId, String strApproveDate, String strCheckNote)
    throws BusinessException
  {
    try
    {
      long s = System.currentTimeMillis();

      SCMEnv.out("审批订单后台更改审批状态开始...[审批通过]：");

      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("30", strBillID, strApproveId, strApproveDate, 2);

      SCMEnv.out("审批订单后台更改审批状态用时[审批通过]：[" + (System.currentTimeMillis() - s) + "]" + "ms");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return true;
  }

  public void backGoing(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
  }

  public void backNoState(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
    try
    {
      long s = System.currentTimeMillis();

      SCMEnv.out("驳回订单后台更改审批状态开始...[驳回]");

      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("30", billId, null, null, 1);

      SCMEnv.out("驳回订单后台更改审批状态用时[驳回]：[" + (System.currentTimeMillis() - s) + "]" + "ms");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  private void checkStoreStructure(SaleOrderVO saleVO, String newHeadID)
    throws BusinessRuntimeException, BusinessException
  {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT DISTINCT so_saleorder_b.cinventoryid1, bd_produce.pk_calbody ");

    sql.append("FROM so_saleorder_b LEFT OUTER JOIN ");
    sql.append("bd_produce ON bd_produce.pk_invmandoc = so_saleorder_b.cinventoryid1 AND ");

    sql.append("bd_produce.pk_calbody = so_saleorder_b.cadvisecalbodyid ");
    sql.append("where so_saleorder_b.csaleid=?");

    SaleorderBVO[] saleBVOs = (SaleorderBVO[])(SaleorderBVO[])saleVO.getChildrenVO();
    Connection con = null;
    PreparedStatement stmt = null;
    if (saleBVOs != null)
      try {
        StringBuffer sbfErr = new StringBuffer();
        HashMap htTemp = new HashMap();
        String invid = null;
        String calbodyid = null;
        con = getConnection();
        stmt = con.prepareStatement(sql.toString());
        stmt.setString(1, newHeadID);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          invid = rs.getString(1);
          invid = invid == null ? null : invid.trim();
          calbodyid = rs.getString(2);
          calbodyid = calbodyid == null ? null : calbodyid.trim();
          if ((invid == null) || (invid.length() <= 0) || (calbodyid == null) || (calbodyid.length() <= 0))
            continue;
          htTemp.put(invid, calbodyid);
        }

        rs.close();
        Object oTemp = null;
        for (int i = 0; i < saleBVOs.length; i++)
        {
          if (saleBVOs[i].getStatus() == 3) {
            continue;
          }
          if ((saleBVOs[i].getLaborflag() != null) && (saleBVOs[i].getLaborflag().booleanValue()))
          {
            continue;
          }
          if ((saleBVOs[i].getDiscountflag() != null) && (saleBVOs[i].getDiscountflag().booleanValue()))
          {
            continue;
          }
          if ((saleBVOs[i].getBoosflag() != null) && (saleBVOs[i].getBoosflag().booleanValue())) {
            continue;
          }
          if ((saleBVOs[i].getCadvisecalbodyid() == null) || (saleBVOs[i].getCadvisecalbodyid().trim().length() <= 0))
          {
            continue;
          }

          oTemp = htTemp.get(saleBVOs[i].getCinventoryid1());
          if (oTemp == null) {
            sbfErr.append(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000002", null, new String[] { saleBVOs[i].getCrowno() == null ? "" : saleBVOs[i].getCrowno() }));

            sbfErr.append("\n");
          }

        }

        if (sbfErr.toString().trim().length() > 0)
          throw new BusinessException(sbfErr.toString());
      }
      catch (SQLException e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessRuntimeException(e.getMessage());
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
  }

  public void delete(String key)
  {
    try
    {
      deleteBodys(key);
      deleteHead(key);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public void deleteBodyAndExe(PreparedStatement stmtdeleteBody, PreparedStatement stmtdeleteExe, String key)
  {
    try
    {
      stmtdeleteBody.setString(1, key);
      executeUpdate(stmtdeleteBody);

      stmtdeleteExe.setString(1, key);
      executeUpdate(stmtdeleteExe);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public void deleteBody(String key)
  {
    String sql = "delete from so_saleorder_b where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);
      stmt.executeUpdate();

      deleteFollowBody(key);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
  }

  public void deleteBodys(String key)
  {
    String sql = "delete from so_saleorder_b where csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);
      stmt.executeUpdate();
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
  }

  public void deleteFollowBody(String key)
  {
    String sql = "delete from so_saleexecute where csale_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);
      stmt.executeUpdate();
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
  }

  public void deleteHead(String key)
  {
    String sql = "delete from so_sale where csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);
      stmt.executeUpdate();
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
  }

  public TypeVO[] getAllContractType(String sWhere)
  {
    if ((sWhere == null) || (sWhere.length() <= 0))
      return null;
    String sql = " SELECT ct_manage.pk_ct_manage,ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle  FROM ct_manage INNER JOIN  ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type ";

    sql = sql + " WHERE " + sWhere;
    TypeVO type = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ArrayList alist = new ArrayList();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        type = new TypeVO(rs.getString(1));

        Integer nbusitype = (Integer)rs.getObject(2);
        type.setNbusitype(nbusitype == null ? null : nbusitype);

        Integer ninvctlstyle = (Integer)rs.getObject(3);
        type.setNinvctlstyle(ninvctlstyle == null ? null : ninvctlstyle);

        Integer ndatactlstyle = (Integer)rs.getObject(4);
        type.setNdatactlstyle(ndatactlstyle == null ? null : ndatactlstyle);

        alist.add(type);
      }
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
    if (alist.size() <= 0)
      return null;
    return (TypeVO[])(TypeVO[])alist.toArray(new TypeVO[0]);
  }

  public Hashtable getAnyValue(String tableName, String fieldName, String key, String[] values)
    throws SQLException
  {
    if ((tableName == null) || (fieldName == null) || (key == null) || (values == null) || (values.length <= 0))
    {
      return null;
    }
    Hashtable table = new Hashtable();

    ArrayList ids = new ArrayList();
    for (int i = 0; i < values.length; i++) {
      if ((values[i] == null) || (values[i].equals("")) || (ids.contains(values[i])))
        continue;
      ids.add(values[i]);
    }

    StringBuffer sWhere = new StringBuffer();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int onenum = 200;
    try
    {
      con = getConnection();

      int i = 0; int count = ids.size() % onenum == 0 ? ids.size() / onenum : ids.size() / onenum + 1;
      for (; i < count; i++)
      {
        String sql = " select " + key + ", " + fieldName + " from " + tableName + " where " + key + " in ( ";

        sWhere.setLength(0);
        int j = 0; int count1 = ids.size() - i * onenum > onenum ? onenum : ids.size() - i * onenum;
        for (; j < count1; j++) {
          if (j > 0) {
            sWhere.append(" , ");
          }
          sWhere.append("'" + ids.get(j + i * onenum).toString() + "'");
        }

        sWhere.append(" ) ");

        sql = sql + sWhere.toString();
        stmt = con.prepareStatement(sql);
        rs = stmt.executeQuery();

        String keyValue = null;
        String fieldValue = null;
        while (rs.next()) {
          keyValue = rs.getString(1);
          if ((keyValue == null) || (keyValue.trim().equals("")))
            continue;
          fieldValue = rs.getString(2);
          if ((fieldValue == null) || (fieldValue.trim().equals("")) || 
            (table.containsKey(keyValue))) continue;
          table.put(keyValue, fieldValue);
        }
        rs.close();
        stmt.close();
      }
    }
    finally {
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
    return table;
  }

  public String[][] getBomChildInfo(String pk_corp, String invID)
    throws SQLException
  {
    String strSql = "SELECT distinct mm_pzbom_b.wlbmid, mm_pzbom_b.sfslkx, mm_pzbom_b.slsx,mm_pzbom_b.slxx, mm_pzbom_b.sl, mm_pzbom_b.jldwid, mm_pzbom_b.sfkx, mm_pzbom_b.bz,mm_pzbom_b.wlglid, mm_pzbom_b.sfqs FROM mm_pzbom INNER JOIN mm_pzbom_b ON mm_pzbom_b.pk_pzbomid = mm_pzbom.pk_pzbomid WHERE mm_pzbom.pk_corp='" + pk_corp + "' and mm_pzbom.wlbmid= '" + invID + "' ";

    Connection con = null;
    PreparedStatement stmt = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Vector vLine = new Vector();
        String obj = rs.getString(1);
        vLine.addElement(obj == null ? null : obj.toString().trim());
        Object o = rs.getObject(2);
        vLine.addElement(o == null ? null : o.toString().trim());
        o = rs.getObject(3);
        vLine.addElement(o == null ? null : o.toString().trim());
        o = rs.getObject(4);
        vLine.addElement(o == null ? null : o.toString().trim());
        o = rs.getObject(5);
        vLine.addElement(o == null ? null : o.toString().trim());
        obj = rs.getString(6);
        vLine.addElement(obj == null ? null : obj.toString().trim());

        o = rs.getObject(7);
        vLine.addElement(o == null ? null : o.toString().trim());
        obj = rs.getString(8);
        vLine.addElement(obj == null ? null : obj.toString().trim());
        obj = rs.getString(9);
        vLine.addElement(obj == null ? null : obj.toString().trim());

        obj = rs.getString(10);
        vLine.addElement(obj == null ? null : obj.toString().trim());
        v.addElement(vLine);
      }
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
    String[][] results = (String[][])null;
    if (v.size() > 0) {
      results = new String[v.size()][((Vector)v.elementAt(0)).size()];
      for (int i = 0; i < v.size(); i++) {
        for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
          Object o = ((Vector)v.elementAt(i)).elementAt(j);
          results[i][j] = (o == null ? null : o.toString());
        }
      }
    }
    return results;
  }

  public String[][] getBomInfo(String pk_corp, String invID, String curdate)
    throws SQLException
  {
    String strSql = "SELECT  mm_pzbom_b.wlbmid,bd_invbasdoc.invname,mm_pzbom_b.wlglid,bd_invmandoc.isspecialty FROM mm_pzbom_b INNER JOIN mm_pzbom ON mm_pzbom_b.pk_pzbomid = mm_pzbom.pk_pzbomid LEFT OUTER JOIN bd_invmandoc INNER JOIN bd_invbasdoc ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ON mm_pzbom_b.wlglid = bd_invmandoc.pk_invmandoc AND mm_pzbom_b.wlbmid = bd_invbasdoc.pk_invbasdoc WHERE mm_pzbom.pk_corp='" + pk_corp + "' and mm_pzbom.wlbmid = '" + invID + "' ";

    Connection con = null;
    PreparedStatement stmt = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Vector vLine = new Vector();

        String obj = rs.getString(1);
        vLine.addElement(obj == null ? "" : obj.trim());

        obj = rs.getString(2);
        vLine.addElement(obj == null ? "" : obj.trim());

        obj = rs.getString(3);
        vLine.addElement(obj == null ? "" : obj.trim());

        obj = rs.getString(4);
        vLine.addElement(obj == null ? "" : obj.trim());
        v.addElement(vLine);
      }
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
    String[][] results = (String[][])null;
    if (v.size() > 0) {
      Vector vtemp = new Vector();
      for (int i = 0; i < v.size(); i++) {
        Vector vline = (Vector)v.elementAt(i);
        if (isHaveSub(pk_corp, vline.elementAt(0).toString(), curdate))
          vtemp.add(vline);
      }
      v = vtemp;
      if (v.size() > 0) {
        results = new String[v.size()][((Vector)v.elementAt(0)).size()];
        for (int i = 0; i < v.size(); i++) {
          for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
            results[i][j] = ((Vector)v.elementAt(i)).elementAt(j).toString();
          }
        }
      }
    }
    return results;
  }

  public void freshVOStatus(SaleOrderVO saleorder)
    throws BusinessException, SQLException
  {
    if ((saleorder == null) || (saleorder.getChildrenVO() == null)) {
      return;
    }
    HashMap hs30 = SOToolsDMO.getAnyValueSORow("so_saleexecute,so_saleorder_b", new String[] { "so_saleexecute.ntotalreceiptnumber", "so_saleexecute.ntotalinventorynumber", "so_saleorder_b.frowstatus", "so_saleexecute.bifreceiptfinish", "so_saleexecute.bifinventoryfinish" }, "csale_bid", SoVoTools.getVOsOnlyValues(saleorder.getChildrenVO(), "corder_bid"), " so_saleexecute.csale_bid=so_saleorder_b.corder_bid and so_saleexecute.csaleid=so_saleorder_b.csaleid and so_saleexecute.creceipttype='30' ");

    if ((hs30 == null) || (hs30.size() <= 0)) {
      return;
    }
    SaleorderBVO[] bvos = (SaleorderBVO[])(SaleorderBVO[])saleorder.getChildrenVO();
    SORowData rowdata = null;
    for (int i = 0; i < bvos.length; i++) {
      if ((bvos[i] == null) || (SoVoTools.isEmptyString(bvos[i].getCorder_bid()))) {
        continue;
      }
      rowdata = (SORowData)hs30.get(bvos[i].getCorder_bid());
      if (rowdata == null)
        continue;
      bvos[i].setNtotalreceiptnumber(rowdata.getUFDouble(0));

      bvos[i].setFrowstatus(rowdata.getInteger(2));
      bvos[i].setBifreceiptfinish(rowdata.getUFBoolean(3));
      bvos[i].setBifinventoryfinish(rowdata.getUFBoolean(4));
    }
  }

  public ParaPoToCtRewriteVO[] getChangeFromSaleVO(SaleOrderVO saleorder)
    throws BusinessException, SQLException
  {
    if (saleorder == null) {
      return null;
    }

    SaleorderBVO[] bvo = (SaleorderBVO[])(SaleorderBVO[])saleorder.getChildrenVO();
    Vector v = new Vector();
    for (int i = 0; i < bvo.length; i++)
    {
      if (bvo[i].getAttributeValue("creceipttype") == null)
      {
        continue;
      }
      if ((!bvo[i].getAttributeValue("creceipttype").equals("Z4")) && (!bvo[i].getAttributeValue("creceipttype").equals("Z3")))
      {
        continue;
      }

      ParaPoToCtRewriteVO vo = new ParaPoToCtRewriteVO();

      vo.setCContractRowID(bvo[i].getCsourcebillbodyid());
      vo.setFirstTime(saleorder.getFirstTime());
      UFDouble number = bvo[i].getNnumber();
      if (number == null)
        number = SoVoConst.duf0;
      UFDouble nmny = bvo[i].getNsummny();
      if (nmny == null) {
        nmny = SoVoConst.duf0;
      }
      if ((bvo[i].getBlargessflag() != null) && (bvo[i].getBlargessflag().booleanValue()))
      {
        number = SoVoConst.duf0;
        nmny = SoVoConst.duf0;
      }

      switch (bvo[i].getStatus()) {
      case 2:
        break;
      case 3:
        UFDouble oldnumber = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNnumber();

        UFDouble oldnmny = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNsummny();

        if ((bvo[i].getOldbvo() != null) && (bvo[i].getOldbvo().getBlargessflag() != null) && (bvo[i].getOldbvo().getBlargessflag().booleanValue()))
        {
          oldnumber = SoVoConst.duf0;
          oldnmny = SoVoConst.duf0;
        }

        if (bvo[i].getOldbvo() != null) {
          number = SoVoConst.duf0.sub(oldnumber == null ? SoVoConst.duf0 : oldnumber);

          nmny = SoVoConst.duf0.sub(oldnmny == null ? SoVoConst.duf0 : oldnmny);
        }
        else {
          number = SoVoConst.duf0.sub(number == null ? SoVoConst.duf0 : number);

          nmny = SoVoConst.duf0.sub(nmny == null ? SoVoConst.duf0 : nmny);
        }

        break;
      case 0:
        break;
      case 1:
      case 4:
          oldnumber = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNnumber();

        if ((bvo[i].getOldbvo() != null) && (bvo[i].getOldbvo().getBlargessflag() != null) && (bvo[i].getOldbvo().getBlargessflag().booleanValue()))
        {
          oldnumber = SoVoConst.duf0;
        }
        number = number.sub(oldnumber == null ? SoVoConst.duf0 : oldnumber);

          oldnmny = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNsummny();

        if ((bvo[i].getOldbvo() != null) && (bvo[i].getOldbvo().getBlargessflag() != null) && (bvo[i].getOldbvo().getBlargessflag().booleanValue()))
        {
          oldnmny = SoVoConst.duf0;
        }
        nmny = nmny.sub(oldnmny == null ? SoVoConst.duf0 : oldnmny);
        break;
      }

      vo.setDNum(number);
      vo.setDSummny(nmny);

      v.add(vo);
    }
    ParaPoToCtRewriteVO[] rvo = null;
    if (v.size() > 0) {
      rvo = new ParaPoToCtRewriteVO[v.size()];
      v.copyInto(rvo);
    }
    return rvo;
  }

  public TypeVO getContractType(String key)
    throws SQLException
  {
    String sql = "SELECT ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle FROM ct_manage INNER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type WHERE ct_manage.pk_ct_manage = ?";

    TypeVO type = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        type = new TypeVO(key);

        Integer nbusitype = (Integer)rs.getObject(1);
        type.setNbusitype(nbusitype == null ? null : nbusitype);

        Integer ninvctlstyle = (Integer)rs.getObject(2);
        type.setNinvctlstyle(ninvctlstyle == null ? null : ninvctlstyle);

        Integer ndatactlstyle = (Integer)rs.getObject(3);
        type.setNdatactlstyle(ndatactlstyle == null ? null : ndatactlstyle);
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
      catch (Exception e) {
      }
    }
    return type;
  }

  public UFDouble getCreditMny(String sCusMID)
    throws SQLException
  {
    StringBuffer sbfSQL = new StringBuffer();
    sbfSQL.append("SELECT creditmny FROM bd_cumandoc WHERE pk_cumandoc = ? ");

    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble result = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL.toString());
      stmt.setString(1, sCusMID);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        Object sTemp = rs.getObject(1);
        result = sTemp == null ? null : new UFDouble(sTemp.toString());
      }
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
    return result;
  }

  public UFDouble getCreditMoney(String sCusMID, String sPKCorp)
    throws SQLException
  {
    StringBuffer sbfSQL = new StringBuffer();
    sbfSQL.append("SELECT creditmoney FROM bd_cumandoc ");
    sbfSQL.append("WHERE pk_cumandoc =? AND pk_corp =? and dr=0 ");
    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble result = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL.toString());
      stmt.setString(1, sCusMID);
      stmt.setString(2, sPKCorp);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        Object sTemp = rs.getObject(1);
        result = sTemp == null ? null : new UFDouble(sTemp.toString());
      }
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
    return result;
  }

  public String[][] getCtCode(String custID, String invID, String ctDate)
    throws SQLException
  {
    String sql = "SELECT ct_manage.pk_ct_manage,ct_manage.ct_code,ct_manage_b.oriprice,ct_manage_b.pk_ct_manage_b, ct_manage_b.taxration, CASE WHEN ct_manage_b.oriprice IS NULL THEN NULL ELSE ct_manage_b.oriprice * (1 + ct_manage_b.taxration / 100) END AS taxprice , ct_manage.ct_name FROM ct_manage INNER JOIN ct_manage_b ON ct_manage.pk_ct_manage = ct_manage_b.pk_ct_manage WHERE ct_manage.custid='" + custID + "' and ct_manage_b.invid='" + invID + "' " + "and ct_manage.valdate<='" + ctDate + "' and ct_manage.invallidate>='" + ctDate + "' and ct_manage.ctflag=2";

    Connection con = null;
    PreparedStatement stmt = null;
    String[][] results = (String[][])null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Vector vLine = new Vector();

        String sTemp = rs.getString(1);
        vLine.addElement(sTemp == null ? "" : sTemp.trim());

        sTemp = rs.getString(2);
        vLine.addElement(sTemp == null ? "" : sTemp.trim());

        Object oTemp = rs.getObject(3);
        vLine.addElement(oTemp == null ? "0.0" : oTemp.toString());

        sTemp = rs.getString(4);
        vLine.addElement(sTemp == null ? "" : sTemp.trim());

        Object taxration = rs.getObject(5);
        vLine.addElement(taxration == null ? "0.0" : taxration.toString());

        Object taxprice = rs.getObject(6);
        vLine.addElement(taxprice == null ? "0.0" : taxprice.toString());

        sTemp = rs.getString(7);
        vLine.addElement(sTemp == null ? "" : sTemp.trim());

        v.addElement(vLine);
      }
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
    v = getCtType(v);
    if (v.size() > 0) {
      results = new String[v.size()][((Vector)v.elementAt(0)).size()];
      for (int i = 0; i < v.size(); i++) {
        for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
          results[i][j] = ((Vector)v.elementAt(i)).elementAt(j).toString();
        }
      }
    }
    return results;
  }

  public Vector getCtType(Vector vecSource)
    throws SQLException
  {
    if ((vecSource == null) || (vecSource.size() == 0))
      return vecSource;
    String sql = "SELECT ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle FROM ct_manage INNER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type WHERE ct_manage.pk_ct_manage = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    Vector vecContract = (Vector)vecSource.elementAt(0);
    String pk_ct_manage = (String)(String)vecContract.elementAt(0);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, pk_ct_manage);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        Object nbusitype = rs.getObject(1);
        vecContract.addElement(nbusitype == null ? "-1" : nbusitype.toString());

        Object ninvctlstyle = (Integer)rs.getObject(2);
        vecContract.addElement(ninvctlstyle == null ? "-1" : ninvctlstyle.toString());

        Object ndatactlstyle = (Integer)rs.getObject(3);
        vecContract.addElement(ndatactlstyle == null ? "-1" : ndatactlstyle.toString());
      }
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
      catch (Exception e) {
      }
    }
    return vecSource;
  }

  public String[] getCustAddress(String sCustManID, String sPKCorp)
    throws SQLException
  {
    StringBuffer sbfSQL = new StringBuffer();
    sbfSQL.append("SELECT bd_cubasdoc.pk_cubasdoc, bd_custaddr.addrname, bd_custaddr.pk_areacl, bd_areacl.areaclname ");

    sbfSQL.append("FROM bd_custaddr LEFT OUTER JOIN ");
    sbfSQL.append("bd_areacl ON bd_custaddr.pk_areacl = bd_areacl.pk_areacl LEFT OUTER JOIN ");

    sbfSQL.append("bd_cubasdoc ON ");
    sbfSQL.append("bd_custaddr.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc LEFT OUTER JOIN ");

    sbfSQL.append("bd_cumandoc ON bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc ");

    sbfSQL.append("WHERE bd_custaddr.defaddrflag = 'Y' AND ");
    sbfSQL.append("bd_cumandoc.pk_cumandoc = '" + sCustManID + "' ");

    Connection con = null;
    PreparedStatement stmt = null;
    String[] result = new String[4];
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL.toString());
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String pk_cubasdoc = rs.getString(1);
        result[0] = (pk_cubasdoc == null ? null : pk_cubasdoc.trim());

        String addrname = rs.getString(2);
        result[1] = (addrname == null ? null : addrname.trim());

        String pk_areacl = rs.getString(3);
        result[2] = (pk_areacl == null ? null : pk_areacl.trim());

        String areaclname = rs.getString(4);
        result[3] = (areaclname == null ? null : areaclname.trim());
      }
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
    return result;
  }

  public UFDouble[] getCustManInfoAr(String sCustManID, String sPKCorp)
    throws SQLException
  {
    if ((sCustManID == null) || (sPKCorp == null))
      return null;
    String sbfSQL = "SELECT accawmny, busawmny, ordawmny, creditmny, creditmoney  FROM bd_cumandoc WHERE pk_corp='" + sPKCorp + "' and pk_cumandoc='" + sCustManID + "' ";

    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble[] result = new UFDouble[5];
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL.toString());
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        Object value = rs.getObject(1);
        result[0] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(2);
        result[1] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(3);
        result[2] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(4);
        result[3] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(5);
        result[4] = (value == null ? null : new UFDouble(value.toString()));
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
      catch (Exception e) {
      }
    }
    return result;
  }

  public double getInvInNumByBatch(String pk_corp, String invID, String vbatchcode)
    throws SQLException
  {
    String strSql = "SELECT sum(ic_general_b.ninnum) FROM ic_general_b INNER JOIN ic_general_h ON ic_general_b.cgeneralhid=ic_general_h.cgeneralhid where ic_general_h.pk_corp= '" + pk_corp + "' and ic_general_b.cinventoryid='" + invID + "' and ic_general_b.vbatchcode='" + vbatchcode + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    double innum = 0.0D;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        UFDouble invBatchNum = (UFDouble)rs.getObject(1);
        if (invBatchNum != null)
          innum = invBatchNum.doubleValue();
      }
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
    return innum;
  }

  public SaleOrderVO[] getOrderVOOfNeedUpdateARATP(SaleOrderVO[] vos)
    throws BusinessException, nc.bs.pub.SystemException, NamingException, SQLException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "getOrderVOOfNeedUpdateARATP", new Object[] { vos });

    if (vos == null) {
      return null;
    }
    String[] ids = new String[vos.length];
    for (int i = 0; i < ids.length; i++) {
      ids[i] = ((SaleorderHVO)vos[i].getParentVO()).getCsaleid();
    }
    CircularlyAccessibleValueObject[] newbvos = queryAllBodyDataByIDs(ids);

    if ((newbvos == null) || (newbvos.length <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000004"));
    }

    Hashtable hsold = new Hashtable();
    for (int i = 0; i < vos.length; i++) {
      for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
        hsold.put(((SaleorderBVO)vos[i].getChildrenVO()[j]).getCorder_bid(), vos[i].getChildrenVO()[j]);
      }
    }

    Hashtable hs = new Hashtable();
    Vector vlist = null;
    for (int i = 0; i < newbvos.length; i++) {
      if ((((SaleorderBVO)newbvos[i]).getFrowstatus().intValue() != 6) && (((SaleorderBVO)newbvos[i]).getFrowstatus().intValue() != 2)) {
        continue;
      }
      SaleorderBVO oldbvo = (SaleorderBVO)hsold.get(((SaleorderBVO)newbvos[i]).getCorder_bid());

      if (oldbvo == null)
        continue;
      if ((oldbvo.getFrowstatus() != null) && (oldbvo.getFrowstatus().intValue() == ((SaleorderBVO)newbvos[i]).getFrowstatus().intValue()))
      {
        continue;
      }
      vlist = (Vector)hs.get(((SaleorderBVO)newbvos[i]).getCsaleid());
      if (vlist != null) {
        vlist.add(newbvos[i]);
      } else {
        vlist = new Vector();
        hs.put(((SaleorderBVO)newbvos[i]).getCsaleid(), vlist);
        vlist.add(newbvos[i]);
      }

    }

    Vector vreobj = new Vector();
    for (int i = 0; i < vos.length; i++) {
      vlist = (Vector)hs.get(((SaleorderHVO)vos[i].getParentVO()).getCsaleid());

      if (vlist == null)
        continue;
      vos[i].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])vlist.toArray(new SaleorderBVO[0]));

      vreobj.add(vos[i]);
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "getOrderVOOfNeedUpdateARATP", new Object[] { vos });

    if (vreobj.size() <= 0) {
      return null;
    }
    return (SaleOrderVO[])(SaleOrderVO[])vreobj.toArray(new SaleOrderVO[0]);
  }

  public String[][] getSoInfoByDate(String pk_corp, String curDate)
    throws SQLException
  {
    String strSql = "SELECT so_sale.csaleid,so_saleorder_b.corder_bid,so_saleorder_b.cbomorderid FROM so_sale INNER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid WHERE so_sale.pk_corp='" + pk_corp + "' and so_sale.dbilldate >= '" + curDate + "' and so_saleorder_b.cbomorderid IS NOT NULL";

    Connection con = null;
    PreparedStatement stmt = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Vector vLine = new Vector();
        String obj = rs.getString(1);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(2);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(3);
        vLine.addElement(obj == null ? "" : obj.trim());
        v.addElement(vLine);
      }
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
    String[][] results = (String[][])null;
    if (v.size() > 0) {
      results = new String[v.size()][((Vector)v.elementAt(0)).size()];
      for (int i = 0; i < v.size(); i++) {
        for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
          results[i][j] = ((Vector)v.elementAt(i)).elementAt(j).toString();
        }
      }
    }
    return results;
  }

  private void getValueFromResultSet(ResultSet rs, int pos, SOField field, CircularlyAccessibleValueObject vo)
    throws SQLException
  {
    if ((rs == null) || (field == null) || (vo == null))
      return;
    Object value = null;
    if (pos < 0)
      switch (field.getUftype()) {
      case 0:
        value = rs.getObject(field.getDatabasename());
        break;
      case 1:
        String stemp = rs.getString(field.getDatabasename());
        value = stemp == null ? null : stemp.trim();
        break;
      case 2:
          stemp = rs.getString(field.getDatabasename());
        value = stemp == null ? null : new UFDate(stemp);
        break;
      case 5:
      case 12:
          stemp = rs.getString(field.getDatabasename());
        value = stemp == null ? null : new UFBoolean(stemp);
        break;
      case 4:
        BigDecimal bgtemp = (BigDecimal)rs.getObject(field.getDatabasename());

        value = bgtemp == null ? null : new UFDouble(bgtemp);
        break;
      case 6:
          stemp = rs.getString(field.getDatabasename());
        value = stemp == null ? null : new UFTime(stemp);
        break;
      case 3:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      default:
        value = rs.getObject(field.getDatabasename());

        break;
      }
    else switch (field.getUftype()) {
      case 0:
        value = rs.getObject(pos);
        break;
      case 1:
        String stemp = rs.getString(pos);
        value = stemp == null ? null : stemp.trim();
        break;
      case 2:
          stemp = rs.getString(pos);
        value = stemp == null ? null : new UFDate(stemp);
        break;
      case 5:
      case 12:
          stemp = rs.getString(pos);
        value = stemp == null ? null : new UFBoolean(stemp);
        break;
      case 4:
        BigDecimal bgtemp = (BigDecimal)rs.getObject(pos);
        value = bgtemp == null ? null : new UFDouble(bgtemp);
        break;
      case 6:
          stemp = rs.getString(pos);
        value = stemp == null ? null : new UFTime(stemp);
        break;
      case 3:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      default:
        value = rs.getObject(pos);
      }


    vo.setAttributeValue(field.getVoname(), value);
  }

  private void initFreeItem(CircularlyAccessibleValueObject[] vos)
    throws SQLException
  {
    if ((vos == null) || (vos.length <= 0)) {
      return;
    }
    ArrayList invs = null;

    for (int i = 0; i < vos.length; i++)
    {
      String inv = (String)vos[i].getAttributeValue("cinventoryid");
      if (invs == null)
        invs = new ArrayList();
      invs.add(inv);
    }
    try
    {
      if (invs != null) {
        DefdefDMO dmo = new DefdefDMO();

        ArrayList freeitem = dmo.queryFreeVOByInvIDsOnceAll(invs);

        for (int i = 0; i < vos.length; i++) {
          FreeVO freeVO = (FreeVO)freeitem.get(i);

          freeVO.setAttributeValue("vfree1", (String)vos[i].getAttributeValue("vfree1"));

          freeVO.setAttributeValue("vfree2", (String)vos[i].getAttributeValue("vfree2"));

          freeVO.setAttributeValue("vfree3", (String)vos[i].getAttributeValue("vfree3"));

          freeVO.setAttributeValue("vfree4", (String)vos[i].getAttributeValue("vfree4"));

          freeVO.setAttributeValue("vfree5", (String)vos[i].getAttributeValue("vfree5"));

          vos[i].setAttributeValue("vfree0", freeVO.getWholeFreeItem());
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new SQLException(e.getMessage());
    }
  }

  public ArrayList insert(SaleOrderVO saleorder)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
    srv.checkDefDataType(saleorder);

    CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
    String vreceiptcode = null;
    SaleorderHVO hvo = (SaleorderHVO)saleorder.getParentVO();
    try
    {
      vreceiptcode = dmocode.getSysBillNO(saleorder);
      hvo.setVreceiptcode(vreceiptcode);

      onCheck(saleorder);
    }
    catch (Exception e) {
      try {
        vreceiptcode = dmocode.getSysBillNO(saleorder);
        hvo.setVreceiptcode(vreceiptcode);

        onCheck(saleorder);
      } catch (Exception ex1) {
        if ((ex1 instanceof BusinessException)) {
          throw ((BusinessException)ex1);
        }
        throw new RemoteException("Remote Call", ex1);
      }

    }

    try
    {
      ATPVO[] atpvos = getUpdateOtherCorpAtpVO(saleorder);

      if (atpvos != null)
      {
        IICPub_InvATPDMO invatpdmo = (IICPub_InvATPDMO)NCLocator.getInstance().lookup(IICPub_InvATPDMO.class.getName());

        invatpdmo.modifyATP(atpvos);
      }

    }
    catch (Exception e)
    {
      if (e.getClass() == BusinessException.class) {
        throw ((BusinessException)e);
      }
      throw new RemoteException("Remote Call", e);
    }

    SaleorderHVO headVO = (SaleorderHVO)saleorder.getParentVO();
    SaleorderBVO[] bodyVO = (SaleorderBVO[])(SaleorderBVO[])saleorder.getChildrenVO();
    String pk_corp = headVO.getPk_corp();
    String creceipttype = headVO.getCreceipttype();

    ArrayList listBillID = null;
    CrossDBConnection con = null;

    String newHeadID = null;

    if ((saleorder.getErrMsg() != null) && (saleorder.getErrMsg().indexOf(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000005")) >= 0))
    {
      headVO.setBoverdate(new UFBoolean(true));
    }
    try {
      con = (CrossDBConnection)getConnection();
      con.setAddTimeStamp(false);

      ArrayList retlistID = fillPkAndTs(saleorder);
      ArrayList listID = new ArrayList();
      newHeadID = insertHead(headVO, pk_corp, con);

      listBillID = insertBodys(bodyVO, pk_corp, newHeadID, creceipttype, listID, con);

      listBillID = retlistID;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (con != null) {
          con.setAddTimeStamp(true);
          con.enableSQLTranslator(true);
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    checkStoreStructure(saleorder, newHeadID);

    checkRecStoreStructure(saleorder, newHeadID);

    setRetNum(saleorder);

    updateBomID(saleorder);

    return listBillID;
  }

  public String insertBody(SaleorderBVO saleItem, String strMainID, String creceipttype, PreparedStatement stmt, PreparedStatement stmtexe)
    throws SQLException, nc.bs.pub.SystemException
  {
    String key = null;
    try
    {
      key = saleItem.getCorder_bid();

      saleItem.setCsaleid(strMainID);

      stmt.setString(1, key);

      stmt.setString(2, strMainID);

      if (saleItem.getPkcorp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getPkcorp());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(11, 4);
      else {
        stmt.setBigDecimal(11, saleItem.getNpacknumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getDconsigndate() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getDconsigndate().toString());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleItem.getDdeliverdate().toString());
      }
      if (saleItem.getBlargessflag() == null)
        stmt.setString(15, "N");
      else {
        stmt.setString(15, saleItem.getBlargessflag().toString());
      }
      if (saleItem.getCeditsaleid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleItem.getCeditsaleid());
      }
      if (saleItem.getBeditflag() == null)
        stmt.setString(17, "N");
      else {
        stmt.setString(17, saleItem.getBeditflag().toString());
      }
      if (saleItem.getVeditreason() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getVeditreason());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(30, 4);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(31, 4);
      else {
        stmt.setBigDecimal(31, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setBigDecimal(33, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setBigDecimal(35, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(36, 4);
      else {
        stmt.setBigDecimal(36, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(37, 4);
      else {
        stmt.setBigDecimal(37, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(38, 4);
      else {
        stmt.setBigDecimal(38, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(39, 4);
      else {
        stmt.setBigDecimal(39, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(40, 4);
      else {
        stmt.setBigDecimal(40, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getCoperatorid() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleItem.getCoperatorid());
      }
      if (saleItem.getFrowstatus() == null)
        stmt.setNull(42, 4);
      else {
        stmt.setInt(42, saleItem.getFrowstatus().intValue());
      }
      if (saleItem.getFrownote() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleItem.getFrownote());
      }

      if (saleItem.getCinvbasdocid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleItem.getCinvbasdocid());
      }

      if (saleItem.getCbatchid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleItem.getCbatchid());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(46, 4);
      else {
        stmt.setInt(46, saleItem.getFbatchstatus().intValue());
      }

      if (saleItem.getCbomorderid() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleItem.getCbomorderid());
      }

      if (saleItem.getCt_manageid() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getCt_manageid());
      }

      if (saleItem.getCfreezeid() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getCfreezeid());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getBoosflag() == null)
        stmt.setString(51, "N");
      else {
        stmt.setString(51, saleItem.getBoosflag().toString());
      }

      if (saleItem.getBsupplyflag() == null)
        stmt.setString(52, "N");
      else {
        stmt.setString(52, saleItem.getBsupplyflag().toString());
      }

      if (saleItem.getCreceiptareaid() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getCreceiptareaid());
      }

      if (saleItem.getVreceiveaddress() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleItem.getVreceiveaddress());
      }

      if (saleItem.getCreceiptcorpid() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleItem.getCreceiptcorpid());
      }

      if (saleItem.getCrowno() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleItem.getCrowno());
      }

      if (saleItem.getTs() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, saleItem.getTs().toString());
      }

      setPreStatementOrdB(stmt, 58, saleItem);

      executeUpdate(stmt);
    }
    finally
    {
    }

    saleItem.setPrimaryKey(key);
    saleItem.setCsaleid(strMainID);
    insertFollowBody(saleItem, creceipttype, stmtexe);
    return key;
  }

  public ArrayList insertBodys(SaleorderBVO[] saleorderB, String pk_corp, String strMainID, String creceipttype, ArrayList listID, CrossDBConnection con)
    throws SQLException, nc.bs.pub.SystemException
  {
    SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    StringBuffer addfieldvaluesql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldvaluesql.append(",?");
        }
      }

    }

    String sql = "insert into so_saleorder_b(corder_bid, csaleid, pk_corp, creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, veditreason, ccurrencytypeid, nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, frowstatus, frownote, cinvbasdocid, cbatchid, fbatchstatus, cbomorderid, ct_manageid, cfreezeid, cadvisecalbodyid, boosflag, bsupplyflag, creceiptareaid, vreceiveaddress, creceiptcorpid, crowno,ts" + addfieldsql.toString() + ") " + "values(" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?" + addfieldvaluesql.toString() + ")";

    PreparedStatement stmt = null;

    addfields = SaleorderBVO.getSoSaleExecuteAddFields();
    addfieldsql.setLength(0);
    addfieldsql.append("");
    addfieldvaluesql.setLength(0);
    addfieldvaluesql.append("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldvaluesql.append(",?");
        }
      }
    }

    String sqlexe = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1, vdef6, vdef5, vdef4, vdef3, vdef2, vdef1,ts" + addfieldsql.toString() + ")" + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + addfieldvaluesql.toString() + ")";

    PreparedStatement stmtexe = null;
    try
    {
      stmt = prepareStatement(con, sql);
      stmtexe = prepareStatement(con, sqlexe);

      for (int i = 0; i < saleorderB.length; i++)
      {
        if ((saleorderB[i].getBoosflag() != null) && (saleorderB[i].getBoosflag().booleanValue()))
          continue;
        saleorderB[i].setPkcorp(pk_corp);

        String strItemID = insertBody(saleorderB[i], strMainID, creceipttype, stmt, stmtexe);

        if (listID != null) {
          listID.add(strItemID);
        }
        String bomID = saleorderB[i].getCbomorderid();
        if ((bomID != null) && (bomID.trim().length() != 0)) {
          updateBom(bomID, strMainID, strItemID);

          updateBomVO(saleorderB[i]);
        }

      }

      executeBatch(stmt);
      executeBatch(stmtexe);
    }
    finally
    {
    }
    return listID;
  }

  public void insertFollowBody(SaleorderBVO saleexecute, String creceipttype, PreparedStatement stmt)
    throws SQLException, nc.bs.pub.SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "insertFollowBody", new Object[] { saleexecute });
    try
    {
      if (saleexecute.getPrimaryKey() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleexecute.getPrimaryKey());
      }

      if (saleexecute.getCsaleid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleexecute.getCsaleid());
      }
      stmt.setString(3, creceipttype);
      if (saleexecute.getNtotalpaymny() == null)
        stmt.setNull(4, 4);
      else {
        stmt.setBigDecimal(4, saleexecute.getNtotalpaymny().toBigDecimal());
      }

      if (saleexecute.getNtotalreceiptnumber() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setBigDecimal(5, saleexecute.getNtotalreceiptnumber().toBigDecimal());
      }

      if (saleexecute.getNtotalinvoicenumber() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, saleexecute.getNtotalinvoicenumber().toBigDecimal());
      }

      if (saleexecute.getNtotalinventorynumber() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleexecute.getNtotalinventorynumber().toBigDecimal());
      }

      if (saleexecute.getBifinvoicefinish() == null)
      {
        stmt.setString(8, "N");
      }
      else stmt.setString(8, saleexecute.getBifinvoicefinish().toString());

      if (saleexecute.getBifreceiptfinish() == null)
      {
        stmt.setString(9, "N");
      }
      else stmt.setString(9, saleexecute.getBifreceiptfinish().toString());

      if (saleexecute.getBifinventoryfinish() == null)
      {
        stmt.setString(10, "N");
      }
      else stmt.setString(10, saleexecute.getBifinventoryfinish().toString());

      if (saleexecute.getBifpayfinish() == null)
      {
        stmt.setString(11, "N");
      }
      else stmt.setString(11, saleexecute.getBifpayfinish().toString());

      if (saleexecute.getNassistcurdiscountmny() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, saleexecute.getNassistcurdiscountmny().toBigDecimal());
      }

      if (saleexecute.getNassistcursummny() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, saleexecute.getNassistcursummny().toBigDecimal());
      }

      if (saleexecute.getNassistcurmny() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setBigDecimal(14, saleexecute.getNassistcurmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxmny() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setBigDecimal(15, saleexecute.getNassistcurtaxmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxnetprice() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setBigDecimal(16, saleexecute.getNassistcurtaxnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurnetprice() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, saleexecute.getNassistcurnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxprice() == null)
        stmt.setNull(18, 4);
      else {
        stmt.setBigDecimal(18, saleexecute.getNassistcurtaxprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurprice() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, saleexecute.getNassistcurprice().toBigDecimal());
      }

      if (saleexecute.getCprojectid3() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, saleexecute.getCprojectid3());
      }
      if (saleexecute.getCprojectphaseid() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, saleexecute.getCprojectphaseid());
      }
      if (saleexecute.getCprojectid() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleexecute.getCprojectid());
      }
      if (saleexecute.getVfree5() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleexecute.getVfree5());
      }
      if (saleexecute.getVfree4() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, saleexecute.getVfree4());
      }
      if (saleexecute.getVfree3() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, saleexecute.getVfree3());
      }
      if (saleexecute.getVfree2() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, saleexecute.getVfree2());
      }
      if (saleexecute.getVfree1() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, saleexecute.getVfree1());
      }
      if (saleexecute.getVdef6() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, saleexecute.getVdef6());
      }
      if (saleexecute.getVdef5() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, saleexecute.getVdef5());
      }
      if (saleexecute.getVdef4() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, saleexecute.getVdef4());
      }
      if (saleexecute.getVdef3() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, saleexecute.getVdef3());
      }
      if (saleexecute.getVdef2() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, saleexecute.getVdef2());
      }
      if (saleexecute.getVdef1() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, saleexecute.getVdef1());
      }
      if (saleexecute.getTs() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, saleexecute.getTs().toString());
      }

      setPreStatementOrdE(stmt, 35, saleexecute);

      executeUpdate(stmt);
    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "insertFollowBody", new Object[] { saleexecute });
  }

  public String insertHead(SaleorderHVO saleHeader, String pk_corp, Connection con)
    throws Exception
  {
    SOField[] addfields = SaleorderHVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    StringBuffer addfieldvaluesql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldvaluesql.append(",?");
        }
      }
    }

    String sql = "insert into so_sale(csaleid, pk_corp, vreceiptcode, creceipttype, cbiztype, finvoiceclass, finvoicetype, vaccountyear, binitflag, dbilldate, ccustomerid, cdeptid, cemployeeid, coperatorid, ctermprotocolid, csalecorpid, creceiptcustomerid, vreceiveaddress, creceiptcorpid, ctransmodeid, ndiscountrate, cwarehouseid, veditreason, bfreecustflag, cfreecustid, ibalanceflag, nsubscription, ccreditnum, nevaluatecarriage, dmakedate, capproveid, dapprovedate, fstatus, vnote, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ccalbodyid, bretinvflag,boutendflag,binvoicendflag,breceiptendflag,npreceiverate,npreceivemny,ts,nheadsummny " + addfieldsql.toString() + ")" + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + addfieldvaluesql.toString() + ")";

    String key = null;
    PreparedStatement stmt = null;
    try {
      stmt = con.prepareStatement(sql);

      if ((saleHeader.getCsaleid() == null) || (saleHeader.getCsaleid().trim().length() <= 0))
      {
        key = getOID(pk_corp);
      }
      else key = saleHeader.getCsaleid();

      saleHeader.setCsaleid(key);

      stmt.setString(1, key);

      if (pk_corp == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, pk_corp);
      }
      if (saleHeader.getVreceiptcode() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleHeader.getVreceiptcode());
      }
      if (saleHeader.getCreceipttype() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleHeader.getCreceipttype());
      }
      if (saleHeader.getCbiztype() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleHeader.getCbiztype());
      }
      if (saleHeader.getFinvoiceclass() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setInt(6, saleHeader.getFinvoiceclass().intValue());
      }
      if (saleHeader.getFinvoicetype() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setInt(7, saleHeader.getFinvoicetype().intValue());
      }
      if (saleHeader.getVaccountyear() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleHeader.getVaccountyear());
      }
      if (saleHeader.getBinitflag() == null)
        stmt.setString(9, "N");
      else {
        stmt.setString(9, saleHeader.getBinitflag().toString());
      }
      if (saleHeader.getDbilldate() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleHeader.getDbilldate().toString());
      }
      if (saleHeader.getCcustomerid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleHeader.getCcustomerid());
      }
      if (saleHeader.getCdeptid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleHeader.getCdeptid());
      }
      if (saleHeader.getCemployeeid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleHeader.getCemployeeid());
      }
      if (saleHeader.getCoperatorid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleHeader.getCoperatorid());
      }
      if (saleHeader.getCtermprotocolid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleHeader.getCtermprotocolid());
      }
      if (saleHeader.getCsalecorpid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleHeader.getCsalecorpid());
      }
      if (saleHeader.getCreceiptcustomerid() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleHeader.getCreceiptcustomerid());
      }
      if (saleHeader.getVreceiveaddress() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleHeader.getVreceiveaddress());
      }
      if (saleHeader.getCreceiptcorpid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleHeader.getCreceiptcorpid());
      }
      if (saleHeader.getCtransmodeid() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, saleHeader.getCtransmodeid());
      }
      if (saleHeader.getNdiscountrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleHeader.getNdiscountrate().toBigDecimal());
      }

      if (saleHeader.getCwarehouseid() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleHeader.getCwarehouseid());
      }
      if (saleHeader.getVeditreason() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleHeader.getVeditreason());
      }
      if (saleHeader.getBfreecustflag() == null)
        stmt.setString(24, "N");
      else {
        stmt.setString(24, saleHeader.getBfreecustflag().toString());
      }
      if (saleHeader.getCfreecustid() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, saleHeader.getCfreecustid());
      }
      if (saleHeader.getIbalanceflag() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setInt(26, saleHeader.getIbalanceflag().intValue());
      }
      if (saleHeader.getNsubscription() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleHeader.getNsubscription().toBigDecimal());
      }

      if (saleHeader.getCcreditnum() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, saleHeader.getCcreditnum());
      }
      if (saleHeader.getNevaluatecarriage() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleHeader.getNevaluatecarriage().toBigDecimal());
      }

      if (saleHeader.getDmakedate() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, saleHeader.getDmakedate().toString());
      }
      if (saleHeader.getCapproveid() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, saleHeader.getCapproveid());
      }
      if (saleHeader.getDapprovedate() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, saleHeader.getDapprovedate().toString());
      }
      if (saleHeader.getFstatus() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setInt(33, saleHeader.getFstatus().intValue());
      }
      if (saleHeader.getVnote() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, saleHeader.getVnote());
      }
      if (saleHeader.getVdef1() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, saleHeader.getVdef1());
      }
      if (saleHeader.getVdef2() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, saleHeader.getVdef2());
      }
      if (saleHeader.getVdef3() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, saleHeader.getVdef3());
      }
      if (saleHeader.getVdef4() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, saleHeader.getVdef4());
      }
      if (saleHeader.getVdef5() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, saleHeader.getVdef5());
      }
      if (saleHeader.getVdef6() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleHeader.getVdef6());
      }
      if (saleHeader.getVdef7() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleHeader.getVdef7());
      }
      if (saleHeader.getVdef8() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleHeader.getVdef8());
      }
      if (saleHeader.getVdef9() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleHeader.getVdef9());
      }
      if (saleHeader.getVdef10() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleHeader.getVdef10());
      }

      if (saleHeader.getCcalbodyid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleHeader.getCcalbodyid());
      }
      if (saleHeader.getBretinvflag() == null)
        stmt.setString(46, "N");
      else {
        stmt.setString(46, saleHeader.getBretinvflag().toString());
      }
      if (saleHeader.getBoutendflag() == null)
        stmt.setString(47, "N");
      else {
        stmt.setString(47, saleHeader.getBoutendflag().toString());
      }
      if (saleHeader.getBinvoicendflag() == null)
        stmt.setString(48, "N");
      else {
        stmt.setString(48, saleHeader.getBinvoicendflag().toString());
      }
      if (saleHeader.getBreceiptendflag() == null)
        stmt.setString(49, "N");
      else {
        stmt.setString(49, saleHeader.getBreceiptendflag().toString());
      }

      if (saleHeader.getNpreceiverate() == null)
        stmt.setNull(50, 4);
      else {
        stmt.setBigDecimal(50, saleHeader.getNpreceiverate().toBigDecimal());
      }

      if (saleHeader.getNpreceivemny() == null)
        stmt.setNull(51, 4);
      else {
        stmt.setBigDecimal(51, saleHeader.getNpreceivemny().toBigDecimal());
      }

      if (saleHeader.getTs() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleHeader.getTs().toString());
      }
      if (saleHeader.getAttributeValue("nheadsummny") == null)
        stmt.setNull(53, 4);
      else {
        stmt.setBigDecimal(53, ((UFDouble)saleHeader.getAttributeValue("nheadsummny")).toBigDecimal());
      }

      setPreStatementOrdH(stmt, 54, saleHeader);

      stmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
    }
    saleHeader.setCsaleid(key);
    return key;
  }

  public boolean isBomApproved(String orderID)
    throws SQLException
  {
    String sql = "SELECT fstatus FROM so_bomorder WHERE csaleid ='" + orderID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o = null;
    boolean isApproved = true;
    int status = -1;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        o = rs.getObject(1);
        status = o == null ? 0 : ((Integer)o).intValue();
        if (status != 2)
          isApproved = false;
      }
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
    return isApproved;
  }

  public boolean isCtExist(String custID, String invID)
    throws SQLException
  {
    String sql = "SELECT ct_manage.ct_code FROM ct_manage INNER JOIN ct_manage_b ON ct_manage.pk_ct_manage = ct_manage_b.pk_ct_manage WHERE ct_manage.custid='" + custID + "' and ct_manage_b.invid='" + invID + "' and ct_manage.ctflag=2";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        o = rs.getObject(1);
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
    return o != null;
  }

  public boolean isFinished(SaleorderHVO saleorderHVO)
    throws BusinessException
  {
    SaleorderHVO[] saleorderHVOs = (SaleorderHVO[])(SaleorderHVO[])queryAllHeadData(" so_sale.csaleid ='" + saleorderHVO.getPrimaryKey() + "'");

    if ((saleorderHVOs != null) && (saleorderHVOs.length != 0))
      return saleorderHVOs[0].getFstatus().intValue() == 6;
    return true;
  }

  private boolean isHaveSub(String pk_corp, String invID, String curdate)
    throws SQLException
  {
    String strSql = "SELECT  mm_pzbom.wlbmid FROM mm_pzbom WHERE mm_pzbom.pk_corp='" + pk_corp + "' and mm_pzbom.wlbmid = '" + invID + "' ";

    Connection con = null;
    PreparedStatement stmt = null;

    boolean result = false;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = true;
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
      catch (Exception e) {
      }
    }
    return result;
  }

  public boolean isInvBatched(String invID)
    throws SQLException
  {
    String sql = "SELECT wholemanaflag FROM bd_invmandoc WHERE pk_invmandoc='" + invID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o = null;
    String isBom = "N";
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        o = rs.getObject(1);
        isBom = o == null ? "N" : o.toString();
      }
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
    return !isBom.equals("N");
  }

  public boolean isInvBom(String invID)
    throws SQLException
  {
    String sql = "SELECT isconfigable FROM bd_invmandoc WHERE pk_invmandoc='" + invID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o = null;
    String isBom = "N";
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        o = rs.getObject(1);
        isBom = o == null ? "N" : o.toString();
      }
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
    return !isBom.equals("N");
  }

  public boolean isInvExist(String pk_corp, String invID)
    throws SQLException
  {
    String sql = "SELECT wlbmid FROM mm_pzbom WHERE pk_corp='" + pk_corp + "' and wlbmid = '" + invID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        o = rs.getObject(1);
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
    return o != null;
  }

  public boolean isInvLocked(String sale_bid)
    throws SQLException
  {
    String sql = "SELECT ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber FROM so_saleexecute WHERE csale_bid = '" + sale_bid + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    Object o1 = null;
    Object o2 = null;
    Object o3 = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        o1 = rs.getObject(1);
        o2 = rs.getObject(2);
        o3 = rs.getObject(3);
      }
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
    if ((o1 != null) && (o1.toString().length() != 0))
      return true;
    if ((o2 != null) && (o2.toString().length() != 0)) {
      return true;
    }
    return (o3 != null) && (o3.toString().length() != 0);
  }

  public void isModifyNOriginalCurnetPrice(SaleOrderVO saleorder)
    throws BusinessException
  {
    if ((saleorder.getModPriceTag() == null) || (saleorder.getModPriceTag().booleanValue()))
    {
      return;
    }String[] paracodes = { "SA15", "SA07", "SA02", "SO17" };

    UFBoolean SA_02 = null;
    UFBoolean SA_07 = null;
    UFBoolean SA_15 = null;
    UFBoolean SO_17 = null;
    ArrayList modrowlist = null;
    try
    {
      SysInitBO sysbo = new SysInitBO();
      Hashtable h = sysbo.queryBatchParaValues(((SaleorderHVO)saleorder.getParentVO()).getPk_corp(), paracodes);

      String str = (String)h.get(paracodes[0]);
      if (str == null)
        SA_15 = new UFBoolean(false);
      else {
        SA_15 = new UFBoolean(str);
      }

      str = (String)h.get(paracodes[1]);
      if (str == null)
        SA_07 = new UFBoolean(false);
      else {
        SA_07 = new UFBoolean(str);
      }
      str = (String)h.get(paracodes[2]);
      if (str == null)
        SA_02 = new UFBoolean(false);
      else {
        SA_02 = new UFBoolean(str);
      }
      str = (String)h.get(paracodes[3]);
      if (str == null)
        SO_17 = new UFBoolean(false);
      else {
        SO_17 = new UFBoolean(str);
      }

      if (!SA_15.booleanValue()) {
        return;
      }
      if (!SA_07.booleanValue()) {
        return;
      }
      UFDouble discount = null;

      FetchValueDMO fvdmo = new FetchValueDMO();
      String sdis = fvdmo.getColValue("bd_cumandoc", "discountrate", " pk_cumandoc='" + ((SaleorderHVO)saleorder.getParentVO()).getCcustomerid() + "' ");

      if ((sdis == null) || (sdis.trim().length() <= 0))
        discount = new UFDouble(100);
      else
        discount = new UFDouble(sdis);
      discount = discount.div(100.0D);
      int i = 0; for (int count = saleorder.getChildrenVO().length; i < count; i++) {
        SaleorderBVO border = (SaleorderBVO)saleorder.getChildrenVO()[i];

        if ((border.getNnumber() == null) || (border.getNnumber().doubleValue() <= 0.0D))
        {
          continue;
        }

        if (border.getBlargessflag().booleanValue())
        {
          continue;
        }
        if ((border.getCt_manageid() != null) && (border.getCt_manageid().trim().length() > 0) && 
          (SO_17.booleanValue()))
        {
          continue;
        }

        UFDouble price0 = null; UFDouble price1 = null;

        if (SA_02.booleanValue()) {
          price0 = border.getNoriginalcurtaxprice();
          price1 = border.getNoriginalcurtaxnetprice();
        } else {
          price0 = border.getNoriginalcurprice();
          price1 = border.getNoriginalcurnetprice();
        }

        UFDouble dtmp = price0.multiply(discount).sub(price1).abs();
        if (dtmp.doubleValue() > 9.999999999999999E-012D) {
          if (modrowlist == null)
            modrowlist = new ArrayList();
          modrowlist.add("" + i);
        }
      }
    }
    catch (Exception e) {
      throw new ModifyPriceException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000006"));
    }

    if ((modrowlist != null) && (modrowlist.size() > 0)) {
      StringBuffer stemp = new StringBuffer();
      int i = 0; for (int count = modrowlist.size(); i < count; i++) {
        if (i != 0)
          stemp.append(",");
        stemp.append(modrowlist.get(i).toString());
      }
      throw new ModifyPriceException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000007", null, new String[] { stemp.toString() }));
    }
  }

  public SaleOrderVO[] mergeToSaleOrderVO(SaleorderBVO[] vobodys)
    throws NamingException, nc.bs.pub.SystemException, BusinessException, SQLException
  {
    if ((vobodys == null) || (vobodys.length <= 0))
      return null;
    Hashtable hs = new Hashtable();
    Vector vlist = null;
    for (int i = 0; i < vobodys.length; i++) {
      vlist = (Vector)hs.get(vobodys[i].getCsaleid());
      if (vlist != null) {
        vlist.add(vobodys[i]);
      } else {
        vlist = new Vector();
        hs.put(vobodys[i].getCsaleid(), vlist);
        vlist.add(vobodys[i]);
      }
    }
    String[] ids = (String[])(String[])hs.keySet().toArray(new String[0]);
    CircularlyAccessibleValueObject[] hvos = queryAllHeadData(ids);

    if ((ids == null) || (ids.length <= 0)) {
      return null;
    }
    SaleOrderVO[] saleorders = new SaleOrderVO[hvos.length];
    for (int i = 0; i < hvos.length; i++) {
      saleorders[i] = new SaleOrderVO();
      saleorders[i].setParentVO(hvos[i]);
      vlist = (Vector)hs.get(((SaleorderHVO)hvos[i]).getCsaleid());
      if (vlist != null) {
        saleorders[i].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])vlist.toArray(new SaleorderBVO[0]));
      }
      else
        saleorders[i].setChildrenVO(null);
    }
    return saleorders;
  }

  public void modificationBody(SaleorderBVO saleItem, String strMainID, String creceipttype, PreparedStatement stmt, PreparedStatement stmtexe, PreparedStatement stmtupdate, PreparedStatement stmtfollow)
    throws SQLException, nc.bs.pub.SystemException
  {
    SaleorderBVO oldsaleItem = queryBodyData(saleItem.getPrimaryKey());

    oldsaleItem.setCeditsaleid(oldsaleItem.getPrimaryKey());

    oldsaleItem.setBeditflag(new UFBoolean(true));

    oldsaleItem.setVeditreason(null);
    String key = getOID(oldsaleItem.getPkcorp());
    oldsaleItem.setCorder_bid(key);

    insertBody(oldsaleItem, strMainID, creceipttype, stmt, stmtexe);

    updateBody(saleItem, stmtupdate, stmtfollow);
  }

  private void onCheck(SaleOrderVO saleorder)
    throws BusinessException, NamingException, SQLException, nc.bs.pub.SystemException
  {
    CheckValueValidityImpl checkDmo = new CheckValueValidityImpl();
    checkDmo.isValueValidity(saleorder);
    try
    {
      saleorder.validate();

      checkBuyLargess(saleorder);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    try
    {
      SaleorderBVO[] bvos = saleorder.getBodyVOs();
      if ((bvos != null) && (bvos.length > 0) && (
        (bvos[0].getCrowno() == null) || (bvos[0].getCrowno().trim().length() == 0)))
      {
        BillRowNoDMO.setVORowNoByRule(saleorder, "30", "crowno");
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }
    if ((saleorder.getHeadVO() != null) && (saleorder.getHeadVO().getCsaleid() != null) && (saleorder.getHeadVO().getFstatus() != null) && (saleorder.getHeadVO().getFstatus().intValue() == 2))
    {
      SaleorderBVO[] servos = queryExecDate(saleorder.getHeadVO().getCsaleid());

      if ((servos != null) && (servos.length > 0))
      {
        String[] snames = null;

        snames = new String[] { "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalshouldoutnum", "ntotalinvoicemny", "ntotalinventorynumber", "ntotalbalancenumber", "ntotalsignnumber", "ntotalcostmny" };

        Hashtable ht = new Hashtable();
        int i = 0; for (int iLen = servos.length; i < iLen; i++) {
          for (int k = 0; k < snames.length; k++) {
            if ((servos[i].getAttributeValue(snames[k]) == null) || (new UFDouble(servos[i].getAttributeValue(snames[k]).toString()).doubleValue() == 0.0D)) {
              continue;
            }
            ht.put(servos[i].getCorder_bid(), servos[i]);
            break;
          }
        }

        if (ht.size() > 0) {
          SaleorderBVO bvo = null;
          for (  i = 0; i < saleorder.getBodyVOs().length; i++) {
            if ((saleorder.getBodyVOs()[i].getCorder_bid() == null) || 
              (!ht.containsKey(saleorder.getBodyVOs()[i].getCorder_bid())))
              continue;
            bvo = (SaleorderBVO)ht.get(saleorder.getBodyVOs()[i].getCorder_bid());

            if ((bvo == null) || (bvo.getCinventoryid().equals(saleorder.getBodyVOs()[i].getCinventoryid())))
            {
              continue;
            }
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000506", null, new String[] { String.valueOf(i + 1) }));
          }
        }
      }
    }
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    if ((key == null) || (key.trim().length() <= 0)) {
      return null;
    }
    String swhere = " so_saleorder_b.csaleid='" + key + "' ";
    try {
      SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
      if ((saleItems == null) || (saleItems.length <= 0)) {
        return null;
      }
      initFreeItem(saleItems);

      afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });

      return saleItems;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String where)
    throws BusinessException
  {
    String swhere = new String();

    if ((key == null) || (key.trim().length() <= 0)) {
      swhere = " so_saleorder_b.csaleid in (select so_sale.csaleid from so_sale where " + where + ") ";
    }
    else
    {
      swhere = " so_saleorder_b.csaleid='" + key + "' " + (where == null ? "" : where);
    }

    try
    {
      SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
      if ((saleItems == null) || (saleItems.length <= 0)) {
        return null;
      }
      initFreeItem(saleItems);

      afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });

      return saleItems;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByIDs(String[] hkeys)
    throws SQLException
  {
    if ((hkeys == null) || (hkeys.length <= 0))
      return null;
    String sWhere = GeneralSqlString.formInSQL("so_saleorder_b.csaleid", hkeys);

    return queryAllBodyDataByWhere(sWhere, null);
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String[] ids)
    throws BusinessException
  {
    if ((ids == null) || (ids.length <= 0)) {
      return null;
    }
    StringBuffer wheresql = null;
    ArrayList relist = new ArrayList();
    int hvocount = 0;
    int i = 0; int count = ids.length % 200 == 0 ? ids.length / 200 : ids.length / 200 + 1;
    for (; i < count; i++) {
      wheresql = new StringBuffer();
      int j = 0; int count1 = ids.length - i * 200 > 200 ? 200 : ids.length - i * 200;
      for (; j < count1; j++) {
        if (j > 0)
          wheresql.append(" , ");
        wheresql.append("'" + ids[(i * 200 + j)] + "'");
      }
      SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])queryAllHeadData(" so_sale.csaleid in ( " + wheresql.toString() + " ) ");

      relist.add(hvos);
      hvocount += hvos.length;
    }
    SaleorderHVO[] reobjs = new SaleorderHVO[hvocount];
    int pos = 0;
      i = 0; for (  count = relist.size(); i < count; i++) {
      SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])relist.get(i);
      System.arraycopy(hvos, 0, reobjs, pos, hvos.length);
      pos += hvos.length;
    }
    return reobjs;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String where)
    throws BusinessException
  {
    SOField[] addfields = SaleorderHVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getTablename());
          addfieldsql.append(".");
          addfieldsql.append(addfields[i].getDatabasename());
        }

      }

    }

    if ((where != null) && 
      (where.indexOf("so_saleexecute") >= 0));
    String strSql = "SELECT DISTINCT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag,so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,so_sale.bpayendflag,so_saleorder_b.ccurrencytypeid, so_sale.nheadsummny" + addfieldsql.toString() + " FROM so_sale, so_saleorder_b,so_saleexecute ";

    strSql = strSql + " where so_saleexecute.creceipttype = '30' AND so_saleorder_b.beditflag = 'N' ";
    strSql = strSql + " and so_sale.csaleid = so_saleorder_b.csaleid  and so_sale.csaleid = so_saleexecute.csaleid ";
    strSql = strSql + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid  and so_saleorder_b.csaleid = so_saleexecute.csaleid ";
    if ((where != null) && (where.trim().length() > 0)) {
      strSql = strSql + " AND " + where;
    }
    strSql = strSql + " order by so_sale.csaleid ";
    SaleorderHVO[] saleorderHs = null;
    ArrayList csaleids = new ArrayList();
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    con = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        SaleorderHVO saleHeader = new SaleorderHVO();

        String pk_corp = rs.getString(1);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(4);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(5);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(6);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(7);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(8);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(9);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String ccustomerid = rs.getString(10);
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString(11);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(12);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(13);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(14);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(15);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(16);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(17);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(18);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(19);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = rs.getBigDecimal(20);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        String cwarehouseid = rs.getString(21);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(22);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(23);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(24);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(25);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        BigDecimal nsubscription = rs.getBigDecimal(26);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

        String ccreditnum = rs.getString(27);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = rs.getBigDecimal(28);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(29);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(30);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(31);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(32);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(33);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(34);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(40);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(41);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(42);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(43);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ccalbodyid = rs.getString(44);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String csaleid = rs.getString(45);
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        csaleids.add(csaleid);

        String bretinvflag = rs.getString(46);
        saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

        String boutendflag = rs.getString(47);
        saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

        String binvoicendflag = rs.getString(48);
        saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

        String breceiptendflag = rs.getString(49);
        saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));

        String ts = rs.getString(50);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        BigDecimal npreceiverate = rs.getBigDecimal(51);
        saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));

        BigDecimal npreceivemny = rs.getBigDecimal(52);
        saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

        String bpayendflag = rs.getString(53);
        saleHeader.setBpayendflag(bpayendflag == null ? null : new UFBoolean(bpayendflag.trim()));

        String ccurrencytypeid = rs.getString(54);
        saleHeader.setAttributeValue("ccurrencytypeid", ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nheadsummny = rs.getBigDecimal(55);
        saleHeader.setAttributeValue("nheadsummny", nheadsummny == null ? null : new UFDouble(nheadsummny));

        getOrdHValueFromResultSet(rs, 56, saleHeader);

        v.addElement(saleHeader);
      }

      if (v.size() > 0) {
        saleorderHs = new SaleorderHVO[v.size()];
        v.copyInto(saleorderHs);

        HashMap hscachpay = queryCachPayByOrdIds((String[])(String[])csaleids.toArray(new String[csaleids.size()]));

        if (hscachpay != null) {
          int i = 0; for (int loop = saleorderHs.length; i < loop; i++) {
            saleorderHs[i].setNreceiptcathmny((UFDouble)hscachpay.get(saleorderHs[i].getCsaleid()));
          }
        }
      }
    }
    catch (SQLException e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
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
    return saleorderHs;
  }

  public CircularlyAccessibleValueObject[] queryBodyAllData(String where)
    throws SQLException, Exception
  {
    if ((where == null) || (where.trim().length() <= 0)) {
      return null;
    }
    String swhere = " so_saleorder_b.csaleid='" + where + "' ";

    SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
    if ((saleItems == null) || (saleItems.length <= 0))
      return null;
    initFreeItem(saleItems);
    return saleItems;
  }

  public SaleorderBVO queryBodyData(String key) throws SQLException
  {
    if ((key == null) || (key.trim().length() <= 0)) {
      return null;
    }

    String swhere = " so_saleorder_b.corder_bid='" + key + "' ";

    SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
    if ((saleItems == null) || (saleItems.length <= 0)) {
      return null;
    }
    initFreeItem(saleItems);

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });

    return saleItems[0];
  }

  public SaleorderBVO[] queryExecDate(String csaleid)
    throws SQLException, NamingException, nc.bs.pub.SystemException
  {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT so_saleexecute.csaleid,so_saleexecute.csale_bid,");
    sql.append("so_saleexecute.ntotalreceiptnumber, so_saleexecute.ntotalinvoicenumber,ntotalshouldoutnum, ntotalinvoicemny, ntotalinventorynumber,");

    sql.append("ntotalbalancenumber, ntotalsignnumber, ntotalcostmny ,so_saleorder_b.cinventoryid ");

    sql.append("FROM  ");
    sql.append(" so_saleexecute,so_saleorder_b ");
    sql.append("WHERE so_saleexecute.csaleid = '" + csaleid + "' and so_saleexecute.creceipttype = '30' ").append(" and so_saleexecute.csale_bid = so_saleorder_b.corder_bid");

    SmartDMO sdmo = new SmartDMO();

    Object[] o = sdmo.selectBy2(sql.toString());
    if ((o == null) || (o.length == 0))
      return null;
    Object[] o1 = null;
    SaleorderBVO bvo = null;
    Vector vt = new Vector();
    for (int i = 0; i < o.length; i++) {
      o1 = (Object[])(Object[])o[i];
      if ((o1 == null) || (o1.length == 0))
        continue;
      bvo = new SaleorderBVO();
      bvo.setCsaleid(o1[0].toString());
      bvo.setCorder_bid(o1[1].toString());
      bvo.setAttributeValue("ntotalreceiptnumber", o1[2] == null ? new UFDouble(0) : new UFDouble(o1[2].toString()));

      bvo.setAttributeValue("ntotalinvoicenumber", o1[3] == null ? new UFDouble(0) : new UFDouble(o1[3].toString()));

      bvo.setAttributeValue("ntotalshouldoutnum", o1[4] == null ? new UFDouble(0) : new UFDouble(o1[4].toString()));

      bvo.setAttributeValue("ntotalinvoicemny", o1[5] == null ? new UFDouble(0) : new UFDouble(o1[5].toString()));

      bvo.setAttributeValue("ntotalinventorynumber", o1[6] == null ? new UFDouble(0) : new UFDouble(o1[6].toString()));

      bvo.setAttributeValue("ntotalbalancenumber", o1[7] == null ? new UFDouble(0) : new UFDouble(o1[7].toString()));

      bvo.setAttributeValue("ntotalsignnumber", o1[8] == null ? new UFDouble(0) : new UFDouble(o1[8].toString()));

      bvo.setAttributeValue("ntotalcostmny", o1[9] == null ? new UFDouble(0) : new UFDouble(o1[9].toString()));

      bvo.setAttributeValue("cinventoryid", o1[10] == null ? null : o1[10].toString());

      vt.add(bvo);
    }
    SaleorderBVO[] bvos = new SaleorderBVO[vt.size()];
    vt.copyInto(bvos);
    return bvos;
  }

  public SaleOrderVO queryData(String strID) throws SQLException, Exception
  {
    Connection con = null;
    PreparedStatement stmt = null;

    SaleOrderVO saleorder = new SaleOrderVO();
    con = getConnection();
    try {
      String strWhere = "so_sale.csaleid = '" + strID + "'";
      CircularlyAccessibleValueObject[] Headvo = queryAllHeadData(strWhere);
      if ((Headvo != null) && (Headvo.length > 0))
        saleorder.setParentVO(Headvo[0]);
      saleorder.setChildrenVO(queryAllBodyData(strID));
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
    return saleorder;
  }

  public SaleOrderVO[] queryDataByContract(String strContractID)
    throws SQLException, Exception
  {
    String[] strIDs = queryOrderIDByContractID(strContractID);

    SaleOrderVO[] saleorders = null;

    if (strIDs != null) {
      saleorders = new SaleOrderVO[strIDs.length];
      for (int i = 0; i < strIDs.length; i++) {
        saleorders[i] = queryData(strIDs[i]);
      }
    }

    return saleorders;
  }

  public AggregatedValueObject[] queryDataByWhere(String swhere)
    throws BusinessException
  {
    if (swhere == null) {
      return null;
    }

    SOField[] addfields = SaleorderBVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getTablename());
          addfieldsql.append(".");
          addfieldsql.append(addfields[i].getDatabasename());
        }
      }
    }

    String sql = "SELECT DISTINCT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag, so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, so_saleorder_b.dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, so_saleorder_b.veditreason, so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, so_saleorder_b.ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, frowstatus, frownote,cinvbasdocid,cbatchid,fbatchstatus,cbomorderid,cfreezeid,ct_manageid, so_saleorder_b.ts, cadvisecalbodyid, boosflag, bsupplyflag, so_saleorder_b.creceiptareaid, so_saleorder_b.vreceiveaddress, so_saleorder_b.creceiptcorpid, so_saleorder_b.crowno, so_saleexecute.creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, so_saleexecute.vdef1, so_saleexecute.vdef2, so_saleexecute.vdef3, so_saleexecute.vdef4, so_saleexecute.vdef5, so_saleexecute.vdef6, so_sale.iprintcount,so_sale.bdeliver, so_sale.vdef11,so_sale.vdef12,so_sale.vdef13,so_sale.vdef14,so_sale.vdef15,so_sale.vdef16, so_sale.vdef17,so_sale.vdef18,so_sale.vdef19,so_sale.vdef20,  so_sale.pk_defdoc1,so_sale.pk_defdoc2,so_sale.pk_defdoc3,so_sale.pk_defdoc4, so_sale.pk_defdoc5,so_sale.pk_defdoc6,so_sale.pk_defdoc7,so_sale.pk_defdoc8, so_sale.pk_defdoc9,so_sale.pk_defdoc10,so_sale.pk_defdoc11,so_sale.pk_defdoc12, so_sale.pk_defdoc13,so_sale.pk_defdoc14,so_sale.pk_defdoc15,so_sale.pk_defdoc16, so_sale.pk_defdoc17,so_sale.pk_defdoc18,so_sale.pk_defdoc19,so_sale.pk_defdoc20,so_saleexecute.ts,so_timedetail.timenew,so_timedetail.timeaudit  " + addfieldsql.toString() + " FROM so_sale, so_saleorder_b,so_saleexecute,so_timedetail " + " where so_sale.csaleid = so_saleorder_b.csaleid AND so_sale.csaleid = so_saleexecute.csaleid  " + " AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid  " + " And so_sale.csaleid=so_timedetail.cbillid " + " AND so_saleexecute.creceipttype='30' AND so_saleorder_b.beditflag = 'N' AND so_sale.dr=0  AND " + swhere;

    SCMEnv.out(sql);

    SaleorderBVO saleItem = null;
    SaleorderHVO saleHeader = null;

    HashMap hordhvo = new HashMap();
    HashMap hordbvo = new HashMap();
    ArrayList lordbvolist = null;

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        saleHeader = new SaleorderHVO();

        String pk_corp = rs.getString(1);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(4);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(5);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(6);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(7);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(8);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(9);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String ccustomerid = rs.getString(10);
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString(11);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(12);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(13);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(14);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(15);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(16);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(17);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(18);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(19);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(20);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate.toString()));

        String cwarehouseid = rs.getString(21);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(22);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(23);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(24);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(25);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        Object nsubscription = (BigDecimal)rs.getObject(26);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription.toString()));

        String ccreditnum = rs.getString(27);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(28);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(29);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(30);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(31);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(32);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(33);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(34);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(40);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(41);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(42);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(43);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ccalbodyid = rs.getString(44);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String csaleid = rs.getString(45);
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String bretinvflag = rs.getString(46);
        saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

        String boutendflag = rs.getString(47);
        saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

        String binvoicendflag = rs.getString(48);
        saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

        String breceiptendflag = rs.getString(49);
        saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));

        String ts = rs.getString(50);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        BigDecimal npreceiverate = (BigDecimal)rs.getObject(51);
        saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));

        BigDecimal npreceivemny = (BigDecimal)rs.getObject(52);
        saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

        if (hordhvo.get(saleHeader.getCsaleid()) == null) {
          hordhvo.put(saleHeader.getCsaleid(), saleHeader);
        }

        saleItem = new SaleorderBVO();

        String corder_bid = rs.getString(53);
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        csaleid = rs.getString(54);
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString(55);
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        creceipttype = rs.getString(56);
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString(57);
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString(58);
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString(59);
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString(60);
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString(61);
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject(62);
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject(63);
        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString(64);
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString(65);
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString(66);
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString(67);
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString(68);
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString(69);
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        veditreason = rs.getString(70);
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString(71);
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject(72);
        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        ndiscountrate = (BigDecimal)rs.getObject(73);
        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(74);
        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(75);
        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject(76);
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject(77);
        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject(78);
        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject(79);
        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject(80);

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject(81);
        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject(82);
        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject(83);
        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject(84);

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject(85);
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject(86);
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject(87);
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject(88);
        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject(89);
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject(90);
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject(91);
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject(92);
        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        coperatorid = rs.getString(93);
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject(94);
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString(95);
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString(96);
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString(97);
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject(98);
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString(99);
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        String cfreezeid = rs.getString(100);
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ct_manageid = rs.getString(101);
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        ts = rs.getString(102);
        saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        String cadvisecalbodyid = rs.getString(103);
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        String boosflag = rs.getString(104);
        saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));

        String bsupplyflag = rs.getString(105);
        saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));

        String creceiptareaid = rs.getString(106);
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        vreceiveaddress = rs.getString(107);
        saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        creceiptcorpid = rs.getString(108);
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String crowno = rs.getString(109);
        saleItem.setCrowno(crowno == null ? null : crowno.trim());

        creceipttype = rs.getString(110);

        BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(111);
        saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(112);
        saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(113);
        saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(114);
        saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(115);

        saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(116);
        saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(117);
        saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        BigDecimal ntotalcostmny = (BigDecimal)rs.getObject(118);
        saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

        String bifinvoicefinish = rs.getString(119);
        saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        String bifreceiptfinish = rs.getString(120);
        saleItem.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        String bifinventoryfinish = rs.getString(121);
        saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        String bifpayfinish = rs.getString(122);
        saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        String bifpaybalance = rs.getString(123);
        saleItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String bifpaysign = rs.getString(124);
        saleItem.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

        BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject(125);

        saleItem.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject(126);
        saleItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject(127);
        saleItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(128);
        saleItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject(129);

        saleItem.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

        BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject(130);
        saleItem.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

        BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject(131);
        saleItem.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

        BigDecimal nassistcurprice = (BigDecimal)rs.getObject(132);
        saleItem.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

        String cprojectid = rs.getString(133);
        saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(134);
        saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String cprojectid3 = rs.getString(135);
        saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

        String vfree1 = rs.getString(136);
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(137);
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(138);
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(139);
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(140);
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        vdef1 = rs.getString(141);
        saleItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        vdef2 = rs.getString(142);
        saleItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        vdef3 = rs.getString(143);
        saleItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        vdef4 = rs.getString(144);
        saleItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        vdef5 = rs.getString(145);
        saleItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        vdef6 = rs.getString(146);
        saleItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        Integer iprintcount = (Integer)rs.getObject(147);
        saleHeader.setIprintcount(iprintcount == null ? null : iprintcount);

        String stransflag = rs.getString(148);
        saleHeader.setBdeliver(stransflag == null ? null : new UFBoolean(stransflag.trim()));

        String vdef11 = rs.getString(149);
        saleHeader.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(150);
        saleHeader.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(151);
        saleHeader.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(152);
        saleHeader.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(153);
        saleHeader.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(154);
        saleHeader.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(155);
        saleHeader.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(156);
        saleHeader.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(157);
        saleHeader.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(158);
        saleHeader.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(159);
        saleHeader.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(160);
        saleHeader.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(161);
        saleHeader.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(162);
        saleHeader.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(163);
        saleHeader.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(164);
        saleHeader.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(165);
        saleHeader.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(166);
        saleHeader.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(167);
        saleHeader.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(168);
        saleHeader.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(169);
        saleHeader.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(170);
        saleHeader.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(171);
        saleHeader.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(172);
        saleHeader.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(173);
        saleHeader.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(174);
        saleHeader.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(175);
        saleHeader.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(176);
        saleHeader.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(177);
        saleHeader.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(178);
        saleHeader.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String exets = rs.getString(179);
        saleItem.setExets(exets == null ? null : new UFDateTime(exets.trim()));

        String timenew = rs.getString(180);
        saleHeader.setTimenew(timenew == null ? null : new UFDateTime(timenew.trim()));

        String timeaudit = rs.getString(181);
        saleHeader.setTimeaudit(timeaudit == null ? null : new UFDateTime(timeaudit.trim()));

        if (addfields != null) {
          int i = 0; for (int loop = addfields.length; i < loop; i++) {
            getValueFromResultSet(rs, 182 + i, addfields[i], saleItem);
          }

        }

        lordbvolist = (ArrayList)hordbvo.get(saleItem.getCsaleid());
        if (lordbvolist == null) {
          lordbvolist = new ArrayList();
          hordbvo.put(saleItem.getCsaleid(), lordbvolist);
        }
        lordbvolist.add(saleItem);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }

      }
      catch (Exception e)
      {
      }

    }

    if (hordhvo.size() <= 0) {
      return null;
    }
    SaleOrderVO[] ordvos = new SaleOrderVO[hordhvo.size()];
    Map.Entry entry = null;
    Iterator iter = hordhvo.entrySet().iterator();
    int i = 0;
    while (iter.hasNext()) {
      ordvos[i] = new SaleOrderVO();
      entry = (Map.Entry)iter.next();
      ordvos[i].setParentVO((SaleorderHVO)entry.getValue());
      lordbvolist = (ArrayList)hordbvo.get(entry.getKey());
      if ((lordbvolist != null) && (lordbvolist.size() > 0)) {
        ordvos[i].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])lordbvolist.toArray(new SaleorderBVO[lordbvolist.size()]));
      }
      else
        ordvos[i].setChildrenVO(null);
      i++;
    }

    return (AggregatedValueObject[])ordvos;
  }

  public void queryFollowBody(SaleorderBVO[] bodys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { bodys });

    if ((bodys == null) || (bodys.length <= 0)) {
      return;
    }
    SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
        }
      }
    }
    String sql = "select csale_bid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ntotalplanreceiptnumber, ntotalreturnnumber " + addfieldsql.toString() + " from so_saleexecute " + " where creceipttype = '30' AND csaleid = ? order by csale_bid";

    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    stmt = con.prepareStatement(sql);
    try {
      SaleorderBVO saleexecute = bodys[0];

      String key = saleexecute.getCsaleid();
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();
      int i = 0;
      while ((rs.next()) && 
        (i < bodys.length)) {
        saleexecute = bodys[i];

        String csale_bid = rs.getString(1);
        if (csale_bid.equals(saleexecute.getPrimaryKey()))
        {
          BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(3);
          saleexecute.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

          BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(4);

          saleexecute.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

          BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(5);

          saleexecute.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

          BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(6);

          saleexecute.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

          BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(7);

          saleexecute.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

          BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(8);

          saleexecute.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

          BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(9);

          saleexecute.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

          BigDecimal ntotalcostmny = (BigDecimal)rs.getObject(10);

          saleexecute.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

          String bifinvoicefinish = rs.getString(11);
          saleexecute.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

          String bifreceiptfinish = rs.getString(12);
          saleexecute.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

          String bifinventoryfinish = rs.getString(13);
          saleexecute.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

          String bifpayfinish = rs.getString(14);
          saleexecute.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

          String bifpaybalance = rs.getString(15);
          saleexecute.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

          String bifpaysign = rs.getString(16);
          saleexecute.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

          BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject(17);

          saleexecute.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

          BigDecimal nassistcursummny = (BigDecimal)rs.getObject(18);

          saleexecute.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

          BigDecimal nassistcurmny = (BigDecimal)rs.getObject(19);

          saleexecute.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

          BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(20);

          saleexecute.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

          BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject(21);

          saleexecute.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

          BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject(22);

          saleexecute.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

          BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject(23);

          saleexecute.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

          BigDecimal nassistcurprice = (BigDecimal)rs.getObject(24);

          saleexecute.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

          String cprojectid = rs.getString(25);
          saleexecute.setCprojectid(cprojectid == null ? null : cprojectid.trim());

          String cprojectphaseid = rs.getString(26);
          saleexecute.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

          String cprojectid3 = rs.getString(27);
          saleexecute.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

          String vfree1 = rs.getString(28);
          saleexecute.setVfree1(vfree1 == null ? null : vfree1.trim());

          String vfree2 = rs.getString(29);
          saleexecute.setVfree2(vfree2 == null ? null : vfree2.trim());

          String vfree3 = rs.getString(30);
          saleexecute.setVfree3(vfree3 == null ? null : vfree3.trim());

          String vfree4 = rs.getString(31);
          saleexecute.setVfree4(vfree4 == null ? null : vfree4.trim());

          String vfree5 = rs.getString(32);
          saleexecute.setVfree5(vfree5 == null ? null : vfree5.trim());

          String vdef1 = rs.getString(33);
          saleexecute.setVdef1(vdef1 == null ? null : vdef1.trim());

          String vdef2 = rs.getString(34);
          saleexecute.setVdef2(vdef2 == null ? null : vdef2.trim());

          String vdef3 = rs.getString(35);
          saleexecute.setVdef3(vdef3 == null ? null : vdef3.trim());

          String vdef4 = rs.getString(36);
          saleexecute.setVdef4(vdef4 == null ? null : vdef4.trim());

          String vdef5 = rs.getString(37);
          saleexecute.setVdef5(vdef5 == null ? null : vdef5.trim());

          String vdef6 = rs.getString(38);
          saleexecute.setVdef6(vdef6 == null ? null : vdef6.trim());

          BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject(39);

          saleexecute.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

          BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject(40);

          saleexecute.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

          getOrdExeValueFromResultSet(rs, 41, saleexecute);

          i += 1;
        }
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { bodys });
  }

  public void queryFollowBody(SaleorderBVO saleexecute)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { saleexecute });

    if (saleexecute == null) {
      return;
    }
    SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
        }
      }
    }
    String sql = "select csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ntotalplanreceiptnumber, ntotalreturnnumber " + addfieldsql.toString() + " from so_saleexecute " + " where creceipttype = '30' AND csale_bid = ?";

    String key = saleexecute.getPrimaryKey();

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String csaleid = rs.getString(1);
        saleexecute.setCsaleid(csaleid == null ? null : csaleid.trim());

        BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(3);
        saleexecute.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(4);
        saleexecute.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(5);
        saleexecute.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(6);
        saleexecute.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(7);
        saleexecute.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(8);
        saleexecute.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(9);
        saleexecute.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        BigDecimal ntotalcostmny = (BigDecimal)rs.getObject(10);
        saleexecute.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

        String bifinvoicefinish = rs.getString(11);
        saleexecute.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        String bifreceiptfinish = rs.getString(12);
        saleexecute.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        String bifinventoryfinish = rs.getString(13);
        saleexecute.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        String bifpayfinish = rs.getString(14);
        saleexecute.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        String bifpaybalance = rs.getString(15);
        saleexecute.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String bifpaysign = rs.getString(16);
        saleexecute.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

        BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject(17);

        saleexecute.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject(18);
        saleexecute.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject(19);
        saleexecute.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(20);
        saleexecute.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject(21);

        saleexecute.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

        BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject(22);
        saleexecute.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

        BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject(23);
        saleexecute.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

        BigDecimal nassistcurprice = (BigDecimal)rs.getObject(24);
        saleexecute.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

        String cprojectid = rs.getString(25);
        saleexecute.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(26);
        saleexecute.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String cprojectid3 = rs.getString(27);
        saleexecute.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

        String vfree1 = rs.getString(28);
        saleexecute.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(29);
        saleexecute.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(30);
        saleexecute.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(31);
        saleexecute.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(32);
        saleexecute.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(33);
        saleexecute.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(34);
        saleexecute.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(35);
        saleexecute.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(36);
        saleexecute.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(37);
        saleexecute.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(38);
        saleexecute.setVdef6(vdef6 == null ? null : vdef6.trim());

        BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject(39);

        saleexecute.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

        BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject(40);
        saleexecute.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

        getOrdExeValueFromResultSet(rs, 41, saleexecute);
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { saleexecute });
  }

  public SaleorderBVO[] queryOrderEnd(String strWhere)
    throws SQLException
  {
    String strSql = "select corder_bid,  fstatus, so_saleorder_b.csaleid,cinvbasdocid,nnumber,ntaxnetprice, nsummny,isnull(nnumber,0)-isnull(ntotalreceiptnumber,0) as ntotalreceiptnumber,bifreceiptfinish,isnull(nnumber,0)-isnull(ntotalinventorynumber,0) as ntotalinventorynumber,bifinventoryfinish,isnull(nnumber,0)-isnull(ntotalinvoicenumber,0) as ntotalinvoicenumber,bifinvoicefinish,(isnull(nnumber,0)-isnull(ntotalinvoicenumber,0))*isnull(ntaxnetprice,0)+ isnull(ntotalinvoicemny,0)-isnull(ntotalpaymny,0) as ntotalpaymny,bifpayfinish,frowstatus,so_sale.ts,so_saleorder_b.ts,so_saleexecute.ts,ccurrencytypeid,so_sale.cfreecustid,so_sale.ccustomerid  FROM so_sale INNER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid INNER JOIN so_saleexecute ON (so_saleorder_b.corder_bid = so_saleexecute.csale_bid and so_saleorder_b.csaleid = so_saleexecute.csaleid ) WHERE 1=1 and so_sale.dr=0 and so_sale.fstatus in (2,4,6) and so_saleorder_b.beditflag = 'N'  and  so_saleexecute.creceipttype = '30'  ";

    if ((strWhere != null) && (!strWhere.equals("")))
      strSql = strSql + " and " + strWhere;
    strSql = strSql + "order by so_saleorder_b.csaleid,corder_bid";

    SaleorderBVO[] saleItems = null;
    Vector v = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    try {
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        Object fstatus = rs.getObject("fstatus");
        saleItem.setFinished(new UFBoolean(Integer.valueOf(fstatus.toString()).intValue() == 6));

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        Object o = rs.getObject("nnumber");
        if (o != null) {
          BigDecimal nnumber = new BigDecimal(o.toString());
          saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
        }

        o = rs.getObject("ntaxnetprice");
        if (o != null) {
          BigDecimal ntaxnetprice = new BigDecimal(o.toString());
          saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
        }

        o = rs.getObject("nsummny");
        if (o != null) {
          BigDecimal nsummny = new BigDecimal(o.toString());
          saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
        }

        o = rs.getObject("ntotalreceiptnumber");
        if (o != null) {
          BigDecimal ntotalreceiptnumber = new BigDecimal(o.toString());

          saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));
        }

        String bifreceiptfinish = rs.getString("bifreceiptfinish");
        saleItem.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        o = rs.getObject("ntotalinventorynumber");
        if (o != null) {
          BigDecimal ntotalinventorynumber = new BigDecimal(o.toString());

          saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));
        }

        String bifinventoryfinish = rs.getString("bifinventoryfinish");
        saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        o = rs.getObject("ntotalinvoicenumber");
        if (o != null) {
          BigDecimal ntotalinvoicenumber = new BigDecimal(o.toString());

          saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));
        }

        String bifinvoicefinish = rs.getString("bifinvoicefinish");
        saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        o = rs.getObject("ntotalpaymny");
        if (o != null) {
          BigDecimal ntotalpaymny = new BigDecimal(o.toString());
          saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
        }

        String bifpayfinish = rs.getString("bifpayfinish");
        saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        saleItem.setFrowstatus(new Integer(rs.getInt("frowstatus")));

        String ts = rs.getString(17);
        saleItem.m_headts = (ts == null ? null : new UFDateTime(ts.trim()));

        ts = rs.getString(18);
        saleItem.m_ts = (ts == null ? null : new UFDateTime(ts.trim()));

        ts = rs.getString(19);
        saleItem.m_exets = (ts == null ? null : new UFDateTime(ts.trim()));

        String ccurrencytypeid = rs.getString(20);
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        String cfreecustid = rs.getString(21);
        saleItem.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        String ccustomerid = rs.getString(22);
        saleItem.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        v.addElement(saleItem);
      }
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
    saleItems = new SaleorderBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }

    return saleItems;
  }

  public String[] queryOrderIDByContractID(String strContractID)
    throws SQLException
  {
    String[] strIDs = null;
    String strSql = "select distinct csaleid from so_saleorder_b where (creceipttype = ?  or creceipttype = ?) and csourcebillid = ?   and frowstatus <> 5 order by csaleid ";

    Vector v = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    stmt = con.prepareStatement(strSql);

    stmt.setString(1, "Z4");
    stmt.setString(2, "Z3");
    stmt.setString(3, strContractID);

    ResultSet rs = stmt.executeQuery();
    try
    {
      while (rs.next())
      {
        String csaleid = rs.getString(1);
        v.addElement(csaleid);
      }
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
    if (v.size() > 0) {
      strIDs = new String[v.size()];
      v.copyInto(strIDs);
    }
    return strIDs;
  }

  public SaleOrderVO removeDelLine(SaleOrderVO voSource)
    throws SQLException, Exception
  {
    Vector vecLines = new Vector();

    SaleorderBVO[] voBodys = (SaleorderBVO[])(SaleorderBVO[])voSource.getChildrenVO();
    SCMEnv.out("//////////////////////////////\tvoBodys.length:" + voBodys.length);

    SCMEnv.out("\n");
    for (int i = 0; i < voBodys.length; i++) {
      if (voBodys[i].getStatus() != 3)
        vecLines.addElement(voBodys[i]);
    }
    SCMEnv.out("//////////////////////////////\tvecLines.size:" + vecLines.size());

    SCMEnv.out("\n");
    SaleorderBVO[] aryBodys = new SaleorderBVO[vecLines.size()];
    vecLines.copyInto(aryBodys);
    SCMEnv.out("//////////////////////////////\taryBodys.length:" + aryBodys.length);

    SCMEnv.out("\n");
    voSource.setChildrenVO(aryBodys);
    return voSource;
  }

  public Vector splitSaleOrderVOs(SaleOrderVO[] aratpvos)
  {
    if ((aratpvos == null) || (aratpvos.length <= 0))
      return null;
    Vector vlist1 = new Vector();
    Vector vlist2 = new Vector();
    Vector vlistaudit = new Vector();
    Vector vlistfinish = new Vector();
    Vector vlistre = new Vector();
    for (int i = 0; i < aratpvos.length; i++) {
      SaleorderBVO[] bvo = (SaleorderBVO[])(SaleorderBVO[])aratpvos[i].getChildrenVO();
      for (int j = 0; j < bvo.length; j++) {
        if (bvo[j].getFrowstatus().intValue() == 2)
          vlist1.add(bvo[j]);
        else if (bvo[j].getFrowstatus().intValue() == 6) {
          vlist2.add(bvo[j]);
        }
      }
      if (vlist1.size() > 0) {
        SaleOrderVO vo = new SaleOrderVO();
        vo.setParentVO(aratpvos[i].getParentVO());
        vo.setChildrenVO((SaleorderBVO[])(SaleorderBVO[])vlist1.toArray(new SaleorderBVO[0]));

        vlistaudit.add(vo);
      }
      if (vlist2.size() > 0) {
        SaleOrderVO vo = new SaleOrderVO();
        vo.setParentVO(aratpvos[i].getParentVO());
        vo.setChildrenVO((SaleorderBVO[])(SaleorderBVO[])vlist2.toArray(new SaleorderBVO[0]));

        vlistfinish.add(vo);
      }
      vlist1.clear();
      vlist2.clear();
    }
    vlistre.add(vlistaudit);
    vlistre.add(vlistfinish);
    return vlistre;
  }

  public ArrayList update(SaleOrderVO saleorder)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    SaleorderHVO hvo = (SaleorderHVO)saleorder.getParentVO();
    ArrayList listID = null;

    if ((saleorder.getIAction() == 1) || (saleorder.getIAction() == 10))
    {
      if ((saleorder.getOldSaleOrderVO() != null) && (!hvo.getCcustomerid().equals(saleorder.getOldSaleOrderVO().getHeadVO().getCcustomerid())))
      {
        UFDouble dcathpay = queryCachPayByOrdId(saleorder.getHeadVO().getCsaleid());

        if ((dcathpay != null) && (dcathpay.doubleValue() != 0.0D)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000011"));
        }

      }

    }

    try
    {
      if ((hvo.getVreceiptcode() == null) || (hvo.getVreceiptcode().trim().equals("")) || ((hvo.getVoldreceiptcode() != null) && (!hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode()))))
      {
        CheckValueValidityImpl dmocode = new CheckValueValidityImpl();

        String vreceiptcode = dmocode.getSysBillNO(saleorder);
        saleorder.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);

        dmocode.isValueValidity(saleorder);
      }

      try
      {
        onCheck(saleorder);
      } catch (Exception ex) {
        saleorder.getParentVO().setAttributeValue("vreceiptcode", null);
        if (ex.getClass() == BusinessException.class) {
          throw ((BusinessException)ex);
        }
        throw new BusinessException(ex.getMessage());
      }

      try
      {
        ATPVO[] atpvos = getUpdateOtherCorpAtpVO(saleorder.getAllSaleOrderVO());

        if (atpvos != null)
        {
          IICPub_InvATPDMO invatpdmo = (IICPub_InvATPDMO)NCLocator.getInstance().lookup(IICPub_InvATPDMO.class.getName());

          invatpdmo.modifyATP(atpvos);
        }

      }
      catch (Exception e)
      {
        if (e.getClass() == BusinessException.class) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage());
      }

      SaleorderHVO headVO = (SaleorderHVO)saleorder.getParentVO();
      SaleorderBVO[] bodyVO = (SaleorderBVO[])(SaleorderBVO[])saleorder.getChildrenVO();

      if ((saleorder.getErrMsg() != null) && (saleorder.getErrMsg().indexOf(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000005")) >= 0))
      {
        headVO.setBoverdate(new UFBoolean(true));
      }

      try
      {
        ArrayList retlist = fillPkAndTs(saleorder);
        updateHead(headVO);
        listID = updateBodys(bodyVO, headVO.getPrimaryKey(), headVO.getCreceipttype());

        listID = retlist;
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }
      finally
      {
      }

      checkStoreStructure(saleorder, headVO.getCsaleid());

      checkRecStoreStructure(saleorder, headVO.getCsaleid());

      setRetNum(saleorder);

      updateBomID(saleorder);

      if (saleorder.getIAction() == 5)
      {
        SaleorderBVO[] ordbvos = saleorder.getBodyVOs();

        if ((ordbvos != null) && (ordbvos.length > 0))
        {
          Hashtable params = new Hashtable();

          Object[] ovalues = null;

          Object[] ovaluesfor33 = null;
          ArrayList paramfor33 = new ArrayList();

          String corder_bid = null;
          String scurdate = saleorder.getDcurdate() == null ? saleorder.getHeadVO().getDmakedate().toString() : saleorder.getDcurdate().toString();

          int i = 0; for (int loop = ordbvos.length; i < loop; i++)
          {
            corder_bid = ordbvos[i].getCorder_bid();

            if ((corder_bid == null) || (corder_bid.trim().length() <= 0))
            {
              continue;
            }
            if ((ordbvos[i].getStatus() != 4) && (ordbvos[i].getStatus() != 1)) {
              continue;
            }
            ovalues = new Object[8];

            ovalues[0] = ordbvos[i].getNtaxnetprice();

            ovalues[1] = ordbvos[i].getNnetprice();

            ovalues[2] = ordbvos[i].getBsafeprice();

            ovalues[3] = ordbvos[i].getBreturnprofit();

            ovalues[4] = ordbvos[i].getCquoteunitid();
            ovalues[5] = ordbvos[i].getNqtscalefactor();
            ovalues[6] = ordbvos[i].getCcurrencytypeid();

            ovalues[7] = ordbvos[i].getNorgqttaxnetprc();

            params.put(corder_bid, ovalues);

            ovaluesfor33 = new Object[20];

            ovaluesfor33[0] = ordbvos[i].getCorder_bid();
            ovaluesfor33[1] = ordbvos[i].getNoriginalcurnetprice();

            ovaluesfor33[2] = ordbvos[i].getNoriginalcurtaxnetprice();

            ovaluesfor33[3] = ordbvos[i].getNnetprice();
            ovaluesfor33[4] = ordbvos[i].getNtaxnetprice();
            ovaluesfor33[5] = ordbvos[i].getNtaxrate();
            ovaluesfor33[6] = ordbvos[i].getNexchangeotobrate();
            ovaluesfor33[7] = ordbvos[i].getNexchangeotoarate();
            ovaluesfor33[8] = ordbvos[i].getCcurrencytypeid();
            ovaluesfor33[9] = scurdate;
            ovaluesfor33[10] = ordbvos[i].getCquoteunitid();
            ovaluesfor33[11] = SoVoTools.div(ordbvos[i].getNquoteunitnum(), ordbvos[i].getNnumber());

            ovaluesfor33[12] = ordbvos[i].getNqttaxnetprc();
            ovaluesfor33[13] = ordbvos[i].getNqtnetprc();
            ovaluesfor33[14] = ordbvos[i].getNqttaxprc();
            ovaluesfor33[15] = ordbvos[i].getNqtprc();
            ovaluesfor33[16] = ordbvos[i].getNorgqttaxnetprc();
            ovaluesfor33[17] = ordbvos[i].getNorgqtnetprc();
            ovaluesfor33[18] = ordbvos[i].getNorgqttaxprc();
            ovaluesfor33[19] = ordbvos[i].getNorgqtprc();

            paramfor33.add(ovaluesfor33);
          }

          if (params.size() > 0)
          {
            try
            {
              IICPub_GeneralBillDMO gdmo = (IICPub_GeneralBillDMO)NCLocator.getInstance().lookup(IICPub_GeneralBillDMO.class.getName());

              String retic = gdmo.changeSalePrices(params, saleorder.getCuruserid(), scurdate, saleorder.getHeadVO().getPk_corp(), "30");

              if (retic != null)
              {
                if ((saleorder.getErrMsg() != null) && (saleorder.getErrMsg().trim().length() > 0))
                {
                  saleorder.setErrMsg(saleorder.getErrMsg() + "\n" + retic);
                }
                else
                  saleorder.setErrMsg(retic);
              }
            }
            catch (Exception e)
            {
              SCMEnv.out(e.getMessage());
              throw new BusinessRuntimeException(e.getMessage());
            }

            try
            {
              SquareDMO sqdmo = new SquareDMO();
              sqdmo.orderChangeToSquare(paramfor33, saleorder.getHeadVO().getPk_corp(), saleorder.getCuruserid());
            }
            catch (BusinessException e)
            {
              throw e;
            } catch (Exception e) {
              SCMEnv.out(e.getMessage());
              throw new BusinessRuntimeException(e.getMessage());
            }
          }

        }

        try
        {
          DMVO othervo = saleorder.getChgVOValueForUpdateOtherBill();
          if (othervo != null) {
            SOToolsImpl toolbo = new SOToolsImpl();
            toolbo.runComMethod("TO", "nc.bs.to.outer.OuterBO", "updateOrderWhenReviseSO", new Class[] { ClientLink.class, DMVO.class }, new Object[] { saleorder.getClientLink(), othervo });
          }

          BillConvertDMO bcdmo = new BillConvertDMO();
          bcdmo.writeBackArrangeInfoFor30(new SaleOrderVO[] { saleorder });
        }
        catch (ProdNotInstallException e)
        {
          SCMEnv.out(e.getMessage());
        } catch (BusinessException e) {
          throw e;
        } catch (Exception e) {
          SCMEnv.out(e.getMessage());
          throw new BusinessRuntimeException(e.getMessage());
        }

      }

      if ((hvo.getVreceiptcode() != null) && (hvo.getVoldreceiptcode() != null) && (!hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode())))
      {
        CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
        try
        {
          dmocode.returnBillNo(saleorder, "voldreceiptcode");
        } catch (Exception e) {
          SCMEnv.out(e.getMessage());

          throw new BusinessException("回退单据号错误!");
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      try
      {
        if (!e.getMessage().equals(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000068")))
        {
          if ((hvo.getVreceiptcode() != null) && (hvo.getVoldreceiptcode() != null) && (!hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode())))
          {
            CheckValueValidityImpl dmocode = new CheckValueValidityImpl();

            dmocode.returnBillNo(saleorder, "vreceiptcode");
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();

        throw new BusinessException(ex.getMessage());
      }
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }

    return listID;
  }

  public void updateBody(SaleorderBVO saleItem, String creceipttype, PreparedStatement stmt, PreparedStatement stmtfollow2)
    throws SQLException, nc.bs.pub.SystemException
  {
    try
    {
      if (saleItem.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleItem.getCsaleid());
      }
      if (saleItem.getPkcorp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getPkcorp());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, saleItem.getNpacknumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getDconsigndate() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getDconsigndate().toString());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getDdeliverdate().toString());
      }
      if (saleItem.getBlargessflag() == null)
      {
        stmt.setString(14, "N");
      }
      else stmt.setString(14, saleItem.getBlargessflag().toString());

      if (saleItem.getCeditsaleid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCeditsaleid());
      }
      if (saleItem.getBeditflag() == null)
      {
        stmt.setString(16, "N");
      }
      else stmt.setString(16, saleItem.getBeditflag().toString());

      if (saleItem.getVeditreason() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleItem.getVeditreason());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(30, 4);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(31, 4);
      else {
        stmt.setBigDecimal(31, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setBigDecimal(33, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setBigDecimal(35, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(36, 4);
      else {
        stmt.setBigDecimal(36, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(37, 4);
      else {
        stmt.setBigDecimal(37, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(38, 4);
      else {
        stmt.setBigDecimal(38, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(39, 4);
      else {
        stmt.setBigDecimal(39, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getCoperatorid() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleItem.getCoperatorid());
      }
      if (saleItem.getFrowstatus() == null)
        stmt.setNull(41, 4);
      else {
        stmt.setInt(41, saleItem.getFrowstatus().intValue());
      }
      if (saleItem.getFrownote() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleItem.getFrownote());
      }

      if (saleItem.getCinvbasdocid() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleItem.getCinvbasdocid());
      }

      if (saleItem.getCbatchid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleItem.getCbatchid());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(45, 4);
      else {
        stmt.setInt(45, saleItem.getFbatchstatus().intValue());
      }

      if (saleItem.getCbomorderid() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, saleItem.getCbomorderid());
      }

      if (saleItem.getCfreezeid() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleItem.getCfreezeid());
      }

      if (saleItem.getCt_manageid() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getCt_manageid());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getBoosflag() == null)
        stmt.setString(50, "N");
      else {
        stmt.setString(50, saleItem.getBoosflag().toString());
      }

      if (saleItem.getBsupplyflag() == null)
        stmt.setString(51, "N");
      else {
        stmt.setString(51, saleItem.getBsupplyflag().toString());
      }

      if (saleItem.getCreceiptareaid() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCreceiptareaid());
      }

      if (saleItem.getVreceiveaddress() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getVreceiveaddress());
      }

      if (saleItem.getCreceiptcorpid() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleItem.getCreceiptcorpid());
      }

      if (saleItem.getCrowno() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleItem.getCrowno());
      }

      if (saleItem.getTs() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleItem.getTs().toString());
      }

      SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();

      setPreStatementOrdB(stmt, 57, saleItem);

      stmt.setString(57 + (addfields == null ? 0 : addfields.length), saleItem.getPrimaryKey());

      executeUpdate(stmt);
    }
    finally {
    }
    updateFollowBody(saleItem, creceipttype, stmtfollow2);
  }

  public void updateBody(SaleorderBVO saleItem, PreparedStatement stmt, PreparedStatement stmtfollow)
    throws SQLException, nc.bs.pub.SystemException
  {
    try
    {
      if (saleItem.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleItem.getCsaleid());
      }
      if (saleItem.getPkcorp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getPkcorp());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, saleItem.getNpacknumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getDconsigndate() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getDconsigndate().toString());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getDdeliverdate().toString());
      }
      if (saleItem.getBlargessflag() == null)
      {
        stmt.setString(14, "N");
      }
      else stmt.setString(14, saleItem.getBlargessflag().toString());

      if (saleItem.getCeditsaleid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCeditsaleid());
      }
      if (saleItem.getBeditflag() == null)
      {
        stmt.setString(16, "N");
      }
      else stmt.setString(16, saleItem.getBeditflag().toString());

      if (saleItem.getVeditreason() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleItem.getVeditreason());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(30, 4);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(31, 4);
      else {
        stmt.setBigDecimal(31, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setBigDecimal(33, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setBigDecimal(35, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(36, 4);
      else {
        stmt.setBigDecimal(36, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(37, 4);
      else {
        stmt.setBigDecimal(37, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(38, 4);
      else {
        stmt.setBigDecimal(38, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(39, 4);
      else {
        stmt.setBigDecimal(39, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getCoperatorid() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleItem.getCoperatorid());
      }
      if (saleItem.getFrowstatus() == null)
        stmt.setNull(41, 4);
      else {
        stmt.setInt(41, saleItem.getFrowstatus().intValue());
      }
      if (saleItem.getFrownote() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleItem.getFrownote());
      }

      if (saleItem.getCinvbasdocid() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleItem.getCinvbasdocid());
      }

      if (saleItem.getCbatchid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleItem.getCbatchid());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(45, 4);
      else {
        stmt.setInt(45, saleItem.getFbatchstatus().intValue());
      }

      if (saleItem.getCbomorderid() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, saleItem.getCbomorderid());
      }

      if (saleItem.getCfreezeid() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleItem.getCfreezeid());
      }

      if (saleItem.getCt_manageid() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getCt_manageid());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getBoosflag() == null)
        stmt.setString(50, "N");
      else {
        stmt.setString(50, saleItem.getBoosflag().toString());
      }

      if (saleItem.getBsupplyflag() == null)
        stmt.setString(51, "N");
      else {
        stmt.setString(51, saleItem.getBsupplyflag().toString());
      }

      if (saleItem.getCreceiptareaid() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCreceiptareaid());
      }

      if (saleItem.getVreceiveaddress() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getVreceiveaddress());
      }

      if (saleItem.getCreceiptcorpid() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleItem.getCreceiptcorpid());
      }

      if (saleItem.getCrowno() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleItem.getCrowno());
      }

      if (saleItem.getTs() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleItem.getTs().toString());
      }

      SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();

      setPreStatementOrdB(stmt, 57, saleItem);

      stmt.setString(57 + (addfields == null ? 0 : addfields.length), saleItem.getPrimaryKey());

      executeUpdate(stmt);
    } finally {
    }
    updateFollowBody(saleItem, stmtfollow);
  }

  public ArrayList updateBodys(SaleorderBVO[] saleorderB, String strMainID, String creceipttype)
    throws SQLException, nc.bs.pub.SystemException
  {
    ArrayList listRet = new ArrayList();
    listRet.add(strMainID);
    if ((saleorderB == null) || (saleorderB.length <= 0)) {
      return listRet;
    }

    SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    StringBuffer addfieldvaluesql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldvaluesql.append(",?");
        }
      }
    }

    String sql = "insert into so_saleorder_b(corder_bid, csaleid, pk_corp, creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, veditreason, ccurrencytypeid, nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, frowstatus, frownote, cinvbasdocid, cbatchid, fbatchstatus, cbomorderid, ct_manageid, cfreezeid, cadvisecalbodyid, boosflag, bsupplyflag, creceiptareaid, vreceiveaddress, creceiptcorpid, crowno,ts " + addfieldsql.toString() + ") " + "values(" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ? " + addfieldvaluesql.toString() + ")";

    PreparedStatement stmt = null;

    addfields = SaleorderBVO.getSoSaleExecuteAddFields();
    addfieldsql.setLength(0);
    addfieldsql.append("");
    addfieldvaluesql.setLength(0);
    addfieldvaluesql.append("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldvaluesql.append(",?");
        }
      }
    }
    String sqlexe = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1, vdef6, vdef5, vdef4, vdef3, vdef2, vdef1, ts " + addfieldsql.toString() + ")" + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? " + addfieldvaluesql.toString() + ")";

    PreparedStatement stmtexe = null;

    addfields = SaleorderBVO.getSoSaleOrderBAddFields();
    addfieldsql.setLength(0);
    addfieldsql.append("");
    addfieldvaluesql.setLength(0);
    addfieldvaluesql.append("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldsql.append(" = ? ");
        }
      }
    }
    String sqlupdate = "update so_saleorder_b set csaleid = ?, pk_corp = ?, creceipttype = ?, csourcebillid = ?, csourcebillbodyid = ?, cinventoryid = ?, cunitid = ?, cpackunitid = ?, nnumber = ?, npacknumber = ?, cbodywarehouseid = ?, dconsigndate = ?, ddeliverdate = ?, blargessflag = ?, ceditsaleid = ?, beditflag = ?, veditreason = ?, ccurrencytypeid = ?, nitemdiscountrate = ?, ndiscountrate = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, ntaxrate = ?, noriginalcurprice = ?, noriginalcurtaxprice = ?, noriginalcurnetprice = ?, noriginalcurtaxnetprice = ?, noriginalcurtaxmny = ?, noriginalcurmny = ?, noriginalcursummny = ?, noriginalcurdiscountmny = ?, nprice = ?, ntaxprice = ?, nnetprice = ?, ntaxnetprice = ?, ntaxmny = ?, nmny = ?, nsummny = ?, ndiscountmny = ?, coperatorid = ?, frowstatus = ?, frownote = ?,cinvbasdocid = ?,cbatchid = ?,fbatchstatus = ?,cbomorderid=?,cfreezeid=?,ct_manageid=?, cadvisecalbodyid = ?, boosflag = ?, bsupplyflag = ?, creceiptareaid= ?, vreceiveaddress= ?, creceiptcorpid= ?, crowno = ? ,ts = ?" + addfieldsql.toString() + " where corder_bid = ?";

    PreparedStatement stmtupdate = null;

    addfields = SaleorderBVO.getSoSaleExecuteAddFields();
    addfieldsql.setLength(0);
    addfieldsql.append("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldsql.append(" = ? ");
        }
      }
    }
    String sqlfollow = "update so_saleexecute set nassistcurdiscountmny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, nassistcurtaxnetprice = ?, nassistcurnetprice = ?, nassistcurtaxprice = ?, nassistcurprice = ?, cprojectid3 = ?, cprojectphaseid = ?, cprojectid = ?, vfree5 = ?, vfree4 = ?, vfree3 = ?, vfree2 = ?, vfree1 = ?, vdef6 = ?, vdef5 = ?, vdef4 = ?, vdef3 = ?, vdef2 = ?, vdef1 = ?, ts = ? " + addfieldsql.toString() + " where csale_bid = ?";

    String sqlfollow2 = "update so_saleexecute set csaleid = ?, creceipttype = ?, nassistcurdiscountmny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, nassistcurtaxnetprice = ?, nassistcurnetprice = ?, nassistcurtaxprice = ?, nassistcurprice = ?, cprojectid3 = ?, cprojectphaseid = ?, cprojectid = ?, vfree5 = ?, vfree4 = ?, vfree3 = ?, vfree2 = ?, vfree1 = ?, vdef6 = ?, vdef5 = ?, vdef4 = ?, vdef3 = ?, vdef2 = ?, vdef1 = ?, ts = ? " + addfieldsql.toString() + " where csale_bid = ? and creceipttype = '30' ";

    PreparedStatement stmtfollow = null;
    PreparedStatement stmtfollow2 = null;

    String deleteBodySql = " delete from so_saleorder_b where corder_bid = ? ";
    PreparedStatement stmtdeleteBody = null;

    String deleteExeSql = " delete from so_saleexecute where csale_bid = ? and creceipttype = '30' ";
    PreparedStatement stmtdeleteExe = null;

    Connection con = null;
    try {
      con = getConnection();
      ((CrossDBConnection)con).setAddTimeStamp(false);
      stmt = prepareStatement(con, sql);
      stmtexe = prepareStatement(con, sqlexe);
      stmtupdate = prepareStatement(con, sqlupdate);
      stmtfollow = prepareStatement(con, sqlfollow);
      stmtfollow2 = prepareStatement(con, sqlfollow2);

      stmtdeleteBody = prepareStatement(con, deleteBodySql);
      stmtdeleteExe = prepareStatement(con, deleteExeSql);

      for (int i = 0; i < saleorderB.length; i++)
      {
        String bomID = saleorderB[i].getCbomorderid();
        if ((bomID != null) && (bomID.trim().length() != 0))
        {
          updateBomVO(saleorderB[i]);
        }
        switch (saleorderB[i].getStatus()) {
        case 2:
          if ((saleorderB[i].getBoosflag() != null) && (saleorderB[i].getBoosflag().booleanValue())) {
            continue;
          }
          String idRow = insertBody(saleorderB[i], strMainID, creceipttype, stmt, stmtexe);

          listRet.add(idRow);
          break;
        case 3:
          deleteBodyAndExe(stmtdeleteBody, stmtdeleteExe, saleorderB[i].getPrimaryKey());

          break;
        case 1:
          updateBody(saleorderB[i], creceipttype, stmtupdate, stmtfollow2);

          break;
        case 4:
          modificationBody(saleorderB[i], strMainID, creceipttype, stmt, stmtexe, stmtupdate, stmtfollow);
        }

      }

      executeBatch(stmt);
      executeBatch(stmtexe);
      executeBatch(stmtfollow);
      executeBatch(stmtfollow2);
      executeBatch(stmtupdate);

      executeBatch(stmtdeleteBody);
      executeBatch(stmtdeleteExe);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtexe != null)
          stmtexe.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtfollow != null)
          stmtfollow.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtfollow2 != null)
          stmtfollow2.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtupdate != null)
          stmtupdate.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtdeleteBody != null)
          stmtdeleteBody.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (stmtdeleteExe != null)
          stmtdeleteExe.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try
      {
        if (con != null) {
          ((CrossDBConnection)con).setAddTimeStamp(true);
          con.close();
        }
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
    }

    return listRet;
  }

  public void updateBom(String bomID, String saleID, String saleBID)
    throws SQLException
  {
    String sql = "update so_bomorder set csaleid = '" + saleID + "', corder_bid = '" + saleBID + "' where cbomorderid = '" + bomID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
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
  }

  public void updateBomVO(SaleorderBVO voBody)
    throws SQLException
  {
    String bomID = voBody.getCbomorderid();
    String sql = "update so_bomorder set nrequirenumber = " + voBody.getNnumber() + " where cbomorderid = '" + bomID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
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
  }

  public void updateFollowBody(SaleorderBVO saleexecute, String creceipttype, PreparedStatement stmt)
    throws SQLException, nc.bs.pub.SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
    try
    {
      if (saleexecute.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleexecute.getCsaleid());
      }
      if (creceipttype == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, creceipttype);
      }

      if (saleexecute.getNassistcurdiscountmny() == null)
        stmt.setNull(3, 4);
      else {
        stmt.setBigDecimal(3, saleexecute.getNassistcurdiscountmny().toBigDecimal());
      }

      if (saleexecute.getNassistcursummny() == null)
        stmt.setNull(4, 4);
      else {
        stmt.setBigDecimal(4, saleexecute.getNassistcursummny().toBigDecimal());
      }

      if (saleexecute.getNassistcurmny() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setBigDecimal(5, saleexecute.getNassistcurmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxmny() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, saleexecute.getNassistcurtaxmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxnetprice() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleexecute.getNassistcurtaxnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurnetprice() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, saleexecute.getNassistcurnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxprice() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, saleexecute.getNassistcurtaxprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurprice() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, saleexecute.getNassistcurprice().toBigDecimal());
      }

      if (saleexecute.getCprojectid3() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleexecute.getCprojectid3());
      }
      if (saleexecute.getCprojectphaseid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleexecute.getCprojectphaseid());
      }
      if (saleexecute.getCprojectid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleexecute.getCprojectid());
      }
      if (saleexecute.getVfree5() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleexecute.getVfree5());
      }
      if (saleexecute.getVfree4() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleexecute.getVfree4());
      }
      if (saleexecute.getVfree3() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleexecute.getVfree3());
      }
      if (saleexecute.getVfree2() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleexecute.getVfree2());
      }
      if (saleexecute.getVfree1() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleexecute.getVfree1());
      }
      if (saleexecute.getVdef6() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleexecute.getVdef6());
      }
      if (saleexecute.getVdef5() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, saleexecute.getVdef5());
      }
      if (saleexecute.getVdef4() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, saleexecute.getVdef4());
      }
      if (saleexecute.getVdef3() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleexecute.getVdef3());
      }
      if (saleexecute.getVdef2() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleexecute.getVdef2());
      }
      if (saleexecute.getVdef1() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, saleexecute.getVdef1());
      }
      if (saleexecute.getTs() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, saleexecute.getTs().toString());
      }

      SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();

      setPreStatementOrdE(stmt, 26, saleexecute);

      stmt.setString(26 + (addfields == null ? 0 : addfields.length), saleexecute.getPrimaryKey());

      executeUpdate(stmt);
    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
  }

  public void updateFollowBody(SaleorderBVO saleexecute, PreparedStatement stmt)
    throws SQLException, nc.bs.pub.SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
    try
    {
      if (saleexecute.getNassistcurdiscountmny() == null)
        stmt.setNull(1, 4);
      else {
        stmt.setBigDecimal(1, saleexecute.getNassistcurdiscountmny().toBigDecimal());
      }

      if (saleexecute.getNassistcursummny() == null)
        stmt.setNull(2, 4);
      else {
        stmt.setBigDecimal(2, saleexecute.getNassistcursummny().toBigDecimal());
      }

      if (saleexecute.getNassistcurmny() == null)
        stmt.setNull(3, 4);
      else {
        stmt.setBigDecimal(3, saleexecute.getNassistcurmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxmny() == null)
        stmt.setNull(4, 4);
      else {
        stmt.setBigDecimal(4, saleexecute.getNassistcurtaxmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxnetprice() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setBigDecimal(5, saleexecute.getNassistcurtaxnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurnetprice() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, saleexecute.getNassistcurnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxprice() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleexecute.getNassistcurtaxprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurprice() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, saleexecute.getNassistcurprice().toBigDecimal());
      }

      if (saleexecute.getCprojectid3() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleexecute.getCprojectid3());
      }
      if (saleexecute.getCprojectphaseid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleexecute.getCprojectphaseid());
      }
      if (saleexecute.getCprojectid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleexecute.getCprojectid());
      }
      if (saleexecute.getVfree5() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleexecute.getVfree5());
      }
      if (saleexecute.getVfree4() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleexecute.getVfree4());
      }
      if (saleexecute.getVfree3() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleexecute.getVfree3());
      }
      if (saleexecute.getVfree2() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleexecute.getVfree2());
      }
      if (saleexecute.getVfree1() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleexecute.getVfree1());
      }
      if (saleexecute.getVdef6() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleexecute.getVdef6());
      }
      if (saleexecute.getVdef5() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleexecute.getVdef5());
      }
      if (saleexecute.getVdef4() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleexecute.getVdef4());
      }
      if (saleexecute.getVdef3() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, saleexecute.getVdef3());
      }
      if (saleexecute.getVdef2() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, saleexecute.getVdef2());
      }
      if (saleexecute.getVdef1() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleexecute.getVdef1());
      }
      if (saleexecute.getTs() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleexecute.getTs().toString());
      }
      SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();

      setPreStatementOrdE(stmt, 24, saleexecute);

      stmt.setString(24 + (addfields == null ? 0 : addfields.length), saleexecute.getPrimaryKey());

      executeUpdate(stmt);
    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
  }

  public void updateHead(SaleorderHVO saleHeader)
    throws SQLException, nc.bs.pub.SystemException
  {
    SOField[] addfields = SaleorderHVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getDatabasename());
          addfieldsql.append(" = ?");
        }
      }
    }

    String sql = "update so_sale set pk_corp = ?, vreceiptcode = ?, creceipttype = ?, cbiztype = ?, finvoiceclass = ?, finvoicetype = ?, vaccountyear = ?, binitflag = ?, dbilldate = ?, ccustomerid = ?, cdeptid = ?, cemployeeid = ?, coperatorid = ?, ctermprotocolid = ?, csalecorpid = ?, creceiptcustomerid = ?, vreceiveaddress = ?, creceiptcorpid = ?, ctransmodeid = ?, ndiscountrate = ?, cwarehouseid = ?, veditreason = ?, bfreecustflag = ?, cfreecustid = ?, ibalanceflag = ?, nsubscription = ?, ccreditnum = ?, nevaluatecarriage = ?, dmakedate = ?, capproveid = ?, dapprovedate = ?, fstatus = ?, vnote = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?,ccalbodyid = ?,bretinvflag = ?,boutendflag = ?,binvoicendflag = ?,breceiptendflag = ?,npreceiverate = ?,npreceivemny = ?,ts = ?,nheadsummny = ? " + addfieldsql.toString() + " where csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      ((CrossDBConnection)con).setAddTimeStamp(false);
      stmt = con.prepareStatement(sql);

      if (saleHeader.getPk_corp() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleHeader.getPk_corp());
      }
      if (saleHeader.getVreceiptcode() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleHeader.getVreceiptcode());
      }
      if (saleHeader.getCreceipttype() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleHeader.getCreceipttype());
      }
      if (saleHeader.getCbiztype() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleHeader.getCbiztype());
      }
      if (saleHeader.getFinvoiceclass() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setInt(5, saleHeader.getFinvoiceclass().intValue());
      }
      if (saleHeader.getFinvoicetype() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setInt(6, saleHeader.getFinvoicetype().intValue());
      }
      if (saleHeader.getVaccountyear() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, saleHeader.getVaccountyear());
      }
      if (saleHeader.getBinitflag() == null)
        stmt.setString(8, "N");
      else {
        stmt.setString(8, saleHeader.getBinitflag().toString());
      }
      if (saleHeader.getDbilldate() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleHeader.getDbilldate().toString());
      }
      if (saleHeader.getCcustomerid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleHeader.getCcustomerid());
      }
      if (saleHeader.getCdeptid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleHeader.getCdeptid());
      }
      if (saleHeader.getCemployeeid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleHeader.getCemployeeid());
      }
      if (saleHeader.getCoperatorid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleHeader.getCoperatorid());
      }
      if (saleHeader.getCtermprotocolid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleHeader.getCtermprotocolid());
      }
      if (saleHeader.getCsalecorpid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleHeader.getCsalecorpid());
      }
      if (saleHeader.getCreceiptcustomerid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleHeader.getCreceiptcustomerid());
      }
      if (saleHeader.getVreceiveaddress() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleHeader.getVreceiveaddress());
      }
      if (saleHeader.getCreceiptcorpid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleHeader.getCreceiptcorpid());
      }
      if (saleHeader.getCtransmodeid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleHeader.getCtransmodeid());
      }
      if (saleHeader.getNdiscountrate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleHeader.getNdiscountrate().toBigDecimal());
      }

      if (saleHeader.getCwarehouseid() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, saleHeader.getCwarehouseid());
      }
      if (saleHeader.getVeditreason() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleHeader.getVeditreason());
      }
      if (saleHeader.getBfreecustflag() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleHeader.getBfreecustflag().toString());
      }
      if (saleHeader.getCfreecustid() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, saleHeader.getCfreecustid());
      }
      if (saleHeader.getIbalanceflag() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setInt(25, saleHeader.getIbalanceflag().intValue());
      }
      if (saleHeader.getNsubscription() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleHeader.getNsubscription().toBigDecimal());
      }

      if (saleHeader.getCcreditnum() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, saleHeader.getCcreditnum());
      }
      if (saleHeader.getNevaluatecarriage() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleHeader.getNevaluatecarriage().toBigDecimal());
      }

      if (saleHeader.getDmakedate() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, saleHeader.getDmakedate().toString());
      }
      if (saleHeader.getCapproveid() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, saleHeader.getCapproveid());
      }
      if (saleHeader.getDapprovedate() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, saleHeader.getDapprovedate().toString());
      }
      if (saleHeader.getFstatus() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setInt(32, saleHeader.getFstatus().intValue());
      }
      if (saleHeader.getVnote() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, saleHeader.getVnote());
      }
      if (saleHeader.getVdef1() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, saleHeader.getVdef1());
      }
      if (saleHeader.getVdef2() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, saleHeader.getVdef2());
      }
      if (saleHeader.getVdef3() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, saleHeader.getVdef3());
      }
      if (saleHeader.getVdef4() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, saleHeader.getVdef4());
      }
      if (saleHeader.getVdef5() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, saleHeader.getVdef5());
      }
      if (saleHeader.getVdef6() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, saleHeader.getVdef6());
      }
      if (saleHeader.getVdef7() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleHeader.getVdef7());
      }
      if (saleHeader.getVdef8() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleHeader.getVdef8());
      }
      if (saleHeader.getVdef9() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleHeader.getVdef9());
      }
      if (saleHeader.getVdef10() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleHeader.getVdef10());
      }

      if (saleHeader.getCcalbodyid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleHeader.getCcalbodyid());
      }
      if (saleHeader.getBretinvflag() == null)
        stmt.setString(45, "N");
      else {
        stmt.setString(45, saleHeader.getBretinvflag().toString());
      }
      if (saleHeader.getBoutendflag() == null)
        stmt.setString(46, "N");
      else {
        stmt.setString(46, saleHeader.getBoutendflag().toString());
      }
      if (saleHeader.getBinvoicendflag() == null)
        stmt.setString(47, "N");
      else {
        stmt.setString(47, saleHeader.getBinvoicendflag().toString());
      }
      if (saleHeader.getBreceiptendflag() == null)
        stmt.setString(48, "N");
      else {
        stmt.setString(48, saleHeader.getBreceiptendflag().toString());
      }

      if (saleHeader.getNpreceiverate() == null)
        stmt.setNull(49, 4);
      else {
        stmt.setBigDecimal(49, saleHeader.getNpreceiverate().toBigDecimal());
      }

      if (saleHeader.getNpreceivemny() == null)
        stmt.setNull(50, 4);
      else {
        stmt.setBigDecimal(50, saleHeader.getNpreceivemny().toBigDecimal());
      }

      if (saleHeader.getTs() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleHeader.getTs().toString());
      }

      if (saleHeader.getAttributeValue("nheadsummny") == null)
        stmt.setNull(52, 4);
      else {
        stmt.setBigDecimal(52, ((UFDouble)saleHeader.getAttributeValue("nheadsummny")).toBigDecimal());
      }

      setPreStatementOrdH(stmt, 53, saleHeader);

      stmt.setString(53 + (addfields == null ? 0 : addfields.length), saleHeader.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
      try {
        if (con != null) {
          ((CrossDBConnection)con).setAddTimeStamp(true);
          con.close();
        }
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
      }
    }
  }

  public void updateLock(String id, String cfreezid)
    throws SQLException
  {
    String sql = "update so_saleorder_b set cfreezeid = '" + cfreezid + "' where corder_bid = '" + id + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
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
  }

  public void updateOrderEnd(SaleorderBVO[] saleexecute)
    throws BusinessException
  {
    if ((saleexecute == null) || (saleexecute.length <= 0)) {
      return;
    }
    String sCuser = saleexecute[0].getCoperatorid();
    if ((sCuser == null) || (sCuser.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000013"));
    }

    ArrayList lockpks = new ArrayList();
    int i = 0; for (int loop = saleexecute.length; i < loop; i++) {
      if (!lockpks.contains(saleexecute[i].getCsaleid())) {
        lockpks.add(saleexecute[i].getCsaleid());
      }
      if (!lockpks.contains(saleexecute[i].getCorder_bid())) {
        lockpks.add(saleexecute[i].getCorder_bid());
      }
    }
    Connection con = null;
    PreparedStatement stmt = null;
    PKLock lock = null;
    boolean islock = false;
    SaleorderHVO[] oldordhvos = null;
    SaleorderBVO[] oldordbvos = null;
    try
    {
      lock = PKLock.getInstance();
      islock = lock.acquireBatchLock((String[])(String[])lockpks.toArray(new String[0]), sCuser, "");

      if (!islock) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000014"));
      }

      String[] bids = new String[saleexecute.length];
      String[] csaleids = new String[saleexecute.length];
      for (  i = 0; i < saleexecute.length; i++) {
        bids[i] = saleexecute[i].getPrimaryKey();
        csaleids[i] = saleexecute[i].getCsaleid();
      }
      Hashtable hsexets = getAnyValue("so_saleexecute", "ts", "csale_bid", bids);

      Hashtable hsbts = getAnyValue("so_saleorder_b", "ts", "corder_bid", bids);

      Hashtable hsbinvs = getAnyValue("so_saleorder_b", "cinventoryid", "corder_bid", bids);

      for (  i = 0; i < saleexecute.length; i++) {
        String bts = saleexecute[i].m_ts == null ? "" : saleexecute[i].m_ts.toString();

        String exets = saleexecute[i].m_exets == null ? "" : saleexecute[i].m_exets.toString();

        if ((hsexets != null) && 
          (!exets.equals(hsexets.get(saleexecute[i].getPrimaryKey()))))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000015"));
        }

        if ((hsbts != null) && 
          (!bts.equals(hsbts.get(saleexecute[i].getPrimaryKey())))) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000015"));
        }

        saleexecute[i].setCinventoryid((String)hsbinvs.get(saleexecute[i].getPrimaryKey()));
      }

      Hashtable hshts = getAnyValue("so_sale", "ts", "csaleid", csaleids);
      for (  i = 0; i < saleexecute.length; i++) {
        String hts = saleexecute[i].m_headts == null ? "" : saleexecute[i].m_headts.toString();

        if ((hshts == null) || 
          (hts.equals(hshts.get(saleexecute[i].getCsaleid())))) continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000015"));
      }

      delDelivdayplWhenClose(saleexecute, sCuser);

      oldordbvos = (SaleorderBVO[])(SaleorderBVO[])queryAllBodyDataByBIDs(SoVoTools.getVOsOnlyValues(saleexecute, "corder_bid"));

      if ((oldordbvos == null) || (oldordbvos.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000016"));
      }

      oldordhvos = (SaleorderHVO[])(SaleorderHVO[])queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(saleexecute, "csaleid"));

      if ((oldordhvos == null) || (oldordhvos.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000017"));
      }

      con = getConnection();
      String sql = " update so_saleexecute set bifinvoicefinish = ?, bifreceiptfinish = ?, bifinventoryfinish = ? where csale_bid = ? and csaleid = ?";
      stmt = prepareStatement(con, sql);
      updateOrderEndExeStatus(saleexecute, stmt);
      executeBatch(stmt);
      stmt.close();
      sql = "update so_saleorder_b set frowstatus = ? where csaleid = ? and corder_bid = ? ";
      stmt = prepareStatement(con, sql);
      updateOrderEndDetailStatus(saleexecute, stmt);
      executeBatch(stmt);
      stmt.close();
      con.close();

      SaleorderBVO[] curordbvos = (SaleorderBVO[])(SaleorderBVO[])queryAllBodyDataByBIDs(SoVoTools.getVOsOnlyValues(saleexecute, "corder_bid"));

      if ((oldordbvos == null) || (oldordbvos.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000016"));
      }

      setOrdHeadStatus(oldordhvos);

      updateOrdBalanceByOrdRows(oldordhvos, oldordbvos, curordbvos);

      updateAdjustForEnd(oldordhvos, oldordbvos, curordbvos);

      updateAtpByOrdRows(oldordhvos, oldordbvos, curordbvos);

      updateArByOrdRows(oldordhvos, oldordbvos, curordbvos);

      if (saleexecute.length > 0) {
        IICToSO iictoso = (IICToSO)NCLocator.getInstance().lookup(IICToSO.class.getName());

        String userid = saleexecute[0].getClientLink().getUser();
        UFDate logindate = saleexecute[0].getClientLink().getLogonDate();

          i = 0; for (int len = saleexecute.length; i < len; i++) {
          iictoso.unLockInv(findHeadBillType(saleexecute[i].getCsaleid(), oldordhvos), new String[] { saleexecute[i].getCorder_bid() }, userid, logindate);
        }

      }

      if ((oldordhvos == null) || (oldordbvos == null) || (oldordhvos.length <= 0) || (oldordbvos.length <= 0));
    }
    catch (Exception e)
    {
      if ((oldordhvos != null) && (oldordbvos != null) && (oldordhvos.length > 0) && (oldordbvos.length > 0))
      {
        ArrayList rowlist = new ArrayList();
          i = 0; int loop = oldordbvos == null ? 0 : oldordbvos.length;
        for (; i < loop; i++) {
          rowlist.add(oldordbvos[i]);
        }

        DataControlDMO datactldmo = null;
        try {
          datactldmo = new DataControlDMO();
        } catch (Exception exp) {
          if ((exp instanceof BusinessException)) {
            throw ((BusinessException)exp);
          }
          exp.printStackTrace();
          throw new BusinessRuntimeException(exp.getMessage());
        }

        SaleOrderVO[] ordvos = toSaleOrderVO(oldordhvos, rowlist);
        OperatelogVO[] logvos = new OperatelogVO[ordvos.length];

        String sbtnname = NCLangResOnserver.getInstance().getStrByID("common", "UC001-0000001");

        String snodename = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000300");

        String smsg = e.getMessage();
          i = 0; for (  loop = ordvos.length; i < loop; i++)
        {
          logvos[i] = datactldmo.getOperLogVO(smsg, sbtnname, "WARNNING", sCuser, null, ordvos[i]);

          logvos[i].setEnterfunction(snodename);
          logvos[i].setEnterbutton(sbtnname);
        }

        datactldmo.insertBusinesslog(logvos);
      }

      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
    finally
    {
      try {
        if ((lock != null) && 
          (islock)) {
          lock.releaseBatchLock((String[])(String[])lockpks.toArray(new String[0]), sCuser, "");
        }

        if (stmt != null) {
          stmt.close();
        }
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private String findHeadBillType(String csaleid, SaleorderHVO[] heads)
  {
    for (SaleorderHVO head : heads) {
      if (head.getCsaleid().equals(csaleid)) {
        return head.getCreceipttype();
      }
    }

    return null;
  }

  public void updateOrderEnd(SaleOrderVO vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getParentVO() == null)) {
      return;
    }
    Connection con = null;

    PreparedStatement stmt = null;
    try
    {
      BusinessControlDMO busidmo = new BusinessControlDMO();

      String csaleid = ((SaleorderHVO)vo.getParentVO()).getCsaleid();
      String cbilltype = ((SaleorderHVO)vo.getParentVO()).getCreceipttype();

      busidmo.setBillFinish(csaleid, cbilltype);

      con = getConnection();
      String sql = " update so_saleexecute set bifinvoicefinish = 'Y', bifreceiptfinish = 'Y', bifinventoryfinish = 'Y', bifpayfinish = 'Y' where csaleid = ? ";

      stmt = con.prepareStatement(sql);
      stmt.setString(1, csaleid);
      stmt.executeUpdate();
      stmt.close();

      con.close();
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
    finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private int updateOrderEndDetailStatus(SaleorderBVO[] saleexecute, PreparedStatement stmt)
    throws BusinessException, nc.bs.pub.SystemException, NamingException, SQLException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderDetailStatus", new Object[] { saleexecute });

    if ((saleexecute == null) || (saleexecute.length <= 0)) {
      throw new BusinessException("SaleorderBVO[] is null ");
    }
    ArrayList updataline = new ArrayList();
    for (int i = 0; i < saleexecute.length; i++) {
      if ((saleexecute[i].getBifinventoryfinish().booleanValue()) && (saleexecute[i].getBifreceiptfinish().booleanValue()) && (saleexecute[i].getBifinvoicefinish().booleanValue()))
      {
        updataline.add(saleexecute[i]);
      } else {
        if ((saleexecute[i].getBifinventoryfinish().booleanValue()) && (saleexecute[i].getBifreceiptfinish().booleanValue()) && (saleexecute[i].getBifinvoicefinish().booleanValue()))
        {
          continue;
        }

        updataline.add(saleexecute[i]);
      }
    }

    if (updataline.size() <= 0) {
      return 0;
    }
    if (stmt == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000019"));
    }

    int updatelinecount = 0;
    try
    {
      for (int i = 0; i < updataline.size(); i++)
      {
        SaleorderBVO updateli = (SaleorderBVO)updataline.get(i);

        if ((updateli.getBifinventoryfinish().booleanValue()) && (updateli.getBifreceiptfinish().booleanValue()) && (updateli.getBifinvoicefinish().booleanValue()))
        {
          updateli.setStatus(6);
          stmt.setInt(1, 6);
        }
        else if ((!updateli.getBifinventoryfinish().booleanValue()) || (!updateli.getBifreceiptfinish().booleanValue()) || (!updateli.getBifinvoicefinish().booleanValue()))
        {
          updateli.setStatus(2);
          stmt.setInt(1, 2);
        }

        stmt.setString(2, updateli.getCsaleid());

        stmt.setString(3, updateli.getPrimaryKey());

        executeUpdate(stmt);
        updatelinecount++;
      }

    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderDetailStatus", new Object[] { saleexecute });

    return updatelinecount;
  }

  private int updateOrderEndExeStatus(SaleorderBVO[] saleexecute, PreparedStatement stmt)
    throws BusinessException, nc.bs.pub.SystemException, NamingException, SQLException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderExeStatus", new Object[] { saleexecute });

    if ((saleexecute == null) || (saleexecute.length <= 0)) {
      throw new BusinessException(" SaleorderBVO[] is null ");
    }
    if (stmt == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000019"));
    }

    int updatelinecount = 0;
    try
    {
      for (int i = 0; i < saleexecute.length; i++)
      {
        if (saleexecute[i].getBifinvoicefinish() == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, saleexecute[i].getBifinvoicefinish().toString());
        }

        if (saleexecute[i].getBifreceiptfinish() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, saleexecute[i].getBifreceiptfinish().toString());
        }

        if (saleexecute[i].getBifinventoryfinish() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, saleexecute[i].getBifinventoryfinish().toString());
        }

        stmt.setString(4, saleexecute[i].getPrimaryKey());

        stmt.setString(5, saleexecute[i].getCsaleid());

        executeUpdate(stmt);
        updatelinecount++;
      }

    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderExeStatus", new Object[] { saleexecute });

    return updatelinecount;
  }

  public void updateRetinvFlag(String id)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "updateRetinvFlag", new Object[] { id });

    String sql = "update so_sale set bretinvflag = 'Y' where csaleid = '" + id + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "updateRetinvFlag", new Object[] { id });
  }

  public void updateStatus(SaleorderHVO saleHeader)
    throws SQLException
  {
    String sql = "update so_sale set fstatus = ? where csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (saleHeader.getFstatus() == null)
        stmt.setNull(1, 4);
      else {
        stmt.setInt(1, saleHeader.getFstatus().intValue());
      }

      stmt.setString(2, saleHeader.getPrimaryKey());

      stmt.executeUpdate();
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
  }

  public void approvedOrdByID(String ordid)
    throws Exception
  {
    if (ordid == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000022"));
    }

    SaleOrderVO ordvo = queryData(ordid);
    if (ordvo == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000023"));
    }

    SaleorderHVO ordhvo = (SaleorderHVO)ordvo.getParentVO();
    ordhvo.setCapproveid(ordhvo.getCoperatorid());
    ordhvo.setDapprovedate(ordhvo.getDmakedate());

    PfUtilBO pfbo = null;
    try
    {
      pfbo = new PfUtilBO();
      pfbo.processAction("APPROVE", "30", ordhvo.getDmakedate().toString(), null, ordvo, null);
    }
    finally
    {
    }
  }

  public void checkVOForPreOrd(AggregatedValueObject vo)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null)
      return;
    if (!(vo instanceof SaleOrderVO)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000028"));
    }

    SaleOrderVO inordvo = (SaleOrderVO)vo;

    inordvo.setStatus(2);

    inordvo.setIAction(0);

    SaleorderHVO ordhvo = (SaleorderHVO)vo.getParentVO();
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])vo.getChildrenVO();

    ArrayList othervolist = new ArrayList();
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if ((ordbvos[i].getCconsigncorpid() == null) || (ordbvos[i].getCconsigncorpid().trim().length() <= 0))
        continue;
      if (!ordbvos[i].getCconsigncorpid().equals(ordhvo.getPk_corp())) {
        othervolist.add(ordbvos[i]);
      }
    }
    SaleorderBVO[] otherordbvos = null;
    if (othervolist.size() > 0) {
      otherordbvos = (SaleorderBVO[])(SaleorderBVO[])othervolist.toArray(new SaleorderBVO[othervolist.size()]);
    }

    if ((ordbvos == null) || (ordbvos.length <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000029"));
    }

    try
    {
      FetchValueDMO fetdmo = new FetchValueDMO();

      if (otherordbvos != null) {
        String crevcalbodyid = null;
        String defaultcalbodyid = null;

        defaultcalbodyid = fetdmo.getColValue("bd_calbody", "pk_calbody", " pk_corp='" + ordhvo.getPk_corp() + "' and ( sealflag is null or sealflag = 'N' ) and ( property = 0 or property = 1) ");

        if ((defaultcalbodyid == null) || (defaultcalbodyid.trim().length() <= 0))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000030"));
        }

        HashMap revcalbodyhs = SOToolsDMO.getAnyValue("bd_produce", "pk_calbody", "pk_invmandoc", SoVoTools.getVOsOnlyValues(otherordbvos, "cinventoryid"), null);

          i = 0; for (int loop = otherordbvos.length; i < loop; i++) {
          if (revcalbodyhs != null) {
            crevcalbodyid = (String)revcalbodyhs.get(otherordbvos[i].getCinventoryid());
          }

          if ((crevcalbodyid != null) && (crevcalbodyid.trim().length() > 0))
          {
            otherordbvos[i].setCreccalbodyid(crevcalbodyid);
          }
          else otherordbvos[i].setCreccalbodyid(defaultcalbodyid);

        }

      }

      ordhvo.setFstatus(new Integer(1));
      ordhvo.setBinitflag(new UFBoolean(false));
      ordhvo.setBretinvflag(new UFBoolean(false));
      ordhvo.setBinvoicendflag(new UFBoolean(false));
      ordhvo.setBoutendflag(new UFBoolean(false));
      ordhvo.setBpayendflag(new UFBoolean(false));
      ordhvo.setBreceiptendflag(new UFBoolean(false));
      ordhvo.setBtransendflag(new UFBoolean(false));

      if (ordhvo.getCcalbodyid() == null) {
        ordhvo.setCcalbodyid(ordbvos[0].getCadvisecalbodyid());
      }
      if (ordhvo.getPk_corp() == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000031"));
      }

      ordhvo.setStatus(2);
      ordhvo.setFstatus(new Integer(1));
      ordhvo.setBretinvflag(new UFBoolean(false));

      processSaleOrderVO(inordvo);

      UFDate dbilldate = ordhvo.getDbilldate();

      String creceiptcorpid = ordhvo.getCreceiptcorpid();
      String vreceiveaddress = ordhvo.getVreceiveaddress();

      ordhvo.setCapproveid(null);

      String[] deskeys = { "cquoteunitid", "nquoteunitnum", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc" };

      String[] sourcekeys = { "cunitid", "nnumber", "noriginalcurtaxprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurnetprice", "ntaxnetprice", "nnetprice", "ntaxprice", "nprice" };

      String ccalbodyid_h = null;

        i = 0; for (int loop = ordbvos.length; i < loop; i++)
      {
        if ((ordbvos[i].getNorgqtprc() == null) && (ordbvos[i].getNorgqttaxprc() == null) && (ordbvos[i].getNquoteunitnum() == null))
        {
          SoVoTools.copyVOByVO(ordbvos[i], deskeys, ordbvos[i], sourcekeys);
        }

        if (ordbvos[i].getNitemdiscountrate() == null)
          ordbvos[i].setNitemdiscountrate(new UFDouble(100));
        if (ordbvos[i].getNdiscountrate() == null) {
          ordbvos[i].setNdiscountrate(new UFDouble(100));
        }
        if (ordbvos[i].getDconsigndate() == null) {
          ordbvos[i].setDconsigndate(dbilldate);
        }
        if (ordbvos[i].getDdeliverdate() == null) {
          ordbvos[i].setDdeliverdate(dbilldate);
        }
        if (ordbvos[i].getPkcorp() == null) {
          ordbvos[i].setPkcorp(ordhvo.getPk_corp());
        }

        if (ordbvos[i].getDiscountflag() == null)
          ordbvos[i].setDiscountflag(new UFBoolean(false));
        if (ordbvos[i].getLaborflag() == null) {
          ordbvos[i].setLaborflag(new UFBoolean(false));
        }
        if (ordbvos[i].getIsappendant() == null) {
          ordbvos[i].setIsappendant(new UFBoolean(false));
        }
        if (ordbvos[i].getBoosflag() == null)
          ordbvos[i].setBoosflag(new UFBoolean(false));
        if (ordbvos[i].getBsupplyflag() == null) {
          ordbvos[i].setBoosflag(new UFBoolean(false));
        }
        ordbvos[i].setBeditflag(new UFBoolean(false));
        ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
        ordbvos[i].setBifpaybalance(new UFBoolean(false));
        ordbvos[i].setBifpayfinish(new UFBoolean(false));
        ordbvos[i].setBifpaysign(new UFBoolean(false));
        ordbvos[i].setBifreceiptfinish(new UFBoolean(false));
        ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
        ordbvos[i].setBiftransfinish(new UFBoolean(false));

        ordbvos[i].setFrowstatus(new Integer(1));
        ordbvos[i].setStatus(2);

        if (ordbvos[i].getCreceiptcorpid() == null) {
          ordbvos[i].setCreceiptcorpid(creceiptcorpid);
        }
        if (ordbvos[i].getVreceiveaddress() == null) {
          ordbvos[i].setVreceiveaddress(vreceiveaddress);
        }

        UFDate dconsigndate = ordbvos[i].getDconsigndate();
        UFDate deliverdate = ordbvos[i].getDdeliverdate();
        if ((dconsigndate == null) || (dconsigndate.toString().length() == 0) || (deliverdate == null) || (deliverdate.toString().length() == 0))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000032"));
        }

        if ((dconsigndate != null) && (dbilldate != null) && 
          (dbilldate.after(dconsigndate)) && (dbilldate != dconsigndate))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000033"));
        }

        if ((dconsigndate != null) && (deliverdate != null) && 
          (dconsigndate.after(deliverdate)) && (deliverdate != dconsigndate))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000034"));
        }

        if (ccalbodyid_h == null) {
          if (!SoVoTools.isEmptyString(ordbvos[i].getCreccalbodyid())) {
            ccalbodyid_h = ordbvos[i].getCreccalbodyid();
          } else {
            if (SoVoTools.isEmptyString(ordbvos[i].getCadvisecalbodyid()))
              continue;
            ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
          }
        }

      }

      String oldcalbodyid = ordhvo.getCcalbodyid();
      ordhvo.setCcalbodyid(ccalbodyid_h);
      if (SoVoTools.isEmptyString(ordhvo.getCcalbodyid())) {
        ordhvo.setCcalbodyid(oldcalbodyid);
      }

      ordhvo.setNdiscountrate(ordbvos[0].getNdiscountrate());
      if (ordhvo.getNdiscountrate() == null) {
        ordhvo.setNdiscountrate(new UFDouble(100));
      }

      checkSaleOrderVO(inordvo);
    }
    catch (BusinessException e)
    {
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public void PushSave5D(AggregatedValueObject ordvo, Object clientojb, String curdate)
    throws RemoteException, NamingException, CreateException, SQLException, BusinessException
  {
    if (ordvo == null)
      return;
    SaleOrderVO[] otherOrdVos = ((SaleOrderVO)ordvo).getOrdVOsOfOtherCorpFor5D();

    if ((otherOrdVos == null) || (otherOrdVos.length <= 0)) {
      return;
    }

    HashMap hscalbody = null;

    HashMap hswareaddr = null;

    BillVO voBilltep = null;

    PfUtilBO pfbo = null;
    try
    {
      AggregatedValueObject[] vos5d = PfUtilTools.runChangeDataAry("30", "5D", otherOrdVos);

      if ((vos5d == null) || (vos5d.length <= 0))
        return;
      ArrayList ojblist = new ArrayList();
      ojblist.add(null);
      ojblist.add(((SaleOrderVO)ordvo).getClientLink());

      String[] addrs = null;
      String[] wareaddrs = null;

      pfbo = new PfUtilBO();

      Object[] userobjarr = new Object[vos5d.length];

      HashMap hsware = null;

      int i = 0; for (int loop = vos5d.length; i < loop; i++)
      {
        userobjarr[i] = ojblist;

        if ((vos5d[i] == null) || (vos5d[i].getParentVO() == null))
        {
          return;
        }
        if ((((SaleorderBVO)otherOrdVos[i].getChildrenVO()[0]).getBdericttrans() != null) && (((SaleorderBVO)otherOrdVos[i].getChildrenVO()[0]).getBdericttrans().booleanValue()))
        {
          if (hsware == null) {
            hsware = SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] { "pk_stordoc" }, "pk_calbody", SoVoTools.getVOsOnlyValues(ordvo.getChildrenVO(), "creccalbodyid"), " isdirectstore='Y' ");

            if (hsware == null) {
              hsware = new HashMap();
            }
          }

          int m = 0; for (int loopm = vos5d[i].getChildrenVO().length; m < loopm; m++)
          {
            addrs = (String[])(String[])hsware.get(otherOrdVos[i].getBodyVOs()[m].getCreccalbodyid() + "");

            if (addrs == null)
              continue;
            vos5d[i].getChildrenVO()[m].setAttributeValue("cinwhid", addrs[0]);
          }

          vos5d[i].getParentVO().setAttributeValue("fallocflag", new Integer(0));
        }
        else
        {
          if (hscalbody == null) {
            hscalbody = SOToolsDMO.getAnyValueArray("bd_calbody", new String[] { "pk_areacl", "area", "pk_address" }, "pk_calbody", SoVoTools.getVOsOnlyValues(ordvo.getChildrenVO(), "creccalbodyid"), null);

            if (hscalbody == null) {
              hscalbody = new HashMap();
            }
          }

          if (hswareaddr == null) {
            hswareaddr = SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] { "pk_calbody", "storaddr", "pk_address" }, "pk_stordoc", SoVoTools.getVOsOnlyValues(ordvo.getChildrenVO(), "crecwareid"), null);

            if (hswareaddr == null) {
              hswareaddr = new HashMap();
            }
          }

          int m = 0; for (int loopm = vos5d[i].getChildrenVO().length; m < loopm; m++)
          {
            vos5d[i].getChildrenVO()[m].setAttributeValue("crowno", "" + (m + 1));

            vos5d[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

            addrs = (String[])(String[])hscalbody.get(otherOrdVos[i].getBodyVOs()[m].getCreccalbodyid() + "");

            wareaddrs = (String[])(String[])hswareaddr.get(otherOrdVos[i].getBodyVOs()[m].getCrecwareid() + "");

            if (wareaddrs == null)
            {
              if (addrs == null)
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

                vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);

                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);
              }
              else
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

                vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
              }

            }
            else if (addrs == null)
            {
              vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

              vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);

              vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
            }
            else
            {
              vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

              if ((wareaddrs[1] != null) && (wareaddrs[1].trim().length() > 0))
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
              }
              else
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
              }

              if ((wareaddrs[2] != null) && (wareaddrs[2].trim().length() > 0))
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
              }
              else
              {
                vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
              }

            }

          }

          vos5d[i].getParentVO().setAttributeValue("fallocflag", new Integer(1));
        }

        voBilltep = (BillVO)vos5d[i];
        voBilltep.setOperator(otherOrdVos[i].getParentVO().getAttributeValue("capproveid").toString());
      }

      pfbo.processBatch("PUSHSAVE", "5D", curdate, vos5d, userobjarr, null);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      if ((BillTools.marshException(e) instanceof ATPNotEnoughException))
        throw new BusinessException(e);
      throw new BusinessException(e.getMessage());
    }
    finally
    {
    }
  }

  public void renovateAR(IBillInvokeCreditManager armanager, AggregatedValueObject ordvo)
    throws BusinessException
  {
    SaleOrderVO saleorder = (SaleOrderVO)ordvo;

    if ((saleorder == null) || (armanager == null)) {
      return;
    }
    BillCreditOriginVO billcreditvo = null;
    UFDate date = ((SaleorderHVO)saleorder.getParentVO()).getDmakedate();

    if ((saleorder.getIAction() == 0) || (saleorder.getIAction() == 10))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 0;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 1)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 1;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());
    }
    else if (saleorder.getIAction() == 5)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 14;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());

      SaleOrderVO curordvo = saleorder.getAllSaleOrderVO();

      SaleOrderVO oldordvo = saleorder.getOldSaleOrderVO();

      if ((oldordvo != null) && (curordvo != null))
      {
        SaleorderBVO[] bvos = curordvo.getBodyVOs();

        if ((bvos != null) && (bvos.length > 0))
        {
          SaleorderBVO[] oldbvos = oldordvo.getBodyVOs();

          HashMap hsoldbvos = new HashMap();
          int i = 0; for (int loop = oldbvos.length; i < loop; i++) {
            hsoldbvos.put(oldbvos[i].getCorder_bid(), oldbvos[i]);
          }

          SaleorderBVO oldbvo = null;

          UFDouble ufzero = new UFDouble(0);

          UFDouble tempmny1 = null;

          UFDouble temppricediff = null;

            i = 0; for (int loop = bvos.length; i < loop; i++)
          {
            oldbvo = (SaleorderBVO)hsoldbvos.get(bvos[i].getCorder_bid());

            if (oldbvo == null) {
              continue;
            }
            bvos[i].setBifinventoryfinish_init(oldbvo.getBifinventoryfinish());

            temppricediff = SoVoTools.sub(bvos[i].getNtaxnetprice(), oldbvo.getNtaxnetprice());

            tempmny1 = temppricediff == null ? ufzero : temppricediff;

            tempmny1 = tempmny1.multiply(bvos[i].getNtotalinventorynumber() == null ? ufzero : bvos[i].getNtotalinventorynumber());

            bvos[i].setNoutmny_diff(SoVoTools.setUFDoubleDigitByUFDouble(tempmny1, bvos[i].getNsummny()));

            tempmny1 = temppricediff == null ? ufzero : temppricediff;

            tempmny1 = tempmny1.multiply(bvos[i].getNtotalbalancenumber() == null ? ufzero : bvos[i].getNtotalbalancenumber());

            bvos[i].setNbalmny_diff(SoVoTools.setUFDoubleDigitByUFDouble(tempmny1, bvos[i].getNsummny()));
          }

        }

      }

    }
    else if (saleorder.getIAction() == 9)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 6;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 6)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 5;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if ((saleorder.getIAction() == 2) || (saleorder.getIAction() == 11))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 2;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 12)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 9;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 14)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 10;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 17)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 12;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 18)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 0;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 13;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }

    if (billcreditvo == null) {
      return;
    }
    try
    {
      billcreditvo.sOperatorid = saleorder.getOperatorid();
      armanager.renovateAR(billcreditvo);
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public void setBillStatus(SaleorderHVO[] hvos, String statefield)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((hvos == null) || (hvos.length == 0) || (statefield == null)) {
      return;
    }
    if (statefield.equals("binvoicendflag")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifinvoicefinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

      UFDouble count = null;
      for (int i = 0; i < hvos.length; i++) {
        if (statehs == null) {
          hvos[i].setBinvoicendflag(new UFBoolean(true));
        } else {
          count = (UFDouble)statehs.get(hvos[i].getCsaleid());
          if ((count != null) && (count.doubleValue() > 0.0D))
            hvos[i].setBinvoicendflag(new UFBoolean(false));
          else {
            hvos[i].setBinvoicendflag(new UFBoolean(true));
          }
        }
      }
      SOToolsDMO.updateBatch(hvos, new String[] { "binvoicendflag" }, "so_sale", new String[] { "csaleid" });
    }
    else if (statefield.equals("breceiptendflag")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifreceiptfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

      UFDouble count = null;
      for (int i = 0; i < hvos.length; i++) {
        if (statehs == null) {
          hvos[i].setBreceiptendflag(new UFBoolean(true));
        } else {
          count = (UFDouble)statehs.get(hvos[i].getCsaleid());
          if ((count != null) && (count.doubleValue() > 0.0D))
            hvos[i].setBreceiptendflag(new UFBoolean(false));
          else {
            hvos[i].setBreceiptendflag(new UFBoolean(true));
          }
        }
      }
      SOToolsDMO.updateBatch(hvos, new String[] { "breceiptendflag" }, "so_sale", new String[] { "csaleid" });
    }
    else if (statefield.equals("boutendflag")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifinventoryfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

      UFDouble count = null;
      for (int i = 0; i < hvos.length; i++) {
        if (statehs == null) {
          hvos[i].setBoutendflag(new UFBoolean(true));
        } else {
          count = (UFDouble)statehs.get(hvos[i].getCsaleid());
          if ((count != null) && (count.doubleValue() > 0.0D))
            hvos[i].setBoutendflag(new UFBoolean(false));
          else {
            hvos[i].setBoutendflag(new UFBoolean(true));
          }
        }
      }
      SOToolsDMO.updateBatch(hvos, new String[] { "boutendflag" }, "so_sale", new String[] { "csaleid" });
    }
    else if (statefield.equals("btransendflag")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " biftransfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

      UFDouble count = null;
      for (int i = 0; i < hvos.length; i++) {
        if (statehs == null) {
          hvos[i].setBtransendflag(new UFBoolean(true));
        } else {
          count = (UFDouble)statehs.get(hvos[i].getCsaleid());
          if ((count != null) && (count.doubleValue() > 0.0D))
            hvos[i].setBtransendflag(new UFBoolean(false));
          else {
            hvos[i].setBtransendflag(new UFBoolean(true));
          }
        }
      }
      SOToolsDMO.updateBatch(hvos, new String[] { "btransendflag" }, "so_sale", new String[] { "csaleid" });
    }
    else if (statefield.equals("bpayendflag")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

      UFDouble count = null;
      for (int i = 0; i < hvos.length; i++) {
        if (statehs == null) {
          hvos[i].setBpayendflag(new UFBoolean(true));
        } else {
          count = (UFDouble)statehs.get(hvos[i].getCsaleid());
          if ((count != null) && (count.doubleValue() > 0.0D))
            hvos[i].setBpayendflag(new UFBoolean(false));
          else {
            hvos[i].setBpayendflag(new UFBoolean(true));
          }
        }
      }
      SOToolsDMO.updateBatch(hvos, new String[] { "bpayendflag" }, "so_sale", new String[] { "csaleid" });
    }
    else if (statefield.equals("fstatus")) {
      HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)", "csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " frowstatus = 6 and beditflag = 'N' group by csaleid ");

      HashMap allstatehs = SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)", "csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " beditflag = 'N' group by csaleid ");

      if (allstatehs == null) {
        return;
      }
      UFDouble allcount = null;
      ArrayList updatehvolist = new ArrayList();
      for (int i = 0; i < hvos.length; i++)
        if (statehs == null) {
          if (hvos[i].getFstatus().intValue() == 6) {
            hvos[i].setFstatus(new Integer(2));

            updatehvolist.add(hvos[i]);
          }
        }
        else {
          allcount = (UFDouble)allstatehs.get(hvos[i].getCsaleid());
          if (allcount.equals(statehs.get(hvos[i].getCsaleid()))) {
            hvos[i].setFstatus(new Integer(6));

            updatehvolist.add(hvos[i]);
          }
        }
      if (updatehvolist.size() > 0)
        SOToolsDMO.updateBatch((SaleorderHVO[])(SaleorderHVO[])updatehvolist.toArray(new SaleorderHVO[updatehvolist.size()]), new String[] { "fstatus" }, "so_sale", new String[] { "csaleid" });
    }
  }

  public void setPreOrdStatus(AggregatedValueObject vo)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null) {
      return;
    }
    if (!(vo instanceof SaleOrderVO)) {
      return;
    }
    SaleOrderVO ordvo = (SaleOrderVO)vo;

    if ((ordvo.getIAction() == 0) || (ordvo.getIAction() == 2))
    {
      ArrayList sourcebillids = new ArrayList();

      SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])vo.getChildrenVO();
      if ((ordbvos == null) || (ordbvos.length <= 0)) {
        return;
      }

      String stemp = null;
      HashMap hp = new HashMap();
      ArrayList al = null;
      int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        if (!"38".equals(ordbvos[i].getCreceipttype()))
          continue;
        stemp = ordbvos[i].getCsourcebillid();
        if ((stemp == null) || (stemp.length() <= 0)) {
          continue;
        }
        if (!sourcebillids.contains(stemp)) {
          sourcebillids.add(stemp);
          al = new ArrayList();
          al.add(ordbvos[i].getCsourcebillbodyid());
          hp.put(stemp, al);
        } else {
          al = (ArrayList)hp.get(stemp);
          al.add(ordbvos[i].getCsourcebillbodyid());
        }
      }

      PreorderDMO preorddmo = new PreorderDMO();
      try
      {
        String[] sbodyKeys = null;
          i = 0; for (int loop = sourcebillids.size(); i < loop; i++)
          if (ordvo.getIAction() == 0) {
            al = (ArrayList)hp.get(stemp);
            sbodyKeys = new String[al.size()];
            al.toArray(sbodyKeys);
            preorddmo.closeOnePreOrder((String)sourcebillids.get(i), sbodyKeys);
          }
          else {
            preorddmo.openOnePreOrder((String)sourcebillids.get(i));
          }
      }
      catch (Exception ex)
      {
        throw new RemoteException(ex.getMessage());
      }
    }
  }

  private void checkAllowDiffer(SaleorderBVO bvo, int i, UFBoolean SO23, UFDouble SO24, UFBoolean SO25, UFDouble SO26)
    throws BusinessException
  {
    if (SO23 == null)
      SO23 = getParaUFBooleanValue(bvo.getPkcorp(), "SO23", null);
    if (SO24 == null)
      SO24 = getParaUFDoubleValue(bvo.getPkcorp(), "SO24", null);
    if (SO25 == null)
      SO25 = getParaUFBooleanValue(bvo.getPkcorp(), "SO25", null);
    if (SO26 == null) {
      SO26 = getParaUFDoubleValue(bvo.getPkcorp(), "SO26", null);
    }
    UFDouble nnumber = bvo.getNnumber() == null ? new UFDouble(0) : bvo.getNnumber();

    boolean bisNeg = false;
    if (nnumber.doubleValue() < 0.0D) {
      bisNeg = true;
      nnumber = nnumber.multiply(-1.0D);
    }

    UFDouble ntotalreceiptnumber = bvo.getNtotalreceiptnumber() == null ? new UFDouble(0) : bvo.getNtotalreceiptnumber();

    UFDouble ntotalinvoicenumber = bvo.getNtotalinvoicenumber() == null ? new UFDouble(0) : bvo.getNtotalinvoicenumber();

    UFDouble ntotalinventorynumber = (bvo.getNtotalinventorynumber() == null ? new UFDouble(0) : bvo.getNtotalinventorynumber()).add(bvo.getNtotalshouldoutnum() == null ? new UFDouble(0) : bvo.getNtotalshouldoutnum());

    UFDouble ntranslossnum = bvo.getNtranslossnum() == null ? new UFDouble(0) : bvo.getNtranslossnum();

    ntotalinventorynumber = ntotalinventorynumber.sub(ntranslossnum);

    if (bisNeg) {
      ntotalreceiptnumber = ntotalreceiptnumber.multiply(-1.0D);
      ntotalinvoicenumber = ntotalinvoicenumber.multiply(-1.0D);
      ntotalinventorynumber = ntotalinventorynumber.multiply(-1.0D);
    }

    UFDouble postiveNum = null;

    String FHMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000524");

    String CKMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000526");

    String KPMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000525");

    ArrayList al = new ArrayList();

    UFDouble nouttoplimit = (UFDouble)bvo.getAttributeValue("nouttoplimit");

    if (nouttoplimit == null)
      nouttoplimit = SO24;
    if (SO23.booleanValue()) {
      postiveNum = nnumber.multiply(1.0D + nouttoplimit.doubleValue() / 100.0D);
    }
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalreceiptnumber) < 0) {
      al.add(FHMSG);
    }

    if (SO25.booleanValue())
      postiveNum = nnumber.multiply(1.0D + SO26.doubleValue() / 100.0D);
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalinvoicenumber) < 0) {
      al.add(KPMSG);
    }

    if (SO23.booleanValue()) {
      postiveNum = nnumber.multiply(1.0D + nouttoplimit.doubleValue() / 100.0D);
    }
    else {
      postiveNum = nnumber;
    }
    if (postiveNum.compareTo(ntotalinventorynumber) < 0) {
      al.add(CKMSG);
    }

    if (al.size() > 0) {
      String sScoremsg = "";
      for (int j = 0; j < al.size(); j++) {
        sScoremsg = sScoremsg + (String)al.get(j) + "\n";
      }
      throw new BusinessException(sScoremsg);
    }
  }

  public void processOutState(SaleorderBVO[] bvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((bvos == null) || (bvos.length == 0)) {
      return;
    }
    SysInitBO sbo = new SysInitBO();

    Hashtable htTemp = sbo.queryBatchParaValues(bvos[0].getPkcorp(), new String[] { "SO23", "SO24", "SO25", "SO26", "SO47" });

    UFBoolean SO23 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO23", htTemp);

    UFDouble SO24 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO24", htTemp);

    UFBoolean SO25 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO25", htTemp);

    UFDouble SO26 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO26", htTemp);

    UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", htTemp);

    SaleorderBVO[] bakbvos = (SaleorderBVO[])(SaleorderBVO[])SoVoTools.getVOsByVOs("nc.vo.so.so001.SaleorderBVO", bvos, bvos[0].getAttributeNames(), bvos[0].getAttributeNames());

    HashMap hpp = SOToolsDMO.getAnyValueUFBoolean("so_sale", "bdeliver", "csaleid", new String[] { bvos[0].getCsaleid() }, null);

    UFBoolean bdeliver = (UFBoolean)hpp.get(bvos[0].getCsaleid());
    if (bdeliver == null) {
      bdeliver = new UFBoolean(false);
    }
    UFDouble ufzero = new UFDouble(0);
    UFBoolean uftrue = new UFBoolean(true);
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble num = null; UFDouble outnum = null; UFDouble ntaltransretnum = null; UFDouble ntranslossnum = null; UFDouble tempnum = null; UFDouble tempnum1 = null;
    UFDouble noutcloselimit = null;
    UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());

    ArrayList outendvolist = new ArrayList();
    ArrayList outopenvolist = new ArrayList();

    ArrayList rowendvolist = new ArrayList();
    ArrayList rowopenvolist = new ArrayList();

    ArrayList rowsendopenlist = new ArrayList();
    SaleorderBVO sendendbvo = null;

    UFDouble ntotalinventorynumber_old = null;

    int i = 0; for (int loop = bvos.length; i < loop; i++)
    {
      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getNtotalinventorynumber() != null) && (bvos[i].getNtotalinventorynumber_old() != null) && (bvos[i].getNtotalinventorynumber().abs().doubleValue() > bvos[i].getNtotalinventorynumber_old().abs().doubleValue()))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000516"));
      }

      checkAllowDiffer(bvos[i], i, SO23, SO24, SO25, SO26);

      bvos[i].setStatus(0);
      bvos[i].setCreceipttype("30");
      bakbvos[i].setCreceipttype("30");

      num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();

      ntotalinventorynumber_old = bvos[i].getNtotalinventorynumber_old();
      if (ntotalinventorynumber_old == null) {
        ntotalinventorynumber_old = SoVoConst.duf0;
      }
      outnum = bvos[i].getNtotalinventorynumber() == null ? ufzero : bvos[i].getNtotalinventorynumber();

      ntaltransretnum = bvos[i].getNtaltransretnum() == null ? ufzero : bvos[i].getNtaltransretnum();

      ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

      noutcloselimit = bvos[i].getAttributeValue("noutcloselimit") == null ? outendrate : new UFDouble(100).sub((UFDouble)bvos[i].getAttributeValue("noutcloselimit")).div(100.0D);

      num = num.abs();
      outnum = outnum.abs();
      ntaltransretnum = ntaltransretnum.abs();
      ntranslossnum = ntranslossnum.abs();
      ntotalinventorynumber_old = ntotalinventorynumber_old.abs();
      tempnum = num.multiply(noutcloselimit).sub(outnum).add(ntaltransretnum).add(ntranslossnum);

      tempnum1 = num.multiply(noutcloselimit).sub(ntotalinventorynumber_old).add(ntaltransretnum).add(ntranslossnum);

      UFDouble ntotalreturnnumber = bvos[i].getNtotalreturnnumber();
      if ((ntotalreturnnumber != null) && (ntotalreturnnumber.abs().doubleValue() + ntranslossnum.doubleValue() > outnum.abs().doubleValue()))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000036"));
      }

      if (tempnum.doubleValue() <= 0.0D)
      {
        if (SO47.booleanValue()) {
          if ((bvos[i].getBifinventoryfinish() == null) || (!bvos[i].getBifinventoryfinish().booleanValue()))
          {
            outendvolist.add(bvos[i]);
            bvos[i].setBifinventoryfinish(uftrue);
          }

          bvos[i].setBifreceiptfinish(uftrue);
          bvos[i].setStatus(1);
        }
      }
      else
      {
        if (SO47.booleanValue()) {
          if ((bvos[i].getBifinventoryfinish() == null) || (bvos[i].getBifinventoryfinish().booleanValue()))
          {
            outopenvolist.add(bvos[i]);
            bvos[i].setBifinventoryfinish(uffalse);
            bvos[i].setStatus(1);
          }

          if (!bdeliver.booleanValue()) {
            bvos[i].setBifreceiptfinish(uffalse);
            bvos[i].setStatus(1);
          }
        }

        if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()))
        {
          if (bvos[i].getNtotalinventorynumber_old() != null) {
            if (tempnum1.doubleValue() <= 0.0D)
            {
              if ((bvos[i].getNtotalreceiptnumber() == null) || (bvos[i].getNtotalreceiptnumber().doubleValue() == 0.0D))
              {
                if ((bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()))
                {
                  sendendbvo = new SaleorderBVO();
                  sendendbvo.setCorder_bid(bvos[i].getCorder_bid());

                  sendendbvo.setCreceipttype("30");
                  sendendbvo.setBifreceiptfinish(uffalse);
                  rowsendopenlist.add(sendendbvo);
                }
              }
              outopenvolist.add(bvos[i]);
              bvos[i].setBifinventoryfinish(uffalse);
              bvos[i].setStatus(1);
            }
          }
          else {
            if ((bvos[i].getNtotalreceiptnumber() == null) || (bvos[i].getNtotalreceiptnumber().doubleValue() == 0.0D))
            {
              if ((bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()))
              {
                sendendbvo = new SaleorderBVO();
                sendendbvo.setCorder_bid(bvos[i].getCorder_bid());

                sendendbvo.setCreceipttype("30");
                sendendbvo.setBifreceiptfinish(uffalse);
                rowsendopenlist.add(sendendbvo);
              }
            }
            outopenvolist.add(bvos[i]);
            bvos[i].setBifinventoryfinish(uffalse);
            bvos[i].setStatus(1);
          }
        }
      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()) && (bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 2))
          continue;
        rowendvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(6));
        bvos[i].setStatus(1);
      }
      else {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 6))
          continue;
        rowopenvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(2));
        bvos[i].setStatus(1);
      }

    }

    ArrayList updatebvolist = new ArrayList();
      i = 0; for (int loop = bvos.length; i < loop; i++) {
      if (bvos[i].getStatus() == 1) {
        updatebvolist.add(bvos[i]);
      }
    }

    int count = updatebvolist.size();
    if (count <= 0)
      return;
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])updatebvolist.toArray(new SaleorderBVO[count]);

    String[] updatefields = { "bifreceiptfinish", "bifinventoryfinish" };
    String[] wherefields = { "corder_bid" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

    updatefields = new String[] { "frowstatus" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields, wherefields);

    SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(bvos, "csaleid"));

    SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

    SaleorderBVO[] tempbvos = null;
      i = 0; for (int loop = vos.length; i < loop; i++) {
      vos[i].getHeadVO().setStatus(0);
      tempbvos = vos[i].getBodyVOs();
      int j = 0; for (int loopj = tempbvos.length; j < loopj; j++)
      {
        if ((tempbvos[j].getFrowstatus() != null) && (tempbvos[j].getFrowstatus().intValue() != 2)) {
          continue;
        }
        if ((vos[i].getHeadVO().getFstatus() == null) || (vos[i].getHeadVO().getFstatus().intValue() != 6))
          continue;
        vos[i].getHeadVO().setFstatus(new Integer(2));

        vos[i].getHeadVO().setStatus(1);
      }

    }

    setBillStatus(hvos, "boutendflag");

    setBillStatus(hvos, "breceiptendflag");

    ArrayList openhvolist = new ArrayList();
    ArrayList closehvolist = new ArrayList();
      i = 0; for (int loop = vos.length; i < loop; i++) {
      if (vos[i].getHeadVO().getStatus() == 1)
        openhvolist.add(vos[i].getHeadVO());
      else {
        closehvolist.add(vos[i].getHeadVO());
      }
    }

    if (openhvolist.size() > 0) {
      String headsql = " update so_sale set fstatus = 2 where csaleid = ? ";

      SOToolsDMO.updateBatch((SaleorderHVO[])(SaleorderHVO[])openhvolist.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
    }

    if (closehvolist.size() > 0) {
      setBillStatus((SaleorderHVO[])(SaleorderHVO[])closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
    }

    if (rowsendopenlist.size() > 0)
    {
      SaleorderBVO[] sendendbvos = (SaleorderBVO[])(SaleorderBVO[])rowsendopenlist.toArray(new SaleorderBVO[rowsendopenlist.size()]);

      SOToolsDMO.updateBatch(sendendbvos, new String[] { "bifreceiptfinish" }, new String[] { "bifreceiptfinish" }, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

      setBillStatus(hvos, "breceiptendflag");
    }

    updateAtpByOrdRows(hvos, bakbvos, bvos);

    IBillInvokeCreditManager armanager = (IBillInvokeCreditManager)NCLocator.getInstance().lookup(IBillInvokeCreditManager.class.getName());

    if (outendvolist.size() > 0)
    {
      vos = toSaleOrderVO(hvos, outendvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(12);

        renovateAR(armanager, vos[i]);
      }

    }

    if (outopenvolist.size() > 0)
    {
      vos = toSaleOrderVO(hvos, outopenvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(14);

        renovateAR(armanager, vos[i]);
      }

    }

    if (rowendvolist.size() > 0)
    {
      incomeAdjust_endForOrder(hvos, rowendvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]), 6);
    }

    if (rowopenvolist.size() > 0)
    {
      incomeAdjust_unEndForOrder(hvos, rowopenvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]), 9);
    }
  }

  private UFBoolean getParaUFBooleanValue(String sPk_corp, String sPara, Hashtable htTemp)
    throws BusinessException
  {
    if (htTemp == null) {
      SysInitBO sbo = new SysInitBO();

      htTemp = sbo.queryBatchParaValues(sPk_corp, new String[] { sPara });
    }
    UFBoolean SO47 = null;
    if ((htTemp == null) || (!htTemp.containsKey(sPara)))
      SO47 = new UFBoolean(true);
    else {
      SO47 = new UFBoolean(htTemp.get(sPara) == null ? "Y" : htTemp.get(sPara).toString());
    }

    return SO47;
  }

  private UFDouble getParaUFDoubleValue(String sPk_corp, String sPara, Hashtable htTemp)
    throws BusinessException
  {
    if (htTemp == null) {
      SysInitBO sbo = new SysInitBO();

      htTemp = sbo.queryBatchParaValues(sPk_corp, new String[] { sPara });
    }
    UFDouble SO47 = null;
    if ((htTemp == null) || (!htTemp.containsKey(sPara)))
      SO47 = new UFDouble(0);
    else {
      SO47 = new UFDouble(htTemp.get(sPara) == null ? "0" : htTemp.get(sPara).toString());
    }

    return SO47;
  }

  private SaleOrderVO[] toSaleOrderVO(SaleorderHVO[] hvos, List bvoslist)
  {
    if ((hvos == null) || (hvos.length == 0) || (bvoslist == null))
      return null;
    ArrayList volist = null;
    HashMap hsvo = new HashMap();
    SaleorderBVO bvo = null;
    int i = 0; for (int loop = bvoslist.size(); i < loop; i++) {
      bvo = (SaleorderBVO)bvoslist.get(i);
      if (bvo.getCsaleid() == null)
        continue;
      volist = (ArrayList)hsvo.get(bvo.getCsaleid());
      if (volist == null) {
        volist = new ArrayList();
        hsvo.put(bvo.getCsaleid(), volist);
      }
      volist.add(bvo);
    }
    if (hsvo.size() <= 0)
      return null;
    ArrayList retvolist = new ArrayList();
    Map.Entry oentry = null;
    Iterator iter = hsvo.entrySet().iterator();
    int pos = -1;
    String[] keys = { "csaleid" };
    Object[] values = new Object[1];
    SaleOrderVO vo = null;
    while (iter.hasNext()) {
      oentry = (Map.Entry)iter.next();
      values[0] = oentry.getKey();
      pos = SoVoTools.find(hvos, keys, values);
      if (pos >= 0) {
        vo = new SaleOrderVO();
        vo.setParentVO(hvos[pos]);
        volist = (ArrayList)oentry.getValue();
        vo.setChildrenVO((SaleorderBVO[])(SaleorderBVO[])volist.toArray(new SaleorderBVO[volist.size()]));

        retvolist.add(vo);
      }
    }
    if (retvolist.size() <= 0)
      return null;
    return (SaleOrderVO[])(SaleOrderVO[])retvolist.toArray(new SaleOrderVO[retvolist.size()]);
  }

  private UFDouble getOutSendEndRate(String corpid)
  {
    UFDouble retrate = null;
    UFDouble SO29 = null;
    try
    {
      SysInitDMO sysinitdmo = new SysInitDMO();
      SO29 = sysinitdmo.getParaDbl(corpid, "SO29");
      if (SO29 == null)
        SO29 = new UFDouble(0);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      SO29 = new UFDouble(0);
    }

    retrate = new UFDouble(1).sub(SO29.abs().div(new UFDouble(100)));
    return retrate;
  }

  private UFDouble getOutInvoiceEndRate(String corpid)
  {
    UFDouble retrate = null;
    UFDouble SO48 = null;
    try {
      SysInitDMO sysinitdmo = new SysInitDMO();
      SO48 = sysinitdmo.getParaDbl(corpid, "SO48");
      if (SO48 == null)
        SO48 = new UFDouble(0);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      SO48 = new UFDouble(0);
    }

    retrate = new UFDouble(1).sub(SO48.abs().div(new UFDouble(100)));
    return retrate;
  }

  public void processInvoicendState(SaleorderBVO[] bvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((bvos == null) || (bvos.length == 0)) {
      return;
    }
    UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

    UFDouble SO48 = getOutInvoiceEndRate(bvos[0].getPkcorp());

    UFDouble ufzero = new UFDouble(0);
    UFBoolean uftrue = new UFBoolean(true);
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble num = null; UFDouble invoicenum = null; UFDouble tempnum = null; UFDouble ntotalinvoicenumber_old = null;

    UFDouble nsummny = null;
    UFDouble ntotalinvoicemny = null;

    ArrayList rowendvolist = new ArrayList();
    ArrayList rowopenvolist = new ArrayList();

    ArrayList invoiceendvolist = new ArrayList();
    ArrayList invoiceopenvolist = new ArrayList();

    int i = 0; for (int loop = bvos.length; i < loop; i++) {
      bvos[i].setStatus(0);
      bvos[i].setCreceipttype("30");

      num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();

      if ((bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()) && (bvos[i].getNtotalinvoicenumber() != null) && (bvos[i].getNtotalinvoicenumber_old() != null) && (bvos[i].getNtotalinvoicenumber().abs().doubleValue() > bvos[i].getNtotalinvoicenumber_old().abs().doubleValue()))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000519"));
      }

      if (SO47.booleanValue()) {
        if (num.doubleValue() == 0.0D)
        {
          nsummny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();

          ntotalinvoicemny = bvos[i].getNtotalinvoicemny() == null ? ufzero : bvos[i].getNtotalinvoicemny();

          nsummny = nsummny.abs();
          ntotalinvoicemny = ntotalinvoicemny.abs();

          tempnum = nsummny.multiply(SO48).sub(ntotalinvoicemny);
          if (tempnum.doubleValue() <= 0.0D) {
            if ((bvos[i].getBifinvoicefinish() == null) || (!bvos[i].getBifinvoicefinish().booleanValue()))
            {
              bvos[i].setBifinvoicefinish(uftrue);
              bvos[i].setStatus(1);

              invoiceendvolist.add(bvos[i]);
            }
          }
          else if ((bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
          {
            bvos[i].setBifinvoicefinish(uffalse);
            bvos[i].setStatus(1);

            invoiceopenvolist.add(bvos[i]);
          }

        }
        else
        {
          invoicenum = bvos[i].getNtotalinvoicenumber() == null ? ufzero : bvos[i].getNtotalinvoicenumber();

          num = num.abs();
          invoicenum = invoicenum.abs();

          tempnum = num.multiply(SO48).sub(invoicenum);
          if (tempnum.doubleValue() <= 0.0D) {
            if ((bvos[i].getBifinvoicefinish() == null) || (!bvos[i].getBifinvoicefinish().booleanValue()))
            {
              bvos[i].setBifinvoicefinish(uftrue);
              bvos[i].setStatus(1);

              invoiceendvolist.add(bvos[i]);
            }
          }
          else if ((bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
          {
            if (bvos[i].getNtotalinvoicenumber_old() != null) {
              ntotalinvoicenumber_old = bvos[i].getNtotalinvoicenumber_old().abs();

              tempnum = num.multiply(SO48).sub(ntotalinvoicenumber_old);

              if (tempnum.doubleValue() <= 0.0D) {
                bvos[i].setBifinvoicefinish(uffalse);
                bvos[i].setStatus(1);

                invoiceopenvolist.add(bvos[i]);
              }
            } else {
              bvos[i].setBifinvoicefinish(uffalse);
              bvos[i].setStatus(1);

              invoiceopenvolist.add(bvos[i]);
            }
          }

        }

      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()) && (bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 2))
          continue;
        rowendvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(6));
        bvos[i].setStatus(1);
      }
      else {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 6))
          continue;
        rowopenvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(2));
        bvos[i].setStatus(1);
      }

    }

    ArrayList updatebvolist = new ArrayList();
      i = 0; for (int loop = bvos.length; i < loop; i++) {
      if (bvos[i].getStatus() == 1) {
        updatebvolist.add(bvos[i]);
      }
    }

    int count = updatebvolist.size();
    if (count <= 0)
      return;
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])updatebvolist.toArray(new SaleorderBVO[count]);

    String[] updatefields = { "bifinvoicefinish" };
    String[] wherefields = { "corder_bid" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

    updatefields = new String[] { "frowstatus" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields, wherefields);

    SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(bvos, "csaleid"));

    SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

    SaleorderBVO[] tempbvos = null;
      i = 0; for (int loop = vos.length; i < loop; i++) {
      vos[i].getHeadVO().setStatus(0);
      tempbvos = vos[i].getBodyVOs();
      int j = 0; for (int loopj = tempbvos.length; j < loopj; j++)
      {
        if ((tempbvos[j].getFrowstatus() != null) && (tempbvos[j].getFrowstatus().intValue() != 2)) {
          continue;
        }
        if ((vos[i].getHeadVO().getFstatus() == null) || (vos[i].getHeadVO().getFstatus().intValue() != 6))
          continue;
        vos[i].getHeadVO().setFstatus(new Integer(2));

        vos[i].getHeadVO().setStatus(1);
      }

    }

    setBillStatus(hvos, "binvoicendflag");

    ArrayList openhvolist = new ArrayList();
    ArrayList closehvolist = new ArrayList();
      i = 0; for (int loop = vos.length; i < loop; i++) {
      if (vos[i].getHeadVO().getStatus() == 1)
        openhvolist.add(vos[i].getHeadVO());
      else {
        closehvolist.add(vos[i].getHeadVO());
      }
    }

    if (openhvolist.size() > 0) {
      String headsql = " update so_sale set fstatus = 2 where csaleid = ? ";

      SOToolsDMO.updateBatch((SaleorderHVO[])(SaleorderHVO[])openhvolist.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
    }

    if (closehvolist.size() > 0) {
      setBillStatus((SaleorderHVO[])(SaleorderHVO[])closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
    }

    if (rowendvolist.size() > 0)
    {
      incomeAdjust_endForOrder(hvos, rowendvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]), 6);
    }

    if (invoiceendvolist.size() > 0) {
      vos = toSaleOrderVO(hvos, invoiceendvolist);
      IBillInvokeCreditManager armanager = DoInterfaceForSO.getBillInvokeCreditManager();

        i = 0; for (int loop = vos.length; i < loop; i++) {
        vos[i].setIAction(17);
        renovateAR(armanager, vos[i]);
      }

    }

    if (rowopenvolist.size() > 0)
    {
      incomeAdjust_unEndForOrder(hvos, rowopenvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]), 9);
    }

    if (invoiceopenvolist.size() > 0) {
      vos = toSaleOrderVO(hvos, invoiceopenvolist);
      IBillInvokeCreditManager armanager = DoInterfaceForSO.getBillInvokeCreditManager();

        i = 0; for (int loop = vos.length; i < loop; i++) {
        vos[i].setIAction(18);
        renovateAR(armanager, vos[i]);
      }
    }
  }

  public void processOrdState(AggregatedValueObject vo)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if (vo == null) {
      return;
    }
    SaleOrderVO ordvo = (SaleOrderVO)vo;
    if ((ordvo == null) || (ordvo.getParentVO() == null) || (ordvo.getChildrenVO() == null) || (ordvo.getChildrenVO().length <= 0))
    {
      return;
    }SaleorderBVO[] bvos = ordvo.getBodyVOs();
    SaleorderHVO hvo = ordvo.getHeadVO();

    SaleorderBVO[] old_bvos = null;

    SaleorderHVO old_hvo = null;
    try
    {
      old_bvos = (SaleorderBVO[])(SaleorderBVO[])ObjectUtils.serializableClone(bvos);

      old_hvo = (SaleorderHVO)ObjectUtils.serializableClone(hvo);
    }
    catch (Exception e) {
      throw new RemoteException("remote call", e);
    }

    UFDouble ufzero = new UFDouble(0);
    UFBoolean uftrue = new UFBoolean(true);
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble num = null; UFDouble outnum = null; UFDouble receiptnum = null; UFDouble ntaltransretnum = null;
    UFDouble ntranslossnum = null; UFDouble invoicenum = null; UFDouble transnum = null; UFDouble summny = null;
    UFDouble yetmny = null; UFDouble tempnum = null; UFDouble tempmny = null; UFDouble tempnum1 = null;

    UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());
    UFDouble noutcloselimit = null;

    int outfinishnum = 0; int receiptfinishnum = 0; int transfinishnum = 0; int invoicefinishnum = 0; int payfinishnum = 0; int rowfinishnum = 0;
    UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

    int i = 0; for (int loop = bvos.length; i < loop; i++) {
      bvos[i].setStatus(0);

      num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
      outnum = bvos[i].getNtotalinventorynumber() == null ? ufzero : bvos[i].getNtotalinventorynumber();

      ntaltransretnum = bvos[i].getNtaltransretnum() == null ? ufzero : bvos[i].getNtaltransretnum();

      ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

      noutcloselimit = bvos[i].getAttributeValue("noutcloselimit") == null ? outendrate : new UFDouble(100).sub((UFDouble)bvos[i].getAttributeValue("noutcloselimit")).div(100.0D);

      num = num.abs();
      outnum = outnum.abs();
      ntaltransretnum = ntaltransretnum.abs();
      ntranslossnum = ntranslossnum.abs();

      tempnum1 = num.sub(outnum);

      tempnum = num.multiply(noutcloselimit).sub(outnum).add(ntaltransretnum).add(ntranslossnum);

      if ((num.doubleValue() > 0.0D) && (tempnum.doubleValue() <= 0.0D))
      {
        if (SO47.booleanValue()) {
          bvos[i].setBifinventoryfinish(uftrue);
          bvos[i].setBifreceiptfinish(uftrue);
        }

      }
      else if (SO47.booleanValue()) {
        bvos[i].setBifinventoryfinish(uffalse);
      }

      receiptnum = bvos[i].getNtotalreceiptnumber() == null ? ufzero : bvos[i].getNtotalreceiptnumber();

      receiptnum = receiptnum.abs();

      tempnum1 = num.sub(receiptnum);

      tempnum = num.multiply(noutcloselimit).sub(receiptnum).add(ntaltransretnum).add(ntranslossnum);

      if ((num.doubleValue() > 0.0D) && (tempnum.doubleValue() <= 0.0D)) {
        bvos[i].setBifreceiptfinish(uftrue);
      }
      else if (!bvos[i].getBifinventoryfinish().booleanValue()) {
        bvos[i].setBifreceiptfinish(uffalse);
      }

      invoicenum = bvos[i].getNtotalinvoicenumber() == null ? ufzero : bvos[i].getNtotalinvoicenumber();

      invoicenum = invoicenum.abs();
      tempnum = num.sub(invoicenum);

      if ((num.doubleValue() > 0.0D) && (tempnum.doubleValue() == 0.0D)) {
        bvos[i].setBifinvoicefinish(uftrue);
      }
      else if (num.doubleValue() == 0.0D)
      {
        UFDouble nsummny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();

        UFDouble ntotalinvoicemny = bvos[i].getNtotalinvoicemny() == null ? ufzero : bvos[i].getNtotalinvoicemny();

        nsummny = nsummny.abs();
        ntotalinvoicemny = ntotalinvoicemny.abs();

        tempnum = nsummny.sub(ntotalinvoicemny);
        if ((nsummny.doubleValue() > 0.0D) && (tempnum.doubleValue() <= 0.0D)) {
          if ((bvos[i].getBifinvoicefinish() == null) || (!bvos[i].getBifinvoicefinish().booleanValue()))
          {
            bvos[i].setBifinvoicefinish(uftrue);
          }
        }
        else if ((bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
        {
          bvos[i].setBifinvoicefinish(uffalse);
        }
      }
      else {
        bvos[i].setBifinvoicefinish(uffalse);
      }

      summny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();

      yetmny = bvos[i].getNtotalpaymny() == null ? ufzero : bvos[i].getNtotalpaymny();

      summny = summny.abs();
      yetmny = yetmny.abs();

      tempmny = summny.sub(yetmny);

      if ((summny.doubleValue() > 0.0D) && (tempmny.doubleValue() == 0.0D))
        bvos[i].setBifpayfinish(uftrue);
      else {
        bvos[i].setBifpayfinish(uffalse);
      }

      ntranslossnum = bvos[i].getNtaltransnum() == null ? ufzero : bvos[i].getNtaltransnum();

      ntranslossnum = ntranslossnum.abs();
      tempnum = num.sub(ntranslossnum).add(ntaltransretnum).add(ntranslossnum);

      if (tempnum.doubleValue() <= 0.0D)
        bvos[i].setBiftransfinish(uftrue);
      else {
        bvos[i].setBiftransfinish(uffalse);
      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()) && (bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        if ((bvos[i].getFrowstatus() != null) && (bvos[i].getFrowstatus().intValue() == 2))
        {
          bvos[i].setFrowstatus(new Integer(6));
        }

      }
      else if ((bvos[i].getFrowstatus() != null) && (bvos[i].getFrowstatus().intValue() == 6))
      {
        bvos[i].setFrowstatus(new Integer(2));
      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()))
      {
        outfinishnum++;
      }
      if ((bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()))
      {
        receiptfinishnum++;
      }
      if ((bvos[i].getBiftransfinish() != null) && (bvos[i].getBiftransfinish().booleanValue()))
      {
        transfinishnum++;
      }
      if ((bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        invoicefinishnum++;
      }
      if ((bvos[i].getBifpayfinish() != null) && (bvos[i].getBifpayfinish().booleanValue()))
      {
        payfinishnum++;
      }
      if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 6))
        continue;
      rowfinishnum++;
    }

    String[] updatefields = { "bifreceiptfinish", "bifinventoryfinish", "biftransfinish", "bifinvoicefinish", "bifpayfinish" };

    String[] wherefields = { "corder_bid" };

    SOToolsDMO.updateBatch(bvos, updatefields, updatefields, "so_saleexecute", wherefields, new String[] { "csale_bid" });

    updatefields = new String[] { "frowstatus" };

    SOToolsDMO.updateBatch(bvos, updatefields, updatefields, "so_saleorder_b", wherefields, wherefields);

    if (outfinishnum >= bvos.length)
      hvo.setBoutendflag(uftrue);
    else {
      hvo.setBoutendflag(uffalse);
    }
    if (receiptfinishnum >= bvos.length)
      hvo.setBreceiptendflag(uftrue);
    else {
      hvo.setBreceiptendflag(uffalse);
    }
    if (transfinishnum >= bvos.length)
      hvo.setBtransendflag(uftrue);
    else {
      hvo.setBtransendflag(uffalse);
    }
    if (invoicefinishnum >= bvos.length)
      hvo.setBinvoicendflag(uftrue);
    else {
      hvo.setBinvoicendflag(uffalse);
    }
    if (payfinishnum >= bvos.length)
      hvo.setBpayendflag(uftrue);
    else {
      hvo.setBpayendflag(uffalse);
    }
    if (rowfinishnum >= bvos.length) {
      if ((hvo.getFstatus() == null) || (hvo.getFstatus().intValue() == 2))
      {
        hvo.setFstatus(new Integer(6));
      }
    } else if ((hvo.getFstatus() == null) || (hvo.getFstatus().intValue() == 6))
    {
      hvo.setFstatus(new Integer(2));
    }

    SOToolsDMO.updateBatch(new SaleorderHVO[] { hvo }, new String[] { "binvoicendflag", "breceiptendflag", "boutendflag", "btransendflag", "bpayendflag", "fstatus" }, "so_sale", new String[] { "csaleid" });

    updateAtpByOrdRows(new SaleorderHVO[] { hvo }, old_bvos, bvos);

    updateArByOrdRows(new SaleorderHVO[] { hvo }, old_bvos, bvos, false);
  }

  public void processPayState(SaleorderBVO[] bvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
  }

  public void processReceiptfinishState(SaleorderBVO[] bvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((bvos == null) || (bvos.length == 0)) {
      return;
    }
    SaleorderBVO[] bakbvos = (SaleorderBVO[])(SaleorderBVO[])SoVoTools.getVOsByVOs("nc.vo.so.so001.SaleorderBVO", bvos, bvos[0].getAttributeNames(), bvos[0].getAttributeNames());

    UFDouble ufzero = new UFDouble(0);
    UFBoolean uftrue = new UFBoolean(true);
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble num = null; UFDouble receiptnum = null; UFDouble ntaltransretnum = null; UFDouble ntranslossnum = null; UFDouble tempnum = null;

    UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());
    UFDouble noutcloselimit = null;

    ArrayList receiptendvolist = new ArrayList();
    ArrayList receiptopenvolist = new ArrayList();

    ArrayList rowendvolist = new ArrayList();
    ArrayList rowopenvolist = new ArrayList();
    UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

    int i = 0; for (int loop = bvos.length; i < loop; i++) {
      bvos[i].setStatus(0);
      bvos[i].setCreceipttype("30");
      bakbvos[i].setCreceipttype("30");

      num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
      receiptnum = bvos[i].getNtotalreceiptnumber() == null ? ufzero : bvos[i].getNtotalreceiptnumber();

      ntaltransretnum = bvos[i].getNtaltransretnum() == null ? ufzero : bvos[i].getNtaltransretnum();

      ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

      noutcloselimit = bvos[i].getAttributeValue("noutcloselimit") == null ? outendrate : new UFDouble(1).sub(((UFDouble)bvos[i].getAttributeValue("noutcloselimit")).div(100.0D));

      num = num.abs();
      receiptnum = receiptnum.abs();
      ntaltransretnum = ntaltransretnum.abs();
      ntranslossnum = ntranslossnum.abs();
      tempnum = num.multiply(noutcloselimit).sub(receiptnum).add(ntaltransretnum).add(ntranslossnum);

      if (tempnum.doubleValue() <= 0.0D) {
        if ((bvos[i].getBifreceiptfinish() == null) || (!bvos[i].getBifreceiptfinish().booleanValue()))
        {
          if (SO47.booleanValue()) {
            receiptendvolist.add(bvos[i]);
            bvos[i].setBifreceiptfinish(uftrue);
            bvos[i].setStatus(1);
          }
        }
      }
      else if ((bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()))
      {
        receiptopenvolist.add(bvos[i]);
        bvos[i].setBifreceiptfinish(uffalse);
        bvos[i].setStatus(1);
      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()) && (bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 2))
          continue;
        rowendvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(6));
        bvos[i].setStatus(1);
      }
      else {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 6))
          continue;
        rowopenvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(2));
        bvos[i].setStatus(1);
      }

    }

    ArrayList updatebvolist = new ArrayList();
      i = 0; for (int loop = bvos.length; i < loop; i++) {
      if (bvos[i].getStatus() == 1) {
        updatebvolist.add(bvos[i]);
      }
    }

    int count = updatebvolist.size();
    if (count <= 0)
      return;
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])updatebvolist.toArray(new SaleorderBVO[count]);

    String[] updatefields = { "bifreceiptfinish" };
    String[] wherefields = { "corder_bid" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

    updatefields = new String[] { "frowstatus" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields, wherefields);

    SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(bvos, "csaleid"));

    SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

    SaleorderBVO[] tempbvos = null;
      i = 0; for (int loop = vos.length; i < loop; i++) {
      vos[i].getHeadVO().setStatus(0);
      tempbvos = vos[i].getBodyVOs();
      int j = 0; for (int loopj = tempbvos.length; j < loopj; j++)
      {
        if ((tempbvos[j].getFrowstatus() != null) && (tempbvos[j].getFrowstatus().intValue() != 2)) {
          continue;
        }
        if ((vos[i].getHeadVO().getFstatus() == null) || (vos[i].getHeadVO().getFstatus().intValue() != 6))
          continue;
        vos[i].getHeadVO().setFstatus(new Integer(2));

        vos[i].getHeadVO().setStatus(1);
      }

    }

    setBillStatus(hvos, "breceiptendflag");

    ArrayList openhvolist = new ArrayList();
    ArrayList closehvolist = new ArrayList();
      i = 0; for (int loop = vos.length; i < loop; i++) {
      if (vos[i].getHeadVO().getStatus() == 1)
        openhvolist.add(vos[i].getHeadVO());
      else {
        closehvolist.add(vos[i].getHeadVO());
      }
    }

    if (openhvolist.size() > 0) {
      String headsql = " update so_sale set fstatus = 2 where csaleid = ? ";

      SOToolsDMO.updateBatch((SaleorderHVO[])(SaleorderHVO[])openhvolist.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
    }

    if (closehvolist.size() > 0) {
      setBillStatus((SaleorderHVO[])(SaleorderHVO[])closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
    }

    updateAtpByOrdRows(hvos, bakbvos, bvos);

    if (rowendvolist.size() > 0)
    {
      incomeAdjust_endForOrder(hvos, rowendvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]), 6);
    }

    if (rowopenvolist.size() > 0)
    {
      incomeAdjust_unEndForOrder(hvos, rowopenvolist);

      setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]), 9);
    }
  }

  public void processTransState(SaleorderBVO[] bvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((bvos == null) || (bvos.length == 0))
      return;
    UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

    UFDouble ufzero = new UFDouble(0);
    UFBoolean uftrue = new UFBoolean(true);
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble num = null; UFDouble transnum = null; UFDouble ntaltransretnum = null; UFDouble ntranslossnum = null; UFDouble tempnum = null;

    ArrayList rowendvolist = new ArrayList();
    ArrayList rowopenvolist = new ArrayList();

    int i = 0; for (int loop = bvos.length; i < loop; i++)
    {
      bvos[i].setStatus(0);
      bvos[i].setCreceipttype("30");

      if (SO47.booleanValue()) {
        num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();

        ntranslossnum = bvos[i].getNtaltransnum() == null ? ufzero : bvos[i].getNtaltransnum();

        ntaltransretnum = bvos[i].getNtaltransretnum() == null ? ufzero : bvos[i].getNtaltransretnum();

        ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

        num = num.abs();
        transnum = transnum.abs();
        ntaltransretnum = ntaltransretnum.abs();
        ntranslossnum = ntranslossnum.abs();
        tempnum = num.sub(ntranslossnum).add(ntaltransretnum).add(ntranslossnum);

        if (tempnum.doubleValue() <= 0.0D) {
          if ((bvos[i].getBiftransfinish() == null) || (!bvos[i].getBiftransfinish().booleanValue()))
          {
            bvos[i].setBiftransfinish(uftrue);
            bvos[i].setStatus(1);
          }
        }
        else if ((bvos[i].getBiftransfinish() != null) && (bvos[i].getBiftransfinish().booleanValue()))
        {
          bvos[i].setBiftransfinish(uffalse);
          bvos[i].setStatus(1);
        }

      }

      if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue()) && (bvos[i].getBifreceiptfinish() != null) && (bvos[i].getBifreceiptfinish().booleanValue()) && (bvos[i].getBifinvoicefinish() != null) && (bvos[i].getBifinvoicefinish().booleanValue()))
      {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 2))
          continue;
        rowendvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(6));
        bvos[i].setStatus(1);
      }
      else {
        if ((bvos[i].getFrowstatus() == null) || (bvos[i].getFrowstatus().intValue() != 6))
          continue;
        rowopenvolist.add(bvos[i]);
        bvos[i].setFrowstatus(new Integer(2));
        bvos[i].setStatus(1);
      }

    }

    ArrayList updatebvolist = new ArrayList();
      i = 0; for (int loop = bvos.length; i < loop; i++) {
      if (bvos[i].getStatus() == 1) {
        updatebvolist.add(bvos[i]);
      }
    }

    int count = updatebvolist.size();
    if (count <= 0)
      return;
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])updatebvolist.toArray(new SaleorderBVO[count]);

    String[] updatefields = { "biftransfinish" };
    String[] wherefields = { "corder_bid" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

    updatefields = new String[] { "frowstatus" };

    SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields, wherefields);

    SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(bvos, "csaleid"));

    SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

    SaleorderBVO[] tempbvos = null;
      i = 0; for (int loop = vos.length; i < loop; i++) {
      vos[i].getHeadVO().setStatus(0);
      tempbvos = vos[i].getBodyVOs();
      int j = 0; for (int loopj = tempbvos.length; j < loopj; j++)
      {
        if ((tempbvos[j].getFrowstatus() != null) && (tempbvos[j].getFrowstatus().intValue() != 2)) {
          continue;
        }
        if ((vos[i].getHeadVO().getFstatus() == null) || (vos[i].getHeadVO().getFstatus().intValue() != 6))
          continue;
        vos[i].getHeadVO().setFstatus(new Integer(2));

        vos[i].getHeadVO().setStatus(1);
      }

    }

    setBillStatus(hvos, "btransendflag");

    ArrayList openhvolist = new ArrayList();
    ArrayList closehvolist = new ArrayList();
      i = 0; for (int loop = vos.length; i < loop; i++) {
      if (vos[i].getHeadVO().getStatus() == 1)
        openhvolist.add(vos[i].getHeadVO());
      else {
        closehvolist.add(vos[i].getHeadVO());
      }
    }

    if (openhvolist.size() > 0) {
      String headsql = " update so_sale set fstatus = 2 where csaleid = ? ";

      SOToolsDMO.updateBatch((SaleorderHVO[])(SaleorderHVO[])openhvolist.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
    }

    if (closehvolist.size() > 0) {
      setBillStatus((SaleorderHVO[])(SaleorderHVO[])closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
    }

    if (rowendvolist.size() > 0) {
      incomeAdjust_endForOrder(hvos, rowendvolist);
    }

    if (rowopenvolist.size() > 0)
      incomeAdjust_unEndForOrder(hvos, rowopenvolist);
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByBIDs(String[] bodykeys)
    throws BusinessException
  {
    if ((bodykeys == null) || (bodykeys.length <= 0)) {
      return null;
    }
    String swhere = GeneralSqlString.formInSQL("so_saleorder_b.corder_bid", bodykeys);
    try
    {
      return queryAllBodyDataByWhere(swhere, null);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByBIDsAndFreeItems(String[] bodykeys)
    throws BusinessException
  {
    CircularlyAccessibleValueObject[] bvos = queryAllBodyDataByBIDs(bodykeys);

    if ((bvos == null) || (bvos.length <= 0))
      return bvos;
    try
    {
      initFreeItem(bvos);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    return bvos;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByIDsAndFreeItems(String[] hkeys)
    throws SQLException, RemoteException
  {
    CircularlyAccessibleValueObject[] bvos = queryAllBodyDataByIDs(hkeys);

    if ((bvos == null) || (bvos.length <= 0)) {
      return bvos;
    }
    initFreeItem(bvos);
    return bvos;
  }

  public CircularlyAccessibleValueObject[] queryBodyDataForUpdateStatus(String[] bodykeys) throws SQLException
  {
    if ((bodykeys == null) || (bodykeys.length <= 0)) {
      return null;
    }
    long s = System.currentTimeMillis();

    String sql = "SELECT so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype,  so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid, so_saleorder_b.cinventoryid,  so_saleorder_b.nnumber, so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate,  so_saleorder_b.blargessflag, so_saleorder_b.ccurrencytypeid, so_saleorder_b.nexchangeotobrate,  so_saleorder_b.nexchangeotoarate, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice,  so_saleorder_b.nmny, so_saleorder_b.nsummny,  so_saleorder_b.frowstatus,  so_saleorder_b.cinvbasdocid, so_saleorder_b.ts, so_saleorder_b.cadvisecalbodyid, so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag,  so_saleexecute.ntotalpaymny,  so_saleexecute.ntotalreceiptnumber, so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinventorynumber,  so_saleexecute.ntotalbalancenumber, so_saleexecute.ntotalsignnumber, so_saleexecute.bifinvoicefinish,  so_saleexecute.bifreceiptfinish, so_saleexecute.bifinventoryfinish, so_saleexecute.bifpayfinish,  so_saleexecute.bifpaybalance, so_saleexecute.bifpaysign, so_saleexecute.vfree1,  so_saleexecute.vfree2, so_saleexecute.vfree3, so_saleexecute.vfree4,  so_saleexecute.vfree5, so_saleexecute.ntotalplanreceiptnumber, so_saleexecute.ntotalreturnnumber,  so_saleorder_b.cconsigncorpid, so_saleorder_b.creccalbodyid, so_saleorder_b.cprolineid,  so_saleorder_b.cinventoryid1, so_saleexecute.ntaltransnum, so_saleexecute.ntalbalancemny,  so_saleexecute.ntaltransretnum, so_saleexecute.ntranslossnum, so_saleexecute.biftransfinish,  so_saleorder_b.noriginalcurnetprice, so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurmny, so_saleorder_b.noriginalcursummny,so_saleorder_b.pk_corp,so_saleexecute.ntotalinvoicemny,  so_saleorder_b.bdericttrans,so_saleorder_b.cbodywarehouseid,so_saleorder_b.crecwareid,so_saleorder_b.cbatchid,so_saleorder_b.noutcloselimit,so_saleorder_b.nouttoplimit  FROM so_saleorder_b, so_saleexecute  WHERE   so_saleexecute.creceipttype='30' AND so_saleorder_b.beditflag = 'N' AND so_saleorder_b.csaleid = so_saleexecute.csaleid AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid " + GeneralSqlString.formInSQL("so_saleorder_b.corder_bid", bodykeys);

    SaleorderBVO[] saleItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    try {
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      SCMEnv.out("queryBodyDataForUpdateStatus1:" + (System.currentTimeMillis() - s));

      s = System.currentTimeMillis();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString(1);
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString(2);
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString(3);
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString(4);
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString(5);
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString(6);
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString(7);
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject(8);
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        String dconsigndate = rs.getString(9);
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString(10);
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString(11);
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ccurrencytypeid = rs.getString(12);
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(13);
        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(14);
        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nnetprice = (BigDecimal)rs.getObject(15);
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject(16);
        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal nmny = (BigDecimal)rs.getObject(17);
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject(18);
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        Integer frowstatus = (Integer)rs.getObject(19);
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String cinvbasdocid = rs.getString(20);
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String ts = rs.getString(21);
        saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        String cadvisecalbodyid = rs.getString(22);
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        String boosflag = rs.getString(23);
        saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));

        String bsupplyflag = rs.getString(24);
        saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));

        BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(25);
        saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(26);
        saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(27);
        saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(28);

        saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(29);
        saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(30);
        saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        String bifinvoicefinish = rs.getString(31);
        saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        String bifreceiptfinish = rs.getString(32);
        saleItem.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        String bifinventoryfinish = rs.getString(33);
        saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        String bifpayfinish = rs.getString(34);
        saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        String bifpaybalance = rs.getString(35);
        saleItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String bifpaysign = rs.getString(36);
        saleItem.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

        String vfree1 = rs.getString(37);
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(38);
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(39);
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(40);
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(41);
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject(42);

        saleItem.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

        BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject(43);
        saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

        String cconsigncorpid = rs.getString(44);
        saleItem.setCconsigncorpid(cconsigncorpid == null ? null : cconsigncorpid.trim());

        String creccalbodyid = rs.getString(45);
        saleItem.setCreccalbodyid(creccalbodyid == null ? null : creccalbodyid.trim());

        String cprolineid = rs.getString(46);
        saleItem.setCprolineid(cprolineid == null ? null : cprolineid.trim());

        String cinventoryid1 = rs.getString(47);
        saleItem.setCinventoryid1(cinventoryid1 == null ? null : cinventoryid1.trim());

        BigDecimal ntaltransnum = (BigDecimal)rs.getObject(48);
        saleItem.setNtaltransnum(ntaltransnum == null ? null : new UFDouble(ntaltransnum));

        BigDecimal ntalbalancemny = (BigDecimal)rs.getObject(49);
        saleItem.setNtalbalancemny(ntalbalancemny == null ? null : new UFDouble(ntalbalancemny));

        BigDecimal ntaltransretnum = (BigDecimal)rs.getObject(50);
        saleItem.setNtaltransretnum(ntaltransretnum == null ? null : new UFDouble(ntaltransretnum));

        BigDecimal ntranslossnum = (BigDecimal)rs.getObject(51);
        saleItem.setNtranslossnum(ntranslossnum == null ? null : new UFDouble(ntranslossnum));

        String biftransfinish = rs.getString(52);
        saleItem.setBiftransfinish(biftransfinish == null ? null : new UFBoolean(biftransfinish.trim()));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject(53);
        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject(54);

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject(55);
        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject(56);
        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String pk_corp = rs.getString(57);
        saleItem.setPkcorp(pk_corp == null ? null : pk_corp.trim());

        BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(58);
        saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

        String bdericttrans = rs.getString(59);
        saleItem.setBdericttrans(bdericttrans == null ? null : new UFBoolean(bdericttrans.trim()));

        String cbodywarehouseid = rs.getString(60);
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String crecwareid = rs.getString(61);
        saleItem.setCrecwareid(crecwareid == null ? null : crecwareid.trim());

        String cbatchid = rs.getString(62);
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal noutcloselimit = (BigDecimal)rs.getObject(63);
        saleItem.setAttributeValue("noutcloselimit", noutcloselimit == null ? null : new UFDouble(noutcloselimit));

        BigDecimal nouttoplimit = (BigDecimal)rs.getObject(64);
        saleItem.setAttributeValue("nouttoplimit", nouttoplimit == null ? null : new UFDouble(nouttoplimit));

        v.addElement(saleItem);
      }
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
    saleItems = new SaleorderBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }

    SCMEnv.out("queryBodyDataForUpdateStatus2:" + (System.currentTimeMillis() - s));

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", bodykeys);

    return saleItems;
  }

  public UFDouble queryCachPayByOrdId(String csaleid)
    throws BusinessException
  {
    OrdVO[] balvos = null;
    try {
      BalanceDMO baldmo = new BalanceDMO();

      balvos = baldmo.queryOrdVO(" so_sale.csaleid ='" + csaleid + "' ");
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
    if ((balvos != null) && (balvos.length > 0) && (balvos[0] != null)) {
      return balvos[0].getNorigbalsummny();
    }
    return null;
  }

  public CircularlyAccessibleValueObject[] queryHeadDataForUpdateStatus(String[] ids)
    throws SQLException
  {
    if ((ids == null) || (ids.length <= 0)) {
      return null;
    }

    SOField[] addfields = SaleorderHVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getTablename());
          addfieldsql.append(".");
          addfieldsql.append(addfields[i].getDatabasename());
        }
      }
    }
    String strSql = "SELECT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag,so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,so_sale.bpayendflag " + addfieldsql.toString() + " FROM so_sale WHERE 1=1 " + GeneralSqlString.formInSQL("so_sale.csaleid", ids);

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    try {
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        SaleorderHVO saleHeader = new SaleorderHVO();

        String pk_corp = rs.getString(1);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(4);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(5);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(6);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(7);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(8);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(9);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String ccustomerid = rs.getString(10);
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString(11);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(12);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(13);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(14);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(15);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(16);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(17);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(18);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(19);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(20);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        String cwarehouseid = rs.getString(21);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(22);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(23);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(24);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(25);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        BigDecimal nsubscription = (BigDecimal)rs.getObject(26);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

        String ccreditnum = rs.getString(27);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(28);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(29);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(30);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(31);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(32);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(33);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(34);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(40);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(41);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(42);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(43);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ccalbodyid = rs.getString(44);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String csaleid = rs.getString(45);
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String bretinvflag = rs.getString(46);
        saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

        String boutendflag = rs.getString(47);
        saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

        String binvoicendflag = rs.getString(48);
        saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

        String breceiptendflag = rs.getString(49);
        saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));

        String ts = rs.getString(50);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        BigDecimal npreceiverate = (BigDecimal)rs.getObject(51);
        saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));

        BigDecimal npreceivemny = (BigDecimal)rs.getObject(52);
        saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

        String bpayendflag = rs.getString(53);
        saleHeader.setBpayendflag(bpayendflag == null ? null : new UFBoolean(bpayendflag.trim()));

        getOrdHValueFromResultSet(rs, 54, saleHeader);

        v.addElement(saleHeader);
      }
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
    if (v.size() > 0) {
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }

    return saleorderHs;
  }

  public void setOrdHeadStatus(SaleorderHVO[] hvos)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((hvos == null) || (hvos.length == 0)) {
      return;
    }

    HashMap statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifinvoicefinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

    UFDouble count = null;
    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        hvos[i].setBinvoicendflag(new UFBoolean(true));
      } else {
        count = (UFDouble)statehs.get(hvos[i].getCsaleid());
        if ((count != null) && (count.doubleValue() > 0.0D))
          hvos[i].setBinvoicendflag(new UFBoolean(false));
        else {
          hvos[i].setBinvoicendflag(new UFBoolean(true));
        }

      }

    }

    statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifreceiptfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        hvos[i].setBreceiptendflag(new UFBoolean(true));
      } else {
        count = (UFDouble)statehs.get(hvos[i].getCsaleid());
        if ((count != null) && (count.doubleValue() > 0.0D))
          hvos[i].setBreceiptendflag(new UFBoolean(false));
        else {
          hvos[i].setBreceiptendflag(new UFBoolean(true));
        }

      }

    }

    statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " bifinventoryfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        hvos[i].setBoutendflag(new UFBoolean(true));
      } else {
        count = (UFDouble)statehs.get(hvos[i].getCsaleid());
        if ((count != null) && (count.doubleValue() > 0.0D))
          hvos[i].setBoutendflag(new UFBoolean(false));
        else {
          hvos[i].setBoutendflag(new UFBoolean(true));
        }

      }

    }

    statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " biftransfinish='N' and beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        hvos[i].setBtransendflag(new UFBoolean(true));
      } else {
        count = (UFDouble)statehs.get(hvos[i].getCsaleid());
        if ((count != null) && (count.doubleValue() > 0.0D))
          hvos[i].setBtransendflag(new UFBoolean(false));
        else {
          hvos[i].setBtransendflag(new UFBoolean(true));
        }

      }

    }

    statehs = SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), "beditflag = 'N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");

    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        hvos[i].setBpayendflag(new UFBoolean(true));
      } else {
        count = (UFDouble)statehs.get(hvos[i].getCsaleid());
        if ((count != null) && (count.doubleValue() > 0.0D))
          hvos[i].setBpayendflag(new UFBoolean(false));
        else {
          hvos[i].setBpayendflag(new UFBoolean(true));
        }

      }

    }

    statehs = SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)", "csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " frowstatus = 6 and beditflag = 'N' group by csaleid ");

    HashMap allstatehs = SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)", "csaleid", SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " beditflag = 'N' group by csaleid ");

    UFDouble allcount = null;
    ArrayList updatehvolist = new ArrayList();
    for (int i = 0; i < hvos.length; i++) {
      if (statehs == null) {
        if (hvos[i].getFstatus().intValue() == 6)
          hvos[i].setFstatus(new Integer(2));
      }
      else
      {
        allcount = (UFDouble)allstatehs.get(hvos[i].getCsaleid());
        if (allcount.equals(statehs.get(hvos[i].getCsaleid()))) {
          hvos[i].setFstatus(new Integer(6));

          updatehvolist.add(hvos[i]);
        }
        else if (hvos[i].getFstatus().intValue() == 6) {
          hvos[i].setFstatus(new Integer(2));
        }
      }
    }

    SOToolsDMO.updateBatch(hvos, new String[] { "binvoicendflag", "breceiptendflag", "boutendflag", "btransendflag", "bpayendflag", "fstatus" }, "so_sale", new String[] { "csaleid" });
  }

  public void setOrdLastDate(String datefield, SaleorderBVO[] ordbvos, UFDate dlastdate)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((datefield == null) || (ordbvos == null) || (ordbvos.length <= 0)) {
      return;
    }
    if (dlastdate == null) {
      dlastdate = ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime().getDate();
    }

    if (dlastdate != null)
    {
      SoVoTools.setVOsValue(ordbvos, datefield, dlastdate);
    }
    else
    {
      String[] corder_bids = SoVoTools.getVOsOnlyValues(ordbvos, "corder_bid");

      HashMap hsdate = null;

      if ("dlastconsigdate".equals(datefield))
      {
        hsdate = SOToolsDMO.getAnyValueSORow("dm_delivdaypl", new String[] { "plandate" }, "pkbillb", corder_bids, " vbilltype='30' and dr=0  order by plandate asc ");
      }
      else if (!"dlasttransdate".equals(datefield))
      {
        if ("dlastoutdate".equals(datefield))
        {
          hsdate = SOToolsDMO.getAnyValueSORow("ic_general_h,ic_general_b", new String[] { "ic_general_h.dbilldate" }, "ic_general_b.cfirstbillbid", corder_bids, " ic_general_h.cgeneralhid=ic_general_b.cgeneralhid and ic_general_h.dr=0 and ic_general_b.cfirsttype='30' order by ic_general_h.dbilldate asc ");
        }
        else if ("dlastinvoicedt".equals(datefield))
        {
          hsdate = SOToolsDMO.getAnyValueSORow("so_saleinvoice,so_saleinvoice_b", new String[] { "so_saleinvoice.dbilldate" }, "so_saleinvoice_b.csourcebillbodyid", corder_bids, " so_saleinvoice.csaleid=so_saleinvoice_b.csaleid and so_saleinvoice.dr=0 and so_saleinvoice.creceipttype='30' order by so_saleinvoice.dbilldate asc ");
        }
        else if ("dlastpaydate".equals(datefield))
        {
          hsdate = SOToolsDMO.getAnyValueSORow("arap_djfb,arap_djclb", new String[] { "arap_djclb.clrq" }, "arap_djfb.cksqsh", corder_bids, " arap_djfb.ph='30' and arap_djclb.dr=0 and arap_djclb.dydjdl='ys' and arap_djclb.dydjzbid=arap_djfb.vouchid and arap_djclb.dydjfbid=arap_djfb.fb_oid order by arap_djclb.clrq asc ");
        }

      }

      if (hsdate != null) {
        SORowData row = null;
        int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
          row = (SORowData)hsdate.get(ordbvos[i].getCorder_bid());
          if (row != null) {
            ordbvos[i].setAttributeValue(datefield, row.getUFDate(0));
          }
          else {
            ordbvos[i].setAttributeValue(datefield, null);
          }
        }
      }
    }

    SoVoTools.setVOsValue(ordbvos, "creceipttype", "30");

    SOToolsDMO.updateBatch(ordbvos, new String[] { datefield }, new String[] { datefield }, "so_saleexecute", new String[] { "corder_bid", "csaleid", "creceipttype" }, new String[] { "csale_bid", "csaleid", "creceipttype" });
  }

  public void setRetNum(SaleOrderVO ordvo)
    throws NamingException, RemoteException, SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((ordvo == null) || (ordvo.getChildrenVO() == null) || (ordvo.getChildrenVO().length == 0))
    {
      return;
    }

    if ((ordvo.getHeadVO().getBretinvflag() == null) || (!ordvo.getHeadVO().getBretinvflag().booleanValue()))
    {
      return;
    }UFDouble uf0 = new UFDouble(0);

    if (ordvo.getIAction() == 0)
    {
      SaleorderBVO[] ordbvos = ordvo.getBodyVOs();
      HashMap hsordbvo = new HashMap();
      int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        if ((ordbvos[i].getCsourcebillbodyid() == null) || (ordbvos[i].getCsourcebillbodyid().trim().length() <= 0))
          continue;
        hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
      }

      if (hsordbvo.size() <= 0) {
        return;
      }

      SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[])(SaleorderBVO[])queryAllBodyDataByBIDs((String[])(String[])hsordbvo.keySet().toArray(new String[hsordbvo.size()]));

      if ((sourceOrdbvos == null) || (sourceOrdbvos.length <= 0)) {
        return;
      }
      ArrayList retvolist = new ArrayList();
      ToRetOrdVO retordvo = null;
      SaleorderBVO ordbvo = null;
      UFDouble dtemp = null;
        i = 0; for (int loop = sourceOrdbvos.length; i < loop; i++) {
        ordbvo = (SaleorderBVO)hsordbvo.get(sourceOrdbvos[i].getCorder_bid());

        if ((ordbvo == null) || (ordbvo.getNnumber() == null)) {
          continue;
        }
        dtemp = SoVoTools.getMnyAdd(sourceOrdbvos[i].getNtotalreturnnumber(), ordbvo.getNnumber().abs());

        if (dtemp != null) if (sourceOrdbvos[i].getNtotalinventorynumber() != null)
          {
            if (dtemp.abs().doubleValue() <= sourceOrdbvos[i].getNtotalinventorynumber().abs().doubleValue() - (sourceOrdbvos[i].getNtranslossnum() == null ? 0.0D : sourceOrdbvos[i].getNtranslossnum().doubleValue()));
          }
          else
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000039"));
          }


        retordvo = new ToRetOrdVO();
        retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
        retordvo.setNtotalreturnnumber(ordbvo.getNnumber().abs());
        retvolist.add(retordvo);
      }
      if (retvolist.size() > 0) {
        SaleToICDRPDMO dmo = new SaleToICDRPDMO();
        dmo.setReturnNum((ToRetOrdVO[])(ToRetOrdVO[])retvolist.toArray(new ToRetOrdVO[retvolist.size()]));
      }

    }
    else if ((ordvo.getIAction() == 1) || (ordvo.getIAction() == 5))
    {
      SaleorderBVO[] oldordbvos = ordvo.getOldSaleOrderVO().getBodyVOs();

      HashMap hsoldordvo = new HashMap();

      int i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
        if ((oldordbvos[i].getCsourcebillbodyid() == null) || (oldordbvos[i].getCsourcebillbodyid().trim().length() <= 0))
          continue;
        hsoldordvo.put(oldordbvos[i].getCsourcebillbodyid(), oldordbvos[i]);
      }

      if (hsoldordvo.size() <= 0) {
        return;
      }

      SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[])(SaleorderBVO[])queryAllBodyDataByBIDs((String[])(String[])hsoldordvo.keySet().toArray(new String[hsoldordvo.size()]));

      if ((sourceOrdbvos == null) || (sourceOrdbvos.length <= 0)) {
        return;
      }

      ArrayList retvolist = new ArrayList();
      ToRetOrdVO retordvo = null;
      SaleorderBVO ordbvo = null;
      UFDouble dtemp = null;
        i = 0; for (int loop = sourceOrdbvos.length; i < loop; i++) {
        ordbvo = (SaleorderBVO)hsoldordvo.get(sourceOrdbvos[i].getCorder_bid());

        if ((ordbvo == null) || (ordbvo.getNnumber() == null))
          continue;
        retordvo = new ToRetOrdVO();
        retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
        retordvo.setNtotalreturnnumber(uf0);
        retordvo.setNtotalreturnnumber(SoVoTools.getMnySub(retordvo.getNtotalreturnnumber(), ordbvo.getNnumber().abs()));

        sourceOrdbvos[i].setNtotalreturnnumber(SoVoTools.getMnySub(sourceOrdbvos[i].getNtotalreturnnumber(), ordbvo.getNnumber().abs()));

        retvolist.add(retordvo);
      }
      if (retvolist.size() > 0) {
        SaleToICDRPDMO dmo = new SaleToICDRPDMO();
        dmo.setReturnNum((ToRetOrdVO[])(ToRetOrdVO[])retvolist.toArray(new ToRetOrdVO[retvolist.size()]));
      }

      retvolist.clear();

      HashMap hsordbvo = new HashMap();

      SaleorderBVO[] ordbvos = ordvo.getAllSaleOrderVO().getBodyVOs();
        i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        if ((ordbvos[i].getCsourcebillbodyid() == null) || (ordbvos[i].getCsourcebillbodyid().trim().length() <= 0))
          continue;
        hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
      }

        i = 0; for (int loop = sourceOrdbvos.length; i < loop; i++) {
        ordbvo = (SaleorderBVO)hsordbvo.get(sourceOrdbvos[i].getCorder_bid());

        if ((ordbvo == null) || (ordbvo.getNnumber() == null)) {
          continue;
        }
        dtemp = SoVoTools.getMnyAdd(sourceOrdbvos[i].getNtotalreturnnumber(), ordbvo.getNnumber().abs());

        dtemp = SoVoTools.getMnyAdd(dtemp, sourceOrdbvos[i].getNtranslossnum());

        if ((dtemp != null) && ((sourceOrdbvos[i].getNtotalinventorynumber() == null) || (dtemp.abs().doubleValue() > sourceOrdbvos[i].getNtotalinventorynumber().abs().doubleValue())))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000039"));
        }

        retordvo = new ToRetOrdVO();
        retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
        retordvo.setNtotalreturnnumber(ordbvo.getNnumber().abs());
        retvolist.add(retordvo);
      }
      if (retvolist.size() > 0) {
        SaleToICDRPDMO dmo = new SaleToICDRPDMO();
        dmo.setReturnNum((ToRetOrdVO[])(ToRetOrdVO[])retvolist.toArray(new ToRetOrdVO[retvolist.size()]));
      }

    }
    else if (ordvo.getIAction() == 2)
    {
      SaleorderBVO[] ordbvos = ordvo.getBodyVOs();
      HashMap hsordbvo = new HashMap();
      int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        if ((ordbvos[i].getCsourcebillbodyid() == null) || (ordbvos[i].getCsourcebillbodyid().trim().length() <= 0))
          continue;
        hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
      }

      if (hsordbvo.size() <= 0) {
        return;
      }

      SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[])(SaleorderBVO[])queryAllBodyDataByBIDs((String[])(String[])hsordbvo.keySet().toArray(new String[hsordbvo.size()]));

      if ((sourceOrdbvos == null) || (sourceOrdbvos.length <= 0))
        return;
      ArrayList retvolist = new ArrayList();
      ToRetOrdVO retordvo = null;
      SaleorderBVO ordbvo = null;
        i = 0; for (int loop = sourceOrdbvos.length; i < loop; i++) {
        ordbvo = (SaleorderBVO)hsordbvo.get(sourceOrdbvos[i].getCorder_bid());

        if ((ordbvo == null) || (ordbvo.getNnumber() == null))
          continue;
        retordvo = new ToRetOrdVO();
        retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
        retordvo.setNtotalreturnnumber(uf0);
        retordvo.setNtotalreturnnumber(SoVoTools.getMnySub(retordvo.getNtotalreturnnumber(), ordbvo.getNnumber().abs()));

        retvolist.add(retordvo);
      }
      if (retvolist.size() > 0) {
        SaleToICDRPDMO dmo = new SaleToICDRPDMO();
        dmo.setReturnNum((ToRetOrdVO[])(ToRetOrdVO[])retvolist.toArray(new ToRetOrdVO[retvolist.size()]));
      }
    }
  }

  public void updateArByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
    throws NamingException, BusinessException, SQLException, RemoteException
  {
    updateArByOrdRows(ordhvos, oldordbvos, curordbvos, true);
  }

  public void updateArByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos, boolean bupdateCT)
    throws NamingException, BusinessException, SQLException, RemoteException
  {
    if ((ordhvos == null) || (oldordbvos == null) || (curordbvos == null)) {
      return;
    }
    HashMap hsoldbvos = new HashMap();
    int i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
      hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
    }

    ArrayList rowendlist = new ArrayList();
    ArrayList rowopenlist = new ArrayList();

    ArrayList outendvolist = new ArrayList();
    ArrayList outopenvolist = new ArrayList();

    ArrayList invoiceendvolist = new ArrayList();
    ArrayList invoiceopenvolist = new ArrayList();

    SaleorderBVO oldbvo = null;

    UFBoolean curbifinventoryfinish = null;
    UFBoolean oldbifinventoryfinish = null;

    UFBoolean curbifinvoicefinish = null;
    UFBoolean oldbifinvoicefinish = null;

    UFBoolean uffalse = new UFBoolean(false);

      i = 0; for (int loop = curordbvos.length; i < loop; i++) {
      oldbvo = (SaleorderBVO)hsoldbvos.get(curordbvos[i].getCorder_bid());

      if (oldbvo == null) {
        continue;
      }
      oldbifinventoryfinish = oldbvo.getBifinventoryfinish() == null ? uffalse : oldbvo.getBifinventoryfinish();

      curbifinventoryfinish = curordbvos[i].getBifinventoryfinish() == null ? uffalse : curordbvos[i].getBifinventoryfinish();

      if (!curbifinventoryfinish.equals(oldbifinventoryfinish)) {
        if (curbifinventoryfinish.booleanValue())
          outendvolist.add(curordbvos[i]);
        else {
          outopenvolist.add(curordbvos[i]);
        }

      }

      oldbifinvoicefinish = oldbvo.getBifinvoicefinish() == null ? uffalse : oldbvo.getBifinvoicefinish();

      curbifinvoicefinish = curordbvos[i].getBifinvoicefinish() == null ? uffalse : curordbvos[i].getBifinvoicefinish();

      if (!curbifinvoicefinish.equals(oldbifinvoicefinish)) {
        if (curbifinvoicefinish.booleanValue())
          invoiceendvolist.add(curordbvos[i]);
        else {
          invoiceopenvolist.add(curordbvos[i]);
        }

      }

      if (curordbvos[i].getFrowstatus().equals(oldbvo.getFrowstatus()))
      {
        continue;
      }
      if ((curordbvos[i].getFrowstatus().intValue() == 6) && (oldbvo.getFrowstatus().intValue() == 2))
      {
        rowendlist.add(oldbvo);
      }

      if ((curordbvos[i].getFrowstatus().intValue() != 2) || (oldbvo.getFrowstatus().intValue() != 6))
        continue;
      rowopenlist.add(oldbvo);
    }

    IBillInvokeCreditManager armanager = DoInterfaceForSO.getBillInvokeCreditManager();

    if (outendvolist.size() > 0)
    {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, outendvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(12);

        renovateAR(armanager, vos[i]);
      }

    }

    if (outopenvolist.size() > 0)
    {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, outopenvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(14);

        renovateAR(armanager, vos[i]);
      }

    }

    if (invoiceendvolist.size() > 0)
    {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, invoiceendvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(17);

        renovateAR(armanager, vos[i]);
      }

    }

    if (invoiceopenvolist.size() > 0)
    {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, invoiceopenvolist);

        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        vos[i].setIAction(18);

        renovateAR(armanager, vos[i]);
      }

    }

    if (rowendlist.size() > 0)
    {
      if (bupdateCT)
      {
        setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowendlist.toArray(new SaleorderBVO[rowendlist.size()]), 6);
      }

    }

    if (rowopenlist.size() > 0)
    {
      if (bupdateCT)
      {
        setSaleCT((SaleorderBVO[])(SaleorderBVO[])rowopenlist.toArray(new SaleorderBVO[rowopenlist.size()]), 9);
      }
    }
  }

  public void updateAtpByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
    throws NamingException, BusinessException, SQLException, RemoteException
  {
    if ((ordhvos == null) || (oldordbvos == null) || (curordbvos == null)) {
      return;
    }
    HashMap hsoldbvos = new HashMap();
    int i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
      hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
    }

    ArrayList sendendlist = new ArrayList();
    ArrayList sendopenlist = new ArrayList();
    ArrayList outendlist = new ArrayList();
    ArrayList outopenlist = new ArrayList();
    SaleorderBVO oldbvo = null;
      i = 0; for (int loop = curordbvos.length; i < loop; i++)
    {
      if ((curordbvos[i].getBdericttrans() != null) && (curordbvos[i].getBdericttrans().booleanValue()))
      {
        continue;
      }
      oldbvo = (SaleorderBVO)hsoldbvos.get(curordbvos[i].getCorder_bid());

      if (oldbvo == null)
      {
        continue;
      }
      if ((curordbvos[i].getBifreceiptfinish() == null) || (!curordbvos[i].getBifreceiptfinish().booleanValue()))
      {
        if ((oldbvo.getBifreceiptfinish() != null) && (oldbvo.getBifreceiptfinish().booleanValue()))
        {
          sendopenlist.add(oldbvo);
          oldbvo.setIAction(15);
          continue;
        }

      }

      if ((curordbvos[i].getBifinventoryfinish() != null) && (curordbvos[i].getBifinventoryfinish().booleanValue()))
      {
        if (!curordbvos[i].getBifinventoryfinish().equals(oldbvo.getBifinventoryfinish()))
        {
          outendlist.add(oldbvo);
          oldbvo.setIAction(12);
          continue;
        }

      }

      if ((curordbvos[i].getBifinventoryfinish() != null) && (!curordbvos[i].getBifinventoryfinish().equals(oldbvo.getBifinventoryfinish())))
      {
        if ((curordbvos[i].getBifinventoryfinish() != null) && (curordbvos[i].getBifinventoryfinish().booleanValue()))
        {
          outendlist.add(oldbvo);
          oldbvo.setIAction(12);
        }
        else {
          outopenlist.add(oldbvo);
          oldbvo.setIAction(14);
        }
      }
      else if ((oldbvo.getBifinventoryfinish() != null) && (!oldbvo.getBifinventoryfinish().equals(curordbvos[i].getBifinventoryfinish())))
      {
        if ((curordbvos[i].getBifinventoryfinish() != null) && (curordbvos[i].getBifinventoryfinish().booleanValue()))
        {
          outendlist.add(oldbvo);
          oldbvo.setIAction(12);
        }
        else {
          outopenlist.add(oldbvo);
          oldbvo.setIAction(14);
        }

      }
      else if ((curordbvos[i].getBifreceiptfinish() != null) && (!curordbvos[i].getBifreceiptfinish().equals(oldbvo.getBifreceiptfinish())))
      {
        if ((curordbvos[i].getBifreceiptfinish() != null) && (curordbvos[i].getBifreceiptfinish().booleanValue()))
        {
          sendendlist.add(oldbvo);
          oldbvo.setIAction(13);
        }
        else {
          sendopenlist.add(oldbvo);
          oldbvo.setIAction(15);
        }
      } else {
        if ((oldbvo.getBifreceiptfinish() == null) || (oldbvo.getBifreceiptfinish().equals(curordbvos[i].getBifreceiptfinish())))
        {
          continue;
        }
        if ((curordbvos[i].getBifreceiptfinish() != null) && (curordbvos[i].getBifreceiptfinish().booleanValue()))
        {
          sendendlist.add(oldbvo);
          oldbvo.setIAction(13);
        }
        else {
          sendopenlist.add(oldbvo);
          oldbvo.setIAction(15);
        }

      }

    }

    SOATP soatp = new SOATP();

    SaleOrderVO[] ordvos = toSaleOrderVO(ordhvos, outendlist);
    if (ordvos != null) {
        i = 0; for (int loop = ordvos.length; i < loop; i++) {
        soatp.modifyATPWhenCloseBill(ordvos[i]);
      }

    }

    ordvos = toSaleOrderVO(ordhvos, outopenlist);
    if (ordvos != null) {
        i = 0; for (int loop = ordvos.length; i < loop; i++) {
        soatp.modifyATPWhenOpenBill(ordvos[i]);
      }

    }

    ordvos = toSaleOrderVO(ordhvos, sendendlist);
    if (ordvos != null) {
        i = 0; for (int loop = ordvos.length; i < loop; i++) {
        soatp.modifyATPWhenCloseBill(ordvos[i]);
      }

    }

    ordvos = toSaleOrderVO(ordhvos, sendopenlist);
    if (ordvos != null) {
        i = 0; for (int loop = ordvos.length; i < loop; i++)
        soatp.modifyATPWhenOpenBill(ordvos[i]);
    }
  }

  public void updateAdjustForEnd(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
    throws NamingException, BusinessException, SQLException, RemoteException
  {
    if ((ordhvos == null) || (oldordbvos == null) || (curordbvos == null)) {
      return;
    }
    HashMap hsoldbvos = new HashMap();
    int i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
      hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
    }

    ArrayList endlist = new ArrayList(curordbvos.length);

    ArrayList openlist = new ArrayList(curordbvos.length);

    SaleorderBVO oldbvo = null;
      i = 0; for (int loop = curordbvos.length; i < loop; i++) {
      oldbvo = (SaleorderBVO)hsoldbvos.get(curordbvos[i].getCorder_bid());

      if (oldbvo == null)
      {
        continue;
      }
      if ((curordbvos[i].getFrowstatus().intValue() == 6) && (oldbvo.getFrowstatus().intValue() == 2))
      {
        endlist.add(oldbvo);
      }

      if ((curordbvos[i].getFrowstatus().intValue() != 2) || (oldbvo.getFrowstatus().intValue() != 6)) {
        continue;
      }
      openlist.add(oldbvo);
    }

    if (endlist.size() > 0) {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, endlist);

      incomeAdjust_endForOrder(ordhvos, endlist);
    }

    if (openlist.size() > 0) {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, openlist);

      incomeAdjust_unEndForOrder(ordhvos, openlist);
    }
  }

  public void updateOrdBalanceByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
    throws NamingException, BusinessException, SQLException, RemoteException
  {
    if ((ordhvos == null) || (oldordbvos == null) || (curordbvos == null)) {
      return;
    }
    HashMap hsoldbvos = new HashMap();
    int i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
      hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
    }

    ArrayList outendlist = new ArrayList();

    SaleorderBVO oldbvo = null;
      i = 0; for (int loop = curordbvos.length; i < loop; i++) {
      oldbvo = (SaleorderBVO)hsoldbvos.get(curordbvos[i].getCorder_bid());

      if (oldbvo == null) {
        continue;
      }
      if ((curordbvos[i].getBifinventoryfinish() != null) && (!curordbvos[i].getBifinventoryfinish().equals(oldbvo.getBifinventoryfinish())))
      {
        if ((curordbvos[i].getBifinventoryfinish() == null) || (!curordbvos[i].getBifinventoryfinish().booleanValue()))
          continue;
        outendlist.add(oldbvo);
      }
      else if ((oldbvo.getBifinventoryfinish() != null) && (!oldbvo.getBifinventoryfinish().equals(curordbvos[i].getBifinventoryfinish())))
      {
        if ((curordbvos[i].getBifinventoryfinish() == null) || (!curordbvos[i].getBifinventoryfinish().booleanValue()))
          continue;
        outendlist.add(oldbvo);
      }
      else
      {
        if ((curordbvos[i].getFrowstatus().intValue() != 6) || (oldbvo.getFrowstatus().intValue() != 2)) {
          continue;
        }
        outendlist.add(oldbvo);
      }

    }

    if (outendlist.size() > 0) {
      SaleOrderVO[] vos = toSaleOrderVO(ordhvos, outendlist);
      BalanceDMO baldmo = new BalanceDMO();
      Integer ioutendaction = new Integer(12);
        i = 0; for (int loop = vos.length; i < loop; i++)
      {
        baldmo.updateSoBalance(vos[i], ioutendaction);
      }
    }
  }

  private void checkRecStoreStructure(SaleOrderVO saleVO, String newHeadID)
    throws SQLException, BusinessException
  {
    if ((saleVO == null) || (!saleVO.isMultCorpOrd())) {
      return;
    }
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT DISTINCT so_saleorder_b.cinventoryid, bd_produce.pk_calbody ");

    sql.append("FROM so_saleorder_b LEFT OUTER JOIN ");
    sql.append("bd_produce ON bd_produce.pk_invmandoc = so_saleorder_b.cinventoryid AND ");

    sql.append("bd_produce.pk_calbody = so_saleorder_b.creccalbodyid ");
    sql.append("where so_saleorder_b.csaleid=?");

    SaleorderBVO[] saleBVOs = (SaleorderBVO[])(SaleorderBVO[])saleVO.getChildrenVO();
    Connection con = null;
    PreparedStatement stmt = null;
    if (saleBVOs != null)
      try {
        StringBuffer sbfErr = new StringBuffer();
        HashMap htTemp = new HashMap();
        String invid = null;
        String calbodyid = null;
        con = getConnection();
        stmt = con.prepareStatement(sql.toString());
        stmt.setString(1, newHeadID);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          invid = rs.getString(1);
          invid = invid == null ? null : invid.trim();
          calbodyid = rs.getString(2);
          calbodyid = calbodyid == null ? null : calbodyid.trim();
          if ((invid == null) || (invid.length() <= 0) || (calbodyid == null) || (calbodyid.length() <= 0))
            continue;
          htTemp.put(invid, calbodyid);
        }

        rs.close();
        Object oTemp = null;
        for (int i = 0; i < saleBVOs.length; i++)
        {
          if (saleBVOs[i].getStatus() == 3) {
            continue;
          }
          if ((saleBVOs[i].getLaborflag() != null) && (saleBVOs[i].getLaborflag().booleanValue()))
          {
            continue;
          }
          if ((saleBVOs[i].getDiscountflag() != null) && (saleBVOs[i].getDiscountflag().booleanValue()))
          {
            continue;
          }
          if ((saleBVOs[i].getBoosflag() != null) && (saleBVOs[i].getBoosflag().booleanValue())) {
            continue;
          }
          if ((saleBVOs[i].getCreccalbodyid() == null) || (saleBVOs[i].getCreccalbodyid().trim().length() <= 0))
          {
            continue;
          }

          oTemp = htTemp.get(saleBVOs[i].getCinventoryid());
          if (oTemp == null) {
            sbfErr.append(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000040", null, new String[] { saleBVOs[i].getCrowno() == null ? "" : saleBVOs[i].getCrowno() }));

            sbfErr.append("\n");
          }

        }

        if (sbfErr.toString().trim().length() > 0)
          throw new BusinessException(sbfErr.toString());
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
  }

  private void getOrdBAndExeValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo)
    throws SQLException
  {
    if ((rs == null) || (bvo == null)) {
      return;
    }

    if (starpos < 0)
    {
      getOrdBValueFromResultSet(rs, -1, bvo);

      getOrdExeValueFromResultSet(rs, -1, bvo);
    }
    else
    {
      int pos = getOrdBValueFromResultSet(rs, starpos, bvo);

      getOrdExeValueFromResultSet(rs, pos + 1, bvo);
    }
  }

  private int getOrdBValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo)
    throws SQLException
  {
    if ((rs == null) || (bvo == null)) {
      return starpos;
    }

    String stemp = null;
    BigDecimal dectemp = null;

    if (starpos < 0)
    {
      stemp = rs.getString("cconsigncorpid");
      bvo.setCconsigncorpid(stemp == null ? null : stemp.trim());

      dectemp = (BigDecimal)rs.getObject("nreturntaxrate");
      bvo.setNreturntaxrate(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("creccalbodyid");
      bvo.setCreccalbodyid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("crecwareid");
      bvo.setCrecwareid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("bdericttrans");
      bvo.setBdericttrans(stemp == null ? null : new UFBoolean(stemp.trim()));

      stemp = rs.getString("tconsigntime");
      bvo.setTconsigntime(stemp == null ? null : stemp.trim());

      stemp = rs.getString("tdelivertime");
      bvo.setTdelivertime(stemp == null ? null : stemp.trim());

      stemp = rs.getString("bsafeprice");
      bvo.setBsafeprice(stemp == null ? null : new UFBoolean(stemp.trim()));

      dectemp = (BigDecimal)rs.getObject("ntaldcnum");
      bvo.setNtaldcnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nasttaldcnum");
      bvo.setNasttaldcnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntaldcmny");
      bvo.setNtaldcmny(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("breturnprofit");
      bvo.setBreturnprofit(stemp == null ? null : new UFBoolean(stemp.trim()));

      dectemp = (BigDecimal)rs.getObject("nretprofnum");
      bvo.setNretprofnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nastretprofnum");
      bvo.setNastretprofnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nretprofmny");
      bvo.setNretprofmny(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("cpricepolicyid");
      bvo.setCpricepolicyid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cpriceitemid");
      bvo.setCpriceitemid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cpriceitemtable");
      bvo.setCpriceitemtable(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cpricecalproc");
      bvo.setCpricecalproc(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cquoteunitid");
      bvo.setCquoteunitid(stemp == null ? null : stemp.trim());

      dectemp = (BigDecimal)rs.getObject("nquoteunitnum");
      bvo.setNquoteunitnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("norgqttaxprc");
      bvo.setNorgqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("norgqtprc");
      bvo.setNorgqtprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("norgqttaxnetprc");
      bvo.setNorgqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("norgqtnetprc");
      bvo.setNorgqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nqttaxnetprc");
      bvo.setNqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nqtnetprc");
      bvo.setNqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nqttaxprc");
      bvo.setNqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nqtprc");
      bvo.setNqtprc(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("cprolineid");
      bvo.setCprolineid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("crecaddrnode");
      bvo.setCrecaddrnode(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cinventoryid1");
      bvo.setCinventoryid1(stemp == null ? null : stemp.trim());

      stemp = rs.getString("cchantypeid");
      bvo.setCchantypeid(stemp == null ? null : stemp.trim());

      dectemp = (BigDecimal)rs.getObject("nqtorgprc");
      bvo.setNqtorgprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nqtorgtaxprc");
      bvo.setNqtorgtaxprc(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("nquoteunitrate");
      bvo.setNqtscalefactor(dectemp == null ? null : new UFDouble(dectemp));
    }
    else
    {
      stemp = rs.getString(starpos);
      bvo.setCconsigncorpid(stemp == null ? null : stemp.trim());

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNreturntaxrate(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; stemp = rs.getString(starpos);
      bvo.setCreccalbodyid(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCrecwareid(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setBdericttrans(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++; stemp = rs.getString(starpos);
      bvo.setTconsigntime(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setTdelivertime(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setBsafeprice(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaldcnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNasttaldcnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaldcmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; stemp = rs.getString(starpos);
      bvo.setBreturnprofit(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNretprofnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNastretprofnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNretprofmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; stemp = rs.getString(starpos);
      bvo.setCpricepolicyid(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCpriceitemid(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCpriceitemtable(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCpricecalproc(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCquoteunitid(stemp == null ? null : stemp.trim());

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNquoteunitnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNorgqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNorgqtprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNorgqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNorgqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqtprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; stemp = rs.getString(starpos);
      bvo.setCprolineid(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCrecaddrnode(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCinventoryid1(stemp == null ? null : stemp.trim());

      starpos++; stemp = rs.getString(starpos);
      bvo.setCchantypeid(stemp == null ? null : stemp.trim());

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqtorgprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqtorgtaxprc(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNqtscalefactor(dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setAttributeValue("nouttoplimit", dectemp == null ? null : new UFDouble(dectemp));

      starpos++; dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setAttributeValue("noutcloselimit", dectemp == null ? null : new UFDouble(dectemp));

      starpos++; stemp = rs.getString(starpos);
      bvo.setAttributeValue("clargessrowno", stemp == null ? null : stemp.trim());
    }

    return starpos;
  }

  private int getOrdExeValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo)
    throws SQLException
  {
    if ((rs == null) || (bvo == null)) {
      return starpos;
    }

    String stemp = null;
    BigDecimal dectemp = null;

    if (starpos < 0)
    {
      dectemp = (BigDecimal)rs.getObject("ntalplconsigmny");
      bvo.setNtalplconsigmny(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntaltransnum");
      bvo.setNtaltransnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntaltransmny");
      bvo.setNtaltransmny(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntaloutmny");
      bvo.setNtaloutmny(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntalsignmny");
      bvo.setNtalsignmny(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntalbalancemny");
      bvo.setNtalbalancemny(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntaltransretnum");
      bvo.setNtaltransretnum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("ntranslossnum");
      bvo.setNtranslossnum(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("biftransfinish");
      bvo.setBiftransfinish(stemp == null ? null : new UFBoolean(stemp.trim()));

      stemp = rs.getString("dlastconsigdate");
      bvo.setDlastconsigdate(stemp == null ? null : new UFDate(stemp.trim()));

      stemp = rs.getString("dlasttransdate");
      bvo.setDlasttransdate(stemp == null ? null : new UFDate(stemp.trim()));

      stemp = rs.getString("dlastoutdate");
      bvo.setDlastoutdate(stemp == null ? null : new UFDate(stemp.trim()));

      stemp = rs.getString("dlastinvoicedt");
      bvo.setDlastinvoicedt(stemp == null ? null : new UFDate(stemp.trim()));

      stemp = rs.getString("dlastpaydate");
      bvo.setDlastpaydate(stemp == null ? null : new UFDate(stemp.trim()));

      stemp = rs.getString("vdef7");
      bvo.setVdef7(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef8");
      bvo.setVdef8(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef9");
      bvo.setVdef9(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef10");
      bvo.setVdef10(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef11");
      bvo.setVdef11(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef12");
      bvo.setVdef12(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef13");
      bvo.setVdef13(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef14");
      bvo.setVdef14(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef15");
      bvo.setVdef15(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef16");
      bvo.setVdef16(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef17");
      bvo.setVdef17(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef18");
      bvo.setVdef18(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef19");
      bvo.setVdef19(stemp == null ? null : stemp.trim());

      stemp = rs.getString("vdef20");
      bvo.setVdef20(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc1");
      bvo.setPk_defdoc1(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc2");
      bvo.setPk_defdoc2(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc3");
      bvo.setPk_defdoc3(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc4");
      bvo.setPk_defdoc4(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc5");
      bvo.setPk_defdoc5(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc6");
      bvo.setPk_defdoc6(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc7");
      bvo.setPk_defdoc7(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc8");
      bvo.setPk_defdoc8(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc9");
      bvo.setPk_defdoc9(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc10");
      bvo.setPk_defdoc10(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc11");
      bvo.setPk_defdoc11(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc12");
      bvo.setPk_defdoc12(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc13");
      bvo.setPk_defdoc13(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc14");
      bvo.setPk_defdoc14(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc15");
      bvo.setPk_defdoc15(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc16");
      bvo.setPk_defdoc16(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc17");
      bvo.setPk_defdoc17(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc18");
      bvo.setPk_defdoc18(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc19");
      bvo.setPk_defdoc19(stemp == null ? null : stemp.trim());

      stemp = rs.getString("pk_defdoc20");
      bvo.setPk_defdoc20(stemp == null ? null : stemp.trim());

      dectemp = (BigDecimal)rs.getObject("narrangescornum");
      bvo.setNarrangescornum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("narrangepoapplynum");
      bvo.setNarrangepoapplynum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("narrangetoornum");
      bvo.setNarrangetoornum(dectemp == null ? null : new UFDouble(dectemp));

      dectemp = (BigDecimal)rs.getObject("norrangetoapplynum");
      bvo.setNorrangetoapplynum(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("barrangedflag");
      bvo.setBarrangedflag(stemp == null ? null : new UFBoolean(stemp.trim()));

      stemp = rs.getString("carrangepersonid");
      bvo.setCarrangepersonid(stemp == null ? null : stemp.trim());

      stemp = rs.getString("tlastarrangetime");
      bvo.setTlastarrangetime(stemp == null ? null : new UFDateTime(stemp.trim()));

      dectemp = (BigDecimal)rs.getObject("narrangemonum");
      bvo.setNarrangemonum(dectemp == null ? null : new UFDouble(dectemp));
    }
    else
    {
      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtalplconsigmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaltransnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaltransmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaloutmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtalsignmny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtalbalancemny(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtaltransretnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNtranslossnum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setBiftransfinish(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setDlastconsigdate(stemp == null ? null : new UFDate(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setDlasttransdate(stemp == null ? null : new UFDate(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setDlastoutdate(stemp == null ? null : new UFDate(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setDlastinvoicedt(stemp == null ? null : new UFDate(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setDlastpaydate(stemp == null ? null : new UFDate(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef7(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef8(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef9(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef10(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef11(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef12(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef13(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef14(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef15(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef16(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef17(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef18(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef19(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setVdef20(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc1(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc2(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc3(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc4(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc5(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc6(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc7(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc8(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc9(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc10(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc11(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc12(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc13(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc14(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc15(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc16(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc17(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc18(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc19(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setPk_defdoc20(stemp == null ? null : stemp.trim());

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNarrangescornum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNarrangepoapplynum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNarrangetoornum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNorrangetoapplynum(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setBarrangedflag(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setCarrangepersonid(stemp == null ? null : stemp.trim());

      starpos++;

      stemp = rs.getString(starpos);
      bvo.setTlastarrangetime(stemp == null ? null : new UFDateTime(stemp.trim()));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      bvo.setNarrangemonum(dectemp == null ? null : new UFDouble(dectemp));
    }

    return starpos;
  }

  public HashMap queryCachPayByOrdIds(String[] csaleids)
    throws BusinessException
  {
    if ((csaleids == null) || (csaleids.length <= 0)) {
      return null;
    }
    OrdVO[] balvos = null;
    try {
      BalanceDMO baldmo = new BalanceDMO();
      balvos = baldmo.queryOrdVO(" so_sale.csaleid  in " + GeneralSqlString.formSubSql("so_balance.csaleid", csaleids));
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
    HashMap retvalues = new HashMap();
    if ((balvos != null) && (balvos.length > 0)) {
      for (int i = 0; i < balvos.length; i++) {
        retvalues.put(balvos[i].getCsaleid(), balvos[i].getNorigbalsummny());
      }
    }

    return retvalues;
  }

  private int getOrdHValueFromResultSet(ResultSet rs, int starpos, SaleorderHVO hvo)
    throws SQLException
  {
    if ((rs == null) || (hvo == null)) {
      return starpos;
    }

    String stemp = null;
    BigDecimal dectemp = null;
    Integer itemp = null;

    if (starpos < 0)
    {
      stemp = rs.getString("btransendflag");
      hvo.setBtransendflag(stemp == null ? null : new UFBoolean(stemp.trim()));

      dectemp = (BigDecimal)rs.getObject("naccountperiod");
      hvo.setNaccountperiod(dectemp == null ? null : new UFDouble(dectemp));

      stemp = rs.getString("boverdate");
      hvo.setBoverdate(stemp == null ? null : new UFBoolean(stemp.trim()));

      itemp = (Integer)rs.getObject("iprintcount");
      hvo.setIprintcount(itemp == null ? null : itemp);

      stemp = rs.getString("bdeliver");
      hvo.setBdeliver(stemp == null ? null : new UFBoolean(stemp.trim()));

      stemp = rs.getString("vdef11");
      hvo.setVdef11(stemp == null ? null : stemp);

      stemp = rs.getString("vdef12");
      hvo.setVdef12(stemp == null ? null : stemp);

      stemp = rs.getString("vdef13");
      hvo.setVdef13(stemp == null ? null : stemp);

      stemp = rs.getString("vdef14");
      hvo.setVdef14(stemp == null ? null : stemp);

      stemp = rs.getString("vdef15");
      hvo.setVdef15(stemp == null ? null : stemp);

      stemp = rs.getString("vdef16");
      hvo.setVdef16(stemp == null ? null : stemp);

      stemp = rs.getString("vdef17");
      hvo.setVdef17(stemp == null ? null : stemp);

      stemp = rs.getString("vdef18");
      hvo.setVdef18(stemp == null ? null : stemp);

      stemp = rs.getString("vdef19");
      hvo.setVdef19(stemp == null ? null : stemp);

      stemp = rs.getString("vdef20");
      hvo.setVdef20(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc1");
      hvo.setPk_defdoc1(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc2");
      hvo.setPk_defdoc2(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc3");
      hvo.setPk_defdoc3(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc4");
      hvo.setPk_defdoc4(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc5");
      hvo.setPk_defdoc5(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc6");
      hvo.setPk_defdoc6(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc7");
      hvo.setPk_defdoc7(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc8");
      hvo.setPk_defdoc8(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc9");
      hvo.setPk_defdoc9(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc10");
      hvo.setPk_defdoc10(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc11");
      hvo.setPk_defdoc11(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc12");
      hvo.setPk_defdoc12(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc13");
      hvo.setPk_defdoc13(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc14");
      hvo.setPk_defdoc14(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc15");
      hvo.setPk_defdoc15(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc16");
      hvo.setPk_defdoc16(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc17");
      hvo.setPk_defdoc17(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc18");
      hvo.setPk_defdoc18(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc19");
      hvo.setPk_defdoc19(stemp == null ? null : stemp);

      stemp = rs.getString("pk_defdoc20");
      hvo.setPk_defdoc20(stemp == null ? null : stemp);

      stemp = rs.getString("dbilltime");
      hvo.setAttributeValue("dbilltime", stemp == null ? null : new UFDateTime(stemp));

      stemp = rs.getString("daudittime");
      hvo.setAttributeValue("daudittime", stemp == null ? null : new UFDateTime(stemp));

      stemp = rs.getString("dmoditime");
      hvo.setAttributeValue("dmoditime", stemp == null ? null : new UFDateTime(stemp));
    }
    else
    {
      stemp = rs.getString(starpos);
      hvo.setBtransendflag(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++;

      dectemp = (BigDecimal)rs.getObject(starpos);
      hvo.setNaccountperiod(dectemp == null ? null : new UFDouble(dectemp));

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setBoverdate(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++;

      itemp = (Integer)rs.getObject(starpos);
      hvo.setIprintcount(itemp == null ? null : itemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setBdeliver(stemp == null ? null : new UFBoolean(stemp.trim()));

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef11(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef12(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef13(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef14(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef15(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef16(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef17(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef18(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef19(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setVdef20(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc1(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc2(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc3(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc4(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc5(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc6(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc7(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc8(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc9(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc10(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc11(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc12(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc13(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc14(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc15(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc16(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc17(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc18(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc19(stemp == null ? null : stemp);

      starpos++;

      stemp = rs.getString(starpos);
      hvo.setPk_defdoc20(stemp == null ? null : stemp);
      starpos++;
      stemp = rs.getString(starpos);
      hvo.setAttributeValue("dbilltime", stemp == null ? null : new UFDateTime(stemp));

      starpos++;
      stemp = rs.getString(starpos);
      hvo.setAttributeValue("daudittime", stemp == null ? null : new UFDateTime(stemp));

      starpos++;
      stemp = rs.getString(starpos);
      hvo.setAttributeValue("dmoditime", stemp == null ? null : new UFDateTime(stemp));
    }

    return starpos;
  }

  private int setPreStatementOrdB(PreparedStatement statement, int starpos, SaleorderBVO bvo)
    throws SQLException, nc.bs.pub.SystemException
  {
    if ((statement == null) || (bvo == null)) {
      return starpos;
    }

    if (bvo.getCconsigncorpid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCconsigncorpid());
    }

    starpos++;

    if (bvo.getNreturntaxrate() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNreturntaxrate().toBigDecimal());
    }

    starpos++;

    if (bvo.getCreccalbodyid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCreccalbodyid());
    }

    starpos++;

    if (bvo.getCrecwareid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCrecwareid());
    }

    starpos++;

    if (bvo.getBdericttrans() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, bvo.getBdericttrans().toString());
    }

    starpos++;

    if (bvo.getTconsigntime() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getTconsigntime());
    }

    starpos++;

    if (bvo.getTdelivertime() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getTdelivertime());
    }

    starpos++;

    if (bvo.getBsafeprice() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, bvo.getBsafeprice().toString());
    }

    starpos++;

    if (bvo.getNtaldcnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaldcnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNasttaldcnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNasttaldcnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtaldcmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaldcmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getBreturnprofit() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, bvo.getBreturnprofit().toString());
    }

    starpos++;

    if (bvo.getNretprofnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNretprofnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNastretprofnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNastretprofnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNretprofmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNretprofmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getCpricepolicyid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCpricepolicyid());
    }

    starpos++;

    if (bvo.getCpriceitemid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCpriceitemid());
    }

    starpos++;

    if (bvo.getCpriceitemtable() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCpriceitemtable());
    }

    starpos++;

    if (bvo.getCpricecalproc() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCpricecalproc());
    }

    starpos++;

    if (bvo.getCquoteunitid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCquoteunitid());
    }

    starpos++;

    if (bvo.getNquoteunitnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNquoteunitnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNorgqttaxprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNorgqttaxprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNorgqtprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNorgqtprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNorgqttaxnetprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNorgqttaxnetprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNorgqtnetprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNorgqtnetprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNqttaxnetprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqttaxnetprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNqtnetprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqtnetprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNqttaxprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqttaxprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNqtprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqtprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getCprolineid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCprolineid());
    }

    starpos++;

    if (bvo.getCrecaddrnode() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCrecaddrnode());
    }

    starpos++;

    if (bvo.getCinventoryid1() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCinventoryid1());
    }

    starpos++;

    if (bvo.getCchantypeid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCchantypeid());
    }

    starpos++;

    if (bvo.getNqtorgprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqtorgprc().toBigDecimal());
    }

    starpos++;

    if (bvo.getNqtorgtaxprc() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNqtorgtaxprc().toBigDecimal());
    }

    starpos++;

    UFDouble nqtscalefactor = bvo.getNqtscalefactor();
    if (nqtscalefactor == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, nqtscalefactor.toBigDecimal());
    }

    starpos++;

    UFDouble nouttoplimit = (UFDouble)bvo.getAttributeValue("nouttoplimit");

    if (nouttoplimit == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, nouttoplimit.toBigDecimal());
    }
    starpos++;

    UFDouble noutcloselimit = (UFDouble)bvo.getAttributeValue("noutcloselimit");

    if (noutcloselimit == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, noutcloselimit.toBigDecimal());
    }
    starpos++;

    String clargessrowno = (String)bvo.getAttributeValue("clargessrowno");
    if (clargessrowno == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, clargessrowno);
    }
    starpos++;
    
    UFDouble tsbl = (UFDouble)bvo.getAttributeValue("tsbl");
    if (tsbl == null)
      statement.setNull(starpos, 1);
    else {
      statement.setDouble(starpos, tsbl.doubleValue());
    }

    return starpos;
  }

  private int setPreStatementOrdE(PreparedStatement statement, int starpos, SaleorderBVO bvo)
    throws SQLException, nc.bs.pub.SystemException
  {
    if ((statement == null) || (bvo == null)) {
      return starpos;
    }

    if (bvo.getNtalplconsigmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtalplconsigmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtaltransnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaltransnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtaltransmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaltransmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtaloutmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaloutmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtalsignmny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtalsignmny().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtalbalancemny() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtalbalancemny().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtaltransretnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtaltransretnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNtranslossnum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNtranslossnum().toBigDecimal());
    }

    starpos++;

    if (bvo.getBiftransfinish() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, bvo.getBiftransfinish().toString());
    }

    starpos++;

    if (bvo.getDlastconsigdate() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getDlastconsigdate().toString());
    }

    starpos++;

    if (bvo.getDlasttransdate() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getDlasttransdate().toString());
    }

    starpos++;

    if (bvo.getDlastoutdate() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getDlastoutdate().toString());
    }

    starpos++;

    if (bvo.getDlastinvoicedt() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getDlastinvoicedt().toString());
    }

    starpos++;

    if (bvo.getDlastpaydate() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getDlastpaydate().toString());
    }

    starpos++;

    if (bvo.getVdef7() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef7());
    }

    starpos++;

    if (bvo.getVdef8() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef8());
    }

    starpos++;

    if (bvo.getVdef9() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef9());
    }

    starpos++;

    if (bvo.getVdef10() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef10());
    }

    starpos++;

    if (bvo.getVdef11() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef11());
    }

    starpos++;

    if (bvo.getVdef12() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef12());
    }

    starpos++;

    if (bvo.getVdef13() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef13());
    }

    starpos++;

    if (bvo.getVdef14() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef14());
    }

    starpos++;

    if (bvo.getVdef15() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef15());
    }

    starpos++;

    if (bvo.getVdef16() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef16());
    }

    starpos++;

    if (bvo.getVdef17() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef17());
    }

    starpos++;

    if (bvo.getVdef18() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef18());
    }

    starpos++;

    if (bvo.getVdef19() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef19());
    }

    starpos++;

    if (bvo.getVdef20() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getVdef20());
    }

    starpos++;

    if (bvo.getPk_defdoc1() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc1());
    }

    starpos++;

    if (bvo.getPk_defdoc2() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc2());
    }

    starpos++;

    if (bvo.getPk_defdoc3() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc3());
    }

    starpos++;

    if (bvo.getPk_defdoc4() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc4());
    }

    starpos++;

    if (bvo.getPk_defdoc5() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc5());
    }

    starpos++;

    if (bvo.getPk_defdoc6() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc6());
    }

    starpos++;

    if (bvo.getPk_defdoc7() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc7());
    }

    starpos++;

    if (bvo.getPk_defdoc8() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc8());
    }

    starpos++;

    if (bvo.getPk_defdoc9() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc9());
    }

    starpos++;

    if (bvo.getPk_defdoc10() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc10());
    }

    starpos++;

    if (bvo.getPk_defdoc11() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc11());
    }

    starpos++;

    if (bvo.getPk_defdoc12() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc12());
    }

    starpos++;

    if (bvo.getPk_defdoc13() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc13());
    }

    starpos++;

    if (bvo.getPk_defdoc14() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc14());
    }

    starpos++;

    if (bvo.getPk_defdoc15() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc15());
    }

    starpos++;

    if (bvo.getPk_defdoc16() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc16());
    }

    starpos++;

    if (bvo.getPk_defdoc17() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc17());
    }

    starpos++;

    if (bvo.getPk_defdoc18() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc18());
    }

    starpos++;

    if (bvo.getPk_defdoc19() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc19());
    }

    starpos++;

    if (bvo.getPk_defdoc20() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getPk_defdoc20());
    }

    starpos++;

    if (bvo.getNarrangescornum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNarrangescornum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNarrangepoapplynum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNarrangepoapplynum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNarrangetoornum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNarrangetoornum().toBigDecimal());
    }

    starpos++;

    if (bvo.getNorrangetoapplynum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNorrangetoapplynum().toBigDecimal());
    }

    starpos++;

    if (bvo.getBarrangedflag() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, bvo.getBarrangedflag().toString());
    }

    starpos++;

    if (bvo.getCarrangepersonid() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getCarrangepersonid());
    }

    starpos++;

    if (bvo.getTlastarrangetime() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, bvo.getTlastarrangetime().toString());
    }

    starpos++;

    if (bvo.getNarrangemonum() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, bvo.getNarrangemonum().toBigDecimal());
    }

    return starpos;
  }

  private int setPreStatementOrdH(PreparedStatement statement, int starpos, SaleorderHVO hvo)
    throws SQLException, nc.bs.pub.SystemException
  {
    if ((statement == null) || (hvo == null)) {
      return starpos;
    }

    if (hvo.getBtransendflag() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, hvo.getBtransendflag().toString());
    }

    starpos++;

    if (hvo.getNaccountperiod() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setBigDecimal(starpos, hvo.getNaccountperiod().toBigDecimal());
    }

    starpos++;

    if (hvo.getBoverdate() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, hvo.getBoverdate().toString());
    }

    starpos++;

    if (hvo.getIprintcount() == null)
      statement.setNull(starpos, 4);
    else {
      statement.setInt(starpos, hvo.getIprintcount().intValue());
    }

    starpos++;

    if (hvo.getBdeliver() == null)
      statement.setString(starpos, "N");
    else {
      statement.setString(starpos, hvo.getBdeliver().toString());
    }

    starpos++;

    if (hvo.getVdef11() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef11());
    }

    starpos++;

    if (hvo.getVdef12() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef12());
    }

    starpos++;

    if (hvo.getVdef13() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef13());
    }

    starpos++;

    if (hvo.getVdef14() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef14());
    }

    starpos++;

    if (hvo.getVdef15() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef15());
    }

    starpos++;

    if (hvo.getVdef16() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef16());
    }

    starpos++;

    if (hvo.getVdef17() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef17());
    }

    starpos++;

    if (hvo.getVdef18() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef18());
    }

    starpos++;

    if (hvo.getVdef19() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef19());
    }

    starpos++;

    if (hvo.getVdef20() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getVdef20());
    }

    starpos++;

    if (hvo.getPk_defdoc1() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc1());
    }

    starpos++;

    if (hvo.getPk_defdoc2() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc2());
    }

    starpos++;

    if (hvo.getPk_defdoc3() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc3());
    }

    starpos++;

    if (hvo.getPk_defdoc4() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc4());
    }

    starpos++;

    if (hvo.getPk_defdoc5() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc5());
    }

    starpos++;

    if (hvo.getPk_defdoc6() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc6());
    }

    starpos++;

    if (hvo.getPk_defdoc7() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc7());
    }

    starpos++;

    if (hvo.getPk_defdoc8() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc8());
    }

    starpos++;

    if (hvo.getPk_defdoc9() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc9());
    }

    starpos++;

    if (hvo.getPk_defdoc10() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc10());
    }

    starpos++;

    if (hvo.getPk_defdoc11() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc11());
    }

    starpos++;

    if (hvo.getPk_defdoc12() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc12());
    }

    starpos++;

    if (hvo.getPk_defdoc13() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc13());
    }

    starpos++;

    if (hvo.getPk_defdoc14() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc14());
    }

    starpos++;

    if (hvo.getPk_defdoc15() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc15());
    }

    starpos++;

    if (hvo.getPk_defdoc16() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc16());
    }

    starpos++;

    if (hvo.getPk_defdoc17() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc17());
    }

    starpos++;

    if (hvo.getPk_defdoc18() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc18());
    }

    starpos++;

    if (hvo.getPk_defdoc19() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc19());
    }

    starpos++;

    if (hvo.getPk_defdoc20() == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getPk_defdoc20());
    }
    starpos++;

    UFDateTime serverTime = new UFDateTime(System.currentTimeMillis());

    if (hvo.getAttributeValue("dbilltime") == null)
      statement.setString(starpos, serverTime.toString());
    else {
      statement.setString(starpos, hvo.getAttributeValue("dbilltime").toString());
    }

    starpos++;

    if (hvo.getAttributeValue("daudittime") == null)
      statement.setNull(starpos, 1);
    else {
      statement.setString(starpos, hvo.getAttributeValue("daudittime").toString());
    }

    starpos++;

    if (hvo.getAttributeValue("dmoditime") == null)
      statement.setString(starpos, serverTime.toString());
    else {
      statement.setString(starpos, hvo.getAttributeValue("dmoditime").toString());
    }

    return starpos;
  }

  public void checkSaleOrderVO(SaleOrderVO vo)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000041"));
    }

    SaleorderHVO ordhvo = vo.getHeadVO();
    SaleorderBVO[] ordbvos = vo.getBodyVOs();

    if (ordhvo == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000042"));
    }

    if ((ordbvos == null) || (ordbvos.length <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000043"));
    }

    String pk_corp = ordhvo.getPk_corp();

    if ((pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000044"));
    }

    String[] paracodes = { "SO01", "SO03", "SO27" };

    SysInitDMO sysdmo = new SysInitDMO();
    Hashtable h = sysdmo.queryBatchParaValues(pk_corp, paracodes);

    Integer SO01 = null;

    String str = (String)h.get("SO01");
    if ((str != null) && (str.length() >= 0)) {
      SO01 = new Integer(str);
    }
    UFBoolean SO03 = new UFBoolean(true);
    str = (String)h.get("SO03");
    if ((str != null) && (str.length() >= 0)) {
      SO03 = new UFBoolean(str);
    }
    UFBoolean SO27 = new UFBoolean(false);
    str = (String)h.get("SO27");
    if ((str != null) && (str.length() >= 0)) {
      SO27 = new UFBoolean(str);
    }
    if ((SO01 != null) && (SO01.intValue() > 0) && 
      (ordbvos.length > SO01.intValue())) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000045", null, new String[] { SO01.toString() }));
    }

    String cprodline = null;
    String cinvtoryid = null;

    ArrayList cprodlinelist = new ArrayList();
    ArrayList cinvtoryidlist = new ArrayList();

    int i = 0; for (int loop = ordbvos.length; i < loop; i++)
    {
      cprodline = ordbvos[i].getCprolineid();
      cinvtoryid = ordbvos[i].getCinventoryid();

      if ((cprodline != null) && (cprodline.trim().length() <= 0)) {
        cprodline = null;
      }
      if ((cinvtoryid == null) && (cinvtoryid.trim().length() <= 0)) {
        throw new ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000047"));
      }

      if ((SO27.booleanValue()) && (cprodlinelist.size() > 0) && (!cprodlinelist.contains(cprodline)))
      {
        throw new ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000048"));
      }

      if ((!SO03.booleanValue()) && (cinvtoryidlist.size() > 0) && (cinvtoryidlist.contains(cinvtoryid)))
      {
        throw new ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000049"));
      }

      cprodlinelist.add(cprodline);

      cinvtoryidlist.add(cinvtoryid);
    }
  }

  public SaleOrderVO getNeedUpdateAtpVOWhenModifyOrd(SaleOrderVO ordvo)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    return ordvo;
  }

  public ATPVO[] getUpdateOtherCorpAtpVO(SaleOrderVO ordvo)
  {
    return null;
  }

  public void processSaleOrderVO(SaleOrderVO vo)
    throws NamingException, BusinessException, SQLException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null) {
      return;
    }
    SaleorderHVO ordhvo = vo.getHeadVO();
    SaleorderBVO[] ordbvos = vo.getBodyVOs();

    if ((ordhvo == null) || (ordbvos == null) || (ordbvos.length <= 0)) {
      return;
    }
    if ((ordhvo.getCcustomerid() == null) || (ordhvo.getCcustomerid().trim().length() <= 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000050"));
    }

    ArrayList formulaslist = new ArrayList();

    String creceiptcorpid = ordhvo.getCreceiptcorpid();
    if ((creceiptcorpid == null) || (creceiptcorpid.trim().length() <= 0)) {
      formulaslist.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
    }

    String creceiptcustomerid = ordhvo.getCreceiptcustomerid();
    if ((creceiptcustomerid == null) || (creceiptcustomerid.trim().length() <= 0))
    {
      formulaslist.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
    }

    String cdeptid = ordhvo.getCdeptid();
    if ((cdeptid == null) || (cdeptid.trim().length() <= 0)) {
      formulaslist.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
    }

    String cemployeeid = ordhvo.getCemployeeid();
    if ((cemployeeid == null) || (cemployeeid.trim().length() <= 0)) {
      formulaslist.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
    }

    UFDouble ndiscountrate = ordbvos[0].getNdiscountrate();
    if (ndiscountrate == null)
      ndiscountrate = ordhvo.getNdiscountrate();
    else
      ordhvo.setNdiscountrate(ndiscountrate);
    if (ndiscountrate == null) {
      formulaslist.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");
    }

    String ctransmodeid = ordhvo.getCtransmodeid();
    if ((ctransmodeid == null) || (ctransmodeid.trim().length() <= 0)) {
      formulaslist.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");
    }

    String ccurrencytypeid = ordbvos[0].getCcurrencytypeid();
    if ((ccurrencytypeid == null) || (ccurrencytypeid.trim().length() <= 0))
      ccurrencytypeid = ordhvo.getCcurrencytypeid();
    else
      ordhvo.setCcurrencytypeid(ccurrencytypeid);
    if ((ccurrencytypeid == null) || (ccurrencytypeid.trim().length() <= 0)) {
      formulaslist.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
    }

    String ctermprotocolid = ordhvo.getCtermprotocolid();
    if ((ctermprotocolid == null) || (ctermprotocolid.trim().length() <= 0)) {
      formulaslist.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
    }

    String ccalbodyid = ordhvo.getCcalbodyid();
    if ((ccalbodyid == null) || (ccalbodyid.trim().length() <= 0)) {
      formulaslist.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
    }

    String csalecorpid = ordhvo.getCsalecorpid();
    if ((csalecorpid == null) || (csalecorpid.trim().length() <= 0)) {
      formulaslist.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");
    }

    formulaslist.add("naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)");

    formulaslist.add("ccustomername->getColValue(bd_cumandoc,pk_pricegroupcorp,pk_cumandoc,ccustomerid)");

    SoVoTools.execFormulasAtBs((String[])(String[])formulaslist.toArray(new String[formulaslist.size()]), new SaleorderHVO[] { ordhvo });

    FetchValueDMO fetdmo = new FetchValueDMO();

    creceiptcustomerid = ordhvo.getCreceiptcustomerid();

    String creceiptareaid = null;

    String vreceiveaddress = null;

    String crecaddrnode = null;

    String cchantypeid = (String)ordhvo.getAttributeValue("ccustomername");

    if ((creceiptcustomerid != null) && (creceiptcustomerid.trim().length() > 0))
    {
      String pk_cubasdoc = fetdmo.getColValue("bd_cumandoc", "pk_cubasdoc", " pk_cumandoc='" + creceiptcustomerid + "' ");

      String pk_custaddr = fetdmo.getColValue("bd_custaddr", "pk_custaddr", " pk_cubasdoc='" + pk_cubasdoc + "' and defaddrflag = 'Y' ");

      if ((pk_custaddr != null) && (pk_custaddr.trim().length() > 0))
      {
        Object[] otemps = fetdmo.getColValue("bd_custaddr", new String[] { "addrname", "pk_areacl", "pk_address" }, " pk_custaddr='" + pk_custaddr + "' ");

        if ((otemps != null) && (otemps.length == 3)) {
          vreceiveaddress = otemps[0] == null ? null : otemps[0].toString();

          creceiptareaid = otemps[1] == null ? null : otemps[1].toString();

          crecaddrnode = otemps[2] == null ? null : otemps[2].toString();
        }

      }

    }

    if (ordhvo.getDbilldate() == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000051"));
    }

    String pk_corp = ordhvo.getPk_corp();

    if ((pk_corp == null) || (pk_corp.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000052"));
    }

    if ((ordhvo.getVreceiveaddress() == null) || (ordhvo.getVreceiveaddress().trim().length() <= 0))
    {
      ordhvo.setVreceiveaddress(vreceiveaddress);
    }
    else vreceiveaddress = ordhvo.getVreceiveaddress();

    UFDouble uf100 = new UFDouble(100);

    UFDouble uf1 = new UFDouble(1);

    ccurrencytypeid = ordhvo.getCcurrencytypeid();
    if ((ccurrencytypeid == null) || (ccurrencytypeid.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000053"));
    }

    ndiscountrate = null;
    ndiscountrate = ordhvo.getNdiscountrate();
    if (ndiscountrate == null)
      ndiscountrate = ordbvos[0].getNdiscountrate();
    if (ndiscountrate == null) {
      ndiscountrate = uf100;
    }
    ordhvo.setNdiscountrate(ndiscountrate);

    String[] bodyfomulas = { "cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)", "nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)", "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)", "cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinvbasdocid)", "cinventoryname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinvbasdocid)", "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)", "laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)", "assistunit->getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cinvbasdocid)" };

    SoVoTools.execFormulasAtBs(bodyfomulas, ordbvos);

    ArrayList invidlist = new ArrayList(ordbvos.length);

    ccalbodyid = ordhvo.getCcalbodyid();

    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if (ordbvos[i].getCinventoryid() != null) {
        invidlist.add(ordbvos[i].getCinventoryid());
      }

      ordbvos[i].setNdiscountrate(ndiscountrate);

      if (ordbvos[i].getNitemdiscountrate() == null) {
        ordbvos[i].setNitemdiscountrate(uf100);
      }
      if ((ordbvos[i].getCreceiptcorpid() == null) || (ordbvos[i].getCreceiptcorpid().trim().length() <= 0))
      {
        ordbvos[i].setCreceiptcorpid(creceiptcustomerid);
      }

      if ((ordbvos[i].getCreceiptareaid() == null) || (ordbvos[i].getCreceiptareaid().trim().length() <= 0))
      {
        ordbvos[i].setCreceiptareaid(creceiptareaid);
      }

      if ((ordbvos[i].getVreceiveaddress() == null) || (ordbvos[i].getVreceiveaddress().trim().length() <= 0))
      {
        ordbvos[i].setVreceiveaddress(vreceiveaddress);
      }

      if ((ordbvos[i].getCrecaddrnode() == null) || (ordbvos[i].getCrecaddrnode().trim().length() <= 0))
      {
        ordbvos[i].setCrecaddrnode(crecaddrnode);
      }

      if ((ordbvos[i].getCadvisecalbodyid() == null) || (ordbvos[i].getCadvisecalbodyid().trim().length() <= 0))
      {
        ordbvos[i].setCadvisecalbodyid(ccalbodyid);
      }

      if ((ordbvos[i].getCquoteunitid() == null) || (ordbvos[i].getCquoteunitid().trim().length() <= 0))
      {
        ordbvos[i].setCquoteunitid(ordbvos[i].getCunitid());
        ordbvos[i].setNqtscalefactor(uf1);
      }

      if (ordbvos[i].getNquoteunitnum() == null) {
        ordbvos[i].setNquoteunitnum(ordbvos[i].getNnumber());
      }

      if (ordbvos[i].getNorgqttaxprc() == null) {
        ordbvos[i].setNorgqttaxprc(ordbvos[i].getNoriginalcurtaxprice());
      }

      if (ordbvos[i].getNorgqtprc() == null) {
        ordbvos[i].setNorgqtprc(ordbvos[i].getNoriginalcurprice());
      }

      if (ordbvos[i].getNorgqttaxnetprc() == null) {
        ordbvos[i].setNorgqttaxnetprc(ordbvos[i].getNoriginalcurtaxnetprice());
      }

      if (ordbvos[i].getNorgqtnetprc() == null) {
        ordbvos[i].setNorgqtnetprc(ordbvos[i].getNoriginalcurnetprice());
      }

      if (ordbvos[i].getNqttaxprc() == null) {
        ordbvos[i].setNqttaxprc(ordbvos[i].getNtaxprice());
      }

      if (ordbvos[i].getNorgqtprc() == null) {
        ordbvos[i].setNorgqtprc(ordbvos[i].getNprice());
      }

      if (ordbvos[i].getNorgqttaxnetprc() == null) {
        ordbvos[i].setNorgqttaxnetprc(ordbvos[i].getNtaxnetprice());
      }

      if (ordbvos[i].getNorgqtnetprc() == null) {
        ordbvos[i].setNorgqtnetprc(ordbvos[i].getNnetprice());
      }

      if (ordbvos[i].getDconsigndate() == null) {
        ordbvos[i].setDconsigndate(ordhvo.getDbilldate());
      }

      if (ordbvos[i].getDdeliverdate() == null) {
        ordbvos[i].setDdeliverdate(ordhvo.getDbilldate());
      }

      if ((ordbvos[i].getCchantypeid() == null) || (ordbvos[i].getCchantypeid().trim().length() <= 0))
      {
        ordbvos[i].setCchantypeid(cchantypeid);
      }

      if (ordbvos[i].getBoosflag() == null)
        ordbvos[i].setBoosflag(new UFBoolean(false));
      if (ordbvos[i].getBsupplyflag() == null) {
        ordbvos[i].setBoosflag(new UFBoolean(false));
      }
      if ((ordbvos[i].getCconsigncorpid() == null) || (ordbvos[i].getCconsigncorpid().trim().length() <= 0))
      {
        ordbvos[i].setCconsigncorpid(pk_corp);
      }
      if ((ordbvos[i].getCinventoryid1() != null) && (ordbvos[i].getCinventoryid1().trim().length() > 0))
        continue;
      ordbvos[i].setCinventoryid1(ordbvos[i].getCinventoryid());
    }

    if (invidlist.size() > 0)
    {
      ChannelGroupDMO groupdmo = new ChannelGroupDMO();

      HashMap hschantypeid = groupdmo.getChantypeidOfCust(ordhvo.getPk_corp(), ordhvo.getCcustomerid());

      HashMap hscinvclassid = SOToolsDMO.getAnyValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc", SoVoTools.getVOsOnlyValues(ordbvos, "cinvbasdocid"), null);

      FetchValueDMO ftdmo = new FetchValueDMO();
      String pk_pricegroupcorp = ftdmo.getColValue("bd_cumandoc", "pk_pricegroupcorp", " pk_cumandoc='" + ordhvo.getCcustomerid() + "' ");

      String cinvclassid = null;
      String cinventoryid = null;
      String chantypeid = null;
        i = 0; for (int loop = ordbvos.length; i < loop; i++)
      {
        cinventoryid = ordbvos[i].getCinventoryid();
        if (cinventoryid == null) {
          continue;
        }
        cinvclassid = null;
        if (hscinvclassid != null) {
          cinvclassid = (String)hscinvclassid.get(cinventoryid);
        }
        if (hschantypeid != null) {
          chantypeid = (String)hschantypeid.get(cinventoryid);
          if (((chantypeid == null) || (chantypeid.trim().length() <= 0)) && 
            (cinvclassid != null)) {
            chantypeid = (String)hschantypeid.get(cinvclassid);
          }
        }

        if ((chantypeid == null) || (chantypeid.trim().length() <= 0)) {
          chantypeid = pk_pricegroupcorp;
        }

        ordbvos[i].setCchantypeid(chantypeid);
      }
    }
  }

  public void PushSave5A(AggregatedValueObject ordvo, Object clientojb, String curdate)
    throws RemoteException, NamingException, CreateException, SQLException, BusinessException
  {
    if (ordvo == null) {
      return;
    }

    HashMap hscalbody = null;

    HashMap hswareaddr = null;
    BillVO voBilltep = null;

    PfUtilBO pfbo = null;
    try
    {
      ArrayList abvolist = null;
      SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])ordvo.getChildrenVO();
      String vokey = null;
      HashMap hssplitvo = new HashMap();

      ArrayList calbodylist = new ArrayList();
      ArrayList warelist = new ArrayList();
      int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        vokey = "";
        if ((ordbvos[i].getCconsigncorpid() == null) || (ordbvos[i].getCconsigncorpid().trim().length() <= 0) || (ordbvos[i].getCconsigncorpid().equals(ordbvos[i].getPkcorp())))
        {
          vokey = vokey + ordbvos[i].getPkcorp();
          if ((ordbvos[i].getCadvisecalbodyid() != null) && (ordbvos[i].getCadvisecalbodyid().trim().length() > 0))
          {
            vokey = vokey + ordbvos[i].getCadvisecalbodyid();
          }
        } else {
          vokey = vokey + ordbvos[i].getCconsigncorpid();
          if ((ordbvos[i].getCreccalbodyid() != null) && (ordbvos[i].getCreccalbodyid().trim().length() > 0))
          {
            vokey = vokey + ordbvos[i].getCreccalbodyid();
          }
        }
        if (ordbvos[i].getBdericttrans() == null)
          vokey = vokey + SoVoConst.buffalse;
        else
          vokey = vokey + ordbvos[i].getBdericttrans();
        abvolist = (ArrayList)hssplitvo.get(vokey);
        if (abvolist == null) {
          abvolist = new ArrayList();
          hssplitvo.put(vokey, abvolist);
        }
        abvolist.add(ordbvos[i]);
        if ((ordbvos[i].getCadvisecalbodyid() != null) && (!calbodylist.contains(ordbvos[i].getCadvisecalbodyid())))
        {
          calbodylist.add(ordbvos[i].getCadvisecalbodyid());
        }if ((ordbvos[i].getCreccalbodyid() != null) && (!calbodylist.contains(ordbvos[i].getCreccalbodyid())))
        {
          calbodylist.add(ordbvos[i].getCreccalbodyid());
        }
        if ((ordbvos[i].getCbodywarehouseid() != null) && (!warelist.contains(ordbvos[i].getCbodywarehouseid())))
        {
          warelist.add(ordbvos[i].getCbodywarehouseid());
        }if ((ordbvos[i].getCrecwareid() == null) || (warelist.contains(ordbvos[i].getCrecwareid())))
          continue;
        warelist.add(ordbvos[i].getCrecwareid());
      }

      if (hssplitvo.size() <= 0)
        return;
      String[] calbodyids = (String[])(String[])calbodylist.toArray(new String[calbodylist.size()]);

      String[] warehouseids = (String[])(String[])warelist.toArray(new String[warelist.size()]);

      SaleOrderVO[] ordvos = new SaleOrderVO[hssplitvo.size()];
      Iterator iter = hssplitvo.values().iterator();
      int count = 0;
      while (iter.hasNext()) {
        abvolist = (ArrayList)iter.next();
        ordvos[count] = new SaleOrderVO();
        ordvos[count].setParentVO(ordvo.getParentVO());
        ordvos[count].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])abvolist.toArray(new SaleorderBVO[abvolist.size()]));

        count++;
      }

      AggregatedValueObject[] vos5A = PfUtilTools.runChangeDataAry("30", "5A", ordvos);

      if (vos5A == null)
        return;
      ArrayList ojblist = new ArrayList();
      ojblist.add(null);
      ojblist.add(((SaleOrderVO)ordvo).getClientLink());

      String[] addrs = null;
      String[] wareaddrs = null;

      pfbo = new PfUtilBO();

      Object[] userobjarr = new Object[vos5A.length];

      HashMap hsware = null;
      String consigncorpid = null;

        i = 0; for (int loop = ordvos.length; i < loop; i++)
      {
        userobjarr[i] = ojblist;

        if ((vos5A[i] == null) || (vos5A[i].getParentVO() == null)) {
          continue;
        }
        voBilltep = (BillVO)vos5A[i];
        voBilltep.setOperator(ordvos[i].getParentVO().getAttributeValue("capproveid").toString());

        consigncorpid = ordvos[i].getBodyVOs()[0].getCconsigncorpid();
        if ((consigncorpid == null) || (consigncorpid.trim().length() <= 0) || (consigncorpid.equals(ordvos[i].getHeadVO().getPk_corp())))
        {
          vos5A[i].getParentVO().setAttributeValue("cincbid", ordvos[i].getBodyVOs()[0].getCadvisecalbodyid());

          if (vos5A[i].getParentVO().getAttributeValue("cincbid") == null) {
            vos5A[i].getParentVO().setAttributeValue("cincbid", ordvos[i].getHeadVO().getCcalbodyid());
          }
          int m = 0; for (int loopm = ordvos[i].getBodyVOs().length; m < loopm; m++)
          {
            vos5A[i].getChildrenVO()[m].setAttributeValue("crowno", "" + (m + 1));

            vos5A[i].getChildrenVO()[m].setAttributeValue("cinwhid", vos5A[i].getChildrenVO()[m].getAttributeValue("coutwhid"));

            vos5A[i].getChildrenVO()[m].setAttributeValue("coutcorpid", null);

            vos5A[i].getChildrenVO()[m].setAttributeValue("coutcbid", null);

            vos5A[i].getChildrenVO()[m].setAttributeValue("coutwhid", null);

            if ((ordvos[i].getBodyVOs()[m].getBdericttrans() != null) && (ordvos[i].getBodyVOs()[m].getBdericttrans().booleanValue()))
            {
              vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(0));
            }
            else
            {
              if (hscalbody == null) {
                hscalbody = SOToolsDMO.getAnyValueArray("bd_calbody", new String[] { "pk_areacl", "area", "pk_address" }, "pk_calbody", calbodyids, null);

                if (hscalbody == null) {
                  hscalbody = new HashMap();
                }
              }

              if (hswareaddr == null) {
                hswareaddr = SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] { "pk_calbody", "storaddr", "pk_address" }, "pk_stordoc", warehouseids, null);

                if (hswareaddr == null) {
                  hswareaddr = new HashMap();
                }
              }

              vos5A[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

              addrs = (String[])(String[])hscalbody.get(ordvos[i].getBodyVOs()[m].getCadvisecalbodyid() + "");

              wareaddrs = (String[])(String[])hswareaddr.get(ordvos[i].getBodyVOs()[m].getCbodywarehouseid() + "");

              if (wareaddrs == null)
              {
                if (addrs == null)
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
                }

              }
              else if (addrs == null)
              {
                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

                vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);

                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
              }
              else
              {
                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

                if ((wareaddrs[1] != null) && (wareaddrs[1].trim().length() > 0))
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
                }

                if ((wareaddrs[2] != null) && (wareaddrs[2].trim().length() > 0))
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
                }

              }

              vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(1));
            }

          }

        }
        else
        {
          int m = 0; for (int loopm = ordvos[i].getBodyVOs().length; m < loopm; m++)
          {
            if ((ordvos[i].getBodyVOs()[m].getBdericttrans() != null) && (ordvos[i].getBodyVOs()[m].getBdericttrans().booleanValue()))
            {
              if (hsware == null) {
                hsware = SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] { "pk_stordoc" }, "pk_calbody", SoVoTools.getVOsOnlyValues(ordvo.getChildrenVO(), "creccalbodyid"), " isdirectstore='Y' ");

                if (hsware == null) {
                  hsware = new HashMap();
                }
              }
              addrs = (String[])(String[])hsware.get(ordvos[i].getBodyVOs()[m].getCreccalbodyid() + "");

              if (addrs != null)
              {
                vos5A[i].getChildrenVO()[m].setAttributeValue("cinwhid", addrs[0]);
              }

              vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(0));
            }
            else
            {
              if (hscalbody == null) {
                hscalbody = SOToolsDMO.getAnyValueArray("bd_calbody", new String[] { "pk_areacl", "area", "pk_address" }, "pk_calbody", calbodyids, null);

                if (hscalbody == null) {
                  hscalbody = new HashMap();
                }
              }

              if (hswareaddr == null) {
                hswareaddr = SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] { "pk_calbody", "storaddr", "pk_address" }, "pk_stordoc", warehouseids, null);

                if (hswareaddr == null) {
                  hswareaddr = new HashMap();
                }
              }

              vos5A[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

              addrs = (String[])(String[])hscalbody.get(ordvos[i].getBodyVOs()[m].getCreccalbodyid() + "");

              wareaddrs = (String[])(String[])hswareaddr.get(ordvos[i].getBodyVOs()[m].getCrecwareid() + "");

              if (wareaddrs == null)
              {
                if (addrs == null)
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
                }

              }
              else if (addrs == null)
              {
                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);

                vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);

                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
              }
              else
              {
                vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

                if ((wareaddrs[1] != null) && (wareaddrs[1].trim().length() > 0))
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
                }

                if ((wareaddrs[2] != null) && (wareaddrs[2].trim().length() > 0))
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
                }
                else
                {
                  vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);
                }

              }

              vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(1));
            }

          }

        }

      }

      try
      {
        pfbo.processBatch("PUSHSAVEVO", "5A", curdate, vos5A, userobjarr, null);
      }
      catch (Exception e) {
        reportException(e);
        if ((BillTools.marshException(e) instanceof ATPNotEnoughException)) {
          throw new BusinessException(e);
        }
        throw new BusinessException(e.getMessage());
      }
    }
    finally
    {
    }
  }

  public void renovateARWhenOrdEnd(IBillInvokeCreditManager armanager, AggregatedValueObject ordvo)
    throws BusinessException
  {
    SaleOrderVO saleorder = (SaleOrderVO)ordvo;

    if ((saleorder == null) || (armanager == null)) {
      return;
    }
    BillCreditOriginVO billcreditvo = null;
    UFDate date = ((SaleorderHVO)saleorder.getParentVO()).getDmakedate();
    try
    {
      if (saleorder.getIAction() == 6)
      {
        SaleorderBVO[] curordbvos = saleorder.getBodyVOs();
        ArrayList bvoslist = new ArrayList();

        ArrayList binvoicelist = new ArrayList();
        int i = 0; for (int loop = curordbvos.length; i < loop; i++) {
          if ((curordbvos[i].getBifinventoryfinish() == null) || (!curordbvos[i].getBifinventoryfinish().booleanValue()))
          {
            bvoslist.add(curordbvos[i]);
          }
          if ((curordbvos[i].getBifinvoicefinish() != null) && (curordbvos[i].getBifinvoicefinish().booleanValue())) {
            continue;
          }
          binvoicelist.add(curordbvos[i]);
        }
        curordbvos = (SaleorderBVO[])(SaleorderBVO[])bvoslist.toArray(new SaleorderBVO[bvoslist.size()]);

        if ((curordbvos != null) && (curordbvos.length > 0)) {
          billcreditvo = new BillCreditOriginVO();
          billcreditvo.m_iBillType = 0;
          billcreditvo.m_voBill = saleorder.getParentVO();
          billcreditvo.m_voBill_b = curordbvos;
          billcreditvo.m_dActDate = date;
          billcreditvo.m_iBillAct = 9;
          billcreditvo.m_voBill_init = null;
          billcreditvo.m_voBill_init_b = null;

          billcreditvo.sOperatorid = saleorder.getOperatorid();
          armanager.renovateAR(billcreditvo);
        }

        curordbvos = (SaleorderBVO[])(SaleorderBVO[])binvoicelist.toArray(new SaleorderBVO[binvoicelist.size()]);

        if ((curordbvos != null) && (curordbvos.length > 0)) {
          billcreditvo = new BillCreditOriginVO();
          billcreditvo.m_iBillType = 0;
          billcreditvo.m_voBill = saleorder.getParentVO();
          billcreditvo.m_voBill_b = curordbvos;
          billcreditvo.m_dActDate = date;
          billcreditvo.m_iBillAct = 12;
          billcreditvo.m_voBill_init = null;
          billcreditvo.m_voBill_init_b = null;

          billcreditvo.sOperatorid = saleorder.getOperatorid();
          armanager.renovateAR(billcreditvo);
        }
      }
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  /** @deprecated */
  public HashMap splitOrdVOFor20_5A_5D(AggregatedValueObject ordvo)
    throws RemoteException, NamingException, CreateException, SQLException, BusinessException
  {
    HashMap hmreturn = new HashMap();

    SaleOrderVO[] retordvos = new SaleOrderVO[3];
    if (ordvo == null) {
      return hmreturn;
    }

    SysInitDMO sysdmo = new SysInitDMO();
    String so35 = sysdmo.getParaString("0001", "SO35");
    boolean isPush5D = false;
    if ("调拨订单".equals(so35)) {
      isPush5D = true;
    }

    HashMap hsinvprop = null;
    SaleorderHVO ordhvo = (SaleorderHVO)ordvo.getParentVO();
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])ordvo.getChildrenVO();
    if ((ordhvo == null) || (ordbvos == null) || (ordbvos.length <= 0))
      return hmreturn;
    String pk_corp = ordhvo.getPk_corp();
    ArrayList bvo20list = new ArrayList();
    ArrayList bvo5Alist = new ArrayList();
    ArrayList bvo5Dlist = new ArrayList();
    String consigncorpid = null; String cmatertype = null;
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      consigncorpid = ordbvos[i].getCconsigncorpid();

      if ((consigncorpid == null) || (consigncorpid.trim().length() <= 0) || (consigncorpid.equals(pk_corp)))
      {
        if ((consigncorpid != null) && (consigncorpid.trim().length() <= 0))
          ordbvos[i].setCconsigncorpid(null);
        if (hsinvprop == null)
        {
          hsinvprop = SOToolsDMO.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.matertype", "so_saleorder_b.corder_bid", SoVoTools.getSArrayVOsValue(ordvo.getChildrenVO(), "corder_bid"), " bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");

          if (hsinvprop == null) {
            hsinvprop = new HashMap();
          }
        }
        if ((ordbvos[i].getNnumber() == null) || (ordbvos[i].getNnumber().doubleValue() <= 0.0D)) {
          continue;
        }
        cmatertype = (String)hsinvprop.get(ordbvos[i].getCorder_bid());

        if ("MR".equals(cmatertype)) {
          bvo20list.add(ordbvos[i]);
        }
        else if ("DB".equals(cmatertype)) {
          bvo5Alist.add(ordbvos[i]);
        }

      }
      else if (isPush5D) {
        bvo5Dlist.add(ordbvos[i]);
      }
      else
      {
        if ((ordbvos[i].getNnumber() == null) || (ordbvos[i].getNnumber().doubleValue() <= 0.0D)) {
          continue;
        }
        bvo5Alist.add(ordbvos[i]);
      }
    }

    String SO58 = null;
    try {
      SO58 = sysdmo.getParaString(pk_corp, "SO58");
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    if ((SO58 == null) || (SO58.trim().length() == 0))
      SO58 = "按订单数量";
    Integer iBD501 = sysdmo.getParaInt(pk_corp, "BD501");
    if (iBD501 == null) {
      iBD501 = new Integer(2);
    }

    if (bvo20list.size() > 0) {
      retordvos[0] = new SaleOrderVO();
      retordvos[0].setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[0].setParentVO(ordhvo);
      retordvos[0].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo20list.toArray(new SaleorderBVO[bvo20list.size()]));

      if (SO58.equals("按净需求")) {
        retordvos[0] = getNumberBySO58(retordvos[0], getLabors((SaleorderBVO[])(SaleorderBVO[])retordvos[0].getChildrenVO()), iBD501.intValue());
      }

      if (retordvos[0] != null) {
        hmreturn.put("CGSQ", splitBodyItemsByOuttype(retordvos[0]));
      }
    }
    if (bvo5Alist.size() > 0) {
      retordvos[1] = new SaleOrderVO();
      retordvos[1].setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[1].setParentVO(ordhvo);
      retordvos[1].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo5Alist.toArray(new SaleorderBVO[bvo5Alist.size()]));

      if (SO58.equals("按净需求")) {
        retordvos[1] = getNumberBySO58(retordvos[1], getLabors((SaleorderBVO[])(SaleorderBVO[])retordvos[1].getChildrenVO()), iBD501.intValue());
      }

      if (retordvos[1] != null) {
        hmreturn.put("DBSQ", retordvos[1]);
      }
    }
    if (bvo5Dlist.size() > 0) {
      retordvos[2] = new SaleOrderVO();
      retordvos[2].setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[2].setParentVO(ordhvo);
      retordvos[2].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo5Dlist.toArray(new SaleorderBVO[bvo5Dlist.size()]));

      if (SO58.equals("按净需求")) {
        retordvos[2] = getNumberBySO58(retordvos[2], getLabors((SaleorderBVO[])(SaleorderBVO[])retordvos[2].getChildrenVO()), iBD501.intValue());
      }

      if (retordvos[2] != null) {
        hmreturn.put("DBDD", retordvos[2]);
      }
    }
    return hmreturn;
  }

  private SaleOrderVO[] splitBodyItemsByOuttype(SaleOrderVO vo)
    throws SQLException
  {
    HashMap hsinvprop = SOToolsDMO.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.outtype", "so_saleorder_b.corder_bid", SoVoTools.getSArrayVOsValue(vo.getChildrenVO(), "corder_bid"), " bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");

    if (hsinvprop == null)
      hsinvprop = new HashMap();
    SaleorderBVO[] ordbvos = vo.getBodyVOs();
    Vector vt = null;
    Hashtable ht = new Hashtable();
    Object outtype = null;
    for (int i = 0; i < ordbvos.length; i++) {
      outtype = hsinvprop.get(ordbvos[i].getCorder_bid());
      if (outtype == null)
        outtype = "NullItems";
      if (ht.containsKey(outtype)) {
        vt = (Vector)ht.get(outtype);
      } else {
        vt = new Vector();
        ht.put(outtype, vt);
      }
      vt.add(ordbvos[i].clone());
    }
    SaleOrderVO[] returnvos = new SaleOrderVO[ht.size()];
    Object[] sKeys = new Object[ht.size()];
    ht.keySet().toArray(sKeys);
    SaleorderBVO[] ordtmp = null;
    for (int i = 0; i < ht.size(); i++) {
      vt = (Vector)ht.get(sKeys[i]);

      returnvos[i] = new SaleOrderVO();
      returnvos[i].setClientLink(vo.getClientLink());
      ordtmp = new SaleorderBVO[vt.size()];
      vt.copyInto(ordtmp);
      returnvos[i].setParentVO((CircularlyAccessibleValueObject)(CircularlyAccessibleValueObject)vo.getParentVO().clone());

      returnvos[i].setChildrenVO(ordtmp);
    }
    return returnvos;
  }

  public AggregatedValueObject[] splitOrdVOFor20_5D_5A(AggregatedValueObject ordvo)
    throws RemoteException, NamingException, CreateException, SQLException, BusinessException
  {
    AggregatedValueObject[] retordvos = new AggregatedValueObject[3];
    if (ordvo == null) {
      return retordvos;
    }

    SysInitDMO sysdmo = new SysInitDMO();
    String so35 = sysdmo.getParaString("0001", "SO35");
    boolean isPush5D = false;
    if ("调拨订单".equals(so35)) {
      isPush5D = true;
    }

    HashMap hsinvprop = null;
    SaleorderHVO ordhvo = (SaleorderHVO)ordvo.getParentVO();
    SaleorderBVO[] ordbvos = (SaleorderBVO[])(SaleorderBVO[])ordvo.getChildrenVO();
    if ((ordhvo == null) || (ordbvos == null) || (ordbvos.length <= 0))
      return retordvos;
    String pk_corp = ordhvo.getPk_corp();
    ArrayList bvo20list = new ArrayList();
    ArrayList bvo5Alist = new ArrayList();
    ArrayList bvo5Dlist = new ArrayList();
    String consigncorpid = null; String cmatertype = null;
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      consigncorpid = ordbvos[i].getCconsigncorpid();

      if ((consigncorpid == null) || (consigncorpid.trim().length() <= 0) || (consigncorpid.equals(pk_corp)))
      {
        if ((consigncorpid != null) && (consigncorpid.trim().length() <= 0))
          ordbvos[i].setCconsigncorpid(null);
        if (hsinvprop == null)
        {
          hsinvprop = SOToolsDMO.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.matertype", "so_saleorder_b.corder_bid", SoVoTools.getSArrayVOsValue(ordvo.getChildrenVO(), "corder_bid"), " bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");

          if (hsinvprop == null) {
            hsinvprop = new HashMap();
          }
        }
        if ((ordbvos[i].getNnumber() == null) || (ordbvos[i].getNnumber().doubleValue() <= 0.0D)) {
          continue;
        }
        cmatertype = (String)hsinvprop.get(ordbvos[i].getCorder_bid());

        if ("MR".equals(cmatertype)) {
          bvo20list.add(ordbvos[i]);
        }
        else if ("DB".equals(cmatertype)) {
          bvo5Alist.add(ordbvos[i]);
        }

      }
      else if (isPush5D) {
        bvo5Dlist.add(ordbvos[i]);
      }
      else
      {
        if ((ordbvos[i].getNnumber() == null) || (ordbvos[i].getNnumber().doubleValue() <= 0.0D)) {
          continue;
        }
        bvo5Alist.add(ordbvos[i]);
      }

    }

    if (bvo20list.size() > 0) {
      retordvos[0] = new SaleOrderVO();
      ((SaleOrderVO)retordvos[0]).setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[0].setParentVO(ordhvo);
      retordvos[0].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo20list.toArray(new SaleorderBVO[bvo20list.size()]));
    }

    if (bvo5Alist.size() > 0) {
      retordvos[1] = new SaleOrderVO();
      ((SaleOrderVO)retordvos[1]).setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[1].setParentVO(ordhvo);
      retordvos[1].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo5Alist.toArray(new SaleorderBVO[bvo5Alist.size()]));
    }

    if (bvo5Dlist.size() > 0) {
      retordvos[2] = new SaleOrderVO();
      ((SaleOrderVO)retordvos[2]).setClientLink(((SaleOrderVO)ordvo).getClientLink());

      retordvos[2].setParentVO(ordhvo);
      retordvos[2].setChildrenVO((SaleorderBVO[])(SaleorderBVO[])bvo5Dlist.toArray(new SaleorderBVO[bvo5Dlist.size()]));
    }

    return retordvos;
  }

  public SaleorderBVO[] queryAllBodyDataByWhere(String swhere, String orderby)
    throws SQLException
  {
    SOField[] addfields = SaleorderBVO.getAddFields();
    StringBuffer addfieldsql = new StringBuffer("");
    if (addfields != null) {
      int i = 0; for (int loop = addfields.length; i < loop; i++) {
        if (addfields[i].getDatabasename() != null) {
          addfieldsql.append(",");
          addfieldsql.append(addfields[i].getTablename());
          addfieldsql.append(".");
          addfieldsql.append(addfields[i].getDatabasename());
        }
      }
    }

    String sql = "select so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid, so_saleorder_b.cinventoryid, so_saleorder_b.cunitid, so_saleorder_b.cpackunitid, so_saleorder_b.nnumber, so_saleorder_b.npacknumber, so_saleorder_b.cbodywarehouseid, so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, so_saleorder_b.blargessflag, so_saleorder_b.ceditsaleid, so_saleorder_b.beditflag, so_saleorder_b.veditreason, so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, so_saleorder_b.ndiscountrate, so_saleorder_b.nexchangeotobrate, so_saleorder_b.nexchangeotoarate, so_saleorder_b.ntaxrate, so_saleorder_b.noriginalcurprice, so_saleorder_b.noriginalcurtaxprice, so_saleorder_b.noriginalcurnetprice, so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurtaxmny, so_saleorder_b.noriginalcurmny, so_saleorder_b.noriginalcursummny, so_saleorder_b.noriginalcurdiscountmny, so_saleorder_b.nprice, so_saleorder_b.ntaxprice, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice, so_saleorder_b.ntaxmny, so_saleorder_b.nmny, so_saleorder_b.nsummny, so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, so_saleorder_b.frowstatus, so_saleorder_b.frownote,so_saleorder_b.cinvbasdocid,so_saleorder_b.cbatchid, so_saleorder_b.fbatchstatus,so_saleorder_b.cbomorderid,so_saleorder_b.cfreezeid,so_saleorder_b.ct_manageid, so_saleorder_b.ts, so_saleorder_b.cadvisecalbodyid, so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag, so_saleorder_b.creceiptareaid, so_saleorder_b.vreceiveaddress, so_saleorder_b.creceiptcorpid, so_saleorder_b.crowno, so_saleexecute.creceipttype, so_saleexecute.ntotalpaymny, so_saleexecute.ntotalreceiptnumber, so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinvoicemny, so_saleexecute.ntotalinventorynumber, so_saleexecute.ntotalbalancenumber, so_saleexecute.ntotalsignnumber, so_saleexecute.ntotalcostmny, so_saleexecute.bifinvoicefinish, so_saleexecute.bifreceiptfinish, so_saleexecute.bifinventoryfinish, so_saleexecute.bifpayfinish, so_saleexecute.bifpaybalance, so_saleexecute.bifpaysign, so_saleexecute.nassistcurdiscountmny, so_saleexecute.nassistcursummny, so_saleexecute.nassistcurmny, so_saleexecute.nassistcurtaxmny, so_saleexecute.nassistcurtaxnetprice, so_saleexecute.nassistcurnetprice, so_saleexecute.nassistcurtaxprice, so_saleexecute.nassistcurprice, so_saleexecute.cprojectid, so_saleexecute.cprojectphaseid, so_saleexecute.cprojectid3, so_saleexecute.vfree1, so_saleexecute.vfree2, so_saleexecute.vfree3, so_saleexecute.vfree4, so_saleexecute.vfree5, so_saleexecute.vdef1, so_saleexecute.vdef2, so_saleexecute.vdef3, so_saleexecute.vdef4, so_saleexecute.vdef5, so_saleexecute.vdef6, so_saleexecute.ntotalplanreceiptnumber,so_saleexecute.ntotalreturnnumber,so_saleexecute.ts, so_saleexecute.ntotalshouldoutnum, so_saleexecute.ntotlbalcostnum, bd_invbasdoc.discountflag  " + addfieldsql.toString() + " from so_saleorder_b, so_saleexecute, so_sale, bd_invbasdoc " + " where so_saleorder_b.csaleid = so_saleexecute.csaleid " + " AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid " + " AND so_saleexecute.creceipttype = '30' " + " AND beditflag = 'N' " + " and so_saleorder_b.csaleid=so_sale.csaleid " + " and bd_invbasdoc.pk_invbasdoc = so_saleorder_b.cinvbasdocid ";

    if (swhere != null) {
      swhere = swhere.trim();
      if (swhere.length() > 0) {
        if ((swhere.indexOf("and") == 0) || (swhere.indexOf("AND") == 0))
          sql = sql + swhere;
        else {
          sql = sql + " AND " + swhere + " ";
        }
      }
    }
    if ((orderby != null) && (orderby.trim().length() > 0)) {
      if ((orderby.indexOf("order by") >= 0) || (orderby.indexOf("ORDER BY") >= 0))
      {
        sql = sql + orderby;
      }
      else sql = sql + " order by " + orderby;
    }

    SaleorderBVO[] saleItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    try {
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString(1);
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString(2);
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString(3);
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString(4);
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString(5);
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString(6);
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString(7);
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString(8);
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString(9);
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject(10);
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject(11);
        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString(12);
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString(13);
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString(14);
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString(15);
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString(16);
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString(17);
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString(18);
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString(19);
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject(20);
        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(21);
        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(22);
        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(23);
        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject(24);
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject(25);
        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject(26);
        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject(27);
        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject(28);

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject(29);
        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject(30);
        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject(31);
        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject(32);

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject(33);
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject(34);
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject(35);
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject(36);
        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject(37);
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject(38);
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject(39);
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject(40);
        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString(41);
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject(42);
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString(43);
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString(44);
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString(45);
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject(46);
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString(47);
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        String cfreezeid = rs.getString(48);
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ct_manageid = rs.getString(49);
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String ts = rs.getString(50);
        saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        String cadvisecalbodyid = rs.getString(51);
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        String boosflag = rs.getString(52);
        saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));

        String bsupplyflag = rs.getString(53);
        saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));

        String creceiptareaid = rs.getString(54);
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        String vreceiveaddress = rs.getString(55);
        saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(56);
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String crowno = rs.getString(57);
        saleItem.setCrowno(crowno == null ? null : crowno.trim());

        creceipttype = rs.getString(58);

        BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(59);
        saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(60);
        saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(61);
        saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(62);
        saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(63);

        saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(64);
        saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(65);
        saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        BigDecimal ntotalcostmny = (BigDecimal)rs.getObject(66);
        saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

        String bifinvoicefinish = rs.getString(67);
        saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        String bifreceiptfinish = rs.getString(68);
        saleItem.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        String bifinventoryfinish = rs.getString(69);
        saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        String bifpayfinish = rs.getString(70);
        saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        String bifpaybalance = rs.getString(71);
        saleItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String bifpaysign = rs.getString(72);
        saleItem.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

        BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject(73);

        saleItem.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject(74);
        saleItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject(75);
        saleItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(76);
        saleItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject(77);

        saleItem.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

        BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject(78);
        saleItem.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

        BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject(79);
        saleItem.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

        BigDecimal nassistcurprice = (BigDecimal)rs.getObject(80);
        saleItem.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

        String cprojectid = rs.getString(81);
        saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(82);
        saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String cprojectid3 = rs.getString(83);
        saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

        String vfree1 = rs.getString(84);
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(85);
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(86);
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(87);
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(88);
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(89);
        saleItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(90);
        saleItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(91);
        saleItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(92);
        saleItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(93);
        saleItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(94);
        saleItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject(95);

        saleItem.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

        BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject(96);
        saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

        String exets = rs.getString(97);
        saleItem.setExets(exets == null ? null : new UFDateTime(exets.trim()));

        BigDecimal ntotalshouldoutnum = (BigDecimal)rs.getObject(98);
        saleItem.setNtotalshouldoutnum(ntotalshouldoutnum == null ? null : new UFDouble(ntotalshouldoutnum));

        BigDecimal ntotlbalcostnum = (BigDecimal)rs.getObject(99);
        saleItem.setNtotlbalcostnum(ntotlbalcostnum == null ? null : new UFDouble(ntotlbalcostnum));
        
        UFDouble tsbl = new UFDouble(rs.getDouble(196));
        saleItem.setTsbl(tsbl);
        

        boolean discountflag = false;

        Object oTemp = rs.getObject(100);
        if ((oTemp != null) && 
          (oTemp.toString() == "Y")) {
          discountflag = true;
        }

        if (!discountflag)
          saleItem.setDiscountflag(UFBoolean.FALSE);
        else {
          saleItem.setDiscountflag(UFBoolean.TRUE);
        }
        getOrdBAndExeValueFromResultSet(rs, 101, saleItem);

        v.addElement(saleItem);
      }
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
    saleItems = new SaleorderBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }

    return saleItems;
  }

  public void setSaleCT(SaleorderBVO[] ordbvos, int iAction)
    throws SQLException, BusinessException, NamingException, nc.bs.pub.SystemException, RemoteException
  {
    if ((ordbvos == null) || (ordbvos.length <= 0))
      return;
    boolean bhvct = false;
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if ((!"Z4".equals(ordbvos[0].getCreceipttype())) && (!"Z3".equals(ordbvos[0].getCreceipttype())))
      {
        continue;
      }
      bhvct = true;
      break;
    }

    if (!bhvct) {
      return;
    }

    if (this.SO39 == null) {
      SysInitDMO sysinitdmo = new SysInitDMO();
      this.SO39 = sysinitdmo.getParaBoolean(ordbvos[0].getPkcorp(), "SO39");
    }
    if (this.SO39 == null) {
      this.SO39 = SoVoConst.buffalse;
    }
    if (!this.SO39.booleanValue()) {
      return;
    }

    ParaPoToCtRewriteVO ctvo = null;
    ArrayList alist = new ArrayList();
    UFDouble nnumber = null; UFDouble nmny = null;
    UFDouble duf_1 = new UFDouble(-1);
      i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if (ordbvos[i].getNnumber() == null)
        continue;
      if ((!"Z4".equals(ordbvos[i].getCreceipttype())) && (!"Z3".equals(ordbvos[i].getCreceipttype())))
      {
        continue;
      }

      nnumber = SoVoTools.getMnySub(ordbvos[i].getNnumber(), ordbvos[i].getNtotalinventorynumber());

      if ((nnumber == null) || (nnumber.compareTo(SoVoConst.duf0) == 0))
        continue;
      nmny = nnumber.div(ordbvos[i].getNnumber()).multiply(ordbvos[i].getNsummny() == null ? SoVoConst.duf0 : ordbvos[i].getNsummny());

      if ((ordbvos[i].getBlargessflag() != null) && (ordbvos[i].getBlargessflag().booleanValue()))
      {
        nnumber = SoVoConst.duf0;
        nmny = SoVoConst.duf0;
      }

      ctvo = new ParaPoToCtRewriteVO();
      ctvo.setCContractRowID(ordbvos[i].getCsourcebillbodyid());
      ctvo.setFirstTime(false);
      if (iAction == 9) {
        ctvo.setDNum(nnumber);
        ctvo.setDSummny(nmny);
        alist.add(ctvo);
      }
      else if (iAction == 6) {
        ctvo.setDNum(nnumber.multiply(duf_1));
        ctvo.setDSummny(nmny.multiply(duf_1));
        alist.add(ctvo);
      }

    }

    if (alist.size() > 0) {
      ParaPoToCtRewriteVO[] ctvos = (ParaPoToCtRewriteVO[])(ParaPoToCtRewriteVO[])alist.toArray(new ParaPoToCtRewriteVO[alist.size()]);
      try
      {
        ICtToPo_BackToCt backtoCt = (ICtToPo_BackToCt)NCLocator.getInstance().lookup(ICtToPo_BackToCt.class.getName());

        if (backtoCt != null)
          backtoCt.writeBackAccuOrdData(ctvos);
      }
      catch (ProdNotInstallException e)
      {
        SCMEnv.out(e.getMessage());
      } catch (BusinessException e) {
        throw e;
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessRuntimeException(e.getMessage());
      }
    }
  }

  public void setSaleCTWhenClose(AggregatedValueObject vo)
    throws SQLException, BusinessException, NamingException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null)
      return;
    String creceipttype = ((SaleOrderVO)vo).getBodyVOs()[0].getCreceipttype();

    if (("Z4".equals(creceipttype)) || ("Z3".equals(creceipttype)))
    {
      setSaleCT(((SaleOrderVO)vo).getBodyVOs(), 6);
    }
  }

  public void setSaleCTWhenOpen(AggregatedValueObject vo)
    throws SQLException, BusinessException, NamingException, nc.bs.pub.SystemException, RemoteException
  {
    if (vo == null)
      return;
    String creceipttype = ((SaleOrderVO)vo).getBodyVOs()[0].getCreceipttype();

    if (("Z4".equals(creceipttype)) || ("Z3".equals(creceipttype)))
    {
      setSaleCT(((SaleOrderVO)vo).getBodyVOs(), 9);
    }
  }

  public String delDelivdaypl(String csaleid, String cuserid, String pk_corp, boolean isdel)
    throws SQLException, javax.transaction.SystemException, BusinessException, Exception
  {
    if (!new SOToolsImpl().isModelStated("DM")) {
      SCMEnv.out("发运没有安装！");
      return null;
    }

    IDMToTO iDMToTOInstance = (IDMToTO)NCLocator.getInstance().lookup(IDMToTO.class.getName());

    return iDMToTOInstance.delDelivdaypl(csaleid, cuserid, pk_corp, isdel);
  }

  public String delAndCloseDelivdaypl(String[] cbids, String cuserid, String pk_corp, boolean isdel)
    throws SQLException, javax.transaction.SystemException, BusinessException, Exception
  {
    if (!new SOToolsImpl().isModelStated("DM")) {
      SCMEnv.out("发运没有安装！");
      return null;
    }

    IDMToTO iDMToTOInstance = (IDMToTO)NCLocator.getInstance().lookup(IDMToTO.class.getName());

    return iDMToTOInstance.delAndCloseDelivdaypl(cbids, cuserid, pk_corp, isdel);
  }

  public void delDelivdaypl(SaleOrderVO vo)
    throws SQLException, javax.transaction.SystemException, BusinessException, Exception
  {
    if ((vo == null) || (vo.getHeadVO() == null) || (vo.getHeadVO().getCsaleid() == null))
    {
      return;
    }
    String userid = vo.getCuruserid();
    if ((userid == null) || (userid.trim().length() <= 0)) {
      userid = vo.getHeadVO().getCoperatorid();
    }
    boolean isdel7D = vo.isIsDel7D();
    String pk_corp = vo.getHeadVO().getPk_corp();

    if (vo.getIAction() == 4) {
      if (!isdel7D) {
        String shint = delDelivdaypl(vo.getHeadVO().getCsaleid(), userid, pk_corp, false);

        if (shint != null)
          throw new Deal7DException(shint);
        freshVOStatus(vo);
      } else {
        delDelivdaypl(vo.getHeadVO().getCsaleid(), userid, pk_corp, true);

        freshVOStatus(vo);
      }
    } else if (vo.getIAction() == 6)
    {
      SaleorderBVO[] ordbvos = vo.getBodyVOs();
      if ((ordbvos == null) || (ordbvos.length <= 0))
        return;
      String[] corder_bids = new String[ordbvos.length];
      int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
        corder_bids[i] = ordbvos[i].getCorder_bid();
      }

      if (!isdel7D) {
        String shint = delAndCloseDelivdaypl(corder_bids, userid, pk_corp, false);

        if (shint != null)
          throw new Deal7DException(shint);
        freshVOStatus(vo);
      } else {
        delAndCloseDelivdaypl(corder_bids, userid, pk_corp, true);
        freshVOStatus(vo);
      }
    }
  }

  public void delDelivdayplWhenClose(SaleorderBVO[] ordbvos, String userid)
    throws SQLException, javax.transaction.SystemException, BusinessException, Exception
  {
    if ((ordbvos == null) || (ordbvos.length <= 0)) {
      return;
    }
    if ((userid == null) || (userid.trim().length() <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000055"));
    }

    ArrayList rowkeylist = new ArrayList();
    String pk_corp = null;
    boolean isdel7D = false;
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if (ordbvos[i].getCorder_bid() == null) {
        continue;
      }
      if ((ordbvos[i].getBifpayfinish() == null) || (!ordbvos[i].getBifpayfinish().booleanValue()) || (ordbvos[i].getBifinventoryfinish() == null) || (!ordbvos[i].getBifinventoryfinish().booleanValue()) || (ordbvos[i].getBifreceiptfinish() == null) || (!ordbvos[i].getBifreceiptfinish().booleanValue()) || (ordbvos[i].getBifinvoicefinish() == null) || (!ordbvos[i].getBifinvoicefinish().booleanValue()))
      {
        continue;
      }

      if (pk_corp == null) {
        pk_corp = ordbvos[i].getPkcorp();
        isdel7D = ordbvos[i].isIsDel7D();
      }

      rowkeylist.add(ordbvos[i].getCorder_bid());
    }

    if (rowkeylist.size() > 0) {
      String[] corder_bids = (String[])(String[])rowkeylist.toArray(new String[rowkeylist.size()]);
      try
      {
        if (!isdel7D) {
          String shint = delAndCloseDelivdaypl(corder_bids, userid, pk_corp, false);

          if (shint != null)
            throw new Deal7DException(shint);
        } else {
          delAndCloseDelivdaypl(corder_bids, userid, pk_corp, true);
        }
      } catch (DelDlvPlanException e) {
        throw new Deal7DException(e.getMessage());
      }
    }
  }

  private ArrayList fillPkAndTs(SaleOrderVO vo)
    throws BusinessException
  {
    ArrayList retlist = new ArrayList();
    if ((vo == null) || (vo.getHeadVO() == null))
      return retlist;
    boolean ishead = false;
    if ((vo.getHeadVO().getCsaleid() == null) || (vo.getHeadVO().getCsaleid().trim().length() <= 0))
    {
      ishead = true;
    }SaleorderBVO[] bvos = vo.getBodyVOs();

    UFDateTime ts = ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime();

    vo.getHeadVO().setTs(ts);
    int count = ishead ? 1 : 0;
    int i = 0; for (int loop = bvos.length; i < loop; i++) {
      if ((bvos[i].getStatus() == 2) && ((bvos[i].getBoosflag() == null) || (!bvos[i].getBoosflag().booleanValue())))
      {
        count++;
      }
      if ((bvos[i].getStatus() == 0) || (bvos[i].getStatus() == 3))
        continue;
      bvos[i].setTs(ts);
    }

    String[] coids = getOIDs(vo.getHeadVO().getPk_corp(), count);
    if (ishead) {
      vo.getHeadVO().setCsaleid(coids[0]);
      retlist.add(vo.getHeadVO().getCsaleid());
      retlist.add(vo.getHeadVO().getVreceiptcode());
    } else {
      retlist.add(vo.getHeadVO().getCsaleid());
    }

    int ipos = ishead ? 1 : 0;
    String csaleid = vo.getHeadVO().getCsaleid();
      i = 0; for (int loop = bvos.length; i < loop; i++) {
      if ((bvos[i].getStatus() != 2) || ((bvos[i].getBoosflag() != null) && (bvos[i].getBoosflag().booleanValue()))) {
        continue;
      }
      bvos[i].setCsaleid(csaleid);
      bvos[i].setCorder_bid(coids[ipos]);
      retlist.add(coids[ipos]);
      ipos++;
    }

    retlist.add(ts.toString());

    return retlist;
  }

  public void getAfterSaveInfo(SaleOrderVO vo, ArrayList retlist)
    throws RemoteException, SQLException, BusinessException
  {
    if ((vo == null) || (retlist == null)) {
      return;
    }
    String csaleid = vo.getHeadVO().getCsaleid();
    if (csaleid == null)
      return;
    String sCustManID = vo.getHeadVO().getCcustomerid();
    String sPKCorp = vo.getHeadVO().getPk_corp();
    if ((sCustManID == null) || (sPKCorp == null)) {
      return;
    }
    String sbfSQL = "SELECT accawmny, busawmny, ordawmny, creditmny, creditmoney  FROM bd_cumandoc WHERE pk_corp= ? and pk_cumandoc= ? ";

    String sbfSQL1 = " select corder_bid,cconsigncorpid,cadvisecalbodyid,cbodywarehouseid,cinventoryid1 from so_saleorder_b where csaleid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble[] result = new UFDouble[5];
    HashMap reths = new HashMap();
    if ((vo.getErrMsg() != null) && (vo.getErrMsg().trim().length() > 0))
      reths.put("ErrMsg", "ERR" + vo.getErrMsg());
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL);
      stmt.setString(1, sPKCorp);
      stmt.setString(2, sCustManID);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        Object value = rs.getObject(1);
        result[0] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(2);
        result[1] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(3);
        result[2] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(4);
        result[3] = (value == null ? null : new UFDouble(value.toString()));

        value = rs.getObject(5);
        result[4] = (value == null ? null : new UFDouble(value.toString()));
      }

      rs.close();
      stmt.close();
      reths.put("showCustManArInfo", result);
      stmt = con.prepareStatement(sbfSQL1);
      stmt.setString(1, csaleid);
      HashMap hsids = new HashMap();
      rs = stmt.executeQuery();
      if (rs.next()) {
        String[] row = new String[4];

        String corder_bid = rs.getString(1);

        row[0] = rs.getString(2);

        row[1] = rs.getString(3);

        row[2] = rs.getString(4);

        row[3] = rs.getString(5);

        hsids.put(corder_bid, row);
      }
      reths.put("reLoadConsignCorpAndCalbody", hsids);
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
      catch (Exception e) {
      }
    }
    if (vo.getIAction() != 0) {
      reths.put("queryCachPayByOrdId", queryCachPayByOrdId(csaleid));
    }

    retlist.add(reths);
  }

  public void updateBomID(SaleOrderVO vo)
    throws SQLException
  {
    if ((vo == null) || (vo.getHeadVO() == null) || (vo.getHeadVO().getCsaleid() == null))
    {
      return;
    }
    SaleorderBVO[] ordbvos = vo.getBodyVOs();
    if ((ordbvos == null) || (ordbvos.length <= 0))
      return;
    ArrayList alist = new ArrayList();
    SOToolVO toolvo = null;
    int i = 0; for (int loop = ordbvos.length; i < loop; i++) {
      if (SoVoTools.isEmptyString(ordbvos[i].getCbomorderid()))
        continue;
      toolvo = new SOToolVO(5);
      toolvo.setAttributeValue("cbomorderid", ordbvos[i].getCbomorderid());

      toolvo.setAttributeValue("csaleid", ordbvos[i].getCsaleid());
      toolvo.setAttributeValue("corder_bid", ordbvos[i].getCorder_bid());
      alist.add(toolvo);
    }

    if (alist.size() > 0) {
      SOToolVO[] toolvos = (SOToolVO[])(SOToolVO[])alist.toArray(new SOToolVO[alist.size()]);

      SOToolsDMO.updateBatch(toolvos, new String[] { "csaleid", "corder_bid" }, "so_bomorder", new String[] { "cbomorderid" });
    }
  }

  public void priceChangeToSquare(ArrayList pList, String sOperid)
    throws BusinessException
  {
  }

  public static void checkSaleorderBVOTs(SaleorderBVO[] vos)
    throws SQLException, nc.bs.pub.SystemException, BusinessException
  {
    if ((vos == null) || (vos.length <= 0)) {
      return;
    }
    HashMap hsvo = new HashMap();
    String keyvalue = null;
    String csaleid = null;
    int i = 0; for (int loop = vos.length; i < loop; i++) {
      if (vos[i] == null)
        continue;
      keyvalue = vos[i].getCorder_bid();
      if ((keyvalue == null) || (keyvalue.trim().length() <= 0))
        continue;
      hsvo.put(keyvalue, vos[i]);

      if (csaleid == null) {
        keyvalue = vos[i].getCsaleid();
        if ((keyvalue != null) && (keyvalue.trim().length() > 0))
          csaleid = keyvalue;
      }
    }
    if ((csaleid == null) || (hsvo.size() <= 0))
      return;
    SORowData[] rows = SOToolsDMO.getAnyValueSORow(" select so_saleorder_b.corder_bid,so_saleorder_b.ts,so_saleexecute.ts from so_saleorder_b,so_saleexecute  where so_saleorder_b.csaleid = so_saleexecute.csaleid and so_saleorder_b.corder_bid = so_saleexecute.csale_bid  and so_saleexecute.creceipttype = '30' and so_saleorder_b.beditflag = 'N'  and so_saleorder_b.dr = 0 and so_saleorder_b.csaleid = '" + csaleid + "' ");

    if ((rows == null) || (rows.length <= 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
    }

    UFDateTime ts1 = null; UFDateTime ts0 = null;
    SaleorderBVO vo = null;
    String corder_bid = null;
      i = 0; for (int loop = rows.length; i < loop; i++) {
      corder_bid = rows[i].getString(0);
      if (corder_bid == null)
        continue;
      vo = (SaleorderBVO)hsvo.get(corder_bid);
      if (vo == null)
        continue;
      ts0 = vo.getTs();
      ts1 = rows[i].getUFDateTime(1);

      if ((ts0 != null) && (ts1 != null) && 
        (!ts0.toString().trim().equals(ts1.toString().trim()))) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
      }

      ts0 = vo.getExets();
      ts1 = rows[i].getUFDateTime(2);
      if ((ts0 == null) || (ts1 == null) || 
        (ts0.toString().trim().equals(ts1.toString().trim()))) continue;
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115"));
    }
  }

  public Object[] queryAllBillDatas(String sheadsql, String sbodysql)
    throws BusinessException
  {
    if ((sheadsql == null) && (sheadsql.trim().length() <= 0)) {
      return null;
    }
    try
    {
      String sheadsqlnew = sheadsql;

      if ((sbodysql != null) && (sbodysql.trim().length() > 0)) {
        if ((sbodysql.trim().startsWith("and")) || (sbodysql.trim().startsWith("AND")))
        {
          sheadsqlnew = sheadsqlnew + " " + sbodysql;
        }
        else sheadsqlnew = sheadsqlnew + " and " + sbodysql;
      }
      CircularlyAccessibleValueObject[] headvos = queryAllHeadData(sheadsqlnew);

      if ((headvos == null) || (headvos.length <= 0)) {
        return null;
      }
      Object[] retobjs = new Object[3];

      retobjs[0] = headvos;

      if ((headvos != null) && (headvos.length > 5000)) {
        CircularlyAccessibleValueObject[] bakvos = headvos;
        headvos = new SaleorderHVO[5000];
        System.arraycopy(bakvos, 0, headvos, 0, headvos.length);
        retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
      }

      SaleorderBVO[] voaItem = queryAllBodyDataByWhere(sbodysql, null);

      retobjs[1] = voaItem;
      if ((voaItem != null) && (voaItem.length > 5000)) {
        CircularlyAccessibleValueObject[] bakvos = voaItem;
        voaItem = new SaleorderBVO[5000];
        System.arraycopy(bakvos, 0, voaItem, 0, voaItem.length);
        retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
      }

      return retobjs;
    }
    catch (Exception ee) {
      SCMEnv.out(ee.getMessage());
      throw new BusinessException(ee.getMessage());
    }
  }

  private void checkBuyLargess(SaleOrderVO vo)
    throws Exception
  {
    HashMap hp = new HashMap();
    SaleorderBVO[] bvos = vo.getBodyVOs();
    for (int i = 0; i < bvos.length; i++) {
      if ((bvos[i].getAttributeValue("clargessrowno") == null) || (bvos[i].getAttributeValue("clargessrowno").toString().trim().length() <= 0) || (bvos[i].getBlargessflag() == null) || (!bvos[i].getBlargessflag().booleanValue()))
      {
        continue;
      }

      hp.put(bvos[i].getAttributeValue("clargessrowno"), bvos[i]);
    }

    if (hp.size() == 0) {
      return;
    }
    Vector vInvs = new Vector();
    Vector vnewVOs = new Vector();
    Vector vtchantype = new Vector();
    for (int i = 0; i < bvos.length; i++) {
      if (hp.containsKey(bvos[i].getCrowno())) {
        vInvs.add(bvos[i].getCinventoryid());
        vnewVOs.add(bvos[i]);
        if ((bvos[i].getCchantypeid() == null) || (bvos[i].getCchantypeid().trim().length() <= 0))
          continue;
        vtchantype.add(bvos[i].getCchantypeid());
      }
    }

    String[] UnitInvs = new String[vInvs.size()];
    vInvs.copyInto(UnitInvs);

    SaleorderHVO hvo = vo.getHeadVO();
    String pk_corp = hvo.getPk_corp();
    String dBillDate = hvo.getDbilldate().toString();
    String pk_cumandoc = hvo.getCcustomerid();
    String ccurrencytypeid = hvo.getCcurrencytypeid();
    String[] cchantypeid = null;
    if (vtchantype.size() > 0) {
      cchantypeid = new String[vtchantype.size()];
      vtchantype.copyInto(cchantypeid);
    }
    BuyLargessImpl buy = new BuyLargessImpl();
    BuylargessVO[] blgvos = null;

    HashMap hmp = getCustgroupCodes(cchantypeid);

    SaleorderBVO[] bnewvos = new SaleorderBVO[vnewVOs.size()];
    vnewVOs.copyInto(bnewvos);
    boolean bfind = false;
    SaleorderBVO bmainlarvo = null;
    BuylargessVO larvo = null;
    for (int i = 0; i < bnewvos.length; i++)
    {
      try {
        blgvos = buy.queryLargessItems(pk_corp, UnitInvs, dBillDate, new String[] { bnewvos[i].getCchantypeid() == null ? null : bnewvos[i].getCchantypeid() }, pk_cumandoc, ccurrencytypeid);
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }
      if ((blgvos == null) || (blgvos.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000522"));
      }

      Hashtable htlargess = new Hashtable();
      String key = null;
      ArrayList al = null;
      for (int j = 0; j < blgvos.length; j++) {
        key = ((BuylargessHVO)blgvos[j].getParentVO()).getPk_invmandoc() + ((BuylargessHVO)blgvos[j].getParentVO()).getCunitid();

        if (htlargess.containsKey(key)) {
          al = (ArrayList)htlargess.get(key);
          al.add(blgvos[j]);
        }
        else {
          al = new ArrayList();
          al.add(blgvos[j]);
          htlargess.put(((BuylargessHVO)blgvos[j].getParentVO()).getPk_invmandoc() + ((BuylargessHVO)blgvos[j].getParentVO()).getCunitid(), al);
        }

      }

      larvo = findLargess(vo.getHeadVO(), bnewvos[i], htlargess, hmp);

      if (larvo == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000522"));
      }

      bmainlarvo = (SaleorderBVO)hp.get(bnewvos[i].getCrowno());
      for (int k = 0; k < larvo.getChildrenVO().length; k++) {
        if ((!bmainlarvo.getCinventoryid().equals(larvo.getChildrenVO()[k].getAttributeValue("pk_invmandoc"))) || (!bmainlarvo.getCunitid().equals(larvo.getChildrenVO()[k].getAttributeValue("cunitid"))))
        {
          continue;
        }

        compareMainInvWithLargess((BuylargessHVO)larvo.getParentVO(), (BuylargessBVO)larvo.getChildrenVO()[k], bmainlarvo);

        bfind = true;
        break;
      }

      if (!bfind)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000522"));
    }
  }

  private HashMap getCustgroupCodes(String[] sKeys)
    throws Exception
  {
    HashMap hp = new HashMap();
    if ((sKeys == null) || (sKeys.length == 0))
      return hp;
    String swheres = "pk_defdoc in(";
    for (int i = 0; i < sKeys.length; i++) {
      if (i > 0) {
        swheres = swheres + ",";
      }
      swheres = swheres + "'" + sKeys[i] + "'";
    }
    swheres = swheres + ")";
    SmartDMO dmo = new SmartDMO();
    Object[] o = dmo.selectBy2("select pk_defdoc,doccode from bd_defdoc where " + swheres);

    Object[] o1 = null;
    if ((o == null) || (o.length == 0))
      return hp;
    for (int i = 0; i < o.length; i++) {
      o1 = (Object[])(Object[])o[i];

      hp.put(o1[0], o1[1]);
    }
    return hp;
  }

  private BuylargessVO findLargess(SaleorderHVO hsalevo, SaleorderBVO bsalevo, Hashtable htLargess, HashMap hmp)
  {
    String sInvPk = bsalevo.getCinventoryid() + bsalevo.getCquoteunitid();

    if (!htLargess.containsKey(sInvPk)) {
      return null;
    }
    ArrayList allargess = (ArrayList)htLargess.get(sInvPk);

    UFDouble nnum = bsalevo.getNquoteunitnum();
    if (nnum == null) {
      nnum = new UFDouble(0);
    }
    BuylargessVO vo = null;
    BuylargessHVO bhvo = null;

    BuylargessVO votmp = null;
    BuylargessHVO btmphvo = null;

    String scustgroup = null;
    String stmpcustgroup = null;
    int i = 0; for (int isize = allargess.size(); i < isize; i++) {
      votmp = (BuylargessVO)allargess.get(i);
      btmphvo = (BuylargessHVO)votmp.getParentVO();

      if (btmphvo.getNbuynum().compareTo(nnum) > 0)
        continue;
      if (vo == null) {
        vo = votmp;
      }
      else {
        bhvo = (BuylargessHVO)vo.getParentVO();

        if ((btmphvo.getPk_cumandoc() != null) && (btmphvo.getPk_cumandoc().trim().length() > 0))
        {
          vo = votmp; } else {
          if ((btmphvo.getPk_custgroup() == null) || (btmphvo.getPk_custgroup().trim().length() <= 0))
          {
            continue;
          }
          if ((bhvo.getPk_custgroup() != null) && (bhvo.getPk_custgroup().trim().length() > 0))
          {
            scustgroup = (String)hmp.get(bhvo.getPk_custgroup());
            stmpcustgroup = (String)hmp.get(btmphvo.getPk_custgroup());

            if ((scustgroup == null) || (stmpcustgroup == null) || (scustgroup.trim().length() >= stmpcustgroup.trim().length()))
            {
              continue;
            }
            vo = votmp;
          }
          else {
            if (((bhvo.getPk_cumandoc() != null) && (bhvo.getPk_cumandoc().trim().length() != 0)) || ((bhvo.getPk_custgroup() != null) && (bhvo.getPk_custgroup().trim().length() != 0)))
            {
              continue;
            }
            vo = votmp;
          }
        }
      }

    }

    return vo;
  }

  private void compareMainInvWithLargess(BuylargessHVO hvo, BuylargessBVO bvo, SaleorderBVO bmainvo)
    throws BusinessException
  {
    UFDouble umainnum = bmainvo.getNquoteunitnum();
    if (umainnum == null) {
      umainnum = new UFDouble(0);
    }
    umainnum = umainnum.div(hvo.getNbuynum());

    if ((bvo.getFtoplimittype() != null) && (bvo.getFtoplimittype().intValue() == 0)) if (bmainvo.getNoriginalcursummny().compareTo(bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo.getNtoplimitvalue()) > 0)
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000520", null, new String[] { bvo.getNtoplimitvalue() == null ? "" : bvo.getNtoplimitvalue().toString(), bmainvo.getCrowno() }));
      }


    if ((bvo.getFtoplimittype() != null) && (bvo.getFtoplimittype().intValue() == 1) && (bmainvo.getNquoteunitnum().compareTo(bvo.ntoplimitvalue) > 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000521", null, new String[] { bvo.getNtoplimitvalue() == null ? "" : bvo.getNtoplimitvalue().toString(), bmainvo.getCrowno() }));
    }
  }

  protected SaleOrderVO getNumberBySO58(SaleOrderVO vo, Vector laborItems, int BD501)
    throws BusinessException
  {
    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0))
    {
      return vo = null;
    }SaleorderBVO[] items = (SaleorderBVO[])(SaleorderBVO[])vo.getChildrenVO();

    if ((items == null) || (items.length <= 0)) {
      return vo = null;
    }
    try
    {
      Vector vtDir = new Vector();
      Vector vtLeft = new Vector();
      Vector vItem = new Vector();

      String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");

      String consigncorpid = null;

      for (int i = 0; i < items.length; i++) {
        consigncorpid = items[i].getCconsigncorpid();

        if ((items[i].getBdericttrans() != null) && (items[i].getBdericttrans().booleanValue()) && (consigncorpid != null) && (consigncorpid.trim().length() > 0) && (!consigncorpid.equals(pk_corp)))
        {
          vtDir.add(items[i]);
        }
        else {
          vtLeft.add(items[i]);
        }
      }

      if (vtLeft.size() > 0) {
        items = new SaleorderBVO[vtLeft.size()];
        vtLeft.copyInto(items);

        DefaultVOMerger vom = new DefaultVOMerger();
        try {
          vom.setGroupingAttr(new String[] { "cinventoryid", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "dconsigndate" });

          vom.setSummingAttr(new String[] { "nnumber" });
          items = (SaleorderBVO[])(SaleorderBVO[])vom.mergeByGroup(items);

          if ((items == null) || (items.length <= 0))
            return vo = null;
        }
        catch (Exception e) {
          throw e;
        }

        UFDouble[] vals = getAtpNum(vo, items);

        if (vals == null) {
          vals = new UFDouble[items.length];
          for (int i = 0; i < vals.length; i++) {
            vals[i] = new UFDouble(0);
          }

        }
        else
        {
          for (int i = 0; i < vals.length; i++) {
            vals[i] = vals[i].add(items[i].getNnumber() == null ? new UFDouble(0) : items[i].getNnumber());
          }

        }

        int prec = BD501;

        for (int i = 0; i < items.length; i++)
        {
          if (items[i].getNnumber() == null) {
            continue;
          }
          if ((items[i].getNnumber().doubleValue() - vals[i].doubleValue() < 0.0D) && (!laborItems.contains(items[i].getCinvbasdocid())))
          {
            continue;
          }
          consigncorpid = items[i].getCconsigncorpid();

          SaleorderBVO[] tempVO = null;
          if (!laborItems.contains(items[i].getCinvbasdocid()))
          {
            items[i].setDirty(true);
            tempVO = (SaleorderBVO[])(SaleorderBVO[])vom.writeBackValue(items[i], "nnumber", vals[i], prec);
          }

          if (((tempVO == null) || (tempVO.length <= 0)) && (laborItems.contains(items[i].getCinvbasdocid())))
          {
            vItem.add(items[i]);
          } else {
            if ((tempVO == null) || (tempVO.length <= 0)) {
              continue;
            }
            for (int j = 0; j < tempVO.length; j++) {
              vItem.addElement(tempVO[j]);
            }
          }
        }

      }

      if ((vItem.size() <= 0) && (vtDir.size() <= 0)) {
        System.out.println("当前库存可用量能够满足此次销售转成的需求");
        vo = null;
      }
      items = null;
      if (vItem.size() > 0) {
        items = new SaleorderBVO[vItem.size()];
        vItem.copyInto(items);
      }
      if (vtDir.size() > 0) {
        SaleorderBVO[] tmpitems = new SaleorderBVO[vtDir.size()];
        vtDir.copyInto(tmpitems);
        if (items != null) {
          SaleorderBVO[] tmpitems1 = new SaleorderBVO[items.length + tmpitems.length];

          for (int i = 0; i < items.length; i++) {
            tmpitems1[i] = items[i];
          }
          int i = 0; for (int iLen = tmpitems.length; i < iLen; i++) {
            tmpitems1[(items.length + i)] = tmpitems[i];
          }
          items = tmpitems1;
        } else {
          items = tmpitems;
        }
      }

      if (items != null) {
        vo.setChildrenVO(items);
      }
      return vo;
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }
    
  }

  private UFDouble[] getAtpNum(AggregatedValueObject vo, SaleorderBVO[] items)
    throws Exception
  {
    UFDouble[] ufdRslt = null;
    try {
      String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");

      String consigncorpid = null;
      String sCstore = null;

      if ((pk_corp == null) || (pk_corp.trim().equals(""))) {
        return null;
      }

      IICPub_InvATPDMO dmo = (IICPub_InvATPDMO)NCLocator.getInstance().lookup(IICPub_InvATPDMO.class.getName());

      if ((items == null) || (items.length <= 0))
        return null;
      ufdRslt = new UFDouble[items.length];

      for (int i = 0; i < items.length; i++) {
        consigncorpid = items[i].getCconsigncorpid();

        if ((consigncorpid == null) || (consigncorpid.trim().length() <= 0) || (consigncorpid.equals(pk_corp)))
        {
          sCstore = items[i].getCadvisecalbodyid();
        }
        else sCstore = items[i].getCreccalbodyid();

        if (sCstore == null) {
          return null;
        }
        ufdRslt[i] = dmo.getATPNum(pk_corp, sCstore, (String)items[i].getAttributeValue("cinventoryid"), (String)items[i].getAttributeValue("vfree1"), (String)items[i].getAttributeValue("vfree2"), (String)items[i].getAttributeValue("vfree3"), (String)items[i].getAttributeValue("vfree4"), (String)items[i].getAttributeValue("vfree5"), null, null, null, null, null, items[i].getAttributeValue("dconsigndate") == null ? null : items[i].getAttributeValue("dconsigndate").toString());
      }

    }
    catch (Exception e)
    {
      throw e;
    }
    return ufdRslt;
  }

  protected Vector getLabors(SaleorderBVO[] items) throws BusinessException {
    try {
      String[] basids = new String[items.length];
      for (int i = 0; i < items.length; i++) {
        basids[i] = items[i].getCinvbasdocid();
      }

      HashMap laborFlags = isLabor(basids);
      Vector laborItems = new Vector();
      String isLabor = "N";
      for (int i = 0; i < items.length; i++) {
        isLabor = laborFlags.get(items[i].getCinvbasdocid()).toString();
        if (isLabor.equals("Y")) {
          laborItems.add(items[i].getCinvbasdocid());
        }
      }
      return laborItems;
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }
    
  }

  private HashMap isLabor(String[] cMangID)
    throws SQLException
  {
    if ((cMangID == null) || (cMangID.length == 0)) {
      return null;
    }
    StringBuffer subcon = new StringBuffer();
    HashMap retIDs = new HashMap();
    for (int i = 0; i < cMangID.length; i++) {
      if (i < cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "',");
      } else if (i == cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "'");
      }
    }
    if (subcon.toString() == null) {
      return null;
    }
    String sql = "select laborflag,pk_invbasdoc from bd_invbasdoc  where pk_invbasdoc in (" + subcon.toString() + ")";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String laborFlag = rs.getString(1);
        String invbas = rs.getString(2);
        if ((laborFlag == null) || (laborFlag.trim().length() == 0)) {
          laborFlag = "N";
        }
        retIDs.put(invbas, laborFlag);
      }
    }
    finally
    {
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
      catch (Exception e) {
      }
    }
    return retIDs;
  }

  public void incomeAdjust_endForOrder(SaleorderHVO[] hvos, ArrayList rowendvolist) throws BusinessException
  {
    try {
      SaleOrderVO[] vos = toSaleOrderVO(hvos, rowendvolist);
      if ((vos != null) && (vos.length > 0)) {
        IncomeAdjust iadjust = new IncomeAdjust();
        int i = 0; for (int iLen = vos.length; i < iLen; i++)
          iadjust.endForOrder(vos[i]);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.toString());
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void incomeAdjust_unEndForOrder(SaleorderHVO[] hvos, ArrayList rowopenvolist) throws BusinessException
  {
    try {
      SaleOrderVO[] vos = toSaleOrderVO(hvos, rowopenvolist);
      if ((vos != null) && (vos.length > 0)) {
        IncomeAdjust iadjust = new IncomeAdjust();
        int i = 0; for (int iLen = vos.length; i < iLen; i++)
          iadjust.unEndForOrder(vos[i]);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.toString());
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean checkValidOrNeed(AggregatedValueObject srcBillVo, String destBilltype, String drivedAction)
    throws BusinessException
  {
    return true;
  }

  public HashSet filterUsers(String srcBilltype, String destBilltype, AggregatedValueObject billvo, RoleVO[] roles)
  {
    if ((billvo == null) || (billvo.getChildrenVO() == null) || (billvo.getChildrenVO().length == 0))
    {
      SCMEnv.out("billvo == null || billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0，直接返回");

      return null;
    }

    HashSet hashRet = new HashSet();
    SaleorderBVO[] items = (SaleorderBVO[])(SaleorderBVO[])billvo.getChildrenVO();
    int iLen = items.length;

    if ("4C".equalsIgnoreCase(destBilltype)) {
      StoreadminBodyVO[] vosToIc = new StoreadminBodyVO[iLen];
      for (int i = 0; i < iLen; i++) {
        vosToIc[i] = new StoreadminBodyVO();
        vosToIc[i].setPk_corp((String)items[i].getAttributeValue("pk_corp"));

        vosToIc[i].setCcalbodyid(items[i].getCadvisecalbodyid());
        vosToIc[i].setCinventoryid(items[i].getCinventoryid());
        vosToIc[i].setCwarehouseid(items[i].getCbodywarehouseid());
      }
      IICToPU_StoreadminDMO icFilterSrv = (IICToPU_StoreadminDMO)NCLocator.getInstance().lookup(IICToPU_StoreadminDMO.class.getName());
      try
      {
        ArrayList listRetFromIc = icFilterSrv.getUserArrayForStore(vosToIc, roles);

        if (listRetFromIc != null)
          hashRet.addAll(listRetFromIc);
      }
      catch (Exception e) {
        SCMEnv.out("调用 IC 服务IICToPU_StoreadminDMO.getUserArrayForStore(vosToIc, roles)异常：");

        SCMEnv.out(e);
      }

    }
    else if ("32".equalsIgnoreCase(destBilltype))
    {
      try {
        IRoleManageQuery roleQry = (IRoleManageQuery)NCLocator.getInstance().lookup(IRoleManageQuery.class.getName());

        UserVO[] voaUser = null;
        int kLen = roles.length;

        for (int i = 0; i < kLen; i++) {
          voaUser = roleQry.getUsers(roles[i].getPk_role(), roles[i].getPk_corp());

          if (voaUser != null) {
            for (int j = 0; j < voaUser.length; j++)
              hashRet.add(voaUser[j].getPrimaryKey());
          }
        }
      }
      catch (Exception e)
      {
        System.err.println("订单给发票下游消息获取信息出错，消息接收用户设置出错");
        SCMEnv.out(e);
      }
    }

    return hashRet;
  }

  public static void checkIsExecuteNumRigth(String[] sIds)
    throws BusinessException
  {
    try
    {
      HashMap hp = SOToolsDMO.getAnyValueArray("so_saleexecute", new String[] { "ntotalinventorynumber", "ntotalreturnnumber", "ntranslossnum" }, "csale_bid", sIds, " creceipttype = '30' ");

      if ((hp == null) || (hp.size() == 0))
        return;
      UFDouble ntotalinventorynumber = null;
      UFDouble ntotalreturnnumber = null;
      UFDouble ntranslossnum = null;
      String[] sValues = null;
      for (int i = 0; i < sIds.length; i++) {
        if (hp.containsKey(sIds[i])) {
          sValues = (String[])(String[])hp.get(sIds[i]);
          if ((sValues == null) || (sValues.length == 0))
            return;
          ntotalinventorynumber = sValues[0] == null ? new UFDouble(0) : new UFDouble(sValues[0]);

          ntotalreturnnumber = sValues[1] == null ? new UFDouble(0) : new UFDouble(sValues[1]);

          ntranslossnum = sValues[2] == null ? new UFDouble(0) : new UFDouble(sValues[2]);

          if (ntotalinventorynumber.doubleValue() >= ntotalreturnnumber.doubleValue() + ntranslossnum.doubleValue())
            continue;
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000039", null, new String[] { String.valueOf(i + 1) }));
        }

      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
  }

  public SourceBillInfo[] findSourceBill(String pk_srcBilltype, AggregatedValueObject billVO)
  {
    if ((billVO == null) || (billVO.getParentVO() == null)) {
      return null;
    }
    String cbilltypecode = (String)billVO.getParentVO().getAttributeValue("cbilltypecode");

    if (cbilltypecode == null) {
      return null;
    }

    if ("30".equals(cbilltypecode)) {
      try
      {
        return CommonDataDMO.findSourceBill(pk_srcBilltype, cbilltypecode, billVO);
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    return null;
  }
}