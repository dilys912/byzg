package nc.ui.ic.ic637;

import nc.ui.pub.beans.UIPanel;
import nc.vo.pub.query.ConditionVO;

public class QueryConditionDlg extends nc.ui.ic.pub.bill.query.QueryConditionDlg {
 	private javax.swing.JTextField ivjtfUnitCode = null;
	private UIPanel ivjUIPanel=null;


/**
 * QueryConditionDlg 构造子注解。
 */
public QueryConditionDlg() {
	super();
}
/**
 * QueryConditionDlg 构造子注解。
 * @param parent java.awt.Container
 */
public QueryConditionDlg(java.awt.Container parent) {
	super(parent);
}
/**
 * QueryConditionDlg 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public QueryConditionDlg(java.awt.Container parent, String title) {
	super(parent, title);
}
/**
 * QueryConditionDlg 构造子注解。
 * @param parent java.awt.Frame
 */
public QueryConditionDlg(java.awt.Frame parent) {
	super(parent);
}
/**
 * QueryConditionDlg 构造子注解。
 * @param parent java.awt.Frame
 * @param title java.lang.String
 */
public QueryConditionDlg(java.awt.Frame parent, String title) {
	super(parent, title);
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-7-12 12:58:52)
 * @return java.lang.String
 * @param table nc.ui.pub.beans.UITable
 */
public String checkCondition(ConditionVO[] conditions) {
	String sReturnError= super.checkCondition(conditions);
	if ((sReturnError != null) && (sReturnError.trim().length() != 0)) {
		return sReturnError;
	}
	try {
		//括弧匹配
		//checkBracket(conditions);

		//全有或全没有
		//checkAllHaveOrNot(conditions, new String[] { "kjqjc", "kjqjd" });
		//checkAllHaveOrNot(conditions, new String[] { "qzrqc", "qzrqd" });

		//最多只能出现一次
		checkOncetime(conditions, new String[] { "pk_corp" });

		//互斥项
		//if(bmust)
		//  strKeys中的fieldcode必须出现一项
		//else
		//  strKeys中的fieldcode最多只能出现一项
		//ArrayList alOneNotOther= new ArrayList();
		//alOneNotOther.add(new String[] { "kjqjc", "kjqjd" });
		//alOneNotOther.add(new String[] { "qzrqc", "qzrqd" });
		//checkOneNotOther(conditions, alOneNotOther, true);
		
//		checkOneNotOther(
//			conditions,
//			new String[] { "cwarehouseclassid", "cwarehouseid" },
//			true);
		

		//必须放至末尾的字段
		//checkNotField(
		//conditions,
		//new String[] {
		//"vfree0flag",
		//"cscodeflag",
		//"vbatchcodeflag",
		//"invclasscodeflag",
		//"measnameflag" });

	} catch (nc.vo.pub.BusinessException be) {
		return be.getMessage();
		//		nc.ui.pub.beans.MessageDialog.showErrorDlg(this,"错误",be.getMessage());

	}

	//return null;

	//检查必输项
	boolean bFoundedCorp= false;
	boolean bFoundedAnalyseType= false;
	boolean bAnalyseType= true;
	boolean bFoundedWarningDate= false;
	boolean bFoundedInv= false;
	int iFoundedWhWhclass= 0;

	for (int i= 0; i < conditions.length; i++) {
		//公司
		if (conditions[i].getFieldCode().trim().equals("pk_corp")) {
			bFoundedCorp= true;
			//库存组织
		} else if (conditions[i].getFieldCode().trim().equals("cwarehouseclassid")) {
			iFoundedWhWhclass++;
			//仓库
		} else if (conditions[i].getFieldCode().trim().equals("cwarehouseid")) {
			iFoundedWhWhclass++;
			//存货
		} else if (conditions[i].getFieldCode().trim().equals("cinventorycode")) {
			bFoundedInv= true;
			//分析方式
		} else if (conditions[i].getFieldCode().trim().equals("analysestyle")) {
			bFoundedAnalyseType= true;
			if (conditions[i].getValue().trim().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008ui","UPP4008ui-000019")/*@res "库龄分析"*/)) {
				//是库龄分析
				bAnalyseType= true;
			} else {
				//是预警分析
				bAnalyseType= false;
			}
			//报警天数
		} else if (conditions[i].getFieldCode().trim().equals("warningdays")) {
			bFoundedWarningDate= true;
		}
	}

	if (bAnalyseType && (iFoundedWhWhclass == 0 && !bFoundedInv)) {
		return null;//nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report","UPP4008report-000076")/*@res "请输入库存组织、仓库或存货！"*/;
	} else if (!bAnalyseType) {
		try {
			checkOneNotOther(
			conditions,
			new String[] { "cwarehouseclassid", "cwarehouseid" },
			true);
		}catch (nc.vo.pub.BusinessException be) {
			return be.getMessage();
			//		nc.ui.pub.beans.MessageDialog.showErrorDlg(this,"错误",be.getMessage());

		}
//		if(conditions!=null){
//			for(int i=0,loop=conditions.length;i<loop;i++){
//				if("def".indexOf(conditions[i].getFieldCode())==0
//						|| "pk_psndoc4".equals(conditions[i].getFieldCode()) || "abctype".equals(conditions[i].getFieldCode())){
//					return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report","UPP4008other-000496")/*@res "预警分析不支持查询条件：计划员，ABC分类，存货自定义项！"*/;
//				}
//			}
//		}
		if(!bFoundedWarningDate)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report","UPP4008report-000077")/*@res "请输入报警天数！"*/;
		else
			return null;
	} else {
		return null;
	}
}
/**
 * 创建者：仲瑞庆
 * 功能：初始化参照
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-27 15:15:52)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void initQueryDlgRef() {
		//以下为对参照的初始化
		//库存组织
		setRefInitWhereClause(
			"cwarehouseclassid",
			"库存组织",		/*-=notranslate=-*/
			" bd_calbody.pk_corp=",
			"pk_corp");
		//一般仓库
		setRefInitWhereClause(
			"cwarehouseid",
			"仓库档案",		/*-=notranslate=-*/
			"gubflag='N'  and pk_corp=",
			"pk_corp");
		 setCorpRefs("pk_corp",new String[]{"cwarehouseclassid","cwarehouseid"});
		//'" + m_sCorpID + "'");
		//入库一般仓库
		setRefInitWhereClause(
			"cinwarehouseid",
			"仓库档案",		/*-=notranslate=-*/
			"gubflag='N'  and pk_corp=",
			"pk_corp");
		//出库一般仓库
		setRefInitWhereClause(
			"coutwarehouseid",
			"仓库档案",		/*-=notranslate=-*/
			"gubflag='N'  and pk_corp=",
			"pk_corp");
		//废品仓库
		setRefInitWhereClause(
			"cwastewarehouseid",
			"仓库档案",		/*-=notranslate=-*/
			"gubflag='Y'  and pk_corp=",
			"pk_corp");
		////客户
		//setRefInitWhereClause(
			//"ccustomerid",
			//"客商档案",
			//"custflag ='Y' and bd_cumandoc.pk_corp=",
			//"pk_corp");
		////'" + m_sCorpID + "'");
		////供应商
		//setRefInitWhereClause(
			//"cproviderid",
			//"供应商档案",
			//"custflag ='N' and bd_cumandoc.pk_corp=",
			//"pk_corp");
		//'" + m_sCorpID + "'");
		//存货,单位编码过滤存货且为非封存存货
		setRefInitWhereClause(
			"cinventorycode",
			"存货档案",		/*-=notranslate=-*/
			" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' and "+
			" bd_invmandoc.pk_corp=",
			"pk_corp");
		//'" + m_sCorpID + "'");
		//存货,单位编码过滤存货且为非封存存货,非劳务，非折扣存货
		setRefInitWhereClause(
			"cinventoryid",
			"存货档案",		/*-=notranslate=-*/
			" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N' and "+
			" bd_invmandoc.pk_corp=",
			"pk_corp");
	}
}