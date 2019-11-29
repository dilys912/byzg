package nc.ui.ic.icy101;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b999.PrayvsbusiBO_Client;
import nc.ui.ic.ic301.OrderPointHelper;
import nc.ui.ic.ic301.QueryConditionDlg;
import nc.ui.ic.ic606.CargDisHelper;
import nc.ui.ic.icy101.ClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ic.ic301.OrderpointBillVO;
import nc.vo.ic.ic301.OrderpointHeadVO;
import nc.vo.ic.ic301.OrderpointVO;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.ic.ic601.InvOnHandVO;
import nc.vo.ic.ic605.CargcardVO;
import nc.vo.ic.ic637.StockAgeItemVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 现存量来查  yqq  2016-10-26
 * 
 * 测试编辑报表
 */

public class ClientUI extends nc.ui.ic.pub.report.IcBaseReport {
	/** 报表模板界面类 */
	private ReportBaseClass ivjReportBase = null;
	/** 查询条件对话框 */
	private QueryConditionDlg ivjQueryConditionDlg = null;
	/** 报表模板节点名 */
	private String m_sRNodeName = "40083018";
	/** 功能节点编码 */
	private String m_sPNodeCode = "40083018";

	private String m_sCorpID = "1016"; // 公司ID
	private String m_sCorpCode = null;
	private String m_sCorpName = null;
	private String m_sUserID = null; // 当前使用者ID
	private String m_sLogDate = null; // 当前登录日期
	private String m_sUserName = null; // 当前使用者名称

	private ButtonObject m_boQuery = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "查询" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("common", "UC001-0000006")/*
																		 * @res
																		 * "查询"
																		 */, 2,
			"查询"); /* -=notranslate=- */

	// 按钮组
	private ButtonObject[] m_MainButtonGroup = { m_boQuery };

	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。 ：
	 * 
	 * @return java.lang.String
	 */
	public String getDefaultPNodeCode() {
		return m_sPNodeCode;
	}

	/**
	 * 返回 ReportBaseClass 特性值。
	 * 
	 * @return nc.ui.pub.report.ReportBaseClass
	 */
	/* 警告：此方法将重新生成。 */
	public ReportBaseClass getReportBaseClass() {
		if (ivjReportBase == null) {
			try {
				ivjReportBase = new nc.ui.pub.report.ReportBaseClass();
				ivjReportBase.setName("ReportBase");
				// user code begin {1}
				ivjReportBase.setRowNOShow(true);
				ivjReportBase.setBodyMenuShow(false);
				ivjReportBase.setTatolRowShow(true);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				// handleException(ivjExc);
			}
		}
		return ivjReportBase;
	}

	/**
	 * 此处插入方法说明。 
	 * @return java.lang.String
	 */
	public java.lang.String getUserID() {
		return m_sUserID;
	}

	/**
	 * 此处插入方法说明。
	 */
	private void initialize() {

		setName("ClientUI");
		setLayout(new java.awt.BorderLayout());
		setSize(774, 419);
		add(getReportBaseClass(), "Center");
		// getCEnvInfo();

		// 初始化模板
		initReportTemplet(m_sPNodeCode);
		// 设置按钮组
		setButtons(getButtonArray(m_MainButtonGroup));

	}

	/**
	 * 此处插入方法说明。 
	 * 
	 * @param sNodeName
	 *            java.lang.String
	 */
	public void initReportTemplet(String sNodeName) {

		// 读取模版数据
		try {
			getReportBaseClass().setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000019")/*
														 * @res
														 * "不能得到模版，请与系统管理员联系！"
														 */);
			return;
		}

	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boQuery)
			onQuery(true);

		else
			super.onButtonClicked(bo);
	}

	/**
	 * 根据相关查询条件，查询单据 
	 */
	public void onQuery(boolean bQuery) {
		getConditionDlg().hideNormal();
		if (bQuery || !m_bEverQry) {
			getConditionDlg().showModal();
			m_bEverQry = true;
		} else {
			getConditionDlg().onButtonConfig();
		}
		if (getConditionDlg().getResult() == QueryDLG.ID_OK) {

			String bodycode = null;
			String invcode = null;
			ConditionVO[] cndvos = getConditionDlg().getConditionVO();
			for (int i = 0; i < cndvos.length; i++) {
				if ("bodycode".equals(cndvos[i].getFieldCode())) {
					bodycode = cndvos[i].getValue().trim();
				}
				if ("invcode".equals(cndvos[i].getFieldCode())) {
					invcode = cndvos[i].getValue().trim();
				}
			}

		getReportBaseClass().setBodyDataVO(getInvclByInvcode(bodycode,invcode));
		}

	}

	private InvOnHandItemVO[] getInvclByInvcode(String bodycode,String invcode) {
		nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
		.getInstance();
		m_sCorpID = ce.getCorporation().getPrimaryKey();
		StringBuffer sql = new StringBuffer();
		sql.append(" select p.unitname,   ") 
		.append("             l.invclassname,   ") 
		.append("             c.invcode,   ") 
		.append("             c.invname,   ") 
		.append("             c.invspec,   ") 
		.append("             sum(m.nonhandnum) as num   ") 
		.append("        from ic_onhandnum m   ") 
		.append("        left join bd_invbasdoc c   ") 
		.append("          on m.cinvbasid = c.pk_invbasdoc   ") 
		.append("        left join bd_invmandoc n ") 
		.append("          on c.pk_invbasdoc=n.pk_invbasdoc ") 
		.append("        left join bd_calbody y   ") 
		.append("          on m.ccalbodyid = y.pk_calbody   ") 
		.append("        left join bd_corp p   ") 
		.append("          on m.pk_corp = p.pk_corp   ") 
		.append("        left join bd_invcl l   ") 
		.append("          on l.pk_invcl = c.pk_invcl   ") 
		.append("       where y.bodycode  = '"+bodycode+"'   ") 
		.append("         and c.invcode = '"+invcode+"'   ") 
		.append("         and n.pk_corp='"+m_sCorpID+"' ") 
		.append("         and y.pk_corp='"+m_sCorpID+"' ") 
		.append("         and nvl(y.dr,0) = 0  ") 
		.append("         and nvl(c.dr,0) = 0 ") 
		.append("         and nvl(n.dr,0) = 0     ") 
		.append("         and nvl(l.dr,0) = 0       ") 

		.append("         or y.bodycode  = '"+bodycode+"'   ") 
		.append("         and n.pk_corp='"+m_sCorpID+"' ") 
		.append("         and y.pk_corp='"+m_sCorpID+"' ") 
		.append("         and nvl(y.dr,0) = 0  ") 
		.append("         and nvl(c.dr,0) = 0 ") 
		.append("         and nvl(n.dr,0) = 0     ") 
		.append("         and nvl(l.dr,0) = 0       ") 
		.append(" 		    group by p.unitname,   ") 
		.append(" 		        l.invclassname,   ") 
		.append(" 		        c.invcode,   ") 
		.append(" 		        c.invname,   ") 
		.append(" 		        c.invspec; ");

		ArrayList list = new ArrayList();
		IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			list  = (ArrayList) uAPQueryBS.executeQuery(sql.toString(), new BeanListProcessor(InvOnHandItemVO.class));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InvOnHandItemVO[] vo =(InvOnHandItemVO[]) list.toArray(new InvOnHandItemVO[list.size()]);
		for(int i = 0;i<list.size();i++){
			vo[i]=(InvOnHandItemVO) list.get(i);
		}		
		return vo;
	}

	/**
	 * 类型说明
	 */
	private void resetConditionVO(nc.vo.pub.query.ConditionVO[] conVO) {

		if (conVO != null && conVO.length != 0) {
			for (int i = 0; i < conVO.length; i++) {
				if ("like".equals(conVO[i].getOperaCode().trim())
						&& conVO[i].getFieldCode() != null) {

					conVO[i].setValue("%" + conVO[i].getValue() + "%");
				}
			}
		}
	}

	private QueryConditionClient getConditionDlg() {
		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new QueryConditionDlg(this);
			// 读查询模版数据
			ivjQueryConditionDlg.setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);
			
		      ArrayList alCorpIDs = new ArrayList();
		      try {
		        alCorpIDs = ICReportHelper.queryCorpIDs(this.m_sUserID);
		      } catch (Exception e) {
		        SCMEnv.error(e);
		      }			
		}
		return ivjQueryConditionDlg;
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @author ：cm
	 * @2012/9/5
	 * 
	 * @since v50
	 */
/*	public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation()
				.getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ParentCorp;
	}*/

	@Override
	public String getCorpID() {
		return m_sCorpID;
	}

	@Override
	public AggregatedValueObject getReportVO() {
		// TODO Auto-generated method stub
		return null;
	}

}