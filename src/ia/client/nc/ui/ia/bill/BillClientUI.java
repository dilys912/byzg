package nc.ui.ia.bill;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.common.ListProcessor;
import nc.ui.ia.ia301.AuditBO_Client;
import nc.ui.ia.ia402.AccountBO_Client;
import nc.ui.ia.pub.AdjustBillItemRef;
import nc.ui.ia.pub.AdjustBillRef;
import nc.ui.ia.pub.BatchRef;
import nc.ui.ia.pub.BillTypeRef;
import nc.ui.ia.pub.CommonDataBO_Client;
import nc.ui.ia.pub.ExceptionUITools;
import nc.ui.ia.pub.IAEnvironment;
import nc.ui.ia.pub.JobPhaseRef;
import nc.ui.ia.pub.JobRef;
import nc.ui.ia.pub.WkCenterRef;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.scm.ic.freeitem.DefHelper;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.def.DefSetTool;
import nc.utils.modify.is.IdetermineService;
import nc.vo.bd.CorpVO;
import nc.vo.bd.def.DefVO;
import nc.vo.ia.bill.AddQueryVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ia.bill.ComboItemsVO;
import nc.vo.ia.bill.TempGeneralBillVO;
import nc.vo.ia.ia402.AccountVO;
import nc.vo.ia.ia505.AssistantLedgerVO;
import nc.vo.ia.outter.SettledInfoVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.ia.pub.FlagTranslator;
import nc.vo.ia.pub.Log;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.bill.BillVOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;

/**
 * ��������������ά������ <p/> ����:���� <p/> ��������:(2001-4-29 11:39:46) <p/> �޸ļ�¼������: <p/> �޸���:
 */
public class BillClientUI extends nc.ui.pub.ToftPanel implements
    UIDialogListener, BillEditListener, BillEditListener2,
    BillTableMouseListener, IBillRelaSortListener2, ValueChangedListener,
    BillBodyMenuListener, ItemListener, ListSelectionListener, ILinkQuery {

  private static final long serialVersionUID = 1L;
  /**
   * ȡ�ýڵ��Ӧ�Ľڵ���� ������ʵ��
   * @return String
   */
  protected  String getFunCode(){
	  return "";
  }
  /**
   * ���鵥�ݷ���
   */
  public void doQueryAction(ILinkQueryData querydata) {
    m_sBillType = querydata.getBillType();
    String sBillID = querydata.getBillID();
    try {

      // ���ò�ѯ��VO
      BillVO bvo = new BillVO();
      BillHeaderVO bhvo = new BillHeaderVO();
      bhvo.setCbillid(sBillID);
      bvo.setParentVO(bhvo);
      // ��ѯ����
      ClientLink cl = ce.getClientLink();
      BillVO[] bills = BillBO_Client.queryByVO(bvo, new Boolean(true),
          "cbillid", cl);
      // ��ʾ����
      if (bills != null && bills.length != 0 && bills[0] != null) {
        // ��ͨ��������
        bhvo = (BillHeaderVO) bills[0].getParentVO();
        String corp = bhvo.getPk_corp();
        //�Ǳ���˾���飬���Ȩ��
        if(ce.getCorporationID().equals(corp)){
          ConditionVO[] conditionVOs = getQueryClientDlg().getDataPowerConVOs(corp, null);
          getQueryClientDlg().parseConditionVO(conditionVOs);
          String[] tables = getQueryClientDlg().getTables();
          String[] sConnection = getQueryClientDlg().getConnections();
          String[] sConditions = getQueryClientDlg().getConditions();
          if(sConditions != null && sConditions.length > 1){
            //ȥ��Ȩ��SQL�ַ�����ʼ�ĵ�һ��and
            sConditions[1] = "(" + sConditions[1].substring(5);
          }
          m_voBills = BillBO_Client.querybillWithOtherTable(tables, sConnection, sConditions,
              bvo, null, null, new Boolean(false), cl);
          if (m_voBills != null && m_voBills.length > 0) {
            setBillsInList(m_voBills);
          }else{
            showErrorMessage(NCLangRes.getInstance().getStrByID("common", 
                "SCMCOMMON000000161"));/*@res"û�в鿴���ݵ�Ȩ��"*/
          }
        }else{
          //���Ǳ���˾���飬�����Ȩ�ޣ���ť�û�
          //���³�ʼ��
          initialize(corp);
          //�������뵥��������˾�ĵ���ģ��
          loadTemplet(m_sTitle, m_sBillType);
          //��ť�û�
          ButtonObject[] buttons = this.buttonTree.getButtonArray();
          for(int i = 0; i<buttons.length; i++){
            buttons[i].setEnabled(false);
          }
          setButtons(buttons);
          //���õ�����Ϣ
          getBillCardPanel().setBillValueVO(bills[0]);
          getBillCardPanel().execHeadTailLoadFormulas();
          getBillCardPanel().getBillModel().execLoadFormula();
          //������ͷ
          setComboBoxInHeadFromVO(bills[0], true, 0);
          //��������
          BillItemVO btvo[] = (BillItemVO[]) bills[0].getChildrenVO();
          for (int i = 0; i < btvo.length; i++) {
            setComboBoxInBodyFromVO(btvo[i], true, i);
          }
        }
      }
      else if (1 == 1) {
        // ���ƽ̨ƾ֤���鵥��ʱʹ��
        bvo = new BillVO();
        // ����ģ��
        getBillListPanel().loadListTemplet(m_sTitle, m_sBillType, null, null,
            null);
        bhvo = new BillHeaderVO();
        BillItemVO btvo = new BillItemVO();
        btvo.setCSQLClause("(cbill_bid = '" + sBillID
            + "' or csumrtvouchid = '" + sBillID + "')");
        BillItemVO[] btvos = new BillItemVO[1];
        btvos[0] = btvo;
        bvo.setParentVO(bhvo);
        bvo.setChildrenVO(btvos);
        // ��ѯ����
        m_voBills = BillBO_Client.querybillWithOtherTable(null, null, null,
            bvo, null, null, new Boolean(false), cl);

        if (m_voBills != null && m_voBills.length > 0) {
          setBillsInList(m_voBills);
        }
        else {
          /*@res "û�з��������ĵ��ݻ�˵����Ǹ��ݼƼ۷�ʽ��������"*/
          MessageDialog.showHintDlg(this, null, NCLangRes.getInstance()
              .getStrByID("20143010", "UPP20143010-000230"));
        }
      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000084")/* @res "δ��ѯ����¼" */);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000103")/* @res "��ʼ�����ݳ���" */
          + e.getMessage());
    }
  }

  /**
   * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
   * 
   * @return boolean ����ֵΪtrue��ʾ�������ڹرգ�����ֵΪfalse��ʾ���������ڹرա�
   */
  public boolean onClosing() {
    if (m_iStatus == ADD_STATUS || m_iStatus == UPDATE_STATUS) {
      int r = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes
          .getInstance().getStrByID("common", "UCH001")
      /* @res�Ƿ񱣴����޸ĵ����ݣ�" */, UIDialog.ID_YES);
      
      if (r==UIDialog.ID_YES){
        if(this.onButtonOKClicked()){
          return true;
        }else{
          return false;
        }
      }else if(r == UIDialog.ID_NO){
        return true;
      }else if(r == UIDialog.ID_CANCEL){
        return false;
      }
    }
    return true;
  }

  /**
   * ���ؽ�������ʱͬʱ�����Ļ�������
   */
  public Object[] getRelaSortObjectArray() {
    return m_voBills;
  }

  /**
   * ��ͷ�ͱ������Ƿ�ת������˰�����¼�������
   * 
   * @param e
   */
  public void itemStateChanged(ItemEvent e) {
    Object o = e.getSource();
    if (o != null && (m_iStatus == UPDATE_STATUS || m_iStatus == ADD_STATUS)) {
      String sName = ((UICheckBox) o).getName();
      // ��Ӧ��ͷ�ĸı�
      if ("btransferincometax_h".equals(sName)) {
        BillItem bt = getBillCardPanel().getHeadItem("btransferincometax_h");
        if (bt == null)
          return;
        UICheckBox cb = (UICheckBox) bt.getComponent();
        int[] iRows = getBillCardPanel().getBillTable().getSelectedRows();
        // �޸ı�����Ӧ�е�״̬��������afterEdit�����༭���¼����л�����˰�ʣ�������˰���Ƿ��ܱ༭
        if (cb.isSelected()) {
          for (int i = 0; i < iRows.length; i++) {
            boolean bIsSuccess = beforeEdit(new BillEditEvent(
                "btransferincometax", Boolean.TRUE, "btransferincometax",
                iRows[i]));
            if (bIsSuccess) {
              getBillCardPanel().getBillModel().setValueAt(Boolean.TRUE,
                  iRows[i], "btransferincometax");
            }
            else {
              return;
            }
            afterEdit(new BillEditEvent("btransferincometax", Boolean.TRUE,
                "btransferincometax", iRows[i]));
          }
        }
        else {
          for (int i = 0; i < iRows.length; i++) {
            boolean bIsSuccess = beforeEdit(new BillEditEvent(
                "btransferincometax", Boolean.FALSE, "btransferincometax",
                iRows[i]));
            if (bIsSuccess) {
              getBillCardPanel().getBillModel().setValueAt(Boolean.FALSE,
                  iRows[i], "btransferincometax");
            }
            else {
              return;
            }
            afterEdit(new BillEditEvent("btransferincometax", Boolean.FALSE,
                "btransferincometax", iRows[i]));
          }
        }
      }
      // ��Ӧ����ĸı�
      else if ("btransferincometax".equals(sName)) {
        BillItem bt = getBillCardPanel().getBodyItem("btransferincometax");
        if (bt == null)
          return;
        UICheckBox cb = (UICheckBox) bt.getComponent();
        //
        BillItem hbt = getBillCardPanel().getHeadItem("btransferincometax_h");
        if (hbt == null)
          return;
        UICheckBox hcb = (UICheckBox) hbt.getComponent();
        // ��ȥ��������
        hcb.removeItemListener(this);
        // �޸ı�ͷ�����ֵ
        if (cb.isSelected()) {
          hcb.setSelected(true);
        }
        else {
          hcb.setSelected(false);
        }
        // �ָ�������
        hcb.addItemListener(this);
        hbt.setComponent(hcb);

      }
    }
  }

  /**
   * ��ѡȡ�仯�¼�����ز���
   * 
   * @param e
   */
  public void valueChanged(ListSelectionEvent e) {

    if (!e.getValueIsAdjusting() && m_bIsOutBill) {
      int[] iRows = getBillCardPanel().getBillTable().getSelectedRows();
      boolean b = iRows.length == 0 ? false : true;

      try {
        for (int i = 0; i < iRows.length; i++) {
          Object o = getBillCardPanel().getBillModel().getValueAt(iRows[i],
              "btransferincometax");
          if (o != null && o.toString().equals("false")) {
            b = false;
            break;
          }
        }
      }
      catch (Exception e1) {
        e1.printStackTrace();
      }
      // �޸ı�ͷ�����ֵ
      BillItem bt = getBillCardPanel().getHeadItem("btransferincometax_h");
      if (bt != null) {
        UICheckBox cb = (UICheckBox) bt.getComponent();
        cb.removeItemListener(this);
        cb.setSelected(b);
        cb.addItemListener(this);
        bt.setComponent(cb);
      }
    }
  }

  //
  class BillBodyListener implements nc.ui.pub.bill.BillEditListener {

    public void afterEdit(BillEditEvent e) {
    }

    /**
     * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
     * 
     * @param e
     *          ufbill.BillEditEvent
     */
    public void bodyRowChange(BillEditEvent e) {
      BillClientUI.this.bodyChanged(e);
    }
  } //

  class BillHeadListener implements nc.ui.pub.bill.BillEditListener {

    public void afterEdit(BillEditEvent e) {
    }

    /**
     * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
     * 
     * @param e
     *          ufbill.BillEditEvent
     */
    public void bodyRowChange(BillEditEvent e) {
      BillClientUI.this.headChanged(e);
    }
  }

  class BillSourceListener implements java.awt.event.ItemListener {
    public void itemStateChanged(java.awt.event.ItemEvent e) {
      if (e.getSource() == BillClientUI.this.getUIComboBoxSource())
      	uIComboBoxSource_ItemStateChanged(e);
    };
  }

  /**
   * ��������:��ѯ�Ի���ȷ���رպ󣬰��������в��� ���ڽ�����ʾ���������Ĵ����Ϣ ����:�� <p/> ����ֵ:void <p/> �쳣:��
   */
  private void addQuery() {
    // 1. ȡ�ò�������
    AddQueryVO aqVO = getQueryPlannedPriceDlg().getQueryVO();
    // 2. �������������Ĵ��
    if (aqVO != null) {
      try {
        aqVO = BillBO_Client.queryAddBillVOs(aqVO);
      }
      catch (Exception e) {
        reportException(e);
        e.printStackTrace();
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000037")/* @res "��ѯ���ݳ���" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000037")/* @res "��ѯ���ݳ���" */
            + e.getMessage());
        return;
      }
    }
    // 3. �Ѳ��ҵ��Ĵ������������
    if (aqVO != null) {
      onAdd(aqVO);
    }
  }

  /**
   * ��������:��˸���Ƽ۵ĵ��ݷ�¼ <p/> ����: boolean isInvi ----- �Ƿ��Ǹ���Ƽ� <p/> ����ֵ: <p/> �쳣:
   */
  private void auditOneBill(boolean isInvi, String sAudit) {

    try {
      if (sAudit == null)
        sAudit = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDSHJZ);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000038")/* @res "���ڳɱ����㣬���Ժ�" */);
      // ���óɱ�����ķ���
      String[] systemInfo = new String[5];
      systemInfo[0] = m_sCorpID;
      systemInfo[1] = ce.getBusinessDate().toString();
      systemInfo[2] = ce.getUser().getPrimaryKey();
      systemInfo[3] = ce.getAccountYear();
      systemInfo[4] = ce.getAccountMonth();
      boolean bSuccessed = true;

      BillVO detVO = (BillVO) m_voCurBill.clone();
      BillItemVO btvo = null;
      if (isInvi) {
        m_voAssistantData = getIndividualAllotDlg().getInviData();
        btvo = getIndividualAllotDlg().getBillItemVO()[0];
        detVO = new BillVO();
        detVO.setParentVO(m_voCurBill.getParentVO());
        BillItemVO detbtvo[] = new BillItemVO[1];
        detbtvo[0] = btvo;
        detVO.setChildrenVO(detbtvo);
        if (m_voAssistantData == null) {
          return;
        }
        bSuccessed = AuditBO_Client.individualBillAccount(systemInfo, btvo,
            m_voAssistantData);
      }
      else {
        BillVO bAuditVO = new BillVO();
        bAuditVO.setParentVO(m_voCurBill.getParentVO());
        Vector vTemp = new Vector(1, 1);
        for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
          String sInvKind = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCinvkind();
          String sAuditorid = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCauditorid();
          int iPriceflag = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getFpricemodeflag().intValue();
          UFDouble dNum = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getNnumber();
          if (sInvKind != null
              && sAudit.indexOf(sInvKind) != -1
              && (iPriceflag != ConstVO.GBJJ || (((BillHeaderVO) m_voCurBill
                  .getParentVO()).getFdispatchflag().intValue() == 0
                  && dNum != null && dNum.doubleValue() > 0))
              && (sAuditorid == null || sAuditorid.trim().length() == 0)) {
            // ���������Ǹ���Ƽ۳��ⵥ��δ�ɱ����㣬��Ҫ�ɱ�����
            vTemp.addElement((BillItemVO) m_voCurBill.getChildrenVO()[i]);
          }
        }
        if (vTemp.size() == 0) {
          // û��Ҫ�����
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000039")/* @res "�˵���û����ɱ������¼" */);
          return;
        }
        BillItemVO[] btvos = new BillItemVO[vTemp.size()];
        vTemp.copyInto(btvos);
        bAuditVO.setChildrenVO(btvos);
        bSuccessed = AuditBO_Client.billaccountbyVO(systemInfo, bAuditVO
            .changeToView(), new Integer(1));
      }
      if (bSuccessed == false) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000022")/* @res "�ɱ�����ʧ��" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000022")/* @res "�ɱ�����ʧ��" */);
        return;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000040")/* @res "�ɱ�����ɹ�" */);
      // ����VO
      ((BillHeaderVO) m_voCurBill.getParentVO()).setBauditedflag(new UFBoolean(
          "Y"));
      if (isInvi) {
        // ������������ơ����ۡ����
        int iRowIndex = getBillListPanel().getBodyTable().getSelectedRow();
        int iModelIndex = -1;
        iModelIndex = iRowIndex;
        // ��������
        if (iRowIndex != -1) {
          ((BillItemVO) m_voCurBill.getChildrenVO()[iModelIndex])
              .setCauditorid(ce.getUser().getPrimaryKey());
          ((BillItemVO) m_voCurBill.getChildrenVO()[iModelIndex])
              .setDauditdate(ce.getBusinessDate());
          ((BillItemVO) m_voCurBill.getChildrenVO()[iModelIndex])
              .setCauditorname(ce.getUser().getUserName());
          getBillListPanel().getBodyBillModel().setValueAt(
              ce.getUser().getUserName(), iRowIndex, "cauditorname");
          getBillListPanel().getBodyBillModel().setValueAt(
          		ce.getBusinessDate(), iRowIndex,"dauditdate");
          String sSQL = " select ";
          sSQL = sSQL + " v.nprice,v.nmoney ";
          sSQL = sSQL + " from ";
          sSQL = sSQL + " ia_bill_b v ";
          sSQL = sSQL + " where ";
          sSQL = sSQL + " dr = 0 ";
          sSQL = sSQL + " and ";
          sSQL = sSQL + " v.cbill_bid = '" + btvo.getPrimaryKey() + "'";
          String[][] sResult = CommonDataBO_Client.queryData(sSQL);
          if (sResult.length != 0) {
            String[] sTemp = sResult[0];
            if (sTemp.length >= 2) {
              String sPrice = sTemp[0];
              if (sPrice != null && sPrice.trim().length() != 0) {
                UFDouble d = new UFDouble(sPrice);
                d = d.setScale(-m_iPeci[1], UFDouble.ROUND_HALF_UP);
                ((BillItemVO) m_voCurBill.getChildrenVO()[iRowIndex])
                    .setNprice(d);
                getBillListPanel().getBodyBillModel().setValueAt(d, iRowIndex,
                    "nprice");
                ((BillItemVO) m_voCurBill.getChildrenVO()[iModelIndex])
                    .setNprice(d);
              }
              String sMny = sTemp[1];
              if (sMny != null && sMny.trim().length() != 0) {
                UFDouble d = new UFDouble(sMny);
                d = d.setScale(-m_iPeci[2], UFDouble.ROUND_HALF_UP);
                ((BillItemVO) m_voCurBill.getChildrenVO()[iRowIndex])
                    .setNmoney(d);
                getBillListPanel().getBodyBillModel().setValueAt(d, iRowIndex,
                    "nmoney");
                ((BillItemVO) m_voCurBill.getChildrenVO()[iModelIndex])
                    .setNmoney(d);
              }
            }
          }
        }
        // �����޸İ�ť״̬
        boolean bAllHasAudit = true;
        for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
          String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCauditorid();
          if (sAuditorID == null || sAuditorID.length() == 0) {
            bAllHasAudit = false;
            break;
          }
        }
        if (bAllHasAudit) {
          // �������
          btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        }
        else {
          btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
        }
      }
      else {
        String sSQL = " select ";
        sSQL = sSQL
            + " v.nprice,v.nmoney,v.cauditorid,v.dauditdate,x.user_name ";
        sSQL = sSQL + " from ";
        sSQL = sSQL
            + " ia_bill_b v left outer join sm_user x on v.cauditorid = x.cuserid ";
        sSQL = sSQL + " where ";
        sSQL = sSQL + " v.dr = 0 ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cbillid = '"
            + m_voCurBill.getParentVO().getPrimaryKey() + "'";
        sSQL = sSQL + " order by ";
        sSQL = sSQL + " v.cbill_bid ";
        String[][] sResult = CommonDataBO_Client.queryData(sSQL);
        for (int i = 0; i < sResult.length; i++) {
          String[] sTemp = sResult[i];
          if (sTemp.length >= 5) {
            String sPrice = sTemp[0];
            if (sPrice != null && sPrice.trim().length() != 0) {
              UFDouble d = new UFDouble(sPrice);
              d = d.setScale(-m_iPeci[1], UFDouble.ROUND_HALF_UP);
              ((BillItemVO) m_voCurBill.getChildrenVO()[i]).setNprice(d);
              getBillCardPanel().getBillModel().setValueAt(d, i, "nprice");
            }
            String sMny = sTemp[1];
            if (sMny != null && sMny.trim().length() != 0) {
              UFDouble d = new UFDouble(sMny);
              d = d.setScale(-m_iPeci[2], UFDouble.ROUND_HALF_UP);
              ((BillItemVO) m_voCurBill.getChildrenVO()[i]).setNmoney(d);
              getBillCardPanel().getBillModel().setValueAt(d, i, "nmoney");
              // �������˰ת�����
              calcTransIncomeTaxMny(i);
            }
            String sAuditorID = sTemp[2];
            if (sAuditorID != null && sAuditorID.trim().length() != 0) {
              ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .setCauditorid(sAuditorID);
            }
            String dAuditDate = sTemp[3];
            if (dAuditDate != null && dAuditDate.trim().length() != 0) {
              ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .setDauditdate(new UFDate(dAuditDate));
              getBillCardPanel().getBillModel().setValueAt(dAuditDate, i,
              		"dauditdate");
            }
            String sAuditorName = sTemp[4];
            if (sAuditorName != null && sAuditorName.trim().length() != 0) {
              ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .setCauditorname(sAuditorName);
              getBillCardPanel().getBillModel().setValueAt(sAuditorName, i,
                  "cauditorname");
            }
          }
        }
        // �����޸İ�ť״̬
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      }
      // ���ð�ť״̬
      btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      if (getBillListPanel().isVisible()) {
        setBtnsForBilltypes(m_aryButtonGroupList);
      }
      else {
        setBtnsForBilltypes(m_aryButtonGroupCard);
      }
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * ��������: <p/> ����: Object[] oInvPKs <p/> oInvValues[0] = sPKs[i]; ����
   * oInvValues[1] = sCodes[i]; ���� oInvValues[2] = sNames[i]; ���� oInvValues[3] =
   * oInvSpec[i]; ��� oInvValues[4] = oInvType[i]; �ͺ� oInvValues[5] = oInvMea[i];
   * ������λ oInvValues[6] = oInvKind[i]; ������� oInvValues[7] = oInvJHJ[i]; �ƻ���
   * oInvValues[8] = oInvAss[i]; �Ƿ񸨼������� oInvValues[9] = oInvPricemethod[i];
   * �Ƽ۷�ʽ oInvValues[10] = oInvIncomeTax[i]; ����˰ <p/> ht :��������Ϣ <p/> ����ֵ: <p/>
   * �쳣:
   */
  private void changeInv(Object[] oInvPKs, int iRow, Hashtable ht)
      throws Exception {
    // ��ô����Ϣ
    Object oInvPK = oInvPKs[0];
    String sRdfCode = oInvPKs[1].toString();
    String sRefName = oInvPKs[2].toString();
    Object oInvSpec = oInvPKs[3];
    Object oInvType = oInvPKs[4];
    Object oInvMea = oInvPKs[5];
    Object oInvKind = oInvPKs[6];

    UFDouble dPlanedPrice = null;
    if (oInvPKs[7] != null) {
      dPlanedPrice = new UFDouble(oInvPKs[7].toString());
    }
    // �жϴ˴���Ƿ��Ǹ���������
    UFBoolean bAstManage = new UFBoolean(false);
    if (oInvPKs[8] != null) {
      bAstManage = new UFBoolean(oInvPKs[8].toString());
    }
    // �Ƽ۷�ʽ 20050629 zlq ���ٿ���
    // int iFpricemethod = -1;
    // if (oInvPKs[9] != null) {
    // iFpricemethod = new Integer(oInvPKs[9].toString()).intValue();
    // if (iFpricemethod != ConstVO.JHJ) {
    // //�Ǽƻ��۴�����ƻ�����Ϊ��
    // dPlanedPrice = null;
    // }
    // }
    // �������ı䣬�����κš����������ۡ����ƻ������Ϊ��
    String[] itemkeys = new String[]{
  			"cinventoryid","cinventorycode",	"cinventoryname","cinventoryspec",
  			"cinventorytype", "cinventorymeasname","vbatch","castunitname","castunitid",
  			"nchangerate","nassistnum", "nnumber","nprice","nmoney","nplanedmny",
  			"nplanedprice", "noriginalprice", "cadjustbillitem",	"dbizdate","cinvkind","nexpaybacktax",
  			"vfree0","vfree1",	"vfree2","vfree3","vfree4","vfree5",}; 
  	for( int i =0 ; i < itemkeys.length; i++){
  		if( getBillCardPanel().getBodyItem(itemkeys[i]) != null){
  			getBillCardPanel().getBillModel().setValueAt(null, iRow,itemkeys[i]);
  		}
  	}
    // ���ò���
    getUIRefPaneBatch().setText("");
    getUIRefPaneAdjustBillItem().setText("");
    if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > iRow) {
      BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRow];
      btvo.setVOFree(null);
    }
//    BillItem bt = getBillCardPanel().getBodyItem("nnumber");
//    if (bt != null) {
//      bt.setValue(null);
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "vbatch");
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
//      // ������������λ��
//      if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") != -1) {
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitid");
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitname");
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nchangerate");
//        getBillCardPanel().getBillModel().setValueAt(null, iRow,"nassistnum" );
//      }
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "nprice");
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "nmoney");
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "nplanedmny");
//      BillItem bt2 = getBillCardPanel().getBodyItem("cadjustbillitem");
//      if (bt2 != null) {
//        getBillCardPanel().getBillModel().setValueAt("", iRow, "cadjustbillitem");
//      }
//      // ����������
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree0");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree1");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree2");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree3");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree4");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree5");
//      // ���ò���
//      getUIRefPaneBatch().setText("");
//      getUIRefPaneAdjustBillItem().setText("");
//      getBillCardPanel().getBillModel().setValueAt("", iRow, "vfree0");
//      if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > iRow) {
//        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRow];
//        btvo.setVOFree(null);
//      }
//    }

    String oRD = (String) getBillCardPanel().getHeadItem("crdcenterid")
        .getValueObject();
    if (oInvPK != null && oInvPK.toString().trim().length() != 0 && oRD != null
        && oRD.trim().length() != 0) {
    	//��ó�����˰��( ���ⵥ�ҿ������δ���� )//
    	if( m_bIsOutBill && !m_bIsICStart && !m_bIsSOStart){
	    	Object[] oexpaybacktax = (Object[]) CacheTool.getCellValue(
	          "bd_invmandoc", "pk_invmandoc","expaybacktax", oInvPK.toString().trim());
	    	 getBillCardPanel().getBillModel().setValueAt(oexpaybacktax[0],
	           iRow, "nexpaybacktax");
    	}
      if (bAstManage.booleanValue()) {
        // ��������������
        // ��ø�������λ
        String sAstUnitName = "";
        String sAstUnitID = "";

        UFDouble dRate = null;
        String sFixedflag = null;
        Object oTemp2 = ht.get(oInvPK.toString());
        if (oTemp2 != null) {
          String[] sTemp = (String[]) oTemp2;
          if (sTemp.length != 0) {
            sAstUnitID = sTemp[0];
            sAstUnitName = sTemp[1];
            if (sTemp[2] != null && sTemp[2].trim().length() != 0) {
              dRate = new UFDouble(sTemp[2].trim());
            }
            sFixedflag = sTemp[3];
            if (sFixedflag != null && sFixedflag.trim().equals("Y")) {
              // �ǹ̶������ʣ��������������� (����������x������=����)
              Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
                  "nnumber");
              if (oTemp != null && oTemp.toString().trim().length() != 0) {
                UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                if (dRate != null) {
                  UFDouble dAssistNum = dNumber.div(dRate); // (����������=����/������)
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
              if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                // ���̶������ʼ�����
                m_htInvAndFix.put(oInvPK.toString().trim() + ","
                    + sAstUnitID.trim(), "Y");
              }
            }
          }
        }
        if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") != -1) {
          getBillCardPanel().getBillModel().setValueAt(sAstUnitID, iRow,
              "castunitid");
          getBillCardPanel().getBillModel().setValueAt(sAstUnitName, iRow,
              "castunitname");
          getBillCardPanel().getBillModel().setValueAt(dRate, iRow,
              "nchangerate");
        }
      }
      getBillCardPanel().getBillModel().setValueAt(oInvPK.toString().trim(),
          iRow, "cinventoryid");
      getBillCardPanel().getBillModel().setValueAt(sRdfCode, iRow,
          "cinventorycode");
      getBillCardPanel().getBillModel().setValueAt(sRefName, iRow,
          "cinventoryname");
      getBillCardPanel().getBillModel().setValueAt(oInvSpec, iRow,
          "cinventoryspec");
      getBillCardPanel().getBillModel().setValueAt(oInvType, iRow,
          "cinventorytype");
      getBillCardPanel().getBillModel().setValueAt(oInvMea, iRow,
          "cinventorymeasname");
      getBillCardPanel().getBillModel().setValueAt(dPlanedPrice, iRow,
          "nplanedprice");
      getBillCardPanel().getBillModel().setValueAt(dPlanedPrice, iRow,
          "noriginalprice");
      getBillCardPanel().getBillModel().setValueAt(oInvKind, iRow, "cinvkind");

    }
    else {
//      getBillCardPanel().getBillModel()
//      		.setValueAt(null, iRow, "cinventoryid");
//      getBillCardPanel().getBillModel()
//          .setValueAt(null, iRow, "cinventorycode");
//      getBillCardPanel().getBillModel()
//          .setValueAt(null, iRow, "cinventoryname");
//      getBillCardPanel().getBillModel()
//          .setValueAt(null, iRow, "cinventoryspec");
//      getBillCardPanel().getBillModel()
//          .setValueAt(null, iRow, "cinventorytype");
//      getBillCardPanel().getBillModel()
//      		.setValueAt(null, iRow, "cinventorymeasname");
//      if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") != -1) {
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitid");
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitname");
//        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nchangerate");
//      }
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "nplanedprice");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "noriginalprice");
//      getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinvkind");
    }
  }

  /**
   * ��������:Ϊ�̶��ʲ��ӿ��õĸı����ķ��� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  // private void changeSingleInv(Object oInvPK, int iRow) throws Exception {
  // //�������ı䣬�����κš����������ۡ����ƻ������Ϊ��
  // BillItem bt = getBillCardPanel().getBodyItem("nnumber");
  // if (bt != null) {
  // bt.setValue(null);
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "vbatch");
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
  // //������������λ��
  // if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") !=
  // -1) {
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitid");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitname");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nchangerate");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nassistnum");
  // }
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nprice");
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nmoney");
  // //�������˰ת�����
  // calcTransIncomeTaxMny(iRow);
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "nplanedmny");
  // BillItem bt2 = getBillCardPanel().getBodyItem("cadjustbillitem");
  // if (bt2 != null) {
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "cadjustbillitem");
  // }
  // //����������
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree0");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree1");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree2");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree3");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree4");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree5");
  // //���ò���
  // getUIRefPaneBatch().setText("");
  // getUIRefPaneAdjustBillItem().setText("");
  // getBillCardPanel().getBillModel().setValueAt("", iRow, "vfree0");
  // if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > iRow) {
  // BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRow];
  // btvo.setVOFree(null);
  // }
  // }
  // nc.ui.bd.ref.AbstractRefModel model = m_refpaneInvBack.getRefModel();
  // UIRefPane pane = new UIRefPane();
  // pane.setRefNodeName("�������");/*-=notranslate=-*/
  // pane.setRefModel(model);
  // pane.setPK(oInvPK);
  // Object oRD = getBillCardPanel().getHeadItem("crdcenterid").getValue();
  // if (oInvPK != null && oInvPK.toString().trim().length() != 0 && oRD != null
  // && oRD.toString().trim().length() != 0) {
  // //��üƻ�����
  // UFDouble dPlanedPrice = CommonDataBO_Client.getPlanedPrice(m_sCorpID,
  // oRD.toString().trim(), oInvPK
  // .toString().trim());
  // //��ô����Ϣ
  // String sRdfCode = pane.getRefCode();
  // String sRefName = pane.getRefName();
  // Object oInvSpec =
  // pane.getRef().getRefModel().getValue("bd_invbasdoc.invspec");
  // Object oInvType =
  // pane.getRef().getRefModel().getValue("bd_invbasdoc.invtype");
  // Object oInvMea =
  // pane.getRef().getRefModel().getValue("bd_measdoc.measname");
  // Object oInvKind =
  // pane.getRef().getRefModel().getValue("bd_produce.materstate");
  // //�жϴ˴���Ƿ��Ǹ���������
  // Hashtable htIsMngFrAssi = ce.isManageForAssi(m_sCorpID, new
  // String[]{oInvPK.toString()});
  // UFBoolean bAstManage = (UFBoolean)htIsMngFrAssi.get(oInvPK.toString());
  // if (bAstManage.booleanValue() && m_bIsAdjustBill == false) {
  // //��������������
  // //��ø�������λ
  // String sAstUnitName = "";
  // String sAstUnitID = "";
  // String sAstUnitFieldName = "";
  // UFDouble dRate = null;
  // String sFixedflag = null;
  // if (m_sBillType.equals(ConstVO.m_sBillCGRKD) ||
  // m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
  // //�ɹ���ⵥ��ί��ӹ��ջ���ȡ�ɹ���������λ
  // sAstUnitFieldName = "pk_measdoc2";
  // } else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) ||
  // m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
  // //���۳ɱ���ת��ȡ���۸�������λ
  // sAstUnitFieldName = "pk_measdoc1";
  // } else {
  // //����ȡ��渨������λ
  // sAstUnitFieldName = "pk_measdoc3";
  // }
  // String sSQL = " select ";
  // sSQL = sSQL + sAstUnitFieldName + ",measname,d.mainmeasrate,d.fixedflag";
  // sSQL = sSQL + " from ";
  // sSQL = sSQL + " bd_invbasdoc a,bd_invmandoc b,bd_measdoc c,bd_convert d";
  // sSQL = sSQL + " where ";
  // sSQL = sSQL + " b.pk_invbasdoc = a.pk_invbasdoc ";
  // sSQL = sSQL + " and ";
  // sSQL = sSQL + " a.pk_invbasdoc =d.pk_invbasdoc ";
  // sSQL = sSQL + " and ";
  // sSQL = sSQL + " a." + sAstUnitFieldName + "=c.pk_measdoc ";
  // sSQL = sSQL + " and ";
  // sSQL = sSQL + " a." + sAstUnitFieldName + "=d.pk_measdoc ";
  // sSQL = sSQL + " and ";
  // sSQL = sSQL + " b.pk_invmandoc = '" + oInvPK.toString().trim() + "'";
  // String[][] sResult = CommonDataBO_Client.queryData(sSQL);
  // if (sResult.length != 0) {
  // String[] sTemp = sResult[0];
  // if (sTemp.length != 0) {
  // sAstUnitID = sTemp[0];
  // sAstUnitName = sTemp[1];
  // if (sTemp[2] != null && sTemp[2].trim().length() != 0) {
  // dRate = new UFDouble(sTemp[2].trim());
  // }
  // sFixedflag = sTemp[2];
  // if (sFixedflag != null && sFixedflag.trim().equals("Y")) {
  // //�ǹ̶������ʣ��������������� (����������x������=����)
  // Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,
  // "nnumber");
  // if (oTemp != null && oTemp.toString().trim().length() != 0) {
  // UFDouble dNumber = new UFDouble(oTemp.toString().trim());
  // if (dRate != null) {
  // UFDouble dAssistNum = dNumber.div(dRate); //(����������=����/������)
  // getBillCardPanel().getBillModel().setValueAt(dAssistNum, iRow,
  // "nassistnum");
  // }
  // }
  // if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
  // //���̶������ʼ�����
  // m_htInvAndFix.put(oInvPK.toString().trim() + "," + sAstUnitID.trim(), "Y");
  // }
  // }
  // }
  // }
  // if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") !=
  // -1) {
  // getBillCardPanel().getBillModel().setValueAt(sAstUnitID, iRow,
  // "castunitid");
  // getBillCardPanel().getBillModel().setValueAt(sAstUnitName, iRow,
  // "castunitname");
  // getBillCardPanel().getBillModel().setValueAt(dRate, iRow, "nchangerate");
  // }
  // }
  // getBillCardPanel().getBillModel().setValueAt(oInvPK.toString().trim(),
  // iRow, "cinventoryid");
  // getBillCardPanel().getBillModel().setValueAt(sRdfCode, iRow,
  // "cinventorycode");
  // getBillCardPanel().getBillModel().setValueAt(sRefName, iRow,
  // "cinventoryname");
  // getBillCardPanel().getBillModel().setValueAt(oInvSpec, iRow,
  // "cinventoryspec");
  // getBillCardPanel().getBillModel().setValueAt(oInvType, iRow,
  // "cinventorytype");
  // getBillCardPanel().getBillModel().setValueAt(oInvMea, iRow,
  // "cinventorymeasname");
  // getBillCardPanel().getBillModel().setValueAt(dPlanedPrice, iRow,
  // "nplanedprice");
  // getBillCardPanel().getBillModel().setValueAt(dPlanedPrice, iRow,
  // "noriginalprice");
  // getBillCardPanel().getBillModel().setValueAt(oInvKind, iRow, "cinvkind");
  // } else {
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinventoryid");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinventorycode");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinventoryname");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinventoryspec");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinventorytype");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow,
  // "cinventorymeasname");
  // if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") !=
  // -1) {
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitid");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "castunitname");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nchangerate");
  // }
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "nplanedprice");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "noriginalprice");
  // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinvkind");
  // }
  // }
  /**
   * ��������:����Ƿ����������� <p/> ����: <p/> ����ֵ:�Ƿ�Ϸ� <p/> �쳣:
   */
  private boolean checkBillHeaderItem(BillItem bt) {
    if (bt != null) {
      Object o = bt.getValueObject();
      String sName = bt.getName();
      if (o == null || o.toString().trim().length() == 0) {
        // String sMessage = "������" + sName;
        String[] value = new String[] {
          sName
        };
        String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000400", null, value);
        showErrorMessage(sMessage);
        return false;
      }
    }
    return true;
  }

  /**
   * ��������:�ж��Ƿ������޸ķǱ����Ƶ��ĵ��� <p/> ����: <p/> ����ֵ: <p/> �쳣: <p/> �������ڣ�(2002-11-14
   * 20:04:46)
   */
  private boolean canAlterBillMadeByOthers(BillVO bvo) {
    boolean bEnabled = false;
    String sUser = ce.getUser().getPrimaryKey();
    if (bvo != null) {
      BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
      String sMaker = bhvo.getCoperatorid();
      if (sUser != null
          && (sUser.equals(sMaker) || !sUser.equals(sMaker)
              && m_bAllowChangeBillMkByOthers == true)) {
        bEnabled = true;
      }
    }
    if (!bEnabled) {
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000042")/* @res "��������������Ϊ��������ɾ�ķǱ����Ƶ��ĵ��ݡ�" */);
    }
    return bEnabled;
  }

  /**
   * ���ݿ����֯�ʹ��ID�����̬
   */
  private Hashtable getMaterialState(String sRdCenterID, String[] sInvIds) {
    Hashtable htReturn = new Hashtable();

    if (sInvIds == null || sInvIds.length == 0) {
      return htReturn;
    }

    String[] sRDIDs = new String[sInvIds.length];
    for (int i = 0; i < sRDIDs.length; i++) {
      sRDIDs[i] = sRdCenterID;
    }

    Object[][] sResult = CacheTool.getMultiColValue2("bd_produce",
        "pk_calbody", "pk_invmandoc", new String[] {
            "pk_calbody", "pk_invmandoc", "materstate"
        }, sRDIDs, sInvIds);

    for (int h = 0; h < sResult.length; h++) {
      if (sResult[h][2] == null
          || sResult[h][2].toString().trim().length() == 0) {
        sResult[h][2] = "a";
      }
      htReturn.put(sResult[h][0] + "," + sResult[h][1], sResult[h][2]);
    }
    return htReturn;
  }

  /**
   * ��������:��ʾ���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void setBillsInList(BillVO[] bvos) throws Exception {
    if (m_voBills != null && m_voBills.length != 0) {
      // ����˵���
      BillHeaderVO[] voBillHeads = null;
      Vector vTempHeads = new Vector(1, 1);
      for (int i = 0; i < m_voBills.length; i++) {
        BillVO voBill = m_voBills[i];
        vTempHeads.addElement(voBill.getParentVO());
      }
      voBillHeads = new BillHeaderVO[vTempHeads.size()];
      vTempHeads.copyInto(voBillHeads);
      getBillListPanel().setHeaderValueVO(voBillHeads);
      // ��ʽ���÷���,ʹ��ʽ����
      execListPanelHeadFormula();
      getBillCardPanel().getBillModel().execLoadFormula();

      String[] sBillIDs = new String[m_voBills.length];
      for (int i = 0; i < m_voBills.length; i++) {
        Object oSource = ((BillHeaderVO) m_voBills[i].getParentVO())
            .getCsourcemodulename();
        if (oSource != null && oSource.toString().trim().length() != 0) {
          String sSource = ce.changeModuleCodeToName(oSource.toString().trim());
          getBillListPanel().getHeadBillModel().setValueAt(sSource, i,
              "csourcemodulename");
        }
        if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          // m_voBills[i] = BillBO_Client.querybillWithOtherTable(null, null,
          // null, null, null, m_voBills[i], new Boolean(false),cl)[0];
          sBillIDs[i] = ((BillHeaderVO) m_voBills[i].getParentVO())
              .getCbillid();
        }
        // ���ü�����,�ݹ�,�������� zlq
        setComboBoxInHeadFromVO(m_voBills[i], false, i);
      }
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        // ��ѯ��Դ��������
        m_hmBillId2Sourcebilltypecode = BillBO_Client.queryBodyItems(sBillIDs,
            "csourcebilltypecode", ce.getClientLink());
        if (m_hmBillId2Sourcebilltypecode != null) {
          for (int i = 0; i < m_voBills.length; i++) {
            String sBillID = m_voBills[i].getParentVO().getPrimaryKey();
            Object sSource = m_hmBillId2Sourcebilltypecode.get(sBillID);
            if (sSource != null && sSource.toString().trim().length() != 0) {
              String sSourceName = "";
              if (sSource.equals(ConstVO.m_sBillXSFP)) {
                sSourceName = m_ComboItemsVO.name_salebill;// ���۷�Ʊ
              }
              else if (sSource.equals(ConstVO.m_sBillXSCKD)) {
                sSourceName = m_ComboItemsVO.name_saleoutlist;// ���۳��ⵥ
              }
              else if (sSource.equals(ConstVO.m_sBillKCTSD)) {
                sSourceName = m_ComboItemsVO.name_waylossbill;// ���;��
              }
              getBillListPanel().getHeadBillModel().setValueAt(sSourceName, i,
                  "cbillsource");
            }
            else {
              getBillListPanel().getHeadBillModel().setValueAt(null, i,
                  "cbillsource");
            }
          }
        }
      }

      // �л���ͷ����һ�ŵ��ݣ��������HeadChanged����
      getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);

      if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > 0) {
        getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
        for (int ii = 0; ii < m_voCurBill.getChildrenVO().length; ii++) {
          setComboBoxInBodyFromVO(
              (BillItemVO) m_voCurBill.getChildrenVO()[ii], false, ii);
        }
      }
      setBtnsForBilltypes(m_aryButtonGroupList);
    }
    else {
      m_voCurBill = null;
      m_iCurBillPrt = -1;
      getBillListPanel().setHeaderValueVO(null);
      getBillListPanel().setBodyValueVO(null);
    }
    m_iStatus = LIST_STATUS;
    setBtnsForStatus(m_iStatus);

  }

  /**
   * ��������:�б�������ת��ʱ�Ĵ��� 1������ǲ��ϳ��ⵥ�������Ƿ������ 2����������������������������������� 3��������������λ <p/>
   * ����:�� <p/> ����ֵ: <p/> �쳣:
   */
  private void dispListToCard() {
    try {
      // ִ�б�ͷ��β��ʽ
      getBillCardPanel().execHeadTailLoadFormulas();
      // ִ�б��幫ʽ
      getBillCardPanel().getBillModel().execLoadFormula();

      int iRowIndex = getBillListPanel().getHeadTable().getSelectedRow();
      Object oTemp = null;
      // 1������ǲ��ϳ��ⵥ�������Ƿ������
      if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "bwithdrawalflag");
        getBillCardPanel().getHeadItem("bwithdrawalflag").setValue(oTemp);
      }
      // 2�����������������������������������
      if (m_bIsInAdjustBill) {
        // �������������������������
        for (int h = 0; h < getBillListPanel().getBodyBillModel().getRowCount(); h++) {
          oTemp = getBillListPanel().getBodyBillModel().getValueAt(h,
              "cadjustbilltypeid");
          getBillCardPanel().setBodyValueAt(oTemp, h, "cadjustbilltype");
          oTemp = getBillListPanel().getBodyBillModel().getValueAt(h,
              "cadjustbill");
          getBillCardPanel().setBodyValueAt(oTemp, h, "cadjustbill");
          oTemp = getBillListPanel().getBodyBillModel().getValueAt(h,
              "cadjustbillitem");
          getBillCardPanel().setBodyValueAt(oTemp, h, "cadjustbillitem");
        }
      }
      else {
        BillItem bt = getBillListPanel().getBodyItem("cadjustbillitem");
        if (bt != null) {
          for (int h = 0; h < getBillListPanel().getBodyBillModel()
              .getRowCount(); h++) {
            oTemp = getBillListPanel().getBodyBillModel().getValueAt(h,
                "cadjustbillitem");
            getBillCardPanel().setBodyValueAt(oTemp, h, "cadjustbillitem");
          }
        }
      }
      // 3����������۳ɱ���ת�������õ�����Դ
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "cbillsource");
        getBillCardPanel().getHeadItem("cbillsource").setValue(oTemp);
      }
      // �����ݹ�ֵ
      BillItem bt = getBillCardPanel().getHeadItem("bestimateflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox ui = (UIComboBox) bt.getComponent();
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "bestimateflag");
        if (oTemp != null
            && (oTemp.toString().trim().equals("Y") || oTemp.toString().trim()
                .equals(m_ComboItemsVO.name_estimated_yes)))
          ui.setSelectedItem(m_ComboItemsVO.name_estimated_yes);
        else if (oTemp != null
                && (oTemp.toString().trim().equals("N") || oTemp.toString().trim()
                        .equals(m_ComboItemsVO.name_estimated_no)))
                  ui.setSelectedItem(m_ComboItemsVO.name_estimated_no);
      }
      // ���õ�������
      if (m_sBillType.equals(ConstVO.m_sBillDBRKD)
          || m_sBillType.equals(ConstVO.m_sBillDBCKD)) {
        oTemp = getBillListPanel().getHeadBillModel().getValueAt(iRowIndex,
            "fallocflag");
        getBillCardPanel().getHeadItem("fallocflag").setValue(oTemp);
      }
      // ���������־
      if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > 0) {
        for (int ii = 0; ii < m_voCurBill.getChildrenVO().length; ii++) {
          setComboBoxInBodyFromVO(
              (BillItemVO) m_voCurBill.getChildrenVO()[ii], true, ii);
        }
      }
      if (getBillCardPanel().getHeadItem("cdeptid") != null) {
        Object obj = getBillCardPanel().getHeadItem("cdeptid").getValueObject();
        String sDeptID = obj == null ? null : obj.toString();
        if (sDeptID != null && sDeptID.trim().length() != 0) {
          // ����ҵ��Ա���յ�����
          bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // ����ҵ��Ա���յ�����
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition
                + "and bd_psndoc.pk_deptdoc = '" + sDeptID + "'");
          }
        }
        else {
          bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // ����ҵ��Ա���յ�����
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition);
          }
        }
      }
      if (getBillCardPanel().getHeadItem("cstockrdcenterid") != null) {
        Object obj = getBillCardPanel().getHeadItem("cstockrdcenterid").getValueObject();
        String sRDID = obj == null ? null : obj.toString();
        bt = m_bd.getHeadItem("cwarehouseid");
        if (sRDID != null && sRDID.trim().length() != 0) {
          // ���òֿ���յ�����
          if (bt != null) {
            // ���òֿ���յ�����
            String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
                + "'";
            // �Ƿ��Ƿ�Ʒ��
            // if (m_sBillType.equals(ConstVO.m_sBillBFD) == false)
            sWhere = sWhere + "and gubflag = 'N'";
            // else
            // sWhere = sWhere + "and gubflag = 'Y'";
            // û���ݷ�
            sWhere = sWhere + "and sealflag = 'N'";
            ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
            obj = bt.getValueObject();
            String sWareID = obj == null ? null : obj.toString();
            // ���òֿ�
            if (sWareID != null && sWareID.trim().length() != 0) {
              ((UIRefPane) bt.getComponent()).setPK(sWareID);
              if (((UIRefPane) bt.getComponent()).getRefCode() == null) {
                getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
              }
            }
          }
        }
        else {
          getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
          // �ָ��ֿ������ //zlq add 20050305
          ((UIRefPane) bt.getComponent()).setWhereString(m_sOldWareCondition);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void execListPanelHeadFormula() {
    getBillListPanel().getHeadBillModel().execLoadFormula();
    // getBillListPanel().getHeadBillModel().getBodyValueVOs(BillHeader)

    if (m_voBills != null && m_voBills.length != 0) {
      for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
        Object oValue = null;
        BillHeaderVO hvo = (BillHeaderVO) m_voBills[i].getParentVO();
        if (getBillListPanel().getHeadItem("coperatorname") != null) {
          oValue = getBillListPanel().getHeadBillModel().getValueAt(i,
              "coperatorname");
          // ȷ���ж�Ӧ��ȷ
          Object oValue1 = getBillListPanel().getHeadBillModel().getValueAt(i,
              "cbillid");
          if (oValue1 != null && oValue1.equals(hvo.getCbillid())) {
            if (oValue != null && hvo.getCoperatorname() == null) {
              hvo.setCoperatorname(oValue.toString());
            }
          }
        }
        if (getBillListPanel().getHeadItem("clastoperatorname") != null) {
          oValue = getBillListPanel().getHeadBillModel().getValueAt(i,
              "clastoperatorname");
          // ȷ���ж�Ӧ��ȷ
          Object oValue1 = getBillListPanel().getHeadBillModel().getValueAt(i,
              "cbillid");
          if (oValue1 != null && oValue1.equals(hvo.getCbillid())) {
            if (oValue != null && hvo.getClastoperatorname() == null) {
              hvo.setClastoperatorname(oValue.toString());
            }
          }
        }

      }
    }
    else {
      Object oValue = null;
      BillHeaderVO hvo = (BillHeaderVO) m_voCurBill.getParentVO();
      if (getBillListPanel().getHeadItem("coperatorname") != null) {
        oValue = getBillListPanel().getHeadBillModel().getValueAt(0,
            "coperatorname");
        // ȷ���ж�Ӧ��ȷ
        Object oValue1 = getBillListPanel().getHeadBillModel().getValueAt(0,
            "cbillid");
        if (oValue1 != null && oValue1.equals(hvo.getCbillid())) {
          if (oValue != null && hvo.getCoperatorname() == null) {
            hvo.setCoperatorname(oValue.toString());
          }
        }
      }
      if (getBillListPanel().getHeadItem("clastoperatorname") != null) {
        oValue = getBillListPanel().getHeadBillModel().getValueAt(0,
            "clastoperatorname");
        // ȷ���ж�Ӧ��ȷ
        Object oValue1 = getBillListPanel().getHeadBillModel().getValueAt(0,
            "cbillid");
        if (oValue1 != null && oValue1.equals(hvo.getCbillid())) {
          if (oValue != null && hvo.getClastoperatorname() == null) {
            hvo.setClastoperatorname(oValue.toString());
          }
        }
      }

    }

  }

  /**
   * ��������:���������㵥�����ã����ݿ�û����ϵͳ�������ͣ�Ϊ��ʾ��Դ�������ͣ���д���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void execListPanelBodyFormula() {
    getBillListPanel().getBodyBillModel().execLoadFormula();
    // �ȴ�����Դ��������
    if (m_voCurBill != null) {
      String sBillType = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCbilltypecode();
      // ���۳ɱ���ת��
      if (sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
          Object oBillSource = getBillListPanel().getBodyBillModel()
              .getValueAt(i, "csourcebilltypecode");
          String sVOBillSource = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCsourcebilltypecode();
          if (sVOBillSource != null
              && sVOBillSource.trim().length() != 0
              && (oBillSource == null || oBillSource.toString().trim().length() == 0)) {
            // VO������Դ������Ϣ������û��
            // ������bd_billtype��û�д˵�������
            if (sVOBillSource.equals(ConstVO.m_sBillXSCKD)) {
              getBillListPanel().getBodyBillModel().setValueAt(
                  m_ComboItemsVO.name_saleoutlist, i, "csourcebilltypecode");
            }
            else if (sVOBillSource.equals(ConstVO.m_sBillXSFP)) {
              getBillListPanel().getBodyBillModel().setValueAt(
                  m_ComboItemsVO.name_salebill, i, "csourcebilltypecode");
            }
            else if (sVOBillSource.equals(ConstVO.m_sBillKCTSD)) {
              getBillListPanel().getBodyBillModel().setValueAt(
                  m_ComboItemsVO.name_waylossbill, i, "csourcebilltypecode");
            }
          }
        }
      }
      // ����ʽ��õ����ݷ���VO
      for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[i];
        Object oValue = null;
        if (getBillListPanel().getBodyItem("cprojectcode") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cprojectcode");
          if (oValue != null && btvo.getCprojectcode() == null) {
            btvo.setCprojectcode(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cprojectname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cprojectname");
          if (oValue != null && btvo.getCprojectname() == null) {
            btvo.setCprojectname(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cprojectphasecode") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cprojectphasecode");
          if (oValue != null && btvo.getCprojectphasecode() == null) {
            btvo.setCprojectphasecode(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cprojectphasename") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cprojectphasename");
          if (oValue != null && btvo.getCprojectphasename() == null) {
            btvo.setCprojectphasename(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("castunitname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "castunitname");
          if (oValue != null && btvo.getCastunitname() == null) {
            btvo.setCastunitname(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("vbomcodecode") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "vbomcodecode");
          if (oValue != null && btvo.getVbomcodecode() == null) {
            btvo.setVbomcodecode(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("vbomcodename") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "vbomcodename");
          if (oValue != null && btvo.getVbomcodename() == null) {
            btvo.setVbomcodename(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cinventorycode") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cinventorycode");
          if (oValue != null && btvo.getCinventorycode() == null) {
            btvo.setCinventorycode(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cinventoryname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cinventoryname");
          if (oValue != null && btvo.getCinventoryname() == null) {
            btvo.setCinventoryname(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cinventoryspec") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cinventoryspec");
          if (oValue != null && btvo.getCinventoryspec() == null) {
            btvo.setCinventoryspec(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cinventorytype") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cinventorytype");
          if (oValue != null && btvo.getCinventorytype() == null) {
            btvo.setCinventorytype(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cinventorymeasname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cinventorymeasname");
          if (oValue != null && btvo.getCinventorymeasname() == null) {
            btvo.setCinventorymeasname(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cauditorname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cauditorname");
          if (oValue != null && btvo.getCauditorname() == null) {
            btvo.setCauditorname(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cwpcode") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cwpcode");
          if (oValue != null && btvo.getCwpcode() == null) {
            btvo.setCwpcode(oValue.toString());
          }
        }
        if (getBillListPanel().getBodyItem("cwpname") != null) {
          oValue = getBillListPanel().getBodyBillModel().getValueAt(i,
              "cwpname");
          if (oValue != null && btvo.getCwpname() == null) {
            btvo.setCwpname(oValue.toString());
          }
        }
      }
    }
  }

  /**
   * ���� BillCard ����ֵ��
   * 
   * @return nc.ui.ia.bill.IABillCardPanel
   */
  /* ���棺�˷������������ɡ� */
  private IABillCardPanel getBillCardPanel() {
    if (ivjBillCardPanel == null) {
      try {
        ivjBillCardPanel = new IABillCardPanel();
        ivjBillCardPanel.setName("BillCardPanel");
        ivjBillCardPanel.setBounds(0, 0, 774, 419);

        ivjBillCardPanel.addEditListener(this);
        ivjBillCardPanel.addBodyEditListener2(this);

      }
      catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return ivjBillCardPanel;
  }

  /**
   * ���� IndividualAllotDlg ����ֵ��
   * 
   * @return nc.ui.ia.bill.IndividualAllotDlg
   */
  /* ���棺�˷������������ɡ� */
  private IndividualAllotDlg getIndividualAllotDlg() {
    if (ivjIndividualAllotDlg == null) {
      try {
        // user code begin {1}
        if (m_iPeci != null) {
          ivjIndividualAllotDlg = new IndividualAllotDlg(this, m_iPeci);
        }
        else {
          ivjIndividualAllotDlg = new IndividualAllotDlg(this);
        }
        ivjIndividualAllotDlg.setName("IndividualAllotDlg");
        ivjIndividualAllotDlg
            .setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ivjIndividualAllotDlg.addUIDialogListener(this);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjIndividualAllotDlg;
  }

  /**
   * ���� LocateConditionDlg ����ֵ��
   * 
   * @return nc.ui.ia.bill.LocateConditionDlg
   */
  /* ���棺�˷������������ɡ� */
  private LocateConditionDlg getLocateConditionDlg() {
    if (ivjLocateConditionDlg == null) {
      try {
        ivjLocateConditionDlg = new LocateConditionDlg();
        ivjLocateConditionDlg.setName("LocateConditionDlg");
        ivjLocateConditionDlg
            .setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        // user code begin {1}
        ivjLocateConditionDlg = new LocateConditionDlg(this);
        ivjLocateConditionDlg.setName("LocateConditionDlg");
        ivjLocateConditionDlg
            .setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ivjLocateConditionDlg.addUIDialogListener(this);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjLocateConditionDlg;
  }

  /**
   * ��������:��ò�ѯ��������Ի��� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private QueryClientDlg getQueryClientDlg() {
    if (m_dlgQuery == null) {
      String sModule = "20143010";
      if (getModuleCode() != null && getModuleCode().trim().length() != 0) {
        sModule = getModuleCode();
      }
      Log.info("Module Code: " + sModule);
      m_dlgQuery = new QueryClientDlg(this, sModule, m_sBillType);
      m_dlgQuery.setIsWarningWithNoInput(true);
    }
    return m_dlgQuery;
  }

  /**
   * ���� LocateConditionDlg ����ֵ��
   * 
   * @return nc.ui.ia.bill.LocateConditionDlg
   */
  /* ���棺�˷������������ɡ� */
  private QueryPlannedPriceDlg getQueryPlannedPriceDlg() {
    if (m_dlgQueryPlannedPrice == null) {
      m_dlgQueryPlannedPrice = new QueryPlannedPriceDlg(this);
      m_dlgQueryPlannedPrice.setIsWarningWithNoInput(true);
    }
    return m_dlgQueryPlannedPrice;
  }

  /**
   * ���� SaleBillsChooseDlg1 ����ֵ��
   * 
   * @return nc.ui.ia.bill.SaleBillsChooseDlg
   */
  /* ���棺�˷������������ɡ� */
  private SaleBillsChooseDlg getSaleBillsChooseDlg() {
    if (ivjSaleBillsChooseDlg == null) {
      try {
        // user code begin {1}
        ivjSaleBillsChooseDlg = new SaleBillsChooseDlg(this);
        ivjSaleBillsChooseDlg.setName("SaleBillsChooseDlg");
        ivjSaleBillsChooseDlg
            .setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ivjSaleBillsChooseDlg.addUIDialogListener(this);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjSaleBillsChooseDlg;
  }

  /**
   * ��������:����б�����ĵ���
   * 
   * @return nc.vo.ia.bill.BillVO[]
   */
  private ArrayList getListBills() throws Exception {
    ArrayList al = new ArrayList();

    for (int i = 0; i < m_voBills.length; i++) {
      if (m_voBills[i].getChildrenVO() == null) {
        // ��û�л�ñ�������
        ClientLink cl = ce.getClientLink();
        m_voBills[i] = BillBO_Client.querybillWithOtherTable(null, null, null,
            null, null, m_voBills[i], new Boolean(true), cl)[0];
      }
      al.add(m_voBills[i]);
    }
    return al;
  }

  /**
   * ��������:����б������ͷѡ�еĵ���
   * 
   * @return nc.vo.ia.bill.BillVO[]
   */
  private ArrayList getListSelectedBill() throws Exception {
    ArrayList al = new ArrayList();
    int index = getBillListPanel().getHeadTable().getSelectedRow();
    String cbillid = (String) getBillListPanel().getHeadBillModel().getValueAt(
        index, "cbillid");
    for (int i = 0; i < m_voBills.length; i++) {
      if (cbillid.equals(m_voBills[i].getParentVO().getPrimaryKey())) {
        if (m_voBills[i].getChildrenVO() == null) {
          // ��û�л�ñ�������
          ClientLink cl = ce.getClientLink();
          m_voBills[i] = BillBO_Client.querybillWithOtherTable(null, null,
              null, null, null, m_voBills[i], new Boolean(true), cl)[0];
        }
        al.add(m_voBills[i]);
        break;
      }
    }
    return al;
  }

  /**
   * ���� UIComboBoxSource ����ֵ��
   * 
   * @return nc.ui.pub.beans.UIComboBox
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.pub.beans.UIComboBox getUIComboBoxSource() {
    if (ivjUIComboBoxSource == null) {
      try {
        ivjUIComboBoxSource = new nc.ui.pub.beans.UIComboBox();
        ivjUIComboBoxSource.setName("UIComboBoxSource");
        ivjUIComboBoxSource.setBounds(20, 459, 100, 22);
        ivjUIComboBoxSource.setVisible(false);
        // user code begin {1}
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIComboBoxSource;
  }

  /**
   * ���� UIRefPaneAdjustBill ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCAdjustBillRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.AdjustBillRef getUIRefPaneAdjustBill() {
    if (ivjUIRefPaneAdjustBill == null) {
      try {
        ivjUIRefPaneAdjustBill = new nc.ui.ia.pub.AdjustBillRef();
        ivjUIRefPaneAdjustBill.setName("UIRefPaneAdjustBill");
        ivjUIRefPaneAdjustBill.setLocation(255, 14);
        ivjUIRefPaneAdjustBill.setVisible(false);
        // user code begin {1}
        ivjUIRefPaneAdjustBill.setReturnCode(true);
        ivjUIRefPaneAdjustBill.setCacheEnabled(false);
        ivjUIRefPaneAdjustBill.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneAdjustBill;
  }

  /**
   * ���� UIRefPaneAdjustBillItem ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCAdjustBillItemRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.AdjustBillItemRef getUIRefPaneAdjustBillItem() {
    if (ivjUIRefPaneAdjustBillItem == null) {
      try {
        ivjUIRefPaneAdjustBillItem = new nc.ui.ia.pub.AdjustBillItemRef();
        ivjUIRefPaneAdjustBillItem.setName("UIRefPaneAdjustBillItem");
        ivjUIRefPaneAdjustBillItem.setLocation(389, 12);
        ivjUIRefPaneAdjustBillItem.setVisible(false);
        // user code begin {1}
        ivjUIRefPaneAdjustBillItem.setReturnCode(true);
        ivjUIRefPaneAdjustBillItem.setCacheEnabled(false);
        ivjUIRefPaneAdjustBillItem.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneAdjustBillItem;
  }

  /**
   * ���� UIRefPaneBatch ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.BatchRef getUIRefPaneBatch() {
    if (ivjUIRefPaneBatch == null) {
      try {
        ivjUIRefPaneBatch = new nc.ui.ia.pub.BatchRef();
        ivjUIRefPaneBatch.setName("UIRefPaneBatch");
        ivjUIRefPaneBatch.setLocation(150, 10);
        // user code begin {1}
        ivjUIRefPaneBatch.setVisible(false);
        ivjUIRefPaneBatch.setReturnCode(true);
        ivjUIRefPaneBatch.setCacheEnabled(false);
        ivjUIRefPaneBatch.setLocation(10000, 10000);
        ivjUIRefPaneBatch.setNumCondition(m_sFQSK, m_sWTDX);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneBatch;
  }

  /**
   * ���� UIRefPaneBillType ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCBillTypeRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.BillTypeRef getUIRefPaneBillType() {
    if (ivjUIRefPaneBillType == null) {
      try {
        ivjUIRefPaneBillType = new nc.ui.ia.pub.BillTypeRef();
        ivjUIRefPaneBillType.setName("UIRefPaneBillType");
        ivjUIRefPaneBillType.setLocation(108, 15);
        ivjUIRefPaneBillType.setVisible(false);
        // user code begin {1}
        String sWhere = "pk_billtypecode in ('" + ConstVO.m_sBillCGRKD + "','"
            + ConstVO.m_sBillCCPRKD + "','" + ConstVO.m_sBillQTRKD + "','"
            + ConstVO.m_sBillWWJGSHD + "','" + ConstVO.m_sBillDBRKD + "')";
        ivjUIRefPaneBillType = new nc.ui.ia.pub.BillTypeRef(sWhere);
        ivjUIRefPaneBillType.setName("UIRefPaneBillType");
        ivjUIRefPaneBillType.setLocation(10000, 10000);
        ivjUIRefPaneBillType.setVisible(false);
        ivjUIRefPaneBillType.setReturnCode(true);
        // ivjUIRefPaneBillType.getUITextField().getDocument().addDocumentListener(this);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneBillType;
  }

  /**
   * ���� UIRefPaneBatch ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* ���棺�˷������������ɡ� */// 20050525 �رչ̶��ʲ��ʹ���ӿ�
  // private nc.ui.fa.outer.FaCardRefPane getUIRefPaneFacard() {
  // if (m_UIRefFa == null) {
  // try {
  // m_UIRefFa = new nc.ui.fa.outer.FaCardRefPane(m_sCorpID);
  // m_UIRefFa.setName("UIRefPaneFaCard");
  // m_UIRefFa.setLocation(150, 10);
  // // user code begin {1}
  // m_UIRefFa.setVisible(false);
  // m_UIRefFa.setReturnCode(true);
  // m_UIRefFa.setLocation(10000, 10000);
  // m_UIRefFa.getRefModel().setRefCodeField("asset_code");
  // m_UIRefFa.getRefModel().setRefNameField("asset_name");
  // //���Ӽ���
  // m_UIRefFa.addValueChangedListener(this);
  // m_UIRefFa.setButtonFireEvent(true);
  // // user code end
  // } catch (java.lang.Throwable ivjExc) {
  // // user code begin {2}
  // // user code end
  // handleException(ivjExc);
  // }
  // }
  // return m_UIRefFa;
  // }
  /**
   * ���� UIRefPaneBatch ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* ���棺�˷������������ɡ� */// 20050525 �رչ̶��ʲ��ʹ���ӿ�
  // private nc.ui.fa.outer.FaCardEquipmentRefPane getUIRefPaneFacardEquipment()
  // {
  // if (m_uiRefFaEqu == null) {
  // try {
  // m_uiRefFaEqu = new nc.ui.fa.outer.FaCardEquipmentRefPane();
  // m_uiRefFaEqu.setName("UIRefPaneFaCardEquipment");
  // m_uiRefFaEqu.setLocation(150, 10);
  // // user code begin {1}
  // m_uiRefFaEqu.setVisible(false);
  // m_uiRefFaEqu.setReturnCode(true);
  // m_uiRefFaEqu.setLocation(10000, 10000);
  // //���Ӽ���
  // m_uiRefFaEqu.addValueChangedListener(this);
  // m_uiRefFaEqu.setButtonFireEvent(true);
  // // user code end
  // } catch (java.lang.Throwable ivjExc) {
  // // user code begin {2}
  // // user code end
  // handleException(ivjExc);
  // }
  // }
  // return m_uiRefFaEqu;
  // }
  /**
   * ���� UIRefPaneFreeItem ����ֵ��
   * 
   * @return nc.ui.ia.pub.freeitem.FreeItemRefPane
   */
  /* ���棺�˷������������ɡ� */
  private FreeItemRefPane getUIRefPaneFreeItem() {
    if (ivjUIRefPaneFreeItem == null) {
      try {
        ivjUIRefPaneFreeItem = new FreeItemRefPane();
        ivjUIRefPaneFreeItem.setName("UIRefPaneFreeItem");
        ivjUIRefPaneFreeItem.setLocation(521, 13);
        ivjUIRefPaneFreeItem.setVisible(false);
        // user code begin {1}
        ivjUIRefPaneFreeItem.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneFreeItem;
  }

  /**
   * ���� UIRefPaneJob ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCJobRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.JobRef getUIRefPaneJob() {
    if (ivjUIRefPaneJob == null) {
      try {
        ivjUIRefPaneJob = new nc.ui.ia.pub.JobRef();
        ivjUIRefPaneJob.setName("UIRefPaneJob");
        ivjUIRefPaneJob.setLocation(162, 14);
        ivjUIRefPaneJob.setVisible(false);
        // user code begin {1}
        ivjUIRefPaneJob.setReturnCode(true);
        ivjUIRefPaneJob.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneJob;
  }

  /**
   * ���� UIRefPaneJobParse ����ֵ��
   * 
   * @return nc.ui.ia.pub.JobPhaseRef
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.JobPhaseRef getUIRefPaneJobParse() {
    if (ivjUIRefPaneJobParse == null) {
      try {
        ivjUIRefPaneJobParse = new nc.ui.ia.pub.JobPhaseRef();
        ivjUIRefPaneJobParse.setName("UIRefPaneJobParse");
        ivjUIRefPaneJobParse.setBounds(814, 20, 10, 10);
        // user code begin {1}
        ivjUIRefPaneJobParse.setVisible(false);
        ivjUIRefPaneJobParse.setReturnCode(true);
        ivjUIRefPaneJobParse.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneJobParse;
  }

  /**
   * ���� UIRefPaneBatch ����ֵ��
   * 
   * @return nc.ui.ia.pub.MyNCBatchRefPane
   */
  /* ���棺�˷������������ɡ� */
  private nc.ui.ia.pub.WkCenterRef getUIRefPaneWkCenter() {
    if (ivjUIRefPaneWkCenter == null) {
      try {
        ivjUIRefPaneWkCenter = new nc.ui.ia.pub.WkCenterRef();
        ivjUIRefPaneWkCenter.setName("UIRefPaneWkCenter");
        ivjUIRefPaneWkCenter.setLocation(150, 10);
        // user code begin {1}
        ivjUIRefPaneWkCenter.setVisible(false);
        ivjUIRefPaneWkCenter.setReturnCode(true);
        ivjUIRefPaneWkCenter.setCacheEnabled(false);
        ivjUIRefPaneWkCenter.setLocation(10000, 10000);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjUIRefPaneWkCenter;
  }

  /**
   * ��������:�����������������ͱ仯��ʹ���������ݺű༭״̬�仯 <p/> ����: DocumentEvent e ----- �ı��仯�¼� <p/>
   * ����ֵ: <p/> �쳣:
   */
  private void handleAdjustBillType(DocumentEvent e) {
    if (m_iStatus != ADD_STATUS && m_iStatus != UPDATE_STATUS) {
      // �������ӻ��޸�
      return;
    }
    // �����Ϣ
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();
    if (iRowCount <= iRow) {
      iRow = -1;
    }
    Object oBillType = getBillCardPanel().getBodyValueAt(iRow,
        "cadjustbilltype");
    if (oBillType != null && oBillType.toString().trim().length() != 0) {
      getUIRefPaneAdjustBill().setWhereString("");
      getBillCardPanel().getBodyItem("cadjustbill").setEdit(true);
      getBillCardPanel().getBodyItem("cadjustbill").setEnabled(true);
      // ���ò�ѯ����
      String sWhereString = " and a.cbilltypecode = '"
          + oBillType.toString().trim() + "' ";
      String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
          .getValue();
      String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
          .getValue();
      // �ж��Ƿ����вֿ⡢�����֯����������������κŲ�������
      if (sWareID != null && sWareID.trim().length() != 0) {
        sWhereString = sWhereString + " and a.cwarehouseid = '" + sWareID
            + "' ";
      }
      if (sRDID != null && sRDID.trim().length() != 0) {
        sWhereString = sWhereString + " and a.cstockrdcenterid = '"
            + sRDID.trim() + "' ";
      }
      getUIRefPaneAdjustBill().setWhereString(sWhereString);
      getBillCardPanel().setBodyValueAt("", iRow, "cadjustbill");
    }
    else {
      getBillCardPanel().setBodyValueAt("", iRow, "cadjustbill");
      getBillCardPanel().getBodyItem("cadjustbill").setEdit(false);
      getBillCardPanel().getBodyItem("cadjustbill").setEnabled(false);
      getBillCardPanel().getBodyItem("cinventorycode").setEdit(true);
      getBillCardPanel().getBodyItem("cinventorycode").setEnabled(true);
      getUIRefPaneAdjustBill().setWhereString("");
      // ���ûس嵥�ݷ�¼
      BillItem bt = getBillCardPanel().getBodyItem("cadjustbillitem");
      if (bt != null && iRow != -1) {
        getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
        getBillCardPanel().getBillModel().setValueAt("", iRow,
            "cadjustbillitem");
        getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
        getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
      }
    }
  }

  /**
   * ÿ�������׳��쳣ʱ������
   * 
   * @param exception
   *          java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {
    /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
    Log.info("--------- δ��׽�����쳣 ---------");
    exception.printStackTrace(System.out);
  }

  /**
   * ͳһ��ȡϵͳ������ 0����ģ���Ƿ����ã� 1: ���ҵ������ 2���Ƿ��ǳ�ʼ�ʻ� 
   * 3��������ݾ��� 4�����õĻ���ڼ� 5: ��ͷ�Զ������ 6: �����Զ������ 
   * 7���Ƿ���ָ���ָ�����棬 7: �Ƿ������޸ġ�ɾ���Ǳ����Ƶ��ĵ��� 
   * 7: �Ƿ���ԭʼ�Ƶ���
   */
  private void getParas() throws Exception {
    ServcallVO[] scDisc = new ServcallVO[7];

    // ׼������
    // 0����ģ���Ƿ�����
    String[] sModules = new String[4];
    sModules[0] = ConstVO.m_sModuleCodeIC;
    sModules[1] = ConstVO.m_sModuleCodeSO;
    sModules[2] = ConstVO.m_sModuleCodePO;
    sModules[3] = ConstVO.m_sModuleCodeSC;

    scDisc[0] = new ServcallVO();
    scDisc[0].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[0].setMethodName("isModuleArrayStarted");
    scDisc[0].setParameterTypes(new Class[] {
        String.class, String[].class, String.class
    });
    scDisc[0].setParameter(new Object[] {
        m_sCorpID, sModules, ce.getAccountPeriod()
    });

    // 1: ���ҵ������
    /*
     * m_sFQSK �����һ���Զ��ŷָ����ַ�����ÿ���ַ�������һ�������տ����͵�ҵ������
     * ����ʹ��ʱ��indexof��������ĳ���ַ����Ƿ���m_sFQSK��
     */
    String[] sBizs = new String[2];
    sBizs[0] = ConstVO.m_sBizFQSK;// �����տ�
    sBizs[1] = ConstVO.m_sBizWTDX;// ί�д���
    scDisc[1] = new ServcallVO();
    scDisc[1].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[1].setMethodName("getBizTypeIDs");
    scDisc[1].setParameterTypes(new Class[] {
        String.class, String[].class
    });
    scDisc[1].setParameter(new Object[] {
        m_sCorpID, sBizs
    });

    // 2���Ƿ��ڳ�����
    scDisc[2] = new ServcallVO();
    scDisc[2].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[2].setMethodName("isBeginAccount");
    scDisc[2].setParameterTypes(new Class[] {
      String.class
    });
    scDisc[2].setParameter(new Object[] {
      m_sCorpID
    });

    // 3��������ݾ���
    scDisc[3] = new ServcallVO();
    scDisc[3].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[3].setMethodName("getDataPrecision");
    scDisc[3].setParameterTypes(new Class[] {
      String.class
    });
    scDisc[3].setParameter(new Object[] {
      m_sCorpID
    });

    // 4����ʼ����ڼ�
    scDisc[4] = new ServcallVO();
    scDisc[4].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[4].setMethodName("getStartPeriod");
    scDisc[4].setParameterTypes(new Class[] {
      String.class
    });
    scDisc[4].setParameter(new Object[] {
      m_sCorpID
    });

    String[] sParas = new String[4];
    // 5: �Ƿ���ָ���ָ������
    sParas[0] = ConstVO.m_sPk_Para[ConstVO.m_iPara_GBZDFS];
    // 5: �Ƿ������޸ġ�ɾ���Ǳ����Ƶ��ĵ���
    sParas[1] = ConstVO.m_sPk_Para[ConstVO.m_iPara_SFYXSGFBR];
    // 5: �Ƿ���ԭʼ�Ƶ���
    sParas[2] = ConstVO.m_sPk_Para[ConstVO.m_iPara_SFBLZDR];
    // 5: ���ⵥ�Ƿ������Զ��嵥��
    sParas[3] = ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDYDJ];

    // 5���Ƿ���ָ���ָ������,�Ƿ������޸ġ�ɾ���Ǳ����Ƶ��ĵ���,�Ƿ���ԭʼ�Ƶ���
    scDisc[5] = new ServcallVO();
    scDisc[5].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[5].setMethodName("getParaValues");
    scDisc[5].setParameterTypes(new Class[] {
        String.class, String[].class
    });
    scDisc[5].setParameter(new Object[] {
        m_sCorpID, sParas
    });

    // 6����ǰ����ڼ���ʼ����
    scDisc[6] = new ServcallVO();
    scDisc[6].setBeanName("nc.itf.ia.pub.ICommonDataQuery");
    scDisc[6].setMethodName("getMonthDates");
    scDisc[6].setParameterTypes(new Class[] {
        String.class, String.class
    });
    scDisc[6].setParameter(new Object[] {
        m_sCorpID, ce.getAccountPeriod()
    });

    // ִ��һ�κ�̨����
    Object[] oParaValue = nc.ui.scm.service.LocalCallService
        .callService(scDisc);

    // ��ȡ���
    // 0: ��ģ���Ƿ�����
    Hashtable ht = (Hashtable) oParaValue[0];
    // ����Ƿ�����
    m_bIsICStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeIC)).booleanValue();
    // �����Ƿ�����
    m_bIsSOStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeSO)).booleanValue();
    // �ɹ��Ƿ�����
    m_bIsPOStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodePO)).booleanValue();
    // ί��ӹ��Ƿ�����
    m_bIsSCStart = ((UFBoolean) ht.get(ConstVO.m_sModuleCodeSC)).booleanValue();

    // 1: ���ҵ������
    ht = (Hashtable) oParaValue[1];
    m_sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);// �����տ�
    m_sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);// ί�д���

    // 2���Ƿ��ڳ�����
    m_bIsBeginAccount = (UFBoolean) oParaValue[2];

    // 3: ������ݾ���
    Integer[] result = (Integer[]) oParaValue[3];
    m_iPeci = new int[result.length];
    for (int i = 0; i < result.length; i++) {
      m_iPeci[i] = result[i].intValue();
    }

    // 4: ���õĻ���ڼ�
    m_sStartPeriod = (String) oParaValue[4];

    // 5: �Ƿ���ָ���ָ������
    ht = (Hashtable) oParaValue[5];
    String sFlag = (String) ht.get(sParas[0]);
    if (sFlag != null) {
      m_iIndiFlag = new Integer(sFlag.substring(0, 1)).intValue();
    }
    // 5: �Ƿ������޸ġ�ɾ���Ǳ����Ƶ��ĵ���
    sFlag = (String) ht.get(sParas[1]);
    if (sFlag != null && (sFlag.equals("Y") || sFlag.equals("��"))) {/*-=notranslate=-*/
      m_bAllowChangeBillMkByOthers = true;
    }
    // 5: �Ƿ���ԭʼ�Ƶ���
    sFlag = (String) ht.get(sParas[2]);
    if (sFlag != null) {
      if ((sFlag.equals("Y") || sFlag.equals("��"))) {/*-=notranslate=-*/
        m_bKeepOriginalOperator = true;
      }
      else {
        m_bKeepOriginalOperator = false;
      }
    }
    // 5: ���ⵥ�Ƿ������Զ��嵥��
    sFlag = (String) ht.get(sParas[3]);
    if (sFlag != null) {
      if ((sFlag.equals("Y") || sFlag.equals("��"))) {/*-=notranslate=-*/
        m_bAllowDefinePriceByUser = true;
      }
      else {
        m_bAllowDefinePriceByUser = false;
      }
      for (int i = 0; i < ConstVO.m_sInBills.length; i++) {//������ⵥ�����Զ���
        if (ConstVO.m_sInBills[i].equals(m_sBillType)) {
          m_bAllowDefinePriceByUser = true;
          break;
        }
      }
    }
    // 6: ��ǰ����ڼ���ʼ����
    m_aBeginEndDates = (UFDate[]) oParaValue[6];

    // �Զ������
    m_voHeaddef = nc.ui.scm.pub.def.DefSetTool.getDefHead(m_sCorpID,ConstVO.IADEF);
    m_voBodydef = nc.ui.scm.pub.def.DefSetTool.getDefBody(m_sCorpID,ConstVO.IADEF);

  }

  /**
   * ��ʼ���ࡣ
   */
  /* ���棺�˷������������ɡ� */
  private void initialize(String pk_corp) {
    // ��õ�λ����
    m_sCorpID = (pk_corp == null ? ce.getCorporationID() : pk_corp);

    setName("BillClientUI");
    setLayout(null);
    setSize(774, 419);
    add(getBillCardPanel(), getBillCardPanel().getName());
    add(getBillListPanel(), getBillListPanel().getName());
    getUIComboBoxSource().addItemListener(billSourceListener);
    setLayout(new java.awt.CardLayout());

    try {
      // ���ð�ť״̬
      buttonTree = new ButtonTree(getFunCode());
      buttonTree.getButton(IABtnConst.BTN_SWITCH).
      setName(NCLangRes.getInstance().getStrByID("common","UCH022"/* �б���ʾ */));
      buttonTree.getButton(IABtnConst.BTN_CHOOSESALEBILL).setVisible(false);
      //add by yhj 2014-03-27
      buttonTree.getButton(IABtnConst.BTN_AUTO_PRICE);//�Զ�ѯ��
      //end
      //add by zy 2019-09-05
      buttonTree.getButton(IABtnConst.BTN_PASS_BIAOCAI);//�����
      //end
      m_aryButtonGroupCard = buttonTree.getButtonArray();
      m_aryButtonGroupList = buttonTree.getButtonArray();
      btnCtrl = new ButtonControl(buttonTree);
      
      // ��ȡ��������Ҫm_sCorpID�ѳ�ʼ��
      getParas();
      //���������붨λ�Ի���
      getLocateConditionDlg().setPeci(m_iPeci);
    }
    catch (Exception e) {
      e.printStackTrace();
      nc.ui.pub.beans.MessageDialog.showErrorDlg(getParent(),
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000059")/* @res "����" */, e.getMessage());
    }
  }

  /**
   * ��������:��ʼ������ <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private boolean isBeginAccount() {
    try {
      if (m_bIsBeginAccount == null) {
        m_bIsBeginAccount = CommonDataBO_Client.isBeginAccount(m_sCorpID);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return m_bIsBeginAccount.booleanValue();
  }

  /**
   * ��˾����Ӧ����ڼ��Ƿ��ѹ���
   * 
   * @return boolean
   * @param pk_corp
   *          java.lang.String
   * @param year
   *          java.lang.String
   * @param month
   *          java.lang.String
   */
  private boolean accountIsOpen() {
    String year = ce.getAccountYear();
    String month = ce.getAccountMonth();
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select caccountyear, caccountmonth, pk_corp from ");
    sql.append(" ia_accountclosed where dr = 0 ");
    sql.append(" and ");
    sql.append("pk_corp", m_sCorpID);
    sql.append(" and ");
    sql.append("caccountyear", year);
    sql.append(" and ");
    sql.append("caccountmonth", month);
    String[][] result = new String[0][0];
    try {
      // ��ѯ�Ƿ��й��˼�¼
      result = CommonDataBO_Client.queryData(sql.toString());
    }
    catch (Exception e) {
      Log.error(e);
      showErrorMessage(e.getMessage());
      showHintMessage(e.getMessage());
    }
    boolean value = true;
    if (result != null && result.length != 0) {
      Log.debug("����������ڼ� " + ce.getAccountPeriod() + " �Ѿ����ˣ����ٴ�������");
      showWarningMessage(NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000291", null, new String[] {
            ce.getAccountPeriod()
          }));
      value = false;
    }
    return value;
  }

  /**
   * ��������:Ϊ��������м��ع�ʽ�����ԴӴ����ʶ�д洢�� ��������������ر��룬���ƣ�����ͺţ���λ������ǰ�ƻ��� <p/> ����: <p/>
   * ����ֵ: <p/> �쳣:
   */
  private void loadInvFormula(BillItem bt) {
    if (bt != null && bt.getKey().equals("cinventoryid")) {
      java.util.Vector vTemp = new java.util.Vector();
      String sFor = "cinventorycode->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)";
      vTemp.addElement(sFor);
      // �������
      sFor = "cinventoryname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // ���
      sFor = "cinventoryspec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // �ͺ�
      sFor = "cinventorytype->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      sFor = "cinventorymeasname->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      // ��λ
      sFor = "cinventorymeasname->getColValue(bd_measdoc,measname,pk_measdoc,cinventorymeasname)";
      vTemp.addElement(sFor);
      // �������
      sFor = "cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinventorycode)";
      vTemp.addElement(sFor);
      String[] sFors = new String[vTemp.size()];
      vTemp.copyInto(sFors);
      bt.setLoadFormula(sFors);
      bt.setEditFormula(sFors);
    }
  }

  /**
   * ��������:���ݶ�λ������λ���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void locateBills() {
    // ��ȷ����ť
    Vector vLocateData = getLocateConditionDlg().getConditionValue();
    Object oKCZZ = vLocateData.elementAt(getLocateConditionDlg().m_iKCZZ);
    Object oCKBM = vLocateData.elementAt(getLocateConditionDlg().m_iCK);
    Object oZDRQ = vLocateData.elementAt(getLocateConditionDlg().m_iZDRQ);
    Object oDJH = vLocateData.elementAt(getLocateConditionDlg().m_iDJH);
    Object oBM = vLocateData.elementAt(getLocateConditionDlg().m_iBM);
    Object oYWY = vLocateData.elementAt(getLocateConditionDlg().m_iYWY);
    Object oKS = vLocateData.elementAt(getLocateConditionDlg().m_iKS);
    Object oCHBM = vLocateData.elementAt(getLocateConditionDlg().m_iCHBM);
    Object oPCH = vLocateData.elementAt(getLocateConditionDlg().m_iPCH);
    Object oSL = vLocateData.elementAt(getLocateConditionDlg().m_iSL);
    Object oDJ = vLocateData.elementAt(getLocateConditionDlg().m_iDJ);
    Object oJE = vLocateData.elementAt(getLocateConditionDlg().m_iJE);
    boolean bHasBodyCondition = false;
    if ((oCHBM != null && oCHBM.toString().trim().length() != 0)
        || (oPCH != null && oPCH.toString().trim().length() != 0)
        || (oSL != null && oSL.toString().trim().length() != 0)
        || (oDJ != null && oDJ.toString().trim().length() != 0)
        || (oJE != null && oJE.toString().trim().length() != 0)) {
      bHasBodyCondition = true;
    }
    if (m_voBills != null && m_voBills.length != 0) {
      boolean bHasFind = false;
      for (int i = 0; i < m_voBills.length; i++) {
        BillHeaderVO bhvo = (BillHeaderVO) m_voBills[i].getParentVO();
        // �жϿ����֯
        if (oKCZZ != null && oKCZZ.toString().trim().length() != 0) {
          if (bhvo.getCrdcenterid() == null
              || bhvo.getCrdcenterid().indexOf(oKCZZ.toString().trim()) == -1) {
            continue;
          }
        }
        // �жϲֿ����
        if (oCKBM != null && oCKBM.toString().trim().length() != 0) {
          if (bhvo.getCwarehouseid() == null
              || bhvo.getCwarehouseid().indexOf(oCKBM.toString().trim()) == -1) {
            continue;
          }
        }
        // �ж��Ƶ�����
        if (oZDRQ != null && oZDRQ.toString().trim().length() != 0) {
          if (bhvo.getDbilldate() == null
              || bhvo.getDbilldate().toString()
                  .indexOf(oZDRQ.toString().trim()) == -1) {
            continue;
          }
        }
        // �жϵ��ݺ�
        if (oDJH != null && oDJH.toString().trim().length() != 0) {
          if (bhvo.getVbillcode() == null
              || bhvo.getVbillcode().indexOf(oDJH.toString().trim()) == -1) {
            continue;
          }
        }
        // �жϲ���
        if (oBM != null && oBM.toString().trim().length() != 0) {
          if (bhvo.getCdeptid() == null
              || bhvo.getCdeptid().indexOf(oBM.toString().trim()) == -1) {
            continue;
          }
        }
        // �ж�ҵ��Ա
        if (oYWY != null && oYWY.toString().trim().length() != 0) {
          if (bhvo.getCemployeeid() == null
              || bhvo.getCemployeeid().indexOf(oYWY.toString().trim()) == -1) {
            continue;
          }
        }
        // �жϿ���
        if (oKS != null && oKS.toString().trim().length() != 0) {
          if (bhvo.getCcustomvendorid() == null
              || bhvo.getCcustomvendorid().indexOf(oKS.toString().trim()) == -1) {
            continue;
          }
        }
        // ��ͷ�����ѷ���
        bHasFind = true;
        // �жϷ�¼�Ƿ��з��������ļ�¼
        BillItemVO[] btvos = (BillItemVO[]) m_voBills[i].getChildrenVO();
        int length = btvos == null ? 0 : btvos.length;
        if (bHasBodyCondition) {
          // ���������ݣ�Ҫ������
          bHasFind = false;
          for (int j = 0; j < length; j++) {
            // �жϴ��
            if (oCHBM != null && oCHBM.toString().trim().length() != 0) {
              if (btvos[j].getCinventoryid() == null
                  || btvos[j].getCinventoryid()
                      .indexOf(oCHBM.toString().trim()) == -1) {
                continue;
              }
            }
            // �ж����κ�
            if (oPCH != null && oPCH.toString().trim().length() != 0) {
              if (btvos[j].getVbatch() == null
                  || btvos[j].getVbatch().indexOf(oPCH.toString().trim()) == -1) {
                continue;
              }
            }
            // �ж�����
            if (oSL != null && oSL.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oSL.toString().trim());
              if (btvos[j].getNnumber() == null
                  || btvos[j].getNnumber().equals(ud) == false) {
                continue;
              }
            }
            // �жϵ���
            if (oDJ != null && oDJ.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oDJ.toString().trim());
              if (btvos[j].getNprice() == null
                  || btvos[j].getNprice().equals(ud) == false) {
                continue;
              }
            }
            // �жϽ��
            if (oJE != null && oJE.toString().trim().length() != 0) {
              UFDouble ud = new UFDouble(oJE.toString().trim());
              if (btvos[j].getNmoney() == null
                  || btvos[j].getNmoney().equals(ud) == false) {
                continue;
              }
            }
            // ���ж����㣬����û��¼���¼�Ķ�λ����
            getBillListPanel().getHeadTable().setRowSelectionInterval(i, i);
            headChanged(new BillEditEvent(this, 0, i));
            getBillListPanel().getBodyTable().clearSelection();
            getBillListPanel().getBodyTable().setRowSelectionInterval(j, j);
            bHasFind = true;
            break;
          }
        }
        if (bHasFind) {
          getBillListPanel().getHeadTable().setRowSelectionInterval(i, i);
          headChanged(new BillEditEvent(this, 0, i));
          // getBillListPanel().getBodyTable().clearSelection();
          // getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
          break;
        }
      }
      if (bHasFind) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000047")/* @res "���ҵ���������������" */);
      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000048")/* @res "δ���ҵ���������������" */);
      }
    }
  }

  /**
   * ��������:�ù�ʽ�ѿ����֯�Ͳ�ѯ���Ĵ����Ϣ���ƻ��ۺͻ�׼��������� <p/> ������AddQueryVO ������ѯ�����Ͳ�ѯ�Ľ�� <p/>
   * ����ֵ: <p/> �쳣:
   */
  private void onAdd(AddQueryVO aqVO) {
    try {
      if (aqVO != null) {
        // ���ó�����״̬
        m_iStatus = ADD_STATUS;
        // ��õ�ǰ����
        String d1 = ce.getBusinessDate().toString();
        // ���ӿյ���
        getBillCardPanel().addBill(d1, m_vIsEnable);
        setBtnsForStatus(m_iStatus);
        // ɾ��Ĭ�����ӵĵ�һ������
        getBillCardPanel().delLine();
        String[] sInvmanIDs = aqVO.getInvmanIDs(); // �������ID
        UFDouble[] ufdbPlannedPrices = aqVO.getPlanedPrices(); // ����ǰ�ƻ���
        UFDouble[] ufdbBasePrices = aqVO.getBasePrices(); // ������׼��
        int iLength = 0;
        if (sInvmanIDs != null && ufdbPlannedPrices != null
            && ufdbBasePrices != null) {
          int iLenInv = sInvmanIDs.length;
          int iLenPlan = ufdbPlannedPrices.length;
          int iLenBase = ufdbBasePrices.length;
          if (iLenInv != iLenPlan || iLenInv != iLenBase) {
            Log.info("BillClientUI.onAdd: ��ѯ���Ĵ����Ŀ��۸��������");
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000049")/* @res "��ѯ����,��鿴��̨��־" */);
            return;
          }
          iLength = iLenInv;
        }
        else {
          Log.info("BillClientUI.onAdd: ��ѯ������ID��ƻ��ۻ��׼������Ϊ��");
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000049")/* @res "��ѯ����,��鿴��̨��־" */);
          return;
        }
        if (iLength == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000050")/*
                                                 * @res
                                                 * "δ��ѯ�����������ļ�¼,�������ѯ�������Ի��ֹ����Ӽƻ��۵�����"
                                                 */);
        }
        else {
          // Ϊ��ͷ�Ŀ����֯��ֵ
          getBillCardPanel().getHeadItem("crdcenterid").setValue(
              aqVO.getRDCenterID());
          // һ�����Ӷ���,����Ϊ��ѯ�������
          getBillCardPanel().getBodyPanel().addLine(iLength);
          // ���úϼ���������ÿһ��ʱ�Ȳ�����ϼ�ֵ
          getBillCardPanel().getBillModel().setNeedCalculate(false);
          for (int i = 0; i < iLength; i++) {

            // Ϊÿ�����Ӵ����Ϣ
            getBillCardPanel().setBodyValueAt(sInvmanIDs[i], i, "cinventoryid");
            // ��ʽ���ù�ʽ
            getBillCardPanel().getBillModel().execEditFormulaByKey(i,
                "cinventoryid");
            // ��д�ƻ��ۺͻ�׼��
            getBillCardPanel().setBodyValueAt(ufdbPlannedPrices[i], i,
                "noriginalprice");
            getBillCardPanel().setBodyValueAt(ufdbPlannedPrices[i], i,
                "nplanedprice");
            getBillCardPanel().setBodyValueAt(ufdbBasePrices[i], i, "nprice");
          }
          // �ָ�����ϼ�ֵ
          getBillCardPanel().getBillModel().setNeedCalculate(true);
          // ����ϼ���
          getBillCardPanel().getBillModel().reCalcurateAll();

          String[] value = new String[] {
            new Integer(iLength).toString()
          };
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000136", null, value));
        }
      }
    }
    catch (Exception e) {
      Log.error(e);
    }
  }

  /**
   * ��������:�����а�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonAddLineClicked() {
    if (buttonTree.getButton(IABtnConst.BTN_LINE_ADD).isEnabled() == false) {
      return;
    }
    // �Ա�ͷ�Ϸ����ж�
    if (getBillCardPanel().getBillModel().getRowCount() == 0) {
      // ���ݺſ����Զ����ɣ����ٱ�������
      // �����֯�Ƿ�¼��
      BillItem bt = getBillCardPanel().getHeadItem("crdcenterid");
      if (checkBillHeaderItem(bt) == false) {
        return;
      }
      // �Ƶ������Ƿ�¼��
      bt = getBillCardPanel().getHeadItem("dbilldate");
      if (checkBillHeaderItem(bt) == false) {
        return;
      }
      // if (m_bIsAdjustBill == false &&
      // m_sBillType.equals(ConstVO.m_sBillXSCBJZD) == false
      // && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false)
      // {
      // //�շ�����Ƿ�¼��
      // bt = getBillCardPanel().getHeadItem("cdispatchid");
      // if (checkBillHeaderItem(bt) == false)
      // {
      // return;
      // }
      // }
      // if (m_sBillType == ConstVO.m_sBillCGRKD
      // || m_sBillType == ConstVO.m_sBillXSCBJZD
      // || m_sBillType == ConstVO.m_sBillQCXSCBJZD)
      // {
      // //ҵ�������Ƿ�¼��
      // bt = getBillCardPanel().getHeadItem("cbiztypeid");
      // if (checkBillHeaderItem(bt) == false)
      // {
      // return;
      // }
      // }
    }
    getBillCardPanel().addLine();
    setBtnsForStatus(UPDATE_STATUS);
    // ��ʾ����
    int i = getBillCardPanel().getRowCount() - 1;
    java.awt.Rectangle rect = getBillCardPanel().getBillTable().getCellRect(i,
        0, false);
    getBillCardPanel().getBillTable().scrollRectToVisible(rect);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000051")/* @res "����һ�з�¼" */);
  }

  /**
   * ��������:�����鰴ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonAssociateBillsClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000052")/* @res "���ڲ�ѯ���ݣ����Ժ�" */);
    if (m_voCurBill == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000053")/* @res "��ǰ����û��������Ϣ" */);
      return;
    }
    String sBillTypeCode = m_voCurBill.getBillTypeCode();
    String sBillID = ((BillHeaderVO) m_voCurBill.getParentVO()).getCbillid();
    String sUserID = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getCoperatorid();
    String sCorpID = m_voCurBill.getPk_corp();
    nc.ui.scm.sourcebill.SourceBillFlowDlg dlgSoure = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        this, sBillTypeCode, sBillID, null, sUserID, sCorpID);
    dlgSoure.showModal();
  }

  /**
   * ��������:����˰�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonAuditClicked() {
    // ��˵���
    boolean bHasInvi = false;
    int iRowIndex = getBillListPanel().getBodyTable().getSelectedRow();
    if (iRowIndex == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000054")/* @res "��ѡ��Ҫ����ָ���ķ�¼" */);
      return;
    }

    // �ж��Ƿ�Ҫ���ָ���Ƽ۷���Ľ��棨��ⵥ���ó��֣�
    Vector vBillItem = new Vector(1, 1);
    BillHeaderVO bhvo = (BillHeaderVO) m_voCurBill.getParentVO();
    BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRowIndex];
    // ��������Ƽ�
    Integer iPriceMode = btvo.getFpricemodeflag();
    Integer iRDFlag = bhvo.getFdispatchflag();
    //
    if (iPriceMode.intValue() == ConstVO.GBJJ) {
      // �Ǹ���Ƽ۵Ĵ��
      if (iRDFlag.intValue() == 1) {
        if (btvo.getNnumber().doubleValue() > 0) {
          // �����۳ɱ���ת�������ϳ��ⵥ���������ⵥ�����ϵ���ί��ӹ����ϵ�
          vBillItem.addElement(btvo);
        }
      }
      else if (m_bIsAdjustBill == false && btvo.getNnumber().doubleValue() < 0) {
        // ������ⵥ
        BillItemVO btvoout = (BillItemVO) btvo.clone();
        btvoout.setNnumber(new UFDouble(-btvo.getNnumber().doubleValue()));
        btvoout.setNmoney(new UFDouble(-btvo.getNmoney().doubleValue()));
        vBillItem.addElement(btvoout);
      }
    }
    try {
      if (vBillItem.size() != 0) {
        bHasInvi = true;
        getIndividualAllotDlg().setInfo(m_voCurBill, vBillItem, m_iIndiFlag);
        if (m_iIndiFlag == ConstVO.m_iHand) {
          // �и���Ƽ۵Ĵ��,Ҫ��ʾ����ָ������
          getIndividualAllotDlg().showModal();
        }
        else if (getIndividualAllotDlg().getUIButtonOK_pub().isEnabled()) {
          // ���Ը���ָ��
          auditOneBill(true, "123");
        }
      }
      if (bHasInvi == false) {
        auditOneBill(false, "123");
      }
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage( ex , this );
    }
  }

  /**
   * ��������:��ȡ����ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonCancelClicked() {
    try {
      m_bIsChangeEvent = false;
      if (m_iStatus == UPDATE_STATUS && m_voCurBill != null) {
        // ���ñ༭����
        BillItem[] bts = getBillCardPanel().getBodyItems();
        for (int i = 0; i < bts.length; i++) {
          bts[i].setEdit(m_bBodyEditFlags[i]);
        }
      }
      m_bIsChangeEvent = true;
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000055")/* @res "����ʧ��" */);
      return;
    }
    m_iStatus = CARD_STATUS;
    // �ָ���������
    getBillCardPanel().resumeValue();
    if (m_iStatus == ADD_STATUS
        && getBillCardPanel().getHeadItem("vbillcode").getValue() == null) {
      // ��ȥ���ݺ��ֶε�����
      getBillCardPanel().getHeadItem("vbillcode").setValue("");
    }
    // ʹ���治�ɱ༭
    getBillCardPanel().setEnabled(false);
    // ��չ̶��ʲ���Ϣ
    // clearFAData();
    setBtnsForStatus(m_iStatus);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000056")/* @res "ȡ���ɹ�" */);
  }

  /**
   * ��������:����յ��ݰ�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonChooseSaleBillClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000052")/* @res "���ڲ�ѯ���ݣ����Ժ�" */);
    // ��ѯ����
    getSaleBillsChooseDlg().showModal();
  }

  /**
   * ��������:�㸴���а�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonCopyLineClicked() {
    int i = getBillCardPanel().getBillTable().getSelectedRow();
    if (i == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000057")/* @res "��ѡ��Ҫ���Ƶ���" */);
      return;
    }
    getBillCardPanel().copyLine();
    this.m_bCanPasteLine = true;
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000058")/* @res "������" */);
  }

  /**
   * ��������:��ɾ����ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonDelClicked() {
    // �ж��Ƿ�����޸��������Ƶĵ���
    if (canAlterBillMadeByOthers(m_voCurBill) == false) {
      return;
    }
    int iStatus = m_iStatus;
    // ��ʾ��ʾ��Ϣ
    String sTitle = NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000061");/* @res "����ά��" */
    String sMessage = NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000059")/* @res "ȷ��ɾ�����ŵ���?" */;
    try {
      // �ж��Ƿ��ѹ���,���˺�����ɾ������
      if (!accountIsOpen())
        return;
      /* ��ǰvo�Ƿ��ڻ�����,�������, �������ӵĵ���, �����, �ǲ�ѯ�õ��Ľ�� */
      boolean bCurBillInCache = false;
      
      BillHeaderVO header = (BillHeaderVO ) m_voCurBill.getParentVO();
      BillItemVO[] items = (BillItemVO[]) m_voCurBill.getChildrenVO();
      //�ǲ��ϳ��ⵥ�����Ǽ����ϵ���
      if( header.getCbilltypecode().equals( ConstVO.m_sBillCLCKD) && 
          header.getBwithdrawalflag().booleanValue() ){
        for( int i =0;i< items.length;i++){
          if( items[i].getNnumber().doubleValue() > 0 ){
            String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000295");
            /* @res "�����������¼����ϵ����½����ɵģ�����ɾ��" */
            throw new BusinessException( message );
          }
        }
      }
      
      if (m_voBills != null) {
        for (int i = 0; i < m_voBills.length; i++) {
          if (m_voCurBill.getParentVO().getPrimaryKey().equals(
              m_voBills[i].getParentVO().getPrimaryKey())) {
            bCurBillInCache = true;
            break;
          }
        }
      }
      if (!bCurBillInCache) {
        // ��ǰvo���ٻ�����,����������,ɾ��ʱ���������б������״̬
        if (getBillCardPanel().isVisible()) {
          int iType = MessageDialog.showYesNoDlg(this, sTitle, sMessage,
              UIDialog.ID_NO);
          if (iType == MessageDialog.ID_YES) {
            // ɾ��
            boolean flag = delete(m_voCurBill);
            if (!flag)
              return;// ɾ����������
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
            btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
            btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
            btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
            btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
            btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
            btnCtrl.set(false,IABtnConst.BTN_PRINT);
            btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
            btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
            setBtnsForBilltypes(m_aryButtonGroupCard);
            m_iStatus = INIT_STATUS;
            getBillCardPanel().getBillData().clearViewData();
            getBillCardPanel().updateValue();
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000225")/* @res "ɾ���ɹ�" */);
          }
        }
      }
      else {
        // ��ǰvo�ڻ�����,�ǲ�ѯ���ĵ���,�������۴��ڿ�Ƭ���б������¶�Ҫ�����б�����
        // ��״̬,���ڿ�Ƭ�����»�Ҫ���ÿ�Ƭ��״̬
        // �����ϵ��к����Ӧ��vo�ڻ����е�λ�õĶ�Ӧ
        HashMap hmRow2Index = new HashMap();
        int iFocusRowAfterDelete = -1;
        int iRowNum = getBillListPanel().getHeadTable().getSelectedRowCount();
        if (iRowNum == 0) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000060")/* @res "��ѡ��Ҫ���ϵĵ���" */);
          return;
        }
        /* ��¼Ҫɾ���ĵ����ڽ����ϵ��кź��ڻ����е�˳��� */
        if (iRowNum >= 1) {
          sMessage = NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000137", null, new String[] {
                new Integer(iRowNum).toString()
              }/* "ȷ��ɾ����{0}�ŵ���?" */);
          for (int i = 0; i < iRowNum; i++) {
            int iIndex = getBillListPanel().getHeadTable().getSelectedRows()[i];// ��ǰ�к�
            String sCurRow = String.valueOf(iIndex);
            String sCacheRow = String.valueOf(iIndex);
            hmRow2Index.put(sCurRow, sCacheRow);
            if (m_voBills[iIndex].getChildrenVO() == null) {
              // ��û�л�ñ�������
              ClientLink cl = ce.getClientLink();
              m_voBills[iIndex] = BillBO_Client.querybillWithOtherTable(null,
                  null, null, null, null, m_voBills[iIndex],
                  new Boolean(false), cl)[0];
            }
          }
          // �ҵ�ɾ����ɺ�λ����һ��
          iFocusRowAfterDelete = getBillListPanel().getHeadTable()
              .getSelectedRows()[iRowNum - 1] + 1;// ѡ���������һ�е���һ��
          if (iFocusRowAfterDelete >= getBillListPanel().getHeadTable()
              .getRowCount()) {// ѡ�������һ��
            iFocusRowAfterDelete = getBillListPanel().getHeadTable()
                .getSelectedRows()[0] - 1;// ѡ�����е�һ�е���һ��
          }
          else {
            iFocusRowAfterDelete -= iRowNum;
          }
        }
        int iType = MessageDialog.showYesNoDlg(this, sTitle, sMessage,
            UIDialog.ID_NO);
        if (iType == MessageDialog.ID_YES) {
          /* ��ѡ�еĵ���ִ��ɾ������ */
          int len = hmRow2Index.size();
          Set keys = hmRow2Index.keySet();
          String[] sKeys = (String[]) keys.toArray(new String[len]);// ��ǰ�к�����
          int[] iRows = new int[len];
          int iRow = 0;
          int iCacheRow = 0;
          BillVO voToBeDeleted = null;
          boolean flag = true;
          for (int i = 0; i < len; i++) {
            iRow = Integer.parseInt(sKeys[i]);// �����ϵ��к�
            iRows[i] = iRow;
            iCacheRow = Integer.parseInt((String) hmRow2Index.get(sKeys[i]));// �����е�˳���
            voToBeDeleted = m_voBills[iCacheRow];
            // ɾ��
            flag = delete(voToBeDeleted);
            if (!flag)
              return;// ɾ����������
            m_voBills[iCacheRow] = null;// �ڻ����б��ɾ��
          }
          getBillListPanel().getHeadBillModel().delLine(iRows);// �б�����ɾ��
          ArrayList tempvo = new ArrayList(m_voBills.length);// �������� ��ʼ
          for (int i = 0; i < m_voBills.length; i++) {
            if (m_voBills[i] != null) {
              tempvo.add(m_voBills[i]);
            }
          }
          m_voBills = new BillVO[tempvo.size()];
          m_voBills = (BillVO[]) tempvo.toArray(m_voBills);// �������� ����
          /* �������ý����ϵ�ѡ���� */
          if (m_voBills != null && m_voBills.length != 0) {
            m_voCurBill = null;
            m_iCurBillPrt = -1;
            String sBillId = null;
            if (iFocusRowAfterDelete >= 0) {
              sBillId = (String) getBillListPanel().getHeadBillModel()
                  .getValueAt(iFocusRowAfterDelete, "cbillid");
            }
            if (sBillId != null) {
              // �ڻ������ҵ�������ѡ���е�VO
              for (int i = 0; i < m_voBills.length; i++) {
                if (sBillId.equals(m_voBills[i].getParentVO().getPrimaryKey())) {
                  getBillListPanel().getHeadTable().setRowSelectionInterval(
                      iFocusRowAfterDelete, iFocusRowAfterDelete);
                  showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "20143010", "UPP20143010-000225")/* @res "ɾ���ɹ�" */);
                  break;
                }
              }
            }
          }
          else {
            // ����û������
            m_voCurBill = null;
            m_iCurBillPrt = -1;
            getBillListPanel().setBodyValueVO(null);
            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
            btnCtrl.set(false,IABtnConst.BTN_BROWSE_LOCATE);
            btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
            btnCtrl.set(false,IABtnConst.BTN_PRINT);
            btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
            btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
            setBtnsForBilltypes(m_aryButtonGroupList);
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000225")/* @res "ɾ���ɹ�" */);
          }
          getBillListPanel().updateUI();
          if (getBillCardPanel().isVisible()) {
          	if( m_voCurBill != null ){
          		displayInCard();
          	} else{
	            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
	            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
	            btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
	            btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
	            btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
	            btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
	            btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
	            btnCtrl.set(false,IABtnConst.BTN_PRINT);
	            btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
	            btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
	            setBtnsForBilltypes(m_aryButtonGroupCard);
	            m_iStatus = INIT_STATUS;
	            getBillCardPanel().getBillData().clearViewData();
          	}
          }
          else {
            m_iStatus = LIST_STATUS;
          }
          getBillCardPanel().updateValue();
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000225")/* @res "ɾ���ɹ�" */);
        }
        else {
          m_iStatus = iStatus;
        }
      }
      setBtnsForStatus(m_iStatus);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * ִ��ɾ������
   * @param bvo
   * @return
   */
  private boolean delete(BillVO bvo) {
    if (bvo == null) {
      return false;
    }
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    if (bhvo.getCbilltypecode().equals(ConstVO.m_sBillQCRKD)
        || bhvo.getCbilltypecode().equals(ConstVO.m_sBillQCXSCBJZD)) {
      // bhvo.getCsourcemodulename() != null &&
      // bhvo.getCsourcemodulename().equals(ConstVO.m_sModuleIC)
      // 2003-11-06.1043 modi by godwit
      // �ڳ����ݣ�Ŀǰ��������ڳ����ݲ������ϣ�Ӧ��Ϊ����¼������������ϡ�
    }
    else if (bhvo.getCsourcemodulename() != null
        && bhvo.getCsourcemodulename().trim().equals(ConstVO.m_sModuleCA) == false
        && bhvo.getCsourcemodulename().trim().equals(ConstVO.m_sModuleIA) == false) {
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010","UPP20143010-000062")/*@res
      // "���ŵ�������ϵͳ����ģ��޷�ɾ��"*/);
      // return;
    }

    // ȷ��ɾ��
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000063")/*
                               * @res "����ɾ�����ݣ����Ժ�"
                               */);
    try {
      // ////
      ClientLink cl = ce.getClientLink();
      BillBO_Client.delete(cl, bvo, ce.getUser().getPrimaryKey(), m_sFQSK,
          m_sWTDX);
      // ////
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage( ex , this );
      return false;
    }
    return true;
  }

  /**
   * ��������:��ɾ�а�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonDelLineClicked() {
    int i = getBillCardPanel().getBillTable().getSelectedRow();
    if (i == -1) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000201")/*
                                 * @res "��ѡ��Ҫɾ������"
                                 */);
      return;
    }
    getBillCardPanel().delLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000066")/*
                               * @res "ɾ��һ�з�¼"
                               */);
  }

  /**
   * ��������:�㵼�밴ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
//  private void onButtonImportClicked() {
//    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
//        "UPP20143010-000067")/* @res "���ڵ��뵥�ݣ����Ժ�" */);
//    // �������ݣ����ڳ�����¼����ʵ��
//    try {
//      // ����������
//      m_voBills = BillBO_Client.importBills(
//          ce.getCorporationID(),
//          ce.getBusinessDate().toString(),
//          ce.getUser().getPrimaryKey(),
//          ConstVO.m_sModuleCodeIC);
//      if( m_voBills != null && m_voBills.length >0 ){
//        setBillsInList(m_voBills);
//      }
//    }
//    catch (Exception ex) {
//      ExceptionUITools.showMessage(ex, this);
//      return;
//    }
//    if (m_voBills == null || m_voBills.length == 0) {
//      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//          "20143010", "UPP20143010-000068")/* @res "û�з����������ڳ�����" */);
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
//          "UPP20143010-000068")/* @res "û�з����������ڳ�����" */);
//      return;
//    }
//    int iLength = 0;
//    if (m_voBills != null) {
//      iLength = m_voBills.length;
//    }
//    String[] value = new String[] {
//      new Integer(iLength).toString()
//    };
//    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
//        "UPP20143010-000138", null, value));
//    // showHintMessage("������ɣ�������" + iLength + "�ŵ���");
//  }

  /**
   * ��������:�㵼�밴ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonImportClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000067")/* @res "���ڵ��뵥�ݣ����Ժ�" */);
    // �������ݣ����ڳ�����¼����ʵ��
    BillVO[] bvoBeginBills = null;
    try {
      // ����������
      BillVO[] bvoICBeginBills = importBills(ConstVO.m_sModuleCodeIC);
      // ������������
      BillVO[] bvoSOBeginBills = new BillVO[0]; // importBills(ConstVO.m_sModuleCodeSO);
      bvoBeginBills = new BillVO[bvoICBeginBills.length
          + bvoSOBeginBills.length];
      for (int i = 0; i < bvoICBeginBills.length; i++) {
        bvoBeginBills[i] = bvoICBeginBills[i];
      }
      for (int i = 0; i < bvoSOBeginBills.length; i++) {
        bvoBeginBills[i + bvoICBeginBills.length] = bvoSOBeginBills[i];
      }
      // 1����ÿ��ϵͳ�ڳ�����
      if (bvoBeginBills == null || bvoBeginBills.length == 0) {
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000068")/* @res "û�з����������ڳ�����" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000068")/* @res "û�з����������ڳ�����" */);
        return;
      }
      // 2����������
      // 2.1 ���뵥��
      ClientLink cl = ce.getClientLink();
      m_voBills = BillBO_Client.insertArrayForBeginBills(cl, bvoBeginBills);
      // 3��������ڳ�������ʾ���б�������
      setBillsInList(m_voBills);
      int iLength = 0;
      if (m_voBills != null) {
        iLength = m_voBills.length;
      }
      // showHintMessage("������ɣ�������" + iLength + "�ŵ���");
      String[] value = new String[] {
        new Integer(iLength).toString()
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000138", null, value));
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }
  
  /**
   * ��������:������а�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonInsertLineClicked() {
    getBillCardPanel().insertLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000070")/* @res "����һ�з�¼" */);
  }

  /**
   * ��������:�㶨λ��ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonLocateClicked() {
    // ��ʾ��λ�����Ի���
    getLocateConditionDlg().showModal();
  }

  /**
   * ��������:��ȷ����ť�����������û������ֵ <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private boolean onButtonOKClicked() {
    long t1 = System.currentTimeMillis();
    long t2 = 0;
    long t3 = 0;
    long t4 = 0;
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000071")/* @res "���ڱ��浥�ݣ����Ժ�" */);
    getBillCardPanel().stopEditing();
    // �����������ݣ�������ɾ��
    int iRowCount = getBillCardPanel().getRowCount();
    Vector vDelRows = new Vector(1, 1);
    for (int i = iRowCount - 1; i >= 0; i--) {
      Object oInvID = getBillCardPanel().getBillModel().getValueAt(i,
          "cinventoryid");
      if (oInvID == null || oInvID.toString().trim().length() == 0) {
        // û����������Ϣ��ɾ������
        vDelRows.addElement(new Integer(i));
      }
    }
    if (vDelRows.size() != 0) {
      int[] iDelRows = new int[vDelRows.size()];
      for (int i = 0; i < iDelRows.length; i++) {
        iDelRows[i] = ((Integer) vDelRows.elementAt(i)).intValue();
        //
        Log.info("ɾ����" + (iDelRows[i] + 1) + "������");
      }
      getBillCardPanel().getBillModel().delLine(iDelRows);
    }
    // t2 = System.currentTimeMillis() -t1;
    if (getBillCardPanel().getBillModel().getRowCount() == 0) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000072")/* @res "û�б������ݣ������" */);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000072")/* @res "û�б������ݣ������" */);
      return false;
    }
    // ����VO����
    if (m_iStatus == ADD_STATUS) {
      // �����Ӻ󱣴�
      // ͨ���������ͻ���շ���־
      if ((m_bIsOtherBill || m_bIsOutBill)) {
        // �ǳ��ⵥ
        getBillCardPanel().setHeadItem("fdispatchflag", new Integer(1));
      }
      else {
        // ���ǳ��ⵥ
        getBillCardPanel().setHeadItem("fdispatchflag", new Integer(0));
      }
      BillVO bvo = new BillVO(getBillCardPanel().getRowCount());
      // ��������
      try {
        // �Ƿ��ѹ���
        if (!accountIsOpen())
          return false;
        // �ѽ���������д��vo��
        // ��ȡ������ʱ��
        UFDateTime time = ClientEnvironment.getServerTime();
        // �Ƶ�ʱ��
        getBillCardPanel().setTailItem("tmaketime", time);
        // ����޸�ʱ��
        getBillCardPanel().setTailItem("tlastmaketime", time);
        // ����޸���
        getBillCardPanel().setTailItem("clastoperatorname",
            ce.getUser().getUserName());
        getBillCardPanel().setTailItem("clastoperatorid",
            ce.getUser().getPrimaryKey());
        getBillCardPanel().getBillValueVO(bvo);
        // �Ϸ��Լ�鲢�������
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }
        // t3 = System.currentTimeMillis() -t1 - t2;
        ClientLink cl = ce.getClientLink();
        // ��������
        if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
            || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // ���ڳ����ݣ�������ʵʱƾ֤��������һ������
          bvo = BillBO_Client.insertForBeginBills(cl, bvo);
        }
        else {
          bvo = BillBO_Client.insert(cl, bvo);
        }
        t4 = System.currentTimeMillis() - t3 - t2 - t1;
        Log.info("--------------------------�������ݿ�ʱ��Ϊ��" + t4 + "ms");
        m_voCurBill = bvo;
        m_iCurBillPrt = -1;
        // ������Զ����ɵĵ��ݺţ���Ѵ˵��ݺ����������Ӧλ��
        BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
        getBillCardPanel().setHeadItem("vbillcode", bhvo.getVbillcode());
        // ��ʾ��Ϣ
        // showHintMessage("�������ݳɹ�" + " " + "����ʱ��"+ t4 + "��");
        String sInfo = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000171")/* @res�������ݳɹ� */;
        String[] value = new String[] {
          new Double(t4 / 1000.0).toString()
        };
        String sTime = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000172", null, value);/* "����ʱ��"+ t4 + "��" */
        showHintMessage(sInfo + " " + sTime);
      }
      catch (ValidationException exception) {
        showErrorMessage(exception.getLocalizedMessage());
        showHintMessage(exception.getLocalizedMessage());
        exception.printStackTrace();
        return false;
      }
      catch (BusinessException ee) {
        showErrorMessage(ee.getMessage());
        showHintMessage(ee.getMessage());
        ee.printStackTrace();
        return false;
      }
      catch (Exception e) {
        e.printStackTrace();
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000073")/* @res "�������ݳ���" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000073")/* @res "�������ݳ���" */
            + e.getMessage());
        return false;
      }
    }
    else if (m_iStatus == UPDATE_STATUS) {
      // ���޸ĺ󱣴�
      BillVO bvo = new BillVO(getBillCardPanel().getRowCount());
      // ���������кŶ�Ӧ����
      Hashtable ht = new Hashtable();
      int iRowNum = getBillCardPanel().getBillModel().getRowCount();
      for (int i = 1; i <= iRowNum; i++) {
        getBillCardPanel().getBillModel().setValueAt(new Integer(i), i - 1,
            "irownumber");
        Object oPK = getBillCardPanel().getBillModel().getValueAt(i - 1,
            "cbill_bid");
        if (getBillCardPanel().getBillModel().getRowState(i - 1) == BillModel.ADD) {
          oPK = "";
        }
        if (oPK != null && oPK.toString().trim().length() != 0) {
          ht.put(oPK, new Integer(i));
        }
      }
      try {
        // �Ƿ��ѹ���
        if (!accountIsOpen())
          return false;
        // ���кŸ�ֵ����
        BillItemVO[] btvos = (BillItemVO[]) m_voCurBill.getChildrenVO();
        for (int i = 0; i < btvos.length; i++) {
          if (btvos[i].getStatus() == BillModel.NORMAL) {
            String sPK = btvos[i].getPrimaryKey();
            Integer iRow = (Integer) ht.get(sPK);
            if (iRow != null && btvos[i].getIrownumber() != null
                && iRow.intValue() != btvos[i].getIrownumber().intValue()) {
              getBillCardPanel().getBillModel().setRowState(
                  iRow.intValue() - 1, BillModel.MODIFICATION);
            }
          }
        }
        UFDateTime time = ClientEnvironment.getServerTime();
        // ����޸�ʱ��
        getBillCardPanel().setTailItem("tlastmaketime", time);
        // ����޸���
        getBillCardPanel().setTailItem("clastoperatorname",
            ce.getUser().getUserName());
        getBillCardPanel().setTailItem("clastoperatorid",
            ce.getUser().getPrimaryKey());
        /* Ϊ���ƻ��۵���������ͬʱ��2����¼��ͬһ������������ȼ��һ��ȫ������ */
        getBillCardPanel().getBillValueVO(bvo);
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }

//        bvo = (BillVO) getBillCardPanel().getBillValueChangeVO(
//            "nc.vo.ia.bill.BillVO", "nc.vo.ia.bill.BillHeaderVO",
//            "nc.vo.ia.bill.BillItemVO");
        // �޸ļ�¼
        // �Ϸ��Լ�鲢�������
        if (checkData(bvo) == false) {
          showErrorMessage(m_sErrorMessage);
          showHintMessage(m_sErrorMessage);
          return false;
        }
        // ����������Ϊɾ���ġ��޸ĵġ����ӵ�
        Vector vDeleteData = new Vector(1, 1);
        Vector vUpdateData = new Vector(1, 1);
        Vector vAddData = new Vector(1, 1);
        BillItemVO[] bnowtvos = (BillItemVO[]) bvo.getChildrenVO();
        for (int i = 0; i < bnowtvos.length; i++) {
          BillItemVO btvo = bnowtvos[i];
          //add by yhj 2014-03-31
          btvo.setStatus(1);
          //end
          if (btvo.getStatus() == VOStatus.NEW) {
            vAddData.addElement(btvo);
          }
          else if (btvo.getStatus() == VOStatus.UPDATED) {
            vUpdateData.addElement(btvo);
          }
          else if (btvo.getStatus() == VOStatus.DELETED) {
            vDeleteData.addElement(btvo);
          }
        }
        // ����
        BillItemVO[] curbtVOs = new BillItemVO[vAddData.size()
            + vUpdateData.size() + vDeleteData.size()];
        int ivo = 0;
        int jvo = 0;
        int hvo = 0;
        int iIndex = 0;
        // ɾ���ĴӺ���ǰ
        int iLength = vDeleteData.size();
        for (ivo = iLength - 1; ivo >= 0; ivo--) {
          curbtVOs[iIndex++] = (BillItemVO) vDeleteData.elementAt(ivo);
        }
        int iLength2 = vUpdateData.size();
        for (jvo = 0; jvo < iLength2; jvo++) {
          curbtVOs[iIndex++] = (BillItemVO) vUpdateData.elementAt(jvo);
        }
        for (; hvo < vAddData.size(); hvo++) {
          curbtVOs[iIndex++] = (BillItemVO) vAddData.elementAt(hvo);
        }
        bvo.setChildrenVO(curbtVOs);
        ClientLink cl = ce.getClientLink();
        // �޸�����
        if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
            || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // ���ڳ����ݣ�������ʵʱƾ֤��������һ������
          m_voCurBill = BillBO_Client.updateForBeginBill(cl, m_voCurBill, bvo,
              ce.getUser().getPrimaryKey());
        }
        else {
          m_voCurBill = BillBO_Client.update(cl, m_voCurBill, bvo,
              new UFBoolean((m_bIsOtherBill || m_bIsOutBill)), new UFBoolean(
                  m_bIsAdjustBill), ce.getUser().getPrimaryKey());
        }
        // ������֯��������
        if (m_voBills != null && m_voBills.length != 0) {
          boolean bHasFind = false;
          int iSelectIndex = -1;
          for (int i = 0; i < m_voBills.length; i++) {
            BillHeaderVO bhvo = (BillHeaderVO) m_voBills[i].getParentVO();
            BillHeaderVO bcurhvo = (BillHeaderVO) m_voCurBill.getParentVO();
            if (bhvo.getCbillid().equals(bcurhvo.getCbillid())) {
              m_voBills[i] = m_voCurBill;
              m_iCurBillPrt = i;
              iSelectIndex = i;
              bHasFind = true;
              break;
            }
          }
          if (bHasFind) {
            // �����б���ʽ�ı�������
            BillHeaderVO[] voBillHeads = null;
            Vector vTempHeads = new Vector(1, 1);
            for (int i = 0; i < m_voBills.length; i++) {
              BillVO voBill = m_voBills[i];
              vTempHeads.addElement(voBill.getParentVO());
            }
            voBillHeads = new BillHeaderVO[vTempHeads.size()];
            vTempHeads.copyInto(voBillHeads);
            getBillListPanel().setHeaderValueVO(voBillHeads);
            getBillListPanel().setBodyValueVO(m_voCurBill.getChildrenVO());
            getBillListPanel().getHeadTable().setRowSelectionInterval(
                iSelectIndex, iSelectIndex);
            for (int iii = 0; iii < voBillHeads.length; iii++) {
              BillHeaderVO bthisVO = voBillHeads[iii];
              Object oSource = bthisVO.getCsourcemodulename();
              if (oSource != null && oSource.toString().trim().length() != 0) {
                String sSource = ce.changeModuleCodeToName(oSource.toString()
                    .trim());
                getBillListPanel().getHeadBillModel().setValueAt(sSource, iii,
                    "csourcemodulename");
              }
              // ���ü����ϣ��ݹ�����������, ��Դ��������
              setComboBoxInHeadFromVO(m_voBills[iii], false, iii);
            }
            // ��������趨Ϊ������ԭʼ�Ƶ��ˣ���ѵ�ǰ����Ա��Ϊ�Ƶ���
            if (!m_bKeepOriginalOperator) {
              getBillCardPanel().setTailItem("coperatorname",
                  ce.getUser().getUserName());
              getBillCardPanel().setTailItem("coperatorid",
                  ce.getUser().getPrimaryKey());
            }
            // ��ʽ���÷���,ʹ��ʽ����
            // getBillListPanel().getHeadBillModel().execLoadFormula();
            execListPanelHeadFormula();
            execListPanelBodyFormula();
          }
        }
      }
      catch (nc.vo.pub.ValidationException exception) {
        showErrorMessage(exception.getMessage());
        showHintMessage(exception.getMessage());
        exception.printStackTrace();
        return false;
      }
      catch (nc.vo.pub.BusinessException ee) {
        showErrorMessage(ee.getMessage());
        showHintMessage(ee.getMessage());
        return false;
      }
      catch (Exception e) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000074")/* @res "�޸����ݳ���" */
            + e.getMessage());
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000074")/* @res "�޸����ݳ���" */
            + e.getMessage());
        return false;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000075")/* @res "�޸����ݳɹ�" */);
    }
    // ˢ��ts
    // freshTs(m_voCurBill);
    getBillCardPanel().setEnabled(false);
    // ���ÿɱ༭״̬
    m_iStatus = CARD_STATUS;
    setBtnsForStatus(m_iStatus);
    // ��VO�������õ�������
    setVODataToInterface(m_voCurBill);
    // ɾ������Ŀɱ༭�е�����
    getBillCardPanel().getBillModel().clearCellEdit();
    try {
      // �Ƿ񱣴���������
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        String sAudit = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDSHJZ);
        if (sAudit != null) {
          if (sAudit.equalsIgnoreCase("Y") || sAudit.equalsIgnoreCase("��")) {/*-=notranslate=-*/
            // ���ݱ����Զ��ɱ����㣬��ǰ�İ汾
            auditOneBill(false, "012");
          }
          else if (sAudit.equalsIgnoreCase("N") || sAudit.equalsIgnoreCase("��")) {/*-=notranslate=-*/
            // ���ݱ��治�Զ��ɱ����㣬��ǰ�İ汾
          }
          else if (sAudit.trim().length() != 0) {
            // �²���������Ҫ�ɱ������
            auditOneBill(false, sAudit);
          }
        }
      }
    }
    catch (nc.vo.pub.BusinessException ee) {
      showErrorMessage(ee.getMessage());
      showHintMessage(ee.getMessage());
      return false;
    }
    catch (Exception e) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000076")/* @res "�Զ��ɱ��������" */
          + e.getMessage());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000076")/* @res "�Զ��ɱ��������" */
          + e.getMessage());
      return false;
    }
    return true;
  }
  
  /**
   * �л���ʾ�ĵ���
   * @param nIndex
   */
  private void switchBill(int nIndex) {
    if (m_voBills != null) {
      m_voCurBill = m_voBills[nIndex];
      m_iCurBillPrt = nIndex;
      BillEditEvent bee = new BillEditEvent(this, 0, nIndex);
      headChanged(bee);
      getBillListPanel().getHeadTable().setRowSelectionInterval(nIndex, nIndex);

      m_iStatus = CARD_STATUS;
      if (m_voCurBill != null) {
        String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO()).getCbilltypecode();
		    if (!sBilltype.equals(m_sBillType)) {
		    	onButtonListClicked();
		      // ���ǵ�ǰ�������ͣ���ʾ
		      // showWarningMessage("��ǰ���ܵ㵥�����ͱ�����"+"'" + m_sBillType +
		      // "'"+","+"��ѡ��ĵ��ݵ������ͱ�����"+" '" + sBilltype + "'"+"�����Բ�����ʾ��������");
		      String[] value = new String[] {
		          m_sBillType, sBilltype
		      };
		      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
		          "20143010", "UPP20143010-000169", null, value));
		      return;
		    }
        String sOtherCorpID = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCothercorpid();
        String sOutCorpID = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCoutcorpid();

        BillItem bt = getBillCardPanel().getHeadItem("cothercalbodyid");
        if (bt != null) {
          UIRefPane uf = (UIRefPane) bt.getComponent();
          uf.setPk_corp(sOtherCorpID);
          uf.getRefModel().setPk_corp(sOtherCorpID);
        }

        bt = getBillCardPanel().getHeadItem("coutcalbodyid");
        if (bt != null) {
          UIRefPane uf = (UIRefPane) bt.getComponent();
          uf.setPk_corp(sOutCorpID);
          uf.getRefModel().setPk_corp(sOutCorpID);
        }

        getBillCardPanel().setBillValueVO(m_voCurBill);

        // ������Դ��������
        bt = getBillCardPanel().getHeadItem("cbillsource");
        if (bt != null
            && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))) {
          BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
          String sSourceBillType = btvo.getCsourcebilltypecode();
          UIComboBox uibox = (UIComboBox) bt.getComponent();
          if (sSourceBillType != null
              && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
            uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// ConstVO.m_sBillXSFPName);//���۷�Ʊ
          }
          else if (sSourceBillType != null
              && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
            uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//���۳��ⵥ
          }
          else {
            uibox.setSelectedIndex(-1);// tem("��");
          }
        }
        // �б��������Ĵ���
        dispListToCard();
      }
      else {
        getBillCardPanel().getBillData().clearViewData();
      }
      setBtnsForStatus(m_iStatus);
      // ���浱ǰ��������
      getBillCardPanel().updateValue();
    }

  }

  /**
   * �����Ű�ť,����ʾ�����е�һ�ŵ���
   */
  private void onButtonFirstClicked() {
    if (m_voBills != null) {
      switchBill(0);
    }
  }

  /**
   * �����Ű�ť,����ʾ���������ŵ���
   */
  private void onButtonPrevClicked() {
    if (m_voBills != null) {
      try {
        switchBill(m_iCurBillPrt - 1);
      }
      catch (ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * �����Ű�ť,����ʾ���������ŵ���
   */
  private void onButtonNextClicked() {
    if (m_voBills != null) {
      try {
        switchBill(m_iCurBillPrt + 1);
      }
      catch (ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * ��ĩ�Ű�ť,����ʾ���������һ�ŵ���
   */
  private void onButtonLastClicked() {
    if (m_voBills != null) {
      switchBill(m_voBills.length - 1);
    }
  }

  /**
   * ��������:��ճ���а�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonPasteLineClicked() {
    getBillCardPanel().pasteLine();
    setBtnsForStatus(UPDATE_STATUS);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000077")/* @res "ճ����" */);
  }

  /**
   * ��������:���ӡ��ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonPrintDirectClicked() {
    String strMsg = "";
    try {
      String sModuleCode = "20142010";
      if (getModuleCode() != null && getModuleCode().trim().length() != 0) {
        sModuleCode = getModuleCode();
      }

      if (m_iStatus == CARD_STATUS) {
        // ������ӡ
        showHintMessage(PrintLogClient.getBeforePrintMsg(false, false));
        strMsg = getBillCardPanel().printData(m_bd, getBillListPanel(),
            m_sBillType, m_voCurBill, sModuleCode);

      }
      else if (m_iStatus == LIST_STATUS) {
        // �б���������ӡ
        showHintMessage(PrintLogClient.getBeforePrintMsg(false, true));
        strMsg = getBillListPanel().printData(m_bd, m_sBillType,
            getListBills(), sModuleCode);

      }
      showHintMessage(strMsg);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * ��������:���ӡ��ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonPrintPreviewClicked() {
    String strMsg = "";
    try {
      String sModule = "20142010";
      if (getModuleCode() != null && getModuleCode().trim().length() != 0) {
        sModule = getModuleCode();
      }

      if (m_iStatus == CARD_STATUS) {
        // ������ӡ
        ArrayList alVO = new ArrayList();
        alVO.add(m_voCurBill);
        String sPKname = "cbillid";
        BillPrintTool bpt = new BillPrintTool(sModule, alVO, m_bd,
            IADataSource.class, null, null, null, sPKname);

        showHintMessage(PrintLogClient.getBeforePrintMsg(true, false));
        bpt.onCardPrintPreview(getBillCardPanel(), getBillListPanel(),
            m_sBillType);
        strMsg = bpt.getPrintMessage();
      }
      else if (m_iStatus == LIST_STATUS) {
        // �б���������ӡ
        String sPKname = "cbillid";
        BillPrintTool bpt = new BillPrintTool(sModule, getListSelectedBill(),
            m_bd, IADataSource.class, null, null, null, sPKname);

        showHintMessage(PrintLogClient.getBeforePrintMsg(true, true));
        bpt.onBatchPrintPreview(getBillListPanel(), m_sBillType);
        strMsg = bpt.getPrintMessage();
      }

      showHintMessage(strMsg);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * ��������:���ѯ��ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonQueryClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000082")/* @res "����׼����ѯ����������棬���Ժ�" */);
    getQueryClientDlg().showModal();
    if (getQueryClientDlg().isCloseOK()) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000052")/* @res "���ڲ�ѯ���ݣ����Ժ�" */);
      // ��ѯ����
      queryBills();
    }
    // ��ѯ����
    // getQueryConditionDlg().showModal();
  }

  /**
   * ��������:��ˢ�°�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void onButtonRefreshClicked() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000083")/* @res "����ˢ�µ��ݣ����Ժ�" */);
    // ���²�ѯ����
    queryBills();
  }

  /**
   * ��������:���ݲ�ѯ������ѯ���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void queryBills() {
    // ��ȷ����ť
    try {
      String[] sTable = getQueryClientDlg().getTables();
      String[] sConnection = getQueryClientDlg().getConnections();
      String[] sConditions = getQueryClientDlg().getConditions();
      /*
       * String[] sTable ----- Ҫ���ӵı� String[] sConnectParam ----- �������� String[]
       * sCondition ----- �������� BillVO condBillAllVO ------ ��������VO
       */
      // long t = System.currentTimeMillis();
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
          || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        BillVO condBillAllVO = new BillVO();
        BillHeaderVO bhvo = new BillHeaderVO();
        condBillAllVO.setParentVO(bhvo);
        bhvo.setCSQLClause(" cbilltypecode in ('" + ConstVO.m_sBillQCRKD
            + "','" + ConstVO.m_sBillQCXSCBJZD + "')");
        ClientLink cl = ce.getClientLink();
        m_voBills = BillBO_Client.querybillWithOtherTable(sTable, sConnection,
            sConditions, condBillAllVO, null, null, new Boolean(false), cl);
      }
      else {
        ClientLink cl = ce.getClientLink();
        m_voBills = BillBO_Client.querybillWithOtherTable(sTable, sConnection,
            sConditions, null, null, null, new Boolean(false), cl);
      }

      setBillsInList(m_voBills);
//      buttonTree.getButton(IAButtonConst.BTN_SWITCH).setName(NCLangRes.getInstance().getStrByID("common",
//          "UCH021"/* ��Ƭ��ʾ */));
      setButtons(m_aryButtonGroupList);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
      return;
    }
    if (m_voBills != null && m_voBills.length != 0) {
      // showHintMessage("��ѯ��" + m_voBills.length + "����¼�����ǵ�һ��");
      String[] value = new String[] {
        new Integer(m_voBills.length).toString()
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000139", null, value));
    }
    else {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000084")/* @res "δ��ѯ����¼" */);
    }
  }

  /**
   * ��������:���ð�ť״̬ <p/> ����: ButtonObject[] sButtons ----- ��ť <p/> ����ֵ: <p/> �쳣:
   */
  private void setBtnsForBilltypes(ButtonObject[] sButtons) {
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // ����������
      // �����۳ɱ���ת��
      // ���ӡ�ɾ�������ơ����С�ɾ�С�ճ���С������в�����
      btnCtrl.set(false,IABtnConst.BTN_ADD);
      btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      //edit by yhj 2014-03-27
//      btnCtrl.set(false, IABtnConst.BTN_AUTO_PRICE);
      //end
    }
    else if (m_bIsPOStart
        && (m_sBillType.equals(ConstVO.m_sBillCGRKD) || m_sBillType
            .equals(ConstVO.m_sBillSYTZD))) {
      // �ɹ�������
      // �ǲɹ���ⵥ�����������
      // ���ӡ�ɾ�������ơ����С�ɾ�С�ճ���С������в�����
      btnCtrl.set(false,IABtnConst.BTN_ADD);
      btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      //edit by yhj 2014-03-27
//      btnCtrl.set(false, IABtnConst.BTN_AUTO_PRICE);
      //end
    }
    else if (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
      // ��������
      UFBoolean bIsEditable = new UFBoolean(true);
      if (m_voCurBill != null) {
        String sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCsourcemodulename();
        if (sSource != null && sSource.equals(ConstVO.m_sModulePO)) {
          // �ǲɹ����˵�
//          bIsEditable = new UFBoolean(false);//edit by shikun ���㵥ɾ������������û��ɾ��
        }
      }
      if (bIsEditable.booleanValue() == false) {
        btnCtrl.set(true,IABtnConst.BTN_ADD);
        btnCtrl.set(true,IABtnConst.BTN_ADD_MANUAL);
        btnCtrl.set(true,IABtnConst.BTN_ADD_QUERY);
        btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
        
        btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
        btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
        btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
        btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      }
    }
    else if (m_bIsSCStart && m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
      // ί��ӹ�������
      // ��ί��ӹ��ջ���
      // ���ӡ�ɾ�������ơ����С�ɾ�С�ճ���С������в�����
      btnCtrl.set(false,IABtnConst.BTN_ADD);
      btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
    }
    else if (m_bIsICStart) {
      // ���������
      if (m_bIsAdjustBill == false
          && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCLCKD) == false) {
        // ���ǵ������������ڳ�����
        // ���ӡ�ɾ�������ơ����С�ɾ�С�ճ���С������С������в�����
        btnCtrl.set(false,IABtnConst.BTN_ADD);
        btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
        btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
        btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
        btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
        btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
        btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
        btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      }
      else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
        // �ǲ��ϳ��ⵥ
        UFBoolean bIsWithdraw = new UFBoolean(false);
        if (m_voCurBill != null) {
          bIsWithdraw = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getBwithdrawalflag();
        }
        // ɾ�������ơ����С�ɾ�С�ճ���С������в�����
        if (m_iStatus == INIT_STATUS) {
          // ��ʼ��
          btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        }
        else if (m_iStatus == CARD_STATUS) {
          // ���
          if (bIsWithdraw.booleanValue()) {
            // �Ǽ����ϣ�����ɾ��
            btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
          }
          else {
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          }
        }
        else if (m_iStatus == ADD_STATUS) {
          // ����
          btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
          if (getBillCardPanel().getBillModel().getRowCount() != 0)
            btnCtrl.set(true,IABtnConst.BTN_LINE_DELETE);
          if (this.m_bCanPasteLine) {
            btnCtrl.set(true,IABtnConst.BTN_LINE_COPY);
            btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
          }
        }
        else if (m_iStatus == UPDATE_STATUS) {
          // �޸�
          if (bIsWithdraw.booleanValue() == false) {
            btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
            btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
            btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
            btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
            btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
//            //edit by yhj 2014-03-27
//            btnCtrl.set(true, IABtnConst.BTN_AUTO_PRICE);
//            //end
          }
        }
        else if (m_iStatus == LIST_STATUS) {
          // �б�
          if (bIsWithdraw.booleanValue()) {
            UFBoolean bAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
                .getBauditedflag();
            if (bAudit.booleanValue() == false) {
              // �Ǽ����ϣ�δ�ɱ����㣬����ɾ��
              btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
            }
            else {
              // �Ǽ����ϣ��ѳɱ����㣬����ɾ��
              btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
            }
          }
          else {
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          }
        }
      }
    }
    if (isBeginAccount()
        && (m_sBillType.equals(ConstVO.m_sBillQCRKD) || m_sBillType
            .equals(ConstVO.m_sBillQCXSCBJZD))) {
      // ���ڳ����������ڳ�����
      btnCtrl.set(false,IABtnConst.BTN_ADD);
      btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_IMPORT_BILL);
    }
    else if (m_bIsICStart == false) {
      // ���û�����ã����ܵ���
      btnCtrl.set(false,IABtnConst.BTN_IMPORT_BILL);
    }
    // �����Ҽ��˵�
    getBillCardPanel().getAddLineMenuItem().setEnabled(
        buttonTree.getButton(IABtnConst.BTN_LINE_ADD).isEnabled());
    getBillCardPanel().getDelLineMenuItem().setEnabled(
        buttonTree.getButton(IABtnConst.BTN_LINE_DELETE).isEnabled());
    getBillCardPanel().getInsertLineMenuItem().setEnabled(
        buttonTree.getButton(IABtnConst.BTN_LINE_INSERT).isEnabled());
    getBillCardPanel().getCopyLineMenuItem().setEnabled(
        buttonTree.getButton(IABtnConst.BTN_LINE_COPY).isEnabled());
    getBillCardPanel().getPasteLineMenuItem().setEnabled(
        buttonTree.getButton(IABtnConst.BTN_LINE_PASTE).isEnabled());
    if (m_voCurBill != null) {
      String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCbilltypecode();
      if (sBilltype.equals(m_sBillType)    
          || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType .equals(ConstVO.m_sBillQCXSCBJZD))
          || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType.equals(ConstVO.m_sBillQCRKD))) {
        // �ǵ�ǰ��������
      }
      else {
        // ���ǵ�ǰ�������ͣ����ɸ���ָ��
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
    }
    setButtons(sButtons);
  }

  /**
   * ��vo�е�������ѡ���ѡ��ֵת��Ϊ��ʾ���û����ַ���
   * 
   * @param vo
   *          BillHeaderVO ��ͷvo
   * @param isCardPanel
   *          boolean true������������ false���б�������
   * @param iRownum
   *          int ���б������¼�¼�������ڱ����������ʱ��0����
   */
  private void setComboBoxInHeadFromVO(BillVO vo, boolean isCardPanel,
      int iRownum) {
    if (vo == null || vo.getParentVO() == null) {
      Log.info("������󣺲�������ȷ(BillClientUI.setComboBoxInHeadFromVO)");
      return;
    }
    BillHeaderVO hvo = (BillHeaderVO) vo.getParentVO();
    // �����ݹ�ѡ��
    if (hvo.getBestimateflag() != null) {
      boolean bEst = hvo.getBestimateflag().booleanValue();
      String sEst = m_ComboItemsVO.name_estimated_no;
      if (bEst) {
        sEst = m_ComboItemsVO.name_estimated_yes;
      }
      if (isCardPanel) {
        getBillCardPanel().getHeadItem("bestimateflag").setValue(sEst);
      }
      else {
        getBillListPanel().getHeadBillModel().setValueAt(sEst, iRownum,
            "bestimateflag");
      }
    }
    // ����������ѡ��
    if (hvo.getBwithdrawalflag() != null) {
      boolean bIsWith = hvo.getBwithdrawalflag().booleanValue();
      String sIsWith = m_ComboItemsVO.name_no;
      if (bIsWith) {
        sIsWith = m_ComboItemsVO.name_yes;
      }
      if (isCardPanel) {
        getBillCardPanel().getHeadItem("bwithdrawalflag").setValue(sIsWith);
      }
      else {
        getBillListPanel().getHeadBillModel().setValueAt(sIsWith, iRownum,
            "bwithdrawalflag");
      }
    }
    // ������������
    if (hvo.getFallocflag() != null) {
      Integer iFlag = hvo.getFallocflag();
      // ֱ�˵���
      String sAllocFlag = m_ComboItemsVO.name_transfer_direct;
      if (iFlag.intValue() == m_ComboItemsVO.type_transfer_instore) {
        // ������
        sAllocFlag = m_ComboItemsVO.name_transfer_instore;
      }
      else if (iFlag.intValue() == m_ComboItemsVO.type_transfer_stock) {
        // ���ɵ���
        sAllocFlag = m_ComboItemsVO.name_transfer_stock;
      }
      if (isCardPanel) {
        getBillCardPanel().getHeadItem("fallocflag").setValue(sAllocFlag);
      }
      else {
        getBillListPanel().getHeadBillModel().setValueAt(sAllocFlag, iRownum,
            "fallocflag");
      }

    }
    // ��������۳ɱ���ת����������Դ��������
    if (ConstVO.m_sBillXSCBJZD.equals(m_sBillType)) {
      // ���۳ɱ���ת��
      String sSource = null;
      if (vo.getChildrenVO() != null && vo.getChildrenVO().length != 0) {
        sSource = ((BillItemVO) vo.getChildrenVO()[0]).getCsourcebilltypecode();
      }
      else {
        Object oSource = m_hmBillId2Sourcebilltypecode.get(((BillHeaderVO) vo
            .getParentVO()).getCbillid());
        if (oSource != null) {
          sSource = oSource.toString();
        }
      }
      String sSourceName = "";
      if (sSource != null && sSource.trim().length() != 0) {

        if (sSource.equals(ConstVO.m_sBillXSFP)) {
          //���۷�Ʊ
          sSourceName = m_ComboItemsVO.name_salebill;
        }
        else if (sSource.equals(ConstVO.m_sBillXSCKD)) {
          //���۳��ⵥ          
          sSourceName = m_ComboItemsVO.name_saleoutlist;
        }
        else if (sSource.equals(ConstVO.m_sBillKCTSD)) {
          //���;��
          sSourceName = m_ComboItemsVO.name_waylossbill; 
        }
      }
      if (isCardPanel) {
        getBillCardPanel().getHeadItem("cbillsource").setValue(sSourceName);
      }
      else {
        getBillListPanel().getHeadBillModel().setValueAt(sSourceName, iRownum,
            "cbillsource");
      }

    }
  }

  /**
   * ��vo�е�������ѡ���ѡ��ֵת��Ϊ��ʾ���û����ַ���
   * 
   * @param bvo
   *          BillItemVO ����vo
   * @param isCardPanel
   *          boolean true������������ false���б�������
   */
  private void setComboBoxInBodyFromVO(BillItemVO bvo, boolean isCardPanel,
      int iRownum) {
    if (bvo == null) {
      Log.info("������󣺲�������ȷ(BillClientUI.setComboBoxInBody)");
      return;
    }
    if (bvo.getBlargessflag() != null) {
      if (getBillCardPanel().getBodyItem("blargessflag") != null) {
        //��Ʒ
        boolean bLas = bvo.getBlargessflag().booleanValue();
        String sLas = m_ComboItemsVO.name_no;
        if (bLas) {
          sLas = m_ComboItemsVO.name_yes;
        }
        if (isCardPanel) {
          getBillCardPanel().getBillModel().setValueAt(sLas, iRownum,
          "blargessflag");
        }
        else {
          getBillListPanel().getBodyBillModel().setValueAt(sLas, iRownum,
          "blargessflag");
        }
      }
    }
    //���ݻ�ȡ��ʽ����
    if (bvo.getFdatagetmodelflag() != null){
      Integer flag = bvo.getFdatagetmodelflag();
      String name = FlagTranslator.dataGetModelName(flag);
      if (isCardPanel) {
        getBillCardPanel().getBillModel().setValueAt(name, iRownum,
            "cdatagetmodelname");
      }
      else {
        getBillListPanel().getBodyBillModel().setValueAt(name, iRownum,
            "cdatagetmodelname");
      }
    }
    //�Ƽ۷�ʽ����
    if (bvo.getFpricemodeflag() != null) {
      Integer flag = bvo.getFpricemodeflag();
      String name = FlagTranslator.priceModeName(flag);
      if (isCardPanel) {
        getBillCardPanel().getBillModel().setValueAt(name, iRownum,
            "cpricemodename");
      }
      else {
        getBillListPanel().getBodyBillModel().setValueAt(name, iRownum,
            "cpricemodename");
      }
    }
  }

  /**
   * ��������:�����ӻ��޸ĺ�����ݸ�ֵ�������� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void setVODataToInterface(BillVO bvo) {
    // ���ý�������
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    getBillCardPanel().getHeadItem("pk_corp").setValue(m_sCorpID);
    getBillCardPanel().getHeadItem("cbilltypecode").setValue(m_sBillType);
    getBillCardPanel().getHeadItem("cbillid").setValue(bhvo.getCbillid());
    // �������ȡ�����·�
    getBillCardPanel().getHeadItem("caccountyear").setValue(
        bhvo.getCaccountyear());
    getBillCardPanel().getHeadItem("caccountmonth").setValue(
        bhvo.getCaccountmonth());
    // �����ݹ��������ϣ���������
    setComboBoxInHeadFromVO(bvo, true, 0);

    boolean bEst = bhvo.getBestimateflag().booleanValue();
    String sEst = m_ComboItemsVO.name_estimated_no;// "���ݹ�";
    if (bEst) {
      sEst = m_ComboItemsVO.name_estimated_yes;// "�ݹ�";
    }
    getBillCardPanel().getHeadItem("bestimateflag").setValue(sEst);
    boolean bIsWith = bhvo.getBwithdrawalflag().booleanValue();
    String sIsWith = m_ComboItemsVO.name_no;// "��";
    if (bIsWith) {
      sIsWith = m_ComboItemsVO.name_yes;// "��";
    }
    getBillCardPanel().getHeadItem("bwithdrawalflag").setValue(sIsWith);

    getBillCardPanel().getHeadItem("bauditedflag").setValue(
        bhvo.getBauditedflag());
    getBillCardPanel().getTailItem("coperatorid").setValue(
        bhvo.getCoperatorid());
    for (int i = 0; i < bvo.getChildrenVO().length; i++) {
      BillItemVO btvo = (BillItemVO) bvo.getChildrenVO()[i];
      btvo.setStatus(BillModel.NORMAL);
      // ��˾���������͡����ݺ�
      getBillCardPanel().getBillModel().setValueAt(m_sCorpID, i, "pk_corp");
      getBillCardPanel().getBillModel().setValueAt(m_sBillType, i,
          "cbilltypecode");
      getBillCardPanel().getBillModel().setValueAt(bhvo.getVbillcode(), i,
          "vbillcode");
      // ����ID
      getBillCardPanel().getBillModel().setValueAt(btvo.getCbill_bid(), i,
          "cbill_bid");
      getBillCardPanel().getBillModel().setValueAt(btvo.getCbillid(), i,
          "cbillid");
      // �Ƿ�����˷�¼
      getBillCardPanel().getBillModel().setValueAt(btvo.getBadjustedItemflag(),
          i, "badjustedItemflag");
      getBillCardPanel().getBillModel().setValueAt(btvo.getFdatagetmodelflag(),
          i, "fdatagetmodelflag");
      getBillCardPanel().getBillModel().setValueAt(
          btvo.getFolddatagetmodelflag(), i, "folddatagetmodelflag");
      getBillCardPanel().getBillModel().setValueAt(
          btvo.getFoutadjustableflag(), i, "foutadjustableflag");
      // �����к�
      getBillCardPanel().getBillModel().setValueAt(new Integer(i + 1), i,
          "irownumber");
      // ���ñ���
      setComboBoxInBodyFromVO(btvo, true, i);

      // ����ҵ������
      getBillCardPanel().getBillModel().setValueAt(btvo.getDbizdate(), i,
          "dbizdate");
      if (m_iStatus == ADD_STATUS) {
        // ���������Ϣ
        getBillCardPanel().getBillModel().setValueAt(btvo.getCauditorid(), i,
            "cauditorid");
        getBillCardPanel().getBillModel().setValueAt(btvo.getDauditdate(), i,
            "dauditdate");
        getBillCardPanel().getBillModel().setValueAt(btvo.getIauditsequence(),
            i, "iauditsequence");
        // �Ƽ۷�ʽ
        getBillCardPanel().getBillModel().setValueAt(btvo.getFpricemodeflag(),
            i, "fpricemodeflag");
        // ���ۡ������������
        getBillCardPanel().getBillModel().setValueAt(btvo.getNprice(), i,
            "nprice");
        getBillCardPanel().getBillModel().setValueAt(btvo.getNmoney(), i,
            "nmoney");
        // �������˰ת�����
        calcTransIncomeTaxMny(i);
      }
      // ���ù̶��ʲ�
      getBillCardPanel().getBillModel().setValueAt(btvo.getCfadeviceid(), i,
          "cfadeviceid");
      getBillCardPanel().getBillModel().setValueAt(btvo.getCfacardid(), i,
          "cfacardid");
      getBillCardPanel().getBillModel().setValueAt(btvo.getCfadevicecode(), i,
          "cfadevicecode");
      getBillCardPanel().getBillModel().setValueAt(btvo.getCfadevicename(), i,
          "cfadevicename");
    }
  }

  /**
   * ��������:�����ճ��ĵ��ݸ�ֵ������������ <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  private void showBillInForm() {
    try {
      getBillCardPanel().setBillValueVO(
          getSaleBillsChooseDlg().getChooseBillVO());
      // ������Դ��������
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        m_bIsChangeEvent = false;
        getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_salebill);
        m_bIsChangeEvent = true;
      }
      // ������ID���
      getBillCardPanel().setHeadItem("cbillid", null);
      // �����ݺ����
      getBillCardPanel().setHeadItem("vbillcode", null);
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        // �����ݷ�¼ID���
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // ������ID���
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbillid");
        // ���ݺ�Ϊ��
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbillcode");
        // �ۼƷ�������Ϊ��
        getBillCardPanel().getBillModel()
            .setValueAt(null, i, "nsettledsendnum");
        // �ۼƻس�����Ϊ��
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "nsettledretractnum");
        // �ۼƻس�����Ϊ��
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // ����csaleadviceid
        getBillCardPanel().getBillModel().setValueAt(
            getSaleBillsChooseDlg().getChooseBillVO().getParentVO()
                .getPrimaryKey(), i, "csaleadviceid");
        // ����ccsaleadviceitemid
        getBillCardPanel().getBillModel().setValueAt(
            getSaleBillsChooseDlg().getChooseBillVO().getChildrenVO()[i]
                .getPrimaryKey(), i, "ccsaleadviceitemid");
        // �����Ϊ��
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorid");
      }
      // ��õ�ǰ����
      String d = ce.getBusinessDate().toString();
      getBillCardPanel().setHeadItem("dbilldate", d);
      // �����֯���ֿ⡢ҵ�����͡��շ���𡢲��š�ҵ��Ա�������ˡ��ͻ������޸�
      getBillCardPanel().getHeadItem("crdcenterid").setEdit(false);
      getBillCardPanel().getHeadItem("crdcenterid").setEnabled(false);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEdit(false);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEnabled(false);
      getBillCardPanel().getHeadItem("cwarehouseid").setEdit(false);
      getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(false);
      getBillCardPanel().getHeadItem("cbiztypeid").setEdit(false);
      getBillCardPanel().getHeadItem("cbiztypeid").setEnabled(false);
      getBillCardPanel().getHeadItem("cdispatchid").setEdit(false);
      getBillCardPanel().getHeadItem("cdispatchid").setEnabled(false);
      getBillCardPanel().getHeadItem("cdeptid").setEdit(false);
      getBillCardPanel().getHeadItem("cdeptid").setEnabled(false);
      getBillCardPanel().getHeadItem("cemployeeid").setEdit(false);
      getBillCardPanel().getHeadItem("cemployeeid").setEnabled(false);
      getBillCardPanel().getHeadItem("cagentid").setEdit(false);
      getBillCardPanel().getHeadItem("cagentid").setEnabled(false);
      getBillCardPanel().getHeadItem("ccustomvendorid").setEdit(false);
      getBillCardPanel().getHeadItem("ccustomvendorid").setEnabled(false);
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("nnumber") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000085")/* @res "ѡ����ɣ������޸�����" */);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * ��������:�����Ӱ�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void addBill() throws Exception {
    m_iStatus = ADD_STATUS;
    // ��õ�ǰ����
    String d = ce.getBusinessDate().toString();
    if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
        || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
      // ���ڳ����ݣ������������ڵ�ǰһ��
      // �ڳ�����
      UFDate dBeginDate = CommonDataBO_Client.getMonthBeginDate(m_sCorpID,
          m_sStartPeriod);
      if (dBeginDate == null) {
        // ����ڼ�û�ж���
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "��ǰ����ڼ�û�ж���" */);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000086")/* @res "��ǰ����ڼ�û�ж���" */);
        return;
      }
      UFDate dPerDate = dBeginDate.getDateBefore(1);
      d = dPerDate.toString();
    }
    getBillCardPanel().addBill(d, m_vIsEnable);
    if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // ����Ĭ�ϵ�����Դ
      //getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ���۳��ⵥ
      if (m_bIsICStart) {
        // ���������ã�Ĭ�������۳��ⵥ
      	getUIComboBoxSource().setSelectedIndex(m_ComboItemsVO.type_saleoutlist);
      }
      else {
        // ������δ���ã�Ĭ�ϲ�ѡ��
      	getUIComboBoxSource().setSelectedIndex(-1);
      }
    }
    BillItem bt = m_bd.getHeadItem("bwithdrawalflag");
    if (bt != null && bt.getComponent() instanceof UIComboBox) {
      UIComboBox box = (UIComboBox) bt.getComponent();
      if (m_bIsICStart) {
        // ���������ã�Ĭ���Ǽ����ϵ�
        box.setSelectedIndex(m_ComboItemsVO.type_yes);
      }
      else {
        // ������δ���ã�Ĭ�ϲ��Ǽ����ϵ�
        box.setSelectedIndex(m_ComboItemsVO.type_no);
      }
    }
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000087")/* @res "�ֹ����ӵ���" */);
  }

  /**
   *  �õ�һ�鲻�ظ��Ĵ������ֵ
   */
  private String[] getUniInvIds(BillItemVO[] btvos) {
    HashMap hmInvIds = new HashMap();
    for (int i = 0; i < btvos.length; i++) {
      hmInvIds.put(btvos[i].getCinventoryid(), null);
    }
    Set keys = hmInvIds.keySet();
    String[] sInvIds = new String[keys.size()];
    sInvIds = (String[]) keys.toArray(sInvIds);
    return sInvIds;
  }

  /**
   * ��������:���ݺϷ��Լ�� <p/> ����: <p/> ����ֵ:�Ƿ�Ϸ� <p/> �쳣:
   */
  protected boolean checkData(BillVO bvo) throws Exception {

    // ������ͷ����
    BillHeaderVO bhvo = (BillHeaderVO) bvo.getParentVO();
    if (bhvo == null) {
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000091")/* @res "�������ͷ����" */;
      return false;
    }
    // ���õ�λID����������
    bhvo.setPk_corp(m_sCorpID);
    bhvo.setCbilltypecode(m_sBillType);

    String sSourceModule = "";
    if (m_iStatus == ADD_STATUS) {
      // �������ȡ�����·���Ϊ��
      bhvo.setCaccountyear(null);
      bhvo.setCaccountmonth(null);
      if (bhvo.getBestimateflag() == null) {
        bhvo.setBestimateflag(new UFBoolean("N"));
      }
      if (bhvo.getBwithdrawalflag() == null) {
        bhvo.setBwithdrawalflag(new UFBoolean("N"));
      }
      else if (!m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
        bhvo.setBwithdrawalflag(new UFBoolean("N"));
      }
      bhvo.setBauditedflag(new UFBoolean("N"));
      bhvo.setCoperatorid(ce.getUser().getPrimaryKey());
    }
    else if (m_iStatus == UPDATE_STATUS) {
      sSourceModule = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCsourcemodulename();
      // ������Դ��Ϣ
      bhvo.setCsourcemodulename(sSourceModule);
      // ��������趨Ϊ������ԭʼ�Ƶ��ˣ���ѵ�ǰ����Ա��Ϊ�Ƶ���
      if (!m_bKeepOriginalOperator) {
        bhvo.setCoperatorid(ce.getUser().getPrimaryKey());
      }
    }
    // ��ñ�ͷ���Ƶ�����
    UFDate dBillDate = bhvo.getDbilldate();
    UFDate dBeginDate = null;
    if (dBillDate == null) {
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000088")/* @res "û�������Ƶ����ڣ������" */;
      return false;
    }
    else if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
        || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
      // �ڳ�����
      UFDate[] sDates = CommonDataBO_Client.getMonthDates(m_sCorpID,
          m_sStartPeriod);
      dBeginDate = sDates[0];
      UFDate dEndDate = sDates[1];
      if (dBeginDate == null || dEndDate == null) {
        // ����ڼ�û�ж���
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "��ǰ����ڼ�û�ж���" */;
        return false;
      }
      if (dBillDate.toString().compareTo(dBeginDate.toString()) >= 0) {
        // �Ƶ�����������������
        // m_sErrorMessage = "�ڳ������Ƶ�����" + dBillDate + "Ӧ����ϵͳ��������" + dBeginDate +
        // "�������";
        String[] value = new String[] {
            dBillDate.toString(), dBeginDate.toString()
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000140", null, value);
        return false;
      }
    }
    else if (m_iStatus == ADD_STATUS) {
      // �ճ�����
      // UFDate[] sDates = CommonDataBO_Client.getMonthDates(m_sCorpID,
      // ce.getAccountPeriod());
      dBeginDate = m_aBeginEndDates[0];// sDates[0];
      UFDate dEndDate = m_aBeginEndDates[1];// sDates[1];
      if (dBeginDate == null || dEndDate == null) {
        // ��ǰ����ڼ�û�ж���
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000086")/* @res "��ǰ����ڼ�û�ж���" */;
        return false;
      }
      if (dBillDate.compareTo(dBeginDate) < 0) {
        // С�ڿ�ʼ����
        // m_sErrorMessage = "�Ƶ��������ڱ��¿�ʼ����" + dBeginDate + "�������";
        String[] value = new String[] {
          dBeginDate.toString()
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000141", null, value);
        return false;
      }
      else if (dBillDate.compareTo(dEndDate) > 0) {
        // ���ڽ�ֹ����
        // m_sErrorMessage = "�Ƶ��������ڱ��½�ֹ����" + dEndDate + "�������";
        String[] value = new String[] {
          dEndDate.toString()
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000142", null, value);
        return false;
      }
    }
    bhvo.setBdisableflag(new UFBoolean("N"));
    boolean bIsWithdrawalflag = bhvo.getBwithdrawalflag().booleanValue();
    if (m_bIsICStart && bIsWithdrawalflag == false
        && m_sBillType.equals(ConstVO.m_sBillCLCKD) && m_iStatus == ADD_STATUS) {
      // ����Ѿ����ã����Ӳ��ϳ��ⵥ�����Ǽ����ϣ�����
      m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000092")/* @res "�������Ѿ����ã��������ֻ�����Ӽ��������͵Ĳ��ϳ��ⵥ�������" */;
      return false;
    }
    // �Ƿ������˱�������������
    boolean bIsInputBillType = false;
    if (m_bIsInAdjustBill
        && getBillCardPanel().getBodyItem("cadjustbilltype") != null) {
      // �����������뱻�����������ͣ������б��������ݺź͵��ݷ�¼
      String sBillType = getBillCardPanel().getBodyItem("cadjustbilltype")
          .getValue();
      if (sBillType != null && sBillType.trim().length() != 0) {
        String sBillID = getBillCardPanel().getBodyItem("cadjustbill")
            .getValue();
        if (sBillID == null || sBillID.trim().length() == 0) {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000093")/*
                                                 * @res
                                                 * "���뱻�����������ͺ�������뱻�������ݺţ������"
                                                 */;
          return false;
        }
        bIsInputBillType = true;
      }
    }

    // ���ݺϷ��Լ�飨��Ҫ��������ķǿ��ԣ�
    // bhvo.validate();
    // ����validate�������븸��smartVO��throws�Ӿ��ͻ�����Ը���verify
    bhvo.verify();

    // �ڳ����ݼ����������ݣ������ǵ�������ݣ�û�н��޸�ʱҲҪ�ж�
    if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) || m_sBillType
        .equals(ConstVO.m_sBillQCXSCBJZD))
        && m_iStatus == UPDATE_STATUS) {
      ArrayList errFields = new ArrayList(); // errFields record those
      // null
      // fields that cannot be null.
      String sErrorString = nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "20143010", "UPP20143010-000094")/* @res "�����ֶβ���Ϊ��:" */;
      Vector vNumString = new Vector(1, 1);
      for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
        Object m_nnumber = getBillCardPanel().getBillModel().getValueAt(i,
            "nnumber");
        Object m_nmoney = getBillCardPanel().getBillModel().getValueAt(i,
            "nmoney");
        Object m_nprice = getBillCardPanel().getBillModel().getValueAt(i,
            "nprice");
        Object iPriceFlag = getBillCardPanel().getBillModel().getValueAt(i,
            "fpricemodeflag");
        if (m_nnumber == null
            && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false
            && m_sBillType.equals(ConstVO.m_sBillSYTZD) == false) {
          // errFields.add("��" + (i + 1) + "�е�����");
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000143", null, value));
        }
        else if (m_nnumber != null) {
          double dNumber = new UFDouble(m_nnumber.toString()).doubleValue();
          if (dNumber < 0 && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // �ڳ����۳ɱ���ת�������������0
            vNumString.addElement(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000095")/* @res "�ڳ����۳ɱ���ת����������Ӧ����0" */);
          }
        }
        // �жϽ��
        // ����ǿ����ǣ��ڳ���ⵥ���Ǽƻ��ۣ����ڳ����ⵥ��
        if (m_nmoney == null
            && ((iPriceFlag != null
                && new Integer(iPriceFlag.toString()).intValue() != nc.vo.ia.pub.ConstVO.JHJ && m_sBillType
                .equals(ConstVO.m_sBillQCRKD)) || m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))) {

          // errFields.add(new String("��" + (i + 1) + "�еĽ��"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000144", null, value));
        }
        // ����ǿ�����������������������
        else if (m_nmoney == null
            && (m_sBillType.equals(ConstVO.m_sBillRKTZD) || m_sBillType
                .equals(ConstVO.m_sBillCKTZD))) {
          // errFields.add(new String("��" + (i + 1) + "�еĽ��"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000144", null, value));
        }
        else if (m_nmoney != null) {
          double dMoney = new UFDouble(m_nmoney.toString()).doubleValue();
          if (dMoney < 0 && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // �ڳ����۳ɱ���ת������������0
            vNumString
                .addElement(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000096")/*
                                                       * @res
                                                       * "�ڳ����۳ɱ���ת�����ݽ��Ӧ���ڵ���0"
                                                       */);
          }
        }
        // �жϵ���
        if (m_nprice == null && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // errFields.add(new String("��" + (i + 1) + "�еĵ���"));
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          errFields.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
              "UPP20143010-000145", null, value));
        }
        else if (m_nprice != null) {
          double dPrice = new UFDouble(m_nprice.toString()).doubleValue();
          if (dPrice < 0 && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false) {
            // vNumString.addElement("��" + (i + 1) + "�еĵ��۲���С��0");
            String[] value = new String[] {
              String.valueOf(i + 1)
            };
            vNumString.addElement(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000146", null, value));
          }
        }
        StringBuffer message = new StringBuffer();
        int iLength = vNumString.size();
        for (int hhh = 0; hhh < iLength; hhh++) {
          if (hhh != 0) {
            message.append(", ");
          }
          message.append(vNumString.elementAt(hhh));
        }
        if (errFields.size() > 0) {
          if (vNumString.size() != 0) {
            message.append(", ");
          }
          message.append(sErrorString);
          String[] temp = (String[]) errFields.toArray(new String[0]);
          message.append(temp[0]);
          for (int hhh = 1; hhh < temp.length; hhh++) {
            message.append(", ");
            message.append(temp[i]);
          }
          // throw the exception:
          throw new nc.vo.pub.NullFieldException(message.toString());
        }
        else if (message.length() != 0) {
          throw new ValidationException(message.toString());
        }
      }
    }

    // ������������
    BillItemVO[] btvos = (BillItemVO[]) bvo.getChildrenVO();
    if (m_iStatus == ADD_STATUS) {
      if (btvos == null || btvos.length == 0) {
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000097")/* @res "�������������" */;
        return false;
      }
    }
    if (m_iStatus == UPDATE_STATUS
        && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
            .equals(ConstVO.m_sBillQCXSCBJZD))) {
      // ������Դ��������
      BillItem bt2 = getBillCardPanel().getHeadItem("cbillsource");
      if (bt2 != null) {
        int iIndex = ((UIComboBox) bt2.getComponent()).getSelectedIndex();
        String sSourceBillType = ConstVO.m_sBillXSCKD;
        if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// ���۷�Ʊ
          sSourceBillType = ConstVO.m_sBillXSFP;
        }
        // �ж�ҵ������
        boolean bIsFQ = false; // �Ƿ��Ƿ����տ�
        boolean bIsWT = false; // �Ƿ���ί�д���
        BillItem bt3 = getBillCardPanel().getHeadItem("cbiztypeid");
        if (bt3 != null && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          String sBizTypeID = bt3.getValue();
          if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
            UIRefPane pane = (UIRefPane) bt3.getComponent();
            Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
            if (oRule != null) {
              if (oRule.toString().equals("W")) {
                // ��ί�д���
                bIsWT = true;
              }
              else if (oRule.toString().equals("F")) {
                // �Ƿ����տ�
                bIsFQ = true;
              }
            }
            for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
              // ��Դ�����۷�Ʊ
              String sSaleAdviceID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .getCsaleadviceid();
              if (bIsFQ) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // û��¼���Ӧ���۳��ⵥ
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000089")/*
                                                                     * @res
                                                                     * "��ǰ������Դ�����۷�Ʊ��ҵ�������Ƿ����տ�,��û��ѡ���Ӧ�����۳��ⵥ�������"
                                                                     */;
                  return false;
                }
              }
              else if (bIsWT) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // û��¼���Ӧ���۳��ⵥ
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000090")/*
                                                                     * @res
                                                                     * "��ǰ������Դ�����۷�Ʊ��ҵ��������ί�д���,��û��ѡ���Ӧ�����۳��ⵥ�������"
                                                                     */;
                  return false;
                }
              }
            }
          }
          else {
            // m_sErrorMessage = "��ǰ������Դ�����۷�Ʊ����û��ҵ�����ͣ������";
            // return false;
          }
        }
      }
    }

    String[] sInvIds = getUniInvIds(btvos);

    // �������������
    String sPK = bhvo.getCbillid();
    // ��õ��ݺ�
    String sBillCode = bhvo.getVbillcode();
    // ��ñ�ͷ�ɱ������֯
    String sRdcenterid = bhvo.getCrdcenterid();

    Vector vInvID = new Vector(1, 1); // �����жϼƻ��۵������Ĵ�������ظ�
    Hashtable htPrice = ce.getPricingMode(m_sCorpID, sRdcenterid, sInvIds);// ��¼�Ƽ۷�ʽ
    Hashtable htAss = ce.isManageForAssi(m_sCorpID, sInvIds);// ��¼�Ƿ��Ǹ���������
    Hashtable htFree = ce.isManageForFree(m_sCorpID, sInvIds);// ��¼�Ƿ������������
    sInvIds = null;

    for (int i = 0; i < btvos.length; i++) {
      // ��ɾ���в��ü��Ϸ���
      if (m_iStatus == UPDATE_STATUS
          && btvos[i].getStatus() == BillVOStatus.DELETED) {
        continue;
      }

      // �������ݾ���
      UFDouble dNumber2 = btvos[i].getNnumber();
      if (dNumber2 != null) {
        dNumber2 = dNumber2.setScale(m_iPeci[0], UFDouble.ROUND_HALF_UP);
        btvos[i].setNnumber(dNumber2);
      }
      UFDouble dMny = btvos[i].getNmoney();
      if (dMny != null) {
        dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
        btvos[i].setNmoney(dMny);
      }
      UFDouble dPrice = btvos[i].getNprice();
      if (dPrice != null) {
        dPrice = dPrice.setScale(m_iPeci[1], UFDouble.ROUND_HALF_UP);
        btvos[i].setNprice(dPrice);
      }

      if (m_iStatus == ADD_STATUS) {
        // ���÷�¼ID
        btvos[i].setCbill_bid(null);
        // �����к�
        btvos[i].setIrownumber(new Integer(i + 1));
        // ���������Ϊ��
        btvos[i].setCauditorid(null);
        btvos[i].setDauditdate(null);
        btvos[i].setIauditsequence(null);
        // �Ƿ�����˷�¼
        btvos[i].setBadjustedItemflag(new UFBoolean("N"));
        // �Ƿ���Ʒ
        if (btvos[i].getBlargessflag() == null)
          btvos[i].setBlargessflag(new UFBoolean("N"));
        // �˻���־
        if (btvos[i].getBretractflag() == null)
          btvos[i].setBretractflag(new UFBoolean("N"));
        // �Ƿ�������ʵʱƾ֤
        btvos[i].setBrtvouchflag(new UFBoolean("N"));
        // ����ҵ������
        if (btvos[i].getDbizdate() == null)
          btvos[i].setDbizdate(dBillDate);
      }
      else if (m_iStatus == UPDATE_STATUS
          && btvos[i].getStatus() == BillVOStatus.UPDATED) {
        // ���޸ĵ�
        String sID = btvos[i].getCbill_bid();
        // ��������
        bhvo.setFallocflag(((BillHeaderVO) m_voCurBill.getParentVO())
            .getFallocflag());
        // ts
        bhvo.setTs(((BillHeaderVO) m_voCurBill.getParentVO()).getTs());
        for (int j = 0; j < m_voCurBill.getChildrenVO().length; j++) {
          if (m_voCurBill.getChildrenVO()[j].getPrimaryKey().equals(sID)) {
            // ������Դ������Ϣ
            btvos[i].setCsourcebilltypecode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCsourcebilltypecode());
            btvos[i].setVsourcebillcode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getVsourcebillcode());
            btvos[i]
                .setCsaleadviceid(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                    .getCsaleadviceid());
            btvos[i].setCcsaleadviceitemid(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCcsaleadviceitemid());
            // ����Դͷ������Ϣ
            btvos[i]
                .setCfirstbillid(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                    .getCfirstbillid());
            btvos[i].setCfirstbillitemid(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCfirstbillitemid());
            btvos[i].setCfirstbilltypecode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getCfirstbilltypecode());
            btvos[i].setVfirstbillcode(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getVfirstbillcode());
            btvos[i]
                .setVfirstrowno(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                    .getVfirstrowno());
            // �����Ƿ������¼���Ƿ���Ʒ
            // �Ƿ�����˷�¼
            btvos[i].setBadjustedItemflag(((BillItemVO) m_voCurBill
                .getChildrenVO()[j]).getBadjustedItemflag());
            // �Ƿ���Ʒ
            if (btvos[i].getBlargessflag() == null)
              btvos[i].setBlargessflag(((BillItemVO) m_voCurBill
                  .getChildrenVO()[j]).getBlargessflag());
            // �˻���־
            if (btvos[i].getBretractflag() == null)
              btvos[i].setBretractflag(((BillItemVO) m_voCurBill
                  .getChildrenVO()[j]).getBretractflag());
            // �Ƿ�������ʵʱƾ֤
            btvos[i]
                .setBrtvouchflag(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                    .getBrtvouchflag());
            // ts
            btvos[i].setTs(((BillItemVO) m_voCurBill.getChildrenVO()[j])
                .getTs());
            break;
          }
        }
      }
      else if (m_iStatus == UPDATE_STATUS
          && btvos[i].getStatus() == BillVOStatus.NEW) {
        // �����ӵ�
        // ���÷�¼ID
        btvos[i].setCbill_bid(null);
        // ���������Ϊ��
        btvos[i].setCauditorid(null);
        btvos[i].setDauditdate(null);
        btvos[i].setIauditsequence(null);
        // �Ƿ�����˷�¼
        btvos[i].setBadjustedItemflag(new UFBoolean("N"));
        // �Ƿ���Ʒ
        if (btvos[i].getBlargessflag() == null)
          btvos[i].setBlargessflag(new UFBoolean("N"));
        // �˻���־
        if (btvos[i].getBretractflag() == null)
          btvos[i].setBretractflag(new UFBoolean("N"));
        // �Ƿ�������ʵʱƾ֤
        btvos[i].setBrtvouchflag(new UFBoolean("N"));
        // ����ҵ������
        if (btvos[i].getDbizdate() == null)
          btvos[i].setDbizdate(dBillDate);
      }

      UFDate dBizdate = btvos[i].getDbizdate();
      if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) || m_sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD))
          && dBizdate != null) {
        // �ڳ�����
        if (dBizdate.toString().compareTo(dBeginDate.toString()) >= 0) {
          // ҵ�����ڴ�����������
          // m_sErrorMessage = "�ڳ�����ҵ������" + dBizdate + "Ӧ����ϵͳ��������" + dBeginDate
          // + "�������";
          String[] value = new String[] {
              dBizdate.toString(), dBeginDate.toString()
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000147", null, value);
          return false;
        }
      }

      // ���õ�λ���롢�ݹ���־
      btvos[i].setPk_corp(m_sCorpID);
      btvos[i].setCbillid(sPK);
      btvos[i].setCbilltypecode(m_sBillType);
      btvos[i].setVbillcode(sBillCode);

      // ������Դ��������
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        int iIndex = ((UIComboBox) bt.getComponent()).getSelectedIndex();
        String sSourceBillType = null;
        if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// ���۷�Ʊ
          sSourceBillType = ConstVO.m_sBillXSFP;
        }
        else if (iIndex != -1 && iIndex == m_ComboItemsVO.type_saleoutlist) {// ���۳��ⵥ
          sSourceBillType = ConstVO.m_sBillXSCKD;
        }
        else if (iIndex != -1 && iIndex == m_ComboItemsVO.type_waylossbill) {// ;��
          sSourceBillType = ConstVO.m_sBillKCTSD;
        }
        //else if (m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        //  sSourceBillType = ConstVO.m_sBillXSCKD;
        //}
        btvos[i].setCsourcebilltypecode(sSourceBillType);
      }

      // �õ��к�,���id
      Integer iRow = btvos[i].getIrownumber();
      String sInvID = btvos[i].getCinventoryid();

      if ((m_sBillType.equals(ConstVO.m_sBillQCRKD) == false && m_sBillType
          .equals(ConstVO.m_sBillQCXSCBJZD) == false)
          || sSourceModule == null || sSourceModule.trim().length() == 0) {
        // 1.�����ڳ����ݣ�2.���ڳ�����,���Ǵ�������Լ����ɵĵ���
        // �Ƿ����������
        // zlq 20050414 Ч���Ż�
        if (!m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {// 20050426 �ƻ��۵�������������
          UFBoolean bIsManageForFree = (UFBoolean) htFree.get(sInvID);
          if (bIsManageForFree != null && bIsManageForFree.booleanValue()) {
            // ��������������Ƿ�������������
            String sFree0 = btvos[i].getVfree0();
            if (sFree0 == null || sFree0.trim().length() == 0) {
              // m_sErrorMessage = "��" + iRow + "�еĴ�����������������û��������������Ϣ�������";
              String[] value = new String[] {
                String.valueOf(iRow)
              };
              m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000148", null, value);
              return false;
            }
          }
        }

        // �Ƿ񸨼�������
        UFBoolean bIsManageForAssi = (UFBoolean) htAss.get(sInvID);
        if (bIsManageForAssi == null) {
          Log.info("δ��ȡ���Ƿ񸨼���������Ϣ�����id��" + sInvID);
          return false;
        }
        if (bIsManageForAssi.booleanValue() && m_bIsAdjustBill == false) {
          // �Ǹ��������������ǵ��������Ƿ������˸���������
          UFDouble dAssiNumber = btvos[i].getNassistnum();
          if (dAssiNumber == null) {
            // m_sErrorMessage = "��" + iRow + "�еĴ���Ǹ�������������û�����븨���������������";
            String[] value = new String[] {
              String.valueOf(iRow)
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000149", null, value);
            return false;
          }
        }
      }

      // ��üƼ۷�ʽ����
      Integer iPriceCode = null;
      UFBoolean bAuditBatch = new UFBoolean(false);

      String[] sPrice = (String[]) htPrice.get(sInvID);
      if (sPrice != null && sPrice.length != 0 && sPrice[0] != null
          && sPrice[0].trim().length() != 0 && sPrice[0].charAt(0) > '0'
          && sPrice[0].charAt(0) < '7') {
        iPriceCode = new Integer(sPrice[0]);
        if (sPrice.length > 1 && sPrice[1] != null
            && sPrice[1].trim().length() != 0) {
          bAuditBatch = new UFBoolean(sPrice[1]);
        }
        else {
          sPrice = new String[2];
          sPrice[0] = iPriceCode.toString();
          sPrice[1] = "N";
        }
      }
      else {
        // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯û�ж���Ƽ۷�ʽ����δ�����䵽�˿����֯�������";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000150", null, value);
        return false;
      }

      if (iPriceCode != null) {
        btvos[i].setFpricemodeflag(iPriceCode);
      }
      else {
        // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯û�ж���Ƽ۷�ʽ����δ�����䵽�˿����֯�������";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000150", null, value);
        return false;
      }

      if (bAuditBatch != null) {
        btvos[i].setBauditbatchflag(bAuditBatch);
        String sBatch = btvos[i].getVbatch();
        if (bAuditBatch.booleanValue()
            && (sBatch == null || sBatch.trim().length() == 0)) {
          // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯����Ϊ�����κ��㣬��û���������κţ������";
          String[] value = new String[] {
            String.valueOf(iRow)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000151", null, value);
          return false;
        }
        // �����������������κ�������¼�����κ� 20050526
        // else if ((m_sBillType.equals(ConstVO.m_sBillRKTZD) || m_sBillType
        // .equals(ConstVO.m_sBillCKTZD))
        // && bAuditBatch.booleanValue() == false
        // && sBatch != null && sBatch.trim().length() != 0) {
        // //�������������������������ǰ����κ��㣬¼�������κ�
        // //m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯����Ϊ���ǰ����κ��㣬�����������κţ������";
        // String[] value = new String[]{String.valueOf(iRow)};
        // m_sErrorMessage =
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        // "UPP20143010-000152", null, value);
        // return false;
        // }
      }
      else {
        // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯û�ж����Ƿ����κ��㣬�����";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000153", null, value);
        return false;
      }

      // �����ί��ӹ����ϵ�����ȫ��ƽ���Ƽۣ����ܱ���
      if (iPriceCode.intValue() == ConstVO.QYPJ
          && m_sBillType.equals(ConstVO.m_sBillWWJGFLD)) {
        // m_sErrorMessage = "��ǰ�����֯����" + iRow + "�еĴ����ȫ��ƽ���Ƽ۷�ʽ��ί��ӹ������޷�֧�֣������";
        String[] value = new String[] {
          String.valueOf(iRow)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000175", null, value);
        return false;
      }

      // ����Ǽƻ��۵��������Ƽ۷�ʽ���Ǽƻ��ۻ�û�мƻ��ۣ����ܱ���
      if( m_bIsPlanedPriceBill ){
        if (iPriceCode.intValue() != ConstVO.JHJ ) {
          // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯�ļƼ۷�ʽ���Ǽƻ��ۼƼۣ������";
          String[] value = new String[] {
            String.valueOf(iRow)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000154", null, value);
          return false;
        }else {
          //����ǰ�ƻ���
          UFDouble dPlanedPrice = btvos[i].getNplanedprice();
          //������ƻ���
          UFDouble dNewPlanedPrice = btvos[i].getNprice();
          if (dPlanedPrice == null
              || dPlanedPrice.toString().trim().length() == 0
              || dNewPlanedPrice == null
              || dNewPlanedPrice.toString().trim().length() == 0) {
            // m_sErrorMessage = "��" + iRow + "�еĴ���ڵ�ǰ�����֯�Ǽƻ��ۼƼۣ���û�ж���ƻ��ۣ������";
            String[] value = new String[] {
              String.valueOf(iRow)
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000155", null, value);
            return false;
          }
        }
      }
      // ��������������������˵����������ͣ���������������ݷ�¼
      if (m_bIsInAdjustBill && bIsInputBillType) {
        String sBillItemID = btvos[i].getCadjustbillitemid();
        if (sBillItemID == null || sBillItemID.trim().length() == 0) {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000098")/*
                                                 * @res
                                                 * "���뱻�������ݺ�������뱻�������ݷ�¼�������"
                                                 */;
          return false;
        }
      }

      // �ƻ��۵���������ͬʱ�����з�¼����ͬһ����ļƻ���
      if (m_bIsPlanedPriceBill) {
        if (vInvID.contains(sInvID) == false) {
          vInvID.addElement(sInvID);
        }
        else {
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000099")/*
                                                 * @res
                                                 * "һ�żƻ��۵���������ͬʱ�����з�¼����ͬһ����ļƻ��ۣ������"
                                                 */;
          return false;
        }
      }

      // ��������۳ɱ���ת������ԴΪ��Ʊ�������ж�Ӧ���۳��ⵥID
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        String sSourceBilltype = btvos[i].getCsourcebilltypecode();
        if (sSourceBilltype != null
            && sSourceBilltype.equals(ConstVO.m_sBillXSFP)) {
          // ��Դ�����۷�Ʊ
          String sSaleAdviceID = btvos[i].getCsaleadviceid();
          // �ж�ҵ������
          boolean bIsFQ = false; // �Ƿ��Ƿ����տ�
          boolean bIsWT = false; // �Ƿ���ί�д���
          BillItem bt2 = getBillCardPanel().getHeadItem("cbiztypeid");
          if (bt2 != null) {
            String sBizTypeID = bt2.getValue();
            if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
              UIRefPane pane = (UIRefPane) bt2.getComponent();
              Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
              if (oRule != null) {
                if (oRule.toString().equals("W")) {
                  // ��ί�д���
                  bIsWT = true;
                }
                else if (oRule.toString().equals("F")) {
                  // �Ƿ����տ�
                  bIsFQ = true;
                }
              }
              if (bIsFQ) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // û��¼���Ӧ���۳��ⵥ
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000089")/*
                                                                     * @res
                                                                     * "��ǰ������Դ�����۷�Ʊ��ҵ�������Ƿ����տ�,��û��ѡ���Ӧ�����۳��ⵥ�������"
                                                                     */;
                  return false;
                }
              }
              else if (bIsWT) {
                if (sSaleAdviceID == null || sSaleAdviceID.trim().length() == 0) {
                  // û��¼���Ӧ���۳��ⵥ
                  m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance()
                      .getStrByID("20143010", "UPP20143010-000090")/*
                                                                     * @res
                                                                     * "��ǰ������Դ�����۷�Ʊ��ҵ��������ί�д���,��û��ѡ���Ӧ�����۳��ⵥ�������"
                                                                     */;
                  return false;
                }
              }
            }
          }
        }
      }

      int iDataGetMode = ConstVO.WQDSJ;
      String sAdjustable = "Y";
      if (btvos[i].getNprice() != null) {
        iDataGetMode = ConstVO.YHLR;
        sAdjustable = "N";
      }
      else if (btvos[i].getNmoney() != null) {
        iDataGetMode = ConstVO.YHLR;
        sAdjustable = "N";
      }
      btvos[i].setFdatagetmodelflag(new Integer(iDataGetMode));
      btvos[i].setFolddatagetmodelflag(new Integer(iDataGetMode));
      btvos[i].setFoutadjustableflag(new UFBoolean(sAdjustable));
      btvos[i].setBadjustedItemflag(new UFBoolean(false));
      btvos[i].setBrtvouchflag(new UFBoolean(false));

      // ����Ǽ����ϣ���������Ϊ����
      UFDouble ufdNumber = btvos[i].getNnumber();
      double dNumber = 0;
      if (ufdNumber != null) {
        dNumber = ufdNumber.doubleValue();
      }
      if (bIsWithdrawalflag && dNumber > 0) {
        // m_sErrorMessage = "��ǰ�����Ǽ����ϵ��ݣ�������������С��0����" + (i + 1) +
        // "�еĴ�������������������";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000156", null, value);
        return false;
      }

      // �����ʲ���Ϊ��
      UFDouble ufdAstRate = btvos[i].getNchangerate();
      double dAstRate = 0;
      if (ufdAstRate != null) {
        dAstRate = ufdAstRate.doubleValue();
      }
      else if (btvos[i].getCastunitid() != null) {
        // m_sErrorMessage = "��" + (i + 1) + "�д����¼¼���˸�������λ����û��¼�뻻����,���ܱ��棬�����";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000157", null, value);
        return false;
      }
      if (dAstRate < 0) {
        // m_sErrorMessage = "��" + (i + 1) + "�д����¼¼��Ļ�����Ϊ����,���ܱ��棬�����";
        String[] value = new String[] {
          String.valueOf(i + 1)
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000158", null, value);
        return false;
      }

      // ���ݺϷ��Լ��
      // btvos[i].validate();
      // ����validate�������븸��smartVO��throws�Ӿ��ͻ�����Ը���verify
      btvos[i].verify();
    }
    if ((btvos == null || btvos.length == 0) && bIsWithdrawalflag) {
      // �Ǽ����ϣ�û�б��壬�޸ĵ�
      for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
        // ����Ǽ����ϣ���������Ϊ����
        UFDouble ufdNumber = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
            .getNnumber();
        double dNumber = 0;
        if (ufdNumber != null) {
          dNumber = ufdNumber.doubleValue();
        }
        if (dNumber > 0) {
          // m_sErrorMessage = "��ǰ�����Ǽ����ϵ��ݣ�������������С��0����" + (i + 1) +
          // "�еĴ�������������������";
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000156", null, value);
          return false;
        }
      }
    }

    // ��������
    for (int i = 0; i < m_bd.getHeadShowItems().length; i++) {
      BillItem bt = m_bd.getHeadShowItems()[i];
      if (bt.isNull() && bt.isEdit()) {
        String sAttrName = bt.getKey();
        String sColumnName = bt.getCaptionLabel().getText();
        if (bhvo.getAttributeValue(sAttrName) == null) {
          // m_sErrorMessage = sColumnName + "�Ǳ������û���������ݣ������";
          String[] value = new String[] {
            sColumnName
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000159", null, value);
          return false;
        }
      }
    }
    for (int i = 0; i < m_bd.getTailShowItems().length; i++) {
      BillItem bt = m_bd.getTailShowItems()[i];
      if (bt.isNull() && bt.isEdit()) {
        // �Ǳ���
        String sAttrName = bt.getKey();
        String sColumnName = bt.getCaptionLabel().getText();
        if (bhvo.getAttributeValue(sAttrName) == null) {
          // m_sErrorMessage = sColumnName + "�Ǳ������û���������ݣ������";
          String[] value = new String[] {
            sColumnName
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000159", null, value);
          return false;
        }
      }
    }
    // �����������
    for (int i = 0; i < m_bd.getBodyShowItems().length; i++) {
      BillItem bt = m_bd.getBodyShowItems()[i];
      if (bt.isNull() && bt.isEdit()) {
        // �����
        String sAttrName = bt.getKey();
        String sColumnName = bt.getName();
        for (int j = 0; j < btvos.length; j++) {
          Integer iRow = btvos[j].getIrownumber();
          if (btvos[j].getAttributeValue(sAttrName) == null) {
            // m_sErrorMessage = sColumnName + "�Ǳ��������" + iRow + "��û���������ݣ������";
            String[] value = new String[] {
                sColumnName, String.valueOf(iRow)
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000160", null, value);
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * ���� BillListPanel ����ֵ��
   * 
   * @return nc.ui.ia.bill.IABillListPanel
   */
  /* ���棺�˷������������ɡ� */
  protected IABillListPanel getBillListPanel() {
    if (ivjBillListPanel == null) {
      try {
        ivjBillListPanel = new IABillListPanel(m_sCorpID);
        ivjBillListPanel.setName("BillListPanel");
        ivjBillListPanel.setBounds(0, 0, 774, 419);
        // user code begin {1}
        // ivjBillListPanel.addEditListener(this);
        // ivjBillListPanel.addMouseListener(this);
        // ivjBillListPanel.addHeadEditListener(new BillHeadListener());
        // ivjBillListPanel.addBodyEditListener(new BillBodyListener());
        // ivjBillListPanel.getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // user code end
      }
      catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjBillListPanel;
  }

  /**
   * �����������ȼ�����֧��ȫ���̲�����
   */
  protected void hotKeyPressed(javax.swing.KeyStroke hotKey) throws Exception {
    Log.info("����" + hotKey.getKeyChar());
  }

  /**
   * ��������:�ж��Ƿ������ <p/> ����: <p/> ����ֵ:�Ƿ������ <p/> �쳣:
   */
  protected boolean isCanAddBill() throws Exception {
    if (m_bIsCanAddBill == null) {
      String sAccountYear = ce.getAccountYear();
      String sAccountMonth = ce.getAccountMonth();
      AccountVO avo = new AccountVO();
      avo.setPk_corp(m_sCorpID);
      avo.setCaccountyear(sAccountYear);
      avo.setCaccountmonth(sAccountMonth);
      AccountVO[] avoResult = AccountBO_Client
          .queryByVO(avo, new Boolean(true));
      if (avoResult != null && avoResult.length != 0) {
        // ����ĩ����
        // m_sErrorMessage = "��ǰ����ڼ�" + sAccountYear + "��" + sAccountMonth +
        // "�ڼ�����ĩ���ˣ����������ӵ���";
        String[] value = new String[] {
            sAccountYear, sAccountMonth
        };
        m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000161", null, value);
        m_bIsCanAddBill = new UFBoolean(false);
      }
      else {
        String sLogDate = ce.getBusinessDate().toString();
        UFDate[] sDates = CommonDataBO_Client.getMonthDates(m_sCorpID,
            m_sStartPeriod);
        String sBeginDate = sDates[0].toString();
        String sEndDate = sDates[1].toString();
        if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
            || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          if (sLogDate.compareTo(sEndDate) > 0) {
            // ��½���ڴ��������ڼ�Ľ�ֹ����
            // m_sErrorMessage = "��ǰ��¼����" + sLogDate + "�������û���ڼ��ֹ����" + sEndDate
            // + "������¼���ڳ�����";
            String[] value = new String[] {
                sLogDate, sEndDate
            };
            m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000162", null, value);
            m_bIsCanAddBill = new UFBoolean(false);
          }
        }
        else if (sLogDate.compareTo(sBeginDate) < 0) {
          // �ճ����ݣ���½����С����������
          // m_sErrorMessage = "��ǰ��¼����" + sLogDate + "������������" + sBeginDate +
          // "������¼�뵥��";
          String[] value = new String[] {
              sLogDate, sBeginDate
          };
          m_sErrorMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000163", null, value);
          m_bIsCanAddBill = new UFBoolean(false);
        }
        m_bIsCanAddBill = new UFBoolean(true);
      }
    }
    return m_bIsCanAddBill.booleanValue();
  }

  /**
   * ��������:���뵥��ģ�� <p/> ����: String sTitle ----- ���� String sBillTypeCode ----- ��������
   * <p/> ����ֵ: <p/> �쳣:
   */
  protected void loadCardTemplet(String sTitle, String sBillTypeCode) {
    try {
      m_htInvAndFix = new Hashtable();
      m_refpaneInvBack = new UIRefPane();
      m_refpaneInvBack.setRefNodeName("�������");/*-=notranslate=-*/
      // ����ֵ
      m_sTitle = sTitle;
      m_sBillType = sBillTypeCode;
      // ���ģ��
      BillTempletVO btvo = null;
      // �ڳ���ⵥ��Ϊ������ⵥ
      // �ڳ����۳ɱ���ת����Ϊ���۳ɱ���ת��
      if (sBillTypeCode.equals(ConstVO.m_sBillQCRKD)) {
        btvo = getBillCardPanel().getDefaultTemplet(ConstVO.m_sBillQTRKD, null,
            ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      else if (sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        btvo = getBillCardPanel().getDefaultTemplet(ConstVO.m_sBillXSCBJZD,
            null, ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      else {
        // ���ģ��
        btvo = getBillCardPanel().getDefaultTemplet(sBillTypeCode, null,
            ce.getUser().getPrimaryKey(), m_sCorpID);
      }
      if (btvo == null) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000100")/* @res "û�л�õ��ݱ���ģ������" */);
        return;
      }
      m_bd = new BillData(btvo);
      // ��ConstVO�ж���ĳ�������ͱȽ��ж��ǳ������ͻ����������
      if (m_sBillType.equals(ConstVO.m_sBillBFD)
          || m_sBillType.equals(ConstVO.m_sBillDBCKD)
          || m_sBillType.equals(ConstVO.m_sBillCLCKD)
          || m_sBillType.equals(ConstVO.m_sBillQTCKD)
          || m_sBillType.equals(ConstVO.m_sBillWWJGFLD)
          || m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        m_bIsOutBill = true;
        m_bIsOtherBill = false;
        m_bIsInBill = false;
      }
      else {
        m_bIsOutBill = false;
        m_bIsOtherBill = true;
        m_bIsInBill = false;
        for (int i = 0; i < ConstVO.m_sInBills.length; i++) {
          if (m_sBillType.equals(ConstVO.m_sInBills[i])) {
            m_bIsOtherBill = false;
            m_bIsInBill = true;
            break;
          }
        }
      }

      if (m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
        // ����������
        m_bIsInAdjustBill = true;
      }
      else {
        m_bIsInAdjustBill = false;
      }
      if (m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        // �Ǽƻ��۵�����,���Ӱ�ť������ѡ��
        m_bIsPlanedPriceBill = true;
      }
      else {
        m_bIsPlanedPriceBill = false;
      }
      if (m_sBillType.equals(ConstVO.m_sBillRKTZD)
          || m_sBillType.equals(ConstVO.m_sBillCKTZD)
          || m_sBillType.equals(ConstVO.m_sBillJHJTZD)) {
        // �ǵ�����
        m_bIsAdjustBill = true;
      }
      else {
        m_bIsAdjustBill = false;
      }
      //�ǳ��ⵥ����ָ����ť����
      if(!m_bIsOutBill){
        buttonTree.getButton(IABtnConst.BTN_AUDIT).setVisible(false);
      }
      // ��ʼ���ñ�ͷ��Ԫ�ز���
      // ���ÿ����֯����
      // "crdcenterid" == �����֯
      BillItem bt = m_bd.getHeadItem("crdcenterid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        String sWhere = pane.getRefModel().getWherePart();
        pane.setWhereString(sWhere + " and property in ("
            + ConstVO.ICalbodyType_All + "," + ConstVO.ICalbodyType_COST + ")");
        // ���Ӽ���
        pane.addValueChangedListener(this);
        pane.setButtonFireEvent(true);
      }
      bt = m_bd.getHeadItem("cstockrdcenterid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        String sWhere = pane.getRefModel().getWherePart();
        pane.setWhereString(sWhere + " and property in ("
            + ConstVO.ICalbodyType_All + "," + ConstVO.ICalbodyType_STOR + ")");
        // ���Ӽ���
        pane.addValueChangedListener(this);
        pane.setButtonFireEvent(true);
      }

      // ���õ������Ͳ��ղ��ɼ�
      // "cbilltypecode" == �������ͱ���
      bt = m_bd.getHeadItem("cbilltypecode");
      if (bt != null) {
        bt.setShow(false);
      }
      // �����շ������յĹ�������//"cdispatchid" == ������
      bt = m_bd.getHeadItem("cdispatchid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        pane.getRef().getRefModel().setPk_corp(m_sCorpID);
        pane.setNotLeafSelectedEnabled(false);
        String sWhere = "";
        if ((m_bIsOtherBill || m_bIsOutBill) == false) {
          sWhere = " and rdflag = 0 ";
        }
        else {
          sWhere = " and rdflag = 1 ";
        }
        pane.getRef().getRefModel().addWherePart(sWhere);
      }
      // ���ÿ��̲��գ����㶪ʧ����ʾ���//"ccustomvendorid" == ��Ӧ��
      bt = m_bd.getHeadItem("ccustomvendorid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        pane.getRef().getRefModel()
            .setRefNameField("bd_cubasdoc.custshortname");
      }

      // ����ҵ�����Ͳ��յĹ�������//"cbiztypeid" == ҵ������
      bt = m_bd.getHeadItem("cbiztypeid");
      if (bt != null) {
        UIRefPane pane = (UIRefPane) bt.getComponent();
        AbstractRefModel refmodel = (AbstractRefModel) pane.getRef()
            .getRefModel();
        refmodel.setPk_corp(m_sCorpID);
        String[] sFieldCode = refmodel.getFieldCode();
        Vector vTemp = new Vector(1, 1);
        for (int i = 0; i < sFieldCode.length; i++) {
          vTemp.addElement(sFieldCode[i]);
        }
        vTemp.addElement("receipttype");
        vTemp.addElement("verifyrule");
        sFieldCode = new String[vTemp.size()];
        vTemp.copyInto(sFieldCode);
        refmodel.setFieldCode(sFieldCode);
        String sWhere = "";
        if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) == false
            && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
          // �����ڳ����۳ɱ���ת�������۳ɱ���ת��
          String sAnd = "";
          if (m_bIsAdjustBill == false) {
            // ���ǵ�������ҵ�����Ͳ����Ƿ����տί�д���
            // ������ҵ�����Ϳ������ѡ
            sAnd = sAnd + " and verifyrule != 'F' ";
            sAnd = sAnd + " and verifyrule != 'W' ";
          }
          if (m_sBillType.equals(ConstVO.m_sBillCGRKD)) {
            // �ǲɹ���ⵥ
            sAnd = sAnd + " and busiprop in (0,2) ";
          }
          sWhere = sWhere + sAnd;
        }
        else if (m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
          // ���ڳ����۳ɱ���ת��
          String sAnd = " and verifyrule in ('F','W') ";
          sWhere = sWhere + sAnd;
        }
        else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
          // �����۳ɱ���ת��
          String sAnd = " and busiprop in (1,2) ";
          sWhere = sWhere + sAnd;
        }
        refmodel.addWherePart(sWhere);
      }
      // ���ȱʡ�Ĳֿ���յ�����//"cwarehouseid" == �ֿ�
      bt = m_bd.getHeadItem("cwarehouseid");
      if (bt != null) {
        // ���ȱʡ�Ĳֿ���յ�����
        UIRefPane warehouseRef = (UIRefPane) bt.getComponent();
        AbstractRefModel refmodel = (AbstractRefModel) warehouseRef.getRef()
            .getRefModel();
        String[] sFieldCode = refmodel.getFieldCode();
        Vector vTemp = new Vector(1, 1);
        for (int i = 0; i < sFieldCode.length; i++) {
          vTemp.addElement(sFieldCode[i]);
        }
        vTemp.addElement("pk_calbody");
        sFieldCode = new String[vTemp.size()];
        vTemp.copyInto(sFieldCode);
        refmodel.setFieldCode(sFieldCode);
        String[] sFieldName = refmodel.getFieldName();
        if (sFieldName != null) {
          vTemp = new Vector(1, 1);
          for (int i = 0; i < sFieldName.length; i++) {
            vTemp.addElement(sFieldName[i]);
          }
          vTemp.addElement("");
          sFieldName = new String[vTemp.size()];
          vTemp.copyInto(sFieldName);
          refmodel.setFieldName(sFieldName);
        }
        m_sOldWareCondition = refmodel.getWherePart();
        m_sOldWareCondition = m_sOldWareCondition
            + " and iscalculatedinvcost = 'Y' ";
        warehouseRef.setWhereString(m_sOldWareCondition);
      }
      // ���ȱʡ��ҵ��Ա���յ�����
      // "cemployeeid" == ҵ��Ա
      bt = m_bd.getHeadItem("cemployeeid");
      if (bt != null) {
        // ���ȱʡ��ҵ��Ա���յ�����
        UIRefPane employeeRef = (UIRefPane) bt.getComponent();
        m_sOldUserCondition = employeeRef.getRef().getRefModel().getWherePart();
      }
      // ���Ƶ���IDתΪ����
      bt = m_bd.getTailItem("coperatorid");
      BillItem bt2 = m_bd.getTailItem("coperatorname");
      if (bt != null && bt2 != null) {
        String[] sFor = new String[] {
          "coperatorname->getColValue(sm_user,user_name,cuserid,coperatorid)"
        };
        bt.setLoadFormula(sFor);
      }

      // �Է���˾
      bt = m_bd.getHeadItem("cothercorpid");
      if (bt != null) {
        // ����������Ȩ��
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // �Է���֯
      bt = m_bd.getHeadItem("cothercalbodyid");
      if (bt != null) {
        // ����������Ȩ��
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.setWhereString("");
        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // ������˾
      bt = m_bd.getHeadItem("coutcorpid");
      if (bt != null) {
        // ����������Ȩ��
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      // ������֯
      bt = m_bd.getHeadItem("coutcalbodyid");
      if (bt != null) {
        // ����������Ȩ��
        UIRefPane uiRef = (UIRefPane) bt.getComponent();

        uiRef.setWhereString("");
        uiRef.getRefModel().setUseDataPower(false);
        bt.setComponent(uiRef);
      }

      bt = m_bd.getHeadItem("btransferincometax_h");
      if (bt != null) {
        UICheckBox cb = (UICheckBox) bt.getComponent();
        cb.addItemListener(this);
        bt.setComponent(cb);
      }

      // ��ʼ���ñ����ֶεĲ���
      // ���������������������ǲ���
      if (m_bIsInAdjustBill) {
        // "cadjustbilltype" == ��������������
        bt = m_bd.getBodyItem("cadjustbilltype");
        if (bt != null) {
          // ���ñ���������ѡ��Ĳ���.
          getUIRefPaneBillType().setVisible(true);
          bt.setComponent(getUIRefPaneBillType());
          bt.setDataType(IBillItem.UFREF);
        }
        // ���������������ݺ��ǲ���
        // "cadjustbill" == ���������ݺ�
        bt = m_bd.getBodyItem("cadjustbill");
        if (bt != null) {
          // ���ñ���������ѡ��Ĳ���.
          getUIRefPaneAdjustBill().setVisible(true);
          getUIRefPaneAdjustBill().setMaxLength(30);
          bt.setComponent(getUIRefPaneAdjustBill());
          bt.setDataType(IBillItem.UFREF);
          getUIRefPaneAdjustBill().addValueChangedListener(this);
          getUIRefPaneAdjustBill().setButtonFireEvent(true);
        }
      }
      // "cadjustbillitem" == ���������ݷ�¼
      bt = m_bd.getBodyItem("cadjustbillitem");
      if (bt != null) {
        // ���ñ��������ݷ�¼ѡ��Ĳ���.
        // һ�㵥���ǻس嵥�ݷ�¼
        // ����������
        bt.setIDColName("cadjustbillitemid");
        getUIRefPaneAdjustBillItem().setVisible(true);
        getUIRefPaneAdjustBillItem().setMaxLength(30);
        bt.setComponent(getUIRefPaneAdjustBillItem());
        bt.setDataType(IBillItem.UFREF);
        getUIRefPaneAdjustBillItem().addValueChangedListener(this);
        getUIRefPaneAdjustBillItem().setButtonFireEvent(true);
      }
      // �������κŵĲ���
      // ����ǳ��ⵥ�Ҳ����ڳ����۳ɱ���ת������������κŹ��������κ�Ϊ����
      if (((m_bIsOtherBill || m_bIsOutBill)
          || m_sBillType.equals(ConstVO.m_sBillCKTZD) || m_sBillType
          .equals(ConstVO.m_sBillRKTZD))
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        bt = m_bd.getBodyItem("vbatch");
        if (bt != null) {
          getUIRefPaneBatch().setVisible(true);
          bt.setComponent(getUIRefPaneBatch());
          bt.setDataType(IBillItem.UFREF);
          // ���κŰ������Ϊ0��
          getUIRefPaneBatch().setHasZero(true);
          if (m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
            // �����������κ�Ҳ�����ֹ�����
            getUIRefPaneBatch().setAutoCheck(false);
            // //�����������κŰ������Ϊ0��
            // getUIRefPaneBatch().setHasZero(true);
          }
          else if (m_sBillType.equals(ConstVO.m_sBillCKTZD)) {
            // //������������κŰ������Ϊ0��
            // getUIRefPaneBatch().setHasZero(true);
          }
        }
      }
      else if (m_bIsInBill) {
        bt = m_bd.getBodyItem("vbatch");
        if (bt != null) {
          getUIRefPaneBatch().setVisible(true);
          bt.setComponent(getUIRefPaneBatch());
          bt.setDataType(IBillItem.UFREF);
          // ��ⵥ���κ�Ҳ�����ֹ�����
          getUIRefPaneBatch().setAutoCheck(false);
          // ��ⵥ���κŰ������Ϊ0��
          getUIRefPaneBatch().setHasZero(true);
        }
      }
      // ���������
      bt = m_bd.getBodyItem("vfree0");
      if (bt != null) {
        getUIRefPaneFreeItem().setVisible(true);
        bt.setComponent(getUIRefPaneFreeItem());
        bt.setDataType(IBillItem.UFREF);
        getUIRefPaneFreeItem().setMaxLength(500);
        // ���Ӽ���
        getUIRefPaneFreeItem().addValueChangedListener(this);
        getUIRefPaneFreeItem().setButtonFireEvent(true);
      }
      // ���ô�����գ���ʾ������λ//"cinventorycode" == �������
      bt = m_bd.getBodyItem("cinventorycode");
      if (bt != null) {
        // ���ȱʡ�Ĵ�����յ�����
        m_refpaneInv = (UIRefPane) bt.getComponent();
        AbstractRefModel refmodel = (AbstractRefModel) m_refpaneInv.getRef()
            .getRefModel();
        String[] sFieldCode = refmodel.getFieldCode();
        Vector vTemp = new Vector(1, 1);
        for (int i = 0; i < sFieldCode.length; i++) {
          vTemp.addElement(sFieldCode[i]);
        }
        vTemp.addElement("bd_measdoc.measname");
        vTemp.addElement("bd_produce.jhj");
        vTemp.addElement("bd_invmandoc.planprice");
        vTemp.addElement("bd_invbasdoc.assistunit");
        vTemp.addElement("bd_measdoc.pk_measdoc");
        vTemp.addElement("bd_produce.materstate");
        vTemp.addElement("bd_produce.pricemethod");
        sFieldCode = new String[vTemp.size()];
        vTemp.copyInto(sFieldCode);
        refmodel.setFieldCode(sFieldCode);
        String[] sFieldName = refmodel.getFieldName();
        if (sFieldName != null) {
          vTemp = new Vector(1, 1);
          for (int i = 0; i < sFieldName.length; i++) {
            vTemp.addElement(sFieldName[i]);
          }
          vTemp.addElement("������λ");/*-=notranslate=-*/
          vTemp.addElement("");
          vTemp.addElement("");
          vTemp.addElement("");
          vTemp.addElement("");
          vTemp.addElement("");
          vTemp.addElement("");
          sFieldName = new String[vTemp.size()];
          vTemp.copyInto(sFieldName);
          refmodel.setFieldName(sFieldName);
        }
        String sTableName = refmodel.getTableName();
        sTableName = sTableName
            + " inner join bd_measdoc on bd_invbasdoc.pk_measdoc = bd_measdoc.pk_measdoc and bd_invmandoc.sealflag = 'N' and bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N'  inner join bd_produce on bd_invmandoc.pk_invmandoc = bd_produce.pk_invmandoc";
        refmodel.setTableName(sTableName);
        refmodel.setChangeTableSeq(false);
        //refmodel.setMatchPkWithWherePart(true);
        m_sOldInvCondition = refmodel.getWherePart();
        refmodel.setWherePart(m_sOldInvCondition);
        m_refpaneInvBack.setRefModel(refmodel);
        // ���Ӽ���
        m_refpaneInv.addValueChangedListener(this);
        m_refpaneInv.setButtonFireEvent(true);
        // ���ö�ѡ
        m_refpaneInv.setMultiSelectedEnabled(true);
        m_refpaneInv.setTreeGridNodeMultiSelected(true);
      }
      // �����ʶ
      bt = m_bd.getBodyItem("cinventoryid");
      if (bt != null) {
        // ���ù�ʽ
        loadInvFormula(bt);
      }
      // ���óɱ������������
      bt = m_bd.getBodyItem("vbomcodecode");
      if (bt != null) {
        // ���ȱʡ�ĳɱ����������
        UIRefPane BomRef = (UIRefPane) bt.getComponent();
        AbstractRefModel refmodel = (AbstractRefModel) BomRef.getRef()
            .getRefModel();
        refmodel.setStrPatch(" distinct ");
        String sTableName = refmodel.getTableName();
        sTableName = sTableName + ",bd_produce";
        refmodel.setTableName(sTableName);
        m_sOldBomCondition = refmodel.getWherePart();
        m_sOldBomCondition = m_sOldBomCondition
            + " and bd_produce.pk_invmandoc = bd_invmandoc.pk_invmandoc ";
        if (m_sBillType.equals(ConstVO.m_sBillWWJGFLD)) {
          // ��ί��ӹ����ϵ�
          m_sOldBomCondition = m_sOldBomCondition
              + " and bd_produce.sfcbdx = 'N' ";
        }
        else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)
            || m_sBillType.equals(ConstVO.m_sBillQTCKD)
            || m_sBillType.equals(ConstVO.m_sBillCKTZD)) {
          // �ǲ��ϳ��ⵥ���������ⵥ����������
          m_sOldBomCondition = m_sOldBomCondition
              + " and bd_produce.sfcbdx = 'Y' ";
        }
        refmodel.setWherePart(m_sOldBomCondition);
      }
      // ���ø������Ĳ���
      // "castunitname" == ����λ
      bt = m_bd.getBodyItem("castunitname");
      if (bt != null) {
        // ���ȱʡ�ļ�����λ���յ�����
        UIRefPane astRef = (UIRefPane) bt.getComponent();
        if (bt.getDataType() == IBillItem.UFREF) {
          astRef.setRefModel(new nc.ui.ia.pub.AssunitRefmodel());
        }
        bt.setComponent(astRef);
        bt.setIDColName("castunitid");
        String[] sFor = new String[] {
          "castunitname->getColValue(bd_measdoc,measname,pk_measdoc,castunitid)"
        };
        bt.setLoadFormula(sFor);
      }

      // ������Ŀ����
      bt = m_bd.getBodyItem("cprojectcode");
      if (bt != null) {
        // ������Ŀ����
        getUIRefPaneJob().setVisible(true);
        bt.setComponent(getUIRefPaneJob());
        bt.setDataType(5);
        String[] sFor = new String[] {
            "cprojectcode->getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cprojectid)",
            "cprojectcode->getColValue(bd_jobbasfil,jobcode,pk_jobbasfil,cprojectcode)"
        };
        bt.setLoadFormula(sFor);
      }
      // ��Ŀ����
      bt = m_bd.getBodyItem("cprojectname");
      if (bt != null) {
        String[] sFor = new String[] {
            "cprojectname->getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cprojectid)",
            "cprojectname->getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cprojectname)"
        };
        bt.setLoadFormula(sFor);
      }
      // ������Ŀ�׶β���
      bt = m_bd.getBodyItem("cprojectphasecode");
      if (bt != null) {
        // ������Ŀ�׶β���
        getUIRefPaneJobParse().setVisible(true);
        bt.setComponent(getUIRefPaneJobParse());
        bt.setDataType(5);
        // ����������
        bt.setIDColName("cprojectphase");
        String[] sFors = new String[3];
        sFors[0] = "cprojectphasename->getColValue(bd_jobobjpha,pk_jobphase,pk_jobobjpha,cprojectphase)";
        sFors[1] = "cprojectphasecode->getColValue(bd_jobphase,jobphasecode,pk_jobphase,cprojectphasename)";
        sFors[2] = "cprojectphasename->getColValue(bd_jobphase,jobphasename,pk_jobphase,cprojectphasename)";
        bt.setLoadFormula(sFors);
      }
      // ���ù̶��ʲ�����//20050525 �رչ̶��ʲ��ʹ���ӿ�
      // bt = m_bd.getBodyItem("cfadevicecode");
      // if (bt != null) {
      // ���ù̶��ʲ�����
      // if ((m_bIsOtherBill || m_bIsOutBill)) {
      // //���ⵥ
      // getUIRefPaneFacard().setVisible(true);
      // bt.setComponent(getUIRefPaneFacard());
      // } else {
      // getUIRefPaneFacardEquipment().setVisible(true);
      // bt.setComponent(getUIRefPaneFacardEquipment());
      // }
      // bt.setDataType(5);
      // }
      // ����������IDתΪ����
      bt = m_bd.getBodyItem("cwp");
      bt2 = m_bd.getBodyItem("cwpcode");
      BillItem bt3 = m_bd.getBodyItem("cwpname");
      if (bt != null && bt2 != null && bt3 != null) {
        String[] sFor = new String[] {
            "cwpcode->getColValue(pd_wk,gzzxbm,pk_wkid,cwp)",
            "cwpname->getColValue(pd_wk,gzzxmc,pk_wkid,cwp)"
        };
        bt.setLoadFormula(sFor);
      }
      // �������IDתΪ����
      bt = m_bd.getBodyItem("cauditorid");
      bt2 = m_bd.getBodyItem("cauditorname");
      if (bt != null && bt2 != null) {
        String[] sFor = new String[] {
          "cauditorname->getColValue(sm_user,user_name,cuserid,cauditorid)"
        };
        bt.setLoadFormula(sFor);
      }
      // ���ù������Ĳ���
      bt = m_bd.getBodyItem("cwpcode");
      if (bt != null) {
        // ���ù������Ĳ���
        getUIRefPaneWkCenter().setVisible(true);
        bt.setComponent(getUIRefPaneWkCenter());
        AbstractRefModel refmodel = (AbstractRefModel) getUIRefPaneWkCenter()
            .getRef().getRefModel();
        m_sOldWkCondition = refmodel.getWherePart();
        bt.setDataType(5);
      }

      // ���ڳ���ⵥ���ڳ����۳ɱ���ת��
      if (sBillTypeCode.equals(ConstVO.m_sBillQCRKD)
          || sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        // ���ڳ����ݣ����뵼�밴ť
        if (m_bIsAddImportButton == false) {
          // ���ڳ����ݣ����뵼�밴ť
          //buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY).addChildButton(buttonTree.getButton(IABtnConst.BTN_IMPORT_BILL));
          m_bIsAddImportButton = true;
        }
        // ʹ����Ļس嵥�ݷ�¼���ɼ�
        bt = m_bd.getBodyItem("cadjustbillitem");
        bt.setShow(false);
        bt.setEdit(false);
        bt.setEnabled(false);
        // ����ʱ�İ�ť
//        m_aryButtonGroupCard = new ButtonObject[9];
//        m_aryButtonGroupCard[0] = buttonTree.getButton(IABtnConst.BTN_ADD_MANUAL);
//        m_aryButtonGroupCard[1] = buttonTree.getButton(IABtnConst.BTN_BILL_EDIT);
//        m_aryButtonGroupCard[2] = buttonTree.getButton(IABtnConst.BTN_SAVE);
//        m_aryButtonGroupCard[3] = buttonTree.getButton(IABtnConst.BTN_LINE);
//        m_aryButtonGroupCard[4] = buttonTree.getButton(IABtnConst.BTN_BILL_CANCEL);
//        m_aryButtonGroupCard[5] = buttonTree.getButton(IABtnConst.BTN_BILL_DELETE);
//        m_aryButtonGroupCard[6] = buttonTree.getButton(IABtnConst.BTN_PRINT);
//        m_aryButtonGroupCard[7] = buttonTree.getButton(IABtnConst.BTN_QUERY);
//        m_aryButtonGroupCard[8] = buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY);
//        // �б�ʱ�İ�ť
//        m_aryButtonGroupList = new ButtonObject[7];
//        m_aryButtonGroupList[0] = buttonTree.getButton(IABtnConst.BTN_QUERY);
//        m_aryButtonGroupList[1] = buttonTree.getButton(IABtnConst.BTN_BILL_EDIT);
//        m_aryButtonGroupList[2] = buttonTree.getButton(IABtnConst.BTN_BILL_DELETE);
//        m_aryButtonGroupList[3] = buttonTree.getButton(IABtnConst.BTN_BROWSE_LOCATE);
//        m_aryButtonGroupList[4] = buttonTree.getButton(IABtnConst.BTN_PRINT);
//        m_aryButtonGroupList[5] = buttonTree.getButton(IABtnConst.BTN_BROWSE_REFRESH);
//        m_aryButtonGroupList[6] = buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY);
      }
      // �����Ƿ������
      bt = m_bd.getHeadItem("bwithdrawalflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_yes);// ��
        uicombobox.addItem(m_ComboItemsVO.name_no);// ��
        if (m_bIsICStart) {
          // ���������ã�Ĭ���Ǽ����ϵ�
          uicombobox.setSelectedIndex(m_ComboItemsVO.type_yes);
        }
        else {
          // ������δ���ã�Ĭ�ϲ��Ǽ����ϵ�
          uicombobox.setSelectedIndex(m_ComboItemsVO.type_no);
        }
      }
      // �ɹ���ⵥ���ݹ���־��Ϊ������
      bt = m_bd.getHeadItem("bestimateflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_estimated_yes);// �ݹ�
        uicombobox.addItem(m_ComboItemsVO.name_estimated_no);// ���ݹ�
        uicombobox.setSelectedIndex(m_ComboItemsVO.type_estimated_no);
      }
      // ���Ƿ���Ʒ��Ϊ������
      bt = m_bd.getBodyItem("blargessflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_yes);// ��
        uicombobox.addItem(m_ComboItemsVO.name_no);// ��
        uicombobox.setSelectedIndex(m_ComboItemsVO.type_no);
      }
      // ������������Ϊ������
      bt = m_bd.getHeadItem("fallocflag");
      if (bt != null && bt.getComponent() instanceof UIComboBox) {
        UIComboBox uicombobox = (UIComboBox) bt.getComponent();
        uicombobox.addItem(m_ComboItemsVO.name_transfer_direct);// ֱ�˵���
        uicombobox.addItem(m_ComboItemsVO.name_transfer_instore);// ������
        uicombobox.addItem(m_ComboItemsVO.name_transfer_stock);// ���ɵ���
      }
      if (sBillTypeCode.equals(ConstVO.m_sBillXSCBJZD)) {
        // ���۳ɱ���ת����������Դ��Ϊ������
        bt = m_bd.getHeadItem("cbillsource");
        if (bt != null && bt.getComponent() instanceof UIComboBox) {
          //ivjUIComboBoxSource = (UIComboBox) bt.getComponent();
          //getUIComboBoxSource().addItem("");// ���۳��ⵥ
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_saleoutlist);// ���۳��ⵥ
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_salebill);// ���۷�Ʊ
          getUIComboBoxSource().addItem(m_ComboItemsVO.name_waylossbill);// ���;��
          getUIComboBoxSource().setSelectedIndex(-1);
          getUIComboBoxSource().setVisible(true);
          bt.setComponent(getUIComboBoxSource());
          ((UIComboBox) bt.getComponent()).addItemListener(billSourceListener);
        }
        if (m_bIsAddChooseButton == false) {
          // �����۳ɱ���ת���ݣ�����ѡ��ť
          //buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY).addChildButton(buttonTree.getButton(IABtnConst.BTN_CHOOSESALEBILL));
          m_bIsAddChooseButton = true;
          btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
        }
      }
      else if (sBillTypeCode.equals(ConstVO.m_sBillQCXSCBJZD)) {
        // �ڳ����۳ɱ���ת����������Դ��Ϊ������
        bt = m_bd.getHeadItem("cbillsource");
        if (bt != null && bt.getComponent() instanceof UIComboBox) {
          UIComboBox uicombobox = (UIComboBox) bt.getComponent();
          // uicombobox.addItem("���۳��ⵥ");
          uicombobox.addItem(m_ComboItemsVO.name_saleoutlist);
          bt.setShow(false);
          bt.setEnabled(false);
        }
      }
      // //��ͷ�Զ������
      // nc.ui.bd.service.BDDef bdef = new nc.ui.bd.service.BDDef();
      // m_voHeaddef = BDDef.queryDefVO(ConstVO.m_sDefHeadName, m_sCorpID);
      m_bd.updateItemByDef(m_voHeaddef, "vdef", true);
      // //�����Զ������
      // m_voBodydef = BDDef.queryDefVO(ConstVO.m_sDefBodyName, m_sCorpID);
      m_bd.updateItemByDef(m_voBodydef, "vdef", false);

      bt = m_bd.getBodyItem("btransferincometax");
      if (bt != null) {
        UICheckBox cb = (UICheckBox) bt.getComponent();
        cb.addItemListener(this);
        bt.setComponent(cb);
      }

      java.awt.Color colorMustInput = null;
      // ���ñ��������ɫ
      for (int i = 0; i < m_bd.getHeadShowItems().length; i++) {
        bt = m_bd.getHeadShowItems()[i];
        if (bt.isNull()
            && bt.getForeground() == nc.ui.bill.tools.ColorConstants.COLOR_DEFAULT) {
          bt.getCaptionLabel().setILabelType(1);
          if (colorMustInput == null) {
            colorMustInput = bt.getCaptionLabel().getForeground();
          }
        }
      }
      for (int i = 0; i < m_bd.getTailShowItems().length; i++) {
        bt = m_bd.getTailShowItems()[i];
        if (bt.isNull()
            && bt.getForeground() == nc.ui.bill.tools.ColorConstants.COLOR_DEFAULT) {
          // �Ǳ���
          bt.getCaptionLabel().setILabelType(5);
          if (colorMustInput == null) {
            colorMustInput = bt.getCaptionLabel().getForeground();
          }
        }
      }
      // �������ݾ��ȼ����ֵ�����Ϊ14λ
      // ����
      bt = m_bd.getBodyItem("nnumber");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[0]);
      }
      // ����
      bt = m_bd.getBodyItem("nprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // ���
      bt = m_bd.getBodyItem("nmoney");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // �ƻ�����
      bt = m_bd.getBodyItem("nplanedprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // ����ǰ�ƻ�����
      bt = m_bd.getBodyItem("noriginalprice");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[1]);
      }
      // �ƻ����
      bt = m_bd.getBodyItem("nplanedmny");
      if (bt != null) {
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // ����������
      bt = m_bd.getBodyItem("nassistnum");
      if (bt != null) {
        // �������ݾ���
        ((UIRefPane) bt.getComponent()).setMaxValue(m_dMaxValue);
        bt.setDecimalDigits(m_iPeci[4]);
      }
      // ������
      bt = m_bd.getBodyItem("nchangerate");
      if (bt != null) {
        // �������ݾ���
        bt.setDecimalDigits(m_iPeci[5]);
      }
      //�����������
      bt = m_bd.getBodyItem("nreasonalwastnum");
      if (bt != null) {
        // �������ݾ���
        bt.setDecimalDigits(m_iPeci[0]);
      }
      //������ĵ���
      bt = m_bd.getBodyItem("nreasonalwastprice");
      if (bt != null) {
        // �������ݾ���
        bt.setDecimalDigits(m_iPeci[1]);
      }
      //������Ľ��
      bt = m_bd.getBodyItem("nreasonalwastmny");
      if (bt != null) {
        // �������ݾ���
        bt.setDecimalDigits(m_iPeci[2]);
      }
      // ���޸ĺ��ģ���������ûؽ���
      getBillCardPanel().setBillData(m_bd);
      // �������ɫ��
      new InvAttrCellRenderer().setFreeItemRenderer(getBillCardPanel());
      getBillCardPanel().addEditListener(this);
      // ���ø����ú�����ʾ
      getBillCardPanel().getBodyPanel().setShowRed(true);
      // ��ʾ�ϼ���
      getBillCardPanel().setTatolRowShow(true);
      // ������Ϊ�����ñ�����������ɫ
      for (int i = 0; i < m_bd.getBodyShowItems().length; i++) {
        bt = m_bd.getBodyShowItems()[i];
        if (bt.isNull()
            && bt.getForeground() == nc.ui.bill.tools.ColorConstants.COLOR_DEFAULT) {
          // �����
          String sKey = bt.getKey();
          int iColumnIndex = getBillCardPanel().getBillModel().getBodyColByKey(
              sKey);
          if (iColumnIndex != -1) {
            // ���ñ༭��
            BillRender br = new BillRender(colorMustInput);
            getBillCardPanel().getBillTable().getColumnModel().getColumn(i)
                .setHeaderRenderer(br);
          }
        }
      }
      // ���ò��ɱ༭
      getBillCardPanel().setEnabled(false);
      // ���ӱ�ͷ�ı༭����
      getBillCardPanel().addBillEditListenerHeadTail(this);
      getBillCardPanel().setAutoExecHeadEditFormula(true);

      // ����������
      // getBillCardPanel().getBodyPanel().addTableSortListener();
      // getBillCardPanel().getBillModel().setRowSort(true);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000102")/* @res "���뵥�ݱ���ģ�����ݳ���" */);
    }
  }

  /**
   * ��������:���뵥��ģ�� <p/> ����: String sTitle ----- ���� String sBillTypeCode ----- ��������
   * <p/> ����ֵ: <p/> �쳣:
   */
  protected void loadListTemplet(String sTitle, String sBillTypeCode) {
    m_bdList = getBillListPanel().loadListTemplet(sTitle, sBillTypeCode,
        m_iPeci, m_voHeaddef, m_voBodydef);
    // ���Ӽ���
    getBillListPanel().addEditListener(this);
    getBillListPanel().addMouseListener(this);
    getBillListPanel().addHeadEditListener(new BillHeadListener());
    getBillListPanel().addBodyEditListener(new BillBodyListener());
    getBillListPanel().getHeadTable().setSelectionMode(
        ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    getBillListPanel().getChildListPanel().setTatolRowShow(true);
  }

  /**
   * ��������:���뵥��ģ�� <p/> ����: String sTitle ----- ���� String sBillTypeCode ----- ��������
   * <p/> ����ֵ: <p/> �쳣:
   */
  protected void loadTemplet(String sTitle, String sBillTypeCode) {
    // ���ɽű��ļ�
    try {
      // �������ģ��
      loadCardTemplet(sTitle, sBillTypeCode);
      // �����б�ģ��
      loadListTemplet(sTitle, sBillTypeCode);
      // getBillListPanel().getHeadBillModel().addSortListener(this);
      getBillListPanel().getHeadBillModel().addSortRelaObjectListener2(this);
      // ���ñ���ѡ��仯������Ϊ����˰�Ƿ�ת������
      getBillCardPanel().getBillTable().getSelectionModel()
          .addListSelectionListener(this);
      // ���ñ���ģ������ʼʱ���ɱ༭
      getBillCardPanel().setEnabled(false);
      m_iStatus = INIT_STATUS;
      setBtnsForStatus(m_iStatus);// ��ʾ����;�����б�
      // zlq change begin
      // m_bIsChangeEvent = true;//�ڶ��崦���ó�ֵ
      // zlq change end
      // ���ҵ������
      /*
       * m_sFQSK �����һ���Զ��ŷָ����ַ�����ÿ���ַ�������һ�������տ����͵�ҵ������
       * ����ʹ��ʱ��indexof��������ĳ���ַ����Ƿ���m_sFQSK��
       */
      // String[] sBizs = new String[2];
      // sBizs[0] = ConstVO.m_sBizFQSK;//�����տ�
      // sBizs[1] = ConstVO.m_sBizWTDX;//ί�д���
      // java.util.Hashtable ht = CommonDataBO_Client.getBizTypeIDs(m_sCorpID,
      // sBizs);
      // m_sFQSK = (String) ht.get(ConstVO.m_sBizFQSK);
      // m_sWTDX = (String) ht.get(ConstVO.m_sBizWTDX);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000103")/* @res "��ʼ�����ݳ���" */
          + e.getMessage());
    }
  }

  /**
   * ��������:�����Ӱ�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void onButtonAddManualClicked() {
    try {
      // �Ƿ��ѹ���
      if (!accountIsOpen())
        return;
      // �ж��Ƿ�����ӣ�����ĩ���ʲ�������
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }
      // ��չ̶��ʲ���Ϣ
      // clearFAData();
      addBill();
      if (m_sBillType.equals(ConstVO.m_sBillRKTZD)) {
        // ������������ʹ�ɵ������ݷ�¼���ɱ༭
        getBillCardPanel().getBodyItem("cadjustbillitemid").setEdit(false);
        getBillCardPanel().getBodyItem("cadjustbillitemid").setEnabled(false);
      }
      if ((m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        if (!m_bAllowDefinePriceByUser) {
          // ���ⵥ�������Զ��嵥�ۣ����ڳ����ݱ������뵥��
          getBillCardPanel().getBodyItem("nprice").setEnabled(false);
          getBillCardPanel().getBodyItem("nmoney").setEnabled(false);
        }
        else {
          getBillCardPanel().getBodyItem("nprice").setEnabled(true);
          getBillCardPanel().getBodyItem("nmoney").setEnabled(true);
        }
      }
      else {
        getBillCardPanel().getBodyItem("nprice").setEnabled(true);
        getBillCardPanel().getBodyItem("nmoney").setEnabled(true);
      }
      m_iStatus = ADD_STATUS;
      setBtnsForStatus(m_iStatus);
    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  /**
   * ��������:�����Ӱ�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void onButtonAddQueryClicked() {
    try {
      if (!m_bIsPOStart) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "20143010", "UPP20143010-000104")/*
                                               * @res
                                               * "�˹�����Ҫ��ѯ�ɹ�ϵͳ���ݣ��ɹ�ϵͳδ���ã�����ʹ�ô˹���"
                                               */);
        return;
      }
      // �ж��Ƿ�����ӣ�����ĩ���ʲ�������
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }

      // ��չ̶��ʲ���Ϣ
      // clearFAData();//20050525 �رչ̶��ʲ��ʹ���ӿ�

      // ��ʾ�ƻ��۵���ѡ���ѯ�Ի���
      getQueryPlannedPriceDlg().showModal();

      if (getQueryPlannedPriceDlg().isCloseOK()) {

        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000052")/* @res "���ڲ�ѯ���ݣ����Ժ�" */);

        // ��ѯ
        addQuery();
      }

    }
    catch (Exception e) {
      e.printStackTrace();
      showErrorMessage(e.getMessage());
    }
  }

  /**
   * ��������:�㵥�ݸ��ư�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void onButtonCopyClicked() {
    try {
      // ���˺��ܸ���
      if (!accountIsOpen())
        return;
      // �ж��Ƿ�����ӣ�����ĩ���ʲ�������
      if (isCanAddBill() == false) {
        showErrorMessage(m_sErrorMessage);
        return;
      }
      m_iStatus = ADD_STATUS;
      // ��չ̶��ʲ���Ϣ
      // clearFAData();//20050525 �رչ̶��ʲ��ʹ���ӿ�
      if (getBillListPanel().isVisible()) {
        getBillCardPanel().setBillValueVO(m_voCurBill);
        dispListToCard();
        getBillCardPanel().setEnabled(true);
      }
      setBtnsForStatus(m_iStatus);
      // ���浱ǰ��������
      getBillCardPanel().updateValue();
      // ������ID���
      getBillCardPanel().setHeadItem("cbillid", null);
      // �����ݺ����
      getBillCardPanel().setHeadItem("vbillcode", null);
      // ����ӡ�������
      getBillCardPanel().setTailItem("iprintcount", null);
      // ����޸������
      getBillCardPanel().setTailItem("clastoperatorid", null);
      getBillCardPanel().setTailItem("clastoperatorname", null);
      // ����޸�ʱ�����
      getBillCardPanel().setTailItem("tlastmaketime", null);
      // �Ƶ�ʱ�����
      getBillCardPanel().setTailItem("tmaketime", null);
      // ���ⵥ�Ƿ������Զ��嵥��
      // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
      // ��ǰ�ļƻ��ۿ����Ѿ�����,Ҫ���²�ѯ
      String[] sInvs = new String[m_voCurBill.getChildrenVO().length];
      String[] sBodys = new String[m_voCurBill.getChildrenVO().length];
      for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
        sInvs[i] = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
            .getCinventoryid();
        sBodys[i] = ((BillHeaderVO) m_voCurBill.getParentVO()).getCrdcenterid();
      }
      Object[][] oInvJHJ = CacheTool.getMultiColValue2("bd_produce",
          "pk_calbody", "pk_invmandoc", new String[] {
            "jhj"
          }, sBodys, sInvs);
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        // �����ݷ�¼ID���
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // ������ID���
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbillid");
        // ���ݺ����
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbillcode");
        // �ۼƷ����������
        getBillCardPanel().getBillModel()
            .setValueAt(null, i, "nsettledsendnum");
        // �ۼƻس��������
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "nsettledretractnum");
        // �ۼƻس��������
        getBillCardPanel().getBillModel().setValueAt(null, i, "cbill_bid");
        // ��������
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorid");
        // ���������
        getBillCardPanel().getBillModel().setValueAt(null, i, "iauditsequence");
        // ��������
        getBillCardPanel().getBillModel().setValueAt(null, i, "cauditorname");
        // ����������
        getBillCardPanel().getBillModel().setValueAt(null, i, "dauditdate");
        // ���ⵥID���
        getBillCardPanel().getBillModel().setValueAt(null, i, "csaleadviceid");
        // ���ⵥ��¼ID���
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "ccsaleadviceitemid");
        if (m_bd.getBodyItem("cadjustbillitem") != null) {
          // ��������¼ID���
          getBillCardPanel().getBillModel().setValueAt(null, i,
              "cadjustbillitem");
        }
        if (m_bd.getBodyItem("cadjustbillid") != null) {
          // ��������ID���
          getBillCardPanel().getBillModel()
              .setValueAt(null, i, "cadjustbillid");
        }
        if (m_bd.getBodyItem("cadjustbillitemid") != null) {
          // ��������¼ID���
          getBillCardPanel().getBillModel().setValueAt(null, i,
              "cadjustbillitemid");
        }
        // ƾ֤��Ϣ���
        getBillCardPanel().getBillModel().setValueAt(null, i, "cvoucherid");
        // �ɱ�����
        getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcode");
        if (m_bd.getBodyItem("vbomcodecode") != null) {
          getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcodecode");
          getBillCardPanel().getBillModel().setValueAt(null, i, "vbomcodename");
        }
        // ��̶��ʲ��йص��������
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadeviceid");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicecode");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicename");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfadevicevo");
        getBillCardPanel().getBillModel().setValueAt(null, i, "cfacardid");
        // if (sParam.equals("��") || sParam.equalsIgnoreCase("N"))
        // {/*-=notranslate=-*/
        if (!m_bAllowDefinePriceByUser) {
          // �������
          getBillCardPanel().getBillModel().setValueAt(null, i, "nprice");
          // ������
          getBillCardPanel().getBillModel().setValueAt(null, i, "nmoney");
          // �������˰ת�����
          calcTransIncomeTaxMny(i);
        }
        if (((BillHeaderVO) m_voCurBill.getParentVO()).getCbilltypecode()
            .equals(ConstVO.m_sBillJHJTZD)) {
          // �ƻ��۵�����
          if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getFpricemodeflag()
              .intValue() == ConstVO.JHJ) {
            // ����ǰ�ƻ���
            getBillCardPanel().getBillModel().setValueAt(oInvJHJ[i][0], i,
                "noriginalprice");
          }
        }
        if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getFpricemodeflag()
            .intValue() == ConstVO.JHJ) {
          // �ƻ���
          getBillCardPanel().getBillModel().setValueAt(oInvJHJ[i][0], i,
              "nplanedprice");
        }
        // ���������
        getBillCardPanel().getBillModel().setValueAt(null, i, "ninvarymny");
        // ����������
        getBillCardPanel().getBillModel().setValueAt(null, i, "noutvarymny");
      }
      // �������
      String sDate = ce.getBusinessDate().toString();
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD)
          || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        sDate = getBillCardPanel().getHeadItem("dbilldate").getValue();
      }
      if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
        getBillCardPanel().setHeadItem("dbilldate", sDate);
      }
      // ��ò���Ա
      String sOprator = ce.getUser().getUserName();
      getBillCardPanel().setTailItem("coperatorname", sOprator);
      // �����ݹ�ֵ
      // BillItem bt = getBillCardPanel().getHeadItem("bestimateflag");
      // if (bt != null && m_sBillType.equals(ConstVO.m_sBillCGRKD))
      // {
      // UIComboBox ui = (UIComboBox)bt.getComponent();
      // ui.setSelectedItem("���ݹ�");
      // }
      // ������Ʒֵ
      BillItem bt = getBillCardPanel().getBodyItem("blargessflag");
      if (bt != null) {
        UIComboBox ui = (UIComboBox) bt.getComponent();
        // ui.setSelectedItem("��");
        ui.setSelectedIndex(m_ComboItemsVO.type_no);
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000105")/* @res "���Ƶ���" */);
      getBillCardPanel().setEnabled(true);
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEdit(true);
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey())
            .setEnabled(true);
      }
      BillItem[] bts = getBillCardPanel().getBodyItems();
      for (int j = 0; j < bts.length; j++) {
        boolean bIsEdit = bts[j].isEdit();
        if (bIsEdit)
          getBillCardPanel().getBodyItem(bts[j].getKey()).setEnabled(true);
      }
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }
  }

  /**
   * ��������:�������ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void onButtonListClicked() {
    if (getBillCardPanel().isVisible()) {
    	displayInList();
    }
    else if (getBillListPanel().isVisible()) {
    	displayInCard();
    }
  }

  private void displayInList(){
    // ��ʾ�б�����
    if (m_voCurBill != null && (m_voBills == null || m_voBills.length == 0)) {
      // ��ʾ��ǰ�ĵ���
      BillHeaderVO[] vos = new BillHeaderVO[] {
        (BillHeaderVO) m_voCurBill.getParentVO()
      };
      getBillListPanel().setBodyValueVO(m_voCurBill.getChildrenVO());
      getBillListPanel().setHeaderValueVO(vos);
      // �����б����������е�ֵΪ��Ӧ�����ִ�
      setComboBoxInHeadFromVO(m_voCurBill, false, 0);

      String sBillcode = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getVbillcode();
      // ���ò�ѯ�ĵ��ݺ�
      getQueryClientDlg().setDefaultValue("v.vbillcode", "", sBillcode);
      // ��ʽ���÷���,ʹ��ʽ����
      // getBillListPanel().getHeadBillModel().execLoadFormula();
      execListPanelHeadFormula();
      execListPanelBodyFormula();
      m_voBills = new BillVO[1];
      m_voBills[0] = m_voCurBill;
      m_iCurBillPrt = 0;
    }
    getBillListPanel().getBodyTable().clearSelection();
    if (getBillListPanel().getBodyTable().getRowCount() > 0) {
      getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
    }
    m_iStatus = LIST_STATUS;
    setBtnsForStatus(m_iStatus);
//    buttonTree.getButton(IAButtonConst.BTN_SWITCH).setName(NCLangRes.getInstance().getStrByID("common",
//        "UCH021"/* ��Ƭ��ʾ */));
    setButtons(m_aryButtonGroupList);
    showHintMessage(NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000106")/* @res "��ʾ�б�����" */);
  
  }
  
  private void displayInCard(){
    if (m_voCurBill != null
        && getBillListPanel().getHeadTable().getSelectedRow() == -1) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "��ѡ�񵥾�" */);
      return;
    }
    m_iStatus = CARD_STATUS;
    if (m_voCurBill != null) {
      String sOtherCorpID = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCothercorpid();
      String sOutCorpID = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCoutcorpid();

      BillItem bt = getBillCardPanel().getHeadItem("cothercalbodyid");
      if (bt != null) {
        UIRefPane uf = (UIRefPane) bt.getComponent();
        uf.setPk_corp(sOtherCorpID);
        uf.getRefModel().setPk_corp(sOtherCorpID);
      }

      bt = getBillCardPanel().getHeadItem("coutcalbodyid");
      if (bt != null) {
        UIRefPane uf = (UIRefPane) bt.getComponent();
        uf.setPk_corp(sOutCorpID);
        uf.getRefModel().setPk_corp(sOutCorpID);
      }

      getBillCardPanel().setBillValueVO(m_voCurBill);

      // ������Դ��������
      bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        UIComboBox uibox = (UIComboBox) bt.getComponent();
        if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// ConstVO.m_sBillXSFPName);//���۷�Ʊ
        }
        else if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//���۳��ⵥ
        }
        else {
          uibox.setSelectedIndex(-1);// tem("��");
        }
      }
      // �б��������Ĵ���
      dispListToCard();
    }
    else {
      getBillCardPanel().getBillData().clearViewData();
    }
    setBtnsForStatus(m_iStatus);
    // ���浱ǰ��������
    getBillCardPanel().updateValue();
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000107")/* @res "��ʾ��������" */);
  
  }
  /**
   * ��������:���޸İ�ť���� <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  protected void onButtonUpdateClicked() {
    // �ж��Ƿ�����޸��������Ƶĵ���
    if (canAlterBillMadeByOthers(m_voCurBill) == false) {
      return;
    }
    if (!accountIsOpen())
      return;
    BillHeaderVO header = (BillHeaderVO ) m_voCurBill.getParentVO();
    BillItemVO[] items = (BillItemVO[]) m_voCurBill.getChildrenVO();
    //�ǲ��ϳ��ⵥ�����Ǽ����ϵ���
    if( header.getCbilltypecode().equals( ConstVO.m_sBillCLCKD) && 
        header.getBwithdrawalflag().booleanValue() ){
      for( int i =0;i< items.length;i++){
        if( items[i].getNnumber().doubleValue() > 0 ){
          String message = nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000294");
          /* @res "�����������¼����ϵ����½����ɵģ������޸�" */
          this.showErrorMessage( message );
          this.showHintMessage( message );
          return;
        }
      }
    }
    
    
    if (getBillListPanel().isVisible()) {
      getBillCardPanel().setBillValueVO(m_voCurBill);
      // ������Դ��������
      BillItem bt = getBillCardPanel().getHeadItem("cbillsource");
      if (bt != null
          && (m_sBillType.equals(ConstVO.m_sBillXSCBJZD) || m_sBillType
              .equals(ConstVO.m_sBillQCXSCBJZD))) {
        BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[0];
        String sSourceBillType = btvo.getCsourcebilltypecode();
        UIComboBox uibox = (UIComboBox) bt.getComponent();
        if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSFP)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_salebill);// uibox.setSelectedItem(ConstVO.m_sBillXSFPName);//���۷�Ʊ
        }
        else if (sSourceBillType != null
            && sSourceBillType.equals(ConstVO.m_sBillXSCKD)) {
          uibox.setSelectedIndex(m_ComboItemsVO.type_saleoutlist);// ConstVO.m_sBillXSCKDName);//���۳��ⵥ
        }
        else {
          uibox.setSelectedIndex(-1);// tem("��");
        }
      }
      // �б��������Ĵ���
      dispListToCard();
    }
    m_iStatus = UPDATE_STATUS;
    setBtnsForStatus(m_iStatus);
    // ���ý���ɱ༭
    getBillCardPanel().setEnabled(true);
    // ���浱ǰ��������
    getBillCardPanel().updateValue();
    BillItem[] bts = getBillCardPanel().getBodyItems();
    // ��¼�༭״̬
    m_bBodyEditFlags = new boolean[bts.length];
    for (int i = 0; i < bts.length; i++) {
      m_bBodyEditFlags[i] = bts[i].isEdit();
    }
    // ���޸�״̬��δ��˵ķ�¼�����޸ģ�ɾ�У�����˵ķ�¼�����޸ġ�ɾ��
    boolean bIsAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getBauditedflag().booleanValue();
    // ��¼������ʵʱƾ֤�Ĳ����޸�ɾ��
    boolean bIsRtVouch = false;
    items = (BillItemVO[]) m_voCurBill.getChildrenVO();
    int len = items != null ? items.length : 0;
    for (int i = 0; i < len; i++) {
      if (items[i].getBrtvouchflag() != null
          && items[i].getBrtvouchflag().booleanValue()) {
        bIsRtVouch = true;
        break;
      }
    }
    if (bIsAudit || bIsRtVouch) {
      // ���з�¼��˻�����ʵʱƾ֤��
      // �������С�ɾ�С������С�ճ����
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      setBtnsForBilltypes(m_aryButtonGroupCard);
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
    }
    else {
      // ���ݺš����ڡ������֯���ֿ⡢ҵ�����Ͳ����޸�
      getBillCardPanel().getHeadItem("vbillcode").setEdit(false);
      getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);
      getBillCardPanel().getHeadItem("dbilldate").setEdit(false);
      getBillCardPanel().getHeadItem("dbilldate").setEnabled(false);
      getBillCardPanel().getHeadItem("crdcenterid").setEdit(false);
      getBillCardPanel().getHeadItem("crdcenterid").setEnabled(false);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEdit(false);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEnabled(false);
      getBillCardPanel().getHeadItem("cwarehouseid").setEdit(false);
      getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(false);
      if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)
          || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
        getBillCardPanel().getHeadItem("cbiztypeid").setEdit(false);
        getBillCardPanel().getHeadItem("cbiztypeid").setEnabled(false);
        if (getBillCardPanel().getHeadItem("cbillsource") != null) {
          getBillCardPanel().getHeadItem("cbillsource").setEdit(false);
          getBillCardPanel().getHeadItem("cbillsource").setEnabled(false);
        }
      }
      // ��־�����޸�
      if (getBillCardPanel().getHeadItem("fdispatchflag") != null) {
        getBillCardPanel().getHeadItem("fdispatchflag").setEdit(false);
        getBillCardPanel().getHeadItem("fdispatchflag").setEnabled(false);
      }
      if (getBillCardPanel().getHeadItem("bauditedflag") != null) {
        getBillCardPanel().getHeadItem("bauditedflag").setEdit(false);
        getBillCardPanel().getHeadItem("bauditedflag").setEnabled(false);
      }
      if (getBillCardPanel().getHeadItem("bdisableflag") != null) {
        getBillCardPanel().getHeadItem("bdisableflag").setEdit(false);
        getBillCardPanel().getHeadItem("bdisableflag").setEnabled(false);
      }
      if (getBillCardPanel().getHeadItem("bestimateflag") != null) {
        getBillCardPanel().getHeadItem("bestimateflag").setEdit(false);
        getBillCardPanel().getHeadItem("bestimateflag").setEnabled(false);
      }
      if (getBillCardPanel().getHeadItem("bwithdrawalflag") != null
          && m_sBillType.equals(ConstVO.m_sBillCLCKD) == false) {
        // ���ǲ��ϳ��ⵥ���Ƿ�����ϲ��ɱ༭
        getBillCardPanel().getHeadItem("bwithdrawalflag").setEdit(false);
        getBillCardPanel().getHeadItem("bwithdrawalflag").setEnabled(false);
      }
      // //ҵ�����ڲ����޸�
      // getBillCardPanel().getBodyItem("dbizdate").setEdit(false);
      // getBillCardPanel().getBodyItem("dbizdate").setEnabled(false);
    }
    String sSource = "";
    if (m_voCurBill != null && m_voCurBill.getParentVO() != null) {
      sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getCsourcemodulename();
    }
    // �����Ҽ��˵����
    if ((m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD))
        || (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillCGRKD))
        || (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillRKTZD)
            && sSource != null && (sSource.equals(ConstVO.m_sModulePO) || sSource
            .equals(ConstVO.m_sModuleSC)))
        || (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillCLCKD)
            && sSource != null && (sSource.equals(ConstVO.m_sModulePO) || sSource
            .equals(ConstVO.m_sModuleSC)))
        || (m_bIsSCStart && m_sBillType.equals(ConstVO.m_sBillWWJGSHD))
        || (m_bIsICStart && m_bIsAdjustBill == false
            && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false && m_sBillType
            .equals(ConstVO.m_sBillQCXSCBJZD) == false) || (bIsAudit)) {
      getBillCardPanel().setBodyMenuShow(false); // 2003-11-06.1743
    }
    else {
      getBillCardPanel().setBodyMenuShow(true); // 2003-11-06.1743
    }
    // ��������ʹ�����
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      // �������ã����۳ɱ���ת��ֻ���޸ĵ��ۡ����
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("nmoney") == false
            && btItemDatas[i].getKey().equals("nprice") == false
            && btItemDatas[i].getKey().equals("vbatch") == false
            && btItemDatas[i].getKey().equals("cadjustbillitem") == false
            && btItemDatas[i].getKey().equals("cfadevicecode") == false
            && btItemDatas[i].getKey().equals("blargessflag") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey())
              .setEdit(false);
        }
      }
    }
    else if (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillCGRKD)) {
      // �ɹ����ã��ɹ���ⵥ�������޸�
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("vbatch") == false
            && btItemDatas[i].getKey().equals("cadjustbillitem") == false
            && btItemDatas[i].getKey().equals("cfadevicecode") == false) {

          // 2005.07.06 ��Ʒ�����޸�
          // && btItemDatas[i].getKey().equals("blargessflag") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
    }
    else if (m_bIsPOStart
        && m_sBillType.equals(ConstVO.m_sBillRKTZD)
        && (sSource != null && sSource.equals(ConstVO.m_sModulePO) || sSource
            .equals(ConstVO.m_sModuleSC))) {
      // �ɹ����ã���������
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("vbatch") == false
            && btItemDatas[i].getKey().equals("blargessflag") == false
            && btItemDatas[i].getKey().equals("vfree0") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
    }
    else if (m_bIsPOStart
        && m_sBillType.equals(ConstVO.m_sBillCLCKD)
        && (sSource != null && (sSource.equals(ConstVO.m_sModulePO) || sSource
            .equals(ConstVO.m_sModuleSC)))) {
      // �ɹ����ã��ǲ��ϳ���(VMI)

      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("vbatch") == false
            && btItemDatas[i].getKey().equals("cadjustbillitem") == false
            && btItemDatas[i].getKey().equals("cfadevicecode") == false
            && btItemDatas[i].getKey().equals("blargessflag") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
    }
    else if (m_bIsSCStart && m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
      // ί��ӹ����ã�ί��ӹ��ջ����������޸�
      // �����޸ı�ͷ����
      BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
      for (int i = 0; i < btHeadDatas.length; i++) {
        getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
            false);
      }
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (btItemDatas[i].getKey().equals("cadjustbillitem") == false) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              false);
        }
      }
    }
    else if (m_bIsICStart && m_bIsAdjustBill == false
        && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
        && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
      UFBoolean bIsWithdraw = ((BillHeaderVO) m_voCurBill.getParentVO())
          .getBwithdrawalflag();
      // �����������ճ�����ֻ���޸ĵ��ۡ����
      if (m_sBillType.equals(ConstVO.m_sBillCLCKD) == false
          || bIsWithdraw.booleanValue() == false) {
        // ���ǲ��ϳ��ⵥ���Ǽ�����
        // �����޸ı�ͷ����
        BillItem[] btHeadDatas = getBillCardPanel().getHeadItems();
        for (int i = 0; i < btHeadDatas.length; i++) {
          getBillCardPanel().getHeadItem(btHeadDatas[i].getKey())
              .setEdit(false);
          getBillCardPanel().getHeadItem(btHeadDatas[i].getKey()).setEnabled(
              false);
        }
        // �����޸Ĳ��ֱ�������
        BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
        for (int i = 0; i < btItemDatas.length; i++) {
          if (btItemDatas[i].getKey().equals("nmoney") == false
              && btItemDatas[i].getKey().equals("nprice") == false
              && btItemDatas[i].getKey().equals("cadjustbillitem") == false
              && btItemDatas[i].getKey().equals("cfadevicecode") == false
              && btItemDatas[i].getKey().equals("blargessflag") == false
              && btItemDatas[i].getKey().equals("vdef1") == false
              && btItemDatas[i].getKey().equals("vdef2") == false
              && btItemDatas[i].getKey().equals("vdef3") == false
              && btItemDatas[i].getKey().equals("vdef4") == false
              && btItemDatas[i].getKey().equals("vdef5") == false
              && btItemDatas[i].getKey().equals("vdef6") == false
              && btItemDatas[i].getKey().equals("vdef7") == false
              && btItemDatas[i].getKey().equals("vdef8") == false
              && btItemDatas[i].getKey().equals("vdef9") == false
              && btItemDatas[i].getKey().equals("vdef10") == false
              && btItemDatas[i].getKey().equals("vdef11") == false
              && btItemDatas[i].getKey().equals("vdef12") == false
              && btItemDatas[i].getKey().equals("vdef13") == false
              && btItemDatas[i].getKey().equals("vdef14") == false
              && btItemDatas[i].getKey().equals("vdef15") == false
              && btItemDatas[i].getKey().equals("vdef16") == false
              && btItemDatas[i].getKey().equals("vdef17") == false
              && btItemDatas[i].getKey().equals("vdef18") == false
              && btItemDatas[i].getKey().equals("vdef19") == false
              && btItemDatas[i].getKey().equals("vdef20") == false) {
            getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
                false);
            getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEdit(
                false);
          }
        }
      }
      else {
        // ���ϳ��ⵥ��������
        if (getBillCardPanel().getHeadItem("bwithdrawalflag") != null) {
          // �ǲ��ϳ��ⵥ���Ƿ�����ϲ��ɱ༭
          getBillCardPanel().getHeadItem("bwithdrawalflag").setEdit(false);
          getBillCardPanel().getHeadItem("bwithdrawalflag").setEnabled(false);
        }
      }
    }
    // ����ǳ��ⵥ�����޸��Ƿ����˰ת����־ zlq add 20050330
    if (m_bIsOutBill) {
    	String[] headitems = new String[]{"btransferincometax_h"};
    	for( int i = 0; i < headitems.length; i++){
    		getBillCardPanel().getHeadItem(headitems[i]).setEdit(true);
    		getBillCardPanel().getHeadItem(headitems[i]).setEnabled(true);
    	}
    	String[] bodyitems = new String[]{"btransferincometax",
    			"nincometax","nexpaybacktax"};
    	for( int i = 0; i < bodyitems.length; i++){
    		getBillCardPanel().getBodyItem(bodyitems[i]).setEdit(true);
    		getBillCardPanel().getBodyItem(bodyitems[i]).setEnabled(true);
    	}
//    	
//      getBillCardPanel().getHeadItem("btransferincometax_h").setEdit(true);
//      getBillCardPanel().getHeadItem("btransferincometax_h").setEnabled(true);
//      getBillCardPanel().getBodyItem("btransferincometax").setEdit(true);
//      getBillCardPanel().getBodyItem("btransferincometax").setEnabled(true);
//      getBillCardPanel().getBodyItem("nincometax").setEdit(true);
//      getBillCardPanel().getBodyItem("nincometax").setEnabled(true);
//      getBillCardPanel().getBodyItem("nexpaybacktax").setEdit(true);
//      getBillCardPanel().getBodyItem("nexpaybacktax").setEnabled(true);
    }
    // ��������˿����֯�����ù�������
    String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
        .getValue();
    if (sRDID != null && sRDID.trim().length() != 0) {
      // ����û�����ĵ��ݺ�
      // ���òֿ���յ�����
      BillItem bt = m_bd.getHeadItem("cwarehouseid");
      if (bt != null) {
        // ���òֿ���յ�����
        String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
            + "'";
        // �Ƿ��Ƿ�Ʒ��
        sWhere = sWhere + "and gubflag = 'N'";
        // û���ݷ�
        sWhere = sWhere + "and sealflag = 'N'";
        ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
      }
      bt = m_bd.getBodyItem("cinventorycode");
      if (bt != null) {
        String sAddString = m_sOldInvCondition;
        sAddString = sAddString + " and bd_produce.pk_calbody='" + sRDID + "'";
        if (m_bIsPlanedPriceBill) {
          // �Ǽƻ��۵�����������ļƼ۷�ʽֻ���Ǽƻ���
          sAddString = sAddString + " and bd_produce.pricemethod = "
              + ConstVO.JHJ;
        }
        m_refpaneInv.setWhereString(sAddString);
        m_refpaneInvBack.setWhereString(sAddString);
      }
    }
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000108")/* @res "�޸ļ�¼" */);
  }

  /**
   * ��������:��ʾ������� <p/> ����: int iStatus ----- ��ǰ��� <p/> ����ֵ: <p/> �쳣:
   */
  protected void setBtnsForStatus(int iStatus) {
    if (iStatus == INIT_STATUS) {
      getBillCardPanel().setVisible(true);
      getBillListPanel().setVisible(false);
      // ���ð�ť״̬
      btnCtrl.set(true, IABtnConst.BTN_ADD);
      btnCtrl.set(true,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(true,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_IMPORT_BILL);
      //add by yhj 2014-04-31
      btnCtrl.set(false, IABtnConst.BTN_AUTO_PRICE);
      //end
      //add by zy 2019-09-05
      btnCtrl.set(true, IABtnConst.BTN_PASS_BIAOCAI);
      //end
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false,IABtnConst.BTN_BILL_CANCEL);
      btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_SAVE);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_REFRESH);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_LOCATE);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
      btnCtrl.set(false,IABtnConst.BTN_SWITCH);
      btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      btnCtrl.set(false,IABtnConst.BTN_PRINT);
      btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
      btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
      btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      btnCtrl.set(false,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      setBtnsForBilltypes(m_aryButtonGroupCard);
    }
    else if (iStatus == CARD_STATUS) {
      getBillCardPanel().setVisible(true);
      getBillListPanel().setVisible(false);

      buttonTree.getButton(IABtnConst.BTN_SWITCH).
      setName(NCLangRes.getInstance().getStrByID("common","UCH022"/* �б���ʾ */));
      
      // ���ð�ť״̬
      btnCtrl.set(true,IABtnConst.BTN_ADD);
      btnCtrl.set(true,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(true,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_SWITCH);
      btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      btnCtrl.set(true,IABtnConst.BTN_IMPORT_BILL);
      //add by yhj 2014-03-31
      btnCtrl.set(false, IABtnConst.BTN_AUTO_PRICE);
      //end
      //add by zy 2019-09-05
      btnCtrl.set(true, IABtnConst.BTN_PASS_BIAOCAI);
      //end
      btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
      btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(false, IABtnConst.BTN_BILL_CANCEL);
      btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_SAVE);
      btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(false,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);

      // ���������ť״̬
      if(getQueryClientDlg() != null){
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_REFRESH);
      }else{
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_REFRESH);
      }
      if (m_voBills != null && m_voBills.length != 0) {
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_LOCATE);
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_TOP);
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_BOTTOM);
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_NEXT);
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_PREVIOUS);
        if (m_iCurBillPrt == 0) {
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
        }
        else if (m_iCurBillPrt == m_voBills.length - 1) {
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
        }
        else if (m_iCurBillPrt < 0 || m_iCurBillPrt >= m_voBills.length) {
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
          btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
        }
      }
      else {
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_LOCATE);
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
      }
      if (m_voCurBill != null) {
        boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getBauditedflag().booleanValue();
        if (bHasAudit == false) {
          // ��δ���
          btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
        }
        else {
          btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        }
        boolean bAllHasAudit = true;
        for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
          String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
              .getCauditorid();
          if (sAuditorID == null || sAuditorID.length() == 0) {
            bAllHasAudit = false;
            break;
          }
        }
        boolean bAllVouchered = true;
        for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
          if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getBrtvouchflag() != null
              && ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .getBrtvouchflag().booleanValue() == false) {
            bAllVouchered = false;
            break;
          }
        }
        if (bAllHasAudit || bAllVouchered) {
          // �������
          btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        }
        else {
          btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
        }
        btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
        btnCtrl.set(true,IABtnConst.BTN_PRINT_PRINT);
        btnCtrl.set(true,IABtnConst.BTN_PRINT_PREVIEW);
        btnCtrl.set(true,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      }
      else {
        btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
        btnCtrl.set(false,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      }
      setBtnsForBilltypes(m_aryButtonGroupCard);
    }
    else if (iStatus == ADD_STATUS || iStatus == UPDATE_STATUS) {
      getBillCardPanel().setVisible(true);
      getBillListPanel().setVisible(false);
      // ���ð�ť״̬
      btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
      btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
      btnCtrl.set(true,IABtnConst.BTN_LINE_COPY);
      btnCtrl.set(true,IABtnConst.BTN_SAVE);
      btnCtrl.set(true,IABtnConst.BTN_BILL_CANCEL);
      //add by yhj 2014-03-31
      btnCtrl.set(true, IABtnConst.BTN_AUTO_PRICE);
      //end
      //add by zy 2019-09-05
      btnCtrl.set(true, IABtnConst.BTN_PASS_BIAOCAI);
      //end
      btnCtrl.set(false,IABtnConst.BTN_ADD);
      btnCtrl.set(false,IABtnConst.BTN_ADD_MANUAL);
      btnCtrl.set(false,IABtnConst.BTN_ADD_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_REFRESH);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_LOCATE);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_TOP);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_BOTTOM);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_NEXT);
      btnCtrl.set(false,IABtnConst.BTN_BROWSE_PREVIOUS);
      btnCtrl.set(false,IABtnConst.BTN_PRINT);
      btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
      btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
      btnCtrl.set(false,IABtnConst.BTN_QUERY);
      btnCtrl.set(false,IABtnConst.BTN_SWITCH);
      btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
      btnCtrl.set(false,IABtnConst.BTN_IMPORT_BILL);
      btnCtrl.set(false,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      
      if (getBillCardPanel().getBillModel().getRowCount() != 0){
        btnCtrl.set(true,IABtnConst.BTN_LINE_DELETE);
      }
      else{
        btnCtrl.set(false,IABtnConst.BTN_LINE_DELETE);
      }
      if (this.m_bCanPasteLine
          && getBillCardPanel().getBillModel().getRowCount() != 0){
        btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
      }
      else{
        btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
      }

      setBtnsForBilltypes(m_aryButtonGroupCard);
    }
    else if (iStatus == LIST_STATUS) {
      getBillCardPanel().setVisible(false);
      getBillListPanel().setVisible(true);

      buttonTree.getButton(IABtnConst.BTN_SWITCH).setName(NCLangRes.getInstance().getStrByID("common",
      "UCH021"/* ��Ƭ��ʾ */));
 
      // ���ð�ť״̬
      btnCtrl.set(true,IABtnConst.BTN_QUERY);
      btnCtrl.set(true,IABtnConst.BTN_SWITCH);
      btnCtrl.set(true,IABtnConst.BTN_PRINT);
      btnCtrl.set(true,IABtnConst.BTN_PRINT_PRINT);
      btnCtrl.set(true,IABtnConst.BTN_PRINT_PREVIEW);
      btnCtrl.set(true,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      btnCtrl.set(true,IABtnConst.BTN_IMPORT_BILL);
      // ���������ť״̬
      if(getQueryClientDlg() != null){
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_REFRESH);
      }else{
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_REFRESH);
      }
      btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
      if (m_voBills == null || m_voBills.length == 0) {
        btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
        btnCtrl.set(false,IABtnConst.BTN_BROWSE_LOCATE);
        btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT_PRINT);
        btnCtrl.set(false,IABtnConst.BTN_PRINT_PREVIEW);
        btnCtrl.set(false,IABtnConst.BTN_ASSIST_QUERY_RELATED);
      }
      else {
        String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCbilltypecode();
        if (sBilltype.equals(m_sBillType)
            || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))
            || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType
                .equals(ConstVO.m_sBillQCRKD))) {
          // �ǵ�ǰ��������
          boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getBauditedflag().booleanValue();
          if (bHasAudit == false) {
            // ��δ���
            btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
          }
          else {
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          }
          btnCtrl.set(true,IABtnConst.BTN_SWITCH);
          btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
          boolean bAllHasAudit = true;
          for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
            String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                .getCauditorid();
            if (sAuditorID == null || sAuditorID.length() == 0) {
              bAllHasAudit = false;
              break;
            }
          }
          boolean bAllVouchered = true;
          for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
            if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getBrtvouchflag() != null
                && ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                    .getBrtvouchflag().booleanValue() == false) {
              bAllVouchered = false;
              break;
            }
          }
          if (bAllHasAudit || bAllVouchered) {
            // �������
            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
          }
          else {
            btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
          }
        }
        else {
          // ���ǵ�ǰ��������
          btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
          btnCtrl.set(false,IABtnConst.BTN_SWITCH);
          btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
          btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
        }
        if (sBilltype.equals(ConstVO.m_sBillDJTQD) == false) {
          // ������ȡû�мƼ۷�ʽ
          // �жϷ�¼�Ƿ�����
          // ����Ƽ����ֳ��ⵥ�������
          // ����Ƽۺ�����ⵥû��ָ���س嵥�ݷ�¼�������
          int iRDFlag = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getFdispatchflag().intValue();
          //
          String sSourceBillType = ((BillItemVO) m_voCurBill.getChildrenVO()[0])
              .getCsourcebilltypecode();
          String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[0])
              .getCauditorid();
          int iPricemode = ((BillItemVO) m_voCurBill.getChildrenVO()[0])
              .getFpricemodeflag().intValue();
          UFDouble dNumber = ((BillItemVO) m_voCurBill.getChildrenVO()[0])
              .getNnumber();
          if (iPricemode == ConstVO.GBJJ
              && (sAuditorID == null || sAuditorID.trim().length() == 0)
              && dNumber != null) {
            // �Ǹ���Ƽ�δ�ɱ�����,��������
            double ddnum = dNumber.doubleValue();
            if (iRDFlag == 1
                && ddnum > 0
                && (sSourceBillType == null || sSourceBillType
                    .equals(ConstVO.m_sBillXSFP) == false)) {
              // �����ֳ��ⵥ������Դ���Ƿ�Ʊ
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (iRDFlag == 0 && ddnum < 0) {
              // �Ǻ�����ⵥ
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
          }
          else {
            // ����������ָ��
            btnCtrl.set(false,IABtnConst.BTN_AUDIT);
          }
        }
        else {
          // ����������ָ��
          btnCtrl.set(false,IABtnConst.BTN_AUDIT);
        }
        btnCtrl.set(true,IABtnConst.BTN_BROWSE_LOCATE);
        btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
      }

      setBtnsForBilltypes(m_aryButtonGroupList);
    }
  }

  public void actionPerformed(ActionEvent e) {
  }

  /**
   * ��������:�༭�󴥷� <p/> ����: nc.ui.pub.bill.BillEditEvent e ----- �༭�¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sKey = e.getKey();
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    int iRow = e.getRow();
    if (iRowCount <= iRow) {
      iRow = -1;
    }
    try {
      // ���������ҵ�����ͣ������շ����
      if (sKey.equals("cbiztypeid")) {
        BillItem bt = getBillCardPanel().getHeadItem("cbiztypeid");
        UIRefPane pane = (UIRefPane) bt.getComponent();
        Object oBizPK = pane.getRefPK();
        if (oBizPK != null && oBizPK.toString().trim().length() != 0) {
          String sBizTypeID = oBizPK.toString().trim();
          // ����շ����
          Object oRDCLID = pane.getRef().getRefModel().getValue("receipttype");
          if (oRDCLID != null && oRDCLID.toString().trim().length() != 0) {
            BillItem bt2 = getBillCardPanel().getHeadItem("cdispatchid");
            if (bt2 != null) {
              bt2.setValue(oRDCLID);
            }
          }
          // �ж��Ƿ�Ҫ����ʾ
          bt = m_bd.getHeadItem("cbillsource");
          if (bt != null) {
            int iIndex = getUIComboBoxSource().getSelectedIndex();
            if (iIndex != -1 && iIndex == m_ComboItemsVO.type_salebill) {// ���۷�Ʊ
              // �����۷�Ʊ
              // �ж�ҵ������
              boolean bIsFQ = false; // �Ƿ��Ƿ����տ�
              boolean bIsWT = false; // �Ƿ���ί�д���
              if (sBizTypeID.trim().length() != 0) {
                Object oRule = pane.getRef().getRefModel().getValue(
                    "verifyrule");
                if (oRule != null) {
                  if (oRule.toString().equals("W")) {
                    // ��ί�д���
                    bIsWT = true;
                  }
                  else if (oRule.toString().equals("F")) {
                    // �Ƿ����տ�
                    bIsFQ = true;
                  }
                }
              }
              if (bIsFQ) {
                showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000109")/*
                                                       * @res
                                                       * "��ǰ������ԴΪ���۷�Ʊ��ҵ�������Ƿ����տ���ڸ����˵���ѡ����ԴΪ���ⵥ�Ķ�Ӧ���۳ɱ���ת��"
                                                       */);
                btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
                btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
                btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
                btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
              }
              else if (bIsWT) {
                showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000110")/*
                                                       * @res
                                                       * "��ǰ������ԴΪ���۷�Ʊ��ҵ��������ί�д��������ڸ����˵���ѡ����ԴΪ���ⵥ�Ķ�Ӧ���۳ɱ���ת��"
                                                       */);
                btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
                btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
                btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
                btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
              }
              else {
                btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
                btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
                btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
                btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
              }
              setBtnsForBilltypes(m_aryButtonGroupCard);
            }
          }
        }
        else {
          btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
          btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
          setBtnsForBilltypes(m_aryButtonGroupCard);
        }
      }
      // ������벿�ţ�����ҵ��Ա�Ĺ�������
      else if (sKey.equals("cdeptid")) {
        String sDeptID = getBillCardPanel().getHeadItem("cdeptid").getValue();
        // String sUserID =
        // getBillCardPanel().getHeadItem("cemployeeid").getValue();
        if (sDeptID != null && sDeptID.trim().length() != 0) {
          // ����ҵ��Ա���յ�����
          BillItem bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // ����ҵ��Ա���յ�����
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition
                + "and bd_psndoc.pk_deptdoc = '" + sDeptID + "'");
          }
          // ����ҵ��Ա���ǿ�ƹ�����ϵ20050519 zlq
          // if (sUserID != null && sUserID.trim().length() != 0) {
          // ((UIRefPane) bt.getComponent()).setPK(sUserID);
          // if (((UIRefPane) bt.getComponent()).getRefCode() == null) {
          // getBillCardPanel().getHeadItem("cemployeeid").setValue("");
          // }
          // }
        }
        else {
          BillItem bt = m_bd.getHeadItem("cemployeeid");
          if (bt != null) {
            // ����ҵ��Ա���յ�����
            ((UIRefPane) bt.getComponent()).setWhereString(m_sOldUserCondition);
          }
        }
      }
      // �������ҵ��Ա����������
      else if (sKey.equals("cemployeeid")) {
        BillItem bt = m_bd.getHeadItem("cdeptid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
              "cemployeeid").getComponent();
          Object oDept = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
          if (oDept != null && oDept.toString().trim().length() != 0) {
            // ���ò���
            bt.setValue(oDept);
          }
          else {
            bt.setValue("");
          }
        }
      }
      // ������빫˾�����˿����֯
      else if (sKey.equals("coutcorpid")) {
        BillItem bt2 = m_bd.getHeadItem("coutcorpid");
        BillItem bt = m_bd.getHeadItem("coutcalbodyid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) bt.getComponent();
          UIRefPane ref2 = (UIRefPane) bt2.getComponent();
          if (e.getValue() != null) {
            ref.setWhereString(" pk_corp = '" + ref2.getRefPK() + "'");
          }
          else {
            ref.setWhereString("");
          }
        }
      }
      // ������빫˾�����˿����֯
      else if (sKey.equals("cothercorpid")) {
        BillItem bt = m_bd.getHeadItem("cothercalbodyid");
        BillItem bt2 = m_bd.getHeadItem("cothercorpid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) bt.getComponent();
          UIRefPane ref2 = (UIRefPane) bt2.getComponent();
          if (e.getValue() != null) {
            ref.setWhereString(" pk_corp = '" + ref2.getRefPK() + "'");
          }
          else {
            ref.setWhereString("");
          }
        }
      }
      // �������ֿ⣬�����ִ������֯
      else if (sKey.equals("cwarehouseid")) {
        BillItem bt = m_bd.getHeadItem("cstockrdcenterid");
        if (bt != null) {
          UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
              "cwarehouseid").getComponent();
          Object oCalbody = ref.getRefModel().getValue("pk_calbody");
          if (oCalbody != null && oCalbody.toString().trim().length() != 0) {
            // ���òִ������֯
            bt.setValue(oCalbody);
            bt = m_bd.getHeadItem("cwarehouseid");
            if (bt != null) {
              // ���òֿ���յ�����
              String sWhere = m_sOldWareCondition + "and pk_calbody = '"
                  + oCalbody + "'";
              // �Ƿ��Ƿ�Ʒ��
              sWhere = sWhere + "and gubflag = 'N'";
              // û���ݷ�
              sWhere = sWhere + "and sealflag = 'N'";
              ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
            }
          }

        }
      }
      else if (sKey.equals("cwpcode")) {
        String sName = getUIRefPaneWkCenter().getRefName();
        getBillCardPanel().getBillModel().setValueAt(sName, iRow, "cwpname");
        String sPK = getUIRefPaneWkCenter().getRefPK();
        getBillCardPanel().getBillModel().setValueAt(sPK, iRow, "cwp");
      }
      // ��������˵����������ͣ�ʹ���������ݺſɱ༭
      else if (sKey.equals("cadjustbilltype")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          getUIRefPaneAdjustBill().setWhereString("");
          getBillCardPanel().getBodyItem("cadjustbill").setEdit(true);
          getBillCardPanel().getBodyItem("cadjustbill").setEnabled(true);
          // ���ò�ѯ����
          String sWhereString = " and a.cbilltypecode = '"
              + oTemp.toString().trim() + "' ";
          String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
              .getValue();
          String sRDID = getBillCardPanel().getHeadItem("crdcenterid")
              .getValue();
          String sStockRDID = getBillCardPanel()
              .getHeadItem("cstockrdcenterid").getValue();
          // �ж��Ƿ����вֿ⡢�����֯����������������κŲ�������
          if (sWareID != null && sWareID.trim().length() != 0) {
            sWhereString = sWhereString + " and a.cwarehouseid = '" + sWareID
                + "' ";
          }
          if (sRDID != null && sRDID.trim().length() != 0) {
            sWhereString = sWhereString + " and a.crdcenterid = '"
                + sRDID.trim() + "' ";
          }
          if (sStockRDID != null && sStockRDID.trim().length() != 0) {
            sWhereString = sWhereString + " and a.cstockrdcenterid = '"
                + sStockRDID.trim() + "' ";
          }
          getUIRefPaneAdjustBill().setWhereString(sWhereString);
          getBillCardPanel().setBodyValueAt("", iRow, "cadjustbill");
        }
        else {
          getBillCardPanel().setBodyValueAt("", iRow, "cadjustbill");
          getBillCardPanel().getBodyItem("cadjustbill").setEdit(false);
          getBillCardPanel().getBodyItem("cadjustbill").setEnabled(false);
          getBillCardPanel().getBodyItem("cinventorycode").setEdit(true);
          getBillCardPanel().getBodyItem("cinventorycode").setEnabled(true);
          getUIRefPaneAdjustBill().setWhereString("");
          // ���ûس嵥�ݷ�¼
          BillItem bt = getBillCardPanel().getBodyItem("cadjustbillitem");
          if (bt != null && iRow != -1) {
            getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitem");
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          }
        }
      }
      // ��������˵�������ID��ʹ���ݷ�¼�ɱ༭
      else if (sKey.equals("cadjustbill")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          getUIRefPaneAdjustBillItem().setWhereString("");
          getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(true);
          getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(true);
          getBillCardPanel().getBodyItem("cinventorycode").setEdit(false);
          getBillCardPanel().getBodyItem("cinventorycode").setEnabled(false);
          String sPK = getUIRefPaneAdjustBill().getRefPK();
          if (sPK != null) {
            getBillCardPanel().setBodyValueAt(sPK, iRow, "cadjustbillid");
            String sWhereString = " and a.cbillid = '" + sPK + "' ";
            getUIRefPaneAdjustBillItem().setWhereString(sWhereString);
          }
          getBillCardPanel().setBodyValueAt(null, iRow, "cadjustbillitemid");
        }
        else {
          getBillCardPanel().setBodyValueAt(null, iRow, "cadjustbillitemid");
          getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
          getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          getBillCardPanel().getBodyItem("cinventorycode").setEdit(true);
          getBillCardPanel().getBodyItem("cinventorycode").setEnabled(true);
          getUIRefPaneAdjustBillItem().setWhereString("");
          // ���ûس嵥�ݷ�¼
          BillItem bt = getBillCardPanel().getBodyItem("cadjustbillitem");
          if (bt != null && iRow != -1) {
            getBillCardPanel().getBillModel().setValueAt("", iRow, "nnumber");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitem");
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          }
        }
      }
      // �����˸�����
      else if (sKey.equals("castunitname")) {
        // ���ø�������ʶ
        Object oTemp1 = e.getValue();
        BillItem bt = getBillCardPanel().getBodyItem("castunitname");
        if (bt != null) {
          UIRefPane pane = (UIRefPane) bt.getComponent();
          Object oInvPK = getBillCardPanel().getBillModel().getValueAt(iRow,
              "cinventoryid");
          if (oTemp1 != null && oTemp1.toString().trim().length() != 0) {
            Object oMeaPK = pane.getRefPK();
            if (oMeaPK == null || oMeaPK.toString().trim().length() == 0) {
              pane.setBlurValue(oTemp1.toString().trim());
              oMeaPK = pane.getRefPK();
            }
            if (oMeaPK != null && oMeaPK.toString().trim().length() != 0) {
              String sName = pane.getRefName();
              getBillCardPanel().getBillModel().setValueAt(sName, iRow,
                  "castunitname");
              Object oFixedflag = pane.getRefModel().getValue(
                  "bd_convert.fixedflag");
              Object oChangeRate = pane.getRefModel().getValue(
                  "bd_convert.mainmeasrate");
              getBillCardPanel().getBillModel().setValueAt(oMeaPK, iRow,
                  "castunitid");
              getBillCardPanel().getBillModel().setValueAt(oChangeRate, iRow,
                  "nchangerate");
              if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("Y")) {
                getBillCardPanel().getBillModel().setValueAt("", iRow,
                    "nassistnum");
                // �ǹ̶������ʣ��������������� (����������x������=����)
                Object oTemp = getBillCardPanel().getBillModel().getValueAt(
                    iRow, "nnumber");
                if (oTemp != null && oTemp.toString().trim().length() != 0) {
                  UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                  if (oChangeRate != null
                      && oChangeRate.toString().trim().length() != 0) {
                    UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                    UFDouble dAssistNum = dNumber.div(dRate); // (����������=����/������)
                    getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                        iRow, "nassistnum");
                  }
                }
                if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                  // ���̶������ʼ�����
                  m_htInvAndFix.put(oInvPK.toString().trim() + ","
                      + oMeaPK.toString().trim(), "Y");
                }
              }
              else if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("N")) {
                // �����ǹ̶������ʣ����������
                Object oTemp = getBillCardPanel().getBillModel().getValueAt(
                    iRow, "nnumber");
                if (oTemp != null && oTemp.toString().trim().length() != 0) {
                  UFDouble dNumber = new UFDouble(oTemp.toString().trim());
                  UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (����������=����/������)
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
                // getBillCardPanel().getBillModel().setValueAt(null,iRow,"nnumber");
              }
            }
            else {
              // ���������������
              getBillCardPanel().getBillModel().setValueAt(null, iRow,
                  "nchangerate");
              getBillCardPanel().getBillModel().setValueAt(null, iRow,
                  "nassistnum");
              if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
                // �����ǹ̶������ʵ�ɾ��
                m_htInvAndFix.remove(oInvPK.toString().trim());
              }
            }
          }
          else {
            // ���������������
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "castunitid");
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "nchangerate");
            getBillCardPanel().getBillModel().setValueAt(null, iRow,
                "nassistnum");
            if (oInvPK != null && oInvPK.toString().trim().length() != 0) {
              // �����ǹ̶������ʵ�ɾ��
              m_htInvAndFix.remove(oInvPK.toString().trim());
            }
          }
        }
      }
      // 20050622 zlq ���ٶ����κ���������
      // ������κ��Ƿ���ȷ
      // else if (sKey.equals("vbatch")) {
      // Object oPCH = e.getValue();
      // if (oPCH != null) {
      // if (checkChar(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      // "UC000-0002060")/*@res "���κ�"*/, oPCH.toString()) == false) {
      // return;
      // }
      // }
      // }
      // ��������˵�������ID���������
      // �����˼ӹ�Ʒ
      else if (sKey.equals("vbomcodecode")) {
        // ����ӹ�Ʒ�ı䣬����������Ϊ��ֵ
        BillItem bt = getBillCardPanel().getBodyItem("vbomcodecode");
        UIRefPane pane = (UIRefPane) bt.getComponent();
        Object oPK = pane.getRefPK();
        if (oPK != null && oPK.toString().trim().length() != 0) {
          // ��ô����Ϣ
          String sRdfCode = pane.getRefCode();
          String sRefName = pane.getRefName();
          getBillCardPanel().getBillModel().setValueAt(oPK.toString().trim(),
              iRow, "vbomcode");
          getBillCardPanel().getBillModel().setValueAt(sRdfCode, iRow,
              "vbomcodecode");
          getBillCardPanel().getBillModel().setValueAt(sRefName, iRow,
              "vbomcodename");
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vbomcode");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "vbomcodecode");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "vbomcodename");
        }
      }
      // ������
      else if (sKey.equals("nnumber")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dNumber = new UFDouble(oTemp.toString().trim());
          if (dNumber.doubleValue() < 0) {
            // �Ǻ�嵥��ʹ���������ݷ�¼�ɵ���
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(true);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(true);
          }
          else {
            // �������ݣ��õ������ݷ�¼Ϊ��
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitem");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitemid");
            getUIRefPaneAdjustBillItem().setText("");
          }
          BillItem bt = getBillCardPanel().getBodyItem("castunitname");
          if (bt != null) {
            Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
                "cinventoryid");
            // ���ø�������ʶ
            Object oMeaPK = getBillCardPanel().getBillModel().getValueAt(iRow,
                "castunitid");
            if (oInvID != null && oMeaPK != null) {
              // ���������������
              Object oMainMeaName = getBillCardPanel().getBillModel()
                  .getValueAt(iRow, "cinventorymeasname");
              Object oMeaName = getBillCardPanel().getBillModel().getValueAt(
                  iRow, "castunitname");
              Object oFixedflag = "Y";
              Object oChangeRate = new Integer(1);
              if (oMainMeaName.equals(oMeaName) == false) {
                // ȡ�ø������̶������ʱ�־��ȡ�ý���������Ļ�����
                if (oMeaPK != null && oMeaPK.toString().trim().length() != 0) {
                  Object[] oData = ce.getAstUnitInfo(m_sCorpID, oInvID
                      .toString(), oMeaPK.toString());
                  oFixedflag = oData[0];
                  oChangeRate = getBillCardPanel().getBillModel().getValueAt(
                      iRow, "nchangerate");
                }
              }
              if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("Y")) {
                // �ǹ̶������ʣ��޸ĸ���������
                if (oChangeRate != null
                    && oChangeRate.toString().trim().length() != 0) {
                  UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (����������=����/������)
                  dAssistNum = dAssistNum.setScale(m_iPeci[0],
                      UFDouble.ROUND_HALF_UP);
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
              else if (oFixedflag != null
                  && oFixedflag.toString().trim().equals("N")) {
                // ���ǹ̶������ʣ��޸Ļ�����
                Object oAssisNum = getBillCardPanel().getBillModel()
                    .getValueAt(iRow, "nassistnum");
                if (oAssisNum != null
                    && oAssisNum.toString().trim().length() != 0) {//��������ֵ
                  UFDouble dAssisNum = new UFDouble(oAssisNum.toString().trim());
                  UFDouble dRate = dNumber.div(dAssisNum); // ������=����/����������
                  getBillCardPanel().getBillModel().setValueAt(dRate, iRow,
                      "nchangerate");
                }else if (oChangeRate != null
                    && oChangeRate.toString().trim().length() != 0){//������û��ֵ
                	UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
                  UFDouble dAssistNum = dNumber.div(dRate); // (����������=����/������)
                  dAssistNum = dAssistNum.setScale(m_iPeci[0],
                      UFDouble.ROUND_HALF_UP);
                  getBillCardPanel().getBillModel().setValueAt(dAssistNum,
                      iRow, "nassistnum");
                }
              }
            }
          }
          // ������ʽ����
          // ��� �� ���� �� ����
          Object oTempDJ = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nprice");
          if (oTempDJ != null && oTempDJ.toString().trim().length() != 0) {
            UFDouble dPrice = new UFDouble(oTempDJ.toString().trim());
            UFDouble dMny = dPrice.multiply(dNumber);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("������������ֵ" + m_dMaxValue + "�������");
              String[] value = new String[] {
                String.valueOf(m_dMaxValue)
              };
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000164", null, value));
              getBillCardPanel().getBillModel()
                  .setValueAt(null, iRow, "nmoney");
              // �������˰ת�����
              calcTransIncomeTaxMny(e.getRow());
            }
            else {
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
              // �������˰ת�����
              calcTransIncomeTaxMny(e.getRow());
            }
          }
          else {
            // ���� �� ��� / ����
            Object oTempJE = getBillCardPanel().getBillModel().getValueAt(iRow,
                "nmoney");
            if (oTempJE != null && oTempJE.toString().trim().length() != 0) {
              UFDouble dMny = new UFDouble(oTempJE.toString().trim());
              UFDouble dPrice = dMny.div(dNumber);
              dPrice = dPrice.setScale(m_iPeci[1], UFDouble.ROUND_HALF_UP);
              getBillCardPanel().getBillModel().setValueAt(dPrice, iRow,
                  "nprice");
            }
          }
          // �ƻ���� �� �ƻ����� �� ����
          oTempDJ = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nplanedprice");
          if (oTempDJ != null && oTempDJ.toString().trim().length() != 0) {
            UFDouble dPrice = new UFDouble(oTempDJ.toString().trim());
            UFDouble dMny = dPrice.multiply(dNumber);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("������ƻ��������ֵ" + m_dMaxValue + "�������");
              String[] value = new String[] {
                String.valueOf(m_dMaxValue)
              };
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000165", null, value));
              getBillCardPanel().getBillModel().setValueAt(null, iRow,
                  "nplanedmny");
            }
            else {
              getBillCardPanel().getBillModel().setValueAt(dMny, iRow,
                  "nplanedmny");
            }
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nmoney");
          // �������˰ת�����
          calcTransIncomeTaxMny(e.getRow());
          getBillCardPanel().getBillModel()
              .setValueAt(null, iRow, "nplanedmny");
          if (getBillCardPanel().getBodyItem("cadjustbillitem") != null) {
            getBillCardPanel().getBodyItem("cadjustbillitem").setValue(null);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          }
          getUIRefPaneAdjustBillItem().setNumCondition("");
          getUIRefPaneBatch().setNumCondition("");
        }
      }
      // ��������������ʽ
      else if (sKey.equals("nmoney")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dMny = new UFDouble(oTemp.toString().trim());
          // �������˰ת�����
          calcTransIncomeTaxMny(e.getRow());
          // ������ʽ����
          // ���� �� ��� / ����
          Object oTempSL = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nnumber");
          if (oTempSL != null && oTempSL.toString().trim().length() != 0) {
            UFDouble dNumber = new UFDouble(oTempSL.toString().trim());
            if (dNumber.doubleValue() == 0)
              getBillCardPanel().getBillModel()
                  .setValueAt(null, iRow, "nprice");
            else {
              UFDouble dPrice = dMny.div(dNumber);
              dPrice = dPrice.setScale(m_iPeci[1], UFDouble.ROUND_HALF_UP);
              getBillCardPanel().getBillModel().setValueAt(dPrice, iRow,
                  "nprice");
            }
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nprice");
          // �������˰ת�����
          calcTransIncomeTaxMny(e.getRow());
        }
      }
      // ������뵥�ۣ�������ʽ
      else if (sKey.equals("nprice")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dPrice = new UFDouble(oTemp.toString().trim());
          // ������ʽ����
          // ��� �� ���� �� ����
          Object oTempSL = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nnumber");
          if (oTempSL != null && oTempSL.toString().trim().length() != 0) {
            UFDouble dNumber = new UFDouble(oTempSL.toString().trim());
            UFDouble dMny = dNumber.multiply(dPrice);
            dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            if (dMny.doubleValue() > m_dMaxValue) {
              // showErrorMessage("������������ֵ" + m_dMaxValue + "�������");
              String[] value = new String[] {
                String.valueOf(m_dMaxValue)
              };
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000164", null, value));
              getBillCardPanel().getBillModel()
                  .setValueAt(null, iRow, "nmoney");
            }
            else {
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
            }
            // �������˰ת�����
            calcTransIncomeTaxMny(e.getRow());
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nmoney");
          // �������˰ת�����
          calcTransIncomeTaxMny(e.getRow());
        }
      }
      // �������Ŀ��Ϣ��գ�����Ŀ�׶���Ϣ���
      else if (sKey.equals("cprojectcode")) {
        Object oTemp = e.getValue();
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectname");
          getBillCardPanel().getBillModel()
              .setValueAt(null, iRow, "cprojectid");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectphasecode");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectphasename");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectphase");
          getUIRefPaneJobParse().setText("");
        }
        else {
          BillItem bt = getBillCardPanel().getBodyItem("cprojectcode");
          if (bt != null) {
            UIRefPane ufpane = (UIRefPane) bt.getComponent();
            String sName = ufpane.getRefName();
            String sPK = ufpane.getRefPK();
            getBillCardPanel().getBillModel().setValueAt(sName, iRow,
                "cprojectname");
            getBillCardPanel().getBillModel().setValueAt(sPK, iRow,
                "cprojectid");
          }
        }
      }
      else if (sKey.equals("cprojectphasecode")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          BillItem bt = getBillCardPanel().getBodyItem("cprojectphasecode");
          if (bt != null) {
            UIRefPane ufpane = (UIRefPane) bt.getComponent();
            String sName = ufpane.getRefName();
            String sPK = ufpane.getRefPK();
            getBillCardPanel().getBillModel().setValueAt(sName, iRow,
                "cprojectphasename");
            getBillCardPanel().getBillModel().setValueAt(sPK, iRow,
                "cprojectphase");
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectphasename");
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "cprojectphase");
        }
      }
      // ���������ʱ仯
      else if (sKey.equals("nchangerate")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dRate = new UFDouble(oTemp.toString().trim());
          Object oAstNum = getBillCardPanel().getBillModel().getValueAt(iRow,
              "nassistnum");
          // �޸�����������
          if (oAstNum != null && oAstNum.toString().trim().length() != 0) {
            UFDouble dAstNum = new UFDouble(oAstNum.toString().trim());
            UFDouble dBasNumber = dAstNum.multiply(dRate); // (����=����������*������)
            getBillCardPanel().getBillModel().setValueAt(dBasNumber, iRow,
                "nnumber");
            // ��� = ���� * ����
            Object oPrice = getBillCardPanel().getBillModel().getValueAt(iRow,
                "nprice");
            if (oPrice != null && oPrice.toString().trim().length() != 0) {
              UFDouble dPrice = new UFDouble(oPrice.toString().trim());
              UFDouble dMny = dBasNumber.multiply(dPrice);
              dMny = dMny.setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
              // �������˰ת�����
              calcTransIncomeTaxMny(e.getRow());
            }
            // �ƻ���� = ���� * �ƻ�����
            Object oPlanedPrice = getBillCardPanel().getBillModel().getValueAt(
                iRow, "nplanedprice");
            if (oPlanedPrice != null
                && oPlanedPrice.toString().trim().length() != 0) {
              UFDouble dPlanedPrice = new UFDouble(oPlanedPrice.toString()
                  .trim());
              UFDouble dPlanedMny = dBasNumber.multiply(dPlanedPrice);
              dPlanedMny = dPlanedMny.setScale(m_iPeci[2],
                  UFDouble.ROUND_HALF_UP);
              getBillCardPanel().getBillModel().setValueAt(dPlanedMny, iRow,
                  "nplanedmny");
            }
          }
        }
        else {
          getBillCardPanel().getBillModel()
              .setValueAt(null, iRow, "nassistnum");
        }
      }
      // ��������������
      else if (sKey.equals("nassistnum")) {
        Object oTemp = e.getValue();
        if (oTemp != null && oTemp.toString().trim().length() != 0) {
          UFDouble dNumber = new UFDouble(oTemp.toString().trim());
          BillItem bt = getBillCardPanel().getBodyItem("castunitname");
          if (bt != null) {
            Object oChangeRate = getBillCardPanel().getBillModel().getValueAt(
                iRow, "nchangerate");
            // �޸�����������
            if (oChangeRate != null
                && oChangeRate.toString().trim().length() != 0) {
              UFDouble dRate = new UFDouble(oChangeRate.toString().trim());
              UFDouble dBasNumber = dNumber.multiply(dRate); // (����=����������*������)
              getBillCardPanel().getBillModel().setValueAt(dBasNumber, iRow,
                  "nnumber");
              // ��� = ���� * ����
              Object oPrice = getBillCardPanel().getBillModel().getValueAt(
                  iRow, "nprice");
              if (oPrice != null && oPrice.toString().trim().length() != 0) {
                UFDouble dPrice = new UFDouble(oPrice.toString().trim());
                UFDouble dMny = dBasNumber.multiply(dPrice);
                if (dMny.doubleValue() > m_dMaxValue) {
                  // showErrorMessage("������������Χ " + m_dMaxValue + "�������");
                  String[] value = new String[] {
                    String.valueOf(m_dMaxValue)
                  };
                  showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "20143010", "UPP20143010-000164", null, value));
                  getBillCardPanel().getBillModel().setValueAt(null, iRow,
                      "nmoney");
                  // �������˰ת�����
                  calcTransIncomeTaxMny(e.getRow());
                }
                else {
                  getBillCardPanel().getBillModel().setValueAt(dMny, iRow,
                      "nmoney");
                  // �������˰ת�����
                  calcTransIncomeTaxMny(e.getRow());
                }
              }
              else {
                // ���� �� ��� / ����
                Object oTempJE = getBillCardPanel().getBillModel().getValueAt(
                    iRow, "nmoney");
                if (oTempJE != null && oTempJE.toString().trim().length() != 0) {
                  UFDouble dMny = new UFDouble(oTempJE.toString().trim());
                  UFDouble dPrice = dMny.div(dNumber);
                  dPrice = dPrice.setScale(m_iPeci[1], UFDouble.ROUND_HALF_UP);
                  getBillCardPanel().getBillModel().setValueAt(dPrice, iRow,
                      "nprice");
                }
              }
              // �ƻ���� = ���� * �ƻ�����
              Object oPlanedPrice = getBillCardPanel().getBillModel()
                  .getValueAt(iRow, "nplanedprice");
              if (oPlanedPrice != null
                  && oPlanedPrice.toString().trim().length() != 0) {
                UFDouble dPlanedPrice = new UFDouble(oPlanedPrice.toString()
                    .trim());
                UFDouble dPlanedMny = dBasNumber.multiply(dPlanedPrice);
                dPlanedMny = dPlanedMny.setScale(m_iPeci[2],
                    UFDouble.ROUND_HALF_UP);
                getBillCardPanel().getBillModel().setValueAt(dPlanedMny, iRow,
                    "nplanedmny");
              }
            }
          }
        }
        else {
          getBillCardPanel().getBillModel().setValueAt(null, iRow,
              "nchangerate");
        }
      }
      // �����Զ�����
      else if (sKey.startsWith("vdef")) {
        // ��ͷ�Զ�����༭�Ժ󴥷�
        if (e.getPos() == BillItem.HEAD) {
          DefSetTool.afterEditHead(getBillCardPanel().getBillData(), sKey,
              "pk_defdoc"
                  + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                      .length()));
        }
        // �����Զ�����༭�Ժ󴥷�
        else if (e.getPos() == BillItem.BODY) {
          DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), e
              .getRow(), sKey, "pk_defdoc"
              + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                  .length()));
        }
      }
      // ��������˰�Ƿ�ת��
      else if (sKey.equals("btransferincometax")) {
        UFBoolean flag = new UFBoolean(getBillCardPanel().getBillModel()
            .getValueAt(e.getRow(), "btransferincometax").toString());
        // ѡ��
        if (flag.booleanValue()) {
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nincometax", true);
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nexpaybacktax", true);
          // �������д���Ľ���˰��
          Object oInTax = getBillCardPanel().getBillModel().getValueAt(
              e.getRow(), "nincometax");
          if (oInTax == null || oInTax.toString().trim().length() == 0) {
            String sPk = (String) getBillCardPanel().getBillModel().getValueAt(
                e.getRow(), "cinventoryid");
            if (sPk != null) {
              Object[] oInvBasId = (Object[]) CacheTool.getCellValue(
                  "bd_invmandoc", "pk_invmandoc", "pk_invbasdoc", sPk);
              Object[] oInvTaxItems = (Object[]) CacheTool.getCellValue(
                  "bd_invbasdoc", "pk_invbasdoc", "pk_taxitems",
                  oInvBasId == null ? null : (String) oInvBasId[0]);
              Object[] oTaxRatio = (Object[]) CacheTool.getCellValue(
                  "bd_taxitems", "pk_taxitems", "taxratio",
                  oInvTaxItems == null ? null : (String) oInvTaxItems[0]);
              Object oInvIncomeTax = oTaxRatio == null ? null : oTaxRatio[0];
              getBillCardPanel().getBillModel().setValueAt(oInvIncomeTax,
                  e.getRow(), "nincometax");
            }
          }
          // ����ת�����
          calcTransIncomeTaxMny(e.getRow());
          // δѡ��
        }
        else {
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nincometax", false);
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nexpaybacktax", false);
          // ���ת�����
          getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
              "ndrawsummny");
        }
      }
      // ������˰���Ƿ���ڵ��ڳ�����˰��,������ת����� zlq add 20050330
      else if (sKey.equals("nincometax") || sKey.equals("nexpaybacktax")) {
        boolean flag = calcTransIncomeTaxMny(e.getRow());
        if (!flag) {
          getBillCardPanel().getBillModel().setValueAt(e.getOldValue(),
              e.getRow(), sKey);
        }
      }
    }
    catch (Exception ee) {
      showErrorMessage(ee.getMessage());
      ee.printStackTrace();
    }
  }

  /**
   * ������˰���Ƿ���ڵ��ڳ�����˰��,������ת�����
   */
  private boolean calcTransIncomeTaxMny(int row) {
    if (m_bIsOutBill) {
      Object value = getBillCardPanel().getBillModel().getValueAt(row,
          "btransferincometax");
      boolean flag = value == null ? false : (new UFBoolean(value.toString()))
          .booleanValue();
      if (flag) {
        Object oValueEx = getBillCardPanel().getBillModel().getValueAt(row,
            "nexpaybacktax");
        UFDouble dbValueEx = oValueEx == null ? new UFDouble(0) : new UFDouble(
            oValueEx.toString());

        Object oValueIn = getBillCardPanel().getBillModel().getValueAt(row,
            "nincometax");
        UFDouble dbValueIn = oValueIn == null ? new UFDouble(0) : new UFDouble(
            oValueIn.toString());

        Object oValueMny = getBillCardPanel().getBillModel().getValueAt(row,
            "nmoney");
        UFDouble dbValueMny = oValueMny == null ? new UFDouble(0)
            : new UFDouble(oValueMny.toString());

        // ������˰���Ƿ���ڵ��ڳ�����˰��
        if (dbValueEx.doubleValue() > dbValueIn.doubleValue()) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000283")/* "����˰��Ӧ���ڵ��ڳ�����˰��" */);
          return false;
        }
        // ����ת��˰��
        if (oValueMny != null) {
          UFDouble dbTransMny = ((dbValueIn.sub(dbValueEx))
              .multiply(dbValueMny)).div(new UFDouble(100));
          if (dbTransMny.doubleValue() > m_dMaxValue) {
            // showErrorMessage("������������ֵ" + m_dMaxValue + "�������");
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000164", null, new String[] {
                  String.valueOf(m_dMaxValue)
                }));
            getBillCardPanel().getBillModel().setValueAt(null, row,
                "ndrawsummny");
          }
          else {
            dbTransMny = dbTransMny
                .setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
            getBillCardPanel().getBillModel().setValueAt(dbTransMny, row,
                "ndrawsummny");
          }
        }
        else {
          getBillCardPanel().getBillModel()
              .setValueAt(null, row, "ndrawsummny");
        }
      }
    }
    return true;
  }

  /**
   * ����󴥷��� �������ڣ�(2001-10-26 14:31:14)
   * 
   * @param key
   *          java.lang.String
   */
  public void afterSort(String key) {
    int iIndex = getBillListPanel().getHeadTable().getSelectedRow();
    String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getCbilltypecode();
    m_voCurBill = m_voBills[iIndex];
    m_iCurBillPrt = iIndex;
    getBillListPanel().setBodyValueVO(m_voCurBill.getChildrenVO());
    // ������ʽ
    execListPanelBodyFormula();
    // showHintMessage("ѡ���" + (iIndex + 1) + "������");
    String[] value = new String[] {
      String.valueOf(iIndex + 1)
    };
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
        "UPP20143010-000166", null, value));
    boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getBauditedflag().booleanValue();
    if (bHasAudit == false) {
      // ��δ���
      btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
    }
    else {
      btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
    }
    if (sBilltype.equals(m_sBillType)
        || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType
            .equals(ConstVO.m_sBillQCXSCBJZD))
        || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType
            .equals(ConstVO.m_sBillQCRKD))) {
      // �ǵ�ǰ��������
      btnCtrl.set(true,IABtnConst.BTN_SWITCH);
      btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
      boolean bAllHasAudit = true;
      boolean bAllVouchered = true;
      for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
        String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
            .getCauditorid();
        if (sAuditorID == null || sAuditorID.length() == 0) {
          bAllHasAudit = false;
          break;
        }
      }
      for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
        if (((BillItemVO) m_voCurBill.getChildrenVO()[i]).getBrtvouchflag()
            .booleanValue() == false) {
          bAllVouchered = false;
          break;
        }
      }
      if (bAllHasAudit || bAllVouchered) {
        // ������˻�����ƾ֤��
        btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
      }
      else {
        btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);
      }
    }
    else {
      // ���ǵ�ǰ��������
      btnCtrl.set(false,IABtnConst.BTN_SWITCH);
      btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
      btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
    }
    setBtnsForBilltypes(m_aryButtonGroupList);
    if (m_voCurBill.getChildrenVO().length != 0) {
      // ��������ʹ������ָ������ť״̬��ȷ
      BillEditEvent event = new BillEditEvent(
          getBillListPanel().getBodyTable(), -1, 0);
      bodyChanged(event);
    }
  }

  /**
   * ��������:�༭ǰ���� <p/> ����: nc.ui.pub.bill.BillEditEvent e ----- �༭�¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sKey = e.getKey();
    int iRowCount = getBillCardPanel().getBillTable().getRowCount();
    int iRow = e.getRow();
    BillItem bt = getBillCardPanel().getBodyItem(sKey);
    if (bt == null) {
      return false;
    }
    String sName = bt.getName();
    if (m_iStatus != ADD_STATUS && m_iStatus != UPDATE_STATUS) {
      // bt.setEnabled(false);
      return false;
    }
    // ������״̬
    UFBoolean bIsAudit = new UFBoolean(false);
    UFBoolean bLineIsAudited = new UFBoolean(false);
    UFBoolean bLineIsVouched = new UFBoolean(false);
    if (m_iStatus == UPDATE_STATUS
        && iRow <= m_voCurBill.getChildrenVO().length - 1) {
      bIsAudit = ((BillHeaderVO) m_voCurBill.getParentVO()).getBauditedflag();
      String sAuditID = ((BillItemVO) m_voCurBill.getChildrenVO()[iRow])
          .getCauditorid();
      if (sAuditID != null && sAuditID.trim().length() != 0) {
        bLineIsAudited = new UFBoolean(true);
      }
      bLineIsVouched = ((BillItemVO) m_voCurBill.getChildrenVO()[iRow])
          .getBrtvouchflag();
    }
    if (bIsAudit.booleanValue()) {
      // �з�¼�Ѿ���ˣ�ֻ���޸����������ۡ����
      if (bLineIsAudited.booleanValue()) {
        // ���з�¼�Ѿ��ɱ�����
        // bt.setEnabled(false);
        String[] value = new String[] {
            String.valueOf(e.getRow() + 1), sName
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000176"/* ��{0}�з�¼�Ѿ��ɱ����㣬�����޸�{1} */, null, value));
        return false;
      }
    }
    if (bLineIsVouched != null && bLineIsVouched.booleanValue()) {
      // �з�¼�Ѿ�����ʵʱƾ֤
      // ���з�¼�Ѿ��ɱ�����
      // bt.setEnabled(false);
      String[] value = new String[] {
          String.valueOf(e.getRow() + 1), sName
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000177"/* ��{0}�з�¼�Ѿ�����ʵʱƾ֤�������޸�{1} */, null, value));
      return false;
    }
    int iColumnCount = getBillCardPanel().getBillModel().getBodyColByKey(sKey);
    if (iColumnCount == -1
        || ((bt.isEnabled() == false || bt.isEdit() == false)))// ������ò����޸ĸ�������Ϣ
    {
      if (m_bIsICStart) {
        // showHintMessage("�����޸�" + sName);
        String[] value = new String[] {
          sName
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000167", null, value));
        return false;
      }
      else if (m_bIsPOStart
          && (m_sBillType.equals(ConstVO.m_sBillCGRKD) || m_sBillType
              .equals(ConstVO.m_sBillWWJGSHD))) {
        // showHintMessage("�����޸�" + sName);
        String[] value = new String[] {
          sName
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000167", null, value));
        return false;
      }
      else if (m_bIsPOStart
          && (m_sBillType.equals(ConstVO.m_sBillRKTZD) || m_sBillType
              .equals(ConstVO.m_sBillCLCKD))) {
        // �ɹ����ã�������������ϳ���(VMI)
        if (m_voCurBill != null) {
          String sSource = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getCsourcemodulename();
          if (sSource != null && sSource.equals(ConstVO.m_sModulePO)) {
            // �ǲɹ����˵�
            // showHintMessage("�����޸�" + sName);
            String[] value = new String[] {
              sName
            };
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000167", null, value));
            return false;
          }
        }
      }
      else if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
        // �������ã������۳ɱ���ת��
        if (sKey.equals("vbatch") == false) {
          // showHintMessage("�����޸�" + sName);
          String[] value = new String[] {
            sName
          };
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000167", null, value));
          return false;
        }
      }
      else if (sKey.equals("nchangerate") == false
          && sKey.equals("castunitname") == false
          && sKey.equals("nassistnum") == false
          && sKey.equals("vbatch") == false) {
        // showHintMessage("�����޸�" + sName);
        String[] value = new String[] {
          sName
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
            "UPP20143010-000167", null, value));
        return false;
      }
    }
    String sNotNull = " isnull ";
    if (iRowCount <= iRow) {
      iRow = -1;
    }
    try {

      // �Ƿ񱻵������ݿɱ༭
      if (sKey.equals("cadjustbill")) {
        Object oBillType = getBillCardPanel().getBodyValueAt(iRow,
            "cadjustbilltype");
        if (oBillType == null || oBillType.toString().trim().length() == 0) {
          return false;
        }
      }
      // ���û�����
      else if (sKey.equals("nchangerate")) {
        Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cinventoryid");
        if (oInvID != null) {
          // ���ø�������ʶ
          Object oMeaPK = getBillCardPanel().getBillModel().getValueAt(iRow,
              "castunitid");
          if (oMeaPK != null && oMeaPK.toString().trim().length() != 0) {
            Object oFixedflag = m_htInvAndFix.get(oInvID.toString().trim()
                + "," + oMeaPK.toString().trim());
            if (oFixedflag != null && oFixedflag.toString().equals("Y")) {
              // �ǹ̶������ʣ������޸Ļ�����
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "SCMCOMMON", "UPPSCMCommon-000082")/*
                                                       * @res
                                                       * "��ǰ����������븨����֮���ǹ̶������ʣ������޸Ļ�����"
                                                       */);
              // getBillCardPanel().getBodyItem("nchangerate").setEnabled(false);
              // getBillCardPanel().getBodyItem("nchangerate").setEdit(false);
              return false;
            }
            else {
              // getBillCardPanel().getBodyItem("nchangerate").setEnabled(true);
              // getBillCardPanel().getBodyItem("nchangerate").setEdit(true);
              return true;
            }
          }
          else {
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000155")/* @res "û��¼�븨������λ���������뻻����" */);
            return false;
            // getBillCardPanel().getBodyItem("nchangerate").setEnabled(false);
          }
        }
      }
      // ���ø�������λ������
      else if (sKey.equals("castunitname")) {
        Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cinventoryid");
        if (oInvID != null) {
          Hashtable htIsMngFrAssi = ce.isManageForAssi(m_sCorpID, new String[] {
            oInvID.toString()
          });
          UFBoolean bIsAss = (UFBoolean) htIsMngFrAssi.get(oInvID.toString());
          // UFBoolean bIsAss = ce.isManageForAssi(m_sCorpID,
          // oInvID.toString().trim());
          if (bIsAss.booleanValue()) {
            getBillCardPanel().getBodyItem("castunitname").setEnabled(true);
            UIRefPane astRef = (UIRefPane) bt.getComponent();
            ((nc.ui.ia.pub.AssunitRefmodel) astRef.getRefModel())
                .setInvs(oInvID.toString());
          }
          else {
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000111")/*
                                                   * @res
                                                   * "��ǰ������Ǹ������������������븨������λ"
                                                   */);
            // getBillCardPanel().getBodyItem("castunitname").setEnabled(false);
            return false;
          }
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000112")/* @res "������������Ϣ�������븨������λ" */);
          return false;
        }
      }
      // �����������õ�����������
      else if (sKey.equals("vfree0")) {
        // �������������
        bt = getBillCardPanel().getBodyItem("vfree0");
        if (bt != null) {
          String sInvManID = (String) getBillCardPanel().getBillModel()
              .getValueAt(iRow, "cinventoryid");
          String sInvCode = (String) getBillCardPanel().getBillModel()
              .getValueAt(iRow, "cinventorycode");
          String sInvName = (String) getBillCardPanel().getBillModel()
              .getValueAt(iRow, "cinventoryname");
          String sInvSpec = (String) getBillCardPanel().getBillModel()
              .getValueAt(iRow, "cinventoryspec");
          String sInvType = (String) getBillCardPanel().getBillModel()
              .getValueAt(iRow, "cinventorytype");
          // ��������VO
          InvVO voInv = new InvVO();
          voInv.setCinvmanid(sInvManID);
          voInv.setCinventoryid(sInvManID);
          voInv.setCinventorycode(sInvCode);
          voInv.setInvname(sInvName);
          voInv.setInvspec(sInvSpec);
          voInv.setInvtype(sInvType);
          // ����������VO
          FreeVO fvo = null;
          if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > iRow) {
            BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRow];
            fvo = btvo.getVOFree();
          }
          if (fvo == null || fvo.getVfree0() == null
              || fvo.getVfree0().trim().length() == 0) {
            ArrayList alInvIDs = new ArrayList();
            alInvIDs.add(sInvManID);
            fvo = (FreeVO) DefHelper.queryFreeVOByInvIDsOnceAll(alInvIDs)
                .get(0);
          }
          // ����VO
          if (fvo != null) {
            voInv.setIsFreeItemMgt(new Integer(1));
            for (int i = 1; i <= 5; i++) {
              Object oFree = getBillCardPanel().getBillModel().getValueAt(iRow,
                  "vfree" + i);
              fvo.setAttributeValue("vfree" + i, oFree);
            }
            voInv.setFreeItemVO(fvo);
          }
          m_bIsFreeEvent = false;
          getUIRefPaneFreeItem().setFreeItemParam(voInv);
          m_bIsFreeEvent = true;
        }
      }
      // �������κŲ���
      else if (sKey.equals("vbatch")) {
        // ����ǳ��ⵥ�Ҳ����ڳ����۳ɱ���ת������������κŹ��������κ�Ϊ����
        getUIRefPaneBatch().setWhereString("");
        Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cinventoryid");
        Object oNumber = getBillCardPanel().getBillModel().getValueAt(iRow,
            "nnumber");
        if (oInvID != null
            && CommonDataBO_Client.isManageForBatch(m_sCorpID,
                oInvID.toString()).booleanValue()) {
          Hashtable htIsmngFrFree = ce.isManageForFree(m_sCorpID, new String[] {
            oInvID.toString()
          });
          if (((UFBoolean) htIsmngFrFree.get(oInvID.toString())).booleanValue()) {
            // �����������
            Object oFree = getBillCardPanel().getBillModel().getValueAt(iRow,
                "vfree0");
            if (oFree == null || oFree.toString().trim().length() == 0) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000113")/*
                                                     * @res
                                                     * "��ǰ������������������������������Ϣ"
                                                     */);
              return false;
            }
          }
          // �����ι���
          getBillCardPanel().getBodyItem("vbatch").setEdit(true);
          getBillCardPanel().getBodyItem("vbatch").setEnabled(true);
          String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
              .getValue();
          String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
              .getValue();
          String sWhereString = "";
          if (oNumber != null && oNumber.toString().trim().length() != 0) {
            sWhereString = " sum(a.innum) - sum(a.outnum) >= " + oNumber;
            getUIRefPaneBatch().setNumCondition(sWhereString);
            sWhereString = "";
          }
          // �ж��Ƿ����вֿ⡢�����֯����������������κŲ�������
          if (sWareID != null && sWareID.trim().length() != 0) {
            sWhereString = sWhereString + " and v.cwarehouseid = '" + sWareID
                + "' ";
          }
          if (sRDID != null && sRDID.trim().length() != 0) {
            sWhereString = sWhereString + " and v.cstockrdcenterid = '"
                + sRDID.trim() + "' ";
          }
          sWhereString = sWhereString + " and v.cinventoryid = '" + oInvID
              + "' ";
          // ����������
          for (int i = 1; i < 6; i++) {
            String sFieldName = "vfree" + i;
            Object oFree = getBillCardPanel().getBillModel().getValueAt(iRow,
                sFieldName);
            if (oFree != null && oFree.toString().trim().length() != 0) {
              sWhereString = sWhereString + " and v." + sFieldName + " = '"
                  + oFree + "'";
            }
          }
          getUIRefPaneBatch().setWhereString(sWhereString);
        }
        else if (oInvID != null) {
          // getBillCardPanel().getBodyItem("vbatch").setEnabled(false);
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000114")/* @res "��ǰ����������ι����������������κ�" */);
          return false;
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000115")/* @res "����������Ϣ" */);
          return false;
        }
        Object oBatch = getBillCardPanel().getBillModel().getValueAt(iRow,
            "vbatch");
        if (oBatch != null) {
          getUIRefPaneBatch().setBatchValue(oBatch.toString());
        }
        // �����ϲ��ϳ��ⵥ������������κ�
        BillItem bt2 = m_bd.getHeadItem("bwithdrawalflag");
        if (m_sBillType.equals(ConstVO.m_sBillCLCKD) && bt2 != null) {
          // Object oValue = bt2.getValue();
          UIComboBox uiCombobox = (UIComboBox) bt2.getComponent();
          int iItem = uiCombobox.getSelectedIndex();
          // if (oValue != null && oValue.toString().equals("��")) {
          if (iItem == m_ComboItemsVO.type_yes) {// �Ǽ�����
            getUIRefPaneBatch().setHasZero(true);
            // �����ֹ�����
            getUIRefPaneBatch().setAutoCheck(false);
          }
          else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
            getUIRefPaneBatch().setHasZero(false);
            // �����ֹ�����
            getUIRefPaneBatch().setAutoCheck(true);
          }
        }
        else if (m_sBillType.equals(ConstVO.m_sBillCLCKD)) {
          getUIRefPaneBatch().setHasZero(false);
          // �����ֹ�����
          getUIRefPaneBatch().setAutoCheck(true);
        }
      }
      // �����Ƿ�������븨��������
      else if (sKey.equals("nassistnum")) {
        Object oAstUnitID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "castunitid");
        if (oAstUnitID == null || oAstUnitID.toString().trim().length() == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000116")/* @res "û��¼�븨������λ���������븨��������" */);
          // getBillCardPanel().getBodyItem("nassistnum").setEnabled(false);
          return false;
        }
        else {
          getBillCardPanel().getBodyItem("nassistnum").setEnabled(true);
          return true;
        }
      }
      // �����Ƿ��������뵥��
      else if (sKey.equals("nprice") && (m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
        // if (sParam.equals("��") || sParam.equalsIgnoreCase("N"))
        // {/*-=notranslate=-*/
        if (!m_bAllowDefinePriceByUser) {
          // ���ⵥ�������Զ��嵥�ۣ����ڳ����ݱ������뵥��
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000117")/*
                                                 * @res
                                                 * "��������Ϊ���ⵥ�������Զ��嵥�ۣ��������뵥��"
                                                 */);
          // getBillCardPanel().getBodyItem("nprice").setEnabled(false);
          return false;
        }
      }
      // �����Ƿ��������뵥��
      else if (sKey.equals("nmoney") && (m_bIsOtherBill || m_bIsOutBill)
          && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false
          && m_sBillType.equals(ConstVO.m_sBillCKTZD) == false
          && m_sBillType.equals(ConstVO.m_sBillJHJTZD) == false) {
        // String sParam = ce.getParaValue(m_sCorpID, ConstVO.m_iPara_ZDYDJ);
        if (!m_bAllowDefinePriceByUser) { // sParam.equals("��") ||
                                          // sParam.equalsIgnoreCase("N"))
                                          // {/*-=notranslate=-*/
          // ���ⵥ�������Զ��嵥�ۣ����ڳ����ݱ������뵥��
          // getBillCardPanel().getBodyItem("nmoney").setEnabled(false);
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000118")/*
                                                 * @res
                                                 * "��������Ϊ���ⵥ�������Զ��嵥�ۣ�����������"
                                                 */);
          return false;
        }
      }
      // ���ûس嵥�ݷ�¼���գ�����������
      else if (sKey.equals("cadjustbill") && m_bIsInAdjustBill == false) {
        String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
            .getValue();
        String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
            .getValue();
        Object oInvID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cinventoryid");
        Object oBill_BID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cbill_bid");
        Object oNumber = getBillCardPanel().getBillModel().getValueAt(iRow,
            "nnumber");
        double dNumber = 0;
        if (oNumber != null && oNumber.toString().trim().length() != 0) {
          dNumber = new UFDouble(oNumber.toString().trim()).doubleValue();
          if (dNumber < 0) {
            // �Ǻ�嵥
            getUIRefPaneAdjustBillItem().setWhereString("");
            String sWhereString = " having a.nnumber - " + sNotNull
                + "(a.nsettledretractnum,0) >= " + (-dNumber);
            getUIRefPaneAdjustBillItem().setNumCondition(sWhereString);
            sWhereString = "";
            sWhereString = sWhereString + " and a.cstockrdcenterid = '" + sRDID
                + "'";
            // �ж��Ƿ����вֿ⣬���������κŲ�������
            if (sWareID != null && sWareID.trim().length() != 0) {
              sWhereString = sWhereString + " and a.cwarehouseid = '" + sWareID
                  + "' ";
            }
            if (oInvID != null && oInvID.toString().trim().length() != 0) {
              sWhereString = sWhereString + " and a.cinventoryid = '"
                  + oInvID.toString().trim() + "'";
            }
            if (oBill_BID != null && oBill_BID.toString().trim().length() != 0) {
              sWhereString = sWhereString + " and a.cbill_bid != '"
                  + oBill_BID.toString().trim() + "'";
            }
            if ((m_bIsOtherBill || m_bIsOutBill) == false) {
              sWhereString = sWhereString + " and a.fdispatchflag = 0 ";
            }
            else {
              sWhereString = sWhereString + " and a.fdispatchflag = 1 ";
            }
            getUIRefPaneAdjustBillItem().setWhereString(sWhereString);
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitemid");
          }
          else {
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000119")/* @res "��ǰ������С��0����������س嵥�ݷ�¼" */);
            return false;
          }
        }
        else {
          // ������������
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000120")/* @res "û��������������������س嵥�ݷ�¼" */);
          return false;
        }
      }
      else if (sKey.equals("cadjustbillitem") && m_bIsInAdjustBill) {
        // �����������û�б������������ͻ򱻵������ݣ����������ݷ�¼���ɱ༭
        Object oAdjustBillType = getBillCardPanel().getBodyValueAt(iRow,
            "cadjustbilltype");
        Object oAdjustBillID = getBillCardPanel().getBodyValueAt(iRow,
            "cadjustbillid");
        if (oAdjustBillType == null
            || oAdjustBillType.toString().trim().length() == 0
            || oAdjustBillID == null
            || oAdjustBillID.toString().trim().length() == 0) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000121")/*
                                                 * @res
                                                 * "�������������ͻ򱻵����������ݲ�ȫ���������������Ϣ��������������ݷ�¼"
                                                 */);
          // getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          return false;
        }
      }
      // ������Ŀ�׶�
      else if (sKey.equals("cprojectphasecode")) {
        Object oProjectID = getBillCardPanel().getBillModel().getValueAt(iRow,
            "cprojectid");
        if (oProjectID == null || oProjectID.toString().trim().length() == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000122")/* @res "û��������Ŀ��Ϣ������������Ŀ�׶���Ϣ" */);
          return false;
        }
        ((nc.ui.bd.b39.PhaseRefModel) getUIRefPaneJobParse().getRefModel())
            .setJobID(oProjectID.toString().trim());
      }
      // 20050525 �رչ̶��ʲ��ʹ���ӿ�
      // ���ù̶��ʲ�����
      // else if (sKey.equals("cfadevicecode") && (m_bIsOtherBill ||
      // m_bIsOutBill)) {
      // Object oFadeviceID = getBillCardPanel().getBillModel().getValueAt(iRow,
      // "cfadeviceid");
      // if (oFadeviceID != null && oFadeviceID.toString().trim().length() != 0)
      // {
      // getUIRefPaneFacard().setPkSubeq(oFadeviceID.toString());
      // }
      // }
      // //���ù̶��ʲ�����
      // else if (sKey.equals("cfadevicecode")) {
      // Object oFadeviceID = getBillCardPanel().getBillModel().getValueAt(iRow,
      // "cfadeviceid");
      // Object oFaCardcode = getBillCardPanel().getBillModel().getValueAt(iRow,
      // "cfadevicecode");
      // if (oFadeviceID != null && oFadeviceID.toString().trim().length() != 0)
      // {
      // getUIRefPaneFacardEquipment().setPkSubeq(oFadeviceID.toString());
      // }
      // if (oFaCardcode != null && oFaCardcode.toString().trim().length() != 0)
      // {
      // getUIRefPaneFacardEquipment().setCode(oFaCardcode.toString());
      // }
      // }
      // ���ù������Ĳ���
      else if (sKey.equals("cwpcode")) {
        String sCalbodyID = getBillCardPanel().getHeadItem("crdcenterid")
            .getValue();
        String sDeptID = getBillCardPanel().getHeadItem("cdeptid").getValue();
        String sWhere = m_sOldWkCondition;
        if (sCalbodyID != null) {
          sWhere = sWhere + "and gcbm = '" + sCalbodyID + "'";
        }
        if (sDeptID != null) {
          sWhere = sWhere + "and ssbmid = '" + sDeptID + "'";
        }
        getUIRefPaneWkCenter().getRefModel().setWherePart(sWhere);
      }
      // ���óɱ�������յ�����
      else if (sKey.equals("vbomcodecode")) {
        String sCalbodyID = getBillCardPanel().getHeadItem("crdcenterid")
            .getValue();
        if (sCalbodyID != null) {
          bt = m_bd.getBodyItem("vbomcodecode");
          if (bt != null) {
            // ���óɱ�������յ�����
            String sWhere = m_sOldBomCondition
                + "and bd_produce.pk_calbody = '" + sCalbodyID + "'";
            ((UIRefPane) bt.getComponent()).getRefModel().setWherePart(sWhere);
          }
        }
      }
      else if (sKey.equals("cinventorycode")) {
        String sCalbodyID = (String)getBillCardPanel().getHeadItem("crdcenterid").getValueObject();
        if (sCalbodyID != null) {
          String sAddString = m_sOldInvCondition;
          if (sAddString.length() != 0)
            sAddString += " and ";
          sAddString += " bd_produce.pk_calbody='" + sCalbodyID + "'";
          if (m_bIsPlanedPriceBill) {
            // �Ǽƻ��۵�����������ļƼ۷�ʽֻ���Ǽƻ���
            sAddString += " and bd_produce.pricemethod = " + ConstVO.JHJ + " ";
          }
          m_refpaneInv.setWhereString(sAddString);
          m_refpaneInvBack.setWhereString(sAddString);
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000285")/* ����ѡ������֯(�ɱ�) */);
          return false;
        }
      }
      // ����Ƿ����˰ת��δѡ�������˰�ͳ�����˰���ɱ༭ zlq add 20050329
      else if (m_bIsOutBill
          && (sKey.equals("nincometax") || sKey.equals("nexpaybacktax"))) {
        Object oValue = getBillCardPanel().getBillModel().getValueAt(
            e.getRow(), "btransferincometax");
        UFBoolean bValue = new UFBoolean(oValue.toString());
        if (!bValue.booleanValue()) {
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nincometax", false);
          getBillCardPanel().getBillModel().setCellEditable(e.getRow(),
              "nexpaybacktax", false);
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000278")/* "δѡ��'�Ƿ����˰ת��',���ܱ༭���ֶ�" */);
          return false;
        }
      }

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000168", "20143010", new String[] {
            sName
          })/* "�޸�{0}" */);
    }
    catch (Exception ee) {
      showErrorMessage(ee.getMessage());
      ee.printStackTrace();
    }
    return true;
  }

  /**
   * ��������:�б仯�󴥷� <p/> ����: nc.ui.pub.bill.BillEditEvent e ----- �༭�¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public void bodyChanged(BillEditEvent e) {
    int iIndex = e.getRow();
    iIndex = getBillListPanel().getBodyTable().getSelectedRow();
    String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
        .getCbilltypecode();
    // �ɵ����б����������񴥷�
    if (e.getSource() == getBillListPanel().getBodyTable()) {

      String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
          .getCauditorid();
      if (sAuditorID != null && sAuditorID.length() != 0) {
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
      else if (sBilltype.equals(ConstVO.m_sBillDJTQD) == false) {
        int iPricemode = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
            .getFpricemodeflag().intValue();
        // δ���
        // �жϷ�¼�Ƿ�����
        // ����Ƽ����ֳ��ⵥ�������
        // ����Ƽۺ�����ⵥû��ָ���س嵥�ݷ�¼�������
        // ��Դ�Ƿ�Ʊ�Ĳ�ָ��
        int iRDFlag = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getFdispatchflag().intValue();
        //
        String sSourceBillType = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
            .getCsourcebilltypecode();
        UFDouble dNumber = ((BillItemVO) m_voCurBill.getChildrenVO()[iIndex])
            .getNnumber();
        String sBizTypeID = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCbiztypeid();
        if (iPricemode == ConstVO.GBJJ && dNumber != null) {
          // �Ǹ���Ƽ�δ�ɱ�����,��������
          double ddnum = dNumber.doubleValue();
          if (iRDFlag == 1 && ddnum > 0) {
            if (sSourceBillType == null) {
              // �����ֳ��ⵥ������ԴΪ��
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
              // �����ֳ��ⵥ������Դ���Ƿ�Ʊ
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else if (sBizTypeID == null
                || (m_sFQSK.indexOf(sBizTypeID) == -1 && m_sWTDX
                    .indexOf(sBizTypeID) == -1)) {
              // �����ֳ��ⵥ������Դ�Ƿ�Ʊ�������Ƿ����տ��ί�д���
              btnCtrl.set(true,IABtnConst.BTN_AUDIT);
            }
            else {
              btnCtrl.set(false,IABtnConst.BTN_AUDIT);
            }
          }
          else if (iRDFlag == 0 && ddnum < 0) {
            // �Ǻ�����ⵥ
            btnCtrl.set(true,IABtnConst.BTN_AUDIT);
          }
        }
        else {
          // ����������ָ��
          btnCtrl.set(false,IABtnConst.BTN_AUDIT);
        }
      }
      else {
        // ����������ָ��
        btnCtrl.set(false,IABtnConst.BTN_AUDIT);
      }
      // ���ð�ť
      setBtnsForBilltypes(m_aryButtonGroupList);
    }
  }

  /**
   * ��������:�б仯�󴥷� <p/> ����: nc.ui.pub.bill.BillEditEvent e ----- �༭�¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public void bodyRowChange(BillEditEvent e) {
  }

  /**
   * �Ի���ر��¼������߱���ʵ�ֵĽӿڷ���
   * 
   * @param event
   *          UIDialogEvent �Ի���ر��¼�
   */
  public void dialogClosed(UIDialogEvent event) {
    if (event.m_Operation == UIDialogEvent.WINDOW_CANCEL) {
      return;
    }
    if (event.getSource() == getLocateConditionDlg()) {
      locateBills();
    }
    else if (event.getSource() == getQueryPlannedPriceDlg()) {
      addQuery();
    }
    else if (event.getSource() == getIndividualAllotDlg()) {
      auditOneBill(true, "123");
    }
    else if (event.getSource() == getSaleBillsChooseDlg()) {
      showBillInForm();
    }
    else {
      // showHintMessage("���ص��ݽ���");
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000173"));
    }
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @return java.lang.String
   */
  public String getTitle() {
    return m_sTitle;
  }

  /**
   * ��������:�б仯�󴥷� <p/> ����: nc.ui.pub.bill.BillEditEvent e ----- �༭�¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public void headChanged(BillEditEvent e) {

    int iIndex = e.getRow();
    if( iIndex == -1)
    	return;
    String sBilltype = "";
    try {
      // �ɵ����б������ͷ���񴥷�
      if (e.getSource() == getBillListPanel().getHeadTable()) {
        if (m_voBills != null && m_voBills.length > iIndex) {
          m_voCurBill = m_voBills[iIndex];
          m_iCurBillPrt = iIndex;
          if (m_voCurBill.getChildrenVO() == null) {
            ClientLink cl = ce.getClientLink();
            m_voCurBill = BillBO_Client.querybillWithOtherTable(null, null,
                null, null, null, m_voCurBill, new Boolean(false), cl)[0];
            m_voBills[iIndex] = m_voCurBill;
          }
          getBillListPanel().setBodyValueVO(m_voCurBill.getChildrenVO());
          // ������ʽ
          execListPanelBodyFormula();
          // showHintMessage("ѡ���" + (iIndex + 1) + "������");
          String[] value = new String[] {
            String.valueOf(iIndex + 1)
          };
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000166", null, value));
          sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
              .getCbilltypecode();
          if (sBilltype.equals(m_sBillType)
              || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType
                  .equals(ConstVO.m_sBillQCXSCBJZD))
              || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType
                  .equals(ConstVO.m_sBillQCRKD))) {
            // �ǵ�ǰ��������
            boolean bHasAudit = ((BillHeaderVO) m_voCurBill.getParentVO())
                .getBauditedflag().booleanValue();
            if (bHasAudit == false) {
              // ��δ���
              btnCtrl.set(true,IABtnConst.BTN_BILL_DELETE);
            }
            else {
              btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
            }
            btnCtrl.set(true,IABtnConst.BTN_SWITCH);
            btnCtrl.set(true,IABtnConst.BTN_BILL_COPY);
            boolean bAllHasAudit = true;
            boolean bAllVouchered = true;
            for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
              String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .getCauditorid();
              if (sAuditorID == null || sAuditorID.length() == 0) {
                bAllHasAudit = false;
                break;
              }
            }
            for (int i = 0; i < m_voCurBill.getChildrenVO().length; i++) {
              if (((BillItemVO) m_voCurBill.getChildrenVO()[i])
                  .getBrtvouchflag().booleanValue() == false) {
                bAllVouchered = false;
                break;
              }
            }
            if (bAllHasAudit || bAllVouchered) {
              // �������
              btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
            }
            else {
              btnCtrl.set(true,IABtnConst.BTN_BILL_EDIT);

            }
          }
          else {
            // ���ǵ�ǰ��������
            btnCtrl.set(false,IABtnConst.BTN_BILL_DELETE);
            btnCtrl.set(false,IABtnConst.BTN_SWITCH);
            btnCtrl.set(false,IABtnConst.BTN_BILL_COPY);
            btnCtrl.set(false,IABtnConst.BTN_BILL_EDIT);
          }
          setBtnsForBilltypes(m_aryButtonGroupList);
          if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > 0) {
            getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
            String sRDID = ((BillHeaderVO) m_voCurBill.getParentVO())
                .getCrdcenterid();
            BillItem bt = getBillListPanel().getBodyItem("blargessflag");

            BillItemVO[] voBillItemTemp = (BillItemVO[]) m_voCurBill
                .getChildrenVO();
            String[] sInvIds = getUniInvIds(voBillItemTemp);
            m_htInvAndKind = getMaterialState(sRDID, sInvIds);

            // ������̬
            for (int j = 0; j < voBillItemTemp.length; j++) {
              String sInv = voBillItemTemp[j].getCinventoryid();
              if (m_htInvAndKind == null) {
                m_htInvAndKind = new Hashtable();
              }
              Object oKind = m_htInvAndKind.get(sRDID + "," + sInv);
              if (oKind == null) {
                oKind = "a";
              }
              ((BillItemVO) m_voCurBill.getChildrenVO()[j]).setCinvkind(oKind
                  .toString());

              if (bt != null) {
                // ��������
                setComboBoxInBodyFromVO((BillItemVO) m_voCurBill
                    .getChildrenVO()[j], false, j);

              }
            }

            int iBodyRow = 0;
            String sAuditorID = ((BillItemVO) m_voCurBill.getChildrenVO()[iBodyRow])
                .getCauditorid();
            if (sAuditorID != null && sAuditorID.length() != 0) {
              btnCtrl.set(false,IABtnConst.BTN_AUDIT);
            }
            else if (sBilltype.equals(ConstVO.m_sBillDJTQD) == false) {
              int iPricemode = ((BillItemVO) m_voCurBill.getChildrenVO()[iBodyRow])
                  .getFpricemodeflag().intValue();
              // δ���
              // �жϷ�¼�Ƿ�����
              // ����Ƽ����ֳ��ⵥ�������
              // ����Ƽۺ�����ⵥû��ָ���س嵥�ݷ�¼�������
              // ��Դ�Ƿ�Ʊ�Ĳ�ָ��
              String sBizTypeID = ((BillHeaderVO) m_voCurBill.getParentVO())
                  .getCbiztypeid();
              int iRDFlag = ((BillHeaderVO) m_voCurBill.getParentVO())
                  .getFdispatchflag().intValue();
              //
              String sSourceBillType = ((BillItemVO) m_voCurBill
                  .getChildrenVO()[0]).getCsourcebilltypecode();

              UFDouble dNumber = ((BillItemVO) m_voCurBill.getChildrenVO()[0])
                  .getNnumber();
              if (iPricemode == ConstVO.GBJJ && dNumber != null) {
                // �Ǹ���Ƽ�δ�ɱ�����,��������
                double ddnum = dNumber.doubleValue();
                if (iRDFlag == 1 && ddnum > 0) {
                  if (sSourceBillType == null) {
                    // �����ֳ��ⵥ������Դ���Ƿ�Ʊ
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else if (sSourceBillType.equals(ConstVO.m_sBillXSFP) == false) {
                    // �����ֳ��ⵥ������Դ���Ƿ�Ʊ
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else if (m_sFQSK.indexOf(sBizTypeID) == -1
                      && m_sWTDX.indexOf(sBizTypeID) == -1) {
                    // �����ֳ��ⵥ������Դ�Ƿ�Ʊ�������Ƿ����տ��ί�д���
                    btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                  }
                  else {
                    btnCtrl.set(false,IABtnConst.BTN_AUDIT);
                  }
                }
                else if (iRDFlag == 0 && ddnum < 0) {
                  // �Ǻ�����ⵥ
                  btnCtrl.set(true,IABtnConst.BTN_AUDIT);
                }
              }
              else {
                // ����������ָ��
                btnCtrl.set(false,IABtnConst.BTN_AUDIT);
              }
            }
            else {
              btnCtrl.set(false,IABtnConst.BTN_AUDIT);
            }
            // ����ѡ��Ϊ��
            getBillListPanel().getBodyTable().clearSelection();
            // ���ð�ť
            setBtnsForBilltypes(m_aryButtonGroupList);
          }
          try {
            for (int hh = 0; hh < m_voCurBill.getChildrenVO().length; hh++) {
              // �����Զ������
              for (int mm = 1; mm < 6; mm++) {
                String sKey = "vdef" + mm;
                BillItem bt = m_bdList.getBodyItem(sKey);
                if (bt != null && bt.isShow()) {
                  Object oTemp = getBillListPanel().getBodyBillModel()
                      .getValueAt(hh, sKey);
                  String sDefKey = sKey.substring(1);
                  String sWhereSQL = "pk_defdef=(SELECT bd_defquote.pk_defdef FROM bd_defquote LEFT JOIN bd_defused ON bd_defquote.pk_defused = bd_defused.pk_defused "
                      + "WHERE bd_defused.objname = '"
                      + ConstVO.m_sDefBodyName
                      + "' AND fieldname='" + sDefKey + "')";
                  nc.vo.bd.def.DefVO[] defvo = null;
                  defvo = nc.ui.bd.def.DefBO_Client.queryBySQL(sWhereSQL);
                  if (defvo != null && defvo.length != 0) {
                    String sDefType = defvo[0].getDefdef().getType();
                    if ("ͳ��".equals(sDefType)) {/*-=notranslate=-*/
                      String sSQL = " select ";
                      sSQL = sSQL + " docname ";
                      sSQL = sSQL + " from ";
                      sSQL = sSQL + " bd_defdoc ";
                      sSQL = sSQL + " where ";
                      sSQL = sSQL + " pk_defdoc ='" + oTemp + "'";
                      String sResult[][] = CommonDataBO_Client.queryData(sSQL);
                      if (sResult != null && sResult.length != 0) {
                        String[] sTemp = sResult[0];
                        if (sTemp != null && sTemp.length != 0) {
                          getBillListPanel().getBodyBillModel().setValueAt(
                              sTemp[0], mm, sKey);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          catch (Exception eee) {
            eee.printStackTrace();
            Log.info("ȡ�Զ�������Ϣ����");
          }
        }
      }
    }
    catch (Exception ee) {
      ee.printStackTrace();
    }
  }

  /**
   * Gives notification that there was an insert into the document. The range
   * given by the DocumentEvent bounds the freshly inserted region.
   * 
   * @param e
   *          the document event
   */
  public void insertUpdate(DocumentEvent e) {
    handleAdjustBillType(e);
  }

  /**
   * ��������:���˫������ <p/> ����: nc.ui.pub.bill.BillMouseEnent e ----- ����¼� <p/> ����ֵ:
   * <p/> �쳣:
   */
  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    if (e.getPos() == 0) {
      // �Ǳ�ͷ����
      if (m_voBills != null && m_voBills.length != 0) {
        String sBilltype = ((BillHeaderVO) m_voCurBill.getParentVO())
            .getCbilltypecode();
        if (sBilltype.equals(m_sBillType)
            || (sBilltype.equals(ConstVO.m_sBillQCRKD) && m_sBillType
                .equals(ConstVO.m_sBillQCXSCBJZD))
            || (sBilltype.equals(ConstVO.m_sBillQCXSCBJZD) && m_sBillType
                .equals(ConstVO.m_sBillQCRKD))) {
          // �ǵ�ǰ��������
          onButtonListClicked();
        }
        else {
          //"��ǰ���ܵ㵥�����ͱ����� {0}��ѡ��ĵ��ݵ������ͱ�����{1}���Բ�����ʾ��������
          String[] value = new String[] {
              m_sBillType, sBilltype
          };
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000169", null, value));
        }
      }
    }
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼�
   * 
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    // ֹͣ�༭
    getBillCardPanel().stopEditing();
    if (bo == buttonTree.getButton(IABtnConst.BTN_ADD)) {
      onButtonAddManualClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_ADD_MANUAL)) {
      onButtonAddManualClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_ADD_QUERY)) {
      onButtonAddQueryClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BILL_EDIT)) {
      onButtonUpdateClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BILL_DELETE)) {
      onButtonDelClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_SAVE)) {
      onButtonOKClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BILL_CANCEL)) {
      onButtonCancelClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_LINE_ADD)) {
      onButtonAddLineClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_LINE_DELETE)) {
      onButtonDelLineClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_LINE_INSERT)) {
      onButtonInsertLineClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_LINE_COPY)) {
      onButtonCopyLineClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_LINE_PASTE)) {
      onButtonPasteLineClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_QUERY)) {
      onButtonQueryClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_SWITCH)) {
      onButtonListClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BILL_COPY)) {
      onButtonCopyClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_IMPORT_BILL)) {
      onButtonImportClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_AUDIT)) {
      onButtonAuditClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_REFRESH)) {
      onButtonRefreshClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_LOCATE)) {
      onButtonLocateClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_PRINT_PRINT)) {
      onButtonPrintDirectClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_PRINT_PREVIEW)) {
      onButtonPrintPreviewClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_CHOOSESALEBILL)) {
      onButtonChooseSaleBillClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_ASSIST_QUERY_RELATED)) {
      onButtonAssociateBillsClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_TOP)) {
      onButtonFirstClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_PREVIOUS)) {
      onButtonPrevClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_NEXT)) {
      onButtonNextClicked();
    }
    else if (bo == buttonTree.getButton(IABtnConst.BTN_BROWSE_BOTTOM)) {
      onButtonLastClicked();
    }
    //add by yhj 2014-03-31
    else if(bo == buttonTree.getButton(IABtnConst.BTN_AUTO_PRICE)){
    	try {
			onButtonAutoPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    //end
    //add by zy 2019-09-05
    else if (bo == buttonTree.getButton(IABtnConst.BTN_PASS_BIAOCAI)) {
//    	passbiaocai();
    	passBC();
      }
    //end
  }

  public void passBC(){
	  StringBuffer strs =null;
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
	  StringBuffer err=new StringBuffer();//�洢��ʾ��Ϣ
	  Boolean result = idetermineService.check(corp);
	  if(result){//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
		  int rows = 0;//ѡ������
		  int [] selectRows = getBillListPanel().getHeadTable().getSelectedRows();//��ȡ�б�״̬��ѡ�����������
		  rows=selectRows.length;//ѡ������
		  
		  for(int a=0;a<selectRows.length;a++)
		  {
			  BillVO billvo = null;
			  m_iCurBillPrt = selectRows[a];//�ѵ�ǰѡ���е���Ÿ�����ǰ�����ڻ����е����
			  billvo = m_voBills[m_iCurBillPrt];//��ǰ���ڴ����ĵ���
			  String billCode = ((BillHeaderVO) billvo.getParentVO()).getVbillcode();//���ݺ�
			  if (billvo.getChildrenVO() == null) {
			        //��û�л�ñ�������
			        ClientLink cl = ce.getClientLink();
			        try {
			        	billvo = BillBO_Client.querybillWithOtherTable(null, null,
						      null, null, null, billvo, new Boolean(true), cl)[0];
					} catch (Exception e) {
						e.printStackTrace();
					}
			        }
			  int brows = billvo.getChildrenVO().length; //��������
			  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();//��ͷVO
			  
			  TempGeneralBillVO tgbvo=new TempGeneralBillVO();//��ʱ��vo
			  String yn=judgeDJBH(hvo.getVbillcode(),corp);
			  if(yn==null || "N".equals(yn)){
			//����
//			  for(int b=0;b<rows;b++)
//			  {
//				  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[a];//��ȡ�����б���ĸ�������VO
//				  for(int i=0;b<brows;i++){
				      Map<String,String> map=new HashMap<String,String>();
					  List list=queryData(hvo.getVbillcode(),hvo.getPk_corp());
					  strs= CheckData(list,hvo.getVbillcode());
					  for(int i=0;i<list.size();i++){
						  map = (Map) list.get(i);
						  String djh=map.get("vbillcode")==null?"":map.get("vbillcode").toString();//���ݺ�
						  String djlx=map.get("cbilltypecode")==null?"":map.get("cbilltypecode").toString();//��������
						  String gs=map.get("pk_corp")==null?"":map.get("pk_corp").toString();//��˾
						  String djbs=map.get("cbillid")==null?"":map.get("cbillid").toString();//���ݱ�ʶ
						  String bmbs=map.get("cdeptid")==null?"":map.get("cdeptid").toString();//���ű�ʶ
						  String rybs=map.get("cemployeeid")==null?"":map.get("cemployeeid").toString();//��Ա��ʶ
						  String zdrbs=map.get("coperatorid")==null?"":map.get("coperatorid").toString();//�Ƶ��˱�ʶ
						  String sfbs=map.get("cdispatchid")==null?"":map.get("cdispatchid").toString();//�շ���ʶ
						  String ksbs=map.get("ccustomvendorid")==null?"":map.get("ccustomvendorid").toString();//���̱�ʶ
						  String flbs=map.get("cbill_bid")==null?"":map.get("cbill_bid").toString();//��¼��ʶ
						  Double je=map.get("nmoney")==null?0:Double.valueOf(String.valueOf(map.get("nmoney")));//���
						  Double sl=map.get("nnumber")==null?0:Double.valueOf(String.valueOf(map.get("nnumber")));//����
						  String chbs=map.get("cinventoryid")==null?"":map.get("cinventoryid").toString();//�����ʶ
						  Double hh=map.get("irownumber")==null?0:Double.valueOf(String.valueOf(map.get("irownumber")));//�к�
						  String zgbs=map.get("bestimateflag")==null?"":map.get("bestimateflag").toString();//�ݹ���ʶ
						  String key="";
						  if("I6".equals(djlx) || "IA".equals(djlx) || "I3".equals(djlx) || "ID".equals(djlx) || "IC".equals(djlx)){
							  key=chbs+bmbs+sfbs;
						  }else if("I5".equals(djlx) || "I5".equals(djlx)){
							  key=chbs+bmbs+sfbs+ksbs;
						  }
						  tgbvo.setQuerykey(key);
						  tgbvo.setVbillcode(djh);
						  tgbvo.setCbilltypecode(djlx);
						  tgbvo.setPk_corp(gs);
						  tgbvo.setCbillid(djbs);
						  tgbvo.setCdeptid(bmbs);
						  tgbvo.setCemployeeid(rybs);
						  tgbvo.setCoperatorid(zdrbs);
						  tgbvo.setCdispatchid(sfbs);
						  tgbvo.setCcustomvendorid(ksbs);
						  tgbvo.setCbill_bid(flbs);
						  tgbvo.setNmoney(je);
						  tgbvo.setNnumber(sl);
						  tgbvo.setCinventoryid(chbs);
						  tgbvo.setIrownumber(hh);
						  tgbvo.setBestimateflag(zgbs);
						  try {
							iVOPersistence.insertVO(tgbvo);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					  }
//				  }
//			  }
			  System.out.println("++++++++����");
			  }else{
				  showWarningMessage("����"+hvo.getVbillcode()+"�ѷ��͹�\n");
//				  err.append("����"+hvo.getVbillcode()+"�ѷ��͹�\n");
				  return;
			  }
		  }
		  
		  String sql="select * from generalmiddle";
			IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  	List list1 = new ArrayList<Map>();
		  	try {
				list1=(List) receiving.executeQuery(sql, new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String,List> vomap=new HashMap<String,List>();//�������ϴ����ͬvo
			for(int k=0;k<list1.size();k++){
//				TempGeneralBillVO tgbvo = (TempGeneralBillVO) list1.get(k);//����ȡѡ��vo��ѯ��ͬ��key
				TempGeneralBillVO tgbvo=new TempGeneralBillVO();
				Map map=(Map) list1.get(k);
				tgbvo.setQuerykey(map.get("querykey")==null?"":map.get("querykey").toString());
				tgbvo.setVbillcode(map.get("vbillcode")==null?"":map.get("vbillcode").toString());
				tgbvo.setPk_corp(map.get("pk_corp")==null?"":map.get("pk_corp").toString());
				tgbvo.setCbillid(map.get("cbillid")==null?"":map.get("cbillid").toString());
				tgbvo.setCdeptid(map.get("cdeptid")==null?"":map.get("cdeptid").toString());
				tgbvo.setCemployeeid(map.get("cemployeeid")==null?"":map.get("cemployeeid").toString());
				tgbvo.setCoperatorid(map.get("coperatorid")==null?"":map.get("coperatorid").toString());
				tgbvo.setCdispatchid(map.get("cdispatchid")==null?"":map.get("cdispatchid").toString());
				tgbvo.setCcustomvendorid(map.get("ccustomvendorid")==null?"":map.get("ccustomvendorid").toString());
				tgbvo.setCbill_bid(map.get("cbill_bid")==null?"":map.get("cbill_bid").toString());
				tgbvo.setNmoney(map.get("nmoney")==null?0:Double.valueOf(String.valueOf(map.get("nmoney"))));
				tgbvo.setNnumber(map.get("nnumber")==null?0:Double.valueOf(String.valueOf(map.get("nnumber"))));
				tgbvo.setCinventoryid(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString());
				tgbvo.setIrownumber(map.get("irownumber")==null?0:Double.valueOf(String.valueOf(map.get("irownumber"))));
				tgbvo.setBestimateflag(map.get("bestimateflag")==null?"":map.get("bestimateflag").toString());
				tgbvo.setCbilltypecode(map.get("cbilltypecode")==null?"":map.get("cbilltypecode").toString());
				judgeDJBH(map.get("vbillcode").toString(),map.get("pk_corp").toString());
				String key=tgbvo.getQuerykey();
				boolean flag=vomap.containsKey(key);//�жϴ����ͬvo�ļ������Ƿ�������ͬvo
				if(flag){//����У�ȡ��list��vo��add��list�ٴ������
					List list=vomap.get(key);
					list.add(tgbvo);
					vomap.put(key,list);
				}else{//û����ͬ��������list
					List<TempGeneralBillVO> list=new ArrayList<TempGeneralBillVO>();
					list.add(tgbvo);
					vomap.put(key,list);
				}
			}
			if(strs.length()==0){
			for(Entry<String, List> entry:vomap.entrySet()){  //����mapȡ��ÿ��key��Ӧ��list��ÿ��listΪһ������
		        System.out.println(entry.getKey()+"--->"+entry.getValue());
		        List<TempGeneralBillVO> list=new ArrayList<TempGeneralBillVO>();
		        TempGeneralBillVO tgbvo=new TempGeneralBillVO();
		        List vlist=vomap.get(entry.getKey());
		        /*int hnum=0;
		        for(int l=0;l<vlist.size();l++){//����list�е�voͳһ�����
		        	tgbvo=(TempGeneralBillVO) vlist.get(l);
		        	hnum=vlist.size();//��ȡ�к�
		        	assemblyData(hnum,tgbvo,err,l+1);
		        }*/
		        assemblyData(vlist,tgbvo,err);
		    }
			}
	  }
	  if(strs.length()>0){
		  showWarningMessage(strs.toString());
	  }
	  if(err.length()>0){
			showWarningMessage(err.toString());
		}
	  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
	  try {
		ipubdmo.executeUpdate("truncate table generalmiddle");
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  
  public void assemblyData(List vlist,TempGeneralBillVO tgbvo,StringBuffer err){
	  UFDouble totalSum = new UFDouble(0);
	  UFDouble totalNum = new UFDouble(0);
	  JSONObject val = new JSONObject();
	  JSONArray bvals = new JSONArray();
	  for(int l=0;l<vlist.size();l++){//����list�е�voͳһ�����
      	tgbvo=(TempGeneralBillVO) vlist.get(l);
      	String flag = checkRequired(tgbvo,err);  //�ǿռ���
      	if("Y".equals(flag)){
      	int hnum=vlist.size();//��ȡ��ϸ����
      	int lsize=l+1;
      
	//�����ܽ��
	  
//	  for(int i=0;i<hnum;i++){
		  Object  money=tgbvo.getNmoney();
		  UFDouble nmoney = new UFDouble(money.toString());
	      totalSum=totalSum.add(nmoney.doubleValue());
	      System.out.println("money==="+money);
//		}
	//����������
	  
//	  for(int i=0;i<hnum;i++){
		  Object  number=tgbvo.getNnumber();
		  UFDouble nnumber = new UFDouble(number.toString());
		  totalNum=totalNum.add(nnumber.doubleValue());
	      System.out.println("number==="+number);
//		}
	  
	  val.put("BILL_SEQ_ID",tgbvo.getVbillcode());//������ˮ��  ������ˮ��  ʵ�ʵ��ݺ�
  	  String bj="";
  	  if(totalSum.doubleValue()>=0){
  		  bj="1";
  	  }else if(totalSum.doubleValue()<0){
  		  bj="0";
  	  }
  	  val.put("OP_FLAG",bj);//������� -- ���������������
  	  String nowtime=new UFDateTime(System.currentTimeMillis()).toString();
  	  String zgbz=tgbvo.getBestimateflag().toString();//�ݹ���־  N���ݹ�   Y�ݹ�
  	  System.out.println("�ݹ���־����������"+zgbz);
  	  String type = tgbvo.getCbilltypecode();
  	  System.out.println("�������ͣ���������"+type);
  	  String sourcetype=billSource(tgbvo.getCbillid());//��Դ�������� �����۳±���ת-���Σ�
  	  Map map=invoiceInfo(tgbvo.getCbillid());
  	  String lytype=map.get("cupsourcebilltype")==null?"":map.get("cupsourcebilltype").toString();//��Դ��������
  	  if("I2".equals(type)){     //OK
  		  if("N".equals(zgbz)){
  			  type = "�ɹ���ⵥ";   //I01003
  			  err.append("����"+tgbvo.getVbillcode()+"Ϊ���ݹ����ݣ������ױ��\n");
  			  return;
  		  }else if("Y".equals(zgbz)){
  			  /*if("45".equals(lytype)){
  				  type = "��ĩ�ɹ��ݹ�ƥ��";  //I01007  
  			  }else{
  				  type = "��ĩ�ɹ��ݹ����";   //I01011  ͳһ��ΪI01007  
  			  }*/
  			type = "��ĩ�ɹ��ݹ�ƥ��";  //I01007 
  		  }
  	  }else if("I9".equals(type) || "I4".equals(type) || "I7".equals(type) || "IA".equals(type)){     //OK
  		type = "������";  //I03001
  	  }
  	  else if("I5".equals(type)){     //OK
  		  type = "���۳��ⵥ";    //I02001
  		  if("4C".equals(sourcetype)){
  		  }else if("32".equals(sourcetype)){
  		  }else{
  			  err.append("����"+tgbvo.getVbillcode()+"��Դ�������Ͳ����ϴ���ƹ���\n");
			  return;
  		  }
  	  }else if("I6".equals(type)){     //OK
  		  type = "���ó��ⵥ";   //I02002
//  		  Lyck(type,billvo,nowtime);
  	  }else if("I3".equals(type)){     //OK
  		  type = "����Ʒ��ⵥ";       //I01001
//  		  Lyck(type,billvo,nowtime);
  	  }else if("ID".equals(type)){     //OK
  		  type = "ί�мӹ����ⵥ";       //I02003
  	  }else if("IC".equals(type)){     //OK
  		  type = "ί�мӹ���ⵥ";       //I01002
  	  }else if("II".equals(type)){
  		  type = "������ⵥ";         //I01004
  	  }else if("IJ".equals(type)){
  		  type = "�������ⵥ";         //I02004
  	  }
  	send(tgbvo,val,type,totalSum,totalNum,hnum,err,nowtime,lsize,bvals);
      	}
	  }
	  sendBC(val,tgbvo,err);
	  
  }
  
  private void send(TempGeneralBillVO tgbvo,JSONObject val,String type,UFDouble totalSum,UFDouble totalNum,int rows,StringBuffer err,String nowtime,int lsize,JSONArray bvals)
  {
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//��������  billType
      val.put("BILL_ID",tgbvo.getVbillcode());//ʵ�ʵ��ݺ�  ͬͷ��ˮ��  ʵ�ʵ��ݺ�
      val.put("SYS_ID","JC");//��Դϵͳ��
      String pkcorp=tgbvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//���� -- ��˾��   
      String cbillid = tgbvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
      System.out.println("");
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//����� -- ҵ������   ��ͷ���������  ???  ��Ʊ�� ��caccountyear���ֶ�ֻ����  ����ʱ��
      String bm = tgbvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//�������� -- ���ű���ȡmdm����������   ???  mdm8λ����
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//�������� -- ��ǰ����
      val.put("CURRENCY_CODE",imap.get("currtypecode")==null?"CNY":imap.get("currtypecode"));//���ִ���
//      val.put("CURRENCY_CODE","cny");//���ִ���
      if("IA".equals(tgbvo.getCbilltypecode())){   //����������Ϊ����ɱ������������෴�Ľ��
    	  val.put("BILL_AMT",totalSum.doubleValue()*-1);//���ݽ��(ԭ�ң� -- �����ܽ��  ��������Ϊ����ɱ������������෴�Ľ��
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue()*-1);//���ݽ��(������ң�
      }else{
    	  val.put("BILL_AMT",totalSum.doubleValue());//���ݽ��(ԭ�ң� -- �����ܽ��
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue());//���ݽ��(������ң�
      }
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//����˰�� -- �ɹ���Ʊ����˰��  ???
      val.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//˰��
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//���� -- �ɹ���Ʊ�۱�����
//      val.put("BILL_AMT_RMB",totalSum.doubleValue());//���ݽ��(������ң�
      val.put("QUALITY",totalNum.doubleValue());//���� -- ����������
      String psnid = tgbvo.getCemployeeid();
      String pname = queryName(psnid);
      String zdr=tgbvo.getCoperatorid();
      Map map=queryPsn(zdr,pkcorp);
      if(map==null || map.size()==0){
//    	  err.append("����"+tgbvo.getVbillcode()+"δ��ȡ���Ƶ�����Ϣ������ʧ��\n");
//    	  return;
		}
      val.put("OP_CODE","L00337");//����¼���˹��� -- �ȴ�erp����Ա  ������ݣ�L00031  map.get("psncode")==null?"":map.get("psncode")
      val.put("OP_NAME",map.get("psnname")==null?"":map.get("psnname"));//����¼��������-- �ȴ�erp����Ա  ������ݣ�����
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//����¼������ -- ��ǰʱ��
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//��¼�ϴ�ʱ�� -- ��ǰʱ��
      Map jlr=queryPsn(PoPublicUIClass.getLoginUser(),pkcorp);
      val.put("REC_CREATOR",jlr.get("psncode")==null?"":jlr.get("psncode"));//��¼�ϴ���
      hzdy(billType,val,rows,tgbvo);//��ͬ�������Ͳ�ͬ�Զ�����
      System.out.println("val:::::::"+val);

      
//      for (int i = 0; i < rows; i++) {
//    	  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];//��ȡ�����б���ĸ�������VO
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",tgbvo.getVbillcode());//������ˮ��  ͬͷ��ˮ��  tgbvo.getVbillcode()
          bval.put("BILL_SUB_SEQ_ID",lsize);//������ϸ�к�
          bval.put("BILL_TYPE",billType);//��������  billType
//          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(ԭ�ң�
          String nmoney=tgbvo.getNmoney().toString();
          Double nm=Double.parseDouble(nmoney);
          String rmbmoney=tgbvo.getNmoney().toString();
          Double rm=Double.parseDouble(rmbmoney);
          if("IA".equals(tgbvo.getCbilltypecode())){   //����������Ϊ����ɱ������������෴�Ľ�� 
        	  bval.put("BILL_SUB_AMT",nm*-1);//���ݽ��(ԭ�ң� -- �����ܽ��  ��������Ϊ����ɱ������������෴�Ľ��   nm
        	  bval.put("BILL_SUB_AMT_RMB",rm*-1);//���ݽ��(������ң�    rm
          }else{
        	  bval.put("BILL_SUB_AMT",nm);//���ݽ��(ԭ�ң� -- �����ܽ��    nm
        	  bval.put("BILL_SUB_AMT_RMB",rm);//���ݽ��(������ң�    rm
          }
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//����˰��
          bval.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//˰��
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//����
//          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(������ң�
          bval.put("SUB_QUALITY",tgbvo.getNnumber());//����
          bzdy(billType,bval,tgbvo,zrzx);
          
          System.out.println("bval:::::::"+bval);

          bvals.add(bval);
//      }
  val.put("bodylist", bvals);
  /*if(rows!=lsize){
	  return;
  }*/

  
  }
//end
  
  public void sendBC(JSONObject val,TempGeneralBillVO tgbvo,StringBuffer err){
	  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
	  JSONObject s = ifc.assembleItfData(val, "JCBCA1","JCBCA2"); 
	  System.out.println(s);
	  String state = (String) s.get("state"); 
	  
	  if("success".equals(state)){ 
	      String content = s.getString("content"); 
	      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
	      if(sjrr.size()>0){ 
	          for (int i = 0; i < sjrr.size(); i++) { 
	              JSONObject rjb = (JSONObject) sjrr.get(i); 
	              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
	              
	          }
//	          if("success".equals(rs.getString("state"))){ 
	              //�ɹ��߼� 
	        	  System.out.println("########���ͳɹ�########");
//	        	  showWarningMessage("����"+hvo.getVbillcode()+"���ͳɹ�");
	        	  err.append("����"+tgbvo.getVbillcode()+"������\n");
//	        	  showWarningMessage("����"+tgbvo.getVbillcode()+"������\n");
	        	  updateDJBH(tgbvo.getVbillcode(),tgbvo.getPk_corp());
//	          }else{ 
//	              //ʧ���߼� 
//	          }
	      } 
	  }else{
//		  showWarningMessage("����"+hvo.getVbillcode()+"����ʧ��");
		  err.append("����"+tgbvo.getVbillcode()+"����ʧ��\n");
	  }
  }
  
  
  public List queryData(String vbillcode,String pkcorp){
	  String sql="select h.vbillcode,h.cbilltypecode,h.pk_corp,h.cbillid,h.cdeptid,h.cemployeeid," +
	  		"h.coperatorid,h.cdispatchid,h.ccustomvendorid,h.bestimateflag,b.cbill_bid,b.nmoney,b.nnumber,b.cinventoryid,b.irownumber " +
	  		"from ia_bill h inner join ia_bill_b b on h.cbillid=b.cbillid " +
	  		"where h.vbillcode='"+vbillcode+"' and h.pk_corp='"+pkcorp+"' and nvl(h.dr,0)=0";
	  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  	  List list = null;
  	  
  	  try {
			list = (List) receiving.executeQuery(sql, new MapListProcessor());
		  } catch (BusinessException e) {
			  // TODO Auto-generated catch block
			e.printStackTrace();
		  }
		 
	  
	return list;
	  
  }
  
//add by zy 2019-10-10
  public void passbiaocai(){
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result){//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
		  int rows = 0;//ѡ������
		  int [] selectRows = getBillListPanel().getHeadTable().getSelectedRows();//��ȡ�б�״̬��ѡ�����������
		  rows=selectRows.length;
		  //��ȡ��������
//		  int brows=this.getBillCardPanel().getRowCount();//��Ƭ״̬
		  
		  StringBuffer err=new StringBuffer();//�洢��ʾ��Ϣ
		  
		  for(int a=0;a<selectRows.length;a++)
		  {
			  BillVO billvo = null;
			  m_iCurBillPrt = selectRows[a];//�ѵ�ǰѡ���е���Ÿ�����ǰ�����ڻ����е����
			  billvo = m_voBills[m_iCurBillPrt];//��ǰ���ڴ����ĵ���
			  String billCode = ((BillHeaderVO) billvo.getParentVO()).getVbillcode();//���ݺ�
			  if (billvo.getChildrenVO() == null) {
			        //��û�л�ñ�������
			        ClientLink cl = ce.getClientLink();
			        try {
			        	billvo = BillBO_Client.querybillWithOtherTable(null, null,
						      null, null, null, billvo, new Boolean(true), cl)[0];
					} catch (Exception e) {
						e.printStackTrace();
					}
			        }
			  
			  int brows = billvo.getChildrenVO().length;
			//�����ܽ��
			  UFDouble totalSum = new UFDouble(0);
			  for(int i=0;i<brows;i++){
				  Object  money=((BillItemVO) billvo.getChildrenVO()[i]).getNmoney()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNmoney();
				  UFDouble nmoney = new UFDouble(money.toString());
			      totalSum=totalSum.add(nmoney.doubleValue());
			      System.out.println("money==="+money);
				}
			  
			//����������
			  UFDouble totalNum = new UFDouble(0);
			  for(int i=0;i<brows;i++){
				  Object  number=((BillItemVO) billvo.getChildrenVO()[i]).getNnumber()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNnumber();
				  UFDouble nnumber = new UFDouble(number.toString());
				  totalNum=totalNum.add(nnumber.doubleValue());
			      System.out.println("number==="+number);
				}
			  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();//��ͷVO
			  String yn=judgeDJBH(hvo.getVbillcode(),corp);
			  if(yn==null){
			//����
			  for(int b=0;b<rows;b++)
			  {
//				  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[b];//��ȡ�����б���ĸ�������VO
				      String flag = null;
//					  flag = checkRequired(hvo,err);  //�ǿռ���
					  if("Y".equals(flag)){
						//XBUS SEND ���ô���  start 
						  JSONObject val = new JSONObject();
						  	  
						  	  val.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//������ˮ��  ������ˮ��  ʵ�ʵ��ݺ�
						  	  String bj="";
						  	  if(totalSum.doubleValue()>=0){
						  		  bj="1";
						  	  }else if(totalSum.doubleValue()<0){
						  		  bj="0";
						  	  }
						  	  val.put("OP_FLAG",bj);//������� -- ���������������
						  	  String nowtime=new UFDateTime(System.currentTimeMillis()).toString();
						      System.out.println("��ǰʱ�䣺��������������"+nowtime);
						  	  String zgbz=hvo.getBestimateflag().toString();//�ݹ���־  N���ݹ�   Y�ݹ�
						  	  System.out.println("�ݹ���־����������"+zgbz);
//						  	  String lytype=btvo.getCsourcebilltypecode();//��Դ��������
//						  	  System.out.println("��Դ�������ͣ���������"+lytype);
						  	  String type = hvo.getCbilltypecode();
						  	  System.out.println("�������ͣ���������"+type);
						  	  System.out.println("cbillid:::::::"+hvo.getCbillid());
						  	  String sourcetype=billSource(hvo.getCbillid());//��Դ�������� �����۳±���ת-���Σ�
						  	  Map map=invoiceInfo(hvo.getCbillid());
						  	  String lytype=map.get("cupsourcebilltype")==null?"":map.get("cupsourcebilltype").toString();//��Դ��������
						  	  if("I2".equals(type)){     //OK
						  		  if("N".equals(zgbz)){
						  			  type = "�ɹ���ⵥ";   //I01003
						  			  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else if("Y".equals(zgbz)){
						  			  if("45".equals(lytype)){
						  				  type = "��ĩ�ɹ��ݹ�ƥ��";  //I01007  
								  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  			  }else{
						  				  type = "��ĩ�ɹ��ݹ����";   //I01011
							  			  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  			  }
						  		  }
						  	  }else if("I9".equals(type) || "I4".equals(type) || "I7".equals(type) || "IA".equals(type)){     //OK
						  		type = "������";  //I03001
						  		cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }
						  	  else if("I5".equals(type)){     //OK
						  		  type = "���۳��ⵥ";    //I02001
						  		  if("4C".equals(sourcetype)){
						  			cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else if("32".equals(sourcetype)){
						  			cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  		  }else{
						  			  err.append("����"+hvo.getVbillcode()+"��Դ�������Ͳ����ϴ���ƹ���\n");
						  		  }
						  	  }else if("I6".equals(type)){     //OK
						  		  judge(billvo);
						  		  type = "���ó��ⵥ";   //I02002
//						  		  Lyck(type,billvo,nowtime);
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("I3".equals(type)){     //OK
						  		  type = "����Ʒ��ⵥ";       //I01001
//						  		  Lyck(type,billvo,nowtime);
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("ID".equals(type)){     //OK
						  		  type = "ί�мӹ����ⵥ";       //I02003
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("IC".equals(type)){     //OK
						  		  type = "ί�мӹ���ⵥ";       //I01002
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("II".equals(type)){
						  		  type = "������ⵥ";         //I01004
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }else if("IJ".equals(type)){
						  		  type = "�������ⵥ";         //I02004
						  		  cgrk(val,hvo,type,totalSum,totalNum,billvo,brows,err,nowtime);
						  	  }
					  }
					
				  }
			  }else{
//				  showWarningMessage(hvo.getVbillcode()+"�ѷ��͹���");
				  err.append("����"+hvo.getVbillcode()+"�ѷ��͹�\n");
			  }
		  }
		  if(err!=null || err.length()>0){
				showWarningMessage(err.toString());
			}
		  }

  }
  
  /**
   * ���������жϻ��ܵ���
   */
  private BillVO judge(BillVO billvo){
	  
	  BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();
	  //��ȡ������ࡢ���š��շ���𡢹�Ӧ�̡��ͻ���Ϣ
	  List list=query(hvo.getVbillcode());
	  String ch=list.get(0).toString(); //�������
	  String bm=list.get(1).toString();//����
	  String sf=list.get(2).toString();//�շ����
	  String gy=list.get(3).toString();//��Ӧ��
	  
	  
	return billvo;
  }
  
  
  private List query(String vbillcode){
	  List list=null;
	  IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select ibb.cinventoryid,ib.cdeptid,ib.cdispatchid,ib.ccustomvendorid from ia_bill ib " +
				"inner join ia_bill_b ibb on ib.vbillcode=ibb.vbillcode where ib.vbillcode='"+vbillcode+"' " +
				"and ib.pk_corp='1071' and nvl(ib.dr,0)=0";
		try {
			list = (List) query.executeQuery(sql, new ListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return list;
  }
  
  /**
   * ����Ϊ�ɹ���ⵥ���������������
   * add by zy 2019-10-31 
   */
  private void cgrk(JSONObject val,BillHeaderVO hvo,String type,UFDouble totalSum,UFDouble totalNum,BillVO billvo,int rows,StringBuffer err,String nowtime){
	  //��ǰʱ��
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//��������  
      val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//ʵ�ʵ��ݺ�  ͬͷ��ˮ��  ʵ�ʵ��ݺ�
      val.put("SYS_ID","JC");//��Դϵͳ��
      String pkcorp=hvo.getPk_corp()==null?"":hvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//���� -- ��˾��   
      String cbillid = hvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
      System.out.println("");
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//����� -- ҵ������   ��ͷ���������  ???  ��Ʊ�� ��caccountyear���ֶ�ֻ����  ����ʱ��
      String bm = hvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//�������� -- ���ű���ȡmdm����������   ???  mdm8λ����
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//�������� -- ��ǰ����
      val.put("CURRENCY_CODE",imap.get("currtypecode")==null?"CNY":imap.get("currtypecode"));//���ִ���
//      val.put("CURRENCY_CODE","cny");//���ִ���
      if("IA".equals(hvo.getCbilltypecode())){   //����������Ϊ����ɱ������������෴�Ľ��
    	  val.put("BILL_AMT",totalSum.doubleValue()*-1);//���ݽ��(ԭ�ң� -- �����ܽ��  ��������Ϊ����ɱ������������෴�Ľ��
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue()*-1);//���ݽ��(������ң�
      }else{
    	  val.put("BILL_AMT",totalSum.doubleValue());//���ݽ��(ԭ�ң� -- �����ܽ��
    	  val.put("BILL_AMT_RMB",totalSum.doubleValue());//���ݽ��(������ң�
      }
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//����˰�� -- �ɹ���Ʊ����˰��  ???
      val.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//˰��
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//���� -- �ɹ���Ʊ�۱�����
//      val.put("BILL_AMT_RMB",totalSum.doubleValue());//���ݽ��(������ң�
      val.put("QUALITY",totalNum.doubleValue());//���� -- ����������
      String psnid = hvo.getCemployeeid();
      String pname = queryName(psnid);
      String zdr=hvo.getCoperatorid();
      Map map=queryPsn(zdr,pkcorp);
      if(map==null || map.size()==0){
    	  err.append("����"+hvo.getVbillcode()+"δ��ȡ���Ƶ�����Ϣ������ʧ��\n");
    	  return;
		}
      val.put("OP_CODE",map.get("psncode")==null?"":map.get("psncode"));//����¼���˹��� -- �ȴ�erp����Ա  ������ݣ�L00031  
      val.put("OP_NAME",map.get("psnname")==null?"":map.get("psnname"));//����¼��������-- �ȴ�erp����Ա  ������ݣ�����
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//����¼������ -- ��ǰʱ��
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//��¼�ϴ�ʱ�� -- ��ǰʱ��
      Map jlr=queryPsn(PoPublicUIClass.getLoginUser(),pkcorp);
      val.put("REC_CREATOR",jlr.get("psncode")==null?"":jlr.get("psncode"));//��¼�ϴ���
//      hzdy(billType,val,rows);//��ͬ�������Ͳ�ͬ�Զ�����
      System.out.println("val:::::::"+val);

      JSONArray bvals = new JSONArray();
      for (int i = 0; i < rows; i++) {
    	  BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];//��ȡ�����б���ĸ�������VO
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//������ˮ��  ͬͷ��ˮ��
          bval.put("BILL_SUB_SEQ_ID",i+1);//������ϸ�к�
          bval.put("BILL_TYPE",billType);//��������
//          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(ԭ�ң�
          String nmoney=btvo.getNmoney()==null?"0":btvo.getNmoney().toString();
          Double nm=Double.parseDouble(nmoney);
          String rmbmoney=btvo.getNmoney()==null?"0":btvo.getNmoney().toString();
          Double rm=Double.parseDouble(rmbmoney);
          if("IA".equals(hvo.getCbilltypecode())){   //����������Ϊ����ɱ������������෴�Ľ��
        	  bval.put("BILL_SUB_AMT",nm*-1);//���ݽ��(ԭ�ң� -- �����ܽ��  ��������Ϊ����ɱ������������෴�Ľ��
        	  bval.put("BILL_SUB_AMT_RMB",rm*-1);//���ݽ��(������ң�
          }else{
        	  bval.put("BILL_SUB_AMT",nm);//���ݽ��(ԭ�ң� -- �����ܽ��
        	  bval.put("BILL_SUB_AMT_RMB",rm);//���ݽ��(������ң�
          }
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny")==null?0:imap.get("noriginaltaxmny"));//����˰��
          bval.put("TAX_RATE",imap.get("ntaxrate")==null?0:imap.get("ntaxrate"));//˰��
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate")==null?0:imap.get("nexchangeotobrate"));//����
//          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(������ң�
          bval.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//����
//          bzdy(billType,bval,btvo,zrzx);
          
          
          System.out.println("bval:::::::"+bval);

          bvals.add(bval);
      }
  val.put("bodylist", bvals);

  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
  JSONObject s = ifc.assembleItfData(val, "JCBCA1","JCBCA2"); 
  System.out.println(s);
  String state = (String) s.get("state"); 
  if("success".equals(state)){ 
      String content = s.getString("content"); 
      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
      if(sjrr.size()>0){ 
          for (int i = 0; i < sjrr.size(); i++) { 
              JSONObject rjb = (JSONObject) sjrr.get(i); 
              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
          }
//          if("success".equals(rs.getString("state"))){ 
              //�ɹ��߼� 
        	  System.out.println("########���ͳɹ�########");
//        	  showWarningMessage("����"+hvo.getVbillcode()+"���ͳɹ�");
        	  err.append("����"+hvo.getVbillcode()+"���ͳɹ�\n");
        	  updateDJBH(hvo.getVbillcode(),pkcorp);
//          }else{ 
//              //ʧ���߼� 
//          }
      } 
  }else{
//	  showWarningMessage("����"+hvo.getVbillcode()+"����ʧ��");
	  err.append("����"+hvo.getVbillcode()+"����ʧ��\n");
  }
  }
//end
  
  /**
   * ����Ϊ���۳ɱ���ת�����
   * add by zy 2019-10-31 
   */
  private void xscbjz(JSONObject val,BillHeaderVO hvo,BillItemVO btvo,String type,UFDouble totalSum,UFDouble totalNum,int rows,StringBuffer err,String nowtime){
	  //��ǰʱ��
      
  	  String billType=DJLX(type);
  	  val.put("BILL_TYPE",billType);//��������  
      val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//ʵ�ʵ��ݺ�  ͬͷ��ˮ��  ʵ�ʵ��ݺ�
      val.put("SYS_ID","JC");//��Դϵͳ��
      String pkcorp=hvo.getPk_corp()==null?"":hvo.getPk_corp();
      System.out.println("pkcorp::::::::::::"+pkcorp);
      String zt=queryZt(pkcorp);
      val.put("COMPANY_CODE",zt);//���� -- ��˾��   
      String cbillid = hvo.getCbillid();
      Map imap=invoiceInfo(cbillid);
      String cyear=imap.get("caccountyear")==null?"":imap.get("caccountyear").toString();
//      String cmonth=nowtime.substring(5, 7);
//      String cdate=cyear+cmonth;
      val.put("ACCOUNT_PERIOD",nowtime.replace("-", "").substring(0, 6));//����� -- ҵ������   ��ͷ���������  ???  ��Ʊ�� ��caccountyear���ֶ�ֻ����  ����ʱ��
      String bm = hvo.getCdeptid();
      System.out.println("bm:::::::::::"+bm);
      String zrzx = queryzrzx(bm);
      val.put("COST_CENTER",zrzx==null?"":zrzx);//�������� -- ���ű���ȡmdm����������   ???  mdm8λ����
      val.put("BILL_DATE",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 8));//�������� -- ��ǰ����
      val.put("CURRENCY_CODE",imap.get("currtypecode"));//���ִ���
      val.put("BILL_AMT",totalSum.doubleValue());//���ݽ��(ԭ�ң� -- �����ܽ��
      val.put("BILL_TAX_AMT",imap.get("noriginaltaxmny"));//����˰�� -- �ɹ���Ʊ����˰��  ???
      val.put("TAX_RATE",imap.get("ntaxrate"));//˰��
      val.put("CURRENCY_RATE",imap.get("nexchangeotobrate"));//���� -- �ɹ���Ʊ�۱�����
      val.put("BILL_AMT_RMB",totalSum.doubleValue());//���ݽ��(������ң�
      val.put("QUALITY",totalNum.doubleValue());//���� -- ����������
      String psnid = hvo.getCemployeeid();
      String pname = queryName(psnid);
      val.put("OP_CODE","L00031");//����¼���˹��� -- �ȴ�erp����Ա  ������ݣ�L00031  
      val.put("OP_NAME","����");//����¼��������-- �ȴ�erp����Ա  ������ݣ�����
      val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//����¼������ -- ��ǰʱ��
      val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//��¼�ϴ�ʱ�� -- ��ǰʱ��
      val.put("REC_CREATOR","L00031");//��¼�ϴ���
//      hzdy(billType,val,rows);//��ͬ�������Ͳ�ͬ�Զ�����

      JSONArray bvals = new JSONArray();
      for (int i = 0; i < rows; i++) {
          JSONObject bval = new JSONObject();
          bval.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//������ˮ��  ͬͷ��ˮ��
          bval.put("BILL_SUB_SEQ_ID",i+1);//������ϸ�к�
          bval.put("BILL_TYPE",billType);//��������
          bval.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(ԭ�ң�
          bval.put("BILL_SUB_TAX_AMT",imap.get("noriginaltaxmny"));//����˰��
          bval.put("TAX_RATE",imap.get("ntaxrate"));//˰��
          bval.put("CURRENCY_RATE",imap.get("nexchangeotobrate"));//����
          bval.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(������ң�
          bval.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//����
//          bzdy(billType,bval,btvo,zrzx);
          
          System.out.println("val:::::::"+val);
          System.out.println("bval:::::::"+bval);

          bvals.add(bval);
      }
  val.put("bodylist", bvals);

  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
  JSONObject s = ifc.assembleItfData(val, "JCBCA1","JCBCA2"); 
  System.out.println(s);
  String state = (String) s.get("state"); 
  if("success".equals(state)){ 
      String content = s.getString("content"); 
      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
      if(sjrr.size()>0){ 
          for (int i = 0; i < sjrr.size(); i++) { 
              JSONObject rjb = (JSONObject) sjrr.get(i); 
              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
          }
//          if("success".equals(rs.getString("state"))){ 
              //�ɹ��߼� 
        	  System.out.println("########���ͳɹ�########");
//        	  showWarningMessage("����"+hvo.getVbillcode()+"���ͳɹ�");
        	  err.append("����"+hvo.getVbillcode()+"���ͳɹ�\n");
        	  updateDJBH(hvo.getVbillcode(),pkcorp);
//          }else{ 
//              //ʧ���߼� 
//          }
      } 
  }else{
//	  showWarningMessage("����"+hvo.getVbillcode()+"����ʧ��");
	  err.append("����"+hvo.getVbillcode()+"����ʧ��\n");
  }
  }
//end
  
  /*public void Lyck(String type,BillVO billvo,String nowtime){ 
		
	  	StringBuffer err = new StringBuffer();
	  	int  rows = billvo.getChildrenVO().length;
	  	UFDouble totalSum = new UFDouble(0);
	  	 //�����ܽ��		
		  for(int i=0;i<rows;i++){
			  Object  money=((BillItemVO) billvo.getChildrenVO()[i]).getNmoney()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNmoney();
			  UFDouble nmoney = new UFDouble(money.toString());
		      totalSum=totalSum.add(nmoney.doubleValue());
		      System.out.println("money==="+money);
			}
		//����������
		  UFDouble totalNum = new UFDouble(0);
		  for(int i=0;i<rows;i++){
			  Object  number=((BillItemVO) billvo.getChildrenVO()[i]).getNnumber()==null?"0":((BillItemVO) billvo.getChildrenVO()[i]).getNnumber();
			  UFDouble nnumber = new UFDouble(number.toString());
			  totalNum=totalNum.add(nnumber.doubleValue());
		      System.out.println("number==="+number);
			}
	  	BillHeaderVO hvo=(BillHeaderVO) billvo.getParentVO();
	    String cbillid = hvo.getCbillid();
		String billType=DJLX(type);
		String zt=queryZt(hvo.getPk_corp()==null?"":hvo.getPk_corp());
		 String bj="";
	  	  if(totalSum.doubleValue()>0){
	  		  bj="1";
	  	  }else if(totalSum.doubleValue()<0){
	  		  bj="0";
	  	  }	
	    String zrzx = queryzrzx(hvo.getCdeptid()==null?"":hvo.getCdeptid());
		JSONObject val = new JSONObject();
	    val.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//������ˮ��
	    val.put("OP_FLAG",bj);//�������
	    val.put("BILL_TYPE",billType);//��������
	    val.put("BILL_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//ʵ�ʵ��ݺ�
	    val.put("SYS_ID","JC");//��Դϵͳ��
	    val.put("COMPANY_CODE",zt);//����
	    val.put("ACCOUNT_PERIOD",hvo.getTmaketime().toString().replace("-", "").substring(0, 6));//�����
	    val.put("COST_CENTER",zrzx);//��������
	    val.put("BILL_DATE",hvo.getTmaketime().toString().replace("-", "").substring(0, 8));//��������
	    val.put("CURRENCY_CODE","CNY");//���ִ���
	    val.put("BILL_AMT",String.format("%.6f",Double.valueOf(totalSum.toString())));//���ݽ��(ԭ�ң�
	    val.put("BILL_TAX_AMT",0.0);//����˰��
	    val.put("TAX_RATE",0.0);//˰��
	    val.put("CURRENCY_RATE",0.0);//����
	    val.put("BILL_AMT_RMB",String.format("%.6f",Double.valueOf(totalSum.toString())));//���ݽ��(������ң�
	    val.put("QUALITY",String.format("%.6f",Double.valueOf(totalNum.toString())));//����
	    val.put("OP_CODE","L00031");//����¼���˹���
	    val.put("OP_NAME","����");//����¼��������
	    val.put("OP_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", ""));//����¼������
	    val.put("REC_CREATE_TIME",nowtime.replace("-", "").replace(":", "").replace(" ", "").substring(0, 10));//��¼�ϴ�ʱ��
	    val.put("REC_CREATOR","L00031");//��¼�ϴ���
	    hzdy(billType,val,rows);//��ͬ�������Ͳ�ͬ�Զ�����
	    JSONArray bvals = new JSONArray();
	    for (int i = 0; i < rows; i++) {
	    	 BillItemVO btvo = (BillItemVO) billvo.getChildrenVO()[i];
	    	JSONObject val1 = new JSONObject();
	        val1.put("BILL_SEQ_ID",hvo.getVbillcode()==null?"":hvo.getVbillcode());//������ˮ��
	        val1.put("BILL_SUB_SEQ_ID",i+1);//������ϸ�к�
	        val1.put("BILL_TYPE",billType);//��������
	        val1.put("BILL_SUB_AMT",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(ԭ�ң�
	        val1.put("BILL_SUB_TAX_AMT",0.0);//����˰��
	        val1.put("TAX_RATE",0.0);//˰��
	        val1.put("CURRENCY_RATE",0.0);//����
	        val1.put("BILL_SUB_AMT_RMB",btvo.getNmoney()==null?"0":btvo.getNmoney());//���ݽ��(������ң�
	        val1.put("SUB_QUALITY",btvo.getNnumber()==null?"0":btvo.getNnumber());//����
	        bzdy(billType,val1,btvo);
	        bvals.add(val1);
	      

	    }
	    val.put("bodylist", bvals);

		  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
		  JSONObject s = ifc.assembleItfData(val, "JCBCA1","JCBCA2"); 
		  System.out.println(s);
		  String state = (String) s.get("state"); 
		  if("success".equals(state)){ 
		      String content = s.getString("content"); 
		      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
		      if(sjrr.size()>0){ 
		          for (int i = 0; i < sjrr.size(); i++) { 
		              JSONObject rjb = (JSONObject) sjrr.get(i); 
		              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
		          }
	                  //�ɹ��߼� 
	            	  System.out.println("########���ͳɹ�########");

	            	  err.append("����"+hvo.getVbillcode()+"���ͳɹ�\n");
//	            	  updateDJBH(hvo.getVbillcode());

		      } 
		  }else{
			  err.append("����"+hvo.getVbillcode()+"����ʧ��\n");
		  }
		  if(err!=null && err.length()>0){
				showWarningMessage(err.toString());
			}
		

	  

  }*/
  
  
  /**
   * add by yhj 2014-03-31
   */
  private void onButtonAutoPrice()throws Exception{
	  BillVO bvo = new BillVO(getBillCardPanel().getRowCount());
	  getBillCardPanel().getBillValueVO(bvo);
	    if(bvo != null ){
	    	if(bvo.getParentVO().getAttributeValue("cbiztypeid").equals("1016A210000000098ZO3")){
	    		String temp_pk_corp = (String)bvo.getParentVO().getAttributeValue("pk_corp");//��˾
				String temp_cfirstbillbid = null;
				String temp_cinventoryid = null;
	    		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    		CircularlyAccessibleValueObject[] cvos = bvo.getChildrenVO();
		    	if(cvos != null && cvos.length > 0){
		    		for (int i = 0; i < cvos.length; i++) {
		    			String pk_cbill_bid = (String)cvos[i].getAttributeValue("cbill_bid");//���۳ɱ���ת��������
						String temp_dh = (String) cvos[i].getAttributeValue("vfree1");//���۳ɱ���ת���
						UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("nnumber");//���۳ɱ���ת����
						temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//�������
						temp_cfirstbillbid = (String)cvos[i].getAttributeValue("csourcebillid");//Դͷ���ݱ���ID��
						//add by yj 2014-04-04 23:16:17
						if(temp_num.toDouble()>0){//���������㣬ȡ�ɹ����������ۣ���������
							//���ݶ��-��ȡ�������ĵ���
							// add by zip: 2014/5/1
							BigDecimal temp_nprice;
							// ��ȡ�ɹ���Ʊ����
							String sql = "select noriginalcurprice from po_invoice_b where nvl(dr,0)=0 and vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and pk_corp='"+temp_pk_corp+"' and ccurrencytypeid='00010000000000000001'";
							Object obj1 = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(obj1 == null) {
								sql = "select nprice from po_arriveorder_b where vfree1='"+temp_dh+"' and cmangid='"+temp_cinventoryid+"' and nvl(dr,0) = 0 and pk_corp='"+temp_pk_corp+"' and ccurrencytypeid='00010000000000000001' ";
								temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							}else {
								temp_nprice = (BigDecimal) obj1;
							}
							//�������۳ɱ���ת���Ľ��
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "����","���ӹ��ɹ�����"+rowno+"��û�������Ϣ�����˹�ָ������");
								getBillCardPanel().setBodyValueAt(null , i, "nprice");
								getBillCardPanel().setBodyValueAt(null , i, "nmoney");
								cvos[i].setAttributeValue("nprice", null);
								cvos[i].setAttributeValue("nmoney", null);
								return;
							}
							BigDecimal a2 = new BigDecimal(temp_num.toString());
							BigDecimal rest = a2.multiply(temp_nprice).setScale(2,BigDecimal.ROUND_HALF_UP);
							getBillCardPanel().setBodyValueAt(temp_nprice , i, "nprice");
							getBillCardPanel().setBodyValueAt(rest , i, "nmoney");
							cvos[i].setAttributeValue("nprice", temp_nprice);
							cvos[i].setAttributeValue("nmoney", rest.toString());
						}else if(temp_num.toDouble()<0){//����С���㣬ȡ���۳��ⵥ���ۣ����
							//�������۳ɱ���ת�����۳��ⵥ�ӵĹ�����ϵ��ȡ�����۳���ĵ��ۡ������뵽��Ӧ�����۳ɱ���ת����
							//���ݶ��-ȡ���۳��ⵥ��
							String sql = "select nprice from ic_general_b where vfree1='"+temp_dh+"' and cinventoryid='"+temp_cinventoryid+"' and nvl(dr,0) = 0  and noutnum>0 and pk_corp='"+temp_pk_corp+"' and cfirsttype='30' ";
							BigDecimal temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "����","���ӹ����۳����"+rowno+"��û�����۳�����Ϣ,���˹�ָ������");
								return;
							}
							else{
								//�������۳ɱ���ת���Ľ��
								BigDecimal a2 = new BigDecimal(temp_num.toString());
								BigDecimal rest = a2.multiply(temp_nprice).setScale(2,BigDecimal.ROUND_HALF_UP);
								getBillCardPanel().setBodyValueAt(temp_nprice , i, "nprice");
								getBillCardPanel().setBodyValueAt(rest , i, "nmoney");
								cvos[i].setAttributeValue("nprice", temp_nprice);
								cvos[i].setAttributeValue("nmoney", rest.toString());
							}
						}
					}
		    	}
		    	//add by yhj 2014-04-04 23:13:12
		    	//�Ǵ��ӹ�ҵ�����̵ĵĵ��ӣ���������С����ģ�ȡ�����۳��ⵥ�ĵ��ۣ�������С����  == �˻����ӣ�
	    	}else if(!bvo.getParentVO().getAttributeValue("cbiztypeid").equals("1016A210000000098ZO3")){
	    		String temp_pk_corp = (String)bvo.getParentVO().getAttributeValue("pk_corp");//��˾
				String temp_cfirstbillbid = null;
				String temp_cinventoryid = null;
	    		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    		CircularlyAccessibleValueObject[] cvos = bvo.getChildrenVO();
	    		if(cvos != null && cvos.length > 0){
	    			for (int i = 0; i < cvos.length; i++) {
	    				String pk_cbill_bid = (String)cvos[i].getAttributeValue("cbill_bid");//���۳ɱ���ת��������
						String temp_dh = (String) cvos[i].getAttributeValue("vfree1");//���۳ɱ���ת���
						UFDouble temp_num = (UFDouble)cvos[i].getAttributeValue("nnumber");//���۳ɱ���ת����
						temp_cinventoryid = (String)cvos[i].getAttributeValue("cinventoryid");//�������
						temp_cfirstbillbid = (String)cvos[i].getAttributeValue("csourcebillid");//Դͷ���ݱ���ID��
						if(temp_num.toDouble()<0){
							String sql = "select nprice from ic_general_b where vfree1='"+temp_dh+"' and cinventoryid='"+temp_cinventoryid+"' and nvl(dr,0) = 0  and noutnum>0 and pk_corp='"+temp_pk_corp+"' and cfirsttype='30' ";
							BigDecimal temp_nprice = (BigDecimal)iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
							if(temp_nprice==null){
								Integer rowno = i + 1;
								MessageDialog.showErrorDlg(this, "����","���ӹ����۳����"+rowno+"��û�����۳�����Ϣ,���˹�ָ������");
								return;
							}else{
								//�������۳ɱ���ת���Ľ��
								BigDecimal a2 = new BigDecimal(temp_num.toString());
								BigDecimal rest = a2.multiply(temp_nprice).setScale(2,BigDecimal.ROUND_HALF_UP);
								getBillCardPanel().setBodyValueAt(temp_nprice , i, "nprice");
								getBillCardPanel().setBodyValueAt(rest , i, "nmoney");
								cvos[i].setAttributeValue("nprice", temp_nprice);
								cvos[i].setAttributeValue("nmoney", rest.toString());
							}						
						}
	    			}
	    		}
	    	}
	    	//end
	    }
  }
  /**
   * �˵���ѡ�� �������ڣ�(01-2-23 15:03:07)
   */
  public void onMenuItemClick(ActionEvent e) {
    UIMenuItem item = (UIMenuItem) e.getSource();
    if (item == getBillCardPanel().getInsertLineMenuItem()) {
      onButtonInsertLineClicked();
    }
    if (item == getBillCardPanel().getAddLineMenuItem()) {
      onButtonAddLineClicked();
    }
    if (item == getBillCardPanel().getDelLineMenuItem()) {
      onButtonDelLineClicked();
    }
    if (item == getBillCardPanel().getCopyLineMenuItem()) {
      onButtonCopyLineClicked();
    }
    if (item == getBillCardPanel().getPasteLineMenuItem()) {
      onButtonPasteLineClicked();
    }
  }

  /**
   * ��������:������ʾ��Ϣ <p/> ����: <p/> ����ֵ: <p/> �쳣:
   */
  public void postInit() {
    if (m_sBillType == null || m_bd == null)
      return;

    // ���ñ���
    if (m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
        && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
      // �����ڳ�����
      m_sTitle = m_bd.getTitle();
      if (m_sTitle != null && m_sTitle.trim().length() != 0) {
        setTitleText(m_sTitle);
      }
    }
    if (m_bIsSOStart && m_sBillType.equals(ConstVO.m_sBillXSCBJZD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000123")/* @res "���۹���ϵͳ�Ѿ����ã�������㲻���������۳ɱ���ת��" */);
    }
    else if (m_bIsPOStart && m_sBillType.equals(ConstVO.m_sBillCGRKD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000124")/* @res "�ɹ�����ϵͳ�Ѿ����ã�������㲻�����Ӳɹ���ⵥ" */);
    }
    else if (m_bIsSCStart && m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000125")/* @res "ί��ӹ�ϵͳ�Ѿ����ã�������㲻������ί��ӹ��ջ���" */);
    }
    else if (m_bIsICStart && m_sBillType.equals(ConstVO.m_sBillQCRKD) == false
        && m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD) == false) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000126")/* @res "������ϵͳ�Ѿ����ã��������ֻ�����ӵ������ͼ����ϵ���" */);
    }
  }

  /**
   * Gives notification that a portion of the document has been removed. The
   * range is given in terms of what the view last saw (that is, before updating
   * sticky positions).
   * 
   * @param e
   *          the document event
   */
  public void removeUpdate(DocumentEvent e) {
    handleAdjustBillType(e);
  }

  /**
   * ��������:������Դѡ��Ի��򴥷� <p/> ����: java.awt.event.ItemEvent itemEvent ----- �¼� <p/>
   * ����ֵ: <p/> �쳣:
   */
  public void uIComboBoxSource_ItemStateChanged(
      java.awt.event.ItemEvent itemEvent) {
    if (m_bIsChangeEvent && m_iStatus == ADD_STATUS) {
      // ѡ���˵�����Դ,ʹѡ����ⵥ��ť����
      if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
        return;
      }
      if (itemEvent.getItem().toString().equals(
          this.m_ComboItemsVO.name_salebill)) {
        // �����۷�Ʊ
        // �ж�ҵ������
        BillItem bt = getBillCardPanel().getHeadItem("cbiztypeid");
        boolean bIsFQ = false; // �Ƿ��Ƿ����տ�
        boolean bIsWT = false; // �Ƿ���ί�д���
        if (bt != null) {
          String sBizTypeID = bt.getValue();
          if (sBizTypeID != null && sBizTypeID.trim().length() != 0) {
            UIRefPane pane = (UIRefPane) bt.getComponent();
            Object oRule = pane.getRef().getRefModel().getValue("verifyrule");
            if (oRule != null) {
              if (oRule.toString().equals("W")) {
                // ��ί�д���
                bIsWT = true;
              }
              else if (oRule.toString().equals("F")) {
                // �Ƿ����տ�
                bIsFQ = true;
              }
            }
          }
        }
        if (bIsFQ) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000127")/*
                                                 * @res
                                                 * "��ǰ������Դ�����۷�Ʊ��ҵ�������Ƿ����տ���ڸ����˵���ѡ����Դ�ǳ��ⵥ�Ķ�Ӧ���۳ɱ���ת��"
                                                 */);
          // �������У�ճ���У�������
          btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
          btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
        }
        else if (bIsWT) {
          showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "20143010", "UPP20143010-000128")/*
                                                 * @res
                                                 * "��ǰ������Դ�����۷�Ʊ��ҵ��������ί�д��������ڸ����˵���ѡ����Դ�ǳ��ⵥ�Ķ�Ӧ���۳ɱ���ת��"
                                                 */);
          // �������У�ճ���У�������
          btnCtrl.set(false,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(false,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(false,IABtnConst.BTN_LINE_INSERT);
          btnCtrl.set(true,IABtnConst.BTN_CHOOSESALEBILL);
        }
        else {
          btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
          // �������У�ճ���У�������
          btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
          btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
          btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
        }
      }
      else {
        btnCtrl.set(false,IABtnConst.BTN_CHOOSESALEBILL);
        // �������У�ճ���У�������
        btnCtrl.set(true,IABtnConst.BTN_LINE_ADD);
        btnCtrl.set(true,IABtnConst.BTN_LINE_PASTE);
        btnCtrl.set(true,IABtnConst.BTN_LINE_INSERT);
      }
      // �����֯���ֿ⡢ҵ�����͡��շ���𡢲��š�ҵ��Ա�������ˡ��ͻ������޸�
      getBillCardPanel().getHeadItem("crdcenterid").setEdit(true);
      getBillCardPanel().getHeadItem("crdcenterid").setEnabled(true);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEdit(true);
      getBillCardPanel().getHeadItem("cstockrdcenterid").setEnabled(true);
      getBillCardPanel().getHeadItem("cwarehouseid").setEdit(true);
      getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(true);
      getBillCardPanel().getHeadItem("cbiztypeid").setEdit(true);
      getBillCardPanel().getHeadItem("cbiztypeid").setEnabled(true);
      getBillCardPanel().getHeadItem("cdispatchid").setEdit(true);
      getBillCardPanel().getHeadItem("cdispatchid").setEnabled(true);
      getBillCardPanel().getHeadItem("cdeptid").setEdit(true);
      getBillCardPanel().getHeadItem("cdeptid").setEnabled(true);
      getBillCardPanel().getHeadItem("cemployeeid").setEdit(true);
      getBillCardPanel().getHeadItem("cemployeeid").setEnabled(true);
      getBillCardPanel().getHeadItem("cagentid").setEdit(true);
      getBillCardPanel().getHeadItem("cagentid").setEnabled(true);
      getBillCardPanel().getHeadItem("ccustomvendorid").setEdit(true);
      getBillCardPanel().getHeadItem("ccustomvendorid").setEnabled(true);
      // �����޸Ĳ��ֱ�������
      BillItem[] btItemDatas = getBillCardPanel().getBodyItems();
      for (int i = 0; i < btItemDatas.length; i++) {
        if (getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).isEdit()) {
          getBillCardPanel().getBodyItem(btItemDatas[i].getKey()).setEnabled(
              true);
        }
      }
      for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
        // ����csaleadviceid
        getBillCardPanel().getBillModel().setValueAt(null, i, "csaleadviceid");
        // ����ccsaleadviceitemid
        getBillCardPanel().getBillModel().setValueAt(null, i,
            "ccsaleadviceitemid");
      }
      setBtnsForBilltypes(m_aryButtonGroupCard);
    }
    return;
  }

  /**
   * ��������:���������ѡ���� <p/> ����: ValueChangedEvent ----- �¼� <p/> ����ֵ: <p/> �쳣:
   */
  public void valueChanged(ValueChangedEvent event) {
    try {
      Object o = event.getSource();
      String sName = ((UIRefPane) o).getName();
      int iRow = getBillCardPanel().getBillTable().getEditingRow();
      if (sName.equals("cinventorycode")) {
        getBillCardPanel().getBillTable().editingStopped(
            new javax.swing.event.ChangeEvent(this));
        // �������
        String[] sPKs = m_refpaneInv.getRefPKs();
        String[] sCodes = m_refpaneInv.getRefCodes();
        String[] sNames = m_refpaneInv.getRefNames();
        Object[] oInvSpec = (Object[]) m_refpaneInv
            .getRefValues("bd_invbasdoc.invspec");
        Object[] oInvType = (Object[]) m_refpaneInv
            .getRefValues("bd_invbasdoc.invtype");
        Object[] oInvMea = (Object[]) m_refpaneInv
            .getRefValues("bd_measdoc.measname");
        Object[] oInvKind = (Object[]) m_refpaneInv
            .getRefValues("bd_produce.materstate");
        Object[] oInvJHJ = (Object[]) m_refpaneInv
            .getRefValues("bd_produce.jhj");
        Object[] oInvJHJatMandoc = (Object[]) m_refpaneInv
            .getRefValues("bd_invmandoc.planprice");
        Object[] oInvAss = (Object[]) m_refpaneInv
            .getRefValues("bd_invbasdoc.assistunit");
        Object[] oInvPricemethod = (Object[]) m_refpaneInv
            .getRefValues("bd_produce.pricemethod");

        Object[] oInvValues = new Object[10];
        Hashtable ht = new Hashtable(); // ��¼����������Ϣ
        if (sPKs != null && sPKs.length > 0) {
          String sAstUnitFieldName = "";
          if (m_sBillType.equals(ConstVO.m_sBillCGRKD)
              || m_sBillType.equals(ConstVO.m_sBillWWJGSHD)) {
            // �ɹ���ⵥ��ί��ӹ��ջ���ȡ�ɹ���������λ
            sAstUnitFieldName = "pk_measdoc2";
          }
          else if (m_sBillType.equals(ConstVO.m_sBillXSCBJZD)
              || m_sBillType.equals(ConstVO.m_sBillQCXSCBJZD)) {
            // ���۳ɱ���ת��ȡ���۸�������λ
            sAstUnitFieldName = "pk_measdoc1";
          }
          else {
            // ����ȡ��渨������λ
            sAstUnitFieldName = "pk_measdoc3";
          }
          String sInvs = "('";
          for (int i = sPKs.length - 1; i >= 0; i--) {
            oInvValues[0] = sPKs[i];
            oInvValues[8] = oInvAss[i];
            // �жϴ˴���Ƿ��Ǹ���������
            UFBoolean bAstManage = new UFBoolean(false);
            if (oInvValues[8] != null) {
              bAstManage = new UFBoolean(oInvValues[8].toString());
            }
            if (bAstManage.booleanValue()) {
              if (sInvs.equals("('") == false) {
                sInvs = sInvs + "','";
              }
              sInvs = sInvs + sPKs[i];
            }
          }
          if (sInvs.equals("('") == false) {
            sInvs = sInvs + "')";
            // ��������������
            // ��ø�������λ
            StringBuffer sSQL = new StringBuffer(" select ");
            sSQL.append(sAstUnitFieldName
                + ",measname,d.mainmeasrate,d.fixedflag,b.pk_invmandoc");
            sSQL.append(" from ");
            sSQL
                .append(" bd_invbasdoc a,bd_invmandoc b,bd_measdoc c,bd_convert d");
            sSQL.append(" where ");
            sSQL.append(" b.pk_invbasdoc = a.pk_invbasdoc ");
            sSQL.append(" and ");
            sSQL.append(" a.pk_invbasdoc =d.pk_invbasdoc ");
            sSQL.append(" and ");
            sSQL.append(" a." + sAstUnitFieldName + "=c.pk_measdoc ");
            sSQL.append(" and ");
            sSQL.append(" a." + sAstUnitFieldName + "=d.pk_measdoc ");
            sSQL.append(" and ");
            sSQL.append(" b.pk_invmandoc in " + sInvs);
            String[][] sResult = CommonDataBO_Client.queryDataNoTranslate(sSQL
                .toString());
            if (sResult.length != 0) {
              for (int i = 0; i < sResult.length; i++) {
                ht.put(sResult[i][4], sResult[i]);
              }
            }
          }
          for (int i = sPKs.length - 1; i >= 0; i--) {
            if (i != sPKs.length - 1) {
              // ����
              getBillCardPanel().insertLine();
            }
            oInvValues[0] = sPKs[i];
            oInvValues[1] = sCodes[i];
            oInvValues[2] = sNames[i];
            oInvValues[3] = oInvSpec[i];
            oInvValues[4] = oInvType[i];
            oInvValues[5] = oInvMea[i];
            oInvValues[6] = oInvKind[i];
            if (Integer.parseInt((oInvPricemethod[i]).toString()) == ConstVO.JHJ) {
              // �ƻ��۵Ĵ��ֻ��������������ȡ�ƻ���
              oInvValues[7] = oInvJHJ[i];
            }
            else {
              // �Ǽƻ��۴�������������������û�ж���ƻ��ۣ���ʹ�ù��������ж���ļƻ���
              oInvValues[7] = (oInvJHJ[i] != null ? oInvJHJ[i]: oInvJHJatMandoc[i]);
            }
            oInvValues[8] = oInvAss[i];
            oInvValues[9] = oInvPricemethod[i];
            changeInv(oInvValues, iRow, ht);
          }
        }
        else {
          String[] itemkeys = new String[]{
        			"cinventoryid","cinventorycode",	"cinventoryname","cinventoryspec",
        			"cinventorytype", "cinventorymeasname","vbatch","castunitname","castunitid",
        			"nchangerate","nassistnum", "nnumber","nprice","nmoney","nplanedmny",
        			"nplanedprice", "noriginalprice", "cadjustbillitem",	"dbizdate","cinvkind","nexpaybacktax",
        			"vfree0","vfree1",	"vfree2","vfree3","vfree4","vfree5",};
        	for( int i =0 ; i < itemkeys.length; i++){
        		if( getBillCardPanel().getBodyItem(itemkeys[i]) != null){
        			getBillCardPanel().getBillModel().setValueAt(null, iRow,itemkeys[i]);
        		}
        	}
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventoryid");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventorycode");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventoryname");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventoryspec");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventorytype");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "cinventorymeasname");
//          if (getBillCardPanel().getBillModel().getBodyColByKey("castunitname") != -1) {
//            getBillCardPanel().getBillModel().setValueAt(null, iRow,
//                "castunitname");
//          }
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "nplanedprice");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "noriginalprice");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow,
//              "nchangerate");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "dbizdate");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "cinvkind");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree0");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree1");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree2");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree3");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree4");
//          getBillCardPanel().getBillModel().setValueAt(null, iRow, "vfree5");
        }
        // zlq add 20050218 ��ղ��գ��Ա�֤�´λ���ѡ��ͬ���Ĵ��
        m_refpaneInv.setPK(null);
      }
      else if (sName.equals("crdcenterid")) {

        String sRDID = getBillCardPanel().getHeadItem("crdcenterid").getValue();
        String sDate = getBillCardPanel().getHeadItem("dbilldate").getValue();
        if (sRDID != null && sRDID.trim().length() != 0 && sDate != null
            && sDate.trim().length() != 0) {
          // ���ñ����¼�ļƻ��ۼ����κ�
          String[] sInvIDs = new String[getBillCardPanel().getRowCount()];
          if (sInvIDs.length != 0) {
            for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
              String sInvID = (String) getBillCardPanel().getBillModel()
                  .getValueAt(i, "cinventoryid");
              sInvIDs[i] = sInvID;
              // �����κż���ⵥ��ID���
              getBillCardPanel().getBillModel().setValueAt(null, i, "vbatch");
              getBillCardPanel().getBillModel().setValueAt(null, i,
                  "cinbillitemid");
            }
            // ��üƼ۷�ʽ���ƻ���
            String[][] sResults = nc.ui.ia.pub.CommonDataBO_Client.getPrices(
                m_sCorpID, sRDID, sInvIDs);
            // ����ÿ������
            for (int i = 0; i < sInvIDs.length; i++) {
              for (int j = 0; j < sResults.length; j++) {
                if (sInvIDs[i].equals(sResults[j][0])) {
                  // �����м�¼
                  Integer iPrice = new Integer(sResults[j][1]);
                  UFDouble dJHJ = new UFDouble(sResults[j][2]);
                  UFDouble dJHJE = null;
                  if (iPrice.intValue() == 5) {
                    // �Ǽƻ���
                    // ��ô��е�����
                    UFDouble dNumber = null;
                    Object oNumber = getBillCardPanel().getBillModel()
                        .getValueAt(i, "nnumber");
                    if (oNumber != null
                        && oNumber.toString().trim().length() != 0) {
                      dNumber = new UFDouble(oNumber.toString().trim());
                    }
                    if (dJHJ != null && dNumber != null) {
                      // �ƻ����
                      dJHJE = dNumber.multiply(dJHJ);
                      dJHJE = dJHJE
                          .setScale(m_iPeci[2], UFDouble.ROUND_HALF_UP);
                    }
                    getBillCardPanel().getBillModel().setValueAt(dJHJ, i,
                        "nplanedprice");
                    getBillCardPanel().getBillModel().setValueAt(dJHJE, i,
                        "nplanedmny");
                  }
                  else {
                    getBillCardPanel().getBillModel().setValueAt(null, i,
                        "nplanedprice");
                    getBillCardPanel().getBillModel().setValueAt(null, i,
                        "nplanedmny");
                  }
                  break;
                }
              }
            }
          }
        }
      }
      else if (sName.equals("cstockrdcenterid")) {

        String sRDID = getBillCardPanel().getHeadItem("cstockrdcenterid")
            .getValue();
        String sWareID = getBillCardPanel().getHeadItem("cwarehouseid")
            .getValue();
        BillItem bt = m_bd.getHeadItem("cwarehouseid");
        if (sRDID != null && sRDID.trim().length() != 0) {
          // ���òֿ���յ�����

          if (bt != null) {
            // ���òֿ���յ�����
            String sWhere = m_sOldWareCondition + "and pk_calbody = '" + sRDID
                + "'";
            // �Ƿ��Ƿ�Ʒ��
            // if (m_sBillType.equals(ConstVO.m_sBillBFD) == false)
            sWhere = sWhere + "and gubflag = 'N'";
            // else
            // sWhere = sWhere + "and gubflag = 'Y'";
            // û���ݷ�
            sWhere = sWhere + "and sealflag = 'N'";
            ((UIRefPane) bt.getComponent()).setWhereString(sWhere);
          }
          // ���òֿ�
          if (sWareID != null && sWareID.trim().length() != 0) {
            ((UIRefPane) bt.getComponent()).setPK(sWareID);
            if (((UIRefPane) bt.getComponent()).getRefCode() == null) {
              getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
            }
          }
        }
        else {
          getBillCardPanel().getHeadItem("cwarehouseid").setValue("");
          // �ָ��ֿ������ //zlq add 20050305
          ((UIRefPane) bt.getComponent()).setWhereString(m_sOldWareCondition);
        }
        // ���ûس嵥�ݷ�¼
        bt = getBillCardPanel().getBodyItem("cadjustbilltype");
        if (bt != null && iRow != -1) {
          for (int hh = 0; hh < getBillCardPanel().getBillModel().getRowCount(); hh++) {
            getBillCardPanel().setBodyValueAt("", iRow, "cadjustbilltype");
            getBillCardPanel().setBodyValueAt("", iRow, "cadjustbill");
            getBillCardPanel().getBodyItem("cadjustbill").setEdit(false);
            getBillCardPanel().getBodyItem("cadjustbill").setEnabled(false);
          }
        }
        bt = getBillCardPanel().getBodyItem("cadjustbillitem");
        if (bt != null && iRow != -1) {
          for (int hh = 0; hh < getBillCardPanel().getBillModel().getRowCount(); hh++) {
            getBillCardPanel().getBillModel().setValueAt("", hh, "nnumber");
            getBillCardPanel().getBodyItem("cadjustbillitem").setEdit(false);
            getBillCardPanel().getBodyItem("cadjustbillitem").setEnabled(false);
          }
        }
      }
      // �����������õ�����������
      else if (sName.equals("UIRefPaneFreeItem") && m_bIsFreeEvent) {
        nc.vo.scm.ic.bill.FreeVO fvo = getUIRefPaneFreeItem().getFreeVO();
        for (int i = 1; i <= 5; i++) {
          String sName2 = "vfree" + i;
          Object oValue = fvo.getAttributeValue(sName2);
          getBillCardPanel().getBillModel().setValueAt(oValue, iRow, sName2);
        }
        if (m_voCurBill != null && m_voCurBill.getChildrenVO().length > iRow) {
          BillItemVO btvo = (BillItemVO) m_voCurBill.getChildrenVO()[iRow];
          btvo.setVOFree(fvo);
        }
      }
      else if (sName.equals("UIRefPaneAdjustBill")) {
        Object oAdjustBill = getUIRefPaneAdjustBill().getUITextField()
            .getText();
        if (oAdjustBill != null && oAdjustBill.toString().trim().length() != 0) {
          // ��鵥��ID�Ƿ���ȷ
          String sPK = getUIRefPaneAdjustBill().getRefPK();
          try {
            if (sPK == null || sPK.trim().length() == 0) {
              getUIRefPaneAdjustBill().setBlurValue(
                  oAdjustBill.toString().trim());
              if (getUIRefPaneAdjustBill().getRefPK() == null) {
                // showErrorMessage("û�����������" + oAdjustBill + "ƥ��ļ�¼");
                String[] value = new String[] {
                  oAdjustBill.toString()
                };
                showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000170", null, value));

                getBillCardPanel().getBillModel().setValueAt("", iRow,
                    "cadjustbill");
                getBillCardPanel().getBillModel().setValueAt("", iRow,
                    "cadjustbillid");
                getUIRefPaneAdjustBill().setPK(null);
                return;
              }
            }
          }
          catch (Exception exception) {
            // showErrorMessage("û�����������" + oAdjustBill + "ƥ��ļ�¼");
            String[] value = new String[] {
              oAdjustBill.toString()
            };
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000170", null, value));

            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbill");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillid");
            getUIRefPaneAdjustBill().setPK(null);
            return;
          }
          // �����е�����
          getBillCardPanel().getBillModel().setValueAt(sPK, iRow,
              "cadjustbillid");
        }
      }
      else if (sName.equals("UIRefPaneAdjustBillItem")) {
        Object oBillItem = getUIRefPaneAdjustBillItem().getUITextField()
            .getText();
        if (oBillItem != null && oBillItem.toString().trim().length() != 0) {
          // ��鵥��ID�Ƿ���ȷ
          String sPK = getUIRefPaneAdjustBillItem().getRefPK();
          if (sPK == null || sPK.trim().length() == 0) {
            try {
              getUIRefPaneAdjustBillItem().setBlurValue(
                  oBillItem.toString().trim());
              if (getUIRefPaneAdjustBillItem().getRefPK() == null) {
                // showErrorMessage("û���������¼" + oBillItem + "ƥ��ļ�¼");
                String[] value = new String[] {
                  oBillItem.toString()
                };
                showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                    "20143010", "UPP20143010-000170", null, value));

                getBillCardPanel().getBillModel().setValueAt("", iRow,
                    "cadjustbillitem");
                getBillCardPanel().getBillModel().setValueAt("", iRow,
                    "cadjustbillitemid");
                getUIRefPaneAdjustBillItem().setPK(null);
                return;
              }
            }
            catch (Exception exception) {
              // showErrorMessage("û���������¼" + oBillItem + "ƥ��ļ�¼");
              String[] value = new String[] {
                oBillItem.toString()
              };
              showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                  "20143010", "UPP20143010-000170", null, value));

              getBillCardPanel().getBillModel().setValueAt("", iRow,
                  "cadjustbillitem");
              getBillCardPanel().getBillModel().setValueAt("", iRow,
                  "cadjustbillitemid");
              getUIRefPaneAdjustBillItem().setPK(null);
              return;
            }
          }
          if (sPK == null || sPK.trim().length() == 0) {
            sPK = getUIRefPaneAdjustBillItem().getRefPK();
          }
          if (sPK.trim().length() == 0) {
            // showErrorMessage("û���������¼" + oBillItem + "ƥ��ļ�¼");
            String[] value = new String[] {
              oBillItem.toString()
            };
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "20143010", "UPP20143010-000170", null, value));

            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitem");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cadjustbillitemid");
            getUIRefPaneAdjustBillItem().setPK(null);
            return;
          }
          else {
            // �����е�����
            Object sInvID = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("e.pk_invmandoc");
            Object sInvCode = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("d.invcode");
            Object sInvName = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("d.invname");
            Object sInvSpec = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("d.invspec");
            Object sInvType = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("d.invtype");
            Object sMeasName = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("f.measname");
            Object oPlanedPrice = getUIRefPaneAdjustBillItem().getRefModel()
                .getValue("a.nplanedprice");
            if (m_bIsAdjustBill == false) {
              Object sBatch = getUIRefPaneAdjustBillItem().getRefModel()
                  .getValue("a.vbatch");
              Object sPrice = getUIRefPaneAdjustBillItem().getRefModel()
                  .getValue("a.nprice");
              UFDouble dMny = null;
              UFDouble dPlanedMny = null;
              Object oNumber = getBillCardPanel().getBillModel().getValueAt(
                  iRow, "nnumber");
              if (oNumber != null && oNumber.toString().trim().length() != 0) {
                UFDouble dNumber = new UFDouble(oNumber.toString().trim());
                if (sPrice != null && sPrice.toString().trim().length() != 0) {
                  UFDouble dPrice = new UFDouble(sPrice.toString().trim());
                  dMny = dNumber.multiply(dPrice);
                }
                if (oPlanedPrice != null
                    && oPlanedPrice.toString().trim().length() != 0) {
                  UFDouble dPlanedPrice = new UFDouble(oPlanedPrice.toString()
                      .trim());
                  dPlanedMny = dNumber.multiply(dPlanedPrice);
                  dPlanedMny = dPlanedMny.setScale(m_iPeci[2],
                      UFDouble.ROUND_HALF_UP);
                }
              }
              getBillCardPanel().getBillModel().setValueAt(sBatch, iRow,
                  "vbatch");
              getBillCardPanel().getBillModel().setValueAt(sPrice, iRow,
                  "nprice");
              getBillCardPanel().getBillModel().setValueAt(dPlanedMny, iRow,
                  "nplanedmny");
              getBillCardPanel().getBillModel()
                  .setValueAt(dMny, iRow, "nmoney");
              // �������˰ת�����
              calcTransIncomeTaxMny(iRow);
              // �����˻س嵥�ݷ�¼�������κ���Ϣ����ⵥ�ݷ�¼���
              getBillCardPanel().getBillModel().setValueAt("", iRow,
                  "cinbillitemid");
            }
            getBillCardPanel().getBillModel().setValueAt(sPK, iRow,
                "cadjustbillitemid");
            getBillCardPanel().getBillModel().setValueAt(sInvID, iRow,
                "cinventoryid");
            getBillCardPanel().getBillModel().setValueAt(sInvCode, iRow,
                "cinventorycode");
            getBillCardPanel().getBillModel().setValueAt(sInvName, iRow,
                "cinventoryname");
            getBillCardPanel().getBillModel().setValueAt(sInvSpec, iRow,
                "cinventoryspec");
            getBillCardPanel().getBillModel().setValueAt(sInvType, iRow,
                "cinventorytype");
            getBillCardPanel().getBillModel().setValueAt(sMeasName, iRow,
                "cinventorymeasname");
            getBillCardPanel().getBillModel().setValueAt(oPlanedPrice, iRow,
                "nplanedprice");
          }
        }
        else {
          String sBillID = getBillCardPanel().getHeadItem("cadjustbillid")
              .getValue();
          if (sBillID != null && sBillID.trim().length() != 0) {
            // �б��������ݣ��������Ϣ���
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventoryid");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventorycode");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventoryname");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventoryspec");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventorytype");
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "cinventorymeasname");
            getBillCardPanel().getBillModel().setValueAt("", iRow, "nmoney");
            // �������˰ת�����
            calcTransIncomeTaxMny(iRow);
            getBillCardPanel().getBillModel().setValueAt("", iRow,
                "nplanedprice");
            getBillCardPanel().getBillModel()
                .setValueAt("", iRow, "nplanedmny");
          }
          //
          getBillCardPanel().getBillModel().setValueAt("", iRow,
              "cadjustbillitem");
          getBillCardPanel().getBillModel().setValueAt("", iRow,
              "cadjustbillitemid");
          getUIRefPaneAdjustBillItem().setPK(null);
        }
      }
      // 20050525 �رչ̶��ʲ��ʹ���ӿ�
      // else if (sName.equals("UIRefPaneFaCard")) {
      // nc.vo.fa.outer.FaSubequipmentVO[] voFas =
      // getUIRefPaneFacard().getUpdateVos();
      // String sPK = null;
      // if (voFas != null && voFas.length != 0) {
      // sPK = voFas[0].getPk_subequipment();
      // }
      // getBillCardPanel().getBillModel().setValueAt(sPK, iRow, "cfadeviceid");
      // getBillCardPanel().getBillModel().setValueAt(getUIRefPaneFacard().getRefPK(),
      // iRow, "cfacardid");
      // getBillCardPanel().getBillModel().setValueAt(getUIRefPaneFacard().getRefCode(),
      // iRow, "cfadevicecode");
      // getBillCardPanel().getBillModel().setValueAt(getUIRefPaneFacard().getRefName(),
      // iRow, "cfadevicename");
      // getBillCardPanel().getBillModel().setValueAt(voFas, iRow,
      // "cfadevicevo");
      // getBillCardPanel().getBillModel().setRowState(iRow,
      // BillModel.MODIFICATION);
      // }
      // else if (sName.equals("UIRefPaneFaCardEquipment")) {
      // getBillCardPanel().getBillTable().editingStopped(new
      // javax.swing.event.ChangeEvent(this));
      // //�������
      // nc.vo.fa.outer.FaSubequipmentVO[] voFas =
      // getUIRefPaneFacardEquipment().getSelectVos();
      // if (voFas != null && voFas.length > 0) {
      // Object oSourceModule = null;
      // if (m_iStatus == UPDATE_STATUS && m_voCurBill != null) {
      // oSourceModule = ((BillHeaderVO)
      // m_voCurBill.getParentVO()).getCsourcemodulename();
      // }
      // if (oSourceModule != null && voFas.length > 1) {
      // //����ϵͳ����ĵ���
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
      // "UPP20143010-000129")/*@res "�˵�������ϵͳ����������ģ�ֻ��ѡ��һ�й̶��ʲ��ĸ����豸�������"*/);
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadeviceid");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicecode");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicename");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicevo");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cfacardid");
      // return;
      // }
      // for (int i = 0; i < voFas.length; i++) {
      // int iwriteRow = -1;
      // if (i == 0) {
      // iwriteRow = iRow;
      // } else {
      // //����
      // getBillCardPanel().addLine();
      // iwriteRow = getBillCardPanel().getRowCount() - 1;
      // }
      // if (oSourceModule == null || oSourceModule.toString().trim().length()
      // == 0) {
      // //������ϵͳ���뵥��
      // changeSingleInv(voFas[i].getPkinv(), iwriteRow);
      // }
      // getBillCardPanel().getBillModel().setValueAt(voFas[i].getPk_subequipment(),
      // iwriteRow,
      // "cfadeviceid");
      // getBillCardPanel().getBillModel().setValueAt(voFas[i].getAssetcode(),
      // iwriteRow,
      // "cfadevicecode");
      // getBillCardPanel().getBillModel().setValueAt(voFas[i].getAssetname(),
      // iwriteRow,
      // "cfadevicename");
      // getBillCardPanel().getBillModel().setValueAt(voFas[i].getFk_card(),
      // iwriteRow, "cfacardid");
      // nc.vo.fa.outer.FaSubequipmentVO[] fvos = new
      // nc.vo.fa.outer.FaSubequipmentVO[1];
      // fvos[0] = voFas[i];
      // getBillCardPanel().getBillModel().setValueAt(fvos, iwriteRow,
      // "cfadevicevo");
      // }
      // } else {
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadeviceid");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicecode");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicename");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow,
      // "cfadevicevo");
      // getBillCardPanel().getBillModel().setValueAt(null, iRow, "cfacardid");
      // }
      // getBillCardPanel().getBillModel().setRowState(iRow,
      // BillModel.MODIFICATION);
      // }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * BillClientUI ������ע�⡣
   */
  public BillClientUI(
      boolean bInit) {
    super();
    if (bInit) {
      // ������ȡ���ͳ�����˰����������ʱ������ʼ��
      initialize(null);
    }
  }

  /**
   * BillClientUI ������ע�⡣
   */
  public BillClientUI(
      String sTitle, String sTemplet, String sBillType) {
    super();
    m_sTitle = sTitle;
    m_sBillType = sBillType;
    initialize(null);
  }

  /**
   * BillClientUI �������鹹�캯��
   * 
   * @param pk_corp
   *          ��˾
   * @param sBillTypeCode
   *          ��������
   * @param sBusinessTypeID
   *          ҵ������
   * @param sOperatorid
   *          �Ƶ���
   * @param sBillID
   *          ����ID
   */
  // public BillClientUI(String pk_corp, String sBillTypeCode, String
  // sBusinessTypeID, String sOperatorid, String sBillID) {
  // super();
  // m_sBillType = sBillTypeCode;
  //
  // try {
  //
  // //���ò�ѯ��VO
  // BillVO bvo = new BillVO();
  // BillHeaderVO bhvo = new BillHeaderVO();
  // bhvo.setCbillid(sBillID);
  // bvo.setParentVO(bhvo);
  // //��ѯ����
  // ClientLink cl = ce.getClientLink();
  // BillVO[] bills = BillBO_Client.queryByVO(bvo, new Boolean(true),
  // "cbillid",cl);
  // //��ʾ����
  // if (bills != null && bills[0] != null) {
  // bhvo = (BillHeaderVO) bills[0].getParentVO();
  // //��ʼ������
  // initialize(bhvo.getPk_corp());
  // //�������ģ��
  // loadCardTemplet(null, sBillTypeCode);
  // //���ñ���ģ������ʼʱ���ɱ༭
  // getBillCardPanel().setEnabled(false);
  //
  // getBillCardPanel().setBillValueVO(bills[0]);
  // getBillCardPanel().execHeadTailLoadFormulas();
  // getBillCardPanel().getBillModel().execLoadFormula();
  //
  // BillItemVO btvo[] = (BillItemVO[]) bills[0].getChildrenVO();
  // setComboBoxInHeadFromVO(bills[0], true, 0);
  // //������Ʒ
  // for (int i = 0; i < btvo.length; i++) {
  // if (getBillCardPanel().getBodyItem("blargessflag") != null) {
  // setComboBoxInBodyFromVO(btvo[i], true, i);
  // }
  // }
  // } else {
  // showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
  // "UPP20143010-000084")/*@res "δ��ѯ����¼"*/);
  // }
  // } catch (Exception e) {
  // e.printStackTrace();
  // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
  // "UPP20143010-000103")/*@res "��ʼ�����ݳ���"*/ + e.getMessage());
  // }
  // }
  //
  // ���廷��
  private IAEnvironment ce = new IAEnvironment();

  private IABillCardPanel ivjBillCardPanel = null;

  private IABillListPanel ivjBillListPanel = null;

  private IndividualAllotDlg ivjIndividualAllotDlg = null;

  private LocateConditionDlg ivjLocateConditionDlg = null;// ��λ�����Ի���

  //
  //
  private SaleBillsChooseDlg ivjSaleBillsChooseDlg = null;

  private UIComboBox ivjUIComboBoxSource = null;

  private AdjustBillRef ivjUIRefPaneAdjustBill = null;

  private AdjustBillItemRef ivjUIRefPaneAdjustBillItem = null;

  private BatchRef ivjUIRefPaneBatch = null;

  private BillTypeRef ivjUIRefPaneBillType = null;

  private FreeItemRefPane ivjUIRefPaneFreeItem = null;

  private JobRef ivjUIRefPaneJob = null;

  private JobPhaseRef ivjUIRefPaneJobParse = null;

  private WkCenterRef ivjUIRefPaneWkCenter = null;

  //
  private boolean[] m_bBodyEditFlags = null;// ��¼�޸�ʱ����ı༭����


  /*
   * ��ť��
   */
  private ButtonTree buttonTree = null; 
  //
  // ����ʱ�İ�ť
  private ButtonObject[] m_aryButtonGroupCard = null;
//  {
//      buttonTree.getButton(IAButtonConst.BTN_ADD_MANUAL), buttonTree.getButton(IAButtonConst.BTN_BILL_EDIT), buttonTree.getButton(IAButtonConst.BTN_SAVE), buttonTree.getButton(IAButtonConst.BTN_LINE),
//      buttonTree.getButton(IAButtonConst.BTN_BROWSE_TOP), buttonTree.getButton(IAButtonConst.BTN_BROWSE_PREVIOUS), buttonTree.getButton(IAButtonConst.BTN_BROWSE_NEXT), buttonTree.getButton(IAButtonConst.BTN_BROWSE_BOTTOM),
//      buttonTree.getButton(IAButtonConst.BTN_BILL_CANCEL), buttonTree.getButton(IAButtonConst.BTN_BILL_DELETE), buttonTree.getButton(IAButtonConst.BTN_PRINT), buttonTree.getButton(IAButtonConst.BTN_QUERY),
//      buttonTree.getButton(IAButtonConst.BTN_ASSIST_QUERY)
//  };

  // �б�ʱ�İ�ť
  private ButtonObject[] m_aryButtonGroupList = null;
//  {
//      buttonTree.getButton(IAButtonConst.BTN_ADD_MANUAL), buttonTree.getButton(IAButtonConst.BTN_QUERY), buttonTree.getButton(IAButtonConst.BTN_BILL_EDIT), buttonTree.getButton(IAButtonConst.BTN_AUDIT),
//      buttonTree.getButton(IAButtonConst.BTN_BILL_DELETE), buttonTree.getButton(IAButtonConst.BTN_BROWSE_LOCATE), buttonTree.getButton(IAButtonConst.BTN_PRINT), buttonTree.getButton(IAButtonConst.BTN_BROWSE_REFRESH),
//      buttonTree.getButton(IAButtonConst.BTN_ASSIST_QUERY)
//  };

  
  
  //
  private boolean m_bAllowChangeBillMkByOthers = false;// �Ƿ������޸ķǱ����Ƶ��ĵ���

  private boolean m_bKeepOriginalOperator = true;// �Ƿ���ԭʼ�Ƶ���

  private boolean m_bAllowDefinePriceByUser = true; // ���ⵥ�Ƿ��Զ��嵥��

  private int m_iIndiFlag = 1;// �Ƿ�Ҫ���ָ���ָ���Ľ���(1�����֣�2��������)

  //
  // ģ������
  private BillData m_bd = null;// ��Ƭ����

  private BillListData m_bdList = null;// �б�����

  private boolean m_bIsAddChooseButton = false;// �Ƿ������˲��յ��ݰ�ť

  private boolean m_bIsAddImportButton = false;// �Ƿ������˵��밴ť

  private boolean m_bIsAdjustBill = false;// �Ƿ��ǵ��������ǣ���������κţ��񣺼�����κ�

  private UFBoolean m_bIsBeginAccount = null;// �Ƿ��ڳ�����

  private String m_sStartPeriod = null;// ��ʼ����ڼ�

  private UFDate[] m_aBeginEndDates = null;// ��ǰ����ڼ俪ʼ��������

  //
  private boolean m_bIsChangeEvent = true;// �Ƿ񴥷��Ի����¼�,��ʼ��Ϊ���ʾ�����û��༭�Ķ�״̬

  //
  private boolean m_bIsFreeEvent = true;// �Ƿ񴥷��������¼�

  //

  // ��ǵ�������
  private boolean m_bIsInAdjustBill = false;// �Ƿ�����������

  private boolean m_bIsInBill = false;// �Ƿ�����ⵥ

  private boolean m_bIsOutBill = false;// �Ƿ��ǳ��ⵥ

  private boolean m_bIsOtherBill = false;// �Ƿ����������͵�

  private boolean m_bIsPlanedPriceBill = false;// �Ƿ��Ǽƻ��۵�����

  // �ⲿ�ӿ��Ƿ�����
  private boolean m_bIsICStart = false;// �������Ƿ�����

  private boolean m_bIsPOStart = false;// �ɹ������Ƿ�����

  private boolean m_bIsSCStart = false;// ί��ӹ��Ƿ�����

  private boolean m_bIsSOStart = false;// ���۹����Ƿ�����

  //
  private QueryClientDlg m_dlgQuery = null;// ��ѯ�Ի���

  private QueryPlannedPriceDlg m_dlgQueryPlannedPrice = null;// �ƻ��۲�ѯ�Ի���

  private double m_dMaxValue = new UFDouble(999999999999.99999999)
      .doubleValue();

  //
  // private double m_dPriceMaxValue = new
  // UFDouble(9999999999.99).doubleValue();
  //
  private Hashtable m_htInvAndFix = null;// ������������������Ƿ�̶�������֮��Ķ�Ӧ

  private Hashtable m_htInvAndKind = null;// �����֯�������������������������̬֮��Ķ�Ӧ

  //
  private int[] m_iPeci = null;// ���ݾ���

  //
  private UIRefPane m_refpaneInv = null;// ���ڴ������

  private UIRefPane m_refpaneInvBack = null;// ���ڴ������

  protected String m_sBillType = ConstVO.m_sBillCGRKD;// �������ͳ�ʼΪ�ɹ���ⵥ

  // private String m_sOldMeaCondition = ""; //��������������
  private String m_sOldBomCondition = "";// �ɱ������Ĭ������

  private String m_sOldInvCondition = ""; // ���û��������֯ʱ������

  private String m_sOldUserCondition = ""; // ��Աû���貿��ʱ������

  private String m_sOldWareCondition = ""; // �ֿ�û��������֯ʱ������

  private String m_sOldWkCondition = "";// �������ĵ�Ĭ������

  // �����иĶ��Ի���״̬���������û��Ķ��Ի����е�ֵ��Ҫ�����¼�
  //
  private Vector m_vIsEnable = null; // ���ڲ��ճ��ⵥ�������۷�Ʊ

  //
  private AssistantLedgerVO[] m_voAssistantData = null;// ����Ƽ۷���ļƼ۸�����VO

  //
  private BillVO[] m_voBills = null;// ����VO���飬��Ų��������

  protected BillVO m_voCurBill = null;// ��ǰ���ڴ����ĵ���

  protected int m_iCurBillPrt = -1; // ��ǰ�����ڻ����е����

  private ComboItemsVO m_ComboItemsVO = new ComboItemsVO();// ����������������

  private boolean m_bCanPasteLine = false;// �Ƿ����ճ����

  BillSourceListener billSourceListener = new BillSourceListener();

  // ״̬
  /**
   * ��ʼ�޵���״̬
   */
  protected int INIT_STATUS = 0;

  /**
   * ��Ƭ�����״̬
   */
  protected int CARD_STATUS = 1;

  /**
   * �����µ���״̬
   */
  protected int ADD_STATUS = 2;
  /**
   * �޸ĵ���״̬
   */
  protected int UPDATE_STATUS = 3;

  /**
   * �б����״̬
   */
  protected int LIST_STATUS = 4;

  //protected int DELETE_STATUS = 5;

  //
  protected UFBoolean m_bIsCanAddBill = null;// �Ƿ�����ӵ��ݣ��ڳ�����¼�����ã���protected��

  protected int m_iStatus = -1;// ״̬

  protected String m_sCorpID = "-1";// ��˾

  //
  protected String m_sErrorMessage = "";// ������Ϣ��������ʾ

  //
  protected String m_sWTDX = "";// ί�д�����ҵ������

  protected String m_sFQSK = "";// �����տ��ҵ������

  protected String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
      "20143010", "UPP20143010-000061")/* @res "����ά��" */;

  /**
   * ������������Դ��������(���۳ɱ���ת�����õ�)
   */
  protected HashMap m_hmBillId2Sourcebilltypecode = new HashMap(); 

  //
  // �Զ������
  protected DefVO[] m_voHeaddef = null;

  protected DefVO[] m_voBodydef = null;

  //��ť������
  ButtonControl btnCtrl = null;
 
 

  /**
   * ��������:��ÿ���ڳ�������� <p/> ����: sModuleName ----- ģ����Ϣ <p/> ����ֵ: <p/> �쳣:
   */
  private BillVO[] importBills(String sModuleName) throws Exception {
    BillVO[] bvos = new BillVO[0]; // ���ؽ��
    SettledInfoVO[] svos = null; // �������
    // ���ڳ����ݣ�����������ڵ�ǰһ��
    // �ڳ�����
    UFDate dBeginDate = CommonDataBO_Client.getMonthBeginDate(m_sCorpID,
        m_sStartPeriod);
    if (dBeginDate == null) {
      // ����ڼ�û�ж���
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000043")/* @res "���û���ڼ�û�ж���" */);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000043")/* @res "���û���ڼ�û�ж���" */);
      return bvos;
    }
    UFDate dPreDate = dBeginDate.getDateBefore(1);
    if (sModuleName.equals(ConstVO.m_sModuleCodeIC)) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
          "UPP20143010-000044")/* @res "���ڵ���������ڳ����ݣ���ȴ�" */);
      // ��ÿ��ϵͳ�����û���ڼ�
      String sICStartPeriod = "";
      String s[] = nc.ui.sm.createcorp.CreatecorpBO_Client.queryEnabledPeriod(
          m_sCorpID, ConstVO.m_sModuleIC);
      if (s != null && s.length != 0) {
        sICStartPeriod = s[0] + "-" + s[1];
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("20143010",
      "UPP20143010-000046")/* @res "���������ڳ����ݣ���ȴ�" */);
      if (sICStartPeriod.compareTo(m_sStartPeriod) < 0) {
        // ��������ã����������
        Log.info("��������ã����������,����������");
        // ��ÿ������(���շ���𣫿����֯���ֿ⣫�������)
        svos = CommonDataBO_Client.getOnHand(m_sCorpID, ce.getBusinessDate());
        if (svos != null) {
          for (int i = 0; i < svos.length; i++) {
            svos[i].setM_sSourceModule(ConstVO.m_sModuleIC);
          }
        }
        if (svos != null && svos.length > 0) {
          bvos =  ChgICVOToIAVO.changeICSettledToIAVOs(svos, dPreDate,ce,m_sCorpID,m_iPeci);
        }
      }
      else if (sICStartPeriod.equals(m_sStartPeriod)) {
        // �������ͬʱ����
        Log.info("�������ͬʱ���ã������ڳ���������");
        // ��ÿ������(���շ���𣫿����֯���ֿ⣫�������)
        Object oData[] = CommonDataBO_Client.queryICInitBills(m_sCorpID);
        if (oData != null && oData.length != 0) {
          GeneralBillVO[] gvos = new GeneralBillVO[oData.length];
          for (int hhh = 0; hhh < gvos.length; hhh++) {
            gvos[hhh] = (GeneralBillVO) oData[hhh];
          }
          bvos = ChgICVOToIAVO.changeICVOsToIAVOs(gvos, dPreDate,ce,m_sCorpID,m_iPeci);
        }
      }
    }
    return bvos;
  }
  
  /**
   * ���ݲ���id��ѯMDM��������
   * add by zy 2019-10-11
   * @param bm
   * @return
   */
  private String queryzrzx(String bm){
	  String deptcode = "";
      String sql = "select deptcode from bd_deptdoc where pk_deptdoc='"+bm+"'";
      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      try {
    	  deptcode = (String) receiving.executeQuery(sql, new ColumnProcessor());
      } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
	  return deptcode;
  }
  
  /**
   * �жϵ�������
   * @return
   */
  public String DJLX(String nx){
   if(nx == null){
    return "";
   }
   IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
   String sql = "select typeno from MDMBillType where instructions = '"+nx+"'";
   String string = "";
   try {
    Object list = iquery .executeQuery(sql.toString(),new ColumnProcessor());
    string = list==null?"":list.toString();
   } catch (BusinessException e) {
    e.printStackTrace();
   }
   return string;
  }
  
  /**
   * ����ҵ��Աid��ѯ����
   * add by zy 2019-10-12
   * @param bm
   * @return
   */
  private String queryName(String pid){
	  String name = "";
      String sql = "select psnname from bd_psndoc where pk_psndoc='"+pid+"'";
      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      try {
    	  Object deptcode = receiving.executeQuery(sql, new ColumnProcessor());
    	  name=deptcode==null?"":deptcode.toString();
      } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
	  return name;
  }
  
  /**
   * ����pk_corp��ѯ����
   * add by zy 2019-10-14
   * @param 
   * @return
   */
  private String queryZt(String cid){
	  String zt = "";
      String sql = "select def8 from bd_corp where pk_corp='"+cid+"'";
      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      try {
    	  Object deptcode = receiving.executeQuery(sql, new ColumnProcessor());
    	  zt=deptcode==null?"":deptcode.toString();
      } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
	  return zt;
  }
  
  /**
   * ����cbillid��ѯ�ɹ���Ʊ��Ϣ��˰�˰��...��
   * add by zy 2019-10-15
   * @param cid
   * @return
   */
  private Map invoiceInfo(String cid){
	  String sql = "select invb.cupsourcebilltype,cur.currtypecode,invb.nexchangeotobrate,invb.ntaxrate,invb.noriginaltaxmny,invh.caccountyear from ia_bill h " +
	  		"inner join ia_bill_b b on h.cbillid=b.cbillid " +
	  		"inner join po_settlebill poh on poh.csettlebillid = b.csourcebillid " +
	  		"inner join po_settlebill_b pob on pob.csettlebillid = poh.csettlebillid " +
	  		"inner join po_invoice invh on invh.cinvoiceid = pob.cinvoiceid inner " +
	  		"join po_invoice_b invb on invh.cinvoiceid = invb.cinvoiceid " +
	  		"inner join bd_currtype cur on invb.ccurrencytypeid = cur.pk_currtype " +
	  		"where h.cbillid='"+cid+"'";
	  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  	  List receivingList = null;
  	  Map<String,String> receivingMap=new HashMap<String,String>();
  	  try {
			receivingList = (List) receiving.executeQuery(sql, new MapListProcessor());
		  } catch (BusinessException e) {
			  // TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  if(receivingList.size()==0){
			  return receivingMap;
		  }
	  receivingMap = (Map) receivingList.get(0);
	  return receivingMap;  
  }
  
  /**
   * ����cbillid��ѯ��Դ��������
   * @param cid
   * @return
   */
  private String billSource(String cid){
	  String source="";
	  List list=null;
      String sql = "select b.csourcebilltypecode from ia_bill h inner join ia_bill_b b on h.cbillid=b.cbillid where h.cbillid='"+cid+"'";
      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
      try {
    	  list = (List) receiving.executeQuery(sql, new ColumnListProcessor());
    	  source=list.get(0)==null?"":list.get(0).toString();
      } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
	  
	return source;
  }
  
  /**
	 * �ж� ia_bill ��  �Ƿ�ΪY,���ΪY,�����ٴ��ڶ���,���Ϊ�պ�N�򻹿����ٴ�
	 * wy
	 * 2019��10��20�� 
	 */
	public String judgeDJBH(String dbh,String pkcorp){
	    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select passbc from ia_bill where vbillcode = '"+dbh+"' and nvl(dr,0)=0 and pk_corp='"+pkcorp+"'";
		String djbh = "";
		try {
			djbh = (String) receiving.executeQuery(sql, new ColumnProcessor());		
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return djbh;
	}
	public void updateDJBH(String djbh,String pkcorp){			
		IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());			 
	    String sql = "update ia_bill set passbc = 'Y' where vbillcode = '"+djbh+"' and nvl(dr,0)=0 and pk_corp='"+pkcorp+"'";
	    try {
			ipubdmo.executeUpdate(sql);
			System.out.println(djbh+" passbc�޸ĳɹ�");
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println(djbh+" passbc�޸�ʧ��");
		}		
	}
	
	/**
	 * ���ݵ�ǰ��¼�û���ȡ��ǰ��¼Ա��������ѯ����������
	 * @param user
	 * @return
	 */
	public Map queryPsn(String user,String pkcorp){
		List list=null;
		Map map=new HashMap();
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select p.psnname,p.psncode from bd_psndoc p inner join sm_user u on p.psnname=u.user_name " +
				"where u.cuserid ='"+user+"' and p.pk_corp='"+pkcorp+"' and nvl(p.dr,0)=0";
		try {
			list = (List) query.executeQuery(sql, new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list==null || list.size()==0){
	    	  return map;
			}
		map=(Map) list.get(0);
		return map;
	}
	
	/**
	 * У������ֶ��Ƿ�Ϊ��
	 * 2019-10-29  by zy
	 * @return
	 */
	private String checkRequired(TempGeneralBillVO hvo,StringBuffer err){
		String flag = "Y";
		String djh=hvo.getVbillcode();
		if(hvo.getVbillcode()==null || "".equals(hvo.getVbillcode())){
//			showWarningMessage("���ݺ�Ϊ�գ���ά��");
			err.append("����"+djh+"���ݺ�Ϊ�գ���ά��\n");
			return flag = "N";
		}else if(hvo.getCdeptid()==null || "".equals(hvo.getCdeptid())){
//			showWarningMessage("����"+djh+"����Ϊ�գ���ά��");
			err.append("����"+djh+"����Ϊ�գ���ά��\n");
			return flag = "N";
		}
		return flag;
	}
	
	/**
	   * ���ϳ��ⵥ�¸���ҳ���������ѯ�������
	   * @return
	   */
	  public String queryChfl(String invcode){
		  String chType="";
		  String sql = "select inv.invclasscode,inv.invclassname from bd_invbasdoc doc inner join bd_invcl inv on doc.pk_invcl = inv.pk_invcl where doc.invcode='"+invcode+"'and nvl(doc.dr,0)=0";
	      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	      try {
	    	  Object deptcode = receiving.executeQuery(sql, new ColumnProcessor());
	    	  chType=deptcode==null?"":deptcode.toString();
	      } catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
		return chType;
	  }
  //��ͷ�Զ�����
  private JSONObject hzdy(String billType,JSONObject val,int rows,TempGeneralBillVO tgbvo){
	  if("I01007".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//Ԥ���ַ���1   ���ʱ��
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//Ԥ���ַ���2   ƾ֤ժҪ1
	      val.put("MAIN_RESERVE_C3","");//Ԥ���ַ���3   ƾ֤ժҪ2
	      val.put("MAIN_RESERVE_C4","");//Ԥ���ַ���4   ƾ֤ժҪ3
	      val.put("MAIN_RESERVE_C5","Z");//Ԥ���ַ���5   ƾ֤����
	      val.put("MAIN_RESERVE_C6","");//Ԥ���ַ���6   ��������
	      val.put("MAIN_RESERVE_C7","");//Ԥ���ַ���7   �ɹ�����
	      val.put("MAIN_RESERVE_C8","");//Ԥ���ַ���8   ��֧��λ��֯����
	      val.put("MAIN_RESERVE_C9","");//Ԥ���ַ���9
	      val.put("MAIN_RESERVE_C10","");//Ԥ���ַ���10
	      val.put("MAIN_RESERVE_C11","");//Ԥ���ַ���11
	      val.put("MAIN_RESERVE_C12","");//Ԥ���ַ���12
	      val.put("MAIN_RESERVE_C13","");//Ԥ���ַ���13
	      val.put("MAIN_RESERVE_C14","");//Ԥ���ַ���14
	      val.put("MAIN_RESERVE_C15","");//Ԥ���ַ���15
	      val.put("MAIN_RESERVE_C16","");//Ԥ���ַ���16
	      val.put("MAIN_RESERVE_C17","");//Ԥ���ַ���17
	      val.put("MAIN_RESERVE_C18","");//Ԥ���ַ���18
	      val.put("MAIN_RESERVE_C19","");//Ԥ���ַ���19
	      val.put("MAIN_RESERVE_C20","");//Ԥ���ַ���21
	      val.put("MAIN_RESERVE_C22","");//Ԥ���ַ���22
	      val.put("MAIN_RESERVE_C23","");//Ԥ���ַ���23
	      val.put("MAIN_RESERVE_C24","");//Ԥ���ַ���24
	      val.put("MAIN_RESERVE_C25","");//Ԥ���ַ���25
	      val.put("MAIN_RESERVE_C26","");//Ԥ���ַ���26
	      val.put("MAIN_RESERVE_C27","");//Ԥ���ַ���27
	      val.put("MAIN_RESERVE_C28","");//Ԥ���ַ���28
	      val.put("MAIN_RESERVE_C29","");//Ԥ���ַ���29
	      val.put("MAIN_RESERVE_C30","");//Ԥ���ַ���30
	      val.put("MAIN_RESERVE_N1",rows);//Ԥ��������1  -- ��ϸ������  rows	
	      val.put("MAIN_RESERVE_N2",0);//Ԥ��������2
	      val.put("MAIN_RESERVE_N3",0);//Ԥ��������3
	      val.put("MAIN_RESERVE_N4",0);//Ԥ��������4
	      val.put("MAIN_RESERVE_N5",0);//Ԥ��������5
	  }else if("I02002".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//Ԥ���ַ���1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//Ԥ���ַ���2
	      val.put("MAIN_RESERVE_C3","");//Ԥ���ַ���3
	      val.put("MAIN_RESERVE_C4","");//Ԥ���ַ���4
	      val.put("MAIN_RESERVE_C5","Z");//Ԥ���ַ���5  ƾ֤���  R�տ� P���� Zת��
	      val.put("MAIN_RESERVE_C6","");//Ԥ���ַ���6
	      val.put("MAIN_RESERVE_C7","");//Ԥ���ַ���7
	      val.put("MAIN_RESERVE_C8","");//Ԥ���ַ���8
	      val.put("MAIN_RESERVE_C9","");//Ԥ���ַ���9
	      val.put("MAIN_RESERVE_C10","");//Ԥ���ַ���10
	      val.put("MAIN_RESERVE_C11","");//Ԥ���ַ���11
	      val.put("MAIN_RESERVE_C12","");//Ԥ���ַ���12
	      val.put("MAIN_RESERVE_C13","");//Ԥ���ַ���13
	      val.put("MAIN_RESERVE_C14","");//Ԥ���ַ���14
	      val.put("MAIN_RESERVE_C15","");//Ԥ���ַ���15
	      val.put("MAIN_RESERVE_C16","");//Ԥ���ַ���16
	      val.put("MAIN_RESERVE_C17","");//Ԥ���ַ���17
	      val.put("MAIN_RESERVE_C18","");//Ԥ���ַ���18
	      val.put("MAIN_RESERVE_C19","");//Ԥ���ַ���19
	      val.put("MAIN_RESERVE_C20","");//Ԥ���ַ���21
	      val.put("MAIN_RESERVE_C22","");//Ԥ���ַ���22
	      val.put("MAIN_RESERVE_C23","");//Ԥ���ַ���23
	      val.put("MAIN_RESERVE_C24","");//Ԥ���ַ���24
	      val.put("MAIN_RESERVE_C25","");//Ԥ���ַ���25
	      val.put("MAIN_RESERVE_C26","");//Ԥ���ַ���26
	      val.put("MAIN_RESERVE_C27","");//Ԥ���ַ���27
	      val.put("MAIN_RESERVE_C28","");//Ԥ���ַ���28
	      val.put("MAIN_RESERVE_C29","");//Ԥ���ַ���29
	      val.put("MAIN_RESERVE_C30","");//Ԥ���ַ���30
	      val.put("MAIN_RESERVE_N1",rows);//Ԥ��������1  -- ��ϸ������  rows
	      val.put("MAIN_RESERVE_N2",0);//Ԥ��������2
	      val.put("MAIN_RESERVE_N3",0);//Ԥ��������3
	      val.put("MAIN_RESERVE_N4",0);//Ԥ��������4
	      val.put("MAIN_RESERVE_N5",0);//Ԥ��������5
	  }else if("I01001".equals(billType)){
		  val.put("MAIN_RESERVE_C1","");//Ԥ���ַ���1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//Ԥ���ַ���2
	      val.put("MAIN_RESERVE_C3","");//Ԥ���ַ���3
	      val.put("MAIN_RESERVE_C4","");//Ԥ���ַ���4
	      val.put("MAIN_RESERVE_C5","Z");//Ԥ���ַ���5  ƾ֤���  R�տ� P���� Zת��
	      val.put("MAIN_RESERVE_C6","");//Ԥ���ַ���6  �������������Ƽ��ŷ�Ʊ�������ж�Ҫ�У�
	      val.put("MAIN_RESERVE_C7","");//Ԥ���ַ���7
	      val.put("MAIN_RESERVE_C8","");//Ԥ���ַ���8
	      val.put("MAIN_RESERVE_C9","");//Ԥ���ַ���9
	      val.put("MAIN_RESERVE_C10","");//Ԥ���ַ���10
	      val.put("MAIN_RESERVE_C11","");//Ԥ���ַ���11
	      val.put("MAIN_RESERVE_C12","");//Ԥ���ַ���12
	      val.put("MAIN_RESERVE_C13","");//Ԥ���ַ���13
	      val.put("MAIN_RESERVE_C14","");//Ԥ���ַ���14
	      val.put("MAIN_RESERVE_C15","");//Ԥ���ַ���15
	      val.put("MAIN_RESERVE_C16","");//Ԥ���ַ���16
	      val.put("MAIN_RESERVE_C17","");//Ԥ���ַ���17
	      val.put("MAIN_RESERVE_C18","");//Ԥ���ַ���18
	      val.put("MAIN_RESERVE_C19","");//Ԥ���ַ���19
	      val.put("MAIN_RESERVE_C20","");//Ԥ���ַ���21
	      val.put("MAIN_RESERVE_C22","");//Ԥ���ַ���22
	      val.put("MAIN_RESERVE_C23","");//Ԥ���ַ���23
	      val.put("MAIN_RESERVE_C24","");//Ԥ���ַ���24
	      val.put("MAIN_RESERVE_C25","");//Ԥ���ַ���25
	      val.put("MAIN_RESERVE_C26","");//Ԥ���ַ���26
	      val.put("MAIN_RESERVE_C27","");//Ԥ���ַ���27
	      val.put("MAIN_RESERVE_C28","");//Ԥ���ַ���28
	      val.put("MAIN_RESERVE_C29","");//Ԥ���ַ���29
	      val.put("MAIN_RESERVE_C30","");//Ԥ���ַ���30
	      val.put("MAIN_RESERVE_N1",rows);//Ԥ��������1  -- ��ϸ������  rows
	      val.put("MAIN_RESERVE_N2",0);//Ԥ��������2
	      val.put("MAIN_RESERVE_N3",0);//Ԥ��������3
	      val.put("MAIN_RESERVE_N4",0);//Ԥ��������4
	      val.put("MAIN_RESERVE_N5",0);//Ԥ��������5
	  }else{
		  val.put("MAIN_RESERVE_C1","");//Ԥ���ַ���1 
	      val.put("MAIN_RESERVE_C2",tgbvo.getVbillcode()==null?"":tgbvo.getVbillcode());//Ԥ���ַ���2
	      val.put("MAIN_RESERVE_C3","");//Ԥ���ַ���3
	      val.put("MAIN_RESERVE_C4","");//Ԥ���ַ���4
	      val.put("MAIN_RESERVE_C5","Z");//Ԥ���ַ���5  
	      val.put("MAIN_RESERVE_C6","");//Ԥ���ַ���6
	      val.put("MAIN_RESERVE_C7","");//Ԥ���ַ���7
	      val.put("MAIN_RESERVE_C8","");//Ԥ���ַ���8
	      val.put("MAIN_RESERVE_C9","");//Ԥ���ַ���9
	      val.put("MAIN_RESERVE_C10","");//Ԥ���ַ���10
	      val.put("MAIN_RESERVE_C11","");//Ԥ���ַ���11
	      val.put("MAIN_RESERVE_C12","");//Ԥ���ַ���12
	      val.put("MAIN_RESERVE_C13","");//Ԥ���ַ���13
	      val.put("MAIN_RESERVE_C14","");//Ԥ���ַ���14
	      val.put("MAIN_RESERVE_C15","");//Ԥ���ַ���15
	      val.put("MAIN_RESERVE_C16","");//Ԥ���ַ���16
	      val.put("MAIN_RESERVE_C17","");//Ԥ���ַ���17
	      val.put("MAIN_RESERVE_C18","");//Ԥ���ַ���18
	      val.put("MAIN_RESERVE_C19","");//Ԥ���ַ���19
	      val.put("MAIN_RESERVE_C20","");//Ԥ���ַ���21
	      val.put("MAIN_RESERVE_C22","");//Ԥ���ַ���22
	      val.put("MAIN_RESERVE_C23","");//Ԥ���ַ���23
	      val.put("MAIN_RESERVE_C24","");//Ԥ���ַ���24
	      val.put("MAIN_RESERVE_C25","");//Ԥ���ַ���25
	      val.put("MAIN_RESERVE_C26","");//Ԥ���ַ���26
	      val.put("MAIN_RESERVE_C27","");//Ԥ���ַ���27
	      val.put("MAIN_RESERVE_C28","");//Ԥ���ַ���28
	      val.put("MAIN_RESERVE_C29","");//Ԥ���ַ���29
	      val.put("MAIN_RESERVE_C30","");//Ԥ���ַ���30
	      val.put("MAIN_RESERVE_N1",rows);//Ԥ��������1  -- ��ϸ������  rows
	      val.put("MAIN_RESERVE_N2",0);//Ԥ��������2
	      val.put("MAIN_RESERVE_N3",0);//Ԥ��������3
	      val.put("MAIN_RESERVE_N4",0);//Ԥ��������4
	      val.put("MAIN_RESERVE_N5",0);//Ԥ��������5
	  }
	  
	return val;
  }
  
//�����Զ�����
  private JSONObject bzdy(String billType,JSONObject bval,TempGeneralBillVO tgbvo,String zrzx){
	  
	  Map map=queryBody(tgbvo.getCbill_bid(),tgbvo.getPk_corp());
	  if("I01007".equals(billType)){
//		  bval.put("DETAIL_RESERVE_C1",btvo.getVbomcode()==null?"":btvo.getVbomcode());//Ԥ���ַ���1   ���ϴ���  -- ��Ʒ
		  bval.put("DETAIL_RESERVE_C1",map.get("vbomcode")==null?"":map.get("vbomcode").toString());//Ԥ���ַ���1   ���ϴ���  -- ��Ʒ
//		  bval.put("DETAIL_RESERVE_C1","");//Ԥ���ַ���1   ���ϴ���  -- ��Ʒ
	      bval.put("DETAIL_RESERVE_C2",zrzx);//Ԥ���ַ���2     zrzx   "BCGRCWKF"
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3   ��������
	      bval.put("DETAIL_RESERVE_C4","");//Ԥ���ַ���4   ����ϸ��
	      bval.put("DETAIL_RESERVE_C5","");//Ԥ���ַ���5   ҵ�����һ
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6   ҵ������
	      String kscode=tgbvo.getCcustomvendorid()==null?"":tgbvo.getCcustomvendorid().toString();
	      String ks = queryks(kscode);
	      bval.put("DETAIL_RESERVE_C7",ks);//Ԥ���ַ���7   ��Ӧ�̴���
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","250101");//Ԥ���ַ���10  �������
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13   ��ͬ��
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15   ҵ����������
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16   ��������
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17   ���̴���
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20   ��ҿ��̴���
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26   ҵ�����
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27   ҵ��ϸ��
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }else if("I02002".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//Ԥ���ַ���1
	      bval.put("DETAIL_RESERVE_C2","BCANCZSC");//Ԥ���ַ���2  ��������  zrzx
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3  
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//Ԥ���ַ���4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString());
	      bval.put("DETAIL_RESERVE_C4","250101");//Ԥ���ַ���4  ��д����ȡchtype
	      bval.put("DETAIL_RESERVE_C5","BCANCZSC"+"10001");//Ԥ���ַ���5  ��������+�ɱ���Ŀ��10001��zrzx
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6
	      bval.put("DETAIL_RESERVE_C7","");//Ԥ���ַ���7
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","250101");//Ԥ���ַ���10   �������  �������  û��������Ĭ��  250101
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }else if("I02001".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//Ԥ���ַ���1
	      bval.put("DETAIL_RESERVE_C2",zrzx);//Ԥ���ַ���2  ��������
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//Ԥ���ַ���4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString()); 
	      bval.put("DETAIL_RESERVE_C4","250101");//Ԥ���ַ���4  �������  û��������Ĭ��  250101
	      bval.put("DETAIL_RESERVE_C5","");//Ԥ���ַ���5
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6
	      String kscode=tgbvo.getCcustomvendorid()==null?"":tgbvo.getCcustomvendorid().toString();
	      String ks = queryks(kscode);
	      bval.put("DETAIL_RESERVE_C7",ks);//Ԥ���ַ���7
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","250101");//Ԥ���ַ���10  �������  �������  û��������Ĭ��  250101
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }else if("I01001".equals(billType)){
		  bval.put("DETAIL_RESERVE_C1","");//Ԥ���ַ���1
	      bval.put("DETAIL_RESERVE_C2","BCANCZSC");//Ԥ���ַ���2  ��������  zrzx
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3  
//	      String chtype=queryChfl(btvo.getCinventorycode());
//	      System.out.println("chtype::::::::::"+chtype);
//	      bval.put("DETAIL_RESERVE_C4",chtype);//Ԥ���ַ���4
	      String chtype=querychtype(map.get("cinventoryid")==null?"":map.get("cinventoryid").toString());
	      bval.put("DETAIL_RESERVE_C4","250101");//Ԥ���ַ���4  ��д����ȡchtype
	      bval.put("DETAIL_RESERVE_C5","BCANCZSC"+"10001");//Ԥ���ַ���5  ��������+�ɱ���Ŀ��10001��zrzx
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6
	      bval.put("DETAIL_RESERVE_C7","");//Ԥ���ַ���7
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","250101");//Ԥ���ַ���10   �������  �������  û��������Ĭ��  250101
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }else if("C01008".equals(billType)){
//		  bval.put("DETAIL_RESERVE_C1",btvo.getCinventorycode());//Ԥ���ַ���1
		  bval.put("DETAIL_RESERVE_C1",map.get("cinventorycode"));//Ԥ���ַ���1
	      bval.put("DETAIL_RESERVE_C2","");//Ԥ���ַ���2
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3
	      bval.put("DETAIL_RESERVE_C4","");//Ԥ���ַ���4
	      bval.put("DETAIL_RESERVE_C5","");//Ԥ���ַ���5
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6
	      bval.put("DETAIL_RESERVE_C7","");//Ԥ���ַ���7
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","");//Ԥ���ַ���10
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }else{
		  bval.put("DETAIL_RESERVE_C1","");//Ԥ���ַ���1
	      bval.put("DETAIL_RESERVE_C2","");//Ԥ���ַ���2
	      bval.put("DETAIL_RESERVE_C3","");//Ԥ���ַ���3
	      bval.put("DETAIL_RESERVE_C4","");//Ԥ���ַ���4
	      bval.put("DETAIL_RESERVE_C5","");//Ԥ���ַ���5
	      bval.put("DETAIL_RESERVE_C6","");//Ԥ���ַ���6
	      bval.put("DETAIL_RESERVE_C7","");//Ԥ���ַ���7
	      bval.put("DETAIL_RESERVE_C8","");//Ԥ���ַ���8
	      bval.put("DETAIL_RESERVE_C9","");//Ԥ���ַ���9
	      bval.put("DETAIL_RESERVE_C10","250101");//Ԥ���ַ���10
	      bval.put("DETAIL_RESERVE_C11","");//Ԥ���ַ���11
	      bval.put("DETAIL_RESERVE_C12","");//Ԥ���ַ���12
	      bval.put("DETAIL_RESERVE_C13","");//Ԥ���ַ���13
	      bval.put("DETAIL_RESERVE_C14","");//Ԥ���ַ���14
	      bval.put("DETAIL_RESERVE_C15","");//Ԥ���ַ���15
	      bval.put("DETAIL_RESERVE_C16","");//Ԥ���ַ���16
	      bval.put("DETAIL_RESERVE_C17","");//Ԥ���ַ���17
	      bval.put("DETAIL_RESERVE_C18","");//Ԥ���ַ���18
	      bval.put("DETAIL_RESERVE_C19","");//Ԥ���ַ���19
	      bval.put("DETAIL_RESERVE_C20","");//Ԥ���ַ���20
	      bval.put("DETAIL_RESERVE_C21","");//Ԥ���ַ���21
	      bval.put("DETAIL_RESERVE_C22","");//Ԥ���ַ���22
	      bval.put("DETAIL_RESERVE_C23","");//Ԥ���ַ���23
	      bval.put("DETAIL_RESERVE_C24","");//Ԥ���ַ���24
	      bval.put("DETAIL_RESERVE_C25","");//Ԥ���ַ���25
	      bval.put("DETAIL_RESERVE_C26","");//Ԥ���ַ���26
	      bval.put("DETAIL_RESERVE_C27","");//Ԥ���ַ���27
	      bval.put("DETAIL_RESERVE_C28","");//Ԥ���ַ���28
	      bval.put("DETAIL_RESERVE_C29","");//Ԥ���ַ���29
	      bval.put("DETAIL_RESERVE_C30","");//Ԥ���ַ���30
	      bval.put("DETAIL_RESERVE_N1",0);//Ԥ��������1
	      bval.put("DETAIL_RESERVE_N2",0);//Ԥ��������2
	      bval.put("DETAIL_RESERVE_N3",0);//Ԥ��������3
	      bval.put("DETAIL_RESERVE_N4",0);//Ԥ��������4
	      bval.put("DETAIL_RESERVE_N5",0);//Ԥ��������5
	  }
	  
	return bval;	  
  }
  /**
   * ��ѯ����vo
   * @param cbill_bid
   * @param pk_corp
   * @return
   */
  public Map queryBody(String cbill_bid,String pk_corp){
	  	List list=null;
		Map map=new HashMap();
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select * from ia_bill_b where cbill_bid='"+cbill_bid+"' and pk_corp='"+pk_corp+"' and nvl(dr,0)=0";
		try {
			list = (List) query.executeQuery(sql, new MapListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map=(Map) list.get(0);
		return map;
  }
  
  /**
   * ��ѯ�������
   * @param type
   * @return
   */
  private String querychtype(String type){
		String chType="";
		  String sql = "select inv.invclasscode from bd_invmandoc man " +
		  		"inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc " +
		  		"inner join bd_invcl inv on bas.pk_invcl=inv.pk_invcl " +
		  		"where man.pk_invmandoc='"+type+"' and nvl(inv.dr,0)=0";
	      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	      try {
	    	  Object deptcode = receiving.executeQuery(sql, new ColumnProcessor());
	    	  chType=deptcode==null?"":deptcode.toString();
	      } catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
		return chType;
	}
  
  /**
   * ��ѯ���̱���
   * @param kscode
   * @return
   */
  private String queryks(String kscode){
		String ks="";
		  String sql = "select bas.custcode from bd_cumandoc man " +
		  		"inner join bd_cubasdoc bas on man.pk_cubasdoc=bas.pk_cubasdoc " +
		  		"where man.pk_cumandoc='"+kscode+"'";
	      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	      try {
	    	  Object deptcode = receiving.executeQuery(sql, new ColumnProcessor());
	    	  ks=deptcode==null?"":deptcode.toString();
	      } catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
		return ks;
	}
  
  //У���������
  public StringBuffer CheckData(List list,String val){
	  StringBuffer str = new StringBuffer();
	  for(int i=0;i<list.size();i++){
		  
		  Map<String,String> map=new HashMap<String,String>();
		  map = (Map) list.get(i);
		  if(map.get("vbillcode")==null||map.get("vbillcode").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�е��ݺ�Ϊ��");
		  }
		  if(map.get("cbilltypecode")==null||map.get("cbilltypecode").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�е�������Ϊ��");
		  }
		  if(map.get("pk_corp")==null||map.get("pk_corp").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�й�˾Ϊ��");
		  }
		  if(map.get("cbillid")==null||map.get("cbillid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�е��ݱ�ʶΪ��");
		  }
		  if(map.get("cdeptid")==null||map.get("cdeptid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�в��ű�ʶ Ϊ��");
		  }
		  /*if(map.get("cemployeeid")==null||map.get("cemployeeid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"����Ա��ʶΪ��");
		  }*/
		  if(map.get("coperatorid")==null||map.get("coperatorid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"���Ƶ��˱�ʶΪ��");
		  }
		  /*if(map.get("cdispatchid")==null||map.get("cdispatchid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"���շ���ʶΪ��");
		  }*/
		  /*else if(map.get("ccustomvendorid")==null||map.get("ccustomvendorid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�п��̱�ʶΪ��");
		  }else if(map.get("cbill_bid")==null||map.get("cbill_bid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�з�¼��ʶΪ��");
		  }else if(map.get("nmoney")==null||map.get("nmoney").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�н��Ϊ��"); 
		  }else if(map.get("nnumber")==null||map.get("nnumber").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"������Ϊ��"); 
		  }else if(map.get("cinventoryid")==null||map.get("cinventoryid").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"�д����ʶΪ��"); 
		  }else if(map.get("irownumber")==null||map.get("irownumber").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"���к�Ϊ��");
		  }else if(map.get("bestimateflag")==null||map.get("bestimateflag").length()==0){
			  str.append("\n"+"���ݺ�"+val+"�е�"+i+1+"���ݹ���ʶΪ��");
		  }*/
		  
	  }
	  return str;
	  
  }


}