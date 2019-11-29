package nc.vo.ic.ic641;

import java.util.HashMap;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

public class QueryCondCtl extends ValueObject
{
  public static boolean DEBUG = false;
  public static final String CorpCond = "公司条件";
  public static final String DateChoose = "日期选择";
  public static final String Sign_InClude = "是否包含签字";
  public static final String Provider_Group = "是否汇总供应商";
  public static final String Group_M = "汇总库存范围";
  public static final String Group_M_ = "库存汇总选择下级";
  public static final String Group_INVCL = "汇总存货分类";
  public static final String Group_INV_ = "汇总存货";
  private boolean m_bBalMnyShow = true;

  private boolean m_bIsAstNumShow = false;

  private boolean m_bIsMZShow = true;

  private boolean m_bIsJHJEShow = true;

  private boolean m_bIsCanKaoMnyShow = true;

  private boolean m_bIsSumInOutPlanMny = true;

  private boolean m_bRdType = false;

  private boolean m_bIsProvider = false;

  private boolean m_bHasRDValue = false;

  private boolean m_bSignBill = false;

  private boolean m_bIsHidWarehouseTransfer = false;
  public static final String IN_SUM_PLANMNY = "ninsumplanmnyx";
  public static final String OUT_SUM_PLANMNY = "noutsumplanmnyx";
  private HashMap m_param = new HashMap();

  public static void main(String[] args)
  {
  }

  public void setIsMnyShow(boolean bBalanceMnyShow)
  {
    this.m_bBalMnyShow = bBalanceMnyShow;
  }

  public boolean isMnyShow() {
    return this.m_bBalMnyShow;
  }

  public void setIsAstNumShow(boolean bAstNumShow) {
    this.m_bIsAstNumShow = bAstNumShow;
  }

  public boolean isAstNumShow() {
    return this.m_bIsAstNumShow;
  }

  public Object getParam(String key)
  {
    return this.m_param.get(key);
  }

  public void setParam(String key, Object value) {
    this.m_param.put(key, value);
  }

  public void setIsMZShow(boolean m_bIsMZShow) {
    this.m_bIsMZShow = m_bIsMZShow;
  }

  public boolean isMZShow() {
    return this.m_bIsMZShow;
  }

  public void setIsJHJEShow(boolean m_bIsJHJEShow) {
    this.m_bIsJHJEShow = m_bIsJHJEShow;
  }

  public boolean isJHJEShow() {
    return this.m_bIsJHJEShow;
  }

  public void setIsCanKaoMnyShow(boolean m_bIsCanKaoMnyShow) {
    this.m_bIsCanKaoMnyShow = m_bIsCanKaoMnyShow;
  }

  public boolean isCanKaoMnyShow() {
    return this.m_bIsCanKaoMnyShow;
  }

  public void setIsSumInOutPlanMny(boolean m_bIsSumInOutPlanMny) {
    this.m_bIsSumInOutPlanMny = m_bIsSumInOutPlanMny;
  }

  public boolean isSumInOutPlanMny() {
    return this.m_bIsSumInOutPlanMny;
  }

  public void setIsSplitRd(boolean m_bRdType) {
    this.m_bRdType = m_bRdType;
  }

  public boolean isSplitRd() {
    return this.m_bRdType;
  }

  public void setIsProvider(boolean m_bIsProvider) {
    this.m_bIsProvider = m_bIsProvider;
  }

  public boolean isProvider() {
    return this.m_bIsProvider;
  }

  public void setHasRDValue(boolean m_bHasRDValue) {
    this.m_bHasRDValue = m_bHasRDValue;
  }

  public boolean hasRDValue() {
    return this.m_bHasRDValue;
  }

  public void setSignBillOnly(boolean m_bSignBill) {
    this.m_bSignBill = m_bSignBill;
  }

  public boolean isSignBillOnly() {
    return this.m_bSignBill;
  }

  public void setIsHidWarehouseTransfer(boolean b) {
    this.m_bIsHidWarehouseTransfer = b;
  }
  public boolean isHidWarehouseTransfer() {
    return this.m_bIsHidWarehouseTransfer;
  }

  public String getSignBillOnlyWhere(String tableAlias)
  {
    String sBillSignWhereStr = "";
    if (isSignBillOnly()) {
      sBillSignWhereStr = " AND (" + tableAlias + ".fbillflag in (" + "3" + "," + "4" + "))";
    }

    return sBillSignWhereStr;
  }

  public String getCorpWhere(String tableAlias)
  {
    if (getParam("公司条件") == null) return null;
    String[] saCorp = (String[])(String[])getParam("公司条件");
    if ((saCorp == null) || (saCorp.length == 0)) return null;
    return SQLUtil.formInSQL(tableAlias + ".pk_corp", saCorp);
  }

  public String getEntityName()
  {
    return null;
  }

  public void validate()
    throws ValidationException
  {
  }
}