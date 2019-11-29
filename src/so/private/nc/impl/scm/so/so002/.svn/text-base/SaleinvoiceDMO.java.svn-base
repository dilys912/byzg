package nc.impl.scm.so.so002;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.IBackCheckState;
import nc.bs.pub.pf.ICheckState;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.scm.ic.freeitem.DefdefDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.pub.BusinessControlDMO;
import nc.impl.scm.so.pub.CheckValueValidityImpl;
import nc.impl.scm.so.pub.FetchValueDMO;
import nc.impl.scm.so.pub.GeneralSqlString;
import nc.impl.scm.so.so015.ArsubDMOImpl;
import nc.impl.scm.so.so016.BalanceDMO;
import nc.itf.dap.pub.IDapSendMessage;
import nc.itf.ic.service.IIC2SO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.pu.inter.IPuToSo_PuToSoDMO;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.ic.ic004.StoreadminBodyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.credit.AccountMnyVO;
import nc.vo.so.credit.BillCreditOriginVO;
import nc.vo.so.pub.VOTools;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so015.ARSubUniteVO;
import nc.vo.so.so016.InvoiceBalVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.uap.rbac.RoleVO;

public class SaleinvoiceDMO extends DataManageObject
  implements IQueryData, IQueryData2, ICheckState, IBackCheckState
{
  public SaleinvoiceDMO()
    throws NamingException, SystemException
  {
  }

  public SaleinvoiceDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void delete(String key)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      deleteBodys(key);
      deleteHead(key);
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

  public void approve(String key)
    throws SQLException
  {
    String sql = "update so_saleinvoice set daudittime=?, fstatus=2 where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setString(1, new UFDateTime(new Date()).toString());
    stmt.setString(2, key);
    stmt.executeUpdate();
  }

  public void auditAfterInsert(SaleinvoiceVO hvo)
  {
    try
    {
      if (hvo != null) {
        SaleVO header = (SaleVO)hvo.getParentVO();
        if (header != null) {
          BusinessControlDMO dmo = new BusinessControlDMO();
          dmo.setBillAudit("32", header.getPrimaryKey(), header.getCoperatorid(), header.getDbilldate().toString(), 2);
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }
  }

  public boolean checkArSubValidity(Hashtable hsArsub, boolean bstrikeflag)
	   throws java.sql.SQLException, nc.bs.pub.SystemException {
		    /** ********************************************************** */
		    // 保留的系统管理接口：
		    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "checkArSubValidity",
		        new Object[] {
		          hsArsub
		        });
		    /** ********************************************************** */
		    if (bstrikeflag == true) {
		      String sql = "select nsubmny - isnull(ntotalsubmny,0) as nremainmny from so_arsub where carsubid = ?";
		      Connection con = null;
		      PreparedStatement stmt = null;
		      try {
		        con = getConnection();
		        stmt = con.prepareStatement(sql);

		        java.util.Enumeration eKey = hsArsub.keys();
		        while (eKey.hasMoreElements()) {
		          String key = eKey.nextElement().toString();
		          stmt.setString(1, key);
		          ResultSet rs = stmt.executeQuery();
		          while (rs.next()) {
		            UFDouble nremainmny = new UFDouble(rs.getBigDecimal("nremainmny"));
		            if (((UFDouble) hsArsub.get(key)).compareTo(nremainmny) > 0)
		              return false;

		          }
		        }
		        return true;

		      }
		      finally {
		        try {
		          if (stmt != null) {
		            stmt.close();
		          }
		        }
		        catch (Exception e) {
		        }
		        try {
		          if (con != null) {
		            con.close();
		          }
		        }
		        catch (Exception e) {
		        }
		      }
		    }
		    else {
		      return true;
		    }

  }

  public boolean checkGoing(String strBillID, String strApproveId, String strApproveDate, String checkNote)
  {
    try
    {
      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("32", strBillID, null, null, 7);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());

      return false;
    }

    return true;
  }

  public boolean checkNoPass(String strBillID, String strApproveID, String strApproveDate, String checkNote)
  {
    try
    {
      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("32", strBillID, null, null, 8);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());

      return false;
    }

    return true;
  }

  public boolean checkPass(String strBillID, String strApproveId, String strApproveDate, String strCheckNote)
  {
    try
    {
      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("32", strBillID, strApproveId, strApproveDate, 2);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());

      return false;
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

      SCMEnv.out("驳回后台更改审批状态开始...[驳回]");

      BusinessControlDMO dmo = new BusinessControlDMO();
      dmo.setBillAudit("32", billId, null, null, 1);

      SCMEnv.out("驳回后台更改审批状态用时[驳回]：[" + (System.currentTimeMillis() - s) + "]" + "ms");
    }
    catch (RemoteException e)
    {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new RemoteException("Remote Call", e);
    }
  }

  public void close(String key)
    throws SQLException
  {
    String sql = "update so_saleinvoice set fstatus=4 where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

  public void deleteBody(String key)
    throws SQLException
  {
    String sql = "delete from so_saleinvoice_b where cinvoice_bid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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
    deleteFollowBody(key);
  }

  public void deleteBodys(String key)
    throws SQLException
  {
    String sql = "delete from so_saleinvoice_b where csaleid = ?";
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

  public void deleteFollowBody(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "delete", new Object[] { key });

    String sql = "delete from so_saleexecute where csale_bid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "delete", new Object[] { key });
  }

  public void deleteHead(String key)
    throws SQLException
  {
    String sql = "delete from so_saleinvoice where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

  public void freeze(String key)
    throws SQLException
  {
    String sql = "update so_saleinvoice set fstatus=5 where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

  private Hashtable getCustBasIDByCustManID(String[] strCustomerIDs)
    throws SystemException, SQLException
  {
    try
    {
      FetchValueDMO fdmo = new FetchValueDMO();
      return fdmo.fetchArrayValue("bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", strCustomerIDs);
    } catch (NamingException e) {
    	 throw new SQLException(e.getMessage());
    }
   
  }

  public String[][] getCustomerInfo(String strID)
    throws SQLException
  {
    String strSql = "select bd_cubasdoc.phone1, bd_cubasdoc.taxpayerid, bd_custbank.accname,bd_custbank.account,bd_custbank.pk_custbank,bd_cubasdoc.pk_cubasdoc,bd_cubasdoc.custname,bd_custaddr.pk_custaddr from bd_cumandoc left outer join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc left outer join bd_custbank on bd_custbank.pk_cubasdoc=bd_cubasdoc.pk_cubasdoc left outer join bd_custaddr on bd_custaddr.pk_cubasdoc=bd_cubasdoc.pk_cubasdoc where  bd_cumandoc.pk_cumandoc = ? ";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    Vector v = new Vector();
    String[][] results = (String[][])null;
    try {
      stmt.setString(1, strID);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Vector vLine = new Vector();
        String obj = rs.getString(1);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(2);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(3);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(4);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(5);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(6);
        vLine.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(7);
        vLine.addElement(obj == null ? "" : obj.trim());

        obj = rs.getString(8);
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

  public void getDataFrom4CTo32(SaleinvoiceVO saleinvoice, String cfirsttype)
    throws SQLException
  {
  }

  public SaleinvoiceVO getGather(SaleinvoiceVO vo)
    throws SQLException, BusinessException
  {
    SaleVO headVO = (SaleVO)vo.getParentVO();
    SaleinvoiceBVO[] bodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vo.getChildrenVO();
    SaleinvoiceVO retVO = new SaleinvoiceVO();
    retVO.setParentVO(vo.getParentVO());

    String sCust = headVO.getCreceiptcorpid();

    String sCalbodyid = headVO.getCcalbodyid();
    Vector vecTemp = new Vector();

    Vector invNoData = new Vector();
    for (int i = 0; i < bodyVO.length; i++)
    {
      String sInv = bodyVO[i].getCinventoryid();

      SaleinvoiceBVO[] sourceVOs = getGatherSource(sCust, sCalbodyid, sInv);

      String cupsourcebillcode = null;
      String coriginalbillcode = null;
      String cadvisecalbodyid = null;
      if ((sourceVOs != null) && (sourceVOs.length > 0)) {
        for (int k = 0; k < sourceVOs.length; k++) {
          try {
            FetchValueDMO dmo = new FetchValueDMO();
            cupsourcebillcode = dmo.getColValue("ic_general_h", "vbillcode", "cgeneralhid='" + sourceVOs[k].getCupsourcebillid() + "'");

            coriginalbillcode = dmo.getColValue("so_sale", "vreceiptcode", "csaleid='" + sourceVOs[k].getCsourcebillid() + "'");

            cadvisecalbodyid = dmo.getColValue("so_saleorder_b", "cadvisecalbodyid", "corder_bid='" + sourceVOs[k].getCsourcebillbodyid() + "'");
          }
          catch (Exception e1)
          {
            e1.printStackTrace();
            throw new BusinessException(e1.toString());
          }
          sourceVOs[k].setCupsourcebillcode(cupsourcebillcode);

          sourceVOs[k].setCoriginalbillcode(coriginalbillcode);

          sourceVOs[k].setCadvisecalbodyid(cadvisecalbodyid);
        }
      }

      int sourceSize = sourceVOs.length;
      if (sourceSize == 0) {
        invNoData.add(new Integer(i + 1));
      }
      else
      {
        UFDouble invoiceNum = bodyVO[i].getNnumber();

        UFDouble sumSourceNum = null;
        if ((sourceVOs == null) || (sourceVOs.length == 0))
          sumSourceNum = invoiceNum;
        else {
          sumSourceNum = sourceVOs[0].getNnumber();
        }
        for (int j = 0; j < sourceSize; j++)
          if (invoiceNum.doubleValue() > sumSourceNum.doubleValue())
          {
            SaleinvoiceBVO voLast = sourceVOs[j];

            CircularlyAccessibleValueObject[] vos = { voLast };
            SoVoTools.execFormulasAtBs(new String[] { "nqtscalefactor->getColValue(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid)", "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)" }, vos);

            voLast = (SaleinvoiceBVO)vos[0];
            voLast.setNpacknumber(BillTools.calc(voLast.getNnumber(), voLast.getNqtscalefactor(), 3));

            voLast.setNoriginalcurnetprice(BillTools.calc(voLast.getNoriginalcurprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNoriginalcurtaxnetprice(BillTools.calc(voLast.getNoriginalcurtaxprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNtaxnetprice(BillTools.calc(voLast.getNtaxprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNoriginalcurmny(BillTools.calc(voLast.getNoriginalcurnetprice(), voLast.getNnumber(), 2));

            voLast.setNoriginalcurtaxmny(BillTools.calc(voLast.getNoriginalcurmny(), voLast.getNtaxrate().div(new UFDouble(100.0D)), 2));

            voLast.setNoriginalcursummny(BillTools.calc(voLast.getNoriginalcurmny(), voLast.getNoriginalcurtaxmny(), 0));

            voLast.setNoriginalcurdiscountmny(BillTools.calc(voLast.getNoriginalcurtaxprice(), voLast.getNnumber(), 2));

            voLast.setNoriginalcurdiscountmny(BillTools.calc(voLast.getNoriginalcurdiscountmny(), voLast.getNoriginalcursummny(), 1));

            voLast.setNmny(BillTools.calc(voLast.getNnetprice(), voLast.getNnumber(), 2));

            voLast.setNtaxmny(BillTools.calc(voLast.getNmny(), voLast.getNtaxrate().div(new UFDouble(100.0D)), 2));

            voLast.setNsummny(BillTools.calc(voLast.getNmny(), voLast.getNtaxmny(), 0));

            voLast.setNdiscountmny(BillTools.calc(voLast.getNtaxprice(), voLast.getNnumber(), 2));

            voLast.setNdiscountmny(BillTools.calc(voLast.getNdiscountmny(), voLast.getNsummny(), 1));

            voLast.setNsubsummny(voLast.getNoriginalcursummny());

            voLast.setNsubcursummny(voLast.getNsummny());

            voLast.setCcustomerid(sCust);
            vecTemp.add(voLast);
            if (j + 1 != sourceSize)
              sumSourceNum = sumSourceNum.add(sourceVOs[(j + 1)].getNnumber());
          }
          else {
            SaleinvoiceBVO voLast = sourceVOs[j];
            voLast.setNnumber(invoiceNum.sub(sumSourceNum.sub(sourceVOs[j].getNnumber())));

            CircularlyAccessibleValueObject[] vos = { voLast };
            SoVoTools.execFormulasAtBs(new String[] { "nqtscalefactor->getColValue(bd_convert,mainmeasrate,pk_invbasdoc,cinvbasdocid)", "cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)", "cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)" }, vos);

            voLast = (SaleinvoiceBVO)vos[0];
            voLast.setNpacknumber(BillTools.calc(voLast.getNnumber(), voLast.getNqtscalefactor(), 3));

            voLast.setNoriginalcurnetprice(BillTools.calc(voLast.getNoriginalcurprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNoriginalcurtaxnetprice(BillTools.calc(voLast.getNoriginalcurtaxprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNtaxnetprice(BillTools.calc(voLast.getNtaxprice(), voLast.getNdiscountrate().multiply(voLast.getNitemdiscountrate()).div(10000.0D), 2));

            voLast.setNoriginalcurmny(BillTools.calc(voLast.getNoriginalcurnetprice(), voLast.getNnumber(), 2));

            voLast.setNoriginalcurtaxmny(BillTools.calc(voLast.getNoriginalcurmny(), voLast.getNtaxrate().div(new UFDouble(100.0D)), 2));

            voLast.setNoriginalcursummny(BillTools.calc(voLast.getNoriginalcurmny(), voLast.getNoriginalcurtaxmny(), 0));

            voLast.setNoriginalcurdiscountmny(BillTools.calc(voLast.getNoriginalcurtaxprice(), voLast.getNnumber(), 2));

            voLast.setNoriginalcurdiscountmny(BillTools.calc(voLast.getNoriginalcurdiscountmny(), voLast.getNoriginalcursummny(), 1));

            voLast.setNmny(BillTools.calc(voLast.getNnetprice(), voLast.getNnumber(), 2));

            voLast.setNtaxmny(BillTools.calc(voLast.getNmny(), voLast.getNtaxrate().div(new UFDouble(100.0D)), 2));

            voLast.setNsummny(BillTools.calc(voLast.getNmny(), voLast.getNtaxmny(), 0));

            voLast.setNdiscountmny(BillTools.calc(voLast.getNtaxprice(), voLast.getNnumber(), 2));

            voLast.setNdiscountmny(BillTools.calc(voLast.getNdiscountmny(), voLast.getNsummny(), 1));

            voLast.setNsubsummny(voLast.getNoriginalcursummny());

            voLast.setNsubcursummny(voLast.getNsummny());

            vecTemp.add(voLast);
            break;
          }
      }
    }
    SaleinvoiceBVO[] invoiceItems = new SaleinvoiceBVO[vecTemp.size()];
    if (invNoData.size() != 0)
    {
      StringBuffer sEx = new StringBuffer("");
      for (int i = 0; i < invNoData.size(); i++) {
        if (i != 0)
          sEx.append(", ");
        sEx.append(invNoData.elementAt(i));
      }
      String sErr = NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000001", null, new String[] { sEx.toString() });

      throw new BusinessException(sErr);
    }
    if (vecTemp.size() > 0) {
      vecTemp.copyInto(invoiceItems);
    }
    retVO.setChildrenVO(invoiceItems);

    UFDouble ntotalmny = new UFDouble(0.0D);
    for (int i = 0; i < invoiceItems.length; i++) {
      if ((invoiceItems[i].getBlargessflag() == null ? new UFBoolean("N") : invoiceItems[i].getBlargessflag()).booleanValue())
        continue;
      ntotalmny = ntotalmny.add(invoiceItems[i].getNoriginalcursummny() == null ? new UFDouble(0.0D) : invoiceItems[i].getNoriginalcursummny());
    }

    ((SaleVO)retVO.getParentVO()).setNtotalsummny(ntotalmny);
    ((SaleVO)retVO.getParentVO()).setNstrikemny(new UFDouble(0.0D));
    ((SaleVO)retVO.getParentVO()).setNnetmny(ntotalmny);
    ((SaleVO)retVO.getParentVO()).setPk_corp(((SaleVO)vo.getParentVO()).getPk_corp());

    return retVO;
  }

  private SaleinvoiceBVO[] getGatherSource(String sCust, String sCalbodyid, String sInv)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ");
    sql.append("corder_bid, so_square_b.csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, ");

    sql.append("so_square_b.cinventoryid, noutnum  , nshouldoutnum, nbalancenum, ic_general_bb3.nsignnum, ");

    sql.append("ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, ");

    sql.append("cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, ");

    sql.append("noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, ");

    sql.append("nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, ");

    sql.append("vfree2, vfree3, vfree4, vfree5, so_square_b.vdef1, ");
    sql.append("so_square_b.vdef2, so_square_b.vdef3, so_square_b.vdef4, so_square_b.vdef5, so_square_b.vdef6, ");

    sql.append("so_square_b.vdef7, so_square_b.vdef8, so_square_b.vdef9, so_square_b.vdef10, so_square_b.vdef11,");

    sql.append("so_square_b.vdef12, so_square_b.vdef13, so_square_b.vdef14, so_square_b.vdef15, so_square_b.vdef16, ");

    sql.append("so_square_b.vdef17, so_square_b.vdef18, so_square_b.vdef19, so_square_b.vdef20, ");

    sql.append("so_square_b.pk_defdoc1, so_square_b.pk_defdoc2, so_square_b.pk_defdoc3, so_square_b.pk_defdoc4, ");

    sql.append("so_square_b.pk_defdoc5, so_square_b.pk_defdoc6, so_square_b.pk_defdoc7, so_square_b.pk_defdoc8, ");

    sql.append("so_square_b.pk_defdoc9, so_square_b.pk_defdoc10, so_square_b.pk_defdoc11, so_square_b.pk_defdoc12, ");

    sql.append("so_square_b.pk_defdoc13, so_square_b.pk_defdoc14, so_square_b.pk_defdoc15, so_square_b.pk_defdoc16, ");

    sql.append("so_square_b.pk_defdoc17, so_square_b.pk_defdoc18, so_square_b.pk_defdoc19, so_square_b.pk_defdoc20, ");

    sql.append("ic_general_h.ts, so_square_b.dr, so_square_b.creceipttype, so_square_b.nnetprice, so_square_b.ntaxnetprice, ");

    sql.append("so_square_b.cbodywarehouseid, so_square_b.cpackunitid , so_square_b.blargessflag, ");

    sql.append("so_square_b.nqtnetprc, so_square_b.nqtprc, so_square_b.nqttaxnetprc, so_square_b.nqttaxprc, so_square_b.nquoteunitnum, ");

    sql.append("so_square_b.nquoteunitrate, so_square_b.cquoteunitid, so_square_b.norgqtnetprc, so_square_b.norgqtprc, so_square_b.norgqttaxnetprc, ");

    sql.append("so_square_b.norgqttaxprc,ic_general_h.ccustomerid  ");
    sql.append("FROM so_square INNER JOIN ");
    sql.append("so_square_b ON ");
    sql.append("so_square_b.csaleid = so_square.csaleid INNER JOIN ");
    sql.append("ic_general_bb3 ON ");
    sql.append("ic_general_bb3.cgeneralbid = so_square_b.corder_bid INNER JOIN ");

    sql.append("ic_general_h ON ic_general_bb3.cgeneralhid = ic_general_h.cgeneralhid ");

    sql.append("WHERE (so_square .verifyrule = 'W') AND (so_square.creceipttype = '4C') ");

    sql.append("AND ISNULL(ic_general_bb3.nsignnum, 0) < so_square_b.noutnum ");

    sql.append("AND ic_general_h.dr = 0 ");

    if ((sCust != null) && (sCust.trim().length() != 0)) {
      sql.append("AND so_square.ccustomerid = '" + sCust + "' ");
    }
    if ((sCalbodyid != null) && (sCalbodyid.trim().length() != 0)) {
      sql.append("AND so_square.ccalbodyid = '" + sCalbodyid + "' ");
    }
    if ((sInv != null) && (sInv.trim().length() != 0))
      sql.append("AND so_square_b.cinventoryid = '" + sInv + "' ");
    sql.append("ORDER BY so_square_b.ts");
    SaleinvoiceBVO[] invoiceItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleinvoiceBVO invoiceItem = new SaleinvoiceBVO();

        String corder_bid = rs.getString("corder_bid");
        invoiceItem.setCupsourcebillbodyid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        invoiceItem.setCupsourcebillid(csaleid == null ? null : csaleid.trim());

        invoiceItem.setCupreceipttype("4C");

        String csourcebillid = rs.getString("csourcebillid");
        invoiceItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        invoiceItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        invoiceItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        invoiceItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        invoiceItem.setNnumber(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        invoiceItem.setNnumber(nsignnum == null ? invoiceItem.getNnumber() : invoiceItem.getNnumber().sub(new UFDouble(nsignnum)));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        invoiceItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        invoiceItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        invoiceItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        invoiceItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        invoiceItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        invoiceItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        invoiceItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        invoiceItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        invoiceItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        invoiceItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        invoiceItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        invoiceItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        invoiceItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        invoiceItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");

        invoiceItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");

        invoiceItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");

        invoiceItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        invoiceItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        invoiceItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        invoiceItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        invoiceItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        invoiceItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        invoiceItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        invoiceItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        invoiceItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        invoiceItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        invoiceItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        invoiceItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        invoiceItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        invoiceItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        invoiceItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        invoiceItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        invoiceItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        invoiceItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        invoiceItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        invoiceItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        invoiceItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        invoiceItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        invoiceItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        invoiceItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        invoiceItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        invoiceItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        invoiceItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        invoiceItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        invoiceItem.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        invoiceItem.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        invoiceItem.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        invoiceItem.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        invoiceItem.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        invoiceItem.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        invoiceItem.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        invoiceItem.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        invoiceItem.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        invoiceItem.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        invoiceItem.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        invoiceItem.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        invoiceItem.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        invoiceItem.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        invoiceItem.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        invoiceItem.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        invoiceItem.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        invoiceItem.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        invoiceItem.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        invoiceItem.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString("ts");
        invoiceItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        Integer dr = (Integer)rs.getObject("dr");

        String creceipttype = rs.getString("creceipttype");
        invoiceItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        invoiceItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        invoiceItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        invoiceItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid);

        String cpackunitid = rs.getString("cpackunitid");
        invoiceItem.setCpackunitid(cpackunitid == null ? null : cpackunitid);

        Object flargess = rs.getObject("blargessflag");
        invoiceItem.setBlargessflag(flargess == null ? new UFBoolean(false) : new UFBoolean(flargess.toString()));

        BigDecimal nqtnetprc = (BigDecimal)rs.getObject("nqtnetprc");
        invoiceItem.setNquotenetprice(nqtnetprc == null ? null : new UFDouble(nqtnetprc));

        BigDecimal nqtprc = (BigDecimal)rs.getObject("nqtprc");
        invoiceItem.setNquoteprice(nqtprc == null ? null : new UFDouble(nqtprc));

        BigDecimal nqttaxnetprc = (BigDecimal)rs.getObject("nqttaxnetprc");

        invoiceItem.setNquotetaxnetprice(nqttaxnetprc == null ? null : new UFDouble(nqttaxnetprc));

        BigDecimal nqttaxprc = (BigDecimal)rs.getObject("nqttaxprc");
        invoiceItem.setNquotetaxprice(nqttaxprc == null ? null : new UFDouble(nqttaxprc));

        BigDecimal nquoteunitnum = (BigDecimal)rs.getObject("nquoteunitnum");

        invoiceItem.setNquotenumber(nquoteunitnum == null ? null : new UFDouble(nquoteunitnum));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject("nquoteunitrate");

        invoiceItem.setNquoteunitrate(nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        String cquoteunitid = rs.getString("cquoteunitid");
        invoiceItem.setCquoteunitid(cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal norgqtnetprc = (BigDecimal)rs.getObject("norgqtnetprc");

        invoiceItem.setNquoteoriginalcurnetprice(norgqtnetprc == null ? null : new UFDouble(norgqtnetprc));

        BigDecimal norgqtprc = (BigDecimal)rs.getObject("norgqtprc");
        invoiceItem.setNquoteoriginalcurprice(norgqtprc == null ? null : new UFDouble(norgqtprc));

        BigDecimal norgqttaxnetprc = (BigDecimal)rs.getObject("norgqttaxnetprc");

        invoiceItem.setNquoteoriginalcurtaxnetprice(norgqttaxnetprc == null ? null : new UFDouble(norgqttaxnetprc));

        BigDecimal norgqttaxprc = (BigDecimal)rs.getObject("norgqttaxprc");

        invoiceItem.setNquoteoriginalcurtaxprice(norgqttaxprc == null ? null : new UFDouble(norgqttaxprc));

        String ccustomerid = rs.getString("ccustomerid");
        invoiceItem.setCcustomerid(ccustomerid);

        invoiceItem.setFrowstatus(new Integer(1));

        v.addElement(invoiceItem);
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
    invoiceItems = new SaleinvoiceBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(invoiceItems);
    }

    return queryOutBodyData(invoiceItems);
  }

  private UFBoolean getInvAttr(String strInvManID)
    throws SQLException
  {
    String sql = "select sellproxyflag from bd_invmandoc where pk_invmandoc = ? ";

    UFBoolean value = new UFBoolean("N");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, strInvManID);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String temp = rs.getString(1);
        if (temp != null)
          value = new UFBoolean(temp);
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
    return value;
  }

  private Hashtable getInvAttrC(String csaleid)
    throws SQLException
  {
    String sql = "select pk_invmandoc,sellproxyflag from bd_invmandoc where pk_invmandoc in (select cinventoryid from so_saleinvoice_b where csaleid=?) ";

    UFBoolean value = new UFBoolean("N");
    Hashtable htResult = new Hashtable();

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, csaleid);
      ResultSet rs = stmt.executeQuery();
      String inv = null;
      String temp = null;
      while (rs.next())
      {
        inv = rs.getString(1);
        inv = inv == null ? null : inv.trim();

        temp = rs.getString(2);
        if (temp != null) {
          value = new UFBoolean(temp);
        }
        if ((inv != null) && (temp != null))
          htResult.put(inv, value);
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
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (htResult != null) {
      return htResult;
    }
    return null;
  }

  public String[][] getInventoryInfo(String strID)
    throws SQLException
  {
    String strSql = "select zb.invcode, zb.invname,zb.invspec,zb.invtype,fb.measname from bd_invbasdoc zb left outer join bd_measdoc fb on fb.pk_measdoc=zb.pk_measdoc where zb.pk_invbasdoc = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    Vector v = new Vector();
    try {
      stmt.setString(1, strID);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Vector vLine = new Vector();
        vLine.addElement(rs.getString("invcode"));
        vLine.addElement(rs.getString("invname"));
        vLine.addElement(rs.getString("invspec"));
        vLine.addElement(rs.getString("invtype"));
        vLine.addElement(rs.getString("measname"));
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
    String[][] results = new String[v.size()][((Vector)v.elementAt(0)).size()];

    if (v.size() > 0) {
      for (int i = 0; i < v.size(); i++) {
        for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
          results[i][j] = ((Vector)v.elementAt(i)).elementAt(j).toString();
        }
      }
    }
    return results;
  }

  private UFDouble getInvInvoiceNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select sum(so_saleinvoice_b.nnumber),so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid from so_saleinvoice  inner join so_saleinvoice_b on so_saleinvoice.csaleid = so_saleinvoice_b.csaleid where so_saleinvoice.dbilldate<='" + endDate.toString() + "' and so_saleinvoice.dbilldate>='" + startDate.toString() + "'  and so_saleinvoice_b.cinvbasdocid ='" + cinvbasdocid + "' and so_saleinvoice_b.cinventoryid='" + cinvmandocid + "' and so_saleinvoice.fstatus=2 group by so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invinvoicenumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble invoiceNum = (UFDouble)rs.getObject(1);
        if (invoiceNum != null)
          invinvoicenumber = invoiceNum;
        else
          invinvoicenumber = new UFDouble(0.0D);
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
    return invinvoicenumber;
  }

  public UFDouble getInvInvoiceNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select isnull(sum(so_saleinvoice_b.nnumber),0),so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid from so_saleinvoice inner join so_saleinvoice_b on so_saleinvoice.csaleid = so_saleinvoice_b.csaleid where so_saleinvoice.dbilldate<='" + endDate.toString() + "' and so_saleinvoice.dbilldate>='" + startDate.toString() + "' and so_saleinvoice_b.cinventoryid='" + cinvmandocid + "' and so_saleinvoice.fstatus=2 and so_saleinvoice.pk_corp='" + pk_corp + "' group by so_saleinvoice_b.cinvbasdocid,so_saleinvoice_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invinvoicenumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble invoiceNum = new UFDouble(rs.getBigDecimal(1));
        if (invoiceNum != null)
          invinvoicenumber = invoiceNum;
        else
          invinvoicenumber = new UFDouble(0.0D);
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
    return invinvoicenumber;
  }

  public String getInvoiceCode(String id, String pk_corp)
    throws SQLException
  {
    String sql = "SELECT vreceiptcode FROM so_saleinvoice WHERE csaleid = '" + id + "' AND pk_corp = '" + pk_corp + "'";

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
    return o == null ? "" : o.toString();
  }

  public double getInvoiceNumber(String saleid, String sale_bid)
    throws SQLException
  {
    String strSql = "SELECT ntotalinvoicenumber FROM so_saleexecute where csaleid= '" + saleid + "' and csale_bid='" + sale_bid + "'";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    double invoicenumber = 0.0D;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble invoiceNum = (UFDouble)rs.getObject(1);
        if (invoiceNum != null)
          invoicenumber = invoiceNum.doubleValue();
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
    return invoicenumber;
  }

  public String[][] getInvoiceType()
    throws SQLException
  {
    String strSql = "SELECT finvoicetype, vinvoicetypename, resid FROM so_invoicetype";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    String[][] results = (String[][])null;
    Vector v = new Vector();
    try {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Vector vLine = new Vector();
        vLine.addElement(String.valueOf(rs.getInt("finvoicetype")).toString());

        vLine.addElement(rs.getString("vinvoicetypename"));
        vLine.addElement(rs.getString("resid"));
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

  private UFDouble getInvOrderNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select sum(so_saleorder_b.nnumber),so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid from so_sale inner join so_saleorder_b on so_sale.csaleid = so_saleorder_b.csaleid where so_sale.dbilldate<='" + endDate.toString() + "' and so_sale.dbilldate>='" + startDate.toString() + "'  and so_saleorder_b.cinvbasdocid ='" + cinvbasdocid + "' and so_saleorder_b.cinventoryid='" + cinvmandocid + "' and so_sale.fstatus=2 group by so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invordernumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble orderNum = (UFDouble)rs.getObject(1);
        if (orderNum != null)
          invordernumber = orderNum;
        else
          invordernumber = new UFDouble(0.0D);
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
    return invordernumber;
  }

  public UFDouble getInvOrderNumber(String cinvbasdocid, String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp)
    throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;
    String strSql = "select isnull(sum(so_saleorder_b.nnumber),0),so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid from so_sale inner join so_saleorder_b on so_sale.csaleid = so_saleorder_b.csaleid where so_sale.dbilldate<='" + endDate.toString() + "' and so_sale.dbilldate>='" + startDate.toString() + "' and so_saleorder_b.cinventoryid='" + cinvmandocid + "' and so_sale.fstatus=2 and so_sale.pk_corp ='" + pk_corp + "' group by so_saleorder_b.cinvbasdocid,so_saleorder_b.cinventoryid";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invordernumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble orderNum = new UFDouble(rs.getBigDecimal(1));
        if (orderNum != null)
          invordernumber = orderNum;
        else
          invordernumber = new UFDouble(0.0D);
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
    return invordernumber;
  }

  private UFDouble getInvOutNumber(String cinvmandocid, UFDate queryDate, Integer queryDay) throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;

    String strSql = "select sum(noutnum),b.cinventoryid from ic_general_b b ,ic_general_h  h where h.cgeneralhid=b.cgeneralhid and h.cbilltypecode='4C'and b.dr=0 and h.dr=0 and b.dbizdate>='" + startDate.toString() + "' and b.dbizdate<='" + endDate.toString() + "' and b.cinventoryid='" + cinvmandocid + "' and h.fbillflag=3 group by b.cinventoryid ";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invoutnumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble outNum = (UFDouble)rs.getObject(1);
        if (outNum != null)
          invoutnumber = outNum;
        else
          invoutnumber = new UFDouble(0.0D);
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
    return invoutnumber;
  }

  public UFDouble getInvOutNumber(String cinvmandocid, UFDate queryDate, Integer queryDay, String pk_corp) throws SQLException
  {
    UFDate startDate = queryDate.getDateBefore(queryDay.intValue());
    UFDate endDate = queryDate;

    String strSql = "select isnull(sum(noutnum),0),b.cinventoryid from ic_general_b b ,ic_general_h  h where h.cgeneralhid=b.cgeneralhid and h.cbilltypecode='4C'and b.dr=0 and h.dr=0 and b.dbizdate>='" + startDate.toString() + "' and b.dbizdate<='" + endDate.toString() + "' and b.cinventoryid='" + cinvmandocid + "' and h.fbillflag=3 and h.pk_corp = '" + pk_corp + "' group by b.cinventoryid ";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    UFDouble invoutnumber = null;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        UFDouble outNum = new UFDouble(rs.getBigDecimal(1));
        if (outNum != null)
          invoutnumber = outNum;
        else
          invoutnumber = new UFDouble(0.0D);
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
    return invoutnumber;
  }

  public String getOrderID(String id) throws SQLException
  {
    String sql = "select distinct csaleid from so_saleorder_b where corder_bid = '" + id + "'";

    String saleid = null;
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    try {
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
        saleid = rs.getString(1);
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
    return saleid;
  }

  public String[] getOrderNumber(String orderID, String orderBID)
    throws SQLException
  {
    String strSql = "SELECT nnumber,npacknumber FROM so_saleorder_b where csaleid='" + orderID + "' and corder_bid='" + orderBID + "'";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    String[] result = new String[2];
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        result[0] = (nnumber == null ? "0.0" : nnumber.toString());

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        result[1] = (npacknumber == null ? "0.0" : npacknumber.toString());
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

  public double getOutNumber(String saleid, String sale_bid, String SO_06)
    throws SQLException
  {
    String strSql = null;
    if (SO_06.equals("实发量")) {
      strSql = "SELECT ic_general_b.noutnum,isnull(ic_general_bb3.nsignnum,0),isnull(ic_general_b.naccumwastnum FROM ic_general_b INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid WHERE ic_general_b.cgeneralhid='" + saleid + "' and ic_general_b.cgeneralbid='" + sale_bid + "' and ic_general_bb3.dr = 0";
    }
    else if (SO_06.equals("应发量")) {
      strSql = "SELECT ic_general_b.nshouldoutnum,isnull(ic_general_bb3.nsignnum,0),isnull(ic_general_b.naccumwastnum FROM ic_general_b INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid WHERE ic_general_b.cgeneralhid='" + saleid + "' and ic_general_b.cgeneralbid='" + sale_bid + "'  and ic_general_bb3.dr = 0";
    }
    else if (SO_06.equals("结算量")) {
      strSql = "SELECT naccountnum1,isnull(ic_general_bb3.nsignnum,0),isnull(ic_general_b.naccumwastnum FROM FROM ic_general_b INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid WHERE ic_general_b.cgeneralhid='" + saleid + "' and ic_general_b.cgeneralbid='" + sale_bid + "'  and ic_general_bb3.dr = 0";
    }

    if (strSql == null) {
      return 0.0D;
    }
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    double invoicenumber = 0.0D;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Object invoiceNum1 = rs.getObject(1);
        Object invoiceNum2 = rs.getObject(2);
        Object invoiceNum3 = rs.getObject(3);
        double dinvoicenum1 = 0.0D;
        double dinvoicenum2 = 0.0D;
        double dinvoicenum3 = 0.0D;
        if (invoiceNum1 != null) {
          dinvoicenum1 = Double.valueOf(invoiceNum1.toString()).doubleValue();
        }
        if (invoiceNum2 != null) {
          dinvoicenum2 = Double.valueOf(invoiceNum2.toString()).doubleValue();
        }
        if (invoiceNum3 != null) {
          dinvoicenum3 = Double.valueOf(invoiceNum3.toString()).doubleValue();
        }
        invoicenumber = dinvoicenum1 - dinvoicenum2 - dinvoicenum2;
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
    return invoicenumber;
  }

  public Hashtable getOutNumber(Hashtable hsBid, String SO_06)
    throws SQLException
  {
    String strSql = null;

    Hashtable hsOutNumByBid = new Hashtable();
    if (SO_06.equals("实发量")) {
      strSql = "SELECT ic_general_b.noutnum-isnull(ic_general_bb3.nsignnum,0)-isnull(ic_general_b.naccumwastnum,0),ic_general_b.cgeneralbid  FROM ic_general_b  INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid  WHERE ic_general_bb3.dr = 0 " + GeneralSqlString.formInSQL("ic_general_b.cgeneralbid", (String[])(String[])hsBid.keySet().toArray(new String[0]));
    }
    else if (SO_06.equals("应发量")) {
      strSql = "SELECT ic_general_b.nshouldoutnum-isnull(ic_general_bb3.nsignnum,0)-isnull(ic_general_b.naccumwastnum,0) ,ic_general_b.cgeneralbid  FROM ic_general_b INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid WHERE ic_general_bb3.dr = 0 " + GeneralSqlString.formInSQL("ic_general_b.cgeneralbid", (String[])(String[])hsBid.keySet().toArray(new String[0]));
    }
    else if (SO_06.equals("结算量")) {
      strSql = "SELECT naccountnum1-isnull(ic_general_bb3.nsignnum,0)-isnull(ic_general_b.naccumwastnum,0) , cgeneralbid  FROM ic_general_b INNER JOIN ic_general_bb3 ON ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid  WHERE ic_general_bb3.dr = 0 " + GeneralSqlString.formInSQL("ic_general_b3.cgeneralbid", (String[])(String[])hsBid.keySet().toArray(new String[0]));
    }

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    double invoicenumber = 0.0D;
    try {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Object otemp = rs.getObject(1);
        UFDouble invoiceNum = otemp == null ? new UFDouble(0) : new UFDouble(otemp.toString());

        String keybid = rs.getString(2).toString();
        hsOutNumByBid.put(keybid, invoiceNum);
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
    return hsOutNumByBid;
  }

  public String[] getReceiptInfo(String strID)
    throws SQLException
  {
    String strSql = "SELECT cupreceipttype, cupsourcebillid,cupsourcebillbodyid FROM so_salereceipt_b WHERE creceipt_bid='" + strID + "'";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    Vector v = new Vector();
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String obj = rs.getString(1);
        v.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(2);
        v.addElement(obj == null ? "" : obj.trim());
        obj = rs.getString(3);
        v.addElement(obj == null ? "" : obj.trim());
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
    String[] results = null;
    if (v.size() > 0) {
      results = new String[v.size()];
      for (int i = 0; i < v.size(); i++) {
        results[i] = v.elementAt(i).toString();
      }
    }
    return results;
  }

  public int getStatus(String key)
    throws SQLException
  {
    String strSql = "SELECT fstatus FROM so_saleinvoice where csaleid='" + key + "'";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    int status = 0;
    try {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Integer fstatus = (Integer)rs.getObject(1);
        if (fstatus != null)
          status = fstatus.intValue();
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
    return status;
  }

  public String getVerifyrule(String sBizType)
    throws SQLException
  {
    String sql = "SELECT verifyrule FROM bd_busitype WHERE pk_busitype='" + sBizType + "' ";

    Connection con = null;
    PreparedStatement stmt = null;
    boolean result = false;
    String verifyrule = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        verifyrule = rs.getString(1);
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
    return verifyrule;
  }

  public String[][] getWarehouseInfo(String strID)
    throws SQLException
  {
    String strSql = "select storname from bd_stordoc where pk_stordoc = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    Vector v = new Vector();
    try {
      stmt.setString(1, strID);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        Vector vLine = new Vector();
        vLine.addElement(rs.getString("storname"));
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
    String[][] results = new String[v.size()][((Vector)v.elementAt(0)).size()];

    if (v.size() > 0) {
      for (int i = 0; i < v.size(); i++) {
        for (int j = 0; j < ((Vector)v.elementAt(i)).size(); j++) {
          results[i][j] = ((Vector)v.elementAt(i)).elementAt(j).toString();
        }
      }
    }
    return results;
  }

  public void giveup(String key)
    throws SQLException
  {
    String sql = "update so_saleinvoice set fstatus=5 where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
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

  private void initFreeItem(CircularlyAccessibleValueObject[] vos)
  {
    if (vos == null) {
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
    }
  }

  private PreparedStatement getBodyStatement(Connection con)
    throws SQLException
  {
    if (con == null)
    {
      con = getConnection();
    }
    String sql = "insert into so_saleinvoice_b( cinvoice_bid, csaleid, pk_corp, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, nbalancenumber, cbodywarehouseid, cupreceipttype, cupsourcebillid, cupsourcebillbodyid, creceipttype, csourcebillid, csourcebillbodyid, blargessflag, cbatchid, ccurrencytypeid, nexchangeotobrate, nexchangeotoarate, nitemdiscountrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, nsimulatecostmny, ncostmny, ddeliverdate, frowstatus, frownote,cinvbasdocid, ndiscountrate,fbatchstatus,ct_manageid,cfreezeid, creceiptcorpid,crowno,ccalbodyid,coriginalbillcode,cupsourcebillcode,cquoteunitid ,nquotenumber ,nquoricurpri ,nquoricurtaxpri ,nquoricurnetpri ,nqocurtaxnetpri ,nquprice ,nqutaxprice ,nqunetprice,nqutaxnetprice ,nsubqupri ,nsubqutaxpri ,nsubqunetpri ,nsubqutaxnetpri ,nsubsummny ,nsubtaxnetprice ,cprolineid ,nsubcursummny, nquoteunitrate,ccustomerid,httsbl,httssl,sjtsje,sjtssl,ztsje,b_cjje1,b_cjje2,b_cjje3 ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

    return prepareStatement(con, sql);
  }

  private PreparedStatement getFolowStatement(Connection con)
    throws SQLException
  {
    if (con == null)
    {
      con = getConnection();
    }
    String followsql = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1,  vdef6, vdef5, vdef4, vdef3, vdef2, vdef1, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20,  pk_defdoc1 , pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10,  pk_defdoc11 , pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20,  narsubinvmny)  values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    return prepareStatement(con, followsql);
  }

  public ArrayList insert(SaleinvoiceVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insert", new Object[] { vo });

    if (vo != null) {
      vo.validate();
    }
    String vreceiptcode = null;
    try {
      CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
      vreceiptcode = dmocode.getSysBillNO(vo);
      vo.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);
      onCheck(vo);
    } catch (Exception e1) {
      vo.getParentVO().setAttributeValue("vreceiptcode", null);
      throw new BusinessException(e1.getMessage());
    }

    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmtfollow = null;
    ArrayList listBillID = null;
    try {
      String pk_corp = ((SaleVO)vo.getParentVO()).getPk_corp();

      con = getConnection();

      String key = insertHeader((SaleVO)vo.getParentVO(), con);

      vo.setPrimaryKey(key);

      listBillID = new ArrayList();
      listBillID.add(key);
      listBillID.add(vreceiptcode);

      SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vo.getChildrenVO();

      stmt = getBodyStatement(con);

      stmtfollow = getFolowStatement(con);
      for (int i = 0; i < items.length; i++) {
        items[i].setPk_corp(pk_corp);

        listBillID.add(insertItem(items[i], key, stmt, stmtfollow));
      }
      executeBatch(stmt);
      executeBatch(stmtfollow);

      if ((((SaleVO)vo.getParentVO()).getFcounteractflag() != null) && (((SaleVO)vo.getParentVO()).getFcounteractflag().intValue() == 2))
      {
        String csaleid = items[0].getCupsourcebillid();
        SmartDMO sdmo = new SmartDMO();

        String selst = "select fcounteractflag from so_saleinvoice where csaleid='" + csaleid + "'";
        Object[] objs = sdmo.selectBy2(selst);
        if ((objs != null) && (objs.length > 0)) {
          Object[] temps = (Object[])(Object[])objs[0];
          if ((temps != null) && (temps.length > 0)) {
            Integer oldflag = new Integer(0);
            if (temps[0] != null)
              oldflag = new Integer(temps[0].toString());
            if (oldflag.intValue() != 0) {
              throw new BusinessException("对冲发票出错！原发票已经被对冲，不能再次对冲！");
            }
          }

        }

        String sqlthis = "update so_saleinvoice set fcounteractflag=1 where csaleid='" + csaleid + "'";
        sdmo.executeUpdate(sqlthis, new ArrayList(), new ArrayList());
      }
    }
    finally
    {
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insert", new Object[] { vo });

    return listBillID;
  }

  public void insertBodys(SaleinvoiceBVO[] saleorderB, String strMainID, String pk_corp)
    throws SQLException, SystemException
  {
    for (int i = 0; i < saleorderB.length; i++) {
      saleorderB[i].setPk_corp(pk_corp);
      insertItem(saleorderB[i], strMainID);
    }
  }

  public void insertFollowBody(SaleinvoiceBVO saleexecute)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });

    String sql = " insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1,  vdef6, vdef5, vdef4, vdef3, vdef2, vdef1, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20,  pk_defdoc1 , pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10,  pk_defdoc11 , pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20,  narsubinvmny)  values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
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
      stmt.setString(3, "32");
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

      UFDouble narsubinvmny = (saleexecute.getNsubsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsubsummny()).div(saleexecute.getNsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsummny());

      if (narsubinvmny == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, narsubinvmny.toBigDecimal());
      }

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

    afterCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });
  }

  public void insertFollowBody(SaleinvoiceBVO saleexecute, PreparedStatement stmt)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });
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
      stmt.setString(3, "32");
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

      if (saleexecute.getVdef7() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, saleexecute.getVdef7());
      }
      if (saleexecute.getVdef8() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, saleexecute.getVdef8());
      }
      if (saleexecute.getVdef9() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, saleexecute.getVdef9());
      }
      if (saleexecute.getVdef10() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, saleexecute.getVdef10());
      }
      if (saleexecute.getVdef11() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, saleexecute.getVdef11());
      }
      if (saleexecute.getVdef12() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, saleexecute.getVdef12());
      }
      if (saleexecute.getVdef13() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleexecute.getVdef13());
      }
      if (saleexecute.getVdef14() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleexecute.getVdef14());
      }
      if (saleexecute.getVdef15() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleexecute.getVdef15());
      }
      if (saleexecute.getVdef16() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleexecute.getVdef16());
      }
      if (saleexecute.getVdef17() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleexecute.getVdef17());
      }
      if (saleexecute.getVdef18() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleexecute.getVdef18());
      }
      if (saleexecute.getVdef19() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, saleexecute.getVdef19());
      }
      if (saleexecute.getVdef20() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleexecute.getVdef20());
      }
      if (saleexecute.getPk_defdoc1() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleexecute.getPk_defdoc1());
      }
      if (saleexecute.getPk_defdoc2() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleexecute.getPk_defdoc2());
      }
      if (saleexecute.getPk_defdoc3() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleexecute.getPk_defdoc3());
      }
      if (saleexecute.getPk_defdoc4() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleexecute.getPk_defdoc4());
      }
      if (saleexecute.getPk_defdoc5() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleexecute.getPk_defdoc5());
      }
      if (saleexecute.getPk_defdoc6() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleexecute.getPk_defdoc6());
      }
      if (saleexecute.getPk_defdoc7() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleexecute.getPk_defdoc7());
      }
      if (saleexecute.getPk_defdoc8() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleexecute.getPk_defdoc8());
      }
      if (saleexecute.getPk_defdoc9() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleexecute.getPk_defdoc9());
      }
      if (saleexecute.getPk_defdoc10() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, saleexecute.getPk_defdoc10());
      }
      if (saleexecute.getPk_defdoc11() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, saleexecute.getPk_defdoc11());
      }
      if (saleexecute.getPk_defdoc12() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, saleexecute.getPk_defdoc12());
      }
      if (saleexecute.getPk_defdoc13() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, saleexecute.getPk_defdoc13());
      }
      if (saleexecute.getPk_defdoc14() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, saleexecute.getPk_defdoc14());
      }
      if (saleexecute.getPk_defdoc15() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, saleexecute.getPk_defdoc15());
      }
      if (saleexecute.getPk_defdoc16() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, saleexecute.getPk_defdoc16());
      }
      if (saleexecute.getPk_defdoc17() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, saleexecute.getPk_defdoc17());
      }
      if (saleexecute.getPk_defdoc18() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, saleexecute.getPk_defdoc18());
      }
      if (saleexecute.getPk_defdoc19() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, saleexecute.getPk_defdoc19());
      }
      if (saleexecute.getPk_defdoc20() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, saleexecute.getPk_defdoc20());
      }

      UFDouble narsubinvmny = (saleexecute.getNsubsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsubsummny()).div(saleexecute.getNsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsummny());

      if (narsubinvmny == null)
        stmt.setNull(68, 4);
      else {
        stmt.setBigDecimal(68, narsubinvmny.toBigDecimal());
      }

      executeUpdate(stmt);
    }
    finally
    {
    }

    afterCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });
  }

  public String insertHeader(SaleVO saleHeader)
    throws SQLException, SystemException
  {
    return insertHeader(saleHeader, null);
  }

  public String insertHeader(SaleVO saleHeader, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertHeader", new Object[] { saleHeader });

    String sql = "insert into so_saleinvoice(csaleid, pk_corp, vreceiptcode, creceipttype, cbiztype, finvoiceclass, finvoicetype, vaccountyear, binitflag, dbilldate,  cdeptid, cemployeeid, coperatorid, ctermprotocolid, csalecorpid, creceiptcustomerid, vreceiveaddress, creceiptcorpid, ctransmodeid, ndiscountrate, cwarehouseid, veditreason, bfreecustflag, cfreecustid, ibalanceflag, nsubscription, ccreditnum, nevaluatecarriage, dmakedate, capproveid, dapprovedate, fstatus, vnote, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10,ccustbankid,ccalbodyid,cdispatcherid,ntotalsummny,nstrikemny,nnetmny , vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20,fcounteractflag ,vprintcustname,dbilltime ) values(?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?  )";

    String key = null;
    String pk_corp = saleHeader.getPk_corp();
    PreparedStatement stmt = null;
    Connection connew = null;
    try
    {
      if (con == null)
        connew = getConnection();
      else
        connew = con;
      stmt = connew.prepareStatement(sql);

      key = getOID(pk_corp);
      int iseq = 1;
      stmt.setString(iseq++, key);

      if (saleHeader.getPk_corp() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_corp());
      }
      if (saleHeader.getVreceiptcode() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVreceiptcode());
      }
      if (saleHeader.getCreceipttype() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCreceipttype());
      }
      if (saleHeader.getCbiztype() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCbiztype());
      }
      if (saleHeader.getFinvoiceclass() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setInt(iseq++, saleHeader.getFinvoiceclass().intValue());
      }
      if (saleHeader.getFinvoicetype() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setInt(iseq++, saleHeader.getFinvoicetype().intValue());
      }
      if (saleHeader.getVaccountyear() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVaccountyear());
      }
      if (saleHeader.getBinitflag() == null)
        stmt.setString(iseq++, "N");
      else {
        stmt.setString(iseq++, saleHeader.getBinitflag().toString());
      }
      if (saleHeader.getDbilldate() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getDbilldate().toString());
      }

      if (saleHeader.getCdeptid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCdeptid());
      }
      if (saleHeader.getCemployeeid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCemployeeid());
      }
      if (saleHeader.getCoperatorid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCoperatorid());
      }
      if (saleHeader.getCtermprotocolid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCtermprotocolid());
      }
      if (saleHeader.getCsalecorpid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCsalecorpid());
      }
      if (saleHeader.getCreceiptcustomerid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCreceiptcustomerid());
      }
      if (saleHeader.getVreceiveaddress() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVreceiveaddress());
      }
      if (saleHeader.getCreceiptcorpid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCreceiptcorpid());
      }
      if (saleHeader.getCtransmodeid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCtransmodeid());
      }
      if (saleHeader.getNdiscountrate() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNdiscountrate().toBigDecimal());
      }

      if (saleHeader.getCwarehouseid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCwarehouseid());
      }
      if (saleHeader.getVeditreason() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVeditreason());
      }
      if (saleHeader.getBfreecustflag() == null)
        stmt.setString(iseq++, "N");
      else {
        stmt.setString(iseq++, saleHeader.getBfreecustflag().toString());
      }
      if (saleHeader.getCfreecustid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCfreecustid());
      }
      if (saleHeader.getIbalanceflag() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setInt(iseq++, saleHeader.getIbalanceflag().intValue());
      }
      if (saleHeader.getNsubscription() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNsubscription().toBigDecimal());
      }

      if (saleHeader.getCcreditnum() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCcreditnum());
      }
      if (saleHeader.getNevaluatecarriage() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNevaluatecarriage().toBigDecimal());
      }

      if (saleHeader.getDmakedate() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getDmakedate().toString());
      }
      if (saleHeader.getCapproveid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCapproveid());
      }
      if (saleHeader.getDapprovedate() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getDapprovedate().toString());
      }
      if (saleHeader.getFstatus() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setInt(iseq++, saleHeader.getFstatus().intValue());
      }
      if (saleHeader.getVnote() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVnote());
      }
      if (saleHeader.getVdef1() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef1());
      }
      if (saleHeader.getVdef2() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef2());
      }
      if (saleHeader.getVdef3() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef3());
      }
      if (saleHeader.getVdef4() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef4());
      }
      if (saleHeader.getVdef5() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef5());
      }
      if (saleHeader.getVdef6() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef6());
      }
      if (saleHeader.getVdef7() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef7());
      }
      if (saleHeader.getVdef8() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef8());
      }
      if (saleHeader.getVdef9() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef9());
      }
      if (saleHeader.getVdef10() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef10());
      }
      if (saleHeader.getCcustbankid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCcustbankid());
      }
      if (saleHeader.getCcalbodyid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getCcalbodyid());
      }

      if (saleHeader.getcdispatcherid() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getcdispatcherid());
      }

      if (saleHeader.getNtotalsummny() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNtotalsummny().toBigDecimal());
      }

      if (saleHeader.getNstrikemny() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNstrikemny().toBigDecimal());
      }

      if (saleHeader.getNnetmny() == null)
        stmt.setNull(iseq++, 4);
      else {
        stmt.setBigDecimal(iseq++, saleHeader.getNnetmny().toBigDecimal());
      }

      if (saleHeader.getVdef11() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef11());
      }
      if (saleHeader.getVdef12() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef12());
      }
      if (saleHeader.getVdef13() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef13());
      }
      if (saleHeader.getVdef14() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef14());
      }
      if (saleHeader.getVdef15() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef15());
      }
      if (saleHeader.getVdef16() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef16());
      }
      if (saleHeader.getVdef17() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef17());
      }
      if (saleHeader.getVdef18() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef18());
      }
      if (saleHeader.getVdef19() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef19());
      }
      if (saleHeader.getVdef20() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVdef20());
      }
      if (saleHeader.getPk_defdoc1() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc1());
      }
      if (saleHeader.getPk_defdoc2() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc2());
      }
      if (saleHeader.getPk_defdoc3() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc3());
      }
      if (saleHeader.getPk_defdoc4() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc4());
      }
      if (saleHeader.getPk_defdoc5() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc5());
      }
      if (saleHeader.getPk_defdoc6() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc6());
      }
      if (saleHeader.getPk_defdoc7() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc7());
      }
      if (saleHeader.getPk_defdoc8() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc8());
      }
      if (saleHeader.getPk_defdoc9() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc9());
      }
      if (saleHeader.getPk_defdoc10() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc10());
      }
      if (saleHeader.getPk_defdoc11() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc11());
      }
      if (saleHeader.getPk_defdoc12() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc12());
      }
      if (saleHeader.getPk_defdoc13() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc13());
      }
      if (saleHeader.getPk_defdoc14() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc14());
      }
      if (saleHeader.getPk_defdoc15() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc15());
      }
      if (saleHeader.getPk_defdoc16() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc16());
      }
      if (saleHeader.getPk_defdoc17() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc17());
      }
      if (saleHeader.getPk_defdoc18() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc18());
      }
      if (saleHeader.getPk_defdoc19() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc19());
      }
      if (saleHeader.getPk_defdoc20() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getPk_defdoc20());
      }

      if (saleHeader.getFcounteractflag() == null)
        stmt.setInt(iseq++, 0);
      else {
        stmt.setInt(iseq++, saleHeader.getFcounteractflag().intValue());
      }
      if (saleHeader.getVprintcustname() == null)
        stmt.setNull(iseq++, 1);
      else {
        stmt.setString(iseq++, saleHeader.getVprintcustname());
      }
      stmt.setString(iseq, new UFDateTime(System.currentTimeMillis()).toString());

      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if ((con == null) && (connew != null)) {
          connew.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertHeader", new Object[] { saleHeader });

    return key;
  }

  public ArrayList insertInit(SaleinvoiceVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insert", new Object[] { vo });

    CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
    String vreceiptcode = dmocode.getSysBillNO(vo);
    vo.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);

    onCheck(vo);
    String pk_corp = ((SaleVO)vo.getParentVO()).getPk_corp();

    String key = insertHeader((SaleVO)vo.getParentVO());

    ArrayList listBillID = new ArrayList();
    listBillID.add(key);
    listBillID.add(vreceiptcode);

    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      items[i].setPk_corp(pk_corp);

      listBillID.add(insertInitItem(items[i], key));
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insert", new Object[] { vo });

    return listBillID;
  }

  public void insertInitFollowBody(SaleinvoiceBVO saleexecute)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });

    String sql = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1, vdef6, vdef5, vdef4, vdef3, vdef2, vdef1,narsubinvmny) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
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
      stmt.setString(3, "3C");
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

      UFDouble narsubinvmny = (saleexecute.getNsubsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsubsummny()).div(saleexecute.getNsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsummny());

      if (narsubinvmny == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, narsubinvmny.toBigDecimal());
      }

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

    afterCallMethod("nc.bs.pub.bill.SaleInvoiceDMO", "insertFollowBody", new Object[] { saleexecute });
  }

  public String insertInitItem(SaleinvoiceBVO saleItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    String sql = "insert into so_saleinvoice_b(cinvoice_bid, csaleid, pk_corp, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, nbalancenumber, cbodywarehouseid, cupreceipttype, cupsourcebillid, cupsourcebillbodyid, creceipttype, csourcebillid, csourcebillbodyid, blargessflag, cbatchid, ccurrencytypeid, nexchangeotobrate, nexchangeotoarate, nitemdiscountrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, nsimulatecostmny, ncostmny, ddeliverdate, frowstatus, frownote,cinvbasdocid,ndiscountrate,fbatchstatus,ct_manageid,crowno,ccalbodyid,coriginalbillcode,cupsourcebillcode,cquoteunitid,nquotenumber,nquoricurpri,nquoricurtaxpri,nquoricurnetpri,nqocurtaxnetpri,nquprice,nqutaxprice,nqunetprice,nqutaxnetprice,nsubqupri,nsubqutaxpri,nsubqunetpri,nsubqutaxnetpri,nsubsummny,nsubtaxnetprice,cprolineid,nsubcursummny) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String key = null;
    String pk_corp = saleItem.getPk_corp();
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try
    {
      key = getOID(pk_corp);
      stmt.setString(1, key);

      if (saleItem.getCsaleid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getCsaleid());
      }
      if (saleItem.getPk_corp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getPk_corp());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, saleItem.getNpacknumber().toBigDecimal());
      }
      if (saleItem.getNbalancenumber() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, saleItem.getNbalancenumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getCupreceipttype() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCupreceipttype());
      }
      if (saleItem.getCupsourcebillid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getCupsourcebillid());
      }
      if (saleItem.getCupsourcebillbodyid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getCupsourcebillbodyid());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getBlargessflag() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleItem.getBlargessflag().toString());
      }
      if (saleItem.getCbatchid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCbatchid());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNitemdiscountrate().toBigDecimal());
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

      if (saleItem.getNsimulatecostmny() == null)
        stmt.setNull(40, 4);
      else {
        stmt.setBigDecimal(40, saleItem.getNsimulatecostmny().toBigDecimal());
      }

      if (saleItem.getNcostmny() == null)
        stmt.setNull(41, 4);
      else {
        stmt.setBigDecimal(41, saleItem.getNcostmny().toBigDecimal());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleItem.getDdeliverdate().toString());
      }
      if (saleItem.getFrowstatus() == null)
        stmt.setNull(43, 4);
      else {
        stmt.setInt(43, saleItem.getFrowstatus().intValue());
      }
      if (saleItem.getFrownote() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleItem.getFrownote());
      }
      if (saleItem.getCinvbasdocid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleItem.getCinvbasdocid());
      }
      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(46, 4);
      else {
        stmt.setBigDecimal(46, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(47, 4);
      else {
        stmt.setInt(47, saleItem.getFbatchstatus().intValue());
      }
      if (saleItem.getCt_manageid() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getCt_manageid());
      }
      if (saleItem.getrowno() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getrowno());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getCoriginalbillcode() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleItem.getCoriginalbillcode());
      }

      if (saleItem.getCupsourcebillcode() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCupsourcebillcode());
      }

      if (saleItem.getCquoteunitid() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getCquoteunitid());
      }
      if (saleItem.getNquotenumber() == null)
        stmt.setNull(54, 4);
      else {
        stmt.setBigDecimal(54, saleItem.getNquotenumber().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurprice() == null)
        stmt.setNull(55, 4);
      else {
        stmt.setBigDecimal(55, saleItem.getNquoteoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxprice() == null)
        stmt.setNull(56, 4);
      else {
        stmt.setBigDecimal(56, saleItem.getNquoteoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurnetprice() == null)
        stmt.setNull(57, 4);
      else {
        stmt.setBigDecimal(57, saleItem.getNquoteoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxnetprice() == null)
        stmt.setNull(58, 4);
      else {
        stmt.setBigDecimal(58, saleItem.getNquoteoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteprice() == null)
        stmt.setNull(59, 4);
      else {
        stmt.setBigDecimal(59, saleItem.getNquoteprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxprice() == null)
        stmt.setNull(60, 4);
      else {
        stmt.setBigDecimal(60, saleItem.getNquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNquotenetprice() == null)
        stmt.setNull(61, 4);
      else {
        stmt.setBigDecimal(61, saleItem.getNquotenetprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxnetprice() == null)
        stmt.setNull(62, 4);
      else {
        stmt.setBigDecimal(62, saleItem.getNquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubquoteprice() == null)
        stmt.setNull(63, 4);
      else {
        stmt.setBigDecimal(63, saleItem.getNsubquoteprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxprice() == null)
        stmt.setNull(64, 4);
      else {
        stmt.setBigDecimal(64, saleItem.getNsubquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNsubquotenetprice() == null)
        stmt.setNull(65, 4);
      else {
        stmt.setBigDecimal(65, saleItem.getNsubquotenetprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxnetprice() == null)
        stmt.setNull(66, 4);
      else {
        stmt.setBigDecimal(66, saleItem.getNsubquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubsummny() == null)
        stmt.setNull(67, 4);
      else {
        stmt.setBigDecimal(67, saleItem.getNsubsummny().toBigDecimal());
      }
      if (saleItem.getNsubtaxnetprice() == null)
        stmt.setNull(68, 4);
      else {
        stmt.setBigDecimal(68, saleItem.getNsubtaxnetprice().toBigDecimal());
      }

      if (saleItem.getCprolineid() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, saleItem.getCprolineid());
      }

      if (saleItem.getNsubcursummny() == null)
        stmt.setNull(70, 4);
      else {
        stmt.setBigDecimal(70, saleItem.getNsubcursummny().toBigDecimal());
      }

      stmt.executeUpdate();

      saleItem.setPrimaryKey(key);
      insertInitFollowBody(saleItem);
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

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    return key;
  }

  public String insertInitItem(SaleinvoiceBVO saleItem, String foreignKey)
    throws SQLException, SystemException
  {
    saleItem.setCsaleid(foreignKey);
    String key = insertInitItem(saleItem);

    return key;
  }

  public String insertItem(SaleinvoiceBVO saleItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    Connection con = getConnection();

    PreparedStatement stmt = getBodyStatement(con);
    PreparedStatement stmtfollow = getFolowStatement(con);
    String key = insertItem(saleItem, stmt, stmtfollow);

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    return key;
  }

  public String insertItem(SaleinvoiceBVO saleItem, String foreignKey)
    throws SQLException, SystemException
  {
    saleItem.setCsaleid(foreignKey);
    String key = insertItem(saleItem);

    return key;
  }

  public String insertItem(SaleinvoiceBVO saleItem, String foreignKey, PreparedStatement stmt, PreparedStatement stmtfollow)
    throws SQLException, SystemException
  {
    saleItem.setCsaleid(foreignKey);
    String key = insertItem(saleItem, stmt, stmtfollow);

    return key;
  }

  public String insertItem(SaleinvoiceBVO saleItem, PreparedStatement stmt, PreparedStatement stmtfollow)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    String key = null;
    String pk_corp = saleItem.getPk_corp();
    try
    {
      if ((saleItem.getCsale_bid() == null) || (saleItem.getCsale_bid().trim().equals("")))
      {
        key = getOID(pk_corp);
      }
      else key = saleItem.getCsale_bid().trim();

      stmt.setString(1, key);

      if (saleItem.getCsaleid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getCsaleid());
      }
      if (saleItem.getPk_corp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getPk_corp());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCinventoryid());
      }

      if (saleItem.getCunitid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCunitid());
      }

      if (saleItem.getCpackunitid() == null)
        stmt.setNull(6, 12);
      else {
        stmt.setString(6, saleItem.getCpackunitid());
      }

      if (saleItem.getNnumber() == null)
        stmt.setNull(7, 8);
      else {
        stmt.setBigDecimal(7, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(8, 8);
      else {
        stmt.setBigDecimal(8, saleItem.getNpacknumber().toBigDecimal());
      }
      if (saleItem.getNbalancenumber() == null)
        stmt.setNull(9, 8);
      else {
        stmt.setBigDecimal(9, saleItem.getNbalancenumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(10, 12);
      else {
        stmt.setString(10, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getCupreceipttype() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCupreceipttype());
      }
      if (saleItem.getCupsourcebillid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getCupsourcebillid());
      }
      if (saleItem.getCupsourcebillbodyid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getCupsourcebillbodyid());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getBlargessflag() == null)
        stmt.setString(17, "N");
      else {
        stmt.setString(17, saleItem.getBlargessflag().toString());
      }
      if (saleItem.getCbatchid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCbatchid());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(20, 8);
      else {
        stmt.setBigDecimal(20, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(21, 8);
      else {
        stmt.setBigDecimal(21, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(22, 8);
      else {
        stmt.setBigDecimal(22, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(23, 8);
      else {
        stmt.setBigDecimal(23, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(24, 8);
      else {
        stmt.setBigDecimal(24, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(25, 8);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(26, 8);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(27, 8);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(28, 8);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(29, 8);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(30, 8);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(31, 8);
      else {
        stmt.setBigDecimal(31, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(32, 8);
      else {
        stmt.setBigDecimal(32, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(33, 8);
      else {
        stmt.setBigDecimal(33, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(34, 8);
      else {
        stmt.setBigDecimal(34, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(35, 8);
      else {
        stmt.setBigDecimal(35, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(36, 8);
      else {
        stmt.setBigDecimal(36, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(37, 8);
      else {
        stmt.setBigDecimal(37, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(38, 8);
      else {
        stmt.setBigDecimal(38, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(39, 8);
      else {
        stmt.setBigDecimal(39, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getNsimulatecostmny() == null)
        stmt.setNull(40, 8);
      else {
        stmt.setBigDecimal(40, saleItem.getNsimulatecostmny().toBigDecimal());
      }

      if (saleItem.getNcostmny() == null)
        stmt.setNull(41, 8);
      else {
        stmt.setBigDecimal(41, saleItem.getNcostmny().toBigDecimal());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleItem.getDdeliverdate().toString());
      }
      if (saleItem.getFrowstatus() == null)
        stmt.setNull(43, 4);
      else {
        stmt.setInt(43, saleItem.getFrowstatus().intValue());
      }
      if (saleItem.getFrownote() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleItem.getFrownote());
      }
      if (saleItem.getCinvbasdocid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleItem.getCinvbasdocid());
      }
      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(46, 8);
      else {
        stmt.setBigDecimal(46, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(47, 4);
      else {
        stmt.setInt(47, saleItem.getFbatchstatus().intValue());
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
      if (saleItem.getCreceiptcorpid() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleItem.getCreceiptcorpid());
      }
      if (saleItem.getrowno() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleItem.getrowno());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getCoriginalbillcode() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getCoriginalbillcode());
      }

      if (saleItem.getCupsourcebillcode() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleItem.getCupsourcebillcode());
      }

      if (saleItem.getCquoteunitid() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleItem.getCquoteunitid());
      }
      if (saleItem.getNquotenumber() == null)
        stmt.setNull(56, 8);
      else {
        stmt.setBigDecimal(56, saleItem.getNquotenumber().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurprice() == null)
        stmt.setNull(57, 8);
      else {
        stmt.setBigDecimal(57, saleItem.getNquoteoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxprice() == null)
        stmt.setNull(58, 8);
      else {
        stmt.setBigDecimal(58, saleItem.getNquoteoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurnetprice() == null)
        stmt.setNull(59, 8);
      else {
        stmt.setBigDecimal(59, saleItem.getNquoteoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxnetprice() == null)
        stmt.setNull(60, 8);
      else {
        stmt.setBigDecimal(60, saleItem.getNquoteoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteprice() == null)
        stmt.setNull(61, 8);
      else {
        stmt.setBigDecimal(61, saleItem.getNquoteprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxprice() == null)
        stmt.setNull(62, 8);
      else {
        stmt.setBigDecimal(62, saleItem.getNquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNquotenetprice() == null)
        stmt.setNull(63, 8);
      else {
        stmt.setBigDecimal(63, saleItem.getNquotenetprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxnetprice() == null)
        stmt.setNull(64, 8);
      else {
        stmt.setBigDecimal(64, saleItem.getNquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubquoteprice() == null)
        stmt.setNull(65, 8);
      else {
        stmt.setBigDecimal(65, saleItem.getNsubquoteprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxprice() == null)
        stmt.setNull(66, 8);
      else {
        stmt.setBigDecimal(66, saleItem.getNsubquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNsubquotenetprice() == null)
        stmt.setNull(67, 8);
      else {
        stmt.setBigDecimal(67, saleItem.getNsubquotenetprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxnetprice() == null)
        stmt.setNull(68, 8);
      else {
        stmt.setBigDecimal(68, saleItem.getNsubquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubsummny() == null)
        stmt.setNull(69, 8);
      else {
        stmt.setBigDecimal(69, saleItem.getNsubsummny().toBigDecimal());
      }
      if (saleItem.getNsubtaxnetprice() == null)
        stmt.setNull(70, 8);
      else {
        stmt.setBigDecimal(70, saleItem.getNsubtaxnetprice().toBigDecimal());
      }

      if (saleItem.getCprolineid() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, saleItem.getCprolineid());
      }

      if (saleItem.getNsubcursummny() == null)
        stmt.setNull(72, 8);
      else {
        stmt.setBigDecimal(72, saleItem.getNsubcursummny().toBigDecimal());
      }

      if (saleItem.getNquoteunitrate() == null)
        stmt.setNull(73, 8);
      else {
        stmt.setBigDecimal(73, saleItem.getNquoteunitrate().toBigDecimal());
      }

      if (saleItem.getCcustomerid() == null)
        stmt.setNull(74, 1);
      else {
        stmt.setString(74, saleItem.getCcustomerid());
      }
      //"httsbl","httssl","sjtsje","sjtssl","ztsje"
      if (saleItem.getHttsbl() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setDouble(75, saleItem.getHttsbl().doubleValue());
        }
      if (saleItem.getHttssl() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setInt(76, saleItem.getHttssl());
        }
      if (saleItem.getSjtsje() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setDouble(77, saleItem.getSjtsje().doubleValue());
        }
      if (saleItem.getSjtssl() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setInt(78, saleItem.getSjtssl());
        }
      if (saleItem.getZtsje() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setDouble(79, saleItem.getZtsje().doubleValue());
        }
      if(saleItem.getB_cjje1()==null)
    	  stmt.setNull(80, 1);
      else {
        stmt.setString(80, saleItem.getB_cjje1());
      }
      if(saleItem.getB_cjje2()==null)
    	  stmt.setNull(81, 1);
      else {
        stmt.setString(81, saleItem.getB_cjje2());
      }
      if(saleItem.getB_cjje3()==null)
    	  stmt.setNull(82, 1);
      else {
        stmt.setString(82, saleItem.getB_cjje3());
      }
      executeUpdate(stmt);

      saleItem.setPrimaryKey(key);
      insertFollowBody(saleItem, stmtfollow);
    }
    finally
    {
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertItem", new Object[] { saleItem });

    return key;
  }

  public void insertSaleData(SaleinvoiceVO saleinvoice)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertSaleData", new Object[] { saleinvoice });

    SaleVO saleHeader = (SaleVO)saleinvoice.getParentVO();
    SaleinvoiceBVO[] saleBodyer = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();

    String sCustBasid = null;

    String sql = "insert into po_saledata(csale_bid,csaleid, pk_corp, dsaledate, cdeptid, cmangid, cbaseid, cvendormangid, cvendorbaseid, nsalenum, nsalemny,cstoreorganization,cwarehouseid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      Hashtable hCustBasID = getCustBasIDByCustManID(BillTools.getUniqueArrayFromVOsByKey(saleBodyer, "ccustomerid"));

      con = getConnection();
      stmt = con.prepareStatement(sql);
      Hashtable htInvAttr = getInvAttrC(saleHeader.getCsaleid());
      Object oTemp = null;
      UFBoolean ufbInvAttr = null;
      for (int i = 0; i < saleBodyer.length; i++) {
        SaleinvoiceBVO saleBody = saleBodyer[i];
        oTemp = htInvAttr.get(saleBody.getCinventoryid());
        if (oTemp != null)
          ufbInvAttr = (UFBoolean)oTemp;
        else {
          ufbInvAttr = null;
        }
        if ((ufbInvAttr == null) || (!ufbInvAttr.booleanValue()))
          continue;
        if (saleBody.getPrimaryKey() == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, saleBody.getPrimaryKey());
        }

        if (saleBody.getCsaleid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, saleBody.getCsaleid());
        }

        if (saleBody.getPk_corp() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, saleBody.getPk_corp());
        }

        if (saleHeader.getDbilldate() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, saleHeader.getDbilldate().toString());
        }

        if (saleHeader.getCdeptid() == null)
          stmt.setNull(5, 1);
        else {
          stmt.setString(5, saleHeader.getCdeptid());
        }

        if (saleBody.getCinventoryid() == null)
          stmt.setNull(6, 1);
        else {
          stmt.setString(6, saleBody.getCinventoryid());
        }

        if (saleBody.getCinvbasdocid() == null)
          stmt.setNull(7, 1);
        else {
          stmt.setString(7, saleBody.getCinvbasdocid());
        }

        if (saleBody.getCinventoryid() == null)
          stmt.setNull(8, 1);
        else {
          stmt.setString(8, saleBody.getCcustomerid());
        }

        sCustBasid = null;
        if (saleBody.getCcustomerid() != null) {
          sCustBasid = (String)hCustBasID.get(saleBody.getCcustomerid());
        }
        if (sCustBasid == null)
          stmt.setNull(9, 1);
        else {
          stmt.setString(9, sCustBasid);
        }

        if (saleBody.getNnumber() == null)
          stmt.setNull(10, 4);
        else {
          stmt.setBigDecimal(10, saleBody.getNnumber().toBigDecimal());
        }

        if (saleBody.getNmny() == null)
          stmt.setNull(11, 4);
        else {
          stmt.setBigDecimal(11, saleBody.getNmny().toBigDecimal());
        }

        if (saleBody.getCadvisecalbodyid() == null)
          stmt.setNull(12, 1);
        else {
          stmt.setString(12, saleBody.getCadvisecalbodyid().trim());
        }

        if (saleBody.getCbodywarehouseid() == null)
          stmt.setNull(13, 1);
        else {
          stmt.setString(13, saleBody.getCbodywarehouseid().trim());
        }

        stmt.executeUpdate();
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "insertSaleData", new Object[] { saleinvoice });
  }

  public boolean isCodeExist(String code, String pk_corp)
    throws SQLException
  {
    String sql = "SELECT vreceiptcode FROM so_saleinvoice WHERE vreceiptcode = '" + code + "' AND pk_corp = '" + pk_corp + "' and creceipttype='" + "32" + "'";

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    stmt = con.prepareStatement(sql);
    Object o = null;
    try {
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

  private boolean isDirectTran(String strBizType)
    throws SQLException
  {
    if ((strBizType == null) || (strBizType.length() == 0))
      return false;
    boolean result = false;
    String sql = "select verifyrule  from bd_busitype where pk_busitype = '" + strBizType + "' ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String temp = rs.getString(1);
        if ((temp == null ? "" : temp.trim()).equals("Z"))
          result = true;
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

  private void onCheck(SaleinvoiceVO saleinvoice)
    throws BusinessException, NamingException, SQLException, SystemException
  {
    CheckValueValidityImpl checkDmo = new CheckValueValidityImpl();
    checkDmo.isValueValidity(saleinvoice);
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    return queryAllBodyData(key, null);
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String where)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryHeadAllData", null);

    StringBuffer sql = new StringBuffer();
    sql.append("select DISTINCT so_saleinvoice.pk_corp, so_saleinvoice.vreceiptcode, so_saleinvoice.creceipttype,");

    sql.append("so_saleinvoice.cbiztype, so_saleinvoice.finvoiceclass, so_saleinvoice.finvoicetype,");

    sql.append("so_saleinvoice.vaccountyear, so_saleinvoice.binitflag, so_saleinvoice.dbilldate,");

    sql.append(" so_saleinvoice.cdeptid, so_saleinvoice.cemployeeid, ");

    sql.append("so_saleinvoice.coperatorid, so_saleinvoice.ctermprotocolid, so_saleinvoice.csalecorpid,");

    sql.append("so_saleinvoice.creceiptcustomerid, so_saleinvoice.vreceiveaddress, so_saleinvoice.creceiptcorpid,");

    sql.append("so_saleinvoice.ctransmodeid, so_saleinvoice.ndiscountrate, so_saleinvoice.cwarehouseid,");

    sql.append("so_saleinvoice.veditreason, so_saleinvoice.bfreecustflag, so_saleinvoice.cfreecustid,");

    sql.append("so_saleinvoice.ibalanceflag, so_saleinvoice.nsubscription, so_saleinvoice.ccreditnum,");

    sql.append("so_saleinvoice.nevaluatecarriage, so_saleinvoice.dmakedate, so_saleinvoice.capproveid,");

    sql.append("so_saleinvoice.dapprovedate, so_saleinvoice.fstatus, so_saleinvoice.vnote, so_saleinvoice.vdef1,");

    sql.append("so_saleinvoice.vdef2, so_saleinvoice.vdef3, so_saleinvoice.vdef4, so_saleinvoice.vdef5,");

    sql.append("so_saleinvoice.vdef6, so_saleinvoice.vdef7, so_saleinvoice.vdef8, so_saleinvoice.vdef9,");

    sql.append("so_saleinvoice.vdef10,so_saleinvoice.csaleid,so_saleinvoice.ccustbankid,so_saleinvoice.ccalbodyid,");

    sql.append("so_saleinvoice.ts,so_saleinvoice.cdispatcherid ");

    sql.append(",so_saleinvoice.ntotalsummny,so_saleinvoice.nstrikemny,so_saleinvoice.nnetmny ");

    sql.append(",so_saleinvoice_b.ccurrencytypeid ");

    sql.append(", so_saleinvoice.vdef11, so_saleinvoice.vdef12, so_saleinvoice.vdef13, so_saleinvoice.vdef14, so_saleinvoice.vdef15 ");

    sql.append(", so_saleinvoice.vdef16, so_saleinvoice.vdef17, so_saleinvoice.vdef18, so_saleinvoice.vdef19, so_saleinvoice.vdef20 ");

    sql.append(", so_saleinvoice.pk_defdoc1, so_saleinvoice.pk_defdoc2, so_saleinvoice.pk_defdoc3, so_saleinvoice.pk_defdoc4, so_saleinvoice.pk_defdoc5 ");

    sql.append(", so_saleinvoice.pk_defdoc6, so_saleinvoice.pk_defdoc7, so_saleinvoice.pk_defdoc8, so_saleinvoice.pk_defdoc9, so_saleinvoice.pk_defdoc10 ");

    sql.append(", so_saleinvoice.pk_defdoc11, so_saleinvoice.pk_defdoc12, so_saleinvoice.pk_defdoc13, so_saleinvoice.pk_defdoc14, so_saleinvoice.pk_defdoc15 ");

    sql.append(", so_saleinvoice.pk_defdoc16, so_saleinvoice.pk_defdoc17, so_saleinvoice.pk_defdoc18, so_saleinvoice.pk_defdoc19, so_saleinvoice.pk_defdoc20 ");

    sql.append(", so_saleinvoice.iprintcount ,so_saleinvoice.fcounteractflag ");
    sql.append(",vprintcustname,dbilltime,daudittime,dmoditime ");
    sql.append(" from so_saleinvoice ");
    sql.append("inner join so_saleinvoice_b on so_saleinvoice.csaleid=so_saleinvoice_b.csaleid ");

    sql.append("inner join so_saleexecute on so_saleinvoice.csaleid = so_saleexecute.csaleid ");

    if ((where != null) && (!where.equals("")))
      sql.append("where ");
    sql.append(where);
    sql.append(" order by so_saleinvoice.csaleid ");

    Vector v = new Vector();
    SaleVO[] saleHeaders = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleVO saleHeader = new SaleVO();
        int iseq = 1;

        String pk_corp = rs.getString(iseq++);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.toString().trim());

        String vreceiptcode = rs.getString(iseq++);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(iseq++);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(iseq++);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(iseq++);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(iseq++);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(iseq++);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(iseq++);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(iseq++);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String cdeptid = rs.getString(iseq++);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(iseq++);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(iseq++);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(iseq++);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(iseq++);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(iseq++);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(iseq++);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(iseq++);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(iseq++);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        String cwarehouseid = rs.getString(iseq++);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(iseq++);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(iseq++);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(iseq++);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(iseq++);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        BigDecimal nsubscription = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

        String ccreditnum = rs.getString(iseq++);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(iseq++);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(iseq++);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(iseq++);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(iseq++);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(iseq++);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(iseq++);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(iseq++);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(iseq++);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(iseq++);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(iseq++);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(iseq++);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(iseq++);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(iseq++);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(iseq++);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(iseq++);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        saleHeader.setPrimaryKey(rs.getString(iseq++));

        String ccustbankid = rs.getString(iseq++);
        saleHeader.setCcustbankid(ccustbankid == null ? null : ccustbankid.trim());

        String ccalbodyid = rs.getString(iseq++);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String ts = rs.getString(iseq++);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts));

        String cdispatcherid = rs.getString(iseq++);
        saleHeader.setcdispatcherid(cdispatcherid == null ? null : cdispatcherid.trim());

        BigDecimal ntotalsummny = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNtotalsummny(ntotalsummny == null ? null : new UFDouble(ntotalsummny));

        BigDecimal nstrikemny = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNstrikemny(nstrikemny == null ? null : new UFDouble(nstrikemny));

        BigDecimal nnetmny = (BigDecimal)rs.getObject(iseq++);
        saleHeader.setNnetmny(nnetmny == null ? null : new UFDouble(nnetmny));

        String ccurrencytypeid = rs.getString(iseq++);
        saleHeader.setCcurrencyid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        String vdef11 = rs.getString(iseq++);
        saleHeader.setVdef11(vdef11 == null ? null : vdef11.trim());
        String vdef12 = rs.getString(iseq++);
        saleHeader.setVdef12(vdef12 == null ? null : vdef12.trim());
        String vdef13 = rs.getString(iseq++);
        saleHeader.setVdef13(vdef13 == null ? null : vdef13.trim());
        String vdef14 = rs.getString(iseq++);
        saleHeader.setVdef14(vdef14 == null ? null : vdef14.trim());
        String vdef15 = rs.getString(iseq++);
        saleHeader.setVdef15(vdef15 == null ? null : vdef15.trim());
        String vdef16 = rs.getString(iseq++);
        saleHeader.setVdef16(vdef16 == null ? null : vdef16.trim());
        String vdef17 = rs.getString(iseq++);
        saleHeader.setVdef17(vdef17 == null ? null : vdef17.trim());
        String vdef18 = rs.getString(iseq++);
        saleHeader.setVdef18(vdef18 == null ? null : vdef18.trim());
        String vdef19 = rs.getString(iseq++);
        saleHeader.setVdef19(vdef19 == null ? null : vdef19.trim());
        String vdef20 = rs.getString(iseq++);
        saleHeader.setVdef20(vdef20 == null ? null : vdef20.trim());
        String pk_defdoc1 = rs.getString(iseq++);
        saleHeader.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(iseq++);
        saleHeader.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(iseq++);
        saleHeader.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(iseq++);
        saleHeader.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(iseq++);
        saleHeader.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(iseq++);
        saleHeader.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(iseq++);
        saleHeader.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(iseq++);
        saleHeader.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(iseq++);
        saleHeader.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(iseq++);
        saleHeader.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(iseq++);
        saleHeader.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(iseq++);
        saleHeader.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(iseq++);
        saleHeader.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(iseq++);
        saleHeader.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(iseq++);
        saleHeader.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(iseq++);
        saleHeader.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(iseq++);
        saleHeader.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(iseq++);
        saleHeader.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(iseq++);
        saleHeader.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(iseq++);
        saleHeader.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        Object iprintcount = rs.getObject(iseq++);
        saleHeader.setIprintcount((iprintcount == null) || (iprintcount.toString().length() <= 0) ? null : new Integer(iprintcount.toString()));

        Integer fcounteractflag = (Integer)rs.getObject(iseq++);
        saleHeader.setFcounteractflag(fcounteractflag == null ? new Integer(0) : fcounteractflag);

        String vprintcustname = rs.getString(iseq++);
        saleHeader.setVprintcustname(vprintcustname == null ? null : vprintcustname.trim());
        String dbilltime = rs.getString(iseq++);
        saleHeader.setDbilltime(dbilltime == null ? null : dbilltime.trim());
        String daudittime = rs.getString(iseq++);
        saleHeader.setDaudittime(daudittime == null ? null : daudittime.trim());
        String dmoditime = rs.getString(iseq++);
        saleHeader.setDmoditime(dmoditime == null ? null : dmoditime.trim());

        v.addElement(saleHeader);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (v.size() > 0) {
      saleHeaders = new SaleVO[v.size()];
      v.copyInto(saleHeaders);
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryHeadAllData", null);

    return saleHeaders;
  }

  public SaleVO[] queryAllHeadData(String corpID, String userID, String curDate)
    throws SQLException
  {
    try
    {
      String sWhere = " pk_corp='" + corpID + "' and coperatorid='" + userID + "' and dmakedate='" + curDate + "' and creceipttype = '" + "32" + "' and fstatus not in (5) order by csaleid";

      return (SaleVO[])(SaleVO[])queryAllHeadData(sWhere);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    }
  }

  public Hashtable queryBillCodeBySource(int type, String range)
    throws SQLException
  {
    if (type < 3) return queryBillCodeBySource2(type, range);
    Hashtable h1 = new Hashtable();
    Hashtable h2 = new Hashtable();

    int ipos = range.indexOf("@#$%");
    String str1 = range.substring(0, ipos);
    String str2 = range.substring(ipos + 4);
    h1 = queryBillCodeBySource2(1, str1);
    h2 = queryBillCodeBySource2(2, str2);
    h1.putAll(h2);
    return h1;
  }

  public Hashtable queryBillCodeBySource2(int type, String range)
    throws SQLException
  {
    StringTokenizer token = new StringTokenizer(range, ",");

    String[] bodyids = new String[token.countTokens()];
    ArrayList aryBodyids = new ArrayList();
    while (token.hasMoreTokens()) {
      String str = token.nextToken();
      str = str.substring(1, str.length() - 1);
      if (!str.equals(""))
        aryBodyids.add(str);
    }
    String sql = null;
    if (type == 1) {
      sql = "select so_saleorder_b.corder_bid,so_sale.vreceiptcode  \tfrom so_sale inner join so_saleorder_b on so_sale.csaleid=so_saleorder_b.csaleid  where (1=1) " + GeneralSqlString.formInSQL("so_saleorder_b.corder_bid", aryBodyids);
    }
    else if (type == 2) {
      sql = "select ic_general_b.cgeneralbid,ic_general_h.vbillcode  from ic_general_h inner join ic_general_b on ic_general_h.cgeneralhid=ic_general_b.cgeneralhid  where (1=1) " + GeneralSqlString.formInSQL("ic_general_b.cgeneralbid", aryBodyids);
    }

    Hashtable h = new Hashtable();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String csourcebillbodyid = null;
        String coriginalbillcode = null;
        csourcebillbodyid = rs.getString(1);
        coriginalbillcode = rs.getString(2);
        h.put(csourcebillbodyid, coriginalbillcode);
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
    return h;
  }

  public SaleinvoiceBVO[] queryBodyDataBybid(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyDataBybid", new Object[] { key });

    String sql = "select cinvoice_bid, csaleid, pk_corp, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, nbalancenumber, cbodywarehouseid, cupreceipttype, cupsourcebillid, cupsourcebillbodyid, creceipttype, csourcebillid, csourcebillbodyid, blargessflag, cbatchid, ccurrencytypeid, nexchangeotobrate, nexchangeotoarate, nitemdiscountrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, nsimulatecostmny, ncostmny, ddeliverdate, frowstatus, frownote,cinvbasdocid, ndiscountrate,fbatchstatus,ct_manageid,cfreezeid,ts, creceiptcorpid,crowno  ,ccalbodyid  ,coriginalbillcode  ,cupsourcebillcode  ,cquoteunitid,nquotenumber,nquoricurpri,nquoricurtaxpri,nquoricurnetpri,nqocurtaxnetpri,nquprice,nqutaxprice,nqunetprice,nqutaxnetprice,nsubqupri,nsubqutaxpri,nsubqunetpri,nsubqutaxnetpri,nsubsummny,nsubtaxnetprice,cprolineid  ,nsubcursummny,nquoteunitrate,ccustomerid from so_saleinvoice_b where cinvoice_bid = ?  ";

    SaleinvoiceBVO[] saleItem = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        saleItem = new SaleinvoiceBVO[1];

        saleItem[0] = new SaleinvoiceBVO();

        String cinvoice_bid = rs.getString("cinvoice_bid");
        saleItem[0].setCinvoice_bid(cinvoice_bid == null ? null : cinvoice_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem[0].setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem[0].setPk_corp(ccorpid == null ? null : ccorpid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem[0].setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem[0].setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem[0].setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem[0].setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem[0].setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        BigDecimal nbalancenumber = (BigDecimal)rs.getObject("nbalancenumber");

        saleItem[0].setNbalancenumber(nbalancenumber == null ? null : new UFDouble(nbalancenumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem[0].setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String cupreceipttype = rs.getString("cupreceipttype");
        saleItem[0].setCupreceipttype(cupreceipttype == null ? null : cupreceipttype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        saleItem[0].setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillbodyid = rs.getString("cupsourcebillbodyid");

        saleItem[0].setCupsourcebillbodyid(cupsourcebillbodyid == null ? null : cupsourcebillbodyid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem[0].setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem[0].setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem[0].setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String blargessflag = rs.getString("blargessflag");
        saleItem[0].setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String cbatchid = rs.getString("cbatchid");
        saleItem[0].setCbatchid(cbatchid == null ? null : cbatchid.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem[0].setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem[0].setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem[0].setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem[0].setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem[0].setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem[0].setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem[0].setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem[0].setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem[0].setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem[0].setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem[0].setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem[0].setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem[0].setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem[0].setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem[0].setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem[0].setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem[0].setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem[0].setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem[0].setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem[0].setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem[0].setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        BigDecimal nsimulatecostmny = (BigDecimal)rs.getObject("nsimulatecostmny");

        saleItem[0].setNsimulatecostmny(nsimulatecostmny == null ? null : new UFDouble(nsimulatecostmny));

        BigDecimal ncostmny = (BigDecimal)rs.getObject("ncostmny");
        saleItem[0].setNcostmny(ncostmny == null ? null : new UFDouble(ncostmny));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem[0].setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem[0].setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem[0].setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem[0].setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem[0].setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem[0].setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String ct_manageid = rs.getString("ct_manageid");
        saleItem[0].setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem[0].setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ts = rs.getString("ts");
        saleItem[0].setTs(ts == null ? null : new UFDateTime(ts));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleItem[0].setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String crowno = rs.getString("crowno");
        saleItem[0].setrowno(crowno == null ? null : crowno.trim());

        String ccalbodyid = rs.getString("ccalbodyid");
        saleItem[0].setCadvisecalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String coriginalbillcode = rs.getString("coriginalbillcode");
        saleItem[0].setCoriginalbillcode(coriginalbillcode == null ? null : coriginalbillcode.trim());

        String cupsourcebillcode = rs.getString("cupsourcebillcode");
        saleItem[0].setCupsourcebillcode(cupsourcebillcode == null ? null : cupsourcebillcode.trim());

        String cquoteunitid = rs.getString("cquoteunitid");
        saleItem[0].setCquoteunitid(cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquotenumber = (BigDecimal)rs.getObject("nquotenumber");

        saleItem[0].setNquotenumber(nquotenumber == null ? null : new UFDouble(nquotenumber));

        BigDecimal nquoricurpri = (BigDecimal)rs.getObject("nquoricurpri");

        saleItem[0].setNquoteoriginalcurprice(nquoricurpri == null ? null : new UFDouble(nquoricurpri));

        BigDecimal nquoricurtaxpri = (BigDecimal)rs.getObject("nquoricurtaxpri");

        saleItem[0].setNquoteoriginalcurtaxprice(nquoricurtaxpri == null ? null : new UFDouble(nquoricurtaxpri));

        BigDecimal nquoricurnetpri = (BigDecimal)rs.getObject("nquoricurnetpri");

        saleItem[0].setNquoteoriginalcurnetprice(nquoricurnetpri == null ? null : new UFDouble(nquoricurnetpri));

        BigDecimal nqocurtaxnetpri = (BigDecimal)rs.getObject("nqocurtaxnetpri");

        saleItem[0].setNquoteoriginalcurtaxnetprice(nqocurtaxnetpri == null ? null : new UFDouble(nqocurtaxnetpri));

        BigDecimal nquprice = (BigDecimal)rs.getObject("nquprice");
        saleItem[0].setNquoteprice(nquprice == null ? null : new UFDouble(nquprice));

        BigDecimal nqutaxprice = (BigDecimal)rs.getObject("nqutaxprice");

        saleItem[0].setNquotetaxprice(nqutaxprice == null ? null : new UFDouble(nqutaxprice));

        BigDecimal nqunetprice = (BigDecimal)rs.getObject("nqunetprice");

        saleItem[0].setNquotenetprice(nqunetprice == null ? null : new UFDouble(nqunetprice));

        BigDecimal nqutaxnetprice = (BigDecimal)rs.getObject("nqutaxnetprice");

        saleItem[0].setNquotetaxnetprice(nqutaxnetprice == null ? null : new UFDouble(nqutaxnetprice));

        BigDecimal nsubqupri = (BigDecimal)rs.getObject("nsubqupri");
        saleItem[0].setNsubquoteprice(nsubqupri == null ? null : new UFDouble(nsubqupri));

        BigDecimal nsubqutaxpri = (BigDecimal)rs.getObject("nsubqutaxpri");

        saleItem[0].setNsubquotetaxprice(nsubqutaxpri == null ? null : new UFDouble(nsubqutaxpri));

        BigDecimal nsubqunetpri = (BigDecimal)rs.getObject("nsubqunetpri");

        saleItem[0].setNsubquotenetprice(nsubqunetpri == null ? null : new UFDouble(nsubqunetpri));

        BigDecimal nsubqutaxnetpri = (BigDecimal)rs.getObject("nsubqutaxnetpri");

        saleItem[0].setNsubquotetaxnetprice(nsubqutaxnetpri == null ? null : new UFDouble(nsubqutaxnetpri));

        BigDecimal nsubsummny = (BigDecimal)rs.getObject("nsubsummny");
        saleItem[0].setNsubsummny(nsubsummny == null ? null : new UFDouble(nsubsummny));

        BigDecimal nsubtaxnetprice = (BigDecimal)rs.getObject("nsubtaxnetprice");

        saleItem[0].setNsubtaxnetprice(nsubtaxnetprice == null ? null : new UFDouble(nsubtaxnetprice));

        String cprolineid = rs.getString("cprolineid");
        saleItem[0].setCprolineid(cprolineid == null ? null : cprolineid.trim());

        BigDecimal nsubcursummny = (BigDecimal)rs.getObject("nsubcursummny");

        saleItem[0].setNsubcursummny(nsubcursummny == null ? null : new UFDouble(nsubcursummny));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject("nquoteunitrate");

        saleItem[0].setNquoteunitrate(nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        String ccustomerid = rs.getString("ccustomerid");
        saleItem[0].setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());
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
    if (saleItem != null) {
      queryFollowBody(saleItem);
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyData", new Object[] { key });

    return saleItem;
  }

  public SaleinvoiceBVO[] queryBodyData(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyData", new Object[] { key });

    String sql = "select cinvoice_bid, csaleid, pk_corp, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, nbalancenumber, cbodywarehouseid, cupreceipttype, cupsourcebillid, cupsourcebillbodyid, creceipttype, csourcebillid, csourcebillbodyid, blargessflag, cbatchid, ccurrencytypeid, nexchangeotobrate, nexchangeotoarate, nitemdiscountrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, nsimulatecostmny, ncostmny, ddeliverdate, frowstatus, frownote,cinvbasdocid, ndiscountrate,fbatchstatus,ct_manageid,cfreezeid,ts, creceiptcorpid,crowno  ,ccalbodyid  ,coriginalbillcode  ,cupsourcebillcode  ,cquoteunitid,nquotenumber,nquoricurpri,nquoricurtaxpri,nquoricurnetpri,nqocurtaxnetpri,nquprice,nqutaxprice,nqunetprice,nqutaxnetprice,nsubqupri,nsubqutaxpri,nsubqunetpri,nsubqutaxnetpri,nsubsummny,nsubtaxnetprice,cprolineid  ,nsubcursummny,nquoteunitrate,ccustomerid,httsbl,httssl,sjtsje,sjtssl,ztsje,b_cjje1,b_cjje2,b_cjje3 from so_saleinvoice_b where csaleid = ?  order by cinvoice_bid ";

    SaleinvoiceBVO[] saleItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleinvoiceBVO saleItem = new SaleinvoiceBVO();

        String cinvoice_bid = rs.getString("cinvoice_bid");
        saleItem.setCinvoice_bid(cinvoice_bid == null ? null : cinvoice_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPk_corp(ccorpid == null ? null : ccorpid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        BigDecimal nbalancenumber = (BigDecimal)rs.getObject("nbalancenumber");

        saleItem.setNbalancenumber(nbalancenumber == null ? null : new UFDouble(nbalancenumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String cupreceipttype = rs.getString("cupreceipttype");
        saleItem.setCupreceipttype(cupreceipttype == null ? null : cupreceipttype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        saleItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillbodyid = rs.getString("cupsourcebillbodyid");

        saleItem.setCupsourcebillbodyid(cupsourcebillbodyid == null ? null : cupsourcebillbodyid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        BigDecimal nsimulatecostmny = (BigDecimal)rs.getObject("nsimulatecostmny");

        saleItem.setNsimulatecostmny(nsimulatecostmny == null ? null : new UFDouble(nsimulatecostmny));

        BigDecimal ncostmny = (BigDecimal)rs.getObject("ncostmny");
        saleItem.setNcostmny(ncostmny == null ? null : new UFDouble(ncostmny));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String ct_manageid = rs.getString("ct_manageid");
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String crowno = rs.getString("crowno");
        saleItem.setrowno(crowno == null ? null : crowno.trim());

        String ccalbodyid = rs.getString("ccalbodyid");
        saleItem.setCadvisecalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String coriginalbillcode = rs.getString("coriginalbillcode");
        saleItem.setCoriginalbillcode(coriginalbillcode == null ? null : coriginalbillcode.trim());

        String cupsourcebillcode = rs.getString("cupsourcebillcode");
        saleItem.setCupsourcebillcode(cupsourcebillcode == null ? null : cupsourcebillcode.trim());

        String cquoteunitid = rs.getString("cquoteunitid");
        saleItem.setCquoteunitid(cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquotenumber = (BigDecimal)rs.getObject("nquotenumber");

        saleItem.setNquotenumber(nquotenumber == null ? null : new UFDouble(nquotenumber));

        BigDecimal nquoricurpri = (BigDecimal)rs.getObject("nquoricurpri");

        saleItem.setNquoteoriginalcurprice(nquoricurpri == null ? null : new UFDouble(nquoricurpri));

        BigDecimal nquoricurtaxpri = (BigDecimal)rs.getObject("nquoricurtaxpri");

        saleItem.setNquoteoriginalcurtaxprice(nquoricurtaxpri == null ? null : new UFDouble(nquoricurtaxpri));

        BigDecimal nquoricurnetpri = (BigDecimal)rs.getObject("nquoricurnetpri");

        saleItem.setNquoteoriginalcurnetprice(nquoricurnetpri == null ? null : new UFDouble(nquoricurnetpri));

        BigDecimal nqocurtaxnetpri = (BigDecimal)rs.getObject("nqocurtaxnetpri");

        saleItem.setNquoteoriginalcurtaxnetprice(nqocurtaxnetpri == null ? null : new UFDouble(nqocurtaxnetpri));

        BigDecimal nquprice = (BigDecimal)rs.getObject("nquprice");
        saleItem.setNquoteprice(nquprice == null ? null : new UFDouble(nquprice));

        BigDecimal nqutaxprice = (BigDecimal)rs.getObject("nqutaxprice");

        saleItem.setNquotetaxprice(nqutaxprice == null ? null : new UFDouble(nqutaxprice));

        BigDecimal nqunetprice = (BigDecimal)rs.getObject("nqunetprice");

        saleItem.setNquotenetprice(nqunetprice == null ? null : new UFDouble(nqunetprice));

        BigDecimal nqutaxnetprice = (BigDecimal)rs.getObject("nqutaxnetprice");

        saleItem.setNquotetaxnetprice(nqutaxnetprice == null ? null : new UFDouble(nqutaxnetprice));

        BigDecimal nsubqupri = (BigDecimal)rs.getObject("nsubqupri");
        saleItem.setNsubquoteprice(nsubqupri == null ? null : new UFDouble(nsubqupri));

        BigDecimal nsubqutaxpri = (BigDecimal)rs.getObject("nsubqutaxpri");

        saleItem.setNsubquotetaxprice(nsubqutaxpri == null ? null : new UFDouble(nsubqutaxpri));

        BigDecimal nsubqunetpri = (BigDecimal)rs.getObject("nsubqunetpri");

        saleItem.setNsubquotenetprice(nsubqunetpri == null ? null : new UFDouble(nsubqunetpri));

        BigDecimal nsubqutaxnetpri = (BigDecimal)rs.getObject("nsubqutaxnetpri");

        saleItem.setNsubquotetaxnetprice(nsubqutaxnetpri == null ? null : new UFDouble(nsubqutaxnetpri));

        BigDecimal nsubsummny = (BigDecimal)rs.getObject("nsubsummny");
        saleItem.setNsubsummny(nsubsummny == null ? null : new UFDouble(nsubsummny));

        BigDecimal nsubtaxnetprice = (BigDecimal)rs.getObject("nsubtaxnetprice");

        saleItem.setNsubtaxnetprice(nsubtaxnetprice == null ? null : new UFDouble(nsubtaxnetprice));

        String cprolineid = rs.getString("cprolineid");
        saleItem.setCprolineid(cprolineid == null ? null : cprolineid.trim());

        BigDecimal nsubcursummny = (BigDecimal)rs.getObject("nsubcursummny");

        saleItem.setNsubcursummny(nsubcursummny == null ? null : new UFDouble(nsubcursummny));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject("nquoteunitrate");

        saleItem.setNquoteunitrate(nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        String ccustomerid = rs.getString("ccustomerid");
        saleItem.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());
        
        //httsbl,httssl,sjtsje,sjtssl,ztsje
        Double dv = rs.getDouble("httsbl");
        dv = dv==null?0:dv;
        saleItem.setHttsbl(new UFDouble(dv));
        
        saleItem.setHttssl(rs.getInt("httssl"));
        
        dv = rs.getDouble("sjtsje");
        dv = dv==null?0:dv;
        saleItem.setSjtsje(new UFDouble(dv));
        
        saleItem.setSjtssl(rs.getInt("sjtssl"));
        
        dv = rs.getDouble("ztsje");
        dv = dv==null?0:dv;
        saleItem.setZtsje(new UFDouble(dv));
        
        saleItem.setB_cjje1(rs.getString("b_cjje1"));
        saleItem.setB_cjje2(rs.getString("b_cjje2"));
        saleItem.setB_cjje3(rs.getString("b_cjje3"));
        
        
        
        

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
    saleItems = new SaleinvoiceBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }
    queryFollowBody(saleItems);

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyData", new Object[] { key });

    return saleItems;
  }

  public SaleinvoiceVO queryData(String strID)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;

    SaleinvoiceVO saleinvoice = new SaleinvoiceVO();
    con = getConnection();
    try {
      SaleVO head = queryHeadData(strID);
      if (head == null) {
        SaleinvoiceBVO[] items = queryBodyDataBybid(strID);
        if (items != null) {
          strID = items[0].getCsaleid();
          saleinvoice.setParentVO(queryHeadData(strID));
          saleinvoice.setChildrenVO(queryBodyData(strID));
        }
      }
      else {
        saleinvoice.setParentVO(head);
        saleinvoice.setChildrenVO(queryBodyData(strID));
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
    return saleinvoice;
  }

  public SaleinvoiceVO queryInvoiceVO(String sCinvoice_bid)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;

    SaleinvoiceVO saleinvoice = new SaleinvoiceVO();
    con = getConnection();
    try {
      SaleVO hVO = null;
      SaleinvoiceBVO[] bVO = queryBodyDataBybid(sCinvoice_bid);

      if ((bVO != null) && (bVO[0] != null) && (bVO[0].getCsaleid() != null)) {
        hVO = queryHeadData(bVO[0].getCsaleid());
      }
      saleinvoice.setParentVO(hVO);
      saleinvoice.setChildrenVO(bVO);
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
    return saleinvoice;
  }

  public SaleinvoiceVO[] queryDataBills(String[] strIDs)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;

    SaleinvoiceVO saleinvoice = null;
    SaleinvoiceVO[] hvos = null;
    ArrayList hvoArray = new ArrayList();

    con = getConnection();
    try
    {
      for (int i = 0; i < strIDs.length; i++) {
        if (strIDs[i] != null) {
          saleinvoice = new SaleinvoiceVO();
          saleinvoice.setParentVO(queryHeadData(strIDs[i]));
          saleinvoice.setChildrenVO(queryBodyData(strIDs[i]));
          hvoArray.add(saleinvoice);
        }
      }
      hvos = new SaleinvoiceVO[hvoArray.size()];
      hvoArray.toArray(hvos);
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
    return hvos;
  }

  public void queryFollowBody(SaleinvoiceBVO[] bodys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryFollowBody", new Object[] { bodys });

    String sql = "select csale_bid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber,  ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber,  ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish,  bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny,  nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice,  nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3,  vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6  , vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20  from so_saleexecute  where csaleid= ? order by csale_bid ";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      SaleinvoiceBVO saleexecute = bodys[0];
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
          String creceipttype = rs.getString(2);

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

          String vdef7 = rs.getString(39);
          saleexecute.setVdef7(vdef7 == null ? null : vdef7.trim());

          String vdef8 = rs.getString(40);
          saleexecute.setVdef8(vdef8 == null ? null : vdef8.trim());

          String vdef9 = rs.getString(41);
          saleexecute.setVdef9(vdef9 == null ? null : vdef9.trim());

          String vdef10 = rs.getString(42);
          saleexecute.setVdef10(vdef10 == null ? null : vdef10.trim());

          String vdef11 = rs.getString(43);
          saleexecute.setVdef11(vdef11 == null ? null : vdef11.trim());

          String vdef12 = rs.getString(44);
          saleexecute.setVdef12(vdef12 == null ? null : vdef12.trim());

          String vdef13 = rs.getString(45);
          saleexecute.setVdef13(vdef13 == null ? null : vdef13.trim());

          String vdef14 = rs.getString(46);
          saleexecute.setVdef14(vdef14 == null ? null : vdef14.trim());

          String vdef15 = rs.getString(47);
          saleexecute.setVdef15(vdef15 == null ? null : vdef15.trim());

          String vdef16 = rs.getString(48);
          saleexecute.setVdef16(vdef16 == null ? null : vdef16.trim());

          String vdef17 = rs.getString(49);
          saleexecute.setVdef17(vdef17 == null ? null : vdef17.trim());

          String vdef18 = rs.getString(50);
          saleexecute.setVdef18(vdef18 == null ? null : vdef18.trim());

          String vdef19 = rs.getString(51);
          saleexecute.setVdef19(vdef19 == null ? null : vdef19.trim());

          String vdef20 = rs.getString(52);
          saleexecute.setVdef20(vdef20 == null ? null : vdef20.trim());

          String pk_defdoc1 = rs.getString(53);
          saleexecute.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

          String pk_defdoc2 = rs.getString(54);
          saleexecute.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

          String pk_defdoc3 = rs.getString(55);
          saleexecute.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

          String pk_defdoc4 = rs.getString(56);
          saleexecute.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

          String pk_defdoc5 = rs.getString(57);
          saleexecute.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

          String pk_defdoc6 = rs.getString(58);
          saleexecute.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

          String pk_defdoc7 = rs.getString(59);
          saleexecute.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

          String pk_defdoc8 = rs.getString(60);
          saleexecute.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

          String pk_defdoc9 = rs.getString(61);
          saleexecute.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

          String pk_defdoc10 = rs.getString(62);
          saleexecute.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

          String pk_defdoc11 = rs.getString(63);
          saleexecute.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

          String pk_defdoc12 = rs.getString(64);
          saleexecute.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

          String pk_defdoc13 = rs.getString(65);
          saleexecute.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

          String pk_defdoc14 = rs.getString(66);
          saleexecute.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

          String pk_defdoc15 = rs.getString(67);
          saleexecute.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

          String pk_defdoc16 = rs.getString(68);
          saleexecute.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

          String pk_defdoc17 = rs.getString(69);
          saleexecute.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

          String pk_defdoc18 = rs.getString(70);
          saleexecute.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

          String pk_defdoc19 = rs.getString(71);
          saleexecute.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

          String pk_defdoc20 = rs.getString(72);
          saleexecute.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

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
    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryFollowBody", new Object[] { bodys });
  }

  public void queryFollowBody(SaleinvoiceBVO saleexecute)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryFollowBody", new Object[] { saleexecute });

    String sql = "select csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6  , vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20  from so_saleexecute where csale_bid = ?";

    String key = saleexecute.getPrimaryKey();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String csaleid = rs.getString(1);
        saleexecute.setCsaleid(csaleid == null ? null : csaleid.trim());

        String creceipttype = rs.getString(2);

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

        String vdef7 = rs.getString(39);
        saleexecute.setVdef7(vdef7 == null ? null : vdef7.trim());
        String vdef8 = rs.getString(40);
        saleexecute.setVdef8(vdef8 == null ? null : vdef8.trim());
        String vdef9 = rs.getString(41);
        saleexecute.setVdef9(vdef9 == null ? null : vdef9.trim());
        String vdef10 = rs.getString(42);
        saleexecute.setVdef10(vdef10 == null ? null : vdef10.trim());
        String vdef11 = rs.getString(43);
        saleexecute.setVdef11(vdef11 == null ? null : vdef11.trim());
        String vdef12 = rs.getString(44);
        saleexecute.setVdef12(vdef12 == null ? null : vdef12.trim());
        String vdef13 = rs.getString(45);
        saleexecute.setVdef13(vdef13 == null ? null : vdef13.trim());
        String vdef14 = rs.getString(46);
        saleexecute.setVdef14(vdef14 == null ? null : vdef14.trim());
        String vdef15 = rs.getString(47);
        saleexecute.setVdef15(vdef15 == null ? null : vdef15.trim());
        String vdef16 = rs.getString(48);
        saleexecute.setVdef16(vdef16 == null ? null : vdef16.trim());
        String vdef17 = rs.getString(49);
        saleexecute.setVdef17(vdef17 == null ? null : vdef17.trim());
        String vdef18 = rs.getString(50);
        saleexecute.setVdef18(vdef18 == null ? null : vdef18.trim());
        String vdef19 = rs.getString(51);
        saleexecute.setVdef19(vdef19 == null ? null : vdef19.trim());
        String vdef20 = rs.getString(52);
        saleexecute.setVdef20(vdef20 == null ? null : vdef20.trim());
        String pk_defdoc1 = rs.getString(53);
        saleexecute.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(54);
        saleexecute.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(55);
        saleexecute.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(56);
        saleexecute.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(57);
        saleexecute.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(58);
        saleexecute.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(59);
        saleexecute.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(60);
        saleexecute.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(61);
        saleexecute.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(62);
        saleexecute.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(63);
        saleexecute.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(64);
        saleexecute.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(65);
        saleexecute.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(66);
        saleexecute.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(67);
        saleexecute.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(68);
        saleexecute.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(69);
        saleexecute.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(70);
        saleexecute.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(71);
        saleexecute.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(72);
        saleexecute.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.so001.SaleinvoiceDMO", "queryFollowBody", new Object[] { saleexecute });
  }

  public SaleVO[] queryHeadAllData()
    throws SQLException
  {
    try
    {
      String sWhere = " creceipttype = '32' ";

      return (SaleVO[])(SaleVO[])queryAllHeadData(sWhere);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    }
  }

  public SaleVO[] queryHeadAllData(String key)
    throws SQLException
  {
    try
    {
      String where = " creceipttype = '32' and csaleid='" + key + "' ";

      return (SaleVO[])(SaleVO[])queryAllHeadData(where);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    }
  }

  public SaleVO queryHeadData(String key)
    throws SQLException
  {
    try
    {
      String where = " so_saleinvoice_b.csaleid='" + key + "' ";

      SaleVO[] svos = (SaleVO[])(SaleVO[])queryAllHeadData(where);
      if ((svos == null) || (svos.length == 0)) return null;
      return svos[0];
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    }
  }

  public SaleinvoiceBVO[] queryInitBodyData(String key)
    throws SQLException
  {
    return queryBodyData(key);
  }

  public SaleinvoiceVO queryInitData(String strID)
    throws SQLException
  {
    SaleinvoiceVO saleinvoice = new SaleinvoiceVO();
    saleinvoice.setParentVO(queryHeadData(strID));
    saleinvoice.setChildrenVO(queryInitBodyData(strID));

    return saleinvoice;
  }

  public SaleorderHVO[] queryInitOrderData(String custID) throws SQLException
  {
    String strSql = "select csaleid, vdef4, vdef3, vdef2, coperatorid, finvoicetype, vdef1, vdef10, csalecorpid, cemployeeid, dbilldate, ibalanceflag, ctermprotocolid, finvoiceclass, creceiptcorpid, cfreecustid, cbiztype, vreceiveaddress, ndiscountrate, dapprovedate, creceiptcustomerid, ctransmodeid, vnote, vreceiptcode, veditreason, nsubscription, creceipttype, fstatus, binitflag, nevaluatecarriage, vaccountyear, capproveid, dmakedate, cdeptid, ccustomerid, cwarehouseid, ccreditnum, vdef9, vdef8, vdef7, vdef6, vdef5, bfreecustflag, ccalbodyid,ts from so_sale where ccustomerid= '" + custID + "' and creceipttype = '3A' order by csaleid";

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    ResultSet rs = stmt.executeQuery();
    try
    {
      while (rs.next()) {
        SaleorderHVO saleorderH = new SaleorderHVO();
        saleorderH.setPrimaryKey(rs.getString(1));
        String vdef4 = rs.getString("vdef4");
        if (vdef4 != null) {
          saleorderH.setVdef4(vdef4.trim());
        }
        String vdef3 = rs.getString("vdef3");
        if (vdef3 != null) {
          saleorderH.setVdef3(vdef3.trim());
        }
        String vdef2 = rs.getString("vdef2");
        if (vdef2 != null) {
          saleorderH.setVdef2(vdef2.trim());
        }
        String coperatorid = rs.getString("coperatorid");
        if (coperatorid != null) {
          saleorderH.setCoperatorid(coperatorid.trim());
        }
        saleorderH.setFinvoicetype(new Integer(rs.getInt("finvoicetype")));

        String vdef1 = rs.getString("vdef1");
        if (vdef1 != null) {
          saleorderH.setVdef1(vdef1.trim());
        }
        String vdef10 = rs.getString("vdef10");
        if (vdef10 != null) {
          saleorderH.setVdef10(vdef10.trim());
        }
        String csalecorpid = rs.getString("csalecorpid");
        if (csalecorpid != null) {
          saleorderH.setCsalecorpid(csalecorpid.trim());
        }
        String cemployeeid = rs.getString("cemployeeid");
        if (cemployeeid != null) {
          saleorderH.setCemployeeid(cemployeeid.trim());
        }
        String dbilldate = rs.getString("dbilldate");
        if (dbilldate != null) {
          saleorderH.setDbilldate(new UFDate(dbilldate.trim()));
        }

        saleorderH.setIbalanceflag(new Integer(rs.getInt("ibalanceflag")));

        String ctermprotocolid = rs.getString("ctermprotocolid");
        if (ctermprotocolid != null) {
          saleorderH.setCtermprotocolid(ctermprotocolid.trim());
        }
        saleorderH.setFinvoiceclass(new Integer(rs.getInt("finvoiceclass")));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        if (creceiptcorpid != null) {
          saleorderH.setCreceiptcorpid(creceiptcorpid.trim());
        }
        String cfreecustid = rs.getString("cfreecustid");
        if (cfreecustid != null) {
          saleorderH.setCfreecustid(cfreecustid.trim());
        }
        String cbiztype = rs.getString("cbiztype");
        if (cbiztype != null) {
          saleorderH.setCbiztype(cbiztype.trim());
        }
        String vreceiveaddress = rs.getString("vreceiveaddress");
        if (vreceiveaddress != null) {
          saleorderH.setVreceiveaddress(vreceiveaddress.trim());
        }
        Object o = rs.getObject("ndiscountrate");
        if (o != null)
          saleorderH.setNdiscountrate(new UFDouble((BigDecimal)o));
        String dapprovedate = rs.getString("dapprovedate");
        if (dapprovedate != null) {
          saleorderH.setDapprovedate(new UFDate(dapprovedate.trim()));
        }
        String creceiptcustomerid = rs.getString("creceiptcustomerid");
        if (creceiptcustomerid != null) {
          saleorderH.setCreceiptcustomerid(creceiptcustomerid.trim());
        }
        String ctransmodeid = rs.getString("ctransmodeid");
        if (ctransmodeid != null) {
          saleorderH.setCtransmodeid(ctransmodeid.trim());
        }
        String vnote = rs.getString("vnote");
        if (vnote != null) {
          saleorderH.setVnote(vnote.trim());
        }
        String vreceiptcode = rs.getString("vreceiptcode");
        if (vreceiptcode != null) {
          saleorderH.setVreceiptcode(vreceiptcode.trim());
        }
        String veditreason = rs.getString("veditreason");
        if (veditreason != null) {
          saleorderH.setVeditreason(veditreason.trim());
        }
        o = rs.getObject("nsubscription");
        if (o != null)
          saleorderH.setNsubscription(new UFDouble((BigDecimal)o));
        String creceipttype = rs.getString("creceipttype");
        if (creceipttype != null) {
          saleorderH.setCreceipttype(creceipttype.trim());
        }
        saleorderH.setFstatus(new Integer(rs.getInt("fstatus")));
        String binitflag = rs.getString("binitflag");
        if (binitflag != null) {
          saleorderH.setBinitflag(new UFBoolean(binitflag.trim()));
        }
        o = rs.getObject("nevaluatecarriage");
        if (o != null) {
          saleorderH.setNevaluatecarriage(new UFDouble((BigDecimal)o));
        }
        String vaccountyear = rs.getString("vaccountyear");
        if (vaccountyear != null) {
          saleorderH.setVaccountyear(vaccountyear.trim());
        }
        String capproveid = rs.getString("capproveid");
        if (capproveid != null) {
          saleorderH.setCapproveid(capproveid.trim());
        }
        String dmakedate = rs.getString("dmakedate");
        if (dmakedate != null) {
          saleorderH.setDmakedate(new UFDate(dmakedate.trim()));
        }
        String cdeptid = rs.getString("cdeptid");
        if (cdeptid != null) {
          saleorderH.setCdeptid(cdeptid.trim());
        }
        String ccustomerid = rs.getString("ccustomerid");
        if (ccustomerid != null) {
          saleorderH.setCcustomerid(ccustomerid.trim());
        }
        String cwarehouseid = rs.getString("cwarehouseid");
        if (cwarehouseid != null) {
          saleorderH.setCwarehouseid(cwarehouseid.trim());
        }
        String ccreditnum = rs.getString("ccreditnum");
        if (ccreditnum != null) {
          saleorderH.setCcreditnum(ccreditnum.trim());
        }
        String vdef9 = rs.getString("vdef9");
        if (vdef9 != null) {
          saleorderH.setVdef9(vdef9.trim());
        }
        String vdef8 = rs.getString("vdef8");
        if (vdef8 != null) {
          saleorderH.setVdef8(vdef8.trim());
        }
        String vdef7 = rs.getString("vdef7");
        if (vdef7 != null) {
          saleorderH.setVdef7(vdef7.trim());
        }
        String vdef6 = rs.getString("vdef6");
        if (vdef6 != null) {
          saleorderH.setVdef6(vdef6.trim());
        }
        String vdef5 = rs.getString("vdef5");
        if (vdef5 != null) {
          saleorderH.setVdef5(vdef5.trim());
        }
        String bfreecustflag = rs.getString("bfreecustflag");
        if (bfreecustflag != null) {
          saleorderH.setBfreecustflag(new UFBoolean(bfreecustflag.trim()));
        }

        String ccalbodyid = rs.getString("ccalbodyid");
        if (ccalbodyid != null) {
          saleorderH.setCcalbodyid(ccalbodyid.trim());
        }
        String ts = rs.getString("ts");
        if (ts != null) {
          saleorderH.setTs(new UFDateTime(ts));
        }
        v.addElement(saleorderH);
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
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }
    return saleorderHs;
  }

  public SaleorderHVO[] queryOrder(String pk_corp, String userID, String curDate)
    throws SQLException
  {
    String strSql = "select csaleid, vdef4, vdef3, vdef2, coperatorid, finvoicetype, vdef1, vdef10, csalecorpid, cemployeeid, dbilldate, ibalanceflag, ctermprotocolid, finvoiceclass, creceiptcorpid, cfreecustid, cbiztype, vreceiveaddress, ndiscountrate, dapprovedate, creceiptcustomerid, ctransmodeid, vnote, vreceiptcode, veditreason, nsubscription, creceipttype, fstatus, binitflag, nevaluatecarriage, vaccountyear, capproveid, dmakedate, cdeptid, ccustomerid, cwarehouseid, ccreditnum, vdef9, vdef8, vdef7, vdef6, vdef5, bfreecustflag, ccalbodyid,bretinvflag,boutendflag,binvoicendflag,breceiptendflag,fbatchstatus,ts  , vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9 ,pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19 ,pk_defdoc20  ,iprintcount  from so_sale where pk_corp='" + pk_corp + "' and coperatorid='" + userID + "' and dmakedate='" + curDate + "' and creceipttype = '" + "30" + "' and fstatus not in (5) order by csaleid";

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    ResultSet rs = stmt.executeQuery();
    try
    {
      while (rs.next()) {
        SaleorderHVO saleorderH = new SaleorderHVO();
        saleorderH.setPrimaryKey(rs.getString(1));
        String vdef4 = rs.getString("vdef4");
        if (vdef4 != null) {
          saleorderH.setVdef4(vdef4.trim());
        }
        String vdef3 = rs.getString("vdef3");
        if (vdef3 != null) {
          saleorderH.setVdef3(vdef3.trim());
        }
        String vdef2 = rs.getString("vdef2");
        if (vdef2 != null) {
          saleorderH.setVdef2(vdef2.trim());
        }
        String coperatorid = rs.getString("coperatorid");
        if (coperatorid != null) {
          saleorderH.setCoperatorid(coperatorid.trim());
        }
        saleorderH.setFinvoicetype(new Integer(rs.getInt("finvoicetype")));

        String vdef1 = rs.getString("vdef1");
        if (vdef1 != null) {
          saleorderH.setVdef1(vdef1.trim());
        }
        String vdef10 = rs.getString("vdef10");
        if (vdef10 != null) {
          saleorderH.setVdef10(vdef10.trim());
        }
        String csalecorpid = rs.getString("csalecorpid");
        if (csalecorpid != null) {
          saleorderH.setCsalecorpid(csalecorpid.trim());
        }
        String cemployeeid = rs.getString("cemployeeid");
        if (cemployeeid != null) {
          saleorderH.setCemployeeid(cemployeeid.trim());
        }
        String dbilldate = rs.getString("dbilldate");
        if (dbilldate != null) {
          saleorderH.setDbilldate(new UFDate(dbilldate.trim()));
        }

        saleorderH.setIbalanceflag(new Integer(rs.getInt("ibalanceflag")));

        String ctermprotocolid = rs.getString("ctermprotocolid");
        if (ctermprotocolid != null) {
          saleorderH.setCtermprotocolid(ctermprotocolid.trim());
        }
        saleorderH.setFinvoiceclass(new Integer(rs.getInt("finvoiceclass")));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        if (creceiptcorpid != null) {
          saleorderH.setCreceiptcorpid(creceiptcorpid.trim());
        }
        String cfreecustid = rs.getString("cfreecustid");
        if (cfreecustid != null) {
          saleorderH.setCfreecustid(cfreecustid.trim());
        }
        String cbiztype = rs.getString("cbiztype");
        if (cbiztype != null) {
          saleorderH.setCbiztype(cbiztype.trim());
        }
        String vreceiveaddress = rs.getString("vreceiveaddress");
        if (vreceiveaddress != null) {
          saleorderH.setVreceiveaddress(vreceiveaddress.trim());
        }
        Object o = rs.getObject("ndiscountrate");
        if (o != null)
          saleorderH.setNdiscountrate(new UFDouble((BigDecimal)o));
        String dapprovedate = rs.getString("dapprovedate");
        if (dapprovedate != null) {
          saleorderH.setDapprovedate(new UFDate(dapprovedate.trim()));
        }
        String creceiptcustomerid = rs.getString("creceiptcustomerid");
        if (creceiptcustomerid != null) {
          saleorderH.setCreceiptcustomerid(creceiptcustomerid.trim());
        }
        String ctransmodeid = rs.getString("ctransmodeid");
        if (ctransmodeid != null) {
          saleorderH.setCtransmodeid(ctransmodeid.trim());
        }
        String vnote = rs.getString("vnote");
        if (vnote != null) {
          saleorderH.setVnote(vnote.trim());
        }
        String vreceiptcode = rs.getString("vreceiptcode");
        if (vreceiptcode != null) {
          saleorderH.setVreceiptcode(vreceiptcode.trim());
        }
        String veditreason = rs.getString("veditreason");
        if (veditreason != null) {
          saleorderH.setVeditreason(veditreason.trim());
        }
        o = rs.getObject("nsubscription");
        if (o != null)
          saleorderH.setNsubscription(new UFDouble((BigDecimal)o));
        String creceipttype = rs.getString("creceipttype");
        if (creceipttype != null) {
          saleorderH.setCreceipttype(creceipttype.trim());
        }
        saleorderH.setFstatus(new Integer(rs.getInt("fstatus")));
        String binitflag = rs.getString("binitflag");
        if (binitflag != null) {
          saleorderH.setBinitflag(new UFBoolean(binitflag.trim()));
        }
        o = rs.getObject("nevaluatecarriage");
        if (o != null) {
          saleorderH.setNevaluatecarriage(new UFDouble((BigDecimal)o));
        }
        String vaccountyear = rs.getString("vaccountyear");
        if (vaccountyear != null) {
          saleorderH.setVaccountyear(vaccountyear.trim());
        }
        String capproveid = rs.getString("capproveid");
        if (capproveid != null) {
          saleorderH.setCapproveid(capproveid.trim());
        }
        String dmakedate = rs.getString("dmakedate");
        if (dmakedate != null) {
          saleorderH.setDmakedate(new UFDate(dmakedate.trim()));
        }
        String cdeptid = rs.getString("cdeptid");
        if (cdeptid != null) {
          saleorderH.setCdeptid(cdeptid.trim());
        }
        String ccustomerid = rs.getString("ccustomerid");
        if (ccustomerid != null) {
          saleorderH.setCcustomerid(ccustomerid.trim());
        }
        String cwarehouseid = rs.getString("cwarehouseid");
        if (cwarehouseid != null) {
          saleorderH.setCwarehouseid(cwarehouseid.trim());
        }
        String ccreditnum = rs.getString("ccreditnum");
        if (ccreditnum != null) {
          saleorderH.setCcreditnum(ccreditnum.trim());
        }
        String vdef9 = rs.getString("vdef9");
        if (vdef9 != null) {
          saleorderH.setVdef9(vdef9.trim());
        }
        String vdef8 = rs.getString("vdef8");
        if (vdef8 != null) {
          saleorderH.setVdef8(vdef8.trim());
        }
        String vdef7 = rs.getString("vdef7");
        if (vdef7 != null) {
          saleorderH.setVdef7(vdef7.trim());
        }
        String vdef6 = rs.getString("vdef6");
        if (vdef6 != null) {
          saleorderH.setVdef6(vdef6.trim());
        }
        String vdef5 = rs.getString("vdef5");
        if (vdef5 != null) {
          saleorderH.setVdef5(vdef5.trim());
        }
        String bfreecustflag = rs.getString("bfreecustflag");
        if (bfreecustflag != null) {
          saleorderH.setBfreecustflag(new UFBoolean(bfreecustflag.trim()));
        }

        String ccalbodyid = rs.getString("ccalbodyid");
        if (ccalbodyid != null) {
          saleorderH.setCcalbodyid(ccalbodyid.trim());
        }

        String bretinvflag = rs.getString("bretinvflag");
        if (bretinvflag != null) {
          saleorderH.setBretinvflag(new UFBoolean(bretinvflag.trim()));
        }

        String boutendflag = rs.getString("boutendflag");
        if (boutendflag != null) {
          saleorderH.setBoutendflag(new UFBoolean(boutendflag.trim()));
        }

        String binvoicendflag = rs.getString("binvoicendflag");
        if (binvoicendflag != null) {
          saleorderH.setBinvoicendflag(new UFBoolean(binvoicendflag.trim()));
        }

        String breceiptendflag = rs.getString("breceiptendflag");
        if (breceiptendflag != null) {
          saleorderH.setBreceiptendflag(new UFBoolean(breceiptendflag.trim()));
        }

        String ts = rs.getString("ts");
        if (ts != null) {
          saleorderH.setTs(new UFDateTime(ts));
        }

        String vdef11 = rs.getString("vdef11");
        if (vdef11 != null) {
          saleorderH.setVdef11(vdef11.trim());
        }
        String vdef12 = rs.getString("vdef12");
        if (vdef12 != null) {
          saleorderH.setVdef12(vdef12.trim());
        }
        String vdef13 = rs.getString("vdef13");
        if (vdef13 != null) {
          saleorderH.setVdef13(vdef13.trim());
        }
        String vdef14 = rs.getString("vdef14");
        if (vdef14 != null) {
          saleorderH.setVdef14(vdef14.trim());
        }
        String vdef15 = rs.getString("vdef15");
        if (vdef15 != null) {
          saleorderH.setVdef15(vdef15.trim());
        }
        String vdef16 = rs.getString("vdef16");
        if (vdef16 != null) {
          saleorderH.setVdef16(vdef16.trim());
        }
        String vdef17 = rs.getString("vdef17");
        if (vdef17 != null) {
          saleorderH.setVdef17(vdef17.trim());
        }
        String vdef18 = rs.getString("vdef18");
        if (vdef18 != null) {
          saleorderH.setVdef18(vdef18.trim());
        }
        String vdef19 = rs.getString("vdef19");
        if (vdef19 != null) {
          saleorderH.setVdef19(vdef19.trim());
        }
        String vdef20 = rs.getString("vdef20");
        if (vdef20 != null) {
          saleorderH.setVdef20(vdef20.trim());
        }
        String pk_defdoc1 = rs.getString("pk_defdoc1");
        if (pk_defdoc1 != null) {
          saleorderH.setPk_defdoc1(pk_defdoc1.trim());
        }
        String pk_defdoc2 = rs.getString("pk_defdoc2");
        if (pk_defdoc2 != null) {
          saleorderH.setPk_defdoc2(pk_defdoc2.trim());
        }
        String pk_defdoc3 = rs.getString("pk_defdoc3");
        if (pk_defdoc3 != null) {
          saleorderH.setPk_defdoc3(pk_defdoc3.trim());
        }
        String pk_defdoc4 = rs.getString("pk_defdoc4");
        if (pk_defdoc4 != null) {
          saleorderH.setPk_defdoc4(pk_defdoc4.trim());
        }
        String pk_defdoc5 = rs.getString("pk_defdoc5");
        if (pk_defdoc5 != null) {
          saleorderH.setPk_defdoc5(pk_defdoc5.trim());
        }
        String pk_defdoc6 = rs.getString("pk_defdoc6");
        if (pk_defdoc6 != null) {
          saleorderH.setPk_defdoc6(pk_defdoc6.trim());
        }
        String pk_defdoc7 = rs.getString("pk_defdoc7");
        if (pk_defdoc7 != null) {
          saleorderH.setPk_defdoc7(pk_defdoc7.trim());
        }
        String pk_defdoc8 = rs.getString("pk_defdoc8");
        if (pk_defdoc8 != null) {
          saleorderH.setPk_defdoc8(pk_defdoc8.trim());
        }
        String pk_defdoc9 = rs.getString("pk_defdoc9");
        if (pk_defdoc9 != null) {
          saleorderH.setPk_defdoc9(pk_defdoc9.trim());
        }
        String pk_defdoc10 = rs.getString("pk_defdoc10");
        if (pk_defdoc10 != null) {
          saleorderH.setPk_defdoc10(pk_defdoc10.trim());
        }
        String pk_defdoc11 = rs.getString("pk_defdoc11");
        if (pk_defdoc11 != null) {
          saleorderH.setPk_defdoc11(pk_defdoc11.trim());
        }
        String pk_defdoc12 = rs.getString("pk_defdoc12");
        if (pk_defdoc12 != null) {
          saleorderH.setPk_defdoc12(pk_defdoc12.trim());
        }
        String pk_defdoc13 = rs.getString("pk_defdoc13");
        if (pk_defdoc13 != null) {
          saleorderH.setPk_defdoc13(pk_defdoc13.trim());
        }
        String pk_defdoc14 = rs.getString("pk_defdoc14");
        if (pk_defdoc14 != null) {
          saleorderH.setPk_defdoc14(pk_defdoc14.trim());
        }
        String pk_defdoc15 = rs.getString("pk_defdoc15");
        if (pk_defdoc15 != null) {
          saleorderH.setPk_defdoc15(pk_defdoc15.trim());
        }
        String pk_defdoc16 = rs.getString("pk_defdoc16");
        if (pk_defdoc16 != null) {
          saleorderH.setPk_defdoc16(pk_defdoc16.trim());
        }
        String pk_defdoc17 = rs.getString("pk_defdoc17");
        if (pk_defdoc17 != null) {
          saleorderH.setPk_defdoc17(pk_defdoc17.trim());
        }
        String pk_defdoc18 = rs.getString("pk_defdoc18");
        if (pk_defdoc18 != null) {
          saleorderH.setPk_defdoc18(pk_defdoc18.trim());
        }
        String pk_defdoc19 = rs.getString("pk_defdoc19");
        if (pk_defdoc19 != null) {
          saleorderH.setPk_defdoc19(pk_defdoc19.trim());
        }
        String pk_defdoc20 = rs.getString("pk_defdoc20");
        if (pk_defdoc20 != null) {
          saleorderH.setPk_defdoc20(pk_defdoc20.trim());
        }

        Object iprintcount = rs.getObject(83);
        saleorderH.setIprintcount((iprintcount == null) || (iprintcount.toString().length() <= 0) ? null : new Integer(iprintcount.toString()));

        v.addElement(saleorderH);
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
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }
    return saleorderHs;
  }

  public SaleorderBVO[] queryOrderBodyList(String id)
    throws SQLException
  {
    String strSql = "select a.corder_bid, a.csaleid, a.pk_corp, a.creceipttype, a.csourcebillid, a.csourcebillbodyid, a.cinventoryid, a.cunitid, a.cpackunitid, a.nnumber, a.npacknumber, a.cbodywarehouseid, a.dconsigndate, a.ddeliverdate, a.blargessflag, a.ceditsaleid, a.beditflag, a.veditreason, a.ccurrencytypeid, a.nitemdiscountrate, a.ndiscountrate, a.nexchangeotobrate, a.nexchangeotoarate, a.ntaxrate, a.noriginalcurprice, a.noriginalcurtaxprice, a.noriginalcurnetprice, a.noriginalcurtaxnetprice, a.noriginalcurtaxmny, a.noriginalcurmny, a.noriginalcursummny, a.noriginalcurdiscountmny, a.nprice, a.ntaxprice, a.nnetprice, a.ntaxnetprice, a.ntaxmny, a.nmny, a.nsummny, a.ndiscountmny, a.coperatorid, a.frowstatus, a.frownote,a.ts from so_saleorder_b a left outer join so_saleexecute b on b.csaleid=a.csaleid and b.csale_bid=a.corder_bid where a.csaleid='" + id + "' order by a.csaleid,a.corder_bid";

    SaleorderBVO[] saleorderBs = null;
    Vector v = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    try {
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString("ceditsaleid");
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString("beditflag");
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString("veditreason");
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString("coperatorid");
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts));

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
      catch (Exception e)
      {
      }
    }
    if (v.size() > 0) {
      saleorderBs = new SaleorderBVO[v.size()];
      v.copyInto(saleorderBs);
    }

    return saleorderBs;
  }

  public SaleorderHVO[] queryOrderData(String custID) throws SQLException
  {
    String strSql = "select csaleid, vdef4, vdef3, vdef2, coperatorid, finvoicetype, vdef1, vdef10, csalecorpid, cemployeeid, dbilldate, ibalanceflag, ctermprotocolid, finvoiceclass, creceiptcorpid, cfreecustid, cbiztype, vreceiveaddress, ndiscountrate, dapprovedate, creceiptcustomerid, ctransmodeid, vnote, vreceiptcode, veditreason, nsubscription, creceipttype, fstatus, binitflag, nevaluatecarriage, vaccountyear, capproveid, dmakedate, cdeptid, ccustomerid, cwarehouseid, ccreditnum, vdef9, vdef8, vdef7, vdef6, vdef5, bfreecustflag, ccalbodyid, bretinvflag,boutendflag,binvoicendflag,breceiptendflag,fbatchstatus,ts  , vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9 ,pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19 ,pk_defdoc20  , iprintcount  from so_sale where creceipttype = '30' and fstatus=2 order by csaleid";

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    ResultSet rs = stmt.executeQuery();
    try
    {
      while (rs.next()) {
        SaleorderHVO saleorderH = new SaleorderHVO();
        saleorderH.setPrimaryKey(rs.getString(1));
        String vdef4 = rs.getString("vdef4");
        if (vdef4 != null) {
          saleorderH.setVdef4(vdef4.trim());
        }
        String vdef3 = rs.getString("vdef3");
        if (vdef3 != null) {
          saleorderH.setVdef3(vdef3.trim());
        }
        String vdef2 = rs.getString("vdef2");
        if (vdef2 != null) {
          saleorderH.setVdef2(vdef2.trim());
        }
        String coperatorid = rs.getString("coperatorid");
        if (coperatorid != null) {
          saleorderH.setCoperatorid(coperatorid.trim());
        }
        saleorderH.setFinvoicetype(new Integer(rs.getInt("finvoicetype")));

        String vdef1 = rs.getString("vdef1");
        if (vdef1 != null) {
          saleorderH.setVdef1(vdef1.trim());
        }
        String vdef10 = rs.getString("vdef10");
        if (vdef10 != null) {
          saleorderH.setVdef10(vdef10.trim());
        }
        String csalecorpid = rs.getString("csalecorpid");
        if (csalecorpid != null) {
          saleorderH.setCsalecorpid(csalecorpid.trim());
        }
        String cemployeeid = rs.getString("cemployeeid");
        if (cemployeeid != null) {
          saleorderH.setCemployeeid(cemployeeid.trim());
        }
        String dbilldate = rs.getString("dbilldate");
        if (dbilldate != null) {
          saleorderH.setDbilldate(new UFDate(dbilldate.trim()));
        }

        saleorderH.setIbalanceflag(new Integer(rs.getInt("ibalanceflag")));

        String ctermprotocolid = rs.getString("ctermprotocolid");
        if (ctermprotocolid != null) {
          saleorderH.setCtermprotocolid(ctermprotocolid.trim());
        }
        saleorderH.setFinvoiceclass(new Integer(rs.getInt("finvoiceclass")));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        if (creceiptcorpid != null) {
          saleorderH.setCreceiptcorpid(creceiptcorpid.trim());
        }
        String cfreecustid = rs.getString("cfreecustid");
        if (cfreecustid != null) {
          saleorderH.setCfreecustid(cfreecustid.trim());
        }
        String cbiztype = rs.getString("cbiztype");
        if (cbiztype != null) {
          saleorderH.setCbiztype(cbiztype.trim());
        }
        String vreceiveaddress = rs.getString("vreceiveaddress");
        if (vreceiveaddress != null) {
          saleorderH.setVreceiveaddress(vreceiveaddress.trim());
        }
        Object o = rs.getObject("ndiscountrate");
        if (o != null)
          saleorderH.setNdiscountrate(new UFDouble((BigDecimal)o));
        String dapprovedate = rs.getString("dapprovedate");
        if (dapprovedate != null) {
          saleorderH.setDapprovedate(new UFDate(dapprovedate.trim()));
        }
        String creceiptcustomerid = rs.getString("creceiptcustomerid");
        if (creceiptcustomerid != null) {
          saleorderH.setCreceiptcustomerid(creceiptcustomerid.trim());
        }
        String ctransmodeid = rs.getString("ctransmodeid");
        if (ctransmodeid != null) {
          saleorderH.setCtransmodeid(ctransmodeid.trim());
        }
        String vnote = rs.getString("vnote");
        if (vnote != null) {
          saleorderH.setVnote(vnote.trim());
        }
        String vreceiptcode = rs.getString("vreceiptcode");
        if (vreceiptcode != null) {
          saleorderH.setVreceiptcode(vreceiptcode.trim());
        }
        String veditreason = rs.getString("veditreason");
        if (veditreason != null) {
          saleorderH.setVeditreason(veditreason.trim());
        }
        o = rs.getObject("nsubscription");
        if (o != null)
          saleorderH.setNsubscription(new UFDouble((BigDecimal)o));
        String creceipttype = rs.getString("creceipttype");
        if (creceipttype != null) {
          saleorderH.setCreceipttype(creceipttype.trim());
        }
        saleorderH.setFstatus(new Integer(rs.getInt("fstatus")));
        String binitflag = rs.getString("binitflag");
        if (binitflag != null) {
          saleorderH.setBinitflag(new UFBoolean(binitflag.trim()));
        }
        o = rs.getObject("nevaluatecarriage");
        if (o != null) {
          saleorderH.setNevaluatecarriage(new UFDouble((BigDecimal)o));
        }
        String vaccountyear = rs.getString("vaccountyear");
        if (vaccountyear != null) {
          saleorderH.setVaccountyear(vaccountyear.trim());
        }
        String capproveid = rs.getString("capproveid");
        if (capproveid != null) {
          saleorderH.setCapproveid(capproveid.trim());
        }
        String dmakedate = rs.getString("dmakedate");
        if (dmakedate != null) {
          saleorderH.setDmakedate(new UFDate(dmakedate.trim()));
        }
        String cdeptid = rs.getString("cdeptid");
        if (cdeptid != null) {
          saleorderH.setCdeptid(cdeptid.trim());
        }
        String ccustomerid = rs.getString("ccustomerid");
        if (ccustomerid != null) {
          saleorderH.setCcustomerid(ccustomerid.trim());
        }
        String cwarehouseid = rs.getString("cwarehouseid");
        if (cwarehouseid != null) {
          saleorderH.setCwarehouseid(cwarehouseid.trim());
        }
        String ccreditnum = rs.getString("ccreditnum");
        if (ccreditnum != null) {
          saleorderH.setCcreditnum(ccreditnum.trim());
        }
        String vdef9 = rs.getString("vdef9");
        if (vdef9 != null) {
          saleorderH.setVdef9(vdef9.trim());
        }
        String vdef8 = rs.getString("vdef8");
        if (vdef8 != null) {
          saleorderH.setVdef8(vdef8.trim());
        }
        String vdef7 = rs.getString("vdef7");
        if (vdef7 != null) {
          saleorderH.setVdef7(vdef7.trim());
        }
        String vdef6 = rs.getString("vdef6");
        if (vdef6 != null) {
          saleorderH.setVdef6(vdef6.trim());
        }
        String vdef5 = rs.getString("vdef5");
        if (vdef5 != null) {
          saleorderH.setVdef5(vdef5.trim());
        }
        String bfreecustflag = rs.getString("bfreecustflag");
        if (bfreecustflag != null) {
          saleorderH.setBfreecustflag(new UFBoolean(bfreecustflag.trim()));
        }

        String ccalbodyid = rs.getString("ccalbodyid");
        if (ccalbodyid != null) {
          saleorderH.setCcalbodyid(ccalbodyid.trim());
        }

        String bretinvflag = rs.getString("bretinvflag");
        if (bretinvflag != null) {
          saleorderH.setBretinvflag(new UFBoolean(bretinvflag.trim()));
        }

        String boutendflag = rs.getString("boutendflag");
        if (boutendflag != null) {
          saleorderH.setBoutendflag(new UFBoolean(boutendflag.trim()));
        }

        String binvoicendflag = rs.getString("binvoicendflag");
        if (binvoicendflag != null) {
          saleorderH.setBinvoicendflag(new UFBoolean(binvoicendflag.trim()));
        }

        String breceiptendflag = rs.getString("breceiptendflag");
        if (breceiptendflag != null) {
          saleorderH.setBreceiptendflag(new UFBoolean(breceiptendflag.trim()));
        }

        String ts = rs.getString("ts");
        if (ts != null) {
          saleorderH.setTs(new UFDateTime(ts));
        }

        String vdef11 = rs.getString("vdef11");
        if (vdef11 != null) {
          saleorderH.setVdef11(vdef11.trim());
        }
        String vdef12 = rs.getString("vdef12");
        if (vdef12 != null) {
          saleorderH.setVdef12(vdef12.trim());
        }
        String vdef13 = rs.getString("vdef13");
        if (vdef13 != null) {
          saleorderH.setVdef13(vdef13.trim());
        }
        String vdef14 = rs.getString("vdef14");
        if (vdef14 != null) {
          saleorderH.setVdef14(vdef14.trim());
        }
        String vdef15 = rs.getString("vdef15");
        if (vdef15 != null) {
          saleorderH.setVdef15(vdef15.trim());
        }
        String vdef16 = rs.getString("vdef16");
        if (vdef16 != null) {
          saleorderH.setVdef16(vdef16.trim());
        }
        String vdef17 = rs.getString("vdef17");
        if (vdef17 != null) {
          saleorderH.setVdef17(vdef17.trim());
        }
        String vdef18 = rs.getString("vdef18");
        if (vdef18 != null) {
          saleorderH.setVdef18(vdef18.trim());
        }
        String vdef19 = rs.getString("vdef19");
        if (vdef19 != null) {
          saleorderH.setVdef19(vdef19.trim());
        }
        String vdef20 = rs.getString("vdef20");
        if (vdef20 != null) {
          saleorderH.setVdef20(vdef20.trim());
        }
        String pk_defdoc1 = rs.getString("pk_defdoc1");
        if (pk_defdoc1 != null) {
          saleorderH.setPk_defdoc1(pk_defdoc1.trim());
        }
        String pk_defdoc2 = rs.getString("pk_defdoc2");
        if (pk_defdoc2 != null) {
          saleorderH.setPk_defdoc2(pk_defdoc2.trim());
        }
        String pk_defdoc3 = rs.getString("pk_defdoc3");
        if (pk_defdoc3 != null) {
          saleorderH.setPk_defdoc3(pk_defdoc3.trim());
        }
        String pk_defdoc4 = rs.getString("pk_defdoc4");
        if (pk_defdoc4 != null) {
          saleorderH.setPk_defdoc4(pk_defdoc4.trim());
        }
        String pk_defdoc5 = rs.getString("pk_defdoc5");
        if (pk_defdoc5 != null) {
          saleorderH.setPk_defdoc5(pk_defdoc5.trim());
        }
        String pk_defdoc6 = rs.getString("pk_defdoc6");
        if (pk_defdoc6 != null) {
          saleorderH.setPk_defdoc6(pk_defdoc6.trim());
        }
        String pk_defdoc7 = rs.getString("pk_defdoc7");
        if (pk_defdoc7 != null) {
          saleorderH.setPk_defdoc7(pk_defdoc7.trim());
        }
        String pk_defdoc8 = rs.getString("pk_defdoc8");
        if (pk_defdoc8 != null) {
          saleorderH.setPk_defdoc8(pk_defdoc8.trim());
        }
        String pk_defdoc9 = rs.getString("pk_defdoc9");
        if (pk_defdoc9 != null) {
          saleorderH.setPk_defdoc9(pk_defdoc9.trim());
        }
        String pk_defdoc10 = rs.getString("pk_defdoc10");
        if (pk_defdoc10 != null) {
          saleorderH.setPk_defdoc10(pk_defdoc10.trim());
        }
        String pk_defdoc11 = rs.getString("pk_defdoc11");
        if (pk_defdoc11 != null) {
          saleorderH.setPk_defdoc11(pk_defdoc11.trim());
        }
        String pk_defdoc12 = rs.getString("pk_defdoc12");
        if (pk_defdoc12 != null) {
          saleorderH.setPk_defdoc12(pk_defdoc12.trim());
        }
        String pk_defdoc13 = rs.getString("pk_defdoc13");
        if (pk_defdoc13 != null) {
          saleorderH.setPk_defdoc13(pk_defdoc13.trim());
        }
        String pk_defdoc14 = rs.getString("pk_defdoc14");
        if (pk_defdoc14 != null) {
          saleorderH.setPk_defdoc14(pk_defdoc14.trim());
        }
        String pk_defdoc15 = rs.getString("pk_defdoc15");
        if (pk_defdoc15 != null) {
          saleorderH.setPk_defdoc15(pk_defdoc15.trim());
        }
        String pk_defdoc16 = rs.getString("pk_defdoc16");
        if (pk_defdoc16 != null) {
          saleorderH.setPk_defdoc16(pk_defdoc16.trim());
        }
        String pk_defdoc17 = rs.getString("pk_defdoc17");
        if (pk_defdoc17 != null) {
          saleorderH.setPk_defdoc17(pk_defdoc17.trim());
        }
        String pk_defdoc18 = rs.getString("pk_defdoc18");
        if (pk_defdoc18 != null) {
          saleorderH.setPk_defdoc18(pk_defdoc18.trim());
        }
        String pk_defdoc19 = rs.getString("pk_defdoc19");
        if (pk_defdoc19 != null) {
          saleorderH.setPk_defdoc19(pk_defdoc19.trim());
        }
        String pk_defdoc20 = rs.getString("pk_defdoc20");
        if (pk_defdoc20 != null) {
          saleorderH.setPk_defdoc20(pk_defdoc20.trim());
        }

        Object iprintcount = rs.getObject(83);
        saleorderH.setIprintcount((iprintcount == null) || (iprintcount.toString().length() <= 0) ? null : new Integer(iprintcount.toString()));

        v.addElement(saleorderH);
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
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }
    return saleorderHs;
  }

  public SaleorderHVO[] queryOrderList(String pk_corp)
    throws SQLException
  {
    String strSql = "select csaleid, vdef4, vdef3, vdef2, coperatorid, finvoicetype, vdef1, vdef10, csalecorpid, cemployeeid, dbilldate, ibalanceflag, ctermprotocolid, finvoiceclass, creceiptcorpid, cfreecustid, cbiztype, vreceiveaddress, ndiscountrate, dapprovedate, creceiptcustomerid, ctransmodeid, vnote, vreceiptcode, veditreason, nsubscription, creceipttype, fstatus, binitflag, nevaluatecarriage, vaccountyear, capproveid, dmakedate, cdeptid, ccustomerid, cwarehouseid, ccreditnum, vdef9, vdef8, vdef7, vdef6, vdef5, bfreecustflag, ccalbodyid,ts  , vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20  , pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9 ,pk_defdoc10  , pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19 ,pk_defdoc20  ,iprintcount  from so_sale where pk_corp='" + pk_corp + "' and creceipttype = '" + "30" + "' and fstatus=2 order by csaleid";

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    ResultSet rs = stmt.executeQuery();
    try
    {
      while (rs.next()) {
        SaleorderHVO saleorderH = new SaleorderHVO();
        saleorderH.setPrimaryKey(rs.getString(1));
        String vdef4 = rs.getString("vdef4");
        if (vdef4 != null) {
          saleorderH.setVdef4(vdef4.trim());
        }
        String vdef3 = rs.getString("vdef3");
        if (vdef3 != null) {
          saleorderH.setVdef3(vdef3.trim());
        }
        String vdef2 = rs.getString("vdef2");
        if (vdef2 != null) {
          saleorderH.setVdef2(vdef2.trim());
        }
        String coperatorid = rs.getString("coperatorid");
        if (coperatorid != null) {
          saleorderH.setCoperatorid(coperatorid.trim());
        }
        saleorderH.setFinvoicetype(new Integer(rs.getInt("finvoicetype")));

        String vdef1 = rs.getString("vdef1");
        if (vdef1 != null) {
          saleorderH.setVdef1(vdef1.trim());
        }
        String vdef10 = rs.getString("vdef10");
        if (vdef10 != null) {
          saleorderH.setVdef10(vdef10.trim());
        }
        String csalecorpid = rs.getString("csalecorpid");
        if (csalecorpid != null) {
          saleorderH.setCsalecorpid(csalecorpid.trim());
        }
        String cemployeeid = rs.getString("cemployeeid");
        if (cemployeeid != null) {
          saleorderH.setCemployeeid(cemployeeid.trim());
        }
        String dbilldate = rs.getString("dbilldate");
        if (dbilldate != null) {
          saleorderH.setDbilldate(new UFDate(dbilldate.trim()));
        }

        saleorderH.setIbalanceflag(new Integer(rs.getInt("ibalanceflag")));

        String ctermprotocolid = rs.getString("ctermprotocolid");
        if (ctermprotocolid != null) {
          saleorderH.setCtermprotocolid(ctermprotocolid.trim());
        }
        saleorderH.setFinvoiceclass(new Integer(rs.getInt("finvoiceclass")));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        if (creceiptcorpid != null) {
          saleorderH.setCreceiptcorpid(creceiptcorpid.trim());
        }
        String cfreecustid = rs.getString("cfreecustid");
        if (cfreecustid != null) {
          saleorderH.setCfreecustid(cfreecustid.trim());
        }
        String cbiztype = rs.getString("cbiztype");
        if (cbiztype != null) {
          saleorderH.setCbiztype(cbiztype.trim());
        }
        String vreceiveaddress = rs.getString("vreceiveaddress");
        if (vreceiveaddress != null) {
          saleorderH.setVreceiveaddress(vreceiveaddress.trim());
        }
        Object o = rs.getObject("ndiscountrate");
        if (o != null)
          saleorderH.setNdiscountrate(new UFDouble((BigDecimal)o));
        String dapprovedate = rs.getString("dapprovedate");
        if (dapprovedate != null) {
          saleorderH.setDapprovedate(new UFDate(dapprovedate.trim()));
        }
        String creceiptcustomerid = rs.getString("creceiptcustomerid");
        if (creceiptcustomerid != null) {
          saleorderH.setCreceiptcustomerid(creceiptcustomerid.trim());
        }
        String ctransmodeid = rs.getString("ctransmodeid");
        if (ctransmodeid != null) {
          saleorderH.setCtransmodeid(ctransmodeid.trim());
        }
        String vnote = rs.getString("vnote");
        if (vnote != null) {
          saleorderH.setVnote(vnote.trim());
        }
        String vreceiptcode = rs.getString("vreceiptcode");
        if (vreceiptcode != null) {
          saleorderH.setVreceiptcode(vreceiptcode.trim());
        }
        String veditreason = rs.getString("veditreason");
        if (veditreason != null) {
          saleorderH.setVeditreason(veditreason.trim());
        }
        o = rs.getObject("nsubscription");
        if (o != null)
          saleorderH.setNsubscription(new UFDouble((BigDecimal)o));
        String creceipttype = rs.getString("creceipttype");
        if (creceipttype != null) {
          saleorderH.setCreceipttype(creceipttype.trim());
        }
        saleorderH.setFstatus(new Integer(rs.getInt("fstatus")));
        String binitflag = rs.getString("binitflag");
        if (binitflag != null) {
          saleorderH.setBinitflag(new UFBoolean(binitflag.trim()));
        }
        o = rs.getObject("nevaluatecarriage");
        if (o != null) {
          saleorderH.setNevaluatecarriage(new UFDouble((BigDecimal)o));
        }
        String vaccountyear = rs.getString("vaccountyear");
        if (vaccountyear != null) {
          saleorderH.setVaccountyear(vaccountyear.trim());
        }
        String capproveid = rs.getString("capproveid");
        if (capproveid != null) {
          saleorderH.setCapproveid(capproveid.trim());
        }
        String dmakedate = rs.getString("dmakedate");
        if (dmakedate != null) {
          saleorderH.setDmakedate(new UFDate(dmakedate.trim()));
        }
        String cdeptid = rs.getString("cdeptid");
        if (cdeptid != null) {
          saleorderH.setCdeptid(cdeptid.trim());
        }
        String ccustomerid = rs.getString("ccustomerid");
        if (ccustomerid != null) {
          saleorderH.setCcustomerid(ccustomerid.trim());
        }
        String cwarehouseid = rs.getString("cwarehouseid");
        if (cwarehouseid != null) {
          saleorderH.setCwarehouseid(cwarehouseid.trim());
        }
        String ccreditnum = rs.getString("ccreditnum");
        if (ccreditnum != null) {
          saleorderH.setCcreditnum(ccreditnum.trim());
        }
        String vdef9 = rs.getString("vdef9");
        if (vdef9 != null) {
          saleorderH.setVdef9(vdef9.trim());
        }
        String vdef8 = rs.getString("vdef8");
        if (vdef8 != null) {
          saleorderH.setVdef8(vdef8.trim());
        }
        String vdef7 = rs.getString("vdef7");
        if (vdef7 != null) {
          saleorderH.setVdef7(vdef7.trim());
        }
        String vdef6 = rs.getString("vdef6");
        if (vdef6 != null) {
          saleorderH.setVdef6(vdef6.trim());
        }
        String vdef5 = rs.getString("vdef5");
        if (vdef5 != null) {
          saleorderH.setVdef5(vdef5.trim());
        }
        String bfreecustflag = rs.getString("bfreecustflag");
        if (bfreecustflag != null) {
          saleorderH.setBfreecustflag(new UFBoolean(bfreecustflag.trim()));
        }

        String ccalbodyid = rs.getString("ccalbodyid");
        if (ccalbodyid != null) {
          saleorderH.setCcalbodyid(ccalbodyid.trim());
        }

        String ts = rs.getString("ts");
        if (ts != null) {
          saleorderH.setTs(new UFDateTime(ts));
        }

        String vdef11 = rs.getString("vdef11");
        if (vdef11 != null) {
          saleorderH.setVdef11(vdef11.trim());
        }
        String vdef12 = rs.getString("vdef12");
        if (vdef12 != null) {
          saleorderH.setVdef12(vdef12.trim());
        }
        String vdef13 = rs.getString("vdef13");
        if (vdef13 != null) {
          saleorderH.setVdef13(vdef13.trim());
        }
        String vdef14 = rs.getString("vdef14");
        if (vdef14 != null) {
          saleorderH.setVdef14(vdef14.trim());
        }
        String vdef15 = rs.getString("vdef15");
        if (vdef15 != null) {
          saleorderH.setVdef15(vdef15.trim());
        }
        String vdef16 = rs.getString("vdef16");
        if (vdef16 != null) {
          saleorderH.setVdef16(vdef16.trim());
        }
        String vdef17 = rs.getString("vdef17");
        if (vdef17 != null) {
          saleorderH.setVdef17(vdef17.trim());
        }
        String vdef18 = rs.getString("vdef18");
        if (vdef18 != null) {
          saleorderH.setVdef18(vdef18.trim());
        }
        String vdef19 = rs.getString("vdef19");
        if (vdef19 != null) {
          saleorderH.setVdef19(vdef19.trim());
        }
        String vdef20 = rs.getString("vdef20");
        if (vdef20 != null) {
          saleorderH.setVdef20(vdef20.trim());
        }
        String pk_defdoc1 = rs.getString("pk_defdoc1");
        if (pk_defdoc1 != null) {
          saleorderH.setPk_defdoc1(pk_defdoc1.trim());
        }
        String pk_defdoc2 = rs.getString("pk_defdoc2");
        if (pk_defdoc2 != null) {
          saleorderH.setPk_defdoc2(pk_defdoc2.trim());
        }
        String pk_defdoc3 = rs.getString("pk_defdoc3");
        if (pk_defdoc3 != null) {
          saleorderH.setPk_defdoc3(pk_defdoc3.trim());
        }
        String pk_defdoc4 = rs.getString("pk_defdoc4");
        if (pk_defdoc4 != null) {
          saleorderH.setPk_defdoc4(pk_defdoc4.trim());
        }
        String pk_defdoc5 = rs.getString("pk_defdoc5");
        if (pk_defdoc5 != null) {
          saleorderH.setPk_defdoc5(pk_defdoc5.trim());
        }
        String pk_defdoc6 = rs.getString("pk_defdoc6");
        if (pk_defdoc6 != null) {
          saleorderH.setPk_defdoc6(pk_defdoc6.trim());
        }
        String pk_defdoc7 = rs.getString("pk_defdoc7");
        if (pk_defdoc7 != null) {
          saleorderH.setPk_defdoc7(pk_defdoc7.trim());
        }
        String pk_defdoc8 = rs.getString("pk_defdoc8");
        if (pk_defdoc8 != null) {
          saleorderH.setPk_defdoc8(pk_defdoc8.trim());
        }
        String pk_defdoc9 = rs.getString("pk_defdoc9");
        if (pk_defdoc9 != null) {
          saleorderH.setPk_defdoc9(pk_defdoc9.trim());
        }
        String pk_defdoc10 = rs.getString("pk_defdoc10");
        if (pk_defdoc10 != null) {
          saleorderH.setPk_defdoc10(pk_defdoc10.trim());
        }
        String pk_defdoc11 = rs.getString("pk_defdoc11");
        if (pk_defdoc11 != null) {
          saleorderH.setPk_defdoc11(pk_defdoc11.trim());
        }
        String pk_defdoc12 = rs.getString("pk_defdoc12");
        if (pk_defdoc12 != null) {
          saleorderH.setPk_defdoc12(pk_defdoc12.trim());
        }
        String pk_defdoc13 = rs.getString("pk_defdoc13");
        if (pk_defdoc13 != null) {
          saleorderH.setPk_defdoc13(pk_defdoc13.trim());
        }
        String pk_defdoc14 = rs.getString("pk_defdoc14");
        if (pk_defdoc14 != null) {
          saleorderH.setPk_defdoc14(pk_defdoc14.trim());
        }
        String pk_defdoc15 = rs.getString("pk_defdoc15");
        if (pk_defdoc15 != null) {
          saleorderH.setPk_defdoc15(pk_defdoc15.trim());
        }
        String pk_defdoc16 = rs.getString("pk_defdoc16");
        if (pk_defdoc16 != null) {
          saleorderH.setPk_defdoc16(pk_defdoc16.trim());
        }
        String pk_defdoc17 = rs.getString("pk_defdoc17");
        if (pk_defdoc17 != null) {
          saleorderH.setPk_defdoc17(pk_defdoc17.trim());
        }
        String pk_defdoc18 = rs.getString("pk_defdoc18");
        if (pk_defdoc18 != null) {
          saleorderH.setPk_defdoc18(pk_defdoc18.trim());
        }
        String pk_defdoc19 = rs.getString("pk_defdoc19");
        if (pk_defdoc19 != null) {
          saleorderH.setPk_defdoc19(pk_defdoc19.trim());
        }
        String pk_defdoc20 = rs.getString("pk_defdoc20");
        if (pk_defdoc20 != null) {
          saleorderH.setPk_defdoc20(pk_defdoc20.trim());
        }

        Object iprintcount = rs.getObject(83);
        saleorderH.setIprintcount((iprintcount == null) || (iprintcount.toString().length() <= 0) ? null : new Integer(iprintcount.toString()));

        v.addElement(saleorderH);
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
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }
    return saleorderHs;
  }

  public SaleinvoiceVO[] queryOrderStore(SaleinvoiceVO[] saleinvoice)
    throws SQLException
  {
    for (int i = 0; i < saleinvoice.length; i++) {
      SaleinvoiceBVO[] saleOutBs = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice[i].getChildrenVO();

      String saleid = null;
      if ((saleOutBs[0].getCreceipttype().equals("30")) || (saleOutBs[0].getCreceipttype().equals("3A")))
      {
        saleid = saleOutBs[0].getCsourcebillid();
      }
      else if ((saleOutBs[0].getCupupreceipttype().equals("30")) || (saleOutBs[0].getCupupreceipttype().equals("3A")))
      {
        saleid = saleOutBs[0].getCupupsourcebillid();
      } else if (saleOutBs[0].getCupupreceipttype().equals("31"))
      {
        String[] souceInfo = getReceiptInfo(saleOutBs[0].getCupupsourcebillid());

        saleid = souceInfo[1];
      }

      SaleorderHVO sale = queryStoreHeadData(saleid);
      ((SaleVO)saleinvoice[i].getParentVO()).setDbilldate(sale.getDbilldate());

      ((SaleVO)saleinvoice[i].getParentVO()).setCreceiptcorpid(sale.getCreceiptcorpid());

      ((SaleVO)saleinvoice[i].getParentVO()).setCsalecorpid(sale.getCsalecorpid());

      ((SaleVO)saleinvoice[i].getParentVO()).setCreceiptcustomerid(sale.getCreceiptcustomerid());

      ((SaleVO)saleinvoice[i].getParentVO()).setVreceiveaddress(sale.getVreceiveaddress());

      ((SaleVO)saleinvoice[i].getParentVO()).setCtransmodeid(sale.getCtransmodeid());

      ((SaleVO)saleinvoice[i].getParentVO()).setNdiscountrate(sale.getNdiscountrate());

      ((SaleVO)saleinvoice[i].getParentVO()).setCcalbodyid(sale.getCcalbodyid());

      for (int j = 0; j < saleOutBs.length; j++) {
        String soucebillbid = null;
        if ((saleOutBs[j].getCreceipttype().equals("30")) || (saleOutBs[j].getCreceipttype().equals("3A")))
        {
          soucebillbid = saleOutBs[j].getCsourcebillbodyid();
        }
        else if ((saleOutBs[j].getCupupreceipttype().equals("30")) || (saleOutBs[j].getCupupreceipttype().equals("3A")))
        {
          soucebillbid = saleOutBs[j].getCupupsourcebillid();
          saleOutBs[j].setCreceipttype(saleOutBs[j].getCupupreceipttype());

          saleOutBs[j].setCsourcebillid(saleOutBs[j].getCupupsourcebillid());

          saleOutBs[j].setCsourcebillbodyid(saleOutBs[j].getCupupsourcebillbodyid());
        }
        else if (saleOutBs[j].getCupupreceipttype().equals("31"))
        {
          String[] souceInfo = getReceiptInfo(saleOutBs[j].getCupupsourcebillid());

          soucebillbid = souceInfo[1];
          saleOutBs[j].setCreceipttype(souceInfo[0]);
          saleOutBs[j].setCsourcebillid(souceInfo[1]);
          saleOutBs[j].setCsourcebillbodyid(souceInfo[2]);
        }

        SaleorderBVO saleorder = queryStoreBodyData(soucebillbid);
        ((SaleVO)saleinvoice[i].getParentVO()).setCcurrencyid(saleorder.getCcurrencytypeid());

        saleOutBs[j].setCcurrencytypeid(saleorder.getCcurrencytypeid());
        saleOutBs[j].setNdiscountrate(saleorder.getNdiscountrate());
        saleOutBs[j].setNitemdiscountrate(saleorder.getNitemdiscountrate());

        saleOutBs[j].setNexchangeotoarate(saleorder.getNexchangeotoarate());

        saleOutBs[j].setNexchangeotobrate(saleorder.getNexchangeotobrate());

        saleOutBs[j].setNtaxrate(saleorder.getNtaxrate());
        saleOutBs[j].setNoriginalcurprice(saleorder.getNoriginalcurprice());

        saleOutBs[j].setNoriginalcurtaxprice(saleorder.getNoriginalcurtaxprice());

        saleOutBs[j].setNoriginalcurnetprice(saleorder.getNoriginalcurnetprice());

        saleOutBs[j].setNoriginalcurtaxnetprice(saleorder.getNoriginalcurtaxnetprice());

        saleOutBs[j].setNprice(saleorder.getNprice());
        saleOutBs[j].setNtaxprice(saleorder.getNtaxprice());
        saleOutBs[j].setNnetprice(saleorder.getNnetprice());
        saleOutBs[j].setNtaxnetprice(saleorder.getNtaxnetprice());
        saleOutBs[j].setNassistcurprice(saleorder.getNassistcurprice());
        saleOutBs[j].setNassistcurnetprice(saleorder.getNassistcurnetprice());

        saleOutBs[j].setNassistcurtaxnetprice(saleorder.getNassistcurtaxnetprice());

        saleOutBs[j].setNassistcurtaxprice(saleorder.getNassistcurtaxprice());

        String receipttype = saleOutBs[j].getCupreceipttype();
        if ((receipttype == null) || (!receipttype.equals("3Q"))) {
          continue;
        }
        saleOutBs[j].setVfree1(saleorder.getVfree1());
        saleOutBs[j].setVfree2(saleorder.getVfree2());
        saleOutBs[j].setVfree3(saleorder.getVfree3());
        saleOutBs[j].setVfree4(saleorder.getVfree4());
        saleOutBs[j].setVfree5(saleorder.getVfree5());
        saleOutBs[j].setCprojectid(saleorder.getCprojectid());
        saleOutBs[j].setCprojectphaseid(saleorder.getCprojectphaseid());
      }

      saleinvoice[i].setChildrenVO(saleOutBs);
    }

    return saleinvoice;
  }

  private SaleinvoiceBVO[] queryOutBodyData(SaleinvoiceBVO[] itemData)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    for (int i = 0; i < itemData.length; i++) {
      String id = itemData[i].getCsourcebillbodyid();
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, noriginalcurprice, ");

      sql.append("noriginalcurtaxprice, nprice, ntaxprice, creceiptcorpid ");

      sql.append("FROM so_saleorder_b ");
      sql.append("WHERE corder_bid = '" + id + "' ");
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
          BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

          itemData[i].setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

          BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

          itemData[i].setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

          BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

          itemData[i].setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

          BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

          itemData[i].setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

          BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

          itemData[i].setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

          BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

          itemData[i].setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

          BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
          itemData[i].setNprice(nprice == null ? null : new UFDouble(nprice));

          BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");

          itemData[i].setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

          String creceiptcorpid = rs.getString("creceiptcorpid");
          itemData[i].setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
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
          if (con != null)
            con.close();
        }
        catch (Exception e) {
        }
      }
    }
    return itemData;
  }

  public SaleorderBVO queryStoreBodyData(String id)
    throws SQLException
  {
    String sql = "select corder_bid, csaleid, pk_corp, creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, veditreason, ccurrencytypeid, nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, frowstatus,frownote,cinvbasdocid,cbatchid,fbatchstatus,ts,ccalbodyid from so_saleorder_b  where corder_bid = '" + id + "' and beditflag = 'N'";

    SaleorderBVO saleItem = new SaleorderBVO();
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString("ceditsaleid");
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString("beditflag");
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString("veditreason");
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString("coperatorid");
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts));

        String ccalbodyid = rs.getString("ccalbodyid");
        saleItem.setCadvisecalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());
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
      catch (Exception e)
      {
      }
    }
    queryStoreFollow(saleItem);

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { id });

    return saleItem;
  }

  public void queryStoreFollow(SaleorderBVO saleexecute)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { saleexecute });

    String sql = "select csaleid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6 from so_saleexecute where csale_bid = ?";

    String key = saleexecute.getPrimaryKey();
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String csaleid = rs.getString(1);
        saleexecute.setCsaleid(csaleid == null ? null : csaleid.trim());

        String creceipttype = rs.getString(2);
        saleexecute.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

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
      }
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

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { saleexecute });
  }

  public SaleorderHVO queryStoreHeadData(String saleid)
    throws SQLException
  {
    String strSql = "select pk_corp, vreceiptcode, creceipttype, cbiztype, finvoiceclass, finvoicetype, vaccountyear, binitflag, dbilldate, ccustomerid, cdeptid, cemployeeid, coperatorid, ctermprotocolid, csalecorpid, creceiptcustomerid, vreceiveaddress, creceiptcorpid, ctransmodeid, ndiscountrate, cwarehouseid, veditreason, bfreecustflag, cfreecustid, ibalanceflag, nsubscription, ccreditnum, nevaluatecarriage, dmakedate, capproveid, dapprovedate, fstatus, vnote, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10,ccalbodyid,csaleid,bretinvflag,boutendflag,binvoicendflag,breceiptendflag,ts from so_sale where csaleid='" + saleid + "'";

    SaleorderHVO saleHeader = new SaleorderHVO();

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(strSql);
    try
    {
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
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
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts));
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
    return saleHeader;
  }

  public Hashtable queryStrikeData(String cinvoiceid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryStikeData", new Object[] { cinvoiceid });

    String sql = " select carsubacctid , ndetailsubmny  from so_arsubexe where cinvoiceid = ? and dr = 0";
    Hashtable hs = new Hashtable();

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, cinvoiceid);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String carsubacctid = rs.getString("carsubacctid");
        BigDecimal ndetailsubmny = (BigDecimal)rs.getObject("ndetailsubmny");

        if (ndetailsubmny.floatValue() != 0.0F)
          hs.put(carsubacctid, new UFDouble(ndetailsubmny));
      }
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

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryStrikeData", new Object[] { cinvoiceid });

    return hs;
  }

  public void setInvoiceCost(SaleinvoiceVO saleinvoice)
    throws Exception
  {
    SaleVO headVO = (SaleVO)saleinvoice.getParentVO();
    String sBiztype = headVO.getCbiztype();
    if (!isDirectTran(sBiztype))
      return;
    SaleinvoiceBVO[] bodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();

    IPuToSo_PuToSoDMO psDmo = (IPuToSo_PuToSoDMO)NCLocator.getInstance().lookup(IPuToSo_PuToSoDMO.class.getName());

    String[] idBodyAry = new String[bodyVO.length];
    for (int i = 0; i < bodyVO.length; i++) {
      idBodyAry[i] = bodyVO[i].getCsourcebillbodyid();
    }
    UFDouble[] costPriceAry = psDmo.getPriceArray(idBodyAry);
    System.out.println("-----------------------------------------costPriceAry:" + costPriceAry);

    if ((costPriceAry == null) || (costPriceAry.length != idBodyAry.length))
    {
      System.out.println("----------------------!!直运销售时未取到采购成本---------------------------");

      return;
    }

    for (int i = 0; i < costPriceAry.length; i++) {
      UFDouble dNumber = bodyVO[i].getNnumber();
      System.out.println("-----------------------------------------dNumber:" + dNumber);

      if ((dNumber == null) || (costPriceAry[i] == null))
        continue;
      setInvoiceItemCost(bodyVO[i].getPrimaryKey(), costPriceAry[i].multiply(dNumber));
    }
  }

  private void setInvoiceItemCost(String idItem, UFDouble costItem)
    throws SQLException
  {
    String sql = "update so_saleinvoice_b set ncostmny = ? where cinvoice_bid = ?";
    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try
    {
      if (costItem == null)
        stmt.setNull(1, 4);
      else {
        stmt.setBigDecimal(1, costItem.toBigDecimal());
      }

      stmt.setString(2, idItem);
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

  public ArrayList update(SaleinvoiceVO saleinvoice)
    throws SQLException, SystemException
  {
    SaleVO headVO = (SaleVO)saleinvoice.getParentVO();
    SaleinvoiceBVO[] bodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();

    ArrayList listBillID = new ArrayList();
    SaleVO hvo = (SaleVO)saleinvoice.getParentVO();
    try {
      if ((hvo.getVreceiptcode() == null) || (hvo.getVreceiptcode().trim().equals("")) || ((hvo.getVoldreceiptcode() != null) && (!hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode()))))
      {
        String vreceiptcode = null;
        try {
          CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
          vreceiptcode = dmocode.getSysBillNO(saleinvoice);
          saleinvoice.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);
        }
        catch (Exception ex) {
          saleinvoice.getParentVO().setAttributeValue("vreceiptcode", null);

          throw new BusinessException(ex.getMessage());
        }
      }
      try {
        onCheck(saleinvoice);
      } catch (Exception ex) {
        saleinvoice.getParentVO().setAttributeValue("vreceiptcode", null);

        throw new BusinessException(ex.getMessage());
      }

      updateHead(headVO);
      if ((bodyVO != null) && (bodyVO.length != 0))
      {
        listBillID = updateBodys(bodyVO, headVO.getPrimaryKey());
      }
      listBillID.add(saleinvoice.getParentVO().getAttributeValue("vreceiptcode"));
    }
    catch (Exception e) {
      throw new SystemException(e.getMessage());
    }
    return listBillID;
  }

  public void updateBody(SaleinvoiceBVO saleItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateBody", new Object[] { saleItem });

    String sql = "update so_saleinvoice_b set csaleid = ?, pk_corp = ?, cinventoryid = ?, cunitid = ?, cpackunitid = ?, nnumber = ?, npacknumber = ?, nbalancenumber = ?, cbodywarehouseid = ?, cupreceipttype = ?, cupsourcebillid = ?, cupsourcebillbodyid = ?, creceipttype = ?, csourcebillid = ?, csourcebillbodyid = ?, blargessflag = ?, cbatchid = ?, ccurrencytypeid = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, nitemdiscountrate = ?, ntaxrate = ?, noriginalcurprice = ?, noriginalcurtaxprice = ?, noriginalcurnetprice = ?, noriginalcurtaxnetprice = ?, noriginalcurtaxmny = ?, noriginalcurmny = ?, noriginalcursummny = ?, noriginalcurdiscountmny = ?, nprice = ?, ntaxprice = ?, nnetprice = ?, ntaxnetprice = ?, ntaxmny = ?, nmny = ?, nsummny = ?, ndiscountmny = ?, nsimulatecostmny = ?, ncostmny = ?, ddeliverdate = ?, frowstatus = ?, frownote = ?,cinvbasdocid= ?,ndiscountrate= ?, fbatchstatus = ?,ct_manageid= ?,cfreezeid= ?,crowno=? , ccalbodyid=? , coriginalbillcode=? , cupsourcebillcode=? ,cquoteunitid=?,nquotenumber=?,nquoricurpri=?,nquoricurtaxpri=?,nquoricurnetpri=?,nqocurtaxnetpri=?,nquprice=?,nqutaxprice=?,nqunetprice=?,nqutaxnetprice=?,nsubqupri=?,nsubqutaxpri=?,nsubqunetpri=?,nsubqutaxnetpri=?,nsubsummny=?,nsubtaxnetprice=?,cprolineid=?,nsubcursummny=?,ccustomerid=?,httsbl=?,httssl=?,sjtsje=?,sjtssl=?,ztsje=?,b_cjje1=?,b_cjje2=?,b_cjje3=?  where cinvoice_bid = ?"; 

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try
    {
      if (saleItem.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleItem.getCsaleid());
      }
      if (saleItem.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getPk_corp());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleItem.getNpacknumber().toBigDecimal());
      }
      if (saleItem.getNbalancenumber() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, saleItem.getNbalancenumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getCupreceipttype() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleItem.getCupreceipttype());
      }
      if (saleItem.getCupsourcebillid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCupsourcebillid());
      }
      if (saleItem.getCupsourcebillbodyid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getCupsourcebillbodyid());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getBlargessflag() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleItem.getBlargessflag().toString());
      }
      if (saleItem.getCbatchid() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleItem.getCbatchid());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(30, 4);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(31, 4);
      else {
        stmt.setBigDecimal(31, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setBigDecimal(33, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setBigDecimal(35, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(36, 4);
      else {
        stmt.setBigDecimal(36, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(37, 4);
      else {
        stmt.setBigDecimal(37, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(38, 4);
      else {
        stmt.setBigDecimal(38, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getNsimulatecostmny() == null)
        stmt.setNull(39, 4);
      else {
        stmt.setBigDecimal(39, saleItem.getNsimulatecostmny().toBigDecimal());
      }

      if (saleItem.getNcostmny() == null)
        stmt.setNull(40, 4);
      else {
        stmt.setBigDecimal(40, saleItem.getNcostmny().toBigDecimal());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleItem.getDdeliverdate().toString());
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
      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(45, 4);
      else {
        stmt.setBigDecimal(45, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(46, 4);
      else {
        stmt.setInt(46, saleItem.getFbatchstatus().intValue());
      }
      if (saleItem.getCt_manageid() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleItem.getCt_manageid());
      }
      if (saleItem.getCfreezeid() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getCfreezeid());
      }
      if (saleItem.getrowno() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getrowno());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getCoriginalbillcode() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleItem.getCoriginalbillcode());
      }

      if (saleItem.getCupsourcebillcode() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCupsourcebillcode());
      }

      if (saleItem.getCquoteunitid() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleItem.getCquoteunitid());
      }
      if (saleItem.getNquotenumber() == null)
        stmt.setNull(54, 4);
      else {
        stmt.setBigDecimal(54, saleItem.getNquotenumber().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurprice() == null)
        stmt.setNull(55, 4);
      else {
        stmt.setBigDecimal(55, saleItem.getNquoteoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxprice() == null)
        stmt.setNull(56, 4);
      else {
        stmt.setBigDecimal(56, saleItem.getNquoteoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurnetprice() == null)
        stmt.setNull(57, 4);
      else {
        stmt.setBigDecimal(57, saleItem.getNquoteoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxnetprice() == null)
        stmt.setNull(58, 4);
      else {
        stmt.setBigDecimal(58, saleItem.getNquoteoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteprice() == null)
        stmt.setNull(59, 4);
      else {
        stmt.setBigDecimal(59, saleItem.getNquoteprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxprice() == null)
        stmt.setNull(60, 4);
      else {
        stmt.setBigDecimal(60, saleItem.getNquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNquotenetprice() == null)
        stmt.setNull(61, 4);
      else {
        stmt.setBigDecimal(61, saleItem.getNquotenetprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxnetprice() == null)
        stmt.setNull(62, 4);
      else {
        stmt.setBigDecimal(62, saleItem.getNquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubquoteprice() == null)
        stmt.setNull(63, 4);
      else {
        stmt.setBigDecimal(63, saleItem.getNsubquoteprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxprice() == null)
        stmt.setNull(64, 4);
      else {
        stmt.setBigDecimal(64, saleItem.getNsubquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNsubquotenetprice() == null)
        stmt.setNull(65, 4);
      else {
        stmt.setBigDecimal(65, saleItem.getNsubquotenetprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxnetprice() == null)
        stmt.setNull(66, 4);
      else {
        stmt.setBigDecimal(66, saleItem.getNsubquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubsummny() == null)
        stmt.setNull(67, 4);
      else {
        stmt.setBigDecimal(67, saleItem.getNsubsummny().toBigDecimal());
      }
      if (saleItem.getNsubtaxnetprice() == null)
        stmt.setNull(68, 4);
      else {
        stmt.setBigDecimal(68, saleItem.getNsubtaxnetprice().toBigDecimal());
      }

      if (saleItem.getCprolineid() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, saleItem.getCprolineid());
      }

      if (saleItem.getNsubcursummny() == null)
        stmt.setNull(70, 4);
      else {
        stmt.setBigDecimal(70, saleItem.getNsubcursummny().toBigDecimal());
      }

      if (saleItem.getCcustomerid() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, saleItem.getCcustomerid());
      }

      //"httsbl","httssl","sjtsje","sjtssl","ztsje"
      if (saleItem.getHttsbl() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setDouble(72, saleItem.getHttsbl().doubleValue());
        }
      if (saleItem.getHttssl() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setInt(73, saleItem.getHttssl());
        }
      if (saleItem.getSjtsje() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setDouble(74, saleItem.getSjtsje().doubleValue());
        }
      if (saleItem.getSjtssl() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setInt(75, saleItem.getSjtssl());
        }
      if (saleItem.getZtsje() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setDouble(76, saleItem.getZtsje().doubleValue());
        }
      if (saleItem.getB_cjje1() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setString(77, saleItem.getB_cjje1());
        }
      if (saleItem.getB_cjje2() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, saleItem.getB_cjje2());
        }  
      if (saleItem.getB_cjje3() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, saleItem.getB_cjje3());
        }
      
      stmt.setString(80, saleItem.getPrimaryKey());

      stmt.executeUpdate();
      updateFollowBody(saleItem);
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
    updateFollowBody(saleItem);

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateBody", new Object[] { saleItem });
  }

  public ArrayList updateBodys(SaleinvoiceBVO[] saleinvoiceB, String strMainID)
    throws SQLException, SystemException
  {
    ArrayList listRet = new ArrayList();
    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmtfollow = null;
    try {
      con = getConnection();
      stmt = getBodyStatement(con);
      stmtfollow = getFolowStatement(con);
      for (int i = 0; i < saleinvoiceB.length; i++) {
        switch (saleinvoiceB[i].getStatus())
        {
        case 2:
          listRet.add(insertItem(saleinvoiceB[i], strMainID, stmt, stmtfollow));

          break;
        case 3:
          deleteBody(saleinvoiceB[i].getPrimaryKey());
          break;
        case 1:
          updateBody(saleinvoiceB[i]);
        }

      }

      executeBatch(stmt);
      executeBatch(stmtfollow);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmtfollow != null)
          stmtfollow.close();
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
    return listRet;
  }

  public void updateFollowBody(SaleinvoiceBVO saleexecute)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });

    String sql = "update so_saleexecute set csaleid = ?, ntotalpaymny = ?, ntotalreceiptnumber = ?, ntotalinvoicenumber = ?, ntotalinventorynumber = ?, bifinvoicefinish = ?, bifreceiptfinish = ?, bifinventoryfinish = ?, bifpayfinish = ?, nassistcurdiscountmny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, nassistcurtaxnetprice = ?, nassistcurnetprice = ?, nassistcurtaxprice = ?, nassistcurprice = ?, cprojectid3 = ?, cprojectphaseid = ?, cprojectid = ?, vfree5 = ?, vfree4 = ?, vfree3 = ?, vfree2 = ?, vfree1 = ?, vdef6 = ?, vdef5 = ?, vdef4 = ?, vdef3 = ?, vdef2 = ?, vdef1 = ?, narsubinvmny = ?  , vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, vdef11 = ?, vdef12 = ?, vdef13 = ?, vdef14 = ?, vdef15 = ?, vdef16 = ?, vdef17 = ?, vdef18 = ?, vdef19 = ?, vdef20 = ?  , pk_defdoc1 = ?, pk_defdoc2 = ?, pk_defdoc3 = ?, pk_defdoc4 = ?, pk_defdoc5 = ?, pk_defdoc6 = ?, pk_defdoc7 = ?, pk_defdoc8 = ?, pk_defdoc9 = ?, pk_defdoc10 = ?  , pk_defdoc11 = ?, pk_defdoc12 = ?, pk_defdoc13 = ?, pk_defdoc14 = ?, pk_defdoc15 = ?, pk_defdoc16 = ?, pk_defdoc17 = ?, pk_defdoc18 = ?, pk_defdoc19 = ?, pk_defdoc20 = ?  where csale_bid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try
    {
      if (saleexecute.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleexecute.getCsaleid());
      }
      if (saleexecute.getNtotalpaymny() == null)
        stmt.setNull(2, 4);
      else {
        stmt.setBigDecimal(2, saleexecute.getNtotalpaymny().toBigDecimal());
      }

      if (saleexecute.getNtotalreceiptnumber() == null)
        stmt.setNull(3, 4);
      else {
        stmt.setBigDecimal(3, saleexecute.getNtotalreceiptnumber().toBigDecimal());
      }

      if (saleexecute.getNtotalinvoicenumber() == null)
        stmt.setNull(4, 4);
      else {
        stmt.setBigDecimal(4, saleexecute.getNtotalinvoicenumber().toBigDecimal());
      }

      if (saleexecute.getNtotalinventorynumber() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setBigDecimal(5, saleexecute.getNtotalinventorynumber().toBigDecimal());
      }

      if (saleexecute.getBifinvoicefinish() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, saleexecute.getBifinvoicefinish().toString());
      }
      if (saleexecute.getBifreceiptfinish() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, saleexecute.getBifreceiptfinish().toString());
      }
      if (saleexecute.getBifinventoryfinish() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleexecute.getBifinventoryfinish().toString());
      }

      if (saleexecute.getBifpayfinish() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleexecute.getBifpayfinish().toString());
      }
      if (saleexecute.getNassistcurdiscountmny() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, saleexecute.getNassistcurdiscountmny().toBigDecimal());
      }

      if (saleexecute.getNassistcursummny() == null)
        stmt.setNull(11, 4);
      else {
        stmt.setBigDecimal(11, saleexecute.getNassistcursummny().toBigDecimal());
      }

      if (saleexecute.getNassistcurmny() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, saleexecute.getNassistcurmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxmny() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, saleexecute.getNassistcurtaxmny().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxnetprice() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setBigDecimal(14, saleexecute.getNassistcurtaxnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurnetprice() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setBigDecimal(15, saleexecute.getNassistcurnetprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurtaxprice() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setBigDecimal(16, saleexecute.getNassistcurtaxprice().toBigDecimal());
      }

      if (saleexecute.getNassistcurprice() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, saleexecute.getNassistcurprice().toBigDecimal());
      }

      if (saleexecute.getCprojectid3() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleexecute.getCprojectid3());
      }
      if (saleexecute.getCprojectphaseid() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, saleexecute.getCprojectphaseid());
      }
      if (saleexecute.getCprojectid() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, saleexecute.getCprojectid());
      }
      if (saleexecute.getVfree5() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, saleexecute.getVfree5());
      }
      if (saleexecute.getVfree4() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, saleexecute.getVfree4());
      }
      if (saleexecute.getVfree3() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, saleexecute.getVfree3());
      }
      if (saleexecute.getVfree2() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, saleexecute.getVfree2());
      }
      if (saleexecute.getVfree1() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, saleexecute.getVfree1());
      }
      if (saleexecute.getVdef6() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, saleexecute.getVdef6());
      }
      if (saleexecute.getVdef5() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, saleexecute.getVdef5());
      }
      if (saleexecute.getVdef4() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, saleexecute.getVdef4());
      }
      if (saleexecute.getVdef3() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, saleexecute.getVdef3());
      }
      if (saleexecute.getVdef2() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, saleexecute.getVdef2());
      }
      if (saleexecute.getVdef1() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, saleexecute.getVdef1());
      }

      UFDouble narsubinvmny = (saleexecute.getNsubsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsubsummny()).div(saleexecute.getNsummny() == null ? new UFDouble(0.0D) : saleexecute.getNsummny());

      if (narsubinvmny == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, narsubinvmny.toBigDecimal());
      }

      if (saleexecute.getVdef7() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, saleexecute.getVdef7());
      }
      if (saleexecute.getVdef8() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, saleexecute.getVdef8());
      }
      if (saleexecute.getVdef9() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, saleexecute.getVdef9());
      }
      if (saleexecute.getVdef10() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, saleexecute.getVdef10());
      }
      if (saleexecute.getVdef11() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, saleexecute.getVdef11());
      }
      if (saleexecute.getVdef12() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, saleexecute.getVdef12());
      }
      if (saleexecute.getVdef13() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, saleexecute.getVdef13());
      }
      if (saleexecute.getVdef14() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, saleexecute.getVdef14());
      }
      if (saleexecute.getVdef15() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleexecute.getVdef15());
      }
      if (saleexecute.getVdef16() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, saleexecute.getVdef16());
      }
      if (saleexecute.getVdef17() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, saleexecute.getVdef17());
      }
      if (saleexecute.getVdef18() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleexecute.getVdef18());
      }
      if (saleexecute.getVdef19() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleexecute.getVdef19());
      }
      if (saleexecute.getVdef20() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, saleexecute.getVdef20());
      }
      if (saleexecute.getPk_defdoc1() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleexecute.getPk_defdoc1());
      }
      if (saleexecute.getPk_defdoc2() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleexecute.getPk_defdoc2());
      }
      if (saleexecute.getPk_defdoc3() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleexecute.getPk_defdoc3());
      }
      if (saleexecute.getPk_defdoc4() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleexecute.getPk_defdoc4());
      }
      if (saleexecute.getPk_defdoc5() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleexecute.getPk_defdoc5());
      }
      if (saleexecute.getPk_defdoc6() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleexecute.getPk_defdoc6());
      }
      if (saleexecute.getPk_defdoc7() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleexecute.getPk_defdoc7());
      }
      if (saleexecute.getPk_defdoc8() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleexecute.getPk_defdoc8());
      }
      if (saleexecute.getPk_defdoc9() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleexecute.getPk_defdoc9());
      }
      if (saleexecute.getPk_defdoc10() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleexecute.getPk_defdoc10());
      }
      if (saleexecute.getPk_defdoc11() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, saleexecute.getPk_defdoc11());
      }
      if (saleexecute.getPk_defdoc12() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, saleexecute.getPk_defdoc12());
      }
      if (saleexecute.getPk_defdoc13() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, saleexecute.getPk_defdoc13());
      }
      if (saleexecute.getPk_defdoc14() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, saleexecute.getPk_defdoc14());
      }
      if (saleexecute.getPk_defdoc15() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, saleexecute.getPk_defdoc15());
      }
      if (saleexecute.getPk_defdoc16() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, saleexecute.getPk_defdoc16());
      }
      if (saleexecute.getPk_defdoc17() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, saleexecute.getPk_defdoc17());
      }
      if (saleexecute.getPk_defdoc18() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, saleexecute.getPk_defdoc18());
      }
      if (saleexecute.getPk_defdoc19() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, saleexecute.getPk_defdoc19());
      }
      if (saleexecute.getPk_defdoc20() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, saleexecute.getPk_defdoc20());
      }

      stmt.setString(67, saleexecute.getPrimaryKey());

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

    afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
  }

  public void updateHead(SaleVO saleHeader)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateHead", new Object[] { saleHeader });

    String sql = "update so_saleinvoice set pk_corp = ?, vreceiptcode = ?, creceipttype = ?, cbiztype = ?, finvoiceclass = ?, finvoicetype = ?, vaccountyear = ?, binitflag = ?, dbilldate = ?,dmoditime=?, cdeptid = ?, cemployeeid = ?, coperatorid = ?, ctermprotocolid = ?, csalecorpid = ?, creceiptcustomerid = ?, vreceiveaddress = ?, creceiptcorpid = ?, ctransmodeid = ?, ndiscountrate = ?, cwarehouseid = ?, veditreason = ?, bfreecustflag = ?, cfreecustid = ?, ibalanceflag = ?, nsubscription = ?, ccreditnum = ?, nevaluatecarriage = ?, dmakedate = ?, capproveid = ?, dapprovedate = ?, fstatus = ?, vnote = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, ccustbankid = ?, ccalbodyid = ?,cdispatcherid=? ,ntotalsummny=?,nstrikemny=?,nnetmny=?   , vdef11=?, vdef12=?, vdef13=?, vdef14=?, vdef15=?, vdef16=?, vdef17=?, vdef18=?, vdef19=?, vdef20=?  , pk_defdoc1=?, pk_defdoc2=?, pk_defdoc3=?, pk_defdoc4=?, pk_defdoc5=?, pk_defdoc6=?, pk_defdoc7=?, pk_defdoc8=?, pk_defdoc9=?, pk_defdoc10=?  , pk_defdoc11=?, pk_defdoc12=?, pk_defdoc13=?, pk_defdoc14=?, pk_defdoc15=?, pk_defdoc16=?, pk_defdoc17=?, pk_defdoc18=?, pk_defdoc19=?, pk_defdoc20=?, fcounteractflag=? ,vprintcustname=?  where csaleid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try {
      int iseq = 1;

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
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, saleHeader.getBinitflag().toString());
      }
      if (saleHeader.getDbilldate() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleHeader.getDbilldate().toString());
      }

      stmt.setString(10, new UFDateTime(new Date()).toString());

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
      if (saleHeader.getCcustbankid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, saleHeader.getCcustbankid());
      }
      if (saleHeader.getCcalbodyid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, saleHeader.getCcalbodyid());
      }

      if (saleHeader.getcdispatcherid() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, saleHeader.getcdispatcherid());
      }

      if (saleHeader.getNtotalsummny() == null)
        stmt.setNull(47, 4);
      else {
        stmt.setBigDecimal(47, saleHeader.getNtotalsummny().toBigDecimal());
      }

      if (saleHeader.getNstrikemny() == null)
        stmt.setNull(48, 4);
      else {
        stmt.setBigDecimal(48, saleHeader.getNstrikemny().toBigDecimal());
      }

      if (saleHeader.getNnetmny() == null)
        stmt.setNull(49, 4);
      else {
        stmt.setBigDecimal(49, saleHeader.getNnetmny().toBigDecimal());
      }

      if (saleHeader.getVdef11() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleHeader.getVdef11());
      }
      if (saleHeader.getVdef12() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleHeader.getVdef12());
      }
      if (saleHeader.getVdef13() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleHeader.getVdef13());
      }
      if (saleHeader.getVdef14() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, saleHeader.getVdef14());
      }
      if (saleHeader.getVdef15() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, saleHeader.getVdef15());
      }
      if (saleHeader.getVdef16() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, saleHeader.getVdef16());
      }
      if (saleHeader.getVdef17() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, saleHeader.getVdef17());
      }
      if (saleHeader.getVdef18() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, saleHeader.getVdef18());
      }
      if (saleHeader.getVdef19() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, saleHeader.getVdef19());
      }
      if (saleHeader.getVdef20() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, saleHeader.getVdef20());
      }
      if (saleHeader.getPk_defdoc1() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, saleHeader.getPk_defdoc1());
      }
      if (saleHeader.getPk_defdoc2() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, saleHeader.getPk_defdoc2());
      }
      if (saleHeader.getPk_defdoc3() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, saleHeader.getPk_defdoc3());
      }
      if (saleHeader.getPk_defdoc4() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, saleHeader.getPk_defdoc4());
      }
      if (saleHeader.getPk_defdoc5() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, saleHeader.getPk_defdoc5());
      }
      if (saleHeader.getPk_defdoc6() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, saleHeader.getPk_defdoc6());
      }
      if (saleHeader.getPk_defdoc7() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, saleHeader.getPk_defdoc7());
      }
      if (saleHeader.getPk_defdoc8() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, saleHeader.getPk_defdoc8());
      }
      if (saleHeader.getPk_defdoc9() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, saleHeader.getPk_defdoc9());
      }
      if (saleHeader.getPk_defdoc10() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, saleHeader.getPk_defdoc10());
      }
      if (saleHeader.getPk_defdoc11() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, saleHeader.getPk_defdoc11());
      }
      if (saleHeader.getPk_defdoc12() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, saleHeader.getPk_defdoc12());
      }
      if (saleHeader.getPk_defdoc13() == null)
        stmt.setNull(72, 1);
      else {
        stmt.setString(72, saleHeader.getPk_defdoc13());
      }
      if (saleHeader.getPk_defdoc14() == null)
        stmt.setNull(73, 1);
      else {
        stmt.setString(73, saleHeader.getPk_defdoc14());
      }
      if (saleHeader.getPk_defdoc15() == null)
        stmt.setNull(74, 1);
      else {
        stmt.setString(74, saleHeader.getPk_defdoc15());
      }
      if (saleHeader.getPk_defdoc16() == null)
        stmt.setNull(75, 1);
      else {
        stmt.setString(75, saleHeader.getPk_defdoc16());
      }
      if (saleHeader.getPk_defdoc17() == null)
        stmt.setNull(76, 1);
      else {
        stmt.setString(76, saleHeader.getPk_defdoc17());
      }
      if (saleHeader.getPk_defdoc18() == null)
        stmt.setNull(77, 1);
      else {
        stmt.setString(77, saleHeader.getPk_defdoc18());
      }
      if (saleHeader.getPk_defdoc19() == null)
        stmt.setNull(78, 1);
      else {
        stmt.setString(78, saleHeader.getPk_defdoc19());
      }
      if (saleHeader.getPk_defdoc20() == null)
        stmt.setNull(79, 1);
      else {
        stmt.setString(79, saleHeader.getPk_defdoc20());
      }
      if (saleHeader.getFcounteractflag() == null)
        stmt.setInt(80, 0);
      else {
        stmt.setInt(80, saleHeader.getFcounteractflag().intValue());
      }
      if (saleHeader.getVprintcustname() == null)
        stmt.setNull(81, 1);
      else {
        stmt.setString(81, saleHeader.getVprintcustname());
      }

      stmt.setString(82, saleHeader.getPrimaryKey());

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
    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateHead", new Object[] { saleHeader });
  }

  public ArrayList updateInit(SaleinvoiceVO saleinvoice)
    throws SQLException, SystemException, RemoteException
  {
    SaleVO headVO = (SaleVO)saleinvoice.getParentVO();
    SaleinvoiceBVO[] bodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();

    ArrayList listBillID = new ArrayList();
    try {
      updateHead(headVO);

      listBillID = updateInitBodys(bodyVO, headVO.getPrimaryKey(), headVO.getPk_corp());
    }
    catch (Exception e)
    {
      throw new RemoteException(e.getMessage());
    }
    return listBillID;
  }

  public void updateInitBody(SaleinvoiceBVO saleItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateBody", new Object[] { saleItem });

    String sql = "update so_saleinvoice_b set csaleid = ?, pk_corp = ?, cinventoryid = ?, cunitid = ?, cpackunitid = ?, nnumber = ?, npacknumber = ?, nbalancenumber = ?, cbodywarehouseid = ?, cupreceipttype = ?, cupsourcebillid = ?, cupsourcebillbodyid = ?, creceipttype = ?, csourcebillid = ?, csourcebillbodyid = ?, blargessflag = ?, cbatchid = ?, ccurrencytypeid = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, nitemdiscountrate = ?, ntaxrate = ?, noriginalcurprice = ?, noriginalcurtaxprice = ?, noriginalcurnetprice = ?, noriginalcurtaxnetprice = ?, noriginalcurtaxmny = ?, noriginalcurmny = ?, noriginalcursummny = ?, noriginalcurdiscountmny = ?, nprice = ?, ntaxprice = ?, nnetprice = ?, ntaxnetprice = ?, ntaxmny = ?, nmny = ?, nsummny = ?, ndiscountmny = ?, nsimulatecostmny = ?, ncostmny = ?, ddeliverdate = ?, frowstatus = ?, frownote = ?,cinvbasdocid= ?,ndiscountrate= ?, fbatchstatus = ?,ct_manageid=?,crowno=?,ccalbodyid=? , coriginalbillcode= ? , cupsourcebillcode = ? ,cquoteunitid=?,nquotenumber=?,nquoricurpri=?,nquoricurtaxpri=?,nquoricurnetpri=?,nqocurtaxnetpri=?,nquprice=?,nqutaxprice=?,nqunetprice=?,nqutaxnetprice=?,nsubqupri=?,nsubqutaxpri=?,nsubqunetpri=?,nsubqutaxnetpri=?,nsubsummny=?,nsubtaxnetprice=?,cprolineid=? ,nsubcursummny=?,ccustomerid=? where cinvoice_bid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(sql);
    try
    {
      if (saleItem.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, saleItem.getCsaleid());
      }
      if (saleItem.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, saleItem.getPk_corp());
      }
      if (saleItem.getCinventoryid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, saleItem.getCinventoryid());
      }
      if (saleItem.getCunitid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, saleItem.getCunitid());
      }
      if (saleItem.getCpackunitid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, saleItem.getCpackunitid());
      }
      if (saleItem.getNnumber() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, saleItem.getNnumber().toBigDecimal());
      }
      if (saleItem.getNpacknumber() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, saleItem.getNpacknumber().toBigDecimal());
      }
      if (saleItem.getNbalancenumber() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, saleItem.getNbalancenumber().toBigDecimal());
      }

      if (saleItem.getCbodywarehouseid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, saleItem.getCbodywarehouseid());
      }
      if (saleItem.getCupreceipttype() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, saleItem.getCupreceipttype());
      }
      if (saleItem.getCupsourcebillid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, saleItem.getCupsourcebillid());
      }
      if (saleItem.getCupsourcebillbodyid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, saleItem.getCupsourcebillbodyid());
      }
      if (saleItem.getCreceipttype() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, saleItem.getCreceipttype());
      }
      if (saleItem.getCsourcebillid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, saleItem.getCsourcebillid());
      }
      if (saleItem.getCsourcebillbodyid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, saleItem.getCsourcebillbodyid());
      }
      if (saleItem.getBlargessflag() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, saleItem.getBlargessflag().toString());
      }
      if (saleItem.getCbatchid() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, saleItem.getCbatchid());
      }
      if (saleItem.getCcurrencytypeid() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, saleItem.getCcurrencytypeid());
      }
      if (saleItem.getNexchangeotobrate() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, saleItem.getNexchangeotobrate().toBigDecimal());
      }

      if (saleItem.getNexchangeotoarate() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, saleItem.getNexchangeotoarate().toBigDecimal());
      }

      if (saleItem.getNitemdiscountrate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, saleItem.getNitemdiscountrate().toBigDecimal());
      }

      if (saleItem.getNtaxrate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, saleItem.getNtaxrate().toBigDecimal());
      }
      if (saleItem.getNoriginalcurprice() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, saleItem.getNoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxprice() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, saleItem.getNoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurnetprice() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, saleItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurmny() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, saleItem.getNoriginalcurmny().toBigDecimal());
      }

      if (saleItem.getNoriginalcursummny() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, saleItem.getNoriginalcursummny().toBigDecimal());
      }

      if (saleItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(30, 4);
      else {
        stmt.setBigDecimal(30, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (saleItem.getNprice() == null)
        stmt.setNull(31, 4);
      else {
        stmt.setBigDecimal(31, saleItem.getNprice().toBigDecimal());
      }
      if (saleItem.getNtaxprice() == null)
        stmt.setNull(32, 4);
      else {
        stmt.setBigDecimal(32, saleItem.getNtaxprice().toBigDecimal());
      }
      if (saleItem.getNnetprice() == null)
        stmt.setNull(33, 4);
      else {
        stmt.setBigDecimal(33, saleItem.getNnetprice().toBigDecimal());
      }
      if (saleItem.getNtaxnetprice() == null)
        stmt.setNull(34, 4);
      else {
        stmt.setBigDecimal(34, saleItem.getNtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNtaxmny() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setBigDecimal(35, saleItem.getNtaxmny().toBigDecimal());
      }
      if (saleItem.getNmny() == null)
        stmt.setNull(36, 4);
      else {
        stmt.setBigDecimal(36, saleItem.getNmny().toBigDecimal());
      }
      if (saleItem.getNsummny() == null)
        stmt.setNull(37, 4);
      else {
        stmt.setBigDecimal(37, saleItem.getNsummny().toBigDecimal());
      }
      if (saleItem.getNdiscountmny() == null)
        stmt.setNull(38, 4);
      else {
        stmt.setBigDecimal(38, saleItem.getNdiscountmny().toBigDecimal());
      }

      if (saleItem.getNsimulatecostmny() == null)
        stmt.setNull(39, 4);
      else {
        stmt.setBigDecimal(39, saleItem.getNsimulatecostmny().toBigDecimal());
      }

      if (saleItem.getNcostmny() == null)
        stmt.setNull(40, 4);
      else {
        stmt.setBigDecimal(40, saleItem.getNcostmny().toBigDecimal());
      }
      if (saleItem.getDdeliverdate() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, saleItem.getDdeliverdate().toString());
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
      if (saleItem.getNdiscountrate() == null)
        stmt.setNull(45, 4);
      else {
        stmt.setBigDecimal(45, saleItem.getNdiscountrate().toBigDecimal());
      }

      if (saleItem.getFbatchstatus() == null)
        stmt.setNull(46, 4);
      else {
        stmt.setInt(46, saleItem.getFbatchstatus().intValue());
      }
      if (saleItem.getCt_manageid() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, saleItem.getCt_manageid());
      }
      if (saleItem.getrowno() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, saleItem.getrowno());
      }

      if (saleItem.getCadvisecalbodyid() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, saleItem.getCadvisecalbodyid());
      }

      if (saleItem.getCoriginalbillcode() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, saleItem.getCoriginalbillcode());
      }

      if (saleItem.getCupsourcebillcode() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, saleItem.getCupsourcebillcode());
      }

      if (saleItem.getCquoteunitid() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, saleItem.getCquoteunitid());
      }
      if (saleItem.getNquotenumber() == null)
        stmt.setNull(53, 4);
      else {
        stmt.setBigDecimal(53, saleItem.getNquotenumber().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurprice() == null)
        stmt.setNull(54, 4);
      else {
        stmt.setBigDecimal(54, saleItem.getNquoteoriginalcurprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxprice() == null)
        stmt.setNull(55, 4);
      else {
        stmt.setBigDecimal(55, saleItem.getNquoteoriginalcurtaxprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurnetprice() == null)
        stmt.setNull(56, 4);
      else {
        stmt.setBigDecimal(56, saleItem.getNquoteoriginalcurnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteoriginalcurtaxnetprice() == null)
        stmt.setNull(57, 4);
      else {
        stmt.setBigDecimal(57, saleItem.getNquoteoriginalcurtaxnetprice().toBigDecimal());
      }

      if (saleItem.getNquoteprice() == null)
        stmt.setNull(58, 4);
      else {
        stmt.setBigDecimal(58, saleItem.getNquoteprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxprice() == null)
        stmt.setNull(59, 4);
      else {
        stmt.setBigDecimal(59, saleItem.getNquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNquotenetprice() == null)
        stmt.setNull(60, 4);
      else {
        stmt.setBigDecimal(60, saleItem.getNquotenetprice().toBigDecimal());
      }

      if (saleItem.getNquotetaxnetprice() == null)
        stmt.setNull(61, 4);
      else {
        stmt.setBigDecimal(61, saleItem.getNquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubquoteprice() == null)
        stmt.setNull(62, 4);
      else {
        stmt.setBigDecimal(62, saleItem.getNsubquoteprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxprice() == null)
        stmt.setNull(63, 4);
      else {
        stmt.setBigDecimal(63, saleItem.getNsubquotetaxprice().toBigDecimal());
      }

      if (saleItem.getNsubquotenetprice() == null)
        stmt.setNull(64, 4);
      else {
        stmt.setBigDecimal(64, saleItem.getNsubquotenetprice().toBigDecimal());
      }

      if (saleItem.getNsubquotetaxnetprice() == null)
        stmt.setNull(65, 4);
      else {
        stmt.setBigDecimal(65, saleItem.getNsubquotetaxnetprice().toBigDecimal());
      }

      if (saleItem.getNsubsummny() == null)
        stmt.setNull(66, 4);
      else {
        stmt.setBigDecimal(66, saleItem.getNsubsummny().toBigDecimal());
      }
      if (saleItem.getNsubtaxnetprice() == null)
        stmt.setNull(67, 4);
      else {
        stmt.setBigDecimal(67, saleItem.getNsubtaxnetprice().toBigDecimal());
      }

      if (saleItem.getCprolineid() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, saleItem.getCprolineid());
      }

      if (saleItem.getNsubcursummny() == null)
        stmt.setNull(69, 4);
      else {
        stmt.setBigDecimal(69, saleItem.getNsubcursummny().toBigDecimal());
      }

      if (saleItem.getCcustomerid() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, saleItem.getCcustomerid());
      }

      stmt.setString(71, saleItem.getPrimaryKey());

      stmt.executeUpdate();
      sql = "update so_saleexecute set ntotalpaymny = ? where csale_bid = ?";
      stmt = con.prepareStatement(sql);
      if (saleItem.getNtotalpaymny() == null)
        stmt.setNull(1, 4);
      else {
        stmt.setBigDecimal(1, saleItem.getNtotalpaymny().toBigDecimal());
      }

      stmt.setString(2, saleItem.getPrimaryKey());

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
      catch (Exception e) {
      }
    }
    updateFollowBody(saleItem);

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "updateBody", new Object[] { saleItem });
  }

  public ArrayList updateInitBodys(SaleinvoiceBVO[] saleinvoiceB, String strMainID, String pk_corp)
    throws SQLException, SystemException
  {
    ArrayList listRet = new ArrayList();
    for (int i = 0; i < saleinvoiceB.length; i++) {
      switch (saleinvoiceB[i].getStatus())
      {
      case 2:
        listRet.add(insertInitItem(saleinvoiceB[i], strMainID));

        break;
      case 3:
        deleteBody(saleinvoiceB[i].getPrimaryKey());
        break;
      case 1:
        updateInitBody(saleinvoiceB[i]);
      }

    }

    return listRet;
  }

  public void updateLock(String id, String cfreezid)
    throws SQLException
  {
    String sql = "update so_saleinvoice_b set cfreezeid = '" + cfreezid + "' where cinvoice_bid = '" + id + "'";

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

  public void writeToARSub(SaleinvoiceVO saleinvoice, Hashtable hsArsub, boolean bstrikeflag)
    throws SQLException, SystemException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSub", new Object[] { saleinvoice, hsArsub });

    if (bstrikeflag == true)
    {
      String sql = "update so_arsub set ntotalsubmny=isnull(ntotalsubmny,0)- isnull((select sum(ndetailsubmny) from so_arsubexe where dr = 0 and cinvoiceid=? and carsubid = ? group by cinvoiceid),0)  where carsubid = ?";

      Connection con = null;
      PreparedStatement stmt = null;
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);

        Enumeration eKey = hsArsub.keys();
        while (eKey.hasMoreElements()) {
          String key = eKey.nextElement().toString();
          UFDouble money = (UFDouble)hsArsub.get(key);
          String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();
          stmt.setString(1, cinvoiceid);
          stmt.setString(2, key);
          stmt.setString(3, key);
          stmt.executeUpdate();
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
      sql = "update so_arsub set ntotalsubmny= ntotalsubmny + ? where carsubid = ?";
      con = null;
      stmt = null;
      try {
        con = getConnection();

        Enumeration eKey = hsArsub.keys();
        while (eKey.hasMoreElements()) {
          stmt = con.prepareStatement(sql);
          String key = eKey.nextElement().toString();
          UFDouble money = (UFDouble)hsArsub.get(key);
          stmt.setBigDecimal(1, money.toBigDecimal());
          stmt.setString(2, key);
          stmt.executeUpdate();
          stmt.close();

          String followsql = "update so_arsub set binvoiceover='Y' where abs(ntotalsubmny-nsubmny)<=0.001 and carsubid = '" + key + "'";
          stmt = con.prepareStatement(followsql);
          stmt.executeUpdate();
          stmt.close();

          String followsql2 = "update so_arsub set binvoiceover='N' where abs(ntotalsubmny-nsubmny)>0.001 and carsubid = '" + key + "'";
          stmt = con.prepareStatement(followsql2);
          stmt.executeUpdate();
          stmt.close();
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
        } catch (Exception e) {
        }
      }
    }
    else {
      String sql = "update so_arsub set ntotalsubmny=isnull(ntotalsubmny,0)- isnull((select sum(ndetailsubmny) from so_arsubexe where dr = 0 and cinvoiceid=? and carsubid = ? group by cinvoiceid),0) , binvoiceover='N',fstatus = 2 where carsubid = ?";

      Connection con = null;
      PreparedStatement stmt = null;
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);

        Enumeration eKey = hsArsub.keys();
        while (eKey.hasMoreElements()) {
          String key = eKey.nextElement().toString();
          UFDouble money = (UFDouble)hsArsub.get(key);
          String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();
          stmt.setString(1, cinvoiceid);
          stmt.setString(2, key);
          stmt.setString(3, key);

          stmt.executeUpdate();
        }
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

    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSub", new Object[] { saleinvoice, hsArsub });
  }

  public void writeToARSubExe(SaleinvoiceVO saleinvoice, Hashtable hsArsub, boolean bstrikeflag)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubExe", new Object[] { saleinvoice, hsArsub, new UFBoolean(bstrikeflag) });

    if (bstrikeflag == true) {
      String sql = "update so_arsubexe set dr = 1 where cinvoiceid = ? ";
      SaleVO saleHeader = (SaleVO)saleinvoice.getParentVO();
      String cinvoiceid = saleHeader.getPrimaryKey();
      Connection con = null;
      PreparedStatement stmt = null;
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, cinvoiceid);
        stmt.setString(2, saleHeader.getPk_corp());
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
      Enumeration ekeys = hsArsub.keys();
      while (ekeys.hasMoreElements()) {
        String carsubkey = (String)ekeys.nextElement();
        Hashtable hsArsubAcct = (Hashtable)hsArsub.get(carsubkey);

        Enumeration earsubacctids = hsArsubAcct.keys();
        while (earsubacctids.hasMoreElements()) {
          String carsubacctid = (String)earsubacctids.nextElement();
          ArrayList ary = (ArrayList)hsArsubAcct.get(carsubacctid);
          UFDouble narsubmny = (UFDouble)ary.get(1);
          sql = "insert into so_arsubexe(carsubexeid,cinvoiceid,cinvoicecode,carsubid,ndetailsubmny,ccurrencytypeid,notobrate,notoarate,subdate,suboperatorid,carsubacctid) values(?,?,?,?,?,?,?,?,?,?,?)";
          String key = null;
          String pk_corp = saleHeader.getPk_corp();
          try {
            con = getConnection();
            stmt = con.prepareStatement(sql);

            key = getOID(pk_corp);
            stmt.setString(1, key);

            if (saleHeader.getPrimaryKey() == null)
              stmt.setNull(2, 1);
            else {
              stmt.setString(2, saleHeader.getPrimaryKey());
            }

            if (saleHeader.getVreceiptcode() == null)
              stmt.setNull(3, 1);
            else {
              stmt.setString(3, saleHeader.getVreceiptcode());
            }

            if (carsubkey == null)
              stmt.setNull(4, 1);
            else {
              stmt.setString(4, carsubkey);
            }

            if (narsubmny == null)
              stmt.setNull(5, 4);
            else {
              stmt.setBigDecimal(5, narsubmny.toBigDecimal());
            }

            if (saleHeader.getCcurrencyid() == null)
              stmt.setNull(6, 1);
            else {
              stmt.setString(6, saleHeader.getCcurrencyid());
            }

            if (((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate() == null)
            {
              stmt.setNull(7, 4);
            }
            else stmt.setBigDecimal(7, ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate().toBigDecimal());

            if (((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate() == null)
            {
              stmt.setNull(8, 4);
            }
            else stmt.setBigDecimal(8, ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate().toBigDecimal());

            if (saleHeader.getDbilldate() == null)
              stmt.setNull(9, 1);
            else {
              stmt.setString(9, saleHeader.getDbilldate().toString());
            }

            if (saleHeader.getCoperatorid() == null)
              stmt.setNull(10, 1);
            else {
              stmt.setString(10, saleHeader.getCoperatorid());
            }

            stmt.setString(11, carsubacctid);

            stmt.executeUpdate();
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
      }
    }
    else {
      String sql = "update so_arsubexe set dr = 1 where cinvoiceid=?";
      Connection con = null;
      PreparedStatement stmt = null;
      SaleVO saleHeader = (SaleVO)saleinvoice.getParentVO();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);

        if (saleHeader.getPrimaryKey() == null)
          return;
        stmt.setString(1, saleHeader.getPrimaryKey());

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
    }
    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubExe", new Object[] { saleinvoice, hsArsub, new UFBoolean(bstrikeflag) });
  }

  public void writeToOut(SaleinvoiceVO saleinvoice, Hashtable hsArsub, boolean bstrikeflag)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToOut", new Object[] { saleinvoice, hsArsub });

    if (hsArsub.size() == 0) {
      return;
    }

    ArrayList aryinvoice = new ArrayList();
    Hashtable hsinvoice = new Hashtable();
    for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO body = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];

      String hid = body.getCupsourcebillid();
      if (hid == null)
        continue;
      if ((body.getCupreceipttype() == null) || (!body.getCupreceipttype().equals("4C")))
        continue;
      body.setNsubsummny(body.getNsubsummny() == null ? new UFDouble(0.0D) : body.getNsubsummny());

      body.setNsummny(body.getNsummny() == null ? new UFDouble(0.0D) : body.getNsummny());

      UFDouble money = bstrikeflag == true ? body.getNsubsummny().sub(body.getNsummny()) : body.getNsubsummny().sub(body.getNsummny()).multiply(new UFDouble(-1));

      if (hsinvoice.containsKey(hid)) {
        hsinvoice.put(hid, ((UFDouble)hsinvoice.get(hid)).add(money));
      }
      else
        hsinvoice.put(hid, money);
    }
    Enumeration eKeys = hsinvoice.keys();
    while (eKeys.hasMoreElements()) {
      ArrayList arydetail = new ArrayList();
      String billhid = (String)eKeys.nextElement();
      UFDouble discountmny = (UFDouble)hsinvoice.get(billhid);
      arydetail.add(billhid);
      arydetail.add(discountmny);
      aryinvoice.add(arydetail);
    }
    if (aryinvoice.size() > 0)
    {
      try
      {
        IIC2SO dmo = (IIC2SO)NCLocator.getInstance().lookup(IIC2SO.class.getName());
        dmo.setNdiscountNums(aryinvoice);
      } catch (Exception e) {
        throw new SystemException(e.getMessage());
      }

    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToOut", new Object[] { saleinvoice, hsArsub });
  }

  public Hashtable distributeToArsubAcct(Hashtable hstable, UFDouble mny)
    throws BusinessException
  {
    Enumeration eArsubacctids = hstable.keys();
    while (eArsubacctids.hasMoreElements()) {
      String carsubacctid = (String)eArsubacctids.nextElement();
      ArrayList arymny = (ArrayList)hstable.get(carsubacctid);
      UFDouble totalmny = (UFDouble)arymny.get(0);
      if (totalmny.compareTo(mny) >= 0) {
        arymny.set(1, mny);
        mny = mny.sub(mny);
        hstable.put(carsubacctid, arymny);
        break;
      }
      arymny.set(1, totalmny);
      mny = mny.sub(totalmny);
    }

    if (mny.compareTo(new UFDouble(0.0D)) > 0) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000006"));
    }

    return hstable;
  }

  public Hashtable fillprolineidwithArsub(Hashtable hsArsubAcct)
    throws SQLException
  {
    Hashtable hsFullArsubAcct = new Hashtable();
    String sql = "select cproducelineid from so_arsub where carsubid= ? ";
    Enumeration eIDs = hsArsubAcct.keys();
    Connection con = null;
    PreparedStatement stmt = null;
    while (eIDs.hasMoreElements()) {
      String id = (String)eIDs.nextElement();
      ArrayList aryArsubAcct = new ArrayList();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rst = stmt.executeQuery();
        while (rst.next()) {
          aryArsubAcct.add(rst.getString(1));
        }
        aryArsubAcct.add((UFDouble)hsArsubAcct.get(id));
        hsFullArsubAcct.put(id, aryArsubAcct);
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
    }
    return hsFullArsubAcct;
  }

  public Hashtable getRelation(Hashtable hsArsubAcct)
    throws SQLException, SystemException, BusinessException
  {
    Hashtable hsRelation = new Hashtable();
    ArrayList aryArsubacctids = new ArrayList();
    Enumeration ekeys = hsArsubAcct.keys();
    while (ekeys.hasMoreElements()) {
      String key = ekeys.nextElement().toString();
      aryArsubacctids.add(key);
    }

    String sql = " select carsubacctid , carsubid  from so_arsubacct where dr=0 ";
    sql = sql + GeneralSqlString.formInSQL("carsubacctid", aryArsubacctids);

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String carsubacctid = rs.getString("carsubacctid");
        String carsubid = rs.getString("carsubid");
        hsRelation.put(carsubacctid, carsubid);
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
    return hsRelation;
  }

  public Hashtable transToARSubFromAcct(Hashtable hsArsubAcct)
    throws SQLException, SystemException, BusinessException
  {
    Hashtable hsArsub = new Hashtable();
    Hashtable hsRelation = new Hashtable();
    hsRelation = getRelation(hsArsubAcct);
    Enumeration earsubacctids = hsArsubAcct.keys();
    while (earsubacctids.hasMoreElements()) {
      String carsubacctid = earsubacctids.nextElement().toString();
      if (!hsRelation.containsKey(carsubacctid)) {
        continue;
      }
      String carsubid = hsRelation.get(carsubacctid).toString();
      if (hsArsub.containsKey(carsubid)) {
        UFDouble nmny = (UFDouble)(hsArsubAcct.get(carsubacctid) == null ? new UFDouble(0.0D) : hsArsubAcct.get(carsubacctid));

        hsArsub.put(carsubid, ((UFDouble)(hsArsub.get(carsubid) == null ? new UFDouble(0.0D) : hsArsub.get(carsubid))).add(nmny));
      }
      else
      {
        hsArsub.put(carsubid, (UFDouble)(hsArsubAcct.get(carsubacctid) == null ? new UFDouble(0.0D) : hsArsubAcct.get(carsubacctid)));
      }

    }

    return hsArsub;
  }

  public void writeBackToBalance(SaleinvoiceVO saleinvoice)
    throws SQLException, SystemException, BusinessException, RemoteException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeBackToBalance", new Object[] { saleinvoice });

    UFDate dClientDate = saleinvoice.getDClientDate();
    Vector vecTemp = new Vector();
    for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
      SaleinvoiceBVO body = (SaleinvoiceBVO)saleinvoice.getChildrenVO()[i];
      InvoiceBalVO balanceVO = new InvoiceBalVO();
      if (body.getCupreceipttype() == null)
        continue;
      if (body.getCupreceipttype().equals("30")) {
        balanceVO.setCsaleid(body.getCsourcebillid());
        balanceVO.setCoutid(null);

        UFDouble dSubsummny = body.getNsubsummny() == null ? new UFDouble(0) : body.getNsubsummny();

        UFDouble dSummny = body.getNsummny() == null ? new UFDouble(0) : body.getNsummny();

        balanceVO.setNinvbalmny(dSubsummny.sub(dSummny));
        balanceVO.setCprodline(body.getCprolineid());
        balanceVO.setCcurrencytypeid(body.getCcurrencytypeid());
        balanceVO.setCinvoiceid(body.getCsaleid());
        balanceVO.setCinvoicerowid(body.getCinvoice_bid());
        balanceVO.setDbilldate(((SaleVO)saleinvoice.getParentVO()).getDbilldate());

        balanceVO.setNexchgtoarate(body.getNexchangeotoarate());
        balanceVO.setNexchgtobrate(body.getNexchangeotobrate());
        balanceVO.setNorginvbalmny(dSubsummny.sub(dSummny));
        balanceVO.setNoriginalcursummny(body.getNoriginalcursummny());
        balanceVO.setNsummny(dSummny);
        balanceVO.setPk_corp(body.getPk_corp());
        balanceVO.setDcurdate(dClientDate);
      } else if (body.getCupreceipttype().equals("4C"))
      {
        balanceVO.setCsaleid(body.getCsourcebillid());
        balanceVO.setCoutid(body.getCupsourcebillid());

        UFDouble dSubsummny = body.getNsubsummny() == null ? new UFDouble(0) : body.getNsubsummny();

        UFDouble dOrgSummny = body.getNoriginalcursummny() == null ? new UFDouble(0) : body.getNoriginalcursummny();

        balanceVO.setNinvbalmny(dSubsummny.sub(dOrgSummny));
        balanceVO.setCprodline(body.getCprolineid());
        balanceVO.setCcurrencytypeid(body.getCcurrencytypeid());
        balanceVO.setCinvoiceid(body.getCsaleid());
        balanceVO.setCinvoicerowid(body.getCinvoice_bid());
        balanceVO.setDbilldate(((SaleVO)saleinvoice.getParentVO()).getDbilldate());

        balanceVO.setNexchgtoarate(body.getNexchangeotoarate());
        balanceVO.setNexchgtobrate(body.getNexchangeotobrate());

        UFDouble dSubcursummny = body.getNsubcursummny() == null ? new UFDouble(0) : body.getNsubcursummny();

        balanceVO.setNorginvbalmny(dSubcursummny.sub(dOrgSummny));
        balanceVO.setNoriginalcursummny(body.getNoriginalcursummny());
        balanceVO.setNsummny(body.getNsummny());
        balanceVO.setPk_corp(body.getPk_corp());
        balanceVO.setDcurdate(dClientDate);
      }
      vecTemp.add(balanceVO);
    }
    if (vecTemp.size() == 0)
      return;
    InvoiceBalVO[] balanceVOs = new InvoiceBalVO[vecTemp.size()];
    vecTemp.copyInto(balanceVOs);

    UFBoolean bStrikeflag = saleinvoice.getBstrikeflag();
    try {
      BalanceDMO balancedmo = new BalanceDMO();
      if (bStrikeflag.booleanValue())
        balancedmo.updateSoBalanceWhenInvoiceBal(balanceVOs);
      else
        balancedmo.updateSoBalanceWhenUnInvoiceBal(balanceVOs);
    } catch (BusinessException e) {
      throw e;
    } catch (RemoteException e) {
      throw e;
    } catch (Exception e) {
      throw new RemoteException("Remote Call", e);
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeBackToBalance", new Object[] { saleinvoice });
  }

  public void writeToARSub(SaleinvoiceVO saleinvoice)
    throws SQLException, SystemException, BusinessException, RemoteException
  {
    if (saleinvoice == null)
      return;
    if (saleinvoice.getBstrikeflag() == null)
      return;
    if ((saleinvoice.getHsSelectedARSubHVO() == null) || (saleinvoice.getHsSelectedARSubHVO().size() <= 0))
    {
      return;
    }if ((saleinvoice.getAllinvoicevo() == null) || (saleinvoice.getAllinvoicevo().getChildrenVO() == null) || (saleinvoice.getAllinvoicevo().getChildrenVO().length <= 0))
    {
      return;
    }Hashtable hsWrite = new Hashtable();
    Hashtable hsWriteToARSub = transToARSubFromAcct(saleinvoice.getHsSelectedARSubHVO());
    try
    {
      writeToARSub(saleinvoice.getAllinvoicevo(), hsWriteToARSub, saleinvoice.getBstrikeflag().booleanValue());

      writeToARSubAcctNEW(saleinvoice.getAllinvoicevo(), saleinvoice.getHsArsubAcct(), saleinvoice.getBstrikeflag().booleanValue());

      Hashtable hsRelation = getRelation(saleinvoice.getHsSelectedARSubHVO());

      writeToARSubExeNEW(saleinvoice, hsRelation);

      writeToOut(saleinvoice.getAllinvoicevo(), hsWriteToARSub, saleinvoice.getBstrikeflag().booleanValue());

      writeBackToBalance(saleinvoice);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      throw new RemoteException("remote call:", e);
    }
  }

  public Hashtable writeToARSubAcct(SaleinvoiceVO saleinvoice, Hashtable hsArsubAcct, boolean bstrikeflag)
    throws SQLException, SystemException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubAcct", new Object[] { saleinvoice, hsArsubAcct, new UFBoolean(bstrikeflag) });

    Hashtable hsFullArsubAcct = new Hashtable();
    hsFullArsubAcct = fillprolineidwithArsub(hsArsubAcct);

    String cbiztypeid = ((SaleVO)saleinvoice.getParentVO()).getCbiztype();

    Hashtable hsResult = new Hashtable();

    Enumeration eKeys = hsFullArsubAcct.keys();
    while (eKeys.hasMoreElements())
    {
      String carsubkey = (String)eKeys.nextElement();
      ArrayList aryarsub = (ArrayList)hsFullArsubAcct.get(carsubkey);
      String cprolineid = (String)aryarsub.get(0);
      UFDouble narsubmny = (UFDouble)aryarsub.get(1);
      Connection con = null;
      PreparedStatement stmt = null;
      Hashtable hsArsubAcctDetail = new Hashtable();
      if (bstrikeflag) {
        String querysql = "select carsubacctid,narsubmny from so_arsubacct where carsubid = ? and (cbiztypeid = ? or cbiztypeid =#123456789*123456789) and cproducelineid = ? ";
        try
        {
          con = getConnection();
          stmt = con.prepareStatement(querysql);
          stmt.setString(1, carsubkey);
          stmt.setString(2, cbiztypeid);
          stmt.setString(3, cprolineid);
          ResultSet rst = stmt.executeQuery();
          while (rst.next()) {
            UFDouble nmny1 = new UFDouble(rst.getBigDecimal(2));
            UFDouble nmny2 = new UFDouble(0.0D);
            ArrayList arymny = new ArrayList();
            arymny.add(nmny1);
            arymny.add(nmny2);
            hsArsubAcctDetail.put(rst.getString(1), arymny);
          }

          if (hsArsubAcctDetail.size() != 0) {
            hsArsubAcctDetail = distributeToArsubAcct(hsArsubAcctDetail, narsubmny);
          }
          else {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000007"));
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
            if (con != null)
              con.close();
          } catch (Exception e) {
          }
        }
      }
      else {
        String querysql = "select carsubacctid,narsubmny,narsubinvmny from so_arsubacct where carsubid = ? and (cbiztypeid = ? or cbiztypeid = #123456789*123456789) and cproducelineid = ? ";
        try
        {
          con = getConnection();
          stmt = con.prepareStatement(querysql);
          stmt.setString(1, carsubkey);
          stmt.setString(2, cbiztypeid);
          stmt.setString(3, cprolineid);
          ResultSet rst = stmt.executeQuery();
          while (rst.next()) {
            UFDouble nmny1 = new UFDouble(rst.getBigDecimal(2));
            UFDouble nmny2 = new UFDouble(rst.getBigDecimal(3));
            ArrayList arymny = new ArrayList();
            arymny.add(nmny1);
            arymny.add(nmny2);
            hsArsubAcctDetail.put(rst.getString(1), arymny);
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
      }
      String updatesql = "";
      if (bstrikeflag)
      {
        String clearsql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) - isnull((select sum(ndetailsubmny) from so_arsubexe where carsubacctid = ? and cinvoiceid = ? and dr=0 group by cinvoiceid),0) where carsubacctid = ? ";
        try {
          con = getConnection();
          stmt = con.prepareStatement(clearsql);
          Enumeration eKey = hsArsubAcctDetail.keys();
          while (eKey.hasMoreElements()) {
            String key = eKey.nextElement().toString();

            String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();

            stmt.setString(2, cinvoiceid);
            stmt.setString(1, key);
            stmt.setString(3, key);
            stmt.executeUpdate();
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
        updatesql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) + ? where carsubacctid = ? ";
        Enumeration earsubacctids = hsArsubAcctDetail.keys();
        while (earsubacctids.hasMoreElements()) {
          String carsubacctid = (String)earsubacctids.nextElement();
          UFDouble narsubinvmny = (UFDouble)((ArrayList)hsArsubAcctDetail.get(carsubacctid)).get(1);
          try
          {
            con = getConnection();
            stmt = con.prepareStatement(updatesql);
            if (narsubinvmny == null)
              stmt.setNull(1, 4);
            else
              stmt.setBigDecimal(1, narsubinvmny.toBigDecimal());
            stmt.setString(2, carsubacctid);
            stmt.executeUpdate();

            checkArsubAcctWrite(carsubacctid);
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
            } catch (Exception e) {
            }
          }
        }
      }
      else {
        updatesql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) - isnull((select sum(ndetailsubmny) from so_arsubexe where carsubacctid = ? and cinvoiceid = ? and dr=0 group by cinvoiceid),0) where carsubacctid=? ";
        try {
          con = getConnection();
          stmt = con.prepareStatement(updatesql);
          Enumeration eKey = hsArsubAcctDetail.keys();
          while (eKey.hasMoreElements()) {
            String key = eKey.nextElement().toString();

            String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();

            stmt.setString(2, cinvoiceid);
            stmt.setString(1, key);
            stmt.setString(3, key);
            stmt.executeUpdate();
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
      }
      hsResult.put(carsubkey, hsArsubAcctDetail);
    }

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubAcct", new Object[] { saleinvoice, hsArsubAcct, new UFBoolean(bstrikeflag) });

    return hsResult;
  }

  public void writeToARSubAcctNEW(SaleinvoiceVO saleinvoice, Hashtable hsArsubAcct, boolean bstrikeflag)
    throws SQLException, SystemException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubAcct", new Object[] { saleinvoice, hsArsubAcct, new UFBoolean(bstrikeflag) });

    String updatesql = "";
    Connection con = null;
    PreparedStatement stmt = null;
    if (bstrikeflag)
    {
      String clearsql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) - isnull((select sum(ndetailsubmny) from so_arsubexe where carsubacctid = ? and cinvoiceid = ? and dr=0 group by cinvoiceid),0) where carsubacctid = ? ";
      try {
        con = getConnection();
        stmt = con.prepareStatement(clearsql);
        Enumeration eKey = hsArsubAcct.keys();
        while (eKey.hasMoreElements()) {
          String key = eKey.nextElement().toString();
          String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();

          stmt.setString(2, cinvoiceid);
          stmt.setString(1, key);
          stmt.setString(3, key);
          stmt.executeUpdate();
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
      updatesql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) + ? where carsubacctid = ? ";
      Enumeration earsubacctids = hsArsubAcct.keys();
      while (earsubacctids.hasMoreElements()) {
        String carsubacctid = (String)earsubacctids.nextElement();
        UFDouble narsubinvmny = (UFDouble)hsArsubAcct.get(carsubacctid);
        try
        {
          con = getConnection();
          stmt = con.prepareStatement(updatesql);
          if (narsubinvmny == null)
            stmt.setNull(1, 4);
          else
            stmt.setBigDecimal(1, narsubinvmny.toBigDecimal());
          stmt.setString(2, carsubacctid);
          stmt.executeUpdate();

          checkArsubAcctWrite(carsubacctid);
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
          } catch (Exception e) {
          }
        }
      }
    }
    else {
      updatesql = "update so_arsubacct set narsubinvmny = isnull(narsubinvmny,0) - isnull((select sum(ndetailsubmny) from so_arsubexe where carsubacctid = ? and cinvoiceid = ? and dr=0 group by cinvoiceid),0) where carsubacctid=? ";
      try {
        con = getConnection();
        stmt = con.prepareStatement(updatesql);
        Enumeration eKey = (saleinvoice.getHsSelectedARSubHVO() == null ? hsArsubAcct : saleinvoice.getHsSelectedARSubHVO()).keys();

        while (eKey.hasMoreElements()) {
          String key = eKey.nextElement().toString();
          String cinvoiceid = ((SaleVO)saleinvoice.getParentVO()).getPrimaryKey();

          stmt.setString(2, cinvoiceid);
          stmt.setString(1, key);
          stmt.setString(3, key);
          stmt.executeUpdate();
        }
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
    }
    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubAcct", new Object[] { saleinvoice, hsArsubAcct, new UFBoolean(bstrikeflag) });
  }

  public void writeToARSubExeNEW(SaleinvoiceVO saleinvoice, Hashtable hsRelation)
    throws SQLException, SystemException, BusinessException
  {
    if ((saleinvoice == null) || (saleinvoice.getChildrenVO() == null) || (saleinvoice.getChildrenVO().length <= 0))
    {
      return;
    }

    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubExe", new Object[] { saleinvoice });

    boolean bstrikeflag = saleinvoice.getBstrikeflag().booleanValue();
    Hashtable hsARSubAcct = saleinvoice.getHsSelectedARSubHVO();
    if ((hsRelation == null) || (hsRelation.size() == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000008"));
    }

    if (bstrikeflag == true) {
      String sql = "update so_arsubexe set dr = 1 where cinvoiceid=?";
      SaleVO saleHeader = (SaleVO)saleinvoice.getParentVO();
      String cinvoiceid = saleHeader.getPrimaryKey();
      Connection con = null;
      PreparedStatement stmt = null;
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, cinvoiceid);
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
      Enumeration ekeys = hsARSubAcct.keys();
      while (ekeys.hasMoreElements()) {
        String carsubacctid = (String)ekeys.nextElement();
        String carsubid = (String)hsRelation.get(carsubacctid);
        UFDouble narsubmny = (UFDouble)hsARSubAcct.get(carsubacctid);
        sql = "insert into so_arsubexe(carsubexeid,cinvoiceid,cinvoicecode,carsubid,ndetailsubmny,ccurrencytypeid,notobrate,notoarate,subdate,suboperatorid,carsubacctid) values(?,?,?,?,?,?,?,?,?,?,?)";
        String key = null;
        String pk_corp = saleHeader.getPk_corp();
        try {
          con = getConnection();
          stmt = con.prepareStatement(sql);

          key = getOID(pk_corp);
          stmt.setString(1, key);

          if (saleHeader.getPrimaryKey() == null)
            stmt.setNull(2, 1);
          else {
            stmt.setString(2, saleHeader.getPrimaryKey());
          }

          if (saleHeader.getVreceiptcode() == null)
            stmt.setNull(3, 1);
          else {
            stmt.setString(3, saleHeader.getVreceiptcode());
          }

          if (carsubid == null)
            stmt.setNull(4, 1);
          else {
            stmt.setString(4, carsubid);
          }

          if (narsubmny == null)
            stmt.setNull(5, 4);
          else {
            stmt.setBigDecimal(5, narsubmny.toBigDecimal());
          }

          if (saleHeader.getCcurrencyid() == null)
            stmt.setNull(6, 1);
          else {
            stmt.setString(6, saleHeader.getCcurrencyid());
          }

          if (((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate() == null)
          {
            stmt.setNull(7, 4);
          }
          else stmt.setBigDecimal(7, ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotobrate().toBigDecimal());

          if (((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate() == null)
          {
            stmt.setNull(8, 4);
          }
          else stmt.setBigDecimal(8, ((SaleinvoiceBVO)saleinvoice.getChildrenVO()[0]).getNexchangeotoarate().toBigDecimal());

          if (saleHeader.getDbilldate() == null)
            stmt.setNull(9, 1);
          else {
            stmt.setString(9, saleHeader.getDbilldate().toString());
          }

          if (saleHeader.getCoperatorid() == null)
            stmt.setNull(10, 1);
          else {
            stmt.setString(10, saleHeader.getCoperatorid());
          }

          stmt.setString(11, carsubacctid);

          stmt.executeUpdate();
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
      }
    }
    else
    {
      String sql = "update so_arsubexe set dr = 1 where cinvoiceid=?";
      Connection con = null;
      PreparedStatement stmt = null;
      SaleVO saleHeader = (SaleVO)saleinvoice.getParentVO();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);

        if (saleHeader.getPrimaryKey() == null)
          return;
        stmt.setString(1, saleHeader.getPrimaryKey());

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
    }
    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "writeToARSubExe", new Object[] { saleinvoice });
  }

  public Hashtable fillDatawithARSubAcct(Hashtable hsArsubAcct)
    throws SQLException
  {
    Hashtable hsFullArsubAcct = new Hashtable();
    String sql = "select carsubacctid,cproducelineid,narsubmny from so_arsubacct where carsubacctid= ? ";
    Enumeration eIDs = hsArsubAcct.keys();
    Connection con = null;
    PreparedStatement stmt = null;
    while (eIDs.hasMoreElements()) {
      String id = (String)eIDs.nextElement();
      ArrayList aryArsubAcct = new ArrayList();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rst = stmt.executeQuery();
        while (rst.next()) {
          aryArsubAcct.add(rst.getString(2));
          UFDouble narsubmny = new UFDouble(0.0D);
          BigDecimal mny = rst.getBigDecimal(3);
          if (mny != null)
            narsubmny = new UFDouble(mny);
          aryArsubAcct.add(narsubmny);
        }
        UFDouble narsubinvmny = (UFDouble)hsArsubAcct.get(id);
        aryArsubAcct.add(narsubinvmny);
        hsFullArsubAcct.put(id, aryArsubAcct);
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
    }
    return hsFullArsubAcct;
  }

  public void deleteVoucher(SaleinvoiceVO inVO)
    throws Exception
  {
    DapMsgVO dapVO = null;
    try {
      SaleVO hVO = (SaleVO)inVO.getParentVO();
      SaleinvoiceBVO[] bVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])inVO.getChildrenVO();

      String beanName = IDapSendMessage.class.getName();
      IDapSendMessage boDap = (IDapSendMessage)NCLocator.getInstance().lookup(beanName);

      if (!boDap.isEditBillTypeOrProc(hVO.getPk_corp(), "SO", "32", hVO.getCbiztype(), hVO.getCsaleid()))
      {
        throw new BusinessException("");
      }

      dapVO = new DapMsgVO();

      dapVO.setSys("SO");

      dapVO.setProc("32");

      dapVO.setBusiType(hVO.getCbiztype());

      dapVO.setBusiDate(hVO.getDapprovedate());

      dapVO.setBillCode(hVO.getVreceiptcode());

      dapVO.setCorp(hVO.getPk_corp());

      dapVO.setOperator(hVO.getCapproveid());

      dapVO.setChecker(hVO.getCapproveid());

      dapVO.setMsgType(1);

      int iSize = bVO.length;
      UFDouble dZero = new UFDouble(0);
      for (int i = 0; i < iSize; i++)
      {
        if ((bVO[i].getNoriginalcursummny() == null) || (bVO[i].getNoriginalcursummny().compareTo(dZero) == 0))
        {
          continue;
        }

        dapVO.setProcMsg(bVO[i].getCinvoice_bid());

        dapVO.setCurrency(bVO[i].getCcurrencytypeid());

        dapVO.setMoney(bVO[i].getNoriginalcursummny());

        boDap.sendMessage(dapVO, null);
      }

    }
    catch (Exception e)
    {
      throw e;
    }
  }

  public void createVoucher(SaleinvoiceVO inVO)
    throws Exception
  {
    DapMsgVO dapVO = null;
    boolean bdebug = false;
    try {
      if (!bdebug) {
        String beanName = IDapSendMessage.class.getName();
        IDapSendMessage boDap = (IDapSendMessage)NCLocator.getInstance().lookup(beanName);

        SaleVO hVO = (SaleVO)inVO.getParentVO();
        SaleinvoiceBVO[] bVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])inVO.getChildrenVO();

        dapVO = new DapMsgVO();

        dapVO.setSys("SO");

        dapVO.setProc("32");

        dapVO.setBusiType(hVO.getCbiztype());

        Hashtable ht = getCustBasIDByCustManID(BillTools.getUniqueArrayFromVOsByKey(bVO, "ccustomerid"));

        if (hVO.getDapprovedate() != null) {
          dapVO.setBusiDate(hVO.getDapprovedate());
        }
        else {
          dapVO.setBusiDate(hVO.getDbilldate());
        }

        dapVO.setBillCode(hVO.getVreceiptcode());

        dapVO.setDestSystem(0);

        dapVO.setCorp(hVO.getPk_corp());

        if (hVO.getCapproveid() != null) {
          dapVO.setOperator(hVO.getCapproveid());

          dapVO.setChecker(hVO.getCapproveid());
        } else {
          dapVO.setOperator(hVO.getCoperatorid());

          dapVO.setChecker(hVO.getCoperatorid());
        }

        dapVO.setMsgType(0);

        int iSize = bVO.length;
        UFDouble dZero = new UFDouble(0);
        String scubasid = null;
        for (int i = 0; i < iSize; i++) {
          if (bVO[i].getCcustomerid() != null)
            scubasid = (String)ht.get(bVO[i].getCcustomerid());
          else
            scubasid = null;
          bVO[i].setCcustbaseid(scubasid);

          if ((bVO[i].getNoriginalcursummny() == null) || (bVO[i].getNoriginalcursummny().compareTo(dZero) == 0))
          {
            continue;
          }

          dapVO.setProcMsg(bVO[i].getCinvoice_bid());

          dapVO.setCurrency(bVO[i].getCcurrencytypeid());

          dapVO.setMoney(bVO[i].getNoriginalcursummny());
          try
          {
            SaleinvoiceVO toVouch = new SaleinvoiceVO();

            SaleinvoiceBVO[] toVouchB = new SaleinvoiceBVO[1];

            toVouchB[0] = bVO[i];

            toVouch.setParentVO((SaleVO)inVO.getParentVO().clone());

            toVouch.setChildrenVO(toVouchB);

            boDap.sendMessage(dapVO, toVouch);
          }
          catch (Exception e)
          {
            SCMEnv.out(e.getMessage());
          }

        }

      }

    }
    catch (Exception e)
    {
      throw e;
    }
  }

  public void clearControlActFlag(SaleinvoiceVO vo)
    throws SQLException
  {
    String pk_billid = null;
    SaleVO head = (SaleVO)vo.getParentVO();
    if ((head.getFcounteractflag().intValue() == 2) && 
      (vo.getChildrenVO() != null)) {
      SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vo.getChildrenVO();
      pk_billid = items[0].getCupsourcebillid();
    }

    try
    {
      SmartDMO sdmo = new SmartDMO();
      String sqlthis = "update so_saleinvoice set fcounteractflag=0 where csaleid='" + pk_billid + "'";
      sdmo.executeUpdate(sqlthis, new ArrayList(), new ArrayList());
    }
    catch (Exception e)
    {
      if ((e instanceof SQLException)) {
        throw ((SQLException)e);
      }
      throw new SQLException(e.getMessage());
    }
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String whereString)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyData", new Object[] { key });

    String sql = "select cinvoice_bid, so_saleinvoice_b.csaleid, pk_corp, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, nbalancenumber, cbodywarehouseid, cupreceipttype, cupsourcebillid, cupsourcebillbodyid, so_saleinvoice_b.creceipttype, csourcebillid, csourcebillbodyid, blargessflag, cbatchid, ccurrencytypeid, nexchangeotobrate, nexchangeotoarate, nitemdiscountrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, nsimulatecostmny, ncostmny, ddeliverdate, frowstatus, frownote,cinvbasdocid, ndiscountrate,fbatchstatus,ct_manageid,cfreezeid,so_saleinvoice_b.ts, creceiptcorpid,crowno,  so_saleexecute.vfree1,so_saleexecute.vfree2,so_saleexecute.vfree3,so_saleexecute.vfree4,so_saleexecute.vfree5  ,so_saleinvoice_b.ccalbodyid  ,so_saleinvoice_b.coriginalbillcode  ,so_saleinvoice_b.cupsourcebillcode  ,so_saleinvoice_b.cquoteunitid,so_saleinvoice_b.nquotenumber,so_saleinvoice_b.nquoricurpri,so_saleinvoice_b.nquoricurtaxpri,so_saleinvoice_b.nquoricurnetpri,  so_saleinvoice_b.nqocurtaxnetpri,so_saleinvoice_b.nquprice,so_saleinvoice_b.nqutaxprice,so_saleinvoice_b.nqunetprice,so_saleinvoice_b.nqutaxnetprice,  so_saleinvoice_b.nsubqupri,so_saleinvoice_b.nsubqutaxpri,so_saleinvoice_b.nsubqunetpri,so_saleinvoice_b.nsubqutaxnetpri,so_saleinvoice_b.nsubsummny,so_saleinvoice_b.nsubtaxnetprice,so_saleinvoice_b.cprolineid ,so_saleinvoice_b.nsubcursummny,so_saleinvoice_b.nquoteunitrate,so_saleinvoice_b.ccustomerid  from so_saleinvoice_b left outer join so_saleexecute on so_saleinvoice_b.cinvoice_bid=so_saleexecute.csale_bid where so_saleinvoice_b.csaleid = ? ";

    if ((whereString != null) && (whereString.trim().length() > 0)) {
      sql = sql + whereString;
    }
    sql = sql + " order by cinvoice_bid ";
    SaleinvoiceBVO[] saleItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleinvoiceBVO saleItem = new SaleinvoiceBVO();

        String cinvoice_bid = rs.getString("cinvoice_bid");
        saleItem.setCinvoice_bid(cinvoice_bid == null ? null : cinvoice_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPk_corp(ccorpid == null ? null : ccorpid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        BigDecimal nbalancenumber = (BigDecimal)rs.getObject("nbalancenumber");

        saleItem.setNbalancenumber(nbalancenumber == null ? null : new UFDouble(nbalancenumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String cupreceipttype = rs.getString("cupreceipttype");
        saleItem.setCupreceipttype(cupreceipttype == null ? null : cupreceipttype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        saleItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillbodyid = rs.getString("cupsourcebillbodyid");

        saleItem.setCupsourcebillbodyid(cupsourcebillbodyid == null ? null : cupsourcebillbodyid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        BigDecimal nsimulatecostmny = (BigDecimal)rs.getObject("nsimulatecostmny");

        saleItem.setNsimulatecostmny(nsimulatecostmny == null ? null : new UFDouble(nsimulatecostmny));

        BigDecimal ncostmny = (BigDecimal)rs.getObject("ncostmny");
        saleItem.setNcostmny(ncostmny == null ? null : new UFDouble(ncostmny));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String ct_manageid = rs.getString("ct_manageid");
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts));

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String crowno = rs.getString("crowno");
        saleItem.setrowno(crowno == null ? null : crowno.trim());

        String vfree1 = rs.getString("vfree1");
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String ccalbodyid = rs.getString("ccalbodyid");
        saleItem.setCadvisecalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String coriginalbillcode = rs.getString("coriginalbillcode");
        saleItem.setCoriginalbillcode(coriginalbillcode == null ? null : coriginalbillcode.trim());

        String cupsourcebillcode = rs.getString("cupsourcebillcode");
        saleItem.setCupsourcebillcode(cupsourcebillcode == null ? null : cupsourcebillcode.trim());

        String cquoteunitid = rs.getString("cquoteunitid");
        saleItem.setCquoteunitid(cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquotenumber = (BigDecimal)rs.getObject("nquotenumber");

        saleItem.setNquotenumber(nquotenumber == null ? null : new UFDouble(nquotenumber));

        BigDecimal nquoricurpri = (BigDecimal)rs.getObject("nquoricurpri");

        saleItem.setNquoteoriginalcurprice(nquoricurpri == null ? null : new UFDouble(nquoricurpri));

        BigDecimal nquoricurtaxpri = (BigDecimal)rs.getObject("nquoricurtaxpri");

        saleItem.setNquoteoriginalcurtaxprice(nquoricurtaxpri == null ? null : new UFDouble(nquoricurtaxpri));

        BigDecimal nquoricurnetpri = (BigDecimal)rs.getObject("nquoricurnetpri");

        saleItem.setNquoteoriginalcurnetprice(nquoricurnetpri == null ? null : new UFDouble(nquoricurnetpri));

        BigDecimal nqocurtaxnetpri = (BigDecimal)rs.getObject("nqocurtaxnetpri");

        saleItem.setNquoteoriginalcurtaxnetprice(nqocurtaxnetpri == null ? null : new UFDouble(nqocurtaxnetpri));

        BigDecimal nquprice = (BigDecimal)rs.getObject("nquprice");
        saleItem.setNquoteprice(nquprice == null ? null : new UFDouble(nquprice));

        BigDecimal nqutaxprice = (BigDecimal)rs.getObject("nqutaxprice");

        saleItem.setNquotetaxprice(nqutaxprice == null ? null : new UFDouble(nqutaxprice));

        BigDecimal nqunetprice = (BigDecimal)rs.getObject("nqunetprice");

        saleItem.setNquotenetprice(nqunetprice == null ? null : new UFDouble(nqunetprice));

        BigDecimal nqutaxnetprice = (BigDecimal)rs.getObject("nqutaxnetprice");

        saleItem.setNquotetaxnetprice(nqutaxnetprice == null ? null : new UFDouble(nqutaxnetprice));

        BigDecimal nsubqupri = (BigDecimal)rs.getObject("nsubqupri");
        saleItem.setNsubquoteprice(nsubqupri == null ? null : new UFDouble(nsubqupri));

        BigDecimal nsubqutaxpri = (BigDecimal)rs.getObject("nsubqutaxpri");

        saleItem.setNsubquotetaxprice(nsubqutaxpri == null ? null : new UFDouble(nsubqutaxpri));

        BigDecimal nsubqunetpri = (BigDecimal)rs.getObject("nsubqunetpri");

        saleItem.setNsubquotenetprice(nsubqunetpri == null ? null : new UFDouble(nsubqunetpri));

        BigDecimal nsubqutaxnetpri = (BigDecimal)rs.getObject("nsubqutaxnetpri");

        saleItem.setNsubquotetaxnetprice(nsubqutaxnetpri == null ? null : new UFDouble(nsubqutaxnetpri));

        BigDecimal nsubsummny = (BigDecimal)rs.getObject("nsubsummny");
        saleItem.setNsubsummny(nsubsummny == null ? null : new UFDouble(nsubsummny));

        BigDecimal nsubtaxnetprice = (BigDecimal)rs.getObject("nsubtaxnetprice");

        saleItem.setNsubtaxnetprice(nsubtaxnetprice == null ? null : new UFDouble(nsubtaxnetprice));

        String cprolineid = rs.getString("cprolineid");
        saleItem.setCprolineid(cprolineid == null ? null : cprolineid.trim());

        BigDecimal nsubcursummny = (BigDecimal)rs.getObject("nsubcursummny");

        saleItem.setNsubcursummny(nsubcursummny == null ? null : new UFDouble(nsubcursummny));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject("nquoteunitrate");

        saleItem.setNquoteunitrate(nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        String ccustomerid = rs.getString("ccustomerid");
        saleItem.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        v.addElement(saleItem);
      }
      saleItems = new SaleinvoiceBVO[v.size()];
      if (v.size() > 0) {
        v.copyInto(saleItems);
      }
      queryFollowBody(saleItems);
    }
    catch (Exception e)
    {
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

    afterCallMethod("nc.bs.so.so002.SaleinvoiceDMO", "queryBodyData2", new Object[] { key });

    return saleItems;
  }

  protected Object getBeanHome(Class homeClass, String beanName)
    throws NamingException
  {
    Object objref = getInitialContext().lookup("java:comp/env/ejb/" + beanName);
    return PortableRemoteObject.narrow(objref, homeClass);
  }

  protected InitialContext getInitialContext()
    throws NamingException
  {
    return new InitialContext();
  }

  public void renovateAR(IBillInvokeCreditManager armanager, SaleinvoiceVO ordvo)
    throws BusinessException
  {
    SaleinvoiceVO saleorder = ordvo;

    if ((saleorder == null) || (armanager == null)) {
      return;
    }
    BillCreditOriginVO billcreditvo = null;
    UFDate date = ((SaleVO)saleorder.getParentVO()).getDmakedate();

    if ((saleorder.getIAction() == 0) || (saleorder.getIAction() == 10))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
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
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 1;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());
    }
    else if ((saleorder.getIAction() == 2) || (saleorder.getIAction() == 11))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 2;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 3)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 16;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 4)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 17;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 12)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 9;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 5)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 14;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());
    }
    else if (saleorder.getIAction() == 9)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
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
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 5;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 14)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 10;
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

  public AccountMnyVO[] getAccounMmnyVO(IBillInvokeCreditManager armanager, SaleinvoiceVO ordvo)
    throws BusinessException
  {
    SaleinvoiceVO saleorder = ordvo;

    if ((saleorder == null) || (armanager == null)) {
      return null;
    }
    BillCreditOriginVO billcreditvo = null;
    UFDate date = ((SaleVO)saleorder.getParentVO()).getDmakedate();

    if ((saleorder.getIAction() == 0) || (saleorder.getIAction() == 10))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
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
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 1;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());
    }
    else if ((saleorder.getIAction() == 2) || (saleorder.getIAction() == 11))
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 2;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 3)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 16;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 4)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 17;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 12)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 9;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 5)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = (saleorder.getAllSaleOrderVO() == null ? saleorder.getParentVO() : saleorder.getAllSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_b = (saleorder.getAllSaleOrderVO() == null ? saleorder.getChildrenVO() : saleorder.getAllSaleOrderVO().getChildrenVO());

      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 14;
      billcreditvo.m_voBill_init = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getParentVO());

      billcreditvo.m_voBill_init_b = (saleorder.getOldSaleOrderVO() == null ? null : saleorder.getOldSaleOrderVO().getChildrenVO());
    }
    else if (saleorder.getIAction() == 9)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
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
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 5;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }
    else if (saleorder.getIAction() == 14)
    {
      billcreditvo = new BillCreditOriginVO();
      billcreditvo.m_iBillType = 2;
      billcreditvo.m_voBill = saleorder.getParentVO();
      billcreditvo.m_voBill_b = saleorder.getChildrenVO();
      billcreditvo.m_dActDate = date;
      billcreditvo.m_iBillAct = 10;
      billcreditvo.m_voBill_init = null;
      billcreditvo.m_voBill_init_b = null;
    }

    if (billcreditvo == null) {
      return null;
    }
    try
    {
      billcreditvo.sOperatorid = saleorder.getOperatorid();
      return armanager.getAccountMnyVO(billcreditvo);
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public static UFDouble add(UFDouble d0, UFDouble d1)
  {
    if ((d0 == null) && (d1 == null))
      return null;
    d0 = d0 == null ? SoVoConst.duf0 : d0;
    d1 = d1 == null ? SoVoConst.duf0 : d1;

    return d0.add(d1);
  }

  public SaleinvoiceVO autoUniteInvoice(SaleinvoiceVO saleinvoice)
    throws SQLException
  {
    Hashtable hsar = saleinvoice.getHsArsubAcct();
    if ((hsar != null) && (hsar.size() > 0)) {
      return saleinvoice;
    }
    if (hsar == null)
      hsar = new Hashtable();
    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();
    SaleVO head = (SaleVO)saleinvoice.getParentVO();
    if ((items == null) || (items.length == 0)) {
      return saleinvoice;
    }

    UFBoolean so36 = new UFBoolean(true);
    UFBoolean so27 = new UFBoolean(false);
    UFBoolean BD302 = new UFBoolean(false);
    UFDouble so22 = new UFDouble(100);
    UFBoolean so48 = new UFBoolean(false);
    try {
      SysInitDMO initdmo = new SysInitDMO();
      so36 = initdmo.getParaBoolean(head.getPk_corp(), "SO36");
      so27 = initdmo.getParaBoolean(head.getPk_corp(), "SO27");
      BD302 = initdmo.getParaBoolean(head.getPk_corp(), "BD302");
      so22 = initdmo.getParaDbl(head.getPk_corp(), "SO22");
      so48 = initdmo.getParaBoolean(head.getPk_corp(), "SO49");

      if (so22 == null)
        so22 = new UFDouble(100);
      so22 = so22.div(100.0D);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      SCMEnv.out("not problem, the SysInitDMO occour problem, and has default param value! ");
    }

    if (((so48 == null) || (!so48.booleanValue())) && ((head.getCsaleid() == null) || (head.getCsaleid().trim().equals("")))) {
      return saleinvoice;
    }
    String wheres = " pk_corp='" + head.getPk_corp() + "'" + " and ccurrencytypeid='" + items[0].getCcurrencytypeid() + "'" + " and ( csourcebilltype  in ('3H','3G') or csourcebilltype = '" + "#&" + "' )  " + " and so_arsub.ccustomerid='" + items[0].getCcustomerid() + "'";

    if ((so36 != null) && (so36.booleanValue())) {
      wheres = wheres + " and (so_arsubacct.cbiztypeid='" + head.getCbiztype() + "') ";
    }
    else
    {
      wheres = wheres + " and (so_arsubacct.cbiztypeid='" + head.getCbiztype() + "' or so_arsubacct.cbiztypeid = '" + "#123456789*123456789" + "') ";
    }

    if ((so27 != null) && (so27.booleanValue()) && 
      (items[0].getCprolineid() != null) && (items[0].getCprolineid().trim().length() > 0)) {
      wheres = wheres + " and so_arsubacct.cproducelineid='" + items[0].getCprolineid() + "'";
    }
    wheres = wheres + " and so_arsub.bsubover='Y' and so_arsub.binvoiceover='N' and so_arsub.fstatus = 2 and so_arsub.dr=0 ";
    try
    {
      ArsubDMOImpl dmo = new ArsubDMOImpl();
      ARSubUniteVO[] accts = (ARSubUniteVO[])(ARSubUniteVO[])dmo.queryAllAcctHeadData(wheres);

      if ((accts == null) || (accts.length == 0)) {
        return saleinvoice;
      }
      int lastinfrow = 0;
      UFDouble totacctmny = new UFDouble(0);

      for (int i = 0; i < accts.length; i++) {
        Object temp = accts[i].getNsubmny();
        UFDouble money = temp == null ? new UFDouble(0) : (UFDouble)temp;
        temp = accts[i].getNtotalsubmny();
        UFDouble ntotalsubmny = temp == null ? new UFDouble(0) : (UFDouble)temp;

        accts[i].setNsubmny(money.sub(ntotalsubmny));
        if (accts[i].getNsubmny().doubleValue() > 0.0D)
          totacctmny = totacctmny.add(accts[i].getNsubmny());
      }
      if (totacctmny.doubleValue() <= 0.0D) {
        return saleinvoice;
      }
      UFDouble totinvmny = new UFDouble(0);
      for (int i = 0; i < items.length; i++) {
        if (((items[i].getBlargessflag() != null) && (items[i].getBlargessflag().booleanValue())) || (isLaborOrDiscount(items[i].getCinvbasdocid())))
          continue;
        if (items[i].getNoriginalcursummny() != null) {
          totinvmny = totinvmny.add(items[i].getNoriginalcursummny());
          lastinfrow = i;
        }
      }
      if (totinvmny.doubleValue() <= 0.0D) {
        return saleinvoice;
      }

      UFDouble thissubtotalmny = totinvmny.multiply(so22);
      if (thissubtotalmny.doubleValue() > totacctmny.doubleValue()) {
        thissubtotalmny = totacctmny;
      }

      BusinessCurrencyRateUtil currtype = new BusinessCurrencyRateUtil(head.getPk_corp());
      CurrinfoVO currVO = null;
      CurrinfoVO localcurrVO = null;
      CurrinfoVO astcurrVO = null;
      try
      {
        currVO = currtype.getCurrinfoVO(head.getCcurrencyid(), null);
        if ((currVO == null) || (currVO.getPk_currtype() == null) || (currVO.getPk_currtype().trim().length() <= 0)) {
          currVO = currtype.getCurrinfoVO(head.getCcurrencyid(), null);
        }

        localcurrVO = currtype.getCurrinfoVO(currtype.getLocalCurrPK(), null);

        if ((BD302 != null) && (BD302.booleanValue()))
          astcurrVO = currtype.getCurrinfoVO(currtype.getFracCurrPK(), null);
      }
      catch (Exception ex)
      {
        try {
          currVO = currtype.getCurrinfoVO(head.getCcurrencyid(), null);

          localcurrVO = currtype.getCurrinfoVO(currtype.getLocalCurrPK(), null);

          if ((BD302 == null) || (!BD302.booleanValue()))
            astcurrVO = currtype.getCurrinfoVO(currtype.getFracCurrPK(), null);
        }
        catch (Exception exx) {
          SCMEnv.out("currVO.getRatedigit().intValue() erro!");
        }
      }

      int digit = 2;

      int localdigit = 2;

      int astdigit = 2;
      try
      {
        digit = currVO.getCurrdigit() == null ? 2 : currVO.getCurrdigit().intValue();
        localdigit = localcurrVO.getCurrdigit() == null ? 2 : localcurrVO.getCurrdigit().intValue();
        if (astcurrVO != null)
          astdigit = astcurrVO.getCurrdigit() == null ? 2 : astcurrVO.getCurrdigit().intValue();
        else {
          astdigit = digit;
        }
        SCMEnv.out(digit);
      } catch (Exception ex2) {
        digit = 2;
        SCMEnv.out("digit = currVO.getCurrdigit().intValue() erro!");
      }

      thissubtotalmny = thissubtotalmny.setScale(digit, 0);

      ArrayList argen = new ArrayList();
      UFDouble tempdb = new UFDouble(0);
      UFDouble thisexe = null;
      for (int i = 0; i < accts.length; i++) {
        if ((accts[i].getNsubmny() != null) && (accts[i].getNsubmny().doubleValue() > 0.0D)) {
          if (tempdb.add(accts[i].getNsubmny()).doubleValue() > thissubtotalmny.doubleValue()) {
            thisexe = thissubtotalmny.sub(tempdb);
            accts[i].setNsubmny(thisexe);
          } else {
            thisexe = accts[i].getNsubmny();
          }
          tempdb = tempdb.add(thisexe);
          argen.add(accts[i]);
          if (tempdb.doubleValue() >= thissubtotalmny.doubleValue()) {
            break;
          }
        }
      }
      ARSubUniteVO[] retVOs = new ARSubUniteVO[argen.size()];
      argen.toArray(retVOs);

      if ((retVOs == null) || (retVOs.length == 0))
        return saleinvoice;
      Hashtable hsArsub = new Hashtable();
      Hashtable hsTotalBykey = new Hashtable();
      Hashtable hsSelectedARSubHVO = new Hashtable();
      for (int i = 0; i < retVOs.length; i++) {
        String key = retVOs[i].getCproducelineid() == null ? new Integer(i).toString() : retVOs[i].getCproducelineid();
        if (hsArsub.containsKey(key))
          hsArsub.put(key, ((UFDouble)hsArsub.get(key)).add(retVOs[i].getNsubmny()));
        else
          hsArsub.put(key, retVOs[i].getNsubmny());
        String arsubacctkey = retVOs[i].getPrimaryKey();
        if (hsTotalBykey.containsKey(arsubacctkey))
          hsTotalBykey.put(arsubacctkey, ((UFDouble)hsTotalBykey.get(arsubacctkey)).add(retVOs[i].getNsubmny()));
        else {
          hsTotalBykey.put(arsubacctkey, retVOs[i].getNsubmny());
        }
      }

      for (int j = 0; j < retVOs.length; j++) {
        String key = retVOs[j].getPrimaryKey();
        if (hsSelectedARSubHVO.containsKey(key))
          hsSelectedARSubHVO.put(key, ((UFDouble)hsSelectedARSubHVO.get(key)).add(retVOs[j].getNsubmny()));
        else {
          hsSelectedARSubHVO.put(key, retVOs[j].getNsubmny());
        }

      }

      saleinvoice.setHsSelectedARSubHVO(hsSelectedARSubHVO);
      saleinvoice.setBstrikeflag(new UFBoolean(true));
      saleinvoice.setHsArsubAcct(hsTotalBykey);

      UFDouble remainMoney = thissubtotalmny;
      for (int i = 0; i < items.length; i++) {
        if (((items[i].getBlargessflag() != null) && (items[i].getBlargessflag().booleanValue())) || (isLaborOrDiscount(items[i].getCinvbasdocid())))
          continue;
        UFDouble money = items[i].getNoriginalcursummny();
        UFDouble changemoney = money.multiply(thissubtotalmny).div(totinvmny);
        changemoney = changemoney.setScale(digit, 4);
        if (i == lastinfrow) {
          changemoney = remainMoney;
        }
        money = money.sub(changemoney);

        remainMoney = remainMoney.sub(changemoney);
        items[i].setNoriginalcursummny(money);

        items[i].setNoriginalcurtaxnetprice(div(items[i].getNoriginalcursummny(), items[i].getNnumber()));

        items[i].setNoriginalcurtaxmny(div(mult(items[i].getNoriginalcursummny(), items[i].getNtaxrate().div(100.0D)), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNoriginalcurmny(sub(items[i].getNoriginalcursummny(), items[i].getNoriginalcurtaxmny()));

        items[i].setNoriginalcurprice(div(items[i].getNoriginalcurtaxprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNoriginalcurnetprice(div(items[i].getNoriginalcurtaxnetprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNitemdiscountrate(div(items[i].getNoriginalcurtaxnetprice(), mult(items[i].getNoriginalcurtaxprice(), items[i].getNdiscountrate().div(100.0D))));

        items[i].setNitemdiscountrate(mult(items[i].getNitemdiscountrate(), new UFDouble(100)));

        items[i].setNdiscountmny(sub(mult(items[i].getNoriginalcurtaxprice(), items[i].getNnumber()), items[i].getNoriginalcursummny()));

        items[i].setNtaxnetprice(mult(items[i].getNtaxprice(), mult(div(items[i].getNdiscountrate(), new UFDouble(100)), div(items[i].getNitemdiscountrate(), new UFDouble(100)))));

        items[i].setNsummny(mult(items[i].getNtaxnetprice(), items[i].getNnumber()));

        items[i].setNtaxmny(div(mult(items[i].getNsummny(), items[i].getNtaxrate().div(100.0D)), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNnetprice(div(items[i].getNtaxnetprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNprice(div(items[i].getNtaxprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNmny(mult(items[i].getNnetprice(), items[i].getNnumber()));

        items[i].setNoriginalcurdiscountmny(sub(items[i].getNsubcursummny(), items[i].getNoriginalcursummny()));

        items[i].setNquoteoriginalcurtaxnetprice(mult(items[i].getNquoteoriginalcurtaxprice(), mult(div(items[i].getNdiscountrate(), new UFDouble(100)), div(items[i].getNitemdiscountrate(), new UFDouble(100)))));

        items[i].setNquoteoriginalcurnetprice(div(items[i].getNquoteoriginalcurtaxnetprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNquoteoriginalcurprice(div(items[i].getNquoteoriginalcurtaxprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNquotetaxnetprice(mult(items[i].getNquotetaxprice(), mult(div(items[i].getNdiscountrate(), new UFDouble(100)), div(items[i].getNitemdiscountrate(), new UFDouble(100)))));

        items[i].setNquotenetprice(div(items[i].getNquotetaxnetprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));

        items[i].setNquoteprice(div(items[i].getNquotetaxprice(), add(new UFDouble(1), items[i].getNtaxrate().div(100.0D))));
      }

      head.setNstrikemny(thissubtotalmny);
      head.setNnetmny(sub(head.getNtotalsummny(), thissubtotalmny));
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new SQLException("销售发票自动合并开票发生异常！" + e.getMessage());
    }

    saleinvoice.setAllinvoicevo(saleinvoice);
    return saleinvoice;
  }

  public UFDouble calc(SaleinvoiceBVO item, String firstcol, String seccol, int calctype)
  {
    UFDouble result = null;

    UFDouble d1 = (UFDouble)item.getAttributeValue(firstcol);
    UFDouble d2 = (UFDouble)item.getAttributeValue(seccol);
    if (calctype == 0)
      result = add(d1, d2);
    else if (calctype == 1)
      result = sub(d1, d2);
    else if (calctype == 2)
      result = mult(d1, d2);
    else if (calctype == 3) {
      result = div(d1, d2);
    }
    return result;
  }

  public void checkArsubAcctWrite(String carsubacctid) throws BusinessException {
    try {
      SmartDMO sdmo = new SmartDMO();
      String sql = "select narsubinvmny,narsubmny from so_arsubacct where carsubacctid = '" + carsubacctid + "' and dr=0 ";
      Object[] objs = sdmo.selectBy2(sql);
      if ((objs == null) || (objs.length == 0)) {
        throw new BusinessException("冲应收单合并开票并发错误，冲应收单冲减明细已经被删除或作废！不能进行合并开票！");
      }
      Object[] temps = (Object[])(Object[])objs[0];
      UFDouble invmny = (UFDouble)temps[0];
      UFDouble submny = (UFDouble)temps[1];
      if (invmny == null)
        invmny = new UFDouble(0);
      if (submny == null) {
        submny = new UFDouble(0);
      }
      submny = submny.setScale(2, 0);
      invmny = invmny.setScale(2, 0);
      if (submny.doubleValue() < invmny.doubleValue())
        throw new BusinessException("冲应收单合并开票并发错误，冲应收单累计合并开票金额已经大于可冲减金额！不能进行合并开票！");
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  public static UFDouble div(UFDouble d0, UFDouble d1) {
    if ((d0 == null) || (d1 == null)) {
      return null;
    }
    if (d1.doubleValue() == 0.0D) {
      return null;
    }
    return d0.div(d1);
  }

  public boolean isLaborOrDiscount(String cinvbasdocid)
  {
    try
    {
      SmartDMO sdmo = new SmartDMO();
      String sql = "select discountflag,laborflag from bd_invbasdoc where pk_invbasdoc='" + cinvbasdocid + "'";
      Object[] objs = sdmo.selectBy2(sql);
      if ((objs != null) && (objs.length > 0)) {
        Object[] temp = (Object[])(Object[])objs[0];
        UFBoolean isDiscount = new UFBoolean(temp[0].toString());
        UFBoolean isLabor = new UFBoolean(temp[1].toString());
        if ((isDiscount.booleanValue()) || (isLabor.booleanValue()))
          return true;
      }
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    return false;
  }

  public static UFDouble mult(UFDouble d0, UFDouble d1) {
    if ((d0 == null) || (d1 == null)) {
      return null;
    }
    return d0.multiply(d1);
  }

  public static UFDouble sub(UFDouble d0, UFDouble d1)
  {
    if ((d0 == null) && (d1 == null))
      return null;
    d0 = d0 == null ? SoVoConst.duf0 : d0;
    d1 = d1 == null ? SoVoConst.duf0 : d1;

    return d0.sub(d1);
  }

  public SaleinvoiceVO splitDisPart(SaleinvoiceVO inVO)
  {
    if ((inVO == null) || (inVO.getParentVO() == null) || (inVO.getChildrenVO() == null))
    {
      return null;
    }SaleVO header = (SaleVO)inVO.getParentVO();
    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])inVO.getChildrenVO();
    Vector al = new Vector();
    if ((items != null) && (items.length > 0)) {
      for (int i = 0; i < items.length; i++) {
        if (isLaborOrDiscount(items[i].getCinvbasdocid()))
          al.add(items[i]);
      }
    }
    if (al.size() > 0) {
      SaleinvoiceBVO[] body = new SaleinvoiceBVO[al.size()];
      al.copyInto(body);
      SaleinvoiceVO retvo = new SaleinvoiceVO();
      retvo.setParentVO(header);
      retvo.setChildrenVO(body);
      return retvo;
    }return null;
  }

  public SaleinvoiceVO delNotInvoiceLine(SaleinvoiceVO inVO)
  {
    if ((inVO == null) || (inVO.getParentVO() == null) || (inVO.getChildrenVO() == null))
    {
      return null;
    }SaleVO header = (SaleVO)inVO.getParentVO();
    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])inVO.getChildrenVO();
    Vector al = new Vector();
    if ((items != null) && (items.length > 0)) {
      for (int i = 0; i < items.length; i++) {
        if (isCanInvoice(items[i].getCinventoryid()))
          al.add(items[i]);
      }
    }
    if (al.size() > 0) {
      SaleinvoiceBVO[] body = new SaleinvoiceBVO[al.size()];
      al.copyInto(body);
      SaleinvoiceVO retvo = new SaleinvoiceVO();
      retvo.setParentVO(header);
      retvo.setChildrenVO(body);
      return retvo;
    }return null;
  }

  public boolean isCanInvoice(String cinvmandocid)
  {
    try
    {
      SmartDMO sdmo = new SmartDMO();
      String sql = "select iscansaleinvoice from bd_invmandoc where pk_invmandoc='" + cinvmandocid + "'";
      Object[] objs = sdmo.selectBy2(sql);
      if ((objs != null) && (objs.length > 0)) {
        Object[] temp = (Object[])(Object[])objs[0];
        UFBoolean isInvoice = new UFBoolean(temp[0].toString());
        if (isInvoice.booleanValue())
          return true;
      }
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    return false;
  }
  public SaleinvoiceVO fillOrignInvoice(SaleinvoiceVO inVO) {
    if ((inVO == null) || (inVO.getParentVO() == null) || (inVO.getChildrenVO() == null))
    {
      return null;
    }SaleVO header = (SaleVO)inVO.getParentVO();
    if (header.m_fcounteractflag.intValue() != 2) return inVO;
    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])inVO.getChildrenVO();
    SaleinvoiceVO oldVO = null;
    try {
      oldVO = queryData(items[0].getCupsourcebillid());
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
    }
    SaleinvoiceBVO[] items2 = (SaleinvoiceBVO[])(SaleinvoiceBVO[])oldVO.getChildrenVO();
    if ((items != null) && (items.length > 0)) {
      for (int i = 0; i < items.length; i++) {
        items[i].setCupreceipttype(items2[i].getCupreceipttype());
        items[i].setCupsourcebillid(items2[i].getCupsourcebillid());
        items[i].setCupsourcebillbodyid(items2[i].getCupsourcebillbodyid());
        items[i].setCupsourcebillcode(items2[i].getCupsourcebillcode());
      }
    }
    inVO.setChildrenVO(items);
    return inVO;
  }

  public SaleinvoiceVO[] splitInvoiceVO(SaleinvoiceVO inVO)
  {
    if ((inVO == null) || (inVO.getParentVO() == null) || (inVO.getChildrenVO() == null))
    {
      return null;
    }if (!"30".equals(inVO.getChildrenVO()[0].getAttributeValue("creceipttype"))) {
      SaleinvoiceVO[] allvo = new SaleinvoiceVO[1];
      allvo[0] = inVO;
      return allvo;
    }
    SaleinvoiceVO[] allvo = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", inVO, null, new String[] { "csourcebillid" });

    ArrayList hvolist = new ArrayList();
    ArrayList bvolist = new ArrayList();
    SOToolVO htoolvo = null;

    for (int i = 0; i < allvo.length; i++) {
      VOTools.setInvoiceHeadMny(allvo[i]);
      htoolvo = new SOToolVO();
      htoolvo.setAttributeValue("cfirstbillhid", allvo[i].getChildrenVO()[0].getAttributeValue("csourcebillid"));

      htoolvo.setAttributeValue("creceiptcorpid", null);
      hvolist.add(htoolvo);
    }
    SOToolVO[] htoolvos = (SOToolVO[])(SOToolVO[])hvolist.toArray(new SOToolVO[hvolist.size()]);
    String[] fs = null;
    fs = new String[] { "creceiptcorpid->getColValue(so_sale,creceiptcorpid,csaleid, cfirstbillhid )" };
    SoVoTools.execFormulasAtBs(fs, htoolvos);
    for (int i = 0; i < allvo.length; i++) {
      allvo[i].getParentVO().setAttributeValue("creceiptcorpid", (String)htoolvos[i].getAttributeValue("creceiptcorpid"));

      htoolvo = new SOToolVO();
      htoolvo.setAttributeValue("ccustomerid", (String)htoolvos[i].getAttributeValue("creceiptcorpid"));
      htoolvo.setAttributeValue("ccustbaseid", null);
      bvolist.add(htoolvo);
    }
    htoolvos = (SOToolVO[])(SOToolVO[])bvolist.toArray(new SOToolVO[bvolist.size()]);
    fs = new String[] { "ccustbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc, ccustomerid)" };
    SoVoTools.execFormulasAtBs(fs, htoolvos);
    hvolist.clear();
    for (int i = 0; i < allvo.length; i++) {
      htoolvo = new SOToolVO();
      htoolvo.setAttributeValue("ccustbaseid", (String)htoolvos[i].getAttributeValue("ccustbaseid"));
      htoolvo.setAttributeValue("vprintcustname", null);
      hvolist.add(htoolvo);
    }
    htoolvos = (SOToolVO[])(SOToolVO[])hvolist.toArray(new SOToolVO[hvolist.size()]);
    fs = new String[] { "vprintcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc, ccustbaseid  )" };
    SoVoTools.execFormulasAtBs(fs, htoolvos);
    hvolist.clear();
    for (int i = 0; i < allvo.length; i++) {
      allvo[i].getParentVO().setAttributeValue("vprintcustname", (String)htoolvos[i].getAttributeValue("vprintcustname"));
    }

    return allvo;
  }

  public AggregatedValueObject[] queryDataByWhere(String swhere)
    throws BusinessException, SQLException
  {
    CircularlyAccessibleValueObject[] hvos = queryAllHeadData(swhere);
    if ((hvos == null) || (hvos.length == 0)) return null;
    SaleinvoiceVO[] avos = new SaleinvoiceVO[hvos.length];
    for (int i = 0; i < avos.length; i++) {
      avos[i] = new SaleinvoiceVO();
      avos[i].setParentVO(hvos[i]);
      avos[i].setChildrenVO(queryBodyData(hvos[i].getPrimaryKey()));
    }
    return avos;
  }

  public HashSet filterUsers(String srcBilltype, String destBilltype, AggregatedValueObject billvo, RoleVO[] roles)
  {
    if ((billvo == null) || (billvo.getChildrenVO() == null) || (billvo.getChildrenVO().length == 0))
    {
      SCMEnv.out("billvo == null || billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0，直接返回");

      return null;
    }

    HashSet hashRet = new HashSet();
    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])billvo.getChildrenVO();
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

    return hashRet;
  }
}