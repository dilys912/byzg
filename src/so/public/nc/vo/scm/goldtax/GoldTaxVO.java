package nc.vo.scm.goldtax;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 金税VO
 * 
 * @author 蒲强华
 * @since 2008-8-27
 */
public class GoldTaxVO extends AggregatedValueObject {
	private static final long serialVersionUID = 4872292392712839246L;

	/** 表单标识 */
	private String billIdentifier;
	/** 表单名称 */
	private String billName;
	/** 销售公司名称 */
	private String sellCorpName;
	/** 销售公司Id */
	private String sellCorpId;
	/** 附注 */
	private String annotations;
	/** 来源模块 */
	private String sourceModule;

	/** 金税表头VO */
	private GoldTaxHeadVO headVO;
	/** 金税表体VO数组 */
	private GoldTaxBodyVO[] bodyVOs;

	@Override
	public GoldTaxBodyVO[] getChildrenVO() {
		return bodyVOs;
	}
	@Override
	public GoldTaxHeadVO getParentVO() {
		return headVO;
	}
	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		bodyVOs = (GoldTaxBodyVO[]) children;
	}
	@Override
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		headVO = (GoldTaxHeadVO) parent;
	}
	/**
	 * @return 表单标识，例:SJJK0101
	 */
	public String getBillIdentifier() {
		return billIdentifier;
	}
	/**
	 * @param billIdentifier 表单标识，例:SJJK0101
	 */
	public void setBillIdentifier(String billIdentifier) {
		this.billIdentifier = billIdentifier;
	}
	/**
	 * @return 表单名称，例:内部交易传入
	 */
	public String getBillName() {
		return billName;
	}
	/**
	 * @param billName 表单名称例:内部交易传入
	 */
	public void setBillName(String billName) {
		this.billName = billName;
	}
	/**
	 * @return 销售公司名称
	 */
	public String getSellCorpName() {
		return sellCorpName;
	}
	/**
	 * @param sellCorpName 销售公司名称
	 */
	public void setSellCorpName(String sellCorpName) {
		this.sellCorpName = sellCorpName;
	}
	/**
	 * @return 销售公司Id
	 */
	public String getSellCorpId() {
		return sellCorpId;
	}
	/**
	 * @param sellCorpId 销售公司Id
	 */
	public void setSellCorpId(String sellCorpId) {
		this.sellCorpId = sellCorpId;
	}
	/**
	 * @return 附注
	 */
	public String getAnnotations() {
		return annotations;
	}
	/**
	 * @param annotations 附注
	 */
	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}
	/**
	 * @return 来源模块，如 "so"、"to" 等
	 */
	public String getSourceModule() {
		return sourceModule;
	}
	/**
	 * @param sourceModule 来源模块，如 "so"、"to" 等
	 */
	public void setSourceModule(String sourceModule) {
		this.sourceModule = sourceModule;
	}
}
