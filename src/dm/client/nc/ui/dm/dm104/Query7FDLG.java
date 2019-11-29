package nc.ui.dm.dm104;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Hashtable;

import nc.ui.ic.pub.report.ArryFormula;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.vo.dm.pub.tools.FormulaTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 单据来源 公共参照对话框
 *
 * @author 樊冠军 2001-7-6
 * @modifier 雷军 2005-2-22 开放getUIDialogContentPane()方法,便于多态
 * @modifier leijun 2006-7-4 适应V5单据模板的修改，废弃一些无用方法
 */
public class Query7FDLG extends  BillSourceDLG {


	public Query7FDLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 主表查询条件
	 * @return
	 */
	public String getHeadCondition() {
		return null;
	}

	/**
	 * 主表隐藏字段
	 * @return
	 */
	public String[] getHeadHideCol() {
		return null;
	}
 

	/**
	 * 根据主表获取子表数据
	 * @param row 选中的表头行
	 */
	public void loadBodyData(int row) {
		try {
			//获得主表ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
			//查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData(getBillType(),
					id, getBodyCondition());
			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			System.out.println("数据加载失败!");
			e.printStackTrace(System.out);
		}

	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.pf.AbstractReferQueryUI#loadHeadData()
	 */
	public void loadHeadData() {
		try {
			//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if(tmpWhere==null||tmpWhere.equals("")){
				tmpWhere = " 1=1 and dm_delivbill_h.apprdate is not null ";
			}else
				tmpWhere = tmpWhere.concat(" and dm_delivbill_h.apprdate is not null" );
//			if (getIsBusinessType()) {
//				businessType = getBusinessType();
//			}
			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
					businessType, tmpWhere);
			
			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();
			

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	}
	public void onOk() {
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 1) {
			MessageDialog.showErrorDlg(this, "", "只能选一条张发运单生成出库单 ");
			return;
		}
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
					m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		
		this.closeOK();
	}
 
}