package nc.ui.pi.invoice;

import java.awt.Component;
import java.util.ArrayList;


import nc.ui.bd.b45.CalbodyBO_Client;
import nc.ui.common.FormularExecutor;
import nc.ui.common.PublicUtil;
import nc.ui.common.util.Util;
import nc.ui.ia.bill.BillBO_Client;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ic.ic212.TestConn;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.b45.CalbodyVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;

public class BaoyinInvoiceUI extends InvoiceUI {

	public void onButtonClicked(ButtonObject btn)
	{
		super.onButtonClicked(btn);
		int count = getComponentCount();
		if("批改".equals(btn.getName()) || "传存货".equals(btn.getName()) || "取消传存货".equals(btn.getName()) && count>0)
		{
			Component comp = getComponent(0);//只有一个Component,卡片或者列表
			if(comp instanceof InvBillPanel)//卡片下
			{
//				btn.setDisplayHotkey("CTRL+G");
				InvBillPanel cardPanel = (InvBillPanel)comp;
				boolean enable = cardPanel.getHeadItem("vinvoicecode").isEnabled();
				if(enable)
				{
					if("批改".equals(btn.getName()))
					try {
//						btn.setDisplayHotkey("CTRL+G");
						InvoiceItemVO selVo = null;
						int selRow = cardPanel.getBillTable().getSelectedRow();
						if(selRow<0)
						{
							MessageDialog.showHintDlg(null, "请选择行", "请选择行");
							return;
						}
						InvoiceItemVO selVos[] = (InvoiceItemVO[])cardPanel.getBillModel().getBodyValueVOs(InvoiceItemVO.class.getName());
						if(selVos == null || selVos.length < 2)
						{
							MessageDialog.showHintDlg(null, "提示", "至少2行记录以上才能操作");
							return;
						}
						selVo = selVos[selRow];
						String resNo = selVo.getVdef1();//资源号
						if(resNo == null || resNo.equals("")) 
						{
							MessageDialog.showHintDlg(null, "提示", "所选择行资源号为空");
							return;
						}
						showHintMessage("");
				    	BillCardPanel panel = (BillCardPanel)getComponent(0);
				    	BillTable table = (BillTable)panel.getBillTable();
						UFDouble noriginalcurprice = selVo.getNoriginalcurprice();
						int j = 0;
						for (int i = 0; i < selVos.length; i++) {
							if(resNo.equals(selVos[i].getVdef1()))
							{
								if(j == 0) j++;
								table.setRowSelectionInterval(i, i);
				    			panel.getBillTable().scrollRectToVisible(table.getCellRect(i, 11, true));
								cardPanel.getBillModel().setValueAt(noriginalcurprice, i, "noriginalcurprice");
								BillItem bi = cardPanel.getBillModel().getItemByKey("noriginalcurprice");
								afterEdit(new BillEditEvent(bi,noriginalcurprice,"noriginalcurprice",i,1));
							}
						}
						if(j != 0)
					    	showHintMessage("批改完成");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else 
				{
					InvoiceVO invoiceVo = (InvoiceVO)cardPanel.getBillValueVO(InvoiceVO.class.getName(),InvoiceHeaderVO.class.getName(),InvoiceItemVO.class.getName());
					String pkVo = invoiceVo.getHeadVO().getPrimaryKey();
					if(pkVo == null || pkVo.equals("")) return;
					transInv(invoiceVo,btn.getName());
				}
			}else//列表下
			{
				InvListPanel listPanel = (InvListPanel)comp;
				int selRows[] = listPanel.getHeadTable().getSelectedRows();
				InvoiceHeaderVO[] headVos = (InvoiceHeaderVO[])listPanel.getHeadBillModel().getBodyValueVOs(InvoiceHeaderVO.class.getName());
				InvoiceItemVO[] itemVos = (InvoiceItemVO[])listPanel.getBodyBillModel().getBodyValueVOs(InvoiceItemVO.class.getName());
				if(selRows == null || selRows.length == 0 || headVos == null || headVos.length == 0 || itemVos == null || itemVos.length == 0 ) return;
				InvoiceVO invoiceVo = new InvoiceVO();
				invoiceVo.setParentVO(headVos[selRows[0]]);
				invoiceVo.setChildrenVO(itemVos);
				transInv(invoiceVo,btn.getName());
			}
		}
	}

	private void transInv(InvoiceVO invoiceVo, String name) {
		
		try {
			String invoicePK = invoiceVo.getPrimaryKey();
			if(invoicePK == null)
				return;
			BillVO billVo = getDestVO(invoicePK);
			if("传存货".equals(name))
			{
				if(billVo != null)
				{
					MessageDialog.showWarningDlg(null, null, "已存在如下目的单据"+(String)billVo.getParentVO().getAttributeValue("vbillcode"));					
					return;
				}
				IAEnvironment iaenv = new IAEnvironment();
				BillVO i9vo = new BillVO();
				BillHeaderVO headVo = new BillHeaderVO();
				InvoiceItemVO[] invoiceBVos = invoiceVo.getBodyVO();
				BillItemVO itemVos [] = new BillItemVO[invoiceBVos.length];
				i9vo.setParentVO(headVo);
				i9vo.setChildrenVO(itemVos);
				headVo.setCbilltypecode("I9");
				headVo.setPk_corp(Util.getCorp());
				headVo.setFdispatchflag(0);//收发标志
				headVo.setDbilldate(new UFDate(Util.getLoginDate()));
				headVo.setCsourcemodulename(ConstVO.m_sModulePO);
				CalbodyVO[] calbodyVOs = CalbodyBO_Client.queryByCondition(Util.getCorp(), null);
				headVo.setCrdcenterid(calbodyVOs!=null && calbodyVOs.length>0 ? calbodyVOs[0].getPk_calbody() : null);//成本库存组织
				ArrayList voList = new ArrayList();
				for (int i = 0; i < invoiceBVos.length; i++) {
					itemVos[i] = new BillItemVO();
					itemVos[i].setCbilltypecode("I9");
					itemVos[i].setFpricemodeflag(0);//计价方式编码
					itemVos[i].setCsourcebilltypecode("25");
					itemVos[i].setCsourcebillid(invoicePK);
					itemVos[i].setCsourcebillitemid(invoiceBVos[i].getPrimaryKey());
					if(invoiceBVos[i].getPKDefDoc2() == null || invoiceBVos[i].getPKDefDoc2().length() == 0)
						continue;
					if(!invInMaintainList(invoiceBVos[i].getPKDefDoc2()))
						continue;
					itemVos[i].setCinventoryid(invoiceBVos[i].getPKDefDoc2());
					itemVos[i].setNmoney(invoiceBVos[i].getNoriginalsummny());
					itemVos[i].setVsourcebillcode(invoiceVo.getHeadVO().getVinvoicecode());
					voList.add(itemVos[i]);
				}
				BillItemVO[] destVos = (BillItemVO[])voList.toArray(new BillItemVO[voList.size()]);
				if(destVos == null || destVos.length == 0)
					throw new Exception("无物料信息或物料未定义存货分类,无法生成调整单");
				i9vo.setChildrenVO(destVos);
				i9vo = BillBO_Client.insert(iaenv.getClientLink(), i9vo);
				MessageDialog.showHintDlg(null, null, "生成目的单据"+(String)i9vo.getParentVO().getAttributeValue("vbillcode"));
			}
			else if("取消传存货".equals(name))
			{
				if(billVo != null)
				{
					IAEnvironment ia = new IAEnvironment();
					nc.vo.scm.pub.session.ClientLink cl = ia.getClientLink();
					BillVO[] vos = BillBO_Client.querybillWithOtherTable(new String[]{"ia_bill","ia_bill_b"}, new String[]{"ia_bill.cbillid=ia_bill_b.cbillid"}, new String[]{""}, null, null, null, new Boolean(false), cl);
//					BillVO[] vos = (BillVO[])HYPubBO_Client.queryBillVOByCondition(new String[]{BillVO.class.getName(),BillHeaderVO.class.getName(),BillItemVO.class.getName()}, 
//							" cbillid in (select cbillid from ia_bill_b where dr= 0 and csourcebillid ='"+invoicePK+"')");
					BillBO_Client.delete(cl, vos[0], ia.getUser().getPrimaryKey(), "", "");
				}else
				{
					throw new Exception("无下游单据");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showWarningDlg(null, null, e.getMessage());
		}
	}

	private boolean invInMaintainList(String defDoc2) {
		if(defDoc2 == null || defDoc2 == null ) return false;
		Object value = PublicUtil.getMapValue("select 1 from baoyin_invcl4adjbill where pk_invcl in (select pk_invcl from bd_invbasdoc where pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc = '"+defDoc2+"' and dr = 0) and dr = 0) and dr = 0");
//		String value = FormularExecutor.execFormular("getColValue(baoyin_invcl4adjbill,pk_invcl,pk_invcl,getColValue(bd_invbasdoc, pk_invcl ,pk_invbasdoc ,getColValue(bd_invmandoc, pk_invbasdoc, pk_invmandoc, vdef2)))", defDoc2);
		if(value != null)
			return true;
		return false;
	}

	/**
	 * 取得目的单据VO
	 * */
	private BillVO getDestVO(String invoicePK) {
		try {
			ClientLink cl = new IAEnvironment().getClientLink();
			String[] sConditions = new String[]{"(v.pk_corp= '"+Util.getCorp()+"')","(v.pk_corp = '"+Util.getCorp()+"' and v.cbilltypecode = 'I9' and v.csourcebillid = '"+invoicePK+"')"};
			BillVO[] m_voBills = BillBO_Client.querybillWithOtherTable(null, null,
					sConditions, null, null, null, new Boolean(false), cl);
			if(m_voBills !=null && m_voBills.length >0 )
			{
				return m_voBills[0];
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public ButtonObject[] getExtendBtns() {
		return super.getExtendBtns();
	}
}
