package nc.ui.po.oper;

import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IRefConst;

public class GongyingshangQueryModel extends AbstractRefModel{
	public String cbaseid=null;
	public GongyingshangQueryModel(String cbaseid) {
		// TODO Auto-generated constructor stub
		super();
		this.cbaseid=cbaseid;
		setRefNodeName("供应商查询");
	}
	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
	
			setFieldCode(new String[] { "po_invoice.dinvoicedate",
					"bd_cubasdoc.custshortname", "bd_invbasdoc.invname","po_invoice_b.norgnettaxprice" });
			setHiddenFieldCode(new String[] { "po_invoice_b.cinvoiceid","po_invoice.cvendorbaseid"});
			setPkFieldCode("po_invoice.cvendorbaseid");
			setTableName("po_invoice left outer join po_invoice_b on po_invoice.cinvoiceid = po_invoice_b.cinvoiceid "+
   " left outer join  bd_cubasdoc on po_invoice.cvendorbaseid=bd_cubasdoc.pk_cubasdoc"+
  " left outer join   bd_invbasdoc on po_invoice_b.cbaseid=bd_invbasdoc.pk_invbasdoc");
			setWherePart("po_invoice_b.cbaseid='"+cbaseid+"'");
			setRefTitle(Transformations.getLstrFromMuiStr("供应商查询","Suppliers Query"));
//			String fomula = "name->getColValue(bd_corp, unitname, pk_corp ,sm_user.pk_corp);iif(name==\"\"||name==null,getLangRes(\"ref\",\"UPPref-000001\"),name)";
//			setFormulas(new String[][]{{"sm_user.pk_corp",fomula}});
			setOrderPart(" po_invoice.dinvoicedate desc ");
			setStrPatch(IRefConst.DISTINCT);
			setDefaultFieldCount(4);

	//	resetFieldName();
			setFieldName(new String[]{Transformations.getLstrFromMuiStr("发票日期","Invoice date"),Transformations.getLstrFromMuiStr("供应商","Suppliers"), Transformations.getLstrFromMuiStr("存货名称","Inventories name"), Transformations.getLstrFromMuiStr("含税单价","Tax price"), Transformations.getLstrFromMuiStr("发票id","Invoice id")});
	}
	
}
