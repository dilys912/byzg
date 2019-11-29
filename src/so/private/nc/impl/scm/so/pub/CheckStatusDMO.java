package nc.impl.scm.so.pub;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so006.SalequotationVO;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareVO;
import nc.vo.so.so013.FeeVO;
import nc.vo.so.so014.SoOutVO;

public class CheckStatusDMO extends DataManageObject
{
  public CheckStatusDMO()
    throws NamingException, SystemException
  {
  }

  public CheckStatusDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void isAdjustApproveStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售调价单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus("36", billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if (oldReceiptStatus == 1) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000045", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isAdjustBlankOutStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售调价单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus("36", billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if (oldReceiptStatus == 1) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000047", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isAdjustEditStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售调价单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus("36", billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if (oldReceiptStatus == 1) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000048", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isAdjustUnApproveStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售调价单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus("36", billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 2) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000049", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isAlterStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 2) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000050", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isApproveStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if ((oldReceiptStatus == 1) || (oldReceiptStatus == 7))
    {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000045", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isBanalceStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行结算状态检测");

    String strMsg = null;

    String[] arraySaleStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    String[] arrayStockStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000051"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000052"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000052"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040") };

    String strType = rtnBillType(billVO);

    int oldReceiptStatus = nReceiptStatus(strType, billVO.getParentVO().getPrimaryKey());

    if (strType.equals("4C")) {
      if ((oldReceiptStatus == 3) || (oldReceiptStatus == 4))
        strMsg = null;
      else {
        strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000053", null, new String[] { arrayStockStatus[(oldReceiptStatus - 1)] });
      }

    }
    else
    {
      if (oldReceiptStatus == 0)
        oldReceiptStatus = 1;
      if (oldReceiptStatus == 2)
        strMsg = null;
      else {
        strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000053", null, new String[] { arraySaleStatus[(oldReceiptStatus - 1)] });
      }

    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isBlankOutStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if (oldReceiptStatus == 1) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000047", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isEditStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 0)
      oldReceiptStatus = 1;
    if ((oldReceiptStatus == 1) || (oldReceiptStatus == 8)) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000048", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isFinishStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 2) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000054", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isFreezeStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 2) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000055", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isInitOutApproveStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行期初销售出库单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus > 0) {
      if (oldReceiptStatus == Integer.valueOf("2").intValue())
      {
        strMsg = null;
      }
      else {
        strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000045", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
      }

    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isInitOutBlankOutStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行期初销售出库单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == Integer.valueOf("2").intValue())
    {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000047", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isInitOutEditStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行期初销售出库单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == Integer.valueOf("2").intValue())
    {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000048", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isUnApproveStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if ((oldReceiptStatus == 2) || (oldReceiptStatus == 7)) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000049", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void isUnFreezeStatus(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("执行销售定单状态检测");

    String strMsg = null;

    String[] arrayStatus = { NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000039"), NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001558"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000040"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000041"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000042"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000043"), NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000044"), NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000242") };

    int oldReceiptStatus = nReceiptStatus(rtnBillType(billVO), billVO.getParentVO().getPrimaryKey());

    if (oldReceiptStatus == 3) {
      strMsg = null;
    }
    else {
      strMsg = NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000056", null, new String[] { arrayStatus[(oldReceiptStatus - 1)] });
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public int nReceiptRowStatus(String ReceiptTypeID, String ReceiptID, String ReceiptDetailID)
    throws SQLException, BusinessException
  {
    String strTempTableName = null;

    String strPrimKeyName = null;

    if ((ReceiptTypeID.equals("30")) || (ReceiptTypeID.equals("3A")))
    {
      strTempTableName = "so_saleorder_b";
      strPrimKeyName = "corder_bid";
    }
    else if (ReceiptTypeID.equals("31")) {
      strTempTableName = "so_salereceipt_b";
      strPrimKeyName = "creceipt_bid";
    }
    else if ((ReceiptTypeID.equals("32")) || (ReceiptTypeID.equals("3C")))
    {
      strTempTableName = "so_saleinvoice_b";
      strPrimKeyName = "cinvoice_bid";
    }

    String SQLRowStatus = "SELECT frowstatus, FROM " + strTempTableName + " WHERE  ";
    SQLRowStatus = SQLRowStatus + " csaleid = '" + ReceiptID + "'";
    SQLRowStatus = SQLRowStatus + "  and " + strPrimKeyName + "  = '" + ReceiptDetailID + "'";

    Connection con = null;
    PreparedStatement stmt = null;

    int bResult = 0;

    BigDecimal dblNumber = new BigDecimal(0);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRowStatus);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        Object o = rstNumber.getObject("frowstatus");
        if (o != null) {
          dblNumber = new BigDecimal(o.toString());
        }
        if (dblNumber != null)
        {
          bResult = dblNumber.intValue();
        }
      }
    }
    finally
    {
      try
      {
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
    return bResult;
  }

  public int nReceiptStatus(String billTypeID, String ReceiptID)
    throws SQLException, BusinessException
  {
    String SQLStatus = null;

    boolean blnCondition = (billTypeID.equals("30")) || (billTypeID.equals("31")) || (billTypeID.equals("32"));
    blnCondition = (blnCondition) || (billTypeID.equals("3A")) || (billTypeID.equals("3C"));

    if (blnCondition) {
      String tablename = null;
      if ((billTypeID.equals("30")) || (billTypeID.equals("3A")))
        tablename = "so_sale";
      if ((billTypeID.equals("32")) || (billTypeID.equals("3C")))
        tablename = "so_saleinvoice";
      if (billTypeID.equals("31"))
        tablename = "so_salereceipt";
      SQLStatus = "SELECT fstatus FROM " + tablename + " WHERE ";
      SQLStatus = SQLStatus + " csaleid = '" + ReceiptID + "'";
    }
    else if (billTypeID == "37")
    {
      SQLStatus = "SELECT cstatus FROM prm_salequotation WHERE ";
      SQLStatus = SQLStatus + " csalequotationid = '" + ReceiptID + "'";
    }
    else if (billTypeID == "37") {
      SQLStatus = "SELECT fstatus FROM prm_adjustprice WHERE ";
      SQLStatus = SQLStatus + " cadjpriceid = '" + ReceiptID + "'";
    }
    else if (billTypeID == "34") {
      SQLStatus = "SELECT fstatus FROM so_fee WHERE ";
      SQLStatus = SQLStatus + " vFeeID = '" + ReceiptID + "'";
    }
    else if ((billTypeID == "4S") || (billTypeID.equals("4C"))) {
      SCMEnv.out("期初销售出库单暂未完成");
      SQLStatus = "SELECT fbillflag FROM ic_general_h WHERE ";
      SQLStatus = SQLStatus + " cgeneralhid = '" + ReceiptID + "'";
    }
    else {
      SQLStatus = "SELECT fstatus FROM so_bomorder WHERE ";
      SQLStatus = SQLStatus + " cbomorderid = '" + ReceiptID + "'";
    }

    Connection con = null;
    PreparedStatement stmt = null;

    int bResult = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLStatus);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        bResult = rstNumber.getInt(1);
      }
    }
    finally
    {
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
    return bResult;
  }

  private String rtnBillType(AggregatedValueObject billVO)
    throws SQLException, BusinessException
  {
    SCMEnv.out("根据VO返回单据类型");

    String strMsg = null;

    String strBillType = null;

    if ((billVO instanceof SalequotationVO))
      strBillType = "37";
    else if ((billVO instanceof BomorderVO))
      strBillType = "3M";
    else if ((billVO instanceof FeeVO))
      strBillType = "34";
    else if ((billVO instanceof SoOutVO))
      strBillType = "3Q";
    else if ((billVO instanceof SquareVO))
    {
      strBillType = ((SquareHeaderVO)billVO.getParentVO()).getCreceipttype();
    }
    else {
      strBillType = billVO.getParentVO().getAttributeValue("creceipttype").toString();
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }

    return strBillType;
  }
}