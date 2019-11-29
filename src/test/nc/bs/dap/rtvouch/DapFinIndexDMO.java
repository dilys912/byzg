package nc.bs.dap.rtvouch;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import nc.bs.dap.out.IAccountRetVoucher;
import nc.bs.dap.pubfactory.CallInterface;
import nc.bs.fipf.pub.BsPubUtil;
import nc.bs.gl.pubinterface.IVoucherAudit;
import nc.bs.gl.pubinterface.IVoucherDelete;
import nc.bs.gl.pubinterface.IVoucherSave;
import nc.bs.gl.pubinterface.IVoucherSign;
import nc.bs.gl.pubinterface.IVoucherTally;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mw.sqltrans.TempTable;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.itf.dap.pub.IVoucherQuery;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.vo.dap.inteface.DetailVO;
import nc.vo.dap.pub.DapBusinessException;
import nc.vo.dap.pub.DapLoger;
import nc.vo.dap.queryplus.QueryLogVO;
import nc.vo.dap.rtvouch.DapExecTypeVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.fip.pub.Translator;
import nc.vo.fipf.pub.PfComm;
import nc.vo.gl.pubinterface.VoucherOperateInterfaceVO;
import nc.vo.gl.pubinterface.VoucherSaveInterfaceVO;
import nc.vo.gl.pubvoucher.OperationResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class DapFinIndexDMO extends DataManageObject
  implements IVoucherAudit, IVoucherDelete, IVoucherTally, IVoucherSave, IVoucherSign
{
  public static final int LOGNUM = 100;
  private String m_checkStr = null;

  private int m_iExecType = 0;

  private UFBoolean m_isGLProc = null;

  public static UFBoolean ubY = new UFBoolean("Y");

  public static UFBoolean ubN = new UFBoolean("N");

  private String PK_SYS = null;

  public final int BothBill = 2;

  public final int BusinessBill = 0;

  public final int BusinessBillCreat = 3;

  public final int VoucherBill = 1;

  private int m_destsystem = -1000;

  public DapFinIndexDMO()
    throws NamingException, SystemException
  {
  }

  public DapFinIndexDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public OperationResultVO[] afterAudit(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    return null;
  }

  public OperationResultVO[] afterDelete(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    return null;
  }

  public OperationResultVO[] afterSave(VoucherSaveInterfaceVO vo)
    throws BusinessException
  {
    Object objUserData = vo.userdata;
    if (objUserData == null) {
      return null;
    }
    String pkRtVouch = null;
    try
    {
      String[] pkAryRtVouch = (String[])null;
      if ((objUserData instanceof String[]))
        pkAryRtVouch = (String[])objUserData;
      else {
        return null;
      }
      boolean isPKRtVouch = true;
      if (pkAryRtVouch[(pkAryRtVouch.length - 1)].equals("PKRTVOUCH"))
        isPKRtVouch = true;
      else if (pkAryRtVouch[(pkAryRtVouch.length - 1)].equals("FININDEX"))
        isPKRtVouch = false;
      else {
        return null;
      }
      if (pkAryRtVouch.length < 30) {
        RtVouchDMO dmVouch = new RtVouchDMO();
        RtvouchBDMO dmVouchB = new RtvouchBDMO();
        for (int i = 0; i < pkAryRtVouch.length - 1; i++) {
          pkRtVouch = pkAryRtVouch[i];
          int retInt = updateVoucher(vo.voucher.getPk_voucher(), pkRtVouch, isPKRtVouch);
          if (retInt == 0) {
            throw new BusinessException(NCLangResOnserver.getInstance()
              .getStrByID("fidap", 
              "UPPfidap-000143"));
          }

          if ((pkRtVouch != null) && (isPKRtVouch)) {
            nc.vo.dap.inteface.VoucherVO vouchVo = new nc.vo.dap.inteface.VoucherVO();
            DetailVO[] voVouchB = (DetailVO[])null;
            vouchVo.setPk_voucher(pkRtVouch);
            dmVouch.delete(vouchVo);
            voVouchB = dmVouchB.queryByPkVouch(pkRtVouch);
            for (int j = 0; j < voVouchB.length; j++)
            {
              dmVouchB.delete(voVouchB[j]);
            }
          }
        }
        if (isPKRtVouch)
        {
          Hashtable retHas = queryBillDataByRtVouchORVoucher(pkAryRtVouch, null);
          if (retHas.size() == 0)
            return null;
          Enumeration e = retHas.keys();
          while (e.hasMoreElements()) {
            String key = String.valueOf(e.nextElement());
            Vector v = (Vector)(Vector)retHas.get(key);
            if (v.size() > 0) {
              String[] pkAry = new String[v.size()];
              v.copyInto(pkAry);
              runBackVoucher(key, pkAry, vo.voucher.getPk_voucher(), vo.voucher.getPk_glorg(), 
                vo.voucher.getPk_glbook(), vo.voucher.getPk_system(), vo.voucher.getPk_corp(), false);
            }
          }
        }
      } else {
        String tableName = createtmpTabQryByBillPks(pkAryRtVouch);
        int retInt = updateVoucher(vo.voucher.getPk_voucher(), tableName, isPKRtVouch, 0);
        if (retInt != pkAryRtVouch.length - 1) {
          throw new BusinessException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000143"));
        }
        pkRtVouch = pkAryRtVouch[0];
        if (isPKRtVouch) {
          RtVouchDMO dmVouch = new RtVouchDMO();
          RtvouchBDMO dmVouchB = new RtvouchBDMO();
          dmVouch.delete(tableName);
          dmVouchB.deleteByFK(tableName, 0);
        }
        if (isPKRtVouch)
        {
          Hashtable retHas = queryBillDataByRtVouchORVoucher(tableName, null);
          if (retHas.size() == 0)
            return null;
          Enumeration e = retHas.keys();
          while (e.hasMoreElements()) {
            String key = String.valueOf(e.nextElement());
            Vector v = (Vector)(Vector)retHas.get(key);
            if (v.size() > 0) {
              String[] pkAry = new String[v.size()];
              v.copyInto(pkAry);
              runBackVoucher(key, pkAry, vo.voucher.getPk_voucher(), vo.voucher.getPk_glorg(), 
                vo.voucher.getPk_glbook(), vo.voucher.getPk_system(), vo.voucher.getPk_corp(), false);
            }
          }
        }
      }
    } catch (Exception ex) {
      Logger.error("保存凭证出现错误！", ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }

    return null;
  }

  public OperationResultVO[] afterSign(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    return null;
  }

  public OperationResultVO[] afterTally(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    return null;
  }

  public OperationResultVO[] beforeDelete(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    String[] strVoucherPKAry = vo.pk_vouchers;
    Object userObj = vo.userdata;

    if (strVoucherPKAry == null) {
      return null;
    }
    if (userObj != null) {
      return null;
    }
    UFBoolean existFlag = null;
    String strVoucherPK = "";
    try {
      for (int i = 0; i < strVoucherPKAry.length; i++) {
        strVoucherPK = strVoucherPKAry[i];
        System.out.println("====================准备删除凭证:" + strVoucherPK);

        existFlag = isExistRelect(strVoucherPK);
        if (existFlag.booleanValue()) {
          if ((this.PK_SYS != null) && (this.PK_SYS.trim().equals("FTS".trim())) && 
            (this.m_destsystem == 0))
          {
            throw new BusinessException(NCLangResOnserver.getInstance()
              .getStrByID("fidap", 
              "UPPfidap-000146"));
          }
          if (this.m_isGLProc.booleanValue()) {
            throw new BusinessException(NCLangResOnserver.getInstance()
              .getStrByID("fidap", 
              "UPPfidap-000147"));
          }
          //反编译后报错，edit by yqq  2016-07-18
          Hashtable retHas = queryBillDataByRtVouchORVoucher(((String []) (null)), strVoucherPK);
   //     Hashtable retHas = queryBillDataByRtVouchORVoucher(null, strVoucherPK);

          int retInt = 0;

          retInt = updateRelectFlag(strVoucherPK, 0, 4, null, false, 
            null);
          if (retInt == 0) {
            throw new BusinessException(NCLangResOnserver.getInstance()
              .getStrByID("fidap", 
              "UPPfidap-000148"));
          }
          if (retHas.size() == 0)
            return null;
          Enumeration e = retHas.keys();
          while (e.hasMoreElements()) {
            String key = String.valueOf(e.nextElement());
            Vector v = (Vector)(Vector)retHas.get(key);
            if (v.size() > 0) {
              String[] pkAry = new String[v.size()];
              v.copyInto(pkAry);
              runBackVoucher(key, pkAry, strVoucherPK, null, null, null, null, true);
            }
          }
        }
      }
    } catch (Exception ex) {
      Logger.error("会计平台删除凭证接口出现错误！", ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }

    return null;
  }

  public OperationResultVO[] beforeSave(VoucherSaveInterfaceVO vo)
    throws BusinessException
  {
    return null;
  }

  public OperationResultVO[] beforeSign(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    String[] strVoucherPK = vo.pk_vouchers;
    Boolean isSign = vo.operatedirection;

    if (strVoucherPK == null) {
      return null;
    }

    try
    {
      Hashtable h = isExistRelect(strVoucherPK);
      DapFinMsgVO dfm = new DapFinMsgVO();
      for (int i = 0; i < strVoucherPK.length; i++)
      {
        boolean existFlag = h.containsKey(strVoucherPK[i]);
        if (existFlag) {
          dfm = (DapFinMsgVO)h.get(strVoucherPK[i]);
          String step = dfm.getVoucherType();

          if (isSign.booleanValue())
          {
            if ((step == null) || (step.trim().length() == 0)) {
              step = "W00";
            }
            else
            {
              step = "W" + step.substring(1);
              int fixlen = 3 - step.length();
              for (int f = 0; f < fixlen; f++) {
                step = step + '0';
              }
            }
            updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
          else
          {
            step = "0" + step.substring(1);
            int fixlen = 3 - step.length();
            for (int f = 0; f < fixlen; f++) {
              step = step + '0';
            }
            if ("000".equals(step))
              updateBackCheckState(strVoucherPK[i], "000", 4);
            else
              updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
        }
      }
    }
    catch (Exception ex) {
      Logger.error("会计平台签字凭证接口出现错误！", ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }

    return null;
  }

  public OperationResultVO[] beforeAudit(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    String[] strVoucherPK = vo.pk_vouchers;
    Boolean isAudit = vo.operatedirection;

    if (strVoucherPK == null) {
      return null;
    }

    try
    {
      Hashtable h = isExistRelect(strVoucherPK);
      DapFinMsgVO dfm = new DapFinMsgVO();
      for (int i = 0; i < strVoucherPK.length; i++)
      {
        boolean existFlag = h.containsKey(strVoucherPK[i]);
        if (existFlag) {
          dfm = (DapFinMsgVO)h.get(strVoucherPK[i]);
          String step = dfm.getVoucherType();

          if (isAudit.booleanValue())
          {
            if ((step != null) && (step.trim().length() > 0)) {
              step = step.substring(0, 1) + "C0";
            }
            else
            {
              step = "0C0";
            }
            updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
          else
          {
            step = step.substring(0, 1) + "00";
            if ("000".equals(step))
              updateBackCheckState(strVoucherPK[i], "000", 4);
            else
              updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
        }
      }
    }
    catch (Exception ex) {
      Logger.error("会计平台审核凭证接口出现错误！", ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }

    return null;
  }

  public OperationResultVO[] beforeTally(VoucherOperateInterfaceVO vo)
    throws BusinessException
  {
    String[] strVoucherPK = vo.pk_vouchers;
    Object userObj = vo.userdata;
    Boolean isTally = vo.operatedirection;

    if (strVoucherPK == null) {
      return null;
    }
    if (userObj != null) {
      return null;
    }
    try
    {
      Hashtable h = isExistRelect(strVoucherPK);
      DapFinMsgVO dfm = new DapFinMsgVO();
      for (int i = 0; i < strVoucherPK.length; i++)
      {
        boolean existFlag = h.containsKey(strVoucherPK[i]);
        if (existFlag) {
          dfm = (DapFinMsgVO)h.get(strVoucherPK[i]);
          String step = dfm.getVoucherType();

          if (isTally.booleanValue())
          {
            if ((step == null) || (step.trim().length() == 0)) {
              step = "00J";
            }
            else
            {
              step = step.trim();
              int len = step.trim().length();
              if (len < 3) {
                if (len == 1)
                  step = step + "00";
                else {
                  step = step + "0";
                }
              }
              step = step.substring(0, 2) + "J";
            }
            updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
          else
          {
            step = step.substring(0, 2) + "0";
            if ("000".equals(step))
              updateBackCheckState(strVoucherPK[i], "000", 4);
            else
              updateRelectFlag(strVoucherPK[i], 5, 4, step, true);
          }
        }
      }
    }
    catch (Exception ex) {
      Logger.error("会计平台记帐凭证接口出现错误！", ex);
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
      throw new BusinessException(ex.getMessage());
    }

    return null;
  }

  private String createtmpTabQryByBillPks(String[] sBillPks)
    throws SQLException
  {
    TempTable tt = new TempTable();
    Connection con = null;
    PreparedStatement stmt = null;
    String sTabName = "tp_dapbillfield_glvoucher";
    try {
      con = getConnection();
      if (con.getAutoCommit()) {
        con.setAutoCommit(false);
      }
      tt.setRowsnum(sBillPks.length);
      sTabName = tt.createTempTable(con, sTabName, " pk_bill char(20) not null ", " pk_bill ");
      String tmpInsertSql = "insert into " + sTabName + " (pk_bill) values(?)";

      ((CrossDBConnection)con).setSqlTrans(false);
      ((CrossDBConnection)con).setAddTimeStamp(false);
      stmt = prepareStatement(con, tmpInsertSql);
      for (int i = 0; i < sBillPks.length; i++) {
        if ((sBillPks[i] == null) || (sBillPks[i].trim().length() <= 0)) {
          continue;
        }
        stmt.setString(1, sBillPks[i]);
        executeUpdate(stmt);
      }
      executeBatch(stmt);
      stmt.close();
      ((CrossDBConnection)con).setSqlTrans(true);
    } catch (Exception ex) {
      Logger.error("会计平台创建临时表出现错误！！", ex);
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000050"));
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    return sTabName;
  }

  private String createtmpTabQryByBillPks(Properties p)
    throws SQLException
  {
    TempTable tt = new TempTable();
    Connection con = null;
    PreparedStatement stmt = null;
    String sTabName = "tp_dapbill2Field";
    try {
      con = getConnection();
      if (con.getAutoCommit()) {
        con.setAutoCommit(false);
      }
      tt.setRowsnum(p.size());
      sTabName = tt.createTempTable(con, sTabName, " pk_bill char(20) not null ,pk_bill1 char(20) not null", 
        " pk_bill ");
      String tmpInsertSql = "insert into " + sTabName + " (pk_bill,pk_bill1) values(?,?)";

      ((CrossDBConnection)con).setSqlTrans(false);
      ((CrossDBConnection)con).setAddTimeStamp(false);
      stmt = prepareStatement(con, tmpInsertSql);
      Enumeration enums = p.propertyNames();
      String pname = "";
      while (enums.hasMoreElements()) {
        pname = (String)enums.nextElement();
        stmt.setString(1, pname);
        stmt.setString(2, (String)p.get(pname));
        executeUpdate(stmt);
      }
      executeBatch(stmt);
      stmt.close();
      ((CrossDBConnection)con).setSqlTrans(true);
    } catch (Exception ex) {
      Logger.error("会计平台创建临时表出现错误！！", ex);
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000050"));
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    return sTabName;
  }

  public void delete(DapFinMsgVO voMsg)
    throws SQLException
  {
    String strTemp = "delete from dap_finindex ";
    String strWhere = "";

    System.out.println("开始删除对照表记录===============>>对照表记录主键为:" + voMsg.getPkFinmsg());
    if ((voMsg.getPkFinmsg() == null) && (
      (voMsg.getSys() == null) || (voMsg.getProcMsg() == null) || (voMsg.getProc() == null) || (voMsg.getCorp() == null))) {
      Logger.info("数据删除存在危险!\nvoMsg.getPkFinmsg()=" + voMsg.getPkFinmsg() + "\n" + "voMsg.getSys()=" + 
        voMsg.getSys() + "\n" + "voMsg.getProcMsg()=" + voMsg.getProcMsg() + "\n" + "voMsg.getProc()=" + 
        voMsg.getProc() + "\n" + "voMsg.getCorp()=" + voMsg.getCorp() + "\n");
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000153"));
    }

    if (voMsg.getPkFinmsg() != null) {
      strWhere = strWhere + "and pk_finindex=? ";
    }

    if (voMsg.getProcMsg() != null) {
      strWhere = strWhere + "and procMsg=? ";
    }

    if (voMsg.getBillCode() != null) {
      strWhere = strWhere + "and billcode=? ";
    }

    if (voMsg.getSys() != null) {
      strWhere = strWhere + "and pk_sys=? ";
    }

    if (voMsg.getProc() != null) {
      strWhere = strWhere + "and pk_proc=? ";
    }

    if (voMsg.getBusiType() != null) {
      strWhere = strWhere + "and pk_busitype=? ";
    }

    if (voMsg.getCorp() != null) {
      strWhere = strWhere + "and pk_corp=? ";
    }

    if (voMsg.getDestSystem() > -1) {
      strWhere = strWhere + "and destsystem=? ";
    }

    if (strWhere.length() > 0)
      strWhere = "where " + strWhere.substring(3, strWhere.length() - 1);
    else {
      strWhere = "";
    }

    strTemp = strTemp + strWhere;

    int i = 1;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      if (voMsg.getPkFinmsg() != null) {
        stmt.setString(i++, voMsg.getPkFinmsg());
      }

      if (voMsg.getProcMsg() != null) {
        stmt.setString(i++, voMsg.getProcMsg());
      }

      if (voMsg.getBillCode() != null) {
        stmt.setString(i++, voMsg.getBillCode());
      }

      if (voMsg.getSys() != null) {
        stmt.setString(i++, voMsg.getSys());
      }

      if (voMsg.getProc() != null) {
        stmt.setString(i++, voMsg.getProc());
      }

      if (voMsg.getBusiType() != null) {
        stmt.setString(i++, voMsg.getBusiType());
      }

      if (voMsg.getCorp() != null) {
        stmt.setString(i++, voMsg.getCorp());
      }

      if (voMsg.getDestSystem() > -1) {
        stmt.setInt(i++, voMsg.getDestSystem());
      }
      stmt.executeUpdate();

      stmt.close();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public int deleteByVO(DapFinMsgVO voMsg)
    throws SQLException
  {
    int resInt = 0;
    String strTemp = "delete from dap_finindex ";
    String strWhere = " flag<4";

    if ((voMsg.getPkFinmsg() == null) && (
      (voMsg.getSys() == null) || (voMsg.getProcMsg() == null) || (voMsg.getProc() == null) || (voMsg.getCorp() == null))) {
      Logger.info("数据删除存在危险!\nvoMsg.getPkFinmsg()=" + voMsg.getPkFinmsg() + "\n" + "voMsg.getSys()=" + 
        voMsg.getSys() + "\n" + "voMsg.getProcMsg()=" + voMsg.getProcMsg() + "\n" + "voMsg.getProc()=" + 
        voMsg.getProc() + "\n" + "voMsg.getCorp()=" + voMsg.getCorp() + "\n");
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000153"));
    }

    if (voMsg.getPkFinmsg() != null) {
      strWhere = strWhere + " and pk_finindex=? ";
    }

    if (voMsg.getProcMsg() != null) {
      strWhere = strWhere + " and procMsg=? ";
    }

    if (voMsg.getBillCode() != null) {
      strWhere = strWhere + " and billcode=? ";
    }

    if (voMsg.getSys() != null) {
      strWhere = strWhere + " and pk_sys=? ";
    }

    if (voMsg.getProc() != null) {
      strWhere = strWhere + " and pk_proc=? ";
    }

    if (voMsg.getBusiType() != null) {
      strWhere = strWhere + " and pk_busitype=? ";
    }

    if (voMsg.getCorp() != null) {
      strWhere = strWhere + " and pk_corp=? ";
    }

    if (voMsg.getDestSystem() > -1) {
      strWhere = strWhere + " and destsystem=? ";
    }

    if (strWhere.length() > 0)
      strWhere = " where " + strWhere;
    else {
      strWhere = "";
    }

    strTemp = strTemp + strWhere;

    int i = 1;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rst = null;
    try {
      con = getConnection();

      stmt = con.prepareStatement(strTemp);

      if (voMsg.getPkFinmsg() != null) {
        stmt.setString(i++, voMsg.getPkFinmsg());
      }

      if (voMsg.getProcMsg() != null) {
        stmt.setString(i++, voMsg.getProcMsg());
      }

      if (voMsg.getBillCode() != null) {
        stmt.setString(i++, voMsg.getBillCode());
      }

      if (voMsg.getSys() != null) {
        stmt.setString(i++, voMsg.getSys());
      }

      if (voMsg.getProc() != null) {
        stmt.setString(i++, voMsg.getProc());
      }

      if (voMsg.getBusiType() != null) {
        stmt.setString(i++, voMsg.getBusiType());
      }

      if (voMsg.getCorp() != null) {
        stmt.setString(i++, voMsg.getCorp());
      }

      if (voMsg.getDestSystem() > -1) {
        stmt.setInt(i++, voMsg.getDestSystem());
      }
      resInt = stmt.executeUpdate();

      stmt.close();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return resInt;
  }

  void dropTempTable(String tableName)
    throws Exception
  {
    if ((tableName == null) || (tableName.trim().equals("")))
      return;
    String dropTableSql = "drop table " + tableName;

    Connection con = null;
    Statement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.createStatement();

      stmt.execute(dropTableSql);
    }
    catch (Exception e) {
      System.out.println("删除临时表出现错误：" + e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
        Logger.error(e);
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
        Logger.error(e);
      }
    }
  }

  public String[] getRedOffsetState(String pk_voucherEntry)
    throws SQLException
  {
    String strTemp = "select mergebatchcode from dap_finindex where pk_vouchentry ='" + pk_voucherEntry + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    String redFlag = null;
    Vector vMerge = new Vector();
    try
    {
      DapExecTypeVO execVo = new DapExecTypeVO();
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        redFlag = rs.getString("mergebatchcode");
        if (redFlag != null)
          vMerge.addElement(redFlag);
      }
    }
    catch (Exception ex) {
      Logger.error(ex);
      if ((ex instanceof SQLException))
        throw ((SQLException)ex);
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMerge.size() == 0)
      return null;
    String[] reMergeCodes = new String[vMerge.size()];

    vMerge.copyInto(reMergeCodes);

    vMerge.clear();

    return reMergeCodes;
  }

  public String insert(DapFinMsgVO voMsg)
    throws SQLException, SystemException
  {
    DapFinMsgVO[] msgs = insertArray(new DapFinMsgVO[] { voMsg });
    return msgs[0].getPkFinmsg();
  }

  public DapFinMsgVO[] insertArray(DapFinMsgVO[] voMsg)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    String strTemp = "insert into dap_finindex (pk_finindex,billcode,pk_sys,pk_proc,pk_busitype,businame,busidate, operator, pk_corp, flag, currency,money, dapcomment, checker, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode,errmsg, checkstate,pk_rtvouch, voucherno, voucherdate,procmsg,vouchertype,destsystem,pk_glorg,pk_glbook)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try
    {
      con = getConnection();
      ((CrossDBConnection)con).setSqlTrans(false);
      stmt = prepareStatement(con, strTemp);
      String[] keys = getOIDs(voMsg.length);
      for (int index = 0; index < voMsg.length; index++) {
        voMsg[index].setPkFinmsg(keys[index]);
        int i = 1;
        stmt.setString(i++, voMsg[index].getPkFinmsg());

        if (voMsg[index].getBillCode() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getBillCode().trim());
        }

        stmt.setString(i++, voMsg[index].getSys().trim());

        stmt.setString(i++, voMsg[index].getProc().trim());

        if (voMsg[index].getBusiType() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getBusiType().trim());
        }

        if (voMsg[index].getBusiName() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getBusiName().trim());
        }

        if (voMsg[index].getBusiDate() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getBusiDate().toString());
        }

        if (voMsg[index].getOperator() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getOperator().trim());
        }

        if (voMsg[index].getCorp() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getCorp().trim());
        }

        if (voMsg[index].getFlag() == null)
          stmt.setNull(i++, 4);
        else {
          stmt.setInt(i++, voMsg[index].getFlag().intValue());
        }

        if (voMsg[index].getCurrency() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getCurrency().trim());
        }

        if (voMsg[index].getMoney() == null)
          stmt.setNull(i++, 8);
        else {
          stmt.setDouble(i++, voMsg[index].getMoney().doubleValue());
        }

        if (voMsg[index].getComment() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getComment().trim());
        }

        if (voMsg[index].getChecker() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getChecker().trim());
        }

        if (voMsg[index].getSettleMode() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getSettleMode().trim());
        }

        if (voMsg[index].getDocNum() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getDocNum().trim());
        }

        if (voMsg[index].getDocDate() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getDocDate().toString().trim());
        }

        if (voMsg[index].getVouchEntry() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getVouchEntry().trim());
        }

        if (voMsg[index].getDeltag() == null)
          stmt.setString(i++, "N");
        else {
          stmt.setString(i++, voMsg[index].getDeltag().toString());
        }

        if (voMsg[index].getSign() == null)
          stmt.setNull(i++, 4);
        else {
          stmt.setInt(i++, voMsg[index].getSign().intValue());
        }

        if (voMsg[index].getMergeBatch() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getMergeBatch().trim());
        }

        if (voMsg[index].getMergeBatchCode() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getMergeBatchCode().trim());
        }

        if (voMsg[index].getErrMsg() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, PfComm.sub_txt(voMsg[index].getErrMsg().trim(), 100));
        }

        if (voMsg[index].getCheckState() == null)
          stmt.setString(i++, "N");
        else {
          stmt.setString(i++, voMsg[index].getCheckState().toString());
        }

        if (voMsg[index].getPkRtVouch() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getPkRtVouch().trim());
        }

        if (voMsg[index].getVoucherNo() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getVoucherNo().trim());
        }

        if (voMsg[index].getVoucherDate() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getVoucherDate().toString());
        }

        if (voMsg[index].getProcMsg() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getProcMsg().trim());
        }

        if (voMsg[index].getVoucherType() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getVoucherType().trim());
        }

        stmt.setInt(i++, voMsg[index].getDestSystem());
        if (voMsg[index].getPkAccOrg() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getPkAccOrg().trim());
        }
        if (voMsg[index].getPkAccount() == null)
          stmt.setNull(i++, 12);
        else {
          stmt.setString(i++, voMsg[index].getPkAccount().trim());
        }
        executeUpdate(stmt);
      }
      executeBatch(stmt);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return voMsg;
  }

  public Hashtable isExistRelect(String[] pkVoucher)
    throws Exception, SQLException, SystemException
  {
    String tempSql = "";
    int limitsize = 20;
    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable h = new Hashtable();
    String tempTable = null;
    boolean usetable = pkVoucher.length > limitsize;
    try {
      if (!usetable) {
        StringBuffer strTemp = new StringBuffer(
          "select pk_vouchentry,sign,checkState,vouchertype from dap_finindex where pk_vouchentry in(");
        strTemp.append("'").append(pkVoucher[0]).append("'");
        for (int i = 1; i < pkVoucher.length; i++) {
          strTemp.append(",'").append(pkVoucher[i]).append("'");
        }
        strTemp.append(")");
        tempSql = strTemp.toString();
      } else {
        tempTable = createtmpTabQryByBillPks(pkVoucher);

        StringBuffer strTemp = new StringBuffer(
          "select pk_vouchentry,sign,checkState,vouchertype from dap_finindex,").append(tempTable)
          .append(" where pk_vouchentry =pk_bill");
        tempSql = strTemp.toString();
      }
      con = getConnection();
      stmt = con.prepareStatement(tempSql);
      ResultSet rsTemp = stmt.executeQuery();
      String pk = null;
      DapFinMsgVO dfm = null;
      String checkStr = null;
      String vouchertype = null;
      while (rsTemp.next()) {
        dfm = new DapFinMsgVO();
        pk = rsTemp.getString("pk_vouchentry");
        dfm.setSign(new Integer(rsTemp.getInt("sign")));
        checkStr = rsTemp.getString("checkState");
        if (checkStr != null)
          dfm.setCheckState(new UFBoolean(checkStr));
        else {
          dfm.setCheckState(new UFBoolean("N"));
        }
        vouchertype = rsTemp.getString("vouchertype");
        if (vouchertype != null)
          dfm.setVoucherType(vouchertype.trim());
        else {
          dfm.setVoucherType(null);
        }
        h.put(pk, dfm);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1)
      {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    return h;
  }

  public UFBoolean isExistRelect(String pkVoucher)
    throws SQLException
  {
    String strTemp = "select sign,checkState,vouchertype,pk_sys,destsystem from dap_finindex where pk_vouchentry='" + 
      pkVoucher + "'";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      ResultSet rsTemp = stmt.executeQuery();
      if (rsTemp.next()) {
        this.m_iExecType = rsTemp.getInt("sign");
        String tmpString = rsTemp.getString("checkState");
        if (tmpString != null)
          this.m_isGLProc = new UFBoolean(tmpString.trim());
        else {
          this.m_isGLProc = new UFBoolean("N");
        }
        tmpString = rsTemp.getString("vouchertype");
        if (tmpString != null) {
          this.m_checkStr = tmpString.trim();
        }
        this.PK_SYS = rsTemp.getString("pk_sys");
        this.m_destsystem = rsTemp.getInt("destsystem");
        //反编译后报错，edit by yqq  2016-07-18       
        UFBoolean localUFBoolean = new UFBoolean("Y");       
  //    localUFBoolean = new UFBoolean("Y");
        return localUFBoolean;
      }
 //     ResultSet rsTemp;
      UFBoolean localUFBoolean = new UFBoolean("N");
      return localUFBoolean;
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException4) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception localException5) {
      }
    }
    //反编译后报错，edit by yqq  2016-07-18
 //   throw localObject;
  }

  public DapFinMsgVO[] queryAllErrVO(DapFinMsgVO[] voArray, QueryLogVO qvo)
    throws SQLException, BusinessException
  {
    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, checker, balanname, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,gl_voucher.no as voucherno, gl_voucher.prepareddate as voucherdate, billtypename, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,gl_voucher.pk_vouchertype as vouchertype,dap_finindex.destsystem, dap_finindex.pk_glorg as pk_glorg,dap_finindex.pk_glbook as pk_glbook from ((((((dap_finindex left outer join sm_user on dap_finindex.checker=sm_user.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join gl_voucher on dap_finindex.pk_vouchentry=gl_voucher.pk_voucher) left outer join bd_balatype on dap_finindex.settlemode=bd_balatype.pk_balatype) ";
    if (qvo.destSystem == 1) {
      strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, checker, balanname, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,fts_voucher.cent_typeID as voucherno, fts_voucher.auditdate as voucherdate,billtypename, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,fts_voucher.pk_vouchertype as vouchertype,dap_finindex.destsystem, dap_finindex.pk_glorg as pk_glorg,dap_finindex.pk_glbook as pk_glbook from ((((((dap_finindex left outer join sm_user on dap_finindex.checker=sm_user.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join fts_voucher on dap_finindex.pk_vouchentry=fts_voucher.pk_voucher) left outer join bd_balatype on dap_finindex.settlemode=bd_balatype.pk_balatype) ";
    }
    String strWhere = null;
    if (qvo.pk_Corp == null)
      strWhere = " where not (errmsg is null)  and dap_finindex.destsystem=" + qvo.destSystem;
    else {
      strWhere = " where dap_finindex.pk_corp=? and not (errmsg is null) and dap_finindex.destsystem=" + 
        qvo.destSystem;
    }

    String strBillType = "";
    String strWhereBillType = "";
    if (voArray != null) {
      for (int i = 0; i < voArray.length; i++) {
        if (voArray[i].getProc() != null) {
          strBillType = strBillType + "'" + voArray[i].getProc() + "',";
        }
      }
      if ((strBillType != null) && (strBillType.trim().length() > 0))
      {
        strBillType = strBillType.substring(0, strBillType.length() - 1);
        strWhereBillType = strWhereBillType + " and dap_finindex.pk_proc in(" + strBillType + ")";
      }

      if ((voArray[0].getBusiType() != null) && (voArray[0].getBusiType().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.pk_busitype='" + voArray[0].getBusiType().trim() + "'";
      }

      if ((voArray[0].getBillCode() != null) && (voArray[0].getBillCode().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.billcode>='" + voArray[0].getBillCode().trim() + "'";
      }
      if ((voArray[1].getBillCode() != null) && (voArray[1].getBillCode().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.billcode<='" + voArray[1].getBillCode().trim() + "'";
      }

      if (voArray[0].getBusiDate() != null) {
        strWhereBillType = strWhereBillType + " and dap_finindex.busidate>='" + voArray[0].getBusiDate() + "'";
      }
      if (voArray[1].getBusiDate() != null) {
        strWhereBillType = strWhereBillType + " and dap_finindex.busidate<='" + voArray[1].getBusiDate() + "'";
      }

      if ((voArray[0].getOperator() != null) && (voArray[0].getOperator().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.operator='" + voArray[0].getOperator() + "'";
      }

      if ((voArray[0].getChecker() != null) && (voArray[0].getChecker().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.checker='" + voArray[0].getChecker() + "'";
      }

      if ((voArray[0].getPkAccOrg() != null) && (voArray[0].getPkAccOrg().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.pk_glorg='" + voArray[0].getPkAccOrg() + "'";
      }

      if ((voArray[0].getPkAccount() != null) && (voArray[0].getPkAccount().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.pk_glbook='" + voArray[0].getPkAccount() + "'";
      }

      if (voArray[0].getMoney() != null) {
        strWhereBillType = strWhereBillType + " and dap_finindex.money>=" + voArray[0].getMoney().toString();
      }
      if (voArray[1].getMoney() != null) {
        strWhereBillType = strWhereBillType + " and dap_finindex.money<=" + voArray[1].getMoney().toString();
      }

      if ((voArray[0].getCurrency() != null) && (voArray[0].getCurrency().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.currency='" + voArray[0].getCurrency() + "'";
      }

      if ((voArray[0].getComment() != null) && (voArray[0].getComment().trim().length() > 0)) {
        strWhereBillType = strWhereBillType + " and dap_finindex.dapcomment like '" + voArray[0].getComment() + "%'";
      }
    }

    String wherePart = null;

    if ((wherePart == null) || (wherePart.trim().length() == 0))
      strTemp = strTemp + strWhere + strWhereBillType;
    else {
      strTemp = strTemp + strWhere + " and " + wherePart + strWhereBillType;
    }

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    Vector vMsg = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      if (qvo.pk_Corp != null) {
        stmt.setString(i++, qvo.pk_Corp);
      }
      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;
      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        if (strValue != null) {
          voTemp.setComment(strValue.trim());
        }

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("balanname");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        if (strValue != null) {
          voTemp.setErrMsg(strValue.trim());
        }

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("voucherno");
        if (strValue != null) {
          voTemp.setVoucherNo(strValue.trim());
        }

        strValue = rsTemp.getString("voucherdate");
        if (strValue != null) {
          voTemp.setVoucherDate(strValue.trim());
        }

        voTemp.setSysname(Translator.getSystemName(null, voTemp.getSys()));

        strValue = rsTemp.getString("billtypename");

        voTemp.setProcName(Translator.getBillTypeName(strValue, voTemp.getProc()));

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        strValue = rsTemp.getString("user_name");
        if (strValue != null) {
          voTemp.setCheckerName(strValue.trim());
        }

        strValue = rsTemp.getString("psnname");
        if (strValue != null) {
          voTemp.setOperatorName(strValue.trim());
        }

        strValue = rsTemp.getString("currtypename");
        if (strValue != null) {
          voTemp.setCurrencyName(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));
        vMsg.addElement(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.copyInto(voMsgArr);
    }
    return voMsgArr;
  }

  public DapFinMsgVO[] queryAllErrVO(QueryLogVO qvo)
    throws SQLException, BusinessException
  {
    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, checker, balanname, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,gl_voucher.no as voucherno, gl_voucher.prepareddate as voucherdate, billtypename, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,gl_voucher.pk_vouchertype as vouchertype,dap_finindex.destsystem, dap_finindex.pk_glorg,dap_finindex.pk_glbook from ((((((dap_finindex left outer join sm_user on dap_finindex.checker=sm_user.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join gl_voucher on dap_finindex.pk_vouchentry=gl_voucher.pk_voucher) left outer join bd_balatype on dap_finindex.settlemode=bd_balatype.pk_balatype) ";
    if (qvo.destSystem == 1) {
      strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, checker, balanname, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,fts_voucher.cent_typeID as voucherno, fts_voucher.auditdate as voucherdate, billtypename, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,fts_voucher.pk_vouchertype as vouchertype,dap_finindex.destsystem, dap_finindex.pk_glorg,dap_finindex.pk_glbook from ((((((dap_finindex left outer join sm_user on dap_finindex.checker=sm_user.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join fts_voucher on dap_finindex.pk_vouchentry=fts_voucher.pk_voucher) left outer join bd_balatype on dap_finindex.settlemode=bd_balatype.pk_balatype) ";
    }
    String strWhere = null;
    if (qvo.pk_Corp == null)
      strWhere = " where not (errmsg is null) ";
    else {
      strWhere = " where dap_finindex.pk_corp=? and not (errmsg is null) ";
    }
    if ((qvo.pk_glorg != null) && (qvo.pk_glorg.trim().length() != 0)) {
      strWhere = strWhere + " and dap_finindex.pk_glorg='" + qvo.pk_glorg + "'  ";
    }
    if ((qvo.pk_glbook != null) && (qvo.pk_glbook.trim().length() != 0)) {
      strWhere = strWhere + " and dap_finindex.pk_glbook='" + qvo.pk_glbook + "' ";
    }
    strWhere = strWhere + " and dap_finindex.destsystem=" + qvo.destSystem;

    String wherePart = null;
    if ((wherePart == null) || (wherePart.trim().length() == 0))
      strTemp = strTemp + strWhere;
    else {
      strTemp = strTemp + strWhere + " and " + wherePart;
    }

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    Vector vMsg = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      if (qvo.pk_Corp != null) {
        stmt.setString(i++, qvo.pk_Corp);
      }
      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;
      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        if (strValue != null) {
          voTemp.setComment(strValue.trim());
        }

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("balanname");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        if (strValue != null) {
          voTemp.setErrMsg(strValue.trim());
        }

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("voucherno");
        if (strValue != null) {
          voTemp.setVoucherNo(strValue.trim());
        }

        strValue = rsTemp.getString("voucherdate");
        if (strValue != null) {
          voTemp.setVoucherDate(strValue.trim());
        }

        voTemp.setSysname(Translator.getSystemName(null, voTemp.getSys()));

        strValue = rsTemp.getString("billtypename");

        voTemp.setProcName(Translator.getBillTypeName(strValue, voTemp.getProc()));

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        strValue = rsTemp.getString("user_name");
        if (strValue != null) {
          voTemp.setCheckerName(strValue.trim());
        }

        strValue = rsTemp.getString("psnname");
        if (strValue != null) {
          voTemp.setOperatorName(strValue.trim());
        }

        strValue = rsTemp.getString("currtypename");
        if (strValue != null) {
          voTemp.setCurrencyName(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));

        strValue = rsTemp.getString("pk_glorg");
        if (strValue != null) {
          voTemp.setPkAccOrg(strValue.trim());
        }

        strValue = rsTemp.getString("pk_glbook");
        if (strValue != null) {
          voTemp.setPkAccount(strValue.trim());
        }
        vMsg.addElement(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.copyInto(voMsgArr);
    }
    return voMsgArr;
  }

  public Hashtable queryBillDataByRtVouchORVoucher(String[] pkAry, String strVoucher)
    throws SQLException
  {
    String[] strTemp = (String[])null;
    if (strVoucher == null)
    {
      int PACKAGESIZE = 200;
      int fixedPKLength = pkAry.length - 1;
      int numofPackage = fixedPKLength / PACKAGESIZE + (fixedPKLength % PACKAGESIZE > 0 ? 1 : 0);

      strTemp = new String[numofPackage];

      for (int s = 0; s < numofPackage; s++) {
        String pkRtVouch = "";

        int beginIndex = s * PACKAGESIZE;
        int endindex = beginIndex + PACKAGESIZE;

        if (endindex > fixedPKLength) {
          endindex = fixedPKLength;
        }
        for (int i = beginIndex; i < endindex; i++) {
          pkRtVouch = pkRtVouch + "'" + pkAry[i] + "',";
        }

        pkRtVouch = "(" + pkRtVouch.substring(0, pkRtVouch.length() - 1) + ")";

        strTemp[s] = ("select pk_proc,procmsg from dap_finindex where pk_rtvouch In " + pkRtVouch);
      }
    } else {
      strTemp = new String[1];
      strTemp[0] = ("select pk_proc,procmsg from dap_finindex where pk_vouchentry ='" + strVoucher + "'");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable billtypeHas = new Hashtable();
    try {
      con = getConnection();

      for (int i = 0; i < strTemp.length; i++)
      {
        stmt = con.prepareStatement(strTemp[i]);

        ResultSet rsTemp = stmt.executeQuery();
        while (rsTemp.next()) {
          String pkBillType = rsTemp.getString("pk_proc");
          if (pkBillType != null) {
            pkBillType = pkBillType.trim();
          }
          String pkBill = rsTemp.getString("procmsg");
          if (pkBill != null) {
            pkBill = pkBill.trim();
          }
          if (billtypeHas.containsKey(pkBillType)) {
            ((Vector)(Vector)billtypeHas.get(pkBillType)).addElement(pkBill);
          }
          else {
            Vector v = new Vector();
            v.addElement(pkBill);
            billtypeHas.put(pkBillType, v);
          }
        }
        try {
          if (stmt != null)
            stmt.close();
        } catch (Exception localException) {
        }
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2)
      {
      }
    }
    return billtypeHas;
  }

  public Hashtable queryBillDataByRtVouchORVoucher(String tableName, String strVoucher)
    throws SQLException
  {
    String strTemp = null;
    if (strVoucher == null) {
      strTemp = "select pk_proc,procmsg from dap_finindex," + tableName + " a where pk_rtvouch= a.pk_bill ";
    }
    else {
      strTemp = "select pk_proc,procmsg from dap_finindex where pk_vouchentry ='" + strVoucher + "'";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable billtypeHas = new Hashtable();
    try {
      con = getConnection();

      stmt = con.prepareStatement(strTemp);

      ResultSet rsTemp = stmt.executeQuery();
      while (rsTemp.next()) {
        String pkBillType = rsTemp.getString("pk_proc");
        if (pkBillType != null) {
          pkBillType = pkBillType.trim();
        }
        String pkBill = rsTemp.getString("procmsg");
        if (pkBill != null) {
          pkBill = pkBill.trim();
        }
        if (billtypeHas.containsKey(pkBillType)) {
          ((Vector)(Vector)billtypeHas.get(pkBillType)).addElement(pkBill);
        }
        else {
          Vector v = new Vector();
          v.addElement(pkBill);
          billtypeHas.put(pkBillType, v);
        }
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2)
      {
      }
    }
    return billtypeHas;
  }

  public String[] queryBillDataIndex(String pkIndex)
    throws SQLException
  {
    String strTemp = "select pk_proc,procmsg from dap_finindex where pk_finindex=?";

    Connection con = null;
    PreparedStatement stmt = null;
    String[] strValueAry = (String[])null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      stmt.setString(1, pkIndex);

      ResultSet rsTemp = stmt.executeQuery();
      if (rsTemp.next()) {
        strValueAry = new String[2];
        String pkBillType = rsTemp.getString("pk_proc");
        if (pkBillType != null)
          pkBillType = pkBillType.trim();
        strValueAry[0] = pkBillType;
        String pkBill = rsTemp.getString("procmsg");
        if (pkBill != null)
          pkBill = pkBill.trim();
        strValueAry[1] = pkBill;
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return strValueAry;
  }

  public DapFinMsgVO querybyTypeandDjPK(String proc, String procMsg, String pk_glorg, String pk_glbook)
    throws DapBusinessException
  {
    StringBuffer strTemp = new StringBuffer("select PK_FININDEX,PK_VOUCHENTRY,PK_RTVOUCH,PK_CORP,PK_SYS,");
    strTemp.append("PK_PROC,PK_BUSITYPE,PROCMSG,BUSINAME,BILLCODE,BUSIDATE,OPERATOR,CURRENCY,MONEY,");
    strTemp.append("DAPCOMMENT,CHECKER,VOUCHERTYPE,SETTLEMODE,DOCNUM,DOCDATE,CHECKSTATE,VOUCHERNO,");
    strTemp
      .append("VOUCHERDATE,DELTAG,SIGN,PK_MERGEBATCH,MERGEBATCHCODE,FLAG,ERRMSG,DESTSYSTEM,pk_glorg,pk_glbook from DAP_FININDEX");
    strTemp.append(" where PK_PROC ");

    if (proc == null)
      strTemp.append(" is null ");

    else {
 //     strTemp.append(" =" + proc);   20160718  yqq
      strTemp.append(" ='" + proc+ "' ");

    }

    if (procMsg == null)
 //   strTemp.append(" and promsg is null");       DAP_FININDEX表内无promsg  20160718  yqq
      strTemp.append(" and procmsg is null");
    else {
 //   strTemp.append(" and promsg=" + procMsg);  DAP_FININDEX表内无promsg  20160718  yqq
      strTemp.append(" and procmsg='" + procMsg+ "' ");
    }
    //add by src 2017年12月29日11:50:03
    if(StringUtils.isNotBlank(pk_glorg)){
    	strTemp.append(" and pk_glorg = '" + pk_glorg + "' ");
    }
    if(StringUtils.isNotBlank(pk_glbook)){
    	strTemp.append(" and pk_glbook = '" + pk_glbook + "' ");
    }
    //end by src 2017年12月29日11:50:03
//    strTemp.append(" and pk_glorg = '" + pk_glorg + "' ");
//    strTemp.append(" and pk_glbook = '" + pk_glbook + "' ");
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp.toString());

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;
      DapFinMsgVO voTemp = null;

      if (rsTemp.next()) {
        voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        if (strValue != null) {
          voTemp.setComment(strValue.trim());
        }

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("errmsg");
        if (strValue != null) {
          voTemp.setErrMsg(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));
        strValue = rsTemp.getString("pk_glorg");
        if (strValue != null) {
          voTemp.setPkAccOrg(strValue.trim());
        }
        strValue = rsTemp.getString("pk_glbook");
        if (strValue != null) {
          voTemp.setPkAccount(strValue.trim());
        }
      }
      DapFinMsgVO localDapFinMsgVO1 = voTemp;
      return localDapFinMsgVO1;
    } catch (Exception e) {
      DapLoger.loger.error(e.getMessage(), e);
      throw new DapBusinessException(e.getMessage(), e);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException3) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception localException4) {
      }
    }
    //反编译后报错，edit by yqq  2016-07-18
//  throw localObject;
  }

  public DapFinMsgVO[] queryByVO(DapFinMsgVO[] voMsg, QueryLogVO qvo)
    throws SQLException
  {
    if (qvo.notRtVouch) {
      if (qvo.destSystem == 1) {
        return querySettle_ByVO(voMsg, qvo);
      }
      return queryGL_ByVO(voMsg, qvo);
    }

    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate,dap_finindex.operator,dap_finindex.checker,dap_rtvouch.prepared as perpared,dap_finindex.pk_corp, flag, currency,money, dapcomment, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch, billtypename, procmsg,a.user_name operatorname,b.user_name checkername,c.user_name perparedname, currtypename,dap_finindex.destsystem,dap_rtvouch.assino, dap_finindex.pk_glorg,dap_finindex.pk_glbook from dap_finindex left outer join dap_rtvouch on dap_finindex.pk_rtvouch=dap_rtvouch.pk_voucher  left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode  left outer join sm_user a on dap_finindex.operator=a.cUserId  left outer join sm_user b on dap_finindex.checker=b.cUserId  left outer join sm_user c on dap_rtvouch.prepared=c.cUserId  left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype ";

    String strTempOracle = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate,dap_finindex.operator,dap_finindex.checker,dap_rtvouch.prepared as perpared,  dap_finindex.pk_corp, flag, currency,money, dapcomment, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch, billtypename, procmsg,a.user_name operatorname,b.user_name checkername,c.user_name perparedname, currtypename,dap_finindex.destsystem,dap_rtvouch.assino, dap_finindex.pk_glorg,dap_finindex.pk_glbook from dap_finindex,dap_rtvouch,bd_currtype,bd_billtype,sm_user a,sm_user b,sm_user c";
    String strWhereOracle = " dap_finindex.pk_rtvouch=dap_rtvouch.pk_voucher(+)  and dap_finindex.pk_proc=bd_billtype.pk_billtypecode(+) and dap_rtvouch.prepared=c.cUserId(+) and dap_finindex.operator=a.cUserId(+) and dap_finindex.checker=b.cUserId(+) and dap_finindex.currency=bd_currtype.pk_currtype(+) ";

    String strWhere = "";
    String strArr = "";

    boolean bMax = false;

    if (voMsg == null) {
      if (qvo.notRtVouch)
        strWhere = strWhere + "where  flag=0";
      else
        strWhere = strWhere + "where  dap_finindex.pk_corp=? and not (errmsg is null) ";
    }
    else if (voMsg != null) {
      if (voMsg.length > 1) {
        bMax = true;
      }

      switch (qvo.queryClass) {
      case 0:
        strWhere = strWhere + "and flag<" + 4 + " ";

        break;
      case 1:
        strWhere = strWhere + "and flag>=" + 4 + " ";
        break;
      case 3:
        strWhere = strWhere + "and flag=" + 2 + " ";
        break;
      case 2:
      }

      if (voMsg[0].getPkFinmsg() != null) {
        strWhere = strWhere + "and pk_finindex=? ";
      }

      if (voMsg[0].getProcMsg() != null) {
        strWhere = strWhere + "and procmsg like ? ";
      }

      if ((voMsg[0].getBillCode() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.billcode=? ";
      else if (voMsg[0].getBillCode() != null) {
        strWhere = strWhere + "and dap_finindex.billcode>=? ";
      }

      if (voMsg[0].getBusiType() != null) {
        strWhere = strWhere + "and dap_finindex.pk_busitype=? ";
      }

      if (voMsg[0].getCorp() != null) {
        strWhere = strWhere + "and dap_finindex.pk_corp=? ";
      }

      for (int iArr = 0; iArr < voMsg.length; iArr++) {
        if (voMsg[iArr].getProc() != null)
          strArr = strArr + "or (pk_sys=? and pk_proc=?) ";
        if ((iArr == voMsg.length - 1) && (strArr.length() != 0)) {
          strWhere = strWhere + "and (" + strArr.substring(2, strArr.length()) + ") ";
        }
      }

      if ((voMsg[0].getBusiDate() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.busidate=? ";
      else if (voMsg[0].getBusiDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }

      if (voMsg[0].getFlag() != null) {
        strWhere = strWhere + "and dap_finindex.flag=? ";
      }

      if (voMsg[0].getCurrency() != null) {
        strWhere = strWhere + "and dap_finindex.currency=? ";
      }

      if ((voMsg[0].getMoney() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.money=? ";
      else if (voMsg[0].getMoney() != null) {
        strWhere = strWhere + "and dap_finindex.money>=? ";
      }

      if (voMsg[0].getComment() != null) {
        strWhere = strWhere + "and dap_finindex.dapcomment like ? ";
      }

      if (voMsg[0].getOperator() != null) {
        strWhere = strWhere + "and dap_finindex.operator=? ";
      }

      if (voMsg[0].getChecker() != null) {
        strWhere = strWhere + "and dap_finindex.checker=? ";
      }

      if (voMsg[0].getPerpared() != null) {
        strWhere = strWhere + "and dap_rtvouch.prepared=? ";
      }

      if (bMax) {
        if (voMsg[1].getBillCode() != null) {
          strWhere = strWhere + "and billcode<=? ";
        }
        if (voMsg[1].getBusiDate() != null) {
          strWhere = strWhere + "and dap_finindex.busidate<=? ";
        }
        if (voMsg[1].getMoney() != null) {
          strWhere = strWhere + "and dap_finindex.money<=? ";
        }
      }

      if (voMsg[0].getPkRtVouch() != null) {
        strWhere = strWhere + "and dap_finindex.pk_rtvouch=? ";
      }

      if (voMsg[0].getAssino() != null) {
        strWhere = strWhere + "and dap_rtvouch.assino=? ";
      }

      if (voMsg[0].getQueryBeginDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }
      if (voMsg[0].getQueryEndDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate<=? ";
      }

      if (strWhere.length() > 0) {
        if (voMsg[0].getVouchEntry() == null)
          strWhere = "where " + strWhere.substring(3, strWhere.length() - 1);
        else {
          strWhere = "where (" + strWhere.substring(3, strWhere.length() - 1) + ") and pk_vouchentry=?";
        }
      }
      else if (voMsg[0].getVouchEntry() == null)
        strWhere = "";
      else {
        strWhere = "where pk_vouchentry=? ";
      }

    }

    String strDest = null;
    if (qvo.destSystem != -1) {
      strDest = " and dap_finindex.destsystem=" + qvo.destSystem;
    }
    if (strDest != null) {
      strWhere = strWhere + strDest;
    }
    if ((voMsg != null) && (voMsg[0].getPkAccOrg() != null)) {
      strWhere = strWhere + " and dap_finindex.pk_glorg=? ";
    }
    if ((voMsg != null) && (voMsg[0].getPkAccount() != null)) {
      strWhere = strWhere + " and dap_finindex.pk_glbook=? ";
    }

    String sql = null;

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    Vector vMsg = new Vector();
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    try {
      con = (CrossDBConnection)getConnection();
      int databaseType = con.getDatabaseType();
      con.setSqlTrans(false);
      if (databaseType == 1) {
        if (strWhere.startsWith("where")) {
          strWhere = strWhere.substring(5, strWhere.length());
        }
        sql = strTempOracle + " where " + strWhereOracle + " and " + strWhere + 
          " order by dap_finindex.billcode ";
      } else {
        sql = strTemp + strWhere + " order by dap_finindex.billcode ";
      }
      stmt = con.prepareStatement(sql);
      if ((voMsg == null) && 
        (!qvo.notRtVouch))
      {
        stmt.setString(i++, qvo.pk_Corp);
      }

      if (voMsg != null) {
        if (voMsg[0].getPkFinmsg() != null) {
          stmt.setString(i++, voMsg[0].getPkFinmsg());
        }

        if (voMsg[0].getProcMsg() != null) {
          stmt.setString(i++, voMsg[0].getProcMsg() + "%");
        }

        if (voMsg[0].getBillCode() != null) {
          stmt.setString(i++, voMsg[0].getBillCode());
        }

        if (voMsg[0].getBusiType() != null) {
          stmt.setString(i++, voMsg[0].getBusiType());
        }

        if (voMsg[0].getCorp() != null) {
          stmt.setString(i++, voMsg[0].getCorp());
        }

        for (int iArr = 0; iArr < voMsg.length; iArr++) {
          if (voMsg[iArr].getProc() != null) {
            stmt.setString(i++, voMsg[iArr].getSys());
            stmt.setString(i++, voMsg[iArr].getProc());
          }
        }

        if (voMsg[0].getBusiDate() != null) {
          stmt.setString(i++, voMsg[0].getBusiDate().toString());
        }

        if (voMsg[0].getFlag() != null) {
          stmt.setInt(i++, voMsg[0].getFlag().intValue());
        }

        if (voMsg[0].getCurrency() != null) {
          stmt.setString(i++, voMsg[0].getCurrency());
        }

        if (voMsg[0].getMoney() != null) {
          stmt.setDouble(i++, voMsg[0].getMoney().doubleValue());
        }

        if (voMsg[0].getComment() != null) {
          stmt.setString(i++, '%' + voMsg[0].getComment() + '%');
        }

        if (voMsg[0].getOperator() != null) {
          stmt.setString(i++, voMsg[0].getOperator());
        }

        if (voMsg[0].getChecker() != null) {
          stmt.setString(i++, voMsg[0].getChecker());
        }

        if (voMsg[0].getPerpared() != null) {
          stmt.setString(i++, voMsg[0].getPerpared());
        }

        if (bMax) {
          if (voMsg[1].getBillCode() != null) {
            stmt.setString(i++, voMsg[1].getBillCode());
          }
          if (voMsg[1].getBusiDate() != null) {
            stmt.setString(i++, voMsg[1].getBusiDate().toString());
          }
          if (voMsg[1].getMoney() != null) {
            stmt.setDouble(i++, voMsg[1].getMoney().doubleValue());
          }
        }

        if (voMsg[0].getPkRtVouch() != null) {
          stmt.setString(i++, voMsg[0].getPkRtVouch());
        }
        if (voMsg[0].getAssino() != null) {
          stmt.setString(i++, voMsg[0].getAssino());
        }

        if (voMsg[0].getQueryBeginDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryBeginDate().toString());
        }
        if (voMsg[0].getQueryEndDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryEndDate().toString());
        }

        if (voMsg[0].getVouchEntry() != null) {
          stmt.setString(i++, voMsg[0].getVouchEntry());
        }
        if (voMsg[0].getPkAccOrg() != null) {
          stmt.setString(i++, voMsg[0].getPkAccOrg());
        }
        if (voMsg[0].getPkAccount() != null) {
          stmt.setString(i++, voMsg[0].getPkAccount());
        }
      }

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;

      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        String pk_sys = rsTemp.getString("pk_sys");
        if (pk_sys != null) {
          voTemp.setSys(pk_sys.trim());
        }

        String pk_proc = rsTemp.getString("pk_proc");
        if (pk_proc != null) {
          voTemp.setProc(pk_proc.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : Translator.bsTranslate("fidap", businame.trim());

        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("perpared");
        if (strValue != null) {
          voTemp.setPerpared(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setComment(strValue);

        strValue = rsTemp.getString("settlemode");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setErrMsg(strValue);

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("billtypename");
        voTemp.setProcName(Translator.getBillTypeName(strValue, voTemp.getProc()));

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        strValue = rsTemp.getString("operatorname");
        if (strValue != null) {
          voTemp.setOperatorName(strValue.trim());
        }

        strValue = rsTemp.getString("checkername");
        if (strValue != null) {
          voTemp.setCheckerName(strValue.trim());
        }

        strValue = rsTemp.getString("perparedname");
        if (strValue != null) {
          voTemp.setPerparedName(strValue.trim());
        }

        strValue = rsTemp.getString("currtypename");
        if (strValue != null) {
          voTemp.setCurrencyName(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));

        strValue = rsTemp.getString("assino");
        if (strValue != null) {
          voTemp.setAssino(strValue.trim());
        }
        strValue = rsTemp.getString("pk_glorg");
        if (strValue != null) {
          voTemp.setPkAccOrg(strValue.trim());
        }
        strValue = rsTemp.getString("pk_glbook");
        if (strValue != null) {
          voTemp.setPkAccount(strValue.trim());
        }

        voTemp.setSysname(Translator.getSystemName(null, voTemp.getSys()));

        vMsg.addElement(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.copyInto(voMsgArr);
    }
    return voMsgArr;
  }

  public DapFinMsgVO[] queryByVO(DapFinMsgVO voMsg, int queryClass)
    throws SQLException
  {
    DapFinMsgVO[] voTemp = (DapFinMsgVO[])null;
    try {
      if (voMsg.getVouchEntry() == null)
        throw new Exception(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000154"));
      voTemp = querybyVoucherEntry(voMsg.getVouchEntry());
      if (voTemp != null)
        for (int i = 0; i < voTemp.length; i++)
          voTemp[i].setVoucherType(null);
    }
    catch (Exception e)
    {
      Logger.error(e);
    }
    QueryLogVO qvo = new QueryLogVO();
    qvo.queryClass = queryClass;
    qvo.notRtVouch = true;
    if (voTemp != null)
      qvo.destSystem = voTemp[0].getDestSystem();
    return queryByVO(new DapFinMsgVO[] { voMsg }, qvo);
  }

  public DapFinMsgVO[] querybyVoucherEntry(String voucherEntry)
    throws Exception
  {
    StringBuffer strTemp = new StringBuffer("select PK_FININDEX,PK_VOUCHENTRY,PK_RTVOUCH,PK_CORP,PK_SYS,");
    strTemp.append("PK_PROC,PK_BUSITYPE,PROCMSG,BUSINAME,BILLCODE,BUSIDATE,OPERATOR,CURRENCY,MONEY,");
    strTemp.append("DAPCOMMENT,CHECKER,VOUCHERTYPE,SETTLEMODE,DOCNUM,DOCDATE,CHECKSTATE,VOUCHERNO,");
    strTemp
      .append("VOUCHERDATE,DELTAG,SIGN,PK_MERGEBATCH,MERGEBATCHCODE,FLAG,ERRMSG,DESTSYSTEM,pk_glorg, pk_glbook from DAP_FININDEX");
    strTemp.append(" where PK_VOUCHENTRY ");

    if (voucherEntry == null)
      strTemp.append(" is null ");
    else {
      strTemp.append(" ='" + voucherEntry + "'");
    }

    strTemp.append(" and  flag > 3 ");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp.toString());

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;
      Vector vTemp = new Vector();

      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setComment(strValue);

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("errmsg");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setErrMsg(strValue);

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));

        strValue = rsTemp.getString("pk_glorg");
        voTemp.setPkAccOrg(strValue);

        strValue = rsTemp.getString("pk_glbook");
        voTemp.setPkAccount(strValue);

        vTemp.addElement(voTemp);
      }

      if (vTemp.size() > 0) {
        DapFinMsgVO[] reIndexVO = new DapFinMsgVO[vTemp.size()];
        vTemp.copyInto(reIndexVO);
        DapFinMsgVO[] arrayOfDapFinMsgVO1 = reIndexVO;
        return arrayOfDapFinMsgVO1;
      }
      return null;
    }
    catch (Exception e) {
      Logger.error(e);
      throw e;
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException5) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception localException6) {
      }
    }
    //反编译后报错，edit by yqq  2016-07-18
//  throw localObject;
  }

  public DapFinMsgVO[] queryGL_ByVO(DapFinMsgVO[] voMsg, QueryLogVO qvo)
    throws SQLException
  {
    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, gl_voucher.pk_prepared, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,gl_voucher.no as voucherno, gl_voucher.prepareddate as voucherdate, billtypename, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,gl_voucher.pk_vouchertype as vouchertype, gl_voucher.totaldebit as totaldebit, gl_voucher.totalcredit as totalcredit, bd_vouchertype.vouchtypename as vouchtypename,dap_finindex.destsystem,dap_rtvouch.assino from (((((((dap_finindex left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join gl_voucher on dap_finindex.pk_vouchentry=gl_voucher.pk_voucher) left outer join sm_user on gl_voucher.pk_prepared=sm_user.cUserId) left outer join bd_vouchertype on gl_voucher.pk_vouchertype = bd_vouchertype.pk_vouchertype) left outer join dap_rtvouch on dap_finindex.pk_rtvouch = dap_rtvouch.pk_voucher) ";
    String strWhere = "";
    String strArr = "";

    boolean bMax = false;

    if (voMsg == null) {
      if (qvo.notRtVouch) {
        strWhere = strWhere + " where flag=0 ";
      }
      else
        strWhere = strWhere + " where dap_finindex.pk_corp=? and not (errmsg is null) ";
    }
    else if (voMsg != null) {
      if (voMsg.length > 1) {
        bMax = true;
      }

      switch (qvo.queryClass)
      {
      case 0:
        strWhere = strWhere + "and flag<" + 4 + " ";

        break;
      case 1:
        strWhere = strWhere + "and flag>=" + 4 + " ";
        break;
      case 3:
        strWhere = strWhere + "and flag=" + 2 + " ";
        break;
      case 2:
      }

      if (voMsg[0].getPkFinmsg() != null) {
        strWhere = strWhere + "and pk_finindex=? ";
      }

      if (voMsg[0].getProcMsg() != null) {
        strWhere = strWhere + "and procmsg like ? ";
      }

      if ((qvo.billIDs != null) && (qvo.billIDs.length > 0)) {
        String strBillID = "(";
        for (int i = 0; i < qvo.billIDs.length; i++) {
          if (i == qvo.billIDs.length - 1) {
            strBillID = strBillID + "'" + qvo.billIDs[i] + "'";
          }
          else
            strBillID = strBillID + "'" + qvo.billIDs[i] + "',";
        }
        strBillID = strBillID + ")";
        strWhere = strWhere + "and procmsg in " + strBillID;
      }

      if ((voMsg[0].getBillCode() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.billcode=? ";
      else if (voMsg[0].getBillCode() != null) {
        strWhere = strWhere + "and dap_finindex.billcode>=? ";
      }

      for (int iArr = 0; iArr < voMsg.length; iArr++) {
        if (voMsg[iArr].getProc() != null)
          strArr = strArr + "or (pk_sys=? and pk_proc=?) ";
        if ((iArr == voMsg.length - 1) && (strArr.length() != 0)) {
          strWhere = strWhere + "and (" + strArr.substring(2, strArr.length()) + ") ";
        }
      }
      if (voMsg[0].getBusiType() != null) {
        strWhere = strWhere + "and dap_finindex.pk_busitype=? ";
      }

      if (voMsg[0].getCorp() != null) {
        strWhere = strWhere + "and dap_finindex.pk_corp=? ";
      }

      if ((voMsg[0].getBusiDate() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.busidate=? ";
      else if (voMsg[0].getBusiDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }

      if ((voMsg[0].getVoucherDate() != null) && (!bMax))
        strWhere = strWhere + "and gl_voucher.prepareddate=? ";
      else if (voMsg[0].getVoucherDate() != null) {
        strWhere = strWhere + "and gl_voucher.prepareddate>=? ";
      }

      if ((voMsg[0].getVoucherNo() != null) && (!bMax))
        strWhere = strWhere + "and gl_voucher.no=? ";
      else if (voMsg[0].getVoucherNo() != null) {
        strWhere = strWhere + "and gl_voucher.no>=? ";
      }

      if (voMsg[0].getOperator() != null) {
        strWhere = strWhere + "and dap_finindex.operator=? ";
      }

      if (voMsg[0].getFlag() != null) {
        strWhere = strWhere + "and dap_finindex.flag=? ";
      }

      if (voMsg[0].getCurrency() != null) {
        strWhere = strWhere + "and dap_finindex.currency=? ";
      }

      if ((voMsg[0].getMoney() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.money=? ";
      else if (voMsg[0].getMoney() != null) {
        strWhere = strWhere + "and dap_finindex.money>=? ";
      }

      if (voMsg[0].getComment() != null) {
        strWhere = strWhere + "and dap_finindex.dapcomment like ? ";
      }

      if (voMsg[0].getPerpared() != null) {
        strWhere = strWhere + "and gl_voucher.pk_prepared=? ";
      }

      if (voMsg[0].getVoucherType() != null) {
        strWhere = strWhere + "and gl_voucher.pk_vouchertype=? ";
      }

      if (bMax) {
        if (voMsg[1].getBillCode() != null) {
          strWhere = strWhere + "and billcode<=? ";
        }
        if (voMsg[1].getBusiDate() != null) {
          strWhere = strWhere + "and dap_finindex.busidate<=? ";
        }
        if (voMsg[1].getMoney() != null) {
          strWhere = strWhere + "and dap_finindex.money<=? ";
        }
        if (voMsg[1].getVoucherDate() != null) {
          strWhere = strWhere + "and gl_voucher.prepareddate<=? ";
        }
        if (voMsg[1].getVoucherNo() != null) {
          strWhere = strWhere + "and gl_voucher.no<=? ";
        }
      }

      if (voMsg[0].getPkRtVouch() != null) {
        strWhere = strWhere + "and dap_finindex.pk_rtvouch=? ";
      }

      if (voMsg[0].getQueryBeginDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }
      if (voMsg[0].getQueryEndDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate<=? ";
      }

      if (strWhere.length() > 0) {
        if (voMsg[0].getVouchEntry() == null)
          strWhere = "where " + strWhere.substring(3, strWhere.length() - 1);
        else {
          strWhere = "where (" + strWhere.substring(3, strWhere.length() - 1) + ") and pk_vouchentry=?";
        }
      }
      else if (voMsg[0].getVouchEntry() == null)
        strWhere = "";
      else {
        strWhere = "where pk_vouchentry=? ";
      }

    }

    if ((strWhere == null) || (strWhere.trim().length() <= 0))
      strWhere = " where gl_voucher.dr is null or gl_voucher.dr=0 ";
    else {
      strWhere = strWhere + " and (gl_voucher.dr is null or gl_voucher.dr=0) ";
    }
    if (qvo.destSystem != -1) {
      strWhere = strWhere + " and dap_finindex.destsystem=" + qvo.destSystem;
    }
    if (!qvo.notRtVouch)
    {
      strWhere = strWhere + " and not (dap_finindex.pk_rtvouch is null)";
    }

    if ((voMsg != null) && (voMsg[0].getPkAccOrg() != null)) {
      strWhere = strWhere + " and dap_finindex.pk_glorg=? ";
    }
    if ((voMsg != null) && (voMsg[0].getPkAccount() != null)) {
      strWhere = strWhere + " and dap_finindex.pk_glbook=? ";
    }

    strTemp = strTemp + strWhere + " order by dap_finindex.billcode,voucherdate,vouchtypename ,voucherno ";

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    ArrayList vMsg = new ArrayList();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      if ((voMsg == null) && 
        (!qvo.notRtVouch))
      {
        stmt.setString(i++, qvo.pk_Corp);
      }

      if (voMsg != null) {
        if (voMsg[0].getPkFinmsg() != null) {
          stmt.setString(i++, voMsg[0].getPkFinmsg());
        }

        if (voMsg[0].getProcMsg() != null) {
          stmt.setString(i++, voMsg[0].getProcMsg() + "%");
        }

        if (voMsg[0].getBillCode() != null) {
          stmt.setString(i++, voMsg[0].getBillCode());
        }

        for (int iArr = 0; iArr < voMsg.length; iArr++) {
          if (voMsg[iArr].getProc() != null) {
            stmt.setString(i++, voMsg[iArr].getSys());
            stmt.setString(i++, voMsg[iArr].getProc());
          }
        }

        if (voMsg[0].getBusiType() != null) {
          stmt.setString(i++, voMsg[0].getBusiType());
        }

        if (voMsg[0].getCorp() != null) {
          stmt.setString(i++, voMsg[0].getCorp());
        }

        if (voMsg[0].getBusiDate() != null) {
          stmt.setString(i++, voMsg[0].getBusiDate().toString());
        }

        if (voMsg[0].getVoucherDate() != null) {
          stmt.setString(i++, voMsg[0].getVoucherDate().toString());
        }

        if (voMsg[0].getVoucherNo() != null) {
          stmt.setString(i++, voMsg[0].getVoucherNo().toString());
        }

        if (voMsg[0].getOperator() != null) {
          stmt.setString(i++, voMsg[0].getOperator());
        }

        if (voMsg[0].getFlag() != null) {
          stmt.setInt(i++, voMsg[0].getFlag().intValue());
        }

        if (voMsg[0].getCurrency() != null) {
          stmt.setString(i++, voMsg[0].getCurrency());
        }

        if (voMsg[0].getMoney() != null) {
          stmt.setDouble(i++, voMsg[0].getMoney().doubleValue());
        }

        if (voMsg[0].getComment() != null) {
          stmt.setString(i++, '%' + voMsg[0].getComment() + '%');
        }

        if (voMsg[0].getPerpared() != null) {
          stmt.setString(i++, voMsg[0].getPerpared());
        }

        if (voMsg[0].getVoucherType() != null) {
          stmt.setString(i++, voMsg[0].getVoucherType());
        }

        if (bMax) {
          if (voMsg[1].getBillCode() != null) {
            stmt.setString(i++, voMsg[1].getBillCode());
          }
          if (voMsg[1].getBusiDate() != null) {
            stmt.setString(i++, voMsg[1].getBusiDate().toString());
          }
          if (voMsg[1].getMoney() != null) {
            stmt.setDouble(i++, voMsg[1].getMoney().doubleValue());
          }
          if (voMsg[1].getVoucherDate() != null) {
            stmt.setString(i++, voMsg[1].getVoucherDate().toString());
          }
          if (voMsg[1].getVoucherNo() != null) {
            stmt.setString(i++, voMsg[1].getVoucherNo().toString());
          }
        }

        if (voMsg[0].getPkRtVouch() != null) {
          stmt.setString(i++, voMsg[0].getPkRtVouch());
        }

        if (voMsg[0].getQueryBeginDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryBeginDate().toString());
        }
        if (voMsg[0].getQueryEndDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryEndDate().toString());
        }

        if (voMsg[0].getVouchEntry() != null) {
          stmt.setString(i++, voMsg[0].getVouchEntry());
        }

        if (voMsg[0].getPkAccOrg() != null) {
          stmt.setString(i++, voMsg[0].getPkAccOrg());
        }
        if (voMsg[0].getPkAccount() != null) {
          stmt.setString(i++, voMsg[0].getPkAccount());
        }
      }

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;

      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        voTemp.setSysname(Translator.getSystemName(null, strValue));

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setComment(strValue);

        strValue = rsTemp.getString("pk_prepared");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("settlemode");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setErrMsg(strValue);

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        int iNo = rsTemp.getInt("voucherno");
        
        //反编译后报错，edit by yqq  2016-07-18
        voTemp.setVoucherNo((new StringBuilder(String.valueOf(iNo))).toString());
  //    voTemp.setVoucherNo(iNo);

        strValue = rsTemp.getString("voucherdate");
        if (strValue != null) {
          voTemp.setVoucherDate(strValue.trim());
        }

        strValue = rsTemp.getString("billtypename");

        voTemp.setProcName(Translator.getBillTypeName(strValue, voTemp.getProc()));

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        strValue = rsTemp.getString("user_name");
        if (strValue != null) {
          voTemp.setPerparedName(strValue.trim());
        }

        strValue = rsTemp.getString("psnname");
        if (strValue != null) {
          voTemp.setOperatorName(strValue.trim());
        }

        strValue = rsTemp.getString("currtypename");
        if (strValue != null) {
          voTemp.setCurrencyName(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        Object o = rsTemp.getBigDecimal("totaldebit");
        if (o != null) {
          voTemp.setDebitMoney(new UFDouble(o.toString().trim()));
        }

        o = rsTemp.getBigDecimal("totalcredit");
        if (o != null) {
          voTemp.setCreditMoney(new UFDouble(o.toString().trim()));
        }

        strValue = rsTemp.getString("vouchtypename");
        if (strValue != null) {
          voTemp.setVoucherTypeName(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));

        strValue = rsTemp.getString("assino");
        if (strValue != null) {
          voTemp.setAssino(strValue.trim());
        }
        vMsg.add(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.toArray(voMsgArr);
    }
    return voMsgArr;
  }

  public DapFinMsgVO[] queryMsgQueue(QueryLogVO qvo)
    throws SQLException
  {
    return queryByVO(null, qvo);
  }

  public nc.vo.dap.inteface.VoucherVO[] queryPkVoucher(String pkBillType, String pkBillId)
    throws SQLException
  {
    String strTemp = "select pk_vouchentry,pk_glorg,pk_glbook from dap_finindex where procmsg=?";

    String pkVoucher = null;
    Connection con = null;
    PreparedStatement stmt = null;
    nc.vo.dap.inteface.VoucherVO rtVO = null;
    nc.vo.dap.inteface.VoucherVO[] rtVOs = (nc.vo.dap.inteface.VoucherVO[])null;
    Vector temp = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      stmt.setString(1, pkBillId);

      ResultSet rsTemp = stmt.executeQuery();
      while (rsTemp.next()) {
        rtVO = new nc.vo.dap.inteface.VoucherVO();
        pkVoucher = rsTemp.getString("pk_vouchentry");
        if (pkVoucher != null) {
          rtVO.m_pk_voucher = pkVoucher;
          rtVO.m_pk_glorg = rsTemp.getString("pk_glorg");
          rtVO.m_pk_glbook = rsTemp.getString("pk_glbook");
          temp.add(rtVO);
        }
      }
      if (temp.size() != 0) {
        rtVOs = new nc.vo.dap.inteface.VoucherVO[temp.size()];
        temp.copyInto(rtVOs);
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return rtVOs;
  }

  private Hashtable queryPkVouchers(String[] billPKs)
    throws SQLException
  {
    if ((billPKs == null) || (billPKs.length == 0)) {
      return null;
    }
    String sTmpTabName = createtmpTabQryByBillPks(billPKs);
    String strTemp = "select tmp.pk_bill,pk_vouchentry,dap_finindex.destsystem from " + sTmpTabName + 
      " tmp left outer join dap_finindex on tmp.pk_bill=dap_finindex.procmsg ";

    String pkVoucher = null;
    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable hashRet = new Hashtable();
    ArrayList al = new ArrayList();
    Integer destSystem = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;
      String strKey = null;
      int i = 0;
      while (rsTemp.next()) {
        strKey = rsTemp.getString(1);
        pkVoucher = rsTemp.getString("pk_vouchentry");
        destSystem = new Integer(rsTemp.getInt(3));
        if (pkVoucher != null) {
          pkVoucher = pkVoucher.trim();
        }
        if ((strKey != null) && (pkVoucher != null)) {
          hashRet.put(strKey.trim(), pkVoucher);
          al.add(pkVoucher);
        }
      }
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
      try {
        dropTempTable(sTmpTabName);
      } catch (Exception localException2) {
      }
    }
    String[] sResults = new String[al.size()];
    if (al.size() > 0) {
      sResults = (String[])al.toArray(sResults);
    }
    hashRet.put("ALL_Bill", sResults);
    hashRet.put("DESTSYSTEM", destSystem);
    return hashRet;
  }

  public String queryReflectPk(String pkCorp, String pkSys, String pkBillType, String pkBusiType, String procMsg)
    throws SQLException
  {
    String strTemp = "select pk_finindex from dap_finindex ";
    String strWhere = "";
    if (pkCorp != null) {
      strWhere = strWhere + "and pk_corp=? ";
    }
    if (pkSys != null) {
      strWhere = strWhere + "and pk_sys=? ";
    }
    if (pkBillType != null) {
      strWhere = strWhere + "and pk_proc=? ";
    }
    if (pkBusiType != null) {
      strWhere = strWhere + "and pk_busitype=? ";
    }
    if (procMsg != null) {
      strWhere = strWhere + "and pk_procmsg=? ";
    }
    if (strWhere.equals("")) {
      strTemp = strTemp + " where " + strWhere.substring(3);
    }

    int i = 1;
    String pkMsgIndex = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      if (pkCorp != null) {
        stmt.setString(i++, pkCorp);
      }
      if (pkSys != null) {
        stmt.setString(i++, pkSys);
      }
      if (pkBillType != null) {
        stmt.setString(i++, pkBillType);
      }
      if (pkBusiType != null) {
        stmt.setString(i++, pkBusiType);
      }
      if (procMsg != null) {
        stmt.setString(i++, procMsg);
      }

      ResultSet rsTemp = stmt.executeQuery();
      if (rsTemp.next())
        pkMsgIndex = rsTemp.getString("pk_finindex");
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return pkMsgIndex;
  }

  public DapFinMsgVO[] querySettle_ByVO(DapFinMsgVO[] voMsg, QueryLogVO qvo)
    throws SQLException
  {
    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc, billtypename, pk_busitype, businame,busidate, operator, dap_finindex.pk_corp, flag, currency,money, dapcomment, fts_voucher.makevuser as checker, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch,fts_voucher.cent_typeID as voucherno, fts_voucher.makevdate as voucherdate, procmsg,sm_user.user_name,a.user_name psnname, currtypename ,fts_voucher.pk_vouchertype as vouchertype, fts_voucher.totaldebit as totaldebit, fts_voucher.totalcredit as totalcredit, bd_vouchertype.vouchtypename as vouchtypename,dap_finindex.destsystem,dap_rtvouch.assino from (((((((dap_finindex left outer join sm_user a on dap_finindex.operator=a.cUserId) left outer join bd_billtype on dap_finindex.pk_proc=bd_billtype.pk_billtypecode) left outer join bd_currtype on dap_finindex.currency=bd_currtype.pk_currtype) left outer join fts_voucher on dap_finindex.pk_vouchentry=fts_voucher.pk_voucher) left outer join sm_user on fts_voucher.makevuser=sm_user.cUserId) left outer join bd_vouchertype on fts_voucher.pk_vouchertype = bd_vouchertype.pk_vouchertype) left outer join dap_rtvouch on dap_finindex.pk_rtvouch = dap_rtvouch.pk_voucher) ";
    String strWhere = "";
    String strArr = "";

    boolean bMax = false;

    if (voMsg == null) {
      if (qvo.notRtVouch)
        strWhere = strWhere + " where flag=0";
      else
        strWhere = strWhere + " where dap_finindex.pk_corp=? and not (errmsg is null) ";
    }
    else if (voMsg != null) {
      if (voMsg.length > 1) {
        bMax = true;
      }

      switch (qvo.queryClass) {
      case 0:
        strWhere = strWhere + "and flag<" + 4 + " ";

        break;
      case 1:
        strWhere = strWhere + "and flag>=" + 4 + " ";
        break;
      case 3:
        strWhere = strWhere + "and flag=" + 2 + " ";
        break;
      case 2:
      }

      if (voMsg[0].getPkFinmsg() != null) {
        strWhere = strWhere + "and pk_finindex=? ";
      }

      if (voMsg[0].getProcMsg() != null) {
        strWhere = strWhere + "and procmsg like ? ";
      }

      if ((qvo.billIDs != null) && (qvo.billIDs.length > 0)) {
        String strBillID = "(";
        for (int i = 0; i < qvo.billIDs.length; i++) {
          if (i == qvo.billIDs.length - 1) {
            strBillID = strBillID + "'" + qvo.billIDs[i] + "'";
          }
          else
            strBillID = strBillID + "'" + qvo.billIDs[i] + "',";
        }
        strBillID = strBillID + ")";
        strWhere = strWhere + "and procmsg in " + strBillID;
      }

      if ((voMsg[0].getBillCode() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.billcode=? ";
      else if (voMsg[0].getBillCode() != null) {
        strWhere = strWhere + "and dap_finindex.billcode>=? ";
      }

      for (int iArr = 0; iArr < voMsg.length; iArr++) {
        if (voMsg[iArr].getProc() != null)
          strArr = strArr + "or (pk_sys=? and pk_proc=?) ";
        if ((iArr == voMsg.length - 1) && (strArr.length() != 0)) {
          strWhere = strWhere + "and (" + strArr.substring(2, strArr.length()) + ") ";
        }
      }
      if (voMsg[0].getBusiType() != null) {
        strWhere = strWhere + "and dap_finindex.pk_busitype=? ";
      }

      if (voMsg[0].getCorp() != null) {
        strWhere = strWhere + "and dap_finindex.pk_corp=? ";
      }

      if ((voMsg[0].getBusiDate() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.busidate=? ";
      else if (voMsg[0].getBusiDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }

      if ((voMsg[0].getVoucherDate() != null) && (!bMax))
        strWhere = strWhere + "and fts_voucher.makevdate=? ";
      else if (voMsg[0].getVoucherDate() != null) {
        strWhere = strWhere + "and fts_voucher.makevdate>=? ";
      }

      if ((voMsg[0].getVoucherNo() != null) && (!bMax))
        strWhere = strWhere + "and fts_voucher.cent_typeID=? ";
      else if (voMsg[0].getVoucherNo() != null) {
        strWhere = strWhere + "and fts_voucher.cent_typeID>=? ";
      }

      if (voMsg[0].getOperator() != null) {
        strWhere = strWhere + "and dap_finindex.operator=? ";
      }

      if (voMsg[0].getFlag() != null) {
        strWhere = strWhere + "and dap_finindex.flag=? ";
      }

      if (voMsg[0].getCurrency() != null) {
        strWhere = strWhere + "and dap_finindex.currency=? ";
      }

      if ((voMsg[0].getMoney() != null) && (!bMax))
        strWhere = strWhere + "and dap_finindex.money=? ";
      else if (voMsg[0].getMoney() != null) {
        strWhere = strWhere + "and dap_finindex.money>=? ";
      }

      if (voMsg[0].getComment() != null) {
        strWhere = strWhere + "and dap_finindex.dapcomment like ? ";
      }

      if (voMsg[0].getChecker() != null) {
        strWhere = strWhere + "and fts_voucher.makevuser=? ";
      }

      if (voMsg[0].getVoucherType() != null) {
        strWhere = strWhere + "and fts_voucher.pk_vouchertype=? ";
      }

      if (bMax) {
        if (voMsg[1].getBillCode() != null) {
          strWhere = strWhere + "and billcode<=? ";
        }
        if (voMsg[1].getBusiDate() != null) {
          strWhere = strWhere + "and dap_finindex.busidate<=? ";
        }
        if (voMsg[1].getMoney() != null) {
          strWhere = strWhere + "and dap_finindex.money<=? ";
        }
        if (voMsg[1].getVoucherDate() != null) {
          strWhere = strWhere + "and fts_voucher.makevdate<=? ";
        }
        if (voMsg[1].getVoucherNo() != null) {
          strWhere = strWhere + "and fts_voucher.cent_typeID<=? ";
        }
      }

      if (voMsg[0].getPkRtVouch() != null) {
        strWhere = strWhere + "and dap_finindex.pk_rtvouch=? ";
      }

      if (voMsg[0].getQueryBeginDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate>=? ";
      }
      if (voMsg[0].getQueryEndDate() != null) {
        strWhere = strWhere + "and dap_finindex.busidate<=? ";
      }

      if (strWhere.length() > 0) {
        if (voMsg[0].getVouchEntry() == null)
          strWhere = "where " + strWhere.substring(3, strWhere.length() - 1);
        else {
          strWhere = "where (" + strWhere.substring(3, strWhere.length() - 1) + ") and pk_vouchentry=?";
        }
      }
      else if (voMsg[0].getVouchEntry() == null)
        strWhere = "";
      else {
        strWhere = "where pk_vouchentry=? ";
      }

    }

    if ((strWhere == null) || (strWhere.trim().length() <= 0))
      strWhere = " where fts_voucher.dr is null or fts_voucher.dr=0 ";
    else {
      strWhere = strWhere + " and (fts_voucher.dr is null or fts_voucher.dr=0) ";
    }
    if (qvo.destSystem != -1) {
      strWhere = strWhere + " and dap_finindex.destsystem=" + qvo.destSystem;
    }
    if (!qvo.notRtVouch)
    {
      strWhere = strWhere + " and not (dap_finindex.pk_rtvouch is null)";
    }

    strTemp = strTemp + strWhere + " order by dap_finindex.billcode,voucherdate,vouchtypename ,voucherno ";

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    Vector vMsg = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      if ((voMsg == null) && 
        (!qvo.notRtVouch))
      {
        stmt.setString(i++, qvo.pk_Corp);
      }

      if (voMsg != null) {
        if (voMsg[0].getPkFinmsg() != null) {
          stmt.setString(i++, voMsg[0].getPkFinmsg());
        }

        if (voMsg[0].getProcMsg() != null) {
          stmt.setString(i++, voMsg[0].getProcMsg() + "%");
        }

        if (voMsg[0].getBillCode() != null) {
          stmt.setString(i++, voMsg[0].getBillCode());
        }

        for (int iArr = 0; iArr < voMsg.length; iArr++) {
          if (voMsg[iArr].getProc() != null) {
            stmt.setString(i++, voMsg[iArr].getSys());
            stmt.setString(i++, voMsg[iArr].getProc());
          }

        }

        if (voMsg[0].getBusiType() != null) {
          stmt.setString(i++, voMsg[0].getBusiType());
        }

        if (voMsg[0].getCorp() != null) {
          stmt.setString(i++, voMsg[0].getCorp());
        }

        if (voMsg[0].getBusiDate() != null) {
          stmt.setString(i++, voMsg[0].getBusiDate().toString());
        }

        if (voMsg[0].getVoucherDate() != null) {
          stmt.setString(i++, voMsg[0].getVoucherDate().toString());
        }

        if (voMsg[0].getVoucherNo() != null) {
          stmt.setString(i++, voMsg[0].getVoucherNo().toString());
        }

        if (voMsg[0].getOperator() != null) {
          stmt.setString(i++, voMsg[0].getOperator());
        }

        if (voMsg[0].getFlag() != null) {
          stmt.setInt(i++, voMsg[0].getFlag().intValue());
        }

        if (voMsg[0].getCurrency() != null) {
          stmt.setString(i++, voMsg[0].getCurrency());
        }

        if (voMsg[0].getMoney() != null) {
          stmt.setDouble(i++, voMsg[0].getMoney().doubleValue());
        }

        if (voMsg[0].getComment() != null) {
          stmt.setString(i++, '%' + voMsg[0].getComment() + '%');
        }

        if (voMsg[0].getChecker() != null) {
          stmt.setString(i++, voMsg[0].getChecker());
        }

        if (voMsg[0].getVoucherType() != null) {
          stmt.setString(i++, voMsg[0].getVoucherType());
        }

        if (bMax) {
          if (voMsg[1].getBillCode() != null) {
            stmt.setString(i++, voMsg[1].getBillCode());
          }
          if (voMsg[1].getBusiDate() != null) {
            stmt.setString(i++, voMsg[1].getBusiDate().toString());
          }
          if (voMsg[1].getMoney() != null) {
            stmt.setDouble(i++, voMsg[1].getMoney().doubleValue());
          }
          if (voMsg[1].getVoucherDate() != null) {
            stmt.setString(i++, voMsg[1].getVoucherDate().toString());
          }
          if (voMsg[1].getVoucherNo() != null) {
            stmt.setString(i++, voMsg[1].getVoucherNo().toString());
          }
        }

        if (voMsg[0].getPkRtVouch() != null) {
          stmt.setString(i++, voMsg[0].getPkRtVouch());
        }

        if (voMsg[0].getQueryBeginDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryBeginDate().toString());
        }
        if (voMsg[0].getQueryEndDate() != null) {
          stmt.setString(i++, voMsg[0].getQueryEndDate().toString());
        }

        if (voMsg[0].getVouchEntry() != null) {
          stmt.setString(i++, voMsg[0].getVouchEntry());
        }
      }

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;

      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        strValue = rsTemp.getString("pk_sys");
        if (strValue != null) {
          voTemp.setSys(strValue.trim());
        }

        voTemp.setSysname(Translator.getSystemName(null, voTemp.getSys()));

        strValue = rsTemp.getString("pk_proc");
        if (strValue != null) {
          voTemp.setProc(strValue.trim());
        }

        voTemp.setProcName(Translator.getBillTypeName(null, voTemp.getProc()));

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : businame.trim();
        businame = Translator.bsTranslate("fidap", businame);
        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setComment(strValue);

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("settlemode");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setErrMsg(strValue);

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        int iNo = rsTemp.getInt("voucherno");
        
        //反编译后报错，edit by yqq  2016-07-18
        voTemp.setVoucherNo((new StringBuilder(String.valueOf(iNo))).toString());
  //    voTemp.setVoucherNo(iNo);

        strValue = rsTemp.getString("voucherdate");
        if (strValue != null) {
          voTemp.setVoucherDate(strValue.trim());
        }

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        strValue = rsTemp.getString("user_name");
        if (strValue != null) {
          voTemp.setCheckerName(strValue.trim());
        }

        strValue = rsTemp.getString("psnname");
        if (strValue != null) {
          voTemp.setOperatorName(strValue.trim());
        }

        strValue = rsTemp.getString("currtypename");
        if (strValue != null) {
          voTemp.setCurrencyName(strValue.trim());
        }

        strValue = rsTemp.getString("vouchertype");
        if (strValue != null) {
          voTemp.setVoucherType(strValue.trim());
        }

        Object o = rsTemp.getBigDecimal("totaldebit");
        if (o != null) {
          voTemp.setDebitMoney(new UFDouble(o.toString().trim()));
        }

        o = rsTemp.getBigDecimal("totalcredit");
        if (o != null) {
          voTemp.setCreditMoney(new UFDouble(o.toString().trim()));
        }

        strValue = rsTemp.getString("vouchtypename");
        if (strValue != null) {
          voTemp.setVoucherTypeName(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));

        strValue = rsTemp.getString("assino");
        if (strValue != null) {
          voTemp.setAssino(strValue.trim());
        }
        vMsg.addElement(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.copyInto(voMsgArr);
    }
    return voMsgArr;
  }

  public nc.vo.dap.inteface.VoucherVO[] queryVouchersByBillPK(String billType, String[] billPKs)
    throws SQLException
  {
    if ((billPKs == null) || (billPKs.length == 0)) {
      return null;
    }

    nc.vo.dap.inteface.VoucherVO[] vouchers = new nc.vo.dap.inteface.VoucherVO[billPKs.length];
    try {
      Hashtable hashPkVouch = queryPkVouchers(billPKs);
      Integer destSystem = (Integer)hashPkVouch.get("DESTSYSTEM");
      nc.vo.dap.inteface.VoucherVO[] tempVouchs = CallInterface.getInstance().getVoucherQueryBO(null, destSystem.intValue())
        .queryByPks((String[])hashPkVouch.get("ALL_Bill"));
      if ((tempVouchs == null) || (tempVouchs.length <= 0)) {
        return null;
      }
      Hashtable hashtmp = new Hashtable();
      for (int i = 0; i < tempVouchs.length; i++) {
        hashtmp.put(tempVouchs[i].getPk_voucher(), tempVouchs[i]);
      }
      Object stmp = null;
      for (int i = 0; i < billPKs.length; i++) {
        stmp = hashPkVouch.get(billPKs[i]);
        if (stmp != null)
          vouchers[i] = ((nc.vo.dap.inteface.VoucherVO)hashtmp.get(stmp));
        else {
          vouchers[i] = null;
        }
      }
      return vouchers;
    } catch (Exception e) {
      Logger.error(e);
      //反编译后报错，edit by yqq  2016-07-18
      throw new SQLException(e.getMessage());
    }
//    }throw new SQLException(e.getMessage());
  }

  public int reflectState(DapFinMsgVO inVo)
    throws SQLException
  {
    int tmpInt = 0;
    String sql = "select flag from dap_finindex where pk_rtvouch = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      DapExecTypeVO execVo = new DapExecTypeVO();
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, inVo.getPkRtVouch());
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        tmpInt = rs.getInt("flag");
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return tmpInt;
  }

  public void runBackVoucher(String pkProc, String[] pkBillAry, String voucherid, String pk_glorg, String pk_glbook, String pk_sys, String pk_corp, boolean blankOrFill)
    throws SQLException
  {
    IAccountRetVoucher retVoucher = null;
    try {
      Object obj = BsPubUtil.loadAccountClassByBillType(pkProc);
      if ((obj instanceof IAccountRetVoucher))
        retVoucher = (IAccountRetVoucher)obj;
      else
        return;
    } catch (Exception ex) {
      Logger.error(ex);
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000155") + (
        ex.getMessage() == null ? "" : ex.getMessage()));
    }
    try {
      if (blankOrFill)
        retVoucher.BackBlankVoucher(pkProc, voucherid, pk_glorg, pk_glbook, pk_sys, pk_corp);
      else
        retVoucher.BackFillVoucher(pkProc, pkBillAry, voucherid, pk_glorg, pk_glbook, pk_sys, pk_corp);
    } catch (Exception ex) {
      Logger.error(ex);
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000156") + 
        ex.getMessage());
    }
  }

  public int updateBackCheckState(String Voucher, String checkStr, int Flag)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;
    strTemp = "update dap_finindex set flag=?,checkstate='N',vouchertype=? where pk_vouchentry=? and checkstate='Y'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setInt(iPara++, Flag);

      stmt.setString(iPara++, checkStr);

      stmt.setString(iPara++, Voucher);

      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public int updateBackFlag(String Voucher, int Flag)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;
    strTemp = "update dap_finindex set flag=? where pk_vouchentry=? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setInt(iPara++, Flag);

      stmt.setString(iPara++, Voucher);

      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public void updateErrMsg(String pkMsg, String errMsg)
    throws SQLException
  {
    String strTemp = null;
    if (pkMsg.startsWith("#")) {
      pkMsg = pkMsg.substring(1);
      strTemp = "update dap_finindex set errmsg=? ,flag=0 where pk_finindex=? and flag<4";
    } else {
      strTemp = "update dap_finindex set errmsg=? where pk_finindex=?  and flag<4";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setString(1, PfComm.sub_txt(errMsg, 100));

      stmt.setString(2, pkMsg);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public void updateExecType(String pkMsg, int execType)
    throws SQLException
  {
    String strTemp = "update dap_finindex set sign=? where pk_finindex=?";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setInt(1, execType);

      stmt.setString(2, pkMsg);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public void updateFlag(String pkMsg, int flag)
    throws SQLException
  {
    String strTemp = "update dap_finindex set flag=? where pk_finindex=?";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setInt(1, flag);

      stmt.setString(2, pkMsg);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public int updateRedFlag(String Voucher, String redFlag)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;
    strTemp = "update dap_finindex set mergebatchcode=?,flag=? ,PK_VOUCHENTRY=?,pk_rtvouch= ?,vouchertype=?,checkstate=? where pk_finindex=? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setString(iPara++, redFlag);

      stmt.setInt(iPara++, 0);
      stmt.setString(iPara++, null);
      stmt.setString(iPara++, null);
      stmt.setString(iPara++, null);
      stmt.setString(iPara++, "N");

      stmt.setString(iPara++, Voucher);
      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public void updateRedFlagByFinindex(String[] pk_finindex)
    throws SQLException
  {
    int FETCHSIZE = 200;
    String strUpdate = "update dap_finindex set mergebatchcode = NULL where pk_finindex in ";

    if (pk_finindex == null) {
      return;
    }
    int repetitionTimes = pk_finindex.length / FETCHSIZE + (pk_finindex.length % FETCHSIZE > 0 ? 1 : 0);

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();

      int beginIndex = 0; int endIndex = 0;

      for (int i = 0; i < repetitionTimes; i++)
      {
        endIndex = FETCHSIZE * (i + 1) > pk_finindex.length ? pk_finindex.length : FETCHSIZE * (i + 1);

        String strIN = "";

        for (int j = beginIndex; j < endIndex; j++) {
          strIN = strIN + "'" + pk_finindex[i] + "',";
        }
        strIN = "(" + strIN.substring(0, strIN.length() - 1) + ")";

        stmt = con.prepareStatement(strUpdate + strIN);
        stmt.executeUpdate();
        stmt.close();

        beginIndex = endIndex;
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public int updateRelectFlag(String Voucher, int nowFlag, int oldFlag, String checkStr, boolean isGLProc)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;

    if (isGLProc) {
      strTemp = "update dap_finindex set flag=?,checkstate='Y',vouchertype=? where pk_vouchentry=? and flag=?";
    }
    else if (nowFlag == 0)
      strTemp = "update dap_finindex set flag=?,checkstate='N',vouchertype=?,pk_vouchentry=? where pk_vouchentry=? and flag=?";
    else {
      strTemp = "update dap_finindex set flag=?,checkstate='N',vouchertype=? where pk_vouchentry=? and flag=?";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setInt(iPara++, nowFlag);

      stmt.setString(iPara++, checkStr);

      if (nowFlag == 0) {
        stmt.setString(iPara++, null);
      }

      stmt.setString(iPara++, Voucher);

      stmt.setInt(iPara++, oldFlag);

      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public int updateRelectFlag(String Voucher, int nowFlag, int oldFlag, String checkStr, boolean isGLProc, String sMergCode)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;

    if (isGLProc) {
      strTemp = "update dap_finindex set flag=?,checkstate='Y',vouchertype=?,mergebatchcode=? where pk_vouchentry=? and flag=?";
    }
    else if (nowFlag == 0)
      strTemp = "update dap_finindex set flag=?,checkstate='N',vouchertype=?,pk_vouchentry=?,mergebatchcode=? where pk_vouchentry=? and flag=?";
    else {
      strTemp = "update dap_finindex set flag=?,checkstate='N',vouchertype=?,mergebatchcode=? where pk_vouchentry=? and flag=?";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setInt(iPara++, nowFlag);

      stmt.setString(iPara++, checkStr);

      if (nowFlag == 0) {
        stmt.setString(iPara++, null);
      }

      stmt.setString(iPara++, sMergCode);

      stmt.setString(iPara++, Voucher);

      stmt.setInt(iPara++, oldFlag);

      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public int updateRelectFlagByCheck(String Voucher, String nowCheckStr, String oldCheckStr)
    throws SQLException
  {
    int retInt = 0;
    String strTemp = null;

    strTemp = "update dap_finindex set vouchertype=? where pk_vouchentry=? and vouchertype=?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      int iPara = 1;

      stmt.setString(iPara++, nowCheckStr);

      stmt.setString(iPara++, Voucher);

      stmt.setString(iPara++, oldCheckStr);

      retInt = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return retInt;
  }

  public void updateRtVouch(String pkRtVouch, String pkMsg, Integer execType, Integer intflag)
    throws SQLException
  {
    String strTemp = "update dap_finindex set pk_rtvouch=?,errmsg=null,mergebatchcode=null,";
    if (execType != null)
      strTemp = strTemp + "sign=?,";
    if (intflag != null) {
      strTemp = strTemp + "flag=?,";
    }
    strTemp = strTemp.substring(0, strTemp.length() - 1);
    strTemp = strTemp + " where pk_finindex=?";

    int iPara = 1;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setString(iPara++, pkRtVouch);
      if (execType != null) {
        stmt.setInt(iPara++, execType.intValue());
      }
      if (intflag != null) {
        stmt.setInt(iPara++, intflag.intValue());
      }

      stmt.setString(iPara++, pkMsg);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public void updateRtVouch(Properties p, int intflag)
    throws SQLException
  {
    String tableName = createtmpTabQryByBillPks(p);
    String strTemp = "update dap_finindex set pk_rtvouch=a.pk_bill1,errmsg=null,";
    strTemp = strTemp + "flag=?,";

    strTemp = strTemp.substring(0, strTemp.length() - 1);
    strTemp = strTemp + " from " + tableName + " a where pk_finindex=a.pk_bill";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);
      stmt.setInt(1, intflag);
      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  private int updateVoucher(String Voucher, String pkRtvouch, boolean isPkRtVouch)
    throws SQLException
  {
    int tmpInt = 0;
    String strTemp = null;
    if (isPkRtVouch)
      strTemp = "update dap_finindex set pk_vouchentry=?,flag=?,errmsg=null where pk_rtvouch=? and flag=?";
    else {
      strTemp = "update dap_finindex set pk_vouchentry=?,flag=?,errmsg=null where pk_finindex=? and flag=?";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setString(1, Voucher);

      stmt.setInt(2, 4);

      stmt.setString(3, pkRtvouch);

      stmt.setInt(4, 2);
      tmpInt = stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return tmpInt;
  }

  private int updateVoucher(String Voucher, String tableName, boolean isPkRtVouch, int i)
    throws SQLException
  {
    int tmpInt = 0;
    String strTemp = null;

    if (isPkRtVouch)
      strTemp = "update dap_finindex set pk_vouchentry=?,flag=?,errmsg=null  where pk_rtvouch in (select pk_bill from " + 
        tableName + ") and flag=?";
    else {
      strTemp = "update dap_finindex set pk_vouchentry=?,flag=?,errmsg=null  where pk_finindex in (select pk_bill from " + 
        tableName + ") and flag=?";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setString(1, Voucher);

      stmt.setInt(2, 4);

      stmt.setInt(3, 2);
      tmpInt = stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return tmpInt;
  }

  public int updateFlagRtVoucher(String pkMsg, int flag)
    throws SQLException
  {
    String strTemp = "update dap_finindex set flag=?,mergebatchcode=null where pk_finindex=? and flag<4";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strTemp);

      stmt.setInt(1, flag);

      stmt.setString(2, pkMsg);
      int res = stmt.executeUpdate();
      int i = res;
      return i;
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException2) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception localException3) {
      }
    }
    //反编译后报错，edit by yqq  2016-07-18
 //   throw localObject;
  }

  public DapFinMsgVO[] queryAllByVO(DapFinMsgVO voMsg, String whereSql)
    throws SQLException
  {
    String strTemp = "select pk_finindex,billcode,pk_sys,pk_proc,pk_busitype, businame,busidate, operator, checker,  pk_corp, flag, currency,money, dapcomment, settlemode, docnum,docdate, pk_vouchentry, deltag, sign, pk_mergebatch,mergebatchcode, errmsg, checkstate, pk_rtvouch, billtypename, procmsg destsystem,  pk_glorg, pk_glbook from dap_finindex  ";

    String strWhere = " 1=1 ";

    strWhere = strWhere + "and flag=" + 2 + " ";

    if (voMsg.getBillCode() != null) {
      strWhere = strWhere + "and dap_finindex.billcode=? ";
    }

    if (voMsg.getBusiType() != null) {
      strWhere = strWhere + "and dap_finindex.pk_busitype=? ";
    }

    if (voMsg.getCorp() != null) {
      strWhere = strWhere + "and dap_finindex.pk_corp=? ";
    }

    if (voMsg.getSys() != null)
      strWhere = strWhere + " and  pk_sys=?  ";
    if (voMsg.getProc() != null) {
      strWhere = strWhere + " and  pk_proc=?  ";
    }

    if (voMsg.getQueryEndDate() != null) {
      strWhere = strWhere + "and dap_finindex.busidate <=? ";
    }

    if (voMsg.getFlag() != null) {
      strWhere = strWhere + "and dap_finindex.flag =? ";
    }

    if (voMsg.getCurrency() != null) {
      strWhere = strWhere + "and dap_finindex.currency=? ";
    }

    if (voMsg.getMoney() != null) {
      strWhere = strWhere + "and dap_finindex.money=? ";
    }

    if (voMsg.getComment() != null) {
      strWhere = strWhere + "and dap_finindex.dapcomment like ? ";
    }

    if (voMsg.getOperator() != null) {
      strWhere = strWhere + "and dap_finindex.operator=? ";
    }

    if (voMsg.getChecker() != null) {
      strWhere = strWhere + "and dap_finindex.checker=? ";
    }

    String strDest = null;
    if (voMsg.getDestSystem() != -1) {
      strDest = " and dap_finindex.destsystem=" + voMsg.getDestSystem();
    }
    if (strDest != null) {
      strWhere = strWhere + strDest;
    }
    if (voMsg.getPkAccOrg() != null) {
      strWhere = strWhere + " and dap_finindex.pk_glorg=? ";
    }
    if (voMsg.getPkAccount() != null) {
      strWhere = strWhere + " and dap_finindex.pk_glbook=? ";
    }
    if ((whereSql != null) && (whereSql.trim().length() > 0)) {
      strWhere = strWhere + " and " + whereSql;
    }
    String sql = null;

    int i = 1;
    DapFinMsgVO[] voMsgArr = (DapFinMsgVO[])null;
    Vector vMsg = new Vector();
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    try {
      con = (CrossDBConnection)getConnection();
      con.setSqlTrans(false);
      sql = strTemp + strWhere;
      stmt = con.prepareStatement(sql);

      if (voMsg.getBillCode() != null) {
        stmt.setString(i++, voMsg.getBillCode());
      }

      if (voMsg.getBusiType() != null) {
        stmt.setString(i++, voMsg.getBusiType());
      }

      if (voMsg.getCorp() != null) {
        stmt.setString(i++, voMsg.getCorp());
      }
      if (voMsg.getSys() != null) {
        stmt.setString(i++, voMsg.getSys());
      }
      if (voMsg.getProc() != null) {
        stmt.setString(i++, voMsg.getProc());
      }

      if (voMsg.getQueryEndDate() != null) {
        stmt.setString(i++, voMsg.getQueryEndDate().toString());
      }

      if (voMsg.getFlag() != null) {
        stmt.setInt(i++, voMsg.getFlag().intValue());
      }

      if (voMsg.getCurrency() != null) {
        stmt.setString(i++, voMsg.getCurrency());
      }

      if (voMsg.getComment() != null) {
        stmt.setString(i++, '%' + voMsg.getComment() + '%');
      }

      if (voMsg.getOperator() != null) {
        stmt.setString(i++, voMsg.getOperator());
      }

      if (voMsg.getChecker() != null) {
        stmt.setString(i++, voMsg.getChecker());
      }

      ResultSet rsTemp = stmt.executeQuery();
      String strValue = null;

      while (rsTemp.next()) {
        DapFinMsgVO voTemp = new DapFinMsgVO();

        strValue = rsTemp.getString("pk_finindex");
        if (strValue != null) {
          voTemp.setPkFinmsg(strValue.trim());
        }

        strValue = rsTemp.getString("billcode");
        if (strValue != null) {
          voTemp.setBillCode(strValue.trim());
        }

        String pk_sys = rsTemp.getString("pk_sys");
        if (pk_sys != null) {
          voTemp.setSys(pk_sys.trim());
        }

        String pk_proc = rsTemp.getString("pk_proc");
        if (pk_proc != null) {
          voTemp.setProc(pk_proc.trim());
        }

        strValue = rsTemp.getString("pk_busitype");
        if (strValue != null) {
          voTemp.setBusiType(strValue.trim());
        }

        String businame = rsTemp.getString("businame");
        businame = businame == null ? null : Translator.bsTranslate("fidap", businame.trim());

        voTemp.setBusiName(businame);

        strValue = rsTemp.getString("busidate");
        if (strValue != null) {
          voTemp.setBusiDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("operator");
        if (strValue != null) {
          voTemp.setOperator(strValue.trim());
        }

        strValue = rsTemp.getString("checker");
        if (strValue != null) {
          voTemp.setChecker(strValue.trim());
        }

        strValue = rsTemp.getString("pk_corp");
        if (strValue != null) {
          voTemp.setCorp(strValue.trim());
        }

        strValue = Integer.toString(rsTemp.getInt("flag"));
        if (strValue != null) {
          voTemp.setFlag(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("currency");
        if (strValue != null) {
          voTemp.setCurrency(strValue.trim());
        }

        strValue = Double.toString(rsTemp.getDouble("money"));
        if (strValue != null) {
          voTemp.setMoney(new UFDouble(strValue.trim()));
        }

        strValue = rsTemp.getString("dapcomment");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setComment(strValue);

        strValue = rsTemp.getString("settlemode");
        if (strValue != null) {
          voTemp.setSettleMode(strValue.trim());
        }

        strValue = rsTemp.getString("docnum");
        if (strValue != null) {
          voTemp.setDocNum(strValue.trim());
        }

        strValue = rsTemp.getString("docdate");
        if (strValue != null) {
          voTemp.setDocDate(new UFDate(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_vouchentry");
        if (strValue != null) {
          voTemp.setVouchEntry(strValue.trim());
        }

        strValue = rsTemp.getString("deltag");
        if (strValue != null) {
          voTemp.setDeltag(new UFBoolean(strValue.trim()));
        }

        strValue = Integer.toString(rsTemp.getInt("sign"));
        if (strValue != null) {
          voTemp.setSign(new Integer(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_mergebatch");
        if (strValue != null) {
          voTemp.setMergeBatch(strValue.trim());
        }

        strValue = rsTemp.getString("mergebatchcode");
        if (strValue != null) {
          voTemp.setMergeBatchCode(strValue.trim());
        }

        strValue = rsTemp.getString("errmsg");
        strValue = strValue == null ? null : strValue.trim();
        strValue = Translator.bsTranslate("fidap", strValue);
        voTemp.setErrMsg(strValue);

        strValue = rsTemp.getString("checkstate");
        if (strValue != null) {
          voTemp.setCheckState(new UFBoolean(strValue.trim()));
        }

        strValue = rsTemp.getString("pk_rtvouch");
        if (strValue != null) {
          voTemp.setPkRtVouch(strValue.trim());
        }

        strValue = rsTemp.getString("procmsg");
        if (strValue != null) {
          voTemp.setProcMsg(strValue.trim());
        }

        voTemp.setDestSystem(rsTemp.getInt("destsystem"));
        strValue = rsTemp.getString("pk_glorg");
        if (strValue != null) {
          voTemp.setPkAccOrg(strValue.trim());
        }
        strValue = rsTemp.getString("pk_glbook");
        if (strValue != null) {
          voTemp.setPkAccount(strValue.trim());
        }
        vMsg.addElement(voTemp);
      }
    } catch (Exception e) {
      Logger.error(e);
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    if (vMsg.size() > 0) {
      voMsgArr = new DapFinMsgVO[vMsg.size()];
      vMsg.copyInto(voMsgArr);
    }
    return voMsgArr;
  }
}