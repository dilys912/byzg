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
 * @����������������ά��
 * @���ߣ���־ƽ
 * @�������ڣ�(2001-5-24 9:42:56)
 */
public class ArriveUI extends nc.ui.pub.ToftPanel implements BillEditListener,
		BillTableMouseListener, ActionListener, BillEditListener2,
		ListSelectionListener, BillBodyMenuListener,
		IBillModelSortPrepareListener, ISetBillVO, IBillExtendFun, ICheckRetVO,
		IBillRelaSortListener2, ILinkMaintain,// �����޸�
		ILinkAdd,// ��������
		ILinkApprove,// ������
		ILinkQuery,// ������
		BillCardBeforeEditListener
{
	  private File txtFile = null;
	  private int xml;
	  private File xmlFile = null;
	  private UITextField txtfFileUrl = null;
	//���ݵ��붨��--start--
		public static Workbook w   = null;
		public static int rows=0;
		public static SaleorderBVO[] wbvo = null;
		public static String pk_corp="";
		//���ݵ��붨��--end--	
	// QC�Ƿ�����
	private boolean m_bQcEnabled = false;
	// �б��Ƿ���ع�
	private boolean m_bLoaded = false;
	// ��ť��ʵ��,since v51
	private ButtonTree m_btnTree = null;

	// �򲻿��˽ڵ��ԭ��
	private String m_strNoOpenReasonMsg = null;
	//
	private boolean m_bQueriedFlag = false;

	// ������������ȷ�������⣺
	class MyBillData implements IBillData {
		public void prepareBillData(nc.ui.pub.bill.BillData bd) {
			ArriveUI.this.initBillBeforeLoad(bd);
		}
	}

	/* ���б��建�� */
	private Hashtable hBodyItem = new Hashtable();
	// �Ƿ�ı���ҵ������
	boolean isChangeBusitype = true;
	/** ����ʱ���⴦���� */
	private boolean isFrmList = false;
	/** ��ŵ�ǰ��ͷ��Ӧ�ı��� */
	private ArriveorderItemVO[] items = null;

	// ��λ�Ի���
	private LocateDlg locatedlg = null;
	// ������Ƭ
	private BillCardPanel m_arrBillPanel = null;
	// ������ѯ���
	private ArriveorderVO[] m_arriveVOs = null;
	// �����б�
	private BillListPanel m_arrListPanel = null;
	// ������ѯ�Ի���
	ATPForOneInvMulCorpUI m_atpDlg = null;
	/* �ɹ��˻���ѯ�� */
	private PoToBackRcQueDLG m_backQuePoDlg = null;
	/* ί���˻���ѯ�� */
	private RcToScQueDLG m_backQueScDlg = null;
	/* �˻����ղɹ�����ѡ����� */
	private ArrFrmOrdUI m_backRefUIPo = null;
	/* �˻�����ί�ⶩ��ѡ����� */
	private ArrFrmOrdUI m_backRefUISc = null;

	// ���﷭�빤��
	private NCLangRes m_lanResTool = NCLangRes.getInstance();

	/* ��Ƭ��ť���� */
	private ButtonObject m_btnCheck = null;// new ButtonObject(
											// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPT40040303-000009")/*@res
											// "����"*/,
											// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000029")/*@res
											// "���鵽����"*/, 2, "����");
											// /*-=notranslate=-*/
	// ҵ������
	private ButtonObject m_btnBusiTypes = null;// new
												// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000003")/*@res
												// "ҵ������"*/,
												// m_lanResTool.getStrByID("common","UC001-0000003")/*@res
												// "ҵ������"*/, 2,"ҵ������");
												// /*-=notranslate=-*/
	// ������
	private ButtonObject m_btnAdds = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000002")/*@res
											// "����"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000230")/*@res
											// "���ɵ�����"*/, 2,"����");
											// /*-=notranslate=-*/
	// �˻���
	private ButtonObject m_btnBackPo = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000021")/*@res
											// "�ɹ�����"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000231")/*@res
											// "���ղɹ��������ɵ�����"*/, 2,"�ɹ�����");
											// /*-=notranslate=-*/
	private ButtonObject m_btnBackSc = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000022")/*@res
											// "ί�ⶩ��"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000232")/*@res
											// "����ί�ⶩ�����ɵ�����"*/, 2,"ί�ⶩ��");
											// /*-=notranslate=-*/
	private ButtonObject m_btnBacks = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
											// "�˻�"*/,m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
											// "�˻�"*/,2, "�˻�");
	// ����ά����
	private ButtonObject m_btnModify = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
											// "�޸�"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000235")/*@res
											// "�޸ĵ�����"*/, 2,"�޸�");
											// /*-=notranslate=-*/
	private ButtonObject m_btnSave = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000001")/*@res
											// "����"*/,
											// m_lanResTool.getStrByID("40040301","UPP40040301-000237")/*@res
											// "�����޸Ľ��"*/, 2,"����");
											// /*-=notranslate=-*/
	private ButtonObject m_btnCancel = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000008")/*@res
											// "ȡ��"*/,
											// m_lanResTool.getStrByID("common","UC001-0000008")/*@res
											// "ȡ��"*/, 2,"ȡ��");
											// /*-=notranslate=-*/
	private ButtonObject m_btnDiscard = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
												// "����"*/,
												// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
												// "����"*/, 2,"����");
												// /*-=notranslate=-*/
	private ButtonObject m_btnSendAudit = null;// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000017")/*
												// @res "����"
												// */,m_lanResTool.getStrByID("40040101","UPP40040101-000451")/*
												// @res "���󵽻���" */, 2, "����");
												// /*-=notranslate=-*/
	private ButtonObject m_btnMaintains = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
												// "����ά��"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
												// "����ά��"*/,2,"����ά��");
	// �в�����
	private ButtonObject m_btnDelLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000013")/*@res
												// "ɾ��"*/,
												// m_lanResTool.getStrByID("common","UC001-0000013")/*@res
												// "ɾ��"*/, 2,"ɾ��");
												// /*-=notranslate=-*/
	private ButtonObject m_btnCpyLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000014")/*@res
												// "������"*/,
												// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "������"*/, 2,"������");
												// /*-=notranslate=-*/
	private ButtonObject m_btnPstLine = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "ճ����"*/,
												// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
												// "ճ����"*/, 2,"ճ����");
												// /*-=notranslate=-*/
	private ButtonObject m_btnLines = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000011")/*@res
											// "�в���"*/,m_lanResTool.getStrByID("common","UC001-0000011")/*@res
											// "�в���"*/,2, "�в���");
	// ���������
	private ButtonObject m_btnBrowses = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000021")/*@res
												// "���"*/,m_lanResTool.getStrByID("common","UC001-0000021")/*@res
												// "���"*/,2, "���");
	private ButtonObject m_btnQuery = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
											// "��ѯ"*/,
											// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
											// "��ѯ"*/, 2,"��ѯ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnFirst = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH031")/*@res
											// "��ҳ"*/,
											// m_lanResTool.getStrByID("common","UCH031")/*@res
											// "��ҳ"*/, 2,"��ҳ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrev = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH033")/*@res
											// "��һҳ"*/,
											// m_lanResTool.getStrByID("common","UCH033")/*@res
											// "��һҳ"*/, 2,"��һҳ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnNext = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH034")/*@res
											// "��һҳ"*/,
											// m_lanResTool.getStrByID("common","UCH034")/*@res
											// "��һҳ"*/, 2,"��һҳ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnLast = null;// ButtonObject(m_lanResTool.getStrByID("common","UCH032")/*@res
											// "ĩҳ"*/,
											// m_lanResTool.getStrByID("common","UCH032")/*@res
											// "ĩҳ"*/, 2,"ĩҳ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnLocate = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
											// "��λ"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
											// "��λ"*/, 2,"��λ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnRefresh = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
												// "ˢ��"*/,
												// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
												// "ˢ��"*/, 2,"ˢ��");
												// /*-=notranslate=-*/
	// �л�
	private ButtonObject m_btnList = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
											// "�б���ʾ"*/,
											// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
											// "�б���ʾ"*/, 2,"�л�");
											// /*-=notranslate=-*/
	// ִ����(��������������Ϣ���Ĺ���)
	private ButtonObject m_btnActions = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
												// "ִ��"*/,
												// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
												// "ִ��"*/, 0,"ִ��");
												// /*-=notranslate=-*/
	// ������
	private ButtonObject m_btnOthers = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "����"*/,2, "����");
	public ButtonObject m_btnCombin = null;// ButtonObject(m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
											// "�ϲ���ʾ"*/,
											// m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
											// "�ϲ���ʾ"*/, 2,"�ϲ���ʾ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrints = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "��ӡ"*/,
											// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "��ӡ"*/, 2,"��ӡ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrint = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "��ӡ"*/,
											// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
											// "��ӡ"*/, 2,"��ӡ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnPrintPreview = null;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/, 2,"Ԥ��");
													// /*-=notranslate=-*/
	private ButtonObject m_btnUsable = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
											// "������ѯ"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
											// "������ѯ"*/, 2,"������ѯ");
											// /*-=notranslate=-*/
	private ButtonObject m_btnQueryBOM = null;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
												// "���׼�"*/,
												// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
												// "���׼�"*/, 2,"���׼�");
												// /*-=notranslate=-*/
	private ButtonObject m_btnQuickReceive = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
													// "�����ջ�"*/,
													// m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
													// "�����ջ�"*/, 2,"�����ջ�");
													// /*-=notranslate=-*/
	protected ButtonObject m_btnDocument = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
												// "�ĵ�����"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
												// "�ĵ�����"*/, 2,"�ĵ�����");
												// /*-=notranslate=-*/
	protected ButtonObject m_btnLookSrcBill = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
													// "����"*/,
													// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
													// "����"*/, 2,"����");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryForAudit = null;// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000032")/*
													// @res "״̬��ѯ"
													// */,m_lanResTool.getStrByID("40040101","UPP40040101-000450")/*
													// @res "����״̬��ѯ" */, 2,
													// "״̬��ѯ");
													// /*-=notranslate=-*/
	private ButtonObject m_btnImportBill = null;  //EXCL���ݵ���
	
	private ButtonObject m_btnImportXml = null;  //XML����  yqq 2016-11-02 ����
	
	
	/* ��Ƭ��ť�˵� */

	private ButtonObject m_aryArrCardButtons[] = null;

	/* �б���ť���� */
	private ButtonObject m_btnSelectAll = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000041")/*@res
												// "ȫѡ"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000238")/*@res
												// "ȫ��ѡ��"*/, 2,"ȫѡ");
												// /*-=notranslate=-*/
	private ButtonObject m_btnSelectNo = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000042")/*@res
												// "ȫ��"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000233")/*@res
												// "ȫ��ȡ��"*/, 2,"ȫ��");
												// /*-=notranslate=-*/
	private ButtonObject m_btnModifyList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
												// "�޸�"*/,
												// m_lanResTool.getStrByID("40040301","UPPSCMCommon-000291")/*@res
												// "�޸ĵ���"*/, 2, "�б��޸�");
												// /*-=notranslate=-*/
	private ButtonObject m_btnDiscardList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
													// "����"*/,
													// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
													// "����"*/, 2,"�б�����");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
												// "��ѯ"*/,
												// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
												// "��ѯ"*/, 2,"�б���ѯ");
												// /*-=notranslate=-*/
	private ButtonObject m_btnCard = null;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
											// "��Ƭ��ʾ"*/,
											// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
											// "��Ƭ��ʾ"*/, 2,"�б��л�");
											// /*-=notranslate=-*/
	private ButtonObject m_btnEndCreate = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000043")/*@res
												// "����ת��"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000234")/*@res
												// "��������������"*/, 2,"����ת��");
												// /*-=notranslate=-*/
	private ButtonObject m_btnRefreshList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
													// "ˢ��"*/,
													// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
													// "ˢ��"*/, 2,"ˢ��");
													// /*-=notranslate=-*/

	// ������
	private ButtonObject m_btnUsableList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
												// "������ѯ"*/,
												// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
												// "������ѯ"*/, 2,"�б�������ѯ");
												// /*-=notranslate=-*/
	protected ButtonObject m_btnDocumentList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
													// "�ĵ�����"*/,
													// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
													// "�ĵ�����"*/, 2,"�б��ĵ�����");
													// /*-=notranslate=-*/
	private ButtonObject m_btnQueryBOMList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
													// "���׼�"*/,
													// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
													// "���׼�"*/, 2,"�б����׼�");
													// /*-=notranslate=-*/
	private ButtonObject m_btnPrintPreviewList = null;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
														// "Ԥ��"*/,
														// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
														// "Ԥ��"*/, 2,"�б���ӡԤ��");
														// /*-=notranslate=-*/
	private ButtonObject m_btnPrintList = null;// ButtonObject(m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
												// "��ӡ����"*/,
												// m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
												// "��ӡ����"*/, 2,"�б���ӡ");
												// /*-=notranslate=-*/
	private ButtonObject m_btnOthersList = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
												// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
												// "����"*/,2, "����");
												// /*-=notranslate=-*/

	/* �б���ť�� */
	private ButtonObject m_aryArrListButtons[] = null;
	/* ��Ϣ���İ�ť�� */
	private ButtonObject m_btnAudit = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000027")/*
											// @res "����"
											// */,m_lanResTool.getStrByID("common","UC001-0000027")/*
											// @res "����" */, 2, "����");
											// /*-=notranslate=-*/
	private ButtonObject m_btnUnAudit = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000028")/*
												// @res "����"
												// */,m_lanResTool.getStrByID("40040401","UPP40040401-000149")/*
												// @res "ִ���������" */, 5, "����");
												// /*-=notranslate=-*/
	private ButtonObject m_btnOthersMsgCenter = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
														// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
														// "����"*/,2, "����");
	private ButtonObject m_btnActionMsgCenter = null;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
														// "ִ��"*/,
														// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
														// "ִ��"*/, 0,"ִ��");
														// /*-=notranslate=-*/
	private ButtonObject m_aryMsgCenter[] = null;

	// ������ѯ�����Ի���
	private RcQueDlg m_dlgArrQueryCondition = null;
	/* �����ջ��Ի��� */
	private QueryOrdDlg m_dlgQuickArr = null;
	// ���浥����ID��Ӧ�Ĵ������ID
	private Hashtable m_hBillIDsForCmangids = new Hashtable();
	/* ����ʱ������ڴ������� */
	private HashMap m_hTS = null;
	// //�����Ƿ񸨼���������־
	// Hashtable m_hIsAssMana = new Hashtable();
	// boolean isFlagsCache = false;
	// //���滻���ʡ��Ƿ�̶�������
	// Hashtable m_hConvertIsfixed = new Hashtable();
	// ���汣��������
	private Hashtable m_hValiddays = new Hashtable();
	// ������ǰ��
	private int m_iArrCurrRow = 0;
	/* ��¼:ת��ǰ�û���ʾ���ݻ���λ�� */
	private int m_OldCardVOPos = 0;
	/* ��¼:ת����ǰ�û��������ݳ��� */
	private int m_OldVOsLen = 0;
	/* ���Ų��� */
	private UIRefPane m_PnlLotRef = null;
	// ������ӡ�����б���ͷ�����к�
	protected ArrayList listSelectBillsPos = null;
	/* ������ʽ����VOs */
	private ArriveorderVO[] m_pushSaveVOs = null;
	// ����״̬:��ʼ������������������޸ģ������б���ת���б���ת���޸ģ���Ϣ����
	private String m_strState = "��ʼ��";
	/* ֧��ת������治�����ʾ��������� */
	/* ��¼:ת���󻺴������е�����VO[],��ʼֵΪת��ǰ�û��������,�û�����ɹ�һ�ŵ���,����������һ�ŵ��� */
	private ArriveorderVO[] m_VOsAll = null;
	private int nAssistUnitDecimal = 2;
	private int nConvertDecimal = 2;
	/** �ؼ��ֶ�Ӧ�ļ��㹫ʽ��ĳ��� ( �μ� RelationsCal ) */
	private int[] nDescriptions = new int[] { RelationsCal.IS_FIXED_CONVERT,
			RelationsCal.CONVERT_RATE, RelationsCal.NUM_ASSIST,
			RelationsCal.NUM,
			// RelationsCal.NUM_QUALIFIED,
			// RelationsCal.NUM_UNQUALIFIED,
			RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.MONEY_ORIGINAL };
	// ȡϵͳ������
	private int nMeasDecimal = 2;
	// �����÷���
	private int nNmoneyDecimal = 2;
	private int nPriceDecimal = 2;
	// ҵ����������
	// private String pk_busitype = null;
	// ������ӡ����
	private ScmPrintTool printList = null;
	private ScmPrintTool printCard = null;
	// ҵ������(���˶��������˵�����ʱ���õ�)
	private UIRefPane refBusi = null;
	/** ��ʽ�����õ����� */
	/** ���ü��㹫ʽ�ؼ����б�(��������ʱҲҪ���������йؼ���) */
	private String[] strKeys = new String[] { "Y", "convertrate", "nassistnum",
			"narrvnum",
			// "nelignum", "nnotelignum",
			"nprice", "nmoney" };
	// ����ֿ��Ƿ���л�λ����
	// Hashtable m_hIsAllot = new Hashtable();
	// �Ƿ��Ѿ�������ֿ���л�λ����
	// boolean isFlagsCacheAllot = false;
	// �ϲ���Դ��������
	// private String upBillType = "21";
	// ��¼�����ɾ����
	private Vector v_DeletedItems = new Vector();
	// ��¼δ����ֵ�ɾ����
	private Vector vDelNoSplitted = new Vector();
	// ��ǰ��¼����Ա��Ȩ�޵Ĺ�˾[]
	private String[] saPkCorp = null;

	// �Ƿ����Ƶ���
	private boolean m_bSaveMaker = true;
	// ��Ϣ���ĵ���ID
	private String m_strBillId = null;

	// �����ջ������г����쳣��־
	private boolean m_bQuickException = false;

	/****************************/
	private Hashtable invidinfo;

	private ArrayList PilenoList;
	nc.vo.scm.ic.bill.FreeVO voFree;
	private String oid_cmangid = new String();

	/****************************/

	/**
	 * ��ȡ�Ƿ�����ջ������г����쳣
	 */
	public boolean isQuickException() {
		return m_bQuickException;
	}

	/**
	 * �����Ƿ�����ջ������г����쳣
	 */
	public void setQuickExceptionFlag(boolean newVal) {
		m_bQuickException = newVal;
	}

	/**
	 * ArriveUI ������ע�⡣
	 */
	public ArriveUI() {
		super();
		initialize();
	}

	/**
	 * ArriveUI ������ע�⡣
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
	 * ���������������հ�ť�¼� ���������������bodyRowChange()��֤
	 * ���û���������������bodyRowChange()���������ص������ť �������ڣ�(2001-10-20 11:25:46)
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
	 * �༭���¼�--��ͷ�����֯
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
	 * @���ܣ��༭���¼� --> ĳ���Ըı�󴥷����߼�
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
		// �����к�
		else if (e.getKey().equals("crowno")) {
			BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e,
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE);
		} else if (
		// ����
		(e.getKey().equals("convertrate") || e.getKey().equals("nassistnum")
				|| e.getKey().equals("narrvnum") || e.getKey().equals("nprice")
				|| e.getKey().equals("nmoney") || e.getKey().equals("nelignum") || e
				.getKey().equals("nnotelignum"))) {
			// ��ʽ����
			afterEditWhenNum(e);
			// ����������Ϊ�෴����ʱ�Ĵ���
			if (e.getKey().equals("narrvnum")
					|| e.getKey().equals("nassistnum"))
				afterSignChged(e);
			// ������Ʒ
			afterEditWhenBodyLargessNums(e.getRow());
		} else if (e.getKey().equals("cassistunitname")) {
			// ������
			afterEditWhenAssistUnit(e);
		} else if (e.getKey().equals("cinventorycode")) {
			// ���
			afterEditWhenInv(e);
		} else if (e.getKey().equals("cemployeeid")) {
			// �ɹ�Ա
			afterEditWhenHeadEmployee(e);
		} else if (e.getKey().equals("vproducenum")) {
			// ����
			afterEditWhenProdNum(e);
		} else if (e.getKey().equals("dproducedate")) {
			// ��������
			afterEditWhenProdDate(e);
		} else if (e.getKey().equals("ivalidday")) {
			// ����������
			afterEditWhenValidDays(e);
		} else if (e.getKey().equals("vmemo") && e.getPos() == 1) {
			if (getBillCardPanel().getBodyItem("vmemo") != null) {
				// ���屸ע
				UIRefPane refBodyVmemo = (UIRefPane) getBillCardPanel()
						.getBodyItem("vmemo").getComponent();
				bm.setValueAt(refBodyVmemo.getUITextField().getText(),
						e.getRow(), "vmemo");
			}
		} else if (e.getKey().equals("vbackreasonb") && e.getPos() == 1) {
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				// �����˻�����
				UIRefPane refBodyReason = (UIRefPane) getBillCardPanel()
						.getBodyItem("vbackreasonb").getComponent();
				bm.setValueAt(refBodyReason.getUITextField().getText(),
						e.getRow(), "vbackreasonb");
			}
		} else if (e.getKey().equals("vfree0")) {
			// ������
			afterEditFree(e);
		} else if (e.getKey().equals("cwarehousename")) {
			// �ֿ�
			try {
				afterEditWhenWareHouse(e);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getKey().equals("cproject")) {
			// ��Ŀ
			afterEditWhenProject(e);
			getSpace(e);
			// ��Դ����������Ʒ�У���Ʒ��־���ܸ���
		} else if (e.getKey().equals("blargess")) {
			afterEditWhenBodyLargessNums(e.getRow());
		}
		// �Զ�����PK����
		if (e.getPos() == 1)
			setBodyDefPK(e);
		else
			setHeadDefPK(e);
		if (getParentCorpCode().equals("10395")) {
			if (e.getKey().equals("StampNo")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem(
						"vdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", "������������Ϊ��!The entire stack Quantity can not be empty!");
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
	 * ��������ֶμ���Ʒ��־�ֶα༭���¼�����
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
	 * ������༭�¼� �������ڣ�(2001-11-28 12:13:08)
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
			if (freeVO == null) {//edit by shikun 2014-11-14 ������1�༭��Ч����--�ӱ���
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
	 * �������༭�¼� ������ # �����������༭ʱ�������� ==>> ������ | # ѡȡ�ļ���ID��������ID���任����Ϊ1���̶������� | #
	 * �ɡ������ʡ�������ʽ���� > ����ɾ������ϸ������Ƿ�ɱ༭ # ͬ�����µ���ģ���еĻ����ʺ��Ƿ�̶����������� | # ���»����������пɱ༭��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenAssistUnit(BillEditEvent e) {
		// �Ƿ�̶��仯�ʸı�ʱҪͬ�����ģ�strKeys[0]��ֵ
		UFBoolean isfixed = new UFBoolean(true);
		UFDouble convert = new UFDouble(0);
		// ���ID
		String sBaseID = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cbaseid");
		// ����������
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
		// ��ȡ������
		convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// �Ƿ�̶�������
		isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
		// �����������༭
		if (e.getKey().equals("cassistunitname")) { // ���ø���������
			setRefPaneAssistunit(e.getRow());
			// ���ø���Ϣ
			setAssisUnitEditState2(e);
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			getBillCardPanel().getBillModel().setValueAt(convert, e.getRow(),
					"convertrate");
			isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
			if (isfixed.booleanValue())
				strKeys[0] = "Y";
			else
				strKeys[0] = "N";
			// �û������������㣺�������������������ϸ����������ϸ����������ۣ����
			RelationsCal.calculate(e, getBillCardPanel(), "convertrate",
					nDescriptions, strKeys, ArriveorderItemVO.class.getName());
			// �ϸ������ɱ༭��
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
	 * ��ͷ�༭���¼�-�ɹ�Ա
	 * 
	 * @param e
	 */
	private void afterEditWhenHeadEmployee(BillEditEvent e) {

		Logger.info("����afterEditWhenHeadEmployee()");/* -=notranslate=- */

		String sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(
				"cemployeeid").getComponent()).getRefPK();

		Logger.info("sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(��cemployeeid��).getComponent()).getRefPK()="
				+ sPsnId);/* -=notranslate=- */

		if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {

			Logger.info("PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null ��true");/*
																					 * -=
																					 * notranslate
																					 * =
																					 * -
																					 */

			return;
		}
		// ��ҵ��Ա����Ĭ�ϲ���
		UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem(
				"cemployeeid").getComponent());

		Logger.info("getBillCardPanel().getHeadItem(��cemployeeid��).getComponent()��"
				+ ref.toString());/* -=notranslate=- */

		// ҵ��Ա��������
		Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");

		Logger.info("ref.getRefModel().getValue(��bd_psndoc.pk_deptdoc��)"
				+ sDeptId);/* -=notranslate=- */

		getBillCardPanel().getHeadItem("cdeptid").setValue(sDeptId);

		Logger.info("�ӷ���afterEditWhenHeadEmployee()��������");/* -=notranslate=- */
	}

	/**
	 * ����༭�¼� �������ڣ�(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenInv(BillEditEvent e) {
		// �ı���ʱ,���������,������,����,����,���ȵ������Ϣ
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
		// ����������
		setAssisUnitEditState2(e);
		// ������κŹ�������
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
	 * �����༭�¼� �������ڣ�(2001-11-28 12:13:08)
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
										"UPPSCMCommon-000059")/* @res "����" */,
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000275")/*
															 * @res "�˻�����������Ϊ��!"
															 */);
						return;
					}
				}
			}
		}

		UFBoolean isfixed = new UFBoolean(true);
		// ���ID
		String sBaseID = (String) getBillCardPanel().getBillModel().getValueAt(
				e.getRow(), "cbaseid");
		// ����������
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
		// ��ȡ������
		// convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// �Ƿ�̶�������
		isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
		// �Զ����㣺�����������������������ʣ��ϸ����������ϸ����������ۣ����
		if ((e.getKey().equals("convertrate")
				|| e.getKey().equals("nassistnum")
				|| e.getKey().equals("narrvnum") || e.getKey().equals("nprice")
				|| e.getKey().equals("nmoney") || e.getKey().equals("nelignum") || e
				.getKey().equals("nnotelignum"))) {
			// ������ݺϷ��ԣ����Ϸ��ָ�ԭֵ����
			String strErr = getErrMsg(e);
			if (strErr != null) {
				MessageDialog.showErrorDlg(
						this,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000085")/* @res "���ݴ���" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000086")/* @res "�������ݴ���\n" */
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

			// ֻ�������͸������༭�������������
			if (e.getKey().equals("nassistnum")
					|| e.getKey().equals("narrvnum"))
				setEditAndDirect(e);
		}
	}

	/**
	 * �������ڱ༭�¼� �������������� + ���������� = ʧЧ����(���ɱ༭) ע���������ڻ���������һ��Ϊ����ʧЧ����Ϊ��
	 */
	private void afterEditWhenProdDate(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		/** ��ȡ��ǰ�༭������VO -- item ,ע�⣺�����ڳ�ʼ��ʱ�ɷ������˴��ݹ�������Ϊ�����Ƕ���ת��ĵ��� */
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
			// ʧЧ����(���ɱ༭)
			bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
			item.setDvaliddate(dvaliddate);
		}
	}

	/**
	 * ���ű༭�¼�
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
			// �������ڣ�ʧЧ���ڣ�����������
			UFDate dateProduce = dateValid.getDateBefore(iDays);
			bm.setValueAt(dateProduce, e.getRow(), "dproducedate");
			bm.setValueAt(dateValid, e.getRow(), "dvaliddate");
		} catch (Exception ex) {
			SCMEnv.out("���ű༭������������ڳ����쳣����ϸ��Ϣ���£�");
			reportException(ex);
		}
	}

	/**
	 * ��Ŀ�༭�¼�
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenProject(BillEditEvent e) {
		int row = e.getRow();
		// ��Ŀ
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
			// ������Ŀ�׶β��ɱ༭
			getBillCardPanel().setCellEditable(row, "cprojectphase",
					getBillCardPanel().getBodyItem("cprojectphase").isEdit());
		} else {
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphase");
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphasebaseid");
			getBillCardPanel().getBillModel().setValueAt(null, row,
					"cprojectphaseid");
			// ������Ŀ�׶β��ɱ༭
			getBillCardPanel().setCellEditable(row, "cprojectphase", false);
		}
	}

	/**
	 * �����������༭�¼� �������������� + ���������� = ʧЧ����(���ɱ༭) ע���������ڻ���������һ��Ϊ����ʧЧ����Ϊ��
	 */
	private void afterEditWhenValidDays(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		/** ��ȡ��ǰ�༭������VO -- item ,ע�⣺�����ڳ�ʼ��ʱ�ɷ������˴��ݹ�������Ϊ�����Ƕ���ת��ĵ��� */
		ArriveorderItemVO item = (ArriveorderItemVO) bm.getBodyValueRowVO(
				e.getRow(), ArriveorderItemVO.class.getName());
		// ����������
		Object ivalidday = bm.getValueAt(e.getRow(), "ivalidday");
		if (item.getDproducedate() == null
				|| item.getDproducedate().toString().trim().equals("")
				|| ivalidday == null || ivalidday.toString().trim().equals("")) {
			item.setDvaliddate(null);
			bm.setValueAt(null, e.getRow(), "dvaliddate");
		} else {
			UFDate dvaliddate = item.getDproducedate().getDateAfter(
					item.getIvalidday().intValue());
			// ʧЧ����(���ɱ༭)
			bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
			item.setDvaliddate(dvaliddate);
		}
	}

	/**
	 * �ֿ�༭�¼� �������ڣ�(2001-11-28 12:13:08)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * @throws BusinessException 
	 */
	private void afterEditWhenWareHouse(BillEditEvent e) throws BusinessException {
		// BillModel bm = getArrBillCardPanel().getBillModel();
		// //���²ֿ��Ƿ��λ��������
		// String cwarehouseid = (String) bm.getValueAt(e.getRow(),
		// "cwarehouseid");
		// UFBoolean isAllot = null;
		// if (cwarehouseid != null && !cwarehouseid.trim().equals("")) {
		// //�ı��λ����ģ��
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
	 * ���������෴�༭�¼� �������ڣ�(2001-11-28 12:13:08)
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
	 * @���ܣ����뵱ǰ�����ת�뵽��������¼���е��ݵĻ�����
	 */
	private void appArriveorderVOSaved(ArriveorderVO voSaved) {
		if (voSaved == null)
			return;
		/* ���´�������� m_VOsAll */
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
	 * �༭ǰ����
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getKey().equals("vfree0")) {
			// �������
			boolean bCanEdit = PuTool.beforeEditInvBillBodyFree(
					getBillCardPanel(), e, new String[] { "cmangid",
							"cinventorycode", "cinventoryname",
							"cinventoryspec", "cinventorytype" }, new String[] {
							"vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
							"vfree5" });
			return bCanEdit;

		} else if (e.getKey().equalsIgnoreCase("vproducenum")) {
			// ���κŴ���
			return beforeEditProdNum(e);
			// return true;
		} else if (e.getKey().equals("cprojectphase")) {
			Object oTmp = getBillCardPanel().getBillModel().getValueAt(
					e.getRow(), "cproject");
			if (oTmp == null || oTmp.toString().trim().equals(""))
				return false;
		}
		// �ֿ�
		else if (e.getKey().equals("cwarehousename")) {
			((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename")
					.getComponent()).setPk_corp(getCorpPrimaryKey());
			PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(),
					getCorpPrimaryKey(),
					getBillCardPanel().getHeadItem("cstoreorganization")
							.getValue(), "cwarehousename");
		}
		// �����������
		else if (e.getKey().equalsIgnoreCase("cinventorycode")) {
			return beforeEditInv(e);
		}
		// ��Ŀ�׶�
		else if (e.getKey().equalsIgnoreCase("cprojectphase")) {
			return beforeEditProjectPhase(e);
		}
		//ncm begin liuydc
	    else if (e.getKey().equalsIgnoreCase("cstorename")) {
			String cwarehouseid = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "cwarehouseid");
			if (cwarehouseid == null)
				return false;
			UIRefPane pane = (UIRefPane) getBillCardPanel().getBodyItem(e.getKey()).getComponent();
			// ���ղֿ���˻�λ����
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
		String strBillLinKey = getStateStr().equals("ת���޸�") ? "corder_bid"
				: "carriveorder_bid";
		if (bm.getValueAt(e.getRow(), strBillLinKey) == null) {
			SCMEnv.out("1-����������Դ�������ܻ�ȡ�������򶩵�ID���������༭������");
			return false;
		}
		if (m_hBillIDsForCmangids == null) {
			SCMEnv.out("2-����������Դ�������ܻ�ȡ�������򶩵�ID���������༭������");
			return false;
		}
		String cmangid = (String) m_hBillIDsForCmangids.get(bm.getValueAt(
				e.getRow(), strBillLinKey));
		if (cmangid == null || cmangid.trim().equals("")) {
			SCMEnv.out("3-����������Դ�������ܻ�ȡ�������򶩵�ID���������༭������");
			return false;
		}
		InvRefModelForRepl refmodel = null;
		if (!bLargessUpRow.booleanValue() && bLargess.booleanValue()) {
			// �����в�����Ʒ�ҵ�����������Ʒ����������ݴ������ȡ���У�������������滻����
			refmodel = new InvRefModelForRepl(cmangid, ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), true);
		} else {
			// �����������Ϊ����������滻��
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
	 * ���ܣ��༭���κ�ǰ�����κŲ��մ��� ������ ���أ� ���⣺ ���ڣ�(2002-9-16 13:01:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private boolean beforeEditProdNum(BillEditEvent e) {

		int iRow = e.getRow();
		ParaVOForBatch vo = new ParaVOForBatch();
		// ����FieldName
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
		// ���ÿ�Ƭģ��,��˾��
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
	 * �����޸��б任ʱ�������(�б任�¼�)
	 */
	private void bmrcSetForModify(BillEditEvent e) {
		BillModel bm = getBillCardPanel().getBillModel();
		Object obj = bm.getValueAt(e.getRow(), "naccumchecknum");
		Object objElg = bm.getValueAt(e.getRow(), "nelignum");
		Object objNotElg = bm.getValueAt(e.getRow(), "nnotelignum");
		/*
		 * ���ɱ༭�߼�(������һ)��1)�� �ʼ����� != 0 ���� ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�2)�� �ϸ�����
		 * != 0 ���� ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�3)�����ϸ����� != 0 ����
		 * ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�
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
			// ��ť�߼�
			// m_btnDelLine.setEnabled(false);
			setBtnLines(false);
			// updateButton(m_btnDelLine);
			// m_btnAllotCarg.setEnabled(false);
			// updateButton(m_btnAllotCarg);
			// ���пɱ༭������Ϊ���ɱ༭
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
			// û�б����찴ť�߼�
			// m_btnDelLine.setEnabled(true);
			setBtnLines(true);
			// updateButton(m_btnDelLine);
		}
	}

	/**
	 * ���ܣ�������Ŀ�׶β���
	 */
	private boolean beforeEditProjectPhase(BillEditEvent e) {
		int row = e.getRow();
		if (row < 0) {
			return false;
		}
		getBillCardPanel().stopEditing();
		// ��Ŀ
		Object o = getBillCardPanel().getBillModel().getValueAt(row,
				"cprojectid");
		Object pk_corp = getBillCardPanel().getBillModel().getValueAt(row,
				"pk_corp");
		String cprojectid = null;
		// ��Ŀ�׶β���
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
	 * �ֿ��Ƿ���л�λ����(�б任�¼�)
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
		 * //���˻�λ UIRefPane refCarg = (UIRefPane)
		 * getArrBillCardPanel().getBodyItem("cstorename").getComponent();
		 * refCarg.getRefModel().addWherePart( "and bd_cargdoc.pk_stordoc = '" +
		 * cwarehouseid + "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' "); }
		 */
	}

	/**
	 * ���ܣ��иı� 1.�����б� 2.ת���б� 3.�����޸ļ�ת���޸�
	 */
	public void bodyRowChange(BillEditEvent e) {
		if (getStateStr().equals("�����б�")) {
			bodyRowChangeLookList(e);
		} else if (getStateStr().equals("ת���޸�") || getStateStr().equals("�����޸�")) {
			bodyRowChangeEdit(e);
		}
	}

	/**
	 * ���ܣ������иı�ʱ�Ĵ���(�����޸ļ�ת���޸�)
	 */
	private void bodyRowChangeEdit(BillEditEvent e) {
		/** ���úϸ������༭������������������ */
		setEditAndDirect(e);
		/** �Ƿ���ʾ�����ť(�Ƿ����������) */
		// bmrcSetForFree(e); V31 czp del ͳһ�ڱ༭ǰ����
		/** ���ø�������Ϣ */
		setAssisUnitEditState2(e);
		/** ����滻�������� */
		// bmrcSetForInvRef(e);V5�ƶ����༭ǰ�¼�
		/** �ֿ⣺�Ƿ���л�λ���� */
		bmrcSetForWareAlot(e);
		/** �����޸�ʱ������� */
		if (getStateStr().equals("�����޸�")) {
			bmrcSetForModify(e);
		}
		/** ����Ƿ����κŹ��� */
		// bmrcSetForProdNum(e);V5�ƶ����༭ǰ�¼�
		/** ��Ŀ�������� */
		// bmrcSetForRefPaneProject(e);V5�ƶ����༭ǰ�¼�

		// �����˵��Ҽ�����Ȩ�޿���
		rightButtonRightControl();
	}

	/**
	 * ����б��б任ʱ���� �������ڣ�(2001-11-18 15:25:26)
	 */
	private void bodyRowChangeLookList(BillEditEvent e) {
		// ѡ�����߼�
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
	 * @���ܣ������޸����ݵĺϷ���
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 14:47:40)
	 * @return boolean
	 * @param newvo
	 *            nc.vo.rc.receive.ArriveorderVO
	 * @param oldvo
	 *            nc.vo.rc.receive.ArriveorderVO
	 */
	private boolean checkModifyData(ArriveorderVO newvo, ArriveorderVO oldvo) {
		try {
			/** ���������������ʱ����ϢΪ������ */
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
									"UC000-0003938")/* @res "����λ" */;
						if (objAssNum == null
								|| objAssNum.toString().trim().equals("")) {
							if (strErr.length() > 0)
								strErr += m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000000")/* @res "��" */;
							strErr += m_lanResTool.getStrByID("common",
									"UC000-0003971")/* @res "������" */;
						}
						if (objExhRate == null
								|| objExhRate.toString().trim().equals("")) {
							if (strErr.length() > 0)
								strErr += m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000000")/* @res "��" */;
							strErr += m_lanResTool.getStrByID("common",
									"UC000-0002161")/* @res "������" */;
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
						"UPP40040301-000100")/* @res "�и����������Ĵ�����ֿ��\n" */;
				while (keys.hasMoreElements()) {
					iKey = (Integer) keys.nextElement();
					vTmp.addElement(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000101")/* @res "���� " */
							+ iKey + ": " + hErr.get(iKey) + "\n");
				}
				for (int i = vTmp.size() - 1; i >= 0; i--) {
					strErr += vTmp.elementAt(i);
				}
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */,
						strErr);
				return false;
			}
			ArriveorderHeaderVO head = (ArriveorderHeaderVO) newvo
					.getParentVO();
			// ��������
			String arrdate = null;
			if (!(head.getAttributeValue("dreceivedate") == null || head
					.getAttributeValue("dreceivedate").toString().trim()
					.equals(""))) {
				arrdate = head.getAttributeValue("dreceivedate").toString();
			}
			// ��Ӧ��
			String vendor = (String) head.getAttributeValue("cvendormangid");
			// ����
			String dept = (String) head.getAttributeValue("cdeptid");
			// ҵ��Ա
			String empl = (String) head.getAttributeValue("cemployeeid");
			// ҵ������
			String busi = (String) head.getAttributeValue("cbiztype");
			// �����֯
			String sStoreOrgId = head.getCstoreorganization();

			if (arrdate == null || arrdate.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000102")/* @res "�������ڲ���Ϊ��" */);
				return false;
			} else if (vendor == null || vendor.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000103")/* @res "��Ӧ�̲���Ϊ��" */);
				return false;
			} else if (dept == null || dept.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000104")/* @res "���Ų���Ϊ��" */);
				return false;
			} else if (empl == null || empl.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000105")/* @res "ҵ��Ա����Ϊ��" */);
				return false;
			} else if (busi == null || busi.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000106")/* @res "ҵ�����Ͳ���Ϊ��" */);
				return false;
			} else if (sStoreOrgId == null || sStoreOrgId.trim().equals("")) {
				showErrorMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000107")/* @res "�����֯����Ϊ��" */);
				return false;
			}

			// ���������пմ������
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
								"UPPSCMCommon-000000")/* @res "��" */;
					}
					lines += line + 1;
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000108")/*
															 * @res
															 * "�д�����벻��Ϊ�գ�������������"
															 */);
				return false;
			}
			// ������������Ϊ�����
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
								"UPPSCMCommon-000000")/* @res "��" */;
					}
					lines += line + 1;
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000087")/*
															 * @res
															 * "�д��������������Ϊ�㣬Ҳ����Ϊ�գ������뵽������"
															 */);
				return false;
			}
			// ������������Ϊ������
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
					// �ǿշ���ʱ���Ϊ��
					if (((UFDouble) getBillCardPanel().getBillModel()
							.getValueAt(line, "nprice")).doubleValue() < 0) {
						if (!lines.trim().equals("")) {
							lines += m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000000")/* @res "��" */;
						}
						lines += line + 1;
					}
				}
			}
			if (!lines.trim().equals("")) {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000109")/*
															 * @res
															 * "�д�������۸�С���㣬���������뵽���۸�"
															 */);
				return false;
			}
			// //|��Ʒ����|<=|��������|
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
			// lines += "��";
			// }
			// lines += line + 1;
			// }
			// }
			// if (!lines.trim().equals("")) {
			// MessageDialog.showWarningDlg(
			// this,
			// "��ʾ",
			// "��������������Ʒ������"
			// + CommonConstant.BEGIN_MARK
			// + lines
			// + CommonConstant.END_MARK
			// + "�д����Ʒ�������ڵ������������������������뵽����������Ʒ����");
			// return false;
			// }
		} catch (Exception e) {
			reportException(e);
			return false;
		}
		return true;
	}

	/**
	 * @���ܣ�ɾ����ǰ���ϵĵ�����(��Ƭ)
	 * @���ߣ���־ƽ �������ڣ�(2001-10-08 20:16:16)
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
			// ת���޸Ļ��޸�������״̬�ڱ��浽������������ʾλ��
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
	 * @���ܣ�ɾ����ǰ�����ת�뵽����(��Ƭ)
	 * @���ߣ���־ƽ �������ڣ�(2001-7-31 20:11:16)
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
			// ת���޸Ļ��޸�������״̬�ڱ��浽������������ʾλ��
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
	 * @���ܣ�ɾ����ǰ���ϵĵ�����(�б�)
	 * @���ߣ���־ƽ �������ڣ�(2001-10-09 20:10:16)
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
							"UPPSCMCommon-000059")/* @res "����" */, m_lanResTool
							.getStrByID("40040301", "UPP40040301-000110")/*
																		 * @res
																		 * "ˢ����ʾǰ�˻���ʱ����"
																		 */);
		}
	}

	/**
	 * @���ܣ��л����б�����(ά���޸�=>>�б�)
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 9:11:08)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void displayArrBillListPanel() {

		// ���ý���״̬
		setM_strState("�����б�");

		// ��ʾ���ݵ��б�����
		loadDataToList();

		// ��ʾ�б�����
		if (!m_bLoaded) {
			add(getBillListPanel(), "Center");
			m_bLoaded = true;
		}
		getBillCardPanel().setVisible(false);
		getBillListPanel().setVisible(true);

		// Ĭ����ʾ��һ��
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
	 * @���ܣ��л����б�����(ת���޸�=>>�б�)
	 */
	private void displayArrBillListPanelNew() {

		// ���ý���״̬
		setM_strState("ת���б�");
		setButtonsState();
		// ��ʾ���ݵ��б�����
		loadDataToList();
		// ��ʾ�б�����
		if (!m_bLoaded) {
			add(getBillListPanel(), "Center");
			m_bLoaded = true;
		}
		getBillListPanel().setVisible(true);
		getBillCardPanel().setVisible(false);
		// �б�״̬Ĭ����ʾ����
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
	 * ���ߣ���ά�� ���ܣ��˴����뷽��˵�� ������ ���أ� ���⣺ ���ڣ�(2004-3-16 15:28:25)
	 */
	public String getAccYear() {
		return getClientEnvironment().getAccountYear();
	}

	/**
	 * ��������:��õ�������Ƭ ��ʼ�������� 1.���������� 2.��ͷ��ע�趨�� (1).���Զ��췵��ֵ�� (2).����ֵ��Ϊ���� 3.��ͷ��ע�趨��
	 * (1).���Զ��췵��ֵ�� (2).����ֵ��Ϊ���� 4.������>=0 5.����>=0 6.�ջ��ֿ���չ��˵���Ʒ�� 7.��λ�Ƿ��
	 * 8.����ģ������� 9.���ȴ��� 2002-08-07 wyf �޸Ĳɹ����ż�ҵ��Ա�����ݿ����Ͳ�ƥ������ DB2 2002-08-08 wyf
	 * �޸�һ��SQL����
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
				// ����ģ���б������ܲ˵���ʼ��
				m_arrBillPanel.setBodyMenuShow(true);
				UIMenuItem[] miBody = m_arrBillPanel.getBodyMenuItems();
				if (miBody != null && miBody.length >= 3) {
					miBody[0].setVisible(false);
					miBody[2].setVisible(false);
				}
				m_arrBillPanel.addBodyMenuListener(this);
				// ����ǧ��λ
				m_arrBillPanel.setBodyShowThMark(true);
				// �����ȼ�
				m_arrBillPanel.hideBodyTableCol("squalitylevelname");
				// ���鴦�����
				m_arrBillPanel.hideBodyTableCol("cdealname");
				// �кŵ�����
				if (m_arrBillPanel.getBodyItem("crowno") != null) {
					BillRowNo.loadRowNoItem(m_arrBillPanel, "crowno");
				}
				// �����Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
						m_arrBillPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Arrive, // ��������
						"vdef", "vdef");
				// �Ӻϼ���
				m_arrBillPanel.setTatolRowShow(true);
				// ����ģ��༭������
				m_arrBillPanel.addEditListener(this);
				// �༭ǰ����
				m_arrBillPanel.addBodyEditListener2(this);
				// �����������
				m_arrBillPanel.getBodyPanel().addTableSortListener();
				m_arrBillPanel.getBillModel().setRowSort(true);
				// �����к��������
				m_arrBillPanel.getBillModel().setSortPrepareListener(this);
			} catch (java.lang.Throwable e) {
				SCMEnv.out("��ʼ������ģ��(��Ƭ)ʱ�����쳣��");
				SCMEnv.out(e);
			}
		}
		return m_arrBillPanel;
	}

	/**
	 * ��õ������б�
	 * 
	 */
	private BillListPanel getBillListPanel() {
		if (m_arrListPanel == null) {
			try {
				m_arrListPanel = new BillListPanel();
				// ����ģ��
				try {
					m_arrListPanel.loadTemplet(ScmConst.PO_Arrive, null,
							getOperatorId(), getCorpPrimaryKey());
				} catch (Exception ex) {
					reportException(ex);
					m_arrListPanel.loadTemplet("40040301010000000000");
				}
				// ����ǧ��λ
				m_arrListPanel.getParentListPanel().setShowThMark(true);
				m_arrListPanel.getChildListPanel().setShowThMark(true);

				// ��ʼ���б�����
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

				// �����ȼ�
				if (m_arrListPanel.getBodyItem("squalitylevelname") != null)
					m_arrListPanel.hideBodyTableCol("squalitylevelname");
				// ���鴦�����
				if (m_arrListPanel.getBodyItem("cdealname") != null)
					m_arrListPanel.hideBodyTableCol("cdealname");

				// �����б��Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(
						m_arrListPanel, getCorpPrimaryKey(),
						ScmConst.PO_Arrive, // ��������
						"vdef", "vdef");
				// �����б��ϼ�
				m_arrListPanel.getChildListPanel().setTotalRowShow(true);

				// ���ݱ༭����
				m_arrListPanel.addEditListener(this);
				m_arrListPanel.addMouseListener(this);
				// ��ѡ����
				m_arrListPanel.getHeadTable().setCellSelectionEnabled(false);
				m_arrListPanel
						.getHeadTable()
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				m_arrListPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);
				// �к��������
				m_arrListPanel.getBodyBillModel().setSortPrepareListener(this);
				// �б������ֶ��������
				m_arrListPanel.getHeadBillModel().addSortRelaObjectListener2(
						this);

			} catch (Exception e) {
				SCMEnv.out("��ʼ������ģ��(�б�)ʱ�����쳣��");
				reportException(e);
			}
		}
		return m_arrListPanel;
	}

	/**
	 * @���ܣ����ص�����VO�����ͷVO����
	 * @���ߣ�Administrator �������ڣ�(2001-6-8 21:53:25)
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
	 * @���ܣ���ѯ�����õ���������VO
	 */
	private void getArriveVOsFromDB() {
		try {
			/* �û��Զ������� */
			ConditionVO[] condsUserDef = getQueryConditionDlg()
					.getConditionVO();

			/* �û��������� */
			ConditionVO[] condsNormal = getQueryConditionDlg()
					.getNormalCondsVO();
			/* ��Դ������Ϣ���� */
			String strUpSrcSQlPart = getQueryConditionDlg().getUpSrcPnl()
					.getSubSQL();
			/* ��ѯ���ݿ� */
			setCacheVOs(ArriveorderHelper.queryAllArriveMy(condsUserDef,
					condsNormal, getCorpPrimaryKey(), getBusitype(),
					strUpSrcSQlPart));
			/* û�в�ѯ������ʱ�Ĵ��� */
			if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
				MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000111")/* @res "û�з��������ļ�¼��" */);
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
			/* �Ѳ�ѯ�����ݻ���{����ɾ������,�����Ӱ�������} */
			else {
				for (int i = 0; i < getCacheVOs().length; i++) {
					if (getCacheVOs()[i].getChildrenVO() != null
							&& getCacheVOs()[i].getChildrenVO().length > 0) {
						// �����ݸ���Դ����
						String cupsourcebilltype = ((ArriveorderItemVO[]) getCacheVOs()[i]
								.getChildrenVO())[0].getCupsourcebilltype();
						((ArriveorderVO) getCacheVOs()[i])
								.setUpBillType(cupsourcebilltype);
						// ˢ�±����ϣ������
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
	 * ���ܣ���ȡ������ѯ�Ի���
	 */
	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return m_atpDlg;
	}

	/**
	 * @���ܣ��˻���ѯ�����Ի���-�ɹ�
	 */
	private PoToBackRcQueDLG getBackQueDlgPo() {
		if (m_backQuePoDlg == null) {
			m_backQuePoDlg = new PoToBackRcQueDLG(this, getCorpPrimaryKey());
		}
		return m_backQuePoDlg;
	}

	/**
	 * @���ܣ��˻���ѯ�����Ի���-ί��
	 */
	private RcToScQueDLG getBackQueDlgSc() {
		if (m_backQueScDlg == null) {
			m_backQueScDlg = new RcToScQueDLG(this, getCorpPrimaryKey(),
					getOperatorId(),
					"40041015", // ��ڵ㣺�������˻���ί�ⶩ��
					getBusitype(), BillTypeConst.PO_ARRIVE,
					BillTypeConst.SC_ORDER, null);
		}
		return m_backQueScDlg;
	}

	/**
	 * @���ܣ���ȡ�˻����ս���-�ɹ�
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
	 * @���ܣ���ȡ�˻����ս���-�ɹ�
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
	 * @���ܣ���ȡҵ������
	 * @���ߣ���־ƽ �������ڣ�(2001-9-4 15:25:00)
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
	 * ���ܣ���������༭ʱ�Ϸ��� 1. narrvnum != null && narrvnum != 0 2. nassistnum != null
	 * && nassistnum != 0 3.|nelignum| <= |narrvnum| 4.|npresentnum| <=
	 * |narrvnum| ����������ģ��༭�¼� ���أ����� ���⣺ ���ڣ�(2002-7-26 9:03:59)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// �����༭ʱ
		if (e.getKey().equals("narrvnum")) {
			if (uArr == null)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000090")/*
																	 * @res
																	 * "����Ϊ��"
																	 */;
			if (uArr.doubleValue() == 0)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000091")/*
																	 * @res
																	 * "����Ϊ��"
																	 */;
		}
		// �������༭ʱ
		if (e.getKey().equals("nassistnum")) {
			if (uAss == null)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000092")/*
																	 * @res
																	 * "������Ϊ��"
																	 */;
			if (uAss.doubleValue() == 0)
				return m_lanResTool
						.getStrByID("40040301", "UPP40040301-000093")/*
																	 * @res
																	 * "������Ϊ��"
																	 */;
		}
		// �ϸ������༭ʱ
		if (e.getKey().equals("nelignum")) {
			if (uElg != null
					&& uElg.abs().doubleValue() > uArr.abs().doubleValue()) {
				strErr = m_lanResTool.getStrByID("40040301",
						"UPP40040301-000114")/* @res "�ϸ���������ֵ���ڵ�����������ֵ" */;
			}
		}
		// ��Ʒ�����༭ʱ
		if (e.getKey().equals("npresentnum")) {
			if (uPrsnt != null
					&& uPrsnt.abs().doubleValue() > uArr.abs().doubleValue()) {
				strErr = m_lanResTool.getStrByID("40040301",
						"UPP40040301-000094")/* @res "��Ʒ��������ֵ���ڵ�����������ֵ" */;
			}
		}
		return strErr;
	}

	/**
	 * ��ȡ���ڵ㹦�ܽڵ�ID �������ڣ�(2001-10-20 17:29:24)
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
	 * @���ܣ���ȡ��λ�Ի���
	 * @���ߣ���־ƽ �������ڣ�(2001-9-15 13:50:13)
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
																			 * "��������λ"
																			 */,
					m_lanResTool.getStrByID("40040301", "UPP40040301-000245")/*
																			 * @res
																			 * "������"
																			 */);
		}
		return locatedlg;
	}

	/**
	 * @���ܣ����ص�����VO����
	 * @���ߣ���־ƽ �������ڣ�(2001-6-19 20:13:12)
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
	 * @���ܣ���ȡ��������ѯ��������Ի���
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
	 * @���ܣ����ص�ǰ��ʾ��VOλ��
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 8:47:47)
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
	 * @���ܣ����ص���״̬
	 * @���ߣ���־ƽ �������ڣ�(2001-6-19 20:18:22)
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
	 * @���ܣ����ز���ԱID
	 * @���ߣ���־ƽ �������ڣ�(2001-10-24 14:12:52)
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
	 * ���ߣ���ά�� ���ܣ��˴����뷽��˵�� ������ ���أ� ���⣺ ���ڣ�(2004-3-16 15:34:30)
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
			SCMEnv.out("���ݲ���Ա������Ա����ʱ����");
			psnid = null;
		}
		return psnid;
	}

	/**
	 * ���ߣ���ά�� ���ܣ��˴����뷽��˵�� ������ ���أ� ���⣺ ���ڣ�(2004-3-15 13:34:10)
	 */
	private QueryOrdDlg getQuickArrDlg() {
		if (m_dlgQuickArr == null)
			m_dlgQuickArr = new QueryOrdDlg(this, this);
		return m_dlgQuickArr;
	}

	/**
	 * ���ܣ�1������δ�ı�ĵ���������VO 2������ɾ���������Ϊ���建���ϣ����֮���� ˵����Ϊ�˶������������������⴦�� czp
	 * 2002-11-13
	 */
	private ArriveorderVO getSaveVO(ArriveorderVO vo) {
		// ���б�������
		Vector vAllBody = new Vector();
		// δ�ı�ı���VO
		ArriveorderItemVO[] voaUIAllBody = (ArriveorderItemVO[]) getBillCardPanel()
				.getBillModel().getBodyValueVOs(
						ArriveorderItemVO.class.getName());
		if (voaUIAllBody == null || voaUIAllBody.length <= 0)
			return vo;
		// δ�ı�ı���VO
		int iNoChangeLen = voaUIAllBody.length;
		for (int i = 0; i < iNoChangeLen; i++) {
			if (voaUIAllBody[i].getStatus() == VOStatus.UNCHANGED) {
				vAllBody.addElement((ArriveorderItemVO) voaUIAllBody[i]);
			}
		}
		// �ı�ı���VO��ƴ�ӱ���������VO�ı���VO��
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
		// ����ɾ���������Ϊ���建���ϣ����֮����
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
		// ȫ��ѡ��ѯ�۵�
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

		// ��ѯδ��������ĵ�����
		try {
			allvos = RcTool.getRefreshedVOs(allvos);
		} catch (BusinessException b) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000116")/* @res "���ִ���:" */+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000117")/* @res "����δ֪����" */);
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
	 * ���ߣ���ά�� ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ��� ������String sItemKey
	 * ITEMKEY ���أ��� ���⣺�� ���ڣ�(2004-03-24 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���ߣ���ά�� ���ܣ��˴����뷽��˵�� ������ ���أ� ���⣺ ���ڣ�(2004-3-16 15:36:26)
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getSysDate() {
		return getClientEnvironment().getDate();
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		String title = m_lanResTool
				.getStrByID("40040301", "UPP40040301-000248")/* @res "������ά��" */;
		if (m_arrBillPanel != null)
			title = m_arrBillPanel.getTitle();
		return title;
	}

	/**
	 * V51�ع���Ҫ��ƥ��,��ťʵ����������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 ����01:15:06
	 */
	private void createBtnInstances() {
		// ����
		m_btnCheck = getBtnTree().getButton(IButtonConstRc.BTN_CHECK);// new
																		// ButtonObject(
																		// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPT40040303-000009")/*@res
																		// "����"*/,
																		// nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000029")/*@res
																		// "���鵽����"*/,
																		// 2,
																		// "����");
																		// /*-=notranslate=-*/
		// ҵ������
		m_btnBusiTypes = getBtnTree().getButton(
				IButtonConstRc.BTN_BUSINESS_TYPE);// new
													// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000003")/*@res
													// "ҵ������"*/,
													// m_lanResTool.getStrByID("common","UC001-0000003")/*@res
													// "ҵ������"*/, 2,"ҵ������");
													// /*-=notranslate=-*/
		// ������
		m_btnAdds = getBtnTree().getButton(IButtonConstRc.BTN_ADD);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000002")/*@res
																	// "����"*/,
																	// m_lanResTool.getStrByID("40040301","UPP40040301-000230")/*@res
																	// "���ɵ�����"*/,
																	// 2,"����");
																	// /*-=notranslate=-*/
		// �˻���
		m_btnBackPo = getBtnTree().getButton(IButtonConstRc.BTN_BACK_PU);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000021")/*@res
																			// "�ɹ�����"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000231")/*@res
																			// "���ղɹ��������ɵ�����"*/,
																			// 2,"�ɹ�����");
																			// /*-=notranslate=-*/
		m_btnBackSc = getBtnTree().getButton(IButtonConstRc.BTN_BACK_SC);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000022")/*@res
																			// "ί�ⶩ��"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000232")/*@res
																			// "����ί�ⶩ�����ɵ�����"*/,
																			// 2,"ί�ⶩ��");
																			// /*-=notranslate=-*/
		m_btnBacks = getBtnTree().getButton(IButtonConstRc.BTN_BACK);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
																		// "�˻�"*/,m_lanResTool.getStrByID("40040301","UPT40040301-000047")/*@res
																		// "�˻�"*/,2,
																		// "�˻�");
		// ����ά����
		m_btnModify = getBtnTree().getButton(IButtonConstRc.BTN_BILL_EDIT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
																			// "�޸�"*/,
																			// m_lanResTool.getStrByID("40040301","UPP40040301-000235")/*@res
																			// "�޸ĵ�����"*/,
																			// 2,"�޸�");
																			// /*-=notranslate=-*/
		m_btnSave = getBtnTree().getButton(IButtonConstRc.BTN_SAVE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000001")/*@res
																	// "����"*/,
																	// m_lanResTool.getStrByID("40040301","UPP40040301-000237")/*@res
																	// "�����޸Ľ��"*/,
																	// 2,"����");
																	// /*-=notranslate=-*/
		m_btnCancel = getBtnTree().getButton(IButtonConstRc.BTN_BILL_CANCEL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000008")/*@res
																				// "ȡ��"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000008")/*@res
																				// "ȡ��"*/,
																				// 2,"ȡ��");
																				// /*-=notranslate=-*/
		m_btnDiscard = getBtnTree().getButton(IButtonConstRc.BTN_BILL_DELETE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
																				// "����"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
																				// "����"*/,
																				// 2,"����");
																				// /*-=notranslate=-*/
		m_btnSendAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_EXECUTE_AUDIT);// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000017")/*
													// @res "����"
													// */,m_lanResTool.getStrByID("40040101","UPP40040101-000451")/*
													// @res "���󵽻���" */, 2,
													// "����");
													// /*-=notranslate=-*/
		m_btnMaintains = getBtnTree().getButton(IButtonConstRc.BTN_BILL);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
																			// "����ά��"*/,
																			// m_lanResTool.getStrByID("40040301","UPT40040301-000033")/*@res
																			// "����ά��"*/,2,"����ά��");
		// �в�����
		m_btnDelLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_DELETE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000013")/*@res
																				// "ɾ��"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000013")/*@res
																				// "ɾ��"*/,
																				// 2,"ɾ��");
																				// /*-=notranslate=-*/
		m_btnCpyLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_COPY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000014")/*@res
																			// "������"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																			// "������"*/,
																			// 2,"������");
																			// /*-=notranslate=-*/
		m_btnPstLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																				// "ճ����"*/,
																				// m_lanResTool.getStrByID("common","UC001-0000015")/*@res
																				// "ճ����"*/,
																				// 2,"ճ����");
																				// /*-=notranslate=-*/
		m_btnLines = getBtnTree().getButton(IButtonConstRc.BTN_LINE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000011")/*@res
																		// "�в���"*/,m_lanResTool.getStrByID("common","UC001-0000011")/*@res
																		// "�в���"*/,2,
																		// "�в���");
		// ���������
		m_btnQuery = getBtnTree().getButton(IButtonConstRc.BTN_QUERY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
																		// "��ѯ"*/,
																		// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
																		// "��ѯ"*/,
																		// 2,"��ѯ");
																		// /*-=notranslate=-*/
		m_btnFirst = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_TOP);// ButtonObject(m_lanResTool.getStrByID("common","UCH031")/*@res
																			// "��ҳ"*/,
																			// m_lanResTool.getStrByID("common","UCH031")/*@res
																			// "��ҳ"*/,
																			// 2,"��ҳ");
																			// /*-=notranslate=-*/
		m_btnPrev = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_PREVIOUS);// ButtonObject(m_lanResTool.getStrByID("common","UCH033")/*@res
																				// "��һҳ"*/,
																				// m_lanResTool.getStrByID("common","UCH033")/*@res
																				// "��һҳ"*/,
																				// 2,"��һҳ");
																				// /*-=notranslate=-*/
		m_btnNext = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_NEXT);// ButtonObject(m_lanResTool.getStrByID("common","UCH034")/*@res
																			// "��һҳ"*/,
																			// m_lanResTool.getStrByID("common","UCH034")/*@res
																			// "��һҳ"*/,
																			// 2,"��һҳ");
																			// /*-=notranslate=-*/
		m_btnLast = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_BOTTOM);// ButtonObject(m_lanResTool.getStrByID("common","UCH032")/*@res
																				// "ĩҳ"*/,
																				// m_lanResTool.getStrByID("common","UCH032")/*@res
																				// "ĩҳ"*/,
																				// 2,"ĩҳ");
																				// /*-=notranslate=-*/
		m_btnLocate = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_LOCATE);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
																				// "��λ"*/,
																				// m_lanResTool.getStrByID("40040301","UPT40040301-000041")/*@res
																				// "��λ"*/,
																				// 2,"��λ");
																				// /*-=notranslate=-*/
		m_btnRefresh = getBtnTree()
				.getButton(IButtonConstRc.BTN_BROWSE_REFRESH);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
																// "ˢ��"*/,
																// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
																// "ˢ��"*/,
																// 2,"ˢ��");
																// /*-=notranslate=-*/
		m_btnBrowses = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000021")/*@res
																			// "���"*/,m_lanResTool.getStrByID("common","UC001-0000021")/*@res
																			// "���"*/,2,
																			// "���");
		// �л�
		m_btnList = getBtnTree().getButton(IButtonConstRc.BTN_SWITCH);// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
																		// "�б���ʾ"*/,
																		// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res
																		// "�б���ʾ"*/,
																		// 2,"�л�");
																		// /*-=notranslate=-*/
		// ִ����(��������������Ϣ���Ĺ���)
		m_btnActions = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
																			// "ִ��"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
																			// "ִ��"*/,
																			// 0,"ִ��");
																			// /*-=notranslate=-*/
		// ������
		m_btnCombin = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_DISTINCT);// ButtonObject(m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
																				// "�ϲ���ʾ"*/,
																				// m_lanResTool.getStrByID("4004020201","UPT4004020201-000084")/*@res
																				// "�ϲ���ʾ"*/,
																				// 2,"�ϲ���ʾ");
																				// /*-=notranslate=-*/
		m_btnPrints = getBtnTree().getButton(IButtonConstRc.BTN_PRINT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																		// "��ӡ"*/,
																		// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																		// "��ӡ"*/,
																		// 2,"��ӡ");
																		// /*-=notranslate=-*/
		m_btnPrint = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PRINT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																			// "��ӡ"*/,
																			// m_lanResTool.getStrByID("common","UC001-0000007")/*@res
																			// "��ӡ"*/,
																			// 2,"��ӡ");
																			// /*-=notranslate=-*/
		m_btnImportBill = getBtnTree().getButton("EXCEL����");//EXCEL����
		
		m_btnImportXml = getBtnTree().getButton("XML����");//XML���� yqq 2016-11-02 ����
		
		m_btnPrintPreview = getBtnTree().getButton(
				IButtonConstRc.BTN_PRINT_PREVIEW);// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/, 2,"Ԥ��");
													// /*-=notranslate=-*/
		m_btnUsable = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_ONHAND);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
														// "������ѯ"*/,
														// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
														// "������ѯ"*/, 2,"������ѯ");
														// /*-=notranslate=-*/
		m_btnQueryBOM = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_SUITE);// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
														// "���׼�"*/,
														// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
														// "���׼�"*/, 2,"���׼�");
														// /*-=notranslate=-*/
		m_btnQuickReceive = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_FUNC_QUICK_RECEIVE);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
																// "�����ջ�"*/,
																// m_lanResTool.getStrByID("40040301","UPT40040301-000031")/*@res
																// "�����ջ�"*/,
																// 2,"�����ջ�");
																// /*-=notranslate=-*/
		m_btnDocument = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_FUNC_DOCUMENT);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
															// "�ĵ�����"*/,
															// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
															// "�ĵ�����"*/,
															// 2,"�ĵ�����");
															// /*-=notranslate=-*/
		m_btnLookSrcBill = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_RELATED);// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
															// "����"*/,
															// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res
															// "����"*/, 2,"����");
															// /*-=notranslate=-*/
		m_btnQueryForAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_ASSIST_QUERY_WORKFLOW);// ButtonObject(m_lanResTool.getStrByID("40040101","UPT40040101-000032")/*
															// @res "״̬��ѯ"
															// */,m_lanResTool.getStrByID("40040101","UPP40040101-000450")/*
															// @res "����״̬��ѯ" */,
															// 2, "״̬��ѯ");
															// /*-=notranslate=-*/
		m_btnOthers = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
																				// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
																				// "����"*/,2,
																				// "����");
		/*
		 * { m_btnUsable, m_btnQueryBOM,m_btnQuickReceive, m_btnDocument,
		 * m_btnLookSrcBill, m_btnQueryForAudit, m_btnCombin, m_btnPrint,
		 * m_btnPrintPreview};
		 */

		/* ��Ƭ��ť�˵� */

		m_aryArrCardButtons = getBtnTree().getButtonArray();

		/* �б���ť���� */
		
		
		m_btnSelectAll = getBtnTree().getButton(
				IButtonConstRc.BTN_BROWSE_SELECT_ALL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000041")/*@res
														// "ȫѡ"*/,
														// m_lanResTool.getStrByID("40040301","UPP40040301-000238")/*@res
														// "ȫ��ѡ��"*/, 2,"ȫѡ");
														// /*-=notranslate=-*/
		m_btnSelectNo = getBtnTree().getButton(
				IButtonConstRc.BTN_BROWSE_SELECT_NONE);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000042")/*@res
														// "ȫ��"*/,
														// m_lanResTool.getStrByID("40040301","UPP40040301-000233")/*@res
														// "ȫ��ȡ��"*/, 2,"ȫ��");
														// /*-=notranslate=-*/
		m_btnModifyList = m_btnModify;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000045")/*@res
										// "�޸�"*/,
										// m_lanResTool.getStrByID("40040301","UPPSCMCommon-000291")/*@res
										// "�޸ĵ���"*/, 2, "�б��޸�");
										// /*-=notranslate=-*/
		m_btnDiscardList = m_btnDiscard;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000005")/*@res
										// "����"*/,
										// m_lanResTool.getStrByID("common","UC001-0000005")/*@res
										// "����"*/, 2,"�б�����");
										// /*-=notranslate=-*/
		m_btnQueryList = m_btnQuery;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000006")/*@res
									// "��ѯ"*/,
									// m_lanResTool.getStrByID("common","UC001-0000006")/*@res
									// "��ѯ"*/, 2,"�б���ѯ"); /*-=notranslate=-*/
		m_btnCard = m_btnList;// ButtonObject(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
								// "��Ƭ��ʾ"*/,
								// m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000463")/*@res
								// "��Ƭ��ʾ"*/, 2,"�б��л�"); /*-=notranslate=-*/
		m_btnEndCreate = getBtnTree().getButton(IButtonConstRc.BTN_REF_CANCEL);// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000043")/*@res
																				// "����ת��"*/,
																				// m_lanResTool.getStrByID("40040301","UPP40040301-000234")/*@res
																				// "��������������"*/,
																				// 2,"����ת��");
																				// /*-=notranslate=-*/
		m_btnRefreshList = m_btnRefresh;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000009")/*@res
										// "ˢ��"*/,
										// m_lanResTool.getStrByID("common","UC001-0000009")/*@res
										// "ˢ��"*/, 2,"ˢ��"); /*-=notranslate=-*/

		// ������
		m_btnUsableList = m_btnUsable;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
										// "������ѯ"*/,
										// m_lanResTool.getStrByID("40040301","UPT40040301-000046")/*@res
										// "������ѯ"*/, 2,"�б�������ѯ");
										// /*-=notranslate=-*/
		m_btnDocumentList = m_btnDocument;// ButtonObject(m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
											// "�ĵ�����"*/,
											// m_lanResTool.getStrByID("40040301","UPT40040301-000044")/*@res
											// "�ĵ�����"*/, 2,"�б��ĵ�����");
											// /*-=notranslate=-*/
		m_btnQueryBOMList = m_btnQueryBOM;// ButtonObject(m_lanResTool.getStrByID("common","UC000-0001953")/*@res
											// "���׼�"*/,
											// m_lanResTool.getStrByID("common","UC000-0001953")/*@res
											// "���׼�"*/, 2,"�б����׼�");
											// /*-=notranslate=-*/
		m_btnPrintPreviewList = m_btnPrintPreview;// ButtonObject(m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/,
													// m_lanResTool.getStrByID("common","4004COMMON000000056")/*@res
													// "Ԥ��"*/, 2,"�б���ӡԤ��");
													// /*-=notranslate=-*/
		m_btnPrintList = m_btnPrint;// ButtonObject(m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
									// "��ӡ����"*/,
									// m_lanResTool.getStrByID("40040301","UPP40040301-000236")/*@res
									// "��ӡ����"*/, 2,"�б���ӡ"); /*-=notranslate=-*/
		m_btnOthersList = m_btnOthers;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
										// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
										// "����"*/,2, "����"); /*-=notranslate=-*/

		/* �б���ť�� */
		m_aryArrListButtons = m_aryArrCardButtons;

		/* ��Ϣ���İ�ť�� */
		m_btnAudit = getBtnTree().getButton(IButtonConstRc.BTN_AUDIT);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000027")/*
																		// @res
																		// "����"
																		// */,m_lanResTool.getStrByID("common","UC001-0000027")/*
																		// @res
																		// "����"
																		// */,
																		// 2,
																		// "����");
																		// /*-=notranslate=-*/
		m_btnAudit.setTag("APPROVE");
		m_btnUnAudit = getBtnTree().getButton(
				IButtonConstRc.BTN_EXECUTE_AUDIT_CANCEL);// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000028")/*
															// @res "����"
															// */,m_lanResTool.getStrByID("40040401","UPP40040401-000149")/*
															// @res "ִ���������" */,
															// 5, "����");
															// /*-=notranslate=-*/
		m_btnUnAudit.setTag("UNAPPROVE");
		m_btnOthersMsgCenter = m_btnOthers;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "����"*/,m_lanResTool.getStrByID("common","UC001-0000036")/*@res
											// "����"*/,2, "����");
		m_btnActionMsgCenter = m_btnActions;// ButtonObject(m_lanResTool.getStrByID("common","UC001-0000026")/*@res
											// "ִ��"*/,
											// m_lanResTool.getStrByID("common","UC001-0000026")/*@res
											// "ִ��"*/, 0,"ִ��");
											// /*-=notranslate=-*/
		m_aryMsgCenter = new ButtonObject[] { m_btnActionMsgCenter,
				m_btnOthersMsgCenter };
	}

	/**
	 * ��ʼ����ť �������ڣ�(01-2-26 13:29:17)
	 */
	private void setButtonsInit() {
		// ���⹦��
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
									 * @res "�б���ʾ"
									 */);
		//
		for (int i = 0; i < m_aryArrCardButtons.length; i++) {
			if (PuTool.isExist(getExtendBtns(), m_aryArrCardButtons[i])) {
				continue;
			}
			m_aryArrCardButtons[i].setEnabled(false);
		}
		// ����ת��
		m_btnEndCreate.setVisible(false);
		// ��������
		getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC).setEnabled(true);

		getBtnTree().getButton(IButtonConstRc.BTN_PRINT).setEnabled(true);
		m_btnActions.setEnabled(true);
		m_btnAudit.setEnabled(false);
		m_btnUnAudit.setEnabled(false);
		// ����ά��
		m_btnMaintains.setEnabled(true);
		m_btnModify.setEnabled(false);
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnDiscard.setEnabled(false);
		m_btnSelectAll.setEnabled(false);
		m_btnSendAudit.setEnabled(false);
		// ���
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML����  yqq 2016-11-02 ����
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
		// ����
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
	 * ���ܣ���ʼ������ ������ ���أ� ���⣺ ���ڣ�(2002-8-21 10:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initDecimal() {
		// ��������

		// ����
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
		// ������
		if (m_arrBillPanel.getBodyItem("nassistnum") != null)
			m_arrBillPanel.getBodyItem("nassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// ������
		if (m_arrBillPanel.getBodyItem("npresentassistnum") != null)
			m_arrBillPanel.getBodyItem("npresentassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// ������
		if (m_arrBillPanel.getBodyItem("convertrate") != null)
			m_arrBillPanel.getBodyItem("convertrate").setDecimalDigits(
					nConvertDecimal);
		// ����
		if (m_arrBillPanel.getBodyItem("nprice") != null)
			m_arrBillPanel.getBodyItem("nprice")
					.setDecimalDigits(nPriceDecimal);
		// ���
		if (m_arrBillPanel.getBodyItem("nmoney") != null) {
			m_arrBillPanel.getBodyItem("nmoney").setDecimalDigits(
					nNmoneyDecimal);
		}

	}

	/**
	 * ��ʼ����ť��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-19 ����09:26:36
	 */
	private void initButtons() {

		// V51�ع���Ҫ��ƥ��,��ťʵ��������
		createBtnInstances();

		// ҵ�����Ͱ�ť���Ӳ˵�
		PfUtilClient.retBusinessBtn(m_btnBusiTypes, getCorpPrimaryKey(),
				BillTypeConst.PO_ARRIVE);

		// ҵ�����Ͱ�ť�򹳴���
		PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds,
				BillTypeConst.PO_ARRIVE, getCorpPrimaryKey());

		// ������չ��ť
		addExtendBtns();

		// ���ؿ�Ƭ��ť
		setButtons(m_btnTree.getButtonArray());
	}

	/**
	 * ������չ��ť��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-19 ����09:23:34
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
	 * ��������:�ڵ��ʼ��
	 * 
	 * ��ȡҵ�����͡����ص���ģ�塢��ʼ����ť״̬
	 */
	private void initialize() {

		// ��ʼ������
		initPara();

		// ��ʼ����ť
		initButtons();

		// ���ð�ť״̬
		setButtonsState();

		// ��ʾ����
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), BorderLayout.CENTER);

		// ��ʼ�����屸ע
		initVmemoBody();

		// ��ʼ������
		initDecimal();

		// ��ʼ��������״̬
		getBillCardPanel().setEnabled(false);
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	/**
	 * ���ܣ���ʼ�����屸ע
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
	 * ���ܣ���ʼ������ ������ ���أ� ���⣺ ���ڣ�(2002-8-21 10:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initListDecimal() {
		// ��������

		// ����
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
		// ������
		if (m_arrListPanel.getBodyItem("nassistnum") != null)
			m_arrListPanel.getBodyItem("nassistnum").setDecimalDigits(
					nAssistUnitDecimal);
		// ������
		if (m_arrListPanel.getBodyItem("convertrate") != null)
			m_arrListPanel.getBodyItem("convertrate").setDecimalDigits(
					nConvertDecimal);
		// ����
		if (m_arrListPanel.getBodyItem("nprice") != null)
			m_arrListPanel.getBodyItem("nprice")
					.setDecimalDigits(nPriceDecimal);
		// ���
		if (m_arrListPanel.getBodyItem("nmoney") != null)
			m_arrListPanel.getBodyItem("nmoney").setDecimalDigits(
					nNmoneyDecimal);

	}

	/**
	 * ��������:��ʼ������
	 */
	public void initPara() {

		// ���ػ�λ���书��
		// m_btnAllotCarg.setVisible(false);
		// m_btnAllotCarg.setEnabled(false);
		// ��ʼ������
		/**
		 * BD502 ������ BD503 ������ BD501 ������ BD505 �ɹ�����
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
			// ��ʼ�����ȣ����������ۣ�
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

			// �Զ�����Զ�̵���������
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

			// ��̨һ�ε���
			Object[] oParaValue = nc.ui.scm.service.LocalCallService
					.callService(scDisc);
			if (oParaValue != null && oParaValue.length == scDisc.length) {
				// ���������ݾ���
				int[] iDigits = (int[]) oParaValue[0];
				if (iDigits != null && iDigits.length == 4) {
					nAssistUnitDecimal = iDigits[0];
					nConvertDecimal = iDigits[1];
					nMeasDecimal = iDigits[2];
					nPriceDecimal = iDigits[3];
				}
				// ���ҽ���
				nNmoneyDecimal = ((Integer) oParaValue[1]).intValue();

				// �Զ�����Ԥ����
				// nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] {
				// oParaValue[2], oParaValue[3] });

				String s = strPara0;
				if (s != null)
					m_bSaveMaker = (new UFBoolean(s)).booleanValue();
			}
			// ��������ģ������
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
	 * ���ܣ���ʼ������ ������ ���أ� ���⣺ ���ڣ�(2002-8-21 10:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initRefPane(BillData bd) {
		// ������������������ͷ����������������

		// ҵ�����ͳ�ʼ��
		if (bd.getHeadItem("cbiztype") != null) {
			refBusi = (UIRefPane) bd.getHeadItem("cbiztype").getComponent();
			refBusi.setEnabled(false);
		}
		// ����
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
		// ҵ��Ա
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
		// ��ͷ�˻�����
		if (bd.getHeadItem("vbackreasonh") != null) {
			UIRefPane refPaneReason = (UIRefPane) bd
					.getHeadItem("vbackreasonh").getComponent();
			refPaneReason.setRefModel(new BackReasonRefModel());
			refPaneReason.setAutoCheck(false);
		}
		// ��ͷ��ע
		if (bd.getHeadItem("vmemo") != null) {
			UIRefPane refVmemo = (UIRefPane) bd.getHeadItem("vmemo")
					.getComponent();
			refVmemo.setRefNodeName("����ժҪ");
			refVmemo.getRefModel().setRefCodeField(
					refVmemo.getRefModel().getRefNameField());
			refVmemo.getRefModel().setBlurFields(
					new String[] { refVmemo.getRefModel().getRefNameField() });
			refVmemo.setAutoCheck(false);
		}

		// �����֯
		if (bd.getHeadItem("cstoreorganization") != null) {
			UIRefPane refPane = (UIRefPane) bd
					.getHeadItem("cstoreorganization").getComponent();
			refPane.getRefModel()
					.addWherePart(
							" and (bd_calbody.property = 0 or bd_calbody.property = 1) ");
		}

		// �������������������壭��������������

		// ������
		/*
		 * if (bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") !=
		 * null) { FreeItemRefPane m_firpFreeItemRefPane = new
		 * FreeItemRefPane();
		 * m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0"
		 * ).getLength()); //�Ӽ�����
		 * m_firpFreeItemRefPane.getUIButton().addActionListener(this);
		 * bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
		 * 
		 * }
		 */
		// �������κŲ���
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

		// //���屸ע����
		// if (bd.getBodyItem("vmemo") != null) {
		// UIRefPane nRefPanel = (UIRefPane)
		// bd.getBodyItem("vmemo").getComponent();
		// nRefPanel.setTable(bd.getBillTable());
		// nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
		// nRefPanel.getRefModel().setBlurFields(new String[] {
		// nRefPanel.getRefModel().getRefNameField()});
		// nRefPanel.setAutoCheck(false);
		// }

		// ��Ŀ����
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
		// �۸�Ǹ�
		if (bd.getBodyItem("nprice") != null) {
			UIRefPane refPrice = (UIRefPane) bd.getBodyItem("nprice")
					.getComponent();
			refPrice.setMinValue(0);
			refPrice.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// �����ʷǸ�
		if (bd.getBodyItem("convertrate") != null) {
			UIRefPane refConvert = (UIRefPane) bd.getBodyItem("convertrate")
					.getComponent();
			refConvert.setMinValue(0);
			refConvert.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// �����������Ǹ�
		if (bd.getBodyItem("ivalidday") != null) {
			UIRefPane refVld = (UIRefPane) bd.getBodyItem("ivalidday")
					.getComponent();
			refVld.setMinValue(0);
			refVld.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}

		// �����˻�����
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
		// �����˻����ɴ���
		// if (bd.getBodyItem("vbackreasonb") != null) {
		// UIRefPane refPanel = (UIRefPane)
		// bd.getBodyItem("vbackreasonb").getComponent();
		// refPanel.setTable(bd.getBillTable());
		// refPanel.getRefModel().setRefCodeField(refPanel.getRefModel().getRefNameField());
		// refPanel.getRefModel().setBlurFields(new String[] {
		// refPanel.getRefModel().getRefNameField()});
		// refPanel.setAutoCheck(false);
		// }

		// �ջ��ֿ�
		if (bd.getBodyItem("cwarehousename") != null) {
			UIRefPane refStore = (UIRefPane) bd.getBodyItem("cwarehousename")
					.getComponent();
			refStore.setRefModel(new WarehouseRefModel(getCorpPrimaryKey()));
			refStore.getRefModel()
					.addWherePart(
							" and UPPER(bd_stordoc.gubflag) <> 'Y' and UPPER(bd_stordoc.sealflag) <> 'Y' ");
		}
		// ��λ�Ƿ��
		if (bd.getBodyItem("cstorename") != null) {
			UIRefPane refCarg = (UIRefPane) bd.getBodyItem("cstorename")
					.getComponent();
			refCarg.getRefModel().addWherePart(
					"and UPPER(bd_cargdoc.sealflag) <> 'Y' ");
		}
		// ����������
		if (bd.getBodyItem("cassistunitname") != null) {
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setIsCustomDefined(true);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setRefModel(new OtherRefModel("��������λ"));
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setReturnCode(false);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setRefInputType(1);
			((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()))
					.setCacheEnabled(false);
		}

	}

	/**
	 * �Ƿ��˻�����
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
	 * ���ܣ�(�������޸ĵı���)���ݵ���������ϵ�жϵ��������Ƿ���� ������ ���أ��㷨(�������޸ĵı���): naccumchecknum ==
	 * null(0) && (nelignum + nnotelignum != null(0)) ���� true ���⣺ ���ڣ�(2002-9-12
	 * 13:03:45) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private boolean isCheckFree(BillEditEvent e) {
		// ��ǰ���ݱ����ϣ����key = ��ID��
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
	 * �Ƿ�ֻ���ڵ������˻�һ�ֵ���(�������ڽ���)
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
	 * @���ܣ����ر�ͷ��������
	 */
	private void loadBDData() {
		/* �������� */
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
		/* ��Ӧ�� */
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
		/* ���� */
		strVarValue = getBillCardPanel().getHeadItem("cdeptid").getValue();
		refpnl[1] = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
				.getComponent();
		strValue = refpnl[1].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[1].put("cdeptid", strVarValue);
		}
		/* ҵ��Ա */
		strVarValue = getBillCardPanel().getHeadItem("cemployeeid").getValue();
		refpnl[2] = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
				.getComponent();
		strValue = refpnl[2].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[2].put("cemployeeid", strVarValue);
		}
		/* ���˷�ʽ */
		strVarValue = getBillCardPanel().getHeadItem("ctransmodeid").getValue();
		refpnl[3] = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid")
				.getComponent();
		strValue = refpnl[3].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[3].put("ctransmodeid", strVarValue);
		}
		/* �ջ��� */
		strVarValue = getBillCardPanel().getHeadItem("creceivepsn").getValue();
		refpnl[4] = (UIRefPane) getBillCardPanel().getHeadItem("creceivepsn")
				.getComponent();
		strValue = refpnl[4].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[4].put("creceivepsn", strVarValue);
		}
		/* �����֯ */
		strVarValue = getBillCardPanel().getHeadItem("cstoreorganization")
				.getValue();
		refpnl[5] = (UIRefPane) getBillCardPanel().getHeadItem(
				"cstoreorganization").getComponent();
		strValue = refpnl[5].getUITextField().getText();
		if (strVarValue != null
				&& (strValue == null || strValue.trim().equals(""))) {
			hData[5].put("cstoreorganization", strVarValue);
		}
		/* ҵ������ */
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
			if (getStateStr().equals("ת���б�")) {
				setM_strState("ת���޸�");
				onCardNew();
			} else {
				// ���û�е����壬����Ϊ����������
				ArriveorderItemVO[] items = (ArriveorderItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0)
					return;
				//
				isFrmList = true;
				setM_strState("�������");
				onCard();
			}
		}
	}

	/**
	 * ����:ִ������
	 */
	private void onAudit(ButtonObject bo) {
		try {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000118")/* @res "��������..." */);
			ArriveorderVO vo = getCacheVOs()[getDispIndex()];
			// ���������ˡ���������
			if (vo == null || vo.getParentVO() == null)
				return;
			// V31SP1:�����������ڲ���С�ڵ�����������
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
			if (getParentCorpCode().equals("10395")&&IsPO(vo.getParentVO().getAttributeValue("cbiztype").toString())&&!(getCorpPrimaryKey().equals("1078")||getCorpPrimaryKey().equals("1108")))//�Ƹǲ���ֱ��������ⵥ2014-11-28
			// �����Ƿ���Ҫ���飬û���м���Ĵ��
			{
				if(!IsNotLockAccount(((ArriveorderHeaderVO)vo.getParentVO()).getDauditdate().toString(),((ArriveorderHeaderVO)vo.getParentVO()).getCstoreorganization()))
				{
					MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", "����ڼ䴦�ڹ����ڼ䣬�������!Audit period is during the Closing��Not warehousing!");
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
						MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", Transformations.getLstrFromMuiStr("����@Odd numbers&"
								+ vo.getHeadVO().getVarrordercode()
								+ "&��δ����Ĵ���������������!@Inspection inventory��Please test after audit!"));
						showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000121"));
						return;
					}
					
				}
				//add by zwx 2015-8-27 
				n++;
				//end by zwx 

			}

			
			/* ���� */
			PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive,
					getClientEnvironment().getDate().toString(),
					new ArriveorderVO[] { vo }, null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000119")/* @res "����δ�ɹ�" */);
				return;
			}
			/* �����ɹ���ˢ�� */
			refreshVoFieldsByKey(vo, vo.getParentVO().getPrimaryKey());
			UFDate auditdate= ((ArriveorderHeaderVO)vo.getParentVO()).getDauditdate();
			// ��ʽ�������ε��ݲɹ����
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
				MessageDialog.showErrorDlg(this, "��˵�����Audit to manifest", e.getMessage());
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
							newVo, null);// �ɹ���ⵥǩ��״̬
				} catch (Exception e) {
					SCMEnv.out(e);
					MessageDialog.showErrorDlg(this, "��˵�����Audit to manifest", e.getMessage());
					showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000121"));
				}
			}
		}
			else 
			{
				
				if (getParentCorpCode().equals("10395"))
					// �����Ƿ���Ҫ���飬û���м���Ĵ��
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
								MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", Transformations.getLstrFromMuiStr("����@Odd numbers&"
										+ vo.getHeadVO().getVarrordercode()
										+ "&��δ����Ĵ���������������!@Inspection inventory��Please test after audit!"));
								showHintMessage(m_lanResTool.getStrByID("40040301",
								"UPP40040301-000121"));
								return;
							}
							
						}

					}
					}
				/* ���� */
				PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive,
						getClientEnvironment().getDate().toString(),
						new ArriveorderVO[] { vo }, null);
				if (!PfUtilClient.isSuccess()) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000119")/* @res "����δ�ɹ�" */);
					return;
				}
			}
			
			//
			getCacheVOs()[getDispIndex()] = vo;
			/* ˢ�°�ť״̬ */
			setButtonsState();
			/* ���ص��� */
			try {
				loadDataToCard();
			} catch (Exception e) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000120")/*
											 * @res
											 * "�����ɹ�,�����ص���ʱ�����쳣,��ˢ�½����ٽ�����������"
											 */);
			}
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000236")/* @res "�����ɹ�" */);
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000121")/* @res "�����쳣,����ʧ��" */);
			SCMEnv.out(e);
			if (e instanceof java.rmi.RemoteException
					|| e instanceof BusinessException
					|| e instanceof PFBusinessException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040301", "UPP40040301-000099")/* @res "����" */, e
						.getMessage());
			}
		}
	}

	/**
	 * @���ܣ��������˻�-�ɹ�
	 */
	private void onBackPo() {
		/* ���ò�ѯ������ */
		getBackRefUIPo().setQueyDlg(getBackQueDlgPo());
		/* ����onQuery(),���������ݵ����ս��� */
		int iType = getBackRefUIPo().onQuery();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* ��ʾ���ս��� */
		iType = getBackRefUIPo().showModal();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* �������� */
		if (getBackRefUIPo().getRetVos() == null
				|| getBackRefUIPo().getRetVos().length <= 0)
			return;
		onExitFrmOrd((ArriveorderVO[]) getBackRefUIPo().getRetVos());
	}

	/**
	 * @���ܣ��������˻�-ί��
	 */
	private void onBackSc() {
		/* ���ò�ѯ������ */
		getBackRefUISc().setQueyDlg(getBackQueDlgSc());
		/* ����onQuery(),���������ݵ����ս��� */
		int iType = getBackRefUISc().onQuery();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* ��ʾ���ս��� */
		iType = getBackRefUISc().showModal();
		if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
			return;
		}
		/* �������� */
		if (getBackRefUISc().getRetVos() == null
				|| getBackRefUISc().getRetVos().length <= 0)
			return;
		onExitFrmOrd((ArriveorderVO[]) getBackRefUISc().getRetVos());
	}

	/**
	 * @���ܣ�ѡȡһ��ҵ�����ͺ���
	 */
	private void onBusi(ButtonObject bo) {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000122")/* @res "���ڳ�ʼ��ҵ������:" */
				+ bo.getHint() + "...");
		/* ���¼���ҵ�����Ͱ�ť�� */
		PfUtilClient.retAddBtn(m_btnAdds, getCorpPrimaryKey(),
				nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, bo);
		setButtons(m_aryArrCardButtons);
		m_btnAdds.setEnabled(true);
		updateButton(m_btnAdds);
		/* ˢ�´��� */
		updateButtons();
		updateButtonsAll();
		updateUI();
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000123")/* @res "��ǰ����ҵ������:" */+ bo.getHint());
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
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
	 * �������������鹦��ʵ��(������������)��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-21 ����07:20:31
	 */
	private void onCheck() {
		if (getCacheVOs() == null || getCacheVOs().length == 0
				|| getCacheVOs()[getDispIndex()] == null) {
			return;
		}
		Hashtable<String, UFBoolean> hStorByChk = new Hashtable<String, UFBoolean>();

		// ������������ʱ��ѯ�Ƿ��������������������
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
							"UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage());
		}
		String strErrInfo = getCacheVOs()[getDispIndex()].judgeCanCheck(
				m_bQcEnabled, hStorByChk);
		if (strErrInfo != null) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "��ʾ" */, strErrInfo);
			return;
		}

		// ��֯��д����

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
			// ��д�õ�����ID��������ID
			carriveorder_bid = voCurr.getBodyVo()[i].getCarriveorder_bid();
			carriveorderid = voCurr.getBodyVo()[i].getCarriveorderid();
			carriveorder_bts = voCurr.getBodyVo()[i].getTs();
			carriveorderts = voCurr.getBodyVo()[i].getTsh();
			// ��֯��д����
			UFDouble[] rewriteNum = new UFDouble[3];
			rewriteNum[0] = new UFDouble(0.0);
			rewriteNum[1] = new UFDouble(0.0);
			// �ۼƼ�������
			rewriteNum[2] = voCurr.getBodyVo()[i].getNarrvnum();
			aryRewriteNum.add(rewriteNum);
			vStrLineId.addElement(carriveorder_bid);
			vStrLineTs.addElement(carriveorder_bts);
			vStrHeadId.addElement(carriveorderid);
			vStrHeadTs.addElement(carriveorderts);
		}

		// ��̨���ò���
		ArrayList listPara = new ArrayList();
		/*
		 * ����˵���� 0-----�ʼ��Ƿ����� 1..4--�����ʼ쵥������ArriveorderBO_Client.crtQcBills
		 * 5-----�Ƿ��д�ۼ��ʼ�����
		 * 6..15-��д�ۼ��ʼ�����������ArriveorderBO_Client.rewriteNaccumchecknumMy
		 */
		listPara.add(0, new UFBoolean(m_bQcEnabled));
		// ��½����
		UFDate dBusinessDate = getClientEnvironment().getDate();
		// ������ʱ��
		UFDateTime dtServerDateTime = ClientEnvironment.getServerTime();
		// Ŀ�����
		UFDateTime dtDateTime = new UFDateTime(dBusinessDate, new UFTime(
				dtServerDateTime.getTime()));
		// ��������ͬһ�����µĲ������󣬽��˷����Ƶ���̨
		listPara.add(1, null);
		listPara.add(2, voCurr.getBodyVo());
		listPara.add(3, getOperatorId());
		listPara.add(4, dtDateTime);
		// �Ƿ��д�ۼ��ʼ�����
		listPara.add(5, UFBoolean.TRUE);
		// ��֯��TS����
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
		listPara.add(7, UFBoolean.TRUE);// ������������(������ʱ�����ܲ�����)
		listPara.add(8, saLineIds);
		listPara.add(9, saHeadIds);
		listPara.add(10, aryRewriteNum);
		listPara.add(11, getOperatorId());
		listPara.add(12, saLineTss);
		listPara.add(13, saHeadTss);
		listPara.add(14, new UFBoolean(voCurr.isCheckOver()));// �Ƿ�Ϊ�ظ�����(�����������)
		listPara.add(15, UFBoolean.FALSE);// �ع��󣬽�����ǰ��̨�����ϲ�Ϊһ�Σ�ʱ���û�б仯

		try {
			// �ع�����������ͬһ�������µĲ�������
			String sRet = ArriveorderHelper.crtQcAndRewriteNum(listPara);
			//
			if (sRet != null) {
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303",
								"UPP40040303-000013")/*
													 * @res "���α���ʧ�ܣ�"
													 */
								+ sRet);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000060")/* @res "����ʧ��" */);
				return;
			}
			//
			if (sRet == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000058")/* @res "����ɹ�" */);
			}
			// ˢ�´���(Ŀǰ����ֱ�ӵ���ˢ�£��д��Ż�)
			onRefresh();
			//
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000058")/* @res "����ɹ�" */);
		} catch (BusinessException b) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, b
					.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000060")/* @res "����ʧ��" */);
		} catch (Exception b) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, b
					.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000060")/* @res "����ʧ��" */);
		}
	}

	/**
	 * ��Ƭ��ť�¼���Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-20 ����04:12:37
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
																 * @res "����"
																 */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000124")/*
														 * @res "ί�ⶩ��ģ��û�����ã�"
														 */);
					return;
				}
			} 
			//add by wbp 2017-6-1 �����ϵ������̵ĵ��ݿ�������
			
			else if(bo.getName().equals("���Ƶ���")){
				// �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
				getBillCardPanel().setEnabled(true);
				this.m_btnLines.setEnabled(true);
				this.m_btnLines.setEnabled(true);
				this.m_btnDelLine.setEnabled(true);
				this.m_btnCpyLine.setEnabled(true);
				this.m_btnPstLine.setEnabled(true);
				this.m_btnAdds.setEnabled(false);
				this.m_btnSave.setEnabled(true);
				this.m_btnCancel.setEnabled(true);
				this.m_btnQuery.setEnabled(false);  //��ѯ
				this.m_btnBrowses.setEnabled(false);  //���
				getBillCardPanel().addNew();
				getBillCardPanel().setEnabled(true);
				return;
			}
			//end add by wbp
			
			else if (!strBillType.equals("21")) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000125")/*
													 * @res "������ֻ���ɲɹ�������ί�ⶩ�����ɣ�"
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
			// ��ӡ�������ӡ����
			onCardPrint();
		} else if (bo == m_btnCombin) {
			// �ϲ���ʾ��ӡ
			onCombin();
		} else if (bo == m_btnPrintPreview) {
			// ��ӡԤ���������ӡ����
			onCardPrintPreview();
		} else if (bo == m_btnList) {
			onList();
		} else if (bo == m_btnModify) {
			onModify();
			// �ù�굽��ͷ��һ���ɱ༭��Ŀ
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
			onImportXml();     //XML����  yqq 2016-11-02 ����
		}
		/* ����V5֧��������****************************** */
		else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if (bo == m_btnAudit) {
			onAudit(bo);
		} else if (bo == m_btnUnAudit) {
			onUnAudit(bo);
		} else if (bo == m_btnQueryForAudit) {
			onQueryForAudit();
			/* ����V5֧��������***************************** */
		}
		// ֧�ֲ�ҵ��������չ
		else if (PuTool.isExist(getExtendBtns(), bo)) {
			onExtendBtnsClick(bo);
		}
	}
	
	/*
	   * ���ܣ����ݵ���
	   * wkf
	   * 2014-09-09
	   * start
	   * 
	   */
		public static void creatFile(String sourceFile){
	      try {
	          /** ����ֻ����Excel�������Ķ���*/
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
			int isok = showYesNoCancelMessage("��Ҫ���븨������?\nע:ģ���ϵ�������������Ϊ��,����0ռ��");
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
							showErrorMessage("��ѡ����.xls��β��(2003��)Excel�ļ�!");
							return;
						}
						creatFile(filepath);
						ArrayList list = new ArrayList();
						Sheet ws = w.getSheet(0);//��ȡ��sheet����
						rows = ws.getRows();//����
						ArrayList cwcslist = new ArrayList();
						for (int i = 1; i < rows; i++) {
							Cell[] cells = ws.getRow(i);
							List al = new ArrayList();//װ��λ0�ֿ�1
							ArriveorderItemVO avo = new ArriveorderItemVO();
							String cinventorycode = cells[0].getContents().trim();//�������
							String vproducenum = cells[1].getContents();//���κ�
							String cstorename = cells[2].getContents().trim();//��λ
							al.add(cstorename);
							String cwarehousename = cells[3].getContents().trim();//�ջ��ֿ�
							al.add(cwarehousename);
							String vfree1 = cells[4].getContents();//������
							String narrvnum1 = cells[5].getContents();
							UFDouble narrvnum = new UFDouble(narrvnum1);//����
							String nassistnum1 = cells[6].getContents();
							UFDouble nassistnum = new UFDouble(nassistnum1);//������
							
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
						Map cwarehouseid1 = getCwarehouseid1(cwcslist);//������вֿ��pk
						Map cstorepk = null;
						if(!cwarehouseid1.equals(null) || cwarehouseid1 != null){
							cstorepk = getCstoreidList(cwcslist);//������л�λ��pkֵ
						}
						Map vomap = cloneBodyVo();//��ñ�������vo��¡
						BillModel bodyitems = getBillCardPanel().getBillModel();
						if (list.size()>0 && list !=null) {
							
							int ss = getBillCardPanel().getBillModel().getRowCount();
							int[] dellineall = new int[ss];
							for (int w = 0; w < ss; w++) {
								dellineall[w] = w;
							}
							getBillCardPanel().getBillModel().delLine(dellineall);//ɾ����������
							
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
								String cwarehouseid = (String) cwarehouseid1.get(cwarehouse);//�ֿ�����
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
								//����������������
								if(narrvnum.compareTo(new UFDouble(0))>0){
									bodyitems.setValueAt(narrvnum, i, "narrvnum");
								}
								if(nassistnum.compareTo(new UFDouble(0))>0){
									Object value = bodyitems.getValueAt(i, "convertrate");//ת����
									if(value != null){
										UFDouble convertrate = new UFDouble(value.toString());
										narrvnum = nassistnum.multiply(convertrate);
										bodyitems.setValueAt(narrvnum, i, "narrvnum");
										bodyitems.setValueAt(nassistnum, i, "nassistnum");
									}else{
										showErrorMessage("�������:"+cinventorycode+"û��ת����,��ά�������µ���!");
									}
								}
								//��Ŀ����	nprice��Ŀ����	nmoney
								Object danjia = bodyitems.getValueAt(i, "nprice");
								if(danjia != null){
									UFDouble nprice = new UFDouble(danjia.toString());
									UFDouble nmoney = narrvnum.multiply(nprice);
									bodyitems.setValueAt(nmoney, i, "nmoney");
								}else{
									showErrorMessage("�������:"+cinventorycode+"û�е���,��ά�������µ���!");
								}
								getBillCardPanel().getBillModel().execEditFormulas(i);
								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					String Error ="�ļ�����ʧ�ܣ�\n"+e.toString();
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
							showErrorMessage("��ѡ����.xls��β��(2003��)Excel�ļ�!");
							return;
						}
						creatFile(filepath);
						ArrayList list = new ArrayList();
						Sheet ws = w.getSheet(0);//��ȡ��sheet����
						rows = ws.getRows();//����
						ArrayList cwcslist = new ArrayList();
						for (int i = 1; i < rows; i++) {
							Cell[] cells = ws.getRow(i);
							List al = new ArrayList();//װ��λ0�ֿ�1
							ArriveorderItemVO avo = new ArriveorderItemVO();
							String cinventorycode = cells[0].getContents().trim();//�������
							String vproducenum = cells[1].getContents();//���κ�
							String cstorename = cells[2].getContents().trim();//��λ
							al.add(cstorename);
							String cwarehousename = cells[3].getContents().trim();//�ջ��ֿ�
							al.add(cwarehousename);
							String vfree1 = cells[4].getContents();//������
							String narrvnum1 = cells[5].getContents();
							UFDouble narrvnum = new UFDouble(narrvnum1);//����
							
							avo.setCbaseid(cinventorycode);
							avo.setVproducenum(vproducenum);
							avo.setCstoreid(cstorename);
							avo.setCwarehouseid(cwarehousename);
							avo.setVfree1(vfree1);
							avo.setNarrvnum(narrvnum);
							cwcslist.add(al);
							list.add(avo);
						}
						Map cwarehouseid1 = getCwarehouseid1(cwcslist);//������вֿ��pk
						Map cstorepk = null;
						if(!cwarehouseid1.equals(null) || cwarehouseid1 != null){
							cstorepk = getCstoreidList(cwcslist);//������л�λ��pkֵ
						}
						Map vomap = cloneBodyVo();//��ñ�������vo��¡
						BillModel bodyitems = getBillCardPanel().getBillModel();
						if (list.size()>0 && list !=null) {
							
							int ss = getBillCardPanel().getBillModel().getRowCount();
							int[] dellineall = new int[ss];
							for (int w = 0; w < ss; w++) {
								dellineall[w] = w;
							}
							getBillCardPanel().getBillModel().delLine(dellineall);//ɾ����������
							
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
								String cwarehouseid = (String) cwarehouseid1.get(cwarehouse);//�ֿ�����
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
								//��Ŀ����	nprice��Ŀ����	nmoney
								Object danjia = bodyitems.getValueAt(i, "nprice");
								if(danjia != null){
									UFDouble nprice = new UFDouble(danjia.toString());
									UFDouble nmoney = narrvnum.multiply(nprice);
									bodyitems.setValueAt(nmoney, i, "nmoney");
								}else{
									showErrorMessage("�������:"+cinventorycode+"û�е���,��ά�������µ���!");
								}
								getBillCardPanel().getBillModel().execEditFormulas(i);
								bodyitems.setValueAt(cwarehouse, i, "cwarehousename");
								bodyitems.setValueAt(cwarehouseid, i, "cwarehouseid");
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					String Error ="�ļ�����ʧ�ܣ�\n"+e.toString();
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
//				String sqlg ="select csname,pk_cargdoc��from bd_cargdoc where csname in ("+sb.toString()+")"; 
				StringBuffer sqlg = new StringBuffer();
				sqlg.append(" select car.csname,car.pk_cargdoc��from bd_cargdoc car ") 
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
		//��ȡ�ֿ�id
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
		 * ���ܣ����ݵ���
		 * wkf
		 * 2014-09-09
		 * end
		 * 
		 */
	/**
	 * �б���ť�¼���Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-20 ����04:13:02
	 */
	private void onButtonClickedList(ButtonObject bo) {

		if (bo == m_btnPrintList) {
			// ����ӡ
			onBatchPrint();
		} else if (bo == m_btnPrintPreviewList) {
			// ����ӡԤ��
			onBatchPrintPreview();
		} else if (bo == m_btnDiscardList) {
			onDiscardSelected();
		} else if (bo == m_btnCard) {
			onCard();
		} else if (bo == m_btnModifyList) {
			if (getStateStr().equals("ת���б�")) {
				onCardNew();
			} else {
				onModifyList();
			}
			// �ù�굽��ͷ��һ���ɱ༭��Ŀ
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
			onImportXml();     //XML����  yqq 2016-11-02 ����
		}
	}

	/**
	 * �����������ܡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-27 ����01:17:02
	 */
	private void onAuditList(ButtonObject bo) {

		Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
		ArriveorderHeaderVO head = new ArriveorderHeaderVO();
		// �����Ź����Ļ�������
		int iRealPos = 0;
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iRealPos = i;
				iRealPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iRealPos);
				// �����ˡ���������
				head = (ArriveorderHeaderVO) getCacheVOs()[iRealPos]
						.getParentVO();
				// �������ǲ���Ա
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
			// ����
			// ArriveorderBO_Client.auditArriveorderMy(arrivevos);
			// ���ò���Ա
			for (int i = 0; i < arrivevos.length; i++) {
				arrivevos[i].getParentVO().setAttributeValue("cuserid",
						getOperatorId());
			}
			boolean isSucc = false;
			try {
				// V31SP1:�����������ڲ���С�ڵ�����������
				String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos,
						"dreceivedate", "varrordercode", ClientEnvironment
								.getInstance().getDate(), ScmConst.PO_Arrive);
				if (strErr != null) {
					throw new BusinessException(strErr);
				}
				// ����ǰ��������
				ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];

				for (int i = 0; i < arrivevos.length; i++) {
					heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();

				}
				// ����δ������ĵ�����
				// arrivevos = ArriveorderBO_Client.getAllWithBody(heads);
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				if(getParentCorpCode().equals("10395"))
				// �����Ƿ���Ҫ���飬û���м���Ĵ��
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
						MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", "����ڼ䴦�ڹ����ڼ䣬�������!Audit period is during the Closing��Not warehousing!");
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
								MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", Transformations.getLstrFromMuiStr("����@Odd numbers&"
										+ checkVO[k].getHeadVO()
												.getVarrordercode()
										+ "&��δ����Ĵ���������������!@Inspection inventory��Please test after audit!"));

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
				// ��ʽ�������ε��ݲɹ����
				for (int i = 0; i < newArrivevos.length; i++) {
					Object returnobj = null;
			
				PfUtilClient.processBatchFlow(this, "APPROVE",
						ScmConst.PO_Arrive, ClientEnvironment.getInstance()
								.getDate().toString(), new ArriveorderVO[]{newArrivevos[i]}, null);
				
				isSucc = PfUtilClient.isSuccess();
				if (isSucc) {
					// ˢ��ǰ����ʾ����
					displayOthersVOs(vSubVos);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "4004COMMON000000071")/*
																		 * @res
																		 * "��˳ɹ�"
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
							MessageDialog.showErrorDlg(this, "��˵�����Audit to manifest",
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
												.toString(), null, newVo, null);// �ɹ���ⵥǩ��״̬
							} catch (Exception e) {
								SCMEnv.out(e);
								MessageDialog.showErrorDlg(this, "��˵�����Audit to manifest",
										e.getMessage());
							}
						}
					}
				else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "4004COMMON000000072")/*
																		 * @res
																		 * "���ʧ��"
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
								MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", "����ڼ䴦�ڹ����ڼ䣬�������!Audit period is during the Closing��Not warehousing!");
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
										MessageDialog.showErrorDlg(this, "�ɹ�������Procurement arrival", Transformations.getLstrFromMuiStr("����@Odd numbers&"
												+ checkVO[k].getHeadVO()
														.getVarrordercode()
												+ "&��δ����Ĵ���������������!@Inspection inventory��Please test after audit!"));
	
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
						// ˢ��ǰ����ʾ����
						displayOthersVOs(vSubVos);
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("common", "4004COMMON000000071")/*
																			 * @res
																			 * "��˳ɹ�"
																			 */);
					}
					else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("common", "4004COMMON000000072")/*
																			 * @res
																			 * "���ʧ��"
																			 */);
					}
				}
			} catch (nc.vo.pub.BusinessException e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000072")/* @res "���ʧ��" */);
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000422")/*
																	 * @res
																	 * "ҵ���쳣"
																	 */,
						e.getMessage());
			} catch (Exception e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "4004COMMON000000072")/* @res "���ʧ��" */);
				if (e instanceof java.rmi.RemoteException) {
					MessageDialog.showErrorDlg(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000422")/*
																		 * @res
																		 * "ҵ���쳣"
																		 */,
							e.getMessage());
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "4004COMMON000000072")/* @res "���ʧ��" */);
		}
	}

	/**
	 * ��������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-27 ����01:30:13
	 */
	private void onUnAuditList(ButtonObject bo) {

		Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
		int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
		BillModel bm = getBillListPanel().getHeadBillModel();
		ArriveorderVO vo = null;
		ArriveorderVO[] arrivevos = null;
		// �����Ź����Ļ�������
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
				// ���ò���Ա
				for (int i = 0; i < arrivevos.length; i++) {
					arrivevos[i].getParentVO().setAttributeValue("cuserid",
							getOperatorId());
				}
				// ����ǰ��������
				ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
				for (int i = 0; i < arrivevos.length; i++) {
					heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
				}
				// ����δ������ĵ�����
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				//
				boolean isSucess = false;
				PfUtilClient.processBatch(this, "UNAPPROVE",
						ScmConst.PO_Arrive, ClientEnvironment.getInstance()
								.getDate().toString(), arrivevos, null);
				isSucess = PfUtilClient.isSuccess();
				if (isSucess) {
					// ˢ��ǰ����ʾ����
					displayOthersVOs(vSubVos);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000184")/*
																			 * @res
																			 * "����ɹ�"
																			 */);
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000185")/*
																			 * @res
																			 * "����ʧ��"
																			 */);
				}
			} catch (nc.vo.pub.BusinessException e) {
				reportException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000185")/* @res "����ʧ��" */);
				MessageDialog.showErrorDlg(
						this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000422")/*
																	 * @res
																	 * "ҵ���쳣"
																	 */,
						e.getMessage());
			} catch (Exception ex) {
				reportException(ex);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000185")/* @res "����ʧ��" */);
				if (ex instanceof java.rmi.RemoteException) {
					MessageDialog.showErrorDlg(
							this,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000422")/*
																		 * @res
																		 * "ҵ���쳣"
																		 */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"40040302", "UPP40040302-000003")/*
																	 * @res
																	 * "������������������ʱ����:"
																	 */
									+ ex.getMessage());
				}
			}
		}
	}

	/**
	 * ��ʾ������ɺ�ĵ��ݡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param subVOs
	 *            <p>
	 * @author czp
	 * @time 2007-3-27 ����01:21:47
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
		// ��ʾ���ݡ�������ť״̬
		loadDataToList();
		// Ĭ����ʾ��һ��
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			onSelectNo();
			getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
			getBillListPanel().getHeadBillModel().setRowState(0,
					BillModel.SELECTED);
		}
		// ˢ�°�ť�߼�
		setButtonsState();
	}

	/**
	 * ����:���������޸�(���������ת����������Ĵ���)
	 */
	public void onCancel() {
		if (getStateStr().equals("ת���޸�")) {
			delArriveorderVOSaved();
			if (getCacheVOs() != null) {
				displayArrBillListPanelNew();
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000126")/* @res "�������ŵ���,����ת��" */);
			} else {
				onEndCreate();
			}
			return;
		}
		onCard();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH008")/*
																	 * @res
																	 * "ȡ���ɹ�"
																	 */);
		// ��ʼ��ȫ�ֱ���
		InitGlobalVar();
	}

	/**
	 * @���ܣ��������б����桰�л�����ť�¼�,�л����������������ת���޸ġ�״̬
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 8:10:39)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onCard() {

		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000128")/* @res "���ڼ�������..." */);

		// ��������
		int index = getBillListPanel().getBodyBillModel().getSortColumn();
		boolean bSortAsc = getBillListPanel().getBodyBillModel()
				.isSortAscending();

		// ����ı�˳���ͬ��
		// int iPos = getBillListPanel().getHeadTable().getSelectedRow();
		// setDispIndex(iPos);
		/* ת���б� */
		if (getStateStr().equals("ת���б�")) {
			onCardNew();
			if (index >= 0) {
				getBillCardPanel().getBillModel().sortByColumn(index, bSortAsc);
			}
			return;
		}
		/* ��ת���б� */
		setM_strState("�������");
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
			SCMEnv.out("���ص���ʱ����");
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000129")/* @res "��������ʧ��" */);
		}
		if (index >= 0) {
			getBillCardPanel().getBillModel().sortByColumn(index, bSortAsc);
		}
		updateUI();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																	 * @res
																	 * "��Ƭ��ʾ"
																	 */);
	}

	/**
	 * @���ܣ�ת�뵽�����޸�
	 */
	private void onCardNew() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000131")/* @res "���ڼ��ص���..." */);
		isFrmList = true;
		setM_strState("ת���޸�");
		getBillListPanel().setVisible(false);
		getBillCardPanel().setVisible(true);
		getBillCardPanel().setEnabled(true);
		setButtonsState();

		// ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
		// ȡҵ������
		String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
		UFBoolean checker = new UFBoolean(false);
		try {
			loadDataToCard();
			// �˻�����(ͷ��)
			setBackReasonEditable();
			// �������ص����к�
			BillRowNo.addNewRowNo(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno");

			checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
			// ���˴������
			if (checker.booleanValue()) {
				String sql = " and (sellproxyflag = 'Y')";
				UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
						.getBodyItem("cinventorycode").getComponent());
				refCinventorycode.getRefModel().addWherePart(sql);
			}
		} catch (Exception e) {
			SCMEnv.out("���ص���ʱ����");
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000132")/* @res "���ص���ʧ��" */);
		}
		/** ���ÿ����֯��ֿ�ƥ�� */
		setOrgWarhouse();

		// ���ݲ���Ա���òɹ�Ա���ɹ�����
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
				// �ɲɹ�Ա�����ɹ�����(����ɹ�������ֵʱ�Ŵ���)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadEmployee(null);
				}
			}
		}

		// ��ӡ���������޸�
		if (getBillCardPanel().getTailItem("iprintcount") != null)
			getBillCardPanel().getTailItem("iprintcount").setEnabled(false);
		updateUI();

		// �����˵��Ҽ�����Ȩ�޿���
		rightButtonRightControl();
		updateButtons();

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "�����޸�" */);
	}

	/**
	 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
	 * 
	 * @return boolean ����ֵΪtrue��ʾ�������ڹرգ�����ֵΪfalse��ʾ���������ڹرա�
	 * 
	 *         �������ڣ�(2001-8-8 13:52:37)
	 */
	public boolean onClosing() {
		if (getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�")) {
			int iRet = MessageDialog
					.showYesNoCancelDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */,
							m_lanResTool.getStrByID("common", "UCH001")/*
																		 * @res
																		 * "�Ƿ񱣴����޸ĵ����ݣ�"
																		 */);
			// ����ɹ�����˳�
			if (iRet == MessageDialog.ID_YES) {
				return onSave();
			}
			// �˳�
			else if (iRet == MessageDialog.ID_NO) {
				return true;
			}
			// ȡ���ر�
			else {
				return false;
			}
		}
		return true;
	}

	/**
	 * ��������:�п���
	 */
	private void onCopyLine() {
		if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() != null
				&& getBillCardPanel().getBodyPanel().getTable()
						.getSelectedRows().length > 0) {
			getBillCardPanel().copyLine();
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH039")/*
																	 * @res
																	 * "�����гɹ�"
																	 */);
	}

	/**
	 * ��������:ɾ��
	 */
	private void onDeleteLine() {
		if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() == null
				|| getBillCardPanel().getBodyPanel().getTable()
						.getSelectedRows().length <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000136")/* @res "δѡȡ�У�ɾ����δ�ɹ�" */);
			return;
		}
		int iSelRowCnt = getBillCardPanel().getBodyPanel().getTable()
				.getSelectedRows().length;
		if (iSelRowCnt == getBillCardPanel().getBodyPanel().getTable()
				.getRowCount()) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000137")/* @res "���嵥����������һ�в��ܱ��棬ɾ����δ�ɹ���" */);
			return;
		}
		getBillCardPanel().delLine();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH037")/*
																	 * @res
																	 * "ɾ�гɹ�"
																	 */);
	}

	/**
	 * @���ܣ����ϵ��� ���̷��� deleteMy() + rewriteOnDiscardMy() �����Ϲ����Ѿ�����Ƭд����ʱ�õ�����
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 10:40:17)
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
						"UPPSCMCommon-000219")/* @res "ȷ��" */, m_lanResTool
						.getStrByID("common", "4004COMMON000000069")/*
																	 * @res
																	 * "�Ƿ�ȷ��Ҫɾ����"
																	 */,
						UIDialog.ID_NO);
		if (iRet != UIDialog.ID_YES) {
			return;
		}
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000139")/* @res "��������..." */);
		ArriveorderVO arrivevo = new ArriveorderVO();
		arrivevo = getCacheVOs()[getDispIndex()];
		// ���Ϻ����ʾλ�ô���
		int IndexLast = getCacheVOs().length - 1;
		int IndexCurr = getDispIndex();
		boolean isLast = false;
		if (IndexLast == IndexCurr) {
			isLast = true;
		}
		// ����
		try {
			// ������Ա
			arrivevo.setCoperatorid(getOperatorId());
			// Ϊ�ж��Ƿ���޸ġ����������˵���
			((ArriveorderHeaderVO) arrivevo.getParentVO())
					.setCoperatoridnow(getOperatorId());
			// ������Ҫ
			arrivevo.getParentVO()
					.setAttributeValue("cuserid", getOperatorId());
			PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive,
					ClientEnvironment.getInstance().getDate().toString(),
					new ArriveorderVO[] { arrivevo });
			boolean bIsSucc = PfUtilClient.isSuccess();
			// ˢ��ǰ�˻���
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
							"UPPSCMCommon-000059")/* @res "����" */, b.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000347")/* @res "����ʧ��" */);
			return;
		} catch (Exception e) {
			reportException(e);
			if (e.getMessage() != null
					&& (e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000250")/* @res "����" */) >= 0
							|| e.getMessage().indexOf(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000212")/* @res "" */) >= 0 || e
							.getMessage()
							.indexOf(
									m_lanResTool.getStrByID("40040301",
											"UPT40040301-000025")/* @res "�˻�" */) >= 0)
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000207")/* @res "�ջ�" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000251")/* @res "����" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000252")/* @res "�ݲ�" */) >= 0
					|| e.getMessage().indexOf(
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000253")/* @res "��" */) >= 0) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */, e
						.getMessage());
			} else
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000140")/* @res "���ϵ���ʧ��" */);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000347")/* @res "����ʧ��" */);
			return;
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "���ϳɹ�" */);
	}

	/**
	 * @���ܣ������б�
	 * @�������������޸���û�б��������;
	 * @���������������� 1.���Ͽ������ϵĵ��� 2.�����������ϵĵ����к�
	 * @���ߣ���־ƽ �������ڣ�(2001-06-20 10:40:17) �޸����ڣ�(2001-10-29 14:40:17)
	 */
	private void onDiscardSelected() {

		int iRet = MessageDialog
				.showYesNoDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000219")/* @res "ȷ��" */, m_lanResTool
						.getStrByID("common", "4004COMMON000000069")/*
																	 * @res
																	 * "�Ƿ�ȷ��Ҫɾ����"
																	 */,
						UIDialog.ID_NO);
		if (iRet != UIDialog.ID_YES) {
			return;
		}
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000139")/* @res "��������..." */);
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
				// ѡ�еĵ���(������)
				arrivevo = getCacheVOs()[iRealPos];
				// ������������������������
				if (!arrivevo.isCanBeModified() || arrivevo.isHaveCheckLine()) {
					if (!lines.trim().equals("")) {
						lines += m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000000")/* @res "��" */;
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
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000141")/*
															 * @res
															 * " �е������Ѿ������������������Ѿ�����,���ŵ��ݲ��ᱻ����"
															 */);
			} else {
				MessageDialog.showWarningDlg(
						this,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						CommonConstant.BEGIN_MARK
								+ lines
								+ CommonConstant.END_MARK
								+ m_lanResTool.getStrByID("40040301",
										"UPP40040301-000142")/*
															 * @res
															 * " �е������Ѿ������������������Ѿ�����,��Щ���ݲ��ᱻ����"
															 */);
			}
		}
		ArriveorderVO[] arrivevos = null;
		if (v.size() > 0) {
			arrivevos = new ArriveorderVO[v.size()];
			v.copyInto(arrivevos);
			try {
				// ������Ա
				for (int j = 0; j < arrivevos.length; j++) {
					arrivevos[j].setCoperatorid(getOperatorId());
					// Ϊ�ж��Ƿ���޸ġ����������˵���
					((ArriveorderHeaderVO) arrivevos[j].getParentVO())
							.setCoperatoridnow(getOperatorId());
					// ������Ҫ
					arrivevos[j].getParentVO().setAttributeValue("cuserid",
							getOperatorId());
				}
				// ���ر���
				arrivevos = RcTool.getRefreshedVOs(arrivevos);
				PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive,
						ClientEnvironment.getInstance().getDate().toString(),
						arrivevos);
				boolean bIsSucc = PfUtilClient.isSuccess();
				// ˢ��ǰ�˻���
				if (bIsSucc) {
					// ȫ������
					if (v.size() == getCacheVOs().length) {
						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getHeadBillModel().clearBodyData();
						setCacheVOs(null);
						updateUI();
					} else {
						// ˢ����ʾ
						delArriveorderVOsDiscarded(v);
						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getHeadBillModel().clearBodyData();
						setDispIndex(0);
						onList();
					}
				}
			} catch (BusinessException b) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */, b
						.getMessage());
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000347")/* @res "����ʧ��" */);
				return;
			} catch (Exception e) {
				reportException(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000059")/* @res "����" */,
						m_lanResTool.getStrByID("40040301",
								"UPP40040301-000143")/* @res "���ϵ�����ʧ��" */);
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000347")/* @res "����ʧ��" */);
				return;
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH006")/*
																		 * @res
																		 * "ɾ���ɹ�"
																		 */);
		} else {
			onSelectNo();
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000144")/* @res "����ʧ��:��ѡ���ݾ���������������" */);
		}
		//
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/* @res "���ϳɹ�" */);
	}

	/**
	 * ���� ���Ĺܹ��� ���õ���״̬����������������������б���
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		if (!(getStateStr().equalsIgnoreCase("�������")
				|| getStateStr().equalsIgnoreCase("�����б�") || getStateStr()
				.equalsIgnoreCase("��Ϣ����"))) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000145")/* @res "��ȡ�������ݺ�,�ĵ��������ܲ�����" */);
		}
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		// ��Ƭ
		if (getStateStr().equalsIgnoreCase("�������")
				|| getStateStr().equalsIgnoreCase("��Ϣ����")) {
			if (getCacheVOs() != null && getCacheVOs().length > 0
					&& getCacheVOs()[getDispIndex()] != null
					&& getCacheVOs()[getDispIndex()].getParentVO() != null) {
				strPks = new String[] { (String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("carriveorderid") };
				strCodes = new String[] { (String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("varrordercode") };
				// �����ĵ�������ɾ����ť�Ƿ����
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
		// �б�
		if (getStateStr().equalsIgnoreCase("�����б�")) {
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
					// �����ĵ�������ɾ����ť�Ƿ����
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
		// �����ĵ������Ի���
		nc.ui.scm.file.DocumentManager.showDM(this, strPks, strCodes,
				mapBtnPowerVo);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000025")/* @res "�ĵ������ɹ�" */);
	}

	/**
	 * @���ܣ�����ת��/ת������
	 */
	private void onEndCreate() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000146")/* @res "�����˳�ת��..." */);
		/* ���û���VO[]:����ת���ɹ���Ҫ���� */
		setCacheVOs(m_VOsAll);
		/* ����б����� */
		getBillListPanel().getBillListData().clearCopyData();
		/* ������ʾλ��:��������ת��ɹ��ĵ�������ͬ���� */
		int iNewCnt = 0;
		if (getCacheVOs() != null && getCacheVOs().length > m_OldVOsLen) {
			iNewCnt = getCacheVOs().length - m_OldVOsLen;
			setDispIndex(getCacheVOs().length - 1);
		} else {
			setDispIndex(m_OldCardVOPos);
		}
		/* ��Ƭ�������� */
		setM_strState("�������");
		/* ���л�����Ƭʱ����ͬ�Ĵ��� */
		onCard();
		/* ��ʼ��ת���ñ��� */
		m_VOsAll = null;
		m_OldCardVOPos = 0;
		m_OldVOsLen = 0;
		if (iNewCnt > 0) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000147")/* @res "ת������:���� " */
					+ iNewCnt
					+ m_lanResTool.getStrByID("40040301", "UPP40040301-000148")/*
																				 * @
																				 * res
																				 * " ���µ���"
																				 */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000149")/* @res "ת������:û���µ�������" */);
		}
		return;
	}

	/**
	 * @���ܣ��Ӷ������ɶԻ������˻�ʱ�Ĵ���
	 */
	private void onExitFrmOrd(ArriveorderVO[] retVOs) {
		/* �����ѡ������ȡ�����أ��� retVOs = null,��onButtonClicked()��֤ */
		if (retVOs != null && retVOs.length > 0) {
			// ���вɹ��繫˾����µĹ�˾��IDת��
			try {
				OrderPubVO.chgDataForArrvCorp(retVOs, getCorpPrimaryKey());
			} catch (BusinessException e) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, e
						.getMessage());
				return;
			}
			//
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000150")/* @res "�������б���������..." */);
			/* ����ת��ǰ����������Ϣ */
			m_VOsAll = getCacheVOs();
			if (m_VOsAll != null && m_VOsAll.length > 0) {
				m_OldVOsLen = m_VOsAll.length;
			} else {
				m_OldVOsLen = 0;
				m_VOsAll = null;
			}
			m_OldCardVOPos = getDispIndex();
			/* ��ǰ�������������������Ϣ */
			setCacheVOs(retVOs);
			// ���ڻ���TS
			m_hTS = new HashMap();
			m_pushSaveVOs = null;
			setDispIndex(0);
			/* ��ʾ���� */
			displayArrBillListPanelNew();
			String[] value = new String[] { String
					.valueOf(getCacheVOs().length) };
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000151", null, value)/*
													 * @res "�б������������: ������"+
													 * getCacheVOs().length +
													 * " �Ŵ����浥��"
													 */);
		}
		this.repaint();
	}

	/**
	 * ��������:��ҳ
	 */
	private void onFirst() {
		int iRollBack = getDispIndex();
		setDispIndex(0);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000026")/* @res "�ɹ���ʾ��ҳ" */);
		} catch (Exception e) {
			SCMEnv.out("���ص���ʱ����");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000153")/* @res "��ʾ��һ�ŵ���ʧ��" */);
		}
	}

	/**
	 * ��������:ĩҳ
	 */
	private void onLast() {
		int iRollBack = getDispIndex();
		setDispIndex(getCacheVOs().length - 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000029")/* @res "�ɹ���ʾĩҳ" */);
		} catch (Exception e) {
			SCMEnv.out("���ص���ʱ����");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000155")/* @res "��ʾ���һ�ŵ���ʧ��" */);
		}
	}

	/**
	 * @���ܣ��б�(��������״̬��ά���޸ĺ�ת���޸�)
	 * @���ߣ���־ƽ �������ڣ�(2001-5-24 9:19:15)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onList() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000156")/* @res "�������б���������" */);
		// ���״̬ͼƬ
		// V5 Del : setImageType(this.IMAGE_NULL);
		// ��������
		int index = getBillCardPanel().getBillModel().getSortColumn();
		boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
		if (getStateStr().equals("ת���޸�")) {
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
																	 * "�б���ʾ"
																	 */);
	}

	/**
	 * ����������
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
				.getStrByID("common", "4004COMMON000000019")/* @res "����ɹ�" */);
	}

	/**
	 * @���ܣ���λ
	 */
	private void onLocate() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000157")/* @res "ѡ��λλ��..." */);
		// ÿ�ε���ʱ���õ���������ʾ����
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
				"UPP40040301-000131")/* @res "���ڼ��ص���..." */);
		if (getLocateDlg().isCloseOK()) {
			int iRollBack = getDispIndex();
			int currIndex = getLocateDlg().getLocateIndex();
			setDispIndex(currIndex - 1);
			setButtonsState();
			try {
				loadDataToCard();
			} catch (Exception e) {
				SCMEnv.out("���ص���ʱ����");
				setDispIndex(iRollBack);
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000158")/* @res "��λʧ��" */);
			}

			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000035")/* @res "��λ�ɹ�" */);
			updateUI();
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000160")/* @res "ȡ����λ" */);
		}
	}

	/**
	 * ��������:���������˵� �������ڣ�(2001-3-27 11:09:34)
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
	 * ��������:����
	 */
	private void onModify() {

		setM_strState("�����޸�");

		int iCurSelectedRow = getBillCardPanel().getBillTable()
				.getSelectedRow();
		getBillCardPanel().updateValue();
		if (iCurSelectedRow >= 0)
			getBillCardPanel().getBillTable().setRowSelectionInterval(
					iCurSelectedRow, iCurSelectedRow);

		// ��ʼ��ɾ������(��λ����ʱ�õ�)
		v_DeletedItems = new Vector();

		// ���Ƿ������ɵ�������Ϊ new UFBoolean(false)
		if (getCacheVOs() != null && getCacheVOs().length > 0) {
			for (int i = 0; i < (getCacheVOs()[getDispIndex()].getChildrenVO().length); i++) {
				((ArriveorderItemVO) ((ArriveorderVO) getCacheVOs()[getDispIndex()])
						.getChildrenVO()[i]).setIssplit(new UFBoolean(false));
				getBillCardPanel().getBillModel().setValueAt(
						new UFBoolean(false), i, "issplit");
			}
		}
		getBillCardPanel().setEnabled(true);
		// ҵ�����Ͳ����޸�
		if (getBillCardPanel().getHeadItem("cbiztype") != null)
			getBillCardPanel().getHeadItem("cbiztype").setEnabled(false);
		// //���ݺŲ����޸�
		// if (getBillCardPanel().getHeadItem("varrordercode") != null)
		// getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
		// �Ƿ��˻������޸�
		if (getBillCardPanel().getHeadItem("bisback") != null)
			getBillCardPanel().getHeadItem("bisback").setEnabled(false);
		// ��ӡ���������޸�
		if (getBillCardPanel().getTailItem("iprintcount") != null)
			getBillCardPanel().getTailItem("iprintcount").setEnabled(false);
		// �˻�����(ͷ��)
		setBackReasonEditable();

		// ���ݲ���Ա���ö�Ӧ�ɹ�Ա������
		setDefaultValueByUser();

		// �����֯���Ʋֿ�
		if (getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�")) {
			setOrgWarhouse();
		}
		setButtonsState();
		updateButtons();

		// ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
		// ȡҵ������
		String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
		UFBoolean checker = new UFBoolean(false);
		try {
			checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
			// ���˴������
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
				.getStrByID("common", "4004COMMON000000030")/* @res "�����޸�" */);
	}

	/**
	 * ���ݲ���Ա���òɹ�Ա���ɹ����š�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-26 ����04:49:35
	 */
	private void setDefaultValueByUser() {
		// ȡ����Ա��Ӧҵ��Ա�����òɹ�Ա(�ɹ�Ա��ֵʱ������)
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
				// �ɲɹ�Ա�����ɹ�����(����ɹ�Ա������ֵʱ�Ŵ���)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadEmployee(null);
				}
			}
		}
	}

	/**
	 * ��������:�����˵��Ҽ�����Ȩ�޿���
	 */
	private void rightButtonRightControl() {
		// û�з����в���Ȩ��
		if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
			getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			getBillCardPanel().getDelLineMenuItem().setEnabled(false);
			getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
		}
		// �����в���Ȩ��
		else {
			getBillCardPanel().getCopyLineMenuItem().setEnabled(
					m_btnCpyLine.isPower());
			getBillCardPanel().getDelLineMenuItem().setEnabled(
					m_btnDelLine.isPower());
			getBillCardPanel().getPasteLineMenuItem().setEnabled(
					m_btnPstLine.isPower());
			// ճ������β��ճ���������߼���ͬ
			getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(
					m_btnPstLine.isPower());
		}
	}

	/**
	 * @���ܣ��б�״̬�µ��޸�
	 * @���ߣ���־ƽ �������ڣ�(2001-9-13 20:02:06)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onModifyList() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000161")/* @res "�����л�����Ƭ..." */);
		isFrmList = true;
		onCard();
		onModify();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/* @res "�����޸�" */);
	}

	/**
	 * ��������:��ҳ
	 */
	private void onNext() {
		int iRollBack = getDispIndex();
		setDispIndex(getDispIndex() + 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000028")/* @res "�ɹ���ʾ��һҳ" */);
		} catch (Exception e) {
			SCMEnv.out("���ص���ʱ����");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000163")/* @res "��ʾ��һ�ŵ���ʧ��" */);
		}
	}

	/**
	 * ��������:ճ����
	 */
	private void onPasteLine() {
		int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
		try {
			getBillCardPanel().pasteLine();
		} catch (Exception e) {
			SCMEnv.out("ճ����ʱ������" + e.getMessage());
		}
		int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000164")/* @res "ճ��δ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��" */);
		} else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
			showHintMessage("");
		} else {
			showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																		 * @res
																		 * "ճ���гɹ�"
																		 */);
			// �����к�
			BillRowNo.pasteLineRowNo(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
	}

	/**
	 * ��������:ճ���е���β
	 */
	private void onPasteLineToTail() {
		int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
		try {
			getBillCardPanel().pasteLineToTail();
		} catch (Exception e) {
			SCMEnv.out("ճ���е���βʱ������" + e.getMessage());
		}
		int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/*
										 * @res
										 * "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��"
										 */);
		} else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
			showHintMessage("");
		} else {
			showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																		 * @res
																		 * "ճ���гɹ�"
																		 */);
			// �����к�
			BillRowNo.addLineRowNos(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
	}

	/**
	 * ��������:��ҳ
	 */
	private void onPrevious() {
		int iRollBack = getDispIndex();
		setDispIndex(getDispIndex() - 1);
		try {
			loadDataToCard();
			setButtonsState();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000027")/* @res "�ɹ���ʾ��һҳ" */);
		} catch (Exception e) {
			SCMEnv.out("���ص���ʱ����");
			setDispIndex(iRollBack);
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000167")/* @res "��ʾ��һ�ŵ���ʧ��" */);
		}
	}

	/**
	 * @���ܣ�������ѯ
	 * @���ߣ���־ƽ
	 * @������(2001-7-18 12:41:25)
	 */
	private void onQuery() {
		/**/
		m_hTS = null;
		int iRetType = getQueryConditionDlg().showModal();
		if (iRetType == UIDialog.ID_OK) {
			m_bQueriedFlag = true;
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000168")/* @res "���ڲ�ѯ����..." */);
			getArriveVOsFromDB();
			setDispIndex(0);
			if (getStateStr().equals("�����б�")) {
				onList();
			} else {
				isFrmList = false;
				onCard();
			}
			if (getCacheVOs() == null || getCacheVOs().length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000169")/* @res "��ѯ���:û�з��������ĵ���" */);
			} else {
				String[] value = new String[] { String
						.valueOf(getCacheVOs().length) };
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000170", null, value)/*
														 * @res "��ѯ���:��ѯ��"+
														 * getCacheVOs().length
														 * + "�ŵ���"
														 */);
			}

			showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																		 * @res
																		 * "��ѯ���"
																		 */);
		}
	}

	/**
	 * ���ߣ���ά�� ���ܣ��˴����뷽��˵�� ������ ���أ� ���⣺ ���ڣ�(2004-3-8 10:35:43)
	 */
	private void onQueryBOM() {
		String sState = getStateStr();
		String sCmangId = null;
		int iPos;
		ArriveorderItemVO itemVO = null;
		if (sState != null && (sState.equals("ת���б�") || sState.equals("�����б�"))) {
			if (getBillListPanel().getBodyTable().getRowCount() == 0)
				return;
			iPos = getBillListPanel().getBodyTable().getSelectedRow();
			if (iPos == -1) {
				MessageDialog.showHintDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000364")/* @res "��ѡ���д������!" */);
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
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
						m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000364")/* @res "��ѡ���д������!" */);
				return;
			}
			itemVO = (ArriveorderItemVO) getCacheVOs()[getDispIndex()]
					.getChildrenVO()[iPos];
			sCmangId = itemVO.getCmangid();
		}
		if (PuPubVO.getString_TrimZeroLenAsNull(sCmangId) == null) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "��ʾ" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000364")/*
																			 * @res
																			 * "��ѡ���д������!"
																			 */);
			return;
		}
		SetPartDlg dlg = new SetPartDlg(this);
		dlg.setParam(getCorpPrimaryKey(), sCmangId);
		dlg.showSetpartDlg();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "��ѯ���"
																	 */);
	}

	/**
	 * ���ܣ�������ѯ ������(2002-10-31 19:45:39) �޸ģ�2003-04-21/czp/ͳһ�����۶Ի���
	 * ����״̬:��ʼ������������������޸ģ������б���ת���б���ת���޸�
	 */
	private void onQueryInvOnHand() {
		ArriveorderVO voPara = null;
		ArriveorderItemVO item = null;
		ArriveorderItemVO[] items = null;
		/* ��Ƭ */
		if (getStateStr().equals("�������") || getStateStr().equals("�����޸�")
				|| getStateStr().equals("ת���޸�")) {
			voPara = (ArriveorderVO) getBillCardPanel().getBillValueVO(
					ArriveorderVO.class.getName(),
					ArriveorderHeaderVO.class.getName(),
					ArriveorderItemVO.class.getName());
			if (voPara == null) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000171")/* @res "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (ArriveorderItemVO) getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								ArriveorderItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (ArriveorderItemVO[]) getBillCardPanel().getBillModel()
						.getBodyValueVOs(ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000172")/* @res "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* �ƻ�ִ������=�������� */
			item.setArrvdate((UFDate) voPara.getParentVO().getAttributeValue(
					"dreceivedate"));
			/* ��Ϣ�����Լ�� */
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
						"UPP40040301-000172")/* @res "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
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
						SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
									"UPP40040301-000173")/* @res "��ȡ��Ȩ�޹�˾�쳣" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000174")/*
														 * @res
														 * "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"
														 */);
					SCMEnv.out(e);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* �б� */
		else if (getStateStr().equals("�����б�") || getStateStr().equals("ת���б�")) {
			/* ��ͷ��Ϣ�����Լ�� */
			ArriveorderHeaderVO head = null;
			if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
					ArriveorderHeaderVO.class.getName()) == null
					|| getBillListPanel().getHeadBillModel()
							.getBodySelectedVOs(
									ArriveorderHeaderVO.class.getName()).length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000171")/* @res "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			head = (ArriveorderHeaderVO) getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(ArriveorderHeaderVO.class.getName())[0];
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| head.getAttributeValue("pk_corp").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000175")/* @res "δ��ȷ��˾,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillListPanel().getBodyTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (ArriveorderItemVO) getBillListPanel()
						.getBodyBillModel().getBodyValueRowVO(iSelRows[0],
								ArriveorderItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (ArriveorderItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								ArriveorderItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040301",
							"UPP40040301-000172")/* @res "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* �ƻ�ִ������=�������� */
			item.setArrvdate(head.getDreceivedate());
			/* ��Ϣ�����Լ�� */
			if (item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("arrvdate") == null
					|| item.getAttributeValue("arrvdate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000172")/* @res "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
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
						SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
									"UPP40040301-000173")/* @res "��ȡ��Ȩ�޹�˾�쳣" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000174")/*
														 * @res
														 * "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"
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
				.getStrByID("common", "4004COMMON000000032")/* @res "������ѯ���" */);
	}

	/**
	 * ���ߣ���ά�� ���ܣ������ջ� ������ ���أ� ���⣺ ���ڣ�(2004-3-15 10:20:43)
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
							"UPP40040301-000176")/* @res "���ݺ�Ϊ�գ������뵥�ݺ�!" */);
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
						"UPP40040301-000177")/* @res "û�з��������ĵ��ݣ�" */);
				return;
			}
			// �����Ƿ񱣴�ǰ���
			if (getQuickArrDlg().isLookBefSave()) {
				onExitFrmOrd(retVOs);
				// ��ʽ���浽����
			} else {
				m_hTS = new HashMap();
				m_pushSaveVOs = retVOs;
				for (int i = 0; i < retVOs.length; i++) {
					ArriveorderVO saveVO = retVOs[i];

					// ���̱��淽���Ĳ���

					// aryPara0 : 0����˾������1���޸�ǰ�ĵ�������2����ǰ���ݱ༭״̬��3.�޸ĺ��VO������VO��
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

					// ���ñ�ͷ����״̬(����)
					((ArriveorderHeaderVO) saveVO.getParentVO())
							.setStatus(VOStatus.NEW);
					for (int j = 0; j < saveVO.getChildrenVO().length; j++) {
						((ArriveorderItemVO[]) saveVO.getChildrenVO())[j]
								.setStatus(VOStatus.NEW);
					}
					saveVO.setOprType(VOStatus.NEW);
					// ������Ա
					saveVO.setCoperatorid(getOperatorId());
					// �жϲ����Ĺ��÷��� PubDMO.checkVoNoChanged() ��Ҫ
					saveVO.getParentVO().setAttributeValue("cuserid",
							getOperatorId());

					ArrayList aryRet = (ArrayList) PfUtilClient.processAction(
							"SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment
									.getInstance().getDate().toString(),
							saveVO, aryPara);

					// ����������ˢ�»���VO��TS
					refreshVOTs((ArriveorderVO) aryRet.get(1));
					String strRetKey = (aryRet == null) ? null
							: (String) aryRet.get(0);
					boolean bIsSucc = PfUtilClient.isSuccess();
					// ���������
					if (strRetKey != null && bIsSucc) {
						/* ˢ�±���ɹ����� */
						/* �ű� N_23_SAVEBASE ���趨��VO */
						ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
						if (voTmp == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "�������ݳɹ�����ˢ������ʱ���������Ժ����ԣ�"
																 */);
						/* ���ӱ���ɹ����ݵ�����ʾ���ݻ���ĩβ */
						appArriveorderVOSaved(voTmp);
					}
				}
				onEndCreate();
				m_pushSaveVOs = null;
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000179")/* @res "����ɹ�,ת������" */);
			}
		} catch (Exception e) {
			reportException(e);
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000036")/* @res "�����ջ����" */);

	}

	/**
	 * @���ܣ�ˢ�����ݣ���Ƭ�б����棩
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 13:35:04)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onRefresh() {
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000168")/* @res "���ڲ�ѯ����..." */);
		// ��������
		getArriveVOsFromDB();
		/* ��ʼ���������λ��(���ǵ�ɾ������) */
		setDispIndex(0);
		// ��ʾ���ݡ�������ť״̬
		if (getStateStr().equals("�������") || getStateStr().equals("��ʼ��")) {
			try {
				loadDataToCard();
			} catch (Exception e) {
				SCMEnv.out("���ص���ʱ����");
			}
		} else if (getStateStr().equals("�����б�")) {
			loadDataToList();
			// Ĭ����ʾ��һ��
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
					"UPP40040301-000169")/* @res "��ѯ���:û�з��������ĵ���" */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000170")/* @res "��ѯ���:��ѯ��" */
					+ getCacheVOs().length
					+ m_lanResTool.getStrByID("40040301", "UPP40040301-000180")/*
																				 * @
																				 * res
																				 * "�ŵ���"
																				 */);
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "ˢ�³ɹ�"
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
	     * ����: �Ƚ�������ֵ�Ĵ�С,����true��ʾ�����ǰ���
	     * @param String sa,String sbҪ�Ƚϵ������ַ���
	     * @return boolean
	     */
	    public  static boolean  compareDou(UFDouble num,UFDouble sum){   //�ɹ����������� num
	        boolean flag=false;
	        int i=num.compareTo(sum);
	        if (i<0){
	            flag = true;
	            return flag;
	        }
	        return flag;
	    }
	    
	    //end by yqq 2016-09-07
	    
	    
		//add by yqq 2016-12-29��̨�����ƣ�������û��ѡ��ֿⲻ׼���棬����òֿ������˻�λ����δѡ���λҲ���ܱ���		
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
	 * @���ܣ������޸Ľ��
	 */
	private boolean onSave() {
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("�ɹ������������onSave��ʼ");
		
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();

		
		//add by yqq 2016-12-29��̨�����ƣ�������û��ѡ��ֿⲻ׼���棬����òֿ������˻�λ����δѡ���λҲ���ܱ��棬�������Ϻ��Ƹǵ�
		if(!(pk_corp.trim().equals("1078")||getCorpPrimaryKey().equals("1108"))){			
		 int sizee = this.getBillCardPanel().getBillModel().getRowCount();	
	      for (int i = 0; i < sizee; i++){  
		        String sCwarehouseid  = (String) getBillCardPanel().getBodyValueAt(i,"cwarehouseid"); //�ջ��ֿ�ID
		  		BillModel bm = getBillCardPanel().getBillModel();
			 	ArrayList list = getCsflag(pk_corp,sCwarehouseid);   //�Ƿ��λ�����Ļ�ȡ
			 	for(int j =0;j<list.size();j++){
			 	  Map map = (Map) list.get(j);	 				 	  
				  String csflag = map.get("csflag") == null ? "" : map.get("csflag").toString();
				  
				  if (csflag.equals("Y")) {  //�ֿ��л�λ����ʱ
					  if(bm.getValueAt(i, "cstoreid")==null){  //��λID
						  showErrorMessage("�����"+(i+1)+"�л�λ����Ϊ��");
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
		
	//edit by yqq 2016-09-07 ��ɽ�ƹ�  �ɹ�����������narrvnum���ܴ��ڲɹ����������� nordernum
		if("1019".equals(pk_corp)){			
		 int size = this.getBillCardPanel().getBillModel().getRowCount();	
	      for (int i = 0; i < size; i++){  
				UFDouble sum = getBillCardPanel().getBodyValueAt(i,"narrvnum")== null ? 
				new UFDouble(0.0): (UFDouble)(getBillCardPanel().getBodyValueAt(i,"narrvnum"));     //��������
		        String sCrowno  = (String) getBillCardPanel().getBodyValueAt(i,"crowno "); //�к�
		
		        String sCorder_bid  = (String) getBillCardPanel().getBodyValueAt(i,"corder_bid "); //�ɹ�������ID
			 	ArrayList list = getNordernum(pk_corp,sCorder_bid);   //�ɹ����������Ļ�ȡ
			 	for(int j =0;j<list.size();j++){
			 	  Map map = (Map) list.get(j);	 	 
				  UFDouble num = map.get("nordernum")==null ? new UFDouble (0.0):new UFDouble(map.get("nordernum").toString()); //�ɹ���������
				   				
				  if (compareDou(num,sum)){ 
					showWarningMessage (sCrowno+"�кŵĲɹ��������������ڲɹ�����������");
					return false;      
				  }					
			   }				  
	     	}	
		}
		//end by yqq
		
		showHintMessage(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000181")/* @res "���ڱ��浥��..." */);
				
		// ��ֹ�༭
		getBillCardPanel().stopEditing();

		// ���Ӷ�У�鹫ʽ��֧��,������ʾ��UAP���� since v501
		if (!getBillCardPanel().getBillData().execValidateFormulas()) {
			return false;
		}

		// ���ڱ����VO
		ArriveorderVO saveVO = null;
		// ����ģ������ʾ��VO(ת���޸ļ������޸ĵĽ��)
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
				MessageDialog.showErrorDlg(this,"����Error",e.getMessage());  
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
        	//MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", Transformations.getLstrFromMuiStr("���@Stamp&:" + dhlist.toString()+ "&���ִ������Ѵ���!@In the existing amount already exists!"));    	 	
        	//return false;
			if(MessageDialog.ID_YES!=showYesNoMessage(Transformations.getLstrFromMuiStr("���@The pile No. &"+dhlist.toString() + "&�ظ���⣡@repeat warehousing!")+" \r\n"
			+ "�Ƿ������ continue?"))
	{
		return false ;
	}
		}
		
		if (!m_bSaveMaker)
			((ArriveorderHeaderVO) newvo.getParentVO())
					.setCoperator(getClientEnvironment().getUser()
							.getPrimaryKey());

		// �������
		if (!chechDataBeforeSave(newvo, oldvo))
			return false;
		
		// ���̱��淽���Ĳ���
		// aryPara0 : 0����˾������1���޸�ǰ�ĵ�������2����ǰ���ݱ༭״̬��3.�޸ĺ��VO������VO��
		// aryPara1 : 0���Ƿ����û�������1���ϴβ���ʱ�Ķ�����ID����������
        
		ArrayList aryPara = new ArrayList(2);
		ArrayList aryPara0 = new ArrayList();
		aryPara0.add(getCorpPrimaryKey());
		aryPara0.add(oldvo);
		aryPara0.add(getStateStr().equals("ת���޸�") ? "insert" : "update");
		// ������������*(-1) �����̨��Ϊ���������� power
		newvo.setDigitsNumPower(CPurchseMethods
				.getMeasDecimal(getCorpPrimaryKey()) * (-1));
		aryPara0.add(newvo);
		aryPara.add(aryPara0);
		aryPara.add(null);
		aryPara.add(new Integer(0));
		aryPara.add(new String("cvendormangid"));
		if (getStateStr().equals("�����޸�")) {
			// �����޸ı����VO
			saveVO = (ArriveorderVO) getBillCardPanel().getBillValueChangeVO(
					ArriveorderVO.class.getName(),
					ArriveorderHeaderVO.class.getName(),
					ArriveorderItemVO.class.getName());
			if (!m_bSaveMaker)
				((ArriveorderHeaderVO) saveVO.getParentVO())
						.setCoperator(getClientEnvironment().getUser()
								.getPrimaryKey());

			// ������޸�״̬�£�ת��״̬���ã�
			// ����б������λ���� �� ��λ��������д���δ�������ɾ������,��ӻ�����һ��������˴���(���ǽ��������ݿ���ɾ��)
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
			// ������Ա
			saveVO.setCoperatorid(getOperatorId());
			// ������Ҫ
			saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
		} else {
			// ����������VO
			saveVO = newvo;
			// ���ñ�ͷ����״̬(����)
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setStatus(VOStatus.NEW);
			// �Ƶ���
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setCoperator(getOperatorId());
			for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
				((ArriveorderItemVO[]) saveVO.getChildrenVO())[i]
						.setStatus(VOStatus.NEW);
			}
			saveVO.setOprType(VOStatus.NEW);
			// ���̻�д�ã��ϲ���Դ��������
			saveVO.setUpBillType(((ArriveorderItemVO) oldvo.getChildrenVO()[0])
					.getCupsourcebilltype());
			// ������Ա
			saveVO.setCoperatorid(getOperatorId());
			// �жϲ����Ĺ��÷��� PubDMO.checkVoNoChanged() ��Ҫ
			saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
		}
		// �������Լ����--��ע
		UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem(
				"vmemo").getComponent();
		UITextField vMemoField = nRefPanel.getUITextField();
		String vmemo = vMemoField.getText();
		((ArriveorderHeaderVO) saveVO.getParentVO()).setVmemo(vmemo);
		// �������Լ����--�˻�����
		if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
			UIRefPane refPanel = (UIRefPane) getBillCardPanel().getHeadItem(
					"vbackreasonh").getComponent();
			UITextField txtBack = refPanel.getUITextField();
			String strBack = txtBack.getText();
			((ArriveorderHeaderVO) saveVO.getParentVO())
					.setVbackreasonh(strBack);
		}
		timer.addExecutePhase(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000254")/* @res "����ǰ��׼������" */);
		// Ϊ�ж��Ƿ���޸ġ����������˵���
		((ArriveorderHeaderVO) saveVO.getParentVO())
				.setCoperatoridnow(getOperatorId());
		String strRetKey = null;
		boolean isCycle = true;
		// �޸�ʱ��ƴ��δ�ı�ı���VO(˵���μ�getSaveVO()����)
		if (getStateStr().equals("�����޸�")) {
			saveVO = getSaveVO(saveVO);
			timer.addExecutePhase("getSaveVO");
		}
		// �Ƿ���Ҫ���˵��ݺ�:�������ֹ�¼�뵥�ݺ�
		if (getStateStr().equals("ת���޸�")) {
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
				timer.addExecutePhase("ִ��SAVE�ű�");
				// ����������ˢ�»���VO��TS
				refreshVOTs((ArriveorderVO) aryRet.get(1));
				timer.addExecutePhase("ˢ�»���VO��TS");
				strRetKey = aryRet == null ? null : (String) aryRet.get(0);
				boolean bIsSucc = PfUtilClient.isSuccess();
				// ���������
				if (getStateStr().equals("�����޸�")) {
					if (strRetKey != null && bIsSucc) {
						setM_strState("�������");
						// �ű����趨��VO
						getCacheVOs()[getDispIndex()] = (ArriveorderVO) aryRet
								.get(1);
						if (getCacheVOs()[getDispIndex()] == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "�������ݳɹ�����ˢ������ʱ���������Ժ����ԣ�"
																 */);
						// ArriveorderBO_Client.findByPrimaryKey(oldvo.getParentVO().getPrimaryKey());
						// ˳�����û�
						setButtonsState();
						loadDataToCard();
						getBillCardPanel().setEnabled(false);
						showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000006")/* @res "����ɹ�" */);
						updateUI();
					}
				} else if (getStateStr().equals("ת���޸�")) {
					if (strRetKey != null && bIsSucc) {
						// �ӵ�ǰδ���浥����ɾ������ɹ�����
						delArriveorderVOSaved();
						// �Ѹ���ts��VO
						ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
						if (voTmp == null)
							throw new BusinessException(
									m_lanResTool.getStrByID("40040301",
											"UPP40040301-000178")/*
																 * @res
																 * "�������ݳɹ�����ˢ������ʱ���������Ժ����ԣ�"
																 */);

						refreshVoFieldsByKey(voTmp, strRetKey);

						// ���ӱ���ɹ����ݵ�����ʾ���ݻ���ĩβ
						appArriveorderVOSaved(voTmp);
						// ����ת���Ƿ��������ͬ����
						if (getCacheVOs() != null) {
							displayArrBillListPanelNew();
							showHintMessage(m_lanResTool.getStrByID("40040301",
									"UPP40040301-000182")/* @res "����ɹ�,����ת��" */);
						} else {
							onEndCreate();
							showHintMessage(m_lanResTool.getStrByID("40040301",
									"UPP40040301-000179")/* @res "����ɹ�,ת������" */);
						}
					}
				}
				timer.addExecutePhase("�������");
				showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																			 * @res
																			 * "����ɹ�"
																			 */);
			} catch (Exception e) {
				// //���˵��ݺ�
				// if (isBackCode) {
				// SCMEnv.out("���˵��ݺſ�ʼ[ArriveUI]...");
				// try {
				// PubHelper.returnBillCode(newvo);
				// } catch (Exception ex) {
				// SCMEnv.out("���˵��ݺ��쳣����[ArriveUI]");
				// }
				// SCMEnv.out("���˵��ݺ���������[ArriveUI]");
				// }
				// ������д�ɹ��������ݲ���ʾ���
				if (e instanceof RwtRcToPoException) {
					// �빺���ۼ�����������ʾ
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// ����ѭ��
						isCycle = true;
						// �Ƿ��û�ȷ��
						saveVO.setUserConfirm(true);
					} else {
						return false;
					}
				}
				// ������дί�ⶩ�����ݲ���ʾ���
				else if (e instanceof RwtRcToScException) {
					// �����ۼ�����������ʾ
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// ����ѭ��
						isCycle = true;
						// �Ƿ��û�ȷ��
						saveVO.setUserConfirm(true);
					} else {
						return false;
					}
				} else if (e instanceof BusinessException
						|| e instanceof java.rmi.RemoteException
						|| e instanceof ValidationException) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "����" */, e
									.getMessage());
				} else if (e.getMessage() != null
						&& (e.getMessage().indexOf(
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000250")/* @res "����" */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000212")/*
																	 * @res "����"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPT40040301-000025")/*
																	 * @res "�˻�"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000207")/*
																	 * @res "�ջ�"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000251")/*
																	 * @res "����"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000252")/*
																	 * @res "�ݲ�"
																	 */) >= 0
								|| e.getMessage().indexOf(
										m_lanResTool.getStrByID("40040301",
												"UPP40040301-000253")/*
																	 * @res "��"
																	 */) >= 0
								|| e.getMessage().indexOf("BusinessException") >= 0 || e
								.getMessage().indexOf("RemoteException") >= 0)) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "����" */, e
									.getMessage());
				} else {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "����" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000183")/* @res "ϵͳ�쳣������ʧ��" */);
				}
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* @res "����ʧ��" */);
			}
		}
		timer.showAllExecutePhase("�ɹ������������onSave��������");
		// ��ʼ��ȫ�ֱ���
		InitGlobalVar();
		return true;
	}




	private Object getBodyInfo(String string) {
			// TODO Auto-generated method stub
			return null;
		}

	/**
	 * ˢ�±���ɹ�����(��Ϊ���漰������������Ҫ��Ҫˢ���������ں�������,����״̬��TS���Ƶ�ʱ�䣬����ʱ�䣬����޸�ʱ��)
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
	 * @���ܣ�ѡ�����е�����
	 * @���ߣ���־ƽ �������ڣ�(2001-6-8 14:21:35)
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
		// ���ð�ť״̬
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000033")/* @res "ȫ��ѡ�гɹ�" */);
	}

	/**
	 * @���ܣ�ȡ������ѡ���ĵ���������
	 * @���ߣ���־ƽ �������ڣ�(2001-6-8 14:22:12)
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
		// ���ð�ť״̬
		setButtonsList();
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000034")/* @res "ȫ��ȡ���ɹ�" */);
	}

	/**
	 * ����:ִ������
	 */
	private void onUnAudit(ButtonObject bo) {
		try {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000186")/* @res "��������..." */);
			ArriveorderVO vo = getCacheVOs()[getDispIndex()];
			// ����
			PfUtilClient.processBatchFlow(null, "UNAPPROVE"
					+ getClientEnvironment().getUser().getPrimaryKey(),
					ScmConst.PO_Arrive, getClientEnvironment().getDate()
							.toString(), new ArriveorderVO[] { vo }, null);
			if (!PfUtilClient.isSuccess()) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000187")/* @res "����ʧ��(ƽ̨���ó����쳣)" */);
				return;
			}
			// ����ɹ���ˢ��
			refreshVoFieldsByKey(vo, vo.getParentVO().getPrimaryKey());

			getCacheVOs()[getDispIndex()] = vo;
			// ˢ�°�ť״̬
			setButtonsState();
			// ���ص���
			try {
				loadDataToCard();
			} catch (Exception e) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000188")/*
											 * @res
											 * "����ɹ�,�����ص���ʱ�����쳣,��ˢ�½����ٽ�����������"
											 */);
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH011")/*
																		 * @res
																		 * "����ɹ�"
																		 */);
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000189")/* @res "�����쳣,����ʧ��" */);
			SCMEnv.out(e);
			if (e instanceof java.rmi.RemoteException
					|| e instanceof BusinessException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040301", "UPP40040301-000099")/* @res "����" */, e
						.getMessage());
			}
		}
	}

	/**
	 * ���ߣ���ά�� ���ܣ�ˢ�±����л���TS�����TS�����ڴ������� ������ ���أ� ���⣺ ���ڣ�(2004-4-1 10:55:04)
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
		// ˢ�»���VO�ı���TS,��������
		try {
			if (items[0].getCupsourcebilltype().equals("21")) {
				String[] sID = new String[size];
				for (int i = 0; i < size; i++) {
					sID[i] = items[i].getCorder_bid();
				}
				// ˢ��TS
				HashMap hTs = ArriveorderHelper.queryNewTs(sID);
				for (int i = 0; i < size; i++) {
					Object[] ob = (Object[]) hTs.get(items[i].getCorder_bid());
					if (ob != null && ob[0] != null)
						items[i].setTsbup(ob[0].toString());
				}
			}
			ArriveorderVO[] vos = getCacheVOs();
			// ������ʽ����
			if (m_pushSaveVOs != null && m_pushSaveVOs.length > 0)
				vos = m_pushSaveVOs;
			if (vos == null || vos.length == 0)
				return;

			String sUpSourceBTs = null;
			String sUpsourceRowid = null;

			// ����TS����TS����
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

			// ��TS����ȥ����VO����
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

		// ����VO����Ƭ����ģ��
		getBillCardPanel().setBillValueVO(VO);
		// �����������ݱ�����(DR!=0)�򶳽�ʱ������ȷ��ʾ��������
		loadBDData();
		// ��������ģ���Զ�����
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
		// �رպϼƿ���
		boolean bOldNeedCalc = bm.isNeedCalculate();
		bm.setNeedCalculate(false);
		// ִ�м��ع�ʽ
		bm.execLoadFormula();
		// �򿪺ϼƿ���
		bm.setNeedCalculate(bOldNeedCalc);
		getBillCardPanel().updateValue();

		// ��ʾ��ͷ��ע
		refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo")
				.getComponent();
		refPane.setValue((String) VO.getParentVO().getAttributeValue("vmemo"));
		// ��ʾ��ͷ�˻�����
		if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
			refPane = (UIRefPane) getBillCardPanel()
					.getHeadItem("vbackreasonh").getComponent();
			refPane.setValue((String) VO.getParentVO().getAttributeValue(
					"vbackreasonh"));
		}
		// �����滻�����չ����Ӳ��� {�������л򶩵���ID = cmangid }
		Vector vCmangids = new Vector();
		String strCmangid = null;
		m_hBillIDsForCmangids = new Hashtable();
		String strKey = (getStateStr().equals("ת���޸�")) ? "corder_bid"
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
		// ���д������屸ע������
		String strCmain = null;
		String strCbaseid = null;
		String strCassid = null;
		Object oNarrvnum = null;
		Object oNassinum = null;
		UFDouble ufdNarrvnum = null;
		UFDouble ufdNassinum = null;
		Object oValue = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			// ���屸ע��ʼ��:�����ܴ���afterEdit()
			if (bm.getValueAt(i, "vmemo") == null) {
				bm.setValueAt("", i, "vmemo");
			}
			// �����˻����ɳ�ʼ��:�����ܴ���afterEdit()
			if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
				if (bm.getValueAt(i, "vbackreasonb") == null) {
					bm.setValueAt("", i, "vbackreasonb");
				}
			}
			// �Ƿ������ɵ���--��δ��
			bm.setValueAt(new UFBoolean(false), i, "issplit");

			strCbaseid = (String) bm.getValueAt(i, "cbaseid");
			strCmangid = (String) bm.getValueAt(i, "cmangid");
			strCassid = (String) bm.getValueAt(i, "cassistunit");
			strCmain = (String) bm.getValueAt(i, "cmainmeasid");
			// �Ƿ񸨼�������
			UFBoolean bIsAssMana = new UFBoolean(
					PuTool.isAssUnitManaged(strCbaseid));
			if (bIsAssMana.booleanValue()) {
				if (strCassid == null || strCassid.trim().equals("")) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "����" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000190")/*
														 * @res
														 * "�и����������Ĵ���д��ڿո�������"
														 */);
					return;
				}
				// ���û�����
				UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
						strCassid);
				bm.setValueAt(convert, i, "convertrate");
				// ����������ͬ��������Ϊ 1.0
				if (strCmain != null && strCmain.equals(strCassid)) {
					bm.setValueAt(new UFDouble(1.0), i, "convertrate");
				}
				// �ǹ̶������ʣ�������=������/������
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
	 * @���ܣ���Ƭ��ʾ����
	 */
	private void loadDataToCard() throws Exception {
		UIRefPane refPane = null;
		BillModel bm = getBillCardPanel().getBillModel();
		if (getCacheVOs() != null) {
			// ���б���Ƭ�л�ʱ��������
			if (isFrmList) {
				isFrmList = false;
				// ��ȡ������ʵ��λ��
				int iShowPos = getBillListPanel().getHeadTable()
						.getSelectedRow();
				if (iShowPos >= 0) {
					iShowPos = PuTool.getIndexBeforeSort(getBillListPanel(),
							iShowPos);
					setDispIndex(iShowPos);
				}
			}
			// ��������ʱ���ش���ˢ�±���,V5,�������޸�Ҫˢ�£���Ϊ�����Ƶ�ʱ�䡢����޸�ʱ�䡢����ʱ��
			if (!getStateStr().equals("ת���޸�")) {
				// ��ȡ����(δ�����ع���ˢ��)
				try {
					getCacheVOs()[getDispIndex()] = RcTool
							.getRefreshedVO(getCacheVOs()[getDispIndex()]);
				} catch (Exception be) {
					if (be instanceof BusinessException)
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000422")/* @res "ҵ���쳣" */,
								be.getMessage());
					throw be;
				}
			}
			// ������ʽ������ⵥʱ����������쳣��Ҫ�������˺����������ÿ�
			ArriveorderHeaderVO ArrBillHeadVO = (ArriveorderHeaderVO) getCacheVOs()[getDispIndex()]
					.getParentVO();
			if (ArrBillHeadVO.getIbillstatus().intValue() == 0) {
				ArrBillHeadVO.setCauditpsn(null);
				ArrBillHeadVO.setDauditdate(null);
			}
			// ���ɹ���˾��ʼ���Ĳ��գ�{ҵ��Ա�����š���Ӧ��}
			String strPurCorp = ArrBillHeadVO.getPk_purcorp();
			((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
					.getComponent()).getRefModel().setPk_corp(strPurCorp);
			((UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
					.getComponent()).getRefModel().setPk_corp(strPurCorp);

			/*
			 * V50 ����ǰ�� 2006-11-24 : Wangyf&Xy&Xhq&Czp,ע�͵�,���ջ���˾������Ӧ�̲���
			 * ((UIRefPane
			 * )getBillCardPanel().getHeadItem("cvendormangid").getComponent
			 * ()).getRefModel().setPk_corp(strPurCorp);
			 */

			// ����VO����Ƭ����ģ��
			getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
			// �����������ݱ�����(DR!=0)�򶳽�ʱ������ȷ��ʾ��������
			loadBDData();
			// ��������ģ���Զ�����
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
			// �رպϼƿ���
			boolean bOldNeedCalc = bm.isNeedCalculate();
			bm.setNeedCalculate(false);
			// ִ�м��ع�ʽ
			bm.execLoadFormula();
			// �򿪺ϼƿ���
			bm.setNeedCalculate(bOldNeedCalc);
			getBillCardPanel().updateValue();

			// ��ʾ��ͷ��ע
			refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo")
					.getComponent();
			refPane.setValue((String) getCacheVOs()[getDispIndex()]
					.getParentVO().getAttributeValue("vmemo"));
			// ��ʾ��ͷ�˻�����
			if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
				refPane = (UIRefPane) getBillCardPanel().getHeadItem(
						"vbackreasonh").getComponent();
				refPane.setValue((String) getCacheVOs()[getDispIndex()]
						.getParentVO().getAttributeValue("vbackreasonh"));
			}
			// �����滻�����չ����Ӳ��� {�������л򶩵���ID = cmangid }
			Vector vCmangids = new Vector();
			String strCmangid = null;
			m_hBillIDsForCmangids = new Hashtable();
			String strKey = (getStateStr().equals("ת���޸�")) ? "corder_bid"
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
			// ���д������屸ע������
			String strCmain = null;
			String strCbaseid = null;
			String strCassid = null;
			Object oNarrvnum = null;
			Object oNassinum = null;
			UFDouble ufdNarrvnum = null;
			UFDouble ufdNassinum = null;
			Object oValue = null;
			for (int i = 0; i < bm.getRowCount(); i++) {
				// ���屸ע��ʼ��:�����ܴ���afterEdit()
				if (bm.getValueAt(i, "vmemo") == null) {
					bm.setValueAt("", i, "vmemo");
				}
				// �����˻����ɳ�ʼ��:�����ܴ���afterEdit()
				if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
					if (bm.getValueAt(i, "vbackreasonb") == null) {
						bm.setValueAt("", i, "vbackreasonb");
					}
				}
				// �Ƿ������ɵ���--��δ��
				bm.setValueAt(new UFBoolean(false), i, "issplit");

				strCbaseid = (String) bm.getValueAt(i, "cbaseid");
				strCmangid = (String) bm.getValueAt(i, "cmangid");
				strCassid = (String) bm.getValueAt(i, "cassistunit");
				strCmain = (String) bm.getValueAt(i, "cmainmeasid");
				// �Ƿ񸨼�������
				UFBoolean bIsAssMana = new UFBoolean(
						PuTool.isAssUnitManaged(strCbaseid));
				if (bIsAssMana.booleanValue()) {
					if (strCassid == null || strCassid.trim().equals("")) {
						MessageDialog.showErrorDlg(this,
								m_lanResTool.getStrByID("SCMCOMMON",
										"UPPSCMCommon-000059")/* @res "����" */,
								m_lanResTool.getStrByID("40040301",
										"UPP40040301-000190")/*
															 * @res
															 * "�и����������Ĵ���д��ڿո�������"
															 */);
						return;
					}
					// ���û�����
					UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
							strCassid);
					bm.setValueAt(convert, i, "convertrate");
					// ����������ͬ��������Ϊ 1.0
					if (strCmain != null && strCmain.equals(strCassid)) {
						bm.setValueAt(new UFDouble(1.0), i, "convertrate");
					}
					// �ǹ̶������ʣ�������=������/������
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
			// ����޸ģ����ù��ܰ�ť�Ƿ���Ч
			if (!getStateStr().equals("ת���޸�")) {
				getBillCardPanel().getHeadItem("varrordercode").setEnabled(
						false);
				if (getStateStr().equals("�������")) {
					setBtnLines(false);
				}
			}
			// ת���޸ģ����õ��ݺš��ñ���������
			if (getStateStr().equals("ת���޸�")) {
				getBillCardPanel().getHeadItem("varrordercode").setValue(null);
				getBillCardPanel().getHeadItem("varrordercode").setEnabled(
						getBillCardPanel().getHeadItem("varrordercode")
								.isEdit());
				// ���汣��������
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
						SCMEnv.out("���汣��������ʱ����[nc.ui.rc.receive.ArriveUI.setArriveVOsToArrCard()");
					}
				}
				for (int i = 0; i < bm.getRowCount(); i++) {
					strCmangid = (String) bm.getValueAt(i, "cmangid");
					bm.setValueAt(m_hValiddays.get(strCmangid), i, "ivalidday");
				}
			}
			// ����״̬ͼƬ
			try {
				getBillCardPanel().update(getGraphics());
			} catch (Exception e) {
				SCMEnv.out("����ͼƬʱ����(��Ӱ��ҵ�����)");
			}
			// ������Դ��Ϣ
			PuTool.loadSourceInfoAll(getBillCardPanel(),
					BillTypeConst.PO_ARRIVE);
		} else {
			if (getBillCardPanel().getBillData() != null) {
				// V5 Del : setImageType(this.IMAGE_NULL);
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
			}
		}
		// ������������ҵ����������ֵ
		if (getBusitype() != null) {
			refBusi.setPK(getBusitype());
		}
		// ��ֵ���ֶ�������������
		setNumFieldsNeg(isBackBill()); // isBackBill()�����ڵ���ģ����ֵ֮��
		// �˻�����(ͷ��)
		boolean bIsEdit = getStateStr().equals("�����޸�")
				|| getStateStr().equals("ת���޸�");
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
	 * @���ܣ���ѯ�۵��б�����д����
	 * @���ߣ���־ƽ �������ڣ�(2001-6-7 17:25:38)
	 */
	private void loadDataToList() {

		if (getCacheVOs() != null) {
			getBillListPanel().getBodyBillModel().clearBodyData();
			ArriveorderHeaderVO[] headers = null;
			headers = getArriveHeaderVOs(getCacheVOs());
			getBillListPanel().setHeaderValueVO(headers);
			// ��ʾ��ͷ��ע���˻�����
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
	 * ��������:�б仯��༭������ 1�����ø��������գ� 2�����û����ʣ� //3�������Ƿ�̶������ʣ� 4�����Ƹ�����������Ϣ�ı༭״̬
	 */
	private void setAssisUnitEditState2(BillEditEvent event) {

		if (event.getRow() < 0) {
			return;
		}
		// �Ƿ���и���������
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
			// ���ø���������
			setRefPaneAssistunit(event.getRow());
			// ���ÿɱ༭��
			getBillCardPanel().setCellEditable(event.getRow(), "convertrate",
					getBillCardPanel().getBodyItem("convertrate").isEdit());
			getBillCardPanel().setCellEditable(event.getRow(), "nassistnum",
					getBillCardPanel().getBodyItem("nassistnum").isEdit());
			getBillCardPanel().setCellEditable(event.getRow(),
					"cassistunitname",
					getBillCardPanel().getBodyItem("cassistunitname").isEdit());
			String cassistunit = (String) getBillCardPanel().getBillModel()
					.getValueAt(event.getRow(), "cassistunit");
			// ������Ϊ��,���������ɱ༭
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
			} else { // ��������Ϊ��
				// ���û�����
				UFDouble ufdConv = nc.ui.pu.pub.PuTool.getInvConvRateValue(
						strCbaseid, cassistunit);
				Object oTmp = getBillCardPanel().getBillModel().getValueAt(
						event.getRow(), "convertrate");
				if (oTmp == null || oTmp.toString().trim().equals("")) {
					getBillCardPanel().getBillModel().setValueAt(ufdConv,
							event.getRow(), "convertrate");
				}
				// ���ÿɱ༭��
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
				// ���������������
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
				// ����������ǹ̶�������
				if (nc.ui.pu.pub.PuTool.isFixedConvertRate(strCbaseid,
						cassistunit)) {
					getBillCardPanel().setCellEditable(event.getRow(),
							"convertrate", false);
				}
				// ���������������ͬ,�����ʲ��ɱ༭
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
			// û�и���������ʱ��������ϢΪ��(ģ���в��ָ���ϢҪ����ʱ�������û����ɼ�)
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
	 * ��������:�����˻������Ƿ�ɱ༭
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
	 * �����в����Ƿ����
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
	 * @���ܣ����õ����б�״̬�µİ�ť
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 7:58:28)
	 */
	private void setButtonsList() {

		// �б�����
		m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000463")/*
									 * @res "��Ƭ��ʾ"
									 */);

		/* ����ת�� */
		m_btnEndCreate.setEnabled(false);
		m_btnEndCreate.setVisible(false);
		// ҵ������
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
		m_btnImportXml.setEnabled(true);		 //XML����  yqq 2016-11-02 ����
		m_btnMaintains.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnPrints.setEnabled(true);

		/* ������ѯ�����׼���ѯ���ĵ�������Ԥ������ӡ���޸ġ����ϡ�����������ȫѡ��ȫ������Ƭ��ʾ/�б���ʾ */

		int iDataCnt = getCacheVOs() == null ? 0 : getCacheVOs().length;
		int iSeltCnt = getBillListPanel().getHeadTable().getSelectedRowCount();

		// ����������
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
		// δѡ������
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

		/* ������ѯ�����׼���ѯ���ĵ�������Ԥ������ӡ���޸ġ����ϡ�����������ȫѡ��ȫ������Ƭ��ʾ/�б���ʾ */
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
	 * @���ܣ��������ɵĵ������б�(δ����ʱ)��ť�߼�
	 */
	private void setButtonsListNew() {
		//
		for (int i = 0; i < m_aryArrListButtons.length; i++) {
			m_aryArrListButtons[i].setEnabled(false);
		}
		m_btnCancel.setEnabled(false);
		/* ֻ�С��л�����������ת������ť���� */
		m_btnModifyList.setEnabled(true);
		m_btnEndCreate.setVisible(true);
		m_btnEndCreate.setEnabled(true);
		//
		updateButtonsAll();
	}

	/**
	 * ��Ϣ���İ�ť�߼�
	 * 
	 */
	private void setButtonsMsgCenter() {

		// ����
		m_btnAudit.setEnabled(true);
		updateButton(m_btnAudit);
		// ����
		m_btnUnAudit.setEnabled(true);
		updateButton(m_btnUnAudit);
		// ״̬��ѯ
		m_btnQueryForAudit.setEnabled(true);
		updateButton(m_btnQueryForAudit);
		// �ĵ�����
		m_btnDocument.setEnabled(true);
		updateButton(m_btnDocument);
		// ������
		m_btnLookSrcBill.setEnabled(true);
		updateButton(m_btnLookSrcBill);
	}

	/**
	 * @���ܣ��������״̬�°�ť����
	 */
	private void setButtonsCard() {
		//
		setButtonsInit();

		if (getCacheVOs() != null && getCacheVOs().length >= 1) {
			// ��ӡ����λ��ˢ�¡��޸ġ����ϡ�����
			m_btnPrint.setEnabled(true);
			m_btnCombin.setEnabled(true);
			m_btnPrintPreview.setEnabled(true);
			m_btnLocate.setEnabled(true);
			m_btnRefresh.setEnabled(m_bQueriedFlag);
			m_btnRefreshList.setEnabled(m_bQueriedFlag);
			// �޸ġ�����
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
			// ����
			m_btnOthers.setEnabled(true);
			// ����ά��
			m_btnMaintains.setEnabled(true);
			// ���
			m_btnBrowses.setEnabled(true);
			// ������ѯ
			m_btnUsable.setEnabled(true);
			// ���׼�
			m_btnQueryBOM.setEnabled(true);
			// �в���
			setBtnLines(false);
			// �ĵ�����
			m_btnDocument.setEnabled(true);
			// ״̬��ѯ
			m_btnQueryForAudit.setEnabled(true);
			// ������
			m_btnLookSrcBill.setEnabled(true);
			// �����ջ�
			m_btnQuickReceive.setEnabled(true);
			// ������ĩ���߼�
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
			// ����ť
			m_btnSendAudit
					.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
			//
			m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
			m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()]
					.isCanUnAudit());

			// ��֧����������δ�����������Ϊά��û��¼��ϸ񡢲��ϸ������Ĺ���
			m_btnCheck.setEnabled(m_bQcEnabled);

		}
		// ��ѯˢ��
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML����  yqq 2016-11-02 ����
		m_btnRefresh.setEnabled(m_bQueriedFlag);
		//
		updateButtonsAll();
	}

	/**
	 * @���ܣ��޸İ�ť�߼�
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 13:39:39)
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
		// ����
		m_btnOthers.setEnabled(true);
		// �����ջ�
		m_btnQuickReceive.setEnabled(false);
		// ������ѯ
		m_btnUsable.setEnabled(true);
		// ���׼�
		m_btnQueryBOM.setEnabled(true);
		m_btnRefresh.setEnabled(false);
		m_btnLocate.setEnabled(false);
		m_btnDocument.setEnabled(false);
		m_btnLookSrcBill.setEnabled(false);

		// ����ά��
		m_btnMaintains.setEnabled(true);
		m_btnSave.setEnabled(true);
		m_btnCancel.setEnabled(true);
		m_btnModify.setEnabled(false);
		m_btnDiscard.setEnabled(false);
		m_btnImportBill.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML����  yqq 2016-11-02 ����
		// ���
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(false);
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);

		// ����ť
		m_btnSendAudit
				.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));

		setBtnLines(true);

		//
		updateButtonsAll();
	}

	/**
	 * @���ܣ��������ɵĵ�������Ƭ���水ť�߼�
	 * @���ߣ���־ƽ �������ڣ�(2001-7-31 18:42:07)
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
	 * ���ð�ť״̬ �������ڣ�(2001-3-17 9:00:09)
	 */
	private void setButtonsState() {

		int iVal = -999;// ֧�ֲ�ҵ��������չ

		if (getStateStr().equals("��ʼ��")) {
			setButtonsInit();
			iVal = 0;
		} else if (getStateStr().equals("�������")) {
			setButtonsCard();
			iVal = 1;
		} else if (getStateStr().equals("�����޸�")) {
			setButtonsModify();
			iVal = 2;
		} else if (getStateStr().equals("�����б�")) {
			setButtonsList();
			iVal = 3;
		} else if (getStateStr().equals("ת���б�")) {
			setButtonsListNew();
			iVal = 4;
		} else if (getStateStr().equals("ת���޸�")) {
			setButtonsModifyNew();
			iVal = 5;
		} else if (getStateStr().equals("��Ϣ����")) {
			setButtonsMsgCenter();
			iVal = 6;
		}
		setExtendBtnsStat(iVal);
	}

	/**
	 * ��������:�ı���水ť״̬
	 */
	private void updateButtonsAll() {
		int iLen = getBtnTree().getButtonArray().length;
		for (int i = 0; i < iLen; i++) {
			update(getBtnTree().getButtonArray()[i]);
		}
	}

	/**
	 * �������ڣ� 2005-9-20 ���������� ���°�ť״̬���ݹ鷽ʽ��
	 */
	private void update(ButtonObject bo) {
		updateButton(bo);
		if (bo.getChildCount() > 0) {
			for (int i = 0, len = bo.getChildCount(); i < len; i++)
				update(bo.getChildButtonGroup()[i]);
		}
	}

	/**
	 * ��ȡ��ť������Ψһʵ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return <p>
	 * @author czp
	 * @time 2007-3-13 ����01:16:48
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
	 * ���ܣ��ڱ༭����������������: 1.���Ϊ�ջ��ߴ���0���ϸ����������ϸ��������ɱ༭����Ʒ������;�����������ͬ����
	 * 2.���С��0���ϸ������ɱ༭����Ʒ������;�����������ϸ񡢲��ϸ�ͬ����
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
		// #������
		if (!bIsNegative) {
			// �ϸ��������ɱ༭
			/*
			 * delete 2003-10-22
			 * getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum",
			 * false);
			 */
			// �ϸ������뵽������ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMinValue(0.0);

			// ��Ʒͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			// //;��ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
			// ���ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMinValue(0.0);

			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMaxValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
		}
		// #������
		else {
			// �ϸ������ɱ༭
			/*
			 * delete 2003-10-22
			 * getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum",
			 * true);
			 */
			// �ϸ������뵽������ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nelignum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// ��Ʒͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("npresentnum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// ;��ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nwastnum")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
			// ���ͬ��
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMaxValue(0.0);
			((UIRefPane) getBillCardPanel().getBodyItem("nmoney")
					.getComponent()).getUITextField().setMinValue(
					nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
		}
	}

	/**
	 * @���ܣ��������б�ͷ�任ʱ��������д����Ӧ�ӱ�����
	 * @���ߣ���־ƽ �������ڣ�(2001-6-8 16:41:42) �޸ģ�Ϊ���Ч�ʣ�Ҫ���Ӷ�ָ����ͷ���ر���Ĳ��� 0530
	 * @param row0
	 *            int
	 */
	private boolean setListBodyData(int row0) {
		boolean isErr = false;
		if (!getStateStr().equals("ת���б�")) {
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
									"UPPSCMCommon-000422")/* @res "ҵ���쳣" */, be
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
		// ���д��� ------------------------------------------------ ��ʼ
		String strCbaseid = null;
		String strCmain = null;
		String strCassid = null;
		Object oNarrvnum = null;
		Object oNassinum = null;
		UFDouble ufdNarrvnum = null;
		UFDouble ufdNassinum = null;
		Object oValue = null;
		for (int i = 0; i < bm.getRowCount(); i++) {
			// ���屸ע��ʼ��
			if (bm.getValueAt(i, "vmemo") == null) {
				bm.setValueAt("", i, "vmemo");
			}
			strCbaseid = (String) bm.getValueAt(i, "cbaseid");
			// strCmangid = (String) bm.getValueAt(i, "cmangid");
			strCassid = (String) bm.getValueAt(i, "cassistunit");
			strCmain = (String) bm.getValueAt(i, "cmainmeasid");
			// �Ƿ񸨼�������
			UFBoolean bIsAssMana = new UFBoolean(
					PuTool.isAssUnitManaged(strCbaseid));
			if (bIsAssMana.booleanValue()) {
				if (strCassid == null || strCassid.trim().equals("")) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/* @res "����" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000190")/*
														 * @res
														 * "�и����������Ĵ���д��ڿո�������"
														 */);
					return true;
				}
				// ���û�����
				UFDouble convert = PuTool.getInvConvRateValue(strCbaseid,
						strCassid);
				bm.setValueAt(convert, i, "convertrate");
				// �������������ͬ
				if (strCmain != null && strCmain.equals(strCassid)) {
					bm.setValueAt(new UFDouble(1.0), i, "convertrate");
				}
				// ������ǹ̶������ʣ�����������/������ȡ�û�����
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
		// ���д��� -------------------------------------------------- ����

		// ������Դ��Ϣ��Դͷ��Ϣ
		PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_ARRIVE);
		//
		return isErr;
	}

	/**
	 * ���ܣ��б�����ʱ�����������б���ť�߼����� ���ߣ���־ƽ ���ڣ�(2003-2-24 17:02:24)
	 */
	private void setButtonsListWhenErr() {
		//
		for (int i = 0; i < m_aryArrListButtons.length; i++) {
			m_aryArrListButtons[i].setEnabled(false);
		}
		//
		m_btnQuery.setEnabled(true);
		m_btnImportXml.setEnabled(true);   //XML����  yqq 2016-11-02 ����
		m_btnBrowses.setEnabled(true);
		m_btnRefresh.setEnabled(true);
		//
		updateButtonsAll();
	}

	/**
	 * @���ܣ����õ�����������
	 * @���ߣ���־ƽ �������ڣ�(2001-6-19 20:13:12)
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
	 * @���ܣ����õ�ǰ��ʾ������
	 * @���ߣ���־ƽ �������ڣ�(2001-6-20 8:47:47)
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
	 * @���ܣ����õ�ǰ����ά��״̬
	 * @���ߣ���־ƽ �������ڣ�(2001-6-19 20:18:22)
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
	 * ��������:������ֵ�����ֶ�ȡֵ��Χ / | false: ����ȡֵ isBack = | | true : �� \
	 */
	private void setNumFieldsNeg(boolean isBack) {
		double iMin = nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM;
		double iMax = nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM;
		if (isBack) {
			iMax = 0;
		}
		// �����ջ�
		UIRefPane refNarrvnum = (UIRefPane) getBillCardPanel().getBodyItem(
				"narrvnum").getComponent();
		refNarrvnum.setMinValue(iMin);
		refNarrvnum.setMaxValue(iMax);
		// ������
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
	 * ���ܣ�����ת��ʱ�л����޸ġ����ϡ�ȫѡ��ȫ�����ĵ�������ť��ʾ�߼�����ǰ���ݶ�λ ��ť�߼�
	 * 
	 * @�л�������ֻ��һ��ѡ��ʱ��Ч
	 * @�޸ģ���Ч
	 * @��ӡ����Ч
	 * @���ϣ���Ч
	 * @ȫѡ����Ч
	 * @ȫ������Ч
	 * @�ĵ���������Ч
	 * @������ѯ����Ч
	 * @����ת������Ч
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
		/* ������ת�������� */
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
	 * @���ܣ����ÿ����֯��ֿ�ƥ��
	 */
	private void setOrgWarhouse() {
		UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
				"cstoreorganization").getComponent();
		String sPkCalBody = ref.getRefPK();
		PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(),
				getCorpPrimaryKey(), sPkCalBody, "cwarehousename");
	}

	private void setRefPaneAssistunit(int row) {
		// �������ID��������ID
		Object cbaseid = getBillCardPanel().getBillModel().getValueAt(row,
				"cbaseid");
		// ���ø�������λ����
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
	 * ������ť �������ڣ�(2001-3-17 9:00:09)
	 */
	private void updateButtonsMy() {
		if (getStateStr().equals("�������б�"))
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
		// ѡ������
		int iSelCnt = 0;
		// ������Ϊδѡ��
		int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.NORMAL);
		}
		// �õ���ѡ�е���
		int[] iaSelectedRow = getBillListPanel().getHeadTable()
				.getSelectedRows();
		if (iaSelectedRow == null || iaSelectedRow.length == 0) {
			m_nFirstSelectedIndex = -1;
		} else {
			iSelCnt = iaSelectedRow.length;
			// m_nFirstSelectedIndex = iaSelectedRow[0];
			// ѡ�е��б�ʾΪ�򣪺�
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
			// ˢ��
			getBillListPanel().getBodyTable().updateUI();
		}
		// ��ť�߼�
		if ("ת���б�".equals(getStateStr())) {
			setButtonsListValueChangedNew(iSelCnt);
		} else {
			setButtonsList();
		}
		// �������ҵ���쳣���������ù��ܰ�ť
		if (isErr) {
			setButtonsListWhenErr();
		}
		updateButtons();
	}

	/**
	 * ��������:�Զ������PK(����)
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
	 * ��������:�Զ������PK(��ͷ)
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
		timer.start("�ɹ��������������chechDataBeforeSave��ʼ");

		int nError = -1;
		// ��鵥���к�
		if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res "����ʧ��" */);
			return false;
		}
		timer.addExecutePhase("��鵥���к�verifyRowNosCorrect");
		// ��鵥��ģ��ǿ���
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("40040301",
					"UPP40040301-000191")/* @res "����ʧ��:������Ŀ���ڿ���" */);
			MessageDialog
					.showWarningDlg(this, m_lanResTool.getStrByID("40040301",
							"UPP40040301-000192")/* @res "����ģ��ǿ�����" */, e
							.getMessage());
			return false;
		}
		timer.addExecutePhase(m_lanResTool.getStrByID("40040301",
				"UPP40040301-000193")/* @res "��鵥��ģ��ǿ���validateNotNullField" */);
		try {
			// ���������������Ϸ���
			ArriveorderItemVO bodyVO[] = (ArriveorderItemVO[]) newvo
					.getChildrenVO();
			ArriveorderHeaderVO headVO = (ArriveorderHeaderVO) newvo
					.getParentVO();
			for (nError = 0; nError < bodyVO.length; nError++) {
				if (headVO.getBisback().booleanValue()
						&& bodyVO[nError].getNarrvnum() != null
						&& bodyVO[nError].getNarrvnum().doubleValue() > 0)
					throw new ValidationException(m_lanResTool.getStrByID(
							"40040301", "UPP40040301-000275")/* @res"�˻�����������Ϊ��!" */);
				bodyVO[nError].validate();
			}
			// ���¼�����������Ϸ���
			if (!checkModifyData(newvo, oldvo)) {
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* @res "����ʧ��" */);
				return false;
			}
			// ���¼�������Ƿ񳬳����ݿ�����ɷ�Χ
			if (!nc.vo.scm.field.pu.FieldDBValidate
					.validate((ArriveorderItemVO[]) newvo.getChildrenVO())) {
				showHintMessage(m_lanResTool.getStrByID("40040301",
						"UPP40040301-000194")/* @res "���ڲ������ݳ������ݿ�����ɷ�Χ,����" */);
				return false;
			}
		} catch (ValidationException e) {
			String[] value = new String[] { String.valueOf(nError + 1),
					e.getMessage() };
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("40040301",
							"UPP40040301-000195")/* @res "�Ϸ��Լ��" */,
							m_lanResTool.getStrByID("40040301",
									"UPP40040301-000196", null, value)/*
																	 * @res
																	 * "������"+
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
					"UPPSCMCommon-000010")/* @res "����ʧ��" */);
			return false;
		}
		timer.addExecutePhase("���ݼ��Ϸ���");
		timer.showAllExecutePhase("�ɹ��������������chechDataBeforeSave����");

		return true;
	}

	/**
	 * ���ߣ���ά�� ���ܣ������ӡ���� �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
	 */
	private void onCardPrint() {
		ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(vo);
		try {
			if (printCard == null) {
				// Ŀǰ�����Ͼ������벹����
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
							"UPPSCMCommon-000270")/* @res "��ʾ" */, printCard
							.getPrintMessage());
		} catch (Exception e1) {
			SCMEnv.out(e1);
		}
	}

	/**
	 * ���ߣ���ά�� ���ܣ������ӡ���� �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
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
				// Ŀǰ�����Ͼ������벹����
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
							"UPPSCMCommon-000270")/* @res "��ʾ" */, printCard
							.getPrintMessage());
		} catch (Exception e1) {
			SCMEnv.out(e1);
		}
	}

	/**
	 * ���ߣ���ά�� ���ܣ�����ӡ �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
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
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, e1
						.getMessage());
			}
		}
		try {
			printList.onBatchPrint(getBillListPanel(), getBillCardPanel(),
					ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "��ʾ" */, printList
							.getPrintMessage());
		} catch (BusinessException e) {

		}
	}

	/**
	 * ���ߣ���ά�� ���ܣ�����ӡ �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
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
						"SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, e1
						.getMessage());
			}
		}
		try {
			printList.onBatchPrintPreview(getBillListPanel(),
					getBillCardPanel(), ScmConst.PO_Arrive);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "��ʾ" */, printList
							.getPrintMessage());
		} catch (BusinessException e) {
		}
	}

	/**
	 * ��������:���ص���ģ��֮ǰ�ĳ�ʼ��
	 */

	private void initBillBeforeLoad(BillData bd) {

		// ---------����ģ�����ǰ�ĳ�ʼ����������������
		if (bd != null && bd.getBodyItem("vfree0") != null
				&& bd.getBodyItem("vfree0") != null) {
			FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
			m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0")
					.getLength());
			// �Ӽ�����
			m_firpFreeItemRefPane.getUIButton().addActionListener(this);
			bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
		}
		// ��ʼ������
		initRefPane(bd);

		// ��ʼ��ComboBox
		// initComboBox(bd);
		// ��ʼ������
		// initDecimal(bd);

	}

	/**
	 * ���ο���������չ��ť��Ҫ����ο��������������ʵ��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * ������ο�����ť�����Ӧ������Ҫ����ο��������������ʵ��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
	}

	/**
	 * ���ο���״̬��ԭ�н���״̬�����󶨣�Ҫ����ο��������������ʵ��
	 * 
	 * ״̬��ֵ���ձ���
	 * 
	 * 0����ʼ�� 1��������� 2�������޸� 3�������б� 4��ת���б� 5��ת���޸�
	 */
	public void setExtendBtnsStat(int iState) {
	}

	/**
	 * ���󵽻���
	 * <p>
	 * <strong>����ģ�飺</strong>�ɹ�����
	 * <p>
	 * <strong>����޸��ˣ�</strong>czp
	 * <p>
	 * <strong>����޸����ڣ�</strong>2006-02-09
	 * <p>
	 * <strong>����������</strong>
	 * <p>
	 * 
	 * @param ��
	 * @return ��
	 * @throws ��
	 * @since NC50
	 * @see
	 */
	private void onSendAudit() {

		// �༭״̬���󣽡����桱��������
		if (getStateStr().equals("ת���޸�") || getStateStr().equals("�����޸�")) {
			onSave();
		}
		// �ò������Ƿ�����������
		if (getCacheVOs() == null || getDispIndex() < 0)
			return;
		ArriveorderVO vo = getCacheVOs()[getDispIndex()];
		if (isNeedSendAudit(getCacheVOs()[getDispIndex()])) {
			try {
				PfUtilClient.processAction("SAVE", BillTypeConst.PO_ARRIVE,
						ClientEnvironment.getInstance().getDate().toString(),
						vo);
			} catch (Exception e) {
				SCMEnv.out("����������ʧ�ܣ�");
				SCMEnv.out(e);
				if (e instanceof BusinessException
						|| e instanceof RuntimeException) {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000270")/* @res "��ʾ" */, e
									.getMessage());
				} else {
					MessageDialog.showErrorDlg(this,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000270")/* @res "��ʾ" */,
							m_lanResTool.getStrByID("SCMCOMMON",
									"UPPSCMCommon-000408")/* @res"����ʧ�ܣ�" */);
				}
			}
		}
		showHintMessage(m_lanResTool.getStrByID("common", "UCH023")/*
																	 * @res
																	 * "������"
																	 */);
	}

	/**
	 * �жϵ������Ƿ��б�Ҫ����
	 * <p>
	 * <strong>����ģ�飺</strong>�ɹ�����
	 * <p>
	 * <strong>����޸��ˣ�</strong>czp
	 * <p>
	 * <strong>����޸����ڣ�</strong>2006-02-09
	 * <p>
	 * <strong>����������</strong>Ҫ��ͬʱ���㣬
	 * <p>
	 * 1)������״̬Ϊ�����ɡ�(Ŀǰ���빺�����ɹ���������һ�£�������ͨ����Ҫ[�޸�]-[����]����������״̬��Ϊ���ɣ��ſ�����)
	 * <p>
	 * 2)��������������
	 * <p>
	 * 
	 * @param ��
	 * @return ��
	 * @throws ��
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
	 * ��ȡ��ǰVO����Ϣ������
	 * <p>
	 * <strong>����ģ�飺</strong>�ɹ�����
	 * <p>
	 * <strong>����޸��ˣ�</strong>czp
	 * <p>
	 * <strong>����޸����ڣ�</strong>2006-02-10
	 * <p>
	 * <strong>����������</strong>
	 * <p>
	 * 
	 * @param ��
	 * @return ��Ϣ������ʾ��ҵ�񵥾�VO
	 * @throws ��
	 * @since NC50
	 * @see
	 */
	public AggregatedValueObject getVo() throws Exception {
		ArriveorderVO vo = null;
		if (getCacheVOs() != null && getCacheVOs().length == 1
				&& getCacheVOs()[0] != null) {
			SCMEnv.out("��������ֵ���������²�ѯ!");
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
	 * ��ѯ��ǰ��������״̬
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
																	 * "����״̬��ѯ�ɹ�"
																	 */);
	}

	/**
	 * <p>
	 * ���򷽷�������Ҫ����Ļ���VO����
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArray() {
		return getCacheVOs();
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ά��
	 **/
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		SCMEnv.out("����ά���ӿ�...");

		if (maintaindata == null || maintaindata.getBillID() == null) {
			SCMEnv.out("msgVo Ϊ�գ�ֱ�ӷ���!");
			SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
			SCMEnv.out(new Exception());
			SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
			return;
		}
		// ���ؿ�Ƭ
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		// ���ð�ť��
		setButtons(m_aryMsgCenter);
		/*
		 * for(int i=0;i<m_aryMsgCenter.length;i++){
		 * m_aryMsgCenter[i].setEnabled(true); }
		 */
		// ��ѯ����������
		ArriveorderVO vo = null;
		// ��¼����ID��getVo()��
		m_strBillId = maintaindata.getBillID();
		try {
			vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
		} catch (Exception e) {
			PuTool.outException(this, e);
			return;
		}

		// �����ǰ��¼��˾���ǲ���Ա�Ƶ����ڹ�˾��������޲�����ť�����ṩ������ܣ�by chao , xy , 2006-11-07
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
				// �����ݸ���Դ����
				String cupsourcebilltype = ((ArriveorderItemVO[]) getCacheVOs()[i]
						.getChildrenVO())[0].getCupsourcebilltype();
				((ArriveorderVO) getCacheVOs()[i])
						.setUpBillType(cupsourcebilltype);
				// ˢ�±����ϣ������
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
			SCMEnv.out("���ص���������ʱ������");/* -=notranslate=- */
			SCMEnv.out(e);
		}
		// �����ǰ��¼��˾���ǵ���������˾������ʾ���ܰ�ť
		if (bSameCorpFlag) {
			onModify();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 **/
	public void doAddAction(ILinkAddData adddata) {

		SCMEnv.out("���������ӿ�...");

		// Ĭ�ϴ˽ڵ�ɴ�
		m_strNoOpenReasonMsg = null;

		if (adddata == null) {
			SCMEnv.out("ILinkAddData::adddata����Ϊ�գ�ֱ�ӷ���");/* -=notranslate=- */
			return;
		}
		String strUpBillType = adddata.getSourceBillType();
		// ����Ϊ�ɹ�����
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
			// �˽ڵ��Ƿ�ɴ�
			if (voOrder == null) {
				MessageDialog.showHintDlg(
						this,
						NCLangRes.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* @res "��ʾ" */,
						NCLangRes.getInstance().getStrByID("40040301",
								"UPP40040301-000287")/*
													 * @res
													 * "�������ݲ������ɵ�����������ԭ��1������ҵ�������ߵ����ƻ�����δ���ɵ����ƻ���2�����ж����о�Ϊ�����ۿ����ԣ�3�������Ѿ���ȫ����"
													 */);
				return;
			}
			ArrFrmOrdUI billReferUI = new ArrFrmOrdUI("corderid",
					ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey(), ClientEnvironment.getInstance()
							.getUser().getPrimaryKey(), "4004020201", "null1",
					ScmConst.PO_Order, null, "nc.ui.po.pub.PoToRcQueDLG",
					ScmConst.PO_Arrive, this, false, true);
			// ��������
			billReferUI.loadDataForMsgCenter(new OrderVO[] { voOrder });
			//
			billReferUI.showModal();
			//
			if (billReferUI.getResult() == UIDialog.ID_OK) {
				ArriveorderVO[] retVOs = (ArriveorderVO[]) billReferUI
						.getRetVos();
				onExitFrmOrd(retVOs);
			} else {
				SCMEnv.out("ȡ���������ɲ���");/* -=notranslate=- */
				billReferUI.closeCancel();
				billReferUI.destroy();
			}
		}

	}

	/**
	 * ���򿪸ýڵ��ǰ�����������ڴ�����ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱 �������
	 * ��Ҫ�жϡ�ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱�Ľڵ㣬��Ҫʵ�ֱ������� �ڷ����ڽ��������жϡ�
	 * ������ݷ���ֵ������Ӧ�������������ֵΪһ���ǿ��ַ�������ô���಻��
	 * �ýڵ㣬ֻ��һ���Ի�������ʾ���ص��ַ������������ֵΪnull����ô������Դ� �����ڵ�һ���򿪸ýڵ㡣
	 * 
	 * �������ڣ�(2002-3-11 10:39:16)
	 * 
	 * @return java.lang.String
	 */
	protected String checkPrerequisite() {
		return m_strNoOpenReasonMsg;
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 **/
	public void doApproveAction(ILinkApproveData approvedata) {
		SCMEnv.out("���������ӿ�...");
		if (approvedata == null || approvedata.getBillID() == null) {
			SCMEnv.out("msgVo Ϊ�գ�ֱ�ӷ���!");
			SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
			SCMEnv.out(new Exception());
			SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
			return;
		}
		// ���ؿ�Ƭ
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		// ��ѯ����������
		ArriveorderVO vo = null;
		// ��¼����ID��getVo()��
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

		// ��¼��˾�뵥��������˾�Ƿ���ͬ
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// ���ð�ť��
		if (bCorpSameFlag) {
			setButtons(m_aryArrCardButtons);
			setM_strState("�������");
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
			setM_strState("��Ϣ����");
		}
		//
		setButtonsState();
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ������
	 **/
	public void doQueryAction(ILinkQueryData querydata) {
		SCMEnv.out("����������ӿ�...");

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
										"UPPSCMCommon-000270")/* "��ʾ" */,
								m_lanResTool.getStrByID("common",
										"SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				return;
			}
			//
			String strPkCorp = vo.getPk_corp();
			// ���յ���������˾���ز�ѯģ��
			RcQueDlg queryDlg = new RcQueDlg(this, getBusitype(), getFuncId(),
					getOperatorId(), strPkCorp);// ��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
			queryDlg.setDefaultValue("po_arriveorder.dreceivedate",
					"po_arriveorder.dreceivedate", "");
			queryDlg.initCorpsRefs();
			// ���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
			ConditionVO[] condsUserDef = queryDlg.getDataPowerConVOs(strPkCorp,
					RcQueDlg.REFKEYS);
			// ��֯�ڶ��β�ѯ���ݣ�����Ȩ�޺͵���PK����
			ArriveorderVO[] voaRet = ArriveorderHelper.queryAllArriveMy(
					condsUserDef, null, strPkCorp, null,
					"po_arriveorder.carriveorderid = '" + billID + "' ");
			if (voaRet == null || voaRet.length <= 0 || voaRet[0] == null) {
				MessageDialog
						.showHintDlg(
								this,
								NCLangRes.getInstance().getStrByID("SCMCOMMON",
										"UPPSCMCommon-000270")/* "��ʾ" */,
								m_lanResTool.getStrByID("common",
										"SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				setButtonsNull();
				return;
			}
			setCacheVOs(voaRet);
			setDispIndex(0);
			loadDataToCard();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */,
					e.getMessage());
			setButtonsNull();
			return;
		}
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// ���ð�ť��
		if (bCorpSameFlag) {
			setButtons(m_aryArrCardButtons);
			setButtonsCard();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��յ�ǰ���水ť
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
	 * �ϲ���ʾ����ӡ����
	 * 
	 * @since v50
	 */
	private void onCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this,
				m_lanResTool.getStrByID("4004020201", "UPT4004020201-000084")/*
																			 * @res
																			 * "�ϲ���ʾ"
																			 */);
		dlg.initData(getBillCardPanel(),
		// �̶�������
				new String[] { "cinventorycode", "cinventoryname",
						"cinventoryspec", "cinventorytype", "prodarea" },
				// ȱʡ������
				null,
				// �����
				new String[] { "nmoney", "narrvnum", "nelignum", "nnotelignum",
						"nwastnum" },
				// ��ƽ����
				null,
				// ���Ȩƽ����
				new String[] { "nprice" },
				// ������
				"narrvnum");
		dlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000039")/* @res "�ϲ���ʾ���" */);
	}

	/**
	 * ���ɨ�� ������ ���ֹ�Ө2012/8/29
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
			MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", "û��ѡ����Ҫɨ��Ĵ��!You need to scan inventory!");
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
				MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", "�ֿⲻ��Ϊ��!warehouse can not be empty!");
				return;
			}
			if ("008".equals(WhCode) || "013".equals(WhCode)) {//edit by shikun 2014-08-28 ���ǲֿ����Ϊ�գ�NULL�������
				cspaceid = String.valueOf(getBillCardPanel().getBodyValueAt(
						mEindex, "cspaceid"));
				if (cspaceid == null || cspaceid.equals("null")) {
					cspaceid = "";
				}
			}

		}
		
//		if (StampIsExist(pileno, Wh, cinventoryid, cspaceid)) {
//			MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", "���:" + pileno
//
//			+ "���ִ������Ѵ���!");
//			
//			return ;
//		}
		if (!pileno.equals("")) {

			if (PilenoList != null || PilenoList.toArray().length > 0) {
				if (PilenoList.indexOf(pileno) >= 0) {
					MessageDialog.showErrorDlg(this, "�ɹ�����Procurement arrival", "���The stack number:" + pileno

					+ "��ɨ���!has scanned!");
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
	 * ��¼ͬһ����Ŀ�ʼ�����ͽ������� ������ ���ֹ�Ө2012/8/29
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
					// list.add(0, initBillItem[0]);//ϵͳ���ɵ��ݵĴ������
					list.add(0, m_invid);// �������
					list.add(1, String.valueOf(index));// ��������ʱĳ������Ŀ�ʼ����
					list.add(2, String.valueOf(recount));// ��¼���������
					list.add(3, String.valueOf(recount - 1 + index));// ��¼��ǰ������ж�Ŵ���������
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
	 * ���ش���Ŀ�ʼ���� ������ ���ֹ�Ө2012/8/29
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
	 * ���ش���Ľ������� ������ ���ֹ�Ө2012/8/29
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
	 * ���¶�š�ʵ������ ������ ���ֹ�Ө2012/8/29
	 * 
	 * @since v50
	 */
	private void SetBodyNewValue(String pileno, UFDouble num, int sindex) {

		getBillCardPanel().setBodyValueAt("[���:" + pileno + "]", sindex,
				"vfree0");
		getBillCardPanel().setBodyValueAt(num, sindex, "narrvnum");

		voFree = new FreeVO();
		voFree.m_vfree0 = "[���:" + pileno + "]";
		voFree.m_vfreename1 = "���";
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
	 * ��λ�ֿ����Ĭ�ϻ�λ ������ ���ֹ�Ө2012/8/29
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
	 * ��ʼ��ȫ�ֱ��� ������ ���ֹ�Ө2012/8/29
	 * 
	 * @since v50
	 */
	private void InitGlobalVar() {
		oid_cmangid = null;

	}

	/**
	 * ��ʼ���ɹ������� ������ ���ֹ�Ө2012/8/29
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
			//MessageDialog.showErrorDlg(this, "ά��������", e.getMessage());
		}
		bo.setStatus(2);
		// testVo.setItem(j, bo);
		return bo;
	}

	/**
	 * �����Ҫ����ģ��������=�ۻ��ϸ����� -�ۻ�������� ������ ���ֹ�Ө2012/8/29
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
				//MessageDialog.showErrorDlg(this, "���鵽����Inspection documents", "����Ϊ�ջ���Ϊ0!");
				throw new BusinessException("����Ϊ�ջ���Ϊ0!Price is empty or 0!");
			  }
		    	if(innum==null||innum.equalsIgnoreCase("null")||new UFDouble(innum).compareTo(new UFDouble( "0"))==0)
		    	{
				throw new BusinessException("����Ϊ�ջ���Ϊ0!Quantity is empty or 0!");
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
	 * ���û�λid ������ ���ֹ�Ө2012/8/29
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
	 * ���û�λ���� ������ ���ֹ�Ө2012/8/29
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
	 * �ж϶���Ƿ���� ������ ���ֹ�Ө2012/8/29
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
	 * @����:���ع�˾���ϼ���˾����
	 * @author ���ֹ�Ө
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
	 * @����:�жϲֿ��Ƿ����û�λ����
	 * @author ���ֹ�Ө
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
	 * @����:У���λ �ֿ� ������
	 * @author ���ֹ�Ө
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public boolean checkVO(ArriveorderVO vo) {
		try {
		
			// ������
			try {
				checkFreeItemAndBatchAndSpaceInput(vo);
				
			} catch (ValidationException e) {
				// ��ʾ��ʾ
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
	  * @����:����
	  * @author ���ֹ�Ө
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
				throw new ValidationException("��ѯ���쳣�����Ϣ!Query exception inventory information!");
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
						
						errMessage+=Transformations.getLstrFromMuiStr("����@Document number&:"+vbillCode+"&�к�@Line number&:"+vo[i].m_crowno+"& ���@Inventories&"+invcode+"&�Ļ�λ����Ϊ��!@of cargo space can not be empty!")+"\n";
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
                        	errMessage+=Transformations.getLstrFromMuiStr("����@Document number&:"+vbillCode+"&�к�@Line number&:"+vo[i].m_crowno+"& ���@Inventories&"+invcode+"&"+Msg)+"\n";
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
				errMessage+=Transformations.getLstrFromMuiStr("����@Document number&:"+vbillCode+"&�к�@Line number&:"+vo[i].m_crowno+"& ���@Inventories&"+invcode+"&����졢�Ƿ���ݼ����������������ò���!����[������������]��<������Ϣ>�н������ã����߱ع�ѡ��һ��!@of the exemption, whether based on the test results storage properties set wrong! Materials production file]> <control information set (two must check one of)!")+"\n";
			}
			if(new UFBoolean(IsBathcMgrt).booleanValue())
			{
				String vbatch=String.valueOf(vo[i].getVproducenum());
				if(vbatch==null||vbatch.equals("")||vbatch.equals("null"))
				{
	
					errMessage+=Transformations.getLstrFromMuiStr("����@Document number&:"+vbillCode+"&�к�@Line number&:"+vo[i].m_crowno+"& ���@Inventories&"+invcode+"&�������κŹ��������κŲ���Ϊ��!@Enable batch number management, batch number can not be empty")+"\n";
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
	   
	        		 errMessage+=Transformations.getLstrFromMuiStr("����@Document number&:"+vbillCode+"&���@The inventory&"+invcode+"&��������@free term&"+String.valueOf(j)+"&����Ϊ��!@can not be empty!");
	        	 }
	         }
			 if(!errMessage.equals("")&&!errMessage.equalsIgnoreCase("null"))
				  throw new ValidationException(errMessage);
		 }
		
	  }

	 /**
	  * @����:��ѯ��� ������ �Ƿ����κŹ���
	  * @author ���ֹ�Ө
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
	    	UFBoolean fseparatespace=new UFBoolean(StringIsNullOrEmpty(invhm.get("fseparatespace"))?"N":String.valueOf(invhm.get("fseparatespace")));//�Ƿ񵥶����
	    	UFBoolean ffixedspace=new UFBoolean(StringIsNullOrEmpty(invhm.get("ffixedspace"))?"N":String.valueOf(invhm.get("ffixedspace")));//�Ƿ�̶���λ
	    	String cspaceid=String.valueOf(invhm.get("cspaceid"));
	    	if(ffixedspace.booleanValue()&&!space.equalsIgnoreCase(cspaceid))
	    	{
	    		return "ffҪ���Ź̶���λ��";
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
	    	        		 return "fsҪ�󵥶���ţ�";
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
	//MessageDialog.showErrorDlg(this,"����Error", e.getMessage());
	//return false;
	throw e;
}
	
	
}
//ncm begin liuydc
/*
 * ���ղֿ���˻�λ����
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

// edit by yqq 2016-11-2 XML���밴ť, ����
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
		// ��ȡ��Ԫ��
		Element root = document.getRootElement();
		List po_order = root.elements("po_order"); // po_order�ڵ��б�

		for (Iterator<Element> i = po_order.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			org.dom4j.Element word;
			if (element.getName().equals("po_order_head")) {
				for (Iterator k = word.elementIterator(); k.hasNext();) { // ����
																			// name
																			// mean
																			// lx�ڵ�

				}
			}
			if (element.getName().equals("po_order_body")) {
				for (Iterator k = word.elementIterator(); k.hasNext();) { // ����
																			// name
																			// mean
																			// lx�ڵ�

				}

			}

		}*/
	}
}

}
