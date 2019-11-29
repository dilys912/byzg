package nc.ui.po.oper;
//ICheckRetVOУ��ӿ�
import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Properties;

import javax.servlet.jsp.jstl.core.Config;

import java.util.Properties;
import java.util.Vector;
import java.io.OutputStreamWriter;
import javax.swing.JFileChooser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;

import nc.bs.framework.common.PropertyUtil;
import nc.bs.framework.common.RuntimeEnv;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.trade.business.HYPubBO;

import nc.itf.pub.rino.IPubDMO;

import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;

import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.manage.RefCellRender.eventHandler;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ic.pub.barcodeoffline.ExcelReadCtrl;
import nc.ui.ic.pub.bill.GeneralBillHelper;

import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pi.invoice.OnQueryAction;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.ContractClassParse;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.po.pub.PoListPanel;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;

import nc.utils.modify.is.IdetermineService;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.handler.EventHandler;
import nc.uif.pub.exception.UifException;
import nc.utils.modify.is.IdetermineService;
import nc.vo.arap.h101.FksqdBVO;
import nc.vo.arap.h101.FksqdVO;
import nc.vo.arap.pub.HeadVO;
import nc.vo.bd.b47.PaytermItemVO;
import nc.vo.ic.pub.barcodeoffline.ExcelFileVO;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.check.CHECKVO;

import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PUMessageVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import sun.security.jca.GetInstance;

import com.ibm.wsdl.util.StringUtils;

import java.io.InputStreamReader;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pfxx.util.FileUtils;


public class OrderUI extends PoToftPanel  
  implements ICheckRetVO, IBillExtendFun, ILinkMaintain, ILinkAdd, ILinkApprove, ILinkQuery,BillEditListener
{
 
  private OrderUIQueDlg m_condition = null;//��ѯ����
  private int res;
  private File txtFile = null;//���������ı���ʽ�Ĳɹ�����
  private UITextField txtfFileUrl = null;
  private int xml;
  private File xmlFile = null;//��������xml��ʽ�ɹ�����

  public OrderUI()
  {
  }

  public String getTitle()
  {
    return getPoCardPanel().getTitle();
  }
 
  //operator��¼��
  public OrderUI(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    initMsgCenter();//������壬���ð�ť

    getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);//�õ��������ֶ���Ȼ����ʾ����
    getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
    getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);
    try {
      getBufferVOManager().resetVOs(
        new OrderVO[] { OrderHelper.queryOrderVOByKey(billID) });//���ݵ��ݺŲ�ѯ������vo�󽫶���vo����
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        OrderPubVO.returnHintMsgWhenNull(e.getMessage()));
    }

    if (getBufferLength() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), //this������ǰ���������
        NCLangRes.getInstance().getStrByID("4004020201", 
        "UPP4004020201-000023"));
      getPoCardPanel().addNew();//���������ӵ��µ������
      return;
    }

    getBufferVOManager().setVOPos(0);//Ϊm_nPos��ֵ

    getPoCardPanel().displayCurVO(
      getOrderViewVOAt(getBufferVOManager().getVOPos()), false);//����ʾ��ǰ�ĵ���

    getPoCardPanel().setEnabled(false);//���ݲ������±༭

    PoPublicUIClass.setCuserId(new OrderVO[] { 
      getOrderViewVOAt(getBufferVOManager().getVOPos()) });//����id
  }
  

  public OrderUI(FramePanel panel) {
    super(panel);
  }

  protected PoToftPanelButton getBtnManager() {
    if (this.m_btnManager == null) {
      this.m_btnManager = new OrderUIButton(this);

      PfUtilClient.retBusinessBtn(this.m_btnManager.btnBillBusitype, 
        getCurPk_corp(), "21");
      try
      {
        ContractClassParse.resetButton(getParameter(
          ContractClassParse.getParaName()), 
          this.m_btnManager.btnBillContractClass);
      } catch (Exception e) {
        SCMEnv.out("��Ŀ�������ο���BUG��" + e.getMessage());
      }
    }
    return this.m_btnManager;
  }

  protected PoCardPanel getInitPoCardPanel(POPubSetUI2 setUi) {
    return new PoCardPanel(this, PoPublicUIClass.getLoginPk_corp(), "21", 
      setUi);
  }

  protected PoListPanel getInitPoListPanel(POPubSetUI2 setUi) {
    return new PoListPanel(this, PoPublicUIClass.getLoginPk_corp(), "21", 
      setUi);
  }

  public PoQueryCondition getPoQueryCondition() {
    if (this.m_condition == null) {
      this.m_condition = new OrderUIQueDlg(this, "4004020201", 
        PoPublicUIClass.getLoginPk_corp(), 
        PoPublicUIClass.getLoginUser());

      this.m_condition.setShowPrintStatusPanel(true);
    }
    return this.m_condition;
  }

  protected OrderVO[] getQueriedVOHead() throws Exception {
    NormalCondVO[] normalVos = ((OrderUIQueDlg)getPoQueryCondition())
      .getNormalVOs(false);
    ArrayList listVos = new ArrayList();
    if ((normalVos != null) && (normalVos.length > 0)) {
      int iLen = normalVos.length;
      for (int i = 0; i < iLen; i++) {
        if (normalVos[i] == null) {
          break;
        }
        listVos.add(normalVos[i]);
      }
      NormalCondVO voTmp = new NormalCondVO();
      voTmp.setKey("ConstrictClosedLines");
      voTmp.setValue("Y");
      listVos.add(voTmp);

      if (!((OrderUIQueDlg)getPoQueryCondition()).isClosedSelected())
        voTmp.setValue("Y");
      else if (!((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()) {
        voTmp.setValue("N");
      }

      if (((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()) {
        voTmp.setValue("N");
        NormalCondVO voTmp1 = new NormalCondVO();
        voTmp1.setKey("OnlyClosedLines");
        voTmp1.setValue("Y");
        listVos.add(voTmp1);
      }

      voTmp = new NormalCondVO();
      voTmp.setKey("����Ա");
      voTmp.setValue(PoPublicUIClass.getLoginUser());
      listVos.add(voTmp);

      normalVos = 
        (NormalCondVO[])listVos
        .toArray(new NormalCondVO[listVos.size()]);
    }

    ConditionVO[] voaUserDefined = getPoQueryCondition().getConditionVO();

    return OrderHelper.queryOrderArrayOnlyHead(normalVos, voaUserDefined);
  }

  private OrderUIButton getThisBtnManager() {
    return (OrderUIButton)getBtnManager();
  }

  public AggregatedValueObject getVo() throws Exception {
    return getOrderViewVOAt(getBufferVOManager().getVOPos());
  }

  private void initMsgCenter() {
    setCurPk_corp(PoPublicUIClass.getLoginPk_corp());//���õ�ǰ��¼�û��ĵ�λ����

    setLayout(new BorderLayout());//���ò���
    add(getPoCardPanel(), "Center");//�����������������ӵ�����м�

    setButtons(getThisBtnManager().getBtnaMsgCenter());//�������еİ�ť�����ȵõ���ǰ��ť�Ĺ����࣬Ȼ���ȡ��ť����Ϣ���ģ�

    ContractClassParse.resetButton(getParameter(ContractClassParse.getParaName()), getThisBtnManager().btnBillContractClass);//���ð�ť
  }

  protected void onButtonClickedBill(ButtonObject bo) {
    if (bo == getThisBtnManager().btnMsgCenterAudit)//�����ǰ����İ�ť�����
      onMsgCenterAudit();
    else if (bo == getThisBtnManager().btnMsgCenterUnAudit)//�����ǰ������Ƿ����
      onMsgCenterUnAudit();
    else if (PuTool.isExist(getExtendBtns(), bo))//?
      onExtendBtnsClick(bo);
    else if (bo.getCode().equals("��Ӧ�̲�ѯ"))
      onBoGYSquery();
    else if (bo.getCode().equals("����Ԥ�������뵥"))//Ԥ�������뵥�����¼�  by zy 2019-07-25
		try {
			onAdvance();
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	else if (bo.getCode().equals("������¼"))
		OnHxSave();	
	else if (bo == getBtnManager().btnBillSave)
        onBoSave();   
       
    else
      super.onButtonClickedBill(bo);
  }
   
  
  
  
  // add by yqq 20160714 �ɹ�����������۱�����Ϊ0���ܱ���
	private void onBoSave() { 
		int size = this.getPoCardPanel().getBillModel().getRowCount();
		if(size>0){
			for (int i = 0; i < size; i++) {
				UFDouble rate = getPoCardPanel().getBodyValueAt(i,
						"nexchangeotobrate") == null ? new UFDouble(0.00000)
						: (UFDouble) (getPoCardPanel().getBodyValueAt(i,
								"nexchangeotobrate")); 
				if (rate.doubleValue()==0) {
					showWarningMessage("�����۱����ʲ���Ϊ0");
					return;
				} else {
					continue;
				}
			}
		}
		super.onBillSave();
	} 
//end by yqq
  public void onBoGYSquery()
  {
    getPoCardPanel().getBodyItems();
    int i = getPoCardPanel().getBillTable().getSelectedRow();
    Object cbaseid = getPoCardPanel().getBodyValueAt(i, "cbaseid");
    if (cbaseid != null) {
      GongyingshangQueryModel model = new GongyingshangQueryModel(
        cbaseid.toString());
      if ((model.getData() == null) || (model.getData().size() < 1)) {
    	  
        NCOptionPane.showConfirmDialog(this, 
          "û�в�ѯ���������!No query to the data!");
        return;
      }if (cbaseid == null) {
        NCOptionPane.showConfirmDialog(this, 
          "��ѡ���������Select the table body stock!");
      }

      UIRefPane pane = new UIRefPane();
      pane.setRefModel(model);
      pane.onButtonClicked();
      String gysnames = pane.getRefName();
      if (gysnames == null) {
        return;
      }
      BillItem item = getPoCardPanel().getHeadItem("cvendormangname");
      if (item != null) {
        item.setValue(gysnames);
      }
      try
      {
        item = getPoCardPanel().getHeadItem("cvendorbaseid");
        if (item != null) {
          UIRefPane panes = (UIRefPane)item.getComponent();
          String cuBasPk = pane.getRefPK();
          String cuManPk = (String)HYPubBO_Client.findColValue(
            "bd_cumandoc", "pk_cumandoc ", " pk_cubasdoc='" + 
            cuBasPk + 
            "' and pk_corp='" + 
            ClientEnvironment.getInstance()
            .getCorporation().getPk_corp() + 
            "'");
          panes.setPK(cuManPk);
        }
        item = getPoCardPanel().getHeadItem("cvendormangid");
        if (item != null) {
          UIRefPane panes = (UIRefPane)item.getComponent();
          String cuBasPk = pane.getRefPK();
          String cuManPk = (String)HYPubBO_Client.findColValue(
            "bd_cumandoc", "pk_cumandoc ", " pk_cubasdoc='" + 
            cuBasPk + 
            "' and pk_corp='" + 
            ClientEnvironment.getInstance()
            .getCorporation().getPk_corp() + 
            "'");
          panes.setPK(cuManPk);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }


  @SuppressWarnings("unchecked")
  private void onMsgCenterAudit() 
  {
    OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
    PoPublicUIClass.setCuserId(new OrderVO[] { vo });

  

    OrderVO[] VOs = new OrderVO[1];
    VOs[0] = vo;

    HashMap mapAuditInfo = new HashMap();
    ArrayList listAuditInfo = null;
    OrderHeaderVO headVO = null;
    /**
     * start 2019-7-23
     * У��Ԥ����ѡ��
     * by zsh
     */
    
    //��ȡ�Ƿ�Ԥ�����ֶΣ��Ƿ�   
    String prepaymny =  vo.getHeadVO().getVdef17();
    //��ȡ������ԴID
    String corderid = vo.getHeadVO().getCorderid();
    //��ȡ��˾
    String  corp   = vo.getHeadVO().getPk_corp();
   
    
    //��ȡ��˾�Ƿ��ǹ��ڵĽ��
    IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
    Boolean result = idetermineService.check(corp);
  
  
   //�ж��Ƿ��ǹ��ڵĹ�˾
    if(result==true){
    //�ж�Ԥ�������뵥�Ƿ����
   
    if("Y".equals(prepaymny.trim())){
    	//���Ϊ�����ж��Ƿ��������뵥����Դ����ID   
    	
    	try {
    		
    		List list = null;
    		StringBuffer sql = new StringBuffer();
    		sql.append("select zfje from arap_fksqd  where  vdef1= '"+corderid+"' and (nvl(dr,0) =0)");
        	IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			list = (List<Object>) uap.executeQuery(sql.toString(), new MapListProcessor());
        	//��ѯ����Ƿ���ںͽ��
        	StringBuffer sqls = new StringBuffer();
        	sqls.append("select bbye from arap_djfb where  zyx20='"+corderid+"' ");
        	List lists = (List<Object>) uap.executeQuery(sqls.toString(), new MapListProcessor());
        	
        	if(list==null||list.size()==0){
	    		//������Դ����ID����Դ���Ϣ
	    		showWarningMessage("��������Ԥ�������뵥");
	    		return;
	    	}
        	if(lists==null||lists.size()==0){
        		//������Դ����ID����Դ���Ϣ
	    		showWarningMessage("�������ɸ��");
	    		return;
        	}
        	//ȡ��Ԥ�������뵥�еĽ��͸���Ľ��
        	Map map =(Map) list.get(0);
        	Map maps = (Map) lists.get(0);
        	double bbye = Double.valueOf(maps.get("bbye").toString());
        	double zfje = Double.valueOf(map.get("zfje").toString());
        	System.out.println("������"+bbye);
        	System.out.println("֧����"+zfje);
//        	//���бȽ�
        	if(bbye!=zfje){
        		//������Դ����ID����Դ���Ϣ
	    		showWarningMessage("�������뵥�еĽ���Ԥ�����б�������");
	    		return;
        	}
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	
      }
    }
  

   
	try {
		 IUAPQueryBS  uap2s = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		    StringBuffer sql2 = new StringBuffer();
			sql2.append("select vordercode from po_order where  corderid='"+corderid+"'");
			List list2 = (List<Object>) uap2s.executeQuery(sql2.toString(), new MapListProcessor());
			String vordercode = ((Map)list2.get(0)).get("vordercode").toString();
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			StringBuffer sql=new StringBuffer();
			 sql.append("update arap_djfb set ddh='"+vordercode+"' where zyx20='"+corderid+"'");
		ipubdmo.executeUpdate(sql.toString());
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    for (int i = 0; i < VOs.length; i++) {
      headVO = VOs[i].getHeadVO();

      if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
        listAuditInfo = new ArrayList();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
      }
      headVO.setCauditpsn(
        getClientEnvironment().getUser().getPrimaryKey());
      headVO.setDauditdate(getClientEnvironment().getDate());
    }
    try {
      String strErr = PuTool.getAuditLessThanMakeMsg(VOs, "dorderdate", 
        "vordercode", getClientEnvironment().getDate(), "21");
      if (strErr != null) {
        throw new BusinessException(strErr);
      }

      Object[] objs = new Object[1];
      objs[0] = new ClientLink(getClientEnvironment());

      PfUtilClient.processBatchFlow(this, "APPROVE", "21", 
        getClientEnvironment().getDate().toString(), VOs, objs);

      if (!PfUtilClient.isSuccess())
      {
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < VOs.length; i++) {
            headVO = VOs[i].getHeadVO();
            if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
              headVO.setCauditpsn(null);
              headVO.setDauditdate(null);
            } else {
              listAuditInfo = (ArrayList)mapAuditInfo.get(
                headVO.getPrimaryKey());
              headVO.setCauditpsn((String)listAuditInfo.get(0));
              headVO.setDauditdate((UFDate)listAuditInfo.get(1));
            }
          }
        }
        return;
      }
      String billID = vo.getParentVO().getPrimaryKey();
      getBufferVOManager().resetVOs(
        new OrderVO[] { OrderHelper.queryOrderVOByKey(billID) });

      getBufferVOManager().setVOPos(0);

      getPoCardPanel().displayCurVO(
        getOrderViewVOAt(getBufferVOManager().getVOPos()), false);

      setButtonsStateBrowse();

      showHintMessage(NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", 
        "UPPSCMCommon-000236"));
    } catch (Exception e) {
      PuTool.outException(this, e);
    }
  }

  private void onMsgCenterUnAudit() {
    OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
    PoPublicUIClass.setCuserId(new OrderVO[] { vo });

    OrderVO[] VOs = new OrderVO[1];
    VOs[0] = vo;

    HashMap mapAuditInfo = new HashMap();
    ArrayList listAuditInfo = null;
    OrderHeaderVO headVO = null;
    for (int i = 0; i < VOs.length; i++) {
      headVO = VOs[i].getHeadVO();

      if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
        listAuditInfo = new ArrayList();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
      }
      headVO.setCauditpsn(
        getClientEnvironment().getUser().getPrimaryKey());
      headVO.setDauditdate(getClientEnvironment().getDate());
    }
    try {
      String strOpr = getClientEnvironment().getUser().getPrimaryKey();

      PfUtilClient.processBatchFlow(this, "UNAPPROVE" + strOpr, "21", 
        getClientEnvironment().getDate().toString(), VOs, null);

      if (!PfUtilClient.isSuccess())
      {
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < VOs.length; i++) {
            headVO = VOs[i].getHeadVO();
            if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
              headVO.setCauditpsn(null);
              headVO.setDauditdate(null);
            } else {
              listAuditInfo = (ArrayList)mapAuditInfo.get(
                headVO.getPrimaryKey());
              headVO.setCauditpsn((String)listAuditInfo.get(0));
              headVO.setDauditdate((UFDate)listAuditInfo.get(1));
            }
          }
        }
        return;
      }
      String billID = vo.getParentVO().getPrimaryKey();
      getBufferVOManager().resetVOs(
        new OrderVO[] { OrderHelper.queryOrderVOByKey(billID) });

      getBufferVOManager().setVOPos(0);

      getPoCardPanel().displayCurVO(
        getOrderViewVOAt(getBufferVOManager().getVOPos()), false);
      updateButtons();

      setButtonsStateBrowse();

      showHintMessage(NCLangRes.getInstance()
        .getStrByID("common", 
        "UCH011"));
    } catch (Exception e) {
      PuTool.outException(this, e);
    }
  }

  public ButtonObject[] getExtendBtns() {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo) {
  }

  public void setExtendBtnsStat(int iState) {
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    SCMEnv.out("���붩��ά���ӿ�...");

    String billID = maintaindata.getBillID();

    setCurPk_corp(PoPublicUIClass.getLoginPk_corp());

    setLayout(new BorderLayout());
    add(getPoCardPanel(), "Center");

    getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
    getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
    getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);
    try {
      getBufferVOManager().resetVOs(
        new OrderVO[] { OrderHelper.queryOrderVOByKey(billID) });
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        OrderPubVO.returnHintMsgWhenNull(e.getMessage()));
    }

    String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
    String strPrayCorpId = getOrderViewVOAt(0) == null ? null : 
      getOrderViewVOAt(0).getHeadVO().getPk_corp();
    boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);

    if (getBufferLength() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("4004020201", 
        "UPP4004020201-000023"));
      if (bSameCorpFlag)
        getPoCardPanel().addNew();
      else {
        setButtonsNull();
      }
      return;
    }

    getBufferVOManager().setVOPos(0);

    getPoCardPanel().displayCurVO(
      getOrderViewVOAt(getBufferVOManager().getVOPos()), false);

    PoPublicUIClass.setCuserId(new OrderVO[] { 
      getOrderViewVOAt(getBufferVOManager().getVOPos()) });

    if (!bSameCorpFlag) {
      setButtonsNull();
      return;
    }

    OrderVO voOrder = getOrderViewVOAt(getBufferVOManager().getVOPos());
    if (voOrder.getHeadVO().isAuditted()) {
      setButtonsStateBrowse();
      return;
    }

    onBillModify();
  }

  public void doAddAction(ILinkAddData adddata) {
    SCMEnv.out("���붩�������ӿ�...");
    if (adddata == null) {
      SCMEnv.out("���ε��òɹ�������������ʱδ��ȷ����������ݣ�ֱ�ӷ���!");
      return;
    }

    String strUpBillType = adddata.getSourceBillType();
    AggregatedValueObject[] vos = (AggregatedValueObject[])null;

    if ((adddata.getUserObject() != null) && 
      ((adddata.getUserObject() instanceof PUMessageVO))) {
      PUMessageVO msgVo = (PUMessageVO)adddata.getUserObject();
      strUpBillType = "28";
      vos = new AggregatedValueObject[] { msgVo.getAskvo() };
    }

    processAfterChange(strUpBillType, vos);
  }

  public void doApproveAction(ILinkApproveData approvedata) {
    SCMEnv.out("���붩�������ӿ�...");

    initMsgCenter();
    try {
      OrderVO voOrder = OrderHelper.queryOrderVOByKey(approvedata
        .getBillID());
      getBufferVOManager().resetVOs(
        new OrderVO[] { voOrder == null ? null : voOrder });
    } catch (Exception e) {
      int iBtnLen = getThisBtnManager().getBtnaMsgCenter().length;
      for (int i = 0; i < iBtnLen; i++) {
        getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false);
      }
      updateButtons();

      PuTool.outException(this, e);
      return;
    }

    if (getBufferLength() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("4004020201", 
        "UPP4004020201-000023"));
      getPoCardPanel().addNew();

      int iBtnLen = getThisBtnManager().getBtnaMsgCenter().length;
      for (int i = 0; i < iBtnLen; i++) {
        getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false);
      }
      updateButtons();
      return;
    }

    getBufferVOManager().setVOPos(0);

    OrderVO voCur = getOrderViewVOAt(getBufferVOManager().getVOPos());

    PoPublicUIClass.setCuserId(new OrderVO[] { voCur });

    getPoCardPanel().displayCurVO(voCur, false);
    getPoCardPanel().setEnabled(false);

    boolean bFrozenFlag = false;
    if (voCur.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE) == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("SCMCOMMON", 
        "UPPSCMCommon-000384"));
      bFrozenFlag = true;
    }
    if (bFrozenFlag) {
      setButtonsNull();
      return;
    }

    boolean bCorpSameFlag = getCorpPrimaryKey().equals(voCur.getPk_corp());

    if (bCorpSameFlag) {
      setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
      setButtonsStateBrowse();
    } else {
      setButtons(getThisBtnManager().getBtnaMsgCenter());
    }
  }

  public void doQueryAction(ILinkQueryData querydata) {
    SCMEnv.out("���붩��������ӿ�...");

    String billID = querydata.getBillID();

    setCurPk_corp(PoPublicUIClass.getLoginPk_corp());

    setLayout(new BorderLayout());
    add(getPoCardPanel(), "Center");

    getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
    getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
    getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);
    try {
      getBufferVOManager().resetVOs(
        new OrderVO[] { OrderHelper.queryOrderVOByKey(billID) });
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        OrderPubVO.returnHintMsgWhenNull(e.getMessage()));
    }

    if (getBufferLength() == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("4004020201", 
        "UPP4004020201-000023"));
      getPoCardPanel().addNew();
      return;
    }

    getBufferVOManager().setVOPos(0);

    OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());

    String strPkCorp = vo.getPk_corp();

    OrderUIQueDlg queryDlg = new OrderUIQueDlg(this, getModuleCode(), 
      strPkCorp, getClientEnvironment().getUser().getPrimaryKey());

    queryDlg.initCorpRefsDataPower("po_order_corp", 
      OrderUIQueDlg.getPowerCodes(), new String[] { strPkCorp });

    ConditionVO[] voaPower = queryDlg.getDataPowerConVOs(strPkCorp, 
      PoQueryCondition.getPowerCodesOrderUI());

    ConditionVO[] voaUserDefined = (ConditionVO[])null;
    ConditionVO voHid = new ConditionVO();
    voHid.setFieldCode("po_order.corderid");
    voHid.setFieldName("�ɹ�����ͷID");
    voHid.setOperaCode("=");
    voHid.setOperaName("����");
    voHid.setValue(billID);
    if ((voaPower == null) || (voaPower.length == 0)) {
      voaUserDefined = new ConditionVO[1];
      voaUserDefined[0] = voHid;
    } else {
      voaUserDefined = new ConditionVO[voaPower.length + 1];
      for (int i = 0; i < voaPower.length; i++) {
        voaUserDefined[i] = voaPower[i];
      }
      voaUserDefined[voaPower.length] = voHid;
    }

    NormalCondVO voNormal = new NormalCondVO();
    voNormal.setKey("����ѯ��˾");
    voNormal.setValue(strPkCorp);
    OrderVO[] voaRet = (OrderVO[])null;
    try {
      voaRet = OrderHelper.queryOrderArrayOnlyHead(
        new NormalCondVO[] { voNormal }, voaUserDefined);
    } catch (Exception e) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), e.getMessage());

      setButtonsNull();
      return;
    }
    if ((voaRet == null) || (voaRet.length <= 0) || (voaRet[0] == null)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000161"));

      return;
    }

    getPoCardPanel().displayCurVO(vo, false);

    getPoCardPanel().setEnabled(false);

    PoPublicUIClass.setCuserId(new OrderVO[] { 
      getOrderViewVOAt(getBufferVOManager().getVOPos()) });

    boolean bFrozenFlag = false;
    if (vo.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE) == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance()
        .getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), 
        NCLangRes.getInstance().getStrByID("SCMCOMMON", 
        "UPPSCMCommon-000384"));
      bFrozenFlag = true;
    }
    if (bFrozenFlag) {
      setButtonsNull();
      return;
    }
    boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());

    if (bCorpSameFlag) {
      setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
      setButtonsStateBrowse();
    } else {
      setButtonsNull();
    }
  }

  private void setButtonsNull() {
    ButtonObject[] objs = getButtons();
    int iLen = objs == null ? 0 : objs.length;
    for (int i = 0; i < iLen; i++) {
      if (objs[i] == null) {
        break;
      }
      objs[i].setVisible(false);
      updateButton(objs[i]);
    }
  }
  

  /**
   * zy
   * �жϲ�����Ԥ�������뵥
   * 2019-07-25
 * @throws UifException 
   * */
  public void onAdvance() throws UifException
  {
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result)//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
	  {
		  OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());//��ȡ�ɹ�����-��������-ά����������VO
		  String advance = vo.getHeadVO().getVdef17();//��ȡ���Ƿ�Ԥ���ѡ���ֵ
		  if("Y".equals(advance))//���ݻ�ȡ����ѡ��ֵ�����ж�
		  {
			  String billno=null;
			  String corderId = vo.getHeadVO().getCorderid();//��ȡ�ɹ������ж���id
			  String sql = "select djbh from arap_fksqd where vdef1='"+corderId+"' and nvl(dr,0)=0 and pk_corp = "+corp+"";
			  IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			  ColumnListProcessor alp = new ColumnListProcessor();//��ȡ��Ҫ���ֶ�
			  List djbh = null;
			    try {
			    	djbh = (List) query.executeQuery(sql, alp);//���ؽ��
			    	if(djbh!=null && djbh.size()>0){
			    		billno = djbh.get(0) == null ? "" : djbh.get(0).toString();
			    	}
			    	
			    } catch (Exception e2) {
			     // TODO: handle exception
			     e2.printStackTrace();
			    }
			    if(djbh.size() == 0)//�ж�Ԥ�������뵥���Ƿ������ɴ˲ɹ�������Ԥ�������뵥
			    {
			    	String fkxx = vo.getHeadVO().getCtermprotocolid();//��ȡ�ɹ�������	������Э�顿ѡ���ֵ��Э���
			    	String receivingSql = "select m.jhyfk,h.acclimited from bd_payterm m inner join bd_paytermch h on m.pk_payterm=h.pk_payterm " +
			    			"where m.pk_payterm='"+fkxx+"'and nvl(m.dr,0)=0 and nvl(h.dr,0)=0";//����Э��Ų�ѯ��Ԥ������ʺ���������
			    	IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			    	MapListProcessor getReceiving = new MapListProcessor();
			    	List receivingList = null;
			    	try {
						receivingList = (List) receiving.executeQuery(receivingSql, new MapListProcessor());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Map receivingMap = (Map) receivingList.get(0);
					String jhyfk = null == receivingMap.get("jhyfk")
					|| "".equals(receivingMap.get("jhyfk"))? "0" : receivingMap.get("jhyfk").toString();//Ԥ������ʣ���*0.01��String���ͣ�
			    	String dayafterfp = null == receivingMap.get("acclimited") 
			    	|| "".equals(receivingMap.get("acclimited"))? "0" : receivingMap.get("acclimited").toString();//����������String���ͣ�
			    	double value = Double.valueOf(jhyfk);//���������������ת��String->double
			    	double ratio = value*0.01;//�������Ԥ������ʣ�double��
			    	Integer date = Integer.valueOf(dayafterfp);//�����������������int��
			    	//��Դ���ݺţ�vdef1��Ϊ�ɹ������������ɹ�����id��corderid����������ȡ��Ϊ��corderId
			    	OrderItemVO[] bvos = vo.getBodyVO();//��ȡ�ɹ���������vo����������
			    	OrderItemVO body = bvos[0];//�ö�������������ֵ
			    	UFDouble money = body.getNoriginalcurmny();//�ɹ��������������ֵ
			    	//��ȡ�ɹ�������������
			    	String bts = body.getTs();//��ȡ����ʱ���		    	
			    	UFDouble nmoney = body.getNoriginaltaxpricemny();//�����˰�ϼ�
			    	UFDouble payRatio = new UFDouble(ratio);//��Ԥ���������doubleתΪUFDouble
			    	UFDouble prepaymentRatio = payRatio.multiply(nmoney);//Ԥ������ʳ˱����˰�ϼƵ�Ԥ������
			    	UFDate uDate = vo.getHeadVO().getDorderdate();//��ȡ�ɹ�������ͷ��������
			    	UFDate afterDay = uDate.getDateAfter(date);//��ȡ�ƻ�֧������
			    	//ֱ�����������뵥������ֵ
			  	  	IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			  	  	//�����������ͷ��ֵ
			  	  	FksqdVO fvoHead = new FksqdVO();
			  	  	billno = new HYPubBO().getBillNo("H101", vo.getHeadVO().getPk_corp(), null, null);
					fvoHead.setDjbh(billno);
			  	  	fvoHead.setPk_corp(vo.getHeadVO().getPk_corp());//pk_cope
			  	  	fvoHead.setSkkhh(vo.getHeadVO().getCaccountbankid());//�տ����
			  	  	fvoHead.setSkzh(vo.getHeadVO().getAccount());//�տ��˺�
			  	  	fvoHead.setSqbm(vo.getHeadVO().getCdeptid());//���벿��
			  	  	fvoHead.setTs(vo.getHeadVO().getTs());//ʱ���
			  	  	fvoHead.setVdef1(corderId);//�Զ�����1����Դ����id
			  	  	fvoHead.setZfje(prepaymentRatio);//Ԥ����֧�����
			  	  	fvoHead.setJhzfrq(afterDay);//�ƻ�֧������
			  	  	fvoHead.setVoperatorid(PoPublicUIClass.getLoginUser());//�Ƶ���
			  	  	String hid = null;
			  	  	try {
			  				hid=ivo.insertVO(fvoHead);
			  			} catch (BusinessException e) {
			  				// TODO Auto-generated catch block
			  				showWarningMessage(e.getMessage());
			  			}
			  		//���������������ֵ
			  		FksqdBVO fbvo = new FksqdBVO();
			  		fbvo.setVmemo(body.getVmemo());//��ע
			  		fbvo.setPk_corp(body.getPk_corp());//pk_cope
			  		fbvo.setPk_bz(body.getCcurrencytypeid());//ԭ�ұ���id
			  		fbvo.setBbhl(body.getNtaxmny());//ԭ���һ��ʣ����Ϊ˰�
			  		fbvo.setFbhl(body.getNexchangeotoarate());//���һ���
			  		fbvo.setHsdj(body.getNorgtaxprice());//ԭ�Һ�˰����
			  		fbvo.setSl(body.getNtaxrate());//˰��
			  		fbvo.setKslb(body.getIdiscounttaxtype() == null ? "" : body.getIdiscounttaxtype().toString());//��˰���
			  		fbvo.setPk_xmmc(body.getCprojectid());//��Ŀ����
			  		fbvo.setPk_sybm(body.getCusedeptid());//ʹ�ò���
			  		fbvo.setDfsl(body.getNordernum());//��������
			  		fbvo.setPk_sfkxy(dayafterfp + "��");//�ո���Э��
			  		fbvo.setSl(body.getNtaxrate());//˰��
			  		fbvo.setQxrq(uDate == null ? "" : uDate.toString());//��Ч����
			  		fbvo.setSkyhzh(vo.getHeadVO().getAccount());//�տ������˺�
			  		UFDouble dMoney = body.getNorgtaxprice().multiply(body.getNordernum());//����ԭ�Ҵ�����������*��˰����
			  		fbvo.setPk_gys(vo.getHeadVO().getCvendorbaseid());//��Ӧ�̻���id
			  		fbvo.setPk_bm(vo.getHeadVO().getCdeptid());//����������Pk_bm��
			  		fbvo.setDfybje(dMoney);//����ԭ�ҽ��
			  		fbvo.setDfbbje(dMoney);//�������ҽ��
			  		
			  	    fbvo.setPk_fksqd(hid);
			  	    try {
			  			ivo.insertVO(fbvo);
			  			showWarningMessage("����Ԥ�������뵥�ɹ�������Ϊ����"+billno+"��");
			  		} catch (BusinessException e1) {
			  			// TODO Auto-generated catch block
			  			showWarningMessage(e1.getMessage());
			  		}
			    }else{
			    	
			    	showWarningMessage("�ö���������Ԥ�������뵥������Ϊ����"+billno+"��");
			    }
		  }else if("N".equals(advance))
		  {
			  showWarningMessage("��ǰ������������Ԥ�������뵥");
		  }
	  }
  }
  //end
  
  /*
   * ljy  
   * �ɹ���������
   * 2014-09-22
   */

  public void onExcel()
  {
    UIFileChooser fileChooser = new UIFileChooser();
    fileChooser.setAcceptAllFileFilterUsed(true);
    this.res = fileChooser.showOpenDialog(this);
    if (this.res == 0) {
      getTFLocalFile().setText(
        fileChooser.getSelectedFile().getAbsolutePath());
      this.txtFile = fileChooser.getSelectedFile();
      String filepath = this.txtFile.getAbsolutePath();
      final WriteExcel exceldata = new WriteExcel();
      WriteExcel.creatFile(filepath);
      Runnable checkRun = new Runnable()
      {
        public void run() {
          BannerDialog dialog = new BannerDialog(OrderUI.this.getParent());
          dialog.start();
          try
          {
            exceldata.readData(0);
            OrderVO[] vo = WriteExcel.vo_hb1;
            HashMap map2 = new HashMap();
          for (int i = 0; i < vo.length; i++) {
            OrderVO voi = vo[i];
            String dindh = voi.getHeadVO().getVordercode();
            if (!map2.containsKey(dindh)) {
              map2.put(dindh, voi);
            } else {
              OrderItemVO[] bvo = (OrderItemVO[])voi.getChildrenVO();
              OrderVO aggvo = (OrderVO)map2.get(dindh);
              OrderItemVO[] aggbvo = (OrderItemVO[])aggvo.getChildrenVO();
              OrderItemVO[] newvo = new OrderItemVO[bvo.length + aggbvo.length];
              for (int j = 0; j < bvo.length; j++) {
                bvo[j].setCrowno(String.valueOf((j + 1) * 10));
                newvo[j] = bvo[j];
              }
              for (int j = 0; j < aggbvo.length; j++) {
                aggbvo[j].setCrowno(String.valueOf((j + bvo.length + 1) * 10));
                newvo[(bvo.length + j)] = aggbvo[j];
              }
              ((OrderVO)map2.get(dindh)).setChildrenVO(newvo);
            }
          }
              Iterator it = map2.entrySet().iterator();
              if ((it != null) && it.hasNext()) {
            	  List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
                while (it.hasNext()) {
                  Map.Entry entry = (Map.Entry)it.next();
                  OrderVO value = (OrderVO)entry.getValue();
                  list.add(value);
                }     
                IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
    			ipubdmo.N_XX_Action( "SAVEBASE", "21",  PoPublicUIClass.getLoginDate().toString(), list);
                showHintMessage("������������ɹ���");
              }
          } catch (Exception e) {
        	  showErrorMessage(e.getMessage());
          } finally {
            dialog.end();
          }
        }
      };
      new Thread(checkRun).start();
    }
  }

  protected void onExcelSave(OrderVO vo)
  {
    onSave(this, getPoCardPanel(), vo);
  }

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

  protected void updateButtonsAll()
  {
    super.updateButtonsAll();
    getBtnManager().btnBillExcel.setEnabled(true);
  }
  //end
  
  //edit by yqq 2016-10-26 XML���밴ť,���ýӿ�
  public void onXml() {
	// TODO Auto-generated method stub
	    UIFileChooser fileChooserXml = new UIFileChooser();
	    fileChooserXml.setAcceptAllFileFilterUsed(true);
	    this.xml = fileChooserXml.showOpenDialog(this);
	    if (this.xml == 0) {
			getTFLocalFile().setText(
					fileChooserXml.getSelectedFile().getAbsolutePath());
			this.xmlFile = fileChooserXml.getSelectedFile();

			try { 		
				// ��ȡServlet���Ӳ���������ķ���
				String url = "http://127.0.0.1:80/service/XChangeServlet?account=1&receiver=10301";  
				HttpURLConnection connection=PostFile.getConnection(url, false);
				connection.setDoOutput(true);
				connection.setRequestProperty("Contect-type", "text/xml");
				connection.setRequestMethod("POST");
				
				String xmlstr = getString("F:\\xml\\UTF-8.xml");	//��ȡxml�ĵ���ת����ʽ			
				InputStream input = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));	
			    org.w3c.dom.Document doc = XMLUtil.getDocumentBuilder().parse(input);			
				Writer writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				XMLUtil.printDOMTree(writer,doc,1,"UTF-8"); // ����XML�ļ���ʽ���
				writer.flush();
				writer.close();
				// �����ӵ���������ȡ�û�ִ��Ϣ
				InputStream inputStream = connection.getInputStream(); 
				InputStreamReader isr = new InputStreamReader(inputStream,"UTF-8");
				BufferedReader bufreader = new BufferedReader(isr);				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	    
  }
 
  public static String getString(String fileName) throws IOException,
			SAXException {
		String xmlString = "";

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = db.parse(fileName);
		xmlString = toString(doc);
		return xmlString;
	}
  
  
  public static String toString(Document document) {
	  String result = null;

	  if (document != null) {
	   StringWriter strWtr = new StringWriter();
	   StreamResult strResult = new StreamResult(strWtr);
	   TransformerFactory tfac = TransformerFactory.newInstance();
	   try {
	    Transformer t = tfac.newTransformer();
	    t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    t.setOutputProperty(OutputKeys.INDENT, "yes");
	    t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml
	    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    t.transform(new DOMSource(document.getDocumentElement()),strResult);
	   } catch (Exception e) {
	    System.err.println("XML.toString(Document): " + e);
	   }
	   result = strResult.getWriter().toString();
	  }
	  return result;
	 }


  
//end by yqq 2016-10-26 XML���밴ť 
  

	// ��ȡpropertise�е�ֵ������һ��list����
	public static List<String> GetValue() {
		List<String> list = new ArrayList<String>();
		Properties props = new Properties();
		try {
			 String fileName = RuntimeEnv.getInstance().getNCHome()
			    + File.separator + "resources" + File.separator
				+ "Markingmoney.properties";// ȡ�������ļ���·��
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));	
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				list.add(Property);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return list;

	}
	public void OnHxSave(){
		try {
			
			BillCardPanel panel = (BillCardPanel)getComponent(0);
			InvoiceVO invoiceVo = new InvoiceVO();
			 OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
			//��ȡ��˾
			String corp =invoiceVo.getHeadVO().getPk_corp();
			//��ȡ��ƱID
			String cinvoiceid =invoiceVo.getHeadVO().getCinvoiceid();
			//�ɹ�����id
			Object corderid = getPoCardPanel().getHeadItem("corderid").getValueObject();
			List<CHECKVO> list = null;
			HashMap map = null;
			StringBuffer sql = new StringBuffer();
			//ͨ���ɹ���ƱID��ѯ������¼��
			sql.append("select 	vinvoicecode, vordercode,yfmoney,hxmoney,yfbalance from po_check where dr=0  and pk_corp='"+corp+"' and corderid ='"+corderid+"' ORDER BY TS desc, CORDERID desc ");
			IUAPQueryBS  uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			list = (List<CHECKVO>) uap.executeQuery(sql.toString(), new BeanListProcessor(CHECKVO.class));
			if(list==null||list.size()==0){
				{MessageDialog.showHintDlg(null, "������¼", "δ��ѯ����ǰ�ĺ�����¼");
				}
									
//				showWarningMessage("������¼");
				}else{
					System.out.println(map);
					System.out.println(panel);
					OnQueryAction dialogs= new OnQueryAction(panel,list);
					dialogs.show();
				}
			} catch (BusinessException e) {				
			e.printStackTrace();
		}
		
		
	}

	public void afterEdit(BillEditEvent e) {
		if(e.getKey().equals("noriginaltaxpricemny"))
		{
			
	    	try {
	    		 OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	    		String fkxx = vo.getHeadVO().getCtermprotocolid();//��ȡ�ɹ�������	������Э�顿ѡ���ֵ��Э���
		    	String receivingSql = "select m.jhyfk,h.acclimited from bd_payterm m inner join bd_paytermch h on m.pk_payterm=h.pk_payterm " +
		    			"where m.pk_payterm='"+fkxx+"'and nvl(m.dr,0)=0 and nvl(h.dr,0)=0";//����Э��Ų�ѯ��Ԥ������ʺ���������
		    	IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		    	MapListProcessor getReceiving = new MapListProcessor();
		    	List receivingList = null;
				receivingList = (List) receiving.executeQuery(receivingSql, new MapListProcessor());
				Map receivingMap = (Map) receivingList.get(0);
				String jhyfk = null == receivingMap.get("jhyfk")
				|| "".equals(receivingMap.get("jhyfk"))? "0" : receivingMap.get("jhyfk").toString();//Ԥ������ʣ���*0.01��String���ͣ�
				double value = Double.valueOf(jhyfk);//���������������ת��String->double
		    	double ratio = value*0.01;//�������Ԥ������ʣ�double��
		    	String dayafterfp = null == receivingMap.get("acclimited") 
		    	|| "".equals(receivingMap.get("acclimited"))? "0" : receivingMap.get("acclimited").toString();//����������String���ͣ�
		    	Integer date = Integer.valueOf(dayafterfp);//�����������������int��
		    	//��Դ���ݺţ�vdef1��Ϊ�ɹ������������ɹ�����id��corderid����������ȡ��Ϊ��corderId
		    	OrderItemVO[] bvos = vo.getBodyVO();//��ȡ�ɹ���������vo����������
		    	OrderItemVO body = bvos[0];//�ö�������������ֵ
		    	UFDouble money = body.getNoriginalcurmny();//�ɹ��������������ֵ
		    	//��ȡ�ɹ�������������
		    	String bts = body.getTs();//��ȡ����ʱ���		    
		    	UFDouble nmoney = body.getNoriginaltaxpricemny();//�����˰�ϼ�
		    	UFDouble payRatio = new UFDouble(ratio);//��Ԥ���������doubleתΪUFDouble
		    	UFDouble prepaymentRatio = payRatio.multiply(nmoney);//Ԥ������ʳ˱����˰�ϼƵ�Ԥ������
		    	BillCardPanel panel = (BillCardPanel)getComponent(0);
				InvoiceVO invoiceVo = new InvoiceVO();
				panel.setHeadItem("noriginaltaxpricemny", prepaymentRatio);
				
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		
	}
  
	  
}