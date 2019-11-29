package nc.vo.scm.goldtax;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;

/**
 * 金税表头VO
 * 
 * @author 蒲强华
 * @since 2008-8-27
 */
public class GoldTaxHeadVO extends CircularlyAccessibleValueObject {
	private static final long serialVersionUID = -4409977219060880292L;

	/** 单据号 */
	private String code;
	/** 商品行数 */
	private int rowNum;
	/** 购方名称 */
	private String customerName;
	/** 购方id */
	private String customerId;
	/** 购方税号 */
	private String taxPayerId;
	/** 购方地址电话 */
	private String saleAddrPhone;
	/** 购方银行ID */
	private String accountId;
	/** 购方银行帐号 */
	private String account;
	/** 购方银行名称 */
	private String accName;
	/** 备注 */
	private String memo;
	/** 复核人 */
	private String checker;
	/** 收款人 */
	private String payee;
	/** 清单行商品名称 */
	private String rowInvName;
	/** 单据日期 */
	private UFDate billDate;
	/** 销方银行帐号 */
	private String sellAccount;
	/** 购方公司 */
	private String purchaserCorpId;
	/** 销售公司 */
	private String sellCorpId;
	/** 金税票号 */
	private String taxBillNo;

	/**
	 * 没有主键，返回null
	 * 
	 * @return null
	 */
	@Override
	public String getPrimaryKey() {
		return null;
	}

	/**
	 * 没有主键，空实现
	 * 
	 * @param key 忽略
	 */
	@Override
	public void setPrimaryKey(String key) {
	}

//	@Override
//	public String[] getAttributeNames() {
//		return AttributeUtils.getAttributeNames(getClass());
//	}
//
//	@Override
//	public Object getAttributeValue(String attributeName) {
//		return AttributeUtils.getAttributeValue(this, attributeName);
//	}
//
//	@Override
//	public void setAttributeValue(String name, Object value) {
//		AttributeUtils.setAttributeValue(this, name, value);
//	}

	@Override
	public String getEntityName() {
		return null;
	}

	@Override
	public void validate() throws ValidationException {
	}

	public static void main(String[] args) {
		for (String name : new GoldTaxHeadVO().getAttributeNames()) {
			SCMEnv.out(name);
		}
	}

	/**
	 * 单据号（内部交易启用结算路径时，为虚拟结算清单编号）
	 * 
	 * @return 单据号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 单据号（内部交易启用结算路径时，为虚拟结算清单编号）
	 * 
	 * @param code 单据号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 指该张税票的明细行数（VO拆分合并后填写）
	 * 
	 * @return 商品行数
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * 指该张税票的明细行数（VO拆分合并后填写）
	 * 
	 * @param rowNum 商品行数
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	/**
	 * 调入公司对应的客户基本档案客户名称
	 * 
	 * @return 购方名称
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * 调入公司对应的客户基本档案客户名称
	 * 
	 * @param customerName 购方名称
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * 调入公司对应的客户基本档案主键
	 * 
	 * @return 购方id
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * 调入公司对应的客户基本档案主键
	 * 
	 * @param customerId 购方id
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * 客户基本档案中客户税号taxpayerid字段值（根据客户查询得到）
	 * 
	 * @return 购方税号
	 */
	public String getTaxPayerId() {
		return taxPayerId;
	}

	/**
	 * 客户基本档案中客户税号taxpayerid字段值（根据客户查询得到）
	 * 
	 * @param taxPayerId 购方税号
	 */
	public void setTaxPayerId(String taxPayerId) {
		this.taxPayerId = taxPayerId;
	}

	/**
	 * 客户基本档案中saleaddr字段值, 客户基本档案中phone3字段值,中间以一个空格分开（根据客户查询得到）
	 * 
	 * @return 购方地址电话 
	 */
	public String getSaleAddrPhone() {
		return saleAddrPhone;
	}

	/**
	 * 客户基本档案中saleaddr字段值, 客户基本档案中phone3字段值,中间以一个空格分开（根据客户查询得到）
	 * 
	 * @param saleAddrPhone 购方地址电话 
	 */
	public void setSaleAddrPhone(String saleAddrPhone) {
		this.saleAddrPhone = saleAddrPhone;
	}

	/**
	 * 客户基本档案中的银行账号（根据客户查询bd_custbank得到）
	 * 
	 * @return 购方银行帐号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 客户基本档案中的银行账号（根据客户查询bd_custbank得到）
	 * 
	 * @param account 购方银行帐号
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 客户基本档案中的银行（根据客户查询bd_custbank得到）
	 * 
	 * @return 购方银行名称
	 */
	public String getAccName() {
		return accName;
	}

	/**
	 * 客户基本档案中的银行（根据客户查询bd_custbank得到）
	 * 
	 * @param accName 购方银行名称
	 */
	public void setAccName(String accName) {
		this.accName = accName;
	}

	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return 复核人
	 */
	public String getChecker() {
		return checker;
	}

	/**
	 * @param checker 复核人
	 */
	public void setChecker(String checker) {
		this.checker = checker;
	}

	/**
	 * @return 收款人
	 */
	public String getPayee() {
		return payee;
	}

	/**
	 * @param payee 收款人
	 */
	public void setPayee(String payee) {
		this.payee = payee;
	}

	/**
	 * @return 清单行商品名称
	 */
	public String getRowInvName() {
		return rowInvName;
	}

	/**
	 * @param rowInvName 清单行商品名称
	 */
	public void setRowInvName(String rowInvName) {
		this.rowInvName = rowInvName;
	}

	/**
	 * 结算清单单据日期字段dbilldate的值
	 * 
	 * @return 单据日期
	 */
	public UFDate getBillDate() {
		return billDate;
	}

	/**
	 * 结算清单单据日期字段dbilldate的值
	 * 
	 * @param billDate 单据日期
	 */
	public void setBillDate(UFDate billDate) {
		this.billDate = billDate;
	}

	/**
	 * @return 销方银行帐号
	 */
	public String getSellAccount() {
		return sellAccount;
	}

	/**
	 * @param sellAccount 销方银行帐号
	 */
	public void setSellAccount(String sellAccount) {
		this.sellAccount = sellAccount;
	}

	/**
	 * @return 购方公司
	 */
	public String getPurchaserCorpId() {
		return purchaserCorpId;
	}

	/**
	 * @param purchaserCorpId 购方公司
	 */
	public void setPurchaserCorpId(String purchaserCorpId) {
		this.purchaserCorpId = purchaserCorpId;
	}

	/**
	 * @return 销售公司
	 */
	public String getSellCorpId() {
		return sellCorpId;
	}

	/**
	 * @param sellCorpId 销售公司
	 */
	public void setSellCorpId(String sellCorpId) {
		this.sellCorpId = sellCorpId;
	}

	/**
	 * 导入的金税票号放到此字段
	 * 
	 * @return 金税票号
	 */
	public String getTaxBillNo() {
		return taxBillNo;
	}

	/**
	 * 导入的金税票号放到此字段
	 * 
	 * @param taxBillNo 金税票号
	 */
	public void setTaxBillNo(String taxBillNo) {
		this.taxBillNo = taxBillNo;
	}

	/**
	 * @return 购方银行ID
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId 购方银行ID
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String[] getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributeValue(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
}
