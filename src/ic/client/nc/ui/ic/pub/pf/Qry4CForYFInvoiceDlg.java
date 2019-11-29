package nc.ui.ic.pub.pf;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.pf.BillSourceDLG;
import nc.vo.dm.dm004.OutStoreVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

public class Qry4CForYFInvoiceDlg extends BillSourceDLG {
	protected int[] m_iaScale = null;
	private Hashtable voht = null;
	private BillListPanel listPanel = null;
	private UITextField txt1;
	public IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

	public Qry4CForYFInvoiceDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);
	}

	public BillListPanel getbillListPanel() {
		if (this.ivjbillListPanel == null) {
			try {
				this.ivjbillListPanel = new BillListPanel();
				this.ivjbillListPanel.setName("billListPanel");

				BillTempletVO vo = this.ivjbillListPanel.getDefaultTemplet("YFD", null, getOperator(), getPkCorp(), getNodeKey());

				BillListData billDataVo = new BillListData(vo);

				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}

				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				this.ivjbillListPanel.setListData(billDataVo);

				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						this.ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						this.ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				this.ivjbillListPanel.setMultiSelect(true);
			} catch (Throwable ivjExc) {
				ivjExc.printStackTrace();
			}
		}
		this.listPanel = this.ivjbillListPanel;
		return this.listPanel;
	}

	public String getHeadCondition() {
		String sHeadCondition = null;

		if ((getPkCorp() != null) && (getPkCorp().trim().length() > 0)) {
			sHeadCondition = " head.pk_corp = '" + getPkCorp().trim() + "' AND  (head.fbillflag = 3 OR head.fbillflag = 4 ) ";
		}
		return sHeadCondition;
	}

	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(HYBillVO.class.getName(), OutStoreVO.class.getName(), OutStoreVO.class.getName());

			ArrayList al = new ArrayList();
			ArrayList key = new ArrayList();
			int index = 0;
			for (AggregatedValueObject agg : selectedBillVOs) {
				OutStoreVO vo = (OutStoreVO) agg.getParentVO();
				if (this.voht.size() > 0) {
					String vokey = vo.getVbillcode() + "," + vo.getPk_invbasdoc();
					OutStoreVO old_vo = (OutStoreVO) this.voht.get(vokey);
					selectedBillVOs[index].getParentVO().setAttributeValue("vuserdef12", old_vo.vuserdef12);
					index++;
				}

				if (vo.getPk_transcust() == null) {
					MessageDialog.showErrorDlg(this, "错误Error", "承运商为空，无法生成运费发票！The carriers is empty, unable to generate shipping invoice!");
					return;
				}
				if (key.size() == 0) {
					key.add(vo.getPk_transcust());
				} else if (!key.contains(vo.getPk_transcust())) {
					MessageDialog.showErrorDlg(this, "错误Error", "所选数据存在多个承运商，无法生成运费发票!The selected data there are multiple carriers, freight invoices can not be generated");
					return;
				}

				al.add(vo);
			}
			HYBillVO vo = new HYBillVO();
			if ((selectedBillVOs != null) && (selectedBillVOs.length > 0)) {
				vo.setParentVO(selectedBillVOs[0].getParentVO());
				vo.setChildrenVO((CircularlyAccessibleValueObject[]) al.toArray(new OutStoreVO[0]));
				this.retBillVo = vo;
				this.retBillVos = new HYBillVO[] {
					vo
				};
			} else {
				MessageDialog.showErrorDlg(this, "错误Error", "没有选择数据！Do not select the data!");
				return;
			}
		}
		closeOK();
	}

	public void loadHeadData() {
		try {
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (this.m_whereStr == null) tmpWhere = " (" + getHeadCondition() + ")";
				else tmpWhere = " (" + this.m_whereStr + ") and (" + getHeadCondition() + ")";
			} else {
				tmpWhere = this.m_whereStr;
			}
			String sql = "select head.vbillcode,body.cinvbasid pk_invbasdoc,body.cinventoryid,body.noutnum num,head.cdilivertypeid fytype,head.cwastewarehouseid pk_transcust ,head.pk_corp ,head.vuserdef7 cph,csourcebillbid,head.vuserdef12,head.dbilldate" +
					" ,inv.invcode " +//add by shikun 2014-04-04 
					" from (select * from ic_general_b where  nvl(dr,0)=0 and csourcetype='7F' and nvl(vuserdef5,0)< 1 ) body  " +
					" inner join (select * from ic_general_h  where  nvl(dr,0)=0 and nvl(vuserdef20,0)<>1 and cbilltypecode='4C') head on  head.cgeneralhid = body.cgeneralhid  " +
					" inner join dm_delivbill_b dmb on dmb.pk_delivbill_b = body.csourcebillbid " +
					" inner join  dm_delivbill_h dmh on dmh.pk_delivbill_h = dmb.pk_delivbill_h " +
					" inner join bd_invbasdoc inv on inv.pk_invbasdoc=body.cinvbasid   where ";//add by shikun 2014-04-04

			String sql1 = "select head.vbillcode,body.cinvbasid pk_invbasdoc,body.cinventoryid,body.noutnum num,dev.pktrancust pk_transcust ,head.pk_corp ,head.vuserdef7 cph,csourcebillbid, dev.pkdelivmode fytype,head.vuserdef12,head.dbilldate " +
					" ,inv.invcode " +//add by shikun 2014-04-04 
					"from (select * from ic_general_b where  nvl(dr,0)=0 and csourcetype='7F' and nvl(vuserdef5,0)< 1  ) body  " +
					"inner join (select * from ic_general_h where  nvl(dr,0)=0 and nvl(vuserdef20,0)<>1 and cbilltypecode='4Y') head on  head.cgeneralhid = body.cgeneralhid  " +
					"inner join bd_invbasdoc inv on inv.pk_invbasdoc=body.cinvbasid   " +//add by shikun 2014-04-04
					"left join (select h.pkdelivmode,b.pk_delivbill_b ,h.pktrancust,b.pksendaddress,b.pkarriveaddress from dm_delivbill_b b ,dm_delivbill_h h where h.pk_delivbill_h=b.pk_delivbill_h   and nvl(b.dr,0)=0 ) dev on body.csourcebillbid = dev.pk_delivbill_b where ";
			
			String sql2 = "select head.vbillcode,body.cinvbasid pk_invbasdoc,body.cinventoryid,body.noutnum num,head.cdilivertypeid fytype,head.cwastewarehouseid pk_transcust , " +
					"head.pk_corp ,head.vuserdef7 cph,csourcebillbid,head.vuserdef12,head.dbilldate ,inv.invcode " +
					"from (select * from ic_general_b where  nvl(dr,0)=0 and csourcetype='30' and nvl(vuserdef5,0)< 1 ) body  " +
					"inner join (select * from ic_general_h  where  nvl(dr,0)=0 and nvl(vuserdef20,0)<>1 and cbilltypecode='4C') head on  head.cgeneralhid = body.cgeneralhid  " +
					"inner join so_saleorder_b sob on sob.corder_bid = body.csourcebillbid " +
					"inner join so_sale so on so.csaleid = sob.csaleid " +
					"inner join bd_invbasdoc inv on inv.pk_invbasdoc=body.cinvbasid   where";
			
			
			OutStoreVO[] vos = (OutStoreVO[]) null;
			if ("4C".equals(getBillType())) {
				String pk_corp = getPkCorp();
				if(pk_corp.equals("1078") || pk_corp.equals("1100")||pk_corp.equals("1108")){//宝钢制盖业务改动，wkf--2014-12-11
					
					sql2 = sql2.concat(tmpWhere);
					sql2 += " and body.noutnum>0 and so.vdef16 = '是' ";
					ArrayList al = (ArrayList) this.iquery.executeQuery(sql2, new BeanListProcessor(OutStoreVO.class));
					vos = (OutStoreVO[]) al.toArray(new OutStoreVO[0]);
				}else{
					sql = sql.concat(tmpWhere);
					ArrayList al = (ArrayList) this.iquery.executeQuery(sql, new BeanListProcessor(OutStoreVO.class));
					vos = (OutStoreVO[]) al.toArray(new OutStoreVO[0]);
				}
			} else if ("4Y".equals(getBillType())) {
				sql1 = sql1.concat(tmpWhere);
				ArrayList al = (ArrayList) this.iquery.executeQuery(sql1, new BeanListProcessor(OutStoreVO.class));
				vos = (OutStoreVO[]) al.toArray(new OutStoreVO[0]);
			}

			vos = RefChange(vos);

			getbillListPanel().setHeaderValueVO(vos);
			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000237"), NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000490"));
		}
	}

	public OutStoreVO[] RefChange(OutStoreVO[] vos) {
		String key = null;
		this.voht = new Hashtable();
		HashMap hm = new HashMap();
		OutStoreVO temp;
		for (OutStoreVO vo : vos) {
			key = vo.getVbillcode() + "," + vo.getPk_invbasdoc();
			//add by shikun 2014-04-04 
			String invcode = vo.getInvcode().substring(0,2);
			UFDouble dqnum = vo.getNum() == null ? new UFDouble(0) : vo.getNum();//当前值
			UFDouble dqnumzs = new UFDouble(dqnum.toString(),0);//当前值取整
			if (hm.containsKey(key)) {
				temp = (OutStoreVO) hm.get(key);
				//如果汇总值为NULL，直接为当前值,如果汇总值不为NULL，加上当前值
				UFDouble hznum = temp.getNum() == null ? dqnum : temp.getNum().add(dqnum);//汇总值
				UFDouble hznumzs = new UFDouble(hznum.toString(),0);//汇总值取整
				//add by shikun 运费发票：钢卷、铝卷、纸等需要增加按重量合计；且铝卷、钢卷、垫罐纸运输按重量计算
				//垛数为Integer，如果按重量计算那么从UFDouble->Integer，如果重量有小数则会出现误差。
				if ("25".equals(invcode)) {
					temp.setRowcount(Integer.valueOf(hznumzs.toString()));
				}else{
					temp.setRowcount(Integer.valueOf(temp.getRowcount().intValue() + 1));
				}
				//edit by shikun 2014-04-04 
//				temp.setNum(temp.getNum() == null ? new UFDouble(0) : temp.getNum().add(vo.getNum() == null ? new UFDouble(0) : vo.getNum()));
				temp.setNum(temp.getNum() == null ? 
						(vo.getNum() == null ? new UFDouble(0) : vo.getNum()) //如果汇总值为NULL，直接为当前值
						: temp.getNum().add(vo.getNum() == null ? new UFDouble(0) : vo.getNum()));//如果汇总值不为NULL，加上当前值
			} else {
				//add by shikun 运费发票：钢卷、铝卷、纸等需要增加按重量合计；且铝卷、钢卷、垫罐纸运输按重量计算
				//垛数为Integer，如果按重量计算那么从UFDouble->Integer，如果重量有小数则会出现误差。
				if ("25".equals(invcode)) {
					vo.setRowcount(Integer.valueOf(dqnumzs.toString()));
				}else{
					vo.setRowcount(Integer.valueOf(1));
				}
				hm.put(key, vo);
			}
			if (this.voht.containsKey(key)) continue;
			this.voht.put(key, vo);
		}

		ArrayList ret = new ArrayList();
		if (!hm.isEmpty()) {
			Set set = hm.keySet();
			String[] keys = (String[]) set.toArray(new String[0]);
			String[] arrayOfString1 = keys;
			OutStoreVO[] localOutStoreVO1 = new OutStoreVO[arrayOfString1.length];
			for (int ttt = 0; ttt < localOutStoreVO1.length; ttt++) {
				String s = arrayOfString1[ttt];
				ret.add(hm.get(s));
			}
		}
		Collections.sort(ret, new sortByVO());
		return (OutStoreVO[]) ret.toArray(new OutStoreVO[0]);
	}

	protected JPanel getUIDialogContentPane() {
		JPanel panel = super.getUIDialogContentPane();
		int count = panel.getComponentCount();
		if (count > 0) {
			UIPanel comp = (UIPanel) panel.getComponent(0);
			// edit by zip:2014/3/17
			if (this.txt1 == null) {
				UILabel lbl1 = new UILabel(Transformations.getLstrFromMuiStr("运费计算总数", "The total number of freight"));
				this.txt1 = new UITextField(8);
				this.txt1.setEditable(false);
				comp.add(lbl1);
				comp.add(this.txt1);
			}
			// end edit
		}
		return panel;
	}

	private void calcTotal() {
		UFDouble totalnum = new UFDouble(0.0D);
		for (int i = 0; i < getbillListPanel().getBillListData().getHeadBillModel().getRowCount(); i++) {
			if (getbillListPanel().getBillListData().getHeadBillModel().getRowState(i) != 4) continue;
			String rcount = String.valueOf(getbillListPanel().getHeadBillModel().getValueAt(i, "rowcount"));
			rcount = (rcount == null) || (rcount.equals("")) || (rcount.equals("null")) ? "0" : rcount;
			totalnum = totalnum.add(new UFDouble(rcount));
		}

		this.txt1.setText(totalnum.toString());
	}

	public void mouse_doubleclick(BillMouseEnent e) {
		if (e.getPos() == 0) {
			int headRow = e.getRow();
			headRowDoubleClicked(headRow);

			calcTotal();
		}
	}

	private void headRowDoubleClicked(int headRow) {
		headRowChange(headRow);

		BillModel bodyModel = getbillListPanel().getBodyBillModel();
		BillModel headModel = getbillListPanel().getHeadBillModel();

		if (getbillListPanel().isMultiSelect()) {
			if (headModel.getRowState(headRow) == 4) {
				headModel.setRowState(headRow, -1);
				for (int i = 0; i < bodyModel.getRowCount(); i++)
					bodyModel.setRowState(i, -1);
			} else {
				headModel.setRowState(headRow, 4);
				for (int i = 0; i < bodyModel.getRowCount(); i++) {
					bodyModel.setRowState(i, 4);
				}
			}
			getbillListPanel().setBodyModelDataCopy(headRow);
		}
	}

	private synchronized void headRowChange(int iNewRow) {
		if ((getbillListPanel().getHeadBillModel().getValueAt(iNewRow, getpkField()) != null) && (!getbillListPanel().setBodyModelData(iNewRow))) {
			loadBodyData(iNewRow);

			getbillListPanel().setBodyModelDataCopy(iNewRow);
		}

		getbillListPanel().repaint();
	}

	class sortByVO implements Comparator {
		sortByVO() {
		}

		public int compare(Object bfcpobj, Object afcpobj) {
			String bfvalue = "0";
			String afvalue = "0";
			if ((bfcpobj instanceof OutStoreVO)) {
				bfvalue = ((OutStoreVO) bfcpobj).getVbillcode();
				afvalue = ((OutStoreVO) afcpobj).getVbillcode();
				if ((bfvalue == null) || (afvalue == null)) return 0;
			}
			return bfvalue.compareToIgnoreCase(afvalue);
		}
	}
}