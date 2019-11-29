package nc.ui.rc.receive;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import nc.ui.bd.languagetransformations.Transformations;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.component.JudgeableServiceComponent;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.uap.sf.ICreateCorpService;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IBusiType;
import nc.ui.common.ListProcessor;
import nc.ui.ic.pub.QueryInfo;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.PoChangeUI;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoToBackRcQueDLG;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pr.pray.IButtonConstPr;
import nc.ui.pr.pray.PrayUIQueryDlg;
import nc.ui.pr.pray.PraybillHelper;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.CheckISSellProxyHelper;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillData;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.rc.pub.BackReasonRefModel;
import nc.ui.rc.pub.CPurchseMethods;
import nc.ui.rc.pub.InvRefModelForRepl;
import nc.ui.rc.pub.LocateDlg;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.rc.pub.RcTool;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.setpart.SetPartDlg;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.ia.bill.BillVO;
import nc.vo.ic.ic637.StockAgeItemVO;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pu.exception.RwtRcToPoException;
import nc.vo.pu.exception.RwtRcToScException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.rc.receive.IArriveorderStatus;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.uap.pf.PFBusinessException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @功能描述：到货单维护
 * @作者：晁志平
 * @创建日期：(2001-5-24 9:42:56)
 */
public class ArriveUI extends nc.ui.pub.ToftPanel implements BillEditListener,
		BillTableMouseListener, ActionListener, BillEditListener2,
		ListSelectionListener, BillBodyMenuListener,
		IBillModelSortPrepareListener, ISetBillVO, IBillExtendFun, ICheckRetVO,
		IBillRelaSortListener2, ILinkMaintain,// 关联修改
		ILinkAdd,// 关联新增
		ILinkApprove,// 审批流
		ILinkQuery,// 逐级联查
		BillCardBeforeEditListener
{
	  private File txtFile = null;
	  private int xml;
	  private File xmlFile = null;
	  private UITextField txtfFileUrl = null;
	//单据导入定义--start--
		public static Workbook w   = null;
		public static int rows=0;
		public static SaleorderBVO[] wbvo = null;
		public static String pk_corp="";
		//单据导入定义--end--	
	// QC是否启用
	private boolean m_bQcEnabled = false;
	// 列表是否加载过
	private boolean m_bLoaded = false;
	// 按钮树实例,since v51
	private ButtonTree m_btnTree = null;

	// 打不开此节点的原因
	private String m_strNoOpenReasonMsg = null;
	//
	private boolean m_bQueriedFlag = false;

	// 解决自由项不能正确加载问题：
	class MyBillData implements IBillData {
		public void prepareBillData(nc.ui.pub.bill.BillData bd) {
			ArriveUI.this.initBillBeforeLoad(bd);
		}
	}

	/* 所有表体缓存 */
	private Hashtable hBodyItem = new Hashtable();
	// 是否改变了业务类型
	boolean isChangeBusitype = true;
	/** 揸序时特殊处理用 */
	private boolean isFrmList = false;
	/** 存放当前表头对应的表体 */
	private ArriveorderItemVO[] items = null;

	// 定位对话框
	private LocateDlg locatedlg = null;
	// 到货卡片
	private BillCardPanel m_arrBillPanel = null;
	// 到货查询结果
	private ArriveorderVO[] m_arriveVOs = null;
	// 到货列表
	private BillListPanel m_arrListPanel = null;
	// 存量查询对话框
	ATPForOneInvMulCorpUI m_atpDlg = null;
	/* 采购退货查询框 */
	private PoToBackRcQueDLG m_backQuePoDlg = null;
	/* 委外退货查询框 */
	private RcToScQueDLG m_backQueScDlg = null;
	/* 退货参照采购订单选择界面 */
	private ArrFrmOrdUI m_backRefUIPo = null;
	/* 退货参照委外订单选择界面 */
	private ArrFrmOrdUI m_backRefUISc = null;

	// 多语翻译工具
	private NCLangRes m_lanResTool = NCLangRes.getInstance();

	/* 卡片按钮定义 */
	private ButtonObject m_btnCheck = null;// new ButtonObject(
											// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPT40040303-000009")/*@res
											// "检验"*/,
											// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000029")/*@res
											// "检验到货单"*/, 2, "检验");
											// /*-=notranslate=-*/
	// 业务类型
	private ButtonObject m_btnBusiTypes = null;// new
												// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000003")/*@res
												// "业务类型"*/,
												// m_lanResTool.getStrByID("common","UC001-0000003")/*@res
												// "业务类型"*/, 2,"业务类型");
												// /*-=notranslate=-*/
	// 增加组
	private ButtonObject m_btnAdds = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000002")/*@res
											// "增加"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000230")/*@res
											// "生成到货单"*/, 2,"增加");
											// /*-=notranslate=-*/
	// 退货组
	private ButtonObject m_btnBackPo = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000021")/*@res
											// "采购订单"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000231")/*@res
											// "参照采购订单生成到货单"*/, 2,"采购订单");
											// /*-=notranslate=-*/
	private ButtonObject m_btnBackSc = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000022")/*@res
											// "委外订单"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000232")/*@res
											// "参照委外订单生成到货单"*/, 2,"委外订单");
											// /*-=notranslate=-*/
	private ButtonObject m_btnBacks = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
											// "退货"*/,m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
											// "退货"*/,2, "退货");
	// 单据维护组
	private ButtonObject m_btnModify = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
											// "修改"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000235")/*@res
											// "修改到货单"*/, 2,"修改");
											// /*-=notranslate=-*/
	private ButtonObject m_btnSave = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000001")/*@res
											// "保存"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000237")/*@res
											// "保存修改结果"*/, 2,"保存");
											// /*-=notranslate=-*/
	private ButtonObject m_btnCancel = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000008")/*@res
											// "取消"*/,
											// m_lanResTool.getStrByID("common","UC001-0000008")/*@res
											// "取消"*/, 2,"取消");
											// /*-=notranslate=-*/
	private ButtonObject m_btnDiscard = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
												// "作废"*/,
												// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
												// "作废"*/, 2,"作废");
												// /*-=notranslate=-*/
	private ButtonObject m_btnSendAudit = null;// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000017")/*
												// @res "送审"
												// */,m_lanResTool.getStrByID("40040101","UPP40040101-000451")/*
												// @res "送审到货单" */, 2, "送审");
												// /*-=notranslate=-*/
	private ButtonObject m_btnMaintains = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
												// "单据维护"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
												// "单据维护"*/,2,"单据维护");
	// 行操作组
	private ButtonObject m_btnDelLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000013")/*@res
												// "删行"*/,
												// m_lanResTool.getStrByID("common","UC001-0000013")/*@res
												// "删行"*/, 2,"删行");
												// /*-=notranslate=-*/
	private ButtonObject m_btnCpyLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000014")/*@res
												// "复制行"*/,
												// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "复制行"*/, 2,"复制行");
												// /*-=notranslate=-*/
	private ButtonObject m_btnPstLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "粘贴行"*/,
												// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "粘贴行"*/, 2,"粘贴行");
												// /*-=notranslate=-*/
	private ButtonObject m_btnLines = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000011")/*@res
											// "行操作"*/,m_lanResTool.getStrByID("common","UC001-0000011")/*@res
											// "行操作"*/,2, "行操作");
	// 单据浏览组
	private ButtonObject m_btnBrowses = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000021")/*@res
												// "浏览"*/,m_lanResTool.getStrByID("common","UC001-0000021")/*@res
												// "浏览"*/,2, "浏览");
	private ButtonObject m_btnQuery = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
											// "查询"*/,
											// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
											// "查询"*/, 2,"查询");
											// /*-=notranslate=-*/
	private ButtonObject m_btnFirst = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH031")/*@res
											// "首页"*/,
											// m_lanResTool.getStrByID("common","UCH031")/*@res
											// "首页"*/, 2,"首页");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrev = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH033")/*@res
											// "上一页"*/,
											// m_lanResTool.getStrByID("common","UCH033")/*@res
											// "上一页"*/, 2,"上一页");
											// /*-=notranslate=-*/
	private ButtonObject m_btnNext = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH034")/*@res
											// "下一页"*/,
											// m_lanResTool.getStrByID("common","UCH034")/*@res
											// "下一页"*/, 2,"下一页");
											// /*-=notranslate=-*/
	private ButtonObject m_btnLast = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH032")/*@res
											// "末页"*/,
											// m_lanResTool.getStrByID("common","UCH032")/*@res
											// "末页"*/, 2,"末页");
											// /*-=notranslate=-*/
	private ButtonObject m_btnLocate = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
											// "定位"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
											// "定位"*/, 2,"定位");
											// /*-=notranslate=-*/
	private ButtonObject m_btnRefresh = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
												// "刷新"*/,
												// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
												// "刷新"*/, 2,"刷新");
												// /*-=notranslate=-*/
	// 切换
	private ButtonObject m_btnList = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
											// "列表显示"*/,
											// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
											// "列表显示"*/, 2,"切换");
											// /*-=notranslate=-*/
	// 执行组(审批、弃审与消息中心共用)
	private ButtonObject m_btnActions = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
												// "执行"*/,
												// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
												// "执行"*/, 0,"执行");
												// /*-=notranslate=-*/
	// 辅助组
	private ButtonObject m_btnOthers = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "辅助"*/,2, "辅助");
	public ButtonObject m_btnCombin = null;// ButtonObject(m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
											// "合并显示"*/,
											// m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
											// "合并显示"*/, 2,"合并显示");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrints = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "打印"*/,
											// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "打印"*/, 2,"打印");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrint = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "打印"*/,
											// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "打印"*/, 2,"打印");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrintPreview = null;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/, 2,"预览");
													// /*-=notranslate=-*/
	private ButtonObject m_btnUsable = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
											// "存量查询"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
											// "存量查询"*/, 2,"存量查询");
											// /*-=notranslate=-*/
	private ButtonObject m_btnQueryBOM = null;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
												// "成套件"*/,
												// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
												// "成套件"*/, 2,"成套件");
												// /*-=notranslate=-*/
	private ButtonObject m_btnQuickReceive = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
													// "快速收货"*/,
													// m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
													// "快速收货"*/, 2,"快速收货");
													// /*-=notranslate=-*/
	protected ButtonObject m_btnDocument = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
												// "文档管理"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
												// "文档管理"*/, 2,"文档管理");
												// /*-=notranslate=-*/
	protected ButtonObject m_btnLookSrcBill = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
													// "联查"*/,
													// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
													// "联查"*/, 2,"联查");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryForAudit = null;// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000032")/*
													// @res "状态查询"
													// */,m_lanResTool.getStrByID("40040101","UPP40040101-000450")/*
													// @res "审批状态查询" */, 2,
													// "状态查询");
													// /*-=notranslate=-*/
	private ButtonObject m_btnImportBill = null;  //EXCL单据导入
	
	private ButtonObject m_btnImportXml = null;  //XML导入  yqq 2016-11-02 测试
	
	
	/* 卡片按钮菜单 */

	private ButtonObject m_aryArrCardButtons[] = null;

	/* 列表按钮定义 */
	private ButtonObject m_btnSelectAll = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000041")/*@res
												// "全选"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000238")/*@res
												// "全部选定"*/, 2,"全选");
												// /*-=notranslate=-*/
	private ButtonObject m_btnSelectNo = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000042")/*@res
												// "全消"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000233")/*@res
												// "全部取消"*/, 2,"全消");
												// /*-=notranslate=-*/
	private ButtonObject m_btnModifyList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
												// "修改"*/,
												// m_lanResTool.getStrByID("40040301","UPPSCMCommon-000291")/*@res
												// "修改单据"*/, 2, "列表修改");
												// /*-=notranslate=-*/
	private ButtonObject m_btnDiscardList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
													// "作废"*/,
													// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
													// "作废"*/, 2,"列表作废");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
												// "查询"*/,
												// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
												// "查询"*/, 2,"列表查询");
												// /*-=notranslate=-*/
	private ButtonObject m_btnCard = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
											// "卡片显示"*/,
											// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
											// "卡片显示"*/, 2,"列表切换");
											// /*-=notranslate=-*/
	private ButtonObject m_btnEndCreate = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000043")/*@res
												// "放弃转单"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000234")/*@res
												// "放弃到货单保存"*/, 2,"放弃转单");
												// /*-=notranslate=-*/
	private ButtonObject m_btnRefreshList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
													// "刷新"*/,
													// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
													// "刷新"*/, 2,"刷新");
													// /*-=notranslate=-*/

	// 辅助组
	private ButtonObject m_btnUsableList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
												// "存量查询"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
												// "存量查询"*/, 2,"列表存量查询");
												// /*-=notranslate=-*/
	protected ButtonObject m_btnDocumentList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
													// "文档管理"*/,
													// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
													// "文档管理"*/, 2,"列表文档管理");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryBOMList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
													// "成套件"*/,
													// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
													// "成套件"*/, 2,"列表成套件");
													// /*-=notranslate=-*/
	private ButtonObject m_btnPrintPreviewList = null;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
														// "预览"*/,
														// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
														// "预览"*/, 2,"列表打印预览");
														// /*-=notranslate=-*/
	private ButtonObject m_btnPrintList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
												// "打印单据"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
												// "打印单据"*/, 2,"列表打印");
												// /*-=notranslate=-*/
	private ButtonObject m_btnOthersList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
												// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
												// "辅助"*/,2, "辅助");
												// /*-=notranslate=-*/

	/* 列表按钮组 */
	private ButtonObject m_aryArrListButtons[] = null;
	/* 消息中心按钮组 */
	private ButtonObject m_btnAudit = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000027")/*
											// @res "审批"
											// */,m_lanResTool.getStrByID("common","UC001-0000027")/*
											// @res "审批" */, 2, "审批");
											// /*-=notranslate=-*/
	private ButtonObject m_btnUnAudit = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000028")/*
												// @res "弃审"
												// */,m_lanResTool.getStrByID("40040401","UPP40040401-000149")/*
												// @res "执行弃审操作" */, 5, "弃审");
												// /*-=notranslate=-*/
	private ButtonObject m_btnOthersMsgCenter = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
														// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
														// "辅助"*/,2, "辅助");
	private ButtonObject m_btnActionMsgCenter = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
														// "执行"*/,
														// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
														// "执行"*/, 0,"执行");
														// /*-=notranslate=-*/
	private ButtonObject m_aryMsgCenter[] = null;

	// 到货查询条件对话框
	private RcQueDlg m_dlgArrQueryCondition = null;
	/* 快速收货对话框 */
	private QueryOrdDlg m_dlgQuickArr = null;
	// 缓存单据行ID对应的存货管理ID
	private Hashtable m_hBillIDsForCmangids = new Hashtable();
	/* 缓存时间戳用于处理并发 */
	private HashMap m_hTS = null;
	// //缓存是否辅计量管理标志
	// Hashtable m_hIsAssMana = new Hashtable();
	// boolean isFlagsCache = false;
	// //缓存换算率、是否固定换算率
	// Hashtable m_hConvertIsfixed = new Hashtable();
	// 缓存保质期天数
	private Hashtable m_hValiddays = new Hashtable();
	// 到货当前行
	private int m_iArrCurrRow = 0;
	/* 记录:转单前用户显示数据缓存位置 */
	private int m_OldCardVOPos = 0;
	/* 记录:转单据前用户缓存数据长度 */
	private int m_OldVOsLen = 0;
	/* 批号参照 */
	private UIRefPane m_PnlLotRef = null;
	// 批量打印保存列表表头单据行号
	protected ArrayList listSelectBillsPos = null;
	/* 缓存推式保存VOs */
	private ArriveorderVO[] m_pushSaveVOs = null;
	// 单据状态:初始化；到货浏览；到货修改；到货列表；转入列表；转入修改；消息中心
	private String m_strState = "初始化";
	/* 支持转单后界面不清空显示定义类变量 */
	/* 记录:转单后缓存中所有到货单VO[],初始值为转单前用户浏览数据,用户保存成功一张单据,则本数组增加一张单据 */
	private ArriveorderVO[] m_VOsAll = null;
	private int nAssistUnitDecimal = 2;
	private int nConvertDecimal = 2;
	/** 关键字对应的计算公式类的常量 ( 参见 RelationsCal ) */
	private int[] nDescriptions = new int[] { RelationsCal.IS_FIXED_CONVERT,
			RelationsCal.CONVERT_RATE, RelationsCal.NUM_ASSIST,
			RelationsCal.NUM,
			// RelationsCal.NUM_QUALIFIED,
			// RelationsCal.NUM_UNQUALIFIED,
			RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.MONEY_ORIGINAL };
	// 取系统参数：
	private int nMeasDecimal = 2;
	// 调常用方法
	private int nNmoneyDecimal = 2;
	private int nPriceDecimal = 2;
	// 业务类型主键
	// private String pk_busitype = null;
	// 批量打印工具
	private ScmPrintTool printList = null;
	private ScmPrintTool printCard = null;
	// 业务类型(过滤订单、过滤到货单时均用到)
	private UIRefPane refBusi = null;
	/** 公式计算用到数组 */
	/** 调用计算公式关键字列表(驱动计算时也要包括驱动列关键字) */
	private String[] strKeys = new String[] { "Y", "convertrate", "nassistnum",
			"narrvnum",
			// "nelignum", "nnotelignum",
			"nprice", "nmoney" };
	// 缓存仓库是否进行货位管理
	// Hashtable m_hIsAllot = new Hashtable();
	// 是否已经缓存过仓库进行货位管理
	// boolean isFlagsCacheAllot = false;
	// 上层来源单据类型
	// private String upBillType = "21";
	// 记录被拆分删除行
	private Vector v_DeletedItems = new Vector();
	// 记录未被拆分的删除行
	private Vector vDelNoSplitted = new Vector();
	// 当前登录操作员有权限的公司[]
	private String[] saPkCorp = null;

	// 是否保留制单人
	private boolean m_bSaveMaker = true;
	// 消息中心单据ID
	private String m_strBillId = null;

	// 快速收货过程中出现异常标志
	private boolean m_bQuickException = false;

	/****************************/
	private Hashtable invidinfo;

	private ArrayList PilenoList;
	nc.vo.scm.ic.bill.FreeVO voFree;
	private String oid_cmangid = new String();

	/****************************/

	/**
	 * 获取是否快速收货过程中出现异常
	 */
	public boolean isQuickException() {
		return m_bQuickException;
	}

	/**
	 * 设置是否快速收货过程中出现异常
	 */
	public void setQuickExceptionFlag(boolean newVal) {
		m_bQuickException = newVal;
	}

	/**
	 * ArriveUI 构造子注解。
	 */
	public ArriveUI() {
		super();
		initialize();
	}

	/**
	 * ArriveUI 构造子注解。
	 */
	public ArriveUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();
		// setCauditid(billID);
		initialize();
		ArriveorderVO vo = null;
		try {
			vo = ArriveorderHelper.findByPrimaryKey(billID);
			if (vo != null) {
				setCacheVOs(new ArriveorderVO[] { vo });
				setDispIndex(0);
				loadDataToCard();
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * 处理点击自由项参照按钮事件 有无自由项管理由bodyRowChange()保证
	 * 如果没有自由项管理则在bodyRowChange()方法中隐藏掉自由项按钮 创建日期：(2001-10-20 11:25:46)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {

		// if (getArrBillCardPanel().getBodyItem("vfree0") != null) {
		// if (e.getSource() == ((FreeItemRefPane)
		// getArrBillCardPanel().getBodyItem("vfree0").getComponent()).getUIButton())
		// {
		// PuTool.actionPerformedFree(
		// getArrBillCardPanel(),
		// e,
		// new String[] { "cmangid", "cinventorycode", "cinventoryname",
		// "cinventoryspec", "cinventorytype" },
		// new String[] { "vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
		// "vfree5" });
		// }
		// }
	}

	/**
	 * 编辑后事件--表头库存组织
	 * 
	 * @param e
	 */
	private void afterEditWhenHeadStorOrg(BillEditEvent e) {
		int iSize = getBillCardPanel().getRowCount();
		if (iSize <= 0) {
			return;
		}
		for (int i = 0; i < iSize; i++) {
			getBillCardPanel().setBodyValueAt(null, i, "cwarehouseid");
			getBillCardPanel().setBodyValueAt(null, i, "cwarehousename");
			getBillCardPanel().getBillModel().setRowState(i,
					BillModel.MODIFICATION);
		}
	}

	/**
	 * @功能：编辑后事件 --> 某属性改变后触发的逻辑
	 */
	public void afterEdit(BillEditEvent e) {
		if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
			getBillCardPanel().getBillTable().editingStopped(
					new ChangeEvent(getBillCardPanel().getBillTable()));
		}
		BillModel bm = getBillCardPanel().getBillModel();
		if (e.getKey().equals("cstoreorganization")) {
			afterEditWhenHeadStorOrg(e);
		}
		// 单据行号
		else if (e.getKey().equals("crowno")) {
			BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e,
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE);
		} else if (
		// 数量
		(e.getKey().equals("convertrate") || e.getKey().equals("nassistnum")
				|| e.getKey().equals("narrvnum") || e.getKey().equals("nprice")
				|| e.getKey().equals("nmoney") || e.getKey().equals("nelignum") || e
				.getKey().equals("nnotelignum"))) {
			// 公式计算
			afterEditWhenNum(e);
			// 数量或辅数量为相反符号时的处理
			if (e.getKey().equals("narrvnum")
					|| e.getKey().equals("nassistnum"))
				afterSignChged(e);
			// 处理赠品
			afterEditWhenBodyLargessNums(e.getRow());
		} else if (e.getKey().equals("cassistunitname")) {
			// 辅计量
			afterEditWhenAssistUnit(e);
		} else if (e.getKey().equals("cinventorycode")) {
			// 存货
			afterEditWhenInv(e);
		} else if (e.getKey().equals("cemployeeid")) {
			// 采购员
			afterEditWhenHeadEmployee(e);
		} else if (e.getKey().equals("vproducenum")) {
			// 批号
			afterEditWhenProdNum(e);
		} else if (e.getKey().equals("dproducedate")) {
			// 生产日期
			afterEditWhenProdDate(e);
		} else if (e.getKey().equals("ivalidday")) {
			// 保质期天数
			afterEditWhenValidDays(e);
		} else if (e.getKey().equals("vmemo") && e.getPos() == 1) {
			if (getBillCardPanel().getBodyItem("vmemo") != null) {
				// 表体备注
				UIRefPane refBodyVmemo = (UIRefPane) getBillCardPanel()
						.getBodyItem("vmemo").getComponent();
				bm.setValueAt(refBodyVmemo.getUITextField().getText(),
						e.getRow(), "vmemo");
			}
		} else if (e.getKey().equals("vbackreasonb") && e.getPos() == 1) {
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				// 表体退货理由
				UIRefPane refBodyReason = (UIRefPane) getBillCardPanel()
						.getBodyItem("vbackreasonb").getComponent();
				bm.setValueAt(refBodyReason.getUITextField().getText(),
						e.getRow(), "vbackreasonb");
			}
		} else if (e.getKey().equals("vfree0")) {
			// 自由项
			afterEditFree(e);
		} else if (e.getKey().equals("cwarehousename")) {
			// 仓库
			try {
				afterEditWhenWareHouse(e);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getKey().equals("cproject")) {
			// 项目
			afterEditWhenProject(e);
			getSpace(e);
			// 来源单据行是赠品行，赠品标志不能更改
		} else if (e.getKey().equals("blargess")) {
			afterEditWhenBodyLargessNums(e.getRow());
		}
		// 自定义项PK处理
		if (e.getPos() == 1)
			setBodyDefPK(e);
		else
			setHeadDefPK(e);
		if (getParentCorpCode().equals("10395")) {
			if (e.getKey().equals("StampNo")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem(
						"vdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", "整垛数量不能为空!The entire stack Quantity can not be empty!");
					return;
				}
				int strIndex = sc.indexOf(".");
				int StampCount = strIndex >= 0 ? Integer.parseInt(sc.substring(
						0, strIndex)) : Integer.parseInt(sc);
				if (!String.valueOf(e.getValue()).equals("")) {
					DoSplitData(e.getValue().toString(), StampCount);
					getBillCardPanel().getHeadItem("StampNo").setValue("");
					for (int i = 0; i < getBillCardPanel().getBillModel()
							.getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt(
								String.valueOf((i + 1) * 10), i, "crowno");
					}
				}
				getBillCardPanel().getHeadItem("StampNo").getComponent()
						.requestFocusInWindow();
			}
		}

	}

	/**
	 * 数量相关字段及赠品标志字段编辑后事件处理
	 * 
	 * @param e
	 * @since v50
	 * @author czp
	 * @date 2006-10-09
	 */
	private void afterEditWhenBodyLargessNums(int iRow) {

		UFBoolean bLargessUpRow = (UFBoolean) PuPubVO.getUFBoolean_NullAs(
				getBillCardPanel().getBillModel().getValueAt(iRow,
						"blargessuprow"), UFBoolean.FALSE);
		if (bLargessUpRow.booleanValue()) {
			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),
					iRow, "blargess");
		}
		UFBoolean bLargess = (UFBoolean) PuPubVO.getUFBoolean_NullAs(
				getBillCardPanel().getBillModel().getValueAt(iRow, "blargess"),
				UFBoolean.FALSE);
		if (bLargess.booleanValue()) {
			Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
					"narrvnum");
			getBillCardPanel().getBillModel().setValueAt(oTemp, iRow,
					"npresentnum");
			oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
					"nassistnum");
			getBillCardPanel().getBillModel().setValueAt(oTemp, iRow,
					"npresentassistnum");
		} else {
			getBillCardPanel().getBillModel().setValueAt(null, iRow,
					"npresentnum");
			getBillCardPanel().getBillModel().setValueAt(null, iRow,
					"npresentassistnum");
		}
	}

	/**
	 * 自由项编辑事件 创建日期：(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditFree(BillEditEvent e) {
		if (!e.getKey().equals("vfree0") || e.getPos() != 1)
			return;
		if (getBillCardPanel().getBodyItem("vfree0") != null) {
			FreeVO freeVO = ((FreeItemRefPane) getBillCardPanel().getBodyItem(
					"vfree0").getComponent()).getFreeVO();
//			if (freeVO != null) {
			if (freeVO == null) {//edit by shikun 2014-11-14 自由项1编辑无效问题--河北。
				freeVO = voFree;
			}
			if (freeVO == null) {
				for (int i = 0; i < 5; i++) {
					String str = "vfree" + new Integer(i + 1).toString();
					getBillCardPanel().setBodyValueAt(null, e.getRow(), str);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					String strName = "vfreename"
							+ new Integer(i + 1).toString();
					if (freeVO.getAttributeValue(strName) != null) {
						String str = "vfree" + new Integer(i + 1).toString();
						Object ob = freeVO.getAttributeValue(str);
						getBillCardPanel().setBodyValueAt(ob, e.getRow(), str);
					}
				}
			}
		}
	}

	/**
	 * 辅计量编辑事件 处理： # 辅计量主键编辑时触发处理 ==>> 换算率 | # 选取的计量ID是主计量ID：变换率置为1，固定换算率 | #
	 * 由“换算率”驱动公式计算 > 步完成均处理合格数量是否可编辑 # 同步更新单据模板中的换算率和是否固定换算率属性 | # 更新换算率属性列可编辑性
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenAssistUnit(BillEditEvent e) {
		// 是否固定变化率改变时要同步更改：strKeys[0]的值
		UFBoolean isfixed = new UFBoolean(true);
		UFDouble convert = new UFDouble(0);
		// 存货ID
		String sBaseID = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cbaseid");
		// 辅计量主键
		String sCassId = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cassistunit");
		if (sCassId == null || sCassId.trim().equals("")) {
			UIRefPane refAss = (UIRefPane) getBillCardPanel().getBodyItem(
					"cassistunitname").getComponent();
			sCassId = refAss.getRefPK();
			String sCassName = refAss.getRefName();
			getBillCardPanel().getBillModel().setValueAt(sCassId, e.getRow(),
					"cassistunit");
			getBillCardPanel().getBillModel().setValueAt(sCassName, e.getRow(),
					"cassistunitname");
		}
		// 获取换算率
		convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// 是否固定换算率
		isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
		// 辅计量主键编辑
		if (e.getKey().equals("cassistunitname")) { // 设置辅计量参照
			setRefPaneAssistunit(e.getRow());
			// 设置辅信息
			setAssisUnitEditState2(e);
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			getBillCardPanel().getBillModel().setValueAt(convert, e.getRow(),
					"convertrate");
			isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
			if (isfixed.booleanValue())
				strKeys[0] = "Y";
			else
				strKeys[0] = "N";
			// 用换算率驱动计算：到货数量，辅数量，合格数量，不合格数量，单价，金额
			RelationsCal.calculate(e, getBillCardPanel(), "convertrate",
					nDescriptions, strKeys, ArriveorderItemVO.class.getName());
			// 合格数量可编辑性
			/*
			 * delete 2003-10-22 Object oarrvnum =
			 * getArrBillCardPanel().getBillModel().getValueAt(e.getRow(),
			 * "narrvnum"); if (oarrvnum == null ||
			 * oarrvnum.toString().trim().equals("") || (new
			 * UFDouble(oarrvnum.toString().trim())).compareTo(new UFDouble(0))
			 * >= 0) { getArrBillCardPanel().setCellEditable(e.getRow(),
			 * "nelignum", false); } else {
			 * getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum",
			 * true); }
			 */
		}
	}

	/**
	 * 表头编辑后事件-采购员
	 * 
	 * @param e
	 */
	private void afterEditWhenHeadEmployee(BillEditEvent e) {

		Logger.info("进入afterEditWhenHeadEmployee()");/* -=notranslate=- */

		String sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(
				"cemployeeid").getComponent()).getRefPK();

		Logger.info("sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(“cemployeeid”).getComponent()).getRefPK()="
				+ sPsnId);/* -=notranslate=- */

		if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {

			Logger.info("PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null ：true");/*
																					 * -=
																					 * notranslate
																					 * =
																					 * -
																					 */

			return;
		}
		// 由业务员带出默认部门
		UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem(
				"cemployeeid").getComponent());

		Logger.info("getBillCardPanel().getHeadItem(“cemployeeid”).getComponent()："
				+ ref.toString());/* -=notranslate=- */

		// 业务员所属部门
		Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");

		Logger.info("ref.getRefModel().getValue(“bd_psndoc.pk_deptdoc”)"
				+ sDeptId);/* -=notranslate=- */

		getBillCardPanel().getHeadItem("cdeptid").setValue(sDeptId);

		Logger.info("从方法afterEditWhenHeadEmployee()正常返回");/* -=notranslate=- */
	}

	/**
	 * 存货编辑事件 创建日期：(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenInv(BillEditEvent e) {
		// 改变存货时,清空自由项,辅数量,数量,单价,金额等到相关信息
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "narrvnum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nassistnum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmoney");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nelignum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nnotelignum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree0");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree1");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree2");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree3");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree4");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree5");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "dproducedate");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "ivalidday");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "dvaliddate");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "vproducenum");
		String[] aryAssistunit = new String[] {
				"cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
				"cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
		getBillCardPanel().getBillModel().execFormulas(e.getRow(),
				aryAssistunit);
		// 辅计量设置
		setAssisUnitEditState2(e);
		// 存货批次号管理处理
		String strCmangid = (String) getBillCardPanel().getBillModel()
				.getValueAt(e.getRow(), "cmangid");
		if (PuTool.isBatchManaged(strCmangid))
			getBillCardPanel().setCellEditable(e.getRow(), "vproducenum",
					getBillCardPanel().getBodyItem("vproducenum").isEdit());
		else
			getBillCardPanel()
					.setCellEditable(e.getRow(), "vproducenum", false);

	}

	/**
	 * 数量编辑事件 创建日期：(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenNum(BillEditEvent e) {
		if (e.getKey().equals("narrvnum")) {
			BillItem item = getBillCardPanel().getHeadItem("bisback");
			UFBoolean bBack = new UFBoolean(false);
			if (item != null)
				bBack = new UFBoolean(item.getValue());
			if (bBack.booleanValue()) {
				Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(),
						"narrvnum");
				if (oTemp != null) {
					UFDouble d = new UFDouble(oTemp.toString());
					if (d.doubleValue() > 0) {
						getBillCardPanel().setBodyValueAt(e.getOldValue(),
								e.getRow(), "narrvnum");
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000059")/* @res "错误" */,
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000275")/*
															 * @res "退货单数量必须为负!"
															 */);
						return;
					}
				}
			}
		}

		UFBoolean isfixed = new UFBoolean(true);
		// 存货ID
		String sBaseID = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cbaseid");
		// 辅计量主键
		String sCassId = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cassistunit");
		if (sCassId == null || sCassId.trim().equals("")) {
			UIRefPane refAss = (UIRefPane) getBillCardPanel().getBodyItem(
					"cassistunitname").getComponent();
			sCassId = refAss.getRefPK();
			String sCassName = refAss.getRefName();
			getBillCardPanel().getBillModel().setValueAt(sCassId, e.getRow(),
					"cassistunit");
			getBillCardPanel().getBillModel().setValueAt(sCassName, e.getRow(),
					"cassistunitname");
		}
		// 获取换算率
		// convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// 是否固定换算率
		isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
		// 自动计算：到货数量，辅数量，换算率，合格数量，不合格数量，单价，金额
		if ((e.getKey().equals("convertrate")
				|| e.getKey().equals("nassistnum")
				|| e.getKey().equals("narrvnum") || e.getKey().equals("nprice")
				|| e.getKey().equals("nmoney") || e.getKey().equals("nelignum") || e
				.getKey().equals("nnotelignum"))) {
			// 检查数据合法性，不合法恢复原值返回
			String strErr = getErrMsg(e);
			if (strErr != null) {
				MessageDialog.showErrorDlg(
						this,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000085")/* @res "数据错误" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000086")/* @res "输入数据错误：\n" */
								+ strErr);
				getBillCardPanel().getBillModel().setValueAt(e.getOldValue(),
						e.getRow(), e.getKey());
				return;
			}
			if (isfixed.booleanValue())
				strKeys[0] = "Y";
			else
				strKeys[0] = "N";
			RelationsCal.calculate(e, getBillCardPanel(), nDescriptions,
					strKeys, ArriveorderItemVO.class.getName());

			// 只有数量和辅数量编辑后才置正负属性
			if (e.getKey().equals("nassistnum")
					|| e.getKey().equals("narrvnum"))
				setEditAndDirect(e);
		}
	}

	/**
	 * 生产日期编辑事件 处理：生产日期 + 保质期天数 = 失效日期(不可编辑) 注：生产日期或保质期天数一方为空则失效日期为空
	 */
	private void afterEditWhenProdDate(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		/** 获取当前编辑表体行VO -- item ,注意：不能在初始化时由服务器端传递过来，因为可能是订单转入的单据 */
		ArriveorderItemVO item = (ArriveorderItemVO) bm.getBodyValueRowVO(
				e.getRow(), ArriveorderItemVO.class.getName());
		Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
		if (dproducedate == null || dproducedate.toString().trim().equals("")
				|| item.getIvalidday() == null
				|| item.getIvalidday().toString().trim().equals("")) {
			item.setDvaliddate(null);
			bm.setValueAt(null, e.getRow(), "dvaliddate");
		} else {
			UFDate dvaliddate = item.getDproducedate().getDateAfter(
					item.getIvalidday().intValue());
			// 失效日期(不可编辑)
			bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
			item.setDvaliddate(dvaliddate);
		}
	}

	/**
	 * 批号编辑事件
	 */
	private void afterEditWhenProdNum(BillEditEvent e) {
		try {
			if (m_PnlLotRef == null) {
				return;
			}
			BillModel bm = getBillCardPanel().getBillModel();
			Object vproducenum = bm.getValueAt(e.getRow(), "vproducenum");
			if (vproducenum == null || vproducenum.toString().trim().equals("")) {
				bm.setValueAt(null, e.getRow(), "dproducedate");
				bm.setValueAt(null, e.getRow(), "dvaliddate");
				return;
			}
			// UFDate dateValid = m_PnlLotRef.getRefInvalideDate();
			UFDate dateValid = new UFDate(System.currentTimeMillis());
			if (dateValid == null) {
				bm.setValueAt(null, e.getRow(), "dvaliddate");
				bm.setValueAt(null, e.getRow(), "dproducedate");
				return;
			}
			// Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
			Object ivalidday = bm.getValueAt(e.getRow(), "ivalidday");
			int iDays = 0;
			if (ivalidday == null || ivalidday.toString().trim().equals("")) {
				iDays = 0;
			} else {
				iDays = Integer.parseInt(ivalidday.toString().trim());
			}
			// 生产日期＝失效日期－保质期天数
			UFDate dateProduce = dateValid.getDateBefore(iDays);
			bm.setValueAt(dateProduce, e.getRow(), "dproducedate");
			bm.setValueAt(dateValid, e.getRow(), "dvaliddate");
		} catch (Exception ex) {
			SCMEnv.out("批号编辑后计算生产日期出现异常：详细信息如下：");
			reportException(ex);
		}
	}

	/**
	 * 项目编辑事件
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenProject(BillEditEvent e) {
		int row = e.getRow();
		// 项目
		Object o = getBillCardPanel().getBillModel().getValueAt(row,
				"cprojectid");
		if (o != null && o.toString().trim().length() > 0) {
			String cprojectid = o.toString();
			PuProjectPhaseRefModel refjobphase = new PuProjectPhaseRefModel(
					getCorpPrimaryKey(), cprojectid);
			((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase")
					.getComponent())).setIsCustomDefined(true);
			((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase")
					.getComponent())).setRefModel(refjobphase);
			// 设置项目阶段不可编辑
			getBillCardPanel().setCellEditable(row, "cprojectphase",
					getBillCardPanel().getBodyItem("cprojectphase").isEdit());
		} else {
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphase");
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphasebaseid");
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphaseid");
			// 设置项目阶段不可编辑
			getBillCardPanel().setCellEditable(row, "cprojectphase", false);
		}
	}

	/**
	 * 保质期天数编辑事件 处理：生产日期 + 保质期天数 = 失效日期(不可编辑) 注：生产日期或保质期天数一方为空则失效日期为空
	 */
	private void afterEditWhenValidDays(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		/** 获取当前编辑表体行VO -- item ,注意：不能在初始化时由服务器端传递过来，因为可能是订单转入的单据 */
		ArriveorderItemVO item = (ArriveorderItemVO) bm.getBodyValueRowVO(
				e.getRow(), ArriveorderItemVO.class.getName());
		// 保质期天数
		Object ivalidday = bm.getValueAt(e.getRow(), "ivalidday");
		if (item.getDproducedate() == null
				|| item.getDproducedate().toString().trim().equals("")
				|| ivalidday == null || ivalidday.toString().trim().equals("")) {
			item.setDvaliddate(null);
			bm.setValueAt(null, e.getRow(), "dvaliddate");
		} else {
			UFDate dvaliddate = item.getDproducedate().getDateAfter(
					item.getIvalidday().intValue());
			// 失效日期(不可编辑)
			bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
			item.setDvaliddate(dvaliddate);
		}
	}

	/**
	 * 仓库编辑事件 创建日期：(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * @throws BusinessException 
	 */
	private void afterEditWhenWareHouse(BillEditEvent e) throws BusinessException {
		// BillModel bm = getArrBillCardPanel().getBillModel();
		// //更新仓库是否货位管理缓存
		// String cwarehouseid = (String) bm.getValueAt(e.getRow(),
		// "cwarehouseid");
		// UFBoolean isAllot = null;
		// if (cwarehouseid != null && !cwarehouseid.trim().equals("")) {
		// //改变货位参照模型
		// UIRefPane refCargDoc =
		// (UIRefPane)
		// getArrBillCardPanel().getBodyItem("cstorename").getComponent();
		// refCargDoc.getRefModel().addWherePart(
		// "and bd_cargdoc.pk_stordoc = '"
		// + cwarehouseid
		// + "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' ");
		// if (m_hIsAllot == null)
		// m_hIsAllot = new Hashtable();
		// if (!m_hIsAllot.containsKey(cwarehouseid)) {
		// try {
		// ArrayList ary = ArriveorderBO_Client.getStorFlags(cwarehouseid);
		// m_hIsAllot.put(cwarehouseid, ary);
		// isAllot = (UFBoolean) ary.get(0);
		// //if (isAllot.booleanValue()) {
		// //m_btnAllotCarg.setEnabled(true);
		// //updateButton(m_btnAllotCarg);
		// //} else {
		// //m_btnAllotCarg.setEnabled(false);
		// //updateButton(m_btnAllotCarg);
		// //}
		// } catch (Exception exx) {
		// reportException(exx);
		// SCMEnv.out("afterEdit()");
		// }
		// } else {
		// isAllot = (UFBoolean) ((ArrayList)
		// m_hIsAllot.get(cwarehouseid)).get(0);
		// if (isAllot.booleanValue()) {
		// m_btnAllotCarg.setEnabled(true);
		// updateButton(m_btnAllotCarg);
		// } else {
		// m_btnAllotCarg.setEnabled(false);
		// updateButton(m_btnAllotCarg);
		// }
		// }
		// } else {
		// m_btnAllotCarg.setEnabled(false);
		// updateButton(m_btnAllotCarg);
		// }
		if (getParentCorpCode().equals("10395")) {
			
			for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {
				String cinventoryid = (String) getBillCardPanel()
						.getBodyValueAt(i, "cmangid");
				String cwarehouseid = (String) getBillCardPanel()
						.getBodyValueAt(i, "cwarehouseid");
				if(!Iscsflag(cwarehouseid))
				{
				   continue;	
				}
				if (cinventoryid == null || cinventoryid.equals("")) {
					return;
				}
				if (cwarehouseid == null || cwarehouseid.equals("")) {
					return;
				}
				String SQL = "select * from (select d.pk_cargdoc,d.csname from po_arriveorder a  ";
				SQL += "left join po_arriveorder_b b on a.carriveorderid=b.carriveorderid  ";
				SQL += "left join bd_cargdoc d on b.cstoreid=d.pk_cargdoc  ";
				SQL += " where b.cwarehouseid='" + cwarehouseid
						+ "'  and b.cmangid='" + cinventoryid + "'  ";
				SQL += "and d.pk_cargdoc is not null  and a.taudittime is not null and nvl(b.dr,0)=0 order by a.dreceivedate desc  ";
				SQL += ") where rownum=1  ";
				IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
						.getInstance().lookup(IUAPQueryBS.class.getName());
				List list = null;
				try {
					list = (List) sessionManager.executeQuery(SQL,
							new ArrayListProcessor());

					if (list.isEmpty()) {

						SQL = "select kp.cspaceid ,car.csname,car.cscode   ";
						SQL += "from   v_ic_onhandnum6 kp  ";
						SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
						SQL += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='"
								+ cwarehouseid
								+ "'  and  kp.cinventoryid='"
								+ cinventoryid + "'  ";
						SQL += "where rownum=1  ";
						list = (List) sessionManager.executeQuery(SQL,
								new ArrayListProcessor());
						if (list.isEmpty()) {
							return;
						}
					}

				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					ArrayList values = new ArrayList();
					Object obj = iterator.next();
					if (obj == null) {
						continue;
					}
					if (obj.getClass().isArray()) {
						int len = Array.getLength(obj);
						for (int j = 0; j < len; j++) {
							values.add(Array.get(obj, j));
						}
					}
					getBillCardPanel().setBodyValueAt((String) values.get(0),
							i, "cstoreid");
					getBillCardPanel().setBodyValueAt((String) values.get(1),
							i, "cstorename");
				}
			}
		}
	}

	/**
	 * 数量符号相反编辑事件 创建日期：(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterSignChged(BillEditEvent e) {
		UFDouble ufdOld = new UFDouble(e.getOldValue().toString().trim());
		UFDouble ufdNew = new UFDouble(e.getValue().toString().trim());
		if (ufdOld.multiply(ufdNew).doubleValue() < 0) {
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
			getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
		}
		return;
	}

	/**
	 * @功能：插入当前保存的转入到货单到记录所有单据的缓存中
	 */
	private void appArriveorderVOSaved(ArriveorderVO voSaved) {
		if (voSaved == null)
			return;
		/* 以下处理类变量 m_VOsAll */
		if (m_VOsAll == null) {
			m_VOsAll = new ArriveorderVO[] { voSaved };
			return;
		}
		ArriveorderVO[] saVOTmp = new ArriveorderVO[m_VOsAll.length + 1];
		for (int i = 0; i < m_VOsAll.length; i++) {
			saVOTmp[i] = m_VOsAll[i];
		}
		saVOTmp[saVOTmp.length - 1] = voSaved;
		m_VOsAll = saVOTmp;
	}

	/**
	 * 编辑前处理
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getKey().equals("vfree0")) {
			// 自由项处理
			boolean bCanEdit = PuTool.beforeEditInvBillBodyFree(
					getBillCardPanel(), e, new String[] { "cmangid",
							"cinventorycode", "cinventoryname",
							"cinventoryspec", "cinventorytype" }, new String[] {
							"vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
							"vfree5" });
			return bCanEdit;

		} else if (e.getKey().equalsIgnoreCase("vproducenum")) {
			// 批次号处理
			return beforeEditProdNum(e);
			// return true;
		} else if (e.getKey().equals("cprojectphase")) {
			Object oTmp = getBillCardPanel().getBillModel().getValueAt(
					e.getRow(), "cproject");
			if (oTmp == null || oTmp.toString().trim().equals(""))
				return false;
		}
		// 仓库
		else if (e.getKey().equals("cwarehousename")) {
			((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename")
					.getComponent()).setPk_corp(getCorpPrimaryKey());
			PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(),
					getCorpPrimaryKey(),
					getBillCardPanel().getHeadItem("cstoreorganization")
							.getValue(), "cwarehousename");
		}
		// 存货参照设置
		else if (e.getKey().equalsIgnoreCase("cinventorycode")) {
			return beforeEditInv(e);
		}
		// 项目阶段
		else if (e.getKey().equalsIgnoreCase("cprojectphase")) {
			return beforeEditProjectPhase(e);
		}
		//ncm begin liuydc
	    else if (e.getKey().equalsIgnoreCase("cstorename")) {
			String cwarehouseid = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "cwarehouseid");
			if (cwarehouseid == null)
				return false;
			UIRefPane pane = (UIRefPane) getBillCardPanel().getBodyItem(e.getKey()).getComponent();
			// 按照仓库过滤货位参照
			filterCargdocRefModel(pane, cwarehouseid);
		}
		//ncm end liuydc
		return true;
	}

	private boolean beforeEditInv(BillEditEvent e) {

		UFBoolean bLargessUpRow = PuPubVO.getUFBoolean_NullAs(
				getBillCardPanel().getBodyValueAt(e.getRow(), "blargessuprow"),
				UFBoolean.FALSE);
		UFBoolean bLargess = PuPubVO.getUFBoolean_NullAs(getBillCardPanel()
				.getBodyValueAt(e.getRow(), "blargess"), UFBoolean.FALSE);
		BillModel bm = getBillCardPanel().getBillModel();
		String strBillLinKey = getStateStr().equals("转入修改") ? "corder_bid"
				: "carriveorder_bid";
		if (bm.getValueAt(e.getRow(), strBillLinKey) == null) {
			SCMEnv.out("1-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
			return false;
		}
		if (m_hBillIDsForCmangids == null) {
			SCMEnv.out("2-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
			return false;
		}
		String cmangid = (String) m_hBillIDsForCmangids.get(bm.getValueAt(
				e.getRow(), strBillLinKey));
		if (cmangid == null || cmangid.trim().equals("")) {
			SCMEnv.out("3-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
			return false;
		}
		InvRefModelForRepl refmodel = null;
		if (!bLargessUpRow.booleanValue() && bLargess.booleanValue()) {
			// 订单行不是赠品且到货单行是赠品存货参照内容存货参照取所有（含订单存货＋替换件）
			refmodel = new InvRefModelForRepl(cmangid, ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), true);
		} else {
			// 存货参照内容为订单存货＋替换件
			refmodel = new InvRefModelForRepl(cmangid, ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), false);
		}
		UIRefPane refCinventorycode = (UIRefPane) getBillCardPanel()
				.getBodyItem("cinventorycode").getComponent();
		refCinventorycode.setIsCustomDefined(true);
		refCinventorycode.setRefType(IBusiType.GRID);
		refCinventorycode.setRefModel(refmodel);
		return true;
	}

	/**
	 * 功能：编辑批次号前对批次号参照处理 参数： 返回： 例外： 日期：(2002-9-16 13:01:38) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private boolean beforeEditProdNum(BillEditEvent e) {

		int iRow = e.getRow();
		ParaVOForBatch vo = new ParaVOForBatch();
		// 传入FieldName
		vo.setMangIdField("cmangid");
		vo.setInvCodeField("cinventorycode");
		vo.setInvNameField("cinventoryname");
		vo.setSpecificationField("cinventoryspec");
		vo.setInvTypeField("cinventorytype");
		vo.setMainMeasureNameField("cmainmeasname");
		vo.setAssistUnitIDField("cassistunit");
		vo.setIsAstMg(new UFBoolean(PuTool
				.isAssUnitManaged((String) getBillCardPanel().getBodyValueAt(
						iRow, "cbaseid"))));
		vo.setWarehouseIDField("cwarehouseid");
		vo.setFreePrefix("vfree");
		// 设置卡片模板,公司等
		vo.setCardPanel(getBillCardPanel());
		vo.setPk_corp(getCorpPrimaryKey());
		vo.setEvent(e);
		try {
			m_PnlLotRef = nc.ui.pu.pub.PuTool.beforeEditWhenBodyBatch(vo);
		} catch (Exception exp) {
			PuTool.outException(this, exp);
		}
		BillModel bm = getBillCardPanel().getBillModel();
		String cmangid = (String) bm.getValueAt(e.getRow(), "cmangid");
		if (m_PnlLotRef == null || !PuTool.isBatchManaged(cmangid)) {
			return false;
		}
		return true;
	}

	/**
	 * 到货修改行变换时特殊控制(行变换事件)
	 */
	private void bmrcSetForModify(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		Object obj = bm.getValueAt(e.getRow(), "naccumchecknum");
		Object objElg = bm.getValueAt(e.getRow(), "nelignum");
		Object objNotElg = bm.getValueAt(e.getRow(), "nnotelignum");
		/*
		 * 不可编辑逻辑(满足其一)：1)、 质检数量 != 0 并且 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负2)、 合格数量
		 * != 0 并且 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负3)、不合格数量 != 0 并且
		 * 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负
		 */
		if ((obj != null
				&& !obj.toString().trim().equals("")
				&& !(new UFDouble(obj.toString().trim())
						.compareTo(new UFDouble(0)) == 0)
				|| objElg != null
				&& !objElg.toString().trim().equals("")
				&& !(new UFDouble(objElg.toString().trim())
						.compareTo(new UFDouble(0)) == 0) || objNotElg != null
				&& !objNotElg.toString().trim().equals("")
				&& !(new UFDouble(objNotElg.toString().trim())
						.compareTo(new UFDouble(0)) == 0))
				&& bm.getRowState(e.getRow()) != BillModel.MODIFICATION
				&& bm.getRowState(e.getRow()) != BillModel.ADD
				&& !isCheckFree(e)
				&& !((objElg != null && new UFDouble(objElg.toString().trim())
						.compareTo(new UFDouble(0)) < 0) || (objNotElg != null && new UFDouble(
						objNotElg.toString().trim()).compareTo(new UFDouble(0)) < 0))) {
			// 按钮逻辑
			// m_btnDelLine.setEnabled(false);
			setBtnLines(false);
			// updateButton(m_btnDelLine);
			// m_btnAllotCarg.setEnabled(false);
			// updateButton(m_btnAllotCarg);
			// 所有可编辑的项置为不可编辑
			getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode",
					false);
			getBillCardPanel()
					.setCellEditable(e.getRow(), "convertrate", false);
			getBillCardPanel().setCellEditable(e.getRow(), "cassistunitname",
					false);
			getBillCardPanel().setCellEditable(e.getRow(), "nassistnum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "narrvnum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "nprice", false);
			getBillCardPanel().setCellEditable(e.getRow(), "nmoney", false);
			getBillCardPanel().setCellEditable(e.getRow(), "nelignum", false);
			getBillCardPanel()
					.setCellEditable(e.getRow(), "npresentnum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "nwastnum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "cwarehousename",
					false);
			getBillCardPanel().setCellEditable(e.getRow(), "cstorename", false);
			getBillCardPanel()
					.setCellEditable(e.getRow(), "vproducenum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "dproducedate",
					false);
			getBillCardPanel().setCellEditable(e.getRow(), "ivalidday", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vmemo", false);
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
				getBillCardPanel().setCellEditable(e.getRow(), "vbackreasonb",
						false);
			getBillCardPanel().setCellEditable(e.getRow(), "vfree0", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef1", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef2", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef3", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef4", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef5", false);
			getBillCardPanel().setCellEditable(e.getRow(), "vdef6", false);
			getBillCardPanel().setCellEditable(e.getRow(), "cproject", false);
			getBillCardPanel().setCellEditable(e.getRow(), "cprojectphase",
					false);
		} else {
			// 没有报过检按钮逻辑
			// m_btnDelLine.setEnabled(true);
			setBtnLines(true);
			// updateButton(m_btnDelLine);
		}
	}

	/**
	 * 功能：设置项目阶段参照
	 */
	private boolean beforeEditProjectPhase(BillEditEvent e) {
		int row = e.getRow();
		if (row < 0) {
			return false;
		}
		getBillCardPanel().stopEditing();
		// 项目
		Object o = getBillCardPanel().getBillModel().getValueAt(row,
				"cprojectid");
		Object pk_corp = getBillCardPanel().getBillModel().getValueAt(row,
				"pk_corp");
		String cprojectid = null;
		// 项目阶段参照
		if ((o != null) && (!o.toString().trim().equals(""))) {
			cprojectid = o.toString().trim();
			PuProjectPhaseRefModel refjobphase = new PuProjectPhaseRefModel(
					pk_corp.toString(), cprojectid);
			((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase")
					.getComponent())).setIsCustomDefined(true);
			((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase")
					.getComponent())).setRefModel(refjobphase);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 仓库是否进行货位管理(行变换事件)
	 */
	private void bmrcSetForWareAlot(BillEditEvent e) {
		/*
		 * BillModel bm = getArrBillCardPanel().getBillModel(); String
		 * cwarehouseid = (String) bm.getValueAt(e.getRow(), "cwarehouseid"); if
		 * (cwarehouseid == null || cwarehouseid.trim().equals("")) {
		 * m_btnAllotCarg.setEnabled(false); updateButton(m_btnAllotCarg); }
		 * else { ArrayList ary1 = (ArrayList) m_hIsAllot.get(cwarehouseid);
		 * UFBoolean isAllot = (UFBoolean) ary1.get(0); if
		 * (isAllot.booleanValue()) { m_btnAllotCarg.setEnabled(true);
		 * updateButton(m_btnAllotCarg); } else {
		 * m_btnAllotCarg.setEnabled(false); updateButton(m_btnAllotCarg); }
		 * //过滤货位 UIRefPane refCarg = (UIRefPane)
		 * getArrBillCardPanel().getBodyItem("cstorename").getComponent();
		 * refCarg.getRefModel().addWherePart( "and bd_cargdoc.pk_stordoc = '" +
		 * cwarehouseid + "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' "); }
		 */
	}

	/**
	 * 功能：行改变 1.到货列表 2.转入列表 3.到货修改及转入修改
	 */
	public void bodyRowChange(BillEditEvent e) {
		if (getStateStr().equals("到货列表")) {
			bodyRowChangeLookList(e);
		} else if (getStateStr().equals("转入修改") || getStateStr().equals("到货修改")) {
			bodyRowChangeEdit(e);
		}
	}

	/**
	 * 功能：单据行改变时的处理(到货修改及转入修改)
	 */
	private void bodyRowChangeEdit(BillEditEvent e) {
		/** 设置合格数量编辑及各数量的正负属性 */
		setEditAndDirect(e);
		/** 是否显示自由项按钮(是否自由项管理) */
		// bmrcSetForFree(e); V31 czp del 统一在编辑前处理
		/** 设置辅计量信息 */
		setAssisUnitEditState2(e);
		/** 存货替换参照设置 */
		// bmrcSetForInvRef(e);V5移动到编辑前事件
		/** 仓库：是否进行货位管理 */
		bmrcSetForWareAlot(e);
		/** 到货修改时特殊控制 */
		if (getStateStr().equals("到货修改")) {
			bmrcSetForModify(e);
		}
		/** 存货是否批次号管理 */
		// bmrcSetForProdNum(e);V5移动到编辑前事件
		/** 项目参照设置 */
		// bmrcSetForRefPaneProject(e);V5移动到编辑前事件

		// 浮动菜单右键功能权限控制
		rightButtonRightControl();
	}

	/**
	 * 浏览列表行变换时处理 创建日期：(2001-11-18 15:25:26)
	 */
	private void bodyRowChangeLookList(BillEditEvent e) {
		// 选中行逻辑
		int iLen = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iLen; i++) {
			if (getBillListPanel().getHeadTable().isRowSelected(i)) {
				getBillListPanel().getHeadBillModel().setRowState(i,
						BillModel.SELECTED);
			} else {
				getBillListPanel().getHeadBillModel().setRowState(i,
						BillModel.NORMAL);
			}
		}
	}

	/**
	 * @功能：检验修改数据的合法性
	 * @作者：晁志平 创建日期：(2001-6-20 14:47:40)
	 * @return boolean
	 * @param newvo
	 *            nc.vo.rc.receive.ArriveorderVO
	 * @param oldvo
	 *            nc.vo.rc.receive.ArriveorderVO
	 */
	private boolean checkModifyData(ArriveorderVO newvo, ArriveorderVO oldvo) {
		try {
			/** 检查存货辅计量管理时辅信息为必输项 */
			BillModel bm = getBillCardPanel().getBillModel();
			Hashtable hErr = new Hashtable();
			String strInvBasId = null, strErr = null;
			Object objAssUnit = null, objAssNum = null, objExhRate = null;
			if (bm != null) {
				for (int i = 0; i < bm.getRowCount(); i++) {
					strErr = "";
					strInvBasId = (String) bm.getValueAt(i, "cbaseid");
					if (PuTool.isAssUnitManaged(strInvBasId)) {
						objAssUnit = bm.getValueAt(i, "cassistunitname");
						objAssNum = bm.getValueAt(i, "nassistnum");
						objExhRate = bm.getValueAt(i, "convertrate");
						if (objAssUnit == null
								|| objAssUnit.toString().trim().equals(""))
							strErr += m_lanResTool.getStrByID("common",
									"UC000-0003938")/* @res "辅单位" */;
						if (objAssNum == null
								|| objAssNum.toString().trim().equals("")) {
							if (strErr.length() > 0)
								strErr += m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000000")/* @res "、" */;
							strErr += m_lanResTool.getStrByID("common",
									"UC000-0003971")/* @res "辅数量" */;
						}
						if (objExhRate == null
								|| objExhRate.toString().trim().equals("")) {
							if (strErr.length() > 0)
								strErr += m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000000")/* @res "、" */;
							strErr += m_lanResTool.getStrByID("common",
									"UC000-0002161")/* @res "换算率" */;
						}
						if (strErr.trim().length() > 0)
							hErr.put(new Integer(i + 1), strErr);
					}
				}
			}
			if (hErr.size() > 0) {
				Vector vTmp = new Vector();
				Enumeration keys = hErr.keys();
				Integer iKey = null;
				strErr = "";
				strErr += m_lanResTool.getStrByID("40040301",
						"UPP40040301-000100")/* @res "有辅计量管理的存货出现空项：\n" */;
				while (keys.hasMoreElements()) {
					iKey = (Integer) keys.nextElement();
					vTmp.addElement(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000101")/* @res "表体 " */
							+ iKey + ": " + hErr.get(iKey) + "\n");
				}
				for (int i = vTmp.size() - 1; i >= 0; i--) {
					strErr += vTmp.elementAt(i);
				}
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
						strErr);
				return false;
			}
			ArriveorderHeaderVO head = (ArriveorderHeaderVO) newvo
					.getParentVO();
			// 到货日期
			String arrdate = null;
			if (!(head.getAttributeValue("dreceivedate") == null || head
					.getAttributeValue("dreceivedate").toString().trim()
					.equals(""))) {
				arrdate = head.getAttributeValue("dreceivedate").toString();
			}
			// 供应商
			String vendor = (String) head.getAttributeValue("cvendormangid");
			// 部门
			String dept = (String) head.getAttributeValue("cdeptid");
			// 业务员
			String empl = (String) head.getAttributeValue("cemployeeid");
			// 业务类型
			String busi = (String) head.getAttributeValue("cbiztype");
			// 库存组织
			String sStoreOrgId = head.getCstoreorganization();

			if (arrdate == null || arrdate.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000102")/* @res "到货日期不能为空" */);
				return false;
			} else if (vendor == null || vendor.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000103")/* @res "供应商不能为空" */);
				return false;
			} else if (dept == null || dept.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000104")/* @res "部门不能为空" */);
				return false;
			} else if (empl == null || empl.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000105")/* @res "业务员不能为空" */);
				return false;
			} else if (busi == null || busi.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000106")/* @res "业务类型不能为空" */);
				return false;
			} else if (sStoreOrgId == null || sStoreOrgId.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000107")/* @res "库存组织不能为空" */);
				return false;
			}

			// 处理表体有空存货的行
			String lines = "";
			int line = 0;
			for (line = 0; line < getBillCardPanel().getBillModel()
					.getRowCount(); line++) {
				if (getBillCardPanel().getBillModel().getValueAt(line,
						"cinventorycode") == null
						|| getBillCardPanel().getBillModel()
								.getValueAt(line, "cinventorycode").toString()
								.trim().equals("")) {
					if (!lines.trim().equals("")) {
						lines += m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000000")/* @res "、" */;
					}
					lines += line + 1;
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000108")/*
															 * @res
															 * "行存货编码不能为空，请输入存货编码"
															 */);
				return false;
			}
			// 处理到货数量为零的行
			lines = "";
			line = 0;
			for (line = 0; line < getBillCardPanel().getBillModel()
					.getRowCount(); line++) {
				if (getBillCardPanel().getBillModel().getValueAt(line,
						"narrvnum") == null
						|| getBillCardPanel().getBillModel()
								.getValueAt(line, "narrvnum").toString().trim()
								.equals("")
						|| ((UFDouble) getBillCardPanel().getBillModel()
								.getValueAt(line, "narrvnum")).doubleValue() == 0) {
					if (!lines.trim().equals("")) {
						lines += m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000000")/* @res "、" */;
					}
					lines += line + 1;
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000087")/*
															 * @res
															 * "行存货到货数量不能为零，也不能为空，请输入到货数量"
															 */);
				return false;
			}
			// 处理到货单价为负的行
			lines = "";
			line = 0;
			for (line = 0; line < getBillCardPanel().getBillModel()
					.getRowCount(); line++) {
				if (!(getBillCardPanel().getBillModel().getValueAt(line,
						"nprice") == null
						|| getBillCardPanel().getBillModel()
								.getValueAt(line, "nprice").toString().trim()
								.equals("") || ((UFDouble) getBillCardPanel()
						.getBillModel().getValueAt(line, "nprice"))
						.doubleValue() == 0)) {
					// 非空非零时如果为负
					if (((UFDouble) getBillCardPanel().getBillModel()
							.getValueAt(line, "nprice")).doubleValue() < 0) {
						if (!lines.trim().equals("")) {
							lines += m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000000")/* @res "、" */;
						}
						lines += line + 1;
					}
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000109")/*
															 * @res
															 * "行存货到货价格小于零，请重新输入到货价格"
															 */);
				return false;
			}
			// //|赠品数量|<=|到货数量|
			// UFDouble givenum = null;
			// UFDouble arrnum = null;
			// lines = "";
			// line = 0;
			// for (line = 0;
			// line < getArrBillCardPanel().getBillModel().getRowCount();
			// line++) {
			// if ((getArrBillCardPanel().getBillModel().getValueAt(line,
			// "npresentnum")
			// == null
			// || getArrBillCardPanel()
			// .getBillModel()
			// .getValueAt(line, "npresentnum")
			// .toString()
			// .trim()
			// .equals("")
			// || ((UFDouble) getArrBillCardPanel()
			// .getBillModel()
			// .getValueAt(line, "npresentnum"))
			// .doubleValue()
			// == 0)) {
			// givenum = new UFDouble(0);
			// } else {
			// givenum =
			// (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line,
			// "npresentnum");
			// }
			// if (getArrBillCardPanel().getBillModel().getValueAt(line,
			// "narrvnum") == null
			// || getArrBillCardPanel()
			// .getBillModel()
			// .getValueAt(line, "narrvnum")
			// .toString()
			// .trim()
			// .equals("")
			// || ((UFDouble)
			// getArrBillCardPanel().getBillModel().getValueAt(line,
			// "narrvnum"))
			// .doubleValue()
			// == 0) {
			// arrnum = new UFDouble(0);
			// } else {
			// arrnum =
			// (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line,
			// "narrvnum");
			// }
			// givenum = new UFDouble(Math.abs(givenum.doubleValue()));
			// arrnum = new UFDouble(Math.abs(arrnum.doubleValue()));
			// if (arrnum.compareTo(givenum) < 0) {
			// if (!lines.trim().equals("")) {
			// lines += "、";
			// }
			// lines += line + 1;
			// }
			// }
			// if (!lines.trim().equals("")) {
			// MessageDialog.showWarningDlg(
			// this,
			// "提示",
			// "到货数量包含赠品数量，"
			// + CommonConstant.BEGIN_MARK
			// + lines
			// + CommonConstant.END_MARK
			// + "行存货赠品数量大于到货数量不合理，请重新输入到货数量或赠品数量");
			// return false;
			// }
		} catch (Exception e) {
			reportException(e);
			return false;
		}
		return true;
	}

	/**
	 * @功能：删除当前作废的到货单(卡片)
	 * @作者：晁志平 创建日期：(2001-10-08 20:16:16)
	 */
	private void delArriveorderVODiscarded() {
		ArriveorderVO[] arrives = null;
		Vector v = new Vector();
		int delIndex = 0;
		try {
			for (int i = 0; i < getCacheVOs().length; i++) {
				if (i == getDispIndex()) {
					delIndex = i;
				}
				v.add(i, getCacheVOs()[i]);
			}
			v.remove(delIndex);

			if (v.size() > 0) {
				arrives = new ArriveorderVO[v.size()];
				v.copyInto(arrives);
				setCacheVOs(arrives);
			} else {
				setCacheVOs(null);
			}
			// 转入修改或修改两面种状态在保存到货单后重置显示位置
			if (getDispIndex() > 0) {
				setDispIndex(getDispIndex() - 1);
			} else {
				setDispIndex(0);
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * @功能：删除当前保存的转入到货单(卡片)
	 * @作者：晁志平 创建日期：(2001-7-31 20:11:16)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void delArriveorderVOSaved() {
		ArriveorderVO[] arrives = null;
		Vector v = new Vector();
		int delIndex = 0;
		try {
			for (int i = 0; i < getCacheVOs().length; i++) {
				if (i == getDispIndex()) {
					delIndex = i;
				}
				v.add(i, getCacheVOs()[i]);
			}
			v.remove(delIndex);

			if (v.size() > 0) {
				arrives = new ArriveorderVO[v.size()];
				v.copyInto(arrives);
				setCacheVOs(arrives);
			} else {
				setCacheVOs(null);
			}
			// 转入修改或修改两面种状态在保存到货单后重置显示位置
			if (getDispIndex() > 0) {
				setDispIndex(getDispIndex() - 1);
			} else {
				setDispIndex(0);
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * @功能：删除当前作废的到货单(列表)
	 * @作者：晁志平 创建日期：(2001-10-09 20:10:16)
	 */
	private void delArriveorderVOsDiscarded(Vector v_removed) {
		ArriveorderVO[] arrives = null;
		Vector v_all = new Vector();
		try {
			for (int i = 0; i < getCacheVOs().length; i++) {
				v_all.add(i, getCacheVOs()[i]);
			}
			for (int i = 0; i < v_removed.size(); i++) {
				v_all.remove(v_removed.elementAt(i));
			}
			if (v_all.size() > 0) {
				arrives = new ArriveorderVO[v_all.size()];
				v_all.copyInto(arrives);
				setCacheVOs(arrives);
			} else {
				setCacheVOs(null);
			}

		} catch (Exception e) {
			reportException(e);
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000059")/* @res "错误" */, m_lanResTool
							.getStrByID("40040301", "UPP40040301-000110")/*
																		 * @res
																		 * "刷新显示前端缓存时出错"
																		 */);
		}
	}

	/**
	 * @功能：切换到列表界面(维护修改=>>列表)
	 * @作者：晁志平 创建日期：(2001-6-20 9:11:08)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void displayArrBillListPanel() {

		// 设置界面状态
		setM_strState("到货列表");

		// 显示数据到列表界面
		loadDataToList();

		// 显示列表界面
		if (!m_bLoaded) {
			add(getBillListPanel(), "Center");
			m_bLoaded = true;
		}
		getBillCardPanel().setVisible(false);
		getBillListPanel().setVisible(true);

		// 默认显示第一张
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					getDispIndex(), getDispIndex());
			getBillListPanel().getHeadBillModel().setRowState(getDispIndex(),
					BillModel.SELECTED);
			setListBodyData(getDispIndex());
		}
		setButtonsState();
		updateUI();
	}

	/**
	 * @功能：切换到列表界面(转入修改=>>列表)
	 */
	private void displayArrBillListPanelNew() {

		// 设置界面状态
		setM_strState("转入列表");
		setButtonsState();
		// 显示数据到列表界面
		loadDataToList();
		// 显示列表界面
		if (!m_bLoaded) {
			add(getBillListPanel(), "Center");
			m_bLoaded = true;
		}
		getBillListPanel().setVisible(true);
		getBillCardPanel().setVisible(false);
		// 列表状态默认显示处理
		setDispIndex(0);
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					getDispIndex(), getDispIndex());
			getBillListPanel().getHeadBillModel().setRowState(getDispIndex(),
					BillModel.SELECTED);
			setListBodyData(getDispIndex());
		}
		setButtonsListValueChangedNew(1);
		updateUI();
	}

	/**
	 * 作者：汪维敏 功能：此处插入方法说明 参数： 返回： 例外： 日期：(2004-3-16 15:28:25)
	 */
	public String getAccYear() {
		return getClientEnvironment().getAccountYear();
	}

	/**
	 * 功能描述:获得到货单卡片 初始化处理： 1.表体自由项 2.表头备注设定： (1).不自动检返回值查 (2).返回值设为名称 3.表头备注设定：
	 * (1).不自动检返回值查 (2).返回值设为名称 4.换算率>=0 5.单价>=0 6.收货仓库参照过滤掉废品库 7.货位非封存
	 * 8.单据模板监听器 9.精度处理 2002-08-07 wyf 修改采购部门及业务员的数据库类型不匹配问题 DB2 2002-08-08 wyf
	 * 修改一个SQL错误
	 */
	private BillCardPanel getBillCardPanel() {
		if (m_arrBillPanel == null) {
			try {
				m_arrBillPanel = new BillCardPanel();
				try {
					m_arrBillPanel.loadTemplet(ScmConst.PO_Arrive, null,
							getOperatorId(), getCorpPrimaryKey(),
							new MyBillData());
				} catch (Exception ex) {
					reportException(ex);
					m_arrBillPanel.loadTemplet("40040301010000000000");
				}
				// 单据模板中表体活动功能菜单初始化
				m_arrBillPanel.setBodyMenuShow(true);
				UIMenuItem[] miBody = m_arrBillPanel.getBodyMenuItems();
				if (miBody != null && miBody.length >= 3) {
					miBody[0].setVisible(false);
					miBody[2].setVisible(false);
				}
				m_arrBillPanel.addBodyMenuListener(this);
				// 设置千分位
				m_arrBillPanel.setBodyShowThMark(true);
				// 质量等级
				m_arrBillPanel.hideBodyTableCol("squalitylevelname");
				// 建议处理意见
				m_arrBillPanel.hideBodyTableCol("cdealname");
				// 行号的设置
				if (m_arrBillPanel.getBodyItem("crowno") != null) {
					BillRowNo.loadRowNoItem(m_arrBillPanel, "crowno");
				}
				// 处理自定义项
				nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
						m_arrBillPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Arrive, // 单据类型
						"vdef", "vdef");
				// 加合计行
				m_arrBillPanel.setTatolRowShow(true);
				// 单据模板编辑监听器
				m_arrBillPanel.addEditListener(this);
				// 编辑前监听
				m_arrBillPanel.addBodyEditListener2(this);
				// 表体排序监听
				m_arrBillPanel.getBodyPanel().addTableSortListener();
				m_arrBillPanel.getBillModel().setRowSort(true);
				// 增加行号排序监听
				m_arrBillPanel.getBillModel().setSortPrepareListener(this);
			} catch (java.lang.Throwable e) {
				SCMEnv.out("初始化单据模板(卡片)时出现异常：");
				SCMEnv.out(e);
			}
		}
		return m_arrBillPanel;
	}

	/**
	 * 获得到货单列表
	 * 
	 */
	private BillListPanel getBillListPanel() {
		if (m_arrListPanel == null) {
			try {
				m_arrListPanel = new BillListPanel();
				// 加载模板
				try {
					m_arrListPanel.loadTemplet(ScmConst.PO_Arrive, null,
							getOperatorId(), getCorpPrimaryKey());
				} catch (Exception ex) {
					reportException(ex);
					m_arrListPanel.loadTemplet("40040301010000000000");
				}
				// 设置千分位
				m_arrListPanel.getParentListPanel().setShowThMark(true);
				m_arrListPanel.getChildListPanel().setShowThMark(true);

				// 初始化列表精度
				initListDecimal();

				if (m_arrListPanel.getHeadItem("cbiztype") != null)
					m_arrListPanel.hideHeadTableCol("cbiztype");
				if (m_arrListPanel.getHeadItem("cvendormangid") != null)
					m_arrListPanel.hideHeadTableCol("cvendormangid");
				if (m_arrListPanel.getHeadItem("cemployeeid") != null)
					m_arrListPanel.hideHeadTableCol("cemployeeid");
				if (m_arrListPanel.getHeadItem("cdeptid") != null)
					m_arrListPanel.hideHeadTableCol("cdeptid");
				if (m_arrListPanel.getHeadItem("ctransmodeid") != null)
					m_arrListPanel.hideHeadTableCol("ctransmodeid");
				if (m_arrListPanel.getHeadItem("creceivepsn") != null)
					m_arrListPanel.hideHeadTableCol("creceivepsn");
				if (m_arrListPanel.getHeadItem("cstoreorganization") != null)
					m_arrListPanel.hideHeadTableCol("cstoreorganization");
				if (m_arrListPanel.getHeadItem("coperator") != null)
					m_arrListPanel.hideHeadTableCol("coperator");
				if (m_arrListPanel.getHeadItem("cauditpsn") != null)
					m_arrListPanel.hideHeadTableCol("cauditpsn");

				// 质量等级
				if (m_arrListPanel.getBodyItem("squalitylevelname") != null)
					m_arrListPanel.hideBodyTableCol("squalitylevelname");
				// 建议处理意见
				if (m_arrListPanel.getBodyItem("cdealname") != null)
					m_arrListPanel.hideBodyTableCol("cdealname");

				// 处理列表自定义项
				nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(
						m_arrListPanel, getCorpPrimaryKey(),
						ScmConst.PO_Arrive, // 单据类型
						"vdef", "vdef");
				// 设置列表合计
				m_arrListPanel.getChildListPanel().setTotalRowShow(true);

				// 单据编辑监听
				m_arrListPanel.addEditListener(this);
				m_arrListPanel.addMouseListener(this);
				// 多选监听
				m_arrListPanel.getHeadTable().setCellSelectionEnabled(false);
				m_arrListPanel
						.getHeadTable()
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				m_arrListPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);
				// 行号排序监听
				m_arrListPanel.getBodyBillModel().setSortPrepareListener(this);
				// 列表其它字段排序监听
				m_arrListPanel.getHeadBillModel().addSortRelaObjectListener2(
						this);

			} catch (Exception e) {
				SCMEnv.out("初始化单据模板(列表)时出现异常：");
				reportException(e);
			}
		}
		return m_arrListPanel;
	}

	/**
	 * @功能：返回到货单VO数组的头VO数组
	 * @作者：Administrator 创建日期：(2001-6-8 21:53:25)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.vo.pp.ask.AskbillHeaderVO[]
	 * @param askbillvos
	 *            nc.vo.pp.ask.AskbillVO[]
	 */
	private ArriveorderHeaderVO[] getArriveHeaderVOs(ArriveorderVO[] arrivevos) {
		ArriveorderHeaderVO[] headers = null;
		if (arrivevos != null) {
			headers = new ArriveorderHeaderVO[arrivevos.length];
			for (int i = 0; i < arrivevos.length; i++) {
				headers[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
			}
		}
		return headers;
	}

	/**
	 * @功能：查询并设置到货单缓存VO
	 */
	private void getArriveVOsFromDB() {
		try {
			/* 用户自定义条件 */
			ConditionVO[] condsUserDef = getQueryConditionDlg()
					.getConditionVO();

			/* 用户常用条件 */
			ConditionVO[] condsNormal = getQueryConditionDlg()
					.getNormalCondsVO();
			/* 来源单据信息条件 */
			String strUpSrcSQlPart = getQueryConditionDlg().getUpSrcPnl()
					.getSubSQL();
			/* 查询数据库 */
			setCacheVOs(ArriveorderHelper.queryAllArriveMy(condsUserDef,
					condsNormal, getCorpPrimaryKey(), getBusitype(),
					strUpSrcSQlPart));
			/* 没有查询到数据时的处理 */
			if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
				MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000111")/* @res "没有符合条件的记录！" */);
				if (getBillCardPanel().getBillData() != null) {
					getBillCardPanel().getBillData().clearViewData();
				}
				if (getBillListPanel().getHeadBillModel() != null) {
					getBillListPanel().getHeadBillModel().clearBodyData();
				}
				if (getBillListPanel().getBodyBillModel() != null) {
					getBillListPanel().getBodyBillModel().clearBodyData();
				}
				updateUI();
			}
			/* 把查询到数据缓存{处理删除表体,否则会影响可用量} */
			else {
				for (int i = 0; i < getCacheVOs().length; i++) {
					if (getCacheVOs()[i].getChildrenVO() != null
							&& getCacheVOs()[i].getChildrenVO().length > 0) {
						// 给单据赋来源属性
						String cupsourcebilltype = ((ArriveorderItemVO[]) getCacheVOs()[i]
								.getChildrenVO())[0].getCupsourcebilltype();
						((ArriveorderVO) getCacheVOs()[i])
								.setUpBillType(cupsourcebilltype);
						// 刷新表体哈希表缓存
						for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
							if (getCacheVOs()[i].getChildrenVO()[j]
									.getPrimaryKey() == null)
								continue;
							if (getCacheVOs()[i].getChildrenVO()[j] == null)
								continue;
							hBodyItem.put(getCacheVOs()[i].getChildrenVO()[j]
									.getPrimaryKey(), getCacheVOs()[i]
									.getChildrenVO()[j]);
						}
					}
				}
			}
		} catch (Exception e) {
			reportException(e);
		}
	}

	/**
	 * 功能：获取存量查询对话框
	 */
	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return m_atpDlg;
	}

	/**
	 * @功能：退货查询条件对话框-采购
	 */
	private PoToBackRcQueDLG getBackQueDlgPo() {
		if (m_backQuePoDlg == null) {
			m_backQuePoDlg = new PoToBackRcQueDLG(this, getCorpPrimaryKey());
		}
		return m_backQuePoDlg;
	}

	/**
	 * @功能：退货查询条件对话框-委外
	 */
	private RcToScQueDLG getBackQueDlgSc() {
		if (m_backQueScDlg == null) {
			m_backQueScDlg = new RcToScQueDLG(this, getCorpPrimaryKey(),
					getOperatorId(),
					"40041015", // 虚节点：到货单退货至委外订单
					getBusitype(), BillTypeConst.PO_ARRIVE,
					BillTypeConst.SC_ORDER, null);
		}
		return m_backQueScDlg;
	}

	/**
	 * @功能：获取退货参照界面-采购
	 */
	private ArrFrmOrdUI getBackRefUIPo() {
		if (m_backRefUIPo == null) {
			m_backRefUIPo = new ArrFrmOrdUI("corderid", getCorpPrimaryKey(),
					getOperatorId(), "40041002", "1>0", BillTypeConst.PO_ORDER,
					getBusitype(), PoToBackRcQueDLG.class.getName(),
					BillTypeConst.PO_ARRIVE, this, true);
		}
		return m_backRefUIPo;
	}

	/**
	 * @功能：获取退货参照界面-采购
	 */
	private ArrFrmOrdUI getBackRefUISc() {
		if (m_backRefUISc == null) {
			m_backRefUISc = new ArrFrmOrdUI("corderid", getCorpPrimaryKey(),
					getOperatorId(), "40041003", "1>0", BillTypeConst.SC_ORDER,
					getBusitype(), ArrFrmOrdQueDLG.class.getName(),
					BillTypeConst.PO_ARRIVE, this, true);
		}
		return m_backRefUISc;
	}

	/**
	 * @功能：获取业务类型
	 * @作者：晁志平 创建日期：(2001-9-4 15:25:00)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return java.lang.String
	 */
	private String getBusitype() {
		if (refBusi != null) {
			return refBusi.getRefPK();
		} else {
			return null;
		}
	}

	/**
	 * 功能：检查数量编辑时合法性 1. narrvnum != null && narrvnum != 0 2. nassistnum != null
	 * && nassistnum != 0 3.|nelignum| <= |narrvnum| 4.|npresentnum| <=
	 * |narrvnum| 参数：单据模板编辑事件 返回：错误串 例外： 日期：(2002-7-26 9:03:59)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private String getErrMsg(BillEditEvent e) {
		String strErr = null;
		Object oArr = null, oAss = null, oElg = null, oPrsnt = null;
		UFDouble uArr = null, uAss = null, uElg = null, uPrsnt = null;
		BillModel bm = getBillCardPanel().getBillModel();
		oArr = bm.getValueAt(e.getRow(), "narrvnum");
		oAss = bm.getValueAt(e.getRow(), "nassistnum");
		oElg = bm.getValueAt(e.getRow(), "nelignum");
		oPrsnt = bm.getValueAt(e.getRow(), "npresentnum");
		if (oArr != null && !oArr.toString().trim().equals(""))
			uArr = new UFDouble(oArr.toString().trim());
		if (oAss != null && !oAss.toString().trim().equals(""))
			uAss = new UFDouble(oAss.toString().trim());
		if (oElg != null && !oElg.toString().trim().equals(""))
			uElg = new UFDouble(oElg.toString().trim());
		if (oPrsnt != null && !oPrsnt.toString().trim().equals(""))
			uPrsnt = new UFDouble(oPrsnt.toString().trim());
		// 数量编辑时
		if (e.getKey().equals("narrvnum")) {
			if (uArr == null)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000090")/*
																	 * @res
																	 * "数量为空"
																	 */;
			if (uArr.doubleValue() == 0)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000091")/*
																	 * @res
																	 * "数量为零"
																	 */;
		}
		// 辅数量编辑时
		if (e.getKey().equals("nassistnum")) {
			if (uAss == null)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000092")/*
																	 * @res
																	 * "辅数量为空"
																	 */;
			if (uAss.doubleValue() == 0)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000093")/*
																	 * @res
																	 * "辅数量为零"
																	 */;
		}
		// 合格数量编辑时
		if (e.getKey().equals("nelignum")) {
			if (uElg != null
					&& uElg.abs().doubleValue() > uArr.abs().doubleValue()) {
				strErr = m_lanResTool.getStrByID("40040301",
						"UPP40040301-000114")/* @res "合格数量绝对值大于到货数量绝对值" */;
			}
		}
		// 赠品数量编辑时
		if (e.getKey().equals("npresentnum")) {
			if (uPrsnt != null
					&& uPrsnt.abs().doubleValue() > uArr.abs().doubleValue()) {
				strErr = m_lanResTool.getStrByID("40040301",
						"UPP40040301-000094")/* @res "赠品数量绝对值大于到货数量绝对值" */;
			}
		}
		return strErr;
	}

	/**
	 * 获取本节点功能节点ID 创建日期：(2001-10-20 17:29:24)
	 * 
	 * @return java.lang.String
	 */
	private String getFuncId() {
		String funId = null;
		// funId = this.getModuleCode();
		if (funId == null || funId.trim().equals("")) {
			funId = "40040301";
		}
		return funId;

	}

	/**
	 * @功能：获取定位对话框
	 * @作者：晁志平 创建日期：(2001-9-15 13:50:13)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.ui.rc.receive.LocateDlg
	 */
	private LocateDlg getLocateDlg() {
		if (locatedlg == null) {
			locatedlg = new LocateDlg(
					this,
					(AggregatedValueObject[]) getCacheVOs(),
					getDispIndex(),
					m_lanResTool.getStrByID("40040301", "UPP40040301-000244")/*
																			 * @res
																			 * "到货单定位"
																			 */,
					m_lanResTool.getStrByID("40040301", "UPP40040301-000245")/*
																			 * @res
																			 * "到货单"
																			 */);
		}
		return locatedlg;
	}

	/**
	 * @功能：返回到货单VO数组
	 * @作者：晁志平 创建日期：(2001-6-19 20:13:12)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.vo.rc.receive.ArriveorderVO[]
	 */
	private nc.vo.rc.receive.ArriveorderVO[] getCacheVOs() {
		return m_arriveVOs;
	}

	/**
	 * @功能：获取到货单查询条件输入对话框
	 */
	private RcQueDlg getQueryConditionDlg() {

		if (m_dlgArrQueryCondition == null && isChangeBusitype) {

			m_dlgArrQueryCondition = new RcQueDlg(this, getBusitype(),
					getFuncId(), getOperatorId(), getCorpPrimaryKey());

			isChangeBusitype = false;
		}
		return m_dlgArrQueryCondition;
	}

	/**
	 * @功能：返回当前显示的VO位置
	 * @作者：晁志平 创建日期：(2001-6-20 8:47:47)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return int
	 */
	private int getDispIndex() {
		return m_iArrCurrRow;
	}

	/**
	 * @功能：返回单据状态
	 * @作者：晁志平 创建日期：(2001-6-19 20:18:22)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return java.lang.String
	 */
	private java.lang.String getStateStr() {
		return m_strState;
	}

	/**
	 * @功能：返回操作员ID
	 * @作者：晁志平 创建日期：(2001-10-24 14:12:52)
	 * @return java.lang.String
	 */
	public String getOperatorId() {
		String operatorid = getClientEnvironment().getUser().getPrimaryKey();
		if (operatorid == null || operatorid.trim().equals("")
				|| operatorid.equals("88888888888888888888")) {
			operatorid = "10013488065564590288";
		}
		return operatorid;
	}

	/**
	 * 作者：汪维敏 功能：此处插入方法说明 参数： 返回： 例外： 日期：(2004-3-16 15:34:30)
	 * 
	 * @return java.lang.String
	 * @param userid
	 *            java.lang.String
	 */
	public String getPsnIdByOperID(String userid) {
		if (userid == null)
			return null;
		if (userid.trim().equals(""))
			return null;
		userid = userid.trim();
		String psnid = null;
		try {
			psnid = ArriveorderHelper.getPkPsnByPkOper(userid);
		} catch (Exception e) {
			// reportException(e);
			SCMEnv.out("根据操作员关联人员档案时出错");
			psnid = null;
		}
		return psnid;
	}

	/**
	 * 作者：汪维敏 功能：此处插入方法说明 参数： 返回： 例外： 日期：(2004-3-15 13:34:10)
	 */
	private QueryOrdDlg getQuickArrDlg() {
		if (m_dlgQuickArr == null)
			m_dlgQuickArr = new QueryOrdDlg(this, this);
		return m_dlgQuickArr;
	}

	/**
	 * 功能：1、补齐未改变的到货单表体VO 2、处理删除表体更新为表体缓存哈希表中之表体 说明：为了对整单加锁所作的特殊处理 czp
	 * 2002-11-13
	 */
	private ArriveorderVO getSaveVO(ArriveorderVO vo) {
		// 所有表体向量
		Vector vAllBody = new Vector();
		// 未改变的表体VO
		ArriveorderItemVO[] voaUIAllBody = (ArriveorderItemVO[]) getBillCardPanel()
				.getBillModel().getBodyValueVOs(
						ArriveorderItemVO.class.getName());
		if (voaUIAllBody == null || voaUIAllBody.length <= 0)
			return vo;
		// 未改变的表体VO
		int iNoChangeLen = voaUIAllBody.length;
		for (int i = 0; i < iNoChangeLen; i++) {
			if (voaUIAllBody[i].getStatus() == VOStatus.UNCHANGED) {
				vAllBody.addElement((ArriveorderItemVO) voaUIAllBody[i]);
			}
		}
		// 改变的表体VO（拼接本方法参数VO的表体VO）
		int iChangeLen = -1;
		if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0)
			iChangeLen = vo.getChildrenVO().length;
		if (iChangeLen > 0) {
			for (int i = 0; i < iChangeLen; i++) {
				vAllBody.addElement((ArriveorderItemVO) vo.getChildrenVO()[i]);
			}
		}
		ArriveorderItemVO[] voaAllBody = new ArriveorderItemVO[vAllBody.size()];
		vAllBody.copyInto(voaAllBody);
		// 处理删除表体更新为表体缓存哈希表中之表体
		if (hBodyItem != null && hBodyItem.size() > 0) {
			if (voaAllBody != null && voaAllBody.length > 0) {
				for (int i = 0; i < voaAllBody.length; i++) {
					if (voaAllBody[i].getStatus() == VOStatus.DELETED) {
						if (voaAllBody[i].getPrimaryKey() == null)
							continue;
						if (hBodyItem.get(voaAllBody[i].getPrimaryKey()) == null)
							continue;
						voaAllBody[i] = (ArriveorderItemVO) hBodyItem
								.get(voaAllBody[i].getPrimaryKey());
						voaAllBody[i].setStatus(VOStatus.DELETED);
					}
				}
			}
		}
		vo.setChildrenVO(voaAllBody);
		return vo;
	}

	private ArrayList getSelectedBills() {

		Vector vAll = new Vector();
		ArriveorderVO[] allvos = null;
		// 全部选中询价单
		int iPos = 0;
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iPos = i;
				iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iPos);
				vAll.add(getCacheVOs()[iPos]);
			}
		}
		allvos = new ArriveorderVO[vAll.size()];
		vAll.copyInto(allvos);

		// 查询未被浏览过的单据体
		try {
			allvos = RcTool.getRefreshedVOs(allvos);
		} catch (BusinessException b) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000116")/* @res "发现错误:" */+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000117")/* @res "发现未知错误" */);
		}
		ArrayList aryRslt = new ArrayList();
		if (allvos != null) {
			for (int i = 0; i < allvos.length; i++) {
				aryRslt.add(allvos[i]);
			}
		}
		return aryRslt;
	}

	/**
	 * 作者：汪维敏 功能：接口IBillModelSortPrepareListener 的实现方法 参数：String sItemKey
	 * ITEMKEY 返回：无 例外：无 日期：(2004-03-24 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public int getSortTypeByBillItemKey(String sItemKey) {

		if ("crowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("csourcebilllinecode".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("cancestorbillrowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		}

		return getBillCardPanel().getBillModel().getItemByKey(sItemKey)
				.getDataType();
	}

	/**
	 * 作者：汪维敏 功能：此处插入方法说明 参数： 返回： 例外： 日期：(2004-3-16 15:36:26)
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getSysDate() {
		return getClientEnvironment().getDate();
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		String title = m_lanResTool
				.getStrByID("40040301", "UPP40040301-000248")/* @res "到货单维护" */;
		if (m_arrBillPanel != null)
			title = m_arrBillPanel.getTitle();
		return title;
	}

	/**
	 * V51重构需要的匹配,按钮实例变量化。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 下午01:15:06
	 */
	private void createBtnInstances() {
		// 检验
		m_btnCheck = getBtnTree().getButton(IButtonConstRc.BTN_CHECK);// new
																		// ButtonObject(
																		// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPT40040303-000009")/*@res
																		// "检验"*/,
																		// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000029")/*@res
																		// "检验到货单"*/,
																		// 2,
																		// "检验");
																		// /*-=notranslate=-*/
		// 业务类型
		m_btnBusiTypes = getBtnTree().getButton(
				IButtonConstRc.BTN_BUSINESS_TYPE);// new
													// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000003")/*@res
													// "业务类型"*/,
													// m_lanResTool.getStrByID("common","UC001-0000003")/*@res
													// "业务类型"*/, 2,"业务类型");
													// /*-=notranslate=-*/
		// 增加组
		m_btnAdds = getBtnTree().getButton(IButtonConstRc.BTN_ADD);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000002")/*@res
																	// "增加"*/,
																	// m_lanResTool.getStrByID("40040301","UPP40040301-000230")/*@res
																	// "生成到货单"*/,
																	// 2,"增加");
																	// /*-=notranslate=-*/
		// 退货组
		m_btnBackPo = getBtnTree().getButton(IButtonConstRc.BTN_BACK_PU);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000021")/*@res
																			// "采购订单"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000231")/*@res
																			// "参照采购订单生成到货单"*/,
																			// 2,"采购订单");
																			// /*-=notranslate=-*/
		m_btnBackSc = getBtnTree().getButton(IButtonConstRc.BTN_BACK_SC);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000022")/*@res
																			// "委外订单"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000232")/*@res
																			// "参照委外订单生成到货单"*/,
																			// 2,"委外订单");
																			// /*-=notranslate=-*/
		m_btnBacks = getBtnTree().getButton(IButtonConstRc.BTN_BACK);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
																		// "退货"*/,m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
																		// "退货"*/,2,
																		// "退货");
		// 单据维护组
		m_btnModify = getBtnTree().getButton(IButtonConstRc.BTN_BILL_EDIT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
																			// "修改"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000235")/*@res
																			// "修改到货单"*/,
																			// 2,"修改");
																			// /*-=notranslate=-*/
		m_btnSave = getBtnTree().getButton(IButtonConstRc.BTN_SAVE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000001")/*@res
																	// "保存"*/,
																	// m_lanResTool.getStrByID("40040301","UPP40040301-000237")/*@res
																	// "保存修改结果"*/,
																	// 2,"保存");
																	// /*-=notranslate=-*/
		m_btnCancel = getBtnTree().getButton(IButtonConstRc.BTN_BILL_CANCEL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000008")/*@res
																				// "取消"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000008")/*@res
																				// "取消"*/,
																				// 2,"取消");
																				// /*-=notranslate=-*/
		m_btnDiscard = getBtnTree().getButton(IButtonConstRc.BTN_BILL_DELETE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
																				// "作废"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
																				// "作废"*/,
																				// 2,"作废");
																				// /*-=notranslate=-*/
		m_btnSendAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_EXECUTE_AUDIT);// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000017")/*
													// @res "送审"
													// */,m_lanResTool.getStrByID("40040101","UPP40040101-000451")/*
													// @res "送审到货单" */, 2,
													// "送审");
													// /*-=notranslate=-*/
		m_btnMaintains = getBtnTree().getButton(IButtonConstRc.BTN_BILL);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
																			// "单据维护"*/,
																			// m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
																			// "单据维护"*/,2,"单据维护");
		// 行操作组
		m_btnDelLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_DELETE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000013")/*@res
																				// "删行"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000013")/*@res
																				// "删行"*/,
																				// 2,"删行");
																				// /*-=notranslate=-*/
		m_btnCpyLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_COPY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000014")/*@res
																			// "复制行"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																			// "复制行"*/,
																			// 2,"复制行");
																			// /*-=notranslate=-*/
		m_btnPstLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																				// "粘贴行"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																				// "粘贴行"*/,
																				// 2,"粘贴行");
																				// /*-=notranslate=-*/
		m_btnLines = getBtnTree().getButton(IButtonConstRc.BTN_LINE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000011")/*@res
																		// "行操作"*/,m_lanResTool.getStrByID("common","UC001-0000011")/*@res
																		// "行操作"*/,2,
																		// "行操作");
		// 单据浏览组
		m_btnQuery = getBtnTree().getButton(IButtonConstRc.BTN_QUERY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
																		// "查询"*/,
																		// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
																		// "查询"*/,
																		// 2,"查询");
																		// /*-=notranslate=-*/
		m_btnFirst = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_TOP);// ButtonObject(m_lanResTool.getStrByID("common","UCH031")/*@res
																			// "首页"*/,
																			// m_lanResTool.getStrByID("common","UCH031")/*@res
																			// "首页"*/,
																			// 2,"首页");
																			// /*-=notranslate=-*/
		m_btnPrev = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_PREVIOUS);// ButtonObject(m_lanResTool.getStrByID("common","UCH033")/*@res
																				// "上一页"*/,
																				// m_lanResTool.getStrByID("common","UCH033")/*@res
																				// "上一页"*/,
																				// 2,"上一页");
																				// /*-=notranslate=-*/
		m_btnNext = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_NEXT);// ButtonObject(m_lanResTool.getStrByID("common","UCH034")/*@res
																			// "下一页"*/,
																			// m_lanResTool.getStrByID("common","UCH034")/*@res
																			// "下一页"*/,
																			// 2,"下一页");
																			// /*-=notranslate=-*/
		m_btnLast = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_BOTTOM);// ButtonObject(m_lanResTool.getStrByID("common","UCH032")/*@res
																				// "末页"*/,
																				// m_lanResTool.getStrByID("common","UCH032")/*@res
																				// "末页"*/,
																				// 2,"末页");
																				// /*-=notranslate=-*/
		m_btnLocate = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_LOCATE);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
																				// "定位"*/,
																				// m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
																				// "定位"*/,
																				// 2,"定位");
																				// /*-=notranslate=-*/
		m_btnRefresh = getBtnTree()
				.getButton(IButtonConstRc.BTN_BROWSE_REFRESH);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
																// "刷新"*/,
																// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
																// "刷新"*/,
																// 2,"刷新");
																// /*-=notranslate=-*/
		m_btnBrowses = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000021")/*@res
																			// "浏览"*/,m_lanResTool.getStrByID("common","UC001-0000021")/*@res
																			// "浏览"*/,2,
																			// "浏览");
		// 切换
		m_btnList = getBtnTree().getButton(IButtonConstRc.BTN_SWITCH);// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
																		// "列表显示"*/,
																		// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
																		// "列表显示"*/,
																		// 2,"切换");
																		// /*-=notranslate=-*/
		// 执行组(审批、弃审与消息中心共用)
		m_btnActions = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
																			// "执行"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
																			// "执行"*/,
																			// 0,"执行");
																			// /*-=notranslate=-*/
		// 辅助组
		m_btnCombin = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_DISTINCT);// ButtonObject(m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
																				// "合并显示"*/,
																				// m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
																				// "合并显示"*/,
																				// 2,"合并显示");
																				// /*-=notranslate=-*/
		m_btnPrints = getBtnTree().getButton(IButtonConstRc.BTN_PRINT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																		// "打印"*/,
																		// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																		// "打印"*/,
																		// 2,"打印");
																		// /*-=notranslate=-*/
		m_btnPrint = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PRINT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																			// "打印"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																			// "打印"*/,
																			// 2,"打印");
																			// /*-=notranslate=-*/
		m_btnImportBill = getBtnTree().getButton("EXCEL导入");//EXCEL导入
		
		m_btnImportXml = getBtnTree().getButton("XML导入");//XML导入 yqq 2016-11-02 测试
		
		m_btnPrintPreview = getBtnTree().getButton(
				IButtonConstRc.BTN_PRINT_PREVIEW);// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/, 2,"预览");
													// /*-=notranslate=-*/
		m_btnUsable = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_ONHAND);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
														// "存量查询"*/,
														// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
														// "存量查询"*/, 2,"存量查询");
														// /*-=notranslate=-*/
		m_btnQueryBOM = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_SUITE);// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
														// "成套件"*/,
														// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
														// "成套件"*/, 2,"成套件");
														// /*-=notranslate=-*/
		m_btnQuickReceive = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_FUNC_QUICK_RECEIVE);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
																// "快速收货"*/,
																// m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
																// "快速收货"*/,
																// 2,"快速收货");
																// /*-=notranslate=-*/
		m_btnDocument = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_FUNC_DOCUMENT);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
															// "文档管理"*/,
															// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
															// "文档管理"*/,
															// 2,"文档管理");
															// /*-=notranslate=-*/
		m_btnLookSrcBill = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_RELATED);// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
															// "联查"*/,
															// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
															// "联查"*/, 2,"联查");
															// /*-=notranslate=-*/
		m_btnQueryForAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_WORKFLOW);// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000032")/*
															// @res "状态查询"
															// */,m_lanResTool.getStrByID("40040101","UPP40040101-000450")/*
															// @res "审批状态查询" */,
															// 2, "状态查询");
															// /*-=notranslate=-*/
		m_btnOthers = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
																				// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
																				// "辅助"*/,2,
																				// "辅助");
		/*
		 * { m_btnUsable, m_btnQueryBOM,m_btnQuickReceive, m_btnDocument,
		 * m_btnLookSrcBill, m_btnQueryForAudit, m_btnCombin, m_btnPrint,
		 * m_btnPrintPreview};
		 */

		/* 卡片按钮菜单 */

		m_aryArrCardButtons = getBtnTree().getButtonArray();

		/* 列表按钮定义 */
		
		
		m_btnSelectAll = getBtnTree().getButton(
				IButtonConstRc.BTN_BROWSE_SELECT_ALL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000041")/*@res
														// "全选"*/,
														// m_lanResTool.getStrByID("40040301","UPP40040301-000238")/*@res
														// "全部选定"*/, 2,"全选");
														// /*-=notranslate=-*/
		m_btnSelectNo = getBtnTree().getButton(
				IButtonConstRc.BTN_BROWSE_SELECT_NONE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000042")/*@res
														// "全消"*/,
														// m_lanResTool.getStrByID("40040301","UPP40040301-000233")/*@res
														// "全部取消"*/, 2,"全消");
														// /*-=notranslate=-*/
		m_btnModifyList = m_btnModify;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
										// "修改"*/,
										// m_lanResTool.getStrByID("40040301","UPPSCMCommon-000291")/*@res
										// "修改单据"*/, 2, "列表修改");
										// /*-=notranslate=-*/
		m_btnDiscardList = m_btnDiscard;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
										// "作废"*/,
										// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
										// "作废"*/, 2,"列表作废");
										// /*-=notranslate=-*/
		m_btnQueryList = m_btnQuery;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
									// "查询"*/,
									// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
									// "查询"*/, 2,"列表查询"); /*-=notranslate=-*/
		m_btnCard = m_btnList;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
								// "卡片显示"*/,
								// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
								// "卡片显示"*/, 2,"列表切换"); /*-=notranslate=-*/
		m_btnEndCreate = getBtnTree().getButton(IButtonConstRc.BTN_REF_CANCEL);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000043")/*@res
																				// "放弃转单"*/,
																				// m_lanResTool.getStrByID("40040301","UPP40040301-000234")/*@res
																				// "放弃到货单保存"*/,
																				// 2,"放弃转单");
																				// /*-=notranslate=-*/
		m_btnRefreshList = m_btnRefresh;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
										// "刷新"*/,
										// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
										// "刷新"*/, 2,"刷新"); /*-=notranslate=-*/

		// 辅助组
		m_btnUsableList = m_btnUsable;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
										// "存量查询"*/,
										// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
										// "存量查询"*/, 2,"列表存量查询");
										// /*-=notranslate=-*/
		m_btnDocumentList = m_btnDocument;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
											// "文档管理"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
											// "文档管理"*/, 2,"列表文档管理");
											// /*-=notranslate=-*/
		m_btnQueryBOMList = m_btnQueryBOM;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
											// "成套件"*/,
											// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
											// "成套件"*/, 2,"列表成套件");
											// /*-=notranslate=-*/
		m_btnPrintPreviewList = m_btnPrintPreview;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "预览"*/, 2,"列表打印预览");
													// /*-=notranslate=-*/
		m_btnPrintList = m_btnPrint;// ButtonObject(m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
									// "打印单据"*/,
									// m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
									// "打印单据"*/, 2,"列表打印"); /*-=notranslate=-*/
		m_btnOthersList = m_btnOthers;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
										// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
										// "辅助"*/,2, "辅助"); /*-=notranslate=-*/

		/* 列表按钮组 */
		m_aryArrListButtons = m_aryArrCardButtons;

		/* 消息中心按钮组 */
		m_btnAudit = getBtnTree().getButton(IButtonConstRc.BTN_AUDIT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000027")/*
																		// @res
																		// "审批"
																		// */,m_lanResTool.getStrByID("common","UC001-0000027")/*
																		// @res
																		// "审批"
																		// */,
																		// 2,
																		// "审批");
																		// /*-=notranslate=-*/
		m_btnAudit.setTag("APPROVE");
		m_btnUnAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_EXECUTE_AUDIT_CANCEL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000028")/*
															// @res "弃审"
															// */,m_lanResTool.getStrByID("40040401","UPP40040401-000149")/*
															// @res "执行弃审操作" */,
															// 5, "弃审");
															// /*-=notranslate=-*/
		m_btnUnAudit.setTag("UNAPPROVE");
		m_btnOthersMsgCenter = m_btnOthers;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "辅助"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "辅助"*/,2, "辅助");
		m_btnActionMsgCenter = m_btnActions;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
											// "执行"*/,
											// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
											// "执行"*/, 0,"执行");
											// /*-=notranslate=-*/
		m_aryMsgCenter = new ButtonObject[] { m_btnActionMsgCenter,
				m_btnOthersMsgCenter };
	}

	/**
	 * 初始化按钮 创建日期：(01-2-26 13:29:17)
	 */
	private void setButtonsInit() {
		// 特殊功能
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "列表显示"
									 */);
		//
		for (int i = 0; i < m_aryArrCardButtons.length; i++) {
			if (PuTool.isExist(getExtendBtns(), m_aryArrCardButtons[i])) {
				continue;
			}
			m_aryArrCardButtons[i].setEnabled(false);
		}
		// 放弃转单
		m_btnEndCreate.setVisible(false);
		// 辅助功能
		getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC).setEnabled(true);

		getBtnTree().getButton(IButtonConstRc.BTN_PRINT).setEnabled(true);
		m_btnActions.setEnabled(true);
		m_btnAudit.setEnabled(false);
		m_btnUnAudit.setEnabled(false);
		// 单据维护
		m_btnMaintains.setEnabled(true);
		m_btnModify.setEnabled(false);
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnDiscard.setEnabled(false);
		m_btnSelectAll.setEnabled(false);
		m_btnSendAudit.setEnabled(false);
		// 浏览
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML导入  yqq 2016-11-02 测试
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
		// 辅助
		m_btnOthers.setEnabled(true);
		m_btnRefresh.setEnabled(false);
		m_btnLocate.setEnabled(false);
		m_btnUsable.setEnabled(false);
		m_btnQueryBOM.setEnabled(false);
		m_btnQuickReceive.setEnabled(true);
		m_btnDocument.setEnabled(false);
		m_btnLookSrcBill.setEnabled(false);
		m_btnPrint.setEnabled(false);
		m_btnCombin.setEnabled(false);
		m_btnPrintPreview.setEnabled(false);
		m_btnQueryForAudit.setEnabled(false);

		m_btnCheck.setEnabled(false);

		m_btnBusiTypes.setEnabled(true);
		m_btnAdds.setEnabled(true);

		m_btnList.setEnabled(true);

		m_btnBacks.setEnabled(true);
		m_btnBackPo.setEnabled(true);
		m_btnBackSc.setEnabled(true);
		m_btnImportBill.setEnabled(false);
		
		//
		updateButtonsAll();
	}

	/**
	 * 功能：初始化精度 参数： 返回： 例外： 日期：(2002-8-21 10:01:19) 修改日期，修改人，修改原因，注释标志：
	 */
	private void initDecimal() {
		// 精度设置

		// 数量
		if (m_arrBillPanel.getBodyItem("narrvnum") != null)
			m_arrBillPanel.getBodyItem("narrvnum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrBillPanel.getBodyItem("nelignum") != null)
			m_arrBillPanel.getBodyItem("nelignum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrBillPanel.getBodyItem("nnotelignum") != null)
			m_arrBillPanel.getBodyItem("nnotelignum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrBillPanel.getBodyItem("npresentnum") != null)
			m_arrBillPanel.getBodyItem("npresentnum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrBillPanel.getBodyItem("nwastnum") != null)
			m_arrBillPanel.getBodyItem("nwastnum").setDecimalDigits(
					nMeasDecimal);
		// 辅数量
		if (m_arrBillPanel.getBodyItem("nassistnum") != null)
			m_arrBillPanel.getBodyItem("nassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// 辅数量
		if (m_arrBillPanel.getBodyItem("npresentassistnum") != null)
			m_arrBillPanel.getBodyItem("npresentassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// 换算率
		if (m_arrBillPanel.getBodyItem("convertrate") != null)
			m_arrBillPanel.getBodyItem("convertrate").setDecimalDigits(
					nConvertDecimal);
		// 单价
		if (m_arrBillPanel.getBodyItem("nprice") != null)
			m_arrBillPanel.getBodyItem("nprice")
					.setDecimalDigits(nPriceDecimal);
		// 金额
		if (m_arrBillPanel.getBodyItem("nmoney") != null) {
			m_arrBillPanel.getBodyItem("nmoney").setDecimalDigits(
					nNmoneyDecimal);
		}

	}

	/**
	 * 初始化按钮。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-19 上午09:26:36
	 */
	private void initButtons() {

		// V51重构需要的匹配,按钮实例变量化
		createBtnInstances();

		// 业务类型按钮组子菜单
		PfUtilClient.retBusinessBtn(m_btnBusiTypes, getCorpPrimaryKey(),
				BillTypeConst.PO_ARRIVE);

		// 业务类型按钮打钩处理
		PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds,
				BillTypeConst.PO_ARRIVE, getCorpPrimaryKey());

		// 加载扩展按钮
		addExtendBtns();

		// 加载卡片按钮
		setButtons(m_btnTree.getButtonArray());
	}

	/**
	 * 增加扩展按钮。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-19 上午09:23:34
	 */
	private void addExtendBtns() {
		ButtonObject[] btnsExtend = getExtendBtns();
		if (btnsExtend == null || btnsExtend.length <= 0) {
			return;
		}
		ButtonObject boExtTop = getBtnTree().getExtTopButton();
		getBtnTree().addMenu(boExtTop);
		int iLen = btnsExtend.length;
		try {
			for (int j = 0; j < iLen; j++) {
				getBtnTree().addChildMenu(boExtTop, btnsExtend[j]);
			}
		} catch (BusinessException be) {
			showHintMessage(be.getMessage());
			return;
		}
	}

	/**
	 * 功能描述:节点初始化
	 * 
	 * 获取业务类型、加载单据模板、初始化按钮状态
	 */
	private void initialize() {

		// 初始化精度
		initPara();

		// 初始化按钮
		initButtons();

		// 设置按钮状态
		setButtonsState();

		// 显示单据
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), BorderLayout.CENTER);

		// 初始化表体备注
		initVmemoBody();

		// 初始化精度
		initDecimal();

		// 初始化到货单状态
		getBillCardPanel().setEnabled(false);
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	/**
	 * 功能：初始化表体备注
	 */
	private void initVmemoBody() {
		if (getBillCardPanel().getBodyItem("vmemo") != null) {
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem(
					"vmemo").getComponent();
			nRefPanel.setTable(getBillCardPanel().getBillTable());
			nRefPanel.getRefModel().setRefCodeField(
					nRefPanel.getRefModel().getRefNameField());
			nRefPanel.getRefModel().setBlurFields(
					new String[] { nRefPanel.getRefModel().getRefNameField() });
			nRefPanel.setAutoCheck(false);
		}
	}

	/**
	 * 功能：初始化精度 参数： 返回： 例外： 日期：(2002-8-21 10:01:19) 修改日期，修改人，修改原因，注释标志：
	 */
	private void initListDecimal() {
		// 精度设置

		// 数量
		if (m_arrListPanel.getBodyItem("narrvnum") != null)
			m_arrListPanel.getBodyItem("narrvnum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrListPanel.getBodyItem("nelignum") != null)
			m_arrListPanel.getBodyItem("nelignum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrListPanel.getBodyItem("nnotelignum") != null)
			m_arrListPanel.getBodyItem("nnotelignum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrListPanel.getBodyItem("npresentnum") != null)
			m_arrListPanel.getBodyItem("npresentnum").setDecimalDigits(
					nMeasDecimal);
		if (m_arrListPanel.getBodyItem("nwastnum") != null)
			m_arrListPanel.getBodyItem("nwastnum").setDecimalDigits(
					nMeasDecimal);
		// 辅数量
		if (m_arrListPanel.getBodyItem("nassistnum") != null)
			m_arrListPanel.getBodyItem("nassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// 换算率
		if (m_arrListPanel.getBodyItem("convertrate") != null)
			m_arrListPanel.getBodyItem("convertrate").setDecimalDigits(
					nConvertDecimal);
		// 单价
		if (m_arrListPanel.getBodyItem("nprice") != null)
			m_arrListPanel.getBodyItem("nprice")
					.setDecimalDigits(nPriceDecimal);
		// 金额
		if (m_arrListPanel.getBodyItem("nmoney") != null)
			m_arrListPanel.getBodyItem("nmoney").setDecimalDigits(
					nNmoneyDecimal);

	}

	/**
	 * 功能描述:初始化参数
	 */
	public void initPara() {

		// 隐藏货位分配功能
		// m_btnAllotCarg.setVisible(false);
		// m_btnAllotCarg.setEnabled(false);
		// 初始化精度
		/**
		 * BD502 辅计量 BD503 换算率 BD501 主计量 BD505 采购单价
		 */
		// int[] iDigits = PuTool.getDigitBatch(getCorpId(), new String[] {
		// "BD502", "BD503", "BD501", "BD505" });
		// if (iDigits != null && iDigits.length == 4) {
		// nAssistUnitDecimal = iDigits[0];
		// nConvertDecimal = iDigits[1];
		// nMeasDecimal = iDigits[2];
		// nPriceDecimal = iDigits[3];
		// }
		// nNmoneyDecimal = CPurchseMethods.getCurrDecimal(getCorpId());

		try {

			ServcallVO[] scDisc = new ServcallVO[2];
			// 初始化精度（数量、单价）
			scDisc[0] = new ServcallVO();
			scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
			scDisc[0].setMethodName("getDigitBatch");
			scDisc[0].setParameter(new Object[] { getCorpPrimaryKey(),
					new String[] { "BD502", "BD503", "BD501", "BD505" } });
			scDisc[0].setParameterTypes(new Class[] { String.class,
					String[].class });

			scDisc[1] = new ServcallVO();
			scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
			scDisc[1].setMethodName("getCurrDecimal");
			scDisc[1].setParameter(new Object[] { getCorpPrimaryKey() });
			scDisc[1].setParameterTypes(new Class[] { String.class });

			// 自定义项远程调用描述类
			// ServletCallDiscription[] scdsDef =
			// nc.ui.scm.pub.def.DefSetTool.getTwoSCDs(getCorpId());
			// scDisc[2] = scdsDef[0];
			// scDisc[3] = scdsDef[1];

			// scDisc[2] = new ServcallVO();
			// scDisc[2].setBeanName("nc.bs.pub.para.SysInitBO");
			// scDisc[2].setMethodName("getParaString");
			// scDisc[2].setParameter(new Object[] { getCorpId(), "PO060"});
			// scDisc[2].setParameterTypes(new Class[] { String.class,
			// String.class });

			String strPara0 = SysInitBO_Client.getParaString(
					PoPublicUIClass.getLoginPk_corp(), "PO060");

			// 后台一次调用
			Object[] oParaValue = nc.ui.scm.service.LocalCallService
					.callService(scDisc);
			if (oParaValue != null && oParaValue.length == scDisc.length) {
				// 数量、单据精度
				int[] iDigits = (int[]) oParaValue[0];
				if (iDigits != null && iDigits.length == 4) {
					nAssistUnitDecimal = iDigits[0];
					nConvertDecimal = iDigits[1];
					nMeasDecimal = iDigits[2];
					nPriceDecimal = iDigits[3];
				}
				// 本币金额精度
				nNmoneyDecimal = ((Integer) oParaValue[1]).intValue();

				// 自定义项预处理
				// nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] {
				// oParaValue[2], oParaValue[3] });

				String s = strPara0;
				if (s != null)
					m_bSaveMaker = (new UFBoolean(s)).booleanValue();
			}
			// 质量管理模块启用
			ICreateCorpQueryService tt = (ICreateCorpQueryService) NCLocator
					.getInstance().lookup(
							ICreateCorpQueryService.class.getName());
			m_bQcEnabled = tt.isEnabled(getCorpPrimaryKey(),
					ProductCode.PROD_QC);
		} catch (Exception e) {
			reportException(e);
		}

	}

	/**
	 * 功能：初始化参照 参数： 返回： 例外： 日期：(2002-8-21 10:01:19) 修改日期，修改人，修改原因，注释标志：
	 */
	private void initRefPane(BillData bd) {
		// －－－－－－－－表头－－－－－－－－

		// 业务类型初始化
		if (bd.getHeadItem("cbiztype") != null) {
			refBusi = (UIRefPane) bd.getHeadItem("cbiztype").getComponent();
			refBusi.setEnabled(false);
		}
		// 部门
		if (bd.getHeadItem("cdeptid") != null) {
			String sql = "and (bd_deptdoc.deptattr IN ('2','4'))";
			UIRefPane refDept = (UIRefPane) (bd.getHeadItem("cdeptid")
					.getComponent());
			/*
			 * String sqltemp = refDept.getRefModel().getWherePart(); if
			 * ((sqltemp != null) && (!(sqltemp.trim().equals("")))) { sql = sql
			 * + " and " + sqltemp; }
			 */
			refDept.getRefModel().addWherePart(sql);
		}
		// 业务员
		if (bd.getHeadItem("cemployeeid") != null) {
			String sql = "and (bd_psndoc.pk_deptdoc IN (SELECT pk_deptdoc FROM bd_deptdoc WHERE (deptattr IN ('2','4')) AND dr = 0))";
			UIRefPane refEmpl = (UIRefPane) (bd.getHeadItem("cemployeeid")
					.getComponent());
			refEmpl.getRefModel().setHiddenFieldCode(
					new String[] { "bd_psndoc.pk_psndoc",
							"bd_psndoc.pk_deptdoc" });
			/*
			 * String sqltemp = refEmpl.getRefModel().getWherePart(); if
			 * ((sqltemp != null) && (!(sqltemp.trim().equals("")))) { sql = sql
			 * + " and " + sqltemp; }
			 */
			refEmpl.getRefModel().addWherePart(sql);
		}
		// 表头退货理由
		if (bd.getHeadItem("vbackreasonh") != null) {
			UIRefPane refPaneReason = (UIRefPane) bd
					.getHeadItem("vbackreasonh").getComponent();
			refPaneReason.setRefModel(new BackReasonRefModel());
			refPaneReason.setAutoCheck(false);
		}
		// 表头备注
		if (bd.getHeadItem("vmemo") != null) {
			UIRefPane refVmemo = (UIRefPane) bd.getHeadItem("vmemo")
					.getComponent();
			refVmemo.setRefNodeName("常用摘要");
			refVmemo.getRefModel().setRefCodeField(
					refVmemo.getRefModel().getRefNameField());
			refVmemo.getRefModel().setBlurFields(
					new String[] { refVmemo.getRefModel().getRefNameField() });
			refVmemo.setAutoCheck(false);
		}

		// 库存组织
		if (bd.getHeadItem("cstoreorganization") != null) {
			UIRefPane refPane = (UIRefPane) bd
					.getHeadItem("cstoreorganization").getComponent();
			refPane.getRefModel()
					.addWherePart(
							" and (bd_calbody.property = 0 or bd_calbody.property = 1) ");
		}

		// －－－－－－－－表体－－－－－－－－

		// 自由项
		/*
		 * if (bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") !=
		 * null) { FreeItemRefPane m_firpFreeItemRefPane = new
		 * FreeItemRefPane();
		 * m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0"
		 * ).getLength()); //加监听器
		 * m_firpFreeItemRefPane.getUIButton().addActionListener(this);
		 * bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
		 * 
		 * }
		 */
		// 表体批次号参照
		try {
			if (bd.getBodyItem("vproducenum") != null
					&& bd.getBodyItem("vproducenum").isShow()) {
				// UIRefPane lotRef =
				// (UIRefPane)InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
				UIRefPane lotRef = (UIRefPane) new LotNumbRefPane();
				lotRef.setIsCustomDefined(true);
				lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
				bd.getBodyItem("vproducenum").setComponent(lotRef);
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		// //表体备注处理
		// if (bd.getBodyItem("vmemo") != null) {
		// UIRefPane nRefPanel = (UIRefPane)
		// bd.getBodyItem("vmemo").getComponent();
		// nRefPanel.setTable(bd.getBillTable());
		// nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
		// nRefPanel.getRefModel().setBlurFields(new String[] {
		// nRefPanel.getRefModel().getRefNameField()});
		// nRefPanel.setAutoCheck(false);
		// }

		// 项目参照
		if (bd.getBodyItem("cproject") != null) {
			String sql = "(upper(isnull(bd_jobbasfil.sealflag,'N')) = 'N')";
			UIRefPane ref = (UIRefPane) (bd.getBodyItem("cproject")
					.getComponent());
			String sqltemp = ref.getRefModel().getWherePart();
			if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
				sql = sql + " and " + sqltemp;
			}
			ref.getRefModel().setWherePart(sql);
		}
		// 价格非负
		if (bd.getBodyItem("nprice") != null) {
			UIRefPane refPrice = (UIRefPane) bd.getBodyItem("nprice")
					.getComponent();
			refPrice.setMinValue(0);
			refPrice.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// 换算率非负
		if (bd.getBodyItem("convertrate") != null) {
			UIRefPane refConvert = (UIRefPane) bd.getBodyItem("convertrate")
					.getComponent();
			refConvert.setMinValue(0);
			refConvert.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// 保质期天数非负
		if (bd.getBodyItem("ivalidday") != null) {
			UIRefPane refVld = (UIRefPane) bd.getBodyItem("ivalidday")
					.getComponent();
			refVld.setMinValue(0);
			refVld.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}

		// 表体退货理由
		if (bd.getBodyItem("vbackreasonb") != null) {
			UIRefPane refPaneReason1 = new UIRefPane();
			refPaneReason1.setRefModel(new BackReasonRefModel());
			bd.getBodyItem("vbackreasonb").setComponent(refPaneReason1);
			refPaneReason1.getRefModel().setRefCodeField(
					refPaneReason1.getRefModel().getRefNameField());
			refPaneReason1.getRefModel().setBlurFields(
					new String[] { refPaneReason1.getRefModel()
							.getRefNameField() });
			refPaneReason1.setAutoCheck(false);
			refPaneReason1.setReturnCode(true);
		}
		// 表体退货理由处理
		// if (bd.getBodyItem("vbackreasonb") != null) {
		// UIRefPane refPanel = (UIRefPane)
		// bd.getBodyItem("vbackreasonb").getComponent();
		// refPanel.setTable(bd.getBillTable());
		// refPanel.getRefModel().setRefCodeField(refPanel.getRefModel().getRefNameField());
		// refPanel.getRefModel().setBlurFields(new String[] {
		// refPanel.getRefModel().getRefNameField()});
		// refPanel.setAutoCheck(false);
		// }

		// 收货仓库
		if (bd.getBodyItem("cwarehousename") != null) {
			UIRefPane refStore = (UIRefPane) bd.getBodyItem("cwarehousename")
					.getComponent();
			refStore.setRefModel(new WarehouseRefModel(getCorpPrimaryKey()));
			refStore.getRefModel()
					.addWherePart(
							" and UPPER(bd_stordoc.gubflag) <> 'Y' and UPPER(bd_stordoc.sealflag) <> 'Y' ");
		}
		// 货位非封存
		if (bd.getBodyItem("cstorename") != null) {
			UIRefPane refCarg = (UIRefPane) bd.getBodyItem("cstorename")
					.getComponent();
			refCarg.getRefModel().addWherePart(
					"and UPPER(bd_cargdoc.sealflag) <> 'Y' ");
		}
		// 辅计量参照
		if (bd.getBodyItem("cassistunitname") != null) {
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setIsCustomDefined(true);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setRefModel(new OtherRefModel("辅计量单位"));
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setReturnCode(false);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setRefInputType(1);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setCacheEnabled(false);
		}

	}

	/**
	 * 是否退货单据
	 */
	private boolean isBackBill() {
		if (!getBillCardPanel().isVisible()) {
			SCMEnv.out("1.bisback='N'");
			return false;
		}
		if (getBillCardPanel().getHeadItem("bisback") == null
				|| getBillCardPanel().getHeadItem("bisback").getValue() == null
				|| getBillCardPanel().getHeadItem("bisback").getValue().trim()
						.equals("")) {
			SCMEnv.out("2.bisback='N'");
			return false;
		}
		if (new UFBoolean(getBillCardPanel().getHeadItem("bisback").getValue())
				.booleanValue()) {
			SCMEnv.out("3.bisback='Y'");
			return true;
		}
		SCMEnv.out("4.bisback='N'");
		return false;
	}

	/**
	 * 功能：(适用于修改的表行)根据到货数量关系判断到货单行是否免检 参数： 返回：算法(适用于修改的表行): naccumchecknum ==
	 * null(0) && (nelignum + nnotelignum != null(0)) 返回 true 例外： 日期：(2002-9-12
	 * 13:03:45) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private boolean isCheckFree(BillEditEvent e) {
		// 当前单据表体哈希表（key = 行ID）
		Hashtable hRowIdBody = new Hashtable();
		for (int i = 0; i < getCacheVOs()[getDispIndex()].getChildrenVO().length; i++) {
			hRowIdBody.put(getCacheVOs()[getDispIndex()].getChildrenVO()[i]
					.getAttributeValue("carriveorder_bid"),
					getCacheVOs()[getDispIndex()].getChildrenVO()[i]);
		}
		String sRowId = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "carriveorder_bid");
		ArriveorderItemVO item = (ArriveorderItemVO) hRowIdBody.get(sRowId);
		UFDouble nAcc = null, nElg = null, nNotElg = null;
		nAcc = (item.getNaccumchecknum() == null || item.getNaccumchecknum()
				.doubleValue() == 0) ? new UFDouble(0) : item
				.getNaccumchecknum();
		nElg = (item.getNelignum() == null || item.getNelignum().doubleValue() == 0) ? new UFDouble(
				0) : item.getNelignum();
		nNotElg = (item.getNnotelignum() == null || item.getNnotelignum()
				.doubleValue() == 0) ? new UFDouble(0) : item.getNnotelignum();
		if (nAcc.doubleValue() == 0 && nElg.add(nNotElg).doubleValue() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 是否只存在到货或退货一种单据(即不存在交叉)
	 */
	private boolean isOnlyOneTypeBill() {
		BillModel bm = getBillListPanel().getHeadBillModel();
		int iRowCnt = bm.getRowCount();
		if (iRowCnt <= 0)
			return true;
		iRowCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (iRowCnt <= 1)
			return true;
		Object objTmp = null;
		Vector vJudge = new Vector();
		for (int i = 0; i < iRowCnt; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED) {
				objTmp = bm.getValueAt(i, "bisback");
				if (vJudge.size() > 0 && !vJudge.contains(objTmp))
					return false;
				vJudge.addElement(objTmp);
			}
		}
		vJudge = null;
		return true;
	}

	/**
	 * @功能：加载表头基础数据
	 */
	private void loadBDData() {
		/* 变量定义 */
		String strFormula[] = new String[] {
				"vendor->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cvendorbaseid)",
				"cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)",
				"cemployee->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)",
				"ctransmode->getColValue(bd_sendtype,sendname,pk_sendtype,ctransmodeid)",
				"creceivepsnlist->getColValue(bd_psndoc,psnname,pk_psndoc,creceivepsn)",
				"cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)",
				"cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)" };
		String strVarValue = null, strValue = null;
		nc.ui.pub.formulaparse.FormulaParse parse = new nc.ui.pub.formulaparse.FormulaParse();
		Hashtable hData[] = new Hashtable[7];
		UIRefPane refpnl[] = new UIRefPane[7];

		for (int i = 0; i < 7; i++)
			hData[i] = new Hashtable();
		/* 供应商 */
		if (getCacheVOs()[getDispIndex()] != null
				&& getCacheVOs()[getDispIndex()].getParentVO() != null) {
			strVarValue = (String) getCacheVOs()[getDispIndex()].getParentVO()
					.getAttributeValue("cvendorbaseid");
			refpnl[0] = (UIRefPane) getBillCardPanel().getHeadItem(
					"cvendormangid").getComponent();
			strValue = refpnl[0].getUITextField().getText();
			if (strVarValue != null
					&& (strValue == null || strValue.trim().equals(""))) {
				hData[0].put("cvendorbaseid", strVarValue);
			}
		}
		/* 部门 */
		strVarValue = getBillCardPanel().getHeadItem("cdeptid").getValue();
		refpnl[1] = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
				.getComponent();
		strValue = refpnl[1].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[1].put("cdeptid", strVarValue);
		}
		/* 业务员 */
		strVarValue = getBillCardPanel().getHeadItem("cemployeeid").getValue();
		refpnl[2] = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
				.getComponent();
		strValue = refpnl[2].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[2].put("cemployeeid", strVarValue);
		}
		/* 发运方式 */
		strVarValue = getBillCardPanel().getHeadItem("ctransmodeid").getValue();
		refpnl[3] = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid")
				.getComponent();
		strValue = refpnl[3].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[3].put("ctransmodeid", strVarValue);
		}
		/* 收货人 */
		strVarValue = getBillCardPanel().getHeadItem("creceivepsn").getValue();
		refpnl[4] = (UIRefPane) getBillCardPanel().getHeadItem("creceivepsn")
				.getComponent();
		strValue = refpnl[4].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[4].put("creceivepsn", strVarValue);
		}
		/* 库存组织 */
		strVarValue = getBillCardPanel().getHeadItem("cstoreorganization")
				.getValue();
		refpnl[5] = (UIRefPane) getBillCardPanel().getHeadItem(
				"cstoreorganization").getComponent();
		strValue = refpnl[5].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[5].put("cstoreorganization", strVarValue);
		}
		/* 业务类型 */
		strVarValue = getBillCardPanel().getHeadItem("cbiztype").getValue();
		refpnl[6] = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype")
				.getComponent();
		strValue = refpnl[6].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[6].put("cbiztype", strVarValue);
		}

		Vector v1 = new Vector(), v2 = new Vector(), v3 = new Vector();
		for (int i = 0; i < 7; i++) {
			if (hData[i].size() > 0) {
				v1.addElement(strFormula[i]);
				v2.addElement(hData[i]);
				v3.addElement(new Integer(i));
			}
		}
		if (v1.size() > 0) {
			strFormula = new String[v1.size()];
			v1.copyInto(strFormula);
			hData = new Hashtable[v2.size()];
			v2.copyInto(hData);

			parse.setExpressArray(strFormula);
			parse.setDataSArray(hData);
			String s[] = parse.getValueS();
			if (s != null && s.length == v1.size()) {
				for (int i = 0; i < v1.size(); i++) {
					int j = ((Integer) v3.elementAt(i)).intValue();
					refpnl[j].getUITextField().setText(s[j]);
				}
			}
		}
	}

	public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
		if (e.getPos() == BillItem.HEAD) {
			if (getStateStr().equals("转入列表")) {
				setM_strState("转入修改");
				onCardNew();
			} else {
				// 如果没有单据体，则认为并发并返回
				ArriveorderItemVO[] items = (ArriveorderItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0)
					return;
				//
				isFrmList = true;
				setM_strState("到货浏览");
				onCard();
			}
		}
	}

	/**
	 * 功能:执行审批
	 */
	private void onAudit(ButtonObject bo) {
		try {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000118")/* @res "正在审批..." */);
			ArriveorderVO vo = getCacheVOs()[getDispIndex()];
			// 设置审批人、审批日期
			if (vo == null || vo.getParentVO() == null)
				return;
			// V31SP1:增加审批日期不能小于到货日期限制
			String strErr = PuTool.getAuditLessThanMakeMsg(
					new ArriveorderVO[] { vo }, "dreceivedate",
					"varrordercode", getClientEnvironment().getDate(),
					ScmConst.PO_Arrive);
			if (strErr != null) {
				throw new BusinessException(strErr);
			}
			vo.getParentVO().setAttributeValue("dauditdate",
					getClientEnvironment().getDate());
			vo.getParentVO().setAttributeValue("cauditpsn", getOperatorId());
			vo.getParentVO().setAttributeValue("cuserid", getOperatorId());
			if (getParentCorpCode().equals("10395")&&IsPO(vo.getParentVO().getAttributeValue("cbiztype").toString())&&!(getCorpPrimaryKey().equals("1078")||getCorpPrimaryKey().equals("1108")))//制盖不用直接生成入库单2014-11-28
			// 检验是否需要检验，没进行检验的存货
			{
				if(!IsNotLockAccount(((ArriveorderHeaderVO)vo.getParentVO()).getDauditdate().toString(),((ArriveorderHeaderVO)vo.getParentVO()).getCstoreorganization()))
				{
					MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", "审核期间处于关帐期间，不能入库!Audit period is during the Closing，Not warehousing!");
					return ;
				}
				if(!checkVO(vo))
				{
					 return ;
				}
				String[] body = new String[vo.getChildrenVO().length];
			Hashtable IsChecked = new Hashtable();
			for (int r = 0; r < body.length; r++) {
				
				body[r] = ((ArriveorderItemVO) vo.getChildrenVO()[r])
						.getPrimaryKey();
				
				UFDouble Naccumchecknum = ((ArriveorderItemVO) vo
						.getChildrenVO()[r]).getNaccumchecknum();
				UFDouble narrvnum = ((ArriveorderItemVO) vo.getChildrenVO()[r])
						.getNarrvnum();
				boolean value = false;
				Naccumchecknum=Naccumchecknum==null?new UFDouble("0"):Naccumchecknum;
				UFDouble Zero = new UFDouble("0");
				if (narrvnum.compareTo(Naccumchecknum) > 0
						&& Naccumchecknum.compareTo(Zero) >= 0) {
					value = true;
				}
				IsChecked.put(body[r], value);
			}
			ArrayList StoreByChk = ArriveorderHelper.getStoreByChkArray(body);
			Iterator rstCheck = StoreByChk.iterator();
			int n = 0;
			boolean IsCalcInNum = false;
			while (rstCheck.hasNext()) {
				UFBoolean IsCheck = (UFBoolean) rstCheck.next();
				if (IsCheck.equals(new UFBoolean("Y"))) {
					IsCalcInNum = true;
					if ((Boolean) IsChecked.get(body[n])) {
						MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", Transformations.getLstrFromMuiStr("单号@Odd numbers&"
								+ vo.getHeadVO().getVarrordercode()
								+ "&有未检验的存货，请检验后再审核!@Inspection inventory，Please test after audit!"));
						showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000121"));
						return;
					}
					
				}
				//add by zwx 2015-8-27 
				n++;
				//end by zwx 

			}

			
			/* 审批 */
			PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive,
					getClientEnvironment().getDate().toString(),
					new ArriveorderVO[] { vo }, null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000119")/* @res "审批未成功" */);
				return;
			}
			/* 审批成功后刷新 */
			refreshVoFieldsByKey(vo, vo.getParentVO().getPrimaryKey());
			UFDate auditdate= ((ArriveorderHeaderVO)vo.getParentVO()).getDauditdate();
			// 推式生成下游单据采购入库
			Object returnobj = null;
			try {
				AggregatedValueObject[] generVO = nc.ui.pf.change.PfUtilUITools
				.runChangeDataAry("23", "45",
						new AggregatedValueObject[] { vo });
		
				String OperUser = getClientEnvironment().getUser()
						.getPrimaryKey();
				String Pk_corp = getClientEnvironment().getCorporation()
						.getPk_corp();
				GeneralBillVO changeVo = (GeneralBillVO) generVO[0];
				GeneralBillVO saveVo = new GeneralBillVO();
				saveVo.setParentVO(changeVo.getHeaderVO());
				saveVo.setLockOperatorid(OperUser);
				String cwarehouse = (String) saveVo.getHeaderVO()
						.getAttributeValue("cproviderid");
				String cstoreorganization = (String) saveVo.getHeaderVO()
						.getAttributeValue("pk_calbody");
				saveVo.getHeaderVO().setStatus(2);
				int voCount = changeVo.getChildrenVO().length;
				saveVo.setChildrenVO(changeVo.getChildrenVO());
				String cwarehouseid = saveVo.getHeaderVO().getCwarehouseid();
				for (int j = 0; j < voCount; j++) {
					GeneralBillItemVO tempvo = (GeneralBillItemVO) changeVo
							.getChildrenVO()[j];
					String cspaceid = setSpace(tempvo.getCsourcebillbid(),
							vo.getChildrenVO());
					String vspacename = setSpaceName(
							tempvo.getCsourcebillbid(), vo.getChildrenVO());
					IsCalcInNum = true;
					GeneralBillItemVO genbo = getAddLocatorVOInItemVO(
							cwarehouseid, cwarehouse, Pk_corp,
							cstoreorganization, cspaceid, vspacename, tempvo,
							IsCalcInNum,j);
					genbo.setDbizdate(auditdate);
					saveVo.setItem(j, genbo);
				}

				returnobj = new PfUtilBO().processAction("SAVE", "45",
						ClientEnvironment.getInstance().getDate().toString(),
						null, saveVo, null);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, "审核到货单Audit to manifest", e.getMessage());
				showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000121"));
			}

			if (returnobj != null) {
				try {
					ArrayList keyList = (ArrayList) ((ArrayList) returnobj)
							.get(1);
					String generVoKey = (String) keyList.get(0);
					GeneralBillVO newVo = new GeneralBillVO();

					QryConditionVO voCond = new QryConditionVO();
					voCond.setQryCond("head.cbilltypecode='45' and head.cgeneralhid='"
							+ generVoKey + "'");
					voCond.setDirty(false);
					ArrayList alListData = (ArrayList) GeneralBillHelper
							.queryBills("45", voCond);
					newVo = (GeneralBillVO) alListData.get(0);
					newVo.setLockOperatorid(getClientEnvironment().getUser()
							.getPrimaryKey());
					String Pk_corp = getClientEnvironment().getCorporation()
							.getPk_corp();
					newVo.getHeaderVO().setStatus(3);
					newVo.getHeaderVO().setFreplenishflag(new UFBoolean("N"));
					new PfUtilBO().processAction("SIGN", "45", newVo
							.getHeaderVO().getDbilldate().toString(), null,
							newVo, null);// 采购入库单签字状态
				} catch (Exception e) {
					SCMEnv.out(e);
					MessageDialog.showErrorDlg(this, "审核到货单Audit to manifest", e.getMessage());
					showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000121"));
				}
			}
		}
			else 
			{
				
				if (getParentCorpCode().equals("10395"))
					// 检验是否需要检验，没进行检验的存货
					{
					String[] body = new String[vo.getChildrenVO().length];
					Hashtable IsChecked = new Hashtable();
					for (int r = 0; r < body.length; r++) {
						
						body[r] = ((ArriveorderItemVO) vo.getChildrenVO()[r])
								.getPrimaryKey();
						
						UFDouble Naccumchecknum = ((ArriveorderItemVO) vo
								.getChildrenVO()[r]).getNaccumchecknum();
						UFDouble narrvnum = ((ArriveorderItemVO) vo.getChildrenVO()[r])
								.getNarrvnum();
						boolean value = false;
						Naccumchecknum=Naccumchecknum==null?new UFDouble("0"):Naccumchecknum;
						UFDouble Zero = new UFDouble("0");
						if (narrvnum.compareTo(Naccumchecknum) > 0
								&& Naccumchecknum.compareTo(Zero) >= 0) {
							value = true;
						}
						IsChecked.put(body[r], value);
					}
					ArrayList StoreByChk = ArriveorderHelper.getStoreByChkArray(body);
					Iterator rstCheck = StoreByChk.iterator();
					int n = 0;
					boolean IsCalcInNum = false;
					while (rstCheck.hasNext()) {
						UFBoolean IsCheck = (UFBoolean) rstCheck.next();
						if (IsCheck.equals(new UFBoolean("Y"))) {
							IsCalcInNum = true;
							if ((Boolean) IsChecked.get(body[n])) {
								MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", Transformations.getLstrFromMuiStr("单号@Odd numbers&"
										+ vo.getHeadVO().getVarrordercode()
										+ "&有未检验的存货，请检验后再审核!@Inspection inventory，Please test after audit!"));
								showHintMessage(m_lanResTool.getStrByID("40040301",
								"UPP40040301-000121"));
								return;
							}
							
						}

					}
					}
				/* 审批 */
				PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive,
						getClientEnvironment().getDate().toString(),
						new ArriveorderVO[] { vo }, null);
				if (!PfUtilClient.isSuccess()) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000119")/* @res "审批未成功" */);
					return;
				}
			}
			
			//
			getCacheVOs()[getDispIndex()] = vo;
			/* 刷新按钮状态 */
			setButtonsState();
			/* 加载单据 */
			try {
				loadDataToCard();
			} catch (Exception e) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000120")/*
											 * @res
											 * "审批成功,但加载单据时出现异常,请刷新界面再进行其它操作"
											 */);
			}
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000236")/* @res "审批成功" */);
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000121")/* @res "出现异常,审批失败" */);
			SCMEnv.out(e);
			if (e instanceof java.rmi.RemoteException
					|| e instanceof BusinessException
					|| e instanceof PFBusinessException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040301", "UPP40040301-000099")/* @res "报错" */, e
						.getMessage());
			}
		}
	}

	/**
	 * @功能：到货区退货-采购
	 */
	private void onBackPo() {
		/* 设置查询条件框 */
		getBackRefUIPo().setQueyDlg(getBackQueDlgPo());
		/* 调用onQuery(),并加载数据到参照界面 */
		int iType = getBackRefUIPo().onQuery();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* 显示参照界面 */
		iType = getBackRefUIPo().showModal();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* 处理返回 */
		if (getBackRefUIPo().getRetVos() == null
				|| getBackRefUIPo().getRetVos().length <= 0)
			return;
		onExitFrmOrd((ArriveorderVO[]) getBackRefUIPo().getRetVos());
	}

	/**
	 * @功能：到货区退货-委外
	 */
	private void onBackSc() {
		/* 设置查询条件框 */
		getBackRefUISc().setQueyDlg(getBackQueDlgSc());
		/* 调用onQuery(),并加载数据到参照界面 */
		int iType = getBackRefUISc().onQuery();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* 显示参照界面 */
		iType = getBackRefUISc().showModal();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* 处理返回 */
		if (getBackRefUISc().getRetVos() == null
				|| getBackRefUISc().getRetVos().length <= 0)
			return;
		onExitFrmOrd((ArriveorderVO[]) getBackRefUISc().getRetVos());
	}

	/**
	 * @功能：选取一个业务类型后处理
	 */
	private void onBusi(ButtonObject bo) {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000122")/* @res "正在初始化业务类型:" */
				+ bo.getHint() + "...");
		/* 重新加载业务类型按钮组 */
		PfUtilClient.retAddBtn(m_btnAdds, getCorpPrimaryKey(),
				nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, bo);
		setButtons(m_aryArrCardButtons);
		m_btnAdds.setEnabled(true);
		updateButton(m_btnAdds);
		/* 刷新处理 */
		updateButtons();
		updateButtonsAll();
		updateUI();
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000123")/* @res "当前操作业务类型:" */+ bo.getHint());
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
		//
		boolean bCardShowing = getBillCardPanel().isShowing();
		//
		if (bCardShowing) {
			onButtonClickedCard(bo);
		} else {
			onButtonClickedList(bo);
		}
	}

	/**
	 * 到货单整单检验功能实现(质量管理启用)。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-21 下午07:20:31
	 */
	private void onCheck() {
		if (getCacheVOs() == null || getCacheVOs().length == 0
				|| getCacheVOs()[getDispIndex()] == null) {
			return;
		}
		Hashtable<String, UFBoolean> hStorByChk = new Hashtable<String, UFBoolean>();

		// 质量管理启用时查询是否根据质量检验结果入库参数
		ArriveorderVO voCurr = getCacheVOs()[getDispIndex()];
		int iLen = voCurr.getBodyLen();
		String[] saBid = new String[iLen];
		for (int i = 0; i < iLen; i++) {
			saBid[i] = voCurr.getBodyVo()[i].getPrimaryKey();
		}
		ArrayList aryTmp = null;
		try {
			aryTmp = ArriveorderHelper.getStoreByChkArray(saBid);
			if (aryTmp != null && aryTmp.size() > 0) {
				for (int i = 0; i < iLen; i++) {
					if (aryTmp.get(i) != null)
						hStorByChk.put(saBid[i], (UFBoolean) aryTmp.get(i));
				}
			}
		} catch (Exception e) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, e.getMessage());
		}
		String strErrInfo = getCacheVOs()[getDispIndex()].judgeCanCheck(
				m_bQcEnabled, hStorByChk);
		if (strErrInfo != null) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, strErrInfo);
			return;
		}

		// 组织回写变量

		String carriveorder_bid = null;
		String carriveorderid = null;
		String carriveorder_bts = null;
		String carriveorderts = null;
		ArrayList aryRewriteNum = new ArrayList();
		Vector<String> vStrLineId = new Vector<String>();
		Vector<String> vStrHeadId = new Vector<String>();
		Vector<String> vStrLineTs = new Vector<String>();
		Vector<String> vStrHeadTs = new Vector<String>();
		int iCnt = voCurr.getBodyLen();
		for (int i = 0; i < iCnt; i++) {
			// 回写用到单据ID及单据行ID
			carriveorder_bid = voCurr.getBodyVo()[i].getCarriveorder_bid();
			carriveorderid = voCurr.getBodyVo()[i].getCarriveorderid();
			carriveorder_bts = voCurr.getBodyVo()[i].getTs();
			carriveorderts = voCurr.getBodyVo()[i].getTsh();
			// 组织回写数据
			UFDouble[] rewriteNum = new UFDouble[3];
			rewriteNum[0] = new UFDouble(0.0);
			rewriteNum[1] = new UFDouble(0.0);
			// 累计检验数据
			rewriteNum[2] = voCurr.getBodyVo()[i].getNarrvnum();
			aryRewriteNum.add(rewriteNum);
			vStrLineId.addElement(carriveorder_bid);
			vStrLineTs.addElement(carriveorder_bts);
			vStrHeadId.addElement(carriveorderid);
			vStrHeadTs.addElement(carriveorderts);
		}

		// 后台调用参数
		ArrayList listPara = new ArrayList();
		/*
		 * 参数说明： 0-----质检是否启用 1..4--生成质检单参数：ArriveorderBO_Client.crtQcBills
		 * 5-----是否回写累计质检数量
		 * 6..15-回写累计质检数量参数：ArriveorderBO_Client.rewriteNaccumchecknumMy
		 */
		listPara.add(0, new UFBoolean(m_bQcEnabled));
		// 登陆日期
		UFDate dBusinessDate = getClientEnvironment().getDate();
		// 服务器时间
		UFDateTime dtServerDateTime = ClientEnvironment.getServerTime();
		// 目标参数
		UFDateTime dtDateTime = new UFDateTime(dBusinessDate, new UFTime(
				dtServerDateTime.getTime()));
		// 修正不在同一事务导致的并发错误，将此方法移到后台
		listPara.add(1, null);
		listPara.add(2, voCurr.getBodyVo());
		listPara.add(3, getOperatorId());
		listPara.add(4, dtDateTime);
		// 是否回写累计质检数量
		listPara.add(5, UFBoolean.TRUE);
		// 组织旧TS数组
		String[] saLineIds = new String[vStrLineId.size()];
		String[] saHeadIds = new String[vStrHeadId.size()];
		String[] saLineTss = new String[vStrLineTs.size()];
		String[] saHeadTss = new String[vStrHeadTs.size()];
		vStrLineId.copyInto(saLineIds);
		vStrHeadId.copyInto(saHeadIds);
		vStrLineTs.copyInto(saLineTss);
		vStrHeadTs.copyInto(saHeadTss);
		//
		listPara.add(6, getCorpPrimaryKey());
		listPara.add(7, UFBoolean.TRUE);// 质量管理启用(不启用时本功能不可用)
		listPara.add(8, saLineIds);
		listPara.add(9, saHeadIds);
		listPara.add(10, aryRewriteNum);
		listPara.add(11, getOperatorId());
		listPara.add(12, saLineTss);
		listPara.add(13, saHeadTss);
		listPara.add(14, new UFBoolean(voCurr.isCheckOver()));// 是否为重复报检(整单报检完成)
		listPara.add(15, UFBoolean.FALSE);// 重构后，将两次前后台操作合并为一次，时间戳没有变化

		try {
			// 重构，修正不在同一个事务导致的并发错误
			String sRet = ArriveorderHelper.crtQcAndRewriteNum(listPara);
			//
			if (sRet != null) {
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303",
								"UPP40040303-000013")/*
													 * @res "本次报检失败："
													 */
								+ sRet);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000060")/* @res "检验失败" */);
				return;
			}
			//
			if (sRet == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000058")/* @res "检验成功" */);
			}
			// 刷新处理(目前做成直接调用刷新，有待优化)
			onRefresh();
			//
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000058")/* @res "检验成功" */);
		} catch (BusinessException b) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, b
					.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000060")/* @res "检验失败" */);
		} catch (Exception b) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, b
					.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000060")/* @res "检验失败" */);
		}
	}

	/**
	 * 卡片按钮事件响应。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-20 下午04:12:37
	 */
	private void onButtonClickedCard(ButtonObject bo) {

		if (bo == m_btnCheck) {
			onCheck();
		} else if (bo.getParent() == m_btnBusiTypes) {
			//
			bo.setSelected(true);
			//
			isChangeBusitype = true;
			onBusi(bo);
		} else if (bo == m_btnDiscard) {
			onDiscard();
		} else if (bo.getParent() == m_btnAdds) {
			// onAdd();
			int iIndexBillType = bo.getTag().indexOf(":");
			String strBillType = bo.getTag().substring(0, iIndexBillType);
			if (strBillType.equals(ScmConst.SC_Order)) {
				if (!nc.ui.sm.user.UserPowerUI.isEnabled(getCorpPrimaryKey(),
						"SC")) {
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000059")/*
																 * @res "错误"
																 */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000124")/*
														 * @res "委外订单模块没有启用！"
														 */);
					return;
				}
			} 
			//add by wbp 2017-6-1 让来料到货流程的单据可以自制
			
			else if(bo.getName().equals("自制单据")){
				// 设置新增单据的初始数据，如日期，制单人等。
				getBillCardPanel().setEnabled(true);
				this.m_btnLines.setEnabled(true);
				this.m_btnLines.setEnabled(true);
				this.m_btnDelLine.setEnabled(true);
				this.m_btnCpyLine.setEnabled(true);
				this.m_btnPstLine.setEnabled(true);
				this.m_btnAdds.setEnabled(false);
				this.m_btnSave.setEnabled(true);
				this.m_btnCancel.setEnabled(true);
				this.m_btnQuery.setEnabled(false);  //查询
				this.m_btnBrowses.setEnabled(false);  //浏览
				getBillCardPanel().addNew();
				getBillCardPanel().setEnabled(true);
				return;
			}
			//end add by wbp
			
			else if (!strBillType.equals("21")) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000125")/*
													 * @res "到货单只能由采购订单或委外订单生成！"
													 */);
				return;
			}
			PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(),
					getFuncId(), getOperatorId(), ScmConst.PO_Arrive, this);
			if (PfUtilClient.isCloseOK()) {
				ArriveorderVO[] retVOs = (ArriveorderVO[]) PfUtilClient
						.getRetVos();
				onExitFrmOrd(retVOs);
			}
		} else if (bo == m_btnBackPo) {
			onBackPo();
		} else if (bo == m_btnBackSc) {
			onBackSc();
		} else if (bo == m_btnLocate) {
			onLocate();
		} else if (bo == m_btnPrint) {
			// 打印并计算打印次数
			onCardPrint();
		} else if (bo == m_btnCombin) {
			// 合并显示打印
			onCombin();
		} else if (bo == m_btnPrintPreview) {
			// 打印预览并计算打印次数
			onCardPrintPreview();
		} else if (bo == m_btnList) {
			onList();
		} else if (bo == m_btnModify) {
			onModify();
			// 置光标到表头第一个可编辑项目
			getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		} else if (bo == m_btnDelLine) {
			onDeleteLine();
		} else if (bo == m_btnCpyLine) {
			onCopyLine();
		} else if (bo == m_btnPstLine) {
			onPasteLine();
		} else if (bo == m_btnSave) {
			onSave();
			
			
			
			
		} else if (bo == m_btnCancel) {
			onCancel();
		} else if (bo == m_btnQuery) {
			onQuery();
		} else if (bo == m_btnFirst) {
			onFirst();
		} else if (bo == m_btnPrev) {
			onPrevious();
		} else if (bo == m_btnNext) {
			onNext();
		} else if (bo == m_btnLast) {
			onLast();
		} else if (bo == m_btnRefresh) {
			onRefresh();
		} else if (bo.getParent() == m_btnActions) {
			if ("APPROVE".equals(bo.getTag())) {
				onAudit(bo);
			} else if ("UNAPPROVE".equals(bo.getTag())) {
				onUnAudit(bo);
			}
		} else if (bo == m_btnUsable) {
			onQueryInvOnHand();
		} else if (bo == m_btnQueryBOM) {
			onQueryBOM();
		} else if (bo == m_btnDocument) {
			onDocument();
		} else if (bo == m_btnLookSrcBill) {
			onLnkQuery();
		} else if (bo == m_btnQuickReceive) {
			onQuickArr();
		}else if (bo == m_btnImportBill){
			onImportBill();
			
		}else if (bo == m_btnImportXml){
			onImportXml();     //XML导入  yqq 2016-11-02 测试
		}
		/* 以下V5支持审批流****************************** */
		else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if (bo == m_btnAudit) {
			onAudit(bo);
		} else if (bo == m_btnUnAudit) {
			onUnAudit(bo);
		} else if (bo == m_btnQueryForAudit) {
			onQueryForAudit();
			/* 以上V5支持审批流***************************** */
		}
		// 支持产业链功能扩展
		else if (PuTool.isExist(getExtendBtns(), bo)) {
			onExtendBtnsClick(bo);
		}
	}
	
	/*
	   * 功能：单据导入
	   * wkf
	   * 2014-09-09
	   * start
	   * 
	   */
		public static void creatFile(String sourceFile){
	      try {
	          /** 创建只读的Excel工作薄的对象*/
	          w = Workbook.getWorkbook(new File(sourceFile));            
	      } catch (BiffException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
		}
		int res = 0 ;
		private Object sbs;
		
		public  void onImportBill(){//BusinessException
			String Pk_corp = getClientEnvironment().getCorporation().getPk_corp();
			if(!(Pk_corp.trim().equals("1078")||getCorpPrimaryKey().equals("1108"))){
				return;
			}
			int isok = showYesNoCancelMessage("需要导入辅数量吗?\n注:模版上的主辅数量不可为空,请用0占格！");
			if(isok == 4){
				try {
					nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
					fileChooser.setAcceptAllFileFilterUsed(true);
					res = fileChooser.showOpenDialog(this);
					File txtFile = null;
					if (res == 0) {
						txtFile = fileChooser.getSelectedFile();
						String filepath = txtFile.getAbsolutePath();
						
						if(!filepath.endsWith(".xls")){
							showErrorMessage("请选择以.xls结尾的(2003版)Excel文件!");
							return;
						}
						creatFile(filepath);
						ArrayList list = new ArrayList();
						Sheet ws = w.getSheet(0);//获取到sheet对象
						rows = ws.getRows();//行数
						ArrayList cwcslist = new ArrayList();
						for (int i = 1; i < rows; i++) {
							Cell[] cells = ws.getRow(i);
							List al = new ArrayList();//装货位0仓库1
							ArriveorderItemVO avo = new ArriveorderItemVO();
							String cinventorycode = cells[0].getContents().trim();//存货编码
							String vproducenum = cells[1].getContents();//批次号
							String cstorename = cells[2].getContents().trim();//货位
							al.add(cstorename);
							String cwarehousename = cells[3].getContents().trim();//收货仓库
							al.add(cwarehousename);
							String vfree1 = cells[4].getContents();//自由项
							String narrvnum1 = cells[5].getContents();
							UFDouble narrvnum = new UFDouble(narrvnum1);//数量
							String nassistnum1 = cells[6].getContents();
							UFDouble nassistnum = new UFDouble(nassistnum1);//辅数量
							
							avo.setCbaseid(cinventorycode);
							avo.setVproducenum(vproducenum);
							avo.setCstoreid(cstorename);
							avo.setCwarehouseid(cwarehousename);
							avo.setVfree1(vfree1);
							avo.setNarrvnum(narrvnum);
							avo.setNassistnum(nassistnum);
							cwcslist.add(al);
							list.add(avo);
						}
						Map cwarehouseid1 = getCwarehouseid1(cwcslist);//获得所有仓库的pk
						Map cstorepk = null;
						if(!cwarehouseid1.equals(null) || cwarehouseid1 != null){
							cstorepk = getCstoreidList(cwcslist);//获得所有货位的pk值
						}
						Map vomap = cloneBodyVo();//获得表体所有vo克隆
						BillModel bodyitems = getBillCardPanel().getBillModel();
						if (list.size()>0 && list !=null) {
							
							int ss = getBillCardPanel().getBillModel().getRowCount();
							int[] dellineall = new int[ss];
							for (int w = 0; w < ss; w++) {
								dellineall[w] = w;
							}
							getBillCardPanel().getBillModel().delLine(dellineall);//删除表体数据
							
							//getBillCardPanel().getBillModel().clearBodyData();
							for (int i = 0; i < list.size(); i++) {
								
								ArriveorderItemVO voi =  (ArriveorderItemVO) list.get(i);
								getBillCardPanel().getBillModel().addLine();
								String cinventorycode = voi.getCbaseid();
								ArriveorderItemVO bvoi = (ArriveorderItemVO) vomap.get(cinventorycode);
								bvoi.setStatus(VOStatus.NEW);
								
								getBillCardPanel().getBillModel().setBodyRowVO(bvoi, i);
								
								String vproducenum = voi.getVproducenum();
								String cstore = voi.getCstoreid();
								String cstoreid = (String) cstorepk.get(cstore);
								String cwarehouse = voi.getCwarehouseid();
								String cwarehouseid = (String) cwarehouseid1.get(cwarehouse);//仓库主键
								String vfree0 = voi.getVfree1();
								UFDouble narrvnum = voi.getNarrvnum();
								UFDouble nassistnum = voi.getNassistnum();
								
								int newrow = (i+1)*10;
								String newrowno = String.valueOf(newrow);
								bodyitems.setValueAt(newrowno, i, "crowno");
								bodyitems.setValueAt(cinventorycode, i, "cinventorycode");
								bodyitems.setValueAt(vproducenum, i, "vproducenum");
								bodyitems.setValueAt(cstoreid, i, "cstoreid");
								bodyitems.setValueAt(cstore, i, "cstorename");
//								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
//								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
								bodyitems.setValueAt(vfree0, i, "vfree0");
								bodyitems.setValueAt(vfree0, i, "vfree1");
								//处理主辅数量计算
								if(narrvnum.compareTo(new UFDouble(0))>0){
									bodyitems.setValueAt(narrvnum, i, "narrvnum");
								}
								if(nassistnum.compareTo(new UFDouble(0))>0){
									Object value = bodyitems.getValueAt(i, "convertrate");//转换率
									if(value != null){
										UFDouble convertrate = new UFDouble(value.toString());
										narrvnum = nassistnum.multiply(convertrate);
										bodyitems.setValueAt(narrvnum, i, "narrvnum");
										bodyitems.setValueAt(nassistnum, i, "nassistnum");
									}else{
										showErrorMessage("存货编码:"+cinventorycode+"没有转换率,请维护后重新导入!");
									}
								}
								//项目主键	nprice项目主键	nmoney
								Object danjia = bodyitems.getValueAt(i, "nprice");
								if(danjia != null){
									UFDouble nprice = new UFDouble(danjia.toString());
									UFDouble nmoney = narrvnum.multiply(nprice);
									bodyitems.setValueAt(nmoney, i, "nmoney");
								}else{
									showErrorMessage("存货编码:"+cinventorycode+"没有单价,请维护后重新导入!");
								}
								getBillCardPanel().getBillModel().execEditFormulas(i);
								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					String Error ="文件导入失败！\n"+e.toString();
					showErrorMessage(Error);
				}
			}else if(isok == 8){
				try {
					nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
					fileChooser.setAcceptAllFileFilterUsed(true);
					res = fileChooser.showOpenDialog(this);
					File txtFile = null;
					if (res == 0) {
						txtFile = fileChooser.getSelectedFile();
						String filepath = txtFile.getAbsolutePath();
						
						if(!filepath.endsWith(".xls")){
							showErrorMessage("请选择以.xls结尾的(2003版)Excel文件!");
							return;
						}
						creatFile(filepath);
						ArrayList list = new ArrayList();
						Sheet ws = w.getSheet(0);//获取到sheet对象
						rows = ws.getRows();//行数
						ArrayList cwcslist = new ArrayList();
						for (int i = 1; i < rows; i++) {
							Cell[] cells = ws.getRow(i);
							List al = new ArrayList();//装货位0仓库1
							ArriveorderItemVO avo = new ArriveorderItemVO();
							String cinventorycode = cells[0].getContents().trim();//存货编码
							String vproducenum = cells[1].getContents();//批次号
							String cstorename = cells[2].getContents().trim();//货位
							al.add(cstorename);
							String cwarehousename = cells[3].getContents().trim();//收货仓库
							al.add(cwarehousename);
							String vfree1 = cells[4].getContents();//自由项
							String narrvnum1 = cells[5].getContents();
							UFDouble narrvnum = new UFDouble(narrvnum1);//数量
							
							avo.setCbaseid(cinventorycode);
							avo.setVproducenum(vproducenum);
							avo.setCstoreid(cstorename);
							avo.setCwarehouseid(cwarehousename);
							avo.setVfree1(vfree1);
							avo.setNarrvnum(narrvnum);
							cwcslist.add(al);
							list.add(avo);
						}
						Map cwarehouseid1 = getCwarehouseid1(cwcslist);//获得所有仓库的pk
						Map cstorepk = null;
						if(!cwarehouseid1.equals(null) || cwarehouseid1 != null){
							cstorepk = getCstoreidList(cwcslist);//获得所有货位的pk值
						}
						Map vomap = cloneBodyVo();//获得表体所有vo克隆
						BillModel bodyitems = getBillCardPanel().getBillModel();
						if (list.size()>0 && list !=null) {
							
							int ss = getBillCardPanel().getBillModel().getRowCount();
							int[] dellineall = new int[ss];
							for (int w = 0; w < ss; w++) {
								dellineall[w] = w;
							}
							getBillCardPanel().getBillModel().delLine(dellineall);//删除表体数据
							
							//getBillCardPanel().getBillModel().clearBodyData();
							for (int i = 0; i < list.size(); i++) {
								
								ArriveorderItemVO voi =  (ArriveorderItemVO) list.get(i);
								getBillCardPanel().getBillModel().addLine();
								String cinventorycode = voi.getCbaseid();
								ArriveorderItemVO bvoi = (ArriveorderItemVO) vomap.get(cinventorycode);
								bvoi.setStatus(VOStatus.NEW);
								
								getBillCardPanel().getBillModel().setBodyRowVO(bvoi, i);
								
								String vproducenum = voi.getVproducenum();
								String cstore = voi.getCstoreid();
								String cstoreid = (String) cstorepk.get(cstore);
								String cwarehouse = voi.getCwarehouseid();
								String cwarehouseid = (String) cwarehouseid1.get(cwarehouse);//仓库主键
								String vfree0 = voi.getVfree1();
								UFDouble narrvnum = voi.getNarrvnum();
								
								int newrow = (i+1)*10;
								String newrowno = String.valueOf(newrow);
								bodyitems.setValueAt(newrowno, i, "crowno");
								bodyitems.setValueAt(cinventorycode, i, "cinventorycode");
								bodyitems.setValueAt(vproducenum, i, "vproducenum");
								bodyitems.setValueAt(cstoreid, i, "cstoreid");
								bodyitems.setValueAt(cstore, i, "cstorename");
//								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
//								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
								bodyitems.setValueAt(vfree0, i, "vfree0");
								bodyitems.setValueAt(vfree0, i, "vfree1");
								bodyitems.setValueAt(narrvnum, i, "narrvnum");
								bodyitems.setValueAt(null, i, "nassistnum");
								//项目主键	nprice项目主键	nmoney
								Object danjia = bodyitems.getValueAt(i, "nprice");
								if(danjia != null){
									UFDouble nprice = new UFDouble(danjia.toString());
									UFDouble nmoney = narrvnum.multiply(nprice);
									bodyitems.setValueAt(nmoney, i, "nmoney");
								}else{
									showErrorMessage("存货编码:"+cinventorycode+"没有单价,请维护后重新导入!");
								}
								getBillCardPanel().getBillModel().execEditFormulas(i);
								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					String Error ="文件导入失败！\n"+e.toString();
					showErrorMessage(Error);
				}
			}
			
	       
		}
		
		private Map cloneBodyVo() {
			Map vomap = new HashMap<String, Object>();
			int inum = getBillCardPanel().getRowCount();
			for (int i = 0; i <inum; i++) {
				Object invobj = getBillCardPanel().getBodyValueAt(i, "cinventorycode");
				if(!invobj.equals(null)){
					String invcode = invobj.toString();
					ArriveorderItemVO clonebodyvo =null;
					clonebodyvo = (ArriveorderItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(i, ArriveorderItemVO.class.getName());
					clonebodyvo = (ArriveorderItemVO) clonebodyvo.clone();
					vomap.put(invcode, clonebodyvo);
				}
				
			}
			return vomap;

		}
		
		private Map getCstoreidList(ArrayList vspacename) {
			Map map = new HashMap<String,String>();
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			for (int i = 0,len=vspacename.size(); i < len; i++) {
				List sss = (List) vspacename.get(i);
				String ss = (String) sss.get(0);
				if(!ss.equals("") || ss != ""){
					sb.append("'");
					sb1.append("'");
					sb.append(ss);
					sb1.append(sss.get(1));
					sb.append("'");
					sb1.append("'");
					if(i!=len-1){
						sb.append(",");
						sb1.append(",");
					}
				}
			}
			String sbs = sb.toString();
			if(!sbs.equals("")){
//				String sqlg ="select csname,pk_cargdoc　from bd_cargdoc where csname in ("+sb.toString()+")"; 
				StringBuffer sqlg = new StringBuffer();
				sqlg.append(" select car.csname,car.pk_cargdoc　from bd_cargdoc car ") 
				.append("        left join  bd_stordoc sto ") 
				.append("        on car.pk_stordoc = sto.pk_stordoc ") 
				.append("        where car.csname in ("+sb.toString()+")  ") 
				.append("        and sto.storname in ("+sb1.toString()+") ") ;
				IUAPQueryBS queryid = (IUAPQueryBS) NCLocator
						.getInstance().lookup(IUAPQueryBS.class.getName());
				Object obj = null;
				try {
					obj = queryid.executeQuery(sqlg.toString(), new ArrayListProcessor());
				} catch (FTSBusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (obj instanceof ArrayList) {
					ArrayList list = (ArrayList) obj;
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);
						map.put(objs[0].toString(),objs[1].toString());
					}
				}
			}
			return map;
			
		}
		//获取仓库id
		private Map getCwarehouseid1(ArrayList vspacename) {
			Map map = new HashMap<String,String>();
			StringBuffer sb = new StringBuffer();
			for (int i = 0,len=vspacename.size(); i < len; i++) {
				sb.append("'");
				List sss= (List) vspacename.get(i);
				sb.append(sss.get(1));
				sb.append("'");
				if(i!=len-1){
					sb.append(",");
				}
			}
			String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey().toString();
			String sqlg ="select storname,pk_stordoc from bd_stordoc where nvl(dr,0)=0 and pk_corp = '"+pk_corp+"' and storname in ("+sb.toString()+")"; 
			IUAPQueryBS queryid = (IUAPQueryBS) NCLocator
					.getInstance().lookup(IUAPQueryBS.class.getName());
			Object obj = null;
			try {
				obj = queryid.executeQuery(sqlg.toString(), new ArrayListProcessor());
			} catch (FTSBusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (obj instanceof ArrayList) {
				ArrayList list = (ArrayList) obj;
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = (Object[]) list.get(i);
					map.put(objs[0].toString(),objs[1].toString());
				}
			}
			return map;
			
		}
		/*
		 * 功能：单据导入
		 * wkf
		 * 2014-09-09
		 * end
		 * 
		 */
	/**
	 * 列表按钮事件响应。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-20 下午04:13:02
	 */
	private void onButtonClickedList(ButtonObject bo) {

		if (bo == m_btnPrintList) {
			// 批打印
			onBatchPrint();
		} else if (bo == m_btnPrintPreviewList) {
			// 批打印预览
			onBatchPrintPreview();
		} else if (bo == m_btnDiscardList) {
			onDiscardSelected();
		} else if (bo == m_btnCard) {
			onCard();
		} else if (bo == m_btnModifyList) {
			if (getStateStr().equals("转入列表")) {
				onCardNew();
			} else {
				onModifyList();
			}
			// 置光标到表头第一个可编辑项目
			getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		} else if (bo == m_btnEndCreate) {
			onEndCreate();
		} else if (bo == m_btnQueryList) {
			onQuery();
		} else if (bo == m_btnSelectAll) {
			onSelectAll();
		} else if (bo == m_btnSelectNo) {
			onSelectNo();
		} else if (bo == m_btnRefreshList) {
			onRefresh();
		} else if (bo == m_btnUsableList) {
			onQueryInvOnHand();
		} else if (bo == m_btnQueryBOMList) {
			onQueryBOM();
		} else if (bo == m_btnDocumentList) {
			onDocument();
		} else if (bo == m_btnAudit) {
			onAuditList(bo);
		} else if (bo == m_btnUnAudit) {
			onUnAuditList(bo);
		} else if (PuTool.isExist(getExtendBtns(), bo)) {
			onExtendBtnsClick(bo);
			
		}else if (bo == m_btnImportXml){
			onImportXml();     //XML导入  yqq 2016-11-02 测试
		}
	}

	/**
	 * 批量审批功能。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-27 下午01:17:02
	 */
	private void onAuditList(ButtonObject bo) {

		Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
		ArriveorderHeaderVO head = new ArriveorderHeaderVO();
		// 处理排过序后的缓存索引
		int iRealPos = 0;
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iRealPos = i;
				iRealPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iRealPos);
				// 审批人、审批日期
				head = (ArriveorderHeaderVO) getCacheVOs()[iRealPos]
						.getParentVO();
				// 审批人是操作员
				head.setCauditpsn(getOperatorId());
				head.setDauditdate(PoPublicUIClass.getLoginDate());
				vSubVos.add(getCacheVOs()[iRealPos]);
			}
		}
		ArriveorderVO[] arrivevos = null;
		if (vSubVos.size() > 0) {
			arrivevos = new ArriveorderVO[vSubVos.size()];
			vSubVos.copyInto(arrivevos);
		}
		try {
			// 审批
			// ArriveorderBO_Client.auditArriveorderMy(arrivevos);
			// 设置操作员
			for (int i = 0; i < arrivevos.length; i++) {
				arrivevos[i].getParentVO().setAttributeValue("cuserid",
						getOperatorId());
			}
			boolean isSucc = false;
			try {
				// V31SP1:增加审批日期不能小于到货日期限制
				String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos,
						"dreceivedate", "varrordercode", ClientEnvironment
								.getInstance().getDate(), ScmConst.PO_Arrive);
				if (strErr != null) {
					throw new BusinessException(strErr);
				}
				// 审批前处理表体
				ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];

				for (int i = 0; i < arrivevos.length; i++) {
					heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();

				}
				// 加载未浏览过的单据体
				// arrivevos = ArriveorderBO_Client.getAllWithBody(heads);
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				if(getParentCorpCode().equals("10395"))
				// 检验是否需要检验，没进行检验的存货
				{int ChkVO = 0;
				Hashtable IsCheckDoc = new Hashtable();
				ArrayList arrvo=new ArrayList();
				ArriveorderVO [] checkVO=null;
				for (int j = 0; j < arrivevos.length; j++) {
					UFDate Dauditdate=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getDauditdate();
					String billno=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getVarrordercode();
					String calbody=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getCstoreorganization();
					if(checkVO(arrivevos[j]))
					{
						continue;
					}
					if(!IsNotLockAccount(Dauditdate.toString(),calbody))
					{
						MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", "审核期间处于关帐期间，不能入库!Audit period is during the Closing，Not warehousing!");
						continue;
					}
					arrvo.add(arrivevos[j]);
				
				}
				checkVO=new ArriveorderVO [arrvo.size()];
				checkVO=(ArriveorderVO[])arrvo.toArray();
				boolean IsNeedCheck = false;
				for(int k=0;k<arrvo.size();k++)
				{	String[] body = new String[checkVO[k].getChildrenVO().length];
					Hashtable IsChecked = new Hashtable();
					for (int r = 0; r < body.length; r++) {
						body[r] = ((ArriveorderItemVO) checkVO[k]
								.getChildrenVO()[r]).getPrimaryKey();
						UFDouble Naccumchecknum = ((ArriveorderItemVO) checkVO[k]
								.getChildrenVO()[r]).getNaccumchecknum();
						UFDouble narrvnum = ((ArriveorderItemVO) checkVO[k]
								.getChildrenVO()[r]).getNarrvnum();
						boolean value = false;
						Naccumchecknum=Naccumchecknum==null?new UFDouble("0"):Naccumchecknum;
						UFDouble Zero = new UFDouble("0");
						if (narrvnum.compareTo(Naccumchecknum) > 0
								&& Naccumchecknum.compareTo(Zero) >= 0) {
							value = true;
						}
						IsChecked.put(body[r], value);
					}
					ArrayList StoreByChk = ArriveorderHelper
							.getStoreByChkArray(body);
					Iterator rstCheck = StoreByChk.iterator();
					int n = 0;
					boolean IsCalcInNum = false;
					while (rstCheck.hasNext()) {
						UFBoolean IsCheck = (UFBoolean) rstCheck.next();
						if (IsCheck.equals(new UFBoolean("Y"))) {
							IsCalcInNum = true;
							if ((Boolean) IsChecked.get(body[n])) {
								MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", Transformations.getLstrFromMuiStr("单号@Odd numbers&"
										+ checkVO[k].getHeadVO()
												.getVarrordercode()
										+ "&有未检验的存货，请检验后再审核!@Inspection inventory，Please test after audit!"));

								// return;
								IsNeedCheck = true;
							}
							ChkVO++;
						}

					}
					IsCheckDoc.put(checkVO[k].getHeadVO().getPrimaryKey(),
							IsNeedCheck);
				}
				ArriveorderVO[] newArrivevos = new ArriveorderVO[checkVO.length
						- ChkVO];
				int Start = 0;
				for (int i = 0; i < checkVO.length; i++) {
					if (checkVO[i] != null) {
						newArrivevos[Start] = checkVO[i];
						Start++;
					}
				}
				if (newArrivevos.length <= 0) {
					return;
				}
				// 推式生成下游单据采购入库
				for (int i = 0; i < newArrivevos.length; i++) {
					Object returnobj = null;
			
				PfUtilClient.processBatchFlow(this, "APPROVE",
						ScmConst.PO_Arrive, ClientEnvironment.getInstance()
								.getDate().toString(), new ArriveorderVO[]{newArrivevos[i]}, null);
				
				isSucc = PfUtilClient.isSuccess();
				if (isSucc) {
					// 刷新前端显示内容
					displayOthersVOs(vSubVos);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "4004COMMON000000071")/*
																		 * @res
																		 * "审核成功"
																	 */);
					if(!IsPO(String.valueOf(newArrivevos[i].getParentVO().getAttributeValue("cbiztype"))))
					{
						continue;
					}
		
					refreshVoFieldsByKey(newArrivevos[i], newArrivevos[i]
								.getParentVO().getPrimaryKey());
					UFDate auditdate= ((ArriveorderHeaderVO)newArrivevos[i].getParentVO()).getDauditdate();
						try {
							AggregatedValueObject[] generVO = nc.ui.pf.change.PfUtilUITools
							.runChangeDataAry(
									"23",
									"45",
									new AggregatedValueObject[] { newArrivevos[i] });
	
							String OperUser = getClientEnvironment().getUser()
									.getPrimaryKey();
							String Pk_corp = getClientEnvironment()
									.getCorporation().getPk_corp();
							GeneralBillVO changeVo = (GeneralBillVO) generVO[0];
							GeneralBillVO saveVo = new GeneralBillVO();
							saveVo.setParentVO(changeVo.getHeaderVO());
							saveVo.setLockOperatorid(OperUser);
							String cwarehouse = (String) saveVo.getHeaderVO()
									.getAttributeValue("cproviderid");
							String cstoreorganization = (String) saveVo
									.getHeaderVO().getAttributeValue(
											"pk_calbody");
							saveVo.getHeaderVO().setStatus(2);
							int voCount = changeVo.getChildrenVO().length;
							saveVo.setChildrenVO(changeVo.getChildrenVO());
							String cwarehouseid = saveVo.getHeaderVO()
									.getCwarehouseid();
							Boolean IsCalcInNum = (Boolean) IsCheckDoc
									.get(newArrivevos[0].getHeadVO()
											.getPrimaryKey());
							for (int j = 0; j < voCount; j++) {
								GeneralBillItemVO tempvo = (GeneralBillItemVO) changeVo
										.getChildrenVO()[j];
								String cspaceid = setSpace(
										tempvo.getCsourcebillbid(),
										newArrivevos[0].getChildrenVO());
								String vspacename = setSpaceName(
										tempvo.getCsourcebillbid(),
										newArrivevos[0].getChildrenVO());

								GeneralBillItemVO genbo = getAddLocatorVOInItemVO(
										cwarehouseid, cwarehouse, Pk_corp,
										cstoreorganization, cspaceid,
										vspacename, tempvo, IsCalcInNum,j);
								genbo.setDbizdate(auditdate);
								saveVo.setItem(j, genbo);
							}

							returnobj = new PfUtilBO().processAction("SAVE",
									"45", ClientEnvironment.getInstance()
											.getDate().toString(), null,
											saveVo, null);
						} catch (Exception e) {
							SCMEnv.out(e);
							MessageDialog.showErrorDlg(this, "审核到货单Audit to manifest",
									e.getMessage());
						}

						if (returnobj != null) {
							try {
								ArrayList keyList = (ArrayList) ((ArrayList) returnobj)
										.get(1);
								String generVoKey = (String) keyList.get(0);
								GeneralBillVO newVo = new GeneralBillVO();

								QryConditionVO voCond = new QryConditionVO();
								voCond.setQryCond("head.cbilltypecode='45' and head.cgeneralhid='"
										+ generVoKey + "'");
								voCond.setDirty(false);
								ArrayList alListData = (ArrayList) GeneralBillHelper
										.queryBills("45", voCond);
								newVo = (GeneralBillVO) alListData.get(0);
								newVo.setLockOperatorid(getClientEnvironment()
										.getUser().getPrimaryKey());
								String Pk_corp = getClientEnvironment()
										.getCorporation().getPk_corp();
								newVo.getHeaderVO().setStatus(3);
								newVo.getHeaderVO().setFreplenishflag(
										new UFBoolean("N"));
								new PfUtilBO().processAction("SIGN", "45",
										newVo.getHeaderVO().getDbilldate()
												.toString(), null, newVo, null);// 采购入库单签字状态
							} catch (Exception e) {
								SCMEnv.out(e);
								MessageDialog.showErrorDlg(this, "审核到货单Audit to manifest",
										e.getMessage());
							}
						}
					}
				else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "4004COMMON000000072")/*
																		 * @res
																		 * "审核失败"
																		 */);
				}
				} 
				}
				else
				{
					if(getParentCorpCode().equals("10395"))
					{
						int ChkVO = 0;
						Hashtable IsCheckDoc = new Hashtable();
						ArrayList arrvo=new ArrayList();
						ArriveorderVO [] checkVO=null;
						for (int j = 0; j < arrivevos.length; j++) {
							UFDate Dauditdate=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getDauditdate();
							String billno=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getVarrordercode();
							String calbody=((ArriveorderHeaderVO)arrivevos[j].getParentVO()).getCstoreorganization();
							if(checkVO(arrivevos[j]))
							{
								continue;
							}
							if(!IsNotLockAccount(Dauditdate.toString(),calbody))
							{
								MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", "审核期间处于关帐期间，不能入库!Audit period is during the Closing，Not warehousing!");
								continue;
							}
							arrvo.add(arrivevos[j]);
						
						}
						checkVO=new ArriveorderVO [arrvo.size()];
						arrvo.toArray(checkVO);
						boolean IsNeedCheck = false;
						for(int k=0;k<arrvo.size();k++)
						{	String[] body = new String[checkVO[k].getChildrenVO().length];
							Hashtable IsChecked = new Hashtable();
							for (int r = 0; r < body.length; r++) {
								body[r] = ((ArriveorderItemVO) checkVO[k]
										.getChildrenVO()[r]).getPrimaryKey();
								UFDouble Naccumchecknum = ((ArriveorderItemVO) checkVO[k]
										.getChildrenVO()[r]).getNaccumchecknum();
								UFDouble narrvnum = ((ArriveorderItemVO) checkVO[k]
										.getChildrenVO()[r]).getNarrvnum();
								boolean value = false;
								Naccumchecknum=Naccumchecknum==null?new UFDouble("0"):Naccumchecknum;
								UFDouble Zero = new UFDouble("0");
								if (narrvnum.compareTo(Naccumchecknum) > 0
										&& Naccumchecknum.compareTo(Zero) >= 0) {
									value = true;
								}
								IsChecked.put(body[r], value);
							}
							ArrayList StoreByChk = ArriveorderHelper
									.getStoreByChkArray(body);
							Iterator rstCheck = StoreByChk.iterator();
							int n = 0;
							boolean IsCalcInNum = false;
							while (rstCheck.hasNext()) {
								UFBoolean IsCheck = (UFBoolean) rstCheck.next();
								if (IsCheck.equals(new UFBoolean("Y"))) {
									IsCalcInNum = true;
									if ((Boolean) IsChecked.get(body[n])) {
										MessageDialog.showErrorDlg(this, "采购到货单Procurement arrival", Transformations.getLstrFromMuiStr("单号@Odd numbers&"
												+ checkVO[k].getHeadVO()
														.getVarrordercode()
												+ "&有未检验的存货，请检验后再审核!@Inspection inventory，Please test after audit!"));
	
										// return;
										IsNeedCheck = true;
									}
									ChkVO++;
								}
	
							}
							IsCheckDoc.put(checkVO[k].getHeadVO().getPrimaryKey(),
									IsNeedCheck);
						}
						ArriveorderVO[] newArrivevos = new ArriveorderVO[checkVO.length
								- ChkVO];
						int Start = 0;
						for (int i = 0; i < checkVO.length; i++) {
							if (checkVO[i] != null) {
								newArrivevos[Start] = checkVO[i];
								Start++;
							}
						}
						if (newArrivevos.length <= 0) {
							return;
						}
						PfUtilClient.processBatchFlow(this, "APPROVE",
								ScmConst.PO_Arrive, ClientEnvironment.getInstance()
										.getDate().toString(), newArrivevos, null);
					}
					else
					{	PfUtilClient.processBatchFlow(this, "APPROVE",
							ScmConst.PO_Arrive, ClientEnvironment.getInstance()
									.getDate().toString(), arrivevos, null);
					}
					if (isSucc) {
						// 刷新前端显示内容
						displayOthersVOs(vSubVos);
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("common", "4004COMMON000000071")/*
																			 * @res
																			 * "审核成功"
																			 */);
					}
					else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("common", "4004COMMON000000072")/*
																			 * @res
																			 * "审核失败"
																			 */);
					}
				}
			} catch (nc.vo.pub.BusinessException e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000072")/* @res "审核失败" */);
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000422")/*
																	 * @res
																	 * "业务异常"
																	 */,
						e.getMessage());
			} catch (Exception e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000072")/* @res "审核失败" */);
				if (e instanceof java.rmi.RemoteException) {
					MessageDialog.showErrorDlg(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000422")/*
																		 * @res
																		 * "业务异常"
																		 */,
							e.getMessage());
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000072")/* @res "审核失败" */);
		}
	}

	/**
	 * 批量弃审。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-27 下午01:30:13
	 */
	private void onUnAuditList(ButtonObject bo) {

		Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
		int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
		BillModel bm = getBillListPanel().getHeadBillModel();
		ArriveorderVO vo = null;
		ArriveorderVO[] arrivevos = null;
		// 处理排过序后的缓存索引
		int iRealPos = 0;
		for (int i = 0; i < rowCount; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED) {
				iRealPos = i;
				iRealPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iRealPos);
				vo = getCacheVOs()[iRealPos];
				vSubVos.add(vo);
			}
		}
		if (vSubVos.size() > 0) {
			arrivevos = new ArriveorderVO[vSubVos.size()];
			vSubVos.copyInto(arrivevos);
			try {
				// 设置操作员
				for (int i = 0; i < arrivevos.length; i++) {
					arrivevos[i].getParentVO().setAttributeValue("cuserid",
							getOperatorId());
				}
				// 弃审前处理表体
				ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
				for (int i = 0; i < arrivevos.length; i++) {
					heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
				}
				// 加载未浏览过的单据体
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				//
				boolean isSucess = false;
				PfUtilClient.processBatch(this, "UNAPPROVE",
						ScmConst.PO_Arrive, ClientEnvironment.getInstance()
								.getDate().toString(), arrivevos, null);
				isSucess = PfUtilClient.isSuccess();
				if (isSucess) {
					// 刷新前端显示内容
					displayOthersVOs(vSubVos);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000184")/*
																			 * @res
																			 * "弃审成功"
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000185")/*
																			 * @res
																			 * "弃审失败"
																			 */);
				}
			} catch (nc.vo.pub.BusinessException e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000185")/* @res "弃审失败" */);
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000422")/*
																	 * @res
																	 * "业务异常"
																	 */,
						e.getMessage());
			} catch (Exception ex) {
				reportException(ex);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000185")/* @res "弃审失败" */);
				if (ex instanceof java.rmi.RemoteException) {
					MessageDialog.showErrorDlg(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000422")/*
																		 * @res
																		 * "业务异常"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"40040302", "UPP40040302-000003")/*
																	 * @res
																	 * "调用流程配置批弃审时出错:"
																	 */
									+ ex.getMessage());
				}
			}
		}
	}

	/**
	 * 显示操作完成后的单据。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param subVOs
	 *            <p>
	 * @author czp
	 * @time 2007-3-27 下午01:21:47
	 */
	private void displayOthersVOs(Vector<ArriveorderVO> subVOs) {
		Vector<ArriveorderVO> allVOs = new Vector<ArriveorderVO>();
		Vector<ArriveorderVO> newVOs = new Vector<ArriveorderVO>();
		ArriveorderVO[] arrvos = null;
		for (int i = 0; i < getCacheVOs().length; i++) {
			allVOs.addElement(getCacheVOs()[i]);
		}
		for (int i = 0; i < allVOs.size(); i++) {
			if (!subVOs.contains(allVOs.elementAt(i))) {
				newVOs.addElement(allVOs.elementAt(i));
			}
		}
		if (newVOs.size() > 0) {
			arrvos = new ArriveorderVO[newVOs.size()];
			newVOs.copyInto(arrvos);
			setCacheVOs(arrvos);
		} else {
			setCacheVOs(null);
		}
		// 显示数据、处理按钮状态
		loadDataToList();
		// 默认显示第一张
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			onSelectNo();
			getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
			getBillListPanel().getHeadBillModel().setRowState(0,
					BillModel.SELECTED);
		}
		// 刷新按钮逻辑
		setButtonsState();
	}

	/**
	 * 功能:放弃本次修改(包括浏览和转单两种情况的处理)
	 */
	public void onCancel() {
		if (getStateStr().equals("转入修改")) {
			delArriveorderVOSaved();
			if (getCacheVOs() != null) {
				displayArrBillListPanelNew();
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000126")/* @res "放弃上张单据,继续转单" */);
			} else {
				onEndCreate();
			}
			return;
		}
		onCard();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH008")/*
																	 * @res
																	 * "取消成功"
																	 */);
		// 初始化全局变量
		InitGlobalVar();
	}

	/**
	 * @功能：到货单列表界面“切换”按钮事件,切换到“到货浏览”“转入修改”状态
	 * @作者：晁志平 创建日期：(2001-6-20 8:10:39)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onCard() {

		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000128")/* @res "正在加载数据..." */);

		// 排序索引
		int index = getBillListPanel().getBodyBillModel().getSortColumn();
		boolean bSortAsc = getBillListPanel().getBodyBillModel()
				.isSortAscending();

		// 排序改变顺序的同步
		// int iPos = getBillListPanel().getHeadTable().getSelectedRow();
		// setDispIndex(iPos);
		/* 转入列表 */
		if (getStateStr().equals("转入列表")) {
			onCardNew();
			if (index >= 0) {
				getBillCardPanel().getBillModel().sortByColumn(index, bSortAsc);
			}
			return;
		}
		/* 非转入列表 */
		setM_strState("到货浏览");
		setButtonsState();
		/*
		 * if (m_arrListPanel != null) { remove(getBillListPanel()); }
		 * setLayout(new java.awt.BorderLayout()); add(getBillCardPanel(),
		 * java.awt.BorderLayout.CENTER);
		 */
		getBillListPanel().setVisible(false);
		getBillCardPanel().setVisible(true);
		getBillCardPanel().setEnabled(false);
		//
		try {
			loadDataToCard();
			setButtonsState();
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000129")/* @res "加载数据失败" */);
		}
		if (index >= 0) {
			getBillCardPanel().getBillModel().sortByColumn(index, bSortAsc);
		}
		updateUI();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																	 * @res
																	 * "卡片显示"
																	 */);
	}

	/**
	 * @功能：转入到货单修改
	 */
	private void onCardNew() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000131")/* @res "正在加载单据..." */);
		isFrmList = true;
		setM_strState("转入修改");
		getBillListPanel().setVisible(false);
		getBillCardPanel().setVisible(true);
		getBillCardPanel().setEnabled(true);
		setButtonsState();

		// 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
		// 取业务类型
		String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
		UFBoolean checker = new UFBoolean(false);
		try {
			loadDataToCard();
			// 退货理由(头体)
			setBackReasonEditable();
			// 批量加载单据行号
			BillRowNo.addNewRowNo(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno");

			checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
			// 过滤存货参照
			if (checker.booleanValue()) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
						.getBodyItem("cinventorycode").getComponent());
				refCinventorycode.getRefModel().addWherePart(sql);
			}
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000132")/* @res "加载单据失败" */);
		}
		/** 设置库存组织与仓库匹配 */
		setOrgWarhouse();

		// 根据操作员设置采购员及采购部门
		String strUserId = getClientEnvironment().getUser().getPrimaryKey();
		if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem(
				"cemployeeid").getValueObject()) == null) {
			IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator
					.getInstance().lookup(IUserManageQuery.class.getName());
			PsndocVO voPsnDoc = null;
			try {
				voPsnDoc = iSrvUser.getPsndocByUserid(getCorpPrimaryKey(),
						strUserId);
			} catch (BusinessException be) {
				SCMEnv.out(be);
			}
			if (voPsnDoc != null) {
				UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel()
						.getHeadItem("cemployeeid").getComponent();
				refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
				// 由采购员带出采购部门(如果采购部门无值时才带出)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadEmployee(null);
				}
			}
		}

		// 打印次数不能修改
		if (getBillCardPanel().getTailItem("iprintcount") != null)
			getBillCardPanel().getTailItem("iprintcount").setEnabled(false);
		updateUI();

		// 浮动菜单右键功能权限控制
		rightButtonRightControl();
		updateButtons();

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "正在修改" */);
	}

	/**
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
	 * 
	 *         创建日期：(2001-8-8 13:52:37)
	 */
	public boolean onClosing() {
		if (getStateStr().equals("到货修改") || getStateStr().equals("转入修改")) {
			int iRet = MessageDialog
					.showYesNoCancelDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */,
							m_lanResTool.getStrByID("common", "UCH001")/*
																		 * @res
																		 * "是否保存已修改的数据？"
																		 */);
			// 保存成功后才退出
			if (iRet == MessageDialog.ID_YES) {
				return onSave();
			}
			// 退出
			else if (iRet == MessageDialog.ID_NO) {
				return true;
			}
			// 取消关闭
			else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 功能描述:行拷贝
	 */
	private void onCopyLine() {
		if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() != null
				&& getBillCardPanel().getBodyPanel().getTable()
						.getSelectedRows().length > 0) {
			getBillCardPanel().copyLine();
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH039")/*
																	 * @res
																	 * "复制行成功"
																	 */);
	}

	/**
	 * 功能描述:删行
	 */
	private void onDeleteLine() {
		if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() == null
				|| getBillCardPanel().getBodyPanel().getTable()
						.getSelectedRows().length <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000136")/* @res "未选取行，删除行未成功" */);
			return;
		}
		int iSelRowCnt = getBillCardPanel().getBodyPanel().getTable()
				.getSelectedRows().length;
		if (iSelRowCnt == getBillCardPanel().getBodyPanel().getTable()
				.getRowCount()) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000137")/* @res "表体单据行至少有一行才能保存，删除行未成功！" */);
			return;
		}
		getBillCardPanel().delLine();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH037")/*
																	 * @res
																	 * "删行成功"
																	 */);
	}

	/**
	 * @功能：作废单据 流程方法 deleteMy() + rewriteOnDiscardMy() 此作废功能已经在向卡片写表体时得到限制
	 * @作者：晁志平 创建日期：(2001-6-20 10:40:17)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onDiscard() {
		int iRet = MessageDialog
				.showYesNoDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000219")/* @res "确定" */, m_lanResTool
						.getStrByID("common", "4004COMMON000000069")/*
																	 * @res
																	 * "是否确认要删除？"
																	 */,
						UIDialog.ID_NO);
		if (iRet != UIDialog.ID_YES) {
			return;
		}
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000139")/* @res "正在作废..." */);
		ArriveorderVO arrivevo = new ArriveorderVO();
		arrivevo = getCacheVOs()[getDispIndex()];
		// 作废后的显示位置处理
		int IndexLast = getCacheVOs().length - 1;
		int IndexCurr = getDispIndex();
		boolean isLast = false;
		if (IndexLast == IndexCurr) {
			isLast = true;
		}
		// 作废
		try {
			// 赋操作员
			arrivevo.setCoperatorid(getOperatorId());
			// 为判断是否可修改、作废其他人单据
			((ArriveorderHeaderVO) arrivevo.getParentVO())
					.setCoperatoridnow(getOperatorId());
			// 加锁需要
			arrivevo.getParentVO()
					.setAttributeValue("cuserid", getOperatorId());
			PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive,
					ClientEnvironment.getInstance().getDate().toString(),
					new ArriveorderVO[] { arrivevo });
			boolean bIsSucc = PfUtilClient.isSuccess();
			// 刷新前端缓存
			if (bIsSucc) {
				delArriveorderVODiscarded();
				if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
					getBillCardPanel().addNew();
					setButtonsState();
				} else {
					getBillCardPanel().getBillData().clearViewData();
					updateUI();
					if (isLast) {
						setDispIndex(getCacheVOs().length - 1);
					} else {
						setDispIndex(IndexCurr);
					}
					onCard();
				}
			}
		} catch (BusinessException b) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000059")/* @res "错误" */, b.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000347")/* @res "作废失败" */);
			return;
		} catch (Exception e) {
			reportException(e);
			if (e.getMessage() != null
					&& (e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000250")/* @res "到货" */) >= 0
							|| e.getMessage().indexOf(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000212")/* @res "" */) >= 0 || e
							.getMessage()
							.indexOf(
									m_lanResTool.getStrByID("40040301",
											"UPT40040301-000025")/* @res "退货" */) >= 0)
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000207")/* @res "收货" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000251")/* @res "单据" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000252")/* @res "容差" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000253")/* @res "号" */) >= 0) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */, e
						.getMessage());
			} else
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000140")/* @res "作废单据失败" */);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000347")/* @res "作废失败" */);
			return;
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "作废成功" */);
	}

	/**
	 * @功能：作废列表
	 * @作废条件：能修改且没有报检过的行;
	 * @不满足条件处理： 1.作废可以作废的单据 2.给出不能作废的单据行号
	 * @作者：晁志平 创建日期：(2001-06-20 10:40:17) 修改日期：(2001-10-29 14:40:17)
	 */
	private void onDiscardSelected() {

		int iRet = MessageDialog
				.showYesNoDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000219")/* @res "确定" */, m_lanResTool
						.getStrByID("common", "4004COMMON000000069")/*
																	 * @res
																	 * "是否确认要删除？"
																	 */,
						UIDialog.ID_NO);
		if (iRet != UIDialog.ID_YES) {
			return;
		}
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000139")/* @res "正在作废..." */);
		Vector v = new Vector();
		String lines = "";
		int i = 0, iRealPos = 0;
		boolean isSortFlag = false;
		if (getBillListPanel().getHeadBillModel().getSortIndex() != null) {
			isSortFlag = true;
		}
		int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
		BillModel bm = getBillListPanel().getHeadBillModel();
		ArriveorderVO arrivevo = null;
		for (i = 0; i < rowcount; i++) {
			if (bm.getRowState(i) == BillModel.SELECTED) {
				iRealPos = i;
				if (isSortFlag) {
					iRealPos = getBillListPanel().getHeadBillModel()
							.getSortIndex()[i];
				}
				// 选中的单据(缓存中)
				arrivevo = getCacheVOs()[iRealPos];
				// 给出不符合作废条件的行数
				if (!arrivevo.isCanBeModified() || arrivevo.isHaveCheckLine()) {
					if (!lines.trim().equals("")) {
						lines += m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000000")/* @res "、" */;
					}
					lines += i + 1;
				} else {
					v.add(arrivevo);
				}
			}
		}
		if (!lines.trim().equals("")) {
			if (lines.length() == 1) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000141")/*
															 * @res
															 * " 行到货单已经审批或正在审批或已经报检,这张单据不会被作废"
															 */);
			} else {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000142")/*
															 * @res
															 * " 行到货单已经审批或正在审批或已经报检,这些单据不会被作废"
															 */);
			}
		}
		ArriveorderVO[] arrivevos = null;
		if (v.size() > 0) {
			arrivevos = new ArriveorderVO[v.size()];
			v.copyInto(arrivevos);
			try {
				// 赋操作员
				for (int j = 0; j < arrivevos.length; j++) {
					arrivevos[j].setCoperatorid(getOperatorId());
					// 为判断是否可修改、作废其他人单据
					((ArriveorderHeaderVO) arrivevos[j].getParentVO())
							.setCoperatoridnow(getOperatorId());
					// 加锁需要
					arrivevos[j].getParentVO().setAttributeValue("cuserid",
							getOperatorId());
				}
				// 加载表体
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive,
						ClientEnvironment.getInstance().getDate().toString(),
						arrivevos);
				boolean bIsSucc = PfUtilClient.isSuccess();
				// 刷新前端缓存
				if (bIsSucc) {
					// 全部作废
					if (v.size() == getCacheVOs().length) {
						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getHeadBillModel().clearBodyData();
						setCacheVOs(null);
						updateUI();
					} else {
						// 刷新显示
						delArriveorderVOsDiscarded(v);
						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getHeadBillModel().clearBodyData();
						setDispIndex(0);
						onList();
					}
				}
			} catch (BusinessException b) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */, b
						.getMessage());
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000347")/* @res "作废失败" */);
				return;
			} catch (Exception e) {
				reportException(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "错误" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000143")/* @res "作废到货单失败" */);
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000347")/* @res "作废失败" */);
				return;
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH006")/*
																		 * @res
																		 * "删除成功"
																		 */);
		} else {
			onSelectNo();
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000144")/* @res "作废失败:所选单据均不符合作废条件" */);
		}
		//
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "作废成功" */);
	}

	/**
	 * 功能 ：文管管理 适用单据状态：“到货浏览”、“到货列表”
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		if (!(getStateStr().equalsIgnoreCase("到货浏览")
				|| getStateStr().equalsIgnoreCase("到货列表") || getStateStr()
				.equalsIgnoreCase("消息中心"))) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000145")/* @res "获取不到单据号,文档管理功能不可用" */);
		}
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		// 卡片
		if (getStateStr().equalsIgnoreCase("到货浏览")
				|| getStateStr().equalsIgnoreCase("消息中心")) {
			if (getCacheVOs() != null && getCacheVOs().length > 0
					&& getCacheVOs()[getDispIndex()] != null
					&& getCacheVOs()[getDispIndex()].getParentVO() != null) {
				strPks = new String[] { (String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("carriveorderid") };
				strCodes = new String[] { (String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("varrordercode") };
				// 处理文档管理框删除按钮是否可用
				BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
				iBillStatus = PuPubVO.getInteger_NullAs(
						getCacheVOs()[getDispIndex()].getParentVO()
								.getAttributeValue("ibillstatus"), new Integer(
								BillStatus.FREE));
				if (iBillStatus.intValue() == 2 || iBillStatus.intValue() == 3) {
					pVo.setFileDelEnable("false");
				}
				mapBtnPowerVo.put(strCodes[0], pVo);
			}
		}
		// 列表
		if (getStateStr().equalsIgnoreCase("到货列表")) {
			if (getCacheVOs() != null && getCacheVOs().length > 0) {
				ArriveorderHeaderVO[] headers = null;
				headers = (ArriveorderHeaderVO[]) getBillListPanel()
						.getHeadBillModel().getBodySelectedVOs(
								ArriveorderHeaderVO.class.getName());
				if (headers == null || headers.length <= 0)
					return;
				strPks = new String[headers.length];
				strCodes = new String[headers.length];
				BtnPowerVO pVo = null;
				for (int i = 0; i < headers.length; i++) {
					strPks[i] = headers[i].getPrimaryKey();
					strCodes[i] = headers[i].getVarrordercode();
					// 处理文档管理框删除按钮是否可用
					pVo = new BtnPowerVO(strCodes[i]);
					iBillStatus = PuPubVO.getInteger_NullAs(
							headers[i].getIbillstatus(), new Integer(0));
					if (iBillStatus.intValue() == 2
							|| iBillStatus.intValue() == 3) {
						pVo.setFileDelEnable("false");
					}
					mapBtnPowerVo.put(strCodes[i], pVo);
				}
			}
		}
		if (strPks == null || strPks.length <= 0)
			return;
		// 调用文档管理对话框
		nc.ui.scm.file.DocumentManager.showDM(this, strPks, strCodes,
				mapBtnPowerVo);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000025")/* @res "文档管理成功" */);
	}

	/**
	 * @功能：放弃转单/转单结束
	 */
	private void onEndCreate() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000146")/* @res "正在退出转单..." */);
		/* 重置缓存VO[]:有无转单成功均要重置 */
		setCacheVOs(m_VOsAll);
		/* 清除列表数据 */
		getBillListPanel().getBillListData().clearCopyData();
		/* 重置显示位置:区分有无转入成功的单据作不同处理 */
		int iNewCnt = 0;
		if (getCacheVOs() != null && getCacheVOs().length > m_OldVOsLen) {
			iNewCnt = getCacheVOs().length - m_OldVOsLen;
			setDispIndex(getCacheVOs().length - 1);
		} else {
			setDispIndex(m_OldCardVOPos);
		}
		/* 向卡片加载数据 */
		setM_strState("到货浏览");
		/* 与切换到卡片时作相同的处理 */
		onCard();
		/* 初始化转入用变量 */
		m_VOsAll = null;
		m_OldCardVOPos = 0;
		m_OldVOsLen = 0;
		if (iNewCnt > 0) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000147")/* @res "转单结束:生成 " */
					+ iNewCnt
					+ m_lanResTool.getStrByID("40040301", "UPP40040301-000148")/*
																				 * @
																				 * res
																				 * " 张新单据"
																				 */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000149")/* @res "转单结束:没有新单据生成" */);
		}
		return;
	}

	/**
	 * @功能：从订单生成对话框中退回时的处理
	 */
	private void onExitFrmOrd(ArriveorderVO[] retVOs) {
		/* 如果从选单界面取消返回，则 retVOs = null,由onButtonClicked()保证 */
		if (retVOs != null && retVOs.length > 0) {
			// 集中采购跨公司情况下的公司间ID转换
			try {
				OrderPubVO.chgDataForArrvCorp(retVOs, getCorpPrimaryKey());
			} catch (BusinessException e) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, e
						.getMessage());
				return;
			}
			//
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000150")/* @res "正在向列表加载数据..." */);
			/* 保存转单前缓存数据信息 */
			m_VOsAll = getCacheVOs();
			if (m_VOsAll != null && m_VOsAll.length > 0) {
				m_OldVOsLen = m_VOsAll.length;
			} else {
				m_OldVOsLen = 0;
				m_VOsAll = null;
			}
			m_OldCardVOPos = getDispIndex();
			/* 当前缓存置入待保存数据信息 */
			setCacheVOs(retVOs);
			// 用于缓存TS
			m_hTS = new HashMap();
			m_pushSaveVOs = null;
			setDispIndex(0);
			/* 显示数据 */
			displayArrBillListPanelNew();
			String[] value = new String[] { String
					.valueOf(getCacheVOs().length) };
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000151", null, value)/*
													 * @res "列表加载数据完成: 共加载"+
													 * getCacheVOs().length +
													 * " 张待保存单据"
													 */);
		}
		this.repaint();
	}

	/**
	 * 功能描述:首页
	 */
	private void onFirst() {
		int iRollBack = getDispIndex();
		setDispIndex(0);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000026")/* @res "成功显示首页" */);
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000153")/* @res "显示第一张单据失败" */);
		}
	}

	/**
	 * 功能描述:末页
	 */
	private void onLast() {
		int iRollBack = getDispIndex();
		setDispIndex(getCacheVOs().length - 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000029")/* @res "成功显示末页" */);
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000155")/* @res "显示最后一张单据失败" */);
		}
	}

	/**
	 * @功能：列表(区分两种状态：维护修改和转入修改)
	 * @作者：晁志平 创建日期：(2001-5-24 9:19:15)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onList() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000156")/* @res "正在向列表加载数据" */);
		// 清空状态图片
		// V5 Del : setImageType(this.IMAGE_NULL);
		// 排序索引
		int index = getBillCardPanel().getBillModel().getSortColumn();
		boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
		if (getStateStr().equals("转入修改")) {
			displayArrBillListPanelNew();
		} else {
			displayArrBillListPanel();
		}
		if (index >= 0) {
			getBillListPanel().getBodyBillModel().sortByColumn(index, bSortAsc);
		}
		updateUI();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH022")/*
																	 * @res
																	 * "列表显示"
																	 */);
	}

	/**
	 * 单据逐级联查
	 */
	private void onLnkQuery() {
		ArriveorderVO vo = getCacheVOs()[getDispIndex()];
		if (vo == null || vo.getParentVO() == null)
			return;
		nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
				this, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
				((ArriveorderHeaderVO) vo.getParentVO()).getPrimaryKey(), null,
				getClientEnvironment().getUser().getPrimaryKey(),
				getCorpPrimaryKey());
		soureDlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000019")/* @res "联查成功" */);
	}

	/**
	 * @功能：定位
	 */
	private void onLocate() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000157")/* @res "选择定位位置..." */);
		// 每次凋用时重置单据数及显示索引
		// UILabel
		getLocateDlg().setCurrBillCount(getCacheVOs().length);
		getLocateDlg().setCurrBillIndex(getDispIndex() + 1);
		String txt = getLocateDlg().getUILabel_Locate().getText();
		getLocateDlg().getUILabel_Locate().setText(
				txt.substring(0, txt.indexOf("{")) + "{1-"
						+ (getLocateDlg().getCurrBillCount()) + "}");
		// UITextField
		getLocateDlg().getUITextField_Locate().setMaxValue(
				getLocateDlg().getCurrBillCount());
		getLocateDlg().getUITextField_Locate().setMinValue(1);
		getLocateDlg().getUITextField_Locate().setText(
				(new Integer(getLocateDlg().getCurrBillIndex())).toString());

		getLocateDlg().showModal();
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000131")/* @res "正在加载单据..." */);
		if (getLocateDlg().isCloseOK()) {
			int iRollBack = getDispIndex();
			int currIndex = getLocateDlg().getLocateIndex();
			setDispIndex(currIndex - 1);
			setButtonsState();
			try {
				loadDataToCard();
			} catch (Exception e) {
				SCMEnv.out("加载单据时报错");
				setDispIndex(iRollBack);
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000158")/* @res "定位失败" */);
			}

			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000035")/* @res "定位成功" */);
			updateUI();
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000160")/* @res "取消定位" */);
		}
	}

	/**
	 * 功能描述:处理浮动菜单 创建日期：(2001-3-27 11:09:34)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent event) {
		UIMenuItem menuItem = (UIMenuItem) event.getSource();
		if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem())) {
			onCopyLine();
		} else if (menuItem.equals(getBillCardPanel().getDelLineMenuItem())) {
			onDeleteLine();
		} else if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem())) {
			onPasteLine();
		} else if (menuItem.equals(getBillCardPanel()
				.getPasteLineToTailMenuItem())) {
			onPasteLineToTail();
		}
	}

	/**
	 * 功能描述:更改
	 */
	private void onModify() {

		setM_strState("到货修改");

		int iCurSelectedRow = getBillCardPanel().getBillTable()
				.getSelectedRow();
		getBillCardPanel().updateValue();
		if (iCurSelectedRow >= 0)
			getBillCardPanel().getBillTable().setRowSelectionInterval(
					iCurSelectedRow, iCurSelectedRow);

		// 初始化删除集合(库位分配时用到)
		v_DeletedItems = new Vector();

		// 置是否拆分生成的行属性为 new UFBoolean(false)
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			for (int i = 0; i < (getCacheVOs()[getDispIndex()].getChildrenVO().length); i++) {
				((ArriveorderItemVO) ((ArriveorderVO) getCacheVOs()[getDispIndex()])
						.getChildrenVO()[i]).setIssplit(new UFBoolean(false));
				getBillCardPanel().getBillModel().setValueAt(
						new UFBoolean(false), i, "issplit");
			}
		}
		getBillCardPanel().setEnabled(true);
		// 业务类型不能修改
		if (getBillCardPanel().getHeadItem("cbiztype") != null)
			getBillCardPanel().getHeadItem("cbiztype").setEnabled(false);
		// //单据号不能修改
		// if (getBillCardPanel().getHeadItem("varrordercode") != null)
		// getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
		// 是否退货不能修改
		if (getBillCardPanel().getHeadItem("bisback") != null)
			getBillCardPanel().getHeadItem("bisback").setEnabled(false);
		// 打印次数不能修改
		if (getBillCardPanel().getTailItem("iprintcount") != null)
			getBillCardPanel().getTailItem("iprintcount").setEnabled(false);
		// 退货理由(头体)
		setBackReasonEditable();

		// 根据操作员设置对应采购员及部门
		setDefaultValueByUser();

		// 库存组织限制仓库
		if (getStateStr().equals("到货修改") || getStateStr().equals("转入修改")) {
			setOrgWarhouse();
		}
		setButtonsState();
		updateButtons();

		// 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
		// 取业务类型
		String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
		UFBoolean checker = new UFBoolean(false);
		try {
			checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
			// 过滤存货参照
			if (checker.booleanValue()) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
						.getBodyItem("cinventorycode").getComponent());
				refCinventorycode.getRefModel().addWherePart(sql);
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "正在修改" */);
	}

	/**
	 * 根据操作员设置采购员及采购部门。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-26 下午04:49:35
	 */
	private void setDefaultValueByUser() {
		// 取操作员对应业务员，设置采购员(采购员无值时才设置)
		if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem(
				"cemployeeid").getValueObject()) == null) {
			IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator
					.getInstance().lookup(IUserManageQuery.class.getName());
			PsndocVO voPsnDoc = null;
			try {
				voPsnDoc = iSrvUser.getPsndocByUserid(getBillCardPanel()
						.getCorp(), PoPublicUIClass.getLoginUser());
			} catch (BusinessException be) {
				SCMEnv.out(be);
			}
			if (voPsnDoc != null) {
				UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel()
						.getHeadItem("cemployeeid").getComponent();
				refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
				// 由采购员带出采购部门(如果采购员部门无值时才带出)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadEmployee(null);
				}
			}
		}
	}

	/**
	 * 功能描述:浮动菜单右键功能权限控制
	 */
	private void rightButtonRightControl() {
		// 没有分配行操作权限
		if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
			getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			getBillCardPanel().getDelLineMenuItem().setEnabled(false);
			getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
		}
		// 分配行操作权限
		else {
			getBillCardPanel().getCopyLineMenuItem().setEnabled(
					m_btnCpyLine.isPower());
			getBillCardPanel().getDelLineMenuItem().setEnabled(
					m_btnDelLine.isPower());
			getBillCardPanel().getPasteLineMenuItem().setEnabled(
					m_btnPstLine.isPower());
			// 粘贴到行尾与粘贴可用性逻辑相同
			getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(
					m_btnPstLine.isPower());
		}
	}

	/**
	 * @功能：列表状态下的修改
	 * @作者：晁志平 创建日期：(2001-9-13 20:02:06)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onModifyList() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000161")/* @res "正在切换到卡片..." */);
		isFrmList = true;
		onCard();
		onModify();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "正在修改" */);
	}

	/**
	 * 功能描述:下页
	 */
	private void onNext() {
		int iRollBack = getDispIndex();
		setDispIndex(getDispIndex() + 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000028")/* @res "成功显示下一页" */);
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000163")/* @res "显示下一张单据失败" */);
		}
	}

	/**
	 * 功能描述:粘贴行
	 */
	private void onPasteLine() {
		int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
		try {
			getBillCardPanel().pasteLine();
		} catch (Exception e) {
			SCMEnv.out("粘贴行时出错：" + e.getMessage());
		}
		int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000164")/* @res "粘贴未成功,可能原因：没有拷贝内容或未确定要粘贴的位置" */);
		} else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
			showHintMessage("");
		} else {
			showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																		 * @res
																		 * "粘贴行成功"
																		 */);
			// 单据行号
			BillRowNo.pasteLineRowNo(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
	}

	/**
	 * 功能描述:粘贴行到行尾
	 */
	private void onPasteLineToTail() {
		int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
		try {
			getBillCardPanel().pasteLineToTail();
		} catch (Exception e) {
			SCMEnv.out("粘贴行到行尾时出错：" + e.getMessage());
		}
		int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/*
										 * @res
										 * "粘贴行到行尾未成功,可能原因：没有拷贝内容或未确定要粘贴的位置"
										 */);
		} else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
			showHintMessage("");
		} else {
			showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																		 * @res
																		 * "粘贴行成功"
																		 */);
			// 单据行号
			BillRowNo.addLineRowNos(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
	}

	/**
	 * 功能描述:上页
	 */
	private void onPrevious() {
		int iRollBack = getDispIndex();
		setDispIndex(getDispIndex() - 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000027")/* @res "成功显示上一页" */);
		} catch (Exception e) {
			SCMEnv.out("加载单据时报错");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000167")/* @res "显示上一张单据失败" */);
		}
	}

	/**
	 * @功能：到货查询
	 * @作者：晁志平
	 * @创建：(2001-7-18 12:41:25)
	 */
	private void onQuery() {
		/**/
		m_hTS = null;
		int iRetType = getQueryConditionDlg().showModal();
		if (iRetType == UIDialog.ID_OK) {
			m_bQueriedFlag = true;
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000168")/* @res "正在查询单据..." */);
			getArriveVOsFromDB();
			setDispIndex(0);
			if (getStateStr().equals("到货列表")) {
				onList();
			} else {
				isFrmList = false;
				onCard();
			}
			if (getCacheVOs() == null || getCacheVOs().length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000169")/* @res "查询完成:没有符合条件的单据" */);
			} else {
				String[] value = new String[] { String
						.valueOf(getCacheVOs().length) };
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000170", null, value)/*
														 * @res "查询完成:查询到"+
														 * getCacheVOs().length
														 * + "张单据"
														 */);
			}

			showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																		 * @res
																		 * "查询完成"
																		 */);
		}
	}

	/**
	 * 作者：汪维敏 功能：此处插入方法说明 参数： 返回： 例外： 日期：(2004-3-8 10:35:43)
	 */
	private void onQueryBOM() {
		String sState = getStateStr();
		String sCmangId = null;
		int iPos;
		ArriveorderItemVO itemVO = null;
		if (sState != null && (sState.equals("转入列表") || sState.equals("到货列表"))) {
			if (getBillListPanel().getBodyTable().getRowCount() == 0)
				return;
			iPos = getBillListPanel().getBodyTable().getSelectedRow();
			if (iPos == -1) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000364")/* @res "请选中有存货的行!" */);
				return;
			}
			itemVO = (ArriveorderItemVO) getBillListPanel().getBodyBillModel()
					.getBodyValueRowVO(iPos, ArriveorderItemVO.class.getName());
			sCmangId = itemVO.getCmangid();
		} else {
			if (getBillCardPanel().getRowCount() == 0) {
				return;
			}
			iPos = getBillCardPanel().getBillTable().getSelectedRow();
			if (iPos == -1) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000364")/* @res "请选中有存货的行!" */);
				return;
			}
			itemVO = (ArriveorderItemVO) getCacheVOs()[getDispIndex()]
					.getChildrenVO()[iPos];
			sCmangId = itemVO.getCmangid();
		}
		if (PuPubVO.getString_TrimZeroLenAsNull(sCmangId) == null) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000364")/*
																			 * @res
																			 * "请选中有存货的行!"
																			 */);
			return;
		}
		SetPartDlg dlg = new SetPartDlg(this);
		dlg.setParam(getCorpPrimaryKey(), sCmangId);
		dlg.showSetpartDlg();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "查询完成"
																	 */);
	}

	/**
	 * 功能：存量查询 创建：(2002-10-31 19:45:39) 修改：2003-04-21/czp/统一走销售对话框
	 * 单据状态:初始化；到货浏览；到货修改；到货列表；转入列表；转入修改
	 */
	private void onQueryInvOnHand() {
		ArriveorderVO voPara = null;
		ArriveorderItemVO item = null;
		ArriveorderItemVO[] items = null;
		/* 卡片 */
		if (getStateStr().equals("到货浏览") || getStateStr().equals("到货修改")
				|| getStateStr().equals("转入修改")) {
			voPara = (ArriveorderVO) getBillCardPanel().getBillValueVO(
					ArriveorderVO.class.getName(),
					ArriveorderHeaderVO.class.getName(),
					ArriveorderItemVO.class.getName());
			if (voPara == null) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000171")/* @res "未选取单据,不能查询存量" */);
				return;
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (ArriveorderItemVO) getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								ArriveorderItemVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
				items = (ArriveorderItemVO[]) getBillCardPanel().getBillModel()
						.getBodyValueVOs(ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000172")/* @res "公司、存货、需求日期信息不完整,不能查询存量" */);
					return;
				}
				item = items[0];
			}
			/* 计划执行日期=到货日期 */
			item.setArrvdate((UFDate) voPara.getParentVO().getAttributeValue(
					"dreceivedate"));
			/* 信息完整性检查 */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")
					|| item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("arrvdate") == null
					|| item.getAttributeValue("arrvdate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000172")/* @res "公司、存货、需求日期信息不完整,不能查询存量" */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara.setChildrenVO(new ArriveorderItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("未查询到有权限公司，直接返回!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000173")/* @res "获取有权限公司异常" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000174")/*
														 * @res
														 * "获取有权限公司时出现异常(详细信息参见控制台日志)!"
														 */);
					SCMEnv.out(e);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* 列表 */
		else if (getStateStr().equals("到货列表") || getStateStr().equals("转入列表")) {
			/* 表头信息完整性检查 */
			ArriveorderHeaderVO head = null;
			if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
					ArriveorderHeaderVO.class.getName()) == null
					|| getBillListPanel().getHeadBillModel()
							.getBodySelectedVOs(
									ArriveorderHeaderVO.class.getName()).length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000171")/* @res "未选取单据,不能查询存量" */);
				return;
			}
			head = (ArriveorderHeaderVO) getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(ArriveorderHeaderVO.class.getName())[0];
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| head.getAttributeValue("pk_corp").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000175")/* @res "未明确公司,不能查询存量" */);
				return;
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = getBillListPanel().getBodyTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (ArriveorderItemVO) getBillListPanel()
						.getBodyBillModel().getBodyValueRowVO(iSelRows[0],
								ArriveorderItemVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
				items = (ArriveorderItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000172")/* @res "公司、存货、需求日期信息不完整,不能查询存量" */);
					return;
				}
				item = items[0];
			}
			/* 计划执行日期=到货日期 */
			item.setArrvdate(head.getDreceivedate());
			/* 信息完整性检查 */
			if (item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("arrvdate") == null
					|| item.getAttributeValue("arrvdate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000172")/* @res "公司、存货、需求日期信息不完整,不能查询存量" */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara = new ArriveorderVO();
			voPara.setParentVO(head);
			voPara.setChildrenVO(new ArriveorderItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("未查询到有权限公司，直接返回!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000173")/* @res "获取有权限公司异常" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000174")/*
														 * @res
														 * "获取有权限公司时出现异常(详细信息参见控制台日志)!"
														 */);
					SCMEnv.out(e);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000032")/* @res "存量查询完成" */);
	}

	/**
	 * 作者：汪维敏 功能：快速收货 参数： 返回： 例外： 日期：(2004-3-15 10:20:43)
	 */
	private void onQuickArr() {
		int iRetType = UIDialog.ID_OK;
		do {
			getQuickArrDlg().setCheckBoxSel(false);
			getQuickArrDlg().setBillCodeValue("");
			getQuickArrDlg().showModal();
			iRetType = getQuickArrDlg().getResult();
			if (iRetType == UIDialog.ID_OK) {
				String sBillCode = getQuickArrDlg().getBillCodeValue();
				if (sBillCode == null || sBillCode.trim().length() == 0) {
					showWarningMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000176")/* @res "单据号为空，请输入单据号!" */);
				} else
					iRetType = UIDialog.ID_NO;
			} else
				return;
		} while (iRetType == UIDialog.ID_OK);

		// V35BUG:
		if (isQuickException()) {
			setQuickExceptionFlag(false);
			return;
		}

		try {
			ArriveorderVO[] retVOs = (ArriveorderVO[]) getQuickArrDlg()
					.getRetVos();
			if (retVOs == null || retVOs.length == 0 || retVOs[0] == null) {
				showWarningMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000177")/* @res "没有符合条件的单据！" */);
				return;
			}
			// 区分是否保存前浏览
			if (getQuickArrDlg().isLookBefSave()) {
				onExitFrmOrd(retVOs);
				// 推式保存到货单
			} else {
				m_hTS = new HashMap();
				m_pushSaveVOs = retVOs;
				for (int i = 0; i < retVOs.length; i++) {
					ArriveorderVO saveVO = retVOs[i];

					// 流程保存方法的参数

					// aryPara0 : 0，公司主键；1，修改前的到货单；2，当前单据编辑状态；3.修改后的VO（界面VO）
					ArrayList aryPara = new ArrayList(2);
					ArrayList aryPara0 = new ArrayList();

					aryPara0.add(getCorpPrimaryKey());
					aryPara0.add(saveVO);
					aryPara0.add("insert");
					aryPara0.add(saveVO);

					aryPara.add(aryPara0);
					aryPara.add(null);
					aryPara.add(new Integer(0));
					aryPara.add(new String("cvendormangid"));

					// 重置表头表体状态(新增)
					((ArriveorderHeaderVO) saveVO.getParentVO())
							.setStatus(VOStatus.NEW);
					for (int j = 0; j < saveVO.getChildrenVO().length; j++) {
						((ArriveorderItemVO[]) saveVO.getChildrenVO())[j]
								.setStatus(VOStatus.NEW);
					}
					saveVO.setOprType(VOStatus.NEW);
					// 赋操作员
					saveVO.setCoperatorid(getOperatorId());
					// 判断并发的公用方法 PubDMO.checkVoNoChanged() 需要
					saveVO.getParentVO().setAttributeValue("cuserid",
							getOperatorId());

					ArrayList aryRet = (ArrayList) PfUtilClient.processAction(
							"SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment
									.getInstance().getDate().toString(),
							saveVO, aryPara);

					// 处理并发，刷新缓存VO的TS
					refreshVOTs((ArriveorderVO) aryRet.get(1));
					String strRetKey = (aryRet == null) ? null
							: (String) aryRet.get(0);
					boolean bIsSucc = PfUtilClient.isSuccess();
					// 保存后处理：
					if (strRetKey != null && bIsSucc) {
						/* 刷新保存成功单据 */
						/* 脚本 N_23_SAVEBASE 中设定新VO */
						ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
						if (voTmp == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "保存数据成功，但刷新数据时出错，请稍后再试！"
																 */);
						/* 增加保存成功单据到待显示单据缓存末尾 */
						appArriveorderVOSaved(voTmp);
					}
				}
				onEndCreate();
				m_pushSaveVOs = null;
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000179")/* @res "保存成功,转单结束" */);
			}
		} catch (Exception e) {
			reportException(e);
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000036")/* @res "快速收货完成" */);

	}

	/**
	 * @功能：刷新数据（卡片列表界面）
	 * @作者：晁志平 创建日期：(2001-6-20 13:35:04)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onRefresh() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000168")/* @res "正在查询单据..." */);
		// 更新数据
		getArriveVOsFromDB();
		/* 初始化浏览单据位置(考虑到删除数据) */
		setDispIndex(0);
		// 显示数据、处理按钮状态
		if (getStateStr().equals("到货浏览") || getStateStr().equals("初始化")) {
			try {
				loadDataToCard();
			} catch (Exception e) {
				SCMEnv.out("加载单据时报错");
			}
		} else if (getStateStr().equals("到货列表")) {
			loadDataToList();
			// 默认显示第一张
			if (getCacheVOs() != null && getCacheVOs().length > 0) {
				getBillListPanel().getHeadTable().setRowSelectionInterval(
						getDispIndex(), getDispIndex());
				getBillListPanel().getHeadBillModel().setRowState(
						getDispIndex(), BillModel.SELECTED);
				setListBodyData(getDispIndex());
			}
		}
		//
		setButtonsState();
		//
		if (getCacheVOs() == null || getCacheVOs().length <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000169")/* @res "查询完成:没有符合条件的单据" */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000170")/* @res "查询完成:查询到" */
					+ getCacheVOs().length
					+ m_lanResTool.getStrByID("40040301", "UPP40040301-000180")/*
																				 * @
																				 * res
																				 * "张单据"
																				 */);
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "刷新成功"
																	 */);
	}
	

	
	//add by yqq 2016-09-07
	public ArrayList getNordernum(String pk_corp,String corder_bid){		
			StringBuffer sql = new StringBuffer();
			sql.append("  select corderid, nordernum ") 
			   .append("  from po_order_b ") 
			   .append("  where pk_corp='"+pk_corp+"' and nvl(dr,0)=0 and corder_bid = '"+corder_bid+"'; ") ;
		
			ArrayList list = new ArrayList();
			IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;	  
	  }
  
	    /**
	     * 功能: 比较两个数值的大小,返回true表示后面比前面大
	     * @param String sa,String sb要比较的两个字符串
	     * @return boolean
	     */
	    public  static boolean  compareDou(UFDouble num,UFDouble sum){   //采购订单的数量 num
	        boolean flag=false;
	        int i=num.compareTo(sum);
	        if (i<0){
	            flag = true;
	            return flag;
	        }
	        return flag;
	    }
	    
	    //end by yqq 2016-09-07
	    
	    
		//add by yqq 2016-12-29后台加限制，到货单没有选择仓库不准保存，如果该仓库启用了货位，则未选择货位也不能保存		
		public ArrayList getCsflag (String pk_corp,String cwarehouseid){		
			StringBuffer sql = new StringBuffer();
			sql.append("  select csflag  ") 
			   .append("  from bd_stordoc ") 
			   .append("  where pk_corp='"+pk_corp+"' and nvl(dr,0)=0 and pk_stordoc = '"+cwarehouseid+"'; ") ;
		
			ArrayList list = new ArrayList();
			IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;	  
	  }
		
		//end by yqq 2016-12-29

	/**
	 * @功能：保存修改结果
	 */
	private boolean onSave() {
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("采购到货保存操作onSave开始");
		
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();

		
		//add by yqq 2016-12-29后台加限制，到货单没有选择仓库不准保存，如果该仓库启用了货位，则未选择货位也不能保存，不包含上海制盖的
		if(!(pk_corp.trim().equals("1078")||getCorpPrimaryKey().equals("1108"))){			
		 int sizee = this.getBillCardPanel().getBillModel().getRowCount();	
	      for (int i = 0; i < sizee; i++){  
		        String sCwarehouseid  = (String) getBillCardPanel().getBodyValueAt(i,"cwarehouseid"); //收货仓库ID
		  		BillModel bm = getBillCardPanel().getBillModel();
			 	ArrayList list = getCsflag(pk_corp,sCwarehouseid);   //是否货位管理的获取
			 	for(int j =0;j<list.size();j++){
			 	  Map map = (Map) list.get(j);	 				 	  
				  String csflag = map.get("csflag") == null ? "" : map.get("csflag").toString();
				  
				  if (csflag.equals("Y")) {  //仓库有货位管理时
					  if(bm.getValueAt(i, "cstoreid")==null){  //货位ID
						  showErrorMessage("表体第"+(i+1)+"行货位不能为空");
						  return true;
					  } else {
						  continue;
					  }
				  
				  }else {
						continue;
				  }
			 	}
	        }	  
	     }		
	//	end by yqq 2016-12-29
		
	//edit by yqq 2016-09-07 佛山制罐  采购到货的数量narrvnum不能大于采购订单的数量 nordernum
		if("1019".equals(pk_corp)){			
		 int size = this.getBillCardPanel().getBillModel().getRowCount();	
	      for (int i = 0; i < size; i++){  
				UFDouble sum = getBillCardPanel().getBodyValueAt(i,"narrvnum")== null ? 
				new UFDouble(0.0): (UFDouble)(getBillCardPanel().getBodyValueAt(i,"narrvnum"));     //到货数量
		        String sCrowno  = (String) getBillCardPanel().getBodyValueAt(i,"crowno "); //行号
		
		        String sCorder_bid  = (String) getBillCardPanel().getBodyValueAt(i,"corder_bid "); //采购订单行ID
			 	ArrayList list = getNordernum(pk_corp,sCorder_bid);   //采购订单数量的获取
			 	for(int j =0;j<list.size();j++){
			 	  Map map = (Map) list.get(j);	 	 
				  UFDouble num = map.get("nordernum")==null ? new UFDouble (0.0):new UFDouble(map.get("nordernum").toString()); //采购订单数量
				   				
				  if (compareDou(num,sum)){ 
					showWarningMessage (sCrowno+"行号的采购到货的数量大于采购订单的数量");
					return false;      
				  }					
			   }				  
	     	}	
		}
		//end by yqq
		
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000181")/* @res "正在保存单据..." */);
				
		// 终止编辑
		getBillCardPanel().stopEditing();

		// 增加对校验公式的支持,错误显示由UAP处理 since v501
		if (!getBillCardPanel().getBillData().execValidateFormulas()) {
			return false;
		}

		// 用于保存的VO
		ArriveorderVO saveVO = null;
		// 单据模板中显示的VO(转入修改及到货修改的结果)
		ArriveorderVO newvo = (ArriveorderVO) getBillCardPanel()
				.getBillValueVO(ArriveorderVO.class.getName(),
						ArriveorderHeaderVO.class.getName(),
						ArriveorderItemVO.class.getName());
		ArriveorderVO oldvo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
		((ArriveorderHeaderVO) newvo.getParentVO())
				.setPk_corp(getClientEnvironment().getCorporation()
						.getPrimaryKey());
		((ArriveorderHeaderVO) oldvo.getParentVO())
				.setPk_corp(getClientEnvironment().getCorporation()
						.getPrimaryKey());
		ArrayList dhlist=new ArrayList();
		for(int i=0;i<newvo.getChildrenVO().length;i++)
		{	
			ArriveorderItemVO tempvo=(ArriveorderItemVO)newvo.getChildrenVO()[i];
			if(tempvo.getStatus()!=VOStatus.NEW&&tempvo.getStatus()!=VOStatus.UPDATED)
			{
				continue;
			}
			String dh=tempvo.getVfree1();
			if(tempvo.getStatus()==VOStatus.UPDATED)
			{
				try
			
			{
				if(!IsNeedCheckvfree(dh,tempvo.getPrimaryKey()))
				{
					continue;
				}
			}
			catch(BusinessException e)
			{
				MessageDialog.showErrorDlg(this,"错误Error",e.getMessage());  
				return false;
			}
			}
		    if(StampIsExist(dh))
		   {
			
			 if(dhlist.indexOf(dh)<=0)
			 {
				dhlist.add(dh);
			 }
		   }
		}
        if(dhlist.size()>0)
		{
        	///if(MessageDialog.ID_NO==
        	//MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", Transformations.getLstrFromMuiStr("跺号@Stamp&:" + dhlist.toString()+ "&在现存量里已存在!@In the existing amount already exists!"));    	 	
        	//return false;
			if(MessageDialog.ID_YES!=showYesNoMessage(Transformations.getLstrFromMuiStr("垛号@The pile No. &"+dhlist.toString() + "&重复入库！@repeat warehousing!")+" \r\n"
			+ "是否继续？ continue?"))
	{
		return false ;
	}
		}
		
		if (!m_bSaveMaker)
			((ArriveorderHeaderVO) newvo.getParentVO())
					.setCoperator(getClientEnvironment().getUser()
							.getPrimaryKey());

		// 检查数据
		if (!chechDataBeforeSave(newvo, oldvo))
			return false;
		
		// 流程保存方法的参数
		// aryPara0 : 0，公司主键；1，修改前的到货单；2，当前单据编辑状态；3.修改后的VO（界面VO）
		// aryPara1 : 0，是否有用户交互；1，上次操作时的订单行ID及订单数量
        
		ArrayList aryPara = new ArrayList(2);
		ArrayList aryPara0 = new ArrayList();
		aryPara0.add(getCorpPrimaryKey());
		aryPara0.add(oldvo);
		aryPara0.add(getStateStr().equals("转入修改") ? "insert" : "update");
		// 到货数量精度*(-1) 传入后台作为订单数量的 power
		newvo.setDigitsNumPower(CPurchseMethods
				.getMeasDecimal(getCorpPrimaryKey()) * (-1));
		aryPara0.add(newvo);
		aryPara.add(aryPara0);
		aryPara.add(null);
		aryPara.add(new Integer(0));
		aryPara.add(new String("cvendormangid"));
		if (getStateStr().equals("到货修改")) {
			// 用于修改保存的VO
			saveVO = (ArriveorderVO) getBillCardPanel().getBillValueChangeVO(
					ArriveorderVO.class.getName(),
					ArriveorderHeaderVO.class.getName(),
					ArriveorderItemVO.class.getName());
			if (!m_bSaveMaker)
				((ArriveorderHeaderVO) saveVO.getParentVO())
						.setCoperator(getClientEnvironment().getUser()
								.getPrimaryKey());

			// 在浏览修改状态下（转入状态不用）
			// 如果有被分配库位的行 或 货位分配过程中存在未补分配而删除的行,则加回来，一并传给后端处理(它们将被从数据库中删除)
			if (vDelNoSplitted.size() > 0) {
				for (int i = 0; i < vDelNoSplitted.size(); i++) {
					v_DeletedItems.addElement(vDelNoSplitted.elementAt(i));
				}
			}
			if (v_DeletedItems.size() > 0) {
				ArriveorderItemVO[] allItems = new ArriveorderItemVO[v_DeletedItems
						.size() + saveVO.getChildrenVO().length];
				if (v_DeletedItems.size() > 0) {
					for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
						v_DeletedItems.addElement(((ArriveorderItemVO[]) saveVO
								.getChildrenVO())[i]);
					}
					v_DeletedItems.copyInto(allItems);
				}
				saveVO.setChildrenVO(allItems);
			}
			saveVO.setOprType(VOStatus.UPDATED);
			saveVO.setUpBillType(((ArriveorderItemVO) newvo.getChildrenVO()[0])
					.getCupsourcebilltype());
			// 赋操作员
			saveVO.setCoperatorid(getOperatorId());
			// 加锁需要
			saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
		} else {
			// 用于新增的VO
			saveVO = newvo;
			// 重置表头表体状态(新增)
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setStatus(VOStatus.NEW);
			// 制单人
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setCoperator(getOperatorId());
			for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
				((ArriveorderItemVO[]) saveVO.getChildrenVO())[i]
						.setStatus(VOStatus.NEW);
			}
			saveVO.setOprType(VOStatus.NEW);
			// 流程回写用：上层来源单据类型
			saveVO.setUpBillType(((ArriveorderItemVO) oldvo.getChildrenVO()[0])
					.getCupsourcebilltype());
			// 赋操作员
			saveVO.setCoperatorid(getOperatorId());
			// 判断并发的公用方法 PubDMO.checkVoNoChanged() 需要
			saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
		}
		// 处理非自检参照--备注
		UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		UITextField vMemoField = nRefPanel.getUITextField();
		String vmemo = vMemoField.getText();
		((ArriveorderHeaderVO) saveVO.getParentVO()).setVmemo(vmemo);
		// 处理非自检参照--退货理由
		if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
			UIRefPane refPanel = (UIRefPane) getBillCardPanel().getHeadItem(
					"vbackreasonh").getComponent();
			UITextField txtBack = refPanel.getUITextField();
			String strBack = txtBack.getText();
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setVbackreasonh(strBack);
		}
		timer.addExecutePhase(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000254")/* @res "保存前的准备操作" */);
		// 为判断是否可修改、作废其他人单据
		((ArriveorderHeaderVO) saveVO.getParentVO())
				.setCoperatoridnow(getOperatorId());
		String strRetKey = null;
		boolean isCycle = true;
		// 修改时：拼接未改变的表体VO(说明参见getSaveVO()方法)
		if (getStateStr().equals("到货修改")) {
			saveVO = getSaveVO(saveVO);
			timer.addExecutePhase("getSaveVO");
		}
		// 是否需要回退单据号:新增且手工录入单据号
		if (getStateStr().equals("转入修改")) {
			if (saveVO.getParentVO() != null
					&& saveVO.getParentVO().getAttributeValue("varrordercode") != null
					&& saveVO.getParentVO().getAttributeValue("varrordercode")
							.toString().trim().length() > 0) {
			}
		}
		doCycle: while (isCycle) {
			isCycle = false;
			try {
				ArrayList aryRet = (ArrayList) PfUtilClient.processAction(
						"SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment
								.getInstance().getDate().toString(), saveVO,
						aryPara);
				timer.addExecutePhase("执行SAVE脚本");
				// 处理并发，刷新缓存VO的TS
				refreshVOTs((ArriveorderVO) aryRet.get(1));
				timer.addExecutePhase("刷新缓存VO的TS");
				strRetKey = aryRet == null ? null : (String) aryRet.get(0);
				boolean bIsSucc = PfUtilClient.isSuccess();
				// 保存后处理：
				if (getStateStr().equals("到货修改")) {
					if (strRetKey != null && bIsSucc) {
						setM_strState("到货浏览");
						// 脚本中设定新VO
						getCacheVOs()[getDispIndex()] = (ArriveorderVO) aryRet
								.get(1);
						if (getCacheVOs()[getDispIndex()] == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "保存数据成功，但刷新数据时出错，请稍后再试！"
																 */);
						// ArriveorderBO_Client.findByPrimaryKey(oldvo.getParentVO().getPrimaryKey());
						// 顺序不能置换
						setButtonsState();
						loadDataToCard();
						getBillCardPanel().setEnabled(false);
						showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000006")/* @res "保存成功" */);
						updateUI();
					}
				} else if (getStateStr().equals("转入修改")) {
					if (strRetKey != null && bIsSucc) {
						// 从当前未保存单据中删除保存成功单据
						delArriveorderVOSaved();
						// 已更新ts的VO
						ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
						if (voTmp == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "保存数据成功，但刷新数据时出错，请稍后再试！"
																 */);

						refreshVoFieldsByKey(voTmp, strRetKey);

						// 增加保存成功单据到待显示单据缓存末尾
						appArriveorderVOSaved(voTmp);
						// 根据转单是否结束作不同处理
						if (getCacheVOs() != null) {
							displayArrBillListPanelNew();
							showHintMessage(m_lanResTool.getStrByID("40040301",
									"UPP40040301-000182")/* @res "保存成功,继续转单" */);
						} else {
							onEndCreate();
							showHintMessage(m_lanResTool.getStrByID("40040301",
									"UPP40040301-000179")/* @res "保存成功,转单结束" */);
						}
					}
				}
				timer.addExecutePhase("保存后处理");
				showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																			 * @res
																			 * "保存成功"
																			 */);
			} catch (Exception e) {
				// //回退单据号
				// if (isBackCode) {
				// SCMEnv.out("回退单据号开始[ArriveUI]...");
				// try {
				// PubHelper.returnBillCode(newvo);
				// } catch (Exception ex) {
				// SCMEnv.out("回退单据号异常结束[ArriveUI]");
				// }
				// SCMEnv.out("回退单据号正常结束[ArriveUI]");
				// }
				// 处理回写采购订单超容差提示情况
				if (e instanceof RwtRcToPoException) {
					// 请购单累计数量超出提示
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// 继续循环
						isCycle = true;
						// 是否用户确认
						saveVO.setUserConfirm(true);
					} else {
						return false;
					}
				}
				// 处理回写委外订单超容差提示情况
				else if (e instanceof RwtRcToScException) {
					// 到货累计数量超出提示
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// 继续循环
						isCycle = true;
						// 是否用户确认
						saveVO.setUserConfirm(true);
					} else {
						return false;
					}
				} else if (e instanceof BusinessException
						|| e instanceof java.rmi.RemoteException
						|| e instanceof ValidationException) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "错误" */, e
									.getMessage());
				} else if (e.getMessage() != null
						&& (e.getMessage().indexOf(
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000250")/* @res "到货" */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000212")/*
																	 * @res "订单"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPT40040301-000025")/*
																	 * @res "退货"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000207")/*
																	 * @res "收货"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000251")/*
																	 * @res "单据"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000252")/*
																	 * @res "容差"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000253")/*
																	 * @res "号"
																	 */) >= 0
								|| e.getMessage().indexOf("BusinessException") >= 0 || e
								.getMessage().indexOf("RemoteException") >= 0)) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "错误" */, e
									.getMessage());
				} else {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "错误" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000183")/* @res "系统异常，保存失败" */);
				}
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* @res "保存失败" */);
			}
		}
		timer.showAllExecutePhase("采购到货保存操作onSave操作结束");
		// 初始化全局变量
		InitGlobalVar();
		return true;
	}




	private Object getBodyInfo(String string) {
			// TODO Auto-generated method stub
			return null;
		}

	/**
	 * 刷新保存成功单据(因为保存及审批操作中需要，要刷新审批日期和审批人,单据状态，TS，制单时间，审批时间，最后修改时间)
	 * 
	 * @param vo
	 * @param strKey
	 * @author czp
	 * @date 2006-05-18
	 */
	private void refreshVoFieldsByKey(ArriveorderVO vo, String strKey)
			throws Exception {
		//
		ArrayList arrRet = ArriveorderHelper.queryForSaveAudit(strKey);
		((ArriveorderHeaderVO) vo.getParentVO()).setDauditdate((UFDate) arrRet
				.get(0));
		((ArriveorderHeaderVO) vo.getParentVO()).setCauditpsn((String) arrRet
				.get(1));
		((ArriveorderHeaderVO) vo.getParentVO())
				.setIbillstatus((Integer) arrRet.get(2));
		((ArriveorderHeaderVO) vo.getParentVO()).setTs((String) arrRet.get(3));
		((ArriveorderHeaderVO) vo.getParentVO())
				.setTmaketime((UFDateTime) arrRet.get(4));
		((ArriveorderHeaderVO) vo.getParentVO())
				.setTaudittime((UFDateTime) arrRet.get(5));
		((ArriveorderHeaderVO) vo.getParentVO())
				.setTlastmaketime((UFDateTime) arrRet.get(6));
	}

	/**
	 * @功能：选定所有到货单
	 * @作者：晁志平 创建日期：(2001-6-8 14:21:35)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onSelectAll() {
		int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.SELECTED);
		}
		// 设置按钮状态
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000033")/* @res "全部选中成功" */);
	}

	/**
	 * @功能：取消所有选定的到货单表行
	 * @作者：晁志平 创建日期：(2001-6-8 14:22:12)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onSelectNo() {
		int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		getBillListPanel().getHeadTable().removeRowSelectionInterval(0,
				iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		// 设置按钮状态
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000034")/* @res "全部取消成功" */);
	}

	/**
	 * 功能:执行弃审
	 */
	private void onUnAudit(ButtonObject bo) {
		try {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000186")/* @res "正在弃审..." */);
			ArriveorderVO vo = getCacheVOs()[getDispIndex()];
			// 弃审
			PfUtilClient.processBatchFlow(null, "UNAPPROVE"
					+ getClientEnvironment().getUser().getPrimaryKey(),
					ScmConst.PO_Arrive, getClientEnvironment().getDate()
							.toString(), new ArriveorderVO[] { vo }, null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000187")/* @res "弃审失败(平台调用出现异常)" */);
				return;
			}
			// 弃审成功后刷新
			refreshVoFieldsByKey(vo, vo.getParentVO().getPrimaryKey());

			getCacheVOs()[getDispIndex()] = vo;
			// 刷新按钮状态
			setButtonsState();
			// 加载单据
			try {
				loadDataToCard();
			} catch (Exception e) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000188")/*
											 * @res
											 * "弃审成功,但加载单据时出现异常,请刷新界面再进行其它操作"
											 */);
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH011")/*
																		 * @res
																		 * "弃审成功"
																		 */);
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000189")/* @res "出现异常,弃审失败" */);
			SCMEnv.out(e);
			if (e instanceof java.rmi.RemoteException
					|| e instanceof BusinessException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040301", "UPP40040301-000099")/* @res "报错" */, e
						.getMessage());
			}
		}
	}

	/**
	 * 作者：汪维敏 功能：刷新表体行或者TS缓存的TS，用于处理并发 参数： 返回： 例外： 日期：(2004-4-1 10:55:04)
	 */
	private void refreshVOTs(ArriveorderVO vo) {
		if (m_hTS == null)
			return;
		if (vo == null)
			return;
		ArriveorderItemVO[] items = (ArriveorderItemVO[]) vo.getChildrenVO();
		if (items == null || items.length == 0)
			return;
		String sUpSourceBillType = items[0].getCupsourcebilltype();
		if (sUpSourceBillType == null || sUpSourceBillType.trim().length() == 0)
			return;
		int size = items.length;
		// 刷新缓存VO的表体TS,处理并发
		try {
			if (items[0].getCupsourcebilltype().equals("21")) {
				String[] sID = new String[size];
				for (int i = 0; i < size; i++) {
					sID[i] = items[i].getCorder_bid();
				}
				// 刷新TS
				HashMap hTs = ArriveorderHelper.queryNewTs(sID);
				for (int i = 0; i < size; i++) {
					Object[] ob = (Object[]) hTs.get(items[i].getCorder_bid());
					if (ob != null && ob[0] != null)
						items[i].setTsbup(ob[0].toString());
				}
			}
			ArriveorderVO[] vos = getCacheVOs();
			// 用于推式保存
			if (m_pushSaveVOs != null && m_pushSaveVOs.length > 0)
				vos = m_pushSaveVOs;
			if (vos == null || vos.length == 0)
				return;

			String sUpSourceBTs = null;
			String sUpsourceRowid = null;

			// 将新TS放入TS缓存
			for (int i = 0; i < size; i++) {
				sUpSourceBTs = items[i].getTsbup();
				sUpsourceRowid = items[i].getCupsourcebillrowid();
				String sTs = null;
				if (m_hTS.containsKey(sUpsourceRowid)) {
					sTs = (String) m_hTS.get(sUpsourceRowid);
					if (sTs != null && sTs.trim().length() > 0
							&& !sTs.equals(sUpSourceBTs)) {
						m_hTS.remove(sUpsourceRowid);
						m_hTS.put(sUpsourceRowid, sUpSourceBTs);
					}
				} else {
					m_hTS.put(sUpsourceRowid, sUpSourceBTs);
				}
			}

			// 用TS缓存去更新VO缓存
			for (int i = 0; i < vos.length; i++) {
				ArriveorderItemVO[] itemVOs = (ArriveorderItemVO[]) vos[i]
						.getChildrenVO();
				for (int j = 0; j < itemVOs.length; j++) {
					sUpSourceBTs = itemVOs[j].getTsbup();
					sUpsourceRowid = itemVOs[j].getCupsourcebillrowid();
					String sTs = (String) m_hTS.get(sUpsourceRowid);
					if (sTs != null && sTs.trim().length() > 0
							&& !sTs.equals(sUpSourceBTs)) {
						itemVOs[j].setTsbup(sTs);
					}
				}
			}
		} catch (Exception e) {
			reportException(e);
		}
	}

	public void setBillVO(nc.vo.pub.AggregatedValueObject vo) {
		UIRefPane refPane = null;
		BillModel bm = getBillCardPanel().getBillModel();
		ArriveorderVO VO = (ArriveorderVO) vo;

		// 设置VO到卡片单据模板
		getBillCardPanel().setBillValueVO(VO);
		// 处理基础数据被作废(DR!=0)或冻结时不能正确显示名称问题
		loadBDData();
		// 处理单据模板自定义项
		ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) VO.getParentVO();
		String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4",
				"vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10",
				"vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16",
				"vdef17", "vdef18", "vdef19", "vdef20" };
		int iLen = saKey.length;
		for (int i = 0; i < iLen; i++) {
			voHead.setAttributeValue(saKey[i],
					getBillCardPanel().getHeadItem(saKey[i]).getValueObject());
		}
		// 关闭合计开关
		boolean bOldNeedCalc = bm.isNeedCalculate();
		bm.setNeedCalculate(false);
		// 执行加载公式
		bm.execLoadFormula();
		// 打开合计开关
		bm.setNeedCalculate(bOldNeedCalc);
		getBillCardPanel().updateValue();

		// 显示表头备注
		refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo")
				.getComponent();
		refPane.setValue((String) VO.getParentVO().getAttributeValue("vmemo"));
		// 显示表头退货理由
		if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
			refPane = (UIRefPane) getBillCardPanel()
					.getHeadItem("vbackreasonh").getComponent();
			refPane.setValue((String) VO.getParentVO().getAttributeValue(
					"vbackreasonh"));
		}
		// 缓存替换件参照构造子参数 {到货单行或订单行ID = cmangid }
		Vector vCmangids = new Vector();
		String strCmangid = null;
		m_hBillIDsForCmangids = new Hashtable();
		String strKey = (getStateStr().equals("转入修改")) ? "corder_bid"
				: "carriveorder_bid";
		for (int i = 0; i < bm.getRowCount(); i++) {
			strCmangid = (String) bm.getValueAt(i, "cmangid");
			if (strCmangid != null && strCmangid.trim().length() > 0
					&& !vCmangids.contains(strCmangid))
				vCmangids.addElement(strCmangid);
			if (bm.getValueAt(i, strKey) == null)
				continue;
			if (!m_hBillIDsForCmangids.containsKey(bm.getValueAt(i, strKey)))
				m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey),
						bm.getValueAt(i, "cmangid"));
		}
		// 逐行处理表体备注、数量
		String strCmain = null;
		String strCbaseid = null;
		String strCassid = null;
		Object oNarrvnum = null;
		Object oNassinum = null;
		UFDouble ufdNarrvnum = null;
		UFDouble ufdNassinum = null;
		Object oValue = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			// 表体备注初始化:否则不能触发afterEdit()
			if (bm.getValueAt(i, "vmemo") == null) {
				bm.setValueAt("", i, "vmemo");
			}
			// 表体退货理由初始化:否则不能触发afterEdit()
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				if (bm.getValueAt(i, "vbackreasonb") == null) {
					bm.setValueAt("", i, "vbackreasonb");
				}
			}
			// 是否拆分生成的行--暂未用
			bm.setValueAt(new UFBoolean(false), i, "issplit");

			strCbaseid = (String) bm.getValueAt(i, "cbaseid");
			strCmangid = (String) bm.getValueAt(i, "cmangid");
			strCassid = (String) bm.getValueAt(i, "cassistunit");
			strCmain = (String) bm.getValueAt(i, "cmainmeasid");
			// 是否辅计量管理
			UFBoolean bIsAssMana = new UFBoolean(
					PuTool.isAssUnitManaged(strCbaseid));
			if (bIsAssMana.booleanValue()) {
				if (strCassid == null || strCassid.trim().equals("")) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "错误" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000190")/*
														 * @res
														 * "有辅计量管理的存货行存在空辅计量！"
														 */);
					return;
				}
				// 设置换算率
				UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
						strCassid);
				bm.setValueAt(convert, i, "convertrate");
				// 主辅计量相同则换算率置为 1.0
				if (strCmain != null && strCmain.equals(strCassid)) {
					bm.setValueAt(new UFDouble(1.0), i, "convertrate");
				}
				// 非固定换算率，换算率=主数量/辅数量
				if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
					oNarrvnum = bm.getValueAt(i, "narrvnum");
					oNassinum = bm.getValueAt(i, "nassistnum");
					if (!(oNarrvnum == null || oNarrvnum.toString().trim()
							.equals(""))
							&& !(oNassinum == null || oNassinum.toString()
									.trim().equals(""))) {
						ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
						ufdNassinum = new UFDouble(oNassinum.toString().trim());
						oValue = ufdNassinum == new UFDouble(0) ? null
								: ufdNarrvnum.div(ufdNassinum);
					} else
						oValue = null;
					bm.setValueAt(oValue, i, "convertrate");
				}
			} else {
				bm.setValueAt(null, i, "convertrate");
			}
		}
		PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_ARRIVE);

	}

	/**
	 * @功能：卡片显示数据
	 */
	private void loadDataToCard() throws Exception {
		UIRefPane refPane = null;
		BillModel bm = getBillCardPanel().getBillModel();
		if (getCacheVOs() != null) {
			// 从列表向卡片切换时处理排序
			if (isFrmList) {
				isFrmList = false;
				// 获取排序后的实际位置
				int iShowPos = getBillListPanel().getHeadTable()
						.getSelectedRow();
				if (iShowPos >= 0) {
					iShowPos = PuTool.getIndexBeforeSort(getBillListPanel(),
							iShowPos);
					setDispIndex(iShowPos);
				}
			}
			// 新增单据时不必处理刷新表体,V5,新增、修改要刷新，因为加入制单时间、最后修改时间、审批时间
			if (!getStateStr().equals("转入修改")) {
				// 获取表体(未被加载过才刷新)
				try {
					getCacheVOs()[getDispIndex()] = RcTool
							.getRefreshedVO(getCacheVOs()[getDispIndex()]);
				} catch (Exception be) {
					if (be instanceof BusinessException)
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000422")/* @res "业务异常" */,
								be.getMessage());
					throw be;
				}
			}
			// 审批推式生成入库单时的如果出现异常，要将审批人和审批日期置空
			ArriveorderHeaderVO ArrBillHeadVO = (ArriveorderHeaderVO) getCacheVOs()[getDispIndex()]
					.getParentVO();
			if (ArrBillHeadVO.getIbillstatus().intValue() == 0) {
				ArrBillHeadVO.setCauditpsn(null);
				ArrBillHeadVO.setDauditdate(null);
			}
			// 按采购公司初始化的参照：{业务员、部门、供应商}
			String strPurCorp = ArrBillHeadVO.getPk_purcorp();
			((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
					.getComponent()).getRefModel().setPk_corp(strPurCorp);
			((UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
					.getComponent()).getRefModel().setPk_corp(strPurCorp);

			/*
			 * V50 发版前， 2006-11-24 : Wangyf&Xy&Xhq&Czp,注释掉,按收货公司处理供应商参照
			 * ((UIRefPane
			 * )getBillCardPanel().getHeadItem("cvendormangid").getComponent
			 * ()).getRefModel().setPk_corp(strPurCorp);
			 */

			// 设置VO到卡片单据模板
			getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
			// 处理基础数据被作废(DR!=0)或冻结时不能正确显示名称问题
			loadBDData();
			// 处理单据模板自定义项
			ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) getCacheVOs()[getDispIndex()]
					.getParentVO();
			String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4",
					"vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10",
					"vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16",
					"vdef17", "vdef18", "vdef19", "vdef20" };
			int iLen = saKey.length;
			for (int i = 0; i < iLen; i++) {
				voHead.setAttributeValue(saKey[i], getBillCardPanel()
						.getHeadItem(saKey[i]).getValue());
			}
			// 关闭合计开关
			boolean bOldNeedCalc = bm.isNeedCalculate();
			bm.setNeedCalculate(false);
			// 执行加载公式
			bm.execLoadFormula();
			// 打开合计开关
			bm.setNeedCalculate(bOldNeedCalc);
			getBillCardPanel().updateValue();

			// 显示表头备注
			refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo")
					.getComponent();
			refPane.setValue((String) getCacheVOs()[getDispIndex()]
					.getParentVO().getAttributeValue("vmemo"));
			// 显示表头退货理由
			if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
				refPane = (UIRefPane) getBillCardPanel().getHeadItem(
						"vbackreasonh").getComponent();
				refPane.setValue((String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("vbackreasonh"));
			}
			// 缓存替换件参照构造子参数 {到货单行或订单行ID = cmangid }
			Vector vCmangids = new Vector();
			String strCmangid = null;
			m_hBillIDsForCmangids = new Hashtable();
			String strKey = (getStateStr().equals("转入修改")) ? "corder_bid"
					: "carriveorder_bid";
			for (int i = 0; i < bm.getRowCount(); i++) {
				strCmangid = (String) bm.getValueAt(i, "cmangid");
				if (strCmangid != null && strCmangid.trim().length() > 0
						&& !vCmangids.contains(strCmangid))
					vCmangids.addElement(strCmangid);
				if (bm.getValueAt(i, strKey) == null)
					continue;
				if (!m_hBillIDsForCmangids
						.containsKey(bm.getValueAt(i, strKey)))
					m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey),
							bm.getValueAt(i, "cmangid"));
			}
			// 逐行处理表体备注、数量
			String strCmain = null;
			String strCbaseid = null;
			String strCassid = null;
			Object oNarrvnum = null;
			Object oNassinum = null;
			UFDouble ufdNarrvnum = null;
			UFDouble ufdNassinum = null;
			Object oValue = null;
			for (int i = 0; i < bm.getRowCount(); i++) {
				// 表体备注初始化:否则不能触发afterEdit()
				if (bm.getValueAt(i, "vmemo") == null) {
					bm.setValueAt("", i, "vmemo");
				}
				// 表体退货理由初始化:否则不能触发afterEdit()
				if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
					if (bm.getValueAt(i, "vbackreasonb") == null) {
						bm.setValueAt("", i, "vbackreasonb");
					}
				}
				// 是否拆分生成的行--暂未用
				bm.setValueAt(new UFBoolean(false), i, "issplit");

				strCbaseid = (String) bm.getValueAt(i, "cbaseid");
				strCmangid = (String) bm.getValueAt(i, "cmangid");
				strCassid = (String) bm.getValueAt(i, "cassistunit");
				strCmain = (String) bm.getValueAt(i, "cmainmeasid");
				// 是否辅计量管理
				UFBoolean bIsAssMana = new UFBoolean(
						PuTool.isAssUnitManaged(strCbaseid));
				if (bIsAssMana.booleanValue()) {
					if (strCassid == null || strCassid.trim().equals("")) {
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000059")/* @res "错误" */,
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000190")/*
															 * @res
															 * "有辅计量管理的存货行存在空辅计量！"
															 */);
						return;
					}
					// 设置换算率
					UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
							strCassid);
					bm.setValueAt(convert, i, "convertrate");
					// 主辅计量相同则换算率置为 1.0
					if (strCmain != null && strCmain.equals(strCassid)) {
						bm.setValueAt(new UFDouble(1.0), i, "convertrate");
					}
					// 非固定换算率，换算率=主数量/辅数量
					if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
						oNarrvnum = bm.getValueAt(i, "narrvnum");
						oNassinum = bm.getValueAt(i, "nassistnum");
						if (!(oNarrvnum == null || oNarrvnum.toString().trim()
								.equals(""))
								&& !(oNassinum == null || oNassinum.toString()
										.trim().equals(""))) {
							ufdNarrvnum = new UFDouble(oNarrvnum.toString()
									.trim());
							ufdNassinum = new UFDouble(oNassinum.toString()
									.trim());
							oValue = ufdNassinum == new UFDouble(0) ? null
									: ufdNarrvnum.div(ufdNassinum);
						} else
							oValue = null;
						bm.setValueAt(oValue, i, "convertrate");
					}
				} else {
					bm.setValueAt(null, i, "convertrate");
				}
			}
			// 浏览修改：设置功能按钮是否有效
			if (!getStateStr().equals("转入修改")) {
				getBillCardPanel().getHeadItem("varrordercode").setEnabled(
						false);
				if (getStateStr().equals("到货浏览")) {
					setBtnLines(false);
				}
			}
			// 转入修改：设置单据号、置保质期天数
			if (getStateStr().equals("转入修改")) {
				getBillCardPanel().getHeadItem("varrordercode").setValue(null);
				getBillCardPanel().getHeadItem("varrordercode").setEnabled(
						getBillCardPanel().getHeadItem("varrordercode")
								.isEdit());
				// 缓存保质期天数
				String[] arrStrCmangids = null;
				ArrayList aryCmangidValiddays = null;
				if (vCmangids.size() > 0) {
					arrStrCmangids = new String[vCmangids.size()];
					vCmangids.copyInto(arrStrCmangids);
					try {
						aryCmangidValiddays = ArriveorderHelper
								.getValiddays(arrStrCmangids);
						m_hValiddays = new Hashtable();
						Integer validdays = null;
						for (int i = 0; i < arrStrCmangids.length; i++) {
							validdays = aryCmangidValiddays.get(i) == null ? null
									: (Integer) aryCmangidValiddays.get(i);
							if (validdays != null) {
								m_hValiddays.put(arrStrCmangids[i], validdays);
							}
						}
					} catch (Exception ex) {
						SCMEnv.out("缓存保质期天数时出错[nc.ui.rc.receive.ArriveUI.setArriveVOsToArrCard()");
					}
				}
				for (int i = 0; i < bm.getRowCount(); i++) {
					strCmangid = (String) bm.getValueAt(i, "cmangid");
					bm.setValueAt(m_hValiddays.get(strCmangid), i, "ivalidday");
				}
			}
			// 设置状态图片
			try {
				getBillCardPanel().update(getGraphics());
			} catch (Exception e) {
				SCMEnv.out("加载图片时出错(不影响业务操作)");
			}
			// 加载来源信息
			PuTool.loadSourceInfoAll(getBillCardPanel(),
					BillTypeConst.PO_ARRIVE);
		} else {
			if (getBillCardPanel().getBillData() != null) {
				// V5 Del : setImageType(this.IMAGE_NULL);
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
			}
		}
		// 无论有无数据业务类型总有值
		if (getBusitype() != null) {
			refBusi.setPK(getBusitype());
		}
		// 数值型字段正负属性设置
		setNumFieldsNeg(isBackBill()); // isBackBill()必须在单据模板置值之后
		// 退货理由(头体)
		boolean bIsEdit = getStateStr().equals("到货修改")
				|| getStateStr().equals("转入修改");
		if (getBillCardPanel().getHeaderPanel("vbackreasonh") != null) {
			getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(
					isBackBill()
							&& bIsEdit
							&& getBillCardPanel().getHeadItem("vbackreasonh")
									.isEdit());
			getBillCardPanel().getHeadItem("vbackreasonh").setEdit(
					isBackBill()
							&& bIsEdit
							&& getBillCardPanel().getHeadItem("vbackreasonh")
									.isEdit());
		}
		if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
			getBillCardPanel().getBodyItem("vbackreasonb").setEdit(
					isBackBill()
							&& bIsEdit
							&& getBillCardPanel().getBodyItem("vbackreasonb")
									.isEdit());
	}

	/**
	 * @功能：向询价单列表界面写数据
	 * @作者：晁志平 创建日期：(2001-6-7 17:25:38)
	 */
	private void loadDataToList() {

		if (getCacheVOs() != null) {
			getBillListPanel().getBodyBillModel().clearBodyData();
			ArriveorderHeaderVO[] headers = null;
			headers = getArriveHeaderVOs(getCacheVOs());
			getBillListPanel().setHeaderValueVO(headers);
			// 显示表头备注、退货理由
			for (int i = 0; i < headers.length; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						headers[i].getVmemo(), i, "vmemo");
				getBillListPanel().getHeadBillModel().setValueAt(
						headers[i].getVbackreasonh(), i, "vbackreasonh");
			}
			getBillListPanel().getHeadBillModel().execLoadFormula();
		} else {
			if (getBillListPanel().getHeadBillModel() != null) {
				getBillListPanel().getHeadBillModel().clearBodyData();
			}
			if (getBillListPanel().getBodyBillModel() != null) {
				getBillListPanel().getBodyBillModel().clearBodyData();
			}
		}
	}

	/**
	 * 功能描述:行变化或编辑后处理： 1。设置辅计量参照； 2。设置换算率； //3。设置是否固定换算率； 4。控制辅计量及辅信息的编辑状态
	 */
	private void setAssisUnitEditState2(BillEditEvent event) {

		if (event.getRow() < 0) {
			return;
		}
		// 是否进行辅计量管理
		String strCbaseid = (String) getBillCardPanel().getBillModel()
				.getValueAt(event.getRow(), "cbaseid");
		if (getBillCardPanel().getBodyValueAt(event.getRow(), "narrvnum") == null
				|| getBillCardPanel()
						.getBodyValueAt(event.getRow(), "narrvnum").toString()
						.equals("")
				|| (new UFDouble(getBillCardPanel().getBodyValueAt(
						event.getRow(), "narrvnum").toString())).doubleValue() >= 0.0) {
		} else {
		}
		if (strCbaseid != null && !strCbaseid.trim().equals("")
				&& nc.ui.pu.pub.PuTool.isAssUnitManaged(strCbaseid)) {
			// 设置辅计量参照
			setRefPaneAssistunit(event.getRow());
			// 设置可编辑性
			getBillCardPanel().setCellEditable(event.getRow(), "convertrate",
					getBillCardPanel().getBodyItem("convertrate").isEdit());
			getBillCardPanel().setCellEditable(event.getRow(), "nassistnum",
					getBillCardPanel().getBodyItem("nassistnum").isEdit());
			getBillCardPanel().setCellEditable(event.getRow(),
					"cassistunitname",
					getBillCardPanel().getBodyItem("cassistunitname").isEdit());
			String cassistunit = (String) getBillCardPanel().getBillModel()
					.getValueAt(event.getRow(), "cassistunit");
			// 辅计量为空,各数量不可编辑
			if (cassistunit == null || cassistunit.trim().equals("")) {
				getBillCardPanel().setCellEditable(
						event.getRow(),
						"cassistunitname",
						getBillCardPanel().getBodyItem("cassistunitname")
								.isEdit());
				getBillCardPanel().setCellEditable(event.getRow(),
						"nassistnum",
						getBillCardPanel().getBodyItem("nassistnum").isEdit());
				getBillCardPanel().setCellEditable(event.getRow(), "nmoney",
						false);
				getBillCardPanel().setCellEditable(event.getRow(), "narrvnum",
						false);
				getBillCardPanel().setCellEditable(event.getRow(), "nelignum",
						false);
				getBillCardPanel().setCellEditable(event.getRow(),
						"npresentnum", false);
				getBillCardPanel().setCellEditable(event.getRow(), "nwastnum",
						false);
				getBillCardPanel().setCellEditable(event.getRow(),
						"convertrate", false);
			} else { // 辅计量不为空
				// 设置换算率
				UFDouble ufdConv = nc.ui.pu.pub.PuTool.getInvConvRateValue(
						strCbaseid, cassistunit);
				Object oTmp = getBillCardPanel().getBillModel().getValueAt(
						event.getRow(), "convertrate");
				if (oTmp == null || oTmp.toString().trim().equals("")) {
					getBillCardPanel().getBillModel().setValueAt(ufdConv,
							event.getRow(), "convertrate");
				}
				// 设置可编辑性
				getBillCardPanel().setCellEditable(
						event.getRow(),
						"cassistunitname",
						getBillCardPanel().getBodyItem("cassistunitname")
								.isEdit());
				getBillCardPanel().setCellEditable(event.getRow(),
						"nassistnum",
						getBillCardPanel().getBodyItem("nassistnum").isEdit());
				getBillCardPanel().setCellEditable(event.getRow(), "nmoney",
						getBillCardPanel().getBodyItem("nmoney").isEdit());
				getBillCardPanel().setCellEditable(event.getRow(), "narrvnum",
						getBillCardPanel().getBodyItem("narrvnum").isEdit());
				// 如果到货是正到货
				/*
				 * delete 2003-10-22 if (!bIsNegative) {
				 * getArrBillCardPanel().setCellEditable(event.getRow(),
				 * "nelignum", false); } else {
				 * getArrBillCardPanel().setCellEditable(event.getRow(),
				 * "nelignum", true); }
				 */
				getBillCardPanel().setCellEditable(event.getRow(),
						"npresentnum",
						getBillCardPanel().getBodyItem("npresentnum").isEdit());
				getBillCardPanel().setCellEditable(event.getRow(), "nwastnum",
						getBillCardPanel().getBodyItem("nwastnum").isEdit());
				getBillCardPanel().setCellEditable(event.getRow(),
						"convertrate",
						getBillCardPanel().getBodyItem("convertrate").isEdit());
				// 如果辅计量是固定换算率
				if (nc.ui.pu.pub.PuTool.isFixedConvertRate(strCbaseid,
						cassistunit)) {
					getBillCardPanel().setCellEditable(event.getRow(),
							"convertrate", false);
				}
				// 如果是主辅计量相同,则换算率不可编辑
				String ass = (String) getBillCardPanel().getBillModel()
						.getValueAt(event.getRow(), "cassistunitname");
				String main = (String) getBillCardPanel().getBillModel()
						.getValueAt(event.getRow(), "cmainmeasname");
				if (ass != null && ass.equals(main)) {
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(1), event.getRow(), "convertrate");
					getBillCardPanel().setCellEditable(event.getRow(),
							"convertrate", false);

				}
			}
		} else {
			// 没有辅计量管理时处理辅信息为空(模板中部分辅信息要保存时处理，用户不可见)
			getBillCardPanel().setCellEditable(event.getRow(), "convertrate",
					false);
			getBillCardPanel().setCellEditable(event.getRow(), "nassistnum",
					false);
			getBillCardPanel().setCellEditable(event.getRow(),
					"cassistunitname", false);
			getBillCardPanel().getBillModel().setValueAt(null, event.getRow(),
					"convertrate");
			getBillCardPanel().getBillModel().setValueAt(null, event.getRow(),
					"nassistnum");
			getBillCardPanel().getBillModel().setValueAt(null, event.getRow(),
					"cassistunitname");
			getBillCardPanel().getBillModel().setValueAt(null, event.getRow(),
					"nassistnum");
			getBillCardPanel().getBillModel().setValueAt(null, event.getRow(),
					"cassistunit");
		}
	}

	/**
	 * 功能描述:设置退货理由是否可编辑
	 */
	private void setBackReasonEditable() {
		if (isBackBill()) {
			if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
				getBillCardPanel().getHeadItem("vbackreasonh")
						.setEnabled(
								getBillCardPanel().getHeadItem("vbackreasonh")
										.isEdit());
				getBillCardPanel().getHeadItem("vbackreasonh")
						.setEdit(
								getBillCardPanel().getHeadItem("vbackreasonh")
										.isEdit());
			}
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				getBillCardPanel().getBodyItem("vbackreasonb")
						.setEnabled(
								getBillCardPanel().getBodyItem("vbackreasonb")
										.isEdit());
				getBillCardPanel().getBodyItem("vbackreasonb")
						.setEdit(
								getBillCardPanel().getBodyItem("vbackreasonb")
										.isEdit());
			}
		} else {
			if (getBillCardPanel().getHeadItem("vbackreasonh") != null)
				getBillCardPanel().getHeadItem("vbackreasonh")
						.setEnabled(false);
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				getBillCardPanel().getBodyItem("vbackreasonb")
						.setEnabled(false);
				getBillCardPanel().getBodyItem("vbackreasonb").setEdit(false);
			}
		}
	}

	/**
	 * 设置行操作是否可用
	 */
	private void setBtnLines(boolean isEnable) {

		m_btnLines.setEnabled(isEnable);
		int iLne = m_btnLines.getChildCount();
		for (int i = 0; i < iLne; i++) {
			((ButtonObject) m_btnLines.getChildren().elementAt(i))
					.setEnabled(isEnable);
			updateButton((ButtonObject) m_btnLines.getChildren().elementAt(i));
		}
		UIMenuItem[] menuitems = getBillCardPanel().getBodyMenuItems();
		if (menuitems != null && menuitems.length > 0) {
			for (int i = 0; i < menuitems.length; i++) {
				menuitems[i].setEnabled(isEnable);
			}
		}
	}

	/**
	 * @功能：设置到货列表状态下的按钮
	 * @作者：晁志平 创建日期：(2001-6-20 7:58:28)
	 */
	private void setButtonsList() {

		// 列表特殊
		m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000463")/*
									 * @res "卡片显示"
									 */);

		/* 结束转单 */
		m_btnEndCreate.setEnabled(false);
		m_btnEndCreate.setVisible(false);
		// 业务类型
		m_btnBusiTypes.setEnabled(false);
		m_btnAdds.setEnabled(false);
		m_btnSave.setEnabled(false);
		m_btnBacks.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnLines.setEnabled(false);
		m_btnRefresh.setEnabled(m_bQueriedFlag);
		m_btnLocate.setEnabled(false);
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);
		m_btnCombin.setEnabled(false);
		m_btnCheck.setEnabled(false);
		m_btnQueryForAudit.setEnabled(false);
		m_btnSendAudit.setEnabled(false);
		m_btnLookSrcBill.setEnabled(false);
		m_btnQuickReceive.setEnabled(false);
		m_btnImportBill.setEnabled(false);
		m_btnImportXml.setEnabled(true);		 //XML导入  yqq 2016-11-02 测试
		m_btnMaintains.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnPrints.setEnabled(true);

		/* 存量查询、成套件查询、文档管理、预览、打印、修改、作废、审批、弃审、全选、全消、卡片显示/列表显示 */

		int iDataCnt = getCacheVOs() == null ? 0 : getCacheVOs().length;
		int iSeltCnt = getBillListPanel().getHeadTable().getSelectedRowCount();

		// 缓存无数据
		if (iDataCnt == 0) {
			m_btnUsable.setEnabled(false);
			m_btnQueryBOM.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnAudit.setEnabled(false);
			m_btnUnAudit.setEnabled(false);
			m_btnSelectAll.setEnabled(false);
			m_btnSelectNo.setEnabled(false);
			m_btnCard.setEnabled(true);
			//
			updateButtonsAll();
			return;
		}
		// 未选中数据
		if (iSeltCnt == 0) {
			m_btnUsable.setEnabled(false);
			m_btnQueryBOM.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnAudit.setEnabled(false);
			m_btnUnAudit.setEnabled(false);
			m_btnSelectAll.setEnabled(false);
			m_btnSelectNo.setEnabled(false);
			m_btnCard.setEnabled(false);
			//
			updateButtonsAll();
			return;
		}

		/* 存量查询、成套件查询、文档管理、预览、打印、修改、作废、审批、弃审、全选、全消、卡片显示/列表显示 */
		boolean bOnlyOneSelected = (iSeltCnt == 1);
		boolean bAllSelected = (iSeltCnt == iDataCnt);

		//
		m_btnUsable.setEnabled(bOnlyOneSelected);
		m_btnQueryBOM.setEnabled(bOnlyOneSelected);
		m_btnCard.setEnabled(bOnlyOneSelected);
		if (bOnlyOneSelected) {
			m_btnModify.setEnabled(getCacheVOs()[getDispIndex()]
					.isCanBeModified());
			m_btnDiscard.setEnabled(getCacheVOs()[getDispIndex()]
					.isCanBeModified());
			m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
			m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()]
					.isCanUnAudit());
		} else {
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(true);
			m_btnAudit.setEnabled(true);
			m_btnUnAudit.setEnabled(true);
		}
		m_btnDocument.setEnabled(true);
		m_btnPrint.setEnabled(true);
		m_btnPrintPreview.setEnabled(true);
		m_btnSelectNo.setEnabled(true);
		m_btnSelectAll.setEnabled(!bAllSelected);
		//
		updateButtonsAll();

	}

	/**
	 * @功能：订单生成的到货单列表(未保存时)按钮逻辑
	 */
	private void setButtonsListNew() {
		//
		for (int i = 0; i < m_aryArrListButtons.length; i++) {
			m_aryArrListButtons[i].setEnabled(false);
		}
		m_btnCancel.setEnabled(false);
		/* 只有“切换”、“放弃转单”按钮可用 */
		m_btnModifyList.setEnabled(true);
		m_btnEndCreate.setVisible(true);
		m_btnEndCreate.setEnabled(true);
		//
		updateButtonsAll();
	}

	/**
	 * 消息中心按钮逻辑
	 * 
	 */
	private void setButtonsMsgCenter() {

		// 审批
		m_btnAudit.setEnabled(true);
		updateButton(m_btnAudit);
		// 弃审
		m_btnUnAudit.setEnabled(true);
		updateButton(m_btnUnAudit);
		// 状态查询
		m_btnQueryForAudit.setEnabled(true);
		updateButton(m_btnQueryForAudit);
		// 文档管理
		m_btnDocument.setEnabled(true);
		updateButton(m_btnDocument);
		// 逐级联查
		m_btnLookSrcBill.setEnabled(true);
		updateButton(m_btnLookSrcBill);
	}

	/**
	 * @功能：到货浏览状态下按钮设置
	 */
	private void setButtonsCard() {
		//
		setButtonsInit();

		if (getCacheVOs() != null && getCacheVOs().length >= 1) {
			// 打印、定位、刷新、修改、作废、辅助
			m_btnPrint.setEnabled(true);
			m_btnCombin.setEnabled(true);
			m_btnPrintPreview.setEnabled(true);
			m_btnLocate.setEnabled(true);
			m_btnRefresh.setEnabled(m_bQueriedFlag);
			m_btnRefreshList.setEnabled(m_bQueriedFlag);
			// 修改、作废
			if (getCacheVOs()[getDispIndex()].isCanBeModified()) {
				m_btnModify.setEnabled(true);
				if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
					m_btnDiscard.setEnabled(false);
				} else {
					m_btnDiscard.setEnabled(true);
				}
			} else {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
			}
			// 辅助
			m_btnOthers.setEnabled(true);
			// 单据维护
			m_btnMaintains.setEnabled(true);
			// 浏览
			m_btnBrowses.setEnabled(true);
			// 存量查询
			m_btnUsable.setEnabled(true);
			// 成套件
			m_btnQueryBOM.setEnabled(true);
			// 行操作
			setBtnLines(false);
			// 文档管理
			m_btnDocument.setEnabled(true);
			// 状态查询
			m_btnQueryForAudit.setEnabled(true);
			// 逐级联查
			m_btnLookSrcBill.setEnabled(true);
			// 快速收货
			m_btnQuickReceive.setEnabled(true);
			// 上下首末张逻辑
			if (getCacheVOs().length == 1) {
				m_btnFirst.setEnabled(false);
				m_btnPrev.setEnabled(false);
				m_btnNext.setEnabled(false);
				m_btnLast.setEnabled(false);
			} else if (getDispIndex() != getCacheVOs().length - 1
					&& getDispIndex() != 0) {
				m_btnFirst.setEnabled(true);
				m_btnPrev.setEnabled(true);
				m_btnNext.setEnabled(true);
				m_btnLast.setEnabled(true);
			} else if (getDispIndex() == 0) {
				m_btnFirst.setEnabled(false);
				m_btnPrev.setEnabled(false);
				m_btnNext.setEnabled(true);
				m_btnLast.setEnabled(true);
			} else {
				m_btnFirst.setEnabled(true);
				m_btnPrev.setEnabled(true);
				m_btnNext.setEnabled(false);
				m_btnLast.setEnabled(false);
			}
			// 送审按钮
			m_btnSendAudit
					.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
			//
			m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
			m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()]
					.isCanUnAudit());

			// 不支持质量管理未启用情况，因为维护没有录入合格、不合格数量的功能
			m_btnCheck.setEnabled(m_bQcEnabled);

		}
		// 查询刷新
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML导入  yqq 2016-11-02 测试
		m_btnRefresh.setEnabled(m_bQueriedFlag);
		//
		updateButtonsAll();
	}

	/**
	 * @功能：修改按钮逻辑
	 * @作者：晁志平 创建日期：(2001-6-20 13:39:39)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void setButtonsModify() {
		//
		m_btnEndCreate.setVisible(false);
		//
		for (int i = 0; i < m_aryArrCardButtons.length; i++) {
			if (PuTool.isExist(getExtendBtns(), m_aryArrCardButtons[i])) {
				continue;
			}
			m_aryArrCardButtons[i].setEnabled(false);
		}
		int iLen = m_btnOthers.getChildCount();
		for (int i = 0; i < iLen; i++) {
			((ButtonObject) m_btnOthers.getChildren().elementAt(i))
					.setEnabled(false);
		}
		iLen = m_btnBacks.getChildCount();
		for (int i = 0; i < iLen; i++) {
			((ButtonObject) m_btnBacks.getChildren().elementAt(i))
					.setEnabled(false);
		}
		// 辅助
		m_btnOthers.setEnabled(true);
		// 快速收货
		m_btnQuickReceive.setEnabled(false);
		// 存量查询
		m_btnUsable.setEnabled(true);
		// 成套件
		m_btnQueryBOM.setEnabled(true);
		m_btnRefresh.setEnabled(false);
		m_btnLocate.setEnabled(false);
		m_btnDocument.setEnabled(false);
		m_btnLookSrcBill.setEnabled(false);

		// 单据维护
		m_btnMaintains.setEnabled(true);
		m_btnSave.setEnabled(true);
		m_btnCancel.setEnabled(true);
		m_btnModify.setEnabled(false);
		m_btnDiscard.setEnabled(false);
		m_btnImportBill.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML导入  yqq 2016-11-02 测试
		// 浏览
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(false);
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);

		// 送审按钮
		m_btnSendAudit
				.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));

		setBtnLines(true);

		//
		updateButtonsAll();
	}

	/**
	 * @功能：订单生成的到货单卡片界面按钮逻辑
	 * @作者：晁志平 创建日期：(2001-7-31 18:42:07)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void setButtonsModifyNew() {

		setButtonsModify();

		m_btnList.setEnabled(false);
		updateButton(m_btnList);

	}

	/**
	 * 设置按钮状态 创建日期：(2001-3-17 9:00:09)
	 */
	private void setButtonsState() {

		int iVal = -999;// 支持产业链功能扩展

		if (getStateStr().equals("初始化")) {
			setButtonsInit();
			iVal = 0;
		} else if (getStateStr().equals("到货浏览")) {
			setButtonsCard();
			iVal = 1;
		} else if (getStateStr().equals("到货修改")) {
			setButtonsModify();
			iVal = 2;
		} else if (getStateStr().equals("到货列表")) {
			setButtonsList();
			iVal = 3;
		} else if (getStateStr().equals("转入列表")) {
			setButtonsListNew();
			iVal = 4;
		} else if (getStateStr().equals("转入修改")) {
			setButtonsModifyNew();
			iVal = 5;
		} else if (getStateStr().equals("消息中心")) {
			setButtonsMsgCenter();
			iVal = 6;
		}
		setExtendBtnsStat(iVal);
	}

	/**
	 * 功能描述:改变界面按钮状态
	 */
	private void updateButtonsAll() {
		int iLen = getBtnTree().getButtonArray().length;
		for (int i = 0; i < iLen; i++) {
			update(getBtnTree().getButtonArray()[i]);
		}
	}

	/**
	 * 创建日期： 2005-9-20 功能描述： 更新按钮状态（递归方式）
	 */
	private void update(ButtonObject bo) {
		updateButton(bo);
		if (bo.getChildCount() > 0) {
			for (int i = 0, len = bo.getChildCount(); i < len; i++)
				update(bo.getChildButtonGroup()[i]);
		}
	}

	/**
	 * 获取按钮树，类唯一实例。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return <p>
	 * @author czp
	 * @time 2007-3-13 下午01:16:48
	 */
	private ButtonTree getBtnTree() {
		if (m_btnTree == null) {
			try {
				m_btnTree = new ButtonTree("40040301");
			} catch (BusinessException be) {
				showHintMessage(be.getMessage());
				return null;
			}
		}
		return m_btnTree;
	}

	/**
	 * 功能：在编辑数量或辅数量后设置: 1.如果为空或者大于0，合格数量、不合格数量不可编辑；赠品数量、途耗数量、金额同正；
	 * 2.如果小于0，合格数量可编辑，赠品数量、途耗数量、金额、合格、不合格同负；
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void setEditAndDirect(BillEditEvent e) {
		boolean bIsNegative = false;
		if (getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum") == null
				|| getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum")
						.toString().equals("")
				|| (new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow(),
						"narrvnum").toString())).doubleValue() >= 0.0) {
			bIsNegative = false;
		} else {
			bIsNegative = true;
		}
		// #正到货
		if (!bIsNegative) {
			// 合格数量不可编辑
			/*
			 * delete 2003-10-22
			 * getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum",
			 * false);
			 */
			// 合格数量与到货数量同正
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMinValue(0.0);

			// 赠品同正
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			// //途耗同正
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			// 金额同正
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// #负到货
		else {
			// 合格数量可编辑
			/*
			 * delete 2003-10-22
			 * getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum",
			 * true);
			 */
			// 合格数量与到货数量同负
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// 赠品同负
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// 途耗同负
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// 金额同负
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
		}
	}

	/**
	 * @功能：到货单列表头变换时向表体表中写入相应子表内容
	 * @作者：晁志平 创建日期：(2001-6-8 16:41:42) 修改：为提高效率，要增加对指定表头加载表体的操作 0530
	 * @param row0
	 *            int
	 */
	private boolean setListBodyData(int row0) {
		boolean isErr = false;
		if (!getStateStr().equals("转入列表")) {
			items = null;
			try {
				getCacheVOs()[row0] = RcTool
						.getRefreshedVO(getCacheVOs()[row0]);
				if (getCacheVOs()[row0] != null
						&& getCacheVOs()[row0].getChildrenVO() != null
						&& getCacheVOs()[row0].getChildrenVO().length > 0) {
					items = (ArriveorderItemVO[]) getCacheVOs()[row0]
							.getChildrenVO();
				}
			} catch (Exception be) {
				getBillListPanel().getBodyBillModel().clearBodyData();
				if (be instanceof BusinessException) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000422")/* @res "业务异常" */, be
									.getMessage());
				}
				return true;
			}
			getBillListPanel().setBodyValueVO(items);
		} else {
			getBillListPanel().setBodyValueVO(
					getCacheVOs()[getDispIndex()].getChildrenVO());
		}
		getBillListPanel().getBodyBillModel().execLoadFormula();
		BillModel bm = getBillListPanel().getBodyBillModel();
		// 逐行处理 ------------------------------------------------ 开始
		String strCbaseid = null;
		String strCmain = null;
		String strCassid = null;
		Object oNarrvnum = null;
		Object oNassinum = null;
		UFDouble ufdNarrvnum = null;
		UFDouble ufdNassinum = null;
		Object oValue = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			// 表体备注初始化
			if (bm.getValueAt(i, "vmemo") == null) {
				bm.setValueAt("", i, "vmemo");
			}
			strCbaseid = (String) bm.getValueAt(i, "cbaseid");
			// strCmangid = (String) bm.getValueAt(i, "cmangid");
			strCassid = (String) bm.getValueAt(i, "cassistunit");
			strCmain = (String) bm.getValueAt(i, "cmainmeasid");
			// 是否辅计量管理
			UFBoolean bIsAssMana = new UFBoolean(
					PuTool.isAssUnitManaged(strCbaseid));
			if (bIsAssMana.booleanValue()) {
				if (strCassid == null || strCassid.trim().equals("")) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "错误" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000190")/*
														 * @res
														 * "有辅计量管理的存货行存在空辅计量！"
														 */);
					return true;
				}
				// 设置换算率
				UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
						strCassid);
				bm.setValueAt(convert, i, "convertrate");
				// 如果主辅计量相同
				if (strCmain != null && strCmain.equals(strCassid)) {
					bm.setValueAt(new UFDouble(1.0), i, "convertrate");
				}
				// 如果不是固定换算率，则用主数量/辅数量取得换算率
				if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
					oNarrvnum = bm.getValueAt(i, "narrvnum");
					oNassinum = bm.getValueAt(i, "nassistnum");
					if (!(oNarrvnum == null || oNarrvnum.toString().trim()
							.equals(""))
							&& !(oNassinum == null || oNassinum.toString()
									.trim().equals(""))) {
						ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
						ufdNassinum = new UFDouble(oNassinum.toString().trim());
						oValue = ufdNassinum == new UFDouble(0) ? null
								: ufdNarrvnum.div(ufdNassinum);
					} else
						oValue = null;
					bm.setValueAt(oValue, i, "convertrate");
				}
			} else {
				bm.setValueAt(null, i, "convertrate");
				bm.setValueAt(null, i, "isfixedrate");
			}
		}
		// 逐行处理 -------------------------------------------------- 结束

		// 加载来源信息、源头信息
		PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_ARRIVE);
		//
		return isErr;
	}

	/**
	 * 功能：列表换行时发生并发后列表按钮逻辑设置 作者：晁志平 日期：(2003-2-24 17:02:24)
	 */
	private void setButtonsListWhenErr() {
		//
		for (int i = 0; i < m_aryArrListButtons.length; i++) {
			m_aryArrListButtons[i].setEnabled(false);
		}
		//
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML导入  yqq 2016-11-02 测试
		m_btnBrowses.setEnabled(true);
		m_btnRefresh.setEnabled(true);
		//
		updateButtonsAll();
	}

	/**
	 * @功能：设置到货单据数组
	 * @作者：晁志平 创建日期：(2001-6-19 20:13:12)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_arriveVOs
	 *            nc.vo.rc.receive.ArriveorderVO[]
	 */
	private void setCacheVOs(nc.vo.rc.receive.ArriveorderVO[] newM_arriveVOs) {
		m_arriveVOs = newM_arriveVOs;
	}

	/**
	 * @功能：设置当前显示索引号
	 * @作者：晁志平 创建日期：(2001-6-20 8:47:47)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_iArrCurrRow
	 *            int
	 */
	private void setDispIndex(int newM_iArrCurrRow) {
		m_iArrCurrRow = newM_iArrCurrRow;
	}

	/**
	 * @功能：设置当前单据维护状态
	 * @作者：晁志平 创建日期：(2001-6-19 20:18:22)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
	private void setM_strState(java.lang.String newM_strState) {
		m_strState = newM_strState;
	}

	/**
	 * 功能描述:设置数值类型字段取值范围 / | false: 任意取值 isBack = | | true : 负 \
	 */
	private void setNumFieldsNeg(boolean isBack) {
		double iMin = nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM;
		double iMax = nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM;
		if (isBack) {
			iMax = 0;
		}
		// 本次收货
		UIRefPane refNarrvnum = (UIRefPane) getBillCardPanel().getBodyItem(
				"narrvnum").getComponent();
		refNarrvnum.setMinValue(iMin);
		refNarrvnum.setMaxValue(iMax);
		// 辅数量
		UIRefPane refNassistnum = (UIRefPane) getBillCardPanel().getBodyItem(
				"nassistnum").getComponent();
		refNassistnum.setMinValue(iMin);
		refNassistnum.setMaxValue(iMax);
		refNarrvnum.setMinValue(iMin);
		refNarrvnum.setMaxValue(iMax);
		refNarrvnum.setMinValue(iMin);
		refNarrvnum.setMaxValue(iMax);
		refNarrvnum.setMinValue(iMin);
		refNarrvnum.setMaxValue(iMax);
	}

	/**
	 * 功能：到货转入时切换、修改、作废、全选、全消、文档管理按钮显示逻辑及当前单据定位 按钮逻辑
	 * 
	 * @切换：有且只有一行选中时有效
	 * @修改：无效
	 * @打印：无效
	 * @作废：无效
	 * @全选：无效
	 * @全消：无效
	 * @文档管理：无效
	 * @存量查询：有效
	 * @放弃转单：有效
	 */
	private void setButtonsListValueChangedNew(int cnt) {
		for (int i = 0; i < m_aryArrListButtons.length; i++) {
			m_aryArrListButtons[i].setEnabled(false);
		}
		m_btnMaintains.setEnabled(true);
		//
		m_btnDiscard.setEnabled(false);
		//
		int iLen = m_btnOthers.getChildCount();
		for (int i = 0; i < iLen; i++) {
			((ButtonObject) m_btnOthers.getChildren().elementAt(i))
					.setEnabled(false);
		}
		m_btnOthersList.setEnabled(true);
		/* “放弃转单”可用 */
		m_btnEndCreate.setVisible(true);
		m_btnEndCreate.setEnabled(true);
		m_btnUsableList.setEnabled(true);
		m_btnQueryBOMList.setEnabled(true);
		if (cnt == 1) {
			m_btnModifyList.setEnabled(true);
		}
		//
		updateButtonsAll();
	}

	/**
	 * @功能：设置库存组织与仓库匹配
	 */
	private void setOrgWarhouse() {
		UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
				"cstoreorganization").getComponent();
		String sPkCalBody = ref.getRefPK();
		PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(),
				getCorpPrimaryKey(), sPkCalBody, "cwarehousename");
	}

	private void setRefPaneAssistunit(int row) {
		// 存货基本ID与主计量ID
		Object cbaseid = getBillCardPanel().getBillModel().getValueAt(row,
				"cbaseid");
		// 设置辅计量单位参照
		UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem(
				"cassistunitname").getComponent();
		String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
		ref.setWhereString(wherePart);
		String unionPart = " union all \n";
		unionPart += "(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n";
		unionPart += "from bd_invbasdoc \n";
		unionPart += "left join bd_measdoc  \n";
		unionPart += "on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n";
		unionPart += "where bd_invbasdoc.pk_invbasdoc='" + cbaseid + "') \n";
		ref.getRefModel().setGroupPart(unionPart);
	}

	/**
	 * 更换按钮 创建日期：(2001-3-17 9:00:09)
	 */
	private void updateButtonsMy() {
		if (getStateStr().equals("到货单列表"))
			setButtons(m_aryArrListButtons);
		else
			for (int i = 0; i < m_aryArrCardButtons.length; i++) {
				updateButton(m_aryArrCardButtons[i]);
			}
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		boolean isErr = false;
		if (!e.getValueIsAdjusting())
			return;
		int m_nFirstSelectedIndex = -1;
		// 选中行数
		int iSelCnt = 0;
		// 所有置为未选中
		int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		// 得到被选中的行
		int[] iaSelectedRow = getBillListPanel().getHeadTable()
				.getSelectedRows();
		if (iaSelectedRow == null || iaSelectedRow.length == 0) {
			m_nFirstSelectedIndex = -1;
		} else {
			iSelCnt = iaSelectedRow.length;
			// m_nFirstSelectedIndex = iaSelectedRow[0];
			// 选中的行表示为打＊号
			for (int i = 0; i < iSelCnt; i++) {
				getBillListPanel().getHeadBillModel().setRowState(
						iaSelectedRow[i], BillModel.SELECTED);
			}
		}
		if (iSelCnt == 1 && iaSelectedRow != null && iaSelectedRow.length > 0) {
			m_nFirstSelectedIndex = iaSelectedRow[0];
		}
		if (m_nFirstSelectedIndex < 0) {
			getBillListPanel().setBodyValueVO(null);
		} else {
			int nCurIndex = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), m_nFirstSelectedIndex);
			setDispIndex(nCurIndex);
			isErr = setListBodyData(nCurIndex);
			// 刷新
			getBillListPanel().getBodyTable().updateUI();
		}
		// 按钮逻辑
		if ("转入列表".equals(getStateStr())) {
			setButtonsListValueChangedNew(iSelCnt);
		} else {
			setButtonsList();
		}
		// 如果发生业务异常则重新设置功能按钮
		if (isErr) {
			setButtonsListWhenErr();
		}
		updateButtons();
	}

	/**
	 * 功能描述:自定义项保存PK(表体)
	 */
	private void setBodyDefPK(BillEditEvent event) {
		if (event.getKey().equals("vdef1")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef1", "pk_defdoc1");
		} else if (event.getKey().equals("vdef2")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef2", "pk_defdoc2");
		} else if (event.getKey().equals("vdef3")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef3", "pk_defdoc3");
		} else if (event.getKey().equals("vdef4")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef4", "pk_defdoc4");
		} else if (event.getKey().equals("vdef5")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef5", "pk_defdoc5");
		} else if (event.getKey().equals("vdef6")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef6", "pk_defdoc6");
		} else if (event.getKey().equals("vdef7")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef7", "pk_defdoc7");
		} else if (event.getKey().equals("vdef8")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef8", "pk_defdoc8");
		} else if (event.getKey().equals("vdef9")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef9", "pk_defdoc9");
		} else if (event.getKey().equals("vdef10")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef10", "pk_defdoc10");
		} else if (event.getKey().equals("vdef11")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef11", "pk_defdoc11");
		} else if (event.getKey().equals("vdef12")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef12", "pk_defdoc12");
		} else if (event.getKey().equals("vdef13")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef13", "pk_defdoc13");
		} else if (event.getKey().equals("vdef14")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef14", "pk_defdoc14");
		} else if (event.getKey().equals("vdef15")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef15", "pk_defdoc15");
		} else if (event.getKey().equals("vdef16")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef16", "pk_defdoc16");
		} else if (event.getKey().equals("vdef17")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef17", "pk_defdoc17");
		} else if (event.getKey().equals("vdef18")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef18", "pk_defdoc18");
		} else if (event.getKey().equals("vdef19")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef19", "pk_defdoc19");
		} else if (event.getKey().equals("vdef20")) {
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					event.getRow(), "vdef20", "pk_defdoc20");
		}
	}

	/**
	 * 功能描述:自定义项保存PK(表头)
	 */
	private void setHeadDefPK(BillEditEvent event) {
		if (event.getKey().equals("vdef1")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1",
					"pk_defdoc1");
		} else if (event.getKey().equals("vdef2")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2",
					"pk_defdoc2");
		} else if (event.getKey().equals("vdef3")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3",
					"pk_defdoc3");
		} else if (event.getKey().equals("vdef4")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4",
					"pk_defdoc4");
		} else if (event.getKey().equals("vdef5")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5",
					"pk_defdoc5");
		} else if (event.getKey().equals("vdef6")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6",
					"pk_defdoc6");
		} else if (event.getKey().equals("vdef7")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7",
					"pk_defdoc7");
		} else if (event.getKey().equals("vdef8")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8",
					"pk_defdoc8");
		} else if (event.getKey().equals("vdef9")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9",
					"pk_defdoc9");
		} else if (event.getKey().equals("vdef10")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef10", "pk_defdoc10");
		} else if (event.getKey().equals("vdef11")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef11", "pk_defdoc11");
		} else if (event.getKey().equals("vdef12")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef12", "pk_defdoc12");
		} else if (event.getKey().equals("vdef13")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef13", "pk_defdoc13");
		} else if (event.getKey().equals("vdef14")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef14", "pk_defdoc14");
		} else if (event.getKey().equals("vdef15")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef15", "pk_defdoc15");
		} else if (event.getKey().equals("vdef16")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef16", "pk_defdoc16");
		} else if (event.getKey().equals("vdef17")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef17", "pk_defdoc17");
		} else if (event.getKey().equals("vdef18")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef18", "pk_defdoc18");
		} else if (event.getKey().equals("vdef19")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef19", "pk_defdoc19");
		} else if (event.getKey().equals("vdef20")) {
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					"vdef20", "pk_defdoc20");
		}
	}

	private boolean chechDataBeforeSave(ArriveorderVO newvo, ArriveorderVO oldvo) {
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("采购到货保存检查操作chechDataBeforeSave开始");

		int nError = -1;
		// 检查单据行号
		if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res "保存失败" */);
			return false;
		}
		timer.addExecutePhase("检查单据行号verifyRowNosCorrect");
		// 检查单据模板非空项
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000191")/* @res "保存失败:单据项目存在空项" */);
			MessageDialog
					.showWarningDlg(this, m_lanResTool.getStrByID("40040301",
							"UPP40040301-000192")/* @res "单据模板非空项检查" */, e
							.getMessage());
			return false;
		}
		timer.addExecutePhase(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000193")/* @res "检查单据模板非空项validateNotNullField" */);
		try {
			// 检查表体数据正负合法性
			ArriveorderItemVO bodyVO[] = (ArriveorderItemVO[]) newvo
					.getChildrenVO();
			ArriveorderHeaderVO headVO = (ArriveorderHeaderVO) newvo
					.getParentVO();
			for (nError = 0; nError < bodyVO.length; nError++) {
				if (headVO.getBisback().booleanValue()
						&& bodyVO[nError].getNarrvnum() != null
						&& bodyVO[nError].getNarrvnum().doubleValue() > 0)
					throw new ValidationException(m_lanResTool.getStrByID(
							"40040301", "UPP40040301-000275")/* @res"退货单数量必须为负!" */);
				bodyVO[nError].validate();
			}
			// 检查录入数据其它合法性
			if (!checkModifyData(newvo, oldvo)) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* @res "保存失败" */);
				return false;
			}
			// 检查录入数据是否超出数据库可容纳范围
			if (!nc.vo.scm.field.pu.FieldDBValidate
					.validate((ArriveorderItemVO[]) newvo.getChildrenVO())) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000194")/* @res "存在部分数据超出数据库可容纳范围,请检查" */);
				return false;
			}
		} catch (ValidationException e) {
			String[] value = new String[] { String.valueOf(nError + 1),
					e.getMessage() };
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("40040301",
							"UPP40040301-000195")/* @res "合法性检查" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000196", null, value)/*
																	 * @res
																	 * "表体行"+
																	 * CommonConstant
																	 * .
																	 * BEGIN_MARK
																	 * + (nError
																	 * + 1) +
																	 * CommonConstant
																	 * .END_MARK
																	 * +
																	 * e.getMessage
																	 * ()
																	 */);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res "保存失败" */);
			return false;
		}
		timer.addExecutePhase("数据检查合法性");
		timer.showAllExecutePhase("采购到货保存检查操作chechDataBeforeSave结束");

		return true;
	}

	/**
	 * 作者：汪维敏 功能：计算打印次数 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
	 */
	private void onCardPrint() {
		ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(vo);
		try {
			if (printCard == null) {
				// 目前还有南京蒲镇不想补空行
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					// printCard = new
					// ScmPrintTool(getArrBillCardPanel(),printData,aryRslt);
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							printData, aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrint(getBillCardPanel(), getBillListPanel(),
					ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, printCard
							.getPrintMessage());
		} catch (Exception e1) {
			SCMEnv.out(e1);
		}
	}

	/**
	 * 作者：汪维敏 功能：计算打印次数 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
	 */
	private void onCardPrintPreview() {

		if (getCacheVOs() == null || getCacheVOs().length == 0) {
			return;
		}
		ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(vo);
		try {
			if (printCard == null) {
				// 目前还有南京蒲镇不想补空行
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					printCard = new ScmPrintTool(getBillCardPanel(), printData,
							aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrintPreview(getBillCardPanel(),
					getBillListPanel(), ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, printCard
							.getPrintMessage());
		} catch (Exception e1) {
			SCMEnv.out(e1);
		}
	}

	/**
	 * 作者：汪维敏 功能：批打印 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
	 */
	private void onBatchPrint() {
		if (printList == null) {
			printList = new ScmPrintTool(this, getBillCardPanel(),
					getSelectedBills(), getModuleCode());
		} else {
			try {
				printList.setData(getSelectedBills());
			} catch (BusinessException e1) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, e1
						.getMessage());
			}
		}
		try {
			printList.onBatchPrint(getBillListPanel(), getBillCardPanel(),
					ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, printList
							.getPrintMessage());
		} catch (BusinessException e) {

		}
	}

	/**
	 * 作者：汪维敏 功能：批打印 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
	 */
	private void onBatchPrintPreview() {
		if (printList == null) {
			printList = new ScmPrintTool(this, getBillCardPanel(),
					getSelectedBills(), getModuleCode());
		} else {
			try {
				printList.setData(getSelectedBills());
			} catch (BusinessException e1) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, e1
						.getMessage());
			}
		}
		try {
			printList.onBatchPrintPreview(getBillListPanel(),
					getBillCardPanel(), ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */, printList
							.getPrintMessage());
		} catch (BusinessException e) {
		}
	}

	/**
	 * 功能描述:加载单据模板之前的初始化
	 */

	private void initBillBeforeLoad(BillData bd) {

		// ---------单据模板加载前的初始化－－－－－－－
		if (bd != null && bd.getBodyItem("vfree0") != null
				&& bd.getBodyItem("vfree0") != null) {
			FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
			m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0")
					.getLength());
			// 加监听器
			m_firpFreeItemRefPane.getUIButton().addActionListener(this);
			bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
		}
		// 初始化参照
		initRefPane(bd);

		// 初始化ComboBox
		// initComboBox(bd);
		// 初始化精度
		// initDecimal(bd);

	}

	/**
	 * 二次开发功能扩展按钮，要求二次开发子类给出具体实现
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * 点击二次开发按钮后的响应处理，要求二次开发子类给出具体实现
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
	}

	/**
	 * 二次开发状态与原有界面状态处理绑定，要求二次开发子类给出具体实现
	 * 
	 * 状态数值对照表：
	 * 
	 * 0：初始化 1：到货浏览 2：到货修改 3：到货列表 4：转入列表 5：转入修改
	 */
	public void setExtendBtnsStat(int iState) {
	}

	/**
	 * 送审到货单
	 * <p>
	 * <strong>调用模块：</strong>采购管理
	 * <p>
	 * <strong>最后修改人：</strong>czp
	 * <p>
	 * <strong>最后修改日期：</strong>2006-02-09
	 * <p>
	 * <strong>用例描述：</strong>
	 * <p>
	 * 
	 * @param 无
	 * @return 无
	 * @throws 无
	 * @since NC50
	 * @see
	 */
	private void onSendAudit() {

		// 编辑状态送审＝“保存”＋“送审”
		if (getStateStr().equals("转入修改") || getStateStr().equals("到货修改")) {
			onSave();
		}
		// 该操作人是否有审批任务
		if (getCacheVOs() == null || getDispIndex() < 0)
			return;
		ArriveorderVO vo = getCacheVOs()[getDispIndex()];
		if (isNeedSendAudit(getCacheVOs()[getDispIndex()])) {
			try {
				PfUtilClient.processAction("SAVE", BillTypeConst.PO_ARRIVE,
						ClientEnvironment.getInstance().getDate().toString(),
						vo);
			} catch (Exception e) {
				SCMEnv.out("到货单送审失败：");
				SCMEnv.out(e);
				if (e instanceof BusinessException
						|| e instanceof RuntimeException) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000270")/* @res "提示" */, e
									.getMessage());
				} else {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000270")/* @res "提示" */,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000408")/* @res"送审失败！" */);
				}
			}
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH023")/*
																	 * @res
																	 * "已送审"
																	 */);
	}

	/**
	 * 判断到货单是否有必要送审
	 * <p>
	 * <strong>调用模块：</strong>采购管理
	 * <p>
	 * <strong>最后修改人：</strong>czp
	 * <p>
	 * <strong>最后修改日期：</strong>2006-02-09
	 * <p>
	 * <strong>用例描述：</strong>要求同时满足，
	 * <p>
	 * 1)、单据状态为“自由”(目前与请购单、采购订单保持一致，审批不通过需要[修改]-[保存]，即将单据状态置为自由，才可送审)
	 * <p>
	 * 2)、定义了审批流
	 * <p>
	 * 
	 * @param 无
	 * @return 无
	 * @throws 无
	 * @since NC50
	 * @see
	 */
	private boolean isNeedSendAudit(ArriveorderVO vo) {

		boolean bRet = false;
		if (vo == null || vo.getHeadVO() == null)
			return false;
		String billid = vo.getHeadVO().getCarriveorderid();
		String busiType = vo.getHeadVO().getCbiztype();
		boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(
				BillTypeConst.PO_ARRIVE, getCorpPrimaryKey(), busiType, billid,
				getClientEnvironment().getUser().getPrimaryKey());
		bRet = (isNeedSendToAuditQ && vo.getHeadVO().getIbillstatus() != null && vo
				.getHeadVO().getIbillstatus().intValue() == 0);
		return bRet;
	}

	/**
	 * 获取当前VO，消息中心用
	 * <p>
	 * <strong>调用模块：</strong>采购管理
	 * <p>
	 * <strong>最后修改人：</strong>czp
	 * <p>
	 * <strong>最后修改日期：</strong>2006-02-10
	 * <p>
	 * <strong>用例描述：</strong>
	 * <p>
	 * 
	 * @param 无
	 * @return 消息中心显示的业务单据VO
	 * @throws 无
	 * @since NC50
	 * @see
	 */
	public AggregatedValueObject getVo() throws Exception {
		ArriveorderVO vo = null;
		if (getCacheVOs() != null && getCacheVOs().length == 1
				&& getCacheVOs()[0] != null) {
			SCMEnv.out("缓存中有值，不必重新查询!");
			return getCacheVOs()[0];
		}
		try {
			vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
		} catch (Exception e) {
			PuTool.outException(this, e);
		}
		return vo;
	}

	/**
	 * 查询当前单据审批状态
	 */
	private void onQueryForAudit() {
		if (getCacheVOs() != null && getCacheVOs().length > 0
				&& getCacheVOs()[0] != null
				&& getCacheVOs()[0].getHeadVO() != null) {
			FlowStateDlg approvestatedlg = new FlowStateDlg(this,
					BillTypeConst.PO_ARRIVE, getCacheVOs()[0].getHeadVO()
							.getPrimaryKey());
			approvestatedlg.showModal();
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH035")/*
																	 * @res
																	 * "审批状态查询成功"
																	 */);
	}

	/**
	 * <p>
	 * 排序方法，返回要排序的缓存VO数组
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArray() {
		return getCacheVOs();
	}

	/**
	 * 界面关联接口方法实现 -- 维护
	 **/
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		SCMEnv.out("进入维护接口...");

		if (maintaindata == null || maintaindata.getBillID() == null) {
			SCMEnv.out("msgVo 为空，直接返回!");
			SCMEnv.out("****************以下是调用堆栈不是错误：****************");
			SCMEnv.out(new Exception());
			SCMEnv.out("****************以上是调用堆栈不是错误：****************");
			return;
		}
		// 加载卡片
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		// 设置按钮组
		setButtons(m_aryMsgCenter);
		/*
		 * for(int i=0;i<m_aryMsgCenter.length;i++){
		 * m_aryMsgCenter[i].setEnabled(true); }
		 */
		// 查询、加载数据
		ArriveorderVO vo = null;
		// 记录单据ID，getVo()用
		m_strBillId = maintaindata.getBillID();
		try {
			vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
		} catch (Exception e) {
			PuTool.outException(this, e);
			return;
		}

		// 如果当前登录公司不是操作员制单所在公司，则界面无操作按钮，仅提供浏览功能，by chao , xy , 2006-11-07
		String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
		String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
		boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);

		if (vo == null) {
			if (!bSameCorpFlag) {
				setButtonsNull();
			}
			return;
		}
		setCacheVOs(new ArriveorderVO[] { vo });
		for (int i = 0; i < getCacheVOs().length; i++) {
			if (getCacheVOs()[i].getChildrenVO() != null
					&& getCacheVOs()[i].getChildrenVO().length > 0) {
				// 给单据赋来源属性
				String cupsourcebilltype = ((ArriveorderItemVO[]) getCacheVOs()[i]
						.getChildrenVO())[0].getCupsourcebilltype();
				((ArriveorderVO) getCacheVOs()[i])
						.setUpBillType(cupsourcebilltype);
				// 刷新表体哈希表缓存
				for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
					try {
						if (getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey() == null) {
							continue;
						}
						if (getCacheVOs()[i].getChildrenVO()[j] == null) {
							continue;
						}
						hBodyItem.put(getCacheVOs()[i].getChildrenVO()[j]
								.getPrimaryKey(), getCacheVOs()[i]
								.getChildrenVO()[j]);
					} catch (BusinessException e) {
						PuTool.outException(this, e);
						return;
					}
				}
			}
		}
		setDispIndex(0);
		try {
			loadDataToCard();
		} catch (Exception e) {
			SCMEnv.out("加载到货单数据时出错：");/* -=notranslate=- */
			SCMEnv.out(e);
		}
		// 如果当前登录公司不是单据所属公司，则不显示功能按钮
		if (bSameCorpFlag) {
			onModify();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * 界面关联接口方法实现 -- 新增
	 **/
	public void doAddAction(ILinkAddData adddata) {

		SCMEnv.out("进入新增接口...");

		// 默认此节点可打开
		m_strNoOpenReasonMsg = null;

		if (adddata == null) {
			SCMEnv.out("ILinkAddData::adddata参数为空，直接返回");/* -=notranslate=- */
			return;
		}
		String strUpBillType = adddata.getSourceBillType();
		// 上游为采购订单
		if (BillTypeConst.PO_ORDER.equals(strUpBillType)) {
			OrderVO voOrder = null;
			try {
				voOrder = OrderHelper.queryForOrderBillLinkAdd(new ClientLink(
						ClientEnvironment.getInstance()), adddata
						.getSourceBillID());
			} catch (Exception e) {
				SCMEnv.out(e);
				return;
			}
			// 此节点是否可打开
			if (voOrder == null) {
				MessageDialog.showHintDlg(
						this,
						NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "提示" */,
						NCLangRes.getInstance().getStrByID("40040301",
								"UPP40040301-000287")/*
													 * @res
													 * "订单数据不能生成到货单，可能原因：1、订单业务类型走到货计划，但未生成到货计划；2、所有订单行均为劳务折扣属性；3、订单已经完全到货"
													 */);
				return;
			}
			ArrFrmOrdUI billReferUI = new ArrFrmOrdUI("corderid",
					ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey(), ClientEnvironment.getInstance()
							.getUser().getPrimaryKey(), "4004020201", "null1",
					ScmConst.PO_Order, null, "nc.ui.po.pub.PoToRcQueDLG",
					ScmConst.PO_Arrive, this, false, true);
			// 加载数据
			billReferUI.loadDataForMsgCenter(new OrderVO[] { voOrder });
			//
			billReferUI.showModal();
			//
			if (billReferUI.getResult() == UIDialog.ID_OK) {
				ArriveorderVO[] retVOs = (ArriveorderVO[]) billReferUI
						.getRetVos();
				onExitFrmOrd(retVOs);
			} else {
				SCMEnv.out("取消本次生成操作");/* -=notranslate=- */
				billReferUI.closeCancel();
				billReferUI.destroy();
			}
		}

	}

	/**
	 * 检查打开该节点的前提条件。用于处理“只有满足某一条件时，才能打开该节点” 的情况。
	 * 需要判断“只有满足某一条件时，才能打开该节点”的节点，需要实现本方法。 在方法内进行条件判断。
	 * 基类根据返回值进行相应处理，如果返回值为一个非空字符串，那么基类不打开
	 * 该节点，只在一个对话框中显示返回的字符串；如果返回值为null，那么基类象对待 其他节点一样打开该节点。
	 * 
	 * 创建日期：(2002-3-11 10:39:16)
	 * 
	 * @return java.lang.String
	 */
	protected String checkPrerequisite() {
		return m_strNoOpenReasonMsg;
	}

	/**
	 * 界面关联接口方法实现 -- 审批
	 **/
	public void doApproveAction(ILinkApproveData approvedata) {
		SCMEnv.out("进入审批接口...");
		if (approvedata == null || approvedata.getBillID() == null) {
			SCMEnv.out("msgVo 为空，直接返回!");
			SCMEnv.out("****************以下是调用堆栈不是错误：****************");
			SCMEnv.out(new Exception());
			SCMEnv.out("****************以上是调用堆栈不是错误：****************");
			return;
		}
		// 加载卡片
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		// 查询、加载数据
		ArriveorderVO vo = null;
		// 记录单据ID，getVo()用
		m_strBillId = approvedata.getBillID();
		try {
			vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
			if (vo == null)
				return;
			setCacheVOs(new ArriveorderVO[] { vo });
			setDispIndex(0);
			loadDataToCard();
		} catch (Exception e) {
			PuTool.outException(this, e);
		}
		getBillCardPanel().setEnabled(false);

		// 登录公司与单据所属公司是否相同
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// 设置按钮组
		if (bCorpSameFlag) {
			setButtons(m_aryArrCardButtons);
			setM_strState("到货浏览");
		} else {
			if (m_btnActionMsgCenter.getChildCount() == 0) {
				m_btnActionMsgCenter.addChildButton(m_btnAudit);
				m_btnActionMsgCenter.addChildButton(m_btnUnAudit);
			}
			if (m_btnOthersMsgCenter.getChildCount() == 0) {
				m_btnOthersMsgCenter.addChildButton(m_btnQueryForAudit);
				m_btnOthersMsgCenter.addChildButton(m_btnDocument);
				m_btnOthersMsgCenter.addChildButton(m_btnLookSrcBill);
			}
			setButtons(m_aryMsgCenter);
			//
			setM_strState("消息中心");
		}
		//
		setButtonsState();
	}

	/**
	 * 界面关联接口方法实现 -- 逐级联查
	 **/
	public void doQueryAction(ILinkQueryData querydata) {
		SCMEnv.out("进入逐级联查接口...");

		String billID = querydata.getBillID();

		initialize();

		ArriveorderVO vo = null;

		try {
			vo = ArriveorderHelper.findByPrimaryKey(billID);
			if (vo == null) {
				MessageDialog
						.showHintDlg(
								this,
								NCLangRes.getInstance().getStrByID("SCMCOMMON",
										"UPPSCMCommon-000270")/* "提示" */,
								m_lanResTool.getStrByID("common",
										"SCMCOMMON000000161")/* "没有察看单据的权限" */);
				return;
			}
			//
			String strPkCorp = vo.getPk_corp();
			// 按照单据所属公司加载查询模版
			RcQueDlg queryDlg = new RcQueDlg(this, getBusitype(), getFuncId(),
					getOperatorId(), strPkCorp);// 查询模板中没有公司时，要设置虚拟公司
			queryDlg.setDefaultValue("po_arriveorder.dreceivedate",
					"po_arriveorder.dreceivedate", "");
			queryDlg.initCorpsRefs();
			// 调用公共方法获取该公司中控制权限的档案条件VO数组
			ConditionVO[] condsUserDef = queryDlg.getDataPowerConVOs(strPkCorp,
					RcQueDlg.REFKEYS);
			// 组织第二次查询单据，按照权限和单据PK过滤
			ArriveorderVO[] voaRet = ArriveorderHelper.queryAllArriveMy(
					condsUserDef, null, strPkCorp, null,
					"po_arriveorder.carriveorderid = '" + billID + "' ");
			if (voaRet == null || voaRet.length <= 0 || voaRet[0] == null) {
				MessageDialog
						.showHintDlg(
								this,
								NCLangRes.getInstance().getStrByID("SCMCOMMON",
										"UPPSCMCommon-000270")/* "提示" */,
								m_lanResTool.getStrByID("common",
										"SCMCOMMON000000161")/* "没有察看单据的权限" */);
				setButtonsNull();
				return;
			}
			setCacheVOs(voaRet);
			setDispIndex(0);
			loadDataToCard();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */,
					e.getMessage());
			setButtonsNull();
			return;
		}
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// 设置按钮组
		if (bCorpSameFlag) {
			setButtons(m_aryArrCardButtons);
			setButtonsCard();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * 清空当前界面按钮
	 */
	private void setButtonsNull() {
		ButtonObject[] objs = getButtons();
		int iLen = objs == null ? 0 : objs.length;
		for (int i = 0; i < iLen; i++) {
			if (objs[i] == null) {
				continue;
			}
			objs[i].setVisible(false);
			updateButton(objs[i]);
		}
	}

	/**
	 * 合并显示、打印功能
	 * 
	 * @since v50
	 */
	private void onCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this,
				m_lanResTool.getStrByID("4004020201", "UPT4004020201-000084")/*
																			 * @res
																			 * "合并显示"
																			 */);
		dlg.initData(getBillCardPanel(),
		// 固定分组列
				new String[] { "cinventorycode", "cinventoryname",
						"cinventoryspec", "cinventorytype", "prodarea" },
				// 缺省分组列
				null,
				// 求和列
				new String[] { "nmoney", "narrvnum", "nelignum", "nnotelignum",
						"nwastnum" },
				// 求平均列
				null,
				// 求加权平均列
				new String[] { "nprice" },
				// 数量列
				"narrvnum");
		dlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000039")/* @res "合并显示完成" */);
	}

	/**
	 * 跺号扫描 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void DoSplitData(String pileno, int pilecount) {
		// TODO Auto-generated method stub
		invidinfo = null;
		PilenoList = null;
		InitInvID();

		String cinventoryid = new String();
		UFDouble num = null;
		String cspaceid = new String();
		String vbatchcode = new String();
		String csname = new String();
		String Wh = new String();
		String WhName = new String();
		int selIndex = getBillCardPanel().getBillTable().getSelectedRow();
		int recount = getBillCardPanel().getBillModel().getRowCount();
		if (selIndex <= -1 && (oid_cmangid == null || oid_cmangid.equals(""))) {
			MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", "没有选择需要扫描的存货!You need to scan inventory!");
			return;
		} else if (recount == 1) {
			cinventoryid = String.valueOf(getBillCardPanel().getBodyValueAt(0,
					"cmangid"));
		} else {
			cinventoryid = String.valueOf(getBillCardPanel().getBodyValueAt(
					selIndex, "cmangid"));
		}
		if (cinventoryid == null || cinventoryid.equals("")
				|| cinventoryid.equals("null")) {
			cinventoryid = oid_cmangid;
		} else {
			oid_cmangid = cinventoryid;
		}
		HashMap tb=getInvdocHashtable(cinventoryid);
		String vfreeid=String.valueOf(tb.get("free1"));
		if(vfreeid==null ||vfreeid.equals("")||vfreeid.equals("null"))
		{ 
		   if(pileno!=null &&!pileno.equals("")&&!pileno.equalsIgnoreCase("null"))
		   {
			 return;
		   }
    	 }
		int Sindex = GetStartIndex(cinventoryid);
		int mEindex = GetEndIndex(cinventoryid);
		String WhCode = new String();
		if (selIndex > 0
				&& (oid_cmangid != null || oid_cmangid.equals("") || oid_cmangid
						.equals("null"))) {
			Wh = String.valueOf(getBillCardPanel().getBodyValueAt(mEindex,
					"cwarehouseid"));
			WhName = String.valueOf(getBillCardPanel().getBodyValueAt(mEindex,
					"cwarehousename"));
			WhCode = ((UIRefPane) getBillCardPanel().getBodyItem(
					"cwarehousename").getComponent()).getRefCode();
			if (Wh == null || Wh.equals("") || Wh.equals("null")) {
				MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", "仓库不能为空!warehouse can not be empty!");
				return;
			}
			if ("008".equals(WhCode) || "013".equals(WhCode)) {//edit by shikun 2014-08-28 考虑仓库编码为空（NULL）的情况
				cspaceid = String.valueOf(getBillCardPanel().getBodyValueAt(
						mEindex, "cspaceid"));
				if (cspaceid == null || cspaceid.equals("null")) {
					cspaceid = "";
				}
			}

		}
		
//		if (StampIsExist(pileno, Wh, cinventoryid, cspaceid)) {
//			MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", "跺号:" + pileno
//
//			+ "在现存量里已存在!");
//			
//			return ;
//		}
		if (!pileno.equals("")) {

			if (PilenoList != null || PilenoList.toArray().length > 0) {
				if (PilenoList.indexOf(pileno) >= 0) {
					MessageDialog.showErrorDlg(this, "采购到货Procurement arrival", "跺号The stack number:" + pileno

					+ "已扫描过!has scanned!");
					return;
				}
			}
		}

		String m_pileno = String.valueOf(getBillCardPanel().getBodyValueAt(
				mEindex, "vfree0"));
		int strIndex = m_pileno.indexOf(":");
		int endIndex = m_pileno.indexOf("]");
		if (strIndex >= 1 || endIndex >= 1) {
			m_pileno = m_pileno.substring(strIndex + 1, endIndex - 1);
		}
		if ((m_pileno == null) || m_pileno.equals("")
				|| m_pileno.equals("null")) {
			String key="";
			try {
				 key=(getBillCardPanel().getBillModel().getBodyValueRowVO(mEindex, ArriveorderItemVO.class.getName())).getPrimaryKey();
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				
			}
			if(key!=null&&!key.equals("")&&!key.equals("null"))
			{
				getBillCardPanel().getBillModel().setRowState(mEindex,BillModel.MODIFICATION);
			}
			SetBodyNewValue(pileno, new UFDouble(String.valueOf(pilecount)),
					mEindex);
		} else {
			getBillCardPanel().getBillModel().copyLine(new int[] { mEindex });
		//	getBillCardPanel().getBillModel().setB
			getBillCardPanel().getBillModel().pasteLine(
					mEindex == recount - 1 ? mEindex : mEindex + 1);
			
			getBillCardPanel().setBodyValueAt(null, mEindex+1, "vfree1");
			getBillCardPanel().setBodyValueAt(null, mEindex+1, "vfree0");
			getBillCardPanel().setBodyValueAt(null, mEindex+1, "vfree0");
			String m_invcode = String.valueOf(getBillCardPanel()
					.getBodyValueAt(mEindex, "cinventorycode"));
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(
					mEindex, "cmangid"));
			String m_invname = String.valueOf(getBillCardPanel()
					.getBodyValueAt(mEindex, "cinventoryname"));
			String nprice = String.valueOf(getBillCardPanel().getBodyValueAt(
					mEindex + 1, "nprice"));
			String vproducenum = String.valueOf(getBillCardPanel()
					.getBodyValueAt(mEindex, "vproducenum"));
			vproducenum=(vproducenum.equals("")||vproducenum.equalsIgnoreCase("null"))?null:vproducenum;
			String nmoney = String.valueOf(getBillCardPanel().getBodyValueAt(
					mEindex + 1, "nmoney"));
			getBillCardPanel().setBodyValueAt(m_invcode, mEindex + 1,
					"cinventorycode");
			getBillCardPanel().setBodyValueAt(m_invname, mEindex + 1,
					"cinventoryname");
			getBillCardPanel().setBodyValueAt(null,mEindex + 1,"");
			if (nprice == null || nprice.equals("") || nprice.equals("null")) {
				nprice = "0";
			}
			if (nmoney == null || nmoney.equals("") || nmoney.equals("null")) {
				nmoney = "0";
			}
			BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem(
					"cinventorycode").getComponent(), m_invcode,
					"cinventorycode", mEindex + 1);
			afterEdit(e);
			getBillCardPanel().setBodyValueAt(new UFDouble(nprice),
					mEindex + 1, "nprice");
			getBillCardPanel().setBodyValueAt(new UFDouble(nmoney),
					mEindex + 1, "nmoney");

			SetBodyNewValue(pileno, new UFDouble(String.valueOf(pilecount)),
					mEindex + 1);
			getBillCardPanel().getBillModel().setValueAt(vproducenum,
					mEindex + 1, "vproducenum");

		}
		
	}

	/**
	 * 记录同一存货的开始索引和结束索引 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void InitInvID() {
		invidinfo = new Hashtable();
		boolean rst = true;
		int recount = 0;
		String old_invid = new String();
		PilenoList = new ArrayList();
		int index = 0;
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(
					i, "cmangid"));
			String m_Pileno = String.valueOf(getBillCardPanel().getBodyValueAt(
					i, "vfree0"));
			int strIndex = m_Pileno.indexOf(":") + 1;
			int endIndex = m_Pileno.indexOf("]");
			if (m_Pileno != null && !m_Pileno.equals("")
					&& !m_Pileno.equals("null")) {
				if (PilenoList.indexOf(m_Pileno) < 0) {

					PilenoList.add(m_Pileno.substring(strIndex, endIndex));
				}
			}
			if (old_invid.equals("") || !m_invid.equals(old_invid)) {
				index = i;
				if (recount > 1) {
					recount = 1;
				}
			}

			if (old_invid.equals("") || m_invid.equals(old_invid)) {
				recount++;

			}
			if (!m_invid.equals(old_invid)) {
				if (!invidinfo.containsKey(m_invid)) {
					ArrayList list = new ArrayList();
					// list.add(0, initBillItem[0]);//系统生成单据的存货数据
					list.add(0, m_invid);// 存货编码
					list.add(1, String.valueOf(index));// 新增单据时某个存货的开始索引
					list.add(2, String.valueOf(recount));// 记录存货的行数
					list.add(3, String.valueOf(recount - 1 + index));// 记录当前存货进行跺号处理的索引
					invidinfo.put(m_invid, list);
				}

			}
			if (m_invid.equals(old_invid)) {

				ArrayList list = (ArrayList) invidinfo.get(m_invid);
				int Sindex = Integer.parseInt(String.valueOf(list.get(1)));
				list.set(2, recount);
				list.add(3, String.valueOf(recount - 1 + Sindex));
				invidinfo.put(m_invid, list);
			}
			old_invid = m_invid;
		}

	}

	/**
	 * 返回存货的开始索引 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */

	private int GetStartIndex(String m_invid) {
		int Index = 0;

		if (invidinfo.containsKey(m_invid)) {
			ArrayList li = (ArrayList) invidinfo.get(m_invid);
			Index = Integer.parseInt(String.valueOf(li.get(1)));
		}
		return Index;

	}

	/**
	 * 返回存货的结束索引 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private int GetEndIndex(String m_invid) {
		int Index = 0;

		if (invidinfo.containsKey(m_invid)) {
			ArrayList li = (ArrayList) invidinfo.get(m_invid);
			Index = Integer.parseInt(String.valueOf(li.get(3)));
		}
		return Index;

	}

	/**
	 * 更新垛号、实收数量 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void SetBodyNewValue(String pileno, UFDouble num, int sindex) {

		getBillCardPanel().setBodyValueAt("[垛号:" + pileno + "]", sindex,
				"vfree0");
		getBillCardPanel().setBodyValueAt(num, sindex, "narrvnum");

		voFree = new FreeVO();
		voFree.m_vfree0 = "[垛号:" + pileno + "]";
		voFree.m_vfreename1 = "垛号";
		voFree.m_vfree1 = pileno;
		voFree.setVfreeid1("00010110000000049P7M");
		getBillCardPanel().setBodyValueAt( pileno, sindex,"vfree1");
		BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem(
				"vfree0").getComponent(), pileno, "vfree0", sindex, 1);
		
		afterEdit(e);


		getBillCardPanel().setBodyValueAt(num, sindex, "narrvnum");
		BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"narrvnum").getComponent(), new UFDouble("0"), num, "narrvnum",
				sindex, -1);
		afterEdit(e2);
	}

	/**
	 * 货位仓库带出默认货位 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void getSpace(BillEditEvent e) {
		// TODO Auto-generated method stub
		int Row = e.getRow();
		String cwarehouseid = String.valueOf(getBillCardPanel().getBodyValueAt(
				Row, "cwarehouseid"));

		if (cwarehouseid == null || cwarehouseid.equals("")) {
			return;
		}
		String sCode = ((UIRefPane) getBillCardPanel().getHeadItem(
				"cwarehouseid").getComponent()).getRefCode();
		if (sCode.equals("008") || sCode.equals("013"))// ||sCode.equals(arg0))
		{

			String cinventoryid = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Row, "cinventoryid"));
			if (cinventoryid == null || cinventoryid.equals("")) {
				return;
			}

			String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from po_arriveorder a  ";
			Sql += "left join po_arriveorder_b b on a.carriveorderid=b.carriveorderid  ";
			Sql += "left join bd_cargdoc d on c.cspaceid=b.cstoreid  ";
			Sql += "where b.cwarehouseid='" + cwarehouseid
					+ "' and  b.cmangid='" + cinventoryid + "'  ";
			Sql += "and b.cstoreid is not null  and a.taudittime is not null and nvl(b.dr,0)=0 order by a.taudittime desc  ";
			Sql += ") where rownum=1  ";
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(Sql,
						new ArrayListProcessor());

				if (list.isEmpty()) {

					Sql = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
					Sql += "from   v_ic_onhandnum6 kp  ";
					Sql += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
					Sql += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='"
							+ cwarehouseid
							+ "'  and  kp.cinventoryid='"
							+ cinventoryid + "')  ";
					Sql += "where rownum=1  ";
					list = (List) sessionManager.executeQuery(Sql,
							new ArrayListProcessor());
					if (list.isEmpty()) {
						return;
					}
				}

			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ArrayList values = new ArrayList();
				Object obj = iterator.next();
				if (obj == null) {
					continue;
				}
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int j = 0; j < len; j++) {
						values.add(Array.get(obj, j));
					}
				}
				getBillCardPanel().setBodyValueAt(values.get(0), Row,
						"cstoreid");
				getBillCardPanel().setBodyValueAt(values.get(1), Row,
						"cstorename");
			}

		}
	}

	/**
	 * 初始化全局变量 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void InitGlobalVar() {
		oid_cmangid = null;

	}

	/**
	 * 初始化采购入库表行 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private GeneralBillItemVO getAddLocatorVOInItemVO(String cwarehouseid,
			String cwarehouse, String Pk_corp, String cstoreorganization,
			String cspaceid, String vspacename, GeneralBillItemVO changeVo,
			boolean IsCalcInNum,int crowindex)throws BusinessException {

		GeneralBillItemVO bo = new GeneralBillItemVO();
		bo = changeVo;
		bo.setIsok(new UFBoolean("N"));
		bo.setBarcodeClose(new UFBoolean("N"));
		bo.setBreturnprofit(new UFBoolean("N"));
		bo.setAttributeValue("bsafeprice", new UFBoolean("N"));
		bo.setAttributeValue("btoinzgflag", new UFBoolean("N"));
		bo.setAttributeValue("btoouttoiaflag", new UFBoolean("N"));
		bo.setAttributeValue("bzgflag", new UFBoolean("N"));
		bo.setFchecked(0);
		//bo.setFlargess(new UFBoolean("N"));
		bo.setIsok(new UFBoolean("N"));
		bo.setStatus(0);
		bo.setCrowno(String.valueOf((crowindex+1)*10));
		bo.setAttributeValue("cvendorid", cwarehouse);
		bo.setAttributeValue("pk_invoicecorp", Pk_corp);
		bo.setAttributeValue("pk_bodycalbody", cstoreorganization);
		bo.setBarcodeClose(new UFBoolean("N"));
		bo.setAttributeValue("pk_reqcorp", Pk_corp);
		bo.setAttributeValue("bonroadflag", new UFBoolean("N"));
		bo.setAttributeValue("idesatype", 0);
		bo.setNbarcodenum(new UFDouble("0"));
		
		try {
			if (IsCalcInNum) {
				getInNum(bo);
			} else {
				bo.setNinnum(bo.getNshouldinnum());
			}
			String batchcode=bo.getVbatchcode();
			if(batchcode==null||batchcode.equals("")||batchcode.equalsIgnoreCase("null"))
			{
				bo.setVbatchcode(null);
			}
			
			if(Iscsflag(cwarehouseid))
			{	bo.setCspaceid(cspaceid);
			bo.setVspacename(vspacename);
			bo.setLocStatus(VOStatus.NEW);
				LocatorVO[] lvos = new LocatorVO[1];
			
			LocatorVO voSpace = new LocatorVO();
			lvos[0] = voSpace;
			voSpace.setCspaceid(cspaceid);
			voSpace.setVspacename(vspacename);
			voSpace.setCwarehouseid(cwarehouseid);
			voSpace.setNinspacenum(bo.getNinnum());
			if (bo.getHsl() != null) {

				voSpace.setNinspaceassistnum(bo.getNinnum().multiply(bo.getHsl()));
			} else {
				voSpace.setNinspaceassistnum(null);
			}
			voSpace.setNingrossnum(null);
			LocatorVO[] voLoc = lvos;
			bo.setLocator(voLoc);
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			throw e;
			//MessageDialog.showErrorDlg(this, "维护单货单", e.getMessage());
		}
		bo.setStatus(2);
		// testVo.setItem(j, bo);
		return bo;
	}

	/**
	 * 如果需要检验的，入库数量=累积合格数量 -累积入库数量 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private void getInNum(GeneralBillItemVO generalBillItemVO)throws BusinessException{
		// TODO Auto-generated method stub

		try {

			String innum = "0";
			String nprice ="0";
	       String SQL = "select narrvnum, nvl(nelignum,0)-nvl(naccumwarehousenum,0) as innum,nprice from  po_arriveorder_b where carriveorder_bid='"
					+ generalBillItemVO.getCsourcebillbid() + "'";

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = (List) sessionManager.executeQuery(SQL,
					new ArrayListProcessor());
			ArrayList values = new ArrayList();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}

				}
			}
			String narrvnum = String.valueOf(values.get(0));
			 innum = String.valueOf(values.get(1));
			 if(generalBillItemVO.getFlargess()==null||!generalBillItemVO.getFlargess().booleanValue())
			{
	         
		      nprice = String.valueOf(values.get(2));
			
			  if(nprice==null||nprice.equalsIgnoreCase("null")||new UFDouble(nprice).compareTo(new UFDouble( "0"))==0)
			  {
				//MessageDialog.showErrorDlg(this, "检验到货单Inspection documents", "单价为空或者为0!");
				throw new BusinessException("单价为空或者为0!Price is empty or 0!");
			  }
		    	if(innum==null||innum.equalsIgnoreCase("null")||new UFDouble(innum).compareTo(new UFDouble( "0"))==0)
		    	{
				throw new BusinessException("数量为空或者为0!Quantity is empty or 0!");
				//return ;
			  }
			}
			UFDouble nmny = new UFDouble(nprice).multiply(new UFDouble(innum));
			generalBillItemVO.setNinnum(new UFDouble(innum));
			generalBillItemVO.setNshouldinnum(new UFDouble(narrvnum));
			generalBillItemVO.setNprice(new UFDouble(nprice));
			generalBillItemVO.setAttributeValue("nmny", nmny);
			// generalBillItemVO.setN
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	/**
	 * 设置货位id 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private String setSpace(String key,
			CircularlyAccessibleValueObject[] childrenVO) {
		// TODO Auto-generated method stub
		String spaceid = new String();
		for (int i = 0; i < childrenVO.length; i++) {
			if (((ArriveorderItemVO) childrenVO[i]).getPrimaryKey().equals(key)) {
				spaceid = (String) ((ArriveorderItemVO) childrenVO[i])
						.getAttributeValue("cstoreid");

			}
		}
		return spaceid;
	}

	/**
	 * 设置货位名称 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private String setSpaceName(String key,
			CircularlyAccessibleValueObject[] childrenVO) {
		// TODO Auto-generated method stub
		String spacename = new String();
		for (int i = 0; i < childrenVO.length; i++) {
			if (((ArriveorderItemVO) childrenVO[i]).getPrimaryKey().equals(key)) {
				spacename = (String) ((ArriveorderItemVO) childrenVO[i])
						.getAttributeValue("vstorename");

			}
		}
		return spacename;
	}

	/**
	 * 判断垛号是否存在 新增人 ：林桂莹2012/8/29
	 * 
	 * @since v50
	 */
	private boolean StampIsExist(String StampNo) {
		boolean rst = false;
		try {

			String SQL = "select vfree1 ";
			SQL += "from   v_ic_onhandnum6 kp  ";
			SQL +="where vfree1='" + StampNo + "'and  nvl(ninspacenum,0.0)-nvl(noutspacenum,0.0)>0 ";
			if (pk_corp != null) {
			SQL = SQL + "and kp.pk_corp='" + pk_corp + "'";
			}
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = (List) sessionManager.executeQuery(SQL,
					new ArrayListProcessor());
			ArrayList values = new ArrayList();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
					if (String.valueOf(values.get(0)) != null
							&& !String.valueOf(values.get(0)).equals("")
							&& !String.valueOf(values.get(0)).equalsIgnoreCase("null")) {
						rst = true;
						break;
					}
				}
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rst;
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getParentCorpCode() {

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
	}
	/**
	 * @功能:判断仓库是否启用货位管理
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public boolean Iscsflag(String primkey) throws BusinessException
	{
		boolean rst=false;
		String SQL="select csflag from bd_stordoc  where pk_stordoc ='"+primkey+"'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
List list = null;
try {
	list = (List) sessionManager.executeQuery(SQL,
			new ArrayListProcessor());

	if (list.isEmpty()) {
		return rst;
	}
	else 
	{
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			ArrayList values = new ArrayList();
			Object obj = iterator.next();
			if (obj == null) {
				continue;
			}
			if (obj.getClass().isArray()) {
				int len = Array.getLength(obj);
				for (int j = 0; j < len; j++) {
					values.add(Array.get(obj, j));
				}
				return  rst= (new UFBoolean(String.valueOf(values.get(0)))).booleanValue();
			}
		}
	}
	}catch(BusinessException e)
	{
		throw e;
	}
		return rst;
	}
	/**
	 * @功能:校验货位 仓库 自由项
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public boolean checkVO(ArriveorderVO vo) {
		try {
		
			// 自由项
			try {
				checkFreeItemAndBatchAndSpaceInput(vo);
				
			} catch (ValidationException e) {
				// 显示提示
				showErrorMessage(e.getMessage());
				return false ;
			}
			

	}
		catch (Exception e)
		{
			return false;
		}
		
		return true;
	}
	 /**
	  * @功能:检验
	  * @author ：林桂莹
	  * @2012/9/5
	  * 
	  * @since v50
	  */
	 public  void checkFreeItemAndBatchAndSpaceInput(AggregatedValueObject hvo)
	    throws ValidationException
	  {
		 String errMessage="";
		 ArriveorderItemVO []vo=( ArriveorderItemVO []) hvo.getChildrenVO();
		 String vbillCode=((ArriveorderHeaderVO)hvo.getParentVO()).getVarrordercode();
		 for(int i=0;i<vo.length;i++)
		 {
			HashMap invhm= getInvdocHashtable(vo[i].m_cmangid);
			if((invhm==null)||(invhm.size()<=0))
			{
				throw new ValidationException("查询出异常存货信息!Query exception inventory information!");
				//return false ;
			}
			String invcode=String.valueOf(invhm.get("invcode"));
			String key=vo[i].getPrimaryKey();
			try {
				if(Iscsflag(vo[i].getCwarehouseid()))
				{
					String storeid=vo[i].getCstoreid();
					if(storeid==null||storeid.equals("")||storeid.equals("null"))
					{
						
						errMessage+=Transformations.getLstrFromMuiStr("单号@Document number&:"+vbillCode+"&行号@Line number&:"+vo[i].m_crowno+"& 存货@Inventories&"+invcode+"&的货位不能为空!@of cargo space can not be empty!")+"\n";
					}
					else 
					{
						String Msg=null;
                        	try {
								 Msg=checkstorectl(vo[i].m_cmangid,vo[i].getCwarehouseid(),storeid);
							} catch (Exception ex) {
								// TODO Auto-generated catch block
								throw new ValidationException(ex.getMessage()); 
							}
                        if(Msg!=null)
                        {
                        	Msg=Msg.indexOf("ff")>0?Msg.substring(Msg.indexOf("ff")+2, Msg.length())+"@requirements fixed cargo space":Msg.substring(Msg.indexOf("fs")+2, Msg.length())+"@require separate storage!";
                        	errMessage+=Transformations.getLstrFromMuiStr("单号@Document number&:"+vbillCode+"&行号@Line number&:"+vo[i].m_crowno+"& 存货@Inventories&"+invcode+"&"+Msg)+"\n";
                        }
					}
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				throw new ValidationException(e.getMessage()); 
			}
			String IsBathcMgrt =String.valueOf(invhm.get("isbathcmgrt"));
			IsBathcMgrt=(IsBathcMgrt!=null&&!IsBathcMgrt.equals("")&&
					     !IsBathcMgrt.equals("null")&&!IsBathcMgrt.equals("N"))?"Y":"N";
			
			
			String chkfreeflag=String.valueOf(invhm.get("chkfreeflag"));
			chkfreeflag=(chkfreeflag!=null&&!chkfreeflag.equals("")&&
				     !chkfreeflag.equals("null")&&!chkfreeflag.equals("N"))?"Y":"N";
			String stockbycheck=String.valueOf(invhm.get("stockbycheck"));
			stockbycheck=(stockbycheck!=null&&!stockbycheck.equals("")&&
				     !stockbycheck.equals("null")&&!stockbycheck.equals("N"))?"Y":"N";
			
			
			if(chkfreeflag.equalsIgnoreCase(stockbycheck))
			{
				errMessage+=Transformations.getLstrFromMuiStr("单号@Document number&:"+vbillCode+"&行号@Line number&:"+vo[i].m_crowno+"& 存货@Inventories&"+invcode+"&的免检、是否根据检验结果入库的属性设置不对!请在[物料生产档案]的<控制信息>中进行设置（二者必勾选其一）!@of the exemption, whether based on the test results storage properties set wrong! Materials production file]> <control information set (two must check one of)!")+"\n";
			}
			if(new UFBoolean(IsBathcMgrt).booleanValue())
			{
				String vbatch=String.valueOf(vo[i].getVproducenum());
				if(vbatch==null||vbatch.equals("")||vbatch.equals("null"))
				{
	
					errMessage+=Transformations.getLstrFromMuiStr("单号@Document number&:"+vbillCode+"&行号@Line number&:"+vo[i].m_crowno+"& 存货@Inventories&"+invcode+"&启用批次号管理，批次号不能为空!@Enable batch number management, batch number can not be empty")+"\n";
				}
		        
			}
			 for(int j=1;j<=5;j++)
	         {
	        	 String vfreeid=String.valueOf(invhm.get("free"+String.valueOf(j)));
	        	 String vfree=String.valueOf(vo[i].getAttributeValue("vfree"+j));
	        	 if(vfreeid==null ||vfreeid.equals("")||vfreeid.equals("null"))
	        	 { 
	        		 continue;
	        	 }
	        	 else if(vfree==null ||vfree.equals("")||vfree.equalsIgnoreCase("null"))
	        	 {
	   
	        		 errMessage+=Transformations.getLstrFromMuiStr("单号@Document number&:"+vbillCode+"&存货@The inventory&"+invcode+"&的自由项@free term&"+String.valueOf(j)+"&不能为空!@can not be empty!");
	        	 }
	         }
			 if(!errMessage.equals("")&&!errMessage.equalsIgnoreCase("null"))
				  throw new ValidationException(errMessage);
		 }
		
	  }

	 /**
	  * @功能:查询存货 自由项 是否批次号管理
	  * @author ：林桂莹
	  * @2012/9/5
	  * 
	  * @since v50
	  */

	private HashMap getInvdocHashtable(String key) {
		// TODO Auto-generated method stub
		String sql="select a.invcode, a.free1,a.free2,a.free3,a.free4,a.free5,wholemanaflag as IsBathcMgrt,chkfreeflag,stockbycheck ";
		sql+=" from bd_invbasdoc a ,bd_invmandoc b,bd_produce c  " ;
		sql+="where a.pk_invbasdoc=b.pk_invbasdoc  and c.pk_invmandoc=b.pk_invmandoc and b.dr=0 and b.pk_corp='"+ getClientEnvironment().getCorporation().getPrimaryKey()+"' and b.pk_invmandoc ='"+key+"'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
        HashMap inv = null;
        try {
        	inv = (HashMap)sessionManager.executeQuery(sql,new MapProcessor());
	        }
	        catch (BusinessException e)
	        {
	        	return null;
	        	
	        }
		return inv;
	}
	private boolean IsPO(String cbizitype)  
	{
		String sql="select busicode from bd_busitype where pk_busitype='"+cbizitype+"'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
        HashMap bizitype = null;
        try {
        	bizitype= (HashMap)sessionManager.executeQuery(sql,new MapProcessor());
	        }
	        catch (BusinessException e)
	        {
	        	return false;
	        	
	        }
	     if( bizitype.size()<=0)
	     {
	    	 return false;
	     }
	     String busicode=String.valueOf(bizitype.get("busicode"));
	     if(busicode==null||busicode.equalsIgnoreCase("")||busicode.equalsIgnoreCase("null")||busicode.equalsIgnoreCase("C004"))
	     {
	    	 return false;
	     }
		return  true ;
	}
	private boolean IsNotLockAccount(String dbDate,String calbody) throws Exception
	{ 
		boolean rst=false ;
	  
		try
		{
			if(new UFDate(dbDate).compareTo(ClientEnvironment.getServerTime().getDate())>0)
			{
				return true;
			}
			StringBuffer  Sql=new StringBuffer();
		    Sql.append("SELECT  acc.faccountflag  ");
		    Sql.append("FROM ic_accountctrl acc, bd_calbody cal  ");
		    Sql.append("where acc.pk_calbody = cal.pk_calbody  AND acc.dr = 0  AND acc.pk_calbody = '").append(calbody).append("'  ");
	        Sql.append("and '"+dbDate+"' between  to_char(to_date(acc.tstarttime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') and  to_char(to_date(acc.tendtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') ");
	        IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
			.getInstance().lookup(IUAPQueryBS.class.getName());
	        ArrayList al=(ArrayList)sessionManager.executeQuery(Sql.toString(),  new ListProcessor());
	        if(al==null||al.size()<=0)
	        {
	        	return true;
	        }
	        rst= new UFBoolean(String.valueOf(al.get(0))).booleanValue();
	        return rst;
		}
		catch(Exception e)
		{
          throw e;
		}
	     
	}


   public String checkstorectl(String inv,String wh,String space) throws Exception
   {
		StringBuffer  Sql=new StringBuffer();
	    Sql.append("SELECT  fseparatespace,ffixedspace,cspaceid ");
	    Sql.append("FROM ic_storectl acc, ic_defaultspace cal  ");
	    Sql.append("where acc.cstorectlid=cal.cstorectlid  and cwarehouseid='"+wh+"' and cinventoryid='"+inv+"'");
	    HashMap invhm = null;
        try {
        	 IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	invhm = (HashMap)sessionManager.executeQuery(Sql.toString(),new MapProcessor());
	        }
	        catch (BusinessException e)
	        {
	        	throw e;
	        	
	        }
	    if(invhm==null||invhm.size()<=0) 
	    {
	    	return null;
	    }
	    else 
	    {
	    	UFBoolean fseparatespace=new UFBoolean(StringIsNullOrEmpty(invhm.get("fseparatespace"))?"N":String.valueOf(invhm.get("fseparatespace")));//是否单独存放
	    	UFBoolean ffixedspace=new UFBoolean(StringIsNullOrEmpty(invhm.get("ffixedspace"))?"N":String.valueOf(invhm.get("ffixedspace")));//是否固定货位
	    	String cspaceid=String.valueOf(invhm.get("cspaceid"));
	    	if(ffixedspace.booleanValue()&&!space.equalsIgnoreCase(cspaceid))
	    	{
	    		return "ff要求存放固定货位！";
	    	}
	    	else if (fseparatespace.booleanValue())
	    	{
	    		Sql.setLength(0);
	    		Sql.append("select nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0) as onhandnum  ");
	    		Sql.append("from v_ic_onhandnum6 hand  where hand.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"' and  hand.cspaceid='"+space+"'  and nvl(hand.ninspacenum,0.0) - nvl(hand.noutspacenum,0.0)>0");
	    		   try {
	    	        	 IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    	        	 HashMap handnumhm = (HashMap)sessionManager.executeQuery(Sql.toString(),new MapProcessor());
	    	        	 if(handnumhm==null)
	    	        	 {
	    	        		 return null;
	    	        	 }
	    	        	 else if(handnumhm.size()>0)
	    	        	 {
	    	        		 return "fs要求单独存放！";
	    	        	 }
	    		        }
	    		        catch (BusinessException e)
	    		        {
	    		        	throw e;
	    		        	
	    		        }
	    	}
	    }
	    return null;
   }
	public boolean StringIsNullOrEmpty(Object obj) 
	{
		return obj==null?true:String.valueOf(obj).equals("")?true:
			  String.valueOf(obj).equalsIgnoreCase("null")?true:false;
	}
public boolean beforeEdit(BillItemEvent arg0) {
	// TODO Auto-generated method stub
	return false;
}
private boolean IsNeedCheckvfree(String dh,String primkey) throws BusinessException
{
	if(StringIsNullOrEmpty(dh))
	{
		return false;
		}
	String SQL = "select vfree1 ";
		SQL+=" from po_arriveorder_b b  ";
		SQL+="where b.dr=0 and b.carriveorder_bid='"+primkey+"' ";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
		.lookup(IUAPQueryBS.class.getName());
try {
	HashMap resuthm = (HashMap) sessionManager.executeQuery(SQL,
			new MapProcessor());
	if(resuthm==null||resuthm.isEmpty())
	{
		return true;
	}
	String vfree1=String.valueOf(resuthm.get("vfree1"));
	
	return  !dh.equals(vfree1);
		
} catch (BusinessException e) {
	// TODO Auto-generated catch block
	//MessageDialog.showErrorDlg(this,"错误Error", e.getMessage());
	//return false;
	throw e;
}
	
	
}
//ncm begin liuydc
/*
 * 按照仓库过滤货位参照
 */
private void filterCargdocRefModel(UIRefPane pane, String cwarehouseid) {
	if (pane == null || pane.getRefModel() == null)
		return;
	AbstractRefModel model = pane.getRefModel();
	model.addWherePart(" and bd_cargdoc.pk_stordoc = '" + cwarehouseid + "' ");
}
//ncm end liuydc

private UITextField getTFLocalFile()
{
  if (this.txtfFileUrl == null) {
    try
    {
      this.txtfFileUrl = new UITextField();
      this.txtfFileUrl.setName("txtfFileUrl");
      this.txtfFileUrl.setBounds(270, 160, 230, 26);
      this.txtfFileUrl.setMaxLength(2000);
      this.txtfFileUrl.setEditable(false);
    }
    catch (Exception e) {
      e.printStackTrace(); 
    }
  }
  return this.txtfFileUrl;
}

// edit by yqq 2016-11-2 XML导入按钮, 测试
public void onImportXml() {
	UIFileChooser fileChooserXmlxy = new UIFileChooser();
	fileChooserXmlxy.setAcceptAllFileFilterUsed(true);
	this.xml = fileChooserXmlxy.showOpenDialog(this);
	if (this.xml == 0) {
		getTFLocalFile().setText(
				fileChooserXmlxy.getSelectedFile().getAbsolutePath());
		this.xmlFile = fileChooserXmlxy.getSelectedFile();
		String filepath = this.txtFile.getAbsolutePath();

/*		SAXReader saxReader = new SAXReader();
		org.dom4j.Document document = saxReader.read(filepath);
		// 获取根元素
		Element root = document.getRootElement();
		List po_order = root.elements("po_order"); // po_order节点列表

		for (Iterator<Element> i = po_order.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			org.dom4j.Element word;
			if (element.getName().equals("po_order_head")) {
				for (Iterator k = word.elementIterator(); k.hasNext();) { // 遍历
																			// name
																			// mean
																			// lx节点

				}
			}
			if (element.getName().equals("po_order_body")) {
				for (Iterator k = word.elementIterator(); k.hasNext();) { // 遍历
																			// name
																			// mean
																			// lx节点

				}

			}

		}*/
	}
}

}

