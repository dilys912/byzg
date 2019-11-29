package nc.vo.uap.itfcheck;

public class XmlAggEntity{

	private XmlHEntity hvo;
	private XmlBEntity[] bvos;

	public XmlBEntity[] getChildrenVO() {
		return bvos;
	}

	public XmlHEntity getParentVO() {
		return hvo;
	}

	public void setChildrenVO(XmlBEntity[] bvos) {
		this.bvos = bvos;
	}

	public void setParentVO(XmlHEntity hvo) {
		this.hvo = hvo;
	}

}