package nc.vo.scm.goldtax;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

/**
 * 金税表体VO
 * 
 * @author 蒲强华
 * @since 2008-8-27
 */
public class GoldTaxBodyVO extends CircularlyAccessibleValueObject {
	private static final long serialVersionUID = -2951916926901187913L;

	/** 存货id */
	private String invBaseId;
	/** 存货名称 */
	private String invName;
	/** 计量单位(统一为报价计量单位) */
	private String quoteUnitId;
	/** 计量单位名称 */
	private String quoteUnitName;
	/** 规格 */
	private String invSpec;
	/** 数量 */
	private UFDouble number;
	/** 含税金额 */
	private UFDouble money;
	/** 税率 */
	private UFDouble taxRate;
	/** 商品税目 */
	private String taxItems;
	/** 折扣金额 */
	private UFDouble discountMny;
	/** 税额 */
	private UFDouble taxMny;
	/** 折扣税额 */
	private UFDouble discountTaxMny;
	/** 小数形式表示的折扣率 */
	private UFDouble discountRate;
	/** 含税单价 */
	private UFDouble price;
	/** 价格方式 */
	private UFDouble priceMode;
	/** 存货类Id */
	private String invClassId;
	/** 存货类名称 */
	private String invClassName;
	/** 无税金额 */
	private UFDouble noTaxMny;
	/** 无税单价 */
	private UFDouble noTaxPrice;

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

	/**
	 * 存货基本档案id
	 * 
	 * @return 存货id
	 */
	public String getInvBaseId() {
		return invBaseId;
	}

	/**
	 * 存货基本档案id
	 * 
	 * @param invBaseId 存货id
	 */
	public void setInvBaseId(String invBaseId) {
		this.invBaseId = invBaseId;
	}

	/**
	 * 该行对应的存货基本档案中存货名称或者对应存货分类名称（按照存货分类合并时）
	 * 
	 * @return 存货名称
	 */
	public String getInvName() {
		return invName;
	}

	/**
	 * 该行对应的存货基本档案中存货名称或者对应存货分类名称（按照存货分类合并时）
	 * 
	 * @param invName 存货名称
	 */
	public void setInvName(String invName) {
		this.invName = invName;
	}

	/**
	 * 计量单位(统一为报价计量单位)
	 * 
	 * @return 计量单位(统一为报价计量单位)
	 */
	public String getQuoteUnitId() {
		return quoteUnitId;
	}

	/**
	 * 计量单位(统一为报价计量单位)
	 * 
	 * @param quoteUnitId 计量单位(统一为报价计量单位)
	 */
	public void setQuoteUnitId(String quoteUnitId) {
		this.quoteUnitId = quoteUnitId;
	}

	/**
	 * 存货基本档案中规格字段值
	 * 
	 * @return 规格
	 */
	public String getInvSpec() {
		return invSpec;
	}

	/**
	 * 存货基本档案中规格字段值
	 * 
	 * @param invSpec 规格
	 */
	public void setInvSpec(String invSpec) {
		this.invSpec = invSpec;
	}

	/**
	 * 按参数SCM04规则合并明细行后导出当前税票行的数量，如果拆行处理，则为拆分到当前税票中的数量
	 * 
	 * @return 数量
	 */
	public UFDouble getNumber() {
		return number;
	}

	/**
	 * 按参数SCM04规则合并明细行后导出当前税票行的数量，如果拆行处理，则为拆分到当前税票中的数量
	 * 
	 * @param number 数量
	 */
	public void setNumber(UFDouble number) {
		this.number = number;
	}

	/**
	 * 为数量乘以单价之积
	 * 
	 * @return 含税金额
	 */
	public UFDouble getMoney() {
		return money;
	}

	/**
	 * 为数量乘以单价之积
	 * 
	 * @param money 含税金额
	 */
	public void setMoney(UFDouble money) {
		this.money = money;
	}

	/**
	 * 税率字段值,如:0.17
	 * 
	 * @return 税率
	 */
	public UFDouble getTaxRate() {
		return taxRate;
	}

	/**
	 * 税率字段值,如:0.17
	 * 
	 * @param taxRate 税率
	 */
	public void setTaxRate(UFDouble taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * 商品税目，固定为4001
	 * 
	 * @return 商品税目
	 */
	public String getTaxItems() {
		return taxItems;
	}

	/**
	 * 商品税目，固定为4001
	 * 
	 * @param taxItems 商品税目
	 */
	public void setTaxItems(String taxItems) {
		this.taxItems = taxItems;
	}

	/**
	 * 折扣金额为0（内部交易单据上目前没有折扣的字段）
	 * 
	 * @return 折扣金额
	 */
	public UFDouble getDiscountMny() {
		return discountMny;
	}

	/**
	 * 折扣金额为0（内部交易单据上目前没有折扣的字段）
	 * 
	 * @param discountMny 折扣金额
	 */
	public void setDiscountMny(UFDouble discountMny) {
		this.discountMny = discountMny;
	}

	/**
	 * @return 税额
	 */
	public UFDouble getTaxMny() {
		return taxMny;
	}

	/**
	 * @param taxMny 税额
	 */
	public void setTaxMny(UFDouble taxMny) {
		this.taxMny = taxMny;
	}

	/**
	 * @return 折扣税额
	 */
	public UFDouble getDiscountTaxMny() {
		return discountTaxMny;
	}

	/**
	 * @param discountTaxMny 折扣税额
	 */
	public void setDiscountTaxMny(UFDouble discountTaxMny) {
		this.discountTaxMny = discountTaxMny;
	}

	/**
	 * @return 小数形式表示的折扣率
	 */
	public UFDouble getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate 小数形式表示的折扣率
	 */
	public void setDiscountRate(UFDouble discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * 按参数SCM04规则合并明细行后的合计含税金额/合计数量
	 * 
	 * @return 含税单价
	 */
	public UFDouble getPrice() {
		return price;
	}

	/**
	 * 按参数SCM04规则合并明细行后的合计含税金额/合计数量
	 * 
	 * @param price 含税单价
	 */
	public void setPrice(UFDouble price) {
		this.price = price;
	}

	/**
	 * 价格方式，固定为1
	 * 
	 * @return 价格方式
	 */
	public UFDouble getPriceMode() {
		return priceMode;
	}

	/**
	 * 价格方式，固定为1
	 * 
	 * @param priceMode 价格方式
	 */
	public void setPriceMode(UFDouble priceMode) {
		this.priceMode = priceMode;
	}

	/**
	 * 按存货类合并VO时用
	 * 
	 * @return 存货类
	 */
	public String getInvClassId() {
		return invClassId;
	}

	/**
	 * 按存货类合并VO时用
	 * 
	 * @param invClassId 存货类
	 */
	public void setInvClassId(String invClassId) {
		this.invClassId = invClassId;
	}

	/**
	 * @return 存货类名称
	 */
	public String getInvClassName() {
		return invClassName;
	}

	/**
	 * @param invClassName 存货类名称
	 */
	public void setInvClassName(String invClassName) {
		this.invClassName = invClassName;
	}

	/**
	 * 无税金额，限额和拆分行用
	 * 
	 * @return 无税金额
	 */
	public UFDouble getNoTaxMny() {
		return noTaxMny;
	}

	/**
	 * 无税金额，限额和拆分行用
	 * 
	 * @param noTaxMny 无税金额
	 */
	public void setNoTaxMny(UFDouble noTaxMny) {
		this.noTaxMny = noTaxMny;
	}

	/**
	 * 无税单价，限额和拆分行用
	 * 
	 * @return 无税单价
	 */
	public UFDouble getNoTaxPrice() {
		return noTaxPrice;
	}

	/**
	 * 无税单价，限额和拆分行用
	 * 
	 * @param noTaxPrice 无税单价
	 */
	public void setNoTaxPrice(UFDouble noTaxPrice) {
		this.noTaxPrice = noTaxPrice;
	}

	/**
	 * @return 计量单位名称
	 */
	public String getQuoteUnitName() {
		return quoteUnitName;
	}

	/**
	 * @param quoteUnitName 计量单位名称
	 */
	public void setQuoteUnitName(String quoteUnitName) {
		this.quoteUnitName = quoteUnitName;
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
